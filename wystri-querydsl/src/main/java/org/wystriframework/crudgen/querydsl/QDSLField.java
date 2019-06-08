package org.wystriframework.crudgen.querydsl;

import static java.util.Arrays.*;
import static org.apache.commons.lang3.StringUtils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.wystriframework.core.definition.FieldMetadata;
import org.wystriframework.core.definition.IConstraint;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IFieldDelegate;
import org.wystriframework.core.definition.IOptionsProvider;
import org.wystriframework.core.definition.IRecord;

import com.querydsl.core.types.Path;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.RelationalPath;

public class QDSLField<E, F> implements IField<E, F> {

    private final QDSLEntity<E>                entity;
    private final Path<F>                      path;
    private String                             name;
    private final FieldMetadata                metadata    = new FieldMetadata();
    private final List<IConstraint<? super F>> constraints = new ArrayList<>();
    private IFieldDelegate<E, F>               delegate;

    public static <E extends RelationalPath<E>, F> QDSLField<E, F> newSimpleField(QDSLEntity<E> entity, Path<F> columnPath) {
        return new QDSLField<>(entity, columnPath).withDefaultConstraints();
    }

    public QDSLField(QDSLEntity<E> entity, Path<F> path) {
        this.entity = entity;
        this.path = path;
    }

    @Override
    public boolean isRequired(IRecord<E> record) {
        return !entity.getEntityPath().getMetadata(path).isNullable();
    }

    @Override
    public String requiredError(IRecord<E> record) {
        return null;
    }

    @Override
    public boolean isEnabled(IRecord<E> record) {
        return true;
    }

    @Override
    public boolean isVisible(IRecord<E> record) {
        return true;
    }

    @Override
    public List<IConstraint<? super F>> getConstraints(IRecord<E> record) {
        return constraints;
    }

    @Override
    public FieldMetadata getMetadata() {
        return this.metadata;
    }

    @Override
    public IFieldDelegate<E, F> getDelegate() {
        return delegate;
    }

    public QDSLField<E, F> setDelegate(IFieldDelegate<E, F> delegate) {
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

    @Override
    public Optional<? extends IOptionsProvider<E, F>> getOptionsProvider() {
        return Optional.empty();
    }

    //@formatter:off
    @Override public QDSLEntity<E> getEntity()  { return entity  ; }
    @Override public String        getName()    { return name    ; }
              public Path<F>       getPath()    { return path    ; }
    public QDSLField<E, F> setName    (String      name) { this.name     = name    ; return this; }
    //@formatter:on

    @Override
    @SuppressWarnings("unchecked")
    public Class<F> getType() {
        return (Class<F>) path.getType();
    }

    private String resolveName(ColumnMetadata cmeta) {
        return capitalize(defaultString(cmeta.getName()).replaceAll("[_-]+", " ").toLowerCase());
    }

    private Collection<? extends IConstraint<F>> resolveConstraints() {
        return asList();
    }
}
