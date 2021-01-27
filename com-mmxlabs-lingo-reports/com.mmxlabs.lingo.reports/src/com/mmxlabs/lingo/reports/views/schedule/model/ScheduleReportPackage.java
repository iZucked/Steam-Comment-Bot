/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.schedule.model;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportFactory
 * @model kind="package"
 * @generated
 */
public interface ScheduleReportPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "model";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.mmxlabs.com/models/lingo/reports/schedule/1";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "reports.schedule";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ScheduleReportPackage eINSTANCE = com.mmxlabs.lingo.reports.views.schedule.model.impl.ScheduleReportPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.RowImpl <em>Row</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.RowImpl
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.ScheduleReportPackageImpl#getRow()
	 * @generated
	 */
	int ROW = 0;

	/**
	 * The feature id for the '<em><b>Scenario Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__SCENARIO_NAME = 0;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__VISIBLE = 1;

	/**
	 * The feature id for the '<em><b>Input Equivalents</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__INPUT_EQUIVALENTS = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__NAME = 3;

	/**
	 * The feature id for the '<em><b>Name2</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__NAME2 = 4;

	/**
	 * The feature id for the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__TARGET = 5;

	/**
	 * The feature id for the '<em><b>Schedule</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__SCHEDULE = 6;

	/**
	 * The feature id for the '<em><b>Sequence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__SEQUENCE = 7;

	/**
	 * The feature id for the '<em><b>Cargo Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__CARGO_ALLOCATION = 8;

	/**
	 * The feature id for the '<em><b>Load Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__LOAD_ALLOCATION = 9;

	/**
	 * The feature id for the '<em><b>Discharge Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__DISCHARGE_ALLOCATION = 10;

	/**
	 * The feature id for the '<em><b>Open Load Slot Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__OPEN_LOAD_SLOT_ALLOCATION = 11;

	/**
	 * The feature id for the '<em><b>Open Discharge Slot Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__OPEN_DISCHARGE_SLOT_ALLOCATION = 12;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__REFERENCE = 13;

	/**
	 * The feature id for the '<em><b>Lhs Link</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__LHS_LINK = 14;

	/**
	 * The feature id for the '<em><b>Rhs Link</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__RHS_LINK = 15;

	/**
	 * The feature id for the '<em><b>Row Group</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__ROW_GROUP = 16;

	/**
	 * The feature id for the '<em><b>Scenario Data Provider</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__SCENARIO_DATA_PROVIDER = 17;

	/**
	 * The number of structural features of the '<em>Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW_FEATURE_COUNT = 18;

	/**
	 * The number of operations of the '<em>Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.RowGroupImpl <em>Row Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.RowGroupImpl
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.ScheduleReportPackageImpl#getRowGroup()
	 * @generated
	 */
	int ROW_GROUP = 1;

	/**
	 * The feature id for the '<em><b>Rows</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW_GROUP__ROWS = 0;

	/**
	 * The number of structural features of the '<em>Row Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW_GROUP_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Row Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW_GROUP_OPERATION_COUNT = 0;


	/**
	 * The meta object id for the '{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.DiffOptionsImpl <em>Diff Options</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.DiffOptionsImpl
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.ScheduleReportPackageImpl#getDiffOptions()
	 * @generated
	 */
	int DIFF_OPTIONS = 2;

	/**
	 * The feature id for the '<em><b>Filter Selected Elements</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_OPTIONS__FILTER_SELECTED_ELEMENTS = 0;

	/**
	 * The feature id for the '<em><b>Filter Selected Sequences</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_OPTIONS__FILTER_SELECTED_SEQUENCES = 1;

	/**
	 * The number of structural features of the '<em>Diff Options</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_OPTIONS_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Diff Options</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFF_OPTIONS_OPERATION_COUNT = 0;


	/**
	 * The meta object id for the '{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.CompositeRowImpl <em>Composite Row</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.CompositeRowImpl
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.ScheduleReportPackageImpl#getCompositeRow()
	 * @generated
	 */
	int COMPOSITE_ROW = 3;

	/**
	 * The feature id for the '<em><b>Previous Row</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_ROW__PREVIOUS_ROW = 0;

	/**
	 * The feature id for the '<em><b>Pinned Row</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_ROW__PINNED_ROW = 1;

	/**
	 * The number of structural features of the '<em>Composite Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_ROW_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Composite Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_ROW_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.lingo.reports.views.schedule.model.ChangeType <em>Change Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ChangeType
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.ScheduleReportPackageImpl#getChangeType()
	 * @generated
	 */
	int CHANGE_TYPE = 4;


	/**
	 * The meta object id for the '<em>IScenario Data Provider</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.ScheduleReportPackageImpl#getIScenarioDataProvider()
	 * @generated
	 */
	int ISCENARIO_DATA_PROVIDER = 5;


	/**
	 * Returns the meta object for class '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row <em>Row</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Row</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Row
	 * @generated
	 */
	EClass getRow();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getScenarioName <em>Scenario Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Scenario Name</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Row#getScenarioName()
	 * @see #getRow()
	 * @generated
	 */
	EAttribute getRow_ScenarioName();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#isVisible <em>Visible</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Visible</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Row#isVisible()
	 * @see #getRow()
	 * @generated
	 */
	EAttribute getRow_Visible();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getInputEquivalents <em>Input Equivalents</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Input Equivalents</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Row#getInputEquivalents()
	 * @see #getRow()
	 * @generated
	 */
	EReference getRow_InputEquivalents();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Row#getName()
	 * @see #getRow()
	 * @generated
	 */
	EAttribute getRow_Name();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getName2 <em>Name2</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name2</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Row#getName2()
	 * @see #getRow()
	 * @generated
	 */
	EAttribute getRow_Name2();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Target</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Row#getTarget()
	 * @see #getRow()
	 * @generated
	 */
	EReference getRow_Target();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getSchedule <em>Schedule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Schedule</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Row#getSchedule()
	 * @see #getRow()
	 * @generated
	 */
	EReference getRow_Schedule();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getSequence <em>Sequence</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Sequence</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Row#getSequence()
	 * @see #getRow()
	 * @generated
	 */
	EReference getRow_Sequence();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getCargoAllocation <em>Cargo Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Cargo Allocation</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Row#getCargoAllocation()
	 * @see #getRow()
	 * @generated
	 */
	EReference getRow_CargoAllocation();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getLoadAllocation <em>Load Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Load Allocation</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Row#getLoadAllocation()
	 * @see #getRow()
	 * @generated
	 */
	EReference getRow_LoadAllocation();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getDischargeAllocation <em>Discharge Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Discharge Allocation</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Row#getDischargeAllocation()
	 * @see #getRow()
	 * @generated
	 */
	EReference getRow_DischargeAllocation();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getOpenLoadSlotAllocation <em>Open Load Slot Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Open Load Slot Allocation</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Row#getOpenLoadSlotAllocation()
	 * @see #getRow()
	 * @generated
	 */
	EReference getRow_OpenLoadSlotAllocation();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getOpenDischargeSlotAllocation <em>Open Discharge Slot Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Open Discharge Slot Allocation</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Row#getOpenDischargeSlotAllocation()
	 * @see #getRow()
	 * @generated
	 */
	EReference getRow_OpenDischargeSlotAllocation();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#isReference <em>Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Reference</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Row#isReference()
	 * @see #getRow()
	 * @generated
	 */
	EAttribute getRow_Reference();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getLhsLink <em>Lhs Link</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Lhs Link</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Row#getLhsLink()
	 * @see #getRow()
	 * @generated
	 */
	EReference getRow_LhsLink();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getRhsLink <em>Rhs Link</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Rhs Link</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Row#getRhsLink()
	 * @see #getRow()
	 * @generated
	 */
	EReference getRow_RhsLink();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getRowGroup <em>Row Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Row Group</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Row#getRowGroup()
	 * @see #getRow()
	 * @generated
	 */
	EReference getRow_RowGroup();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getScenarioDataProvider <em>Scenario Data Provider</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Scenario Data Provider</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Row#getScenarioDataProvider()
	 * @see #getRow()
	 * @generated
	 */
	EAttribute getRow_ScenarioDataProvider();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.lingo.reports.views.schedule.model.RowGroup <em>Row Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Row Group</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.RowGroup
	 * @generated
	 */
	EClass getRowGroup();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.lingo.reports.views.schedule.model.RowGroup#getRows <em>Rows</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Rows</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.RowGroup#getRows()
	 * @see #getRowGroup()
	 * @generated
	 */
	EReference getRowGroup_Rows();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.lingo.reports.views.schedule.model.DiffOptions <em>Diff Options</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Diff Options</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.DiffOptions
	 * @generated
	 */
	EClass getDiffOptions();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.schedule.model.DiffOptions#isFilterSelectedElements <em>Filter Selected Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Filter Selected Elements</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.DiffOptions#isFilterSelectedElements()
	 * @see #getDiffOptions()
	 * @generated
	 */
	EAttribute getDiffOptions_FilterSelectedElements();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.schedule.model.DiffOptions#isFilterSelectedSequences <em>Filter Selected Sequences</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Filter Selected Sequences</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.DiffOptions#isFilterSelectedSequences()
	 * @see #getDiffOptions()
	 * @generated
	 */
	EAttribute getDiffOptions_FilterSelectedSequences();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.lingo.reports.views.schedule.model.CompositeRow <em>Composite Row</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Composite Row</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.CompositeRow
	 * @generated
	 */
	EClass getCompositeRow();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.schedule.model.CompositeRow#getPreviousRow <em>Previous Row</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Previous Row</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.CompositeRow#getPreviousRow()
	 * @see #getCompositeRow()
	 * @generated
	 */
	EReference getCompositeRow_PreviousRow();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.schedule.model.CompositeRow#getPinnedRow <em>Pinned Row</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Pinned Row</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.CompositeRow#getPinnedRow()
	 * @see #getCompositeRow()
	 * @generated
	 */
	EReference getCompositeRow_PinnedRow();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.lingo.reports.views.schedule.model.ChangeType <em>Change Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Change Type</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ChangeType
	 * @generated
	 */
	EEnum getChangeType();

	/**
	 * Returns the meta object for data type '{@link com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider <em>IScenario Data Provider</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>IScenario Data Provider</em>'.
	 * @see com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider
	 * @model instanceClass="com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider" serializeable="false"
	 * @generated
	 */
	EDataType getIScenarioDataProvider();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ScheduleReportFactory getScheduleReportFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.RowImpl <em>Row</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.RowImpl
		 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.ScheduleReportPackageImpl#getRow()
		 * @generated
		 */
		EClass ROW = eINSTANCE.getRow();

		/**
		 * The meta object literal for the '<em><b>Scenario Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROW__SCENARIO_NAME = eINSTANCE.getRow_ScenarioName();

		/**
		 * The meta object literal for the '<em><b>Visible</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROW__VISIBLE = eINSTANCE.getRow_Visible();

		/**
		 * The meta object literal for the '<em><b>Input Equivalents</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW__INPUT_EQUIVALENTS = eINSTANCE.getRow_InputEquivalents();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROW__NAME = eINSTANCE.getRow_Name();

		/**
		 * The meta object literal for the '<em><b>Name2</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROW__NAME2 = eINSTANCE.getRow_Name2();

		/**
		 * The meta object literal for the '<em><b>Target</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW__TARGET = eINSTANCE.getRow_Target();

		/**
		 * The meta object literal for the '<em><b>Schedule</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW__SCHEDULE = eINSTANCE.getRow_Schedule();

		/**
		 * The meta object literal for the '<em><b>Sequence</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW__SEQUENCE = eINSTANCE.getRow_Sequence();

		/**
		 * The meta object literal for the '<em><b>Cargo Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW__CARGO_ALLOCATION = eINSTANCE.getRow_CargoAllocation();

		/**
		 * The meta object literal for the '<em><b>Load Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW__LOAD_ALLOCATION = eINSTANCE.getRow_LoadAllocation();

		/**
		 * The meta object literal for the '<em><b>Discharge Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW__DISCHARGE_ALLOCATION = eINSTANCE.getRow_DischargeAllocation();

		/**
		 * The meta object literal for the '<em><b>Open Load Slot Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW__OPEN_LOAD_SLOT_ALLOCATION = eINSTANCE.getRow_OpenLoadSlotAllocation();

		/**
		 * The meta object literal for the '<em><b>Open Discharge Slot Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW__OPEN_DISCHARGE_SLOT_ALLOCATION = eINSTANCE.getRow_OpenDischargeSlotAllocation();

		/**
		 * The meta object literal for the '<em><b>Reference</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROW__REFERENCE = eINSTANCE.getRow_Reference();

		/**
		 * The meta object literal for the '<em><b>Lhs Link</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW__LHS_LINK = eINSTANCE.getRow_LhsLink();

		/**
		 * The meta object literal for the '<em><b>Rhs Link</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW__RHS_LINK = eINSTANCE.getRow_RhsLink();

		/**
		 * The meta object literal for the '<em><b>Row Group</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW__ROW_GROUP = eINSTANCE.getRow_RowGroup();

		/**
		 * The meta object literal for the '<em><b>Scenario Data Provider</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROW__SCENARIO_DATA_PROVIDER = eINSTANCE.getRow_ScenarioDataProvider();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.RowGroupImpl <em>Row Group</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.RowGroupImpl
		 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.ScheduleReportPackageImpl#getRowGroup()
		 * @generated
		 */
		EClass ROW_GROUP = eINSTANCE.getRowGroup();

		/**
		 * The meta object literal for the '<em><b>Rows</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW_GROUP__ROWS = eINSTANCE.getRowGroup_Rows();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.DiffOptionsImpl <em>Diff Options</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.DiffOptionsImpl
		 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.ScheduleReportPackageImpl#getDiffOptions()
		 * @generated
		 */
		EClass DIFF_OPTIONS = eINSTANCE.getDiffOptions();

		/**
		 * The meta object literal for the '<em><b>Filter Selected Elements</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIFF_OPTIONS__FILTER_SELECTED_ELEMENTS = eINSTANCE.getDiffOptions_FilterSelectedElements();

		/**
		 * The meta object literal for the '<em><b>Filter Selected Sequences</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIFF_OPTIONS__FILTER_SELECTED_SEQUENCES = eINSTANCE.getDiffOptions_FilterSelectedSequences();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.CompositeRowImpl <em>Composite Row</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.CompositeRowImpl
		 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.ScheduleReportPackageImpl#getCompositeRow()
		 * @generated
		 */
		EClass COMPOSITE_ROW = eINSTANCE.getCompositeRow();

		/**
		 * The meta object literal for the '<em><b>Previous Row</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPOSITE_ROW__PREVIOUS_ROW = eINSTANCE.getCompositeRow_PreviousRow();

		/**
		 * The meta object literal for the '<em><b>Pinned Row</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPOSITE_ROW__PINNED_ROW = eINSTANCE.getCompositeRow_PinnedRow();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.lingo.reports.views.schedule.model.ChangeType <em>Change Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lingo.reports.views.schedule.model.ChangeType
		 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.ScheduleReportPackageImpl#getChangeType()
		 * @generated
		 */
		EEnum CHANGE_TYPE = eINSTANCE.getChangeType();

		/**
		 * The meta object literal for the '<em>IScenario Data Provider</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider
		 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.ScheduleReportPackageImpl#getIScenarioDataProvider()
		 * @generated
		 */
		EDataType ISCENARIO_DATA_PROVIDER = eINSTANCE.getIScenarioDataProvider();

	}

} //ScheduleReportPackage
