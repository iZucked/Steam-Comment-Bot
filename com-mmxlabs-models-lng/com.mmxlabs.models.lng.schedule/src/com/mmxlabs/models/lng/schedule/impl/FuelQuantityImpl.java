/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;
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

import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.SchedulePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Fuel Quantity</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.FuelQuantityImpl#getFuel <em>Fuel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.FuelQuantityImpl#getCost <em>Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.FuelQuantityImpl#getAmounts <em>Amounts</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FuelQuantityImpl extends EObjectImpl implements FuelQuantity {
	/**
	 * The default value of the '{@link #getFuel() <em>Fuel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFuel()
	 * @generated
	 * @ordered
	 */
	protected static final Fuel FUEL_EDEFAULT = Fuel.BASE_FUEL;

	/**
	 * The cached value of the '{@link #getFuel() <em>Fuel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFuel()
	 * @generated
	 * @ordered
	 */
	protected Fuel fuel = FUEL_EDEFAULT;

	/**
	 * The default value of the '{@link #getCost() <em>Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCost()
	 * @generated
	 * @ordered
	 */
	protected static final int COST_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getCost() <em>Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCost()
	 * @generated
	 * @ordered
	 */
	protected int cost = COST_EDEFAULT;

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
	public Fuel getFuel() {
		return fuel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFuel(Fuel newFuel) {
		Fuel oldFuel = fuel;
		fuel = newFuel == null ? FUEL_EDEFAULT : newFuel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.FUEL_QUANTITY__FUEL, oldFuel, fuel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getCost() {
		return cost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCost(int newCost) {
		int oldCost = cost;
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
			case SchedulePackage.FUEL_QUANTITY__FUEL:
				return getFuel();
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
			case SchedulePackage.FUEL_QUANTITY__FUEL:
				setFuel((Fuel)newValue);
				return;
			case SchedulePackage.FUEL_QUANTITY__COST:
				setCost((Integer)newValue);
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
			case SchedulePackage.FUEL_QUANTITY__FUEL:
				setFuel(FUEL_EDEFAULT);
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
			case SchedulePackage.FUEL_QUANTITY__FUEL:
				return fuel != FUEL_EDEFAULT;
			case SchedulePackage.FUEL_QUANTITY__COST:
				return cost != COST_EDEFAULT;
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
		result.append(" (fuel: ");
		result.append(fuel);
		result.append(", cost: ");
		result.append(cost);
		result.append(')');
		return result.toString();
	}

} // end of FuelQuantityImpl

// finish type fixing
