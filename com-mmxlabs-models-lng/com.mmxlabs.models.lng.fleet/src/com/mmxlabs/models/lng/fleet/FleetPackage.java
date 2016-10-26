/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.fleet.impl.VesselClassImpl <em>Vessel Class</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.fleet.impl.VesselClassImpl
	 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getVesselClass()
	 * @generated
	 */
	int VESSEL_CLASS = 3;

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
	 * The feature id for the '<em><b>Vessel Classes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL__VESSEL_CLASSES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Base Fuels</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL__BASE_FUELS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Vessel Groups</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL__VESSEL_GROUPS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

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
	 * The feature id for the '<em><b>Vessel Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__VESSEL_CLASS = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Inaccessible Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__INACCESSIBLE_PORTS = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__CAPACITY = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Fill Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__FILL_CAPACITY = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Override Inaccessible Routes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__OVERRIDE_INACCESSIBLE_ROUTES = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Inaccessible Routes</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__INACCESSIBLE_ROUTES = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Vessel</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_FEATURE_COUNT = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__EXTENSIONS = TypesPackage.AVESSEL_SET__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__UUID = TypesPackage.AVESSEL_SET__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__NAME = TypesPackage.AVESSEL_SET__NAME;

	/**
	 * The feature id for the '<em><b>Inaccessible Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__INACCESSIBLE_PORTS = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Base Fuel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__BASE_FUEL = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__CAPACITY = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Fill Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__FILL_CAPACITY = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Laden Attributes</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__LADEN_ATTRIBUTES = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Ballast Attributes</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__BALLAST_ATTRIBUTES = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Min Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__MIN_SPEED = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Max Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__MAX_SPEED = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Min Heel</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__MIN_HEEL = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Warming Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__WARMING_TIME = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Cooling Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__COOLING_VOLUME = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Route Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__ROUTE_PARAMETERS = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>Pilot Light Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__PILOT_LIGHT_RATE = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 12;

	/**
	 * The feature id for the '<em><b>Min Base Fuel Consumption</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__MIN_BASE_FUEL_CONSUMPTION = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 13;

	/**
	 * The feature id for the '<em><b>Has Reliq Capability</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__HAS_RELIQ_CAPABILITY = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 14;

	/**
	 * The feature id for the '<em><b>Inaccessible Routes</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__INACCESSIBLE_ROUTES = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 15;

	/**
	 * The number of structural features of the '<em>Vessel Class</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS_FEATURE_COUNT = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 16;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.fleet.impl.HeelOptionsImpl <em>Heel Options</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.fleet.impl.HeelOptionsImpl
	 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getHeelOptions()
	 * @generated
	 */
	int HEEL_OPTIONS = 5;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.fleet.impl.VesselStateAttributesImpl <em>Vessel State Attributes</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.fleet.impl.VesselStateAttributesImpl
	 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getVesselStateAttributes()
	 * @generated
	 */
	int VESSEL_STATE_ATTRIBUTES = 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.fleet.impl.FuelConsumptionImpl <em>Fuel Consumption</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.fleet.impl.FuelConsumptionImpl
	 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getFuelConsumption()
	 * @generated
	 */
	int FUEL_CONSUMPTION = 7;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.fleet.impl.VesselClassRouteParametersImpl <em>Vessel Class Route Parameters</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.fleet.impl.VesselClassRouteParametersImpl
	 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getVesselClassRouteParameters()
	 * @generated
	 */
	int VESSEL_CLASS_ROUTE_PARAMETERS = 8;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.fleet.impl.VesselGroupImpl <em>Vessel Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.fleet.impl.VesselGroupImpl
	 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getVesselGroup()
	 * @generated
	 */
	int VESSEL_GROUP = 4;

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
	int HEEL_OPTIONS__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Volume Available</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HEEL_OPTIONS__VOLUME_AVAILABLE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HEEL_OPTIONS__CV_VALUE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Price Per MMBTU</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HEEL_OPTIONS__PRICE_PER_MMBTU = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Heel Options</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HEEL_OPTIONS_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

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
	 * The feature id for the '<em><b>Fuel Consumption</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Service Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES__SERVICE_SPEED = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>In Port NBO Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES__IN_PORT_NBO_RATE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Vessel State Attributes</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 7;

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
	 * The feature id for the '<em><b>Route</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS_ROUTE_PARAMETERS__ROUTE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

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
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.fleet.Vessel#getVesselClass <em>Vessel Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel Class</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getVesselClass()
	 * @see #getVessel()
	 * @generated
	 */
	EReference getVessel_VesselClass();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.Vessel#isOverrideInaccessibleRoutes <em>Override Inaccessible Routes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Override Inaccessible Routes</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#isOverrideInaccessibleRoutes()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_OverrideInaccessibleRoutes();

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.fleet.VesselClass <em>Vessel Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vessel Class</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselClass
	 * @generated
	 */
	EClass getVesselClass();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.fleet.VesselClass#getInaccessiblePorts <em>Inaccessible Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Inaccessible Ports</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselClass#getInaccessiblePorts()
	 * @see #getVesselClass()
	 * @generated
	 */
	EReference getVesselClass_InaccessiblePorts();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.fleet.VesselClass#getBaseFuel <em>Base Fuel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Base Fuel</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselClass#getBaseFuel()
	 * @see #getVesselClass()
	 * @generated
	 */
	EReference getVesselClass_BaseFuel();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselClass#getCapacity <em>Capacity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Capacity</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselClass#getCapacity()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_Capacity();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselClass#getFillCapacity <em>Fill Capacity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fill Capacity</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselClass#getFillCapacity()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_FillCapacity();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.fleet.VesselClass#getLadenAttributes <em>Laden Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Laden Attributes</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselClass#getLadenAttributes()
	 * @see #getVesselClass()
	 * @generated
	 */
	EReference getVesselClass_LadenAttributes();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.fleet.VesselClass#getBallastAttributes <em>Ballast Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Ballast Attributes</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselClass#getBallastAttributes()
	 * @see #getVesselClass()
	 * @generated
	 */
	EReference getVesselClass_BallastAttributes();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselClass#getMinSpeed <em>Min Speed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Speed</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselClass#getMinSpeed()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_MinSpeed();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselClass#getMaxSpeed <em>Max Speed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Speed</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselClass#getMaxSpeed()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_MaxSpeed();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselClass#getMinHeel <em>Min Heel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Heel</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselClass#getMinHeel()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_MinHeel();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselClass#getWarmingTime <em>Warming Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Warming Time</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselClass#getWarmingTime()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_WarmingTime();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselClass#getCoolingVolume <em>Cooling Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cooling Volume</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselClass#getCoolingVolume()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_CoolingVolume();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.fleet.VesselClass#getRouteParameters <em>Route Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Route Parameters</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselClass#getRouteParameters()
	 * @see #getVesselClass()
	 * @generated
	 */
	EReference getVesselClass_RouteParameters();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselClass#getPilotLightRate <em>Pilot Light Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Pilot Light Rate</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselClass#getPilotLightRate()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_PilotLightRate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselClass#getMinBaseFuelConsumption <em>Min Base Fuel Consumption</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Base Fuel Consumption</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselClass#getMinBaseFuelConsumption()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_MinBaseFuelConsumption();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselClass#isHasReliqCapability <em>Has Reliq Capability</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Has Reliq Capability</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselClass#isHasReliqCapability()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_HasReliqCapability();

	/**
	 * Returns the meta object for the attribute list '{@link com.mmxlabs.models.lng.fleet.VesselClass#getInaccessibleRoutes <em>Inaccessible Routes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Inaccessible Routes</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselClass#getInaccessibleRoutes()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_InaccessibleRoutes();

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
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.fleet.FleetModel#getVesselClasses <em>Vessel Classes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Vessel Classes</em>'.
	 * @see com.mmxlabs.models.lng.fleet.FleetModel#getVesselClasses()
	 * @see #getFleetModel()
	 * @generated
	 */
	EReference getFleetModel_VesselClasses();

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.fleet.HeelOptions <em>Heel Options</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Heel Options</em>'.
	 * @see com.mmxlabs.models.lng.fleet.HeelOptions
	 * @generated
	 */
	EClass getHeelOptions();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.HeelOptions#getVolumeAvailable <em>Volume Available</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volume Available</em>'.
	 * @see com.mmxlabs.models.lng.fleet.HeelOptions#getVolumeAvailable()
	 * @see #getHeelOptions()
	 * @generated
	 */
	EAttribute getHeelOptions_VolumeAvailable();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.HeelOptions#getCvValue <em>Cv Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cv Value</em>'.
	 * @see com.mmxlabs.models.lng.fleet.HeelOptions#getCvValue()
	 * @see #getHeelOptions()
	 * @generated
	 */
	EAttribute getHeelOptions_CvValue();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.HeelOptions#getPricePerMMBTU <em>Price Per MMBTU</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price Per MMBTU</em>'.
	 * @see com.mmxlabs.models.lng.fleet.HeelOptions#getPricePerMMBTU()
	 * @see #getHeelOptions()
	 * @generated
	 */
	EAttribute getHeelOptions_PricePerMMBTU();

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
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.fleet.VesselClassRouteParameters#getRoute <em>Route</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Route</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselClassRouteParameters#getRoute()
	 * @see #getVesselClassRouteParameters()
	 * @generated
	 */
	EReference getVesselClassRouteParameters_Route();

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
		 * The meta object literal for the '<em><b>Vessel Class</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL__VESSEL_CLASS = eINSTANCE.getVessel_VesselClass();

		/**
		 * The meta object literal for the '<em><b>Inaccessible Ports</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL__INACCESSIBLE_PORTS = eINSTANCE.getVessel_InaccessiblePorts();

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
		 * The meta object literal for the '<em><b>Override Inaccessible Routes</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL__OVERRIDE_INACCESSIBLE_ROUTES = eINSTANCE.getVessel_OverrideInaccessibleRoutes();

		/**
		 * The meta object literal for the '<em><b>Inaccessible Routes</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL__INACCESSIBLE_ROUTES = eINSTANCE.getVessel_InaccessibleRoutes();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.fleet.impl.VesselClassImpl <em>Vessel Class</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.fleet.impl.VesselClassImpl
		 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getVesselClass()
		 * @generated
		 */
		EClass VESSEL_CLASS = eINSTANCE.getVesselClass();

		/**
		 * The meta object literal for the '<em><b>Inaccessible Ports</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_CLASS__INACCESSIBLE_PORTS = eINSTANCE.getVesselClass_InaccessiblePorts();

		/**
		 * The meta object literal for the '<em><b>Base Fuel</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_CLASS__BASE_FUEL = eINSTANCE.getVesselClass_BaseFuel();

		/**
		 * The meta object literal for the '<em><b>Capacity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS__CAPACITY = eINSTANCE.getVesselClass_Capacity();

		/**
		 * The meta object literal for the '<em><b>Fill Capacity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS__FILL_CAPACITY = eINSTANCE.getVesselClass_FillCapacity();

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
		 * The meta object literal for the '<em><b>Min Heel</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS__MIN_HEEL = eINSTANCE.getVesselClass_MinHeel();

		/**
		 * The meta object literal for the '<em><b>Warming Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS__WARMING_TIME = eINSTANCE.getVesselClass_WarmingTime();

		/**
		 * The meta object literal for the '<em><b>Cooling Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS__COOLING_VOLUME = eINSTANCE.getVesselClass_CoolingVolume();

		/**
		 * The meta object literal for the '<em><b>Route Parameters</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_CLASS__ROUTE_PARAMETERS = eINSTANCE.getVesselClass_RouteParameters();

		/**
		 * The meta object literal for the '<em><b>Pilot Light Rate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS__PILOT_LIGHT_RATE = eINSTANCE.getVesselClass_PilotLightRate();

		/**
		 * The meta object literal for the '<em><b>Min Base Fuel Consumption</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS__MIN_BASE_FUEL_CONSUMPTION = eINSTANCE.getVesselClass_MinBaseFuelConsumption();

		/**
		 * The meta object literal for the '<em><b>Has Reliq Capability</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS__HAS_RELIQ_CAPABILITY = eINSTANCE.getVesselClass_HasReliqCapability();

		/**
		 * The meta object literal for the '<em><b>Inaccessible Routes</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS__INACCESSIBLE_ROUTES = eINSTANCE.getVesselClass_InaccessibleRoutes();

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
		 * The meta object literal for the '<em><b>Vessel Classes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FLEET_MODEL__VESSEL_CLASSES = eINSTANCE.getFleetModel_VesselClasses();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.fleet.impl.HeelOptionsImpl <em>Heel Options</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.fleet.impl.HeelOptionsImpl
		 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getHeelOptions()
		 * @generated
		 */
		EClass HEEL_OPTIONS = eINSTANCE.getHeelOptions();

		/**
		 * The meta object literal for the '<em><b>Volume Available</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HEEL_OPTIONS__VOLUME_AVAILABLE = eINSTANCE.getHeelOptions_VolumeAvailable();

		/**
		 * The meta object literal for the '<em><b>Cv Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HEEL_OPTIONS__CV_VALUE = eINSTANCE.getHeelOptions_CvValue();

		/**
		 * The meta object literal for the '<em><b>Price Per MMBTU</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HEEL_OPTIONS__PRICE_PER_MMBTU = eINSTANCE.getHeelOptions_PricePerMMBTU();

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
		 * The meta object literal for the '<em><b>Route</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_CLASS_ROUTE_PARAMETERS__ROUTE = eINSTANCE.getVesselClassRouteParameters_Route();

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
