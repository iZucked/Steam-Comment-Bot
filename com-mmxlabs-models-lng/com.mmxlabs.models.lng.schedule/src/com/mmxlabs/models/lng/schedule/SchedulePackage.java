/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.mmxcore.MMXCorePackage;

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
	 * The feature id for the '<em><b>Schedule</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_MODEL__SCHEDULE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Dirty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_MODEL__DIRTY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_MODEL___MAKE_PROXIES = MMXCorePackage.UUID_OBJECT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_MODEL___RESOLVE_PROXIES__MAP = MMXCorePackage.UUID_OBJECT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_MODEL___RESTORE_PROXIES = MMXCorePackage.UUID_OBJECT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_MODEL___COLLECT_UUID_OBJECTS__MAP = MMXCorePackage.UUID_OBJECT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_MODEL___COLLECT_UUID_OBJECTS = MMXCorePackage.UUID_OBJECT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_MODEL___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_MODEL___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_MODEL___ECONTAINER_OP = MMXCorePackage.UUID_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_MODEL_OPERATION_COUNT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 0;

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
	 * The feature id for the '<em><b>Cargo Allocations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE__CARGO_ALLOCATIONS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Slot Allocations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE__SLOT_ALLOCATIONS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Fitnesses</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE__FITNESSES = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Unused Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE__UNUSED_ELEMENTS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Schedule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE___MAKE_PROXIES = MMXCorePackage.MMX_OBJECT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE___RESOLVE_PROXIES__MAP = MMXCorePackage.MMX_OBJECT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE___RESTORE_PROXIES = MMXCorePackage.MMX_OBJECT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE___COLLECT_UUID_OBJECTS__MAP = MMXCorePackage.MMX_OBJECT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE___COLLECT_UUID_OBJECTS = MMXCorePackage.MMX_OBJECT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.MMX_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.MMX_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE___ECONTAINER_OP = MMXCorePackage.MMX_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>Schedule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_OPERATION_COUNT = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 0;

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
	 * The feature id for the '<em><b>Fitnesses</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE__FITNESSES = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Daily Hire Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE__DAILY_HIRE_RATE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Spot Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE__SPOT_INDEX = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Sequence Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE__SEQUENCE_TYPE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Sequence</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE___MAKE_PROXIES = MMXCorePackage.MMX_OBJECT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE___RESOLVE_PROXIES__MAP = MMXCorePackage.MMX_OBJECT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE___RESTORE_PROXIES = MMXCorePackage.MMX_OBJECT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE___COLLECT_UUID_OBJECTS__MAP = MMXCorePackage.MMX_OBJECT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE___COLLECT_UUID_OBJECTS = MMXCorePackage.MMX_OBJECT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.MMX_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.MMX_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE___ECONTAINER_OP = MMXCorePackage.MMX_OBJECT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE___GET_NAME = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Is Spot Vessel</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE___IS_SPOT_VESSEL = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 1;

	/**
	 * The operation id for the '<em>Is Fleet Vessel</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE___IS_FLEET_VESSEL = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 2;

	/**
	 * The operation id for the '<em>Is Time Charter Vessel</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE___IS_TIME_CHARTER_VESSEL = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 3;

	/**
	 * The number of operations of the '<em>Sequence</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE_OPERATION_COUNT = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 4;

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
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT__PORT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Previous Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT__PREVIOUS_EVENT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Next Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT__NEXT_EVENT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Sequence</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT__SEQUENCE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT___MAKE_PROXIES = MMXCorePackage.MMX_OBJECT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT___RESOLVE_PROXIES__MAP = MMXCorePackage.MMX_OBJECT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT___RESTORE_PROXIES = MMXCorePackage.MMX_OBJECT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT___COLLECT_UUID_OBJECTS__MAP = MMXCorePackage.MMX_OBJECT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT___COLLECT_UUID_OBJECTS = MMXCorePackage.MMX_OBJECT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.MMX_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.MMX_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT___ECONTAINER_OP = MMXCorePackage.MMX_OBJECT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Time Zone</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT___GET_TIME_ZONE__EATTRIBUTE = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT___GET_DURATION = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Local Start</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT___GET_LOCAL_START = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 2;

	/**
	 * The operation id for the '<em>Get Local End</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT___GET_LOCAL_END = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 3;

	/**
	 * The operation id for the '<em>Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT___TYPE = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 4;

	/**
	 * The operation id for the '<em>Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT___NAME = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 5;

	/**
	 * The operation id for the '<em>Get Hire Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT___GET_HIRE_COST = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 6;

	/**
	 * The number of operations of the '<em>Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_OPERATION_COUNT = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 7;

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
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT__PORT = EVENT__PORT;

	/**
	 * The feature id for the '<em><b>Previous Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT__PREVIOUS_EVENT = EVENT__PREVIOUS_EVENT;

	/**
	 * The feature id for the '<em><b>Next Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT__NEXT_EVENT = EVENT__NEXT_EVENT;

	/**
	 * The feature id for the '<em><b>Sequence</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT__SEQUENCE = EVENT__SEQUENCE;

	/**
	 * The feature id for the '<em><b>Fuels</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT__FUELS = EVENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Port Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT__PORT_COST = EVENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Slot Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT__SLOT_ALLOCATION = EVENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Slot Visit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT_FEATURE_COUNT = EVENT_FEATURE_COUNT + 3;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT___MAKE_PROXIES = EVENT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT___RESOLVE_PROXIES__MAP = EVENT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT___RESTORE_PROXIES = EVENT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT___COLLECT_UUID_OBJECTS__MAP = EVENT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT___COLLECT_UUID_OBJECTS = EVENT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = EVENT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = EVENT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT___ECONTAINER_OP = EVENT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Time Zone</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT___GET_TIME_ZONE__EATTRIBUTE = EVENT___GET_TIME_ZONE__EATTRIBUTE;

	/**
	 * The operation id for the '<em>Get Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT___GET_DURATION = EVENT___GET_DURATION;

	/**
	 * The operation id for the '<em>Get Local Start</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT___GET_LOCAL_START = EVENT___GET_LOCAL_START;

	/**
	 * The operation id for the '<em>Get Local End</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT___GET_LOCAL_END = EVENT___GET_LOCAL_END;

	/**
	 * The operation id for the '<em>Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT___TYPE = EVENT___TYPE;

	/**
	 * The operation id for the '<em>Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT___NAME = EVENT___NAME;

	/**
	 * The operation id for the '<em>Get Hire Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT___GET_HIRE_COST = EVENT___GET_HIRE_COST;

	/**
	 * The operation id for the '<em>Get Fuel Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT___GET_FUEL_COST = EVENT_OPERATION_COUNT + 0;

	/**
	 * The number of operations of the '<em>Slot Visit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT_OPERATION_COUNT = EVENT_OPERATION_COUNT + 1;

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
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT__PORT = EVENT__PORT;

	/**
	 * The feature id for the '<em><b>Previous Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT__PREVIOUS_EVENT = EVENT__PREVIOUS_EVENT;

	/**
	 * The feature id for the '<em><b>Next Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT__NEXT_EVENT = EVENT__NEXT_EVENT;

	/**
	 * The feature id for the '<em><b>Sequence</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT__SEQUENCE = EVENT__SEQUENCE;

	/**
	 * The feature id for the '<em><b>Port Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT__PORT_COST = EVENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Extra Data</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT__EXTRA_DATA = EVENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Vessel Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT__VESSEL_EVENT = EVENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Vessel Event Visit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT_FEATURE_COUNT = EVENT_FEATURE_COUNT + 3;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT___MAKE_PROXIES = EVENT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT___RESOLVE_PROXIES__MAP = EVENT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT___RESTORE_PROXIES = EVENT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT___COLLECT_UUID_OBJECTS__MAP = EVENT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT___COLLECT_UUID_OBJECTS = EVENT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = EVENT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = EVENT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT___ECONTAINER_OP = EVENT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Time Zone</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT___GET_TIME_ZONE__EATTRIBUTE = EVENT___GET_TIME_ZONE__EATTRIBUTE;

	/**
	 * The operation id for the '<em>Get Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT___GET_DURATION = EVENT___GET_DURATION;

	/**
	 * The operation id for the '<em>Get Local Start</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT___GET_LOCAL_START = EVENT___GET_LOCAL_START;

	/**
	 * The operation id for the '<em>Get Local End</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT___GET_LOCAL_END = EVENT___GET_LOCAL_END;

	/**
	 * The operation id for the '<em>Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT___TYPE = EVENT___TYPE;

	/**
	 * The operation id for the '<em>Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT___NAME = EVENT___NAME;

	/**
	 * The operation id for the '<em>Get Hire Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT___GET_HIRE_COST = EVENT___GET_HIRE_COST;

	/**
	 * The operation id for the '<em>Get Data With Path</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT___GET_DATA_WITH_PATH__ITERABLE = EVENT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Data With Key</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT___GET_DATA_WITH_KEY__STRING = EVENT_OPERATION_COUNT + 1;

	/**
	 * The operation id for the '<em>Add Extra Data</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT___ADD_EXTRA_DATA__STRING_STRING = EVENT_OPERATION_COUNT + 2;

	/**
	 * The operation id for the '<em>Add Extra Data</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT___ADD_EXTRA_DATA__STRING_STRING_SERIALIZABLE_EXTRADATAFORMATTYPE = EVENT_OPERATION_COUNT + 3;

	/**
	 * The operation id for the '<em>Get Value With Path As</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT___GET_VALUE_WITH_PATH_AS__ITERABLE_CLASS_OBJECT = EVENT_OPERATION_COUNT + 4;

	/**
	 * The number of operations of the '<em>Vessel Event Visit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT_OPERATION_COUNT = EVENT_OPERATION_COUNT + 5;

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
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__PORT = EVENT__PORT;

	/**
	 * The feature id for the '<em><b>Previous Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__PREVIOUS_EVENT = EVENT__PREVIOUS_EVENT;

	/**
	 * The feature id for the '<em><b>Next Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__NEXT_EVENT = EVENT__NEXT_EVENT;

	/**
	 * The feature id for the '<em><b>Sequence</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__SEQUENCE = EVENT__SEQUENCE;

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
	 * The feature id for the '<em><b>Distance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__DISTANCE = EVENT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__SPEED = EVENT_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Journey</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY_FEATURE_COUNT = EVENT_FEATURE_COUNT + 7;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY___MAKE_PROXIES = EVENT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY___RESOLVE_PROXIES__MAP = EVENT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY___RESTORE_PROXIES = EVENT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY___COLLECT_UUID_OBJECTS__MAP = EVENT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY___COLLECT_UUID_OBJECTS = EVENT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY___GET_UNSET_VALUE__ESTRUCTURALFEATURE = EVENT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = EVENT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY___ECONTAINER_OP = EVENT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Time Zone</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY___GET_TIME_ZONE__EATTRIBUTE = EVENT___GET_TIME_ZONE__EATTRIBUTE;

	/**
	 * The operation id for the '<em>Get Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY___GET_DURATION = EVENT___GET_DURATION;

	/**
	 * The operation id for the '<em>Get Local Start</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY___GET_LOCAL_START = EVENT___GET_LOCAL_START;

	/**
	 * The operation id for the '<em>Get Local End</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY___GET_LOCAL_END = EVENT___GET_LOCAL_END;

	/**
	 * The operation id for the '<em>Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY___TYPE = EVENT___TYPE;

	/**
	 * The operation id for the '<em>Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY___NAME = EVENT___NAME;

	/**
	 * The operation id for the '<em>Get Hire Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY___GET_HIRE_COST = EVENT___GET_HIRE_COST;

	/**
	 * The operation id for the '<em>Get Fuel Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY___GET_FUEL_COST = EVENT_OPERATION_COUNT + 0;

	/**
	 * The number of operations of the '<em>Journey</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY_OPERATION_COUNT = EVENT_OPERATION_COUNT + 1;

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
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE__PORT = EVENT__PORT;

	/**
	 * The feature id for the '<em><b>Previous Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE__PREVIOUS_EVENT = EVENT__PREVIOUS_EVENT;

	/**
	 * The feature id for the '<em><b>Next Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE__NEXT_EVENT = EVENT__NEXT_EVENT;

	/**
	 * The feature id for the '<em><b>Sequence</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE__SEQUENCE = EVENT__SEQUENCE;

	/**
	 * The feature id for the '<em><b>Fuels</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE__FUELS = EVENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Laden</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE__LADEN = EVENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Idle</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE_FEATURE_COUNT = EVENT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE___MAKE_PROXIES = EVENT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE___RESOLVE_PROXIES__MAP = EVENT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE___RESTORE_PROXIES = EVENT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE___COLLECT_UUID_OBJECTS__MAP = EVENT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE___COLLECT_UUID_OBJECTS = EVENT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE___GET_UNSET_VALUE__ESTRUCTURALFEATURE = EVENT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = EVENT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE___ECONTAINER_OP = EVENT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Time Zone</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE___GET_TIME_ZONE__EATTRIBUTE = EVENT___GET_TIME_ZONE__EATTRIBUTE;

	/**
	 * The operation id for the '<em>Get Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE___GET_DURATION = EVENT___GET_DURATION;

	/**
	 * The operation id for the '<em>Get Local Start</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE___GET_LOCAL_START = EVENT___GET_LOCAL_START;

	/**
	 * The operation id for the '<em>Get Local End</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE___GET_LOCAL_END = EVENT___GET_LOCAL_END;

	/**
	 * The operation id for the '<em>Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE___TYPE = EVENT___TYPE;

	/**
	 * The operation id for the '<em>Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE___NAME = EVENT___NAME;

	/**
	 * The operation id for the '<em>Get Hire Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE___GET_HIRE_COST = EVENT___GET_HIRE_COST;

	/**
	 * The operation id for the '<em>Get Fuel Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE___GET_FUEL_COST = EVENT_OPERATION_COUNT + 0;

	/**
	 * The number of operations of the '<em>Idle</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE_OPERATION_COUNT = EVENT_OPERATION_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.GeneratedCharterOutImpl <em>Generated Charter Out</em>}' class.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.GeneratedCharterOutImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getGeneratedCharterOut()
	 * @generated
	 */
	int GENERATED_CHARTER_OUT = 8;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT__EXTENSIONS = EVENT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT__PROXIES = EVENT__PROXIES;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT__START = EVENT__START;

	/**
	 * The feature id for the '<em><b>End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT__END = EVENT__END;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
 	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT__PORT = EVENT__PORT;

	/**
	 * The feature id for the '<em><b>Previous Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT__PREVIOUS_EVENT = EVENT__PREVIOUS_EVENT;

	/**
	 * The feature id for the '<em><b>Next Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT__NEXT_EVENT = EVENT__NEXT_EVENT;

	/**
	 * The feature id for the '<em><b>Sequence</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT__SEQUENCE = EVENT__SEQUENCE;

	/**
	 * The feature id for the '<em><b>Extra Data</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT__EXTRA_DATA = EVENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Revenue</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT__REVENUE = EVENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Generated Charter Out</em>' class.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT_FEATURE_COUNT = EVENT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT___MAKE_PROXIES = EVENT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT___RESOLVE_PROXIES__MAP = EVENT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT___RESTORE_PROXIES = EVENT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT___COLLECT_UUID_OBJECTS__MAP = EVENT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT___COLLECT_UUID_OBJECTS = EVENT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = EVENT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = EVENT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT___ECONTAINER_OP = EVENT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Time Zone</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT___GET_TIME_ZONE__EATTRIBUTE = EVENT___GET_TIME_ZONE__EATTRIBUTE;

	/**
	 * The operation id for the '<em>Get Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT___GET_DURATION = EVENT___GET_DURATION;

	/**
	 * The operation id for the '<em>Get Local Start</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT___GET_LOCAL_START = EVENT___GET_LOCAL_START;

	/**
	 * The operation id for the '<em>Get Local End</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT___GET_LOCAL_END = EVENT___GET_LOCAL_END;

	/**
	 * The operation id for the '<em>Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT___TYPE = EVENT___TYPE;

	/**
	 * The operation id for the '<em>Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT___NAME = EVENT___NAME;

	/**
	 * The operation id for the '<em>Get Hire Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT___GET_HIRE_COST = EVENT___GET_HIRE_COST;

	/**
	 * The operation id for the '<em>Get Data With Path</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT___GET_DATA_WITH_PATH__ITERABLE = EVENT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Data With Key</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT___GET_DATA_WITH_KEY__STRING = EVENT_OPERATION_COUNT + 1;

	/**
	 * The operation id for the '<em>Add Extra Data</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT___ADD_EXTRA_DATA__STRING_STRING = EVENT_OPERATION_COUNT + 2;

	/**
	 * The operation id for the '<em>Add Extra Data</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT___ADD_EXTRA_DATA__STRING_STRING_SERIALIZABLE_EXTRADATAFORMATTYPE = EVENT_OPERATION_COUNT + 3;

	/**
	 * The operation id for the '<em>Get Value With Path As</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT___GET_VALUE_WITH_PATH_AS__ITERABLE_CLASS_OBJECT = EVENT_OPERATION_COUNT + 4;

	/**
	 * The number of operations of the '<em>Generated Charter Out</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT_OPERATION_COUNT = EVENT_OPERATION_COUNT + 5;

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
	 * The operation id for the '<em>Get Fuel Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_USAGE___GET_FUEL_COST = 0;

	/**
	 * The number of operations of the '<em>Fuel Usage</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_USAGE_OPERATION_COUNT = 1;

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
	 * The feature id for the '<em><b>Fuel</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_QUANTITY__FUEL = 0;

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
	 * The number of operations of the '<em>Fuel Quantity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_QUANTITY_OPERATION_COUNT = 0;

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
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN__PORT = EVENT__PORT;

	/**
	 * The feature id for the '<em><b>Previous Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN__PREVIOUS_EVENT = EVENT__PREVIOUS_EVENT;

	/**
	 * The feature id for the '<em><b>Next Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN__NEXT_EVENT = EVENT__NEXT_EVENT;

	/**
	 * The feature id for the '<em><b>Sequence</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN__SEQUENCE = EVENT__SEQUENCE;

	/**
	 * The feature id for the '<em><b>Fuels</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN__FUELS = EVENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN__VOLUME = EVENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN__COST = EVENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Cooldown</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN_FEATURE_COUNT = EVENT_FEATURE_COUNT + 3;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN___MAKE_PROXIES = EVENT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN___RESOLVE_PROXIES__MAP = EVENT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN___RESTORE_PROXIES = EVENT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN___COLLECT_UUID_OBJECTS__MAP = EVENT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN___COLLECT_UUID_OBJECTS = EVENT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN___GET_UNSET_VALUE__ESTRUCTURALFEATURE = EVENT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = EVENT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN___ECONTAINER_OP = EVENT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Time Zone</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN___GET_TIME_ZONE__EATTRIBUTE = EVENT___GET_TIME_ZONE__EATTRIBUTE;

	/**
	 * The operation id for the '<em>Get Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN___GET_DURATION = EVENT___GET_DURATION;

	/**
	 * The operation id for the '<em>Get Local Start</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN___GET_LOCAL_START = EVENT___GET_LOCAL_START;

	/**
	 * The operation id for the '<em>Get Local End</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN___GET_LOCAL_END = EVENT___GET_LOCAL_END;

	/**
	 * The operation id for the '<em>Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN___TYPE = EVENT___TYPE;

	/**
	 * The operation id for the '<em>Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN___NAME = EVENT___NAME;

	/**
	 * The operation id for the '<em>Get Hire Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN___GET_HIRE_COST = EVENT___GET_HIRE_COST;

	/**
	 * The operation id for the '<em>Get Fuel Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN___GET_FUEL_COST = EVENT_OPERATION_COUNT + 0;

	/**
	 * The number of operations of the '<em>Cooldown</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN_OPERATION_COUNT = EVENT_OPERATION_COUNT + 1;

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
	 * The feature id for the '<em><b>Extra Data</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__EXTRA_DATA = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Slot Allocations</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__SLOT_ALLOCATIONS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Input Cargo</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__INPUT_CARGO = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Events</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__EVENTS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Sequence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__SEQUENCE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Cargo Allocation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION___MAKE_PROXIES = MMXCorePackage.MMX_OBJECT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION___RESOLVE_PROXIES__MAP = MMXCorePackage.MMX_OBJECT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION___RESTORE_PROXIES = MMXCorePackage.MMX_OBJECT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION___COLLECT_UUID_OBJECTS__MAP = MMXCorePackage.MMX_OBJECT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION___COLLECT_UUID_OBJECTS = MMXCorePackage.MMX_OBJECT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.MMX_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.MMX_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION___ECONTAINER_OP = MMXCorePackage.MMX_OBJECT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Data With Path</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION___GET_DATA_WITH_PATH__ITERABLE = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Data With Key</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION___GET_DATA_WITH_KEY__STRING = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 1;

	/**
	 * The operation id for the '<em>Add Extra Data</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION___ADD_EXTRA_DATA__STRING_STRING = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 2;

	/**
	 * The operation id for the '<em>Add Extra Data</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION___ADD_EXTRA_DATA__STRING_STRING_SERIALIZABLE_EXTRADATAFORMATTYPE = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 3;

	/**
	 * The operation id for the '<em>Get Value With Path As</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION___GET_VALUE_WITH_PATH_AS__ITERABLE_CLASS_OBJECT = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 4;

	/**
	 * The operation id for the '<em>Get Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION___GET_NAME = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 5;

	/**
	 * The number of operations of the '<em>Cargo Allocation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION_OPERATION_COUNT = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 6;

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
	 * The feature id for the '<em><b>Cargo Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION__CARGO_ALLOCATION = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Slot Visit</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION__SLOT_VISIT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION__PRICE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Volume Transferred</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION__VOLUME_TRANSFERRED = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Slot Allocation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION___MAKE_PROXIES = MMXCorePackage.MMX_OBJECT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION___RESOLVE_PROXIES__MAP = MMXCorePackage.MMX_OBJECT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION___RESTORE_PROXIES = MMXCorePackage.MMX_OBJECT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION___COLLECT_UUID_OBJECTS__MAP = MMXCorePackage.MMX_OBJECT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION___COLLECT_UUID_OBJECTS = MMXCorePackage.MMX_OBJECT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.MMX_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.MMX_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION___ECONTAINER_OP = MMXCorePackage.MMX_OBJECT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Port</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION___GET_PORT = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Local Start</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION___GET_LOCAL_START = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Local End</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION___GET_LOCAL_END = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 2;

	/**
	 * The operation id for the '<em>Get Contract</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION___GET_CONTRACT = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 3;

	/**
	 * The operation id for the '<em>Get Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION___GET_NAME = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 4;

	/**
	 * The number of operations of the '<em>Slot Allocation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION_OPERATION_COUNT = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 5;

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
	 * The number of structural features of the '<em>Fuel Amount</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_AMOUNT_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Fuel Amount</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_AMOUNT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.FitnessImpl <em>Fitness</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.FitnessImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getFitness()
	 * @generated
	 */
	int FITNESS = 15;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FITNESS__EXTENSIONS = MMXCorePackage.NAMED_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FITNESS__PROXIES = MMXCorePackage.NAMED_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FITNESS__NAME = MMXCorePackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FITNESS__OTHER_NAMES = MMXCorePackage.NAMED_OBJECT__OTHER_NAMES;

	/**
	 * The feature id for the '<em><b>Fitness Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FITNESS__FITNESS_VALUE = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Fitness</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FITNESS_FEATURE_COUNT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FITNESS___MAKE_PROXIES = MMXCorePackage.NAMED_OBJECT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FITNESS___RESOLVE_PROXIES__MAP = MMXCorePackage.NAMED_OBJECT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FITNESS___RESTORE_PROXIES = MMXCorePackage.NAMED_OBJECT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FITNESS___COLLECT_UUID_OBJECTS__MAP = MMXCorePackage.NAMED_OBJECT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FITNESS___COLLECT_UUID_OBJECTS = MMXCorePackage.NAMED_OBJECT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FITNESS___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.NAMED_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FITNESS___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.NAMED_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FITNESS___ECONTAINER_OP = MMXCorePackage.NAMED_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>Fitness</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FITNESS_OPERATION_COUNT = MMXCorePackage.NAMED_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.PortVisitImpl <em>Port Visit</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.PortVisitImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getPortVisit()
	 * @generated
	 */
	int PORT_VISIT = 16;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT__EXTENSIONS = EVENT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT__PROXIES = EVENT__PROXIES;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT__START = EVENT__START;

	/**
	 * The feature id for the '<em><b>End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT__END = EVENT__END;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT__PORT = EVENT__PORT;

	/**
	 * The feature id for the '<em><b>Previous Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT__PREVIOUS_EVENT = EVENT__PREVIOUS_EVENT;

	/**
	 * The feature id for the '<em><b>Next Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT__NEXT_EVENT = EVENT__NEXT_EVENT;

	/**
	 * The feature id for the '<em><b>Sequence</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT__SEQUENCE = EVENT__SEQUENCE;

	/**
	 * The feature id for the '<em><b>Port Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT__PORT_COST = EVENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Port Visit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT_FEATURE_COUNT = EVENT_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT___MAKE_PROXIES = EVENT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT___RESOLVE_PROXIES__MAP = EVENT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT___RESTORE_PROXIES = EVENT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT___COLLECT_UUID_OBJECTS__MAP = EVENT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT___COLLECT_UUID_OBJECTS = EVENT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = EVENT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = EVENT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT___ECONTAINER_OP = EVENT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Time Zone</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT___GET_TIME_ZONE__EATTRIBUTE = EVENT___GET_TIME_ZONE__EATTRIBUTE;

	/**
	 * The operation id for the '<em>Get Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT___GET_DURATION = EVENT___GET_DURATION;

	/**
	 * The operation id for the '<em>Get Local Start</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT___GET_LOCAL_START = EVENT___GET_LOCAL_START;

	/**
	 * The operation id for the '<em>Get Local End</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT___GET_LOCAL_END = EVENT___GET_LOCAL_END;

	/**
	 * The operation id for the '<em>Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT___TYPE = EVENT___TYPE;

	/**
	 * The operation id for the '<em>Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT___NAME = EVENT___NAME;

	/**
	 * The operation id for the '<em>Get Hire Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT___GET_HIRE_COST = EVENT___GET_HIRE_COST;

	/**
	 * The number of operations of the '<em>Port Visit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT_OPERATION_COUNT = EVENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.StartEventImpl <em>Start Event</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.StartEventImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getStartEvent()
	 * @generated
	 */
	int START_EVENT = 17;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT__EXTENSIONS = EVENT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT__PROXIES = EVENT__PROXIES;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT__START = EVENT__START;

	/**
	 * The feature id for the '<em><b>End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT__END = EVENT__END;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT__PORT = EVENT__PORT;

	/**
	 * The feature id for the '<em><b>Previous Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT__PREVIOUS_EVENT = EVENT__PREVIOUS_EVENT;

	/**
	 * The feature id for the '<em><b>Next Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT__NEXT_EVENT = EVENT__NEXT_EVENT;

	/**
	 * The feature id for the '<em><b>Sequence</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT__SEQUENCE = EVENT__SEQUENCE;

	/**
	 * The feature id for the '<em><b>Fuels</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT__FUELS = EVENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Port Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT__PORT_COST = EVENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Extra Data</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT__EXTRA_DATA = EVENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Slot Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT__SLOT_ALLOCATION = EVENT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Start Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT_FEATURE_COUNT = EVENT_FEATURE_COUNT + 4;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT___MAKE_PROXIES = EVENT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT___RESOLVE_PROXIES__MAP = EVENT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT___RESTORE_PROXIES = EVENT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT___COLLECT_UUID_OBJECTS__MAP = EVENT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT___COLLECT_UUID_OBJECTS = EVENT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = EVENT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = EVENT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT___ECONTAINER_OP = EVENT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Time Zone</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT___GET_TIME_ZONE__EATTRIBUTE = EVENT___GET_TIME_ZONE__EATTRIBUTE;

	/**
	 * The operation id for the '<em>Get Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT___GET_DURATION = EVENT___GET_DURATION;

	/**
	 * The operation id for the '<em>Get Local Start</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT___GET_LOCAL_START = EVENT___GET_LOCAL_START;

	/**
	 * The operation id for the '<em>Get Local End</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT___GET_LOCAL_END = EVENT___GET_LOCAL_END;

	/**
	 * The operation id for the '<em>Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT___TYPE = EVENT___TYPE;

	/**
	 * The operation id for the '<em>Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT___NAME = EVENT___NAME;

	/**
	 * The operation id for the '<em>Get Hire Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT___GET_HIRE_COST = EVENT___GET_HIRE_COST;

	/**
	 * The operation id for the '<em>Get Fuel Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT___GET_FUEL_COST = EVENT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Data With Path</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT___GET_DATA_WITH_PATH__ITERABLE = EVENT_OPERATION_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Data With Key</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT___GET_DATA_WITH_KEY__STRING = EVENT_OPERATION_COUNT + 2;

	/**
	 * The operation id for the '<em>Add Extra Data</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT___ADD_EXTRA_DATA__STRING_STRING = EVENT_OPERATION_COUNT + 3;

	/**
	 * The operation id for the '<em>Add Extra Data</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT___ADD_EXTRA_DATA__STRING_STRING_SERIALIZABLE_EXTRADATAFORMATTYPE = EVENT_OPERATION_COUNT + 4;

	/**
	 * The operation id for the '<em>Get Value With Path As</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT___GET_VALUE_WITH_PATH_AS__ITERABLE_CLASS_OBJECT = EVENT_OPERATION_COUNT + 5;

	/**
	 * The number of operations of the '<em>Start Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT_OPERATION_COUNT = EVENT_OPERATION_COUNT + 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.EndEventImpl <em>End Event</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.EndEventImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getEndEvent()
	 * @generated
	 */
	int END_EVENT = 18;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT__EXTENSIONS = EVENT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT__PROXIES = EVENT__PROXIES;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT__START = EVENT__START;

	/**
	 * The feature id for the '<em><b>End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT__END = EVENT__END;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT__PORT = EVENT__PORT;

	/**
	 * The feature id for the '<em><b>Previous Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT__PREVIOUS_EVENT = EVENT__PREVIOUS_EVENT;

	/**
	 * The feature id for the '<em><b>Next Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT__NEXT_EVENT = EVENT__NEXT_EVENT;

	/**
	 * The feature id for the '<em><b>Sequence</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT__SEQUENCE = EVENT__SEQUENCE;

	/**
	 * The feature id for the '<em><b>Fuels</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT__FUELS = EVENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Port Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT__PORT_COST = EVENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Extra Data</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT__EXTRA_DATA = EVENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Slot Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT__SLOT_ALLOCATION = EVENT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>End Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT_FEATURE_COUNT = EVENT_FEATURE_COUNT + 4;

	/**
	 * The operation id for the '<em>Make Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT___MAKE_PROXIES = EVENT___MAKE_PROXIES;

	/**
	 * The operation id for the '<em>Resolve Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT___RESOLVE_PROXIES__MAP = EVENT___RESOLVE_PROXIES__MAP;

	/**
	 * The operation id for the '<em>Restore Proxies</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT___RESTORE_PROXIES = EVENT___RESTORE_PROXIES;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT___COLLECT_UUID_OBJECTS__MAP = EVENT___COLLECT_UUID_OBJECTS__MAP;

	/**
	 * The operation id for the '<em>Collect UUID Objects</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT___COLLECT_UUID_OBJECTS = EVENT___COLLECT_UUID_OBJECTS;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = EVENT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = EVENT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT___ECONTAINER_OP = EVENT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Time Zone</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT___GET_TIME_ZONE__EATTRIBUTE = EVENT___GET_TIME_ZONE__EATTRIBUTE;

	/**
	 * The operation id for the '<em>Get Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT___GET_DURATION = EVENT___GET_DURATION;

	/**
	 * The operation id for the '<em>Get Local Start</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT___GET_LOCAL_START = EVENT___GET_LOCAL_START;

	/**
	 * The operation id for the '<em>Get Local End</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT___GET_LOCAL_END = EVENT___GET_LOCAL_END;

	/**
	 * The operation id for the '<em>Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT___TYPE = EVENT___TYPE;

	/**
	 * The operation id for the '<em>Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT___NAME = EVENT___NAME;

	/**
	 * The operation id for the '<em>Get Hire Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT___GET_HIRE_COST = EVENT___GET_HIRE_COST;

	/**
	 * The operation id for the '<em>Get Fuel Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT___GET_FUEL_COST = EVENT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Data With Path</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT___GET_DATA_WITH_PATH__ITERABLE = EVENT_OPERATION_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Data With Key</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT___GET_DATA_WITH_KEY__STRING = EVENT_OPERATION_COUNT + 2;

	/**
	 * The operation id for the '<em>Add Extra Data</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT___ADD_EXTRA_DATA__STRING_STRING = EVENT_OPERATION_COUNT + 3;

	/**
	 * The operation id for the '<em>Add Extra Data</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT___ADD_EXTRA_DATA__STRING_STRING_SERIALIZABLE_EXTRADATAFORMATTYPE = EVENT_OPERATION_COUNT + 4;

	/**
	 * The operation id for the '<em>Get Value With Path As</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT___GET_VALUE_WITH_PATH_AS__ITERABLE_CLASS_OBJECT = EVENT_OPERATION_COUNT + 5;

	/**
	 * The number of operations of the '<em>End Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT_OPERATION_COUNT = EVENT_OPERATION_COUNT + 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.FuelUnit <em>Fuel Unit</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.FuelUnit
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getFuelUnit()
	 * @generated
	 */
	int FUEL_UNIT = 19;


	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.Fuel <em>Fuel</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.Fuel
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getFuel()
	 * @generated
	 */
	int FUEL = 20;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.SequenceType <em>Sequence Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.SequenceType
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getSequenceType()
	 * @generated
	 */
	int SEQUENCE_TYPE = 21;

	/**
	 * The meta object id for the '<em>Calendar</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.util.Calendar
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getCalendar()
	 * @generated
	 */
	int CALENDAR = 22;


	/**
	 * The meta object id for the '<em>Iterable</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.Iterable
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getIterable()
	 * @generated
	 */
	int ITERABLE = 23;


	/**
	 * The meta object id for the '<em>Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.Object
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getObject()
	 * @generated
	 */
	int OBJECT = 24;


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
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.schedule.ScheduleModel#getSchedule <em>Schedule</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Schedule</em>'.
	 * @see com.mmxlabs.models.lng.schedule.ScheduleModel#getSchedule()
	 * @see #getScheduleModel()
	 * @generated
	 */
	EReference getScheduleModel_Schedule();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.ScheduleModel#isDirty <em>Dirty</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Dirty</em>'.
	 * @see com.mmxlabs.models.lng.schedule.ScheduleModel#isDirty()
	 * @see #getScheduleModel()
	 * @generated
	 */
	EAttribute getScheduleModel_Dirty();

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
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.schedule.Schedule#getCargoAllocations <em>Cargo Allocations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Cargo Allocations</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Schedule#getCargoAllocations()
	 * @see #getSchedule()
	 * @generated
	 */
	EReference getSchedule_CargoAllocations();

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
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.schedule.Schedule#getFitnesses <em>Fitnesses</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Fitnesses</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Schedule#getFitnesses()
	 * @see #getSchedule()
	 * @generated
	 */
	EReference getSchedule_Fitnesses();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.schedule.Schedule#getUnusedElements <em>Unused Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Unused Elements</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Schedule#getUnusedElements()
	 * @see #getSchedule()
	 * @generated
	 */
	EReference getSchedule_UnusedElements();

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
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.schedule.Sequence#getFitnesses <em>Fitnesses</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Fitnesses</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Sequence#getFitnesses()
	 * @see #getSequence()
	 * @generated
	 */
	EReference getSequence_Fitnesses();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.Sequence#getDailyHireRate <em>Daily Hire Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Daily Hire Rate</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Sequence#getDailyHireRate()
	 * @see #getSequence()
	 * @generated
	 */
	EAttribute getSequence_DailyHireRate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.Sequence#getSpotIndex <em>Spot Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Spot Index</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Sequence#getSpotIndex()
	 * @see #getSequence()
	 * @generated
	 */
	EAttribute getSequence_SpotIndex();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.Sequence#getSequenceType <em>Sequence Type</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Sequence Type</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Sequence#getSequenceType()
	 * @see #getSequence()
	 * @generated
	 */
	EAttribute getSequence_SequenceType();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.schedule.Sequence#getName() <em>Get Name</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Name</em>' operation.
	 * @see com.mmxlabs.models.lng.schedule.Sequence#getName()
	 * @generated
	 */
	EOperation getSequence__GetName();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.schedule.Sequence#isSpotVessel() <em>Is Spot Vessel</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Is Spot Vessel</em>' operation.
	 * @see com.mmxlabs.models.lng.schedule.Sequence#isSpotVessel()
	 * @generated
	 */
	EOperation getSequence__IsSpotVessel();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.schedule.Sequence#isFleetVessel() <em>Is Fleet Vessel</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Is Fleet Vessel</em>' operation.
	 * @see com.mmxlabs.models.lng.schedule.Sequence#isFleetVessel()
	 * @generated
	 */
	EOperation getSequence__IsFleetVessel();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.schedule.Sequence#isTimeCharterVessel() <em>Is Time Charter Vessel</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Is Time Charter Vessel</em>' operation.
	 * @see com.mmxlabs.models.lng.schedule.Sequence#isTimeCharterVessel()
	 * @generated
	 */
	EOperation getSequence__IsTimeCharterVessel();

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
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.Event#getPort <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Port</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Event#getPort()
	 * @see #getEvent()
	 * @generated
	 */
	EReference getEvent_Port();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.Event#getPreviousEvent <em>Previous Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Previous Event</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Event#getPreviousEvent()
	 * @see #getEvent()
	 * @generated
	 */
	EReference getEvent_PreviousEvent();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.Event#getNextEvent <em>Next Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Next Event</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Event#getNextEvent()
	 * @see #getEvent()
	 * @generated
	 */
	EReference getEvent_NextEvent();

	/**
	 * Returns the meta object for the container reference '{@link com.mmxlabs.models.lng.schedule.Event#getSequence <em>Sequence</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Sequence</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Event#getSequence()
	 * @see #getEvent()
	 * @generated
	 */
	EReference getEvent_Sequence();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.schedule.Event#getDuration() <em>Get Duration</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Duration</em>' operation.
	 * @see com.mmxlabs.models.lng.schedule.Event#getDuration()
	 * @generated
	 */
	EOperation getEvent__GetDuration();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.schedule.Event#getLocalStart() <em>Get Local Start</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Local Start</em>' operation.
	 * @see com.mmxlabs.models.lng.schedule.Event#getLocalStart()
	 * @generated
	 */
	EOperation getEvent__GetLocalStart();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.schedule.Event#getLocalEnd() <em>Get Local End</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Local End</em>' operation.
	 * @see com.mmxlabs.models.lng.schedule.Event#getLocalEnd()
	 * @generated
	 */
	EOperation getEvent__GetLocalEnd();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.schedule.Event#type() <em>Type</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Type</em>' operation.
	 * @see com.mmxlabs.models.lng.schedule.Event#type()
	 * @generated
	 */
	EOperation getEvent__Type();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.schedule.Event#name() <em>Name</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Name</em>' operation.
	 * @see com.mmxlabs.models.lng.schedule.Event#name()
	 * @generated
	 */
	EOperation getEvent__Name();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.schedule.Event#getHireCost() <em>Get Hire Cost</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Hire Cost</em>' operation.
	 * @see com.mmxlabs.models.lng.schedule.Event#getHireCost()
	 * @generated
	 */
	EOperation getEvent__GetHireCost();

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
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.SlotVisit#getSlotAllocation <em>Slot Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Slot Allocation</em>'.
	 * @see com.mmxlabs.models.lng.schedule.SlotVisit#getSlotAllocation()
	 * @see #getSlotVisit()
	 * @generated
	 */
	EReference getSlotVisit_SlotAllocation();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.Journey#getDistance <em>Distance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Distance</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Journey#getDistance()
	 * @see #getJourney()
	 * @generated
	 */
	EAttribute getJourney_Distance();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.Journey#getSpeed <em>Speed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Speed</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Journey#getSpeed()
	 * @see #getJourney()
	 * @generated
	 */
	EAttribute getJourney_Speed();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.Idle#isLaden <em>Laden</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Laden</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Idle#isLaden()
	 * @see #getIdle()
	 * @generated
	 */
	EAttribute getIdle_Laden();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.GeneratedCharterOut <em>Generated Charter Out</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Generated Charter Out</em>'.
	 * @see com.mmxlabs.models.lng.schedule.GeneratedCharterOut
	 * @generated
	 */
	EClass getGeneratedCharterOut();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.GeneratedCharterOut#getRevenue <em>Revenue</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Revenue</em>'.
	 * @see com.mmxlabs.models.lng.schedule.GeneratedCharterOut#getRevenue()
	 * @see #getGeneratedCharterOut()
	 * @generated
	 */
	EAttribute getGeneratedCharterOut_Revenue();

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
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.schedule.FuelUsage#getFuelCost() <em>Get Fuel Cost</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Fuel Cost</em>' operation.
	 * @see com.mmxlabs.models.lng.schedule.FuelUsage#getFuelCost()
	 * @generated
	 */
	EOperation getFuelUsage__GetFuelCost();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.FuelQuantity#getFuel <em>Fuel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fuel</em>'.
	 * @see com.mmxlabs.models.lng.schedule.FuelQuantity#getFuel()
	 * @see #getFuelQuantity()
	 * @generated
	 */
	EAttribute getFuelQuantity_Fuel();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.Cooldown#getVolume <em>Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volume</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Cooldown#getVolume()
	 * @see #getCooldown()
	 * @generated
	 */
	EAttribute getCooldown_Volume();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.Cooldown#getCost <em>Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cost</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Cooldown#getCost()
	 * @see #getCooldown()
	 * @generated
	 */
	EAttribute getCooldown_Cost();

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
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getSlotAllocations <em>Slot Allocations</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Slot Allocations</em>'.
	 * @see com.mmxlabs.models.lng.schedule.CargoAllocation#getSlotAllocations()
	 * @see #getCargoAllocation()
	 * @generated
	 */
	EReference getCargoAllocation_SlotAllocations();

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
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getEvents <em>Events</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Events</em>'.
	 * @see com.mmxlabs.models.lng.schedule.CargoAllocation#getEvents()
	 * @see #getCargoAllocation()
	 * @generated
	 */
	EReference getCargoAllocation_Events();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getSequence <em>Sequence</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Sequence</em>'.
	 * @see com.mmxlabs.models.lng.schedule.CargoAllocation#getSequence()
	 * @see #getCargoAllocation()
	 * @generated
	 */
	EReference getCargoAllocation_Sequence();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getName() <em>Get Name</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Name</em>' operation.
	 * @see com.mmxlabs.models.lng.schedule.CargoAllocation#getName()
	 * @generated
	 */
	EOperation getCargoAllocation__GetName();

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
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getCargoAllocation <em>Cargo Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Cargo Allocation</em>'.
	 * @see com.mmxlabs.models.lng.schedule.SlotAllocation#getCargoAllocation()
	 * @see #getSlotAllocation()
	 * @generated
	 */
	EReference getSlotAllocation_CargoAllocation();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getSlotVisit <em>Slot Visit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Slot Visit</em>'.
	 * @see com.mmxlabs.models.lng.schedule.SlotAllocation#getSlotVisit()
	 * @see #getSlotAllocation()
	 * @generated
	 */
	EReference getSlotAllocation_SlotVisit();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getPrice <em>Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price</em>'.
	 * @see com.mmxlabs.models.lng.schedule.SlotAllocation#getPrice()
	 * @see #getSlotAllocation()
	 * @generated
	 */
	EAttribute getSlotAllocation_Price();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getVolumeTransferred <em>Volume Transferred</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volume Transferred</em>'.
	 * @see com.mmxlabs.models.lng.schedule.SlotAllocation#getVolumeTransferred()
	 * @see #getSlotAllocation()
	 * @generated
	 */
	EAttribute getSlotAllocation_VolumeTransferred();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getPort() <em>Get Port</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Port</em>' operation.
	 * @see com.mmxlabs.models.lng.schedule.SlotAllocation#getPort()
	 * @generated
	 */
	EOperation getSlotAllocation__GetPort();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getLocalStart() <em>Get Local Start</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Local Start</em>' operation.
	 * @see com.mmxlabs.models.lng.schedule.SlotAllocation#getLocalStart()
	 * @generated
	 */
	EOperation getSlotAllocation__GetLocalStart();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getLocalEnd() <em>Get Local End</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Local End</em>' operation.
	 * @see com.mmxlabs.models.lng.schedule.SlotAllocation#getLocalEnd()
	 * @generated
	 */
	EOperation getSlotAllocation__GetLocalEnd();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getContract() <em>Get Contract</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Contract</em>' operation.
	 * @see com.mmxlabs.models.lng.schedule.SlotAllocation#getContract()
	 * @generated
	 */
	EOperation getSlotAllocation__GetContract();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getName() <em>Get Name</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Name</em>' operation.
	 * @see com.mmxlabs.models.lng.schedule.SlotAllocation#getName()
	 * @generated
	 */
	EOperation getSlotAllocation__GetName();

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.Fitness <em>Fitness</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Fitness</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Fitness
	 * @generated
	 */
	EClass getFitness();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.Fitness#getFitnessValue <em>Fitness Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fitness Value</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Fitness#getFitnessValue()
	 * @see #getFitness()
	 * @generated
	 */
	EAttribute getFitness_FitnessValue();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.PortVisit <em>Port Visit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Port Visit</em>'.
	 * @see com.mmxlabs.models.lng.schedule.PortVisit
	 * @generated
	 */
	EClass getPortVisit();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.PortVisit#getPortCost <em>Port Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Port Cost</em>'.
	 * @see com.mmxlabs.models.lng.schedule.PortVisit#getPortCost()
	 * @see #getPortVisit()
	 * @generated
	 */
	EAttribute getPortVisit_PortCost();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.StartEvent <em>Start Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Start Event</em>'.
	 * @see com.mmxlabs.models.lng.schedule.StartEvent
	 * @generated
	 */
	EClass getStartEvent();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.StartEvent#getSlotAllocation <em>Slot Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Slot Allocation</em>'.
	 * @see com.mmxlabs.models.lng.schedule.StartEvent#getSlotAllocation()
	 * @see #getStartEvent()
	 * @generated
	 */
	EReference getStartEvent_SlotAllocation();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.EndEvent <em>End Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>End Event</em>'.
	 * @see com.mmxlabs.models.lng.schedule.EndEvent
	 * @generated
	 */
	EClass getEndEvent();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.EndEvent#getSlotAllocation <em>Slot Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Slot Allocation</em>'.
	 * @see com.mmxlabs.models.lng.schedule.EndEvent#getSlotAllocation()
	 * @see #getEndEvent()
	 * @generated
	 */
	EReference getEndEvent_SlotAllocation();

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
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.schedule.Fuel <em>Fuel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Fuel</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Fuel
	 * @generated
	 */
	EEnum getFuel();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.schedule.SequenceType <em>Sequence Type</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Sequence Type</em>'.
	 * @see com.mmxlabs.models.lng.schedule.SequenceType
	 * @generated
	 */
	EEnum getSequenceType();

	/**
	 * Returns the meta object for data type '{@link java.util.Calendar <em>Calendar</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Calendar</em>'.
	 * @see java.util.Calendar
	 * @model instanceClass="java.util.Calendar"
	 * @generated
	 */
	EDataType getCalendar();

	/**
	 * Returns the meta object for data type '{@link java.lang.Iterable <em>Iterable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Iterable</em>'.
	 * @see java.lang.Iterable
	 * @model instanceClass="java.lang.Iterable" typeParameters="T"
	 * @generated
	 */
	EDataType getIterable();

	/**
	 * Returns the meta object for data type '{@link java.lang.Object <em>Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Object</em>'.
	 * @see java.lang.Object
	 * @model instanceClass="java.lang.Object"
	 * @generated
	 */
	EDataType getObject();

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
		 * The meta object literal for the '<em><b>Schedule</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * @since 3.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEDULE_MODEL__SCHEDULE = eINSTANCE.getScheduleModel_Schedule();

		/**
		 * The meta object literal for the '<em><b>Dirty</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCHEDULE_MODEL__DIRTY = eINSTANCE.getScheduleModel_Dirty();

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
		 * The meta object literal for the '<em><b>Cargo Allocations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEDULE__CARGO_ALLOCATIONS = eINSTANCE.getSchedule_CargoAllocations();

		/**
		 * The meta object literal for the '<em><b>Slot Allocations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEDULE__SLOT_ALLOCATIONS = eINSTANCE.getSchedule_SlotAllocations();

		/**
		 * The meta object literal for the '<em><b>Fitnesses</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEDULE__FITNESSES = eINSTANCE.getSchedule_Fitnesses();

		/**
		 * The meta object literal for the '<em><b>Unused Elements</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEDULE__UNUSED_ELEMENTS = eINSTANCE.getSchedule_UnusedElements();

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
		 * The meta object literal for the '<em><b>Fitnesses</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SEQUENCE__FITNESSES = eINSTANCE.getSequence_Fitnesses();

		/**
		 * The meta object literal for the '<em><b>Daily Hire Rate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SEQUENCE__DAILY_HIRE_RATE = eINSTANCE.getSequence_DailyHireRate();

		/**
		 * The meta object literal for the '<em><b>Spot Index</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SEQUENCE__SPOT_INDEX = eINSTANCE.getSequence_SpotIndex();

		/**
		 * The meta object literal for the '<em><b>Sequence Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SEQUENCE__SEQUENCE_TYPE = eINSTANCE.getSequence_SequenceType();

		/**
		 * The meta object literal for the '<em><b>Get Name</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SEQUENCE___GET_NAME = eINSTANCE.getSequence__GetName();

		/**
		 * The meta object literal for the '<em><b>Is Spot Vessel</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SEQUENCE___IS_SPOT_VESSEL = eINSTANCE.getSequence__IsSpotVessel();

		/**
		 * The meta object literal for the '<em><b>Is Fleet Vessel</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SEQUENCE___IS_FLEET_VESSEL = eINSTANCE.getSequence__IsFleetVessel();

		/**
		 * The meta object literal for the '<em><b>Is Time Charter Vessel</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SEQUENCE___IS_TIME_CHARTER_VESSEL = eINSTANCE.getSequence__IsTimeCharterVessel();

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
		 * The meta object literal for the '<em><b>Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EVENT__PORT = eINSTANCE.getEvent_Port();

		/**
		 * The meta object literal for the '<em><b>Previous Event</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EVENT__PREVIOUS_EVENT = eINSTANCE.getEvent_PreviousEvent();

		/**
		 * The meta object literal for the '<em><b>Next Event</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EVENT__NEXT_EVENT = eINSTANCE.getEvent_NextEvent();

		/**
		 * The meta object literal for the '<em><b>Sequence</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EVENT__SEQUENCE = eINSTANCE.getEvent_Sequence();

		/**
		 * The meta object literal for the '<em><b>Get Duration</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation EVENT___GET_DURATION = eINSTANCE.getEvent__GetDuration();

		/**
		 * The meta object literal for the '<em><b>Get Local Start</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation EVENT___GET_LOCAL_START = eINSTANCE.getEvent__GetLocalStart();

		/**
		 * The meta object literal for the '<em><b>Get Local End</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation EVENT___GET_LOCAL_END = eINSTANCE.getEvent__GetLocalEnd();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation EVENT___TYPE = eINSTANCE.getEvent__Type();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation EVENT___NAME = eINSTANCE.getEvent__Name();

		/**
		 * The meta object literal for the '<em><b>Get Hire Cost</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation EVENT___GET_HIRE_COST = eINSTANCE.getEvent__GetHireCost();

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
		 * The meta object literal for the '<em><b>Slot Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT_VISIT__SLOT_ALLOCATION = eINSTANCE.getSlotVisit_SlotAllocation();

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
		 * The meta object literal for the '<em><b>Distance</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JOURNEY__DISTANCE = eINSTANCE.getJourney_Distance();

		/**
		 * The meta object literal for the '<em><b>Speed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JOURNEY__SPEED = eINSTANCE.getJourney_Speed();

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
		 * The meta object literal for the '<em><b>Laden</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IDLE__LADEN = eINSTANCE.getIdle_Laden();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.GeneratedCharterOutImpl <em>Generated Charter Out</em>}' class.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.GeneratedCharterOutImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getGeneratedCharterOut()
		 * @generated
		 */
		EClass GENERATED_CHARTER_OUT = eINSTANCE.getGeneratedCharterOut();

		/**
		 * The meta object literal for the '<em><b>Revenue</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GENERATED_CHARTER_OUT__REVENUE = eINSTANCE.getGeneratedCharterOut_Revenue();

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
		 * The meta object literal for the '<em><b>Get Fuel Cost</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation FUEL_USAGE___GET_FUEL_COST = eINSTANCE.getFuelUsage__GetFuelCost();

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
		 * The meta object literal for the '<em><b>Fuel</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FUEL_QUANTITY__FUEL = eINSTANCE.getFuelQuantity_Fuel();

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
		 * The meta object literal for the '<em><b>Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COOLDOWN__VOLUME = eINSTANCE.getCooldown_Volume();

		/**
		 * The meta object literal for the '<em><b>Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COOLDOWN__COST = eINSTANCE.getCooldown_Cost();

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
		 * The meta object literal for the '<em><b>Slot Allocations</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * @since 3.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_ALLOCATION__SLOT_ALLOCATIONS = eINSTANCE.getCargoAllocation_SlotAllocations();

		/**
		 * The meta object literal for the '<em><b>Input Cargo</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_ALLOCATION__INPUT_CARGO = eINSTANCE.getCargoAllocation_InputCargo();

		/**
		 * The meta object literal for the '<em><b>Events</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * @since 3.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_ALLOCATION__EVENTS = eINSTANCE.getCargoAllocation_Events();

		/**
		 * The meta object literal for the '<em><b>Sequence</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_ALLOCATION__SEQUENCE = eINSTANCE.getCargoAllocation_Sequence();

		/**
		 * The meta object literal for the '<em><b>Get Name</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CARGO_ALLOCATION___GET_NAME = eINSTANCE.getCargoAllocation__GetName();

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
		 * The meta object literal for the '<em><b>Cargo Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT_ALLOCATION__CARGO_ALLOCATION = eINSTANCE.getSlotAllocation_CargoAllocation();

		/**
		 * The meta object literal for the '<em><b>Slot Visit</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT_ALLOCATION__SLOT_VISIT = eINSTANCE.getSlotAllocation_SlotVisit();

		/**
		 * The meta object literal for the '<em><b>Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT_ALLOCATION__PRICE = eINSTANCE.getSlotAllocation_Price();

		/**
		 * The meta object literal for the '<em><b>Volume Transferred</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT_ALLOCATION__VOLUME_TRANSFERRED = eINSTANCE.getSlotAllocation_VolumeTransferred();

		/**
		 * The meta object literal for the '<em><b>Get Port</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT_ALLOCATION___GET_PORT = eINSTANCE.getSlotAllocation__GetPort();

		/**
		 * The meta object literal for the '<em><b>Get Local Start</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT_ALLOCATION___GET_LOCAL_START = eINSTANCE.getSlotAllocation__GetLocalStart();

		/**
		 * The meta object literal for the '<em><b>Get Local End</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT_ALLOCATION___GET_LOCAL_END = eINSTANCE.getSlotAllocation__GetLocalEnd();

		/**
		 * The meta object literal for the '<em><b>Get Contract</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT_ALLOCATION___GET_CONTRACT = eINSTANCE.getSlotAllocation__GetContract();

		/**
		 * The meta object literal for the '<em><b>Get Name</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT_ALLOCATION___GET_NAME = eINSTANCE.getSlotAllocation__GetName();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.FitnessImpl <em>Fitness</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.FitnessImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getFitness()
		 * @generated
		 */
		EClass FITNESS = eINSTANCE.getFitness();

		/**
		 * The meta object literal for the '<em><b>Fitness Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FITNESS__FITNESS_VALUE = eINSTANCE.getFitness_FitnessValue();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.PortVisitImpl <em>Port Visit</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.PortVisitImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getPortVisit()
		 * @generated
		 */
		EClass PORT_VISIT = eINSTANCE.getPortVisit();

		/**
		 * The meta object literal for the '<em><b>Port Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT_VISIT__PORT_COST = eINSTANCE.getPortVisit_PortCost();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.StartEventImpl <em>Start Event</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.StartEventImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getStartEvent()
		 * @generated
		 */
		EClass START_EVENT = eINSTANCE.getStartEvent();

		/**
		 * The meta object literal for the '<em><b>Slot Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference START_EVENT__SLOT_ALLOCATION = eINSTANCE.getStartEvent_SlotAllocation();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.EndEventImpl <em>End Event</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.EndEventImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getEndEvent()
		 * @generated
		 */
		EClass END_EVENT = eINSTANCE.getEndEvent();

		/**
		 * The meta object literal for the '<em><b>Slot Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference END_EVENT__SLOT_ALLOCATION = eINSTANCE.getEndEvent_SlotAllocation();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.FuelUnit <em>Fuel Unit</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.FuelUnit
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getFuelUnit()
		 * @generated
		 */
		EEnum FUEL_UNIT = eINSTANCE.getFuelUnit();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.Fuel <em>Fuel</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.Fuel
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getFuel()
		 * @generated
		 */
		EEnum FUEL = eINSTANCE.getFuel();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.SequenceType <em>Sequence Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.SequenceType
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getSequenceType()
		 * @generated
		 */
		EEnum SEQUENCE_TYPE = eINSTANCE.getSequenceType();

		/**
		 * The meta object literal for the '<em>Calendar</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.util.Calendar
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getCalendar()
		 * @generated
		 */
		EDataType CALENDAR = eINSTANCE.getCalendar();

		/**
		 * The meta object literal for the '<em>Iterable</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.Iterable
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getIterable()
		 * @generated
		 */
		EDataType ITERABLE = eINSTANCE.getIterable();

		/**
		 * The meta object literal for the '<em>Object</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.Object
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getObject()
		 * @generated
		 */
		EDataType OBJECT = eINSTANCE.getObject();

	}

} //SchedulePackage
