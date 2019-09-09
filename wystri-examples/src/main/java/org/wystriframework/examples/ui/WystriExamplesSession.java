package org.wystriframework.examples.ui;

import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

public class WystriExamplesSession extends WebSession {

    public WystriExamplesSession(Request request) {
        super(request);
    }
}
