package org.wystriframework.core.definition.options;

import java.util.List;

import org.wystriframework.core.definition.IOptionsProvider;
import org.wystriframework.core.definition.IRecord;

import com.google.common.collect.ImmutableList;

public class SimpleStringOptionsProvider implements IOptionsProvider<String> {

    private ImmutableList<String> options;

    @Override
    public List<? extends String> getOptions(IRecord record) {
        return options;
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

    public List<? extends String> getOptions() {
        return options;
    }
    public SimpleStringOptionsProvider setOptions(List<String> options) {
        this.options = ImmutableList.copyOf(options);
        return this;
    }
    public SimpleStringOptionsProvider setOptions(String... options) {
        return setOptions(ImmutableList.copyOf(options));
    }
}
