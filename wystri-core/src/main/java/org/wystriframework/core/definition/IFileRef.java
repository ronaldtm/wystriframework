package org.wystriframework.core.definition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public interface IFileRef extends Serializable {

    String getId();
    String getName();
    long getSize();
    String getMimeType();

    InputStream openStream() throws IOException;
    void moveContentTo(File destination) throws IOException;

    boolean isValid();
    boolean isTemporary();
    void invalidate();
}
