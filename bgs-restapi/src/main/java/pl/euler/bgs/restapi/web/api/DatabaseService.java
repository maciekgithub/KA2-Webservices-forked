package pl.euler.bgs.restapi.web.api;

import com.codahale.metrics.annotation.Timed;
import com.google.common.io.CharStreams;
import javaslang.control.Option;
import javaslang.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;
import pl.euler.bgs.restapi.core.tracking.Tracked;
import pl.euler.bgs.restapi.web.api.params.ApiHeaders;
import pl.euler.bgs.restapi.web.api.params.RequestParams;
import pl.euler.bgs.restapi.web.common.HttpCodeException;

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
    private static final String REQUEST_METHOD = "p_request_method";
    private static final String REQUEST_PARAMS = "p_request_urlparams";
    private static final String REQUEST_HEADER_AGENT = "p_header_agent";
    private static final String REQUEST_HEADER_DATE = "p_header_date";
    private static final String REQUEST_HEADER_CONTENT_TYPE = "p_header_contenttype";
    private static final String REQUEST_BODY_PARAM = "p_request_body";
    private static final String RESPONSE_STATUS_PARAM = "p_error_code";
    private static final String RESPONSE_BODY_PARAM = "p_answer_body";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Tracked
    @Timed(name = "wbs_webservices_procedure")
    public DatabaseResponse executeRequestLogic(final DatabaseRequest request) {
        log.info("execute request: {}", request.infoLog());
        List<SqlParameter> declaredParameters = new ArrayList<>();
        declaredParameters.add(new SqlParameter(REQUEST_URL, Types.VARCHAR));
        declaredParameters.add(new SqlParameter(REQUEST_METHOD, Types.VARCHAR));
        declaredParameters.add(new SqlParameter(REQUEST_PARAMS, Types.VARCHAR));
        declaredParameters.add(new SqlParameter(REQUEST_HEADER_AGENT, Types.VARCHAR));
        declaredParameters.add(new SqlParameter(REQUEST_HEADER_DATE, Types.VARCHAR));
        declaredParameters.add(new SqlParameter(REQUEST_HEADER_CONTENT_TYPE, Types.VARCHAR));
        declaredParameters.add(new SqlParameter(REQUEST_BODY_PARAM, Types.CLOB));
        declaredParameters.add(new SqlOutParameter(RESPONSE_STATUS_PARAM, Types.NUMERIC));
        declaredParameters.add(new SqlOutParameter(RESPONSE_BODY_PARAM, Types.CLOB));

        ApiHeaders headers = request.getParams().getHeaders();
        RequestParams requestParams = request.getParams();

        try {
            Map<String, Object> result = this.jdbcTemplate.call(con -> {
                CallableStatement statement = con.prepareCall("{call bgs_webservices.wbs_webservices.request(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
                statement.setString(1, request.getRequestUrl());
                statement.setString(2, requestParams.getHttpMethod().name());
                statement.setString(3, requestParams.getUrlParams());
                statement.setString(4, headers.getUserAgent());
                statement.setString(5, headers.getDate());
                statement.setString(6, headers.getAcceptType());
                statement.setCharacterStream(7, new StringReader(request.getRequestJson()), request.getRequestJson().length());
                statement.registerOutParameter(8, Types.NUMERIC);
                statement.registerOutParameter(9, Types.CLOB);
                return statement;
            }, declaredParameters);

            Clob responseBody = (Clob) result.get(RESPONSE_BODY_PARAM);
            Option<String> optionalJson = Option.of(responseBody)
                    .map(rb -> Try.of(rb::getCharacterStream).map(CharStreams::toString).get());

            int status = ((BigDecimal) result.get(RESPONSE_STATUS_PARAM)).intValue();
            DatabaseResponse dbResponse = new DatabaseResponse(optionalJson, status);
            log.info("return response: {}", dbResponse.infoLog());
            return dbResponse;
        } catch (RecoverableDataAccessException dataException) {
            log.info("Cancelling request due to immediate maintenance mode! Cause: {}", dataException.getMessage());
            throw new HttpCodeException(HttpStatus.SERVICE_UNAVAILABLE, "The immediate maintenance mode has been enabled. " +
                    "Transaction has been cancelled");
        } catch (Exception e) {
            log.error("", e);
            throw new IllegalStateException("Cannot execute request logic!", e);
        }
    }

}
