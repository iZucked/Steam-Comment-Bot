/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.commercial.impl;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusTerm;
import com.mmxlabs.models.lng.commercial.NotionalJourneyTerm;

import com.mmxlabs.models.lng.port.Port;

import com.mmxlabs.models.lng.types.APortSet;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Notional Journey Ballast Bonus Term</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.NotionalJourneyBallastBonusTermImpl#getSpeed <em>Speed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.NotionalJourneyBallastBonusTermImpl#getFuelPriceExpression <em>Fuel Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.NotionalJourneyBallastBonusTermImpl#getHirePriceExpression <em>Hire Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.NotionalJourneyBallastBonusTermImpl#isIncludeCanal <em>Include Canal</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.NotionalJourneyBallastBonusTermImpl#isIncludeCanalTime <em>Include Canal Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.NotionalJourneyBallastBonusTermImpl#getLumpSumPriceExpression <em>Lump Sum Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.NotionalJourneyBallastBonusTermImpl#isPriceOnLastLNGPrice <em>Price On Last LNG Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.NotionalJourneyBallastBonusTermImpl#getReturnPorts <em>Return Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.NotionalJourneyBallastBonusTermImpl#isIsFirstLoadPort <em>Is First Load Port</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NotionalJourneyBallastBonusTermImpl extends BallastBonusTermImpl implements NotionalJourneyBallastBonusTerm {
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
	 * The default value of the '{@link #isPriceOnLastLNGPrice() <em>Price On Last LNG Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isPriceOnLastLNGPrice()
	 * @generated
	 * @ordered
	 */
	protected static final boolean PRICE_ON_LAST_LNG_PRICE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isPriceOnLastLNGPrice() <em>Price On Last LNG Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isPriceOnLastLNGPrice()
	 * @generated
	 * @ordered
	 */
	protected boolean priceOnLastLNGPrice = PRICE_ON_LAST_LNG_PRICE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getReturnPorts() <em>Return Ports</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReturnPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet<Port>> returnPorts;

	/**
	 * The default value of the '{@link #isIsFirstLoadPort() <em>Is First Load Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsFirstLoadPort()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_FIRST_LOAD_PORT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIsFirstLoadPort() <em>Is First Load Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsFirstLoadPort()
	 * @generated
	 * @ordered
	 */
	protected boolean isFirstLoadPort = IS_FIRST_LOAD_PORT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NotionalJourneyBallastBonusTermImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CommercialPackage.Literals.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM;
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
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__SPEED, oldSpeed, speed));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__FUEL_PRICE_EXPRESSION, oldFuelPriceExpression, fuelPriceExpression));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__HIRE_PRICE_EXPRESSION, oldHirePriceExpression, hirePriceExpression));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__INCLUDE_CANAL, oldIncludeCanal, includeCanal));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__INCLUDE_CANAL_TIME, oldIncludeCanalTime, includeCanalTime));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__LUMP_SUM_PRICE_EXPRESSION, oldLumpSumPriceExpression, lumpSumPriceExpression));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isPriceOnLastLNGPrice() {
		return priceOnLastLNGPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPriceOnLastLNGPrice(boolean newPriceOnLastLNGPrice) {
		boolean oldPriceOnLastLNGPrice = priceOnLastLNGPrice;
		priceOnLastLNGPrice = newPriceOnLastLNGPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__PRICE_ON_LAST_LNG_PRICE, oldPriceOnLastLNGPrice, priceOnLastLNGPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<APortSet<Port>> getReturnPorts() {
		if (returnPorts == null) {
			returnPorts = new EObjectResolvingEList<APortSet<Port>>(APortSet.class, this, CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__RETURN_PORTS);
		}
		return returnPorts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isIsFirstLoadPort() {
		return isFirstLoadPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setIsFirstLoadPort(boolean newIsFirstLoadPort) {
		boolean oldIsFirstLoadPort = isFirstLoadPort;
		isFirstLoadPort = newIsFirstLoadPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__IS_FIRST_LOAD_PORT, oldIsFirstLoadPort, isFirstLoadPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__SPEED:
				return getSpeed();
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__FUEL_PRICE_EXPRESSION:
				return getFuelPriceExpression();
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__HIRE_PRICE_EXPRESSION:
				return getHirePriceExpression();
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__INCLUDE_CANAL:
				return isIncludeCanal();
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__INCLUDE_CANAL_TIME:
				return isIncludeCanalTime();
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__LUMP_SUM_PRICE_EXPRESSION:
				return getLumpSumPriceExpression();
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__PRICE_ON_LAST_LNG_PRICE:
				return isPriceOnLastLNGPrice();
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__RETURN_PORTS:
				return getReturnPorts();
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__IS_FIRST_LOAD_PORT:
				return isIsFirstLoadPort();
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
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__SPEED:
				setSpeed((Double)newValue);
				return;
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__FUEL_PRICE_EXPRESSION:
				setFuelPriceExpression((String)newValue);
				return;
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__HIRE_PRICE_EXPRESSION:
				setHirePriceExpression((String)newValue);
				return;
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__INCLUDE_CANAL:
				setIncludeCanal((Boolean)newValue);
				return;
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__INCLUDE_CANAL_TIME:
				setIncludeCanalTime((Boolean)newValue);
				return;
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__LUMP_SUM_PRICE_EXPRESSION:
				setLumpSumPriceExpression((String)newValue);
				return;
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__PRICE_ON_LAST_LNG_PRICE:
				setPriceOnLastLNGPrice((Boolean)newValue);
				return;
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__RETURN_PORTS:
				getReturnPorts().clear();
				getReturnPorts().addAll((Collection<? extends APortSet<Port>>)newValue);
				return;
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__IS_FIRST_LOAD_PORT:
				setIsFirstLoadPort((Boolean)newValue);
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
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__SPEED:
				setSpeed(SPEED_EDEFAULT);
				return;
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__FUEL_PRICE_EXPRESSION:
				setFuelPriceExpression(FUEL_PRICE_EXPRESSION_EDEFAULT);
				return;
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__HIRE_PRICE_EXPRESSION:
				setHirePriceExpression(HIRE_PRICE_EXPRESSION_EDEFAULT);
				return;
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__INCLUDE_CANAL:
				setIncludeCanal(INCLUDE_CANAL_EDEFAULT);
				return;
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__INCLUDE_CANAL_TIME:
				setIncludeCanalTime(INCLUDE_CANAL_TIME_EDEFAULT);
				return;
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__LUMP_SUM_PRICE_EXPRESSION:
				setLumpSumPriceExpression(LUMP_SUM_PRICE_EXPRESSION_EDEFAULT);
				return;
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__PRICE_ON_LAST_LNG_PRICE:
				setPriceOnLastLNGPrice(PRICE_ON_LAST_LNG_PRICE_EDEFAULT);
				return;
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__RETURN_PORTS:
				getReturnPorts().clear();
				return;
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__IS_FIRST_LOAD_PORT:
				setIsFirstLoadPort(IS_FIRST_LOAD_PORT_EDEFAULT);
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
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__SPEED:
				return speed != SPEED_EDEFAULT;
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__FUEL_PRICE_EXPRESSION:
				return FUEL_PRICE_EXPRESSION_EDEFAULT == null ? fuelPriceExpression != null : !FUEL_PRICE_EXPRESSION_EDEFAULT.equals(fuelPriceExpression);
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__HIRE_PRICE_EXPRESSION:
				return HIRE_PRICE_EXPRESSION_EDEFAULT == null ? hirePriceExpression != null : !HIRE_PRICE_EXPRESSION_EDEFAULT.equals(hirePriceExpression);
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__INCLUDE_CANAL:
				return includeCanal != INCLUDE_CANAL_EDEFAULT;
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__INCLUDE_CANAL_TIME:
				return includeCanalTime != INCLUDE_CANAL_TIME_EDEFAULT;
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__LUMP_SUM_PRICE_EXPRESSION:
				return LUMP_SUM_PRICE_EXPRESSION_EDEFAULT == null ? lumpSumPriceExpression != null : !LUMP_SUM_PRICE_EXPRESSION_EDEFAULT.equals(lumpSumPriceExpression);
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__PRICE_ON_LAST_LNG_PRICE:
				return priceOnLastLNGPrice != PRICE_ON_LAST_LNG_PRICE_EDEFAULT;
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__RETURN_PORTS:
				return returnPorts != null && !returnPorts.isEmpty();
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__IS_FIRST_LOAD_PORT:
				return isFirstLoadPort != IS_FIRST_LOAD_PORT_EDEFAULT;
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
				case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__SPEED: return CommercialPackage.NOTIONAL_JOURNEY_TERM__SPEED;
				case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__FUEL_PRICE_EXPRESSION: return CommercialPackage.NOTIONAL_JOURNEY_TERM__FUEL_PRICE_EXPRESSION;
				case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__HIRE_PRICE_EXPRESSION: return CommercialPackage.NOTIONAL_JOURNEY_TERM__HIRE_PRICE_EXPRESSION;
				case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__INCLUDE_CANAL: return CommercialPackage.NOTIONAL_JOURNEY_TERM__INCLUDE_CANAL;
				case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__INCLUDE_CANAL_TIME: return CommercialPackage.NOTIONAL_JOURNEY_TERM__INCLUDE_CANAL_TIME;
				case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__LUMP_SUM_PRICE_EXPRESSION: return CommercialPackage.NOTIONAL_JOURNEY_TERM__LUMP_SUM_PRICE_EXPRESSION;
				case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__PRICE_ON_LAST_LNG_PRICE: return CommercialPackage.NOTIONAL_JOURNEY_TERM__PRICE_ON_LAST_LNG_PRICE;
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
				case CommercialPackage.NOTIONAL_JOURNEY_TERM__SPEED: return CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__SPEED;
				case CommercialPackage.NOTIONAL_JOURNEY_TERM__FUEL_PRICE_EXPRESSION: return CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__FUEL_PRICE_EXPRESSION;
				case CommercialPackage.NOTIONAL_JOURNEY_TERM__HIRE_PRICE_EXPRESSION: return CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__HIRE_PRICE_EXPRESSION;
				case CommercialPackage.NOTIONAL_JOURNEY_TERM__INCLUDE_CANAL: return CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__INCLUDE_CANAL;
				case CommercialPackage.NOTIONAL_JOURNEY_TERM__INCLUDE_CANAL_TIME: return CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__INCLUDE_CANAL_TIME;
				case CommercialPackage.NOTIONAL_JOURNEY_TERM__LUMP_SUM_PRICE_EXPRESSION: return CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__LUMP_SUM_PRICE_EXPRESSION;
				case CommercialPackage.NOTIONAL_JOURNEY_TERM__PRICE_ON_LAST_LNG_PRICE: return CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__PRICE_ON_LAST_LNG_PRICE;
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
		result.append(", priceOnLastLNGPrice: ");
		result.append(priceOnLastLNGPrice);
		result.append(", isFirstLoadPort: ");
		result.append(isFirstLoadPort);
		result.append(')');
		return result.toString();
	}

} //NotionalJourneyBallastBonusTermImpl
