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
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityRowImpl#getLhsResults <em>Lhs Results</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityRowImpl#getRhsResults <em>Rhs Results</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityRowImpl#getTarget <em>Target</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityRowImpl#getPrice <em>Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityRowImpl#getEta <em>Eta</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityRowImpl#getReferencePrice <em>Reference Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityRowImpl#getStartVolume <em>Start Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityRowImpl#getBuySlotAllocation <em>Buy Slot Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityRowImpl#getSellSlotAllocation <em>Sell Slot Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityRowImpl#getNextSlotVisit <em>Next Slot Visit</em>}</li>
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
	 * The cached value of the '{@link #getLhsResults() <em>Lhs Results</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLhsResults()
	 * @generated
	 * @ordered
	 */
	protected EList<MarketabilityResult> lhsResults;

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
	 * The cached value of the '{@link #getTarget() <em>Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTarget()
	 * @generated
	 * @ordered
	 */
	protected EObject target;

	/**
	 * The default value of the '{@link #getPrice() <em>Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrice()
	 * @generated
	 * @ordered
	 */
	protected static final double PRICE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getPrice() <em>Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrice()
	 * @generated
	 * @ordered
	 */
	protected double price = PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getEta() <em>Eta</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEta()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate ETA_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEta() <em>Eta</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEta()
	 * @generated
	 * @ordered
	 */
	protected LocalDate eta = ETA_EDEFAULT;

	/**
	 * The default value of the '{@link #getReferencePrice() <em>Reference Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferencePrice()
	 * @generated
	 * @ordered
	 */
	protected static final double REFERENCE_PRICE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getReferencePrice() <em>Reference Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferencePrice()
	 * @generated
	 * @ordered
	 */
	protected double referencePrice = REFERENCE_PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getStartVolume() <em>Start Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartVolume()
	 * @generated
	 * @ordered
	 */
	protected static final long START_VOLUME_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getStartVolume() <em>Start Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartVolume()
	 * @generated
	 * @ordered
	 */
	protected long startVolume = START_VOLUME_EDEFAULT;

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
	public EList<MarketabilityResult> getLhsResults() {
		if (lhsResults == null) {
			lhsResults = new EObjectContainmentEList<MarketabilityResult>(MarketabilityResult.class, this, AnalyticsPackage.MARKETABILITY_ROW__LHS_RESULTS);
		}
		return lhsResults;
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
	public EObject getTarget() {
		if (target != null && target.eIsProxy()) {
			InternalEObject oldTarget = (InternalEObject)target;
			target = eResolveProxy(oldTarget);
			if (target != oldTarget) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.MARKETABILITY_ROW__TARGET, oldTarget, target));
			}
		}
		return target;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetTarget() {
		return target;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTarget(EObject newTarget) {
		EObject oldTarget = target;
		target = newTarget;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MARKETABILITY_ROW__TARGET, oldTarget, target));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getPrice() {
		return price;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPrice(double newPrice) {
		double oldPrice = price;
		price = newPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MARKETABILITY_ROW__PRICE, oldPrice, price));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDate getEta() {
		return eta;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEta(LocalDate newEta) {
		LocalDate oldEta = eta;
		eta = newEta;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MARKETABILITY_ROW__ETA, oldEta, eta));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getReferencePrice() {
		return referencePrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setReferencePrice(double newReferencePrice) {
		double oldReferencePrice = referencePrice;
		referencePrice = newReferencePrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MARKETABILITY_ROW__REFERENCE_PRICE, oldReferencePrice, referencePrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getStartVolume() {
		return startVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setStartVolume(long newStartVolume) {
		long oldStartVolume = startVolume;
		startVolume = newStartVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MARKETABILITY_ROW__START_VOLUME, oldStartVolume, startVolume));
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
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.MARKETABILITY_ROW__LHS_RESULTS:
				return ((InternalEList<?>)getLhsResults()).basicRemove(otherEnd, msgs);
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
			case AnalyticsPackage.MARKETABILITY_ROW__LHS_RESULTS:
				return getLhsResults();
			case AnalyticsPackage.MARKETABILITY_ROW__RHS_RESULTS:
				return getRhsResults();
			case AnalyticsPackage.MARKETABILITY_ROW__TARGET:
				if (resolve) return getTarget();
				return basicGetTarget();
			case AnalyticsPackage.MARKETABILITY_ROW__PRICE:
				return getPrice();
			case AnalyticsPackage.MARKETABILITY_ROW__ETA:
				return getEta();
			case AnalyticsPackage.MARKETABILITY_ROW__REFERENCE_PRICE:
				return getReferencePrice();
			case AnalyticsPackage.MARKETABILITY_ROW__START_VOLUME:
				return getStartVolume();
			case AnalyticsPackage.MARKETABILITY_ROW__BUY_SLOT_ALLOCATION:
				if (resolve) return getBuySlotAllocation();
				return basicGetBuySlotAllocation();
			case AnalyticsPackage.MARKETABILITY_ROW__SELL_SLOT_ALLOCATION:
				if (resolve) return getSellSlotAllocation();
				return basicGetSellSlotAllocation();
			case AnalyticsPackage.MARKETABILITY_ROW__NEXT_SLOT_VISIT:
				if (resolve) return getNextSlotVisit();
				return basicGetNextSlotVisit();
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
			case AnalyticsPackage.MARKETABILITY_ROW__LHS_RESULTS:
				getLhsResults().clear();
				getLhsResults().addAll((Collection<? extends MarketabilityResult>)newValue);
				return;
			case AnalyticsPackage.MARKETABILITY_ROW__RHS_RESULTS:
				getRhsResults().clear();
				getRhsResults().addAll((Collection<? extends MarketabilityResult>)newValue);
				return;
			case AnalyticsPackage.MARKETABILITY_ROW__TARGET:
				setTarget((EObject)newValue);
				return;
			case AnalyticsPackage.MARKETABILITY_ROW__PRICE:
				setPrice((Double)newValue);
				return;
			case AnalyticsPackage.MARKETABILITY_ROW__ETA:
				setEta((LocalDate)newValue);
				return;
			case AnalyticsPackage.MARKETABILITY_ROW__REFERENCE_PRICE:
				setReferencePrice((Double)newValue);
				return;
			case AnalyticsPackage.MARKETABILITY_ROW__START_VOLUME:
				setStartVolume((Long)newValue);
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
			case AnalyticsPackage.MARKETABILITY_ROW__LHS_RESULTS:
				getLhsResults().clear();
				return;
			case AnalyticsPackage.MARKETABILITY_ROW__RHS_RESULTS:
				getRhsResults().clear();
				return;
			case AnalyticsPackage.MARKETABILITY_ROW__TARGET:
				setTarget((EObject)null);
				return;
			case AnalyticsPackage.MARKETABILITY_ROW__PRICE:
				setPrice(PRICE_EDEFAULT);
				return;
			case AnalyticsPackage.MARKETABILITY_ROW__ETA:
				setEta(ETA_EDEFAULT);
				return;
			case AnalyticsPackage.MARKETABILITY_ROW__REFERENCE_PRICE:
				setReferencePrice(REFERENCE_PRICE_EDEFAULT);
				return;
			case AnalyticsPackage.MARKETABILITY_ROW__START_VOLUME:
				setStartVolume(START_VOLUME_EDEFAULT);
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
			case AnalyticsPackage.MARKETABILITY_ROW__LHS_RESULTS:
				return lhsResults != null && !lhsResults.isEmpty();
			case AnalyticsPackage.MARKETABILITY_ROW__RHS_RESULTS:
				return rhsResults != null && !rhsResults.isEmpty();
			case AnalyticsPackage.MARKETABILITY_ROW__TARGET:
				return target != null;
			case AnalyticsPackage.MARKETABILITY_ROW__PRICE:
				return price != PRICE_EDEFAULT;
			case AnalyticsPackage.MARKETABILITY_ROW__ETA:
				return ETA_EDEFAULT == null ? eta != null : !ETA_EDEFAULT.equals(eta);
			case AnalyticsPackage.MARKETABILITY_ROW__REFERENCE_PRICE:
				return referencePrice != REFERENCE_PRICE_EDEFAULT;
			case AnalyticsPackage.MARKETABILITY_ROW__START_VOLUME:
				return startVolume != START_VOLUME_EDEFAULT;
			case AnalyticsPackage.MARKETABILITY_ROW__BUY_SLOT_ALLOCATION:
				return buySlotAllocation != null;
			case AnalyticsPackage.MARKETABILITY_ROW__SELL_SLOT_ALLOCATION:
				return sellSlotAllocation != null;
			case AnalyticsPackage.MARKETABILITY_ROW__NEXT_SLOT_VISIT:
				return nextSlotVisit != null;
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
		result.append(" (price: ");
		result.append(price);
		result.append(", eta: ");
		result.append(eta);
		result.append(", referencePrice: ");
		result.append(referencePrice);
		result.append(", startVolume: ");
		result.append(startVolume);
		result.append(')');
		return result.toString();
	}

} //MarketabilityRowImpl
