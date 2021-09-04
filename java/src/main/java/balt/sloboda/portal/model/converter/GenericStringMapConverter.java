package balt.sloboda.portal.model.converter;

import balt.sloboda.portal.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.persistence.AttributeConverter;
import java.util.*;

public class GenericStringMapConverter<T> implements AttributeConverter<Map<String, T>, String> {

  @Override
  public String convertToDatabaseColumn(Map<String, T> map) {
    if (map == null) {
      return "";
    }
    return JsonUtils.asJsonString(map);
  }

  @Override
  public Map<String, T> convertToEntityAttribute(String json) {
    if (json == null || json.isEmpty()){
      return new HashMap<>();
    }
    return JsonUtils.fromJsonString(json, new TypeReference<Map<String, T>>() {});
  }

}