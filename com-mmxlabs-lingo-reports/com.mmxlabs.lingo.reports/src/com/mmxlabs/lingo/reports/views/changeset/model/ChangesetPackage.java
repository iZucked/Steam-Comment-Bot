/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
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
	 * The feature id for the '<em><b>Metrics To Default Base</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__METRICS_TO_DEFAULT_BASE = 0;

	/**
	 * The feature id for the '<em><b>Metrics To Alternative Base</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__METRICS_TO_ALTERNATIVE_BASE = 1;

	/**
	 * The feature id for the '<em><b>Base Scenario</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__BASE_SCENARIO = 2;

	/**
	 * The feature id for the '<em><b>Current Scenario</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__CURRENT_SCENARIO = 3;

	/**
	 * The feature id for the '<em><b>Alt Base Scenario</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__ALT_BASE_SCENARIO = 4;

	/**
	 * The feature id for the '<em><b>Alt Current Scenario</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__ALT_CURRENT_SCENARIO = 5;

	/**
	 * The feature id for the '<em><b>Change Set Rows To Default Base</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__CHANGE_SET_ROWS_TO_DEFAULT_BASE = 6;

	/**
	 * The feature id for the '<em><b>Change Set Rows To Alternative Base</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__CHANGE_SET_ROWS_TO_ALTERNATIVE_BASE = 7;

	/**
	 * The feature id for the '<em><b>Current Metrics</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__CURRENT_METRICS = 8;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__DESCRIPTION = 9;

	/**
	 * The feature id for the '<em><b>Change Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__CHANGE_DESCRIPTION = 10;

	/**
	 * The feature id for the '<em><b>User Settings</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__USER_SETTINGS = 11;

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
	 * The meta object id for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetWiringGroupImpl <em>Change Set Wiring Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetWiringGroupImpl
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getChangeSetWiringGroup()
	 * @generated
	 */
	int CHANGE_SET_WIRING_GROUP = 5;

	/**
	 * The feature id for the '<em><b>Rows</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_WIRING_GROUP__ROWS = 0;

	/**
	 * The number of structural features of the '<em>Change Set Wiring Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_WIRING_GROUP_FEATURE_COUNT = 1;

	/**
	 * The operation id for the '<em>Is Wiring Change</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_WIRING_GROUP___IS_WIRING_CHANGE = 0;

	/**
	 * The operation id for the '<em>Is Vessel Change</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_WIRING_GROUP___IS_VESSEL_CHANGE = 1;

	/**
	 * The operation id for the '<em>Is Date Change</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_WIRING_GROUP___IS_DATE_CHANGE = 2;

	/**
	 * The number of operations of the '<em>Change Set Wiring Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_WIRING_GROUP_OPERATION_COUNT = 3;

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
	 * The feature id for the '<em><b>Vessel Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__VESSEL_TYPE = 5;

	/**
	 * The feature id for the '<em><b>Vessel Charter Number</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__VESSEL_CHARTER_NUMBER = 6;

	/**
	 * The feature id for the '<em><b>Lhs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__LHS_NAME = 7;

	/**
	 * The feature id for the '<em><b>Rhs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__RHS_NAME = 8;

	/**
	 * The feature id for the '<em><b>Lhs Link</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__LHS_LINK = 9;

	/**
	 * The feature id for the '<em><b>Rhs Link</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__RHS_LINK = 10;

	/**
	 * The feature id for the '<em><b>Lhs Slot</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__LHS_SLOT = 11;

	/**
	 * The feature id for the '<em><b>Lhs Spot</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__LHS_SPOT = 12;

	/**
	 * The feature id for the '<em><b>Lhs Optional</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__LHS_OPTIONAL = 13;

	/**
	 * The feature id for the '<em><b>Rhs Slot</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__RHS_SLOT = 14;

	/**
	 * The feature id for the '<em><b>Rhs Spot</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__RHS_SPOT = 15;

	/**
	 * The feature id for the '<em><b>Rhs Optional</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__RHS_OPTIONAL = 16;

	/**
	 * The feature id for the '<em><b>Lhs Non Shipped</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__LHS_NON_SHIPPED = 17;

	/**
	 * The feature id for the '<em><b>Rhs Non Shipped</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__RHS_NON_SHIPPED = 18;

	/**
	 * The feature id for the '<em><b>Load Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__LOAD_SLOT = 19;

	/**
	 * The feature id for the '<em><b>Discharge Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__DISCHARGE_SLOT = 20;

	/**
	 * The feature id for the '<em><b>Load Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__LOAD_ALLOCATION = 21;

	/**
	 * The feature id for the '<em><b>Discharge Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__DISCHARGE_ALLOCATION = 22;

	/**
	 * The feature id for the '<em><b>Open Load Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__OPEN_LOAD_ALLOCATION = 23;

	/**
	 * The feature id for the '<em><b>Open Discharge Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__OPEN_DISCHARGE_ALLOCATION = 24;

	/**
	 * The feature id for the '<em><b>Lhs Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__LHS_EVENT = 25;

	/**
	 * The feature id for the '<em><b>Rhs Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__RHS_EVENT = 26;

	/**
	 * The feature id for the '<em><b>Lhs Group Profit And Loss</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__LHS_GROUP_PROFIT_AND_LOSS = 27;

	/**
	 * The feature id for the '<em><b>Rhs Group Profit And Loss</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__RHS_GROUP_PROFIT_AND_LOSS = 28;

	/**
	 * The feature id for the '<em><b>Paper Deal Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA__PAPER_DEAL_ALLOCATION = 29;

	/**
	 * The number of structural features of the '<em>Change Set Row Data</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_DATA_FEATURE_COUNT = 30;

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
	 * The feature id for the '<em><b>Rows</b></em>' reference list.
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
	 * The feature id for the '<em><b>Base Scenario</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_GROUP__BASE_SCENARIO = 5;

	/**
	 * The feature id for the '<em><b>Current Scenario</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_GROUP__CURRENT_SCENARIO = 6;

	/**
	 * The feature id for the '<em><b>Linked Group</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_GROUP__LINKED_GROUP = 7;

	/**
	 * The feature id for the '<em><b>Complexity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_GROUP__COMPLEXITY = 8;

	/**
	 * The feature id for the '<em><b>Sort Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_GROUP__SORT_VALUE = 9;

	/**
	 * The feature id for the '<em><b>Group Sort Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_GROUP__GROUP_SORT_VALUE = 10;

	/**
	 * The feature id for the '<em><b>Group Object</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_GROUP__GROUP_OBJECT = 11;

	/**
	 * The feature id for the '<em><b>Group Alternative</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_GROUP__GROUP_ALTERNATIVE = 12;

	/**
	 * The feature id for the '<em><b>Table Root</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_GROUP__TABLE_ROOT = 13;

	/**
	 * The number of structural features of the '<em>Change Set Table Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_GROUP_FEATURE_COUNT = 14;

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
	 * The feature id for the '<em><b>Lhs Before</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__LHS_BEFORE = 0;

	/**
	 * The feature id for the '<em><b>Lhs After</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__LHS_AFTER = 1;

	/**
	 * The feature id for the '<em><b>Lhs Valid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__LHS_VALID = 2;

	/**
	 * The feature id for the '<em><b>Current Rhs Before</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__CURRENT_RHS_BEFORE = 3;

	/**
	 * The feature id for the '<em><b>Current Rhs After</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__CURRENT_RHS_AFTER = 4;

	/**
	 * The feature id for the '<em><b>Previous Rhs Before</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__PREVIOUS_RHS_BEFORE = 5;

	/**
	 * The feature id for the '<em><b>Previous Rhs After</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__PREVIOUS_RHS_AFTER = 6;

	/**
	 * The feature id for the '<em><b>Current Rhs Valid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__CURRENT_RHS_VALID = 7;

	/**
	 * The feature id for the '<em><b>Wiring Change</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__WIRING_CHANGE = 8;

	/**
	 * The feature id for the '<em><b>Vessel Change</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__VESSEL_CHANGE = 9;

	/**
	 * The feature id for the '<em><b>Date Change</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__DATE_CHANGE = 10;

	/**
	 * The feature id for the '<em><b>Wiring Group</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__WIRING_GROUP = 11;

	/**
	 * The feature id for the '<em><b>Table Group</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW__TABLE_GROUP = 12;

	/**
	 * The number of structural features of the '<em>Change Set Table Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW_FEATURE_COUNT = 13;

	/**
	 * The operation id for the '<em>Is Major Change</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW___IS_MAJOR_CHANGE = 0;

	/**
	 * The operation id for the '<em>Get LHS After Or Before</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW___GET_LHS_AFTER_OR_BEFORE = 1;

	/**
	 * The operation id for the '<em>Get Current RHS After Or Before</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW___GET_CURRENT_RHS_AFTER_OR_BEFORE = 2;

	/**
	 * The operation id for the '<em>Get Lhs Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW___GET_LHS_NAME = 3;

	/**
	 * The operation id for the '<em>Get Current Rhs Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW___GET_CURRENT_RHS_NAME = 4;

	/**
	 * The operation id for the '<em>Get Before Vessel Charter Number</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW___GET_BEFORE_VESSEL_CHARTER_NUMBER = 5;

	/**
	 * The operation id for the '<em>Get Before Vessel Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW___GET_BEFORE_VESSEL_NAME = 6;

	/**
	 * The operation id for the '<em>Get Before Vessel Short Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW___GET_BEFORE_VESSEL_SHORT_NAME = 7;

	/**
	 * The operation id for the '<em>Get Before Vessel Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW___GET_BEFORE_VESSEL_TYPE = 8;

	/**
	 * The operation id for the '<em>Get After Vessel Charter Number</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW___GET_AFTER_VESSEL_CHARTER_NUMBER = 9;

	/**
	 * The operation id for the '<em>Get After Vessel Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW___GET_AFTER_VESSEL_NAME = 10;

	/**
	 * The operation id for the '<em>Get After Vessel Short Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW___GET_AFTER_VESSEL_SHORT_NAME = 11;

	/**
	 * The operation id for the '<em>Get After Vessel Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW___GET_AFTER_VESSEL_TYPE = 12;

	/**
	 * The operation id for the '<em>Is Lhs Slot</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW___IS_LHS_SLOT = 13;

	/**
	 * The operation id for the '<em>Is Current Rhs Slot</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW___IS_CURRENT_RHS_SLOT = 14;

	/**
	 * The operation id for the '<em>Is Lhs Spot</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW___IS_LHS_SPOT = 15;

	/**
	 * The operation id for the '<em>Is Current Rhs Optional</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW___IS_CURRENT_RHS_OPTIONAL = 16;

	/**
	 * The operation id for the '<em>Is Lhs Optional</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW___IS_LHS_OPTIONAL = 17;

	/**
	 * The operation id for the '<em>Is Current Rhs Spot</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW___IS_CURRENT_RHS_SPOT = 18;

	/**
	 * The operation id for the '<em>Is Lhs Non Shipped</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW___IS_LHS_NON_SHIPPED = 19;

	/**
	 * The operation id for the '<em>Is Current Rhs Non Shipped</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW___IS_CURRENT_RHS_NON_SHIPPED = 20;

	/**
	 * The number of operations of the '<em>Change Set Table Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_TABLE_ROW_OPERATION_COUNT = 21;

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
	 * The meta object id for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetVesselType <em>Change Set Vessel Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetVesselType
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getChangeSetVesselType()
	 * @generated
	 */
	int CHANGE_SET_VESSEL_TYPE = 10;

	/**
	 * The meta object id for the '<em>Scenario Result</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.scenario.service.ScenarioResult
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getScenarioResult()
	 * @generated
	 */
	int SCENARIO_RESULT = 11;


	/**
	 * The meta object id for the '<em>Change Description</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.analytics.ChangeDescription
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getChangeDescription()
	 * @generated
	 */
	int CHANGE_DESCRIPTION = 12;


	/**
	 * The meta object id for the '<em>User Settings</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.parameters.UserSettings
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getUserSettings()
	 * @generated
	 */
	int USER_SETTINGS = 13;


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
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getMetricsToDefaultBase <em>Metrics To Default Base</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Metrics To Default Base</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getMetricsToDefaultBase()
	 * @see #getChangeSet()
	 * @generated
	 */
	EReference getChangeSet_MetricsToDefaultBase();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getMetricsToAlternativeBase <em>Metrics To Alternative Base</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Metrics To Alternative Base</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getMetricsToAlternativeBase()
	 * @see #getChangeSet()
	 * @generated
	 */
	EReference getChangeSet_MetricsToAlternativeBase();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getAltBaseScenario <em>Alt Base Scenario</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Alt Base Scenario</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getAltBaseScenario()
	 * @see #getChangeSet()
	 * @generated
	 */
	EAttribute getChangeSet_AltBaseScenario();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getAltCurrentScenario <em>Alt Current Scenario</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Alt Current Scenario</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getAltCurrentScenario()
	 * @see #getChangeSet()
	 * @generated
	 */
	EAttribute getChangeSet_AltCurrentScenario();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getChangeSetRowsToDefaultBase <em>Change Set Rows To Default Base</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Change Set Rows To Default Base</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getChangeSetRowsToDefaultBase()
	 * @see #getChangeSet()
	 * @generated
	 */
	EReference getChangeSet_ChangeSetRowsToDefaultBase();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getChangeSetRowsToAlternativeBase <em>Change Set Rows To Alternative Base</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Change Set Rows To Alternative Base</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getChangeSetRowsToAlternativeBase()
	 * @see #getChangeSet()
	 * @generated
	 */
	EReference getChangeSet_ChangeSetRowsToAlternativeBase();

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
	 * Returns the meta object for class '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetWiringGroup <em>Change Set Wiring Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Change Set Wiring Group</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetWiringGroup
	 * @generated
	 */
	EClass getChangeSetWiringGroup();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetWiringGroup#getRows <em>Rows</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Rows</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetWiringGroup#getRows()
	 * @see #getChangeSetWiringGroup()
	 * @generated
	 */
	EReference getChangeSetWiringGroup_Rows();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetWiringGroup#isWiringChange() <em>Is Wiring Change</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Is Wiring Change</em>' operation.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetWiringGroup#isWiringChange()
	 * @generated
	 */
	EOperation getChangeSetWiringGroup__IsWiringChange();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetWiringGroup#isVesselChange() <em>Is Vessel Change</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Is Vessel Change</em>' operation.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetWiringGroup#isVesselChange()
	 * @generated
	 */
	EOperation getChangeSetWiringGroup__IsVesselChange();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetWiringGroup#isDateChange() <em>Is Date Change</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Is Date Change</em>' operation.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetWiringGroup#isDateChange()
	 * @generated
	 */
	EOperation getChangeSetWiringGroup__IsDateChange();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isLhsSlot <em>Lhs Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lhs Slot</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isLhsSlot()
	 * @see #getChangeSetRowData()
	 * @generated
	 */
	EAttribute getChangeSetRowData_LhsSlot();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isLhsSpot <em>Lhs Spot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lhs Spot</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isLhsSpot()
	 * @see #getChangeSetRowData()
	 * @generated
	 */
	EAttribute getChangeSetRowData_LhsSpot();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isLhsOptional <em>Lhs Optional</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lhs Optional</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isLhsOptional()
	 * @see #getChangeSetRowData()
	 * @generated
	 */
	EAttribute getChangeSetRowData_LhsOptional();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isRhsSlot <em>Rhs Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Rhs Slot</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isRhsSlot()
	 * @see #getChangeSetRowData()
	 * @generated
	 */
	EAttribute getChangeSetRowData_RhsSlot();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isRhsSpot <em>Rhs Spot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Rhs Spot</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isRhsSpot()
	 * @see #getChangeSetRowData()
	 * @generated
	 */
	EAttribute getChangeSetRowData_RhsSpot();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isRhsOptional <em>Rhs Optional</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Rhs Optional</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isRhsOptional()
	 * @see #getChangeSetRowData()
	 * @generated
	 */
	EAttribute getChangeSetRowData_RhsOptional();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isLhsNonShipped <em>Lhs Non Shipped</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lhs Non Shipped</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isLhsNonShipped()
	 * @see #getChangeSetRowData()
	 * @generated
	 */
	EAttribute getChangeSetRowData_LhsNonShipped();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isRhsNonShipped <em>Rhs Non Shipped</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Rhs Non Shipped</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isRhsNonShipped()
	 * @see #getChangeSetRowData()
	 * @generated
	 */
	EAttribute getChangeSetRowData_RhsNonShipped();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getChangeDescription <em>Change Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Change Description</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getChangeDescription()
	 * @see #getChangeSet()
	 * @generated
	 */
	EAttribute getChangeSet_ChangeDescription();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getUserSettings <em>User Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>User Settings</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getUserSettings()
	 * @see #getChangeSet()
	 * @generated
	 */
	EAttribute getChangeSet_UserSettings();

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
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getPaperDealAllocation <em>Paper Deal Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Paper Deal Allocation</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getPaperDealAllocation()
	 * @see #getChangeSetRowData()
	 * @generated
	 */
	EReference getChangeSetRowData_PaperDealAllocation();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getVesselCharterNumber <em>Vessel Charter Number</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Vessel Charter Number</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getVesselCharterNumber()
	 * @see #getChangeSetRowData()
	 * @generated
	 */
	EAttribute getChangeSetRowData_VesselCharterNumber();

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
	 * Returns the meta object for the reference list '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getRows <em>Rows</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Rows</em>'.
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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getBaseScenario <em>Base Scenario</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Base Scenario</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getBaseScenario()
	 * @see #getChangeSetTableGroup()
	 * @generated
	 */
	EAttribute getChangeSetTableGroup_BaseScenario();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getCurrentScenario <em>Current Scenario</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Current Scenario</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getCurrentScenario()
	 * @see #getChangeSetTableGroup()
	 * @generated
	 */
	EAttribute getChangeSetTableGroup_CurrentScenario();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getLinkedGroup <em>Linked Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Linked Group</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getLinkedGroup()
	 * @see #getChangeSetTableGroup()
	 * @generated
	 */
	EReference getChangeSetTableGroup_LinkedGroup();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getComplexity <em>Complexity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Complexity</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getComplexity()
	 * @see #getChangeSetTableGroup()
	 * @generated
	 */
	EAttribute getChangeSetTableGroup_Complexity();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getSortValue <em>Sort Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Sort Value</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getSortValue()
	 * @see #getChangeSetTableGroup()
	 * @generated
	 */
	EAttribute getChangeSetTableGroup_SortValue();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getGroupSortValue <em>Group Sort Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Group Sort Value</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getGroupSortValue()
	 * @see #getChangeSetTableGroup()
	 * @generated
	 */
	EAttribute getChangeSetTableGroup_GroupSortValue();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getGroupObject <em>Group Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Group Object</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getGroupObject()
	 * @see #getChangeSetTableGroup()
	 * @generated
	 */
	EAttribute getChangeSetTableGroup_GroupObject();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#isGroupAlternative <em>Group Alternative</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Group Alternative</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#isGroupAlternative()
	 * @see #getChangeSetTableGroup()
	 * @generated
	 */
	EAttribute getChangeSetTableGroup_GroupAlternative();

	/**
	 * Returns the meta object for the container reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getTableRoot <em>Table Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Table Root</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getTableRoot()
	 * @see #getChangeSetTableGroup()
	 * @generated
	 */
	EReference getChangeSetTableGroup_TableRoot();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isDateChange <em>Date Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Date Change</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isDateChange()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EAttribute getChangeSetTableRow_DateChange();

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
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getCurrentRhsBefore <em>Current Rhs Before</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Current Rhs Before</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getCurrentRhsBefore()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EReference getChangeSetTableRow_CurrentRhsBefore();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getCurrentRhsAfter <em>Current Rhs After</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Current Rhs After</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getCurrentRhsAfter()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EReference getChangeSetTableRow_CurrentRhsAfter();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getPreviousRhsBefore <em>Previous Rhs Before</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Previous Rhs Before</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getPreviousRhsBefore()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EReference getChangeSetTableRow_PreviousRhsBefore();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getPreviousRhsAfter <em>Previous Rhs After</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Previous Rhs After</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getPreviousRhsAfter()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EReference getChangeSetTableRow_PreviousRhsAfter();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isCurrentRhsValid <em>Current Rhs Valid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Current Rhs Valid</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isCurrentRhsValid()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EAttribute getChangeSetTableRow_CurrentRhsValid();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getWiringGroup <em>Wiring Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Wiring Group</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getWiringGroup()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EReference getChangeSetTableRow_WiringGroup();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getTableGroup <em>Table Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Table Group</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getTableGroup()
	 * @see #getChangeSetTableRow()
	 * @generated
	 */
	EReference getChangeSetTableRow_TableGroup();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isMajorChange() <em>Is Major Change</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Is Major Change</em>' operation.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isMajorChange()
	 * @generated
	 */
	EOperation getChangeSetTableRow__IsMajorChange();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getLHSAfterOrBefore() <em>Get LHS After Or Before</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get LHS After Or Before</em>' operation.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getLHSAfterOrBefore()
	 * @generated
	 */
	EOperation getChangeSetTableRow__GetLHSAfterOrBefore();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getCurrentRHSAfterOrBefore() <em>Get Current RHS After Or Before</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Current RHS After Or Before</em>' operation.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getCurrentRHSAfterOrBefore()
	 * @generated
	 */
	EOperation getChangeSetTableRow__GetCurrentRHSAfterOrBefore();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getLhsName() <em>Get Lhs Name</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Lhs Name</em>' operation.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getLhsName()
	 * @generated
	 */
	EOperation getChangeSetTableRow__GetLhsName();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getCurrentRhsName() <em>Get Current Rhs Name</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Current Rhs Name</em>' operation.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getCurrentRhsName()
	 * @generated
	 */
	EOperation getChangeSetTableRow__GetCurrentRhsName();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getBeforeVesselCharterNumber() <em>Get Before Vessel Charter Number</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Before Vessel Charter Number</em>' operation.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getBeforeVesselCharterNumber()
	 * @generated
	 */
	EOperation getChangeSetTableRow__GetBeforeVesselCharterNumber();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getBeforeVesselName() <em>Get Before Vessel Name</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Before Vessel Name</em>' operation.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getBeforeVesselName()
	 * @generated
	 */
	EOperation getChangeSetTableRow__GetBeforeVesselName();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getBeforeVesselShortName() <em>Get Before Vessel Short Name</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Before Vessel Short Name</em>' operation.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getBeforeVesselShortName()
	 * @generated
	 */
	EOperation getChangeSetTableRow__GetBeforeVesselShortName();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getBeforeVesselType() <em>Get Before Vessel Type</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Before Vessel Type</em>' operation.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getBeforeVesselType()
	 * @generated
	 */
	EOperation getChangeSetTableRow__GetBeforeVesselType();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getAfterVesselCharterNumber() <em>Get After Vessel Charter Number</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get After Vessel Charter Number</em>' operation.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getAfterVesselCharterNumber()
	 * @generated
	 */
	EOperation getChangeSetTableRow__GetAfterVesselCharterNumber();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getAfterVesselName() <em>Get After Vessel Name</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get After Vessel Name</em>' operation.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getAfterVesselName()
	 * @generated
	 */
	EOperation getChangeSetTableRow__GetAfterVesselName();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getAfterVesselShortName() <em>Get After Vessel Short Name</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get After Vessel Short Name</em>' operation.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getAfterVesselShortName()
	 * @generated
	 */
	EOperation getChangeSetTableRow__GetAfterVesselShortName();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getAfterVesselType() <em>Get After Vessel Type</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get After Vessel Type</em>' operation.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getAfterVesselType()
	 * @generated
	 */
	EOperation getChangeSetTableRow__GetAfterVesselType();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isLhsSlot() <em>Is Lhs Slot</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Is Lhs Slot</em>' operation.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isLhsSlot()
	 * @generated
	 */
	EOperation getChangeSetTableRow__IsLhsSlot();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isCurrentRhsSlot() <em>Is Current Rhs Slot</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Is Current Rhs Slot</em>' operation.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isCurrentRhsSlot()
	 * @generated
	 */
	EOperation getChangeSetTableRow__IsCurrentRhsSlot();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isLhsSpot() <em>Is Lhs Spot</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Is Lhs Spot</em>' operation.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isLhsSpot()
	 * @generated
	 */
	EOperation getChangeSetTableRow__IsLhsSpot();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isCurrentRhsOptional() <em>Is Current Rhs Optional</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Is Current Rhs Optional</em>' operation.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isCurrentRhsOptional()
	 * @generated
	 */
	EOperation getChangeSetTableRow__IsCurrentRhsOptional();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isLhsOptional() <em>Is Lhs Optional</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Is Lhs Optional</em>' operation.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isLhsOptional()
	 * @generated
	 */
	EOperation getChangeSetTableRow__IsLhsOptional();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isCurrentRhsSpot() <em>Is Current Rhs Spot</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Is Current Rhs Spot</em>' operation.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isCurrentRhsSpot()
	 * @generated
	 */
	EOperation getChangeSetTableRow__IsCurrentRhsSpot();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isLhsNonShipped() <em>Is Lhs Non Shipped</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Is Lhs Non Shipped</em>' operation.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isLhsNonShipped()
	 * @generated
	 */
	EOperation getChangeSetTableRow__IsLhsNonShipped();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isCurrentRhsNonShipped() <em>Is Current Rhs Non Shipped</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Is Current Rhs Non Shipped</em>' operation.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isCurrentRhsNonShipped()
	 * @generated
	 */
	EOperation getChangeSetTableRow__IsCurrentRhsNonShipped();

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
	 * Returns the meta object for enum '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetVesselType <em>Change Set Vessel Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Change Set Vessel Type</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetVesselType
	 * @generated
	 */
	EEnum getChangeSetVesselType();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getVesselType <em>Vessel Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Vessel Type</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getVesselType()
	 * @see #getChangeSetRowData()
	 * @generated
	 */
	EAttribute getChangeSetRowData_VesselType();

	/**
	 * Returns the meta object for data type '{@link com.mmxlabs.scenario.service.ScenarioResult <em>Scenario Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Scenario Result</em>'.
	 * @see com.mmxlabs.scenario.service.ScenarioResult
	 * @model instanceClass="com.mmxlabs.scenario.service.ScenarioResult"
	 * @generated
	 */
	EDataType getScenarioResult();

	/**
	 * Returns the meta object for data type '{@link com.mmxlabs.models.lng.analytics.ChangeDescription <em>Change Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Change Description</em>'.
	 * @see com.mmxlabs.models.lng.analytics.ChangeDescription
	 * @model instanceClass="com.mmxlabs.models.lng.analytics.ChangeDescription"
	 * @generated
	 */
	EDataType getChangeDescription();

	/**
	 * Returns the meta object for data type '{@link com.mmxlabs.models.lng.parameters.UserSettings <em>User Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>User Settings</em>'.
	 * @see com.mmxlabs.models.lng.parameters.UserSettings
	 * @model instanceClass="com.mmxlabs.models.lng.parameters.UserSettings"
	 * @generated
	 */
	EDataType getUserSettings();

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
		 * The meta object literal for the '<em><b>Metrics To Default Base</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET__METRICS_TO_DEFAULT_BASE = eINSTANCE.getChangeSet_MetricsToDefaultBase();

		/**
		 * The meta object literal for the '<em><b>Metrics To Alternative Base</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET__METRICS_TO_ALTERNATIVE_BASE = eINSTANCE.getChangeSet_MetricsToAlternativeBase();

		/**
		 * The meta object literal for the '<em><b>Base Scenario</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET__BASE_SCENARIO = eINSTANCE.getChangeSet_BaseScenario();

		/**
		 * The meta object literal for the '<em><b>Current Scenario</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET__CURRENT_SCENARIO = eINSTANCE.getChangeSet_CurrentScenario();

		/**
		 * The meta object literal for the '<em><b>Alt Base Scenario</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET__ALT_BASE_SCENARIO = eINSTANCE.getChangeSet_AltBaseScenario();

		/**
		 * The meta object literal for the '<em><b>Alt Current Scenario</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET__ALT_CURRENT_SCENARIO = eINSTANCE.getChangeSet_AltCurrentScenario();

		/**
		 * The meta object literal for the '<em><b>Change Set Rows To Default Base</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET__CHANGE_SET_ROWS_TO_DEFAULT_BASE = eINSTANCE.getChangeSet_ChangeSetRowsToDefaultBase();

		/**
		 * The meta object literal for the '<em><b>Change Set Rows To Alternative Base</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET__CHANGE_SET_ROWS_TO_ALTERNATIVE_BASE = eINSTANCE.getChangeSet_ChangeSetRowsToAlternativeBase();

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
		 * The meta object literal for the '<em><b>Change Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET__CHANGE_DESCRIPTION = eINSTANCE.getChangeSet_ChangeDescription();

		/**
		 * The meta object literal for the '<em><b>User Settings</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET__USER_SETTINGS = eINSTANCE.getChangeSet_UserSettings();

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
		 * The meta object literal for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetWiringGroupImpl <em>Change Set Wiring Group</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetWiringGroupImpl
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getChangeSetWiringGroup()
		 * @generated
		 */
		EClass CHANGE_SET_WIRING_GROUP = eINSTANCE.getChangeSetWiringGroup();

		/**
		 * The meta object literal for the '<em><b>Rows</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_WIRING_GROUP__ROWS = eINSTANCE.getChangeSetWiringGroup_Rows();

		/**
		 * The meta object literal for the '<em><b>Is Wiring Change</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CHANGE_SET_WIRING_GROUP___IS_WIRING_CHANGE = eINSTANCE.getChangeSetWiringGroup__IsWiringChange();

		/**
		 * The meta object literal for the '<em><b>Is Vessel Change</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CHANGE_SET_WIRING_GROUP___IS_VESSEL_CHANGE = eINSTANCE.getChangeSetWiringGroup__IsVesselChange();

		/**
		 * The meta object literal for the '<em><b>Is Date Change</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CHANGE_SET_WIRING_GROUP___IS_DATE_CHANGE = eINSTANCE.getChangeSetWiringGroup__IsDateChange();

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
		 * The meta object literal for the '<em><b>Lhs Slot</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_ROW_DATA__LHS_SLOT = eINSTANCE.getChangeSetRowData_LhsSlot();

		/**
		 * The meta object literal for the '<em><b>Lhs Spot</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_ROW_DATA__LHS_SPOT = eINSTANCE.getChangeSetRowData_LhsSpot();

		/**
		 * The meta object literal for the '<em><b>Lhs Optional</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_ROW_DATA__LHS_OPTIONAL = eINSTANCE.getChangeSetRowData_LhsOptional();

		/**
		 * The meta object literal for the '<em><b>Rhs Slot</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_ROW_DATA__RHS_SLOT = eINSTANCE.getChangeSetRowData_RhsSlot();

		/**
		 * The meta object literal for the '<em><b>Rhs Spot</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_ROW_DATA__RHS_SPOT = eINSTANCE.getChangeSetRowData_RhsSpot();

		/**
		 * The meta object literal for the '<em><b>Rhs Optional</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_ROW_DATA__RHS_OPTIONAL = eINSTANCE.getChangeSetRowData_RhsOptional();

		/**
		 * The meta object literal for the '<em><b>Lhs Non Shipped</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_ROW_DATA__LHS_NON_SHIPPED = eINSTANCE.getChangeSetRowData_LhsNonShipped();

		/**
		 * The meta object literal for the '<em><b>Rhs Non Shipped</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_ROW_DATA__RHS_NON_SHIPPED = eINSTANCE.getChangeSetRowData_RhsNonShipped();

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
		 * The meta object literal for the '<em><b>Paper Deal Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW_DATA__PAPER_DEAL_ALLOCATION = eINSTANCE.getChangeSetRowData_PaperDealAllocation();

		/**
		 * The meta object literal for the '<em><b>Vessel Charter Number</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_ROW_DATA__VESSEL_CHARTER_NUMBER = eINSTANCE.getChangeSetRowData_VesselCharterNumber();

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
		 * The meta object literal for the '<em><b>Rows</b></em>' reference list feature.
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
		 * The meta object literal for the '<em><b>Base Scenario</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_TABLE_GROUP__BASE_SCENARIO = eINSTANCE.getChangeSetTableGroup_BaseScenario();

		/**
		 * The meta object literal for the '<em><b>Current Scenario</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_TABLE_GROUP__CURRENT_SCENARIO = eINSTANCE.getChangeSetTableGroup_CurrentScenario();

		/**
		 * The meta object literal for the '<em><b>Linked Group</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_TABLE_GROUP__LINKED_GROUP = eINSTANCE.getChangeSetTableGroup_LinkedGroup();

		/**
		 * The meta object literal for the '<em><b>Complexity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_TABLE_GROUP__COMPLEXITY = eINSTANCE.getChangeSetTableGroup_Complexity();

		/**
		 * The meta object literal for the '<em><b>Sort Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_TABLE_GROUP__SORT_VALUE = eINSTANCE.getChangeSetTableGroup_SortValue();

		/**
		 * The meta object literal for the '<em><b>Group Sort Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_TABLE_GROUP__GROUP_SORT_VALUE = eINSTANCE.getChangeSetTableGroup_GroupSortValue();

		/**
		 * The meta object literal for the '<em><b>Group Object</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_TABLE_GROUP__GROUP_OBJECT = eINSTANCE.getChangeSetTableGroup_GroupObject();

		/**
		 * The meta object literal for the '<em><b>Group Alternative</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_TABLE_GROUP__GROUP_ALTERNATIVE = eINSTANCE.getChangeSetTableGroup_GroupAlternative();

		/**
		 * The meta object literal for the '<em><b>Table Root</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_TABLE_GROUP__TABLE_ROOT = eINSTANCE.getChangeSetTableGroup_TableRoot();

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
		 * The meta object literal for the '<em><b>Date Change</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_TABLE_ROW__DATE_CHANGE = eINSTANCE.getChangeSetTableRow_DateChange();

		/**
		 * The meta object literal for the '<em><b>Lhs Valid</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_TABLE_ROW__LHS_VALID = eINSTANCE.getChangeSetTableRow_LhsValid();

		/**
		 * The meta object literal for the '<em><b>Current Rhs Before</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_TABLE_ROW__CURRENT_RHS_BEFORE = eINSTANCE.getChangeSetTableRow_CurrentRhsBefore();

		/**
		 * The meta object literal for the '<em><b>Current Rhs After</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_TABLE_ROW__CURRENT_RHS_AFTER = eINSTANCE.getChangeSetTableRow_CurrentRhsAfter();

		/**
		 * The meta object literal for the '<em><b>Previous Rhs Before</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_TABLE_ROW__PREVIOUS_RHS_BEFORE = eINSTANCE.getChangeSetTableRow_PreviousRhsBefore();

		/**
		 * The meta object literal for the '<em><b>Previous Rhs After</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_TABLE_ROW__PREVIOUS_RHS_AFTER = eINSTANCE.getChangeSetTableRow_PreviousRhsAfter();

		/**
		 * The meta object literal for the '<em><b>Current Rhs Valid</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_TABLE_ROW__CURRENT_RHS_VALID = eINSTANCE.getChangeSetTableRow_CurrentRhsValid();

		/**
		 * The meta object literal for the '<em><b>Wiring Group</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_TABLE_ROW__WIRING_GROUP = eINSTANCE.getChangeSetTableRow_WiringGroup();

		/**
		 * The meta object literal for the '<em><b>Table Group</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_TABLE_ROW__TABLE_GROUP = eINSTANCE.getChangeSetTableRow_TableGroup();

		/**
		 * The meta object literal for the '<em><b>Is Major Change</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CHANGE_SET_TABLE_ROW___IS_MAJOR_CHANGE = eINSTANCE.getChangeSetTableRow__IsMajorChange();

		/**
		 * The meta object literal for the '<em><b>Get LHS After Or Before</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CHANGE_SET_TABLE_ROW___GET_LHS_AFTER_OR_BEFORE = eINSTANCE.getChangeSetTableRow__GetLHSAfterOrBefore();

		/**
		 * The meta object literal for the '<em><b>Get Current RHS After Or Before</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CHANGE_SET_TABLE_ROW___GET_CURRENT_RHS_AFTER_OR_BEFORE = eINSTANCE.getChangeSetTableRow__GetCurrentRHSAfterOrBefore();

		/**
		 * The meta object literal for the '<em><b>Get Lhs Name</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CHANGE_SET_TABLE_ROW___GET_LHS_NAME = eINSTANCE.getChangeSetTableRow__GetLhsName();

		/**
		 * The meta object literal for the '<em><b>Get Current Rhs Name</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CHANGE_SET_TABLE_ROW___GET_CURRENT_RHS_NAME = eINSTANCE.getChangeSetTableRow__GetCurrentRhsName();

		/**
		 * The meta object literal for the '<em><b>Get Before Vessel Charter Number</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CHANGE_SET_TABLE_ROW___GET_BEFORE_VESSEL_CHARTER_NUMBER = eINSTANCE.getChangeSetTableRow__GetBeforeVesselCharterNumber();

		/**
		 * The meta object literal for the '<em><b>Get Before Vessel Name</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CHANGE_SET_TABLE_ROW___GET_BEFORE_VESSEL_NAME = eINSTANCE.getChangeSetTableRow__GetBeforeVesselName();

		/**
		 * The meta object literal for the '<em><b>Get Before Vessel Short Name</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CHANGE_SET_TABLE_ROW___GET_BEFORE_VESSEL_SHORT_NAME = eINSTANCE.getChangeSetTableRow__GetBeforeVesselShortName();

		/**
		 * The meta object literal for the '<em><b>Get Before Vessel Type</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CHANGE_SET_TABLE_ROW___GET_BEFORE_VESSEL_TYPE = eINSTANCE.getChangeSetTableRow__GetBeforeVesselType();

		/**
		 * The meta object literal for the '<em><b>Get After Vessel Charter Number</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CHANGE_SET_TABLE_ROW___GET_AFTER_VESSEL_CHARTER_NUMBER = eINSTANCE.getChangeSetTableRow__GetAfterVesselCharterNumber();

		/**
		 * The meta object literal for the '<em><b>Get After Vessel Name</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CHANGE_SET_TABLE_ROW___GET_AFTER_VESSEL_NAME = eINSTANCE.getChangeSetTableRow__GetAfterVesselName();

		/**
		 * The meta object literal for the '<em><b>Get After Vessel Short Name</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CHANGE_SET_TABLE_ROW___GET_AFTER_VESSEL_SHORT_NAME = eINSTANCE.getChangeSetTableRow__GetAfterVesselShortName();

		/**
		 * The meta object literal for the '<em><b>Get After Vessel Type</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CHANGE_SET_TABLE_ROW___GET_AFTER_VESSEL_TYPE = eINSTANCE.getChangeSetTableRow__GetAfterVesselType();

		/**
		 * The meta object literal for the '<em><b>Is Lhs Slot</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CHANGE_SET_TABLE_ROW___IS_LHS_SLOT = eINSTANCE.getChangeSetTableRow__IsLhsSlot();

		/**
		 * The meta object literal for the '<em><b>Is Current Rhs Slot</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CHANGE_SET_TABLE_ROW___IS_CURRENT_RHS_SLOT = eINSTANCE.getChangeSetTableRow__IsCurrentRhsSlot();

		/**
		 * The meta object literal for the '<em><b>Is Lhs Spot</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CHANGE_SET_TABLE_ROW___IS_LHS_SPOT = eINSTANCE.getChangeSetTableRow__IsLhsSpot();

		/**
		 * The meta object literal for the '<em><b>Is Current Rhs Optional</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CHANGE_SET_TABLE_ROW___IS_CURRENT_RHS_OPTIONAL = eINSTANCE.getChangeSetTableRow__IsCurrentRhsOptional();

		/**
		 * The meta object literal for the '<em><b>Is Lhs Optional</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CHANGE_SET_TABLE_ROW___IS_LHS_OPTIONAL = eINSTANCE.getChangeSetTableRow__IsLhsOptional();

		/**
		 * The meta object literal for the '<em><b>Is Current Rhs Spot</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CHANGE_SET_TABLE_ROW___IS_CURRENT_RHS_SPOT = eINSTANCE.getChangeSetTableRow__IsCurrentRhsSpot();

		/**
		 * The meta object literal for the '<em><b>Is Lhs Non Shipped</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CHANGE_SET_TABLE_ROW___IS_LHS_NON_SHIPPED = eINSTANCE.getChangeSetTableRow__IsLhsNonShipped();

		/**
		 * The meta object literal for the '<em><b>Is Current Rhs Non Shipped</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CHANGE_SET_TABLE_ROW___IS_CURRENT_RHS_NON_SHIPPED = eINSTANCE.getChangeSetTableRow__IsCurrentRhsNonShipped();

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
		 * The meta object literal for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetVesselType <em>Change Set Vessel Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetVesselType
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getChangeSetVesselType()
		 * @generated
		 */
		EEnum CHANGE_SET_VESSEL_TYPE = eINSTANCE.getChangeSetVesselType();

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
		 * The meta object literal for the '<em><b>Vessel Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_ROW_DATA__VESSEL_TYPE = eINSTANCE.getChangeSetRowData_VesselType();

		/**
		 * The meta object literal for the '<em>Scenario Result</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.scenario.service.ScenarioResult
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getScenarioResult()
		 * @generated
		 */
		EDataType SCENARIO_RESULT = eINSTANCE.getScenarioResult();

		/**
		 * The meta object literal for the '<em>Change Description</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.analytics.ChangeDescription
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getChangeDescription()
		 * @generated
		 */
		EDataType CHANGE_DESCRIPTION = eINSTANCE.getChangeDescription();

		/**
		 * The meta object literal for the '<em>User Settings</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.parameters.UserSettings
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getUserSettings()
		 * @generated
		 */
		EDataType USER_SETTINGS = eINSTANCE.getUserSettings();

	}

} //ChangesetPackage
