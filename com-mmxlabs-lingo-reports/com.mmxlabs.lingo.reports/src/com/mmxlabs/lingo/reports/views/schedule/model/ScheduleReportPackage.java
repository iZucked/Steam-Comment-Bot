/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.schedule.model;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
	 * The meta object id for the '{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.TableImpl <em>Table</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.TableImpl
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.ScheduleReportPackageImpl#getTable()
	 * @generated
	 */
	int TABLE = 0;

	/**
	 * The feature id for the '<em><b>Options</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE__OPTIONS = 0;

	/**
	 * The feature id for the '<em><b>Rows</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE__ROWS = 1;

	/**
	 * The feature id for the '<em><b>Cycle Groups</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE__CYCLE_GROUPS = 2;

	/**
	 * The feature id for the '<em><b>Row Groups</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE__ROW_GROUPS = 3;

	/**
	 * The feature id for the '<em><b>Scenarios</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE__SCENARIOS = 4;

	/**
	 * The feature id for the '<em><b>Pinned Scenario</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE__PINNED_SCENARIO = 5;

	/**
	 * The feature id for the '<em><b>User Groups</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE__USER_GROUPS = 6;

	/**
	 * The feature id for the '<em><b>Selected Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE__SELECTED_ELEMENTS = 7;

	/**
	 * The number of structural features of the '<em>Table</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE_FEATURE_COUNT = 8;

	/**
	 * The number of operations of the '<em>Table</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.RowImpl <em>Row</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.RowImpl
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.ScheduleReportPackageImpl#getRow()
	 * @generated
	 */
	int ROW = 1;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__VISIBLE = 0;

	/**
	 * The feature id for the '<em><b>Cycle Group</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__CYCLE_GROUP = 1;

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
	 * The feature id for the '<em><b>Open Slot Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__OPEN_SLOT_ALLOCATION = 11;

	/**
	 * The feature id for the '<em><b>Reference Row</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__REFERENCE_ROW = 12;

	/**
	 * The feature id for the '<em><b>Referring Rows</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__REFERRING_ROWS = 13;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__REFERENCE = 14;

	/**
	 * The feature id for the '<em><b>Row Group</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__ROW_GROUP = 15;

	/**
	 * The feature id for the '<em><b>Scenario</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__SCENARIO = 16;

	/**
	 * The feature id for the '<em><b>Table</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__TABLE = 17;

	/**
	 * The feature id for the '<em><b>Linked Sequences</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__LINKED_SEQUENCES = 18;

	/**
	 * The number of structural features of the '<em>Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW_FEATURE_COUNT = 19;

	/**
	 * The number of operations of the '<em>Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.CycleGroupImpl <em>Cycle Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.CycleGroupImpl
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.ScheduleReportPackageImpl#getCycleGroup()
	 * @generated
	 */
	int CYCLE_GROUP = 2;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CYCLE_GROUP__DESCRIPTION = 0;

	/**
	 * The feature id for the '<em><b>Rows</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CYCLE_GROUP__ROWS = 1;

	/**
	 * The feature id for the '<em><b>Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CYCLE_GROUP__INDEX = 2;

	/**
	 * The feature id for the '<em><b>User Group</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CYCLE_GROUP__USER_GROUP = 3;

	/**
	 * The feature id for the '<em><b>Delta</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CYCLE_GROUP__DELTA = 4;

	/**
	 * The feature id for the '<em><b>Change Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CYCLE_GROUP__CHANGE_TYPE = 5;

	/**
	 * The number of structural features of the '<em>Cycle Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CYCLE_GROUP_FEATURE_COUNT = 6;

	/**
	 * The number of operations of the '<em>Cycle Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CYCLE_GROUP_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.RowGroupImpl <em>Row Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.RowGroupImpl
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.ScheduleReportPackageImpl#getRowGroup()
	 * @generated
	 */
	int ROW_GROUP = 3;

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
	 * The meta object id for the '{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.UserGroupImpl <em>User Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.UserGroupImpl
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.ScheduleReportPackageImpl#getUserGroup()
	 * @generated
	 */
	int USER_GROUP = 4;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_GROUP__COMMENT = 0;

	/**
	 * The feature id for the '<em><b>Groups</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_GROUP__GROUPS = 1;

	/**
	 * The feature id for the '<em><b>Delta</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_GROUP__DELTA = 2;

	/**
	 * The number of structural features of the '<em>User Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_GROUP_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>User Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_GROUP_OPERATION_COUNT = 0;


	/**
	 * The meta object id for the '{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.DiffOptionsImpl <em>Diff Options</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.DiffOptionsImpl
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.ScheduleReportPackageImpl#getDiffOptions()
	 * @generated
	 */
	int DIFF_OPTIONS = 5;

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
	 * The meta object id for the '{@link com.mmxlabs.lingo.reports.views.schedule.model.ChangeType <em>Change Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ChangeType
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.ScheduleReportPackageImpl#getChangeType()
	 * @generated
	 */
	int CHANGE_TYPE = 6;


	/**
	 * Returns the meta object for class '{@link com.mmxlabs.lingo.reports.views.schedule.model.Table <em>Table</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Table</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Table
	 * @generated
	 */
	EClass getTable();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.lingo.reports.views.schedule.model.Table#getOptions <em>Options</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Options</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Table#getOptions()
	 * @see #getTable()
	 * @generated
	 */
	EReference getTable_Options();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.lingo.reports.views.schedule.model.Table#getRows <em>Rows</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Rows</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Table#getRows()
	 * @see #getTable()
	 * @generated
	 */
	EReference getTable_Rows();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.lingo.reports.views.schedule.model.Table#getCycleGroups <em>Cycle Groups</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Cycle Groups</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Table#getCycleGroups()
	 * @see #getTable()
	 * @generated
	 */
	EReference getTable_CycleGroups();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.lingo.reports.views.schedule.model.Table#getRowGroups <em>Row Groups</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Row Groups</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Table#getRowGroups()
	 * @see #getTable()
	 * @generated
	 */
	EReference getTable_RowGroups();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.lingo.reports.views.schedule.model.Table#getScenarios <em>Scenarios</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Scenarios</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Table#getScenarios()
	 * @see #getTable()
	 * @generated
	 */
	EReference getTable_Scenarios();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.schedule.model.Table#getPinnedScenario <em>Pinned Scenario</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Pinned Scenario</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Table#getPinnedScenario()
	 * @see #getTable()
	 * @generated
	 */
	EReference getTable_PinnedScenario();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.lingo.reports.views.schedule.model.Table#getUserGroups <em>User Groups</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>User Groups</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Table#getUserGroups()
	 * @see #getTable()
	 * @generated
	 */
	EReference getTable_UserGroups();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.lingo.reports.views.schedule.model.Table#getSelectedElements <em>Selected Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Selected Elements</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Table#getSelectedElements()
	 * @see #getTable()
	 * @generated
	 */
	EReference getTable_SelectedElements();

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
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getCycleGroup <em>Cycle Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Cycle Group</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Row#getCycleGroup()
	 * @see #getRow()
	 * @generated
	 */
	EReference getRow_CycleGroup();

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
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getOpenSlotAllocation <em>Open Slot Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Open Slot Allocation</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Row#getOpenSlotAllocation()
	 * @see #getRow()
	 * @generated
	 */
	EReference getRow_OpenSlotAllocation();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getReferenceRow <em>Reference Row</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Reference Row</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Row#getReferenceRow()
	 * @see #getRow()
	 * @generated
	 */
	EReference getRow_ReferenceRow();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getReferringRows <em>Referring Rows</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Referring Rows</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Row#getReferringRows()
	 * @see #getRow()
	 * @generated
	 */
	EReference getRow_ReferringRows();

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
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getScenario <em>Scenario</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Scenario</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Row#getScenario()
	 * @see #getRow()
	 * @generated
	 */
	EReference getRow_Scenario();

	/**
	 * Returns the meta object for the container reference '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getTable <em>Table</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Table</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Row#getTable()
	 * @see #getRow()
	 * @generated
	 */
	EReference getRow_Table();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getLinkedSequences <em>Linked Sequences</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Linked Sequences</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Row#getLinkedSequences()
	 * @see #getRow()
	 * @generated
	 */
	EReference getRow_LinkedSequences();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup <em>Cycle Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cycle Group</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup
	 * @generated
	 */
	EClass getCycleGroup();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup#getDescription()
	 * @see #getCycleGroup()
	 * @generated
	 */
	EAttribute getCycleGroup_Description();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup#getRows <em>Rows</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Rows</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup#getRows()
	 * @see #getCycleGroup()
	 * @generated
	 */
	EReference getCycleGroup_Rows();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup#getIndex <em>Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Index</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup#getIndex()
	 * @see #getCycleGroup()
	 * @generated
	 */
	EAttribute getCycleGroup_Index();

	/**
	 * Returns the meta object for the container reference '{@link com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup#getUserGroup <em>User Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>User Group</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup#getUserGroup()
	 * @see #getCycleGroup()
	 * @generated
	 */
	EReference getCycleGroup_UserGroup();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup#getDelta <em>Delta</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Delta</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup#getDelta()
	 * @see #getCycleGroup()
	 * @generated
	 */
	EAttribute getCycleGroup_Delta();


	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup#getChangeType <em>Change Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Change Type</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup#getChangeType()
	 * @see #getCycleGroup()
	 * @generated
	 */
	EAttribute getCycleGroup_ChangeType();

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
	 * Returns the meta object for class '{@link com.mmxlabs.lingo.reports.views.schedule.model.UserGroup <em>User Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>User Group</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.UserGroup
	 * @generated
	 */
	EClass getUserGroup();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.schedule.model.UserGroup#getComment <em>Comment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Comment</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.UserGroup#getComment()
	 * @see #getUserGroup()
	 * @generated
	 */
	EAttribute getUserGroup_Comment();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.lingo.reports.views.schedule.model.UserGroup#getGroups <em>Groups</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Groups</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.UserGroup#getGroups()
	 * @see #getUserGroup()
	 * @generated
	 */
	EReference getUserGroup_Groups();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.schedule.model.UserGroup#getDelta <em>Delta</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Delta</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.UserGroup#getDelta()
	 * @see #getUserGroup()
	 * @generated
	 */
	EAttribute getUserGroup_Delta();

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
	 * Returns the meta object for enum '{@link com.mmxlabs.lingo.reports.views.schedule.model.ChangeType <em>Change Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Change Type</em>'.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ChangeType
	 * @generated
	 */
	EEnum getChangeType();

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
		 * The meta object literal for the '{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.TableImpl <em>Table</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.TableImpl
		 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.ScheduleReportPackageImpl#getTable()
		 * @generated
		 */
		EClass TABLE = eINSTANCE.getTable();

		/**
		 * The meta object literal for the '<em><b>Options</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TABLE__OPTIONS = eINSTANCE.getTable_Options();

		/**
		 * The meta object literal for the '<em><b>Rows</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TABLE__ROWS = eINSTANCE.getTable_Rows();

		/**
		 * The meta object literal for the '<em><b>Cycle Groups</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TABLE__CYCLE_GROUPS = eINSTANCE.getTable_CycleGroups();

		/**
		 * The meta object literal for the '<em><b>Row Groups</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TABLE__ROW_GROUPS = eINSTANCE.getTable_RowGroups();

		/**
		 * The meta object literal for the '<em><b>Scenarios</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TABLE__SCENARIOS = eINSTANCE.getTable_Scenarios();

		/**
		 * The meta object literal for the '<em><b>Pinned Scenario</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TABLE__PINNED_SCENARIO = eINSTANCE.getTable_PinnedScenario();

		/**
		 * The meta object literal for the '<em><b>User Groups</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TABLE__USER_GROUPS = eINSTANCE.getTable_UserGroups();

		/**
		 * The meta object literal for the '<em><b>Selected Elements</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TABLE__SELECTED_ELEMENTS = eINSTANCE.getTable_SelectedElements();

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
		 * The meta object literal for the '<em><b>Visible</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROW__VISIBLE = eINSTANCE.getRow_Visible();

		/**
		 * The meta object literal for the '<em><b>Cycle Group</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW__CYCLE_GROUP = eINSTANCE.getRow_CycleGroup();

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
		 * The meta object literal for the '<em><b>Open Slot Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW__OPEN_SLOT_ALLOCATION = eINSTANCE.getRow_OpenSlotAllocation();

		/**
		 * The meta object literal for the '<em><b>Reference Row</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW__REFERENCE_ROW = eINSTANCE.getRow_ReferenceRow();

		/**
		 * The meta object literal for the '<em><b>Referring Rows</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW__REFERRING_ROWS = eINSTANCE.getRow_ReferringRows();

		/**
		 * The meta object literal for the '<em><b>Reference</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROW__REFERENCE = eINSTANCE.getRow_Reference();

		/**
		 * The meta object literal for the '<em><b>Row Group</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW__ROW_GROUP = eINSTANCE.getRow_RowGroup();

		/**
		 * The meta object literal for the '<em><b>Scenario</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW__SCENARIO = eINSTANCE.getRow_Scenario();

		/**
		 * The meta object literal for the '<em><b>Table</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW__TABLE = eINSTANCE.getRow_Table();

		/**
		 * The meta object literal for the '<em><b>Linked Sequences</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW__LINKED_SEQUENCES = eINSTANCE.getRow_LinkedSequences();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.CycleGroupImpl <em>Cycle Group</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.CycleGroupImpl
		 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.ScheduleReportPackageImpl#getCycleGroup()
		 * @generated
		 */
		EClass CYCLE_GROUP = eINSTANCE.getCycleGroup();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CYCLE_GROUP__DESCRIPTION = eINSTANCE.getCycleGroup_Description();

		/**
		 * The meta object literal for the '<em><b>Rows</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CYCLE_GROUP__ROWS = eINSTANCE.getCycleGroup_Rows();

		/**
		 * The meta object literal for the '<em><b>Index</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CYCLE_GROUP__INDEX = eINSTANCE.getCycleGroup_Index();

		/**
		 * The meta object literal for the '<em><b>User Group</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CYCLE_GROUP__USER_GROUP = eINSTANCE.getCycleGroup_UserGroup();

		/**
		 * The meta object literal for the '<em><b>Delta</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CYCLE_GROUP__DELTA = eINSTANCE.getCycleGroup_Delta();


		/**
		 * The meta object literal for the '<em><b>Change Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CYCLE_GROUP__CHANGE_TYPE = eINSTANCE.getCycleGroup_ChangeType();

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
		 * The meta object literal for the '{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.UserGroupImpl <em>User Group</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.UserGroupImpl
		 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.ScheduleReportPackageImpl#getUserGroup()
		 * @generated
		 */
		EClass USER_GROUP = eINSTANCE.getUserGroup();

		/**
		 * The meta object literal for the '<em><b>Comment</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute USER_GROUP__COMMENT = eINSTANCE.getUserGroup_Comment();

		/**
		 * The meta object literal for the '<em><b>Groups</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference USER_GROUP__GROUPS = eINSTANCE.getUserGroup_Groups();

		/**
		 * The meta object literal for the '<em><b>Delta</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute USER_GROUP__DELTA = eINSTANCE.getUserGroup_Delta();

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
		 * The meta object literal for the '{@link com.mmxlabs.lingo.reports.views.schedule.model.ChangeType <em>Change Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lingo.reports.views.schedule.model.ChangeType
		 * @see com.mmxlabs.lingo.reports.views.schedule.model.impl.ScheduleReportPackageImpl#getChangeType()
		 * @generated
		 */
		EEnum CHANGE_TYPE = eINSTANCE.getChangeType();

	}

} //ScheduleReportPackage
