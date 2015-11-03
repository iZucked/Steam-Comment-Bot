/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing.impl;

import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.RouteCost;

import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cost Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.CostModelImpl#getRouteCosts <em>Route Costs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.CostModelImpl#getPortCosts <em>Port Costs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.CostModelImpl#getCooldownCosts <em>Cooldown Costs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.CostModelImpl#getBaseFuelCosts <em>Base Fuel Costs</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CostModelImpl extends UUIDObjectImpl implements CostModel {
	/**
	 * The cached value of the '{@link #getRouteCosts() <em>Route Costs</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRouteCosts()
	 * @generated
	 * @ordered
	 */
	protected EList<RouteCost> routeCosts;

	/**
	 * The cached value of the '{@link #getPortCosts() <em>Port Costs</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortCosts()
	 * @generated
	 * @ordered
	 */
	protected EList<PortCost> portCosts;

	/**
	 * The cached value of the '{@link #getCooldownCosts() <em>Cooldown Costs</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCooldownCosts()
	 * @generated
	 * @ordered
	 */
	protected EList<CooldownPrice> cooldownCosts;

	/**
	 * The cached value of the '{@link #getBaseFuelCosts() <em>Base Fuel Costs</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseFuelCosts()
	 * @generated
	 * @ordered
	 */
	protected EList<BaseFuelCost> baseFuelCosts;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CostModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.COST_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<RouteCost> getRouteCosts() {
		if (routeCosts == null) {
			routeCosts = new EObjectContainmentEList<RouteCost>(RouteCost.class, this, PricingPackage.COST_MODEL__ROUTE_COSTS);
		}
		return routeCosts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PortCost> getPortCosts() {
		if (portCosts == null) {
			portCosts = new EObjectContainmentEList<PortCost>(PortCost.class, this, PricingPackage.COST_MODEL__PORT_COSTS);
		}
		return portCosts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<CooldownPrice> getCooldownCosts() {
		if (cooldownCosts == null) {
			cooldownCosts = new EObjectContainmentEList<CooldownPrice>(CooldownPrice.class, this, PricingPackage.COST_MODEL__COOLDOWN_COSTS);
		}
		return cooldownCosts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<BaseFuelCost> getBaseFuelCosts() {
		if (baseFuelCosts == null) {
			baseFuelCosts = new EObjectContainmentEList<BaseFuelCost>(BaseFuelCost.class, this, PricingPackage.COST_MODEL__BASE_FUEL_COSTS);
		}
		return baseFuelCosts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PricingPackage.COST_MODEL__ROUTE_COSTS:
				return ((InternalEList<?>)getRouteCosts()).basicRemove(otherEnd, msgs);
			case PricingPackage.COST_MODEL__PORT_COSTS:
				return ((InternalEList<?>)getPortCosts()).basicRemove(otherEnd, msgs);
			case PricingPackage.COST_MODEL__COOLDOWN_COSTS:
				return ((InternalEList<?>)getCooldownCosts()).basicRemove(otherEnd, msgs);
			case PricingPackage.COST_MODEL__BASE_FUEL_COSTS:
				return ((InternalEList<?>)getBaseFuelCosts()).basicRemove(otherEnd, msgs);
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
			case PricingPackage.COST_MODEL__ROUTE_COSTS:
				return getRouteCosts();
			case PricingPackage.COST_MODEL__PORT_COSTS:
				return getPortCosts();
			case PricingPackage.COST_MODEL__COOLDOWN_COSTS:
				return getCooldownCosts();
			case PricingPackage.COST_MODEL__BASE_FUEL_COSTS:
				return getBaseFuelCosts();
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
			case PricingPackage.COST_MODEL__ROUTE_COSTS:
				getRouteCosts().clear();
				getRouteCosts().addAll((Collection<? extends RouteCost>)newValue);
				return;
			case PricingPackage.COST_MODEL__PORT_COSTS:
				getPortCosts().clear();
				getPortCosts().addAll((Collection<? extends PortCost>)newValue);
				return;
			case PricingPackage.COST_MODEL__COOLDOWN_COSTS:
				getCooldownCosts().clear();
				getCooldownCosts().addAll((Collection<? extends CooldownPrice>)newValue);
				return;
			case PricingPackage.COST_MODEL__BASE_FUEL_COSTS:
				getBaseFuelCosts().clear();
				getBaseFuelCosts().addAll((Collection<? extends BaseFuelCost>)newValue);
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
			case PricingPackage.COST_MODEL__ROUTE_COSTS:
				getRouteCosts().clear();
				return;
			case PricingPackage.COST_MODEL__PORT_COSTS:
				getPortCosts().clear();
				return;
			case PricingPackage.COST_MODEL__COOLDOWN_COSTS:
				getCooldownCosts().clear();
				return;
			case PricingPackage.COST_MODEL__BASE_FUEL_COSTS:
				getBaseFuelCosts().clear();
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
			case PricingPackage.COST_MODEL__ROUTE_COSTS:
				return routeCosts != null && !routeCosts.isEmpty();
			case PricingPackage.COST_MODEL__PORT_COSTS:
				return portCosts != null && !portCosts.isEmpty();
			case PricingPackage.COST_MODEL__COOLDOWN_COSTS:
				return cooldownCosts != null && !cooldownCosts.isEmpty();
			case PricingPackage.COST_MODEL__BASE_FUEL_COSTS:
				return baseFuelCosts != null && !baseFuelCosts.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //CostModelImpl
