/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.nominations.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.mmxlabs.models.datetime.DateTimePackage;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.nominations.AbstractNominatedValue;
import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.AbstractNominationSpec;
import com.mmxlabs.models.lng.nominations.ContractNomination;
import com.mmxlabs.models.lng.nominations.ContractNominationSpec;
import com.mmxlabs.models.lng.nominations.DatePeriodPrior;
import com.mmxlabs.models.lng.nominations.NominationsFactory;
import com.mmxlabs.models.lng.nominations.NominationsModel;
import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.lng.nominations.NominationsParameters;
import com.mmxlabs.models.lng.nominations.Side;
import com.mmxlabs.models.lng.nominations.SlotNomination;
import com.mmxlabs.models.lng.nominations.SlotNominationSpec;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class NominationsPackageImpl extends EPackageImpl implements NominationsPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass nominationsModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass abstractNominationSpecEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass slotNominationSpecEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass slotNominationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass contractNominationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass contractNominationSpecEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass abstractNominationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass nominationsParametersEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass abstractNominatedValueEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum datePeriodPriorEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum sideEEnum = null;

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
	 * @see com.mmxlabs.models.lng.nominations.NominationsPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private NominationsPackageImpl() {
		super(eNS_URI, NominationsFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link NominationsPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static NominationsPackage init() {
		if (isInited) return (NominationsPackage)EPackage.Registry.INSTANCE.getEPackage(NominationsPackage.eNS_URI);

		// Obtain or create and register package
		Object registeredNominationsPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		NominationsPackageImpl theNominationsPackage = registeredNominationsPackage instanceof NominationsPackageImpl ? (NominationsPackageImpl)registeredNominationsPackage : new NominationsPackageImpl();

		isInited = true;

		// Initialize simple dependencies
		CommercialPackage.eINSTANCE.eClass();
		FleetPackage.eINSTANCE.eClass();
		TypesPackage.eINSTANCE.eClass();
		MMXCorePackage.eINSTANCE.eClass();
		PortPackage.eINSTANCE.eClass();
		PricingPackage.eINSTANCE.eClass();
		DateTimePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theNominationsPackage.createPackageContents();

		// Initialize created meta-data
		theNominationsPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theNominationsPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(NominationsPackage.eNS_URI, theNominationsPackage);
		return theNominationsPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getNominationsModel() {
		return nominationsModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getNominationsModel_NominationSpecs() {
		return (EReference)nominationsModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getNominationsModel_Nominations() {
		return (EReference)nominationsModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getNominationsModel_NominationParameters() {
		return (EReference)nominationsModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getAbstractNominationSpec() {
		return abstractNominationSpecEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAbstractNominationSpec_Type() {
		return (EAttribute)abstractNominationSpecEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAbstractNominationSpec_Counterparty() {
		return (EAttribute)abstractNominationSpecEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAbstractNominationSpec_Remark() {
		return (EAttribute)abstractNominationSpecEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAbstractNominationSpec_Size() {
		return (EAttribute)abstractNominationSpecEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAbstractNominationSpec_SizeUnits() {
		return (EAttribute)abstractNominationSpecEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAbstractNominationSpec_DayOfMonth() {
		return (EAttribute)abstractNominationSpecEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAbstractNominationSpec_AlertSize() {
		return (EAttribute)abstractNominationSpecEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAbstractNominationSpec_AlertSizeUnits() {
		return (EAttribute)abstractNominationSpecEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAbstractNominationSpec_Side() {
		return (EAttribute)abstractNominationSpecEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAbstractNominationSpec_RefererId() {
		return (EAttribute)abstractNominationSpecEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSlotNominationSpec() {
		return slotNominationSpecEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSlotNomination() {
		return slotNominationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getContractNomination() {
		return contractNominationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getContractNominationSpec() {
		return contractNominationSpecEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getAbstractNomination() {
		return abstractNominationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAbstractNomination_NomineeId() {
		return (EAttribute)abstractNominationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAbstractNomination_DueDate() {
		return (EAttribute)abstractNominationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAbstractNomination_Done() {
		return (EAttribute)abstractNominationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAbstractNomination_AlertDate() {
		return (EAttribute)abstractNominationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAbstractNomination_SpecUuid() {
		return (EAttribute)abstractNominationEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAbstractNomination_Deleted() {
		return (EAttribute)abstractNominationEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAbstractNomination_NominatedValue() {
		return (EAttribute)abstractNominationEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getNominationsParameters() {
		return nominationsParametersEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNominationsParameters_StartDate() {
		return (EAttribute)nominationsParametersEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNominationsParameters_EndDate() {
		return (EAttribute)nominationsParametersEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getAbstractNominatedValue() {
		return abstractNominatedValueEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getDatePeriodPrior() {
		return datePeriodPriorEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getSide() {
		return sideEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NominationsFactory getNominationsFactory() {
		return (NominationsFactory)getEFactoryInstance();
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
		nominationsModelEClass = createEClass(NOMINATIONS_MODEL);
		createEReference(nominationsModelEClass, NOMINATIONS_MODEL__NOMINATION_SPECS);
		createEReference(nominationsModelEClass, NOMINATIONS_MODEL__NOMINATIONS);
		createEReference(nominationsModelEClass, NOMINATIONS_MODEL__NOMINATION_PARAMETERS);

		abstractNominationSpecEClass = createEClass(ABSTRACT_NOMINATION_SPEC);
		createEAttribute(abstractNominationSpecEClass, ABSTRACT_NOMINATION_SPEC__TYPE);
		createEAttribute(abstractNominationSpecEClass, ABSTRACT_NOMINATION_SPEC__COUNTERPARTY);
		createEAttribute(abstractNominationSpecEClass, ABSTRACT_NOMINATION_SPEC__REMARK);
		createEAttribute(abstractNominationSpecEClass, ABSTRACT_NOMINATION_SPEC__SIZE);
		createEAttribute(abstractNominationSpecEClass, ABSTRACT_NOMINATION_SPEC__SIZE_UNITS);
		createEAttribute(abstractNominationSpecEClass, ABSTRACT_NOMINATION_SPEC__DAY_OF_MONTH);
		createEAttribute(abstractNominationSpecEClass, ABSTRACT_NOMINATION_SPEC__ALERT_SIZE);
		createEAttribute(abstractNominationSpecEClass, ABSTRACT_NOMINATION_SPEC__ALERT_SIZE_UNITS);
		createEAttribute(abstractNominationSpecEClass, ABSTRACT_NOMINATION_SPEC__SIDE);
		createEAttribute(abstractNominationSpecEClass, ABSTRACT_NOMINATION_SPEC__REFERER_ID);

		slotNominationSpecEClass = createEClass(SLOT_NOMINATION_SPEC);

		slotNominationEClass = createEClass(SLOT_NOMINATION);

		contractNominationEClass = createEClass(CONTRACT_NOMINATION);

		contractNominationSpecEClass = createEClass(CONTRACT_NOMINATION_SPEC);

		abstractNominationEClass = createEClass(ABSTRACT_NOMINATION);
		createEAttribute(abstractNominationEClass, ABSTRACT_NOMINATION__NOMINEE_ID);
		createEAttribute(abstractNominationEClass, ABSTRACT_NOMINATION__DUE_DATE);
		createEAttribute(abstractNominationEClass, ABSTRACT_NOMINATION__DONE);
		createEAttribute(abstractNominationEClass, ABSTRACT_NOMINATION__ALERT_DATE);
		createEAttribute(abstractNominationEClass, ABSTRACT_NOMINATION__SPEC_UUID);
		createEAttribute(abstractNominationEClass, ABSTRACT_NOMINATION__DELETED);
		createEAttribute(abstractNominationEClass, ABSTRACT_NOMINATION__NOMINATED_VALUE);

		nominationsParametersEClass = createEClass(NOMINATIONS_PARAMETERS);
		createEAttribute(nominationsParametersEClass, NOMINATIONS_PARAMETERS__START_DATE);
		createEAttribute(nominationsParametersEClass, NOMINATIONS_PARAMETERS__END_DATE);

		abstractNominatedValueEClass = createEClass(ABSTRACT_NOMINATED_VALUE);

		// Create enums
		datePeriodPriorEEnum = createEEnum(DATE_PERIOD_PRIOR);
		sideEEnum = createEEnum(SIDE);
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
		MMXCorePackage theMMXCorePackage = (MMXCorePackage)EPackage.Registry.INSTANCE.getEPackage(MMXCorePackage.eNS_URI);
		DateTimePackage theDateTimePackage = (DateTimePackage)EPackage.Registry.INSTANCE.getEPackage(DateTimePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		nominationsModelEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		abstractNominationSpecEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		slotNominationSpecEClass.getESuperTypes().add(this.getAbstractNominationSpec());
		slotNominationEClass.getESuperTypes().add(this.getAbstractNomination());
		contractNominationEClass.getESuperTypes().add(this.getAbstractNomination());
		contractNominationSpecEClass.getESuperTypes().add(this.getAbstractNominationSpec());
		abstractNominationEClass.getESuperTypes().add(this.getAbstractNominationSpec());

		// Initialize classes and features; add operations and parameters
		initEClass(nominationsModelEClass, NominationsModel.class, "NominationsModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getNominationsModel_NominationSpecs(), this.getAbstractNominationSpec(), null, "nominationSpecs", null, 0, -1, NominationsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getNominationsModel_Nominations(), this.getAbstractNomination(), null, "nominations", null, 0, -1, NominationsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getNominationsModel_NominationParameters(), this.getNominationsParameters(), null, "nominationParameters", null, 0, 1, NominationsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(abstractNominationSpecEClass, AbstractNominationSpec.class, "AbstractNominationSpec", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAbstractNominationSpec_Type(), ecorePackage.getEString(), "type", null, 0, 1, AbstractNominationSpec.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAbstractNominationSpec_Counterparty(), ecorePackage.getEBoolean(), "counterparty", null, 0, 1, AbstractNominationSpec.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAbstractNominationSpec_Remark(), ecorePackage.getEString(), "remark", null, 0, 1, AbstractNominationSpec.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAbstractNominationSpec_Size(), ecorePackage.getEInt(), "size", null, 0, 1, AbstractNominationSpec.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAbstractNominationSpec_SizeUnits(), this.getDatePeriodPrior(), "sizeUnits", "DaysPrior", 0, 1, AbstractNominationSpec.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAbstractNominationSpec_DayOfMonth(), ecorePackage.getEInt(), "dayOfMonth", null, 0, 1, AbstractNominationSpec.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAbstractNominationSpec_AlertSize(), ecorePackage.getEInt(), "alertSize", null, 0, 1, AbstractNominationSpec.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAbstractNominationSpec_AlertSizeUnits(), this.getDatePeriodPrior(), "alertSizeUnits", "DaysPrior", 0, 1, AbstractNominationSpec.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAbstractNominationSpec_Side(), this.getSide(), "side", null, 0, 1, AbstractNominationSpec.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAbstractNominationSpec_RefererId(), ecorePackage.getEString(), "refererId", null, 0, 1, AbstractNominationSpec.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(slotNominationSpecEClass, SlotNominationSpec.class, "SlotNominationSpec", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(slotNominationEClass, SlotNomination.class, "SlotNomination", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(contractNominationEClass, ContractNomination.class, "ContractNomination", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(contractNominationSpecEClass, ContractNominationSpec.class, "ContractNominationSpec", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(abstractNominationEClass, AbstractNomination.class, "AbstractNomination", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAbstractNomination_NomineeId(), ecorePackage.getEString(), "nomineeId", null, 0, 1, AbstractNomination.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAbstractNomination_DueDate(), theDateTimePackage.getLocalDate(), "dueDate", null, 0, 1, AbstractNomination.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAbstractNomination_Done(), ecorePackage.getEBoolean(), "done", null, 0, 1, AbstractNomination.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAbstractNomination_AlertDate(), theDateTimePackage.getLocalDate(), "alertDate", null, 0, 1, AbstractNomination.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAbstractNomination_SpecUuid(), ecorePackage.getEString(), "specUuid", null, 0, 1, AbstractNomination.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAbstractNomination_Deleted(), ecorePackage.getEBoolean(), "deleted", "false", 0, 1, AbstractNomination.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAbstractNomination_NominatedValue(), ecorePackage.getEString(), "nominatedValue", null, 0, 1, AbstractNomination.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(nominationsParametersEClass, NominationsParameters.class, "NominationsParameters", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getNominationsParameters_StartDate(), theDateTimePackage.getLocalDate(), "startDate", null, 0, 1, NominationsParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNominationsParameters_EndDate(), theDateTimePackage.getLocalDate(), "endDate", null, 0, 1, NominationsParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(abstractNominatedValueEClass, AbstractNominatedValue.class, "AbstractNominatedValue", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		// Initialize enums and add enum literals
		initEEnum(datePeriodPriorEEnum, DatePeriodPrior.class, "DatePeriodPrior");
		addEEnumLiteral(datePeriodPriorEEnum, DatePeriodPrior.DAYS_PRIOR);
		addEEnumLiteral(datePeriodPriorEEnum, DatePeriodPrior.MONTHS_PRIOR);

		initEEnum(sideEEnum, Side.class, "Side");
		addEEnumLiteral(sideEEnum, Side.BUY);
		addEEnumLiteral(sideEEnum, Side.SELL);

		// Create resource
		createResource(eNS_URI);
	}

} //NominationsPackageImpl
