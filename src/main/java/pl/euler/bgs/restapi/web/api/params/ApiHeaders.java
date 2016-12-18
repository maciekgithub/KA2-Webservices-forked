package pl.euler.bgs.restapi.web.api.params;

import com.google.common.base.MoreObjects;

import java.util.Objects;

public class ApiHeaders {
    private final AgentNameAndPassword agent;
    private final String date;
    private final String acceptType;

    public ApiHeaders(AgentNameAndPassword agent, String date, String acceptType) {
        this.agent = agent;
        this.date = date;
        this.acceptType = acceptType;
    }

    public AgentNameAndPassword getAgent() {
        return agent;
    }

    public String getDate() {
        return date;
    }

    public String getAcceptType() {
        return acceptType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ApiHeaders that = (ApiHeaders) o;
        return Objects.equals(agent, that.agent) &&
                Objects.equals(date, that.date) &&
                Objects.equals(acceptType, that.acceptType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agent, date, acceptType);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("agent", agent)
                .add("date", date)
                .add("acceptType", acceptType)
                .toString();
    }
}
