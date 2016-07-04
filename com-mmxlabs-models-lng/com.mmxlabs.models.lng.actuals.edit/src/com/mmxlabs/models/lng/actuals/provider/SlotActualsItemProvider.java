/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.actuals.provider;


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
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.lng.actuals.SlotActuals;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.actuals.SlotActuals} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SlotActualsItemProvider
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
	public SlotActualsItemProvider(AdapterFactory adapterFactory) {
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

			addSlotPropertyDescriptor(object);
			addCounterpartyPropertyDescriptor(object);
			addOperationsStartPropertyDescriptor(object);
			addOperationsEndPropertyDescriptor(object);
			addTitleTransferPointPropertyDescriptor(object);
			addVolumeInM3PropertyDescriptor(object);
			addVolumeInMMBtuPropertyDescriptor(object);
			addPriceDOLPropertyDescriptor(object);
			addPenaltyPropertyDescriptor(object);
			addNotesPropertyDescriptor(object);
			addCVPropertyDescriptor(object);
			addBaseFuelConsumptionPropertyDescriptor(object);
			addPortBaseFuelConsumptionPropertyDescriptor(object);
			addRoutePropertyDescriptor(object);
			addDistancePropertyDescriptor(object);
			addRouteCostsPropertyDescriptor(object);
			addCrewBonusPropertyDescriptor(object);
			addPortChargesPropertyDescriptor(object);
			addCapacityChargesPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the CV feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCVPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotActuals_CV_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotActuals_CV_feature", "_UI_SlotActuals_type"),
				 ActualsPackage.Literals.SLOT_ACTUALS__CV,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Port Charges feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPortChargesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotActuals_portCharges_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotActuals_portCharges_feature", "_UI_SlotActuals_type"),
				 ActualsPackage.Literals.SLOT_ACTUALS__PORT_CHARGES,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Capacity Charges feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCapacityChargesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotActuals_capacityCharges_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotActuals_capacityCharges_feature", "_UI_SlotActuals_type"),
				 ActualsPackage.Literals.SLOT_ACTUALS__CAPACITY_CHARGES,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Base Fuel Consumption feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBaseFuelConsumptionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotActuals_baseFuelConsumption_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotActuals_baseFuelConsumption_feature", "_UI_SlotActuals_type"),
				 ActualsPackage.Literals.SLOT_ACTUALS__BASE_FUEL_CONSUMPTION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Port Base Fuel Consumption feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPortBaseFuelConsumptionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotActuals_portBaseFuelConsumption_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotActuals_portBaseFuelConsumption_feature", "_UI_SlotActuals_type"),
				 ActualsPackage.Literals.SLOT_ACTUALS__PORT_BASE_FUEL_CONSUMPTION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Route feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRoutePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotActuals_route_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotActuals_route_feature", "_UI_SlotActuals_type"),
				 ActualsPackage.Literals.SLOT_ACTUALS__ROUTE,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Distance feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDistancePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotActuals_distance_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotActuals_distance_feature", "_UI_SlotActuals_type"),
				 ActualsPackage.Literals.SLOT_ACTUALS__DISTANCE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Route Costs feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRouteCostsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotActuals_routeCosts_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotActuals_routeCosts_feature", "_UI_SlotActuals_type"),
				 ActualsPackage.Literals.SLOT_ACTUALS__ROUTE_COSTS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Crew Bonus feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCrewBonusPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotActuals_crewBonus_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotActuals_crewBonus_feature", "_UI_SlotActuals_type"),
				 ActualsPackage.Literals.SLOT_ACTUALS__CREW_BONUS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Slot feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSlotPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotActuals_slot_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotActuals_slot_feature", "_UI_SlotActuals_type"),
				 ActualsPackage.Literals.SLOT_ACTUALS__SLOT,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Counterparty feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCounterpartyPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotActuals_counterparty_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotActuals_counterparty_feature", "_UI_SlotActuals_type"),
				 ActualsPackage.Literals.SLOT_ACTUALS__COUNTERPARTY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Operations Start feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addOperationsStartPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotActuals_operationsStart_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotActuals_operationsStart_feature", "_UI_SlotActuals_type"),
				 ActualsPackage.Literals.SLOT_ACTUALS__OPERATIONS_START,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Operations End feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addOperationsEndPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotActuals_operationsEnd_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotActuals_operationsEnd_feature", "_UI_SlotActuals_type"),
				 ActualsPackage.Literals.SLOT_ACTUALS__OPERATIONS_END,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Title Transfer Point feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTitleTransferPointPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotActuals_titleTransferPoint_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotActuals_titleTransferPoint_feature", "_UI_SlotActuals_type"),
				 ActualsPackage.Literals.SLOT_ACTUALS__TITLE_TRANSFER_POINT,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Volume In M3 feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVolumeInM3PropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotActuals_volumeInM3_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotActuals_volumeInM3_feature", "_UI_SlotActuals_type"),
				 ActualsPackage.Literals.SLOT_ACTUALS__VOLUME_IN_M3,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Volume In MM Btu feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVolumeInMMBtuPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotActuals_volumeInMMBtu_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotActuals_volumeInMMBtu_feature", "_UI_SlotActuals_type"),
				 ActualsPackage.Literals.SLOT_ACTUALS__VOLUME_IN_MM_BTU,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Price DOL feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPriceDOLPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotActuals_priceDOL_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotActuals_priceDOL_feature", "_UI_SlotActuals_type"),
				 ActualsPackage.Literals.SLOT_ACTUALS__PRICE_DOL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Penalty feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPenaltyPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotActuals_penalty_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotActuals_penalty_feature", "_UI_SlotActuals_type"),
				 ActualsPackage.Literals.SLOT_ACTUALS__PENALTY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Notes feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNotesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SlotActuals_notes_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SlotActuals_notes_feature", "_UI_SlotActuals_type"),
				 ActualsPackage.Literals.SLOT_ACTUALS__NOTES,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
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
			childrenFeatures.add(ActualsPackage.Literals.SLOT_ACTUALS__SLOT);
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
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((SlotActuals)object).getCounterparty();
		return label == null || label.length() == 0 ?
			getString("_UI_SlotActuals_type") :
			getString("_UI_SlotActuals_type") + " " + label;
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

		switch (notification.getFeatureID(SlotActuals.class)) {
			case ActualsPackage.SLOT_ACTUALS__COUNTERPARTY:
			case ActualsPackage.SLOT_ACTUALS__OPERATIONS_START:
			case ActualsPackage.SLOT_ACTUALS__OPERATIONS_END:
			case ActualsPackage.SLOT_ACTUALS__VOLUME_IN_M3:
			case ActualsPackage.SLOT_ACTUALS__VOLUME_IN_MM_BTU:
			case ActualsPackage.SLOT_ACTUALS__PRICE_DOL:
			case ActualsPackage.SLOT_ACTUALS__PENALTY:
			case ActualsPackage.SLOT_ACTUALS__NOTES:
			case ActualsPackage.SLOT_ACTUALS__CV:
			case ActualsPackage.SLOT_ACTUALS__BASE_FUEL_CONSUMPTION:
			case ActualsPackage.SLOT_ACTUALS__PORT_BASE_FUEL_CONSUMPTION:
			case ActualsPackage.SLOT_ACTUALS__DISTANCE:
			case ActualsPackage.SLOT_ACTUALS__ROUTE_COSTS:
			case ActualsPackage.SLOT_ACTUALS__CREW_BONUS:
			case ActualsPackage.SLOT_ACTUALS__PORT_CHARGES:
			case ActualsPackage.SLOT_ACTUALS__CAPACITY_CHARGES:
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
		return ActualsEditPlugin.INSTANCE;
	}

}
