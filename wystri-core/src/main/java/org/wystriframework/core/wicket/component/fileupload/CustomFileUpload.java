package org.wystriframework.core.wicket.component.fileupload;

import org.apache.wicket.markup.html.form.upload.FileUpload;

public class CustomFileUpload extends FileUpload {
    private final CustomDiskFileItem item;

    public CustomFileUpload(CustomDiskFileItem item) {
        super(item);
        this.item = item;
    }

    public CustomDiskFileItem getItem() {
        return item;
    }
}