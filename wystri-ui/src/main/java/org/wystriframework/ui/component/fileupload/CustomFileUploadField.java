package org.wystriframework.ui.component.fileupload;

import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.IMultipartWebRequest;
import org.apache.wicket.request.Request;
import org.apache.wicket.util.string.Strings;

/**
 * Form component that corresponds to a &lt;input type=&quot;file&quot;&gt;. When a FileInput
 * component is nested in a {@link org.apache.wicket.markup.html.form.Form}, that has multipart ==
 * true, its model is updated with the {@link org.apache.wicket.markup.html.form.upload.FileUpload}
 * for this component.
 * <p>
 * <strong>NOTE</strong>The model of this component is reset with {@code null} at the end of the
 * request because {@link FileUpload} instances do not survive across requests since the input
 * streams they point to will be closed. Because of this, the {@link FileUpload} instance should be
 * processed within the same request as the form containing it was submitted.
 * </p>
 * 
 * @see FileUploadField
 */
@SuppressWarnings("serial")
public class CustomFileUploadField extends FileUploadField {
    public CustomFileUploadField(final String id) {
        super(id);
    }

    public CustomFileUploadField(final String id, IModel<? extends List<FileUpload>> model) {
        super(id, model);
    }

    /**
     * @return a list of all uploaded files. The list is empty if no files were selected. It will return more than one files if:
     *         <ul>
     *         <li>HTML5 &lt;input type="file" <strong>multiple</strong> /&gt; is used</li>
     *         <li>the browser supports <em>multiple</em> attribute</li>
     *         <li>the user has selected more than one files from the <em>Select file</em> dialog</li>
     *         </ul>
     */
    @Override
    public List<FileUpload> getFileUploads()
    {
        final List<FileUpload> fileUploads = super.getFileUploads();
        if (fileUploads.stream().allMatch(it -> it instanceof CustomFileUpload))
        {
            return fileUploads;
        }
        else
        {
            fileUploads.clear();
    
            // Get request
            final Request request = getRequest();
    
            // If we successfully installed a multipart request
            if (request instanceof IMultipartWebRequest)
            {
                // Get the item for the path
                final List<FileItem> fileItems = ((IMultipartWebRequest)request).getFile(getInputName());
    
                if (fileItems != null)
                {
                    for (FileItem item : fileItems)
                    {
                        // WICKET-6270 detect empty field by missing file name
                        if (Strings.isEmpty(item.getName()) == false) {
                            if (item instanceof CustomDiskFileItem)
                            {
                                fileUploads.add(new CustomFileUpload((CustomDiskFileItem) item));
                            }
                            else
                            {
                                fileUploads.add(new FileUpload(item));
                            }
                        }
                    }
                }
            }
        }
        
        return fileUploads;
    }
}
