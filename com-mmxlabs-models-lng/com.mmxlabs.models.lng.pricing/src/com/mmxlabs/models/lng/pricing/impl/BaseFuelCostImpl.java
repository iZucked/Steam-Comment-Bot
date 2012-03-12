/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.impl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Base Fuel Cost</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.BaseFuelCostImpl#getFuel <em>Fuel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.BaseFuelCostImpl#getPrice <em>Price</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BaseFuelCostImpl extends MMXObjectImpl implements BaseFuelCost {
	/**
	 * The cached value of the '{@link #getFuel() <em>Fuel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFuel()
	 * @generated
	 * @ordered
	 */
	protected BaseFuel fuel;

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
	public BaseFuel getFuel() {
		if (fuel != null && fuel.eIsProxy()) {
			InternalEObject oldFuel = (InternalEObject)fuel;
			fuel = (BaseFuel)eResolveProxy(oldFuel);
			if (fuel != oldFuel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PricingPackage.BASE_FUEL_COST__FUEL, oldFuel, fuel));
			}
		}
		return fuel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseFuel basicGetFuel() {
		return fuel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFuel(BaseFuel newFuel) {
		BaseFuel oldFuel = fuel;
		fuel = newFuel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.BASE_FUEL_COST__FUEL, oldFuel, fuel));
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
			case PricingPackage.BASE_FUEL_COST__FUEL:
				if (resolve) return getFuel();
				return basicGetFuel();
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
			case PricingPackage.BASE_FUEL_COST__FUEL:
				setFuel((BaseFuel)newValue);
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
			case PricingPackage.BASE_FUEL_COST__FUEL:
				setFuel((BaseFuel)null);
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
			case PricingPackage.BASE_FUEL_COST__FUEL:
				return fuel != null;
			case PricingPackage.BASE_FUEL_COST__PRICE:
				return price != null;
		}
		return super.eIsSet(featureID);
	}

} // end of BaseFuelCostImpl

// finish type fixing
