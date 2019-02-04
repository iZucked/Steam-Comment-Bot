/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.provider.UUIDObjectItemProvider;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.commercial.Contract} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ContractItemProvider
	extends UUIDObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ContractItemProvider(AdapterFactory adapterFactory) {
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
			addCodePropertyDescriptor(object);
			addCounterpartyPropertyDescriptor(object);
			addCnPropertyDescriptor(object);
			addEntityPropertyDescriptor(object);
			addStartDatePropertyDescriptor(object);
			addEndDatePropertyDescriptor(object);
			addContractYearStartPropertyDescriptor(object);
			addAllowedPortsPropertyDescriptor(object);
			addPreferredPortPropertyDescriptor(object);
			addMinQuantityPropertyDescriptor(object);
			addMaxQuantityPropertyDescriptor(object);
			addVolumeLimitsUnitPropertyDescriptor(object);
			addOperationalTolerancePropertyDescriptor(object);
			addRestrictedListsArePermissivePropertyDescriptor(object);
			addRestrictedContractsPropertyDescriptor(object);
			addRestrictedPortsPropertyDescriptor(object);
			addPriceInfoPropertyDescriptor(object);
			addNotesPropertyDescriptor(object);
			addContractTypePropertyDescriptor(object);
			addPricingEventPropertyDescriptor(object);
			addCancellationExpressionPropertyDescriptor(object);
			addWindowNominationSizePropertyDescriptor(object);
			addWindowNominationSizeUnitsPropertyDescriptor(object);
			addWindowNominationCounterpartyPropertyDescriptor(object);
			addVesselNominationSizePropertyDescriptor(object);
			addVesselNominationSizeUnitsPropertyDescriptor(object);
			addVesselNominationCounterpartyPropertyDescriptor(object);
			addVolumeNominationSizePropertyDescriptor(object);
			addVolumeNominationSizeUnitsPropertyDescriptor(object);
			addVolumeNominationCounterpartyPropertyDescriptor(object);
			addPortNominationSizePropertyDescriptor(object);
			addPortNominationSizeUnitsPropertyDescriptor(object);
			addPortNominationCounterpartyPropertyDescriptor(object);
			addDivertiblePropertyDescriptor(object);
			addShippingDaysRestrictionPropertyDescriptor(object);
			addPortLoadNominationSizePropertyDescriptor(object);
			addPortLoadNominationSizeUnitsPropertyDescriptor(object);
			addPortLoadNominationCounterpartyPropertyDescriptor(object);
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
				 getString("_UI_Contract_entity_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Contract_entity_feature", "_UI_Contract_type"),
				 CommercialPackage.Literals.CONTRACT__ENTITY,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Allowed Ports feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addAllowedPortsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Contract_allowedPorts_feature"),
				 getString("_UI_Contract_allowedPorts_description"),
				 CommercialPackage.Literals.CONTRACT__ALLOWED_PORTS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Preferred Port feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPreferredPortPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Contract_preferredPort_feature"),
				 getString("_UI_Contract_preferredPort_description"),
				 CommercialPackage.Literals.CONTRACT__PREFERRED_PORT,
				 true,
				 false,
				 true,
				 null,
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
				 getString("_UI_Contract_minQuantity_feature"),
				 getString("_UI_Contract_minQuantity_description"),
				 CommercialPackage.Literals.CONTRACT__MIN_QUANTITY,
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
				 getString("_UI_Contract_maxQuantity_feature"),
				 getString("_UI_Contract_maxQuantity_description"),
				 CommercialPackage.Literals.CONTRACT__MAX_QUANTITY,
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
				 getString("_UI_Contract_operationalTolerance_feature"),
				 getString("_UI_Contract_operationalTolerance_description"),
				 CommercialPackage.Literals.CONTRACT__OPERATIONAL_TOLERANCE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
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
				 getString("_UI_Contract_volumeLimitsUnit_feature"),
				 getString("_UI_Contract_volumeLimitsUnit_description"),
				 CommercialPackage.Literals.CONTRACT__VOLUME_LIMITS_UNIT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Restricted Lists Are Permissive feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRestrictedListsArePermissivePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Contract_restrictedListsArePermissive_feature"),
				 getString("_UI_Contract_restrictedListsArePermissive_description"),
				 CommercialPackage.Literals.CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
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
				 getString("_UI_Contract_restrictedContracts_feature"),
				 getString("_UI_Contract_restrictedContracts_description"),
				 CommercialPackage.Literals.CONTRACT__RESTRICTED_CONTRACTS,
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
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRestrictedPortsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Contract_restrictedPorts_feature"),
				 getString("_UI_Contract_restrictedPorts_description"),
				 CommercialPackage.Literals.CONTRACT__RESTRICTED_PORTS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Price Info feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPriceInfoPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Contract_priceInfo_feature"),
				 getString("_UI_Contract_priceInfo_description"),
				 CommercialPackage.Literals.CONTRACT__PRICE_INFO,
				 false,
				 false,
				 false,
				 null,
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
				 getString("_UI_Contract_notes_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Contract_notes_feature", "_UI_Contract_type"),
				 CommercialPackage.Literals.CONTRACT__NOTES,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Contract Type feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addContractTypePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Contract_contractType_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Contract_contractType_feature", "_UI_Contract_type"),
				 CommercialPackage.Literals.CONTRACT__CONTRACT_TYPE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
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
				 getString("_UI_Contract_pricingEvent_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Contract_pricingEvent_feature", "_UI_Contract_type"),
				 CommercialPackage.Literals.CONTRACT__PRICING_EVENT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
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
				 getString("_UI_Contract_cancellationExpression_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Contract_cancellationExpression_feature", "_UI_Contract_type"),
				 CommercialPackage.Literals.CONTRACT__CANCELLATION_EXPRESSION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Window Nomination Size feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addWindowNominationSizePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Contract_windowNominationSize_feature"),
				 getString("_UI_Contract_windowNominationSize_description"),
				 CommercialPackage.Literals.CONTRACT__WINDOW_NOMINATION_SIZE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Window Nomination Size Units feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addWindowNominationSizeUnitsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Contract_windowNominationSizeUnits_feature"),
				 getString("_UI_Contract_windowNominationSizeUnits_description"),
				 CommercialPackage.Literals.CONTRACT__WINDOW_NOMINATION_SIZE_UNITS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Window Nomination Counterparty feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addWindowNominationCounterpartyPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Contract_windowNominationCounterparty_feature"),
				 getString("_UI_Contract_windowNominationCounterparty_description"),
				 CommercialPackage.Literals.CONTRACT__WINDOW_NOMINATION_COUNTERPARTY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Divertible feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDivertiblePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Contract_divertible_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Contract_divertible_feature", "_UI_Contract_type"),
				 CommercialPackage.Literals.CONTRACT__DIVERTIBLE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
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
				 getString("_UI_Contract_shippingDaysRestriction_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Contract_shippingDaysRestriction_feature", "_UI_Contract_type"),
				 CommercialPackage.Literals.CONTRACT__SHIPPING_DAYS_RESTRICTION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Port Load Nomination Size feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPortLoadNominationSizePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Contract_portLoadNominationSize_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Contract_portLoadNominationSize_feature", "_UI_Contract_type"),
				 CommercialPackage.Literals.CONTRACT__PORT_LOAD_NOMINATION_SIZE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Port Load Nomination Size Units feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPortLoadNominationSizeUnitsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Contract_portLoadNominationSizeUnits_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Contract_portLoadNominationSizeUnits_feature", "_UI_Contract_type"),
				 CommercialPackage.Literals.CONTRACT__PORT_LOAD_NOMINATION_SIZE_UNITS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Port Load Nomination Counterparty feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPortLoadNominationCounterpartyPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Contract_portLoadNominationCounterparty_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Contract_portLoadNominationCounterparty_feature", "_UI_Contract_type"),
				 CommercialPackage.Literals.CONTRACT__PORT_LOAD_NOMINATION_COUNTERPARTY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Vessel Nomination Size feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVesselNominationSizePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Contract_vesselNominationSize_feature"),
				 getString("_UI_Contract_vesselNominationSize_description"),
				 CommercialPackage.Literals.CONTRACT__VESSEL_NOMINATION_SIZE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Vessel Nomination Size Units feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVesselNominationSizeUnitsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Contract_vesselNominationSizeUnits_feature"),
				 getString("_UI_Contract_vesselNominationSizeUnits_description"),
				 CommercialPackage.Literals.CONTRACT__VESSEL_NOMINATION_SIZE_UNITS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Vessel Nomination Counterparty feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVesselNominationCounterpartyPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Contract_vesselNominationCounterparty_feature"),
				 getString("_UI_Contract_vesselNominationCounterparty_description"),
				 CommercialPackage.Literals.CONTRACT__VESSEL_NOMINATION_COUNTERPARTY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Volume Nomination Size feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVolumeNominationSizePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Contract_volumeNominationSize_feature"),
				 getString("_UI_Contract_volumeNominationSize_description"),
				 CommercialPackage.Literals.CONTRACT__VOLUME_NOMINATION_SIZE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Volume Nomination Size Units feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVolumeNominationSizeUnitsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Contract_volumeNominationSizeUnits_feature"),
				 getString("_UI_Contract_volumeNominationSizeUnits_description"),
				 CommercialPackage.Literals.CONTRACT__VOLUME_NOMINATION_SIZE_UNITS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Volume Nomination Counterparty feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVolumeNominationCounterpartyPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Contract_volumeNominationCounterparty_feature"),
				 getString("_UI_Contract_volumeNominationCounterparty_description"),
				 CommercialPackage.Literals.CONTRACT__VOLUME_NOMINATION_COUNTERPARTY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Port Nomination Size feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPortNominationSizePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Contract_portNominationSize_feature"),
				 getString("_UI_Contract_portNominationSize_description"),
				 CommercialPackage.Literals.CONTRACT__PORT_NOMINATION_SIZE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Port Nomination Size Units feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPortNominationSizeUnitsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Contract_portNominationSizeUnits_feature"),
				 getString("_UI_Contract_portNominationSizeUnits_description"),
				 CommercialPackage.Literals.CONTRACT__PORT_NOMINATION_SIZE_UNITS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Port Nomination Counterparty feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPortNominationCounterpartyPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Contract_portNominationCounterparty_feature"),
				 getString("_UI_Contract_portNominationCounterparty_description"),
				 CommercialPackage.Literals.CONTRACT__PORT_NOMINATION_COUNTERPARTY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Contract Year Start feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addContractYearStartPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Contract_contractYearStart_feature"),
				 getString("_UI_Contract_contractYearStart_description"),
				 CommercialPackage.Literals.CONTRACT__CONTRACT_YEAR_START,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Code feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCodePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Contract_code_feature"),
				 getString("_UI_Contract_code_description"),
				 CommercialPackage.Literals.CONTRACT__CODE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
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
				 getString("_UI_Contract_counterparty_feature"),
				 getString("_UI_Contract_counterparty_description"),
				 CommercialPackage.Literals.CONTRACT__COUNTERPARTY,
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
				 getString("_UI_Contract_cn_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Contract_cn_feature", "_UI_Contract_type"),
				 CommercialPackage.Literals.CONTRACT__CN,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Start Date feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addStartDatePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Contract_startDate_feature"),
				 getString("_UI_Contract_startDate_description"),
				 CommercialPackage.Literals.CONTRACT__START_DATE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the End Date feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addEndDatePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Contract_endDate_feature"),
				 getString("_UI_Contract_endDate_description"),
				 CommercialPackage.Literals.CONTRACT__END_DATE,
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
			childrenFeatures.add(CommercialPackage.Literals.CONTRACT__PRICE_INFO);
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
	 * This returns Contract.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/Contract"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		String label = ((Contract)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_Contract_type") :
			label;
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

		switch (notification.getFeatureID(Contract.class)) {
			case CommercialPackage.CONTRACT__NAME:
			case CommercialPackage.CONTRACT__CODE:
			case CommercialPackage.CONTRACT__COUNTERPARTY:
			case CommercialPackage.CONTRACT__CN:
			case CommercialPackage.CONTRACT__START_DATE:
			case CommercialPackage.CONTRACT__END_DATE:
			case CommercialPackage.CONTRACT__CONTRACT_YEAR_START:
			case CommercialPackage.CONTRACT__MIN_QUANTITY:
			case CommercialPackage.CONTRACT__MAX_QUANTITY:
			case CommercialPackage.CONTRACT__VOLUME_LIMITS_UNIT:
			case CommercialPackage.CONTRACT__OPERATIONAL_TOLERANCE:
			case CommercialPackage.CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE:
			case CommercialPackage.CONTRACT__NOTES:
			case CommercialPackage.CONTRACT__CONTRACT_TYPE:
			case CommercialPackage.CONTRACT__PRICING_EVENT:
			case CommercialPackage.CONTRACT__CANCELLATION_EXPRESSION:
			case CommercialPackage.CONTRACT__WINDOW_NOMINATION_SIZE:
			case CommercialPackage.CONTRACT__WINDOW_NOMINATION_SIZE_UNITS:
			case CommercialPackage.CONTRACT__WINDOW_NOMINATION_COUNTERPARTY:
			case CommercialPackage.CONTRACT__VESSEL_NOMINATION_SIZE:
			case CommercialPackage.CONTRACT__VESSEL_NOMINATION_SIZE_UNITS:
			case CommercialPackage.CONTRACT__VESSEL_NOMINATION_COUNTERPARTY:
			case CommercialPackage.CONTRACT__VOLUME_NOMINATION_SIZE:
			case CommercialPackage.CONTRACT__VOLUME_NOMINATION_SIZE_UNITS:
			case CommercialPackage.CONTRACT__VOLUME_NOMINATION_COUNTERPARTY:
			case CommercialPackage.CONTRACT__PORT_NOMINATION_SIZE:
			case CommercialPackage.CONTRACT__PORT_NOMINATION_SIZE_UNITS:
			case CommercialPackage.CONTRACT__PORT_NOMINATION_COUNTERPARTY:
			case CommercialPackage.CONTRACT__DIVERTIBLE:
			case CommercialPackage.CONTRACT__SHIPPING_DAYS_RESTRICTION:
			case CommercialPackage.CONTRACT__PORT_LOAD_NOMINATION_SIZE:
			case CommercialPackage.CONTRACT__PORT_LOAD_NOMINATION_SIZE_UNITS:
			case CommercialPackage.CONTRACT__PORT_LOAD_NOMINATION_COUNTERPARTY:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case CommercialPackage.CONTRACT__PRICE_INFO:
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
				(CommercialPackage.Literals.CONTRACT__PRICE_INFO,
				 CommercialFactory.eINSTANCE.createExpressionPriceParameters()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.CONTRACT__PRICE_INFO,
				 CommercialFactory.eINSTANCE.createDateShiftExpressionPriceParameters()));
	}

}
