package balt.sloboda.portal.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

@Converter
public class DatesSetToStringConverter implements AttributeConverter<Set<LocalDate>, String> {

  @Override
  public String convertToDatabaseColumn(Set<LocalDate> dates) {
    if (dates == null) {
      return "";
    }
    return dates.stream().map(LocalDate::toString).collect(Collectors.joining(","));
  }

  @Override
  public Set<LocalDate> convertToEntityAttribute(String joined) {
    HashSet<LocalDate> myHashSet = new HashSet<>();
    if (joined == null || joined.isEmpty()) {
      return myHashSet;
    }
    StringTokenizer st = new StringTokenizer(joined, ",");
    while(st.hasMoreTokens())
      myHashSet.add(LocalDate.parse(st.nextToken()));

    return myHashSet;
  }


}