/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.properties.factory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.eclipse.EclipseRegistry;
import org.ops4j.peaberry.util.TypeLiterals;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.models.ui.properties.extensions.DetailPropertyFactoryExtensionPoint;

/**
 * The {@link DetailPropertyFactoryRegistry} can be used to group {@link DetailPropertyFactoryExtensionPoint}s by category and {@link EClass}. This needs to be combined with a Guice {@link Module}
 * with eclipse registry support;
 * 
 * *
 * 
 * <pre>
 * 
 * &#064;Override
 * protected void configure() {
 * 	install(Peaberry.osgiModule(bundleContext, EclipseRegistry.eclipseRegistry()));
 * 	bind(TypeLiterals.iterable(DetailPropertyFactoryExtensionPoint.class)).toProvider(Peaberry.service(DetailPropertyFactoryExtensionPoint.class).multiple());
 * 	bind(DetailPropertyFactoryRegistry.class);
 * }
 * </pre>
 * 
 * @author Simon Goodall
 * 
 */
public class DetailPropertyFactoryRegistry {

	// Category -> EClass -> Ext point
	private final Map<String, Map<String, DetailPropertyFactoryExtensionPoint>> factories = new HashMap<>();

	/**
	 * This method is called by the Guice/Peaberry frameworks to inject the known DetailPropertyFactory extensions into this instance.
	 * 
	 * @param factoryExtPoints
	 */
	@Inject
	public void init(final Iterable<DetailPropertyFactoryExtensionPoint> factoryExtPoints) {

		for (final DetailPropertyFactoryExtensionPoint ext : factoryExtPoints) {
			final String eClass = ext.getEClass();
			final String category = ext.getCategory();

			final Map<String, DetailPropertyFactoryExtensionPoint> map;
			if (factories.containsKey(category)) {
				map = factories.get(category);
			} else {
				map = new HashMap<>();
				factories.put(category, map);
			}
			map.put(eClass, ext);
		}
	}

	@Nullable
	public IDetailPropertyFactory getFactory(@NonNull final String category, @NonNull final EClass eClass) {
		final Map<String, DetailPropertyFactoryExtensionPoint> map;
		if (factories.containsKey(category)) {
			map = factories.get(category);
		} else {
			return null;
		}

		final Set<EClass> searchClasses = new HashSet<EClass>();
		searchClasses.add(eClass);
		while (!searchClasses.isEmpty()) {
			final EClass cls = searchClasses.iterator().next();
			searchClasses.remove(cls);
			final String instanceClassName = cls.getInstanceClassName();
			if (map.containsKey(instanceClassName)) {
				final DetailPropertyFactoryExtensionPoint ext = map.get(instanceClassName);
				if (ext != null) {
					return ext.createDetailPropertyFactory();
				}
			} else {
				searchClasses.addAll(cls.getEAllSuperTypes());
			}
		}

		return null;
	}

	/**
	 * Create a {@link DetailPropertyFactoryRegistry} and initialise it with known extension points.
	 * 
	 * @return
	 */
	@SuppressWarnings("null")
	@NonNull
	public static DetailPropertyFactoryRegistry createRegistry() {
		final BundleContext bc = FrameworkUtil.getBundle(DetailPropertyFactoryRegistry.class).getBundleContext();
		final Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				install(Peaberry.osgiModule(bc, EclipseRegistry.eclipseRegistry()));
				bind(TypeLiterals.iterable(DetailPropertyFactoryExtensionPoint.class)).toProvider(Peaberry.service(DetailPropertyFactoryExtensionPoint.class).multiple());
				bind(DetailPropertyFactoryRegistry.class);
			}
		});
		return injector.getInstance(DetailPropertyFactoryRegistry.class);
	}
}
