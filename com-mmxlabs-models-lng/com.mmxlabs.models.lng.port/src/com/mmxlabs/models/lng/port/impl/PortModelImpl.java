/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.port.CapabilityGroup;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortCountryGroup;
import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortModelImpl#getPorts <em>Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortModelImpl#getPortGroups <em>Port Groups</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortModelImpl#getRoutes <em>Routes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortModelImpl#getSpecialPortGroups <em>Special Port Groups</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.PortModelImpl#getPortCountryGroups <em>Port Country Groups</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PortModelImpl extends UUIDObjectImpl implements PortModel {
	/**
	 * The cached value of the '{@link #getPorts() <em>Ports</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<Port> ports;

	/**
	 * The cached value of the '{@link #getPortGroups() <em>Port Groups</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortGroups()
	 * @generated
	 * @ordered
	 */
	protected EList<PortGroup> portGroups;

	/**
	 * The cached value of the '{@link #getRoutes() <em>Routes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRoutes()
	 * @generated
	 * @ordered
	 */
	protected EList<Route> routes;

	/**
	 * The cached value of the '{@link #getSpecialPortGroups() <em>Special Port Groups</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpecialPortGroups()
	 * @generated
	 * @ordered
	 */
	protected EList<CapabilityGroup> specialPortGroups;

	/**
	 * The cached value of the '{@link #getPortCountryGroups() <em>Port Country Groups</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortCountryGroups()
	 * @generated
	 * @ordered
	 */
	protected EList<PortCountryGroup> portCountryGroups;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PortModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PortPackage.Literals.PORT_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Port> getPorts() {
		if (ports == null) {
			ports = new EObjectContainmentEList<Port>(Port.class, this, PortPackage.PORT_MODEL__PORTS);
		}
		return ports;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PortGroup> getPortGroups() {
		if (portGroups == null) {
			portGroups = new EObjectContainmentEList<PortGroup>(PortGroup.class, this, PortPackage.PORT_MODEL__PORT_GROUPS);
		}
		return portGroups;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Route> getRoutes() {
		if (routes == null) {
			routes = new EObjectContainmentEList<Route>(Route.class, this, PortPackage.PORT_MODEL__ROUTES);
		}
		return routes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<CapabilityGroup> getSpecialPortGroups() {
		if (specialPortGroups == null) {
			specialPortGroups = new EObjectContainmentEList<CapabilityGroup>(CapabilityGroup.class, this, PortPackage.PORT_MODEL__SPECIAL_PORT_GROUPS);
		}
		return specialPortGroups;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PortCountryGroup> getPortCountryGroups() {
		if (portCountryGroups == null) {
			portCountryGroups = new EObjectContainmentEList<PortCountryGroup>(PortCountryGroup.class, this, PortPackage.PORT_MODEL__PORT_COUNTRY_GROUPS);
		}
		return portCountryGroups;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PortPackage.PORT_MODEL__PORTS:
				return ((InternalEList<?>)getPorts()).basicRemove(otherEnd, msgs);
			case PortPackage.PORT_MODEL__PORT_GROUPS:
				return ((InternalEList<?>)getPortGroups()).basicRemove(otherEnd, msgs);
			case PortPackage.PORT_MODEL__ROUTES:
				return ((InternalEList<?>)getRoutes()).basicRemove(otherEnd, msgs);
			case PortPackage.PORT_MODEL__SPECIAL_PORT_GROUPS:
				return ((InternalEList<?>)getSpecialPortGroups()).basicRemove(otherEnd, msgs);
			case PortPackage.PORT_MODEL__PORT_COUNTRY_GROUPS:
				return ((InternalEList<?>)getPortCountryGroups()).basicRemove(otherEnd, msgs);
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
			case PortPackage.PORT_MODEL__PORTS:
				return getPorts();
			case PortPackage.PORT_MODEL__PORT_GROUPS:
				return getPortGroups();
			case PortPackage.PORT_MODEL__ROUTES:
				return getRoutes();
			case PortPackage.PORT_MODEL__SPECIAL_PORT_GROUPS:
				return getSpecialPortGroups();
			case PortPackage.PORT_MODEL__PORT_COUNTRY_GROUPS:
				return getPortCountryGroups();
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
			case PortPackage.PORT_MODEL__PORTS:
				getPorts().clear();
				getPorts().addAll((Collection<? extends Port>)newValue);
				return;
			case PortPackage.PORT_MODEL__PORT_GROUPS:
				getPortGroups().clear();
				getPortGroups().addAll((Collection<? extends PortGroup>)newValue);
				return;
			case PortPackage.PORT_MODEL__ROUTES:
				getRoutes().clear();
				getRoutes().addAll((Collection<? extends Route>)newValue);
				return;
			case PortPackage.PORT_MODEL__SPECIAL_PORT_GROUPS:
				getSpecialPortGroups().clear();
				getSpecialPortGroups().addAll((Collection<? extends CapabilityGroup>)newValue);
				return;
			case PortPackage.PORT_MODEL__PORT_COUNTRY_GROUPS:
				getPortCountryGroups().clear();
				getPortCountryGroups().addAll((Collection<? extends PortCountryGroup>)newValue);
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
			case PortPackage.PORT_MODEL__PORTS:
				getPorts().clear();
				return;
			case PortPackage.PORT_MODEL__PORT_GROUPS:
				getPortGroups().clear();
				return;
			case PortPackage.PORT_MODEL__ROUTES:
				getRoutes().clear();
				return;
			case PortPackage.PORT_MODEL__SPECIAL_PORT_GROUPS:
				getSpecialPortGroups().clear();
				return;
			case PortPackage.PORT_MODEL__PORT_COUNTRY_GROUPS:
				getPortCountryGroups().clear();
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
			case PortPackage.PORT_MODEL__PORTS:
				return ports != null && !ports.isEmpty();
			case PortPackage.PORT_MODEL__PORT_GROUPS:
				return portGroups != null && !portGroups.isEmpty();
			case PortPackage.PORT_MODEL__ROUTES:
				return routes != null && !routes.isEmpty();
			case PortPackage.PORT_MODEL__SPECIAL_PORT_GROUPS:
				return specialPortGroups != null && !specialPortGroups.isEmpty();
			case PortPackage.PORT_MODEL__PORT_COUNTRY_GROUPS:
				return portCountryGroups != null && !portCountryGroups.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //PortModelImpl
