/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.impl;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PricingPackage;

import com.mmxlabs.models.lng.types.APortSet;
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
 * An implementation of the model object '<em><b>Port Cost</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PortCostImpl#getPorts <em>Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PortCostImpl#getVessels <em>Vessels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PortCostImpl#getLadenCost <em>Laden Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PortCostImpl#getBallastCost <em>Ballast Cost</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PortCostImpl extends EObjectImpl implements PortCost {
	/**
	 * The cached value of the '{@link #getPorts() <em>Ports</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet> ports;

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
	 * The cached value of the '{@link #getLadenCost() <em>Laden Cost</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLadenCost()
	 * @generated
	 * @ordered
	 */
	protected Index<Integer> ladenCost;

	/**
	 * The cached value of the '{@link #getBallastCost() <em>Ballast Cost</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastCost()
	 * @generated
	 * @ordered
	 */
	protected Index<Integer> ballastCost;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PortCostImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.PORT_COST;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<APortSet> getPorts() {
		if (ports == null) {
			ports = new EObjectResolvingEList<APortSet>(APortSet.class, this, PricingPackage.PORT_COST__PORTS);
		}
		return ports;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AVesselSet> getVessels() {
		if (vessels == null) {
			vessels = new EObjectResolvingEList<AVesselSet>(AVesselSet.class, this, PricingPackage.PORT_COST__VESSELS);
		}
		return vessels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public Index<Integer> getLadenCost() {
		if (ladenCost != null && ladenCost.eIsProxy()) {
			InternalEObject oldLadenCost = (InternalEObject)ladenCost;
			ladenCost = (Index<Integer>)eResolveProxy(oldLadenCost);
			if (ladenCost != oldLadenCost) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PricingPackage.PORT_COST__LADEN_COST, oldLadenCost, ladenCost));
			}
		}
		return ladenCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Index<Integer> basicGetLadenCost() {
		return ladenCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLadenCost(Index<Integer> newLadenCost) {
		Index<Integer> oldLadenCost = ladenCost;
		ladenCost = newLadenCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.PORT_COST__LADEN_COST, oldLadenCost, ladenCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public Index<Integer> getBallastCost() {
		if (ballastCost != null && ballastCost.eIsProxy()) {
			InternalEObject oldBallastCost = (InternalEObject)ballastCost;
			ballastCost = (Index<Integer>)eResolveProxy(oldBallastCost);
			if (ballastCost != oldBallastCost) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PricingPackage.PORT_COST__BALLAST_COST, oldBallastCost, ballastCost));
			}
		}
		return ballastCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Index<Integer> basicGetBallastCost() {
		return ballastCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBallastCost(Index<Integer> newBallastCost) {
		Index<Integer> oldBallastCost = ballastCost;
		ballastCost = newBallastCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.PORT_COST__BALLAST_COST, oldBallastCost, ballastCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PricingPackage.PORT_COST__PORTS:
				return getPorts();
			case PricingPackage.PORT_COST__VESSELS:
				return getVessels();
			case PricingPackage.PORT_COST__LADEN_COST:
				if (resolve) return getLadenCost();
				return basicGetLadenCost();
			case PricingPackage.PORT_COST__BALLAST_COST:
				if (resolve) return getBallastCost();
				return basicGetBallastCost();
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
			case PricingPackage.PORT_COST__PORTS:
				getPorts().clear();
				getPorts().addAll((Collection<? extends APortSet>)newValue);
				return;
			case PricingPackage.PORT_COST__VESSELS:
				getVessels().clear();
				getVessels().addAll((Collection<? extends AVesselSet>)newValue);
				return;
			case PricingPackage.PORT_COST__LADEN_COST:
				setLadenCost((Index<Integer>)newValue);
				return;
			case PricingPackage.PORT_COST__BALLAST_COST:
				setBallastCost((Index<Integer>)newValue);
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
			case PricingPackage.PORT_COST__PORTS:
				getPorts().clear();
				return;
			case PricingPackage.PORT_COST__VESSELS:
				getVessels().clear();
				return;
			case PricingPackage.PORT_COST__LADEN_COST:
				setLadenCost((Index<Integer>)null);
				return;
			case PricingPackage.PORT_COST__BALLAST_COST:
				setBallastCost((Index<Integer>)null);
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
			case PricingPackage.PORT_COST__PORTS:
				return ports != null && !ports.isEmpty();
			case PricingPackage.PORT_COST__VESSELS:
				return vessels != null && !vessels.isEmpty();
			case PricingPackage.PORT_COST__LADEN_COST:
				return ladenCost != null;
			case PricingPackage.PORT_COST__BALLAST_COST:
				return ballastCost != null;
		}
		return super.eIsSet(featureID);
	}

} // end of PortCostImpl

// finish type fixing
