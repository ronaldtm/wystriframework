package org.wystriframework.crudgen.annotation;

public enum Bool {
    TRUE, FALSE, UNDEFINED;

    //@formatter:off
    public boolean isTrue()         { return this == TRUE     ; }
    public boolean isNotTrue()      { return this != TRUE     ; }
    public boolean isFalse()        { return this == FALSE    ; }
    public boolean isNotFalse()     { return this != FALSE    ; }
    public boolean isUndefined()    { return this == UNDEFINED; }
    public boolean isNotUndefined() { return this != UNDEFINED; }
}
