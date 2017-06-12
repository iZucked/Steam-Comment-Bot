/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.manifest.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.IBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.impl.DefaultBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduling.EarliestSlotTimeScheduler;
import com.mmxlabs.scheduler.optimiser.scheduling.ISlotTimeScheduler;

public class MicroCaseUtils {

	public static void storeToFile(final LNGScenarioModel lngScenarioModel, final String name) throws IOException {
		ServiceHelper.withCheckedServiceConsumer(IMigrationRegistry.class, migrationRegistry -> {
			storeToFile(lngScenarioModel, migrationRegistry, new File(String.format("C://temp//%s.lingo", name)));
		});
	}

	public static void storeToFile(final LNGScenarioModel lngScenarioModel, final IMigrationRegistry migrationRegistry, final File output) throws IOException {
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
		instance.setInstance(lngScenarioModel);

		ScenarioStorageUtil.storeToFile(instance, output);
	}

	public static void withInjectorPerChainScope(@NonNull final LNGScenarioToOptimiserBridge bridge, @NonNull final Runnable r) {

		@NonNull
		final Injector injector = bridge.getDataTransformer().getInjector();
		try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
			scope.enter();
			r.run();
		}
	}

	public static <T> T getClassFromInjector(@NonNull final LNGScenarioToOptimiserBridge bridge, @NonNull final Class<T> clazz) {
		return bridge.getDataTransformer().getInjector().getInstance(clazz);
	}

	public static <T> T getClassFromChildInjector(@NonNull final LNGScenarioToOptimiserBridge bridge, @NonNull final Class<T> clazz) {
		return bridge.getDataTransformer().getInjector().createChildInjector().getInstance(clazz);
	}

	@Nullable
	public static <T> T getOptimiserObjectFromEMF(@NonNull final LNGScenarioToOptimiserBridge bridge, @NonNull final EObject object, @NonNull final Class<T> clazz) {
		final ModelEntityMap modelEntityMap = bridge.getDataTransformer().getModelEntityMap();
		@Nullable
		final T optimiserObject = modelEntityMap.getOptimiserObject(object, clazz);
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
				if (moduleType == ModuleType.Module_LNGTransformerModule) {
					return Collections.<@NonNull Module> singletonList(new AbstractModule() {

						@Override
						protected void configure() {

							// Time windows
							bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.Key_UsePriceBasedWindowTrimming)).toInstance(Boolean.TRUE);
							bind(ISlotTimeScheduler.class).to(EarliestSlotTimeScheduler.class);

						}
					});
				} else if (moduleType == ModuleType.Module_Evaluation) {
					return Collections.<@NonNull Module> singletonList(new AbstractModule() {
						@Override
						protected void configure() {
							bind(IBreakEvenEvaluator.class).to(DefaultBreakEvenEvaluator.class);
						}
					});
				}
				return null;
			}
		};
	}

}
