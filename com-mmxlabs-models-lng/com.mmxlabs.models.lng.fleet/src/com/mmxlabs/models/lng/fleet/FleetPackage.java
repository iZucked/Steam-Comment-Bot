/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
 * @see com.mmxlabs.models.lng.fleet.FleetFactory
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
	String eNS_URI = "http://www.mmxlabs.com/models/lng/fleet/1/";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "lng.fleet";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	FleetPackage eINSTANCE = com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl <em>Vessel</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.fleet.impl.VesselImpl
	 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getVessel()
	 * @generated
	 */
	int VESSEL = 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.fleet.impl.FleetModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.fleet.impl.FleetModelImpl
	 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getFleetModel()
	 * @generated
	 */
	int FLEET_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Vessels</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL__VESSELS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Base Fuels</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL__BASE_FUELS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Vessel Groups</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL__VESSEL_GROUPS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Fleet Version Record</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL__FLEET_VERSION_RECORD = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Vessel Group Version Record</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL__VESSEL_GROUP_VERSION_RECORD = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Bunker Fuels Version Record</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL__BUNKER_FUELS_VERSION_RECORD = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>MMX Vessel DB Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL__MMX_VESSEL_DB_VERSION = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.fleet.impl.BaseFuelImpl <em>Base Fuel</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.fleet.impl.BaseFuelImpl
	 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getBaseFuel()
	 * @generated
	 */
	int BASE_FUEL = 1;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Equivalence Factor</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL__EQUIVALENCE_FACTOR = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Base Fuel</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__EXTENSIONS = TypesPackage.AVESSEL_SET__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__UUID = TypesPackage.AVESSEL_SET__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__NAME = TypesPackage.AVESSEL_SET__NAME;

	/**
	 * The feature id for the '<em><b>Short Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__SHORT_NAME = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>IMO</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__IMO = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__TYPE = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__REFERENCE = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__CAPACITY = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Fill Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__FILL_CAPACITY = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Scnt</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__SCNT = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Base Fuel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__BASE_FUEL = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>In Port Base Fuel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__IN_PORT_BASE_FUEL = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Pilot Light Base Fuel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__PILOT_LIGHT_BASE_FUEL = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Idle Base Fuel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__IDLE_BASE_FUEL = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Pilot Light Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__PILOT_LIGHT_RATE = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>Safety Heel</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__SAFETY_HEEL = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 12;

	/**
	 * The feature id for the '<em><b>Warming Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__WARMING_TIME = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 13;

	/**
	 * The feature id for the '<em><b>Cooling Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__COOLING_VOLUME = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 14;

	/**
	 * The feature id for the '<em><b>Cooling Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__COOLING_TIME = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 15;

	/**
	 * The feature id for the '<em><b>Purge Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__PURGE_VOLUME = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 16;

	/**
	 * The feature id for the '<em><b>Purge Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__PURGE_TIME = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 17;

	/**
	 * The feature id for the '<em><b>Laden Attributes</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__LADEN_ATTRIBUTES = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 18;

	/**
	 * The feature id for the '<em><b>Ballast Attributes</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__BALLAST_ATTRIBUTES = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 19;

	/**
	 * The feature id for the '<em><b>Min Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__MIN_SPEED = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 20;

	/**
	 * The feature id for the '<em><b>Max Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__MAX_SPEED = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 21;

	/**
	 * The feature id for the '<em><b>Inaccessible Ports Override</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__INACCESSIBLE_PORTS_OVERRIDE = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 22;

	/**
	 * The feature id for the '<em><b>Inaccessible Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__INACCESSIBLE_PORTS = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 23;

	/**
	 * The feature id for the '<em><b>Inaccessible Routes Override</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__INACCESSIBLE_ROUTES_OVERRIDE = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 24;

	/**
	 * The feature id for the '<em><b>Inaccessible Routes</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__INACCESSIBLE_ROUTES = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 25;

	/**
	 * The feature id for the '<em><b>Route Parameters Override</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__ROUTE_PARAMETERS_OVERRIDE = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 26;

	/**
	 * The feature id for the '<em><b>Route Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__ROUTE_PARAMETERS = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 27;

	/**
	 * The feature id for the '<em><b>Min Base Fuel Consumption</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__MIN_BASE_FUEL_CONSUMPTION = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 28;

	/**
	 * The feature id for the '<em><b>Has Reliq Capability Override</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__HAS_RELIQ_CAPABILITY_OVERRIDE = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 29;

	/**
	 * The feature id for the '<em><b>Has Reliq Capability</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__HAS_RELIQ_CAPABILITY = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 30;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__NOTES = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 31;

	/**
	 * The feature id for the '<em><b>Mmx Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__MMX_ID = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 32;

	/**
	 * The feature id for the '<em><b>Reference Vessel</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__REFERENCE_VESSEL = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 33;

	/**
	 * The feature id for the '<em><b>Mmx Reference</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__MMX_REFERENCE = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 34;

	/**
	 * The number of structural features of the '<em>Vessel</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_FEATURE_COUNT = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 35;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.fleet.impl.VesselStateAttributesImpl <em>Vessel State Attributes</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.fleet.impl.VesselStateAttributesImpl
	 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getVesselStateAttributes()
	 * @generated
	 */
	int VESSEL_STATE_ATTRIBUTES = 4;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.fleet.impl.FuelConsumptionImpl <em>Fuel Consumption</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.fleet.impl.FuelConsumptionImpl
	 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getFuelConsumption()
	 * @generated
	 */
	int FUEL_CONSUMPTION = 5;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.fleet.impl.VesselClassRouteParametersImpl <em>Vessel Class Route Parameters</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.fleet.impl.VesselClassRouteParametersImpl
	 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getVesselClassRouteParameters()
	 * @generated
	 */
	int VESSEL_CLASS_ROUTE_PARAMETERS = 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.fleet.impl.VesselGroupImpl <em>Vessel Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.fleet.impl.VesselGroupImpl
	 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getVesselGroup()
	 * @generated
	 */
	int VESSEL_GROUP = 3;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_GROUP__EXTENSIONS = TypesPackage.AVESSEL_SET__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_GROUP__UUID = TypesPackage.AVESSEL_SET__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_GROUP__NAME = TypesPackage.AVESSEL_SET__NAME;

	/**
	 * The feature id for the '<em><b>Vessels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_GROUP__VESSELS = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Vessel Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_GROUP_FEATURE_COUNT = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 1;


	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Nbo Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES__NBO_RATE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Idle NBO Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Idle Base Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES__IDLE_BASE_RATE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>In Port Base Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES__IN_PORT_BASE_RATE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Fuel Consumption Override</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_OVERRIDE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Fuel Consumption</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Service Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES__SERVICE_SPEED = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>In Port NBO Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES__IN_PORT_NBO_RATE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The number of structural features of the '<em>Vessel State Attributes</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_CONSUMPTION__SPEED = 0;

	/**
	 * The feature id for the '<em><b>Consumption</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_CONSUMPTION__CONSUMPTION = 1;

	/**
	 * The number of structural features of the '<em>Fuel Consumption</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_CONSUMPTION_FEATURE_COUNT = 2;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS_ROUTE_PARAMETERS__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Route Option</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS_ROUTE_PARAMETERS__ROUTE_OPTION = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Extra Transit Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS_ROUTE_PARAMETERS__EXTRA_TRANSIT_TIME = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Laden Consumption Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS_ROUTE_PARAMETERS__LADEN_CONSUMPTION_RATE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Laden NBO Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS_ROUTE_PARAMETERS__LADEN_NBO_RATE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Ballast Consumption Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS_ROUTE_PARAMETERS__BALLAST_CONSUMPTION_RATE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Ballast NBO Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS_ROUTE_PARAMETERS__BALLAST_NBO_RATE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Vessel Class Route Parameters</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS_ROUTE_PARAMETERS_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 6;

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.fleet.Vessel <em>Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vessel</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel
	 * @generated
	 */
	EClass getVessel();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.Vessel#getShortName <em>Short Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Short Name</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getShortName()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_ShortName();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.Vessel#getIMO <em>IMO</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>IMO</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getIMO()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_IMO();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.Vessel#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getType()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_Type();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.fleet.Vessel#getReference <em>Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Reference</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getReference()
	 * @see #getVessel()
	 * @generated
	 */
	EReference getVessel_Reference();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.fleet.Vessel#getInaccessiblePorts <em>Inaccessible Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Inaccessible Ports</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getInaccessiblePorts()
	 * @see #getVessel()
	 * @generated
	 */
	EReference getVessel_InaccessiblePorts();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.Vessel#isInaccessibleRoutesOverride <em>Inaccessible Routes Override</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Inaccessible Routes Override</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#isInaccessibleRoutesOverride()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_InaccessibleRoutesOverride();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.Vessel#getCapacity <em>Capacity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Capacity</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getCapacity()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_Capacity();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.Vessel#getFillCapacity <em>Fill Capacity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fill Capacity</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getFillCapacity()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_FillCapacity();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.fleet.Vessel#getLadenAttributes <em>Laden Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Laden Attributes</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getLadenAttributes()
	 * @see #getVessel()
	 * @generated
	 */
	EReference getVessel_LadenAttributes();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.fleet.Vessel#getBallastAttributes <em>Ballast Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Ballast Attributes</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getBallastAttributes()
	 * @see #getVessel()
	 * @generated
	 */
	EReference getVessel_BallastAttributes();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.Vessel#getMinSpeed <em>Min Speed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Speed</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getMinSpeed()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_MinSpeed();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.Vessel#getMaxSpeed <em>Max Speed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Speed</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getMaxSpeed()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_MaxSpeed();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.Vessel#getSafetyHeel <em>Safety Heel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Safety Heel</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getSafetyHeel()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_SafetyHeel();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.Vessel#getWarmingTime <em>Warming Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Warming Time</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getWarmingTime()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_WarmingTime();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.Vessel#getPurgeTime <em>Purge Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Purge Time</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getPurgeTime()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_PurgeTime();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.Vessel#getCoolingVolume <em>Cooling Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cooling Volume</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getCoolingVolume()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_CoolingVolume();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.Vessel#getCoolingTime <em>Cooling Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cooling Time</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getCoolingTime()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_CoolingTime();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.Vessel#getPurgeVolume <em>Purge Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Purge Volume</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getPurgeVolume()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_PurgeVolume();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.Vessel#isRouteParametersOverride <em>Route Parameters Override</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Route Parameters Override</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#isRouteParametersOverride()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_RouteParametersOverride();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.fleet.Vessel#getRouteParameters <em>Route Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Route Parameters</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getRouteParameters()
	 * @see #getVessel()
	 * @generated
	 */
	EReference getVessel_RouteParameters();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.Vessel#getPilotLightRate <em>Pilot Light Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Pilot Light Rate</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getPilotLightRate()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_PilotLightRate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.Vessel#getMinBaseFuelConsumption <em>Min Base Fuel Consumption</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Base Fuel Consumption</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getMinBaseFuelConsumption()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_MinBaseFuelConsumption();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.Vessel#isHasReliqCapabilityOverride <em>Has Reliq Capability Override</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Has Reliq Capability Override</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#isHasReliqCapabilityOverride()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_HasReliqCapabilityOverride();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.Vessel#isHasReliqCapability <em>Has Reliq Capability</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Has Reliq Capability</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#isHasReliqCapability()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_HasReliqCapability();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.Vessel#getNotes <em>Notes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Notes</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getNotes()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_Notes();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.Vessel#getMmxId <em>Mmx Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Mmx Id</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getMmxId()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_MmxId();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.Vessel#isReferenceVessel <em>Reference Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Reference Vessel</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#isReferenceVessel()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_ReferenceVessel();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.Vessel#isMmxReference <em>Mmx Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Mmx Reference</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#isMmxReference()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_MmxReference();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.Vessel#getScnt <em>Scnt</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Scnt</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getScnt()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_Scnt();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.Vessel#isInaccessiblePortsOverride <em>Inaccessible Ports Override</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Inaccessible Ports Override</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#isInaccessiblePortsOverride()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_InaccessiblePortsOverride();

	/**
	 * Returns the meta object for the attribute list '{@link com.mmxlabs.models.lng.fleet.Vessel#getInaccessibleRoutes <em>Inaccessible Routes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Inaccessible Routes</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getInaccessibleRoutes()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_InaccessibleRoutes();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.fleet.Vessel#getBaseFuel <em>Base Fuel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Base Fuel</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getBaseFuel()
	 * @see #getVessel()
	 * @generated
	 */
	EReference getVessel_BaseFuel();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.fleet.Vessel#getInPortBaseFuel <em>In Port Base Fuel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>In Port Base Fuel</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getInPortBaseFuel()
	 * @see #getVessel()
	 * @generated
	 */
	EReference getVessel_InPortBaseFuel();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.fleet.Vessel#getPilotLightBaseFuel <em>Pilot Light Base Fuel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Pilot Light Base Fuel</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getPilotLightBaseFuel()
	 * @see #getVessel()
	 * @generated
	 */
	EReference getVessel_PilotLightBaseFuel();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.fleet.Vessel#getIdleBaseFuel <em>Idle Base Fuel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Idle Base Fuel</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getIdleBaseFuel()
	 * @see #getVessel()
	 * @generated
	 */
	EReference getVessel_IdleBaseFuel();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.fleet.FleetModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see com.mmxlabs.models.lng.fleet.FleetModel
	 * @generated
	 */
	EClass getFleetModel();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.fleet.FleetModel#getVessels <em>Vessels</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Vessels</em>'.
	 * @see com.mmxlabs.models.lng.fleet.FleetModel#getVessels()
	 * @see #getFleetModel()
	 * @generated
	 */
	EReference getFleetModel_Vessels();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.fleet.FleetModel#getBaseFuels <em>Base Fuels</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Base Fuels</em>'.
	 * @see com.mmxlabs.models.lng.fleet.FleetModel#getBaseFuels()
	 * @see #getFleetModel()
	 * @generated
	 */
	EReference getFleetModel_BaseFuels();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.fleet.FleetModel#getVesselGroups <em>Vessel Groups</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Vessel Groups</em>'.
	 * @see com.mmxlabs.models.lng.fleet.FleetModel#getVesselGroups()
	 * @see #getFleetModel()
	 * @generated
	 */
	EReference getFleetModel_VesselGroups();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.fleet.FleetModel#getFleetVersionRecord <em>Fleet Version Record</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Fleet Version Record</em>'.
	 * @see com.mmxlabs.models.lng.fleet.FleetModel#getFleetVersionRecord()
	 * @see #getFleetModel()
	 * @generated
	 */
	EReference getFleetModel_FleetVersionRecord();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.fleet.FleetModel#getVesselGroupVersionRecord <em>Vessel Group Version Record</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Vessel Group Version Record</em>'.
	 * @see com.mmxlabs.models.lng.fleet.FleetModel#getVesselGroupVersionRecord()
	 * @see #getFleetModel()
	 * @generated
	 */
	EReference getFleetModel_VesselGroupVersionRecord();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.fleet.FleetModel#getBunkerFuelsVersionRecord <em>Bunker Fuels Version Record</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Bunker Fuels Version Record</em>'.
	 * @see com.mmxlabs.models.lng.fleet.FleetModel#getBunkerFuelsVersionRecord()
	 * @see #getFleetModel()
	 * @generated
	 */
	EReference getFleetModel_BunkerFuelsVersionRecord();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.FleetModel#getMMXVesselDBVersion <em>MMX Vessel DB Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>MMX Vessel DB Version</em>'.
	 * @see com.mmxlabs.models.lng.fleet.FleetModel#getMMXVesselDBVersion()
	 * @see #getFleetModel()
	 * @generated
	 */
	EAttribute getFleetModel_MMXVesselDBVersion();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.fleet.BaseFuel <em>Base Fuel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Base Fuel</em>'.
	 * @see com.mmxlabs.models.lng.fleet.BaseFuel
	 * @generated
	 */
	EClass getBaseFuel();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.BaseFuel#getEquivalenceFactor <em>Equivalence Factor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Equivalence Factor</em>'.
	 * @see com.mmxlabs.models.lng.fleet.BaseFuel#getEquivalenceFactor()
	 * @see #getBaseFuel()
	 * @generated
	 */
	EAttribute getBaseFuel_EquivalenceFactor();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes <em>Vessel State Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vessel State Attributes</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselStateAttributes
	 * @generated
	 */
	EClass getVesselStateAttributes();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getNboRate <em>Nbo Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Nbo Rate</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselStateAttributes#getNboRate()
	 * @see #getVesselStateAttributes()
	 * @generated
	 */
	EAttribute getVesselStateAttributes_NboRate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getIdleNBORate <em>Idle NBO Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Idle NBO Rate</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselStateAttributes#getIdleNBORate()
	 * @see #getVesselStateAttributes()
	 * @generated
	 */
	EAttribute getVesselStateAttributes_IdleNBORate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getIdleBaseRate <em>Idle Base Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Idle Base Rate</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselStateAttributes#getIdleBaseRate()
	 * @see #getVesselStateAttributes()
	 * @generated
	 */
	EAttribute getVesselStateAttributes_IdleBaseRate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getInPortBaseRate <em>In Port Base Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>In Port Base Rate</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselStateAttributes#getInPortBaseRate()
	 * @see #getVesselStateAttributes()
	 * @generated
	 */
	EAttribute getVesselStateAttributes_InPortBaseRate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#isFuelConsumptionOverride <em>Fuel Consumption Override</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fuel Consumption Override</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselStateAttributes#isFuelConsumptionOverride()
	 * @see #getVesselStateAttributes()
	 * @generated
	 */
	EAttribute getVesselStateAttributes_FuelConsumptionOverride();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getFuelConsumption <em>Fuel Consumption</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Fuel Consumption</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselStateAttributes#getFuelConsumption()
	 * @see #getVesselStateAttributes()
	 * @generated
	 */
	EReference getVesselStateAttributes_FuelConsumption();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getServiceSpeed <em>Service Speed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Service Speed</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselStateAttributes#getServiceSpeed()
	 * @see #getVesselStateAttributes()
	 * @generated
	 */
	EAttribute getVesselStateAttributes_ServiceSpeed();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes#getInPortNBORate <em>In Port NBO Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>In Port NBO Rate</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselStateAttributes#getInPortNBORate()
	 * @see #getVesselStateAttributes()
	 * @generated
	 */
	EAttribute getVesselStateAttributes_InPortNBORate();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.fleet.FuelConsumption <em>Fuel Consumption</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Fuel Consumption</em>'.
	 * @see com.mmxlabs.models.lng.fleet.FuelConsumption
	 * @generated
	 */
	EClass getFuelConsumption();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.FuelConsumption#getSpeed <em>Speed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Speed</em>'.
	 * @see com.mmxlabs.models.lng.fleet.FuelConsumption#getSpeed()
	 * @see #getFuelConsumption()
	 * @generated
	 */
	EAttribute getFuelConsumption_Speed();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.FuelConsumption#getConsumption <em>Consumption</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Consumption</em>'.
	 * @see com.mmxlabs.models.lng.fleet.FuelConsumption#getConsumption()
	 * @see #getFuelConsumption()
	 * @generated
	 */
	EAttribute getFuelConsumption_Consumption();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.fleet.VesselClassRouteParameters <em>Vessel Class Route Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vessel Class Route Parameters</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselClassRouteParameters
	 * @generated
	 */
	EClass getVesselClassRouteParameters();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselClassRouteParameters#getRouteOption <em>Route Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Route Option</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselClassRouteParameters#getRouteOption()
	 * @see #getVesselClassRouteParameters()
	 * @generated
	 */
	EAttribute getVesselClassRouteParameters_RouteOption();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselClassRouteParameters#getExtraTransitTime <em>Extra Transit Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Extra Transit Time</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselClassRouteParameters#getExtraTransitTime()
	 * @see #getVesselClassRouteParameters()
	 * @generated
	 */
	EAttribute getVesselClassRouteParameters_ExtraTransitTime();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselClassRouteParameters#getLadenConsumptionRate <em>Laden Consumption Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Laden Consumption Rate</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselClassRouteParameters#getLadenConsumptionRate()
	 * @see #getVesselClassRouteParameters()
	 * @generated
	 */
	EAttribute getVesselClassRouteParameters_LadenConsumptionRate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselClassRouteParameters#getLadenNBORate <em>Laden NBO Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Laden NBO Rate</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselClassRouteParameters#getLadenNBORate()
	 * @see #getVesselClassRouteParameters()
	 * @generated
	 */
	EAttribute getVesselClassRouteParameters_LadenNBORate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselClassRouteParameters#getBallastConsumptionRate <em>Ballast Consumption Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ballast Consumption Rate</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselClassRouteParameters#getBallastConsumptionRate()
	 * @see #getVesselClassRouteParameters()
	 * @generated
	 */
	EAttribute getVesselClassRouteParameters_BallastConsumptionRate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselClassRouteParameters#getBallastNBORate <em>Ballast NBO Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ballast NBO Rate</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselClassRouteParameters#getBallastNBORate()
	 * @see #getVesselClassRouteParameters()
	 * @generated
	 */
	EAttribute getVesselClassRouteParameters_BallastNBORate();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.fleet.VesselGroup <em>Vessel Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vessel Group</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselGroup
	 * @generated
	 */
	EClass getVesselGroup();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.fleet.VesselGroup#getVessels <em>Vessels</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Vessels</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselGroup#getVessels()
	 * @see #getVesselGroup()
	 * @generated
	 */
	EReference getVesselGroup_Vessels();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.fleet.impl.VesselImpl <em>Vessel</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.fleet.impl.VesselImpl
		 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getVessel()
		 * @generated
		 */
		EClass VESSEL = eINSTANCE.getVessel();

		/**
		 * The meta object literal for the '<em><b>Short Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL__SHORT_NAME = eINSTANCE.getVessel_ShortName();

		/**
		 * The meta object literal for the '<em><b>IMO</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL__IMO = eINSTANCE.getVessel_IMO();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL__TYPE = eINSTANCE.getVessel_Type();

		/**
		 * The meta object literal for the '<em><b>Reference</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL__REFERENCE = eINSTANCE.getVessel_Reference();

		/**
		 * The meta object literal for the '<em><b>Inaccessible Ports</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL__INACCESSIBLE_PORTS = eINSTANCE.getVessel_InaccessiblePorts();

		/**
		 * The meta object literal for the '<em><b>Inaccessible Routes Override</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL__INACCESSIBLE_ROUTES_OVERRIDE = eINSTANCE.getVessel_InaccessibleRoutesOverride();

		/**
		 * The meta object literal for the '<em><b>Capacity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL__CAPACITY = eINSTANCE.getVessel_Capacity();

		/**
		 * The meta object literal for the '<em><b>Fill Capacity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL__FILL_CAPACITY = eINSTANCE.getVessel_FillCapacity();

		/**
		 * The meta object literal for the '<em><b>Laden Attributes</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL__LADEN_ATTRIBUTES = eINSTANCE.getVessel_LadenAttributes();

		/**
		 * The meta object literal for the '<em><b>Ballast Attributes</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL__BALLAST_ATTRIBUTES = eINSTANCE.getVessel_BallastAttributes();

		/**
		 * The meta object literal for the '<em><b>Min Speed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL__MIN_SPEED = eINSTANCE.getVessel_MinSpeed();

		/**
		 * The meta object literal for the '<em><b>Max Speed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL__MAX_SPEED = eINSTANCE.getVessel_MaxSpeed();

		/**
		 * The meta object literal for the '<em><b>Safety Heel</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL__SAFETY_HEEL = eINSTANCE.getVessel_SafetyHeel();

		/**
		 * The meta object literal for the '<em><b>Warming Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL__WARMING_TIME = eINSTANCE.getVessel_WarmingTime();

		/**
		 * The meta object literal for the '<em><b>Purge Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL__PURGE_TIME = eINSTANCE.getVessel_PurgeTime();

		/**
		 * The meta object literal for the '<em><b>Cooling Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL__COOLING_VOLUME = eINSTANCE.getVessel_CoolingVolume();

		/**
		 * The meta object literal for the '<em><b>Cooling Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL__COOLING_TIME = eINSTANCE.getVessel_CoolingTime();

		/**
		 * The meta object literal for the '<em><b>Purge Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL__PURGE_VOLUME = eINSTANCE.getVessel_PurgeVolume();

		/**
		 * The meta object literal for the '<em><b>Route Parameters Override</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL__ROUTE_PARAMETERS_OVERRIDE = eINSTANCE.getVessel_RouteParametersOverride();

		/**
		 * The meta object literal for the '<em><b>Route Parameters</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL__ROUTE_PARAMETERS = eINSTANCE.getVessel_RouteParameters();

		/**
		 * The meta object literal for the '<em><b>Pilot Light Rate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL__PILOT_LIGHT_RATE = eINSTANCE.getVessel_PilotLightRate();

		/**
		 * The meta object literal for the '<em><b>Min Base Fuel Consumption</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL__MIN_BASE_FUEL_CONSUMPTION = eINSTANCE.getVessel_MinBaseFuelConsumption();

		/**
		 * The meta object literal for the '<em><b>Has Reliq Capability Override</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL__HAS_RELIQ_CAPABILITY_OVERRIDE = eINSTANCE.getVessel_HasReliqCapabilityOverride();

		/**
		 * The meta object literal for the '<em><b>Has Reliq Capability</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL__HAS_RELIQ_CAPABILITY = eINSTANCE.getVessel_HasReliqCapability();

		/**
		 * The meta object literal for the '<em><b>Notes</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL__NOTES = eINSTANCE.getVessel_Notes();

		/**
		 * The meta object literal for the '<em><b>Mmx Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL__MMX_ID = eINSTANCE.getVessel_MmxId();

		/**
		 * The meta object literal for the '<em><b>Reference Vessel</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL__REFERENCE_VESSEL = eINSTANCE.getVessel_ReferenceVessel();

		/**
		 * The meta object literal for the '<em><b>Mmx Reference</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL__MMX_REFERENCE = eINSTANCE.getVessel_MmxReference();

		/**
		 * The meta object literal for the '<em><b>Scnt</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL__SCNT = eINSTANCE.getVessel_Scnt();

		/**
		 * The meta object literal for the '<em><b>Inaccessible Ports Override</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL__INACCESSIBLE_PORTS_OVERRIDE = eINSTANCE.getVessel_InaccessiblePortsOverride();

		/**
		 * The meta object literal for the '<em><b>Inaccessible Routes</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL__INACCESSIBLE_ROUTES = eINSTANCE.getVessel_InaccessibleRoutes();

		/**
		 * The meta object literal for the '<em><b>Base Fuel</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL__BASE_FUEL = eINSTANCE.getVessel_BaseFuel();

		/**
		 * The meta object literal for the '<em><b>In Port Base Fuel</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL__IN_PORT_BASE_FUEL = eINSTANCE.getVessel_InPortBaseFuel();

		/**
		 * The meta object literal for the '<em><b>Pilot Light Base Fuel</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL__PILOT_LIGHT_BASE_FUEL = eINSTANCE.getVessel_PilotLightBaseFuel();

		/**
		 * The meta object literal for the '<em><b>Idle Base Fuel</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL__IDLE_BASE_FUEL = eINSTANCE.getVessel_IdleBaseFuel();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.fleet.impl.FleetModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.fleet.impl.FleetModelImpl
		 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getFleetModel()
		 * @generated
		 */
		EClass FLEET_MODEL = eINSTANCE.getFleetModel();

		/**
		 * The meta object literal for the '<em><b>Vessels</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FLEET_MODEL__VESSELS = eINSTANCE.getFleetModel_Vessels();

		/**
		 * The meta object literal for the '<em><b>Base Fuels</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FLEET_MODEL__BASE_FUELS = eINSTANCE.getFleetModel_BaseFuels();

		/**
		 * The meta object literal for the '<em><b>Vessel Groups</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FLEET_MODEL__VESSEL_GROUPS = eINSTANCE.getFleetModel_VesselGroups();

		/**
		 * The meta object literal for the '<em><b>Fleet Version Record</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FLEET_MODEL__FLEET_VERSION_RECORD = eINSTANCE.getFleetModel_FleetVersionRecord();

		/**
		 * The meta object literal for the '<em><b>Vessel Group Version Record</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FLEET_MODEL__VESSEL_GROUP_VERSION_RECORD = eINSTANCE.getFleetModel_VesselGroupVersionRecord();

		/**
		 * The meta object literal for the '<em><b>Bunker Fuels Version Record</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FLEET_MODEL__BUNKER_FUELS_VERSION_RECORD = eINSTANCE.getFleetModel_BunkerFuelsVersionRecord();

		/**
		 * The meta object literal for the '<em><b>MMX Vessel DB Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FLEET_MODEL__MMX_VESSEL_DB_VERSION = eINSTANCE.getFleetModel_MMXVesselDBVersion();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.fleet.impl.BaseFuelImpl <em>Base Fuel</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.fleet.impl.BaseFuelImpl
		 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getBaseFuel()
		 * @generated
		 */
		EClass BASE_FUEL = eINSTANCE.getBaseFuel();

		/**
		 * The meta object literal for the '<em><b>Equivalence Factor</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BASE_FUEL__EQUIVALENCE_FACTOR = eINSTANCE.getBaseFuel_EquivalenceFactor();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.fleet.impl.VesselStateAttributesImpl <em>Vessel State Attributes</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.fleet.impl.VesselStateAttributesImpl
		 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getVesselStateAttributes()
		 * @generated
		 */
		EClass VESSEL_STATE_ATTRIBUTES = eINSTANCE.getVesselStateAttributes();

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
		 * The meta object literal for the '<em><b>Idle Base Rate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_STATE_ATTRIBUTES__IDLE_BASE_RATE = eINSTANCE.getVesselStateAttributes_IdleBaseRate();

		/**
		 * The meta object literal for the '<em><b>In Port Base Rate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_STATE_ATTRIBUTES__IN_PORT_BASE_RATE = eINSTANCE.getVesselStateAttributes_InPortBaseRate();

		/**
		 * The meta object literal for the '<em><b>Fuel Consumption Override</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_OVERRIDE = eINSTANCE.getVesselStateAttributes_FuelConsumptionOverride();

		/**
		 * The meta object literal for the '<em><b>Fuel Consumption</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION = eINSTANCE.getVesselStateAttributes_FuelConsumption();

		/**
		 * The meta object literal for the '<em><b>Service Speed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_STATE_ATTRIBUTES__SERVICE_SPEED = eINSTANCE.getVesselStateAttributes_ServiceSpeed();

		/**
		 * The meta object literal for the '<em><b>In Port NBO Rate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_STATE_ATTRIBUTES__IN_PORT_NBO_RATE = eINSTANCE.getVesselStateAttributes_InPortNBORate();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.fleet.impl.FuelConsumptionImpl <em>Fuel Consumption</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.fleet.impl.FuelConsumptionImpl
		 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getFuelConsumption()
		 * @generated
		 */
		EClass FUEL_CONSUMPTION = eINSTANCE.getFuelConsumption();

		/**
		 * The meta object literal for the '<em><b>Speed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FUEL_CONSUMPTION__SPEED = eINSTANCE.getFuelConsumption_Speed();

		/**
		 * The meta object literal for the '<em><b>Consumption</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FUEL_CONSUMPTION__CONSUMPTION = eINSTANCE.getFuelConsumption_Consumption();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.fleet.impl.VesselClassRouteParametersImpl <em>Vessel Class Route Parameters</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.fleet.impl.VesselClassRouteParametersImpl
		 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getVesselClassRouteParameters()
		 * @generated
		 */
		EClass VESSEL_CLASS_ROUTE_PARAMETERS = eINSTANCE.getVesselClassRouteParameters();

		/**
		 * The meta object literal for the '<em><b>Route Option</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS_ROUTE_PARAMETERS__ROUTE_OPTION = eINSTANCE.getVesselClassRouteParameters_RouteOption();

		/**
		 * The meta object literal for the '<em><b>Extra Transit Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS_ROUTE_PARAMETERS__EXTRA_TRANSIT_TIME = eINSTANCE.getVesselClassRouteParameters_ExtraTransitTime();

		/**
		 * The meta object literal for the '<em><b>Laden Consumption Rate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS_ROUTE_PARAMETERS__LADEN_CONSUMPTION_RATE = eINSTANCE.getVesselClassRouteParameters_LadenConsumptionRate();

		/**
		 * The meta object literal for the '<em><b>Laden NBO Rate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS_ROUTE_PARAMETERS__LADEN_NBO_RATE = eINSTANCE.getVesselClassRouteParameters_LadenNBORate();

		/**
		 * The meta object literal for the '<em><b>Ballast Consumption Rate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS_ROUTE_PARAMETERS__BALLAST_CONSUMPTION_RATE = eINSTANCE.getVesselClassRouteParameters_BallastConsumptionRate();

		/**
		 * The meta object literal for the '<em><b>Ballast NBO Rate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS_ROUTE_PARAMETERS__BALLAST_NBO_RATE = eINSTANCE.getVesselClassRouteParameters_BallastNBORate();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.fleet.impl.VesselGroupImpl <em>Vessel Group</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.fleet.impl.VesselGroupImpl
		 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getVesselGroup()
		 * @generated
		 */
		EClass VESSEL_GROUP = eINSTANCE.getVesselGroup();

		/**
		 * The meta object literal for the '<em><b>Vessels</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_GROUP__VESSELS = eINSTANCE.getVesselGroup_Vessels();

	}

} //FleetPackage
