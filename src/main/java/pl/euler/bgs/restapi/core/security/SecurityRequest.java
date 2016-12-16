package pl.euler.bgs.restapi.core.security;

import com.google.common.base.MoreObjects;

import java.util.Objects;

public class SecurityRequest {
    private final String schema;
    private final String passwordHash;

    public SecurityRequest(String schema, String passwordHash) {
        this.schema = schema;
        this.passwordHash = passwordHash;
    }

    public String getSchema() {
        return schema;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SecurityRequest that = (SecurityRequest) o;
        return Objects.equals(schema, that.schema) &&
                Objects.equals(passwordHash, that.passwordHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schema, passwordHash);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("schema", schema)
                .add("passwordHash", "***")
                .toString();
    }
}
