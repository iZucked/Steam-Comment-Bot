/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port;

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
	 * The feature id for the '<em><b>Short Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__SHORT_NAME = TypesPackage.APORT_SET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Location</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__LOCATION = TypesPackage.APORT_SET_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Capabilities</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__CAPABILITIES = TypesPackage.APORT_SET_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Load Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__LOAD_DURATION = TypesPackage.APORT_SET_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Discharge Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__DISCHARGE_DURATION = TypesPackage.APORT_SET_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Berths</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__BERTHS = TypesPackage.APORT_SET_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__CV_VALUE = TypesPackage.APORT_SET_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Default Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__DEFAULT_START_TIME = TypesPackage.APORT_SET_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Allow Cooldown</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__ALLOW_COOLDOWN = TypesPackage.APORT_SET_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Default Window Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__DEFAULT_WINDOW_SIZE = TypesPackage.APORT_SET_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Default Window Size Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__DEFAULT_WINDOW_SIZE_UNITS = TypesPackage.APORT_SET_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Min Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__MIN_CV_VALUE = TypesPackage.APORT_SET_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>Max Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__MAX_CV_VALUE = TypesPackage.APORT_SET_FEATURE_COUNT + 12;

	/**
	 * The feature id for the '<em><b>Min Vessel Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__MIN_VESSEL_SIZE = TypesPackage.APORT_SET_FEATURE_COUNT + 13;

	/**
	 * The feature id for the '<em><b>Max Vessel Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__MAX_VESSEL_SIZE = TypesPackage.APORT_SET_FEATURE_COUNT + 14;

	/**
	 * The number of structural features of the '<em>Port</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_FEATURE_COUNT = TypesPackage.APORT_SET_FEATURE_COUNT + 15;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = TypesPackage.APORT_SET___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = TypesPackage.APORT_SET___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT___ECONTAINER_OP = TypesPackage.APORT_SET___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Collect</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT___COLLECT__ELIST = TypesPackage.APORT_SET___COLLECT__ELIST;

	/**
	 * The operation id for the '<em>Get Zone Id</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT___GET_ZONE_ID = TypesPackage.APORT_SET_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Mmx ID</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT___MMX_ID = TypesPackage.APORT_SET_OPERATION_COUNT + 1;

	/**
	 * The number of operations of the '<em>Port</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_OPERATION_COUNT = TypesPackage.APORT_SET_OPERATION_COUNT + 2;

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
	 * The feature id for the '<em><b>North Entrance</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE__NORTH_ENTRANCE = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>South Entrance</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE__SOUTH_ENTRANCE = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Distance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE__DISTANCE = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Route</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_FEATURE_COUNT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.NAMED_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.NAMED_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE___ECONTAINER_OP = MMXCorePackage.NAMED_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>Route</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_OPERATION_COUNT = MMXCorePackage.NAMED_OBJECT_OPERATION_COUNT + 0;

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
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_GROUP___GET_UNSET_VALUE__ESTRUCTURALFEATURE = TypesPackage.APORT_SET___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_GROUP___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = TypesPackage.APORT_SET___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_GROUP___ECONTAINER_OP = TypesPackage.APORT_SET___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Collect</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_GROUP___COLLECT__ELIST = TypesPackage.APORT_SET___COLLECT__ELIST;

	/**
	 * The number of operations of the '<em>Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_GROUP_OPERATION_COUNT = TypesPackage.APORT_SET_OPERATION_COUNT + 0;

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
	 * The feature id for the '<em><b>Provider</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_LINE__PROVIDER = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Error Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_LINE__ERROR_CODE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Route Line</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_LINE_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_LINE___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.MMX_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_LINE___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.MMX_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_LINE___ECONTAINER_OP = MMXCorePackage.MMX_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>Route Line</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_LINE_OPERATION_COUNT = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 0;

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
	 * The feature id for the '<em><b>Port Country Groups</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_MODEL__PORT_COUNTRY_GROUPS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Contingency Matrix</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_MODEL__CONTINGENCY_MATRIX = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Port Version Record</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_MODEL__PORT_VERSION_RECORD = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Port Group Version Record</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_MODEL__PORT_GROUP_VERSION_RECORD = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Distance Version Record</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_MODEL__DISTANCE_VERSION_RECORD = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 9;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_MODEL___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_MODEL___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_MODEL___ECONTAINER_OP = MMXCorePackage.UUID_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_MODEL_OPERATION_COUNT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 0;

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
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAPABILITY_GROUP___GET_UNSET_VALUE__ESTRUCTURALFEATURE = TypesPackage.APORT_SET___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAPABILITY_GROUP___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = TypesPackage.APORT_SET___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAPABILITY_GROUP___ECONTAINER_OP = TypesPackage.APORT_SET___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Collect</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAPABILITY_GROUP___COLLECT__ELIST = TypesPackage.APORT_SET___COLLECT__ELIST;

	/**
	 * The number of operations of the '<em>Capability Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAPABILITY_GROUP_OPERATION_COUNT = TypesPackage.APORT_SET_OPERATION_COUNT + 0;

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
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION__EXTENSIONS = MMXCorePackage.NAMED_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION__NAME = MMXCorePackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION__OTHER_NAMES = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Mmx Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION__MMX_ID = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Time Zone</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION__TIME_ZONE = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Country</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION__COUNTRY = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Lat</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION__LAT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Lon</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION__LON = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Location</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION_FEATURE_COUNT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.NAMED_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.NAMED_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION___ECONTAINER_OP = MMXCorePackage.NAMED_OBJECT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Zone Id</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION___GET_ZONE_ID = MMXCorePackage.NAMED_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The number of operations of the '<em>Location</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION_OPERATION_COUNT = MMXCorePackage.NAMED_OBJECT_OPERATION_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.port.impl.PortCountryGroupImpl <em>Country Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.port.impl.PortCountryGroupImpl
	 * @see com.mmxlabs.models.lng.port.impl.PortPackageImpl#getPortCountryGroup()
	 * @generated
	 */
	int PORT_COUNTRY_GROUP = 7;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_COUNTRY_GROUP__EXTENSIONS = TypesPackage.APORT_SET__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_COUNTRY_GROUP__UUID = TypesPackage.APORT_SET__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_COUNTRY_GROUP__NAME = TypesPackage.APORT_SET__NAME;

	/**
	 * The number of structural features of the '<em>Country Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_COUNTRY_GROUP_FEATURE_COUNT = TypesPackage.APORT_SET_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_COUNTRY_GROUP___GET_UNSET_VALUE__ESTRUCTURALFEATURE = TypesPackage.APORT_SET___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_COUNTRY_GROUP___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = TypesPackage.APORT_SET___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_COUNTRY_GROUP___ECONTAINER_OP = TypesPackage.APORT_SET___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Collect</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_COUNTRY_GROUP___COLLECT__ELIST = TypesPackage.APORT_SET___COLLECT__ELIST;

	/**
	 * The number of operations of the '<em>Country Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_COUNTRY_GROUP_OPERATION_COUNT = TypesPackage.APORT_SET_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.port.impl.EntryPointImpl <em>Entry Point</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.port.impl.EntryPointImpl
	 * @see com.mmxlabs.models.lng.port.impl.PortPackageImpl#getEntryPoint()
	 * @generated
	 */
	int ENTRY_POINT = 8;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTRY_POINT__EXTENSIONS = MMXCorePackage.NAMED_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTRY_POINT__NAME = MMXCorePackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTRY_POINT__PORT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Entry Point</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTRY_POINT_FEATURE_COUNT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTRY_POINT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.NAMED_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTRY_POINT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.NAMED_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTRY_POINT___ECONTAINER_OP = MMXCorePackage.NAMED_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>Entry Point</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTRY_POINT_OPERATION_COUNT = MMXCorePackage.NAMED_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.port.impl.ContingencyMatrixImpl <em>Contingency Matrix</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.port.impl.ContingencyMatrixImpl
	 * @see com.mmxlabs.models.lng.port.impl.PortPackageImpl#getContingencyMatrix()
	 * @generated
	 */
	int CONTINGENCY_MATRIX = 9;

	/**
	 * The feature id for the '<em><b>Entries</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTINGENCY_MATRIX__ENTRIES = 0;

	/**
	 * The feature id for the '<em><b>Default Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTINGENCY_MATRIX__DEFAULT_DURATION = 1;

	/**
	 * The number of structural features of the '<em>Contingency Matrix</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTINGENCY_MATRIX_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Contingency Matrix</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTINGENCY_MATRIX_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.port.impl.ContingencyMatrixEntryImpl <em>Contingency Matrix Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.port.impl.ContingencyMatrixEntryImpl
	 * @see com.mmxlabs.models.lng.port.impl.PortPackageImpl#getContingencyMatrixEntry()
	 * @generated
	 */
	int CONTINGENCY_MATRIX_ENTRY = 10;

	/**
	 * The feature id for the '<em><b>From Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTINGENCY_MATRIX_ENTRY__FROM_PORT = 0;

	/**
	 * The feature id for the '<em><b>To Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTINGENCY_MATRIX_ENTRY__TO_PORT = 1;

	/**
	 * The feature id for the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTINGENCY_MATRIX_ENTRY__DURATION = 2;

	/**
	 * The number of structural features of the '<em>Contingency Matrix Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTINGENCY_MATRIX_ENTRY_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Contingency Matrix Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTINGENCY_MATRIX_ENTRY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.port.RouteOption <em>Route Option</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.port.RouteOption
	 * @see com.mmxlabs.models.lng.port.impl.PortPackageImpl#getRouteOption()
	 * @generated
	 */
	int ROUTE_OPTION = 11;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.port.CanalEntry <em>Canal Entry</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.port.CanalEntry
	 * @see com.mmxlabs.models.lng.port.impl.PortPackageImpl#getCanalEntry()
	 * @generated
	 */
	int CANAL_ENTRY = 12;

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.Port#getMinVesselSize <em>Min Vessel Size</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Vessel Size</em>'.
	 * @see com.mmxlabs.models.lng.port.Port#getMinVesselSize()
	 * @see #getPort()
	 * @generated
	 */
	EAttribute getPort_MinVesselSize();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.Port#getMaxVesselSize <em>Max Vessel Size</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Vessel Size</em>'.
	 * @see com.mmxlabs.models.lng.port.Port#getMaxVesselSize()
	 * @see #getPort()
	 * @generated
	 */
	EAttribute getPort_MaxVesselSize();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.port.Port#getZoneId() <em>Get Zone Id</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Zone Id</em>' operation.
	 * @see com.mmxlabs.models.lng.port.Port#getZoneId()
	 * @generated
	 */
	EOperation getPort__GetZoneId();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.port.Port#mmxID() <em>Mmx ID</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Mmx ID</em>' operation.
	 * @see com.mmxlabs.models.lng.port.Port#mmxID()
	 * @generated
	 */
	EOperation getPort__MmxID();

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
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.port.Route#getNorthEntrance <em>North Entrance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>North Entrance</em>'.
	 * @see com.mmxlabs.models.lng.port.Route#getNorthEntrance()
	 * @see #getRoute()
	 * @generated
	 */
	EReference getRoute_NorthEntrance();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.port.Route#getSouthEntrance <em>South Entrance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>South Entrance</em>'.
	 * @see com.mmxlabs.models.lng.port.Route#getSouthEntrance()
	 * @see #getRoute()
	 * @generated
	 */
	EReference getRoute_SouthEntrance();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.Route#getDistance <em>Distance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Distance</em>'.
	 * @see com.mmxlabs.models.lng.port.Route#getDistance()
	 * @see #getRoute()
	 * @generated
	 */
	EAttribute getRoute_Distance();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.RouteLine#getProvider <em>Provider</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Provider</em>'.
	 * @see com.mmxlabs.models.lng.port.RouteLine#getProvider()
	 * @see #getRouteLine()
	 * @generated
	 */
	EAttribute getRouteLine_Provider();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.RouteLine#getErrorCode <em>Error Code</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Error Code</em>'.
	 * @see com.mmxlabs.models.lng.port.RouteLine#getErrorCode()
	 * @see #getRouteLine()
	 * @generated
	 */
	EAttribute getRouteLine_ErrorCode();

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
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.port.PortModel#getPortCountryGroups <em>Port Country Groups</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Port Country Groups</em>'.
	 * @see com.mmxlabs.models.lng.port.PortModel#getPortCountryGroups()
	 * @see #getPortModel()
	 * @generated
	 */
	EReference getPortModel_PortCountryGroups();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.port.PortModel#getContingencyMatrix <em>Contingency Matrix</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Contingency Matrix</em>'.
	 * @see com.mmxlabs.models.lng.port.PortModel#getContingencyMatrix()
	 * @see #getPortModel()
	 * @generated
	 */
	EReference getPortModel_ContingencyMatrix();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.port.PortModel#getPortVersionRecord <em>Port Version Record</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Port Version Record</em>'.
	 * @see com.mmxlabs.models.lng.port.PortModel#getPortVersionRecord()
	 * @see #getPortModel()
	 * @generated
	 */
	EReference getPortModel_PortVersionRecord();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.port.PortModel#getPortGroupVersionRecord <em>Port Group Version Record</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Port Group Version Record</em>'.
	 * @see com.mmxlabs.models.lng.port.PortModel#getPortGroupVersionRecord()
	 * @see #getPortModel()
	 * @generated
	 */
	EReference getPortModel_PortGroupVersionRecord();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.port.PortModel#getDistanceVersionRecord <em>Distance Version Record</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Distance Version Record</em>'.
	 * @see com.mmxlabs.models.lng.port.PortModel#getDistanceVersionRecord()
	 * @see #getPortModel()
	 * @generated
	 */
	EReference getPortModel_DistanceVersionRecord();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.Location#getMmxId <em>Mmx Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Mmx Id</em>'.
	 * @see com.mmxlabs.models.lng.port.Location#getMmxId()
	 * @see #getLocation()
	 * @generated
	 */
	EAttribute getLocation_MmxId();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.Location#getTimeZone <em>Time Zone</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Time Zone</em>'.
	 * @see com.mmxlabs.models.lng.port.Location#getTimeZone()
	 * @see #getLocation()
	 * @generated
	 */
	EAttribute getLocation_TimeZone();

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
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.port.Location#getZoneId() <em>Get Zone Id</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Zone Id</em>' operation.
	 * @see com.mmxlabs.models.lng.port.Location#getZoneId()
	 * @generated
	 */
	EOperation getLocation__GetZoneId();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.port.PortCountryGroup <em>Country Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Country Group</em>'.
	 * @see com.mmxlabs.models.lng.port.PortCountryGroup
	 * @generated
	 */
	EClass getPortCountryGroup();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.port.EntryPoint <em>Entry Point</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Entry Point</em>'.
	 * @see com.mmxlabs.models.lng.port.EntryPoint
	 * @generated
	 */
	EClass getEntryPoint();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.port.EntryPoint#getPort <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Port</em>'.
	 * @see com.mmxlabs.models.lng.port.EntryPoint#getPort()
	 * @see #getEntryPoint()
	 * @generated
	 */
	EReference getEntryPoint_Port();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.port.ContingencyMatrix <em>Contingency Matrix</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Contingency Matrix</em>'.
	 * @see com.mmxlabs.models.lng.port.ContingencyMatrix
	 * @generated
	 */
	EClass getContingencyMatrix();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.port.ContingencyMatrix#getEntries <em>Entries</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Entries</em>'.
	 * @see com.mmxlabs.models.lng.port.ContingencyMatrix#getEntries()
	 * @see #getContingencyMatrix()
	 * @generated
	 */
	EReference getContingencyMatrix_Entries();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.ContingencyMatrix#getDefaultDuration <em>Default Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default Duration</em>'.
	 * @see com.mmxlabs.models.lng.port.ContingencyMatrix#getDefaultDuration()
	 * @see #getContingencyMatrix()
	 * @generated
	 */
	EAttribute getContingencyMatrix_DefaultDuration();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.port.ContingencyMatrixEntry <em>Contingency Matrix Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Contingency Matrix Entry</em>'.
	 * @see com.mmxlabs.models.lng.port.ContingencyMatrixEntry
	 * @generated
	 */
	EClass getContingencyMatrixEntry();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.port.ContingencyMatrixEntry#getFromPort <em>From Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>From Port</em>'.
	 * @see com.mmxlabs.models.lng.port.ContingencyMatrixEntry#getFromPort()
	 * @see #getContingencyMatrixEntry()
	 * @generated
	 */
	EReference getContingencyMatrixEntry_FromPort();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.port.ContingencyMatrixEntry#getToPort <em>To Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>To Port</em>'.
	 * @see com.mmxlabs.models.lng.port.ContingencyMatrixEntry#getToPort()
	 * @see #getContingencyMatrixEntry()
	 * @generated
	 */
	EReference getContingencyMatrixEntry_ToPort();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.port.ContingencyMatrixEntry#getDuration <em>Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Duration</em>'.
	 * @see com.mmxlabs.models.lng.port.ContingencyMatrixEntry#getDuration()
	 * @see #getContingencyMatrixEntry()
	 * @generated
	 */
	EAttribute getContingencyMatrixEntry_Duration();

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
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.port.CanalEntry <em>Canal Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Canal Entry</em>'.
	 * @see com.mmxlabs.models.lng.port.CanalEntry
	 * @generated
	 */
	EEnum getCanalEntry();

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
		 * The meta object literal for the '<em><b>Min Vessel Size</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT__MIN_VESSEL_SIZE = eINSTANCE.getPort_MinVesselSize();

		/**
		 * The meta object literal for the '<em><b>Max Vessel Size</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT__MAX_VESSEL_SIZE = eINSTANCE.getPort_MaxVesselSize();

		/**
		 * The meta object literal for the '<em><b>Get Zone Id</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation PORT___GET_ZONE_ID = eINSTANCE.getPort__GetZoneId();

		/**
		 * The meta object literal for the '<em><b>Mmx ID</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation PORT___MMX_ID = eINSTANCE.getPort__MmxID();

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
		 * The meta object literal for the '<em><b>North Entrance</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROUTE__NORTH_ENTRANCE = eINSTANCE.getRoute_NorthEntrance();

		/**
		 * The meta object literal for the '<em><b>South Entrance</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROUTE__SOUTH_ENTRANCE = eINSTANCE.getRoute_SouthEntrance();

		/**
		 * The meta object literal for the '<em><b>Distance</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUTE__DISTANCE = eINSTANCE.getRoute_Distance();

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
		 * The meta object literal for the '<em><b>Provider</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUTE_LINE__PROVIDER = eINSTANCE.getRouteLine_Provider();

		/**
		 * The meta object literal for the '<em><b>Error Code</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUTE_LINE__ERROR_CODE = eINSTANCE.getRouteLine_ErrorCode();

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
		 * The meta object literal for the '<em><b>Port Country Groups</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PORT_MODEL__PORT_COUNTRY_GROUPS = eINSTANCE.getPortModel_PortCountryGroups();

		/**
		 * The meta object literal for the '<em><b>Contingency Matrix</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PORT_MODEL__CONTINGENCY_MATRIX = eINSTANCE.getPortModel_ContingencyMatrix();

		/**
		 * The meta object literal for the '<em><b>Port Version Record</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PORT_MODEL__PORT_VERSION_RECORD = eINSTANCE.getPortModel_PortVersionRecord();

		/**
		 * The meta object literal for the '<em><b>Port Group Version Record</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PORT_MODEL__PORT_GROUP_VERSION_RECORD = eINSTANCE.getPortModel_PortGroupVersionRecord();

		/**
		 * The meta object literal for the '<em><b>Distance Version Record</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PORT_MODEL__DISTANCE_VERSION_RECORD = eINSTANCE.getPortModel_DistanceVersionRecord();

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
		 * The meta object literal for the '<em><b>Mmx Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOCATION__MMX_ID = eINSTANCE.getLocation_MmxId();

		/**
		 * The meta object literal for the '<em><b>Time Zone</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOCATION__TIME_ZONE = eINSTANCE.getLocation_TimeZone();

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
		 * The meta object literal for the '<em><b>Get Zone Id</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation LOCATION___GET_ZONE_ID = eINSTANCE.getLocation__GetZoneId();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.port.impl.PortCountryGroupImpl <em>Country Group</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.port.impl.PortCountryGroupImpl
		 * @see com.mmxlabs.models.lng.port.impl.PortPackageImpl#getPortCountryGroup()
		 * @generated
		 */
		EClass PORT_COUNTRY_GROUP = eINSTANCE.getPortCountryGroup();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.port.impl.EntryPointImpl <em>Entry Point</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.port.impl.EntryPointImpl
		 * @see com.mmxlabs.models.lng.port.impl.PortPackageImpl#getEntryPoint()
		 * @generated
		 */
		EClass ENTRY_POINT = eINSTANCE.getEntryPoint();

		/**
		 * The meta object literal for the '<em><b>Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENTRY_POINT__PORT = eINSTANCE.getEntryPoint_Port();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.port.impl.ContingencyMatrixImpl <em>Contingency Matrix</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.port.impl.ContingencyMatrixImpl
		 * @see com.mmxlabs.models.lng.port.impl.PortPackageImpl#getContingencyMatrix()
		 * @generated
		 */
		EClass CONTINGENCY_MATRIX = eINSTANCE.getContingencyMatrix();

		/**
		 * The meta object literal for the '<em><b>Entries</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTINGENCY_MATRIX__ENTRIES = eINSTANCE.getContingencyMatrix_Entries();

		/**
		 * The meta object literal for the '<em><b>Default Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTINGENCY_MATRIX__DEFAULT_DURATION = eINSTANCE.getContingencyMatrix_DefaultDuration();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.port.impl.ContingencyMatrixEntryImpl <em>Contingency Matrix Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.port.impl.ContingencyMatrixEntryImpl
		 * @see com.mmxlabs.models.lng.port.impl.PortPackageImpl#getContingencyMatrixEntry()
		 * @generated
		 */
		EClass CONTINGENCY_MATRIX_ENTRY = eINSTANCE.getContingencyMatrixEntry();

		/**
		 * The meta object literal for the '<em><b>From Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTINGENCY_MATRIX_ENTRY__FROM_PORT = eINSTANCE.getContingencyMatrixEntry_FromPort();

		/**
		 * The meta object literal for the '<em><b>To Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTINGENCY_MATRIX_ENTRY__TO_PORT = eINSTANCE.getContingencyMatrixEntry_ToPort();

		/**
		 * The meta object literal for the '<em><b>Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTINGENCY_MATRIX_ENTRY__DURATION = eINSTANCE.getContingencyMatrixEntry_Duration();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.port.RouteOption <em>Route Option</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.port.RouteOption
		 * @see com.mmxlabs.models.lng.port.impl.PortPackageImpl#getRouteOption()
		 * @generated
		 */
		EEnum ROUTE_OPTION = eINSTANCE.getRouteOption();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.port.CanalEntry <em>Canal Entry</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.port.CanalEntry
		 * @see com.mmxlabs.models.lng.port.impl.PortPackageImpl#getCanalEntry()
		 * @generated
		 */
		EEnum CANAL_ENTRY = eINSTANCE.getCanalEntry();

	}

} //PortPackage
