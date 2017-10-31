package logic.db;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import javax.persistence.EmbeddedId;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import data.DBQuery;
import util.Strings;

public class DatabaseHandler {

	private Configuration config;

	/**
	 * @param config
	 */
	public DatabaseHandler(Configuration config) {
		super();
		this.config = config;
	}

	/**
	 * Get a list of table names from database
	 * 
	 * @return list of table names
	 * @throws SQLException
	 *             if database error occurs
	 * @throws HibernateException
	 *             if database error occurs
	 */
	public List<String> getTableNames() throws SQLException, HibernateException {

		List<String> tableNames = new ArrayList<String>();

		try (SessionFactory sf = this.buildSessionFactory(); Session session = sf.openSession();) {

			session.beginTransaction();
			session.doWork(con -> {
				// connection will be closed automatically
				// calling Connection.close() causes an exception (closing already closed thing)

				ResultSet tables = con.getMetaData().getTables(null, null, null, null);
				while (tables.next()) {
					// table name is 3rd elements of ResultSet
					tableNames.add(tables.getString(3));
				}
			});
		}

		return tableNames;
	}

	/**
	 * Get name of columns in a table
	 * 
	 * @param table
	 *            Name of table to get columns from (case-insensitive)
	 * @return List name of columns of the table separated by comma (CSV)
	 * @throws SQLException
	 *             If a database error occurs
	 * @throws HibernateException
	 *             If a database error occurs
	 */
	public List<String> getTableColumns(String table)
			throws SQLException, HibernateException, IllegalArgumentException {

		if (Strings.isNullOrEmpty(table)) throw new IllegalArgumentException("Table name must not be null or empty");

		List<String> columnNames = new ArrayList<String>();

		try (SessionFactory sf = this.buildSessionFactory(); Session session = sf.openSession();) {

			session.beginTransaction();
			session.doWork(con -> {
				// connection will be closed automatically
				// calling Connection.close() causes an exception (closing already closed thing)

				ResultSet columns = con.getMetaData().getColumns(null, null, table, null);
				while (columns.next()) {
					// column name is 4th elements of ResultSet
					columnNames.add(columns.getString(4));
				}
			});
		}

		/* Basic JDBC connection way
		try (Connection con = this.helper.getSqlConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("select * from emp2 limit 1");) {
			ResultSetMetaData rsmd = rs.getMetaData();
		
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				columnNames.add(rsmd.getColumnName(i));
			}
		}
		*/

		return columnNames;
	}

	/**
	 * Get rows from a table where conditions match
	 * 
	 * @param <T>
	 *            Class which represents the table
	 * @param queryObject
	 *            DBQuery object JSON string that accords with DBQuery class
	 * @return Rows matching the condition as JSON string
	 * @throws HibernateException
	 *             If there is something wrong with accessing DB
	 */
	public <T> List<T> getRowsMatching(DBQuery queryObject) throws HibernateException, IllegalArgumentException {

		String hql = this.createQueryFromQueryObject(queryObject);

		List<T> resultList;
		try (SessionFactory sf = this.buildSessionFactory(); Session session = sf.openSession();) {

			session.beginTransaction();
			@SuppressWarnings("unchecked")
			Query<T> query = session.createQuery(hql);
			resultList = query.list();
		}

		return resultList;
	}

	/**
	 * Create HQL from DBQuery object. If the value of an Entry of Map is null
	 * or empty, then that Entry will be ignored. The argument must be a DBQuery
	 * object deserialised with TypeAdapter so that validation has been done and
	 * table name has been capitalised.
	 * 
	 * @param queryObject
	 *            DBQuery entity
	 * @return HQL string
	 * @throws IllegalArgumentException
	 *             when table name in the argument is null or empty
	 */
	private String createQueryFromQueryObject(DBQuery queryObject) throws IllegalArgumentException {

		// To use hibernate, table name must match the corresponding class name (case-sensitive).
		StringJoiner query = new StringJoiner(" AND ", "FROM " + queryObject.getTableName() + " WHERE ", "");

		// create query string
		Map<String, String> condition = queryObject.getCondition();
		condition.entrySet().forEach(entry -> {
			if (Strings.isNullOrEmpty(entry.getValue())) return;
			query.add(entry.getKey() + " = '" + entry.getValue() + "'");
		});

		return query.toString();
	}

	private SessionFactory buildSessionFactory() {
		return this.config.configure().buildSessionFactory();
	}

	// This is not in use currently
	public <T> String createQueryFromEntity(T entity) {

		// Objects.requireNonNull(entity, "Supplied entity cannot be null");

		String query = "FROM " + entity.getClass().getSimpleName() + " WHERE ";

		query += this.createConditionFromFields(entity.getClass().getDeclaredFields(), entity);

		return query;
	}

	// This is not in use currently
	private String createConditionFromFields(Field[] fields, Object declaringObject) {

		StringJoiner conditions = new StringJoiner(" AND ");
		try {

			for (Field field : fields) {
				field.setAccessible(true);

				if (field.isAnnotationPresent(EmbeddedId.class)) {
					// for composite key
					conditions.add(this.createConditionFromFields(field.getType().getDeclaredFields(),
							field.get(declaringObject)));
				} else {
					conditions.add(field.getName() + "='" + field.get(declaringObject).toString() + "'");
				}

			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Bug alert: setAccessible() should be called to obtain the field's value");
		}
		return conditions.toString();
	}
}
