/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;

import java.util.Calendar;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.mmxlabs.models.datetime.DateTimePackage;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.pricing.PricingPackage;
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
import com.mmxlabs.models.lng.schedule.ExposureDetail;
import com.mmxlabs.models.lng.schedule.Fitness;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.GeneralPNLDetails;
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
import com.mmxlabs.models.lng.schedule.SlotAllocationType;
import com.mmxlabs.models.lng.schedule.SlotPNLDetails;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import java.lang.Iterable;

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
	private EClass marketAllocationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass openSlotAllocationEClass = null;

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
	private EClass capacityViolationsHolderEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass capacityMapEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass profitAndLossContainerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass groupProfitAndLossEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass entityProfitAndLossEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass entityPNLDetailsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass slotPNLDetailsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass generalPNLDetailsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass basicSlotPNLDetailsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eventGroupingEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass portVisitLatenessEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass exposureDetailEClass = null;

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
	private EEnum capacityViolationTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum portVisitLatenessTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum slotAllocationTypeEEnum = null;

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
		CargoPackage.eINSTANCE.eClass();

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
	public EReference getScheduleModel_Schedule() {
		return (EReference)scheduleModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getScheduleModel_Dirty() {
		return (EAttribute)scheduleModelEClass.getEStructuralFeatures().get(1);
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
	public EReference getSchedule_Sequences() {
		return (EReference)scheduleEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchedule_CargoAllocations() {
		return (EReference)scheduleEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchedule_OpenSlotAllocations() {
		return (EReference)scheduleEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchedule_MarketAllocations() {
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
	public EReference getSequence_VesselAvailability() {
		return (EReference)sequenceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSequence_CharterInMarket() {
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
	public EAttribute getSequence_SpotIndex() {
		return (EAttribute)sequenceEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSequence_SequenceType() {
		return (EAttribute)sequenceEClass.getEStructuralFeatures().get(5);
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
	public EAttribute getEvent_CharterCost() {
		return (EAttribute)eventEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEvent_HeelAtStart() {
		return (EAttribute)eventEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEvent_HeelAtEnd() {
		return (EAttribute)eventEClass.getEStructuralFeatures().get(8);
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
	public EOperation getEvent__Type() {
		return eventEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getEvent__Name() {
		return eventEClass.getEOperations().get(2);
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
	public EReference getVesselEventVisit_RedeliveryPort() {
		return (EReference)vesselEventVisitEClass.getEStructuralFeatures().get(1);
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
	public EReference getJourney_Route() {
		return (EReference)journeyEClass.getEStructuralFeatures().get(2);
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
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGeneratedCharterOut() {
		return generatedCharterOutEClass;
	}

	/**
	 * <!-- begin-user-doc -->
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
	public EReference getCargoAllocation_SlotAllocations() {
		return (EReference)cargoAllocationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCargoAllocation_Sequence() {
		return (EReference)cargoAllocationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCargoAllocation_CargoType() {
		return (EAttribute)cargoAllocationEClass.getEStructuralFeatures().get(2);
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
	public EClass getMarketAllocation() {
		return marketAllocationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMarketAllocation_Slot() {
		return (EReference)marketAllocationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMarketAllocation_Market() {
		return (EReference)marketAllocationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMarketAllocation_SlotAllocation() {
		return (EReference)marketAllocationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMarketAllocation_Price() {
		return (EAttribute)marketAllocationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMarketAllocation_SlotVisit() {
		return (EReference)marketAllocationEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getOpenSlotAllocation() {
		return openSlotAllocationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOpenSlotAllocation_Slot() {
		return (EReference)openSlotAllocationEClass.getEStructuralFeatures().get(0);
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
	public EReference getSlotAllocation_MarketAllocation() {
		return (EReference)slotAllocationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSlotAllocation_SlotVisit() {
		return (EReference)slotAllocationEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlotAllocation_Price() {
		return (EAttribute)slotAllocationEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlotAllocation_VolumeTransferred() {
		return (EAttribute)slotAllocationEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlotAllocation_EnergyTransferred() {
		return (EAttribute)slotAllocationEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlotAllocation_Cv() {
		return (EAttribute)slotAllocationEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlotAllocation_VolumeValue() {
		return (EAttribute)slotAllocationEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSlotAllocation_Exposures() {
		return (EReference)slotAllocationEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlotAllocation_PhysicalVolumeTransferred() {
		return (EAttribute)slotAllocationEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlotAllocation_PhysicalEnergyTransferred() {
		return (EAttribute)slotAllocationEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSlotAllocation_SlotAllocationType() {
		return (EAttribute)slotAllocationEClass.getEStructuralFeatures().get(13);
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
	public EOperation getSlotAllocation__GetContract() {
		return slotAllocationEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getSlotAllocation__GetName() {
		return slotAllocationEClass.getEOperations().get(2);
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
	public EAttribute getFuelAmount_UnitPrice() {
		return (EAttribute)fuelAmountEClass.getEStructuralFeatures().get(2);
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
	public EReference getPortVisit_Lateness() {
		return (EReference)portVisitEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPortVisit_HeelCost() {
		return (EAttribute)portVisitEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPortVisit_HeelRevenue() {
		return (EAttribute)portVisitEClass.getEStructuralFeatures().get(3);
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
	public EClass getCapacityViolationsHolder() {
		return capacityViolationsHolderEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCapacityViolationsHolder_Violations() {
		return (EReference)capacityViolationsHolderEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCapacityMapEntry() {
		return capacityMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCapacityMapEntry_Key() {
		return (EAttribute)capacityMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCapacityMapEntry_Value() {
		return (EAttribute)capacityMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getProfitAndLossContainer() {
		return profitAndLossContainerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getProfitAndLossContainer_GroupProfitAndLoss() {
		return (EReference)profitAndLossContainerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getProfitAndLossContainer_GeneralPNLDetails() {
		return (EReference)profitAndLossContainerEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGroupProfitAndLoss() {
		return groupProfitAndLossEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGroupProfitAndLoss_ProfitAndLoss() {
		return (EAttribute)groupProfitAndLossEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGroupProfitAndLoss_ProfitAndLossPreTax() {
		return (EAttribute)groupProfitAndLossEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGroupProfitAndLoss_TaxValue() {
		return (EAttribute)groupProfitAndLossEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGroupProfitAndLoss_EntityProfitAndLosses() {
		return (EReference)groupProfitAndLossEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEntityProfitAndLoss() {
		return entityProfitAndLossEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEntityProfitAndLoss_Entity() {
		return (EReference)entityProfitAndLossEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEntityProfitAndLoss_EntityBook() {
		return (EReference)entityProfitAndLossEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEntityProfitAndLoss_ProfitAndLoss() {
		return (EAttribute)entityProfitAndLossEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEntityProfitAndLoss_ProfitAndLossPreTax() {
		return (EAttribute)entityProfitAndLossEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEntityPNLDetails() {
		return entityPNLDetailsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEntityPNLDetails_Entity() {
		return (EReference)entityPNLDetailsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEntityPNLDetails_GeneralPNLDetails() {
		return (EReference)entityPNLDetailsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSlotPNLDetails() {
		return slotPNLDetailsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSlotPNLDetails_Slot() {
		return (EReference)slotPNLDetailsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSlotPNLDetails_GeneralPNLDetails() {
		return (EReference)slotPNLDetailsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGeneralPNLDetails() {
		return generalPNLDetailsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBasicSlotPNLDetails() {
		return basicSlotPNLDetailsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBasicSlotPNLDetails_ExtraShippingPNL() {
		return (EAttribute)basicSlotPNLDetailsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBasicSlotPNLDetails_AdditionalPNL() {
		return (EAttribute)basicSlotPNLDetailsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBasicSlotPNLDetails_CancellationFees() {
		return (EAttribute)basicSlotPNLDetailsEClass.getEStructuralFeatures().get(2);
	}


	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBasicSlotPNLDetails_HedgingValue() {
		return (EAttribute)basicSlotPNLDetailsEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBasicSlotPNLDetails_MiscCostsValue() {
		return (EAttribute)basicSlotPNLDetailsEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBasicSlotPNLDetails_ExtraUpsidePNL() {
		return (EAttribute)basicSlotPNLDetailsEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEventGrouping() {
		return eventGroupingEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEventGrouping_Events() {
		return (EReference)eventGroupingEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPortVisitLateness() {
		return portVisitLatenessEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPortVisitLateness_Type() {
		return (EAttribute)portVisitLatenessEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPortVisitLateness_LatenessInHours() {
		return (EAttribute)portVisitLatenessEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getExposureDetail() {
		return exposureDetailEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getExposureDetail_Index() {
		return (EReference)exposureDetailEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExposureDetail_Date() {
		return (EAttribute)exposureDetailEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExposureDetail_VolumeInMMBTU() {
		return (EAttribute)exposureDetailEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExposureDetail_VolumeInNativeUnits() {
		return (EAttribute)exposureDetailEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExposureDetail_UnitPrice() {
		return (EAttribute)exposureDetailEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExposureDetail_NativeValue() {
		return (EAttribute)exposureDetailEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExposureDetail_VolumeUnit() {
		return (EAttribute)exposureDetailEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExposureDetail_CurrencyUnit() {
		return (EAttribute)exposureDetailEClass.getEStructuralFeatures().get(7);
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
	public EEnum getCapacityViolationType() {
		return capacityViolationTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getPortVisitLatenessType() {
		return portVisitLatenessTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getSlotAllocationType() {
		return slotAllocationTypeEEnum;
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
		createEReference(scheduleModelEClass, SCHEDULE_MODEL__SCHEDULE);
		createEAttribute(scheduleModelEClass, SCHEDULE_MODEL__DIRTY);

		scheduleEClass = createEClass(SCHEDULE);
		createEReference(scheduleEClass, SCHEDULE__SEQUENCES);
		createEReference(scheduleEClass, SCHEDULE__CARGO_ALLOCATIONS);
		createEReference(scheduleEClass, SCHEDULE__OPEN_SLOT_ALLOCATIONS);
		createEReference(scheduleEClass, SCHEDULE__MARKET_ALLOCATIONS);
		createEReference(scheduleEClass, SCHEDULE__SLOT_ALLOCATIONS);
		createEReference(scheduleEClass, SCHEDULE__FITNESSES);
		createEReference(scheduleEClass, SCHEDULE__UNUSED_ELEMENTS);

		fitnessEClass = createEClass(FITNESS);
		createEAttribute(fitnessEClass, FITNESS__FITNESS_VALUE);

		cargoAllocationEClass = createEClass(CARGO_ALLOCATION);
		createEReference(cargoAllocationEClass, CARGO_ALLOCATION__SLOT_ALLOCATIONS);
		createEReference(cargoAllocationEClass, CARGO_ALLOCATION__SEQUENCE);
		createEAttribute(cargoAllocationEClass, CARGO_ALLOCATION__CARGO_TYPE);
		createEOperation(cargoAllocationEClass, CARGO_ALLOCATION___GET_NAME);

		marketAllocationEClass = createEClass(MARKET_ALLOCATION);
		createEReference(marketAllocationEClass, MARKET_ALLOCATION__SLOT);
		createEReference(marketAllocationEClass, MARKET_ALLOCATION__MARKET);
		createEReference(marketAllocationEClass, MARKET_ALLOCATION__SLOT_ALLOCATION);
		createEAttribute(marketAllocationEClass, MARKET_ALLOCATION__PRICE);
		createEReference(marketAllocationEClass, MARKET_ALLOCATION__SLOT_VISIT);

		openSlotAllocationEClass = createEClass(OPEN_SLOT_ALLOCATION);
		createEReference(openSlotAllocationEClass, OPEN_SLOT_ALLOCATION__SLOT);

		slotAllocationEClass = createEClass(SLOT_ALLOCATION);
		createEReference(slotAllocationEClass, SLOT_ALLOCATION__SLOT);
		createEReference(slotAllocationEClass, SLOT_ALLOCATION__SPOT_MARKET);
		createEReference(slotAllocationEClass, SLOT_ALLOCATION__CARGO_ALLOCATION);
		createEReference(slotAllocationEClass, SLOT_ALLOCATION__MARKET_ALLOCATION);
		createEReference(slotAllocationEClass, SLOT_ALLOCATION__SLOT_VISIT);
		createEAttribute(slotAllocationEClass, SLOT_ALLOCATION__PRICE);
		createEAttribute(slotAllocationEClass, SLOT_ALLOCATION__VOLUME_TRANSFERRED);
		createEAttribute(slotAllocationEClass, SLOT_ALLOCATION__ENERGY_TRANSFERRED);
		createEAttribute(slotAllocationEClass, SLOT_ALLOCATION__CV);
		createEAttribute(slotAllocationEClass, SLOT_ALLOCATION__VOLUME_VALUE);
		createEReference(slotAllocationEClass, SLOT_ALLOCATION__EXPOSURES);
		createEAttribute(slotAllocationEClass, SLOT_ALLOCATION__PHYSICAL_VOLUME_TRANSFERRED);
		createEAttribute(slotAllocationEClass, SLOT_ALLOCATION__PHYSICAL_ENERGY_TRANSFERRED);
		createEAttribute(slotAllocationEClass, SLOT_ALLOCATION__SLOT_ALLOCATION_TYPE);
		createEOperation(slotAllocationEClass, SLOT_ALLOCATION___GET_PORT);
		createEOperation(slotAllocationEClass, SLOT_ALLOCATION___GET_CONTRACT);
		createEOperation(slotAllocationEClass, SLOT_ALLOCATION___GET_NAME);

		sequenceEClass = createEClass(SEQUENCE);
		createEReference(sequenceEClass, SEQUENCE__EVENTS);
		createEReference(sequenceEClass, SEQUENCE__VESSEL_AVAILABILITY);
		createEReference(sequenceEClass, SEQUENCE__CHARTER_IN_MARKET);
		createEReference(sequenceEClass, SEQUENCE__FITNESSES);
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
		createEAttribute(eventEClass, EVENT__CHARTER_COST);
		createEAttribute(eventEClass, EVENT__HEEL_AT_START);
		createEAttribute(eventEClass, EVENT__HEEL_AT_END);
		createEOperation(eventEClass, EVENT___GET_DURATION);
		createEOperation(eventEClass, EVENT___TYPE);
		createEOperation(eventEClass, EVENT___NAME);

		startEventEClass = createEClass(START_EVENT);
		createEReference(startEventEClass, START_EVENT__SLOT_ALLOCATION);

		endEventEClass = createEClass(END_EVENT);
		createEReference(endEventEClass, END_EVENT__SLOT_ALLOCATION);

		journeyEClass = createEClass(JOURNEY);
		createEReference(journeyEClass, JOURNEY__DESTINATION);
		createEAttribute(journeyEClass, JOURNEY__LADEN);
		createEReference(journeyEClass, JOURNEY__ROUTE);
		createEAttribute(journeyEClass, JOURNEY__TOLL);
		createEAttribute(journeyEClass, JOURNEY__DISTANCE);
		createEAttribute(journeyEClass, JOURNEY__SPEED);

		idleEClass = createEClass(IDLE);
		createEAttribute(idleEClass, IDLE__LADEN);

		portVisitEClass = createEClass(PORT_VISIT);
		createEAttribute(portVisitEClass, PORT_VISIT__PORT_COST);
		createEReference(portVisitEClass, PORT_VISIT__LATENESS);
		createEAttribute(portVisitEClass, PORT_VISIT__HEEL_COST);
		createEAttribute(portVisitEClass, PORT_VISIT__HEEL_REVENUE);

		slotVisitEClass = createEClass(SLOT_VISIT);
		createEReference(slotVisitEClass, SLOT_VISIT__SLOT_ALLOCATION);

		vesselEventVisitEClass = createEClass(VESSEL_EVENT_VISIT);
		createEReference(vesselEventVisitEClass, VESSEL_EVENT_VISIT__VESSEL_EVENT);
		createEReference(vesselEventVisitEClass, VESSEL_EVENT_VISIT__REDELIVERY_PORT);

		generatedCharterOutEClass = createEClass(GENERATED_CHARTER_OUT);
		createEAttribute(generatedCharterOutEClass, GENERATED_CHARTER_OUT__REVENUE);

		cooldownEClass = createEClass(COOLDOWN);
		createEAttribute(cooldownEClass, COOLDOWN__VOLUME);
		createEAttribute(cooldownEClass, COOLDOWN__COST);

		fuelUsageEClass = createEClass(FUEL_USAGE);
		createEReference(fuelUsageEClass, FUEL_USAGE__FUELS);
		createEOperation(fuelUsageEClass, FUEL_USAGE___GET_FUEL_COST);

		fuelQuantityEClass = createEClass(FUEL_QUANTITY);
		createEAttribute(fuelQuantityEClass, FUEL_QUANTITY__FUEL);
		createEAttribute(fuelQuantityEClass, FUEL_QUANTITY__COST);
		createEReference(fuelQuantityEClass, FUEL_QUANTITY__AMOUNTS);

		fuelAmountEClass = createEClass(FUEL_AMOUNT);
		createEAttribute(fuelAmountEClass, FUEL_AMOUNT__UNIT);
		createEAttribute(fuelAmountEClass, FUEL_AMOUNT__QUANTITY);
		createEAttribute(fuelAmountEClass, FUEL_AMOUNT__UNIT_PRICE);

		capacityViolationsHolderEClass = createEClass(CAPACITY_VIOLATIONS_HOLDER);
		createEReference(capacityViolationsHolderEClass, CAPACITY_VIOLATIONS_HOLDER__VIOLATIONS);

		capacityMapEntryEClass = createEClass(CAPACITY_MAP_ENTRY);
		createEAttribute(capacityMapEntryEClass, CAPACITY_MAP_ENTRY__KEY);
		createEAttribute(capacityMapEntryEClass, CAPACITY_MAP_ENTRY__VALUE);

		profitAndLossContainerEClass = createEClass(PROFIT_AND_LOSS_CONTAINER);
		createEReference(profitAndLossContainerEClass, PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS);
		createEReference(profitAndLossContainerEClass, PROFIT_AND_LOSS_CONTAINER__GENERAL_PNL_DETAILS);

		groupProfitAndLossEClass = createEClass(GROUP_PROFIT_AND_LOSS);
		createEAttribute(groupProfitAndLossEClass, GROUP_PROFIT_AND_LOSS__PROFIT_AND_LOSS);
		createEAttribute(groupProfitAndLossEClass, GROUP_PROFIT_AND_LOSS__PROFIT_AND_LOSS_PRE_TAX);
		createEAttribute(groupProfitAndLossEClass, GROUP_PROFIT_AND_LOSS__TAX_VALUE);
		createEReference(groupProfitAndLossEClass, GROUP_PROFIT_AND_LOSS__ENTITY_PROFIT_AND_LOSSES);

		entityProfitAndLossEClass = createEClass(ENTITY_PROFIT_AND_LOSS);
		createEReference(entityProfitAndLossEClass, ENTITY_PROFIT_AND_LOSS__ENTITY);
		createEReference(entityProfitAndLossEClass, ENTITY_PROFIT_AND_LOSS__ENTITY_BOOK);
		createEAttribute(entityProfitAndLossEClass, ENTITY_PROFIT_AND_LOSS__PROFIT_AND_LOSS);
		createEAttribute(entityProfitAndLossEClass, ENTITY_PROFIT_AND_LOSS__PROFIT_AND_LOSS_PRE_TAX);

		entityPNLDetailsEClass = createEClass(ENTITY_PNL_DETAILS);
		createEReference(entityPNLDetailsEClass, ENTITY_PNL_DETAILS__ENTITY);
		createEReference(entityPNLDetailsEClass, ENTITY_PNL_DETAILS__GENERAL_PNL_DETAILS);

		slotPNLDetailsEClass = createEClass(SLOT_PNL_DETAILS);
		createEReference(slotPNLDetailsEClass, SLOT_PNL_DETAILS__SLOT);
		createEReference(slotPNLDetailsEClass, SLOT_PNL_DETAILS__GENERAL_PNL_DETAILS);

		generalPNLDetailsEClass = createEClass(GENERAL_PNL_DETAILS);

		basicSlotPNLDetailsEClass = createEClass(BASIC_SLOT_PNL_DETAILS);
		createEAttribute(basicSlotPNLDetailsEClass, BASIC_SLOT_PNL_DETAILS__EXTRA_SHIPPING_PNL);
		createEAttribute(basicSlotPNLDetailsEClass, BASIC_SLOT_PNL_DETAILS__ADDITIONAL_PNL);
		createEAttribute(basicSlotPNLDetailsEClass, BASIC_SLOT_PNL_DETAILS__CANCELLATION_FEES);
		createEAttribute(basicSlotPNLDetailsEClass, BASIC_SLOT_PNL_DETAILS__HEDGING_VALUE);
		createEAttribute(basicSlotPNLDetailsEClass, BASIC_SLOT_PNL_DETAILS__MISC_COSTS_VALUE);
		createEAttribute(basicSlotPNLDetailsEClass, BASIC_SLOT_PNL_DETAILS__EXTRA_UPSIDE_PNL);

		eventGroupingEClass = createEClass(EVENT_GROUPING);
		createEReference(eventGroupingEClass, EVENT_GROUPING__EVENTS);

		portVisitLatenessEClass = createEClass(PORT_VISIT_LATENESS);
		createEAttribute(portVisitLatenessEClass, PORT_VISIT_LATENESS__TYPE);
		createEAttribute(portVisitLatenessEClass, PORT_VISIT_LATENESS__LATENESS_IN_HOURS);

		exposureDetailEClass = createEClass(EXPOSURE_DETAIL);
		createEReference(exposureDetailEClass, EXPOSURE_DETAIL__INDEX);
		createEAttribute(exposureDetailEClass, EXPOSURE_DETAIL__DATE);
		createEAttribute(exposureDetailEClass, EXPOSURE_DETAIL__VOLUME_IN_MMBTU);
		createEAttribute(exposureDetailEClass, EXPOSURE_DETAIL__VOLUME_IN_NATIVE_UNITS);
		createEAttribute(exposureDetailEClass, EXPOSURE_DETAIL__UNIT_PRICE);
		createEAttribute(exposureDetailEClass, EXPOSURE_DETAIL__NATIVE_VALUE);
		createEAttribute(exposureDetailEClass, EXPOSURE_DETAIL__VOLUME_UNIT);
		createEAttribute(exposureDetailEClass, EXPOSURE_DETAIL__CURRENCY_UNIT);

		// Create enums
		sequenceTypeEEnum = createEEnum(SEQUENCE_TYPE);
		fuelEEnum = createEEnum(FUEL);
		fuelUnitEEnum = createEEnum(FUEL_UNIT);
		capacityViolationTypeEEnum = createEEnum(CAPACITY_VIOLATION_TYPE);
		portVisitLatenessTypeEEnum = createEEnum(PORT_VISIT_LATENESS_TYPE);
		slotAllocationTypeEEnum = createEEnum(SLOT_ALLOCATION_TYPE);

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
		CargoPackage theCargoPackage = (CargoPackage)EPackage.Registry.INSTANCE.getEPackage(CargoPackage.eNS_URI);
		SpotMarketsPackage theSpotMarketsPackage = (SpotMarketsPackage)EPackage.Registry.INSTANCE.getEPackage(SpotMarketsPackage.eNS_URI);
		PortPackage thePortPackage = (PortPackage)EPackage.Registry.INSTANCE.getEPackage(PortPackage.eNS_URI);
		CommercialPackage theCommercialPackage = (CommercialPackage)EPackage.Registry.INSTANCE.getEPackage(CommercialPackage.eNS_URI);
		TypesPackage theTypesPackage = (TypesPackage)EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);
		DateTimePackage theDateTimePackage = (DateTimePackage)EPackage.Registry.INSTANCE.getEPackage(DateTimePackage.eNS_URI);
		PricingPackage thePricingPackage = (PricingPackage)EPackage.Registry.INSTANCE.getEPackage(PricingPackage.eNS_URI);

		// Create type parameters
		addETypeParameter(iterableEDataType, "T");

		// Set bounds for type parameters

		// Add supertypes to classes
		scheduleModelEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		scheduleModelEClass.getESuperTypes().add(theMMXCorePackage.getMMXResultRoot());
		scheduleEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		fitnessEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		cargoAllocationEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		cargoAllocationEClass.getESuperTypes().add(this.getProfitAndLossContainer());
		cargoAllocationEClass.getESuperTypes().add(this.getEventGrouping());
		marketAllocationEClass.getESuperTypes().add(this.getProfitAndLossContainer());
		openSlotAllocationEClass.getESuperTypes().add(this.getProfitAndLossContainer());
		slotAllocationEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		sequenceEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		eventEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		eventEClass.getESuperTypes().add(theTypesPackage.getITimezoneProvider());
		startEventEClass.getESuperTypes().add(this.getEvent());
		startEventEClass.getESuperTypes().add(this.getFuelUsage());
		startEventEClass.getESuperTypes().add(this.getPortVisit());
		startEventEClass.getESuperTypes().add(this.getProfitAndLossContainer());
		startEventEClass.getESuperTypes().add(this.getEventGrouping());
		endEventEClass.getESuperTypes().add(this.getEvent());
		endEventEClass.getESuperTypes().add(this.getFuelUsage());
		endEventEClass.getESuperTypes().add(this.getPortVisit());
		endEventEClass.getESuperTypes().add(this.getProfitAndLossContainer());
		endEventEClass.getESuperTypes().add(this.getEventGrouping());
		journeyEClass.getESuperTypes().add(this.getEvent());
		journeyEClass.getESuperTypes().add(this.getFuelUsage());
		idleEClass.getESuperTypes().add(this.getEvent());
		idleEClass.getESuperTypes().add(this.getFuelUsage());
		portVisitEClass.getESuperTypes().add(this.getEvent());
		portVisitEClass.getESuperTypes().add(this.getCapacityViolationsHolder());
		slotVisitEClass.getESuperTypes().add(this.getEvent());
		slotVisitEClass.getESuperTypes().add(this.getFuelUsage());
		slotVisitEClass.getESuperTypes().add(this.getPortVisit());
		vesselEventVisitEClass.getESuperTypes().add(this.getEvent());
		vesselEventVisitEClass.getESuperTypes().add(this.getPortVisit());
		vesselEventVisitEClass.getESuperTypes().add(this.getProfitAndLossContainer());
		vesselEventVisitEClass.getESuperTypes().add(this.getEventGrouping());
		generatedCharterOutEClass.getESuperTypes().add(this.getPortVisit());
		generatedCharterOutEClass.getESuperTypes().add(this.getProfitAndLossContainer());
		generatedCharterOutEClass.getESuperTypes().add(this.getEventGrouping());
		cooldownEClass.getESuperTypes().add(this.getEvent());
		cooldownEClass.getESuperTypes().add(this.getFuelUsage());
		capacityViolationsHolderEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		profitAndLossContainerEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		entityPNLDetailsEClass.getESuperTypes().add(this.getGeneralPNLDetails());
		slotPNLDetailsEClass.getESuperTypes().add(this.getGeneralPNLDetails());
		basicSlotPNLDetailsEClass.getESuperTypes().add(this.getGeneralPNLDetails());

		// Initialize classes, features, and operations; add parameters
		initEClass(scheduleModelEClass, ScheduleModel.class, "ScheduleModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getScheduleModel_Schedule(), this.getSchedule(), null, "schedule", null, 1, 1, ScheduleModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScheduleModel_Dirty(), ecorePackage.getEBoolean(), "dirty", null, 1, 1, ScheduleModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(scheduleEClass, Schedule.class, "Schedule", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSchedule_Sequences(), this.getSequence(), null, "sequences", null, 0, -1, Schedule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSchedule_CargoAllocations(), this.getCargoAllocation(), null, "cargoAllocations", null, 0, -1, Schedule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSchedule_OpenSlotAllocations(), this.getOpenSlotAllocation(), null, "openSlotAllocations", null, 0, -1, Schedule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSchedule_MarketAllocations(), this.getMarketAllocation(), null, "marketAllocations", null, 0, -1, Schedule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSchedule_SlotAllocations(), this.getSlotAllocation(), null, "slotAllocations", null, 0, -1, Schedule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSchedule_Fitnesses(), this.getFitness(), null, "fitnesses", null, 0, -1, Schedule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSchedule_UnusedElements(), ecorePackage.getEObject(), null, "unusedElements", null, 0, -1, Schedule.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(fitnessEClass, Fitness.class, "Fitness", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFitness_FitnessValue(), ecorePackage.getELong(), "fitnessValue", null, 1, 1, Fitness.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(cargoAllocationEClass, CargoAllocation.class, "CargoAllocation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCargoAllocation_SlotAllocations(), this.getSlotAllocation(), this.getSlotAllocation_CargoAllocation(), "slotAllocations", null, 1, -1, CargoAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCargoAllocation_Sequence(), this.getSequence(), null, "sequence", null, 1, 1, CargoAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCargoAllocation_CargoType(), theCargoPackage.getCargoType(), "cargoType", null, 0, 1, CargoAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getCargoAllocation__GetName(), ecorePackage.getEString(), "getName", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(marketAllocationEClass, MarketAllocation.class, "MarketAllocation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMarketAllocation_Slot(), theCargoPackage.getSlot(), null, "slot", null, 1, 1, MarketAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMarketAllocation_Market(), theSpotMarketsPackage.getSpotMarket(), null, "market", null, 1, 1, MarketAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMarketAllocation_SlotAllocation(), this.getSlotAllocation(), this.getSlotAllocation_MarketAllocation(), "slotAllocation", null, 0, 1, MarketAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMarketAllocation_Price(), ecorePackage.getEDouble(), "price", null, 0, 1, MarketAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMarketAllocation_SlotVisit(), this.getSlotVisit(), null, "slotVisit", null, 0, 1, MarketAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(openSlotAllocationEClass, OpenSlotAllocation.class, "OpenSlotAllocation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getOpenSlotAllocation_Slot(), theCargoPackage.getSlot(), null, "slot", null, 1, 1, OpenSlotAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(slotAllocationEClass, SlotAllocation.class, "SlotAllocation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSlotAllocation_Slot(), theCargoPackage.getSlot(), null, "slot", null, 1, 1, SlotAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSlotAllocation_SpotMarket(), theSpotMarketsPackage.getSpotMarket(), null, "spotMarket", null, 1, 1, SlotAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSlotAllocation_CargoAllocation(), this.getCargoAllocation(), this.getCargoAllocation_SlotAllocations(), "cargoAllocation", null, 1, 1, SlotAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSlotAllocation_MarketAllocation(), this.getMarketAllocation(), this.getMarketAllocation_SlotAllocation(), "marketAllocation", null, 1, 1, SlotAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSlotAllocation_SlotVisit(), this.getSlotVisit(), this.getSlotVisit_SlotAllocation(), "slotVisit", null, 1, 1, SlotAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlotAllocation_Price(), ecorePackage.getEDouble(), "price", null, 0, 1, SlotAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlotAllocation_VolumeTransferred(), ecorePackage.getEInt(), "volumeTransferred", null, 1, 1, SlotAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlotAllocation_EnergyTransferred(), ecorePackage.getEInt(), "energyTransferred", null, 1, 1, SlotAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlotAllocation_Cv(), ecorePackage.getEDouble(), "cv", null, 1, 1, SlotAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlotAllocation_VolumeValue(), ecorePackage.getEInt(), "volumeValue", null, 0, 1, SlotAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSlotAllocation_Exposures(), this.getExposureDetail(), null, "exposures", null, 0, -1, SlotAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlotAllocation_PhysicalVolumeTransferred(), ecorePackage.getEInt(), "physicalVolumeTransferred", null, 1, 1, SlotAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlotAllocation_PhysicalEnergyTransferred(), ecorePackage.getEInt(), "physicalEnergyTransferred", null, 1, 1, SlotAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSlotAllocation_SlotAllocationType(), this.getSlotAllocationType(), "slotAllocationType", null, 0, 1, SlotAllocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getSlotAllocation__GetPort(), thePortPackage.getPort(), "getPort", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlotAllocation__GetContract(), theCommercialPackage.getContract(), "getContract", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSlotAllocation__GetName(), ecorePackage.getEString(), "getName", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(sequenceEClass, Sequence.class, "Sequence", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSequence_Events(), this.getEvent(), this.getEvent_Sequence(), "events", null, 0, -1, Sequence.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSequence_VesselAvailability(), theCargoPackage.getVesselAvailability(), null, "vesselAvailability", null, 1, 1, Sequence.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSequence_CharterInMarket(), theSpotMarketsPackage.getCharterInMarket(), null, "charterInMarket", null, 1, 1, Sequence.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSequence_Fitnesses(), this.getFitness(), null, "fitnesses", null, 0, -1, Sequence.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSequence_SpotIndex(), ecorePackage.getEInt(), "spotIndex", null, 1, 1, Sequence.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSequence_SequenceType(), this.getSequenceType(), "sequenceType", null, 0, 1, Sequence.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getSequence__GetName(), ecorePackage.getEString(), "getName", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSequence__IsSpotVessel(), ecorePackage.getEBoolean(), "isSpotVessel", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSequence__IsFleetVessel(), ecorePackage.getEBoolean(), "isFleetVessel", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getSequence__IsTimeCharterVessel(), ecorePackage.getEBoolean(), "isTimeCharterVessel", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(eventEClass, Event.class, "Event", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEvent_Start(), theDateTimePackage.getDateTime(), "start", null, 1, 1, Event.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEvent_End(), theDateTimePackage.getDateTime(), "end", null, 1, 1, Event.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEvent_Port(), thePortPackage.getPort(), null, "port", null, 1, 1, Event.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEvent_PreviousEvent(), this.getEvent(), this.getEvent_NextEvent(), "previousEvent", null, 0, 1, Event.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEvent_NextEvent(), this.getEvent(), this.getEvent_PreviousEvent(), "nextEvent", null, 0, 1, Event.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEvent_Sequence(), this.getSequence(), this.getSequence_Events(), "sequence", null, 0, 1, Event.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEvent_CharterCost(), ecorePackage.getEInt(), "charterCost", null, 0, 1, Event.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEvent_HeelAtStart(), ecorePackage.getEInt(), "heelAtStart", null, 0, 1, Event.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEvent_HeelAtEnd(), ecorePackage.getEInt(), "heelAtEnd", null, 0, 1, Event.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getEvent__GetDuration(), ecorePackage.getEInt(), "getDuration", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getEvent__Type(), ecorePackage.getEString(), "type", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getEvent__Name(), ecorePackage.getEString(), "name", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(startEventEClass, StartEvent.class, "StartEvent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getStartEvent_SlotAllocation(), this.getSlotAllocation(), null, "slotAllocation", null, 1, 1, StartEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(endEventEClass, EndEvent.class, "EndEvent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEndEvent_SlotAllocation(), this.getSlotAllocation(), null, "slotAllocation", null, 1, 1, EndEvent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(journeyEClass, Journey.class, "Journey", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getJourney_Destination(), thePortPackage.getPort(), null, "destination", null, 1, 1, Journey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJourney_Laden(), ecorePackage.getEBoolean(), "laden", null, 1, 1, Journey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getJourney_Route(), thePortPackage.getRoute(), null, "route", null, 0, 1, Journey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJourney_Toll(), ecorePackage.getEInt(), "toll", null, 1, 1, Journey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJourney_Distance(), ecorePackage.getEInt(), "distance", null, 1, 1, Journey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJourney_Speed(), ecorePackage.getEDouble(), "speed", null, 1, 1, Journey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(idleEClass, Idle.class, "Idle", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIdle_Laden(), ecorePackage.getEBoolean(), "laden", null, 1, 1, Idle.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(portVisitEClass, PortVisit.class, "PortVisit", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPortVisit_PortCost(), ecorePackage.getEInt(), "portCost", null, 1, 1, PortVisit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPortVisit_Lateness(), this.getPortVisitLateness(), null, "lateness", null, 0, 1, PortVisit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPortVisit_HeelCost(), ecorePackage.getEInt(), "heelCost", null, 0, 1, PortVisit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPortVisit_HeelRevenue(), ecorePackage.getEInt(), "heelRevenue", null, 0, 1, PortVisit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(slotVisitEClass, SlotVisit.class, "SlotVisit", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSlotVisit_SlotAllocation(), this.getSlotAllocation(), this.getSlotAllocation_SlotVisit(), "slotAllocation", null, 1, 1, SlotVisit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(vesselEventVisitEClass, VesselEventVisit.class, "VesselEventVisit", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getVesselEventVisit_VesselEvent(), theCargoPackage.getVesselEvent(), null, "vesselEvent", null, 1, 1, VesselEventVisit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVesselEventVisit_RedeliveryPort(), thePortPackage.getPort(), null, "redeliveryPort", null, 1, 1, VesselEventVisit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(generatedCharterOutEClass, GeneratedCharterOut.class, "GeneratedCharterOut", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getGeneratedCharterOut_Revenue(), ecorePackage.getEInt(), "revenue", null, 1, 1, GeneratedCharterOut.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(cooldownEClass, Cooldown.class, "Cooldown", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCooldown_Volume(), ecorePackage.getEInt(), "volume", null, 1, 1, Cooldown.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCooldown_Cost(), ecorePackage.getEInt(), "cost", null, 1, 1, Cooldown.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(fuelUsageEClass, FuelUsage.class, "FuelUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFuelUsage_Fuels(), this.getFuelQuantity(), null, "fuels", null, 0, -1, FuelUsage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getFuelUsage__GetFuelCost(), ecorePackage.getEInt(), "getFuelCost", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(fuelQuantityEClass, FuelQuantity.class, "FuelQuantity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFuelQuantity_Fuel(), this.getFuel(), "fuel", null, 1, 1, FuelQuantity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFuelQuantity_Cost(), ecorePackage.getEInt(), "cost", null, 1, 1, FuelQuantity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFuelQuantity_Amounts(), this.getFuelAmount(), null, "amounts", null, 0, -1, FuelQuantity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(fuelAmountEClass, FuelAmount.class, "FuelAmount", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFuelAmount_Unit(), this.getFuelUnit(), "unit", null, 1, 1, FuelAmount.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFuelAmount_Quantity(), ecorePackage.getEInt(), "quantity", null, 1, 1, FuelAmount.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFuelAmount_UnitPrice(), ecorePackage.getEDouble(), "unitPrice", null, 0, 1, FuelAmount.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(capacityViolationsHolderEClass, CapacityViolationsHolder.class, "CapacityViolationsHolder", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCapacityViolationsHolder_Violations(), this.getCapacityMapEntry(), null, "violations", null, 0, -1, CapacityViolationsHolder.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(capacityMapEntryEClass, Map.Entry.class, "CapacityMapEntry", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCapacityMapEntry_Key(), this.getCapacityViolationType(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCapacityMapEntry_Value(), ecorePackage.getELongObject(), "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(profitAndLossContainerEClass, ProfitAndLossContainer.class, "ProfitAndLossContainer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getProfitAndLossContainer_GroupProfitAndLoss(), this.getGroupProfitAndLoss(), null, "groupProfitAndLoss", null, 0, 1, ProfitAndLossContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProfitAndLossContainer_GeneralPNLDetails(), this.getGeneralPNLDetails(), null, "generalPNLDetails", null, 0, -1, ProfitAndLossContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(groupProfitAndLossEClass, GroupProfitAndLoss.class, "GroupProfitAndLoss", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getGroupProfitAndLoss_ProfitAndLoss(), ecorePackage.getELong(), "profitAndLoss", null, 0, 1, GroupProfitAndLoss.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGroupProfitAndLoss_ProfitAndLossPreTax(), ecorePackage.getELong(), "profitAndLossPreTax", null, 0, 1, GroupProfitAndLoss.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGroupProfitAndLoss_TaxValue(), ecorePackage.getELong(), "taxValue", null, 0, 1, GroupProfitAndLoss.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGroupProfitAndLoss_EntityProfitAndLosses(), this.getEntityProfitAndLoss(), null, "entityProfitAndLosses", null, 0, -1, GroupProfitAndLoss.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(entityProfitAndLossEClass, EntityProfitAndLoss.class, "EntityProfitAndLoss", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEntityProfitAndLoss_Entity(), theCommercialPackage.getBaseLegalEntity(), null, "entity", null, 0, 1, EntityProfitAndLoss.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntityProfitAndLoss_EntityBook(), theCommercialPackage.getBaseEntityBook(), null, "entityBook", null, 0, 1, EntityProfitAndLoss.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEntityProfitAndLoss_ProfitAndLoss(), ecorePackage.getELong(), "profitAndLoss", null, 0, 1, EntityProfitAndLoss.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEntityProfitAndLoss_ProfitAndLossPreTax(), ecorePackage.getELong(), "profitAndLossPreTax", null, 0, 1, EntityProfitAndLoss.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(entityPNLDetailsEClass, EntityPNLDetails.class, "EntityPNLDetails", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEntityPNLDetails_Entity(), theCommercialPackage.getBaseLegalEntity(), null, "entity", null, 0, 1, EntityPNLDetails.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntityPNLDetails_GeneralPNLDetails(), this.getGeneralPNLDetails(), null, "generalPNLDetails", null, 0, -1, EntityPNLDetails.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(slotPNLDetailsEClass, SlotPNLDetails.class, "SlotPNLDetails", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSlotPNLDetails_Slot(), theCargoPackage.getSlot(), null, "slot", null, 0, 1, SlotPNLDetails.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSlotPNLDetails_GeneralPNLDetails(), this.getGeneralPNLDetails(), null, "generalPNLDetails", null, 0, -1, SlotPNLDetails.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(generalPNLDetailsEClass, GeneralPNLDetails.class, "GeneralPNLDetails", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(basicSlotPNLDetailsEClass, BasicSlotPNLDetails.class, "BasicSlotPNLDetails", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getBasicSlotPNLDetails_ExtraShippingPNL(), ecorePackage.getEInt(), "extraShippingPNL", null, 0, 1, BasicSlotPNLDetails.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBasicSlotPNLDetails_AdditionalPNL(), ecorePackage.getEInt(), "additionalPNL", null, 0, 1, BasicSlotPNLDetails.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBasicSlotPNLDetails_CancellationFees(), ecorePackage.getEInt(), "cancellationFees", null, 0, 1, BasicSlotPNLDetails.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBasicSlotPNLDetails_HedgingValue(), ecorePackage.getEInt(), "hedgingValue", null, 0, 1, BasicSlotPNLDetails.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBasicSlotPNLDetails_MiscCostsValue(), ecorePackage.getEInt(), "miscCostsValue", null, 0, 1, BasicSlotPNLDetails.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBasicSlotPNLDetails_ExtraUpsidePNL(), ecorePackage.getEInt(), "extraUpsidePNL", null, 0, 1, BasicSlotPNLDetails.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eventGroupingEClass, EventGrouping.class, "EventGrouping", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEventGrouping_Events(), this.getEvent(), null, "events", null, 1, -1, EventGrouping.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(portVisitLatenessEClass, PortVisitLateness.class, "PortVisitLateness", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPortVisitLateness_Type(), this.getPortVisitLatenessType(), "type", null, 0, 1, PortVisitLateness.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPortVisitLateness_LatenessInHours(), ecorePackage.getEInt(), "latenessInHours", null, 0, 1, PortVisitLateness.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(exposureDetailEClass, ExposureDetail.class, "ExposureDetail", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getExposureDetail_Index(), thePricingPackage.getCommodityIndex(), null, "index", null, 0, 1, ExposureDetail.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExposureDetail_Date(), theDateTimePackage.getYearMonth(), "date", null, 0, 1, ExposureDetail.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExposureDetail_VolumeInMMBTU(), ecorePackage.getEDouble(), "volumeInMMBTU", null, 0, 1, ExposureDetail.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExposureDetail_VolumeInNativeUnits(), ecorePackage.getEDouble(), "volumeInNativeUnits", null, 0, 1, ExposureDetail.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExposureDetail_UnitPrice(), ecorePackage.getEDouble(), "unitPrice", null, 0, 1, ExposureDetail.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExposureDetail_NativeValue(), ecorePackage.getEDouble(), "nativeValue", null, 0, 1, ExposureDetail.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExposureDetail_VolumeUnit(), ecorePackage.getEString(), "volumeUnit", null, 0, 1, ExposureDetail.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExposureDetail_CurrencyUnit(), ecorePackage.getEString(), "currencyUnit", null, 0, 1, ExposureDetail.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(sequenceTypeEEnum, SequenceType.class, "SequenceType");
		addEEnumLiteral(sequenceTypeEEnum, SequenceType.VESSEL);
		addEEnumLiteral(sequenceTypeEEnum, SequenceType.SPOT_VESSEL);
		addEEnumLiteral(sequenceTypeEEnum, SequenceType.DES_PURCHASE);
		addEEnumLiteral(sequenceTypeEEnum, SequenceType.FOB_SALE);
		addEEnumLiteral(sequenceTypeEEnum, SequenceType.ROUND_TRIP);

		initEEnum(fuelEEnum, Fuel.class, "Fuel");
		addEEnumLiteral(fuelEEnum, Fuel.BASE_FUEL);
		addEEnumLiteral(fuelEEnum, Fuel.FBO);
		addEEnumLiteral(fuelEEnum, Fuel.NBO);
		addEEnumLiteral(fuelEEnum, Fuel.PILOT_LIGHT);

		initEEnum(fuelUnitEEnum, FuelUnit.class, "FuelUnit");
		addEEnumLiteral(fuelUnitEEnum, FuelUnit.M3);
		addEEnumLiteral(fuelUnitEEnum, FuelUnit.MT);
		addEEnumLiteral(fuelUnitEEnum, FuelUnit.MMBTU);

		initEEnum(capacityViolationTypeEEnum, CapacityViolationType.class, "CapacityViolationType");
		addEEnumLiteral(capacityViolationTypeEEnum, CapacityViolationType.MAX_LOAD);
		addEEnumLiteral(capacityViolationTypeEEnum, CapacityViolationType.MAX_DISCHARGE);
		addEEnumLiteral(capacityViolationTypeEEnum, CapacityViolationType.MIN_LOAD);
		addEEnumLiteral(capacityViolationTypeEEnum, CapacityViolationType.MIN_DISCHARGE);
		addEEnumLiteral(capacityViolationTypeEEnum, CapacityViolationType.MAX_HEEL);
		addEEnumLiteral(capacityViolationTypeEEnum, CapacityViolationType.FORCED_COOLDOWN);
		addEEnumLiteral(capacityViolationTypeEEnum, CapacityViolationType.VESSEL_CAPACITY);
		addEEnumLiteral(capacityViolationTypeEEnum, CapacityViolationType.LOST_HEEL);
		addEEnumLiteral(capacityViolationTypeEEnum, CapacityViolationType.MIN_HEEL);

		initEEnum(portVisitLatenessTypeEEnum, PortVisitLatenessType.class, "PortVisitLatenessType");
		addEEnumLiteral(portVisitLatenessTypeEEnum, PortVisitLatenessType.PROMPT);
		addEEnumLiteral(portVisitLatenessTypeEEnum, PortVisitLatenessType.MID_TERM);
		addEEnumLiteral(portVisitLatenessTypeEEnum, PortVisitLatenessType.BEYOND);

		initEEnum(slotAllocationTypeEEnum, SlotAllocationType.class, "SlotAllocationType");
		addEEnumLiteral(slotAllocationTypeEEnum, SlotAllocationType.PURCHASE);
		addEEnumLiteral(slotAllocationTypeEEnum, SlotAllocationType.SALE);

		// Initialize data types
		initEDataType(calendarEDataType, Calendar.class, "Calendar", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(iterableEDataType, Iterable.class, "Iterable", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(objectEDataType, Object.class, "Object", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //SchedulePackageImpl
