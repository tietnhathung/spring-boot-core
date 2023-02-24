package vn.amisoft.common.models.types;

import com.fasterxml.jackson.annotation.JsonValue;

public enum JsonWebTokenType {
    BEARER("Bearer");
    private final String value;
    JsonWebTokenType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
