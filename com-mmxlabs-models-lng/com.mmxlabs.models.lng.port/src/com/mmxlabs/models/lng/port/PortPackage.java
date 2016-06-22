/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
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
 * @see com.mmxlabs.models.lng.port.PortFactory
 * @model kind="package"
 * @generated
 */
public interface PortPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "port";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.mmxlabs.com/models/lng/port/1/";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "lng.port";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	PortPackage eINSTANCE = com.mmxlabs.models.lng.port.impl.PortPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.port.impl.PortImpl <em>Port</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.port.impl.PortImpl
	 * @see com.mmxlabs.models.lng.port.impl.PortPackageImpl#getPort()
	 * @generated
	 */
	int PORT = 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__EXTENSIONS = TypesPackage.APORT_SET__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__UUID = TypesPackage.APORT_SET__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__NAME = TypesPackage.APORT_SET__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__OTHER_NAMES = TypesPackage.APORT_SET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Short Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__SHORT_NAME = TypesPackage.APORT_SET_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Capabilities</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__CAPABILITIES = TypesPackage.APORT_SET_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Time Zone</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__TIME_ZONE = TypesPackage.APORT_SET_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Load Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__LOAD_DURATION = TypesPackage.APORT_SET_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Discharge Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__DISCHARGE_DURATION = TypesPackage.APORT_SET_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Berths</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__BERTHS = TypesPackage.APORT_SET_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__CV_VALUE = TypesPackage.APORT_SET_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Default Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__DEFAULT_START_TIME = TypesPackage.APORT_SET_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Allow Cooldown</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__ALLOW_COOLDOWN = TypesPackage.APORT_SET_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Default Window Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__DEFAULT_WINDOW_SIZE = TypesPackage.APORT_SET_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Default Window Size Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__DEFAULT_WINDOW_SIZE_UNITS = TypesPackage.APORT_SET_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>Location</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__LOCATION = TypesPackage.APORT_SET_FEATURE_COUNT + 12;

	/**
	 * The feature id for the '<em><b>Atobviac Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__ATOBVIAC_CODE = TypesPackage.APORT_SET_FEATURE_COUNT + 13;

	/**
	 * The feature id for the '<em><b>Dataloy Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__DATALOY_CODE = TypesPackage.APORT_SET_FEATURE_COUNT + 14;

	/**
	 * The feature id for the '<em><b>Veson Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__VESON_CODE = TypesPackage.APORT_SET_FEATURE_COUNT + 15;

	/**
	 * The feature id for the '<em><b>External Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__EXTERNAL_CODE = TypesPackage.APORT_SET_FEATURE_COUNT + 16;

	/**
	 * The feature id for the '<em><b>UN Locode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__UN_LOCODE = TypesPackage.APORT_SET_FEATURE_COUNT + 17;

	/**
	 * The feature id for the '<em><b>Min Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__MIN_CV_VALUE = TypesPackage.APORT_SET_FEATURE_COUNT + 18;

	/**
	 * The feature id for the '<em><b>Max Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__MAX_CV_VALUE = TypesPackage.APORT_SET_FEATURE_COUNT + 19;

	/**
	 * The number of structural features of the '<em>Port</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_FEATURE_COUNT = TypesPackage.APORT_SET_FEATURE_COUNT + 20;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.port.impl.RouteImpl <em>Route</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.port.impl.RouteImpl
	 * @see com.mmxlabs.models.lng.port.impl.PortPackageImpl#getRoute()
	 * @generated
	 */
	int ROUTE = 1;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE__EXTENSIONS = MMXCorePackage.NAMED_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE__NAME = MMXCorePackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE__UUID = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Lines</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE__LINES = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Route Option</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE__ROUTE_OPTION = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Canal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE__CANAL = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Routing Options</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE__ROUTING_OPTIONS = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Route</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_FEATURE_COUNT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.port.impl.PortGroupImpl <em>Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.port.impl.PortGroupImpl
	 * @see com.mmxlabs.models.lng.port.impl.PortPackageImpl#getPortGroup()
	 * @generated
	 */
	int PORT_GROUP = 2;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_GROUP__EXTENSIONS = TypesPackage.APORT_SET__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_GROUP__UUID = TypesPackage.APORT_SET__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_GROUP__NAME = TypesPackage.APORT_SET__NAME;

	/**
	 * The feature id for the '<em><b>Contents</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_GROUP__CONTENTS = TypesPackage.APORT_SET_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_GROUP_FEATURE_COUNT = TypesPackage.APORT_SET_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.port.impl.RouteLineImpl <em>Route Line</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.port.impl.RouteLineImpl
	 * @see com.mmxlabs.models.lng.port.impl.PortPackageImpl#getRouteLine()
	 * @generated
	 */
	int ROUTE_LINE = 3;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_LINE__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>From</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_LINE__FROM = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>To</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_LINE__TO = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Distance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_LINE__DISTANCE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Vias</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_LINE__VIAS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Route Line</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_LINE_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 4;


	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.port.impl.PortModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.port.impl.PortModelImpl
	 * @see com.mmxlabs.models.lng.port.impl.PortPackageImpl#getPortModel()
	 * @generated
	 */
	int PORT_MODEL = 4;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_MODEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_MODEL__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Ports</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_MODEL__PORTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Port Groups</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_MODEL__PORT_GROUPS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Routes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_MODEL__ROUTES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Special Port Groups</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_MODEL__SPECIAL_PORT_GROUPS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.port.impl.CapabilityGroupImpl <em>Capability Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.port.impl.CapabilityGroupImpl
	 * @see com.mmxlabs.models.lng.port.impl.PortPackageImpl#getCapabilityGroup()
	 * @generated
	 */
	int CAPABILITY_GROUP = 5;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAPABILITY_GROUP__EXTENSIONS = TypesPackage.APORT_SET__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAPABILITY_GROUP__UUID = TypesPackage.APORT_SET__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAPABILITY_GROUP__NAME = TypesPackage.APORT_SET__NAME;

	/**
	 * The feature id for the '<em><b>Capability</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAPABILITY_GROUP__CAPABILITY = TypesPackage.APORT_SET_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Capability Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAPABILITY_GROUP_FEATURE_COUNT = TypesPackage.APORT_SET_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.port.impl.LocationImpl <em>Location</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.port.impl.LocationImpl
	 * @see com.mmxlabs.models.lng.port.impl.PortPackageImpl#getLocation()
	 * @generated
	 */
	int LOCATION = 6;

	/**
	 * The feature id for the '<em><b>Country</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION__COUNTRY = 0;

	/**
	 * The feature id for the '<em><b>Lat</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION__LAT = 1;

	/**
	 * The feature id for the '<em><b>Lon</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION__LON = 2;

	/**
	 * The number of structural features of the '<em>Location</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.port.RouteOption <em>Route Option</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.port.RouteOption
	 * @see com.mmxlabs.models.lng.port.impl.PortPackageImpl#getRouteOption()
	 * @generated
	 */
	int ROUTE_OPTION = 7;

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.port.Port <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Port</em>'.
	 * @see com.mmxlabs.models.lng.port.Port
	 * @generated
	 */
	EClass getPort();

	/**
	 * Returns the meta object for the attribute list '{@link com.mmxlabs.models.lng.port.Port#getCapabilities <em>Capabilities</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Capabilities</em>'.
	 * @see com.mmxlabs.models.lng.port.Port#getCapabilities()
	 * @see #getPort()
	 * @generated
	 */
	EAttribute getPort_Capabilities();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.Port#getTimeZone <em>Time Zone</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Time Zone</em>'.
	 * @see com.mmxlabs.models.lng.port.Port#getTimeZone()
	 * @see #getPort()
	 * @generated
	 */
	EAttribute getPort_TimeZone();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.Port#getLoadDuration <em>Load Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Load Duration</em>'.
	 * @see com.mmxlabs.models.lng.port.Port#getLoadDuration()
	 * @see #getPort()
	 * @generated
	 */
	EAttribute getPort_LoadDuration();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.Port#getDischargeDuration <em>Discharge Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Discharge Duration</em>'.
	 * @see com.mmxlabs.models.lng.port.Port#getDischargeDuration()
	 * @see #getPort()
	 * @generated
	 */
	EAttribute getPort_DischargeDuration();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.Port#getBerths <em>Berths</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Berths</em>'.
	 * @see com.mmxlabs.models.lng.port.Port#getBerths()
	 * @see #getPort()
	 * @generated
	 */
	EAttribute getPort_Berths();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.Port#getCvValue <em>Cv Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cv Value</em>'.
	 * @see com.mmxlabs.models.lng.port.Port#getCvValue()
	 * @see #getPort()
	 * @generated
	 */
	EAttribute getPort_CvValue();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.Port#getDefaultStartTime <em>Default Start Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default Start Time</em>'.
	 * @see com.mmxlabs.models.lng.port.Port#getDefaultStartTime()
	 * @see #getPort()
	 * @generated
	 */
	EAttribute getPort_DefaultStartTime();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.Port#isAllowCooldown <em>Allow Cooldown</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Allow Cooldown</em>'.
	 * @see com.mmxlabs.models.lng.port.Port#isAllowCooldown()
	 * @see #getPort()
	 * @generated
	 */
	EAttribute getPort_AllowCooldown();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.Port#getDefaultWindowSize <em>Default Window Size</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default Window Size</em>'.
	 * @see com.mmxlabs.models.lng.port.Port#getDefaultWindowSize()
	 * @see #getPort()
	 * @generated
	 */
	EAttribute getPort_DefaultWindowSize();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.Port#getDefaultWindowSizeUnits <em>Default Window Size Units</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default Window Size Units</em>'.
	 * @see com.mmxlabs.models.lng.port.Port#getDefaultWindowSizeUnits()
	 * @see #getPort()
	 * @generated
	 */
	EAttribute getPort_DefaultWindowSizeUnits();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.port.Port#getLocation <em>Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Location</em>'.
	 * @see com.mmxlabs.models.lng.port.Port#getLocation()
	 * @see #getPort()
	 * @generated
	 */
	EReference getPort_Location();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.Port#getAtobviacCode <em>Atobviac Code</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Atobviac Code</em>'.
	 * @see com.mmxlabs.models.lng.port.Port#getAtobviacCode()
	 * @see #getPort()
	 * @generated
	 */
	EAttribute getPort_AtobviacCode();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.Port#getDataloyCode <em>Dataloy Code</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Dataloy Code</em>'.
	 * @see com.mmxlabs.models.lng.port.Port#getDataloyCode()
	 * @see #getPort()
	 * @generated
	 */
	EAttribute getPort_DataloyCode();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.Port#getVesonCode <em>Veson Code</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Veson Code</em>'.
	 * @see com.mmxlabs.models.lng.port.Port#getVesonCode()
	 * @see #getPort()
	 * @generated
	 */
	EAttribute getPort_VesonCode();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.Port#getExternalCode <em>External Code</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>External Code</em>'.
	 * @see com.mmxlabs.models.lng.port.Port#getExternalCode()
	 * @see #getPort()
	 * @generated
	 */
	EAttribute getPort_ExternalCode();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.Port#getUNLocode <em>UN Locode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>UN Locode</em>'.
	 * @see com.mmxlabs.models.lng.port.Port#getUNLocode()
	 * @see #getPort()
	 * @generated
	 */
	EAttribute getPort_UNLocode();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.Port#getMinCvValue <em>Min Cv Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Cv Value</em>'.
	 * @see com.mmxlabs.models.lng.port.Port#getMinCvValue()
	 * @see #getPort()
	 * @generated
	 */
	EAttribute getPort_MinCvValue();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.Port#getMaxCvValue <em>Max Cv Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Cv Value</em>'.
	 * @see com.mmxlabs.models.lng.port.Port#getMaxCvValue()
	 * @see #getPort()
	 * @generated
	 */
	EAttribute getPort_MaxCvValue();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.Port#getShortName <em>Short Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Short Name</em>'.
	 * @see com.mmxlabs.models.lng.port.Port#getShortName()
	 * @see #getPort()
	 * @generated
	 */
	EAttribute getPort_ShortName();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.port.Route <em>Route</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Route</em>'.
	 * @see com.mmxlabs.models.lng.port.Route
	 * @generated
	 */
	EClass getRoute();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.port.Route#getLines <em>Lines</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Lines</em>'.
	 * @see com.mmxlabs.models.lng.port.Route#getLines()
	 * @see #getRoute()
	 * @generated
	 */
	EReference getRoute_Lines();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.Route#getRouteOption <em>Route Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Route Option</em>'.
	 * @see com.mmxlabs.models.lng.port.Route#getRouteOption()
	 * @see #getRoute()
	 * @generated
	 */
	EAttribute getRoute_RouteOption();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.Route#isCanal <em>Canal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Canal</em>'.
	 * @see com.mmxlabs.models.lng.port.Route#isCanal()
	 * @see #getRoute()
	 * @generated
	 */
	EAttribute getRoute_Canal();

	/**
	 * Returns the meta object for the attribute list '{@link com.mmxlabs.models.lng.port.Route#getRoutingOptions <em>Routing Options</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Routing Options</em>'.
	 * @see com.mmxlabs.models.lng.port.Route#getRoutingOptions()
	 * @see #getRoute()
	 * @generated
	 */
	EAttribute getRoute_RoutingOptions();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.port.PortGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Group</em>'.
	 * @see com.mmxlabs.models.lng.port.PortGroup
	 * @generated
	 */
	EClass getPortGroup();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.port.PortGroup#getContents <em>Contents</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Contents</em>'.
	 * @see com.mmxlabs.models.lng.port.PortGroup#getContents()
	 * @see #getPortGroup()
	 * @generated
	 */
	EReference getPortGroup_Contents();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.port.RouteLine <em>Route Line</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Route Line</em>'.
	 * @see com.mmxlabs.models.lng.port.RouteLine
	 * @generated
	 */
	EClass getRouteLine();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.port.RouteLine#getFrom <em>From</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>From</em>'.
	 * @see com.mmxlabs.models.lng.port.RouteLine#getFrom()
	 * @see #getRouteLine()
	 * @generated
	 */
	EReference getRouteLine_From();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.port.RouteLine#getTo <em>To</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>To</em>'.
	 * @see com.mmxlabs.models.lng.port.RouteLine#getTo()
	 * @see #getRouteLine()
	 * @generated
	 */
	EReference getRouteLine_To();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.RouteLine#getDistance <em>Distance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Distance</em>'.
	 * @see com.mmxlabs.models.lng.port.RouteLine#getDistance()
	 * @see #getRouteLine()
	 * @generated
	 */
	EAttribute getRouteLine_Distance();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.port.RouteLine#getVias <em>Vias</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Vias</em>'.
	 * @see com.mmxlabs.models.lng.port.RouteLine#getVias()
	 * @see #getRouteLine()
	 * @generated
	 */
	EReference getRouteLine_Vias();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.port.PortModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see com.mmxlabs.models.lng.port.PortModel
	 * @generated
	 */
	EClass getPortModel();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.port.PortModel#getPorts <em>Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Ports</em>'.
	 * @see com.mmxlabs.models.lng.port.PortModel#getPorts()
	 * @see #getPortModel()
	 * @generated
	 */
	EReference getPortModel_Ports();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.port.PortModel#getPortGroups <em>Port Groups</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Port Groups</em>'.
	 * @see com.mmxlabs.models.lng.port.PortModel#getPortGroups()
	 * @see #getPortModel()
	 * @generated
	 */
	EReference getPortModel_PortGroups();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.port.PortModel#getRoutes <em>Routes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Routes</em>'.
	 * @see com.mmxlabs.models.lng.port.PortModel#getRoutes()
	 * @see #getPortModel()
	 * @generated
	 */
	EReference getPortModel_Routes();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.port.PortModel#getSpecialPortGroups <em>Special Port Groups</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Special Port Groups</em>'.
	 * @see com.mmxlabs.models.lng.port.PortModel#getSpecialPortGroups()
	 * @see #getPortModel()
	 * @generated
	 */
	EReference getPortModel_SpecialPortGroups();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.port.CapabilityGroup <em>Capability Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Capability Group</em>'.
	 * @see com.mmxlabs.models.lng.port.CapabilityGroup
	 * @generated
	 */
	EClass getCapabilityGroup();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.CapabilityGroup#getCapability <em>Capability</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Capability</em>'.
	 * @see com.mmxlabs.models.lng.port.CapabilityGroup#getCapability()
	 * @see #getCapabilityGroup()
	 * @generated
	 */
	EAttribute getCapabilityGroup_Capability();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.port.Location <em>Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Location</em>'.
	 * @see com.mmxlabs.models.lng.port.Location
	 * @generated
	 */
	EClass getLocation();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.Location#getCountry <em>Country</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Country</em>'.
	 * @see com.mmxlabs.models.lng.port.Location#getCountry()
	 * @see #getLocation()
	 * @generated
	 */
	EAttribute getLocation_Country();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.Location#getLat <em>Lat</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lat</em>'.
	 * @see com.mmxlabs.models.lng.port.Location#getLat()
	 * @see #getLocation()
	 * @generated
	 */
	EAttribute getLocation_Lat();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.Location#getLon <em>Lon</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lon</em>'.
	 * @see com.mmxlabs.models.lng.port.Location#getLon()
	 * @see #getLocation()
	 * @generated
	 */
	EAttribute getLocation_Lon();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.port.RouteOption <em>Route Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Route Option</em>'.
	 * @see com.mmxlabs.models.lng.port.RouteOption
	 * @generated
	 */
	EEnum getRouteOption();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	PortFactory getPortFactory();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.port.impl.PortImpl <em>Port</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.port.impl.PortImpl
		 * @see com.mmxlabs.models.lng.port.impl.PortPackageImpl#getPort()
		 * @generated
		 */
		EClass PORT = eINSTANCE.getPort();

		/**
		 * The meta object literal for the '<em><b>Capabilities</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT__CAPABILITIES = eINSTANCE.getPort_Capabilities();

		/**
		 * The meta object literal for the '<em><b>Time Zone</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT__TIME_ZONE = eINSTANCE.getPort_TimeZone();

		/**
		 * The meta object literal for the '<em><b>Load Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT__LOAD_DURATION = eINSTANCE.getPort_LoadDuration();

		/**
		 * The meta object literal for the '<em><b>Discharge Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT__DISCHARGE_DURATION = eINSTANCE.getPort_DischargeDuration();

		/**
		 * The meta object literal for the '<em><b>Berths</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT__BERTHS = eINSTANCE.getPort_Berths();

		/**
		 * The meta object literal for the '<em><b>Cv Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT__CV_VALUE = eINSTANCE.getPort_CvValue();

		/**
		 * The meta object literal for the '<em><b>Default Start Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT__DEFAULT_START_TIME = eINSTANCE.getPort_DefaultStartTime();

		/**
		 * The meta object literal for the '<em><b>Allow Cooldown</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT__ALLOW_COOLDOWN = eINSTANCE.getPort_AllowCooldown();

		/**
		 * The meta object literal for the '<em><b>Default Window Size</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT__DEFAULT_WINDOW_SIZE = eINSTANCE.getPort_DefaultWindowSize();

		/**
		 * The meta object literal for the '<em><b>Default Window Size Units</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT__DEFAULT_WINDOW_SIZE_UNITS = eINSTANCE.getPort_DefaultWindowSizeUnits();

		/**
		 * The meta object literal for the '<em><b>Location</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PORT__LOCATION = eINSTANCE.getPort_Location();

		/**
		 * The meta object literal for the '<em><b>Atobviac Code</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT__ATOBVIAC_CODE = eINSTANCE.getPort_AtobviacCode();

		/**
		 * The meta object literal for the '<em><b>Dataloy Code</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT__DATALOY_CODE = eINSTANCE.getPort_DataloyCode();

		/**
		 * The meta object literal for the '<em><b>Veson Code</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT__VESON_CODE = eINSTANCE.getPort_VesonCode();

		/**
		 * The meta object literal for the '<em><b>External Code</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT__EXTERNAL_CODE = eINSTANCE.getPort_ExternalCode();

		/**
		 * The meta object literal for the '<em><b>UN Locode</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT__UN_LOCODE = eINSTANCE.getPort_UNLocode();

		/**
		 * The meta object literal for the '<em><b>Min Cv Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT__MIN_CV_VALUE = eINSTANCE.getPort_MinCvValue();

		/**
		 * The meta object literal for the '<em><b>Max Cv Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT__MAX_CV_VALUE = eINSTANCE.getPort_MaxCvValue();

		/**
		 * The meta object literal for the '<em><b>Short Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT__SHORT_NAME = eINSTANCE.getPort_ShortName();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.port.impl.RouteImpl <em>Route</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.port.impl.RouteImpl
		 * @see com.mmxlabs.models.lng.port.impl.PortPackageImpl#getRoute()
		 * @generated
		 */
		EClass ROUTE = eINSTANCE.getRoute();

		/**
		 * The meta object literal for the '<em><b>Lines</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROUTE__LINES = eINSTANCE.getRoute_Lines();

		/**
		 * The meta object literal for the '<em><b>Route Option</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUTE__ROUTE_OPTION = eINSTANCE.getRoute_RouteOption();

		/**
		 * The meta object literal for the '<em><b>Canal</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUTE__CANAL = eINSTANCE.getRoute_Canal();

		/**
		 * The meta object literal for the '<em><b>Routing Options</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUTE__ROUTING_OPTIONS = eINSTANCE.getRoute_RoutingOptions();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.port.impl.PortGroupImpl <em>Group</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.port.impl.PortGroupImpl
		 * @see com.mmxlabs.models.lng.port.impl.PortPackageImpl#getPortGroup()
		 * @generated
		 */
		EClass PORT_GROUP = eINSTANCE.getPortGroup();

		/**
		 * The meta object literal for the '<em><b>Contents</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PORT_GROUP__CONTENTS = eINSTANCE.getPortGroup_Contents();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.port.impl.RouteLineImpl <em>Route Line</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.port.impl.RouteLineImpl
		 * @see com.mmxlabs.models.lng.port.impl.PortPackageImpl#getRouteLine()
		 * @generated
		 */
		EClass ROUTE_LINE = eINSTANCE.getRouteLine();

		/**
		 * The meta object literal for the '<em><b>From</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROUTE_LINE__FROM = eINSTANCE.getRouteLine_From();

		/**
		 * The meta object literal for the '<em><b>To</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROUTE_LINE__TO = eINSTANCE.getRouteLine_To();

		/**
		 * The meta object literal for the '<em><b>Distance</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUTE_LINE__DISTANCE = eINSTANCE.getRouteLine_Distance();

		/**
		 * The meta object literal for the '<em><b>Vias</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROUTE_LINE__VIAS = eINSTANCE.getRouteLine_Vias();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.port.impl.PortModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.port.impl.PortModelImpl
		 * @see com.mmxlabs.models.lng.port.impl.PortPackageImpl#getPortModel()
		 * @generated
		 */
		EClass PORT_MODEL = eINSTANCE.getPortModel();

		/**
		 * The meta object literal for the '<em><b>Ports</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PORT_MODEL__PORTS = eINSTANCE.getPortModel_Ports();

		/**
		 * The meta object literal for the '<em><b>Port Groups</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PORT_MODEL__PORT_GROUPS = eINSTANCE.getPortModel_PortGroups();

		/**
		 * The meta object literal for the '<em><b>Routes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PORT_MODEL__ROUTES = eINSTANCE.getPortModel_Routes();

		/**
		 * The meta object literal for the '<em><b>Special Port Groups</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PORT_MODEL__SPECIAL_PORT_GROUPS = eINSTANCE.getPortModel_SpecialPortGroups();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.port.impl.CapabilityGroupImpl <em>Capability Group</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.port.impl.CapabilityGroupImpl
		 * @see com.mmxlabs.models.lng.port.impl.PortPackageImpl#getCapabilityGroup()
		 * @generated
		 */
		EClass CAPABILITY_GROUP = eINSTANCE.getCapabilityGroup();

		/**
		 * The meta object literal for the '<em><b>Capability</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CAPABILITY_GROUP__CAPABILITY = eINSTANCE.getCapabilityGroup_Capability();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.port.impl.LocationImpl <em>Location</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.port.impl.LocationImpl
		 * @see com.mmxlabs.models.lng.port.impl.PortPackageImpl#getLocation()
		 * @generated
		 */
		EClass LOCATION = eINSTANCE.getLocation();

		/**
		 * The meta object literal for the '<em><b>Country</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOCATION__COUNTRY = eINSTANCE.getLocation_Country();

		/**
		 * The meta object literal for the '<em><b>Lat</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOCATION__LAT = eINSTANCE.getLocation_Lat();

		/**
		 * The meta object literal for the '<em><b>Lon</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOCATION__LON = eINSTANCE.getLocation_Lon();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.port.RouteOption <em>Route Option</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.port.RouteOption
		 * @see com.mmxlabs.models.lng.port.impl.PortPackageImpl#getRouteOption()
		 * @generated
		 */
		EEnum ROUTE_OPTION = eINSTANCE.getRouteOption();

	}

} //PortPackage
