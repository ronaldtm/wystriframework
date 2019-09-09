package org.wystriframework.ui.bootstrap;

import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;
import static org.apache.commons.lang3.StringUtils.*;
import static org.wystriframework.ui.component.jquery.JQuery.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
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
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.lang.Bytes;
import org.wystriframework.core.definition.IFileRef;
import org.wystriframework.core.filemanager.ITempFileManager;
import org.wystriframework.core.filemanager.SessionScopedTempFileDownloadResource;
import org.wystriframework.core.wicket.Wystri;
import org.wystriframework.ui.component.fileupload.CustomDiskFileItem;
import org.wystriframework.ui.component.fileupload.CustomFileUpload;
import org.wystriframework.ui.component.fileupload.CustomFileUploadField;
import org.wystriframework.ui.util.IBehaviorShortcutsMixin;

public class BSCustomFileField extends FormComponentPanel<IFileRef> {

    private static final String        KEY_CLEAR_ICON      = "BSCustomFileField.clear.icon";
    private static final String        KEY_SELECT_FILE     = "BSCustomFileField.select.label";
    private static final String        EVENT_DELAYEDSUBMIT = "delayedsubmit";

    private transient List<FileUpload> fileList;
    private IFileRef                   fileRef;
    private final Set<String>          acceptedFileTypes   = new LinkedHashSet<>();

    private final Form<?>              form;
    private final FileUploadField      input;

    public BSCustomFileField(String id) {
        this(id, null);
    }

    public BSCustomFileField(String id, IModel<IFileRef> model) {
        super(id, model);

        form = new Form<>("form");
        input = newFileUploadField("input", new FileListModel());

        final MarkupContainer linkGroup = new WebMarkupContainer("linkGroup");
        final MarkupContainer inputGroup = new WebMarkupContainer("inputGroup");
        final AbstractLink downloadLink = newDownloadLink("link");
        final UploadBehavior uploadBehavior = new UploadBehavior(form, EVENT_DELAYEDSUBMIT);
        final IModel<String> fileDescription = () -> (getFileRef() == null) ? getString(KEY_SELECT_FILE, null, "...") : getFileDescription(getFileRef());

        form.setMultiPart(true);

        add(form

            .add(linkGroup
                .add(downloadLink.setBody(fileDescription)
                    .add(IBehaviorShortcutsMixin.$b.attrReplace("target", "_" + getClass().getSimpleName() + "_" + downloadLink.getMarkupId())))
                .add(newClearLink("clearLink")
                    .add(newClearIcon("clearIcon"))))

            .add(inputGroup
                .add(input
                    .setLabel(fileDescription)
                    .add(uploadBehavior)))

            .add(new BSProgressBar("progress", form, input))

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

    protected void onClear(AjaxRequestTarget target) {
        this.setFileRef(null);
        target.add(form);
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        if (this.getFileRef() == null)
            this.setFileRef(getModelObject());
    }

    protected AbstractLink newDownloadLink(String id) {
        return new ExternalLink(id, () -> getDownloadURL());
    }
    protected Component newClearIcon(String id) {
        return new ClearIcon(id);
    }
    protected AbstractLink newClearLink(String id) {
        return new AjaxLink<Void>(id) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                onClear(target);
            }
        };
    }

    protected Optional<File> resolveFile(final FileUpload fu) {
        try {
            if (fu instanceof CustomFileUpload) {
                final CustomFileUpload cfu = (CustomFileUpload) fu;
                final CustomDiskFileItem fileItem = cfu.getItem();
                final File tempFile = fileItem.getTempFile();
                if (tempFile.canWrite())
                    return Optional.of(tempFile);
            }
            return Optional.empty();
        } catch (SecurityException | IllegalArgumentException ex) {
            return Optional.empty();
        }
    }
    public BSCustomFileField setAcceptedFileTypes(Collection<String> types) {
        this.acceptedFileTypes.clear();
        this.acceptedFileTypes.addAll(types);
        return this;
    }

    public BSCustomFileField setAcceptedFileTypes(String... types) {
        return setAcceptedFileTypes(asList(types));
    }

    public Set<String> getAcceptedFileTypes() {
        return Collections.unmodifiableSet(acceptedFileTypes);
    }

    protected String getDownloadURL() {
        return (getFileRef() != null)
            ? getDownloadUrl(getFileRef())
            : "javascript:alert('File not available')";
    }

    protected FileUploadField newFileUploadField(String id, IModel<List<FileUpload>> model) {
        return new BSCustomFileUploadField(id, model);
    }

    protected String getClearIconStyleClass() {
        return getString(KEY_CLEAR_ICON, null, "");
    }

    protected String getDownloadUrl(IFileRef fileRef) {
        final ResourceReference resRef = SessionScopedTempFileDownloadResource.getReference(getApplication());
        final PageParameters params = new PageParameters().set("id", fileRef.getId());
        return urlFor(resRef, params).toString();
    }

    protected String getFileDescription(IFileRef fileRef) {
        if (fileRef == null)
            return null;
        return String.format("%s [%s]", fileRef.getName(), Bytes.bytes(fileRef.getSize()));
    }

    //@formatter:off
    private List<FileUpload> getFileList() { return fileList; }
    private IFileRef         getFileRef()  { return fileRef ; }
    private BSCustomFileField setFileList(List<FileUpload> fileList) { this.fileList = fileList; return this; }
    private BSCustomFileField setFileRef (IFileRef          fileRef) { this.fileRef  = fileRef ; return this; }
    
    @Override public BSCustomFileField setModel      (IModel<IFileRef> model) { return (BSCustomFileField) super.setModel      (model ); }
    @Override public BSCustomFileField setModelObject(IFileRef        object) { return (BSCustomFileField) super.setModelObject(object); }
    //@formatter:on

    protected class ClearIcon extends Label {
        protected ClearIcon(String id) {
            super(id, "");
        }
        @Override
        protected void onComponentTag(ComponentTag tag) {
            super.onComponentTag(tag);
            tag.put("class", defaultString(tag.getAttribute("class")) + " " + getClearIconStyleClass());
        }
    }
    private final class FileListModel implements IModel<List<FileUpload>> {
        @Override
        public List<FileUpload> getObject() {
            return getFileList();
        }
        @Override
        public void setObject(List<FileUpload> object) {
            setFileList(object);
        }
    }

    protected final class BSCustomFileUploadField extends CustomFileUploadField {
        protected BSCustomFileUploadField(String id, IModel<? extends List<FileUpload>> model) {
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
        protected UploadBehavior(Form<?> form, String event) {
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
                    .on("change", $(input).trigger(EVENT_DELAYEDSUBMIT).asFunction())
                    .on("click", $(input).each("function(){ this.value=null; }").asFunction())));
        }
        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            final IFileRef oldFileRef = BSCustomFileField.this.getModelObject();
            if (oldFileRef != null && oldFileRef.isValid())
                oldFileRef.invalidate();

            if (getFileList() != null && !getFileList().isEmpty()) {
                final ITempFileManager fileman = Wystri.get().getTempFileManager();
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

                    target.add(this.getForm());
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
}
