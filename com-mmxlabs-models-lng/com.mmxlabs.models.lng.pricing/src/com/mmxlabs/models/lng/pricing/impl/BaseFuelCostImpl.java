/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.impl;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.PricingPackage;

import com.mmxlabs.models.lng.fleet.BaseFuel;

import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Base Fuel Cost</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.BaseFuelCostImpl#getFuels <em>Fuels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.BaseFuelCostImpl#getPrice <em>Price</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BaseFuelCostImpl extends MMXObjectImpl implements BaseFuelCost {
	/**
	 * The cached value of the '{@link #getFuels() <em>Fuels</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFuels()
	 * @generated
	 * @ordered
	 */
	protected EList<BaseFuel> fuels;

	/**
	 * The cached value of the '{@link #getPrice() <em>Price</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrice()
	 * @generated
	 * @ordered
	 */
	protected Index<Double> price;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BaseFuelCostImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.BASE_FUEL_COST;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<BaseFuel> getFuels() {
		if (fuels == null) {
			fuels = new EObjectResolvingEList<BaseFuel>(BaseFuel.class, this, PricingPackage.BASE_FUEL_COST__FUELS);
		}
		return fuels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public Index<Double> getPrice() {
		if (price != null && price.eIsProxy()) {
			InternalEObject oldPrice = (InternalEObject)price;
			price = (Index<Double>)eResolveProxy(oldPrice);
			if (price != oldPrice) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PricingPackage.BASE_FUEL_COST__PRICE, oldPrice, price));
			}
		}
		return price;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Index<Double> basicGetPrice() {
		return price;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPrice(Index<Double> newPrice) {
		Index<Double> oldPrice = price;
		price = newPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.BASE_FUEL_COST__PRICE, oldPrice, price));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PricingPackage.BASE_FUEL_COST__FUELS:
				return getFuels();
			case PricingPackage.BASE_FUEL_COST__PRICE:
				if (resolve) return getPrice();
				return basicGetPrice();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case PricingPackage.BASE_FUEL_COST__FUELS:
				getFuels().clear();
				getFuels().addAll((Collection<? extends BaseFuel>)newValue);
				return;
			case PricingPackage.BASE_FUEL_COST__PRICE:
				setPrice((Index<Double>)newValue);
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
			case PricingPackage.BASE_FUEL_COST__FUELS:
				getFuels().clear();
				return;
			case PricingPackage.BASE_FUEL_COST__PRICE:
				setPrice((Index<Double>)null);
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
			case PricingPackage.BASE_FUEL_COST__FUELS:
				return fuels != null && !fuels.isEmpty();
			case PricingPackage.BASE_FUEL_COST__PRICE:
				return price != null;
		}
		return super.eIsSet(featureID);
	}

} // end of BaseFuelCostImpl

// finish type fixing
