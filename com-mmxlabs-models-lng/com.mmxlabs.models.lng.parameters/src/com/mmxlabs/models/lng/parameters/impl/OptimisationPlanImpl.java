/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters.impl;

import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.OptimisationStage;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.parameters.SolutionBuilderSettings;
import com.mmxlabs.models.lng.parameters.UserSettings;

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
 * An implementation of the model object '<em><b>Optimisation Plan</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.OptimisationPlanImpl#getUserSettings <em>User Settings</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.OptimisationPlanImpl#getStages <em>Stages</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.OptimisationPlanImpl#getSolutionBuilderSettings <em>Solution Builder Settings</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OptimisationPlanImpl extends EObjectImpl implements OptimisationPlan {
	/**
	 * The cached value of the '{@link #getUserSettings() <em>User Settings</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUserSettings()
	 * @generated
	 * @ordered
	 */
	protected UserSettings userSettings;

	/**
	 * The cached value of the '{@link #getStages() <em>Stages</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStages()
	 * @generated
	 * @ordered
	 */
	protected EList<OptimisationStage> stages;

	/**
	 * The cached value of the '{@link #getSolutionBuilderSettings() <em>Solution Builder Settings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSolutionBuilderSettings()
	 * @generated
	 * @ordered
	 */
	protected SolutionBuilderSettings solutionBuilderSettings;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OptimisationPlanImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ParametersPackage.Literals.OPTIMISATION_PLAN;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UserSettings getUserSettings() {
		if (userSettings != null && userSettings.eIsProxy()) {
			InternalEObject oldUserSettings = (InternalEObject)userSettings;
			userSettings = (UserSettings)eResolveProxy(oldUserSettings);
			if (userSettings != oldUserSettings) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ParametersPackage.OPTIMISATION_PLAN__USER_SETTINGS, oldUserSettings, userSettings));
			}
		}
		return userSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UserSettings basicGetUserSettings() {
		return userSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUserSettings(UserSettings newUserSettings) {
		UserSettings oldUserSettings = userSettings;
		userSettings = newUserSettings;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.OPTIMISATION_PLAN__USER_SETTINGS, oldUserSettings, userSettings));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<OptimisationStage> getStages() {
		if (stages == null) {
			stages = new EObjectContainmentEList<OptimisationStage>(OptimisationStage.class, this, ParametersPackage.OPTIMISATION_PLAN__STAGES);
		}
		return stages;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SolutionBuilderSettings getSolutionBuilderSettings() {
		return solutionBuilderSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSolutionBuilderSettings(SolutionBuilderSettings newSolutionBuilderSettings, NotificationChain msgs) {
		SolutionBuilderSettings oldSolutionBuilderSettings = solutionBuilderSettings;
		solutionBuilderSettings = newSolutionBuilderSettings;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ParametersPackage.OPTIMISATION_PLAN__SOLUTION_BUILDER_SETTINGS, oldSolutionBuilderSettings, newSolutionBuilderSettings);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSolutionBuilderSettings(SolutionBuilderSettings newSolutionBuilderSettings) {
		if (newSolutionBuilderSettings != solutionBuilderSettings) {
			NotificationChain msgs = null;
			if (solutionBuilderSettings != null)
				msgs = ((InternalEObject)solutionBuilderSettings).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.OPTIMISATION_PLAN__SOLUTION_BUILDER_SETTINGS, null, msgs);
			if (newSolutionBuilderSettings != null)
				msgs = ((InternalEObject)newSolutionBuilderSettings).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.OPTIMISATION_PLAN__SOLUTION_BUILDER_SETTINGS, null, msgs);
			msgs = basicSetSolutionBuilderSettings(newSolutionBuilderSettings, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.OPTIMISATION_PLAN__SOLUTION_BUILDER_SETTINGS, newSolutionBuilderSettings, newSolutionBuilderSettings));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ParametersPackage.OPTIMISATION_PLAN__STAGES:
				return ((InternalEList<?>)getStages()).basicRemove(otherEnd, msgs);
			case ParametersPackage.OPTIMISATION_PLAN__SOLUTION_BUILDER_SETTINGS:
				return basicSetSolutionBuilderSettings(null, msgs);
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
			case ParametersPackage.OPTIMISATION_PLAN__USER_SETTINGS:
				if (resolve) return getUserSettings();
				return basicGetUserSettings();
			case ParametersPackage.OPTIMISATION_PLAN__STAGES:
				return getStages();
			case ParametersPackage.OPTIMISATION_PLAN__SOLUTION_BUILDER_SETTINGS:
				return getSolutionBuilderSettings();
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
			case ParametersPackage.OPTIMISATION_PLAN__USER_SETTINGS:
				setUserSettings((UserSettings)newValue);
				return;
			case ParametersPackage.OPTIMISATION_PLAN__STAGES:
				getStages().clear();
				getStages().addAll((Collection<? extends OptimisationStage>)newValue);
				return;
			case ParametersPackage.OPTIMISATION_PLAN__SOLUTION_BUILDER_SETTINGS:
				setSolutionBuilderSettings((SolutionBuilderSettings)newValue);
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
			case ParametersPackage.OPTIMISATION_PLAN__USER_SETTINGS:
				setUserSettings((UserSettings)null);
				return;
			case ParametersPackage.OPTIMISATION_PLAN__STAGES:
				getStages().clear();
				return;
			case ParametersPackage.OPTIMISATION_PLAN__SOLUTION_BUILDER_SETTINGS:
				setSolutionBuilderSettings((SolutionBuilderSettings)null);
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
			case ParametersPackage.OPTIMISATION_PLAN__USER_SETTINGS:
				return userSettings != null;
			case ParametersPackage.OPTIMISATION_PLAN__STAGES:
				return stages != null && !stages.isEmpty();
			case ParametersPackage.OPTIMISATION_PLAN__SOLUTION_BUILDER_SETTINGS:
				return solutionBuilderSettings != null;
		}
		return super.eIsSet(featureID);
	}

} //OptimisationPlanImpl
