/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing.impl;

import com.mmxlabs.models.lng.pricing.InstrumentPeriod;
import com.mmxlabs.models.lng.pricing.PricingCalendar;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.SettleStrategy;

import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.mmxcore.impl.NamedObjectImpl;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

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
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SettleStrategyImpl#getPricingCalendar <em>Pricing Calendar</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SettleStrategyImpl#getPricingPeriod <em>Pricing Period</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SettleStrategyImpl#getHedgingPeriod <em>Hedging Period</em>}</li>
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
	 * The cached value of the '{@link #getPricingCalendar() <em>Pricing Calendar</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPricingCalendar()
	 * @generated
	 * @ordered
	 */
	protected PricingCalendar pricingCalendar;

	/**
	 * This is true if the Pricing Calendar reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean pricingCalendarESet;

	/**
	 * The cached value of the '{@link #getPricingPeriod() <em>Pricing Period</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPricingPeriod()
	 * @generated
	 * @ordered
	 */
	protected InstrumentPeriod pricingPeriod;

	/**
	 * The cached value of the '{@link #getHedgingPeriod() <em>Hedging Period</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHedgingPeriod()
	 * @generated
	 * @ordered
	 */
	protected InstrumentPeriod hedgingPeriod;

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
	public PricingCalendar getPricingCalendar() {
		if (pricingCalendar != null && pricingCalendar.eIsProxy()) {
			InternalEObject oldPricingCalendar = (InternalEObject)pricingCalendar;
			pricingCalendar = (PricingCalendar)eResolveProxy(oldPricingCalendar);
			if (pricingCalendar != oldPricingCalendar) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PricingPackage.SETTLE_STRATEGY__PRICING_CALENDAR, oldPricingCalendar, pricingCalendar));
			}
		}
		return pricingCalendar;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PricingCalendar basicGetPricingCalendar() {
		return pricingCalendar;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPricingCalendar(PricingCalendar newPricingCalendar) {
		PricingCalendar oldPricingCalendar = pricingCalendar;
		pricingCalendar = newPricingCalendar;
		boolean oldPricingCalendarESet = pricingCalendarESet;
		pricingCalendarESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SETTLE_STRATEGY__PRICING_CALENDAR, oldPricingCalendar, pricingCalendar, !oldPricingCalendarESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetPricingCalendar() {
		PricingCalendar oldPricingCalendar = pricingCalendar;
		boolean oldPricingCalendarESet = pricingCalendarESet;
		pricingCalendar = null;
		pricingCalendarESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, PricingPackage.SETTLE_STRATEGY__PRICING_CALENDAR, oldPricingCalendar, null, oldPricingCalendarESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetPricingCalendar() {
		return pricingCalendarESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public InstrumentPeriod getPricingPeriod() {
		return pricingPeriod;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPricingPeriod(InstrumentPeriod newPricingPeriod, NotificationChain msgs) {
		InstrumentPeriod oldPricingPeriod = pricingPeriod;
		pricingPeriod = newPricingPeriod;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PricingPackage.SETTLE_STRATEGY__PRICING_PERIOD, oldPricingPeriod, newPricingPeriod);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPricingPeriod(InstrumentPeriod newPricingPeriod) {
		if (newPricingPeriod != pricingPeriod) {
			NotificationChain msgs = null;
			if (pricingPeriod != null)
				msgs = ((InternalEObject)pricingPeriod).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PricingPackage.SETTLE_STRATEGY__PRICING_PERIOD, null, msgs);
			if (newPricingPeriod != null)
				msgs = ((InternalEObject)newPricingPeriod).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PricingPackage.SETTLE_STRATEGY__PRICING_PERIOD, null, msgs);
			msgs = basicSetPricingPeriod(newPricingPeriod, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SETTLE_STRATEGY__PRICING_PERIOD, newPricingPeriod, newPricingPeriod));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public InstrumentPeriod getHedgingPeriod() {
		return hedgingPeriod;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetHedgingPeriod(InstrumentPeriod newHedgingPeriod, NotificationChain msgs) {
		InstrumentPeriod oldHedgingPeriod = hedgingPeriod;
		hedgingPeriod = newHedgingPeriod;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PricingPackage.SETTLE_STRATEGY__HEDGING_PERIOD, oldHedgingPeriod, newHedgingPeriod);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setHedgingPeriod(InstrumentPeriod newHedgingPeriod) {
		if (newHedgingPeriod != hedgingPeriod) {
			NotificationChain msgs = null;
			if (hedgingPeriod != null)
				msgs = ((InternalEObject)hedgingPeriod).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PricingPackage.SETTLE_STRATEGY__HEDGING_PERIOD, null, msgs);
			if (newHedgingPeriod != null)
				msgs = ((InternalEObject)newHedgingPeriod).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PricingPackage.SETTLE_STRATEGY__HEDGING_PERIOD, null, msgs);
			msgs = basicSetHedgingPeriod(newHedgingPeriod, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SETTLE_STRATEGY__HEDGING_PERIOD, newHedgingPeriod, newHedgingPeriod));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PricingPackage.SETTLE_STRATEGY__PRICING_PERIOD:
				return basicSetPricingPeriod(null, msgs);
			case PricingPackage.SETTLE_STRATEGY__HEDGING_PERIOD:
				return basicSetHedgingPeriod(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
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
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PricingPackage.SETTLE_STRATEGY__DAY_OF_THE_MONTH:
				return getDayOfTheMonth();
			case PricingPackage.SETTLE_STRATEGY__LAST_DAY_OF_THE_MONTH:
				return isLastDayOfTheMonth();
			case PricingPackage.SETTLE_STRATEGY__PRICING_CALENDAR:
				if (resolve) return getPricingCalendar();
				return basicGetPricingCalendar();
			case PricingPackage.SETTLE_STRATEGY__PRICING_PERIOD:
				return getPricingPeriod();
			case PricingPackage.SETTLE_STRATEGY__HEDGING_PERIOD:
				return getHedgingPeriod();
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
			case PricingPackage.SETTLE_STRATEGY__PRICING_CALENDAR:
				setPricingCalendar((PricingCalendar)newValue);
				return;
			case PricingPackage.SETTLE_STRATEGY__PRICING_PERIOD:
				setPricingPeriod((InstrumentPeriod)newValue);
				return;
			case PricingPackage.SETTLE_STRATEGY__HEDGING_PERIOD:
				setHedgingPeriod((InstrumentPeriod)newValue);
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
			case PricingPackage.SETTLE_STRATEGY__PRICING_CALENDAR:
				unsetPricingCalendar();
				return;
			case PricingPackage.SETTLE_STRATEGY__PRICING_PERIOD:
				setPricingPeriod((InstrumentPeriod)null);
				return;
			case PricingPackage.SETTLE_STRATEGY__HEDGING_PERIOD:
				setHedgingPeriod((InstrumentPeriod)null);
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
			case PricingPackage.SETTLE_STRATEGY__PRICING_CALENDAR:
				return isSetPricingCalendar();
			case PricingPackage.SETTLE_STRATEGY__PRICING_PERIOD:
				return pricingPeriod != null;
			case PricingPackage.SETTLE_STRATEGY__HEDGING_PERIOD:
				return hedgingPeriod != null;
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
		result.append(')');
		return result.toString();
	}

} //SettleStrategyImpl
