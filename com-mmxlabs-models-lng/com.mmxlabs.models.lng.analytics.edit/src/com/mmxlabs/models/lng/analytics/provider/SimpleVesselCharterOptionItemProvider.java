/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.provider;


import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption;

import com.mmxlabs.models.mmxcore.provider.UUIDObjectItemProvider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SimpleVesselCharterOptionItemProvider extends UUIDObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SimpleVesselCharterOptionItemProvider(AdapterFactory adapterFactory) {
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

			addVesselPropertyDescriptor(object);
			addHireCostPropertyDescriptor(object);
			addEntityPropertyDescriptor(object);
			addUseSafetyHeelPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Vessel feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVesselPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SimpleVesselCharterOption_vessel_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SimpleVesselCharterOption_vessel_feature", "_UI_SimpleVesselCharterOption_type"),
				 AnalyticsPackage.Literals.SIMPLE_VESSEL_CHARTER_OPTION__VESSEL,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Hire Cost feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addHireCostPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SimpleVesselCharterOption_hireCost_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SimpleVesselCharterOption_hireCost_feature", "_UI_SimpleVesselCharterOption_type"),
				 AnalyticsPackage.Literals.SIMPLE_VESSEL_CHARTER_OPTION__HIRE_COST,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Entity feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addEntityPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SimpleVesselCharterOption_entity_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SimpleVesselCharterOption_entity_feature", "_UI_SimpleVesselCharterOption_type"),
				 AnalyticsPackage.Literals.SIMPLE_VESSEL_CHARTER_OPTION__ENTITY,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Use Safety Heel feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addUseSafetyHeelPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SimpleVesselCharterOption_useSafetyHeel_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SimpleVesselCharterOption_useSafetyHeel_feature", "_UI_SimpleVesselCharterOption_type"),
				 AnalyticsPackage.Literals.SIMPLE_VESSEL_CHARTER_OPTION__USE_SAFETY_HEEL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns SimpleVesselCharterOption.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/SimpleVesselCharterOption"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((SimpleVesselCharterOption)object).getUuid();
		return label == null || label.length() == 0 ?
			getString("_UI_SimpleVesselCharterOption_type") :
			getString("_UI_SimpleVesselCharterOption_type") + " " + label;
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

		switch (notification.getFeatureID(SimpleVesselCharterOption.class)) {
			case AnalyticsPackage.SIMPLE_VESSEL_CHARTER_OPTION__HIRE_COST:
			case AnalyticsPackage.SIMPLE_VESSEL_CHARTER_OPTION__USE_SAFETY_HEEL:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
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
	}

}
