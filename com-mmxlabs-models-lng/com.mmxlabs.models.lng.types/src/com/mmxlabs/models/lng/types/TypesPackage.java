/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.types;

import com.mmxlabs.models.mmxcore.MMXCorePackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
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
 * @noimplement This interface is not intended to be implemented by clients.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.types.TypesFactory
 * @model kind="package"
 * @generated
 */
public interface TypesPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "types";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.mmxlabs.com/models/lng/types/1/";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "lng.types";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	TypesPackage eINSTANCE = com.mmxlabs.models.lng.types.impl.TypesPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.APortSetImpl <em>APort Set</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.APortSetImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getAPortSet()
	 * @generated
	 */
	int APORT_SET = 1;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT_SET__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT_SET__PROXIES = MMXCorePackage.UUID_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT_SET__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT_SET__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT_SET__OTHER_NAMES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>APort Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT_SET_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT_SET___MAKE_PROXIES = MMXCorePackage.UUID_OBJECT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT_SET___RESOLVE_PROXIES__MAP = MMXCorePackage.UUID_OBJECT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT_SET___RESTORE_PROXIES = MMXCorePackage.UUID_OBJECT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT_SET___COLLECT_UUID_OBJECTS__MAP = MMXCorePackage.UUID_OBJECT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT_SET___COLLECT_UUID_OBJECTS = MMXCorePackage.UUID_OBJECT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT_SET___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT_SET___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT_SET___ECONTAINER_OP = MMXCorePackage.UUID_OBJECT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Collect</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT_SET___COLLECT__ELIST = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The number of operations of the '<em>APort Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT_SET_OPERATION_COUNT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.APortImpl <em>APort</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.APortImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getAPort()
	 * @generated
	 */
	int APORT = 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT__EXTENSIONS = APORT_SET__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT__PROXIES = APORT_SET__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT__UUID = APORT_SET__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT__NAME = APORT_SET__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT__OTHER_NAMES = APORT_SET__OTHER_NAMES;

	/**
	 * The number of structural features of the '<em>APort</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT_FEATURE_COUNT = APORT_SET_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT___MAKE_PROXIES = APORT_SET___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT___RESOLVE_PROXIES__MAP = APORT_SET___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT___RESTORE_PROXIES = APORT_SET___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT___COLLECT_UUID_OBJECTS__MAP = APORT_SET___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT___COLLECT_UUID_OBJECTS = APORT_SET___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = APORT_SET___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = APORT_SET___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT___ECONTAINER_OP = APORT_SET___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Collect</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT___COLLECT__ELIST = APORT_SET_OPERATION_COUNT + 0;

	/**
	 * The number of operations of the '<em>APort</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT_OPERATION_COUNT = APORT_SET_OPERATION_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.ARouteImpl <em>ARoute</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.ARouteImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getARoute()
	 * @generated
	 */
	int AROUTE = 2;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AROUTE__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AROUTE__PROXIES = MMXCorePackage.UUID_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AROUTE__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AROUTE__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AROUTE__OTHER_NAMES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>ARoute</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AROUTE_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AROUTE___MAKE_PROXIES = MMXCorePackage.UUID_OBJECT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AROUTE___RESOLVE_PROXIES__MAP = MMXCorePackage.UUID_OBJECT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AROUTE___RESTORE_PROXIES = MMXCorePackage.UUID_OBJECT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AROUTE___COLLECT_UUID_OBJECTS__MAP = MMXCorePackage.UUID_OBJECT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AROUTE___COLLECT_UUID_OBJECTS = MMXCorePackage.UUID_OBJECT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AROUTE___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AROUTE___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AROUTE___ECONTAINER_OP = MMXCorePackage.UUID_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>ARoute</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AROUTE_OPERATION_COUNT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.AVesselSetImpl <em>AVessel Set</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.AVesselSetImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getAVesselSet()
	 * @generated
	 */
	int AVESSEL_SET = 12;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_SET__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_SET__PROXIES = MMXCorePackage.UUID_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_SET__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_SET__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_SET__OTHER_NAMES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>AVessel Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_SET_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_SET___MAKE_PROXIES = MMXCorePackage.UUID_OBJECT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_SET___RESOLVE_PROXIES__MAP = MMXCorePackage.UUID_OBJECT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_SET___RESTORE_PROXIES = MMXCorePackage.UUID_OBJECT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_SET___COLLECT_UUID_OBJECTS__MAP = MMXCorePackage.UUID_OBJECT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_SET___COLLECT_UUID_OBJECTS = MMXCorePackage.UUID_OBJECT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_SET___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_SET___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_SET___ECONTAINER_OP = MMXCorePackage.UUID_OBJECT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Collect</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_SET___COLLECT__ELIST = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The number of operations of the '<em>AVessel Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_SET_OPERATION_COUNT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.AVesselImpl <em>AVessel</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.AVesselImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getAVessel()
	 * @generated
	 */
	int AVESSEL = 3;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL__EXTENSIONS = AVESSEL_SET__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL__PROXIES = AVESSEL_SET__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL__UUID = AVESSEL_SET__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL__NAME = AVESSEL_SET__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL__OTHER_NAMES = AVESSEL_SET__OTHER_NAMES;

	/**
	 * The number of structural features of the '<em>AVessel</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_FEATURE_COUNT = AVESSEL_SET_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL___MAKE_PROXIES = AVESSEL_SET___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL___RESOLVE_PROXIES__MAP = AVESSEL_SET___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL___RESTORE_PROXIES = AVESSEL_SET___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL___COLLECT_UUID_OBJECTS__MAP = AVESSEL_SET___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL___COLLECT_UUID_OBJECTS = AVESSEL_SET___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL___GET_UNSET_VALUE__ESTRUCTURALFEATURE = AVESSEL_SET___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = AVESSEL_SET___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL___ECONTAINER_OP = AVESSEL_SET___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Collect</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL___COLLECT__ELIST = AVESSEL_SET_OPERATION_COUNT + 0;

	/**
	 * The number of operations of the '<em>AVessel</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_OPERATION_COUNT = AVESSEL_SET_OPERATION_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.AFleetVesselImpl <em>AFleet Vessel</em>}' class.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.AFleetVesselImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getAFleetVessel()
	 * @generated
	 */
	int AFLEET_VESSEL = 4;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AFLEET_VESSEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AFLEET_VESSEL__PROXIES = MMXCorePackage.UUID_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AFLEET_VESSEL__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The number of structural features of the '<em>AFleet Vessel</em>' class.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AFLEET_VESSEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AFLEET_VESSEL___MAKE_PROXIES = MMXCorePackage.UUID_OBJECT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AFLEET_VESSEL___RESOLVE_PROXIES__MAP = MMXCorePackage.UUID_OBJECT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AFLEET_VESSEL___RESTORE_PROXIES = MMXCorePackage.UUID_OBJECT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AFLEET_VESSEL___COLLECT_UUID_OBJECTS__MAP = MMXCorePackage.UUID_OBJECT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AFLEET_VESSEL___COLLECT_UUID_OBJECTS = MMXCorePackage.UUID_OBJECT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AFLEET_VESSEL___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AFLEET_VESSEL___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AFLEET_VESSEL___ECONTAINER_OP = MMXCorePackage.UUID_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>AFleet Vessel</em>' class.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AFLEET_VESSEL_OPERATION_COUNT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.AVesselClassImpl <em>AVessel Class</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.AVesselClassImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getAVesselClass()
	 * @generated
	 */
	int AVESSEL_CLASS = 5;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_CLASS__EXTENSIONS = AVESSEL_SET__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_CLASS__PROXIES = AVESSEL_SET__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_CLASS__UUID = AVESSEL_SET__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_CLASS__NAME = AVESSEL_SET__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_CLASS__OTHER_NAMES = AVESSEL_SET__OTHER_NAMES;

	/**
	 * The number of structural features of the '<em>AVessel Class</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_CLASS_FEATURE_COUNT = AVESSEL_SET_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_CLASS___MAKE_PROXIES = AVESSEL_SET___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_CLASS___RESOLVE_PROXIES__MAP = AVESSEL_SET___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_CLASS___RESTORE_PROXIES = AVESSEL_SET___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_CLASS___COLLECT_UUID_OBJECTS__MAP = AVESSEL_SET___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_CLASS___COLLECT_UUID_OBJECTS = AVESSEL_SET___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_CLASS___GET_UNSET_VALUE__ESTRUCTURALFEATURE = AVESSEL_SET___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_CLASS___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = AVESSEL_SET___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_CLASS___ECONTAINER_OP = AVESSEL_SET___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Collect</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_CLASS___COLLECT__ELIST = AVESSEL_SET___COLLECT__ELIST;

	/**
	 * The number of operations of the '<em>AVessel Class</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_CLASS_OPERATION_COUNT = AVESSEL_SET_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.AVesselEventImpl <em>AVessel Event</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.AVesselEventImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getAVesselEvent()
	 * @generated
	 */
	int AVESSEL_EVENT = 6;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_EVENT__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_EVENT__PROXIES = MMXCorePackage.UUID_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_EVENT__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_EVENT__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_EVENT__OTHER_NAMES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>AVessel Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_EVENT_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_EVENT___MAKE_PROXIES = MMXCorePackage.UUID_OBJECT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_EVENT___RESOLVE_PROXIES__MAP = MMXCorePackage.UUID_OBJECT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_EVENT___RESTORE_PROXIES = MMXCorePackage.UUID_OBJECT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_EVENT___COLLECT_UUID_OBJECTS__MAP = MMXCorePackage.UUID_OBJECT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_EVENT___COLLECT_UUID_OBJECTS = MMXCorePackage.UUID_OBJECT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_EVENT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_EVENT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_EVENT___ECONTAINER_OP = MMXCorePackage.UUID_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>AVessel Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_EVENT_OPERATION_COUNT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.AContractImpl <em>AContract</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.AContractImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getAContract()
	 * @generated
	 */
	int ACONTRACT = 7;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACONTRACT__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACONTRACT__PROXIES = MMXCorePackage.UUID_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACONTRACT__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACONTRACT__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACONTRACT__OTHER_NAMES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>AContract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACONTRACT_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACONTRACT___MAKE_PROXIES = MMXCorePackage.UUID_OBJECT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACONTRACT___RESOLVE_PROXIES__MAP = MMXCorePackage.UUID_OBJECT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACONTRACT___RESTORE_PROXIES = MMXCorePackage.UUID_OBJECT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACONTRACT___COLLECT_UUID_OBJECTS__MAP = MMXCorePackage.UUID_OBJECT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACONTRACT___COLLECT_UUID_OBJECTS = MMXCorePackage.UUID_OBJECT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACONTRACT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACONTRACT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACONTRACT___ECONTAINER_OP = MMXCorePackage.UUID_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>AContract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACONTRACT_OPERATION_COUNT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.ALegalEntityImpl <em>ALegal Entity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.ALegalEntityImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getALegalEntity()
	 * @generated
	 */
	int ALEGAL_ENTITY = 8;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALEGAL_ENTITY__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALEGAL_ENTITY__PROXIES = MMXCorePackage.UUID_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALEGAL_ENTITY__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALEGAL_ENTITY__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALEGAL_ENTITY__OTHER_NAMES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>ALegal Entity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALEGAL_ENTITY_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALEGAL_ENTITY___MAKE_PROXIES = MMXCorePackage.UUID_OBJECT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALEGAL_ENTITY___RESOLVE_PROXIES__MAP = MMXCorePackage.UUID_OBJECT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALEGAL_ENTITY___RESTORE_PROXIES = MMXCorePackage.UUID_OBJECT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALEGAL_ENTITY___COLLECT_UUID_OBJECTS__MAP = MMXCorePackage.UUID_OBJECT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALEGAL_ENTITY___COLLECT_UUID_OBJECTS = MMXCorePackage.UUID_OBJECT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALEGAL_ENTITY___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALEGAL_ENTITY___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALEGAL_ENTITY___ECONTAINER_OP = MMXCorePackage.UUID_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>ALegal Entity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALEGAL_ENTITY_OPERATION_COUNT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.AIndexImpl <em>AIndex</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.AIndexImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getAIndex()
	 * @generated
	 */
	int AINDEX = 9;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AINDEX__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AINDEX__PROXIES = MMXCorePackage.UUID_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AINDEX__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AINDEX__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AINDEX__OTHER_NAMES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>AIndex</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AINDEX_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AINDEX___MAKE_PROXIES = MMXCorePackage.UUID_OBJECT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AINDEX___RESOLVE_PROXIES__MAP = MMXCorePackage.UUID_OBJECT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AINDEX___RESTORE_PROXIES = MMXCorePackage.UUID_OBJECT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AINDEX___COLLECT_UUID_OBJECTS__MAP = MMXCorePackage.UUID_OBJECT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AINDEX___COLLECT_UUID_OBJECTS = MMXCorePackage.UUID_OBJECT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AINDEX___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AINDEX___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AINDEX___ECONTAINER_OP = MMXCorePackage.UUID_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>AIndex</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AINDEX_OPERATION_COUNT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.ACargoImpl <em>ACargo</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.ACargoImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getACargo()
	 * @generated
	 */
	int ACARGO = 10;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACARGO__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACARGO__PROXIES = MMXCorePackage.UUID_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACARGO__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACARGO__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACARGO__OTHER_NAMES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>ACargo</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACARGO_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACARGO___MAKE_PROXIES = MMXCorePackage.UUID_OBJECT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACARGO___RESOLVE_PROXIES__MAP = MMXCorePackage.UUID_OBJECT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACARGO___RESTORE_PROXIES = MMXCorePackage.UUID_OBJECT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACARGO___COLLECT_UUID_OBJECTS__MAP = MMXCorePackage.UUID_OBJECT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACARGO___COLLECT_UUID_OBJECTS = MMXCorePackage.UUID_OBJECT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACARGO___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACARGO___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACARGO___ECONTAINER_OP = MMXCorePackage.UUID_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>ACargo</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACARGO_OPERATION_COUNT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.ASlotImpl <em>ASlot</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.ASlotImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getASlot()
	 * @generated
	 */
	int ASLOT = 11;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASLOT__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASLOT__PROXIES = MMXCorePackage.UUID_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASLOT__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASLOT__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASLOT__OTHER_NAMES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>ASlot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASLOT_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASLOT___MAKE_PROXIES = MMXCorePackage.UUID_OBJECT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASLOT___RESOLVE_PROXIES__MAP = MMXCorePackage.UUID_OBJECT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASLOT___RESTORE_PROXIES = MMXCorePackage.UUID_OBJECT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASLOT___COLLECT_UUID_OBJECTS__MAP = MMXCorePackage.UUID_OBJECT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASLOT___COLLECT_UUID_OBJECTS = MMXCorePackage.UUID_OBJECT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASLOT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASLOT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASLOT___ECONTAINER_OP = MMXCorePackage.UUID_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>ASlot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASLOT_OPERATION_COUNT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.ITimezoneProvider <em>ITimezone Provider</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.ITimezoneProvider
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getITimezoneProvider()
	 * @generated
	 */
	int ITIMEZONE_PROVIDER = 13;

	/**
	 * The number of structural features of the '<em>ITimezone Provider</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITIMEZONE_PROVIDER_FEATURE_COUNT = 0;

	/**
	 * The operation id for the '<em>Get Time Zone</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITIMEZONE_PROVIDER___GET_TIME_ZONE__EATTRIBUTE = 0;

	/**
	 * The number of operations of the '<em>ITimezone Provider</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITIMEZONE_PROVIDER_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.ABaseFuelImpl <em>ABase Fuel</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.ABaseFuelImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getABaseFuel()
	 * @generated
	 */
	int ABASE_FUEL = 14;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABASE_FUEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABASE_FUEL__PROXIES = MMXCorePackage.UUID_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABASE_FUEL__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABASE_FUEL__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABASE_FUEL__OTHER_NAMES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>ABase Fuel</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABASE_FUEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABASE_FUEL___MAKE_PROXIES = MMXCorePackage.UUID_OBJECT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABASE_FUEL___RESOLVE_PROXIES__MAP = MMXCorePackage.UUID_OBJECT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABASE_FUEL___RESTORE_PROXIES = MMXCorePackage.UUID_OBJECT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABASE_FUEL___COLLECT_UUID_OBJECTS__MAP = MMXCorePackage.UUID_OBJECT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABASE_FUEL___COLLECT_UUID_OBJECTS = MMXCorePackage.UUID_OBJECT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABASE_FUEL___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABASE_FUEL___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABASE_FUEL___ECONTAINER_OP = MMXCorePackage.UUID_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>ABase Fuel</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABASE_FUEL_OPERATION_COUNT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.ASpotMarketImpl <em>ASpot Market</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.ASpotMarketImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getASpotMarket()
	 * @generated
	 */
	int ASPOT_MARKET = 15;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASPOT_MARKET__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASPOT_MARKET__PROXIES = MMXCorePackage.UUID_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASPOT_MARKET__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASPOT_MARKET__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASPOT_MARKET__OTHER_NAMES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>ASpot Market</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASPOT_MARKET_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASPOT_MARKET___MAKE_PROXIES = MMXCorePackage.UUID_OBJECT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASPOT_MARKET___RESOLVE_PROXIES__MAP = MMXCorePackage.UUID_OBJECT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASPOT_MARKET___RESTORE_PROXIES = MMXCorePackage.UUID_OBJECT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASPOT_MARKET___COLLECT_UUID_OBJECTS__MAP = MMXCorePackage.UUID_OBJECT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASPOT_MARKET___COLLECT_UUID_OBJECTS = MMXCorePackage.UUID_OBJECT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASPOT_MARKET___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASPOT_MARKET___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASPOT_MARKET___ECONTAINER_OP = MMXCorePackage.UUID_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>ASpot Market</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASPOT_MARKET_OPERATION_COUNT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.AOptimisationSettingsImpl <em>AOptimisation Settings</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.AOptimisationSettingsImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getAOptimisationSettings()
	 * @generated
	 */
	int AOPTIMISATION_SETTINGS = 16;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AOPTIMISATION_SETTINGS__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AOPTIMISATION_SETTINGS__PROXIES = MMXCorePackage.UUID_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AOPTIMISATION_SETTINGS__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AOPTIMISATION_SETTINGS__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AOPTIMISATION_SETTINGS__OTHER_NAMES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>AOptimisation Settings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AOPTIMISATION_SETTINGS_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AOPTIMISATION_SETTINGS___MAKE_PROXIES = MMXCorePackage.UUID_OBJECT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AOPTIMISATION_SETTINGS___RESOLVE_PROXIES__MAP = MMXCorePackage.UUID_OBJECT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AOPTIMISATION_SETTINGS___RESTORE_PROXIES = MMXCorePackage.UUID_OBJECT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AOPTIMISATION_SETTINGS___COLLECT_UUID_OBJECTS__MAP = MMXCorePackage.UUID_OBJECT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AOPTIMISATION_SETTINGS___COLLECT_UUID_OBJECTS = MMXCorePackage.UUID_OBJECT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AOPTIMISATION_SETTINGS___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AOPTIMISATION_SETTINGS___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AOPTIMISATION_SETTINGS___ECONTAINER_OP = MMXCorePackage.UUID_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>AOptimisation Settings</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AOPTIMISATION_SETTINGS_OPERATION_COUNT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.APurchaseContractImpl <em>APurchase Contract</em>}' class.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.APurchaseContractImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getAPurchaseContract()
	 * @generated
	 */
	int APURCHASE_CONTRACT = 17;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APURCHASE_CONTRACT__EXTENSIONS = ACONTRACT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APURCHASE_CONTRACT__PROXIES = ACONTRACT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APURCHASE_CONTRACT__UUID = ACONTRACT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APURCHASE_CONTRACT__NAME = ACONTRACT__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APURCHASE_CONTRACT__OTHER_NAMES = ACONTRACT__OTHER_NAMES;

	/**
	 * The number of structural features of the '<em>APurchase Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APURCHASE_CONTRACT_FEATURE_COUNT = ACONTRACT_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APURCHASE_CONTRACT___MAKE_PROXIES = ACONTRACT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APURCHASE_CONTRACT___RESOLVE_PROXIES__MAP = ACONTRACT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APURCHASE_CONTRACT___RESTORE_PROXIES = ACONTRACT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APURCHASE_CONTRACT___COLLECT_UUID_OBJECTS__MAP = ACONTRACT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APURCHASE_CONTRACT___COLLECT_UUID_OBJECTS = ACONTRACT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APURCHASE_CONTRACT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = ACONTRACT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APURCHASE_CONTRACT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = ACONTRACT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APURCHASE_CONTRACT___ECONTAINER_OP = ACONTRACT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>APurchase Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APURCHASE_CONTRACT_OPERATION_COUNT = ACONTRACT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.ASalesContractImpl <em>ASales Contract</em>}' class.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.ASalesContractImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getASalesContract()
	 * @generated
	 */
	int ASALES_CONTRACT = 18;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASALES_CONTRACT__EXTENSIONS = ACONTRACT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASALES_CONTRACT__PROXIES = ACONTRACT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASALES_CONTRACT__UUID = ACONTRACT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASALES_CONTRACT__NAME = ACONTRACT__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASALES_CONTRACT__OTHER_NAMES = ACONTRACT__OTHER_NAMES;

	/**
	 * The number of structural features of the '<em>ASales Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASALES_CONTRACT_FEATURE_COUNT = ACONTRACT_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASALES_CONTRACT___MAKE_PROXIES = ACONTRACT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASALES_CONTRACT___RESOLVE_PROXIES__MAP = ACONTRACT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASALES_CONTRACT___RESTORE_PROXIES = ACONTRACT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASALES_CONTRACT___COLLECT_UUID_OBJECTS__MAP = ACONTRACT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASALES_CONTRACT___COLLECT_UUID_OBJECTS = ACONTRACT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASALES_CONTRACT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = ACONTRACT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASALES_CONTRACT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = ACONTRACT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASALES_CONTRACT___ECONTAINER_OP = ACONTRACT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>ASales Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASALES_CONTRACT_OPERATION_COUNT = ACONTRACT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.ExtraDataContainerImpl <em>Extra Data Container</em>}' class.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.ExtraDataContainerImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getExtraDataContainer()
	 * @generated
	 */
	int EXTRA_DATA_CONTAINER = 20;

	/**
	 * The feature id for the '<em><b>Extra Data</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTRA_DATA_CONTAINER__EXTRA_DATA = 0;

	/**
	 * The number of structural features of the '<em>Extra Data Container</em>' class.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTRA_DATA_CONTAINER_FEATURE_COUNT = 1;

	/**
	 * The operation id for the '<em>Get Data With Path</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTRA_DATA_CONTAINER___GET_DATA_WITH_PATH__ITERABLE = 0;

	/**
	 * The operation id for the '<em>Get Data With Key</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTRA_DATA_CONTAINER___GET_DATA_WITH_KEY__STRING = 1;

	/**
	 * The operation id for the '<em>Add Extra Data</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTRA_DATA_CONTAINER___ADD_EXTRA_DATA__STRING_STRING = 2;

	/**
	 * The operation id for the '<em>Add Extra Data</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTRA_DATA_CONTAINER___ADD_EXTRA_DATA__STRING_STRING_SERIALIZABLE_EXTRADATAFORMATTYPE = 3;

	/**
	 * The operation id for the '<em>Get Value With Path As</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTRA_DATA_CONTAINER___GET_VALUE_WITH_PATH_AS__ITERABLE_CLASS_OBJECT = 4;

	/**
	 * The number of operations of the '<em>Extra Data Container</em>' class.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTRA_DATA_CONTAINER_OPERATION_COUNT = 5;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.ExtraDataImpl <em>Extra Data</em>}' class.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.ExtraDataImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getExtraData()
	 * @generated
	 */
	int EXTRA_DATA = 19;

	/**
	 * The feature id for the '<em><b>Extra Data</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTRA_DATA__EXTRA_DATA = EXTRA_DATA_CONTAINER__EXTRA_DATA;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTRA_DATA__KEY = EXTRA_DATA_CONTAINER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTRA_DATA__NAME = EXTRA_DATA_CONTAINER_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTRA_DATA__VALUE = EXTRA_DATA_CONTAINER_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Format</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTRA_DATA__FORMAT = EXTRA_DATA_CONTAINER_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Format Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTRA_DATA__FORMAT_TYPE = EXTRA_DATA_CONTAINER_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Extra Data</em>' class.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTRA_DATA_FEATURE_COUNT = EXTRA_DATA_CONTAINER_FEATURE_COUNT + 5;

	/**
	 * The operation id for the '<em>Get Data With Path</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTRA_DATA___GET_DATA_WITH_PATH__ITERABLE = EXTRA_DATA_CONTAINER___GET_DATA_WITH_PATH__ITERABLE;

	/**
	 * The operation id for the '<em>Get Data With Key</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTRA_DATA___GET_DATA_WITH_KEY__STRING = EXTRA_DATA_CONTAINER___GET_DATA_WITH_KEY__STRING;

	/**
	 * The operation id for the '<em>Add Extra Data</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTRA_DATA___ADD_EXTRA_DATA__STRING_STRING = EXTRA_DATA_CONTAINER___ADD_EXTRA_DATA__STRING_STRING;

	/**
	 * The operation id for the '<em>Add Extra Data</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTRA_DATA___ADD_EXTRA_DATA__STRING_STRING_SERIALIZABLE_EXTRADATAFORMATTYPE = EXTRA_DATA_CONTAINER___ADD_EXTRA_DATA__STRING_STRING_SERIALIZABLE_EXTRADATAFORMATTYPE;

	/**
	 * The operation id for the '<em>Get Value With Path As</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTRA_DATA___GET_VALUE_WITH_PATH_AS__ITERABLE_CLASS_OBJECT = EXTRA_DATA_CONTAINER___GET_VALUE_WITH_PATH_AS__ITERABLE_CLASS_OBJECT;

	/**
	 * The operation id for the '<em>Get Value As</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTRA_DATA___GET_VALUE_AS__CLASS = EXTRA_DATA_CONTAINER_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Format Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTRA_DATA___FORMAT_VALUE = EXTRA_DATA_CONTAINER_OPERATION_COUNT + 1;

	/**
	 * The number of operations of the '<em>Extra Data</em>' class.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTRA_DATA_OPERATION_COUNT = EXTRA_DATA_CONTAINER_OPERATION_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.PortCapability <em>Port Capability</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.PortCapability
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getPortCapability()
	 * @generated
	 */
	int PORT_CAPABILITY = 21;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.ExtraDataFormatType <em>Extra Data Format Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.ExtraDataFormatType
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getExtraDataFormatType()
	 * @generated
	 */
	int EXTRA_DATA_FORMAT_TYPE = 22;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.CargoDeliveryType <em>Cargo Delivery Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * @since 3.1
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.CargoDeliveryType
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getCargoDeliveryType()
	 * @generated
	 */
	int CARGO_DELIVERY_TYPE = 25;

	/**
	 * The meta object id for the '<em>Serializable Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see java.io.Serializable
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getSerializableObject()
	 * @generated
	 */
	int SERIALIZABLE_OBJECT = 23;

	/**
	 * The meta object id for the '<em>Iterable</em>' data type.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see java.lang.Iterable
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getIterable()
	 * @generated
	 */
	int ITERABLE = 24;

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.types.APort <em>APort</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>APort</em>'.
	 * @see com.mmxlabs.models.lng.types.APort
	 * @generated
	 */
	EClass getAPort();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.types.APort#collect(org.eclipse.emf.common.util.EList) <em>Collect</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Collect</em>' operation.
	 * @see com.mmxlabs.models.lng.types.APort#collect(org.eclipse.emf.common.util.EList)
	 * @generated
	 */
	EOperation getAPort__Collect__EList();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.types.APortSet <em>APort Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>APort Set</em>'.
	 * @see com.mmxlabs.models.lng.types.APortSet
	 * @generated
	 */
	EClass getAPortSet();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.types.APortSet#collect(org.eclipse.emf.common.util.EList) <em>Collect</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Collect</em>' operation.
	 * @see com.mmxlabs.models.lng.types.APortSet#collect(org.eclipse.emf.common.util.EList)
	 * @generated
	 */
	EOperation getAPortSet__Collect__EList();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.types.ARoute <em>ARoute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>ARoute</em>'.
	 * @see com.mmxlabs.models.lng.types.ARoute
	 * @generated
	 */
	EClass getARoute();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.types.AVessel <em>AVessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>AVessel</em>'.
	 * @see com.mmxlabs.models.lng.types.AVessel
	 * @generated
	 */
	EClass getAVessel();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.types.AVessel#collect(org.eclipse.emf.common.util.EList) <em>Collect</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Collect</em>' operation.
	 * @see com.mmxlabs.models.lng.types.AVessel#collect(org.eclipse.emf.common.util.EList)
	 * @generated
	 */
	EOperation getAVessel__Collect__EList();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.types.AFleetVessel <em>AFleet Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>AFleet Vessel</em>'.
	 * @see com.mmxlabs.models.lng.types.AFleetVessel
	 * @generated
	 */
	EClass getAFleetVessel();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.types.AVesselClass <em>AVessel Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>AVessel Class</em>'.
	 * @see com.mmxlabs.models.lng.types.AVesselClass
	 * @generated
	 */
	EClass getAVesselClass();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.types.AVesselEvent <em>AVessel Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>AVessel Event</em>'.
	 * @see com.mmxlabs.models.lng.types.AVesselEvent
	 * @generated
	 */
	EClass getAVesselEvent();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.types.AContract <em>AContract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>AContract</em>'.
	 * @see com.mmxlabs.models.lng.types.AContract
	 * @generated
	 */
	EClass getAContract();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.types.ALegalEntity <em>ALegal Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>ALegal Entity</em>'.
	 * @see com.mmxlabs.models.lng.types.ALegalEntity
	 * @generated
	 */
	EClass getALegalEntity();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.types.AIndex <em>AIndex</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>AIndex</em>'.
	 * @see com.mmxlabs.models.lng.types.AIndex
	 * @generated
	 */
	EClass getAIndex();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.types.ACargo <em>ACargo</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>ACargo</em>'.
	 * @see com.mmxlabs.models.lng.types.ACargo
	 * @generated
	 */
	EClass getACargo();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.types.ASlot <em>ASlot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>ASlot</em>'.
	 * @see com.mmxlabs.models.lng.types.ASlot
	 * @generated
	 */
	EClass getASlot();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.types.AVesselSet <em>AVessel Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>AVessel Set</em>'.
	 * @see com.mmxlabs.models.lng.types.AVesselSet
	 * @generated
	 */
	EClass getAVesselSet();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.types.AVesselSet#collect(org.eclipse.emf.common.util.EList) <em>Collect</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Collect</em>' operation.
	 * @see com.mmxlabs.models.lng.types.AVesselSet#collect(org.eclipse.emf.common.util.EList)
	 * @generated
	 */
	EOperation getAVesselSet__Collect__EList();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.types.ITimezoneProvider <em>ITimezone Provider</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>ITimezone Provider</em>'.
	 * @see com.mmxlabs.models.lng.types.ITimezoneProvider
	 * @generated
	 */
	EClass getITimezoneProvider();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.types.ITimezoneProvider#getTimeZone(org.eclipse.emf.ecore.EAttribute) <em>Get Time Zone</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Time Zone</em>' operation.
	 * @see com.mmxlabs.models.lng.types.ITimezoneProvider#getTimeZone(org.eclipse.emf.ecore.EAttribute)
	 * @generated
	 */
	EOperation getITimezoneProvider__GetTimeZone__EAttribute();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.types.ABaseFuel <em>ABase Fuel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>ABase Fuel</em>'.
	 * @see com.mmxlabs.models.lng.types.ABaseFuel
	 * @generated
	 */
	EClass getABaseFuel();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.types.ASpotMarket <em>ASpot Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>ASpot Market</em>'.
	 * @see com.mmxlabs.models.lng.types.ASpotMarket
	 * @generated
	 */
	EClass getASpotMarket();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.types.AOptimisationSettings <em>AOptimisation Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>AOptimisation Settings</em>'.
	 * @see com.mmxlabs.models.lng.types.AOptimisationSettings
	 * @generated
	 */
	EClass getAOptimisationSettings();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.types.APurchaseContract <em>APurchase Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>APurchase Contract</em>'.
	 * @see com.mmxlabs.models.lng.types.APurchaseContract
	 * @generated
	 */
	EClass getAPurchaseContract();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.types.ASalesContract <em>ASales Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>ASales Contract</em>'.
	 * @see com.mmxlabs.models.lng.types.ASalesContract
	 * @generated
	 */
	EClass getASalesContract();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.types.ExtraData <em>Extra Data</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Extra Data</em>'.
	 * @see com.mmxlabs.models.lng.types.ExtraData
	 * @generated
	 */
	EClass getExtraData();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.types.ExtraData#getKey <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see com.mmxlabs.models.lng.types.ExtraData#getKey()
	 * @see #getExtraData()
	 * @generated
	 */
	EAttribute getExtraData_Key();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.types.ExtraData#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see com.mmxlabs.models.lng.types.ExtraData#getName()
	 * @see #getExtraData()
	 * @generated
	 */
	EAttribute getExtraData_Name();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.types.ExtraData#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see com.mmxlabs.models.lng.types.ExtraData#getValue()
	 * @see #getExtraData()
	 * @generated
	 */
	EAttribute getExtraData_Value();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.types.ExtraData#getFormat <em>Format</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Format</em>'.
	 * @see com.mmxlabs.models.lng.types.ExtraData#getFormat()
	 * @see #getExtraData()
	 * @generated
	 */
	EAttribute getExtraData_Format();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.types.ExtraData#getFormatType <em>Format Type</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Format Type</em>'.
	 * @see com.mmxlabs.models.lng.types.ExtraData#getFormatType()
	 * @see #getExtraData()
	 * @generated
	 */
	EAttribute getExtraData_FormatType();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.types.ExtraData#getValueAs(java.lang.Class) <em>Get Value As</em>}' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Value As</em>' operation.
	 * @see com.mmxlabs.models.lng.types.ExtraData#getValueAs(java.lang.Class)
	 * @generated
	 */
	EOperation getExtraData__GetValueAs__Class();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.types.ExtraData#formatValue() <em>Format Value</em>}' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Format Value</em>' operation.
	 * @see com.mmxlabs.models.lng.types.ExtraData#formatValue()
	 * @generated
	 */
	EOperation getExtraData__FormatValue();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.types.ExtraDataContainer <em>Extra Data Container</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Extra Data Container</em>'.
	 * @see com.mmxlabs.models.lng.types.ExtraDataContainer
	 * @generated
	 */
	EClass getExtraDataContainer();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.types.ExtraDataContainer#getExtraData <em>Extra Data</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Extra Data</em>'.
	 * @see com.mmxlabs.models.lng.types.ExtraDataContainer#getExtraData()
	 * @see #getExtraDataContainer()
	 * @generated
	 */
	EReference getExtraDataContainer_ExtraData();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.types.ExtraDataContainer#getDataWithPath(java.lang.Iterable) <em>Get Data With Path</em>}' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Data With Path</em>' operation.
	 * @see com.mmxlabs.models.lng.types.ExtraDataContainer#getDataWithPath(java.lang.Iterable)
	 * @generated
	 */
	EOperation getExtraDataContainer__GetDataWithPath__Iterable();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.types.ExtraDataContainer#getDataWithKey(java.lang.String) <em>Get Data With Key</em>}' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Data With Key</em>' operation.
	 * @see com.mmxlabs.models.lng.types.ExtraDataContainer#getDataWithKey(java.lang.String)
	 * @generated
	 */
	EOperation getExtraDataContainer__GetDataWithKey__String();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.types.ExtraDataContainer#addExtraData(java.lang.String, java.lang.String) <em>Add Extra Data</em>}' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Add Extra Data</em>' operation.
	 * @see com.mmxlabs.models.lng.types.ExtraDataContainer#addExtraData(java.lang.String, java.lang.String)
	 * @generated
	 */
	EOperation getExtraDataContainer__AddExtraData__String_String();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.types.ExtraDataContainer#addExtraData(java.lang.String, java.lang.String, java.io.Serializable, com.mmxlabs.models.lng.types.ExtraDataFormatType) <em>Add Extra Data</em>}' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Add Extra Data</em>' operation.
	 * @see com.mmxlabs.models.lng.types.ExtraDataContainer#addExtraData(java.lang.String, java.lang.String, java.io.Serializable, com.mmxlabs.models.lng.types.ExtraDataFormatType)
	 * @generated
	 */
	EOperation getExtraDataContainer__AddExtraData__String_String_Serializable_ExtraDataFormatType();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.types.ExtraDataContainer#getValueWithPathAs(java.lang.Iterable, java.lang.Class, java.lang.Object) <em>Get Value With Path As</em>}' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Value With Path As</em>' operation.
	 * @see com.mmxlabs.models.lng.types.ExtraDataContainer#getValueWithPathAs(java.lang.Iterable, java.lang.Class, java.lang.Object)
	 * @generated
	 */
	EOperation getExtraDataContainer__GetValueWithPathAs__Iterable_Class_Object();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.types.PortCapability <em>Port Capability</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Port Capability</em>'.
	 * @see com.mmxlabs.models.lng.types.PortCapability
	 * @generated
	 */
	EEnum getPortCapability();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.types.ExtraDataFormatType <em>Extra Data Format Type</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Extra Data Format Type</em>'.
	 * @see com.mmxlabs.models.lng.types.ExtraDataFormatType
	 * @generated
	 */
	EEnum getExtraDataFormatType();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.types.CargoDeliveryType <em>Cargo Delivery Type</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 3.1
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Cargo Delivery Type</em>'.
	 * @see com.mmxlabs.models.lng.types.CargoDeliveryType
	 * @generated
	 */
	EEnum getCargoDeliveryType();

	/**
	 * Returns the meta object for data type '{@link java.io.Serializable <em>Serializable Object</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Serializable Object</em>'.
	 * @see java.io.Serializable
	 * @model instanceClass="java.io.Serializable"
	 * @generated
	 */
	EDataType getSerializableObject();

	/**
	 * Returns the meta object for data type '{@link java.lang.Iterable <em>Iterable</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Iterable</em>'.
	 * @see java.lang.Iterable
	 * @model instanceClass="java.lang.Iterable" typeParameters="T"
	 * @generated
	 */
	EDataType getIterable();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	TypesFactory getTypesFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * @noimplement This interface is not intended to be implemented by clients.
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.impl.APortImpl <em>APort</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.impl.APortImpl
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getAPort()
		 * @generated
		 */
		EClass APORT = eINSTANCE.getAPort();

		/**
		 * The meta object literal for the '<em><b>Collect</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation APORT___COLLECT__ELIST = eINSTANCE.getAPort__Collect__EList();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.impl.APortSetImpl <em>APort Set</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.impl.APortSetImpl
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getAPortSet()
		 * @generated
		 */
		EClass APORT_SET = eINSTANCE.getAPortSet();

		/**
		 * The meta object literal for the '<em><b>Collect</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation APORT_SET___COLLECT__ELIST = eINSTANCE.getAPortSet__Collect__EList();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.impl.ARouteImpl <em>ARoute</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.impl.ARouteImpl
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getARoute()
		 * @generated
		 */
		EClass AROUTE = eINSTANCE.getARoute();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.impl.AVesselImpl <em>AVessel</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.impl.AVesselImpl
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getAVessel()
		 * @generated
		 */
		EClass AVESSEL = eINSTANCE.getAVessel();

		/**
		 * The meta object literal for the '<em><b>Collect</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation AVESSEL___COLLECT__ELIST = eINSTANCE.getAVessel__Collect__EList();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.impl.AFleetVesselImpl <em>AFleet Vessel</em>}' class.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.impl.AFleetVesselImpl
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getAFleetVessel()
		 * @generated
		 */
		EClass AFLEET_VESSEL = eINSTANCE.getAFleetVessel();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.impl.AVesselClassImpl <em>AVessel Class</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.impl.AVesselClassImpl
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getAVesselClass()
		 * @generated
		 */
		EClass AVESSEL_CLASS = eINSTANCE.getAVesselClass();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.impl.AVesselEventImpl <em>AVessel Event</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.impl.AVesselEventImpl
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getAVesselEvent()
		 * @generated
		 */
		EClass AVESSEL_EVENT = eINSTANCE.getAVesselEvent();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.impl.AContractImpl <em>AContract</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.impl.AContractImpl
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getAContract()
		 * @generated
		 */
		EClass ACONTRACT = eINSTANCE.getAContract();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.impl.ALegalEntityImpl <em>ALegal Entity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.impl.ALegalEntityImpl
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getALegalEntity()
		 * @generated
		 */
		EClass ALEGAL_ENTITY = eINSTANCE.getALegalEntity();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.impl.AIndexImpl <em>AIndex</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.impl.AIndexImpl
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getAIndex()
		 * @generated
		 */
		EClass AINDEX = eINSTANCE.getAIndex();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.impl.ACargoImpl <em>ACargo</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.impl.ACargoImpl
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getACargo()
		 * @generated
		 */
		EClass ACARGO = eINSTANCE.getACargo();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.impl.ASlotImpl <em>ASlot</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.impl.ASlotImpl
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getASlot()
		 * @generated
		 */
		EClass ASLOT = eINSTANCE.getASlot();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.impl.AVesselSetImpl <em>AVessel Set</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.impl.AVesselSetImpl
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getAVesselSet()
		 * @generated
		 */
		EClass AVESSEL_SET = eINSTANCE.getAVesselSet();

		/**
		 * The meta object literal for the '<em><b>Collect</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation AVESSEL_SET___COLLECT__ELIST = eINSTANCE.getAVesselSet__Collect__EList();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.ITimezoneProvider <em>ITimezone Provider</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.ITimezoneProvider
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getITimezoneProvider()
		 * @generated
		 */
		EClass ITIMEZONE_PROVIDER = eINSTANCE.getITimezoneProvider();

		/**
		 * The meta object literal for the '<em><b>Get Time Zone</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation ITIMEZONE_PROVIDER___GET_TIME_ZONE__EATTRIBUTE = eINSTANCE.getITimezoneProvider__GetTimeZone__EAttribute();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.impl.ABaseFuelImpl <em>ABase Fuel</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.impl.ABaseFuelImpl
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getABaseFuel()
		 * @generated
		 */
		EClass ABASE_FUEL = eINSTANCE.getABaseFuel();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.impl.ASpotMarketImpl <em>ASpot Market</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.impl.ASpotMarketImpl
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getASpotMarket()
		 * @generated
		 */
		EClass ASPOT_MARKET = eINSTANCE.getASpotMarket();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.impl.AOptimisationSettingsImpl <em>AOptimisation Settings</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.impl.AOptimisationSettingsImpl
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getAOptimisationSettings()
		 * @generated
		 */
		EClass AOPTIMISATION_SETTINGS = eINSTANCE.getAOptimisationSettings();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.impl.APurchaseContractImpl <em>APurchase Contract</em>}' class.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.impl.APurchaseContractImpl
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getAPurchaseContract()
		 * @generated
		 */
		EClass APURCHASE_CONTRACT = eINSTANCE.getAPurchaseContract();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.impl.ASalesContractImpl <em>ASales Contract</em>}' class.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.impl.ASalesContractImpl
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getASalesContract()
		 * @generated
		 */
		EClass ASALES_CONTRACT = eINSTANCE.getASalesContract();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.impl.ExtraDataImpl <em>Extra Data</em>}' class.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.impl.ExtraDataImpl
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getExtraData()
		 * @generated
		 */
		EClass EXTRA_DATA = eINSTANCE.getExtraData();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXTRA_DATA__KEY = eINSTANCE.getExtraData_Key();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXTRA_DATA__NAME = eINSTANCE.getExtraData_Name();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXTRA_DATA__VALUE = eINSTANCE.getExtraData_Value();

		/**
		 * The meta object literal for the '<em><b>Format</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXTRA_DATA__FORMAT = eINSTANCE.getExtraData_Format();

		/**
		 * The meta object literal for the '<em><b>Format Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXTRA_DATA__FORMAT_TYPE = eINSTANCE.getExtraData_FormatType();

		/**
		 * The meta object literal for the '<em><b>Get Value As</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation EXTRA_DATA___GET_VALUE_AS__CLASS = eINSTANCE.getExtraData__GetValueAs__Class();

		/**
		 * The meta object literal for the '<em><b>Format Value</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation EXTRA_DATA___FORMAT_VALUE = eINSTANCE.getExtraData__FormatValue();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.impl.ExtraDataContainerImpl <em>Extra Data Container</em>}' class.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.impl.ExtraDataContainerImpl
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getExtraDataContainer()
		 * @generated
		 */
		EClass EXTRA_DATA_CONTAINER = eINSTANCE.getExtraDataContainer();

		/**
		 * The meta object literal for the '<em><b>Extra Data</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXTRA_DATA_CONTAINER__EXTRA_DATA = eINSTANCE.getExtraDataContainer_ExtraData();

		/**
		 * The meta object literal for the '<em><b>Get Data With Path</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation EXTRA_DATA_CONTAINER___GET_DATA_WITH_PATH__ITERABLE = eINSTANCE.getExtraDataContainer__GetDataWithPath__Iterable();

		/**
		 * The meta object literal for the '<em><b>Get Data With Key</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation EXTRA_DATA_CONTAINER___GET_DATA_WITH_KEY__STRING = eINSTANCE.getExtraDataContainer__GetDataWithKey__String();

		/**
		 * The meta object literal for the '<em><b>Add Extra Data</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation EXTRA_DATA_CONTAINER___ADD_EXTRA_DATA__STRING_STRING = eINSTANCE.getExtraDataContainer__AddExtraData__String_String();

		/**
		 * The meta object literal for the '<em><b>Add Extra Data</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation EXTRA_DATA_CONTAINER___ADD_EXTRA_DATA__STRING_STRING_SERIALIZABLE_EXTRADATAFORMATTYPE = eINSTANCE
				.getExtraDataContainer__AddExtraData__String_String_Serializable_ExtraDataFormatType();

		/**
		 * The meta object literal for the '<em><b>Get Value With Path As</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation EXTRA_DATA_CONTAINER___GET_VALUE_WITH_PATH_AS__ITERABLE_CLASS_OBJECT = eINSTANCE.getExtraDataContainer__GetValueWithPathAs__Iterable_Class_Object();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.PortCapability <em>Port Capability</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.PortCapability
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getPortCapability()
		 * @generated
		 */
		EEnum PORT_CAPABILITY = eINSTANCE.getPortCapability();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.ExtraDataFormatType <em>Extra Data Format Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.ExtraDataFormatType
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getExtraDataFormatType()
		 * @generated
		 */
		EEnum EXTRA_DATA_FORMAT_TYPE = eINSTANCE.getExtraDataFormatType();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.CargoDeliveryType <em>Cargo Delivery Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * @since 3.1
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.CargoDeliveryType
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getCargoDeliveryType()
		 * @generated
		 */
		EEnum CARGO_DELIVERY_TYPE = eINSTANCE.getCargoDeliveryType();

		/**
		 * The meta object literal for the '<em>Serializable Object</em>' data type.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @see java.io.Serializable
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getSerializableObject()
		 * @generated
		 */
		EDataType SERIALIZABLE_OBJECT = eINSTANCE.getSerializableObject();

		/**
		 * The meta object literal for the '<em>Iterable</em>' data type.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @see java.lang.Iterable
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getIterable()
		 * @generated
		 */
		EDataType ITERABLE = eINSTANCE.getIterable();

	}

} //TypesPackage
