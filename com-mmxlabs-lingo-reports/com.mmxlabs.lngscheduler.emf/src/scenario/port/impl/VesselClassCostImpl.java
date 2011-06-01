/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.port.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import scenario.fleet.VesselClass;
import scenario.port.PortPackage;
import scenario.port.VesselClassCost;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vessel Class Cost</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.port.impl.VesselClassCostImpl#getVesselClass <em>Vessel Class</em>}</li>
 *   <li>{@link scenario.port.impl.VesselClassCostImpl#getLadenCost <em>Laden Cost</em>}</li>
 *   <li>{@link scenario.port.impl.VesselClassCostImpl#getUnladenCost <em>Unladen Cost</em>}</li>
 *   <li>{@link scenario.port.impl.VesselClassCostImpl#getTransitTime <em>Transit Time</em>}</li>
 *   <li>{@link scenario.port.impl.VesselClassCostImpl#getTransitFuel <em>Transit Fuel</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VesselClassCostImpl extends EObjectImpl implements VesselClassCost {
	/**
	 * The cached value of the '{@link #getVesselClass() <em>Vessel Class</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselClass()
	 * @generated
	 * @ordered
	 */
	protected VesselClass vesselClass;

	/**
	 * The default value of the '{@link #getLadenCost() <em>Laden Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLadenCost()
	 * @generated
	 * @ordered
	 */
	protected static final int LADEN_COST_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getLadenCost() <em>Laden Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLadenCost()
	 * @generated
	 * @ordered
	 */
	protected int ladenCost = LADEN_COST_EDEFAULT;

	/**
	 * The default value of the '{@link #getUnladenCost() <em>Unladen Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnladenCost()
	 * @generated
	 * @ordered
	 */
	protected static final int UNLADEN_COST_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getUnladenCost() <em>Unladen Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnladenCost()
	 * @generated
	 * @ordered
	 */
	protected int unladenCost = UNLADEN_COST_EDEFAULT;

	/**
	 * The default value of the '{@link #getTransitTime() <em>Transit Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransitTime()
	 * @generated
	 * @ordered
	 */
	protected static final int TRANSIT_TIME_EDEFAULT = 24;

	/**
	 * The cached value of the '{@link #getTransitTime() <em>Transit Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransitTime()
	 * @generated
	 * @ordered
	 */
	protected int transitTime = TRANSIT_TIME_EDEFAULT;

	/**
	 * The default value of the '{@link #getTransitFuel() <em>Transit Fuel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransitFuel()
	 * @generated
	 * @ordered
	 */
	protected static final float TRANSIT_FUEL_EDEFAULT = 50.0F;

	/**
	 * The cached value of the '{@link #getTransitFuel() <em>Transit Fuel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransitFuel()
	 * @generated
	 * @ordered
	 */
	protected float transitFuel = TRANSIT_FUEL_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselClassCostImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PortPackage.Literals.VESSEL_CLASS_COST;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VesselClass getVesselClass() {
		if (vesselClass != null && vesselClass.eIsProxy()) {
			InternalEObject oldVesselClass = (InternalEObject)vesselClass;
			vesselClass = (VesselClass)eResolveProxy(oldVesselClass);
			if (vesselClass != oldVesselClass) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PortPackage.VESSEL_CLASS_COST__VESSEL_CLASS, oldVesselClass, vesselClass));
			}
		}
		return vesselClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselClass basicGetVesselClass() {
		return vesselClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVesselClass(VesselClass newVesselClass) {
		VesselClass oldVesselClass = vesselClass;
		vesselClass = newVesselClass;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.VESSEL_CLASS_COST__VESSEL_CLASS, oldVesselClass, vesselClass));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getLadenCost() {
		return ladenCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLadenCost(int newLadenCost) {
		int oldLadenCost = ladenCost;
		ladenCost = newLadenCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.VESSEL_CLASS_COST__LADEN_COST, oldLadenCost, ladenCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getUnladenCost() {
		return unladenCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setUnladenCost(int newUnladenCost) {
		int oldUnladenCost = unladenCost;
		unladenCost = newUnladenCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.VESSEL_CLASS_COST__UNLADEN_COST, oldUnladenCost, unladenCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getTransitTime() {
		return transitTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTransitTime(int newTransitTime) {
		int oldTransitTime = transitTime;
		transitTime = newTransitTime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.VESSEL_CLASS_COST__TRANSIT_TIME, oldTransitTime, transitTime));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public float getTransitFuel() {
		return transitFuel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTransitFuel(float newTransitFuel) {
		float oldTransitFuel = transitFuel;
		transitFuel = newTransitFuel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.VESSEL_CLASS_COST__TRANSIT_FUEL, oldTransitFuel, transitFuel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PortPackage.VESSEL_CLASS_COST__VESSEL_CLASS:
				if (resolve) return getVesselClass();
				return basicGetVesselClass();
			case PortPackage.VESSEL_CLASS_COST__LADEN_COST:
				return getLadenCost();
			case PortPackage.VESSEL_CLASS_COST__UNLADEN_COST:
				return getUnladenCost();
			case PortPackage.VESSEL_CLASS_COST__TRANSIT_TIME:
				return getTransitTime();
			case PortPackage.VESSEL_CLASS_COST__TRANSIT_FUEL:
				return getTransitFuel();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case PortPackage.VESSEL_CLASS_COST__VESSEL_CLASS:
				setVesselClass((VesselClass)newValue);
				return;
			case PortPackage.VESSEL_CLASS_COST__LADEN_COST:
				setLadenCost((Integer)newValue);
				return;
			case PortPackage.VESSEL_CLASS_COST__UNLADEN_COST:
				setUnladenCost((Integer)newValue);
				return;
			case PortPackage.VESSEL_CLASS_COST__TRANSIT_TIME:
				setTransitTime((Integer)newValue);
				return;
			case PortPackage.VESSEL_CLASS_COST__TRANSIT_FUEL:
				setTransitFuel((Float)newValue);
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
			case PortPackage.VESSEL_CLASS_COST__VESSEL_CLASS:
				setVesselClass((VesselClass)null);
				return;
			case PortPackage.VESSEL_CLASS_COST__LADEN_COST:
				setLadenCost(LADEN_COST_EDEFAULT);
				return;
			case PortPackage.VESSEL_CLASS_COST__UNLADEN_COST:
				setUnladenCost(UNLADEN_COST_EDEFAULT);
				return;
			case PortPackage.VESSEL_CLASS_COST__TRANSIT_TIME:
				setTransitTime(TRANSIT_TIME_EDEFAULT);
				return;
			case PortPackage.VESSEL_CLASS_COST__TRANSIT_FUEL:
				setTransitFuel(TRANSIT_FUEL_EDEFAULT);
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
			case PortPackage.VESSEL_CLASS_COST__VESSEL_CLASS:
				return vesselClass != null;
			case PortPackage.VESSEL_CLASS_COST__LADEN_COST:
				return ladenCost != LADEN_COST_EDEFAULT;
			case PortPackage.VESSEL_CLASS_COST__UNLADEN_COST:
				return unladenCost != UNLADEN_COST_EDEFAULT;
			case PortPackage.VESSEL_CLASS_COST__TRANSIT_TIME:
				return transitTime != TRANSIT_TIME_EDEFAULT;
			case PortPackage.VESSEL_CLASS_COST__TRANSIT_FUEL:
				return transitFuel != TRANSIT_FUEL_EDEFAULT;
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

} //VesselClassCostImpl
