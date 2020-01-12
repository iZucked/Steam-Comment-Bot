/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResult;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResultSet;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisRow;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;

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
 * An implementation of the model object '<em><b>Break Even Analysis Row</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisRowImpl#getBuyOption <em>Buy Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisRowImpl#getSellOption <em>Sell Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisRowImpl#getShipping <em>Shipping</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisRowImpl#getLhsResults <em>Lhs Results</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisRowImpl#getRhsResults <em>Rhs Results</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisRowImpl#getLhsBasedOn <em>Lhs Based On</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisRowImpl#getRhsBasedOn <em>Rhs Based On</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BreakEvenAnalysisRowImpl extends EObjectImpl implements BreakEvenAnalysisRow {
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
	protected EList<BreakEvenAnalysisResultSet> lhsResults;

	/**
	 * The cached value of the '{@link #getRhsResults() <em>Rhs Results</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRhsResults()
	 * @generated
	 * @ordered
	 */
	protected EList<BreakEvenAnalysisResultSet> rhsResults;

	/**
	 * The cached value of the '{@link #getLhsBasedOn() <em>Lhs Based On</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLhsBasedOn()
	 * @generated
	 * @ordered
	 */
	protected BreakEvenAnalysisResult lhsBasedOn;

	/**
	 * The cached value of the '{@link #getRhsBasedOn() <em>Rhs Based On</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRhsBasedOn()
	 * @generated
	 * @ordered
	 */
	protected BreakEvenAnalysisResult rhsBasedOn;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BreakEvenAnalysisRowImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_ROW;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__BUY_OPTION, oldBuyOption, buyOption));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__BUY_OPTION, oldBuyOption, buyOption));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__SELL_OPTION, oldSellOption, sellOption));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__SELL_OPTION, oldSellOption, sellOption));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__SHIPPING, oldShipping, shipping));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__SHIPPING, oldShipping, shipping));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<BreakEvenAnalysisResultSet> getLhsResults() {
		if (lhsResults == null) {
			lhsResults = new EObjectContainmentEList<BreakEvenAnalysisResultSet>(BreakEvenAnalysisResultSet.class, this, AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__LHS_RESULTS);
		}
		return lhsResults;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<BreakEvenAnalysisResultSet> getRhsResults() {
		if (rhsResults == null) {
			rhsResults = new EObjectContainmentEList<BreakEvenAnalysisResultSet>(BreakEvenAnalysisResultSet.class, this, AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__RHS_RESULTS);
		}
		return rhsResults;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BreakEvenAnalysisResult getLhsBasedOn() {
		if (lhsBasedOn != null && lhsBasedOn.eIsProxy()) {
			InternalEObject oldLhsBasedOn = (InternalEObject)lhsBasedOn;
			lhsBasedOn = (BreakEvenAnalysisResult)eResolveProxy(oldLhsBasedOn);
			if (lhsBasedOn != oldLhsBasedOn) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__LHS_BASED_ON, oldLhsBasedOn, lhsBasedOn));
			}
		}
		return lhsBasedOn;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BreakEvenAnalysisResult basicGetLhsBasedOn() {
		return lhsBasedOn;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLhsBasedOn(BreakEvenAnalysisResult newLhsBasedOn) {
		BreakEvenAnalysisResult oldLhsBasedOn = lhsBasedOn;
		lhsBasedOn = newLhsBasedOn;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__LHS_BASED_ON, oldLhsBasedOn, lhsBasedOn));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BreakEvenAnalysisResult getRhsBasedOn() {
		if (rhsBasedOn != null && rhsBasedOn.eIsProxy()) {
			InternalEObject oldRhsBasedOn = (InternalEObject)rhsBasedOn;
			rhsBasedOn = (BreakEvenAnalysisResult)eResolveProxy(oldRhsBasedOn);
			if (rhsBasedOn != oldRhsBasedOn) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__RHS_BASED_ON, oldRhsBasedOn, rhsBasedOn));
			}
		}
		return rhsBasedOn;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BreakEvenAnalysisResult basicGetRhsBasedOn() {
		return rhsBasedOn;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRhsBasedOn(BreakEvenAnalysisResult newRhsBasedOn) {
		BreakEvenAnalysisResult oldRhsBasedOn = rhsBasedOn;
		rhsBasedOn = newRhsBasedOn;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__RHS_BASED_ON, oldRhsBasedOn, rhsBasedOn));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__LHS_RESULTS:
				return ((InternalEList<?>)getLhsResults()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__RHS_RESULTS:
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
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__BUY_OPTION:
				if (resolve) return getBuyOption();
				return basicGetBuyOption();
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__SELL_OPTION:
				if (resolve) return getSellOption();
				return basicGetSellOption();
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__SHIPPING:
				if (resolve) return getShipping();
				return basicGetShipping();
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__LHS_RESULTS:
				return getLhsResults();
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__RHS_RESULTS:
				return getRhsResults();
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__LHS_BASED_ON:
				if (resolve) return getLhsBasedOn();
				return basicGetLhsBasedOn();
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__RHS_BASED_ON:
				if (resolve) return getRhsBasedOn();
				return basicGetRhsBasedOn();
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
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__BUY_OPTION:
				setBuyOption((BuyOption)newValue);
				return;
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__SELL_OPTION:
				setSellOption((SellOption)newValue);
				return;
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__SHIPPING:
				setShipping((ShippingOption)newValue);
				return;
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__LHS_RESULTS:
				getLhsResults().clear();
				getLhsResults().addAll((Collection<? extends BreakEvenAnalysisResultSet>)newValue);
				return;
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__RHS_RESULTS:
				getRhsResults().clear();
				getRhsResults().addAll((Collection<? extends BreakEvenAnalysisResultSet>)newValue);
				return;
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__LHS_BASED_ON:
				setLhsBasedOn((BreakEvenAnalysisResult)newValue);
				return;
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__RHS_BASED_ON:
				setRhsBasedOn((BreakEvenAnalysisResult)newValue);
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
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__BUY_OPTION:
				setBuyOption((BuyOption)null);
				return;
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__SELL_OPTION:
				setSellOption((SellOption)null);
				return;
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__SHIPPING:
				setShipping((ShippingOption)null);
				return;
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__LHS_RESULTS:
				getLhsResults().clear();
				return;
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__RHS_RESULTS:
				getRhsResults().clear();
				return;
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__LHS_BASED_ON:
				setLhsBasedOn((BreakEvenAnalysisResult)null);
				return;
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__RHS_BASED_ON:
				setRhsBasedOn((BreakEvenAnalysisResult)null);
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
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__BUY_OPTION:
				return buyOption != null;
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__SELL_OPTION:
				return sellOption != null;
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__SHIPPING:
				return shipping != null;
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__LHS_RESULTS:
				return lhsResults != null && !lhsResults.isEmpty();
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__RHS_RESULTS:
				return rhsResults != null && !rhsResults.isEmpty();
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__LHS_BASED_ON:
				return lhsBasedOn != null;
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_ROW__RHS_BASED_ON:
				return rhsBasedOn != null;
		}
		return super.eIsSet(featureID);
	}

} //BreakEvenAnalysisRowImpl
