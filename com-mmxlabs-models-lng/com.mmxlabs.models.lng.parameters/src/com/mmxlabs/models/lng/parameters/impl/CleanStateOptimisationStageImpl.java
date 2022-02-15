/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters.impl;

import com.mmxlabs.models.lng.parameters.AnnealingSettings;
import com.mmxlabs.models.lng.parameters.CleanStateOptimisationSettings;
import com.mmxlabs.models.lng.parameters.CleanStateOptimisationStage;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.ConstraintsAndFitnessSettingsStage;
import com.mmxlabs.models.lng.parameters.ParametersPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Clean State Optimisation Stage</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.CleanStateOptimisationStageImpl#getSeed <em>Seed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.CleanStateOptimisationStageImpl#getAnnealingSettings <em>Annealing Settings</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.CleanStateOptimisationStageImpl#getCleanStateSettings <em>Clean State Settings</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CleanStateOptimisationStageImpl extends ConstraintsAndFitnessSettingsStageImpl implements CleanStateOptimisationStage {
	/**
	 * The default value of the '{@link #getSeed() <em>Seed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSeed()
	 * @generated
	 * @ordered
	 */
	protected static final int SEED_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSeed() <em>Seed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSeed()
	 * @generated
	 * @ordered
	 */
	protected int seed = SEED_EDEFAULT;

	/**
	 * The cached value of the '{@link #getAnnealingSettings() <em>Annealing Settings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAnnealingSettings()
	 * @generated
	 * @ordered
	 */
	protected AnnealingSettings annealingSettings;

	/**
	 * The cached value of the '{@link #getCleanStateSettings() <em>Clean State Settings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCleanStateSettings()
	 * @generated
	 * @ordered
	 */
	protected CleanStateOptimisationSettings cleanStateSettings;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CleanStateOptimisationStageImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ParametersPackage.Literals.CLEAN_STATE_OPTIMISATION_STAGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getSeed() {
		return seed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSeed(int newSeed) {
		int oldSeed = seed;
		seed = newSeed;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.CLEAN_STATE_OPTIMISATION_STAGE__SEED, oldSeed, seed));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AnnealingSettings getAnnealingSettings() {
		return annealingSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAnnealingSettings(AnnealingSettings newAnnealingSettings, NotificationChain msgs) {
		AnnealingSettings oldAnnealingSettings = annealingSettings;
		annealingSettings = newAnnealingSettings;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ParametersPackage.CLEAN_STATE_OPTIMISATION_STAGE__ANNEALING_SETTINGS, oldAnnealingSettings, newAnnealingSettings);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAnnealingSettings(AnnealingSettings newAnnealingSettings) {
		if (newAnnealingSettings != annealingSettings) {
			NotificationChain msgs = null;
			if (annealingSettings != null)
				msgs = ((InternalEObject)annealingSettings).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.CLEAN_STATE_OPTIMISATION_STAGE__ANNEALING_SETTINGS, null, msgs);
			if (newAnnealingSettings != null)
				msgs = ((InternalEObject)newAnnealingSettings).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.CLEAN_STATE_OPTIMISATION_STAGE__ANNEALING_SETTINGS, null, msgs);
			msgs = basicSetAnnealingSettings(newAnnealingSettings, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.CLEAN_STATE_OPTIMISATION_STAGE__ANNEALING_SETTINGS, newAnnealingSettings, newAnnealingSettings));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CleanStateOptimisationSettings getCleanStateSettings() {
		return cleanStateSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCleanStateSettings(CleanStateOptimisationSettings newCleanStateSettings, NotificationChain msgs) {
		CleanStateOptimisationSettings oldCleanStateSettings = cleanStateSettings;
		cleanStateSettings = newCleanStateSettings;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ParametersPackage.CLEAN_STATE_OPTIMISATION_STAGE__CLEAN_STATE_SETTINGS, oldCleanStateSettings, newCleanStateSettings);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCleanStateSettings(CleanStateOptimisationSettings newCleanStateSettings) {
		if (newCleanStateSettings != cleanStateSettings) {
			NotificationChain msgs = null;
			if (cleanStateSettings != null)
				msgs = ((InternalEObject)cleanStateSettings).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.CLEAN_STATE_OPTIMISATION_STAGE__CLEAN_STATE_SETTINGS, null, msgs);
			if (newCleanStateSettings != null)
				msgs = ((InternalEObject)newCleanStateSettings).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.CLEAN_STATE_OPTIMISATION_STAGE__CLEAN_STATE_SETTINGS, null, msgs);
			msgs = basicSetCleanStateSettings(newCleanStateSettings, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.CLEAN_STATE_OPTIMISATION_STAGE__CLEAN_STATE_SETTINGS, newCleanStateSettings, newCleanStateSettings));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_STAGE__ANNEALING_SETTINGS:
				return basicSetAnnealingSettings(null, msgs);
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_STAGE__CLEAN_STATE_SETTINGS:
				return basicSetCleanStateSettings(null, msgs);
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
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_STAGE__SEED:
				return getSeed();
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_STAGE__ANNEALING_SETTINGS:
				return getAnnealingSettings();
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_STAGE__CLEAN_STATE_SETTINGS:
				return getCleanStateSettings();
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
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_STAGE__SEED:
				setSeed((Integer)newValue);
				return;
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_STAGE__ANNEALING_SETTINGS:
				setAnnealingSettings((AnnealingSettings)newValue);
				return;
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_STAGE__CLEAN_STATE_SETTINGS:
				setCleanStateSettings((CleanStateOptimisationSettings)newValue);
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
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_STAGE__SEED:
				setSeed(SEED_EDEFAULT);
				return;
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_STAGE__ANNEALING_SETTINGS:
				setAnnealingSettings((AnnealingSettings)null);
				return;
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_STAGE__CLEAN_STATE_SETTINGS:
				setCleanStateSettings((CleanStateOptimisationSettings)null);
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
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_STAGE__SEED:
				return seed != SEED_EDEFAULT;
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_STAGE__ANNEALING_SETTINGS:
				return annealingSettings != null;
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_STAGE__CLEAN_STATE_SETTINGS:
				return cleanStateSettings != null;
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

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (seed: ");
		result.append(seed);
		result.append(')');
		return result.toString();
	}

} //CleanStateOptimisationStageImpl
