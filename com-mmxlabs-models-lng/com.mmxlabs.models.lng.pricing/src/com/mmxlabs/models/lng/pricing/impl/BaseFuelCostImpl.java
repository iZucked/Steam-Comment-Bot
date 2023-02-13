/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Base Fuel Cost</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.BaseFuelCostImpl#getFuel <em>Fuel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.BaseFuelCostImpl#getExpression <em>Expression</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BaseFuelCostImpl extends MMXObjectImpl implements BaseFuelCost {
	/**
	 * The cached value of the '{@link #getFuel() <em>Fuel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFuel()
	 * @generated
	 * @ordered
	 */
	protected BaseFuel fuel;

	/**
	 * The default value of the '{@link #getExpression() <em>Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String EXPRESSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getExpression() <em>Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExpression()
	 * @generated
	 * @ordered
	 */
	protected String expression = EXPRESSION_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BaseFuelCostImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.BASE_FUEL_COST;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BaseFuel getFuel() {
		if (fuel != null && fuel.eIsProxy()) {
			InternalEObject oldFuel = (InternalEObject)fuel;
			fuel = (BaseFuel)eResolveProxy(oldFuel);
			if (fuel != oldFuel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PricingPackage.BASE_FUEL_COST__FUEL, oldFuel, fuel));
			}
		}
		return fuel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseFuel basicGetFuel() {
		return fuel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFuel(BaseFuel newFuel) {
		BaseFuel oldFuel = fuel;
		fuel = newFuel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.BASE_FUEL_COST__FUEL, oldFuel, fuel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getExpression() {
		return expression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setExpression(String newExpression) {
		String oldExpression = expression;
		expression = newExpression;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.BASE_FUEL_COST__EXPRESSION, oldExpression, expression));
	}

	//	/**
//	 * <!-- begin-user-doc -->
//	 * <!-- end-user-doc -->
//	 * @generated NOT
//	 */
//	public double _getPrice() {
//		if (getIndex() == null) {
//			return 0;
//		}
//		Index<Double> data = getIndex().getData();
//		if (data == null) {
//			return 0;
//		}
//		Date date = data.getDates().get(0);
//		return data.getValueForMonth(date);
//		//return getIndex().getData().getValueForMonth(date)
//		// TODO: implement this method
//		// Ensure that you remove @generated or mark it @generated NOT
//		//throw new UnsupportedOperationException();
//	}
//
//	/**
//	 * <!-- begin-user-doc -->
//	 * <!-- end-user-doc -->
//	 * @generated NOT
//	 */
//	public void _setPrice(double price) {
//		// debug measure: dummy up a price field while moving over to base price indices
//	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PricingPackage.BASE_FUEL_COST__FUEL:
				if (resolve) return getFuel();
				return basicGetFuel();
			case PricingPackage.BASE_FUEL_COST__EXPRESSION:
				return getExpression();
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
			case PricingPackage.BASE_FUEL_COST__FUEL:
				setFuel((BaseFuel)newValue);
				return;
			case PricingPackage.BASE_FUEL_COST__EXPRESSION:
				setExpression((String)newValue);
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
			case PricingPackage.BASE_FUEL_COST__FUEL:
				setFuel((BaseFuel)null);
				return;
			case PricingPackage.BASE_FUEL_COST__EXPRESSION:
				setExpression(EXPRESSION_EDEFAULT);
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
			case PricingPackage.BASE_FUEL_COST__FUEL:
				return fuel != null;
			case PricingPackage.BASE_FUEL_COST__EXPRESSION:
				return EXPRESSION_EDEFAULT == null ? expression != null : !EXPRESSION_EDEFAULT.equals(expression);
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
		result.append(" (expression: ");
		result.append(expression);
		result.append(')');
		return result.toString();
	}

} // end of BaseFuelCostImpl

// finish type fixing
