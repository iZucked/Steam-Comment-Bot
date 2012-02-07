/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.optimiser.lso.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import scenario.optimiser.lso.LsoPackage;
import scenario.optimiser.lso.ThresholderSettings;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Thresholder Settings</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link scenario.optimiser.lso.impl.ThresholderSettingsImpl#getAlpha <em>Alpha</em>}</li>
 * <li>{@link scenario.optimiser.lso.impl.ThresholderSettingsImpl#getInitialAcceptanceRate <em>Initial Acceptance Rate</em>}</li>
 * <li>{@link scenario.optimiser.lso.impl.ThresholderSettingsImpl#getEpochLength <em>Epoch Length</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class ThresholderSettingsImpl extends EObjectImpl implements ThresholderSettings {
	/**
	 * The default value of the '{@link #getAlpha() <em>Alpha</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAlpha()
	 * @generated
	 * @ordered
	 */
	protected static final double ALPHA_EDEFAULT = 0.95;

	/**
	 * The cached value of the '{@link #getAlpha() <em>Alpha</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAlpha()
	 * @generated
	 * @ordered
	 */
	protected double alpha = ALPHA_EDEFAULT;

	/**
	 * The default value of the '{@link #getInitialAcceptanceRate() <em>Initial Acceptance Rate</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getInitialAcceptanceRate()
	 * @generated
	 * @ordered
	 */
	protected static final double INITIAL_ACCEPTANCE_RATE_EDEFAULT = 0.75;

	/**
	 * The cached value of the '{@link #getInitialAcceptanceRate() <em>Initial Acceptance Rate</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getInitialAcceptanceRate()
	 * @generated
	 * @ordered
	 */
	protected double initialAcceptanceRate = INITIAL_ACCEPTANCE_RATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getEpochLength() <em>Epoch Length</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getEpochLength()
	 * @generated
	 * @ordered
	 */
	protected static final int EPOCH_LENGTH_EDEFAULT = 1000;

	/**
	 * The cached value of the '{@link #getEpochLength() <em>Epoch Length</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getEpochLength()
	 * @generated
	 * @ordered
	 */
	protected int epochLength = EPOCH_LENGTH_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ThresholderSettingsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return LsoPackage.Literals.THRESHOLDER_SETTINGS;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public double getAlpha() {
		return alpha;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setAlpha(final double newAlpha) {
		final double oldAlpha = alpha;
		alpha = newAlpha;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, LsoPackage.THRESHOLDER_SETTINGS__ALPHA, oldAlpha, alpha));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public double getInitialAcceptanceRate() {
		return initialAcceptanceRate;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setInitialAcceptanceRate(final double newInitialAcceptanceRate) {
		final double oldInitialAcceptanceRate = initialAcceptanceRate;
		initialAcceptanceRate = newInitialAcceptanceRate;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, LsoPackage.THRESHOLDER_SETTINGS__INITIAL_ACCEPTANCE_RATE, oldInitialAcceptanceRate, initialAcceptanceRate));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int getEpochLength() {
		return epochLength;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setEpochLength(final int newEpochLength) {
		final int oldEpochLength = epochLength;
		epochLength = newEpochLength;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, LsoPackage.THRESHOLDER_SETTINGS__EPOCH_LENGTH, oldEpochLength, epochLength));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(final int featureID, final boolean resolve, final boolean coreType) {
		switch (featureID) {
		case LsoPackage.THRESHOLDER_SETTINGS__ALPHA:
			return getAlpha();
		case LsoPackage.THRESHOLDER_SETTINGS__INITIAL_ACCEPTANCE_RATE:
			return getInitialAcceptanceRate();
		case LsoPackage.THRESHOLDER_SETTINGS__EPOCH_LENGTH:
			return getEpochLength();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eSet(final int featureID, final Object newValue) {
		switch (featureID) {
		case LsoPackage.THRESHOLDER_SETTINGS__ALPHA:
			setAlpha((Double) newValue);
			return;
		case LsoPackage.THRESHOLDER_SETTINGS__INITIAL_ACCEPTANCE_RATE:
			setInitialAcceptanceRate((Double) newValue);
			return;
		case LsoPackage.THRESHOLDER_SETTINGS__EPOCH_LENGTH:
			setEpochLength((Integer) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(final int featureID) {
		switch (featureID) {
		case LsoPackage.THRESHOLDER_SETTINGS__ALPHA:
			setAlpha(ALPHA_EDEFAULT);
			return;
		case LsoPackage.THRESHOLDER_SETTINGS__INITIAL_ACCEPTANCE_RATE:
			setInitialAcceptanceRate(INITIAL_ACCEPTANCE_RATE_EDEFAULT);
			return;
		case LsoPackage.THRESHOLDER_SETTINGS__EPOCH_LENGTH:
			setEpochLength(EPOCH_LENGTH_EDEFAULT);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(final int featureID) {
		switch (featureID) {
		case LsoPackage.THRESHOLDER_SETTINGS__ALPHA:
			return alpha != ALPHA_EDEFAULT;
		case LsoPackage.THRESHOLDER_SETTINGS__INITIAL_ACCEPTANCE_RATE:
			return initialAcceptanceRate != INITIAL_ACCEPTANCE_RATE_EDEFAULT;
		case LsoPackage.THRESHOLDER_SETTINGS__EPOCH_LENGTH:
			return epochLength != EPOCH_LENGTH_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) {
			return super.toString();
		}

		final StringBuffer result = new StringBuffer(super.toString());
		result.append(" (alpha: ");
		result.append(alpha);
		result.append(", initialAcceptanceRate: ");
		result.append(initialAcceptanceRate);
		result.append(", epochLength: ");
		result.append(epochLength);
		result.append(')');
		return result.toString();
	}

} // ThresholderSettingsImpl
