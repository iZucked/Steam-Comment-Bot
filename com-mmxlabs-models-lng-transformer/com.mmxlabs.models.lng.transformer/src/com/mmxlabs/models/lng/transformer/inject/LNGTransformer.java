/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ui.PlatformUI;
import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.util.TypeLiterals;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.transformer.IOptimisationTransformer;
import com.mmxlabs.models.lng.transformer.LNGScenarioTransformer;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.OptimisationTransformer;
import com.mmxlabs.models.lng.transformer.ResourcelessModelEntityMap;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGTransformerModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LSOConstructorModule;
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
	private Pair<IOptimisationContext, LocalSearchOptimiser> contextAndOptimiser;

	public LNGTransformer(final MMXRootObject scenario) {
		this(scenario, null);
	}

	@SuppressWarnings("unchecked")
	public LNGTransformer(final MMXRootObject scenario, final Module module) {
		this.scenario = scenario;
		{
			final Injector tmpInjector;
			if (PlatformUI.isWorkbenchRunning()) {
				// Create temp injector to grab extraModules from OSGi services
				final AbstractModule optimiserInjectorServiceModule = new AbstractModule() {

					@Override
					protected void configure() {
						bind(TypeLiterals.iterable(IOptimiserInjectorService.class)).toProvider(Peaberry.service(IOptimiserInjectorService.class).multiple());
					}
				};
				tmpInjector = Guice.createInjector(Peaberry.osgiModule(Activator.getDefault().getBundle().getBundleContext()), optimiserInjectorServiceModule);
				final Key<Iterable<? extends IOptimiserInjectorService>> key = Key.<Iterable<? extends IOptimiserInjectorService>> get(TypeLiterals.iterable(IOptimiserInjectorService.class));
				this.extraModules = (Iterable<IOptimiserInjectorService>) tmpInjector.getInstance(key);
			} else {
				tmpInjector = Guice.createInjector(module);
			}

			final Key<Iterable<? extends IOptimiserInjectorService>> key = Key.<Iterable<? extends IOptimiserInjectorService>> get(TypeLiterals.iterable(IOptimiserInjectorService.class));
			this.extraModules = (Iterable<IOptimiserInjectorService>) tmpInjector.getInstance(key);
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

		// Install standard module with optional overrides
		installModuleOverrides(modules, new DataComponentProviderModule(), moduleOverrides, IOptimiserInjectorService.ModuleType.Module_DataComponentProviderModule);
		// modules.add(new SequencesManipulatorModule());
		installModuleOverrides(modules, new LNGTransformerModule(scenario), moduleOverrides, IOptimiserInjectorService.ModuleType.Module_LNGTransformerModule);
		modules.add(new LSOConstructorModule());

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
	public Pair<IOptimisationContext, LocalSearchOptimiser> getOptimiserAndContext() {

		return contextAndOptimiser;
	}

	/**
	 * @since 2.0
	 */
	public Injector getInjector() {
		return injector;
	}
}
