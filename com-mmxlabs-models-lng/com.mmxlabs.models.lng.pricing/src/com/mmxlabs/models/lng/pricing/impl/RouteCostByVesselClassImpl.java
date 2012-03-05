

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.pricing.impl;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.RouteCostByVesselClass;

import com.mmxlabs.models.lng.fleet.VesselClass;

import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Route Cost By Vessel Class</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.RouteCostByVesselClassImpl#getVesselClass <em>Vessel Class</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.RouteCostByVesselClassImpl#getLadenCost <em>Laden Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.RouteCostByVesselClassImpl#getBallastCost <em>Ballast Cost</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RouteCostByVesselClassImpl extends MMXObjectImpl implements RouteCostByVesselClass {
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
	 * The default value of the '{@link #getBallastCost() <em>Ballast Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastCost()
	 * @generated
	 * @ordered
	 */
	protected static final int BALLAST_COST_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getBallastCost() <em>Ballast Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastCost()
	 * @generated
	 * @ordered
	 */
	protected int ballastCost = BALLAST_COST_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RouteCostByVesselClassImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.ROUTE_COST_BY_VESSEL_CLASS;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PricingPackage.ROUTE_COST_BY_VESSEL_CLASS__VESSEL_CLASS, oldVesselClass, vesselClass));
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
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.ROUTE_COST_BY_VESSEL_CLASS__VESSEL_CLASS, oldVesselClass, vesselClass));
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
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.ROUTE_COST_BY_VESSEL_CLASS__LADEN_COST, oldLadenCost, ladenCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getBallastCost() {
		return ballastCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBallastCost(int newBallastCost) {
		int oldBallastCost = ballastCost;
		ballastCost = newBallastCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.ROUTE_COST_BY_VESSEL_CLASS__BALLAST_COST, oldBallastCost, ballastCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PricingPackage.ROUTE_COST_BY_VESSEL_CLASS__VESSEL_CLASS:
				if (resolve) return getVesselClass();
				return basicGetVesselClass();
			case PricingPackage.ROUTE_COST_BY_VESSEL_CLASS__LADEN_COST:
				return getLadenCost();
			case PricingPackage.ROUTE_COST_BY_VESSEL_CLASS__BALLAST_COST:
				return getBallastCost();
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
			case PricingPackage.ROUTE_COST_BY_VESSEL_CLASS__VESSEL_CLASS:
				setVesselClass((VesselClass)newValue);
				return;
			case PricingPackage.ROUTE_COST_BY_VESSEL_CLASS__LADEN_COST:
				setLadenCost((Integer)newValue);
				return;
			case PricingPackage.ROUTE_COST_BY_VESSEL_CLASS__BALLAST_COST:
				setBallastCost((Integer)newValue);
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
			case PricingPackage.ROUTE_COST_BY_VESSEL_CLASS__VESSEL_CLASS:
				setVesselClass((VesselClass)null);
				return;
			case PricingPackage.ROUTE_COST_BY_VESSEL_CLASS__LADEN_COST:
				setLadenCost(LADEN_COST_EDEFAULT);
				return;
			case PricingPackage.ROUTE_COST_BY_VESSEL_CLASS__BALLAST_COST:
				setBallastCost(BALLAST_COST_EDEFAULT);
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
			case PricingPackage.ROUTE_COST_BY_VESSEL_CLASS__VESSEL_CLASS:
				return vesselClass != null;
			case PricingPackage.ROUTE_COST_BY_VESSEL_CLASS__LADEN_COST:
				return ladenCost != LADEN_COST_EDEFAULT;
			case PricingPackage.ROUTE_COST_BY_VESSEL_CLASS__BALLAST_COST:
				return ballastCost != BALLAST_COST_EDEFAULT;
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
		result.append(", ballastCost: ");
		result.append(ballastCost);
		result.append(')');
		return result.toString();
	}

} // end of RouteCostByVesselClassImpl

// finish type fixing
