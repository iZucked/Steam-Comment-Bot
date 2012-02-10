/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.schedule.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import scenario.schedule.*;
import scenario.schedule.BookedRevenue;
import scenario.schedule.CargoAllocation;
import scenario.schedule.CargoRevenue;
import scenario.schedule.Schedule;
import scenario.schedule.ScheduleFactory;
import scenario.schedule.ScheduleFitness;
import scenario.schedule.ScheduleModel;
import scenario.schedule.SchedulePackage;
import scenario.schedule.Sequence;
import scenario.schedule.VesselEventRevenue;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!-- end-user-doc -->
 * @generated
 */
public class ScheduleFactoryImpl extends EFactoryImpl implements ScheduleFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static ScheduleFactory init() {
		try {
			ScheduleFactory theScheduleFactory = (ScheduleFactory)EPackage.Registry.INSTANCE.getEFactory("http://com.mmxlabs.lng.emf2/schedule"); 
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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ScheduleFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case SchedulePackage.SCHEDULE_MODEL: return createScheduleModel();
			case SchedulePackage.SCHEDULE: return createSchedule();
			case SchedulePackage.SEQUENCE: return createSequence();
			case SchedulePackage.CARGO_ALLOCATION: return createCargoAllocation();
			case SchedulePackage.SCHEDULE_FITNESS: return createScheduleFitness();
			case SchedulePackage.BOOKED_REVENUE: return createBookedRevenue();
			case SchedulePackage.CARGO_REVENUE: return createCargoRevenue();
			case SchedulePackage.VESSEL_EVENT_REVENUE: return createVesselEventRevenue();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ScheduleModel createScheduleModel() {
		ScheduleModelImpl scheduleModel = new ScheduleModelImpl();
		return scheduleModel;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Schedule createSchedule() {
		ScheduleImpl schedule = new ScheduleImpl();
		return schedule;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Sequence createSequence() {
		SequenceImpl sequence = new SequenceImpl();
		return sequence;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CargoAllocation createCargoAllocation() {
		CargoAllocationImpl cargoAllocation = new CargoAllocationImpl();
		return cargoAllocation;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ScheduleFitness createScheduleFitness() {
		ScheduleFitnessImpl scheduleFitness = new ScheduleFitnessImpl();
		return scheduleFitness;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BookedRevenue createBookedRevenue() {
		BookedRevenueImpl bookedRevenue = new BookedRevenueImpl();
		return bookedRevenue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CargoRevenue createCargoRevenue() {
		CargoRevenueImpl cargoRevenue = new CargoRevenueImpl();
		return cargoRevenue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VesselEventRevenue createVesselEventRevenue() {
		VesselEventRevenueImpl vesselEventRevenue = new VesselEventRevenueImpl();
		return vesselEventRevenue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SchedulePackage getSchedulePackage() {
		return (SchedulePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static SchedulePackage getPackage() {
		return SchedulePackage.eINSTANCE;
	}

} // ScheduleFactoryImpl
