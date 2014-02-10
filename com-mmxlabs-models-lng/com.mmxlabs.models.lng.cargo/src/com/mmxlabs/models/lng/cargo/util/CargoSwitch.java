/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import com.mmxlabs.models.lng.cargo.*;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoGroup;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
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
 * @see com.mmxlabs.models.lng.cargo.CargoPackage
 * @generated
 */
public class CargoSwitch<T> extends Switch<T> {
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
	 * @parameter ePackage the package in question.
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
			case CargoPackage.CARGO: {
				Cargo cargo = (Cargo)theEObject;
				T result = caseCargo(cargo);
				if (result == null) result = caseUUIDObject(cargo);
				if (result == null) result = caseNamedObject(cargo);
				if (result == null) result = caseAssignableElement(cargo);
				if (result == null) result = caseMMXObject(cargo);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.SLOT: {
				Slot slot = (Slot)theEObject;
				T result = caseSlot(slot);
				if (result == null) result = caseUUIDObject(slot);
				if (result == null) result = caseNamedObject(slot);
				if (result == null) result = caseITimezoneProvider(slot);
				if (result == null) result = caseAssignableElement(slot);
				if (result == null) result = caseMMXObject(slot);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.LOAD_SLOT: {
				LoadSlot loadSlot = (LoadSlot)theEObject;
				T result = caseLoadSlot(loadSlot);
				if (result == null) result = caseSlot(loadSlot);
				if (result == null) result = caseUUIDObject(loadSlot);
				if (result == null) result = caseNamedObject(loadSlot);
				if (result == null) result = caseITimezoneProvider(loadSlot);
				if (result == null) result = caseAssignableElement(loadSlot);
				if (result == null) result = caseMMXObject(loadSlot);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.DISCHARGE_SLOT: {
				DischargeSlot dischargeSlot = (DischargeSlot)theEObject;
				T result = caseDischargeSlot(dischargeSlot);
				if (result == null) result = caseSlot(dischargeSlot);
				if (result == null) result = caseUUIDObject(dischargeSlot);
				if (result == null) result = caseNamedObject(dischargeSlot);
				if (result == null) result = caseITimezoneProvider(dischargeSlot);
				if (result == null) result = caseAssignableElement(dischargeSlot);
				if (result == null) result = caseMMXObject(dischargeSlot);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.CARGO_MODEL: {
				CargoModel cargoModel = (CargoModel)theEObject;
				T result = caseCargoModel(cargoModel);
				if (result == null) result = caseUUIDObject(cargoModel);
				if (result == null) result = caseMMXObject(cargoModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.SPOT_SLOT: {
				SpotSlot spotSlot = (SpotSlot)theEObject;
				T result = caseSpotSlot(spotSlot);
				if (result == null) result = caseMMXObject(spotSlot);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.SPOT_LOAD_SLOT: {
				SpotLoadSlot spotLoadSlot = (SpotLoadSlot)theEObject;
				T result = caseSpotLoadSlot(spotLoadSlot);
				if (result == null) result = caseLoadSlot(spotLoadSlot);
				if (result == null) result = caseSpotSlot(spotLoadSlot);
				if (result == null) result = caseSlot(spotLoadSlot);
				if (result == null) result = caseUUIDObject(spotLoadSlot);
				if (result == null) result = caseNamedObject(spotLoadSlot);
				if (result == null) result = caseITimezoneProvider(spotLoadSlot);
				if (result == null) result = caseAssignableElement(spotLoadSlot);
				if (result == null) result = caseMMXObject(spotLoadSlot);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.SPOT_DISCHARGE_SLOT: {
				SpotDischargeSlot spotDischargeSlot = (SpotDischargeSlot)theEObject;
				T result = caseSpotDischargeSlot(spotDischargeSlot);
				if (result == null) result = caseDischargeSlot(spotDischargeSlot);
				if (result == null) result = caseSpotSlot(spotDischargeSlot);
				if (result == null) result = caseSlot(spotDischargeSlot);
				if (result == null) result = caseUUIDObject(spotDischargeSlot);
				if (result == null) result = caseNamedObject(spotDischargeSlot);
				if (result == null) result = caseITimezoneProvider(spotDischargeSlot);
				if (result == null) result = caseAssignableElement(spotDischargeSlot);
				if (result == null) result = caseMMXObject(spotDischargeSlot);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.CARGO_GROUP: {
				CargoGroup cargoGroup = (CargoGroup)theEObject;
				T result = caseCargoGroup(cargoGroup);
				if (result == null) result = caseNamedObject(cargoGroup);
				if (result == null) result = caseMMXObject(cargoGroup);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.VESSEL_AVAILABILITY: {
				VesselAvailability vesselAvailability = (VesselAvailability)theEObject;
				T result = caseVesselAvailability(vesselAvailability);
				if (result == null) result = caseUUIDObject(vesselAvailability);
				if (result == null) result = caseMMXObject(vesselAvailability);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CargoPackage.VESSEL_EVENT: {
				VesselEvent vesselEvent = (VesselEvent)theEObject;
				T result = caseVesselEvent(vesselEvent);
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
				T result = caseMaintenanceEvent(maintenanceEvent);
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
				T result = caseDryDockEvent(dryDockEvent);
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
				T result = caseCharterOutEvent(charterOutEvent);
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
				T result = caseAssignableElement(assignableElement);
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
	public T caseCargo(Cargo object) {
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
	public T caseSlot(Slot object) {
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
	public T caseLoadSlot(LoadSlot object) {
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
	public T caseDischargeSlot(DischargeSlot object) {
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
	public T caseCargoModel(CargoModel object) {
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
	public T caseSpotSlot(SpotSlot object) {
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
	public T caseSpotLoadSlot(SpotLoadSlot object) {
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
	public T caseSpotDischargeSlot(SpotDischargeSlot object) {
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
	public T caseCargoGroup(CargoGroup object) {
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
	public T caseVesselAvailability(VesselAvailability object) {
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
	public T caseVesselEvent(VesselEvent object) {
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
	public T caseMaintenanceEvent(MaintenanceEvent object) {
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
	public T caseDryDockEvent(DryDockEvent object) {
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
	public T caseCharterOutEvent(CharterOutEvent object) {
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
	public T caseAssignableElement(AssignableElement object) {
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

} //CargoSwitch
