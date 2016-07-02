package com.github.azell.sample;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import java.lang.reflect.Type;

import java.util.Arrays;
import java.util.Map;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.common.io.Closeables;

@Test
public final class JsonSerializerTest {
  private final JsonSerializer                     impl   =
    new JsonSerializer();
  private final ObjectMapper                       mapper = new ObjectMapper();
  private final TypeReference<Map<String, Object>> type   =
    new TypeReference<Map<String, Object>>() {}
  ;

  private void close(Closeable obj) {
    try {
      Closeables.close(obj, true);
    } catch (IOException e) {

      /* should never get here */
      throw new UncheckedIOException(e);
    }
  }

  private <T> boolean eq(T lhs, T rhs) {
    return mapper.valueToTree(lhs).equals(mapper.valueToTree(rhs));
  }

  @DataProvider(name = "JsonFileLoader")
  public Object[][] jsonFileLoader() {
    return Arrays.asList("001.json",
                         "002.json",
                         "003.json",
                         "004.json",
                         "005.json")
                 .stream()
                 .map(path -> new Object[] { parse(path) })
                 .toArray(Object[][]::new);
  }

  private Map<String, Object> parse(String path) {
    InputStream src = null;

    try {
      src = getClass().getResourceAsStream(path);

      return mapper.readValue(src, type);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } finally {
      close(src);
    }
  }

  @Test(dataProvider = "JsonFileLoader")
  public void shouldGenerateValidJson(Map<String, Object> lhs)
          throws IOException {
    String              str = impl.toJson(lhs);
    Map<String, Object> rhs = mapper.readValue(str, type);

    assertTrue(eq(lhs, rhs));
  }
}
