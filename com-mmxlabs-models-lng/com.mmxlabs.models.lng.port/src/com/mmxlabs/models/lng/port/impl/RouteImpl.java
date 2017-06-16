/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
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
 *   <li>{@link com.mmxlabs.models.lng.port.impl.RouteImpl#isCanal <em>Canal</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.RouteImpl#getRoutingOptions <em>Routing Options</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.RouteImpl#getEntryA <em>Entry A</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.RouteImpl#getEntryB <em>Entry B</em>}</li>
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
	 * The cached value of the '{@link #getRoutingOptions() <em>Routing Options</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRoutingOptions()
	 * @generated
	 * @ordered
	 */
	protected EList<String> routingOptions;

	/**
	 * The cached value of the '{@link #getEntryA() <em>Entry A</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntryA()
	 * @generated
	 * @ordered
	 */
	protected EntryPoint entryA;

	/**
	 * The cached value of the '{@link #getEntryB() <em>Entry B</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntryB()
	 * @generated
	 * @ordered
	 */
	protected EntryPoint entryB;

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
	public EList<String> getRoutingOptions() {
		if (routingOptions == null) {
			routingOptions = new EDataTypeUniqueEList<String>(String.class, this, PortPackage.ROUTE__ROUTING_OPTIONS);
		}
		return routingOptions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntryPoint getEntryA() {
		return entryA;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetEntryA(EntryPoint newEntryA, NotificationChain msgs) {
		EntryPoint oldEntryA = entryA;
		entryA = newEntryA;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PortPackage.ROUTE__ENTRY_A, oldEntryA, newEntryA);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEntryA(EntryPoint newEntryA) {
		if (newEntryA != entryA) {
			NotificationChain msgs = null;
			if (entryA != null)
				msgs = ((InternalEObject)entryA).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PortPackage.ROUTE__ENTRY_A, null, msgs);
			if (newEntryA != null)
				msgs = ((InternalEObject)newEntryA).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PortPackage.ROUTE__ENTRY_A, null, msgs);
			msgs = basicSetEntryA(newEntryA, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.ROUTE__ENTRY_A, newEntryA, newEntryA));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntryPoint getEntryB() {
		return entryB;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetEntryB(EntryPoint newEntryB, NotificationChain msgs) {
		EntryPoint oldEntryB = entryB;
		entryB = newEntryB;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PortPackage.ROUTE__ENTRY_B, oldEntryB, newEntryB);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEntryB(EntryPoint newEntryB) {
		if (newEntryB != entryB) {
			NotificationChain msgs = null;
			if (entryB != null)
				msgs = ((InternalEObject)entryB).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PortPackage.ROUTE__ENTRY_B, null, msgs);
			if (newEntryB != null)
				msgs = ((InternalEObject)newEntryB).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PortPackage.ROUTE__ENTRY_B, null, msgs);
			msgs = basicSetEntryB(newEntryB, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.ROUTE__ENTRY_B, newEntryB, newEntryB));
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
			case PortPackage.ROUTE__ENTRY_A:
				return basicSetEntryA(null, msgs);
			case PortPackage.ROUTE__ENTRY_B:
				return basicSetEntryB(null, msgs);
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
			case PortPackage.ROUTE__CANAL:
				return isCanal();
			case PortPackage.ROUTE__ROUTING_OPTIONS:
				return getRoutingOptions();
			case PortPackage.ROUTE__ENTRY_A:
				return getEntryA();
			case PortPackage.ROUTE__ENTRY_B:
				return getEntryB();
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
			case PortPackage.ROUTE__CANAL:
				setCanal((Boolean)newValue);
				return;
			case PortPackage.ROUTE__ROUTING_OPTIONS:
				getRoutingOptions().clear();
				getRoutingOptions().addAll((Collection<? extends String>)newValue);
				return;
			case PortPackage.ROUTE__ENTRY_A:
				setEntryA((EntryPoint)newValue);
				return;
			case PortPackage.ROUTE__ENTRY_B:
				setEntryB((EntryPoint)newValue);
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
			case PortPackage.ROUTE__CANAL:
				setCanal(CANAL_EDEFAULT);
				return;
			case PortPackage.ROUTE__ROUTING_OPTIONS:
				getRoutingOptions().clear();
				return;
			case PortPackage.ROUTE__ENTRY_A:
				setEntryA((EntryPoint)null);
				return;
			case PortPackage.ROUTE__ENTRY_B:
				setEntryB((EntryPoint)null);
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
			case PortPackage.ROUTE__CANAL:
				return canal != CANAL_EDEFAULT;
			case PortPackage.ROUTE__ROUTING_OPTIONS:
				return routingOptions != null && !routingOptions.isEmpty();
			case PortPackage.ROUTE__ENTRY_A:
				return entryA != null;
			case PortPackage.ROUTE__ENTRY_B:
				return entryB != null;
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
