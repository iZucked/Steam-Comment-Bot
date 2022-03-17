/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.commercial.impl;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.NotionalJourneyTerm;
import com.mmxlabs.models.lng.commercial.OriginPortRepositioningFeeTerm;

import com.mmxlabs.models.lng.port.Port;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Origin Port Repositioning Fee Term</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.OriginPortRepositioningFeeTermImpl#getSpeed <em>Speed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.OriginPortRepositioningFeeTermImpl#getFuelPriceExpression <em>Fuel Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.OriginPortRepositioningFeeTermImpl#getHirePriceExpression <em>Hire Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.OriginPortRepositioningFeeTermImpl#isIncludeCanal <em>Include Canal</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.OriginPortRepositioningFeeTermImpl#isIncludeCanalTime <em>Include Canal Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.OriginPortRepositioningFeeTermImpl#getLumpSumPriceExpression <em>Lump Sum Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.OriginPortRepositioningFeeTermImpl#getOriginPort <em>Origin Port</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OriginPortRepositioningFeeTermImpl extends RepositioningFeeTermImpl implements OriginPortRepositioningFeeTerm {
	/**
	 * The default value of the '{@link #getSpeed() <em>Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpeed()
	 * @generated
	 * @ordered
	 */
	protected static final double SPEED_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getSpeed() <em>Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpeed()
	 * @generated
	 * @ordered
	 */
	protected double speed = SPEED_EDEFAULT;

	/**
	 * The default value of the '{@link #getFuelPriceExpression() <em>Fuel Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFuelPriceExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String FUEL_PRICE_EXPRESSION_EDEFAULT = "";

	/**
	 * The cached value of the '{@link #getFuelPriceExpression() <em>Fuel Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFuelPriceExpression()
	 * @generated
	 * @ordered
	 */
	protected String fuelPriceExpression = FUEL_PRICE_EXPRESSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getHirePriceExpression() <em>Hire Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHirePriceExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String HIRE_PRICE_EXPRESSION_EDEFAULT = "";

	/**
	 * The cached value of the '{@link #getHirePriceExpression() <em>Hire Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHirePriceExpression()
	 * @generated
	 * @ordered
	 */
	protected String hirePriceExpression = HIRE_PRICE_EXPRESSION_EDEFAULT;

	/**
	 * The default value of the '{@link #isIncludeCanal() <em>Include Canal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIncludeCanal()
	 * @generated
	 * @ordered
	 */
	protected static final boolean INCLUDE_CANAL_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIncludeCanal() <em>Include Canal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIncludeCanal()
	 * @generated
	 * @ordered
	 */
	protected boolean includeCanal = INCLUDE_CANAL_EDEFAULT;

	/**
	 * The default value of the '{@link #isIncludeCanalTime() <em>Include Canal Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIncludeCanalTime()
	 * @generated
	 * @ordered
	 */
	protected static final boolean INCLUDE_CANAL_TIME_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isIncludeCanalTime() <em>Include Canal Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIncludeCanalTime()
	 * @generated
	 * @ordered
	 */
	protected boolean includeCanalTime = INCLUDE_CANAL_TIME_EDEFAULT;

	/**
	 * The default value of the '{@link #getLumpSumPriceExpression() <em>Lump Sum Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLumpSumPriceExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String LUMP_SUM_PRICE_EXPRESSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLumpSumPriceExpression() <em>Lump Sum Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLumpSumPriceExpression()
	 * @generated
	 * @ordered
	 */
	protected String lumpSumPriceExpression = LUMP_SUM_PRICE_EXPRESSION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getOriginPort() <em>Origin Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginPort()
	 * @generated
	 * @ordered
	 */
	protected Port originPort;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OriginPortRepositioningFeeTermImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CommercialPackage.Literals.ORIGIN_PORT_REPOSITIONING_FEE_TERM;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getSpeed() {
		return speed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSpeed(double newSpeed) {
		double oldSpeed = speed;
		speed = newSpeed;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__SPEED, oldSpeed, speed));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getFuelPriceExpression() {
		return fuelPriceExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFuelPriceExpression(String newFuelPriceExpression) {
		String oldFuelPriceExpression = fuelPriceExpression;
		fuelPriceExpression = newFuelPriceExpression;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__FUEL_PRICE_EXPRESSION, oldFuelPriceExpression, fuelPriceExpression));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getHirePriceExpression() {
		return hirePriceExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setHirePriceExpression(String newHirePriceExpression) {
		String oldHirePriceExpression = hirePriceExpression;
		hirePriceExpression = newHirePriceExpression;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__HIRE_PRICE_EXPRESSION, oldHirePriceExpression, hirePriceExpression));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isIncludeCanal() {
		return includeCanal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setIncludeCanal(boolean newIncludeCanal) {
		boolean oldIncludeCanal = includeCanal;
		includeCanal = newIncludeCanal;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__INCLUDE_CANAL, oldIncludeCanal, includeCanal));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isIncludeCanalTime() {
		return includeCanalTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setIncludeCanalTime(boolean newIncludeCanalTime) {
		boolean oldIncludeCanalTime = includeCanalTime;
		includeCanalTime = newIncludeCanalTime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__INCLUDE_CANAL_TIME, oldIncludeCanalTime, includeCanalTime));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getLumpSumPriceExpression() {
		return lumpSumPriceExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLumpSumPriceExpression(String newLumpSumPriceExpression) {
		String oldLumpSumPriceExpression = lumpSumPriceExpression;
		lumpSumPriceExpression = newLumpSumPriceExpression;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__LUMP_SUM_PRICE_EXPRESSION, oldLumpSumPriceExpression, lumpSumPriceExpression));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Port getOriginPort() {
		if (originPort != null && originPort.eIsProxy()) {
			InternalEObject oldOriginPort = (InternalEObject)originPort;
			originPort = (Port)eResolveProxy(oldOriginPort);
			if (originPort != oldOriginPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__ORIGIN_PORT, oldOriginPort, originPort));
			}
		}
		return originPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetOriginPort() {
		return originPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOriginPort(Port newOriginPort) {
		Port oldOriginPort = originPort;
		originPort = newOriginPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__ORIGIN_PORT, oldOriginPort, originPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__SPEED:
				return getSpeed();
			case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__FUEL_PRICE_EXPRESSION:
				return getFuelPriceExpression();
			case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__HIRE_PRICE_EXPRESSION:
				return getHirePriceExpression();
			case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__INCLUDE_CANAL:
				return isIncludeCanal();
			case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__INCLUDE_CANAL_TIME:
				return isIncludeCanalTime();
			case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__LUMP_SUM_PRICE_EXPRESSION:
				return getLumpSumPriceExpression();
			case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__ORIGIN_PORT:
				if (resolve) return getOriginPort();
				return basicGetOriginPort();
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
			case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__SPEED:
				setSpeed((Double)newValue);
				return;
			case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__FUEL_PRICE_EXPRESSION:
				setFuelPriceExpression((String)newValue);
				return;
			case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__HIRE_PRICE_EXPRESSION:
				setHirePriceExpression((String)newValue);
				return;
			case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__INCLUDE_CANAL:
				setIncludeCanal((Boolean)newValue);
				return;
			case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__INCLUDE_CANAL_TIME:
				setIncludeCanalTime((Boolean)newValue);
				return;
			case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__LUMP_SUM_PRICE_EXPRESSION:
				setLumpSumPriceExpression((String)newValue);
				return;
			case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__ORIGIN_PORT:
				setOriginPort((Port)newValue);
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
			case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__SPEED:
				setSpeed(SPEED_EDEFAULT);
				return;
			case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__FUEL_PRICE_EXPRESSION:
				setFuelPriceExpression(FUEL_PRICE_EXPRESSION_EDEFAULT);
				return;
			case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__HIRE_PRICE_EXPRESSION:
				setHirePriceExpression(HIRE_PRICE_EXPRESSION_EDEFAULT);
				return;
			case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__INCLUDE_CANAL:
				setIncludeCanal(INCLUDE_CANAL_EDEFAULT);
				return;
			case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__INCLUDE_CANAL_TIME:
				setIncludeCanalTime(INCLUDE_CANAL_TIME_EDEFAULT);
				return;
			case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__LUMP_SUM_PRICE_EXPRESSION:
				setLumpSumPriceExpression(LUMP_SUM_PRICE_EXPRESSION_EDEFAULT);
				return;
			case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__ORIGIN_PORT:
				setOriginPort((Port)null);
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
			case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__SPEED:
				return speed != SPEED_EDEFAULT;
			case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__FUEL_PRICE_EXPRESSION:
				return FUEL_PRICE_EXPRESSION_EDEFAULT == null ? fuelPriceExpression != null : !FUEL_PRICE_EXPRESSION_EDEFAULT.equals(fuelPriceExpression);
			case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__HIRE_PRICE_EXPRESSION:
				return HIRE_PRICE_EXPRESSION_EDEFAULT == null ? hirePriceExpression != null : !HIRE_PRICE_EXPRESSION_EDEFAULT.equals(hirePriceExpression);
			case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__INCLUDE_CANAL:
				return includeCanal != INCLUDE_CANAL_EDEFAULT;
			case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__INCLUDE_CANAL_TIME:
				return includeCanalTime != INCLUDE_CANAL_TIME_EDEFAULT;
			case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__LUMP_SUM_PRICE_EXPRESSION:
				return LUMP_SUM_PRICE_EXPRESSION_EDEFAULT == null ? lumpSumPriceExpression != null : !LUMP_SUM_PRICE_EXPRESSION_EDEFAULT.equals(lumpSumPriceExpression);
			case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__ORIGIN_PORT:
				return originPort != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == NotionalJourneyTerm.class) {
			switch (derivedFeatureID) {
				case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__SPEED: return CommercialPackage.NOTIONAL_JOURNEY_TERM__SPEED;
				case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__FUEL_PRICE_EXPRESSION: return CommercialPackage.NOTIONAL_JOURNEY_TERM__FUEL_PRICE_EXPRESSION;
				case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__HIRE_PRICE_EXPRESSION: return CommercialPackage.NOTIONAL_JOURNEY_TERM__HIRE_PRICE_EXPRESSION;
				case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__INCLUDE_CANAL: return CommercialPackage.NOTIONAL_JOURNEY_TERM__INCLUDE_CANAL;
				case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__INCLUDE_CANAL_TIME: return CommercialPackage.NOTIONAL_JOURNEY_TERM__INCLUDE_CANAL_TIME;
				case CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__LUMP_SUM_PRICE_EXPRESSION: return CommercialPackage.NOTIONAL_JOURNEY_TERM__LUMP_SUM_PRICE_EXPRESSION;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == NotionalJourneyTerm.class) {
			switch (baseFeatureID) {
				case CommercialPackage.NOTIONAL_JOURNEY_TERM__SPEED: return CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__SPEED;
				case CommercialPackage.NOTIONAL_JOURNEY_TERM__FUEL_PRICE_EXPRESSION: return CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__FUEL_PRICE_EXPRESSION;
				case CommercialPackage.NOTIONAL_JOURNEY_TERM__HIRE_PRICE_EXPRESSION: return CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__HIRE_PRICE_EXPRESSION;
				case CommercialPackage.NOTIONAL_JOURNEY_TERM__INCLUDE_CANAL: return CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__INCLUDE_CANAL;
				case CommercialPackage.NOTIONAL_JOURNEY_TERM__INCLUDE_CANAL_TIME: return CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__INCLUDE_CANAL_TIME;
				case CommercialPackage.NOTIONAL_JOURNEY_TERM__LUMP_SUM_PRICE_EXPRESSION: return CommercialPackage.ORIGIN_PORT_REPOSITIONING_FEE_TERM__LUMP_SUM_PRICE_EXPRESSION;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
		result.append(" (speed: ");
		result.append(speed);
		result.append(", fuelPriceExpression: ");
		result.append(fuelPriceExpression);
		result.append(", hirePriceExpression: ");
		result.append(hirePriceExpression);
		result.append(", includeCanal: ");
		result.append(includeCanal);
		result.append(", includeCanalTime: ");
		result.append(includeCanalTime);
		result.append(", lumpSumPriceExpression: ");
		result.append(lumpSumPriceExpression);
		result.append(')');
		return result.toString();
	}

} //OriginPortRepositioningFeeTermImpl
