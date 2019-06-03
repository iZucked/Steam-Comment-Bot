/**
 */
package com.mmxlabs.models.lng.nominations.util;

import com.mmxlabs.models.lng.nominations.*;

import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.eclipse.jdt.annotation.Nullable;

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
 * @see com.mmxlabs.models.lng.nominations.NominationsPackage
 * @generated
 */
public class NominationsSwitch<@Nullable T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static NominationsPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NominationsSwitch() {
		if (modelPackage == null) {
			modelPackage = NominationsPackage.eINSTANCE;
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
			case NominationsPackage.NOMINATIONS_MODEL: {
				NominationsModel nominationsModel = (NominationsModel)theEObject;
				T result = caseNominationsModel(nominationsModel);
				if (result == null) result = caseUUIDObject(nominationsModel);
				if (result == null) result = caseMMXObject(nominationsModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC: {
				AbstractNominationSpec abstractNominationSpec = (AbstractNominationSpec)theEObject;
				T result = caseAbstractNominationSpec(abstractNominationSpec);
				if (result == null) result = caseUUIDObject(abstractNominationSpec);
				if (result == null) result = caseMMXObject(abstractNominationSpec);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case NominationsPackage.SLOT_NOMINATION_SPEC: {
				SlotNominationSpec slotNominationSpec = (SlotNominationSpec)theEObject;
				T result = caseSlotNominationSpec(slotNominationSpec);
				if (result == null) result = caseAbstractNominationSpec(slotNominationSpec);
				if (result == null) result = caseUUIDObject(slotNominationSpec);
				if (result == null) result = caseMMXObject(slotNominationSpec);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case NominationsPackage.SLOT_NOMINATION: {
				SlotNomination slotNomination = (SlotNomination)theEObject;
				T result = caseSlotNomination(slotNomination);
				if (result == null) result = caseAbstractNomination(slotNomination);
				if (result == null) result = caseAbstractNominationSpec(slotNomination);
				if (result == null) result = caseUUIDObject(slotNomination);
				if (result == null) result = caseMMXObject(slotNomination);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case NominationsPackage.CONTRACT_NOMINATION: {
				ContractNomination contractNomination = (ContractNomination)theEObject;
				T result = caseContractNomination(contractNomination);
				if (result == null) result = caseAbstractNomination(contractNomination);
				if (result == null) result = caseAbstractNominationSpec(contractNomination);
				if (result == null) result = caseUUIDObject(contractNomination);
				if (result == null) result = caseMMXObject(contractNomination);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case NominationsPackage.CONTRACT_NOMINATION_SPEC: {
				ContractNominationSpec contractNominationSpec = (ContractNominationSpec)theEObject;
				T result = caseContractNominationSpec(contractNominationSpec);
				if (result == null) result = caseAbstractNominationSpec(contractNominationSpec);
				if (result == null) result = caseUUIDObject(contractNominationSpec);
				if (result == null) result = caseMMXObject(contractNominationSpec);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case NominationsPackage.ABSTRACT_NOMINATION: {
				AbstractNomination abstractNomination = (AbstractNomination)theEObject;
				T result = caseAbstractNomination(abstractNomination);
				if (result == null) result = caseAbstractNominationSpec(abstractNomination);
				if (result == null) result = caseUUIDObject(abstractNomination);
				if (result == null) result = caseMMXObject(abstractNomination);
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
	public T caseNominationsModel(NominationsModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Abstract Nomination Spec</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Abstract Nomination Spec</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAbstractNominationSpec(AbstractNominationSpec object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Slot Nomination Spec</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Slot Nomination Spec</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSlotNominationSpec(SlotNominationSpec object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Slot Nomination</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Slot Nomination</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSlotNomination(SlotNomination object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Contract Nomination</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Contract Nomination</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseContractNomination(ContractNomination object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Contract Nomination Spec</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Contract Nomination Spec</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseContractNominationSpec(ContractNominationSpec object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Abstract Nomination</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Abstract Nomination</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAbstractNomination(AbstractNomination object) {
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

} //NominationsSwitch