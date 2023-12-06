package com.mmxlabs.models.lng.transformer.extensions.euets;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.ops4j.peaberry.activation.util.PeaberryActivationModule;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.providers.IEuEtsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IEuEtsProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.DefaultEuEtsProviderEditor;

public class EuEtsModule extends PeaberryActivationModule {
	@Override
	protected void configure() {
		bindService(EuEtsInjectorService.class).export();
		bindService(EuEtsTransformerFactory.class).export();
	}

	public static class EuEtsInjectorService implements IOptimiserInjectorService {
		@Override
		public @Nullable Module requestModule(@NonNull ModuleType moduleType, @NonNull Collection<@NonNull String> hints) {
			if (moduleType == ModuleType.Module_DataComponentProviderModule) {
				return new AbstractModule() {
					@Override
					protected void configure() {
						final IEuEtsProviderEditor euEtsProviderEditor = new DefaultEuEtsProviderEditor();
						bind(IEuEtsProviderEditor.class).toInstance(euEtsProviderEditor);
						bind(IEuEtsProvider.class).toInstance(euEtsProviderEditor);
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
