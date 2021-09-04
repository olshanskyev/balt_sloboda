package balt.sloboda.portal.model.converter;

import balt.sloboda.portal.model.EveryDays;

import javax.persistence.Converter;

@Converter
public class EveryDaysMapToStringConverter extends GenericStringMapConverter<EveryDays[]> {
}