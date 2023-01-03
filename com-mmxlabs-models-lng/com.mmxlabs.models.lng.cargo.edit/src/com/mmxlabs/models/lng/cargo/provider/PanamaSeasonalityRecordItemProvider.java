/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.provider;


import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord;

import java.time.LocalDate;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IChildCreationExtender;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class PanamaSeasonalityRecordItemProvider 
	extends ItemProviderAdapter
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
	public PanamaSeasonalityRecordItemProvider(AdapterFactory adapterFactory) {
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

			addVesselGroupCanalParameterPropertyDescriptor(object);
			addStartDayPropertyDescriptor(object);
			addStartMonthPropertyDescriptor(object);
			addStartYearPropertyDescriptor(object);
			addNorthboundWaitingDaysPropertyDescriptor(object);
			addSouthboundWaitingDaysPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Vessel Group Canal Parameter feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVesselGroupCanalParameterPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_PanamaSeasonalityRecord_vesselGroupCanalParameter_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_PanamaSeasonalityRecord_vesselGroupCanalParameter_feature", "_UI_PanamaSeasonalityRecord_type"),
				 CargoPackage.Literals.PANAMA_SEASONALITY_RECORD__VESSEL_GROUP_CANAL_PARAMETER,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Start Day feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addStartDayPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_PanamaSeasonalityRecord_startDay_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_PanamaSeasonalityRecord_startDay_feature", "_UI_PanamaSeasonalityRecord_type"),
				 CargoPackage.Literals.PANAMA_SEASONALITY_RECORD__START_DAY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Start Month feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addStartMonthPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_PanamaSeasonalityRecord_startMonth_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_PanamaSeasonalityRecord_startMonth_feature", "_UI_PanamaSeasonalityRecord_type"),
				 CargoPackage.Literals.PANAMA_SEASONALITY_RECORD__START_MONTH,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Start Year feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addStartYearPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_PanamaSeasonalityRecord_startYear_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_PanamaSeasonalityRecord_startYear_feature", "_UI_PanamaSeasonalityRecord_type"),
				 CargoPackage.Literals.PANAMA_SEASONALITY_RECORD__START_YEAR,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Northbound Waiting Days feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNorthboundWaitingDaysPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_PanamaSeasonalityRecord_northboundWaitingDays_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_PanamaSeasonalityRecord_northboundWaitingDays_feature", "_UI_PanamaSeasonalityRecord_type"),
				 CargoPackage.Literals.PANAMA_SEASONALITY_RECORD__NORTHBOUND_WAITING_DAYS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Southbound Waiting Days feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSouthboundWaitingDaysPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_PanamaSeasonalityRecord_southboundWaitingDays_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_PanamaSeasonalityRecord_southboundWaitingDays_feature", "_UI_PanamaSeasonalityRecord_type"),
				 CargoPackage.Literals.PANAMA_SEASONALITY_RECORD__SOUTHBOUND_WAITING_DAYS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns PanamaSeasonalityRecord.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/PanamaSeasonalityRecord"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		PanamaSeasonalityRecord panamaSeasonalityRecord = (PanamaSeasonalityRecord)object;
		return getString("_UI_PanamaSeasonalityRecord_type") + " " + panamaSeasonalityRecord.getStartDay();
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

		switch (notification.getFeatureID(PanamaSeasonalityRecord.class)) {
			case CargoPackage.PANAMA_SEASONALITY_RECORD__START_DAY:
			case CargoPackage.PANAMA_SEASONALITY_RECORD__START_MONTH:
			case CargoPackage.PANAMA_SEASONALITY_RECORD__START_YEAR:
			case CargoPackage.PANAMA_SEASONALITY_RECORD__NORTHBOUND_WAITING_DAYS:
			case CargoPackage.PANAMA_SEASONALITY_RECORD__SOUTHBOUND_WAITING_DAYS:
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

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return ((IChildCreationExtender)adapterFactory).getResourceLocator();
	}

}
