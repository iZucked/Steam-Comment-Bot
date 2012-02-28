/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.SchedulePackage;

import com.mmxlabs.models.lng.port.Port;

import java.math.BigDecimal;

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
 * An implementation of the model object '<em><b>Journey</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.JourneyImpl#getFuels <em>Fuels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.JourneyImpl#getDestination <em>Destination</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.JourneyImpl#isLaden <em>Laden</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.JourneyImpl#getRoute <em>Route</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.JourneyImpl#getToll <em>Toll</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class JourneyImpl extends EventImpl implements Journey {
	/**
	 * The cached value of the '{@link #getFuels() <em>Fuels</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFuels()
	 * @generated
	 * @ordered
	 */
	protected EList<FuelQuantity> fuels;

	/**
	 * The cached value of the '{@link #getDestination() <em>Destination</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDestination()
	 * @generated
	 * @ordered
	 */
	protected Port destination;

	/**
	 * The default value of the '{@link #isLaden() <em>Laden</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLaden()
	 * @generated
	 * @ordered
	 */
	protected static final boolean LADEN_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isLaden() <em>Laden</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLaden()
	 * @generated
	 * @ordered
	 */
	protected boolean laden = LADEN_EDEFAULT;

	/**
	 * The default value of the '{@link #getRoute() <em>Route</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRoute()
	 * @generated
	 * @ordered
	 */
	protected static final String ROUTE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRoute() <em>Route</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRoute()
	 * @generated
	 * @ordered
	 */
	protected String route = ROUTE_EDEFAULT;

	/**
	 * The default value of the '{@link #getToll() <em>Toll</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getToll()
	 * @generated
	 * @ordered
	 */
	protected static final BigDecimal TOLL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getToll() <em>Toll</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getToll()
	 * @generated
	 * @ordered
	 */
	protected BigDecimal toll = TOLL_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected JourneyImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.JOURNEY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<FuelQuantity> getFuels() {
		if (fuels == null) {
			fuels = new EObjectContainmentEList<FuelQuantity>(FuelQuantity.class, this, SchedulePackage.JOURNEY__FUELS);
		}
		return fuels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port getDestination() {
		if (destination != null && destination.eIsProxy()) {
			InternalEObject oldDestination = (InternalEObject)destination;
			destination = (Port)eResolveProxy(oldDestination);
			if (destination != oldDestination) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.JOURNEY__DESTINATION, oldDestination, destination));
			}
		}
		return destination;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetDestination() {
		return destination;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDestination(Port newDestination) {
		Port oldDestination = destination;
		destination = newDestination;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.JOURNEY__DESTINATION, oldDestination, destination));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isLaden() {
		return laden;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLaden(boolean newLaden) {
		boolean oldLaden = laden;
		laden = newLaden;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.JOURNEY__LADEN, oldLaden, laden));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getRoute() {
		return route;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRoute(String newRoute) {
		String oldRoute = route;
		route = newRoute;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.JOURNEY__ROUTE, oldRoute, route));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BigDecimal getToll() {
		return toll;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setToll(BigDecimal newToll) {
		BigDecimal oldToll = toll;
		toll = newToll;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.JOURNEY__TOLL, oldToll, toll));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.JOURNEY__FUELS:
				return ((InternalEList<?>)getFuels()).basicRemove(otherEnd, msgs);
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
			case SchedulePackage.JOURNEY__FUELS:
				return getFuels();
			case SchedulePackage.JOURNEY__DESTINATION:
				if (resolve) return getDestination();
				return basicGetDestination();
			case SchedulePackage.JOURNEY__LADEN:
				return isLaden();
			case SchedulePackage.JOURNEY__ROUTE:
				return getRoute();
			case SchedulePackage.JOURNEY__TOLL:
				return getToll();
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
			case SchedulePackage.JOURNEY__FUELS:
				getFuels().clear();
				getFuels().addAll((Collection<? extends FuelQuantity>)newValue);
				return;
			case SchedulePackage.JOURNEY__DESTINATION:
				setDestination((Port)newValue);
				return;
			case SchedulePackage.JOURNEY__LADEN:
				setLaden((Boolean)newValue);
				return;
			case SchedulePackage.JOURNEY__ROUTE:
				setRoute((String)newValue);
				return;
			case SchedulePackage.JOURNEY__TOLL:
				setToll((BigDecimal)newValue);
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
			case SchedulePackage.JOURNEY__FUELS:
				getFuels().clear();
				return;
			case SchedulePackage.JOURNEY__DESTINATION:
				setDestination((Port)null);
				return;
			case SchedulePackage.JOURNEY__LADEN:
				setLaden(LADEN_EDEFAULT);
				return;
			case SchedulePackage.JOURNEY__ROUTE:
				setRoute(ROUTE_EDEFAULT);
				return;
			case SchedulePackage.JOURNEY__TOLL:
				setToll(TOLL_EDEFAULT);
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
			case SchedulePackage.JOURNEY__FUELS:
				return fuels != null && !fuels.isEmpty();
			case SchedulePackage.JOURNEY__DESTINATION:
				return destination != null;
			case SchedulePackage.JOURNEY__LADEN:
				return laden != LADEN_EDEFAULT;
			case SchedulePackage.JOURNEY__ROUTE:
				return ROUTE_EDEFAULT == null ? route != null : !ROUTE_EDEFAULT.equals(route);
			case SchedulePackage.JOURNEY__TOLL:
				return TOLL_EDEFAULT == null ? toll != null : !TOLL_EDEFAULT.equals(toll);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == FuelUsage.class) {
			switch (derivedFeatureID) {
				case SchedulePackage.JOURNEY__FUELS: return SchedulePackage.FUEL_USAGE__FUELS;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == FuelUsage.class) {
			switch (baseFeatureID) {
				case SchedulePackage.FUEL_USAGE__FUELS: return SchedulePackage.JOURNEY__FUELS;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
		result.append(" (laden: ");
		result.append(laden);
		result.append(", route: ");
		result.append(route);
		result.append(", toll: ");
		result.append(toll);
		result.append(')');
		return result.toString();
	}

} // end of JourneyImpl

// finish type fixing
