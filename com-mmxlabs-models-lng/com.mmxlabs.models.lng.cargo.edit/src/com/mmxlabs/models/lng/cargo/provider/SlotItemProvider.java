/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.provider.UUIDObjectItemProvider;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.cargo.Slot} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SlotItemProvider
	extends UUIDObjectItemProvider
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
	public SlotItemProvider(AdapterFactory adapterFactory) {
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

			addNamePropertyDescriptor(object);
			addAssignmentPropertyDescriptor(object);
			addSpotIndexPropertyDescriptor(object);
			addSequenceHintPropertyDescriptor(object);
			addLockedPropertyDescriptor(object);
			addContractPropertyDescriptor(object);
			addPortPropertyDescriptor(object);
			addWindowStartPropertyDescriptor(object);
			addWindowStartTimePropertyDescriptor(object);
			addWindowSizePropertyDescriptor(object);
			addDurationPropertyDescriptor(object);
			addMinQuantityPropertyDescriptor(object);
			addMaxQuantityPropertyDescriptor(object);
			addOptionalPropertyDescriptor(object);
			addPriceExpressionPropertyDescriptor(object);
			addCargoPropertyDescriptor(object);
			addPricingDatePropertyDescriptor(object);
			addNotesPropertyDescriptor(object);
			addShippingDaysRestrictionPropertyDescriptor(object);
			addEntityPropertyDescriptor(object);
			addRestrictedContractsPropertyDescriptor(object);
			addRestrictedPortsPropertyDescriptor(object);
			addRestrictedListsArePermissivePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Name feature.
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_NamedObject_name_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_NamedObject_name_feature", "_UI_NamedObject_type"),
				 MMXCorePackage.Literals.NAMED_OBJECT__NAME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Assignment feature.
	 * <!-- begin-user-doc -->
	 * @since 8.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addAssignmentPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_AssignableElement_assignment_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_AssignableElement_assignment_feature", "_UI_AssignableElement_type"),
				 CargoPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Spot Index feature.
	 * <!-- begin-user-doc -->
	 * @since 8.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSpotIndexPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_AssignableElement_spotIndex_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_AssignableElement_spotIndex_feature", "_UI_AssignableElement_type"),
				 CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Sequence Hint feature.
	 * <!-- begin-user-doc -->
	 * @since 8.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSequenceHintPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_AssignableElement_sequenceHint_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_AssignableElement_sequenceHint_feature", "_UI_AssignableElement_type"),
				 CargoPackage.Literals.ASSIGNABLE_ELEMENT__SEQUENCE_HINT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Locked feature.
	 * <!-- begin-user-doc -->
	 * @since 8.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addLockedPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_AssignableElement_locked_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_AssignableElement_locked_feature", "_UI_AssignableElement_type"),
				 CargoPackage.Literals.ASSIGNABLE_ELEMENT__LOCKED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Window Start feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addWindowStartPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_windowStart_feature"),
				 getString("_UI_Slot_windowStart_description"),
				 CargoPackage.Literals.SLOT__WINDOW_START,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Window Start Time feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addWindowStartTimePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_windowStartTime_feature"),
				 getString("_UI_Slot_windowStartTime_description"),
				 CargoPackage.Literals.SLOT__WINDOW_START_TIME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Window Size feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addWindowSizePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_windowSize_feature"),
				 getString("_UI_Slot_windowSize_description"),
				 CargoPackage.Literals.SLOT__WINDOW_SIZE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Port feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPortPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_port_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Slot_port_feature", "_UI_Slot_type"),
				 CargoPackage.Literals.SLOT__PORT,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Contract feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addContractPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_contract_feature"),
				 getString("_UI_Slot_contract_description"),
				 CargoPackage.Literals.SLOT__CONTRACT,
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
				 getString("_UI_Slot_duration_feature"),
				 getString("_UI_Slot_duration_description"),
				 CargoPackage.Literals.SLOT__DURATION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Min Quantity feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMinQuantityPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_minQuantity_feature"),
				 getString("_UI_Slot_minQuantity_description"),
				 CargoPackage.Literals.SLOT__MIN_QUANTITY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Max Quantity feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMaxQuantityPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_maxQuantity_feature"),
				 getString("_UI_Slot_maxQuantity_description"),
				 CargoPackage.Literals.SLOT__MAX_QUANTITY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Optional feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addOptionalPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_optional_feature"),
				 getString("_UI_Slot_optional_description"),
				 CargoPackage.Literals.SLOT__OPTIONAL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Price Expression feature.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPriceExpressionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_priceExpression_feature"),
				 getString("_UI_Slot_priceExpression_description"),
				 CargoPackage.Literals.SLOT__PRICE_EXPRESSION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Cargo feature.
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCargoPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_cargo_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Slot_cargo_feature", "_UI_Slot_type"),
				 CargoPackage.Literals.SLOT__CARGO,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Pricing Date feature.
	 * <!-- begin-user-doc -->
	 * @since 5.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPricingDatePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_pricingDate_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Slot_pricingDate_feature", "_UI_Slot_type"),
				 CargoPackage.Literals.SLOT__PRICING_DATE,
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
	 * @since 5.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNotesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_notes_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Slot_notes_feature", "_UI_Slot_type"),
				 CargoPackage.Literals.SLOT__NOTES,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Shipping Days Restriction feature.
	 * <!-- begin-user-doc -->
	 * @since 8.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addShippingDaysRestrictionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_shippingDaysRestriction_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Slot_shippingDaysRestriction_feature", "_UI_Slot_type"),
				 CargoPackage.Literals.SLOT__SHIPPING_DAYS_RESTRICTION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Entity feature.
	 * <!-- begin-user-doc -->
	 * @since 8.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addEntityPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_entity_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Slot_entity_feature", "_UI_Slot_type"),
				 CargoPackage.Literals.SLOT__ENTITY,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Restricted Contracts feature.
	 * <!-- begin-user-doc -->
	 * @since 8.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRestrictedContractsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_restrictedContracts_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Slot_restrictedContracts_feature", "_UI_Slot_type"),
				 CargoPackage.Literals.SLOT__RESTRICTED_CONTRACTS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Restricted Ports feature.
	 * <!-- begin-user-doc -->
	 * @since 8.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRestrictedPortsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_restrictedPorts_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Slot_restrictedPorts_feature", "_UI_Slot_type"),
				 CargoPackage.Literals.SLOT__RESTRICTED_PORTS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Restricted Lists Are Permissive feature.
	 * <!-- begin-user-doc -->
	 * @since 8.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRestrictedListsArePermissivePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_restrictedListsArePermissive_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Slot_restrictedListsArePermissive_feature", "_UI_Slot_type"),
				 CargoPackage.Literals.SLOT__RESTRICTED_LISTS_ARE_PERMISSIVE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns Slot.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/Slot"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((Slot)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_Slot_type") :
			getString("_UI_Slot_type") + " " + label;
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

		switch (notification.getFeatureID(Slot.class)) {
			case CargoPackage.SLOT__NAME:
			case CargoPackage.SLOT__SPOT_INDEX:
			case CargoPackage.SLOT__SEQUENCE_HINT:
			case CargoPackage.SLOT__LOCKED:
			case CargoPackage.SLOT__WINDOW_START:
			case CargoPackage.SLOT__WINDOW_START_TIME:
			case CargoPackage.SLOT__WINDOW_SIZE:
			case CargoPackage.SLOT__DURATION:
			case CargoPackage.SLOT__MIN_QUANTITY:
			case CargoPackage.SLOT__MAX_QUANTITY:
			case CargoPackage.SLOT__OPTIONAL:
			case CargoPackage.SLOT__PRICE_EXPRESSION:
			case CargoPackage.SLOT__PRICING_DATE:
			case CargoPackage.SLOT__NOTES:
			case CargoPackage.SLOT__SHIPPING_DAYS_RESTRICTION:
			case CargoPackage.SLOT__RESTRICTED_LISTS_ARE_PERMISSIVE:
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
