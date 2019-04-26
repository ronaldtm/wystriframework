package org.wystriframework.crudgen.querydsl;

import static java.util.Arrays.*;
import static org.apache.commons.lang3.StringUtils.*;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.wystriframework.core.definition.FieldMetadata;
import org.wystriframework.core.definition.FieldType;
import org.wystriframework.core.definition.IConstraint;
import org.wystriframework.core.definition.IField;
import org.wystriframework.crudgen.base.Constraints;

import com.querydsl.core.types.Path;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.RelationalPath;

public class QDSLField<E, F> implements IField<F> {

    private final QDSLEntity<E>        entity;
    private final Path<F>              path;
    private FieldType                  type;
    private String                     name;
    private boolean                    required;
    private final FieldMetadata        metadata    = new FieldMetadata();
    private final List<IConstraint<F>> constraints = new ArrayList<>();

    public static <E extends RelationalPath<E>, F> QDSLField<E, F> newSimpleField(QDSLEntity<E> entity, Path<F> columnPath) {
        return new QDSLField<>(entity, columnPath).withDefaultConstraints();
    }

    public QDSLField(QDSLEntity<E> entity, Path<F> path) {
        this.entity = entity;
        this.path = path;
    }

    @Override
    public List<IConstraint<F>> getConstraints() {
        return constraints;
    }

    @Override
    public FieldMetadata getMetadata() {
        return this.metadata;
    }

    public QDSLField<E, F> withDefaultConstraints() {
        ColumnMetadata cmeta = entity.getEntityPath().getMetadata(path);
        setType(resolveFieldType(cmeta));
        setName(resolveName(cmeta));
        setRequired(!cmeta.isNullable());
        setConstraints(resolveConstraints(cmeta, getType()));

        cmeta.getDigits();
        cmeta.getSize();

        return this;
    }

    public void addConstraint(IConstraint<F> constraint) {
        this.constraints.add(constraint);
    }
    public void setConstraints(Collection<? extends IConstraint<F>> constraints) {
        this.constraints.clear();
        this.constraints.addAll(constraints);
    }

    //@formatter:off
    @Override public QDSLEntity<E> getEntity()  { return entity  ; }
    @Override public String        getName()    { return name    ; }
    @Override public FieldType     getType()    { return type    ; }
              public Path<F>       getPath()    { return path    ; }
              public boolean       isRequired() { return required; }
    public QDSLField<E, F> setType    (FieldType   type) { this.type     = type    ; return this; }
    public QDSLField<E, F> setName    (String      name) { this.name     = name    ; return this; }
    public QDSLField<E, F> setRequired(boolean required) { this.required = required; return this; }
    //@formatter:on

    private String resolveName(ColumnMetadata cmeta) {
        return capitalize(defaultString(cmeta.getName()).replaceAll("[_-]+", " ").toLowerCase());
    }

    private Collection<? extends IConstraint<F>> resolveConstraints(ColumnMetadata cmeta, FieldType type) {
        switch (type) {
            case CHAR:
            case INTEGER:
            case STRING:
            case LONG_STRING:
            case DECIMAL:
                return asList(Constraints.maxLength(cmeta.getSize()));

            case BOOLEAN:
            case DATE:
            case TIME:
            case TIMESTAMP:
            case FILE:
            case MONEY:
                return asList();
        }
        return asList();
    }

    private static FieldType resolveFieldType(ColumnMetadata cmeta) {
        switch (cmeta.getJdbcType()) {
            case Types.VARCHAR:
            case Types.NVARCHAR:
                return FieldType.STRING;

            case Types.LONGNVARCHAR:
            case Types.LONGVARCHAR:
            case Types.NCLOB:
            case Types.CLOB:
                return FieldType.LONG_STRING;

            case Types.NCHAR:
            case Types.CHAR:
                return FieldType.CHAR;

            case Types.BIGINT:
            case Types.INTEGER:
            case Types.NUMERIC:
            case Types.TINYINT:
            case Types.SMALLINT:
                return FieldType.INTEGER;

            case Types.DECIMAL:
            case Types.DOUBLE:
            case Types.FLOAT:
            case Types.REAL:
                return FieldType.DECIMAL;

            case Types.BIT:
            case Types.BOOLEAN:
                return FieldType.BOOLEAN;

            case Types.LONGVARBINARY:
            case Types.BINARY:
            case Types.BLOB:
            case Types.VARBINARY:
                return FieldType.FILE;

            case Types.DATE:
                return FieldType.DATE;

            case Types.TIME:
            case Types.TIME_WITH_TIMEZONE:
                return FieldType.TIME;

            case Types.TIMESTAMP:
            case Types.TIMESTAMP_WITH_TIMEZONE:
                return FieldType.TIMESTAMP;

            case Types.ARRAY:
            case Types.DATALINK:
            case Types.DISTINCT:
            case Types.JAVA_OBJECT:
            case Types.OTHER:
            case Types.REF:
            case Types.REF_CURSOR:
            case Types.ROWID:
            case Types.SQLXML:
            case Types.STRUCT:
            case Types.NULL:
            default:
                return null;
        }
    }
}
