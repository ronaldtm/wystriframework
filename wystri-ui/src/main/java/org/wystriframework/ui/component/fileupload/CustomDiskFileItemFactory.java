package org.wystriframework.ui.component.fileupload;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.FileCleaningTracker;
import org.apache.wicket.Application;
import org.apache.wicket.util.file.FileCleanerTrackerAdapter;

public class CustomDiskFileItemFactory extends DiskFileItemFactory {
    @Override
    public FileCleaningTracker getFileCleaningTracker() {
        return new FileCleanerTrackerAdapter(Application.get().getResourceSettings().getFileCleaner());
    }
    @Override
    public FileItem createItem(String fieldName, String contentType, boolean isFormField, String fileName) {
        //return super.createItem(fieldName, contentType, isFormField, fileName);
        CustomDiskFileItem result = new CustomDiskFileItem(fieldName, contentType, isFormField, fileName, getSizeThreshold(), getRepository());
        result.setDefaultCharset(getDefaultCharset());
        FileCleaningTracker tracker = getFileCleaningTracker();
        if (tracker != null) {
            tracker.track(result.getTempFile(), result);
        }
        return result;
    }
}