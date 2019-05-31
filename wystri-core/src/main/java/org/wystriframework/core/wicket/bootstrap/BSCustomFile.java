package org.wystriframework.core.wicket.bootstrap;

import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;
import static org.wystriframework.core.wicket.component.jquery.JQuery.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.fileupload.FileItem;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Bytes;
import org.wystriframework.core.definition.IFileRef;
import org.wystriframework.core.filemanager.ITempFileManager;
import org.wystriframework.core.filemanager.SessionScopedTempFileDownloadResource;
import org.wystriframework.core.wicket.WystriConfiguration;
import org.wystriframework.core.wicket.component.fileupload.CustomDiskFileItem;
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
    private final AbstractLink         downloadLink;
    private final ClearLink            clearLink;
    private final BSProgressBar        progressBar;
    private final Set<String>          acceptedFileTypes = new LinkedHashSet<>();

    public BSCustomFile(String id) {
        this(id, null);
    }

    public BSCustomFile(String id, IModel<IFileRef> model) {
        super(id, model);

        form = new Form<>("form");
        form.setMultiPart(true);

        input = new CustomFileUploadField("input", IModelShortcutsMixin.$m.getSet(this::getFileList, this::setFileList));
        uploadBehavior = new UploadBehavior("delayedsubmit");
        linkGroup = new WebMarkupContainer("linkGroup");
        downloadLink = newDownloadLink("link");
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

    protected AbstractLink newDownloadLink(String id) {
        return new ExternalLink(id, IModel.of(() -> {
            return (getFileRef() != null)
                ? urlFor(SessionScopedTempFileDownloadResource.getReference(getApplication()), new PageParameters().set("id", getFileRef().getId()))
                : "";
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

    public BSCustomFile setAcceptedFileTypes(Collection<String> types) {
        this.acceptedFileTypes.clear();
        this.acceptedFileTypes.addAll(types);
        return this;
    }

    public BSCustomFile setAcceptedFileTypes(String... types) {
        return setAcceptedFileTypes(asList(types));
    }

    public Set<String> getAcceptedFileTypes() {
        return Collections.unmodifiableSet(acceptedFileTypes);
    }

    //@formatter:off
    private List<FileUpload> getFileList() { return fileList; }
    private IFileRef         getFileRef()  { return fileRef ; }
    private BSCustomFile setFileList(List<FileUpload> fileList) { this.fileList = fileList; return this; }
    private BSCustomFile setFileRef (IFileRef          fileRef) { this.fileRef  = fileRef ; return this; }
    
    @Override public BSCustomFile setModel      (IModel<IFileRef> model) { return (BSCustomFile) super.setModel      (model ); }
    @Override public BSCustomFile setModelObject(IFileRef        object) { return (BSCustomFile) super.setModelObject(object); }
    //@formatter:on

    protected final class CustomFileUploadField extends FileUploadField {
        protected CustomFileUploadField(String id, IModel<? extends List<FileUpload>> model) {
            super(id, model);
        }
        @Override
        protected void onComponentTag(ComponentTag tag) {
            super.onComponentTag(tag);
            if (!acceptedFileTypes.isEmpty())
                tag.put("accept", acceptedFileTypes.stream().collect(joining(",")));
        }
    }

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
                final ITempFileManager fileman = WystriConfiguration.get().getTempFileManager();
                final FileUpload fu = getFileList().get(0);

                try {
                    final Optional<File> file = resolveFile(fu);
                    if (file.isPresent()) {
                        fileRef = fileman.moveAsTempFile(fu.getClientFileName(), file.get());
                    } else {
                        try (InputStream is = fu.getInputStream()) {
                            fileRef = fileman.createTempFile(fu.getClientFileName(), is);
                        }
                    }

                    target.add(form);
                    onUploadComplete(target);

                } catch (SecurityException | IllegalArgumentException | IOException ex) {
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

    private static Optional<File> resolveFile(final FileUpload fu) {
        try {
            Field itemField = FileUpload.class.getDeclaredField("item");
            itemField.setAccessible(true);
            final FileItem fileItem = (FileItem) itemField.get(fu);

            if (fileItem instanceof CustomDiskFileItem)
                return Optional.of(((CustomDiskFileItem) fileItem).getTempFile());
            else
                return Optional.empty();

        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            return Optional.empty();
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
