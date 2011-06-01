/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.schedule.events;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
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
 * @see scenario.schedule.events.EventsFactory
 * @model kind="package"
 * @generated
 */
public interface EventsPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "events";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://com.mmxlabs.lng.emf/schedule/events";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "com.mmxlabs.lng.emf.schedule.events";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	EventsPackage eINSTANCE = scenario.schedule.events.impl.EventsPackageImpl.init();

	/**
	 * The meta object id for the '{@link scenario.schedule.events.impl.FuelMixtureImpl <em>Fuel Mixture</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.schedule.events.impl.FuelMixtureImpl
	 * @see scenario.schedule.events.impl.EventsPackageImpl#getFuelMixture()
	 * @generated
	 */
	int FUEL_MIXTURE = 0;

	/**
	 * The feature id for the '<em><b>Fuel Usage</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_MIXTURE__FUEL_USAGE = 0;

	/**
	 * The number of structural features of the '<em>Fuel Mixture</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_MIXTURE_FEATURE_COUNT = 1;

	/**
	 * The operation id for the '<em>Get Total Fuel Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_MIXTURE___GET_TOTAL_FUEL_COST = 0;

	/**
	 * The number of operations of the '<em>Fuel Mixture</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_MIXTURE_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link scenario.schedule.events.impl.FuelQuantityImpl <em>Fuel Quantity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.schedule.events.impl.FuelQuantityImpl
	 * @see scenario.schedule.events.impl.EventsPackageImpl#getFuelQuantity()
	 * @generated
	 */
	int FUEL_QUANTITY = 1;

	/**
	 * The feature id for the '<em><b>Fuel Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_QUANTITY__FUEL_TYPE = 0;

	/**
	 * The feature id for the '<em><b>Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_QUANTITY__QUANTITY = 1;

	/**
	 * The feature id for the '<em><b>Unit Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_QUANTITY__UNIT_PRICE = 2;

	/**
	 * The feature id for the '<em><b>Total Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_QUANTITY__TOTAL_PRICE = 3;

	/**
	 * The feature id for the '<em><b>Fuel Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_QUANTITY__FUEL_UNIT = 4;

	/**
	 * The number of structural features of the '<em>Fuel Quantity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_QUANTITY_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>Fuel Quantity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_QUANTITY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link scenario.schedule.events.impl.ScheduledEventImpl <em>Scheduled Event</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.schedule.events.impl.ScheduledEventImpl
	 * @see scenario.schedule.events.impl.EventsPackageImpl#getScheduledEvent()
	 * @generated
	 */
	int SCHEDULED_EVENT = 2;

	/**
	 * The feature id for the '<em><b>Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULED_EVENT__START_TIME = ScenarioPackage.SCENARIO_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>End Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULED_EVENT__END_TIME = ScenarioPackage.SCENARIO_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Scheduled Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULED_EVENT_FEATURE_COUNT = ScenarioPackage.SCENARIO_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Get Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULED_EVENT___GET_CONTAINER = ScenarioPackage.SCENARIO_OBJECT___GET_CONTAINER;

	/**
	 * The operation id for the '<em>Get Event Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULED_EVENT___GET_EVENT_DURATION = ScenarioPackage.SCENARIO_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Hire Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULED_EVENT___GET_HIRE_COST = ScenarioPackage.SCENARIO_OBJECT_OPERATION_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Local Start Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULED_EVENT___GET_LOCAL_START_TIME = ScenarioPackage.SCENARIO_OBJECT_OPERATION_COUNT + 2;

	/**
	 * The operation id for the '<em>Get Local End Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULED_EVENT___GET_LOCAL_END_TIME = ScenarioPackage.SCENARIO_OBJECT_OPERATION_COUNT + 3;

	/**
	 * The number of operations of the '<em>Scheduled Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULED_EVENT_OPERATION_COUNT = ScenarioPackage.SCENARIO_OBJECT_OPERATION_COUNT + 4;

	/**
	 * The meta object id for the '{@link scenario.schedule.events.impl.IdleImpl <em>Idle</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.schedule.events.impl.IdleImpl
	 * @see scenario.schedule.events.impl.EventsPackageImpl#getIdle()
	 * @generated
	 */
	int IDLE = 3;

	/**
	 * The meta object id for the '{@link scenario.schedule.events.impl.JourneyImpl <em>Journey</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.schedule.events.impl.JourneyImpl
	 * @see scenario.schedule.events.impl.EventsPackageImpl#getJourney()
	 * @generated
	 */
	int JOURNEY = 4;

	/**
	 * The meta object id for the '{@link scenario.schedule.events.impl.PortVisitImpl <em>Port Visit</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.schedule.events.impl.PortVisitImpl
	 * @see scenario.schedule.events.impl.EventsPackageImpl#getPortVisit()
	 * @generated
	 */
	int PORT_VISIT = 5;

	/**
	 * The feature id for the '<em><b>Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT__START_TIME = SCHEDULED_EVENT__START_TIME;

	/**
	 * The feature id for the '<em><b>End Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT__END_TIME = SCHEDULED_EVENT__END_TIME;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT__PORT = SCHEDULED_EVENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Port Visit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT_FEATURE_COUNT = SCHEDULED_EVENT_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT___GET_CONTAINER = SCHEDULED_EVENT___GET_CONTAINER;

	/**
	 * The operation id for the '<em>Get Event Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT___GET_EVENT_DURATION = SCHEDULED_EVENT___GET_EVENT_DURATION;

	/**
	 * The operation id for the '<em>Get Hire Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT___GET_HIRE_COST = SCHEDULED_EVENT___GET_HIRE_COST;

	/**
	 * The operation id for the '<em>Get Local Start Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT___GET_LOCAL_START_TIME = SCHEDULED_EVENT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Local End Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT___GET_LOCAL_END_TIME = SCHEDULED_EVENT_OPERATION_COUNT + 1;

	/**
	 * The number of operations of the '<em>Port Visit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT_OPERATION_COUNT = SCHEDULED_EVENT_OPERATION_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE__START_TIME = PORT_VISIT__START_TIME;

	/**
	 * The feature id for the '<em><b>End Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE__END_TIME = PORT_VISIT__END_TIME;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE__PORT = PORT_VISIT__PORT;

	/**
	 * The feature id for the '<em><b>Fuel Usage</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE__FUEL_USAGE = PORT_VISIT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Vessel State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE__VESSEL_STATE = PORT_VISIT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Idle</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE_FEATURE_COUNT = PORT_VISIT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Get Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE___GET_CONTAINER = PORT_VISIT___GET_CONTAINER;

	/**
	 * The operation id for the '<em>Get Event Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE___GET_EVENT_DURATION = PORT_VISIT___GET_EVENT_DURATION;

	/**
	 * The operation id for the '<em>Get Hire Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE___GET_HIRE_COST = PORT_VISIT___GET_HIRE_COST;

	/**
	 * The operation id for the '<em>Get Local Start Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE___GET_LOCAL_START_TIME = PORT_VISIT___GET_LOCAL_START_TIME;

	/**
	 * The operation id for the '<em>Get Local End Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE___GET_LOCAL_END_TIME = PORT_VISIT___GET_LOCAL_END_TIME;

	/**
	 * The operation id for the '<em>Get Total Fuel Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE___GET_TOTAL_FUEL_COST = PORT_VISIT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Total Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE___GET_TOTAL_COST = PORT_VISIT_OPERATION_COUNT + 1;

	/**
	 * The number of operations of the '<em>Idle</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE_OPERATION_COUNT = PORT_VISIT_OPERATION_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__START_TIME = SCHEDULED_EVENT__START_TIME;

	/**
	 * The feature id for the '<em><b>End Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__END_TIME = SCHEDULED_EVENT__END_TIME;

	/**
	 * The feature id for the '<em><b>Fuel Usage</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__FUEL_USAGE = SCHEDULED_EVENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>To Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__TO_PORT = SCHEDULED_EVENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Vessel State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__VESSEL_STATE = SCHEDULED_EVENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Route</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__ROUTE = SCHEDULED_EVENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__SPEED = SCHEDULED_EVENT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Distance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__DISTANCE = SCHEDULED_EVENT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>From Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__FROM_PORT = SCHEDULED_EVENT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Route Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__ROUTE_COST = SCHEDULED_EVENT_FEATURE_COUNT + 7;

	/**
	 * The number of structural features of the '<em>Journey</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY_FEATURE_COUNT = SCHEDULED_EVENT_FEATURE_COUNT + 8;

	/**
	 * The operation id for the '<em>Get Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY___GET_CONTAINER = SCHEDULED_EVENT___GET_CONTAINER;

	/**
	 * The operation id for the '<em>Get Event Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY___GET_EVENT_DURATION = SCHEDULED_EVENT___GET_EVENT_DURATION;

	/**
	 * The operation id for the '<em>Get Hire Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY___GET_HIRE_COST = SCHEDULED_EVENT___GET_HIRE_COST;

	/**
	 * The operation id for the '<em>Get Total Fuel Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY___GET_TOTAL_FUEL_COST = SCHEDULED_EVENT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Total Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY___GET_TOTAL_COST = SCHEDULED_EVENT_OPERATION_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Local Start Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY___GET_LOCAL_START_TIME = SCHEDULED_EVENT_OPERATION_COUNT + 2;

	/**
	 * The operation id for the '<em>Get Local End Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY___GET_LOCAL_END_TIME = SCHEDULED_EVENT_OPERATION_COUNT + 3;

	/**
	 * The number of operations of the '<em>Journey</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY_OPERATION_COUNT = SCHEDULED_EVENT_OPERATION_COUNT + 4;

	/**
	 * The meta object id for the '{@link scenario.schedule.events.impl.SlotVisitImpl <em>Slot Visit</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.schedule.events.impl.SlotVisitImpl
	 * @see scenario.schedule.events.impl.EventsPackageImpl#getSlotVisit()
	 * @generated
	 */
	int SLOT_VISIT = 6;

	/**
	 * The feature id for the '<em><b>Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT__START_TIME = PORT_VISIT__START_TIME;

	/**
	 * The feature id for the '<em><b>End Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT__END_TIME = PORT_VISIT__END_TIME;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT__PORT = PORT_VISIT__PORT;

	/**
	 * The feature id for the '<em><b>Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT__SLOT = PORT_VISIT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Cargo Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT__CARGO_ALLOCATION = PORT_VISIT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Slot Visit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT_FEATURE_COUNT = PORT_VISIT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Get Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT___GET_CONTAINER = PORT_VISIT___GET_CONTAINER;

	/**
	 * The operation id for the '<em>Get Event Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT___GET_EVENT_DURATION = PORT_VISIT___GET_EVENT_DURATION;

	/**
	 * The operation id for the '<em>Get Hire Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT___GET_HIRE_COST = PORT_VISIT___GET_HIRE_COST;

	/**
	 * The operation id for the '<em>Get Local Start Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT___GET_LOCAL_START_TIME = PORT_VISIT___GET_LOCAL_START_TIME;

	/**
	 * The operation id for the '<em>Get Local End Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT___GET_LOCAL_END_TIME = PORT_VISIT___GET_LOCAL_END_TIME;

	/**
	 * The number of operations of the '<em>Slot Visit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT_OPERATION_COUNT = PORT_VISIT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link scenario.schedule.events.impl.CharterOutVisitImpl <em>Charter Out Visit</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.schedule.events.impl.CharterOutVisitImpl
	 * @see scenario.schedule.events.impl.EventsPackageImpl#getCharterOutVisit()
	 * @generated
	 */
	int CHARTER_OUT_VISIT = 7;

	/**
	 * The feature id for the '<em><b>Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_VISIT__START_TIME = PORT_VISIT__START_TIME;

	/**
	 * The feature id for the '<em><b>End Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_VISIT__END_TIME = PORT_VISIT__END_TIME;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_VISIT__PORT = PORT_VISIT__PORT;

	/**
	 * The feature id for the '<em><b>Charter Out</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_VISIT__CHARTER_OUT = PORT_VISIT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Revenue</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_VISIT__REVENUE = PORT_VISIT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Charter Out Visit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_VISIT_FEATURE_COUNT = PORT_VISIT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Get Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_VISIT___GET_CONTAINER = PORT_VISIT___GET_CONTAINER;

	/**
	 * The operation id for the '<em>Get Event Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_VISIT___GET_EVENT_DURATION = PORT_VISIT___GET_EVENT_DURATION;

	/**
	 * The operation id for the '<em>Get Hire Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_VISIT___GET_HIRE_COST = PORT_VISIT___GET_HIRE_COST;

	/**
	 * The operation id for the '<em>Get Local Start Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_VISIT___GET_LOCAL_START_TIME = PORT_VISIT___GET_LOCAL_START_TIME;

	/**
	 * The operation id for the '<em>Get Local End Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_VISIT___GET_LOCAL_END_TIME = PORT_VISIT___GET_LOCAL_END_TIME;

	/**
	 * The number of operations of the '<em>Charter Out Visit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTER_OUT_VISIT_OPERATION_COUNT = PORT_VISIT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link scenario.schedule.events.FuelUnit <em>Fuel Unit</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.schedule.events.FuelUnit
	 * @see scenario.schedule.events.impl.EventsPackageImpl#getFuelUnit()
	 * @generated
	 */
	int FUEL_UNIT = 8;

	/**
	 * The meta object id for the '{@link scenario.schedule.events.FuelType <em>Fuel Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.schedule.events.FuelType
	 * @see scenario.schedule.events.impl.EventsPackageImpl#getFuelType()
	 * @generated
	 */
	int FUEL_TYPE = 9;


	/**
	 * Returns the meta object for class '{@link scenario.schedule.events.FuelMixture <em>Fuel Mixture</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Fuel Mixture</em>'.
	 * @see scenario.schedule.events.FuelMixture
	 * @generated
	 */
	EClass getFuelMixture();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.schedule.events.FuelMixture#getFuelUsage <em>Fuel Usage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Fuel Usage</em>'.
	 * @see scenario.schedule.events.FuelMixture#getFuelUsage()
	 * @see #getFuelMixture()
	 * @generated
	 */
	EReference getFuelMixture_FuelUsage();

	/**
	 * Returns the meta object for the '{@link scenario.schedule.events.FuelMixture#getTotalFuelCost() <em>Get Total Fuel Cost</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Total Fuel Cost</em>' operation.
	 * @see scenario.schedule.events.FuelMixture#getTotalFuelCost()
	 * @generated
	 */
	EOperation getFuelMixture__GetTotalFuelCost();

	/**
	 * Returns the meta object for class '{@link scenario.schedule.events.FuelQuantity <em>Fuel Quantity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Fuel Quantity</em>'.
	 * @see scenario.schedule.events.FuelQuantity
	 * @generated
	 */
	EClass getFuelQuantity();

	/**
	 * Returns the meta object for the attribute '{@link scenario.schedule.events.FuelQuantity#getFuelType <em>Fuel Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fuel Type</em>'.
	 * @see scenario.schedule.events.FuelQuantity#getFuelType()
	 * @see #getFuelQuantity()
	 * @generated
	 */
	EAttribute getFuelQuantity_FuelType();

	/**
	 * Returns the meta object for the attribute '{@link scenario.schedule.events.FuelQuantity#getQuantity <em>Quantity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Quantity</em>'.
	 * @see scenario.schedule.events.FuelQuantity#getQuantity()
	 * @see #getFuelQuantity()
	 * @generated
	 */
	EAttribute getFuelQuantity_Quantity();

	/**
	 * Returns the meta object for the attribute '{@link scenario.schedule.events.FuelQuantity#getUnitPrice <em>Unit Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Unit Price</em>'.
	 * @see scenario.schedule.events.FuelQuantity#getUnitPrice()
	 * @see #getFuelQuantity()
	 * @generated
	 */
	EAttribute getFuelQuantity_UnitPrice();

	/**
	 * Returns the meta object for the attribute '{@link scenario.schedule.events.FuelQuantity#getTotalPrice <em>Total Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Total Price</em>'.
	 * @see scenario.schedule.events.FuelQuantity#getTotalPrice()
	 * @see #getFuelQuantity()
	 * @generated
	 */
	EAttribute getFuelQuantity_TotalPrice();

	/**
	 * Returns the meta object for the attribute '{@link scenario.schedule.events.FuelQuantity#getFuelUnit <em>Fuel Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fuel Unit</em>'.
	 * @see scenario.schedule.events.FuelQuantity#getFuelUnit()
	 * @see #getFuelQuantity()
	 * @generated
	 */
	EAttribute getFuelQuantity_FuelUnit();

	/**
	 * Returns the meta object for class '{@link scenario.schedule.events.ScheduledEvent <em>Scheduled Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Scheduled Event</em>'.
	 * @see scenario.schedule.events.ScheduledEvent
	 * @generated
	 */
	EClass getScheduledEvent();

	/**
	 * Returns the meta object for the attribute '{@link scenario.schedule.events.ScheduledEvent#getStartTime <em>Start Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start Time</em>'.
	 * @see scenario.schedule.events.ScheduledEvent#getStartTime()
	 * @see #getScheduledEvent()
	 * @generated
	 */
	EAttribute getScheduledEvent_StartTime();

	/**
	 * Returns the meta object for the attribute '{@link scenario.schedule.events.ScheduledEvent#getEndTime <em>End Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End Time</em>'.
	 * @see scenario.schedule.events.ScheduledEvent#getEndTime()
	 * @see #getScheduledEvent()
	 * @generated
	 */
	EAttribute getScheduledEvent_EndTime();

	/**
	 * Returns the meta object for the '{@link scenario.schedule.events.ScheduledEvent#getEventDuration() <em>Get Event Duration</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Event Duration</em>' operation.
	 * @see scenario.schedule.events.ScheduledEvent#getEventDuration()
	 * @generated
	 */
	EOperation getScheduledEvent__GetEventDuration();

	/**
	 * Returns the meta object for the '{@link scenario.schedule.events.ScheduledEvent#getHireCost() <em>Get Hire Cost</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Hire Cost</em>' operation.
	 * @see scenario.schedule.events.ScheduledEvent#getHireCost()
	 * @generated
	 */
	EOperation getScheduledEvent__GetHireCost();

	/**
	 * Returns the meta object for the '{@link scenario.schedule.events.ScheduledEvent#getLocalStartTime() <em>Get Local Start Time</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Local Start Time</em>' operation.
	 * @see scenario.schedule.events.ScheduledEvent#getLocalStartTime()
	 * @generated
	 */
	EOperation getScheduledEvent__GetLocalStartTime();

	/**
	 * Returns the meta object for the '{@link scenario.schedule.events.ScheduledEvent#getLocalEndTime() <em>Get Local End Time</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Local End Time</em>' operation.
	 * @see scenario.schedule.events.ScheduledEvent#getLocalEndTime()
	 * @generated
	 */
	EOperation getScheduledEvent__GetLocalEndTime();

	/**
	 * Returns the meta object for class '{@link scenario.schedule.events.Idle <em>Idle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Idle</em>'.
	 * @see scenario.schedule.events.Idle
	 * @generated
	 */
	EClass getIdle();

	/**
	 * Returns the meta object for the attribute '{@link scenario.schedule.events.Idle#getVesselState <em>Vessel State</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Vessel State</em>'.
	 * @see scenario.schedule.events.Idle#getVesselState()
	 * @see #getIdle()
	 * @generated
	 */
	EAttribute getIdle_VesselState();

	/**
	 * Returns the meta object for the '{@link scenario.schedule.events.Idle#getTotalCost() <em>Get Total Cost</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Total Cost</em>' operation.
	 * @see scenario.schedule.events.Idle#getTotalCost()
	 * @generated
	 */
	EOperation getIdle__GetTotalCost();

	/**
	 * Returns the meta object for class '{@link scenario.schedule.events.Journey <em>Journey</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Journey</em>'.
	 * @see scenario.schedule.events.Journey
	 * @generated
	 */
	EClass getJourney();

	/**
	 * Returns the meta object for the reference '{@link scenario.schedule.events.Journey#getToPort <em>To Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>To Port</em>'.
	 * @see scenario.schedule.events.Journey#getToPort()
	 * @see #getJourney()
	 * @generated
	 */
	EReference getJourney_ToPort();

	/**
	 * Returns the meta object for the attribute '{@link scenario.schedule.events.Journey#getVesselState <em>Vessel State</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Vessel State</em>'.
	 * @see scenario.schedule.events.Journey#getVesselState()
	 * @see #getJourney()
	 * @generated
	 */
	EAttribute getJourney_VesselState();

	/**
	 * Returns the meta object for the attribute '{@link scenario.schedule.events.Journey#getRoute <em>Route</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Route</em>'.
	 * @see scenario.schedule.events.Journey#getRoute()
	 * @see #getJourney()
	 * @generated
	 */
	EAttribute getJourney_Route();

	/**
	 * Returns the meta object for the attribute '{@link scenario.schedule.events.Journey#getSpeed <em>Speed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Speed</em>'.
	 * @see scenario.schedule.events.Journey#getSpeed()
	 * @see #getJourney()
	 * @generated
	 */
	EAttribute getJourney_Speed();

	/**
	 * Returns the meta object for the attribute '{@link scenario.schedule.events.Journey#getDistance <em>Distance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Distance</em>'.
	 * @see scenario.schedule.events.Journey#getDistance()
	 * @see #getJourney()
	 * @generated
	 */
	EAttribute getJourney_Distance();

	/**
	 * Returns the meta object for the reference '{@link scenario.schedule.events.Journey#getFromPort <em>From Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>From Port</em>'.
	 * @see scenario.schedule.events.Journey#getFromPort()
	 * @see #getJourney()
	 * @generated
	 */
	EReference getJourney_FromPort();

	/**
	 * Returns the meta object for the attribute '{@link scenario.schedule.events.Journey#getRouteCost <em>Route Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Route Cost</em>'.
	 * @see scenario.schedule.events.Journey#getRouteCost()
	 * @see #getJourney()
	 * @generated
	 */
	EAttribute getJourney_RouteCost();

	/**
	 * Returns the meta object for the '{@link scenario.schedule.events.Journey#getTotalCost() <em>Get Total Cost</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Total Cost</em>' operation.
	 * @see scenario.schedule.events.Journey#getTotalCost()
	 * @generated
	 */
	EOperation getJourney__GetTotalCost();

	/**
	 * Returns the meta object for the '{@link scenario.schedule.events.Journey#getLocalStartTime() <em>Get Local Start Time</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Local Start Time</em>' operation.
	 * @see scenario.schedule.events.Journey#getLocalStartTime()
	 * @generated
	 */
	EOperation getJourney__GetLocalStartTime();

	/**
	 * Returns the meta object for the '{@link scenario.schedule.events.Journey#getLocalEndTime() <em>Get Local End Time</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Local End Time</em>' operation.
	 * @see scenario.schedule.events.Journey#getLocalEndTime()
	 * @generated
	 */
	EOperation getJourney__GetLocalEndTime();

	/**
	 * Returns the meta object for class '{@link scenario.schedule.events.PortVisit <em>Port Visit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Port Visit</em>'.
	 * @see scenario.schedule.events.PortVisit
	 * @generated
	 */
	EClass getPortVisit();

	/**
	 * Returns the meta object for the reference '{@link scenario.schedule.events.PortVisit#getPort <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Port</em>'.
	 * @see scenario.schedule.events.PortVisit#getPort()
	 * @see #getPortVisit()
	 * @generated
	 */
	EReference getPortVisit_Port();

	/**
	 * Returns the meta object for the '{@link scenario.schedule.events.PortVisit#getLocalStartTime() <em>Get Local Start Time</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Local Start Time</em>' operation.
	 * @see scenario.schedule.events.PortVisit#getLocalStartTime()
	 * @generated
	 */
	EOperation getPortVisit__GetLocalStartTime();

	/**
	 * Returns the meta object for the '{@link scenario.schedule.events.PortVisit#getLocalEndTime() <em>Get Local End Time</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Local End Time</em>' operation.
	 * @see scenario.schedule.events.PortVisit#getLocalEndTime()
	 * @generated
	 */
	EOperation getPortVisit__GetLocalEndTime();

	/**
	 * Returns the meta object for class '{@link scenario.schedule.events.SlotVisit <em>Slot Visit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Slot Visit</em>'.
	 * @see scenario.schedule.events.SlotVisit
	 * @generated
	 */
	EClass getSlotVisit();

	/**
	 * Returns the meta object for the reference '{@link scenario.schedule.events.SlotVisit#getSlot <em>Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Slot</em>'.
	 * @see scenario.schedule.events.SlotVisit#getSlot()
	 * @see #getSlotVisit()
	 * @generated
	 */
	EReference getSlotVisit_Slot();

	/**
	 * Returns the meta object for the reference '{@link scenario.schedule.events.SlotVisit#getCargoAllocation <em>Cargo Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Cargo Allocation</em>'.
	 * @see scenario.schedule.events.SlotVisit#getCargoAllocation()
	 * @see #getSlotVisit()
	 * @generated
	 */
	EReference getSlotVisit_CargoAllocation();

	/**
	 * Returns the meta object for class '{@link scenario.schedule.events.CharterOutVisit <em>Charter Out Visit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Charter Out Visit</em>'.
	 * @see scenario.schedule.events.CharterOutVisit
	 * @generated
	 */
	EClass getCharterOutVisit();

	/**
	 * Returns the meta object for the reference '{@link scenario.schedule.events.CharterOutVisit#getCharterOut <em>Charter Out</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Charter Out</em>'.
	 * @see scenario.schedule.events.CharterOutVisit#getCharterOut()
	 * @see #getCharterOutVisit()
	 * @generated
	 */
	EReference getCharterOutVisit_CharterOut();

	/**
	 * Returns the meta object for the reference '{@link scenario.schedule.events.CharterOutVisit#getRevenue <em>Revenue</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Revenue</em>'.
	 * @see scenario.schedule.events.CharterOutVisit#getRevenue()
	 * @see #getCharterOutVisit()
	 * @generated
	 */
	EReference getCharterOutVisit_Revenue();

	/**
	 * Returns the meta object for enum '{@link scenario.schedule.events.FuelUnit <em>Fuel Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Fuel Unit</em>'.
	 * @see scenario.schedule.events.FuelUnit
	 * @generated
	 */
	EEnum getFuelUnit();

	/**
	 * Returns the meta object for enum '{@link scenario.schedule.events.FuelType <em>Fuel Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Fuel Type</em>'.
	 * @see scenario.schedule.events.FuelType
	 * @generated
	 */
	EEnum getFuelType();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	EventsFactory getEventsFactory();

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
		 * The meta object literal for the '{@link scenario.schedule.events.impl.FuelMixtureImpl <em>Fuel Mixture</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.schedule.events.impl.FuelMixtureImpl
		 * @see scenario.schedule.events.impl.EventsPackageImpl#getFuelMixture()
		 * @generated
		 */
		EClass FUEL_MIXTURE = eINSTANCE.getFuelMixture();

		/**
		 * The meta object literal for the '<em><b>Fuel Usage</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FUEL_MIXTURE__FUEL_USAGE = eINSTANCE.getFuelMixture_FuelUsage();

		/**
		 * The meta object literal for the '<em><b>Get Total Fuel Cost</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation FUEL_MIXTURE___GET_TOTAL_FUEL_COST = eINSTANCE.getFuelMixture__GetTotalFuelCost();

		/**
		 * The meta object literal for the '{@link scenario.schedule.events.impl.FuelQuantityImpl <em>Fuel Quantity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.schedule.events.impl.FuelQuantityImpl
		 * @see scenario.schedule.events.impl.EventsPackageImpl#getFuelQuantity()
		 * @generated
		 */
		EClass FUEL_QUANTITY = eINSTANCE.getFuelQuantity();

		/**
		 * The meta object literal for the '<em><b>Fuel Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FUEL_QUANTITY__FUEL_TYPE = eINSTANCE.getFuelQuantity_FuelType();

		/**
		 * The meta object literal for the '<em><b>Quantity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FUEL_QUANTITY__QUANTITY = eINSTANCE.getFuelQuantity_Quantity();

		/**
		 * The meta object literal for the '<em><b>Unit Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FUEL_QUANTITY__UNIT_PRICE = eINSTANCE.getFuelQuantity_UnitPrice();

		/**
		 * The meta object literal for the '<em><b>Total Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FUEL_QUANTITY__TOTAL_PRICE = eINSTANCE.getFuelQuantity_TotalPrice();

		/**
		 * The meta object literal for the '<em><b>Fuel Unit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FUEL_QUANTITY__FUEL_UNIT = eINSTANCE.getFuelQuantity_FuelUnit();

		/**
		 * The meta object literal for the '{@link scenario.schedule.events.impl.ScheduledEventImpl <em>Scheduled Event</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.schedule.events.impl.ScheduledEventImpl
		 * @see scenario.schedule.events.impl.EventsPackageImpl#getScheduledEvent()
		 * @generated
		 */
		EClass SCHEDULED_EVENT = eINSTANCE.getScheduledEvent();

		/**
		 * The meta object literal for the '<em><b>Start Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCHEDULED_EVENT__START_TIME = eINSTANCE.getScheduledEvent_StartTime();

		/**
		 * The meta object literal for the '<em><b>End Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCHEDULED_EVENT__END_TIME = eINSTANCE.getScheduledEvent_EndTime();

		/**
		 * The meta object literal for the '<em><b>Get Event Duration</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SCHEDULED_EVENT___GET_EVENT_DURATION = eINSTANCE.getScheduledEvent__GetEventDuration();

		/**
		 * The meta object literal for the '<em><b>Get Hire Cost</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SCHEDULED_EVENT___GET_HIRE_COST = eINSTANCE.getScheduledEvent__GetHireCost();

		/**
		 * The meta object literal for the '<em><b>Get Local Start Time</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SCHEDULED_EVENT___GET_LOCAL_START_TIME = eINSTANCE.getScheduledEvent__GetLocalStartTime();

		/**
		 * The meta object literal for the '<em><b>Get Local End Time</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SCHEDULED_EVENT___GET_LOCAL_END_TIME = eINSTANCE.getScheduledEvent__GetLocalEndTime();

		/**
		 * The meta object literal for the '{@link scenario.schedule.events.impl.IdleImpl <em>Idle</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.schedule.events.impl.IdleImpl
		 * @see scenario.schedule.events.impl.EventsPackageImpl#getIdle()
		 * @generated
		 */
		EClass IDLE = eINSTANCE.getIdle();

		/**
		 * The meta object literal for the '<em><b>Vessel State</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IDLE__VESSEL_STATE = eINSTANCE.getIdle_VesselState();

		/**
		 * The meta object literal for the '<em><b>Get Total Cost</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation IDLE___GET_TOTAL_COST = eINSTANCE.getIdle__GetTotalCost();

		/**
		 * The meta object literal for the '{@link scenario.schedule.events.impl.JourneyImpl <em>Journey</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.schedule.events.impl.JourneyImpl
		 * @see scenario.schedule.events.impl.EventsPackageImpl#getJourney()
		 * @generated
		 */
		EClass JOURNEY = eINSTANCE.getJourney();

		/**
		 * The meta object literal for the '<em><b>To Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference JOURNEY__TO_PORT = eINSTANCE.getJourney_ToPort();

		/**
		 * The meta object literal for the '<em><b>Vessel State</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JOURNEY__VESSEL_STATE = eINSTANCE.getJourney_VesselState();

		/**
		 * The meta object literal for the '<em><b>Route</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JOURNEY__ROUTE = eINSTANCE.getJourney_Route();

		/**
		 * The meta object literal for the '<em><b>Speed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JOURNEY__SPEED = eINSTANCE.getJourney_Speed();

		/**
		 * The meta object literal for the '<em><b>Distance</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JOURNEY__DISTANCE = eINSTANCE.getJourney_Distance();

		/**
		 * The meta object literal for the '<em><b>From Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference JOURNEY__FROM_PORT = eINSTANCE.getJourney_FromPort();

		/**
		 * The meta object literal for the '<em><b>Route Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JOURNEY__ROUTE_COST = eINSTANCE.getJourney_RouteCost();

		/**
		 * The meta object literal for the '<em><b>Get Total Cost</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation JOURNEY___GET_TOTAL_COST = eINSTANCE.getJourney__GetTotalCost();

		/**
		 * The meta object literal for the '<em><b>Get Local Start Time</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation JOURNEY___GET_LOCAL_START_TIME = eINSTANCE.getJourney__GetLocalStartTime();

		/**
		 * The meta object literal for the '<em><b>Get Local End Time</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation JOURNEY___GET_LOCAL_END_TIME = eINSTANCE.getJourney__GetLocalEndTime();

		/**
		 * The meta object literal for the '{@link scenario.schedule.events.impl.PortVisitImpl <em>Port Visit</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.schedule.events.impl.PortVisitImpl
		 * @see scenario.schedule.events.impl.EventsPackageImpl#getPortVisit()
		 * @generated
		 */
		EClass PORT_VISIT = eINSTANCE.getPortVisit();

		/**
		 * The meta object literal for the '<em><b>Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PORT_VISIT__PORT = eINSTANCE.getPortVisit_Port();

		/**
		 * The meta object literal for the '<em><b>Get Local Start Time</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation PORT_VISIT___GET_LOCAL_START_TIME = eINSTANCE.getPortVisit__GetLocalStartTime();

		/**
		 * The meta object literal for the '<em><b>Get Local End Time</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation PORT_VISIT___GET_LOCAL_END_TIME = eINSTANCE.getPortVisit__GetLocalEndTime();

		/**
		 * The meta object literal for the '{@link scenario.schedule.events.impl.SlotVisitImpl <em>Slot Visit</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.schedule.events.impl.SlotVisitImpl
		 * @see scenario.schedule.events.impl.EventsPackageImpl#getSlotVisit()
		 * @generated
		 */
		EClass SLOT_VISIT = eINSTANCE.getSlotVisit();

		/**
		 * The meta object literal for the '<em><b>Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT_VISIT__SLOT = eINSTANCE.getSlotVisit_Slot();

		/**
		 * The meta object literal for the '<em><b>Cargo Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT_VISIT__CARGO_ALLOCATION = eINSTANCE.getSlotVisit_CargoAllocation();

		/**
		 * The meta object literal for the '{@link scenario.schedule.events.impl.CharterOutVisitImpl <em>Charter Out Visit</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.schedule.events.impl.CharterOutVisitImpl
		 * @see scenario.schedule.events.impl.EventsPackageImpl#getCharterOutVisit()
		 * @generated
		 */
		EClass CHARTER_OUT_VISIT = eINSTANCE.getCharterOutVisit();

		/**
		 * The meta object literal for the '<em><b>Charter Out</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHARTER_OUT_VISIT__CHARTER_OUT = eINSTANCE.getCharterOutVisit_CharterOut();

		/**
		 * The meta object literal for the '<em><b>Revenue</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHARTER_OUT_VISIT__REVENUE = eINSTANCE.getCharterOutVisit_Revenue();

		/**
		 * The meta object literal for the '{@link scenario.schedule.events.FuelUnit <em>Fuel Unit</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.schedule.events.FuelUnit
		 * @see scenario.schedule.events.impl.EventsPackageImpl#getFuelUnit()
		 * @generated
		 */
		EEnum FUEL_UNIT = eINSTANCE.getFuelUnit();

		/**
		 * The meta object literal for the '{@link scenario.schedule.events.FuelType <em>Fuel Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.schedule.events.FuelType
		 * @see scenario.schedule.events.impl.EventsPackageImpl#getFuelType()
		 * @generated
		 */
		EEnum FUEL_TYPE = eINSTANCE.getFuelType();

	}

} //EventsPackage
