package org.wystriframework.ui.component.fileupload;

import java.io.File;

import org.apache.commons.fileupload.disk.DiskFileItem;

public class CustomDiskFileItem extends DiskFileItem {
    public CustomDiskFileItem(String fieldName, String contentType, boolean isFormField, String fileName, int sizeThreshold, File repository) {
        super(fieldName, contentType, isFormField, fileName, sizeThreshold, repository);
    }
    @Override
    public File getTempFile() {
        return super.getTempFile();
    }
}