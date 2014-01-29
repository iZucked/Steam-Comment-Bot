/**
 */
package com.mmxlabs.models.lng.actuals.impl;

import com.mmxlabs.models.lng.actuals.ActualsFactory;
import com.mmxlabs.models.lng.actuals.ActualsModel;
import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.lng.actuals.CargoActuals;
import com.mmxlabs.models.lng.actuals.DischargeActuals;
import com.mmxlabs.models.lng.actuals.LoadActuals;
import com.mmxlabs.models.lng.actuals.SlotActuals;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ActualsPackageImpl extends EPackageImpl implements ActualsPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass actualsModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass slotActualsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass cargoActualsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass loadActualsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dischargeActualsEClass = null;

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
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ActualsPackageImpl() {
		super(eNS_URI, ActualsFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link ActualsPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static ActualsPackage init() {
		if (isInited) return (ActualsPackage)EPackage.Registry.INSTANCE.getEPackage(ActualsPackage.eNS_URI);

		// Obtain or create and register package
		ActualsPackageImpl theActualsPackage = (ActualsPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ActualsPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ActualsPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		CargoPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theActualsPackage.createPackageContents();

		// Initialize created meta-data
		theActualsPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theActualsPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(ActualsPackage.eNS_URI, theActualsPackage);
		return theActualsPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getActualsModel() {
		return actualsModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getActualsModel_CargoActuals() {
		return (EReference)actualsModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSlotActuals() {
		return slotActualsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlotActuals_CV() {
		return (EAttribute)slotActualsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlotActuals_Volume() {
		return (EAttribute)slotActualsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlotActuals_MmBtu() {
		return (EAttribute)slotActualsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlotActuals_PortCharges() {
		return (EAttribute)slotActualsEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSlotActuals_BaseFuelConsumption() {
		return (EAttribute)slotActualsEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSlotActuals_Slot() {
		return (EReference)slotActualsEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCargoActuals() {
		return cargoActualsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCargoActuals_BaseFuelPrice() {
		return (EAttribute)cargoActualsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCargoActuals_Volume() {
		return (EAttribute)cargoActualsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCargoActuals_InsurancePremium() {
		return (EAttribute)cargoActualsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCargoActuals_CrewBonus() {
		return (EAttribute)cargoActualsEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCargoActuals_Actuals() {
		return (EReference)cargoActualsEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getLoadActuals() {
		return loadActualsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDischargeActuals() {
		return dischargeActualsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ActualsFactory getActualsFactory() {
		return (ActualsFactory)getEFactoryInstance();
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
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		actualsModelEClass = createEClass(ACTUALS_MODEL);
		createEReference(actualsModelEClass, ACTUALS_MODEL__CARGO_ACTUALS);

		slotActualsEClass = createEClass(SLOT_ACTUALS);
		createEAttribute(slotActualsEClass, SLOT_ACTUALS__CV);
		createEAttribute(slotActualsEClass, SLOT_ACTUALS__VOLUME);
		createEAttribute(slotActualsEClass, SLOT_ACTUALS__MM_BTU);
		createEAttribute(slotActualsEClass, SLOT_ACTUALS__PORT_CHARGES);
		createEAttribute(slotActualsEClass, SLOT_ACTUALS__BASE_FUEL_CONSUMPTION);
		createEReference(slotActualsEClass, SLOT_ACTUALS__SLOT);

		cargoActualsEClass = createEClass(CARGO_ACTUALS);
		createEAttribute(cargoActualsEClass, CARGO_ACTUALS__BASE_FUEL_PRICE);
		createEAttribute(cargoActualsEClass, CARGO_ACTUALS__VOLUME);
		createEAttribute(cargoActualsEClass, CARGO_ACTUALS__INSURANCE_PREMIUM);
		createEAttribute(cargoActualsEClass, CARGO_ACTUALS__CREW_BONUS);
		createEReference(cargoActualsEClass, CARGO_ACTUALS__ACTUALS);

		loadActualsEClass = createEClass(LOAD_ACTUALS);

		dischargeActualsEClass = createEClass(DISCHARGE_ACTUALS);
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
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		CargoPackage theCargoPackage = (CargoPackage)EPackage.Registry.INSTANCE.getEPackage(CargoPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		loadActualsEClass.getESuperTypes().add(this.getSlotActuals());
		dischargeActualsEClass.getESuperTypes().add(this.getSlotActuals());

		// Initialize classes, features, and operations; add parameters
		initEClass(actualsModelEClass, ActualsModel.class, "ActualsModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getActualsModel_CargoActuals(), this.getCargoActuals(), null, "cargoActuals", null, 0, -1, ActualsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(slotActualsEClass, SlotActuals.class, "SlotActuals", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSlotActuals_CV(), ecorePackage.getEFloat(), "CV", null, 0, 1, SlotActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlotActuals_Volume(), ecorePackage.getEFloat(), "volume", null, 0, 1, SlotActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlotActuals_MmBtu(), ecorePackage.getEInt(), "mmBtu", null, 0, 1, SlotActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlotActuals_PortCharges(), ecorePackage.getEInt(), "portCharges", null, 0, 1, SlotActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlotActuals_BaseFuelConsumption(), ecorePackage.getEInt(), "baseFuelConsumption", null, 0, 1, SlotActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSlotActuals_Slot(), theCargoPackage.getSlot(), null, "slot", null, 0, 1, SlotActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(cargoActualsEClass, CargoActuals.class, "CargoActuals", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCargoActuals_BaseFuelPrice(), ecorePackage.getEFloat(), "baseFuelPrice", null, 0, 1, CargoActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCargoActuals_Volume(), ecorePackage.getEFloat(), "volume", null, 0, 1, CargoActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCargoActuals_InsurancePremium(), ecorePackage.getEInt(), "insurancePremium", null, 0, 1, CargoActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCargoActuals_CrewBonus(), ecorePackage.getEInt(), "crewBonus", null, 0, 1, CargoActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargoActuals_Actuals(), this.getSlotActuals(), null, "actuals", null, 0, -1, CargoActuals.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(loadActualsEClass, LoadActuals.class, "LoadActuals", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(dischargeActualsEClass, DischargeActuals.class, "DischargeActuals", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //ActualsPackageImpl
