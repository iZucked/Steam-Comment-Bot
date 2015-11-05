/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.restrictedelements;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.ops4j.peaberry.activation.util.PeaberryActivationModule;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

/**
 * This is a PeaberryActivationModule to hook in the restricted elements constraints into the optimisation. This in not intended to be registered directly in the MANIFEST.MF file, but installed in a
 * 
 * @link{TransformerActivatorModule
 * 
 * @author Simon Goodall
 * 
 */
public class RestrictedElementsModule extends PeaberryActivationModule {

	@Override
	protected void configure() {

		bindService(RestrictedElementsConstraintCheckerFactory.class).export();
		bindService(RestrictedElementsInjectorService.class).export();
		bindService(RestrictedElementsTransformerFactory.class).export();
	}

	/**
	 * An implementation of @link{IOptimiserInjectorService} to bind a @link{IRestrictedElementsProvider} to the DCP module
	 */
	public static class RestrictedElementsInjectorService implements IOptimiserInjectorService {

		@Override
		public Module requestModule(@NonNull final ModuleType moduleType, @NonNull final Collection<String> hints) {

			if (moduleType == ModuleType.Module_DataComponentProviderModule) {
				return new AbstractModule() {

					@Override
					protected void configure() {
						final HashMapRestrictedElementsProviderEditor restrictedElementsProviderEditor = new HashMapRestrictedElementsProviderEditor();
						bind(IRestrictedElementsProvider.class).toInstance(restrictedElementsProviderEditor);
						bind(IRestrictedElementsProviderEditor.class).toInstance(restrictedElementsProviderEditor);
					}
				};
			}

			return null;
		}

		@Override
		public List<Module> requestModuleOverrides(ModuleType moduleType, @NonNull final Collection<String> hints) {
			return null;
		}
	}
}
