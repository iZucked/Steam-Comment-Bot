/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters.impl;

import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.parameters.SolutionBuilderSettings;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Solution Builder Settings</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.SolutionBuilderSettingsImpl#getConstraintAndFitnessSettings <em>Constraint And Fitness Settings</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SolutionBuilderSettingsImpl extends EObjectImpl implements SolutionBuilderSettings {
	/**
	 * The cached value of the '{@link #getConstraintAndFitnessSettings() <em>Constraint And Fitness Settings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConstraintAndFitnessSettings()
	 * @generated
	 * @ordered
	 */
	protected ConstraintAndFitnessSettings constraintAndFitnessSettings;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SolutionBuilderSettingsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ParametersPackage.Literals.SOLUTION_BUILDER_SETTINGS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConstraintAndFitnessSettings getConstraintAndFitnessSettings() {
		return constraintAndFitnessSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetConstraintAndFitnessSettings(ConstraintAndFitnessSettings newConstraintAndFitnessSettings, NotificationChain msgs) {
		ConstraintAndFitnessSettings oldConstraintAndFitnessSettings = constraintAndFitnessSettings;
		constraintAndFitnessSettings = newConstraintAndFitnessSettings;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ParametersPackage.SOLUTION_BUILDER_SETTINGS__CONSTRAINT_AND_FITNESS_SETTINGS, oldConstraintAndFitnessSettings, newConstraintAndFitnessSettings);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConstraintAndFitnessSettings(ConstraintAndFitnessSettings newConstraintAndFitnessSettings) {
		if (newConstraintAndFitnessSettings != constraintAndFitnessSettings) {
			NotificationChain msgs = null;
			if (constraintAndFitnessSettings != null)
				msgs = ((InternalEObject)constraintAndFitnessSettings).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.SOLUTION_BUILDER_SETTINGS__CONSTRAINT_AND_FITNESS_SETTINGS, null, msgs);
			if (newConstraintAndFitnessSettings != null)
				msgs = ((InternalEObject)newConstraintAndFitnessSettings).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.SOLUTION_BUILDER_SETTINGS__CONSTRAINT_AND_FITNESS_SETTINGS, null, msgs);
			msgs = basicSetConstraintAndFitnessSettings(newConstraintAndFitnessSettings, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.SOLUTION_BUILDER_SETTINGS__CONSTRAINT_AND_FITNESS_SETTINGS, newConstraintAndFitnessSettings, newConstraintAndFitnessSettings));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ParametersPackage.SOLUTION_BUILDER_SETTINGS__CONSTRAINT_AND_FITNESS_SETTINGS:
				return basicSetConstraintAndFitnessSettings(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ParametersPackage.SOLUTION_BUILDER_SETTINGS__CONSTRAINT_AND_FITNESS_SETTINGS:
				return getConstraintAndFitnessSettings();
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
			case ParametersPackage.SOLUTION_BUILDER_SETTINGS__CONSTRAINT_AND_FITNESS_SETTINGS:
				setConstraintAndFitnessSettings((ConstraintAndFitnessSettings)newValue);
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
			case ParametersPackage.SOLUTION_BUILDER_SETTINGS__CONSTRAINT_AND_FITNESS_SETTINGS:
				setConstraintAndFitnessSettings((ConstraintAndFitnessSettings)null);
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
			case ParametersPackage.SOLUTION_BUILDER_SETTINGS__CONSTRAINT_AND_FITNESS_SETTINGS:
				return constraintAndFitnessSettings != null;
		}
		return super.eIsSet(featureID);
	}

} //SolutionBuilderSettingsImpl
