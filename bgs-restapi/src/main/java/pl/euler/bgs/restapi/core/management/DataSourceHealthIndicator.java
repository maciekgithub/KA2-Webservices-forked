package pl.euler.bgs.restapi.core.management;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import pl.euler.bgs.restapi.web.maintenance.MaintenanceService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

/**
 * Data sourrce healt indicator which takes into account the maintenance mode.
 */
public class DataSourceHealthIndicator extends AbstractHealthIndicator
		implements InitializingBean {

	private static final String DEFAULT_QUERY = "SELECT 1";

	private DataSource dataSource;

	private String query;

	private JdbcTemplate jdbcTemplate;

	private MaintenanceService maintenanceService;

	/**
	 * Create a new {@link DataSourceHealthIndicator} using the specified
	 * {@link DataSource} and validation query.
	 * @param dataSource the data source
	 * @param query the validation query to use (can be {@code null})
	 */
	public DataSourceHealthIndicator(DataSource dataSource, String query, MaintenanceService maintenanceService) {
		this.dataSource = dataSource;
		this.query = query;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.maintenanceService = maintenanceService;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.state(this.dataSource != null,
				"DataSource for DataSourceHealthIndicator must be specified");
	}

	@Override
	protected void doHealthCheck(Health.Builder builder) throws Exception {
		if (maintenanceService.isMaintenanceModeEnabled()) {
			builder.up().status(new Status("MAINTENANCE")).withDetail("info", "Pool suspended!");
		} else {
			if (this.dataSource == null) {
				builder.up().withDetail("database", "unknown");
			}
			else {
				doDataSourceHealthCheck(builder);
			}
		}
	}

	private void doDataSourceHealthCheck(Health.Builder builder) throws Exception {
		String product = getProduct();
		builder.up().withDetail("database", product);
		String validationQuery = getValidationQuery(product);
		if (StringUtils.hasText(validationQuery)) {
			try {
				// Avoid calling getObject as it breaks MySQL on Java 7
				List<Object> results = this.jdbcTemplate.query(validationQuery,
						new SingleColumnRowMapper());
				Object result = DataAccessUtils.requiredSingleResult(results);
				builder.withDetail("hello", result);
			}
			catch (Exception ex) {
				builder.down(ex);
			}
		}
	}

	private String getProduct() {
		return this.jdbcTemplate.execute(new ConnectionCallback<String>() {
			@Override
			public String doInConnection(Connection connection)
					throws SQLException, DataAccessException {
				return connection.getMetaData().getDatabaseProductName();
			}
		});
	}

	protected String getValidationQuery(String product) {
		String query = this.query;
		if (!StringUtils.hasText(query)) {
			DatabaseDriver specific = DatabaseDriver.fromProductName(product);
			query = specific.getValidationQuery();
		}
		if (!StringUtils.hasText(query)) {
			query = DEFAULT_QUERY;
		}
		return query;
	}

	/**
	 * Set the {@link DataSource} to use.
	 * @param dataSource the data source
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * Set a specific validation query to use to validate a connection. If none is set, a
	 * default validation query is used.
	 * @param query the query
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * Return the validation query or {@code null}.
	 * @return the query
	 */
	public String getQuery() {
		return this.query;
	}

	/**
	 * {@link RowMapper} that expects and returns results from a single column.
	 */
	private static class SingleColumnRowMapper implements RowMapper<Object> {

		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			ResultSetMetaData metaData = rs.getMetaData();
			int columns = metaData.getColumnCount();
			if (columns != 1) {
				throw new IncorrectResultSetColumnCountException(1, columns);
			}
			return JdbcUtils.getResultSetValue(rs, 1);
		}

	}

}
