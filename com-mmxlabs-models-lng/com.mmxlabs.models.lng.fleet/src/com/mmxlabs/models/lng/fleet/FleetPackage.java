/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet;

import com.mmxlabs.models.lng.types.TypesPackage;

import com.mmxlabs.models.mmxcore.MMXCorePackage;

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
	int VESSEL = 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__EXTENSIONS = TypesPackage.AVESSEL__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__PROXIES = TypesPackage.AVESSEL__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__UUID = TypesPackage.AVESSEL__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__NAME = TypesPackage.AVESSEL__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__OTHER_NAMES = TypesPackage.AVESSEL__OTHER_NAMES;

	/**
	 * The feature id for the '<em><b>Vessel Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__VESSEL_CLASS = TypesPackage.AVESSEL_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Inaccessible Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__INACCESSIBLE_PORTS = TypesPackage.AVESSEL_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Availability</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__AVAILABILITY = TypesPackage.AVESSEL_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Start Heel</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__START_HEEL = TypesPackage.AVESSEL_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Time Charter Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__TIME_CHARTER_RATE = TypesPackage.AVESSEL_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Vessel</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_FEATURE_COUNT = TypesPackage.AVESSEL_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.fleet.impl.VesselClassImpl <em>Vessel Class</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.fleet.impl.VesselClassImpl
	 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getVesselClass()
	 * @generated
	 */
	int VESSEL_CLASS = 1;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__EXTENSIONS = TypesPackage.AVESSEL_CLASS__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__PROXIES = TypesPackage.AVESSEL_CLASS__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__UUID = TypesPackage.AVESSEL_CLASS__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__NAME = TypesPackage.AVESSEL_CLASS__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__OTHER_NAMES = TypesPackage.AVESSEL_CLASS__OTHER_NAMES;

	/**
	 * The feature id for the '<em><b>Inaccessible Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__INACCESSIBLE_PORTS = TypesPackage.AVESSEL_CLASS_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Base Fuel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__BASE_FUEL = TypesPackage.AVESSEL_CLASS_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__CAPACITY = TypesPackage.AVESSEL_CLASS_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Fill Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__FILL_CAPACITY = TypesPackage.AVESSEL_CLASS_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Laden Attributes</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__LADEN_ATTRIBUTES = TypesPackage.AVESSEL_CLASS_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Ballast Attributes</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__BALLAST_ATTRIBUTES = TypesPackage.AVESSEL_CLASS_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Min Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__MIN_SPEED = TypesPackage.AVESSEL_CLASS_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Max Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__MAX_SPEED = TypesPackage.AVESSEL_CLASS_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Min Heel</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__MIN_HEEL = TypesPackage.AVESSEL_CLASS_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Warming Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__WARMING_TIME = TypesPackage.AVESSEL_CLASS_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Cooling Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__COOLING_TIME = TypesPackage.AVESSEL_CLASS_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Cooling Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__COOLING_VOLUME = TypesPackage.AVESSEL_CLASS_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>Route Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__ROUTE_PARAMETERS = TypesPackage.AVESSEL_CLASS_FEATURE_COUNT + 12;

	/**
	 * The feature id for the '<em><b>Pilot Light Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__PILOT_LIGHT_RATE = TypesPackage.AVESSEL_CLASS_FEATURE_COUNT + 13;

	/**
	 * The number of structural features of the '<em>Vessel Class</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS_FEATURE_COUNT = TypesPackage.AVESSEL_CLASS_FEATURE_COUNT + 14;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.fleet.impl.VesselEventImpl <em>Vessel Event</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.fleet.impl.VesselEventImpl
	 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getVesselEvent()
	 * @generated
	 */
	int VESSEL_EVENT = 2;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT__EXTENSIONS = TypesPackage.AVESSEL_EVENT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT__PROXIES = TypesPackage.AVESSEL_EVENT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT__UUID = TypesPackage.AVESSEL_EVENT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT__NAME = TypesPackage.AVESSEL_EVENT__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT__OTHER_NAMES = TypesPackage.AVESSEL_EVENT__OTHER_NAMES;

	/**
	 * The feature id for the '<em><b>Duration In Days</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT__DURATION_IN_DAYS = TypesPackage.AVESSEL_EVENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Allowed Vessels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT__ALLOWED_VESSELS = TypesPackage.AVESSEL_EVENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT__PORT = TypesPackage.AVESSEL_EVENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Start After</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT__START_AFTER = TypesPackage.AVESSEL_EVENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Start By</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT__START_BY = TypesPackage.AVESSEL_EVENT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Vessel Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_FEATURE_COUNT = TypesPackage.AVESSEL_EVENT_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.fleet.impl.FleetModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.fleet.impl.FleetModelImpl
	 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getFleetModel()
	 * @generated
	 */
	int FLEET_MODEL = 3;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL__PROXIES = MMXCorePackage.UUID_OBJECT__PROXIES;

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
	 * The feature id for the '<em><b>Vessel Events</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL__VESSEL_EVENTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Base Fuels</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL__BASE_FUELS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Vessel Groups</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL__VESSEL_GROUPS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Special Vessel Groups</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL__SPECIAL_VESSEL_GROUPS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.fleet.impl.BaseFuelImpl <em>Base Fuel</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.fleet.impl.BaseFuelImpl
	 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getBaseFuel()
	 * @generated
	 */
	int BASE_FUEL = 4;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL__EXTENSIONS = TypesPackage.ABASE_FUEL__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL__PROXIES = TypesPackage.ABASE_FUEL__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL__UUID = TypesPackage.ABASE_FUEL__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL__NAME = TypesPackage.ABASE_FUEL__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL__OTHER_NAMES = TypesPackage.ABASE_FUEL__OTHER_NAMES;

	/**
	 * The feature id for the '<em><b>Equivalence Factor</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL__EQUIVALENCE_FACTOR = TypesPackage.ABASE_FUEL_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Base Fuel</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_FUEL_FEATURE_COUNT = TypesPackage.ABASE_FUEL_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.fleet.impl.DryDockEventImpl <em>Dry Dock Event</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.fleet.impl.DryDockEventImpl
	 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getDryDockEvent()
	 * @generated
	 */
	int DRY_DOCK_EVENT = 5;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRY_DOCK_EVENT__EXTENSIONS = VESSEL_EVENT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRY_DOCK_EVENT__PROXIES = VESSEL_EVENT__PROXIES;

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
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DRY_DOCK_EVENT__OTHER_NAMES = VESSEL_EVENT__OTHER_NAMES;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.fleet.impl.CharterOutEventImpl <em>Charter Out Event</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.fleet.impl.CharterOutEventImpl
	 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getCharterOutEvent()
	 * @generated
	 */
	int CHARTER_OUT_EVENT = 6;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT__EXTENSIONS = VESSEL_EVENT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT__PROXIES = VESSEL_EVENT__PROXIES;

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
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_EVENT__OTHER_NAMES = VESSEL_EVENT__OTHER_NAMES;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.fleet.impl.HeelOptionsImpl <em>Heel Options</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.fleet.impl.HeelOptionsImpl
	 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getHeelOptions()
	 * @generated
	 */
	int HEEL_OPTIONS = 7;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HEEL_OPTIONS__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HEEL_OPTIONS__PROXIES = MMXCorePackage.MMX_OBJECT__PROXIES;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.fleet.impl.VesselStateAttributesImpl <em>Vessel State Attributes</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.fleet.impl.VesselStateAttributesImpl
	 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getVesselStateAttributes()
	 * @generated
	 */
	int VESSEL_STATE_ATTRIBUTES = 8;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES__PROXIES = MMXCorePackage.MMX_OBJECT__PROXIES;

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
	 * The number of structural features of the '<em>Vessel State Attributes</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.fleet.impl.VesselAvailabilityImpl <em>Vessel Availability</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.fleet.impl.VesselAvailabilityImpl
	 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getVesselAvailability()
	 * @generated
	 */
	int VESSEL_AVAILABILITY = 9;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__PROXIES = MMXCorePackage.MMX_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Start At</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__START_AT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Start After</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__START_AFTER = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Start By</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__START_BY = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>End At</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__END_AT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>End After</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__END_AFTER = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>End By</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY__END_BY = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Vessel Availability</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_AVAILABILITY_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.fleet.impl.FuelConsumptionImpl <em>Fuel Consumption</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.fleet.impl.FuelConsumptionImpl
	 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getFuelConsumption()
	 * @generated
	 */
	int FUEL_CONSUMPTION = 10;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.fleet.impl.MaintenanceEventImpl <em>Maintenance Event</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.fleet.impl.MaintenanceEventImpl
	 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getMaintenanceEvent()
	 * @generated
	 */
	int MAINTENANCE_EVENT = 11;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAINTENANCE_EVENT__EXTENSIONS = VESSEL_EVENT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAINTENANCE_EVENT__PROXIES = VESSEL_EVENT__PROXIES;

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
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAINTENANCE_EVENT__OTHER_NAMES = VESSEL_EVENT__OTHER_NAMES;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.fleet.impl.VesselClassRouteParametersImpl <em>Vessel Class Route Parameters</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.fleet.impl.VesselClassRouteParametersImpl
	 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getVesselClassRouteParameters()
	 * @generated
	 */
	int VESSEL_CLASS_ROUTE_PARAMETERS = 12;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS_ROUTE_PARAMETERS__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS_ROUTE_PARAMETERS__PROXIES = MMXCorePackage.MMX_OBJECT__PROXIES;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.fleet.impl.VesselGroupImpl <em>Vessel Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.fleet.impl.VesselGroupImpl
	 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getVesselGroup()
	 * @generated
	 */
	int VESSEL_GROUP = 13;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_GROUP__EXTENSIONS = TypesPackage.AVESSEL_SET__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_GROUP__PROXIES = TypesPackage.AVESSEL_SET__PROXIES;

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
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_GROUP__OTHER_NAMES = TypesPackage.AVESSEL_SET__OTHER_NAMES;

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
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_GROUP_FEATURE_COUNT = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 1;


	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.fleet.impl.VesselTypeGroupImpl <em>Vessel Type Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.fleet.impl.VesselTypeGroupImpl
	 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getVesselTypeGroup()
	 * @generated
	 */
	int VESSEL_TYPE_GROUP = 14;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_TYPE_GROUP__EXTENSIONS = TypesPackage.AVESSEL_SET__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_TYPE_GROUP__PROXIES = TypesPackage.AVESSEL_SET__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_TYPE_GROUP__UUID = TypesPackage.AVESSEL_SET__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_TYPE_GROUP__NAME = TypesPackage.AVESSEL_SET__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_TYPE_GROUP__OTHER_NAMES = TypesPackage.AVESSEL_SET__OTHER_NAMES;

	/**
	 * The feature id for the '<em><b>Vessel Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_TYPE_GROUP__VESSEL_TYPE = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Vessel Type Group</em>' class.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_TYPE_GROUP_FEATURE_COUNT = TypesPackage.AVESSEL_SET_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.fleet.VesselType <em>Vessel Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.fleet.VesselType
	 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getVesselType()
	 * @generated
	 */
	int VESSEL_TYPE = 15;


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
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.fleet.Vessel#getStartHeel <em>Start Heel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Start Heel</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getStartHeel()
	 * @see #getVessel()
	 * @generated
	 */
	EReference getVessel_StartHeel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.fleet.Vessel#getAvailability <em>Availability</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Availability</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getAvailability()
	 * @see #getVessel()
	 * @generated
	 */
	EReference getVessel_Availability();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.Vessel#getTimeCharterRate <em>Time Charter Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Time Charter Rate</em>'.
	 * @see com.mmxlabs.models.lng.fleet.Vessel#getTimeCharterRate()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_TimeCharterRate();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselClass#getCoolingTime <em>Cooling Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cooling Time</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselClass#getCoolingTime()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_CoolingTime();

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.fleet.VesselEvent <em>Vessel Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vessel Event</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselEvent
	 * @generated
	 */
	EClass getVesselEvent();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselEvent#getDurationInDays <em>Duration In Days</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Duration In Days</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselEvent#getDurationInDays()
	 * @see #getVesselEvent()
	 * @generated
	 */
	EAttribute getVesselEvent_DurationInDays();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.fleet.VesselEvent#getAllowedVessels <em>Allowed Vessels</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Allowed Vessels</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselEvent#getAllowedVessels()
	 * @see #getVesselEvent()
	 * @generated
	 */
	EReference getVesselEvent_AllowedVessels();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.fleet.VesselEvent#getPort <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Port</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselEvent#getPort()
	 * @see #getVesselEvent()
	 * @generated
	 */
	EReference getVesselEvent_Port();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselEvent#getStartAfter <em>Start After</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start After</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselEvent#getStartAfter()
	 * @see #getVesselEvent()
	 * @generated
	 */
	EAttribute getVesselEvent_StartAfter();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselEvent#getStartBy <em>Start By</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start By</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselEvent#getStartBy()
	 * @see #getVesselEvent()
	 * @generated
	 */
	EAttribute getVesselEvent_StartBy();

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
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.fleet.FleetModel#getVesselEvents <em>Vessel Events</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Vessel Events</em>'.
	 * @see com.mmxlabs.models.lng.fleet.FleetModel#getVesselEvents()
	 * @see #getFleetModel()
	 * @generated
	 */
	EReference getFleetModel_VesselEvents();

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
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.fleet.FleetModel#getSpecialVesselGroups <em>Special Vessel Groups</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Special Vessel Groups</em>'.
	 * @see com.mmxlabs.models.lng.fleet.FleetModel#getSpecialVesselGroups()
	 * @see #getFleetModel()
	 * @generated
	 */
	EReference getFleetModel_SpecialVesselGroups();

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.fleet.DryDockEvent <em>Dry Dock Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Dry Dock Event</em>'.
	 * @see com.mmxlabs.models.lng.fleet.DryDockEvent
	 * @generated
	 */
	EClass getDryDockEvent();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.fleet.CharterOutEvent <em>Charter Out Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Charter Out Event</em>'.
	 * @see com.mmxlabs.models.lng.fleet.CharterOutEvent
	 * @generated
	 */
	EClass getCharterOutEvent();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.fleet.CharterOutEvent#getRelocateTo <em>Relocate To</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Relocate To</em>'.
	 * @see com.mmxlabs.models.lng.fleet.CharterOutEvent#getRelocateTo()
	 * @see #getCharterOutEvent()
	 * @generated
	 */
	EReference getCharterOutEvent_RelocateTo();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.fleet.CharterOutEvent#getHeelOptions <em>Heel Options</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Heel Options</em>'.
	 * @see com.mmxlabs.models.lng.fleet.CharterOutEvent#getHeelOptions()
	 * @see #getCharterOutEvent()
	 * @generated
	 */
	EReference getCharterOutEvent_HeelOptions();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.CharterOutEvent#getRepositioningFee <em>Repositioning Fee</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Repositioning Fee</em>'.
	 * @see com.mmxlabs.models.lng.fleet.CharterOutEvent#getRepositioningFee()
	 * @see #getCharterOutEvent()
	 * @generated
	 */
	EAttribute getCharterOutEvent_RepositioningFee();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.CharterOutEvent#getHireRate <em>Hire Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Hire Rate</em>'.
	 * @see com.mmxlabs.models.lng.fleet.CharterOutEvent#getHireRate()
	 * @see #getCharterOutEvent()
	 * @generated
	 */
	EAttribute getCharterOutEvent_HireRate();

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.fleet.VesselAvailability <em>Vessel Availability</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vessel Availability</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselAvailability
	 * @generated
	 */
	EClass getVesselAvailability();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.fleet.VesselAvailability#getStartAt <em>Start At</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Start At</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselAvailability#getStartAt()
	 * @see #getVesselAvailability()
	 * @generated
	 */
	EReference getVesselAvailability_StartAt();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselAvailability#getStartAfter <em>Start After</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start After</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselAvailability#getStartAfter()
	 * @see #getVesselAvailability()
	 * @generated
	 */
	EAttribute getVesselAvailability_StartAfter();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselAvailability#getStartBy <em>Start By</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start By</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselAvailability#getStartBy()
	 * @see #getVesselAvailability()
	 * @generated
	 */
	EAttribute getVesselAvailability_StartBy();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.fleet.VesselAvailability#getEndAt <em>End At</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>End At</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselAvailability#getEndAt()
	 * @see #getVesselAvailability()
	 * @generated
	 */
	EReference getVesselAvailability_EndAt();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselAvailability#getEndAfter <em>End After</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End After</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselAvailability#getEndAfter()
	 * @see #getVesselAvailability()
	 * @generated
	 */
	EAttribute getVesselAvailability_EndAfter();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselAvailability#getEndBy <em>End By</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End By</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselAvailability#getEndBy()
	 * @see #getVesselAvailability()
	 * @generated
	 */
	EAttribute getVesselAvailability_EndBy();

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.fleet.MaintenanceEvent <em>Maintenance Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Maintenance Event</em>'.
	 * @see com.mmxlabs.models.lng.fleet.MaintenanceEvent
	 * @generated
	 */
	EClass getMaintenanceEvent();

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.fleet.VesselTypeGroup <em>Vessel Type Group</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vessel Type Group</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselTypeGroup
	 * @generated
	 */
	EClass getVesselTypeGroup();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.fleet.VesselTypeGroup#getVesselType <em>Vessel Type</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Vessel Type</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselTypeGroup#getVesselType()
	 * @see #getVesselTypeGroup()
	 * @generated
	 */
	EAttribute getVesselTypeGroup_VesselType();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.fleet.VesselType <em>Vessel Type</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Vessel Type</em>'.
	 * @see com.mmxlabs.models.lng.fleet.VesselType
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
		 * The meta object literal for the '<em><b>Start Heel</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL__START_HEEL = eINSTANCE.getVessel_StartHeel();

		/**
		 * The meta object literal for the '<em><b>Availability</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL__AVAILABILITY = eINSTANCE.getVessel_Availability();

		/**
		 * The meta object literal for the '<em><b>Time Charter Rate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL__TIME_CHARTER_RATE = eINSTANCE.getVessel_TimeCharterRate();

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
		 * The meta object literal for the '<em><b>Cooling Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS__COOLING_TIME = eINSTANCE.getVesselClass_CoolingTime();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.fleet.impl.VesselEventImpl <em>Vessel Event</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.fleet.impl.VesselEventImpl
		 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getVesselEvent()
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
		 * The meta object literal for the '<em><b>Vessel Events</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FLEET_MODEL__VESSEL_EVENTS = eINSTANCE.getFleetModel_VesselEvents();

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
		 * The meta object literal for the '<em><b>Special Vessel Groups</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FLEET_MODEL__SPECIAL_VESSEL_GROUPS = eINSTANCE.getFleetModel_SpecialVesselGroups();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.fleet.impl.DryDockEventImpl <em>Dry Dock Event</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.fleet.impl.DryDockEventImpl
		 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getDryDockEvent()
		 * @generated
		 */
		EClass DRY_DOCK_EVENT = eINSTANCE.getDryDockEvent();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.fleet.impl.CharterOutEventImpl <em>Charter Out Event</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.fleet.impl.CharterOutEventImpl
		 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getCharterOutEvent()
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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.fleet.impl.VesselAvailabilityImpl <em>Vessel Availability</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.fleet.impl.VesselAvailabilityImpl
		 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getVesselAvailability()
		 * @generated
		 */
		EClass VESSEL_AVAILABILITY = eINSTANCE.getVesselAvailability();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.fleet.impl.MaintenanceEventImpl <em>Maintenance Event</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.fleet.impl.MaintenanceEventImpl
		 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getMaintenanceEvent()
		 * @generated
		 */
		EClass MAINTENANCE_EVENT = eINSTANCE.getMaintenanceEvent();

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

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.fleet.impl.VesselTypeGroupImpl <em>Vessel Type Group</em>}' class.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.fleet.impl.VesselTypeGroupImpl
		 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getVesselTypeGroup()
		 * @generated
		 */
		EClass VESSEL_TYPE_GROUP = eINSTANCE.getVesselTypeGroup();

		/**
		 * The meta object literal for the '<em><b>Vessel Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_TYPE_GROUP__VESSEL_TYPE = eINSTANCE.getVesselTypeGroup_VesselType();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.fleet.VesselType <em>Vessel Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.fleet.VesselType
		 * @see com.mmxlabs.models.lng.fleet.impl.FleetPackageImpl#getVesselType()
		 * @generated
		 */
		EEnum VESSEL_TYPE = eINSTANCE.getVesselType();

	}

} //FleetPackage
