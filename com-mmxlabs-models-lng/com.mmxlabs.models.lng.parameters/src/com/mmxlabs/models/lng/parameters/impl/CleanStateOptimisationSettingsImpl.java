/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters.impl;

import com.mmxlabs.models.lng.parameters.CleanStateOptimisationSettings;
import com.mmxlabs.models.lng.parameters.ParametersPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Clean State Optimisation Settings</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.CleanStateOptimisationSettingsImpl#getSeed <em>Seed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.CleanStateOptimisationSettingsImpl#getGlobalIterations <em>Global Iterations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.CleanStateOptimisationSettingsImpl#getLocalIterations <em>Local Iterations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.CleanStateOptimisationSettingsImpl#getTabuSize <em>Tabu Size</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CleanStateOptimisationSettingsImpl extends EObjectImpl implements CleanStateOptimisationSettings {
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
	 * The default value of the '{@link #getGlobalIterations() <em>Global Iterations</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGlobalIterations()
	 * @generated
	 * @ordered
	 */
	protected static final int GLOBAL_ITERATIONS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getGlobalIterations() <em>Global Iterations</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGlobalIterations()
	 * @generated
	 * @ordered
	 */
	protected int globalIterations = GLOBAL_ITERATIONS_EDEFAULT;

	/**
	 * The default value of the '{@link #getLocalIterations() <em>Local Iterations</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLocalIterations()
	 * @generated
	 * @ordered
	 */
	protected static final int LOCAL_ITERATIONS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getLocalIterations() <em>Local Iterations</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLocalIterations()
	 * @generated
	 * @ordered
	 */
	protected int localIterations = LOCAL_ITERATIONS_EDEFAULT;

	/**
	 * The default value of the '{@link #getTabuSize() <em>Tabu Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTabuSize()
	 * @generated
	 * @ordered
	 */
	protected static final int TABU_SIZE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getTabuSize() <em>Tabu Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTabuSize()
	 * @generated
	 * @ordered
	 */
	protected int tabuSize = TABU_SIZE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CleanStateOptimisationSettingsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ParametersPackage.Literals.CLEAN_STATE_OPTIMISATION_SETTINGS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getGlobalIterations() {
		return globalIterations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setGlobalIterations(int newGlobalIterations) {
		int oldGlobalIterations = globalIterations;
		globalIterations = newGlobalIterations;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.CLEAN_STATE_OPTIMISATION_SETTINGS__GLOBAL_ITERATIONS, oldGlobalIterations, globalIterations));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getLocalIterations() {
		return localIterations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLocalIterations(int newLocalIterations) {
		int oldLocalIterations = localIterations;
		localIterations = newLocalIterations;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.CLEAN_STATE_OPTIMISATION_SETTINGS__LOCAL_ITERATIONS, oldLocalIterations, localIterations));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getTabuSize() {
		return tabuSize;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTabuSize(int newTabuSize) {
		int oldTabuSize = tabuSize;
		tabuSize = newTabuSize;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.CLEAN_STATE_OPTIMISATION_SETTINGS__TABU_SIZE, oldTabuSize, tabuSize));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.CLEAN_STATE_OPTIMISATION_SETTINGS__SEED, oldSeed, seed));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_SETTINGS__SEED:
				return getSeed();
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_SETTINGS__GLOBAL_ITERATIONS:
				return getGlobalIterations();
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_SETTINGS__LOCAL_ITERATIONS:
				return getLocalIterations();
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_SETTINGS__TABU_SIZE:
				return getTabuSize();
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
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_SETTINGS__SEED:
				setSeed((Integer)newValue);
				return;
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_SETTINGS__GLOBAL_ITERATIONS:
				setGlobalIterations((Integer)newValue);
				return;
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_SETTINGS__LOCAL_ITERATIONS:
				setLocalIterations((Integer)newValue);
				return;
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_SETTINGS__TABU_SIZE:
				setTabuSize((Integer)newValue);
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
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_SETTINGS__SEED:
				setSeed(SEED_EDEFAULT);
				return;
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_SETTINGS__GLOBAL_ITERATIONS:
				setGlobalIterations(GLOBAL_ITERATIONS_EDEFAULT);
				return;
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_SETTINGS__LOCAL_ITERATIONS:
				setLocalIterations(LOCAL_ITERATIONS_EDEFAULT);
				return;
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_SETTINGS__TABU_SIZE:
				setTabuSize(TABU_SIZE_EDEFAULT);
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
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_SETTINGS__SEED:
				return seed != SEED_EDEFAULT;
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_SETTINGS__GLOBAL_ITERATIONS:
				return globalIterations != GLOBAL_ITERATIONS_EDEFAULT;
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_SETTINGS__LOCAL_ITERATIONS:
				return localIterations != LOCAL_ITERATIONS_EDEFAULT;
			case ParametersPackage.CLEAN_STATE_OPTIMISATION_SETTINGS__TABU_SIZE:
				return tabuSize != TABU_SIZE_EDEFAULT;
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
		result.append(", globalIterations: ");
		result.append(globalIterations);
		result.append(", localIterations: ");
		result.append(localIterations);
		result.append(", tabuSize: ");
		result.append(tabuSize);
		result.append(')');
		return result.toString();
	}

} //CleanStateOptimisationSettingsImpl
