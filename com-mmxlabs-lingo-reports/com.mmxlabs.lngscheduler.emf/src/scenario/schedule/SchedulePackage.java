/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */
 

package scenario.schedule;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
	 * The number of structural features of the '<em>Schedule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULE_FEATURE_COUNT = 4;

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
	 * The number of structural features of the '<em>Sequence</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE_FEATURE_COUNT = 2;


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
	 * The number of structural features of the '<em>Cargo Allocation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ALLOCATION_FEATURE_COUNT = 8;


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

	}

} //SchedulePackage
