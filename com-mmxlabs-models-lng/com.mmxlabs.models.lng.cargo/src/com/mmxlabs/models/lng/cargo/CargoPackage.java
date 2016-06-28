/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.cargo.CargoFactory
 * @model kind="package"
 * @generated
 */
public interface CargoPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "cargo";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.mmxlabs.com/models/lng/cargo/1/";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "lng.cargo";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	CargoPackage eINSTANCE = com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.CargoImpl <em>Cargo</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getCargo()
	 * @generated
	 */
	int CARGO = 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl <em>Slot</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.SlotImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getSlot()
	 * @generated
	 */
	int SLOT = 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.LoadSlotImpl <em>Load Slot</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.LoadSlotImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getLoadSlot()
	 * @generated
	 */
	int LOAD_SLOT = 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.DischargeSlotImpl <em>Discharge Slot</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.DischargeSlotImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getDischargeSlot()
	 * @generated
	 */
	int DISCHARGE_SLOT = 4;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.CargoModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoModelImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getCargoModel()
	 * @generated
	 */
	int CARGO_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Load Slots</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL__LOAD_SLOTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Discharge Slots</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL__DISCHARGE_SLOTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Cargoes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL__CARGOES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Cargo Groups</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL__CARGO_GROUPS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Vessel Availabilities</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL__VESSEL_AVAILABILITIES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Vessel Events</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL__VESSEL_EVENTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Vessel Type Groups</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL__VESSEL_TYPE_GROUPS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL___ECONTAINER_OP = MMXCorePackage.UUID_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL_OPERATION_COUNT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Sequence Hint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO__SEQUENCE_HINT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Vessel Assignment Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO__VESSEL_ASSIGNMENT_TYPE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Spot Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO__SPOT_INDEX = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Locked</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO__LOCKED = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Allow Rewiring</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO__ALLOW_REWIRING = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Slots</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO__SLOTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Cargo</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO___ECONTAINER_OP = MMXCorePackage.UUID_OBJECT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Cargo Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO___GET_CARGO_TYPE = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Sorted Slots</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO___GET_SORTED_SLOTS = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Load Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO___GET_LOAD_NAME = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 2;

	/**
	 * The number of operations of the '<em>Cargo</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_OPERATION_COUNT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__CONTRACT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__PORT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Window Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__WINDOW_START = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Window Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__WINDOW_START_TIME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Window Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__WINDOW_SIZE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Window Size Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__WINDOW_SIZE_UNITS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Window Flex</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__WINDOW_FLEX = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Window Flex Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__WINDOW_FLEX_UNITS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__DURATION = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Volume Limits Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__VOLUME_LIMITS_UNIT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__MIN_QUANTITY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__MAX_QUANTITY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 12;

	/**
	 * The feature id for the '<em><b>Optional</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__OPTIONAL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 13;

	/**
	 * The feature id for the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__PRICE_EXPRESSION = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 14;

	/**
	 * The feature id for the '<em><b>Cargo</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__CARGO = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 15;

	/**
	 * The feature id for the '<em><b>Pricing Event</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__PRICING_EVENT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 16;

	/**
	 * The feature id for the '<em><b>Pricing Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__PRICING_DATE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 17;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__NOTES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 18;

	/**
	 * The feature id for the '<em><b>Divertible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__DIVERTIBLE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 19;

	/**
	 * The feature id for the '<em><b>Shipping Days Restriction</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__SHIPPING_DAYS_RESTRICTION = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 20;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__ENTITY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 21;

	/**
	 * The feature id for the '<em><b>Restricted Contracts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__RESTRICTED_CONTRACTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 22;

	/**
	 * The feature id for the '<em><b>Restricted Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__RESTRICTED_PORTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 23;

	/**
	 * The feature id for the '<em><b>Restricted Lists Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__RESTRICTED_LISTS_ARE_PERMISSIVE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 24;

	/**
	 * The feature id for the '<em><b>Hedges</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__HEDGES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 25;

	/**
	 * The feature id for the '<em><b>Allowed Vessels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__ALLOWED_VESSELS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 26;

	/**
	 * The feature id for the '<em><b>Cancellation Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__CANCELLATION_EXPRESSION = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 27;

	/**
	 * The feature id for the '<em><b>Override Restrictions</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__OVERRIDE_RESTRICTIONS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 28;

	/**
	 * The feature id for the '<em><b>Nominated Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__NOMINATED_VESSEL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 29;

	/**
	 * The feature id for the '<em><b>Locked</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__LOCKED = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 30;

	/**
	 * The number of structural features of the '<em>Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 31;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___ECONTAINER_OP = MMXCorePackage.UUID_OBJECT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Time Zone</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_TIME_ZONE__EATTRIBUTE = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Slot Or Port Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_PORT_DURATION = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Min Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_CONTRACT_MIN_QUANTITY = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 2;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Max Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_CONTRACT_MAX_QUANTITY = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 3;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Volume Limits Unit</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_CONTRACT_VOLUME_LIMITS_UNIT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 4;

	/**
	 * The operation id for the '<em>Get Window End With Slot Or Port Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 5;

	/**
	 * The operation id for the '<em>Get Window Start With Slot Or Port Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 6;

	/**
	 * The operation id for the '<em>Get Window End With Slot Or Port Time With Flex</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME_WITH_FLEX = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 7;

	/**
	 * The operation id for the '<em>Get Window Start With Slot Or Port Time With Flex</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME_WITH_FLEX = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 8;

	/**
	 * The operation id for the '<em>Get Slot Or Port Window Size</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 9;

	/**
	 * The operation id for the '<em>Get Slot Or Port Window Size Units</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE_UNITS = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 10;

	/**
	 * The operation id for the '<em>Get Window Size In Hours</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_WINDOW_SIZE_IN_HOURS = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 11;

	/**
	 * The operation id for the '<em>Get Slot Or Delegated Entity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_DELEGATED_ENTITY = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 12;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Restricted Contracts</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_CONTRACTS = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 13;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Restricted Ports</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_PORTS = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 14;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Restricted Lists Are Permissive</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_LISTS_ARE_PERMISSIVE = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 15;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Cancellation Expression</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_CONTRACT_CANCELLATION_EXPRESSION = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 16;

	/**
	 * The operation id for the '<em>Get Slot Or Delegated Pricing Event</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_DELEGATED_PRICING_EVENT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 17;

	/**
	 * The operation id for the '<em>Get Pricing Date As Date Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_PRICING_DATE_AS_DATE_TIME = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 18;

	/**
	 * The operation id for the '<em>Get Slot Contract Params</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_CONTRACT_PARAMS = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 19;

	/**
	 * The number of operations of the '<em>Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_OPERATION_COUNT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 20;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__EXTENSIONS = SLOT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__UUID = SLOT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__NAME = SLOT__NAME;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__CONTRACT = SLOT__CONTRACT;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__PORT = SLOT__PORT;

	/**
	 * The feature id for the '<em><b>Window Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__WINDOW_START = SLOT__WINDOW_START;

	/**
	 * The feature id for the '<em><b>Window Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__WINDOW_START_TIME = SLOT__WINDOW_START_TIME;

	/**
	 * The feature id for the '<em><b>Window Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__WINDOW_SIZE = SLOT__WINDOW_SIZE;

	/**
	 * The feature id for the '<em><b>Window Size Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__WINDOW_SIZE_UNITS = SLOT__WINDOW_SIZE_UNITS;

	/**
	 * The feature id for the '<em><b>Window Flex</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__WINDOW_FLEX = SLOT__WINDOW_FLEX;

	/**
	 * The feature id for the '<em><b>Window Flex Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__WINDOW_FLEX_UNITS = SLOT__WINDOW_FLEX_UNITS;

	/**
	 * The feature id for the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__DURATION = SLOT__DURATION;

	/**
	 * The feature id for the '<em><b>Volume Limits Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__VOLUME_LIMITS_UNIT = SLOT__VOLUME_LIMITS_UNIT;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__MIN_QUANTITY = SLOT__MIN_QUANTITY;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__MAX_QUANTITY = SLOT__MAX_QUANTITY;

	/**
	 * The feature id for the '<em><b>Optional</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__OPTIONAL = SLOT__OPTIONAL;

	/**
	 * The feature id for the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__PRICE_EXPRESSION = SLOT__PRICE_EXPRESSION;

	/**
	 * The feature id for the '<em><b>Cargo</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__CARGO = SLOT__CARGO;

	/**
	 * The feature id for the '<em><b>Pricing Event</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__PRICING_EVENT = SLOT__PRICING_EVENT;

	/**
	 * The feature id for the '<em><b>Pricing Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__PRICING_DATE = SLOT__PRICING_DATE;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__NOTES = SLOT__NOTES;

	/**
	 * The feature id for the '<em><b>Divertible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__DIVERTIBLE = SLOT__DIVERTIBLE;

	/**
	 * The feature id for the '<em><b>Shipping Days Restriction</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__SHIPPING_DAYS_RESTRICTION = SLOT__SHIPPING_DAYS_RESTRICTION;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__ENTITY = SLOT__ENTITY;

	/**
	 * The feature id for the '<em><b>Restricted Contracts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__RESTRICTED_CONTRACTS = SLOT__RESTRICTED_CONTRACTS;

	/**
	 * The feature id for the '<em><b>Restricted Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__RESTRICTED_PORTS = SLOT__RESTRICTED_PORTS;

	/**
	 * The feature id for the '<em><b>Restricted Lists Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__RESTRICTED_LISTS_ARE_PERMISSIVE = SLOT__RESTRICTED_LISTS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Hedges</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__HEDGES = SLOT__HEDGES;

	/**
	 * The feature id for the '<em><b>Allowed Vessels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__ALLOWED_VESSELS = SLOT__ALLOWED_VESSELS;

	/**
	 * The feature id for the '<em><b>Cancellation Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__CANCELLATION_EXPRESSION = SLOT__CANCELLATION_EXPRESSION;

	/**
	 * The feature id for the '<em><b>Override Restrictions</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__OVERRIDE_RESTRICTIONS = SLOT__OVERRIDE_RESTRICTIONS;

	/**
	 * The feature id for the '<em><b>Nominated Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__NOMINATED_VESSEL = SLOT__NOMINATED_VESSEL;

	/**
	 * The feature id for the '<em><b>Locked</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__LOCKED = SLOT__LOCKED;

	/**
	 * The feature id for the '<em><b>Cargo CV</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__CARGO_CV = SLOT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Arrive Cold</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__ARRIVE_COLD = SLOT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>DES Purchase</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__DES_PURCHASE = SLOT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Transfer From</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__TRANSFER_FROM = SLOT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Sales Delivery Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__SALES_DELIVERY_TYPE = SLOT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Load Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT_FEATURE_COUNT = SLOT_FEATURE_COUNT + 5;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = SLOT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = SLOT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___ECONTAINER_OP = SLOT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Time Zone</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_TIME_ZONE__EATTRIBUTE = SLOT___GET_TIME_ZONE__EATTRIBUTE;

	/**
	 * The operation id for the '<em>Get Slot Or Port Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_PORT_DURATION = SLOT___GET_SLOT_OR_PORT_DURATION;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Min Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_CONTRACT_MIN_QUANTITY = SLOT___GET_SLOT_OR_CONTRACT_MIN_QUANTITY;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Max Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_CONTRACT_MAX_QUANTITY = SLOT___GET_SLOT_OR_CONTRACT_MAX_QUANTITY;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Volume Limits Unit</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_CONTRACT_VOLUME_LIMITS_UNIT = SLOT___GET_SLOT_OR_CONTRACT_VOLUME_LIMITS_UNIT;

	/**
	 * The operation id for the '<em>Get Window End With Slot Or Port Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME = SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME;

	/**
	 * The operation id for the '<em>Get Window Start With Slot Or Port Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME = SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME;

	/**
	 * The operation id for the '<em>Get Window End With Slot Or Port Time With Flex</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME_WITH_FLEX = SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME_WITH_FLEX;

	/**
	 * The operation id for the '<em>Get Window Start With Slot Or Port Time With Flex</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME_WITH_FLEX = SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME_WITH_FLEX;

	/**
	 * The operation id for the '<em>Get Slot Or Port Window Size</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE = SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE;

	/**
	 * The operation id for the '<em>Get Slot Or Port Window Size Units</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE_UNITS = SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE_UNITS;

	/**
	 * The operation id for the '<em>Get Window Size In Hours</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_WINDOW_SIZE_IN_HOURS = SLOT___GET_WINDOW_SIZE_IN_HOURS;

	/**
	 * The operation id for the '<em>Get Slot Or Delegated Entity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_DELEGATED_ENTITY = SLOT___GET_SLOT_OR_DELEGATED_ENTITY;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Restricted Contracts</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_CONTRACTS = SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_CONTRACTS;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Restricted Ports</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_PORTS = SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_PORTS;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Restricted Lists Are Permissive</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_LISTS_ARE_PERMISSIVE = SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_LISTS_ARE_PERMISSIVE;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Cancellation Expression</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_CONTRACT_CANCELLATION_EXPRESSION = SLOT___GET_SLOT_OR_CONTRACT_CANCELLATION_EXPRESSION;

	/**
	 * The operation id for the '<em>Get Slot Or Delegated Pricing Event</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_DELEGATED_PRICING_EVENT = SLOT___GET_SLOT_OR_DELEGATED_PRICING_EVENT;

	/**
	 * The operation id for the '<em>Get Pricing Date As Date Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_PRICING_DATE_AS_DATE_TIME = SLOT___GET_PRICING_DATE_AS_DATE_TIME;

	/**
	 * The operation id for the '<em>Get Slot Contract Params</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_CONTRACT_PARAMS = SLOT___GET_SLOT_CONTRACT_PARAMS;

	/**
	 * The operation id for the '<em>Get Slot Or Delegated CV</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_DELEGATED_CV = SLOT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Delivery Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_CONTRACT_DELIVERY_TYPE = SLOT_OPERATION_COUNT + 1;

	/**
	 * The number of operations of the '<em>Load Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT_OPERATION_COUNT = SLOT_OPERATION_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__EXTENSIONS = SLOT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__UUID = SLOT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__NAME = SLOT__NAME;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__CONTRACT = SLOT__CONTRACT;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__PORT = SLOT__PORT;

	/**
	 * The feature id for the '<em><b>Window Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__WINDOW_START = SLOT__WINDOW_START;

	/**
	 * The feature id for the '<em><b>Window Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__WINDOW_START_TIME = SLOT__WINDOW_START_TIME;

	/**
	 * The feature id for the '<em><b>Window Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__WINDOW_SIZE = SLOT__WINDOW_SIZE;

	/**
	 * The feature id for the '<em><b>Window Size Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__WINDOW_SIZE_UNITS = SLOT__WINDOW_SIZE_UNITS;

	/**
	 * The feature id for the '<em><b>Window Flex</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__WINDOW_FLEX = SLOT__WINDOW_FLEX;

	/**
	 * The feature id for the '<em><b>Window Flex Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__WINDOW_FLEX_UNITS = SLOT__WINDOW_FLEX_UNITS;

	/**
	 * The feature id for the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__DURATION = SLOT__DURATION;

	/**
	 * The feature id for the '<em><b>Volume Limits Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__VOLUME_LIMITS_UNIT = SLOT__VOLUME_LIMITS_UNIT;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__MIN_QUANTITY = SLOT__MIN_QUANTITY;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__MAX_QUANTITY = SLOT__MAX_QUANTITY;

	/**
	 * The feature id for the '<em><b>Optional</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__OPTIONAL = SLOT__OPTIONAL;

	/**
	 * The feature id for the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__PRICE_EXPRESSION = SLOT__PRICE_EXPRESSION;

	/**
	 * The feature id for the '<em><b>Cargo</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__CARGO = SLOT__CARGO;

	/**
	 * The feature id for the '<em><b>Pricing Event</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__PRICING_EVENT = SLOT__PRICING_EVENT;

	/**
	 * The feature id for the '<em><b>Pricing Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__PRICING_DATE = SLOT__PRICING_DATE;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__NOTES = SLOT__NOTES;

	/**
	 * The feature id for the '<em><b>Divertible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__DIVERTIBLE = SLOT__DIVERTIBLE;

	/**
	 * The feature id for the '<em><b>Shipping Days Restriction</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__SHIPPING_DAYS_RESTRICTION = SLOT__SHIPPING_DAYS_RESTRICTION;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__ENTITY = SLOT__ENTITY;

	/**
	 * The feature id for the '<em><b>Restricted Contracts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__RESTRICTED_CONTRACTS = SLOT__RESTRICTED_CONTRACTS;

	/**
	 * The feature id for the '<em><b>Restricted Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__RESTRICTED_PORTS = SLOT__RESTRICTED_PORTS;

	/**
	 * The feature id for the '<em><b>Restricted Lists Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__RESTRICTED_LISTS_ARE_PERMISSIVE = SLOT__RESTRICTED_LISTS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Hedges</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__HEDGES = SLOT__HEDGES;

	/**
	 * The feature id for the '<em><b>Allowed Vessels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__ALLOWED_VESSELS = SLOT__ALLOWED_VESSELS;

	/**
	 * The feature id for the '<em><b>Cancellation Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__CANCELLATION_EXPRESSION = SLOT__CANCELLATION_EXPRESSION;

	/**
	 * The feature id for the '<em><b>Override Restrictions</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__OVERRIDE_RESTRICTIONS = SLOT__OVERRIDE_RESTRICTIONS;

	/**
	 * The feature id for the '<em><b>Nominated Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__NOMINATED_VESSEL = SLOT__NOMINATED_VESSEL;

	/**
	 * The feature id for the '<em><b>Locked</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__LOCKED = SLOT__LOCKED;

	/**
	 * The feature id for the '<em><b>FOB Sale</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__FOB_SALE = SLOT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Purchase Delivery Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__PURCHASE_DELIVERY_TYPE = SLOT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Transfer To</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__TRANSFER_TO = SLOT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Min Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__MIN_CV_VALUE = SLOT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Max Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__MAX_CV_VALUE = SLOT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Discharge Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT_FEATURE_COUNT = SLOT_FEATURE_COUNT + 5;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = SLOT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = SLOT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___ECONTAINER_OP = SLOT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Time Zone</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_TIME_ZONE__EATTRIBUTE = SLOT___GET_TIME_ZONE__EATTRIBUTE;

	/**
	 * The operation id for the '<em>Get Slot Or Port Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_PORT_DURATION = SLOT___GET_SLOT_OR_PORT_DURATION;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Min Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_MIN_QUANTITY = SLOT___GET_SLOT_OR_CONTRACT_MIN_QUANTITY;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Max Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_MAX_QUANTITY = SLOT___GET_SLOT_OR_CONTRACT_MAX_QUANTITY;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Volume Limits Unit</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_VOLUME_LIMITS_UNIT = SLOT___GET_SLOT_OR_CONTRACT_VOLUME_LIMITS_UNIT;

	/**
	 * The operation id for the '<em>Get Window End With Slot Or Port Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME = SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME;

	/**
	 * The operation id for the '<em>Get Window Start With Slot Or Port Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME = SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME;

	/**
	 * The operation id for the '<em>Get Window End With Slot Or Port Time With Flex</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME_WITH_FLEX = SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME_WITH_FLEX;

	/**
	 * The operation id for the '<em>Get Window Start With Slot Or Port Time With Flex</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME_WITH_FLEX = SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME_WITH_FLEX;

	/**
	 * The operation id for the '<em>Get Slot Or Port Window Size</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE = SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE;

	/**
	 * The operation id for the '<em>Get Slot Or Port Window Size Units</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE_UNITS = SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE_UNITS;

	/**
	 * The operation id for the '<em>Get Window Size In Hours</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_WINDOW_SIZE_IN_HOURS = SLOT___GET_WINDOW_SIZE_IN_HOURS;

	/**
	 * The operation id for the '<em>Get Slot Or Delegated Entity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_DELEGATED_ENTITY = SLOT___GET_SLOT_OR_DELEGATED_ENTITY;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Restricted Contracts</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_CONTRACTS = SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_CONTRACTS;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Restricted Ports</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_PORTS = SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_PORTS;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Restricted Lists Are Permissive</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_LISTS_ARE_PERMISSIVE = SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_LISTS_ARE_PERMISSIVE;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Cancellation Expression</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_CANCELLATION_EXPRESSION = SLOT___GET_SLOT_OR_CONTRACT_CANCELLATION_EXPRESSION;

	/**
	 * The operation id for the '<em>Get Slot Or Delegated Pricing Event</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_DELEGATED_PRICING_EVENT = SLOT___GET_SLOT_OR_DELEGATED_PRICING_EVENT;

	/**
	 * The operation id for the '<em>Get Pricing Date As Date Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_PRICING_DATE_AS_DATE_TIME = SLOT___GET_PRICING_DATE_AS_DATE_TIME;

	/**
	 * The operation id for the '<em>Get Slot Contract Params</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_CONTRACT_PARAMS = SLOT___GET_SLOT_CONTRACT_PARAMS;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Min Cv</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_MIN_CV = SLOT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Max Cv</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_MAX_CV = SLOT_OPERATION_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Delivery Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_DELIVERY_TYPE = SLOT_OPERATION_COUNT + 2;

	/**
	 * The number of operations of the '<em>Discharge Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT_OPERATION_COUNT = SLOT_OPERATION_COUNT + 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.SpotSlot <em>Spot Slot</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.SpotSlot
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getSpotSlot()
	 * @generated
	 */
	int SPOT_SLOT = 5;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_SLOT__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_SLOT__MARKET = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Spot Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_SLOT_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_SLOT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.MMX_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_SLOT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.MMX_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_SLOT___ECONTAINER_OP = MMXCorePackage.MMX_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>Spot Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_SLOT_OPERATION_COUNT = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.SpotLoadSlotImpl <em>Spot Load Slot</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.SpotLoadSlotImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getSpotLoadSlot()
	 * @generated
	 */
	int SPOT_LOAD_SLOT = 6;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__EXTENSIONS = LOAD_SLOT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__UUID = LOAD_SLOT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__NAME = LOAD_SLOT__NAME;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__CONTRACT = LOAD_SLOT__CONTRACT;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__PORT = LOAD_SLOT__PORT;

	/**
	 * The feature id for the '<em><b>Window Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__WINDOW_START = LOAD_SLOT__WINDOW_START;

	/**
	 * The feature id for the '<em><b>Window Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__WINDOW_START_TIME = LOAD_SLOT__WINDOW_START_TIME;

	/**
	 * The feature id for the '<em><b>Window Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__WINDOW_SIZE = LOAD_SLOT__WINDOW_SIZE;

	/**
	 * The feature id for the '<em><b>Window Size Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__WINDOW_SIZE_UNITS = LOAD_SLOT__WINDOW_SIZE_UNITS;

	/**
	 * The feature id for the '<em><b>Window Flex</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__WINDOW_FLEX = LOAD_SLOT__WINDOW_FLEX;

	/**
	 * The feature id for the '<em><b>Window Flex Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__WINDOW_FLEX_UNITS = LOAD_SLOT__WINDOW_FLEX_UNITS;

	/**
	 * The feature id for the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__DURATION = LOAD_SLOT__DURATION;

	/**
	 * The feature id for the '<em><b>Volume Limits Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__VOLUME_LIMITS_UNIT = LOAD_SLOT__VOLUME_LIMITS_UNIT;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__MIN_QUANTITY = LOAD_SLOT__MIN_QUANTITY;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__MAX_QUANTITY = LOAD_SLOT__MAX_QUANTITY;

	/**
	 * The feature id for the '<em><b>Optional</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__OPTIONAL = LOAD_SLOT__OPTIONAL;

	/**
	 * The feature id for the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__PRICE_EXPRESSION = LOAD_SLOT__PRICE_EXPRESSION;

	/**
	 * The feature id for the '<em><b>Cargo</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__CARGO = LOAD_SLOT__CARGO;

	/**
	 * The feature id for the '<em><b>Pricing Event</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__PRICING_EVENT = LOAD_SLOT__PRICING_EVENT;

	/**
	 * The feature id for the '<em><b>Pricing Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__PRICING_DATE = LOAD_SLOT__PRICING_DATE;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__NOTES = LOAD_SLOT__NOTES;

	/**
	 * The feature id for the '<em><b>Divertible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__DIVERTIBLE = LOAD_SLOT__DIVERTIBLE;

	/**
	 * The feature id for the '<em><b>Shipping Days Restriction</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__SHIPPING_DAYS_RESTRICTION = LOAD_SLOT__SHIPPING_DAYS_RESTRICTION;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__ENTITY = LOAD_SLOT__ENTITY;

	/**
	 * The feature id for the '<em><b>Restricted Contracts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__RESTRICTED_CONTRACTS = LOAD_SLOT__RESTRICTED_CONTRACTS;

	/**
	 * The feature id for the '<em><b>Restricted Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__RESTRICTED_PORTS = LOAD_SLOT__RESTRICTED_PORTS;

	/**
	 * The feature id for the '<em><b>Restricted Lists Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__RESTRICTED_LISTS_ARE_PERMISSIVE = LOAD_SLOT__RESTRICTED_LISTS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Hedges</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__HEDGES = LOAD_SLOT__HEDGES;

	/**
	 * The feature id for the '<em><b>Allowed Vessels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__ALLOWED_VESSELS = LOAD_SLOT__ALLOWED_VESSELS;

	/**
	 * The feature id for the '<em><b>Cancellation Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__CANCELLATION_EXPRESSION = LOAD_SLOT__CANCELLATION_EXPRESSION;

	/**
	 * The feature id for the '<em><b>Override Restrictions</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__OVERRIDE_RESTRICTIONS = LOAD_SLOT__OVERRIDE_RESTRICTIONS;

	/**
	 * The feature id for the '<em><b>Nominated Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__NOMINATED_VESSEL = LOAD_SLOT__NOMINATED_VESSEL;

	/**
	 * The feature id for the '<em><b>Locked</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__LOCKED = LOAD_SLOT__LOCKED;

	/**
	 * The feature id for the '<em><b>Cargo CV</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__CARGO_CV = LOAD_SLOT__CARGO_CV;

	/**
	 * The feature id for the '<em><b>Arrive Cold</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__ARRIVE_COLD = LOAD_SLOT__ARRIVE_COLD;

	/**
	 * The feature id for the '<em><b>DES Purchase</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__DES_PURCHASE = LOAD_SLOT__DES_PURCHASE;

	/**
	 * The feature id for the '<em><b>Transfer From</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__TRANSFER_FROM = LOAD_SLOT__TRANSFER_FROM;

	/**
	 * The feature id for the '<em><b>Sales Delivery Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__SALES_DELIVERY_TYPE = LOAD_SLOT__SALES_DELIVERY_TYPE;

	/**
	 * The feature id for the '<em><b>Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__MARKET = LOAD_SLOT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Spot Load Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT_FEATURE_COUNT = LOAD_SLOT_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = LOAD_SLOT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = LOAD_SLOT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___ECONTAINER_OP = LOAD_SLOT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Time Zone</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_TIME_ZONE__EATTRIBUTE = LOAD_SLOT___GET_TIME_ZONE__EATTRIBUTE;

	/**
	 * The operation id for the '<em>Get Slot Or Port Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_PORT_DURATION = LOAD_SLOT___GET_SLOT_OR_PORT_DURATION;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Min Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_CONTRACT_MIN_QUANTITY = LOAD_SLOT___GET_SLOT_OR_CONTRACT_MIN_QUANTITY;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Max Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_CONTRACT_MAX_QUANTITY = LOAD_SLOT___GET_SLOT_OR_CONTRACT_MAX_QUANTITY;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Volume Limits Unit</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_CONTRACT_VOLUME_LIMITS_UNIT = LOAD_SLOT___GET_SLOT_OR_CONTRACT_VOLUME_LIMITS_UNIT;

	/**
	 * The operation id for the '<em>Get Window End With Slot Or Port Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME = LOAD_SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME;

	/**
	 * The operation id for the '<em>Get Window Start With Slot Or Port Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME = LOAD_SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME;

	/**
	 * The operation id for the '<em>Get Window End With Slot Or Port Time With Flex</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME_WITH_FLEX = LOAD_SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME_WITH_FLEX;

	/**
	 * The operation id for the '<em>Get Window Start With Slot Or Port Time With Flex</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME_WITH_FLEX = LOAD_SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME_WITH_FLEX;

	/**
	 * The operation id for the '<em>Get Slot Or Port Window Size</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE = LOAD_SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE;

	/**
	 * The operation id for the '<em>Get Slot Or Port Window Size Units</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE_UNITS = LOAD_SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE_UNITS;

	/**
	 * The operation id for the '<em>Get Window Size In Hours</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_WINDOW_SIZE_IN_HOURS = LOAD_SLOT___GET_WINDOW_SIZE_IN_HOURS;

	/**
	 * The operation id for the '<em>Get Slot Or Delegated Entity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_DELEGATED_ENTITY = LOAD_SLOT___GET_SLOT_OR_DELEGATED_ENTITY;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Restricted Contracts</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_CONTRACTS = LOAD_SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_CONTRACTS;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Restricted Ports</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_PORTS = LOAD_SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_PORTS;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Restricted Lists Are Permissive</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_LISTS_ARE_PERMISSIVE = LOAD_SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_LISTS_ARE_PERMISSIVE;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Cancellation Expression</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_CONTRACT_CANCELLATION_EXPRESSION = LOAD_SLOT___GET_SLOT_OR_CONTRACT_CANCELLATION_EXPRESSION;

	/**
	 * The operation id for the '<em>Get Slot Or Delegated Pricing Event</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_DELEGATED_PRICING_EVENT = LOAD_SLOT___GET_SLOT_OR_DELEGATED_PRICING_EVENT;

	/**
	 * The operation id for the '<em>Get Pricing Date As Date Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_PRICING_DATE_AS_DATE_TIME = LOAD_SLOT___GET_PRICING_DATE_AS_DATE_TIME;

	/**
	 * The operation id for the '<em>Get Slot Contract Params</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_CONTRACT_PARAMS = LOAD_SLOT___GET_SLOT_CONTRACT_PARAMS;

	/**
	 * The operation id for the '<em>Get Slot Or Delegated CV</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_DELEGATED_CV = LOAD_SLOT___GET_SLOT_OR_DELEGATED_CV;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Delivery Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_CONTRACT_DELIVERY_TYPE = LOAD_SLOT___GET_SLOT_OR_CONTRACT_DELIVERY_TYPE;

	/**
	 * The number of operations of the '<em>Spot Load Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT_OPERATION_COUNT = LOAD_SLOT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.SpotDischargeSlotImpl <em>Spot Discharge Slot</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.SpotDischargeSlotImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getSpotDischargeSlot()
	 * @generated
	 */
	int SPOT_DISCHARGE_SLOT = 7;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__EXTENSIONS = DISCHARGE_SLOT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__UUID = DISCHARGE_SLOT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__NAME = DISCHARGE_SLOT__NAME;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__CONTRACT = DISCHARGE_SLOT__CONTRACT;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__PORT = DISCHARGE_SLOT__PORT;

	/**
	 * The feature id for the '<em><b>Window Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__WINDOW_START = DISCHARGE_SLOT__WINDOW_START;

	/**
	 * The feature id for the '<em><b>Window Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__WINDOW_START_TIME = DISCHARGE_SLOT__WINDOW_START_TIME;

	/**
	 * The feature id for the '<em><b>Window Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__WINDOW_SIZE = DISCHARGE_SLOT__WINDOW_SIZE;

	/**
	 * The feature id for the '<em><b>Window Size Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__WINDOW_SIZE_UNITS = DISCHARGE_SLOT__WINDOW_SIZE_UNITS;

	/**
	 * The feature id for the '<em><b>Window Flex</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__WINDOW_FLEX = DISCHARGE_SLOT__WINDOW_FLEX;

	/**
	 * The feature id for the '<em><b>Window Flex Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__WINDOW_FLEX_UNITS = DISCHARGE_SLOT__WINDOW_FLEX_UNITS;

	/**
	 * The feature id for the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__DURATION = DISCHARGE_SLOT__DURATION;

	/**
	 * The feature id for the '<em><b>Volume Limits Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__VOLUME_LIMITS_UNIT = DISCHARGE_SLOT__VOLUME_LIMITS_UNIT;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__MIN_QUANTITY = DISCHARGE_SLOT__MIN_QUANTITY;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__MAX_QUANTITY = DISCHARGE_SLOT__MAX_QUANTITY;

	/**
	 * The feature id for the '<em><b>Optional</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__OPTIONAL = DISCHARGE_SLOT__OPTIONAL;

	/**
	 * The feature id for the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__PRICE_EXPRESSION = DISCHARGE_SLOT__PRICE_EXPRESSION;

	/**
	 * The feature id for the '<em><b>Cargo</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__CARGO = DISCHARGE_SLOT__CARGO;

	/**
	 * The feature id for the '<em><b>Pricing Event</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__PRICING_EVENT = DISCHARGE_SLOT__PRICING_EVENT;

	/**
	 * The feature id for the '<em><b>Pricing Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__PRICING_DATE = DISCHARGE_SLOT__PRICING_DATE;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__NOTES = DISCHARGE_SLOT__NOTES;

	/**
	 * The feature id for the '<em><b>Divertible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__DIVERTIBLE = DISCHARGE_SLOT__DIVERTIBLE;

	/**
	 * The feature id for the '<em><b>Shipping Days Restriction</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__SHIPPING_DAYS_RESTRICTION = DISCHARGE_SLOT__SHIPPING_DAYS_RESTRICTION;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__ENTITY = DISCHARGE_SLOT__ENTITY;

	/**
	 * The feature id for the '<em><b>Restricted Contracts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__RESTRICTED_CONTRACTS = DISCHARGE_SLOT__RESTRICTED_CONTRACTS;

	/**
	 * The feature id for the '<em><b>Restricted Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__RESTRICTED_PORTS = DISCHARGE_SLOT__RESTRICTED_PORTS;

	/**
	 * The feature id for the '<em><b>Restricted Lists Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__RESTRICTED_LISTS_ARE_PERMISSIVE = DISCHARGE_SLOT__RESTRICTED_LISTS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Hedges</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__HEDGES = DISCHARGE_SLOT__HEDGES;

	/**
	 * The feature id for the '<em><b>Allowed Vessels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__ALLOWED_VESSELS = DISCHARGE_SLOT__ALLOWED_VESSELS;

	/**
	 * The feature id for the '<em><b>Cancellation Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__CANCELLATION_EXPRESSION = DISCHARGE_SLOT__CANCELLATION_EXPRESSION;

	/**
	 * The feature id for the '<em><b>Override Restrictions</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__OVERRIDE_RESTRICTIONS = DISCHARGE_SLOT__OVERRIDE_RESTRICTIONS;

	/**
	 * The feature id for the '<em><b>Nominated Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__NOMINATED_VESSEL = DISCHARGE_SLOT__NOMINATED_VESSEL;

	/**
	 * The feature id for the '<em><b>Locked</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__LOCKED = DISCHARGE_SLOT__LOCKED;

	/**
	 * The feature id for the '<em><b>FOB Sale</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__FOB_SALE = DISCHARGE_SLOT__FOB_SALE;

	/**
	 * The feature id for the '<em><b>Purchase Delivery Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__PURCHASE_DELIVERY_TYPE = DISCHARGE_SLOT__PURCHASE_DELIVERY_TYPE;

	/**
	 * The feature id for the '<em><b>Transfer To</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__TRANSFER_TO = DISCHARGE_SLOT__TRANSFER_TO;

	/**
	 * The feature id for the '<em><b>Min Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__MIN_CV_VALUE = DISCHARGE_SLOT__MIN_CV_VALUE;

	/**
	 * The feature id for the '<em><b>Max Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__MAX_CV_VALUE = DISCHARGE_SLOT__MAX_CV_VALUE;

	/**
	 * The feature id for the '<em><b>Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__MARKET = DISCHARGE_SLOT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Spot Discharge Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT_FEATURE_COUNT = DISCHARGE_SLOT_FEATURE_COUNT + 1;


	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = DISCHARGE_SLOT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = DISCHARGE_SLOT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___ECONTAINER_OP = DISCHARGE_SLOT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Time Zone</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_TIME_ZONE__EATTRIBUTE = DISCHARGE_SLOT___GET_TIME_ZONE__EATTRIBUTE;

	/**
	 * The operation id for the '<em>Get Slot Or Port Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_PORT_DURATION = DISCHARGE_SLOT___GET_SLOT_OR_PORT_DURATION;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Min Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_MIN_QUANTITY = DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_MIN_QUANTITY;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Max Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_MAX_QUANTITY = DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_MAX_QUANTITY;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Volume Limits Unit</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_VOLUME_LIMITS_UNIT = DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_VOLUME_LIMITS_UNIT;

	/**
	 * The operation id for the '<em>Get Window End With Slot Or Port Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME = DISCHARGE_SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME;

	/**
	 * The operation id for the '<em>Get Window Start With Slot Or Port Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME = DISCHARGE_SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME;

	/**
	 * The operation id for the '<em>Get Window End With Slot Or Port Time With Flex</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME_WITH_FLEX = DISCHARGE_SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME_WITH_FLEX;

	/**
	 * The operation id for the '<em>Get Window Start With Slot Or Port Time With Flex</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME_WITH_FLEX = DISCHARGE_SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME_WITH_FLEX;

	/**
	 * The operation id for the '<em>Get Slot Or Port Window Size</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE = DISCHARGE_SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE;

	/**
	 * The operation id for the '<em>Get Slot Or Port Window Size Units</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE_UNITS = DISCHARGE_SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE_UNITS;

	/**
	 * The operation id for the '<em>Get Window Size In Hours</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_WINDOW_SIZE_IN_HOURS = DISCHARGE_SLOT___GET_WINDOW_SIZE_IN_HOURS;

	/**
	 * The operation id for the '<em>Get Slot Or Delegated Entity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_DELEGATED_ENTITY = DISCHARGE_SLOT___GET_SLOT_OR_DELEGATED_ENTITY;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Restricted Contracts</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_CONTRACTS = DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_CONTRACTS;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Restricted Ports</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_PORTS = DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_PORTS;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Restricted Lists Are Permissive</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_LISTS_ARE_PERMISSIVE = DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_LISTS_ARE_PERMISSIVE;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Cancellation Expression</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_CANCELLATION_EXPRESSION = DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_CANCELLATION_EXPRESSION;

	/**
	 * The operation id for the '<em>Get Slot Or Delegated Pricing Event</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_DELEGATED_PRICING_EVENT = DISCHARGE_SLOT___GET_SLOT_OR_DELEGATED_PRICING_EVENT;

	/**
	 * The operation id for the '<em>Get Pricing Date As Date Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_PRICING_DATE_AS_DATE_TIME = DISCHARGE_SLOT___GET_PRICING_DATE_AS_DATE_TIME;

	/**
	 * The operation id for the '<em>Get Slot Contract Params</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_CONTRACT_PARAMS = DISCHARGE_SLOT___GET_SLOT_CONTRACT_PARAMS;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Min Cv</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_MIN_CV = DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_MIN_CV;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Max Cv</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_MAX_CV = DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_MAX_CV;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Delivery Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_DELIVERY_TYPE = DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_DELIVERY_TYPE;

	/**
	 * The number of operations of the '<em>Spot Discharge Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT_OPERATION_COUNT = DISCHARGE_SLOT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.CargoGroupImpl <em>Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoGroupImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getCargoGroup()
	 * @generated
	 */
	int CARGO_GROUP = 8;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_GROUP__EXTENSIONS = MMXCorePackage.NAMED_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_GROUP__NAME = MMXCorePackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Cargoes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_GROUP__CARGOES = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_GROUP_FEATURE_COUNT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_GROUP___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.NAMED_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_GROUP___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.NAMED_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_GROUP___ECONTAINER_OP = MMXCorePackage.NAMED_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_GROUP_OPERATION_COUNT = MMXCorePackage.NAMED_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.VesselAvailabilityImpl <em>Vessel Availability</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.VesselAvailabilityImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getVesselAvailability()
	 * @generated
	 */
	int VESSEL_AVAILABILITY = 9;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__VESSEL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__ENTITY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Time Charter Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__TIME_CHARTER_RATE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Start At</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__START_AT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Start After</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__START_AFTER = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Start By</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__START_BY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>End At</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__END_AT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>End After</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__END_AFTER = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>End By</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__END_BY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Start Heel</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__START_HEEL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>End Heel</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__END_HEEL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 10;

	/**
	 * The number of structural features of the '<em>Vessel Availability</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 11;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY___ECONTAINER_OP = MMXCorePackage.UUID_OBJECT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Start By As Date Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY___GET_START_BY_AS_DATE_TIME = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Start After As Date Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY___GET_START_AFTER_AS_DATE_TIME = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 1;

	/**
	 * The operation id for the '<em>Get End By As Date Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY___GET_END_BY_AS_DATE_TIME = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 2;

	/**
	 * The operation id for the '<em>Get End After As Date Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY___GET_END_AFTER_AS_DATE_TIME = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 3;

	/**
	 * The number of operations of the '<em>Vessel Availability</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY_OPERATION_COUNT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 4;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.VesselEventImpl <em>Vessel Event</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.VesselEventImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getVesselEvent()
	 * @generated
	 */
	int VESSEL_EVENT = 10;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Sequence Hint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT__SEQUENCE_HINT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Vessel Assignment Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT__VESSEL_ASSIGNMENT_TYPE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Spot Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT__SPOT_INDEX = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Locked</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT__LOCKED = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Duration In Days</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT__DURATION_IN_DAYS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Allowed Vessels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT__ALLOWED_VESSELS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT__PORT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Start After</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT__START_AFTER = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Start By</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT__START_BY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 9;

	/**
	 * The number of structural features of the '<em>Vessel Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 10;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT___ECONTAINER_OP = MMXCorePackage.UUID_OBJECT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Time Zone</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT___GET_TIME_ZONE__EATTRIBUTE = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Start By As Date Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT___GET_START_BY_AS_DATE_TIME = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Start After As Date Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT___GET_START_AFTER_AS_DATE_TIME = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 2;

	/**
	 * The number of operations of the '<em>Vessel Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_OPERATION_COUNT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.MaintenanceEventImpl <em>Maintenance Event</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.MaintenanceEventImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getMaintenanceEvent()
	 * @generated
	 */
	int MAINTENANCE_EVENT = 11;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAINTENANCE_EVENT__EXTENSIONS = VESSEL_EVENT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAINTENANCE_EVENT__UUID = VESSEL_EVENT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAINTENANCE_EVENT__NAME = VESSEL_EVENT__NAME;

	/**
	 * The feature id for the '<em><b>Sequence Hint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAINTENANCE_EVENT__SEQUENCE_HINT = VESSEL_EVENT__SEQUENCE_HINT;

	/**
	 * The feature id for the '<em><b>Vessel Assignment Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAINTENANCE_EVENT__VESSEL_ASSIGNMENT_TYPE = VESSEL_EVENT__VESSEL_ASSIGNMENT_TYPE;

	/**
	 * The feature id for the '<em><b>Spot Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAINTENANCE_EVENT__SPOT_INDEX = VESSEL_EVENT__SPOT_INDEX;

	/**
	 * The feature id for the '<em><b>Locked</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAINTENANCE_EVENT__LOCKED = VESSEL_EVENT__LOCKED;

	/**
	 * The feature id for the '<em><b>Duration In Days</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAINTENANCE_EVENT__DURATION_IN_DAYS = VESSEL_EVENT__DURATION_IN_DAYS;

	/**
	 * The feature id for the '<em><b>Allowed Vessels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAINTENANCE_EVENT__ALLOWED_VESSELS = VESSEL_EVENT__ALLOWED_VESSELS;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAINTENANCE_EVENT__PORT = VESSEL_EVENT__PORT;

	/**
	 * The feature id for the '<em><b>Start After</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAINTENANCE_EVENT__START_AFTER = VESSEL_EVENT__START_AFTER;

	/**
	 * The feature id for the '<em><b>Start By</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAINTENANCE_EVENT__START_BY = VESSEL_EVENT__START_BY;

	/**
	 * The number of structural features of the '<em>Maintenance Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAINTENANCE_EVENT_FEATURE_COUNT = VESSEL_EVENT_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAINTENANCE_EVENT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = VESSEL_EVENT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAINTENANCE_EVENT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = VESSEL_EVENT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAINTENANCE_EVENT___ECONTAINER_OP = VESSEL_EVENT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Time Zone</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAINTENANCE_EVENT___GET_TIME_ZONE__EATTRIBUTE = VESSEL_EVENT___GET_TIME_ZONE__EATTRIBUTE;

	/**
	 * The operation id for the '<em>Get Start By As Date Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAINTENANCE_EVENT___GET_START_BY_AS_DATE_TIME = VESSEL_EVENT___GET_START_BY_AS_DATE_TIME;

	/**
	 * The operation id for the '<em>Get Start After As Date Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAINTENANCE_EVENT___GET_START_AFTER_AS_DATE_TIME = VESSEL_EVENT___GET_START_AFTER_AS_DATE_TIME;

	/**
	 * The number of operations of the '<em>Maintenance Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAINTENANCE_EVENT_OPERATION_COUNT = VESSEL_EVENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.DryDockEventImpl <em>Dry Dock Event</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.DryDockEventImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getDryDockEvent()
	 * @generated
	 */
	int DRY_DOCK_EVENT = 12;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRY_DOCK_EVENT__EXTENSIONS = VESSEL_EVENT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRY_DOCK_EVENT__UUID = VESSEL_EVENT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRY_DOCK_EVENT__NAME = VESSEL_EVENT__NAME;

	/**
	 * The feature id for the '<em><b>Sequence Hint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRY_DOCK_EVENT__SEQUENCE_HINT = VESSEL_EVENT__SEQUENCE_HINT;

	/**
	 * The feature id for the '<em><b>Vessel Assignment Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRY_DOCK_EVENT__VESSEL_ASSIGNMENT_TYPE = VESSEL_EVENT__VESSEL_ASSIGNMENT_TYPE;

	/**
	 * The feature id for the '<em><b>Spot Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRY_DOCK_EVENT__SPOT_INDEX = VESSEL_EVENT__SPOT_INDEX;

	/**
	 * The feature id for the '<em><b>Locked</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRY_DOCK_EVENT__LOCKED = VESSEL_EVENT__LOCKED;

	/**
	 * The feature id for the '<em><b>Duration In Days</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRY_DOCK_EVENT__DURATION_IN_DAYS = VESSEL_EVENT__DURATION_IN_DAYS;

	/**
	 * The feature id for the '<em><b>Allowed Vessels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRY_DOCK_EVENT__ALLOWED_VESSELS = VESSEL_EVENT__ALLOWED_VESSELS;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRY_DOCK_EVENT__PORT = VESSEL_EVENT__PORT;

	/**
	 * The feature id for the '<em><b>Start After</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRY_DOCK_EVENT__START_AFTER = VESSEL_EVENT__START_AFTER;

	/**
	 * The feature id for the '<em><b>Start By</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRY_DOCK_EVENT__START_BY = VESSEL_EVENT__START_BY;

	/**
	 * The number of structural features of the '<em>Dry Dock Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRY_DOCK_EVENT_FEATURE_COUNT = VESSEL_EVENT_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRY_DOCK_EVENT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = VESSEL_EVENT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRY_DOCK_EVENT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = VESSEL_EVENT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRY_DOCK_EVENT___ECONTAINER_OP = VESSEL_EVENT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Time Zone</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRY_DOCK_EVENT___GET_TIME_ZONE__EATTRIBUTE = VESSEL_EVENT___GET_TIME_ZONE__EATTRIBUTE;

	/**
	 * The operation id for the '<em>Get Start By As Date Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRY_DOCK_EVENT___GET_START_BY_AS_DATE_TIME = VESSEL_EVENT___GET_START_BY_AS_DATE_TIME;

	/**
	 * The operation id for the '<em>Get Start After As Date Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRY_DOCK_EVENT___GET_START_AFTER_AS_DATE_TIME = VESSEL_EVENT___GET_START_AFTER_AS_DATE_TIME;

	/**
	 * The number of operations of the '<em>Dry Dock Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRY_DOCK_EVENT_OPERATION_COUNT = VESSEL_EVENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.CharterOutEventImpl <em>Charter Out Event</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.CharterOutEventImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getCharterOutEvent()
	 * @generated
	 */
	int CHARTER_OUT_EVENT = 13;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT__EXTENSIONS = VESSEL_EVENT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT__UUID = VESSEL_EVENT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT__NAME = VESSEL_EVENT__NAME;

	/**
	 * The feature id for the '<em><b>Sequence Hint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT__SEQUENCE_HINT = VESSEL_EVENT__SEQUENCE_HINT;

	/**
	 * The feature id for the '<em><b>Vessel Assignment Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT__VESSEL_ASSIGNMENT_TYPE = VESSEL_EVENT__VESSEL_ASSIGNMENT_TYPE;

	/**
	 * The feature id for the '<em><b>Spot Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT__SPOT_INDEX = VESSEL_EVENT__SPOT_INDEX;

	/**
	 * The feature id for the '<em><b>Locked</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT__LOCKED = VESSEL_EVENT__LOCKED;

	/**
	 * The feature id for the '<em><b>Duration In Days</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT__DURATION_IN_DAYS = VESSEL_EVENT__DURATION_IN_DAYS;

	/**
	 * The feature id for the '<em><b>Allowed Vessels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT__ALLOWED_VESSELS = VESSEL_EVENT__ALLOWED_VESSELS;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT__PORT = VESSEL_EVENT__PORT;

	/**
	 * The feature id for the '<em><b>Start After</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT__START_AFTER = VESSEL_EVENT__START_AFTER;

	/**
	 * The feature id for the '<em><b>Start By</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT__START_BY = VESSEL_EVENT__START_BY;

	/**
	 * The feature id for the '<em><b>Relocate To</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT__RELOCATE_TO = VESSEL_EVENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Heel Options</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT__HEEL_OPTIONS = VESSEL_EVENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Repositioning Fee</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT__REPOSITIONING_FEE = VESSEL_EVENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Hire Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT__HIRE_RATE = VESSEL_EVENT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Charter Out Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT_FEATURE_COUNT = VESSEL_EVENT_FEATURE_COUNT + 4;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = VESSEL_EVENT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = VESSEL_EVENT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT___ECONTAINER_OP = VESSEL_EVENT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Time Zone</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT___GET_TIME_ZONE__EATTRIBUTE = VESSEL_EVENT___GET_TIME_ZONE__EATTRIBUTE;

	/**
	 * The operation id for the '<em>Get Start By As Date Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT___GET_START_BY_AS_DATE_TIME = VESSEL_EVENT___GET_START_BY_AS_DATE_TIME;

	/**
	 * The operation id for the '<em>Get Start After As Date Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT___GET_START_AFTER_AS_DATE_TIME = VESSEL_EVENT___GET_START_AFTER_AS_DATE_TIME;

	/**
	 * The operation id for the '<em>Get End Port</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT___GET_END_PORT = VESSEL_EVENT_OPERATION_COUNT + 0;

	/**
	 * The number of operations of the '<em>Charter Out Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT_OPERATION_COUNT = VESSEL_EVENT_OPERATION_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.AssignableElementImpl <em>Assignable Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.AssignableElementImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getAssignableElement()
	 * @generated
	 */
	int ASSIGNABLE_ELEMENT = 14;

	/**
	 * The feature id for the '<em><b>Sequence Hint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSIGNABLE_ELEMENT__SEQUENCE_HINT = 0;

	/**
	 * The feature id for the '<em><b>Vessel Assignment Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE = 1;

	/**
	 * The feature id for the '<em><b>Spot Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSIGNABLE_ELEMENT__SPOT_INDEX = 2;

	/**
	 * The feature id for the '<em><b>Locked</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSIGNABLE_ELEMENT__LOCKED = 3;

	/**
	 * The number of structural features of the '<em>Assignable Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSIGNABLE_ELEMENT_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Assignable Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSIGNABLE_ELEMENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.VesselTypeGroupImpl <em>Vessel Type Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.VesselTypeGroupImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getVesselTypeGroup()
	 * @generated
	 */
	int VESSEL_TYPE_GROUP = 15;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_TYPE_GROUP__EXTENSIONS = TypesPackage.AVESSEL_SET__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_TYPE_GROUP__UUID = TypesPackage.AVESSEL_SET__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_TYPE_GROUP__NAME = TypesPackage.AVESSEL_SET__NAME;

	/**
	 * The feature id for the '<em><b>Vessel Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_TYPE_GROUP__VESSEL_TYPE = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Vessel Type Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_TYPE_GROUP_FEATURE_COUNT = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_TYPE_GROUP___GET_UNSET_VALUE__ESTRUCTURALFEATURE = TypesPackage.AVESSEL_SET___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_TYPE_GROUP___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = TypesPackage.AVESSEL_SET___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_TYPE_GROUP___ECONTAINER_OP = TypesPackage.AVESSEL_SET___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Collect</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_TYPE_GROUP___COLLECT__ELIST = TypesPackage.AVESSEL_SET___COLLECT__ELIST;

	/**
	 * The number of operations of the '<em>Vessel Type Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_TYPE_GROUP_OPERATION_COUNT = TypesPackage.AVESSEL_SET_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.EndHeelOptionsImpl <em>End Heel Options</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.EndHeelOptionsImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getEndHeelOptions()
	 * @generated
	 */
	int END_HEEL_OPTIONS = 16;

	/**
	 * The feature id for the '<em><b>Target End Heel</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_HEEL_OPTIONS__TARGET_END_HEEL = 0;

	/**
	 * The number of structural features of the '<em>End Heel Options</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_HEEL_OPTIONS_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>End Heel Options</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_HEEL_OPTIONS_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.CargoType <em>Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.CargoType
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getCargoType()
	 * @generated
	 */
	int CARGO_TYPE = 17;


	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.VesselType <em>Vessel Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.VesselType
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getVesselType()
	 * @generated
	 */
	int VESSEL_TYPE = 18;


	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.Cargo <em>Cargo</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cargo</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Cargo
	 * @generated
	 */
	EClass getCargo();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Cargo#isAllowRewiring <em>Allow Rewiring</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Allow Rewiring</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Cargo#isAllowRewiring()
	 * @see #getCargo()
	 * @generated
	 */
	EAttribute getCargo_AllowRewiring();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.cargo.Cargo#getSlots <em>Slots</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Slots</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Cargo#getSlots()
	 * @see #getCargo()
	 * @generated
	 */
	EReference getCargo_Slots();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Cargo#getCargoType() <em>Get Cargo Type</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Cargo Type</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Cargo#getCargoType()
	 * @generated
	 */
	EOperation getCargo__GetCargoType();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Cargo#getSortedSlots() <em>Get Sorted Slots</em>}' operation.
	 * <!-- begin-user-doc -->
	 * @return the meta object for the '<em>Get Sorted Slots</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Cargo#getSortedSlots()
	 * @generated
	 */
	EOperation getCargo__GetSortedSlots();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Cargo#getLoadName() <em>Get Load Name</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Load Name</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Cargo#getLoadName()
	 * @generated
	 */
	EOperation getCargo__GetLoadName();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.Slot <em>Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Slot</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot
	 * @generated
	 */
	EClass getSlot();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.Slot#getContract <em>Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Contract</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getContract()
	 * @see #getSlot()
	 * @generated
	 */
	EReference getSlot_Contract();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowStart <em>Window Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Window Start</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getWindowStart()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_WindowStart();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowStartTime <em>Window Start Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Window Start Time</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getWindowStartTime()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_WindowStartTime();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowSize <em>Window Size</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Window Size</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getWindowSize()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_WindowSize();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowSizeUnits <em>Window Size Units</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Window Size Units</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getWindowSizeUnits()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_WindowSizeUnits();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowFlex <em>Window Flex</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Window Flex</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getWindowFlex()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_WindowFlex();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowFlexUnits <em>Window Flex Units</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Window Flex Units</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getWindowFlexUnits()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_WindowFlexUnits();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.Slot#getPort <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Port</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getPort()
	 * @see #getSlot()
	 * @generated
	 */
	EReference getSlot_Port();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.Slot#getContract <em>Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Contract</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getContract()
	 * @see #getSlot()
	 * @generated
	 */
//	EReference getSlot_Contract();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#getDuration <em>Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Duration</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getDuration()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_Duration();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#getVolumeLimitsUnit <em>Volume Limits Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volume Limits Unit</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getVolumeLimitsUnit()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_VolumeLimitsUnit();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#getMinQuantity <em>Min Quantity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Quantity</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getMinQuantity()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_MinQuantity();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#getMaxQuantity <em>Max Quantity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Quantity</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getMaxQuantity()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_MaxQuantity();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#isOptional <em>Optional</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Optional</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#isOptional()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_Optional();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#getPriceExpression <em>Price Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price Expression</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getPriceExpression()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_PriceExpression();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.Slot#getCargo <em>Cargo</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Cargo</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getCargo()
	 * @see #getSlot()
	 * @generated
	 */
	EReference getSlot_Cargo();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#getPricingEvent <em>Pricing Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Pricing Event</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getPricingEvent()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_PricingEvent();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#getPricingDate <em>Pricing Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Pricing Date</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getPricingDate()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_PricingDate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#getNotes <em>Notes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Notes</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getNotes()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_Notes();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#isDivertible <em>Divertible</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Divertible</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#isDivertible()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_Divertible();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#getShippingDaysRestriction <em>Shipping Days Restriction</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Shipping Days Restriction</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getShippingDaysRestriction()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_ShippingDaysRestriction();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.Slot#getEntity <em>Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Entity</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getEntity()
	 * @see #getSlot()
	 * @generated
	 */
	EReference getSlot_Entity();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.cargo.Slot#getRestrictedContracts <em>Restricted Contracts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Restricted Contracts</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getRestrictedContracts()
	 * @see #getSlot()
	 * @generated
	 */
	EReference getSlot_RestrictedContracts();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.cargo.Slot#getRestrictedPorts <em>Restricted Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Restricted Ports</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getRestrictedPorts()
	 * @see #getSlot()
	 * @generated
	 */
	EReference getSlot_RestrictedPorts();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#isRestrictedListsArePermissive <em>Restricted Lists Are Permissive</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Restricted Lists Are Permissive</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#isRestrictedListsArePermissive()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_RestrictedListsArePermissive();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#getHedges <em>Hedges</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Hedges</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getHedges()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_Hedges();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#getCancellationExpression <em>Cancellation Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cancellation Expression</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getCancellationExpression()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_CancellationExpression();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#isOverrideRestrictions <em>Override Restrictions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Override Restrictions</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#isOverrideRestrictions()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_OverrideRestrictions();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.Slot#getNominatedVessel <em>Nominated Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Nominated Vessel</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getNominatedVessel()
	 * @see #getSlot()
	 * @generated
	 */
	EReference getSlot_NominatedVessel();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#isLocked <em>Locked</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Locked</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#isLocked()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_Locked();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.cargo.Slot#getAllowedVessels <em>Allowed Vessels</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Allowed Vessels</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getAllowedVessels()
	 * @see #getSlot()
	 * @generated
	 */
	EReference getSlot_AllowedVessels();


	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrPortDuration() <em>Get Slot Or Port Duration</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Port Duration</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrPortDuration()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrPortDuration();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrContractMinQuantity() <em>Get Slot Or Contract Min Quantity</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Contract Min Quantity</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrContractMinQuantity()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrContractMinQuantity();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrContractMaxQuantity() <em>Get Slot Or Contract Max Quantity</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Contract Max Quantity</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrContractMaxQuantity()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrContractMaxQuantity();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrContractVolumeLimitsUnit() <em>Get Slot Or Contract Volume Limits Unit</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Contract Volume Limits Unit</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrContractVolumeLimitsUnit()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrContractVolumeLimitsUnit();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowEndWithSlotOrPortTime() <em>Get Window End With Slot Or Port Time</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Window End With Slot Or Port Time</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getWindowEndWithSlotOrPortTime()
	 * @generated
	 */
	EOperation getSlot__GetWindowEndWithSlotOrPortTime();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowStartWithSlotOrPortTime() <em>Get Window Start With Slot Or Port Time</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Window Start With Slot Or Port Time</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getWindowStartWithSlotOrPortTime()
	 * @generated
	 */
	EOperation getSlot__GetWindowStartWithSlotOrPortTime();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowEndWithSlotOrPortTimeWithFlex() <em>Get Window End With Slot Or Port Time With Flex</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Window End With Slot Or Port Time With Flex</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getWindowEndWithSlotOrPortTimeWithFlex()
	 * @generated
	 */
	EOperation getSlot__GetWindowEndWithSlotOrPortTimeWithFlex();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowStartWithSlotOrPortTimeWithFlex() <em>Get Window Start With Slot Or Port Time With Flex</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Window Start With Slot Or Port Time With Flex</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getWindowStartWithSlotOrPortTimeWithFlex()
	 * @generated
	 */
	EOperation getSlot__GetWindowStartWithSlotOrPortTimeWithFlex();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrPortWindowSize() <em>Get Slot Or Port Window Size</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Port Window Size</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrPortWindowSize()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrPortWindowSize();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrPortWindowSizeUnits() <em>Get Slot Or Port Window Size Units</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Port Window Size Units</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrPortWindowSizeUnits()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrPortWindowSizeUnits();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowSizeInHours() <em>Get Window Size In Hours</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Window Size In Hours</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getWindowSizeInHours()
	 * @generated
	 */
	EOperation getSlot__GetWindowSizeInHours();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegatedEntity() <em>Get Slot Or Delegated Entity</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Delegated Entity</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegatedEntity()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrDelegatedEntity();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrContractRestrictedContracts() <em>Get Slot Or Contract Restricted Contracts</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Contract Restricted Contracts</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrContractRestrictedContracts()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrContractRestrictedContracts();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrContractRestrictedPorts() <em>Get Slot Or Contract Restricted Ports</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Contract Restricted Ports</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrContractRestrictedPorts()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrContractRestrictedPorts();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrContractRestrictedListsArePermissive() <em>Get Slot Or Contract Restricted Lists Are Permissive</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Contract Restricted Lists Are Permissive</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrContractRestrictedListsArePermissive()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrContractRestrictedListsArePermissive();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrContractCancellationExpression() <em>Get Slot Or Contract Cancellation Expression</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Contract Cancellation Expression</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrContractCancellationExpression()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrContractCancellationExpression();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegatedPricingEvent() <em>Get Slot Or Delegated Pricing Event</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Delegated Pricing Event</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegatedPricingEvent()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrDelegatedPricingEvent();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getPricingDateAsDateTime() <em>Get Pricing Date As Date Time</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Pricing Date As Date Time</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getPricingDateAsDateTime()
	 * @generated
	 */
	EOperation getSlot__GetPricingDateAsDateTime();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotContractParams() <em>Get Slot Contract Params</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Contract Params</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotContractParams()
	 * @generated
	 */
	EOperation getSlot__GetSlotContractParams();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.LoadSlot <em>Load Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Load Slot</em>'.
	 * @see com.mmxlabs.models.lng.cargo.LoadSlot
	 * @generated
	 */
	EClass getLoadSlot();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.LoadSlot#getCargoCV <em>Cargo CV</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cargo CV</em>'.
	 * @see com.mmxlabs.models.lng.cargo.LoadSlot#getCargoCV()
	 * @see #getLoadSlot()
	 * @generated
	 */
	EAttribute getLoadSlot_CargoCV();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.LoadSlot#isArriveCold <em>Arrive Cold</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Arrive Cold</em>'.
	 * @see com.mmxlabs.models.lng.cargo.LoadSlot#isArriveCold()
	 * @see #getLoadSlot()
	 * @generated
	 */
	EAttribute getLoadSlot_ArriveCold();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.LoadSlot#isDESPurchase <em>DES Purchase</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>DES Purchase</em>'.
	 * @see com.mmxlabs.models.lng.cargo.LoadSlot#isDESPurchase()
	 * @see #getLoadSlot()
	 * @generated
	 */
	EAttribute getLoadSlot_DESPurchase();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.LoadSlot#getTransferFrom <em>Transfer From</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Transfer From</em>'.
	 * @see com.mmxlabs.models.lng.cargo.LoadSlot#getTransferFrom()
	 * @see #getLoadSlot()
	 * @generated
	 */
	EReference getLoadSlot_TransferFrom();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.LoadSlot#getSalesDeliveryType <em>Sales Delivery Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Sales Delivery Type</em>'.
	 * @see com.mmxlabs.models.lng.cargo.LoadSlot#getSalesDeliveryType()
	 * @see #getLoadSlot()
	 * @generated
	 */
	EAttribute getLoadSlot_SalesDeliveryType();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.LoadSlot#getSlotOrDelegatedCV() <em>Get Slot Or Delegated CV</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Delegated CV</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.LoadSlot#getSlotOrDelegatedCV()
	 * @generated
	 */
	EOperation getLoadSlot__GetSlotOrDelegatedCV();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.LoadSlot#getSlotOrContractDeliveryType() <em>Get Slot Or Contract Delivery Type</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Contract Delivery Type</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.LoadSlot#getSlotOrContractDeliveryType()
	 * @generated
	 */
	EOperation getLoadSlot__GetSlotOrContractDeliveryType();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.DischargeSlot <em>Discharge Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Discharge Slot</em>'.
	 * @see com.mmxlabs.models.lng.cargo.DischargeSlot
	 * @generated
	 */
	EClass getDischargeSlot();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#isFOBSale <em>FOB Sale</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>FOB Sale</em>'.
	 * @see com.mmxlabs.models.lng.cargo.DischargeSlot#isFOBSale()
	 * @see #getDischargeSlot()
	 * @generated
	 */
	EAttribute getDischargeSlot_FOBSale();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getPurchaseDeliveryType <em>Purchase Delivery Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Purchase Delivery Type</em>'.
	 * @see com.mmxlabs.models.lng.cargo.DischargeSlot#getPurchaseDeliveryType()
	 * @see #getDischargeSlot()
	 * @generated
	 */
	EAttribute getDischargeSlot_PurchaseDeliveryType();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getTransferTo <em>Transfer To</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Transfer To</em>'.
	 * @see com.mmxlabs.models.lng.cargo.DischargeSlot#getTransferTo()
	 * @see #getDischargeSlot()
	 * @generated
	 */
	EReference getDischargeSlot_TransferTo();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getMinCvValue <em>Min Cv Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Cv Value</em>'.
	 * @see com.mmxlabs.models.lng.cargo.DischargeSlot#getMinCvValue()
	 * @see #getDischargeSlot()
	 * @generated
	 */
	EAttribute getDischargeSlot_MinCvValue();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getMaxCvValue <em>Max Cv Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Cv Value</em>'.
	 * @see com.mmxlabs.models.lng.cargo.DischargeSlot#getMaxCvValue()
	 * @see #getDischargeSlot()
	 * @generated
	 */
	EAttribute getDischargeSlot_MaxCvValue();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getSlotOrContractMinCv() <em>Get Slot Or Contract Min Cv</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Contract Min Cv</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.DischargeSlot#getSlotOrContractMinCv()
	 * @generated
	 */
	EOperation getDischargeSlot__GetSlotOrContractMinCv();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getSlotOrContractMaxCv() <em>Get Slot Or Contract Max Cv</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Contract Max Cv</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.DischargeSlot#getSlotOrContractMaxCv()
	 * @generated
	 */
	EOperation getDischargeSlot__GetSlotOrContractMaxCv();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getSlotOrContractDeliveryType() <em>Get Slot Or Contract Delivery Type</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Contract Delivery Type</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.DischargeSlot#getSlotOrContractDeliveryType()
	 * @generated
	 */
	EOperation getDischargeSlot__GetSlotOrContractDeliveryType();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.CargoModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CargoModel
	 * @generated
	 */
	EClass getCargoModel();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.cargo.CargoModel#getLoadSlots <em>Load Slots</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Load Slots</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CargoModel#getLoadSlots()
	 * @see #getCargoModel()
	 * @generated
	 */
	EReference getCargoModel_LoadSlots();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.cargo.CargoModel#getDischargeSlots <em>Discharge Slots</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Discharge Slots</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CargoModel#getDischargeSlots()
	 * @see #getCargoModel()
	 * @generated
	 */
	EReference getCargoModel_DischargeSlots();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.cargo.CargoModel#getCargoes <em>Cargoes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Cargoes</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CargoModel#getCargoes()
	 * @see #getCargoModel()
	 * @generated
	 */
	EReference getCargoModel_Cargoes();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.cargo.CargoModel#getCargoGroups <em>Cargo Groups</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Cargo Groups</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CargoModel#getCargoGroups()
	 * @see #getCargoModel()
	 * @generated
	 */
	EReference getCargoModel_CargoGroups();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.cargo.CargoModel#getVesselAvailabilities <em>Vessel Availabilities</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Vessel Availabilities</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CargoModel#getVesselAvailabilities()
	 * @see #getCargoModel()
	 * @generated
	 */
	EReference getCargoModel_VesselAvailabilities();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.cargo.CargoModel#getVesselEvents <em>Vessel Events</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Vessel Events</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CargoModel#getVesselEvents()
	 * @see #getCargoModel()
	 * @generated
	 */
	EReference getCargoModel_VesselEvents();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.cargo.CargoModel#getVesselTypeGroups <em>Vessel Type Groups</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Vessel Type Groups</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CargoModel#getVesselTypeGroups()
	 * @see #getCargoModel()
	 * @generated
	 */
	EReference getCargoModel_VesselTypeGroups();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.SpotSlot <em>Spot Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Spot Slot</em>'.
	 * @see com.mmxlabs.models.lng.cargo.SpotSlot
	 * @generated
	 */
	EClass getSpotSlot();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.SpotSlot#getMarket <em>Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Market</em>'.
	 * @see com.mmxlabs.models.lng.cargo.SpotSlot#getMarket()
	 * @see #getSpotSlot()
	 * @generated
	 */
	EReference getSpotSlot_Market();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.SpotLoadSlot <em>Spot Load Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Spot Load Slot</em>'.
	 * @see com.mmxlabs.models.lng.cargo.SpotLoadSlot
	 * @generated
	 */
	EClass getSpotLoadSlot();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.SpotDischargeSlot <em>Spot Discharge Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Spot Discharge Slot</em>'.
	 * @see com.mmxlabs.models.lng.cargo.SpotDischargeSlot
	 * @generated
	 */
	EClass getSpotDischargeSlot();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.CargoGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Group</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CargoGroup
	 * @generated
	 */
	EClass getCargoGroup();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.cargo.CargoGroup#getCargoes <em>Cargoes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Cargoes</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CargoGroup#getCargoes()
	 * @see #getCargoGroup()
	 * @generated
	 */
	EReference getCargoGroup_Cargoes();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.VesselAvailability <em>Vessel Availability</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vessel Availability</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselAvailability
	 * @generated
	 */
	EClass getVesselAvailability();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getVessel <em>Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselAvailability#getVessel()
	 * @see #getVesselAvailability()
	 * @generated
	 */
	EReference getVesselAvailability_Vessel();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getTimeCharterRate <em>Time Charter Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Time Charter Rate</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselAvailability#getTimeCharterRate()
	 * @see #getVesselAvailability()
	 * @generated
	 */
	EAttribute getVesselAvailability_TimeCharterRate();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getStartAt <em>Start At</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Start At</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselAvailability#getStartAt()
	 * @see #getVesselAvailability()
	 * @generated
	 */
	EReference getVesselAvailability_StartAt();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getStartAfter <em>Start After</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start After</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselAvailability#getStartAfter()
	 * @see #getVesselAvailability()
	 * @generated
	 */
	EAttribute getVesselAvailability_StartAfter();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getStartBy <em>Start By</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start By</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselAvailability#getStartBy()
	 * @see #getVesselAvailability()
	 * @generated
	 */
	EAttribute getVesselAvailability_StartBy();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getEndAt <em>End At</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>End At</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselAvailability#getEndAt()
	 * @see #getVesselAvailability()
	 * @generated
	 */
	EReference getVesselAvailability_EndAt();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getEndAfter <em>End After</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End After</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselAvailability#getEndAfter()
	 * @see #getVesselAvailability()
	 * @generated
	 */
	EAttribute getVesselAvailability_EndAfter();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getEndBy <em>End By</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End By</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselAvailability#getEndBy()
	 * @see #getVesselAvailability()
	 * @generated
	 */
	EAttribute getVesselAvailability_EndBy();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getStartHeel <em>Start Heel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Start Heel</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselAvailability#getStartHeel()
	 * @see #getVesselAvailability()
	 * @generated
	 */
	EReference getVesselAvailability_StartHeel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getEndHeel <em>End Heel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>End Heel</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselAvailability#getEndHeel()
	 * @see #getVesselAvailability()
	 * @generated
	 */
	EReference getVesselAvailability_EndHeel();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getStartByAsDateTime() <em>Get Start By As Date Time</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Start By As Date Time</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.VesselAvailability#getStartByAsDateTime()
	 * @generated
	 */
	EOperation getVesselAvailability__GetStartByAsDateTime();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getStartAfterAsDateTime() <em>Get Start After As Date Time</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Start After As Date Time</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.VesselAvailability#getStartAfterAsDateTime()
	 * @generated
	 */
	EOperation getVesselAvailability__GetStartAfterAsDateTime();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getEndByAsDateTime() <em>Get End By As Date Time</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get End By As Date Time</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.VesselAvailability#getEndByAsDateTime()
	 * @generated
	 */
	EOperation getVesselAvailability__GetEndByAsDateTime();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getEndAfterAsDateTime() <em>Get End After As Date Time</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get End After As Date Time</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.VesselAvailability#getEndAfterAsDateTime()
	 * @generated
	 */
	EOperation getVesselAvailability__GetEndAfterAsDateTime();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getEntity <em>Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Entity</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselAvailability#getEntity()
	 * @see #getVesselAvailability()
	 * @generated
	 */
	EReference getVesselAvailability_Entity();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.VesselEvent <em>Vessel Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vessel Event</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselEvent
	 * @generated
	 */
	EClass getVesselEvent();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.VesselEvent#getDurationInDays <em>Duration In Days</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Duration In Days</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselEvent#getDurationInDays()
	 * @see #getVesselEvent()
	 * @generated
	 */
	EAttribute getVesselEvent_DurationInDays();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.cargo.VesselEvent#getAllowedVessels <em>Allowed Vessels</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Allowed Vessels</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselEvent#getAllowedVessels()
	 * @see #getVesselEvent()
	 * @generated
	 */
	EReference getVesselEvent_AllowedVessels();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.VesselEvent#getPort <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Port</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselEvent#getPort()
	 * @see #getVesselEvent()
	 * @generated
	 */
	EReference getVesselEvent_Port();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.VesselEvent#getStartAfter <em>Start After</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start After</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselEvent#getStartAfter()
	 * @see #getVesselEvent()
	 * @generated
	 */
	EAttribute getVesselEvent_StartAfter();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.VesselEvent#getStartBy <em>Start By</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start By</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselEvent#getStartBy()
	 * @see #getVesselEvent()
	 * @generated
	 */
	EAttribute getVesselEvent_StartBy();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.VesselEvent#getStartByAsDateTime() <em>Get Start By As Date Time</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Start By As Date Time</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.VesselEvent#getStartByAsDateTime()
	 * @generated
	 */
	EOperation getVesselEvent__GetStartByAsDateTime();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.VesselEvent#getStartAfterAsDateTime() <em>Get Start After As Date Time</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Start After As Date Time</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.VesselEvent#getStartAfterAsDateTime()
	 * @generated
	 */
	EOperation getVesselEvent__GetStartAfterAsDateTime();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.MaintenanceEvent <em>Maintenance Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Maintenance Event</em>'.
	 * @see com.mmxlabs.models.lng.cargo.MaintenanceEvent
	 * @generated
	 */
	EClass getMaintenanceEvent();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.DryDockEvent <em>Dry Dock Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Dry Dock Event</em>'.
	 * @see com.mmxlabs.models.lng.cargo.DryDockEvent
	 * @generated
	 */
	EClass getDryDockEvent();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.CharterOutEvent <em>Charter Out Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Charter Out Event</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CharterOutEvent
	 * @generated
	 */
	EClass getCharterOutEvent();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.CharterOutEvent#getRelocateTo <em>Relocate To</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Relocate To</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CharterOutEvent#getRelocateTo()
	 * @see #getCharterOutEvent()
	 * @generated
	 */
	EReference getCharterOutEvent_RelocateTo();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.cargo.CharterOutEvent#getHeelOptions <em>Heel Options</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Heel Options</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CharterOutEvent#getHeelOptions()
	 * @see #getCharterOutEvent()
	 * @generated
	 */
	EReference getCharterOutEvent_HeelOptions();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.CharterOutEvent#getRepositioningFee <em>Repositioning Fee</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Repositioning Fee</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CharterOutEvent#getRepositioningFee()
	 * @see #getCharterOutEvent()
	 * @generated
	 */
	EAttribute getCharterOutEvent_RepositioningFee();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.CharterOutEvent#getHireRate <em>Hire Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Hire Rate</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CharterOutEvent#getHireRate()
	 * @see #getCharterOutEvent()
	 * @generated
	 */
	EAttribute getCharterOutEvent_HireRate();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.CharterOutEvent#getEndPort() <em>Get End Port</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get End Port</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.CharterOutEvent#getEndPort()
	 * @generated
	 */
	EOperation getCharterOutEvent__GetEndPort();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.AssignableElement <em>Assignable Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Assignable Element</em>'.
	 * @see com.mmxlabs.models.lng.cargo.AssignableElement
	 * @generated
	 */
	EClass getAssignableElement();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.AssignableElement#getSpotIndex <em>Spot Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Spot Index</em>'.
	 * @see com.mmxlabs.models.lng.cargo.AssignableElement#getSpotIndex()
	 * @see #getAssignableElement()
	 * @generated
	 */
	EAttribute getAssignableElement_SpotIndex();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.AssignableElement#getSequenceHint <em>Sequence Hint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Sequence Hint</em>'.
	 * @see com.mmxlabs.models.lng.cargo.AssignableElement#getSequenceHint()
	 * @see #getAssignableElement()
	 * @generated
	 */
	EAttribute getAssignableElement_SequenceHint();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.AssignableElement#isLocked <em>Locked</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Locked</em>'.
	 * @see com.mmxlabs.models.lng.cargo.AssignableElement#isLocked()
	 * @see #getAssignableElement()
	 * @generated
	 */
	EAttribute getAssignableElement_Locked();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.AssignableElement#getVesselAssignmentType <em>Vessel Assignment Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel Assignment Type</em>'.
	 * @see com.mmxlabs.models.lng.cargo.AssignableElement#getVesselAssignmentType()
	 * @see #getAssignableElement()
	 * @generated
	 */
	EReference getAssignableElement_VesselAssignmentType();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.VesselTypeGroup <em>Vessel Type Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vessel Type Group</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselTypeGroup
	 * @generated
	 */
	EClass getVesselTypeGroup();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.VesselTypeGroup#getVesselType <em>Vessel Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Vessel Type</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselTypeGroup#getVesselType()
	 * @see #getVesselTypeGroup()
	 * @generated
	 */
	EAttribute getVesselTypeGroup_VesselType();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.EndHeelOptions <em>End Heel Options</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>End Heel Options</em>'.
	 * @see com.mmxlabs.models.lng.cargo.EndHeelOptions
	 * @generated
	 */
	EClass getEndHeelOptions();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.EndHeelOptions#getTargetEndHeel <em>Target End Heel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Target End Heel</em>'.
	 * @see com.mmxlabs.models.lng.cargo.EndHeelOptions#getTargetEndHeel()
	 * @see #getEndHeelOptions()
	 * @generated
	 */
	EAttribute getEndHeelOptions_TargetEndHeel();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.cargo.CargoType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Type</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CargoType
	 * @generated
	 */
	EEnum getCargoType();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.cargo.VesselType <em>Vessel Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Vessel Type</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselType
	 * @generated
	 */
	EEnum getVesselType();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	CargoFactory getCargoFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.CargoImpl <em>Cargo</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getCargo()
		 * @generated
		 */
		EClass CARGO = eINSTANCE.getCargo();

		/**
		 * The meta object literal for the '<em><b>Allow Rewiring</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO__ALLOW_REWIRING = eINSTANCE.getCargo_AllowRewiring();

		/**
		 * The meta object literal for the '<em><b>Slots</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO__SLOTS = eINSTANCE.getCargo_Slots();

		/**
		 * The meta object literal for the '<em><b>Get Cargo Type</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CARGO___GET_CARGO_TYPE = eINSTANCE.getCargo__GetCargoType();

		/**
		 * The meta object literal for the '<em><b>Get Sorted Slots</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CARGO___GET_SORTED_SLOTS = eINSTANCE.getCargo__GetSortedSlots();

		/**
		 * The meta object literal for the '<em><b>Get Load Name</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CARGO___GET_LOAD_NAME = eINSTANCE.getCargo__GetLoadName();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl <em>Slot</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.SlotImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getSlot()
		 * @generated
		 */
		EClass SLOT = eINSTANCE.getSlot();

		/**
		 * The meta object literal for the '<em><b>Window Start</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__WINDOW_START = eINSTANCE.getSlot_WindowStart();

		/**
		 * The meta object literal for the '<em><b>Window Start Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__WINDOW_START_TIME = eINSTANCE.getSlot_WindowStartTime();

		/**
		 * The meta object literal for the '<em><b>Window Size</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__WINDOW_SIZE = eINSTANCE.getSlot_WindowSize();

		/**
		 * The meta object literal for the '<em><b>Window Size Units</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__WINDOW_SIZE_UNITS = eINSTANCE.getSlot_WindowSizeUnits();

		/**
		 * The meta object literal for the '<em><b>Window Flex</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__WINDOW_FLEX = eINSTANCE.getSlot_WindowFlex();

		/**
		 * The meta object literal for the '<em><b>Window Flex Units</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__WINDOW_FLEX_UNITS = eINSTANCE.getSlot_WindowFlexUnits();

		/**
		 * The meta object literal for the '<em><b>Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT__PORT = eINSTANCE.getSlot_Port();

		/**
		 * The meta object literal for the '<em><b>Contract</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT__CONTRACT = eINSTANCE.getSlot_Contract();

		/**
		 * The meta object literal for the '<em><b>Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__DURATION = eINSTANCE.getSlot_Duration();

		/**
		 * The meta object literal for the '<em><b>Volume Limits Unit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__VOLUME_LIMITS_UNIT = eINSTANCE.getSlot_VolumeLimitsUnit();

		/**
		 * The meta object literal for the '<em><b>Min Quantity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__MIN_QUANTITY = eINSTANCE.getSlot_MinQuantity();

		/**
		 * The meta object literal for the '<em><b>Max Quantity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__MAX_QUANTITY = eINSTANCE.getSlot_MaxQuantity();

		/**
		 * The meta object literal for the '<em><b>Optional</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__OPTIONAL = eINSTANCE.getSlot_Optional();

		/**
		 * The meta object literal for the '<em><b>Price Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__PRICE_EXPRESSION = eINSTANCE.getSlot_PriceExpression();

		/**
		 * The meta object literal for the '<em><b>Cargo</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT__CARGO = eINSTANCE.getSlot_Cargo();

		/**
		 * The meta object literal for the '<em><b>Pricing Event</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__PRICING_EVENT = eINSTANCE.getSlot_PricingEvent();

		/**
		 * The meta object literal for the '<em><b>Pricing Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__PRICING_DATE = eINSTANCE.getSlot_PricingDate();

		/**
		 * The meta object literal for the '<em><b>Notes</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__NOTES = eINSTANCE.getSlot_Notes();

		/**
		 * The meta object literal for the '<em><b>Divertible</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__DIVERTIBLE = eINSTANCE.getSlot_Divertible();

		/**
		 * The meta object literal for the '<em><b>Shipping Days Restriction</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__SHIPPING_DAYS_RESTRICTION = eINSTANCE.getSlot_ShippingDaysRestriction();

		/**
		 * The meta object literal for the '<em><b>Entity</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT__ENTITY = eINSTANCE.getSlot_Entity();

		/**
		 * The meta object literal for the '<em><b>Restricted Contracts</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT__RESTRICTED_CONTRACTS = eINSTANCE.getSlot_RestrictedContracts();

		/**
		 * The meta object literal for the '<em><b>Restricted Ports</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT__RESTRICTED_PORTS = eINSTANCE.getSlot_RestrictedPorts();

		/**
		 * The meta object literal for the '<em><b>Restricted Lists Are Permissive</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__RESTRICTED_LISTS_ARE_PERMISSIVE = eINSTANCE.getSlot_RestrictedListsArePermissive();

		/**
		 * The meta object literal for the '<em><b>Hedges</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__HEDGES = eINSTANCE.getSlot_Hedges();

		/**
		 * The meta object literal for the '<em><b>Allowed Vessels</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT__ALLOWED_VESSELS = eINSTANCE.getSlot_AllowedVessels();

		/**
		 * The meta object literal for the '<em><b>Cancellation Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__CANCELLATION_EXPRESSION = eINSTANCE.getSlot_CancellationExpression();

		/**
		 * The meta object literal for the '<em><b>Override Restrictions</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__OVERRIDE_RESTRICTIONS = eINSTANCE.getSlot_OverrideRestrictions();

		/**
		 * The meta object literal for the '<em><b>Nominated Vessel</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT__NOMINATED_VESSEL = eINSTANCE.getSlot_NominatedVessel();

		/**
		 * The meta object literal for the '<em><b>Locked</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__LOCKED = eINSTANCE.getSlot_Locked();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Port Duration</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_PORT_DURATION = eINSTANCE.getSlot__GetSlotOrPortDuration();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Contract Min Quantity</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_CONTRACT_MIN_QUANTITY = eINSTANCE.getSlot__GetSlotOrContractMinQuantity();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Contract Max Quantity</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_CONTRACT_MAX_QUANTITY = eINSTANCE.getSlot__GetSlotOrContractMaxQuantity();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Contract Volume Limits Unit</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_CONTRACT_VOLUME_LIMITS_UNIT = eINSTANCE.getSlot__GetSlotOrContractVolumeLimitsUnit();

		/**
		 * The meta object literal for the '<em><b>Get Window End With Slot Or Port Time</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME = eINSTANCE.getSlot__GetWindowEndWithSlotOrPortTime();

		/**
		 * The meta object literal for the '<em><b>Get Window Start With Slot Or Port Time</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME = eINSTANCE.getSlot__GetWindowStartWithSlotOrPortTime();

		/**
		 * The meta object literal for the '<em><b>Get Window End With Slot Or Port Time With Flex</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME_WITH_FLEX = eINSTANCE.getSlot__GetWindowEndWithSlotOrPortTimeWithFlex();

		/**
		 * The meta object literal for the '<em><b>Get Window Start With Slot Or Port Time With Flex</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME_WITH_FLEX = eINSTANCE.getSlot__GetWindowStartWithSlotOrPortTimeWithFlex();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Port Window Size</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE = eINSTANCE.getSlot__GetSlotOrPortWindowSize();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Port Window Size Units</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE_UNITS = eINSTANCE.getSlot__GetSlotOrPortWindowSizeUnits();

		/**
		 * The meta object literal for the '<em><b>Get Window Size In Hours</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_WINDOW_SIZE_IN_HOURS = eINSTANCE.getSlot__GetWindowSizeInHours();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Delegated Entity</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_DELEGATED_ENTITY = eINSTANCE.getSlot__GetSlotOrDelegatedEntity();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Contract Restricted Contracts</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_CONTRACTS = eINSTANCE.getSlot__GetSlotOrContractRestrictedContracts();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Contract Restricted Ports</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_PORTS = eINSTANCE.getSlot__GetSlotOrContractRestrictedPorts();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Contract Restricted Lists Are Permissive</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_CONTRACT_RESTRICTED_LISTS_ARE_PERMISSIVE = eINSTANCE.getSlot__GetSlotOrContractRestrictedListsArePermissive();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Contract Cancellation Expression</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_CONTRACT_CANCELLATION_EXPRESSION = eINSTANCE.getSlot__GetSlotOrContractCancellationExpression();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Delegated Pricing Event</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_DELEGATED_PRICING_EVENT = eINSTANCE.getSlot__GetSlotOrDelegatedPricingEvent();

		/**
		 * The meta object literal for the '<em><b>Get Pricing Date As Date Time</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_PRICING_DATE_AS_DATE_TIME = eINSTANCE.getSlot__GetPricingDateAsDateTime();

		/**
		 * The meta object literal for the '<em><b>Get Slot Contract Params</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_CONTRACT_PARAMS = eINSTANCE.getSlot__GetSlotContractParams();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.LoadSlotImpl <em>Load Slot</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.LoadSlotImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getLoadSlot()
		 * @generated
		 */
		EClass LOAD_SLOT = eINSTANCE.getLoadSlot();

		/**
		 * The meta object literal for the '<em><b>Cargo CV</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOAD_SLOT__CARGO_CV = eINSTANCE.getLoadSlot_CargoCV();

		/**
		 * The meta object literal for the '<em><b>Arrive Cold</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOAD_SLOT__ARRIVE_COLD = eINSTANCE.getLoadSlot_ArriveCold();

		/**
		 * The meta object literal for the '<em><b>DES Purchase</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOAD_SLOT__DES_PURCHASE = eINSTANCE.getLoadSlot_DESPurchase();

		/**
		 * The meta object literal for the '<em><b>Transfer From</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LOAD_SLOT__TRANSFER_FROM = eINSTANCE.getLoadSlot_TransferFrom();

		/**
		 * The meta object literal for the '<em><b>Sales Delivery Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOAD_SLOT__SALES_DELIVERY_TYPE = eINSTANCE.getLoadSlot_SalesDeliveryType();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Delegated CV</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation LOAD_SLOT___GET_SLOT_OR_DELEGATED_CV = eINSTANCE.getLoadSlot__GetSlotOrDelegatedCV();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Contract Delivery Type</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation LOAD_SLOT___GET_SLOT_OR_CONTRACT_DELIVERY_TYPE = eINSTANCE.getLoadSlot__GetSlotOrContractDeliveryType();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.DischargeSlotImpl <em>Discharge Slot</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.DischargeSlotImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getDischargeSlot()
		 * @generated
		 */
		EClass DISCHARGE_SLOT = eINSTANCE.getDischargeSlot();

		/**
		 * The meta object literal for the '<em><b>FOB Sale</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DISCHARGE_SLOT__FOB_SALE = eINSTANCE.getDischargeSlot_FOBSale();

		/**
		 * The meta object literal for the '<em><b>Purchase Delivery Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DISCHARGE_SLOT__PURCHASE_DELIVERY_TYPE = eINSTANCE.getDischargeSlot_PurchaseDeliveryType();

		/**
		 * The meta object literal for the '<em><b>Transfer To</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DISCHARGE_SLOT__TRANSFER_TO = eINSTANCE.getDischargeSlot_TransferTo();

		/**
		 * The meta object literal for the '<em><b>Min Cv Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DISCHARGE_SLOT__MIN_CV_VALUE = eINSTANCE.getDischargeSlot_MinCvValue();

		/**
		 * The meta object literal for the '<em><b>Max Cv Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DISCHARGE_SLOT__MAX_CV_VALUE = eINSTANCE.getDischargeSlot_MaxCvValue();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Contract Min Cv</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_MIN_CV = eINSTANCE.getDischargeSlot__GetSlotOrContractMinCv();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Contract Max Cv</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_MAX_CV = eINSTANCE.getDischargeSlot__GetSlotOrContractMaxCv();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Contract Delivery Type</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_DELIVERY_TYPE = eINSTANCE.getDischargeSlot__GetSlotOrContractDeliveryType();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.CargoModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoModelImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getCargoModel()
		 * @generated
		 */
		EClass CARGO_MODEL = eINSTANCE.getCargoModel();

		/**
		 * The meta object literal for the '<em><b>Load Slots</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_MODEL__LOAD_SLOTS = eINSTANCE.getCargoModel_LoadSlots();

		/**
		 * The meta object literal for the '<em><b>Discharge Slots</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_MODEL__DISCHARGE_SLOTS = eINSTANCE.getCargoModel_DischargeSlots();

		/**
		 * The meta object literal for the '<em><b>Cargoes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_MODEL__CARGOES = eINSTANCE.getCargoModel_Cargoes();

		/**
		 * The meta object literal for the '<em><b>Cargo Groups</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_MODEL__CARGO_GROUPS = eINSTANCE.getCargoModel_CargoGroups();

		/**
		 * The meta object literal for the '<em><b>Vessel Availabilities</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_MODEL__VESSEL_AVAILABILITIES = eINSTANCE.getCargoModel_VesselAvailabilities();

		/**
		 * The meta object literal for the '<em><b>Vessel Events</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_MODEL__VESSEL_EVENTS = eINSTANCE.getCargoModel_VesselEvents();

		/**
		 * The meta object literal for the '<em><b>Vessel Type Groups</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_MODEL__VESSEL_TYPE_GROUPS = eINSTANCE.getCargoModel_VesselTypeGroups();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.SpotSlot <em>Spot Slot</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.SpotSlot
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getSpotSlot()
		 * @generated
		 */
		EClass SPOT_SLOT = eINSTANCE.getSpotSlot();

		/**
		 * The meta object literal for the '<em><b>Market</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SPOT_SLOT__MARKET = eINSTANCE.getSpotSlot_Market();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.SpotLoadSlotImpl <em>Spot Load Slot</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.SpotLoadSlotImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getSpotLoadSlot()
		 * @generated
		 */
		EClass SPOT_LOAD_SLOT = eINSTANCE.getSpotLoadSlot();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.SpotDischargeSlotImpl <em>Spot Discharge Slot</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.SpotDischargeSlotImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getSpotDischargeSlot()
		 * @generated
		 */
		EClass SPOT_DISCHARGE_SLOT = eINSTANCE.getSpotDischargeSlot();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.CargoGroupImpl <em>Group</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoGroupImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getCargoGroup()
		 * @generated
		 */
		EClass CARGO_GROUP = eINSTANCE.getCargoGroup();

		/**
		 * The meta object literal for the '<em><b>Cargoes</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_GROUP__CARGOES = eINSTANCE.getCargoGroup_Cargoes();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.VesselAvailabilityImpl <em>Vessel Availability</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.VesselAvailabilityImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getVesselAvailability()
		 * @generated
		 */
		EClass VESSEL_AVAILABILITY = eINSTANCE.getVesselAvailability();

		/**
		 * The meta object literal for the '<em><b>Vessel</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_AVAILABILITY__VESSEL = eINSTANCE.getVesselAvailability_Vessel();

		/**
		 * The meta object literal for the '<em><b>Time Charter Rate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_AVAILABILITY__TIME_CHARTER_RATE = eINSTANCE.getVesselAvailability_TimeCharterRate();

		/**
		 * The meta object literal for the '<em><b>Start At</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_AVAILABILITY__START_AT = eINSTANCE.getVesselAvailability_StartAt();

		/**
		 * The meta object literal for the '<em><b>Start After</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_AVAILABILITY__START_AFTER = eINSTANCE.getVesselAvailability_StartAfter();

		/**
		 * The meta object literal for the '<em><b>Start By</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_AVAILABILITY__START_BY = eINSTANCE.getVesselAvailability_StartBy();

		/**
		 * The meta object literal for the '<em><b>End At</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_AVAILABILITY__END_AT = eINSTANCE.getVesselAvailability_EndAt();

		/**
		 * The meta object literal for the '<em><b>End After</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_AVAILABILITY__END_AFTER = eINSTANCE.getVesselAvailability_EndAfter();

		/**
		 * The meta object literal for the '<em><b>End By</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_AVAILABILITY__END_BY = eINSTANCE.getVesselAvailability_EndBy();

		/**
		 * The meta object literal for the '<em><b>Start Heel</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_AVAILABILITY__START_HEEL = eINSTANCE.getVesselAvailability_StartHeel();

		/**
		 * The meta object literal for the '<em><b>End Heel</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_AVAILABILITY__END_HEEL = eINSTANCE.getVesselAvailability_EndHeel();

		/**
		 * The meta object literal for the '<em><b>Get Start By As Date Time</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation VESSEL_AVAILABILITY___GET_START_BY_AS_DATE_TIME = eINSTANCE.getVesselAvailability__GetStartByAsDateTime();

		/**
		 * The meta object literal for the '<em><b>Get Start After As Date Time</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation VESSEL_AVAILABILITY___GET_START_AFTER_AS_DATE_TIME = eINSTANCE.getVesselAvailability__GetStartAfterAsDateTime();

		/**
		 * The meta object literal for the '<em><b>Get End By As Date Time</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation VESSEL_AVAILABILITY___GET_END_BY_AS_DATE_TIME = eINSTANCE.getVesselAvailability__GetEndByAsDateTime();

		/**
		 * The meta object literal for the '<em><b>Get End After As Date Time</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation VESSEL_AVAILABILITY___GET_END_AFTER_AS_DATE_TIME = eINSTANCE.getVesselAvailability__GetEndAfterAsDateTime();

		/**
		 * The meta object literal for the '<em><b>Entity</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_AVAILABILITY__ENTITY = eINSTANCE.getVesselAvailability_Entity();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.VesselEventImpl <em>Vessel Event</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.VesselEventImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getVesselEvent()
		 * @generated
		 */
		EClass VESSEL_EVENT = eINSTANCE.getVesselEvent();

		/**
		 * The meta object literal for the '<em><b>Duration In Days</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_EVENT__DURATION_IN_DAYS = eINSTANCE.getVesselEvent_DurationInDays();

		/**
		 * The meta object literal for the '<em><b>Allowed Vessels</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_EVENT__ALLOWED_VESSELS = eINSTANCE.getVesselEvent_AllowedVessels();

		/**
		 * The meta object literal for the '<em><b>Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_EVENT__PORT = eINSTANCE.getVesselEvent_Port();

		/**
		 * The meta object literal for the '<em><b>Start After</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_EVENT__START_AFTER = eINSTANCE.getVesselEvent_StartAfter();

		/**
		 * The meta object literal for the '<em><b>Start By</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_EVENT__START_BY = eINSTANCE.getVesselEvent_StartBy();

		/**
		 * The meta object literal for the '<em><b>Get Start By As Date Time</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation VESSEL_EVENT___GET_START_BY_AS_DATE_TIME = eINSTANCE.getVesselEvent__GetStartByAsDateTime();

		/**
		 * The meta object literal for the '<em><b>Get Start After As Date Time</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation VESSEL_EVENT___GET_START_AFTER_AS_DATE_TIME = eINSTANCE.getVesselEvent__GetStartAfterAsDateTime();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.MaintenanceEventImpl <em>Maintenance Event</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.MaintenanceEventImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getMaintenanceEvent()
		 * @generated
		 */
		EClass MAINTENANCE_EVENT = eINSTANCE.getMaintenanceEvent();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.DryDockEventImpl <em>Dry Dock Event</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.DryDockEventImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getDryDockEvent()
		 * @generated
		 */
		EClass DRY_DOCK_EVENT = eINSTANCE.getDryDockEvent();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.CharterOutEventImpl <em>Charter Out Event</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.CharterOutEventImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getCharterOutEvent()
		 * @generated
		 */
		EClass CHARTER_OUT_EVENT = eINSTANCE.getCharterOutEvent();

		/**
		 * The meta object literal for the '<em><b>Relocate To</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHARTER_OUT_EVENT__RELOCATE_TO = eINSTANCE.getCharterOutEvent_RelocateTo();

		/**
		 * The meta object literal for the '<em><b>Heel Options</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHARTER_OUT_EVENT__HEEL_OPTIONS = eINSTANCE.getCharterOutEvent_HeelOptions();

		/**
		 * The meta object literal for the '<em><b>Repositioning Fee</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHARTER_OUT_EVENT__REPOSITIONING_FEE = eINSTANCE.getCharterOutEvent_RepositioningFee();

		/**
		 * The meta object literal for the '<em><b>Hire Rate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHARTER_OUT_EVENT__HIRE_RATE = eINSTANCE.getCharterOutEvent_HireRate();

		/**
		 * The meta object literal for the '<em><b>Get End Port</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CHARTER_OUT_EVENT___GET_END_PORT = eINSTANCE.getCharterOutEvent__GetEndPort();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.AssignableElementImpl <em>Assignable Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.AssignableElementImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getAssignableElement()
		 * @generated
		 */
		EClass ASSIGNABLE_ELEMENT = eINSTANCE.getAssignableElement();

		/**
		 * The meta object literal for the '<em><b>Spot Index</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ASSIGNABLE_ELEMENT__SPOT_INDEX = eINSTANCE.getAssignableElement_SpotIndex();

		/**
		 * The meta object literal for the '<em><b>Sequence Hint</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ASSIGNABLE_ELEMENT__SEQUENCE_HINT = eINSTANCE.getAssignableElement_SequenceHint();

		/**
		 * The meta object literal for the '<em><b>Locked</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ASSIGNABLE_ELEMENT__LOCKED = eINSTANCE.getAssignableElement_Locked();

		/**
		 * The meta object literal for the '<em><b>Vessel Assignment Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE = eINSTANCE.getAssignableElement_VesselAssignmentType();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.VesselTypeGroupImpl <em>Vessel Type Group</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.VesselTypeGroupImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getVesselTypeGroup()
		 * @generated
		 */
		EClass VESSEL_TYPE_GROUP = eINSTANCE.getVesselTypeGroup();

		/**
		 * The meta object literal for the '<em><b>Vessel Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_TYPE_GROUP__VESSEL_TYPE = eINSTANCE.getVesselTypeGroup_VesselType();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.EndHeelOptionsImpl <em>End Heel Options</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.EndHeelOptionsImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getEndHeelOptions()
		 * @generated
		 */
		EClass END_HEEL_OPTIONS = eINSTANCE.getEndHeelOptions();

		/**
		 * The meta object literal for the '<em><b>Target End Heel</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute END_HEEL_OPTIONS__TARGET_END_HEEL = eINSTANCE.getEndHeelOptions_TargetEndHeel();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.CargoType <em>Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.CargoType
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getCargoType()
		 * @generated
		 */
		EEnum CARGO_TYPE = eINSTANCE.getCargoType();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.VesselType <em>Vessel Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.VesselType
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getVesselType()
		 * @generated
		 */
		EEnum VESSEL_TYPE = eINSTANCE.getVesselType();

	}

} //CargoPackage
