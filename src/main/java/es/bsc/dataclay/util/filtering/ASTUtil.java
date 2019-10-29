
package es.bsc.dataclay.util.filtering;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ASTUtil {

	public static List<Object> cimiToAST(final String cimiAST) {
		final String jsonAST = cimiToJSON(cimiAST);
		return jsonToAST(jsonAST);
	}

	public static List<Object> jsonToAST(final String jsonAST) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
		    mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

			final TypeReference<List<Object>> resulttype = new TypeReference<List<Object>>() {
			};
			return mapper.readValue(jsonAST, resulttype);
		} catch (final Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
	}

	public static String cimiToJSON(final String cimiAST) {
		String result = cimiAST;
		// nested attributes
		result = result.replace("\" \"/\" \"", "/");
		// Spaces are commas in JSON
		result = result.replace(" ", ",");
		// [:token => ["token"
		result = result.replaceAll("[:([a-zA-Z]+)", "\"$1\"");
		// remove inner quote on strings
		result = result.replace("\"\\\"", "\"");  // open double quote
		result = result.replace("\\\"\"", "\"");  // close double quote
		result = result.replace("\"'", "\"");  // open single quote
		result = result.replace("'\"", "\"");  // close single quote
		
		return result;
	}
}
