/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.types;

import com.mmxlabs.models.mmxcore.MMXCorePackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
	 * The number of structural features of the '<em>APort Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT_SET_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

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
	 * The number of structural features of the '<em>APort</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT_FEATURE_COUNT = APORT_SET_FEATURE_COUNT + 0;

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
	 * The number of structural features of the '<em>ARoute</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AROUTE_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

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
	int AVESSEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL__PROXIES = MMXCorePackage.UUID_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>AVessel</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.AVesselClassImpl <em>AVessel Class</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.AVesselClassImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getAVesselClass()
	 * @generated
	 */
	int AVESSEL_CLASS = 4;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_CLASS__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_CLASS__PROXIES = MMXCorePackage.UUID_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_CLASS__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_CLASS__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>AVessel Class</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_CLASS_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.AVesselEventImpl <em>AVessel Event</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.AVesselEventImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getAVesselEvent()
	 * @generated
	 */
	int AVESSEL_EVENT = 5;

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
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_EVENT__PORT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>AVessel Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_EVENT_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.AContractImpl <em>AContract</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.AContractImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getAContract()
	 * @generated
	 */
	int ACONTRACT = 6;

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
	 * The number of structural features of the '<em>AContract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACONTRACT_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.ALegalEntityImpl <em>ALegal Entity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.ALegalEntityImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getALegalEntity()
	 * @generated
	 */
	int ALEGAL_ENTITY = 7;

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
	 * The number of structural features of the '<em>ALegal Entity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALEGAL_ENTITY_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.AIndexImpl <em>AIndex</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.AIndexImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getAIndex()
	 * @generated
	 */
	int AINDEX = 8;

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
	 * The number of structural features of the '<em>AIndex</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AINDEX_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.ACargoImpl <em>ACargo</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.ACargoImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getACargo()
	 * @generated
	 */
	int ACARGO = 9;

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
	 * The number of structural features of the '<em>ACargo</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACARGO_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.ASlotImpl <em>ASlot</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.ASlotImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getASlot()
	 * @generated
	 */
	int ASLOT = 10;

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
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASLOT__PORT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>ASlot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASLOT_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;


	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.ALocatedImpl <em>ALocated</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.ALocatedImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getALocated()
	 * @generated
	 */
	int ALOCATED = 11;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALOCATED__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALOCATED__PROXIES = MMXCorePackage.MMX_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALOCATED__PORT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>ALocated</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALOCATED_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.TimeWindowImpl <em>Time Window</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.TimeWindowImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getTimeWindow()
	 * @generated
	 */
	int TIME_WINDOW = 12;

	/**
	 * The feature id for the '<em><b>Start Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIME_WINDOW__START_DATE = 0;

	/**
	 * The feature id for the '<em><b>Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIME_WINDOW__LENGTH = 1;

	/**
	 * The number of structural features of the '<em>Time Window</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIME_WINDOW_FEATURE_COUNT = 2;


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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.types.APortSet <em>APort Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>APort Set</em>'.
	 * @see com.mmxlabs.models.lng.types.APortSet
	 * @generated
	 */
	EClass getAPortSet();

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.types.ALocated <em>ALocated</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>ALocated</em>'.
	 * @see com.mmxlabs.models.lng.types.ALocated
	 * @generated
	 */
	EClass getALocated();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.types.ALocated#getPort <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Port</em>'.
	 * @see com.mmxlabs.models.lng.types.ALocated#getPort()
	 * @see #getALocated()
	 * @generated
	 */
	EReference getALocated_Port();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.types.TimeWindow <em>Time Window</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Time Window</em>'.
	 * @see com.mmxlabs.models.lng.types.TimeWindow
	 * @generated
	 */
	EClass getTimeWindow();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.types.TimeWindow#getStartDate <em>Start Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start Date</em>'.
	 * @see com.mmxlabs.models.lng.types.TimeWindow#getStartDate()
	 * @see #getTimeWindow()
	 * @generated
	 */
	EAttribute getTimeWindow_StartDate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.types.TimeWindow#getLength <em>Length</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Length</em>'.
	 * @see com.mmxlabs.models.lng.types.TimeWindow#getLength()
	 * @see #getTimeWindow()
	 * @generated
	 */
	EAttribute getTimeWindow_Length();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.impl.APortSetImpl <em>APort Set</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.impl.APortSetImpl
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getAPortSet()
		 * @generated
		 */
		EClass APORT_SET = eINSTANCE.getAPortSet();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.impl.ALocatedImpl <em>ALocated</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.impl.ALocatedImpl
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getALocated()
		 * @generated
		 */
		EClass ALOCATED = eINSTANCE.getALocated();

		/**
		 * The meta object literal for the '<em><b>Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ALOCATED__PORT = eINSTANCE.getALocated_Port();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.impl.TimeWindowImpl <em>Time Window</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.impl.TimeWindowImpl
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getTimeWindow()
		 * @generated
		 */
		EClass TIME_WINDOW = eINSTANCE.getTimeWindow();

		/**
		 * The meta object literal for the '<em><b>Start Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TIME_WINDOW__START_DATE = eINSTANCE.getTimeWindow_StartDate();

		/**
		 * The meta object literal for the '<em><b>Length</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TIME_WINDOW__LENGTH = eINSTANCE.getTimeWindow_Length();

	}

} //TypesPackage
