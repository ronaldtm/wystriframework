package org.wystriframework.core.filemanager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.wicket.Application;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.Session;
import org.apache.wicket.util.io.Streams;
import org.wystriframework.core.definition.IFileRef;

public class SessionScopedTempFileManager implements ITempFileManager {

    private static final MetaDataKey<Map<String, SessionScopedTempFileManager>> APPLICATION_METADATA_KEY = new MetaDataKey<Map<String, SessionScopedTempFileManager>>() {};

    private static synchronized Map<String, SessionScopedTempFileManager> getFileManagers(Application application) {
        var map = application.getMetaData(APPLICATION_METADATA_KEY);
        if (map == null)
            application.setMetaData(APPLICATION_METADATA_KEY, map = new HashMap<>());
        return map;
    }

    public static synchronized ITempFileManager get(Session session) {
        return get(session.getApplication(), session.getId());
    }

    public static SessionScopedTempFileManager get(final Application application, final String sessionId) {
        final var map = getFileManagers(application);
        SessionScopedTempFileManager tfm = map.get(sessionId);
        if (tfm == null)
            map.put(sessionId, tfm = new SessionScopedTempFileManager(sessionId));
        return tfm;
    }

    private Set<FileEntry> files = new HashSet<>();
    private Path           baseFolder;

    public SessionScopedTempFileManager(String sessionId) {
        try {
            baseFolder = Files.createTempDirectory("_" + sessionId);
        } catch (IOException ex) {
            throw new IllegalStateException(ex.getMessage(), ex);
        }
    }

    @Override
    public void close() throws IOException {
        for (FileEntry entry : files)
            entry.invalidate();
    }

    @Override
    public IFileRef createTempFile(String name, InputStream content) throws IOException {
        final Path temp = Files.createTempFile(baseFolder, name + ".", ".tmp");
        try (OutputStream out = Files.newOutputStream(temp, StandardOpenOption.WRITE)) {
            Streams.copy(content, out);
        }
        return new FileEntry(name, temp.toString());
    }

    private static class FileEntry implements IFileRef {

        private final String name;
        private final String path;

        FileEntry(String name, String path) {
            this.name = name;
            this.path = path;
        }

        @Override
        public String getName() {
            return name;
        }
        @Override
        public long getSize() {
            try {
                return Files.size(getPath());
            } catch (IOException ex) {
                return -1;
            }
        }
        @Override
        public InputStream openStream() throws IOException {
            return Files.newInputStream(getPath(), StandardOpenOption.READ);
        }
        @Override
        public void moveContentTo(File destination) throws IOException {
            final Path destPath = (destination.isDirectory()) ? destination.toPath().resolve(getName()) : destination.toPath();
            Files.move(getPath(), destPath, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
        }
        @Override
        public void invalidate() {
            try {
                Files.deleteIfExists(getPath());
            } catch (IOException ex) {
                throw new RuntimeException(ex.getMessage(), ex);
            }
        }
        @Override
        public boolean isValid() {
            return Files.exists(getPath());
        }
        @Override
        public boolean isTemporary() {
            return true;
        }

        Path getPath() {
            return Paths.get(path);
        }

        @Override
        public String getMimeType() {
            return null;
        }
    }
}
