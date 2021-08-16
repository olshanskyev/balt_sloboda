package balt.sloboda.portal.model.converter;

import balt.sloboda.portal.model.Role;
import balt.sloboda.portal.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

@Converter
public class StringMapConverter implements AttributeConverter<Map<String, String>, String> {

  @Override
  public String convertToDatabaseColumn(Map<String, String> set) {
    return JsonUtils.asJsonString(set);
  }

  @Override
  public Map<String, String> convertToEntityAttribute(String json) {
    return JsonUtils.fromJsonString(json, new TypeReference<Map<String, String>>() {});
  }

}