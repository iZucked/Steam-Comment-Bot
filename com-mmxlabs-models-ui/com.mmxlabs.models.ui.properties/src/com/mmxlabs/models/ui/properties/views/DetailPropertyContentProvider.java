/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.properties.views;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.Viewer;
import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.eclipse.EclipseRegistry;
import org.ops4j.peaberry.util.TypeLiterals;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.models.ui.properties.DetailProperty;
import com.mmxlabs.models.ui.properties.extensions.DetailPropertyFactoryExtensionPoint;
import com.mmxlabs.models.ui.properties.factory.DetailPropertyFactoryRegistry;
import com.mmxlabs.models.ui.properties.factory.IDetailPropertyFactory;

public class DetailPropertyContentProvider extends AdapterFactoryContentProvider {

	private final String category;

	/**
	 * Maps a {@link DetailProperty} to the originating element. Used {@link #getParent(Object)}
	 */
	private final Map<DetailProperty, Object> propertyParentMap = new HashMap<>();

	private final DetailPropertyFactoryRegistry registry;

	public DetailPropertyContentProvider(@NonNull final AdapterFactory adapterFactory, @NonNull final String category, @NonNull final DetailPropertyFactoryRegistry registry) {
		super(adapterFactory);
		this.category = category;
		this.registry = createRegistry();
	}

	public DetailPropertyContentProvider(@NonNull final AdapterFactory adapterFactory, @NonNull final String category) {
		super(adapterFactory);
		this.category = category;
		this.registry = createRegistry();
	}

	@Override
	public void dispose() {
		propertyParentMap.clear();
		super.dispose();
	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
		propertyParentMap.clear();
		super.inputChanged(viewer, oldInput, newInput);
	}

	@Override
	public Object[] getElements(final Object inputElement) {

		if (inputElement instanceof Collection<?>) {
			final Collection<?> col = (Collection<?>) inputElement;
			final List<Object> newElements = new LinkedList<>();
			for (final Object o : col) {
				final DetailProperty property = getDetailProperty(o);
				if (property != null) {
					newElements.add(property);
				}
			}
			return newElements.toArray();
		} else if (inputElement instanceof Object[]) {
			final List<Object> newElements = new LinkedList<>();
			for (final Object o : (Object[]) inputElement) {
				final DetailProperty property = getDetailProperty(o);
				if (property != null) {
					newElements.add(property);
				}
			}

			return newElements.toArray();
		} else if (!(inputElement instanceof DetailProperty)) {
			final DetailProperty property = getDetailProperty(inputElement);
			if (property != null) {
				return new Object[] { property };
			}
		}

		return super.getElements(inputElement);
	}

	private DetailProperty getDetailProperty(final Object o) {
		final DetailProperty property;
		if (!(o instanceof DetailProperty)) {
			final IDetailPropertyFactory factory = getDetailPropertyFactory(o);
			if (factory != null) {
				property = factory.createProperties((EObject) o);
				propertyParentMap.put(property, o);
			} else {
				property = null;
			}
		} else {
			property = (DetailProperty) o;
		}
		return property;
	}

	@Override
	public Object[] getChildren(final Object parentElement) {

		if (!(parentElement instanceof DetailProperty)) {
			final DetailProperty property = getDetailProperty(parentElement);
			if (property != null) {
				return new Object[] { property };
			}
		}
		return super.getChildren(parentElement);
	}

	@Override
	public Object getParent(final Object element) {

		if (propertyParentMap.containsKey(element)) {
			return propertyParentMap.get(element);
		} else {
			return super.getParent(element);
		}
	}

	@Override
	public boolean hasChildren(final Object element) {
		final Object[] arr = getChildren(element);
		return arr != null && arr.length > 0;
	}

	private @Nullable
	IDetailPropertyFactory getDetailPropertyFactory(@Nullable final Object object) {
		if (object instanceof EObject) {
			final EObject eObject = (EObject) object;
			return registry.getFactory(category, eObject.eClass());
		}
		return null;
	}

	/**
	 * Create a {@link DetailPropertyFactoryRegistry} and initialise it with known extension points.
	 * 
	 * @return
	 */
	private DetailPropertyFactoryRegistry createRegistry() {
		final BundleContext bc = FrameworkUtil.getBundle(getClass()).getBundleContext();
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
