package org.wystriframework.core.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public interface IFileRef extends Serializable {

    String getName();
    long getSize();
    InputStream openStream() throws IOException;
    void moveContentTo(File destination) throws IOException;
    boolean isValid();
    boolean isTemporary();
    void invalidate();

    //    static IFileRef ofTempFile(File file, String name) {
    //        return new IFileRef() {
    //            @Override
    //            public String getName() {
    //                return file.getName();
    //            }
    //            @Override
    //            public long getSize() {
    //                return file.length();
    //            }
    //            @Override
    //            public InputStream openStream() throws IOException {
    //                return new FileInputStream(file);
    //            }
    //            @Override
    //            public void moveTo(File destination) throws IOException {
    //                Files.move(file.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
    //            }
    //            @Override
    //            public boolean isValid() {
    //                return file.exists();
    //            }
    //        };
    //    }
}
