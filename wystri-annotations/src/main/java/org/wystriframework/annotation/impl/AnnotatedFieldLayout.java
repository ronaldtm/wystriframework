package org.wystriframework.annotation.impl;

import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.wystriframework.annotation.FormLayout;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IFieldLayout;

public class AnnotatedFieldLayout<E> implements IFieldLayout<E> {

    private final AnnotatedEntity<E> entity;

    public AnnotatedFieldLayout(AnnotatedEntity<E> entity) {
        this.entity = entity;
    }

    @Override
    public List<Section> getSections() {
        Class<E> type = entity.getObjectClass();
        FormLayout formLayout = type.getAnnotation(FormLayout.class);
        if (formLayout != null) {
            return resolveSectionsFromFormLayoutAnnotation(formLayout);
        } else {
            return resolveSectionsFromAnnotatedFields(entity);
        }
    }

    private List<Section> resolveSectionsFromAnnotatedFields(AnnotatedEntity<E> entity) {
        final List<Row> rows = new ArrayList<>();
        final AtomicReference<List<Cell>> cellsRef = new AtomicReference<>();
        final AtomicInteger colCountRef = new AtomicInteger(Integer.MAX_VALUE);

        final Consumer<List<Cell>> finalizeRow = list -> {
            rows.add(new Row(list));
            cellsRef.set(null);
            colCountRef.set(Integer.MAX_VALUE);
        };

        for (AnnotatedField<E, ?> field : entity.fields().collect(toList())) {
            final FormLayout.BeginRow beginRow = field.getFieldAnnotation(FormLayout.BeginRow.class);
            final FormLayout.EndRow endRow = field.getFieldAnnotation(FormLayout.EndRow.class);
            final FormLayout.Cell cell = field.getFieldAnnotation(FormLayout.Cell.class);

            if (beginRow != null) {
                colCountRef.set(beginRow.cols());
                if (cellsRef.get() != null)
                    finalizeRow.accept(cellsRef.get());
                cellsRef.set(new ArrayList<>());
            }

            if (cellsRef.get() != null) {
                cellsRef.get().add(new Cell(field, (cell != null) ? cell.spec() : FormLayout.Cell.DEFAULT_SPEC));
            } else {
                finalizeRow.accept(asList(new Cell(field, (cell != null) ? cell.spec() : FormLayout.Cell.DEFAULT_SPEC)));
            }

            if (colCountRef.decrementAndGet() <= 0) {
                finalizeRow.accept(cellsRef.get());
            } else if (endRow != null) {
                if (cellsRef.get() == null)
                    throw new IllegalStateException("Reached an @EndRow without a matching @BeginRow");
                finalizeRow.accept(cellsRef.get());
            }
        }

        final List<Section> sections = new ArrayList<>();
        sections.add(new Section(null, rows));
        return sections;
    }

    private List<Section> resolveSectionsFromFormLayoutAnnotation(FormLayout formLayout) {
        Map<String, IField<?, ?>> fields = entity.fields()
            .collect(Collectors.toMap(IField::getName, Function.identity()));

        final List<Section> sections = new ArrayList<>();
        sections.add(createSection(formLayout.title(), formLayout.value(), fields));
        for (FormLayout.Section section : formLayout.sections())
            sections.add(createSection(section.title(), section.value(), fields));

        sections.removeIf(it -> it == null);
        return sections;
    }

    private Section createSection(String title, FormLayout.Row[] fRows, Map<String, IField<?, ?>> fields) {
        final List<Row> rows = new ArrayList<>();
        for (FormLayout.Row row : fRows) {
            final List<Cell> cells = new ArrayList<>();
            for (FormLayout.Cell cell : row.value())
                cells.add(new Cell(fields.remove(cell.value()), cell.spec()));
            rows.add(new Row(cells));
        }
        return new Section(title, rows);
    }

}
