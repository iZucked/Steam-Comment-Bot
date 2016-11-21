/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.mmxcore.provider.UUIDObjectItemProvider;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.port.PortModel} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class PortModelItemProvider
	extends UUIDObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PortModelItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

		}
		return itemPropertyDescriptors;
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(PortPackage.Literals.PORT_MODEL__PORTS);
			childrenFeatures.add(PortPackage.Literals.PORT_MODEL__PORT_GROUPS);
			childrenFeatures.add(PortPackage.Literals.PORT_MODEL__ROUTES);
			childrenFeatures.add(PortPackage.Literals.PORT_MODEL__SPECIAL_PORT_GROUPS);
			childrenFeatures.add(PortPackage.Literals.PORT_MODEL__PORT_COUNTRY_GROUPS);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns PortModel.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/PortModel"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((PortModel)object).getUuid();
		return label == null || label.length() == 0 ?
			getString("_UI_PortModel_type") :
			getString("_UI_PortModel_type") + " " + label;
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(PortModel.class)) {
			case PortPackage.PORT_MODEL__PORTS:
			case PortPackage.PORT_MODEL__PORT_GROUPS:
			case PortPackage.PORT_MODEL__ROUTES:
			case PortPackage.PORT_MODEL__SPECIAL_PORT_GROUPS:
			case PortPackage.PORT_MODEL__PORT_COUNTRY_GROUPS:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(PortPackage.Literals.PORT_MODEL__PORTS,
				 PortFactory.eINSTANCE.createPort()));

		newChildDescriptors.add
			(createChildParameter
				(PortPackage.Literals.PORT_MODEL__PORT_GROUPS,
				 PortFactory.eINSTANCE.createPortGroup()));

		newChildDescriptors.add
			(createChildParameter
				(PortPackage.Literals.PORT_MODEL__ROUTES,
				 PortFactory.eINSTANCE.createRoute()));

		newChildDescriptors.add
			(createChildParameter
				(PortPackage.Literals.PORT_MODEL__SPECIAL_PORT_GROUPS,
				 PortFactory.eINSTANCE.createCapabilityGroup()));

		newChildDescriptors.add
			(createChildParameter
				(PortPackage.Literals.PORT_MODEL__PORT_COUNTRY_GROUPS,
				 PortFactory.eINSTANCE.createPortCountryGroup()));
	}

}
