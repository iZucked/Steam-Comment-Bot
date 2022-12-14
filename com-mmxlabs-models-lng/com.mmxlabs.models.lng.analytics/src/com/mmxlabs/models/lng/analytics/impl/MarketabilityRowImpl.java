/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.MarketabilityResult;
import com.mmxlabs.models.lng.analytics.MarketabilityRow;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.ViabilityResult;

import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import java.time.LocalDate;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Marketability Row</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityRowImpl#getBuyOption <em>Buy Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityRowImpl#getSellOption <em>Sell Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityRowImpl#getShipping <em>Shipping</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityRowImpl#getRhsResults <em>Rhs Results</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityRowImpl#getBuySlotAllocation <em>Buy Slot Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityRowImpl#getSellSlotAllocation <em>Sell Slot Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityRowImpl#getNextSlotVisit <em>Next Slot Visit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityRowImpl#getLadenPanama <em>Laden Panama</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityRowImpl#getBallastPanama <em>Ballast Panama</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MarketabilityRowImpl extends EObjectImpl implements MarketabilityRow {
	/**
	 * The cached value of the '{@link #getBuyOption() <em>Buy Option</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBuyOption()
	 * @generated
	 * @ordered
	 */
	protected BuyOption buyOption;

	/**
	 * The cached value of the '{@link #getSellOption() <em>Sell Option</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSellOption()
	 * @generated
	 * @ordered
	 */
	protected SellOption sellOption;

	/**
	 * The cached value of the '{@link #getShipping() <em>Shipping</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShipping()
	 * @generated
	 * @ordered
	 */
	protected ShippingOption shipping;

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
	 * The cached value of the '{@link #getBuySlotAllocation() <em>Buy Slot Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBuySlotAllocation()
	 * @generated
	 * @ordered
	 */
	protected SlotAllocation buySlotAllocation;

	/**
	 * The cached value of the '{@link #getSellSlotAllocation() <em>Sell Slot Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSellSlotAllocation()
	 * @generated
	 * @ordered
	 */
	protected SlotAllocation sellSlotAllocation;

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
	protected Journey ladenPanama;

	/**
	 * The cached value of the '{@link #getBallastPanama() <em>Ballast Panama</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastPanama()
	 * @generated
	 * @ordered
	 */
	protected Journey ballastPanama;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MarketabilityRowImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.MARKETABILITY_ROW;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BuyOption getBuyOption() {
		if (buyOption != null && buyOption.eIsProxy()) {
			InternalEObject oldBuyOption = (InternalEObject)buyOption;
			buyOption = (BuyOption)eResolveProxy(oldBuyOption);
			if (buyOption != oldBuyOption) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.MARKETABILITY_ROW__BUY_OPTION, oldBuyOption, buyOption));
			}
		}
		return buyOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BuyOption basicGetBuyOption() {
		return buyOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBuyOption(BuyOption newBuyOption) {
		BuyOption oldBuyOption = buyOption;
		buyOption = newBuyOption;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MARKETABILITY_ROW__BUY_OPTION, oldBuyOption, buyOption));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SellOption getSellOption() {
		if (sellOption != null && sellOption.eIsProxy()) {
			InternalEObject oldSellOption = (InternalEObject)sellOption;
			sellOption = (SellOption)eResolveProxy(oldSellOption);
			if (sellOption != oldSellOption) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.MARKETABILITY_ROW__SELL_OPTION, oldSellOption, sellOption));
			}
		}
		return sellOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SellOption basicGetSellOption() {
		return sellOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSellOption(SellOption newSellOption) {
		SellOption oldSellOption = sellOption;
		sellOption = newSellOption;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MARKETABILITY_ROW__SELL_OPTION, oldSellOption, sellOption));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ShippingOption getShipping() {
		if (shipping != null && shipping.eIsProxy()) {
			InternalEObject oldShipping = (InternalEObject)shipping;
			shipping = (ShippingOption)eResolveProxy(oldShipping);
			if (shipping != oldShipping) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.MARKETABILITY_ROW__SHIPPING, oldShipping, shipping));
			}
		}
		return shipping;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ShippingOption basicGetShipping() {
		return shipping;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setShipping(ShippingOption newShipping) {
		ShippingOption oldShipping = shipping;
		shipping = newShipping;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MARKETABILITY_ROW__SHIPPING, oldShipping, shipping));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<MarketabilityResult> getRhsResults() {
		if (rhsResults == null) {
			rhsResults = new EObjectContainmentEList<MarketabilityResult>(MarketabilityResult.class, this, AnalyticsPackage.MARKETABILITY_ROW__RHS_RESULTS);
		}
		return rhsResults;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SlotAllocation getBuySlotAllocation() {
		if (buySlotAllocation != null && buySlotAllocation.eIsProxy()) {
			InternalEObject oldBuySlotAllocation = (InternalEObject)buySlotAllocation;
			buySlotAllocation = (SlotAllocation)eResolveProxy(oldBuySlotAllocation);
			if (buySlotAllocation != oldBuySlotAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.MARKETABILITY_ROW__BUY_SLOT_ALLOCATION, oldBuySlotAllocation, buySlotAllocation));
			}
		}
		return buySlotAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocation basicGetBuySlotAllocation() {
		return buySlotAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBuySlotAllocation(SlotAllocation newBuySlotAllocation) {
		SlotAllocation oldBuySlotAllocation = buySlotAllocation;
		buySlotAllocation = newBuySlotAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MARKETABILITY_ROW__BUY_SLOT_ALLOCATION, oldBuySlotAllocation, buySlotAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SlotAllocation getSellSlotAllocation() {
		if (sellSlotAllocation != null && sellSlotAllocation.eIsProxy()) {
			InternalEObject oldSellSlotAllocation = (InternalEObject)sellSlotAllocation;
			sellSlotAllocation = (SlotAllocation)eResolveProxy(oldSellSlotAllocation);
			if (sellSlotAllocation != oldSellSlotAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.MARKETABILITY_ROW__SELL_SLOT_ALLOCATION, oldSellSlotAllocation, sellSlotAllocation));
			}
		}
		return sellSlotAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocation basicGetSellSlotAllocation() {
		return sellSlotAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSellSlotAllocation(SlotAllocation newSellSlotAllocation) {
		SlotAllocation oldSellSlotAllocation = sellSlotAllocation;
		sellSlotAllocation = newSellSlotAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MARKETABILITY_ROW__SELL_SLOT_ALLOCATION, oldSellSlotAllocation, sellSlotAllocation));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.MARKETABILITY_ROW__NEXT_SLOT_VISIT, oldNextSlotVisit, nextSlotVisit));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MARKETABILITY_ROW__NEXT_SLOT_VISIT, oldNextSlotVisit, nextSlotVisit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Journey getLadenPanama() {
		if (ladenPanama != null && ladenPanama.eIsProxy()) {
			InternalEObject oldLadenPanama = (InternalEObject)ladenPanama;
			ladenPanama = (Journey)eResolveProxy(oldLadenPanama);
			if (ladenPanama != oldLadenPanama) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.MARKETABILITY_ROW__LADEN_PANAMA, oldLadenPanama, ladenPanama));
			}
		}
		return ladenPanama;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Journey basicGetLadenPanama() {
		return ladenPanama;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLadenPanama(Journey newLadenPanama) {
		Journey oldLadenPanama = ladenPanama;
		ladenPanama = newLadenPanama;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MARKETABILITY_ROW__LADEN_PANAMA, oldLadenPanama, ladenPanama));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Journey getBallastPanama() {
		if (ballastPanama != null && ballastPanama.eIsProxy()) {
			InternalEObject oldBallastPanama = (InternalEObject)ballastPanama;
			ballastPanama = (Journey)eResolveProxy(oldBallastPanama);
			if (ballastPanama != oldBallastPanama) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.MARKETABILITY_ROW__BALLAST_PANAMA, oldBallastPanama, ballastPanama));
			}
		}
		return ballastPanama;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Journey basicGetBallastPanama() {
		return ballastPanama;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBallastPanama(Journey newBallastPanama) {
		Journey oldBallastPanama = ballastPanama;
		ballastPanama = newBallastPanama;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MARKETABILITY_ROW__BALLAST_PANAMA, oldBallastPanama, ballastPanama));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.MARKETABILITY_ROW__RHS_RESULTS:
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
			case AnalyticsPackage.MARKETABILITY_ROW__BUY_OPTION:
				if (resolve) return getBuyOption();
				return basicGetBuyOption();
			case AnalyticsPackage.MARKETABILITY_ROW__SELL_OPTION:
				if (resolve) return getSellOption();
				return basicGetSellOption();
			case AnalyticsPackage.MARKETABILITY_ROW__SHIPPING:
				if (resolve) return getShipping();
				return basicGetShipping();
			case AnalyticsPackage.MARKETABILITY_ROW__RHS_RESULTS:
				return getRhsResults();
			case AnalyticsPackage.MARKETABILITY_ROW__BUY_SLOT_ALLOCATION:
				if (resolve) return getBuySlotAllocation();
				return basicGetBuySlotAllocation();
			case AnalyticsPackage.MARKETABILITY_ROW__SELL_SLOT_ALLOCATION:
				if (resolve) return getSellSlotAllocation();
				return basicGetSellSlotAllocation();
			case AnalyticsPackage.MARKETABILITY_ROW__NEXT_SLOT_VISIT:
				if (resolve) return getNextSlotVisit();
				return basicGetNextSlotVisit();
			case AnalyticsPackage.MARKETABILITY_ROW__LADEN_PANAMA:
				if (resolve) return getLadenPanama();
				return basicGetLadenPanama();
			case AnalyticsPackage.MARKETABILITY_ROW__BALLAST_PANAMA:
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
			case AnalyticsPackage.MARKETABILITY_ROW__BUY_OPTION:
				setBuyOption((BuyOption)newValue);
				return;
			case AnalyticsPackage.MARKETABILITY_ROW__SELL_OPTION:
				setSellOption((SellOption)newValue);
				return;
			case AnalyticsPackage.MARKETABILITY_ROW__SHIPPING:
				setShipping((ShippingOption)newValue);
				return;
			case AnalyticsPackage.MARKETABILITY_ROW__RHS_RESULTS:
				getRhsResults().clear();
				getRhsResults().addAll((Collection<? extends MarketabilityResult>)newValue);
				return;
			case AnalyticsPackage.MARKETABILITY_ROW__BUY_SLOT_ALLOCATION:
				setBuySlotAllocation((SlotAllocation)newValue);
				return;
			case AnalyticsPackage.MARKETABILITY_ROW__SELL_SLOT_ALLOCATION:
				setSellSlotAllocation((SlotAllocation)newValue);
				return;
			case AnalyticsPackage.MARKETABILITY_ROW__NEXT_SLOT_VISIT:
				setNextSlotVisit((SlotVisit)newValue);
				return;
			case AnalyticsPackage.MARKETABILITY_ROW__LADEN_PANAMA:
				setLadenPanama((Journey)newValue);
				return;
			case AnalyticsPackage.MARKETABILITY_ROW__BALLAST_PANAMA:
				setBallastPanama((Journey)newValue);
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
			case AnalyticsPackage.MARKETABILITY_ROW__BUY_OPTION:
				setBuyOption((BuyOption)null);
				return;
			case AnalyticsPackage.MARKETABILITY_ROW__SELL_OPTION:
				setSellOption((SellOption)null);
				return;
			case AnalyticsPackage.MARKETABILITY_ROW__SHIPPING:
				setShipping((ShippingOption)null);
				return;
			case AnalyticsPackage.MARKETABILITY_ROW__RHS_RESULTS:
				getRhsResults().clear();
				return;
			case AnalyticsPackage.MARKETABILITY_ROW__BUY_SLOT_ALLOCATION:
				setBuySlotAllocation((SlotAllocation)null);
				return;
			case AnalyticsPackage.MARKETABILITY_ROW__SELL_SLOT_ALLOCATION:
				setSellSlotAllocation((SlotAllocation)null);
				return;
			case AnalyticsPackage.MARKETABILITY_ROW__NEXT_SLOT_VISIT:
				setNextSlotVisit((SlotVisit)null);
				return;
			case AnalyticsPackage.MARKETABILITY_ROW__LADEN_PANAMA:
				setLadenPanama((Journey)null);
				return;
			case AnalyticsPackage.MARKETABILITY_ROW__BALLAST_PANAMA:
				setBallastPanama((Journey)null);
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
			case AnalyticsPackage.MARKETABILITY_ROW__BUY_OPTION:
				return buyOption != null;
			case AnalyticsPackage.MARKETABILITY_ROW__SELL_OPTION:
				return sellOption != null;
			case AnalyticsPackage.MARKETABILITY_ROW__SHIPPING:
				return shipping != null;
			case AnalyticsPackage.MARKETABILITY_ROW__RHS_RESULTS:
				return rhsResults != null && !rhsResults.isEmpty();
			case AnalyticsPackage.MARKETABILITY_ROW__BUY_SLOT_ALLOCATION:
				return buySlotAllocation != null;
			case AnalyticsPackage.MARKETABILITY_ROW__SELL_SLOT_ALLOCATION:
				return sellSlotAllocation != null;
			case AnalyticsPackage.MARKETABILITY_ROW__NEXT_SLOT_VISIT:
				return nextSlotVisit != null;
			case AnalyticsPackage.MARKETABILITY_ROW__LADEN_PANAMA:
				return ladenPanama != null;
			case AnalyticsPackage.MARKETABILITY_ROW__BALLAST_PANAMA:
				return ballastPanama != null;
		}
		return super.eIsSet(featureID);
	}

} //MarketabilityRowImpl
