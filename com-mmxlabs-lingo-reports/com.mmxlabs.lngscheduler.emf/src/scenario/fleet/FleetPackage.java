/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.fleet;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import scenario.ScenarioPackage;

/**
 * <!-- begin-user-doc --> The <b>Package</b> for the model. It contains accessors for the meta objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each operation of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * 
 * @see scenario.fleet.FleetFactory
 * @model kind="package"
 * @generated
 */
public interface FleetPackage extends EPackage {
	/**
	 * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "fleet";

	/**
	 * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "http://com.mmxlabs.lng.emf2/fleet";

	/**
	 * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "com.mmxlabs.lng.emf.fleet";

	/**
	 * The singleton instance of the package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	FleetPackage eINSTANCE = scenario.fleet.impl.FleetPackageImpl.init();

	/**
	 * The meta object id for the '{@link scenario.fleet.impl.FleetModelImpl <em>Model</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see scenario.fleet.impl.FleetModelImpl
	 * @see scenario.fleet.impl.FleetPackageImpl#getFleetModel()
	 * @generated
	 */
	int FLEET_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Fleet</b></em>' containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL__FLEET = 0;

	/**
	 * The feature id for the '<em><b>Vessel Classes</b></em>' containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL__VESSEL_CLASSES = 1;

	/**
	 * The feature id for the '<em><b>Vessel Events</b></em>' containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL__VESSEL_EVENTS = 2;

	/**
	 * The feature id for the '<em><b>Fuels</b></em>' containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL__FUELS = 3;

	/**
	 * The number of structural features of the '<em>Model</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Model</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link scenario.fleet.impl.VesselImpl <em>Vessel</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see scenario.fleet.impl.VesselImpl
	 * @see scenario.fleet.impl.FleetPackageImpl#getVessel()
	 * @generated
	 */
	int VESSEL = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL__NAME = ScenarioPackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL__NOTES = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Time Chartered</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL__TIME_CHARTERED = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Daily Charter Out Price</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL__DAILY_CHARTER_OUT_PRICE = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Class</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL__CLASS = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Start Requirement</b></em>' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL__START_REQUIREMENT = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>End Requirement</b></em>' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL__END_REQUIREMENT = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Vessel</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_FEATURE_COUNT = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The operation id for the '<em>Get Container</em>' operation. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL___GET_CONTAINER = ScenarioPackage.NAMED_OBJECT___GET_CONTAINER;

	/**
	 * The number of operations of the '<em>Vessel</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_OPERATION_COUNT = ScenarioPackage.NAMED_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link scenario.fleet.impl.VesselClassImpl <em>Vessel Class</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see scenario.fleet.impl.VesselClassImpl
	 * @see scenario.fleet.impl.FleetPackageImpl#getVesselClass()
	 * @generated
	 */
	int VESSEL_CLASS = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__NAME = ScenarioPackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__NOTES = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Capacity</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__CAPACITY = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Min Speed</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__MIN_SPEED = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Max Speed</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__MAX_SPEED = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Min Heel Volume</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__MIN_HEEL_VOLUME = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Fill Capacity</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__FILL_CAPACITY = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Spot Charter Count</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__SPOT_CHARTER_COUNT = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Daily Charter In Price</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__DAILY_CHARTER_IN_PRICE = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Daily Charter Out Price</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__DAILY_CHARTER_OUT_PRICE = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Laden Attributes</b></em>' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__LADEN_ATTRIBUTES = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Ballast Attributes</b></em>' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__BALLAST_ATTRIBUTES = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Base Fuel</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__BASE_FUEL = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>Pilot Light Rate</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__PILOT_LIGHT_RATE = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 12;

	/**
	 * The feature id for the '<em><b>Inaccessible Ports</b></em>' reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__INACCESSIBLE_PORTS = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 13;

	/**
	 * The feature id for the '<em><b>Canal Costs</b></em>' containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__CANAL_COSTS = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 14;

	/**
	 * The feature id for the '<em><b>Warmup Time</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__WARMUP_TIME = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 15;

	/**
	 * The feature id for the '<em><b>Cooldown Time</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__COOLDOWN_TIME = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 16;

	/**
	 * The feature id for the '<em><b>Cooldown Volume</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__COOLDOWN_VOLUME = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 17;

	/**
	 * The number of structural features of the '<em>Vessel Class</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS_FEATURE_COUNT = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 18;

	/**
	 * The operation id for the '<em>Get Container</em>' operation. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS___GET_CONTAINER = ScenarioPackage.NAMED_OBJECT___GET_CONTAINER;

	/**
	 * The number of operations of the '<em>Vessel Class</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS_OPERATION_COUNT = ScenarioPackage.NAMED_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link scenario.fleet.impl.FuelConsumptionLineImpl <em>Fuel Consumption Line</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see scenario.fleet.impl.FuelConsumptionLineImpl
	 * @see scenario.fleet.impl.FleetPackageImpl#getFuelConsumptionLine()
	 * @generated
	 */
	int FUEL_CONSUMPTION_LINE = 3;

	/**
	 * The feature id for the '<em><b>Speed</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FUEL_CONSUMPTION_LINE__SPEED = 0;

	/**
	 * The feature id for the '<em><b>Consumption</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FUEL_CONSUMPTION_LINE__CONSUMPTION = 1;

	/**
	 * The number of structural features of the '<em>Fuel Consumption Line</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FUEL_CONSUMPTION_LINE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Fuel Consumption Line</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FUEL_CONSUMPTION_LINE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link scenario.fleet.impl.VesselStateAttributesImpl <em>Vessel State Attributes</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see scenario.fleet.impl.VesselStateAttributesImpl
	 * @see scenario.fleet.impl.FleetPackageImpl#getVesselStateAttributes()
	 * @generated
	 */
	int VESSEL_STATE_ATTRIBUTES = 4;

	/**
	 * The feature id for the '<em><b>Nbo Rate</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES__NBO_RATE = 0;

	/**
	 * The feature id for the '<em><b>Idle NBO Rate</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE = 1;

	/**
	 * The feature id for the '<em><b>Idle Consumption Rate</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES__IDLE_CONSUMPTION_RATE = 2;

	/**
	 * The feature id for the '<em><b>Fuel Consumption Curve</b></em>' containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_CURVE = 3;

	/**
	 * The feature id for the '<em><b>Vessel State</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES__VESSEL_STATE = 4;

	/**
	 * The number of structural features of the '<em>Vessel State Attributes</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>Vessel State Attributes</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link scenario.fleet.impl.PortAndTimeImpl <em>Port And Time</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see scenario.fleet.impl.PortAndTimeImpl
	 * @see scenario.fleet.impl.FleetPackageImpl#getPortAndTime()
	 * @generated
	 */
	int PORT_AND_TIME = 5;

	/**
	 * The feature id for the '<em><b>Start Time</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PORT_AND_TIME__START_TIME = 0;

	/**
	 * The feature id for the '<em><b>End Time</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PORT_AND_TIME__END_TIME = 1;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PORT_AND_TIME__PORT = 2;

	/**
	 * The number of structural features of the '<em>Port And Time</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PORT_AND_TIME_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Port And Time</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PORT_AND_TIME_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link scenario.fleet.impl.VesselEventImpl <em>Vessel Event</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see scenario.fleet.impl.VesselEventImpl
	 * @see scenario.fleet.impl.FleetPackageImpl#getVesselEvent()
	 * @generated
	 */
	int VESSEL_EVENT = 6;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT__NOTES = ScenarioPackage.ANNOTATED_OBJECT__NOTES;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT__ID = ScenarioPackage.ANNOTATED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Start Date</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT__START_DATE = ScenarioPackage.ANNOTATED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>End Date</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT__END_DATE = ScenarioPackage.ANNOTATED_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Duration</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT__DURATION = ScenarioPackage.ANNOTATED_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Vessels</b></em>' reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT__VESSELS = ScenarioPackage.ANNOTATED_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Vessel Classes</b></em>' reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT__VESSEL_CLASSES = ScenarioPackage.ANNOTATED_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Start Port</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT__START_PORT = ScenarioPackage.ANNOTATED_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Vessel Event</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_FEATURE_COUNT = ScenarioPackage.ANNOTATED_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The number of operations of the '<em>Vessel Event</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_OPERATION_COUNT = ScenarioPackage.ANNOTATED_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link scenario.fleet.impl.CharterOutImpl <em>Charter Out</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see scenario.fleet.impl.CharterOutImpl
	 * @see scenario.fleet.impl.FleetPackageImpl#getCharterOut()
	 * @generated
	 */
	int CHARTER_OUT = 7;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT__NOTES = VESSEL_EVENT__NOTES;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT__ID = VESSEL_EVENT__ID;

	/**
	 * The feature id for the '<em><b>Start Date</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT__START_DATE = VESSEL_EVENT__START_DATE;

	/**
	 * The feature id for the '<em><b>End Date</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT__END_DATE = VESSEL_EVENT__END_DATE;

	/**
	 * The feature id for the '<em><b>Duration</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT__DURATION = VESSEL_EVENT__DURATION;

	/**
	 * The feature id for the '<em><b>Vessels</b></em>' reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT__VESSELS = VESSEL_EVENT__VESSELS;

	/**
	 * The feature id for the '<em><b>Vessel Classes</b></em>' reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT__VESSEL_CLASSES = VESSEL_EVENT__VESSEL_CLASSES;

	/**
	 * The feature id for the '<em><b>Start Port</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT__START_PORT = VESSEL_EVENT__START_PORT;

	/**
	 * The feature id for the '<em><b>Heel Limit</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT__HEEL_LIMIT = VESSEL_EVENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Heel CV Value</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT__HEEL_CV_VALUE = VESSEL_EVENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Heel Unit Price</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT__HEEL_UNIT_PRICE = VESSEL_EVENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>End Port</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT__END_PORT = VESSEL_EVENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Daily Charter Out Price</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT__DAILY_CHARTER_OUT_PRICE = VESSEL_EVENT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Repositioning Fee</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT__REPOSITIONING_FEE = VESSEL_EVENT_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Charter Out</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_FEATURE_COUNT = VESSEL_EVENT_FEATURE_COUNT + 6;

	/**
	 * The operation id for the '<em>Get Effective End Port</em>' operation. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT___GET_EFFECTIVE_END_PORT = VESSEL_EVENT_OPERATION_COUNT + 0;

	/**
	 * The number of operations of the '<em>Charter Out</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_OPERATION_COUNT = VESSEL_EVENT_OPERATION_COUNT + 1;

	/**
	 * The meta object id for the '{@link scenario.fleet.impl.DrydockImpl <em>Drydock</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see scenario.fleet.impl.DrydockImpl
	 * @see scenario.fleet.impl.FleetPackageImpl#getDrydock()
	 * @generated
	 */
	int DRYDOCK = 8;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DRYDOCK__NOTES = VESSEL_EVENT__NOTES;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DRYDOCK__ID = VESSEL_EVENT__ID;

	/**
	 * The feature id for the '<em><b>Start Date</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DRYDOCK__START_DATE = VESSEL_EVENT__START_DATE;

	/**
	 * The feature id for the '<em><b>End Date</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DRYDOCK__END_DATE = VESSEL_EVENT__END_DATE;

	/**
	 * The feature id for the '<em><b>Duration</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DRYDOCK__DURATION = VESSEL_EVENT__DURATION;

	/**
	 * The feature id for the '<em><b>Vessels</b></em>' reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DRYDOCK__VESSELS = VESSEL_EVENT__VESSELS;

	/**
	 * The feature id for the '<em><b>Vessel Classes</b></em>' reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DRYDOCK__VESSEL_CLASSES = VESSEL_EVENT__VESSEL_CLASSES;

	/**
	 * The feature id for the '<em><b>Start Port</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DRYDOCK__START_PORT = VESSEL_EVENT__START_PORT;

	/**
	 * The number of structural features of the '<em>Drydock</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DRYDOCK_FEATURE_COUNT = VESSEL_EVENT_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Drydock</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DRYDOCK_OPERATION_COUNT = VESSEL_EVENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link scenario.fleet.impl.VesselFuelImpl <em>Vessel Fuel</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see scenario.fleet.impl.VesselFuelImpl
	 * @see scenario.fleet.impl.FleetPackageImpl#getVesselFuel()
	 * @generated
	 */
	int VESSEL_FUEL = 9;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_FUEL__NAME = ScenarioPackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Unit Price</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_FUEL__UNIT_PRICE = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Equivalence Factor</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_FUEL__EQUIVALENCE_FACTOR = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Vessel Fuel</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_FUEL_FEATURE_COUNT = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Get Container</em>' operation. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_FUEL___GET_CONTAINER = ScenarioPackage.NAMED_OBJECT___GET_CONTAINER;

	/**
	 * The number of operations of the '<em>Vessel Fuel</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_FUEL_OPERATION_COUNT = ScenarioPackage.NAMED_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link scenario.fleet.impl.PortExclusionImpl <em>Port Exclusion</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see scenario.fleet.impl.PortExclusionImpl
	 * @see scenario.fleet.impl.FleetPackageImpl#getPortExclusion()
	 * @generated
	 */
	int PORT_EXCLUSION = 10;

	/**
	 * The feature id for the '<em><b>Start Date</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PORT_EXCLUSION__START_DATE = ScenarioPackage.SCENARIO_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>End Date</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PORT_EXCLUSION__END_DATE = ScenarioPackage.SCENARIO_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PORT_EXCLUSION__PORT = ScenarioPackage.SCENARIO_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Port Exclusion</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PORT_EXCLUSION_FEATURE_COUNT = ScenarioPackage.SCENARIO_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The operation id for the '<em>Get Container</em>' operation. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PORT_EXCLUSION___GET_CONTAINER = ScenarioPackage.SCENARIO_OBJECT___GET_CONTAINER;

	/**
	 * The number of operations of the '<em>Port Exclusion</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PORT_EXCLUSION_OPERATION_COUNT = ScenarioPackage.SCENARIO_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link scenario.fleet.impl.VesselClassCostImpl <em>Vessel Class Cost</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see scenario.fleet.impl.VesselClassCostImpl
	 * @see scenario.fleet.impl.FleetPackageImpl#getVesselClassCost()
	 * @generated
	 */
	int VESSEL_CLASS_COST = 11;

	/**
	 * The feature id for the '<em><b>Canal</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS_COST__CANAL = 0;

	/**
	 * The feature id for the '<em><b>Laden Cost</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS_COST__LADEN_COST = 1;

	/**
	 * The feature id for the '<em><b>Unladen Cost</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS_COST__UNLADEN_COST = 2;

	/**
	 * The feature id for the '<em><b>Transit Time</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS_COST__TRANSIT_TIME = 3;

	/**
	 * The feature id for the '<em><b>Transit Fuel</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS_COST__TRANSIT_FUEL = 4;

	/**
	 * The number of structural features of the '<em>Vessel Class Cost</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS_COST_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>Vessel Class Cost</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS_COST_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link scenario.fleet.impl.PortTimeAndHeelImpl <em>Port Time And Heel</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see scenario.fleet.impl.PortTimeAndHeelImpl
	 * @see scenario.fleet.impl.FleetPackageImpl#getPortTimeAndHeel()
	 * @generated
	 */
	int PORT_TIME_AND_HEEL = 12;

	/**
	 * The feature id for the '<em><b>Start Time</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PORT_TIME_AND_HEEL__START_TIME = PORT_AND_TIME__START_TIME;

	/**
	 * The feature id for the '<em><b>End Time</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PORT_TIME_AND_HEEL__END_TIME = PORT_AND_TIME__END_TIME;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PORT_TIME_AND_HEEL__PORT = PORT_AND_TIME__PORT;

	/**
	 * The feature id for the '<em><b>Heel Limit</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PORT_TIME_AND_HEEL__HEEL_LIMIT = PORT_AND_TIME_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Heel CV Value</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PORT_TIME_AND_HEEL__HEEL_CV_VALUE = PORT_AND_TIME_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Heel Unit Price</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PORT_TIME_AND_HEEL__HEEL_UNIT_PRICE = PORT_AND_TIME_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Port Time And Heel</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PORT_TIME_AND_HEEL_FEATURE_COUNT = PORT_AND_TIME_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>Port Time And Heel</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PORT_TIME_AND_HEEL_OPERATION_COUNT = PORT_AND_TIME_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link scenario.fleet.impl.HeelOptionsImpl <em>Heel Options</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see scenario.fleet.impl.HeelOptionsImpl
	 * @see scenario.fleet.impl.FleetPackageImpl#getHeelOptions()
	 * @generated
	 */
	int HEEL_OPTIONS = 13;

	/**
	 * The feature id for the '<em><b>Heel Limit</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int HEEL_OPTIONS__HEEL_LIMIT = 0;

	/**
	 * The feature id for the '<em><b>Heel CV Value</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int HEEL_OPTIONS__HEEL_CV_VALUE = 1;

	/**
	 * The feature id for the '<em><b>Heel Unit Price</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int HEEL_OPTIONS__HEEL_UNIT_PRICE = 2;

	/**
	 * The number of structural features of the '<em>Heel Options</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int HEEL_OPTIONS_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Heel Options</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int HEEL_OPTIONS_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link scenario.fleet.VesselState <em>Vessel State</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see scenario.fleet.VesselState
	 * @see scenario.fleet.impl.FleetPackageImpl#getVesselState()
	 * @generated
	 */
	int VESSEL_STATE = 14;

	/**
	 * Returns the meta object for class '{@link scenario.fleet.FleetModel <em>Model</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Model</em>'.
	 * @see scenario.fleet.FleetModel
	 * @generated
	 */
	EClass getFleetModel();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.fleet.FleetModel#getFleet <em>Fleet</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Fleet</em>'.
	 * @see scenario.fleet.FleetModel#getFleet()
	 * @see #getFleetModel()
	 * @generated
	 */
	EReference getFleetModel_Fleet();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.fleet.FleetModel#getVesselClasses <em>Vessel Classes</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Vessel Classes</em>'.
	 * @see scenario.fleet.FleetModel#getVesselClasses()
	 * @see #getFleetModel()
	 * @generated
	 */
	EReference getFleetModel_VesselClasses();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.fleet.FleetModel#getVesselEvents <em>Vessel Events</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Vessel Events</em>'.
	 * @see scenario.fleet.FleetModel#getVesselEvents()
	 * @see #getFleetModel()
	 * @generated
	 */
	EReference getFleetModel_VesselEvents();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.fleet.FleetModel#getFuels <em>Fuels</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Fuels</em>'.
	 * @see scenario.fleet.FleetModel#getFuels()
	 * @see #getFleetModel()
	 * @generated
	 */
	EReference getFleetModel_Fuels();

	/**
	 * Returns the meta object for class '{@link scenario.fleet.Vessel <em>Vessel</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Vessel</em>'.
	 * @see scenario.fleet.Vessel
	 * @generated
	 */
	EClass getVessel();

	/**
	 * Returns the meta object for the reference '{@link scenario.fleet.Vessel#getClass_ <em>Class</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Class</em>'.
	 * @see scenario.fleet.Vessel#getClass_()
	 * @see #getVessel()
	 * @generated
	 */
	EReference getVessel_Class();

	/**
	 * Returns the meta object for the containment reference '{@link scenario.fleet.Vessel#getStartRequirement <em>Start Requirement</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Start Requirement</em>'.
	 * @see scenario.fleet.Vessel#getStartRequirement()
	 * @see #getVessel()
	 * @generated
	 */
	EReference getVessel_StartRequirement();

	/**
	 * Returns the meta object for the containment reference '{@link scenario.fleet.Vessel#getEndRequirement <em>End Requirement</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>End Requirement</em>'.
	 * @see scenario.fleet.Vessel#getEndRequirement()
	 * @see #getVessel()
	 * @generated
	 */
	EReference getVessel_EndRequirement();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.Vessel#isTimeChartered <em>Time Chartered</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Time Chartered</em>'.
	 * @see scenario.fleet.Vessel#isTimeChartered()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_TimeChartered();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.Vessel#getDailyCharterOutPrice <em>Daily Charter Out Price</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Daily Charter Out Price</em>'.
	 * @see scenario.fleet.Vessel#getDailyCharterOutPrice()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_DailyCharterOutPrice();

	/**
	 * Returns the meta object for class '{@link scenario.fleet.VesselClass <em>Vessel Class</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Vessel Class</em>'.
	 * @see scenario.fleet.VesselClass
	 * @generated
	 */
	EClass getVesselClass();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselClass#getCapacity <em>Capacity</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Capacity</em>'.
	 * @see scenario.fleet.VesselClass#getCapacity()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_Capacity();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselClass#getMinSpeed <em>Min Speed</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Min Speed</em>'.
	 * @see scenario.fleet.VesselClass#getMinSpeed()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_MinSpeed();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselClass#getMaxSpeed <em>Max Speed</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Max Speed</em>'.
	 * @see scenario.fleet.VesselClass#getMaxSpeed()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_MaxSpeed();

	/**
	 * Returns the meta object for the containment reference '{@link scenario.fleet.VesselClass#getLadenAttributes <em>Laden Attributes</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Laden Attributes</em>'.
	 * @see scenario.fleet.VesselClass#getLadenAttributes()
	 * @see #getVesselClass()
	 * @generated
	 */
	EReference getVesselClass_LadenAttributes();

	/**
	 * Returns the meta object for the containment reference '{@link scenario.fleet.VesselClass#getBallastAttributes <em>Ballast Attributes</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Ballast Attributes</em>'.
	 * @see scenario.fleet.VesselClass#getBallastAttributes()
	 * @see #getVesselClass()
	 * @generated
	 */
	EReference getVesselClass_BallastAttributes();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselClass#getMinHeelVolume <em>Min Heel Volume</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Min Heel Volume</em>'.
	 * @see scenario.fleet.VesselClass#getMinHeelVolume()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_MinHeelVolume();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselClass#getFillCapacity <em>Fill Capacity</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Fill Capacity</em>'.
	 * @see scenario.fleet.VesselClass#getFillCapacity()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_FillCapacity();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselClass#getDailyCharterOutPrice <em>Daily Charter Out Price</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Daily Charter Out Price</em>'.
	 * @see scenario.fleet.VesselClass#getDailyCharterOutPrice()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_DailyCharterOutPrice();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselClass#getSpotCharterCount <em>Spot Charter Count</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Spot Charter Count</em>'.
	 * @see scenario.fleet.VesselClass#getSpotCharterCount()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_SpotCharterCount();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselClass#getDailyCharterInPrice <em>Daily Charter In Price</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Daily Charter In Price</em>'.
	 * @see scenario.fleet.VesselClass#getDailyCharterInPrice()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_DailyCharterInPrice();

	/**
	 * Returns the meta object for the reference list '{@link scenario.fleet.VesselClass#getInaccessiblePorts <em>Inaccessible Ports</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference list '<em>Inaccessible Ports</em>'.
	 * @see scenario.fleet.VesselClass#getInaccessiblePorts()
	 * @see #getVesselClass()
	 * @generated
	 */
	EReference getVesselClass_InaccessiblePorts();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.fleet.VesselClass#getCanalCosts <em>Canal Costs</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Canal Costs</em>'.
	 * @see scenario.fleet.VesselClass#getCanalCosts()
	 * @see #getVesselClass()
	 * @generated
	 */
	EReference getVesselClass_CanalCosts();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselClass#getWarmupTime <em>Warmup Time</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Warmup Time</em>'.
	 * @see scenario.fleet.VesselClass#getWarmupTime()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_WarmupTime();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselClass#getCooldownTime <em>Cooldown Time</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Cooldown Time</em>'.
	 * @see scenario.fleet.VesselClass#getCooldownTime()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_CooldownTime();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselClass#getCooldownVolume <em>Cooldown Volume</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Cooldown Volume</em>'.
	 * @see scenario.fleet.VesselClass#getCooldownVolume()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_CooldownVolume();

	/**
	 * Returns the meta object for the reference '{@link scenario.fleet.VesselClass#getBaseFuel <em>Base Fuel</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Base Fuel</em>'.
	 * @see scenario.fleet.VesselClass#getBaseFuel()
	 * @see #getVesselClass()
	 * @generated
	 */
	EReference getVesselClass_BaseFuel();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselClass#getPilotLightRate <em>Pilot Light Rate</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Pilot Light Rate</em>'.
	 * @see scenario.fleet.VesselClass#getPilotLightRate()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_PilotLightRate();

	/**
	 * Returns the meta object for class '{@link scenario.fleet.FuelConsumptionLine <em>Fuel Consumption Line</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Fuel Consumption Line</em>'.
	 * @see scenario.fleet.FuelConsumptionLine
	 * @generated
	 */
	EClass getFuelConsumptionLine();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.FuelConsumptionLine#getSpeed <em>Speed</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Speed</em>'.
	 * @see scenario.fleet.FuelConsumptionLine#getSpeed()
	 * @see #getFuelConsumptionLine()
	 * @generated
	 */
	EAttribute getFuelConsumptionLine_Speed();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.FuelConsumptionLine#getConsumption <em>Consumption</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Consumption</em>'.
	 * @see scenario.fleet.FuelConsumptionLine#getConsumption()
	 * @see #getFuelConsumptionLine()
	 * @generated
	 */
	EAttribute getFuelConsumptionLine_Consumption();

	/**
	 * Returns the meta object for class '{@link scenario.fleet.VesselStateAttributes <em>Vessel State Attributes</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Vessel State Attributes</em>'.
	 * @see scenario.fleet.VesselStateAttributes
	 * @generated
	 */
	EClass getVesselStateAttributes();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselStateAttributes#getVesselState <em>Vessel State</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Vessel State</em>'.
	 * @see scenario.fleet.VesselStateAttributes#getVesselState()
	 * @see #getVesselStateAttributes()
	 * @generated
	 */
	EAttribute getVesselStateAttributes_VesselState();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselStateAttributes#getNboRate <em>Nbo Rate</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Nbo Rate</em>'.
	 * @see scenario.fleet.VesselStateAttributes#getNboRate()
	 * @see #getVesselStateAttributes()
	 * @generated
	 */
	EAttribute getVesselStateAttributes_NboRate();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselStateAttributes#getIdleNBORate <em>Idle NBO Rate</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Idle NBO Rate</em>'.
	 * @see scenario.fleet.VesselStateAttributes#getIdleNBORate()
	 * @see #getVesselStateAttributes()
	 * @generated
	 */
	EAttribute getVesselStateAttributes_IdleNBORate();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselStateAttributes#getIdleConsumptionRate <em>Idle Consumption Rate</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Idle Consumption Rate</em>'.
	 * @see scenario.fleet.VesselStateAttributes#getIdleConsumptionRate()
	 * @see #getVesselStateAttributes()
	 * @generated
	 */
	EAttribute getVesselStateAttributes_IdleConsumptionRate();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.fleet.VesselStateAttributes#getFuelConsumptionCurve <em>Fuel Consumption Curve</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Fuel Consumption Curve</em>'.
	 * @see scenario.fleet.VesselStateAttributes#getFuelConsumptionCurve()
	 * @see #getVesselStateAttributes()
	 * @generated
	 */
	EReference getVesselStateAttributes_FuelConsumptionCurve();

	/**
	 * Returns the meta object for class '{@link scenario.fleet.PortAndTime <em>Port And Time</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Port And Time</em>'.
	 * @see scenario.fleet.PortAndTime
	 * @generated
	 */
	EClass getPortAndTime();

	/**
	 * Returns the meta object for the reference '{@link scenario.fleet.PortAndTime#getPort <em>Port</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Port</em>'.
	 * @see scenario.fleet.PortAndTime#getPort()
	 * @see #getPortAndTime()
	 * @generated
	 */
	EReference getPortAndTime_Port();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.PortAndTime#getStartTime <em>Start Time</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Start Time</em>'.
	 * @see scenario.fleet.PortAndTime#getStartTime()
	 * @see #getPortAndTime()
	 * @generated
	 */
	EAttribute getPortAndTime_StartTime();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.PortAndTime#getEndTime <em>End Time</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>End Time</em>'.
	 * @see scenario.fleet.PortAndTime#getEndTime()
	 * @see #getPortAndTime()
	 * @generated
	 */
	EAttribute getPortAndTime_EndTime();

	/**
	 * Returns the meta object for class '{@link scenario.fleet.VesselEvent <em>Vessel Event</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Vessel Event</em>'.
	 * @see scenario.fleet.VesselEvent
	 * @generated
	 */
	EClass getVesselEvent();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselEvent#getId <em>Id</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see scenario.fleet.VesselEvent#getId()
	 * @see #getVesselEvent()
	 * @generated
	 */
	EAttribute getVesselEvent_Id();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselEvent#getStartDate <em>Start Date</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Start Date</em>'.
	 * @see scenario.fleet.VesselEvent#getStartDate()
	 * @see #getVesselEvent()
	 * @generated
	 */
	EAttribute getVesselEvent_StartDate();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselEvent#getEndDate <em>End Date</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>End Date</em>'.
	 * @see scenario.fleet.VesselEvent#getEndDate()
	 * @see #getVesselEvent()
	 * @generated
	 */
	EAttribute getVesselEvent_EndDate();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselEvent#getDuration <em>Duration</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Duration</em>'.
	 * @see scenario.fleet.VesselEvent#getDuration()
	 * @see #getVesselEvent()
	 * @generated
	 */
	EAttribute getVesselEvent_Duration();

	/**
	 * Returns the meta object for the reference '{@link scenario.fleet.VesselEvent#getStartPort <em>Start Port</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Start Port</em>'.
	 * @see scenario.fleet.VesselEvent#getStartPort()
	 * @see #getVesselEvent()
	 * @generated
	 */
	EReference getVesselEvent_StartPort();

	/**
	 * Returns the meta object for the reference list '{@link scenario.fleet.VesselEvent#getVessels <em>Vessels</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference list '<em>Vessels</em>'.
	 * @see scenario.fleet.VesselEvent#getVessels()
	 * @see #getVesselEvent()
	 * @generated
	 */
	EReference getVesselEvent_Vessels();

	/**
	 * Returns the meta object for the reference list '{@link scenario.fleet.VesselEvent#getVesselClasses <em>Vessel Classes</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference list '<em>Vessel Classes</em>'.
	 * @see scenario.fleet.VesselEvent#getVesselClasses()
	 * @see #getVesselEvent()
	 * @generated
	 */
	EReference getVesselEvent_VesselClasses();

	/**
	 * Returns the meta object for class '{@link scenario.fleet.CharterOut <em>Charter Out</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Charter Out</em>'.
	 * @see scenario.fleet.CharterOut
	 * @generated
	 */
	EClass getCharterOut();

	/**
	 * Returns the meta object for the reference '{@link scenario.fleet.CharterOut#getEndPort <em>End Port</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>End Port</em>'.
	 * @see scenario.fleet.CharterOut#getEndPort()
	 * @see #getCharterOut()
	 * @generated
	 */
	EReference getCharterOut_EndPort();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.CharterOut#getDailyCharterOutPrice <em>Daily Charter Out Price</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Daily Charter Out Price</em>'.
	 * @see scenario.fleet.CharterOut#getDailyCharterOutPrice()
	 * @see #getCharterOut()
	 * @generated
	 */
	EAttribute getCharterOut_DailyCharterOutPrice();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.CharterOut#getRepositioningFee <em>Repositioning Fee</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Repositioning Fee</em>'.
	 * @see scenario.fleet.CharterOut#getRepositioningFee()
	 * @see #getCharterOut()
	 * @generated
	 */
	EAttribute getCharterOut_RepositioningFee();

	/**
	 * Returns the meta object for the '{@link scenario.fleet.CharterOut#getEffectiveEndPort() <em>Get Effective End Port</em>}' operation. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the '<em>Get Effective End Port</em>' operation.
	 * @see scenario.fleet.CharterOut#getEffectiveEndPort()
	 * @generated
	 */
	EOperation getCharterOut__GetEffectiveEndPort();

	/**
	 * Returns the meta object for class '{@link scenario.fleet.Drydock <em>Drydock</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Drydock</em>'.
	 * @see scenario.fleet.Drydock
	 * @generated
	 */
	EClass getDrydock();

	/**
	 * Returns the meta object for class '{@link scenario.fleet.VesselFuel <em>Vessel Fuel</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Vessel Fuel</em>'.
	 * @see scenario.fleet.VesselFuel
	 * @generated
	 */
	EClass getVesselFuel();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselFuel#getUnitPrice <em>Unit Price</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Unit Price</em>'.
	 * @see scenario.fleet.VesselFuel#getUnitPrice()
	 * @see #getVesselFuel()
	 * @generated
	 */
	EAttribute getVesselFuel_UnitPrice();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselFuel#getEquivalenceFactor <em>Equivalence Factor</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Equivalence Factor</em>'.
	 * @see scenario.fleet.VesselFuel#getEquivalenceFactor()
	 * @see #getVesselFuel()
	 * @generated
	 */
	EAttribute getVesselFuel_EquivalenceFactor();

	/**
	 * Returns the meta object for class '{@link scenario.fleet.PortExclusion <em>Port Exclusion</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Port Exclusion</em>'.
	 * @see scenario.fleet.PortExclusion
	 * @generated
	 */
	EClass getPortExclusion();

	/**
	 * Returns the meta object for the reference '{@link scenario.fleet.PortExclusion#getPort <em>Port</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Port</em>'.
	 * @see scenario.fleet.PortExclusion#getPort()
	 * @see #getPortExclusion()
	 * @generated
	 */
	EReference getPortExclusion_Port();

	/**
	 * Returns the meta object for class '{@link scenario.fleet.VesselClassCost <em>Vessel Class Cost</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Vessel Class Cost</em>'.
	 * @see scenario.fleet.VesselClassCost
	 * @generated
	 */
	EClass getVesselClassCost();

	/**
	 * Returns the meta object for the reference '{@link scenario.fleet.VesselClassCost#getCanal <em>Canal</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Canal</em>'.
	 * @see scenario.fleet.VesselClassCost#getCanal()
	 * @see #getVesselClassCost()
	 * @generated
	 */
	EReference getVesselClassCost_Canal();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselClassCost#getLadenCost <em>Laden Cost</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Laden Cost</em>'.
	 * @see scenario.fleet.VesselClassCost#getLadenCost()
	 * @see #getVesselClassCost()
	 * @generated
	 */
	EAttribute getVesselClassCost_LadenCost();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselClassCost#getUnladenCost <em>Unladen Cost</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Unladen Cost</em>'.
	 * @see scenario.fleet.VesselClassCost#getUnladenCost()
	 * @see #getVesselClassCost()
	 * @generated
	 */
	EAttribute getVesselClassCost_UnladenCost();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselClassCost#getTransitTime <em>Transit Time</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Transit Time</em>'.
	 * @see scenario.fleet.VesselClassCost#getTransitTime()
	 * @see #getVesselClassCost()
	 * @generated
	 */
	EAttribute getVesselClassCost_TransitTime();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselClassCost#getTransitFuel <em>Transit Fuel</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Transit Fuel</em>'.
	 * @see scenario.fleet.VesselClassCost#getTransitFuel()
	 * @see #getVesselClassCost()
	 * @generated
	 */
	EAttribute getVesselClassCost_TransitFuel();

	/**
	 * Returns the meta object for class '{@link scenario.fleet.PortTimeAndHeel <em>Port Time And Heel</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Port Time And Heel</em>'.
	 * @see scenario.fleet.PortTimeAndHeel
	 * @generated
	 */
	EClass getPortTimeAndHeel();

	/**
	 * Returns the meta object for class '{@link scenario.fleet.HeelOptions <em>Heel Options</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Heel Options</em>'.
	 * @see scenario.fleet.HeelOptions
	 * @generated
	 */
	EClass getHeelOptions();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.HeelOptions#getHeelLimit <em>Heel Limit</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Heel Limit</em>'.
	 * @see scenario.fleet.HeelOptions#getHeelLimit()
	 * @see #getHeelOptions()
	 * @generated
	 */
	EAttribute getHeelOptions_HeelLimit();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.HeelOptions#getHeelCVValue <em>Heel CV Value</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Heel CV Value</em>'.
	 * @see scenario.fleet.HeelOptions#getHeelCVValue()
	 * @see #getHeelOptions()
	 * @generated
	 */
	EAttribute getHeelOptions_HeelCVValue();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.HeelOptions#getHeelUnitPrice <em>Heel Unit Price</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Heel Unit Price</em>'.
	 * @see scenario.fleet.HeelOptions#getHeelUnitPrice()
	 * @see #getHeelOptions()
	 * @generated
	 */
	EAttribute getHeelOptions_HeelUnitPrice();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.PortExclusion#getStartDate <em>Start Date</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Start Date</em>'.
	 * @see scenario.fleet.PortExclusion#getStartDate()
	 * @see #getPortExclusion()
	 * @generated
	 */
	EAttribute getPortExclusion_StartDate();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.PortExclusion#getEndDate <em>End Date</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>End Date</em>'.
	 * @see scenario.fleet.PortExclusion#getEndDate()
	 * @see #getPortExclusion()
	 * @generated
	 */
	EAttribute getPortExclusion_EndDate();

	/**
	 * Returns the meta object for enum '{@link scenario.fleet.VesselState <em>Vessel State</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Vessel State</em>'.
	 * @see scenario.fleet.VesselState
	 * @generated
	 */
	EEnum getVesselState();

	/**
	 * Returns the factory that creates the instances of the model. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	FleetFactory getFleetFactory();

	/**
	 * <!-- begin-user-doc --> Defines literals for the meta objects that represent
	 * <ul>
	 * <li>each class,</li>
	 * <li>each feature of each class,</li>
	 * <li>each operation of each class,</li>
	 * <li>each enum,</li>
	 * <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link scenario.fleet.impl.FleetModelImpl <em>Model</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see scenario.fleet.impl.FleetModelImpl
		 * @see scenario.fleet.impl.FleetPackageImpl#getFleetModel()
		 * @generated
		 */
		EClass FLEET_MODEL = eINSTANCE.getFleetModel();

		/**
		 * The meta object literal for the '<em><b>Fleet</b></em>' containment reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference FLEET_MODEL__FLEET = eINSTANCE.getFleetModel_Fleet();

		/**
		 * The meta object literal for the '<em><b>Vessel Classes</b></em>' containment reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference FLEET_MODEL__VESSEL_CLASSES = eINSTANCE.getFleetModel_VesselClasses();

		/**
		 * The meta object literal for the '<em><b>Vessel Events</b></em>' containment reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference FLEET_MODEL__VESSEL_EVENTS = eINSTANCE.getFleetModel_VesselEvents();

		/**
		 * The meta object literal for the '<em><b>Fuels</b></em>' containment reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference FLEET_MODEL__FUELS = eINSTANCE.getFleetModel_Fuels();

		/**
		 * The meta object literal for the '{@link scenario.fleet.impl.VesselImpl <em>Vessel</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see scenario.fleet.impl.VesselImpl
		 * @see scenario.fleet.impl.FleetPackageImpl#getVessel()
		 * @generated
		 */
		EClass VESSEL = eINSTANCE.getVessel();

		/**
		 * The meta object literal for the '<em><b>Class</b></em>' reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference VESSEL__CLASS = eINSTANCE.getVessel_Class();

		/**
		 * The meta object literal for the '<em><b>Start Requirement</b></em>' containment reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference VESSEL__START_REQUIREMENT = eINSTANCE.getVessel_StartRequirement();

		/**
		 * The meta object literal for the '<em><b>End Requirement</b></em>' containment reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference VESSEL__END_REQUIREMENT = eINSTANCE.getVessel_EndRequirement();

		/**
		 * The meta object literal for the '<em><b>Time Chartered</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute VESSEL__TIME_CHARTERED = eINSTANCE.getVessel_TimeChartered();

		/**
		 * The meta object literal for the '<em><b>Daily Charter Out Price</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute VESSEL__DAILY_CHARTER_OUT_PRICE = eINSTANCE.getVessel_DailyCharterOutPrice();

		/**
		 * The meta object literal for the '{@link scenario.fleet.impl.VesselClassImpl <em>Vessel Class</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see scenario.fleet.impl.VesselClassImpl
		 * @see scenario.fleet.impl.FleetPackageImpl#getVesselClass()
		 * @generated
		 */
		EClass VESSEL_CLASS = eINSTANCE.getVesselClass();

		/**
		 * The meta object literal for the '<em><b>Capacity</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute VESSEL_CLASS__CAPACITY = eINSTANCE.getVesselClass_Capacity();

		/**
		 * The meta object literal for the '<em><b>Min Speed</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute VESSEL_CLASS__MIN_SPEED = eINSTANCE.getVesselClass_MinSpeed();

		/**
		 * The meta object literal for the '<em><b>Max Speed</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute VESSEL_CLASS__MAX_SPEED = eINSTANCE.getVesselClass_MaxSpeed();

		/**
		 * The meta object literal for the '<em><b>Laden Attributes</b></em>' containment reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference VESSEL_CLASS__LADEN_ATTRIBUTES = eINSTANCE.getVesselClass_LadenAttributes();

		/**
		 * The meta object literal for the '<em><b>Ballast Attributes</b></em>' containment reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference VESSEL_CLASS__BALLAST_ATTRIBUTES = eINSTANCE.getVesselClass_BallastAttributes();

		/**
		 * The meta object literal for the '<em><b>Min Heel Volume</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute VESSEL_CLASS__MIN_HEEL_VOLUME = eINSTANCE.getVesselClass_MinHeelVolume();

		/**
		 * The meta object literal for the '<em><b>Fill Capacity</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute VESSEL_CLASS__FILL_CAPACITY = eINSTANCE.getVesselClass_FillCapacity();

		/**
		 * The meta object literal for the '<em><b>Daily Charter Out Price</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute VESSEL_CLASS__DAILY_CHARTER_OUT_PRICE = eINSTANCE.getVesselClass_DailyCharterOutPrice();

		/**
		 * The meta object literal for the '<em><b>Spot Charter Count</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute VESSEL_CLASS__SPOT_CHARTER_COUNT = eINSTANCE.getVesselClass_SpotCharterCount();

		/**
		 * The meta object literal for the '<em><b>Daily Charter In Price</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute VESSEL_CLASS__DAILY_CHARTER_IN_PRICE = eINSTANCE.getVesselClass_DailyCharterInPrice();

		/**
		 * The meta object literal for the '<em><b>Inaccessible Ports</b></em>' reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference VESSEL_CLASS__INACCESSIBLE_PORTS = eINSTANCE.getVesselClass_InaccessiblePorts();

		/**
		 * The meta object literal for the '<em><b>Canal Costs</b></em>' containment reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference VESSEL_CLASS__CANAL_COSTS = eINSTANCE.getVesselClass_CanalCosts();

		/**
		 * The meta object literal for the '<em><b>Warmup Time</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute VESSEL_CLASS__WARMUP_TIME = eINSTANCE.getVesselClass_WarmupTime();

		/**
		 * The meta object literal for the '<em><b>Cooldown Time</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute VESSEL_CLASS__COOLDOWN_TIME = eINSTANCE.getVesselClass_CooldownTime();

		/**
		 * The meta object literal for the '<em><b>Cooldown Volume</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute VESSEL_CLASS__COOLDOWN_VOLUME = eINSTANCE.getVesselClass_CooldownVolume();

		/**
		 * The meta object literal for the '<em><b>Base Fuel</b></em>' reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference VESSEL_CLASS__BASE_FUEL = eINSTANCE.getVesselClass_BaseFuel();

		/**
		 * The meta object literal for the '<em><b>Pilot Light Rate</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute VESSEL_CLASS__PILOT_LIGHT_RATE = eINSTANCE.getVesselClass_PilotLightRate();

		/**
		 * The meta object literal for the '{@link scenario.fleet.impl.FuelConsumptionLineImpl <em>Fuel Consumption Line</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see scenario.fleet.impl.FuelConsumptionLineImpl
		 * @see scenario.fleet.impl.FleetPackageImpl#getFuelConsumptionLine()
		 * @generated
		 */
		EClass FUEL_CONSUMPTION_LINE = eINSTANCE.getFuelConsumptionLine();

		/**
		 * The meta object literal for the '<em><b>Speed</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute FUEL_CONSUMPTION_LINE__SPEED = eINSTANCE.getFuelConsumptionLine_Speed();

		/**
		 * The meta object literal for the '<em><b>Consumption</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute FUEL_CONSUMPTION_LINE__CONSUMPTION = eINSTANCE.getFuelConsumptionLine_Consumption();

		/**
		 * The meta object literal for the '{@link scenario.fleet.impl.VesselStateAttributesImpl <em>Vessel State Attributes</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see scenario.fleet.impl.VesselStateAttributesImpl
		 * @see scenario.fleet.impl.FleetPackageImpl#getVesselStateAttributes()
		 * @generated
		 */
		EClass VESSEL_STATE_ATTRIBUTES = eINSTANCE.getVesselStateAttributes();

		/**
		 * The meta object literal for the '<em><b>Vessel State</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute VESSEL_STATE_ATTRIBUTES__VESSEL_STATE = eINSTANCE.getVesselStateAttributes_VesselState();

		/**
		 * The meta object literal for the '<em><b>Nbo Rate</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute VESSEL_STATE_ATTRIBUTES__NBO_RATE = eINSTANCE.getVesselStateAttributes_NboRate();

		/**
		 * The meta object literal for the '<em><b>Idle NBO Rate</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE = eINSTANCE.getVesselStateAttributes_IdleNBORate();

		/**
		 * The meta object literal for the '<em><b>Idle Consumption Rate</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute VESSEL_STATE_ATTRIBUTES__IDLE_CONSUMPTION_RATE = eINSTANCE.getVesselStateAttributes_IdleConsumptionRate();

		/**
		 * The meta object literal for the '<em><b>Fuel Consumption Curve</b></em>' containment reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_CURVE = eINSTANCE.getVesselStateAttributes_FuelConsumptionCurve();

		/**
		 * The meta object literal for the '{@link scenario.fleet.impl.PortAndTimeImpl <em>Port And Time</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see scenario.fleet.impl.PortAndTimeImpl
		 * @see scenario.fleet.impl.FleetPackageImpl#getPortAndTime()
		 * @generated
		 */
		EClass PORT_AND_TIME = eINSTANCE.getPortAndTime();

		/**
		 * The meta object literal for the '<em><b>Port</b></em>' reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference PORT_AND_TIME__PORT = eINSTANCE.getPortAndTime_Port();

		/**
		 * The meta object literal for the '<em><b>Start Time</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute PORT_AND_TIME__START_TIME = eINSTANCE.getPortAndTime_StartTime();

		/**
		 * The meta object literal for the '<em><b>End Time</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute PORT_AND_TIME__END_TIME = eINSTANCE.getPortAndTime_EndTime();

		/**
		 * The meta object literal for the '{@link scenario.fleet.impl.VesselEventImpl <em>Vessel Event</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see scenario.fleet.impl.VesselEventImpl
		 * @see scenario.fleet.impl.FleetPackageImpl#getVesselEvent()
		 * @generated
		 */
		EClass VESSEL_EVENT = eINSTANCE.getVesselEvent();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute VESSEL_EVENT__ID = eINSTANCE.getVesselEvent_Id();

		/**
		 * The meta object literal for the '<em><b>Start Date</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute VESSEL_EVENT__START_DATE = eINSTANCE.getVesselEvent_StartDate();

		/**
		 * The meta object literal for the '<em><b>End Date</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute VESSEL_EVENT__END_DATE = eINSTANCE.getVesselEvent_EndDate();

		/**
		 * The meta object literal for the '<em><b>Duration</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute VESSEL_EVENT__DURATION = eINSTANCE.getVesselEvent_Duration();

		/**
		 * The meta object literal for the '<em><b>Start Port</b></em>' reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference VESSEL_EVENT__START_PORT = eINSTANCE.getVesselEvent_StartPort();

		/**
		 * The meta object literal for the '<em><b>Vessels</b></em>' reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference VESSEL_EVENT__VESSELS = eINSTANCE.getVesselEvent_Vessels();

		/**
		 * The meta object literal for the '<em><b>Vessel Classes</b></em>' reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference VESSEL_EVENT__VESSEL_CLASSES = eINSTANCE.getVesselEvent_VesselClasses();

		/**
		 * The meta object literal for the '{@link scenario.fleet.impl.CharterOutImpl <em>Charter Out</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see scenario.fleet.impl.CharterOutImpl
		 * @see scenario.fleet.impl.FleetPackageImpl#getCharterOut()
		 * @generated
		 */
		EClass CHARTER_OUT = eINSTANCE.getCharterOut();

		/**
		 * The meta object literal for the '<em><b>End Port</b></em>' reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference CHARTER_OUT__END_PORT = eINSTANCE.getCharterOut_EndPort();

		/**
		 * The meta object literal for the '<em><b>Daily Charter Out Price</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute CHARTER_OUT__DAILY_CHARTER_OUT_PRICE = eINSTANCE.getCharterOut_DailyCharterOutPrice();

		/**
		 * The meta object literal for the '<em><b>Repositioning Fee</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute CHARTER_OUT__REPOSITIONING_FEE = eINSTANCE.getCharterOut_RepositioningFee();

		/**
		 * The meta object literal for the '<em><b>Get Effective End Port</b></em>' operation. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EOperation CHARTER_OUT___GET_EFFECTIVE_END_PORT = eINSTANCE.getCharterOut__GetEffectiveEndPort();

		/**
		 * The meta object literal for the '{@link scenario.fleet.impl.DrydockImpl <em>Drydock</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see scenario.fleet.impl.DrydockImpl
		 * @see scenario.fleet.impl.FleetPackageImpl#getDrydock()
		 * @generated
		 */
		EClass DRYDOCK = eINSTANCE.getDrydock();

		/**
		 * The meta object literal for the '{@link scenario.fleet.impl.VesselFuelImpl <em>Vessel Fuel</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see scenario.fleet.impl.VesselFuelImpl
		 * @see scenario.fleet.impl.FleetPackageImpl#getVesselFuel()
		 * @generated
		 */
		EClass VESSEL_FUEL = eINSTANCE.getVesselFuel();

		/**
		 * The meta object literal for the '<em><b>Unit Price</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute VESSEL_FUEL__UNIT_PRICE = eINSTANCE.getVesselFuel_UnitPrice();

		/**
		 * The meta object literal for the '<em><b>Equivalence Factor</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute VESSEL_FUEL__EQUIVALENCE_FACTOR = eINSTANCE.getVesselFuel_EquivalenceFactor();

		/**
		 * The meta object literal for the '{@link scenario.fleet.impl.PortExclusionImpl <em>Port Exclusion</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see scenario.fleet.impl.PortExclusionImpl
		 * @see scenario.fleet.impl.FleetPackageImpl#getPortExclusion()
		 * @generated
		 */
		EClass PORT_EXCLUSION = eINSTANCE.getPortExclusion();

		/**
		 * The meta object literal for the '<em><b>Port</b></em>' reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference PORT_EXCLUSION__PORT = eINSTANCE.getPortExclusion_Port();

		/**
		 * The meta object literal for the '{@link scenario.fleet.impl.VesselClassCostImpl <em>Vessel Class Cost</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see scenario.fleet.impl.VesselClassCostImpl
		 * @see scenario.fleet.impl.FleetPackageImpl#getVesselClassCost()
		 * @generated
		 */
		EClass VESSEL_CLASS_COST = eINSTANCE.getVesselClassCost();

		/**
		 * The meta object literal for the '<em><b>Canal</b></em>' reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference VESSEL_CLASS_COST__CANAL = eINSTANCE.getVesselClassCost_Canal();

		/**
		 * The meta object literal for the '<em><b>Laden Cost</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute VESSEL_CLASS_COST__LADEN_COST = eINSTANCE.getVesselClassCost_LadenCost();

		/**
		 * The meta object literal for the '<em><b>Unladen Cost</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute VESSEL_CLASS_COST__UNLADEN_COST = eINSTANCE.getVesselClassCost_UnladenCost();

		/**
		 * The meta object literal for the '<em><b>Transit Time</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute VESSEL_CLASS_COST__TRANSIT_TIME = eINSTANCE.getVesselClassCost_TransitTime();

		/**
		 * The meta object literal for the '<em><b>Transit Fuel</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute VESSEL_CLASS_COST__TRANSIT_FUEL = eINSTANCE.getVesselClassCost_TransitFuel();

		/**
		 * The meta object literal for the '{@link scenario.fleet.impl.PortTimeAndHeelImpl <em>Port Time And Heel</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see scenario.fleet.impl.PortTimeAndHeelImpl
		 * @see scenario.fleet.impl.FleetPackageImpl#getPortTimeAndHeel()
		 * @generated
		 */
		EClass PORT_TIME_AND_HEEL = eINSTANCE.getPortTimeAndHeel();

		/**
		 * The meta object literal for the '{@link scenario.fleet.impl.HeelOptionsImpl <em>Heel Options</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see scenario.fleet.impl.HeelOptionsImpl
		 * @see scenario.fleet.impl.FleetPackageImpl#getHeelOptions()
		 * @generated
		 */
		EClass HEEL_OPTIONS = eINSTANCE.getHeelOptions();

		/**
		 * The meta object literal for the '<em><b>Heel Limit</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute HEEL_OPTIONS__HEEL_LIMIT = eINSTANCE.getHeelOptions_HeelLimit();

		/**
		 * The meta object literal for the '<em><b>Heel CV Value</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute HEEL_OPTIONS__HEEL_CV_VALUE = eINSTANCE.getHeelOptions_HeelCVValue();

		/**
		 * The meta object literal for the '<em><b>Heel Unit Price</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute HEEL_OPTIONS__HEEL_UNIT_PRICE = eINSTANCE.getHeelOptions_HeelUnitPrice();

		/**
		 * The meta object literal for the '<em><b>Start Date</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute PORT_EXCLUSION__START_DATE = eINSTANCE.getPortExclusion_StartDate();

		/**
		 * The meta object literal for the '<em><b>End Date</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute PORT_EXCLUSION__END_DATE = eINSTANCE.getPortExclusion_EndDate();

		/**
		 * The meta object literal for the '{@link scenario.fleet.VesselState <em>Vessel State</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see scenario.fleet.VesselState
		 * @see scenario.fleet.impl.FleetPackageImpl#getVesselState()
		 * @generated
		 */
		EEnum VESSEL_STATE = eINSTANCE.getVesselState();

	}

} // FleetPackage
