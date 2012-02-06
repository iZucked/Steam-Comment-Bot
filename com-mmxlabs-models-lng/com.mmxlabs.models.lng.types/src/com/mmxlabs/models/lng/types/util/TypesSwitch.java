/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.types.util;

import com.mmxlabs.models.lng.types.*;

import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

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
 * @see com.mmxlabs.models.lng.types.TypesPackage
 * @generated
 */
public class TypesSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static TypesPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TypesSwitch() {
		if (modelPackage == null) {
			modelPackage = TypesPackage.eINSTANCE;
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
			case TypesPackage.APORT: {
				APort aPort = (APort)theEObject;
				T result = caseAPort(aPort);
				if (result == null) result = caseAPortSet(aPort);
				if (result == null) result = caseUUIDObject(aPort);
				if (result == null) result = caseNamedObject(aPort);
				if (result == null) result = caseMMXObject(aPort);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case TypesPackage.APORT_SET: {
				APortSet aPortSet = (APortSet)theEObject;
				T result = caseAPortSet(aPortSet);
				if (result == null) result = caseUUIDObject(aPortSet);
				if (result == null) result = caseNamedObject(aPortSet);
				if (result == null) result = caseMMXObject(aPortSet);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case TypesPackage.AROUTE: {
				ARoute aRoute = (ARoute)theEObject;
				T result = caseARoute(aRoute);
				if (result == null) result = caseUUIDObject(aRoute);
				if (result == null) result = caseNamedObject(aRoute);
				if (result == null) result = caseMMXObject(aRoute);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case TypesPackage.AVESSEL: {
				AVessel aVessel = (AVessel)theEObject;
				T result = caseAVessel(aVessel);
				if (result == null) result = caseAVesselSet(aVessel);
				if (result == null) result = caseUUIDObject(aVessel);
				if (result == null) result = caseNamedObject(aVessel);
				if (result == null) result = caseMMXObject(aVessel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case TypesPackage.AVESSEL_CLASS: {
				AVesselClass aVesselClass = (AVesselClass)theEObject;
				T result = caseAVesselClass(aVesselClass);
				if (result == null) result = caseAVesselSet(aVesselClass);
				if (result == null) result = caseUUIDObject(aVesselClass);
				if (result == null) result = caseNamedObject(aVesselClass);
				if (result == null) result = caseMMXObject(aVesselClass);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case TypesPackage.AVESSEL_EVENT: {
				AVesselEvent aVesselEvent = (AVesselEvent)theEObject;
				T result = caseAVesselEvent(aVesselEvent);
				if (result == null) result = caseUUIDObject(aVesselEvent);
				if (result == null) result = caseNamedObject(aVesselEvent);
				if (result == null) result = caseMMXObject(aVesselEvent);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case TypesPackage.ACONTRACT: {
				AContract aContract = (AContract)theEObject;
				T result = caseAContract(aContract);
				if (result == null) result = caseUUIDObject(aContract);
				if (result == null) result = caseNamedObject(aContract);
				if (result == null) result = caseMMXObject(aContract);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case TypesPackage.ALEGAL_ENTITY: {
				ALegalEntity aLegalEntity = (ALegalEntity)theEObject;
				T result = caseALegalEntity(aLegalEntity);
				if (result == null) result = caseUUIDObject(aLegalEntity);
				if (result == null) result = caseNamedObject(aLegalEntity);
				if (result == null) result = caseMMXObject(aLegalEntity);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case TypesPackage.AINDEX: {
				AIndex aIndex = (AIndex)theEObject;
				T result = caseAIndex(aIndex);
				if (result == null) result = caseUUIDObject(aIndex);
				if (result == null) result = caseNamedObject(aIndex);
				if (result == null) result = caseMMXObject(aIndex);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case TypesPackage.ACARGO: {
				ACargo aCargo = (ACargo)theEObject;
				T result = caseACargo(aCargo);
				if (result == null) result = caseUUIDObject(aCargo);
				if (result == null) result = caseNamedObject(aCargo);
				if (result == null) result = caseMMXObject(aCargo);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case TypesPackage.ASLOT: {
				ASlot aSlot = (ASlot)theEObject;
				T result = caseASlot(aSlot);
				if (result == null) result = caseUUIDObject(aSlot);
				if (result == null) result = caseNamedObject(aSlot);
				if (result == null) result = caseMMXObject(aSlot);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case TypesPackage.TIME_WINDOW: {
				TimeWindow timeWindow = (TimeWindow)theEObject;
				T result = caseTimeWindow(timeWindow);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case TypesPackage.AVESSEL_SET: {
				AVesselSet aVesselSet = (AVesselSet)theEObject;
				T result = caseAVesselSet(aVesselSet);
				if (result == null) result = caseUUIDObject(aVesselSet);
				if (result == null) result = caseNamedObject(aVesselSet);
				if (result == null) result = caseMMXObject(aVesselSet);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>APort</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>APort</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAPort(APort object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>APort Set</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>APort Set</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAPortSet(APortSet object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>ARoute</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>ARoute</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseARoute(ARoute object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>AContract</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>AContract</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAContract(AContract object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>ALegal Entity</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>ALegal Entity</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseALegalEntity(ALegalEntity object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>AIndex</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>AIndex</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAIndex(AIndex object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>ACargo</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>ACargo</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseACargo(ACargo object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>ASlot</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>ASlot</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseASlot(ASlot object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Time Window</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Time Window</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTimeWindow(TimeWindow object) {
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

} //TypesSwitch
