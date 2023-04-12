/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.jobrunners.sandbox;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Objects;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.util.SandboxModeConstants;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioChainBuilder;
import com.mmxlabs.models.lng.transformer.ui.headless.common.CustomTypeResolverBuilder;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.AbstractJobRunner;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

@NonNullByDefault
public class SandboxJobRunner extends AbstractJobRunner {

	public static final String JOB_TYPE = "sandbox";
	public static final String JOB_DISPLAY_NAME = "Sandbox";

	private @Nullable SandboxSettings sandboxSettings;
	private @Nullable Object loggingData;

	public void withParams(final SandboxSettings settings) {
		sandboxSettings = settings;
	}

	@Override
	public void withParams(final String json) throws IOException {
		final ObjectMapper mapper = createObjectMapper();
		sandboxSettings = mapper.readValue(json, SandboxSettings.class);
	}

	@Override
	public @Nullable AbstractSolutionSet run(final int threadsAvailable, final IProgressMonitor monitor) {
		final SandboxSettings pSandboxSettings = sandboxSettings;
		if (pSandboxSettings == null) {
			throw new IllegalStateException("Optimiser parameters have not been set");
		}
		final IScenarioDataProvider pSDP = sdp;
		if (pSDP == null) {
			throw new IllegalStateException("Scenario has not been set");
		}

		return doJobRun(pSDP, pSandboxSettings, threadsAvailable, SubMonitor.convert(monitor));
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

			// HeadlessRunnerUtils.renameInvalidBsonFields(loggingData.getMetrics().getStages());

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

	public @Nullable AbstractSolutionSet doJobRun(final IScenarioDataProvider sdp, final SandboxSettings sandboxSettings, final int numThreads, final IProgressMonitor monitor) {
		final LNGScenarioModel lngScenarioModel = sdp.getTypedScenario(LNGScenarioModel.class);

		final UserSettings userSettings = sandboxSettings.getUserSettings();

		final OptionAnalysisModel model = findSandboxModel(sandboxSettings, lngScenarioModel);

		final SubMonitor subMonitor = SubMonitor.convert(monitor, 100);

		int threadsToUse = numThreads;
		if (threadsToUse < 1) {
			threadsToUse = LNGScenarioChainBuilder.getNumberOfAvailableCores();
		}

		return switch (model.getMode()) {
		case SandboxModeConstants.MODE_OPTIONISE -> runSandboxOptioniser(threadsToUse, sdp, null, model,  subMonitor);
		case SandboxModeConstants.MODE_OPTIMISE -> runSandboxOptimisation(threadsToUse, sdp, null, model,  subMonitor);
		case SandboxModeConstants.MODE_DEFINE -> runSandboxDefine(threadsToUse, sdp, null, model,  subMonitor);
		default -> throw new IllegalArgumentException("Unknown sandbox mode");
		};

	}

	private OptionAnalysisModel findSandboxModel(final SandboxSettings sandboxSettings, final LNGScenarioModel lngScenarioModel) {
		OptionAnalysisModel model = null;
		for (final OptionAnalysisModel oam : lngScenarioModel.getAnalyticsModel().getOptionModels()) {
			// Prefer UUID
			if (sandboxSettings.getSandboxUUID() != null) {
				if (Objects.equals(sandboxSettings.getSandboxUUID(), oam.getUuid())) {
					model = oam;
					break;
				}
			} else if (sandboxSettings.getSandboxName() != null) {
				if (Objects.equals(sandboxSettings.getSandboxName(), oam.getName())) {
					model = oam;
					break;
				}
			}
		}
		if (model == null) {
			if (sandboxSettings.getSandboxUUID() != null) {
				throw new IllegalArgumentException("Missing sandbox " + sandboxSettings.getSandboxUUID());
			} else if (sandboxSettings.getSandboxName() != null) {
				throw new IllegalArgumentException("Missing sandbox " + sandboxSettings.getSandboxName());
			}
			throw new IllegalArgumentException("Missing sandbox");
		}
		return model;
	}

	public @Nullable AbstractSolutionSet runSandboxOptioniser(final int numThreads, final IScenarioDataProvider scenarioDataProvider, @Nullable final ScenarioInstance scenarioInstance,
			final OptionAnalysisModel model, final IProgressMonitor progressMonitor) {
		return SandboxOptioniserRunner.createSandboxJobFunction(numThreads, sdp, scenarioInstance, sandboxSettings, model, meta, l -> SandboxJobRunner.this.loggingData = l).apply(progressMonitor);
	}

	public @Nullable AbstractSolutionSet runSandboxOptimisation(final int numThreads, final IScenarioDataProvider scenarioDataProvider, final @Nullable ScenarioInstance scenarioInstance,
			final OptionAnalysisModel model, final IProgressMonitor progressMonitor) {
		return SandboxOptimiserRunner.createSandboxJobFunction(numThreads, sdp, scenarioInstance, sandboxSettings, model, meta, l -> SandboxJobRunner.this.loggingData = l).apply(progressMonitor);
	}

	public @Nullable AbstractSolutionSet runSandboxDefine(final int numThreads, final IScenarioDataProvider scenarioDataProvider, final @Nullable ScenarioInstance scenarioInstance,
			final OptionAnalysisModel model, final IProgressMonitor progressMonitor) {
		return SandboxDefineRunner.createSandboxJobFunction(numThreads, sdp, scenarioInstance, sandboxSettings, model, meta, l -> SandboxJobRunner.this.loggingData = l).apply(progressMonitor);
	}
}
