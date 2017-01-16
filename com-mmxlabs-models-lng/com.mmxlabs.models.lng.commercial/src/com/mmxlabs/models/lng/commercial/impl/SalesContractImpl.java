/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.impl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.types.CargoDeliveryType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Sales Contract</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.SalesContractImpl#getMinCvValue <em>Min Cv Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.SalesContractImpl#getMaxCvValue <em>Max Cv Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.SalesContractImpl#getPurchaseDeliveryType <em>Purchase Delivery Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SalesContractImpl extends ContractImpl implements SalesContract {
	/**
	 * The default value of the '{@link #getMinCvValue() <em>Min Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @see #getMinCvValue()
	 * @generated
	 * @ordered
	 */
	protected static final double MIN_CV_VALUE_EDEFAULT = 0.0;
	/**
	 * The cached value of the '{@link #getMinCvValue() <em>Min Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinCvValue()
	 * @generated
	 * @ordered
	 */
	protected double minCvValue = MIN_CV_VALUE_EDEFAULT;
	/**
	 * This is true if the Min Cv Value attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean minCvValueESet;
	/**
	 * The default value of the '{@link #getMaxCvValue() <em>Max Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxCvValue()
	 * @generated
	 * @ordered
	 */
	protected static final double MAX_CV_VALUE_EDEFAULT = 0.0;
	/**
	 * The cached value of the '{@link #getMaxCvValue() <em>Max Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxCvValue()
	 * @generated
	 * @ordered
	 */
	protected double maxCvValue = MAX_CV_VALUE_EDEFAULT;

	/**
	 * This is true if the Max Cv Value attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean maxCvValueESet;

	/**
	 * The default value of the '{@link #getPurchaseDeliveryType() <em>Purchase Delivery Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPurchaseDeliveryType()
	 * @generated
	 * @ordered
	 */
	protected static final CargoDeliveryType PURCHASE_DELIVERY_TYPE_EDEFAULT = CargoDeliveryType.ANY;
	/**
	 * The cached value of the '{@link #getPurchaseDeliveryType() <em>Purchase Delivery Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPurchaseDeliveryType()
	 * @generated
	 * @ordered
	 */
	protected CargoDeliveryType purchaseDeliveryType = PURCHASE_DELIVERY_TYPE_EDEFAULT;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SalesContractImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CommercialPackage.Literals.SALES_CONTRACT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getMinCvValue() {
		return minCvValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinCvValue(double newMinCvValue) {
		double oldMinCvValue = minCvValue;
		minCvValue = newMinCvValue;
		boolean oldMinCvValueESet = minCvValueESet;
		minCvValueESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.SALES_CONTRACT__MIN_CV_VALUE, oldMinCvValue, minCvValue, !oldMinCvValueESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetMinCvValue() {
		double oldMinCvValue = minCvValue;
		boolean oldMinCvValueESet = minCvValueESet;
		minCvValue = MIN_CV_VALUE_EDEFAULT;
		minCvValueESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CommercialPackage.SALES_CONTRACT__MIN_CV_VALUE, oldMinCvValue, MIN_CV_VALUE_EDEFAULT, oldMinCvValueESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetMinCvValue() {
		return minCvValueESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getMaxCvValue() {
		return maxCvValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxCvValue(double newMaxCvValue) {
		double oldMaxCvValue = maxCvValue;
		maxCvValue = newMaxCvValue;
		boolean oldMaxCvValueESet = maxCvValueESet;
		maxCvValueESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.SALES_CONTRACT__MAX_CV_VALUE, oldMaxCvValue, maxCvValue, !oldMaxCvValueESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetMaxCvValue() {
		double oldMaxCvValue = maxCvValue;
		boolean oldMaxCvValueESet = maxCvValueESet;
		maxCvValue = MAX_CV_VALUE_EDEFAULT;
		maxCvValueESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CommercialPackage.SALES_CONTRACT__MAX_CV_VALUE, oldMaxCvValue, MAX_CV_VALUE_EDEFAULT, oldMaxCvValueESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetMaxCvValue() {
		return maxCvValueESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CargoDeliveryType getPurchaseDeliveryType() {
		return purchaseDeliveryType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPurchaseDeliveryType(CargoDeliveryType newPurchaseDeliveryType) {
		CargoDeliveryType oldPurchaseDeliveryType = purchaseDeliveryType;
		purchaseDeliveryType = newPurchaseDeliveryType == null ? PURCHASE_DELIVERY_TYPE_EDEFAULT : newPurchaseDeliveryType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.SALES_CONTRACT__PURCHASE_DELIVERY_TYPE, oldPurchaseDeliveryType, purchaseDeliveryType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CommercialPackage.SALES_CONTRACT__MIN_CV_VALUE:
				return getMinCvValue();
			case CommercialPackage.SALES_CONTRACT__MAX_CV_VALUE:
				return getMaxCvValue();
			case CommercialPackage.SALES_CONTRACT__PURCHASE_DELIVERY_TYPE:
				return getPurchaseDeliveryType();
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
			case CommercialPackage.SALES_CONTRACT__MIN_CV_VALUE:
				setMinCvValue((Double)newValue);
				return;
			case CommercialPackage.SALES_CONTRACT__MAX_CV_VALUE:
				setMaxCvValue((Double)newValue);
				return;
			case CommercialPackage.SALES_CONTRACT__PURCHASE_DELIVERY_TYPE:
				setPurchaseDeliveryType((CargoDeliveryType)newValue);
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
			case CommercialPackage.SALES_CONTRACT__MIN_CV_VALUE:
				unsetMinCvValue();
				return;
			case CommercialPackage.SALES_CONTRACT__MAX_CV_VALUE:
				unsetMaxCvValue();
				return;
			case CommercialPackage.SALES_CONTRACT__PURCHASE_DELIVERY_TYPE:
				setPurchaseDeliveryType(PURCHASE_DELIVERY_TYPE_EDEFAULT);
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
			case CommercialPackage.SALES_CONTRACT__MIN_CV_VALUE:
				return isSetMinCvValue();
			case CommercialPackage.SALES_CONTRACT__MAX_CV_VALUE:
				return isSetMaxCvValue();
			case CommercialPackage.SALES_CONTRACT__PURCHASE_DELIVERY_TYPE:
				return purchaseDeliveryType != PURCHASE_DELIVERY_TYPE_EDEFAULT;
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
		result.append(" (minCvValue: ");
		if (minCvValueESet) result.append(minCvValue); else result.append("<unset>");
		result.append(", maxCvValue: ");
		if (maxCvValueESet) result.append(maxCvValue); else result.append("<unset>");
		result.append(", PurchaseDeliveryType: ");
		result.append(purchaseDeliveryType);
		result.append(')');
		return result.toString();
	}

} // end of SalesContractImpl

// finish type fixing
