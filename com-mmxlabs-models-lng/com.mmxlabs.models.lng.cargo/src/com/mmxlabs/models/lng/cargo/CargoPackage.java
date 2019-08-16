/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
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
	 * The feature id for the '<em><b>Inventory Models</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL__INVENTORY_MODELS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Canal Bookings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL__CANAL_BOOKINGS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Charter In Market Overrides</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL__CHARTER_IN_MARKET_OVERRIDES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Paper Deals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL__PAPER_DEALS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Deal Sets</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL__DEAL_SETS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 11;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 12;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.SpotSlot <em>Spot Slot</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.SpotSlot
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getSpotSlot()
	 * @generated
	 */
	int SPOT_SLOT = 5;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.SpotDischargeSlotImpl <em>Spot Discharge Slot</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.SpotDischargeSlotImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getSpotDischargeSlot()
	 * @generated
	 */
	int SPOT_DISCHARGE_SLOT = 7;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.VesselAvailabilityImpl <em>Vessel Availability</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.VesselAvailabilityImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getVesselAvailability()
	 * @generated
	 */
	int VESSEL_AVAILABILITY = 9;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.MaintenanceEventImpl <em>Maintenance Event</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.MaintenanceEventImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getMaintenanceEvent()
	 * @generated
	 */
	int MAINTENANCE_EVENT = 11;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.CharterOutEventImpl <em>Charter Out Event</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.CharterOutEventImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getCharterOutEvent()
	 * @generated
	 */
	int CHARTER_OUT_EVENT = 13;

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
	 * The feature id for the '<em><b>Counterparty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__COUNTERPARTY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Cn</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__CN = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__PORT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Window Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__WINDOW_START = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Window Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__WINDOW_START_TIME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Window Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__WINDOW_SIZE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Window Size Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__WINDOW_SIZE_UNITS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Window Flex</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__WINDOW_FLEX = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Window Flex Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__WINDOW_FLEX_UNITS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__DURATION = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>Volume Limits Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__VOLUME_LIMITS_UNIT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 12;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__MIN_QUANTITY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 13;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__MAX_QUANTITY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 14;

	/**
	 * The feature id for the '<em><b>Operational Tolerance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__OPERATIONAL_TOLERANCE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 15;

	/**
	 * The feature id for the '<em><b>Full Cargo Lot</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__FULL_CARGO_LOT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 16;

	/**
	 * The feature id for the '<em><b>Optional</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__OPTIONAL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 17;

	/**
	 * The feature id for the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__PRICE_EXPRESSION = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 18;

	/**
	 * The feature id for the '<em><b>Cargo</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__CARGO = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 19;

	/**
	 * The feature id for the '<em><b>Pricing Event</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__PRICING_EVENT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 20;

	/**
	 * The feature id for the '<em><b>Pricing Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__PRICING_DATE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 21;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__NOTES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 22;

	/**
	 * The feature id for the '<em><b>Shipping Days Restriction</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__SHIPPING_DAYS_RESTRICTION = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 23;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__ENTITY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 24;

	/**
	 * The feature id for the '<em><b>Restricted Contracts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__RESTRICTED_CONTRACTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 25;

	/**
	 * The feature id for the '<em><b>Restricted Contracts Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 26;

	/**
	 * The feature id for the '<em><b>Restricted Contracts Override</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__RESTRICTED_CONTRACTS_OVERRIDE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 27;

	/**
	 * The feature id for the '<em><b>Restricted Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__RESTRICTED_PORTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 28;

	/**
	 * The feature id for the '<em><b>Restricted Ports Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__RESTRICTED_PORTS_ARE_PERMISSIVE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 29;

	/**
	 * The feature id for the '<em><b>Restricted Ports Override</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__RESTRICTED_PORTS_OVERRIDE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 30;

	/**
	 * The feature id for the '<em><b>Restricted Vessels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__RESTRICTED_VESSELS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 31;

	/**
	 * The feature id for the '<em><b>Restricted Vessels Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__RESTRICTED_VESSELS_ARE_PERMISSIVE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 32;

	/**
	 * The feature id for the '<em><b>Restricted Vessels Override</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__RESTRICTED_VESSELS_OVERRIDE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 33;

	/**
	 * The feature id for the '<em><b>Restricted Slots</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__RESTRICTED_SLOTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 34;

	/**
	 * The feature id for the '<em><b>Restricted Slots Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__RESTRICTED_SLOTS_ARE_PERMISSIVE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 35;

	/**
	 * The feature id for the '<em><b>Hedges</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__HEDGES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 36;

	/**
	 * The feature id for the '<em><b>Misc Costs</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__MISC_COSTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 37;

	/**
	 * The feature id for the '<em><b>Cancellation Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__CANCELLATION_EXPRESSION = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 38;

	/**
	 * The feature id for the '<em><b>Nominated Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__NOMINATED_VESSEL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 39;

	/**
	 * The feature id for the '<em><b>Locked</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__LOCKED = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 40;

	/**
	 * The feature id for the '<em><b>Cancelled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__CANCELLED = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 41;

	/**
	 * The feature id for the '<em><b>Window Counter Party</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__WINDOW_COUNTER_PARTY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 42;

	/**
	 * The number of structural features of the '<em>Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 43;

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
	 * The operation id for the '<em>Get Slot Or Delegate Min Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_DELEGATE_MIN_QUANTITY = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Max Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_DELEGATE_MAX_QUANTITY = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 2;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Operational Tolerance</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_DELEGATE_OPERATIONAL_TOLERANCE = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 3;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Volume Limits Unit</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_DELEGATE_VOLUME_LIMITS_UNIT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 4;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Entity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_DELEGATE_ENTITY = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 5;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Cancellation Expression</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_DELEGATE_CANCELLATION_EXPRESSION = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 6;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Pricing Event</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_DELEGATE_PRICING_EVENT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 7;

	/**
	 * The operation id for the '<em>Get Pricing Date As Date Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_PRICING_DATE_AS_DATE_TIME = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 8;

	/**
	 * The operation id for the '<em>Get Slot Contract Params</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_CONTRACT_PARAMS = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 9;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Counterparty</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_DELEGATE_COUNTERPARTY = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 10;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate CN</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_DELEGATE_CN = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 11;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Shipping Days Restriction</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_DELEGATE_SHIPPING_DAYS_RESTRICTION = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 12;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Full Cargo Lot</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_DELEGATE_FULL_CARGO_LOT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 13;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Contract Restrictions Are Permissive</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_DELEGATE_CONTRACT_RESTRICTIONS_ARE_PERMISSIVE = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 14;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Port Restrictions Are Permissive</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_DELEGATE_PORT_RESTRICTIONS_ARE_PERMISSIVE = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 15;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Vessel Restrictions Are Permissive</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_DELEGATE_VESSEL_RESTRICTIONS_ARE_PERMISSIVE = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 16;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Contract Restrictions</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_DELEGATE_CONTRACT_RESTRICTIONS = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 17;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Port Restrictions</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_DELEGATE_PORT_RESTRICTIONS = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 18;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Vessel Restrictions</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_DELEGATE_VESSEL_RESTRICTIONS = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 19;

	/**
	 * The operation id for the '<em>Get Scheduling Time Window</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SCHEDULING_TIME_WINDOW = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 20;

	/**
	 * The operation id for the '<em>Get Days Buffer</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_DAYS_BUFFER = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 21;

	/**
	 * The number of operations of the '<em>Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_OPERATION_COUNT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 22;

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
	 * The feature id for the '<em><b>Counterparty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__COUNTERPARTY = SLOT__COUNTERPARTY;

	/**
	 * The feature id for the '<em><b>Cn</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__CN = SLOT__CN;

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
	 * The feature id for the '<em><b>Operational Tolerance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__OPERATIONAL_TOLERANCE = SLOT__OPERATIONAL_TOLERANCE;

	/**
	 * The feature id for the '<em><b>Full Cargo Lot</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__FULL_CARGO_LOT = SLOT__FULL_CARGO_LOT;

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
	 * The feature id for the '<em><b>Restricted Contracts Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE = SLOT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Restricted Contracts Override</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__RESTRICTED_CONTRACTS_OVERRIDE = SLOT__RESTRICTED_CONTRACTS_OVERRIDE;

	/**
	 * The feature id for the '<em><b>Restricted Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__RESTRICTED_PORTS = SLOT__RESTRICTED_PORTS;

	/**
	 * The feature id for the '<em><b>Restricted Ports Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__RESTRICTED_PORTS_ARE_PERMISSIVE = SLOT__RESTRICTED_PORTS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Restricted Ports Override</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__RESTRICTED_PORTS_OVERRIDE = SLOT__RESTRICTED_PORTS_OVERRIDE;

	/**
	 * The feature id for the '<em><b>Restricted Vessels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__RESTRICTED_VESSELS = SLOT__RESTRICTED_VESSELS;

	/**
	 * The feature id for the '<em><b>Restricted Vessels Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__RESTRICTED_VESSELS_ARE_PERMISSIVE = SLOT__RESTRICTED_VESSELS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Restricted Vessels Override</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__RESTRICTED_VESSELS_OVERRIDE = SLOT__RESTRICTED_VESSELS_OVERRIDE;

	/**
	 * The feature id for the '<em><b>Restricted Slots</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__RESTRICTED_SLOTS = SLOT__RESTRICTED_SLOTS;

	/**
	 * The feature id for the '<em><b>Restricted Slots Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__RESTRICTED_SLOTS_ARE_PERMISSIVE = SLOT__RESTRICTED_SLOTS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Hedges</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__HEDGES = SLOT__HEDGES;

	/**
	 * The feature id for the '<em><b>Misc Costs</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__MISC_COSTS = SLOT__MISC_COSTS;

	/**
	 * The feature id for the '<em><b>Cancellation Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__CANCELLATION_EXPRESSION = SLOT__CANCELLATION_EXPRESSION;

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
	 * The feature id for the '<em><b>Cancelled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__CANCELLED = SLOT__CANCELLED;

	/**
	 * The feature id for the '<em><b>Window Counter Party</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__WINDOW_COUNTER_PARTY = SLOT__WINDOW_COUNTER_PARTY;

	/**
	 * The feature id for the '<em><b>Cargo CV</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__CARGO_CV = SLOT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Schedule Purge</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__SCHEDULE_PURGE = SLOT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Arrive Cold</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__ARRIVE_COLD = SLOT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>DES Purchase</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__DES_PURCHASE = SLOT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Transfer From</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__TRANSFER_FROM = SLOT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Sales Delivery Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__SALES_DELIVERY_TYPE = SLOT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Des Purchase Deal Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__DES_PURCHASE_DEAL_TYPE = SLOT_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Load Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT_FEATURE_COUNT = SLOT_FEATURE_COUNT + 7;

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
	 * The operation id for the '<em>Get Slot Or Delegate Min Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_DELEGATE_MIN_QUANTITY = SLOT___GET_SLOT_OR_DELEGATE_MIN_QUANTITY;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Max Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_DELEGATE_MAX_QUANTITY = SLOT___GET_SLOT_OR_DELEGATE_MAX_QUANTITY;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Operational Tolerance</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_DELEGATE_OPERATIONAL_TOLERANCE = SLOT___GET_SLOT_OR_DELEGATE_OPERATIONAL_TOLERANCE;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Volume Limits Unit</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_DELEGATE_VOLUME_LIMITS_UNIT = SLOT___GET_SLOT_OR_DELEGATE_VOLUME_LIMITS_UNIT;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Entity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_DELEGATE_ENTITY = SLOT___GET_SLOT_OR_DELEGATE_ENTITY;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Cancellation Expression</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_DELEGATE_CANCELLATION_EXPRESSION = SLOT___GET_SLOT_OR_DELEGATE_CANCELLATION_EXPRESSION;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Pricing Event</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_DELEGATE_PRICING_EVENT = SLOT___GET_SLOT_OR_DELEGATE_PRICING_EVENT;

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
	 * The operation id for the '<em>Get Slot Or Delegate Counterparty</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_DELEGATE_COUNTERPARTY = SLOT___GET_SLOT_OR_DELEGATE_COUNTERPARTY;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate CN</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_DELEGATE_CN = SLOT___GET_SLOT_OR_DELEGATE_CN;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Shipping Days Restriction</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_DELEGATE_SHIPPING_DAYS_RESTRICTION = SLOT___GET_SLOT_OR_DELEGATE_SHIPPING_DAYS_RESTRICTION;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Full Cargo Lot</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_DELEGATE_FULL_CARGO_LOT = SLOT___GET_SLOT_OR_DELEGATE_FULL_CARGO_LOT;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Contract Restrictions Are Permissive</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_DELEGATE_CONTRACT_RESTRICTIONS_ARE_PERMISSIVE = SLOT___GET_SLOT_OR_DELEGATE_CONTRACT_RESTRICTIONS_ARE_PERMISSIVE;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Port Restrictions Are Permissive</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_DELEGATE_PORT_RESTRICTIONS_ARE_PERMISSIVE = SLOT___GET_SLOT_OR_DELEGATE_PORT_RESTRICTIONS_ARE_PERMISSIVE;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Vessel Restrictions Are Permissive</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_DELEGATE_VESSEL_RESTRICTIONS_ARE_PERMISSIVE = SLOT___GET_SLOT_OR_DELEGATE_VESSEL_RESTRICTIONS_ARE_PERMISSIVE;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Contract Restrictions</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_DELEGATE_CONTRACT_RESTRICTIONS = SLOT___GET_SLOT_OR_DELEGATE_CONTRACT_RESTRICTIONS;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Port Restrictions</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_DELEGATE_PORT_RESTRICTIONS = SLOT___GET_SLOT_OR_DELEGATE_PORT_RESTRICTIONS;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Vessel Restrictions</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_DELEGATE_VESSEL_RESTRICTIONS = SLOT___GET_SLOT_OR_DELEGATE_VESSEL_RESTRICTIONS;

	/**
	 * The operation id for the '<em>Get Scheduling Time Window</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SCHEDULING_TIME_WINDOW = SLOT___GET_SCHEDULING_TIME_WINDOW;

	/**
	 * The operation id for the '<em>Get Days Buffer</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_DAYS_BUFFER = SLOT___GET_DAYS_BUFFER;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate CV</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_DELEGATE_CV = SLOT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Delivery Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_DELEGATE_DELIVERY_TYPE = SLOT_OPERATION_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate DES Purchase Deal Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_DELEGATE_DES_PURCHASE_DEAL_TYPE = SLOT_OPERATION_COUNT + 2;

	/**
	 * The number of operations of the '<em>Load Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT_OPERATION_COUNT = SLOT_OPERATION_COUNT + 3;

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
	 * The feature id for the '<em><b>Counterparty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__COUNTERPARTY = SLOT__COUNTERPARTY;

	/**
	 * The feature id for the '<em><b>Cn</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__CN = SLOT__CN;

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
	 * The feature id for the '<em><b>Operational Tolerance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__OPERATIONAL_TOLERANCE = SLOT__OPERATIONAL_TOLERANCE;

	/**
	 * The feature id for the '<em><b>Full Cargo Lot</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__FULL_CARGO_LOT = SLOT__FULL_CARGO_LOT;

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
	 * The feature id for the '<em><b>Restricted Contracts Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE = SLOT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Restricted Contracts Override</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__RESTRICTED_CONTRACTS_OVERRIDE = SLOT__RESTRICTED_CONTRACTS_OVERRIDE;

	/**
	 * The feature id for the '<em><b>Restricted Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__RESTRICTED_PORTS = SLOT__RESTRICTED_PORTS;

	/**
	 * The feature id for the '<em><b>Restricted Ports Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__RESTRICTED_PORTS_ARE_PERMISSIVE = SLOT__RESTRICTED_PORTS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Restricted Ports Override</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__RESTRICTED_PORTS_OVERRIDE = SLOT__RESTRICTED_PORTS_OVERRIDE;

	/**
	 * The feature id for the '<em><b>Restricted Vessels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__RESTRICTED_VESSELS = SLOT__RESTRICTED_VESSELS;

	/**
	 * The feature id for the '<em><b>Restricted Vessels Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__RESTRICTED_VESSELS_ARE_PERMISSIVE = SLOT__RESTRICTED_VESSELS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Restricted Vessels Override</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__RESTRICTED_VESSELS_OVERRIDE = SLOT__RESTRICTED_VESSELS_OVERRIDE;

	/**
	 * The feature id for the '<em><b>Restricted Slots</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__RESTRICTED_SLOTS = SLOT__RESTRICTED_SLOTS;

	/**
	 * The feature id for the '<em><b>Restricted Slots Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__RESTRICTED_SLOTS_ARE_PERMISSIVE = SLOT__RESTRICTED_SLOTS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Hedges</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__HEDGES = SLOT__HEDGES;

	/**
	 * The feature id for the '<em><b>Misc Costs</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__MISC_COSTS = SLOT__MISC_COSTS;

	/**
	 * The feature id for the '<em><b>Cancellation Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__CANCELLATION_EXPRESSION = SLOT__CANCELLATION_EXPRESSION;

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
	 * The feature id for the '<em><b>Cancelled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__CANCELLED = SLOT__CANCELLED;

	/**
	 * The feature id for the '<em><b>Window Counter Party</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__WINDOW_COUNTER_PARTY = SLOT__WINDOW_COUNTER_PARTY;

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
	 * The feature id for the '<em><b>Fob Sale Deal Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__FOB_SALE_DEAL_TYPE = SLOT_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Discharge Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT_FEATURE_COUNT = SLOT_FEATURE_COUNT + 6;

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
	 * The operation id for the '<em>Get Slot Or Delegate Min Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_MIN_QUANTITY = SLOT___GET_SLOT_OR_DELEGATE_MIN_QUANTITY;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Max Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_MAX_QUANTITY = SLOT___GET_SLOT_OR_DELEGATE_MAX_QUANTITY;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Operational Tolerance</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_OPERATIONAL_TOLERANCE = SLOT___GET_SLOT_OR_DELEGATE_OPERATIONAL_TOLERANCE;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Volume Limits Unit</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_VOLUME_LIMITS_UNIT = SLOT___GET_SLOT_OR_DELEGATE_VOLUME_LIMITS_UNIT;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Entity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_ENTITY = SLOT___GET_SLOT_OR_DELEGATE_ENTITY;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Cancellation Expression</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_CANCELLATION_EXPRESSION = SLOT___GET_SLOT_OR_DELEGATE_CANCELLATION_EXPRESSION;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Pricing Event</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_PRICING_EVENT = SLOT___GET_SLOT_OR_DELEGATE_PRICING_EVENT;

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
	 * The operation id for the '<em>Get Slot Or Delegate Counterparty</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_COUNTERPARTY = SLOT___GET_SLOT_OR_DELEGATE_COUNTERPARTY;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate CN</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_CN = SLOT___GET_SLOT_OR_DELEGATE_CN;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Shipping Days Restriction</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_SHIPPING_DAYS_RESTRICTION = SLOT___GET_SLOT_OR_DELEGATE_SHIPPING_DAYS_RESTRICTION;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Full Cargo Lot</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_FULL_CARGO_LOT = SLOT___GET_SLOT_OR_DELEGATE_FULL_CARGO_LOT;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Contract Restrictions Are Permissive</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_CONTRACT_RESTRICTIONS_ARE_PERMISSIVE = SLOT___GET_SLOT_OR_DELEGATE_CONTRACT_RESTRICTIONS_ARE_PERMISSIVE;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Port Restrictions Are Permissive</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_PORT_RESTRICTIONS_ARE_PERMISSIVE = SLOT___GET_SLOT_OR_DELEGATE_PORT_RESTRICTIONS_ARE_PERMISSIVE;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Vessel Restrictions Are Permissive</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_VESSEL_RESTRICTIONS_ARE_PERMISSIVE = SLOT___GET_SLOT_OR_DELEGATE_VESSEL_RESTRICTIONS_ARE_PERMISSIVE;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Contract Restrictions</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_CONTRACT_RESTRICTIONS = SLOT___GET_SLOT_OR_DELEGATE_CONTRACT_RESTRICTIONS;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Port Restrictions</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_PORT_RESTRICTIONS = SLOT___GET_SLOT_OR_DELEGATE_PORT_RESTRICTIONS;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Vessel Restrictions</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_VESSEL_RESTRICTIONS = SLOT___GET_SLOT_OR_DELEGATE_VESSEL_RESTRICTIONS;

	/**
	 * The operation id for the '<em>Get Scheduling Time Window</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SCHEDULING_TIME_WINDOW = SLOT___GET_SCHEDULING_TIME_WINDOW;

	/**
	 * The operation id for the '<em>Get Days Buffer</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_DAYS_BUFFER = SLOT___GET_DAYS_BUFFER;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Min Cv</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_MIN_CV = SLOT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Max Cv</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_MAX_CV = SLOT_OPERATION_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Delivery Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_DELIVERY_TYPE = SLOT_OPERATION_COUNT + 2;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate FOB Sale Deal Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_FOB_SALE_DEAL_TYPE = SLOT_OPERATION_COUNT + 3;

	/**
	 * The number of operations of the '<em>Discharge Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT_OPERATION_COUNT = SLOT_OPERATION_COUNT + 4;

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
	 * The feature id for the '<em><b>Counterparty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__COUNTERPARTY = LOAD_SLOT__COUNTERPARTY;

	/**
	 * The feature id for the '<em><b>Cn</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__CN = LOAD_SLOT__CN;

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
	 * The feature id for the '<em><b>Operational Tolerance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__OPERATIONAL_TOLERANCE = LOAD_SLOT__OPERATIONAL_TOLERANCE;

	/**
	 * The feature id for the '<em><b>Full Cargo Lot</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__FULL_CARGO_LOT = LOAD_SLOT__FULL_CARGO_LOT;

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
	 * The feature id for the '<em><b>Restricted Contracts Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE = LOAD_SLOT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Restricted Contracts Override</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__RESTRICTED_CONTRACTS_OVERRIDE = LOAD_SLOT__RESTRICTED_CONTRACTS_OVERRIDE;

	/**
	 * The feature id for the '<em><b>Restricted Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__RESTRICTED_PORTS = LOAD_SLOT__RESTRICTED_PORTS;

	/**
	 * The feature id for the '<em><b>Restricted Ports Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__RESTRICTED_PORTS_ARE_PERMISSIVE = LOAD_SLOT__RESTRICTED_PORTS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Restricted Ports Override</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__RESTRICTED_PORTS_OVERRIDE = LOAD_SLOT__RESTRICTED_PORTS_OVERRIDE;

	/**
	 * The feature id for the '<em><b>Restricted Vessels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__RESTRICTED_VESSELS = LOAD_SLOT__RESTRICTED_VESSELS;

	/**
	 * The feature id for the '<em><b>Restricted Vessels Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__RESTRICTED_VESSELS_ARE_PERMISSIVE = LOAD_SLOT__RESTRICTED_VESSELS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Restricted Vessels Override</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__RESTRICTED_VESSELS_OVERRIDE = LOAD_SLOT__RESTRICTED_VESSELS_OVERRIDE;

	/**
	 * The feature id for the '<em><b>Restricted Slots</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__RESTRICTED_SLOTS = LOAD_SLOT__RESTRICTED_SLOTS;

	/**
	 * The feature id for the '<em><b>Restricted Slots Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__RESTRICTED_SLOTS_ARE_PERMISSIVE = LOAD_SLOT__RESTRICTED_SLOTS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Hedges</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__HEDGES = LOAD_SLOT__HEDGES;

	/**
	 * The feature id for the '<em><b>Misc Costs</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__MISC_COSTS = LOAD_SLOT__MISC_COSTS;

	/**
	 * The feature id for the '<em><b>Cancellation Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__CANCELLATION_EXPRESSION = LOAD_SLOT__CANCELLATION_EXPRESSION;

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
	 * The feature id for the '<em><b>Cancelled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__CANCELLED = LOAD_SLOT__CANCELLED;

	/**
	 * The feature id for the '<em><b>Window Counter Party</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__WINDOW_COUNTER_PARTY = LOAD_SLOT__WINDOW_COUNTER_PARTY;

	/**
	 * The feature id for the '<em><b>Cargo CV</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__CARGO_CV = LOAD_SLOT__CARGO_CV;

	/**
	 * The feature id for the '<em><b>Schedule Purge</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__SCHEDULE_PURGE = LOAD_SLOT__SCHEDULE_PURGE;

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
	 * The feature id for the '<em><b>Des Purchase Deal Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__DES_PURCHASE_DEAL_TYPE = LOAD_SLOT__DES_PURCHASE_DEAL_TYPE;

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
	 * The operation id for the '<em>Get Slot Or Delegate Min Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_DELEGATE_MIN_QUANTITY = LOAD_SLOT___GET_SLOT_OR_DELEGATE_MIN_QUANTITY;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Max Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_DELEGATE_MAX_QUANTITY = LOAD_SLOT___GET_SLOT_OR_DELEGATE_MAX_QUANTITY;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Operational Tolerance</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_DELEGATE_OPERATIONAL_TOLERANCE = LOAD_SLOT___GET_SLOT_OR_DELEGATE_OPERATIONAL_TOLERANCE;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Volume Limits Unit</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_DELEGATE_VOLUME_LIMITS_UNIT = LOAD_SLOT___GET_SLOT_OR_DELEGATE_VOLUME_LIMITS_UNIT;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Entity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_DELEGATE_ENTITY = LOAD_SLOT___GET_SLOT_OR_DELEGATE_ENTITY;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Cancellation Expression</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_DELEGATE_CANCELLATION_EXPRESSION = LOAD_SLOT___GET_SLOT_OR_DELEGATE_CANCELLATION_EXPRESSION;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Pricing Event</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_DELEGATE_PRICING_EVENT = LOAD_SLOT___GET_SLOT_OR_DELEGATE_PRICING_EVENT;

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
	 * The operation id for the '<em>Get Slot Or Delegate Counterparty</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_DELEGATE_COUNTERPARTY = LOAD_SLOT___GET_SLOT_OR_DELEGATE_COUNTERPARTY;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate CN</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_DELEGATE_CN = LOAD_SLOT___GET_SLOT_OR_DELEGATE_CN;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Shipping Days Restriction</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_DELEGATE_SHIPPING_DAYS_RESTRICTION = LOAD_SLOT___GET_SLOT_OR_DELEGATE_SHIPPING_DAYS_RESTRICTION;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Full Cargo Lot</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_DELEGATE_FULL_CARGO_LOT = LOAD_SLOT___GET_SLOT_OR_DELEGATE_FULL_CARGO_LOT;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Contract Restrictions Are Permissive</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_DELEGATE_CONTRACT_RESTRICTIONS_ARE_PERMISSIVE = LOAD_SLOT___GET_SLOT_OR_DELEGATE_CONTRACT_RESTRICTIONS_ARE_PERMISSIVE;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Port Restrictions Are Permissive</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_DELEGATE_PORT_RESTRICTIONS_ARE_PERMISSIVE = LOAD_SLOT___GET_SLOT_OR_DELEGATE_PORT_RESTRICTIONS_ARE_PERMISSIVE;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Vessel Restrictions Are Permissive</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_DELEGATE_VESSEL_RESTRICTIONS_ARE_PERMISSIVE = LOAD_SLOT___GET_SLOT_OR_DELEGATE_VESSEL_RESTRICTIONS_ARE_PERMISSIVE;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Contract Restrictions</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_DELEGATE_CONTRACT_RESTRICTIONS = LOAD_SLOT___GET_SLOT_OR_DELEGATE_CONTRACT_RESTRICTIONS;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Port Restrictions</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_DELEGATE_PORT_RESTRICTIONS = LOAD_SLOT___GET_SLOT_OR_DELEGATE_PORT_RESTRICTIONS;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Vessel Restrictions</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_DELEGATE_VESSEL_RESTRICTIONS = LOAD_SLOT___GET_SLOT_OR_DELEGATE_VESSEL_RESTRICTIONS;

	/**
	 * The operation id for the '<em>Get Scheduling Time Window</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SCHEDULING_TIME_WINDOW = LOAD_SLOT___GET_SCHEDULING_TIME_WINDOW;

	/**
	 * The operation id for the '<em>Get Days Buffer</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_DAYS_BUFFER = LOAD_SLOT___GET_DAYS_BUFFER;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate CV</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_DELEGATE_CV = LOAD_SLOT___GET_SLOT_OR_DELEGATE_CV;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Delivery Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_DELEGATE_DELIVERY_TYPE = LOAD_SLOT___GET_SLOT_OR_DELEGATE_DELIVERY_TYPE;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate DES Purchase Deal Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_DELEGATE_DES_PURCHASE_DEAL_TYPE = LOAD_SLOT___GET_SLOT_OR_DELEGATE_DES_PURCHASE_DEAL_TYPE;

	/**
	 * The number of operations of the '<em>Spot Load Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT_OPERATION_COUNT = LOAD_SLOT_OPERATION_COUNT + 0;

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
	 * The feature id for the '<em><b>Counterparty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__COUNTERPARTY = DISCHARGE_SLOT__COUNTERPARTY;

	/**
	 * The feature id for the '<em><b>Cn</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__CN = DISCHARGE_SLOT__CN;

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
	 * The feature id for the '<em><b>Operational Tolerance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__OPERATIONAL_TOLERANCE = DISCHARGE_SLOT__OPERATIONAL_TOLERANCE;

	/**
	 * The feature id for the '<em><b>Full Cargo Lot</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__FULL_CARGO_LOT = DISCHARGE_SLOT__FULL_CARGO_LOT;

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
	 * The feature id for the '<em><b>Restricted Contracts Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE = DISCHARGE_SLOT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Restricted Contracts Override</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__RESTRICTED_CONTRACTS_OVERRIDE = DISCHARGE_SLOT__RESTRICTED_CONTRACTS_OVERRIDE;

	/**
	 * The feature id for the '<em><b>Restricted Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__RESTRICTED_PORTS = DISCHARGE_SLOT__RESTRICTED_PORTS;

	/**
	 * The feature id for the '<em><b>Restricted Ports Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__RESTRICTED_PORTS_ARE_PERMISSIVE = DISCHARGE_SLOT__RESTRICTED_PORTS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Restricted Ports Override</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__RESTRICTED_PORTS_OVERRIDE = DISCHARGE_SLOT__RESTRICTED_PORTS_OVERRIDE;

	/**
	 * The feature id for the '<em><b>Restricted Vessels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__RESTRICTED_VESSELS = DISCHARGE_SLOT__RESTRICTED_VESSELS;

	/**
	 * The feature id for the '<em><b>Restricted Vessels Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__RESTRICTED_VESSELS_ARE_PERMISSIVE = DISCHARGE_SLOT__RESTRICTED_VESSELS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Restricted Vessels Override</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__RESTRICTED_VESSELS_OVERRIDE = DISCHARGE_SLOT__RESTRICTED_VESSELS_OVERRIDE;

	/**
	 * The feature id for the '<em><b>Restricted Slots</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__RESTRICTED_SLOTS = DISCHARGE_SLOT__RESTRICTED_SLOTS;

	/**
	 * The feature id for the '<em><b>Restricted Slots Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__RESTRICTED_SLOTS_ARE_PERMISSIVE = DISCHARGE_SLOT__RESTRICTED_SLOTS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Hedges</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__HEDGES = DISCHARGE_SLOT__HEDGES;

	/**
	 * The feature id for the '<em><b>Misc Costs</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__MISC_COSTS = DISCHARGE_SLOT__MISC_COSTS;

	/**
	 * The feature id for the '<em><b>Cancellation Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__CANCELLATION_EXPRESSION = DISCHARGE_SLOT__CANCELLATION_EXPRESSION;

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
	 * The feature id for the '<em><b>Cancelled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__CANCELLED = DISCHARGE_SLOT__CANCELLED;

	/**
	 * The feature id for the '<em><b>Window Counter Party</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__WINDOW_COUNTER_PARTY = DISCHARGE_SLOT__WINDOW_COUNTER_PARTY;

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
	 * The feature id for the '<em><b>Fob Sale Deal Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__FOB_SALE_DEAL_TYPE = DISCHARGE_SLOT__FOB_SALE_DEAL_TYPE;

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
	 * The operation id for the '<em>Get Slot Or Delegate Min Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_MIN_QUANTITY = DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_MIN_QUANTITY;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Max Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_MAX_QUANTITY = DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_MAX_QUANTITY;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Operational Tolerance</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_OPERATIONAL_TOLERANCE = DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_OPERATIONAL_TOLERANCE;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Volume Limits Unit</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_VOLUME_LIMITS_UNIT = DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_VOLUME_LIMITS_UNIT;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Entity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_ENTITY = DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_ENTITY;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Cancellation Expression</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_CANCELLATION_EXPRESSION = DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_CANCELLATION_EXPRESSION;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Pricing Event</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_PRICING_EVENT = DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_PRICING_EVENT;

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
	 * The operation id for the '<em>Get Slot Or Delegate Counterparty</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_COUNTERPARTY = DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_COUNTERPARTY;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate CN</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_CN = DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_CN;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Shipping Days Restriction</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_SHIPPING_DAYS_RESTRICTION = DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_SHIPPING_DAYS_RESTRICTION;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Full Cargo Lot</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_FULL_CARGO_LOT = DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_FULL_CARGO_LOT;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Contract Restrictions Are Permissive</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_CONTRACT_RESTRICTIONS_ARE_PERMISSIVE = DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_CONTRACT_RESTRICTIONS_ARE_PERMISSIVE;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Port Restrictions Are Permissive</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_PORT_RESTRICTIONS_ARE_PERMISSIVE = DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_PORT_RESTRICTIONS_ARE_PERMISSIVE;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Vessel Restrictions Are Permissive</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_VESSEL_RESTRICTIONS_ARE_PERMISSIVE = DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_VESSEL_RESTRICTIONS_ARE_PERMISSIVE;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Contract Restrictions</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_CONTRACT_RESTRICTIONS = DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_CONTRACT_RESTRICTIONS;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Port Restrictions</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_PORT_RESTRICTIONS = DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_PORT_RESTRICTIONS;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Vessel Restrictions</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_VESSEL_RESTRICTIONS = DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_VESSEL_RESTRICTIONS;

	/**
	 * The operation id for the '<em>Get Scheduling Time Window</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SCHEDULING_TIME_WINDOW = DISCHARGE_SLOT___GET_SCHEDULING_TIME_WINDOW;

	/**
	 * The operation id for the '<em>Get Days Buffer</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_DAYS_BUFFER = DISCHARGE_SLOT___GET_DAYS_BUFFER;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Min Cv</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_MIN_CV = DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_MIN_CV;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Max Cv</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_MAX_CV = DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_MAX_CV;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate Delivery Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_DELIVERY_TYPE = DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_DELIVERY_TYPE;

	/**
	 * The operation id for the '<em>Get Slot Or Delegate FOB Sale Deal Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_FOB_SALE_DEAL_TYPE = DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_FOB_SALE_DEAL_TYPE;

	/**
	 * The number of operations of the '<em>Spot Discharge Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT_OPERATION_COUNT = DISCHARGE_SLOT_OPERATION_COUNT + 0;

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
	 * The feature id for the '<em><b>Fleet</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__FLEET = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Optional</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__OPTIONAL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__VESSEL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Charter Number</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__CHARTER_NUMBER = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__ENTITY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Time Charter Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__TIME_CHARTER_RATE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Start At</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__START_AT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Start After</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__START_AFTER = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Start By</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__START_BY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>End At</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__END_AT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>End After</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__END_AFTER = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>End By</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__END_BY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>Start Heel</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__START_HEEL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 12;

	/**
	 * The feature id for the '<em><b>End Heel</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__END_HEEL = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 13;

	/**
	 * The feature id for the '<em><b>Force Hire Cost Only End Rule</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__FORCE_HIRE_COST_ONLY_END_RULE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 14;

	/**
	 * The feature id for the '<em><b>Repositioning Fee</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__REPOSITIONING_FEE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 15;

	/**
	 * The feature id for the '<em><b>Ballast Bonus Contract</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__BALLAST_BONUS_CONTRACT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 16;

	/**
	 * The feature id for the '<em><b>Charter Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__CHARTER_CONTRACT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 17;

	/**
	 * The feature id for the '<em><b>Min Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__MIN_DURATION = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 18;

	/**
	 * The feature id for the '<em><b>Max Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__MAX_DURATION = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 19;

	/**
	 * The number of structural features of the '<em>Vessel Availability</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 20;

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
	 * The operation id for the '<em>Get Charter Or Delegate Ballast Bonus Contract</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY___GET_CHARTER_OR_DELEGATE_BALLAST_BONUS_CONTRACT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 4;

	/**
	 * The operation id for the '<em>Get Charter Or Delegate Min Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY___GET_CHARTER_OR_DELEGATE_MIN_DURATION = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 5;

	/**
	 * The operation id for the '<em>Get Charter Or Delegate Max Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY___GET_CHARTER_OR_DELEGATE_MAX_DURATION = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 6;

	/**
	 * The operation id for the '<em>Get Charter Or Delegate Entity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY___GET_CHARTER_OR_DELEGATE_ENTITY = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 7;

	/**
	 * The operation id for the '<em>Get Charter Or Delegate Repositioning Fee</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY___GET_CHARTER_OR_DELEGATE_REPOSITIONING_FEE = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 8;

	/**
	 * The operation id for the '<em>Jsonid</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY___JSONID = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 9;

	/**
	 * The number of operations of the '<em>Vessel Availability</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY_OPERATION_COUNT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 10;

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
	 * The feature id for the '<em><b>Optional</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT__OPTIONAL = VESSEL_EVENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Relocate To</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT__RELOCATE_TO = VESSEL_EVENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Hire Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT__HIRE_RATE = VESSEL_EVENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Ballast Bonus</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT__BALLAST_BONUS = VESSEL_EVENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Repositioning Fee</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT__REPOSITIONING_FEE = VESSEL_EVENT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Required Heel</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT__REQUIRED_HEEL = VESSEL_EVENT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Available Heel</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT__AVAILABLE_HEEL = VESSEL_EVENT_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Charter Out Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT_FEATURE_COUNT = VESSEL_EVENT_FEATURE_COUNT + 7;

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
	 * The feature id for the '<em><b>Tank State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_HEEL_OPTIONS__TANK_STATE = 0;

	/**
	 * The feature id for the '<em><b>Minimum End Heel</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_HEEL_OPTIONS__MINIMUM_END_HEEL = 1;

	/**
	 * The feature id for the '<em><b>Maximum End Heel</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_HEEL_OPTIONS__MAXIMUM_END_HEEL = 2;

	/**
	 * The feature id for the '<em><b>Use Last Heel Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_HEEL_OPTIONS__USE_LAST_HEEL_PRICE = 3;

	/**
	 * The feature id for the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_HEEL_OPTIONS__PRICE_EXPRESSION = 4;

	/**
	 * The number of structural features of the '<em>End Heel Options</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_HEEL_OPTIONS_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>End Heel Options</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_HEEL_OPTIONS_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.StartHeelOptionsImpl <em>Start Heel Options</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.StartHeelOptionsImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getStartHeelOptions()
	 * @generated
	 */
	int START_HEEL_OPTIONS = 17;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_HEEL_OPTIONS__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_HEEL_OPTIONS__CV_VALUE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Min Volume Available</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_HEEL_OPTIONS__MIN_VOLUME_AVAILABLE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Max Volume Available</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_HEEL_OPTIONS__MAX_VOLUME_AVAILABLE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_HEEL_OPTIONS__PRICE_EXPRESSION = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Start Heel Options</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_HEEL_OPTIONS_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_HEEL_OPTIONS___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.MMX_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_HEEL_OPTIONS___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.MMX_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_HEEL_OPTIONS___ECONTAINER_OP = MMXCorePackage.MMX_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>Start Heel Options</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_HEEL_OPTIONS_OPERATION_COUNT = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.InventoryEventRowImpl <em>Inventory Event Row</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.InventoryEventRowImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getInventoryEventRow()
	 * @generated
	 */
	int INVENTORY_EVENT_ROW = 18;

	/**
	 * The feature id for the '<em><b>Start Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVENTORY_EVENT_ROW__START_DATE = 0;

	/**
	 * The feature id for the '<em><b>End Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVENTORY_EVENT_ROW__END_DATE = 1;

	/**
	 * The feature id for the '<em><b>Period</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVENTORY_EVENT_ROW__PERIOD = 2;

	/**
	 * The feature id for the '<em><b>Counter Party</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVENTORY_EVENT_ROW__COUNTER_PARTY = 3;

	/**
	 * The feature id for the '<em><b>Reliability</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVENTORY_EVENT_ROW__RELIABILITY = 4;

	/**
	 * The feature id for the '<em><b>Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVENTORY_EVENT_ROW__VOLUME = 5;

	/**
	 * The feature id for the '<em><b>Forecast Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVENTORY_EVENT_ROW__FORECAST_DATE = 6;

	/**
	 * The feature id for the '<em><b>Volume Low</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVENTORY_EVENT_ROW__VOLUME_LOW = 7;

	/**
	 * The feature id for the '<em><b>Volume High</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVENTORY_EVENT_ROW__VOLUME_HIGH = 8;

	/**
	 * The number of structural features of the '<em>Inventory Event Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVENTORY_EVENT_ROW_FEATURE_COUNT = 9;

	/**
	 * The operation id for the '<em>Get Reliable Volume</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVENTORY_EVENT_ROW___GET_RELIABLE_VOLUME = 0;

	/**
	 * The number of operations of the '<em>Inventory Event Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVENTORY_EVENT_ROW_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.InventoryCapacityRowImpl <em>Inventory Capacity Row</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.InventoryCapacityRowImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getInventoryCapacityRow()
	 * @generated
	 */
	int INVENTORY_CAPACITY_ROW = 19;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVENTORY_CAPACITY_ROW__DATE = 0;

	/**
	 * The feature id for the '<em><b>Min Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVENTORY_CAPACITY_ROW__MIN_VOLUME = 1;

	/**
	 * The feature id for the '<em><b>Max Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVENTORY_CAPACITY_ROW__MAX_VOLUME = 2;

	/**
	 * The number of structural features of the '<em>Inventory Capacity Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVENTORY_CAPACITY_ROW_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Inventory Capacity Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVENTORY_CAPACITY_ROW_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.InventoryImpl <em>Inventory</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.InventoryImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getInventory()
	 * @generated
	 */
	int INVENTORY = 20;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVENTORY__EXTENSIONS = MMXCorePackage.NAMED_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVENTORY__NAME = MMXCorePackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVENTORY__PORT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Feeds</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVENTORY__FEEDS = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Offtakes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVENTORY__OFFTAKES = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Capacities</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVENTORY__CAPACITIES = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Inventory</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVENTORY_FEATURE_COUNT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVENTORY___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.NAMED_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVENTORY___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.NAMED_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVENTORY___ECONTAINER_OP = MMXCorePackage.NAMED_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>Inventory</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVENTORY_OPERATION_COUNT = MMXCorePackage.NAMED_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.CanalBookingSlotImpl <em>Canal Booking Slot</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.CanalBookingSlotImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getCanalBookingSlot()
	 * @generated
	 */
	int CANAL_BOOKING_SLOT = 21;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL_BOOKING_SLOT__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Route Option</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL_BOOKING_SLOT__ROUTE_OPTION = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Canal Entrance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL_BOOKING_SLOT__CANAL_ENTRANCE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Booking Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL_BOOKING_SLOT__BOOKING_DATE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL_BOOKING_SLOT__SLOT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL_BOOKING_SLOT__NOTES = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Canal Booking Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL_BOOKING_SLOT_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL_BOOKING_SLOT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.MMX_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL_BOOKING_SLOT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.MMX_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL_BOOKING_SLOT___ECONTAINER_OP = MMXCorePackage.MMX_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>Canal Booking Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL_BOOKING_SLOT_OPERATION_COUNT = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.CanalBookingsImpl <em>Canal Bookings</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.CanalBookingsImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getCanalBookings()
	 * @generated
	 */
	int CANAL_BOOKINGS = 22;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL_BOOKINGS__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Canal Booking Slots</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL_BOOKINGS__CANAL_BOOKING_SLOTS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Strict Boundary Offset Days</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL_BOOKINGS__STRICT_BOUNDARY_OFFSET_DAYS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Relaxed Boundary Offset Days</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL_BOOKINGS__RELAXED_BOUNDARY_OFFSET_DAYS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Arrival Margin Hours</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL_BOOKINGS__ARRIVAL_MARGIN_HOURS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Flexible Booking Amount Northbound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL_BOOKINGS__FLEXIBLE_BOOKING_AMOUNT_NORTHBOUND = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Flexible Booking Amount Southbound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL_BOOKINGS__FLEXIBLE_BOOKING_AMOUNT_SOUTHBOUND = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Northbound Max Idle Days</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL_BOOKINGS__NORTHBOUND_MAX_IDLE_DAYS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Canal Bookings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL_BOOKINGS_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL_BOOKINGS___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.MMX_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL_BOOKINGS___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.MMX_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL_BOOKINGS___ECONTAINER_OP = MMXCorePackage.MMX_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>Canal Bookings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANAL_BOOKINGS_OPERATION_COUNT = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.ScheduleSpecificationImpl <em>Schedule Specification</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.ScheduleSpecificationImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getScheduleSpecification()
	 * @generated
	 */
	int SCHEDULE_SPECIFICATION = 23;

	/**
	 * The feature id for the '<em><b>Vessel Schedule Specifications</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_SPECIFICATION__VESSEL_SCHEDULE_SPECIFICATIONS = 0;

	/**
	 * The feature id for the '<em><b>Non Shipped Cargo Specifications</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_SPECIFICATION__NON_SHIPPED_CARGO_SPECIFICATIONS = 1;

	/**
	 * The feature id for the '<em><b>Open Events</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_SPECIFICATION__OPEN_EVENTS = 2;

	/**
	 * The number of structural features of the '<em>Schedule Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_SPECIFICATION_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Schedule Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_SPECIFICATION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.NonShippedCargoSpecificationImpl <em>Non Shipped Cargo Specification</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.NonShippedCargoSpecificationImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getNonShippedCargoSpecification()
	 * @generated
	 */
	int NON_SHIPPED_CARGO_SPECIFICATION = 24;

	/**
	 * The feature id for the '<em><b>Slot Specifications</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NON_SHIPPED_CARGO_SPECIFICATION__SLOT_SPECIFICATIONS = 0;

	/**
	 * The number of structural features of the '<em>Non Shipped Cargo Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NON_SHIPPED_CARGO_SPECIFICATION_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Non Shipped Cargo Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NON_SHIPPED_CARGO_SPECIFICATION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.VesselScheduleSpecificationImpl <em>Vessel Schedule Specification</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.VesselScheduleSpecificationImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getVesselScheduleSpecification()
	 * @generated
	 */
	int VESSEL_SCHEDULE_SPECIFICATION = 25;

	/**
	 * The feature id for the '<em><b>Vessel Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_SCHEDULE_SPECIFICATION__VESSEL_ALLOCATION = 0;

	/**
	 * The feature id for the '<em><b>Spot Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_SCHEDULE_SPECIFICATION__SPOT_INDEX = 1;

	/**
	 * The feature id for the '<em><b>Events</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_SCHEDULE_SPECIFICATION__EVENTS = 2;

	/**
	 * The number of structural features of the '<em>Vessel Schedule Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_SCHEDULE_SPECIFICATION_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Vessel Schedule Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_SCHEDULE_SPECIFICATION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.ScheduleSpecificationEventImpl <em>Schedule Specification Event</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.ScheduleSpecificationEventImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getScheduleSpecificationEvent()
	 * @generated
	 */
	int SCHEDULE_SPECIFICATION_EVENT = 26;

	/**
	 * The number of structural features of the '<em>Schedule Specification Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_SPECIFICATION_EVENT_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Schedule Specification Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_SPECIFICATION_EVENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.VesselEventSpecificationImpl <em>Vessel Event Specification</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.VesselEventSpecificationImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getVesselEventSpecification()
	 * @generated
	 */
	int VESSEL_EVENT_SPECIFICATION = 27;

	/**
	 * The feature id for the '<em><b>Vessel Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_SPECIFICATION__VESSEL_EVENT = SCHEDULE_SPECIFICATION_EVENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Vessel Event Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_SPECIFICATION_FEATURE_COUNT = SCHEDULE_SPECIFICATION_EVENT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Vessel Event Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_SPECIFICATION_OPERATION_COUNT = SCHEDULE_SPECIFICATION_EVENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.VoyageSpecificationImpl <em>Voyage Specification</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.VoyageSpecificationImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getVoyageSpecification()
	 * @generated
	 */
	int VOYAGE_SPECIFICATION = 28;

	/**
	 * The number of structural features of the '<em>Voyage Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VOYAGE_SPECIFICATION_FEATURE_COUNT = SCHEDULE_SPECIFICATION_EVENT_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Voyage Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VOYAGE_SPECIFICATION_OPERATION_COUNT = SCHEDULE_SPECIFICATION_EVENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.SlotSpecificationImpl <em>Slot Specification</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.SlotSpecificationImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getSlotSpecification()
	 * @generated
	 */
	int SLOT_SPECIFICATION = 29;

	/**
	 * The feature id for the '<em><b>Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_SPECIFICATION__SLOT = SCHEDULE_SPECIFICATION_EVENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Slot Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_SPECIFICATION_FEATURE_COUNT = SCHEDULE_SPECIFICATION_EVENT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Slot Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_SPECIFICATION_OPERATION_COUNT = SCHEDULE_SPECIFICATION_EVENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.CharterInMarketOverrideImpl <em>Charter In Market Override</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.CharterInMarketOverrideImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getCharterInMarketOverride()
	 * @generated
	 */
	int CHARTER_IN_MARKET_OVERRIDE = 30;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_IN_MARKET_OVERRIDE__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Charter In Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_IN_MARKET_OVERRIDE__CHARTER_IN_MARKET = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Spot Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_IN_MARKET_OVERRIDE__SPOT_INDEX = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Start Heel</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_IN_MARKET_OVERRIDE__START_HEEL = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Start Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_IN_MARKET_OVERRIDE__START_DATE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>End Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_IN_MARKET_OVERRIDE__END_PORT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>End Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_IN_MARKET_OVERRIDE__END_DATE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>End Heel</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_IN_MARKET_OVERRIDE__END_HEEL = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Include Ballast Bonus</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_IN_MARKET_OVERRIDE__INCLUDE_BALLAST_BONUS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Min Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_IN_MARKET_OVERRIDE__MIN_DURATION = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Max Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_IN_MARKET_OVERRIDE__MAX_DURATION = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 9;

	/**
	 * The number of structural features of the '<em>Charter In Market Override</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_IN_MARKET_OVERRIDE_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 10;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_IN_MARKET_OVERRIDE___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.MMX_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_IN_MARKET_OVERRIDE___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.MMX_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_IN_MARKET_OVERRIDE___ECONTAINER_OP = MMXCorePackage.MMX_OBJECT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Start Date As Date Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_IN_MARKET_OVERRIDE___GET_START_DATE_AS_DATE_TIME = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get End Date As Date Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_IN_MARKET_OVERRIDE___GET_END_DATE_AS_DATE_TIME = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Local Or Delegate Min Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_IN_MARKET_OVERRIDE___GET_LOCAL_OR_DELEGATE_MIN_DURATION = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 2;

	/**
	 * The operation id for the '<em>Get Local Or Delegate Max Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_IN_MARKET_OVERRIDE___GET_LOCAL_OR_DELEGATE_MAX_DURATION = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 3;

	/**
	 * The number of operations of the '<em>Charter In Market Override</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_IN_MARKET_OVERRIDE_OPERATION_COUNT = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 4;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.PaperDealImpl <em>Paper Deal</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.PaperDealImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getPaperDeal()
	 * @generated
	 */
	int PAPER_DEAL = 31;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAPER_DEAL__EXTENSIONS = MMXCorePackage.NAMED_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAPER_DEAL__NAME = MMXCorePackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAPER_DEAL__PRICE = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAPER_DEAL__INDEX = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Instrument</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAPER_DEAL__INSTRUMENT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAPER_DEAL__QUANTITY = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Start Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAPER_DEAL__START_DATE = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>End Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAPER_DEAL__END_DATE = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAPER_DEAL__ENTITY = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Year</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAPER_DEAL__YEAR = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAPER_DEAL__COMMENT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The number of structural features of the '<em>Paper Deal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAPER_DEAL_FEATURE_COUNT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 9;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAPER_DEAL___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.NAMED_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAPER_DEAL___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.NAMED_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAPER_DEAL___ECONTAINER_OP = MMXCorePackage.NAMED_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>Paper Deal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAPER_DEAL_OPERATION_COUNT = MMXCorePackage.NAMED_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.BuyPaperDealImpl <em>Buy Paper Deal</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.BuyPaperDealImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getBuyPaperDeal()
	 * @generated
	 */
	int BUY_PAPER_DEAL = 32;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_PAPER_DEAL__EXTENSIONS = PAPER_DEAL__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_PAPER_DEAL__NAME = PAPER_DEAL__NAME;

	/**
	 * The feature id for the '<em><b>Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_PAPER_DEAL__PRICE = PAPER_DEAL__PRICE;

	/**
	 * The feature id for the '<em><b>Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_PAPER_DEAL__INDEX = PAPER_DEAL__INDEX;

	/**
	 * The feature id for the '<em><b>Instrument</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_PAPER_DEAL__INSTRUMENT = PAPER_DEAL__INSTRUMENT;

	/**
	 * The feature id for the '<em><b>Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_PAPER_DEAL__QUANTITY = PAPER_DEAL__QUANTITY;

	/**
	 * The feature id for the '<em><b>Start Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_PAPER_DEAL__START_DATE = PAPER_DEAL__START_DATE;

	/**
	 * The feature id for the '<em><b>End Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_PAPER_DEAL__END_DATE = PAPER_DEAL__END_DATE;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_PAPER_DEAL__ENTITY = PAPER_DEAL__ENTITY;

	/**
	 * The feature id for the '<em><b>Year</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_PAPER_DEAL__YEAR = PAPER_DEAL__YEAR;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_PAPER_DEAL__COMMENT = PAPER_DEAL__COMMENT;

	/**
	 * The number of structural features of the '<em>Buy Paper Deal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_PAPER_DEAL_FEATURE_COUNT = PAPER_DEAL_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_PAPER_DEAL___GET_UNSET_VALUE__ESTRUCTURALFEATURE = PAPER_DEAL___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_PAPER_DEAL___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = PAPER_DEAL___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_PAPER_DEAL___ECONTAINER_OP = PAPER_DEAL___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>Buy Paper Deal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUY_PAPER_DEAL_OPERATION_COUNT = PAPER_DEAL_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.SellPaperDealImpl <em>Sell Paper Deal</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.SellPaperDealImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getSellPaperDeal()
	 * @generated
	 */
	int SELL_PAPER_DEAL = 33;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_PAPER_DEAL__EXTENSIONS = PAPER_DEAL__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_PAPER_DEAL__NAME = PAPER_DEAL__NAME;

	/**
	 * The feature id for the '<em><b>Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_PAPER_DEAL__PRICE = PAPER_DEAL__PRICE;

	/**
	 * The feature id for the '<em><b>Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_PAPER_DEAL__INDEX = PAPER_DEAL__INDEX;

	/**
	 * The feature id for the '<em><b>Instrument</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_PAPER_DEAL__INSTRUMENT = PAPER_DEAL__INSTRUMENT;

	/**
	 * The feature id for the '<em><b>Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_PAPER_DEAL__QUANTITY = PAPER_DEAL__QUANTITY;

	/**
	 * The feature id for the '<em><b>Start Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_PAPER_DEAL__START_DATE = PAPER_DEAL__START_DATE;

	/**
	 * The feature id for the '<em><b>End Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_PAPER_DEAL__END_DATE = PAPER_DEAL__END_DATE;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_PAPER_DEAL__ENTITY = PAPER_DEAL__ENTITY;

	/**
	 * The feature id for the '<em><b>Year</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_PAPER_DEAL__YEAR = PAPER_DEAL__YEAR;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_PAPER_DEAL__COMMENT = PAPER_DEAL__COMMENT;

	/**
	 * The number of structural features of the '<em>Sell Paper Deal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_PAPER_DEAL_FEATURE_COUNT = PAPER_DEAL_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_PAPER_DEAL___GET_UNSET_VALUE__ESTRUCTURALFEATURE = PAPER_DEAL___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_PAPER_DEAL___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = PAPER_DEAL___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_PAPER_DEAL___ECONTAINER_OP = PAPER_DEAL___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>Sell Paper Deal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SELL_PAPER_DEAL_OPERATION_COUNT = PAPER_DEAL_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.DealSetImpl <em>Deal Set</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.DealSetImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getDealSet()
	 * @generated
	 */
	int DEAL_SET = 34;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEAL_SET__EXTENSIONS = MMXCorePackage.NAMED_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEAL_SET__NAME = MMXCorePackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Slots</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEAL_SET__SLOTS = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Paper Deals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEAL_SET__PAPER_DEALS = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Deal Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEAL_SET_FEATURE_COUNT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEAL_SET___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.NAMED_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEAL_SET___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.NAMED_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEAL_SET___ECONTAINER_OP = MMXCorePackage.NAMED_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>Deal Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEAL_SET_OPERATION_COUNT = MMXCorePackage.NAMED_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.CargoType <em>Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.CargoType
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getCargoType()
	 * @generated
	 */
	int CARGO_TYPE = 35;


	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.VesselType <em>Vessel Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.VesselType
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getVesselType()
	 * @generated
	 */
	int VESSEL_TYPE = 36;


	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.EVesselTankState <em>EVessel Tank State</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.EVesselTankState
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getEVesselTankState()
	 * @generated
	 */
	int EVESSEL_TANK_STATE = 37;


	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.InventoryFrequency <em>Inventory Frequency</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.InventoryFrequency
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getInventoryFrequency()
	 * @generated
	 */
	int INVENTORY_FREQUENCY = 38;


	/**
	 * The meta object id for the '<em>Scheduling Time Window</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.SchedulingTimeWindow
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getSchedulingTimeWindow()
	 * @generated
	 */
	int SCHEDULING_TIME_WINDOW = 39;

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#getCounterparty <em>Counterparty</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Counterparty</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getCounterparty()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_Counterparty();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#getCn <em>Cn</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cn</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getCn()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_Cn();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#getOperationalTolerance <em>Operational Tolerance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Operational Tolerance</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getOperationalTolerance()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_OperationalTolerance();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#isFullCargoLot <em>Full Cargo Lot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Full Cargo Lot</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#isFullCargoLot()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_FullCargoLot();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#isRestrictedContractsArePermissive <em>Restricted Contracts Are Permissive</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Restricted Contracts Are Permissive</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#isRestrictedContractsArePermissive()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_RestrictedContractsArePermissive();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#isRestrictedContractsOverride <em>Restricted Contracts Override</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Restricted Contracts Override</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#isRestrictedContractsOverride()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_RestrictedContractsOverride();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#isRestrictedPortsArePermissive <em>Restricted Ports Are Permissive</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Restricted Ports Are Permissive</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#isRestrictedPortsArePermissive()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_RestrictedPortsArePermissive();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#isRestrictedPortsOverride <em>Restricted Ports Override</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Restricted Ports Override</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#isRestrictedPortsOverride()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_RestrictedPortsOverride();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.cargo.Slot#getRestrictedSlots <em>Restricted Slots</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Restricted Slots</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getRestrictedSlots()
	 * @see #getSlot()
	 * @generated
	 */
	EReference getSlot_RestrictedSlots();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#isRestrictedSlotsArePermissive <em>Restricted Slots Are Permissive</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Restricted Slots Are Permissive</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#isRestrictedSlotsArePermissive()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_RestrictedSlotsArePermissive();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.cargo.Slot#getRestrictedVessels <em>Restricted Vessels</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Restricted Vessels</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getRestrictedVessels()
	 * @see #getSlot()
	 * @generated
	 */
	EReference getSlot_RestrictedVessels();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#isRestrictedVesselsArePermissive <em>Restricted Vessels Are Permissive</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Restricted Vessels Are Permissive</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#isRestrictedVesselsArePermissive()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_RestrictedVesselsArePermissive();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#isRestrictedVesselsOverride <em>Restricted Vessels Override</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Restricted Vessels Override</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#isRestrictedVesselsOverride()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_RestrictedVesselsOverride();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#getMiscCosts <em>Misc Costs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Misc Costs</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getMiscCosts()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_MiscCosts();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#isCancelled <em>Cancelled</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cancelled</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#isCancelled()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_Cancelled();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#isWindowCounterParty <em>Window Counter Party</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Window Counter Party</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#isWindowCounterParty()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_WindowCounterParty();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegateMinQuantity() <em>Get Slot Or Delegate Min Quantity</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Delegate Min Quantity</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegateMinQuantity()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrDelegateMinQuantity();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegateMaxQuantity() <em>Get Slot Or Delegate Max Quantity</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Delegate Max Quantity</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegateMaxQuantity()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrDelegateMaxQuantity();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegateOperationalTolerance() <em>Get Slot Or Delegate Operational Tolerance</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Delegate Operational Tolerance</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegateOperationalTolerance()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrDelegateOperationalTolerance();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegateVolumeLimitsUnit() <em>Get Slot Or Delegate Volume Limits Unit</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Delegate Volume Limits Unit</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegateVolumeLimitsUnit()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrDelegateVolumeLimitsUnit();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegateEntity() <em>Get Slot Or Delegate Entity</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Delegate Entity</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegateEntity()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrDelegateEntity();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegateCancellationExpression() <em>Get Slot Or Delegate Cancellation Expression</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Delegate Cancellation Expression</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegateCancellationExpression()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrDelegateCancellationExpression();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegatePricingEvent() <em>Get Slot Or Delegate Pricing Event</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Delegate Pricing Event</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegatePricingEvent()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrDelegatePricingEvent();

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
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegateCounterparty() <em>Get Slot Or Delegate Counterparty</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Delegate Counterparty</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegateCounterparty()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrDelegateCounterparty();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegateCN() <em>Get Slot Or Delegate CN</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Delegate CN</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegateCN()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrDelegateCN();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegateShippingDaysRestriction() <em>Get Slot Or Delegate Shipping Days Restriction</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Delegate Shipping Days Restriction</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegateShippingDaysRestriction()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrDelegateShippingDaysRestriction();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegateFullCargoLot() <em>Get Slot Or Delegate Full Cargo Lot</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Delegate Full Cargo Lot</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegateFullCargoLot()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrDelegateFullCargoLot();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegateContractRestrictionsArePermissive() <em>Get Slot Or Delegate Contract Restrictions Are Permissive</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Delegate Contract Restrictions Are Permissive</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegateContractRestrictionsArePermissive()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrDelegateContractRestrictionsArePermissive();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegatePortRestrictionsArePermissive() <em>Get Slot Or Delegate Port Restrictions Are Permissive</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Delegate Port Restrictions Are Permissive</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegatePortRestrictionsArePermissive()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrDelegatePortRestrictionsArePermissive();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegateVesselRestrictionsArePermissive() <em>Get Slot Or Delegate Vessel Restrictions Are Permissive</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Delegate Vessel Restrictions Are Permissive</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegateVesselRestrictionsArePermissive()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrDelegateVesselRestrictionsArePermissive();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegateContractRestrictions() <em>Get Slot Or Delegate Contract Restrictions</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Delegate Contract Restrictions</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegateContractRestrictions()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrDelegateContractRestrictions();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegatePortRestrictions() <em>Get Slot Or Delegate Port Restrictions</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Delegate Port Restrictions</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegatePortRestrictions()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrDelegatePortRestrictions();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegateVesselRestrictions() <em>Get Slot Or Delegate Vessel Restrictions</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Delegate Vessel Restrictions</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrDelegateVesselRestrictions()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrDelegateVesselRestrictions();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSchedulingTimeWindow() <em>Get Scheduling Time Window</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Scheduling Time Window</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSchedulingTimeWindow()
	 * @generated
	 */
	EOperation getSlot__GetSchedulingTimeWindow();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getDaysBuffer() <em>Get Days Buffer</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Days Buffer</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getDaysBuffer()
	 * @generated
	 */
	EOperation getSlot__GetDaysBuffer();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.LoadSlot#isSchedulePurge <em>Schedule Purge</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Schedule Purge</em>'.
	 * @see com.mmxlabs.models.lng.cargo.LoadSlot#isSchedulePurge()
	 * @see #getLoadSlot()
	 * @generated
	 */
	EAttribute getLoadSlot_SchedulePurge();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.LoadSlot#getDesPurchaseDealType <em>Des Purchase Deal Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Des Purchase Deal Type</em>'.
	 * @see com.mmxlabs.models.lng.cargo.LoadSlot#getDesPurchaseDealType()
	 * @see #getLoadSlot()
	 * @generated
	 */
	EAttribute getLoadSlot_DesPurchaseDealType();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.LoadSlot#getSlotOrDelegateCV() <em>Get Slot Or Delegate CV</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Delegate CV</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.LoadSlot#getSlotOrDelegateCV()
	 * @generated
	 */
	EOperation getLoadSlot__GetSlotOrDelegateCV();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.LoadSlot#getSlotOrDelegateDeliveryType() <em>Get Slot Or Delegate Delivery Type</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Delegate Delivery Type</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.LoadSlot#getSlotOrDelegateDeliveryType()
	 * @generated
	 */
	EOperation getLoadSlot__GetSlotOrDelegateDeliveryType();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.LoadSlot#getSlotOrDelegateDESPurchaseDealType() <em>Get Slot Or Delegate DES Purchase Deal Type</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Delegate DES Purchase Deal Type</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.LoadSlot#getSlotOrDelegateDESPurchaseDealType()
	 * @generated
	 */
	EOperation getLoadSlot__GetSlotOrDelegateDESPurchaseDealType();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getFobSaleDealType <em>Fob Sale Deal Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fob Sale Deal Type</em>'.
	 * @see com.mmxlabs.models.lng.cargo.DischargeSlot#getFobSaleDealType()
	 * @see #getDischargeSlot()
	 * @generated
	 */
	EAttribute getDischargeSlot_FobSaleDealType();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getSlotOrDelegateMinCv() <em>Get Slot Or Delegate Min Cv</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Delegate Min Cv</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.DischargeSlot#getSlotOrDelegateMinCv()
	 * @generated
	 */
	EOperation getDischargeSlot__GetSlotOrDelegateMinCv();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getSlotOrDelegateMaxCv() <em>Get Slot Or Delegate Max Cv</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Delegate Max Cv</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.DischargeSlot#getSlotOrDelegateMaxCv()
	 * @generated
	 */
	EOperation getDischargeSlot__GetSlotOrDelegateMaxCv();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getSlotOrDelegateDeliveryType() <em>Get Slot Or Delegate Delivery Type</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Delegate Delivery Type</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.DischargeSlot#getSlotOrDelegateDeliveryType()
	 * @generated
	 */
	EOperation getDischargeSlot__GetSlotOrDelegateDeliveryType();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getSlotOrDelegateFOBSaleDealType() <em>Get Slot Or Delegate FOB Sale Deal Type</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Delegate FOB Sale Deal Type</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.DischargeSlot#getSlotOrDelegateFOBSaleDealType()
	 * @generated
	 */
	EOperation getDischargeSlot__GetSlotOrDelegateFOBSaleDealType();

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
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.cargo.CargoModel#getInventoryModels <em>Inventory Models</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Inventory Models</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CargoModel#getInventoryModels()
	 * @see #getCargoModel()
	 * @generated
	 */
	EReference getCargoModel_InventoryModels();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.cargo.CargoModel#getCanalBookings <em>Canal Bookings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Canal Bookings</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CargoModel#getCanalBookings()
	 * @see #getCargoModel()
	 * @generated
	 */
	EReference getCargoModel_CanalBookings();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.cargo.CargoModel#getCharterInMarketOverrides <em>Charter In Market Overrides</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Charter In Market Overrides</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CargoModel#getCharterInMarketOverrides()
	 * @see #getCargoModel()
	 * @generated
	 */
	EReference getCargoModel_CharterInMarketOverrides();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.cargo.CargoModel#getPaperDeals <em>Paper Deals</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Paper Deals</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CargoModel#getPaperDeals()
	 * @see #getCargoModel()
	 * @generated
	 */
	EReference getCargoModel_PaperDeals();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.cargo.CargoModel#getDealSets <em>Deal Sets</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Deal Sets</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CargoModel#getDealSets()
	 * @see #getCargoModel()
	 * @generated
	 */
	EReference getCargoModel_DealSets();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#isFleet <em>Fleet</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fleet</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselAvailability#isFleet()
	 * @see #getVesselAvailability()
	 * @generated
	 */
	EAttribute getVesselAvailability_Fleet();

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
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getStartAt <em>Start At</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Start At</em>'.
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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#isForceHireCostOnlyEndRule <em>Force Hire Cost Only End Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Force Hire Cost Only End Rule</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselAvailability#isForceHireCostOnlyEndRule()
	 * @see #getVesselAvailability()
	 * @generated
	 */
	EAttribute getVesselAvailability_ForceHireCostOnlyEndRule();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#isOptional <em>Optional</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Optional</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselAvailability#isOptional()
	 * @see #getVesselAvailability()
	 * @generated
	 */
	EAttribute getVesselAvailability_Optional();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getRepositioningFee <em>Repositioning Fee</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Repositioning Fee</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselAvailability#getRepositioningFee()
	 * @see #getVesselAvailability()
	 * @generated
	 */
	EAttribute getVesselAvailability_RepositioningFee();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getBallastBonusContract <em>Ballast Bonus Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Ballast Bonus Contract</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselAvailability#getBallastBonusContract()
	 * @see #getVesselAvailability()
	 * @generated
	 */
	EReference getVesselAvailability_BallastBonusContract();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getCharterNumber <em>Charter Number</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Charter Number</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselAvailability#getCharterNumber()
	 * @see #getVesselAvailability()
	 * @generated
	 */
	EAttribute getVesselAvailability_CharterNumber();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getCharterContract <em>Charter Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Charter Contract</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselAvailability#getCharterContract()
	 * @see #getVesselAvailability()
	 * @generated
	 */
	EReference getVesselAvailability_CharterContract();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getMinDuration <em>Min Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Duration</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselAvailability#getMinDuration()
	 * @see #getVesselAvailability()
	 * @generated
	 */
	EAttribute getVesselAvailability_MinDuration();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getMaxDuration <em>Max Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Duration</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselAvailability#getMaxDuration()
	 * @see #getVesselAvailability()
	 * @generated
	 */
	EAttribute getVesselAvailability_MaxDuration();

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
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getCharterOrDelegateBallastBonusContract() <em>Get Charter Or Delegate Ballast Bonus Contract</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Charter Or Delegate Ballast Bonus Contract</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.VesselAvailability#getCharterOrDelegateBallastBonusContract()
	 * @generated
	 */
	EOperation getVesselAvailability__GetCharterOrDelegateBallastBonusContract();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getCharterOrDelegateMinDuration() <em>Get Charter Or Delegate Min Duration</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Charter Or Delegate Min Duration</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.VesselAvailability#getCharterOrDelegateMinDuration()
	 * @generated
	 */
	EOperation getVesselAvailability__GetCharterOrDelegateMinDuration();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getCharterOrDelegateMaxDuration() <em>Get Charter Or Delegate Max Duration</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Charter Or Delegate Max Duration</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.VesselAvailability#getCharterOrDelegateMaxDuration()
	 * @generated
	 */
	EOperation getVesselAvailability__GetCharterOrDelegateMaxDuration();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getCharterOrDelegateEntity() <em>Get Charter Or Delegate Entity</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Charter Or Delegate Entity</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.VesselAvailability#getCharterOrDelegateEntity()
	 * @generated
	 */
	EOperation getVesselAvailability__GetCharterOrDelegateEntity();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getCharterOrDelegateRepositioningFee() <em>Get Charter Or Delegate Repositioning Fee</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Charter Or Delegate Repositioning Fee</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.VesselAvailability#getCharterOrDelegateRepositioningFee()
	 * @generated
	 */
	EOperation getVesselAvailability__GetCharterOrDelegateRepositioningFee();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#jsonid() <em>Jsonid</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Jsonid</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.VesselAvailability#jsonid()
	 * @generated
	 */
	EOperation getVesselAvailability__Jsonid();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.CharterOutEvent#isOptional <em>Optional</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Optional</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CharterOutEvent#isOptional()
	 * @see #getCharterOutEvent()
	 * @generated
	 */
	EAttribute getCharterOutEvent_Optional();

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
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.cargo.CharterOutEvent#getRequiredHeel <em>Required Heel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Required Heel</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CharterOutEvent#getRequiredHeel()
	 * @see #getCharterOutEvent()
	 * @generated
	 */
	EReference getCharterOutEvent_RequiredHeel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.cargo.CharterOutEvent#getAvailableHeel <em>Available Heel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Available Heel</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CharterOutEvent#getAvailableHeel()
	 * @see #getCharterOutEvent()
	 * @generated
	 */
	EReference getCharterOutEvent_AvailableHeel();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.CharterOutEvent#getBallastBonus <em>Ballast Bonus</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ballast Bonus</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CharterOutEvent#getBallastBonus()
	 * @see #getCharterOutEvent()
	 * @generated
	 */
	EAttribute getCharterOutEvent_BallastBonus();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.EndHeelOptions#getTankState <em>Tank State</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Tank State</em>'.
	 * @see com.mmxlabs.models.lng.cargo.EndHeelOptions#getTankState()
	 * @see #getEndHeelOptions()
	 * @generated
	 */
	EAttribute getEndHeelOptions_TankState();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.EndHeelOptions#getMinimumEndHeel <em>Minimum End Heel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Minimum End Heel</em>'.
	 * @see com.mmxlabs.models.lng.cargo.EndHeelOptions#getMinimumEndHeel()
	 * @see #getEndHeelOptions()
	 * @generated
	 */
	EAttribute getEndHeelOptions_MinimumEndHeel();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.EndHeelOptions#getMaximumEndHeel <em>Maximum End Heel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Maximum End Heel</em>'.
	 * @see com.mmxlabs.models.lng.cargo.EndHeelOptions#getMaximumEndHeel()
	 * @see #getEndHeelOptions()
	 * @generated
	 */
	EAttribute getEndHeelOptions_MaximumEndHeel();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.EndHeelOptions#isUseLastHeelPrice <em>Use Last Heel Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Use Last Heel Price</em>'.
	 * @see com.mmxlabs.models.lng.cargo.EndHeelOptions#isUseLastHeelPrice()
	 * @see #getEndHeelOptions()
	 * @generated
	 */
	EAttribute getEndHeelOptions_UseLastHeelPrice();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.EndHeelOptions#getPriceExpression <em>Price Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price Expression</em>'.
	 * @see com.mmxlabs.models.lng.cargo.EndHeelOptions#getPriceExpression()
	 * @see #getEndHeelOptions()
	 * @generated
	 */
	EAttribute getEndHeelOptions_PriceExpression();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.StartHeelOptions <em>Start Heel Options</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Start Heel Options</em>'.
	 * @see com.mmxlabs.models.lng.cargo.StartHeelOptions
	 * @generated
	 */
	EClass getStartHeelOptions();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.StartHeelOptions#getCvValue <em>Cv Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cv Value</em>'.
	 * @see com.mmxlabs.models.lng.cargo.StartHeelOptions#getCvValue()
	 * @see #getStartHeelOptions()
	 * @generated
	 */
	EAttribute getStartHeelOptions_CvValue();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.StartHeelOptions#getMinVolumeAvailable <em>Min Volume Available</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Volume Available</em>'.
	 * @see com.mmxlabs.models.lng.cargo.StartHeelOptions#getMinVolumeAvailable()
	 * @see #getStartHeelOptions()
	 * @generated
	 */
	EAttribute getStartHeelOptions_MinVolumeAvailable();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.StartHeelOptions#getMaxVolumeAvailable <em>Max Volume Available</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Volume Available</em>'.
	 * @see com.mmxlabs.models.lng.cargo.StartHeelOptions#getMaxVolumeAvailable()
	 * @see #getStartHeelOptions()
	 * @generated
	 */
	EAttribute getStartHeelOptions_MaxVolumeAvailable();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.StartHeelOptions#getPriceExpression <em>Price Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price Expression</em>'.
	 * @see com.mmxlabs.models.lng.cargo.StartHeelOptions#getPriceExpression()
	 * @see #getStartHeelOptions()
	 * @generated
	 */
	EAttribute getStartHeelOptions_PriceExpression();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.InventoryEventRow <em>Inventory Event Row</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Inventory Event Row</em>'.
	 * @see com.mmxlabs.models.lng.cargo.InventoryEventRow
	 * @generated
	 */
	EClass getInventoryEventRow();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getStartDate <em>Start Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start Date</em>'.
	 * @see com.mmxlabs.models.lng.cargo.InventoryEventRow#getStartDate()
	 * @see #getInventoryEventRow()
	 * @generated
	 */
	EAttribute getInventoryEventRow_StartDate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getEndDate <em>End Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End Date</em>'.
	 * @see com.mmxlabs.models.lng.cargo.InventoryEventRow#getEndDate()
	 * @see #getInventoryEventRow()
	 * @generated
	 */
	EAttribute getInventoryEventRow_EndDate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getPeriod <em>Period</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Period</em>'.
	 * @see com.mmxlabs.models.lng.cargo.InventoryEventRow#getPeriod()
	 * @see #getInventoryEventRow()
	 * @generated
	 */
	EAttribute getInventoryEventRow_Period();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getCounterParty <em>Counter Party</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Counter Party</em>'.
	 * @see com.mmxlabs.models.lng.cargo.InventoryEventRow#getCounterParty()
	 * @see #getInventoryEventRow()
	 * @generated
	 */
	EAttribute getInventoryEventRow_CounterParty();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getReliability <em>Reliability</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Reliability</em>'.
	 * @see com.mmxlabs.models.lng.cargo.InventoryEventRow#getReliability()
	 * @see #getInventoryEventRow()
	 * @generated
	 */
	EAttribute getInventoryEventRow_Reliability();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getVolume <em>Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volume</em>'.
	 * @see com.mmxlabs.models.lng.cargo.InventoryEventRow#getVolume()
	 * @see #getInventoryEventRow()
	 * @generated
	 */
	EAttribute getInventoryEventRow_Volume();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getForecastDate <em>Forecast Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Forecast Date</em>'.
	 * @see com.mmxlabs.models.lng.cargo.InventoryEventRow#getForecastDate()
	 * @see #getInventoryEventRow()
	 * @generated
	 */
	EAttribute getInventoryEventRow_ForecastDate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getVolumeLow <em>Volume Low</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volume Low</em>'.
	 * @see com.mmxlabs.models.lng.cargo.InventoryEventRow#getVolumeLow()
	 * @see #getInventoryEventRow()
	 * @generated
	 */
	EAttribute getInventoryEventRow_VolumeLow();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getVolumeHigh <em>Volume High</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volume High</em>'.
	 * @see com.mmxlabs.models.lng.cargo.InventoryEventRow#getVolumeHigh()
	 * @see #getInventoryEventRow()
	 * @generated
	 */
	EAttribute getInventoryEventRow_VolumeHigh();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getReliableVolume() <em>Get Reliable Volume</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Reliable Volume</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.InventoryEventRow#getReliableVolume()
	 * @generated
	 */
	EOperation getInventoryEventRow__GetReliableVolume();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.InventoryCapacityRow <em>Inventory Capacity Row</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Inventory Capacity Row</em>'.
	 * @see com.mmxlabs.models.lng.cargo.InventoryCapacityRow
	 * @generated
	 */
	EClass getInventoryCapacityRow();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.InventoryCapacityRow#getDate <em>Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Date</em>'.
	 * @see com.mmxlabs.models.lng.cargo.InventoryCapacityRow#getDate()
	 * @see #getInventoryCapacityRow()
	 * @generated
	 */
	EAttribute getInventoryCapacityRow_Date();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.InventoryCapacityRow#getMinVolume <em>Min Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Volume</em>'.
	 * @see com.mmxlabs.models.lng.cargo.InventoryCapacityRow#getMinVolume()
	 * @see #getInventoryCapacityRow()
	 * @generated
	 */
	EAttribute getInventoryCapacityRow_MinVolume();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.InventoryCapacityRow#getMaxVolume <em>Max Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Volume</em>'.
	 * @see com.mmxlabs.models.lng.cargo.InventoryCapacityRow#getMaxVolume()
	 * @see #getInventoryCapacityRow()
	 * @generated
	 */
	EAttribute getInventoryCapacityRow_MaxVolume();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.Inventory <em>Inventory</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Inventory</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Inventory
	 * @generated
	 */
	EClass getInventory();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.cargo.Inventory#getFeeds <em>Feeds</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Feeds</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Inventory#getFeeds()
	 * @see #getInventory()
	 * @generated
	 */
	EReference getInventory_Feeds();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.cargo.Inventory#getOfftakes <em>Offtakes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Offtakes</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Inventory#getOfftakes()
	 * @see #getInventory()
	 * @generated
	 */
	EReference getInventory_Offtakes();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.cargo.Inventory#getCapacities <em>Capacities</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Capacities</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Inventory#getCapacities()
	 * @see #getInventory()
	 * @generated
	 */
	EReference getInventory_Capacities();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.Inventory#getPort <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Port</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Inventory#getPort()
	 * @see #getInventory()
	 * @generated
	 */
	EReference getInventory_Port();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.CanalBookingSlot <em>Canal Booking Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Canal Booking Slot</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CanalBookingSlot
	 * @generated
	 */
	EClass getCanalBookingSlot();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.CanalBookingSlot#getRouteOption <em>Route Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Route Option</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CanalBookingSlot#getRouteOption()
	 * @see #getCanalBookingSlot()
	 * @generated
	 */
	EAttribute getCanalBookingSlot_RouteOption();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.CanalBookingSlot#getBookingDate <em>Booking Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Booking Date</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CanalBookingSlot#getBookingDate()
	 * @see #getCanalBookingSlot()
	 * @generated
	 */
	EAttribute getCanalBookingSlot_BookingDate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.CanalBookingSlot#getCanalEntrance <em>Canal Entrance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Canal Entrance</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CanalBookingSlot#getCanalEntrance()
	 * @see #getCanalBookingSlot()
	 * @generated
	 */
	EAttribute getCanalBookingSlot_CanalEntrance();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.CanalBookingSlot#getSlot <em>Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Slot</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CanalBookingSlot#getSlot()
	 * @see #getCanalBookingSlot()
	 * @generated
	 */
	EReference getCanalBookingSlot_Slot();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.CanalBookingSlot#getNotes <em>Notes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Notes</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CanalBookingSlot#getNotes()
	 * @see #getCanalBookingSlot()
	 * @generated
	 */
	EAttribute getCanalBookingSlot_Notes();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.CanalBookings <em>Canal Bookings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Canal Bookings</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CanalBookings
	 * @generated
	 */
	EClass getCanalBookings();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.cargo.CanalBookings#getCanalBookingSlots <em>Canal Booking Slots</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Canal Booking Slots</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CanalBookings#getCanalBookingSlots()
	 * @see #getCanalBookings()
	 * @generated
	 */
	EReference getCanalBookings_CanalBookingSlots();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.CanalBookings#getStrictBoundaryOffsetDays <em>Strict Boundary Offset Days</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Strict Boundary Offset Days</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CanalBookings#getStrictBoundaryOffsetDays()
	 * @see #getCanalBookings()
	 * @generated
	 */
	EAttribute getCanalBookings_StrictBoundaryOffsetDays();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.CanalBookings#getRelaxedBoundaryOffsetDays <em>Relaxed Boundary Offset Days</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Relaxed Boundary Offset Days</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CanalBookings#getRelaxedBoundaryOffsetDays()
	 * @see #getCanalBookings()
	 * @generated
	 */
	EAttribute getCanalBookings_RelaxedBoundaryOffsetDays();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.CanalBookings#getArrivalMarginHours <em>Arrival Margin Hours</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Arrival Margin Hours</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CanalBookings#getArrivalMarginHours()
	 * @see #getCanalBookings()
	 * @generated
	 */
	EAttribute getCanalBookings_ArrivalMarginHours();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.CanalBookings#getFlexibleBookingAmountNorthbound <em>Flexible Booking Amount Northbound</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Flexible Booking Amount Northbound</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CanalBookings#getFlexibleBookingAmountNorthbound()
	 * @see #getCanalBookings()
	 * @generated
	 */
	EAttribute getCanalBookings_FlexibleBookingAmountNorthbound();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.CanalBookings#getFlexibleBookingAmountSouthbound <em>Flexible Booking Amount Southbound</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Flexible Booking Amount Southbound</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CanalBookings#getFlexibleBookingAmountSouthbound()
	 * @see #getCanalBookings()
	 * @generated
	 */
	EAttribute getCanalBookings_FlexibleBookingAmountSouthbound();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.CanalBookings#getNorthboundMaxIdleDays <em>Northbound Max Idle Days</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Northbound Max Idle Days</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CanalBookings#getNorthboundMaxIdleDays()
	 * @see #getCanalBookings()
	 * @generated
	 */
	EAttribute getCanalBookings_NorthboundMaxIdleDays();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.ScheduleSpecification <em>Schedule Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Schedule Specification</em>'.
	 * @see com.mmxlabs.models.lng.cargo.ScheduleSpecification
	 * @generated
	 */
	EClass getScheduleSpecification();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.cargo.ScheduleSpecification#getVesselScheduleSpecifications <em>Vessel Schedule Specifications</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Vessel Schedule Specifications</em>'.
	 * @see com.mmxlabs.models.lng.cargo.ScheduleSpecification#getVesselScheduleSpecifications()
	 * @see #getScheduleSpecification()
	 * @generated
	 */
	EReference getScheduleSpecification_VesselScheduleSpecifications();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.cargo.ScheduleSpecification#getNonShippedCargoSpecifications <em>Non Shipped Cargo Specifications</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Non Shipped Cargo Specifications</em>'.
	 * @see com.mmxlabs.models.lng.cargo.ScheduleSpecification#getNonShippedCargoSpecifications()
	 * @see #getScheduleSpecification()
	 * @generated
	 */
	EReference getScheduleSpecification_NonShippedCargoSpecifications();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.cargo.ScheduleSpecification#getOpenEvents <em>Open Events</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Open Events</em>'.
	 * @see com.mmxlabs.models.lng.cargo.ScheduleSpecification#getOpenEvents()
	 * @see #getScheduleSpecification()
	 * @generated
	 */
	EReference getScheduleSpecification_OpenEvents();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.NonShippedCargoSpecification <em>Non Shipped Cargo Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Non Shipped Cargo Specification</em>'.
	 * @see com.mmxlabs.models.lng.cargo.NonShippedCargoSpecification
	 * @generated
	 */
	EClass getNonShippedCargoSpecification();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.cargo.NonShippedCargoSpecification#getSlotSpecifications <em>Slot Specifications</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Slot Specifications</em>'.
	 * @see com.mmxlabs.models.lng.cargo.NonShippedCargoSpecification#getSlotSpecifications()
	 * @see #getNonShippedCargoSpecification()
	 * @generated
	 */
	EReference getNonShippedCargoSpecification_SlotSpecifications();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.VesselScheduleSpecification <em>Vessel Schedule Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vessel Schedule Specification</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselScheduleSpecification
	 * @generated
	 */
	EClass getVesselScheduleSpecification();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.VesselScheduleSpecification#getVesselAllocation <em>Vessel Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel Allocation</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselScheduleSpecification#getVesselAllocation()
	 * @see #getVesselScheduleSpecification()
	 * @generated
	 */
	EReference getVesselScheduleSpecification_VesselAllocation();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.VesselScheduleSpecification#getSpotIndex <em>Spot Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Spot Index</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselScheduleSpecification#getSpotIndex()
	 * @see #getVesselScheduleSpecification()
	 * @generated
	 */
	EAttribute getVesselScheduleSpecification_SpotIndex();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.cargo.VesselScheduleSpecification#getEvents <em>Events</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Events</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselScheduleSpecification#getEvents()
	 * @see #getVesselScheduleSpecification()
	 * @generated
	 */
	EReference getVesselScheduleSpecification_Events();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.ScheduleSpecificationEvent <em>Schedule Specification Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Schedule Specification Event</em>'.
	 * @see com.mmxlabs.models.lng.cargo.ScheduleSpecificationEvent
	 * @generated
	 */
	EClass getScheduleSpecificationEvent();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.VesselEventSpecification <em>Vessel Event Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vessel Event Specification</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselEventSpecification
	 * @generated
	 */
	EClass getVesselEventSpecification();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.VesselEventSpecification#getVesselEvent <em>Vessel Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel Event</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VesselEventSpecification#getVesselEvent()
	 * @see #getVesselEventSpecification()
	 * @generated
	 */
	EReference getVesselEventSpecification_VesselEvent();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.VoyageSpecification <em>Voyage Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Voyage Specification</em>'.
	 * @see com.mmxlabs.models.lng.cargo.VoyageSpecification
	 * @generated
	 */
	EClass getVoyageSpecification();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.SlotSpecification <em>Slot Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Slot Specification</em>'.
	 * @see com.mmxlabs.models.lng.cargo.SlotSpecification
	 * @generated
	 */
	EClass getSlotSpecification();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.SlotSpecification#getSlot <em>Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Slot</em>'.
	 * @see com.mmxlabs.models.lng.cargo.SlotSpecification#getSlot()
	 * @see #getSlotSpecification()
	 * @generated
	 */
	EReference getSlotSpecification_Slot();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride <em>Charter In Market Override</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Charter In Market Override</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CharterInMarketOverride
	 * @generated
	 */
	EClass getCharterInMarketOverride();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getCharterInMarket <em>Charter In Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Charter In Market</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getCharterInMarket()
	 * @see #getCharterInMarketOverride()
	 * @generated
	 */
	EReference getCharterInMarketOverride_CharterInMarket();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getSpotIndex <em>Spot Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Spot Index</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getSpotIndex()
	 * @see #getCharterInMarketOverride()
	 * @generated
	 */
	EAttribute getCharterInMarketOverride_SpotIndex();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getStartHeel <em>Start Heel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Start Heel</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getStartHeel()
	 * @see #getCharterInMarketOverride()
	 * @generated
	 */
	EReference getCharterInMarketOverride_StartHeel();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getStartDate <em>Start Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start Date</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getStartDate()
	 * @see #getCharterInMarketOverride()
	 * @generated
	 */
	EAttribute getCharterInMarketOverride_StartDate();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getEndPort <em>End Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>End Port</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getEndPort()
	 * @see #getCharterInMarketOverride()
	 * @generated
	 */
	EReference getCharterInMarketOverride_EndPort();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getEndDate <em>End Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End Date</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getEndDate()
	 * @see #getCharterInMarketOverride()
	 * @generated
	 */
	EAttribute getCharterInMarketOverride_EndDate();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getEndHeel <em>End Heel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>End Heel</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getEndHeel()
	 * @see #getCharterInMarketOverride()
	 * @generated
	 */
	EReference getCharterInMarketOverride_EndHeel();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#isIncludeBallastBonus <em>Include Ballast Bonus</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Include Ballast Bonus</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CharterInMarketOverride#isIncludeBallastBonus()
	 * @see #getCharterInMarketOverride()
	 * @generated
	 */
	EAttribute getCharterInMarketOverride_IncludeBallastBonus();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getMinDuration <em>Min Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Duration</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getMinDuration()
	 * @see #getCharterInMarketOverride()
	 * @generated
	 */
	EAttribute getCharterInMarketOverride_MinDuration();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getMaxDuration <em>Max Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Duration</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getMaxDuration()
	 * @see #getCharterInMarketOverride()
	 * @generated
	 */
	EAttribute getCharterInMarketOverride_MaxDuration();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getStartDateAsDateTime() <em>Get Start Date As Date Time</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Start Date As Date Time</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getStartDateAsDateTime()
	 * @generated
	 */
	EOperation getCharterInMarketOverride__GetStartDateAsDateTime();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getEndDateAsDateTime() <em>Get End Date As Date Time</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get End Date As Date Time</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getEndDateAsDateTime()
	 * @generated
	 */
	EOperation getCharterInMarketOverride__GetEndDateAsDateTime();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getLocalOrDelegateMinDuration() <em>Get Local Or Delegate Min Duration</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Local Or Delegate Min Duration</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getLocalOrDelegateMinDuration()
	 * @generated
	 */
	EOperation getCharterInMarketOverride__GetLocalOrDelegateMinDuration();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getLocalOrDelegateMaxDuration() <em>Get Local Or Delegate Max Duration</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Local Or Delegate Max Duration</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getLocalOrDelegateMaxDuration()
	 * @generated
	 */
	EOperation getCharterInMarketOverride__GetLocalOrDelegateMaxDuration();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.PaperDeal <em>Paper Deal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Paper Deal</em>'.
	 * @see com.mmxlabs.models.lng.cargo.PaperDeal
	 * @generated
	 */
	EClass getPaperDeal();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.PaperDeal#getQuantity <em>Quantity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Quantity</em>'.
	 * @see com.mmxlabs.models.lng.cargo.PaperDeal#getQuantity()
	 * @see #getPaperDeal()
	 * @generated
	 */
	EAttribute getPaperDeal_Quantity();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.PaperDeal#getStartDate <em>Start Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start Date</em>'.
	 * @see com.mmxlabs.models.lng.cargo.PaperDeal#getStartDate()
	 * @see #getPaperDeal()
	 * @generated
	 */
	EAttribute getPaperDeal_StartDate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.PaperDeal#getEndDate <em>End Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End Date</em>'.
	 * @see com.mmxlabs.models.lng.cargo.PaperDeal#getEndDate()
	 * @see #getPaperDeal()
	 * @generated
	 */
	EAttribute getPaperDeal_EndDate();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.PaperDeal#getEntity <em>Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Entity</em>'.
	 * @see com.mmxlabs.models.lng.cargo.PaperDeal#getEntity()
	 * @see #getPaperDeal()
	 * @generated
	 */
	EReference getPaperDeal_Entity();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.PaperDeal#getYear <em>Year</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Year</em>'.
	 * @see com.mmxlabs.models.lng.cargo.PaperDeal#getYear()
	 * @see #getPaperDeal()
	 * @generated
	 */
	EAttribute getPaperDeal_Year();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.PaperDeal#getComment <em>Comment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Comment</em>'.
	 * @see com.mmxlabs.models.lng.cargo.PaperDeal#getComment()
	 * @see #getPaperDeal()
	 * @generated
	 */
	EAttribute getPaperDeal_Comment();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.PaperDeal#getInstrument <em>Instrument</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Instrument</em>'.
	 * @see com.mmxlabs.models.lng.cargo.PaperDeal#getInstrument()
	 * @see #getPaperDeal()
	 * @generated
	 */
	EReference getPaperDeal_Instrument();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.PaperDeal#getPrice <em>Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price</em>'.
	 * @see com.mmxlabs.models.lng.cargo.PaperDeal#getPrice()
	 * @see #getPaperDeal()
	 * @generated
	 */
	EAttribute getPaperDeal_Price();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.PaperDeal#getIndex <em>Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Index</em>'.
	 * @see com.mmxlabs.models.lng.cargo.PaperDeal#getIndex()
	 * @see #getPaperDeal()
	 * @generated
	 */
	EAttribute getPaperDeal_Index();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.BuyPaperDeal <em>Buy Paper Deal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Buy Paper Deal</em>'.
	 * @see com.mmxlabs.models.lng.cargo.BuyPaperDeal
	 * @generated
	 */
	EClass getBuyPaperDeal();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.SellPaperDeal <em>Sell Paper Deal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Sell Paper Deal</em>'.
	 * @see com.mmxlabs.models.lng.cargo.SellPaperDeal
	 * @generated
	 */
	EClass getSellPaperDeal();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.DealSet <em>Deal Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Deal Set</em>'.
	 * @see com.mmxlabs.models.lng.cargo.DealSet
	 * @generated
	 */
	EClass getDealSet();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.cargo.DealSet#getSlots <em>Slots</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Slots</em>'.
	 * @see com.mmxlabs.models.lng.cargo.DealSet#getSlots()
	 * @see #getDealSet()
	 * @generated
	 */
	EReference getDealSet_Slots();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.cargo.DealSet#getPaperDeals <em>Paper Deals</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Paper Deals</em>'.
	 * @see com.mmxlabs.models.lng.cargo.DealSet#getPaperDeals()
	 * @see #getDealSet()
	 * @generated
	 */
	EReference getDealSet_PaperDeals();

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
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.cargo.EVesselTankState <em>EVessel Tank State</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>EVessel Tank State</em>'.
	 * @see com.mmxlabs.models.lng.cargo.EVesselTankState
	 * @generated
	 */
	EEnum getEVesselTankState();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.cargo.InventoryFrequency <em>Inventory Frequency</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Inventory Frequency</em>'.
	 * @see com.mmxlabs.models.lng.cargo.InventoryFrequency
	 * @generated
	 */
	EEnum getInventoryFrequency();

	/**
	 * Returns the meta object for data type '{@link com.mmxlabs.models.lng.cargo.SchedulingTimeWindow <em>Scheduling Time Window</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Scheduling Time Window</em>'.
	 * @see com.mmxlabs.models.lng.cargo.SchedulingTimeWindow
	 * @model instanceClass="com.mmxlabs.models.lng.cargo.SchedulingTimeWindow"
	 * @generated
	 */
	EDataType getSchedulingTimeWindow();

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
		 * The meta object literal for the '<em><b>Counterparty</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__COUNTERPARTY = eINSTANCE.getSlot_Counterparty();

		/**
		 * The meta object literal for the '<em><b>Cn</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__CN = eINSTANCE.getSlot_Cn();

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
		 * The meta object literal for the '<em><b>Operational Tolerance</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__OPERATIONAL_TOLERANCE = eINSTANCE.getSlot_OperationalTolerance();

		/**
		 * The meta object literal for the '<em><b>Full Cargo Lot</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__FULL_CARGO_LOT = eINSTANCE.getSlot_FullCargoLot();

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
		 * The meta object literal for the '<em><b>Restricted Contracts Are Permissive</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE = eINSTANCE.getSlot_RestrictedContractsArePermissive();

		/**
		 * The meta object literal for the '<em><b>Restricted Contracts Override</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__RESTRICTED_CONTRACTS_OVERRIDE = eINSTANCE.getSlot_RestrictedContractsOverride();

		/**
		 * The meta object literal for the '<em><b>Restricted Ports</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT__RESTRICTED_PORTS = eINSTANCE.getSlot_RestrictedPorts();

		/**
		 * The meta object literal for the '<em><b>Restricted Ports Are Permissive</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__RESTRICTED_PORTS_ARE_PERMISSIVE = eINSTANCE.getSlot_RestrictedPortsArePermissive();

		/**
		 * The meta object literal for the '<em><b>Restricted Ports Override</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__RESTRICTED_PORTS_OVERRIDE = eINSTANCE.getSlot_RestrictedPortsOverride();

		/**
		 * The meta object literal for the '<em><b>Restricted Slots</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT__RESTRICTED_SLOTS = eINSTANCE.getSlot_RestrictedSlots();

		/**
		 * The meta object literal for the '<em><b>Restricted Slots Are Permissive</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__RESTRICTED_SLOTS_ARE_PERMISSIVE = eINSTANCE.getSlot_RestrictedSlotsArePermissive();

		/**
		 * The meta object literal for the '<em><b>Restricted Vessels</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT__RESTRICTED_VESSELS = eINSTANCE.getSlot_RestrictedVessels();

		/**
		 * The meta object literal for the '<em><b>Restricted Vessels Are Permissive</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__RESTRICTED_VESSELS_ARE_PERMISSIVE = eINSTANCE.getSlot_RestrictedVesselsArePermissive();

		/**
		 * The meta object literal for the '<em><b>Restricted Vessels Override</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__RESTRICTED_VESSELS_OVERRIDE = eINSTANCE.getSlot_RestrictedVesselsOverride();

		/**
		 * The meta object literal for the '<em><b>Hedges</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__HEDGES = eINSTANCE.getSlot_Hedges();

		/**
		 * The meta object literal for the '<em><b>Misc Costs</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__MISC_COSTS = eINSTANCE.getSlot_MiscCosts();

		/**
		 * The meta object literal for the '<em><b>Cancellation Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__CANCELLATION_EXPRESSION = eINSTANCE.getSlot_CancellationExpression();

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
		 * The meta object literal for the '<em><b>Cancelled</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__CANCELLED = eINSTANCE.getSlot_Cancelled();

		/**
		 * The meta object literal for the '<em><b>Window Counter Party</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__WINDOW_COUNTER_PARTY = eINSTANCE.getSlot_WindowCounterParty();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Delegate Min Quantity</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_DELEGATE_MIN_QUANTITY = eINSTANCE.getSlot__GetSlotOrDelegateMinQuantity();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Delegate Max Quantity</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_DELEGATE_MAX_QUANTITY = eINSTANCE.getSlot__GetSlotOrDelegateMaxQuantity();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Delegate Operational Tolerance</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_DELEGATE_OPERATIONAL_TOLERANCE = eINSTANCE.getSlot__GetSlotOrDelegateOperationalTolerance();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Delegate Volume Limits Unit</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_DELEGATE_VOLUME_LIMITS_UNIT = eINSTANCE.getSlot__GetSlotOrDelegateVolumeLimitsUnit();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Delegate Entity</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_DELEGATE_ENTITY = eINSTANCE.getSlot__GetSlotOrDelegateEntity();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Delegate Cancellation Expression</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_DELEGATE_CANCELLATION_EXPRESSION = eINSTANCE.getSlot__GetSlotOrDelegateCancellationExpression();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Delegate Pricing Event</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_DELEGATE_PRICING_EVENT = eINSTANCE.getSlot__GetSlotOrDelegatePricingEvent();

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
		 * The meta object literal for the '<em><b>Get Slot Or Delegate Counterparty</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_DELEGATE_COUNTERPARTY = eINSTANCE.getSlot__GetSlotOrDelegateCounterparty();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Delegate CN</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_DELEGATE_CN = eINSTANCE.getSlot__GetSlotOrDelegateCN();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Delegate Shipping Days Restriction</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_DELEGATE_SHIPPING_DAYS_RESTRICTION = eINSTANCE.getSlot__GetSlotOrDelegateShippingDaysRestriction();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Delegate Full Cargo Lot</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_DELEGATE_FULL_CARGO_LOT = eINSTANCE.getSlot__GetSlotOrDelegateFullCargoLot();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Delegate Contract Restrictions Are Permissive</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_DELEGATE_CONTRACT_RESTRICTIONS_ARE_PERMISSIVE = eINSTANCE.getSlot__GetSlotOrDelegateContractRestrictionsArePermissive();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Delegate Port Restrictions Are Permissive</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_DELEGATE_PORT_RESTRICTIONS_ARE_PERMISSIVE = eINSTANCE.getSlot__GetSlotOrDelegatePortRestrictionsArePermissive();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Delegate Vessel Restrictions Are Permissive</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_DELEGATE_VESSEL_RESTRICTIONS_ARE_PERMISSIVE = eINSTANCE.getSlot__GetSlotOrDelegateVesselRestrictionsArePermissive();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Delegate Contract Restrictions</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_DELEGATE_CONTRACT_RESTRICTIONS = eINSTANCE.getSlot__GetSlotOrDelegateContractRestrictions();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Delegate Port Restrictions</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_DELEGATE_PORT_RESTRICTIONS = eINSTANCE.getSlot__GetSlotOrDelegatePortRestrictions();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Delegate Vessel Restrictions</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_DELEGATE_VESSEL_RESTRICTIONS = eINSTANCE.getSlot__GetSlotOrDelegateVesselRestrictions();

		/**
		 * The meta object literal for the '<em><b>Get Scheduling Time Window</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SCHEDULING_TIME_WINDOW = eINSTANCE.getSlot__GetSchedulingTimeWindow();

		/**
		 * The meta object literal for the '<em><b>Get Days Buffer</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_DAYS_BUFFER = eINSTANCE.getSlot__GetDaysBuffer();

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
		 * The meta object literal for the '<em><b>Schedule Purge</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOAD_SLOT__SCHEDULE_PURGE = eINSTANCE.getLoadSlot_SchedulePurge();

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
		 * The meta object literal for the '<em><b>Des Purchase Deal Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOAD_SLOT__DES_PURCHASE_DEAL_TYPE = eINSTANCE.getLoadSlot_DesPurchaseDealType();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Delegate CV</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation LOAD_SLOT___GET_SLOT_OR_DELEGATE_CV = eINSTANCE.getLoadSlot__GetSlotOrDelegateCV();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Delegate Delivery Type</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation LOAD_SLOT___GET_SLOT_OR_DELEGATE_DELIVERY_TYPE = eINSTANCE.getLoadSlot__GetSlotOrDelegateDeliveryType();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Delegate DES Purchase Deal Type</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation LOAD_SLOT___GET_SLOT_OR_DELEGATE_DES_PURCHASE_DEAL_TYPE = eINSTANCE.getLoadSlot__GetSlotOrDelegateDESPurchaseDealType();

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
		 * The meta object literal for the '<em><b>Fob Sale Deal Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DISCHARGE_SLOT__FOB_SALE_DEAL_TYPE = eINSTANCE.getDischargeSlot_FobSaleDealType();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Delegate Min Cv</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_MIN_CV = eINSTANCE.getDischargeSlot__GetSlotOrDelegateMinCv();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Delegate Max Cv</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_MAX_CV = eINSTANCE.getDischargeSlot__GetSlotOrDelegateMaxCv();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Delegate Delivery Type</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_DELIVERY_TYPE = eINSTANCE.getDischargeSlot__GetSlotOrDelegateDeliveryType();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Delegate FOB Sale Deal Type</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DISCHARGE_SLOT___GET_SLOT_OR_DELEGATE_FOB_SALE_DEAL_TYPE = eINSTANCE.getDischargeSlot__GetSlotOrDelegateFOBSaleDealType();

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
		 * The meta object literal for the '<em><b>Inventory Models</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_MODEL__INVENTORY_MODELS = eINSTANCE.getCargoModel_InventoryModels();

		/**
		 * The meta object literal for the '<em><b>Canal Bookings</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_MODEL__CANAL_BOOKINGS = eINSTANCE.getCargoModel_CanalBookings();

		/**
		 * The meta object literal for the '<em><b>Charter In Market Overrides</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_MODEL__CHARTER_IN_MARKET_OVERRIDES = eINSTANCE.getCargoModel_CharterInMarketOverrides();

		/**
		 * The meta object literal for the '<em><b>Paper Deals</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_MODEL__PAPER_DEALS = eINSTANCE.getCargoModel_PaperDeals();

		/**
		 * The meta object literal for the '<em><b>Deal Sets</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_MODEL__DEAL_SETS = eINSTANCE.getCargoModel_DealSets();

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
		 * The meta object literal for the '<em><b>Fleet</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_AVAILABILITY__FLEET = eINSTANCE.getVesselAvailability_Fleet();

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
		 * The meta object literal for the '<em><b>Start At</b></em>' reference feature.
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
		 * The meta object literal for the '<em><b>Force Hire Cost Only End Rule</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_AVAILABILITY__FORCE_HIRE_COST_ONLY_END_RULE = eINSTANCE.getVesselAvailability_ForceHireCostOnlyEndRule();

		/**
		 * The meta object literal for the '<em><b>Optional</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_AVAILABILITY__OPTIONAL = eINSTANCE.getVesselAvailability_Optional();

		/**
		 * The meta object literal for the '<em><b>Repositioning Fee</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_AVAILABILITY__REPOSITIONING_FEE = eINSTANCE.getVesselAvailability_RepositioningFee();

		/**
		 * The meta object literal for the '<em><b>Ballast Bonus Contract</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_AVAILABILITY__BALLAST_BONUS_CONTRACT = eINSTANCE.getVesselAvailability_BallastBonusContract();

		/**
		 * The meta object literal for the '<em><b>Charter Number</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_AVAILABILITY__CHARTER_NUMBER = eINSTANCE.getVesselAvailability_CharterNumber();

		/**
		 * The meta object literal for the '<em><b>Charter Contract</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_AVAILABILITY__CHARTER_CONTRACT = eINSTANCE.getVesselAvailability_CharterContract();

		/**
		 * The meta object literal for the '<em><b>Min Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_AVAILABILITY__MIN_DURATION = eINSTANCE.getVesselAvailability_MinDuration();

		/**
		 * The meta object literal for the '<em><b>Max Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_AVAILABILITY__MAX_DURATION = eINSTANCE.getVesselAvailability_MaxDuration();

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
		 * The meta object literal for the '<em><b>Get Charter Or Delegate Ballast Bonus Contract</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation VESSEL_AVAILABILITY___GET_CHARTER_OR_DELEGATE_BALLAST_BONUS_CONTRACT = eINSTANCE.getVesselAvailability__GetCharterOrDelegateBallastBonusContract();

		/**
		 * The meta object literal for the '<em><b>Get Charter Or Delegate Min Duration</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation VESSEL_AVAILABILITY___GET_CHARTER_OR_DELEGATE_MIN_DURATION = eINSTANCE.getVesselAvailability__GetCharterOrDelegateMinDuration();

		/**
		 * The meta object literal for the '<em><b>Get Charter Or Delegate Max Duration</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation VESSEL_AVAILABILITY___GET_CHARTER_OR_DELEGATE_MAX_DURATION = eINSTANCE.getVesselAvailability__GetCharterOrDelegateMaxDuration();

		/**
		 * The meta object literal for the '<em><b>Get Charter Or Delegate Entity</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation VESSEL_AVAILABILITY___GET_CHARTER_OR_DELEGATE_ENTITY = eINSTANCE.getVesselAvailability__GetCharterOrDelegateEntity();

		/**
		 * The meta object literal for the '<em><b>Get Charter Or Delegate Repositioning Fee</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation VESSEL_AVAILABILITY___GET_CHARTER_OR_DELEGATE_REPOSITIONING_FEE = eINSTANCE.getVesselAvailability__GetCharterOrDelegateRepositioningFee();

		/**
		 * The meta object literal for the '<em><b>Jsonid</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation VESSEL_AVAILABILITY___JSONID = eINSTANCE.getVesselAvailability__Jsonid();

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
		 * The meta object literal for the '<em><b>Optional</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHARTER_OUT_EVENT__OPTIONAL = eINSTANCE.getCharterOutEvent_Optional();

		/**
		 * The meta object literal for the '<em><b>Relocate To</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHARTER_OUT_EVENT__RELOCATE_TO = eINSTANCE.getCharterOutEvent_RelocateTo();

		/**
		 * The meta object literal for the '<em><b>Repositioning Fee</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHARTER_OUT_EVENT__REPOSITIONING_FEE = eINSTANCE.getCharterOutEvent_RepositioningFee();

		/**
		 * The meta object literal for the '<em><b>Required Heel</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHARTER_OUT_EVENT__REQUIRED_HEEL = eINSTANCE.getCharterOutEvent_RequiredHeel();

		/**
		 * The meta object literal for the '<em><b>Available Heel</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHARTER_OUT_EVENT__AVAILABLE_HEEL = eINSTANCE.getCharterOutEvent_AvailableHeel();

		/**
		 * The meta object literal for the '<em><b>Hire Rate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHARTER_OUT_EVENT__HIRE_RATE = eINSTANCE.getCharterOutEvent_HireRate();

		/**
		 * The meta object literal for the '<em><b>Ballast Bonus</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHARTER_OUT_EVENT__BALLAST_BONUS = eINSTANCE.getCharterOutEvent_BallastBonus();

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
		 * The meta object literal for the '<em><b>Tank State</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute END_HEEL_OPTIONS__TANK_STATE = eINSTANCE.getEndHeelOptions_TankState();

		/**
		 * The meta object literal for the '<em><b>Minimum End Heel</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute END_HEEL_OPTIONS__MINIMUM_END_HEEL = eINSTANCE.getEndHeelOptions_MinimumEndHeel();

		/**
		 * The meta object literal for the '<em><b>Maximum End Heel</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute END_HEEL_OPTIONS__MAXIMUM_END_HEEL = eINSTANCE.getEndHeelOptions_MaximumEndHeel();

		/**
		 * The meta object literal for the '<em><b>Use Last Heel Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute END_HEEL_OPTIONS__USE_LAST_HEEL_PRICE = eINSTANCE.getEndHeelOptions_UseLastHeelPrice();

		/**
		 * The meta object literal for the '<em><b>Price Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute END_HEEL_OPTIONS__PRICE_EXPRESSION = eINSTANCE.getEndHeelOptions_PriceExpression();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.StartHeelOptionsImpl <em>Start Heel Options</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.StartHeelOptionsImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getStartHeelOptions()
		 * @generated
		 */
		EClass START_HEEL_OPTIONS = eINSTANCE.getStartHeelOptions();

		/**
		 * The meta object literal for the '<em><b>Cv Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute START_HEEL_OPTIONS__CV_VALUE = eINSTANCE.getStartHeelOptions_CvValue();

		/**
		 * The meta object literal for the '<em><b>Min Volume Available</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute START_HEEL_OPTIONS__MIN_VOLUME_AVAILABLE = eINSTANCE.getStartHeelOptions_MinVolumeAvailable();

		/**
		 * The meta object literal for the '<em><b>Max Volume Available</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute START_HEEL_OPTIONS__MAX_VOLUME_AVAILABLE = eINSTANCE.getStartHeelOptions_MaxVolumeAvailable();

		/**
		 * The meta object literal for the '<em><b>Price Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute START_HEEL_OPTIONS__PRICE_EXPRESSION = eINSTANCE.getStartHeelOptions_PriceExpression();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.InventoryEventRowImpl <em>Inventory Event Row</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.InventoryEventRowImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getInventoryEventRow()
		 * @generated
		 */
		EClass INVENTORY_EVENT_ROW = eINSTANCE.getInventoryEventRow();

		/**
		 * The meta object literal for the '<em><b>Start Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INVENTORY_EVENT_ROW__START_DATE = eINSTANCE.getInventoryEventRow_StartDate();

		/**
		 * The meta object literal for the '<em><b>End Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INVENTORY_EVENT_ROW__END_DATE = eINSTANCE.getInventoryEventRow_EndDate();

		/**
		 * The meta object literal for the '<em><b>Period</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INVENTORY_EVENT_ROW__PERIOD = eINSTANCE.getInventoryEventRow_Period();

		/**
		 * The meta object literal for the '<em><b>Counter Party</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INVENTORY_EVENT_ROW__COUNTER_PARTY = eINSTANCE.getInventoryEventRow_CounterParty();

		/**
		 * The meta object literal for the '<em><b>Reliability</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INVENTORY_EVENT_ROW__RELIABILITY = eINSTANCE.getInventoryEventRow_Reliability();

		/**
		 * The meta object literal for the '<em><b>Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INVENTORY_EVENT_ROW__VOLUME = eINSTANCE.getInventoryEventRow_Volume();

		/**
		 * The meta object literal for the '<em><b>Forecast Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INVENTORY_EVENT_ROW__FORECAST_DATE = eINSTANCE.getInventoryEventRow_ForecastDate();

		/**
		 * The meta object literal for the '<em><b>Volume Low</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INVENTORY_EVENT_ROW__VOLUME_LOW = eINSTANCE.getInventoryEventRow_VolumeLow();

		/**
		 * The meta object literal for the '<em><b>Volume High</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INVENTORY_EVENT_ROW__VOLUME_HIGH = eINSTANCE.getInventoryEventRow_VolumeHigh();

		/**
		 * The meta object literal for the '<em><b>Get Reliable Volume</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation INVENTORY_EVENT_ROW___GET_RELIABLE_VOLUME = eINSTANCE.getInventoryEventRow__GetReliableVolume();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.InventoryCapacityRowImpl <em>Inventory Capacity Row</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.InventoryCapacityRowImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getInventoryCapacityRow()
		 * @generated
		 */
		EClass INVENTORY_CAPACITY_ROW = eINSTANCE.getInventoryCapacityRow();

		/**
		 * The meta object literal for the '<em><b>Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INVENTORY_CAPACITY_ROW__DATE = eINSTANCE.getInventoryCapacityRow_Date();

		/**
		 * The meta object literal for the '<em><b>Min Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INVENTORY_CAPACITY_ROW__MIN_VOLUME = eINSTANCE.getInventoryCapacityRow_MinVolume();

		/**
		 * The meta object literal for the '<em><b>Max Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INVENTORY_CAPACITY_ROW__MAX_VOLUME = eINSTANCE.getInventoryCapacityRow_MaxVolume();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.InventoryImpl <em>Inventory</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.InventoryImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getInventory()
		 * @generated
		 */
		EClass INVENTORY = eINSTANCE.getInventory();

		/**
		 * The meta object literal for the '<em><b>Feeds</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INVENTORY__FEEDS = eINSTANCE.getInventory_Feeds();

		/**
		 * The meta object literal for the '<em><b>Offtakes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INVENTORY__OFFTAKES = eINSTANCE.getInventory_Offtakes();

		/**
		 * The meta object literal for the '<em><b>Capacities</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INVENTORY__CAPACITIES = eINSTANCE.getInventory_Capacities();

		/**
		 * The meta object literal for the '<em><b>Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INVENTORY__PORT = eINSTANCE.getInventory_Port();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.CanalBookingSlotImpl <em>Canal Booking Slot</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.CanalBookingSlotImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getCanalBookingSlot()
		 * @generated
		 */
		EClass CANAL_BOOKING_SLOT = eINSTANCE.getCanalBookingSlot();

		/**
		 * The meta object literal for the '<em><b>Route Option</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CANAL_BOOKING_SLOT__ROUTE_OPTION = eINSTANCE.getCanalBookingSlot_RouteOption();

		/**
		 * The meta object literal for the '<em><b>Booking Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CANAL_BOOKING_SLOT__BOOKING_DATE = eINSTANCE.getCanalBookingSlot_BookingDate();

		/**
		 * The meta object literal for the '<em><b>Canal Entrance</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CANAL_BOOKING_SLOT__CANAL_ENTRANCE = eINSTANCE.getCanalBookingSlot_CanalEntrance();

		/**
		 * The meta object literal for the '<em><b>Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CANAL_BOOKING_SLOT__SLOT = eINSTANCE.getCanalBookingSlot_Slot();

		/**
		 * The meta object literal for the '<em><b>Notes</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CANAL_BOOKING_SLOT__NOTES = eINSTANCE.getCanalBookingSlot_Notes();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.CanalBookingsImpl <em>Canal Bookings</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.CanalBookingsImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getCanalBookings()
		 * @generated
		 */
		EClass CANAL_BOOKINGS = eINSTANCE.getCanalBookings();

		/**
		 * The meta object literal for the '<em><b>Canal Booking Slots</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CANAL_BOOKINGS__CANAL_BOOKING_SLOTS = eINSTANCE.getCanalBookings_CanalBookingSlots();

		/**
		 * The meta object literal for the '<em><b>Strict Boundary Offset Days</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CANAL_BOOKINGS__STRICT_BOUNDARY_OFFSET_DAYS = eINSTANCE.getCanalBookings_StrictBoundaryOffsetDays();

		/**
		 * The meta object literal for the '<em><b>Relaxed Boundary Offset Days</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CANAL_BOOKINGS__RELAXED_BOUNDARY_OFFSET_DAYS = eINSTANCE.getCanalBookings_RelaxedBoundaryOffsetDays();

		/**
		 * The meta object literal for the '<em><b>Arrival Margin Hours</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CANAL_BOOKINGS__ARRIVAL_MARGIN_HOURS = eINSTANCE.getCanalBookings_ArrivalMarginHours();

		/**
		 * The meta object literal for the '<em><b>Flexible Booking Amount Northbound</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CANAL_BOOKINGS__FLEXIBLE_BOOKING_AMOUNT_NORTHBOUND = eINSTANCE.getCanalBookings_FlexibleBookingAmountNorthbound();

		/**
		 * The meta object literal for the '<em><b>Flexible Booking Amount Southbound</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CANAL_BOOKINGS__FLEXIBLE_BOOKING_AMOUNT_SOUTHBOUND = eINSTANCE.getCanalBookings_FlexibleBookingAmountSouthbound();

		/**
		 * The meta object literal for the '<em><b>Northbound Max Idle Days</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CANAL_BOOKINGS__NORTHBOUND_MAX_IDLE_DAYS = eINSTANCE.getCanalBookings_NorthboundMaxIdleDays();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.ScheduleSpecificationImpl <em>Schedule Specification</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.ScheduleSpecificationImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getScheduleSpecification()
		 * @generated
		 */
		EClass SCHEDULE_SPECIFICATION = eINSTANCE.getScheduleSpecification();

		/**
		 * The meta object literal for the '<em><b>Vessel Schedule Specifications</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEDULE_SPECIFICATION__VESSEL_SCHEDULE_SPECIFICATIONS = eINSTANCE.getScheduleSpecification_VesselScheduleSpecifications();

		/**
		 * The meta object literal for the '<em><b>Non Shipped Cargo Specifications</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEDULE_SPECIFICATION__NON_SHIPPED_CARGO_SPECIFICATIONS = eINSTANCE.getScheduleSpecification_NonShippedCargoSpecifications();

		/**
		 * The meta object literal for the '<em><b>Open Events</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEDULE_SPECIFICATION__OPEN_EVENTS = eINSTANCE.getScheduleSpecification_OpenEvents();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.NonShippedCargoSpecificationImpl <em>Non Shipped Cargo Specification</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.NonShippedCargoSpecificationImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getNonShippedCargoSpecification()
		 * @generated
		 */
		EClass NON_SHIPPED_CARGO_SPECIFICATION = eINSTANCE.getNonShippedCargoSpecification();

		/**
		 * The meta object literal for the '<em><b>Slot Specifications</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NON_SHIPPED_CARGO_SPECIFICATION__SLOT_SPECIFICATIONS = eINSTANCE.getNonShippedCargoSpecification_SlotSpecifications();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.VesselScheduleSpecificationImpl <em>Vessel Schedule Specification</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.VesselScheduleSpecificationImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getVesselScheduleSpecification()
		 * @generated
		 */
		EClass VESSEL_SCHEDULE_SPECIFICATION = eINSTANCE.getVesselScheduleSpecification();

		/**
		 * The meta object literal for the '<em><b>Vessel Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_SCHEDULE_SPECIFICATION__VESSEL_ALLOCATION = eINSTANCE.getVesselScheduleSpecification_VesselAllocation();

		/**
		 * The meta object literal for the '<em><b>Spot Index</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_SCHEDULE_SPECIFICATION__SPOT_INDEX = eINSTANCE.getVesselScheduleSpecification_SpotIndex();

		/**
		 * The meta object literal for the '<em><b>Events</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_SCHEDULE_SPECIFICATION__EVENTS = eINSTANCE.getVesselScheduleSpecification_Events();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.ScheduleSpecificationEventImpl <em>Schedule Specification Event</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.ScheduleSpecificationEventImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getScheduleSpecificationEvent()
		 * @generated
		 */
		EClass SCHEDULE_SPECIFICATION_EVENT = eINSTANCE.getScheduleSpecificationEvent();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.VesselEventSpecificationImpl <em>Vessel Event Specification</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.VesselEventSpecificationImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getVesselEventSpecification()
		 * @generated
		 */
		EClass VESSEL_EVENT_SPECIFICATION = eINSTANCE.getVesselEventSpecification();

		/**
		 * The meta object literal for the '<em><b>Vessel Event</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_EVENT_SPECIFICATION__VESSEL_EVENT = eINSTANCE.getVesselEventSpecification_VesselEvent();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.VoyageSpecificationImpl <em>Voyage Specification</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.VoyageSpecificationImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getVoyageSpecification()
		 * @generated
		 */
		EClass VOYAGE_SPECIFICATION = eINSTANCE.getVoyageSpecification();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.SlotSpecificationImpl <em>Slot Specification</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.SlotSpecificationImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getSlotSpecification()
		 * @generated
		 */
		EClass SLOT_SPECIFICATION = eINSTANCE.getSlotSpecification();

		/**
		 * The meta object literal for the '<em><b>Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT_SPECIFICATION__SLOT = eINSTANCE.getSlotSpecification_Slot();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.CharterInMarketOverrideImpl <em>Charter In Market Override</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.CharterInMarketOverrideImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getCharterInMarketOverride()
		 * @generated
		 */
		EClass CHARTER_IN_MARKET_OVERRIDE = eINSTANCE.getCharterInMarketOverride();

		/**
		 * The meta object literal for the '<em><b>Charter In Market</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHARTER_IN_MARKET_OVERRIDE__CHARTER_IN_MARKET = eINSTANCE.getCharterInMarketOverride_CharterInMarket();

		/**
		 * The meta object literal for the '<em><b>Spot Index</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHARTER_IN_MARKET_OVERRIDE__SPOT_INDEX = eINSTANCE.getCharterInMarketOverride_SpotIndex();

		/**
		 * The meta object literal for the '<em><b>Start Heel</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHARTER_IN_MARKET_OVERRIDE__START_HEEL = eINSTANCE.getCharterInMarketOverride_StartHeel();

		/**
		 * The meta object literal for the '<em><b>Start Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHARTER_IN_MARKET_OVERRIDE__START_DATE = eINSTANCE.getCharterInMarketOverride_StartDate();

		/**
		 * The meta object literal for the '<em><b>End Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHARTER_IN_MARKET_OVERRIDE__END_PORT = eINSTANCE.getCharterInMarketOverride_EndPort();

		/**
		 * The meta object literal for the '<em><b>End Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHARTER_IN_MARKET_OVERRIDE__END_DATE = eINSTANCE.getCharterInMarketOverride_EndDate();

		/**
		 * The meta object literal for the '<em><b>End Heel</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHARTER_IN_MARKET_OVERRIDE__END_HEEL = eINSTANCE.getCharterInMarketOverride_EndHeel();

		/**
		 * The meta object literal for the '<em><b>Include Ballast Bonus</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHARTER_IN_MARKET_OVERRIDE__INCLUDE_BALLAST_BONUS = eINSTANCE.getCharterInMarketOverride_IncludeBallastBonus();

		/**
		 * The meta object literal for the '<em><b>Min Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHARTER_IN_MARKET_OVERRIDE__MIN_DURATION = eINSTANCE.getCharterInMarketOverride_MinDuration();

		/**
		 * The meta object literal for the '<em><b>Max Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHARTER_IN_MARKET_OVERRIDE__MAX_DURATION = eINSTANCE.getCharterInMarketOverride_MaxDuration();

		/**
		 * The meta object literal for the '<em><b>Get Start Date As Date Time</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CHARTER_IN_MARKET_OVERRIDE___GET_START_DATE_AS_DATE_TIME = eINSTANCE.getCharterInMarketOverride__GetStartDateAsDateTime();

		/**
		 * The meta object literal for the '<em><b>Get End Date As Date Time</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CHARTER_IN_MARKET_OVERRIDE___GET_END_DATE_AS_DATE_TIME = eINSTANCE.getCharterInMarketOverride__GetEndDateAsDateTime();

		/**
		 * The meta object literal for the '<em><b>Get Local Or Delegate Min Duration</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CHARTER_IN_MARKET_OVERRIDE___GET_LOCAL_OR_DELEGATE_MIN_DURATION = eINSTANCE.getCharterInMarketOverride__GetLocalOrDelegateMinDuration();

		/**
		 * The meta object literal for the '<em><b>Get Local Or Delegate Max Duration</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CHARTER_IN_MARKET_OVERRIDE___GET_LOCAL_OR_DELEGATE_MAX_DURATION = eINSTANCE.getCharterInMarketOverride__GetLocalOrDelegateMaxDuration();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.PaperDealImpl <em>Paper Deal</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.PaperDealImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getPaperDeal()
		 * @generated
		 */
		EClass PAPER_DEAL = eINSTANCE.getPaperDeal();

		/**
		 * The meta object literal for the '<em><b>Quantity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PAPER_DEAL__QUANTITY = eINSTANCE.getPaperDeal_Quantity();

		/**
		 * The meta object literal for the '<em><b>Start Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PAPER_DEAL__START_DATE = eINSTANCE.getPaperDeal_StartDate();

		/**
		 * The meta object literal for the '<em><b>End Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PAPER_DEAL__END_DATE = eINSTANCE.getPaperDeal_EndDate();

		/**
		 * The meta object literal for the '<em><b>Entity</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PAPER_DEAL__ENTITY = eINSTANCE.getPaperDeal_Entity();

		/**
		 * The meta object literal for the '<em><b>Year</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PAPER_DEAL__YEAR = eINSTANCE.getPaperDeal_Year();

		/**
		 * The meta object literal for the '<em><b>Comment</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PAPER_DEAL__COMMENT = eINSTANCE.getPaperDeal_Comment();

		/**
		 * The meta object literal for the '<em><b>Instrument</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PAPER_DEAL__INSTRUMENT = eINSTANCE.getPaperDeal_Instrument();

		/**
		 * The meta object literal for the '<em><b>Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PAPER_DEAL__PRICE = eINSTANCE.getPaperDeal_Price();

		/**
		 * The meta object literal for the '<em><b>Index</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PAPER_DEAL__INDEX = eINSTANCE.getPaperDeal_Index();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.BuyPaperDealImpl <em>Buy Paper Deal</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.BuyPaperDealImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getBuyPaperDeal()
		 * @generated
		 */
		EClass BUY_PAPER_DEAL = eINSTANCE.getBuyPaperDeal();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.SellPaperDealImpl <em>Sell Paper Deal</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.SellPaperDealImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getSellPaperDeal()
		 * @generated
		 */
		EClass SELL_PAPER_DEAL = eINSTANCE.getSellPaperDeal();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.DealSetImpl <em>Deal Set</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.DealSetImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getDealSet()
		 * @generated
		 */
		EClass DEAL_SET = eINSTANCE.getDealSet();

		/**
		 * The meta object literal for the '<em><b>Slots</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEAL_SET__SLOTS = eINSTANCE.getDealSet_Slots();

		/**
		 * The meta object literal for the '<em><b>Paper Deals</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEAL_SET__PAPER_DEALS = eINSTANCE.getDealSet_PaperDeals();

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

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.EVesselTankState <em>EVessel Tank State</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.EVesselTankState
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getEVesselTankState()
		 * @generated
		 */
		EEnum EVESSEL_TANK_STATE = eINSTANCE.getEVesselTankState();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.InventoryFrequency <em>Inventory Frequency</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.InventoryFrequency
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getInventoryFrequency()
		 * @generated
		 */
		EEnum INVENTORY_FREQUENCY = eINSTANCE.getInventoryFrequency();

		/**
		 * The meta object literal for the '<em>Scheduling Time Window</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.SchedulingTimeWindow
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getSchedulingTimeWindow()
		 * @generated
		 */
		EDataType SCHEDULING_TIME_WINDOW = eINSTANCE.getSchedulingTimeWindow();

	}

} //CargoPackage
