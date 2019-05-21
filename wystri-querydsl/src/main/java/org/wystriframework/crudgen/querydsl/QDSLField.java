package org.wystriframework.crudgen.querydsl;

import static java.util.Arrays.*;
import static org.apache.commons.lang3.StringUtils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.wystriframework.core.definition.FieldMetadata;
import org.wystriframework.core.definition.IConstraint;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IFieldDelegate;

import com.querydsl.core.types.Path;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.RelationalPath;

public class QDSLField<E, F> implements IField<F> {

    private final QDSLEntity<E>                entity;
    private final Path<F>                      path;
    private String                             name;
    private final FieldMetadata                metadata    = new FieldMetadata();
    private final List<IConstraint<? super F>> constraints = new ArrayList<>();
    private IFieldDelegate<F>                  delegate;

    public static <E extends RelationalPath<E>, F> QDSLField<E, F> newSimpleField(QDSLEntity<E> entity, Path<F> columnPath) {
        return new QDSLField<>(entity, columnPath).withDefaultConstraints();
    }

    public QDSLField(QDSLEntity<E> entity, Path<F> path) {
        this.entity = entity;
        this.path = path;
    }

    @Override
    public List<IConstraint<? super F>> getConstraints() {
        return constraints;
    }

    @Override
    public FieldMetadata getMetadata() {
        return this.metadata;
    }

    @Override
    public IFieldDelegate<F> getDelegate() {
        return delegate;
    }

    public QDSLField<E, F> setDelegate(IFieldDelegate<F> delegate) {
        this.delegate = delegate;
        return this;
    }

    public QDSLField<E, F> withDefaultConstraints() {
        ColumnMetadata cmeta = entity.getEntityPath().getMetadata(path);
        setName(resolveName(cmeta));
        setConstraints(resolveConstraints()); //

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
              public Path<F>       getPath()    { return path    ; }
    public QDSLField<E, F> setName    (String      name) { this.name     = name    ; return this; }
    //@formatter:on

    @Override
    public Class<? extends F> getType() {
        return path.getType();
    }

    private String resolveName(ColumnMetadata cmeta) {
        return capitalize(defaultString(cmeta.getName()).replaceAll("[_-]+", " ").toLowerCase());
    }

    private Collection<? extends IConstraint<F>> resolveConstraints() {
        return asList();
    }
}
