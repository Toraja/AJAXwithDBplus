package json.deserialiser;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import data.DBQuery;
import util.Strings;

/**
 * Deserialiser for DBQuery class. This validates the fields of DBQuery that
 * table name and condition are not null or empty and condition has at least one
 * value.
 */
public class DBQueryDeserialiser implements JsonDeserializer<DBQuery> {

	@Override
	public DBQuery deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {

		Gson gson = new Gson();
		DBQuery dbQuery = gson.fromJson(json, typeOfT);

		String tableName = dbQuery.getTableName();
		Map<String, String> condition = dbQuery.getCondition();

		// validation
		if (Strings.isNullOrEmpty(tableName)) throw new JsonSyntaxException("Table name must not be null or empty");

		if (condition == null || condition.isEmpty())
			throw new JsonSyntaxException("Condition must not be null or empty");

		boolean conditionAllNullOrEmpty = true;
		for (String value : condition.values()) {
			if (!Strings.isNullOrEmpty(value)) {
				conditionAllNullOrEmpty = false;
				break;
			}
		}
		if (conditionAllNullOrEmpty) throw new JsonSyntaxException("Values of condition must not be all null or empty");

		// modify value
		dbQuery.setTableName(Strings.capitalise(tableName));

		return dbQuery;
	}

}
