/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.pricing;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class PricingVersionDateDeserializer extends JsonDeserializer<LocalDateTime> {

	// List of patterns to check over. Returned dates have varying number of "S" parameters
	private static final String[] PATTERNS = { "yyyy-MM-dd'T'HH:mm:ss.SSS", //
			"yyyy-MM-dd'T'HH:mm:ss.SS", //
			"yyyy-MM-dd'T'HH:mm:ss.S", //
			"yyyy-MM-dd'T'HH:mm:ss" //
	};

	@Override
	public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String date = p.getText();
		for (String pattern : PATTERNS) {
			try {
				return LocalDateTime.from(DateTimeFormatter.ofPattern(pattern).parse(date));
			} catch (Exception e) {
				// Ignore
			}
		}
		throw new RuntimeException("Unable to parse date " + date);
	}
}