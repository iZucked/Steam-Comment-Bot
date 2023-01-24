/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.impl;

import com.mmxlabs.models.lng.cargo.*;
import com.mmxlabs.models.lng.commercial.Contract;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import com.mmxlabs.models.lng.cargo.BuyPaperDeal;
import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoGroup;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DealSet;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.InventoryCapacityRow;
import com.mmxlabs.models.lng.cargo.InventoryEventRow;
import com.mmxlabs.models.lng.cargo.InventoryFacilityType;
import com.mmxlabs.models.lng.cargo.InventoryFrequency;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.NonShippedCargoSpecification;
import com.mmxlabs.models.lng.cargo.PaperPricingType;
import com.mmxlabs.models.lng.cargo.ScheduleSpecification;
import com.mmxlabs.models.lng.cargo.ScheduleSpecificationEvent;
import com.mmxlabs.models.lng.cargo.SchedulingTimeWindow;
import com.mmxlabs.models.lng.cargo.SellPaperDeal;
import com.mmxlabs.models.lng.cargo.SlotSpecification;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEventSpecification;
import com.mmxlabs.models.lng.cargo.VesselScheduleSpecification;
import com.mmxlabs.models.lng.cargo.VesselType;
import com.mmxlabs.models.lng.cargo.VesselTypeGroup;
import com.mmxlabs.models.lng.cargo.VoyageSpecification;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class CargoFactoryImpl extends EFactoryImpl implements CargoFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static CargoFactory init() {
		try {
			CargoFactory theCargoFactory = (CargoFactory)EPackage.Registry.INSTANCE.getEFactory(CargoPackage.eNS_URI);
			if (theCargoFactory != null) {
				return theCargoFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new CargoFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CargoFactoryImpl() {
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
			case CargoPackage.CARGO_MODEL: return createCargoModel();
			case CargoPackage.CARGO: return createCargo();
			case CargoPackage.LOAD_SLOT: return createLoadSlot();
			case CargoPackage.DISCHARGE_SLOT: return createDischargeSlot();
			case CargoPackage.SPOT_LOAD_SLOT: return createSpotLoadSlot();
			case CargoPackage.SPOT_DISCHARGE_SLOT: return createSpotDischargeSlot();
			case CargoPackage.CARGO_GROUP: return createCargoGroup();
			case CargoPackage.VESSEL_CHARTER: return createVesselCharter();
			case CargoPackage.MAINTENANCE_EVENT: return createMaintenanceEvent();
			case CargoPackage.DRY_DOCK_EVENT: return createDryDockEvent();
			case CargoPackage.CHARTER_OUT_EVENT: return createCharterOutEvent();
			case CargoPackage.VESSEL_TYPE_GROUP: return createVesselTypeGroup();
			case CargoPackage.INVENTORY_EVENT_ROW: return createInventoryEventRow();
			case CargoPackage.INVENTORY_CAPACITY_ROW: return createInventoryCapacityRow();
			case CargoPackage.INVENTORY: return createInventory();
			case CargoPackage.CANAL_BOOKING_SLOT: return createCanalBookingSlot();
			case CargoPackage.CANAL_BOOKINGS: return createCanalBookings();
			case CargoPackage.SCHEDULE_SPECIFICATION: return createScheduleSpecification();
			case CargoPackage.NON_SHIPPED_CARGO_SPECIFICATION: return createNonShippedCargoSpecification();
			case CargoPackage.VESSEL_SCHEDULE_SPECIFICATION: return createVesselScheduleSpecification();
			case CargoPackage.SCHEDULE_SPECIFICATION_EVENT: return createScheduleSpecificationEvent();
			case CargoPackage.VESSEL_EVENT_SPECIFICATION: return createVesselEventSpecification();
			case CargoPackage.VOYAGE_SPECIFICATION: return createVoyageSpecification();
			case CargoPackage.SLOT_SPECIFICATION: return createSlotSpecification();
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE: return createCharterInMarketOverride();
			case CargoPackage.BUY_PAPER_DEAL: return createBuyPaperDeal();
			case CargoPackage.SELL_PAPER_DEAL: return createSellPaperDeal();
			case CargoPackage.DEAL_SET: return createDealSet();
			case CargoPackage.VESSEL_GROUP_CANAL_PARAMETERS: return createVesselGroupCanalParameters();
			case CargoPackage.PANAMA_SEASONALITY_RECORD: return createPanamaSeasonalityRecord();
			case CargoPackage.GROUPED_SLOTS_CONSTRAINT: return createGroupedSlotsConstraint();
			case CargoPackage.GROUPED_DISCHARGE_SLOTS_CONSTRAINT: return createGroupedDischargeSlotsConstraint();
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
			case CargoPackage.CARGO_TYPE:
				return createCargoTypeFromString(eDataType, initialValue);
			case CargoPackage.VESSEL_TYPE:
				return createVesselTypeFromString(eDataType, initialValue);
			case CargoPackage.INVENTORY_FACILITY_TYPE:
				return createInventoryFacilityTypeFromString(eDataType, initialValue);
			case CargoPackage.INVENTORY_FREQUENCY:
				return createInventoryFrequencyFromString(eDataType, initialValue);
			case CargoPackage.PAPER_PRICING_TYPE:
				return createPaperPricingTypeFromString(eDataType, initialValue);
			case CargoPackage.FUEL_CHOICE:
				return createFuelChoiceFromString(eDataType, initialValue);
			case CargoPackage.SCHEDULING_TIME_WINDOW:
				return createSchedulingTimeWindowFromString(eDataType, initialValue);
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
			case CargoPackage.CARGO_TYPE:
				return convertCargoTypeToString(eDataType, instanceValue);
			case CargoPackage.VESSEL_TYPE:
				return convertVesselTypeToString(eDataType, instanceValue);
			case CargoPackage.INVENTORY_FACILITY_TYPE:
				return convertInventoryFacilityTypeToString(eDataType, instanceValue);
			case CargoPackage.INVENTORY_FREQUENCY:
				return convertInventoryFrequencyToString(eDataType, instanceValue);
			case CargoPackage.PAPER_PRICING_TYPE:
				return convertPaperPricingTypeToString(eDataType, instanceValue);
			case CargoPackage.FUEL_CHOICE:
				return convertFuelChoiceToString(eDataType, instanceValue);
			case CargoPackage.SCHEDULING_TIME_WINDOW:
				return convertSchedulingTimeWindowToString(eDataType, instanceValue);
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
	public Cargo createCargo() {
		CargoImpl cargo = new CargoImpl();
		return cargo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LoadSlot createLoadSlot() {
		LoadSlotImpl loadSlot = new LoadSlotImpl();
		return loadSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DischargeSlot createDischargeSlot() {
		DischargeSlotImpl dischargeSlot = new DischargeSlotImpl();
		return dischargeSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CargoModel createCargoModel() {
		CargoModelImpl cargoModel = new CargoModelImpl();
		return cargoModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SpotLoadSlot createSpotLoadSlot() {
		SpotLoadSlotImpl spotLoadSlot = new SpotLoadSlotImpl();
		return spotLoadSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SpotDischargeSlot createSpotDischargeSlot() {
		SpotDischargeSlotImpl spotDischargeSlot = new SpotDischargeSlotImpl();
		return spotDischargeSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CargoGroup createCargoGroup() {
		CargoGroupImpl cargoGroup = new CargoGroupImpl();
		return cargoGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VesselCharter createVesselCharter() {
		VesselCharterImpl vesselCharter = new VesselCharterImpl();
		return vesselCharter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MaintenanceEvent createMaintenanceEvent() {
		MaintenanceEventImpl maintenanceEvent = new MaintenanceEventImpl();
		return maintenanceEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DryDockEvent createDryDockEvent() {
		DryDockEventImpl dryDockEvent = new DryDockEventImpl();
		return dryDockEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CharterOutEvent createCharterOutEvent() {
		CharterOutEventImpl charterOutEvent = new CharterOutEventImpl();
		return charterOutEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VesselTypeGroup createVesselTypeGroup() {
		VesselTypeGroupImpl vesselTypeGroup = new VesselTypeGroupImpl();
		return vesselTypeGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public InventoryEventRow createInventoryEventRow() {
		InventoryEventRowImpl inventoryEventRow = new InventoryEventRowImpl();
		return inventoryEventRow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public InventoryCapacityRow createInventoryCapacityRow() {
		InventoryCapacityRowImpl inventoryCapacityRow = new InventoryCapacityRowImpl();
		return inventoryCapacityRow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Inventory createInventory() {
		InventoryImpl inventory = new InventoryImpl();
		return inventory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CanalBookingSlot createCanalBookingSlot() {
		CanalBookingSlotImpl canalBookingSlot = new CanalBookingSlotImpl();
		return canalBookingSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CanalBookings createCanalBookings() {
		CanalBookingsImpl canalBookings = new CanalBookingsImpl();
		return canalBookings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ScheduleSpecification createScheduleSpecification() {
		ScheduleSpecificationImpl scheduleSpecification = new ScheduleSpecificationImpl();
		return scheduleSpecification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NonShippedCargoSpecification createNonShippedCargoSpecification() {
		NonShippedCargoSpecificationImpl nonShippedCargoSpecification = new NonShippedCargoSpecificationImpl();
		return nonShippedCargoSpecification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VesselScheduleSpecification createVesselScheduleSpecification() {
		VesselScheduleSpecificationImpl vesselScheduleSpecification = new VesselScheduleSpecificationImpl();
		return vesselScheduleSpecification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ScheduleSpecificationEvent createScheduleSpecificationEvent() {
		ScheduleSpecificationEventImpl scheduleSpecificationEvent = new ScheduleSpecificationEventImpl();
		return scheduleSpecificationEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VesselEventSpecification createVesselEventSpecification() {
		VesselEventSpecificationImpl vesselEventSpecification = new VesselEventSpecificationImpl();
		return vesselEventSpecification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VoyageSpecification createVoyageSpecification() {
		VoyageSpecificationImpl voyageSpecification = new VoyageSpecificationImpl();
		return voyageSpecification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SlotSpecification createSlotSpecification() {
		SlotSpecificationImpl slotSpecification = new SlotSpecificationImpl();
		return slotSpecification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CharterInMarketOverride createCharterInMarketOverride() {
		CharterInMarketOverrideImpl charterInMarketOverride = new CharterInMarketOverrideImpl();
		return charterInMarketOverride;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BuyPaperDeal createBuyPaperDeal() {
		BuyPaperDealImpl buyPaperDeal = new BuyPaperDealImpl();
		return buyPaperDeal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SellPaperDeal createSellPaperDeal() {
		SellPaperDealImpl sellPaperDeal = new SellPaperDealImpl();
		return sellPaperDeal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DealSet createDealSet() {
		DealSetImpl dealSet = new DealSetImpl();
		return dealSet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VesselGroupCanalParameters createVesselGroupCanalParameters() {
		VesselGroupCanalParametersImpl vesselGroupCanalParameters = new VesselGroupCanalParametersImpl();
		return vesselGroupCanalParameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PanamaSeasonalityRecord createPanamaSeasonalityRecord() {
		PanamaSeasonalityRecordImpl panamaSeasonalityRecord = new PanamaSeasonalityRecordImpl();
		return panamaSeasonalityRecord;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public <U extends Contract, T extends Slot<U>> GroupedSlotsConstraint<U, T> createGroupedSlotsConstraint() {
		GroupedSlotsConstraintImpl<U, T> groupedSlotsConstraint = new GroupedSlotsConstraintImpl<U, T>();
		return groupedSlotsConstraint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public GroupedDischargeSlotsConstraint createGroupedDischargeSlotsConstraint() {
		GroupedDischargeSlotsConstraintImpl groupedDischargeSlotsConstraint = new GroupedDischargeSlotsConstraintImpl();
		return groupedDischargeSlotsConstraint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CargoType createCargoTypeFromString(EDataType eDataType, String initialValue) {
		CargoType result = CargoType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertCargoTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselType createVesselTypeFromString(EDataType eDataType, String initialValue) {
		VesselType result = VesselType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertVesselTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InventoryFacilityType createInventoryFacilityTypeFromString(EDataType eDataType, String initialValue) {
		InventoryFacilityType result = InventoryFacilityType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertInventoryFacilityTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InventoryFrequency createInventoryFrequencyFromString(EDataType eDataType, String initialValue) {
		InventoryFrequency result = InventoryFrequency.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertInventoryFrequencyToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PaperPricingType createPaperPricingTypeFromString(EDataType eDataType, String initialValue) {
		PaperPricingType result = PaperPricingType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertPaperPricingTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FuelChoice createFuelChoiceFromString(EDataType eDataType, String initialValue) {
		FuelChoice result = FuelChoice.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertFuelChoiceToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SchedulingTimeWindow createSchedulingTimeWindowFromString(EDataType eDataType, String initialValue) {
		return (SchedulingTimeWindow)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertSchedulingTimeWindowToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CargoPackage getCargoPackage() {
		return (CargoPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static CargoPackage getPackage() {
		return CargoPackage.eINSTANCE;
	}

} //CargoFactoryImpl
