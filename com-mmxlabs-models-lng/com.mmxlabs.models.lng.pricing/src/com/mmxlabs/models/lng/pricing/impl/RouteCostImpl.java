/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.impl;
import com.mmxlabs.models.lng.fleet.Vessel;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;
import java.util.Collection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Route Cost</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.RouteCostImpl#getRouteOption <em>Route Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.RouteCostImpl#getVessels <em>Vessels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.RouteCostImpl#getLadenCost <em>Laden Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.RouteCostImpl#getBallastCost <em>Ballast Cost</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RouteCostImpl extends MMXObjectImpl implements RouteCost {
	/**
	 * The default value of the '{@link #getRouteOption() <em>Route Option</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRouteOption()
	 * @generated
	 * @ordered
	 */
	protected static final RouteOption ROUTE_OPTION_EDEFAULT = RouteOption.DIRECT;
	/**
	 * The cached value of the '{@link #getRouteOption() <em>Route Option</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRouteOption()
	 * @generated
	 * @ordered
	 */
	protected RouteOption routeOption = ROUTE_OPTION_EDEFAULT;
	/**
	 * The cached value of the '{@link #getVessels() <em>Vessels</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVessels()
	 * @generated
	 * @ordered
	 */
	protected EList<AVesselSet<Vessel>> vessels;
	/**
	 * The default value of the '{@link #getLadenCost() <em>Laden Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLadenCost()
	 * @generated
	 * @ordered
	 */
	protected static final int LADEN_COST_EDEFAULT = 0;
	/**
	 * The cached value of the '{@link #getLadenCost() <em>Laden Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLadenCost()
	 * @generated
	 * @ordered
	 */
	protected int ladenCost = LADEN_COST_EDEFAULT;
	/**
	 * The default value of the '{@link #getBallastCost() <em>Ballast Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastCost()
	 * @generated
	 * @ordered
	 */
	protected static final int BALLAST_COST_EDEFAULT = 0;
	/**
	 * The cached value of the '{@link #getBallastCost() <em>Ballast Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastCost()
	 * @generated
	 * @ordered
	 */
	protected int ballastCost = BALLAST_COST_EDEFAULT;
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
	@Override
	public RouteOption getRouteOption() {
		return routeOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRouteOption(RouteOption newRouteOption) {
		RouteOption oldRouteOption = routeOption;
		routeOption = newRouteOption == null ? ROUTE_OPTION_EDEFAULT : newRouteOption;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.ROUTE_COST__ROUTE_OPTION, oldRouteOption, routeOption));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<AVesselSet<Vessel>> getVessels() {
		if (vessels == null) {
			vessels = new EObjectResolvingEList<AVesselSet<Vessel>>(AVesselSet.class, this, PricingPackage.ROUTE_COST__VESSELS);
		}
		return vessels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getLadenCost() {
		return ladenCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLadenCost(int newLadenCost) {
		int oldLadenCost = ladenCost;
		ladenCost = newLadenCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.ROUTE_COST__LADEN_COST, oldLadenCost, ladenCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getBallastCost() {
		return ballastCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBallastCost(int newBallastCost) {
		int oldBallastCost = ballastCost;
		ballastCost = newBallastCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.ROUTE_COST__BALLAST_COST, oldBallastCost, ballastCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PricingPackage.ROUTE_COST__ROUTE_OPTION:
				return getRouteOption();
			case PricingPackage.ROUTE_COST__VESSELS:
				return getVessels();
			case PricingPackage.ROUTE_COST__LADEN_COST:
				return getLadenCost();
			case PricingPackage.ROUTE_COST__BALLAST_COST:
				return getBallastCost();
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
			case PricingPackage.ROUTE_COST__ROUTE_OPTION:
				setRouteOption((RouteOption)newValue);
				return;
			case PricingPackage.ROUTE_COST__VESSELS:
				getVessels().clear();
				getVessels().addAll((Collection<? extends AVesselSet<Vessel>>)newValue);
				return;
			case PricingPackage.ROUTE_COST__LADEN_COST:
				setLadenCost((Integer)newValue);
				return;
			case PricingPackage.ROUTE_COST__BALLAST_COST:
				setBallastCost((Integer)newValue);
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
			case PricingPackage.ROUTE_COST__ROUTE_OPTION:
				setRouteOption(ROUTE_OPTION_EDEFAULT);
				return;
			case PricingPackage.ROUTE_COST__VESSELS:
				getVessels().clear();
				return;
			case PricingPackage.ROUTE_COST__LADEN_COST:
				setLadenCost(LADEN_COST_EDEFAULT);
				return;
			case PricingPackage.ROUTE_COST__BALLAST_COST:
				setBallastCost(BALLAST_COST_EDEFAULT);
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
			case PricingPackage.ROUTE_COST__ROUTE_OPTION:
				return routeOption != ROUTE_OPTION_EDEFAULT;
			case PricingPackage.ROUTE_COST__VESSELS:
				return vessels != null && !vessels.isEmpty();
			case PricingPackage.ROUTE_COST__LADEN_COST:
				return ladenCost != LADEN_COST_EDEFAULT;
			case PricingPackage.ROUTE_COST__BALLAST_COST:
				return ballastCost != BALLAST_COST_EDEFAULT;
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
		result.append(" (routeOption: ");
		result.append(routeOption);
		result.append(", ladenCost: ");
		result.append(ladenCost);
		result.append(", ballastCost: ");
		result.append(ballastCost);
		result.append(')');
		return result.toString();
	}

} // end of RouteCostImpl

// finish type fixing
