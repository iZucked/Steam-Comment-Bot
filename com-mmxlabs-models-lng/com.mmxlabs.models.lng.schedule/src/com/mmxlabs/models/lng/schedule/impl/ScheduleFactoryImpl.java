/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;

import com.mmxlabs.models.lng.schedule.*;
import java.util.Calendar;
import java.util.Map;


import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails;
import com.mmxlabs.models.lng.schedule.CapacityViolationType;
import com.mmxlabs.models.lng.schedule.CapacityViolationsHolder;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.EntityPNLDetails;
import com.mmxlabs.models.lng.schedule.EntityProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.Fitness;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.PortVisitLateness;
import com.mmxlabs.models.lng.schedule.PortVisitLatenessType;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotPNLDetails;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ScheduleFactoryImpl extends EFactoryImpl implements ScheduleFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ScheduleFactory init() {
		try {
			ScheduleFactory theScheduleFactory = (ScheduleFactory)EPackage.Registry.INSTANCE.getEFactory(SchedulePackage.eNS_URI);
			if (theScheduleFactory != null) {
				return theScheduleFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ScheduleFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScheduleFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case SchedulePackage.SCHEDULE_MODEL: return createScheduleModel();
			case SchedulePackage.SCHEDULE: return createSchedule();
			case SchedulePackage.FITNESS: return createFitness();
			case SchedulePackage.CARGO_ALLOCATION: return createCargoAllocation();
			case SchedulePackage.MARKET_ALLOCATION: return createMarketAllocation();
			case SchedulePackage.OPEN_SLOT_ALLOCATION: return createOpenSlotAllocation();
			case SchedulePackage.SLOT_ALLOCATION: return createSlotAllocation();
			case SchedulePackage.SEQUENCE: return createSequence();
			case SchedulePackage.OTHER_PNL: return createOtherPNL();
			case SchedulePackage.EVENT: return createEvent();
			case SchedulePackage.START_EVENT: return createStartEvent();
			case SchedulePackage.END_EVENT: return createEndEvent();
			case SchedulePackage.JOURNEY: return createJourney();
			case SchedulePackage.IDLE: return createIdle();
			case SchedulePackage.PORT_VISIT: return createPortVisit();
			case SchedulePackage.SLOT_VISIT: return createSlotVisit();
			case SchedulePackage.VESSEL_EVENT_VISIT: return createVesselEventVisit();
			case SchedulePackage.GENERATED_CHARTER_OUT: return createGeneratedCharterOut();
			case SchedulePackage.CHARTER_LENGTH_EVENT: return createCharterLengthEvent();
			case SchedulePackage.COOLDOWN: return createCooldown();
			case SchedulePackage.PURGE: return createPurge();
			case SchedulePackage.FUEL_USAGE: return createFuelUsage();
			case SchedulePackage.FUEL_QUANTITY: return createFuelQuantity();
			case SchedulePackage.FUEL_AMOUNT: return createFuelAmount();
			case SchedulePackage.CAPACITY_VIOLATIONS_HOLDER: return createCapacityViolationsHolder();
			case SchedulePackage.CAPACITY_MAP_ENTRY: return (EObject)createCapacityMapEntry();
			case SchedulePackage.PROFIT_AND_LOSS_CONTAINER: return createProfitAndLossContainer();
			case SchedulePackage.GROUP_PROFIT_AND_LOSS: return createGroupProfitAndLoss();
			case SchedulePackage.ENTITY_PROFIT_AND_LOSS: return createEntityProfitAndLoss();
			case SchedulePackage.ENTITY_PNL_DETAILS: return createEntityPNLDetails();
			case SchedulePackage.SLOT_PNL_DETAILS: return createSlotPNLDetails();
			case SchedulePackage.BASIC_SLOT_PNL_DETAILS: return createBasicSlotPNLDetails();
			case SchedulePackage.EVENT_GROUPING: return createEventGrouping();
			case SchedulePackage.PORT_VISIT_LATENESS: return createPortVisitLateness();
			case SchedulePackage.EXPOSURE_DETAIL: return createExposureDetail();
			case SchedulePackage.BALLAST_BONUS_FEE_DETAILS: return createBallastBonusFeeDetails();
			case SchedulePackage.LUMP_SUM_CONTRACT_DETAILS: return createLumpSumContractDetails();
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS: return createNotionalJourneyContractDetails();
			case SchedulePackage.CHARTER_AVAILABLE_TO_EVENT: return createCharterAvailableToEvent();
			case SchedulePackage.CANAL_BOOKING_EVENT: return createCanalBookingEvent();
			case SchedulePackage.CHARTER_AVAILABLE_FROM_EVENT: return createCharterAvailableFromEvent();
			case SchedulePackage.GROUPED_CHARTER_LENGTH_EVENT: return createGroupedCharterLengthEvent();
			case SchedulePackage.INVENTORY_EVENTS: return createInventoryEvents();
			case SchedulePackage.INVENTORY_CHANGE_EVENT: return createInventoryChangeEvent();
			case SchedulePackage.PAPER_DEAL_ALLOCATION: return createPaperDealAllocation();
			case SchedulePackage.PAPER_DEAL_ALLOCATION_ENTRY: return createPaperDealAllocationEntry();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case SchedulePackage.SEQUENCE_TYPE:
				return createSequenceTypeFromString(eDataType, initialValue);
			case SchedulePackage.FUEL:
				return createFuelFromString(eDataType, initialValue);
			case SchedulePackage.FUEL_UNIT:
				return createFuelUnitFromString(eDataType, initialValue);
			case SchedulePackage.CAPACITY_VIOLATION_TYPE:
				return createCapacityViolationTypeFromString(eDataType, initialValue);
			case SchedulePackage.PORT_VISIT_LATENESS_TYPE:
				return createPortVisitLatenessTypeFromString(eDataType, initialValue);
			case SchedulePackage.SLOT_ALLOCATION_TYPE:
				return createSlotAllocationTypeFromString(eDataType, initialValue);
			case SchedulePackage.PANAMA_BOOKING_PERIOD:
				return createPanamaBookingPeriodFromString(eDataType, initialValue);
			case SchedulePackage.CALENDAR:
				return createCalendarFromString(eDataType, initialValue);
			case SchedulePackage.ITERABLE:
				return createIterableFromString(eDataType, initialValue);
			case SchedulePackage.OBJECT:
				return createObjectFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case SchedulePackage.SEQUENCE_TYPE:
				return convertSequenceTypeToString(eDataType, instanceValue);
			case SchedulePackage.FUEL:
				return convertFuelToString(eDataType, instanceValue);
			case SchedulePackage.FUEL_UNIT:
				return convertFuelUnitToString(eDataType, instanceValue);
			case SchedulePackage.CAPACITY_VIOLATION_TYPE:
				return convertCapacityViolationTypeToString(eDataType, instanceValue);
			case SchedulePackage.PORT_VISIT_LATENESS_TYPE:
				return convertPortVisitLatenessTypeToString(eDataType, instanceValue);
			case SchedulePackage.SLOT_ALLOCATION_TYPE:
				return convertSlotAllocationTypeToString(eDataType, instanceValue);
			case SchedulePackage.PANAMA_BOOKING_PERIOD:
				return convertPanamaBookingPeriodToString(eDataType, instanceValue);
			case SchedulePackage.CALENDAR:
				return convertCalendarToString(eDataType, instanceValue);
			case SchedulePackage.ITERABLE:
				return convertIterableToString(eDataType, instanceValue);
			case SchedulePackage.OBJECT:
				return convertObjectToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ScheduleModel createScheduleModel() {
		ScheduleModelImpl scheduleModel = new ScheduleModelImpl();
		return scheduleModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Schedule createSchedule() {
		ScheduleImpl schedule = new ScheduleImpl();
		return schedule;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Sequence createSequence() {
		SequenceImpl sequence = new SequenceImpl();
		return sequence;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OtherPNL createOtherPNL() {
		OtherPNLImpl otherPNL = new OtherPNLImpl();
		return otherPNL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Event createEvent() {
		EventImpl event = new EventImpl();
		return event;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SlotVisit createSlotVisit() {
		SlotVisitImpl slotVisit = new SlotVisitImpl();
		return slotVisit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VesselEventVisit createVesselEventVisit() {
		VesselEventVisitImpl vesselEventVisit = new VesselEventVisitImpl();
		return vesselEventVisit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Idle createIdle() {
		IdleImpl idle = new IdleImpl();
		return idle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public GeneratedCharterOut createGeneratedCharterOut() {
		GeneratedCharterOutImpl generatedCharterOut = new GeneratedCharterOutImpl();
		return generatedCharterOut;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CharterLengthEvent createCharterLengthEvent() {
		CharterLengthEventImpl charterLengthEvent = new CharterLengthEventImpl();
		return charterLengthEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public FuelUsage createFuelUsage() {
		FuelUsageImpl fuelUsage = new FuelUsageImpl();
		return fuelUsage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public FuelQuantity createFuelQuantity() {
		FuelQuantityImpl fuelQuantity = new FuelQuantityImpl();
		return fuelQuantity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Cooldown createCooldown() {
		CooldownImpl cooldown = new CooldownImpl();
		return cooldown;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Purge createPurge() {
		PurgeImpl purge = new PurgeImpl();
		return purge;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CargoAllocation createCargoAllocation() {
		CargoAllocationImpl cargoAllocation = new CargoAllocationImpl();
		return cargoAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MarketAllocation createMarketAllocation() {
		MarketAllocationImpl marketAllocation = new MarketAllocationImpl();
		return marketAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OpenSlotAllocation createOpenSlotAllocation() {
		OpenSlotAllocationImpl openSlotAllocation = new OpenSlotAllocationImpl();
		return openSlotAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SlotAllocation createSlotAllocation() {
		SlotAllocationImpl slotAllocation = new SlotAllocationImpl();
		return slotAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public FuelAmount createFuelAmount() {
		FuelAmountImpl fuelAmount = new FuelAmountImpl();
		return fuelAmount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Fitness createFitness() {
		FitnessImpl fitness = new FitnessImpl();
		return fitness;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PortVisit createPortVisit() {
		PortVisitImpl portVisit = new PortVisitImpl();
		return portVisit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public StartEvent createStartEvent() {
		StartEventImpl startEvent = new StartEventImpl();
		return startEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EndEvent createEndEvent() {
		EndEventImpl endEvent = new EndEventImpl();
		return endEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Journey createJourney() {
		JourneyImpl journey = new JourneyImpl();
		return journey;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CapacityViolationsHolder createCapacityViolationsHolder() {
		CapacityViolationsHolderImpl capacityViolationsHolder = new CapacityViolationsHolderImpl();
		return capacityViolationsHolder;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<CapacityViolationType, Long> createCapacityMapEntry() {
		CapacityMapEntryImpl capacityMapEntry = new CapacityMapEntryImpl();
		return capacityMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ProfitAndLossContainer createProfitAndLossContainer() {
		ProfitAndLossContainerImpl profitAndLossContainer = new ProfitAndLossContainerImpl();
		return profitAndLossContainer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public GroupProfitAndLoss createGroupProfitAndLoss() {
		GroupProfitAndLossImpl groupProfitAndLoss = new GroupProfitAndLossImpl();
		return groupProfitAndLoss;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EntityProfitAndLoss createEntityProfitAndLoss() {
		EntityProfitAndLossImpl entityProfitAndLoss = new EntityProfitAndLossImpl();
		return entityProfitAndLoss;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EntityPNLDetails createEntityPNLDetails() {
		EntityPNLDetailsImpl entityPNLDetails = new EntityPNLDetailsImpl();
		return entityPNLDetails;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SlotPNLDetails createSlotPNLDetails() {
		SlotPNLDetailsImpl slotPNLDetails = new SlotPNLDetailsImpl();
		return slotPNLDetails;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BasicSlotPNLDetails createBasicSlotPNLDetails() {
		BasicSlotPNLDetailsImpl basicSlotPNLDetails = new BasicSlotPNLDetailsImpl();
		return basicSlotPNLDetails;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EventGrouping createEventGrouping() {
		EventGroupingImpl eventGrouping = new EventGroupingImpl();
		return eventGrouping;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PortVisitLateness createPortVisitLateness() {
		PortVisitLatenessImpl portVisitLateness = new PortVisitLatenessImpl();
		return portVisitLateness;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ExposureDetail createExposureDetail() {
		ExposureDetailImpl exposureDetail = new ExposureDetailImpl();
		return exposureDetail;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BallastBonusFeeDetails createBallastBonusFeeDetails() {
		BallastBonusFeeDetailsImpl ballastBonusFeeDetails = new BallastBonusFeeDetailsImpl();
		return ballastBonusFeeDetails;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LumpSumContractDetails createLumpSumContractDetails() {
		LumpSumContractDetailsImpl lumpSumContractDetails = new LumpSumContractDetailsImpl();
		return lumpSumContractDetails;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotionalJourneyContractDetails createNotionalJourneyContractDetails() {
		NotionalJourneyContractDetailsImpl notionalJourneyContractDetails = new NotionalJourneyContractDetailsImpl();
		return notionalJourneyContractDetails;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CharterAvailableToEvent createCharterAvailableToEvent() {
		CharterAvailableToEventImpl charterAvailableToEvent = new CharterAvailableToEventImpl();
		return charterAvailableToEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CharterAvailableFromEvent createCharterAvailableFromEvent() {
		CharterAvailableFromEventImpl charterAvailableFromEvent = new CharterAvailableFromEventImpl();
		return charterAvailableFromEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public GroupedCharterLengthEvent createGroupedCharterLengthEvent() {
		GroupedCharterLengthEventImpl groupedCharterLengthEvent = new GroupedCharterLengthEventImpl();
		return groupedCharterLengthEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CanalBookingEvent createCanalBookingEvent() {
		CanalBookingEventImpl canalBookingEvent = new CanalBookingEventImpl();
		return canalBookingEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public InventoryEvents createInventoryEvents() {
		InventoryEventsImpl inventoryEvents = new InventoryEventsImpl();
		return inventoryEvents;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public InventoryChangeEvent createInventoryChangeEvent() {
		InventoryChangeEventImpl inventoryChangeEvent = new InventoryChangeEventImpl();
		return inventoryChangeEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PaperDealAllocation createPaperDealAllocation() {
		PaperDealAllocationImpl paperDealAllocation = new PaperDealAllocationImpl();
		return paperDealAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PaperDealAllocationEntry createPaperDealAllocationEntry() {
		PaperDealAllocationEntryImpl paperDealAllocationEntry = new PaperDealAllocationEntryImpl();
		return paperDealAllocationEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FuelUnit createFuelUnitFromString(EDataType eDataType, String initialValue) {
		FuelUnit result = FuelUnit.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertFuelUnitToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Fuel createFuelFromString(EDataType eDataType, String initialValue) {
		Fuel result = Fuel.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertFuelToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SequenceType createSequenceTypeFromString(EDataType eDataType, String initialValue) {
		SequenceType result = SequenceType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertSequenceTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CapacityViolationType createCapacityViolationTypeFromString(EDataType eDataType, String initialValue) {
		CapacityViolationType result = CapacityViolationType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertCapacityViolationTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PortVisitLatenessType createPortVisitLatenessTypeFromString(EDataType eDataType, String initialValue) {
		PortVisitLatenessType result = PortVisitLatenessType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertPortVisitLatenessTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocationType createSlotAllocationTypeFromString(EDataType eDataType, String initialValue) {
		SlotAllocationType result = SlotAllocationType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertSlotAllocationTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PanamaBookingPeriod createPanamaBookingPeriodFromString(EDataType eDataType, String initialValue) {
		PanamaBookingPeriod result = PanamaBookingPeriod.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertPanamaBookingPeriodToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Calendar createCalendarFromString(EDataType eDataType, String initialValue) {
		return (Calendar)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertCalendarToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Iterable<?> createIterableFromString(EDataType eDataType, String initialValue) {
		return (Iterable<?>)super.createFromString(initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertIterableToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object createObjectFromString(EDataType eDataType, String initialValue) {
		return super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertObjectToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SchedulePackage getSchedulePackage() {
		return (SchedulePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static SchedulePackage getPackage() {
		return SchedulePackage.eINSTANCE;
	}

} //ScheduleFactoryImpl
