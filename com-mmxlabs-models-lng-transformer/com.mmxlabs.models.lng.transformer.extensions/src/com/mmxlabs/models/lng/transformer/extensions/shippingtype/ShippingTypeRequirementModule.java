/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.shippingtype;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
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
public class ShippingTypeRequirementModule extends PeaberryActivationModule {

	@Override
	protected void configure() {

		bindService(ShippingTypeRequirementConstraintCheckerFactory.class).export();
		bindService(DesPermissionInjectorService.class).export();
		bindService(ShippingTypeRequirementTransformerFactory.class).export();
	}

	/**
	 * Module to contribute the DCP to the builder via the @link{RestrictedElementsInjectorService}
	 * 
	 * 
	 */
	public static class DesPermissionDCPModule extends AbstractModule {
		@Override
		protected void configure() {

			final HashMapShippingTypeRequirementProvider desPermissionProviderEditor = new HashMapShippingTypeRequirementProvider();
			bind(IShippingTypeRequirementProvider.class).toInstance(desPermissionProviderEditor);
			bind(IShippingTypeRequirementProviderEditor.class).toInstance(desPermissionProviderEditor);
		}
	}

	/**
	 * An implementation of @link{IOptimiserInjectorService} to bind a @link{IRestrictedElementsProvider} to the DCP module
	 */
	public static class DesPermissionInjectorService implements IOptimiserInjectorService {

		@Override
		@Nullable
		public Module requestModule(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
			return null;
		}

		@Override
		@Nullable
		public List<@NonNull Module> requestModuleOverrides(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
			if (moduleType == ModuleType.Module_DataComponentProviderModule) {
				return Collections.<@NonNull Module> singletonList(new DesPermissionDCPModule());
			}
			return null;
		}
	}
}
