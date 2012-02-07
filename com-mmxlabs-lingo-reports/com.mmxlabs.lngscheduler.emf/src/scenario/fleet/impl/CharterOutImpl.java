/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.fleet.impl;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import scenario.fleet.CharterOut;
import scenario.fleet.FleetPackage;
import scenario.fleet.HeelOptions;
import scenario.port.Port;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Charter Out</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link scenario.fleet.impl.CharterOutImpl#getHeelLimit <em>Heel Limit</em>}</li>
 * <li>{@link scenario.fleet.impl.CharterOutImpl#getHeelCVValue <em>Heel CV Value</em>}</li>
 * <li>{@link scenario.fleet.impl.CharterOutImpl#getHeelUnitPrice <em>Heel Unit Price</em>}</li>
 * <li>{@link scenario.fleet.impl.CharterOutImpl#getEndPort <em>End Port</em>}</li>
 * <li>{@link scenario.fleet.impl.CharterOutImpl#getDailyCharterOutPrice <em>Daily Charter Out Price</em>}</li>
 * <li>{@link scenario.fleet.impl.CharterOutImpl#getRepositioningFee <em>Repositioning Fee</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class CharterOutImpl extends VesselEventImpl implements CharterOut {
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
	 * The cached value of the '{@link #getEndPort() <em>End Port</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getEndPort()
	 * @generated
	 * @ordered
	 */
	protected Port endPort;

	/**
	 * This is true if the End Port reference has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean endPortESet;
	/**
	 * The default value of the '{@link #getDailyCharterOutPrice() <em>Daily Charter Out Price</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDailyCharterOutPrice()
	 * @generated
	 * @ordered
	 */
	protected static final int DAILY_CHARTER_OUT_PRICE_EDEFAULT = 0;
	/**
	 * The cached value of the '{@link #getDailyCharterOutPrice() <em>Daily Charter Out Price</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDailyCharterOutPrice()
	 * @generated
	 * @ordered
	 */
	protected int dailyCharterOutPrice = DAILY_CHARTER_OUT_PRICE_EDEFAULT;
	/**
	 * This is true if the Daily Charter Out Price attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean dailyCharterOutPriceESet;
	/**
	 * The default value of the '{@link #getRepositioningFee() <em>Repositioning Fee</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRepositioningFee()
	 * @generated
	 * @ordered
	 */
	protected static final int REPOSITIONING_FEE_EDEFAULT = 0;
	/**
	 * The cached value of the '{@link #getRepositioningFee() <em>Repositioning Fee</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRepositioningFee()
	 * @generated
	 * @ordered
	 */
	protected int repositioningFee = REPOSITIONING_FEE_EDEFAULT;
	/**
	 * This is true if the Repositioning Fee attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean repositioningFeeESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected CharterOutImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.CHARTER_OUT;
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
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.CHARTER_OUT__HEEL_LIMIT, oldHeelLimit, heelLimit, !oldHeelLimitESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.CHARTER_OUT__HEEL_LIMIT, oldHeelLimit, HEEL_LIMIT_EDEFAULT, oldHeelLimitESet));
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
	public Port getEndPort() {
		if ((endPort != null) && endPort.eIsProxy()) {
			final InternalEObject oldEndPort = (InternalEObject) endPort;
			endPort = (Port) eResolveProxy(oldEndPort);
			if (endPort != oldEndPort) {
				if (eNotificationRequired()) {
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, FleetPackage.CHARTER_OUT__END_PORT, oldEndPort, endPort));
				}
			}
		}
		return endPort;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Port basicGetEndPort() {
		return endPort;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setEndPort(final Port newEndPort) {
		final Port oldEndPort = endPort;
		endPort = newEndPort;
		final boolean oldEndPortESet = endPortESet;
		endPortESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.CHARTER_OUT__END_PORT, oldEndPort, endPort, !oldEndPortESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void unsetEndPort() {
		final Port oldEndPort = endPort;
		final boolean oldEndPortESet = endPortESet;
		endPort = null;
		endPortESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.CHARTER_OUT__END_PORT, oldEndPort, null, oldEndPortESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean isSetEndPort() {
		return endPortESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int getDailyCharterOutPrice() {
		return dailyCharterOutPrice;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setDailyCharterOutPrice(final int newDailyCharterOutPrice) {
		final int oldDailyCharterOutPrice = dailyCharterOutPrice;
		dailyCharterOutPrice = newDailyCharterOutPrice;
		final boolean oldDailyCharterOutPriceESet = dailyCharterOutPriceESet;
		dailyCharterOutPriceESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.CHARTER_OUT__DAILY_CHARTER_OUT_PRICE, oldDailyCharterOutPrice, dailyCharterOutPrice, !oldDailyCharterOutPriceESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void unsetDailyCharterOutPrice() {
		final int oldDailyCharterOutPrice = dailyCharterOutPrice;
		final boolean oldDailyCharterOutPriceESet = dailyCharterOutPriceESet;
		dailyCharterOutPrice = DAILY_CHARTER_OUT_PRICE_EDEFAULT;
		dailyCharterOutPriceESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.CHARTER_OUT__DAILY_CHARTER_OUT_PRICE, oldDailyCharterOutPrice, DAILY_CHARTER_OUT_PRICE_EDEFAULT,
					oldDailyCharterOutPriceESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean isSetDailyCharterOutPrice() {
		return dailyCharterOutPriceESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int getRepositioningFee() {
		return repositioningFee;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setRepositioningFee(final int newRepositioningFee) {
		final int oldRepositioningFee = repositioningFee;
		repositioningFee = newRepositioningFee;
		final boolean oldRepositioningFeeESet = repositioningFeeESet;
		repositioningFeeESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.CHARTER_OUT__REPOSITIONING_FEE, oldRepositioningFee, repositioningFee, !oldRepositioningFeeESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void unsetRepositioningFee() {
		final int oldRepositioningFee = repositioningFee;
		final boolean oldRepositioningFeeESet = repositioningFeeESet;
		repositioningFee = REPOSITIONING_FEE_EDEFAULT;
		repositioningFeeESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.CHARTER_OUT__REPOSITIONING_FEE, oldRepositioningFee, REPOSITIONING_FEE_EDEFAULT, oldRepositioningFeeESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean isSetRepositioningFee() {
		return repositioningFeeESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Port getEffectiveEndPort() {
		if (isSetEndPort()) {
			return getEndPort();
		} else {
			return getStartPort();
		}
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
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.CHARTER_OUT__HEEL_CV_VALUE, oldHeelCVValue, heelCVValue));
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
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.CHARTER_OUT__HEEL_UNIT_PRICE, oldHeelUnitPrice, heelUnitPrice));
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
		case FleetPackage.CHARTER_OUT__HEEL_LIMIT:
			return getHeelLimit();
		case FleetPackage.CHARTER_OUT__HEEL_CV_VALUE:
			return getHeelCVValue();
		case FleetPackage.CHARTER_OUT__HEEL_UNIT_PRICE:
			return getHeelUnitPrice();
		case FleetPackage.CHARTER_OUT__END_PORT:
			if (resolve) {
				return getEndPort();
			}
			return basicGetEndPort();
		case FleetPackage.CHARTER_OUT__DAILY_CHARTER_OUT_PRICE:
			return getDailyCharterOutPrice();
		case FleetPackage.CHARTER_OUT__REPOSITIONING_FEE:
			return getRepositioningFee();
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
		case FleetPackage.CHARTER_OUT__HEEL_LIMIT:
			setHeelLimit((Integer) newValue);
			return;
		case FleetPackage.CHARTER_OUT__HEEL_CV_VALUE:
			setHeelCVValue((Float) newValue);
			return;
		case FleetPackage.CHARTER_OUT__HEEL_UNIT_PRICE:
			setHeelUnitPrice((Float) newValue);
			return;
		case FleetPackage.CHARTER_OUT__END_PORT:
			setEndPort((Port) newValue);
			return;
		case FleetPackage.CHARTER_OUT__DAILY_CHARTER_OUT_PRICE:
			setDailyCharterOutPrice((Integer) newValue);
			return;
		case FleetPackage.CHARTER_OUT__REPOSITIONING_FEE:
			setRepositioningFee((Integer) newValue);
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
		case FleetPackage.CHARTER_OUT__HEEL_LIMIT:
			unsetHeelLimit();
			return;
		case FleetPackage.CHARTER_OUT__HEEL_CV_VALUE:
			setHeelCVValue(HEEL_CV_VALUE_EDEFAULT);
			return;
		case FleetPackage.CHARTER_OUT__HEEL_UNIT_PRICE:
			setHeelUnitPrice(HEEL_UNIT_PRICE_EDEFAULT);
			return;
		case FleetPackage.CHARTER_OUT__END_PORT:
			unsetEndPort();
			return;
		case FleetPackage.CHARTER_OUT__DAILY_CHARTER_OUT_PRICE:
			unsetDailyCharterOutPrice();
			return;
		case FleetPackage.CHARTER_OUT__REPOSITIONING_FEE:
			unsetRepositioningFee();
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
		case FleetPackage.CHARTER_OUT__HEEL_LIMIT:
			return isSetHeelLimit();
		case FleetPackage.CHARTER_OUT__HEEL_CV_VALUE:
			return heelCVValue != HEEL_CV_VALUE_EDEFAULT;
		case FleetPackage.CHARTER_OUT__HEEL_UNIT_PRICE:
			return heelUnitPrice != HEEL_UNIT_PRICE_EDEFAULT;
		case FleetPackage.CHARTER_OUT__END_PORT:
			return isSetEndPort();
		case FleetPackage.CHARTER_OUT__DAILY_CHARTER_OUT_PRICE:
			return isSetDailyCharterOutPrice();
		case FleetPackage.CHARTER_OUT__REPOSITIONING_FEE:
			return isSetRepositioningFee();
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
			case FleetPackage.CHARTER_OUT__HEEL_LIMIT:
				return FleetPackage.HEEL_OPTIONS__HEEL_LIMIT;
			case FleetPackage.CHARTER_OUT__HEEL_CV_VALUE:
				return FleetPackage.HEEL_OPTIONS__HEEL_CV_VALUE;
			case FleetPackage.CHARTER_OUT__HEEL_UNIT_PRICE:
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
				return FleetPackage.CHARTER_OUT__HEEL_LIMIT;
			case FleetPackage.HEEL_OPTIONS__HEEL_CV_VALUE:
				return FleetPackage.CHARTER_OUT__HEEL_CV_VALUE;
			case FleetPackage.HEEL_OPTIONS__HEEL_UNIT_PRICE:
				return FleetPackage.CHARTER_OUT__HEEL_UNIT_PRICE;
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
	public Object eInvoke(final int operationID, final EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
		case FleetPackage.CHARTER_OUT___GET_EFFECTIVE_END_PORT:
			return getEffectiveEndPort();
		}
		return super.eInvoke(operationID, arguments);
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
		result.append(", dailyCharterOutPrice: ");
		if (dailyCharterOutPriceESet) {
			result.append(dailyCharterOutPrice);
		} else {
			result.append("<unset>");
		}
		result.append(", repositioningFee: ");
		if (repositioningFeeESet) {
			result.append(repositioningFee);
		} else {
			result.append("<unset>");
		}
		result.append(')');
		return result.toString();
	}

} // CharterOutImpl
