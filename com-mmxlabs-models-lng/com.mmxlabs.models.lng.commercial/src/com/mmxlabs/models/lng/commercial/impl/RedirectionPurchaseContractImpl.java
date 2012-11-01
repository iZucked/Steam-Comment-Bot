

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.commercial.impl;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract;

import com.mmxlabs.models.lng.port.Port;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Redirection Purchase Contract</b></em>'.
 * @since 2.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.RedirectionPurchaseContractImpl#getBaseSalesMarketPort <em>Base Sales Market Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.RedirectionPurchaseContractImpl#getBaseSalesPriceExpression <em>Base Sales Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.RedirectionPurchaseContractImpl#getBasePurchasePriceExpression <em>Base Purchase Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.RedirectionPurchaseContractImpl#getNotionalSpeed <em>Notional Speed</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RedirectionPurchaseContractImpl extends PurchaseContractImpl implements RedirectionPurchaseContract {
	/**
	 * The cached value of the '{@link #getBaseSalesMarketPort() <em>Base Sales Market Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseSalesMarketPort()
	 * @generated
	 * @ordered
	 */
	protected Port baseSalesMarketPort;

	/**
	 * The default value of the '{@link #getBaseSalesPriceExpression() <em>Base Sales Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseSalesPriceExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String BASE_SALES_PRICE_EXPRESSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBaseSalesPriceExpression() <em>Base Sales Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseSalesPriceExpression()
	 * @generated
	 * @ordered
	 */
	protected String baseSalesPriceExpression = BASE_SALES_PRICE_EXPRESSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getBasePurchasePriceExpression() <em>Base Purchase Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBasePurchasePriceExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String BASE_PURCHASE_PRICE_EXPRESSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBasePurchasePriceExpression() <em>Base Purchase Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBasePurchasePriceExpression()
	 * @generated
	 * @ordered
	 */
	protected String basePurchasePriceExpression = BASE_PURCHASE_PRICE_EXPRESSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getNotionalSpeed() <em>Notional Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNotionalSpeed()
	 * @generated
	 * @ordered
	 */
	protected static final double NOTIONAL_SPEED_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getNotionalSpeed() <em>Notional Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNotionalSpeed()
	 * @generated
	 * @ordered
	 */
	protected double notionalSpeed = NOTIONAL_SPEED_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RedirectionPurchaseContractImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CommercialPackage.Literals.REDIRECTION_PURCHASE_CONTRACT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port getBaseSalesMarketPort() {
		if (baseSalesMarketPort != null && baseSalesMarketPort.eIsProxy()) {
			InternalEObject oldBaseSalesMarketPort = (InternalEObject)baseSalesMarketPort;
			baseSalesMarketPort = (Port)eResolveProxy(oldBaseSalesMarketPort);
			if (baseSalesMarketPort != oldBaseSalesMarketPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CommercialPackage.REDIRECTION_PURCHASE_CONTRACT__BASE_SALES_MARKET_PORT, oldBaseSalesMarketPort, baseSalesMarketPort));
			}
		}
		return baseSalesMarketPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetBaseSalesMarketPort() {
		return baseSalesMarketPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBaseSalesMarketPort(Port newBaseSalesMarketPort) {
		Port oldBaseSalesMarketPort = baseSalesMarketPort;
		baseSalesMarketPort = newBaseSalesMarketPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.REDIRECTION_PURCHASE_CONTRACT__BASE_SALES_MARKET_PORT, oldBaseSalesMarketPort, baseSalesMarketPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getBaseSalesPriceExpression() {
		return baseSalesPriceExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBaseSalesPriceExpression(String newBaseSalesPriceExpression) {
		String oldBaseSalesPriceExpression = baseSalesPriceExpression;
		baseSalesPriceExpression = newBaseSalesPriceExpression;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.REDIRECTION_PURCHASE_CONTRACT__BASE_SALES_PRICE_EXPRESSION, oldBaseSalesPriceExpression, baseSalesPriceExpression));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getBasePurchasePriceExpression() {
		return basePurchasePriceExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBasePurchasePriceExpression(String newBasePurchasePriceExpression) {
		String oldBasePurchasePriceExpression = basePurchasePriceExpression;
		basePurchasePriceExpression = newBasePurchasePriceExpression;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.REDIRECTION_PURCHASE_CONTRACT__BASE_PURCHASE_PRICE_EXPRESSION, oldBasePurchasePriceExpression, basePurchasePriceExpression));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getNotionalSpeed() {
		return notionalSpeed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNotionalSpeed(double newNotionalSpeed) {
		double oldNotionalSpeed = notionalSpeed;
		notionalSpeed = newNotionalSpeed;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.REDIRECTION_PURCHASE_CONTRACT__NOTIONAL_SPEED, oldNotionalSpeed, notionalSpeed));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CommercialPackage.REDIRECTION_PURCHASE_CONTRACT__BASE_SALES_MARKET_PORT:
				if (resolve) return getBaseSalesMarketPort();
				return basicGetBaseSalesMarketPort();
			case CommercialPackage.REDIRECTION_PURCHASE_CONTRACT__BASE_SALES_PRICE_EXPRESSION:
				return getBaseSalesPriceExpression();
			case CommercialPackage.REDIRECTION_PURCHASE_CONTRACT__BASE_PURCHASE_PRICE_EXPRESSION:
				return getBasePurchasePriceExpression();
			case CommercialPackage.REDIRECTION_PURCHASE_CONTRACT__NOTIONAL_SPEED:
				return getNotionalSpeed();
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
			case CommercialPackage.REDIRECTION_PURCHASE_CONTRACT__BASE_SALES_MARKET_PORT:
				setBaseSalesMarketPort((Port)newValue);
				return;
			case CommercialPackage.REDIRECTION_PURCHASE_CONTRACT__BASE_SALES_PRICE_EXPRESSION:
				setBaseSalesPriceExpression((String)newValue);
				return;
			case CommercialPackage.REDIRECTION_PURCHASE_CONTRACT__BASE_PURCHASE_PRICE_EXPRESSION:
				setBasePurchasePriceExpression((String)newValue);
				return;
			case CommercialPackage.REDIRECTION_PURCHASE_CONTRACT__NOTIONAL_SPEED:
				setNotionalSpeed((Double)newValue);
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
			case CommercialPackage.REDIRECTION_PURCHASE_CONTRACT__BASE_SALES_MARKET_PORT:
				setBaseSalesMarketPort((Port)null);
				return;
			case CommercialPackage.REDIRECTION_PURCHASE_CONTRACT__BASE_SALES_PRICE_EXPRESSION:
				setBaseSalesPriceExpression(BASE_SALES_PRICE_EXPRESSION_EDEFAULT);
				return;
			case CommercialPackage.REDIRECTION_PURCHASE_CONTRACT__BASE_PURCHASE_PRICE_EXPRESSION:
				setBasePurchasePriceExpression(BASE_PURCHASE_PRICE_EXPRESSION_EDEFAULT);
				return;
			case CommercialPackage.REDIRECTION_PURCHASE_CONTRACT__NOTIONAL_SPEED:
				setNotionalSpeed(NOTIONAL_SPEED_EDEFAULT);
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
			case CommercialPackage.REDIRECTION_PURCHASE_CONTRACT__BASE_SALES_MARKET_PORT:
				return baseSalesMarketPort != null;
			case CommercialPackage.REDIRECTION_PURCHASE_CONTRACT__BASE_SALES_PRICE_EXPRESSION:
				return BASE_SALES_PRICE_EXPRESSION_EDEFAULT == null ? baseSalesPriceExpression != null : !BASE_SALES_PRICE_EXPRESSION_EDEFAULT.equals(baseSalesPriceExpression);
			case CommercialPackage.REDIRECTION_PURCHASE_CONTRACT__BASE_PURCHASE_PRICE_EXPRESSION:
				return BASE_PURCHASE_PRICE_EXPRESSION_EDEFAULT == null ? basePurchasePriceExpression != null : !BASE_PURCHASE_PRICE_EXPRESSION_EDEFAULT.equals(basePurchasePriceExpression);
			case CommercialPackage.REDIRECTION_PURCHASE_CONTRACT__NOTIONAL_SPEED:
				return notionalSpeed != NOTIONAL_SPEED_EDEFAULT;
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
		result.append(" (baseSalesPriceExpression: ");
		result.append(baseSalesPriceExpression);
		result.append(", basePurchasePriceExpression: ");
		result.append(basePurchasePriceExpression);
		result.append(", notionalSpeed: ");
		result.append(notionalSpeed);
		result.append(')');
		return result.toString();
	}

} // end of RedirectionPurchaseContractImpl

// finish type fixing
