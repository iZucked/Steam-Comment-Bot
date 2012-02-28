/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.impl;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselEvent;

import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;

import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

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
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.FleetModelImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.FleetModelImpl#getVessels <em>Vessels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.FleetModelImpl#getVesselClasses <em>Vessel Classes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.FleetModelImpl#getVesselEvents <em>Vessel Events</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.FleetModelImpl#getBaseFuels <em>Base Fuels</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FleetModelImpl extends UUIDObjectImpl implements FleetModel {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getVessels() <em>Vessels</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVessels()
	 * @generated
	 * @ordered
	 */
	protected EList<Vessel> vessels;

	/**
	 * The cached value of the '{@link #getVesselClasses() <em>Vessel Classes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselClasses()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselClass> vesselClasses;

	/**
	 * The cached value of the '{@link #getVesselEvents() <em>Vessel Events</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselEvents()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselEvent> vesselEvents;

	/**
	 * The cached value of the '{@link #getBaseFuels() <em>Base Fuels</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseFuels()
	 * @generated
	 * @ordered
	 */
	protected EList<BaseFuel> baseFuels;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FleetModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.FLEET_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.FLEET_MODEL__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Vessel> getVessels() {
		if (vessels == null) {
			vessels = new EObjectContainmentEList<Vessel>(Vessel.class, this, FleetPackage.FLEET_MODEL__VESSELS);
		}
		return vessels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<VesselClass> getVesselClasses() {
		if (vesselClasses == null) {
			vesselClasses = new EObjectContainmentEList<VesselClass>(VesselClass.class, this, FleetPackage.FLEET_MODEL__VESSEL_CLASSES);
		}
		return vesselClasses;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<VesselEvent> getVesselEvents() {
		if (vesselEvents == null) {
			vesselEvents = new EObjectContainmentEList<VesselEvent>(VesselEvent.class, this, FleetPackage.FLEET_MODEL__VESSEL_EVENTS);
		}
		return vesselEvents;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<BaseFuel> getBaseFuels() {
		if (baseFuels == null) {
			baseFuels = new EObjectContainmentEList<BaseFuel>(BaseFuel.class, this, FleetPackage.FLEET_MODEL__BASE_FUELS);
		}
		return baseFuels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case FleetPackage.FLEET_MODEL__VESSELS:
				return ((InternalEList<?>)getVessels()).basicRemove(otherEnd, msgs);
			case FleetPackage.FLEET_MODEL__VESSEL_CLASSES:
				return ((InternalEList<?>)getVesselClasses()).basicRemove(otherEnd, msgs);
			case FleetPackage.FLEET_MODEL__VESSEL_EVENTS:
				return ((InternalEList<?>)getVesselEvents()).basicRemove(otherEnd, msgs);
			case FleetPackage.FLEET_MODEL__BASE_FUELS:
				return ((InternalEList<?>)getBaseFuels()).basicRemove(otherEnd, msgs);
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
			case FleetPackage.FLEET_MODEL__NAME:
				return getName();
			case FleetPackage.FLEET_MODEL__VESSELS:
				return getVessels();
			case FleetPackage.FLEET_MODEL__VESSEL_CLASSES:
				return getVesselClasses();
			case FleetPackage.FLEET_MODEL__VESSEL_EVENTS:
				return getVesselEvents();
			case FleetPackage.FLEET_MODEL__BASE_FUELS:
				return getBaseFuels();
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
			case FleetPackage.FLEET_MODEL__NAME:
				setName((String)newValue);
				return;
			case FleetPackage.FLEET_MODEL__VESSELS:
				getVessels().clear();
				getVessels().addAll((Collection<? extends Vessel>)newValue);
				return;
			case FleetPackage.FLEET_MODEL__VESSEL_CLASSES:
				getVesselClasses().clear();
				getVesselClasses().addAll((Collection<? extends VesselClass>)newValue);
				return;
			case FleetPackage.FLEET_MODEL__VESSEL_EVENTS:
				getVesselEvents().clear();
				getVesselEvents().addAll((Collection<? extends VesselEvent>)newValue);
				return;
			case FleetPackage.FLEET_MODEL__BASE_FUELS:
				getBaseFuels().clear();
				getBaseFuels().addAll((Collection<? extends BaseFuel>)newValue);
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
			case FleetPackage.FLEET_MODEL__NAME:
				setName(NAME_EDEFAULT);
				return;
			case FleetPackage.FLEET_MODEL__VESSELS:
				getVessels().clear();
				return;
			case FleetPackage.FLEET_MODEL__VESSEL_CLASSES:
				getVesselClasses().clear();
				return;
			case FleetPackage.FLEET_MODEL__VESSEL_EVENTS:
				getVesselEvents().clear();
				return;
			case FleetPackage.FLEET_MODEL__BASE_FUELS:
				getBaseFuels().clear();
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
			case FleetPackage.FLEET_MODEL__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case FleetPackage.FLEET_MODEL__VESSELS:
				return vessels != null && !vessels.isEmpty();
			case FleetPackage.FLEET_MODEL__VESSEL_CLASSES:
				return vesselClasses != null && !vesselClasses.isEmpty();
			case FleetPackage.FLEET_MODEL__VESSEL_EVENTS:
				return vesselEvents != null && !vesselEvents.isEmpty();
			case FleetPackage.FLEET_MODEL__BASE_FUELS:
				return baseFuels != null && !baseFuels.isEmpty();
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
		if (baseClass == NamedObject.class) {
			switch (derivedFeatureID) {
				case FleetPackage.FLEET_MODEL__NAME: return MMXCorePackage.NAMED_OBJECT__NAME;
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
		if (baseClass == NamedObject.class) {
			switch (baseFeatureID) {
				case MMXCorePackage.NAMED_OBJECT__NAME: return FleetPackage.FLEET_MODEL__NAME;
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
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} // end of FleetModelImpl

// finish type fixing
