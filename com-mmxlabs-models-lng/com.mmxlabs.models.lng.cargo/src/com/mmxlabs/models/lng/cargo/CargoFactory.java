/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.cargo.CargoPackage
 * @generated
 */
public interface CargoFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	CargoFactory eINSTANCE = com.mmxlabs.models.lng.cargo.impl.CargoFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Cargo</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Cargo</em>'.
	 * @generated
	 */
	Cargo createCargo();

	/**
	 * Returns a new object of class '<em>Load Slot</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Load Slot</em>'.
	 * @generated
	 */
	LoadSlot createLoadSlot();

	/**
	 * Returns a new object of class '<em>Discharge Slot</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Discharge Slot</em>'.
	 * @generated
	 */
	DischargeSlot createDischargeSlot();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	CargoModel createCargoModel();

	/**
	 * Returns a new object of class '<em>Spot Load Slot</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Spot Load Slot</em>'.
	 * @generated
	 */
	SpotLoadSlot createSpotLoadSlot();

	/**
	 * Returns a new object of class '<em>Spot Discharge Slot</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Spot Discharge Slot</em>'.
	 * @generated
	 */
	SpotDischargeSlot createSpotDischargeSlot();

	/**
	 * Returns a new object of class '<em>Group</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Group</em>'.
	 * @generated
	 */
	CargoGroup createCargoGroup();

	/**
	 * Returns a new object of class '<em>Vessel Availability</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Vessel Availability</em>'.
	 * @generated
	 */
	VesselAvailability createVesselAvailability();

	/**
	 * Returns a new object of class '<em>Maintenance Event</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Maintenance Event</em>'.
	 * @generated
	 */
	MaintenanceEvent createMaintenanceEvent();

	/**
	 * Returns a new object of class '<em>Dry Dock Event</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Dry Dock Event</em>'.
	 * @generated
	 */
	DryDockEvent createDryDockEvent();

	/**
	 * Returns a new object of class '<em>Charter Out Event</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Charter Out Event</em>'.
	 * @generated
	 */
	CharterOutEvent createCharterOutEvent();

	/**
	 * Returns a new object of class '<em>Vessel Type Group</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Vessel Type Group</em>'.
	 * @generated
	 */
	VesselTypeGroup createVesselTypeGroup();

	/**
	 * Returns a new object of class '<em>Inventory Event Row</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Inventory Event Row</em>'.
	 * @generated
	 */
	InventoryEventRow createInventoryEventRow();

	/**
	 * Returns a new object of class '<em>Inventory Capacity Row</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Inventory Capacity Row</em>'.
	 * @generated
	 */
	InventoryCapacityRow createInventoryCapacityRow();

	/**
	 * Returns a new object of class '<em>Inventory</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Inventory</em>'.
	 * @generated
	 */
	Inventory createInventory();

	/**
	 * Returns a new object of class '<em>Canal Booking Slot</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Canal Booking Slot</em>'.
	 * @generated
	 */
	CanalBookingSlot createCanalBookingSlot();

	/**
	 * Returns a new object of class '<em>Canal Bookings</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Canal Bookings</em>'.
	 * @generated
	 */
	CanalBookings createCanalBookings();

	/**
	 * Returns a new object of class '<em>Schedule Specification</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Schedule Specification</em>'.
	 * @generated
	 */
	ScheduleSpecification createScheduleSpecification();

	/**
	 * Returns a new object of class '<em>Non Shipped Cargo Specification</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Non Shipped Cargo Specification</em>'.
	 * @generated
	 */
	NonShippedCargoSpecification createNonShippedCargoSpecification();

	/**
	 * Returns a new object of class '<em>Vessel Schedule Specification</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Vessel Schedule Specification</em>'.
	 * @generated
	 */
	VesselScheduleSpecification createVesselScheduleSpecification();

	/**
	 * Returns a new object of class '<em>Schedule Specification Event</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Schedule Specification Event</em>'.
	 * @generated
	 */
	ScheduleSpecificationEvent createScheduleSpecificationEvent();

	/**
	 * Returns a new object of class '<em>Vessel Event Specification</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Vessel Event Specification</em>'.
	 * @generated
	 */
	VesselEventSpecification createVesselEventSpecification();

	/**
	 * Returns a new object of class '<em>Voyage Specification</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Voyage Specification</em>'.
	 * @generated
	 */
	VoyageSpecification createVoyageSpecification();

	/**
	 * Returns a new object of class '<em>Slot Specification</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Slot Specification</em>'.
	 * @generated
	 */
	SlotSpecification createSlotSpecification();

	/**
	 * Returns a new object of class '<em>Charter In Market Override</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Charter In Market Override</em>'.
	 * @generated
	 */
	CharterInMarketOverride createCharterInMarketOverride();

	/**
	 * Returns a new object of class '<em>Buy Paper Deal</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Buy Paper Deal</em>'.
	 * @generated
	 */
	BuyPaperDeal createBuyPaperDeal();

	/**
	 * Returns a new object of class '<em>Sell Paper Deal</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Sell Paper Deal</em>'.
	 * @generated
	 */
	SellPaperDeal createSellPaperDeal();

	/**
	 * Returns a new object of class '<em>Deal Set</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Deal Set</em>'.
	 * @generated
	 */
	DealSet createDealSet();

	/**
	 * Returns a new object of class '<em>Vessel Group Canal Parameters</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Vessel Group Canal Parameters</em>'.
	 * @generated
	 */
	VesselGroupCanalParameters createVesselGroupCanalParameters();

	/**
	 * Returns a new object of class '<em>Panama Seasonality Record</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Panama Seasonality Record</em>'.
	 * @generated
	 */
	PanamaSeasonalityRecord createPanamaSeasonalityRecord();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	CargoPackage getCargoPackage();

} //CargoFactory
