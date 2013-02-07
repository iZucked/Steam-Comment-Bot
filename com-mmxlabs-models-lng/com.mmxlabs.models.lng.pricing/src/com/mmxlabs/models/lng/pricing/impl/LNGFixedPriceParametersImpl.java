

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.pricing.impl;
import com.mmxlabs.models.lng.pricing.LNGFixedPriceParameters;
import com.mmxlabs.models.lng.pricing.PricingPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>LNG Fixed Price Parameters</b></em>'.
 * @since 3.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.LNGFixedPriceParametersImpl#getPricePerMMBTU <em>Price Per MMBTU</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LNGFixedPriceParametersImpl extends LNGPriceCalculatorParametersImpl implements LNGFixedPriceParameters {
	/**
	 * The default value of the '{@link #getPricePerMMBTU() <em>Price Per MMBTU</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPricePerMMBTU()
	 * @generated
	 * @ordered
	 */
	protected static final double PRICE_PER_MMBTU_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getPricePerMMBTU() <em>Price Per MMBTU</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPricePerMMBTU()
	 * @generated
	 * @ordered
	 */
	protected double pricePerMMBTU = PRICE_PER_MMBTU_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LNGFixedPriceParametersImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.LNG_FIXED_PRICE_PARAMETERS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getPricePerMMBTU() {
		return pricePerMMBTU;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPricePerMMBTU(double newPricePerMMBTU) {
		double oldPricePerMMBTU = pricePerMMBTU;
		pricePerMMBTU = newPricePerMMBTU;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.LNG_FIXED_PRICE_PARAMETERS__PRICE_PER_MMBTU, oldPricePerMMBTU, pricePerMMBTU));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PricingPackage.LNG_FIXED_PRICE_PARAMETERS__PRICE_PER_MMBTU:
				return getPricePerMMBTU();
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
			case PricingPackage.LNG_FIXED_PRICE_PARAMETERS__PRICE_PER_MMBTU:
				setPricePerMMBTU((Double)newValue);
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
			case PricingPackage.LNG_FIXED_PRICE_PARAMETERS__PRICE_PER_MMBTU:
				setPricePerMMBTU(PRICE_PER_MMBTU_EDEFAULT);
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
			case PricingPackage.LNG_FIXED_PRICE_PARAMETERS__PRICE_PER_MMBTU:
				return pricePerMMBTU != PRICE_PER_MMBTU_EDEFAULT;
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
		result.append(" (pricePerMMBTU: ");
		result.append(pricePerMMBTU);
		result.append(')');
		return result.toString();
	}

} // end of LNGFixedPriceParametersImpl

// finish type fixing
