/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.Assert;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.lingo.its.internal.Activator;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_OptimiserSettingsModule;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModeExtender;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModesRegistry;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.models.migration.scenario.MigrationHelper;
import com.mmxlabs.scenario.service.manifest.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.util.encryption.IScenarioCipherProvider;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.EnumeratingSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class LNGScenarioRunnerCreator {

	@NonNull
	public static LNGScenarioRunner createScenarioRunnerForEvaluationWithGCO(@NonNull final LNGScenarioModel originalScenario) throws IOException {
		//
		OptimiserSettings optimiserSettings = ScenarioUtils.createDefaultSettings();
		optimiserSettings = createExtendedSettings(optimiserSettings);

		optimiserSettings.setGenerateCharterOuts(true);

		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		try {
			final LNGScenarioRunner scenarioRunner = LNGScenarioRunnerCreator.createScenarioRunnerWithLSO(executorService, originalScenario, optimiserSettings);
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
	public static LNGScenarioRunner createScenarioRunnerWithLSO(@NonNull final ExecutorService executorService, final @NonNull URL url) throws IOException {
		return createScenarioRunnerWithLSO(executorService, getScenarioModelFromURL(url), null, null);
	}

	@NonNull
	public static LNGScenarioRunner createScenarioRunnerForEvaluation(@NonNull final LNGScenarioModel originalScenario, @Nullable Boolean withGCO) {

		final OptimiserSettings settings = createExtendedSettings(ScenarioUtils.createDefaultSettings());
		if (withGCO != null) {
			settings.setGenerateCharterOuts(withGCO);
		}
		return createScenarioRunnerForEvaluation(originalScenario, settings);

	}

	@NonNull
	public static LNGScenarioRunner createScenarioRunnerWithLSO(@NonNull final ExecutorService executorService, @NonNull final LNGScenarioModel originalScenario, @Nullable Boolean withGCO,
			@Nullable Integer lsoIterations) {
		final OptimiserSettings settings = createExtendedSettings(ScenarioUtils.createDefaultSettings());
		if (withGCO != null) {
			settings.setGenerateCharterOuts(withGCO);
		}
		if (lsoIterations != null) {
			settings.getAnnealingSettings().setIterations(lsoIterations);
		}

		return createScenarioRunnerWithLSO(executorService, originalScenario, settings);
	}

	@NonNull
	public static LNGScenarioRunner createScenarioRunnerForEvaluation(@NonNull final LNGScenarioModel originalScenario, @NonNull final OptimiserSettings settings) {

		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		try {
			final LNGScenarioRunner originalScenarioRunner = new LNGScenarioRunner(executorService, originalScenario, null, settings, LNGSchedulerJobUtils.createLocalEditingDomain(), null,
					createITSService(), null, true);

			originalScenarioRunner.evaluateInitialState();

			return originalScenarioRunner;
		} finally {
			executorService.shutdown();
		}
	}

	@NonNull
	public static LNGScenarioRunner createScenarioRunnerWithLSO(@NonNull final ExecutorService executorService, @NonNull final LNGScenarioModel originalScenario,
			@NonNull final OptimiserSettings settings) {
		final LNGScenarioRunner originalScenarioRunner = new LNGScenarioRunner(executorService, originalScenario, null, settings, LNGSchedulerJobUtils.createLocalEditingDomain(), null,
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

		final BundleContext bundleContext = FrameworkUtil.getBundle(AbstractOptimisationResultTester.class).getBundleContext();
		final ServiceReference<IScenarioCipherProvider> serviceReference = bundleContext.getServiceReference(IScenarioCipherProvider.class);
		try {
			final ScenarioInstance instance = ScenarioStorageUtil.loadInstanceFromURI(uri, bundleContext.getService(serviceReference));
			Assert.assertNotNull(instance);
			final LNGScenarioModel originalScenario = getScenarioModel(instance);
			Assert.assertNotNull(originalScenario);
			return originalScenario;
		} finally {
			bundleContext.ungetService(serviceReference);
		}
	}

	public static void saveScenarioModel(@NonNull final LNGScenarioModel scenario, @NonNull final File destinationFile) throws IOException {

		final LNGScenarioModel copy = EcoreUtil.copy(scenario);
		final IMigrationRegistry migrationRegistry = Activator.getDefault().getMigrationRegistry();
		final String context = migrationRegistry.getDefaultMigrationContext();
		if (context == null) {
			throw new NullPointerException("Context cannot be null");
		}
		int version = migrationRegistry.getLatestContextVersion(context);
		if (version < 0) {
			version = migrationRegistry.getLastReleaseVersion(context);
		}
		ScenarioTools.storeToFile(copy, destinationFile, context, version);
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
	@NonNull
	public static OptimiserSettings createExtendedSettings(@NonNull final OptimiserSettings optimiserSettings) {
		IParameterModesRegistry parameterModesRegistry = null;

		final Activator activator = Activator.getDefault();
		if (activator != null) {
			parameterModesRegistry = activator.getParameterModesRegistry();
		}

		if (parameterModesRegistry != null) {
			final Collection<IParameterModeExtender> extenders = parameterModesRegistry.getExtenders();
			if (extenders != null) {
				for (final IParameterModeExtender extender : extenders) {
					extender.extend(optimiserSettings, null);
				}
			}
		}
		return optimiserSettings;
	}

	/**
	 * Special optimiser injection service to disable special deployment settings during ITS runs
	 * 
	 * @return
	 */
	@NonNull
	public static IOptimiserInjectorService createITSService() {

		return new IOptimiserInjectorService() {

			@Override
			public Module requestModule(@NonNull final ModuleType moduleType, @NonNull final Collection<String> hints) {
				return null;
			}

			@Override
			public List<Module> requestModuleOverrides(@NonNull final ModuleType moduleType, @NonNull final Collection<String> hints) {
				if (moduleType == ModuleType.Module_EvaluationParametersModule) {
					return Collections.<Module> singletonList(new AbstractModule() {

						@Override
						protected void configure() {

						}

						@Provides
						@Named(EnumeratingSequenceScheduler.OPTIMISER_REEVALUATE)
						private boolean isOptimiserReevaluating() {
							return false;
						}
					});
				}
				if (moduleType == ModuleType.Module_OptimisationParametersModule) {
					return Collections.<Module> singletonList(new AbstractModule() {

						@Override
						protected void configure() {

						}

						@Provides
						@Named(LNGParameters_OptimiserSettingsModule.PROPERTY_MMX_HALF_SPEED_ACTION_SETS)
						private boolean isHalfSpeedActionSets() {
							return false;
						}
					});

				}
				return null;
			}
		};
	}
}
