/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import com.mmxlabs.models.lng.parameters.IndividualSolutionImprovementSettings;
import com.mmxlabs.models.lng.parameters.ParametersPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Individual Solution Improvement Settings</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.IndividualSolutionImprovementSettingsImpl#getIterations <em>Iterations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.IndividualSolutionImprovementSettingsImpl#isImprovingSolutions <em>Improving Solutions</em>}</li>
 * </ul>
 *
 * @generated
 */
public class IndividualSolutionImprovementSettingsImpl extends EObjectImpl implements IndividualSolutionImprovementSettings {
	/**
	 * The default value of the '{@link #getIterations() <em>Iterations</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIterations()
	 * @generated
	 * @ordered
	 */
	protected static final int ITERATIONS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getIterations() <em>Iterations</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIterations()
	 * @generated
	 * @ordered
	 */
	protected int iterations = ITERATIONS_EDEFAULT;

	/**
	 * The default value of the '{@link #isImprovingSolutions() <em>Improving Solutions</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isImprovingSolutions()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IMPROVING_SOLUTIONS_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isImprovingSolutions() <em>Improving Solutions</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isImprovingSolutions()
	 * @generated
	 * @ordered
	 */
	protected boolean improvingSolutions = IMPROVING_SOLUTIONS_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IndividualSolutionImprovementSettingsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ParametersPackage.Literals.INDIVIDUAL_SOLUTION_IMPROVEMENT_SETTINGS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getIterations() {
		return iterations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setIterations(int newIterations) {
		int oldIterations = iterations;
		iterations = newIterations;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.INDIVIDUAL_SOLUTION_IMPROVEMENT_SETTINGS__ITERATIONS, oldIterations, iterations));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isImprovingSolutions() {
		return improvingSolutions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setImprovingSolutions(boolean newImprovingSolutions) {
		boolean oldImprovingSolutions = improvingSolutions;
		improvingSolutions = newImprovingSolutions;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.INDIVIDUAL_SOLUTION_IMPROVEMENT_SETTINGS__IMPROVING_SOLUTIONS, oldImprovingSolutions, improvingSolutions));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ParametersPackage.INDIVIDUAL_SOLUTION_IMPROVEMENT_SETTINGS__ITERATIONS:
				return getIterations();
			case ParametersPackage.INDIVIDUAL_SOLUTION_IMPROVEMENT_SETTINGS__IMPROVING_SOLUTIONS:
				return isImprovingSolutions();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ParametersPackage.INDIVIDUAL_SOLUTION_IMPROVEMENT_SETTINGS__ITERATIONS:
				setIterations((Integer)newValue);
				return;
			case ParametersPackage.INDIVIDUAL_SOLUTION_IMPROVEMENT_SETTINGS__IMPROVING_SOLUTIONS:
				setImprovingSolutions((Boolean)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ParametersPackage.INDIVIDUAL_SOLUTION_IMPROVEMENT_SETTINGS__ITERATIONS:
				setIterations(ITERATIONS_EDEFAULT);
				return;
			case ParametersPackage.INDIVIDUAL_SOLUTION_IMPROVEMENT_SETTINGS__IMPROVING_SOLUTIONS:
				setImprovingSolutions(IMPROVING_SOLUTIONS_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ParametersPackage.INDIVIDUAL_SOLUTION_IMPROVEMENT_SETTINGS__ITERATIONS:
				return iterations != ITERATIONS_EDEFAULT;
			case ParametersPackage.INDIVIDUAL_SOLUTION_IMPROVEMENT_SETTINGS__IMPROVING_SOLUTIONS:
				return improvingSolutions != IMPROVING_SOLUTIONS_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (iterations: ");
		result.append(iterations);
		result.append(", improvingSolutions: ");
		result.append(improvingSolutions);
		result.append(')');
		return result.toString();
	}

} //IndividualSolutionImprovementSettingsImpl
