/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.impl;

import com.mmxlabs.models.lng.port.EntryPoint;
import com.mmxlabs.models.lng.port.Port;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.mmxcore.impl.NamedObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Route</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.RouteImpl#getUuid <em>Uuid</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.RouteImpl#getLines <em>Lines</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.RouteImpl#getRouteOption <em>Route Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.RouteImpl#getVirtualPort <em>Virtual Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.RouteImpl#getNorthEntrance <em>North Entrance</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.RouteImpl#getSouthEntrance <em>South Entrance</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.RouteImpl#getDistance <em>Distance</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RouteImpl extends NamedObjectImpl implements Route {
	/**
	 * The default value of the '{@link #getUuid() <em>Uuid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUuid()
	 * @generated
	 * @ordered
	 */
	protected static final String UUID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUuid() <em>Uuid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUuid()
	 * @generated
	 * @ordered
	 */
	protected String uuid = UUID_EDEFAULT;

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
	 * The cached value of the '{@link #getVirtualPort() <em>Virtual Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVirtualPort()
	 * @generated
	 * @ordered
	 */
	protected Port virtualPort;

	/**
	 * The cached value of the '{@link #getNorthEntrance() <em>North Entrance</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNorthEntrance()
	 * @generated
	 * @ordered
	 */
	protected EntryPoint northEntrance;

	/**
	 * The cached value of the '{@link #getSouthEntrance() <em>South Entrance</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSouthEntrance()
	 * @generated
	 * @ordered
	 */
	protected EntryPoint southEntrance;

	/**
	 * The default value of the '{@link #getDistance() <em>Distance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDistance()
	 * @generated
	 * @ordered
	 */
	protected static final double DISTANCE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getDistance() <em>Distance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDistance()
	 * @generated
	 * @ordered
	 */
	protected double distance = DISTANCE_EDEFAULT;

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
	public String getUuid() {
		return uuid;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUuid(String newUuid) {
		String oldUuid = uuid;
		uuid = newUuid;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.ROUTE__UUID, oldUuid, uuid));
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
	public RouteOption getRouteOption() {
		return routeOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRouteOption(RouteOption newRouteOption) {
		RouteOption oldRouteOption = routeOption;
		routeOption = newRouteOption == null ? ROUTE_OPTION_EDEFAULT : newRouteOption;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.ROUTE__ROUTE_OPTION, oldRouteOption, routeOption));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port getVirtualPort() {
		if (virtualPort != null && virtualPort.eIsProxy()) {
			InternalEObject oldVirtualPort = (InternalEObject)virtualPort;
			virtualPort = (Port)eResolveProxy(oldVirtualPort);
			if (virtualPort != oldVirtualPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PortPackage.ROUTE__VIRTUAL_PORT, oldVirtualPort, virtualPort));
			}
		}
		return virtualPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetVirtualPort() {
		return virtualPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVirtualPort(Port newVirtualPort) {
		Port oldVirtualPort = virtualPort;
		virtualPort = newVirtualPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.ROUTE__VIRTUAL_PORT, oldVirtualPort, virtualPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntryPoint getNorthEntrance() {
		return northEntrance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetNorthEntrance(EntryPoint newNorthEntrance, NotificationChain msgs) {
		EntryPoint oldNorthEntrance = northEntrance;
		northEntrance = newNorthEntrance;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PortPackage.ROUTE__NORTH_ENTRANCE, oldNorthEntrance, newNorthEntrance);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNorthEntrance(EntryPoint newNorthEntrance) {
		if (newNorthEntrance != northEntrance) {
			NotificationChain msgs = null;
			if (northEntrance != null)
				msgs = ((InternalEObject)northEntrance).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PortPackage.ROUTE__NORTH_ENTRANCE, null, msgs);
			if (newNorthEntrance != null)
				msgs = ((InternalEObject)newNorthEntrance).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PortPackage.ROUTE__NORTH_ENTRANCE, null, msgs);
			msgs = basicSetNorthEntrance(newNorthEntrance, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.ROUTE__NORTH_ENTRANCE, newNorthEntrance, newNorthEntrance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntryPoint getSouthEntrance() {
		return southEntrance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSouthEntrance(EntryPoint newSouthEntrance, NotificationChain msgs) {
		EntryPoint oldSouthEntrance = southEntrance;
		southEntrance = newSouthEntrance;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PortPackage.ROUTE__SOUTH_ENTRANCE, oldSouthEntrance, newSouthEntrance);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSouthEntrance(EntryPoint newSouthEntrance) {
		if (newSouthEntrance != southEntrance) {
			NotificationChain msgs = null;
			if (southEntrance != null)
				msgs = ((InternalEObject)southEntrance).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PortPackage.ROUTE__SOUTH_ENTRANCE, null, msgs);
			if (newSouthEntrance != null)
				msgs = ((InternalEObject)newSouthEntrance).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PortPackage.ROUTE__SOUTH_ENTRANCE, null, msgs);
			msgs = basicSetSouthEntrance(newSouthEntrance, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.ROUTE__SOUTH_ENTRANCE, newSouthEntrance, newSouthEntrance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDistance(double newDistance) {
		double oldDistance = distance;
		distance = newDistance;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.ROUTE__DISTANCE, oldDistance, distance));
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
			case PortPackage.ROUTE__NORTH_ENTRANCE:
				return basicSetNorthEntrance(null, msgs);
			case PortPackage.ROUTE__SOUTH_ENTRANCE:
				return basicSetSouthEntrance(null, msgs);
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
			case PortPackage.ROUTE__UUID:
				return getUuid();
			case PortPackage.ROUTE__LINES:
				return getLines();
			case PortPackage.ROUTE__ROUTE_OPTION:
				return getRouteOption();
			case PortPackage.ROUTE__VIRTUAL_PORT:
				if (resolve) return getVirtualPort();
				return basicGetVirtualPort();
			case PortPackage.ROUTE__NORTH_ENTRANCE:
				return getNorthEntrance();
			case PortPackage.ROUTE__SOUTH_ENTRANCE:
				return getSouthEntrance();
			case PortPackage.ROUTE__DISTANCE:
				return getDistance();
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
			case PortPackage.ROUTE__UUID:
				setUuid((String)newValue);
				return;
			case PortPackage.ROUTE__LINES:
				getLines().clear();
				getLines().addAll((Collection<? extends RouteLine>)newValue);
				return;
			case PortPackage.ROUTE__ROUTE_OPTION:
				setRouteOption((RouteOption)newValue);
				return;
			case PortPackage.ROUTE__VIRTUAL_PORT:
				setVirtualPort((Port)newValue);
				return;
			case PortPackage.ROUTE__NORTH_ENTRANCE:
				setNorthEntrance((EntryPoint)newValue);
				return;
			case PortPackage.ROUTE__SOUTH_ENTRANCE:
				setSouthEntrance((EntryPoint)newValue);
				return;
			case PortPackage.ROUTE__DISTANCE:
				setDistance((Double)newValue);
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
			case PortPackage.ROUTE__UUID:
				setUuid(UUID_EDEFAULT);
				return;
			case PortPackage.ROUTE__LINES:
				getLines().clear();
				return;
			case PortPackage.ROUTE__ROUTE_OPTION:
				setRouteOption(ROUTE_OPTION_EDEFAULT);
				return;
			case PortPackage.ROUTE__VIRTUAL_PORT:
				setVirtualPort((Port)null);
				return;
			case PortPackage.ROUTE__NORTH_ENTRANCE:
				setNorthEntrance((EntryPoint)null);
				return;
			case PortPackage.ROUTE__SOUTH_ENTRANCE:
				setSouthEntrance((EntryPoint)null);
				return;
			case PortPackage.ROUTE__DISTANCE:
				setDistance(DISTANCE_EDEFAULT);
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
			case PortPackage.ROUTE__UUID:
				return UUID_EDEFAULT == null ? uuid != null : !UUID_EDEFAULT.equals(uuid);
			case PortPackage.ROUTE__LINES:
				return lines != null && !lines.isEmpty();
			case PortPackage.ROUTE__ROUTE_OPTION:
				return routeOption != ROUTE_OPTION_EDEFAULT;
			case PortPackage.ROUTE__VIRTUAL_PORT:
				return virtualPort != null;
			case PortPackage.ROUTE__NORTH_ENTRANCE:
				return northEntrance != null;
			case PortPackage.ROUTE__SOUTH_ENTRANCE:
				return southEntrance != null;
			case PortPackage.ROUTE__DISTANCE:
				return distance != DISTANCE_EDEFAULT;
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
		if (baseClass == UUIDObject.class) {
			switch (derivedFeatureID) {
				case PortPackage.ROUTE__UUID: return MMXCorePackage.UUID_OBJECT__UUID;
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
		if (baseClass == UUIDObject.class) {
			switch (baseFeatureID) {
				case MMXCorePackage.UUID_OBJECT__UUID: return PortPackage.ROUTE__UUID;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

//		StringBuffer result = new StringBuffer(super.toString());
//		result.append(" (uuid: ");
//		result.append(uuid);
//		result.append(", canal: ");
//		result.append(canal);
//		result.append(", routingOptions: ");
//		result.append(routingOptions);
//		result.append(')');
//		return result.toString();
		
		return getName();
	}

} //RouteImpl
