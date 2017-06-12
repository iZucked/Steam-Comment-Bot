/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.panamaslots;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.ops4j.peaberry.activation.util.PeaberryActivationModule;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.providers.IPanamaSlotsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPanamaSlotsProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.PanamaSlotsProviderEditor;

/**
 * This is a PeaberryActivationModule to hook in the restricted elements constraints into the optimisation. This in not intended to be registered directly in the MANIFEST.MF file, but installed in a
 * 
 * @link{TransformerActivatorModule
 * 
 * @author Simon Goodall
 * 
 */
public class PanamaSlotsModule extends PeaberryActivationModule {

	@Override
	protected void configure() {

		bindService(PanamaSlotsConstraintCheckerFactory.class).export();
		bindService(RestrictedElementsInjectorService.class).export();
		bindService(PanamaSlotsTransformerFactory.class).export();
	}

	/**
	 * An implementation of @link{IOptimiserInjectorService} to bind a @link{IRestrictedElementsProvider} to the DCP module
	 */
	public static class RestrictedElementsInjectorService implements IOptimiserInjectorService {

		@Override
		@Nullable
		public Module requestModule(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {

			if (moduleType == ModuleType.Module_DataComponentProviderModule) {
				return new AbstractModule() {

					@Override
					protected void configure() {
						final PanamaSlotsProviderEditor restrictedElementsProviderEditor = new PanamaSlotsProviderEditor();
						bind(IPanamaSlotsProvider.class).toInstance(restrictedElementsProviderEditor);
						bind(IPanamaSlotsProviderEditor.class).toInstance(restrictedElementsProviderEditor);
					}
				};
			}

			return null;
		}

		@Override
		@Nullable
		public List<@NonNull Module> requestModuleOverrides(ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
			return null;
		}
	}
}
