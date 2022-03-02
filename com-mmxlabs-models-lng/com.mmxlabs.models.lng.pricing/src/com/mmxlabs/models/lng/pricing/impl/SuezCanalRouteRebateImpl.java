/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing.impl;

import com.mmxlabs.models.lng.port.Port;

import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.SuezCanalRouteRebate;

import com.mmxlabs.models.lng.types.APortSet;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Suez Canal Route Rebate</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SuezCanalRouteRebateImpl#getFrom <em>From</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SuezCanalRouteRebateImpl#getTo <em>To</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SuezCanalRouteRebateImpl#getRebate <em>Rebate</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SuezCanalRouteRebateImpl extends EObjectImpl implements SuezCanalRouteRebate {
	/**
	 * The cached value of the '{@link #getFrom() <em>From</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFrom()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet<Port>> from;

	/**
	 * The cached value of the '{@link #getTo() <em>To</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTo()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet<Port>> to;

	/**
	 * The default value of the '{@link #getRebate() <em>Rebate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRebate()
	 * @generated
	 * @ordered
	 */
	protected static final double REBATE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getRebate() <em>Rebate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRebate()
	 * @generated
	 * @ordered
	 */
	protected double rebate = REBATE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SuezCanalRouteRebateImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.SUEZ_CANAL_ROUTE_REBATE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<APortSet<Port>> getFrom() {
		if (from == null) {
			from = new EObjectResolvingEList<APortSet<Port>>(APortSet.class, this, PricingPackage.SUEZ_CANAL_ROUTE_REBATE__FROM);
		}
		return from;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<APortSet<Port>> getTo() {
		if (to == null) {
			to = new EObjectResolvingEList<APortSet<Port>>(APortSet.class, this, PricingPackage.SUEZ_CANAL_ROUTE_REBATE__TO);
		}
		return to;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getRebate() {
		return rebate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRebate(double newRebate) {
		double oldRebate = rebate;
		rebate = newRebate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SUEZ_CANAL_ROUTE_REBATE__REBATE, oldRebate, rebate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PricingPackage.SUEZ_CANAL_ROUTE_REBATE__FROM:
				return getFrom();
			case PricingPackage.SUEZ_CANAL_ROUTE_REBATE__TO:
				return getTo();
			case PricingPackage.SUEZ_CANAL_ROUTE_REBATE__REBATE:
				return getRebate();
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
			case PricingPackage.SUEZ_CANAL_ROUTE_REBATE__FROM:
				getFrom().clear();
				getFrom().addAll((Collection<? extends APortSet<Port>>)newValue);
				return;
			case PricingPackage.SUEZ_CANAL_ROUTE_REBATE__TO:
				getTo().clear();
				getTo().addAll((Collection<? extends APortSet<Port>>)newValue);
				return;
			case PricingPackage.SUEZ_CANAL_ROUTE_REBATE__REBATE:
				setRebate((Double)newValue);
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
			case PricingPackage.SUEZ_CANAL_ROUTE_REBATE__FROM:
				getFrom().clear();
				return;
			case PricingPackage.SUEZ_CANAL_ROUTE_REBATE__TO:
				getTo().clear();
				return;
			case PricingPackage.SUEZ_CANAL_ROUTE_REBATE__REBATE:
				setRebate(REBATE_EDEFAULT);
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
			case PricingPackage.SUEZ_CANAL_ROUTE_REBATE__FROM:
				return from != null && !from.isEmpty();
			case PricingPackage.SUEZ_CANAL_ROUTE_REBATE__TO:
				return to != null && !to.isEmpty();
			case PricingPackage.SUEZ_CANAL_ROUTE_REBATE__REBATE:
				return rebate != REBATE_EDEFAULT;
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
		result.append(" (rebate: ");
		result.append(rebate);
		result.append(')');
		return result.toString();
	}

} //SuezCanalRouteRebateImpl
