package com.mmxlabs.models.util.emfpath;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.provider.PropertyDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * The {@link EMFPathPropertyDescriptor} is an {@link IPropertyDescriptor} implementation to take an {@link EObject} and combine it with an {@link EMFPath} to a sub-feature reference. We then delegate
 * the EMF Edit {@link PropertyDescriptor} to display the property field. Note - this is read-only although in future perhaps this should come from the gen-model. The purpose of this class is to
 * collapse the tree structure of an object hierarchy so that features of reference objects appear in the same level as the main object features. Note: {@link #getId()} will return an {@link EMFPath}.
 * This should be used by {@link IPropertySource} implementations to find the real data to display.
 * 
 * @since 3.0
 */
public class EMFPathPropertyDescriptor implements IPropertyDescriptor {

	private final PropertyDescriptor propertyDescriptor;

	private final EMFPath path;

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
		if (object2 != null) {
			final IItemPropertySource ps = (IItemPropertySource) adapterFactory.adapt(object2, IItemPropertySource.class);
			if (ps != null) {
				return new EMFPathPropertyDescriptor(new PropertyDescriptor(object, ps.getPropertyDescriptor(object2, feature)), path);
			}
		}
		return null;
	}

	public EMFPathPropertyDescriptor(final PropertyDescriptor propertyDescriptor, final EMFPath path) {
		this.path = path;
		this.propertyDescriptor = propertyDescriptor;
	}

	@Override
	public CellEditor createPropertyEditor(final Composite composite) {
		// Disable Editing. Perhaps
		return null;// propertyDescriptor.createPropertyEditor(composite);
	}

	@Override
	public String getCategory() {
		return propertyDescriptor.getCategory();
	}

	@Override
	public String getDescription() {
		return propertyDescriptor.getDescription();
	}

	@Override
	public String getDisplayName() {
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

}
