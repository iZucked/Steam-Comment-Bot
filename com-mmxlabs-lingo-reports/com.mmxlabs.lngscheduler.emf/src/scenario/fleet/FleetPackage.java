/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package scenario.fleet;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

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
 * @see scenario.fleet.FleetFactory
 * @model kind="package"
 * @generated
 */
public interface FleetPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "fleet";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://com.mmxlabs.lng.emf/fleet";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "com.mmxlabs.lng.emf.fleet";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	FleetPackage eINSTANCE = scenario.fleet.impl.FleetPackageImpl.init();

	/**
	 * The meta object id for the '{@link scenario.fleet.impl.FleetModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.fleet.impl.FleetModelImpl
	 * @see scenario.fleet.impl.FleetPackageImpl#getFleetModel()
	 * @generated
	 */
	int FLEET_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Fleet</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL__FLEET = 0;

	/**
	 * The feature id for the '<em><b>Vessel Classes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL__VESSEL_CLASSES = 1;

	/**
	 * The feature id for the '<em><b>Charter Outs</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL__CHARTER_OUTS = 2;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link scenario.fleet.impl.VesselImpl <em>Vessel</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.fleet.impl.VesselImpl
	 * @see scenario.fleet.impl.FleetPackageImpl#getVessel()
	 * @generated
	 */
	int VESSEL = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__NAME = 0;

	/**
	 * The feature id for the '<em><b>Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__CLASS = 1;

	/**
	 * The feature id for the '<em><b>Start Requirement</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__START_REQUIREMENT = 2;

	/**
	 * The feature id for the '<em><b>End Requirement</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__END_REQUIREMENT = 3;

	/**
	 * The number of structural features of the '<em>Vessel</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link scenario.fleet.impl.VesselClassImpl <em>Vessel Class</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.fleet.impl.VesselClassImpl
	 * @see scenario.fleet.impl.FleetPackageImpl#getVesselClass()
	 * @generated
	 */
	int VESSEL_CLASS = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__NAME = 0;

	/**
	 * The feature id for the '<em><b>Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__CAPACITY = 1;

	/**
	 * The feature id for the '<em><b>Min Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__MIN_SPEED = 2;

	/**
	 * The feature id for the '<em><b>Max Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__MAX_SPEED = 3;

	/**
	 * The feature id for the '<em><b>Base Fuel Unit Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__BASE_FUEL_UNIT_PRICE = 4;

	/**
	 * The feature id for the '<em><b>Laden Attributes</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__LADEN_ATTRIBUTES = 5;

	/**
	 * The feature id for the '<em><b>Ballast Attributes</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__BALLAST_ATTRIBUTES = 6;

	/**
	 * The feature id for the '<em><b>Min Heel Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__MIN_HEEL_VOLUME = 7;

	/**
	 * The feature id for the '<em><b>Fill Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__FILL_CAPACITY = 8;

	/**
	 * The feature id for the '<em><b>Daily Charter Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__DAILY_CHARTER_PRICE = 9;

	/**
	 * The feature id for the '<em><b>Spot Charter Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__SPOT_CHARTER_COUNT = 10;

	/**
	 * The feature id for the '<em><b>Base Fuel Equivalence Factor</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__BASE_FUEL_EQUIVALENCE_FACTOR = 11;

	/**
	 * The feature id for the '<em><b>Inaccessible Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__INACCESSIBLE_PORTS = 12;

	/**
	 * The number of structural features of the '<em>Vessel Class</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS_FEATURE_COUNT = 13;

	/**
	 * The meta object id for the '{@link scenario.fleet.impl.FuelConsumptionLineImpl <em>Fuel Consumption Line</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.fleet.impl.FuelConsumptionLineImpl
	 * @see scenario.fleet.impl.FleetPackageImpl#getFuelConsumptionLine()
	 * @generated
	 */
	int FUEL_CONSUMPTION_LINE = 3;

	/**
	 * The feature id for the '<em><b>Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_CONSUMPTION_LINE__SPEED = 0;

	/**
	 * The feature id for the '<em><b>Consumption</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_CONSUMPTION_LINE__CONSUMPTION = 1;

	/**
	 * The number of structural features of the '<em>Fuel Consumption Line</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_CONSUMPTION_LINE_FEATURE_COUNT = 2;


	/**
	 * The meta object id for the '{@link scenario.fleet.impl.VesselStateAttributesImpl <em>Vessel State Attributes</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.fleet.impl.VesselStateAttributesImpl
	 * @see scenario.fleet.impl.FleetPackageImpl#getVesselStateAttributes()
	 * @generated
	 */
	int VESSEL_STATE_ATTRIBUTES = 4;

	/**
	 * The feature id for the '<em><b>Vessel State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES__VESSEL_STATE = 0;

	/**
	 * The feature id for the '<em><b>Nbo Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES__NBO_RATE = 1;

	/**
	 * The feature id for the '<em><b>Idle NBO Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE = 2;

	/**
	 * The feature id for the '<em><b>Idle Consumption Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES__IDLE_CONSUMPTION_RATE = 3;

	/**
	 * The feature id for the '<em><b>Fuel Consumption Curve</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_CURVE = 4;

	/**
	 * The number of structural features of the '<em>Vessel State Attributes</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link scenario.fleet.impl.PortAndTimeImpl <em>Port And Time</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.fleet.impl.PortAndTimeImpl
	 * @see scenario.fleet.impl.FleetPackageImpl#getPortAndTime()
	 * @generated
	 */
	int PORT_AND_TIME = 5;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_AND_TIME__PORT = 0;

	/**
	 * The feature id for the '<em><b>Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_AND_TIME__TIME = 1;

	/**
	 * The number of structural features of the '<em>Port And Time</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_AND_TIME_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link scenario.fleet.impl.CharterOutImpl <em>Charter Out</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.fleet.impl.CharterOutImpl
	 * @see scenario.fleet.impl.FleetPackageImpl#getCharterOut()
	 * @generated
	 */
	int CHARTER_OUT = 6;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT__PORT = 0;

	/**
	 * The feature id for the '<em><b>Vessels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT__VESSELS = 1;

	/**
	 * The feature id for the '<em><b>Vessel Classes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT__VESSEL_CLASSES = 2;

	/**
	 * The feature id for the '<em><b>Start Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT__START_DATE = 3;

	/**
	 * The feature id for the '<em><b>End Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT__END_DATE = 4;

	/**
	 * The feature id for the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT__DURATION = 5;

	/**
	 * The number of structural features of the '<em>Charter Out</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link scenario.fleet.VesselState <em>Vessel State</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.fleet.VesselState
	 * @see scenario.fleet.impl.FleetPackageImpl#getVesselState()
	 * @generated
	 */
	int VESSEL_STATE = 7;


	/**
	 * Returns the meta object for class '{@link scenario.fleet.FleetModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see scenario.fleet.FleetModel
	 * @generated
	 */
	EClass getFleetModel();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.fleet.FleetModel#getFleet <em>Fleet</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Fleet</em>'.
	 * @see scenario.fleet.FleetModel#getFleet()
	 * @see #getFleetModel()
	 * @generated
	 */
	EReference getFleetModel_Fleet();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.fleet.FleetModel#getVesselClasses <em>Vessel Classes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Vessel Classes</em>'.
	 * @see scenario.fleet.FleetModel#getVesselClasses()
	 * @see #getFleetModel()
	 * @generated
	 */
	EReference getFleetModel_VesselClasses();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.fleet.FleetModel#getCharterOuts <em>Charter Outs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Charter Outs</em>'.
	 * @see scenario.fleet.FleetModel#getCharterOuts()
	 * @see #getFleetModel()
	 * @generated
	 */
	EReference getFleetModel_CharterOuts();

	/**
	 * Returns the meta object for class '{@link scenario.fleet.Vessel <em>Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vessel</em>'.
	 * @see scenario.fleet.Vessel
	 * @generated
	 */
	EClass getVessel();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.Vessel#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see scenario.fleet.Vessel#getName()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_Name();

	/**
	 * Returns the meta object for the reference '{@link scenario.fleet.Vessel#getClass_ <em>Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Class</em>'.
	 * @see scenario.fleet.Vessel#getClass_()
	 * @see #getVessel()
	 * @generated
	 */
	EReference getVessel_Class();

	/**
	 * Returns the meta object for the containment reference '{@link scenario.fleet.Vessel#getStartRequirement <em>Start Requirement</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Start Requirement</em>'.
	 * @see scenario.fleet.Vessel#getStartRequirement()
	 * @see #getVessel()
	 * @generated
	 */
	EReference getVessel_StartRequirement();

	/**
	 * Returns the meta object for the containment reference '{@link scenario.fleet.Vessel#getEndRequirement <em>End Requirement</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>End Requirement</em>'.
	 * @see scenario.fleet.Vessel#getEndRequirement()
	 * @see #getVessel()
	 * @generated
	 */
	EReference getVessel_EndRequirement();

	/**
	 * Returns the meta object for class '{@link scenario.fleet.VesselClass <em>Vessel Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vessel Class</em>'.
	 * @see scenario.fleet.VesselClass
	 * @generated
	 */
	EClass getVesselClass();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselClass#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see scenario.fleet.VesselClass#getName()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_Name();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselClass#getCapacity <em>Capacity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Capacity</em>'.
	 * @see scenario.fleet.VesselClass#getCapacity()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_Capacity();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselClass#getMinSpeed <em>Min Speed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Speed</em>'.
	 * @see scenario.fleet.VesselClass#getMinSpeed()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_MinSpeed();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselClass#getMaxSpeed <em>Max Speed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Speed</em>'.
	 * @see scenario.fleet.VesselClass#getMaxSpeed()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_MaxSpeed();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselClass#getBaseFuelUnitPrice <em>Base Fuel Unit Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Base Fuel Unit Price</em>'.
	 * @see scenario.fleet.VesselClass#getBaseFuelUnitPrice()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_BaseFuelUnitPrice();

	/**
	 * Returns the meta object for the containment reference '{@link scenario.fleet.VesselClass#getLadenAttributes <em>Laden Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Laden Attributes</em>'.
	 * @see scenario.fleet.VesselClass#getLadenAttributes()
	 * @see #getVesselClass()
	 * @generated
	 */
	EReference getVesselClass_LadenAttributes();

	/**
	 * Returns the meta object for the containment reference '{@link scenario.fleet.VesselClass#getBallastAttributes <em>Ballast Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Ballast Attributes</em>'.
	 * @see scenario.fleet.VesselClass#getBallastAttributes()
	 * @see #getVesselClass()
	 * @generated
	 */
	EReference getVesselClass_BallastAttributes();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselClass#getMinHeelVolume <em>Min Heel Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Heel Volume</em>'.
	 * @see scenario.fleet.VesselClass#getMinHeelVolume()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_MinHeelVolume();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselClass#getFillCapacity <em>Fill Capacity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fill Capacity</em>'.
	 * @see scenario.fleet.VesselClass#getFillCapacity()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_FillCapacity();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselClass#getDailyCharterPrice <em>Daily Charter Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Daily Charter Price</em>'.
	 * @see scenario.fleet.VesselClass#getDailyCharterPrice()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_DailyCharterPrice();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselClass#getSpotCharterCount <em>Spot Charter Count</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Spot Charter Count</em>'.
	 * @see scenario.fleet.VesselClass#getSpotCharterCount()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_SpotCharterCount();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselClass#getBaseFuelEquivalenceFactor <em>Base Fuel Equivalence Factor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Base Fuel Equivalence Factor</em>'.
	 * @see scenario.fleet.VesselClass#getBaseFuelEquivalenceFactor()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_BaseFuelEquivalenceFactor();

	/**
	 * Returns the meta object for the reference list '{@link scenario.fleet.VesselClass#getInaccessiblePorts <em>Inaccessible Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Inaccessible Ports</em>'.
	 * @see scenario.fleet.VesselClass#getInaccessiblePorts()
	 * @see #getVesselClass()
	 * @generated
	 */
	EReference getVesselClass_InaccessiblePorts();

	/**
	 * Returns the meta object for class '{@link scenario.fleet.FuelConsumptionLine <em>Fuel Consumption Line</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Fuel Consumption Line</em>'.
	 * @see scenario.fleet.FuelConsumptionLine
	 * @generated
	 */
	EClass getFuelConsumptionLine();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.FuelConsumptionLine#getSpeed <em>Speed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Speed</em>'.
	 * @see scenario.fleet.FuelConsumptionLine#getSpeed()
	 * @see #getFuelConsumptionLine()
	 * @generated
	 */
	EAttribute getFuelConsumptionLine_Speed();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.FuelConsumptionLine#getConsumption <em>Consumption</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Consumption</em>'.
	 * @see scenario.fleet.FuelConsumptionLine#getConsumption()
	 * @see #getFuelConsumptionLine()
	 * @generated
	 */
	EAttribute getFuelConsumptionLine_Consumption();

	/**
	 * Returns the meta object for class '{@link scenario.fleet.VesselStateAttributes <em>Vessel State Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vessel State Attributes</em>'.
	 * @see scenario.fleet.VesselStateAttributes
	 * @generated
	 */
	EClass getVesselStateAttributes();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselStateAttributes#getVesselState <em>Vessel State</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Vessel State</em>'.
	 * @see scenario.fleet.VesselStateAttributes#getVesselState()
	 * @see #getVesselStateAttributes()
	 * @generated
	 */
	EAttribute getVesselStateAttributes_VesselState();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselStateAttributes#getNboRate <em>Nbo Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Nbo Rate</em>'.
	 * @see scenario.fleet.VesselStateAttributes#getNboRate()
	 * @see #getVesselStateAttributes()
	 * @generated
	 */
	EAttribute getVesselStateAttributes_NboRate();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselStateAttributes#getIdleNBORate <em>Idle NBO Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Idle NBO Rate</em>'.
	 * @see scenario.fleet.VesselStateAttributes#getIdleNBORate()
	 * @see #getVesselStateAttributes()
	 * @generated
	 */
	EAttribute getVesselStateAttributes_IdleNBORate();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselStateAttributes#getIdleConsumptionRate <em>Idle Consumption Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Idle Consumption Rate</em>'.
	 * @see scenario.fleet.VesselStateAttributes#getIdleConsumptionRate()
	 * @see #getVesselStateAttributes()
	 * @generated
	 */
	EAttribute getVesselStateAttributes_IdleConsumptionRate();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.fleet.VesselStateAttributes#getFuelConsumptionCurve <em>Fuel Consumption Curve</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Fuel Consumption Curve</em>'.
	 * @see scenario.fleet.VesselStateAttributes#getFuelConsumptionCurve()
	 * @see #getVesselStateAttributes()
	 * @generated
	 */
	EReference getVesselStateAttributes_FuelConsumptionCurve();

	/**
	 * Returns the meta object for class '{@link scenario.fleet.PortAndTime <em>Port And Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Port And Time</em>'.
	 * @see scenario.fleet.PortAndTime
	 * @generated
	 */
	EClass getPortAndTime();

	/**
	 * Returns the meta object for the reference '{@link scenario.fleet.PortAndTime#getPort <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Port</em>'.
	 * @see scenario.fleet.PortAndTime#getPort()
	 * @see #getPortAndTime()
	 * @generated
	 */
	EReference getPortAndTime_Port();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.PortAndTime#getTime <em>Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Time</em>'.
	 * @see scenario.fleet.PortAndTime#getTime()
	 * @see #getPortAndTime()
	 * @generated
	 */
	EAttribute getPortAndTime_Time();

	/**
	 * Returns the meta object for class '{@link scenario.fleet.CharterOut <em>Charter Out</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Charter Out</em>'.
	 * @see scenario.fleet.CharterOut
	 * @generated
	 */
	EClass getCharterOut();

	/**
	 * Returns the meta object for the reference '{@link scenario.fleet.CharterOut#getPort <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Port</em>'.
	 * @see scenario.fleet.CharterOut#getPort()
	 * @see #getCharterOut()
	 * @generated
	 */
	EReference getCharterOut_Port();

	/**
	 * Returns the meta object for the reference list '{@link scenario.fleet.CharterOut#getVessels <em>Vessels</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Vessels</em>'.
	 * @see scenario.fleet.CharterOut#getVessels()
	 * @see #getCharterOut()
	 * @generated
	 */
	EReference getCharterOut_Vessels();

	/**
	 * Returns the meta object for the reference list '{@link scenario.fleet.CharterOut#getVesselClasses <em>Vessel Classes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Vessel Classes</em>'.
	 * @see scenario.fleet.CharterOut#getVesselClasses()
	 * @see #getCharterOut()
	 * @generated
	 */
	EReference getCharterOut_VesselClasses();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.CharterOut#getStartDate <em>Start Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start Date</em>'.
	 * @see scenario.fleet.CharterOut#getStartDate()
	 * @see #getCharterOut()
	 * @generated
	 */
	EAttribute getCharterOut_StartDate();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.CharterOut#getEndDate <em>End Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End Date</em>'.
	 * @see scenario.fleet.CharterOut#getEndDate()
	 * @see #getCharterOut()
	 * @generated
	 */
	EAttribute getCharterOut_EndDate();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.CharterOut#getDuration <em>Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Duration</em>'.
	 * @see scenario.fleet.CharterOut#getDuration()
	 * @see #getCharterOut()
	 * @generated
	 */
	EAttribute getCharterOut_Duration();

	/**
	 * Returns the meta object for enum '{@link scenario.fleet.VesselState <em>Vessel State</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Vessel State</em>'.
	 * @see scenario.fleet.VesselState
	 * @generated
	 */
	EEnum getVesselState();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	FleetFactory getFleetFactory();

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
		 * The meta object literal for the '{@link scenario.fleet.impl.FleetModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.fleet.impl.FleetModelImpl
		 * @see scenario.fleet.impl.FleetPackageImpl#getFleetModel()
		 * @generated
		 */
		EClass FLEET_MODEL = eINSTANCE.getFleetModel();

		/**
		 * The meta object literal for the '<em><b>Fleet</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FLEET_MODEL__FLEET = eINSTANCE.getFleetModel_Fleet();

		/**
		 * The meta object literal for the '<em><b>Vessel Classes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FLEET_MODEL__VESSEL_CLASSES = eINSTANCE.getFleetModel_VesselClasses();

		/**
		 * The meta object literal for the '<em><b>Charter Outs</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FLEET_MODEL__CHARTER_OUTS = eINSTANCE.getFleetModel_CharterOuts();

		/**
		 * The meta object literal for the '{@link scenario.fleet.impl.VesselImpl <em>Vessel</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.fleet.impl.VesselImpl
		 * @see scenario.fleet.impl.FleetPackageImpl#getVessel()
		 * @generated
		 */
		EClass VESSEL = eINSTANCE.getVessel();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL__NAME = eINSTANCE.getVessel_Name();

		/**
		 * The meta object literal for the '<em><b>Class</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL__CLASS = eINSTANCE.getVessel_Class();

		/**
		 * The meta object literal for the '<em><b>Start Requirement</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL__START_REQUIREMENT = eINSTANCE.getVessel_StartRequirement();

		/**
		 * The meta object literal for the '<em><b>End Requirement</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL__END_REQUIREMENT = eINSTANCE.getVessel_EndRequirement();

		/**
		 * The meta object literal for the '{@link scenario.fleet.impl.VesselClassImpl <em>Vessel Class</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.fleet.impl.VesselClassImpl
		 * @see scenario.fleet.impl.FleetPackageImpl#getVesselClass()
		 * @generated
		 */
		EClass VESSEL_CLASS = eINSTANCE.getVesselClass();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS__NAME = eINSTANCE.getVesselClass_Name();

		/**
		 * The meta object literal for the '<em><b>Capacity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS__CAPACITY = eINSTANCE.getVesselClass_Capacity();

		/**
		 * The meta object literal for the '<em><b>Min Speed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS__MIN_SPEED = eINSTANCE.getVesselClass_MinSpeed();

		/**
		 * The meta object literal for the '<em><b>Max Speed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS__MAX_SPEED = eINSTANCE.getVesselClass_MaxSpeed();

		/**
		 * The meta object literal for the '<em><b>Base Fuel Unit Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS__BASE_FUEL_UNIT_PRICE = eINSTANCE.getVesselClass_BaseFuelUnitPrice();

		/**
		 * The meta object literal for the '<em><b>Laden Attributes</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_CLASS__LADEN_ATTRIBUTES = eINSTANCE.getVesselClass_LadenAttributes();

		/**
		 * The meta object literal for the '<em><b>Ballast Attributes</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_CLASS__BALLAST_ATTRIBUTES = eINSTANCE.getVesselClass_BallastAttributes();

		/**
		 * The meta object literal for the '<em><b>Min Heel Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS__MIN_HEEL_VOLUME = eINSTANCE.getVesselClass_MinHeelVolume();

		/**
		 * The meta object literal for the '<em><b>Fill Capacity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS__FILL_CAPACITY = eINSTANCE.getVesselClass_FillCapacity();

		/**
		 * The meta object literal for the '<em><b>Daily Charter Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS__DAILY_CHARTER_PRICE = eINSTANCE.getVesselClass_DailyCharterPrice();

		/**
		 * The meta object literal for the '<em><b>Spot Charter Count</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS__SPOT_CHARTER_COUNT = eINSTANCE.getVesselClass_SpotCharterCount();

		/**
		 * The meta object literal for the '<em><b>Base Fuel Equivalence Factor</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS__BASE_FUEL_EQUIVALENCE_FACTOR = eINSTANCE.getVesselClass_BaseFuelEquivalenceFactor();

		/**
		 * The meta object literal for the '<em><b>Inaccessible Ports</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_CLASS__INACCESSIBLE_PORTS = eINSTANCE.getVesselClass_InaccessiblePorts();

		/**
		 * The meta object literal for the '{@link scenario.fleet.impl.FuelConsumptionLineImpl <em>Fuel Consumption Line</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.fleet.impl.FuelConsumptionLineImpl
		 * @see scenario.fleet.impl.FleetPackageImpl#getFuelConsumptionLine()
		 * @generated
		 */
		EClass FUEL_CONSUMPTION_LINE = eINSTANCE.getFuelConsumptionLine();

		/**
		 * The meta object literal for the '<em><b>Speed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FUEL_CONSUMPTION_LINE__SPEED = eINSTANCE.getFuelConsumptionLine_Speed();

		/**
		 * The meta object literal for the '<em><b>Consumption</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FUEL_CONSUMPTION_LINE__CONSUMPTION = eINSTANCE.getFuelConsumptionLine_Consumption();

		/**
		 * The meta object literal for the '{@link scenario.fleet.impl.VesselStateAttributesImpl <em>Vessel State Attributes</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.fleet.impl.VesselStateAttributesImpl
		 * @see scenario.fleet.impl.FleetPackageImpl#getVesselStateAttributes()
		 * @generated
		 */
		EClass VESSEL_STATE_ATTRIBUTES = eINSTANCE.getVesselStateAttributes();

		/**
		 * The meta object literal for the '<em><b>Vessel State</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_STATE_ATTRIBUTES__VESSEL_STATE = eINSTANCE.getVesselStateAttributes_VesselState();

		/**
		 * The meta object literal for the '<em><b>Nbo Rate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_STATE_ATTRIBUTES__NBO_RATE = eINSTANCE.getVesselStateAttributes_NboRate();

		/**
		 * The meta object literal for the '<em><b>Idle NBO Rate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE = eINSTANCE.getVesselStateAttributes_IdleNBORate();

		/**
		 * The meta object literal for the '<em><b>Idle Consumption Rate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_STATE_ATTRIBUTES__IDLE_CONSUMPTION_RATE = eINSTANCE.getVesselStateAttributes_IdleConsumptionRate();

		/**
		 * The meta object literal for the '<em><b>Fuel Consumption Curve</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_CURVE = eINSTANCE.getVesselStateAttributes_FuelConsumptionCurve();

		/**
		 * The meta object literal for the '{@link scenario.fleet.impl.PortAndTimeImpl <em>Port And Time</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.fleet.impl.PortAndTimeImpl
		 * @see scenario.fleet.impl.FleetPackageImpl#getPortAndTime()
		 * @generated
		 */
		EClass PORT_AND_TIME = eINSTANCE.getPortAndTime();

		/**
		 * The meta object literal for the '<em><b>Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PORT_AND_TIME__PORT = eINSTANCE.getPortAndTime_Port();

		/**
		 * The meta object literal for the '<em><b>Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT_AND_TIME__TIME = eINSTANCE.getPortAndTime_Time();

		/**
		 * The meta object literal for the '{@link scenario.fleet.impl.CharterOutImpl <em>Charter Out</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.fleet.impl.CharterOutImpl
		 * @see scenario.fleet.impl.FleetPackageImpl#getCharterOut()
		 * @generated
		 */
		EClass CHARTER_OUT = eINSTANCE.getCharterOut();

		/**
		 * The meta object literal for the '<em><b>Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHARTER_OUT__PORT = eINSTANCE.getCharterOut_Port();

		/**
		 * The meta object literal for the '<em><b>Vessels</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHARTER_OUT__VESSELS = eINSTANCE.getCharterOut_Vessels();

		/**
		 * The meta object literal for the '<em><b>Vessel Classes</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHARTER_OUT__VESSEL_CLASSES = eINSTANCE.getCharterOut_VesselClasses();

		/**
		 * The meta object literal for the '<em><b>Start Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHARTER_OUT__START_DATE = eINSTANCE.getCharterOut_StartDate();

		/**
		 * The meta object literal for the '<em><b>End Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHARTER_OUT__END_DATE = eINSTANCE.getCharterOut_EndDate();

		/**
		 * The meta object literal for the '<em><b>Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHARTER_OUT__DURATION = eINSTANCE.getCharterOut_Duration();

		/**
		 * The meta object literal for the '{@link scenario.fleet.VesselState <em>Vessel State</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.fleet.VesselState
		 * @see scenario.fleet.impl.FleetPackageImpl#getVesselState()
		 * @generated
		 */
		EEnum VESSEL_STATE = eINSTANCE.getVesselState();

	}

} //FleetPackage
