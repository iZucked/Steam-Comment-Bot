/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.schedule.events.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import scenario.schedule.events.EventsPackage;
import scenario.schedule.events.FuelMixture;
import scenario.schedule.events.FuelQuantity;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Fuel Mixture</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.schedule.events.impl.FuelMixtureImpl#getFuelUsage <em>Fuel Usage</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FuelMixtureImpl extends EObjectImpl implements FuelMixture {
	/**
	 * The cached value of the '{@link #getFuelUsage() <em>Fuel Usage</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFuelUsage()
	 * @generated
	 * @ordered
	 */
	protected EList<FuelQuantity> fuelUsage;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FuelMixtureImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EventsPackage.Literals.FUEL_MIXTURE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<FuelQuantity> getFuelUsage() {
		if (fuelUsage == null) {
			fuelUsage = new EObjectContainmentEList<FuelQuantity>(FuelQuantity.class, this, EventsPackage.FUEL_MIXTURE__FUEL_USAGE);
		}
		return fuelUsage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getTotalFuelCost() {
		long totalCost = 0;
		
		for (final FuelQuantity quantity : getFuelUsage()) {
			totalCost += quantity.getTotalPrice();
		}
		
		return totalCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case EventsPackage.FUEL_MIXTURE__FUEL_USAGE:
				return ((InternalEList<?>)getFuelUsage()).basicRemove(otherEnd, msgs);
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
			case EventsPackage.FUEL_MIXTURE__FUEL_USAGE:
				return getFuelUsage();
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
			case EventsPackage.FUEL_MIXTURE__FUEL_USAGE:
				getFuelUsage().clear();
				getFuelUsage().addAll((Collection<? extends FuelQuantity>)newValue);
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
			case EventsPackage.FUEL_MIXTURE__FUEL_USAGE:
				getFuelUsage().clear();
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
			case EventsPackage.FUEL_MIXTURE__FUEL_USAGE:
				return fuelUsage != null && !fuelUsage.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case EventsPackage.FUEL_MIXTURE___GET_TOTAL_FUEL_COST:
				return getTotalFuelCost();
		}
		return super.eInvoke(operationID, arguments);
	}

} //FuelMixtureImpl
