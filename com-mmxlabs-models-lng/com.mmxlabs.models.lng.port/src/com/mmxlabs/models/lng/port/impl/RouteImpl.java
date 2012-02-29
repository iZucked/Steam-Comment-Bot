/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.impl;

import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;

import com.mmxlabs.models.lng.types.impl.ARouteImpl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Route</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.RouteImpl#getLines <em>Lines</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.RouteImpl#isCanal <em>Canal</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.RouteImpl#getAdditionalTravelTime <em>Additional Travel Time</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RouteImpl extends ARouteImpl implements Route {
	/**
	 * The cached value of the '{@link #getLines() <em>Lines</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLines()
	 * @generated
	 * @ordered
	 */
	protected EList<RouteLine> lines;

	/**
	 * The default value of the '{@link #isCanal() <em>Canal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isCanal()
	 * @generated
	 * @ordered
	 */
	protected static final boolean CANAL_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isCanal() <em>Canal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isCanal()
	 * @generated
	 * @ordered
	 */
	protected boolean canal = CANAL_EDEFAULT;

	/**
	 * The default value of the '{@link #getAdditionalTravelTime() <em>Additional Travel Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAdditionalTravelTime()
	 * @generated
	 * @ordered
	 */
	protected static final int ADDITIONAL_TRAVEL_TIME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getAdditionalTravelTime() <em>Additional Travel Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAdditionalTravelTime()
	 * @generated
	 * @ordered
	 */
	protected int additionalTravelTime = ADDITIONAL_TRAVEL_TIME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RouteImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PortPackage.Literals.ROUTE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<RouteLine> getLines() {
		if (lines == null) {
			lines = new EObjectContainmentEList<RouteLine>(RouteLine.class, this, PortPackage.ROUTE__LINES);
		}
		return lines;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isCanal() {
		return canal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCanal(boolean newCanal) {
		boolean oldCanal = canal;
		canal = newCanal;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.ROUTE__CANAL, oldCanal, canal));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getAdditionalTravelTime() {
		return additionalTravelTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAdditionalTravelTime(int newAdditionalTravelTime) {
		int oldAdditionalTravelTime = additionalTravelTime;
		additionalTravelTime = newAdditionalTravelTime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.ROUTE__ADDITIONAL_TRAVEL_TIME, oldAdditionalTravelTime, additionalTravelTime));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PortPackage.ROUTE__LINES:
				return ((InternalEList<?>)getLines()).basicRemove(otherEnd, msgs);
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
			case PortPackage.ROUTE__LINES:
				return getLines();
			case PortPackage.ROUTE__CANAL:
				return isCanal();
			case PortPackage.ROUTE__ADDITIONAL_TRAVEL_TIME:
				return getAdditionalTravelTime();
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
			case PortPackage.ROUTE__LINES:
				getLines().clear();
				getLines().addAll((Collection<? extends RouteLine>)newValue);
				return;
			case PortPackage.ROUTE__CANAL:
				setCanal((Boolean)newValue);
				return;
			case PortPackage.ROUTE__ADDITIONAL_TRAVEL_TIME:
				setAdditionalTravelTime((Integer)newValue);
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
			case PortPackage.ROUTE__LINES:
				getLines().clear();
				return;
			case PortPackage.ROUTE__CANAL:
				setCanal(CANAL_EDEFAULT);
				return;
			case PortPackage.ROUTE__ADDITIONAL_TRAVEL_TIME:
				setAdditionalTravelTime(ADDITIONAL_TRAVEL_TIME_EDEFAULT);
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
			case PortPackage.ROUTE__LINES:
				return lines != null && !lines.isEmpty();
			case PortPackage.ROUTE__CANAL:
				return canal != CANAL_EDEFAULT;
			case PortPackage.ROUTE__ADDITIONAL_TRAVEL_TIME:
				return additionalTravelTime != ADDITIONAL_TRAVEL_TIME_EDEFAULT;
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
		result.append(" (canal: ");
		result.append(canal);
		result.append(", additionalTravelTime: ");
		result.append(additionalTravelTime);
		result.append(')');
		return result.toString();
	}

} //RouteImpl
