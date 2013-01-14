/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;

import java.util.Calendar;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.mmxlabs.models.lng.schedule.AdditionalData;
import com.mmxlabs.models.lng.schedule.AdditionalDataHolder;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fitness;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.UnscheduledCargo;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SchedulePackageImpl extends EPackageImpl implements SchedulePackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass scheduleModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass scheduleEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass sequenceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eventEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass slotVisitEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass vesselEventVisitEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass journeyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass idleEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass generatedCharterOutEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass unscheduledCargoEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass fuelUsageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass fuelQuantityEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass cooldownEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass cargoAllocationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass slotAllocationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass fuelAmountEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass fitnessEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass portVisitEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass additionalDataEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass additionalDataHolderEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass startEventEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass endEventEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum fuelUnitEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum fuelEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum sequenceTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType calendarEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType iterableEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType objectEDataType = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private SchedulePackageImpl() {
		super(eNS_URI, ScheduleFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link SchedulePackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static SchedulePackage init() {
		if (isInited) return (SchedulePackage)EPackage.Registry.INSTANCE.getEPackage(SchedulePackage.eNS_URI);

		// Obtain or create and register package
		SchedulePackageImpl theSchedulePackage = (SchedulePackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof SchedulePackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new SchedulePackageImpl());

		isInited = true;

		// Initialize simple dependencies
		TypesPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theSchedulePackage.createPackageContents();

		// Initialize created meta-data
		theSchedulePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theSchedulePackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(SchedulePackage.eNS_URI, theSchedulePackage);
		return theSchedulePackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getScheduleModel() {
		return scheduleModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getScheduleModel_InitialSchedule() {
		return (EReference)scheduleModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getScheduleModel_OptimisedSchedule() {
		return (EReference)scheduleModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getScheduleModel_Dirty() {
		return (EAttribute)scheduleModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSchedule() {
		return scheduleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSchedule_Complete() {
		return (EAttribute)scheduleEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchedule_Sequences() {
		return (EReference)scheduleEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchedule_UnscheduledCargoes() {
		return (EReference)scheduleEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchedule_CargoAllocations() {
		return (EReference)scheduleEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchedule_SlotAllocations() {
		return (EReference)scheduleEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchedule_Fitnesses() {
		return (EReference)scheduleEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchedule_UnusedElements() {
		return (EReference)scheduleEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSequence() {
		return sequenceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSequence_Events() {
		return (EReference)sequenceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSequence_Vessel() {
		return (EReference)sequenceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSequence_VesselClass() {
		return (EReference)sequenceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSequence_Fitnesses() {
		return (EReference)sequenceEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSequence_DailyHireRate() {
		return (EAttribute)sequenceEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSequence_SpotIndex() {
		return (EAttribute)sequenceEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSequence_SequenceType() {
		return (EAttribute)sequenceEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSequence__GetName() {
		return sequenceEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSequence__IsSpotVessel() {
		return sequenceEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSequence__IsFleetVessel() {
		return sequenceEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSequence__IsTimeCharterVessel() {
		return sequenceEClass.getEOperations().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEvent() {
		return eventEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEvent_Start() {
		return (EAttribute)eventEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEvent_End() {
		return (EAttribute)eventEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEvent_Port() {
		return (EReference)eventEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEvent_PreviousEvent() {
		return (EReference)eventEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEvent_NextEvent() {
		return (EReference)eventEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEvent_Sequence() {
		return (EReference)eventEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEvent__GetDuration() {
		return eventEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEvent__GetLocalStart() {
		return eventEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEvent__GetLocalEnd() {
		return eventEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEvent__Type() {
		return eventEClass.getEOperations().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEvent__Name() {
		return eventEClass.getEOperations().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEvent__GetHireCost() {
		return eventEClass.getEOperations().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSlotVisit() {
		return slotVisitEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSlotVisit_SlotAllocation() {
		return (EReference)slotVisitEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getVesselEventVisit() {
		return vesselEventVisitEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVesselEventVisit_VesselEvent() {
		return (EReference)vesselEventVisitEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getJourney() {
		return journeyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getJourney_Destination() {
		return (EReference)journeyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getJourney_Laden() {
		return (EAttribute)journeyEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getJourney_Route() {
		return (EAttribute)journeyEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getJourney_Toll() {
		return (EAttribute)journeyEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getJourney_Distance() {
		return (EAttribute)journeyEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getJourney_Speed() {
		return (EAttribute)journeyEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIdle() {
		return idleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIdle_Laden() {
		return (EAttribute)idleEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGeneratedCharterOut() {
		return generatedCharterOutEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGeneratedCharterOut_Revenue() {
		return (EAttribute)generatedCharterOutEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUnscheduledCargo() {
		return unscheduledCargoEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUnscheduledCargo_LoadAllocation() {
		return (EReference)unscheduledCargoEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUnscheduledCargo_DischargeAllocation() {
		return (EReference)unscheduledCargoEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFuelUsage() {
		return fuelUsageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFuelUsage_Fuels() {
		return (EReference)fuelUsageEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getFuelUsage__GetFuelCost() {
		return fuelUsageEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFuelQuantity() {
		return fuelQuantityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFuelQuantity_Fuel() {
		return (EAttribute)fuelQuantityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFuelQuantity_Cost() {
		return (EAttribute)fuelQuantityEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFuelQuantity_Amounts() {
		return (EReference)fuelQuantityEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCooldown() {
		return cooldownEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCooldown_Volume() {
		return (EAttribute)cooldownEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCooldown_Cost() {
		return (EAttribute)cooldownEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCargoAllocation() {
		return cargoAllocationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCargoAllocation_LoadAllocation() {
		return (EReference)cargoAllocationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCargoAllocation_DischargeAllocation() {
		return (EReference)cargoAllocationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCargoAllocation_LoadVolume() {
		return (EAttribute)cargoAllocationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCargoAllocation_DischargeVolume() {
		return (EAttribute)cargoAllocationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCargoAllocation_InputCargo() {
		return (EReference)cargoAllocationEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCargoAllocation_LadenLeg() {
		return (EReference)cargoAllocationEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCargoAllocation_BallastLeg() {
		return (EReference)cargoAllocationEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCargoAllocation_LadenIdle() {
		return (EReference)cargoAllocationEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCargoAllocation_BallastIdle() {
		return (EReference)cargoAllocationEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCargoAllocation_Sequence() {
		return (EReference)cargoAllocationEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getCargoAllocation__GetName() {
		return cargoAllocationEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getCargoAllocation__GetType() {
		return cargoAllocationEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSlotAllocation() {
		return slotAllocationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSlotAllocation_Slot() {
		return (EReference)slotAllocationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSlotAllocation_SpotMarket() {
		return (EReference)slotAllocationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSlotAllocation_CargoAllocation() {
		return (EReference)slotAllocationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSlotAllocation_SlotVisit() {
		return (EReference)slotAllocationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlotAllocation_Price() {
		return (EAttribute)slotAllocationEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSlotAllocation__GetPort() {
		return slotAllocationEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSlotAllocation__GetLocalStart() {
		return slotAllocationEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSlotAllocation__GetLocalEnd() {
		return slotAllocationEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSlotAllocation__GetContract() {
		return slotAllocationEClass.getEOperations().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSlotAllocation__GetName() {
		return slotAllocationEClass.getEOperations().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFuelAmount() {
		return fuelAmountEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFuelAmount_Unit() {
		return (EAttribute)fuelAmountEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFuelAmount_Quantity() {
		return (EAttribute)fuelAmountEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFitness() {
		return fitnessEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFitness_FitnessValue() {
		return (EAttribute)fitnessEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPortVisit() {
		return portVisitEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPortVisit_PortCost() {
		return (EAttribute)portVisitEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAdditionalData() {
		return additionalDataEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAdditionalData_Key() {
		return (EAttribute)additionalDataEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAdditionalData_Value() {
		return (EAttribute)additionalDataEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAdditionalData_Render() {
		return (EAttribute)additionalDataEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getAdditionalData__GetIntValue() {
		return additionalDataEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getAdditionalData__Format() {
		return additionalDataEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getAdditionalData__GetIntegerValue() {
		return additionalDataEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAdditionalDataHolder() {
		return additionalDataHolderEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAdditionalDataHolder_AdditionalData() {
		return (EReference)additionalDataHolderEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getAdditionalDataHolder__GetAdditionalDataWithKey__String() {
		return additionalDataHolderEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getAdditionalDataHolder__GetAdditionalDataWithPath__Iterable() {
		return additionalDataHolderEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStartEvent() {
		return startEventEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getStartEvent_SlotAllocation() {
		return (EReference)startEventEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEndEvent() {
		return endEventEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEndEvent_SlotAllocation() {
		return (EReference)endEventEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getFuelUnit() {
		return fuelUnitEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getFuel() {
		return fuelEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getSequenceType() {
		return sequenceTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getCalendar() {
		return calendarEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getIterable() {
		return iterableEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getObject() {
		return objectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScheduleFactory getScheduleFactory() {
		return (ScheduleFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		scheduleModelEClass = createEClass(SCHEDULE_MODEL);
		createEReference(scheduleModelEClass, SCHEDULE_MODEL__INITIAL_SCHEDULE);
		createEReference(scheduleModelEClass, SCHEDULE_MODEL__OPTIMISED_SCHEDULE);
		createEAttribute(scheduleModelEClass, SCHEDULE_MODEL__DIRTY);

		scheduleEClass = createEClass(SCHEDULE);
		createEAttribute(scheduleEClass, SCHEDULE__COMPLETE);
		createEReference(scheduleEClass, SCHEDULE__SEQUENCES);
		createEReference(scheduleEClass, SCHEDULE__UNSCHEDULED_CARGOES);
		createEReference(scheduleEClass, SCHEDULE__CARGO_ALLOCATIONS);
		createEReference(scheduleEClass, SCHEDULE__SLOT_ALLOCATIONS);
		createEReference(scheduleEClass, SCHEDULE__FITNESSES);
		createEReference(scheduleEClass, SCHEDULE__UNUSED_ELEMENTS);

		sequenceEClass = createEClass(SEQUENCE);
		createEReference(sequenceEClass, SEQUENCE__EVENTS);
		createEReference(sequenceEClass, SEQUENCE__VESSEL);
		createEReference(sequenceEClass, SEQUENCE__VESSEL_CLASS);
		createEReference(sequenceEClass, SEQUENCE__FITNESSES);
		createEAttribute(sequenceEClass, SEQUENCE__DAILY_HIRE_RATE);
		createEAttribute(sequenceEClass, SEQUENCE__SPOT_INDEX);
		createEAttribute(sequenceEClass, SEQUENCE__SEQUENCE_TYPE);
		createEOperation(sequenceEClass, SEQUENCE___GET_NAME);
		createEOperation(sequenceEClass, SEQUENCE___IS_SPOT_VESSEL);
		createEOperation(sequenceEClass, SEQUENCE___IS_FLEET_VESSEL);
		createEOperation(sequenceEClass, SEQUENCE___IS_TIME_CHARTER_VESSEL);

		eventEClass = createEClass(EVENT);
		createEAttribute(eventEClass, EVENT__START);
		createEAttribute(eventEClass, EVENT__END);
		createEReference(eventEClass, EVENT__PORT);
		createEReference(eventEClass, EVENT__PREVIOUS_EVENT);
		createEReference(eventEClass, EVENT__NEXT_EVENT);
		createEReference(eventEClass, EVENT__SEQUENCE);
		createEOperation(eventEClass, EVENT___GET_DURATION);
		createEOperation(eventEClass, EVENT___GET_LOCAL_START);
		createEOperation(eventEClass, EVENT___GET_LOCAL_END);
		createEOperation(eventEClass, EVENT___TYPE);
		createEOperation(eventEClass, EVENT___NAME);
		createEOperation(eventEClass, EVENT___GET_HIRE_COST);

		slotVisitEClass = createEClass(SLOT_VISIT);
		createEReference(slotVisitEClass, SLOT_VISIT__SLOT_ALLOCATION);

		vesselEventVisitEClass = createEClass(VESSEL_EVENT_VISIT);
		createEReference(vesselEventVisitEClass, VESSEL_EVENT_VISIT__VESSEL_EVENT);

		journeyEClass = createEClass(JOURNEY);
		createEReference(journeyEClass, JOURNEY__DESTINATION);
		createEAttribute(journeyEClass, JOURNEY__LADEN);
		createEAttribute(journeyEClass, JOURNEY__ROUTE);
		createEAttribute(journeyEClass, JOURNEY__TOLL);
		createEAttribute(journeyEClass, JOURNEY__DISTANCE);
		createEAttribute(journeyEClass, JOURNEY__SPEED);

		idleEClass = createEClass(IDLE);
		createEAttribute(idleEClass, IDLE__LADEN);

		generatedCharterOutEClass = createEClass(GENERATED_CHARTER_OUT);
		createEAttribute(generatedCharterOutEClass, GENERATED_CHARTER_OUT__REVENUE);

		unscheduledCargoEClass = createEClass(UNSCHEDULED_CARGO);
		createEReference(unscheduledCargoEClass, UNSCHEDULED_CARGO__LOAD_ALLOCATION);
		createEReference(unscheduledCargoEClass, UNSCHEDULED_CARGO__DISCHARGE_ALLOCATION);

		fuelUsageEClass = createEClass(FUEL_USAGE);
		createEReference(fuelUsageEClass, FUEL_USAGE__FUELS);
		createEOperation(fuelUsageEClass, FUEL_USAGE___GET_FUEL_COST);

		fuelQuantityEClass = createEClass(FUEL_QUANTITY);
		createEAttribute(fuelQuantityEClass, FUEL_QUANTITY__FUEL);
		createEAttribute(fuelQuantityEClass, FUEL_QUANTITY__COST);
		createEReference(fuelQuantityEClass, FUEL_QUANTITY__AMOUNTS);

		cooldownEClass = createEClass(COOLDOWN);
		createEAttribute(cooldownEClass, COOLDOWN__VOLUME);
		createEAttribute(cooldownEClass, COOLDOWN__COST);

		cargoAllocationEClass = createEClass(CARGO_ALLOCATION);
		createEReference(cargoAllocationEClass, CARGO_ALLOCATION__LOAD_ALLOCATION);
		createEReference(cargoAllocationEClass, CARGO_ALLOCATION__DISCHARGE_ALLOCATION);
		createEAttribute(cargoAllocationEClass, CARGO_ALLOCATION__LOAD_VOLUME);
		createEAttribute(cargoAllocationEClass, CARGO_ALLOCATION__DISCHARGE_VOLUME);
		createEReference(cargoAllocationEClass, CARGO_ALLOCATION__INPUT_CARGO);
		createEReference(cargoAllocationEClass, CARGO_ALLOCATION__LADEN_LEG);
		createEReference(cargoAllocationEClass, CARGO_ALLOCATION__BALLAST_LEG);
		createEReference(cargoAllocationEClass, CARGO_ALLOCATION__LADEN_IDLE);
		createEReference(cargoAllocationEClass, CARGO_ALLOCATION__BALLAST_IDLE);
		createEReference(cargoAllocationEClass, CARGO_ALLOCATION__SEQUENCE);
		createEOperation(cargoAllocationEClass, CARGO_ALLOCATION___GET_NAME);
		createEOperation(cargoAllocationEClass, CARGO_ALLOCATION___GET_TYPE);

		slotAllocationEClass = createEClass(SLOT_ALLOCATION);
		createEReference(slotAllocationEClass, SLOT_ALLOCATION__SLOT);
		createEReference(slotAllocationEClass, SLOT_ALLOCATION__SPOT_MARKET);
		createEReference(slotAllocationEClass, SLOT_ALLOCATION__CARGO_ALLOCATION);
		createEReference(slotAllocationEClass, SLOT_ALLOCATION__SLOT_VISIT);
		createEAttribute(slotAllocationEClass, SLOT_ALLOCATION__PRICE);
		createEOperation(slotAllocationEClass, SLOT_ALLOCATION___GET_PORT);
		createEOperation(slotAllocationEClass, SLOT_ALLOCATION___GET_LOCAL_START);
		createEOperation(slotAllocationEClass, SLOT_ALLOCATION___GET_LOCAL_END);
		createEOperation(slotAllocationEClass, SLOT_ALLOCATION___GET_CONTRACT);
		createEOperation(slotAllocationEClass, SLOT_ALLOCATION___GET_NAME);

		fuelAmountEClass = createEClass(FUEL_AMOUNT);
		createEAttribute(fuelAmountEClass, FUEL_AMOUNT__UNIT);
		createEAttribute(fuelAmountEClass, FUEL_AMOUNT__QUANTITY);

		fitnessEClass = createEClass(FITNESS);
		createEAttribute(fitnessEClass, FITNESS__FITNESS_VALUE);

		portVisitEClass = createEClass(PORT_VISIT);
		createEAttribute(portVisitEClass, PORT_VISIT__PORT_COST);

		additionalDataEClass = createEClass(ADDITIONAL_DATA);
		createEAttribute(additionalDataEClass, ADDITIONAL_DATA__KEY);
		createEAttribute(additionalDataEClass, ADDITIONAL_DATA__VALUE);
		createEAttribute(additionalDataEClass, ADDITIONAL_DATA__RENDER);
		createEOperation(additionalDataEClass, ADDITIONAL_DATA___GET_INT_VALUE);
		createEOperation(additionalDataEClass, ADDITIONAL_DATA___FORMAT);
		createEOperation(additionalDataEClass, ADDITIONAL_DATA___GET_INTEGER_VALUE);

		additionalDataHolderEClass = createEClass(ADDITIONAL_DATA_HOLDER);
		createEReference(additionalDataHolderEClass, ADDITIONAL_DATA_HOLDER__ADDITIONAL_DATA);
		createEOperation(additionalDataHolderEClass, ADDITIONAL_DATA_HOLDER___GET_ADDITIONAL_DATA_WITH_KEY__STRING);
		createEOperation(additionalDataHolderEClass, ADDITIONAL_DATA_HOLDER___GET_ADDITIONAL_DATA_WITH_PATH__ITERABLE);

		startEventEClass = createEClass(START_EVENT);
		createEReference(startEventEClass, START_EVENT__SLOT_ALLOCATION);

		endEventEClass = createEClass(END_EVENT);
		createEReference(endEventEClass, END_EVENT__SLOT_ALLOCATION);

		// Create enums
		fuelUnitEEnum = createEEnum(FUEL_UNIT);
		fuelEEnum = createEEnum(FUEL);
		sequenceTypeEEnum = createEEnum(SEQUENCE_TYPE);

		// Create data types
		calendarEDataType = createEDataType(CALENDAR);
		iterableEDataType = createEDataType(ITERABLE);
		objectEDataType = createEDataType(OBJECT);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		MMXCorePackage theMMXCorePackage = (MMXCorePackage)EPackage.Registry.INSTANCE.getEPackage(MMXCorePackage.eNS_URI);
		TypesPackage theTypesPackage = (TypesPackage)EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);

		// Create type parameters
		addETypeParameter(iterableEDataType, "T");

		// Set bounds for type parameters

		// Add supertypes to classes
		scheduleModelEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		scheduleEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		sequenceEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		eventEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		eventEClass.getESuperTypes().add(theTypesPackage.getITimezoneProvider());
		slotVisitEClass.getESuperTypes().add(this.getEvent());
		slotVisitEClass.getESuperTypes().add(this.getFuelUsage());
		slotVisitEClass.getESuperTypes().add(this.getPortVisit());
		vesselEventVisitEClass.getESuperTypes().add(this.getEvent());
		vesselEventVisitEClass.getESuperTypes().add(this.getPortVisit());
		vesselEventVisitEClass.getESuperTypes().add(theTypesPackage.getExtraDataContainer());
		journeyEClass.getESuperTypes().add(this.getEvent());
		journeyEClass.getESuperTypes().add(this.getFuelUsage());
		idleEClass.getESuperTypes().add(this.getEvent());
		idleEClass.getESuperTypes().add(this.getFuelUsage());
		generatedCharterOutEClass.getESuperTypes().add(this.getEvent());
		generatedCharterOutEClass.getESuperTypes().add(theTypesPackage.getExtraDataContainer());
		unscheduledCargoEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		cooldownEClass.getESuperTypes().add(this.getEvent());
		cooldownEClass.getESuperTypes().add(this.getFuelUsage());
		cargoAllocationEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		cargoAllocationEClass.getESuperTypes().add(this.getAdditionalDataHolder());
		cargoAllocationEClass.getESuperTypes().add(theTypesPackage.getExtraDataContainer());
		slotAllocationEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		fitnessEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		portVisitEClass.getESuperTypes().add(this.getEvent());
		additionalDataEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		additionalDataEClass.getESuperTypes().add(this.getAdditionalDataHolder());
		startEventEClass.getESuperTypes().add(this.getEvent());
		startEventEClass.getESuperTypes().add(this.getFuelUsage());
		startEventEClass.getESuperTypes().add(this.getPortVisit());
		startEventEClass.getESuperTypes().add(theTypesPackage.getExtraDataContainer());
		endEventEClass.getESuperTypes().add(this.getEvent());
		endEventEClass.getESuperTypes().add(this.getFuelUsage());
		endEventEClass.getESuperTypes().add(this.getPortVisit());
		endEventEClass.getESuperTypes().add(theTypesPackage.getExtraDataContainer());

		// Initialize classes, features, and operations; add parameters
		initEClass(scheduleModelEClass, ScheduleModel.class, "ScheduleModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getScheduleModel_InitialSchedule(), this.getSchedule(), null, "initialSchedule", null, 1, 1, ScheduleModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getScheduleModel_OptimisedSchedule(), this.getSchedule(), null, "optimisedSchedule", null, 1, 1, ScheduleModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScheduleModel_Dirty(), ecorePackage.getEBoolean(), "dirty", null, 1, 1, ScheduleModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(scheduleEClass, Schedule.class, "Schedule", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSchedule_Complete(), ecorePackage.getEBoolean(), "complete", null, 1, 1, Schedule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSchedule_Sequences(), this.getSequence(), null, "sequences", null, 0, -1, Schedule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSchedule_UnscheduledCargoes(), this.getUnscheduledCargo(), null, "unscheduledCargoes", null, 0, -1, Schedule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSchedule_CargoAllocations(), this.getCargoAllocation(), null, "cargoAllocations", null, 0, -1, Schedule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSchedule_SlotAllocations(), this.getSlotAllocation(), null, "slotAllocations", null, 0, -1, Schedule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSchedule_Fitnesses(), this.getFitness(), null, "fitnesses", null, 0, -1, Schedule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSchedule_UnusedElements(), ecorePackage.getEObject(), null, "unusedElements", null, 0, -1, Schedule.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(sequenceEClass, Sequence.class, "Sequence", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSequence_Events(), this.getEvent(), this.getEvent_Sequence(), "events", null, 0, -1, Sequence.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSequence_Vessel(), theTypesPackage.getAVessel(), null, "vessel", null, 1, 1, Sequence.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSequence_VesselClass(), theTypesPackage.getAVesselClass(), null, "vesselClass", null, 1, 1, Sequence.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSequence_Fitnesses(), this.getFitness(), null, "fitnesses", null, 0, -1, Sequence.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSequence_DailyHireRate(), ecorePackage.getEInt(), "dailyHireRate", null, 1, 1, Sequence.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSequence_SpotIndex(), ecorePackage.getEInt(), "spotIndex", null, 1, 1, Sequence.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSequence_SequenceType(), this.getSequenceType(), "sequenceType", null, 0, 1, Sequence.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getSequence__GetName(), ecorePackage.getEString(), "getName", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSequence__IsSpotVessel(), ecorePackage.getEBoolean(), "isSpotVessel", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSequence__IsFleetVessel(), ecorePackage.getEBoolean(), "isFleetVessel", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSequence__IsTimeCharterVessel(), ecorePackage.getEBoolean(), "isTimeCharterVessel", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(eventEClass, Event.class, "Event", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEvent_Start(), ecorePackage.getEDate(), "start", null, 1, 1, Event.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEvent_End(), ecorePackage.getEDate(), "end", null, 1, 1, Event.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEvent_Port(), theTypesPackage.getAPort(), null, "port", null, 1, 1, Event.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEvent_PreviousEvent(), this.getEvent(), this.getEvent_NextEvent(), "previousEvent", null, 0, 1, Event.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEvent_NextEvent(), this.getEvent(), this.getEvent_PreviousEvent(), "nextEvent", null, 0, 1, Event.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEvent_Sequence(), this.getSequence(), this.getSequence_Events(), "sequence", null, 0, 1, Event.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getEvent__GetDuration(), ecorePackage.getEInt(), "getDuration", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getEvent__GetLocalStart(), this.getCalendar(), "getLocalStart", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getEvent__GetLocalEnd(), this.getCalendar(), "getLocalEnd", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getEvent__Type(), ecorePackage.getEString(), "type", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getEvent__Name(), ecorePackage.getEString(), "name", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getEvent__GetHireCost(), ecorePackage.getEInt(), "getHireCost", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(slotVisitEClass, SlotVisit.class, "SlotVisit", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSlotVisit_SlotAllocation(), this.getSlotAllocation(), this.getSlotAllocation_SlotVisit(), "slotAllocation", null, 1, 1, SlotVisit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(vesselEventVisitEClass, VesselEventVisit.class, "VesselEventVisit", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getVesselEventVisit_VesselEvent(), theTypesPackage.getAVesselEvent(), null, "vesselEvent", null, 1, 1, VesselEventVisit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(journeyEClass, Journey.class, "Journey", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getJourney_Destination(), theTypesPackage.getAPort(), null, "destination", null, 1, 1, Journey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJourney_Laden(), ecorePackage.getEBoolean(), "laden", null, 1, 1, Journey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJourney_Route(), ecorePackage.getEString(), "route", null, 1, 1, Journey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJourney_Toll(), ecorePackage.getEInt(), "toll", null, 1, 1, Journey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJourney_Distance(), ecorePackage.getEInt(), "distance", null, 1, 1, Journey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJourney_Speed(), ecorePackage.getEDouble(), "speed", null, 1, 1, Journey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(idleEClass, Idle.class, "Idle", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIdle_Laden(), ecorePackage.getEBoolean(), "laden", null, 1, 1, Idle.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(generatedCharterOutEClass, GeneratedCharterOut.class, "GeneratedCharterOut", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getGeneratedCharterOut_Revenue(), ecorePackage.getEInt(), "revenue", null, 1, 1, GeneratedCharterOut.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(unscheduledCargoEClass, UnscheduledCargo.class, "UnscheduledCargo", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getUnscheduledCargo_LoadAllocation(), this.getSlotAllocation(), null, "loadAllocation", null, 1, 1, UnscheduledCargo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getUnscheduledCargo_DischargeAllocation(), this.getSlotAllocation(), null, "dischargeAllocation", null, 1, 1, UnscheduledCargo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(fuelUsageEClass, FuelUsage.class, "FuelUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFuelUsage_Fuels(), this.getFuelQuantity(), null, "fuels", null, 0, -1, FuelUsage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getFuelUsage__GetFuelCost(), ecorePackage.getEInt(), "getFuelCost", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(fuelQuantityEClass, FuelQuantity.class, "FuelQuantity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFuelQuantity_Fuel(), this.getFuel(), "fuel", null, 1, 1, FuelQuantity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFuelQuantity_Cost(), ecorePackage.getEInt(), "cost", null, 1, 1, FuelQuantity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFuelQuantity_Amounts(), this.getFuelAmount(), null, "amounts", null, 0, -1, FuelQuantity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(cooldownEClass, Cooldown.class, "Cooldown", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCooldown_Volume(), ecorePackage.getEInt(), "volume", null, 1, 1, Cooldown.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCooldown_Cost(), ecorePackage.getEInt(), "cost", null, 1, 1, Cooldown.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(cargoAllocationEClass, CargoAllocation.class, "CargoAllocation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCargoAllocation_LoadAllocation(), this.getSlotAllocation(), null, "loadAllocation", null, 1, 1, CargoAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargoAllocation_DischargeAllocation(), this.getSlotAllocation(), null, "dischargeAllocation", null, 1, 1, CargoAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCargoAllocation_LoadVolume(), ecorePackage.getEInt(), "loadVolume", null, 1, 1, CargoAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCargoAllocation_DischargeVolume(), ecorePackage.getEInt(), "dischargeVolume", null, 1, 1, CargoAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargoAllocation_InputCargo(), theTypesPackage.getACargo(), null, "inputCargo", null, 1, 1, CargoAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargoAllocation_LadenLeg(), this.getJourney(), null, "ladenLeg", null, 1, 1, CargoAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargoAllocation_BallastLeg(), this.getJourney(), null, "ballastLeg", null, 1, 1, CargoAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargoAllocation_LadenIdle(), this.getIdle(), null, "ladenIdle", null, 1, 1, CargoAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargoAllocation_BallastIdle(), this.getIdle(), null, "ballastIdle", null, 1, 1, CargoAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargoAllocation_Sequence(), this.getSequence(), null, "sequence", null, 1, 1, CargoAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getCargoAllocation__GetName(), ecorePackage.getEString(), "getName", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getCargoAllocation__GetType(), ecorePackage.getEString(), "getType", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(slotAllocationEClass, SlotAllocation.class, "SlotAllocation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSlotAllocation_Slot(), theTypesPackage.getASlot(), null, "slot", null, 1, 1, SlotAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSlotAllocation_SpotMarket(), theTypesPackage.getASpotMarket(), null, "spotMarket", null, 1, 1, SlotAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSlotAllocation_CargoAllocation(), this.getCargoAllocation(), null, "cargoAllocation", null, 1, 1, SlotAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSlotAllocation_SlotVisit(), this.getSlotVisit(), this.getSlotVisit_SlotAllocation(), "slotVisit", null, 1, 1, SlotAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlotAllocation_Price(), ecorePackage.getEDouble(), "price", null, 0, 1, SlotAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getSlotAllocation__GetPort(), theTypesPackage.getAPort(), "getPort", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlotAllocation__GetLocalStart(), this.getCalendar(), "getLocalStart", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlotAllocation__GetLocalEnd(), this.getCalendar(), "getLocalEnd", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlotAllocation__GetContract(), theTypesPackage.getAContract(), "getContract", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlotAllocation__GetName(), ecorePackage.getEString(), "getName", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(fuelAmountEClass, FuelAmount.class, "FuelAmount", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFuelAmount_Unit(), this.getFuelUnit(), "unit", null, 1, 1, FuelAmount.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFuelAmount_Quantity(), ecorePackage.getEInt(), "quantity", null, 1, 1, FuelAmount.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(fitnessEClass, Fitness.class, "Fitness", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFitness_FitnessValue(), ecorePackage.getELong(), "fitnessValue", null, 1, 1, Fitness.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(portVisitEClass, PortVisit.class, "PortVisit", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPortVisit_PortCost(), ecorePackage.getEInt(), "portCost", null, 1, 1, PortVisit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(additionalDataEClass, AdditionalData.class, "AdditionalData", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAdditionalData_Key(), ecorePackage.getEString(), "key", null, 1, 1, AdditionalData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAdditionalData_Value(), this.getObject(), "value", null, 1, 1, AdditionalData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAdditionalData_Render(), ecorePackage.getEString(), "render", null, 1, 1, AdditionalData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getAdditionalData__GetIntValue(), ecorePackage.getEInt(), "getIntValue", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getAdditionalData__Format(), ecorePackage.getEString(), "format", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getAdditionalData__GetIntegerValue(), ecorePackage.getEIntegerObject(), "getIntegerValue", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(additionalDataHolderEClass, AdditionalDataHolder.class, "AdditionalDataHolder", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAdditionalDataHolder_AdditionalData(), this.getAdditionalData(), null, "additionalData", null, 0, -1, AdditionalDataHolder.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		EOperation op = initEOperation(getAdditionalDataHolder__GetAdditionalDataWithKey__String(), this.getAdditionalData(), "getAdditionalDataWithKey", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "key", 1, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getAdditionalDataHolder__GetAdditionalDataWithPath__Iterable(), this.getAdditionalData(), "getAdditionalDataWithPath", 1, 1, IS_UNIQUE, IS_ORDERED);
		EGenericType g1 = createEGenericType(this.getIterable());
		EGenericType g2 = createEGenericType(ecorePackage.getEString());
		g1.getETypeArguments().add(g2);
		addEParameter(op, g1, "keys", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(startEventEClass, StartEvent.class, "StartEvent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getStartEvent_SlotAllocation(), this.getSlotAllocation(), null, "slotAllocation", null, 1, 1, StartEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(endEventEClass, EndEvent.class, "EndEvent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEndEvent_SlotAllocation(), this.getSlotAllocation(), null, "slotAllocation", null, 1, 1, EndEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(fuelUnitEEnum, FuelUnit.class, "FuelUnit");
		addEEnumLiteral(fuelUnitEEnum, FuelUnit.M3);
		addEEnumLiteral(fuelUnitEEnum, FuelUnit.MT);
		addEEnumLiteral(fuelUnitEEnum, FuelUnit.MMBTU);

		initEEnum(fuelEEnum, Fuel.class, "Fuel");
		addEEnumLiteral(fuelEEnum, Fuel.BASE_FUEL);
		addEEnumLiteral(fuelEEnum, Fuel.FBO);
		addEEnumLiteral(fuelEEnum, Fuel.NBO);
		addEEnumLiteral(fuelEEnum, Fuel.PILOT_LIGHT);

		initEEnum(sequenceTypeEEnum, SequenceType.class, "SequenceType");
		addEEnumLiteral(sequenceTypeEEnum, SequenceType.VESSEL);
		addEEnumLiteral(sequenceTypeEEnum, SequenceType.SPOT_VESSEL);
		addEEnumLiteral(sequenceTypeEEnum, SequenceType.DES_PURCHASE);
		addEEnumLiteral(sequenceTypeEEnum, SequenceType.FOB_SALE);
		addEEnumLiteral(sequenceTypeEEnum, SequenceType.CARGO_SHORTS);

		// Initialize data types
		initEDataType(calendarEDataType, Calendar.class, "Calendar", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(iterableEDataType, Iterable.class, "Iterable", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(objectEDataType, Object.class, "Object", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.mmxlabs.com/models/mmxcore/validation/NamedObject
		createNamedObjectAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http://www.mmxlabs.com/models/mmxcore/validation/NamedObject</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createNamedObjectAnnotations() {
		String source = "http://www.mmxlabs.com/models/mmxcore/validation/NamedObject";			
		addAnnotation
		  (additionalDataEClass, 
		   source, 
		   new String[] {
			 "nonUnique", "true"
		   });					
	}

} //SchedulePackageImpl
