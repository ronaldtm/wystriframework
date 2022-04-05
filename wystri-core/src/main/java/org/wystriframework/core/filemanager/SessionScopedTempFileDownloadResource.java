package org.wystriframework.core.filemanager;

import static org.apache.commons.lang3.StringUtils.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;

import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.Application;
import org.apache.wicket.Session;
import org.apache.wicket.SharedResources;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wystriframework.core.definition.IFileRef;

@SuppressWarnings("serial")
public class SessionScopedTempFileDownloadResource extends AbstractResource {

	private static final Logger log = LoggerFactory.getLogger(SessionScopedTempFileDownloadResource.class);

    public static final synchronized ResourceReference getReference(Application application) {
        final SharedResources sharedResources = application.getSharedResources();
        final String sharedResourceName = "SessionScopedTempFileDownloadResource";
        ResourceReference resRef = sharedResources.get(sharedResourceName);
        if (resRef == null)
            sharedResources.add(sharedResourceName, new SessionScopedTempFileDownloadResource());
        return sharedResources.get(sharedResourceName);
    };

    @Override
    protected ResourceResponse newResourceResponse(Attributes attributes) {
        final IRequestParameters params = attributes.getRequest().getQueryParameters();
        final String id = params.getParameterValue("id").toOptionalString();

        if (isNotBlank(id) && Session.exists()) {
            final ITempFileManager fileManager = SessionScopedTempFileManager.get(Session.get());
            final IFileRef fileRef = fileManager.get(id);

            if (fileRef != null) {
                final ResourceResponse resp = new ResourceResponse()
                    .setContentDisposition(ContentDisposition.INLINE)
                    .setCacheDuration(Duration.ZERO);

                return resp
                    .setFileName(fileRef.getName())
                    .setContentLength(fileRef.getSize())
                    .setContentType(fileRef.getMimeType())
                    .setWriteCallback(new WriteCallback() {
                        @Override
                        public void writeData(Attributes attributes) throws IOException {
                            try (OutputStream output = attributes.getResponse().getOutputStream(); InputStream input = fileRef.openStream();) {
                                IOUtils.copy(input, output);
                            } catch (Exception ex) {
                                log.trace(ex.getMessage(), ex);
                            }
                        }
                    });
            } else {
                return new ResourceResponse().setError(HttpServletResponse.SC_NOT_FOUND);
            }
        }

        return new ResourceResponse().setError(HttpServletResponse.SC_BAD_REQUEST);
    }
}
