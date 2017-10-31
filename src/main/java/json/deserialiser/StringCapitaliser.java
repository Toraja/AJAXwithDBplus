package json.deserialiser;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import util.Strings;

public class StringCapitaliser implements JsonDeserializer<String> {

	@Override
	public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {

		return Strings.capitalise(json.getAsString());
	}

}
