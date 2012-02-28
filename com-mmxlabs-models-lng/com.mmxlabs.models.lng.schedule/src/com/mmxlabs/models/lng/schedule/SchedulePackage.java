/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.schedule;

import com.mmxlabs.models.mmxcore.MMXCorePackage;

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
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.schedule.ScheduleFactory
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
	String eNS_URI = "http://www.mmxlabs.com/models/lng/schedule/1/";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "lng.schedule";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	SchedulePackage eINSTANCE = com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl.init();

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.ScheduleModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.ScheduleModelImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getScheduleModel()
	 * @generated
	 */
	int SCHEDULE_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_MODEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_MODEL__PROXIES = MMXCorePackage.UUID_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_MODEL__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_MODEL__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Initial Schedule</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_MODEL__INITIAL_SCHEDULE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Optimised Schedule</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_MODEL__OPTIMISED_SCHEDULE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.ScheduleImpl <em>Schedule</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.ScheduleImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getSchedule()
	 * @generated
	 */
	int SCHEDULE = 1;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE__PROXIES = MMXCorePackage.MMX_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Complete</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE__COMPLETE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Sequences</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE__SEQUENCES = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Unscheduled Cargos</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE__UNSCHEDULED_CARGOS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Allocations</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE__ALLOCATIONS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Slot Allocations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE__SLOT_ALLOCATIONS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Schedule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.SequenceImpl <em>Sequence</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.SequenceImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getSequence()
	 * @generated
	 */
	int SEQUENCE = 2;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE__PROXIES = MMXCorePackage.MMX_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Events</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE__EVENTS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE__VESSEL = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Vessel Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE__VESSEL_CLASS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Sequence</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.EventImpl <em>Event</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.EventImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getEvent()
	 * @generated
	 */
	int EVENT = 3;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT__PROXIES = MMXCorePackage.MMX_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT__START = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT__END = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Location</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT__LOCATION = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.SlotVisitImpl <em>Slot Visit</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.SlotVisitImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getSlotVisit()
	 * @generated
	 */
	int SLOT_VISIT = 4;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT__EXTENSIONS = EVENT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT__PROXIES = EVENT__PROXIES;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT__START = EVENT__START;

	/**
	 * The feature id for the '<em><b>End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT__END = EVENT__END;

	/**
	 * The feature id for the '<em><b>Location</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT__LOCATION = EVENT__LOCATION;

	/**
	 * The feature id for the '<em><b>Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT__SLOT = EVENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Slot Visit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT_FEATURE_COUNT = EVENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.VesselEventVisitImpl <em>Vessel Event Visit</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.VesselEventVisitImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getVesselEventVisit()
	 * @generated
	 */
	int VESSEL_EVENT_VISIT = 5;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT__EXTENSIONS = EVENT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT__PROXIES = EVENT__PROXIES;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT__START = EVENT__START;

	/**
	 * The feature id for the '<em><b>End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT__END = EVENT__END;

	/**
	 * The feature id for the '<em><b>Location</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT__LOCATION = EVENT__LOCATION;

	/**
	 * The feature id for the '<em><b>Vessel Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT__VESSEL_EVENT = EVENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Vessel Event Visit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT_FEATURE_COUNT = EVENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.JourneyImpl <em>Journey</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.JourneyImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getJourney()
	 * @generated
	 */
	int JOURNEY = 6;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__EXTENSIONS = EVENT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__PROXIES = EVENT__PROXIES;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__START = EVENT__START;

	/**
	 * The feature id for the '<em><b>End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__END = EVENT__END;

	/**
	 * The feature id for the '<em><b>Location</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__LOCATION = EVENT__LOCATION;

	/**
	 * The feature id for the '<em><b>Fuels</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__FUELS = EVENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Destination</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__DESTINATION = EVENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Laden</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__LADEN = EVENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Route</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__ROUTE = EVENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Toll</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__TOLL = EVENT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Journey</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY_FEATURE_COUNT = EVENT_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.IdleImpl <em>Idle</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.IdleImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getIdle()
	 * @generated
	 */
	int IDLE = 7;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE__EXTENSIONS = EVENT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE__PROXIES = EVENT__PROXIES;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE__START = EVENT__START;

	/**
	 * The feature id for the '<em><b>End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE__END = EVENT__END;

	/**
	 * The feature id for the '<em><b>Location</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE__LOCATION = EVENT__LOCATION;

	/**
	 * The feature id for the '<em><b>Fuels</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE__FUELS = EVENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Idle</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE_FEATURE_COUNT = EVENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.UnscheduledCargoImpl <em>Unscheduled Cargo</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.UnscheduledCargoImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getUnscheduledCargo()
	 * @generated
	 */
	int UNSCHEDULED_CARGO = 8;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNSCHEDULED_CARGO__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNSCHEDULED_CARGO__PROXIES = MMXCorePackage.MMX_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Load</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNSCHEDULED_CARGO__LOAD = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Discharge</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNSCHEDULED_CARGO__DISCHARGE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Unscheduled Cargo</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNSCHEDULED_CARGO_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.FuelUsageImpl <em>Fuel Usage</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.FuelUsageImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getFuelUsage()
	 * @generated
	 */
	int FUEL_USAGE = 9;

	/**
	 * The feature id for the '<em><b>Fuels</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_USAGE__FUELS = 0;

	/**
	 * The number of structural features of the '<em>Fuel Usage</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_USAGE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.FuelQuantityImpl <em>Fuel Quantity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.FuelQuantityImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getFuelQuantity()
	 * @generated
	 */
	int FUEL_QUANTITY = 10;

	/**
	 * The feature id for the '<em><b>Fuel Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_QUANTITY__FUEL_NAME = 0;

	/**
	 * The feature id for the '<em><b>Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_QUANTITY__COST = 1;

	/**
	 * The feature id for the '<em><b>Amounts</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_QUANTITY__AMOUNTS = 2;

	/**
	 * The number of structural features of the '<em>Fuel Quantity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_QUANTITY_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.CooldownImpl <em>Cooldown</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.CooldownImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getCooldown()
	 * @generated
	 */
	int COOLDOWN = 11;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN__EXTENSIONS = EVENT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN__PROXIES = EVENT__PROXIES;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN__START = EVENT__START;

	/**
	 * The feature id for the '<em><b>End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN__END = EVENT__END;

	/**
	 * The feature id for the '<em><b>Location</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN__LOCATION = EVENT__LOCATION;

	/**
	 * The feature id for the '<em><b>Fuels</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN__FUELS = EVENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Cooldown</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN_FEATURE_COUNT = EVENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl <em>Cargo Allocation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getCargoAllocation()
	 * @generated
	 */
	int CARGO_ALLOCATION = 12;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__PROXIES = MMXCorePackage.MMX_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Load Visit</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__LOAD_VISIT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Discharge Visit</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__DISCHARGE_VISIT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Load Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__LOAD_VOLUME = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Discharge Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__DISCHARGE_VOLUME = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Input Cargo</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__INPUT_CARGO = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Cargo Allocation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.SlotAllocationImpl <em>Slot Allocation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.SlotAllocationImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getSlotAllocation()
	 * @generated
	 */
	int SLOT_ALLOCATION = 13;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION__PROXIES = MMXCorePackage.MMX_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION__SLOT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Spot Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION__SPOT_MARKET = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Slot Allocation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.FuelAmountImpl <em>Fuel Amount</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.FuelAmountImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getFuelAmount()
	 * @generated
	 */
	int FUEL_AMOUNT = 14;

	/**
	 * The feature id for the '<em><b>Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_AMOUNT__UNIT = 0;

	/**
	 * The feature id for the '<em><b>Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_AMOUNT__QUANTITY = 1;

	/**
	 * The feature id for the '<em><b>Unit Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_AMOUNT__UNIT_PRICE = 2;

	/**
	 * The number of structural features of the '<em>Fuel Amount</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_AMOUNT_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.FuelUnit <em>Fuel Unit</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.FuelUnit
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getFuelUnit()
	 * @generated
	 */
	int FUEL_UNIT = 15;


	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.ScheduleModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see com.mmxlabs.models.lng.schedule.ScheduleModel
	 * @generated
	 */
	EClass getScheduleModel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.schedule.ScheduleModel#getInitialSchedule <em>Initial Schedule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Initial Schedule</em>'.
	 * @see com.mmxlabs.models.lng.schedule.ScheduleModel#getInitialSchedule()
	 * @see #getScheduleModel()
	 * @generated
	 */
	EReference getScheduleModel_InitialSchedule();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.schedule.ScheduleModel#getOptimisedSchedule <em>Optimised Schedule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Optimised Schedule</em>'.
	 * @see com.mmxlabs.models.lng.schedule.ScheduleModel#getOptimisedSchedule()
	 * @see #getScheduleModel()
	 * @generated
	 */
	EReference getScheduleModel_OptimisedSchedule();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.Schedule <em>Schedule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Schedule</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Schedule
	 * @generated
	 */
	EClass getSchedule();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.Schedule#isComplete <em>Complete</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Complete</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Schedule#isComplete()
	 * @see #getSchedule()
	 * @generated
	 */
	EAttribute getSchedule_Complete();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.schedule.Schedule#getSequences <em>Sequences</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Sequences</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Schedule#getSequences()
	 * @see #getSchedule()
	 * @generated
	 */
	EReference getSchedule_Sequences();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.schedule.Schedule#getUnscheduledCargos <em>Unscheduled Cargos</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Unscheduled Cargos</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Schedule#getUnscheduledCargos()
	 * @see #getSchedule()
	 * @generated
	 */
	EReference getSchedule_UnscheduledCargos();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.schedule.Schedule#getAllocations <em>Allocations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Allocations</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Schedule#getAllocations()
	 * @see #getSchedule()
	 * @generated
	 */
	EReference getSchedule_Allocations();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.schedule.Schedule#getSlotAllocations <em>Slot Allocations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Slot Allocations</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Schedule#getSlotAllocations()
	 * @see #getSchedule()
	 * @generated
	 */
	EReference getSchedule_SlotAllocations();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.Sequence <em>Sequence</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Sequence</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Sequence
	 * @generated
	 */
	EClass getSequence();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.schedule.Sequence#getEvents <em>Events</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Events</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Sequence#getEvents()
	 * @see #getSequence()
	 * @generated
	 */
	EReference getSequence_Events();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.Sequence#getVessel <em>Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Sequence#getVessel()
	 * @see #getSequence()
	 * @generated
	 */
	EReference getSequence_Vessel();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.Sequence#getVesselClass <em>Vessel Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel Class</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Sequence#getVesselClass()
	 * @see #getSequence()
	 * @generated
	 */
	EReference getSequence_VesselClass();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.Event <em>Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Event</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Event
	 * @generated
	 */
	EClass getEvent();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.Event#getStart <em>Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Event#getStart()
	 * @see #getEvent()
	 * @generated
	 */
	EAttribute getEvent_Start();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.Event#getEnd <em>End</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Event#getEnd()
	 * @see #getEvent()
	 * @generated
	 */
	EAttribute getEvent_End();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.Event#getLocation <em>Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Location</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Event#getLocation()
	 * @see #getEvent()
	 * @generated
	 */
	EReference getEvent_Location();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.SlotVisit <em>Slot Visit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Slot Visit</em>'.
	 * @see com.mmxlabs.models.lng.schedule.SlotVisit
	 * @generated
	 */
	EClass getSlotVisit();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.SlotVisit#getSlot <em>Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Slot</em>'.
	 * @see com.mmxlabs.models.lng.schedule.SlotVisit#getSlot()
	 * @see #getSlotVisit()
	 * @generated
	 */
	EReference getSlotVisit_Slot();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.VesselEventVisit <em>Vessel Event Visit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vessel Event Visit</em>'.
	 * @see com.mmxlabs.models.lng.schedule.VesselEventVisit
	 * @generated
	 */
	EClass getVesselEventVisit();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.VesselEventVisit#getVesselEvent <em>Vessel Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel Event</em>'.
	 * @see com.mmxlabs.models.lng.schedule.VesselEventVisit#getVesselEvent()
	 * @see #getVesselEventVisit()
	 * @generated
	 */
	EReference getVesselEventVisit_VesselEvent();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.Journey <em>Journey</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Journey</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Journey
	 * @generated
	 */
	EClass getJourney();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.Journey#getDestination <em>Destination</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Destination</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Journey#getDestination()
	 * @see #getJourney()
	 * @generated
	 */
	EReference getJourney_Destination();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.Journey#isLaden <em>Laden</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Laden</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Journey#isLaden()
	 * @see #getJourney()
	 * @generated
	 */
	EAttribute getJourney_Laden();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.Journey#getRoute <em>Route</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Route</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Journey#getRoute()
	 * @see #getJourney()
	 * @generated
	 */
	EAttribute getJourney_Route();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.Journey#getToll <em>Toll</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Toll</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Journey#getToll()
	 * @see #getJourney()
	 * @generated
	 */
	EAttribute getJourney_Toll();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.Idle <em>Idle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Idle</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Idle
	 * @generated
	 */
	EClass getIdle();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.UnscheduledCargo <em>Unscheduled Cargo</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Unscheduled Cargo</em>'.
	 * @see com.mmxlabs.models.lng.schedule.UnscheduledCargo
	 * @generated
	 */
	EClass getUnscheduledCargo();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.UnscheduledCargo#getLoad <em>Load</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Load</em>'.
	 * @see com.mmxlabs.models.lng.schedule.UnscheduledCargo#getLoad()
	 * @see #getUnscheduledCargo()
	 * @generated
	 */
	EReference getUnscheduledCargo_Load();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.UnscheduledCargo#getDischarge <em>Discharge</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Discharge</em>'.
	 * @see com.mmxlabs.models.lng.schedule.UnscheduledCargo#getDischarge()
	 * @see #getUnscheduledCargo()
	 * @generated
	 */
	EReference getUnscheduledCargo_Discharge();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.FuelUsage <em>Fuel Usage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Fuel Usage</em>'.
	 * @see com.mmxlabs.models.lng.schedule.FuelUsage
	 * @generated
	 */
	EClass getFuelUsage();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.schedule.FuelUsage#getFuels <em>Fuels</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Fuels</em>'.
	 * @see com.mmxlabs.models.lng.schedule.FuelUsage#getFuels()
	 * @see #getFuelUsage()
	 * @generated
	 */
	EReference getFuelUsage_Fuels();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.FuelQuantity <em>Fuel Quantity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Fuel Quantity</em>'.
	 * @see com.mmxlabs.models.lng.schedule.FuelQuantity
	 * @generated
	 */
	EClass getFuelQuantity();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.FuelQuantity#getFuelName <em>Fuel Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fuel Name</em>'.
	 * @see com.mmxlabs.models.lng.schedule.FuelQuantity#getFuelName()
	 * @see #getFuelQuantity()
	 * @generated
	 */
	EAttribute getFuelQuantity_FuelName();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.FuelQuantity#getCost <em>Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cost</em>'.
	 * @see com.mmxlabs.models.lng.schedule.FuelQuantity#getCost()
	 * @see #getFuelQuantity()
	 * @generated
	 */
	EAttribute getFuelQuantity_Cost();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.schedule.FuelQuantity#getAmounts <em>Amounts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Amounts</em>'.
	 * @see com.mmxlabs.models.lng.schedule.FuelQuantity#getAmounts()
	 * @see #getFuelQuantity()
	 * @generated
	 */
	EReference getFuelQuantity_Amounts();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.Cooldown <em>Cooldown</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cooldown</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Cooldown
	 * @generated
	 */
	EClass getCooldown();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.CargoAllocation <em>Cargo Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cargo Allocation</em>'.
	 * @see com.mmxlabs.models.lng.schedule.CargoAllocation
	 * @generated
	 */
	EClass getCargoAllocation();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getLoadVisit <em>Load Visit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Load Visit</em>'.
	 * @see com.mmxlabs.models.lng.schedule.CargoAllocation#getLoadVisit()
	 * @see #getCargoAllocation()
	 * @generated
	 */
	EReference getCargoAllocation_LoadVisit();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getDischargeVisit <em>Discharge Visit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Discharge Visit</em>'.
	 * @see com.mmxlabs.models.lng.schedule.CargoAllocation#getDischargeVisit()
	 * @see #getCargoAllocation()
	 * @generated
	 */
	EReference getCargoAllocation_DischargeVisit();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getLoadVolume <em>Load Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Load Volume</em>'.
	 * @see com.mmxlabs.models.lng.schedule.CargoAllocation#getLoadVolume()
	 * @see #getCargoAllocation()
	 * @generated
	 */
	EAttribute getCargoAllocation_LoadVolume();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getDischargeVolume <em>Discharge Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Discharge Volume</em>'.
	 * @see com.mmxlabs.models.lng.schedule.CargoAllocation#getDischargeVolume()
	 * @see #getCargoAllocation()
	 * @generated
	 */
	EAttribute getCargoAllocation_DischargeVolume();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getInputCargo <em>Input Cargo</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Input Cargo</em>'.
	 * @see com.mmxlabs.models.lng.schedule.CargoAllocation#getInputCargo()
	 * @see #getCargoAllocation()
	 * @generated
	 */
	EReference getCargoAllocation_InputCargo();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.SlotAllocation <em>Slot Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Slot Allocation</em>'.
	 * @see com.mmxlabs.models.lng.schedule.SlotAllocation
	 * @generated
	 */
	EClass getSlotAllocation();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getSlot <em>Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Slot</em>'.
	 * @see com.mmxlabs.models.lng.schedule.SlotAllocation#getSlot()
	 * @see #getSlotAllocation()
	 * @generated
	 */
	EReference getSlotAllocation_Slot();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getSpotMarket <em>Spot Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Spot Market</em>'.
	 * @see com.mmxlabs.models.lng.schedule.SlotAllocation#getSpotMarket()
	 * @see #getSlotAllocation()
	 * @generated
	 */
	EReference getSlotAllocation_SpotMarket();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.FuelAmount <em>Fuel Amount</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Fuel Amount</em>'.
	 * @see com.mmxlabs.models.lng.schedule.FuelAmount
	 * @generated
	 */
	EClass getFuelAmount();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.FuelAmount#getUnit <em>Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Unit</em>'.
	 * @see com.mmxlabs.models.lng.schedule.FuelAmount#getUnit()
	 * @see #getFuelAmount()
	 * @generated
	 */
	EAttribute getFuelAmount_Unit();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.FuelAmount#getQuantity <em>Quantity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Quantity</em>'.
	 * @see com.mmxlabs.models.lng.schedule.FuelAmount#getQuantity()
	 * @see #getFuelAmount()
	 * @generated
	 */
	EAttribute getFuelAmount_Quantity();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.FuelAmount#getUnitPrice <em>Unit Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Unit Price</em>'.
	 * @see com.mmxlabs.models.lng.schedule.FuelAmount#getUnitPrice()
	 * @see #getFuelAmount()
	 * @generated
	 */
	EAttribute getFuelAmount_UnitPrice();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.schedule.FuelUnit <em>Fuel Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Fuel Unit</em>'.
	 * @see com.mmxlabs.models.lng.schedule.FuelUnit
	 * @generated
	 */
	EEnum getFuelUnit();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.ScheduleModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.ScheduleModelImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getScheduleModel()
		 * @generated
		 */
		EClass SCHEDULE_MODEL = eINSTANCE.getScheduleModel();

		/**
		 * The meta object literal for the '<em><b>Initial Schedule</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEDULE_MODEL__INITIAL_SCHEDULE = eINSTANCE.getScheduleModel_InitialSchedule();

		/**
		 * The meta object literal for the '<em><b>Optimised Schedule</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEDULE_MODEL__OPTIMISED_SCHEDULE = eINSTANCE.getScheduleModel_OptimisedSchedule();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.ScheduleImpl <em>Schedule</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.ScheduleImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getSchedule()
		 * @generated
		 */
		EClass SCHEDULE = eINSTANCE.getSchedule();

		/**
		 * The meta object literal for the '<em><b>Complete</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCHEDULE__COMPLETE = eINSTANCE.getSchedule_Complete();

		/**
		 * The meta object literal for the '<em><b>Sequences</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEDULE__SEQUENCES = eINSTANCE.getSchedule_Sequences();

		/**
		 * The meta object literal for the '<em><b>Unscheduled Cargos</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEDULE__UNSCHEDULED_CARGOS = eINSTANCE.getSchedule_UnscheduledCargos();

		/**
		 * The meta object literal for the '<em><b>Allocations</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEDULE__ALLOCATIONS = eINSTANCE.getSchedule_Allocations();

		/**
		 * The meta object literal for the '<em><b>Slot Allocations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEDULE__SLOT_ALLOCATIONS = eINSTANCE.getSchedule_SlotAllocations();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.SequenceImpl <em>Sequence</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.SequenceImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getSequence()
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
		 * The meta object literal for the '<em><b>Vessel Class</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SEQUENCE__VESSEL_CLASS = eINSTANCE.getSequence_VesselClass();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.EventImpl <em>Event</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.EventImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getEvent()
		 * @generated
		 */
		EClass EVENT = eINSTANCE.getEvent();

		/**
		 * The meta object literal for the '<em><b>Start</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EVENT__START = eINSTANCE.getEvent_Start();

		/**
		 * The meta object literal for the '<em><b>End</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EVENT__END = eINSTANCE.getEvent_End();

		/**
		 * The meta object literal for the '<em><b>Location</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EVENT__LOCATION = eINSTANCE.getEvent_Location();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.SlotVisitImpl <em>Slot Visit</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.SlotVisitImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getSlotVisit()
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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.VesselEventVisitImpl <em>Vessel Event Visit</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.VesselEventVisitImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getVesselEventVisit()
		 * @generated
		 */
		EClass VESSEL_EVENT_VISIT = eINSTANCE.getVesselEventVisit();

		/**
		 * The meta object literal for the '<em><b>Vessel Event</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_EVENT_VISIT__VESSEL_EVENT = eINSTANCE.getVesselEventVisit_VesselEvent();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.JourneyImpl <em>Journey</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.JourneyImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getJourney()
		 * @generated
		 */
		EClass JOURNEY = eINSTANCE.getJourney();

		/**
		 * The meta object literal for the '<em><b>Destination</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference JOURNEY__DESTINATION = eINSTANCE.getJourney_Destination();

		/**
		 * The meta object literal for the '<em><b>Laden</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JOURNEY__LADEN = eINSTANCE.getJourney_Laden();

		/**
		 * The meta object literal for the '<em><b>Route</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JOURNEY__ROUTE = eINSTANCE.getJourney_Route();

		/**
		 * The meta object literal for the '<em><b>Toll</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JOURNEY__TOLL = eINSTANCE.getJourney_Toll();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.IdleImpl <em>Idle</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.IdleImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getIdle()
		 * @generated
		 */
		EClass IDLE = eINSTANCE.getIdle();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.UnscheduledCargoImpl <em>Unscheduled Cargo</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.UnscheduledCargoImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getUnscheduledCargo()
		 * @generated
		 */
		EClass UNSCHEDULED_CARGO = eINSTANCE.getUnscheduledCargo();

		/**
		 * The meta object literal for the '<em><b>Load</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UNSCHEDULED_CARGO__LOAD = eINSTANCE.getUnscheduledCargo_Load();

		/**
		 * The meta object literal for the '<em><b>Discharge</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UNSCHEDULED_CARGO__DISCHARGE = eINSTANCE.getUnscheduledCargo_Discharge();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.FuelUsageImpl <em>Fuel Usage</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.FuelUsageImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getFuelUsage()
		 * @generated
		 */
		EClass FUEL_USAGE = eINSTANCE.getFuelUsage();

		/**
		 * The meta object literal for the '<em><b>Fuels</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FUEL_USAGE__FUELS = eINSTANCE.getFuelUsage_Fuels();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.FuelQuantityImpl <em>Fuel Quantity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.FuelQuantityImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getFuelQuantity()
		 * @generated
		 */
		EClass FUEL_QUANTITY = eINSTANCE.getFuelQuantity();

		/**
		 * The meta object literal for the '<em><b>Fuel Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FUEL_QUANTITY__FUEL_NAME = eINSTANCE.getFuelQuantity_FuelName();

		/**
		 * The meta object literal for the '<em><b>Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FUEL_QUANTITY__COST = eINSTANCE.getFuelQuantity_Cost();

		/**
		 * The meta object literal for the '<em><b>Amounts</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FUEL_QUANTITY__AMOUNTS = eINSTANCE.getFuelQuantity_Amounts();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.CooldownImpl <em>Cooldown</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.CooldownImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getCooldown()
		 * @generated
		 */
		EClass COOLDOWN = eINSTANCE.getCooldown();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl <em>Cargo Allocation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getCargoAllocation()
		 * @generated
		 */
		EClass CARGO_ALLOCATION = eINSTANCE.getCargoAllocation();

		/**
		 * The meta object literal for the '<em><b>Load Visit</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_ALLOCATION__LOAD_VISIT = eINSTANCE.getCargoAllocation_LoadVisit();

		/**
		 * The meta object literal for the '<em><b>Discharge Visit</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_ALLOCATION__DISCHARGE_VISIT = eINSTANCE.getCargoAllocation_DischargeVisit();

		/**
		 * The meta object literal for the '<em><b>Load Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO_ALLOCATION__LOAD_VOLUME = eINSTANCE.getCargoAllocation_LoadVolume();

		/**
		 * The meta object literal for the '<em><b>Discharge Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO_ALLOCATION__DISCHARGE_VOLUME = eINSTANCE.getCargoAllocation_DischargeVolume();

		/**
		 * The meta object literal for the '<em><b>Input Cargo</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_ALLOCATION__INPUT_CARGO = eINSTANCE.getCargoAllocation_InputCargo();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.SlotAllocationImpl <em>Slot Allocation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.SlotAllocationImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getSlotAllocation()
		 * @generated
		 */
		EClass SLOT_ALLOCATION = eINSTANCE.getSlotAllocation();

		/**
		 * The meta object literal for the '<em><b>Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT_ALLOCATION__SLOT = eINSTANCE.getSlotAllocation_Slot();

		/**
		 * The meta object literal for the '<em><b>Spot Market</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT_ALLOCATION__SPOT_MARKET = eINSTANCE.getSlotAllocation_SpotMarket();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.FuelAmountImpl <em>Fuel Amount</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.FuelAmountImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getFuelAmount()
		 * @generated
		 */
		EClass FUEL_AMOUNT = eINSTANCE.getFuelAmount();

		/**
		 * The meta object literal for the '<em><b>Unit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FUEL_AMOUNT__UNIT = eINSTANCE.getFuelAmount_Unit();

		/**
		 * The meta object literal for the '<em><b>Quantity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FUEL_AMOUNT__QUANTITY = eINSTANCE.getFuelAmount_Quantity();

		/**
		 * The meta object literal for the '<em><b>Unit Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FUEL_AMOUNT__UNIT_PRICE = eINSTANCE.getFuelAmount_UnitPrice();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.FuelUnit <em>Fuel Unit</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.FuelUnit
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getFuelUnit()
		 * @generated
		 */
		EEnum FUEL_UNIT = eINSTANCE.getFuelUnit();

	}

} //SchedulePackage
