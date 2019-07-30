package com.softplan.jpm.converter;

import java.util.Arrays;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.softplan.jpm.enun.JudicialProcessStatusEnum;

@Converter(autoApply = true)
public class JudicialProcessStatusConverter implements AttributeConverter<JudicialProcessStatusEnum, String> {

	@Override
	public String convertToDatabaseColumn(JudicialProcessStatusEnum attribute) {
		if (attribute == null) {
			return null;
		}
		return attribute.getStatus();
	}

	@Override
	public JudicialProcessStatusEnum convertToEntityAttribute(String status) {
		if (status == null) {
			return null;
		}
		System.out.println();
		JudicialProcessStatusEnum enumeration = Arrays.stream(JudicialProcessStatusEnum.values())
				.filter(c -> c.getStatus().equals(status))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
		return enumeration;
	}
}
