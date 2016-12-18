package pl.euler.bgs.restapi.web.api.params;

import com.google.common.base.MoreObjects;
import org.springframework.util.DigestUtils;

import java.util.Objects;

public class AgentNameAndPassword {
    private final String agentName;
    private final String password;

    public AgentNameAndPassword(String agentName, String password) {
        this.agentName = agentName;
        this.password = password;
    }

    public String getAgentName() {
        return agentName;
    }

    public String getPassword() {
        return password;
    }

    public String getHashedPassword() {
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AgentNameAndPassword that = (AgentNameAndPassword) o;
        return Objects.equals(agentName, that.agentName) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agentName, password);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("agentName", agentName)
                .add("password", "**")
                .toString();
    }
}
