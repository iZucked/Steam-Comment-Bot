/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;

import com.mmxlabs.models.lng.schedule.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.Iterable;
import java.util.Calendar;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

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
			case SchedulePackage.EVENT: return createEvent();
			case SchedulePackage.START_EVENT: return createStartEvent();
			case SchedulePackage.END_EVENT: return createEndEvent();
			case SchedulePackage.JOURNEY: return createJourney();
			case SchedulePackage.IDLE: return createIdle();
			case SchedulePackage.PORT_VISIT: return createPortVisit();
			case SchedulePackage.SLOT_VISIT: return createSlotVisit();
			case SchedulePackage.VESSEL_EVENT_VISIT: return createVesselEventVisit();
			case SchedulePackage.GENERATED_CHARTER_OUT: return createGeneratedCharterOut();
			case SchedulePackage.COOLDOWN: return createCooldown();
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
	public ScheduleModel createScheduleModel() {
		ScheduleModelImpl scheduleModel = new ScheduleModelImpl();
		return scheduleModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Schedule createSchedule() {
		ScheduleImpl schedule = new ScheduleImpl();
		return schedule;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Sequence createSequence() {
		SequenceImpl sequence = new SequenceImpl();
		return sequence;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Event createEvent() {
		EventImpl event = new EventImpl();
		return event;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotVisit createSlotVisit() {
		SlotVisitImpl slotVisit = new SlotVisitImpl();
		return slotVisit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselEventVisit createVesselEventVisit() {
		VesselEventVisitImpl vesselEventVisit = new VesselEventVisitImpl();
		return vesselEventVisit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Journey createJourney() {
		JourneyImpl journey = new JourneyImpl();
		return journey;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Idle createIdle() {
		IdleImpl idle = new IdleImpl();
		return idle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GeneratedCharterOut createGeneratedCharterOut() {
		GeneratedCharterOutImpl generatedCharterOut = new GeneratedCharterOutImpl();
		return generatedCharterOut;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FuelUsage createFuelUsage() {
		FuelUsageImpl fuelUsage = new FuelUsageImpl();
		return fuelUsage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FuelQuantity createFuelQuantity() {
		FuelQuantityImpl fuelQuantity = new FuelQuantityImpl();
		return fuelQuantity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Cooldown createCooldown() {
		CooldownImpl cooldown = new CooldownImpl();
		return cooldown;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CargoAllocation createCargoAllocation() {
		CargoAllocationImpl cargoAllocation = new CargoAllocationImpl();
		return cargoAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MarketAllocation createMarketAllocation() {
		MarketAllocationImpl marketAllocation = new MarketAllocationImpl();
		return marketAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OpenSlotAllocation createOpenSlotAllocation() {
		OpenSlotAllocationImpl openSlotAllocation = new OpenSlotAllocationImpl();
		return openSlotAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocation createSlotAllocation() {
		SlotAllocationImpl slotAllocation = new SlotAllocationImpl();
		return slotAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FuelAmount createFuelAmount() {
		FuelAmountImpl fuelAmount = new FuelAmountImpl();
		return fuelAmount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Fitness createFitness() {
		FitnessImpl fitness = new FitnessImpl();
		return fitness;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PortVisit createPortVisit() {
		PortVisitImpl portVisit = new PortVisitImpl();
		return portVisit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StartEvent createStartEvent() {
		StartEventImpl startEvent = new StartEventImpl();
		return startEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EndEvent createEndEvent() {
		EndEventImpl endEvent = new EndEventImpl();
		return endEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
	public ProfitAndLossContainer createProfitAndLossContainer() {
		ProfitAndLossContainerImpl profitAndLossContainer = new ProfitAndLossContainerImpl();
		return profitAndLossContainer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GroupProfitAndLoss createGroupProfitAndLoss() {
		GroupProfitAndLossImpl groupProfitAndLoss = new GroupProfitAndLossImpl();
		return groupProfitAndLoss;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntityProfitAndLoss createEntityProfitAndLoss() {
		EntityProfitAndLossImpl entityProfitAndLoss = new EntityProfitAndLossImpl();
		return entityProfitAndLoss;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntityPNLDetails createEntityPNLDetails() {
		EntityPNLDetailsImpl entityPNLDetails = new EntityPNLDetailsImpl();
		return entityPNLDetails;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotPNLDetails createSlotPNLDetails() {
		SlotPNLDetailsImpl slotPNLDetails = new SlotPNLDetailsImpl();
		return slotPNLDetails;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BasicSlotPNLDetails createBasicSlotPNLDetails() {
		BasicSlotPNLDetailsImpl basicSlotPNLDetails = new BasicSlotPNLDetailsImpl();
		return basicSlotPNLDetails;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EventGrouping createEventGrouping() {
		EventGroupingImpl eventGrouping = new EventGroupingImpl();
		return eventGrouping;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PortVisitLateness createPortVisitLateness() {
		PortVisitLatenessImpl portVisitLateness = new PortVisitLatenessImpl();
		return portVisitLateness;
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
	 * Bodge to read b64 encoded serialized objects
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Object createObjectFromString(EDataType eDataType, String initialValue) {
		try {
			final ByteArrayInputStream bais = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(initialValue));
			final ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			return null;
		} finally {
			
		}
//		return super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * This is a slightly grim approach to putting Serializable objects into the ecore.
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String convertObjectToString(EDataType eDataType, Object instanceValue) {
		if (instanceValue instanceof Serializable) {
			final Serializable s = (Serializable) instanceValue;
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				final ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(s);
				oos.flush();
				oos.close();
				final String b64 = DatatypeConverter.printBase64Binary(baos.toByteArray());
				return b64;
			} catch (IOException e) {
			}
			
		}
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
