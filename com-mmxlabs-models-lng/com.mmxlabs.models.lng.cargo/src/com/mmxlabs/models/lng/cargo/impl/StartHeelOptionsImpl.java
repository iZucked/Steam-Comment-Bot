/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.StartHeelOptions;

import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Start Heel Options</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.StartHeelOptionsImpl#getCvValue <em>Cv Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.StartHeelOptionsImpl#getMinVolumeAvailable <em>Min Volume Available</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.StartHeelOptionsImpl#getMaxVolumeAvailable <em>Max Volume Available</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.StartHeelOptionsImpl#getPriceExpression <em>Price Expression</em>}</li>
 * </ul>
 *
 * @generated
 */
public class StartHeelOptionsImpl extends MMXObjectImpl implements StartHeelOptions {
	/**
	 * The default value of the '{@link #getCvValue() <em>Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCvValue()
	 * @generated
	 * @ordered
	 */
	protected static final double CV_VALUE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getCvValue() <em>Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCvValue()
	 * @generated
	 * @ordered
	 */
	protected double cvValue = CV_VALUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getMinVolumeAvailable() <em>Min Volume Available</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinVolumeAvailable()
	 * @generated
	 * @ordered
	 */
	protected static final double MIN_VOLUME_AVAILABLE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getMinVolumeAvailable() <em>Min Volume Available</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinVolumeAvailable()
	 * @generated
	 * @ordered
	 */
	protected double minVolumeAvailable = MIN_VOLUME_AVAILABLE_EDEFAULT;

	/**
	 * The default value of the '{@link #getMaxVolumeAvailable() <em>Max Volume Available</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxVolumeAvailable()
	 * @generated
	 * @ordered
	 */
	protected static final double MAX_VOLUME_AVAILABLE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getMaxVolumeAvailable() <em>Max Volume Available</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxVolumeAvailable()
	 * @generated
	 * @ordered
	 */
	protected double maxVolumeAvailable = MAX_VOLUME_AVAILABLE_EDEFAULT;

	/**
	 * The default value of the '{@link #getPriceExpression() <em>Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriceExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String PRICE_EXPRESSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPriceExpression() <em>Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriceExpression()
	 * @generated
	 * @ordered
	 */
	protected String priceExpression = PRICE_EXPRESSION_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected StartHeelOptionsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.START_HEEL_OPTIONS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getCvValue() {
		return cvValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCvValue(double newCvValue) {
		double oldCvValue = cvValue;
		cvValue = newCvValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.START_HEEL_OPTIONS__CV_VALUE, oldCvValue, cvValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getMinVolumeAvailable() {
		return minVolumeAvailable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinVolumeAvailable(double newMinVolumeAvailable) {
		double oldMinVolumeAvailable = minVolumeAvailable;
		minVolumeAvailable = newMinVolumeAvailable;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.START_HEEL_OPTIONS__MIN_VOLUME_AVAILABLE, oldMinVolumeAvailable, minVolumeAvailable));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getMaxVolumeAvailable() {
		return maxVolumeAvailable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxVolumeAvailable(double newMaxVolumeAvailable) {
		double oldMaxVolumeAvailable = maxVolumeAvailable;
		maxVolumeAvailable = newMaxVolumeAvailable;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.START_HEEL_OPTIONS__MAX_VOLUME_AVAILABLE, oldMaxVolumeAvailable, maxVolumeAvailable));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPriceExpression() {
		return priceExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPriceExpression(String newPriceExpression) {
		String oldPriceExpression = priceExpression;
		priceExpression = newPriceExpression;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.START_HEEL_OPTIONS__PRICE_EXPRESSION, oldPriceExpression, priceExpression));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoPackage.START_HEEL_OPTIONS__CV_VALUE:
				return getCvValue();
			case CargoPackage.START_HEEL_OPTIONS__MIN_VOLUME_AVAILABLE:
				return getMinVolumeAvailable();
			case CargoPackage.START_HEEL_OPTIONS__MAX_VOLUME_AVAILABLE:
				return getMaxVolumeAvailable();
			case CargoPackage.START_HEEL_OPTIONS__PRICE_EXPRESSION:
				return getPriceExpression();
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
			case CargoPackage.START_HEEL_OPTIONS__CV_VALUE:
				setCvValue((Double)newValue);
				return;
			case CargoPackage.START_HEEL_OPTIONS__MIN_VOLUME_AVAILABLE:
				setMinVolumeAvailable((Double)newValue);
				return;
			case CargoPackage.START_HEEL_OPTIONS__MAX_VOLUME_AVAILABLE:
				setMaxVolumeAvailable((Double)newValue);
				return;
			case CargoPackage.START_HEEL_OPTIONS__PRICE_EXPRESSION:
				setPriceExpression((String)newValue);
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
			case CargoPackage.START_HEEL_OPTIONS__CV_VALUE:
				setCvValue(CV_VALUE_EDEFAULT);
				return;
			case CargoPackage.START_HEEL_OPTIONS__MIN_VOLUME_AVAILABLE:
				setMinVolumeAvailable(MIN_VOLUME_AVAILABLE_EDEFAULT);
				return;
			case CargoPackage.START_HEEL_OPTIONS__MAX_VOLUME_AVAILABLE:
				setMaxVolumeAvailable(MAX_VOLUME_AVAILABLE_EDEFAULT);
				return;
			case CargoPackage.START_HEEL_OPTIONS__PRICE_EXPRESSION:
				setPriceExpression(PRICE_EXPRESSION_EDEFAULT);
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
			case CargoPackage.START_HEEL_OPTIONS__CV_VALUE:
				return cvValue != CV_VALUE_EDEFAULT;
			case CargoPackage.START_HEEL_OPTIONS__MIN_VOLUME_AVAILABLE:
				return minVolumeAvailable != MIN_VOLUME_AVAILABLE_EDEFAULT;
			case CargoPackage.START_HEEL_OPTIONS__MAX_VOLUME_AVAILABLE:
				return maxVolumeAvailable != MAX_VOLUME_AVAILABLE_EDEFAULT;
			case CargoPackage.START_HEEL_OPTIONS__PRICE_EXPRESSION:
				return PRICE_EXPRESSION_EDEFAULT == null ? priceExpression != null : !PRICE_EXPRESSION_EDEFAULT.equals(priceExpression);
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
		result.append(" (cvValue: ");
		result.append(cvValue);
		result.append(", minVolumeAvailable: ");
		result.append(minVolumeAvailable);
		result.append(", maxVolumeAvailable: ");
		result.append(maxVolumeAvailable);
		result.append(", priceExpression: ");
		result.append(priceExpression);
		result.append(')');
		return result.toString();
	}

} //StartHeelOptionsImpl
