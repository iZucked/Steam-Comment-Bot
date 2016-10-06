package com.mmxlabs.models.lng.transformer.ui.analytics;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.analytics.services.IAnalyticsScenarioEvaluator;
import com.mmxlabs.models.lng.parameters.BreakEvenOptimisationStage;
import com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.OptimisationStage;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunnerUtils;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class AnalyticsScenarioEvaluator implements IAnalyticsScenarioEvaluator {
	@Override
	public void evaluate(@org.eclipse.jdt.annotation.NonNull final LNGScenarioModel lngScenarioModel, @org.eclipse.jdt.annotation.NonNull final UserSettings userSettings,
			@Nullable final ScenarioInstance parentForFork) {

		OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);
		optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(optimisationPlan);

		// Generate internal data
		final ExecutorService executorService = Executors.newFixedThreadPool(1);
		try {

			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, lngScenarioModel, null, optimisationPlan, LNGSchedulerJobUtils.createLocalEditingDomain(), null, null, null,
					false);

			scenarioRunner.evaluateInitialState();
			if (parentForFork != null) {
				final IScenarioService scenarioService = parentForFork.getScenarioService();

				final ScenarioInstance dup = scenarioService.insert(parentForFork, EcoreUtil.copy(lngScenarioModel));
				dup.setName("Base Case");

				// Copy across various bits of information
				dup.getMetadata().setContentType(parentForFork.getMetadata().getContentType());
				dup.getMetadata().setCreated(parentForFork.getMetadata().getCreated());
				dup.getMetadata().setLastModified(new Date());

				// Copy version context information
				dup.setVersionContext(parentForFork.getVersionContext());
				dup.setScenarioVersion(parentForFork.getScenarioVersion());

				dup.setClientVersionContext(parentForFork.getClientVersionContext());
				dup.setClientScenarioVersion(parentForFork.getClientScenarioVersion());
			}

		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			executorService.shutdownNow();
		}

	}

	@Override
	public void breakEvenEvaluate(@NonNull LNGScenarioModel lngScenarioModel, @NonNull UserSettings userSettings, @Nullable ScenarioInstance parentForFork, long targetProfitAndLoss, BreakEvenMode breakEvenMode) {
		OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);
		if (breakEvenMode == BreakEvenMode.PORTFOLIO) {
			optimisationPlan.getStages().clear();
			BreakEvenOptimisationStage breakEvenOptimisationStage = ParametersFactory.eINSTANCE.createBreakEvenOptimisationStage();
			breakEvenOptimisationStage.setTargetProfitAndLoss(targetProfitAndLoss);
			optimisationPlan.getStages().add(breakEvenOptimisationStage);
		}
		optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(optimisationPlan);

		// Generate internal data
		final ExecutorService executorService = Executors.newFixedThreadPool(1);
		try {
			String[] hints;
			if (breakEvenMode == BreakEvenMode.POINT_TO_POINT) {
				hints = new String[]{};
			} else {
				hints = new String[] {LNGTransformerHelper.HINT_DISABLE_CACHES};
			}
			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, lngScenarioModel, null, optimisationPlan, LNGSchedulerJobUtils.createLocalEditingDomain(), null, null, null,
					false, hints);

			scenarioRunner.evaluateInitialState();
			scenarioRunner.run();
			if (parentForFork != null) {
				final IScenarioService scenarioService = parentForFork.getScenarioService();

				final ScenarioInstance dup = scenarioService.insert(parentForFork, EcoreUtil.copy(lngScenarioModel));
				dup.setName("What if");

				// Copy across various bits of information
				dup.getMetadata().setContentType(parentForFork.getMetadata().getContentType());
				dup.getMetadata().setCreated(parentForFork.getMetadata().getCreated());
				dup.getMetadata().setLastModified(new Date());

				// Copy version context information
				dup.setVersionContext(parentForFork.getVersionContext());
				dup.setScenarioVersion(parentForFork.getScenarioVersion());

				dup.setClientVersionContext(parentForFork.getClientVersionContext());
				dup.setClientScenarioVersion(parentForFork.getClientScenarioVersion());
			}

		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			executorService.shutdownNow();
		}

	}
}
