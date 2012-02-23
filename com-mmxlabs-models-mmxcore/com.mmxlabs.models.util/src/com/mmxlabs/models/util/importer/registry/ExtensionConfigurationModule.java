package com.mmxlabs.models.util.importer.registry;

import static org.ops4j.peaberry.Peaberry.osgiModule;
import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.eclipse.EclipseRegistry.eclipseRegistry;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import org.osgi.framework.BundleContext;

import com.google.inject.AbstractModule;
import com.mmxlabs.models.util.importer.registry.impl.ImporterRegistry;

/**
 * A guice module which wires up the various extension point registries to
 * the actual extension points, and their interfaces to their implementations.
 * 
 * @author hinton
 *
 */
public class ExtensionConfigurationModule extends AbstractModule {
	private final BundleContext context;
	
	public ExtensionConfigurationModule(BundleContext context) {
		super();
		this.context = context;
	}

	@Override
	protected void configure() {
		install(osgiModule(context, eclipseRegistry()));


		bind(iterable(IAttributeImporterExtension.class)).toProvider(service(IAttributeImporterExtension.class).multiple());
		bind(iterable(IClassImporterExtension.class)).toProvider(service(IClassImporterExtension.class).multiple());
		bind(iterable(ISubmodelImporterExtension.class)).toProvider(service(ISubmodelImporterExtension.class).multiple());
		
		//registry implementation bindings; they all have extensions injected by the above bindings.
		bind(IImporterRegistry.class).to(ImporterRegistry.class);
	}
}
