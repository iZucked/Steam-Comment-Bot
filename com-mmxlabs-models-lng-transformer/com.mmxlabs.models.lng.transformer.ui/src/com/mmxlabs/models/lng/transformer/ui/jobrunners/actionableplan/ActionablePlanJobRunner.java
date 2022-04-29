package com.mmxlabs.models.lng.transformer.ui.jobrunners.actionableplan;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.OptionalLong;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.models.lng.analytics.ActionableSetPlan;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.chain.IChainLink;
import com.mmxlabs.models.lng.transformer.chain.SequencesContainer;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioChainBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.actionablesets.ActionableSetsTransformerUnit;
import com.mmxlabs.models.lng.transformer.ui.common.SolutionSetExporterUnit;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.AbstractJobRunner;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.rcp.common.ecore.EMFCopier;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.constraints.impl.LadenLegLimitConstraintCheckerFactory;

public class ActionablePlanJobRunner extends AbstractJobRunner {

	public static final String JOB_TYPE = "actionableplan";
	private ActionablePlanSettings optioniserSettings;
	private Object loggingData;

	@Override
	public void withParams(final String json) throws IOException {
		final ObjectMapper mapper = createObjectMapper();
		optioniserSettings = mapper.readValue(json, ActionablePlanSettings.class);
	}

	@Override
	public @Nullable ActionableSetPlan run(final int threadsAvailable, final IProgressMonitor monitor) {
		if (optioniserSettings == null) {
			throw new IllegalStateException("Optioniser parameters have not been set");
		}
		final IScenarioDataProvider pSDP = sdp;
		if (pSDP == null) {
			throw new IllegalStateException("Scenario has not been set");
		}

		if (enableLogging) {
			// final HeadlessOptioniserJSONTransformer transformer = new HeadlessOptioniserJSONTransformer();
			// final HeadlessOptioniserJSON json = transformer.createJSONResultObject(optioniserSettings);
			// if (meta != null) {
			// json.setMeta(meta);
			// }
			// json.getParams().setCores(threadsAvailable);
			//
			// loggingData = json;

		}
		return doJobRun(pSDP, optioniserSettings, threadsAvailable, SubMonitor.convert(monitor));
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

	private @Nullable ActionableSetPlan doJobRun(final @NonNull IScenarioDataProvider sdp, final @NonNull ActionablePlanSettings actionablePlanSettings, final int threadsAvailable,
			@NonNull final IProgressMonitor parentMonitor) {

		final UserSettings userSettings = actionablePlanSettings.userSettings;

		int threadCount = threadsAvailable;

		if (threadCount < 1) {
			threadCount = LNGScenarioChainBuilder.getNumberOfAvailableCores();
		}

		final int threadsToUse = threadCount;

		final OptimisationPlan plan = ParametersFactory.eINSTANCE.createOptimisationPlan();
		plan.setUserSettings(EMFCopier.copy(userSettings));
		plan.setSolutionBuilderSettings(ScenarioUtils.createDefaultSolutionBuilderSettings());

		final LNGScenarioRunner scenarioRunner = LNGOptimisationBuilder.begin(sdp, null) //
				.withOptimisationPlan(plan) //
				.withOptimiseHint() //
				.withHints(LNGTransformerHelper.HINT_OPTIMISE_LSO) //
				.withThreadCount(threadsToUse) //
				.buildDefaultRunner() //
				.getScenarioRunner();

		final ConstraintAndFitnessSettings constraintAndFitnessSettings = ScenarioUtils.createDefaultConstraintAndFitnessSettings();
		// TODO: Filter
		// final Iterator<Constraint> iterator = constraintAndFitnessSettings.getConstraints().iterator();
		// while (iterator.hasNext()) {
		// final Constraint constraint = iterator.next();
		// if (constraint.getName().equals(RoundTripVesselPermissionConstraintCheckerFactory.NAME)) {
		// iterator.remove();
		// }
		// }
		ScenarioUtils.createOrUpdateContraints(LadenLegLimitConstraintCheckerFactory.NAME, true, constraintAndFitnessSettings);

		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
		final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
		//

		final ActionPlanOptimisationStage stageSettings = ParametersFactory.eINSTANCE.createActionPlanOptimisationStage();
		stageSettings.setName("actionplan");
		stageSettings.setConstraintAndFitnessSettings(constraintAndFitnessSettings);
		//
		try {
			final SubMonitor subMonitor = SubMonitor.convert(parentMonitor, 100);
			final ActionableSetsTransformerUnit actionPlanUnit = new ActionableSetsTransformerUnit("actionplan", dataTransformer.getUserSettings(), stageSettings,
					scenarioRunner.getJobExecutorFactory(), 100);

			final SequencesContainer initialSequencesContainer = new SequencesContainer(dataTransformer.getInitialResult().getBestSolution());

			final IMultiStateResult results = actionPlanUnit.run(dataTransformer, initialSequencesContainer, dataTransformer.getInitialResult(), subMonitor.split(90));
			if (subMonitor.isCanceled()) {
				return null;
			}

			final ActionableSetPlan result = AnalyticsFactory.eINSTANCE.createActionableSetPlan();
			if (actionablePlanSettings.resultName != null) {
				result.setName(actionablePlanSettings.resultName);
			}
			result.setUserSettings(EcoreUtil.copy(userSettings));

			final IChainLink link = SolutionSetExporterUnit.exportMultipleSolutions(null, 1, scenarioRunner.getScenarioToOptimiserBridge(), () -> result, false, OptionalLong.empty(), false);

			link.run(dataTransformer, initialSequencesContainer, results, subMonitor.split(10));
			return result;

		} finally {
			parentMonitor.done();
		}

	}
}
