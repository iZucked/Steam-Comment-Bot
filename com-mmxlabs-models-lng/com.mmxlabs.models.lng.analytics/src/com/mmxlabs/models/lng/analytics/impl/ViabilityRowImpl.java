/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.ViabilityResult;
import com.mmxlabs.models.lng.analytics.ViabilityRow;

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
 * An implementation of the model object '<em><b>Viability Row</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ViabilityRowImpl#getBuyOption <em>Buy Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ViabilityRowImpl#getSellOption <em>Sell Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ViabilityRowImpl#getShipping <em>Shipping</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ViabilityRowImpl#getLhsResults <em>Lhs Results</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ViabilityRowImpl#getRhsResults <em>Rhs Results</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ViabilityRowImpl#getTarget <em>Target</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ViabilityRowImpl#getPrice <em>Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ViabilityRowImpl#getEta <em>Eta</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ViabilityRowImpl#getReferencePrice <em>Reference Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ViabilityRowImpl#getStartVolume <em>Start Volume</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ViabilityRowImpl extends EObjectImpl implements ViabilityRow {
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
	protected EList<ViabilityResult> lhsResults;

	/**
	 * The cached value of the '{@link #getRhsResults() <em>Rhs Results</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRhsResults()
	 * @generated
	 * @ordered
	 */
	protected EList<ViabilityResult> rhsResults;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ViabilityRowImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.VIABILITY_ROW;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.VIABILITY_ROW__BUY_OPTION, oldBuyOption, buyOption));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.VIABILITY_ROW__BUY_OPTION, oldBuyOption, buyOption));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.VIABILITY_ROW__SELL_OPTION, oldSellOption, sellOption));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.VIABILITY_ROW__SELL_OPTION, oldSellOption, sellOption));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.VIABILITY_ROW__SHIPPING, oldShipping, shipping));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.VIABILITY_ROW__SHIPPING, oldShipping, shipping));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<ViabilityResult> getLhsResults() {
		if (lhsResults == null) {
			lhsResults = new EObjectContainmentEList<ViabilityResult>(ViabilityResult.class, this, AnalyticsPackage.VIABILITY_ROW__LHS_RESULTS);
		}
		return lhsResults;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<ViabilityResult> getRhsResults() {
		if (rhsResults == null) {
			rhsResults = new EObjectContainmentEList<ViabilityResult>(ViabilityResult.class, this, AnalyticsPackage.VIABILITY_ROW__RHS_RESULTS);
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.VIABILITY_ROW__TARGET, oldTarget, target));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.VIABILITY_ROW__TARGET, oldTarget, target));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.VIABILITY_ROW__PRICE, oldPrice, price));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.VIABILITY_ROW__ETA, oldEta, eta));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.VIABILITY_ROW__REFERENCE_PRICE, oldReferencePrice, referencePrice));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.VIABILITY_ROW__START_VOLUME, oldStartVolume, startVolume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.VIABILITY_ROW__LHS_RESULTS:
				return ((InternalEList<?>)getLhsResults()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.VIABILITY_ROW__RHS_RESULTS:
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
			case AnalyticsPackage.VIABILITY_ROW__BUY_OPTION:
				if (resolve) return getBuyOption();
				return basicGetBuyOption();
			case AnalyticsPackage.VIABILITY_ROW__SELL_OPTION:
				if (resolve) return getSellOption();
				return basicGetSellOption();
			case AnalyticsPackage.VIABILITY_ROW__SHIPPING:
				if (resolve) return getShipping();
				return basicGetShipping();
			case AnalyticsPackage.VIABILITY_ROW__LHS_RESULTS:
				return getLhsResults();
			case AnalyticsPackage.VIABILITY_ROW__RHS_RESULTS:
				return getRhsResults();
			case AnalyticsPackage.VIABILITY_ROW__TARGET:
				if (resolve) return getTarget();
				return basicGetTarget();
			case AnalyticsPackage.VIABILITY_ROW__PRICE:
				return getPrice();
			case AnalyticsPackage.VIABILITY_ROW__ETA:
				return getEta();
			case AnalyticsPackage.VIABILITY_ROW__REFERENCE_PRICE:
				return getReferencePrice();
			case AnalyticsPackage.VIABILITY_ROW__START_VOLUME:
				return getStartVolume();
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
			case AnalyticsPackage.VIABILITY_ROW__BUY_OPTION:
				setBuyOption((BuyOption)newValue);
				return;
			case AnalyticsPackage.VIABILITY_ROW__SELL_OPTION:
				setSellOption((SellOption)newValue);
				return;
			case AnalyticsPackage.VIABILITY_ROW__SHIPPING:
				setShipping((ShippingOption)newValue);
				return;
			case AnalyticsPackage.VIABILITY_ROW__LHS_RESULTS:
				getLhsResults().clear();
				getLhsResults().addAll((Collection<? extends ViabilityResult>)newValue);
				return;
			case AnalyticsPackage.VIABILITY_ROW__RHS_RESULTS:
				getRhsResults().clear();
				getRhsResults().addAll((Collection<? extends ViabilityResult>)newValue);
				return;
			case AnalyticsPackage.VIABILITY_ROW__TARGET:
				setTarget((EObject)newValue);
				return;
			case AnalyticsPackage.VIABILITY_ROW__PRICE:
				setPrice((Double)newValue);
				return;
			case AnalyticsPackage.VIABILITY_ROW__ETA:
				setEta((LocalDate)newValue);
				return;
			case AnalyticsPackage.VIABILITY_ROW__REFERENCE_PRICE:
				setReferencePrice((Double)newValue);
				return;
			case AnalyticsPackage.VIABILITY_ROW__START_VOLUME:
				setStartVolume((Long)newValue);
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
			case AnalyticsPackage.VIABILITY_ROW__BUY_OPTION:
				setBuyOption((BuyOption)null);
				return;
			case AnalyticsPackage.VIABILITY_ROW__SELL_OPTION:
				setSellOption((SellOption)null);
				return;
			case AnalyticsPackage.VIABILITY_ROW__SHIPPING:
				setShipping((ShippingOption)null);
				return;
			case AnalyticsPackage.VIABILITY_ROW__LHS_RESULTS:
				getLhsResults().clear();
				return;
			case AnalyticsPackage.VIABILITY_ROW__RHS_RESULTS:
				getRhsResults().clear();
				return;
			case AnalyticsPackage.VIABILITY_ROW__TARGET:
				setTarget((EObject)null);
				return;
			case AnalyticsPackage.VIABILITY_ROW__PRICE:
				setPrice(PRICE_EDEFAULT);
				return;
			case AnalyticsPackage.VIABILITY_ROW__ETA:
				setEta(ETA_EDEFAULT);
				return;
			case AnalyticsPackage.VIABILITY_ROW__REFERENCE_PRICE:
				setReferencePrice(REFERENCE_PRICE_EDEFAULT);
				return;
			case AnalyticsPackage.VIABILITY_ROW__START_VOLUME:
				setStartVolume(START_VOLUME_EDEFAULT);
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
			case AnalyticsPackage.VIABILITY_ROW__BUY_OPTION:
				return buyOption != null;
			case AnalyticsPackage.VIABILITY_ROW__SELL_OPTION:
				return sellOption != null;
			case AnalyticsPackage.VIABILITY_ROW__SHIPPING:
				return shipping != null;
			case AnalyticsPackage.VIABILITY_ROW__LHS_RESULTS:
				return lhsResults != null && !lhsResults.isEmpty();
			case AnalyticsPackage.VIABILITY_ROW__RHS_RESULTS:
				return rhsResults != null && !rhsResults.isEmpty();
			case AnalyticsPackage.VIABILITY_ROW__TARGET:
				return target != null;
			case AnalyticsPackage.VIABILITY_ROW__PRICE:
				return price != PRICE_EDEFAULT;
			case AnalyticsPackage.VIABILITY_ROW__ETA:
				return ETA_EDEFAULT == null ? eta != null : !ETA_EDEFAULT.equals(eta);
			case AnalyticsPackage.VIABILITY_ROW__REFERENCE_PRICE:
				return referencePrice != REFERENCE_PRICE_EDEFAULT;
			case AnalyticsPackage.VIABILITY_ROW__START_VOLUME:
				return startVolume != START_VOLUME_EDEFAULT;
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

} //ViabilityRowImpl
