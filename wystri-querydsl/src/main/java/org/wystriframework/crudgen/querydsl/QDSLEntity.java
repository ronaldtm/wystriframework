package org.wystriframework.crudgen.querydsl;

import java.util.Arrays;
import java.util.List;

import org.wystriframework.core.definition.IEntity;
import org.wystriframework.core.definition.IField;

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
    public List<? extends IField<?>> getFields() {
        return Arrays.asList();
    }

    public RelationalPath<T> getEntityPath() {
        return entityPath;
    }

}
