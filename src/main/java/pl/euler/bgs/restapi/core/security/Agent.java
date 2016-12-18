package pl.euler.bgs.restapi.core.security;

import com.google.common.base.MoreObjects;

import java.util.Objects;

public class Agent {
    private final String name;
    private final String passwordHash;
    private final Boolean sslRequired;
    private final Boolean enabled;

    public Agent(String name, String passwordHash, Boolean sslRequired, Boolean enabled) {
        this.name = name;
        this.passwordHash = passwordHash;
        this.sslRequired = sslRequired;
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Boolean getSslRequired() {
        return sslRequired;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Agent agent = (Agent) o;
        return Objects.equals(name, agent.name) &&
                Objects.equals(passwordHash, agent.passwordHash) &&
                Objects.equals(sslRequired, agent.sslRequired) &&
                Objects.equals(enabled, agent.enabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, passwordHash, sslRequired, enabled);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("passwordHash", "****")
                .add("sslRequired", sslRequired)
                .add("enabled", enabled)
                .toString();
    }
}
