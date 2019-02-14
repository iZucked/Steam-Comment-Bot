/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing.impl;

import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.SettleStrategy;

import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.mmxcore.impl.NamedObjectImpl;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Settle Strategy</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SettleStrategyImpl#getDayOfTheMonth <em>Day Of The Month</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SettleStrategyImpl#isLastDayOfTheMonth <em>Last Day Of The Month</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SettleStrategyImpl#isUseCalendarMonth <em>Use Calendar Month</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SettleStrategyImpl#getSettlePeriod <em>Settle Period</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SettleStrategyImpl#getSettlePeriodUnit <em>Settle Period Unit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SettleStrategyImpl#getSettleStartMonthsPrior <em>Settle Start Months Prior</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SettleStrategyImpl extends NamedObjectImpl implements SettleStrategy {
	/**
	 * The default value of the '{@link #getDayOfTheMonth() <em>Day Of The Month</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDayOfTheMonth()
	 * @generated
	 * @ordered
	 */
	protected static final int DAY_OF_THE_MONTH_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDayOfTheMonth() <em>Day Of The Month</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDayOfTheMonth()
	 * @generated
	 * @ordered
	 */
	protected int dayOfTheMonth = DAY_OF_THE_MONTH_EDEFAULT;

	/**
	 * The default value of the '{@link #isLastDayOfTheMonth() <em>Last Day Of The Month</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLastDayOfTheMonth()
	 * @generated
	 * @ordered
	 */
	protected static final boolean LAST_DAY_OF_THE_MONTH_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isLastDayOfTheMonth() <em>Last Day Of The Month</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLastDayOfTheMonth()
	 * @generated
	 * @ordered
	 */
	protected boolean lastDayOfTheMonth = LAST_DAY_OF_THE_MONTH_EDEFAULT;

	/**
	 * The default value of the '{@link #isUseCalendarMonth() <em>Use Calendar Month</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUseCalendarMonth()
	 * @generated
	 * @ordered
	 */
	protected static final boolean USE_CALENDAR_MONTH_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isUseCalendarMonth() <em>Use Calendar Month</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUseCalendarMonth()
	 * @generated
	 * @ordered
	 */
	protected boolean useCalendarMonth = USE_CALENDAR_MONTH_EDEFAULT;

	/**
	 * The default value of the '{@link #getSettlePeriod() <em>Settle Period</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSettlePeriod()
	 * @generated
	 * @ordered
	 */
	protected static final int SETTLE_PERIOD_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSettlePeriod() <em>Settle Period</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSettlePeriod()
	 * @generated
	 * @ordered
	 */
	protected int settlePeriod = SETTLE_PERIOD_EDEFAULT;

	/**
	 * The default value of the '{@link #getSettlePeriodUnit() <em>Settle Period Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSettlePeriodUnit()
	 * @generated
	 * @ordered
	 */
	protected static final TimePeriod SETTLE_PERIOD_UNIT_EDEFAULT = TimePeriod.HOURS;

	/**
	 * The cached value of the '{@link #getSettlePeriodUnit() <em>Settle Period Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSettlePeriodUnit()
	 * @generated
	 * @ordered
	 */
	protected TimePeriod settlePeriodUnit = SETTLE_PERIOD_UNIT_EDEFAULT;

	/**
	 * The default value of the '{@link #getSettleStartMonthsPrior() <em>Settle Start Months Prior</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSettleStartMonthsPrior()
	 * @generated
	 * @ordered
	 */
	protected static final int SETTLE_START_MONTHS_PRIOR_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSettleStartMonthsPrior() <em>Settle Start Months Prior</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSettleStartMonthsPrior()
	 * @generated
	 * @ordered
	 */
	protected int settleStartMonthsPrior = SETTLE_START_MONTHS_PRIOR_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SettleStrategyImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.SETTLE_STRATEGY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getDayOfTheMonth() {
		return dayOfTheMonth;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDayOfTheMonth(int newDayOfTheMonth) {
		int oldDayOfTheMonth = dayOfTheMonth;
		dayOfTheMonth = newDayOfTheMonth;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SETTLE_STRATEGY__DAY_OF_THE_MONTH, oldDayOfTheMonth, dayOfTheMonth));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getSettlePeriod() {
		return settlePeriod;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSettlePeriod(int newSettlePeriod) {
		int oldSettlePeriod = settlePeriod;
		settlePeriod = newSettlePeriod;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SETTLE_STRATEGY__SETTLE_PERIOD, oldSettlePeriod, settlePeriod));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TimePeriod getSettlePeriodUnit() {
		return settlePeriodUnit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSettlePeriodUnit(TimePeriod newSettlePeriodUnit) {
		TimePeriod oldSettlePeriodUnit = settlePeriodUnit;
		settlePeriodUnit = newSettlePeriodUnit == null ? SETTLE_PERIOD_UNIT_EDEFAULT : newSettlePeriodUnit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SETTLE_STRATEGY__SETTLE_PERIOD_UNIT, oldSettlePeriodUnit, settlePeriodUnit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getSettleStartMonthsPrior() {
		return settleStartMonthsPrior;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSettleStartMonthsPrior(int newSettleStartMonthsPrior) {
		int oldSettleStartMonthsPrior = settleStartMonthsPrior;
		settleStartMonthsPrior = newSettleStartMonthsPrior;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SETTLE_STRATEGY__SETTLE_START_MONTHS_PRIOR, oldSettleStartMonthsPrior, settleStartMonthsPrior));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isLastDayOfTheMonth() {
		return lastDayOfTheMonth;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLastDayOfTheMonth(boolean newLastDayOfTheMonth) {
		boolean oldLastDayOfTheMonth = lastDayOfTheMonth;
		lastDayOfTheMonth = newLastDayOfTheMonth;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SETTLE_STRATEGY__LAST_DAY_OF_THE_MONTH, oldLastDayOfTheMonth, lastDayOfTheMonth));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isUseCalendarMonth() {
		return useCalendarMonth;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setUseCalendarMonth(boolean newUseCalendarMonth) {
		boolean oldUseCalendarMonth = useCalendarMonth;
		useCalendarMonth = newUseCalendarMonth;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SETTLE_STRATEGY__USE_CALENDAR_MONTH, oldUseCalendarMonth, useCalendarMonth));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PricingPackage.SETTLE_STRATEGY__DAY_OF_THE_MONTH:
				return getDayOfTheMonth();
			case PricingPackage.SETTLE_STRATEGY__LAST_DAY_OF_THE_MONTH:
				return isLastDayOfTheMonth();
			case PricingPackage.SETTLE_STRATEGY__USE_CALENDAR_MONTH:
				return isUseCalendarMonth();
			case PricingPackage.SETTLE_STRATEGY__SETTLE_PERIOD:
				return getSettlePeriod();
			case PricingPackage.SETTLE_STRATEGY__SETTLE_PERIOD_UNIT:
				return getSettlePeriodUnit();
			case PricingPackage.SETTLE_STRATEGY__SETTLE_START_MONTHS_PRIOR:
				return getSettleStartMonthsPrior();
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
			case PricingPackage.SETTLE_STRATEGY__DAY_OF_THE_MONTH:
				setDayOfTheMonth((Integer)newValue);
				return;
			case PricingPackage.SETTLE_STRATEGY__LAST_DAY_OF_THE_MONTH:
				setLastDayOfTheMonth((Boolean)newValue);
				return;
			case PricingPackage.SETTLE_STRATEGY__USE_CALENDAR_MONTH:
				setUseCalendarMonth((Boolean)newValue);
				return;
			case PricingPackage.SETTLE_STRATEGY__SETTLE_PERIOD:
				setSettlePeriod((Integer)newValue);
				return;
			case PricingPackage.SETTLE_STRATEGY__SETTLE_PERIOD_UNIT:
				setSettlePeriodUnit((TimePeriod)newValue);
				return;
			case PricingPackage.SETTLE_STRATEGY__SETTLE_START_MONTHS_PRIOR:
				setSettleStartMonthsPrior((Integer)newValue);
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
			case PricingPackage.SETTLE_STRATEGY__DAY_OF_THE_MONTH:
				setDayOfTheMonth(DAY_OF_THE_MONTH_EDEFAULT);
				return;
			case PricingPackage.SETTLE_STRATEGY__LAST_DAY_OF_THE_MONTH:
				setLastDayOfTheMonth(LAST_DAY_OF_THE_MONTH_EDEFAULT);
				return;
			case PricingPackage.SETTLE_STRATEGY__USE_CALENDAR_MONTH:
				setUseCalendarMonth(USE_CALENDAR_MONTH_EDEFAULT);
				return;
			case PricingPackage.SETTLE_STRATEGY__SETTLE_PERIOD:
				setSettlePeriod(SETTLE_PERIOD_EDEFAULT);
				return;
			case PricingPackage.SETTLE_STRATEGY__SETTLE_PERIOD_UNIT:
				setSettlePeriodUnit(SETTLE_PERIOD_UNIT_EDEFAULT);
				return;
			case PricingPackage.SETTLE_STRATEGY__SETTLE_START_MONTHS_PRIOR:
				setSettleStartMonthsPrior(SETTLE_START_MONTHS_PRIOR_EDEFAULT);
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
			case PricingPackage.SETTLE_STRATEGY__DAY_OF_THE_MONTH:
				return dayOfTheMonth != DAY_OF_THE_MONTH_EDEFAULT;
			case PricingPackage.SETTLE_STRATEGY__LAST_DAY_OF_THE_MONTH:
				return lastDayOfTheMonth != LAST_DAY_OF_THE_MONTH_EDEFAULT;
			case PricingPackage.SETTLE_STRATEGY__USE_CALENDAR_MONTH:
				return useCalendarMonth != USE_CALENDAR_MONTH_EDEFAULT;
			case PricingPackage.SETTLE_STRATEGY__SETTLE_PERIOD:
				return settlePeriod != SETTLE_PERIOD_EDEFAULT;
			case PricingPackage.SETTLE_STRATEGY__SETTLE_PERIOD_UNIT:
				return settlePeriodUnit != SETTLE_PERIOD_UNIT_EDEFAULT;
			case PricingPackage.SETTLE_STRATEGY__SETTLE_START_MONTHS_PRIOR:
				return settleStartMonthsPrior != SETTLE_START_MONTHS_PRIOR_EDEFAULT;
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
		result.append(" (dayOfTheMonth: ");
		result.append(dayOfTheMonth);
		result.append(", lastDayOfTheMonth: ");
		result.append(lastDayOfTheMonth);
		result.append(", useCalendarMonth: ");
		result.append(useCalendarMonth);
		result.append(", settlePeriod: ");
		result.append(settlePeriod);
		result.append(", settlePeriodUnit: ");
		result.append(settlePeriodUnit);
		result.append(", settleStartMonthsPrior: ");
		result.append(settleStartMonthsPrior);
		result.append(')');
		return result.toString();
	}

} //SettleStrategyImpl
