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
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowDataGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
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
	private EClass changeSetRowDataGroupEClass = null;

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
	private EClass changeSetRowDataEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass changeSetTableGroupEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass changeSetTableRowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass changeSetTableRootEClass = null;

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
	public EAttribute getChangeSet_BaseScenario() {
		return (EAttribute)changeSetEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSet_PrevScenario() {
		return (EAttribute)changeSetEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSet_CurrentScenario() {
		return (EAttribute)changeSetEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSet_ChangeSetRowsToBase() {
		return (EReference)changeSetEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSet_ChangeSetRowsToPrevious() {
		return (EReference)changeSetEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSet_CurrentMetrics() {
		return (EReference)changeSetEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSet_Description() {
		return (EAttribute)changeSetEClass.getEStructuralFeatures().get(8);
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
	public EClass getChangeSetRowDataGroup() {
		return changeSetRowDataGroupEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRowDataGroup_Members() {
		return (EReference)changeSetRowDataGroupEClass.getEStructuralFeatures().get(0);
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
	public EAttribute getChangeSetRow_WiringChange() {
		return (EAttribute)changeSetRowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetRow_VesselChange() {
		return (EAttribute)changeSetRowEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRow_BeforeData() {
		return (EReference)changeSetRowEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRow_AfterData() {
		return (EReference)changeSetRowEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getChangeSetRowData() {
		return changeSetRowDataEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetRowData_PrimaryRecord() {
		return (EAttribute)changeSetRowDataEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRowData_RowDataGroup() {
		return (EReference)changeSetRowDataEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRowData_EventGrouping() {
		return (EReference)changeSetRowDataEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetRowData_VesselName() {
		return (EAttribute)changeSetRowDataEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetRowData_VesselShortName() {
		return (EAttribute)changeSetRowDataEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetRowData_LhsName() {
		return (EAttribute)changeSetRowDataEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetRowData_RhsName() {
		return (EAttribute)changeSetRowDataEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRowData_LhsLink() {
		return (EReference)changeSetRowDataEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRowData_RhsLink() {
		return (EReference)changeSetRowDataEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRowData_LoadSlot() {
		return (EReference)changeSetRowDataEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRowData_DischargeSlot() {
		return (EReference)changeSetRowDataEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRowData_LoadAllocation() {
		return (EReference)changeSetRowDataEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRowData_DischargeAllocation() {
		return (EReference)changeSetRowDataEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRowData_OpenLoadAllocation() {
		return (EReference)changeSetRowDataEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRowData_OpenDischargeAllocation() {
		return (EReference)changeSetRowDataEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRowData_LhsEvent() {
		return (EReference)changeSetRowDataEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRowData_RhsEvent() {
		return (EReference)changeSetRowDataEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRowData_LhsGroupProfitAndLoss() {
		return (EReference)changeSetRowDataEClass.getEStructuralFeatures().get(17);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetRowData_RhsGroupProfitAndLoss() {
		return (EReference)changeSetRowDataEClass.getEStructuralFeatures().get(18);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getChangeSetTableGroup() {
		return changeSetTableGroupEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetTableGroup_Rows() {
		return (EReference)changeSetTableGroupEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetTableGroup_DeltaMetrics() {
		return (EReference)changeSetTableGroupEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetTableGroup_CurrentMetrics() {
		return (EReference)changeSetTableGroupEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetTableGroup_ChangeSet() {
		return (EReference)changeSetTableGroupEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetTableGroup_Description() {
		return (EAttribute)changeSetTableGroupEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getChangeSetTableRow() {
		return changeSetTableRowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetTableRow_LhsName() {
		return (EAttribute)changeSetTableRowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetTableRow_RhsName() {
		return (EAttribute)changeSetTableRowEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetTableRow_LhsBefore() {
		return (EReference)changeSetTableRowEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetTableRow_LhsAfter() {
		return (EReference)changeSetTableRowEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetTableRow_RhsBefore() {
		return (EReference)changeSetTableRowEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetTableRow_RhsAfter() {
		return (EReference)changeSetTableRowEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetTableRow_BeforeVesselName() {
		return (EAttribute)changeSetTableRowEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetTableRow_BeforeVesselShortName() {
		return (EAttribute)changeSetTableRowEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetTableRow_AfterVesselName() {
		return (EAttribute)changeSetTableRowEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetTableRow_AfterVesselShortName() {
		return (EAttribute)changeSetTableRowEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetTableRow_WiringChange() {
		return (EAttribute)changeSetTableRowEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetTableRow_VesselChange() {
		return (EAttribute)changeSetTableRowEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetTableRow_PreviousRHS() {
		return (EReference)changeSetTableRowEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetTableRow_NextLHS() {
		return (EReference)changeSetTableRowEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetTableRow_LhsSlot() {
		return (EAttribute)changeSetTableRowEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetTableRow_LhsSpot() {
		return (EAttribute)changeSetTableRowEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetTableRow_LhsOptional() {
		return (EAttribute)changeSetTableRowEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetTableRow_LhsValid() {
		return (EAttribute)changeSetTableRowEClass.getEStructuralFeatures().get(17);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetTableRow_LhsNonShipped() {
		return (EAttribute)changeSetTableRowEClass.getEStructuralFeatures().get(18);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetTableRow_RhsSlot() {
		return (EAttribute)changeSetTableRowEClass.getEStructuralFeatures().get(19);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetTableRow_RhsSpot() {
		return (EAttribute)changeSetTableRowEClass.getEStructuralFeatures().get(20);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetTableRow_RhsOptional() {
		return (EAttribute)changeSetTableRowEClass.getEStructuralFeatures().get(21);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetTableRow_RhsValid() {
		return (EAttribute)changeSetTableRowEClass.getEStructuralFeatures().get(22);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChangeSetTableRow_RhsNonShipped() {
		return (EAttribute)changeSetTableRowEClass.getEStructuralFeatures().get(23);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getChangeSetTableRoot() {
		return changeSetTableRootEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeSetTableRoot_Groups() {
		return (EReference)changeSetTableRootEClass.getEStructuralFeatures().get(0);
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

		changeSetRowDataGroupEClass = createEClass(CHANGE_SET_ROW_DATA_GROUP);
		createEReference(changeSetRowDataGroupEClass, CHANGE_SET_ROW_DATA_GROUP__MEMBERS);

		changeSetRowEClass = createEClass(CHANGE_SET_ROW);
		createEAttribute(changeSetRowEClass, CHANGE_SET_ROW__WIRING_CHANGE);
		createEAttribute(changeSetRowEClass, CHANGE_SET_ROW__VESSEL_CHANGE);
		createEReference(changeSetRowEClass, CHANGE_SET_ROW__BEFORE_DATA);
		createEReference(changeSetRowEClass, CHANGE_SET_ROW__AFTER_DATA);

		changeSetRowDataEClass = createEClass(CHANGE_SET_ROW_DATA);
		createEAttribute(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__PRIMARY_RECORD);
		createEReference(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__ROW_DATA_GROUP);
		createEReference(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__EVENT_GROUPING);
		createEAttribute(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__VESSEL_NAME);
		createEAttribute(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__VESSEL_SHORT_NAME);
		createEAttribute(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__LHS_NAME);
		createEAttribute(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__RHS_NAME);
		createEReference(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__LHS_LINK);
		createEReference(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__RHS_LINK);
		createEReference(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__LOAD_SLOT);
		createEReference(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__DISCHARGE_SLOT);
		createEReference(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__LOAD_ALLOCATION);
		createEReference(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__DISCHARGE_ALLOCATION);
		createEReference(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__OPEN_LOAD_ALLOCATION);
		createEReference(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__OPEN_DISCHARGE_ALLOCATION);
		createEReference(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__LHS_EVENT);
		createEReference(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__RHS_EVENT);
		createEReference(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__LHS_GROUP_PROFIT_AND_LOSS);
		createEReference(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__RHS_GROUP_PROFIT_AND_LOSS);

		changeSetTableGroupEClass = createEClass(CHANGE_SET_TABLE_GROUP);
		createEReference(changeSetTableGroupEClass, CHANGE_SET_TABLE_GROUP__ROWS);
		createEReference(changeSetTableGroupEClass, CHANGE_SET_TABLE_GROUP__DELTA_METRICS);
		createEReference(changeSetTableGroupEClass, CHANGE_SET_TABLE_GROUP__CURRENT_METRICS);
		createEReference(changeSetTableGroupEClass, CHANGE_SET_TABLE_GROUP__CHANGE_SET);
		createEAttribute(changeSetTableGroupEClass, CHANGE_SET_TABLE_GROUP__DESCRIPTION);

		changeSetTableRowEClass = createEClass(CHANGE_SET_TABLE_ROW);
		createEAttribute(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__LHS_NAME);
		createEAttribute(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__RHS_NAME);
		createEReference(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__LHS_BEFORE);
		createEReference(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__LHS_AFTER);
		createEReference(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__RHS_BEFORE);
		createEReference(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__RHS_AFTER);
		createEAttribute(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__BEFORE_VESSEL_NAME);
		createEAttribute(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__BEFORE_VESSEL_SHORT_NAME);
		createEAttribute(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__AFTER_VESSEL_NAME);
		createEAttribute(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__AFTER_VESSEL_SHORT_NAME);
		createEAttribute(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__WIRING_CHANGE);
		createEAttribute(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__VESSEL_CHANGE);
		createEReference(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__PREVIOUS_RHS);
		createEReference(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__NEXT_LHS);
		createEAttribute(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__LHS_SLOT);
		createEAttribute(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__LHS_SPOT);
		createEAttribute(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__LHS_OPTIONAL);
		createEAttribute(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__LHS_VALID);
		createEAttribute(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__LHS_NON_SHIPPED);
		createEAttribute(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__RHS_SLOT);
		createEAttribute(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__RHS_SPOT);
		createEAttribute(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__RHS_OPTIONAL);
		createEAttribute(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__RHS_VALID);
		createEAttribute(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__RHS_NON_SHIPPED);

		changeSetTableRootEClass = createEClass(CHANGE_SET_TABLE_ROOT);
		createEReference(changeSetTableRootEClass, CHANGE_SET_TABLE_ROOT__GROUPS);

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
		SchedulePackage theSchedulePackage = (SchedulePackage)EPackage.Registry.INSTANCE.getEPackage(SchedulePackage.eNS_URI);
		CargoPackage theCargoPackage = (CargoPackage)EPackage.Registry.INSTANCE.getEPackage(CargoPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes, features, and operations; add parameters
		initEClass(changeSetRootEClass, ChangeSetRoot.class, "ChangeSetRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getChangeSetRoot_ChangeSets(), this.getChangeSet(), null, "changeSets", null, 0, -1, ChangeSetRoot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(changeSetEClass, ChangeSet.class, "ChangeSet", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getChangeSet_MetricsToBase(), this.getDeltaMetrics(), null, "metricsToBase", null, 0, 1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSet_MetricsToPrevious(), this.getDeltaMetrics(), null, "metricsToPrevious", null, 0, 1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
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

		initEClass(changeSetRowDataGroupEClass, ChangeSetRowDataGroup.class, "ChangeSetRowDataGroup", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getChangeSetRowDataGroup_Members(), this.getChangeSetRowData(), this.getChangeSetRowData_RowDataGroup(), "members", null, 0, -1, ChangeSetRowDataGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(changeSetRowEClass, ChangeSetRow.class, "ChangeSetRow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getChangeSetRow_WiringChange(), ecorePackage.getEBoolean(), "wiringChange", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetRow_VesselChange(), ecorePackage.getEBoolean(), "vesselChange", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRow_BeforeData(), this.getChangeSetRowDataGroup(), null, "beforeData", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRow_AfterData(), this.getChangeSetRowDataGroup(), null, "afterData", null, 0, 1, ChangeSetRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(changeSetRowDataEClass, ChangeSetRowData.class, "ChangeSetRowData", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getChangeSetRowData_PrimaryRecord(), ecorePackage.getEBoolean(), "primaryRecord", "true", 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRowData_RowDataGroup(), this.getChangeSetRowDataGroup(), this.getChangeSetRowDataGroup_Members(), "rowDataGroup", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRowData_EventGrouping(), theSchedulePackage.getEventGrouping(), null, "eventGrouping", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetRowData_VesselName(), ecorePackage.getEString(), "vesselName", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetRowData_VesselShortName(), ecorePackage.getEString(), "vesselShortName", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetRowData_LhsName(), ecorePackage.getEString(), "lhsName", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetRowData_RhsName(), ecorePackage.getEString(), "rhsName", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRowData_LhsLink(), this.getChangeSetRowData(), null, "lhsLink", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRowData_RhsLink(), this.getChangeSetRowData(), null, "rhsLink", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRowData_LoadSlot(), theCargoPackage.getLoadSlot(), null, "loadSlot", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRowData_DischargeSlot(), theCargoPackage.getDischargeSlot(), null, "dischargeSlot", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRowData_LoadAllocation(), theSchedulePackage.getSlotAllocation(), null, "loadAllocation", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRowData_DischargeAllocation(), theSchedulePackage.getSlotAllocation(), null, "dischargeAllocation", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRowData_OpenLoadAllocation(), theSchedulePackage.getOpenSlotAllocation(), null, "openLoadAllocation", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRowData_OpenDischargeAllocation(), theSchedulePackage.getOpenSlotAllocation(), null, "openDischargeAllocation", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRowData_LhsEvent(), theSchedulePackage.getEvent(), null, "lhsEvent", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRowData_RhsEvent(), theSchedulePackage.getEvent(), null, "rhsEvent", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRowData_LhsGroupProfitAndLoss(), theSchedulePackage.getProfitAndLossContainer(), null, "lhsGroupProfitAndLoss", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRowData_RhsGroupProfitAndLoss(), theSchedulePackage.getProfitAndLossContainer(), null, "rhsGroupProfitAndLoss", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(changeSetTableGroupEClass, ChangeSetTableGroup.class, "ChangeSetTableGroup", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getChangeSetTableGroup_Rows(), this.getChangeSetTableRow(), null, "rows", null, 0, -1, ChangeSetTableGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetTableGroup_DeltaMetrics(), this.getDeltaMetrics(), null, "deltaMetrics", null, 0, 1, ChangeSetTableGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetTableGroup_CurrentMetrics(), this.getMetrics(), null, "currentMetrics", null, 0, 1, ChangeSetTableGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetTableGroup_ChangeSet(), this.getChangeSet(), null, "changeSet", null, 0, 1, ChangeSetTableGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetTableGroup_Description(), ecorePackage.getEString(), "description", null, 0, 1, ChangeSetTableGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(changeSetTableRowEClass, ChangeSetTableRow.class, "ChangeSetTableRow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getChangeSetTableRow_LhsName(), ecorePackage.getEString(), "lhsName", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetTableRow_RhsName(), ecorePackage.getEString(), "rhsName", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetTableRow_LhsBefore(), this.getChangeSetRowData(), null, "lhsBefore", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetTableRow_LhsAfter(), this.getChangeSetRowData(), null, "lhsAfter", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetTableRow_RhsBefore(), this.getChangeSetRowData(), null, "rhsBefore", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetTableRow_RhsAfter(), this.getChangeSetRowData(), null, "rhsAfter", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetTableRow_BeforeVesselName(), ecorePackage.getEString(), "beforeVesselName", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetTableRow_BeforeVesselShortName(), ecorePackage.getEString(), "beforeVesselShortName", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetTableRow_AfterVesselName(), ecorePackage.getEString(), "afterVesselName", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetTableRow_AfterVesselShortName(), ecorePackage.getEString(), "afterVesselShortName", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetTableRow_WiringChange(), ecorePackage.getEBoolean(), "wiringChange", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetTableRow_VesselChange(), ecorePackage.getEBoolean(), "vesselChange", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetTableRow_PreviousRHS(), this.getChangeSetTableRow(), this.getChangeSetTableRow_NextLHS(), "previousRHS", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetTableRow_NextLHS(), this.getChangeSetTableRow(), this.getChangeSetTableRow_PreviousRHS(), "nextLHS", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetTableRow_LhsSlot(), ecorePackage.getEBoolean(), "lhsSlot", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetTableRow_LhsSpot(), ecorePackage.getEBoolean(), "lhsSpot", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetTableRow_LhsOptional(), ecorePackage.getEBoolean(), "lhsOptional", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetTableRow_LhsValid(), ecorePackage.getEBoolean(), "lhsValid", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetTableRow_LhsNonShipped(), ecorePackage.getEBoolean(), "lhsNonShipped", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetTableRow_RhsSlot(), ecorePackage.getEBoolean(), "rhsSlot", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetTableRow_RhsSpot(), ecorePackage.getEBoolean(), "rhsSpot", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetTableRow_RhsOptional(), ecorePackage.getEBoolean(), "rhsOptional", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetTableRow_RhsValid(), ecorePackage.getEBoolean(), "rhsValid", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetTableRow_RhsNonShipped(), ecorePackage.getEBoolean(), "rhsNonShipped", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(changeSetTableRootEClass, ChangeSetTableRoot.class, "ChangeSetTableRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getChangeSetTableRoot_Groups(), this.getChangeSetTableGroup(), null, "groups", null, 0, -1, ChangeSetTableRoot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize data types
		initEDataType(scenarioResultEDataType, ScenarioResult.class, "ScenarioResult", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //ChangesetPackageImpl
