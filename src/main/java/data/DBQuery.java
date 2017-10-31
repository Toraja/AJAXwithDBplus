package data;

import java.util.Map;

/**
 * Format of DB query. <i>condition</i> must be an objects with key-value pair
 * and the key as the column name of the table and the value as data.
 */
public class DBQuery {

	private String tableName;

	private Map<String, String> condition;

	/**
	 * Prohibit instantiation
	 */
	private DBQuery() {
		super();
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Map<String, String> getCondition() {
		return condition;
	}

	public void setCondition(Map<String, String> condition) {
		this.condition = condition;
	}

	@Override
	public String toString() {
		return "DBQuery [tableName=" + tableName + ", condition=" + condition + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((condition == null) ? 0 : condition.hashCode());
		result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		DBQuery other = (DBQuery) obj;
		if (this.condition == null) {
			if (other.condition != null) return false;
		} else if (!this.condition.equals(other.condition)) return false;
		if (this.tableName == null) {
			if (other.tableName != null) return false;
		} else if (!this.tableName.equals(other.tableName)) return false;
		return true;
	}
}
