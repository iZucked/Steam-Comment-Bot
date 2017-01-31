/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
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
 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory
 * @model kind="package"
 * @generated
 */
public interface ChangesetPackage extends EPackage {
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
	String eNS_URI = "http://www.minimaxlabs.com/models/changeset/1";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "changeset";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ChangesetPackage eINSTANCE = com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRootImpl <em>Change Set Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRootImpl
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getChangeSetRoot()
	 * @generated
	 */
	int CHANGE_SET_ROOT = 0;

	/**
	 * The feature id for the '<em><b>Change Sets</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROOT__CHANGE_SETS = 0;

	/**
	 * The number of structural features of the '<em>Change Set Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROOT_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Change Set Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROOT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetImpl <em>Change Set</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetImpl
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getChangeSet()
	 * @generated
	 */
	int CHANGE_SET = 1;

	/**
	 * The feature id for the '<em><b>Metrics To Base</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__METRICS_TO_BASE = 0;

	/**
	 * The feature id for the '<em><b>Metrics To Previous</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__METRICS_TO_PREVIOUS = 1;

	/**
	 * The feature id for the '<em><b>Base Scenario Ref</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__BASE_SCENARIO_REF = 2;

	/**
	 * The feature id for the '<em><b>Prev Scenario Ref</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__PREV_SCENARIO_REF = 3;

	/**
	 * The feature id for the '<em><b>Current Scenario Ref</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__CURRENT_SCENARIO_REF = 4;

	/**
	 * The feature id for the '<em><b>Base Scenario</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__BASE_SCENARIO = 5;

	/**
	 * The feature id for the '<em><b>Prev Scenario</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__PREV_SCENARIO = 6;

	/**
	 * The feature id for the '<em><b>Current Scenario</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__CURRENT_SCENARIO = 7;

	/**
	 * The feature id for the '<em><b>Change Set Rows To Base</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__CHANGE_SET_ROWS_TO_BASE = 8;

	/**
	 * The feature id for the '<em><b>Change Set Rows To Previous</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__CHANGE_SET_ROWS_TO_PREVIOUS = 9;

	/**
	 * The feature id for the '<em><b>Current Metrics</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__CURRENT_METRICS = 10;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__DESCRIPTION = 11;

	/**
	 * The number of structural features of the '<em>Change Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_FEATURE_COUNT = 12;

	/**
	 * The number of operations of the '<em>Change Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.MetricsImpl <em>Metrics</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.MetricsImpl
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getMetrics()
	 * @generated
	 */
	int METRICS = 2;

	/**
	 * The feature id for the '<em><b>Pnl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METRICS__PNL = 0;

	/**
	 * The feature id for the '<em><b>Lateness</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METRICS__LATENESS = 1;

	/**
	 * The feature id for the '<em><b>Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METRICS__CAPACITY = 2;

	/**
	 * The number of structural features of the '<em>Metrics</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METRICS_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Metrics</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METRICS_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.DeltaMetricsImpl <em>Delta Metrics</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.DeltaMetricsImpl
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getDeltaMetrics()
	 * @generated
	 */
	int DELTA_METRICS = 3;

	/**
	 * The feature id for the '<em><b>Pnl Delta</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELTA_METRICS__PNL_DELTA = 0;

	/**
	 * The feature id for the '<em><b>Lateness Delta</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELTA_METRICS__LATENESS_DELTA = 1;

	/**
	 * The feature id for the '<em><b>Capacity Delta</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELTA_METRICS__CAPACITY_DELTA = 2;

	/**
	 * The number of structural features of the '<em>Delta Metrics</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELTA_METRICS_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Delta Metrics</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELTA_METRICS_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataGroupImpl <em>Change Set Row Data Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataGroupImpl
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getChangeSetRowDataGroup()
	 * @generated
	 */
	int CHANGE_SET_ROW_DATA_GROUP = 4;

	/**
	 * The feature id for the '<em><b>Members</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA_GROUP__MEMBERS = 0;

	/**
	 * The number of structural features of the '<em>Change Set Row Data Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA_GROUP_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Change Set Row Data Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA_GROUP_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl <em>Change Set Row</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getChangeSetRow()
	 * @generated
	 */
	int CHANGE_SET_ROW = 5;

	/**
	 * The feature id for the '<em><b>Wiring Change</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW__WIRING_CHANGE = 0;

	/**
	 * The feature id for the '<em><b>Vessel Change</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW__VESSEL_CHANGE = 1;

	/**
	 * The feature id for the '<em><b>Before Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW__BEFORE_DATA = 2;

	/**
	 * The feature id for the '<em><b>After Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW__AFTER_DATA = 3;

	/**
	 * The number of structural features of the '<em>Change Set Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Change Set Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_OPERATION_COUNT = 0;


	/**
	 * The meta object id for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl <em>Change Set Row Data</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getChangeSetRowData()
	 * @generated
	 */
	int CHANGE_SET_ROW_DATA = 6;

	/**
	 * The feature id for the '<em><b>Primary Record</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__PRIMARY_RECORD = 0;

	/**
	 * The feature id for the '<em><b>Row Data Group</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__ROW_DATA_GROUP = 1;

	/**
	 * The feature id for the '<em><b>Event Grouping</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__EVENT_GROUPING = 2;

	/**
	 * The feature id for the '<em><b>Vessel Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__VESSEL_NAME = 3;

	/**
	 * The feature id for the '<em><b>Vessel Short Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__VESSEL_SHORT_NAME = 4;

	/**
	 * The feature id for the '<em><b>Lhs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__LHS_NAME = 5;

	/**
	 * The feature id for the '<em><b>Rhs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__RHS_NAME = 6;

	/**
	 * The feature id for the '<em><b>Lhs Link</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__LHS_LINK = 7;

	/**
	 * The feature id for the '<em><b>Rhs Link</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__RHS_LINK = 8;

	/**
	 * The feature id for the '<em><b>Load Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__LOAD_SLOT = 9;

	/**
	 * The feature id for the '<em><b>Discharge Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__DISCHARGE_SLOT = 10;

	/**
	 * The feature id for the '<em><b>Load Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__LOAD_ALLOCATION = 11;

	/**
	 * The feature id for the '<em><b>Discharge Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__DISCHARGE_ALLOCATION = 12;

	/**
	 * The feature id for the '<em><b>Open Load Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__OPEN_LOAD_ALLOCATION = 13;

	/**
	 * The feature id for the '<em><b>Open Discharge Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__OPEN_DISCHARGE_ALLOCATION = 14;

	/**
	 * The feature id for the '<em><b>Lhs Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__LHS_EVENT = 15;

	/**
	 * The feature id for the '<em><b>Rhs Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__RHS_EVENT = 16;

	/**
	 * The feature id for the '<em><b>Lhs Group Profit And Loss</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__LHS_GROUP_PROFIT_AND_LOSS = 17;

	/**
	 * The feature id for the '<em><b>Rhs Group Profit And Loss</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__RHS_GROUP_PROFIT_AND_LOSS = 18;

	/**
	 * The number of structural features of the '<em>Change Set Row Data</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA_FEATURE_COUNT = 19;

	/**
	 * The number of operations of the '<em>Change Set Row Data</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA_OPERATION_COUNT = 0;


	/**
	 * The meta object id for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableGroupImpl <em>Change Set Table Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableGroupImpl
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getChangeSetTableGroup()
	 * @generated
	 */
	int CHANGE_SET_TABLE_GROUP = 7;

	/**
	 * The feature id for the '<em><b>Rows</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_GROUP__ROWS = 0;

	/**
	 * The feature id for the '<em><b>Delta Metrics</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_GROUP__DELTA_METRICS = 1;

	/**
	 * The feature id for the '<em><b>Current Metrics</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_GROUP__CURRENT_METRICS = 2;

	/**
	 * The feature id for the '<em><b>Change Set</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_GROUP__CHANGE_SET = 3;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_GROUP__DESCRIPTION = 4;

	/**
	 * The number of structural features of the '<em>Change Set Table Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_GROUP_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>Change Set Table Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_GROUP_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl <em>Change Set Table Row</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getChangeSetTableRow()
	 * @generated
	 */
	int CHANGE_SET_TABLE_ROW = 8;

	/**
	 * The feature id for the '<em><b>Lhs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__LHS_NAME = 0;

	/**
	 * The feature id for the '<em><b>Rhs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__RHS_NAME = 1;

	/**
	 * The feature id for the '<em><b>Lhs Before</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__LHS_BEFORE = 2;

	/**
	 * The feature id for the '<em><b>Lhs After</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__LHS_AFTER = 3;

	/**
	 * The feature id for the '<em><b>Rhs Before</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__RHS_BEFORE = 4;

	/**
	 * The feature id for the '<em><b>Rhs After</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__RHS_AFTER = 5;

	/**
	 * The feature id for the '<em><b>Before Vessel Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__BEFORE_VESSEL_NAME = 6;

	/**
	 * The feature id for the '<em><b>Before Vessel Short Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__BEFORE_VESSEL_SHORT_NAME = 7;

	/**
	 * The feature id for the '<em><b>After Vessel Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__AFTER_VESSEL_NAME = 8;

	/**
	 * The feature id for the '<em><b>After Vessel Short Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__AFTER_VESSEL_SHORT_NAME = 9;

	/**
	 * The feature id for the '<em><b>Wiring Change</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__WIRING_CHANGE = 10;

	/**
	 * The feature id for the '<em><b>Vessel Change</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__VESSEL_CHANGE = 11;

	/**
	 * The feature id for the '<em><b>Previous RHS</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__PREVIOUS_RHS = 12;

	/**
	 * The feature id for the '<em><b>Next LHS</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__NEXT_LHS = 13;

	/**
	 * The feature id for the '<em><b>Lhs Slot</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__LHS_SLOT = 14;

	/**
	 * The feature id for the '<em><b>Lhs Spot</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__LHS_SPOT = 15;

	/**
	 * The feature id for the '<em><b>Lhs Optional</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__LHS_OPTIONAL = 16;

	/**
	 * The feature id for the '<em><b>Lhs Valid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__LHS_VALID = 17;

	/**
	 * The feature id for the '<em><b>Lhs Non Shipped</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__LHS_NON_SHIPPED = 18;

	/**
	 * The feature id for the '<em><b>Rhs Slot</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__RHS_SLOT = 19;

	/**
	 * The feature id for the '<em><b>Rhs Spot</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__RHS_SPOT = 20;

	/**
	 * The feature id for the '<em><b>Rhs Optional</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__RHS_OPTIONAL = 21;

	/**
	 * The feature id for the '<em><b>Rhs Valid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__RHS_VALID = 22;

	/**
	 * The feature id for the '<em><b>Rhs Non Shipped</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__RHS_NON_SHIPPED = 23;

	/**
	 * The number of structural features of the '<em>Change Set Table Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW_FEATURE_COUNT = 24;

	/**
	 * The number of operations of the '<em>Change Set Table Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRootImpl <em>Change Set Table Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRootImpl
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getChangeSetTableRoot()
	 * @generated
	 */
	int CHANGE_SET_TABLE_ROOT = 9;

	/**
	 * The feature id for the '<em><b>Groups</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROOT__GROUPS = 0;

	/**
	 * The number of structural features of the '<em>Change Set Table Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROOT_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Change Set Table Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROOT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '<em>Scenario Result</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.scenario.service.ui.ScenarioResult
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getScenarioResult()
	 * @generated
	 */
	int SCENARIO_RESULT = 10;


	/**
	 * Returns the meta object for class '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot <em>Change Set Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Change Set Root</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot
	 * @generated
	 */
	EClass getChangeSetRoot();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot#getChangeSets <em>Change Sets</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Change Sets</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot#getChangeSets()
	 * @see #getChangeSetRoot()
	 * @generated
	 */
	EReference getChangeSetRoot_ChangeSets();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet <em>Change Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Change Set</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet
	 * @generated
	 */
	EClass getChangeSet();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getMetricsToBase <em>Metrics To Base</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Metrics To Base</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getMetricsToBase()
	 * @see #getChangeSet()
	 * @generated
	 */
	EReference getChangeSet_MetricsToBase();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getMetricsToPrevious <em>Metrics To Previous</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Metrics To Previous</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getMetricsToPrevious()
	 * @see #getChangeSet()
	 * @generated
	 */
	EReference getChangeSet_MetricsToPrevious();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getBaseScenarioRef <em>Base Scenario Ref</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Base Scenario Ref</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getBaseScenarioRef()
	 * @see #getChangeSet()
	 * @generated
	 */
	EReference getChangeSet_BaseScenarioRef();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getPrevScenarioRef <em>Prev Scenario Ref</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Prev Scenario Ref</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getPrevScenarioRef()
	 * @see #getChangeSet()
	 * @generated
	 */
	EReference getChangeSet_PrevScenarioRef();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getCurrentScenarioRef <em>Current Scenario Ref</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Current Scenario Ref</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getCurrentScenarioRef()
	 * @see #getChangeSet()
	 * @generated
	 */
	EReference getChangeSet_CurrentScenarioRef();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getBaseScenario <em>Base Scenario</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Base Scenario</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getBaseScenario()
	 * @see #getChangeSet()
	 * @generated
	 */
	EAttribute getChangeSet_BaseScenario();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getPrevScenario <em>Prev Scenario</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Prev Scenario</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getPrevScenario()
	 * @see #getChangeSet()
	 * @generated
	 */
	EAttribute getChangeSet_PrevScenario();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getCurrentScenario <em>Current Scenario</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Current Scenario</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getCurrentScenario()
	 * @see #getChangeSet()
	 * @generated
	 */
	EAttribute getChangeSet_CurrentScenario();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getChangeSetRowsToBase <em>Change Set Rows To Base</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Change Set Rows To Base</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getChangeSetRowsToBase()
	 * @see #getChangeSet()
	 * @generated
	 */
	EReference getChangeSet_ChangeSetRowsToBase();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getChangeSetRowsToPrevious <em>Change Set Rows To Previous</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Change Set Rows To Previous</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getChangeSetRowsToPrevious()
	 * @see #getChangeSet()
	 * @generated
	 */
	EReference getChangeSet_ChangeSetRowsToPrevious();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getCurrentMetrics <em>Current Metrics</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Current Metrics</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getCurrentMetrics()
	 * @see #getChangeSet()
	 * @generated
	 */
	EReference getChangeSet_CurrentMetrics();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.lingo.reports.views.changeset.model.Metrics <em>Metrics</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Metrics</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.Metrics
	 * @generated
	 */
	EClass getMetrics();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.Metrics#getPnl <em>Pnl</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Pnl</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.Metrics#getPnl()
	 * @see #getMetrics()
	 * @generated
	 */
	EAttribute getMetrics_Pnl();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.Metrics#getLateness <em>Lateness</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lateness</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.Metrics#getLateness()
	 * @see #getMetrics()
	 * @generated
	 */
	EAttribute getMetrics_Lateness();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.Metrics#getCapacity <em>Capacity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Capacity</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.Metrics#getCapacity()
	 * @see #getMetrics()
	 * @generated
	 */
	EAttribute getMetrics_Capacity();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics <em>Delta Metrics</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Delta Metrics</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics
	 * @generated
	 */
	EClass getDeltaMetrics();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics#getPnlDelta <em>Pnl Delta</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Pnl Delta</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics#getPnlDelta()
	 * @see #getDeltaMetrics()
	 * @generated
	 */
	EAttribute getDeltaMetrics_PnlDelta();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics#getLatenessDelta <em>Lateness Delta</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lateness Delta</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics#getLatenessDelta()
	 * @see #getDeltaMetrics()
	 * @generated
	 */
	EAttribute getDeltaMetrics_LatenessDelta();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics#getCapacityDelta <em>Capacity Delta</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Capacity Delta</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics#getCapacityDelta()
	 * @see #getDeltaMetrics()
	 * @generated
	 */
	EAttribute getDeltaMetrics_CapacityDelta();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowDataGroup <em>Change Set Row Data Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Change Set Row Data Group</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowDataGroup
	 * @generated
	 */
	EClass getChangeSetRowDataGroup();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowDataGroup#getMembers <em>Members</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Members</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowDataGroup#getMembers()
	 * @see #getChangeSetRowDataGroup()
	 * @generated
	 */
	EReference getChangeSetRowDataGroup_Members();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow <em>Change Set Row</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Change Set Row</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow
	 * @generated
	 */
	EClass getChangeSetRow();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#isWiringChange <em>Wiring Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Wiring Change</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#isWiringChange()
	 * @see #getChangeSetRow()
	 * @generated
	 */
	EAttribute getChangeSetRow_WiringChange();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#isVesselChange <em>Vessel Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Vessel Change</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#isVesselChange()
	 * @see #getChangeSetRow()
	 * @generated
	 */
	EAttribute getChangeSetRow_VesselChange();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getBeforeData <em>Before Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Before Data</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getBeforeData()
	 * @see #getChangeSetRow()
	 * @generated
	 */
	EReference getChangeSetRow_BeforeData();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getAfterData <em>After Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>After Data</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getAfterData()
	 * @see #getChangeSetRow()
	 * @generated
	 */
	EReference getChangeSetRow_AfterData();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData <em>Change Set Row Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Change Set Row Data</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData
	 * @generated
	 */
	EClass getChangeSetRowData();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isPrimaryRecord <em>Primary Record</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Primary Record</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isPrimaryRecord()
	 * @see #getChangeSetRowData()
	 * @generated
	 */
	EAttribute getChangeSetRowData_PrimaryRecord();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getLhsName <em>Lhs Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lhs Name</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getLhsName()
	 * @see #getChangeSetRowData()
	 * @generated
	 */
	EAttribute getChangeSetRowData_LhsName();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getRhsName <em>Rhs Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Rhs Name</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getRhsName()
	 * @see #getChangeSetRowData()
	 * @generated
	 */
	EAttribute getChangeSetRowData_RhsName();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getLhsLink <em>Lhs Link</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Lhs Link</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getLhsLink()
	 * @see #getChangeSetRowData()
	 * @generated
	 */
	EReference getChangeSetRowData_LhsLink();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getRhsLink <em>Rhs Link</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Rhs Link</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getRhsLink()
	 * @see #getChangeSetRowData()
	 * @generated
	 */
	EReference getChangeSetRowData_RhsLink();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getLoadSlot <em>Load Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Load Slot</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getLoadSlot()
	 * @see #getChangeSetRowData()
	 * @generated
	 */
	EReference getChangeSetRowData_LoadSlot();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getDischargeSlot <em>Discharge Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Discharge Slot</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getDischargeSlot()
	 * @see #getChangeSetRowData()
	 * @generated
	 */
	EReference getChangeSetRowData_DischargeSlot();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getLoadAllocation <em>Load Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Load Allocation</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getLoadAllocation()
	 * @see #getChangeSetRowData()
	 * @generated
	 */
	EReference getChangeSetRowData_LoadAllocation();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getDescription()
	 * @see #getChangeSet()
	 * @generated
	 */
	EAttribute getChangeSet_Description();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getDischargeAllocation <em>Discharge Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Discharge Allocation</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getDischargeAllocation()
	 * @see #getChangeSetRowData()
	 * @generated
	 */
	EReference getChangeSetRowData_DischargeAllocation();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getOpenLoadAllocation <em>Open Load Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Open Load Allocation</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getOpenLoadAllocation()
	 * @see #getChangeSetRowData()
	 * @generated
	 */
	EReference getChangeSetRowData_OpenLoadAllocation();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getOpenDischargeAllocation <em>Open Discharge Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Open Discharge Allocation</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getOpenDischargeAllocation()
	 * @see #getChangeSetRowData()
	 * @generated
	 */
	EReference getChangeSetRowData_OpenDischargeAllocation();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getLhsEvent <em>Lhs Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Lhs Event</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getLhsEvent()
	 * @see #getChangeSetRowData()
	 * @generated
	 */
	EReference getChangeSetRowData_LhsEvent();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getRhsEvent <em>Rhs Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Rhs Event</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getRhsEvent()
	 * @see #getChangeSetRowData()
	 * @generated
	 */
	EReference getChangeSetRowData_RhsEvent();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getLhsGroupProfitAndLoss <em>Lhs Group Profit And Loss</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Lhs Group Profit And Loss</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getLhsGroupProfitAndLoss()
	 * @see #getChangeSetRowData()
	 * @generated
	 */
	EReference getChangeSetRowData_LhsGroupProfitAndLoss();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getRhsGroupProfitAndLoss <em>Rhs Group Profit And Loss</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Rhs Group Profit And Loss</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getRhsGroupProfitAndLoss()
	 * @see #getChangeSetRowData()
	 * @generated
	 */
	EReference getChangeSetRowData_RhsGroupProfitAndLoss();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup <em>Change Set Table Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Change Set Table Group</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup
	 * @generated
	 */
	EClass getChangeSetTableGroup();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getRows <em>Rows</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Rows</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getRows()
	 * @see #getChangeSetTableGroup()
	 * @generated
	 */
	EReference getChangeSetTableGroup_Rows();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getDeltaMetrics <em>Delta Metrics</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Delta Metrics</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getDeltaMetrics()
	 * @see #getChangeSetTableGroup()
	 * @generated
	 */
	EReference getChangeSetTableGroup_DeltaMetrics();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getCurrentMetrics <em>Current Metrics</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Current Metrics</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getCurrentMetrics()
	 * @see #getChangeSetTableGroup()
	 * @generated
	 */
	EReference getChangeSetTableGroup_CurrentMetrics();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getChangeSet <em>Change Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Change Set</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getChangeSet()
	 * @see #getChangeSetTableGroup()
	 * @generated
	 */
	EReference getChangeSetTableGroup_ChangeSet();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getDescription()
	 * @see #getChangeSetTableGroup()
	 * @generated
	 */
	EAttribute getChangeSetTableGroup_Description();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow <em>Change Set Table Row</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Change Set Table Row</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow
	 * @generated
	 */
	EClass getChangeSetTableRow();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getLhsName <em>Lhs Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lhs Name</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getLhsName()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EAttribute getChangeSetTableRow_LhsName();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getRhsName <em>Rhs Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Rhs Name</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getRhsName()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EAttribute getChangeSetTableRow_RhsName();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getLhsBefore <em>Lhs Before</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Lhs Before</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getLhsBefore()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EReference getChangeSetTableRow_LhsBefore();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getLhsAfter <em>Lhs After</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Lhs After</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getLhsAfter()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EReference getChangeSetTableRow_LhsAfter();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getRhsBefore <em>Rhs Before</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Rhs Before</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getRhsBefore()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EReference getChangeSetTableRow_RhsBefore();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getRhsAfter <em>Rhs After</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Rhs After</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getRhsAfter()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EReference getChangeSetTableRow_RhsAfter();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getBeforeVesselName <em>Before Vessel Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Before Vessel Name</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getBeforeVesselName()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EAttribute getChangeSetTableRow_BeforeVesselName();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getBeforeVesselShortName <em>Before Vessel Short Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Before Vessel Short Name</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getBeforeVesselShortName()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EAttribute getChangeSetTableRow_BeforeVesselShortName();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getAfterVesselName <em>After Vessel Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>After Vessel Name</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getAfterVesselName()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EAttribute getChangeSetTableRow_AfterVesselName();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getAfterVesselShortName <em>After Vessel Short Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>After Vessel Short Name</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getAfterVesselShortName()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EAttribute getChangeSetTableRow_AfterVesselShortName();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isWiringChange <em>Wiring Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Wiring Change</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isWiringChange()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EAttribute getChangeSetTableRow_WiringChange();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isVesselChange <em>Vessel Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Vessel Change</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isVesselChange()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EAttribute getChangeSetTableRow_VesselChange();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getPreviousRHS <em>Previous RHS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Previous RHS</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getPreviousRHS()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EReference getChangeSetTableRow_PreviousRHS();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getNextLHS <em>Next LHS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Next LHS</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getNextLHS()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EReference getChangeSetTableRow_NextLHS();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isLhsSlot <em>Lhs Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lhs Slot</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isLhsSlot()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EAttribute getChangeSetTableRow_LhsSlot();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isLhsSpot <em>Lhs Spot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lhs Spot</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isLhsSpot()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EAttribute getChangeSetTableRow_LhsSpot();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isLhsOptional <em>Lhs Optional</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lhs Optional</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isLhsOptional()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EAttribute getChangeSetTableRow_LhsOptional();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isLhsValid <em>Lhs Valid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lhs Valid</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isLhsValid()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EAttribute getChangeSetTableRow_LhsValid();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isLhsNonShipped <em>Lhs Non Shipped</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lhs Non Shipped</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isLhsNonShipped()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EAttribute getChangeSetTableRow_LhsNonShipped();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isRhsSlot <em>Rhs Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Rhs Slot</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isRhsSlot()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EAttribute getChangeSetTableRow_RhsSlot();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isRhsSpot <em>Rhs Spot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Rhs Spot</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isRhsSpot()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EAttribute getChangeSetTableRow_RhsSpot();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isRhsOptional <em>Rhs Optional</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Rhs Optional</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isRhsOptional()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EAttribute getChangeSetTableRow_RhsOptional();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isRhsValid <em>Rhs Valid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Rhs Valid</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isRhsValid()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EAttribute getChangeSetTableRow_RhsValid();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isRhsNonShipped <em>Rhs Non Shipped</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Rhs Non Shipped</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isRhsNonShipped()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EAttribute getChangeSetTableRow_RhsNonShipped();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRoot <em>Change Set Table Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Change Set Table Root</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRoot
	 * @generated
	 */
	EClass getChangeSetTableRoot();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRoot#getGroups <em>Groups</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Groups</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRoot#getGroups()
	 * @see #getChangeSetTableRoot()
	 * @generated
	 */
	EReference getChangeSetTableRoot_Groups();

	/**
	 * Returns the meta object for the container reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getRowDataGroup <em>Row Data Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Row Data Group</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getRowDataGroup()
	 * @see #getChangeSetRowData()
	 * @generated
	 */
	EReference getChangeSetRowData_RowDataGroup();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getEventGrouping <em>Event Grouping</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Event Grouping</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getEventGrouping()
	 * @see #getChangeSetRowData()
	 * @generated
	 */
	EReference getChangeSetRowData_EventGrouping();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getVesselName <em>Vessel Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Vessel Name</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getVesselName()
	 * @see #getChangeSetRowData()
	 * @generated
	 */
	EAttribute getChangeSetRowData_VesselName();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getVesselShortName <em>Vessel Short Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Vessel Short Name</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getVesselShortName()
	 * @see #getChangeSetRowData()
	 * @generated
	 */
	EAttribute getChangeSetRowData_VesselShortName();

	/**
	 * Returns the meta object for data type '{@link com.mmxlabs.scenario.service.ui.ScenarioResult <em>Scenario Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Scenario Result</em>'.
	 * @see com.mmxlabs.scenario.service.ui.ScenarioResult
	 * @model instanceClass="com.mmxlabs.scenario.service.ui.ScenarioResult"
	 * @generated
	 */
	EDataType getScenarioResult();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ChangesetFactory getChangesetFactory();

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
		 * The meta object literal for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRootImpl <em>Change Set Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRootImpl
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getChangeSetRoot()
		 * @generated
		 */
		EClass CHANGE_SET_ROOT = eINSTANCE.getChangeSetRoot();

		/**
		 * The meta object literal for the '<em><b>Change Sets</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROOT__CHANGE_SETS = eINSTANCE.getChangeSetRoot_ChangeSets();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetImpl <em>Change Set</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetImpl
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getChangeSet()
		 * @generated
		 */
		EClass CHANGE_SET = eINSTANCE.getChangeSet();

		/**
		 * The meta object literal for the '<em><b>Metrics To Base</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET__METRICS_TO_BASE = eINSTANCE.getChangeSet_MetricsToBase();

		/**
		 * The meta object literal for the '<em><b>Metrics To Previous</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET__METRICS_TO_PREVIOUS = eINSTANCE.getChangeSet_MetricsToPrevious();

		/**
		 * The meta object literal for the '<em><b>Base Scenario Ref</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET__BASE_SCENARIO_REF = eINSTANCE.getChangeSet_BaseScenarioRef();

		/**
		 * The meta object literal for the '<em><b>Prev Scenario Ref</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET__PREV_SCENARIO_REF = eINSTANCE.getChangeSet_PrevScenarioRef();

		/**
		 * The meta object literal for the '<em><b>Current Scenario Ref</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET__CURRENT_SCENARIO_REF = eINSTANCE.getChangeSet_CurrentScenarioRef();

		/**
		 * The meta object literal for the '<em><b>Base Scenario</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET__BASE_SCENARIO = eINSTANCE.getChangeSet_BaseScenario();

		/**
		 * The meta object literal for the '<em><b>Prev Scenario</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET__PREV_SCENARIO = eINSTANCE.getChangeSet_PrevScenario();

		/**
		 * The meta object literal for the '<em><b>Current Scenario</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET__CURRENT_SCENARIO = eINSTANCE.getChangeSet_CurrentScenario();

		/**
		 * The meta object literal for the '<em><b>Change Set Rows To Base</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET__CHANGE_SET_ROWS_TO_BASE = eINSTANCE.getChangeSet_ChangeSetRowsToBase();

		/**
		 * The meta object literal for the '<em><b>Change Set Rows To Previous</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET__CHANGE_SET_ROWS_TO_PREVIOUS = eINSTANCE.getChangeSet_ChangeSetRowsToPrevious();

		/**
		 * The meta object literal for the '<em><b>Current Metrics</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET__CURRENT_METRICS = eINSTANCE.getChangeSet_CurrentMetrics();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET__DESCRIPTION = eINSTANCE.getChangeSet_Description();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.MetricsImpl <em>Metrics</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.MetricsImpl
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getMetrics()
		 * @generated
		 */
		EClass METRICS = eINSTANCE.getMetrics();

		/**
		 * The meta object literal for the '<em><b>Pnl</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute METRICS__PNL = eINSTANCE.getMetrics_Pnl();

		/**
		 * The meta object literal for the '<em><b>Lateness</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute METRICS__LATENESS = eINSTANCE.getMetrics_Lateness();

		/**
		 * The meta object literal for the '<em><b>Capacity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute METRICS__CAPACITY = eINSTANCE.getMetrics_Capacity();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.DeltaMetricsImpl <em>Delta Metrics</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.DeltaMetricsImpl
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getDeltaMetrics()
		 * @generated
		 */
		EClass DELTA_METRICS = eINSTANCE.getDeltaMetrics();

		/**
		 * The meta object literal for the '<em><b>Pnl Delta</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DELTA_METRICS__PNL_DELTA = eINSTANCE.getDeltaMetrics_PnlDelta();

		/**
		 * The meta object literal for the '<em><b>Lateness Delta</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DELTA_METRICS__LATENESS_DELTA = eINSTANCE.getDeltaMetrics_LatenessDelta();

		/**
		 * The meta object literal for the '<em><b>Capacity Delta</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DELTA_METRICS__CAPACITY_DELTA = eINSTANCE.getDeltaMetrics_CapacityDelta();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataGroupImpl <em>Change Set Row Data Group</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataGroupImpl
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getChangeSetRowDataGroup()
		 * @generated
		 */
		EClass CHANGE_SET_ROW_DATA_GROUP = eINSTANCE.getChangeSetRowDataGroup();

		/**
		 * The meta object literal for the '<em><b>Members</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW_DATA_GROUP__MEMBERS = eINSTANCE.getChangeSetRowDataGroup_Members();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl <em>Change Set Row</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getChangeSetRow()
		 * @generated
		 */
		EClass CHANGE_SET_ROW = eINSTANCE.getChangeSetRow();

		/**
		 * The meta object literal for the '<em><b>Wiring Change</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_ROW__WIRING_CHANGE = eINSTANCE.getChangeSetRow_WiringChange();

		/**
		 * The meta object literal for the '<em><b>Vessel Change</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_ROW__VESSEL_CHANGE = eINSTANCE.getChangeSetRow_VesselChange();

		/**
		 * The meta object literal for the '<em><b>Before Data</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW__BEFORE_DATA = eINSTANCE.getChangeSetRow_BeforeData();

		/**
		 * The meta object literal for the '<em><b>After Data</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW__AFTER_DATA = eINSTANCE.getChangeSetRow_AfterData();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl <em>Change Set Row Data</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getChangeSetRowData()
		 * @generated
		 */
		EClass CHANGE_SET_ROW_DATA = eINSTANCE.getChangeSetRowData();

		/**
		 * The meta object literal for the '<em><b>Primary Record</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_ROW_DATA__PRIMARY_RECORD = eINSTANCE.getChangeSetRowData_PrimaryRecord();

		/**
		 * The meta object literal for the '<em><b>Lhs Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_ROW_DATA__LHS_NAME = eINSTANCE.getChangeSetRowData_LhsName();

		/**
		 * The meta object literal for the '<em><b>Rhs Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_ROW_DATA__RHS_NAME = eINSTANCE.getChangeSetRowData_RhsName();

		/**
		 * The meta object literal for the '<em><b>Lhs Link</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW_DATA__LHS_LINK = eINSTANCE.getChangeSetRowData_LhsLink();

		/**
		 * The meta object literal for the '<em><b>Rhs Link</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW_DATA__RHS_LINK = eINSTANCE.getChangeSetRowData_RhsLink();

		/**
		 * The meta object literal for the '<em><b>Load Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW_DATA__LOAD_SLOT = eINSTANCE.getChangeSetRowData_LoadSlot();

		/**
		 * The meta object literal for the '<em><b>Discharge Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW_DATA__DISCHARGE_SLOT = eINSTANCE.getChangeSetRowData_DischargeSlot();

		/**
		 * The meta object literal for the '<em><b>Load Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW_DATA__LOAD_ALLOCATION = eINSTANCE.getChangeSetRowData_LoadAllocation();

		/**
		 * The meta object literal for the '<em><b>Discharge Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW_DATA__DISCHARGE_ALLOCATION = eINSTANCE.getChangeSetRowData_DischargeAllocation();

		/**
		 * The meta object literal for the '<em><b>Open Load Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW_DATA__OPEN_LOAD_ALLOCATION = eINSTANCE.getChangeSetRowData_OpenLoadAllocation();

		/**
		 * The meta object literal for the '<em><b>Open Discharge Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW_DATA__OPEN_DISCHARGE_ALLOCATION = eINSTANCE.getChangeSetRowData_OpenDischargeAllocation();

		/**
		 * The meta object literal for the '<em><b>Lhs Event</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW_DATA__LHS_EVENT = eINSTANCE.getChangeSetRowData_LhsEvent();

		/**
		 * The meta object literal for the '<em><b>Rhs Event</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW_DATA__RHS_EVENT = eINSTANCE.getChangeSetRowData_RhsEvent();

		/**
		 * The meta object literal for the '<em><b>Lhs Group Profit And Loss</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW_DATA__LHS_GROUP_PROFIT_AND_LOSS = eINSTANCE.getChangeSetRowData_LhsGroupProfitAndLoss();

		/**
		 * The meta object literal for the '<em><b>Rhs Group Profit And Loss</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW_DATA__RHS_GROUP_PROFIT_AND_LOSS = eINSTANCE.getChangeSetRowData_RhsGroupProfitAndLoss();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableGroupImpl <em>Change Set Table Group</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableGroupImpl
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getChangeSetTableGroup()
		 * @generated
		 */
		EClass CHANGE_SET_TABLE_GROUP = eINSTANCE.getChangeSetTableGroup();

		/**
		 * The meta object literal for the '<em><b>Rows</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_TABLE_GROUP__ROWS = eINSTANCE.getChangeSetTableGroup_Rows();

		/**
		 * The meta object literal for the '<em><b>Delta Metrics</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_TABLE_GROUP__DELTA_METRICS = eINSTANCE.getChangeSetTableGroup_DeltaMetrics();

		/**
		 * The meta object literal for the '<em><b>Current Metrics</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_TABLE_GROUP__CURRENT_METRICS = eINSTANCE.getChangeSetTableGroup_CurrentMetrics();

		/**
		 * The meta object literal for the '<em><b>Change Set</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_TABLE_GROUP__CHANGE_SET = eINSTANCE.getChangeSetTableGroup_ChangeSet();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_TABLE_GROUP__DESCRIPTION = eINSTANCE.getChangeSetTableGroup_Description();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl <em>Change Set Table Row</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getChangeSetTableRow()
		 * @generated
		 */
		EClass CHANGE_SET_TABLE_ROW = eINSTANCE.getChangeSetTableRow();

		/**
		 * The meta object literal for the '<em><b>Lhs Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_TABLE_ROW__LHS_NAME = eINSTANCE.getChangeSetTableRow_LhsName();

		/**
		 * The meta object literal for the '<em><b>Rhs Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_TABLE_ROW__RHS_NAME = eINSTANCE.getChangeSetTableRow_RhsName();

		/**
		 * The meta object literal for the '<em><b>Lhs Before</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_TABLE_ROW__LHS_BEFORE = eINSTANCE.getChangeSetTableRow_LhsBefore();

		/**
		 * The meta object literal for the '<em><b>Lhs After</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_TABLE_ROW__LHS_AFTER = eINSTANCE.getChangeSetTableRow_LhsAfter();

		/**
		 * The meta object literal for the '<em><b>Rhs Before</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_TABLE_ROW__RHS_BEFORE = eINSTANCE.getChangeSetTableRow_RhsBefore();

		/**
		 * The meta object literal for the '<em><b>Rhs After</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_TABLE_ROW__RHS_AFTER = eINSTANCE.getChangeSetTableRow_RhsAfter();

		/**
		 * The meta object literal for the '<em><b>Before Vessel Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_TABLE_ROW__BEFORE_VESSEL_NAME = eINSTANCE.getChangeSetTableRow_BeforeVesselName();

		/**
		 * The meta object literal for the '<em><b>Before Vessel Short Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_TABLE_ROW__BEFORE_VESSEL_SHORT_NAME = eINSTANCE.getChangeSetTableRow_BeforeVesselShortName();

		/**
		 * The meta object literal for the '<em><b>After Vessel Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_TABLE_ROW__AFTER_VESSEL_NAME = eINSTANCE.getChangeSetTableRow_AfterVesselName();

		/**
		 * The meta object literal for the '<em><b>After Vessel Short Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_TABLE_ROW__AFTER_VESSEL_SHORT_NAME = eINSTANCE.getChangeSetTableRow_AfterVesselShortName();

		/**
		 * The meta object literal for the '<em><b>Wiring Change</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_TABLE_ROW__WIRING_CHANGE = eINSTANCE.getChangeSetTableRow_WiringChange();

		/**
		 * The meta object literal for the '<em><b>Vessel Change</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_TABLE_ROW__VESSEL_CHANGE = eINSTANCE.getChangeSetTableRow_VesselChange();

		/**
		 * The meta object literal for the '<em><b>Previous RHS</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_TABLE_ROW__PREVIOUS_RHS = eINSTANCE.getChangeSetTableRow_PreviousRHS();

		/**
		 * The meta object literal for the '<em><b>Next LHS</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_TABLE_ROW__NEXT_LHS = eINSTANCE.getChangeSetTableRow_NextLHS();

		/**
		 * The meta object literal for the '<em><b>Lhs Slot</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_TABLE_ROW__LHS_SLOT = eINSTANCE.getChangeSetTableRow_LhsSlot();

		/**
		 * The meta object literal for the '<em><b>Lhs Spot</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_TABLE_ROW__LHS_SPOT = eINSTANCE.getChangeSetTableRow_LhsSpot();

		/**
		 * The meta object literal for the '<em><b>Lhs Optional</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_TABLE_ROW__LHS_OPTIONAL = eINSTANCE.getChangeSetTableRow_LhsOptional();

		/**
		 * The meta object literal for the '<em><b>Lhs Valid</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_TABLE_ROW__LHS_VALID = eINSTANCE.getChangeSetTableRow_LhsValid();

		/**
		 * The meta object literal for the '<em><b>Lhs Non Shipped</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_TABLE_ROW__LHS_NON_SHIPPED = eINSTANCE.getChangeSetTableRow_LhsNonShipped();

		/**
		 * The meta object literal for the '<em><b>Rhs Slot</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_TABLE_ROW__RHS_SLOT = eINSTANCE.getChangeSetTableRow_RhsSlot();

		/**
		 * The meta object literal for the '<em><b>Rhs Spot</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_TABLE_ROW__RHS_SPOT = eINSTANCE.getChangeSetTableRow_RhsSpot();

		/**
		 * The meta object literal for the '<em><b>Rhs Optional</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_TABLE_ROW__RHS_OPTIONAL = eINSTANCE.getChangeSetTableRow_RhsOptional();

		/**
		 * The meta object literal for the '<em><b>Rhs Valid</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_TABLE_ROW__RHS_VALID = eINSTANCE.getChangeSetTableRow_RhsValid();

		/**
		 * The meta object literal for the '<em><b>Rhs Non Shipped</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_TABLE_ROW__RHS_NON_SHIPPED = eINSTANCE.getChangeSetTableRow_RhsNonShipped();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRootImpl <em>Change Set Table Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRootImpl
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getChangeSetTableRoot()
		 * @generated
		 */
		EClass CHANGE_SET_TABLE_ROOT = eINSTANCE.getChangeSetTableRoot();

		/**
		 * The meta object literal for the '<em><b>Groups</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_TABLE_ROOT__GROUPS = eINSTANCE.getChangeSetTableRoot_Groups();

		/**
		 * The meta object literal for the '<em><b>Row Data Group</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW_DATA__ROW_DATA_GROUP = eINSTANCE.getChangeSetRowData_RowDataGroup();

		/**
		 * The meta object literal for the '<em><b>Event Grouping</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW_DATA__EVENT_GROUPING = eINSTANCE.getChangeSetRowData_EventGrouping();

		/**
		 * The meta object literal for the '<em><b>Vessel Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_ROW_DATA__VESSEL_NAME = eINSTANCE.getChangeSetRowData_VesselName();

		/**
		 * The meta object literal for the '<em><b>Vessel Short Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_ROW_DATA__VESSEL_SHORT_NAME = eINSTANCE.getChangeSetRowData_VesselShortName();

		/**
		 * The meta object literal for the '<em>Scenario Result</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.scenario.service.ui.ScenarioResult
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getScenarioResult()
		 * @generated
		 */
		EDataType SCENARIO_RESULT = eINSTANCE.getScenarioResult();

	}

} //ChangesetPackage
