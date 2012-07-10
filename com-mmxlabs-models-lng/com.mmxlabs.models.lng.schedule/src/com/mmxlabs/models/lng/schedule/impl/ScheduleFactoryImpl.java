/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
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

import javax.xml.bind.DatatypeConverter;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;


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
			ScheduleFactory theScheduleFactory = (ScheduleFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.mmxlabs.com/models/lng/schedule/1/"); 
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
			case SchedulePackage.SEQUENCE: return createSequence();
			case SchedulePackage.EVENT: return createEvent();
			case SchedulePackage.SLOT_VISIT: return createSlotVisit();
			case SchedulePackage.VESSEL_EVENT_VISIT: return createVesselEventVisit();
			case SchedulePackage.JOURNEY: return createJourney();
			case SchedulePackage.IDLE: return createIdle();
			case SchedulePackage.UNSCHEDULED_CARGO: return createUnscheduledCargo();
			case SchedulePackage.FUEL_USAGE: return createFuelUsage();
			case SchedulePackage.FUEL_QUANTITY: return createFuelQuantity();
			case SchedulePackage.COOLDOWN: return createCooldown();
			case SchedulePackage.CARGO_ALLOCATION: return createCargoAllocation();
			case SchedulePackage.SLOT_ALLOCATION: return createSlotAllocation();
			case SchedulePackage.FUEL_AMOUNT: return createFuelAmount();
			case SchedulePackage.FITNESS: return createFitness();
			case SchedulePackage.PORT_VISIT: return createPortVisit();
			case SchedulePackage.ADDITIONAL_DATA: return createAdditionalData();
			case SchedulePackage.ADDITIONAL_DATA_HOLDER: return createAdditionalDataHolder();
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
			case SchedulePackage.FUEL_UNIT:
				return createFuelUnitFromString(eDataType, initialValue);
			case SchedulePackage.FUEL:
				return createFuelFromString(eDataType, initialValue);
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
			case SchedulePackage.FUEL_UNIT:
				return convertFuelUnitToString(eDataType, instanceValue);
			case SchedulePackage.FUEL:
				return convertFuelToString(eDataType, instanceValue);
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
	public UnscheduledCargo createUnscheduledCargo() {
		UnscheduledCargoImpl unscheduledCargo = new UnscheduledCargoImpl();
		return unscheduledCargo;
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
	public AdditionalData createAdditionalData() {
		AdditionalDataImpl additionalData = new AdditionalDataImpl();
		return additionalData;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AdditionalDataHolder createAdditionalDataHolder() {
		AdditionalDataHolderImpl additionalDataHolder = new AdditionalDataHolderImpl();
		return additionalDataHolder;
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
