/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.impl;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.RouteCost;

import com.mmxlabs.models.lng.pricing.RouteCostByVesselClass;
import com.mmxlabs.models.lng.port.Route;

import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Route Cost</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.RouteCostImpl#getRoute <em>Route</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.RouteCostImpl#getCostsByClass <em>Costs By Class</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RouteCostImpl extends MMXObjectImpl implements RouteCost {
	/**
	 * The cached value of the '{@link #getRoute() <em>Route</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRoute()
	 * @generated
	 * @ordered
	 */
	protected Route route;
	/**
	 * The cached value of the '{@link #getCostsByClass() <em>Costs By Class</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCostsByClass()
	 * @generated
	 * @ordered
	 */
	protected EList<RouteCostByVesselClass> costsByClass;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RouteCostImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.ROUTE_COST;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Route getRoute() {
		if (route != null && route.eIsProxy()) {
			InternalEObject oldRoute = (InternalEObject)route;
			route = (Route)eResolveProxy(oldRoute);
			if (route != oldRoute) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PricingPackage.ROUTE_COST__ROUTE, oldRoute, route));
			}
		}
		return route;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Route basicGetRoute() {
		return route;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRoute(Route newRoute) {
		Route oldRoute = route;
		route = newRoute;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.ROUTE_COST__ROUTE, oldRoute, route));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<RouteCostByVesselClass> getCostsByClass() {
		if (costsByClass == null) {
			costsByClass = new EObjectContainmentEList<RouteCostByVesselClass>(RouteCostByVesselClass.class, this, PricingPackage.ROUTE_COST__COSTS_BY_CLASS);
		}
		return costsByClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PricingPackage.ROUTE_COST__COSTS_BY_CLASS:
				return ((InternalEList<?>)getCostsByClass()).basicRemove(otherEnd, msgs);
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
			case PricingPackage.ROUTE_COST__ROUTE:
				if (resolve) return getRoute();
				return basicGetRoute();
			case PricingPackage.ROUTE_COST__COSTS_BY_CLASS:
				return getCostsByClass();
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
			case PricingPackage.ROUTE_COST__ROUTE:
				setRoute((Route)newValue);
				return;
			case PricingPackage.ROUTE_COST__COSTS_BY_CLASS:
				getCostsByClass().clear();
				getCostsByClass().addAll((Collection<? extends RouteCostByVesselClass>)newValue);
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
			case PricingPackage.ROUTE_COST__ROUTE:
				setRoute((Route)null);
				return;
			case PricingPackage.ROUTE_COST__COSTS_BY_CLASS:
				getCostsByClass().clear();
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
			case PricingPackage.ROUTE_COST__ROUTE:
				return route != null;
			case PricingPackage.ROUTE_COST__COSTS_BY_CLASS:
				return costsByClass != null && !costsByClass.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // end of RouteCostImpl

// finish type fixing
