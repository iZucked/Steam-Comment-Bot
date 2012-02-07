/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.optimiser.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;

import scenario.NamedObject;
import scenario.ScenarioObject;
import scenario.optimiser.Constraint;
import scenario.optimiser.Discount;
import scenario.optimiser.DiscountCurve;
import scenario.optimiser.Objective;
import scenario.optimiser.Optimisation;
import scenario.optimiser.OptimisationSettings;
import scenario.optimiser.OptimiserPackage;

/**
 * <!-- begin-user-doc --> The <b>Switch</b> for the model's inheritance hierarchy. It supports the call {@link #doSwitch(EObject) doSwitch(object)} to invoke the <code>caseXXX</code> method for each
 * class of the model, starting with the actual class of the object and proceeding up the inheritance hierarchy until a non-null result is returned, which is the result of the switch. <!--
 * end-user-doc -->
 * 
 * @see scenario.optimiser.OptimiserPackage
 * @generated
 */
public class OptimiserSwitch<T> extends Switch<T> {
	/**
	 * The cached model package <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected static OptimiserPackage modelPackage;

	/**
	 * Creates an instance of the switch. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public OptimiserSwitch() {
		if (modelPackage == null) {
			modelPackage = OptimiserPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @parameter ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(final EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(final int classifierID, final EObject theEObject) {
		switch (classifierID) {
		case OptimiserPackage.OPTIMISATION_SETTINGS: {
			final OptimisationSettings optimisationSettings = (OptimisationSettings) theEObject;
			T result = caseOptimisationSettings(optimisationSettings);
			if (result == null) {
				result = caseNamedObject(optimisationSettings);
			}
			if (result == null) {
				result = caseScenarioObject(optimisationSettings);
			}
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case OptimiserPackage.OPTIMISATION: {
			final Optimisation optimisation = (Optimisation) theEObject;
			T result = caseOptimisation(optimisation);
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case OptimiserPackage.CONSTRAINT: {
			final Constraint constraint = (Constraint) theEObject;
			T result = caseConstraint(constraint);
			if (result == null) {
				result = caseNamedObject(constraint);
			}
			if (result == null) {
				result = caseScenarioObject(constraint);
			}
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case OptimiserPackage.OBJECTIVE: {
			final Objective objective = (Objective) theEObject;
			T result = caseObjective(objective);
			if (result == null) {
				result = caseNamedObject(objective);
			}
			if (result == null) {
				result = caseScenarioObject(objective);
			}
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case OptimiserPackage.DISCOUNT_CURVE: {
			final DiscountCurve discountCurve = (DiscountCurve) theEObject;
			T result = caseDiscountCurve(discountCurve);
			if (result == null) {
				result = caseNamedObject(discountCurve);
			}
			if (result == null) {
				result = caseScenarioObject(discountCurve);
			}
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case OptimiserPackage.DISCOUNT: {
			final Discount discount = (Discount) theEObject;
			T result = caseDiscount(discount);
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		default:
			return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Optimisation Settings</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
	 * terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Optimisation Settings</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOptimisationSettings(final OptimisationSettings object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Optimisation</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Optimisation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOptimisation(final Optimisation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Constraint</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Constraint</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseConstraint(final Constraint object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Objective</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Objective</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseObjective(final Objective object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Discount Curve</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Discount Curve</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDiscountCurve(final DiscountCurve object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Discount</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Discount</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDiscount(final Discount object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Object</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Object</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseScenarioObject(final ScenarioObject object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Named Object</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Named Object</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNamedObject(final NamedObject object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch, but this is the last case anyway. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(final EObject object) {
		return null;
	}

} // OptimiserSwitch
