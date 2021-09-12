package balt.sloboda.portal.model.converter;

import javax.persistence.Converter;
import java.util.List;

@Converter
public class EveryDaysMapToStringConverter extends GenericStringMapConverter<List<Integer>> {
}