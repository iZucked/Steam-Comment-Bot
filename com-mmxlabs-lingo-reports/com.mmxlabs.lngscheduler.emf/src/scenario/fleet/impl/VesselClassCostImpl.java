/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.fleet.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import scenario.fleet.FleetPackage;
import scenario.fleet.VesselClassCost;
import scenario.port.Canal;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Vessel Class Cost</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.fleet.impl.VesselClassCostImpl#getCanal <em>Canal</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassCostImpl#getLadenCost <em>Laden Cost</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassCostImpl#getUnladenCost <em>Unladen Cost</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassCostImpl#getTransitTime <em>Transit Time</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselClassCostImpl#getTransitFuel <em>Transit Fuel</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VesselClassCostImpl extends EObjectImpl implements VesselClassCost {
	/**
	 * The cached value of the '{@link #getCanal() <em>Canal</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getCanal()
	 * @generated
	 * @ordered
	 */
	protected Canal canal;

	/**
	 * The default value of the '{@link #getLadenCost() <em>Laden Cost</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getLadenCost()
	 * @generated
	 * @ordered
	 */
	protected static final int LADEN_COST_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getLadenCost() <em>Laden Cost</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getLadenCost()
	 * @generated
	 * @ordered
	 */
	protected int ladenCost = LADEN_COST_EDEFAULT;

	/**
	 * The default value of the '{@link #getUnladenCost() <em>Unladen Cost</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getUnladenCost()
	 * @generated
	 * @ordered
	 */
	protected static final int UNLADEN_COST_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getUnladenCost() <em>Unladen Cost</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getUnladenCost()
	 * @generated
	 * @ordered
	 */
	protected int unladenCost = UNLADEN_COST_EDEFAULT;

	/**
	 * The default value of the '{@link #getTransitTime() <em>Transit Time</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getTransitTime()
	 * @generated
	 * @ordered
	 */
	protected static final int TRANSIT_TIME_EDEFAULT = 24;

	/**
	 * The cached value of the '{@link #getTransitTime() <em>Transit Time</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getTransitTime()
	 * @generated
	 * @ordered
	 */
	protected int transitTime = TRANSIT_TIME_EDEFAULT;

	/**
	 * The default value of the '{@link #getTransitFuel() <em>Transit Fuel</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getTransitFuel()
	 * @generated
	 * @ordered
	 */
	protected static final float TRANSIT_FUEL_EDEFAULT = 50.0F;

	/**
	 * The cached value of the '{@link #getTransitFuel() <em>Transit Fuel</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getTransitFuel()
	 * @generated
	 * @ordered
	 */
	protected float transitFuel = TRANSIT_FUEL_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselClassCostImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.VESSEL_CLASS_COST;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Canal getCanal() {
		if (canal != null && canal.eIsProxy()) {
			InternalEObject oldCanal = (InternalEObject)canal;
			canal = (Canal)eResolveProxy(oldCanal);
			if (canal != oldCanal) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, FleetPackage.VESSEL_CLASS_COST__CANAL, oldCanal, canal));
			}
		}
		return canal;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Canal basicGetCanal() {
		return canal;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCanal(Canal newCanal) {
		Canal oldCanal = canal;
		canal = newCanal;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS_COST__CANAL, oldCanal, canal));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getLadenCost() {
		return ladenCost;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLadenCost(int newLadenCost) {
		int oldLadenCost = ladenCost;
		ladenCost = newLadenCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS_COST__LADEN_COST, oldLadenCost, ladenCost));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getUnladenCost() {
		return unladenCost;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setUnladenCost(int newUnladenCost) {
		int oldUnladenCost = unladenCost;
		unladenCost = newUnladenCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS_COST__UNLADEN_COST, oldUnladenCost, unladenCost));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getTransitTime() {
		return transitTime;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTransitTime(int newTransitTime) {
		int oldTransitTime = transitTime;
		transitTime = newTransitTime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS_COST__TRANSIT_TIME, oldTransitTime, transitTime));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public float getTransitFuel() {
		return transitFuel;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTransitFuel(float newTransitFuel) {
		float oldTransitFuel = transitFuel;
		transitFuel = newTransitFuel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_CLASS_COST__TRANSIT_FUEL, oldTransitFuel, transitFuel));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case FleetPackage.VESSEL_CLASS_COST__CANAL:
				if (resolve) return getCanal();
				return basicGetCanal();
			case FleetPackage.VESSEL_CLASS_COST__LADEN_COST:
				return getLadenCost();
			case FleetPackage.VESSEL_CLASS_COST__UNLADEN_COST:
				return getUnladenCost();
			case FleetPackage.VESSEL_CLASS_COST__TRANSIT_TIME:
				return getTransitTime();
			case FleetPackage.VESSEL_CLASS_COST__TRANSIT_FUEL:
				return getTransitFuel();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case FleetPackage.VESSEL_CLASS_COST__CANAL:
				setCanal((Canal)newValue);
				return;
			case FleetPackage.VESSEL_CLASS_COST__LADEN_COST:
				setLadenCost((Integer)newValue);
				return;
			case FleetPackage.VESSEL_CLASS_COST__UNLADEN_COST:
				setUnladenCost((Integer)newValue);
				return;
			case FleetPackage.VESSEL_CLASS_COST__TRANSIT_TIME:
				setTransitTime((Integer)newValue);
				return;
			case FleetPackage.VESSEL_CLASS_COST__TRANSIT_FUEL:
				setTransitFuel((Float)newValue);
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
			case FleetPackage.VESSEL_CLASS_COST__CANAL:
				setCanal((Canal)null);
				return;
			case FleetPackage.VESSEL_CLASS_COST__LADEN_COST:
				setLadenCost(LADEN_COST_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS_COST__UNLADEN_COST:
				setUnladenCost(UNLADEN_COST_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS_COST__TRANSIT_TIME:
				setTransitTime(TRANSIT_TIME_EDEFAULT);
				return;
			case FleetPackage.VESSEL_CLASS_COST__TRANSIT_FUEL:
				setTransitFuel(TRANSIT_FUEL_EDEFAULT);
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
			case FleetPackage.VESSEL_CLASS_COST__CANAL:
				return canal != null;
			case FleetPackage.VESSEL_CLASS_COST__LADEN_COST:
				return ladenCost != LADEN_COST_EDEFAULT;
			case FleetPackage.VESSEL_CLASS_COST__UNLADEN_COST:
				return unladenCost != UNLADEN_COST_EDEFAULT;
			case FleetPackage.VESSEL_CLASS_COST__TRANSIT_TIME:
				return transitTime != TRANSIT_TIME_EDEFAULT;
			case FleetPackage.VESSEL_CLASS_COST__TRANSIT_FUEL:
				return transitFuel != TRANSIT_FUEL_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (ladenCost: ");
		result.append(ladenCost);
		result.append(", unladenCost: ");
		result.append(unladenCost);
		result.append(", transitTime: ");
		result.append(transitTime);
		result.append(", transitFuel: ");
		result.append(transitFuel);
		result.append(')');
		return result.toString();
	}

} // VesselClassCostImpl
