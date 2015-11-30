/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.mmxlabs.lingo.its.internal.Activator;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformer;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModeExtender;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModesRegistry;
import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.models.migration.scenario.MigrationHelper;
import com.mmxlabs.scenario.service.manifest.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.util.encryption.IScenarioCipherProvider;

public class LNGScenarioRunnerCreator {

	@NonNull

	public static LNGScenarioRunner createScenarioRunner(@NonNull final LNGScenarioModel originalScenario) {
		return createScenarioRunner(originalScenario, LNGScenarioRunner.createDefaultSettings());
	}

	@NonNull
	public static LNGScenarioRunner createScenarioRunner(@NonNull final LNGScenarioModel originalScenario, @NonNull final OptimiserSettings settings) {
		final LNGScenarioRunner originalScenarioRunner = new LNGScenarioRunner(originalScenario, settings, LNGTransformer.HINT_OPTIMISE_LSO);
		return originalScenarioRunner;
	}

	@NonNull
	public static LNGScenarioModel getScenarioModel(@NonNull final ScenarioInstance instance) throws IOException {
		MigrationHelper.migrateAndLoad(instance);

		final LNGScenarioModel originalScenario = (LNGScenarioModel) instance.getInstance();
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

	@NonNull
	public static LNGScenarioRunner createScenarioRunner(final @NonNull URL url) throws IOException {
		return createScenarioRunner(getScenarioModelFromURL(url));
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
}
