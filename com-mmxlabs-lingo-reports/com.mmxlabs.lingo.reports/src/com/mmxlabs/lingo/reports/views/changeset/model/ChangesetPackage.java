/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
	 * The feature id for the '<em><b>Changes To Base</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__CHANGES_TO_BASE = 0;

	/**
	 * The feature id for the '<em><b>Changes To Previous</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__CHANGES_TO_PREVIOUS = 1;

	/**
	 * The feature id for the '<em><b>Metrics To Base</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__METRICS_TO_BASE = 2;

	/**
	 * The feature id for the '<em><b>Metrics To Previous</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__METRICS_TO_PREVIOUS = 3;

	/**
	 * The feature id for the '<em><b>Base Scenario Ref</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__BASE_SCENARIO_REF = 4;

	/**
	 * The feature id for the '<em><b>Prev Scenario Ref</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__PREV_SCENARIO_REF = 5;

	/**
	 * The feature id for the '<em><b>Current Scenario Ref</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__CURRENT_SCENARIO_REF = 6;

	/**
	 * The feature id for the '<em><b>Base Scenario</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__BASE_SCENARIO = 7;

	/**
	 * The feature id for the '<em><b>Prev Scenario</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__PREV_SCENARIO = 8;

	/**
	 * The feature id for the '<em><b>Current Scenario</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__CURRENT_SCENARIO = 9;

	/**
	 * The feature id for the '<em><b>Change Set Rows To Base</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__CHANGE_SET_ROWS_TO_BASE = 10;

	/**
	 * The feature id for the '<em><b>Change Set Rows To Previous</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET__CHANGE_SET_ROWS_TO_PREVIOUS = 11;

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
	 * The feature id for the '<em><b>Pnl Delta</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METRICS__PNL_DELTA = 0;

	/**
	 * The feature id for the '<em><b>Lateness Delta</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METRICS__LATENESS_DELTA = 1;

	/**
	 * The feature id for the '<em><b>Capacity Delta</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METRICS__CAPACITY_DELTA = 2;

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
	 * The meta object id for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeImpl <em>Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeImpl
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getChange()
	 * @generated
	 */
	int CHANGE = 3;

	/**
	 * The feature id for the '<em><b>Original Group Profit And Loss</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE__ORIGINAL_GROUP_PROFIT_AND_LOSS = 0;

	/**
	 * The feature id for the '<em><b>New Group Profit And Loss</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE__NEW_GROUP_PROFIT_AND_LOSS = 1;

	/**
	 * The feature id for the '<em><b>Original Event Grouping</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE__ORIGINAL_EVENT_GROUPING = 2;

	/**
	 * The feature id for the '<em><b>New Event Grouping</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE__NEW_EVENT_GROUPING = 3;

	/**
	 * The number of structural features of the '<em>Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.EventVesselChangeImpl <em>Event Vessel Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.EventVesselChangeImpl
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getEventVesselChange()
	 * @generated
	 */
	int EVENT_VESSEL_CHANGE = 4;

	/**
	 * The feature id for the '<em><b>Original Group Profit And Loss</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_VESSEL_CHANGE__ORIGINAL_GROUP_PROFIT_AND_LOSS = CHANGE__ORIGINAL_GROUP_PROFIT_AND_LOSS;

	/**
	 * The feature id for the '<em><b>New Group Profit And Loss</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_VESSEL_CHANGE__NEW_GROUP_PROFIT_AND_LOSS = CHANGE__NEW_GROUP_PROFIT_AND_LOSS;

	/**
	 * The feature id for the '<em><b>Original Event Grouping</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_VESSEL_CHANGE__ORIGINAL_EVENT_GROUPING = CHANGE__ORIGINAL_EVENT_GROUPING;

	/**
	 * The feature id for the '<em><b>New Event Grouping</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_VESSEL_CHANGE__NEW_EVENT_GROUPING = CHANGE__NEW_EVENT_GROUPING;

	/**
	 * The feature id for the '<em><b>Original Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_VESSEL_CHANGE__ORIGINAL_EVENT = CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>New Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_VESSEL_CHANGE__NEW_EVENT = CHANGE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Original Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_VESSEL_CHANGE__ORIGINAL_VESSEL = CHANGE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>New Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_VESSEL_CHANGE__NEW_VESSEL = CHANGE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Event Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_VESSEL_CHANGE__EVENT_NAME = CHANGE_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Event Vessel Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_VESSEL_CHANGE_FEATURE_COUNT = CHANGE_FEATURE_COUNT + 5;

	/**
	 * The number of operations of the '<em>Event Vessel Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_VESSEL_CHANGE_OPERATION_COUNT = CHANGE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.WiringChangeImpl <em>Wiring Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.WiringChangeImpl
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getWiringChange()
	 * @generated
	 */
	int WIRING_CHANGE = 5;

	/**
	 * The feature id for the '<em><b>Original Group Profit And Loss</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING_CHANGE__ORIGINAL_GROUP_PROFIT_AND_LOSS = CHANGE__ORIGINAL_GROUP_PROFIT_AND_LOSS;

	/**
	 * The feature id for the '<em><b>New Group Profit And Loss</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING_CHANGE__NEW_GROUP_PROFIT_AND_LOSS = CHANGE__NEW_GROUP_PROFIT_AND_LOSS;

	/**
	 * The feature id for the '<em><b>Original Event Grouping</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING_CHANGE__ORIGINAL_EVENT_GROUPING = CHANGE__ORIGINAL_EVENT_GROUPING;

	/**
	 * The feature id for the '<em><b>New Event Grouping</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING_CHANGE__NEW_EVENT_GROUPING = CHANGE__NEW_EVENT_GROUPING;

	/**
	 * The feature id for the '<em><b>Original Load Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING_CHANGE__ORIGINAL_LOAD_SLOT = CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>New Load Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING_CHANGE__NEW_LOAD_SLOT = CHANGE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Original Discharge Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING_CHANGE__ORIGINAL_DISCHARGE_SLOT = CHANGE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>New Discharge Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING_CHANGE__NEW_DISCHARGE_SLOT = CHANGE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Original Load Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING_CHANGE__ORIGINAL_LOAD_ALLOCATION = CHANGE_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>New Load Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING_CHANGE__NEW_LOAD_ALLOCATION = CHANGE_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Original Discharge Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING_CHANGE__ORIGINAL_DISCHARGE_ALLOCATION = CHANGE_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>New Discharge Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING_CHANGE__NEW_DISCHARGE_ALLOCATION = CHANGE_FEATURE_COUNT + 7;

	/**
	 * The number of structural features of the '<em>Wiring Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING_CHANGE_FEATURE_COUNT = CHANGE_FEATURE_COUNT + 8;

	/**
	 * The number of operations of the '<em>Wiring Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING_CHANGE_OPERATION_COUNT = CHANGE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.VesselChangeImpl <em>Vessel Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.VesselChangeImpl
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getVesselChange()
	 * @generated
	 */
	int VESSEL_CHANGE = 6;

	/**
	 * The feature id for the '<em><b>Original Group Profit And Loss</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CHANGE__ORIGINAL_GROUP_PROFIT_AND_LOSS = CHANGE__ORIGINAL_GROUP_PROFIT_AND_LOSS;

	/**
	 * The feature id for the '<em><b>New Group Profit And Loss</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CHANGE__NEW_GROUP_PROFIT_AND_LOSS = CHANGE__NEW_GROUP_PROFIT_AND_LOSS;

	/**
	 * The feature id for the '<em><b>Original Event Grouping</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CHANGE__ORIGINAL_EVENT_GROUPING = CHANGE__ORIGINAL_EVENT_GROUPING;

	/**
	 * The feature id for the '<em><b>New Event Grouping</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CHANGE__NEW_EVENT_GROUPING = CHANGE__NEW_EVENT_GROUPING;

	/**
	 * The feature id for the '<em><b>Original Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CHANGE__ORIGINAL_VESSEL = CHANGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>New Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CHANGE__NEW_VESSEL = CHANGE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Original Load Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CHANGE__ORIGINAL_LOAD_SLOT = CHANGE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>New Load Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CHANGE__NEW_LOAD_SLOT = CHANGE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Original Discharge Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CHANGE__ORIGINAL_DISCHARGE_SLOT = CHANGE_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>New Discharge Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CHANGE__NEW_DISCHARGE_SLOT = CHANGE_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Original Load Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CHANGE__ORIGINAL_LOAD_ALLOCATION = CHANGE_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>New Load Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CHANGE__NEW_LOAD_ALLOCATION = CHANGE_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Original Discharge Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CHANGE__ORIGINAL_DISCHARGE_ALLOCATION = CHANGE_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>New Discharge Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CHANGE__NEW_DISCHARGE_ALLOCATION = CHANGE_FEATURE_COUNT + 9;

	/**
	 * The number of structural features of the '<em>Vessel Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CHANGE_FEATURE_COUNT = CHANGE_FEATURE_COUNT + 10;

	/**
	 * The number of operations of the '<em>Vessel Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CHANGE_OPERATION_COUNT = CHANGE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl <em>Change Set Row</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getChangeSetRow()
	 * @generated
	 */
	int CHANGE_SET_ROW = 7;

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
	 * The feature id for the '<em><b>Lhs Vessel Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW__LHS_VESSEL_NAME = 2;

	/**
	 * The feature id for the '<em><b>Rhs Vessel Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW__RHS_VESSEL_NAME = 3;

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
	 * The number of structural features of the '<em>Change Set Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_FEATURE_COUNT = 18;

	/**
	 * The number of operations of the '<em>Change Set Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_SET_ROW_OPERATION_COUNT = 0;


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
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getChangesToBase <em>Changes To Base</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Changes To Base</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getChangesToBase()
	 * @see #getChangeSet()
	 * @generated
	 */
	EReference getChangeSet_ChangesToBase();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getChangesToPrevious <em>Changes To Previous</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Changes To Previous</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getChangesToPrevious()
	 * @see #getChangeSet()
	 * @generated
	 */
	EReference getChangeSet_ChangesToPrevious();

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
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getBaseScenario <em>Base Scenario</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Base Scenario</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getBaseScenario()
	 * @see #getChangeSet()
	 * @generated
	 */
	EReference getChangeSet_BaseScenario();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getPrevScenario <em>Prev Scenario</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Prev Scenario</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getPrevScenario()
	 * @see #getChangeSet()
	 * @generated
	 */
	EReference getChangeSet_PrevScenario();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getCurrentScenario <em>Current Scenario</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Current Scenario</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet#getCurrentScenario()
	 * @see #getChangeSet()
	 * @generated
	 */
	EReference getChangeSet_CurrentScenario();

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
	 * Returns the meta object for class '{@link com.mmxlabs.lingo.reports.views.changeset.model.Metrics <em>Metrics</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Metrics</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.Metrics
	 * @generated
	 */
	EClass getMetrics();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.Metrics#getPnlDelta <em>Pnl Delta</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Pnl Delta</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.Metrics#getPnlDelta()
	 * @see #getMetrics()
	 * @generated
	 */
	EAttribute getMetrics_PnlDelta();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.Metrics#getLatenessDelta <em>Lateness Delta</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lateness Delta</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.Metrics#getLatenessDelta()
	 * @see #getMetrics()
	 * @generated
	 */
	EAttribute getMetrics_LatenessDelta();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.Metrics#getCapacityDelta <em>Capacity Delta</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Capacity Delta</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.Metrics#getCapacityDelta()
	 * @see #getMetrics()
	 * @generated
	 */
	EAttribute getMetrics_CapacityDelta();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.lingo.reports.views.changeset.model.Change <em>Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Change</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.Change
	 * @generated
	 */
	EClass getChange();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.Change#getOriginalGroupProfitAndLoss <em>Original Group Profit And Loss</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Original Group Profit And Loss</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.Change#getOriginalGroupProfitAndLoss()
	 * @see #getChange()
	 * @generated
	 */
	EReference getChange_OriginalGroupProfitAndLoss();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.Change#getNewGroupProfitAndLoss <em>New Group Profit And Loss</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>New Group Profit And Loss</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.Change#getNewGroupProfitAndLoss()
	 * @see #getChange()
	 * @generated
	 */
	EReference getChange_NewGroupProfitAndLoss();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.Change#getOriginalEventGrouping <em>Original Event Grouping</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Original Event Grouping</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.Change#getOriginalEventGrouping()
	 * @see #getChange()
	 * @generated
	 */
	EReference getChange_OriginalEventGrouping();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.Change#getNewEventGrouping <em>New Event Grouping</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>New Event Grouping</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.Change#getNewEventGrouping()
	 * @see #getChange()
	 * @generated
	 */
	EReference getChange_NewEventGrouping();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange <em>Event Vessel Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Event Vessel Change</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange
	 * @generated
	 */
	EClass getEventVesselChange();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange#getOriginalEvent <em>Original Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Original Event</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange#getOriginalEvent()
	 * @see #getEventVesselChange()
	 * @generated
	 */
	EReference getEventVesselChange_OriginalEvent();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange#getNewEvent <em>New Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>New Event</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange#getNewEvent()
	 * @see #getEventVesselChange()
	 * @generated
	 */
	EReference getEventVesselChange_NewEvent();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange#getOriginalVessel <em>Original Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Original Vessel</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange#getOriginalVessel()
	 * @see #getEventVesselChange()
	 * @generated
	 */
	EReference getEventVesselChange_OriginalVessel();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange#getNewVessel <em>New Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>New Vessel</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange#getNewVessel()
	 * @see #getEventVesselChange()
	 * @generated
	 */
	EReference getEventVesselChange_NewVessel();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange#getEventName <em>Event Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Event Name</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange#getEventName()
	 * @see #getEventVesselChange()
	 * @generated
	 */
	EAttribute getEventVesselChange_EventName();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.lingo.reports.views.changeset.model.WiringChange <em>Wiring Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Wiring Change</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.WiringChange
	 * @generated
	 */
	EClass getWiringChange();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getOriginalLoadSlot <em>Original Load Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Original Load Slot</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getOriginalLoadSlot()
	 * @see #getWiringChange()
	 * @generated
	 */
	EReference getWiringChange_OriginalLoadSlot();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getNewLoadSlot <em>New Load Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>New Load Slot</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getNewLoadSlot()
	 * @see #getWiringChange()
	 * @generated
	 */
	EReference getWiringChange_NewLoadSlot();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getOriginalDischargeSlot <em>Original Discharge Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Original Discharge Slot</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getOriginalDischargeSlot()
	 * @see #getWiringChange()
	 * @generated
	 */
	EReference getWiringChange_OriginalDischargeSlot();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getNewDischargeSlot <em>New Discharge Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>New Discharge Slot</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getNewDischargeSlot()
	 * @see #getWiringChange()
	 * @generated
	 */
	EReference getWiringChange_NewDischargeSlot();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getOriginalLoadAllocation <em>Original Load Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Original Load Allocation</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getOriginalLoadAllocation()
	 * @see #getWiringChange()
	 * @generated
	 */
	EReference getWiringChange_OriginalLoadAllocation();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getNewLoadAllocation <em>New Load Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>New Load Allocation</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getNewLoadAllocation()
	 * @see #getWiringChange()
	 * @generated
	 */
	EReference getWiringChange_NewLoadAllocation();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getOriginalDischargeAllocation <em>Original Discharge Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Original Discharge Allocation</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getOriginalDischargeAllocation()
	 * @see #getWiringChange()
	 * @generated
	 */
	EReference getWiringChange_OriginalDischargeAllocation();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getNewDischargeAllocation <em>New Discharge Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>New Discharge Allocation</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getNewDischargeAllocation()
	 * @see #getWiringChange()
	 * @generated
	 */
	EReference getWiringChange_NewDischargeAllocation();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.lingo.reports.views.changeset.model.VesselChange <em>Vessel Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vessel Change</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.VesselChange
	 * @generated
	 */
	EClass getVesselChange();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getNewLoadSlot <em>New Load Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>New Load Slot</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getNewLoadSlot()
	 * @see #getVesselChange()
	 * @generated
	 */
	EReference getVesselChange_NewLoadSlot();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getOriginalDischargeSlot <em>Original Discharge Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Original Discharge Slot</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getOriginalDischargeSlot()
	 * @see #getVesselChange()
	 * @generated
	 */
	EReference getVesselChange_OriginalDischargeSlot();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getNewDischargeSlot <em>New Discharge Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>New Discharge Slot</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getNewDischargeSlot()
	 * @see #getVesselChange()
	 * @generated
	 */
	EReference getVesselChange_NewDischargeSlot();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getOriginalVessel <em>Original Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Original Vessel</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getOriginalVessel()
	 * @see #getVesselChange()
	 * @generated
	 */
	EReference getVesselChange_OriginalVessel();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getNewVessel <em>New Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>New Vessel</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getNewVessel()
	 * @see #getVesselChange()
	 * @generated
	 */
	EReference getVesselChange_NewVessel();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getOriginalLoadSlot <em>Original Load Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Original Load Slot</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getOriginalLoadSlot()
	 * @see #getVesselChange()
	 * @generated
	 */
	EReference getVesselChange_OriginalLoadSlot();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getOriginalLoadAllocation <em>Original Load Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Original Load Allocation</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getOriginalLoadAllocation()
	 * @see #getVesselChange()
	 * @generated
	 */
	EReference getVesselChange_OriginalLoadAllocation();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getNewLoadAllocation <em>New Load Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>New Load Allocation</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getNewLoadAllocation()
	 * @see #getVesselChange()
	 * @generated
	 */
	EReference getVesselChange_NewLoadAllocation();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getOriginalDischargeAllocation <em>Original Discharge Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Original Discharge Allocation</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getOriginalDischargeAllocation()
	 * @see #getVesselChange()
	 * @generated
	 */
	EReference getVesselChange_OriginalDischargeAllocation();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getNewDischargeAllocation <em>New Discharge Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>New Discharge Allocation</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getNewDischargeAllocation()
	 * @see #getVesselChange()
	 * @generated
	 */
	EReference getVesselChange_NewDischargeAllocation();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getLhsVesselName <em>Lhs Vessel Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lhs Vessel Name</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getLhsVesselName()
	 * @see #getChangeSetRow()
	 * @generated
	 */
	EAttribute getChangeSetRow_LhsVesselName();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getRhsVesselName <em>Rhs Vessel Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Rhs Vessel Name</em>'.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getRhsVesselName()
	 * @see #getChangeSetRow()
	 * @generated
	 */
	EAttribute getChangeSetRow_RhsVesselName();

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
		 * The meta object literal for the '<em><b>Changes To Base</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET__CHANGES_TO_BASE = eINSTANCE.getChangeSet_ChangesToBase();

		/**
		 * The meta object literal for the '<em><b>Changes To Previous</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET__CHANGES_TO_PREVIOUS = eINSTANCE.getChangeSet_ChangesToPrevious();

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
		 * The meta object literal for the '<em><b>Base Scenario</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET__BASE_SCENARIO = eINSTANCE.getChangeSet_BaseScenario();

		/**
		 * The meta object literal for the '<em><b>Prev Scenario</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET__PREV_SCENARIO = eINSTANCE.getChangeSet_PrevScenario();

		/**
		 * The meta object literal for the '<em><b>Current Scenario</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_SET__CURRENT_SCENARIO = eINSTANCE.getChangeSet_CurrentScenario();

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
		 * The meta object literal for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.MetricsImpl <em>Metrics</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.MetricsImpl
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getMetrics()
		 * @generated
		 */
		EClass METRICS = eINSTANCE.getMetrics();

		/**
		 * The meta object literal for the '<em><b>Pnl Delta</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute METRICS__PNL_DELTA = eINSTANCE.getMetrics_PnlDelta();

		/**
		 * The meta object literal for the '<em><b>Lateness Delta</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute METRICS__LATENESS_DELTA = eINSTANCE.getMetrics_LatenessDelta();

		/**
		 * The meta object literal for the '<em><b>Capacity Delta</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute METRICS__CAPACITY_DELTA = eINSTANCE.getMetrics_CapacityDelta();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeImpl <em>Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeImpl
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getChange()
		 * @generated
		 */
		EClass CHANGE = eINSTANCE.getChange();

		/**
		 * The meta object literal for the '<em><b>Original Group Profit And Loss</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE__ORIGINAL_GROUP_PROFIT_AND_LOSS = eINSTANCE.getChange_OriginalGroupProfitAndLoss();

		/**
		 * The meta object literal for the '<em><b>New Group Profit And Loss</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE__NEW_GROUP_PROFIT_AND_LOSS = eINSTANCE.getChange_NewGroupProfitAndLoss();

		/**
		 * The meta object literal for the '<em><b>Original Event Grouping</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE__ORIGINAL_EVENT_GROUPING = eINSTANCE.getChange_OriginalEventGrouping();

		/**
		 * The meta object literal for the '<em><b>New Event Grouping</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE__NEW_EVENT_GROUPING = eINSTANCE.getChange_NewEventGrouping();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.EventVesselChangeImpl <em>Event Vessel Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.EventVesselChangeImpl
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getEventVesselChange()
		 * @generated
		 */
		EClass EVENT_VESSEL_CHANGE = eINSTANCE.getEventVesselChange();

		/**
		 * The meta object literal for the '<em><b>Original Event</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EVENT_VESSEL_CHANGE__ORIGINAL_EVENT = eINSTANCE.getEventVesselChange_OriginalEvent();

		/**
		 * The meta object literal for the '<em><b>New Event</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EVENT_VESSEL_CHANGE__NEW_EVENT = eINSTANCE.getEventVesselChange_NewEvent();

		/**
		 * The meta object literal for the '<em><b>Original Vessel</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EVENT_VESSEL_CHANGE__ORIGINAL_VESSEL = eINSTANCE.getEventVesselChange_OriginalVessel();

		/**
		 * The meta object literal for the '<em><b>New Vessel</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EVENT_VESSEL_CHANGE__NEW_VESSEL = eINSTANCE.getEventVesselChange_NewVessel();

		/**
		 * The meta object literal for the '<em><b>Event Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EVENT_VESSEL_CHANGE__EVENT_NAME = eINSTANCE.getEventVesselChange_EventName();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.WiringChangeImpl <em>Wiring Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.WiringChangeImpl
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getWiringChange()
		 * @generated
		 */
		EClass WIRING_CHANGE = eINSTANCE.getWiringChange();

		/**
		 * The meta object literal for the '<em><b>Original Load Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference WIRING_CHANGE__ORIGINAL_LOAD_SLOT = eINSTANCE.getWiringChange_OriginalLoadSlot();

		/**
		 * The meta object literal for the '<em><b>New Load Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference WIRING_CHANGE__NEW_LOAD_SLOT = eINSTANCE.getWiringChange_NewLoadSlot();

		/**
		 * The meta object literal for the '<em><b>Original Discharge Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference WIRING_CHANGE__ORIGINAL_DISCHARGE_SLOT = eINSTANCE.getWiringChange_OriginalDischargeSlot();

		/**
		 * The meta object literal for the '<em><b>New Discharge Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference WIRING_CHANGE__NEW_DISCHARGE_SLOT = eINSTANCE.getWiringChange_NewDischargeSlot();

		/**
		 * The meta object literal for the '<em><b>Original Load Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference WIRING_CHANGE__ORIGINAL_LOAD_ALLOCATION = eINSTANCE.getWiringChange_OriginalLoadAllocation();

		/**
		 * The meta object literal for the '<em><b>New Load Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference WIRING_CHANGE__NEW_LOAD_ALLOCATION = eINSTANCE.getWiringChange_NewLoadAllocation();

		/**
		 * The meta object literal for the '<em><b>Original Discharge Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference WIRING_CHANGE__ORIGINAL_DISCHARGE_ALLOCATION = eINSTANCE.getWiringChange_OriginalDischargeAllocation();

		/**
		 * The meta object literal for the '<em><b>New Discharge Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference WIRING_CHANGE__NEW_DISCHARGE_ALLOCATION = eINSTANCE.getWiringChange_NewDischargeAllocation();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.VesselChangeImpl <em>Vessel Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.VesselChangeImpl
		 * @see com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangesetPackageImpl#getVesselChange()
		 * @generated
		 */
		EClass VESSEL_CHANGE = eINSTANCE.getVesselChange();

		/**
		 * The meta object literal for the '<em><b>New Load Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_CHANGE__NEW_LOAD_SLOT = eINSTANCE.getVesselChange_NewLoadSlot();

		/**
		 * The meta object literal for the '<em><b>Original Discharge Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_CHANGE__ORIGINAL_DISCHARGE_SLOT = eINSTANCE.getVesselChange_OriginalDischargeSlot();

		/**
		 * The meta object literal for the '<em><b>New Discharge Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_CHANGE__NEW_DISCHARGE_SLOT = eINSTANCE.getVesselChange_NewDischargeSlot();

		/**
		 * The meta object literal for the '<em><b>Original Vessel</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_CHANGE__ORIGINAL_VESSEL = eINSTANCE.getVesselChange_OriginalVessel();

		/**
		 * The meta object literal for the '<em><b>New Vessel</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_CHANGE__NEW_VESSEL = eINSTANCE.getVesselChange_NewVessel();

		/**
		 * The meta object literal for the '<em><b>Original Load Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_CHANGE__ORIGINAL_LOAD_SLOT = eINSTANCE.getVesselChange_OriginalLoadSlot();

		/**
		 * The meta object literal for the '<em><b>Original Load Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_CHANGE__ORIGINAL_LOAD_ALLOCATION = eINSTANCE.getVesselChange_OriginalLoadAllocation();

		/**
		 * The meta object literal for the '<em><b>New Load Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_CHANGE__NEW_LOAD_ALLOCATION = eINSTANCE.getVesselChange_NewLoadAllocation();

		/**
		 * The meta object literal for the '<em><b>Original Discharge Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_CHANGE__ORIGINAL_DISCHARGE_ALLOCATION = eINSTANCE.getVesselChange_OriginalDischargeAllocation();

		/**
		 * The meta object literal for the '<em><b>New Discharge Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_CHANGE__NEW_DISCHARGE_ALLOCATION = eINSTANCE.getVesselChange_NewDischargeAllocation();

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
		 * The meta object literal for the '<em><b>Lhs Vessel Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_ROW__LHS_VESSEL_NAME = eINSTANCE.getChangeSetRow_LhsVesselName();

		/**
		 * The meta object literal for the '<em><b>Rhs Vessel Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHANGE_SET_ROW__RHS_VESSEL_NAME = eINSTANCE.getChangeSetRow_RhsVesselName();

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

	}

} //ChangesetPackage
