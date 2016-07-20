/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScope;
import com.mmxlabs.scenario.service.manifest.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scheduler.optimiser.fitness.ISequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.PriceBasedSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.IBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.impl.DefaultBreakEvenEvaluator;

public class MicroCaseUtils {
	private void storeToFile(final LNGScenarioModel lngScenarioModel, final String name) throws IOException {
		final BundleContext bundleContext = FrameworkUtil.getBundle(ScenarioStorageUtil.class).getBundleContext();
		final ServiceReference<IMigrationRegistry> serviceReference = bundleContext.getServiceReference(IMigrationRegistry.class);
		try {
			final IMigrationRegistry migrationRegistry = bundleContext.getService(serviceReference);
			assert migrationRegistry != null;
			storeToFile(lngScenarioModel, migrationRegistry, new File(String.format("C://temp//%s.lingo", name)));
		} finally {
			bundleContext.ungetService(serviceReference);
		}
	}

	private void storeToFile(final LNGScenarioModel lngScenarioModel, final IMigrationRegistry migrationRegistry, final File output) throws IOException {

		final ScenarioInstance instance = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();

		final String scenarioVersionContext = migrationRegistry.getDefaultMigrationContext();
		final String clientVersionContext = migrationRegistry.getDefaultClientMigrationContext();
		if (scenarioVersionContext != null) {
			instance.setVersionContext(scenarioVersionContext);
			int latestContextVersion = migrationRegistry.getLatestContextVersion(scenarioVersionContext);
			// Snapshot version - so find last good version number
			if (latestContextVersion < 0) {
				latestContextVersion = migrationRegistry.getLastReleaseVersion(scenarioVersionContext);
			}
			instance.setScenarioVersion(latestContextVersion);
		}
		if (clientVersionContext != null) {
			instance.setClientVersionContext(clientVersionContext);
			int latestClientContextVersion = migrationRegistry.getLatestClientContextVersion(clientVersionContext);
			// Snapshot version - so find last good version number
			if (latestClientContextVersion < 0) {
				latestClientContextVersion = migrationRegistry.getLastReleaseClientVersion(clientVersionContext);
			}
			instance.setClientScenarioVersion(latestClientContextVersion);
		}

		final Metadata metadata = ScenarioServiceFactory.eINSTANCE.createMetadata();
		metadata.setCreated(new Date());
		metadata.setLastModified(new Date());
		metadata.setContentType("com.mmxlabs.shiplingo.platform.models.manifest.scnfile");

		instance.setMetadata(metadata);

		instance.setInstance(EcoreUtil.copy(lngScenarioModel));
		ScenarioStorageUtil.storeToFile(instance, output);

	}
	
	public static <T> T getClassFromInjector(@NonNull LNGScenarioToOptimiserBridge bridge, @NonNull Class<T> clazz) {
		return bridge.getDataTransformer().getInjector().getInstance(clazz);
	}
	
	public static <T> T getClassFromChildInjector(@NonNull LNGScenarioToOptimiserBridge bridge, @NonNull Class<T> clazz) {
		return bridge.getDataTransformer().getInjector().createChildInjector().getInstance(clazz);
	}

	@Nullable
	public static <T> T getOptimiserObjectFromEMF(@NonNull LNGScenarioToOptimiserBridge bridge, @NonNull EObject object, @NonNull Class<T> clazz) {
		ModelEntityMap modelEntityMap = bridge.getDataTransformer().getModelEntityMap();
		@Nullable
		T optimiserObject = modelEntityMap.getOptimiserObject(object, clazz);
		return optimiserObject;
	}

	public static IOptimiserInjectorService getInjectorServiceWithPriceBasedScheduler() {
		return new IOptimiserInjectorService() {
			@Override
			public Module requestModule(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
				return null;
			}

			@Override
			@Nullable
			public List<@NonNull Module> requestModuleOverrides(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
				if (moduleType == ModuleType.Module_Evaluation) {
					return Collections.<@NonNull Module> singletonList(new AbstractModule() {
						@Override
						protected void configure() {
							bind(IBreakEvenEvaluator.class).to(DefaultBreakEvenEvaluator.class);
							// time windows
							bind(PriceBasedSequenceScheduler.class).in(PerChainUnitScope.class);
							bind(ISequenceScheduler.class).to(PriceBasedSequenceScheduler.class);
						}
					});
				}
				return null;
			}
		};
	}

}
