/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Platform;
import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.util.TypeLiterals;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.mmxlabs.models.lng.transformer.IOptimisationTransformer;
import com.mmxlabs.models.lng.transformer.LNGScenarioTransformer;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.OptimisationTransformer;
import com.mmxlabs.models.lng.transformer.ResourcelessModelEntityMap;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGTransformerModule;
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

	private final MMXRootObject scenario;

	private final Injector injector;

	@Inject
	private ResourcelessModelEntityMap entities;

	@Inject
	private LNGScenarioTransformer lngScenarioTransformer;

	@Inject
	private IOptimisationData optimisationData;
	@Inject
	private IOptimisationTransformer optimisationTransformer;

	// @Inject(optional = true)
	private Iterable<IOptimiserInjectorService> extraModules;

	@Inject
	private IOptimisationContext context;

	@Inject
	private LocalSearchOptimiser optimiser;

	private final Map<IOptimiserInjectorService.ModuleType, List<Module>> localOverrides = new HashMap<IOptimiserInjectorService.ModuleType, List<Module>>();

	public LNGTransformer(final MMXRootObject scenario) {
		this(scenario, null);
	}

	@SuppressWarnings("unchecked")
	public LNGTransformer(final MMXRootObject scenario, final Module module) {
		this.scenario = scenario;
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
				final Map<ModuleType, List<Module>> m = service.requestModuleOverrides();
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
		for (Map.Entry<IOptimiserInjectorService.ModuleType, List<Module>> e : localOverrides.entrySet()) {
			List<Module> overrides;
			if (moduleOverrides.containsKey(e.getKey())) {
				overrides = moduleOverrides.get(e.getKey());
			} else {
				overrides = new ArrayList<Module>();
				moduleOverrides.put(e.getKey(), overrides);
			}
			overrides.addAll(e.getValue());
		}

		// Install standard module with optional overrides
		installModuleOverrides(modules, new DataComponentProviderModule(), moduleOverrides, IOptimiserInjectorService.ModuleType.Module_DataComponentProviderModule);
		// modules.add(new SequencesManipulatorModule());
		installModuleOverrides(modules, new LNGTransformerModule(scenario), moduleOverrides, IOptimiserInjectorService.ModuleType.Module_LNGTransformerModule);

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
		// Might no be required, could be done using @Inject on the method
		entities.setScenario(scenario);
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

	public synchronized LNGScenarioTransformer getLNGScenarioTransformer() {
		return lngScenarioTransformer;
	}

	/**
	 * @since 2.0
	 */
	public IOptimisationTransformer getOptimisationTransformer() {
		return optimisationTransformer;
	}

	public MMXRootObject getScenario() {
		return scenario;
	}

	public ResourcelessModelEntityMap getEntities() {
		return entities;
	}

	public LNGScenarioTransformer getLngScenarioTransformer() {
		return lngScenarioTransformer;
	}

	public IOptimisationData getOptimisationData() {
		return optimisationData;
	}

	/**
	 * @since 2.0
	 */
	public IOptimisationContext getOptimisationContext() {
		return context;
	}

	/**
	 * @since 2.0
	 */
	public LocalSearchOptimiser getOptimiser() {
		return optimiser;
	}

	/**
	 * @since 2.0
	 */
	public Injector getInjector() {
		return injector;
	}

	/**
	 * Manually add an override {@link Module} for the given IOptimiserInjectorService.ModuleType
	 * 
	 * @since 2.0
	 */

	public void addModuleOverride(final IOptimiserInjectorService.ModuleType moduleType, final Module module) {
		final List<Module> modules;
		if (localOverrides.containsKey(moduleType)) {
			modules = localOverrides.get(moduleType);
		} else {
			modules = new LinkedList<Module>();
			localOverrides.put(moduleType, modules);
		}
		modules.add(module);
	}
}
