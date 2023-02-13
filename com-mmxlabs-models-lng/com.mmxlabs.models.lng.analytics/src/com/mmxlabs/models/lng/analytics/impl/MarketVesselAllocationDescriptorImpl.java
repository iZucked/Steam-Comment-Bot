/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.MarketVesselAllocationDescriptor;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Market Vessel Allocation Descriptor</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketVesselAllocationDescriptorImpl#getMarketName <em>Market Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketVesselAllocationDescriptorImpl#getSpotIndex <em>Spot Index</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MarketVesselAllocationDescriptorImpl extends VesselAllocationDescriptorImpl implements MarketVesselAllocationDescriptor {
	/**
	 * The default value of the '{@link #getMarketName() <em>Market Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarketName()
	 * @generated
	 * @ordered
	 */
	protected static final String MARKET_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMarketName() <em>Market Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarketName()
	 * @generated
	 * @ordered
	 */
	protected String marketName = MARKET_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getSpotIndex() <em>Spot Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpotIndex()
	 * @generated
	 * @ordered
	 */
	protected static final int SPOT_INDEX_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSpotIndex() <em>Spot Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpotIndex()
	 * @generated
	 * @ordered
	 */
	protected int spotIndex = SPOT_INDEX_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MarketVesselAllocationDescriptorImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.MARKET_VESSEL_ALLOCATION_DESCRIPTOR;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getMarketName() {
		return marketName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMarketName(String newMarketName) {
		String oldMarketName = marketName;
		marketName = newMarketName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MARKET_VESSEL_ALLOCATION_DESCRIPTOR__MARKET_NAME, oldMarketName, marketName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getSpotIndex() {
		return spotIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSpotIndex(int newSpotIndex) {
		int oldSpotIndex = spotIndex;
		spotIndex = newSpotIndex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MARKET_VESSEL_ALLOCATION_DESCRIPTOR__SPOT_INDEX, oldSpotIndex, spotIndex));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AnalyticsPackage.MARKET_VESSEL_ALLOCATION_DESCRIPTOR__MARKET_NAME:
				return getMarketName();
			case AnalyticsPackage.MARKET_VESSEL_ALLOCATION_DESCRIPTOR__SPOT_INDEX:
				return getSpotIndex();
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
			case AnalyticsPackage.MARKET_VESSEL_ALLOCATION_DESCRIPTOR__MARKET_NAME:
				setMarketName((String)newValue);
				return;
			case AnalyticsPackage.MARKET_VESSEL_ALLOCATION_DESCRIPTOR__SPOT_INDEX:
				setSpotIndex((Integer)newValue);
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
			case AnalyticsPackage.MARKET_VESSEL_ALLOCATION_DESCRIPTOR__MARKET_NAME:
				setMarketName(MARKET_NAME_EDEFAULT);
				return;
			case AnalyticsPackage.MARKET_VESSEL_ALLOCATION_DESCRIPTOR__SPOT_INDEX:
				setSpotIndex(SPOT_INDEX_EDEFAULT);
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
			case AnalyticsPackage.MARKET_VESSEL_ALLOCATION_DESCRIPTOR__MARKET_NAME:
				return MARKET_NAME_EDEFAULT == null ? marketName != null : !MARKET_NAME_EDEFAULT.equals(marketName);
			case AnalyticsPackage.MARKET_VESSEL_ALLOCATION_DESCRIPTOR__SPOT_INDEX:
				return spotIndex != SPOT_INDEX_EDEFAULT;
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

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (marketName: ");
		result.append(marketName);
		result.append(", spotIndex: ");
		result.append(spotIndex);
		result.append(')');
		return result.toString();
	}

} //MarketVesselAllocationDescriptorImpl
