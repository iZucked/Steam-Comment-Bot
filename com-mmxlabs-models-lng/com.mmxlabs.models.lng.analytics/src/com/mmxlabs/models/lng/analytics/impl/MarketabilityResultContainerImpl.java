/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.MarketabilityResult;
import com.mmxlabs.models.lng.analytics.MarketabilityResultContainer;

import com.mmxlabs.models.lng.schedule.CanalJourneyEvent;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
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
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityResultContainerImpl#getBuySlotVisit <em>Buy Slot Visit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityResultContainerImpl#getSellSlotVisit <em>Sell Slot Visit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityResultContainerImpl#getNextSlotVisit <em>Next Slot Visit</em>}</li>
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
	 * The cached value of the '{@link #getBuySlotVisit() <em>Buy Slot Visit</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBuySlotVisit()
	 * @generated
	 * @ordered
	 */
	protected SlotVisit buySlotVisit;
	/**
	 * The cached value of the '{@link #getSellSlotVisit() <em>Sell Slot Visit</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSellSlotVisit()
	 * @generated
	 * @ordered
	 */
	protected SlotVisit sellSlotVisit;
	/**
	 * The cached value of the '{@link #getNextSlotVisit() <em>Next Slot Visit</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNextSlotVisit()
	 * @generated
	 * @ordered
	 */
	protected SlotVisit nextSlotVisit;
	/**
	 * The cached value of the '{@link #getLadenPanama() <em>Laden Panama</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLadenPanama()
	 * @generated
	 * @ordered
	 */
	protected CanalJourneyEvent ladenPanama;
	/**
	 * The cached value of the '{@link #getBallastPanama() <em>Ballast Panama</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastPanama()
	 * @generated
	 * @ordered
	 */
	protected CanalJourneyEvent ballastPanama;

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
	public SlotVisit getBuySlotVisit() {
		if (buySlotVisit != null && buySlotVisit.eIsProxy()) {
			InternalEObject oldBuySlotVisit = (InternalEObject)buySlotVisit;
			buySlotVisit = (SlotVisit)eResolveProxy(oldBuySlotVisit);
			if (buySlotVisit != oldBuySlotVisit) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__BUY_SLOT_VISIT, oldBuySlotVisit, buySlotVisit));
			}
		}
		return buySlotVisit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotVisit basicGetBuySlotVisit() {
		return buySlotVisit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBuySlotVisit(SlotVisit newBuySlotVisit) {
		SlotVisit oldBuySlotVisit = buySlotVisit;
		buySlotVisit = newBuySlotVisit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__BUY_SLOT_VISIT, oldBuySlotVisit, buySlotVisit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SlotVisit getSellSlotVisit() {
		if (sellSlotVisit != null && sellSlotVisit.eIsProxy()) {
			InternalEObject oldSellSlotVisit = (InternalEObject)sellSlotVisit;
			sellSlotVisit = (SlotVisit)eResolveProxy(oldSellSlotVisit);
			if (sellSlotVisit != oldSellSlotVisit) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__SELL_SLOT_VISIT, oldSellSlotVisit, sellSlotVisit));
			}
		}
		return sellSlotVisit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotVisit basicGetSellSlotVisit() {
		return sellSlotVisit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSellSlotVisit(SlotVisit newSellSlotVisit) {
		SlotVisit oldSellSlotVisit = sellSlotVisit;
		sellSlotVisit = newSellSlotVisit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__SELL_SLOT_VISIT, oldSellSlotVisit, sellSlotVisit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SlotVisit getNextSlotVisit() {
		if (nextSlotVisit != null && nextSlotVisit.eIsProxy()) {
			InternalEObject oldNextSlotVisit = (InternalEObject)nextSlotVisit;
			nextSlotVisit = (SlotVisit)eResolveProxy(oldNextSlotVisit);
			if (nextSlotVisit != oldNextSlotVisit) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__NEXT_SLOT_VISIT, oldNextSlotVisit, nextSlotVisit));
			}
		}
		return nextSlotVisit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotVisit basicGetNextSlotVisit() {
		return nextSlotVisit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setNextSlotVisit(SlotVisit newNextSlotVisit) {
		SlotVisit oldNextSlotVisit = nextSlotVisit;
		nextSlotVisit = newNextSlotVisit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__NEXT_SLOT_VISIT, oldNextSlotVisit, nextSlotVisit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CanalJourneyEvent getLadenPanama() {
		if (ladenPanama != null && ladenPanama.eIsProxy()) {
			InternalEObject oldLadenPanama = (InternalEObject)ladenPanama;
			ladenPanama = (CanalJourneyEvent)eResolveProxy(oldLadenPanama);
			if (ladenPanama != oldLadenPanama) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__LADEN_PANAMA, oldLadenPanama, ladenPanama));
			}
		}
		return ladenPanama;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CanalJourneyEvent basicGetLadenPanama() {
		return ladenPanama;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLadenPanama(CanalJourneyEvent newLadenPanama) {
		CanalJourneyEvent oldLadenPanama = ladenPanama;
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
	public CanalJourneyEvent getBallastPanama() {
		if (ballastPanama != null && ballastPanama.eIsProxy()) {
			InternalEObject oldBallastPanama = (InternalEObject)ballastPanama;
			ballastPanama = (CanalJourneyEvent)eResolveProxy(oldBallastPanama);
			if (ballastPanama != oldBallastPanama) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__BALLAST_PANAMA, oldBallastPanama, ballastPanama));
			}
		}
		return ballastPanama;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CanalJourneyEvent basicGetBallastPanama() {
		return ballastPanama;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBallastPanama(CanalJourneyEvent newBallastPanama) {
		CanalJourneyEvent oldBallastPanama = ballastPanama;
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
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__RHS_RESULTS:
				return ((InternalEList<?>)getRhsResults()).basicRemove(otherEnd, msgs);
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
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__BUY_SLOT_VISIT:
				if (resolve) return getBuySlotVisit();
				return basicGetBuySlotVisit();
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__SELL_SLOT_VISIT:
				if (resolve) return getSellSlotVisit();
				return basicGetSellSlotVisit();
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__NEXT_SLOT_VISIT:
				if (resolve) return getNextSlotVisit();
				return basicGetNextSlotVisit();
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__LADEN_PANAMA:
				if (resolve) return getLadenPanama();
				return basicGetLadenPanama();
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__BALLAST_PANAMA:
				if (resolve) return getBallastPanama();
				return basicGetBallastPanama();
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
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__BUY_SLOT_VISIT:
				setBuySlotVisit((SlotVisit)newValue);
				return;
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__SELL_SLOT_VISIT:
				setSellSlotVisit((SlotVisit)newValue);
				return;
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__NEXT_SLOT_VISIT:
				setNextSlotVisit((SlotVisit)newValue);
				return;
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__LADEN_PANAMA:
				setLadenPanama((CanalJourneyEvent)newValue);
				return;
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__BALLAST_PANAMA:
				setBallastPanama((CanalJourneyEvent)newValue);
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
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__BUY_SLOT_VISIT:
				setBuySlotVisit((SlotVisit)null);
				return;
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__SELL_SLOT_VISIT:
				setSellSlotVisit((SlotVisit)null);
				return;
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__NEXT_SLOT_VISIT:
				setNextSlotVisit((SlotVisit)null);
				return;
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__LADEN_PANAMA:
				setLadenPanama((CanalJourneyEvent)null);
				return;
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__BALLAST_PANAMA:
				setBallastPanama((CanalJourneyEvent)null);
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
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__BUY_SLOT_VISIT:
				return buySlotVisit != null;
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__SELL_SLOT_VISIT:
				return sellSlotVisit != null;
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__NEXT_SLOT_VISIT:
				return nextSlotVisit != null;
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__LADEN_PANAMA:
				return ladenPanama != null;
			case AnalyticsPackage.MARKETABILITY_RESULT_CONTAINER__BALLAST_PANAMA:
				return ballastPanama != null;
		}
		return super.eIsSet(featureID);
	}

} //MarketabilityResultContainerImpl
