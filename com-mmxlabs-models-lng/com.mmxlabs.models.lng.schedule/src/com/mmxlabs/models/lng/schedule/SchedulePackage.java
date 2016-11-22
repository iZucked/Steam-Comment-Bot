/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_MODEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

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
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Sequences</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE__SEQUENCES = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Cargo Allocations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE__CARGO_ALLOCATIONS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Open Slot Allocations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE__OPEN_SLOT_ALLOCATIONS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Market Allocations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE__MARKET_ALLOCATIONS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Slot Allocations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE__SLOT_ALLOCATIONS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Fitnesses</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE__FITNESSES = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Unused Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE__UNUSED_ELEMENTS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Schedule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 7;

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
	int SEQUENCE = 7;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.EventImpl <em>Event</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.EventImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getEvent()
	 * @generated
	 */
	int EVENT = 8;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.SlotVisitImpl <em>Slot Visit</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.SlotVisitImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getSlotVisit()
	 * @generated
	 */
	int SLOT_VISIT = 14;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.VesselEventVisitImpl <em>Vessel Event Visit</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.VesselEventVisitImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getVesselEventVisit()
	 * @generated
	 */
	int VESSEL_EVENT_VISIT = 15;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.JourneyImpl <em>Journey</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.JourneyImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getJourney()
	 * @generated
	 */
	int JOURNEY = 11;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.IdleImpl <em>Idle</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.IdleImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getIdle()
	 * @generated
	 */
	int IDLE = 12;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.GeneratedCharterOutImpl <em>Generated Charter Out</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.GeneratedCharterOutImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getGeneratedCharterOut()
	 * @generated
	 */
	int GENERATED_CHARTER_OUT = 16;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.FuelUsageImpl <em>Fuel Usage</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.FuelUsageImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getFuelUsage()
	 * @generated
	 */
	int FUEL_USAGE = 18;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.FuelQuantityImpl <em>Fuel Quantity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.FuelQuantityImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getFuelQuantity()
	 * @generated
	 */
	int FUEL_QUANTITY = 19;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.CooldownImpl <em>Cooldown</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.CooldownImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getCooldown()
	 * @generated
	 */
	int COOLDOWN = 17;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl <em>Cargo Allocation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getCargoAllocation()
	 * @generated
	 */
	int CARGO_ALLOCATION = 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.SlotAllocationImpl <em>Slot Allocation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.SlotAllocationImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getSlotAllocation()
	 * @generated
	 */
	int SLOT_ALLOCATION = 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.FuelAmountImpl <em>Fuel Amount</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.FuelAmountImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getFuelAmount()
	 * @generated
	 */
	int FUEL_AMOUNT = 20;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.FitnessImpl <em>Fitness</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.FitnessImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getFitness()
	 * @generated
	 */
	int FITNESS = 2;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FITNESS__EXTENSIONS = MMXCorePackage.NAMED_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FITNESS__NAME = MMXCorePackage.NAMED_OBJECT__NAME;

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
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Group Profit And Loss</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__GROUP_PROFIT_AND_LOSS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>General PNL Details</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__GENERAL_PNL_DETAILS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Events</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__EVENTS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Slot Allocations</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__SLOT_ALLOCATIONS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Input Cargo</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__INPUT_CARGO = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Sequence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION__SEQUENCE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Cargo Allocation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 6;

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
	 * The operation id for the '<em>Get Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION___GET_NAME = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The number of operations of the '<em>Cargo Allocation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION_OPERATION_COUNT = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.PortVisitImpl <em>Port Visit</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.PortVisitImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getPortVisit()
	 * @generated
	 */
	int PORT_VISIT = 13;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.StartEventImpl <em>Start Event</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.StartEventImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getStartEvent()
	 * @generated
	 */
	int START_EVENT = 9;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.EndEventImpl <em>End Event</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.EndEventImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getEndEvent()
	 * @generated
	 */
	int END_EVENT = 10;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.CapacityViolationsHolderImpl <em>Capacity Violations Holder</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.CapacityViolationsHolderImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getCapacityViolationsHolder()
	 * @generated
	 */
	int CAPACITY_VIOLATIONS_HOLDER = 21;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.CapacityMapEntryImpl <em>Capacity Map Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.CapacityMapEntryImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getCapacityMapEntry()
	 * @generated
	 */
	int CAPACITY_MAP_ENTRY = 22;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.ProfitAndLossContainerImpl <em>Profit And Loss Container</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.ProfitAndLossContainerImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getProfitAndLossContainer()
	 * @generated
	 */
	int PROFIT_AND_LOSS_CONTAINER = 23;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_AND_LOSS_CONTAINER__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Group Profit And Loss</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>General PNL Details</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_AND_LOSS_CONTAINER__GENERAL_PNL_DETAILS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Profit And Loss Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_AND_LOSS_CONTAINER_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_AND_LOSS_CONTAINER___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.MMX_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_AND_LOSS_CONTAINER___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.MMX_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_AND_LOSS_CONTAINER___ECONTAINER_OP = MMXCorePackage.MMX_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>Profit And Loss Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_AND_LOSS_CONTAINER_OPERATION_COUNT = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.MarketAllocationImpl <em>Market Allocation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.MarketAllocationImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getMarketAllocation()
	 * @generated
	 */
	int MARKET_ALLOCATION = 4;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKET_ALLOCATION__EXTENSIONS = PROFIT_AND_LOSS_CONTAINER__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Group Profit And Loss</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKET_ALLOCATION__GROUP_PROFIT_AND_LOSS = PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS;

	/**
	 * The feature id for the '<em><b>General PNL Details</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKET_ALLOCATION__GENERAL_PNL_DETAILS = PROFIT_AND_LOSS_CONTAINER__GENERAL_PNL_DETAILS;

	/**
	 * The feature id for the '<em><b>Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKET_ALLOCATION__SLOT = PROFIT_AND_LOSS_CONTAINER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKET_ALLOCATION__MARKET = PROFIT_AND_LOSS_CONTAINER_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Slot Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKET_ALLOCATION__SLOT_ALLOCATION = PROFIT_AND_LOSS_CONTAINER_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKET_ALLOCATION__PRICE = PROFIT_AND_LOSS_CONTAINER_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Slot Visit</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKET_ALLOCATION__SLOT_VISIT = PROFIT_AND_LOSS_CONTAINER_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Market Allocation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKET_ALLOCATION_FEATURE_COUNT = PROFIT_AND_LOSS_CONTAINER_FEATURE_COUNT + 5;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKET_ALLOCATION___GET_UNSET_VALUE__ESTRUCTURALFEATURE = PROFIT_AND_LOSS_CONTAINER___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKET_ALLOCATION___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = PROFIT_AND_LOSS_CONTAINER___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKET_ALLOCATION___ECONTAINER_OP = PROFIT_AND_LOSS_CONTAINER___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>Market Allocation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKET_ALLOCATION_OPERATION_COUNT = PROFIT_AND_LOSS_CONTAINER_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.OpenSlotAllocationImpl <em>Open Slot Allocation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.OpenSlotAllocationImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getOpenSlotAllocation()
	 * @generated
	 */
	int OPEN_SLOT_ALLOCATION = 5;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPEN_SLOT_ALLOCATION__EXTENSIONS = PROFIT_AND_LOSS_CONTAINER__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Group Profit And Loss</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPEN_SLOT_ALLOCATION__GROUP_PROFIT_AND_LOSS = PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS;

	/**
	 * The feature id for the '<em><b>General PNL Details</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPEN_SLOT_ALLOCATION__GENERAL_PNL_DETAILS = PROFIT_AND_LOSS_CONTAINER__GENERAL_PNL_DETAILS;

	/**
	 * The feature id for the '<em><b>Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPEN_SLOT_ALLOCATION__SLOT = PROFIT_AND_LOSS_CONTAINER_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Open Slot Allocation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPEN_SLOT_ALLOCATION_FEATURE_COUNT = PROFIT_AND_LOSS_CONTAINER_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPEN_SLOT_ALLOCATION___GET_UNSET_VALUE__ESTRUCTURALFEATURE = PROFIT_AND_LOSS_CONTAINER___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPEN_SLOT_ALLOCATION___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = PROFIT_AND_LOSS_CONTAINER___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPEN_SLOT_ALLOCATION___ECONTAINER_OP = PROFIT_AND_LOSS_CONTAINER___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>Open Slot Allocation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPEN_SLOT_ALLOCATION_OPERATION_COUNT = PROFIT_AND_LOSS_CONTAINER_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

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
	 * The feature id for the '<em><b>Market Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION__MARKET_ALLOCATION = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Slot Visit</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION__SLOT_VISIT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION__PRICE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Volume Transferred</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION__VOLUME_TRANSFERRED = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Energy Transferred</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION__ENERGY_TRANSFERRED = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Cv</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION__CV = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Volume Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION__VOLUME_VALUE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Exposures</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION__EXPOSURES = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Physical Volume Transferred</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION__PHYSICAL_VOLUME_TRANSFERRED = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>Physical Energy Transferred</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION__PHYSICAL_ENERGY_TRANSFERRED = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 12;

	/**
	 * The feature id for the '<em><b>Slot Allocation Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION__SLOT_ALLOCATION_TYPE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 13;

	/**
	 * The number of structural features of the '<em>Slot Allocation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 14;

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
	 * The operation id for the '<em>Get Contract</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION___GET_CONTRACT = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION___GET_NAME = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 2;

	/**
	 * The number of operations of the '<em>Slot Allocation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ALLOCATION_OPERATION_COUNT = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Events</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE__EVENTS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Vessel Availability</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE__VESSEL_AVAILABILITY = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Charter In Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE__CHARTER_IN_MARKET = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Fitnesses</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE__FITNESSES = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Spot Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE__SPOT_INDEX = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Sequence Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE__SEQUENCE_TYPE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Sequence</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 6;

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
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

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
	 * The feature id for the '<em><b>Charter Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT__CHARTER_COST = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Heel At Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT__HEEL_AT_START = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Heel At End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT__HEEL_AT_END = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The number of structural features of the '<em>Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 9;

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
	 * The operation id for the '<em>Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT___TYPE = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 2;

	/**
	 * The operation id for the '<em>Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT___NAME = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 3;

	/**
	 * The number of operations of the '<em>Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_OPERATION_COUNT = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT__EXTENSIONS = EVENT__EXTENSIONS;

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
	 * The feature id for the '<em><b>Charter Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT__CHARTER_COST = EVENT__CHARTER_COST;

	/**
	 * The feature id for the '<em><b>Heel At Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT__HEEL_AT_START = EVENT__HEEL_AT_START;

	/**
	 * The feature id for the '<em><b>Heel At End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT__HEEL_AT_END = EVENT__HEEL_AT_END;

	/**
	 * The feature id for the '<em><b>Fuels</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT__FUELS = EVENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Violations</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT__VIOLATIONS = EVENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Port Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT__PORT_COST = EVENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Lateness</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT__LATENESS = EVENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Group Profit And Loss</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT__GROUP_PROFIT_AND_LOSS = EVENT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>General PNL Details</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT__GENERAL_PNL_DETAILS = EVENT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Events</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT__EVENTS = EVENT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Slot Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT__SLOT_ALLOCATION = EVENT_FEATURE_COUNT + 7;

	/**
	 * The number of structural features of the '<em>Start Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT_FEATURE_COUNT = EVENT_FEATURE_COUNT + 8;

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
	 * The operation id for the '<em>Get Fuel Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT___GET_FUEL_COST = EVENT_OPERATION_COUNT + 0;

	/**
	 * The number of operations of the '<em>Start Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int START_EVENT_OPERATION_COUNT = EVENT_OPERATION_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT__EXTENSIONS = EVENT__EXTENSIONS;

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
	 * The feature id for the '<em><b>Charter Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT__CHARTER_COST = EVENT__CHARTER_COST;

	/**
	 * The feature id for the '<em><b>Heel At Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT__HEEL_AT_START = EVENT__HEEL_AT_START;

	/**
	 * The feature id for the '<em><b>Heel At End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT__HEEL_AT_END = EVENT__HEEL_AT_END;

	/**
	 * The feature id for the '<em><b>Fuels</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT__FUELS = EVENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Violations</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT__VIOLATIONS = EVENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Port Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT__PORT_COST = EVENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Lateness</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT__LATENESS = EVENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Group Profit And Loss</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT__GROUP_PROFIT_AND_LOSS = EVENT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>General PNL Details</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT__GENERAL_PNL_DETAILS = EVENT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Events</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT__EVENTS = EVENT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Slot Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT__SLOT_ALLOCATION = EVENT_FEATURE_COUNT + 7;

	/**
	 * The number of structural features of the '<em>End Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT_FEATURE_COUNT = EVENT_FEATURE_COUNT + 8;

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
	 * The operation id for the '<em>Get Fuel Cost</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT___GET_FUEL_COST = EVENT_OPERATION_COUNT + 0;

	/**
	 * The number of operations of the '<em>End Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int END_EVENT_OPERATION_COUNT = EVENT_OPERATION_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__EXTENSIONS = EVENT__EXTENSIONS;

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
	 * The feature id for the '<em><b>Charter Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__CHARTER_COST = EVENT__CHARTER_COST;

	/**
	 * The feature id for the '<em><b>Heel At Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__HEEL_AT_START = EVENT__HEEL_AT_START;

	/**
	 * The feature id for the '<em><b>Heel At End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JOURNEY__HEEL_AT_END = EVENT__HEEL_AT_END;

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
	 * The feature id for the '<em><b>Route</b></em>' reference.
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
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE__EXTENSIONS = EVENT__EXTENSIONS;

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
	 * The feature id for the '<em><b>Charter Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE__CHARTER_COST = EVENT__CHARTER_COST;

	/**
	 * The feature id for the '<em><b>Heel At Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE__HEEL_AT_START = EVENT__HEEL_AT_START;

	/**
	 * The feature id for the '<em><b>Heel At End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDLE__HEEL_AT_END = EVENT__HEEL_AT_END;

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
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT__EXTENSIONS = EVENT__EXTENSIONS;

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
	 * The feature id for the '<em><b>Charter Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT__CHARTER_COST = EVENT__CHARTER_COST;

	/**
	 * The feature id for the '<em><b>Heel At Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT__HEEL_AT_START = EVENT__HEEL_AT_START;

	/**
	 * The feature id for the '<em><b>Heel At End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT__HEEL_AT_END = EVENT__HEEL_AT_END;

	/**
	 * The feature id for the '<em><b>Violations</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT__VIOLATIONS = EVENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Port Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT__PORT_COST = EVENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Lateness</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT__LATENESS = EVENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Port Visit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT_FEATURE_COUNT = EVENT_FEATURE_COUNT + 3;

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
	 * The number of operations of the '<em>Port Visit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT_OPERATION_COUNT = EVENT_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT__EXTENSIONS = EVENT__EXTENSIONS;

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
	 * The feature id for the '<em><b>Charter Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT__CHARTER_COST = EVENT__CHARTER_COST;

	/**
	 * The feature id for the '<em><b>Heel At Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT__HEEL_AT_START = EVENT__HEEL_AT_START;

	/**
	 * The feature id for the '<em><b>Heel At End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT__HEEL_AT_END = EVENT__HEEL_AT_END;

	/**
	 * The feature id for the '<em><b>Fuels</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT__FUELS = EVENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Violations</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT__VIOLATIONS = EVENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Port Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT__PORT_COST = EVENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Lateness</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT__LATENESS = EVENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Slot Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT__SLOT_ALLOCATION = EVENT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Slot Visit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_VISIT_FEATURE_COUNT = EVENT_FEATURE_COUNT + 5;

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
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT__EXTENSIONS = EVENT__EXTENSIONS;

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
	 * The feature id for the '<em><b>Charter Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT__CHARTER_COST = EVENT__CHARTER_COST;

	/**
	 * The feature id for the '<em><b>Heel At Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT__HEEL_AT_START = EVENT__HEEL_AT_START;

	/**
	 * The feature id for the '<em><b>Heel At End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT__HEEL_AT_END = EVENT__HEEL_AT_END;

	/**
	 * The feature id for the '<em><b>Violations</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT__VIOLATIONS = EVENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Port Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT__PORT_COST = EVENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Lateness</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT__LATENESS = EVENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Group Profit And Loss</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT__GROUP_PROFIT_AND_LOSS = EVENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>General PNL Details</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT__GENERAL_PNL_DETAILS = EVENT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Events</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT__EVENTS = EVENT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Vessel Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT__VESSEL_EVENT = EVENT_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Vessel Event Visit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT_FEATURE_COUNT = EVENT_FEATURE_COUNT + 7;

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
	 * The number of operations of the '<em>Vessel Event Visit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_EVENT_VISIT_OPERATION_COUNT = EVENT_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT__EXTENSIONS = PORT_VISIT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT__START = PORT_VISIT__START;

	/**
	 * The feature id for the '<em><b>End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT__END = PORT_VISIT__END;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT__PORT = PORT_VISIT__PORT;

	/**
	 * The feature id for the '<em><b>Previous Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT__PREVIOUS_EVENT = PORT_VISIT__PREVIOUS_EVENT;

	/**
	 * The feature id for the '<em><b>Next Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT__NEXT_EVENT = PORT_VISIT__NEXT_EVENT;

	/**
	 * The feature id for the '<em><b>Sequence</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT__SEQUENCE = PORT_VISIT__SEQUENCE;

	/**
	 * The feature id for the '<em><b>Charter Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT__CHARTER_COST = PORT_VISIT__CHARTER_COST;

	/**
	 * The feature id for the '<em><b>Heel At Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT__HEEL_AT_START = PORT_VISIT__HEEL_AT_START;

	/**
	 * The feature id for the '<em><b>Heel At End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT__HEEL_AT_END = PORT_VISIT__HEEL_AT_END;

	/**
	 * The feature id for the '<em><b>Violations</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT__VIOLATIONS = PORT_VISIT__VIOLATIONS;

	/**
	 * The feature id for the '<em><b>Port Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT__PORT_COST = PORT_VISIT__PORT_COST;

	/**
	 * The feature id for the '<em><b>Lateness</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT__LATENESS = PORT_VISIT__LATENESS;

	/**
	 * The feature id for the '<em><b>Group Profit And Loss</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT__GROUP_PROFIT_AND_LOSS = PORT_VISIT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>General PNL Details</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT__GENERAL_PNL_DETAILS = PORT_VISIT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Events</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT__EVENTS = PORT_VISIT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Revenue</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT__REVENUE = PORT_VISIT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Generated Charter Out</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT_FEATURE_COUNT = PORT_VISIT_FEATURE_COUNT + 4;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = PORT_VISIT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = PORT_VISIT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT___ECONTAINER_OP = PORT_VISIT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Time Zone</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT___GET_TIME_ZONE__EATTRIBUTE = PORT_VISIT___GET_TIME_ZONE__EATTRIBUTE;

	/**
	 * The operation id for the '<em>Get Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT___GET_DURATION = PORT_VISIT___GET_DURATION;

	/**
	 * The operation id for the '<em>Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT___TYPE = PORT_VISIT___TYPE;

	/**
	 * The operation id for the '<em>Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT___NAME = PORT_VISIT___NAME;

	/**
	 * The number of operations of the '<em>Generated Charter Out</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERATED_CHARTER_OUT_OPERATION_COUNT = PORT_VISIT_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN__EXTENSIONS = EVENT__EXTENSIONS;

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
	 * The feature id for the '<em><b>Charter Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN__CHARTER_COST = EVENT__CHARTER_COST;

	/**
	 * The feature id for the '<em><b>Heel At Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN__HEEL_AT_START = EVENT__HEEL_AT_START;

	/**
	 * The feature id for the '<em><b>Heel At End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COOLDOWN__HEEL_AT_END = EVENT__HEEL_AT_END;

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
	 * The number of operations of the '<em>Fuel Amount</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_AMOUNT_OPERATION_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAPACITY_VIOLATIONS_HOLDER__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Violations</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAPACITY_VIOLATIONS_HOLDER__VIOLATIONS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Capacity Violations Holder</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAPACITY_VIOLATIONS_HOLDER_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAPACITY_VIOLATIONS_HOLDER___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.MMX_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAPACITY_VIOLATIONS_HOLDER___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.MMX_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAPACITY_VIOLATIONS_HOLDER___ECONTAINER_OP = MMXCorePackage.MMX_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>Capacity Violations Holder</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAPACITY_VIOLATIONS_HOLDER_OPERATION_COUNT = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAPACITY_MAP_ENTRY__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAPACITY_MAP_ENTRY__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Capacity Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAPACITY_MAP_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Capacity Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAPACITY_MAP_ENTRY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.GroupProfitAndLossImpl <em>Group Profit And Loss</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.GroupProfitAndLossImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getGroupProfitAndLoss()
	 * @generated
	 */
	int GROUP_PROFIT_AND_LOSS = 24;

	/**
	 * The feature id for the '<em><b>Profit And Loss</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_PROFIT_AND_LOSS__PROFIT_AND_LOSS = 0;

	/**
	 * The feature id for the '<em><b>Profit And Loss Pre Tax</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_PROFIT_AND_LOSS__PROFIT_AND_LOSS_PRE_TAX = 1;

	/**
	 * The feature id for the '<em><b>Tax Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_PROFIT_AND_LOSS__TAX_VALUE = 2;

	/**
	 * The feature id for the '<em><b>Entity Profit And Losses</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_PROFIT_AND_LOSS__ENTITY_PROFIT_AND_LOSSES = 3;

	/**
	 * The number of structural features of the '<em>Group Profit And Loss</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_PROFIT_AND_LOSS_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Group Profit And Loss</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_PROFIT_AND_LOSS_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.EntityProfitAndLossImpl <em>Entity Profit And Loss</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.EntityProfitAndLossImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getEntityProfitAndLoss()
	 * @generated
	 */
	int ENTITY_PROFIT_AND_LOSS = 25;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_PROFIT_AND_LOSS__ENTITY = 0;

	/**
	 * The feature id for the '<em><b>Entity Book</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_PROFIT_AND_LOSS__ENTITY_BOOK = 1;

	/**
	 * The feature id for the '<em><b>Profit And Loss</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_PROFIT_AND_LOSS__PROFIT_AND_LOSS = 2;

	/**
	 * The feature id for the '<em><b>Profit And Loss Pre Tax</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_PROFIT_AND_LOSS__PROFIT_AND_LOSS_PRE_TAX = 3;

	/**
	 * The number of structural features of the '<em>Entity Profit And Loss</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_PROFIT_AND_LOSS_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Entity Profit And Loss</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_PROFIT_AND_LOSS_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.EntityPNLDetailsImpl <em>Entity PNL Details</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.EntityPNLDetailsImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getEntityPNLDetails()
	 * @generated
	 */
	int ENTITY_PNL_DETAILS = 26;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.GeneralPNLDetailsImpl <em>General PNL Details</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.GeneralPNLDetailsImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getGeneralPNLDetails()
	 * @generated
	 */
	int GENERAL_PNL_DETAILS = 28;

	/**
	 * The number of structural features of the '<em>General PNL Details</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERAL_PNL_DETAILS_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>General PNL Details</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERAL_PNL_DETAILS_OPERATION_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_PNL_DETAILS__ENTITY = GENERAL_PNL_DETAILS_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>General PNL Details</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_PNL_DETAILS__GENERAL_PNL_DETAILS = GENERAL_PNL_DETAILS_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Entity PNL Details</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_PNL_DETAILS_FEATURE_COUNT = GENERAL_PNL_DETAILS_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Entity PNL Details</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_PNL_DETAILS_OPERATION_COUNT = GENERAL_PNL_DETAILS_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.SlotPNLDetailsImpl <em>Slot PNL Details</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.SlotPNLDetailsImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getSlotPNLDetails()
	 * @generated
	 */
	int SLOT_PNL_DETAILS = 27;

	/**
	 * The feature id for the '<em><b>Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_PNL_DETAILS__SLOT = GENERAL_PNL_DETAILS_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>General PNL Details</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_PNL_DETAILS__GENERAL_PNL_DETAILS = GENERAL_PNL_DETAILS_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Slot PNL Details</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_PNL_DETAILS_FEATURE_COUNT = GENERAL_PNL_DETAILS_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Slot PNL Details</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_PNL_DETAILS_OPERATION_COUNT = GENERAL_PNL_DETAILS_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.BasicSlotPNLDetailsImpl <em>Basic Slot PNL Details</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.BasicSlotPNLDetailsImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getBasicSlotPNLDetails()
	 * @generated
	 */
	int BASIC_SLOT_PNL_DETAILS = 29;

	/**
	 * The feature id for the '<em><b>Extra Shipping PNL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASIC_SLOT_PNL_DETAILS__EXTRA_SHIPPING_PNL = GENERAL_PNL_DETAILS_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Additional PNL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASIC_SLOT_PNL_DETAILS__ADDITIONAL_PNL = GENERAL_PNL_DETAILS_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Cancellation Fees</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASIC_SLOT_PNL_DETAILS__CANCELLATION_FEES = GENERAL_PNL_DETAILS_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Hedging Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASIC_SLOT_PNL_DETAILS__HEDGING_VALUE = GENERAL_PNL_DETAILS_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Misc Costs Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASIC_SLOT_PNL_DETAILS__MISC_COSTS_VALUE = GENERAL_PNL_DETAILS_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Extra Upside PNL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASIC_SLOT_PNL_DETAILS__EXTRA_UPSIDE_PNL = GENERAL_PNL_DETAILS_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Basic Slot PNL Details</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASIC_SLOT_PNL_DETAILS_FEATURE_COUNT = GENERAL_PNL_DETAILS_FEATURE_COUNT + 6;

	/**
	 * The number of operations of the '<em>Basic Slot PNL Details</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASIC_SLOT_PNL_DETAILS_OPERATION_COUNT = GENERAL_PNL_DETAILS_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.EventGroupingImpl <em>Event Grouping</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.EventGroupingImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getEventGrouping()
	 * @generated
	 */
	int EVENT_GROUPING = 30;

	/**
	 * The feature id for the '<em><b>Events</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_GROUPING__EVENTS = 0;

	/**
	 * The number of structural features of the '<em>Event Grouping</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_GROUPING_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Event Grouping</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_GROUPING_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.PortVisitLatenessImpl <em>Port Visit Lateness</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.PortVisitLatenessImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getPortVisitLateness()
	 * @generated
	 */
	int PORT_VISIT_LATENESS = 31;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT_LATENESS__TYPE = 0;

	/**
	 * The feature id for the '<em><b>Lateness In Hours</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT_LATENESS__LATENESS_IN_HOURS = 1;

	/**
	 * The number of structural features of the '<em>Port Visit Lateness</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT_LATENESS_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Port Visit Lateness</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_VISIT_LATENESS_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.impl.ExposureDetailImpl <em>Exposure Detail</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.impl.ExposureDetailImpl
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getExposureDetail()
	 * @generated
	 */
	int EXPOSURE_DETAIL = 32;

	/**
	 * The feature id for the '<em><b>Index</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPOSURE_DETAIL__INDEX = 0;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPOSURE_DETAIL__DATE = 1;

	/**
	 * The feature id for the '<em><b>Volume In MMBTU</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPOSURE_DETAIL__VOLUME_IN_MMBTU = 2;

	/**
	 * The feature id for the '<em><b>Volume In Native Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPOSURE_DETAIL__VOLUME_IN_NATIVE_UNITS = 3;

	/**
	 * The feature id for the '<em><b>Unit Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPOSURE_DETAIL__UNIT_PRICE = 4;

	/**
	 * The feature id for the '<em><b>Native Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPOSURE_DETAIL__NATIVE_VALUE = 5;

	/**
	 * The feature id for the '<em><b>Volume Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPOSURE_DETAIL__VOLUME_UNIT = 6;

	/**
	 * The feature id for the '<em><b>Currency Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPOSURE_DETAIL__CURRENCY_UNIT = 7;

	/**
	 * The number of structural features of the '<em>Exposure Detail</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPOSURE_DETAIL_FEATURE_COUNT = 8;

	/**
	 * The number of operations of the '<em>Exposure Detail</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPOSURE_DETAIL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.FuelUnit <em>Fuel Unit</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.FuelUnit
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getFuelUnit()
	 * @generated
	 */
	int FUEL_UNIT = 35;


	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.Fuel <em>Fuel</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.Fuel
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getFuel()
	 * @generated
	 */
	int FUEL = 34;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.SequenceType <em>Sequence Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.SequenceType
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getSequenceType()
	 * @generated
	 */
	int SEQUENCE_TYPE = 33;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.CapacityViolationType <em>Capacity Violation Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.CapacityViolationType
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getCapacityViolationType()
	 * @generated
	 */
	int CAPACITY_VIOLATION_TYPE = 36;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.PortVisitLatenessType <em>Port Visit Lateness Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.PortVisitLatenessType
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getPortVisitLatenessType()
	 * @generated
	 */
	int PORT_VISIT_LATENESS_TYPE = 37;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.schedule.SlotAllocationType <em>Slot Allocation Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.schedule.SlotAllocationType
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getSlotAllocationType()
	 * @generated
	 */
	int SLOT_ALLOCATION_TYPE = 38;

	/**
	 * The meta object id for the '<em>Calendar</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.util.Calendar
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getCalendar()
	 * @generated
	 */
	int CALENDAR = 39;


	/**
	 * The meta object id for the '<em>Iterable</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.Iterable
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getIterable()
	 * @generated
	 */
	int ITERABLE = 40;


	/**
	 * The meta object id for the '<em>Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.Object
	 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getObject()
	 * @generated
	 */
	int OBJECT = 41;


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
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.schedule.Schedule#getOpenSlotAllocations <em>Open Slot Allocations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Open Slot Allocations</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Schedule#getOpenSlotAllocations()
	 * @see #getSchedule()
	 * @generated
	 */
	EReference getSchedule_OpenSlotAllocations();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.schedule.Schedule#getMarketAllocations <em>Market Allocations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Market Allocations</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Schedule#getMarketAllocations()
	 * @see #getSchedule()
	 * @generated
	 */
	EReference getSchedule_MarketAllocations();

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
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.Sequence#getVesselAvailability <em>Vessel Availability</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel Availability</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Sequence#getVesselAvailability()
	 * @see #getSequence()
	 * @generated
	 */
	EReference getSequence_VesselAvailability();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.Sequence#getCharterInMarket <em>Charter In Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Charter In Market</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Sequence#getCharterInMarket()
	 * @see #getSequence()
	 * @generated
	 */
	EReference getSequence_CharterInMarket();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.Event#getCharterCost <em>Charter Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Charter Cost</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Event#getCharterCost()
	 * @see #getEvent()
	 * @generated
	 */
	EAttribute getEvent_CharterCost();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.Event#getHeelAtStart <em>Heel At Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Heel At Start</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Event#getHeelAtStart()
	 * @see #getEvent()
	 * @generated
	 */
	EAttribute getEvent_HeelAtStart();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.Event#getHeelAtEnd <em>Heel At End</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Heel At End</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Event#getHeelAtEnd()
	 * @see #getEvent()
	 * @generated
	 */
	EAttribute getEvent_HeelAtEnd();

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
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.Journey#getRoute <em>Route</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Route</em>'.
	 * @see com.mmxlabs.models.lng.schedule.Journey#getRoute()
	 * @see #getJourney()
	 * @generated
	 */
	EReference getJourney_Route();

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
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Generated Charter Out</em>'.
	 * @see com.mmxlabs.models.lng.schedule.GeneratedCharterOut
	 * @generated
	 */
	EClass getGeneratedCharterOut();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.GeneratedCharterOut#getRevenue <em>Revenue</em>}'.
	 * <!-- begin-user-doc -->
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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.MarketAllocation <em>Market Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Market Allocation</em>'.
	 * @see com.mmxlabs.models.lng.schedule.MarketAllocation
	 * @generated
	 */
	EClass getMarketAllocation();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.MarketAllocation#getSlot <em>Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Slot</em>'.
	 * @see com.mmxlabs.models.lng.schedule.MarketAllocation#getSlot()
	 * @see #getMarketAllocation()
	 * @generated
	 */
	EReference getMarketAllocation_Slot();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.MarketAllocation#getMarket <em>Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Market</em>'.
	 * @see com.mmxlabs.models.lng.schedule.MarketAllocation#getMarket()
	 * @see #getMarketAllocation()
	 * @generated
	 */
	EReference getMarketAllocation_Market();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.MarketAllocation#getSlotAllocation <em>Slot Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Slot Allocation</em>'.
	 * @see com.mmxlabs.models.lng.schedule.MarketAllocation#getSlotAllocation()
	 * @see #getMarketAllocation()
	 * @generated
	 */
	EReference getMarketAllocation_SlotAllocation();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.MarketAllocation#getPrice <em>Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price</em>'.
	 * @see com.mmxlabs.models.lng.schedule.MarketAllocation#getPrice()
	 * @see #getMarketAllocation()
	 * @generated
	 */
	EAttribute getMarketAllocation_Price();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.schedule.MarketAllocation#getSlotVisit <em>Slot Visit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Slot Visit</em>'.
	 * @see com.mmxlabs.models.lng.schedule.MarketAllocation#getSlotVisit()
	 * @see #getMarketAllocation()
	 * @generated
	 */
	EReference getMarketAllocation_SlotVisit();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.OpenSlotAllocation <em>Open Slot Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Open Slot Allocation</em>'.
	 * @see com.mmxlabs.models.lng.schedule.OpenSlotAllocation
	 * @generated
	 */
	EClass getOpenSlotAllocation();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.OpenSlotAllocation#getSlot <em>Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Slot</em>'.
	 * @see com.mmxlabs.models.lng.schedule.OpenSlotAllocation#getSlot()
	 * @see #getOpenSlotAllocation()
	 * @generated
	 */
	EReference getOpenSlotAllocation_Slot();

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
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getMarketAllocation <em>Market Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Market Allocation</em>'.
	 * @see com.mmxlabs.models.lng.schedule.SlotAllocation#getMarketAllocation()
	 * @see #getSlotAllocation()
	 * @generated
	 */
	EReference getSlotAllocation_MarketAllocation();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getEnergyTransferred <em>Energy Transferred</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Energy Transferred</em>'.
	 * @see com.mmxlabs.models.lng.schedule.SlotAllocation#getEnergyTransferred()
	 * @see #getSlotAllocation()
	 * @generated
	 */
	EAttribute getSlotAllocation_EnergyTransferred();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getCv <em>Cv</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cv</em>'.
	 * @see com.mmxlabs.models.lng.schedule.SlotAllocation#getCv()
	 * @see #getSlotAllocation()
	 * @generated
	 */
	EAttribute getSlotAllocation_Cv();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getVolumeValue <em>Volume Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volume Value</em>'.
	 * @see com.mmxlabs.models.lng.schedule.SlotAllocation#getVolumeValue()
	 * @see #getSlotAllocation()
	 * @generated
	 */
	EAttribute getSlotAllocation_VolumeValue();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getExposures <em>Exposures</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Exposures</em>'.
	 * @see com.mmxlabs.models.lng.schedule.SlotAllocation#getExposures()
	 * @see #getSlotAllocation()
	 * @generated
	 */
	EReference getSlotAllocation_Exposures();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getPhysicalVolumeTransferred <em>Physical Volume Transferred</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Physical Volume Transferred</em>'.
	 * @see com.mmxlabs.models.lng.schedule.SlotAllocation#getPhysicalVolumeTransferred()
	 * @see #getSlotAllocation()
	 * @generated
	 */
	EAttribute getSlotAllocation_PhysicalVolumeTransferred();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getPhysicalEnergyTransferred <em>Physical Energy Transferred</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Physical Energy Transferred</em>'.
	 * @see com.mmxlabs.models.lng.schedule.SlotAllocation#getPhysicalEnergyTransferred()
	 * @see #getSlotAllocation()
	 * @generated
	 */
	EAttribute getSlotAllocation_PhysicalEnergyTransferred();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getSlotAllocationType <em>Slot Allocation Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Slot Allocation Type</em>'.
	 * @see com.mmxlabs.models.lng.schedule.SlotAllocation#getSlotAllocationType()
	 * @see #getSlotAllocation()
	 * @generated
	 */
	EAttribute getSlotAllocation_SlotAllocationType();

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
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.schedule.PortVisit#getLateness <em>Lateness</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Lateness</em>'.
	 * @see com.mmxlabs.models.lng.schedule.PortVisit#getLateness()
	 * @see #getPortVisit()
	 * @generated
	 */
	EReference getPortVisit_Lateness();

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.CapacityViolationsHolder <em>Capacity Violations Holder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Capacity Violations Holder</em>'.
	 * @see com.mmxlabs.models.lng.schedule.CapacityViolationsHolder
	 * @generated
	 */
	EClass getCapacityViolationsHolder();

	/**
	 * Returns the meta object for the map '{@link com.mmxlabs.models.lng.schedule.CapacityViolationsHolder#getViolations <em>Violations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Violations</em>'.
	 * @see com.mmxlabs.models.lng.schedule.CapacityViolationsHolder#getViolations()
	 * @see #getCapacityViolationsHolder()
	 * @generated
	 */
	EReference getCapacityViolationsHolder_Violations();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>Capacity Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Capacity Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="com.mmxlabs.models.lng.schedule.CapacityViolationType"
	 *        valueDataType="org.eclipse.emf.ecore.ELongObject"
	 * @generated
	 */
	EClass getCapacityMapEntry();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getCapacityMapEntry()
	 * @generated
	 */
	EAttribute getCapacityMapEntry_Key();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getCapacityMapEntry()
	 * @generated
	 */
	EAttribute getCapacityMapEntry_Value();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.ProfitAndLossContainer <em>Profit And Loss Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Profit And Loss Container</em>'.
	 * @see com.mmxlabs.models.lng.schedule.ProfitAndLossContainer
	 * @generated
	 */
	EClass getProfitAndLossContainer();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.schedule.ProfitAndLossContainer#getGroupProfitAndLoss <em>Group Profit And Loss</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Group Profit And Loss</em>'.
	 * @see com.mmxlabs.models.lng.schedule.ProfitAndLossContainer#getGroupProfitAndLoss()
	 * @see #getProfitAndLossContainer()
	 * @generated
	 */
	EReference getProfitAndLossContainer_GroupProfitAndLoss();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.schedule.ProfitAndLossContainer#getGeneralPNLDetails <em>General PNL Details</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>General PNL Details</em>'.
	 * @see com.mmxlabs.models.lng.schedule.ProfitAndLossContainer#getGeneralPNLDetails()
	 * @see #getProfitAndLossContainer()
	 * @generated
	 */
	EReference getProfitAndLossContainer_GeneralPNLDetails();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.GroupProfitAndLoss <em>Group Profit And Loss</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Group Profit And Loss</em>'.
	 * @see com.mmxlabs.models.lng.schedule.GroupProfitAndLoss
	 * @generated
	 */
	EClass getGroupProfitAndLoss();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.GroupProfitAndLoss#getProfitAndLoss <em>Profit And Loss</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Profit And Loss</em>'.
	 * @see com.mmxlabs.models.lng.schedule.GroupProfitAndLoss#getProfitAndLoss()
	 * @see #getGroupProfitAndLoss()
	 * @generated
	 */
	EAttribute getGroupProfitAndLoss_ProfitAndLoss();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.GroupProfitAndLoss#getProfitAndLossPreTax <em>Profit And Loss Pre Tax</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Profit And Loss Pre Tax</em>'.
	 * @see com.mmxlabs.models.lng.schedule.GroupProfitAndLoss#getProfitAndLossPreTax()
	 * @see #getGroupProfitAndLoss()
	 * @generated
	 */
	EAttribute getGroupProfitAndLoss_ProfitAndLossPreTax();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.GroupProfitAndLoss#getTaxValue <em>Tax Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Tax Value</em>'.
	 * @see com.mmxlabs.models.lng.schedule.GroupProfitAndLoss#getTaxValue()
	 * @see #getGroupProfitAndLoss()
	 * @generated
	 */
	EAttribute getGroupProfitAndLoss_TaxValue();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.schedule.GroupProfitAndLoss#getEntityProfitAndLosses <em>Entity Profit And Losses</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Entity Profit And Losses</em>'.
	 * @see com.mmxlabs.models.lng.schedule.GroupProfitAndLoss#getEntityProfitAndLosses()
	 * @see #getGroupProfitAndLoss()
	 * @generated
	 */
	EReference getGroupProfitAndLoss_EntityProfitAndLosses();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.EntityProfitAndLoss <em>Entity Profit And Loss</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Entity Profit And Loss</em>'.
	 * @see com.mmxlabs.models.lng.schedule.EntityProfitAndLoss
	 * @generated
	 */
	EClass getEntityProfitAndLoss();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.EntityProfitAndLoss#getEntity <em>Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Entity</em>'.
	 * @see com.mmxlabs.models.lng.schedule.EntityProfitAndLoss#getEntity()
	 * @see #getEntityProfitAndLoss()
	 * @generated
	 */
	EReference getEntityProfitAndLoss_Entity();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.EntityProfitAndLoss#getEntityBook <em>Entity Book</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Entity Book</em>'.
	 * @see com.mmxlabs.models.lng.schedule.EntityProfitAndLoss#getEntityBook()
	 * @see #getEntityProfitAndLoss()
	 * @generated
	 */
	EReference getEntityProfitAndLoss_EntityBook();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.EntityProfitAndLoss#getProfitAndLoss <em>Profit And Loss</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Profit And Loss</em>'.
	 * @see com.mmxlabs.models.lng.schedule.EntityProfitAndLoss#getProfitAndLoss()
	 * @see #getEntityProfitAndLoss()
	 * @generated
	 */
	EAttribute getEntityProfitAndLoss_ProfitAndLoss();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.EntityProfitAndLoss#getProfitAndLossPreTax <em>Profit And Loss Pre Tax</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Profit And Loss Pre Tax</em>'.
	 * @see com.mmxlabs.models.lng.schedule.EntityProfitAndLoss#getProfitAndLossPreTax()
	 * @see #getEntityProfitAndLoss()
	 * @generated
	 */
	EAttribute getEntityProfitAndLoss_ProfitAndLossPreTax();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.EntityPNLDetails <em>Entity PNL Details</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Entity PNL Details</em>'.
	 * @see com.mmxlabs.models.lng.schedule.EntityPNLDetails
	 * @generated
	 */
	EClass getEntityPNLDetails();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.EntityPNLDetails#getEntity <em>Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Entity</em>'.
	 * @see com.mmxlabs.models.lng.schedule.EntityPNLDetails#getEntity()
	 * @see #getEntityPNLDetails()
	 * @generated
	 */
	EReference getEntityPNLDetails_Entity();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.schedule.EntityPNLDetails#getGeneralPNLDetails <em>General PNL Details</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>General PNL Details</em>'.
	 * @see com.mmxlabs.models.lng.schedule.EntityPNLDetails#getGeneralPNLDetails()
	 * @see #getEntityPNLDetails()
	 * @generated
	 */
	EReference getEntityPNLDetails_GeneralPNLDetails();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.SlotPNLDetails <em>Slot PNL Details</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Slot PNL Details</em>'.
	 * @see com.mmxlabs.models.lng.schedule.SlotPNLDetails
	 * @generated
	 */
	EClass getSlotPNLDetails();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.SlotPNLDetails#getSlot <em>Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Slot</em>'.
	 * @see com.mmxlabs.models.lng.schedule.SlotPNLDetails#getSlot()
	 * @see #getSlotPNLDetails()
	 * @generated
	 */
	EReference getSlotPNLDetails_Slot();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.schedule.SlotPNLDetails#getGeneralPNLDetails <em>General PNL Details</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>General PNL Details</em>'.
	 * @see com.mmxlabs.models.lng.schedule.SlotPNLDetails#getGeneralPNLDetails()
	 * @see #getSlotPNLDetails()
	 * @generated
	 */
	EReference getSlotPNLDetails_GeneralPNLDetails();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.GeneralPNLDetails <em>General PNL Details</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>General PNL Details</em>'.
	 * @see com.mmxlabs.models.lng.schedule.GeneralPNLDetails
	 * @generated
	 */
	EClass getGeneralPNLDetails();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails <em>Basic Slot PNL Details</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Basic Slot PNL Details</em>'.
	 * @see com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails
	 * @generated
	 */
	EClass getBasicSlotPNLDetails();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getExtraShippingPNL <em>Extra Shipping PNL</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Extra Shipping PNL</em>'.
	 * @see com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getExtraShippingPNL()
	 * @see #getBasicSlotPNLDetails()
	 * @generated
	 */
	EAttribute getBasicSlotPNLDetails_ExtraShippingPNL();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getAdditionalPNL <em>Additional PNL</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Additional PNL</em>'.
	 * @see com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getAdditionalPNL()
	 * @see #getBasicSlotPNLDetails()
	 * @generated
	 */
	EAttribute getBasicSlotPNLDetails_AdditionalPNL();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getCancellationFees <em>Cancellation Fees</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cancellation Fees</em>'.
	 * @see com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getCancellationFees()
	 * @see #getBasicSlotPNLDetails()
	 * @generated
	 */
	EAttribute getBasicSlotPNLDetails_CancellationFees();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getHedgingValue <em>Hedging Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Hedging Value</em>'.
	 * @see com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getHedgingValue()
	 * @see #getBasicSlotPNLDetails()
	 * @generated
	 */
	EAttribute getBasicSlotPNLDetails_HedgingValue();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getMiscCostsValue <em>Misc Costs Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Misc Costs Value</em>'.
	 * @see com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getMiscCostsValue()
	 * @see #getBasicSlotPNLDetails()
	 * @generated
	 */
	EAttribute getBasicSlotPNLDetails_MiscCostsValue();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getExtraUpsidePNL <em>Extra Upside PNL</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Extra Upside PNL</em>'.
	 * @see com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails#getExtraUpsidePNL()
	 * @see #getBasicSlotPNLDetails()
	 * @generated
	 */
	EAttribute getBasicSlotPNLDetails_ExtraUpsidePNL();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.EventGrouping <em>Event Grouping</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Event Grouping</em>'.
	 * @see com.mmxlabs.models.lng.schedule.EventGrouping
	 * @generated
	 */
	EClass getEventGrouping();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.schedule.EventGrouping#getEvents <em>Events</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Events</em>'.
	 * @see com.mmxlabs.models.lng.schedule.EventGrouping#getEvents()
	 * @see #getEventGrouping()
	 * @generated
	 */
	EReference getEventGrouping_Events();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.PortVisitLateness <em>Port Visit Lateness</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Port Visit Lateness</em>'.
	 * @see com.mmxlabs.models.lng.schedule.PortVisitLateness
	 * @generated
	 */
	EClass getPortVisitLateness();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.PortVisitLateness#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see com.mmxlabs.models.lng.schedule.PortVisitLateness#getType()
	 * @see #getPortVisitLateness()
	 * @generated
	 */
	EAttribute getPortVisitLateness_Type();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.PortVisitLateness#getLatenessInHours <em>Lateness In Hours</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lateness In Hours</em>'.
	 * @see com.mmxlabs.models.lng.schedule.PortVisitLateness#getLatenessInHours()
	 * @see #getPortVisitLateness()
	 * @generated
	 */
	EAttribute getPortVisitLateness_LatenessInHours();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.schedule.ExposureDetail <em>Exposure Detail</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Exposure Detail</em>'.
	 * @see com.mmxlabs.models.lng.schedule.ExposureDetail
	 * @generated
	 */
	EClass getExposureDetail();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.schedule.ExposureDetail#getIndex <em>Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Index</em>'.
	 * @see com.mmxlabs.models.lng.schedule.ExposureDetail#getIndex()
	 * @see #getExposureDetail()
	 * @generated
	 */
	EReference getExposureDetail_Index();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.ExposureDetail#getDate <em>Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Date</em>'.
	 * @see com.mmxlabs.models.lng.schedule.ExposureDetail#getDate()
	 * @see #getExposureDetail()
	 * @generated
	 */
	EAttribute getExposureDetail_Date();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.ExposureDetail#getVolumeInMMBTU <em>Volume In MMBTU</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volume In MMBTU</em>'.
	 * @see com.mmxlabs.models.lng.schedule.ExposureDetail#getVolumeInMMBTU()
	 * @see #getExposureDetail()
	 * @generated
	 */
	EAttribute getExposureDetail_VolumeInMMBTU();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.ExposureDetail#getVolumeInNativeUnits <em>Volume In Native Units</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volume In Native Units</em>'.
	 * @see com.mmxlabs.models.lng.schedule.ExposureDetail#getVolumeInNativeUnits()
	 * @see #getExposureDetail()
	 * @generated
	 */
	EAttribute getExposureDetail_VolumeInNativeUnits();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.ExposureDetail#getUnitPrice <em>Unit Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Unit Price</em>'.
	 * @see com.mmxlabs.models.lng.schedule.ExposureDetail#getUnitPrice()
	 * @see #getExposureDetail()
	 * @generated
	 */
	EAttribute getExposureDetail_UnitPrice();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.ExposureDetail#getNativeValue <em>Native Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Native Value</em>'.
	 * @see com.mmxlabs.models.lng.schedule.ExposureDetail#getNativeValue()
	 * @see #getExposureDetail()
	 * @generated
	 */
	EAttribute getExposureDetail_NativeValue();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.ExposureDetail#getVolumeUnit <em>Volume Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volume Unit</em>'.
	 * @see com.mmxlabs.models.lng.schedule.ExposureDetail#getVolumeUnit()
	 * @see #getExposureDetail()
	 * @generated
	 */
	EAttribute getExposureDetail_VolumeUnit();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.schedule.ExposureDetail#getCurrencyUnit <em>Currency Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Currency Unit</em>'.
	 * @see com.mmxlabs.models.lng.schedule.ExposureDetail#getCurrencyUnit()
	 * @see #getExposureDetail()
	 * @generated
	 */
	EAttribute getExposureDetail_CurrencyUnit();

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
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Sequence Type</em>'.
	 * @see com.mmxlabs.models.lng.schedule.SequenceType
	 * @generated
	 */
	EEnum getSequenceType();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.schedule.CapacityViolationType <em>Capacity Violation Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Capacity Violation Type</em>'.
	 * @see com.mmxlabs.models.lng.schedule.CapacityViolationType
	 * @generated
	 */
	EEnum getCapacityViolationType();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.schedule.PortVisitLatenessType <em>Port Visit Lateness Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Port Visit Lateness Type</em>'.
	 * @see com.mmxlabs.models.lng.schedule.PortVisitLatenessType
	 * @generated
	 */
	EEnum getPortVisitLatenessType();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.schedule.SlotAllocationType <em>Slot Allocation Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Slot Allocation Type</em>'.
	 * @see com.mmxlabs.models.lng.schedule.SlotAllocationType
	 * @generated
	 */
	EEnum getSlotAllocationType();

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
		 * The meta object literal for the '<em><b>Open Slot Allocations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEDULE__OPEN_SLOT_ALLOCATIONS = eINSTANCE.getSchedule_OpenSlotAllocations();

		/**
		 * The meta object literal for the '<em><b>Market Allocations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEDULE__MARKET_ALLOCATIONS = eINSTANCE.getSchedule_MarketAllocations();

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
		 * The meta object literal for the '<em><b>Vessel Availability</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SEQUENCE__VESSEL_AVAILABILITY = eINSTANCE.getSequence_VesselAvailability();

		/**
		 * The meta object literal for the '<em><b>Charter In Market</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SEQUENCE__CHARTER_IN_MARKET = eINSTANCE.getSequence_CharterInMarket();

		/**
		 * The meta object literal for the '<em><b>Fitnesses</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SEQUENCE__FITNESSES = eINSTANCE.getSequence_Fitnesses();

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
		 * The meta object literal for the '<em><b>Charter Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EVENT__CHARTER_COST = eINSTANCE.getEvent_CharterCost();

		/**
		 * The meta object literal for the '<em><b>Heel At Start</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EVENT__HEEL_AT_START = eINSTANCE.getEvent_HeelAtStart();

		/**
		 * The meta object literal for the '<em><b>Heel At End</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EVENT__HEEL_AT_END = eINSTANCE.getEvent_HeelAtEnd();

		/**
		 * The meta object literal for the '<em><b>Get Duration</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation EVENT___GET_DURATION = eINSTANCE.getEvent__GetDuration();

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
		 * The meta object literal for the '<em><b>Route</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference JOURNEY__ROUTE = eINSTANCE.getJourney_Route();

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
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.GeneratedCharterOutImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getGeneratedCharterOut()
		 * @generated
		 */
		EClass GENERATED_CHARTER_OUT = eINSTANCE.getGeneratedCharterOut();

		/**
		 * The meta object literal for the '<em><b>Revenue</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.MarketAllocationImpl <em>Market Allocation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.MarketAllocationImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getMarketAllocation()
		 * @generated
		 */
		EClass MARKET_ALLOCATION = eINSTANCE.getMarketAllocation();

		/**
		 * The meta object literal for the '<em><b>Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MARKET_ALLOCATION__SLOT = eINSTANCE.getMarketAllocation_Slot();

		/**
		 * The meta object literal for the '<em><b>Market</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MARKET_ALLOCATION__MARKET = eINSTANCE.getMarketAllocation_Market();

		/**
		 * The meta object literal for the '<em><b>Slot Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MARKET_ALLOCATION__SLOT_ALLOCATION = eINSTANCE.getMarketAllocation_SlotAllocation();

		/**
		 * The meta object literal for the '<em><b>Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MARKET_ALLOCATION__PRICE = eINSTANCE.getMarketAllocation_Price();

		/**
		 * The meta object literal for the '<em><b>Slot Visit</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MARKET_ALLOCATION__SLOT_VISIT = eINSTANCE.getMarketAllocation_SlotVisit();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.OpenSlotAllocationImpl <em>Open Slot Allocation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.OpenSlotAllocationImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getOpenSlotAllocation()
		 * @generated
		 */
		EClass OPEN_SLOT_ALLOCATION = eINSTANCE.getOpenSlotAllocation();

		/**
		 * The meta object literal for the '<em><b>Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPEN_SLOT_ALLOCATION__SLOT = eINSTANCE.getOpenSlotAllocation_Slot();

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
		 * The meta object literal for the '<em><b>Market Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT_ALLOCATION__MARKET_ALLOCATION = eINSTANCE.getSlotAllocation_MarketAllocation();

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
		 * The meta object literal for the '<em><b>Energy Transferred</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT_ALLOCATION__ENERGY_TRANSFERRED = eINSTANCE.getSlotAllocation_EnergyTransferred();

		/**
		 * The meta object literal for the '<em><b>Cv</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT_ALLOCATION__CV = eINSTANCE.getSlotAllocation_Cv();

		/**
		 * The meta object literal for the '<em><b>Volume Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT_ALLOCATION__VOLUME_VALUE = eINSTANCE.getSlotAllocation_VolumeValue();

		/**
		 * The meta object literal for the '<em><b>Exposures</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT_ALLOCATION__EXPOSURES = eINSTANCE.getSlotAllocation_Exposures();

		/**
		 * The meta object literal for the '<em><b>Physical Volume Transferred</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT_ALLOCATION__PHYSICAL_VOLUME_TRANSFERRED = eINSTANCE.getSlotAllocation_PhysicalVolumeTransferred();

		/**
		 * The meta object literal for the '<em><b>Physical Energy Transferred</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT_ALLOCATION__PHYSICAL_ENERGY_TRANSFERRED = eINSTANCE.getSlotAllocation_PhysicalEnergyTransferred();

		/**
		 * The meta object literal for the '<em><b>Slot Allocation Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT_ALLOCATION__SLOT_ALLOCATION_TYPE = eINSTANCE.getSlotAllocation_SlotAllocationType();

		/**
		 * The meta object literal for the '<em><b>Get Port</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT_ALLOCATION___GET_PORT = eINSTANCE.getSlotAllocation__GetPort();

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
		 * The meta object literal for the '<em><b>Unit Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FUEL_AMOUNT__UNIT_PRICE = eINSTANCE.getFuelAmount_UnitPrice();

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
		 * The meta object literal for the '<em><b>Lateness</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PORT_VISIT__LATENESS = eINSTANCE.getPortVisit_Lateness();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.CapacityViolationsHolderImpl <em>Capacity Violations Holder</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.CapacityViolationsHolderImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getCapacityViolationsHolder()
		 * @generated
		 */
		EClass CAPACITY_VIOLATIONS_HOLDER = eINSTANCE.getCapacityViolationsHolder();

		/**
		 * The meta object literal for the '<em><b>Violations</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CAPACITY_VIOLATIONS_HOLDER__VIOLATIONS = eINSTANCE.getCapacityViolationsHolder_Violations();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.CapacityMapEntryImpl <em>Capacity Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.CapacityMapEntryImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getCapacityMapEntry()
		 * @generated
		 */
		EClass CAPACITY_MAP_ENTRY = eINSTANCE.getCapacityMapEntry();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CAPACITY_MAP_ENTRY__KEY = eINSTANCE.getCapacityMapEntry_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CAPACITY_MAP_ENTRY__VALUE = eINSTANCE.getCapacityMapEntry_Value();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.ProfitAndLossContainerImpl <em>Profit And Loss Container</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.ProfitAndLossContainerImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getProfitAndLossContainer()
		 * @generated
		 */
		EClass PROFIT_AND_LOSS_CONTAINER = eINSTANCE.getProfitAndLossContainer();

		/**
		 * The meta object literal for the '<em><b>Group Profit And Loss</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS = eINSTANCE.getProfitAndLossContainer_GroupProfitAndLoss();

		/**
		 * The meta object literal for the '<em><b>General PNL Details</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROFIT_AND_LOSS_CONTAINER__GENERAL_PNL_DETAILS = eINSTANCE.getProfitAndLossContainer_GeneralPNLDetails();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.GroupProfitAndLossImpl <em>Group Profit And Loss</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.GroupProfitAndLossImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getGroupProfitAndLoss()
		 * @generated
		 */
		EClass GROUP_PROFIT_AND_LOSS = eINSTANCE.getGroupProfitAndLoss();

		/**
		 * The meta object literal for the '<em><b>Profit And Loss</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GROUP_PROFIT_AND_LOSS__PROFIT_AND_LOSS = eINSTANCE.getGroupProfitAndLoss_ProfitAndLoss();

		/**
		 * The meta object literal for the '<em><b>Profit And Loss Pre Tax</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GROUP_PROFIT_AND_LOSS__PROFIT_AND_LOSS_PRE_TAX = eINSTANCE.getGroupProfitAndLoss_ProfitAndLossPreTax();

		/**
		 * The meta object literal for the '<em><b>Tax Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GROUP_PROFIT_AND_LOSS__TAX_VALUE = eINSTANCE.getGroupProfitAndLoss_TaxValue();

		/**
		 * The meta object literal for the '<em><b>Entity Profit And Losses</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GROUP_PROFIT_AND_LOSS__ENTITY_PROFIT_AND_LOSSES = eINSTANCE.getGroupProfitAndLoss_EntityProfitAndLosses();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.EntityProfitAndLossImpl <em>Entity Profit And Loss</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.EntityProfitAndLossImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getEntityProfitAndLoss()
		 * @generated
		 */
		EClass ENTITY_PROFIT_AND_LOSS = eINSTANCE.getEntityProfitAndLoss();

		/**
		 * The meta object literal for the '<em><b>Entity</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENTITY_PROFIT_AND_LOSS__ENTITY = eINSTANCE.getEntityProfitAndLoss_Entity();

		/**
		 * The meta object literal for the '<em><b>Entity Book</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENTITY_PROFIT_AND_LOSS__ENTITY_BOOK = eINSTANCE.getEntityProfitAndLoss_EntityBook();

		/**
		 * The meta object literal for the '<em><b>Profit And Loss</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENTITY_PROFIT_AND_LOSS__PROFIT_AND_LOSS = eINSTANCE.getEntityProfitAndLoss_ProfitAndLoss();

		/**
		 * The meta object literal for the '<em><b>Profit And Loss Pre Tax</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENTITY_PROFIT_AND_LOSS__PROFIT_AND_LOSS_PRE_TAX = eINSTANCE.getEntityProfitAndLoss_ProfitAndLossPreTax();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.EntityPNLDetailsImpl <em>Entity PNL Details</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.EntityPNLDetailsImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getEntityPNLDetails()
		 * @generated
		 */
		EClass ENTITY_PNL_DETAILS = eINSTANCE.getEntityPNLDetails();

		/**
		 * The meta object literal for the '<em><b>Entity</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENTITY_PNL_DETAILS__ENTITY = eINSTANCE.getEntityPNLDetails_Entity();

		/**
		 * The meta object literal for the '<em><b>General PNL Details</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENTITY_PNL_DETAILS__GENERAL_PNL_DETAILS = eINSTANCE.getEntityPNLDetails_GeneralPNLDetails();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.SlotPNLDetailsImpl <em>Slot PNL Details</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.SlotPNLDetailsImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getSlotPNLDetails()
		 * @generated
		 */
		EClass SLOT_PNL_DETAILS = eINSTANCE.getSlotPNLDetails();

		/**
		 * The meta object literal for the '<em><b>Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT_PNL_DETAILS__SLOT = eINSTANCE.getSlotPNLDetails_Slot();

		/**
		 * The meta object literal for the '<em><b>General PNL Details</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT_PNL_DETAILS__GENERAL_PNL_DETAILS = eINSTANCE.getSlotPNLDetails_GeneralPNLDetails();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.GeneralPNLDetailsImpl <em>General PNL Details</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.GeneralPNLDetailsImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getGeneralPNLDetails()
		 * @generated
		 */
		EClass GENERAL_PNL_DETAILS = eINSTANCE.getGeneralPNLDetails();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.BasicSlotPNLDetailsImpl <em>Basic Slot PNL Details</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.BasicSlotPNLDetailsImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getBasicSlotPNLDetails()
		 * @generated
		 */
		EClass BASIC_SLOT_PNL_DETAILS = eINSTANCE.getBasicSlotPNLDetails();

		/**
		 * The meta object literal for the '<em><b>Extra Shipping PNL</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BASIC_SLOT_PNL_DETAILS__EXTRA_SHIPPING_PNL = eINSTANCE.getBasicSlotPNLDetails_ExtraShippingPNL();

		/**
		 * The meta object literal for the '<em><b>Additional PNL</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BASIC_SLOT_PNL_DETAILS__ADDITIONAL_PNL = eINSTANCE.getBasicSlotPNLDetails_AdditionalPNL();

		/**
		 * The meta object literal for the '<em><b>Cancellation Fees</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BASIC_SLOT_PNL_DETAILS__CANCELLATION_FEES = eINSTANCE.getBasicSlotPNLDetails_CancellationFees();

		/**
		 * The meta object literal for the '<em><b>Hedging Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BASIC_SLOT_PNL_DETAILS__HEDGING_VALUE = eINSTANCE.getBasicSlotPNLDetails_HedgingValue();

		/**
		 * The meta object literal for the '<em><b>Misc Costs Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BASIC_SLOT_PNL_DETAILS__MISC_COSTS_VALUE = eINSTANCE.getBasicSlotPNLDetails_MiscCostsValue();

		/**
		 * The meta object literal for the '<em><b>Extra Upside PNL</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BASIC_SLOT_PNL_DETAILS__EXTRA_UPSIDE_PNL = eINSTANCE.getBasicSlotPNLDetails_ExtraUpsidePNL();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.EventGroupingImpl <em>Event Grouping</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.EventGroupingImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getEventGrouping()
		 * @generated
		 */
		EClass EVENT_GROUPING = eINSTANCE.getEventGrouping();

		/**
		 * The meta object literal for the '<em><b>Events</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EVENT_GROUPING__EVENTS = eINSTANCE.getEventGrouping_Events();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.PortVisitLatenessImpl <em>Port Visit Lateness</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.PortVisitLatenessImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getPortVisitLateness()
		 * @generated
		 */
		EClass PORT_VISIT_LATENESS = eINSTANCE.getPortVisitLateness();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT_VISIT_LATENESS__TYPE = eINSTANCE.getPortVisitLateness_Type();

		/**
		 * The meta object literal for the '<em><b>Lateness In Hours</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT_VISIT_LATENESS__LATENESS_IN_HOURS = eINSTANCE.getPortVisitLateness_LatenessInHours();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.impl.ExposureDetailImpl <em>Exposure Detail</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.impl.ExposureDetailImpl
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getExposureDetail()
		 * @generated
		 */
		EClass EXPOSURE_DETAIL = eINSTANCE.getExposureDetail();

		/**
		 * The meta object literal for the '<em><b>Index</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXPOSURE_DETAIL__INDEX = eINSTANCE.getExposureDetail_Index();

		/**
		 * The meta object literal for the '<em><b>Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPOSURE_DETAIL__DATE = eINSTANCE.getExposureDetail_Date();

		/**
		 * The meta object literal for the '<em><b>Volume In MMBTU</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPOSURE_DETAIL__VOLUME_IN_MMBTU = eINSTANCE.getExposureDetail_VolumeInMMBTU();

		/**
		 * The meta object literal for the '<em><b>Volume In Native Units</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPOSURE_DETAIL__VOLUME_IN_NATIVE_UNITS = eINSTANCE.getExposureDetail_VolumeInNativeUnits();

		/**
		 * The meta object literal for the '<em><b>Unit Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPOSURE_DETAIL__UNIT_PRICE = eINSTANCE.getExposureDetail_UnitPrice();

		/**
		 * The meta object literal for the '<em><b>Native Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPOSURE_DETAIL__NATIVE_VALUE = eINSTANCE.getExposureDetail_NativeValue();

		/**
		 * The meta object literal for the '<em><b>Volume Unit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPOSURE_DETAIL__VOLUME_UNIT = eINSTANCE.getExposureDetail_VolumeUnit();

		/**
		 * The meta object literal for the '<em><b>Currency Unit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPOSURE_DETAIL__CURRENCY_UNIT = eINSTANCE.getExposureDetail_CurrencyUnit();

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
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.SequenceType
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getSequenceType()
		 * @generated
		 */
		EEnum SEQUENCE_TYPE = eINSTANCE.getSequenceType();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.CapacityViolationType <em>Capacity Violation Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.CapacityViolationType
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getCapacityViolationType()
		 * @generated
		 */
		EEnum CAPACITY_VIOLATION_TYPE = eINSTANCE.getCapacityViolationType();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.PortVisitLatenessType <em>Port Visit Lateness Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.PortVisitLatenessType
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getPortVisitLatenessType()
		 * @generated
		 */
		EEnum PORT_VISIT_LATENESS_TYPE = eINSTANCE.getPortVisitLatenessType();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.schedule.SlotAllocationType <em>Slot Allocation Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.schedule.SlotAllocationType
		 * @see com.mmxlabs.models.lng.schedule.impl.SchedulePackageImpl#getSlotAllocationType()
		 * @generated
		 */
		EEnum SLOT_ALLOCATION_TYPE = eINSTANCE.getSlotAllocationType();

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
