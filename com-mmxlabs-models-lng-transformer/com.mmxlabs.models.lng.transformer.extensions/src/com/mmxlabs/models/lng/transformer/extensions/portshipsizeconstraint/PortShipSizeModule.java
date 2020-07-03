/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.portshipsizeconstraint;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.ops4j.peaberry.activation.util.PeaberryActivationModule;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

/**
 * This is a PeaberryActivationModule to hook in the restricted elements constraints into the optimisation.
 * This in not intended to be registered directly in the MANIFEST.MF file.
 */
public class PortShipSizeModule extends PeaberryActivationModule {

	@Override
	protected void configure() {

		bindService(PortShipSizeConstraintCheckerFactory.class).export();
		bindService(PortShipSizeInjectorService.class).export();
		bindService(PortShipSizeTransformerFactory.class).export();
	}

	/**
	 * An implementation of @link{IOptimiserInjectorService} to bind a @link{IRestrictedSlotsProvider} to the DCP module
	 */
	public static class PortShipSizeInjectorService implements IOptimiserInjectorService {

		@Override
		@Nullable
		public Module requestModule(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {

			if (moduleType == ModuleType.Module_DataComponentProviderModule) {
				return new AbstractModule() {

					@Override
					protected void configure() {
						final HashMapPortShipSizeProviderEditor portShipSizeProviderEditor = new HashMapPortShipSizeProviderEditor();
						bind(IPortShipSizeProvider.class).toInstance(portShipSizeProviderEditor);
						bind(IPortShipSizeProviderEditor.class).toInstance(portShipSizeProviderEditor);
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
