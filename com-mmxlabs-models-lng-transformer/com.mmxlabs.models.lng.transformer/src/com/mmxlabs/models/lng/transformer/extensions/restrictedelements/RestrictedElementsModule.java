/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.restrictedelements;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.ops4j.peaberry.activation.util.PeaberryActivationModule;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

/**
 * This is a PeaberryActivationModule to hook in the restricted elements constraints into the optimisation. This in not intended to be registered directly in the MANIFEST.MF file, but installed in a
 * 
 * @link{TransformerActivatorModule}
 * 
 * @author Simon Goodall
 * @since 2.0
 * 
 */
public class RestrictedElementsModule extends PeaberryActivationModule {

	public static final String DCP_restrictedElementProvider = "provider-restricted-elements";

	@Override
	protected void configure() {

		bindService(RestrictedElementsConstraintCheckerFactory.class).export();
		bindService(RestrictedElementsInjectorService.class).export();
	}

	/**
	 * Module to contribute the DCP to the builder via the @link{RestrictedElementsInjectorService}
	 * 
	 * 
	 */
	public static class RestrictedElementsDCPModule extends AbstractModule {
		@Override
		protected void configure() {

			final HashMapRestrictedElementsProviderEditor restrictedElementsProviderEditor = new HashMapRestrictedElementsProviderEditor(DCP_restrictedElementProvider);
			bind(IRestrictedElementsProvider.class).toInstance(restrictedElementsProviderEditor);
			bind(IRestrictedElementsProviderEditor.class).toInstance(restrictedElementsProviderEditor);
		}
	}

	/**
	 * An implementation of @link{IOptimiserInjectorService} to bind a @link{IRestrictedElementsProvider} to the DCP module
	 */
	public static class RestrictedElementsInjectorService implements IOptimiserInjectorService {

		@Override
		public Module requestModule(final String... hints) {
			return null;
		}

		@Override
		public Map<ModuleType, List<Module>> requestModuleOverrides(final String... hints) {
			final Map<ModuleType, List<Module>> map = new EnumMap<ModuleType, List<Module>>(ModuleType.class);
			map.put(ModuleType.Module_DataComponentProviderModule, Collections.<Module> singletonList(new RestrictedElementsDCPModule()));

			return map;
		}
	}
}
