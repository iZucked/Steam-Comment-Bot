/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
	 * The number of structural features of the '<em>Change Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_FEATURE_COUNT = 11;

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
	 * The meta object id for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl <em>Change Set Row</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getChangeSetRow()
	 * @generated
	 */
	int CHANGE_SET_ROW = 4;

	/**
	 * The feature id for the '<em><b>Lhs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW__LHS_NAME = 0;

	/**
	 * The feature id for the '<em><b>Rhs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW__RHS_NAME = 1;

	/**
	 * The feature id for the '<em><b>Original Vessel Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW__ORIGINAL_VESSEL_NAME = 2;

	/**
	 * The feature id for the '<em><b>New Vessel Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW__NEW_VESSEL_NAME = 3;

	/**
	 * The feature id for the '<em><b>Lhs Wiring Link</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW__LHS_WIRING_LINK = 4;

	/**
	 * The feature id for the '<em><b>Rhs Wiring Link</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW__RHS_WIRING_LINK = 5;

	/**
	 * The feature id for the '<em><b>Load Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW__LOAD_SLOT = 6;

	/**
	 * The feature id for the '<em><b>Discharge Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW__DISCHARGE_SLOT = 7;

	/**
	 * The feature id for the '<em><b>Original Load Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW__ORIGINAL_LOAD_ALLOCATION = 8;

	/**
	 * The feature id for the '<em><b>New Load Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW__NEW_LOAD_ALLOCATION = 9;

	/**
	 * The feature id for the '<em><b>Original Discharge Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW__ORIGINAL_DISCHARGE_ALLOCATION = 10;

	/**
	 * The feature id for the '<em><b>New Discharge Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW__NEW_DISCHARGE_ALLOCATION = 11;

	/**
	 * The feature id for the '<em><b>Wiring Change</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW__WIRING_CHANGE = 12;

	/**
	 * The feature id for the '<em><b>Vessel Change</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW__VESSEL_CHANGE = 13;

	/**
	 * The feature id for the '<em><b>Original Group Profit And Loss</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW__ORIGINAL_GROUP_PROFIT_AND_LOSS = 14;

	/**
	 * The feature id for the '<em><b>New Group Profit And Loss</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW__NEW_GROUP_PROFIT_AND_LOSS = 15;

	/**
	 * The feature id for the '<em><b>Original Event Grouping</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW__ORIGINAL_EVENT_GROUPING = 16;

	/**
	 * The feature id for the '<em><b>New Event Grouping</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW__NEW_EVENT_GROUPING = 17;

	/**
	 * The feature id for the '<em><b>Original Vessel Short Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW__ORIGINAL_VESSEL_SHORT_NAME = 18;

	/**
	 * The feature id for the '<em><b>New Vessel Short Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW__NEW_VESSEL_SHORT_NAME = 19;

	/**
	 * The feature id for the '<em><b>Original Open Load Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW__ORIGINAL_OPEN_LOAD_ALLOCATION = 20;

	/**
	 * The feature id for the '<em><b>New Open Load Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW__NEW_OPEN_LOAD_ALLOCATION = 21;

	/**
	 * The feature id for the '<em><b>Original Open Discharge Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW__ORIGINAL_OPEN_DISCHARGE_ALLOCATION = 22;

	/**
	 * The feature id for the '<em><b>New Open Discharge Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW__NEW_OPEN_DISCHARGE_ALLOCATION = 23;

	/**
	 * The number of structural features of the '<em>Change Set Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_FEATURE_COUNT = 24;

	/**
	 * The number of operations of the '<em>Change Set Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_OPERATION_COUNT = 0;


	/**
	 * The meta object id for the '<em>Scenario Result</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.scenario.service.ui.ScenarioResult
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getScenarioResult()
	 * @generated
	 */
	int SCENARIO_RESULT = 5;


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
	 * Returns the meta object for class '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow <em>Change Set Row</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Change Set Row</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow
	 * @generated
	 */
	EClass getChangeSetRow();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getLhsName <em>Lhs Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lhs Name</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getLhsName()
	 * @see #getChangeSetRow()
	 * @generated
	 */
	EAttribute getChangeSetRow_LhsName();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getRhsName <em>Rhs Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Rhs Name</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getRhsName()
	 * @see #getChangeSetRow()
	 * @generated
	 */
	EAttribute getChangeSetRow_RhsName();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalVesselName <em>Original Vessel Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Original Vessel Name</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalVesselName()
	 * @see #getChangeSetRow()
	 * @generated
	 */
	EAttribute getChangeSetRow_OriginalVesselName();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewVesselName <em>New Vessel Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>New Vessel Name</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewVesselName()
	 * @see #getChangeSetRow()
	 * @generated
	 */
	EAttribute getChangeSetRow_NewVesselName();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getLhsWiringLink <em>Lhs Wiring Link</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Lhs Wiring Link</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getLhsWiringLink()
	 * @see #getChangeSetRow()
	 * @generated
	 */
	EReference getChangeSetRow_LhsWiringLink();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getRhsWiringLink <em>Rhs Wiring Link</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Rhs Wiring Link</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getRhsWiringLink()
	 * @see #getChangeSetRow()
	 * @generated
	 */
	EReference getChangeSetRow_RhsWiringLink();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getLoadSlot <em>Load Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Load Slot</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getLoadSlot()
	 * @see #getChangeSetRow()
	 * @generated
	 */
	EReference getChangeSetRow_LoadSlot();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getDischargeSlot <em>Discharge Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Discharge Slot</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getDischargeSlot()
	 * @see #getChangeSetRow()
	 * @generated
	 */
	EReference getChangeSetRow_DischargeSlot();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalLoadAllocation <em>Original Load Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Original Load Allocation</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalLoadAllocation()
	 * @see #getChangeSetRow()
	 * @generated
	 */
	EReference getChangeSetRow_OriginalLoadAllocation();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewLoadAllocation <em>New Load Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>New Load Allocation</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewLoadAllocation()
	 * @see #getChangeSetRow()
	 * @generated
	 */
	EReference getChangeSetRow_NewLoadAllocation();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalDischargeAllocation <em>Original Discharge Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Original Discharge Allocation</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalDischargeAllocation()
	 * @see #getChangeSetRow()
	 * @generated
	 */
	EReference getChangeSetRow_OriginalDischargeAllocation();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewDischargeAllocation <em>New Discharge Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>New Discharge Allocation</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewDischargeAllocation()
	 * @see #getChangeSetRow()
	 * @generated
	 */
	EReference getChangeSetRow_NewDischargeAllocation();

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
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalGroupProfitAndLoss <em>Original Group Profit And Loss</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Original Group Profit And Loss</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalGroupProfitAndLoss()
	 * @see #getChangeSetRow()
	 * @generated
	 */
	EReference getChangeSetRow_OriginalGroupProfitAndLoss();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewGroupProfitAndLoss <em>New Group Profit And Loss</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>New Group Profit And Loss</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewGroupProfitAndLoss()
	 * @see #getChangeSetRow()
	 * @generated
	 */
	EReference getChangeSetRow_NewGroupProfitAndLoss();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalEventGrouping <em>Original Event Grouping</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Original Event Grouping</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalEventGrouping()
	 * @see #getChangeSetRow()
	 * @generated
	 */
	EReference getChangeSetRow_OriginalEventGrouping();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewEventGrouping <em>New Event Grouping</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>New Event Grouping</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewEventGrouping()
	 * @see #getChangeSetRow()
	 * @generated
	 */
	EReference getChangeSetRow_NewEventGrouping();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalVesselShortName <em>Original Vessel Short Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Original Vessel Short Name</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalVesselShortName()
	 * @see #getChangeSetRow()
	 * @generated
	 */
	EAttribute getChangeSetRow_OriginalVesselShortName();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewVesselShortName <em>New Vessel Short Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>New Vessel Short Name</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewVesselShortName()
	 * @see #getChangeSetRow()
	 * @generated
	 */
	EAttribute getChangeSetRow_NewVesselShortName();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalOpenLoadAllocation <em>Original Open Load Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Original Open Load Allocation</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalOpenLoadAllocation()
	 * @see #getChangeSetRow()
	 * @generated
	 */
	EReference getChangeSetRow_OriginalOpenLoadAllocation();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewOpenLoadAllocation <em>New Open Load Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>New Open Load Allocation</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewOpenLoadAllocation()
	 * @see #getChangeSetRow()
	 * @generated
	 */
	EReference getChangeSetRow_NewOpenLoadAllocation();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalOpenDischargeAllocation <em>Original Open Discharge Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Original Open Discharge Allocation</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalOpenDischargeAllocation()
	 * @see #getChangeSetRow()
	 * @generated
	 */
	EReference getChangeSetRow_OriginalOpenDischargeAllocation();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewOpenDischargeAllocation <em>New Open Discharge Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>New Open Discharge Allocation</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewOpenDischargeAllocation()
	 * @see #getChangeSetRow()
	 * @generated
	 */
	EReference getChangeSetRow_NewOpenDischargeAllocation();

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
		 * The meta object literal for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl <em>Change Set Row</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getChangeSetRow()
		 * @generated
		 */
		EClass CHANGE_SET_ROW = eINSTANCE.getChangeSetRow();

		/**
		 * The meta object literal for the '<em><b>Lhs Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_ROW__LHS_NAME = eINSTANCE.getChangeSetRow_LhsName();

		/**
		 * The meta object literal for the '<em><b>Rhs Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_ROW__RHS_NAME = eINSTANCE.getChangeSetRow_RhsName();

		/**
		 * The meta object literal for the '<em><b>Original Vessel Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_ROW__ORIGINAL_VESSEL_NAME = eINSTANCE.getChangeSetRow_OriginalVesselName();

		/**
		 * The meta object literal for the '<em><b>New Vessel Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_ROW__NEW_VESSEL_NAME = eINSTANCE.getChangeSetRow_NewVesselName();

		/**
		 * The meta object literal for the '<em><b>Lhs Wiring Link</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW__LHS_WIRING_LINK = eINSTANCE.getChangeSetRow_LhsWiringLink();

		/**
		 * The meta object literal for the '<em><b>Rhs Wiring Link</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW__RHS_WIRING_LINK = eINSTANCE.getChangeSetRow_RhsWiringLink();

		/**
		 * The meta object literal for the '<em><b>Load Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW__LOAD_SLOT = eINSTANCE.getChangeSetRow_LoadSlot();

		/**
		 * The meta object literal for the '<em><b>Discharge Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW__DISCHARGE_SLOT = eINSTANCE.getChangeSetRow_DischargeSlot();

		/**
		 * The meta object literal for the '<em><b>Original Load Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW__ORIGINAL_LOAD_ALLOCATION = eINSTANCE.getChangeSetRow_OriginalLoadAllocation();

		/**
		 * The meta object literal for the '<em><b>New Load Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW__NEW_LOAD_ALLOCATION = eINSTANCE.getChangeSetRow_NewLoadAllocation();

		/**
		 * The meta object literal for the '<em><b>Original Discharge Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW__ORIGINAL_DISCHARGE_ALLOCATION = eINSTANCE.getChangeSetRow_OriginalDischargeAllocation();

		/**
		 * The meta object literal for the '<em><b>New Discharge Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW__NEW_DISCHARGE_ALLOCATION = eINSTANCE.getChangeSetRow_NewDischargeAllocation();

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
		 * The meta object literal for the '<em><b>Original Group Profit And Loss</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW__ORIGINAL_GROUP_PROFIT_AND_LOSS = eINSTANCE.getChangeSetRow_OriginalGroupProfitAndLoss();

		/**
		 * The meta object literal for the '<em><b>New Group Profit And Loss</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW__NEW_GROUP_PROFIT_AND_LOSS = eINSTANCE.getChangeSetRow_NewGroupProfitAndLoss();

		/**
		 * The meta object literal for the '<em><b>Original Event Grouping</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW__ORIGINAL_EVENT_GROUPING = eINSTANCE.getChangeSetRow_OriginalEventGrouping();

		/**
		 * The meta object literal for the '<em><b>New Event Grouping</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW__NEW_EVENT_GROUPING = eINSTANCE.getChangeSetRow_NewEventGrouping();

		/**
		 * The meta object literal for the '<em><b>Original Vessel Short Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_ROW__ORIGINAL_VESSEL_SHORT_NAME = eINSTANCE.getChangeSetRow_OriginalVesselShortName();

		/**
		 * The meta object literal for the '<em><b>New Vessel Short Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_ROW__NEW_VESSEL_SHORT_NAME = eINSTANCE.getChangeSetRow_NewVesselShortName();

		/**
		 * The meta object literal for the '<em><b>Original Open Load Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW__ORIGINAL_OPEN_LOAD_ALLOCATION = eINSTANCE.getChangeSetRow_OriginalOpenLoadAllocation();

		/**
		 * The meta object literal for the '<em><b>New Open Load Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW__NEW_OPEN_LOAD_ALLOCATION = eINSTANCE.getChangeSetRow_NewOpenLoadAllocation();

		/**
		 * The meta object literal for the '<em><b>Original Open Discharge Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW__ORIGINAL_OPEN_DISCHARGE_ALLOCATION = eINSTANCE.getChangeSetRow_OriginalOpenDischargeAllocation();

		/**
		 * The meta object literal for the '<em><b>New Open Discharge Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET_ROW__NEW_OPEN_DISCHARGE_ALLOCATION = eINSTANCE.getChangeSetRow_NewOpenDischargeAllocation();

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
