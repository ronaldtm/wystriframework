package org.wystriframework.crudgen.querydsl;

import java.util.stream.Stream;

import org.wystriframework.core.definition.IEntity;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IFieldLayout;

import com.querydsl.sql.RelationalPath;

public class QDSLEntity<T> implements IEntity {

    private final RelationalPath<T> entityPath;

    public QDSLEntity(RelationalPath<T> entityPath) {
        this.entityPath = entityPath;
    }

    @Override
    public String getName() {
        return entityPath.getTableName();
    }

    @Override
    public Stream<? extends IField<?>> fields() {
        return Stream.empty();
    }
    
    @Override
    public IFieldLayout getLayout() {
        return null;
    }

    public RelationalPath<T> getEntityPath() {
        return entityPath;
    }

}
