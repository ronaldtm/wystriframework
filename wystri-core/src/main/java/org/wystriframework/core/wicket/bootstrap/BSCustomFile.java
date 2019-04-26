package org.wystriframework.core.wicket.bootstrap;

import static org.wystriframework.core.wicket.component.jquery.JQuery.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.time.Duration;
import org.wystriframework.core.filemanager.SessionScopedTempFileManager;
import org.wystriframework.core.util.IFileRef;
import org.wystriframework.core.wicket.util.IBehaviorShortcutsMixin;
import org.wystriframework.core.wicket.util.IModelShortcutsMixin;

public class BSCustomFile extends FormComponentPanel<IFileRef> {

    private transient List<FileUpload> fileList;
    private IFileRef                   fileRef;

    private final Form<?>              form;
    private final FileUploadField      input;
    private final WebMarkupContainer   inputGroup;
    private final WebMarkupContainer   linkGroup;
    private final UploadBehavior       uploadBehavior;
    private final ResourceLink<Object> downloadLink;
    private final ClearLink            clearLink;
    private final BSProgressBar        progressBar;

    public BSCustomFile(String id) {
        this(id, null);
    }

    public BSCustomFile(String id, IModel<IFileRef> model) {
        super(id, model);

        form = new Form<>("form");
        form.setMultiPart(true);

        input = new FileUploadField("input", IModelShortcutsMixin.$m.getSet(this::getFileList, this::setFileList));
        uploadBehavior = new UploadBehavior("delayedsubmit");
        linkGroup = new WebMarkupContainer("linkGroup");
        downloadLink = new ResourceLink<>("link", new DownloadResource());
        clearLink = new ClearLink("clear");
        progressBar = new BSProgressBar("progress", form, input);
        inputGroup = new WebMarkupContainer("inputGroup");

        final IModel<String> fileDescription = () -> (getFileRef() == null) ? "Selecione..." : getFileDescription(getFileRef());

        add(form

            .add(linkGroup
                .add(downloadLink.setBody(fileDescription)
                    .add(IBehaviorShortcutsMixin.$b.attrReplace("target", "_" + getClass().getSimpleName() + "_" + downloadLink.getMarkupId())))
                .add(clearLink))

            .add(inputGroup
                .add(input
                    .setLabel(fileDescription)
                    .add(uploadBehavior)))

            .add(progressBar)

            .setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true)

            .add(new Behavior() {
                @Override
                public void onConfigure(Component component) {
                    boolean fileSelected = (getFileRef() != null);
                    inputGroup.setVisible(!fileSelected);
                    linkGroup.setVisible(fileSelected);
                }
            }));
    }

    protected void onUploadComplete(AjaxRequestTarget target) {}
    protected void onUploadError(AjaxRequestTarget target, Optional<? extends Exception> error) {}

    protected String getFileDescription(IFileRef fileRef) {
        if (fileRef == null)
            return null;
        return String.format("%s [%s]", fileRef.getName(), Bytes.bytes(fileRef.getSize()));
    }

    protected void onClear(AjaxRequestTarget target) {
        this.setFileRef(null);
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        if (this.getFileRef() == null)
            this.setFileRef(getModelObject());
    }

    //@formatter:off
    private List<FileUpload> getFileList() { return fileList; }
    private IFileRef         getFileRef()  { return fileRef ; }
    private BSCustomFile setFileList(List<FileUpload> fileList) { this.fileList = fileList; return this; }
    private BSCustomFile setFileRef (IFileRef          fileRef) { this.fileRef  = fileRef ; return this; }
    
    @Override public BSCustomFile setModel      (IModel<IFileRef> model) { return (BSCustomFile) super.setModel      (model ); }
    @Override public BSCustomFile setModelObject(IFileRef        object) { return (BSCustomFile) super.setModelObject(object); }
    //@formatter:on

    protected class UploadBehavior extends AjaxFormSubmitBehavior {
        protected UploadBehavior(String event) {
            super(form, event);
        }
        @Override
        protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
            super.updateAjaxAttributes(attributes);
            attributes.getAjaxCallListeners()
                .add(new AjaxCallListener()
                    .onBeforeSend($(input).trigger("beforesubmit")));
        }
        @Override
        public void renderHead(Component component, IHeaderResponse response) {
            super.renderHead(component, response);
            response.render(OnDomReadyHeaderItem.forScript(
                $(input)
                    .on("change", $(input).trigger("delayedsubmit").asFunction())
                    .on("click", $(input).each("function(){ this.value=null; }").asFunction())));
        }
        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            final IFileRef oldFileRef = BSCustomFile.this.getModelObject();
            if (oldFileRef != null && oldFileRef.isValid())
                oldFileRef.invalidate();

            if (getFileList() != null && !getFileList().isEmpty()) {
                final FileUpload fu = getFileList().get(0);
                try (final InputStream is = fu.getInputStream()) {
                    final SessionScopedTempFileManager fileman = SessionScopedTempFileManager.get(getSession());
                    fileRef = fileman.createTempFile(fu.getClientFileName(), is);

                    target.add(form);
                    onUploadComplete(target);

                } catch (IOException ex) {

                    onUploadError(target, Optional.of(ex));
                    throw new RuntimeException(ex.getMessage(), ex);
                }
            }
        }
        @Override
        protected void onError(AjaxRequestTarget target) {
            onUploadError(target, Optional.empty());
        }
    }

    protected class DownloadResource extends AbstractResource {
        @Override
        protected ResourceResponse newResourceResponse(Attributes attributes) {
            return new ResourceResponse()
                .setCacheDuration(Duration.NONE)
                .setFileName(getFileRef().getName())
                .setContentLength(getFileRef().getSize())
                .setContentDisposition(ContentDisposition.INLINE)
                .setWriteCallback(new WriteCallback() {
                    @Override
                    public void writeData(Attributes attributes) throws IOException {
                        try (OutputStream output = attributes.getResponse().getOutputStream(); InputStream input = getFileRef().openStream();) {
                            IOUtils.copy(input, output);
                        }
                    }
                });
        }
    }

    protected class ClearLink extends AjaxLink<Void> {
        protected ClearLink(String id) {
            super(id);
        }
        @Override
        public void onClick(AjaxRequestTarget target) {
            onClear(target);
            target.add(form);
        }
    }

}
