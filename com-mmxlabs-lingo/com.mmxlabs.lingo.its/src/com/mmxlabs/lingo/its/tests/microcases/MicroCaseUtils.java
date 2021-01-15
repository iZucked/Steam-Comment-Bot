/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.IBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.impl.DefaultBreakEvenEvaluator;

public class MicroCaseUtils {
	public static void storeToFile(final @NonNull IScenarioDataProvider scenarioDataProvider, final String name) throws IOException {

		String path_template;

		// Check is runtime is Windows, else 'Unix'
		// TODO: Path should be refactored using the File.path api
		// Link:
		// https://www.sghill.net/how-do-i-make-cross-platform-file-paths-in-java.html
		if (System.getProperty("os.name").startsWith("Windows")) {
			path_template = "C://temp//%s.lingo";
		} else {
			path_template = "/tmp/%s.lingo";
		}

		ScenarioStorageUtil.storeCopyToFile(scenarioDataProvider, new File(String.format(path_template, name)));
	}

	public static void withInjectorPerChainScope(@NonNull final LNGScenarioToOptimiserBridge bridge, @NonNull final Runnable r) {

		@NonNull
		final Injector injector = bridge.getDataTransformer().getInjector();
		try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
			scope.enter();
			r.run();
		}
	}

	public static void withEvaluationInjectorPerChainScope(@NonNull final LNGScenarioToOptimiserBridge bridge, @NonNull final Consumer<Injector> r) {
		final Injector injector = MicroTestUtils.createEvaluationInjector(bridge.getDataTransformer());
		try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
			scope.enter();
			r.accept(injector);
		}
	}

	public static <T> T _getClassFromInjector(@NonNull final LNGScenarioToOptimiserBridge bridge, @NonNull final Class<T> clazz) {
		return bridge.getDataTransformer().getInjector().getInstance(clazz);
	}

	public static <T> T _getClassFromChildInjector(@NonNull final LNGScenarioToOptimiserBridge bridge, @NonNull final Class<T> clazz) {
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
					return Collections.<@NonNull Module>singletonList(new AbstractModule() {

						@Override
						protected void configure() {

							// Time windows
							bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.Key_UsePriceBasedWindowTrimming)).toInstance(Boolean.TRUE);
							bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.Key_UsePNLBasedWindowTrimming)).toInstance(Boolean.FALSE);
						}
					});
				} else if (moduleType == ModuleType.Module_Evaluation) {
					return Collections.<@NonNull Module>singletonList(new AbstractModule() {
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
