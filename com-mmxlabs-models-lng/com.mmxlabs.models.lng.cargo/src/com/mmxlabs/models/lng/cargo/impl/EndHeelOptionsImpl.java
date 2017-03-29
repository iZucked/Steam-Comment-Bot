/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.EVesselTankState;
import com.mmxlabs.models.lng.cargo.EndHeelOptions;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>End Heel Options</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.EndHeelOptionsImpl#getTankState <em>Tank State</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.EndHeelOptionsImpl#getMinimumEndHeel <em>Minimum End Heel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.EndHeelOptionsImpl#getMaximumEndHeel <em>Maximum End Heel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.EndHeelOptionsImpl#getPriceExpression <em>Price Expression</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EndHeelOptionsImpl extends EObjectImpl implements EndHeelOptions {
	/**
	 * The default value of the '{@link #getTankState() <em>Tank State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTankState()
	 * @generated
	 * @ordered
	 */
	protected static final EVesselTankState TANK_STATE_EDEFAULT = EVesselTankState.EITHER;

	/**
	 * The cached value of the '{@link #getTankState() <em>Tank State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTankState()
	 * @generated
	 * @ordered
	 */
	protected EVesselTankState tankState = TANK_STATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getMinimumEndHeel() <em>Minimum End Heel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinimumEndHeel()
	 * @generated
	 * @ordered
	 */
	protected static final int MINIMUM_END_HEEL_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMinimumEndHeel() <em>Minimum End Heel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinimumEndHeel()
	 * @generated
	 * @ordered
	 */
	protected int minimumEndHeel = MINIMUM_END_HEEL_EDEFAULT;

	/**
	 * The default value of the '{@link #getMaximumEndHeel() <em>Maximum End Heel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaximumEndHeel()
	 * @generated
	 * @ordered
	 */
	protected static final int MAXIMUM_END_HEEL_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMaximumEndHeel() <em>Maximum End Heel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaximumEndHeel()
	 * @generated
	 * @ordered
	 */
	protected int maximumEndHeel = MAXIMUM_END_HEEL_EDEFAULT;

	/**
	 * The default value of the '{@link #getPriceExpression() <em>Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriceExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String PRICE_EXPRESSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPriceExpression() <em>Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriceExpression()
	 * @generated
	 * @ordered
	 */
	protected String priceExpression = PRICE_EXPRESSION_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EndHeelOptionsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.END_HEEL_OPTIONS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EVesselTankState getTankState() {
		return tankState;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTankState(EVesselTankState newTankState) {
		EVesselTankState oldTankState = tankState;
		tankState = newTankState == null ? TANK_STATE_EDEFAULT : newTankState;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.END_HEEL_OPTIONS__TANK_STATE, oldTankState, tankState));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMinimumEndHeel() {
		return minimumEndHeel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinimumEndHeel(int newMinimumEndHeel) {
		int oldMinimumEndHeel = minimumEndHeel;
		minimumEndHeel = newMinimumEndHeel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.END_HEEL_OPTIONS__MINIMUM_END_HEEL, oldMinimumEndHeel, minimumEndHeel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMaximumEndHeel() {
		return maximumEndHeel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaximumEndHeel(int newMaximumEndHeel) {
		int oldMaximumEndHeel = maximumEndHeel;
		maximumEndHeel = newMaximumEndHeel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.END_HEEL_OPTIONS__MAXIMUM_END_HEEL, oldMaximumEndHeel, maximumEndHeel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPriceExpression() {
		return priceExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPriceExpression(String newPriceExpression) {
		String oldPriceExpression = priceExpression;
		priceExpression = newPriceExpression;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.END_HEEL_OPTIONS__PRICE_EXPRESSION, oldPriceExpression, priceExpression));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoPackage.END_HEEL_OPTIONS__TANK_STATE:
				return getTankState();
			case CargoPackage.END_HEEL_OPTIONS__MINIMUM_END_HEEL:
				return getMinimumEndHeel();
			case CargoPackage.END_HEEL_OPTIONS__MAXIMUM_END_HEEL:
				return getMaximumEndHeel();
			case CargoPackage.END_HEEL_OPTIONS__PRICE_EXPRESSION:
				return getPriceExpression();
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
			case CargoPackage.END_HEEL_OPTIONS__TANK_STATE:
				setTankState((EVesselTankState)newValue);
				return;
			case CargoPackage.END_HEEL_OPTIONS__MINIMUM_END_HEEL:
				setMinimumEndHeel((Integer)newValue);
				return;
			case CargoPackage.END_HEEL_OPTIONS__MAXIMUM_END_HEEL:
				setMaximumEndHeel((Integer)newValue);
				return;
			case CargoPackage.END_HEEL_OPTIONS__PRICE_EXPRESSION:
				setPriceExpression((String)newValue);
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
			case CargoPackage.END_HEEL_OPTIONS__TANK_STATE:
				setTankState(TANK_STATE_EDEFAULT);
				return;
			case CargoPackage.END_HEEL_OPTIONS__MINIMUM_END_HEEL:
				setMinimumEndHeel(MINIMUM_END_HEEL_EDEFAULT);
				return;
			case CargoPackage.END_HEEL_OPTIONS__MAXIMUM_END_HEEL:
				setMaximumEndHeel(MAXIMUM_END_HEEL_EDEFAULT);
				return;
			case CargoPackage.END_HEEL_OPTIONS__PRICE_EXPRESSION:
				setPriceExpression(PRICE_EXPRESSION_EDEFAULT);
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
			case CargoPackage.END_HEEL_OPTIONS__TANK_STATE:
				return tankState != TANK_STATE_EDEFAULT;
			case CargoPackage.END_HEEL_OPTIONS__MINIMUM_END_HEEL:
				return minimumEndHeel != MINIMUM_END_HEEL_EDEFAULT;
			case CargoPackage.END_HEEL_OPTIONS__MAXIMUM_END_HEEL:
				return maximumEndHeel != MAXIMUM_END_HEEL_EDEFAULT;
			case CargoPackage.END_HEEL_OPTIONS__PRICE_EXPRESSION:
				return PRICE_EXPRESSION_EDEFAULT == null ? priceExpression != null : !PRICE_EXPRESSION_EDEFAULT.equals(priceExpression);
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
		result.append(" (tankState: ");
		result.append(tankState);
		result.append(", minimumEndHeel: ");
		result.append(minimumEndHeel);
		result.append(", maximumEndHeel: ");
		result.append(maximumEndHeel);
		result.append(", priceExpression: ");
		result.append(priceExpression);
		result.append(')');
		return result.toString();
	}

} //EndHeelOptionsImpl
