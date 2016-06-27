package com.github.azell.sample;

import java.util.List;
import java.util.Map;

public class JsonSerializer {
  private static final String DELIM = ",";
  private static final String SEP   = ":";

  @SuppressWarnings("unchecked")
  private StringBuilder listToJson(StringBuilder doc, List<Object> obj) {
    doc.append("[");

    String delim = "";

    for (Object value : obj) {
      doc.append(delim);
      delim = DELIM;

      if (value instanceof String) {
        doc.append(quote(value));
      } else if (value instanceof Number) {
        doc.append(value);
      } else if (value instanceof Map) {
        doc = mapToJson(doc, (Map<String, Object>) value);
      } else if (value instanceof List) {
        doc = listToJson(doc, (List<Object>) value);
      }
    }

    return doc.append("]");
  }

  @SuppressWarnings("unchecked")
  private StringBuilder mapToJson(StringBuilder doc, Map<String, Object> obj) {
    doc.append("{");

    String delim = "";

    for (Map.Entry<String, Object> e : obj.entrySet()) {
      String key   = e.getKey();
      Object value = e.getValue();

      doc.append(delim);
      delim = DELIM;

      if (value instanceof String) {
        doc.append(quote(key)).append(SEP).append(quote(value));
      } else if (value instanceof Number) {
        doc.append(quote(key)).append(SEP).append(value);
      } else if (value instanceof Map) {
        doc = mapToJson(doc.append(quote(key)).append(SEP),
                        (Map<String, Object>) value);
      } else if (value instanceof List) {
        doc = listToJson(doc.append(quote(key)).append(SEP),
                         (List<Object>) value);
      }
    }

    return doc.append("}");
  }

  private String quote(Object obj) {
    return String.format("\"%s\"", obj.toString());
  }

  public String toJson(Map<String, Object> map) {
    return mapToJson(new StringBuilder(), map).toString();
  }
}
