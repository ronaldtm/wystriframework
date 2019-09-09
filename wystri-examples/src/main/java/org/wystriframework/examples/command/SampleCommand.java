package org.wystriframework.examples.command;

import static java.util.Arrays.*;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.danekja.java.util.function.serializable.SerializablePredicate;
import org.wystriframework.annotation.Action;
import org.wystriframework.annotation.Bool;
import org.wystriframework.annotation.ConstraintFor;
import org.wystriframework.annotation.CustomView;
import org.wystriframework.annotation.Field;
import org.wystriframework.annotation.Selection;
import org.wystriframework.annotation.constraints.Length;
import org.wystriframework.annotation.constraints.Range;
import org.wystriframework.annotation.impl.ActionType;
import org.wystriframework.core.definition.IConstrainable;
import org.wystriframework.core.definition.IFileRef;
import org.wystriframework.core.definition.IOptionsProvider;
import org.wystriframework.core.definition.IRecord;
import org.wystriframework.form.formbuilder.appenders.BooleanFieldAppender;

public class SampleCommand implements Serializable {

    @Field(
        requiredIf = SampleCommand.IsNotAnnonymous.class, requiredError = "Campo obrigatório",
        enabledIf = SampleCommand.IsNotAnnonymous.class, disabledDefaultValue = "")
    @Length(min = 10, max = 40)
    public String   name;

    @Field
    public boolean  annonymous;

    @Field(label = "Foto do perfil",
        enabledIf = SampleCommand.IsNotAnnonymous.class, disabledDefaultValue = "")
    public IFileRef profilePhoto;

    @Field(label = "Situação")
    @CustomView(appender = BooleanFieldAppender.class, appenderArgs = { "Yep", "Nope", "Wut?" })
    private Boolean situacao;

    @Field(required = Bool.TRUE, requiredError = "Campo obrigatório")
    @Range(rangeExpression = "[1..99999]")
    public int      matricula;

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

    @Selection({ "M", "F" })
    public String   sex;

    @Selection(provider = SampleCommand.GenderOptionsProvider.class)
    public String   gender;

    public Boolean getSituacao() {
        return situacao;
    }
    public SampleCommand setSituacao(Boolean situacao) {
        this.situacao = situacao;
        this.matricula++;
        return this;
    }

    @Action(type = ActionType.PRIMARY)
    public void executar() {
        System.out.println(ToStringBuilder.reflectionToString(this));
    }

    @ConstraintFor("matricula")
    public void validateMatricula(IConstrainable<Integer> c) {
        if (c.getValue() % 2 == 0)
            c.error("A matrícula deve ser ímpar: " + c.getValue());
    }

    public static class IsNotAnnonymous implements SerializablePredicate<SampleCommand> {
        @Override
        public boolean test(SampleCommand t) {
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
                "Non-Binary",
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