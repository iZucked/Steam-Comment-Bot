/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AbstractSensitivityResult;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;

import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Sensitivity Result</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AbstractSensitivityResultImpl#getMinPnL <em>Min Pn L</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AbstractSensitivityResultImpl#getMaxPnL <em>Max Pn L</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AbstractSensitivityResultImpl#getAveragePnL <em>Average Pn L</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AbstractSensitivityResultImpl#getVariance <em>Variance</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class AbstractSensitivityResultImpl extends UUIDObjectImpl implements AbstractSensitivityResult {
	/**
	 * The default value of the '{@link #getMinPnL() <em>Min Pn L</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinPnL()
	 * @generated
	 * @ordered
	 */
	protected static final long MIN_PN_L_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getMinPnL() <em>Min Pn L</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinPnL()
	 * @generated
	 * @ordered
	 */
	protected long minPnL = MIN_PN_L_EDEFAULT;

	/**
	 * The default value of the '{@link #getMaxPnL() <em>Max Pn L</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxPnL()
	 * @generated
	 * @ordered
	 */
	protected static final long MAX_PN_L_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getMaxPnL() <em>Max Pn L</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxPnL()
	 * @generated
	 * @ordered
	 */
	protected long maxPnL = MAX_PN_L_EDEFAULT;

	/**
	 * The default value of the '{@link #getAveragePnL() <em>Average Pn L</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAveragePnL()
	 * @generated
	 * @ordered
	 */
	protected static final long AVERAGE_PN_L_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getAveragePnL() <em>Average Pn L</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAveragePnL()
	 * @generated
	 * @ordered
	 */
	protected long averagePnL = AVERAGE_PN_L_EDEFAULT;

	/**
	 * The default value of the '{@link #getVariance() <em>Variance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVariance()
	 * @generated
	 * @ordered
	 */
	protected static final double VARIANCE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getVariance() <em>Variance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVariance()
	 * @generated
	 * @ordered
	 */
	protected double variance = VARIANCE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AbstractSensitivityResultImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.ABSTRACT_SENSITIVITY_RESULT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getMinPnL() {
		return minPnL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMinPnL(long newMinPnL) {
		long oldMinPnL = minPnL;
		minPnL = newMinPnL;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.ABSTRACT_SENSITIVITY_RESULT__MIN_PN_L, oldMinPnL, minPnL));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getMaxPnL() {
		return maxPnL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMaxPnL(long newMaxPnL) {
		long oldMaxPnL = maxPnL;
		maxPnL = newMaxPnL;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.ABSTRACT_SENSITIVITY_RESULT__MAX_PN_L, oldMaxPnL, maxPnL));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getAveragePnL() {
		return averagePnL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAveragePnL(long newAveragePnL) {
		long oldAveragePnL = averagePnL;
		averagePnL = newAveragePnL;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.ABSTRACT_SENSITIVITY_RESULT__AVERAGE_PN_L, oldAveragePnL, averagePnL));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getVariance() {
		return variance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVariance(double newVariance) {
		double oldVariance = variance;
		variance = newVariance;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.ABSTRACT_SENSITIVITY_RESULT__VARIANCE, oldVariance, variance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AnalyticsPackage.ABSTRACT_SENSITIVITY_RESULT__MIN_PN_L:
				return getMinPnL();
			case AnalyticsPackage.ABSTRACT_SENSITIVITY_RESULT__MAX_PN_L:
				return getMaxPnL();
			case AnalyticsPackage.ABSTRACT_SENSITIVITY_RESULT__AVERAGE_PN_L:
				return getAveragePnL();
			case AnalyticsPackage.ABSTRACT_SENSITIVITY_RESULT__VARIANCE:
				return getVariance();
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
			case AnalyticsPackage.ABSTRACT_SENSITIVITY_RESULT__MIN_PN_L:
				setMinPnL((Long)newValue);
				return;
			case AnalyticsPackage.ABSTRACT_SENSITIVITY_RESULT__MAX_PN_L:
				setMaxPnL((Long)newValue);
				return;
			case AnalyticsPackage.ABSTRACT_SENSITIVITY_RESULT__AVERAGE_PN_L:
				setAveragePnL((Long)newValue);
				return;
			case AnalyticsPackage.ABSTRACT_SENSITIVITY_RESULT__VARIANCE:
				setVariance((Double)newValue);
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
			case AnalyticsPackage.ABSTRACT_SENSITIVITY_RESULT__MIN_PN_L:
				setMinPnL(MIN_PN_L_EDEFAULT);
				return;
			case AnalyticsPackage.ABSTRACT_SENSITIVITY_RESULT__MAX_PN_L:
				setMaxPnL(MAX_PN_L_EDEFAULT);
				return;
			case AnalyticsPackage.ABSTRACT_SENSITIVITY_RESULT__AVERAGE_PN_L:
				setAveragePnL(AVERAGE_PN_L_EDEFAULT);
				return;
			case AnalyticsPackage.ABSTRACT_SENSITIVITY_RESULT__VARIANCE:
				setVariance(VARIANCE_EDEFAULT);
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
			case AnalyticsPackage.ABSTRACT_SENSITIVITY_RESULT__MIN_PN_L:
				return minPnL != MIN_PN_L_EDEFAULT;
			case AnalyticsPackage.ABSTRACT_SENSITIVITY_RESULT__MAX_PN_L:
				return maxPnL != MAX_PN_L_EDEFAULT;
			case AnalyticsPackage.ABSTRACT_SENSITIVITY_RESULT__AVERAGE_PN_L:
				return averagePnL != AVERAGE_PN_L_EDEFAULT;
			case AnalyticsPackage.ABSTRACT_SENSITIVITY_RESULT__VARIANCE:
				return variance != VARIANCE_EDEFAULT;
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
		result.append(" (minPnL: ");
		result.append(minPnL);
		result.append(", maxPnL: ");
		result.append(maxPnL);
		result.append(", averagePnL: ");
		result.append(averagePnL);
		result.append(", variance: ");
		result.append(variance);
		result.append(')');
		return result.toString();
	}

} //AbstractSensitivityResultImpl
