/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.pricing;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.jdt.annotation.NonNull;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.lngdataserver.integration.general.ModelVersionHeader;
import com.mmxlabs.lngdataserver.integration.pricing.model.PricingVersion;

public class PricingIO {

	public static final int CURRENT_MODEL_VERSION = 1;

	private PricingIO() {

	}

	public static @NonNull ObjectMapper createObjectMapper() {
		final JsonFactory jsonFactory = new JsonFactory();
		jsonFactory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
		jsonFactory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);

		final ObjectMapper objectMapper = new ObjectMapper(jsonFactory);

		objectMapper.registerModule(new Jdk8Module());
		objectMapper.registerModule(new JavaTimeModule());
		return objectMapper;
	}

	public static PricingVersion read(final InputStream is) throws IOException {
		final JsonFactory jf = new JsonFactory();
		try (JsonParser jp = jf.createParser(is)) {
			jp.setCodec(createObjectMapper());
			jp.nextToken();

			// Read off header info
			final ModelVersionHeader header = jp.readValueAs(ModelVersionHeader.class);
			jp.nextToken();
			if (header.getModelVersion() == CURRENT_MODEL_VERSION) {
				return jp.readValueAs(PricingVersion.class);
			} else {
				switch (header.getModelVersion()) {
				case 0: {
					// DO SOmething with the parser stream.
					// try (BufferedReader reader = new BufferedReader(new InputStreamReader(str))) {
					// final String json = reader.lines().collect(Collectors.joining(" "));
					// Migrate json text file
					// return mapper.readValue(json, SettledPricesVersion.class);
					// }
				}
				default:
					throw new RuntimeException("Unknown model data version");
				}
			}
		}
	}

	public static void write(final PricingVersion version, final OutputStream os) throws IOException {
		final ObjectMapper mapper = createObjectMapper();
		final ModelVersionHeader header = new ModelVersionHeader();
		header.setModelVersion(CURRENT_MODEL_VERSION);
		mapper.writerWithDefaultPrettyPrinter().writeValue(os, header);
		mapper.writerWithDefaultPrettyPrinter().writeValue(os, version);
	}

	public static String write(final PricingVersion version) throws IOException {
		final ObjectMapper mapper = createObjectMapper();
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(version);
	}

	public static void writeHeader(final OutputStream os) throws IOException {
		final ObjectMapper mapper = createObjectMapper();
		final ModelVersionHeader header = new ModelVersionHeader();
		header.setModelVersion(CURRENT_MODEL_VERSION);
		mapper.writerWithDefaultPrettyPrinter().writeValue(os, header);
	}
}
