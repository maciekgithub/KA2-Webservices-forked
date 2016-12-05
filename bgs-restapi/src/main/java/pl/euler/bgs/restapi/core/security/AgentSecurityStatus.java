package pl.euler.bgs.restapi.core.security;

public enum AgentSecurityStatus {
    NOT_ENABLED,
    INCORRECT_PASSWORD,
    SSL_REQUIRED,
    FAILURE,
    SUCCESS;
}
