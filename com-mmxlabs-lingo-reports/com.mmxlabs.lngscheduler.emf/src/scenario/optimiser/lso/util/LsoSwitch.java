/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.optimiser.lso.util;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import scenario.NamedObject;
import scenario.ScenarioObject;
import scenario.optimiser.OptimisationSettings;
import scenario.optimiser.lso.ConstrainedMoveGeneratorSettings;
import scenario.optimiser.lso.LSOSettings;
import scenario.optimiser.lso.LsoPackage;
import scenario.optimiser.lso.MoveGeneratorSettings;
import scenario.optimiser.lso.RandomMoveGeneratorSettings;
import scenario.optimiser.lso.ThresholderSettings;

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
 * @see scenario.optimiser.lso.LsoPackage
 * @generated
 */
public class LsoSwitch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static LsoPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LsoSwitch() {
		if (modelPackage == null) {
			modelPackage = LsoPackage.eINSTANCE;
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
			case LsoPackage.LSO_SETTINGS: {
				LSOSettings lsoSettings = (LSOSettings)theEObject;
				T result = caseLSOSettings(lsoSettings);
				if (result == null) result = caseOptimisationSettings(lsoSettings);
				if (result == null) result = caseNamedObject(lsoSettings);
				if (result == null) result = caseScenarioObject(lsoSettings);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case LsoPackage.THRESHOLDER_SETTINGS: {
				ThresholderSettings thresholderSettings = (ThresholderSettings)theEObject;
				T result = caseThresholderSettings(thresholderSettings);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case LsoPackage.MOVE_GENERATOR_SETTINGS: {
				MoveGeneratorSettings moveGeneratorSettings = (MoveGeneratorSettings)theEObject;
				T result = caseMoveGeneratorSettings(moveGeneratorSettings);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS: {
				RandomMoveGeneratorSettings randomMoveGeneratorSettings = (RandomMoveGeneratorSettings)theEObject;
				T result = caseRandomMoveGeneratorSettings(randomMoveGeneratorSettings);
				if (result == null) result = caseMoveGeneratorSettings(randomMoveGeneratorSettings);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case LsoPackage.CONSTRAINED_MOVE_GENERATOR_SETTINGS: {
				ConstrainedMoveGeneratorSettings constrainedMoveGeneratorSettings = (ConstrainedMoveGeneratorSettings)theEObject;
				T result = caseConstrainedMoveGeneratorSettings(constrainedMoveGeneratorSettings);
				if (result == null) result = caseMoveGeneratorSettings(constrainedMoveGeneratorSettings);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>LSO Settings</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>LSO Settings</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLSOSettings(LSOSettings object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Thresholder Settings</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Thresholder Settings</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseThresholderSettings(ThresholderSettings object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Move Generator Settings</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Move Generator Settings</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMoveGeneratorSettings(MoveGeneratorSettings object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Random Move Generator Settings</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Random Move Generator Settings</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRandomMoveGeneratorSettings(RandomMoveGeneratorSettings object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Constrained Move Generator Settings</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Constrained Move Generator Settings</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseConstrainedMoveGeneratorSettings(ConstrainedMoveGeneratorSettings object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>Optimisation Settings</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Optimisation Settings</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOptimisationSettings(OptimisationSettings object) {
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

} //LsoSwitch
