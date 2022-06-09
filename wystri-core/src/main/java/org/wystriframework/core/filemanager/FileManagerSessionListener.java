package org.wystriframework.core.filemanager;

import java.io.IOException;

import org.apache.wicket.Application;
import org.apache.wicket.IInitializer;
import org.apache.wicket.ISessionListener;

public class FileManagerSessionListener implements IInitializer, ISessionListener {

    private Application application;

    @Override
    public void init(Application application) {
        this.application = application;
        application.getSessionListeners().add(this);
    }
    @Override
    public void destroy(Application application) {
        application.getSessionListeners().remove(this);
        this.application = null;
    }

    @Override
    public void onUnbound(String sessionId) {
        ITempFileManager tfm = SessionScopedTempFileManager.get(application, sessionId);
        try {
            tfm.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
