/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.impl;
import com.mmxlabs.models.lng.pricing.Index;

import com.mmxlabs.models.lng.spotmarkets.CharterCostModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;

import com.mmxlabs.models.lng.fleet.VesselClass;

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
 * An implementation of the model object '<em><b>Charter Cost Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterCostModelImpl#getVesselClasses <em>Vessel Classes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterCostModelImpl#getCharterInPrice <em>Charter In Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterCostModelImpl#getCharterOutPrice <em>Charter Out Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterCostModelImpl#getSpotCharterCount <em>Spot Charter Count</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterCostModelImpl#getMinCharterOutDuration <em>Min Charter Out Duration</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CharterCostModelImpl extends MMXObjectImpl implements CharterCostModel {
	/**
	 * The cached value of the '{@link #getVesselClasses() <em>Vessel Classes</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselClasses()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselClass> vesselClasses;

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
	 * The default value of the '{@link #getSpotCharterCount() <em>Spot Charter Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpotCharterCount()
	 * @generated
	 * @ordered
	 */
	protected static final int SPOT_CHARTER_COUNT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSpotCharterCount() <em>Spot Charter Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpotCharterCount()
	 * @generated
	 * @ordered
	 */
	protected int spotCharterCount = SPOT_CHARTER_COUNT_EDEFAULT;

	/**
	 * The default value of the '{@link #getMinCharterOutDuration() <em>Min Charter Out Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinCharterOutDuration()
	 * @generated
	 * @ordered
	 */
	protected static final int MIN_CHARTER_OUT_DURATION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMinCharterOutDuration() <em>Min Charter Out Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinCharterOutDuration()
	 * @generated
	 * @ordered
	 */
	protected int minCharterOutDuration = MIN_CHARTER_OUT_DURATION_EDEFAULT;

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
		return SpotMarketsPackage.Literals.CHARTER_COST_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<VesselClass> getVesselClasses() {
		if (vesselClasses == null) {
			vesselClasses = new EObjectResolvingEList<VesselClass>(VesselClass.class, this, SpotMarketsPackage.CHARTER_COST_MODEL__VESSEL_CLASSES);
		}
		return vesselClasses;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SpotMarketsPackage.CHARTER_COST_MODEL__CHARTER_IN_PRICE, oldCharterInPrice, charterInPrice));
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
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_COST_MODEL__CHARTER_IN_PRICE, oldCharterInPrice, charterInPrice));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SpotMarketsPackage.CHARTER_COST_MODEL__CHARTER_OUT_PRICE, oldCharterOutPrice, charterOutPrice));
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
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_COST_MODEL__CHARTER_OUT_PRICE, oldCharterOutPrice, charterOutPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getSpotCharterCount() {
		return spotCharterCount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSpotCharterCount(int newSpotCharterCount) {
		int oldSpotCharterCount = spotCharterCount;
		spotCharterCount = newSpotCharterCount;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_COST_MODEL__SPOT_CHARTER_COUNT, oldSpotCharterCount, spotCharterCount));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMinCharterOutDuration() {
		return minCharterOutDuration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinCharterOutDuration(int newMinCharterOutDuration) {
		int oldMinCharterOutDuration = minCharterOutDuration;
		minCharterOutDuration = newMinCharterOutDuration;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_COST_MODEL__MIN_CHARTER_OUT_DURATION, oldMinCharterOutDuration, minCharterOutDuration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SpotMarketsPackage.CHARTER_COST_MODEL__VESSEL_CLASSES:
				return getVesselClasses();
			case SpotMarketsPackage.CHARTER_COST_MODEL__CHARTER_IN_PRICE:
				if (resolve) return getCharterInPrice();
				return basicGetCharterInPrice();
			case SpotMarketsPackage.CHARTER_COST_MODEL__CHARTER_OUT_PRICE:
				if (resolve) return getCharterOutPrice();
				return basicGetCharterOutPrice();
			case SpotMarketsPackage.CHARTER_COST_MODEL__SPOT_CHARTER_COUNT:
				return getSpotCharterCount();
			case SpotMarketsPackage.CHARTER_COST_MODEL__MIN_CHARTER_OUT_DURATION:
				return getMinCharterOutDuration();
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
			case SpotMarketsPackage.CHARTER_COST_MODEL__VESSEL_CLASSES:
				getVesselClasses().clear();
				getVesselClasses().addAll((Collection<? extends VesselClass>)newValue);
				return;
			case SpotMarketsPackage.CHARTER_COST_MODEL__CHARTER_IN_PRICE:
				setCharterInPrice((Index<Integer>)newValue);
				return;
			case SpotMarketsPackage.CHARTER_COST_MODEL__CHARTER_OUT_PRICE:
				setCharterOutPrice((Index<Integer>)newValue);
				return;
			case SpotMarketsPackage.CHARTER_COST_MODEL__SPOT_CHARTER_COUNT:
				setSpotCharterCount((Integer)newValue);
				return;
			case SpotMarketsPackage.CHARTER_COST_MODEL__MIN_CHARTER_OUT_DURATION:
				setMinCharterOutDuration((Integer)newValue);
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
			case SpotMarketsPackage.CHARTER_COST_MODEL__VESSEL_CLASSES:
				getVesselClasses().clear();
				return;
			case SpotMarketsPackage.CHARTER_COST_MODEL__CHARTER_IN_PRICE:
				setCharterInPrice((Index<Integer>)null);
				return;
			case SpotMarketsPackage.CHARTER_COST_MODEL__CHARTER_OUT_PRICE:
				setCharterOutPrice((Index<Integer>)null);
				return;
			case SpotMarketsPackage.CHARTER_COST_MODEL__SPOT_CHARTER_COUNT:
				setSpotCharterCount(SPOT_CHARTER_COUNT_EDEFAULT);
				return;
			case SpotMarketsPackage.CHARTER_COST_MODEL__MIN_CHARTER_OUT_DURATION:
				setMinCharterOutDuration(MIN_CHARTER_OUT_DURATION_EDEFAULT);
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
			case SpotMarketsPackage.CHARTER_COST_MODEL__VESSEL_CLASSES:
				return vesselClasses != null && !vesselClasses.isEmpty();
			case SpotMarketsPackage.CHARTER_COST_MODEL__CHARTER_IN_PRICE:
				return charterInPrice != null;
			case SpotMarketsPackage.CHARTER_COST_MODEL__CHARTER_OUT_PRICE:
				return charterOutPrice != null;
			case SpotMarketsPackage.CHARTER_COST_MODEL__SPOT_CHARTER_COUNT:
				return spotCharterCount != SPOT_CHARTER_COUNT_EDEFAULT;
			case SpotMarketsPackage.CHARTER_COST_MODEL__MIN_CHARTER_OUT_DURATION:
				return minCharterOutDuration != MIN_CHARTER_OUT_DURATION_EDEFAULT;
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
		result.append(" (spotCharterCount: ");
		result.append(spotCharterCount);
		result.append(", minCharterOutDuration: ");
		result.append(minCharterOutDuration);
		result.append(')');
		return result.toString();
	}

} // end of CharterCostModelImpl

// finish type fixing
