package com.mmxlabs.models.lng.transformer.ui.jobrunners.optioniser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessOptioniserJSON;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessOptioniserJSONTransformer;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessOptioniserRunner;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.AbstractJobRunner;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class OptioniserJobRunner extends AbstractJobRunner {

	private OptioniserSettings optioniserSettings;
	private IScenarioDataProvider sdp;

	private HeadlessOptioniserJSON loggingData;

	@Override
	public void withParams(final String json) throws IOException {
		final ObjectMapper mapper = createObjectMapper();
		optioniserSettings = mapper.readValue(json, OptioniserSettings.class);
	}

	@Override
	public void withParams(final File file) throws IOException {

		try (FileInputStream is = new FileInputStream(file)) {
			final String text = new String(is.readAllBytes(), StandardCharsets.UTF_8);
			withParams(text);
		}
	}

	@Override
	public void withParams(final InputStream is) throws IOException {
		final String text = new String(is.readAllBytes(), StandardCharsets.UTF_8);
		withParams(text);
	}

	@Override
	public void withScenario(final IScenarioDataProvider sdp) {
		this.sdp = sdp;
	}

	@Override
	public SlotInsertionOptions run(int threadsAvailable, final IProgressMonitor monitor) {
		if (optioniserSettings == null) {
			throw new IllegalStateException("Optioniser parameters have not been set");
		}
		if (sdp == null) {
			throw new IllegalStateException("Scenario has not been set");
		}

		if (enableLogging) {
			HeadlessOptioniserJSONTransformer transformer = new HeadlessOptioniserJSONTransformer();
			final HeadlessOptioniserJSON json = transformer.createJSONResultObject(optioniserSettings);
			if (meta != null) {
				json.setMeta(meta);
			}
			json.getParams().setCores(threadsAvailable);

			loggingData = json;

		}

		final HeadlessOptioniserRunner runner = new HeadlessOptioniserRunner();
		return runner.doJobRun(sdp, optioniserSettings, loggingData, threadsAvailable, SubMonitor.convert(monitor));
	}

	@Override
	public void saveLogs(final File file) throws IOException {
		if (enableLogging && loggingData != null) {
			try (final PrintWriter p = new PrintWriter(new FileOutputStream(file))) {
				p.write(saveLogsAsString());
			}
		}
	}

	@Override
	public void saveLogs(final OutputStream os) throws IOException {
		if (enableLogging && loggingData != null) {
			try (final PrintWriter p = new PrintWriter(os)) {
				p.write(saveLogsAsString());
			}
		}
	}

	@Override
	public String saveLogsAsString() throws IOException {
		if (enableLogging && loggingData != null) {

			final ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.registerModule(new Jdk8Module());

			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			mapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);

			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(loggingData);

		}
		throw new IllegalStateException("Logging not configured");
	}
}
