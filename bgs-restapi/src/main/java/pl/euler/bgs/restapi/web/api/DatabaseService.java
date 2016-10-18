package pl.euler.bgs.restapi.web.api;

import com.google.common.io.CharStreams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DatabaseService {
    private static final String REQUEST_URL_PARAM = "p_request_url";
    private static final String REQUEST_BODY_PARAM = "p_request_body";
    private static final String RESPONSE_STATUS_PARAM = "p_error_code";
    private static final String RESPONSE_BODY_PARAM = "p_answer";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public DatabaseResponse executeRequestLogic(String requestUrl, String requestJson) {
        List<SqlParameter> declaredParameters = new ArrayList<>();
        declaredParameters.add(new SqlParameter(REQUEST_URL_PARAM, Types.VARCHAR));
        declaredParameters.add(new SqlParameter(REQUEST_BODY_PARAM, Types.CLOB));
        declaredParameters.add(new SqlOutParameter(RESPONSE_STATUS_PARAM, Types.NUMERIC));
        declaredParameters.add(new SqlOutParameter(RESPONSE_BODY_PARAM, Types.CLOB));

        try {
            Map<String, Object> result = this.jdbcTemplate.call(con -> {
                CallableStatement statement = con.prepareCall("{call bgs_webservices.wbs_webservices.request(?, ?, ?, ?)}");
                statement.setString(1, requestUrl);
                statement.setCharacterStream(2, new StringReader(requestJson), requestJson.length());
                statement.registerOutParameter(3, Types.NUMERIC);
                statement.registerOutParameter(4, Types.CLOB);
                return statement;
            }, declaredParameters);

            Clob responseBody = (Clob) result.get(RESPONSE_BODY_PARAM);
            String responseJson = CharStreams.toString(responseBody.getCharacterStream());
            int status = ((BigDecimal) result.get(RESPONSE_STATUS_PARAM)).intValue();
            return new DatabaseResponse(responseJson, status);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot execute request logic!", e);
        }
    }

}
