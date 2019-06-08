package org.wystriframework.core.definition;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.ImmutableList;

public interface IFieldLayout<E> extends Serializable {

    List<Section> getSections();

    public static class Section implements Serializable {
        private final String                title;
        private final ImmutableList<Row> rows;
        public Section(String title, List<Row> rows) {
            this.title = title;
            this.rows = ImmutableList.copyOf(rows);
        }
        //@formatter:off
        public String             getTitle() { return title; }
        public ImmutableList<Row> getRows()  { return rows ; }
        //@formatter:on
    }

    public static class Row implements Serializable {
        private final ImmutableList<Cell> cells;
        public Row(List<Cell> cells) {
            this.cells = ImmutableList.copyOf(cells);
        }
        //@formatter:off
        public List<Cell> getCells() { return cells; }
        //@formatter:on
    }

    public static class Cell implements Serializable {
        private final IField<?, ?> field;
        private final String       spec;

        public Cell(IField<?, ?> field, String spec) {
            this.field = field;
            this.spec = spec;
        }
        //@formatter:off
        public IField<?,?> getField() { return field; }
        public String      getSpec()  { return spec ; }
        //@formatter:on
    }
}
