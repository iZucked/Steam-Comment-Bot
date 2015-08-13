/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model.impl;

import com.mmxlabs.lingo.reports.views.changeset.model.Change;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage;
import com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange;
import com.mmxlabs.lingo.reports.views.changeset.model.Metrics;
import com.mmxlabs.lingo.reports.views.changeset.model.VesselChange;
import com.mmxlabs.lingo.reports.views.changeset.model.WiringChange;

import com.mmxlabs.models.lng.cargo.CargoPackage;

import com.mmxlabs.models.lng.types.TypesPackage;

import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

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
	private EClass changeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eventVesselChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass wiringChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass vesselChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass changeSetRowEClass = null;

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
		com.mmxlabs.models.lng.schedule.SchedulePackage.eINSTANCE.eClass();

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
	public EReference getChangeSet_ChangesToBase() {
		return (EReference)changeSetEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSet_ChangesToPrevious() {
		return (EReference)changeSetEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSet_MetricsToBase() {
		return (EReference)changeSetEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSet_MetricsToPrevious() {
		return (EReference)changeSetEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSet_BaseScenarioRef() {
		return (EReference)changeSetEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSet_PrevScenarioRef() {
		return (EReference)changeSetEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSet_CurrentScenarioRef() {
		return (EReference)changeSetEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSet_BaseScenario() {
		return (EReference)changeSetEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSet_PrevScenario() {
		return (EReference)changeSetEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSet_CurrentScenario() {
		return (EReference)changeSetEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSet_ChangeSetRowsToBase() {
		return (EReference)changeSetEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSet_ChangeSetRowsToPrevious() {
		return (EReference)changeSetEClass.getEStructuralFeatures().get(11);
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
	public EAttribute getMetrics_PnlDelta() {
		return (EAttribute)metricsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMetrics_LatenessDelta() {
		return (EAttribute)metricsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMetrics_CapacityDelta() {
		return (EAttribute)metricsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getChange() {
		return changeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEventVesselChange() {
		return eventVesselChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEventVesselChange_LoadSlot_base() {
		return (EReference)eventVesselChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEventVesselChange_LoadSlot_target() {
		return (EReference)eventVesselChangeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEventVesselChange_OriginalVessel() {
		return (EReference)eventVesselChangeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEventVesselChange_NewVessel() {
		return (EReference)eventVesselChangeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getWiringChange() {
		return wiringChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getWiringChange_LoadSlot_base() {
		return (EReference)wiringChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getWiringChange_LoadSlot_target() {
		return (EReference)wiringChangeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getWiringChange_OriginalDischargeSlot() {
		return (EReference)wiringChangeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getWiringChange_NewDischargeSlot() {
		return (EReference)wiringChangeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getWiringChange_OriginalLoadAllocation() {
		return (EReference)wiringChangeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getWiringChange_NewLoadAllocation() {
		return (EReference)wiringChangeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getWiringChange_OriginalDischargeAllocation() {
		return (EReference)wiringChangeEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getWiringChange_NewDischargeAllocation() {
		return (EReference)wiringChangeEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getVesselChange() {
		return vesselChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselChange_LoadSlot_base() {
		return (EReference)vesselChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselChange_LoadSlot_target() {
		return (EReference)vesselChangeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselChange_OriginalVessel() {
		return (EReference)vesselChangeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselChange_NewVessel() {
		return (EReference)vesselChangeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselChange_DischargeSlot_base() {
		return (EReference)vesselChangeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselChange_DischargeSlot_target() {
		return (EReference)vesselChangeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselChange_OriginalLoadAllocation() {
		return (EReference)vesselChangeEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselChange_NewLoadAllocation() {
		return (EReference)vesselChangeEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselChange_OriginalDischargeAllocation() {
		return (EReference)vesselChangeEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselChange_NewDischargeAllocation() {
		return (EReference)vesselChangeEClass.getEStructuralFeatures().get(9);
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
	public EAttribute getChangeSetRow_LhsVesselName() {
		return (EAttribute)changeSetRowEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetRow_RhsVesselName() {
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
	public EReference getChangeSetRow_LhsWiringChange() {
		return (EReference)changeSetRowEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRow_RhsWiringChange() {
		return (EReference)changeSetRowEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRow_LhsVesselChange() {
		return (EReference)changeSetRowEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRow_LoadSlot() {
		return (EReference)changeSetRowEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRow_DischargeSlot() {
		return (EReference)changeSetRowEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRow_OriginalLoadAllocation() {
		return (EReference)changeSetRowEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRow_NewLoadAllocation() {
		return (EReference)changeSetRowEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRow_OriginalDischargeAllocation() {
		return (EReference)changeSetRowEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRow_NewDischargeAllocation() {
		return (EReference)changeSetRowEClass.getEStructuralFeatures().get(14);
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
		createEReference(changeSetEClass, CHANGE_SET__CHANGES_TO_BASE);
		createEReference(changeSetEClass, CHANGE_SET__CHANGES_TO_PREVIOUS);
		createEReference(changeSetEClass, CHANGE_SET__METRICS_TO_BASE);
		createEReference(changeSetEClass, CHANGE_SET__METRICS_TO_PREVIOUS);
		createEReference(changeSetEClass, CHANGE_SET__BASE_SCENARIO_REF);
		createEReference(changeSetEClass, CHANGE_SET__PREV_SCENARIO_REF);
		createEReference(changeSetEClass, CHANGE_SET__CURRENT_SCENARIO_REF);
		createEReference(changeSetEClass, CHANGE_SET__BASE_SCENARIO);
		createEReference(changeSetEClass, CHANGE_SET__PREV_SCENARIO);
		createEReference(changeSetEClass, CHANGE_SET__CURRENT_SCENARIO);
		createEReference(changeSetEClass, CHANGE_SET__CHANGE_SET_ROWS_TO_BASE);
		createEReference(changeSetEClass, CHANGE_SET__CHANGE_SET_ROWS_TO_PREVIOUS);

		metricsEClass = createEClass(METRICS);
		createEAttribute(metricsEClass, METRICS__PNL_DELTA);
		createEAttribute(metricsEClass, METRICS__LATENESS_DELTA);
		createEAttribute(metricsEClass, METRICS__CAPACITY_DELTA);

		changeEClass = createEClass(CHANGE);

		eventVesselChangeEClass = createEClass(EVENT_VESSEL_CHANGE);
		createEReference(eventVesselChangeEClass, EVENT_VESSEL_CHANGE__LOAD_SLOT_BASE);
		createEReference(eventVesselChangeEClass, EVENT_VESSEL_CHANGE__LOAD_SLOT_TARGET);
		createEReference(eventVesselChangeEClass, EVENT_VESSEL_CHANGE__ORIGINAL_VESSEL);
		createEReference(eventVesselChangeEClass, EVENT_VESSEL_CHANGE__NEW_VESSEL);

		wiringChangeEClass = createEClass(WIRING_CHANGE);
		createEReference(wiringChangeEClass, WIRING_CHANGE__LOAD_SLOT_BASE);
		createEReference(wiringChangeEClass, WIRING_CHANGE__LOAD_SLOT_TARGET);
		createEReference(wiringChangeEClass, WIRING_CHANGE__ORIGINAL_DISCHARGE_SLOT);
		createEReference(wiringChangeEClass, WIRING_CHANGE__NEW_DISCHARGE_SLOT);
		createEReference(wiringChangeEClass, WIRING_CHANGE__ORIGINAL_LOAD_ALLOCATION);
		createEReference(wiringChangeEClass, WIRING_CHANGE__NEW_LOAD_ALLOCATION);
		createEReference(wiringChangeEClass, WIRING_CHANGE__ORIGINAL_DISCHARGE_ALLOCATION);
		createEReference(wiringChangeEClass, WIRING_CHANGE__NEW_DISCHARGE_ALLOCATION);

		vesselChangeEClass = createEClass(VESSEL_CHANGE);
		createEReference(vesselChangeEClass, VESSEL_CHANGE__LOAD_SLOT_BASE);
		createEReference(vesselChangeEClass, VESSEL_CHANGE__LOAD_SLOT_TARGET);
		createEReference(vesselChangeEClass, VESSEL_CHANGE__ORIGINAL_VESSEL);
		createEReference(vesselChangeEClass, VESSEL_CHANGE__NEW_VESSEL);
		createEReference(vesselChangeEClass, VESSEL_CHANGE__DISCHARGE_SLOT_BASE);
		createEReference(vesselChangeEClass, VESSEL_CHANGE__DISCHARGE_SLOT_TARGET);
		createEReference(vesselChangeEClass, VESSEL_CHANGE__ORIGINAL_LOAD_ALLOCATION);
		createEReference(vesselChangeEClass, VESSEL_CHANGE__NEW_LOAD_ALLOCATION);
		createEReference(vesselChangeEClass, VESSEL_CHANGE__ORIGINAL_DISCHARGE_ALLOCATION);
		createEReference(vesselChangeEClass, VESSEL_CHANGE__NEW_DISCHARGE_ALLOCATION);

		changeSetRowEClass = createEClass(CHANGE_SET_ROW);
		createEAttribute(changeSetRowEClass, CHANGE_SET_ROW__LHS_NAME);
		createEAttribute(changeSetRowEClass, CHANGE_SET_ROW__RHS_NAME);
		createEAttribute(changeSetRowEClass, CHANGE_SET_ROW__LHS_VESSEL_NAME);
		createEAttribute(changeSetRowEClass, CHANGE_SET_ROW__RHS_VESSEL_NAME);
		createEReference(changeSetRowEClass, CHANGE_SET_ROW__LHS_WIRING_LINK);
		createEReference(changeSetRowEClass, CHANGE_SET_ROW__RHS_WIRING_LINK);
		createEReference(changeSetRowEClass, CHANGE_SET_ROW__LHS_WIRING_CHANGE);
		createEReference(changeSetRowEClass, CHANGE_SET_ROW__RHS_WIRING_CHANGE);
		createEReference(changeSetRowEClass, CHANGE_SET_ROW__LHS_VESSEL_CHANGE);
		createEReference(changeSetRowEClass, CHANGE_SET_ROW__LOAD_SLOT);
		createEReference(changeSetRowEClass, CHANGE_SET_ROW__DISCHARGE_SLOT);
		createEReference(changeSetRowEClass, CHANGE_SET_ROW__ORIGINAL_LOAD_ALLOCATION);
		createEReference(changeSetRowEClass, CHANGE_SET_ROW__NEW_LOAD_ALLOCATION);
		createEReference(changeSetRowEClass, CHANGE_SET_ROW__ORIGINAL_DISCHARGE_ALLOCATION);
		createEReference(changeSetRowEClass, CHANGE_SET_ROW__NEW_DISCHARGE_ALLOCATION);
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
		TypesPackage theTypesPackage = (TypesPackage)EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);
		com.mmxlabs.models.lng.schedule.SchedulePackage theSchedulePackage = (com.mmxlabs.models.lng.schedule.SchedulePackage)EPackage.Registry.INSTANCE.getEPackage(com.mmxlabs.models.lng.schedule.SchedulePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		eventVesselChangeEClass.getESuperTypes().add(this.getChange());
		wiringChangeEClass.getESuperTypes().add(this.getChange());
		vesselChangeEClass.getESuperTypes().add(this.getChange());

		// Initialize classes, features, and operations; add parameters
		initEClass(changeSetRootEClass, ChangeSetRoot.class, "ChangeSetRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getChangeSetRoot_ChangeSets(), this.getChangeSet(), null, "changeSets", null, 0, -1, ChangeSetRoot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(changeSetEClass, ChangeSet.class, "ChangeSet", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getChangeSet_ChangesToBase(), this.getChange(), null, "changesToBase", null, 0, -1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSet_ChangesToPrevious(), this.getChange(), null, "changesToPrevious", null, 0, -1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSet_MetricsToBase(), this.getMetrics(), null, "metricsToBase", null, 0, 1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSet_MetricsToPrevious(), this.getMetrics(), null, "metricsToPrevious", null, 0, 1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSet_BaseScenarioRef(), theScenarioServicePackage.getModelReference(), null, "baseScenarioRef", null, 0, 1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSet_PrevScenarioRef(), theScenarioServicePackage.getModelReference(), null, "prevScenarioRef", null, 0, 1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSet_CurrentScenarioRef(), theScenarioServicePackage.getModelReference(), null, "currentScenarioRef", null, 0, 1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSet_BaseScenario(), theScenarioServicePackage.getScenarioInstance(), null, "baseScenario", null, 0, 1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSet_PrevScenario(), theScenarioServicePackage.getScenarioInstance(), null, "prevScenario", null, 0, 1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSet_CurrentScenario(), theScenarioServicePackage.getScenarioInstance(), null, "currentScenario", null, 0, 1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSet_ChangeSetRowsToBase(), this.getChangeSetRow(), null, "changeSetRowsToBase", null, 0, -1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSet_ChangeSetRowsToPrevious(), this.getChangeSetRow(), null, "changeSetRowsToPrevious", null, 0, -1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(metricsEClass, Metrics.class, "Metrics", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMetrics_PnlDelta(), ecorePackage.getEInt(), "pnlDelta", null, 0, 1, Metrics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMetrics_LatenessDelta(), ecorePackage.getEInt(), "latenessDelta", null, 0, 1, Metrics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMetrics_CapacityDelta(), ecorePackage.getEInt(), "capacityDelta", null, 0, 1, Metrics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(changeEClass, Change.class, "Change", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(eventVesselChangeEClass, EventVesselChange.class, "EventVesselChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEventVesselChange_LoadSlot_base(), theCargoPackage.getLoadSlot(), null, "loadSlot_base", null, 0, 1, EventVesselChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEventVesselChange_LoadSlot_target(), theCargoPackage.getLoadSlot(), null, "loadSlot_target", null, 0, 1, EventVesselChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEventVesselChange_OriginalVessel(), theTypesPackage.getVesselAssignmentType(), null, "originalVessel", null, 0, 1, EventVesselChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEventVesselChange_NewVessel(), theTypesPackage.getVesselAssignmentType(), null, "newVessel", null, 0, 1, EventVesselChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(wiringChangeEClass, WiringChange.class, "WiringChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getWiringChange_LoadSlot_base(), theCargoPackage.getLoadSlot(), null, "loadSlot_base", null, 0, 1, WiringChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getWiringChange_LoadSlot_target(), theCargoPackage.getLoadSlot(), null, "loadSlot_target", null, 0, 1, WiringChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getWiringChange_OriginalDischargeSlot(), theCargoPackage.getDischargeSlot(), null, "originalDischargeSlot", null, 0, 1, WiringChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getWiringChange_NewDischargeSlot(), theCargoPackage.getDischargeSlot(), null, "newDischargeSlot", null, 0, 1, WiringChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getWiringChange_OriginalLoadAllocation(), theSchedulePackage.getSlotAllocation(), null, "originalLoadAllocation", null, 0, 1, WiringChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getWiringChange_NewLoadAllocation(), theSchedulePackage.getSlotAllocation(), null, "newLoadAllocation", null, 0, 1, WiringChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getWiringChange_OriginalDischargeAllocation(), theSchedulePackage.getSlotAllocation(), null, "originalDischargeAllocation", null, 0, 1, WiringChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getWiringChange_NewDischargeAllocation(), theSchedulePackage.getSlotAllocation(), null, "newDischargeAllocation", null, 0, 1, WiringChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(vesselChangeEClass, VesselChange.class, "VesselChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getVesselChange_LoadSlot_base(), theCargoPackage.getLoadSlot(), null, "loadSlot_base", null, 0, 1, VesselChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselChange_LoadSlot_target(), theCargoPackage.getLoadSlot(), null, "loadSlot_target", null, 0, 1, VesselChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselChange_OriginalVessel(), theTypesPackage.getVesselAssignmentType(), null, "originalVessel", null, 0, 1, VesselChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselChange_NewVessel(), theTypesPackage.getVesselAssignmentType(), null, "newVessel", null, 0, 1, VesselChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselChange_DischargeSlot_base(), theCargoPackage.getDischargeSlot(), null, "dischargeSlot_base", null, 0, 1, VesselChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselChange_DischargeSlot_target(), theCargoPackage.getDischargeSlot(), null, "dischargeSlot_target", null, 0, 1, VesselChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselChange_OriginalLoadAllocation(), theSchedulePackage.getSlotAllocation(), null, "originalLoadAllocation", null, 0, 1, VesselChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselChange_NewLoadAllocation(), theSchedulePackage.getSlotAllocation(), null, "newLoadAllocation", null, 0, 1, VesselChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselChange_OriginalDischargeAllocation(), theSchedulePackage.getSlotAllocation(), null, "originalDischargeAllocation", null, 0, 1, VesselChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselChange_NewDischargeAllocation(), theSchedulePackage.getSlotAllocation(), null, "newDischargeAllocation", null, 0, 1, VesselChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(changeSetRowEClass, ChangeSetRow.class, "ChangeSetRow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getChangeSetRow_LhsName(), ecorePackage.getEString(), "lhsName", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetRow_RhsName(), ecorePackage.getEString(), "rhsName", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetRow_LhsVesselName(), ecorePackage.getEString(), "lhsVesselName", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetRow_RhsVesselName(), ecorePackage.getEString(), "rhsVesselName", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRow_LhsWiringLink(), this.getChangeSetRow(), this.getChangeSetRow_RhsWiringLink(), "lhsWiringLink", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRow_RhsWiringLink(), this.getChangeSetRow(), this.getChangeSetRow_LhsWiringLink(), "rhsWiringLink", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRow_LhsWiringChange(), this.getWiringChange(), null, "lhsWiringChange", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRow_RhsWiringChange(), this.getWiringChange(), null, "rhsWiringChange", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRow_LhsVesselChange(), this.getWiringChange(), null, "lhsVesselChange", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRow_LoadSlot(), theCargoPackage.getLoadSlot(), null, "loadSlot", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRow_DischargeSlot(), theCargoPackage.getDischargeSlot(), null, "dischargeSlot", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRow_OriginalLoadAllocation(), theSchedulePackage.getSlotAllocation(), null, "originalLoadAllocation", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRow_NewLoadAllocation(), theSchedulePackage.getSlotAllocation(), null, "newLoadAllocation", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRow_OriginalDischargeAllocation(), theSchedulePackage.getSlotAllocation(), null, "originalDischargeAllocation", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRow_NewDischargeAllocation(), theSchedulePackage.getSlotAllocation(), null, "newDischargeAllocation", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //ChangesetPackageImpl
