/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.impl;
import com.mmxlabs.models.lng.pricing.CharterCostModel;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.PricingPackage;

import com.mmxlabs.models.lng.types.AVesselSet;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Charter Cost Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.CharterCostModelImpl#getVessels <em>Vessels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.CharterCostModelImpl#getCharterInPrice <em>Charter In Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.CharterCostModelImpl#getCharterOutPrice <em>Charter Out Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.CharterCostModelImpl#getSpotCharterCount <em>Spot Charter Count</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CharterCostModelImpl extends EObjectImpl implements CharterCostModel {
	/**
	 * The cached value of the '{@link #getVessels() <em>Vessels</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVessels()
	 * @generated
	 * @ordered
	 */
	protected EList<AVesselSet> vessels;

	/**
	 * The cached value of the '{@link #getCharterInPrice() <em>Charter In Price</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterInPrice()
	 * @generated
	 * @ordered
	 */
	protected Index<Integer> charterInPrice;

	/**
	 * The cached value of the '{@link #getCharterOutPrice() <em>Charter Out Price</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterOutPrice()
	 * @generated
	 * @ordered
	 */
	protected Index<Integer> charterOutPrice;

	/**
	 * The cached value of the '{@link #getSpotCharterCount() <em>Spot Charter Count</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpotCharterCount()
	 * @generated
	 * @ordered
	 */
	protected Index<Integer> spotCharterCount;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CharterCostModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.CHARTER_COST_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AVesselSet> getVessels() {
		if (vessels == null) {
			vessels = new EObjectResolvingEList<AVesselSet>(AVesselSet.class, this, PricingPackage.CHARTER_COST_MODEL__VESSELS);
		}
		return vessels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public Index<Integer> getCharterInPrice() {
		if (charterInPrice != null && charterInPrice.eIsProxy()) {
			InternalEObject oldCharterInPrice = (InternalEObject)charterInPrice;
			charterInPrice = (Index<Integer>)eResolveProxy(oldCharterInPrice);
			if (charterInPrice != oldCharterInPrice) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PricingPackage.CHARTER_COST_MODEL__CHARTER_IN_PRICE, oldCharterInPrice, charterInPrice));
			}
		}
		return charterInPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Index<Integer> basicGetCharterInPrice() {
		return charterInPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCharterInPrice(Index<Integer> newCharterInPrice) {
		Index<Integer> oldCharterInPrice = charterInPrice;
		charterInPrice = newCharterInPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.CHARTER_COST_MODEL__CHARTER_IN_PRICE, oldCharterInPrice, charterInPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public Index<Integer> getCharterOutPrice() {
		if (charterOutPrice != null && charterOutPrice.eIsProxy()) {
			InternalEObject oldCharterOutPrice = (InternalEObject)charterOutPrice;
			charterOutPrice = (Index<Integer>)eResolveProxy(oldCharterOutPrice);
			if (charterOutPrice != oldCharterOutPrice) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PricingPackage.CHARTER_COST_MODEL__CHARTER_OUT_PRICE, oldCharterOutPrice, charterOutPrice));
			}
		}
		return charterOutPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Index<Integer> basicGetCharterOutPrice() {
		return charterOutPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCharterOutPrice(Index<Integer> newCharterOutPrice) {
		Index<Integer> oldCharterOutPrice = charterOutPrice;
		charterOutPrice = newCharterOutPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.CHARTER_COST_MODEL__CHARTER_OUT_PRICE, oldCharterOutPrice, charterOutPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public Index<Integer> getSpotCharterCount() {
		if (spotCharterCount != null && spotCharterCount.eIsProxy()) {
			InternalEObject oldSpotCharterCount = (InternalEObject)spotCharterCount;
			spotCharterCount = (Index<Integer>)eResolveProxy(oldSpotCharterCount);
			if (spotCharterCount != oldSpotCharterCount) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PricingPackage.CHARTER_COST_MODEL__SPOT_CHARTER_COUNT, oldSpotCharterCount, spotCharterCount));
			}
		}
		return spotCharterCount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Index<Integer> basicGetSpotCharterCount() {
		return spotCharterCount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSpotCharterCount(Index<Integer> newSpotCharterCount) {
		Index<Integer> oldSpotCharterCount = spotCharterCount;
		spotCharterCount = newSpotCharterCount;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.CHARTER_COST_MODEL__SPOT_CHARTER_COUNT, oldSpotCharterCount, spotCharterCount));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PricingPackage.CHARTER_COST_MODEL__VESSELS:
				return getVessels();
			case PricingPackage.CHARTER_COST_MODEL__CHARTER_IN_PRICE:
				if (resolve) return getCharterInPrice();
				return basicGetCharterInPrice();
			case PricingPackage.CHARTER_COST_MODEL__CHARTER_OUT_PRICE:
				if (resolve) return getCharterOutPrice();
				return basicGetCharterOutPrice();
			case PricingPackage.CHARTER_COST_MODEL__SPOT_CHARTER_COUNT:
				if (resolve) return getSpotCharterCount();
				return basicGetSpotCharterCount();
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
			case PricingPackage.CHARTER_COST_MODEL__VESSELS:
				getVessels().clear();
				getVessels().addAll((Collection<? extends AVesselSet>)newValue);
				return;
			case PricingPackage.CHARTER_COST_MODEL__CHARTER_IN_PRICE:
				setCharterInPrice((Index<Integer>)newValue);
				return;
			case PricingPackage.CHARTER_COST_MODEL__CHARTER_OUT_PRICE:
				setCharterOutPrice((Index<Integer>)newValue);
				return;
			case PricingPackage.CHARTER_COST_MODEL__SPOT_CHARTER_COUNT:
				setSpotCharterCount((Index<Integer>)newValue);
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
			case PricingPackage.CHARTER_COST_MODEL__VESSELS:
				getVessels().clear();
				return;
			case PricingPackage.CHARTER_COST_MODEL__CHARTER_IN_PRICE:
				setCharterInPrice((Index<Integer>)null);
				return;
			case PricingPackage.CHARTER_COST_MODEL__CHARTER_OUT_PRICE:
				setCharterOutPrice((Index<Integer>)null);
				return;
			case PricingPackage.CHARTER_COST_MODEL__SPOT_CHARTER_COUNT:
				setSpotCharterCount((Index<Integer>)null);
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
			case PricingPackage.CHARTER_COST_MODEL__VESSELS:
				return vessels != null && !vessels.isEmpty();
			case PricingPackage.CHARTER_COST_MODEL__CHARTER_IN_PRICE:
				return charterInPrice != null;
			case PricingPackage.CHARTER_COST_MODEL__CHARTER_OUT_PRICE:
				return charterOutPrice != null;
			case PricingPackage.CHARTER_COST_MODEL__SPOT_CHARTER_COUNT:
				return spotCharterCount != null;
		}
		return super.eIsSet(featureID);
	}

} // end of CharterCostModelImpl

// finish type fixing
