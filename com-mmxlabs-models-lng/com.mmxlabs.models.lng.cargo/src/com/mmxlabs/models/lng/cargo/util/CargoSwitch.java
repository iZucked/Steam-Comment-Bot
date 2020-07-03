/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import com.mmxlabs.models.lng.cargo.*;
import com.mmxlabs.models.lng.commercial.Contract;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoGroup;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.EndHeelOptions;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.VesselTypeGroup;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.ITimezoneProvider;
import com.mmxlabs.models.lng.types.ObjectSet;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
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
 * @see com.mmxlabs.models.lng.cargo.CargoPackage
 * @generated
 */
public class CargoSwitch<@Nullable T1> extends Switch<T1> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static CargoPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CargoSwitch() {
		if (modelPackage == null) {
			modelPackage = CargoPackage.eINSTANCE;
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
	protected T1 doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case CargoPackage.CARGO_MODEL: {
				CargoModel cargoModel = (CargoModel)theEObject;
				T1 result = caseCargoModel(cargoModel);
				if (result == null) result = caseUUIDObject(cargoModel);
				if (result == null) result = caseMMXObject(cargoModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.CARGO: {
				Cargo cargo = (Cargo)theEObject;
				T1 result = caseCargo(cargo);
				if (result == null) result = caseUUIDObject(cargo);
				if (result == null) result = caseAssignableElement(cargo);
				if (result == null) result = caseMMXObject(cargo);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.SLOT: {
				Slot<?> slot = (Slot<?>)theEObject;
				T1 result = caseSlot(slot);
				if (result == null) result = caseUUIDObject(slot);
				if (result == null) result = caseNamedObject(slot);
				if (result == null) result = caseITimezoneProvider(slot);
				if (result == null) result = caseMMXObject(slot);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.LOAD_SLOT: {
				LoadSlot loadSlot = (LoadSlot)theEObject;
				T1 result = caseLoadSlot(loadSlot);
				if (result == null) result = caseSlot(loadSlot);
				if (result == null) result = caseUUIDObject(loadSlot);
				if (result == null) result = caseNamedObject(loadSlot);
				if (result == null) result = caseITimezoneProvider(loadSlot);
				if (result == null) result = caseMMXObject(loadSlot);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.DISCHARGE_SLOT: {
				DischargeSlot dischargeSlot = (DischargeSlot)theEObject;
				T1 result = caseDischargeSlot(dischargeSlot);
				if (result == null) result = caseSlot(dischargeSlot);
				if (result == null) result = caseUUIDObject(dischargeSlot);
				if (result == null) result = caseNamedObject(dischargeSlot);
				if (result == null) result = caseITimezoneProvider(dischargeSlot);
				if (result == null) result = caseMMXObject(dischargeSlot);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.SPOT_SLOT: {
				SpotSlot spotSlot = (SpotSlot)theEObject;
				T1 result = caseSpotSlot(spotSlot);
				if (result == null) result = caseMMXObject(spotSlot);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.SPOT_LOAD_SLOT: {
				SpotLoadSlot spotLoadSlot = (SpotLoadSlot)theEObject;
				T1 result = caseSpotLoadSlot(spotLoadSlot);
				if (result == null) result = caseLoadSlot(spotLoadSlot);
				if (result == null) result = caseSpotSlot(spotLoadSlot);
				if (result == null) result = caseSlot(spotLoadSlot);
				if (result == null) result = caseUUIDObject(spotLoadSlot);
				if (result == null) result = caseNamedObject(spotLoadSlot);
				if (result == null) result = caseITimezoneProvider(spotLoadSlot);
				if (result == null) result = caseMMXObject(spotLoadSlot);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.SPOT_DISCHARGE_SLOT: {
				SpotDischargeSlot spotDischargeSlot = (SpotDischargeSlot)theEObject;
				T1 result = caseSpotDischargeSlot(spotDischargeSlot);
				if (result == null) result = caseDischargeSlot(spotDischargeSlot);
				if (result == null) result = caseSpotSlot(spotDischargeSlot);
				if (result == null) result = caseSlot(spotDischargeSlot);
				if (result == null) result = caseUUIDObject(spotDischargeSlot);
				if (result == null) result = caseNamedObject(spotDischargeSlot);
				if (result == null) result = caseITimezoneProvider(spotDischargeSlot);
				if (result == null) result = caseMMXObject(spotDischargeSlot);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.CARGO_GROUP: {
				CargoGroup cargoGroup = (CargoGroup)theEObject;
				T1 result = caseCargoGroup(cargoGroup);
				if (result == null) result = caseNamedObject(cargoGroup);
				if (result == null) result = caseMMXObject(cargoGroup);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.VESSEL_AVAILABILITY: {
				VesselAvailability vesselAvailability = (VesselAvailability)theEObject;
				T1 result = caseVesselAvailability(vesselAvailability);
				if (result == null) result = caseUUIDObject(vesselAvailability);
				if (result == null) result = caseVesselAssignmentType(vesselAvailability);
				if (result == null) result = caseMMXObject(vesselAvailability);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.VESSEL_EVENT: {
				VesselEvent vesselEvent = (VesselEvent)theEObject;
				T1 result = caseVesselEvent(vesselEvent);
				if (result == null) result = caseUUIDObject(vesselEvent);
				if (result == null) result = caseNamedObject(vesselEvent);
				if (result == null) result = caseITimezoneProvider(vesselEvent);
				if (result == null) result = caseAssignableElement(vesselEvent);
				if (result == null) result = caseMMXObject(vesselEvent);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.MAINTENANCE_EVENT: {
				MaintenanceEvent maintenanceEvent = (MaintenanceEvent)theEObject;
				T1 result = caseMaintenanceEvent(maintenanceEvent);
				if (result == null) result = caseVesselEvent(maintenanceEvent);
				if (result == null) result = caseUUIDObject(maintenanceEvent);
				if (result == null) result = caseNamedObject(maintenanceEvent);
				if (result == null) result = caseITimezoneProvider(maintenanceEvent);
				if (result == null) result = caseAssignableElement(maintenanceEvent);
				if (result == null) result = caseMMXObject(maintenanceEvent);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.DRY_DOCK_EVENT: {
				DryDockEvent dryDockEvent = (DryDockEvent)theEObject;
				T1 result = caseDryDockEvent(dryDockEvent);
				if (result == null) result = caseVesselEvent(dryDockEvent);
				if (result == null) result = caseUUIDObject(dryDockEvent);
				if (result == null) result = caseNamedObject(dryDockEvent);
				if (result == null) result = caseITimezoneProvider(dryDockEvent);
				if (result == null) result = caseAssignableElement(dryDockEvent);
				if (result == null) result = caseMMXObject(dryDockEvent);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.CHARTER_OUT_EVENT: {
				CharterOutEvent charterOutEvent = (CharterOutEvent)theEObject;
				T1 result = caseCharterOutEvent(charterOutEvent);
				if (result == null) result = caseVesselEvent(charterOutEvent);
				if (result == null) result = caseUUIDObject(charterOutEvent);
				if (result == null) result = caseNamedObject(charterOutEvent);
				if (result == null) result = caseITimezoneProvider(charterOutEvent);
				if (result == null) result = caseAssignableElement(charterOutEvent);
				if (result == null) result = caseMMXObject(charterOutEvent);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.ASSIGNABLE_ELEMENT: {
				AssignableElement assignableElement = (AssignableElement)theEObject;
				T1 result = caseAssignableElement(assignableElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.VESSEL_TYPE_GROUP: {
				VesselTypeGroup vesselTypeGroup = (VesselTypeGroup)theEObject;
				T1 result = caseVesselTypeGroup(vesselTypeGroup);
				if (result == null) result = caseAVesselSet(vesselTypeGroup);
				if (result == null) result = caseObjectSet(vesselTypeGroup);
				if (result == null) result = caseUUIDObject(vesselTypeGroup);
				if (result == null) result = caseNamedObject(vesselTypeGroup);
				if (result == null) result = caseMMXObject(vesselTypeGroup);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.END_HEEL_OPTIONS: {
				EndHeelOptions endHeelOptions = (EndHeelOptions)theEObject;
				T1 result = caseEndHeelOptions(endHeelOptions);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.START_HEEL_OPTIONS: {
				StartHeelOptions startHeelOptions = (StartHeelOptions)theEObject;
				T1 result = caseStartHeelOptions(startHeelOptions);
				if (result == null) result = caseMMXObject(startHeelOptions);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.INVENTORY_EVENT_ROW: {
				InventoryEventRow inventoryEventRow = (InventoryEventRow)theEObject;
				T1 result = caseInventoryEventRow(inventoryEventRow);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.INVENTORY_CAPACITY_ROW: {
				InventoryCapacityRow inventoryCapacityRow = (InventoryCapacityRow)theEObject;
				T1 result = caseInventoryCapacityRow(inventoryCapacityRow);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.INVENTORY: {
				Inventory inventory = (Inventory)theEObject;
				T1 result = caseInventory(inventory);
				if (result == null) result = caseNamedObject(inventory);
				if (result == null) result = caseMMXObject(inventory);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.CANAL_BOOKING_SLOT: {
				CanalBookingSlot canalBookingSlot = (CanalBookingSlot)theEObject;
				T1 result = caseCanalBookingSlot(canalBookingSlot);
				if (result == null) result = caseMMXObject(canalBookingSlot);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.CANAL_BOOKINGS: {
				CanalBookings canalBookings = (CanalBookings)theEObject;
				T1 result = caseCanalBookings(canalBookings);
				if (result == null) result = caseMMXObject(canalBookings);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.SCHEDULE_SPECIFICATION: {
				ScheduleSpecification scheduleSpecification = (ScheduleSpecification)theEObject;
				T1 result = caseScheduleSpecification(scheduleSpecification);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.NON_SHIPPED_CARGO_SPECIFICATION: {
				NonShippedCargoSpecification nonShippedCargoSpecification = (NonShippedCargoSpecification)theEObject;
				T1 result = caseNonShippedCargoSpecification(nonShippedCargoSpecification);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.VESSEL_SCHEDULE_SPECIFICATION: {
				VesselScheduleSpecification vesselScheduleSpecification = (VesselScheduleSpecification)theEObject;
				T1 result = caseVesselScheduleSpecification(vesselScheduleSpecification);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.SCHEDULE_SPECIFICATION_EVENT: {
				ScheduleSpecificationEvent scheduleSpecificationEvent = (ScheduleSpecificationEvent)theEObject;
				T1 result = caseScheduleSpecificationEvent(scheduleSpecificationEvent);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.VESSEL_EVENT_SPECIFICATION: {
				VesselEventSpecification vesselEventSpecification = (VesselEventSpecification)theEObject;
				T1 result = caseVesselEventSpecification(vesselEventSpecification);
				if (result == null) result = caseScheduleSpecificationEvent(vesselEventSpecification);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.VOYAGE_SPECIFICATION: {
				VoyageSpecification voyageSpecification = (VoyageSpecification)theEObject;
				T1 result = caseVoyageSpecification(voyageSpecification);
				if (result == null) result = caseScheduleSpecificationEvent(voyageSpecification);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.SLOT_SPECIFICATION: {
				SlotSpecification slotSpecification = (SlotSpecification)theEObject;
				T1 result = caseSlotSpecification(slotSpecification);
				if (result == null) result = caseScheduleSpecificationEvent(slotSpecification);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE: {
				CharterInMarketOverride charterInMarketOverride = (CharterInMarketOverride)theEObject;
				T1 result = caseCharterInMarketOverride(charterInMarketOverride);
				if (result == null) result = caseMMXObject(charterInMarketOverride);
				if (result == null) result = caseVesselAssignmentType(charterInMarketOverride);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.PAPER_DEAL: {
				PaperDeal paperDeal = (PaperDeal)theEObject;
				T1 result = casePaperDeal(paperDeal);
				if (result == null) result = caseNamedObject(paperDeal);
				if (result == null) result = caseMMXObject(paperDeal);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.BUY_PAPER_DEAL: {
				BuyPaperDeal buyPaperDeal = (BuyPaperDeal)theEObject;
				T1 result = caseBuyPaperDeal(buyPaperDeal);
				if (result == null) result = casePaperDeal(buyPaperDeal);
				if (result == null) result = caseNamedObject(buyPaperDeal);
				if (result == null) result = caseMMXObject(buyPaperDeal);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.SELL_PAPER_DEAL: {
				SellPaperDeal sellPaperDeal = (SellPaperDeal)theEObject;
				T1 result = caseSellPaperDeal(sellPaperDeal);
				if (result == null) result = casePaperDeal(sellPaperDeal);
				if (result == null) result = caseNamedObject(sellPaperDeal);
				if (result == null) result = caseMMXObject(sellPaperDeal);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.DEAL_SET: {
				DealSet dealSet = (DealSet)theEObject;
				T1 result = caseDealSet(dealSet);
				if (result == null) result = caseNamedObject(dealSet);
				if (result == null) result = caseMMXObject(dealSet);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Cargo</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Cargo</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseCargo(Cargo object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Slot</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Slot</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <T extends Contract> T1 caseSlot(Slot<T> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Load Slot</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Load Slot</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseLoadSlot(LoadSlot object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Discharge Slot</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Discharge Slot</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseDischargeSlot(DischargeSlot object) {
		return null;
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
	public T1 caseCargoModel(CargoModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Spot Slot</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Spot Slot</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseSpotSlot(SpotSlot object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Spot Load Slot</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Spot Load Slot</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseSpotLoadSlot(SpotLoadSlot object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Spot Discharge Slot</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Spot Discharge Slot</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseSpotDischargeSlot(SpotDischargeSlot object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Group</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Group</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseCargoGroup(CargoGroup object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Vessel Availability</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Vessel Availability</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseVesselAvailability(VesselAvailability object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Vessel Event</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Vessel Event</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseVesselEvent(VesselEvent object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Maintenance Event</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Maintenance Event</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseMaintenanceEvent(MaintenanceEvent object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Dry Dock Event</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Dry Dock Event</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseDryDockEvent(DryDockEvent object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Charter Out Event</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Charter Out Event</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseCharterOutEvent(CharterOutEvent object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Assignable Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Assignable Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseAssignableElement(AssignableElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Vessel Type Group</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Vessel Type Group</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseVesselTypeGroup(VesselTypeGroup object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>End Heel Options</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>End Heel Options</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEndHeelOptions(EndHeelOptions object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Start Heel Options</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Start Heel Options</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseStartHeelOptions(StartHeelOptions object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Inventory Event Row</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Inventory Event Row</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseInventoryEventRow(InventoryEventRow object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Inventory Capacity Row</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Inventory Capacity Row</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseInventoryCapacityRow(InventoryCapacityRow object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Inventory</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Inventory</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseInventory(Inventory object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Canal Booking Slot</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Canal Booking Slot</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseCanalBookingSlot(CanalBookingSlot object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Canal Bookings</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Canal Bookings</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseCanalBookings(CanalBookings object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Schedule Specification</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Schedule Specification</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseScheduleSpecification(ScheduleSpecification object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Non Shipped Cargo Specification</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Non Shipped Cargo Specification</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseNonShippedCargoSpecification(NonShippedCargoSpecification object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Vessel Schedule Specification</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Vessel Schedule Specification</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseVesselScheduleSpecification(VesselScheduleSpecification object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Schedule Specification Event</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Schedule Specification Event</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseScheduleSpecificationEvent(ScheduleSpecificationEvent object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Vessel Event Specification</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Vessel Event Specification</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseVesselEventSpecification(VesselEventSpecification object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Voyage Specification</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Voyage Specification</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseVoyageSpecification(VoyageSpecification object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Slot Specification</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Slot Specification</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseSlotSpecification(SlotSpecification object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Charter In Market Override</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Charter In Market Override</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseCharterInMarketOverride(CharterInMarketOverride object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Paper Deal</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Paper Deal</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 casePaperDeal(PaperDeal object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Buy Paper Deal</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Buy Paper Deal</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseBuyPaperDeal(BuyPaperDeal object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Sell Paper Deal</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Sell Paper Deal</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseSellPaperDeal(SellPaperDeal object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Deal Set</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Deal Set</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseDealSet(DealSet object) {
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
	public T1 caseMMXObject(MMXObject object) {
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
	public T1 caseUUIDObject(UUIDObject object) {
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
	public T1 caseNamedObject(NamedObject object) {
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
	public T1 caseITimezoneProvider(ITimezoneProvider object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Vessel Assignment Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Vessel Assignment Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseVesselAssignmentType(VesselAssignmentType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Object Set</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Object Set</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <T extends ObjectSet<T, U>, U> T1 caseObjectSet(ObjectSet<T, U> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>AVessel Set</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>AVessel Set</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <U> T1 caseAVesselSet(AVesselSet<U> object) {
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
	public T1 defaultCase(EObject object) {
		return null;
	}

} //CargoSwitch
