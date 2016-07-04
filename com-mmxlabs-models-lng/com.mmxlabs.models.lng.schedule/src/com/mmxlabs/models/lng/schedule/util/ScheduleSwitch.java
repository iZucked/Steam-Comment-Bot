/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.util;

import com.mmxlabs.models.lng.schedule.*;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;
import org.eclipse.jdt.annotation.Nullable;

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
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
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
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotPNLDetails;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.types.ITimezoneProvider;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage
 * @generated
 */
public class ScheduleSwitch<@Nullable T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static SchedulePackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScheduleSwitch() {
		if (modelPackage == null) {
			modelPackage = SchedulePackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case SchedulePackage.SCHEDULE_MODEL: {
				ScheduleModel scheduleModel = (ScheduleModel)theEObject;
				T result = caseScheduleModel(scheduleModel);
				if (result == null) result = caseUUIDObject(scheduleModel);
				if (result == null) result = caseMMXObject(scheduleModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchedulePackage.SCHEDULE: {
				Schedule schedule = (Schedule)theEObject;
				T result = caseSchedule(schedule);
				if (result == null) result = caseMMXObject(schedule);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchedulePackage.FITNESS: {
				Fitness fitness = (Fitness)theEObject;
				T result = caseFitness(fitness);
				if (result == null) result = caseNamedObject(fitness);
				if (result == null) result = caseMMXObject(fitness);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchedulePackage.CARGO_ALLOCATION: {
				CargoAllocation cargoAllocation = (CargoAllocation)theEObject;
				T result = caseCargoAllocation(cargoAllocation);
				if (result == null) result = caseProfitAndLossContainer(cargoAllocation);
				if (result == null) result = caseEventGrouping(cargoAllocation);
				if (result == null) result = caseMMXObject(cargoAllocation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchedulePackage.MARKET_ALLOCATION: {
				MarketAllocation marketAllocation = (MarketAllocation)theEObject;
				T result = caseMarketAllocation(marketAllocation);
				if (result == null) result = caseProfitAndLossContainer(marketAllocation);
				if (result == null) result = caseMMXObject(marketAllocation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchedulePackage.OPEN_SLOT_ALLOCATION: {
				OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation)theEObject;
				T result = caseOpenSlotAllocation(openSlotAllocation);
				if (result == null) result = caseProfitAndLossContainer(openSlotAllocation);
				if (result == null) result = caseMMXObject(openSlotAllocation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchedulePackage.SLOT_ALLOCATION: {
				SlotAllocation slotAllocation = (SlotAllocation)theEObject;
				T result = caseSlotAllocation(slotAllocation);
				if (result == null) result = caseMMXObject(slotAllocation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchedulePackage.SEQUENCE: {
				Sequence sequence = (Sequence)theEObject;
				T result = caseSequence(sequence);
				if (result == null) result = caseMMXObject(sequence);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchedulePackage.EVENT: {
				Event event = (Event)theEObject;
				T result = caseEvent(event);
				if (result == null) result = caseMMXObject(event);
				if (result == null) result = caseITimezoneProvider(event);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchedulePackage.START_EVENT: {
				StartEvent startEvent = (StartEvent)theEObject;
				T result = caseStartEvent(startEvent);
				if (result == null) result = caseFuelUsage(startEvent);
				if (result == null) result = casePortVisit(startEvent);
				if (result == null) result = caseProfitAndLossContainer(startEvent);
				if (result == null) result = caseEventGrouping(startEvent);
				if (result == null) result = caseEvent(startEvent);
				if (result == null) result = caseITimezoneProvider(startEvent);
				if (result == null) result = caseCapacityViolationsHolder(startEvent);
				if (result == null) result = caseMMXObject(startEvent);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchedulePackage.END_EVENT: {
				EndEvent endEvent = (EndEvent)theEObject;
				T result = caseEndEvent(endEvent);
				if (result == null) result = caseFuelUsage(endEvent);
				if (result == null) result = casePortVisit(endEvent);
				if (result == null) result = caseProfitAndLossContainer(endEvent);
				if (result == null) result = caseEventGrouping(endEvent);
				if (result == null) result = caseEvent(endEvent);
				if (result == null) result = caseITimezoneProvider(endEvent);
				if (result == null) result = caseCapacityViolationsHolder(endEvent);
				if (result == null) result = caseMMXObject(endEvent);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchedulePackage.JOURNEY: {
				Journey journey = (Journey)theEObject;
				T result = caseJourney(journey);
				if (result == null) result = caseEvent(journey);
				if (result == null) result = caseFuelUsage(journey);
				if (result == null) result = caseMMXObject(journey);
				if (result == null) result = caseITimezoneProvider(journey);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchedulePackage.IDLE: {
				Idle idle = (Idle)theEObject;
				T result = caseIdle(idle);
				if (result == null) result = caseEvent(idle);
				if (result == null) result = caseFuelUsage(idle);
				if (result == null) result = caseMMXObject(idle);
				if (result == null) result = caseITimezoneProvider(idle);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchedulePackage.PORT_VISIT: {
				PortVisit portVisit = (PortVisit)theEObject;
				T result = casePortVisit(portVisit);
				if (result == null) result = caseEvent(portVisit);
				if (result == null) result = caseCapacityViolationsHolder(portVisit);
				if (result == null) result = caseMMXObject(portVisit);
				if (result == null) result = caseITimezoneProvider(portVisit);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchedulePackage.SLOT_VISIT: {
				SlotVisit slotVisit = (SlotVisit)theEObject;
				T result = caseSlotVisit(slotVisit);
				if (result == null) result = caseFuelUsage(slotVisit);
				if (result == null) result = casePortVisit(slotVisit);
				if (result == null) result = caseEvent(slotVisit);
				if (result == null) result = caseITimezoneProvider(slotVisit);
				if (result == null) result = caseCapacityViolationsHolder(slotVisit);
				if (result == null) result = caseMMXObject(slotVisit);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchedulePackage.VESSEL_EVENT_VISIT: {
				VesselEventVisit vesselEventVisit = (VesselEventVisit)theEObject;
				T result = caseVesselEventVisit(vesselEventVisit);
				if (result == null) result = casePortVisit(vesselEventVisit);
				if (result == null) result = caseProfitAndLossContainer(vesselEventVisit);
				if (result == null) result = caseEventGrouping(vesselEventVisit);
				if (result == null) result = caseEvent(vesselEventVisit);
				if (result == null) result = caseITimezoneProvider(vesselEventVisit);
				if (result == null) result = caseCapacityViolationsHolder(vesselEventVisit);
				if (result == null) result = caseMMXObject(vesselEventVisit);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchedulePackage.GENERATED_CHARTER_OUT: {
				GeneratedCharterOut generatedCharterOut = (GeneratedCharterOut)theEObject;
				T result = caseGeneratedCharterOut(generatedCharterOut);
				if (result == null) result = casePortVisit(generatedCharterOut);
				if (result == null) result = caseProfitAndLossContainer(generatedCharterOut);
				if (result == null) result = caseEventGrouping(generatedCharterOut);
				if (result == null) result = caseEvent(generatedCharterOut);
				if (result == null) result = caseCapacityViolationsHolder(generatedCharterOut);
				if (result == null) result = caseMMXObject(generatedCharterOut);
				if (result == null) result = caseITimezoneProvider(generatedCharterOut);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchedulePackage.COOLDOWN: {
				Cooldown cooldown = (Cooldown)theEObject;
				T result = caseCooldown(cooldown);
				if (result == null) result = caseEvent(cooldown);
				if (result == null) result = caseFuelUsage(cooldown);
				if (result == null) result = caseMMXObject(cooldown);
				if (result == null) result = caseITimezoneProvider(cooldown);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchedulePackage.FUEL_USAGE: {
				FuelUsage fuelUsage = (FuelUsage)theEObject;
				T result = caseFuelUsage(fuelUsage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchedulePackage.FUEL_QUANTITY: {
				FuelQuantity fuelQuantity = (FuelQuantity)theEObject;
				T result = caseFuelQuantity(fuelQuantity);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchedulePackage.FUEL_AMOUNT: {
				FuelAmount fuelAmount = (FuelAmount)theEObject;
				T result = caseFuelAmount(fuelAmount);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchedulePackage.CAPACITY_VIOLATIONS_HOLDER: {
				CapacityViolationsHolder capacityViolationsHolder = (CapacityViolationsHolder)theEObject;
				T result = caseCapacityViolationsHolder(capacityViolationsHolder);
				if (result == null) result = caseMMXObject(capacityViolationsHolder);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchedulePackage.CAPACITY_MAP_ENTRY: {
				@SuppressWarnings("unchecked") Map.Entry<CapacityViolationType, Long> capacityMapEntry = (Map.Entry<CapacityViolationType, Long>)theEObject;
				T result = caseCapacityMapEntry(capacityMapEntry);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchedulePackage.PROFIT_AND_LOSS_CONTAINER: {
				ProfitAndLossContainer profitAndLossContainer = (ProfitAndLossContainer)theEObject;
				T result = caseProfitAndLossContainer(profitAndLossContainer);
				if (result == null) result = caseMMXObject(profitAndLossContainer);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchedulePackage.GROUP_PROFIT_AND_LOSS: {
				GroupProfitAndLoss groupProfitAndLoss = (GroupProfitAndLoss)theEObject;
				T result = caseGroupProfitAndLoss(groupProfitAndLoss);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchedulePackage.ENTITY_PROFIT_AND_LOSS: {
				EntityProfitAndLoss entityProfitAndLoss = (EntityProfitAndLoss)theEObject;
				T result = caseEntityProfitAndLoss(entityProfitAndLoss);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchedulePackage.ENTITY_PNL_DETAILS: {
				EntityPNLDetails entityPNLDetails = (EntityPNLDetails)theEObject;
				T result = caseEntityPNLDetails(entityPNLDetails);
				if (result == null) result = caseGeneralPNLDetails(entityPNLDetails);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchedulePackage.SLOT_PNL_DETAILS: {
				SlotPNLDetails slotPNLDetails = (SlotPNLDetails)theEObject;
				T result = caseSlotPNLDetails(slotPNLDetails);
				if (result == null) result = caseGeneralPNLDetails(slotPNLDetails);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchedulePackage.GENERAL_PNL_DETAILS: {
				GeneralPNLDetails generalPNLDetails = (GeneralPNLDetails)theEObject;
				T result = caseGeneralPNLDetails(generalPNLDetails);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchedulePackage.BASIC_SLOT_PNL_DETAILS: {
				BasicSlotPNLDetails basicSlotPNLDetails = (BasicSlotPNLDetails)theEObject;
				T result = caseBasicSlotPNLDetails(basicSlotPNLDetails);
				if (result == null) result = caseGeneralPNLDetails(basicSlotPNLDetails);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchedulePackage.EVENT_GROUPING: {
				EventGrouping eventGrouping = (EventGrouping)theEObject;
				T result = caseEventGrouping(eventGrouping);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchedulePackage.PORT_VISIT_LATENESS: {
				PortVisitLateness portVisitLateness = (PortVisitLateness)theEObject;
				T result = casePortVisitLateness(portVisitLateness);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseScheduleModel(ScheduleModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Schedule</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Schedule</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSchedule(Schedule object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Sequence</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Sequence</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSequence(Sequence object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Event</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Event</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEvent(Event object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Slot Visit</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Slot Visit</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSlotVisit(SlotVisit object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Vessel Event Visit</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Vessel Event Visit</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVesselEventVisit(VesselEventVisit object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Journey</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Journey</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseJourney(Journey object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Idle</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Idle</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIdle(Idle object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Generated Charter Out</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Generated Charter Out</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseGeneratedCharterOut(GeneratedCharterOut object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Fuel Usage</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Fuel Usage</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFuelUsage(FuelUsage object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Fuel Quantity</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Fuel Quantity</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFuelQuantity(FuelQuantity object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Cooldown</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Cooldown</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCooldown(Cooldown object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Cargo Allocation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Cargo Allocation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCargoAllocation(CargoAllocation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Market Allocation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Market Allocation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMarketAllocation(MarketAllocation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Open Slot Allocation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Open Slot Allocation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOpenSlotAllocation(OpenSlotAllocation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Slot Allocation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Slot Allocation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSlotAllocation(SlotAllocation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Fuel Amount</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Fuel Amount</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFuelAmount(FuelAmount object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Fitness</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Fitness</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFitness(Fitness object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Port Visit</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Port Visit</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePortVisit(PortVisit object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Start Event</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Start Event</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStartEvent(StartEvent object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>End Event</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>End Event</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEndEvent(EndEvent object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Capacity Violations Holder</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Capacity Violations Holder</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCapacityViolationsHolder(CapacityViolationsHolder object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Capacity Map Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Capacity Map Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCapacityMapEntry(Map.Entry<CapacityViolationType, Long> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Profit And Loss Container</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Profit And Loss Container</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseProfitAndLossContainer(ProfitAndLossContainer object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Group Profit And Loss</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Group Profit And Loss</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseGroupProfitAndLoss(GroupProfitAndLoss object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Entity Profit And Loss</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Entity Profit And Loss</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEntityProfitAndLoss(EntityProfitAndLoss object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Entity PNL Details</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Entity PNL Details</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEntityPNLDetails(EntityPNLDetails object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Slot PNL Details</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Slot PNL Details</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSlotPNLDetails(SlotPNLDetails object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>General PNL Details</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>General PNL Details</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseGeneralPNLDetails(GeneralPNLDetails object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Basic Slot PNL Details</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Basic Slot PNL Details</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBasicSlotPNLDetails(BasicSlotPNLDetails object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Event Grouping</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Event Grouping</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEventGrouping(EventGrouping object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Port Visit Lateness</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Port Visit Lateness</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePortVisitLateness(PortVisitLateness object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>MMX Object</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>MMX Object</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMMXObject(MMXObject object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UUID Object</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UUID Object</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUUIDObject(UUIDObject object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Named Object</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Named Object</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNamedObject(NamedObject object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>ITimezone Provider</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>ITimezone Provider</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseITimezoneProvider(ITimezoneProvider object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} //ScheduleSwitch
