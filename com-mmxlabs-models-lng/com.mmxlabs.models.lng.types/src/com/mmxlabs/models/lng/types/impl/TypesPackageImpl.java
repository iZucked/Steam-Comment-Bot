/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.types.impl;

import com.mmxlabs.models.lng.types.ABaseFuel;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.mmxlabs.models.lng.types.ACargo;
import com.mmxlabs.models.lng.types.AContract;
import com.mmxlabs.models.lng.types.AFleetVessel;
import com.mmxlabs.models.lng.types.AIndex;
import com.mmxlabs.models.lng.types.ALNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.types.ALegalEntity;
import com.mmxlabs.models.lng.types.AOptimisationSettings;
import com.mmxlabs.models.lng.types.APort;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.APurchaseContract;
import com.mmxlabs.models.lng.types.ARoute;
import com.mmxlabs.models.lng.types.ASalesContract;
import com.mmxlabs.models.lng.types.ASlot;
import com.mmxlabs.models.lng.types.ASpotMarket;
import com.mmxlabs.models.lng.types.AVessel;
import com.mmxlabs.models.lng.types.AVesselClass;
import com.mmxlabs.models.lng.types.AVesselEvent;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.models.lng.types.ExtraData;
import com.mmxlabs.models.lng.types.ExtraDataContainer;
import com.mmxlabs.models.lng.types.ExtraDataFormatType;
import com.mmxlabs.models.lng.types.ITimezoneProvider;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.TypesFactory;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import java.io.Serializable;
import java.lang.Iterable;
import org.eclipse.emf.ecore.EAttribute;

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
	private EClass aPortEClass = null;

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
	private EClass aRouteEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass aVesselEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass aFleetVesselEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass aVesselClassEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass aVesselEventEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass aContractEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass aLegalEntityEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass aIndexEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass aCargoEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass aSlotEClass = null;

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
	private EClass aBaseFuelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass aSpotMarketEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass aOptimisationSettingsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass aPurchaseContractEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass aSalesContractEClass = null;

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
	private EClass alngPriceCalculatorParametersEClass = null;

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
	private EDataType serializableObjectEDataType = null;

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
			return (TypesPackage) EPackage.Registry.INSTANCE
					.getEPackage(TypesPackage.eNS_URI);

		// Obtain or create and register package
		TypesPackageImpl theTypesPackage = (TypesPackageImpl) (EPackage.Registry.INSTANCE
				.get(eNS_URI) instanceof TypesPackageImpl ? EPackage.Registry.INSTANCE
				.get(eNS_URI) : new TypesPackageImpl());

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
	public EClass getAPort() {
		return aPortEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getAPort__Collect__EList() {
		return aPortEClass.getEOperations().get(0);
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
	public EOperation getAPortSet__Collect__EList() {
		return aPortSetEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getARoute() {
		return aRouteEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAVessel() {
		return aVesselEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getAVessel__Collect__EList() {
		return aVesselEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAFleetVessel() {
		return aFleetVesselEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAVesselClass() {
		return aVesselClassEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAVesselEvent() {
		return aVesselEventEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAContract() {
		return aContractEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getALegalEntity() {
		return aLegalEntityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAIndex() {
		return aIndexEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getACargo() {
		return aCargoEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getASlot() {
		return aSlotEClass;
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
	public EOperation getAVesselSet__Collect__EList() {
		return aVesselSetEClass.getEOperations().get(0);
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
	public EClass getABaseFuel() {
		return aBaseFuelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getASpotMarket() {
		return aSpotMarketEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAOptimisationSettings() {
		return aOptimisationSettingsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAPurchaseContract() {
		return aPurchaseContractEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getASalesContract() {
		return aSalesContractEClass;
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
		return (EReference) extraDataContainerEClass.getEStructuralFeatures()
				.get(0);
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
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getExtraDataContainer__AddExtraData__String_String_Serializable_ExtraDataFormatType() {
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
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getALNGPriceCalculatorParameters() {
		return alngPriceCalculatorParametersEClass;
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
	public EDataType getSerializableObject() {
		return serializableObjectEDataType;
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
		aPortEClass = createEClass(APORT);
		createEOperation(aPortEClass, APORT___COLLECT__ELIST);

		aPortSetEClass = createEClass(APORT_SET);
		createEOperation(aPortSetEClass, APORT_SET___COLLECT__ELIST);

		aRouteEClass = createEClass(AROUTE);

		aVesselEClass = createEClass(AVESSEL);
		createEOperation(aVesselEClass, AVESSEL___COLLECT__ELIST);

		aFleetVesselEClass = createEClass(AFLEET_VESSEL);

		aVesselClassEClass = createEClass(AVESSEL_CLASS);

		aVesselEventEClass = createEClass(AVESSEL_EVENT);

		aContractEClass = createEClass(ACONTRACT);

		aLegalEntityEClass = createEClass(ALEGAL_ENTITY);

		aIndexEClass = createEClass(AINDEX);

		aCargoEClass = createEClass(ACARGO);

		aSlotEClass = createEClass(ASLOT);

		aVesselSetEClass = createEClass(AVESSEL_SET);
		createEOperation(aVesselSetEClass, AVESSEL_SET___COLLECT__ELIST);

		iTimezoneProviderEClass = createEClass(ITIMEZONE_PROVIDER);
		createEOperation(iTimezoneProviderEClass,
				ITIMEZONE_PROVIDER___GET_TIME_ZONE__EATTRIBUTE);

		aBaseFuelEClass = createEClass(ABASE_FUEL);

		aSpotMarketEClass = createEClass(ASPOT_MARKET);

		aOptimisationSettingsEClass = createEClass(AOPTIMISATION_SETTINGS);

		aPurchaseContractEClass = createEClass(APURCHASE_CONTRACT);

		aSalesContractEClass = createEClass(ASALES_CONTRACT);

		extraDataEClass = createEClass(EXTRA_DATA);
		createEAttribute(extraDataEClass, EXTRA_DATA__KEY);
		createEAttribute(extraDataEClass, EXTRA_DATA__NAME);
		createEAttribute(extraDataEClass, EXTRA_DATA__VALUE);
		createEAttribute(extraDataEClass, EXTRA_DATA__FORMAT);
		createEAttribute(extraDataEClass, EXTRA_DATA__FORMAT_TYPE);
		createEOperation(extraDataEClass, EXTRA_DATA___GET_VALUE_AS__CLASS);
		createEOperation(extraDataEClass, EXTRA_DATA___FORMAT_VALUE);

		extraDataContainerEClass = createEClass(EXTRA_DATA_CONTAINER);
		createEReference(extraDataContainerEClass,
				EXTRA_DATA_CONTAINER__EXTRA_DATA);
		createEOperation(extraDataContainerEClass,
				EXTRA_DATA_CONTAINER___GET_DATA_WITH_PATH__ITERABLE);
		createEOperation(extraDataContainerEClass,
				EXTRA_DATA_CONTAINER___GET_DATA_WITH_KEY__STRING);
		createEOperation(extraDataContainerEClass,
				EXTRA_DATA_CONTAINER___ADD_EXTRA_DATA__STRING_STRING);
		createEOperation(
				extraDataContainerEClass,
				EXTRA_DATA_CONTAINER___ADD_EXTRA_DATA__STRING_STRING_SERIALIZABLE_EXTRADATAFORMATTYPE);
		createEOperation(extraDataContainerEClass,
				EXTRA_DATA_CONTAINER___GET_VALUE_WITH_PATH_AS__ITERABLE_CLASS_OBJECT);

		alngPriceCalculatorParametersEClass = createEClass(ALNG_PRICE_CALCULATOR_PARAMETERS);

		// Create enums
		portCapabilityEEnum = createEEnum(PORT_CAPABILITY);
		extraDataFormatTypeEEnum = createEEnum(EXTRA_DATA_FORMAT_TYPE);
		cargoDeliveryTypeEEnum = createEEnum(CARGO_DELIVERY_TYPE);

		// Create data types
		serializableObjectEDataType = createEDataType(SERIALIZABLE_OBJECT);
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
		MMXCorePackage theMMXCorePackage = (MMXCorePackage) EPackage.Registry.INSTANCE
				.getEPackage(MMXCorePackage.eNS_URI);

		// Create type parameters
		addETypeParameter(iterableEDataType, "T");

		// Set bounds for type parameters

		// Add supertypes to classes
		aPortEClass.getESuperTypes().add(this.getAPortSet());
		aPortSetEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		aPortSetEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		aRouteEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		aRouteEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		aVesselEClass.getESuperTypes().add(this.getAVesselSet());
		aFleetVesselEClass.getESuperTypes().add(
				theMMXCorePackage.getUUIDObject());
		aVesselClassEClass.getESuperTypes().add(this.getAVesselSet());
		aVesselEventEClass.getESuperTypes().add(
				theMMXCorePackage.getUUIDObject());
		aVesselEventEClass.getESuperTypes().add(
				theMMXCorePackage.getNamedObject());
		aContractEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		aContractEClass.getESuperTypes()
				.add(theMMXCorePackage.getNamedObject());
		aLegalEntityEClass.getESuperTypes().add(
				theMMXCorePackage.getUUIDObject());
		aLegalEntityEClass.getESuperTypes().add(
				theMMXCorePackage.getNamedObject());
		aIndexEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		aIndexEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		aCargoEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		aCargoEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		aSlotEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		aSlotEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		aVesselSetEClass.getESuperTypes()
				.add(theMMXCorePackage.getUUIDObject());
		aVesselSetEClass.getESuperTypes().add(
				theMMXCorePackage.getNamedObject());
		aBaseFuelEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		aBaseFuelEClass.getESuperTypes()
				.add(theMMXCorePackage.getNamedObject());
		aSpotMarketEClass.getESuperTypes().add(
				theMMXCorePackage.getUUIDObject());
		aSpotMarketEClass.getESuperTypes().add(
				theMMXCorePackage.getNamedObject());
		aOptimisationSettingsEClass.getESuperTypes().add(
				theMMXCorePackage.getUUIDObject());
		aOptimisationSettingsEClass.getESuperTypes().add(
				theMMXCorePackage.getNamedObject());
		aPurchaseContractEClass.getESuperTypes().add(this.getAContract());
		aSalesContractEClass.getESuperTypes().add(this.getAContract());
		extraDataEClass.getESuperTypes().add(this.getExtraDataContainer());
		alngPriceCalculatorParametersEClass.getESuperTypes().add(
				theMMXCorePackage.getUUIDObject());
		alngPriceCalculatorParametersEClass.getESuperTypes().add(
				theMMXCorePackage.getNamedObject());

		// Initialize classes, features, and operations; add parameters
		initEClass(aPortEClass, APort.class, "APort", IS_ABSTRACT,
				!IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		EOperation op = initEOperation(getAPort__Collect__EList(),
				this.getAPort(), "collect", 0, -1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getAPortSet(), "marked", 0, -1, IS_UNIQUE,
				IS_ORDERED);

		initEClass(aPortSetEClass, APortSet.class, "APortSet", IS_ABSTRACT,
				!IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		op = initEOperation(getAPortSet__Collect__EList(), this.getAPort(),
				"collect", 0, -1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getAPortSet(), "marked", 0, -1, IS_UNIQUE,
				IS_ORDERED);

		initEClass(aRouteEClass, ARoute.class, "ARoute", IS_ABSTRACT,
				!IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(aVesselEClass, AVessel.class, "AVessel", IS_ABSTRACT,
				!IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		op = initEOperation(getAVessel__Collect__EList(), this.getAVessel(),
				"collect", 0, -1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getAVesselSet(), "marked", 0, -1, IS_UNIQUE,
				IS_ORDERED);

		initEClass(aFleetVesselEClass, AFleetVessel.class, "AFleetVessel",
				IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(aVesselClassEClass, AVesselClass.class, "AVesselClass",
				IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(aVesselEventEClass, AVesselEvent.class, "AVesselEvent",
				IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(aContractEClass, AContract.class, "AContract", IS_ABSTRACT,
				!IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(aLegalEntityEClass, ALegalEntity.class, "ALegalEntity",
				IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(aIndexEClass, AIndex.class, "AIndex", IS_ABSTRACT,
				!IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(aCargoEClass, ACargo.class, "ACargo", IS_ABSTRACT,
				!IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(aSlotEClass, ASlot.class, "ASlot", IS_ABSTRACT,
				!IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(aVesselSetEClass, AVesselSet.class, "AVesselSet",
				!IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		op = initEOperation(getAVesselSet__Collect__EList(), this.getAVessel(),
				"collect", 0, -1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getAVesselSet(), "marked", 0, -1, IS_UNIQUE,
				IS_ORDERED);

		initEClass(iTimezoneProviderEClass, ITimezoneProvider.class,
				"ITimezoneProvider", IS_ABSTRACT, IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);

		op = initEOperation(getITimezoneProvider__GetTimeZone__EAttribute(),
				ecorePackage.getEString(), "getTimeZone", 1, 1, IS_UNIQUE,
				IS_ORDERED);
		addEParameter(op, ecorePackage.getEAttribute(), "attribute", 1, 1,
				IS_UNIQUE, IS_ORDERED);

		initEClass(aBaseFuelEClass, ABaseFuel.class, "ABaseFuel", !IS_ABSTRACT,
				!IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(aSpotMarketEClass, ASpotMarket.class, "ASpotMarket",
				IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(aOptimisationSettingsEClass, AOptimisationSettings.class,
				"AOptimisationSettings", IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);

		initEClass(aPurchaseContractEClass, APurchaseContract.class,
				"APurchaseContract", IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);

		initEClass(aSalesContractEClass, ASalesContract.class,
				"ASalesContract", IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);

		initEClass(extraDataEClass, ExtraData.class, "ExtraData", !IS_ABSTRACT,
				!IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getExtraData_Key(), ecorePackage.getEString(), "key",
				null, 1, 1, ExtraData.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEAttribute(getExtraData_Name(), ecorePackage.getEString(), "name",
				null, 1, 1, ExtraData.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEAttribute(getExtraData_Value(), this.getSerializableObject(),
				"value", null, 1, 1, ExtraData.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getExtraData_Format(), ecorePackage.getEString(),
				"format", null, 1, 1, ExtraData.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getExtraData_FormatType(),
				this.getExtraDataFormatType(), "formatType", "AUTO", 1, 1,
				ExtraData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = initEOperation(getExtraData__GetValueAs__Class(), null,
				"getValueAs", 1, 1, IS_UNIQUE, IS_ORDERED);
		ETypeParameter t1 = addETypeParameter(op, "T");
		EGenericType g1 = createEGenericType(ecorePackage.getEJavaClass());
		EGenericType g2 = createEGenericType(t1);
		g1.getETypeArguments().add(g2);
		addEParameter(op, g1, "clazz", 1, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(t1);
		initEOperation(op, g1);

		initEOperation(getExtraData__FormatValue(), ecorePackage.getEString(),
				"formatValue", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(extraDataContainerEClass, ExtraDataContainer.class,
				"ExtraDataContainer", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getExtraDataContainer_ExtraData(), this.getExtraData(),
				null, "extraData", null, 0, -1, ExtraDataContainer.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);

		op = initEOperation(getExtraDataContainer__GetDataWithPath__Iterable(),
				this.getExtraData(), "getDataWithPath", 1, 1, IS_UNIQUE,
				IS_ORDERED);
		g1 = createEGenericType(this.getIterable());
		g2 = createEGenericType(ecorePackage.getEString());
		g1.getETypeArguments().add(g2);
		addEParameter(op, g1, "keys", 1, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getExtraDataContainer__GetDataWithKey__String(),
				this.getExtraData(), "getDataWithKey", 1, 1, IS_UNIQUE,
				IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "key", 1, 1, IS_UNIQUE,
				IS_ORDERED);

		op = initEOperation(
				getExtraDataContainer__AddExtraData__String_String(),
				this.getExtraData(), "addExtraData", 1, 1, IS_UNIQUE,
				IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "key", 1, 1, IS_UNIQUE,
				IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "name", 1, 1, IS_UNIQUE,
				IS_ORDERED);

		op = initEOperation(
				getExtraDataContainer__AddExtraData__String_String_Serializable_ExtraDataFormatType(),
				this.getExtraData(), "addExtraData", 1, 1, IS_UNIQUE,
				IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "key", 1, 1, IS_UNIQUE,
				IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "name", 1, 1, IS_UNIQUE,
				IS_ORDERED);
		addEParameter(op, this.getSerializableObject(), "value", 1, 1,
				IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getExtraDataFormatType(), "format", 1, 1,
				IS_UNIQUE, IS_ORDERED);

		op = initEOperation(
				getExtraDataContainer__GetValueWithPathAs__Iterable_Class_Object(),
				null, "getValueWithPathAs", 1, 1, IS_UNIQUE, IS_ORDERED);
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

		initEClass(alngPriceCalculatorParametersEClass,
				ALNGPriceCalculatorParameters.class,
				"ALNGPriceCalculatorParameters", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);

		// Initialize enums and add enum literals
		initEEnum(portCapabilityEEnum, PortCapability.class, "PortCapability");
		addEEnumLiteral(portCapabilityEEnum, PortCapability.LOAD);
		addEEnumLiteral(portCapabilityEEnum, PortCapability.DISCHARGE);
		addEEnumLiteral(portCapabilityEEnum, PortCapability.DRYDOCK);
		addEEnumLiteral(portCapabilityEEnum, PortCapability.MAINTENANCE);

		initEEnum(extraDataFormatTypeEEnum, ExtraDataFormatType.class,
				"ExtraDataFormatType");
		addEEnumLiteral(extraDataFormatTypeEEnum, ExtraDataFormatType.AUTO);
		addEEnumLiteral(extraDataFormatTypeEEnum, ExtraDataFormatType.INTEGER);
		addEEnumLiteral(extraDataFormatTypeEEnum, ExtraDataFormatType.DURATION);
		addEEnumLiteral(extraDataFormatTypeEEnum, ExtraDataFormatType.CURRENCY);
		addEEnumLiteral(extraDataFormatTypeEEnum, ExtraDataFormatType.DATE);
		addEEnumLiteral(extraDataFormatTypeEEnum,
				ExtraDataFormatType.STRING_FORMAT);

		initEEnum(cargoDeliveryTypeEEnum, CargoDeliveryType.class,
				"CargoDeliveryType");
		addEEnumLiteral(cargoDeliveryTypeEEnum, CargoDeliveryType.ANY);
		addEEnumLiteral(cargoDeliveryTypeEEnum, CargoDeliveryType.SHIPPED);
		addEEnumLiteral(cargoDeliveryTypeEEnum, CargoDeliveryType.DELIVERED);

		// Initialize data types
		initEDataType(serializableObjectEDataType, Serializable.class,
				"SerializableObject", IS_SERIALIZABLE,
				!IS_GENERATED_INSTANCE_CLASS);
		initEDataType(iterableEDataType, Iterable.class, "Iterable",
				IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.mmxlabs.com/mmxcore/1/MMXCore
		createMMXCoreAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http://www.mmxlabs.com/mmxcore/1/MMXCore</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createMMXCoreAnnotations() {
		String source = "http://www.mmxlabs.com/mmxcore/1/MMXCore";
		addAnnotation(aPortEClass, source, new String[] { "generatedType",
				"com.mmxlabs.models.lng.port.Port" });
		addAnnotation(aRouteEClass, source, new String[] { "generatedType",
				"com.mmxlabs.models.lng.port.Route" });
		addAnnotation(aVesselEClass, source, new String[] { "generatedType",
				"com.mmxlabs.models.lng.fleet.Vessel" });
		addAnnotation(aFleetVesselEClass, source, new String[] {
				"generatedType", "com.mmxlabs.models.lng.fleet.Vessel" });
		addAnnotation(aVesselClassEClass, source, new String[] {
				"generatedType", "com.mmxlabs.models.lng.fleet.VesselClass" });
		addAnnotation(aVesselEventEClass, source, new String[] {
				"generatedType", "com.mmxlabs.models.lng.fleet.VesselEvent" });
		addAnnotation(aContractEClass, source, new String[] { "generatedType",
				"com.mmxlabs.models.lng.commercial.Contract" });
		addAnnotation(aLegalEntityEClass, source, new String[] {
				"generatedType",
				"com.mmxlabs.models.lng.commercial.LegalEntity" });
		addAnnotation(aIndexEClass, source, new String[] { "generatedType",
				"com.mmxlabs.models.lng.pricing.Index" });
		addAnnotation(aCargoEClass, source, new String[] { "generatedType",
				"com.mmxlabs.models.lng.cargo.Cargo" });
		addAnnotation(aSlotEClass, source, new String[] { "generatedType",
				"com.mmxlabs.models.lng.cargo.Slot" });
		addAnnotation(aBaseFuelEClass, source, new String[] { "generatedType",
				"com.mmxlabs.models.lng.fleet.BaseFuel" });
		addAnnotation(aPurchaseContractEClass, source, new String[] {
				"generatedType",
				"com.mmxlabs.models.lng.commercial.PurchaseContract" });
		addAnnotation(aSalesContractEClass, source, new String[] {
				"generatedType",
				"com.mmxlabs.models.lng.commercial.SalesContract" });
		addAnnotation(
				alngPriceCalculatorParametersEClass,
				source,
				new String[] { "generatedType",
						"com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters" });
	}

} //TypesPackageImpl
