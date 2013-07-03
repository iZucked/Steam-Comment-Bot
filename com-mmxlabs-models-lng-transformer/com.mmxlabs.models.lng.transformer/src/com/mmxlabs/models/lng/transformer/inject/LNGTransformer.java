/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Platform;
import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.util.TypeLiterals;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.IOptimisationTransformer;
import com.mmxlabs.models.lng.transformer.LNGScenarioTransformer;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.OptimisationTransformer;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGTransformerModule;
import com.mmxlabs.models.lng.transformer.inject.modules.OptimisationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.OptimiserSettingsModule;
import com.mmxlabs.models.lng.transformer.internal.Activator;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService.ModuleType;
import com.mmxlabs.scheduler.optimiser.providers.guice.DataComponentProviderModule;

/**
 * Helper class to create {@link LNGScenarioTransformer}, {@link OptimisationTransformer}, {@link IOptimisationData} and {@link ModelEntityMap} instances using Guice to inject components as necessary.
 * 
 * @author Simon Goodall
 * 
 */
public class LNGTransformer {

	/**
	 * @since 3.0
	 */
	public static final String HINT_OPTIMISE_LSO = "hint-lngtransformer-optimise-lso";

	private final LNGScenarioModel scenario;

	private final Injector injector;

	private Iterable<IOptimiserInjectorService> extraModules;

	private final OptimiserSettings optimiserSettings;

	/**
	 * @param scenario
	 * @param hints
	 * @since 5.0
	 */
	public LNGTransformer(final LNGScenarioModel scenario, final OptimiserSettings optimiserSettings, final String... hints) {
		this(scenario, optimiserSettings, null, null, hints);
	}

	/**
	 * @since 5.0
	 */
	public LNGTransformer(final LNGScenarioModel scenario, final OptimiserSettings optimiserSettings, final Map<IOptimiserInjectorService.ModuleType, List<Module>> localOverrides,
			final String... hints) {
		this(scenario, optimiserSettings, null, localOverrides, hints);
	}

	/**
	 * 
	 * @param scenario
	 * @param module
	 * @param hints
	 * @since 5.0
	 */
	public LNGTransformer(final LNGScenarioModel scenario, final OptimiserSettings optimiserSettings, final Module module, final String... hints) {
		this(scenario, optimiserSettings, module, null, hints);
	}

	/**
	 * @since 5.0
	 */
	public LNGTransformer(final LNGScenarioModel scenario, final OptimiserSettings optimiserSettings, final Module module,
			final Map<IOptimiserInjectorService.ModuleType, List<Module>> localOverrides, final String... hints) {
		this.scenario = scenario;
		this.optimiserSettings = optimiserSettings;

		boolean performOptimisation = false;
		// Check hints
		if (hints != null) {
			for (final String hint : hints) {
				if (HINT_OPTIMISE_LSO.equals(hint)) {
					performOptimisation = true;
				}
			}
		}

		{
			final Injector tmpInjector;
			if (Platform.isRunning()) {
				// Create temp injector to grab extraModules from OSGi services
				final AbstractModule optimiserInjectorServiceModule = new AbstractModule() {

					@Override
					protected void configure() {
						bind(TypeLiterals.iterable(IOptimiserInjectorService.class)).toProvider(Peaberry.service(IOptimiserInjectorService.class).multiple());
					}
				};
				final List<Module> m = new ArrayList<Module>(3);
				m.add(Peaberry.osgiModule(Activator.getDefault().getBundle().getBundleContext()));
				m.add(optimiserInjectorServiceModule);
				if (module != null) {
					m.add(module);
				}
				tmpInjector = Guice.createInjector(m);
			} else if (module != null) {
				tmpInjector = Guice.createInjector(module);
			} else {
				tmpInjector = null;
			}

			if (tmpInjector != null) {
				final Key<Iterable<? extends IOptimiserInjectorService>> key = Key.<Iterable<? extends IOptimiserInjectorService>> get(TypeLiterals.iterable(IOptimiserInjectorService.class));
				this.extraModules = (Iterable<IOptimiserInjectorService>) tmpInjector.getInstance(key);
			}
		}

		final List<Module> modules = new ArrayList<Module>();
		if (module != null) {
			modules.add(module);
		}

		final Map<IOptimiserInjectorService.ModuleType, List<Module>> moduleOverrides = new EnumMap<IOptimiserInjectorService.ModuleType, List<Module>>(IOptimiserInjectorService.ModuleType.class);

		// Grab any module overrides
		if (extraModules != null) {
			for (final IOptimiserInjectorService service : extraModules) {
				final Map<ModuleType, List<Module>> m = service.requestModuleOverrides(hints);
				if (m != null) {
					for (final Map.Entry<IOptimiserInjectorService.ModuleType, List<Module>> e : m.entrySet()) {
						List<Module> overrides;
						if (moduleOverrides.containsKey(e.getKey())) {
							overrides = moduleOverrides.get(e.getKey());
						} else {
							overrides = new ArrayList<Module>();
							moduleOverrides.put(e.getKey(), overrides);
						}
						overrides.addAll(e.getValue());
					}
				}
			}
		}
		// Process local overrides
		if (localOverrides != null) {
			for (final Map.Entry<IOptimiserInjectorService.ModuleType, List<Module>> e : localOverrides.entrySet()) {
				List<Module> overrides;
				if (moduleOverrides.containsKey(e.getKey())) {
					overrides = moduleOverrides.get(e.getKey());
				} else {
					overrides = new ArrayList<Module>();
					moduleOverrides.put(e.getKey(), overrides);
				}
				overrides.addAll(e.getValue());
			}
		}

		// Install standard module with optional overrides
		installModuleOverrides(modules, new DataComponentProviderModule(), moduleOverrides, IOptimiserInjectorService.ModuleType.Module_DataComponentProviderModule);

		installModuleOverrides(modules, new LNGTransformerModule(scenario, optimiserSettings), moduleOverrides, IOptimiserInjectorService.ModuleType.Module_LNGTransformerModule);

		installModuleOverrides(modules, new OptimiserSettingsModule(), moduleOverrides, IOptimiserInjectorService.ModuleType.Module_ParametersModule);

		if (performOptimisation) {
			installModuleOverrides(modules, new OptimisationModule(), moduleOverrides, IOptimiserInjectorService.ModuleType.Module_Optimisation);
		}

		// Insert extra modules into modules list
		if (extraModules != null) {
			for (final IOptimiserInjectorService service : extraModules) {
				final Module requestModule = service.requestModule();
				if (requestModule != null) {
					modules.add(requestModule);
				}
			}
		}
		// Create the master injector
		this.injector = Guice.createInjector(modules);

		injector.injectMembers(this);
	}

	private void installModuleOverrides(final List<Module> modules, final Module mainModule, final Map<IOptimiserInjectorService.ModuleType, List<Module>> moduleOverrides,
			final IOptimiserInjectorService.ModuleType moduleType) {
		if (moduleOverrides.containsKey(moduleType)) {
			final List<Module> overrides = moduleOverrides.get(moduleType);
			if (overrides != null && !overrides.isEmpty()) {
				modules.add(Modules.override(mainModule).with(overrides));
			} else {
				modules.add(mainModule);
			}
		} else {
			modules.add(mainModule);
		}
	}

	/**
	 * @since 2.0
	 */
	public IOptimisationTransformer getOptimisationTransformer() {
		return injector.getInstance(IOptimisationTransformer.class);
	}

	public MMXRootObject getScenario() {
		return scenario;
	}

	/**
	 * @since 3.0
	 */
	public ModelEntityMap getEntities() {
		return injector.getInstance(ModelEntityMap.class);
	}

	public LNGScenarioTransformer getLngScenarioTransformer() {
		return injector.getInstance(LNGScenarioTransformer.class);
	}

	public IOptimisationData getOptimisationData() {
		return injector.getInstance(IOptimisationData.class);
	}

	/**
	 * @since 2.0
	 */
	public IOptimisationContext getOptimisationContext() {
		return injector.getInstance(IOptimisationContext.class);
	}

	/**
	 * @since 2.0
	 */
	public LocalSearchOptimiser getOptimiser() {
		return injector.getInstance(LocalSearchOptimiser.class);
	}

	/**
	 * @since 2.0
	 */
	public Injector getInjector() {
		return injector;
	}

	/**
	 * @since 5.0
	 */
	public OptimiserSettings getOptimiserSettings() {
		return optimiserSettings;
	}
}
