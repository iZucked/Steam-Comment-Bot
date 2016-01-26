/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage
 * @generated
 */
public interface ScheduleFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ScheduleFactory eINSTANCE = com.mmxlabs.models.lng.schedule.impl.ScheduleFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	ScheduleModel createScheduleModel();

	/**
	 * Returns a new object of class '<em>Schedule</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Schedule</em>'.
	 * @generated
	 */
	Schedule createSchedule();

	/**
	 * Returns a new object of class '<em>Sequence</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Sequence</em>'.
	 * @generated
	 */
	Sequence createSequence();

	/**
	 * Returns a new object of class '<em>Event</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Event</em>'.
	 * @generated
	 */
	Event createEvent();

	/**
	 * Returns a new object of class '<em>Slot Visit</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Slot Visit</em>'.
	 * @generated
	 */
	SlotVisit createSlotVisit();

	/**
	 * Returns a new object of class '<em>Vessel Event Visit</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Vessel Event Visit</em>'.
	 * @generated
	 */
	VesselEventVisit createVesselEventVisit();

	/**
	 * Returns a new object of class '<em>Journey</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Journey</em>'.
	 * @generated
	 */
	Journey createJourney();

	/**
	 * Returns a new object of class '<em>Idle</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Idle</em>'.
	 * @generated
	 */
	Idle createIdle();

	/**
	 * Returns a new object of class '<em>Generated Charter Out</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Generated Charter Out</em>'.
	 * @generated
	 */
	GeneratedCharterOut createGeneratedCharterOut();

	/**
	 * Returns a new object of class '<em>Fuel Usage</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Fuel Usage</em>'.
	 * @generated
	 */
	FuelUsage createFuelUsage();

	/**
	 * Returns a new object of class '<em>Fuel Quantity</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Fuel Quantity</em>'.
	 * @generated
	 */
	FuelQuantity createFuelQuantity();

	/**
	 * Returns a new object of class '<em>Cooldown</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Cooldown</em>'.
	 * @generated
	 */
	Cooldown createCooldown();

	/**
	 * Returns a new object of class '<em>Cargo Allocation</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Cargo Allocation</em>'.
	 * @generated
	 */
	CargoAllocation createCargoAllocation();

	/**
	 * Returns a new object of class '<em>Market Allocation</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Market Allocation</em>'.
	 * @generated
	 */
	MarketAllocation createMarketAllocation();

	/**
	 * Returns a new object of class '<em>Open Slot Allocation</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Open Slot Allocation</em>'.
	 * @generated
	 */
	OpenSlotAllocation createOpenSlotAllocation();

	/**
	 * Returns a new object of class '<em>Slot Allocation</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Slot Allocation</em>'.
	 * @generated
	 */
	SlotAllocation createSlotAllocation();

	/**
	 * Returns a new object of class '<em>Fuel Amount</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Fuel Amount</em>'.
	 * @generated
	 */
	FuelAmount createFuelAmount();

	/**
	 * Returns a new object of class '<em>Fitness</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Fitness</em>'.
	 * @generated
	 */
	Fitness createFitness();

	/**
	 * Returns a new object of class '<em>Port Visit</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Port Visit</em>'.
	 * @generated
	 */
	PortVisit createPortVisit();

	/**
	 * Returns a new object of class '<em>Start Event</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Start Event</em>'.
	 * @generated
	 */
	StartEvent createStartEvent();

	/**
	 * Returns a new object of class '<em>End Event</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>End Event</em>'.
	 * @generated
	 */
	EndEvent createEndEvent();

	/**
	 * Returns a new object of class '<em>Capacity Violations Holder</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Capacity Violations Holder</em>'.
	 * @generated
	 */
	CapacityViolationsHolder createCapacityViolationsHolder();

	/**
	 * Returns a new object of class '<em>Profit And Loss Container</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Profit And Loss Container</em>'.
	 * @generated
	 */
	ProfitAndLossContainer createProfitAndLossContainer();

	/**
	 * Returns a new object of class '<em>Group Profit And Loss</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Group Profit And Loss</em>'.
	 * @generated
	 */
	GroupProfitAndLoss createGroupProfitAndLoss();

	/**
	 * Returns a new object of class '<em>Entity Profit And Loss</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Entity Profit And Loss</em>'.
	 * @generated
	 */
	EntityProfitAndLoss createEntityProfitAndLoss();

	/**
	 * Returns a new object of class '<em>Entity PNL Details</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Entity PNL Details</em>'.
	 * @generated
	 */
	EntityPNLDetails createEntityPNLDetails();

	/**
	 * Returns a new object of class '<em>Slot PNL Details</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Slot PNL Details</em>'.
	 * @generated
	 */
	SlotPNLDetails createSlotPNLDetails();

	/**
	 * Returns a new object of class '<em>Basic Slot PNL Details</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Basic Slot PNL Details</em>'.
	 * @generated
	 */
	BasicSlotPNLDetails createBasicSlotPNLDetails();

	/**
	 * Returns a new object of class '<em>Event Grouping</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Event Grouping</em>'.
	 * @generated
	 */
	EventGrouping createEventGrouping();

	/**
	 * Returns a new object of class '<em>Port Visit Lateness</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Port Visit Lateness</em>'.
	 * @generated
	 */
	PortVisitLateness createPortVisitLateness();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	SchedulePackage getSchedulePackage();

} //ScheduleFactory
