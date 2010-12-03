/**
 * Copyright (C) Minimax Labs Ltd., 2010
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
	public int getLadenCost() {
		return ladenCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
	public int getUnladenCost() {
		return unladenCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PortPackage.VESSEL_CLASS_COST__VESSEL_CLASS:
				if (resolve) return getVesselClass();
				return basicGetVesselClass();
			case PortPackage.VESSEL_CLASS_COST__LADEN_COST:
				return getLadenCost();
			case PortPackage.VESSEL_CLASS_COST__UNLADEN_COST:
				return getUnladenCost();
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
		result.append(')');
		return result.toString();
	}

} //VesselClassCostImpl
