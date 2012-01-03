/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.market.impl;

import java.util.Date;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import scenario.market.MarketPackage;
import scenario.market.StepwisePrice;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Stepwise Price</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.market.impl.StepwisePriceImpl#getDate <em>Date</em>}</li>
 *   <li>{@link scenario.market.impl.StepwisePriceImpl#getPriceFromDate <em>Price From Date</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class StepwisePriceImpl extends EObjectImpl implements StepwisePrice {
	/**
	 * The default value of the '{@link #getDate() <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDate()
	 * @generated
	 * @ordered
	 */
	protected static final Date DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDate() <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDate()
	 * @generated
	 * @ordered
	 */
	protected Date date = DATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getPriceFromDate() <em>Price From Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriceFromDate()
	 * @generated
	 * @ordered
	 */
	protected static final float PRICE_FROM_DATE_EDEFAULT = 0.0F;

	/**
	 * The cached value of the '{@link #getPriceFromDate() <em>Price From Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriceFromDate()
	 * @generated
	 * @ordered
	 */
	protected float priceFromDate = PRICE_FROM_DATE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected StepwisePriceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MarketPackage.Literals.STEPWISE_PRICE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDate(Date newDate) {
		Date oldDate = date;
		date = newDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MarketPackage.STEPWISE_PRICE__DATE, oldDate, date));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public float getPriceFromDate() {
		return priceFromDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPriceFromDate(float newPriceFromDate) {
		float oldPriceFromDate = priceFromDate;
		priceFromDate = newPriceFromDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MarketPackage.STEPWISE_PRICE__PRICE_FROM_DATE, oldPriceFromDate, priceFromDate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MarketPackage.STEPWISE_PRICE__DATE:
				return getDate();
			case MarketPackage.STEPWISE_PRICE__PRICE_FROM_DATE:
				return getPriceFromDate();
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
			case MarketPackage.STEPWISE_PRICE__DATE:
				setDate((Date)newValue);
				return;
			case MarketPackage.STEPWISE_PRICE__PRICE_FROM_DATE:
				setPriceFromDate((Float)newValue);
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
			case MarketPackage.STEPWISE_PRICE__DATE:
				setDate(DATE_EDEFAULT);
				return;
			case MarketPackage.STEPWISE_PRICE__PRICE_FROM_DATE:
				setPriceFromDate(PRICE_FROM_DATE_EDEFAULT);
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
			case MarketPackage.STEPWISE_PRICE__DATE:
				return DATE_EDEFAULT == null ? date != null : !DATE_EDEFAULT.equals(date);
			case MarketPackage.STEPWISE_PRICE__PRICE_FROM_DATE:
				return priceFromDate != PRICE_FROM_DATE_EDEFAULT;
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
		result.append(" (date: ");
		result.append(date);
		result.append(", priceFromDate: ");
		result.append(priceFromDate);
		result.append(')');
		return result.toString();
	}

} //StepwisePriceImpl
