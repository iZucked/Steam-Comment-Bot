/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.types.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.models.lng.types.ExtraData;
import com.mmxlabs.models.lng.types.ExtraDataContainer;
import com.mmxlabs.models.lng.types.ExtraDataFormatType;
import com.mmxlabs.models.lng.types.ITimezoneProvider;
import com.mmxlabs.models.lng.types.ObjectSet;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.TypesFactory;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class TypesPackageImpl extends EPackageImpl implements TypesPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass aPortSetEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass aVesselSetEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iTimezoneProviderEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass extraDataEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass extraDataContainerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass objectSetEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum portCapabilityEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum extraDataFormatTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum cargoDeliveryTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType iterableEDataType = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see com.mmxlabs.models.lng.types.TypesPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private TypesPackageImpl() {
		super(eNS_URI, TypesFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link TypesPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static TypesPackage init() {
		if (isInited)
			return (TypesPackage) EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);

		// Obtain or create and register package
		TypesPackageImpl theTypesPackage = (TypesPackageImpl) (EPackage.Registry.INSTANCE.get(eNS_URI) instanceof TypesPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new TypesPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		MMXCorePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theTypesPackage.createPackageContents();

		// Initialize created meta-data
		theTypesPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theTypesPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(TypesPackage.eNS_URI, theTypesPackage);
		return theTypesPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAPortSet() {
		return aPortSetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAVesselSet() {
		return aVesselSetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getITimezoneProvider() {
		return iTimezoneProviderEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getITimezoneProvider__GetTimeZone__EAttribute() {
		return iTimezoneProviderEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getExtraData() {
		return extraDataEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExtraData_Key() {
		return (EAttribute) extraDataEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExtraData_Name() {
		return (EAttribute) extraDataEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExtraData_Value() {
		return (EAttribute) extraDataEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExtraData_Format() {
		return (EAttribute) extraDataEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExtraData_FormatType() {
		return (EAttribute) extraDataEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getExtraData__GetValueAs__Class() {
		return extraDataEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getExtraData__FormatValue() {
		return extraDataEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getExtraDataContainer() {
		return extraDataContainerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getExtraDataContainer_ExtraData() {
		return (EReference) extraDataContainerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getExtraDataContainer__GetDataWithPath__Iterable() {
		return extraDataContainerEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getExtraDataContainer__GetDataWithKey__String() {
		return extraDataContainerEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getExtraDataContainer__AddExtraData__String_String() {
		return extraDataContainerEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 5.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getExtraDataContainer__AddExtraData__String_String_Object_ExtraDataFormatType() {
		return extraDataContainerEClass.getEOperations().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getExtraDataContainer__GetValueWithPathAs__Iterable_Class_Object() {
		return extraDataContainerEClass.getEOperations().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 5.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getObjectSet() {
		return objectSetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 5.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getObjectSet__Collect__EList() {
		return objectSetEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getPortCapability() {
		return portCapabilityEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getExtraDataFormatType() {
		return extraDataFormatTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.1
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getCargoDeliveryType() {
		return cargoDeliveryTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getIterable() {
		return iterableEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TypesFactory getTypesFactory() {
		return (TypesFactory) getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated)
			return;
		isCreated = true;

		// Create classes and their features
		objectSetEClass = createEClass(OBJECT_SET);
		createEOperation(objectSetEClass, OBJECT_SET___COLLECT__ELIST);

		aPortSetEClass = createEClass(APORT_SET);

		aVesselSetEClass = createEClass(AVESSEL_SET);

		iTimezoneProviderEClass = createEClass(ITIMEZONE_PROVIDER);
		createEOperation(iTimezoneProviderEClass, ITIMEZONE_PROVIDER___GET_TIME_ZONE__EATTRIBUTE);

		extraDataEClass = createEClass(EXTRA_DATA);
		createEAttribute(extraDataEClass, EXTRA_DATA__KEY);
		createEAttribute(extraDataEClass, EXTRA_DATA__NAME);
		createEAttribute(extraDataEClass, EXTRA_DATA__VALUE);
		createEAttribute(extraDataEClass, EXTRA_DATA__FORMAT);
		createEAttribute(extraDataEClass, EXTRA_DATA__FORMAT_TYPE);
		createEOperation(extraDataEClass, EXTRA_DATA___GET_VALUE_AS__CLASS);
		createEOperation(extraDataEClass, EXTRA_DATA___FORMAT_VALUE);

		extraDataContainerEClass = createEClass(EXTRA_DATA_CONTAINER);
		createEReference(extraDataContainerEClass, EXTRA_DATA_CONTAINER__EXTRA_DATA);
		createEOperation(extraDataContainerEClass, EXTRA_DATA_CONTAINER___GET_DATA_WITH_PATH__ITERABLE);
		createEOperation(extraDataContainerEClass, EXTRA_DATA_CONTAINER___GET_DATA_WITH_KEY__STRING);
		createEOperation(extraDataContainerEClass, EXTRA_DATA_CONTAINER___ADD_EXTRA_DATA__STRING_STRING);
		createEOperation(extraDataContainerEClass, EXTRA_DATA_CONTAINER___ADD_EXTRA_DATA__STRING_STRING_OBJECT_EXTRADATAFORMATTYPE);
		createEOperation(extraDataContainerEClass, EXTRA_DATA_CONTAINER___GET_VALUE_WITH_PATH_AS__ITERABLE_CLASS_OBJECT);

		// Create enums
		portCapabilityEEnum = createEEnum(PORT_CAPABILITY);
		extraDataFormatTypeEEnum = createEEnum(EXTRA_DATA_FORMAT_TYPE);
		cargoDeliveryTypeEEnum = createEEnum(CARGO_DELIVERY_TYPE);

		// Create data types
		iterableEDataType = createEDataType(ITERABLE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized)
			return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		MMXCorePackage theMMXCorePackage = (MMXCorePackage) EPackage.Registry.INSTANCE.getEPackage(MMXCorePackage.eNS_URI);

		// Create type parameters
		ETypeParameter objectSetEClass_T = addETypeParameter(objectSetEClass, "T");
		ETypeParameter objectSetEClass_U = addETypeParameter(objectSetEClass, "U");
		ETypeParameter aPortSetEClass_U = addETypeParameter(aPortSetEClass, "U");
		ETypeParameter aVesselSetEClass_U = addETypeParameter(aVesselSetEClass, "U");
		addETypeParameter(iterableEDataType, "T");

		// Set bounds for type parameters
		EGenericType g1 = createEGenericType(this.getObjectSet());
		EGenericType g2 = createEGenericType(objectSetEClass_T);
		g1.getETypeArguments().add(g2);
		g2 = createEGenericType(objectSetEClass_U);
		g1.getETypeArguments().add(g2);
		objectSetEClass_T.getEBounds().add(g1);

		// Add supertypes to classes
		objectSetEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		objectSetEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		g1 = createEGenericType(this.getObjectSet());
		g2 = createEGenericType(this.getAPortSet());
		g1.getETypeArguments().add(g2);
		EGenericType g3 = createEGenericType(aPortSetEClass_U);
		g2.getETypeArguments().add(g3);
		g2 = createEGenericType(aPortSetEClass_U);
		g1.getETypeArguments().add(g2);
		aPortSetEClass.getEGenericSuperTypes().add(g1);
		g1 = createEGenericType(this.getObjectSet());
		g2 = createEGenericType(this.getAVesselSet());
		g1.getETypeArguments().add(g2);
		g3 = createEGenericType(aVesselSetEClass_U);
		g2.getETypeArguments().add(g3);
		g2 = createEGenericType(aVesselSetEClass_U);
		g1.getETypeArguments().add(g2);
		aVesselSetEClass.getEGenericSuperTypes().add(g1);
		extraDataEClass.getESuperTypes().add(this.getExtraDataContainer());

		// Initialize classes, features, and operations; add parameters
		initEClass(objectSetEClass, ObjectSet.class, "ObjectSet", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		EOperation op = initEOperation(getObjectSet__Collect__EList(), null, "collect", 0, -1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(objectSetEClass_T);
		addEParameter(op, g1, "marked", 0, -1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(objectSetEClass_U);
		initEOperation(op, g1);

		initEClass(aPortSetEClass, APortSet.class, "APortSet", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(aVesselSetEClass, AVesselSet.class, "AVesselSet", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(iTimezoneProviderEClass, ITimezoneProvider.class, "ITimezoneProvider", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		op = initEOperation(getITimezoneProvider__GetTimeZone__EAttribute(), ecorePackage.getEString(), "getTimeZone", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEAttribute(), "attribute", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(extraDataEClass, ExtraData.class, "ExtraData", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getExtraData_Key(), ecorePackage.getEString(), "key", null, 1, 1, ExtraData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEAttribute(getExtraData_Name(), ecorePackage.getEString(), "name", null, 1, 1, ExtraData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEAttribute(getExtraData_Value(), ecorePackage.getEJavaObject(), "value", null, 1, 1, ExtraData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getExtraData_Format(), ecorePackage.getEString(), "format", null, 1, 1, ExtraData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getExtraData_FormatType(), this.getExtraDataFormatType(), "formatType", "AUTO", 1, 1, ExtraData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = initEOperation(getExtraData__GetValueAs__Class(), null, "getValueAs", 1, 1, IS_UNIQUE, IS_ORDERED);
		ETypeParameter t1 = addETypeParameter(op, "T");
		g1 = createEGenericType(ecorePackage.getEJavaClass());
		g2 = createEGenericType(t1);
		g1.getETypeArguments().add(g2);
		addEParameter(op, g1, "clazz", 1, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(t1);
		initEOperation(op, g1);

		initEOperation(getExtraData__FormatValue(), ecorePackage.getEString(), "formatValue", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(extraDataContainerEClass, ExtraDataContainer.class, "ExtraDataContainer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getExtraDataContainer_ExtraData(), this.getExtraData(), null, "extraData", null, 0, -1, ExtraDataContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = initEOperation(getExtraDataContainer__GetDataWithPath__Iterable(), this.getExtraData(), "getDataWithPath", 1, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(this.getIterable());
		g2 = createEGenericType(ecorePackage.getEString());
		g1.getETypeArguments().add(g2);
		addEParameter(op, g1, "keys", 1, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getExtraDataContainer__GetDataWithKey__String(), this.getExtraData(), "getDataWithKey", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "key", 1, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getExtraDataContainer__AddExtraData__String_String(), this.getExtraData(), "addExtraData", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "key", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "name", 1, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getExtraDataContainer__AddExtraData__String_String_Object_ExtraDataFormatType(), this.getExtraData(), "addExtraData", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "key", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "name", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEJavaObject(), "value", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getExtraDataFormatType(), "format", 1, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getExtraDataContainer__GetValueWithPathAs__Iterable_Class_Object(), null, "getValueWithPathAs", 1, 1, IS_UNIQUE, IS_ORDERED);
		t1 = addETypeParameter(op, "T");
		g1 = createEGenericType(this.getIterable());
		g2 = createEGenericType(ecorePackage.getEString());
		g1.getETypeArguments().add(g2);
		addEParameter(op, g1, "path", 1, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(ecorePackage.getEJavaClass());
		g2 = createEGenericType(t1);
		g1.getETypeArguments().add(g2);
		addEParameter(op, g1, "clazz", 1, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(t1);
		addEParameter(op, g1, "defaultValue", 1, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(t1);
		initEOperation(op, g1);

		// Initialize enums and add enum literals
		initEEnum(portCapabilityEEnum, PortCapability.class, "PortCapability");
		addEEnumLiteral(portCapabilityEEnum, PortCapability.LOAD);
		addEEnumLiteral(portCapabilityEEnum, PortCapability.DISCHARGE);
		addEEnumLiteral(portCapabilityEEnum, PortCapability.DRYDOCK);
		addEEnumLiteral(portCapabilityEEnum, PortCapability.MAINTENANCE);

		initEEnum(extraDataFormatTypeEEnum, ExtraDataFormatType.class, "ExtraDataFormatType");
		addEEnumLiteral(extraDataFormatTypeEEnum, ExtraDataFormatType.AUTO);
		addEEnumLiteral(extraDataFormatTypeEEnum, ExtraDataFormatType.INTEGER);
		addEEnumLiteral(extraDataFormatTypeEEnum, ExtraDataFormatType.DURATION);
		addEEnumLiteral(extraDataFormatTypeEEnum, ExtraDataFormatType.CURRENCY);
		addEEnumLiteral(extraDataFormatTypeEEnum, ExtraDataFormatType.DATE);
		addEEnumLiteral(extraDataFormatTypeEEnum, ExtraDataFormatType.STRING_FORMAT);

		initEEnum(cargoDeliveryTypeEEnum, CargoDeliveryType.class, "CargoDeliveryType");
		addEEnumLiteral(cargoDeliveryTypeEEnum, CargoDeliveryType.ANY);
		addEEnumLiteral(cargoDeliveryTypeEEnum, CargoDeliveryType.SHIPPED);
		addEEnumLiteral(cargoDeliveryTypeEEnum, CargoDeliveryType.DELIVERED);

		// Initialize data types
		initEDataType(iterableEDataType, Iterable.class, "Iterable", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //TypesPackageImpl
