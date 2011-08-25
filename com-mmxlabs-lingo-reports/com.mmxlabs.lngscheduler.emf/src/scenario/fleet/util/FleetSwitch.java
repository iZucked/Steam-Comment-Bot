/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.fleet.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;

import scenario.AnnotatedObject;
import scenario.NamedObject;
import scenario.ScenarioObject;
import scenario.fleet.*;
import scenario.fleet.CharterOut;
import scenario.fleet.Drydock;
import scenario.fleet.FleetModel;
import scenario.fleet.FleetPackage;
import scenario.fleet.FuelConsumptionLine;
import scenario.fleet.PortAndTime;
import scenario.fleet.PortExclusion;
import scenario.fleet.Vessel;
import scenario.fleet.VesselClass;
import scenario.fleet.VesselClassCost;
import scenario.fleet.VesselEvent;
import scenario.fleet.VesselFuel;
import scenario.fleet.VesselStateAttributes;

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
 * @see scenario.fleet.FleetPackage
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
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.VESSEL: {
				Vessel vessel = (Vessel)theEObject;
				T result = caseVessel(vessel);
				if (result == null) result = caseNamedObject(vessel);
				if (result == null) result = caseAnnotatedObject(vessel);
				if (result == null) result = caseScenarioObject(vessel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.VESSEL_CLASS: {
				VesselClass vesselClass = (VesselClass)theEObject;
				T result = caseVesselClass(vesselClass);
				if (result == null) result = caseNamedObject(vesselClass);
				if (result == null) result = caseAnnotatedObject(vesselClass);
				if (result == null) result = caseScenarioObject(vesselClass);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.FUEL_CONSUMPTION_LINE: {
				FuelConsumptionLine fuelConsumptionLine = (FuelConsumptionLine)theEObject;
				T result = caseFuelConsumptionLine(fuelConsumptionLine);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.VESSEL_STATE_ATTRIBUTES: {
				VesselStateAttributes vesselStateAttributes = (VesselStateAttributes)theEObject;
				T result = caseVesselStateAttributes(vesselStateAttributes);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.PORT_AND_TIME: {
				PortAndTime portAndTime = (PortAndTime)theEObject;
				T result = casePortAndTime(portAndTime);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.VESSEL_EVENT: {
				VesselEvent vesselEvent = (VesselEvent)theEObject;
				T result = caseVesselEvent(vesselEvent);
				if (result == null) result = caseAnnotatedObject(vesselEvent);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.CHARTER_OUT: {
				CharterOut charterOut = (CharterOut)theEObject;
				T result = caseCharterOut(charterOut);
				if (result == null) result = caseVesselEvent(charterOut);
				if (result == null) result = caseHeelOptions(charterOut);
				if (result == null) result = caseAnnotatedObject(charterOut);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.DRYDOCK: {
				Drydock drydock = (Drydock)theEObject;
				T result = caseDrydock(drydock);
				if (result == null) result = caseVesselEvent(drydock);
				if (result == null) result = caseAnnotatedObject(drydock);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.VESSEL_FUEL: {
				VesselFuel vesselFuel = (VesselFuel)theEObject;
				T result = caseVesselFuel(vesselFuel);
				if (result == null) result = caseNamedObject(vesselFuel);
				if (result == null) result = caseScenarioObject(vesselFuel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.PORT_EXCLUSION: {
				PortExclusion portExclusion = (PortExclusion)theEObject;
				T result = casePortExclusion(portExclusion);
				if (result == null) result = caseScenarioObject(portExclusion);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.VESSEL_CLASS_COST: {
				VesselClassCost vesselClassCost = (VesselClassCost)theEObject;
				T result = caseVesselClassCost(vesselClassCost);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.PORT_TIME_AND_HEEL: {
				PortTimeAndHeel portTimeAndHeel = (PortTimeAndHeel)theEObject;
				T result = casePortTimeAndHeel(portTimeAndHeel);
				if (result == null) result = casePortAndTime(portTimeAndHeel);
				if (result == null) result = caseHeelOptions(portTimeAndHeel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case FleetPackage.HEEL_OPTIONS: {
				HeelOptions heelOptions = (HeelOptions)theEObject;
				T result = caseHeelOptions(heelOptions);
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
	public T caseFleetModel(FleetModel object) {
		return null;
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
	 * Returns the result of interpreting the object as an instance of '<em>Fuel Consumption Line</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Fuel Consumption Line</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFuelConsumptionLine(FuelConsumptionLine object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>Port And Time</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Port And Time</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePortAndTime(PortAndTime object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>Charter Out</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Charter Out</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCharterOut(CharterOut object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Drydock</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Drydock</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDrydock(Drydock object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Vessel Fuel</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Vessel Fuel</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVesselFuel(VesselFuel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Port Exclusion</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Port Exclusion</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePortExclusion(PortExclusion object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Vessel Class Cost</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Vessel Class Cost</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVesselClassCost(VesselClassCost object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Port Time And Heel</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Port Time And Heel</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePortTimeAndHeel(PortTimeAndHeel object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>Object</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Object</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseScenarioObject(ScenarioObject object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>Annotated Object</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Annotated Object</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAnnotatedObject(AnnotatedObject object) {
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
