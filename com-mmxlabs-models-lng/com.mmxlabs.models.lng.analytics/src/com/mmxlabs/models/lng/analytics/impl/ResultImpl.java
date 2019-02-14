/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.Result;
import com.mmxlabs.models.lng.analytics.ResultSet;

import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.Slot;

import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Result</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ResultImpl#getExtraSlots <em>Extra Slots</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ResultImpl#getResultSets <em>Result Sets</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ResultImpl#getExtraVesselAvailabilities <em>Extra Vessel Availabilities</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ResultImpl#getCharterInMarketOverrides <em>Charter In Market Overrides</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ResultImpl#getExtraCharterInMarkets <em>Extra Charter In Markets</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ResultImpl extends EObjectImpl implements Result {
	/**
	 * The cached value of the '{@link #getExtraSlots() <em>Extra Slots</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtraSlots()
	 * @generated
	 * @ordered
	 */
	protected EList<Slot> extraSlots;

	/**
	 * The cached value of the '{@link #getResultSets() <em>Result Sets</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResultSets()
	 * @generated
	 * @ordered
	 */
	protected EList<ResultSet> resultSets;

	/**
	 * The cached value of the '{@link #getExtraVesselAvailabilities() <em>Extra Vessel Availabilities</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtraVesselAvailabilities()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselAvailability> extraVesselAvailabilities;

	/**
	 * The cached value of the '{@link #getCharterInMarketOverrides() <em>Charter In Market Overrides</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterInMarketOverrides()
	 * @generated
	 * @ordered
	 */
	protected EList<CharterInMarketOverride> charterInMarketOverrides;

	/**
	 * The cached value of the '{@link #getExtraCharterInMarkets() <em>Extra Charter In Markets</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtraCharterInMarkets()
	 * @generated
	 * @ordered
	 */
	protected EList<CharterInMarket> extraCharterInMarkets;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ResultImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.RESULT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Slot> getExtraSlots() {
		if (extraSlots == null) {
			extraSlots = new EObjectContainmentEList<Slot>(Slot.class, this, AnalyticsPackage.RESULT__EXTRA_SLOTS);
		}
		return extraSlots;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ResultSet> getResultSets() {
		if (resultSets == null) {
			resultSets = new EObjectContainmentEList<ResultSet>(ResultSet.class, this, AnalyticsPackage.RESULT__RESULT_SETS);
		}
		return resultSets;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<VesselAvailability> getExtraVesselAvailabilities() {
		if (extraVesselAvailabilities == null) {
			extraVesselAvailabilities = new EObjectContainmentEList<VesselAvailability>(VesselAvailability.class, this, AnalyticsPackage.RESULT__EXTRA_VESSEL_AVAILABILITIES);
		}
		return extraVesselAvailabilities;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<CharterInMarketOverride> getCharterInMarketOverrides() {
		if (charterInMarketOverrides == null) {
			charterInMarketOverrides = new EObjectContainmentEList<CharterInMarketOverride>(CharterInMarketOverride.class, this, AnalyticsPackage.RESULT__CHARTER_IN_MARKET_OVERRIDES);
		}
		return charterInMarketOverrides;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<CharterInMarket> getExtraCharterInMarkets() {
		if (extraCharterInMarkets == null) {
			extraCharterInMarkets = new EObjectContainmentEList<CharterInMarket>(CharterInMarket.class, this, AnalyticsPackage.RESULT__EXTRA_CHARTER_IN_MARKETS);
		}
		return extraCharterInMarkets;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.RESULT__EXTRA_SLOTS:
				return ((InternalEList<?>)getExtraSlots()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.RESULT__RESULT_SETS:
				return ((InternalEList<?>)getResultSets()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.RESULT__EXTRA_VESSEL_AVAILABILITIES:
				return ((InternalEList<?>)getExtraVesselAvailabilities()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.RESULT__CHARTER_IN_MARKET_OVERRIDES:
				return ((InternalEList<?>)getCharterInMarketOverrides()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.RESULT__EXTRA_CHARTER_IN_MARKETS:
				return ((InternalEList<?>)getExtraCharterInMarkets()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AnalyticsPackage.RESULT__EXTRA_SLOTS:
				return getExtraSlots();
			case AnalyticsPackage.RESULT__RESULT_SETS:
				return getResultSets();
			case AnalyticsPackage.RESULT__EXTRA_VESSEL_AVAILABILITIES:
				return getExtraVesselAvailabilities();
			case AnalyticsPackage.RESULT__CHARTER_IN_MARKET_OVERRIDES:
				return getCharterInMarketOverrides();
			case AnalyticsPackage.RESULT__EXTRA_CHARTER_IN_MARKETS:
				return getExtraCharterInMarkets();
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
			case AnalyticsPackage.RESULT__EXTRA_SLOTS:
				getExtraSlots().clear();
				getExtraSlots().addAll((Collection<? extends Slot>)newValue);
				return;
			case AnalyticsPackage.RESULT__RESULT_SETS:
				getResultSets().clear();
				getResultSets().addAll((Collection<? extends ResultSet>)newValue);
				return;
			case AnalyticsPackage.RESULT__EXTRA_VESSEL_AVAILABILITIES:
				getExtraVesselAvailabilities().clear();
				getExtraVesselAvailabilities().addAll((Collection<? extends VesselAvailability>)newValue);
				return;
			case AnalyticsPackage.RESULT__CHARTER_IN_MARKET_OVERRIDES:
				getCharterInMarketOverrides().clear();
				getCharterInMarketOverrides().addAll((Collection<? extends CharterInMarketOverride>)newValue);
				return;
			case AnalyticsPackage.RESULT__EXTRA_CHARTER_IN_MARKETS:
				getExtraCharterInMarkets().clear();
				getExtraCharterInMarkets().addAll((Collection<? extends CharterInMarket>)newValue);
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
			case AnalyticsPackage.RESULT__EXTRA_SLOTS:
				getExtraSlots().clear();
				return;
			case AnalyticsPackage.RESULT__RESULT_SETS:
				getResultSets().clear();
				return;
			case AnalyticsPackage.RESULT__EXTRA_VESSEL_AVAILABILITIES:
				getExtraVesselAvailabilities().clear();
				return;
			case AnalyticsPackage.RESULT__CHARTER_IN_MARKET_OVERRIDES:
				getCharterInMarketOverrides().clear();
				return;
			case AnalyticsPackage.RESULT__EXTRA_CHARTER_IN_MARKETS:
				getExtraCharterInMarkets().clear();
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
			case AnalyticsPackage.RESULT__EXTRA_SLOTS:
				return extraSlots != null && !extraSlots.isEmpty();
			case AnalyticsPackage.RESULT__RESULT_SETS:
				return resultSets != null && !resultSets.isEmpty();
			case AnalyticsPackage.RESULT__EXTRA_VESSEL_AVAILABILITIES:
				return extraVesselAvailabilities != null && !extraVesselAvailabilities.isEmpty();
			case AnalyticsPackage.RESULT__CHARTER_IN_MARKET_OVERRIDES:
				return charterInMarketOverrides != null && !charterInMarketOverrides.isEmpty();
			case AnalyticsPackage.RESULT__EXTRA_CHARTER_IN_MARKETS:
				return extraCharterInMarkets != null && !extraCharterInMarkets.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ResultImpl
