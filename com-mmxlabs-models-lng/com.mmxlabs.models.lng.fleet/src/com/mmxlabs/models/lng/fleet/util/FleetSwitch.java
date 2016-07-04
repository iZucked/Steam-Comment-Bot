/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.util;

import com.mmxlabs.models.lng.fleet.*;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.HeelOptions;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.fleet.VesselGroup;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.ObjectSet;
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
public class FleetSwitch<@Nullable T1> extends Switch<T1> {
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
			case FleetPackage.FLEET_MODEL: {
				FleetModel fleetModel = (FleetModel)theEObject;
				T1 result = caseFleetModel(fleetModel);
				if (result == null) result = caseUUIDObject(fleetModel);
				if (result == null) result = caseMMXObject(fleetModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.BASE_FUEL: {
				BaseFuel baseFuel = (BaseFuel)theEObject;
				T1 result = caseBaseFuel(baseFuel);
				if (result == null) result = caseUUIDObject(baseFuel);
				if (result == null) result = caseNamedObject(baseFuel);
				if (result == null) result = caseMMXObject(baseFuel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.VESSEL: {
				Vessel vessel = (Vessel)theEObject;
				T1 result = caseVessel(vessel);
				if (result == null) result = caseAVesselSet(vessel);
				if (result == null) result = caseObjectSet(vessel);
				if (result == null) result = caseUUIDObject(vessel);
				if (result == null) result = caseNamedObject(vessel);
				if (result == null) result = caseMMXObject(vessel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.VESSEL_CLASS: {
				VesselClass vesselClass = (VesselClass)theEObject;
				T1 result = caseVesselClass(vesselClass);
				if (result == null) result = caseAVesselSet(vesselClass);
				if (result == null) result = caseObjectSet(vesselClass);
				if (result == null) result = caseUUIDObject(vesselClass);
				if (result == null) result = caseNamedObject(vesselClass);
				if (result == null) result = caseMMXObject(vesselClass);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.VESSEL_GROUP: {
				VesselGroup vesselGroup = (VesselGroup)theEObject;
				T1 result = caseVesselGroup(vesselGroup);
				if (result == null) result = caseAVesselSet(vesselGroup);
				if (result == null) result = caseObjectSet(vesselGroup);
				if (result == null) result = caseUUIDObject(vesselGroup);
				if (result == null) result = caseNamedObject(vesselGroup);
				if (result == null) result = caseMMXObject(vesselGroup);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.HEEL_OPTIONS: {
				HeelOptions heelOptions = (HeelOptions)theEObject;
				T1 result = caseHeelOptions(heelOptions);
				if (result == null) result = caseMMXObject(heelOptions);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.VESSEL_STATE_ATTRIBUTES: {
				VesselStateAttributes vesselStateAttributes = (VesselStateAttributes)theEObject;
				T1 result = caseVesselStateAttributes(vesselStateAttributes);
				if (result == null) result = caseMMXObject(vesselStateAttributes);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.FUEL_CONSUMPTION: {
				FuelConsumption fuelConsumption = (FuelConsumption)theEObject;
				T1 result = caseFuelConsumption(fuelConsumption);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.VESSEL_CLASS_ROUTE_PARAMETERS: {
				VesselClassRouteParameters vesselClassRouteParameters = (VesselClassRouteParameters)theEObject;
				T1 result = caseVesselClassRouteParameters(vesselClassRouteParameters);
				if (result == null) result = caseMMXObject(vesselClassRouteParameters);
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
	public T1 caseVessel(Vessel object) {
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
	public T1 caseVesselClass(VesselClass object) {
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
	public T1 caseFleetModel(FleetModel object) {
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
	public T1 caseBaseFuel(BaseFuel object) {
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
	public T1 caseHeelOptions(HeelOptions object) {
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
	public T1 caseVesselStateAttributes(VesselStateAttributes object) {
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
	public T1 caseFuelConsumption(FuelConsumption object) {
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
	public T1 caseVesselClassRouteParameters(VesselClassRouteParameters object) {
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
	public T1 caseVesselGroup(VesselGroup object) {
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

} //FleetSwitch
