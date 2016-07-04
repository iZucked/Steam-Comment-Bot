/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.types;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;

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
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
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
	 * The number of structural features of the '<em>Object Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_SET_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_SET___GET_UNSET_VALUE__ESTRUCTURALFEATURE_1 = MMXCorePackage.UUID_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_SET___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE_1 = MMXCorePackage.UUID_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

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
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
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
	int APORT_SET___GET_UNSET_VALUE__ESTRUCTURALFEATURE_1 = OBJECT_SET___GET_UNSET_VALUE__ESTRUCTURALFEATURE_1;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int APORT_SET___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE_1 = OBJECT_SET___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE_1;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
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
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
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
	int AVESSEL_SET___GET_UNSET_VALUE__ESTRUCTURALFEATURE_1 = OBJECT_SET___GET_UNSET_VALUE__ESTRUCTURALFEATURE_1;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVESSEL_SET___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE_1 = OBJECT_SET___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE_1;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.VesselAssignmentType <em>Vessel Assignment Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.VesselAssignmentType
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getVesselAssignmentType()
	 * @generated
	 */
	int VESSEL_ASSIGNMENT_TYPE = 4;

	/**
	 * The number of structural features of the '<em>Vessel Assignment Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_ASSIGNMENT_TYPE_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Vessel Assignment Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_ASSIGNMENT_TYPE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.PortCapability <em>Port Capability</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.PortCapability
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getPortCapability()
	 * @generated
	 */
	int PORT_CAPABILITY = 5;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.CargoDeliveryType <em>Cargo Delivery Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.CargoDeliveryType
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getCargoDeliveryType()
	 * @generated
	 */
	int CARGO_DELIVERY_TYPE = 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.VolumeUnits <em>Volume Units</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.VolumeUnits
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getVolumeUnits()
	 * @generated
	 */
	int VOLUME_UNITS = 7;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.types.TimePeriod <em>Time Period</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.types.TimePeriod
	 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getTimePeriod()
	 * @generated
	 */
	int TIME_PERIOD = 8;

	/**
	 * The meta object id for the '<em>Iterable</em>' data type.
	 * <!-- begin-user-doc -->
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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.types.VesselAssignmentType <em>Vessel Assignment Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vessel Assignment Type</em>'.
	 * @see com.mmxlabs.models.lng.types.VesselAssignmentType
	 * @generated
	 */
	EClass getVesselAssignmentType();

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
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.types.CargoDeliveryType <em>Cargo Delivery Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Cargo Delivery Type</em>'.
	 * @see com.mmxlabs.models.lng.types.CargoDeliveryType
	 * @generated
	 */
	EEnum getCargoDeliveryType();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.types.VolumeUnits <em>Volume Units</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Volume Units</em>'.
	 * @see com.mmxlabs.models.lng.types.VolumeUnits
	 * @generated
	 */
	EEnum getVolumeUnits();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.types.TimePeriod <em>Time Period</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Time Period</em>'.
	 * @see com.mmxlabs.models.lng.types.TimePeriod
	 * @generated
	 */
	EEnum getTimePeriod();

	/**
	 * Returns the meta object for data type '{@link java.lang.Iterable <em>Iterable</em>}'.
	 * <!-- begin-user-doc -->
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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.VesselAssignmentType <em>Vessel Assignment Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.VesselAssignmentType
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getVesselAssignmentType()
		 * @generated
		 */
		EClass VESSEL_ASSIGNMENT_TYPE = eINSTANCE.getVesselAssignmentType();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.CargoDeliveryType <em>Cargo Delivery Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.CargoDeliveryType
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getCargoDeliveryType()
		 * @generated
		 */
		EEnum CARGO_DELIVERY_TYPE = eINSTANCE.getCargoDeliveryType();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.VolumeUnits <em>Volume Units</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.VolumeUnits
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getVolumeUnits()
		 * @generated
		 */
		EEnum VOLUME_UNITS = eINSTANCE.getVolumeUnits();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.types.TimePeriod <em>Time Period</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.types.TimePeriod
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getTimePeriod()
		 * @generated
		 */
		EEnum TIME_PERIOD = eINSTANCE.getTimePeriod();

		/**
		 * The meta object literal for the '<em>Iterable</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.Iterable
		 * @see com.mmxlabs.models.lng.types.impl.TypesPackageImpl#getIterable()
		 * @generated
		 */
		EDataType ITERABLE = eINSTANCE.getIterable();

	}

} //TypesPackage
