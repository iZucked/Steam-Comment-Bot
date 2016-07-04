/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.actuals.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.actuals.ActualsModel;
import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.lng.actuals.CargoActuals;
import com.mmxlabs.models.lng.actuals.DischargeActuals;
import com.mmxlabs.models.lng.actuals.LoadActuals;
import com.mmxlabs.models.lng.actuals.ReturnActuals;
import com.mmxlabs.models.lng.actuals.SlotActuals;
import com.mmxlabs.models.lng.types.ITimezoneProvider;

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
 * @see com.mmxlabs.models.lng.actuals.ActualsPackage
 * @generated
 */
public class ActualsSwitch<@Nullable T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static ActualsPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ActualsSwitch() {
		if (modelPackage == null) {
			modelPackage = ActualsPackage.eINSTANCE;
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
			case ActualsPackage.ACTUALS_MODEL: {
				ActualsModel actualsModel = (ActualsModel)theEObject;
				T result = caseActualsModel(actualsModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ActualsPackage.SLOT_ACTUALS: {
				SlotActuals slotActuals = (SlotActuals)theEObject;
				T result = caseSlotActuals(slotActuals);
				if (result == null) result = caseITimezoneProvider(slotActuals);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ActualsPackage.CARGO_ACTUALS: {
				CargoActuals cargoActuals = (CargoActuals)theEObject;
				T result = caseCargoActuals(cargoActuals);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ActualsPackage.LOAD_ACTUALS: {
				LoadActuals loadActuals = (LoadActuals)theEObject;
				T result = caseLoadActuals(loadActuals);
				if (result == null) result = caseSlotActuals(loadActuals);
				if (result == null) result = caseITimezoneProvider(loadActuals);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ActualsPackage.DISCHARGE_ACTUALS: {
				DischargeActuals dischargeActuals = (DischargeActuals)theEObject;
				T result = caseDischargeActuals(dischargeActuals);
				if (result == null) result = caseSlotActuals(dischargeActuals);
				if (result == null) result = caseITimezoneProvider(dischargeActuals);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ActualsPackage.RETURN_ACTUALS: {
				ReturnActuals returnActuals = (ReturnActuals)theEObject;
				T result = caseReturnActuals(returnActuals);
				if (result == null) result = caseITimezoneProvider(returnActuals);
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
	public T caseActualsModel(ActualsModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Slot Actuals</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Slot Actuals</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSlotActuals(SlotActuals object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Cargo Actuals</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Cargo Actuals</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCargoActuals(CargoActuals object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Load Actuals</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Load Actuals</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLoadActuals(LoadActuals object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Discharge Actuals</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Discharge Actuals</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDischargeActuals(DischargeActuals object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Return Actuals</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Return Actuals</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseReturnActuals(ReturnActuals object) {
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

} //ActualsSwitch
