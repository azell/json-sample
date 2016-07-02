package com.github.azell.sample;

import java.util.List;
import java.util.Map;

/**
 * Implementation of simple Json serializer
 *
 * NEW Version!! Include fix to the empty array and empty object producing bad json.
 *
 * @author ys.ahnpark@gmail.com
 *
 */
public class JsonSerializer {

	/**
	 * Serializes the Map object in JSON format.
	 * @param map
	 * @return
	 */
	public String toJson(Map<String, Object> map) {
		// @TODO: The result object is only used internally: no need for synchronization, use StringBuilder
		StringBuffer result = new StringBuffer();
		this.toJsonRecursive(map, result);
		return result.toString();
	}

	/**
	 * Called by the public toJson() method
	 * @param map
	 * @param result
	 */
	void toJsonRecursive(Map<String, Object> map, StringBuffer result) {
		result.append("{");

		for (Map.Entry<String, Object> entry: map.entrySet()) {
			result.append("\"").append( entry.getKey() ).append("\":");

			this.emitObject(entry.getValue(), result);

			result.append(",");
		}
		// Removing the last comma
		if (result.charAt(result.length()-1) == ',')
			result.delete(result.length()-1, result.length());

		result.append("}");
	}

	/**
	 * Emits the object as appropriate json element to the string buffer.
	 *
	 * This allows to reuse the branching by type block
	 * @param obj
	 * @param result
	 */
	void emitObject(Object obj, StringBuffer result)
	{
		if (obj instanceof Map) {
			this.toJsonRecursive( (Map)obj, result);
		} else if (obj instanceof List) {

			List list = (List)obj;

			result.append("[");
			for (Object element: list) {
				this.emitObject(element, result);
				result.append(",");
			}
			if (result.charAt(result.length()-1) == ',')
				result.delete(result.length()-1, result.length());
			result.append("]");

		} else if (obj instanceof String) {
			// @TODO: string sanitization - e.g. escape quotes
			result.append("\"").append( obj ).append("\"");

		} else if (obj instanceof Boolean) {
			result.append( obj.toString() );

		} else if (obj instanceof Number) {
			result.append( obj.toString() );
		}
	}

}
