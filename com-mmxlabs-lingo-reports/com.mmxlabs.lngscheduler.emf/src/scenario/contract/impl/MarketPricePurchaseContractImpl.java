/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.contract.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import scenario.contract.ContractPackage;
import scenario.contract.MarketPricePurchaseContract;

import scenario.market.Market;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Market Price Purchase Contract</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.contract.impl.MarketPricePurchaseContractImpl#getMarket <em>Market</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MarketPricePurchaseContractImpl extends PurchaseContractImpl implements MarketPricePurchaseContract {
	/**
	 * The cached value of the '{@link #getMarket() <em>Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarket()
	 * @generated
	 * @ordered
	 */
	protected Market market;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MarketPricePurchaseContractImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ContractPackage.Literals.MARKET_PRICE_PURCHASE_CONTRACT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Market getMarket() {
		if (market != null && market.eIsProxy()) {
			InternalEObject oldMarket = (InternalEObject)market;
			market = (Market)eResolveProxy(oldMarket);
			if (market != oldMarket) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ContractPackage.MARKET_PRICE_PURCHASE_CONTRACT__MARKET, oldMarket, market));
			}
		}
		return market;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Market basicGetMarket() {
		return market;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMarket(Market newMarket) {
		Market oldMarket = market;
		market = newMarket;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.MARKET_PRICE_PURCHASE_CONTRACT__MARKET, oldMarket, market));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ContractPackage.MARKET_PRICE_PURCHASE_CONTRACT__MARKET:
				if (resolve) return getMarket();
				return basicGetMarket();
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
			case ContractPackage.MARKET_PRICE_PURCHASE_CONTRACT__MARKET:
				setMarket((Market)newValue);
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
			case ContractPackage.MARKET_PRICE_PURCHASE_CONTRACT__MARKET:
				setMarket((Market)null);
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
			case ContractPackage.MARKET_PRICE_PURCHASE_CONTRACT__MARKET:
				return market != null;
		}
		return super.eIsSet(featureID);
	}

} //MarketPricePurchaseContractImpl
