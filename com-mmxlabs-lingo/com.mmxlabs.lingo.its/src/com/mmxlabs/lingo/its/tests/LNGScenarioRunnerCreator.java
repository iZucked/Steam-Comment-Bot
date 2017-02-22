/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

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
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.manifest.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.model.manager.InstanceData;
import com.mmxlabs.scenario.service.model.manager.ModelRecord;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.util.MMXAdaptersAwareCommandStack;
import com.mmxlabs.scenario.service.util.ResourceHelper;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.cache.CacheMode;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class LNGScenarioRunnerCreator {

	public static void withLiNGOFileEvaluationRunner(@NonNull final URL url, @NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, Exception> consumer) throws Exception {

		ScenarioStorageUtil.withExternalScenarioFromResourceURL(url, (scenarioInstance, modelReference) -> {
			LNGScenarioModel scenarioModel = (LNGScenarioModel) modelReference.getInstance();
			final OptimisationPlan optimiserSettings = LNGScenarioRunnerUtils.createExtendedSettings(ScenarioUtils.createDefaultOptimisationPlan());
			withEvaluationRunner(scenarioModel, optimiserSettings, consumer);
		});
	}

	public static void withLiNGOFileLegacyEvaluationRunner(@NonNull final URL url, @Nullable final Boolean withGCO, @NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, Exception> consumer)
			throws Exception {
		ScenarioStorageUtil.withExternalScenarioFromResourceURL(url, (scenarioInstance, modelReference) -> {
			LNGScenarioModel scenarioModel = (LNGScenarioModel) modelReference.getInstance();
			withLegacyEvaluationRunner(scenarioModel, withGCO, consumer);
		});
	}

	public static void withLiNGOFileLegacyOptimisationRunner(@NonNull final URL url, @Nullable final Boolean withGCO, @Nullable final Integer lsoIterations,
			@NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, Exception> consumer) throws Exception {

		ScenarioStorageUtil.withExternalScenarioFromResourceURL(url, (scenarioInstance, modelReference) -> {
			LNGScenarioModel scenarioModel = (LNGScenarioModel) modelReference.getInstance();
			withLegacyOptimisationRunner(scenarioModel, withGCO, lsoIterations, consumer);
		});
	}

	public static void withLiNGOFileOptimisationRunner(@NonNull final URL url, @NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, Exception> consumer) throws Exception {

		ScenarioStorageUtil.withExternalScenarioFromResourceURL(url, (scenarioInstance, modelReference) -> {
			LNGScenarioModel scenarioModel = (LNGScenarioModel) modelReference.getInstance();
			withOptimisationRunner(scenarioModel, consumer);
		});
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

	public static <E extends Exception> void withOptimisationRunner(@NonNull final LNGScenarioModel originalScenario, @NonNull final OptimisationPlan optimisationPlan,
			@NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, E> consumer) throws E {
		withExecutorService(executorService -> {
			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, originalScenario, null, optimisationPlan, LNGSchedulerJobUtils.createLocalEditingDomain(), null,
					createITSService(), null, false, LNGTransformerHelper.HINT_OPTIMISE_LSO);

			scenarioRunner.evaluateInitialState();
			consumer.accept(scenarioRunner);
		});
	}

	public static <E extends Exception> void withOptimisationRunner(@NonNull final LNGScenarioModel originalScenario, @NonNull final OptimisationPlan optimisationPlan,
			@NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, E> consumer, @Nullable IOptimiserInjectorService optimiserInjectorService, String... extraHints) throws E {

		String[] hints = new String[1 + (extraHints == null ? 0 : extraHints.length)];
		hints[0] = LNGTransformerHelper.HINT_OPTIMISE_LSO;
		if (extraHints != null) {
			int idx = 1;
			for (String h : extraHints) {
				hints[idx++] = h;
			}
		}

		withExecutorService(executorService -> {
			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, originalScenario, null, optimisationPlan, LNGSchedulerJobUtils.createLocalEditingDomain(), null,
					optimiserInjectorService, null, false, hints);

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

	public static <E extends Exception> void withOptimisationRunner(@NonNull final LNGScenarioModel originalScenario, @NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, E> consumer) throws E {
		final OptimisationPlan optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(ScenarioUtils.createDefaultOptimisationPlan());

		withOptimisationRunner(originalScenario, optimisationPlan, consumer);
	}

	// @NonNull
	// public static LNGScenarioRunner createScenarioRunnerForEvaluation(@NonNull final LNGScenarioModel originalScenario, @NonNull final OptimisationPlan optimisationPlan) {
	//
	// final @NonNull ExecutorService executorService = Executors.newSingleThreadExecutor();
	// try {
	// final LNGScenarioRunner originalScenarioRunner = new LNGScenarioRunner(executorService, originalScenario, null, optimisationPlan, LNGSchedulerJobUtils.createLocalEditingDomain(), null,
	// createITSService(), null, true);
	//
	// originalScenarioRunner.evaluateInitialState();
	//
	// return originalScenarioRunner;
	// } finally {
	// executorService.shutdown();
	// }
	// }
	//
	// @NonNull
	// public static LNGScenarioRunner createScenarioRunnerWithLSO(@NonNull final ExecutorService executorService, @NonNull final LNGScenarioModel originalScenario,
	// @NonNull final OptimisationPlan optimisationPlan) {
	// final LNGScenarioRunner originalScenarioRunner = new LNGScenarioRunner(executorService, originalScenario, null, optimisationPlan, LNGSchedulerJobUtils.createLocalEditingDomain(), null,
	// createITSService(), null, false, LNGTransformerHelper.HINT_OPTIMISE_LSO);
	//
	// originalScenarioRunner.evaluateInitialState();
	//
	// return originalScenarioRunner;
	// }
	//
	// @NonNull
	// public static LNGScenarioModel getScenarioModel(@NonNull final ScenarioInstance instance) throws IOException {
	// MigrationHelper.migrateAndLoad(instance);
	//
	// final LNGScenarioModel originalScenario = (LNGScenarioModel) instance.getInstance();
	// Assert.assertNotNull(originalScenario);
	// return originalScenario;
	// }

	// public static <E extends Exception> void withEvaluationRunner(@NonNull final LNGScenarioModel originalScenario, @NonNull final OptimiserSettings settings,
	// @NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, E> consumer) throws E {
	// withExecutorService(executorService -> {
	// final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, originalScenario, null, settings, LNGSchedulerJobUtils.createLocalEditingDomain(), null, createITSService(),
	// null, false);
	//
	// scenarioRunner.evaluateInitialState();
	// consumer.accept(scenarioRunner);
	// });
	// }
	//
	// public static <E extends Exception> void withLegacyEvaluationRunner(@NonNull final LNGScenarioModel originalScenario, @Nullable final Boolean withGCO,
	// @NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, E> consumer) throws E {
	//
	//
	// final OptimiserSettings settings = createExtendedSettings(ScenarioUtils.createDefaultSettings());
	// if (withGCO != null) {
	// settings.setGenerateCharterOuts(withGCO);
	// }
	//
	// @NonNull
	// public static LNGScenarioModel getScenarioModelFromURL(final URL url) throws IOException {
	// final URI uri = URI.createURI(FileLocator.toFileURL(url).toString().replaceAll(" ", "%20"));
	// return ServiceHelper.withCheckedService(IScenarioCipherProvider.class, (scenarioCipherProvider) -> {
	//
	// final ScenarioInstance instance = ScenarioStorageUtil.createInstanceFromURI(uri, scenarioCipherProvider);
	// Assert.assertNotNull(instance);
	// final LNGScenarioModel originalScenario = getScenarioModel(instance);
	// Assert.assertNotNull(originalScenario);
	// return originalScenario;
	// });
	// }

	public static void saveScenarioModel(@NonNull final LNGScenarioModel scenario, @NonNull final File destinationFile) throws IOException {
		ServiceHelper.withCheckedServiceConsumer(IMigrationRegistry.class, (migrationRegistry) -> {

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
							bind(CacheMode.class).annotatedWith(Names.named(SchedulerConstants.Key_VolumeAllocationCache)).toInstance(CacheMode.On);
							bind(CacheMode.class).annotatedWith(Names.named(SchedulerConstants.Key_VolumeAllocatedSequenceCache)).toInstance(CacheMode.On);
							bind(CacheMode.class).annotatedWith(Names.named(SchedulerConstants.Key_ProfitandLossCache)).toInstance(CacheMode.On);
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

	/**
	 * Load a scenario from the filesystem, migrating if required. The {@link ScenarioInstance} and an initial {@link ModelReference} is returned. Once unloaded, it is not expected that the scenario
	 * will be able to be reloaded.
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static <T extends EObject> @NonNull ScenarioInstance createFromModelInstance(T modelInstance) throws IOException {
		return ServiceHelper.withCheckedService(IMigrationRegistry.class, migrationRegistry -> {

			final ScenarioInstance newInstance = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();
			newInstance.setUuid(EcoreUtil.generateUUID());

			final String scenarioVersionContext = migrationRegistry.getDefaultMigrationContext();
			final String clientVersionContext = migrationRegistry.getDefaultClientMigrationContext();
			if (scenarioVersionContext != null) {
				newInstance.setVersionContext(scenarioVersionContext);
				int latestContextVersion = migrationRegistry.getLatestContextVersion(scenarioVersionContext);
				// Snapshot version - so find last good version number
				if (latestContextVersion < 0) {
					latestContextVersion = migrationRegistry.getLastReleaseVersion(scenarioVersionContext);
				}
				newInstance.setScenarioVersion(latestContextVersion);
			}
			if (clientVersionContext != null) {
				newInstance.setClientVersionContext(clientVersionContext);
				int latestClientContextVersion = migrationRegistry.getLatestClientContextVersion(clientVersionContext);
				// Snapshot version - so find last good version number
				if (latestClientContextVersion < 0) {
					latestClientContextVersion = migrationRegistry.getLastReleaseClientVersion(clientVersionContext);
				}
				newInstance.setClientScenarioVersion(latestClientContextVersion);
			}

			final Metadata metadata = ScenarioServiceFactory.eINSTANCE.createMetadata();
			metadata.setCreated(new Date());
			metadata.setLastModified(new Date());
			metadata.setContentType("com.mmxlabs.shiplingo.platform.models.manifest.scnfile");

			newInstance.setMetadata(metadata);

			@NonNull
			final ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(newInstance, (record, monitor) -> {
				// ServiceHelper.withOptionalService(IScenarioCipherProvider.class, scenarioCipherProvider -> {

				try {
					ResourceSet resourceSet = ResourceHelper.createResourceSet(null);

					final Pair<CommandProviderAwareEditingDomain, MMXAdaptersAwareCommandStack> p = ScenarioStorageUtil.initEditingDomain(resourceSet, modelInstance, newInstance);
					final InstanceData data = new InstanceData(record, modelInstance, p.getFirst(), p.getSecond(), (d) -> {
						throw new UnsupportedOperationException();
					}, (d) -> {

					});
					p.getSecond().setInstanceData(data);

					return data;
				} catch (final Exception e) {
					record.setLoadFailure(e);
					return null;
				}
			});

			return newInstance;
		});

	}

}
