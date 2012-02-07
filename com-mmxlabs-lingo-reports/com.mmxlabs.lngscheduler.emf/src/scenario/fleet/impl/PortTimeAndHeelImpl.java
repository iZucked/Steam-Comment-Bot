/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.fleet.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import scenario.fleet.FleetPackage;
import scenario.fleet.HeelOptions;
import scenario.fleet.PortTimeAndHeel;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Port Time And Heel</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link scenario.fleet.impl.PortTimeAndHeelImpl#getHeelLimit <em>Heel Limit</em>}</li>
 * <li>{@link scenario.fleet.impl.PortTimeAndHeelImpl#getHeelCVValue <em>Heel CV Value</em>}</li>
 * <li>{@link scenario.fleet.impl.PortTimeAndHeelImpl#getHeelUnitPrice <em>Heel Unit Price</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class PortTimeAndHeelImpl extends PortAndTimeImpl implements PortTimeAndHeel {
	/**
	 * The default value of the '{@link #getHeelLimit() <em>Heel Limit</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getHeelLimit()
	 * @generated
	 * @ordered
	 */
	protected static final int HEEL_LIMIT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getHeelLimit() <em>Heel Limit</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getHeelLimit()
	 * @generated
	 * @ordered
	 */
	protected int heelLimit = HEEL_LIMIT_EDEFAULT;

	/**
	 * This is true if the Heel Limit attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean heelLimitESet;

	/**
	 * The default value of the '{@link #getHeelCVValue() <em>Heel CV Value</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getHeelCVValue()
	 * @generated
	 * @ordered
	 */
	protected static final float HEEL_CV_VALUE_EDEFAULT = 22.8F;

	/**
	 * The cached value of the '{@link #getHeelCVValue() <em>Heel CV Value</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getHeelCVValue()
	 * @generated
	 * @ordered
	 */
	protected float heelCVValue = HEEL_CV_VALUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getHeelUnitPrice() <em>Heel Unit Price</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getHeelUnitPrice()
	 * @generated
	 * @ordered
	 */
	protected static final float HEEL_UNIT_PRICE_EDEFAULT = 0.0F;

	/**
	 * The cached value of the '{@link #getHeelUnitPrice() <em>Heel Unit Price</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getHeelUnitPrice()
	 * @generated
	 * @ordered
	 */
	protected float heelUnitPrice = HEEL_UNIT_PRICE_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected PortTimeAndHeelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.PORT_TIME_AND_HEEL;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int getHeelLimit() {
		return heelLimit;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setHeelLimit(final int newHeelLimit) {
		final int oldHeelLimit = heelLimit;
		heelLimit = newHeelLimit;
		final boolean oldHeelLimitESet = heelLimitESet;
		heelLimitESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.PORT_TIME_AND_HEEL__HEEL_LIMIT, oldHeelLimit, heelLimit, !oldHeelLimitESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void unsetHeelLimit() {
		final int oldHeelLimit = heelLimit;
		final boolean oldHeelLimitESet = heelLimitESet;
		heelLimit = HEEL_LIMIT_EDEFAULT;
		heelLimitESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.PORT_TIME_AND_HEEL__HEEL_LIMIT, oldHeelLimit, HEEL_LIMIT_EDEFAULT, oldHeelLimitESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean isSetHeelLimit() {
		return heelLimitESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public float getHeelCVValue() {
		return heelCVValue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setHeelCVValue(final float newHeelCVValue) {
		final float oldHeelCVValue = heelCVValue;
		heelCVValue = newHeelCVValue;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.PORT_TIME_AND_HEEL__HEEL_CV_VALUE, oldHeelCVValue, heelCVValue));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public float getHeelUnitPrice() {
		return heelUnitPrice;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setHeelUnitPrice(final float newHeelUnitPrice) {
		final float oldHeelUnitPrice = heelUnitPrice;
		heelUnitPrice = newHeelUnitPrice;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.PORT_TIME_AND_HEEL__HEEL_UNIT_PRICE, oldHeelUnitPrice, heelUnitPrice));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(final int featureID, final boolean resolve, final boolean coreType) {
		switch (featureID) {
		case FleetPackage.PORT_TIME_AND_HEEL__HEEL_LIMIT:
			return getHeelLimit();
		case FleetPackage.PORT_TIME_AND_HEEL__HEEL_CV_VALUE:
			return getHeelCVValue();
		case FleetPackage.PORT_TIME_AND_HEEL__HEEL_UNIT_PRICE:
			return getHeelUnitPrice();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eSet(final int featureID, final Object newValue) {
		switch (featureID) {
		case FleetPackage.PORT_TIME_AND_HEEL__HEEL_LIMIT:
			setHeelLimit((Integer) newValue);
			return;
		case FleetPackage.PORT_TIME_AND_HEEL__HEEL_CV_VALUE:
			setHeelCVValue((Float) newValue);
			return;
		case FleetPackage.PORT_TIME_AND_HEEL__HEEL_UNIT_PRICE:
			setHeelUnitPrice((Float) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(final int featureID) {
		switch (featureID) {
		case FleetPackage.PORT_TIME_AND_HEEL__HEEL_LIMIT:
			unsetHeelLimit();
			return;
		case FleetPackage.PORT_TIME_AND_HEEL__HEEL_CV_VALUE:
			setHeelCVValue(HEEL_CV_VALUE_EDEFAULT);
			return;
		case FleetPackage.PORT_TIME_AND_HEEL__HEEL_UNIT_PRICE:
			setHeelUnitPrice(HEEL_UNIT_PRICE_EDEFAULT);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(final int featureID) {
		switch (featureID) {
		case FleetPackage.PORT_TIME_AND_HEEL__HEEL_LIMIT:
			return isSetHeelLimit();
		case FleetPackage.PORT_TIME_AND_HEEL__HEEL_CV_VALUE:
			return heelCVValue != HEEL_CV_VALUE_EDEFAULT;
		case FleetPackage.PORT_TIME_AND_HEEL__HEEL_UNIT_PRICE:
			return heelUnitPrice != HEEL_UNIT_PRICE_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(final int derivedFeatureID, final Class<?> baseClass) {
		if (baseClass == HeelOptions.class) {
			switch (derivedFeatureID) {
			case FleetPackage.PORT_TIME_AND_HEEL__HEEL_LIMIT:
				return FleetPackage.HEEL_OPTIONS__HEEL_LIMIT;
			case FleetPackage.PORT_TIME_AND_HEEL__HEEL_CV_VALUE:
				return FleetPackage.HEEL_OPTIONS__HEEL_CV_VALUE;
			case FleetPackage.PORT_TIME_AND_HEEL__HEEL_UNIT_PRICE:
				return FleetPackage.HEEL_OPTIONS__HEEL_UNIT_PRICE;
			default:
				return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(final int baseFeatureID, final Class<?> baseClass) {
		if (baseClass == HeelOptions.class) {
			switch (baseFeatureID) {
			case FleetPackage.HEEL_OPTIONS__HEEL_LIMIT:
				return FleetPackage.PORT_TIME_AND_HEEL__HEEL_LIMIT;
			case FleetPackage.HEEL_OPTIONS__HEEL_CV_VALUE:
				return FleetPackage.PORT_TIME_AND_HEEL__HEEL_CV_VALUE;
			case FleetPackage.HEEL_OPTIONS__HEEL_UNIT_PRICE:
				return FleetPackage.PORT_TIME_AND_HEEL__HEEL_UNIT_PRICE;
			default:
				return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) {
			return super.toString();
		}

		final StringBuffer result = new StringBuffer(super.toString());
		result.append(" (heelLimit: ");
		if (heelLimitESet) {
			result.append(heelLimit);
		} else {
			result.append("<unset>");
		}
		result.append(", heelCVValue: ");
		result.append(heelCVValue);
		result.append(", heelUnitPrice: ");
		result.append(heelUnitPrice);
		result.append(')');
		return result.toString();
	}

} // PortTimeAndHeelImpl
