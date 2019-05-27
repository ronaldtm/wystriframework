package org.wystriframework.examples.ui.view.crudgen;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.danekja.java.util.function.serializable.SerializablePredicate;
import org.wystriframework.core.definition.IFileRef;
import org.wystriframework.core.formbuilder.EntityFormProcessorBehavior;
import org.wystriframework.core.wicket.WystriConfiguration;
import org.wystriframework.core.wicket.bootstrap.BSAlertFeedback;
import org.wystriframework.core.wicket.bootstrap.BSCustomFile;
import org.wystriframework.core.wicket.bootstrap.BSFormRowLayout;
import org.wystriframework.crudgen.annotation.AnnotatedRecord;
import org.wystriframework.crudgen.annotation.Bool;
import org.wystriframework.crudgen.annotation.Field;
import org.wystriframework.crudgen.annotation.FormLayout;
import org.wystriframework.crudgen.annotation.FormLayout.Cell;
import org.wystriframework.crudgen.annotation.FormLayout.Row;
import org.wystriframework.crudgen.view.wicket.CrudgenPanel;

public class CrudgenDemoPage extends WebPage {
    private static final long serialVersionUID = 1L;

    public enum Sexo {
        Masculino, Feminino;
    }

    public CrudgenDemoPage(final PageParameters parameters) {
        super(parameters);

        final Model<String> nome = new Model<>();
        final Model<Integer> codigo = new Model<>();
        final Model<Sexo> sexo = new Model<>();
        final Model<Boolean> aceite = new Model<>();
        final IModel<IFileRef> upload = new Model<>();

        final BSFormRowLayout layout = new BSFormRowLayout("layout");

        final BSCustomFile fUpload = new BSCustomFile("upload", upload);
        final BSAlertFeedback feedback = new BSAlertFeedback("feedback");

        final Form<?> form = new Form<Void>("form") {};

        form.setMultiPart(true);

        add(form

            .add(feedback)

            .add(layout

            //                .appendFormGroup(fg -> fg
            //                    .add(fUpload
            //                        .setLabel(() -> "Upload de arquivo...")))
            //
            //                .appendFormRow(fr -> fr
            //
            //                    .appendFormGroup(fg -> fg
            //                        .setGroupColSizes(BSColSize.col_2)
            //                        .add(new TextField<>("codigo", codigo)
            //                            .setRequired(true)
            //                            .setLabel(() -> "Código")
            //                            .add(BSValidationStatusBehavior.getInstance())))
            //
            //                    .appendFormGroup(fg -> fg
            //                        .setGroupColSizes(BSColSize.col_8)
            //                        .add(new TextField<>("nome", nome)
            //                            .setRequired(true)
            //                            .setLabel(() -> "Nome")
            //                            .add(BSValidationStatusBehavior.getInstance())
            //                            .add(FeedbackMessageUtils.keepMessage(FeedbackMessage.UNDEFINED, c -> "Mínimo de 10 caracteres")))) //
            //                )
            //
            //                .appendFormRow(row -> row
            //
            //                    .appendFormGroup(fg -> fg
            //                        .setGroupColSizes(BSColSize.col_4)
            //                        .add(new DropDownChoice<>("sexo", sexo, asList(Sexo.values()))
            //                            .setRequired(true)
            //                            .setLabel(() -> "Sexo")
            //                            .add(BSValidationStatusBehavior.getInstance()))) //
            //                )
            //
            //                .appendFormGroup(fg -> fg.setMode(BSFormGroup.Mode.CHECK)
            //                    .add(new CheckBox("aceite", aceite)
            //                        .setRequired(true)
            //                        .add(v -> {
            //                            if (!Boolean.TRUE.equals(v.getValue()))
            //                                v.error(new ValidationError("É necessário aceitar os termos de serviço para prosseguir"));
            //                        })
            //                        .setLabel(() -> "Aceito os termos de serviço")
            //                        .add(BSValidationStatusBehavior.getInstance())))

            )

            .add(new CrudgenPanel<>("crud", new Model<>(new AnnotatedRecord<>(new Person()))))

            .add(new AjaxButton("enviar", () -> "Enviar", form) {
                @Override
                protected void onSubmit(AjaxRequestTarget target) {
                    target.add(form);
                    target.appendJavaScript("console.log('submit');");
                }
                @Override
                protected void onError(AjaxRequestTarget target) {
                    target.add(form);
                    target.appendJavaScript("console.log('error');");
                }
            })

            .add(new EntityFormProcessorBehavior()));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptHeaderItem.forReference(getApplication().getJavaScriptLibrarySettings().getJQueryReference()));
        WystriConfiguration.get().getHeaderContributor().renderHead(response);
    }

    @FormLayout({
        //@formatter:off
        @Row({ @Cell(value = "name"     , spec = "col-md-8"), @Cell("annonymous") }),
        @Row({ @Cell(value = "matricula", spec = "col-md-4") }),
        //@formatter:on
    })
    public static class Person implements Serializable {

        @Field(
            requiredIf = IsNotAnnonymous.class, requiredError = "Campo obrigatório",
            enabledIf = IsNotAnnonymous.class)
        public String name;

        @Field(required = Bool.TRUE, requiredError = "Campo obrigatório")
        public int    matricula;

        @Field
        boolean       annonymous;

        public static class IsNotAnnonymous implements SerializablePredicate<Person> {
            @Override
            public boolean test(Person t) {
                return !t.annonymous;
            }
        }
    }
}
