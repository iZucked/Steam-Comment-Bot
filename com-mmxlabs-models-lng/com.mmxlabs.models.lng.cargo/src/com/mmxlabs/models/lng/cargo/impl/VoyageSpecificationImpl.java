/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.FuelChoice;
import com.mmxlabs.models.lng.cargo.VoyageSpecification;

import com.mmxlabs.models.lng.port.RouteOption;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Voyage Specification</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VoyageSpecificationImpl#getRouteOption <em>Route Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VoyageSpecificationImpl#getFuelChoice <em>Fuel Choice</em>}</li>
 * </ul>
 *
 * @generated
 */
public class VoyageSpecificationImpl extends ScheduleSpecificationEventImpl implements VoyageSpecification {
	/**
	 * The default value of the '{@link #getRouteOption() <em>Route Option</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRouteOption()
	 * @generated
	 * @ordered
	 */
	protected static final RouteOption ROUTE_OPTION_EDEFAULT = RouteOption.DIRECT;
	/**
	 * The cached value of the '{@link #getRouteOption() <em>Route Option</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRouteOption()
	 * @generated
	 * @ordered
	 */
	protected RouteOption routeOption = ROUTE_OPTION_EDEFAULT;

	/**
	 * The default value of the '{@link #getFuelChoice() <em>Fuel Choice</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFuelChoice()
	 * @generated
	 * @ordered
	 */
	protected static final FuelChoice FUEL_CHOICE_EDEFAULT = FuelChoice.NBO_FBO;
	/**
	 * The cached value of the '{@link #getFuelChoice() <em>Fuel Choice</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFuelChoice()
	 * @generated
	 * @ordered
	 */
	protected FuelChoice fuelChoice = FUEL_CHOICE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VoyageSpecificationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.VOYAGE_SPECIFICATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public RouteOption getRouteOption() {
		return routeOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRouteOption(RouteOption newRouteOption) {
		RouteOption oldRouteOption = routeOption;
		routeOption = newRouteOption == null ? ROUTE_OPTION_EDEFAULT : newRouteOption;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VOYAGE_SPECIFICATION__ROUTE_OPTION, oldRouteOption, routeOption));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public FuelChoice getFuelChoice() {
		return fuelChoice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFuelChoice(FuelChoice newFuelChoice) {
		FuelChoice oldFuelChoice = fuelChoice;
		fuelChoice = newFuelChoice == null ? FUEL_CHOICE_EDEFAULT : newFuelChoice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VOYAGE_SPECIFICATION__FUEL_CHOICE, oldFuelChoice, fuelChoice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoPackage.VOYAGE_SPECIFICATION__ROUTE_OPTION:
				return getRouteOption();
			case CargoPackage.VOYAGE_SPECIFICATION__FUEL_CHOICE:
				return getFuelChoice();
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
			case CargoPackage.VOYAGE_SPECIFICATION__ROUTE_OPTION:
				setRouteOption((RouteOption)newValue);
				return;
			case CargoPackage.VOYAGE_SPECIFICATION__FUEL_CHOICE:
				setFuelChoice((FuelChoice)newValue);
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
			case CargoPackage.VOYAGE_SPECIFICATION__ROUTE_OPTION:
				setRouteOption(ROUTE_OPTION_EDEFAULT);
				return;
			case CargoPackage.VOYAGE_SPECIFICATION__FUEL_CHOICE:
				setFuelChoice(FUEL_CHOICE_EDEFAULT);
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
			case CargoPackage.VOYAGE_SPECIFICATION__ROUTE_OPTION:
				return routeOption != ROUTE_OPTION_EDEFAULT;
			case CargoPackage.VOYAGE_SPECIFICATION__FUEL_CHOICE:
				return fuelChoice != FUEL_CHOICE_EDEFAULT;
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
		result.append(" (routeOption: ");
		result.append(routeOption);
		result.append(", fuelChoice: ");
		result.append(fuelChoice);
		result.append(')');
		return result.toString();
	}

} //VoyageSpecificationImpl
