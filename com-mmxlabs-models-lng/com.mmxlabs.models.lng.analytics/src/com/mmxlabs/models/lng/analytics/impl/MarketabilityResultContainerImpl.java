/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.MarketabilityEvent;
import com.mmxlabs.models.lng.analytics.MarketabilityResult;
import com.mmxlabs.models.lng.analytics.MarketabilityResultContainer;

import com.mmxlabs.models.lng.schedule.CanalJourneyEvent;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Marketability Result Container</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityResultContainerImpl#getRhsResults <em>Rhs Results</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityResultContainerImpl#getNextEvent <em>Next Event</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityResultContainerImpl#getBuyDate <em>Buy Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityResultContainerImpl#getSellDate <em>Sell Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityResultContainerImpl#getLadenPanama <em>Laden Panama</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityResultContainerImpl#getBallastPanama <em>Ballast Panama</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MarketabilityResultContainerImpl extends EObjectImpl implements MarketabilityResultContainer {
	/**
	 * The cached value of the '{@link #getRhsResults() <em>Rhs Results</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRhsResults()
	 * @generated
	 * @ordered
	 */
	protected EList<MarketabilityResult> rhsResults;
	/**
	 * The cached value of the '{@link #getNextEvent() <em>Next Event</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNextEvent()
	 * @generated
	 * @ordered
	 */
	protected MarketabilityEvent nextEvent;
	/**
	 * The default value of the '{@link #getBuyDate() <em>Buy Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBuyDate()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDateTime BUY_DATE_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getBuyDate() <em>Buy Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBuyDate()
	 * @generated
	 * @ordered
	 */
	protected LocalDateTime buyDate = BUY_DATE_EDEFAULT;
	/**
	 * The default value of the '{@link #getSellDate() <em>Sell Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSellDate()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDateTime SELL_DATE_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getSellDate() <em>Sell Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSellDate()
	 * @generated
	 * @ordered
	 */
	protected LocalDateTime sellDate = SELL_DATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getLadenPanama() <em>Laden Panama</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLadenPanama()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDateTime LADEN_PANAMA_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getLadenPanama() <em>Laden Panama</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLadenPanama()
	 * @generated
	 * @ordered
	 */
	protected LocalDateTime ladenPanama = LADEN_PANAMA_EDEFAULT;
	/**
	 * The default value of the '{@link #getBallastPanama() <em>Ballast Panama</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastPanama()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDateTime BALLAST_PANAMA_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getBallastPanama() <em>Ballast Panama</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastPanama()
	 * @generated
	 * @ordered
	 */
	protected LocalDateTime ballastPanama = BALLAST_PANAMA_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MarketabilityResultContainerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.MARKETABILITY_RESULT_CONTAINER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<MarketabilityResult> getRhsResults() {
		if (rhsResults == null) {
			rhsResults = new EObjectContainmentEList<MarketabilityResult>(MarketabilityResult.class, this, AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__RHS_RESULTS);
		}
		return rhsResults;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MarketabilityEvent getNextEvent() {
		return nextEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetNextEvent(MarketabilityEvent newNextEvent, NotificationChain msgs) {
		MarketabilityEvent oldNextEvent = nextEvent;
		nextEvent = newNextEvent;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__NEXT_EVENT, oldNextEvent, newNextEvent);
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
	public void setNextEvent(MarketabilityEvent newNextEvent) {
		if (newNextEvent != nextEvent) {
			NotificationChain msgs = null;
			if (nextEvent != null)
				msgs = ((InternalEObject)nextEvent).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__NEXT_EVENT, null, msgs);
			if (newNextEvent != null)
				msgs = ((InternalEObject)newNextEvent).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__NEXT_EVENT, null, msgs);
			msgs = basicSetNextEvent(newNextEvent, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__NEXT_EVENT, newNextEvent, newNextEvent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDateTime getLadenPanama() {
		return ladenPanama;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLadenPanama(LocalDateTime newLadenPanama) {
		LocalDateTime oldLadenPanama = ladenPanama;
		ladenPanama = newLadenPanama;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__LADEN_PANAMA, oldLadenPanama, ladenPanama));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDateTime getBallastPanama() {
		return ballastPanama;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBallastPanama(LocalDateTime newBallastPanama) {
		LocalDateTime oldBallastPanama = ballastPanama;
		ballastPanama = newBallastPanama;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__BALLAST_PANAMA, oldBallastPanama, ballastPanama));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDateTime getBuyDate() {
		return buyDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBuyDate(LocalDateTime newBuyDate) {
		LocalDateTime oldBuyDate = buyDate;
		buyDate = newBuyDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__BUY_DATE, oldBuyDate, buyDate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDateTime getSellDate() {
		return sellDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSellDate(LocalDateTime newSellDate) {
		LocalDateTime oldSellDate = sellDate;
		sellDate = newSellDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__SELL_DATE, oldSellDate, sellDate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__RHS_RESULTS:
				return ((InternalEList<?>)getRhsResults()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__NEXT_EVENT:
				return basicSetNextEvent(null, msgs);
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
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__RHS_RESULTS:
				return getRhsResults();
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__NEXT_EVENT:
				return getNextEvent();
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__BUY_DATE:
				return getBuyDate();
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__SELL_DATE:
				return getSellDate();
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__LADEN_PANAMA:
				return getLadenPanama();
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__BALLAST_PANAMA:
				return getBallastPanama();
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
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__RHS_RESULTS:
				getRhsResults().clear();
				getRhsResults().addAll((Collection<? extends MarketabilityResult>)newValue);
				return;
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__NEXT_EVENT:
				setNextEvent((MarketabilityEvent)newValue);
				return;
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__BUY_DATE:
				setBuyDate((LocalDateTime)newValue);
				return;
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__SELL_DATE:
				setSellDate((LocalDateTime)newValue);
				return;
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__LADEN_PANAMA:
				setLadenPanama((LocalDateTime)newValue);
				return;
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__BALLAST_PANAMA:
				setBallastPanama((LocalDateTime)newValue);
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
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__RHS_RESULTS:
				getRhsResults().clear();
				return;
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__NEXT_EVENT:
				setNextEvent((MarketabilityEvent)null);
				return;
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__BUY_DATE:
				setBuyDate(BUY_DATE_EDEFAULT);
				return;
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__SELL_DATE:
				setSellDate(SELL_DATE_EDEFAULT);
				return;
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__LADEN_PANAMA:
				setLadenPanama(LADEN_PANAMA_EDEFAULT);
				return;
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__BALLAST_PANAMA:
				setBallastPanama(BALLAST_PANAMA_EDEFAULT);
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
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__RHS_RESULTS:
				return rhsResults != null && !rhsResults.isEmpty();
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__NEXT_EVENT:
				return nextEvent != null;
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__BUY_DATE:
				return BUY_DATE_EDEFAULT == null ? buyDate != null : !BUY_DATE_EDEFAULT.equals(buyDate);
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__SELL_DATE:
				return SELL_DATE_EDEFAULT == null ? sellDate != null : !SELL_DATE_EDEFAULT.equals(sellDate);
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__LADEN_PANAMA:
				return LADEN_PANAMA_EDEFAULT == null ? ladenPanama != null : !LADEN_PANAMA_EDEFAULT.equals(ladenPanama);
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__BALLAST_PANAMA:
				return BALLAST_PANAMA_EDEFAULT == null ? ballastPanama != null : !BALLAST_PANAMA_EDEFAULT.equals(ballastPanama);
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
		result.append(" (buyDate: ");
		result.append(buyDate);
		result.append(", sellDate: ");
		result.append(sellDate);
		result.append(", ladenPanama: ");
		result.append(ladenPanama);
		result.append(", ballastPanama: ");
		result.append(ballastPanama);
		result.append(')');
		return result.toString();
	}

} //MarketabilityResultContainerImpl
