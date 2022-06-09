package org.wystriframework.querydsl;

import java.util.stream.Stream;

import org.wystriframework.core.definition.IEntity;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IFieldLayout;

import com.querydsl.sql.RelationalPath;

public class QDSLEntity<E> implements IEntity<E> {

    private final RelationalPath<E> entityPath;

    public QDSLEntity(RelationalPath<E> entityPath) {
        this.entityPath = entityPath;
    }

    @Override
    public String getName() {
        return entityPath.getTableName();
    }

    @Override
    public Stream<? extends IField<E, ?>> fields() {
        return Stream.empty();
    }
    
    @Override
    public IFieldLayout<E> getLayout() {
        return null;
    }

    public RelationalPath<E> getEntityPath() {
        return entityPath;
    }

}
