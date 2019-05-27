package org.wystriframework.crudgen.annotation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IFieldLayout;

public class AnnotatedFieldLayout<E> implements IFieldLayout {

    private final AnnotatedEntity<E> entity;

    public AnnotatedFieldLayout(AnnotatedEntity<E> entity) {
        this.entity = entity;
    }

    @Override
    public List<Section> getSections() {
        Class<E> type = entity.getObjectClass();
        FormLayout formLayout = type.getAnnotation(FormLayout.class);
        Map<String, IField<?>> fields = entity.fields()
            .collect(Collectors.toMap(IField::getName, Function.identity()));

        final List<Section> sections = new ArrayList<>();
        sections.add(createSection(formLayout.title(), formLayout.value(), fields));
        for (FormLayout.Section section : formLayout.sections())
            sections.add(createSection(section.title(), section.value(), fields));

        sections.removeIf(it -> it == null);
        return sections;
    }

    private Section createSection(String title, FormLayout.Row[] fRows, Map<String, IField<?>> fields) {
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
