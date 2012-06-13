/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.types;

import com.mmxlabs.models.mmxcore.MMXCorePackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;

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
	TypesPackage eINSTANCE = com.mmxlabs.models.lng.types.impl.TypesPackageImpl
			.init();

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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
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
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.AFleetVesselImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getAFleetVessel()
	 * @generated
	 * @since 2.0
	 */
	int AFLEET_VESSEL = 4;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
	 */
	int AFLEET_VESSEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
	 */
	int AFLEET_VESSEL__PROXIES = MMXCorePackage.UUID_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
	 */
	int AFLEET_VESSEL__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The number of structural features of the '<em>AFleet Vessel</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
	 */
	int AFLEET_VESSEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
	 */
	int AFLEET_VESSEL___MAKE_PROXIES = MMXCorePackage.UUID_OBJECT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
	 */
	int AFLEET_VESSEL___RESOLVE_PROXIES__MAP = MMXCorePackage.UUID_OBJECT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
	 */
	int AFLEET_VESSEL___RESTORE_PROXIES = MMXCorePackage.UUID_OBJECT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
	 */
	int AFLEET_VESSEL___COLLECT_UUID_OBJECTS__MAP = MMXCorePackage.UUID_OBJECT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
	 */
	int AFLEET_VESSEL___COLLECT_UUID_OBJECTS = MMXCorePackage.UUID_OBJECT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
	 */
	int AFLEET_VESSEL___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
	 */
	int AFLEET_VESSEL___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
	 */
	int AFLEET_VESSEL___ECONTAINER_OP = MMXCorePackage.UUID_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>AFleet Vessel</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 * @since 2.0
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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.PortCapability <em>Port Capability</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.PortCapability
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getPortCapability()
	 * @generated
	 */
	int PORT_CAPABILITY = 17;

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
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>AFleet Vessel</em>'.
	 * @see com.mmxlabs.models.lng.types.AFleetVessel
	 * @generated
	 * @since 2.0
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
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.types.PortCapability <em>Port Capability</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Port Capability</em>'.
	 * @see com.mmxlabs.models.lng.types.PortCapability
	 * @generated
	 */
	EEnum getPortCapability();

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
		EOperation APORT___COLLECT__ELIST = eINSTANCE
				.getAPort__Collect__EList();

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
		EOperation APORT_SET___COLLECT__ELIST = eINSTANCE
				.getAPortSet__Collect__EList();

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
		EOperation AVESSEL___COLLECT__ELIST = eINSTANCE
				.getAVessel__Collect__EList();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.impl.AFleetVesselImpl <em>AFleet Vessel</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.impl.AFleetVesselImpl
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getAFleetVessel()
		 * @generated
		 * @since 2.0
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
		EOperation AVESSEL_SET___COLLECT__ELIST = eINSTANCE
				.getAVesselSet__Collect__EList();

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
		EOperation ITIMEZONE_PROVIDER___GET_TIME_ZONE__EATTRIBUTE = eINSTANCE
				.getITimezoneProvider__GetTimeZone__EAttribute();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.PortCapability <em>Port Capability</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.PortCapability
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getPortCapability()
		 * @generated
		 */
		EEnum PORT_CAPABILITY = eINSTANCE.getPortCapability();

	}

} //TypesPackage
