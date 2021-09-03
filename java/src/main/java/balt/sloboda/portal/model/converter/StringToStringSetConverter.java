package balt.sloboda.portal.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

@Converter
public class StringToStringSetConverter implements AttributeConverter<Set<String>, String> {

  @Override
  public String convertToDatabaseColumn(Set<String> set) {
    return String.join(",", set);
  }

  @Override
  public Set<String> convertToEntityAttribute(String joined) {
    HashSet<String> myHashSet = new HashSet<>();
    if (joined == null)
      return myHashSet;
    StringTokenizer st = new StringTokenizer(joined, ",");
    while(st.hasMoreTokens())
      myHashSet.add(st.nextToken());

    return myHashSet;
  }


}