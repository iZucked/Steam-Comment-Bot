/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.actuals;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.types.TypesPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.actuals.ActualsFactory
 * @model kind="package"
 * @generated
 */
public interface ActualsPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "actuals";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.mmxlabs.com/models/lng/actuals/1/";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "lng.actuals";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ActualsPackage eINSTANCE = com.mmxlabs.models.lng.actuals.impl.ActualsPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.actuals.impl.ActualsModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.actuals.impl.ActualsModelImpl
	 * @see com.mmxlabs.models.lng.actuals.impl.ActualsPackageImpl#getActualsModel()
	 * @generated
	 */
	int ACTUALS_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Cargo Actuals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTUALS_MODEL__CARGO_ACTUALS = 0;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTUALS_MODEL_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTUALS_MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl <em>Slot Actuals</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl
	 * @see com.mmxlabs.models.lng.actuals.impl.ActualsPackageImpl#getSlotActuals()
	 * @generated
	 */
	int SLOT_ACTUALS = 1;

	/**
	 * The feature id for the '<em><b>Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ACTUALS__SLOT = TypesPackage.ITIMEZONE_PROVIDER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Counterparty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ACTUALS__COUNTERPARTY = TypesPackage.ITIMEZONE_PROVIDER_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Operations Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ACTUALS__OPERATIONS_START = TypesPackage.ITIMEZONE_PROVIDER_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Operations End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ACTUALS__OPERATIONS_END = TypesPackage.ITIMEZONE_PROVIDER_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Title Transfer Point</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ACTUALS__TITLE_TRANSFER_POINT = TypesPackage.ITIMEZONE_PROVIDER_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Volume In M3</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ACTUALS__VOLUME_IN_M3 = TypesPackage.ITIMEZONE_PROVIDER_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Volume In MM Btu</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ACTUALS__VOLUME_IN_MM_BTU = TypesPackage.ITIMEZONE_PROVIDER_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Price DOL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ACTUALS__PRICE_DOL = TypesPackage.ITIMEZONE_PROVIDER_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Penalty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ACTUALS__PENALTY = TypesPackage.ITIMEZONE_PROVIDER_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ACTUALS__NOTES = TypesPackage.ITIMEZONE_PROVIDER_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>CV</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ACTUALS__CV = TypesPackage.ITIMEZONE_PROVIDER_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Base Fuel Consumption</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ACTUALS__BASE_FUEL_CONSUMPTION = TypesPackage.ITIMEZONE_PROVIDER_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>Port Base Fuel Consumption</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ACTUALS__PORT_BASE_FUEL_CONSUMPTION = TypesPackage.ITIMEZONE_PROVIDER_FEATURE_COUNT + 12;

	/**
	 * The feature id for the '<em><b>Route</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ACTUALS__ROUTE = TypesPackage.ITIMEZONE_PROVIDER_FEATURE_COUNT + 13;

	/**
	 * The feature id for the '<em><b>Distance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ACTUALS__DISTANCE = TypesPackage.ITIMEZONE_PROVIDER_FEATURE_COUNT + 14;

	/**
	 * The feature id for the '<em><b>Route Costs</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ACTUALS__ROUTE_COSTS = TypesPackage.ITIMEZONE_PROVIDER_FEATURE_COUNT + 15;

	/**
	 * The feature id for the '<em><b>Crew Bonus</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ACTUALS__CREW_BONUS = TypesPackage.ITIMEZONE_PROVIDER_FEATURE_COUNT + 16;

	/**
	 * The feature id for the '<em><b>Port Charges</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ACTUALS__PORT_CHARGES = TypesPackage.ITIMEZONE_PROVIDER_FEATURE_COUNT + 17;

	/**
	 * The feature id for the '<em><b>Capacity Charges</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ACTUALS__CAPACITY_CHARGES = TypesPackage.ITIMEZONE_PROVIDER_FEATURE_COUNT + 18;

	/**
	 * The number of structural features of the '<em>Slot Actuals</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ACTUALS_FEATURE_COUNT = TypesPackage.ITIMEZONE_PROVIDER_FEATURE_COUNT + 19;

	/**
	 * The operation id for the '<em>Get Time Zone</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ACTUALS___GET_TIME_ZONE__EATTRIBUTE = TypesPackage.ITIMEZONE_PROVIDER___GET_TIME_ZONE__EATTRIBUTE;

	/**
	 * The operation id for the '<em>Get Operations Start As Date Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ACTUALS___GET_OPERATIONS_START_AS_DATE_TIME = TypesPackage.ITIMEZONE_PROVIDER_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Operations End As Date Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ACTUALS___GET_OPERATIONS_END_AS_DATE_TIME = TypesPackage.ITIMEZONE_PROVIDER_OPERATION_COUNT + 1;

	/**
	 * The number of operations of the '<em>Slot Actuals</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ACTUALS_OPERATION_COUNT = TypesPackage.ITIMEZONE_PROVIDER_OPERATION_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.actuals.impl.CargoActualsImpl <em>Cargo Actuals</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.actuals.impl.CargoActualsImpl
	 * @see com.mmxlabs.models.lng.actuals.impl.ActualsPackageImpl#getCargoActuals()
	 * @generated
	 */
	int CARGO_ACTUALS = 2;

	/**
	 * The feature id for the '<em><b>Actuals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ACTUALS__ACTUALS = 0;

	/**
	 * The feature id for the '<em><b>Return Actuals</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ACTUALS__RETURN_ACTUALS = 1;

	/**
	 * The feature id for the '<em><b>Contract Year</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ACTUALS__CONTRACT_YEAR = 2;

	/**
	 * The feature id for the '<em><b>Operation Number</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ACTUALS__OPERATION_NUMBER = 3;

	/**
	 * The feature id for the '<em><b>Sub Operation Number</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ACTUALS__SUB_OPERATION_NUMBER = 4;

	/**
	 * The feature id for the '<em><b>Seller ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ACTUALS__SELLER_ID = 5;

	/**
	 * The feature id for the '<em><b>Cargo Reference</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ACTUALS__CARGO_REFERENCE = 6;

	/**
	 * The feature id for the '<em><b>Cargo Reference Seller</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ACTUALS__CARGO_REFERENCE_SELLER = 7;

	/**
	 * The feature id for the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ACTUALS__VESSEL = 8;

	/**
	 * The feature id for the '<em><b>Charter Rate Per Day</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ACTUALS__CHARTER_RATE_PER_DAY = 9;

	/**
	 * The feature id for the '<em><b>Cargo</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ACTUALS__CARGO = 10;

	/**
	 * The feature id for the '<em><b>Base Fuel Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ACTUALS__BASE_FUEL_PRICE = 11;

	/**
	 * The feature id for the '<em><b>Insurance Premium</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ACTUALS__INSURANCE_PREMIUM = 12;

	/**
	 * The number of structural features of the '<em>Cargo Actuals</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ACTUALS_FEATURE_COUNT = 13;

	/**
	 * The operation id for the '<em>Get Sorted Actuals</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ACTUALS___GET_SORTED_ACTUALS = 0;

	/**
	 * The number of operations of the '<em>Cargo Actuals</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ACTUALS_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.actuals.impl.LoadActualsImpl <em>Load Actuals</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.actuals.impl.LoadActualsImpl
	 * @see com.mmxlabs.models.lng.actuals.impl.ActualsPackageImpl#getLoadActuals()
	 * @generated
	 */
	int LOAD_ACTUALS = 3;

	/**
	 * The feature id for the '<em><b>Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_ACTUALS__SLOT = SLOT_ACTUALS__SLOT;

	/**
	 * The feature id for the '<em><b>Counterparty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_ACTUALS__COUNTERPARTY = SLOT_ACTUALS__COUNTERPARTY;

	/**
	 * The feature id for the '<em><b>Operations Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_ACTUALS__OPERATIONS_START = SLOT_ACTUALS__OPERATIONS_START;

	/**
	 * The feature id for the '<em><b>Operations End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_ACTUALS__OPERATIONS_END = SLOT_ACTUALS__OPERATIONS_END;

	/**
	 * The feature id for the '<em><b>Title Transfer Point</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_ACTUALS__TITLE_TRANSFER_POINT = SLOT_ACTUALS__TITLE_TRANSFER_POINT;

	/**
	 * The feature id for the '<em><b>Volume In M3</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_ACTUALS__VOLUME_IN_M3 = SLOT_ACTUALS__VOLUME_IN_M3;

	/**
	 * The feature id for the '<em><b>Volume In MM Btu</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_ACTUALS__VOLUME_IN_MM_BTU = SLOT_ACTUALS__VOLUME_IN_MM_BTU;

	/**
	 * The feature id for the '<em><b>Price DOL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_ACTUALS__PRICE_DOL = SLOT_ACTUALS__PRICE_DOL;

	/**
	 * The feature id for the '<em><b>Penalty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_ACTUALS__PENALTY = SLOT_ACTUALS__PENALTY;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_ACTUALS__NOTES = SLOT_ACTUALS__NOTES;

	/**
	 * The feature id for the '<em><b>CV</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_ACTUALS__CV = SLOT_ACTUALS__CV;

	/**
	 * The feature id for the '<em><b>Base Fuel Consumption</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_ACTUALS__BASE_FUEL_CONSUMPTION = SLOT_ACTUALS__BASE_FUEL_CONSUMPTION;

	/**
	 * The feature id for the '<em><b>Port Base Fuel Consumption</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_ACTUALS__PORT_BASE_FUEL_CONSUMPTION = SLOT_ACTUALS__PORT_BASE_FUEL_CONSUMPTION;

	/**
	 * The feature id for the '<em><b>Route</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_ACTUALS__ROUTE = SLOT_ACTUALS__ROUTE;

	/**
	 * The feature id for the '<em><b>Distance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_ACTUALS__DISTANCE = SLOT_ACTUALS__DISTANCE;

	/**
	 * The feature id for the '<em><b>Route Costs</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_ACTUALS__ROUTE_COSTS = SLOT_ACTUALS__ROUTE_COSTS;

	/**
	 * The feature id for the '<em><b>Crew Bonus</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_ACTUALS__CREW_BONUS = SLOT_ACTUALS__CREW_BONUS;

	/**
	 * The feature id for the '<em><b>Port Charges</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_ACTUALS__PORT_CHARGES = SLOT_ACTUALS__PORT_CHARGES;

	/**
	 * The feature id for the '<em><b>Capacity Charges</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_ACTUALS__CAPACITY_CHARGES = SLOT_ACTUALS__CAPACITY_CHARGES;

	/**
	 * The feature id for the '<em><b>Contract Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_ACTUALS__CONTRACT_TYPE = SLOT_ACTUALS_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Starting Heel M3</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_ACTUALS__STARTING_HEEL_M3 = SLOT_ACTUALS_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Starting Heel MMB Tu</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_ACTUALS__STARTING_HEEL_MMB_TU = SLOT_ACTUALS_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Load Actuals</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_ACTUALS_FEATURE_COUNT = SLOT_ACTUALS_FEATURE_COUNT + 3;

	/**
	 * The operation id for the '<em>Get Time Zone</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_ACTUALS___GET_TIME_ZONE__EATTRIBUTE = SLOT_ACTUALS___GET_TIME_ZONE__EATTRIBUTE;

	/**
	 * The operation id for the '<em>Get Operations Start As Date Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_ACTUALS___GET_OPERATIONS_START_AS_DATE_TIME = SLOT_ACTUALS___GET_OPERATIONS_START_AS_DATE_TIME;

	/**
	 * The operation id for the '<em>Get Operations End As Date Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_ACTUALS___GET_OPERATIONS_END_AS_DATE_TIME = SLOT_ACTUALS___GET_OPERATIONS_END_AS_DATE_TIME;

	/**
	 * The number of operations of the '<em>Load Actuals</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_ACTUALS_OPERATION_COUNT = SLOT_ACTUALS_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.actuals.impl.DischargeActualsImpl <em>Discharge Actuals</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.actuals.impl.DischargeActualsImpl
	 * @see com.mmxlabs.models.lng.actuals.impl.ActualsPackageImpl#getDischargeActuals()
	 * @generated
	 */
	int DISCHARGE_ACTUALS = 4;

	/**
	 * The feature id for the '<em><b>Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_ACTUALS__SLOT = SLOT_ACTUALS__SLOT;

	/**
	 * The feature id for the '<em><b>Counterparty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_ACTUALS__COUNTERPARTY = SLOT_ACTUALS__COUNTERPARTY;

	/**
	 * The feature id for the '<em><b>Operations Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_ACTUALS__OPERATIONS_START = SLOT_ACTUALS__OPERATIONS_START;

	/**
	 * The feature id for the '<em><b>Operations End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_ACTUALS__OPERATIONS_END = SLOT_ACTUALS__OPERATIONS_END;

	/**
	 * The feature id for the '<em><b>Title Transfer Point</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_ACTUALS__TITLE_TRANSFER_POINT = SLOT_ACTUALS__TITLE_TRANSFER_POINT;

	/**
	 * The feature id for the '<em><b>Volume In M3</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_ACTUALS__VOLUME_IN_M3 = SLOT_ACTUALS__VOLUME_IN_M3;

	/**
	 * The feature id for the '<em><b>Volume In MM Btu</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_ACTUALS__VOLUME_IN_MM_BTU = SLOT_ACTUALS__VOLUME_IN_MM_BTU;

	/**
	 * The feature id for the '<em><b>Price DOL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_ACTUALS__PRICE_DOL = SLOT_ACTUALS__PRICE_DOL;

	/**
	 * The feature id for the '<em><b>Penalty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_ACTUALS__PENALTY = SLOT_ACTUALS__PENALTY;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_ACTUALS__NOTES = SLOT_ACTUALS__NOTES;

	/**
	 * The feature id for the '<em><b>CV</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_ACTUALS__CV = SLOT_ACTUALS__CV;

	/**
	 * The feature id for the '<em><b>Base Fuel Consumption</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_ACTUALS__BASE_FUEL_CONSUMPTION = SLOT_ACTUALS__BASE_FUEL_CONSUMPTION;

	/**
	 * The feature id for the '<em><b>Port Base Fuel Consumption</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_ACTUALS__PORT_BASE_FUEL_CONSUMPTION = SLOT_ACTUALS__PORT_BASE_FUEL_CONSUMPTION;

	/**
	 * The feature id for the '<em><b>Route</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_ACTUALS__ROUTE = SLOT_ACTUALS__ROUTE;

	/**
	 * The feature id for the '<em><b>Distance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_ACTUALS__DISTANCE = SLOT_ACTUALS__DISTANCE;

	/**
	 * The feature id for the '<em><b>Route Costs</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_ACTUALS__ROUTE_COSTS = SLOT_ACTUALS__ROUTE_COSTS;

	/**
	 * The feature id for the '<em><b>Crew Bonus</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_ACTUALS__CREW_BONUS = SLOT_ACTUALS__CREW_BONUS;

	/**
	 * The feature id for the '<em><b>Port Charges</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_ACTUALS__PORT_CHARGES = SLOT_ACTUALS__PORT_CHARGES;

	/**
	 * The feature id for the '<em><b>Capacity Charges</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_ACTUALS__CAPACITY_CHARGES = SLOT_ACTUALS__CAPACITY_CHARGES;

	/**
	 * The feature id for the '<em><b>Delivery Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_ACTUALS__DELIVERY_TYPE = SLOT_ACTUALS_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>End Heel M3</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_ACTUALS__END_HEEL_M3 = SLOT_ACTUALS_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>End Heel MMB Tu</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_ACTUALS__END_HEEL_MMB_TU = SLOT_ACTUALS_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Discharge Actuals</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_ACTUALS_FEATURE_COUNT = SLOT_ACTUALS_FEATURE_COUNT + 3;

	/**
	 * The operation id for the '<em>Get Time Zone</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_ACTUALS___GET_TIME_ZONE__EATTRIBUTE = SLOT_ACTUALS___GET_TIME_ZONE__EATTRIBUTE;

	/**
	 * The operation id for the '<em>Get Operations Start As Date Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_ACTUALS___GET_OPERATIONS_START_AS_DATE_TIME = SLOT_ACTUALS___GET_OPERATIONS_START_AS_DATE_TIME;

	/**
	 * The operation id for the '<em>Get Operations End As Date Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_ACTUALS___GET_OPERATIONS_END_AS_DATE_TIME = SLOT_ACTUALS___GET_OPERATIONS_END_AS_DATE_TIME;

	/**
	 * The number of operations of the '<em>Discharge Actuals</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_ACTUALS_OPERATION_COUNT = SLOT_ACTUALS_OPERATION_COUNT + 0;


	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.actuals.impl.ReturnActualsImpl <em>Return Actuals</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.actuals.impl.ReturnActualsImpl
	 * @see com.mmxlabs.models.lng.actuals.impl.ActualsPackageImpl#getReturnActuals()
	 * @generated
	 */
	int RETURN_ACTUALS = 5;

	/**
	 * The feature id for the '<em><b>Title Transfer Point</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RETURN_ACTUALS__TITLE_TRANSFER_POINT = TypesPackage.ITIMEZONE_PROVIDER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Operations Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RETURN_ACTUALS__OPERATIONS_START = TypesPackage.ITIMEZONE_PROVIDER_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>End Heel M3</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RETURN_ACTUALS__END_HEEL_M3 = TypesPackage.ITIMEZONE_PROVIDER_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Return Actuals</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RETURN_ACTUALS_FEATURE_COUNT = TypesPackage.ITIMEZONE_PROVIDER_FEATURE_COUNT + 3;

	/**
	 * The operation id for the '<em>Get Time Zone</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RETURN_ACTUALS___GET_TIME_ZONE__EATTRIBUTE = TypesPackage.ITIMEZONE_PROVIDER___GET_TIME_ZONE__EATTRIBUTE;

	/**
	 * The operation id for the '<em>Get Operations Start As Date Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RETURN_ACTUALS___GET_OPERATIONS_START_AS_DATE_TIME = TypesPackage.ITIMEZONE_PROVIDER_OPERATION_COUNT + 0;

	/**
	 * The number of operations of the '<em>Return Actuals</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RETURN_ACTUALS_OPERATION_COUNT = TypesPackage.ITIMEZONE_PROVIDER_OPERATION_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.actuals.PenaltyType <em>Penalty Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.actuals.PenaltyType
	 * @see com.mmxlabs.models.lng.actuals.impl.ActualsPackageImpl#getPenaltyType()
	 * @generated
	 */
	int PENALTY_TYPE = 6;

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.actuals.ActualsModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see com.mmxlabs.models.lng.actuals.ActualsModel
	 * @generated
	 */
	EClass getActualsModel();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.actuals.ActualsModel#getCargoActuals <em>Cargo Actuals</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Cargo Actuals</em>'.
	 * @see com.mmxlabs.models.lng.actuals.ActualsModel#getCargoActuals()
	 * @see #getActualsModel()
	 * @generated
	 */
	EReference getActualsModel_CargoActuals();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.actuals.SlotActuals <em>Slot Actuals</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Slot Actuals</em>'.
	 * @see com.mmxlabs.models.lng.actuals.SlotActuals
	 * @generated
	 */
	EClass getSlotActuals();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getCV <em>CV</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>CV</em>'.
	 * @see com.mmxlabs.models.lng.actuals.SlotActuals#getCV()
	 * @see #getSlotActuals()
	 * @generated
	 */
	EAttribute getSlotActuals_CV();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getPortCharges <em>Port Charges</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Port Charges</em>'.
	 * @see com.mmxlabs.models.lng.actuals.SlotActuals#getPortCharges()
	 * @see #getSlotActuals()
	 * @generated
	 */
	EAttribute getSlotActuals_PortCharges();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getCapacityCharges <em>Capacity Charges</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Capacity Charges</em>'.
	 * @see com.mmxlabs.models.lng.actuals.SlotActuals#getCapacityCharges()
	 * @see #getSlotActuals()
	 * @generated
	 */
	EAttribute getSlotActuals_CapacityCharges();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getOperationsStartAsDateTime() <em>Get Operations Start As Date Time</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Operations Start As Date Time</em>' operation.
	 * @see com.mmxlabs.models.lng.actuals.SlotActuals#getOperationsStartAsDateTime()
	 * @generated
	 */
	EOperation getSlotActuals__GetOperationsStartAsDateTime();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getOperationsEndAsDateTime() <em>Get Operations End As Date Time</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Operations End As Date Time</em>' operation.
	 * @see com.mmxlabs.models.lng.actuals.SlotActuals#getOperationsEndAsDateTime()
	 * @generated
	 */
	EOperation getSlotActuals__GetOperationsEndAsDateTime();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getBaseFuelConsumption <em>Base Fuel Consumption</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Base Fuel Consumption</em>'.
	 * @see com.mmxlabs.models.lng.actuals.SlotActuals#getBaseFuelConsumption()
	 * @see #getSlotActuals()
	 * @generated
	 */
	EAttribute getSlotActuals_BaseFuelConsumption();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getPortBaseFuelConsumption <em>Port Base Fuel Consumption</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Port Base Fuel Consumption</em>'.
	 * @see com.mmxlabs.models.lng.actuals.SlotActuals#getPortBaseFuelConsumption()
	 * @see #getSlotActuals()
	 * @generated
	 */
	EAttribute getSlotActuals_PortBaseFuelConsumption();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getRoute <em>Route</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Route</em>'.
	 * @see com.mmxlabs.models.lng.actuals.SlotActuals#getRoute()
	 * @see #getSlotActuals()
	 * @generated
	 */
	EReference getSlotActuals_Route();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getDistance <em>Distance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Distance</em>'.
	 * @see com.mmxlabs.models.lng.actuals.SlotActuals#getDistance()
	 * @see #getSlotActuals()
	 * @generated
	 */
	EAttribute getSlotActuals_Distance();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getRouteCosts <em>Route Costs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Route Costs</em>'.
	 * @see com.mmxlabs.models.lng.actuals.SlotActuals#getRouteCosts()
	 * @see #getSlotActuals()
	 * @generated
	 */
	EAttribute getSlotActuals_RouteCosts();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getCrewBonus <em>Crew Bonus</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Crew Bonus</em>'.
	 * @see com.mmxlabs.models.lng.actuals.SlotActuals#getCrewBonus()
	 * @see #getSlotActuals()
	 * @generated
	 */
	EAttribute getSlotActuals_CrewBonus();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getSlot <em>Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Slot</em>'.
	 * @see com.mmxlabs.models.lng.actuals.SlotActuals#getSlot()
	 * @see #getSlotActuals()
	 * @generated
	 */
	EReference getSlotActuals_Slot();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getCounterparty <em>Counterparty</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Counterparty</em>'.
	 * @see com.mmxlabs.models.lng.actuals.SlotActuals#getCounterparty()
	 * @see #getSlotActuals()
	 * @generated
	 */
	EAttribute getSlotActuals_Counterparty();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getOperationsStart <em>Operations Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Operations Start</em>'.
	 * @see com.mmxlabs.models.lng.actuals.SlotActuals#getOperationsStart()
	 * @see #getSlotActuals()
	 * @generated
	 */
	EAttribute getSlotActuals_OperationsStart();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getOperationsEnd <em>Operations End</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Operations End</em>'.
	 * @see com.mmxlabs.models.lng.actuals.SlotActuals#getOperationsEnd()
	 * @see #getSlotActuals()
	 * @generated
	 */
	EAttribute getSlotActuals_OperationsEnd();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getTitleTransferPoint <em>Title Transfer Point</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Title Transfer Point</em>'.
	 * @see com.mmxlabs.models.lng.actuals.SlotActuals#getTitleTransferPoint()
	 * @see #getSlotActuals()
	 * @generated
	 */
	EReference getSlotActuals_TitleTransferPoint();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getVolumeInM3 <em>Volume In M3</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volume In M3</em>'.
	 * @see com.mmxlabs.models.lng.actuals.SlotActuals#getVolumeInM3()
	 * @see #getSlotActuals()
	 * @generated
	 */
	EAttribute getSlotActuals_VolumeInM3();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getVolumeInMMBtu <em>Volume In MM Btu</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volume In MM Btu</em>'.
	 * @see com.mmxlabs.models.lng.actuals.SlotActuals#getVolumeInMMBtu()
	 * @see #getSlotActuals()
	 * @generated
	 */
	EAttribute getSlotActuals_VolumeInMMBtu();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getPriceDOL <em>Price DOL</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price DOL</em>'.
	 * @see com.mmxlabs.models.lng.actuals.SlotActuals#getPriceDOL()
	 * @see #getSlotActuals()
	 * @generated
	 */
	EAttribute getSlotActuals_PriceDOL();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getPenalty <em>Penalty</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Penalty</em>'.
	 * @see com.mmxlabs.models.lng.actuals.SlotActuals#getPenalty()
	 * @see #getSlotActuals()
	 * @generated
	 */
	EAttribute getSlotActuals_Penalty();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getNotes <em>Notes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Notes</em>'.
	 * @see com.mmxlabs.models.lng.actuals.SlotActuals#getNotes()
	 * @see #getSlotActuals()
	 * @generated
	 */
	EAttribute getSlotActuals_Notes();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.actuals.CargoActuals <em>Cargo Actuals</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cargo Actuals</em>'.
	 * @see com.mmxlabs.models.lng.actuals.CargoActuals
	 * @generated
	 */
	EClass getCargoActuals();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getBaseFuelPrice <em>Base Fuel Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Base Fuel Price</em>'.
	 * @see com.mmxlabs.models.lng.actuals.CargoActuals#getBaseFuelPrice()
	 * @see #getCargoActuals()
	 * @generated
	 */
	EAttribute getCargoActuals_BaseFuelPrice();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getInsurancePremium <em>Insurance Premium</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Insurance Premium</em>'.
	 * @see com.mmxlabs.models.lng.actuals.CargoActuals#getInsurancePremium()
	 * @see #getCargoActuals()
	 * @generated
	 */
	EAttribute getCargoActuals_InsurancePremium();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getSortedActuals() <em>Get Sorted Actuals</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Sorted Actuals</em>' operation.
	 * @see com.mmxlabs.models.lng.actuals.CargoActuals#getSortedActuals()
	 * @generated
	 */
	EOperation getCargoActuals__GetSortedActuals();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getActuals <em>Actuals</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Actuals</em>'.
	 * @see com.mmxlabs.models.lng.actuals.CargoActuals#getActuals()
	 * @see #getCargoActuals()
	 * @generated
	 */
	EReference getCargoActuals_Actuals();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getReturnActuals <em>Return Actuals</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Return Actuals</em>'.
	 * @see com.mmxlabs.models.lng.actuals.CargoActuals#getReturnActuals()
	 * @see #getCargoActuals()
	 * @generated
	 */
	EReference getCargoActuals_ReturnActuals();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getContractYear <em>Contract Year</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Contract Year</em>'.
	 * @see com.mmxlabs.models.lng.actuals.CargoActuals#getContractYear()
	 * @see #getCargoActuals()
	 * @generated
	 */
	EAttribute getCargoActuals_ContractYear();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getOperationNumber <em>Operation Number</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Operation Number</em>'.
	 * @see com.mmxlabs.models.lng.actuals.CargoActuals#getOperationNumber()
	 * @see #getCargoActuals()
	 * @generated
	 */
	EAttribute getCargoActuals_OperationNumber();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getSubOperationNumber <em>Sub Operation Number</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Sub Operation Number</em>'.
	 * @see com.mmxlabs.models.lng.actuals.CargoActuals#getSubOperationNumber()
	 * @see #getCargoActuals()
	 * @generated
	 */
	EAttribute getCargoActuals_SubOperationNumber();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getSellerID <em>Seller ID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Seller ID</em>'.
	 * @see com.mmxlabs.models.lng.actuals.CargoActuals#getSellerID()
	 * @see #getCargoActuals()
	 * @generated
	 */
	EAttribute getCargoActuals_SellerID();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getCargoReference <em>Cargo Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cargo Reference</em>'.
	 * @see com.mmxlabs.models.lng.actuals.CargoActuals#getCargoReference()
	 * @see #getCargoActuals()
	 * @generated
	 */
	EAttribute getCargoActuals_CargoReference();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getCargoReferenceSeller <em>Cargo Reference Seller</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cargo Reference Seller</em>'.
	 * @see com.mmxlabs.models.lng.actuals.CargoActuals#getCargoReferenceSeller()
	 * @see #getCargoActuals()
	 * @generated
	 */
	EAttribute getCargoActuals_CargoReferenceSeller();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getVessel <em>Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel</em>'.
	 * @see com.mmxlabs.models.lng.actuals.CargoActuals#getVessel()
	 * @see #getCargoActuals()
	 * @generated
	 */
	EReference getCargoActuals_Vessel();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getCharterRatePerDay <em>Charter Rate Per Day</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Charter Rate Per Day</em>'.
	 * @see com.mmxlabs.models.lng.actuals.CargoActuals#getCharterRatePerDay()
	 * @see #getCargoActuals()
	 * @generated
	 */
	EAttribute getCargoActuals_CharterRatePerDay();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getCargo <em>Cargo</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Cargo</em>'.
	 * @see com.mmxlabs.models.lng.actuals.CargoActuals#getCargo()
	 * @see #getCargoActuals()
	 * @generated
	 */
	EReference getCargoActuals_Cargo();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.actuals.LoadActuals <em>Load Actuals</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Load Actuals</em>'.
	 * @see com.mmxlabs.models.lng.actuals.LoadActuals
	 * @generated
	 */
	EClass getLoadActuals();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.LoadActuals#getContractType <em>Contract Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Contract Type</em>'.
	 * @see com.mmxlabs.models.lng.actuals.LoadActuals#getContractType()
	 * @see #getLoadActuals()
	 * @generated
	 */
	EAttribute getLoadActuals_ContractType();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.LoadActuals#getStartingHeelM3 <em>Starting Heel M3</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Starting Heel M3</em>'.
	 * @see com.mmxlabs.models.lng.actuals.LoadActuals#getStartingHeelM3()
	 * @see #getLoadActuals()
	 * @generated
	 */
	EAttribute getLoadActuals_StartingHeelM3();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.LoadActuals#getStartingHeelMMBTu <em>Starting Heel MMB Tu</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Starting Heel MMB Tu</em>'.
	 * @see com.mmxlabs.models.lng.actuals.LoadActuals#getStartingHeelMMBTu()
	 * @see #getLoadActuals()
	 * @generated
	 */
	EAttribute getLoadActuals_StartingHeelMMBTu();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.actuals.DischargeActuals <em>Discharge Actuals</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Discharge Actuals</em>'.
	 * @see com.mmxlabs.models.lng.actuals.DischargeActuals
	 * @generated
	 */
	EClass getDischargeActuals();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.DischargeActuals#getDeliveryType <em>Delivery Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Delivery Type</em>'.
	 * @see com.mmxlabs.models.lng.actuals.DischargeActuals#getDeliveryType()
	 * @see #getDischargeActuals()
	 * @generated
	 */
	EAttribute getDischargeActuals_DeliveryType();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.DischargeActuals#getEndHeelM3 <em>End Heel M3</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End Heel M3</em>'.
	 * @see com.mmxlabs.models.lng.actuals.DischargeActuals#getEndHeelM3()
	 * @see #getDischargeActuals()
	 * @generated
	 */
	EAttribute getDischargeActuals_EndHeelM3();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.DischargeActuals#getEndHeelMMBTu <em>End Heel MMB Tu</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End Heel MMB Tu</em>'.
	 * @see com.mmxlabs.models.lng.actuals.DischargeActuals#getEndHeelMMBTu()
	 * @see #getDischargeActuals()
	 * @generated
	 */
	EAttribute getDischargeActuals_EndHeelMMBTu();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.actuals.ReturnActuals <em>Return Actuals</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Return Actuals</em>'.
	 * @see com.mmxlabs.models.lng.actuals.ReturnActuals
	 * @generated
	 */
	EClass getReturnActuals();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.actuals.ReturnActuals#getTitleTransferPoint <em>Title Transfer Point</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Title Transfer Point</em>'.
	 * @see com.mmxlabs.models.lng.actuals.ReturnActuals#getTitleTransferPoint()
	 * @see #getReturnActuals()
	 * @generated
	 */
	EReference getReturnActuals_TitleTransferPoint();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.ReturnActuals#getOperationsStart <em>Operations Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Operations Start</em>'.
	 * @see com.mmxlabs.models.lng.actuals.ReturnActuals#getOperationsStart()
	 * @see #getReturnActuals()
	 * @generated
	 */
	EAttribute getReturnActuals_OperationsStart();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.ReturnActuals#getEndHeelM3 <em>End Heel M3</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End Heel M3</em>'.
	 * @see com.mmxlabs.models.lng.actuals.ReturnActuals#getEndHeelM3()
	 * @see #getReturnActuals()
	 * @generated
	 */
	EAttribute getReturnActuals_EndHeelM3();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.actuals.ReturnActuals#getOperationsStartAsDateTime() <em>Get Operations Start As Date Time</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Operations Start As Date Time</em>' operation.
	 * @see com.mmxlabs.models.lng.actuals.ReturnActuals#getOperationsStartAsDateTime()
	 * @generated
	 */
	EOperation getReturnActuals__GetOperationsStartAsDateTime();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.actuals.PenaltyType <em>Penalty Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Penalty Type</em>'.
	 * @see com.mmxlabs.models.lng.actuals.PenaltyType
	 * @generated
	 */
	EEnum getPenaltyType();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ActualsFactory getActualsFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.actuals.impl.ActualsModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.actuals.impl.ActualsModelImpl
		 * @see com.mmxlabs.models.lng.actuals.impl.ActualsPackageImpl#getActualsModel()
		 * @generated
		 */
		EClass ACTUALS_MODEL = eINSTANCE.getActualsModel();

		/**
		 * The meta object literal for the '<em><b>Cargo Actuals</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ACTUALS_MODEL__CARGO_ACTUALS = eINSTANCE.getActualsModel_CargoActuals();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl <em>Slot Actuals</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl
		 * @see com.mmxlabs.models.lng.actuals.impl.ActualsPackageImpl#getSlotActuals()
		 * @generated
		 */
		EClass SLOT_ACTUALS = eINSTANCE.getSlotActuals();

		/**
		 * The meta object literal for the '<em><b>CV</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT_ACTUALS__CV = eINSTANCE.getSlotActuals_CV();

		/**
		 * The meta object literal for the '<em><b>Port Charges</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT_ACTUALS__PORT_CHARGES = eINSTANCE.getSlotActuals_PortCharges();

		/**
		 * The meta object literal for the '<em><b>Capacity Charges</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT_ACTUALS__CAPACITY_CHARGES = eINSTANCE.getSlotActuals_CapacityCharges();

		/**
		 * The meta object literal for the '<em><b>Get Operations Start As Date Time</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT_ACTUALS___GET_OPERATIONS_START_AS_DATE_TIME = eINSTANCE.getSlotActuals__GetOperationsStartAsDateTime();

		/**
		 * The meta object literal for the '<em><b>Get Operations End As Date Time</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT_ACTUALS___GET_OPERATIONS_END_AS_DATE_TIME = eINSTANCE.getSlotActuals__GetOperationsEndAsDateTime();

		/**
		 * The meta object literal for the '<em><b>Base Fuel Consumption</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT_ACTUALS__BASE_FUEL_CONSUMPTION = eINSTANCE.getSlotActuals_BaseFuelConsumption();

		/**
		 * The meta object literal for the '<em><b>Port Base Fuel Consumption</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT_ACTUALS__PORT_BASE_FUEL_CONSUMPTION = eINSTANCE.getSlotActuals_PortBaseFuelConsumption();

		/**
		 * The meta object literal for the '<em><b>Route</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT_ACTUALS__ROUTE = eINSTANCE.getSlotActuals_Route();

		/**
		 * The meta object literal for the '<em><b>Distance</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT_ACTUALS__DISTANCE = eINSTANCE.getSlotActuals_Distance();

		/**
		 * The meta object literal for the '<em><b>Route Costs</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT_ACTUALS__ROUTE_COSTS = eINSTANCE.getSlotActuals_RouteCosts();

		/**
		 * The meta object literal for the '<em><b>Crew Bonus</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT_ACTUALS__CREW_BONUS = eINSTANCE.getSlotActuals_CrewBonus();

		/**
		 * The meta object literal for the '<em><b>Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT_ACTUALS__SLOT = eINSTANCE.getSlotActuals_Slot();

		/**
		 * The meta object literal for the '<em><b>Counterparty</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT_ACTUALS__COUNTERPARTY = eINSTANCE.getSlotActuals_Counterparty();

		/**
		 * The meta object literal for the '<em><b>Operations Start</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT_ACTUALS__OPERATIONS_START = eINSTANCE.getSlotActuals_OperationsStart();

		/**
		 * The meta object literal for the '<em><b>Operations End</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT_ACTUALS__OPERATIONS_END = eINSTANCE.getSlotActuals_OperationsEnd();

		/**
		 * The meta object literal for the '<em><b>Title Transfer Point</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT_ACTUALS__TITLE_TRANSFER_POINT = eINSTANCE.getSlotActuals_TitleTransferPoint();

		/**
		 * The meta object literal for the '<em><b>Volume In M3</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT_ACTUALS__VOLUME_IN_M3 = eINSTANCE.getSlotActuals_VolumeInM3();

		/**
		 * The meta object literal for the '<em><b>Volume In MM Btu</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT_ACTUALS__VOLUME_IN_MM_BTU = eINSTANCE.getSlotActuals_VolumeInMMBtu();

		/**
		 * The meta object literal for the '<em><b>Price DOL</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT_ACTUALS__PRICE_DOL = eINSTANCE.getSlotActuals_PriceDOL();

		/**
		 * The meta object literal for the '<em><b>Penalty</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT_ACTUALS__PENALTY = eINSTANCE.getSlotActuals_Penalty();

		/**
		 * The meta object literal for the '<em><b>Notes</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT_ACTUALS__NOTES = eINSTANCE.getSlotActuals_Notes();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.actuals.impl.CargoActualsImpl <em>Cargo Actuals</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.actuals.impl.CargoActualsImpl
		 * @see com.mmxlabs.models.lng.actuals.impl.ActualsPackageImpl#getCargoActuals()
		 * @generated
		 */
		EClass CARGO_ACTUALS = eINSTANCE.getCargoActuals();

		/**
		 * The meta object literal for the '<em><b>Base Fuel Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO_ACTUALS__BASE_FUEL_PRICE = eINSTANCE.getCargoActuals_BaseFuelPrice();

		/**
		 * The meta object literal for the '<em><b>Insurance Premium</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO_ACTUALS__INSURANCE_PREMIUM = eINSTANCE.getCargoActuals_InsurancePremium();

		/**
		 * The meta object literal for the '<em><b>Get Sorted Actuals</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CARGO_ACTUALS___GET_SORTED_ACTUALS = eINSTANCE.getCargoActuals__GetSortedActuals();

		/**
		 * The meta object literal for the '<em><b>Actuals</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_ACTUALS__ACTUALS = eINSTANCE.getCargoActuals_Actuals();

		/**
		 * The meta object literal for the '<em><b>Return Actuals</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_ACTUALS__RETURN_ACTUALS = eINSTANCE.getCargoActuals_ReturnActuals();

		/**
		 * The meta object literal for the '<em><b>Contract Year</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO_ACTUALS__CONTRACT_YEAR = eINSTANCE.getCargoActuals_ContractYear();

		/**
		 * The meta object literal for the '<em><b>Operation Number</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO_ACTUALS__OPERATION_NUMBER = eINSTANCE.getCargoActuals_OperationNumber();

		/**
		 * The meta object literal for the '<em><b>Sub Operation Number</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO_ACTUALS__SUB_OPERATION_NUMBER = eINSTANCE.getCargoActuals_SubOperationNumber();

		/**
		 * The meta object literal for the '<em><b>Seller ID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO_ACTUALS__SELLER_ID = eINSTANCE.getCargoActuals_SellerID();

		/**
		 * The meta object literal for the '<em><b>Cargo Reference</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO_ACTUALS__CARGO_REFERENCE = eINSTANCE.getCargoActuals_CargoReference();

		/**
		 * The meta object literal for the '<em><b>Cargo Reference Seller</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO_ACTUALS__CARGO_REFERENCE_SELLER = eINSTANCE.getCargoActuals_CargoReferenceSeller();

		/**
		 * The meta object literal for the '<em><b>Vessel</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_ACTUALS__VESSEL = eINSTANCE.getCargoActuals_Vessel();

		/**
		 * The meta object literal for the '<em><b>Charter Rate Per Day</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO_ACTUALS__CHARTER_RATE_PER_DAY = eINSTANCE.getCargoActuals_CharterRatePerDay();

		/**
		 * The meta object literal for the '<em><b>Cargo</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_ACTUALS__CARGO = eINSTANCE.getCargoActuals_Cargo();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.actuals.impl.LoadActualsImpl <em>Load Actuals</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.actuals.impl.LoadActualsImpl
		 * @see com.mmxlabs.models.lng.actuals.impl.ActualsPackageImpl#getLoadActuals()
		 * @generated
		 */
		EClass LOAD_ACTUALS = eINSTANCE.getLoadActuals();

		/**
		 * The meta object literal for the '<em><b>Contract Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOAD_ACTUALS__CONTRACT_TYPE = eINSTANCE.getLoadActuals_ContractType();

		/**
		 * The meta object literal for the '<em><b>Starting Heel M3</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOAD_ACTUALS__STARTING_HEEL_M3 = eINSTANCE.getLoadActuals_StartingHeelM3();

		/**
		 * The meta object literal for the '<em><b>Starting Heel MMB Tu</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOAD_ACTUALS__STARTING_HEEL_MMB_TU = eINSTANCE.getLoadActuals_StartingHeelMMBTu();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.actuals.impl.DischargeActualsImpl <em>Discharge Actuals</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.actuals.impl.DischargeActualsImpl
		 * @see com.mmxlabs.models.lng.actuals.impl.ActualsPackageImpl#getDischargeActuals()
		 * @generated
		 */
		EClass DISCHARGE_ACTUALS = eINSTANCE.getDischargeActuals();

		/**
		 * The meta object literal for the '<em><b>Delivery Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DISCHARGE_ACTUALS__DELIVERY_TYPE = eINSTANCE.getDischargeActuals_DeliveryType();

		/**
		 * The meta object literal for the '<em><b>End Heel M3</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DISCHARGE_ACTUALS__END_HEEL_M3 = eINSTANCE.getDischargeActuals_EndHeelM3();

		/**
		 * The meta object literal for the '<em><b>End Heel MMB Tu</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DISCHARGE_ACTUALS__END_HEEL_MMB_TU = eINSTANCE.getDischargeActuals_EndHeelMMBTu();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.actuals.impl.ReturnActualsImpl <em>Return Actuals</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.actuals.impl.ReturnActualsImpl
		 * @see com.mmxlabs.models.lng.actuals.impl.ActualsPackageImpl#getReturnActuals()
		 * @generated
		 */
		EClass RETURN_ACTUALS = eINSTANCE.getReturnActuals();

		/**
		 * The meta object literal for the '<em><b>Title Transfer Point</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RETURN_ACTUALS__TITLE_TRANSFER_POINT = eINSTANCE.getReturnActuals_TitleTransferPoint();

		/**
		 * The meta object literal for the '<em><b>Operations Start</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RETURN_ACTUALS__OPERATIONS_START = eINSTANCE.getReturnActuals_OperationsStart();

		/**
		 * The meta object literal for the '<em><b>End Heel M3</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RETURN_ACTUALS__END_HEEL_M3 = eINSTANCE.getReturnActuals_EndHeelM3();

		/**
		 * The meta object literal for the '<em><b>Get Operations Start As Date Time</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation RETURN_ACTUALS___GET_OPERATIONS_START_AS_DATE_TIME = eINSTANCE.getReturnActuals__GetOperationsStartAsDateTime();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.actuals.PenaltyType <em>Penalty Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.actuals.PenaltyType
		 * @see com.mmxlabs.models.lng.actuals.impl.ActualsPackageImpl#getPenaltyType()
		 * @generated
		 */
		EEnum PENALTY_TYPE = eINSTANCE.getPenaltyType();

	}

} //ActualsPackage
