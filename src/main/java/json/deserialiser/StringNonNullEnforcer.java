package json.deserialiser;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

// This will never be used as Gson skips null and no TypeAdaptor is applied.
// Not deleted just for archive purpose.
public class StringNonNullEnforcer implements JsonDeserializer<String> {

	@Override
	public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {

		String message = "String field cannot be empty";
		String stringField = json.getAsString();

		if (stringField.isEmpty()) throw new JsonSyntaxException(message);

		return stringField;
	}
}
