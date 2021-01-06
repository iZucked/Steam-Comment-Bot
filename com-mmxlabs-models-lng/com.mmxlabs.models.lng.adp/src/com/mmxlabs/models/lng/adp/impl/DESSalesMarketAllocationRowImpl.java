/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.DESSalesMarketAllocationRow;

import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>DES Sales Market Allocation Row</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.DESSalesMarketAllocationRowImpl#getDesSalesMarket <em>Des Sales Market</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DESSalesMarketAllocationRowImpl extends MullAllocationRowImpl implements DESSalesMarketAllocationRow {
	/**
	 * The cached value of the '{@link #getDesSalesMarket() <em>Des Sales Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDesSalesMarket()
	 * @generated
	 * @ordered
	 */
	protected DESSalesMarket desSalesMarket;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DESSalesMarketAllocationRowImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.DES_SALES_MARKET_ALLOCATION_ROW;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DESSalesMarket getDesSalesMarket() {
		if (desSalesMarket != null && desSalesMarket.eIsProxy()) {
			InternalEObject oldDesSalesMarket = (InternalEObject)desSalesMarket;
			desSalesMarket = (DESSalesMarket)eResolveProxy(oldDesSalesMarket);
			if (desSalesMarket != oldDesSalesMarket) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.DES_SALES_MARKET_ALLOCATION_ROW__DES_SALES_MARKET, oldDesSalesMarket, desSalesMarket));
			}
		}
		return desSalesMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DESSalesMarket basicGetDesSalesMarket() {
		return desSalesMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDesSalesMarket(DESSalesMarket newDesSalesMarket) {
		DESSalesMarket oldDesSalesMarket = desSalesMarket;
		desSalesMarket = newDesSalesMarket;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.DES_SALES_MARKET_ALLOCATION_ROW__DES_SALES_MARKET, oldDesSalesMarket, desSalesMarket));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ADPPackage.DES_SALES_MARKET_ALLOCATION_ROW__DES_SALES_MARKET:
				if (resolve) return getDesSalesMarket();
				return basicGetDesSalesMarket();
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
			case ADPPackage.DES_SALES_MARKET_ALLOCATION_ROW__DES_SALES_MARKET:
				setDesSalesMarket((DESSalesMarket)newValue);
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
			case ADPPackage.DES_SALES_MARKET_ALLOCATION_ROW__DES_SALES_MARKET:
				setDesSalesMarket((DESSalesMarket)null);
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
			case ADPPackage.DES_SALES_MARKET_ALLOCATION_ROW__DES_SALES_MARKET:
				return desSalesMarket != null;
		}
		return super.eIsSet(featureID);
	}

} //DESSalesMarketAllocationRowImpl
