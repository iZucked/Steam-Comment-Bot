/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.commons.model;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CreatedAtInstantDeserializer extends JsonDeserializer<Instant> {

	// List of patterns to check over. Returned dates have varying number of "S" parameters
	private static final String[] PATTERNS = { "yyyy-MM-dd'T'HH:mm:ss.SSS", //
			"yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS", //
			"yyyy-MM-dd'T'HH:mm:ss.SSSSSSSS", //
			"yyyy-MM-dd'T'HH:mm:ss.SSSSSSS", //
			"yyyy-MM-dd'T'HH:mm:ss.SSSSSS", //
			"yyyy-MM-dd'T'HH:mm:ss.SSSSS", //
			"yyyy-MM-dd'T'HH:mm:ss.SSSS", //
			"yyyy-MM-dd'T'HH:mm:ss.SSS", //
			"yyyy-MM-dd'T'HH:mm:ss.SS", //
			"yyyy-MM-dd'T'HH:mm:ss.S", //
			"yyyy-MM-dd'T'HH:mm:ss", //
			"yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS'Z'", //
			"yyyy-MM-dd'T'HH:mm:ss.SSSSSSSS'Z'", //
			"yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'", //
			"yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", //
			"yyyy-MM-dd'T'HH:mm:ss.SSSSS'Z'", //
			"yyyy-MM-dd'T'HH:mm:ss.SSSS'Z'", //
			"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", //
			"yyyy-MM-dd'T'HH:mm:ss.SS'Z'", //
			"yyyy-MM-dd'T'HH:mm:ss.S'Z'", //
			"yyyy-MM-dd'T'HH:mm:ss'Z'", //
	};

	@Override
	public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		String date = p.getText();
		for (String pattern : PATTERNS) {
			try {
				TemporalAccessor temporal = DateTimeFormatter.ofPattern(pattern).parse(date);
				try {
					return Instant.from(temporal);
				} catch (DateTimeException e) {
					if (!pattern.endsWith("Z")) {
						return LocalDateTime.from(temporal).toInstant(ZoneOffset.UTC);
					}
				}
			} catch (Exception e) {
				// Ignore
				// e.printStackTrace();
			}
		}
		throw new RuntimeException("Unable to parse date " + date);
	}
}