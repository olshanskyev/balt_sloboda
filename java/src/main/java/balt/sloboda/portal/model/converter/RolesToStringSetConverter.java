package balt.sloboda.portal.model.converter;

import balt.sloboda.portal.model.Role;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

@Converter
public class RolesToStringSetConverter implements AttributeConverter<Set<Role>, String> {

  @Override
  public String convertToDatabaseColumn(Set<Role> set) {
    return set.stream().map(Enum::toString).collect(Collectors.joining(","));
  }

  @Override
  public Set<Role> convertToEntityAttribute(String joined) {
    HashSet<Role> myHashSet = new HashSet<>();
    if (joined == null) {
      return myHashSet;
    }
    StringTokenizer st = new StringTokenizer(joined, ",");
    while(st.hasMoreTokens())
      myHashSet.add(Role.valueOf(st.nextToken()));

    return myHashSet;
  }


}