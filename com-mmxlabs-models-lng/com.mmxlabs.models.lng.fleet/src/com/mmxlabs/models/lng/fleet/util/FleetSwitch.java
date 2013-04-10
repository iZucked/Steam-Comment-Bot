/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.CharterOutEvent;
import com.mmxlabs.models.lng.fleet.DryDockEvent;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.HeelOptions;
import com.mmxlabs.models.lng.fleet.MaintenanceEvent;
import com.mmxlabs.models.lng.fleet.ScenarioFleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.fleet.VesselGroup;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.fleet.VesselTypeGroup;
import com.mmxlabs.models.lng.types.ABaseFuel;
import com.mmxlabs.models.lng.types.AVessel;
import com.mmxlabs.models.lng.types.AVesselClass;
import com.mmxlabs.models.lng.types.AVesselEvent;
import com.mmxlabs.models.lng.types.AVesselSet;
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
 * @see com.mmxlabs.models.lng.fleet.FleetPackage
 * @generated
 */
public class FleetSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static FleetPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FleetSwitch() {
		if (modelPackage == null) {
			modelPackage = FleetPackage.eINSTANCE;
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
			case FleetPackage.FLEET_MODEL: {
				FleetModel fleetModel = (FleetModel)theEObject;
				T result = caseFleetModel(fleetModel);
				if (result == null) result = caseUUIDObject(fleetModel);
				if (result == null) result = caseMMXObject(fleetModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.BASE_FUEL: {
				BaseFuel baseFuel = (BaseFuel)theEObject;
				T result = caseBaseFuel(baseFuel);
				if (result == null) result = caseABaseFuel(baseFuel);
				if (result == null) result = caseUUIDObject(baseFuel);
				if (result == null) result = caseNamedObject(baseFuel);
				if (result == null) result = caseMMXObject(baseFuel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.VESSEL: {
				Vessel vessel = (Vessel)theEObject;
				T result = caseVessel(vessel);
				if (result == null) result = caseAVessel(vessel);
				if (result == null) result = caseAVesselSet(vessel);
				if (result == null) result = caseUUIDObject(vessel);
				if (result == null) result = caseNamedObject(vessel);
				if (result == null) result = caseMMXObject(vessel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.VESSEL_CLASS: {
				VesselClass vesselClass = (VesselClass)theEObject;
				T result = caseVesselClass(vesselClass);
				if (result == null) result = caseAVesselClass(vesselClass);
				if (result == null) result = caseAVesselSet(vesselClass);
				if (result == null) result = caseUUIDObject(vesselClass);
				if (result == null) result = caseNamedObject(vesselClass);
				if (result == null) result = caseMMXObject(vesselClass);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.VESSEL_GROUP: {
				VesselGroup vesselGroup = (VesselGroup)theEObject;
				T result = caseVesselGroup(vesselGroup);
				if (result == null) result = caseAVesselSet(vesselGroup);
				if (result == null) result = caseUUIDObject(vesselGroup);
				if (result == null) result = caseNamedObject(vesselGroup);
				if (result == null) result = caseMMXObject(vesselGroup);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.VESSEL_TYPE_GROUP: {
				VesselTypeGroup vesselTypeGroup = (VesselTypeGroup)theEObject;
				T result = caseVesselTypeGroup(vesselTypeGroup);
				if (result == null) result = caseAVesselSet(vesselTypeGroup);
				if (result == null) result = caseUUIDObject(vesselTypeGroup);
				if (result == null) result = caseNamedObject(vesselTypeGroup);
				if (result == null) result = caseMMXObject(vesselTypeGroup);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.HEEL_OPTIONS: {
				HeelOptions heelOptions = (HeelOptions)theEObject;
				T result = caseHeelOptions(heelOptions);
				if (result == null) result = caseMMXObject(heelOptions);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.VESSEL_STATE_ATTRIBUTES: {
				VesselStateAttributes vesselStateAttributes = (VesselStateAttributes)theEObject;
				T result = caseVesselStateAttributes(vesselStateAttributes);
				if (result == null) result = caseMMXObject(vesselStateAttributes);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.FUEL_CONSUMPTION: {
				FuelConsumption fuelConsumption = (FuelConsumption)theEObject;
				T result = caseFuelConsumption(fuelConsumption);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.VESSEL_CLASS_ROUTE_PARAMETERS: {
				VesselClassRouteParameters vesselClassRouteParameters = (VesselClassRouteParameters)theEObject;
				T result = caseVesselClassRouteParameters(vesselClassRouteParameters);
				if (result == null) result = caseMMXObject(vesselClassRouteParameters);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.SCENARIO_FLEET_MODEL: {
				ScenarioFleetModel scenarioFleetModel = (ScenarioFleetModel)theEObject;
				T result = caseScenarioFleetModel(scenarioFleetModel);
				if (result == null) result = caseUUIDObject(scenarioFleetModel);
				if (result == null) result = caseMMXObject(scenarioFleetModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.VESSEL_AVAILABILITY: {
				VesselAvailability vesselAvailability = (VesselAvailability)theEObject;
				T result = caseVesselAvailability(vesselAvailability);
				if (result == null) result = caseMMXObject(vesselAvailability);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.VESSEL_EVENT: {
				VesselEvent vesselEvent = (VesselEvent)theEObject;
				T result = caseVesselEvent(vesselEvent);
				if (result == null) result = caseAVesselEvent(vesselEvent);
				if (result == null) result = caseITimezoneProvider(vesselEvent);
				if (result == null) result = caseUUIDObject(vesselEvent);
				if (result == null) result = caseNamedObject(vesselEvent);
				if (result == null) result = caseMMXObject(vesselEvent);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.MAINTENANCE_EVENT: {
				MaintenanceEvent maintenanceEvent = (MaintenanceEvent)theEObject;
				T result = caseMaintenanceEvent(maintenanceEvent);
				if (result == null) result = caseVesselEvent(maintenanceEvent);
				if (result == null) result = caseAVesselEvent(maintenanceEvent);
				if (result == null) result = caseITimezoneProvider(maintenanceEvent);
				if (result == null) result = caseUUIDObject(maintenanceEvent);
				if (result == null) result = caseNamedObject(maintenanceEvent);
				if (result == null) result = caseMMXObject(maintenanceEvent);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.DRY_DOCK_EVENT: {
				DryDockEvent dryDockEvent = (DryDockEvent)theEObject;
				T result = caseDryDockEvent(dryDockEvent);
				if (result == null) result = caseVesselEvent(dryDockEvent);
				if (result == null) result = caseAVesselEvent(dryDockEvent);
				if (result == null) result = caseITimezoneProvider(dryDockEvent);
				if (result == null) result = caseUUIDObject(dryDockEvent);
				if (result == null) result = caseNamedObject(dryDockEvent);
				if (result == null) result = caseMMXObject(dryDockEvent);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.CHARTER_OUT_EVENT: {
				CharterOutEvent charterOutEvent = (CharterOutEvent)theEObject;
				T result = caseCharterOutEvent(charterOutEvent);
				if (result == null) result = caseVesselEvent(charterOutEvent);
				if (result == null) result = caseAVesselEvent(charterOutEvent);
				if (result == null) result = caseITimezoneProvider(charterOutEvent);
				if (result == null) result = caseUUIDObject(charterOutEvent);
				if (result == null) result = caseNamedObject(charterOutEvent);
				if (result == null) result = caseMMXObject(charterOutEvent);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Vessel</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Vessel</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVessel(Vessel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Vessel Class</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Vessel Class</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVesselClass(VesselClass object) {
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
	public T caseFleetModel(FleetModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Base Fuel</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Base Fuel</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBaseFuel(BaseFuel object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>Heel Options</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Heel Options</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseHeelOptions(HeelOptions object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Vessel State Attributes</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Vessel State Attributes</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVesselStateAttributes(VesselStateAttributes object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>Fuel Consumption</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Fuel Consumption</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFuelConsumption(FuelConsumption object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>Vessel Class Route Parameters</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Vessel Class Route Parameters</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVesselClassRouteParameters(VesselClassRouteParameters object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Scenario Fleet Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Scenario Fleet Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseScenarioFleetModel(ScenarioFleetModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Vessel Group</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Vessel Group</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVesselGroup(VesselGroup object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Vessel Type Group</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Vessel Type Group</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVesselTypeGroup(VesselTypeGroup object) {
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
	public T caseAVesselSet(AVesselSet object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>AVessel</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>AVessel</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAVessel(AVessel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>AVessel Class</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>AVessel Class</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAVesselClass(AVesselClass object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>AVessel Event</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>AVessel Event</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAVesselEvent(AVesselEvent object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>ABase Fuel</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>ABase Fuel</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseABaseFuel(ABaseFuel object) {
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

} //FleetSwitch
