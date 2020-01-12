/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.models.bunkerfuels;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.eclipse.jdt.annotation.NonNull;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.lngdataserver.integration.general.ModelVersionHeader;

public class BunkerFuelsIO {

	public static final String MODEL_TYPE = "bunker-fuels";

	public static final int CURRENT_MODEL_VERSION = 1;

	private BunkerFuelsIO() {

	}

	public static @NonNull ObjectMapper createObjectMapper() {
		final JsonFactory jsonFactory = new JsonFactory();
		jsonFactory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);

		final ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
		objectMapper.registerModule(new Jdk8Module());
		objectMapper.registerModule(new JavaTimeModule());
		return objectMapper;
	}

	public static BunkerFuelsVersion read(final InputStream is) throws IOException {
		final ObjectMapper mapper = createObjectMapper();
		final ModelVersionHeader header = mapper.readValue(is, ModelVersionHeader.class);
		if (header.getModelVersion() == CURRENT_MODEL_VERSION) {
			return mapper.readValue(is, BunkerFuelsVersion.class);
		} else {
			switch (header.getModelVersion()) {
			case 0: {
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
					// final String json = reader.lines().collect(Collectors.joining(" "));
					// Migrate json text file
					// return mapper.readValue(json, PortGroupsVersion.class);
				}
			}
			default:
				throw new RuntimeException("Unknown model data version");
			}
		}
	}

	public static void write(final BunkerFuelsVersion version, final OutputStream os) throws IOException {
		final ObjectMapper mapper = createObjectMapper();
		final ModelVersionHeader header = new ModelVersionHeader();
		header.setModelVersion(CURRENT_MODEL_VERSION);
		mapper.writerWithDefaultPrettyPrinter().writeValue(os, header);
		mapper.writerWithDefaultPrettyPrinter().writeValue(os, version);
	}
}
