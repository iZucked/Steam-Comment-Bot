/**
 */
package com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.mmxlabs.models.datetime.DateTimePackage;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.CargoEditorModelFactory;
import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.CargoEditorModelPackage;
import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow;
import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesTableRoot;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import org.eclipse.emf.ecore.EAttribute;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class CargoEditorModelPackageImpl extends EPackageImpl implements CargoEditorModelPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass tradesRowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass tradesTableRootEClass = null;

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
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.CargoEditorModelPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private CargoEditorModelPackageImpl() {
		super(eNS_URI, CargoEditorModelFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link CargoEditorModelPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static CargoEditorModelPackage init() {
		if (isInited) return (CargoEditorModelPackage)EPackage.Registry.INSTANCE.getEPackage(CargoEditorModelPackage.eNS_URI);

		// Obtain or create and register package
		Object registeredCargoEditorModelPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		CargoEditorModelPackageImpl theCargoEditorModelPackage = registeredCargoEditorModelPackage instanceof CargoEditorModelPackageImpl ? (CargoEditorModelPackageImpl)registeredCargoEditorModelPackage : new CargoEditorModelPackageImpl();

		isInited = true;

		// Initialize simple dependencies
		CommercialPackage.eINSTANCE.eClass();
		DateTimePackage.eINSTANCE.eClass();
		FleetPackage.eINSTANCE.eClass();
		TypesPackage.eINSTANCE.eClass();
		MMXCorePackage.eINSTANCE.eClass();
		PortPackage.eINSTANCE.eClass();
		PricingPackage.eINSTANCE.eClass();
		SchedulePackage.eINSTANCE.eClass();
		SpotMarketsPackage.eINSTANCE.eClass();
		CargoPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theCargoEditorModelPackage.createPackageContents();

		// Initialize created meta-data
		theCargoEditorModelPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theCargoEditorModelPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(CargoEditorModelPackage.eNS_URI, theCargoEditorModelPackage);
		return theCargoEditorModelPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getTradesRow() {
		return tradesRowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTradesRow_LoadSlot() {
		return (EReference)tradesRowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTradesRow_DischargeSlot() {
		return (EReference)tradesRowEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTradesRow_Cargo() {
		return (EReference)tradesRowEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTradesRow_CargoAllocation() {
		return (EReference)tradesRowEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTradesRow_LoadAllocation() {
		return (EReference)tradesRowEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTradesRow_DischargeAllocation() {
		return (EReference)tradesRowEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTradesRow_OpenSlotAllocation() {
		return (EReference)tradesRowEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTradesRow_InputEquivalents() {
		return (EReference)tradesRowEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTradesRow_LoadSlotContractParams() {
		return (EReference)tradesRowEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTradesRow_DischargeSlotContractParams() {
		return (EReference)tradesRowEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTradesRow_PrimaryRecord() {
		return (EAttribute)tradesRowEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTradesRow_MarketAllocation() {
		return (EReference)tradesRowEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTradesRow_LoadTerminalValid() {
		return (EAttribute)tradesRowEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTradesRow_DischargeTerminalValid() {
		return (EAttribute)tradesRowEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTradesRow_Group() {
		return (EAttribute)tradesRowEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getTradesRow__GetAssignableObject() {
		return tradesRowEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getTradesTableRoot() {
		return tradesTableRootEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTradesTableRoot_TradesRows() {
		return (EReference)tradesTableRootEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CargoEditorModelFactory getCargoEditorModelFactory() {
		return (CargoEditorModelFactory)getEFactoryInstance();
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
		tradesRowEClass = createEClass(TRADES_ROW);
		createEReference(tradesRowEClass, TRADES_ROW__LOAD_SLOT);
		createEReference(tradesRowEClass, TRADES_ROW__DISCHARGE_SLOT);
		createEReference(tradesRowEClass, TRADES_ROW__CARGO);
		createEReference(tradesRowEClass, TRADES_ROW__CARGO_ALLOCATION);
		createEReference(tradesRowEClass, TRADES_ROW__LOAD_ALLOCATION);
		createEReference(tradesRowEClass, TRADES_ROW__DISCHARGE_ALLOCATION);
		createEReference(tradesRowEClass, TRADES_ROW__OPEN_SLOT_ALLOCATION);
		createEReference(tradesRowEClass, TRADES_ROW__INPUT_EQUIVALENTS);
		createEReference(tradesRowEClass, TRADES_ROW__LOAD_SLOT_CONTRACT_PARAMS);
		createEAttribute(tradesRowEClass, TRADES_ROW__PRIMARY_RECORD);
		createEReference(tradesRowEClass, TRADES_ROW__DISCHARGE_SLOT_CONTRACT_PARAMS);
		createEReference(tradesRowEClass, TRADES_ROW__MARKET_ALLOCATION);
		createEAttribute(tradesRowEClass, TRADES_ROW__LOAD_TERMINAL_VALID);
		createEAttribute(tradesRowEClass, TRADES_ROW__DISCHARGE_TERMINAL_VALID);
		createEAttribute(tradesRowEClass, TRADES_ROW__GROUP);
		createEOperation(tradesRowEClass, TRADES_ROW___GET_ASSIGNABLE_OBJECT);

		tradesTableRootEClass = createEClass(TRADES_TABLE_ROOT);
		createEReference(tradesTableRootEClass, TRADES_TABLE_ROOT__TRADES_ROWS);
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
		SchedulePackage theSchedulePackage = (SchedulePackage)EPackage.Registry.INSTANCE.getEPackage(SchedulePackage.eNS_URI);
		CommercialPackage theCommercialPackage = (CommercialPackage)EPackage.Registry.INSTANCE.getEPackage(CommercialPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes, features, and operations; add parameters
		initEClass(tradesRowEClass, TradesRow.class, "TradesRow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getTradesRow_LoadSlot(), theCargoPackage.getLoadSlot(), null, "loadSlot", null, 0, 1, TradesRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTradesRow_DischargeSlot(), theCargoPackage.getDischargeSlot(), null, "dischargeSlot", null, 0, 1, TradesRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTradesRow_Cargo(), theCargoPackage.getCargo(), null, "cargo", null, 0, 1, TradesRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTradesRow_CargoAllocation(), theSchedulePackage.getCargoAllocation(), null, "cargoAllocation", null, 0, 1, TradesRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTradesRow_LoadAllocation(), theSchedulePackage.getSlotAllocation(), null, "loadAllocation", null, 0, 1, TradesRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTradesRow_DischargeAllocation(), theSchedulePackage.getSlotAllocation(), null, "dischargeAllocation", null, 0, 1, TradesRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTradesRow_OpenSlotAllocation(), theSchedulePackage.getOpenSlotAllocation(), null, "openSlotAllocation", null, 0, 1, TradesRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTradesRow_InputEquivalents(), ecorePackage.getEObject(), null, "inputEquivalents", null, 0, -1, TradesRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTradesRow_LoadSlotContractParams(), theCommercialPackage.getSlotContractParams(), null, "loadSlotContractParams", null, 0, 1, TradesRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTradesRow_PrimaryRecord(), ecorePackage.getEBoolean(), "primaryRecord", null, 0, 1, TradesRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTradesRow_DischargeSlotContractParams(), theCommercialPackage.getSlotContractParams(), null, "dischargeSlotContractParams", null, 0, 1, TradesRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTradesRow_MarketAllocation(), theSchedulePackage.getMarketAllocation(), null, "marketAllocation", null, 0, 1, TradesRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTradesRow_LoadTerminalValid(), ecorePackage.getEBoolean(), "loadTerminalValid", null, 0, 1, TradesRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTradesRow_DischargeTerminalValid(), ecorePackage.getEBoolean(), "dischargeTerminalValid", null, 0, 1, TradesRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTradesRow_Group(), ecorePackage.getEJavaObject(), "group", null, 0, 1, TradesRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		initEOperation(getTradesRow__GetAssignableObject(), ecorePackage.getEObject(), "getAssignableObject", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(tradesTableRootEClass, TradesTableRoot.class, "TradesTableRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getTradesTableRoot_TradesRows(), this.getTradesRow(), null, "tradesRows", null, 0, -1, TradesTableRoot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //CargoEditorModelPackageImpl
