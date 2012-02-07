/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.schedule.events.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import scenario.schedule.events.CharterOutVisit;
import scenario.schedule.events.EventsFactory;
import scenario.schedule.events.EventsPackage;
import scenario.schedule.events.FuelMixture;
import scenario.schedule.events.FuelPurpose;
import scenario.schedule.events.FuelQuantity;
import scenario.schedule.events.FuelType;
import scenario.schedule.events.FuelUnit;
import scenario.schedule.events.Idle;
import scenario.schedule.events.Journey;
import scenario.schedule.events.PortVisit;
import scenario.schedule.events.ScheduledEvent;
import scenario.schedule.events.SlotVisit;
import scenario.schedule.events.VesselEventVisit;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!-- end-user-doc -->
 * 
 * @generated
 */
public class EventsFactoryImpl extends EFactoryImpl implements EventsFactory {
	/**
	 * Creates the default factory implementation. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static EventsFactory init() {
		try {
			final EventsFactory theEventsFactory = (EventsFactory) EPackage.Registry.INSTANCE.getEFactory("http://com.mmxlabs.lng.emf2/schedule/events");
			if (theEventsFactory != null) {
				return theEventsFactory;
			}
		} catch (final Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new EventsFactoryImpl();
	}

	/**
	 * Creates an instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EventsFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EObject create(final EClass eClass) {
		switch (eClass.getClassifierID()) {
		case EventsPackage.FUEL_MIXTURE:
			return createFuelMixture();
		case EventsPackage.FUEL_QUANTITY:
			return createFuelQuantity();
		case EventsPackage.SCHEDULED_EVENT:
			return createScheduledEvent();
		case EventsPackage.JOURNEY:
			return createJourney();
		case EventsPackage.PORT_VISIT:
			return createPortVisit();
		case EventsPackage.IDLE:
			return createIdle();
		case EventsPackage.SLOT_VISIT:
			return createSlotVisit();
		case EventsPackage.VESSEL_EVENT_VISIT:
			return createVesselEventVisit();
		case EventsPackage.CHARTER_OUT_VISIT:
			return createCharterOutVisit();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object createFromString(final EDataType eDataType, final String initialValue) {
		switch (eDataType.getClassifierID()) {
		case EventsPackage.FUEL_UNIT:
			return createFuelUnitFromString(eDataType, initialValue);
		case EventsPackage.FUEL_PURPOSE:
			return createFuelPurposeFromString(eDataType, initialValue);
		case EventsPackage.FUEL_TYPE:
			return createFuelTypeFromString(eDataType, initialValue);
		default:
			throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String convertToString(final EDataType eDataType, final Object instanceValue) {
		switch (eDataType.getClassifierID()) {
		case EventsPackage.FUEL_UNIT:
			return convertFuelUnitToString(eDataType, instanceValue);
		case EventsPackage.FUEL_PURPOSE:
			return convertFuelPurposeToString(eDataType, instanceValue);
		case EventsPackage.FUEL_TYPE:
			return convertFuelTypeToString(eDataType, instanceValue);
		default:
			throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public FuelMixture createFuelMixture() {
		final FuelMixtureImpl fuelMixture = new FuelMixtureImpl();
		return fuelMixture;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public FuelQuantity createFuelQuantity() {
		final FuelQuantityImpl fuelQuantity = new FuelQuantityImpl();
		return fuelQuantity;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public ScheduledEvent createScheduledEvent() {
		final ScheduledEventImpl scheduledEvent = new ScheduledEventImpl();
		return scheduledEvent;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Idle createIdle() {
		final IdleImpl idle = new IdleImpl();
		return idle;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Journey createJourney() {
		final JourneyImpl journey = new JourneyImpl();
		return journey;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public PortVisit createPortVisit() {
		final PortVisitImpl portVisit = new PortVisitImpl();
		return portVisit;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public SlotVisit createSlotVisit() {
		final SlotVisitImpl slotVisit = new SlotVisitImpl();
		return slotVisit;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public CharterOutVisit createCharterOutVisit() {
		final CharterOutVisitImpl charterOutVisit = new CharterOutVisitImpl();
		return charterOutVisit;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public VesselEventVisit createVesselEventVisit() {
		final VesselEventVisitImpl vesselEventVisit = new VesselEventVisitImpl();
		return vesselEventVisit;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public FuelUnit createFuelUnitFromString(final EDataType eDataType, final String initialValue) {
		final FuelUnit result = FuelUnit.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertFuelUnitToString(final EDataType eDataType, final Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public FuelPurpose createFuelPurposeFromString(final EDataType eDataType, final String initialValue) {
		final FuelPurpose result = FuelPurpose.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertFuelPurposeToString(final EDataType eDataType, final Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public FuelType createFuelTypeFromString(final EDataType eDataType, final String initialValue) {
		final FuelType result = FuelType.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertFuelTypeToString(final EDataType eDataType, final Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EventsPackage getEventsPackage() {
		return (EventsPackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static EventsPackage getPackage() {
		return EventsPackage.eINSTANCE;
	}

} // EventsFactoryImpl
