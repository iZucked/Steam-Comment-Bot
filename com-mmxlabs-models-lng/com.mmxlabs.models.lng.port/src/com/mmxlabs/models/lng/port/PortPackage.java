/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port;

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
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__EXTENSIONS = TypesPackage.APORT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__PROXIES = TypesPackage.APORT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__UUID = TypesPackage.APORT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__NAME = TypesPackage.APORT__NAME;

	/**
	 * The feature id for the '<em><b>Capabilities</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__CAPABILITIES = TypesPackage.APORT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Time Zone</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__TIME_ZONE = TypesPackage.APORT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Load Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__LOAD_DURATION = TypesPackage.APORT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Discharge Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__DISCHARGE_DURATION = TypesPackage.APORT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__CV_VALUE = TypesPackage.APORT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Default Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__DEFAULT_START_TIME = TypesPackage.APORT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Allow Cooldown</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__ALLOW_COOLDOWN = TypesPackage.APORT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Default Window Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__DEFAULT_WINDOW_SIZE = TypesPackage.APORT_FEATURE_COUNT + 7;

	/**
	 * The number of structural features of the '<em>Port</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_FEATURE_COUNT = TypesPackage.APORT_FEATURE_COUNT + 8;

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
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE__EXTENSIONS = TypesPackage.AROUTE__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE__PROXIES = TypesPackage.AROUTE__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE__UUID = TypesPackage.AROUTE__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE__NAME = TypesPackage.AROUTE__NAME;

	/**
	 * The feature id for the '<em><b>Lines</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE__LINES = TypesPackage.AROUTE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Canal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE__CANAL = TypesPackage.AROUTE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Route</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_FEATURE_COUNT = TypesPackage.AROUTE_FEATURE_COUNT + 2;

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
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_GROUP__EXTENSIONS = TypesPackage.APORT_SET__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_GROUP__PROXIES = TypesPackage.APORT_SET__PROXIES;

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
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_LINE__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_LINE__PROXIES = MMXCorePackage.MMX_OBJECT__PROXIES;

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
	 * The number of structural features of the '<em>Route Line</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_LINE_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;


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
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_MODEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_MODEL__PROXIES = MMXCorePackage.UUID_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_MODEL__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_MODEL__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Ports</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_MODEL__PORTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Port Groups</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_MODEL__PORT_GROUPS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Routes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_MODEL__ROUTES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

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
		 * The meta object literal for the '<em><b>Canal</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUTE__CANAL = eINSTANCE.getRoute_Canal();

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

	}

} //PortPackage
