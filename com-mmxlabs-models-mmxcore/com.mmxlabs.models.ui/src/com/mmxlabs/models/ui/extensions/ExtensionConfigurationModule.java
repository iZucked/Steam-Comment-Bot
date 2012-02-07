package com.mmxlabs.models.ui.extensions;

import static org.ops4j.peaberry.Peaberry.osgiModule;
import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.eclipse.EclipseRegistry.eclipseRegistry;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import com.google.inject.AbstractModule;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;
import com.mmxlabs.models.ui.registries.IDisplayCompositeFactoryRegistry;
import com.mmxlabs.models.ui.registries.IEditorFactoryRegistry;
import com.mmxlabs.models.ui.registries.IReferenceValueProviderFactoryRegistry;
import com.mmxlabs.models.ui.registries.impl.ComponentHelperRegistry;
import com.mmxlabs.models.ui.registries.impl.DisplayCompositeFactoryRegistry;
import com.mmxlabs.models.ui.registries.impl.EditorFactoryRegistry;
import com.mmxlabs.models.ui.registries.impl.ReferenceValueProviderFactoryRegistry;

public class ExtensionConfigurationModule extends AbstractModule {
	@Override
	protected void configure() {
		install(osgiModule(Activator.getDefault().getBundle()
				.getBundleContext(), eclipseRegistry()));

		bind(iterable(IDisplayCompositeFactoryExtension.class)).toProvider(
				service(IDisplayCompositeFactoryExtension.class).multiple());
		
		bind(IDisplayCompositeFactoryRegistry.class).to(DisplayCompositeFactoryRegistry.class);
		bind(IComponentHelperRegistry.class).to(ComponentHelperRegistry.class);
		bind(IEditorFactoryRegistry.class).to(EditorFactoryRegistry.class);
		bind(IReferenceValueProviderFactoryRegistry.class).to(ReferenceValueProviderFactoryRegistry.class);
	}
}
