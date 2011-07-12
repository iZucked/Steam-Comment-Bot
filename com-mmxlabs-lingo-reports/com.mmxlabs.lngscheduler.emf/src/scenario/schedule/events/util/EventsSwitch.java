/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule.events.util;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import scenario.ScenarioObject;

import scenario.schedule.events.*;

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
 * @see scenario.schedule.events.EventsPackage
 * @generated
 */
public class EventsSwitch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static EventsPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EventsSwitch() {
		if (modelPackage == null) {
			modelPackage = EventsPackage.eINSTANCE;
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	public T doSwitch(EObject theEObject) {
		return doSwitch(theEObject.eClass(), theEObject);
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected T doSwitch(EClass theEClass, EObject theEObject) {
		if (theEClass.eContainer() == modelPackage) {
			return doSwitch(theEClass.getClassifierID(), theEObject);
		}
		else {
			List<EClass> eSuperTypes = theEClass.getESuperTypes();
			return
				eSuperTypes.isEmpty() ?
					defaultCase(theEObject) :
					doSwitch(eSuperTypes.get(0), theEObject);
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case EventsPackage.FUEL_MIXTURE: {
				FuelMixture fuelMixture = (FuelMixture)theEObject;
				T result = caseFuelMixture(fuelMixture);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case EventsPackage.FUEL_QUANTITY: {
				FuelQuantity fuelQuantity = (FuelQuantity)theEObject;
				T result = caseFuelQuantity(fuelQuantity);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case EventsPackage.SCHEDULED_EVENT: {
				ScheduledEvent scheduledEvent = (ScheduledEvent)theEObject;
				T result = caseScheduledEvent(scheduledEvent);
				if (result == null) result = caseScenarioObject(scheduledEvent);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case EventsPackage.JOURNEY: {
				Journey journey = (Journey)theEObject;
				T result = caseJourney(journey);
				if (result == null) result = caseScheduledEvent(journey);
				if (result == null) result = caseFuelMixture(journey);
				if (result == null) result = caseScenarioObject(journey);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case EventsPackage.PORT_VISIT: {
				PortVisit portVisit = (PortVisit)theEObject;
				T result = casePortVisit(portVisit);
				if (result == null) result = caseScheduledEvent(portVisit);
				if (result == null) result = caseScenarioObject(portVisit);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case EventsPackage.IDLE: {
				Idle idle = (Idle)theEObject;
				T result = caseIdle(idle);
				if (result == null) result = casePortVisit(idle);
				if (result == null) result = caseFuelMixture(idle);
				if (result == null) result = caseScheduledEvent(idle);
				if (result == null) result = caseScenarioObject(idle);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case EventsPackage.SLOT_VISIT: {
				SlotVisit slotVisit = (SlotVisit)theEObject;
				T result = caseSlotVisit(slotVisit);
				if (result == null) result = casePortVisit(slotVisit);
				if (result == null) result = caseScheduledEvent(slotVisit);
				if (result == null) result = caseScenarioObject(slotVisit);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case EventsPackage.VESSEL_EVENT_VISIT: {
				VesselEventVisit vesselEventVisit = (VesselEventVisit)theEObject;
				T result = caseVesselEventVisit(vesselEventVisit);
				if (result == null) result = casePortVisit(vesselEventVisit);
				if (result == null) result = caseScheduledEvent(vesselEventVisit);
				if (result == null) result = caseScenarioObject(vesselEventVisit);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case EventsPackage.CHARTER_OUT_VISIT: {
				CharterOutVisit charterOutVisit = (CharterOutVisit)theEObject;
				T result = caseCharterOutVisit(charterOutVisit);
				if (result == null) result = caseVesselEventVisit(charterOutVisit);
				if (result == null) result = casePortVisit(charterOutVisit);
				if (result == null) result = caseScheduledEvent(charterOutVisit);
				if (result == null) result = caseScenarioObject(charterOutVisit);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Fuel Mixture</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Fuel Mixture</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFuelMixture(FuelMixture object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>Scheduled Event</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Scheduled Event</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseScheduledEvent(ScheduledEvent object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>Charter Out Visit</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Charter Out Visit</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCharterOutVisit(CharterOutVisit object) {
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
	public T defaultCase(EObject object) {
		return null;
	}

} //EventsSwitch
