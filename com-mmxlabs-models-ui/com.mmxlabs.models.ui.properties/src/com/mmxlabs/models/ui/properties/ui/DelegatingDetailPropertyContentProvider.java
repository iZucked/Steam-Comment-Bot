package com.mmxlabs.models.ui.properties.ui;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ITreeContentProvider;
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

public class DelegatingDetailPropertyContentProvider implements ITreeContentProvider {

	private final ITreeContentProvider delegate;

	private final String category;

	/**
	 * Maps a {@link DetailProperty} to the originating element. Used {@link #getParent(Object)}
	 */
	private final Map<DetailProperty, Object> propertyParentMap = new HashMap<>();

	private final DetailPropertyFactoryRegistry registry;

	public DelegatingDetailPropertyContentProvider(@NonNull final ITreeContentProvider delegate, @NonNull final String category, @NonNull final DetailPropertyFactoryRegistry registry) {
		this.delegate = delegate;
		this.category = category;
		this.registry = registry;
	}

	public DelegatingDetailPropertyContentProvider(@NonNull final ITreeContentProvider delegate, @NonNull final String category) {
		this.delegate = delegate;
		this.category = category;
		this.registry = DetailPropertyFactoryRegistry.createRegistry();
	}

	@Override
	public void dispose() {
		propertyParentMap.clear();
		delegate.dispose();
	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
		propertyParentMap.clear();
		delegate.inputChanged(viewer, oldInput, newInput);
	}

	@Override
	public Object[] getElements(final Object inputElement) {

		if (inputElement instanceof DetailProperty) {
			final DetailProperty detailProperty = (DetailProperty) inputElement;
			return detailProperty.getChildren().toArray();
		}

		final Object[] children = delegate.getElements(inputElement);
		final IDetailPropertyFactory factory = getDetailPropertyFactory(inputElement);
		if (factory != null) {
			final DetailProperty property = factory.createProperties((EObject) inputElement);
			if (property != null) {
				propertyParentMap.put(property, inputElement);
				return merge(children, Collections.singleton(property));
			}
		}

		return children;
	}

	@Override
	public Object[] getChildren(final Object parentElement) {

		if (parentElement instanceof DetailProperty) {
			final DetailProperty detailProperty = (DetailProperty) parentElement;
			return detailProperty.getChildren().toArray();
		}

		final Object[] children = delegate.getChildren(parentElement);
		final IDetailPropertyFactory factory = getDetailPropertyFactory(parentElement);
		if (factory != null) {
			final DetailProperty property = factory.createProperties((EObject) parentElement);
			if (property != null) {
				propertyParentMap.put(property, parentElement);
				return merge(children, Collections.singleton(property));
			}
		}

		return children;
	}

	@Override
	public Object getParent(final Object element) {

		if (propertyParentMap.containsKey(element)) {
			return propertyParentMap.get(element);
		} else if (element instanceof DetailProperty) {
			final DetailProperty detailProperty = (DetailProperty) element;
			return detailProperty.getParent();
		} else {
			return delegate.getParent(element);
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

	private Object[] merge(@NonNull final Object[] original, @NonNull final Collection<?> col) {
		final Object[] a = Arrays.copyOf(original, original.length + col.size());

		int idx = original.length;
		for (final Object o : col) {
			a[idx++] = o;
		}

		return a;

	}
}
