/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
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
	extends UUIDObjectItemProvider {
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
			addContractPropertyDescriptor(object);
			addCounterpartyPropertyDescriptor(object);
			addCnPropertyDescriptor(object);
			addPortPropertyDescriptor(object);
			addWindowStartPropertyDescriptor(object);
			addWindowStartTimePropertyDescriptor(object);
			addWindowSizePropertyDescriptor(object);
			addWindowSizeUnitsPropertyDescriptor(object);
			addWindowFlexPropertyDescriptor(object);
			addWindowFlexUnitsPropertyDescriptor(object);
			addDurationPropertyDescriptor(object);
			addVolumeLimitsUnitPropertyDescriptor(object);
			addMinQuantityPropertyDescriptor(object);
			addMaxQuantityPropertyDescriptor(object);
			addOperationalTolerancePropertyDescriptor(object);
			addFullCargoLotPropertyDescriptor(object);
			addOptionalPropertyDescriptor(object);
			addPriceExpressionPropertyDescriptor(object);
			addCargoPropertyDescriptor(object);
			addPricingEventPropertyDescriptor(object);
			addPricingDatePropertyDescriptor(object);
			addNotesPropertyDescriptor(object);
			addShippingDaysRestrictionPropertyDescriptor(object);
			addEntityPropertyDescriptor(object);
			addRestrictedContractsPropertyDescriptor(object);
			addRestrictedContractsArePermissivePropertyDescriptor(object);
			addRestrictedContractsOverridePropertyDescriptor(object);
			addRestrictedPortsPropertyDescriptor(object);
			addRestrictedPortsArePermissivePropertyDescriptor(object);
			addRestrictedPortsOverridePropertyDescriptor(object);
			addRestrictedVesselsPropertyDescriptor(object);
			addRestrictedVesselsArePermissivePropertyDescriptor(object);
			addRestrictedVesselsOverridePropertyDescriptor(object);
			addRestrictedSlotsPropertyDescriptor(object);
			addRestrictedSlotsArePermissivePropertyDescriptor(object);
			addHedgesPropertyDescriptor(object);
			addMiscCostsPropertyDescriptor(object);
			addCancellationExpressionPropertyDescriptor(object);
			addNominatedVesselPropertyDescriptor(object);
			addLockedPropertyDescriptor(object);
			addCancelledPropertyDescriptor(object);
			addWindowCounterPartyPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Name feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_NamedObject_name_feature"),
				 getString("_UI_NamedObject_name_description"),
				 MMXCorePackage.Literals.NAMED_OBJECT__NAME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
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
	 * This adds a property descriptor for the Window Size Units feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addWindowSizeUnitsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_windowSizeUnits_feature"),
				 getString("_UI_Slot_windowSizeUnits_description"),
				 CargoPackage.Literals.SLOT__WINDOW_SIZE_UNITS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Window Flex feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addWindowFlexPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_windowFlex_feature"),
				 getString("_UI_Slot_windowFlex_description"),
				 CargoPackage.Literals.SLOT__WINDOW_FLEX,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Window Flex Units feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addWindowFlexUnitsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_windowFlexUnits_feature"),
				 getString("_UI_Slot_windowFlexUnits_description"),
				 CargoPackage.Literals.SLOT__WINDOW_FLEX_UNITS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
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
				 getString("_UI_Slot_port_description"),
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
				 getString("_UI_Slot_counterparty_feature"),
				 getString("_UI_Slot_counterparty_description"),
				 CargoPackage.Literals.SLOT__COUNTERPARTY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Cn feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCnPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_cn_feature"),
				 getString("_UI_Slot_cn_description"),
				 CargoPackage.Literals.SLOT__CN,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
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
	 * This adds a property descriptor for the Volume Limits Unit feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVolumeLimitsUnitPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_volumeLimitsUnit_feature"),
				 getString("_UI_Slot_volumeLimitsUnit_description"),
				 CargoPackage.Literals.SLOT__VOLUME_LIMITS_UNIT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
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
	 * This adds a property descriptor for the Operational Tolerance feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addOperationalTolerancePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_operationalTolerance_feature"),
				 getString("_UI_Slot_operationalTolerance_description"),
				 CargoPackage.Literals.SLOT__OPERATIONAL_TOLERANCE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Full Cargo Lot feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addFullCargoLotPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_fullCargoLot_feature"),
				 getString("_UI_Slot_fullCargoLot_description"),
				 CargoPackage.Literals.SLOT__FULL_CARGO_LOT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
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
	 * This adds a property descriptor for the Pricing Event feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPricingEventPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_pricingEvent_feature"),
				 getString("_UI_Slot_pricingEvent_description"),
				 CargoPackage.Literals.SLOT__PRICING_EVENT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Pricing Date feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPricingDatePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_pricingDate_feature"),
				 getString("_UI_Slot_pricingDate_description"),
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
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNotesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_notes_feature"),
				 getString("_UI_Slot_notes_description"),
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
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addShippingDaysRestrictionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_shippingDaysRestriction_feature"),
				 getString("_UI_Slot_shippingDaysRestriction_description"),
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
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addEntityPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_entity_feature"),
				 getString("_UI_Slot_entity_description"),
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
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRestrictedContractsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_restrictedContracts_feature"),
				 getString("_UI_Slot_restrictedContracts_description"),
				 CargoPackage.Literals.SLOT__RESTRICTED_CONTRACTS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Restricted Contracts Are Permissive feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRestrictedContractsArePermissivePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_restrictedContractsArePermissive_feature"),
				 getString("_UI_Slot_restrictedContractsArePermissive_description"),
				 CargoPackage.Literals.SLOT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Restricted Contracts Override feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRestrictedContractsOverridePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_restrictedContractsOverride_feature"),
				 getString("_UI_Slot_restrictedContractsOverride_description"),
				 CargoPackage.Literals.SLOT__RESTRICTED_CONTRACTS_OVERRIDE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Restricted Ports feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRestrictedPortsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_restrictedPorts_feature"),
				 getString("_UI_Slot_restrictedPorts_description"),
				 CargoPackage.Literals.SLOT__RESTRICTED_PORTS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Restricted Ports Are Permissive feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRestrictedPortsArePermissivePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_restrictedPortsArePermissive_feature"),
				 getString("_UI_Slot_restrictedPortsArePermissive_description"),
				 CargoPackage.Literals.SLOT__RESTRICTED_PORTS_ARE_PERMISSIVE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Restricted Ports Override feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRestrictedPortsOverridePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_restrictedPortsOverride_feature"),
				 getString("_UI_Slot_restrictedPortsOverride_description"),
				 CargoPackage.Literals.SLOT__RESTRICTED_PORTS_OVERRIDE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Restricted Slots feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRestrictedSlotsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_restrictedSlots_feature"),
				 getString("_UI_Slot_restrictedSlots_description"),
				 CargoPackage.Literals.SLOT__RESTRICTED_SLOTS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Restricted Slots Are Permissive feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRestrictedSlotsArePermissivePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_restrictedSlotsArePermissive_feature"),
				 getString("_UI_Slot_restrictedSlotsArePermissive_description"),
				 CargoPackage.Literals.SLOT__RESTRICTED_SLOTS_ARE_PERMISSIVE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Restricted Vessels feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRestrictedVesselsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_restrictedVessels_feature"),
				 getString("_UI_Slot_restrictedVessels_description"),
				 CargoPackage.Literals.SLOT__RESTRICTED_VESSELS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Restricted Vessels Are Permissive feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRestrictedVesselsArePermissivePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_restrictedVesselsArePermissive_feature"),
				 getString("_UI_Slot_restrictedVesselsArePermissive_description"),
				 CargoPackage.Literals.SLOT__RESTRICTED_VESSELS_ARE_PERMISSIVE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Restricted Vessels Override feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRestrictedVesselsOverridePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_restrictedVesselsOverride_feature"),
				 getString("_UI_Slot_restrictedVesselsOverride_description"),
				 CargoPackage.Literals.SLOT__RESTRICTED_VESSELS_OVERRIDE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Hedges feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addHedgesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_hedges_feature"),
				 getString("_UI_Slot_hedges_description"),
				 CargoPackage.Literals.SLOT__HEDGES,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Misc Costs feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMiscCostsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_miscCosts_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Slot_miscCosts_feature", "_UI_Slot_type"),
				 CargoPackage.Literals.SLOT__MISC_COSTS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Cancellation Expression feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCancellationExpressionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_cancellationExpression_feature"),
				 getString("_UI_Slot_cancellationExpression_description"),
				 CargoPackage.Literals.SLOT__CANCELLATION_EXPRESSION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Nominated Vessel feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNominatedVesselPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_nominatedVessel_feature"),
				 getString("_UI_Slot_nominatedVessel_description"),
				 CargoPackage.Literals.SLOT__NOMINATED_VESSEL,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Locked feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addLockedPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_locked_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Slot_locked_feature", "_UI_Slot_type"),
				 CargoPackage.Literals.SLOT__LOCKED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Cancelled feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCancelledPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_cancelled_feature"),
				 getString("_UI_Slot_cancelled_description"),
				 CargoPackage.Literals.SLOT__CANCELLED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Window Counter Party feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addWindowCounterPartyPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Slot_windowCounterParty_feature"),
				 getString("_UI_Slot_windowCounterParty_description"),
				 CargoPackage.Literals.SLOT__WINDOW_COUNTER_PARTY,
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
		String label = ((Slot<?>)object).getName();
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
			case CargoPackage.SLOT__COUNTERPARTY:
			case CargoPackage.SLOT__CN:
			case CargoPackage.SLOT__WINDOW_START:
			case CargoPackage.SLOT__WINDOW_START_TIME:
			case CargoPackage.SLOT__WINDOW_SIZE:
			case CargoPackage.SLOT__WINDOW_SIZE_UNITS:
			case CargoPackage.SLOT__WINDOW_FLEX:
			case CargoPackage.SLOT__WINDOW_FLEX_UNITS:
			case CargoPackage.SLOT__DURATION:
			case CargoPackage.SLOT__VOLUME_LIMITS_UNIT:
			case CargoPackage.SLOT__MIN_QUANTITY:
			case CargoPackage.SLOT__MAX_QUANTITY:
			case CargoPackage.SLOT__OPERATIONAL_TOLERANCE:
			case CargoPackage.SLOT__FULL_CARGO_LOT:
			case CargoPackage.SLOT__OPTIONAL:
			case CargoPackage.SLOT__PRICE_EXPRESSION:
			case CargoPackage.SLOT__PRICING_EVENT:
			case CargoPackage.SLOT__PRICING_DATE:
			case CargoPackage.SLOT__NOTES:
			case CargoPackage.SLOT__SHIPPING_DAYS_RESTRICTION:
			case CargoPackage.SLOT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE:
			case CargoPackage.SLOT__RESTRICTED_CONTRACTS_OVERRIDE:
			case CargoPackage.SLOT__RESTRICTED_PORTS_ARE_PERMISSIVE:
			case CargoPackage.SLOT__RESTRICTED_PORTS_OVERRIDE:
			case CargoPackage.SLOT__RESTRICTED_VESSELS_ARE_PERMISSIVE:
			case CargoPackage.SLOT__RESTRICTED_VESSELS_OVERRIDE:
			case CargoPackage.SLOT__RESTRICTED_SLOTS_ARE_PERMISSIVE:
			case CargoPackage.SLOT__HEDGES:
			case CargoPackage.SLOT__MISC_COSTS:
			case CargoPackage.SLOT__CANCELLATION_EXPRESSION:
			case CargoPackage.SLOT__LOCKED:
			case CargoPackage.SLOT__CANCELLED:
			case CargoPackage.SLOT__WINDOW_COUNTER_PARTY:
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
