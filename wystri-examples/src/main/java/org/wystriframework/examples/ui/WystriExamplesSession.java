package org.wystriframework.examples.ui;

import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

@SuppressWarnings("serial")
public class WystriExamplesSession extends WebSession {

    public WystriExamplesSession(Request request) {
        super(request);
    }
}
