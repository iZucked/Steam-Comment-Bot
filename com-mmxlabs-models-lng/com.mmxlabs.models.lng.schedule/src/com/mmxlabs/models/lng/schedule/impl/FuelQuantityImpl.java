/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.SchedulePackage;

import java.math.BigDecimal;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Fuel Quantity</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.FuelQuantityImpl#getFuelName <em>Fuel Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.FuelQuantityImpl#getCost <em>Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.FuelQuantityImpl#getAmounts <em>Amounts</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FuelQuantityImpl extends EObjectImpl implements FuelQuantity {
	/**
	 * The default value of the '{@link #getFuelName() <em>Fuel Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFuelName()
	 * @generated
	 * @ordered
	 */
	protected static final String FUEL_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFuelName() <em>Fuel Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFuelName()
	 * @generated
	 * @ordered
	 */
	protected String fuelName = FUEL_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getCost() <em>Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCost()
	 * @generated
	 * @ordered
	 */
	protected static final BigDecimal COST_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCost() <em>Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCost()
	 * @generated
	 * @ordered
	 */
	protected BigDecimal cost = COST_EDEFAULT;

	/**
	 * The cached value of the '{@link #getAmounts() <em>Amounts</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAmounts()
	 * @generated
	 * @ordered
	 */
	protected EList<FuelAmount> amounts;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FuelQuantityImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.FUEL_QUANTITY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getFuelName() {
		return fuelName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFuelName(String newFuelName) {
		String oldFuelName = fuelName;
		fuelName = newFuelName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.FUEL_QUANTITY__FUEL_NAME, oldFuelName, fuelName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BigDecimal getCost() {
		return cost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCost(BigDecimal newCost) {
		BigDecimal oldCost = cost;
		cost = newCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.FUEL_QUANTITY__COST, oldCost, cost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<FuelAmount> getAmounts() {
		if (amounts == null) {
			amounts = new EObjectContainmentEList<FuelAmount>(FuelAmount.class, this, SchedulePackage.FUEL_QUANTITY__AMOUNTS);
		}
		return amounts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.FUEL_QUANTITY__AMOUNTS:
				return ((InternalEList<?>)getAmounts()).basicRemove(otherEnd, msgs);
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
			case SchedulePackage.FUEL_QUANTITY__FUEL_NAME:
				return getFuelName();
			case SchedulePackage.FUEL_QUANTITY__COST:
				return getCost();
			case SchedulePackage.FUEL_QUANTITY__AMOUNTS:
				return getAmounts();
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
			case SchedulePackage.FUEL_QUANTITY__FUEL_NAME:
				setFuelName((String)newValue);
				return;
			case SchedulePackage.FUEL_QUANTITY__COST:
				setCost((BigDecimal)newValue);
				return;
			case SchedulePackage.FUEL_QUANTITY__AMOUNTS:
				getAmounts().clear();
				getAmounts().addAll((Collection<? extends FuelAmount>)newValue);
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
			case SchedulePackage.FUEL_QUANTITY__FUEL_NAME:
				setFuelName(FUEL_NAME_EDEFAULT);
				return;
			case SchedulePackage.FUEL_QUANTITY__COST:
				setCost(COST_EDEFAULT);
				return;
			case SchedulePackage.FUEL_QUANTITY__AMOUNTS:
				getAmounts().clear();
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
			case SchedulePackage.FUEL_QUANTITY__FUEL_NAME:
				return FUEL_NAME_EDEFAULT == null ? fuelName != null : !FUEL_NAME_EDEFAULT.equals(fuelName);
			case SchedulePackage.FUEL_QUANTITY__COST:
				return COST_EDEFAULT == null ? cost != null : !COST_EDEFAULT.equals(cost);
			case SchedulePackage.FUEL_QUANTITY__AMOUNTS:
				return amounts != null && !amounts.isEmpty();
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
		result.append(" (fuelName: ");
		result.append(fuelName);
		result.append(", cost: ");
		result.append(cost);
		result.append(')');
		return result.toString();
	}

} // end of FuelQuantityImpl

// finish type fixing
