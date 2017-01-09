/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.parameters.impl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import com.mmxlabs.models.lng.parameters.AnnealingSettings;
import com.mmxlabs.models.lng.parameters.ParametersPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Annealing Settings</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.AnnealingSettingsImpl#getIterations <em>Iterations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.AnnealingSettingsImpl#getEpochLength <em>Epoch Length</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.AnnealingSettingsImpl#getCooling <em>Cooling</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.AnnealingSettingsImpl#getInitialTemperature <em>Initial Temperature</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.AnnealingSettingsImpl#isRestarting <em>Restarting</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.AnnealingSettingsImpl#getRestartIterationsThreshold <em>Restart Iterations Threshold</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AnnealingSettingsImpl extends EObjectImpl implements AnnealingSettings {
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
	 * The default value of the '{@link #getEpochLength() <em>Epoch Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEpochLength()
	 * @generated
	 * @ordered
	 */
	protected static final int EPOCH_LENGTH_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getEpochLength() <em>Epoch Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEpochLength()
	 * @generated
	 * @ordered
	 */
	protected int epochLength = EPOCH_LENGTH_EDEFAULT;

	/**
	 * The default value of the '{@link #getCooling() <em>Cooling</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCooling()
	 * @generated
	 * @ordered
	 */
	protected static final double COOLING_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getCooling() <em>Cooling</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCooling()
	 * @generated
	 * @ordered
	 */
	protected double cooling = COOLING_EDEFAULT;

	/**
	 * The default value of the '{@link #getInitialTemperature() <em>Initial Temperature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInitialTemperature()
	 * @generated
	 * @ordered
	 */
	protected static final int INITIAL_TEMPERATURE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getInitialTemperature() <em>Initial Temperature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInitialTemperature()
	 * @generated
	 * @ordered
	 */
	protected int initialTemperature = INITIAL_TEMPERATURE_EDEFAULT;

	/**
	 * The default value of the '{@link #isRestarting() <em>Restarting</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRestarting()
	 * @generated
	 * @ordered
	 */
	protected static final boolean RESTARTING_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isRestarting() <em>Restarting</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRestarting()
	 * @generated
	 * @ordered
	 */
	protected boolean restarting = RESTARTING_EDEFAULT;

	/**
	 * The default value of the '{@link #getRestartIterationsThreshold() <em>Restart Iterations Threshold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRestartIterationsThreshold()
	 * @generated
	 * @ordered
	 */
	protected static final int RESTART_ITERATIONS_THRESHOLD_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getRestartIterationsThreshold() <em>Restart Iterations Threshold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRestartIterationsThreshold()
	 * @generated
	 * @ordered
	 */
	protected int restartIterationsThreshold = RESTART_ITERATIONS_THRESHOLD_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AnnealingSettingsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ParametersPackage.Literals.ANNEALING_SETTINGS;
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
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.ANNEALING_SETTINGS__ITERATIONS, oldIterations, iterations));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getEpochLength() {
		return epochLength;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEpochLength(int newEpochLength) {
		int oldEpochLength = epochLength;
		epochLength = newEpochLength;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.ANNEALING_SETTINGS__EPOCH_LENGTH, oldEpochLength, epochLength));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getCooling() {
		return cooling;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCooling(double newCooling) {
		double oldCooling = cooling;
		cooling = newCooling;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.ANNEALING_SETTINGS__COOLING, oldCooling, cooling));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getInitialTemperature() {
		return initialTemperature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setInitialTemperature(int newInitialTemperature) {
		int oldInitialTemperature = initialTemperature;
		initialTemperature = newInitialTemperature;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.ANNEALING_SETTINGS__INITIAL_TEMPERATURE, oldInitialTemperature, initialTemperature));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isRestarting() {
		return restarting;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRestarting(boolean newRestarting) {
		boolean oldRestarting = restarting;
		restarting = newRestarting;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.ANNEALING_SETTINGS__RESTARTING, oldRestarting, restarting));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getRestartIterationsThreshold() {
		return restartIterationsThreshold;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRestartIterationsThreshold(int newRestartIterationsThreshold) {
		int oldRestartIterationsThreshold = restartIterationsThreshold;
		restartIterationsThreshold = newRestartIterationsThreshold;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.ANNEALING_SETTINGS__RESTART_ITERATIONS_THRESHOLD, oldRestartIterationsThreshold, restartIterationsThreshold));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ParametersPackage.ANNEALING_SETTINGS__ITERATIONS:
				return getIterations();
			case ParametersPackage.ANNEALING_SETTINGS__EPOCH_LENGTH:
				return getEpochLength();
			case ParametersPackage.ANNEALING_SETTINGS__COOLING:
				return getCooling();
			case ParametersPackage.ANNEALING_SETTINGS__INITIAL_TEMPERATURE:
				return getInitialTemperature();
			case ParametersPackage.ANNEALING_SETTINGS__RESTARTING:
				return isRestarting();
			case ParametersPackage.ANNEALING_SETTINGS__RESTART_ITERATIONS_THRESHOLD:
				return getRestartIterationsThreshold();
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
			case ParametersPackage.ANNEALING_SETTINGS__ITERATIONS:
				setIterations((Integer)newValue);
				return;
			case ParametersPackage.ANNEALING_SETTINGS__EPOCH_LENGTH:
				setEpochLength((Integer)newValue);
				return;
			case ParametersPackage.ANNEALING_SETTINGS__COOLING:
				setCooling((Double)newValue);
				return;
			case ParametersPackage.ANNEALING_SETTINGS__INITIAL_TEMPERATURE:
				setInitialTemperature((Integer)newValue);
				return;
			case ParametersPackage.ANNEALING_SETTINGS__RESTARTING:
				setRestarting((Boolean)newValue);
				return;
			case ParametersPackage.ANNEALING_SETTINGS__RESTART_ITERATIONS_THRESHOLD:
				setRestartIterationsThreshold((Integer)newValue);
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
			case ParametersPackage.ANNEALING_SETTINGS__ITERATIONS:
				setIterations(ITERATIONS_EDEFAULT);
				return;
			case ParametersPackage.ANNEALING_SETTINGS__EPOCH_LENGTH:
				setEpochLength(EPOCH_LENGTH_EDEFAULT);
				return;
			case ParametersPackage.ANNEALING_SETTINGS__COOLING:
				setCooling(COOLING_EDEFAULT);
				return;
			case ParametersPackage.ANNEALING_SETTINGS__INITIAL_TEMPERATURE:
				setInitialTemperature(INITIAL_TEMPERATURE_EDEFAULT);
				return;
			case ParametersPackage.ANNEALING_SETTINGS__RESTARTING:
				setRestarting(RESTARTING_EDEFAULT);
				return;
			case ParametersPackage.ANNEALING_SETTINGS__RESTART_ITERATIONS_THRESHOLD:
				setRestartIterationsThreshold(RESTART_ITERATIONS_THRESHOLD_EDEFAULT);
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
			case ParametersPackage.ANNEALING_SETTINGS__ITERATIONS:
				return iterations != ITERATIONS_EDEFAULT;
			case ParametersPackage.ANNEALING_SETTINGS__EPOCH_LENGTH:
				return epochLength != EPOCH_LENGTH_EDEFAULT;
			case ParametersPackage.ANNEALING_SETTINGS__COOLING:
				return cooling != COOLING_EDEFAULT;
			case ParametersPackage.ANNEALING_SETTINGS__INITIAL_TEMPERATURE:
				return initialTemperature != INITIAL_TEMPERATURE_EDEFAULT;
			case ParametersPackage.ANNEALING_SETTINGS__RESTARTING:
				return restarting != RESTARTING_EDEFAULT;
			case ParametersPackage.ANNEALING_SETTINGS__RESTART_ITERATIONS_THRESHOLD:
				return restartIterationsThreshold != RESTART_ITERATIONS_THRESHOLD_EDEFAULT;
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
		result.append(", epochLength: ");
		result.append(epochLength);
		result.append(", cooling: ");
		result.append(cooling);
		result.append(", initialTemperature: ");
		result.append(initialTemperature);
		result.append(", restarting: ");
		result.append(restarting);
		result.append(", restartIterationsThreshold: ");
		result.append(restartIterationsThreshold);
		result.append(')');
		return result.toString();
	}

} // end of AnnealingSettingsImpl

// finish type fixing
