/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.provider;


import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.UnitCostLine;

import com.mmxlabs.models.lng.types.TypesFactory;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.provider.MMXObjectItemProvider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.analytics.UnitCostLine} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class UnitCostLineItemProvider
	extends MMXObjectItemProvider
	implements
		IEditingDomainItemProvider,
		IStructuredItemContentProvider,
		ITreeItemContentProvider,
		IItemLabelProvider,
		IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UnitCostLineItemProvider(AdapterFactory adapterFactory) {
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

			addUnitCostPropertyDescriptor(object);
			addMmbtuDeliveredPropertyDescriptor(object);
			addFromPropertyDescriptor(object);
			addToPropertyDescriptor(object);
			addDurationPropertyDescriptor(object);
			addVolumeLoadedPropertyDescriptor(object);
			addVolumeDischargedPropertyDescriptor(object);
			addHireCostPropertyDescriptor(object);
			addFuelCostPropertyDescriptor(object);
			addCanalCostPropertyDescriptor(object);
			addPortCostPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Unit Cost feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addUnitCostPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UnitCostLine_unitCost_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UnitCostLine_unitCost_feature", "_UI_UnitCostLine_type"),
				 AnalyticsPackage.Literals.UNIT_COST_LINE__UNIT_COST,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Mmbtu Delivered feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMmbtuDeliveredPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UnitCostLine_mmbtuDelivered_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UnitCostLine_mmbtuDelivered_feature", "_UI_UnitCostLine_type"),
				 AnalyticsPackage.Literals.UNIT_COST_LINE__MMBTU_DELIVERED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the From feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addFromPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UnitCostLine_from_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UnitCostLine_from_feature", "_UI_UnitCostLine_type"),
				 AnalyticsPackage.Literals.UNIT_COST_LINE__FROM,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the To feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addToPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UnitCostLine_to_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UnitCostLine_to_feature", "_UI_UnitCostLine_type"),
				 AnalyticsPackage.Literals.UNIT_COST_LINE__TO,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Duration feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDurationPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UnitCostLine_duration_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UnitCostLine_duration_feature", "_UI_UnitCostLine_type"),
				 AnalyticsPackage.Literals.UNIT_COST_LINE__DURATION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Volume Loaded feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVolumeLoadedPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UnitCostLine_volumeLoaded_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UnitCostLine_volumeLoaded_feature", "_UI_UnitCostLine_type"),
				 AnalyticsPackage.Literals.UNIT_COST_LINE__VOLUME_LOADED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Volume Discharged feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVolumeDischargedPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UnitCostLine_volumeDischarged_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UnitCostLine_volumeDischarged_feature", "_UI_UnitCostLine_type"),
				 AnalyticsPackage.Literals.UNIT_COST_LINE__VOLUME_DISCHARGED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
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
				 getString("_UI_UnitCostLine_hireCost_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UnitCostLine_hireCost_feature", "_UI_UnitCostLine_type"),
				 AnalyticsPackage.Literals.UNIT_COST_LINE__HIRE_COST,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Fuel Cost feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addFuelCostPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UnitCostLine_fuelCost_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UnitCostLine_fuelCost_feature", "_UI_UnitCostLine_type"),
				 AnalyticsPackage.Literals.UNIT_COST_LINE__FUEL_COST,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Canal Cost feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCanalCostPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UnitCostLine_canalCost_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UnitCostLine_canalCost_feature", "_UI_UnitCostLine_type"),
				 AnalyticsPackage.Literals.UNIT_COST_LINE__CANAL_COST,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Port Cost feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPortCostPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UnitCostLine_portCost_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UnitCostLine_portCost_feature", "_UI_UnitCostLine_type"),
				 AnalyticsPackage.Literals.UNIT_COST_LINE__PORT_COST,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
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
			childrenFeatures.add(TypesPackage.Literals.EXTRA_DATA_CONTAINER__EXTRA_DATA);
			childrenFeatures.add(AnalyticsPackage.Literals.UNIT_COST_LINE__COST_COMPONENTS);
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
	 * This returns UnitCostLine.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/UnitCostLine"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		UnitCostLine unitCostLine = (UnitCostLine)object;
		return getString("_UI_UnitCostLine_type") + " " + unitCostLine.getUnitCost();
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

		switch (notification.getFeatureID(UnitCostLine.class)) {
			case AnalyticsPackage.UNIT_COST_LINE__UNIT_COST:
			case AnalyticsPackage.UNIT_COST_LINE__MMBTU_DELIVERED:
			case AnalyticsPackage.UNIT_COST_LINE__DURATION:
			case AnalyticsPackage.UNIT_COST_LINE__VOLUME_LOADED:
			case AnalyticsPackage.UNIT_COST_LINE__VOLUME_DISCHARGED:
			case AnalyticsPackage.UNIT_COST_LINE__HIRE_COST:
			case AnalyticsPackage.UNIT_COST_LINE__FUEL_COST:
			case AnalyticsPackage.UNIT_COST_LINE__CANAL_COST:
			case AnalyticsPackage.UNIT_COST_LINE__PORT_COST:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case AnalyticsPackage.UNIT_COST_LINE__EXTRA_DATA:
			case AnalyticsPackage.UNIT_COST_LINE__COST_COMPONENTS:
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
				(TypesPackage.Literals.EXTRA_DATA_CONTAINER__EXTRA_DATA,
				 TypesFactory.eINSTANCE.createExtraData()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.UNIT_COST_LINE__COST_COMPONENTS,
				 AnalyticsFactory.eINSTANCE.createCostComponent()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.UNIT_COST_LINE__COST_COMPONENTS,
				 AnalyticsFactory.eINSTANCE.createVoyage()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.UNIT_COST_LINE__COST_COMPONENTS,
				 AnalyticsFactory.eINSTANCE.createVisit()));
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return AnalyticsEditPlugin.INSTANCE;
	}

}
