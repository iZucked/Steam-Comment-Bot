

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.pricing.impl;
import com.mmxlabs.models.lng.pricing.LNGIndexPriceParameters;
import com.mmxlabs.models.lng.pricing.PricingPackage;

import com.mmxlabs.models.lng.pricing.Index;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>LNG Index Price Parameters</b></em>'.
 * @since 3.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.LNGIndexPriceParametersImpl#getIndex <em>Index</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.LNGIndexPriceParametersImpl#getMultiplier <em>Multiplier</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.LNGIndexPriceParametersImpl#getConstant <em>Constant</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LNGIndexPriceParametersImpl extends LNGPriceCalculatorParametersImpl implements LNGIndexPriceParameters {
	/**
	 * The cached value of the '{@link #getIndex() <em>Index</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndex()
	 * @generated
	 * @ordered
	 */
	protected Index index;

	/**
	 * The default value of the '{@link #getMultiplier() <em>Multiplier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMultiplier()
	 * @generated
	 * @ordered
	 */
	protected static final double MULTIPLIER_EDEFAULT = 1.0;

	/**
	 * The cached value of the '{@link #getMultiplier() <em>Multiplier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMultiplier()
	 * @generated
	 * @ordered
	 */
	protected double multiplier = MULTIPLIER_EDEFAULT;

	/**
	 * The default value of the '{@link #getConstant() <em>Constant</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConstant()
	 * @generated
	 * @ordered
	 */
	protected static final double CONSTANT_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getConstant() <em>Constant</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConstant()
	 * @generated
	 * @ordered
	 */
	protected double constant = CONSTANT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LNGIndexPriceParametersImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.LNG_INDEX_PRICE_PARAMETERS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Index getIndex() {
		if (index != null && index.eIsProxy()) {
			InternalEObject oldIndex = (InternalEObject)index;
			index = (Index)eResolveProxy(oldIndex);
			if (index != oldIndex) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PricingPackage.LNG_INDEX_PRICE_PARAMETERS__INDEX, oldIndex, index));
			}
		}
		return index;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Index basicGetIndex() {
		return index;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIndex(Index newIndex) {
		Index oldIndex = index;
		index = newIndex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.LNG_INDEX_PRICE_PARAMETERS__INDEX, oldIndex, index));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getMultiplier() {
		return multiplier;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMultiplier(double newMultiplier) {
		double oldMultiplier = multiplier;
		multiplier = newMultiplier;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.LNG_INDEX_PRICE_PARAMETERS__MULTIPLIER, oldMultiplier, multiplier));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getConstant() {
		return constant;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConstant(double newConstant) {
		double oldConstant = constant;
		constant = newConstant;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.LNG_INDEX_PRICE_PARAMETERS__CONSTANT, oldConstant, constant));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PricingPackage.LNG_INDEX_PRICE_PARAMETERS__INDEX:
				if (resolve) return getIndex();
				return basicGetIndex();
			case PricingPackage.LNG_INDEX_PRICE_PARAMETERS__MULTIPLIER:
				return getMultiplier();
			case PricingPackage.LNG_INDEX_PRICE_PARAMETERS__CONSTANT:
				return getConstant();
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
			case PricingPackage.LNG_INDEX_PRICE_PARAMETERS__INDEX:
				setIndex((Index)newValue);
				return;
			case PricingPackage.LNG_INDEX_PRICE_PARAMETERS__MULTIPLIER:
				setMultiplier((Double)newValue);
				return;
			case PricingPackage.LNG_INDEX_PRICE_PARAMETERS__CONSTANT:
				setConstant((Double)newValue);
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
			case PricingPackage.LNG_INDEX_PRICE_PARAMETERS__INDEX:
				setIndex((Index)null);
				return;
			case PricingPackage.LNG_INDEX_PRICE_PARAMETERS__MULTIPLIER:
				setMultiplier(MULTIPLIER_EDEFAULT);
				return;
			case PricingPackage.LNG_INDEX_PRICE_PARAMETERS__CONSTANT:
				setConstant(CONSTANT_EDEFAULT);
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
			case PricingPackage.LNG_INDEX_PRICE_PARAMETERS__INDEX:
				return index != null;
			case PricingPackage.LNG_INDEX_PRICE_PARAMETERS__MULTIPLIER:
				return multiplier != MULTIPLIER_EDEFAULT;
			case PricingPackage.LNG_INDEX_PRICE_PARAMETERS__CONSTANT:
				return constant != CONSTANT_EDEFAULT;
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
		result.append(" (multiplier: ");
		result.append(multiplier);
		result.append(", constant: ");
		result.append(constant);
		result.append(')');
		return result.toString();
	}

} // end of LNGIndexPriceParametersImpl

// finish type fixing
