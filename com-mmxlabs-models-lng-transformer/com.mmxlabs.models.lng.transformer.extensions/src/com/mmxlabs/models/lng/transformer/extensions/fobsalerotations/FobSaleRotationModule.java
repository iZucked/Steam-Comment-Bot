package com.mmxlabs.models.lng.transformer.extensions.fobsalerotations;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.ops4j.peaberry.activation.util.PeaberryActivationModule;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.providers.IFobSaleRotationProvider;
import com.mmxlabs.scheduler.optimiser.providers.IFobSaleRotationProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.DefaultFobSaleRotationProviderEditor;

public class FobSaleRotationModule extends PeaberryActivationModule {

	@Override
	protected void configure() {
		bindService(FobSaleRotationInjectorService.class).export();
		bindService(FobSaleRotationTransformerFactory.class).export();
		bindService(FobSaleRotationExporterExtensionFactory.class).export();
	}

	public static class FobSaleRotationInjectorService implements IOptimiserInjectorService {
		@Override
		public @Nullable Module requestModule(@NonNull ModuleType moduleType, @NonNull Collection<@NonNull String> hints) {
			if (moduleType == ModuleType.Module_DataComponentProviderModule) {
				return new AbstractModule() {
					@Override
					protected void configure() {
						final IFobSaleRotationProviderEditor fobSaleRotationProviderEditor = new DefaultFobSaleRotationProviderEditor();
						bind(IFobSaleRotationProvider.class).toInstance(fobSaleRotationProviderEditor);
						bind(IFobSaleRotationProviderEditor.class).toInstance(fobSaleRotationProviderEditor);
						bind(boolean.class).annotatedWith(Names.named(LNGTransformerHelper.HINT_NONSHIPPED_ROTATIONS)).toInstance(hints.contains(LNGTransformerHelper.HINT_NONSHIPPED_ROTATIONS));
					}
				};
			}
			return null;
		}

		@Override
		public @Nullable List<@NonNull Module> requestModuleOverrides(@NonNull ModuleType moduleType, @NonNull Collection<@NonNull String> hints) {
			return null;
		}
	}
}
