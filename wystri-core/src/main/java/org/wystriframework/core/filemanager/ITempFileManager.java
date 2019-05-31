package org.wystriframework.core.filemanager;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.wystriframework.core.definition.IFileRef;

public interface ITempFileManager extends Closeable {

    IFileRef createTempFile(String name, InputStream content) throws IOException;
    IFileRef moveAsTempFile(String name, File content) throws IOException;
    
    IFileRef get(String id);

}