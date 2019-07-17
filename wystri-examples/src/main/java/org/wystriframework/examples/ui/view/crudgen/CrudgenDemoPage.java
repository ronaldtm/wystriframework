package org.wystriframework.examples.ui.view.crudgen;

import static java.util.Arrays.*;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.danekja.java.util.function.serializable.SerializablePredicate;
import org.wystriframework.annotation.Action;
import org.wystriframework.annotation.ConstraintFor;
import org.wystriframework.annotation.CustomView;
import org.wystriframework.annotation.Field;
import org.wystriframework.annotation.Selection;
import org.wystriframework.annotation.constraints.Length;
import org.wystriframework.annotation.constraints.Range;
import org.wystriframework.annotation.impl.ActionType;
import org.wystriframework.annotation.impl.AnnotatedRecord;
import org.wystriframework.annotation.impl.Bool;
import org.wystriframework.core.definition.IConstrainable;
import org.wystriframework.core.definition.IFileRef;
import org.wystriframework.core.definition.IOptionsProvider;
import org.wystriframework.core.definition.IRecord;
import org.wystriframework.core.wicket.WystriConfiguration;
import org.wystriframework.form.crudgen.view.wicket.CrudgenPanel;
import org.wystriframework.form.formbuilder.EntityFormProcessorBehavior;
import org.wystriframework.form.formbuilder.appenders.BooleanFieldAppender;
import org.wystriframework.ui.bootstrap.BSAlertFeedback;

public class CrudgenDemoPage extends WebPage {
    private static final long serialVersionUID = 1L;

    public enum Sexo {
        Masculino, Feminino;
    }

    public CrudgenDemoPage(final PageParameters parameters) {
        super(parameters);

        final BSAlertFeedback feedback = new BSAlertFeedback("feedback");

        final Form<?> form = new Form<Void>("form") {};

        form.setMultiPart(true);

        add(form

            .add(feedback)

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

    public static class Person implements Serializable {

        @Field(
            requiredIf = IsNotAnnonymous.class, requiredError = "Campo obrigatório",
            enabledIf = IsNotAnnonymous.class, disabledDefaultValue = "")
        @Length(min = 10, max = 40)
        public String   name;

        @Field
        public boolean  annonymous;

        @Field(label = "Foto do perfil",
            enabledIf = IsNotAnnonymous.class, disabledDefaultValue = "")
        public IFileRef profilePhoto;

        @Field(required = Bool.TRUE, requiredError = "Campo obrigatório")
        @Range(rangeExpression = "[1..99999]")
        public int      matricula;

        @Field(label = "Situação")
        @CustomView(appender = BooleanFieldAppender.class, appenderArgs = { "Yep", "Nope", "Wut?" })
        public Boolean  situacao;

        @Field
        @Selection(options = {
            @Selection.Option(id = "1", value = "Janeiro"),
            @Selection.Option(id = "2", value = "Fevereiro"),
            @Selection.Option(id = "3", value = "Março"),
            @Selection.Option(id = "4", value = "Abril"),
            @Selection.Option(id = "5", value = "Maio"),
            @Selection.Option(id = "6", value = "Junho"),
            @Selection.Option(id = "7", value = "Julho"),
            @Selection.Option(id = "8", value = "Agosto"),
            @Selection.Option(id = "9", value = "Setembro"),
            @Selection.Option(id = "10", value = "Outubro"),
            @Selection.Option(id = "11", value = "Novembro"),
            @Selection.Option(id = "12", value = "Dezembro"),
        })
        public Integer  monthOfBirth;

        @Field
        @Selection({ "M", "F" })
        public String   sex;

        @Field
        @Selection(provider = GenderOptionsProvider.class)
        public String   gender;

        @Action(type = ActionType.PRIMARY)
        public void executar() {

        }

        @ConstraintFor("matricula")
        public void validateMatricula(IConstrainable<Integer> c) {
            if (c.getValue() % 2 == 0)
                c.error("A matrícula deve ser ímpar: " + c.getValue());
        }

        public static class IsNotAnnonymous implements SerializablePredicate<Person> {
            @Override
            public boolean test(Person t) {
                return !t.annonymous;
            }
        }

        public static class GenderOptionsProvider<E> implements IOptionsProvider<E, String> {
            @Override
            public List<String> getOptions(IRecord<E> record) {
                return asList(
                    "Male to Female (MtF)",
                    "Female to Male (FtM)",
                    "Binary",
                    "Non",
                    "Genderfluid",
                    "Agender",
                    "Bigender",
                    "Polygender",
                    "Neutrois",
                    "Gender Apathetic",
                    "Androgyne",
                    "Intergender",
                    "Demigender",
                    "Greygender",
                    "Aporagender",
                    "Maverique",
                    "Novigender",
                    "Designated gender",
                    "AFAB",
                    "AMAB",
                    "Gender roles",
                    "Gender Presentation",
                    "Transitioning",
                    "Intersex",
                    "Dyadic",
                    "Trans Woman",
                    "Trans Man",
                    "Trans Feminine",
                    "Trans Masculine",
                    "Social Dysphoria",
                    "Body Dysphoria",
                    "Butch",
                    "Femme (Fem)",
                    "Binarism");
            }

            @Override
            public String objectToId(String object) {
                return object;
            }

            @Override
            public String idToObject(String id, List<? extends String> options) {
                return id;
            }

            @Override
            public String objectToDisplay(String object, List<? extends String> options) {
                return object;
            }
        }
    }
}
