/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.extensions;

import static org.ops4j.peaberry.Peaberry.osgiModule;
import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.eclipse.EclipseRegistry.eclipseRegistry;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;
import com.mmxlabs.models.ui.registries.IDisplayCompositeFactoryRegistry;
import com.mmxlabs.models.ui.registries.IEditorFactoryRegistry;
import com.mmxlabs.models.ui.registries.IJointModelEditorContributionRegistry;
import com.mmxlabs.models.ui.registries.IModelFactoryRegistry;
import com.mmxlabs.models.ui.registries.IReferenceValueProviderFactoryRegistry;
import com.mmxlabs.models.ui.registries.impl.ComponentHelperRegistry;
import com.mmxlabs.models.ui.registries.impl.DisplayCompositeFactoryRegistry;
import com.mmxlabs.models.ui.registries.impl.EditorFactoryRegistry;
import com.mmxlabs.models.ui.registries.impl.JointModelEditorContributionRegistry;
import com.mmxlabs.models.ui.registries.impl.ModelFactoryRegistry;
import com.mmxlabs.models.ui.registries.impl.ReferenceValueProviderFactoryRegistry;

/**
 * A guice module which wires up the various extension point registries to
 * the actual extension points, and their interfaces to their implementations.
 * 
 * @author hinton
 *
 */
public class ExtensionConfigurationModule extends AbstractModule {
	private final Activator activator;
	
	public ExtensionConfigurationModule(Activator activator) {
		super();
		this.activator = activator;
	}

	@Override
	protected void configure() {
		install(osgiModule(activator.getBundle()
				.getBundleContext(), eclipseRegistry()));

		// extension point com.mmxlabs.models.ui.displaycompositefactories
		bind(iterable(IDisplayCompositeFactoryExtension.class)).toProvider(service(IDisplayCompositeFactoryExtension.class).multiple());
		// extension point com.mmxlabs.models.ui.componenthelpers
		bind(iterable(IComponentHelperExtension.class)).toProvider(service(IComponentHelperExtension.class).multiple());
		// extension point com.mmxlabs.models.ui.editorfactories
		bind(iterable(IInlineEditorFactoryExtension.class)).toProvider(service(IInlineEditorFactoryExtension.class).multiple());
		// extension point com.mmxlabs.models.ui.valueproviders
		bind(iterable(IReferenceValueProviderExtension.class)).toProvider(service(IReferenceValueProviderExtension.class).multiple());
		// extension point com.mmxlabs.models.ui.jointmodeleditor.contributions
		bind(iterable(IJointModelEditorExtension.class)).toProvider(service(IJointModelEditorExtension.class).multiple());
		// extension point com.mmxlabs.models.ui.modelfactories
		bind(iterable(IModelFactoryExtension.class)).toProvider(service(IModelFactoryExtension.class).multiple());
		
		//registry implementation bindings; they all have extensions injected by the above bindings.
		bind(IDisplayCompositeFactoryRegistry.class).to(DisplayCompositeFactoryRegistry.class).in(Singleton.class);
		bind(IComponentHelperRegistry.class).to(ComponentHelperRegistry.class).in(Singleton.class);
		bind(IEditorFactoryRegistry.class).to(EditorFactoryRegistry.class).in(Singleton.class);
		bind(IReferenceValueProviderFactoryRegistry.class).to(ReferenceValueProviderFactoryRegistry.class).in(Singleton.class);
		bind(IJointModelEditorContributionRegistry.class).to(JointModelEditorContributionRegistry.class).in(Singleton.class);
		bind(IModelFactoryRegistry.class).to(ModelFactoryRegistry.class).in(Singleton.class);
	}
}
