/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage;
import com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics;
import com.mmxlabs.lingo.reports.views.changeset.model.Metrics;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ChangesetPackageImpl extends EPackageImpl implements ChangesetPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass changeSetRootEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass changeSetEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass metricsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass deltaMetricsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass changeSetRowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType scenarioResultEDataType = null;

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
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ChangesetPackageImpl() {
		super(eNS_URI, ChangesetFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link ChangesetPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static ChangesetPackage init() {
		if (isInited) return (ChangesetPackage)EPackage.Registry.INSTANCE.getEPackage(ChangesetPackage.eNS_URI);

		// Obtain or create and register package
		ChangesetPackageImpl theChangesetPackage = (ChangesetPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ChangesetPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ChangesetPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		ScenarioServicePackage.eINSTANCE.eClass();
		SchedulePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theChangesetPackage.createPackageContents();

		// Initialize created meta-data
		theChangesetPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theChangesetPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(ChangesetPackage.eNS_URI, theChangesetPackage);
		return theChangesetPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getChangeSetRoot() {
		return changeSetRootEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRoot_ChangeSets() {
		return (EReference)changeSetRootEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getChangeSet() {
		return changeSetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSet_MetricsToBase() {
		return (EReference)changeSetEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSet_MetricsToPrevious() {
		return (EReference)changeSetEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSet_BaseScenarioRef() {
		return (EReference)changeSetEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSet_PrevScenarioRef() {
		return (EReference)changeSetEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSet_CurrentScenarioRef() {
		return (EReference)changeSetEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSet_BaseScenario() {
		return (EAttribute)changeSetEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSet_PrevScenario() {
		return (EAttribute)changeSetEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSet_CurrentScenario() {
		return (EAttribute)changeSetEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSet_ChangeSetRowsToBase() {
		return (EReference)changeSetEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSet_ChangeSetRowsToPrevious() {
		return (EReference)changeSetEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSet_CurrentMetrics() {
		return (EReference)changeSetEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSet_Description() {
		return (EAttribute)changeSetEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMetrics() {
		return metricsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMetrics_Pnl() {
		return (EAttribute)metricsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMetrics_Lateness() {
		return (EAttribute)metricsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMetrics_Capacity() {
		return (EAttribute)metricsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDeltaMetrics() {
		return deltaMetricsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDeltaMetrics_PnlDelta() {
		return (EAttribute)deltaMetricsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDeltaMetrics_LatenessDelta() {
		return (EAttribute)deltaMetricsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDeltaMetrics_CapacityDelta() {
		return (EAttribute)deltaMetricsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getChangeSetRow() {
		return changeSetRowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetRow_LhsName() {
		return (EAttribute)changeSetRowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetRow_RhsName() {
		return (EAttribute)changeSetRowEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetRow_OriginalVesselName() {
		return (EAttribute)changeSetRowEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetRow_NewVesselName() {
		return (EAttribute)changeSetRowEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRow_LhsWiringLink() {
		return (EReference)changeSetRowEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRow_RhsWiringLink() {
		return (EReference)changeSetRowEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRow_LoadSlot() {
		return (EReference)changeSetRowEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRow_DischargeSlot() {
		return (EReference)changeSetRowEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRow_OriginalLoadAllocation() {
		return (EReference)changeSetRowEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRow_NewLoadAllocation() {
		return (EReference)changeSetRowEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRow_OriginalDischargeAllocation() {
		return (EReference)changeSetRowEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRow_NewDischargeAllocation() {
		return (EReference)changeSetRowEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetRow_WiringChange() {
		return (EAttribute)changeSetRowEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetRow_VesselChange() {
		return (EAttribute)changeSetRowEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRow_OriginalGroupProfitAndLoss() {
		return (EReference)changeSetRowEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRow_NewGroupProfitAndLoss() {
		return (EReference)changeSetRowEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRow_OriginalEventGrouping() {
		return (EReference)changeSetRowEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRow_NewEventGrouping() {
		return (EReference)changeSetRowEClass.getEStructuralFeatures().get(17);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetRow_OriginalVesselShortName() {
		return (EAttribute)changeSetRowEClass.getEStructuralFeatures().get(18);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetRow_NewVesselShortName() {
		return (EAttribute)changeSetRowEClass.getEStructuralFeatures().get(19);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRow_OriginalOpenLoadAllocation() {
		return (EReference)changeSetRowEClass.getEStructuralFeatures().get(20);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRow_NewOpenLoadAllocation() {
		return (EReference)changeSetRowEClass.getEStructuralFeatures().get(21);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRow_OriginalOpenDischargeAllocation() {
		return (EReference)changeSetRowEClass.getEStructuralFeatures().get(22);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRow_NewOpenDischargeAllocation() {
		return (EReference)changeSetRowEClass.getEStructuralFeatures().get(23);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getScenarioResult() {
		return scenarioResultEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangesetFactory getChangesetFactory() {
		return (ChangesetFactory)getEFactoryInstance();
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
		changeSetRootEClass = createEClass(CHANGE_SET_ROOT);
		createEReference(changeSetRootEClass, CHANGE_SET_ROOT__CHANGE_SETS);

		changeSetEClass = createEClass(CHANGE_SET);
		createEReference(changeSetEClass, CHANGE_SET__METRICS_TO_BASE);
		createEReference(changeSetEClass, CHANGE_SET__METRICS_TO_PREVIOUS);
		createEReference(changeSetEClass, CHANGE_SET__BASE_SCENARIO_REF);
		createEReference(changeSetEClass, CHANGE_SET__PREV_SCENARIO_REF);
		createEReference(changeSetEClass, CHANGE_SET__CURRENT_SCENARIO_REF);
		createEAttribute(changeSetEClass, CHANGE_SET__BASE_SCENARIO);
		createEAttribute(changeSetEClass, CHANGE_SET__PREV_SCENARIO);
		createEAttribute(changeSetEClass, CHANGE_SET__CURRENT_SCENARIO);
		createEReference(changeSetEClass, CHANGE_SET__CHANGE_SET_ROWS_TO_BASE);
		createEReference(changeSetEClass, CHANGE_SET__CHANGE_SET_ROWS_TO_PREVIOUS);
		createEReference(changeSetEClass, CHANGE_SET__CURRENT_METRICS);
		createEAttribute(changeSetEClass, CHANGE_SET__DESCRIPTION);

		metricsEClass = createEClass(METRICS);
		createEAttribute(metricsEClass, METRICS__PNL);
		createEAttribute(metricsEClass, METRICS__LATENESS);
		createEAttribute(metricsEClass, METRICS__CAPACITY);

		deltaMetricsEClass = createEClass(DELTA_METRICS);
		createEAttribute(deltaMetricsEClass, DELTA_METRICS__PNL_DELTA);
		createEAttribute(deltaMetricsEClass, DELTA_METRICS__LATENESS_DELTA);
		createEAttribute(deltaMetricsEClass, DELTA_METRICS__CAPACITY_DELTA);

		changeSetRowEClass = createEClass(CHANGE_SET_ROW);
		createEAttribute(changeSetRowEClass, CHANGE_SET_ROW__LHS_NAME);
		createEAttribute(changeSetRowEClass, CHANGE_SET_ROW__RHS_NAME);
		createEAttribute(changeSetRowEClass, CHANGE_SET_ROW__ORIGINAL_VESSEL_NAME);
		createEAttribute(changeSetRowEClass, CHANGE_SET_ROW__NEW_VESSEL_NAME);
		createEReference(changeSetRowEClass, CHANGE_SET_ROW__LHS_WIRING_LINK);
		createEReference(changeSetRowEClass, CHANGE_SET_ROW__RHS_WIRING_LINK);
		createEReference(changeSetRowEClass, CHANGE_SET_ROW__LOAD_SLOT);
		createEReference(changeSetRowEClass, CHANGE_SET_ROW__DISCHARGE_SLOT);
		createEReference(changeSetRowEClass, CHANGE_SET_ROW__ORIGINAL_LOAD_ALLOCATION);
		createEReference(changeSetRowEClass, CHANGE_SET_ROW__NEW_LOAD_ALLOCATION);
		createEReference(changeSetRowEClass, CHANGE_SET_ROW__ORIGINAL_DISCHARGE_ALLOCATION);
		createEReference(changeSetRowEClass, CHANGE_SET_ROW__NEW_DISCHARGE_ALLOCATION);
		createEAttribute(changeSetRowEClass, CHANGE_SET_ROW__WIRING_CHANGE);
		createEAttribute(changeSetRowEClass, CHANGE_SET_ROW__VESSEL_CHANGE);
		createEReference(changeSetRowEClass, CHANGE_SET_ROW__ORIGINAL_GROUP_PROFIT_AND_LOSS);
		createEReference(changeSetRowEClass, CHANGE_SET_ROW__NEW_GROUP_PROFIT_AND_LOSS);
		createEReference(changeSetRowEClass, CHANGE_SET_ROW__ORIGINAL_EVENT_GROUPING);
		createEReference(changeSetRowEClass, CHANGE_SET_ROW__NEW_EVENT_GROUPING);
		createEAttribute(changeSetRowEClass, CHANGE_SET_ROW__ORIGINAL_VESSEL_SHORT_NAME);
		createEAttribute(changeSetRowEClass, CHANGE_SET_ROW__NEW_VESSEL_SHORT_NAME);
		createEReference(changeSetRowEClass, CHANGE_SET_ROW__ORIGINAL_OPEN_LOAD_ALLOCATION);
		createEReference(changeSetRowEClass, CHANGE_SET_ROW__NEW_OPEN_LOAD_ALLOCATION);
		createEReference(changeSetRowEClass, CHANGE_SET_ROW__ORIGINAL_OPEN_DISCHARGE_ALLOCATION);
		createEReference(changeSetRowEClass, CHANGE_SET_ROW__NEW_OPEN_DISCHARGE_ALLOCATION);

		// Create data types
		scenarioResultEDataType = createEDataType(SCENARIO_RESULT);
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
		ScenarioServicePackage theScenarioServicePackage = (ScenarioServicePackage)EPackage.Registry.INSTANCE.getEPackage(ScenarioServicePackage.eNS_URI);
		CargoPackage theCargoPackage = (CargoPackage)EPackage.Registry.INSTANCE.getEPackage(CargoPackage.eNS_URI);
		SchedulePackage theSchedulePackage = (SchedulePackage)EPackage.Registry.INSTANCE.getEPackage(SchedulePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes, features, and operations; add parameters
		initEClass(changeSetRootEClass, ChangeSetRoot.class, "ChangeSetRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getChangeSetRoot_ChangeSets(), this.getChangeSet(), null, "changeSets", null, 0, -1, ChangeSetRoot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(changeSetEClass, ChangeSet.class, "ChangeSet", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getChangeSet_MetricsToBase(), this.getDeltaMetrics(), null, "metricsToBase", null, 0, 1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSet_MetricsToPrevious(), this.getDeltaMetrics(), null, "metricsToPrevious", null, 0, 1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSet_BaseScenarioRef(), theScenarioServicePackage.getModelReference(), null, "baseScenarioRef", null, 0, 1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSet_PrevScenarioRef(), theScenarioServicePackage.getModelReference(), null, "prevScenarioRef", null, 0, 1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSet_CurrentScenarioRef(), theScenarioServicePackage.getModelReference(), null, "currentScenarioRef", null, 0, 1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSet_BaseScenario(), this.getScenarioResult(), "baseScenario", null, 0, 1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSet_PrevScenario(), this.getScenarioResult(), "prevScenario", null, 0, 1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSet_CurrentScenario(), this.getScenarioResult(), "currentScenario", null, 0, 1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSet_ChangeSetRowsToBase(), this.getChangeSetRow(), null, "changeSetRowsToBase", null, 0, -1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSet_ChangeSetRowsToPrevious(), this.getChangeSetRow(), null, "changeSetRowsToPrevious", null, 0, -1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSet_CurrentMetrics(), this.getMetrics(), null, "currentMetrics", null, 0, 1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSet_Description(), ecorePackage.getEString(), "description", null, 0, 1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(metricsEClass, Metrics.class, "Metrics", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMetrics_Pnl(), ecorePackage.getEInt(), "pnl", null, 0, 1, Metrics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMetrics_Lateness(), ecorePackage.getEInt(), "lateness", null, 0, 1, Metrics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMetrics_Capacity(), ecorePackage.getEInt(), "capacity", null, 0, 1, Metrics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(deltaMetricsEClass, DeltaMetrics.class, "DeltaMetrics", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDeltaMetrics_PnlDelta(), ecorePackage.getEInt(), "pnlDelta", null, 0, 1, DeltaMetrics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDeltaMetrics_LatenessDelta(), ecorePackage.getEInt(), "latenessDelta", null, 0, 1, DeltaMetrics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDeltaMetrics_CapacityDelta(), ecorePackage.getEInt(), "capacityDelta", null, 0, 1, DeltaMetrics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(changeSetRowEClass, ChangeSetRow.class, "ChangeSetRow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getChangeSetRow_LhsName(), ecorePackage.getEString(), "lhsName", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetRow_RhsName(), ecorePackage.getEString(), "rhsName", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetRow_OriginalVesselName(), ecorePackage.getEString(), "originalVesselName", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetRow_NewVesselName(), ecorePackage.getEString(), "newVesselName", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRow_LhsWiringLink(), this.getChangeSetRow(), this.getChangeSetRow_RhsWiringLink(), "lhsWiringLink", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRow_RhsWiringLink(), this.getChangeSetRow(), this.getChangeSetRow_LhsWiringLink(), "rhsWiringLink", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRow_LoadSlot(), theCargoPackage.getLoadSlot(), null, "loadSlot", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRow_DischargeSlot(), theCargoPackage.getDischargeSlot(), null, "dischargeSlot", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRow_OriginalLoadAllocation(), theSchedulePackage.getSlotAllocation(), null, "originalLoadAllocation", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRow_NewLoadAllocation(), theSchedulePackage.getSlotAllocation(), null, "newLoadAllocation", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRow_OriginalDischargeAllocation(), theSchedulePackage.getSlotAllocation(), null, "originalDischargeAllocation", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRow_NewDischargeAllocation(), theSchedulePackage.getSlotAllocation(), null, "newDischargeAllocation", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetRow_WiringChange(), ecorePackage.getEBoolean(), "wiringChange", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetRow_VesselChange(), ecorePackage.getEBoolean(), "vesselChange", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRow_OriginalGroupProfitAndLoss(), theSchedulePackage.getProfitAndLossContainer(), null, "originalGroupProfitAndLoss", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRow_NewGroupProfitAndLoss(), theSchedulePackage.getProfitAndLossContainer(), null, "newGroupProfitAndLoss", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRow_OriginalEventGrouping(), theSchedulePackage.getEventGrouping(), null, "originalEventGrouping", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRow_NewEventGrouping(), theSchedulePackage.getEventGrouping(), null, "newEventGrouping", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetRow_OriginalVesselShortName(), ecorePackage.getEString(), "originalVesselShortName", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetRow_NewVesselShortName(), ecorePackage.getEString(), "newVesselShortName", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRow_OriginalOpenLoadAllocation(), theSchedulePackage.getOpenSlotAllocation(), null, "originalOpenLoadAllocation", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRow_NewOpenLoadAllocation(), theSchedulePackage.getOpenSlotAllocation(), null, "newOpenLoadAllocation", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRow_OriginalOpenDischargeAllocation(), theSchedulePackage.getOpenSlotAllocation(), null, "originalOpenDischargeAllocation", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRow_NewOpenDischargeAllocation(), theSchedulePackage.getOpenSlotAllocation(), null, "newOpenDischargeAllocation", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize data types
		initEDataType(scenarioResultEDataType, ScenarioResult.class, "ScenarioResult", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //ChangesetPackageImpl
