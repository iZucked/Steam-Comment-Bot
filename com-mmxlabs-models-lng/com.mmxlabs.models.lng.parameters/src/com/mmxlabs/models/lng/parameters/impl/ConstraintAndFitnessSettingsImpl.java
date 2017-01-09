/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters.impl;

import com.mmxlabs.models.lng.parameters.Constraint;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.Objective;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.parameters.SimilaritySettings;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Constraint And Fitness Settings</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.ConstraintAndFitnessSettingsImpl#getObjectives <em>Objectives</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.ConstraintAndFitnessSettingsImpl#getConstraints <em>Constraints</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.ConstraintAndFitnessSettingsImpl#getFloatingDaysLimit <em>Floating Days Limit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.ConstraintAndFitnessSettingsImpl#getSimilaritySettings <em>Similarity Settings</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ConstraintAndFitnessSettingsImpl extends EObjectImpl implements ConstraintAndFitnessSettings {
	/**
	 * The cached value of the '{@link #getObjectives() <em>Objectives</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getObjectives()
	 * @generated
	 * @ordered
	 */
	protected EList<Objective> objectives;

	/**
	 * The cached value of the '{@link #getConstraints() <em>Constraints</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConstraints()
	 * @generated
	 * @ordered
	 */
	protected EList<Constraint> constraints;

	/**
	 * The default value of the '{@link #getFloatingDaysLimit() <em>Floating Days Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFloatingDaysLimit()
	 * @generated
	 * @ordered
	 */
	protected static final int FLOATING_DAYS_LIMIT_EDEFAULT = 15;

	/**
	 * The cached value of the '{@link #getFloatingDaysLimit() <em>Floating Days Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFloatingDaysLimit()
	 * @generated
	 * @ordered
	 */
	protected int floatingDaysLimit = FLOATING_DAYS_LIMIT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getSimilaritySettings() <em>Similarity Settings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSimilaritySettings()
	 * @generated
	 * @ordered
	 */
	protected SimilaritySettings similaritySettings;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ConstraintAndFitnessSettingsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ParametersPackage.Literals.CONSTRAINT_AND_FITNESS_SETTINGS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Objective> getObjectives() {
		if (objectives == null) {
			objectives = new EObjectContainmentEList<Objective>(Objective.class, this, ParametersPackage.CONSTRAINT_AND_FITNESS_SETTINGS__OBJECTIVES);
		}
		return objectives;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Constraint> getConstraints() {
		if (constraints == null) {
			constraints = new EObjectContainmentEList<Constraint>(Constraint.class, this, ParametersPackage.CONSTRAINT_AND_FITNESS_SETTINGS__CONSTRAINTS);
		}
		return constraints;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getFloatingDaysLimit() {
		return floatingDaysLimit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFloatingDaysLimit(int newFloatingDaysLimit) {
		int oldFloatingDaysLimit = floatingDaysLimit;
		floatingDaysLimit = newFloatingDaysLimit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.CONSTRAINT_AND_FITNESS_SETTINGS__FLOATING_DAYS_LIMIT, oldFloatingDaysLimit, floatingDaysLimit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SimilaritySettings getSimilaritySettings() {
		return similaritySettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSimilaritySettings(SimilaritySettings newSimilaritySettings, NotificationChain msgs) {
		SimilaritySettings oldSimilaritySettings = similaritySettings;
		similaritySettings = newSimilaritySettings;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ParametersPackage.CONSTRAINT_AND_FITNESS_SETTINGS__SIMILARITY_SETTINGS, oldSimilaritySettings, newSimilaritySettings);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSimilaritySettings(SimilaritySettings newSimilaritySettings) {
		if (newSimilaritySettings != similaritySettings) {
			NotificationChain msgs = null;
			if (similaritySettings != null)
				msgs = ((InternalEObject)similaritySettings).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.CONSTRAINT_AND_FITNESS_SETTINGS__SIMILARITY_SETTINGS, null, msgs);
			if (newSimilaritySettings != null)
				msgs = ((InternalEObject)newSimilaritySettings).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.CONSTRAINT_AND_FITNESS_SETTINGS__SIMILARITY_SETTINGS, null, msgs);
			msgs = basicSetSimilaritySettings(newSimilaritySettings, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.CONSTRAINT_AND_FITNESS_SETTINGS__SIMILARITY_SETTINGS, newSimilaritySettings, newSimilaritySettings));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ParametersPackage.CONSTRAINT_AND_FITNESS_SETTINGS__OBJECTIVES:
				return ((InternalEList<?>)getObjectives()).basicRemove(otherEnd, msgs);
			case ParametersPackage.CONSTRAINT_AND_FITNESS_SETTINGS__CONSTRAINTS:
				return ((InternalEList<?>)getConstraints()).basicRemove(otherEnd, msgs);
			case ParametersPackage.CONSTRAINT_AND_FITNESS_SETTINGS__SIMILARITY_SETTINGS:
				return basicSetSimilaritySettings(null, msgs);
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
			case ParametersPackage.CONSTRAINT_AND_FITNESS_SETTINGS__OBJECTIVES:
				return getObjectives();
			case ParametersPackage.CONSTRAINT_AND_FITNESS_SETTINGS__CONSTRAINTS:
				return getConstraints();
			case ParametersPackage.CONSTRAINT_AND_FITNESS_SETTINGS__FLOATING_DAYS_LIMIT:
				return getFloatingDaysLimit();
			case ParametersPackage.CONSTRAINT_AND_FITNESS_SETTINGS__SIMILARITY_SETTINGS:
				return getSimilaritySettings();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ParametersPackage.CONSTRAINT_AND_FITNESS_SETTINGS__OBJECTIVES:
				getObjectives().clear();
				getObjectives().addAll((Collection<? extends Objective>)newValue);
				return;
			case ParametersPackage.CONSTRAINT_AND_FITNESS_SETTINGS__CONSTRAINTS:
				getConstraints().clear();
				getConstraints().addAll((Collection<? extends Constraint>)newValue);
				return;
			case ParametersPackage.CONSTRAINT_AND_FITNESS_SETTINGS__FLOATING_DAYS_LIMIT:
				setFloatingDaysLimit((Integer)newValue);
				return;
			case ParametersPackage.CONSTRAINT_AND_FITNESS_SETTINGS__SIMILARITY_SETTINGS:
				setSimilaritySettings((SimilaritySettings)newValue);
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
			case ParametersPackage.CONSTRAINT_AND_FITNESS_SETTINGS__OBJECTIVES:
				getObjectives().clear();
				return;
			case ParametersPackage.CONSTRAINT_AND_FITNESS_SETTINGS__CONSTRAINTS:
				getConstraints().clear();
				return;
			case ParametersPackage.CONSTRAINT_AND_FITNESS_SETTINGS__FLOATING_DAYS_LIMIT:
				setFloatingDaysLimit(FLOATING_DAYS_LIMIT_EDEFAULT);
				return;
			case ParametersPackage.CONSTRAINT_AND_FITNESS_SETTINGS__SIMILARITY_SETTINGS:
				setSimilaritySettings((SimilaritySettings)null);
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
			case ParametersPackage.CONSTRAINT_AND_FITNESS_SETTINGS__OBJECTIVES:
				return objectives != null && !objectives.isEmpty();
			case ParametersPackage.CONSTRAINT_AND_FITNESS_SETTINGS__CONSTRAINTS:
				return constraints != null && !constraints.isEmpty();
			case ParametersPackage.CONSTRAINT_AND_FITNESS_SETTINGS__FLOATING_DAYS_LIMIT:
				return floatingDaysLimit != FLOATING_DAYS_LIMIT_EDEFAULT;
			case ParametersPackage.CONSTRAINT_AND_FITNESS_SETTINGS__SIMILARITY_SETTINGS:
				return similaritySettings != null;
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
		result.append(" (floatingDaysLimit: ");
		result.append(floatingDaysLimit);
		result.append(')');
		return result.toString();
	}

} //ConstraintAndFitnessSettingsImpl
