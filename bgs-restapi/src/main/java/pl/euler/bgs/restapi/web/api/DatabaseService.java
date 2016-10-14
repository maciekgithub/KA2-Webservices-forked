package pl.euler.bgs.restapi.web.api;

import com.google.common.collect.ImmutableMap;
import com.google.common.io.CharStreams;
import oracle.jdbc.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.sql.Clob;
import java.util.Map;

import static java.util.Objects.requireNonNull;

@Service
public class DatabaseService {
    private static final String PROCEDURE_NAME = "request";
    private static final String REQUEST_URL_PARAM = "p_request_url";
    private static final String REQUEST_BODY_PARAM = "p_request_body";
    private static final String RESPONSE_BODY_PARAM = "p_answer";
    private static final String RESPONSE_STATUS_PARAM = "p_status";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public DatabaseResponse executeRequestLogic(String requestUrl, String requestJson) {

        SimpleJdbcCall procedureCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName(PROCEDURE_NAME);
        procedureCall.addDeclaredParameter(new SqlParameter(REQUEST_URL_PARAM, OracleTypes.VARCHAR));
        procedureCall.addDeclaredParameter(new SqlParameter(REQUEST_BODY_PARAM, OracleTypes.CLOB));
        procedureCall.addDeclaredParameter(new SqlOutParameter(RESPONSE_BODY_PARAM, OracleTypes.CLOB));
        procedureCall.addDeclaredParameter(new SqlOutParameter(RESPONSE_STATUS_PARAM, OracleTypes.INTEGER));

        try {
            Map<String, Object> result = procedureCall.execute(
                    ImmutableMap.of(REQUEST_URL_PARAM, requireNonNull(requestUrl), REQUEST_BODY_PARAM, requireNonNull(requestJson))
            );

            Clob responseBody = (Clob) result.get(RESPONSE_BODY_PARAM);
            String responseJson = CharStreams.toString(responseBody.getCharacterStream());
            int status = (int) result.get(RESPONSE_STATUS_PARAM);
            return new DatabaseResponse(responseJson, status);
        } catch (Exception ex) {
            throw new IllegalStateException("Problem with database procedure call", ex);
        }
    }

}
