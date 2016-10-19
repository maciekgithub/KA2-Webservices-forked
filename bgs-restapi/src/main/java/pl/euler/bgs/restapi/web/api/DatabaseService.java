package pl.euler.bgs.restapi.web.api;

import com.google.common.io.CharStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(DatabaseService.class);

    private static final String REQUEST_URL = "p_request_url";
    private static final String REQUEST_PARAMS = "p_request_urlparams";
    private static final String REQUEST_HEADER_AGENT = "p_header_agent";
    private static final String REQUEST_HEADER_DATE = "p_header_date";
    private static final String REQUEST_HEADER_CONTENT_TYPE = "p_header_contenttype";
    private static final String REQUEST_BODY_PARAM = "p_request_body";
    private static final String RESPONSE_STATUS_PARAM = "p_error_code";
    private static final String RESPONSE_BODY_PARAM = "p_answer";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public DatabaseResponse executeRequestLogic(final DatabaseRequest request) {
        log.info("execute request: {}", request);
        List<SqlParameter> declaredParameters = new ArrayList<>();
        declaredParameters.add(new SqlParameter(REQUEST_URL, Types.VARCHAR));
        declaredParameters.add(new SqlParameter(REQUEST_PARAMS, Types.VARCHAR));
        declaredParameters.add(new SqlParameter(REQUEST_HEADER_AGENT, Types.VARCHAR));
        declaredParameters.add(new SqlParameter(REQUEST_HEADER_DATE, Types.VARCHAR));
        declaredParameters.add(new SqlParameter(REQUEST_HEADER_CONTENT_TYPE, Types.VARCHAR));
        declaredParameters.add(new SqlParameter(REQUEST_BODY_PARAM, Types.CLOB));
        declaredParameters.add(new SqlOutParameter(RESPONSE_STATUS_PARAM, Types.NUMERIC));
        declaredParameters.add(new SqlOutParameter(RESPONSE_BODY_PARAM, Types.CLOB));

        ApiHeaders headers = request.getHeaders();

        try {
            Map<String, Object> result = this.jdbcTemplate.call(con -> {
                CallableStatement statement = con.prepareCall("{call bgs_webservices.wbs_webservices.request(?, ?, ?, ?, ?, ?, ?, ?)}");
                statement.setString(1, request.getRequestUrl());
                statement.setString(2, request.getRequestParams());
                statement.setString(3, headers.getUserAgent());
                statement.setString(4, headers.getDate());
                statement.setString(5, headers.getContentType());
                statement.setCharacterStream(6, new StringReader(request.getRequestJson()), request.getRequestJson().length());
                statement.registerOutParameter(7, Types.NUMERIC);
                statement.registerOutParameter(8, Types.CLOB);
                return statement;
            }, declaredParameters);

            Clob responseBody = (Clob) result.get(RESPONSE_BODY_PARAM);
            String responseJson = CharStreams.toString(responseBody.getCharacterStream());
            int status = ((BigDecimal) result.get(RESPONSE_STATUS_PARAM)).intValue();
            DatabaseResponse dbResponse = new DatabaseResponse(responseJson, status);
            log.info("return response: {}", dbResponse);
            return dbResponse;
        } catch (Exception e) {
            log.error("", e);
            throw new IllegalStateException("Cannot execute request logic!", e);
        }
    }

}
