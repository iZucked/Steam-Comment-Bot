/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.FleetCostModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Fleet Cost Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.FleetCostModelImpl#getBaseFuelPrices <em>Base Fuel Prices</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FleetCostModelImpl extends MMXObjectImpl implements FleetCostModel {
	/**
	 * The cached value of the '{@link #getBaseFuelPrices() <em>Base Fuel Prices</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseFuelPrices()
	 * @generated
	 * @ordered
	 */
	protected EList<BaseFuelCost> baseFuelPrices;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FleetCostModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.FLEET_COST_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<BaseFuelCost> getBaseFuelPrices() {
		if (baseFuelPrices == null) {
			baseFuelPrices = new EObjectContainmentEList<BaseFuelCost>(BaseFuelCost.class, this, PricingPackage.FLEET_COST_MODEL__BASE_FUEL_PRICES);
		}
		return baseFuelPrices;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PricingPackage.FLEET_COST_MODEL__BASE_FUEL_PRICES:
				return ((InternalEList<?>)getBaseFuelPrices()).basicRemove(otherEnd, msgs);
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
			case PricingPackage.FLEET_COST_MODEL__BASE_FUEL_PRICES:
				return getBaseFuelPrices();
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
			case PricingPackage.FLEET_COST_MODEL__BASE_FUEL_PRICES:
				getBaseFuelPrices().clear();
				getBaseFuelPrices().addAll((Collection<? extends BaseFuelCost>)newValue);
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
			case PricingPackage.FLEET_COST_MODEL__BASE_FUEL_PRICES:
				getBaseFuelPrices().clear();
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
			case PricingPackage.FLEET_COST_MODEL__BASE_FUEL_PRICES:
				return baseFuelPrices != null && !baseFuelPrices.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // end of FleetCostModelImpl

// finish type fixing
