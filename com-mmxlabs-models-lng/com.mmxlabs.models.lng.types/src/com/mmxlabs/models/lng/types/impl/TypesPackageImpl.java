/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.types.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.models.lng.types.ITimezoneProvider;
import com.mmxlabs.models.lng.types.ObjectSet;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.TypesFactory;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import java.lang.Iterable;

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
	private EClass vesselAssignmentTypeEClass = null;

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
	private EEnum cargoDeliveryTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum volumeUnitsEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum timePeriodEEnum = null;

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
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getVesselAssignmentType() {
		return vesselAssignmentTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getObjectSet() {
		return objectSetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
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
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getCargoDeliveryType() {
		return cargoDeliveryTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getVolumeUnits() {
		return volumeUnitsEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getTimePeriod() {
		return timePeriodEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
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

		vesselAssignmentTypeEClass = createEClass(VESSEL_ASSIGNMENT_TYPE);

		// Create enums
		portCapabilityEEnum = createEEnum(PORT_CAPABILITY);
		cargoDeliveryTypeEEnum = createEEnum(CARGO_DELIVERY_TYPE);
		volumeUnitsEEnum = createEEnum(VOLUME_UNITS);
		timePeriodEEnum = createEEnum(TIME_PERIOD);

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

		initEClass(vesselAssignmentTypeEClass, VesselAssignmentType.class, "VesselAssignmentType", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		// Initialize enums and add enum literals
		initEEnum(portCapabilityEEnum, PortCapability.class, "PortCapability");
		addEEnumLiteral(portCapabilityEEnum, PortCapability.LOAD);
		addEEnumLiteral(portCapabilityEEnum, PortCapability.DISCHARGE);
		addEEnumLiteral(portCapabilityEEnum, PortCapability.DRYDOCK);
		addEEnumLiteral(portCapabilityEEnum, PortCapability.MAINTENANCE);
		addEEnumLiteral(portCapabilityEEnum, PortCapability.TRANSFER);

		initEEnum(cargoDeliveryTypeEEnum, CargoDeliveryType.class, "CargoDeliveryType");
		addEEnumLiteral(cargoDeliveryTypeEEnum, CargoDeliveryType.ANY);
		addEEnumLiteral(cargoDeliveryTypeEEnum, CargoDeliveryType.SHIPPED);
		addEEnumLiteral(cargoDeliveryTypeEEnum, CargoDeliveryType.NOT_SHIPPED);

		initEEnum(volumeUnitsEEnum, VolumeUnits.class, "VolumeUnits");
		addEEnumLiteral(volumeUnitsEEnum, VolumeUnits.M3);
		addEEnumLiteral(volumeUnitsEEnum, VolumeUnits.MMBTU);

		initEEnum(timePeriodEEnum, TimePeriod.class, "TimePeriod");
		addEEnumLiteral(timePeriodEEnum, TimePeriod.HOURS);
		addEEnumLiteral(timePeriodEEnum, TimePeriod.DAYS);
		addEEnumLiteral(timePeriodEEnum, TimePeriod.MONTHS);

		// Initialize data types
		initEDataType(iterableEDataType, Iterable.class, "Iterable", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //TypesPackageImpl
