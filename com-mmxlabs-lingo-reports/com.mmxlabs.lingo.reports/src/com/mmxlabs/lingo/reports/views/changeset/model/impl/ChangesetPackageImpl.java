/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowDataGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetVesselType;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetWiringGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage;
import com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics;
import com.mmxlabs.lingo.reports.views.changeset.model.Metrics;
import com.mmxlabs.models.datetime.DateTimePackage;
import com.mmxlabs.models.lng.analytics.ChangeDescription;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.scenario.service.ScenarioResult;

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
	private EClass changeSetWiringGroupEClass = null;

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
	private EEnum changeSetVesselTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType scenarioResultEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType changeDescriptionEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType userSettingsEDataType = null;

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
		Object registeredChangesetPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		ChangesetPackageImpl theChangesetPackage = registeredChangesetPackage instanceof ChangesetPackageImpl ? (ChangesetPackageImpl)registeredChangesetPackage : new ChangesetPackageImpl();

		isInited = true;

		// Initialize simple dependencies
		CargoPackage.eINSTANCE.eClass();
		CommercialPackage.eINSTANCE.eClass();
		DateTimePackage.eINSTANCE.eClass();
		FleetPackage.eINSTANCE.eClass();
		TypesPackage.eINSTANCE.eClass();
		MMXCorePackage.eINSTANCE.eClass();
		PortPackage.eINSTANCE.eClass();
		PricingPackage.eINSTANCE.eClass();
		SchedulePackage.eINSTANCE.eClass();
		SpotMarketsPackage.eINSTANCE.eClass();

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
	@Override
	public EClass getChangeSetRoot() {
		return changeSetRootEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetRoot_ChangeSets() {
		return (EReference)changeSetRootEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getChangeSet() {
		return changeSetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSet_MetricsToDefaultBase() {
		return (EReference)changeSetEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSet_MetricsToAlternativeBase() {
		return (EReference)changeSetEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSet_BaseScenario() {
		return (EAttribute)changeSetEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSet_CurrentScenario() {
		return (EAttribute)changeSetEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSet_AltBaseScenario() {
		return (EAttribute)changeSetEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSet_AltCurrentScenario() {
		return (EAttribute)changeSetEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSet_ChangeSetRowsToDefaultBase() {
		return (EReference)changeSetEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSet_ChangeSetRowsToAlternativeBase() {
		return (EReference)changeSetEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSet_CurrentMetrics() {
		return (EReference)changeSetEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSet_Description() {
		return (EAttribute)changeSetEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSet_ChangeDescription() {
		return (EAttribute)changeSetEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSet_UserSettings() {
		return (EAttribute)changeSetEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMetrics() {
		return metricsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMetrics_Pnl() {
		return (EAttribute)metricsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMetrics_Lateness() {
		return (EAttribute)metricsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMetrics_Capacity() {
		return (EAttribute)metricsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDeltaMetrics() {
		return deltaMetricsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDeltaMetrics_PnlDelta() {
		return (EAttribute)deltaMetricsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDeltaMetrics_LatenessDelta() {
		return (EAttribute)deltaMetricsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDeltaMetrics_CapacityDelta() {
		return (EAttribute)deltaMetricsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getChangeSetRowDataGroup() {
		return changeSetRowDataGroupEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetRowDataGroup_Members() {
		return (EReference)changeSetRowDataGroupEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getChangeSetWiringGroup() {
		return changeSetWiringGroupEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetWiringGroup_Rows() {
		return (EReference)changeSetWiringGroupEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getChangeSetWiringGroup__IsWiringChange() {
		return changeSetWiringGroupEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getChangeSetWiringGroup__IsVesselChange() {
		return changeSetWiringGroupEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getChangeSetWiringGroup__IsDateChange() {
		return changeSetWiringGroupEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getChangeSetRowData() {
		return changeSetRowDataEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSetRowData_PrimaryRecord() {
		return (EAttribute)changeSetRowDataEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetRowData_RowDataGroup() {
		return (EReference)changeSetRowDataEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetRowData_EventGrouping() {
		return (EReference)changeSetRowDataEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSetRowData_VesselName() {
		return (EAttribute)changeSetRowDataEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSetRowData_VesselShortName() {
		return (EAttribute)changeSetRowDataEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSetRowData_VesselType() {
		return (EAttribute)changeSetRowDataEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSetRowData_LhsName() {
		return (EAttribute)changeSetRowDataEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSetRowData_RhsName() {
		return (EAttribute)changeSetRowDataEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetRowData_LhsLink() {
		return (EReference)changeSetRowDataEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetRowData_RhsLink() {
		return (EReference)changeSetRowDataEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSetRowData_LhsSlot() {
		return (EAttribute)changeSetRowDataEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSetRowData_LhsSpot() {
		return (EAttribute)changeSetRowDataEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSetRowData_LhsOptional() {
		return (EAttribute)changeSetRowDataEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSetRowData_RhsSlot() {
		return (EAttribute)changeSetRowDataEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSetRowData_RhsSpot() {
		return (EAttribute)changeSetRowDataEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSetRowData_RhsOptional() {
		return (EAttribute)changeSetRowDataEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSetRowData_LhsNonShipped() {
		return (EAttribute)changeSetRowDataEClass.getEStructuralFeatures().get(17);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSetRowData_RhsNonShipped() {
		return (EAttribute)changeSetRowDataEClass.getEStructuralFeatures().get(18);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetRowData_LoadSlot() {
		return (EReference)changeSetRowDataEClass.getEStructuralFeatures().get(19);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetRowData_DischargeSlot() {
		return (EReference)changeSetRowDataEClass.getEStructuralFeatures().get(20);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetRowData_LoadAllocation() {
		return (EReference)changeSetRowDataEClass.getEStructuralFeatures().get(21);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetRowData_DischargeAllocation() {
		return (EReference)changeSetRowDataEClass.getEStructuralFeatures().get(22);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetRowData_OpenLoadAllocation() {
		return (EReference)changeSetRowDataEClass.getEStructuralFeatures().get(23);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetRowData_OpenDischargeAllocation() {
		return (EReference)changeSetRowDataEClass.getEStructuralFeatures().get(24);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetRowData_LhsEvent() {
		return (EReference)changeSetRowDataEClass.getEStructuralFeatures().get(25);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetRowData_RhsEvent() {
		return (EReference)changeSetRowDataEClass.getEStructuralFeatures().get(26);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetRowData_LhsGroupProfitAndLoss() {
		return (EReference)changeSetRowDataEClass.getEStructuralFeatures().get(27);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetRowData_RhsGroupProfitAndLoss() {
		return (EReference)changeSetRowDataEClass.getEStructuralFeatures().get(28);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetRowData_PaperDealAllocation() {
		return (EReference)changeSetRowDataEClass.getEStructuralFeatures().get(29);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSetRowData_VesselCharterNumber() {
		return (EAttribute)changeSetRowDataEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getChangeSetTableGroup() {
		return changeSetTableGroupEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetTableGroup_Rows() {
		return (EReference)changeSetTableGroupEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetTableGroup_DeltaMetrics() {
		return (EReference)changeSetTableGroupEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetTableGroup_CurrentMetrics() {
		return (EReference)changeSetTableGroupEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetTableGroup_ChangeSet() {
		return (EReference)changeSetTableGroupEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSetTableGroup_Description() {
		return (EAttribute)changeSetTableGroupEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSetTableGroup_BaseScenario() {
		return (EAttribute)changeSetTableGroupEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSetTableGroup_CurrentScenario() {
		return (EAttribute)changeSetTableGroupEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetTableGroup_LinkedGroup() {
		return (EReference)changeSetTableGroupEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSetTableGroup_Complexity() {
		return (EAttribute)changeSetTableGroupEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSetTableGroup_SortValue() {
		return (EAttribute)changeSetTableGroupEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSetTableGroup_GroupSortValue() {
		return (EAttribute)changeSetTableGroupEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSetTableGroup_GroupObject() {
		return (EAttribute)changeSetTableGroupEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSetTableGroup_GroupAlternative() {
		return (EAttribute)changeSetTableGroupEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetTableGroup_TableRoot() {
		return (EReference)changeSetTableGroupEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getChangeSetTableRow() {
		return changeSetTableRowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetTableRow_LhsBefore() {
		return (EReference)changeSetTableRowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetTableRow_LhsAfter() {
		return (EReference)changeSetTableRowEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSetTableRow_WiringChange() {
		return (EAttribute)changeSetTableRowEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSetTableRow_VesselChange() {
		return (EAttribute)changeSetTableRowEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSetTableRow_DateChange() {
		return (EAttribute)changeSetTableRowEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSetTableRow_LhsValid() {
		return (EAttribute)changeSetTableRowEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetTableRow_CurrentRhsBefore() {
		return (EReference)changeSetTableRowEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetTableRow_CurrentRhsAfter() {
		return (EReference)changeSetTableRowEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetTableRow_PreviousRhsBefore() {
		return (EReference)changeSetTableRowEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetTableRow_PreviousRhsAfter() {
		return (EReference)changeSetTableRowEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChangeSetTableRow_CurrentRhsValid() {
		return (EAttribute)changeSetTableRowEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetTableRow_WiringGroup() {
		return (EReference)changeSetTableRowEClass.getEStructuralFeatures().get(11);
	}
 
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetTableRow_TableGroup() {
		return (EReference)changeSetTableRowEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getChangeSetTableRow__IsMajorChange() {
		return changeSetTableRowEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getChangeSetTableRow__GetLHSAfterOrBefore() {
		return changeSetTableRowEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getChangeSetTableRow__GetCurrentRHSAfterOrBefore() {
		return changeSetTableRowEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getChangeSetTableRow__GetLhsName() {
		return changeSetTableRowEClass.getEOperations().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getChangeSetTableRow__GetCurrentRhsName() {
		return changeSetTableRowEClass.getEOperations().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getChangeSetTableRow__GetBeforeVesselCharterNumber() {
		return changeSetTableRowEClass.getEOperations().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getChangeSetTableRow__GetBeforeVesselName() {
		return changeSetTableRowEClass.getEOperations().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getChangeSetTableRow__GetBeforeVesselShortName() {
		return changeSetTableRowEClass.getEOperations().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getChangeSetTableRow__GetBeforeVesselType() {
		return changeSetTableRowEClass.getEOperations().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getChangeSetTableRow__GetAfterVesselCharterNumber() {
		return changeSetTableRowEClass.getEOperations().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getChangeSetTableRow__GetAfterVesselName() {
		return changeSetTableRowEClass.getEOperations().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getChangeSetTableRow__GetAfterVesselShortName() {
		return changeSetTableRowEClass.getEOperations().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getChangeSetTableRow__GetAfterVesselType() {
		return changeSetTableRowEClass.getEOperations().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getChangeSetTableRow__IsLhsSlot() {
		return changeSetTableRowEClass.getEOperations().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getChangeSetTableRow__IsCurrentRhsSlot() {
		return changeSetTableRowEClass.getEOperations().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getChangeSetTableRow__IsLhsSpot() {
		return changeSetTableRowEClass.getEOperations().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getChangeSetTableRow__IsCurrentRhsOptional() {
		return changeSetTableRowEClass.getEOperations().get(16);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getChangeSetTableRow__IsLhsOptional() {
		return changeSetTableRowEClass.getEOperations().get(17);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getChangeSetTableRow__IsCurrentRhsSpot() {
		return changeSetTableRowEClass.getEOperations().get(18);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getChangeSetTableRow__IsLhsNonShipped() {
		return changeSetTableRowEClass.getEOperations().get(19);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getChangeSetTableRow__IsCurrentRhsNonShipped() {
		return changeSetTableRowEClass.getEOperations().get(20);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getChangeSetTableRoot() {
		return changeSetTableRootEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangeSetTableRoot_Groups() {
		return (EReference)changeSetTableRootEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getChangeSetVesselType() {
		return changeSetVesselTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getScenarioResult() {
		return scenarioResultEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getChangeDescription() {
		return changeDescriptionEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getUserSettings() {
		return userSettingsEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
		createEReference(changeSetEClass, CHANGE_SET__METRICS_TO_DEFAULT_BASE);
		createEReference(changeSetEClass, CHANGE_SET__METRICS_TO_ALTERNATIVE_BASE);
		createEAttribute(changeSetEClass, CHANGE_SET__BASE_SCENARIO);
		createEAttribute(changeSetEClass, CHANGE_SET__CURRENT_SCENARIO);
		createEAttribute(changeSetEClass, CHANGE_SET__ALT_BASE_SCENARIO);
		createEAttribute(changeSetEClass, CHANGE_SET__ALT_CURRENT_SCENARIO);
		createEReference(changeSetEClass, CHANGE_SET__CHANGE_SET_ROWS_TO_DEFAULT_BASE);
		createEReference(changeSetEClass, CHANGE_SET__CHANGE_SET_ROWS_TO_ALTERNATIVE_BASE);
		createEReference(changeSetEClass, CHANGE_SET__CURRENT_METRICS);
		createEAttribute(changeSetEClass, CHANGE_SET__DESCRIPTION);
		createEAttribute(changeSetEClass, CHANGE_SET__CHANGE_DESCRIPTION);
		createEAttribute(changeSetEClass, CHANGE_SET__USER_SETTINGS);

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

		changeSetWiringGroupEClass = createEClass(CHANGE_SET_WIRING_GROUP);
		createEReference(changeSetWiringGroupEClass, CHANGE_SET_WIRING_GROUP__ROWS);
		createEOperation(changeSetWiringGroupEClass, CHANGE_SET_WIRING_GROUP___IS_WIRING_CHANGE);
		createEOperation(changeSetWiringGroupEClass, CHANGE_SET_WIRING_GROUP___IS_VESSEL_CHANGE);
		createEOperation(changeSetWiringGroupEClass, CHANGE_SET_WIRING_GROUP___IS_DATE_CHANGE);

		changeSetRowDataEClass = createEClass(CHANGE_SET_ROW_DATA);
		createEAttribute(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__PRIMARY_RECORD);
		createEReference(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__ROW_DATA_GROUP);
		createEReference(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__EVENT_GROUPING);
		createEAttribute(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__VESSEL_NAME);
		createEAttribute(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__VESSEL_SHORT_NAME);
		createEAttribute(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__VESSEL_TYPE);
		createEAttribute(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__VESSEL_CHARTER_NUMBER);
		createEAttribute(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__LHS_NAME);
		createEAttribute(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__RHS_NAME);
		createEReference(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__LHS_LINK);
		createEReference(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__RHS_LINK);
		createEAttribute(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__LHS_SLOT);
		createEAttribute(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__LHS_SPOT);
		createEAttribute(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__LHS_OPTIONAL);
		createEAttribute(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__RHS_SLOT);
		createEAttribute(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__RHS_SPOT);
		createEAttribute(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__RHS_OPTIONAL);
		createEAttribute(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__LHS_NON_SHIPPED);
		createEAttribute(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__RHS_NON_SHIPPED);
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
		createEReference(changeSetRowDataEClass, CHANGE_SET_ROW_DATA__PAPER_DEAL_ALLOCATION);

		changeSetTableGroupEClass = createEClass(CHANGE_SET_TABLE_GROUP);
		createEReference(changeSetTableGroupEClass, CHANGE_SET_TABLE_GROUP__ROWS);
		createEReference(changeSetTableGroupEClass, CHANGE_SET_TABLE_GROUP__DELTA_METRICS);
		createEReference(changeSetTableGroupEClass, CHANGE_SET_TABLE_GROUP__CURRENT_METRICS);
		createEReference(changeSetTableGroupEClass, CHANGE_SET_TABLE_GROUP__CHANGE_SET);
		createEAttribute(changeSetTableGroupEClass, CHANGE_SET_TABLE_GROUP__DESCRIPTION);
		createEAttribute(changeSetTableGroupEClass, CHANGE_SET_TABLE_GROUP__BASE_SCENARIO);
		createEAttribute(changeSetTableGroupEClass, CHANGE_SET_TABLE_GROUP__CURRENT_SCENARIO);
		createEReference(changeSetTableGroupEClass, CHANGE_SET_TABLE_GROUP__LINKED_GROUP);
		createEAttribute(changeSetTableGroupEClass, CHANGE_SET_TABLE_GROUP__COMPLEXITY);
		createEAttribute(changeSetTableGroupEClass, CHANGE_SET_TABLE_GROUP__SORT_VALUE);
		createEAttribute(changeSetTableGroupEClass, CHANGE_SET_TABLE_GROUP__GROUP_SORT_VALUE);
		createEAttribute(changeSetTableGroupEClass, CHANGE_SET_TABLE_GROUP__GROUP_OBJECT);
		createEAttribute(changeSetTableGroupEClass, CHANGE_SET_TABLE_GROUP__GROUP_ALTERNATIVE);
		createEReference(changeSetTableGroupEClass, CHANGE_SET_TABLE_GROUP__TABLE_ROOT);

		changeSetTableRowEClass = createEClass(CHANGE_SET_TABLE_ROW);
		createEReference(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__LHS_BEFORE);
		createEReference(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__LHS_AFTER);
		createEAttribute(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__LHS_VALID);
		createEReference(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__CURRENT_RHS_BEFORE);
		createEReference(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__CURRENT_RHS_AFTER);
		createEReference(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__PREVIOUS_RHS_BEFORE);
		createEReference(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__PREVIOUS_RHS_AFTER);
		createEAttribute(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__CURRENT_RHS_VALID);
		createEAttribute(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__WIRING_CHANGE);
		createEAttribute(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__VESSEL_CHANGE);
		createEAttribute(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__DATE_CHANGE);
		createEReference(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__WIRING_GROUP);
		createEReference(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW__TABLE_GROUP);
		createEOperation(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW___IS_MAJOR_CHANGE);
		createEOperation(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW___GET_LHS_AFTER_OR_BEFORE);
		createEOperation(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW___GET_CURRENT_RHS_AFTER_OR_BEFORE);
		createEOperation(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW___GET_LHS_NAME);
		createEOperation(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW___GET_CURRENT_RHS_NAME);
		createEOperation(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW___GET_BEFORE_VESSEL_CHARTER_NUMBER);
		createEOperation(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW___GET_BEFORE_VESSEL_NAME);
		createEOperation(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW___GET_BEFORE_VESSEL_SHORT_NAME);
		createEOperation(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW___GET_BEFORE_VESSEL_TYPE);
		createEOperation(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW___GET_AFTER_VESSEL_CHARTER_NUMBER);
		createEOperation(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW___GET_AFTER_VESSEL_NAME);
		createEOperation(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW___GET_AFTER_VESSEL_SHORT_NAME);
		createEOperation(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW___GET_AFTER_VESSEL_TYPE);
		createEOperation(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW___IS_LHS_SLOT);
		createEOperation(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW___IS_CURRENT_RHS_SLOT);
		createEOperation(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW___IS_LHS_SPOT);
		createEOperation(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW___IS_CURRENT_RHS_OPTIONAL);
		createEOperation(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW___IS_LHS_OPTIONAL);
		createEOperation(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW___IS_CURRENT_RHS_SPOT);
		createEOperation(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW___IS_LHS_NON_SHIPPED);
		createEOperation(changeSetTableRowEClass, CHANGE_SET_TABLE_ROW___IS_CURRENT_RHS_NON_SHIPPED);

		changeSetTableRootEClass = createEClass(CHANGE_SET_TABLE_ROOT);
		createEReference(changeSetTableRootEClass, CHANGE_SET_TABLE_ROOT__GROUPS);

		// Create enums
		changeSetVesselTypeEEnum = createEEnum(CHANGE_SET_VESSEL_TYPE);

		// Create data types
		scenarioResultEDataType = createEDataType(SCENARIO_RESULT);
		changeDescriptionEDataType = createEDataType(CHANGE_DESCRIPTION);
		userSettingsEDataType = createEDataType(USER_SETTINGS);
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
		initEReference(getChangeSet_MetricsToDefaultBase(), this.getDeltaMetrics(), null, "metricsToDefaultBase", null, 0, 1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSet_MetricsToAlternativeBase(), this.getDeltaMetrics(), null, "metricsToAlternativeBase", null, 0, 1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSet_BaseScenario(), this.getScenarioResult(), "baseScenario", null, 0, 1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSet_CurrentScenario(), this.getScenarioResult(), "currentScenario", null, 0, 1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSet_AltBaseScenario(), this.getScenarioResult(), "altBaseScenario", null, 0, 1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSet_AltCurrentScenario(), this.getScenarioResult(), "altCurrentScenario", null, 0, 1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSet_ChangeSetRowsToDefaultBase(), this.getChangeSetTableRow(), null, "changeSetRowsToDefaultBase", null, 0, -1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSet_ChangeSetRowsToAlternativeBase(), this.getChangeSetTableRow(), null, "changeSetRowsToAlternativeBase", null, 0, -1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSet_CurrentMetrics(), this.getMetrics(), null, "currentMetrics", null, 0, 1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSet_Description(), ecorePackage.getEString(), "description", null, 0, 1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSet_ChangeDescription(), this.getChangeDescription(), "changeDescription", null, 0, 1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSet_UserSettings(), this.getUserSettings(), "userSettings", null, 0, 1, ChangeSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(metricsEClass, Metrics.class, "Metrics", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMetrics_Pnl(), ecorePackage.getELong(), "pnl", null, 0, 1, Metrics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMetrics_Lateness(), ecorePackage.getEInt(), "lateness", null, 0, 1, Metrics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMetrics_Capacity(), ecorePackage.getEInt(), "capacity", null, 0, 1, Metrics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(deltaMetricsEClass, DeltaMetrics.class, "DeltaMetrics", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDeltaMetrics_PnlDelta(), ecorePackage.getELong(), "pnlDelta", null, 0, 1, DeltaMetrics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDeltaMetrics_LatenessDelta(), ecorePackage.getEInt(), "latenessDelta", null, 0, 1, DeltaMetrics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDeltaMetrics_CapacityDelta(), ecorePackage.getEInt(), "capacityDelta", null, 0, 1, DeltaMetrics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(changeSetRowDataGroupEClass, ChangeSetRowDataGroup.class, "ChangeSetRowDataGroup", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getChangeSetRowDataGroup_Members(), this.getChangeSetRowData(), this.getChangeSetRowData_RowDataGroup(), "members", null, 0, -1, ChangeSetRowDataGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(changeSetWiringGroupEClass, ChangeSetWiringGroup.class, "ChangeSetWiringGroup", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getChangeSetWiringGroup_Rows(), this.getChangeSetTableRow(), this.getChangeSetTableRow_WiringGroup(), "rows", null, 0, -1, ChangeSetWiringGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getChangeSetWiringGroup__IsWiringChange(), ecorePackage.getEBoolean(), "isWiringChange", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getChangeSetWiringGroup__IsVesselChange(), ecorePackage.getEBoolean(), "isVesselChange", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getChangeSetWiringGroup__IsDateChange(), ecorePackage.getEBoolean(), "isDateChange", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(changeSetRowDataEClass, ChangeSetRowData.class, "ChangeSetRowData", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getChangeSetRowData_PrimaryRecord(), ecorePackage.getEBoolean(), "primaryRecord", "true", 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRowData_RowDataGroup(), this.getChangeSetRowDataGroup(), this.getChangeSetRowDataGroup_Members(), "rowDataGroup", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRowData_EventGrouping(), theSchedulePackage.getEventGrouping(), null, "eventGrouping", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetRowData_VesselName(), ecorePackage.getEString(), "vesselName", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetRowData_VesselShortName(), ecorePackage.getEString(), "vesselShortName", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetRowData_VesselType(), this.getChangeSetVesselType(), "vesselType", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetRowData_VesselCharterNumber(), ecorePackage.getEInt(), "vesselCharterNumber", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetRowData_LhsName(), ecorePackage.getEString(), "lhsName", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetRowData_RhsName(), ecorePackage.getEString(), "rhsName", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRowData_LhsLink(), this.getChangeSetRowData(), null, "lhsLink", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetRowData_RhsLink(), this.getChangeSetRowData(), null, "rhsLink", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetRowData_LhsSlot(), ecorePackage.getEBoolean(), "lhsSlot", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetRowData_LhsSpot(), ecorePackage.getEBoolean(), "lhsSpot", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetRowData_LhsOptional(), ecorePackage.getEBoolean(), "lhsOptional", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetRowData_RhsSlot(), ecorePackage.getEBoolean(), "rhsSlot", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetRowData_RhsSpot(), ecorePackage.getEBoolean(), "rhsSpot", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetRowData_RhsOptional(), ecorePackage.getEBoolean(), "rhsOptional", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetRowData_LhsNonShipped(), ecorePackage.getEBoolean(), "lhsNonShipped", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetRowData_RhsNonShipped(), ecorePackage.getEBoolean(), "rhsNonShipped", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
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
		initEReference(getChangeSetRowData_PaperDealAllocation(), theSchedulePackage.getPaperDealAllocation(), null, "paperDealAllocation", null, 0, 1, ChangeSetRowData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(changeSetTableGroupEClass, ChangeSetTableGroup.class, "ChangeSetTableGroup", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getChangeSetTableGroup_Rows(), this.getChangeSetTableRow(), this.getChangeSetTableRow_TableGroup(), "rows", null, 0, -1, ChangeSetTableGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetTableGroup_DeltaMetrics(), this.getDeltaMetrics(), null, "deltaMetrics", null, 0, 1, ChangeSetTableGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetTableGroup_CurrentMetrics(), this.getMetrics(), null, "currentMetrics", null, 0, 1, ChangeSetTableGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetTableGroup_ChangeSet(), this.getChangeSet(), null, "changeSet", null, 0, 1, ChangeSetTableGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetTableGroup_Description(), ecorePackage.getEString(), "description", null, 0, 1, ChangeSetTableGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetTableGroup_BaseScenario(), this.getScenarioResult(), "baseScenario", null, 0, 1, ChangeSetTableGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetTableGroup_CurrentScenario(), this.getScenarioResult(), "currentScenario", null, 0, 1, ChangeSetTableGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetTableGroup_LinkedGroup(), this.getChangeSetTableGroup(), null, "linkedGroup", null, 0, 1, ChangeSetTableGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetTableGroup_Complexity(), ecorePackage.getEInt(), "complexity", null, 0, 1, ChangeSetTableGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetTableGroup_SortValue(), ecorePackage.getEDouble(), "sortValue", null, 0, 1, ChangeSetTableGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetTableGroup_GroupSortValue(), ecorePackage.getEDouble(), "groupSortValue", null, 0, 1, ChangeSetTableGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetTableGroup_GroupObject(), ecorePackage.getEJavaObject(), "groupObject", null, 0, 1, ChangeSetTableGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetTableGroup_GroupAlternative(), ecorePackage.getEBoolean(), "groupAlternative", null, 0, 1, ChangeSetTableGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetTableGroup_TableRoot(), this.getChangeSetTableRoot(), this.getChangeSetTableRoot_Groups(), "tableRoot", null, 0, 1, ChangeSetTableGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(changeSetTableRowEClass, ChangeSetTableRow.class, "ChangeSetTableRow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getChangeSetTableRow_LhsBefore(), this.getChangeSetRowData(), null, "lhsBefore", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetTableRow_LhsAfter(), this.getChangeSetRowData(), null, "lhsAfter", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetTableRow_LhsValid(), ecorePackage.getEBoolean(), "lhsValid", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetTableRow_CurrentRhsBefore(), this.getChangeSetRowData(), null, "currentRhsBefore", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetTableRow_CurrentRhsAfter(), this.getChangeSetRowData(), null, "currentRhsAfter", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetTableRow_PreviousRhsBefore(), this.getChangeSetRowData(), null, "previousRhsBefore", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetTableRow_PreviousRhsAfter(), this.getChangeSetRowData(), null, "previousRhsAfter", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetTableRow_CurrentRhsValid(), ecorePackage.getEBoolean(), "currentRhsValid", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetTableRow_WiringChange(), ecorePackage.getEBoolean(), "wiringChange", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetTableRow_VesselChange(), ecorePackage.getEBoolean(), "vesselChange", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChangeSetTableRow_DateChange(), ecorePackage.getEBoolean(), "dateChange", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetTableRow_WiringGroup(), this.getChangeSetWiringGroup(), this.getChangeSetWiringGroup_Rows(), "wiringGroup", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangeSetTableRow_TableGroup(), this.getChangeSetTableGroup(), this.getChangeSetTableGroup_Rows(), "tableGroup", null, 0, 1, ChangeSetTableRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getChangeSetTableRow__IsMajorChange(), ecorePackage.getEBoolean(), "isMajorChange", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getChangeSetTableRow__GetLHSAfterOrBefore(), this.getChangeSetRowData(), "getLHSAfterOrBefore", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getChangeSetTableRow__GetCurrentRHSAfterOrBefore(), this.getChangeSetRowData(), "getCurrentRHSAfterOrBefore", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getChangeSetTableRow__GetLhsName(), ecorePackage.getEString(), "getLhsName", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getChangeSetTableRow__GetCurrentRhsName(), ecorePackage.getEString(), "getCurrentRhsName", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getChangeSetTableRow__GetBeforeVesselCharterNumber(), ecorePackage.getEInt(), "getBeforeVesselCharterNumber", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getChangeSetTableRow__GetBeforeVesselName(), ecorePackage.getEString(), "getBeforeVesselName", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getChangeSetTableRow__GetBeforeVesselShortName(), ecorePackage.getEString(), "getBeforeVesselShortName", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getChangeSetTableRow__GetBeforeVesselType(), this.getChangeSetVesselType(), "getBeforeVesselType", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getChangeSetTableRow__GetAfterVesselCharterNumber(), ecorePackage.getEInt(), "getAfterVesselCharterNumber", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getChangeSetTableRow__GetAfterVesselName(), ecorePackage.getEString(), "getAfterVesselName", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getChangeSetTableRow__GetAfterVesselShortName(), ecorePackage.getEString(), "getAfterVesselShortName", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getChangeSetTableRow__GetAfterVesselType(), this.getChangeSetVesselType(), "getAfterVesselType", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getChangeSetTableRow__IsLhsSlot(), ecorePackage.getEBoolean(), "isLhsSlot", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getChangeSetTableRow__IsCurrentRhsSlot(), ecorePackage.getEBoolean(), "isCurrentRhsSlot", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getChangeSetTableRow__IsLhsSpot(), ecorePackage.getEBoolean(), "isLhsSpot", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getChangeSetTableRow__IsCurrentRhsOptional(), ecorePackage.getEBoolean(), "isCurrentRhsOptional", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getChangeSetTableRow__IsLhsOptional(), ecorePackage.getEBoolean(), "isLhsOptional", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getChangeSetTableRow__IsCurrentRhsSpot(), ecorePackage.getEBoolean(), "isCurrentRhsSpot", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getChangeSetTableRow__IsLhsNonShipped(), ecorePackage.getEBoolean(), "isLhsNonShipped", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getChangeSetTableRow__IsCurrentRhsNonShipped(), ecorePackage.getEBoolean(), "isCurrentRhsNonShipped", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(changeSetTableRootEClass, ChangeSetTableRoot.class, "ChangeSetTableRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getChangeSetTableRoot_Groups(), this.getChangeSetTableGroup(), this.getChangeSetTableGroup_TableRoot(), "groups", null, 0, -1, ChangeSetTableRoot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(changeSetVesselTypeEEnum, ChangeSetVesselType.class, "ChangeSetVesselType");
		addEEnumLiteral(changeSetVesselTypeEEnum, ChangeSetVesselType.FLEET);
		addEEnumLiteral(changeSetVesselTypeEEnum, ChangeSetVesselType.MARKET);
		addEEnumLiteral(changeSetVesselTypeEEnum, ChangeSetVesselType.NOMINAL);
		addEEnumLiteral(changeSetVesselTypeEEnum, ChangeSetVesselType.FOB);
		addEEnumLiteral(changeSetVesselTypeEEnum, ChangeSetVesselType.DES);

		// Initialize data types
		initEDataType(scenarioResultEDataType, ScenarioResult.class, "ScenarioResult", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(changeDescriptionEDataType, ChangeDescription.class, "ChangeDescription", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(userSettingsEDataType, UserSettings.class, "UserSettings", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //ChangesetPackageImpl
