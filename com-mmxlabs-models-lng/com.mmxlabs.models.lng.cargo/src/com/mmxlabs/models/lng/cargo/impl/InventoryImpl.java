/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.InventoryCapacityRow;
import com.mmxlabs.models.lng.cargo.InventoryFacilityType;
import com.mmxlabs.models.lng.cargo.InventoryFeedRow;
import com.mmxlabs.models.lng.cargo.InventoryOfftakeRow;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.mmxcore.impl.NamedObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Inventory</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.InventoryImpl#getPort <em>Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.InventoryImpl#getFeeds <em>Feeds</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.InventoryImpl#getOfftakes <em>Offtakes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.InventoryImpl#getCapacities <em>Capacities</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.InventoryImpl#getFacilityType <em>Facility Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InventoryImpl extends NamedObjectImpl implements Inventory {
	/**
	 * The cached value of the '{@link #getPort() <em>Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPort()
	 * @generated
	 * @ordered
	 */
	protected Port port;

	/**
	 * The cached value of the '{@link #getFeeds() <em>Feeds</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFeeds()
	 * @generated
	 * @ordered
	 */
	protected EList<InventoryFeedRow> feeds;

	/**
	 * The cached value of the '{@link #getOfftakes() <em>Offtakes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOfftakes()
	 * @generated
	 * @ordered
	 */
	protected EList<InventoryOfftakeRow> offtakes;

	/**
	 * The cached value of the '{@link #getCapacities() <em>Capacities</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCapacities()
	 * @generated
	 * @ordered
	 */
	protected EList<InventoryCapacityRow> capacities;

	/**
	 * The default value of the '{@link #getFacilityType() <em>Facility Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFacilityType()
	 * @generated
	 * @ordered
	 */
	protected static final InventoryFacilityType FACILITY_TYPE_EDEFAULT = InventoryFacilityType.UPSTREAM;

	/**
	 * The cached value of the '{@link #getFacilityType() <em>Facility Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFacilityType()
	 * @generated
	 * @ordered
	 */
	protected InventoryFacilityType facilityType = FACILITY_TYPE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InventoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.INVENTORY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<InventoryFeedRow> getFeeds() {
		if (feeds == null) {
			feeds = new EObjectContainmentEList.Resolving<InventoryFeedRow>(InventoryFeedRow.class, this, CargoPackage.INVENTORY__FEEDS);
		}
		return feeds;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<InventoryOfftakeRow> getOfftakes() {
		if (offtakes == null) {
			offtakes = new EObjectContainmentEList.Resolving<InventoryOfftakeRow>(InventoryOfftakeRow.class, this, CargoPackage.INVENTORY__OFFTAKES);
		}
		return offtakes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<InventoryCapacityRow> getCapacities() {
		if (capacities == null) {
			capacities = new EObjectContainmentEList.Resolving<InventoryCapacityRow>(InventoryCapacityRow.class, this, CargoPackage.INVENTORY__CAPACITIES);
		}
		return capacities;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public InventoryFacilityType getFacilityType() {
		return facilityType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFacilityType(InventoryFacilityType newFacilityType) {
		InventoryFacilityType oldFacilityType = facilityType;
		facilityType = newFacilityType == null ? FACILITY_TYPE_EDEFAULT : newFacilityType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.INVENTORY__FACILITY_TYPE, oldFacilityType, facilityType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Port getPort() {
		if (port != null && port.eIsProxy()) {
			InternalEObject oldPort = (InternalEObject)port;
			port = (Port)eResolveProxy(oldPort);
			if (port != oldPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.INVENTORY__PORT, oldPort, port));
			}
		}
		return port;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetPort() {
		return port;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPort(Port newPort) {
		Port oldPort = port;
		port = newPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.INVENTORY__PORT, oldPort, port));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CargoPackage.INVENTORY__FEEDS:
				return ((InternalEList<?>)getFeeds()).basicRemove(otherEnd, msgs);
			case CargoPackage.INVENTORY__OFFTAKES:
				return ((InternalEList<?>)getOfftakes()).basicRemove(otherEnd, msgs);
			case CargoPackage.INVENTORY__CAPACITIES:
				return ((InternalEList<?>)getCapacities()).basicRemove(otherEnd, msgs);
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
			case CargoPackage.INVENTORY__PORT:
				if (resolve) return getPort();
				return basicGetPort();
			case CargoPackage.INVENTORY__FEEDS:
				return getFeeds();
			case CargoPackage.INVENTORY__OFFTAKES:
				return getOfftakes();
			case CargoPackage.INVENTORY__CAPACITIES:
				return getCapacities();
			case CargoPackage.INVENTORY__FACILITY_TYPE:
				return getFacilityType();
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
			case CargoPackage.INVENTORY__PORT:
				setPort((Port)newValue);
				return;
			case CargoPackage.INVENTORY__FEEDS:
				getFeeds().clear();
				getFeeds().addAll((Collection<? extends InventoryFeedRow>)newValue);
				return;
			case CargoPackage.INVENTORY__OFFTAKES:
				getOfftakes().clear();
				getOfftakes().addAll((Collection<? extends InventoryOfftakeRow>)newValue);
				return;
			case CargoPackage.INVENTORY__CAPACITIES:
				getCapacities().clear();
				getCapacities().addAll((Collection<? extends InventoryCapacityRow>)newValue);
				return;
			case CargoPackage.INVENTORY__FACILITY_TYPE:
				setFacilityType((InventoryFacilityType)newValue);
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
			case CargoPackage.INVENTORY__PORT:
				setPort((Port)null);
				return;
			case CargoPackage.INVENTORY__FEEDS:
				getFeeds().clear();
				return;
			case CargoPackage.INVENTORY__OFFTAKES:
				getOfftakes().clear();
				return;
			case CargoPackage.INVENTORY__CAPACITIES:
				getCapacities().clear();
				return;
			case CargoPackage.INVENTORY__FACILITY_TYPE:
				setFacilityType(FACILITY_TYPE_EDEFAULT);
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
			case CargoPackage.INVENTORY__PORT:
				return port != null;
			case CargoPackage.INVENTORY__FEEDS:
				return feeds != null && !feeds.isEmpty();
			case CargoPackage.INVENTORY__OFFTAKES:
				return offtakes != null && !offtakes.isEmpty();
			case CargoPackage.INVENTORY__CAPACITIES:
				return capacities != null && !capacities.isEmpty();
			case CargoPackage.INVENTORY__FACILITY_TYPE:
				return facilityType != FACILITY_TYPE_EDEFAULT;
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
		result.append(" (facilityType: ");
		result.append(facilityType);
		result.append(')');
		return result.toString();
	}

} //InventoryImpl
