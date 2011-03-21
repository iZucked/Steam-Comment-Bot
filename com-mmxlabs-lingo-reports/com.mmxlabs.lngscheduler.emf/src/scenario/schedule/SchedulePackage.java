/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */
 

package scenario.schedule;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import scenario.ScenarioPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see scenario.schedule.ScheduleFactory
 * @model kind="package"
 * @generated
 */
public interface SchedulePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "schedule";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://com.mmxlabs.lng.emf/schedule";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "com.mmxlabs.lng.emf.schedule";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	SchedulePackage eINSTANCE = scenario.schedule.impl.SchedulePackageImpl.init();

	/**
	 * The meta object id for the '{@link scenario.schedule.impl.ScheduleModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.schedule.impl.ScheduleModelImpl
	 * @see scenario.schedule.impl.SchedulePackageImpl#getScheduleModel()
	 * @generated
	 */
	int SCHEDULE_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Schedules</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_MODEL__SCHEDULES = 0;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_MODEL_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link scenario.schedule.impl.ScheduleImpl <em>Schedule</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.schedule.impl.ScheduleImpl
	 * @see scenario.schedule.impl.SchedulePackageImpl#getSchedule()
	 * @generated
	 */
	int SCHEDULE = 1;

	/**
	 * The feature id for the '<em><b>Sequences</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE__SEQUENCES = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE__NAME = 1;

	/**
	 * The feature id for the '<em><b>Cargo Allocations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE__CARGO_ALLOCATIONS = 2;

	/**
	 * The feature id for the '<em><b>Fleet</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE__FLEET = 3;

	/**
	 * The feature id for the '<em><b>Fitness</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE__FITNESS = 4;

	/**
	 * The feature id for the '<em><b>Revenue</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE__REVENUE = 5;

	/**
	 * The number of structural features of the '<em>Schedule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_FEATURE_COUNT = 6;

	/**
	 * The number of operations of the '<em>Schedule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link scenario.schedule.impl.SequenceImpl <em>Sequence</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.schedule.impl.SequenceImpl
	 * @see scenario.schedule.impl.SchedulePackageImpl#getSequence()
	 * @generated
	 */
	int SEQUENCE = 2;

	/**
	 * The feature id for the '<em><b>Events</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE__EVENTS = 0;

	/**
	 * The feature id for the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE__VESSEL = 1;

	/**
	 * The feature id for the '<em><b>Fitness</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE__FITNESS = 2;

	/**
	 * The number of structural features of the '<em>Sequence</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE_FEATURE_COUNT = 3;


	/**
	 * The number of operations of the '<em>Sequence</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link scenario.schedule.impl.CargoAllocationImpl <em>Cargo Allocation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.schedule.impl.CargoAllocationImpl
	 * @see scenario.schedule.impl.SchedulePackageImpl#getCargoAllocation()
	 * @generated
	 */
	int CARGO_ALLOCATION = 3;

	/**
	 * The feature id for the '<em><b>Load Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__LOAD_SLOT = 0;

	/**
	 * The feature id for the '<em><b>Discharge Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__DISCHARGE_SLOT = 1;

	/**
	 * The feature id for the '<em><b>Fuel Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__FUEL_VOLUME = 2;

	/**
	 * The feature id for the '<em><b>Discharge Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__DISCHARGE_VOLUME = 3;

	/**
	 * The feature id for the '<em><b>Load Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__LOAD_DATE = 4;

	/**
	 * The feature id for the '<em><b>Discharge Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__DISCHARGE_DATE = 5;

	/**
	 * The feature id for the '<em><b>Load Price M3</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__LOAD_PRICE_M3 = 6;

	/**
	 * The feature id for the '<em><b>Discharge Price M3</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__DISCHARGE_PRICE_M3 = 7;

	/**
	 * The feature id for the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__VESSEL = 8;

	/**
	 * The feature id for the '<em><b>Laden Leg</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__LADEN_LEG = 9;

	/**
	 * The feature id for the '<em><b>Ballast Leg</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__BALLAST_LEG = 10;

	/**
	 * The feature id for the '<em><b>Laden Idle</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__LADEN_IDLE = 11;

	/**
	 * The feature id for the '<em><b>Ballast Idle</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__BALLAST_IDLE = 12;

	/**
	 * The feature id for the '<em><b>Load Revenue</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__LOAD_REVENUE = 13;

	/**
	 * The feature id for the '<em><b>Shipping Revenue</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__SHIPPING_REVENUE = 14;

	/**
	 * The feature id for the '<em><b>Discharge Revenue</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__DISCHARGE_REVENUE = 15;

	/**
	 * The number of structural features of the '<em>Cargo Allocation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION_FEATURE_COUNT = 16;


	/**
	 * The operation id for the '<em>Get Total Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION___GET_TOTAL_COST = 0;

	/**
	 * The operation id for the '<em>Get Local Load Date</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION___GET_LOCAL_LOAD_DATE = 1;

	/**
	 * The operation id for the '<em>Get Local Discharge Date</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION___GET_LOCAL_DISCHARGE_DATE = 2;

	/**
	 * The operation id for the '<em>Get Load Volume</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION___GET_LOAD_VOLUME = 3;

	/**
	 * The number of operations of the '<em>Cargo Allocation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION_OPERATION_COUNT = 4;

	/**
	 * The meta object id for the '{@link scenario.schedule.impl.ScheduleFitnessImpl <em>Fitness</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.schedule.impl.ScheduleFitnessImpl
	 * @see scenario.schedule.impl.SchedulePackageImpl#getScheduleFitness()
	 * @generated
	 */
	int SCHEDULE_FITNESS = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_FITNESS__NAME = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_FITNESS__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Fitness</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_FITNESS_FEATURE_COUNT = 2;


	/**
	 * The number of operations of the '<em>Fitness</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_FITNESS_OPERATION_COUNT = 0;


	/**
	 * The meta object id for the '{@link scenario.schedule.impl.LineItemImpl <em>Line Item</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.schedule.impl.LineItemImpl
	 * @see scenario.schedule.impl.SchedulePackageImpl#getLineItem()
	 * @generated
	 */
	int LINE_ITEM = 5;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINE_ITEM__NAME = ScenarioPackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINE_ITEM__VALUE = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Party</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINE_ITEM__PARTY = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Line Item</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINE_ITEM_FEATURE_COUNT = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Get Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINE_ITEM___GET_CONTAINER = ScenarioPackage.NAMED_OBJECT___GET_CONTAINER;

	/**
	 * The operation id for the '<em>Is Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINE_ITEM___IS_COST = ScenarioPackage.NAMED_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Is Revenue</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINE_ITEM___IS_REVENUE = ScenarioPackage.NAMED_OBJECT_OPERATION_COUNT + 1;

	/**
	 * The number of operations of the '<em>Line Item</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINE_ITEM_OPERATION_COUNT = ScenarioPackage.NAMED_OBJECT_OPERATION_COUNT + 2;

	/**
	 * The meta object id for the '{@link scenario.schedule.impl.BookedRevenueImpl <em>Booked Revenue</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.schedule.impl.BookedRevenueImpl
	 * @see scenario.schedule.impl.SchedulePackageImpl#getBookedRevenue()
	 * @generated
	 */
	int BOOKED_REVENUE = 6;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOOKED_REVENUE__ENTITY = ScenarioPackage.SCENARIO_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Cargo</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOOKED_REVENUE__CARGO = ScenarioPackage.SCENARIO_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOOKED_REVENUE__DATE = ScenarioPackage.SCENARIO_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Line Items</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOOKED_REVENUE__LINE_ITEMS = ScenarioPackage.SCENARIO_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Booked Revenue</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOOKED_REVENUE_FEATURE_COUNT = ScenarioPackage.SCENARIO_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The operation id for the '<em>Get Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOOKED_REVENUE___GET_CONTAINER = ScenarioPackage.SCENARIO_OBJECT___GET_CONTAINER;

	/**
	 * The operation id for the '<em>Get Untaxed Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOOKED_REVENUE___GET_UNTAXED_VALUE = ScenarioPackage.SCENARIO_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Taxed Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOOKED_REVENUE___GET_TAXED_VALUE = ScenarioPackage.SCENARIO_OBJECT_OPERATION_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Untaxed Revenues</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOOKED_REVENUE___GET_UNTAXED_REVENUES = ScenarioPackage.SCENARIO_OBJECT_OPERATION_COUNT + 2;

	/**
	 * The operation id for the '<em>Get Untaxed Costs</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOOKED_REVENUE___GET_UNTAXED_COSTS = ScenarioPackage.SCENARIO_OBJECT_OPERATION_COUNT + 3;

	/**
	 * The operation id for the '<em>Get Tax Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOOKED_REVENUE___GET_TAX_COST = ScenarioPackage.SCENARIO_OBJECT_OPERATION_COUNT + 4;

	/**
	 * The number of operations of the '<em>Booked Revenue</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOOKED_REVENUE_OPERATION_COUNT = ScenarioPackage.SCENARIO_OBJECT_OPERATION_COUNT + 5;


	/**
	 * Returns the meta object for class '{@link scenario.schedule.ScheduleModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see scenario.schedule.ScheduleModel
	 * @generated
	 */
	EClass getScheduleModel();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.schedule.ScheduleModel#getSchedules <em>Schedules</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Schedules</em>'.
	 * @see scenario.schedule.ScheduleModel#getSchedules()
	 * @see #getScheduleModel()
	 * @generated
	 */
	EReference getScheduleModel_Schedules();

	/**
	 * Returns the meta object for class '{@link scenario.schedule.Schedule <em>Schedule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Schedule</em>'.
	 * @see scenario.schedule.Schedule
	 * @generated
	 */
	EClass getSchedule();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.schedule.Schedule#getSequences <em>Sequences</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Sequences</em>'.
	 * @see scenario.schedule.Schedule#getSequences()
	 * @see #getSchedule()
	 * @generated
	 */
	EReference getSchedule_Sequences();

	/**
	 * Returns the meta object for the attribute '{@link scenario.schedule.Schedule#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see scenario.schedule.Schedule#getName()
	 * @see #getSchedule()
	 * @generated
	 */
	EAttribute getSchedule_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.schedule.Schedule#getCargoAllocations <em>Cargo Allocations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Cargo Allocations</em>'.
	 * @see scenario.schedule.Schedule#getCargoAllocations()
	 * @see #getSchedule()
	 * @generated
	 */
	EReference getSchedule_CargoAllocations();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.schedule.Schedule#getFleet <em>Fleet</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Fleet</em>'.
	 * @see scenario.schedule.Schedule#getFleet()
	 * @see #getSchedule()
	 * @generated
	 */
	EReference getSchedule_Fleet();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.schedule.Schedule#getFitness <em>Fitness</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Fitness</em>'.
	 * @see scenario.schedule.Schedule#getFitness()
	 * @see #getSchedule()
	 * @generated
	 */
	EReference getSchedule_Fitness();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.schedule.Schedule#getRevenue <em>Revenue</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Revenue</em>'.
	 * @see scenario.schedule.Schedule#getRevenue()
	 * @see #getSchedule()
	 * @generated
	 */
	EReference getSchedule_Revenue();

	/**
	 * Returns the meta object for class '{@link scenario.schedule.Sequence <em>Sequence</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Sequence</em>'.
	 * @see scenario.schedule.Sequence
	 * @generated
	 */
	EClass getSequence();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.schedule.Sequence#getEvents <em>Events</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Events</em>'.
	 * @see scenario.schedule.Sequence#getEvents()
	 * @see #getSequence()
	 * @generated
	 */
	EReference getSequence_Events();

	/**
	 * Returns the meta object for the reference '{@link scenario.schedule.Sequence#getVessel <em>Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel</em>'.
	 * @see scenario.schedule.Sequence#getVessel()
	 * @see #getSequence()
	 * @generated
	 */
	EReference getSequence_Vessel();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.schedule.Sequence#getFitness <em>Fitness</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Fitness</em>'.
	 * @see scenario.schedule.Sequence#getFitness()
	 * @see #getSequence()
	 * @generated
	 */
	EReference getSequence_Fitness();

	/**
	 * Returns the meta object for class '{@link scenario.schedule.CargoAllocation <em>Cargo Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cargo Allocation</em>'.
	 * @see scenario.schedule.CargoAllocation
	 * @generated
	 */
	EClass getCargoAllocation();

	/**
	 * Returns the meta object for the reference '{@link scenario.schedule.CargoAllocation#getLoadSlot <em>Load Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Load Slot</em>'.
	 * @see scenario.schedule.CargoAllocation#getLoadSlot()
	 * @see #getCargoAllocation()
	 * @generated
	 */
	EReference getCargoAllocation_LoadSlot();

	/**
	 * Returns the meta object for the reference '{@link scenario.schedule.CargoAllocation#getDischargeSlot <em>Discharge Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Discharge Slot</em>'.
	 * @see scenario.schedule.CargoAllocation#getDischargeSlot()
	 * @see #getCargoAllocation()
	 * @generated
	 */
	EReference getCargoAllocation_DischargeSlot();

	/**
	 * Returns the meta object for the attribute '{@link scenario.schedule.CargoAllocation#getFuelVolume <em>Fuel Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fuel Volume</em>'.
	 * @see scenario.schedule.CargoAllocation#getFuelVolume()
	 * @see #getCargoAllocation()
	 * @generated
	 */
	EAttribute getCargoAllocation_FuelVolume();

	/**
	 * Returns the meta object for the attribute '{@link scenario.schedule.CargoAllocation#getDischargeVolume <em>Discharge Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Discharge Volume</em>'.
	 * @see scenario.schedule.CargoAllocation#getDischargeVolume()
	 * @see #getCargoAllocation()
	 * @generated
	 */
	EAttribute getCargoAllocation_DischargeVolume();

	/**
	 * Returns the meta object for the attribute '{@link scenario.schedule.CargoAllocation#getLoadDate <em>Load Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Load Date</em>'.
	 * @see scenario.schedule.CargoAllocation#getLoadDate()
	 * @see #getCargoAllocation()
	 * @generated
	 */
	EAttribute getCargoAllocation_LoadDate();

	/**
	 * Returns the meta object for the attribute '{@link scenario.schedule.CargoAllocation#getDischargeDate <em>Discharge Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Discharge Date</em>'.
	 * @see scenario.schedule.CargoAllocation#getDischargeDate()
	 * @see #getCargoAllocation()
	 * @generated
	 */
	EAttribute getCargoAllocation_DischargeDate();

	/**
	 * Returns the meta object for the attribute '{@link scenario.schedule.CargoAllocation#getLoadPriceM3 <em>Load Price M3</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Load Price M3</em>'.
	 * @see scenario.schedule.CargoAllocation#getLoadPriceM3()
	 * @see #getCargoAllocation()
	 * @generated
	 */
	EAttribute getCargoAllocation_LoadPriceM3();

	/**
	 * Returns the meta object for the attribute '{@link scenario.schedule.CargoAllocation#getDischargePriceM3 <em>Discharge Price M3</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Discharge Price M3</em>'.
	 * @see scenario.schedule.CargoAllocation#getDischargePriceM3()
	 * @see #getCargoAllocation()
	 * @generated
	 */
	EAttribute getCargoAllocation_DischargePriceM3();

	/**
	 * Returns the meta object for the reference '{@link scenario.schedule.CargoAllocation#getVessel <em>Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel</em>'.
	 * @see scenario.schedule.CargoAllocation#getVessel()
	 * @see #getCargoAllocation()
	 * @generated
	 */
	EReference getCargoAllocation_Vessel();

	/**
	 * Returns the meta object for the reference '{@link scenario.schedule.CargoAllocation#getLadenLeg <em>Laden Leg</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Laden Leg</em>'.
	 * @see scenario.schedule.CargoAllocation#getLadenLeg()
	 * @see #getCargoAllocation()
	 * @generated
	 */
	EReference getCargoAllocation_LadenLeg();

	/**
	 * Returns the meta object for the reference '{@link scenario.schedule.CargoAllocation#getBallastLeg <em>Ballast Leg</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Ballast Leg</em>'.
	 * @see scenario.schedule.CargoAllocation#getBallastLeg()
	 * @see #getCargoAllocation()
	 * @generated
	 */
	EReference getCargoAllocation_BallastLeg();

	/**
	 * Returns the meta object for the reference '{@link scenario.schedule.CargoAllocation#getLadenIdle <em>Laden Idle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Laden Idle</em>'.
	 * @see scenario.schedule.CargoAllocation#getLadenIdle()
	 * @see #getCargoAllocation()
	 * @generated
	 */
	EReference getCargoAllocation_LadenIdle();

	/**
	 * Returns the meta object for the reference '{@link scenario.schedule.CargoAllocation#getBallastIdle <em>Ballast Idle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Ballast Idle</em>'.
	 * @see scenario.schedule.CargoAllocation#getBallastIdle()
	 * @see #getCargoAllocation()
	 * @generated
	 */
	EReference getCargoAllocation_BallastIdle();

	/**
	 * Returns the meta object for the reference '{@link scenario.schedule.CargoAllocation#getLoadRevenue <em>Load Revenue</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Load Revenue</em>'.
	 * @see scenario.schedule.CargoAllocation#getLoadRevenue()
	 * @see #getCargoAllocation()
	 * @generated
	 */
	EReference getCargoAllocation_LoadRevenue();

	/**
	 * Returns the meta object for the reference '{@link scenario.schedule.CargoAllocation#getShippingRevenue <em>Shipping Revenue</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Shipping Revenue</em>'.
	 * @see scenario.schedule.CargoAllocation#getShippingRevenue()
	 * @see #getCargoAllocation()
	 * @generated
	 */
	EReference getCargoAllocation_ShippingRevenue();

	/**
	 * Returns the meta object for the reference '{@link scenario.schedule.CargoAllocation#getDischargeRevenue <em>Discharge Revenue</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Discharge Revenue</em>'.
	 * @see scenario.schedule.CargoAllocation#getDischargeRevenue()
	 * @see #getCargoAllocation()
	 * @generated
	 */
	EReference getCargoAllocation_DischargeRevenue();

	/**
	 * Returns the meta object for the '{@link scenario.schedule.CargoAllocation#getTotalCost() <em>Get Total Cost</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Total Cost</em>' operation.
	 * @see scenario.schedule.CargoAllocation#getTotalCost()
	 * @generated
	 */
	EOperation getCargoAllocation__GetTotalCost();

	/**
	 * Returns the meta object for the '{@link scenario.schedule.CargoAllocation#getLocalLoadDate() <em>Get Local Load Date</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Local Load Date</em>' operation.
	 * @see scenario.schedule.CargoAllocation#getLocalLoadDate()
	 * @generated
	 */
	EOperation getCargoAllocation__GetLocalLoadDate();

	/**
	 * Returns the meta object for the '{@link scenario.schedule.CargoAllocation#getLocalDischargeDate() <em>Get Local Discharge Date</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Local Discharge Date</em>' operation.
	 * @see scenario.schedule.CargoAllocation#getLocalDischargeDate()
	 * @generated
	 */
	EOperation getCargoAllocation__GetLocalDischargeDate();

	/**
	 * Returns the meta object for the '{@link scenario.schedule.CargoAllocation#getLoadVolume() <em>Get Load Volume</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Load Volume</em>' operation.
	 * @see scenario.schedule.CargoAllocation#getLoadVolume()
	 * @generated
	 */
	EOperation getCargoAllocation__GetLoadVolume();

	/**
	 * Returns the meta object for class '{@link scenario.schedule.ScheduleFitness <em>Fitness</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Fitness</em>'.
	 * @see scenario.schedule.ScheduleFitness
	 * @generated
	 */
	EClass getScheduleFitness();

	/**
	 * Returns the meta object for the attribute '{@link scenario.schedule.ScheduleFitness#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see scenario.schedule.ScheduleFitness#getName()
	 * @see #getScheduleFitness()
	 * @generated
	 */
	EAttribute getScheduleFitness_Name();

	/**
	 * Returns the meta object for the attribute '{@link scenario.schedule.ScheduleFitness#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see scenario.schedule.ScheduleFitness#getValue()
	 * @see #getScheduleFitness()
	 * @generated
	 */
	EAttribute getScheduleFitness_Value();

	/**
	 * Returns the meta object for class '{@link scenario.schedule.LineItem <em>Line Item</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Line Item</em>'.
	 * @see scenario.schedule.LineItem
	 * @generated
	 */
	EClass getLineItem();

	/**
	 * Returns the meta object for the attribute '{@link scenario.schedule.LineItem#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see scenario.schedule.LineItem#getValue()
	 * @see #getLineItem()
	 * @generated
	 */
	EAttribute getLineItem_Value();

	/**
	 * Returns the meta object for the reference '{@link scenario.schedule.LineItem#getParty <em>Party</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Party</em>'.
	 * @see scenario.schedule.LineItem#getParty()
	 * @see #getLineItem()
	 * @generated
	 */
	EReference getLineItem_Party();

	/**
	 * Returns the meta object for the '{@link scenario.schedule.LineItem#isCost() <em>Is Cost</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Is Cost</em>' operation.
	 * @see scenario.schedule.LineItem#isCost()
	 * @generated
	 */
	EOperation getLineItem__IsCost();

	/**
	 * Returns the meta object for the '{@link scenario.schedule.LineItem#isRevenue() <em>Is Revenue</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Is Revenue</em>' operation.
	 * @see scenario.schedule.LineItem#isRevenue()
	 * @generated
	 */
	EOperation getLineItem__IsRevenue();

	/**
	 * Returns the meta object for class '{@link scenario.schedule.BookedRevenue <em>Booked Revenue</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Booked Revenue</em>'.
	 * @see scenario.schedule.BookedRevenue
	 * @generated
	 */
	EClass getBookedRevenue();

	/**
	 * Returns the meta object for the reference '{@link scenario.schedule.BookedRevenue#getEntity <em>Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Entity</em>'.
	 * @see scenario.schedule.BookedRevenue#getEntity()
	 * @see #getBookedRevenue()
	 * @generated
	 */
	EReference getBookedRevenue_Entity();

	/**
	 * Returns the meta object for the reference '{@link scenario.schedule.BookedRevenue#getCargo <em>Cargo</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Cargo</em>'.
	 * @see scenario.schedule.BookedRevenue#getCargo()
	 * @see #getBookedRevenue()
	 * @generated
	 */
	EReference getBookedRevenue_Cargo();

	/**
	 * Returns the meta object for the attribute '{@link scenario.schedule.BookedRevenue#getDate <em>Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Date</em>'.
	 * @see scenario.schedule.BookedRevenue#getDate()
	 * @see #getBookedRevenue()
	 * @generated
	 */
	EAttribute getBookedRevenue_Date();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.schedule.BookedRevenue#getLineItems <em>Line Items</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Line Items</em>'.
	 * @see scenario.schedule.BookedRevenue#getLineItems()
	 * @see #getBookedRevenue()
	 * @generated
	 */
	EReference getBookedRevenue_LineItems();

	/**
	 * Returns the meta object for the '{@link scenario.schedule.BookedRevenue#getUntaxedValue() <em>Get Untaxed Value</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Untaxed Value</em>' operation.
	 * @see scenario.schedule.BookedRevenue#getUntaxedValue()
	 * @generated
	 */
	EOperation getBookedRevenue__GetUntaxedValue();

	/**
	 * Returns the meta object for the '{@link scenario.schedule.BookedRevenue#getTaxedValue() <em>Get Taxed Value</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Taxed Value</em>' operation.
	 * @see scenario.schedule.BookedRevenue#getTaxedValue()
	 * @generated
	 */
	EOperation getBookedRevenue__GetTaxedValue();

	/**
	 * Returns the meta object for the '{@link scenario.schedule.BookedRevenue#getUntaxedRevenues() <em>Get Untaxed Revenues</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Untaxed Revenues</em>' operation.
	 * @see scenario.schedule.BookedRevenue#getUntaxedRevenues()
	 * @generated
	 */
	EOperation getBookedRevenue__GetUntaxedRevenues();

	/**
	 * Returns the meta object for the '{@link scenario.schedule.BookedRevenue#getUntaxedCosts() <em>Get Untaxed Costs</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Untaxed Costs</em>' operation.
	 * @see scenario.schedule.BookedRevenue#getUntaxedCosts()
	 * @generated
	 */
	EOperation getBookedRevenue__GetUntaxedCosts();

	/**
	 * Returns the meta object for the '{@link scenario.schedule.BookedRevenue#getTaxCost() <em>Get Tax Cost</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Tax Cost</em>' operation.
	 * @see scenario.schedule.BookedRevenue#getTaxCost()
	 * @generated
	 */
	EOperation getBookedRevenue__GetTaxCost();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ScheduleFactory getScheduleFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link scenario.schedule.impl.ScheduleModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.schedule.impl.ScheduleModelImpl
		 * @see scenario.schedule.impl.SchedulePackageImpl#getScheduleModel()
		 * @generated
		 */
		EClass SCHEDULE_MODEL = eINSTANCE.getScheduleModel();

		/**
		 * The meta object literal for the '<em><b>Schedules</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEDULE_MODEL__SCHEDULES = eINSTANCE.getScheduleModel_Schedules();

		/**
		 * The meta object literal for the '{@link scenario.schedule.impl.ScheduleImpl <em>Schedule</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.schedule.impl.ScheduleImpl
		 * @see scenario.schedule.impl.SchedulePackageImpl#getSchedule()
		 * @generated
		 */
		EClass SCHEDULE = eINSTANCE.getSchedule();

		/**
		 * The meta object literal for the '<em><b>Sequences</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEDULE__SEQUENCES = eINSTANCE.getSchedule_Sequences();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCHEDULE__NAME = eINSTANCE.getSchedule_Name();

		/**
		 * The meta object literal for the '<em><b>Cargo Allocations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEDULE__CARGO_ALLOCATIONS = eINSTANCE.getSchedule_CargoAllocations();

		/**
		 * The meta object literal for the '<em><b>Fleet</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEDULE__FLEET = eINSTANCE.getSchedule_Fleet();

		/**
		 * The meta object literal for the '<em><b>Fitness</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEDULE__FITNESS = eINSTANCE.getSchedule_Fitness();

		/**
		 * The meta object literal for the '<em><b>Revenue</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEDULE__REVENUE = eINSTANCE.getSchedule_Revenue();

		/**
		 * The meta object literal for the '{@link scenario.schedule.impl.SequenceImpl <em>Sequence</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.schedule.impl.SequenceImpl
		 * @see scenario.schedule.impl.SchedulePackageImpl#getSequence()
		 * @generated
		 */
		EClass SEQUENCE = eINSTANCE.getSequence();

		/**
		 * The meta object literal for the '<em><b>Events</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SEQUENCE__EVENTS = eINSTANCE.getSequence_Events();

		/**
		 * The meta object literal for the '<em><b>Vessel</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SEQUENCE__VESSEL = eINSTANCE.getSequence_Vessel();

		/**
		 * The meta object literal for the '<em><b>Fitness</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SEQUENCE__FITNESS = eINSTANCE.getSequence_Fitness();

		/**
		 * The meta object literal for the '{@link scenario.schedule.impl.CargoAllocationImpl <em>Cargo Allocation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.schedule.impl.CargoAllocationImpl
		 * @see scenario.schedule.impl.SchedulePackageImpl#getCargoAllocation()
		 * @generated
		 */
		EClass CARGO_ALLOCATION = eINSTANCE.getCargoAllocation();

		/**
		 * The meta object literal for the '<em><b>Load Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_ALLOCATION__LOAD_SLOT = eINSTANCE.getCargoAllocation_LoadSlot();

		/**
		 * The meta object literal for the '<em><b>Discharge Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_ALLOCATION__DISCHARGE_SLOT = eINSTANCE.getCargoAllocation_DischargeSlot();

		/**
		 * The meta object literal for the '<em><b>Fuel Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO_ALLOCATION__FUEL_VOLUME = eINSTANCE.getCargoAllocation_FuelVolume();

		/**
		 * The meta object literal for the '<em><b>Discharge Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO_ALLOCATION__DISCHARGE_VOLUME = eINSTANCE.getCargoAllocation_DischargeVolume();

		/**
		 * The meta object literal for the '<em><b>Load Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO_ALLOCATION__LOAD_DATE = eINSTANCE.getCargoAllocation_LoadDate();

		/**
		 * The meta object literal for the '<em><b>Discharge Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO_ALLOCATION__DISCHARGE_DATE = eINSTANCE.getCargoAllocation_DischargeDate();

		/**
		 * The meta object literal for the '<em><b>Load Price M3</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO_ALLOCATION__LOAD_PRICE_M3 = eINSTANCE.getCargoAllocation_LoadPriceM3();

		/**
		 * The meta object literal for the '<em><b>Discharge Price M3</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO_ALLOCATION__DISCHARGE_PRICE_M3 = eINSTANCE.getCargoAllocation_DischargePriceM3();

		/**
		 * The meta object literal for the '<em><b>Vessel</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_ALLOCATION__VESSEL = eINSTANCE.getCargoAllocation_Vessel();

		/**
		 * The meta object literal for the '<em><b>Laden Leg</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_ALLOCATION__LADEN_LEG = eINSTANCE.getCargoAllocation_LadenLeg();

		/**
		 * The meta object literal for the '<em><b>Ballast Leg</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_ALLOCATION__BALLAST_LEG = eINSTANCE.getCargoAllocation_BallastLeg();

		/**
		 * The meta object literal for the '<em><b>Laden Idle</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_ALLOCATION__LADEN_IDLE = eINSTANCE.getCargoAllocation_LadenIdle();

		/**
		 * The meta object literal for the '<em><b>Ballast Idle</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_ALLOCATION__BALLAST_IDLE = eINSTANCE.getCargoAllocation_BallastIdle();

		/**
		 * The meta object literal for the '<em><b>Load Revenue</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_ALLOCATION__LOAD_REVENUE = eINSTANCE.getCargoAllocation_LoadRevenue();

		/**
		 * The meta object literal for the '<em><b>Shipping Revenue</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_ALLOCATION__SHIPPING_REVENUE = eINSTANCE.getCargoAllocation_ShippingRevenue();

		/**
		 * The meta object literal for the '<em><b>Discharge Revenue</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_ALLOCATION__DISCHARGE_REVENUE = eINSTANCE.getCargoAllocation_DischargeRevenue();

		/**
		 * The meta object literal for the '<em><b>Get Total Cost</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CARGO_ALLOCATION___GET_TOTAL_COST = eINSTANCE.getCargoAllocation__GetTotalCost();

		/**
		 * The meta object literal for the '<em><b>Get Local Load Date</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CARGO_ALLOCATION___GET_LOCAL_LOAD_DATE = eINSTANCE.getCargoAllocation__GetLocalLoadDate();

		/**
		 * The meta object literal for the '<em><b>Get Local Discharge Date</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CARGO_ALLOCATION___GET_LOCAL_DISCHARGE_DATE = eINSTANCE.getCargoAllocation__GetLocalDischargeDate();

		/**
		 * The meta object literal for the '<em><b>Get Load Volume</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CARGO_ALLOCATION___GET_LOAD_VOLUME = eINSTANCE.getCargoAllocation__GetLoadVolume();

		/**
		 * The meta object literal for the '{@link scenario.schedule.impl.ScheduleFitnessImpl <em>Fitness</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.schedule.impl.ScheduleFitnessImpl
		 * @see scenario.schedule.impl.SchedulePackageImpl#getScheduleFitness()
		 * @generated
		 */
		EClass SCHEDULE_FITNESS = eINSTANCE.getScheduleFitness();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCHEDULE_FITNESS__NAME = eINSTANCE.getScheduleFitness_Name();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCHEDULE_FITNESS__VALUE = eINSTANCE.getScheduleFitness_Value();

		/**
		 * The meta object literal for the '{@link scenario.schedule.impl.LineItemImpl <em>Line Item</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.schedule.impl.LineItemImpl
		 * @see scenario.schedule.impl.SchedulePackageImpl#getLineItem()
		 * @generated
		 */
		EClass LINE_ITEM = eINSTANCE.getLineItem();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LINE_ITEM__VALUE = eINSTANCE.getLineItem_Value();

		/**
		 * The meta object literal for the '<em><b>Party</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LINE_ITEM__PARTY = eINSTANCE.getLineItem_Party();

		/**
		 * The meta object literal for the '<em><b>Is Cost</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation LINE_ITEM___IS_COST = eINSTANCE.getLineItem__IsCost();

		/**
		 * The meta object literal for the '<em><b>Is Revenue</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation LINE_ITEM___IS_REVENUE = eINSTANCE.getLineItem__IsRevenue();

		/**
		 * The meta object literal for the '{@link scenario.schedule.impl.BookedRevenueImpl <em>Booked Revenue</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.schedule.impl.BookedRevenueImpl
		 * @see scenario.schedule.impl.SchedulePackageImpl#getBookedRevenue()
		 * @generated
		 */
		EClass BOOKED_REVENUE = eINSTANCE.getBookedRevenue();

		/**
		 * The meta object literal for the '<em><b>Entity</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BOOKED_REVENUE__ENTITY = eINSTANCE.getBookedRevenue_Entity();

		/**
		 * The meta object literal for the '<em><b>Cargo</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BOOKED_REVENUE__CARGO = eINSTANCE.getBookedRevenue_Cargo();

		/**
		 * The meta object literal for the '<em><b>Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BOOKED_REVENUE__DATE = eINSTANCE.getBookedRevenue_Date();

		/**
		 * The meta object literal for the '<em><b>Line Items</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BOOKED_REVENUE__LINE_ITEMS = eINSTANCE.getBookedRevenue_LineItems();

		/**
		 * The meta object literal for the '<em><b>Get Untaxed Value</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation BOOKED_REVENUE___GET_UNTAXED_VALUE = eINSTANCE.getBookedRevenue__GetUntaxedValue();

		/**
		 * The meta object literal for the '<em><b>Get Taxed Value</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation BOOKED_REVENUE___GET_TAXED_VALUE = eINSTANCE.getBookedRevenue__GetTaxedValue();

		/**
		 * The meta object literal for the '<em><b>Get Untaxed Revenues</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation BOOKED_REVENUE___GET_UNTAXED_REVENUES = eINSTANCE.getBookedRevenue__GetUntaxedRevenues();

		/**
		 * The meta object literal for the '<em><b>Get Untaxed Costs</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation BOOKED_REVENUE___GET_UNTAXED_COSTS = eINSTANCE.getBookedRevenue__GetUntaxedCosts();

		/**
		 * The meta object literal for the '<em><b>Get Tax Cost</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation BOOKED_REVENUE___GET_TAX_COST = eINSTANCE.getBookedRevenue__GetTaxCost();

	}

} //SchedulePackage
