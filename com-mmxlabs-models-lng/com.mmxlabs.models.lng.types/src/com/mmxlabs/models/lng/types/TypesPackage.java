/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.types;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.ObjectSetImpl <em>Object Set</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.ObjectSetImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getObjectSet()
	 * @generated
	 */
	int OBJECT_SET = 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_SET__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_SET__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_SET__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_SET__OTHER_NAMES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Object Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_SET_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_SET___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_SET___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_SET___ECONTAINER_OP = MMXCorePackage.UUID_OBJECT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Collect</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_SET___COLLECT__ELIST = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The number of operations of the '<em>Object Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_SET_OPERATION_COUNT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 1;

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
	int APORT_SET__EXTENSIONS = OBJECT_SET__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT_SET__UUID = OBJECT_SET__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT_SET__NAME = OBJECT_SET__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT_SET__OTHER_NAMES = OBJECT_SET__OTHER_NAMES;

	/**
	 * The number of structural features of the '<em>APort Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT_SET_FEATURE_COUNT = OBJECT_SET_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT_SET___GET_UNSET_VALUE__ESTRUCTURALFEATURE = OBJECT_SET___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT_SET___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = OBJECT_SET___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT_SET___ECONTAINER_OP = OBJECT_SET___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Collect</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT_SET___COLLECT__ELIST = OBJECT_SET___COLLECT__ELIST;

	/**
	 * The number of operations of the '<em>APort Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT_SET_OPERATION_COUNT = OBJECT_SET_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.AVesselSetImpl <em>AVessel Set</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.AVesselSetImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getAVesselSet()
	 * @generated
	 */
	int AVESSEL_SET = 2;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_SET__EXTENSIONS = OBJECT_SET__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_SET__UUID = OBJECT_SET__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_SET__NAME = OBJECT_SET__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_SET__OTHER_NAMES = OBJECT_SET__OTHER_NAMES;

	/**
	 * The number of structural features of the '<em>AVessel Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_SET_FEATURE_COUNT = OBJECT_SET_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_SET___GET_UNSET_VALUE__ESTRUCTURALFEATURE = OBJECT_SET___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_SET___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = OBJECT_SET___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_SET___ECONTAINER_OP = OBJECT_SET___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Collect</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_SET___COLLECT__ELIST = OBJECT_SET___COLLECT__ELIST;

	/**
	 * The number of operations of the '<em>AVessel Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_SET_OPERATION_COUNT = OBJECT_SET_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.ITimezoneProvider <em>ITimezone Provider</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.ITimezoneProvider
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getITimezoneProvider()
	 * @generated
	 */
	int ITIMEZONE_PROVIDER = 3;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.impl.ExtraDataContainerImpl <em>Extra Data Container</em>}' class.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.impl.ExtraDataContainerImpl
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getExtraDataContainer()
	 * @generated
	 */
	int EXTRA_DATA_CONTAINER = 5;

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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTRA_DATA_CONTAINER___ADD_EXTRA_DATA__STRING_STRING_OBJECT_EXTRADATAFORMATTYPE = 3;

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
	int EXTRA_DATA = 4;

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
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTRA_DATA___ADD_EXTRA_DATA__STRING_STRING_OBJECT_EXTRADATAFORMATTYPE = EXTRA_DATA_CONTAINER___ADD_EXTRA_DATA__STRING_STRING_OBJECT_EXTRADATAFORMATTYPE;

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
	int PORT_CAPABILITY = 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.ExtraDataFormatType <em>Extra Data Format Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.ExtraDataFormatType
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getExtraDataFormatType()
	 * @generated
	 */
	int EXTRA_DATA_FORMAT_TYPE = 7;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.CargoDeliveryType <em>Cargo Delivery Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * @since 3.1
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.CargoDeliveryType
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getCargoDeliveryType()
	 * @generated
	 */
	int CARGO_DELIVERY_TYPE = 8;

	/**
	 * The meta object id for the '<em>Iterable</em>' data type.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see java.lang.Iterable
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getIterable()
	 * @generated
	 */
	int ITERABLE = 9;

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.types.AVesselSet <em>AVessel Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>AVessel Set</em>'.
	 * @see com.mmxlabs.models.lng.types.AVesselSet
	 * @generated
	 */
	EClass getAVesselSet();

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
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.types.ExtraDataContainer#addExtraData(java.lang.String, java.lang.String, java.lang.Object, com.mmxlabs.models.lng.types.ExtraDataFormatType) <em>Add Extra Data</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Add Extra Data</em>' operation.
	 * @see com.mmxlabs.models.lng.types.ExtraDataContainer#addExtraData(java.lang.String, java.lang.String, java.lang.Object, com.mmxlabs.models.lng.types.ExtraDataFormatType)
	 * @generated
	 */
	EOperation getExtraDataContainer__AddExtraData__String_String_Object_ExtraDataFormatType();

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.types.ObjectSet <em>Object Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Object Set</em>'.
	 * @see com.mmxlabs.models.lng.types.ObjectSet
	 * @generated
	 */
	EClass getObjectSet();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.types.ObjectSet#collect(org.eclipse.emf.common.util.EList) <em>Collect</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Collect</em>' operation.
	 * @see com.mmxlabs.models.lng.types.ObjectSet#collect(org.eclipse.emf.common.util.EList)
	 * @generated
	 */
	EOperation getObjectSet__Collect__EList();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.impl.APortSetImpl <em>APort Set</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.impl.APortSetImpl
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getAPortSet()
		 * @generated
		 */
		EClass APORT_SET = eINSTANCE.getAPortSet();

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
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation EXTRA_DATA_CONTAINER___ADD_EXTRA_DATA__STRING_STRING_OBJECT_EXTRADATAFORMATTYPE = eINSTANCE.getExtraDataContainer__AddExtraData__String_String_Object_ExtraDataFormatType();

		/**
		 * The meta object literal for the '<em><b>Get Value With Path As</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation EXTRA_DATA_CONTAINER___GET_VALUE_WITH_PATH_AS__ITERABLE_CLASS_OBJECT = eINSTANCE.getExtraDataContainer__GetValueWithPathAs__Iterable_Class_Object();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.impl.ObjectSetImpl <em>Object Set</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.impl.ObjectSetImpl
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getObjectSet()
		 * @generated
		 */
		EClass OBJECT_SET = eINSTANCE.getObjectSet();

		/**
		 * The meta object literal for the '<em><b>Collect</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation OBJECT_SET___COLLECT__ELIST = eINSTANCE.getObjectSet__Collect__EList();

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
