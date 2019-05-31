package org.wystriframework.core.formbuilder.appenders;

import org.apache.wicket.markup.html.form.FormComponent;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IFileRef;
import org.wystriframework.core.formbuilder.AbstractFieldComponentAppender;
import org.wystriframework.core.formbuilder.FieldComponentContext;
import org.wystriframework.core.wicket.bootstrap.BSCustomFile;

public class FileUploadFieldAppender extends AbstractFieldComponentAppender<IFileRef> {

    @Override
    protected FormComponent<IFileRef> newFormComponent(FieldComponentContext<IFileRef> ctx) {
        final IField<IFileRef> field = (IField<IFileRef>) ctx.getField();

        return new BSCustomFile(field.getName(), ctx.getRecord().field(field));
    }
}
