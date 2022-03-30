package com.mmxlabs.models.lng.transformer.ui.jobrunners.optimisation;

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

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.ui.headless.common.CustomTypeResolverBuilder;
import com.mmxlabs.models.lng.transformer.ui.headless.common.HeadlessRunnerUtils;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.HeadlessOptimiserJSON;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.HeadlessOptimiserJSONTransformer;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.HeadlessOptimiserRunner;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.AbstractJobRunner;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class OptimisationJobRunner extends AbstractJobRunner {

	private OptimisationSettings optimiserSettings;
	private IScenarioDataProvider sdp;

	private HeadlessOptimiserJSON loggingData;

	@Override
	public void withParams(final String json) throws IOException {
		final ObjectMapper mapper = createObjectMapper();
		try {
			optimiserSettings = mapper.readValue(json, OptimisationSettings.class);
		} catch (final Exception e) {
			final OptimisationSettings settings = new OptimisationSettings();
			settings.setUserSettings(mapper.readValue(json, UserSettings.class));
			this.optimiserSettings = settings;
		}
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
	public void withLogging(final Object mi) {
		enableLogging = true;

	}

	@Override
	public void withScenario(final IScenarioDataProvider sdp) {
		this.sdp = sdp;
	}

	@Override
	public AbstractSolutionSet run(final int threadsAvailable, final IProgressMonitor monitor) {
		if (optimiserSettings == null) {
			throw new IllegalStateException("Optimiser parameters have not been set");
		}
		if (sdp == null) {
			throw new IllegalStateException("Scenario has not been set");
		}

		if (enableLogging) {
			final HeadlessOptimiserJSONTransformer transformer = new HeadlessOptimiserJSONTransformer();
			final HeadlessOptimiserJSON json = transformer.createJSONResultObject();
			if (meta != null) {
				json.setMeta(meta);
			}
			json.getParams().setCores(threadsAvailable);

			loggingData = json;
		}

		final HeadlessOptimiserRunner runner = new HeadlessOptimiserRunner();
		return runner.doJobRun(sdp, optimiserSettings, loggingData, threadsAvailable, SubMonitor.convert(monitor));

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

			HeadlessRunnerUtils.renameInvalidBsonFields(loggingData.getMetrics().getStages());

			final ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.registerModule(new Jdk8Module());

			final CustomTypeResolverBuilder resolver = new CustomTypeResolverBuilder();
			resolver.init(JsonTypeInfo.Id.CLASS, null);
			resolver.inclusion(JsonTypeInfo.As.PROPERTY);
			resolver.typeProperty("@class");
			mapper.setDefaultTyping(resolver);

			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			mapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);

			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(loggingData);
		}
		throw new IllegalStateException("Logging not configured");
	}
}
