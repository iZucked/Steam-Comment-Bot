/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.util.emfpath;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.provider.PropertyDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import com.google.common.base.Optional;

/**
 * The {@link EMFPathPropertyDescriptor} is an {@link IPropertyDescriptor} implementation to take an {@link EObject} and combine it with an {@link EMFPath} to a sub-feature reference. We then delegate
 * the EMF Edit {@link PropertyDescriptor} to display the property field. Note - this is read-only by default - calls to {@link #setEditable(boolean)} will attempt to enable editing via
 * {@link PropertyDescriptor#createPropertyEditor(Composite)}. The purpose of this class is to collapse the tree structure of an object hierarchy so that features of reference objects appear in the
 * same level as the main object features. Note: {@link #getId()} will return an {@link EMFPath}. This should be used by {@link IPropertySource} implementations to find the real data to display.
 * 
 */
public class EMFPathPropertyDescriptor implements IPropertyDescriptor {

	private final PropertyDescriptor propertyDescriptor;

	private final EMFPath path;

	/**
	 * The {@link IPropertyDescriptor#getCategory()} category definition override. If {@link Optional#isPresent()} returns false then {@link #getCategory()} will delegate to the contained
	 * propertyDescriptor. Otherwise the value of the {@link Optional} will be returned (it may of course still be null).
	 */
	private Optional<String> category = Optional.absent();
	private Optional<String> displayName = Optional.absent();
	private final Optional<Object> id = Optional.absent();

	/**
	 * Editing disabled by default.
	 */
	private boolean editable = false;

	public static EMFPathPropertyDescriptor create(final EObject object, final AdapterFactory adapterFactory, final EStructuralFeature feature, final List<ETypedElement> featurePath) {
		final EMFPath path = new EMFPath(true, featurePath);
		return create(object, adapterFactory, feature, path);
	}

	public static EMFPathPropertyDescriptor create(final EObject object, final AdapterFactory adapterFactory, final EStructuralFeature feature, final ETypedElement... featurePath) {
		final EMFPath path = new EMFPath(true, featurePath);
		return create(object, adapterFactory, feature, path);
	}

	/**
	 * Handy method to create an {@link EMFPathPropertyDescriptor} or return null if it fails to find the target at the end of the EMFPath. If a feature in the chain is null, then we cannot query for
	 * the EMF edit property descriptor.
	 * 
	 * @param object
	 * @param adapterFactory
	 * @param feature
	 * @param path
	 * @return
	 */
	public static EMFPathPropertyDescriptor create(final EObject object, final AdapterFactory adapterFactory, final EStructuralFeature feature, final EMFPath path) {
		final Object object2 = path.get(object);

		// Combine the existing path and the extra feature into a new path object
		final List<ETypedElement> combinedPath = new ArrayList<>();
		for (final ETypedElement o : path.path) {
			combinedPath.add(o);
		}
		combinedPath.add(feature);
		final EMFPath path2 = new EMFPath(true, combinedPath);

		if (object2 != null) {
			final IItemPropertySource ps = (IItemPropertySource) adapterFactory.adapt(object2, IItemPropertySource.class);
			if (ps != null) {
				final IItemPropertyDescriptor pd = ps.getPropertyDescriptor(object2, feature);
				if (pd != null) {
					return new EMFPathPropertyDescriptor(new PropertyDescriptor(object2, pd), path2);
				}
			}
		}
		return null;
	}

	public EMFPathPropertyDescriptor(final PropertyDescriptor propertyDescriptor, final EMFPath path) {
		this.path = path;
		this.propertyDescriptor = propertyDescriptor;
		if (propertyDescriptor == null) {
			throw new NullPointerException("property desriptor is null");
		}
	}

	@Override
	public CellEditor createPropertyEditor(final Composite composite) {
		if (editable) {
			return propertyDescriptor.createPropertyEditor(composite);
		} else {
			return null;
		}
	}

	@Override
	public String getCategory() {
		if (category.isPresent()) {
			return category.get();
		}
		return propertyDescriptor.getCategory();
	}

	public void setCategory(final String category) {
		this.category = Optional.fromNullable(category);
	}

	@Override
	public String getDescription() {
		return propertyDescriptor.getDescription();
	}

	@Override
	public String getDisplayName() {
		if (displayName.isPresent()) {
			return displayName.get();
		}
		return propertyDescriptor.getDisplayName();
	}

	@Override
	public String[] getFilterFlags() {
		return propertyDescriptor.getFilterFlags();
	}

	@Override
	public Object getHelpContextIds() {
		return propertyDescriptor.getHelpContextIds();
	}

	@Override
	public Object getId() {

		if (id.isPresent()) {
			return id.get();
		}

		// It is intended that the containing property source will check this instance
		return path;// propertyDescriptor.getId();
	}

	@Override
	public ILabelProvider getLabelProvider() {
		return propertyDescriptor.getLabelProvider();
	}

	@Override
	public boolean isCompatibleWith(final IPropertyDescriptor anotherProperty) {
		return propertyDescriptor.isCompatibleWith(anotherProperty);
	}

	/**
	 * Returns the editable state of this {@link PropertyDescriptor}. When true {@link #createPropertyEditor(Composite)} will delegate to {@link PropertyDescriptor#createPropertyEditor(Composite)}.
	 * When false {@link #createPropertyEditor(Composite)} method will return null.
	 * 
	 * @return
	 */
	public boolean isEditable() {
		return editable;
	}

	/**
	 * Change the editable state of this {@link IPropertyDescriptor}
	 * 
	 * @param editable
	 */
	public void setEditable(final boolean editable) {
		this.editable = editable;
	}

	public void setDisplayName(final String displayName) {
		this.displayName = Optional.fromNullable(displayName);
	}
}
