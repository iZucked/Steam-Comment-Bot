/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.Assert;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.util.CheckedConsumer;
import com.mmxlabs.common.util.CheckedFunction;
import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunnerUtils;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.models.migration.scenario.MigrationHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.manifest.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.util.MMXAdaptersAwareCommandStack;
import com.mmxlabs.scenario.service.util.ResourceHelper;
import com.mmxlabs.scenario.service.util.encryption.IScenarioCipherProvider;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class LNGScenarioRunnerCreator {

	public static void withLiNGOFileEvaluationRunner(@NonNull final URL url, @NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, Exception> consumer) throws Exception {

		// ScenarioStorageUtil.withExternalScenarioFromResourceURL(url, (scenarioInstance, modelReference) -> {
		// LNGScenarioModel scenarioModel = (LNGScenarioModel) modelReference.getInstance();
		// final OptimisationPlan optimiserSettings = LNGScenarioRunnerUtils.createExtendedSettings(ScenarioUtils.createDefaultOptimisationPlan());
		// withEvaluationRunner(scenarioModel, optimiserSettings, consumer);
		// });

		final LNGScenarioRunner runner = LNGScenarioRunnerCreator.createScenarioRunnerForEvaluation(url);
		consumer.accept(runner);
	}

	public static void withLiNGOFileLegacyEvaluationRunner(@NonNull final URL url, @Nullable final Boolean withGCO, @NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, Exception> consumer)
			throws Exception {
		withLegacyEvaluationRunner(getScenarioModelFromURL(url), withGCO, consumer);
	}

	public static void withLiNGOFileLegacyOptimisationRunner(@NonNull final URL url, @Nullable final Boolean withGCO, @Nullable final Integer lsoIterations,
			@NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, Exception> consumer) throws Exception {
		withLegacyOptimisationRunner(getScenarioModelFromURL(url), withGCO, lsoIterations, consumer);
	}

	public static void withLiNGOFileOptimisationRunner(@NonNull final URL url, @NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, Exception> consumer) throws Exception {
		withOptimisationRunner(getScenarioModelFromURL(url), consumer);
	}

	public static <E extends Exception> void withExecutorService(final CheckedConsumer<@NonNull ExecutorService, E> consumer) throws E {
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		try {
			consumer.accept(executorService);
		} finally {
			executorService.shutdown();
		}
	}

	public static <T, E extends Exception> T withExecutorService(final CheckedFunction<@NonNull ExecutorService, T, E> func) throws E {
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		try {
			return func.apply(executorService);
		} finally {
			executorService.shutdown();
		}
	}

	public static void withEvaluationRunner(@NonNull final LNGScenarioModel originalScenario, @NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, Exception> consumer) throws Exception {
		final OptimisationPlan optimiserSettings = LNGScenarioRunnerUtils.createExtendedSettings(ScenarioUtils.createDefaultOptimisationPlan());

		withEvaluationRunner(originalScenario, optimiserSettings, consumer);
	}

	public static void withEvaluationRunner(@NonNull final LNGScenarioModel originalScenario, @NonNull final OptimisationPlan optimisationPlan,
			@NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, Exception> consumer) throws Exception {
		withExecutorService(executorService -> {
			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, originalScenario, null, optimisationPlan, LNGSchedulerJobUtils.createLocalEditingDomain(), null,
					createITSService(), null, false);

			scenarioRunner.evaluateInitialState();
			consumer.accept(scenarioRunner);
		});
	}

	public static void withEvaluationRunnerWithGCO(@NonNull final LNGScenarioModel originalScenario, @NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, Exception> consumer) throws Exception {
		withLegacyEvaluationRunner(originalScenario, true, consumer);
	}

	public static void withLegacyEvaluationRunner(@NonNull final LNGScenarioModel originalScenario, @Nullable final Boolean withGCO,
			@NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, Exception> consumer) throws Exception {
		final OptimisationPlan optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(ScenarioUtils.createDefaultOptimisationPlan());

		if (withGCO != null) {
			optimisationPlan.getUserSettings().setGenerateCharterOuts(withGCO);
		}
		withEvaluationRunner(originalScenario, optimisationPlan, consumer);
	}

	public static <E extends Exception> void withOptimisationRunner(@NonNull final LNGScenarioModel originalScenario, @NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, E> consumer) throws E {
		final OptimisationPlan optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(ScenarioUtils.createDefaultOptimisationPlan());

		withOptimisationRunner(originalScenario, optimisationPlan, consumer);
	}

	public static <E extends Exception> void withOptimisationRunner(@NonNull final LNGScenarioModel originalScenario, @NonNull final OptimisationPlan optimisationPlan,
			@NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, E> consumer) throws E {
		withExecutorService(executorService -> {
			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, originalScenario, null, optimisationPlan, LNGSchedulerJobUtils.createLocalEditingDomain(), null,
					createITSService(), null, false, LNGTransformerHelper.HINT_OPTIMISE_LSO);

			scenarioRunner.evaluateInitialState();
			consumer.accept(scenarioRunner);
		});
	}

	public static <E extends Exception> void withLegacyOptimisationRunner(@NonNull final LNGScenarioModel originalScenario, @Nullable final Boolean withGCO, @Nullable final Integer lsoIterations,
			@NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, E> consumer) throws E {
		final OptimisationPlan optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(ScenarioUtils.createDefaultOptimisationPlan());

		if (withGCO != null) {
			optimisationPlan.getUserSettings().setGenerateCharterOuts(withGCO);
		}
		if (lsoIterations != null) {
			ScenarioUtils.setLSOStageIterations(optimisationPlan, lsoIterations);
		}
		withOptimisationRunner(originalScenario, optimisationPlan, consumer);
	}

	@NonNull
	public static LNGScenarioRunner createScenarioRunnerForEvaluationWithGCO(@NonNull final LNGScenarioModel originalScenario) throws IOException {
		//
		OptimisationPlan optimisationPlan = ScenarioUtils.createDefaultOptimisationPlan();
		optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(optimisationPlan);

		optimisationPlan.getUserSettings().setGenerateCharterOuts(true);

		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		try {
			final LNGScenarioRunner scenarioRunner = LNGScenarioRunnerCreator.createScenarioRunnerWithLSO(executorService, originalScenario, optimisationPlan);
			scenarioRunner.evaluateInitialState();
			return scenarioRunner;
		} finally {
			executorService.shutdown();
		}
	}

	@NonNull
	public static LNGScenarioRunner createScenarioRunnerForEvaluation(@NonNull final LNGScenarioModel originalScenario) throws IOException {

		final LNGScenarioRunner scenarioRunner = createScenarioRunnerForEvaluation(originalScenario, (Boolean) null);
		return scenarioRunner;
	}

	@NonNull
	public static LNGScenarioRunner createScenarioRunnerForEvaluation(final @NonNull URL url) throws IOException {

		final LNGScenarioRunner scenarioRunner = createScenarioRunnerForEvaluation(getScenarioModelFromURL(url), (Boolean) null);
		return scenarioRunner;
	}

	@NonNull
	public static LNGScenarioRunner createScenarioRunnerForEvaluation(final @NonNull URL url, @Nullable Boolean withGCO) throws IOException {

		final LNGScenarioRunner scenarioRunner = createScenarioRunnerForEvaluation(getScenarioModelFromURL(url), withGCO);
		return scenarioRunner;
	}

	@NonNull
	public static LNGScenarioRunner createScenarioRunnerWithLSO(@NonNull final ExecutorService executorService, final @NonNull URL url, @Nullable Boolean withGCO, @Nullable Integer lsoIterations)
			throws IOException {
		return createScenarioRunnerWithLSO(executorService, getScenarioModelFromURL(url), withGCO, lsoIterations);
	}

	@NonNull
	public static LNGScenarioRunner createScenarioRunnerWithLSO(@NonNull final ExecutorService executorService, final @NonNull URL url) throws IOException {
		return createScenarioRunnerWithLSO(executorService, getScenarioModelFromURL(url), null, null);
	}

	@NonNull
	public static LNGScenarioRunner createScenarioRunnerForEvaluation(@NonNull final LNGScenarioModel originalScenario, @Nullable Boolean withGCO) {

		final OptimisationPlan optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(ScenarioUtils.createDefaultOptimisationPlan());
		if (withGCO != null) {
			optimisationPlan.getUserSettings().setGenerateCharterOuts(withGCO);
		}
		return createScenarioRunnerForEvaluation(originalScenario, optimisationPlan);

	}

	@NonNull
	public static LNGScenarioRunner createScenarioRunnerWithLSO(@NonNull final ExecutorService executorService, @NonNull final LNGScenarioModel originalScenario, @Nullable Boolean withGCO,
			@Nullable Integer lsoIterations) {
		final OptimisationPlan optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(ScenarioUtils.createDefaultOptimisationPlan());
		if (withGCO != null) {
			optimisationPlan.getUserSettings().setGenerateCharterOuts(withGCO);
		}
		if (lsoIterations != null) {
			ScenarioUtils.setLSOStageIterations(optimisationPlan, lsoIterations);
		}

		return createScenarioRunnerWithLSO(executorService, originalScenario, optimisationPlan);
	}

	@NonNull
	public static LNGScenarioRunner createScenarioRunnerForEvaluation(@NonNull final LNGScenarioModel originalScenario, @NonNull final OptimisationPlan optimisationPlan) {

		final @NonNull ExecutorService executorService = Executors.newSingleThreadExecutor();
		try {
			final LNGScenarioRunner originalScenarioRunner = new LNGScenarioRunner(executorService, originalScenario, null, optimisationPlan, LNGSchedulerJobUtils.createLocalEditingDomain(), null,
					createITSService(), null, true);

			originalScenarioRunner.evaluateInitialState();

			return originalScenarioRunner;
		} finally {
			executorService.shutdown();
		}
	}

	@NonNull
	public static LNGScenarioRunner createScenarioRunnerWithLSO(@NonNull final ExecutorService executorService, @NonNull final LNGScenarioModel originalScenario,
			@NonNull final OptimisationPlan optimisationPlan) {
		final LNGScenarioRunner originalScenarioRunner = new LNGScenarioRunner(executorService, originalScenario, null, optimisationPlan, LNGSchedulerJobUtils.createLocalEditingDomain(), null,
				createITSService(), null, false, LNGTransformerHelper.HINT_OPTIMISE_LSO);

		originalScenarioRunner.evaluateInitialState();

		return originalScenarioRunner;
	}

	@NonNull
	public static LNGScenarioModel getScenarioModel(@NonNull final ScenarioInstance instance) throws IOException {
		MigrationHelper.migrateAndLoad(instance);

		final LNGScenarioModel originalScenario = (LNGScenarioModel) instance.getInstance();
		Assert.assertNotNull(originalScenario);
		return originalScenario;
	}

	@NonNull
	public static LNGScenarioModel getScenarioModelFromURL(final URL url) throws IOException {
		final URI uri = URI.createURI(FileLocator.toFileURL(url).toString().replaceAll(" ", "%20"));
		return ServiceHelper.withCheckedService(IScenarioCipherProvider.class, (scenarioCipherProvider) -> {

			final ScenarioInstance instance = ScenarioStorageUtil.loadInstanceFromURI(uri, scenarioCipherProvider);
			Assert.assertNotNull(instance);
			final LNGScenarioModel originalScenario = getScenarioModel(instance);
			Assert.assertNotNull(originalScenario);
			return originalScenario;
		});
	}

	public static void saveScenarioModel(@NonNull final LNGScenarioModel scenario, @NonNull final File destinationFile) throws IOException {
		ServiceHelper.withCheckedService(IMigrationRegistry.class, (migrationRegistry) -> {

			final LNGScenarioModel copy = EcoreUtil.copy(scenario);
			final String context = migrationRegistry.getDefaultMigrationContext();
			if (context == null) {
				throw new NullPointerException("Context cannot be null");
			}
			int version = migrationRegistry.getLatestContextVersion(context);
			if (version < 0) {
				version = migrationRegistry.getLastReleaseVersion(context);
			}
			ScenarioTools.storeToFile(copy, destinationFile, context, version);
		});
	}

	@NonNull
	public static ScenarioInstance createScenarioInstance(@NonNull final LNGScenarioModel scenario, @NonNull final URL scenarioURL) {
		final ScenarioInstance scenarioInstance = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();
		scenarioInstance.setMetadata(ScenarioServiceFactory.eINSTANCE.createMetadata());
		scenarioInstance.setName(scenarioURL.getPath());
		scenarioInstance.setInstance(scenario);

		return scenarioInstance;
	}

	/**
	 * Use the {@link IParameterModesRegistry} to extend the existing settings object.
	 * 
	 * @param optimiserSettings
	 * @return
	 */

	/**
	 * Special optimiser injection service to disable special deployment settings during ITS runs
	 * 
	 * @return
	 */
	@NonNull
	public static IOptimiserInjectorService createITSService() {

		return new IOptimiserInjectorService() {

			@Override
			@Nullable
			public Module requestModule(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
				return null;
			}

			@Override
			@Nullable
			public List<@NonNull Module> requestModuleOverrides(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
				if (moduleType == ModuleType.Module_LNGTransformerModule) {
					return Collections.<@NonNull Module> singletonList(new AbstractModule() {

						@Override
						protected void configure() {
							// Default bindings for caches
							bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.Key_VolumeAllocationCache)).toInstance(Boolean.TRUE);
							bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.Key_VolumeAllocatedSequenceCache)).toInstance(Boolean.TRUE);
							bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.Key_ProfitandLossCache)).toInstance(Boolean.TRUE);
						}
					});
				}
				if (moduleType == ModuleType.Module_EvaluationParametersModule) {
					return Collections.<@NonNull Module> singletonList(new AbstractModule() {

						@Override
						protected void configure() {

						}

						@Provides
						@Named(LNGParameters_EvaluationSettingsModule.OPTIMISER_REEVALUATE)
						private boolean isOptimiserReevaluating() {
							return false;
						}
					});
				}
				return null;
			}
		};
	}
}
