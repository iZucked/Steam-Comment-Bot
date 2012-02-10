/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.fleet.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import scenario.fleet.FleetModel;
import scenario.fleet.FleetPackage;
import scenario.fleet.Vessel;
import scenario.fleet.VesselClass;
import scenario.fleet.VesselEvent;
import scenario.fleet.VesselFuel;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Model</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.fleet.impl.FleetModelImpl#getFleet <em>Fleet</em>}</li>
 *   <li>{@link scenario.fleet.impl.FleetModelImpl#getVesselClasses <em>Vessel Classes</em>}</li>
 *   <li>{@link scenario.fleet.impl.FleetModelImpl#getVesselEvents <em>Vessel Events</em>}</li>
 *   <li>{@link scenario.fleet.impl.FleetModelImpl#getFuels <em>Fuels</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FleetModelImpl extends EObjectImpl implements FleetModel {
	/**
	 * The cached value of the '{@link #getFleet() <em>Fleet</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getFleet()
	 * @generated
	 * @ordered
	 */
	protected EList<Vessel> fleet;

	/**
	 * The cached value of the '{@link #getVesselClasses() <em>Vessel Classes</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getVesselClasses()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselClass> vesselClasses;

	/**
	 * The cached value of the '{@link #getVesselEvents() <em>Vessel Events</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getVesselEvents()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselEvent> vesselEvents;

	/**
	 * The cached value of the '{@link #getFuels() <em>Fuels</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getFuels()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselFuel> fuels;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected FleetModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.FLEET_MODEL;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Vessel> getFleet() {
		if (fleet == null) {
			fleet = new EObjectContainmentEList.Resolving<Vessel>(Vessel.class, this, FleetPackage.FLEET_MODEL__FLEET);
		}
		return fleet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<VesselClass> getVesselClasses() {
		if (vesselClasses == null) {
			vesselClasses = new EObjectContainmentEList.Resolving<VesselClass>(VesselClass.class, this, FleetPackage.FLEET_MODEL__VESSEL_CLASSES);
		}
		return vesselClasses;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<VesselEvent> getVesselEvents() {
		if (vesselEvents == null) {
			vesselEvents = new EObjectContainmentEList.Resolving<VesselEvent>(VesselEvent.class, this, FleetPackage.FLEET_MODEL__VESSEL_EVENTS);
		}
		return vesselEvents;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<VesselFuel> getFuels() {
		if (fuels == null) {
			fuels = new EObjectContainmentEList.Resolving<VesselFuel>(VesselFuel.class, this, FleetPackage.FLEET_MODEL__FUELS);
		}
		return fuels;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case FleetPackage.FLEET_MODEL__FLEET:
				return ((InternalEList<?>)getFleet()).basicRemove(otherEnd, msgs);
			case FleetPackage.FLEET_MODEL__VESSEL_CLASSES:
				return ((InternalEList<?>)getVesselClasses()).basicRemove(otherEnd, msgs);
			case FleetPackage.FLEET_MODEL__VESSEL_EVENTS:
				return ((InternalEList<?>)getVesselEvents()).basicRemove(otherEnd, msgs);
			case FleetPackage.FLEET_MODEL__FUELS:
				return ((InternalEList<?>)getFuels()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case FleetPackage.FLEET_MODEL__FLEET:
				return getFleet();
			case FleetPackage.FLEET_MODEL__VESSEL_CLASSES:
				return getVesselClasses();
			case FleetPackage.FLEET_MODEL__VESSEL_EVENTS:
				return getVesselEvents();
			case FleetPackage.FLEET_MODEL__FUELS:
				return getFuels();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case FleetPackage.FLEET_MODEL__FLEET:
				getFleet().clear();
				getFleet().addAll((Collection<? extends Vessel>)newValue);
				return;
			case FleetPackage.FLEET_MODEL__VESSEL_CLASSES:
				getVesselClasses().clear();
				getVesselClasses().addAll((Collection<? extends VesselClass>)newValue);
				return;
			case FleetPackage.FLEET_MODEL__VESSEL_EVENTS:
				getVesselEvents().clear();
				getVesselEvents().addAll((Collection<? extends VesselEvent>)newValue);
				return;
			case FleetPackage.FLEET_MODEL__FUELS:
				getFuels().clear();
				getFuels().addAll((Collection<? extends VesselFuel>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case FleetPackage.FLEET_MODEL__FLEET:
				getFleet().clear();
				return;
			case FleetPackage.FLEET_MODEL__VESSEL_CLASSES:
				getVesselClasses().clear();
				return;
			case FleetPackage.FLEET_MODEL__VESSEL_EVENTS:
				getVesselEvents().clear();
				return;
			case FleetPackage.FLEET_MODEL__FUELS:
				getFuels().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case FleetPackage.FLEET_MODEL__FLEET:
				return fleet != null && !fleet.isEmpty();
			case FleetPackage.FLEET_MODEL__VESSEL_CLASSES:
				return vesselClasses != null && !vesselClasses.isEmpty();
			case FleetPackage.FLEET_MODEL__VESSEL_EVENTS:
				return vesselEvents != null && !vesselEvents.isEmpty();
			case FleetPackage.FLEET_MODEL__FUELS:
				return fuels != null && !fuels.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // FleetModelImpl
