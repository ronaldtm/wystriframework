package org.wystriframework.form.formbuilder.appenders;

import org.apache.wicket.markup.html.form.FormComponent;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IFileRef;
import org.wystriframework.form.formbuilder.AbstractFieldComponentAppender;
import org.wystriframework.form.formbuilder.FieldComponentContext;
import org.wystriframework.ui.bootstrap.BSCustomFileField;

@SuppressWarnings("serial")
public class FileUploadFieldAppender extends AbstractFieldComponentAppender<IFileRef> {

    @Override
    protected <E> FormComponent<IFileRef> newFormComponent(FieldComponentContext<E, IFileRef> ctx) {
        final IField<E, IFileRef> field = (IField<E, IFileRef>) ctx.getField();

        return new BSCustomFileField(field.getName(), ctx.getRecord().field(field));
    }
}
