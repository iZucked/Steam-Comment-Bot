/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.PlatformUI;
import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.util.TypeLiterals;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.mmxlabs.models.lng.transformer.IncompleteScenarioException;
import com.mmxlabs.models.lng.transformer.LNGScenarioTransformer;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.OptimisationTransformer;
import com.mmxlabs.models.lng.transformer.ResourcelessModelEntityMap;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGTransformerModule;
import com.mmxlabs.models.lng.transformer.internal.Activator;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.manipulators.SequencesManipulatorModule;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
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

	private IOptimisationData optimisationData;

	private final OptimisationTransformer optimisationTransformer;

	// @Inject(optional = true)
	private Iterable<IOptimiserInjectorService> extraModules;

	public LNGTransformer(final MMXRootObject scenario) {
		this(scenario, null);
	}

	public LNGTransformer(final MMXRootObject scenario, final Module module) {
		this.scenario = scenario;
		{
			final Injector injector;
			if (PlatformUI.isWorkbenchRunning()) {
				// Create temp injector to grab extraModules
				// TODO: DOuble injector is not great.... nor is internal temp class
				final AbstractModule abstractModule = new AbstractModule() {

					@Override
					protected void configure() {
						bind(TypeLiterals.iterable(IOptimiserInjectorService.class)).toProvider(Peaberry.service(IOptimiserInjectorService.class).multiple());
					}
				};
				injector = Guice.createInjector(Peaberry.osgiModule(Activator.getDefault().getBundle().getBundleContext()), abstractModule);
			} else {
				injector = Guice.createInjector();

			}
			class Internal {
				@Inject(optional = true)
				public Iterable<IOptimiserInjectorService> extraModules;
			}
			final Internal internal = new Internal();
			injector.injectMembers(internal);
			this.extraModules = internal.extraModules;
		}

		final List<Module> modules = new ArrayList<Module>();
		if (module != null) {
			modules.add(module);
		}
		modules.add(new DataComponentProviderModule());
		modules.add(new SequencesManipulatorModule());
		modules.add(new LNGTransformerModule(scenario));

		// TODO: Add specific ones here....
		if (extraModules != null) {
			for (final IOptimiserInjectorService service : extraModules) {
				modules.add(service.requestModule());
			}
		}
		this.injector = Guice.createInjector(modules);

		injector.injectMembers(this);

		entities.setScenario(scenario);

		try {
			optimisationData = lngScenarioTransformer.createOptimisationData(entities);
		} catch (final IncompleteScenarioException e) {
			throw new RuntimeException(e);
		}
		optimisationTransformer = new OptimisationTransformer(scenario, lngScenarioTransformer.getOptimisationSettings());
		injector.injectMembers(optimisationTransformer);
	}

	public synchronized LNGScenarioTransformer getLNGScenarioTransformer() {
		return lngScenarioTransformer;
	}

	public OptimisationTransformer getOptimisationTransformer() {
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
}
