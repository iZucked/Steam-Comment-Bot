/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;

import com.mmxlabs.models.lng.analytics.VesselEventOption;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Base Case Row</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BaseCaseRowImpl#getBuyOption <em>Buy Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BaseCaseRowImpl#getSellOption <em>Sell Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BaseCaseRowImpl#getVesselEventOption <em>Vessel Event Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BaseCaseRowImpl#getShipping <em>Shipping</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BaseCaseRowImpl#isOptionise <em>Optionise</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BaseCaseRowImpl extends EObjectImpl implements BaseCaseRow {
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
	 * The cached value of the '{@link #getVesselEventOption() <em>Vessel Event Option</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselEventOption()
	 * @generated
	 * @ordered
	 */
	protected VesselEventOption vesselEventOption;

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
	 * The default value of the '{@link #isOptionise() <em>Optionise</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isOptionise()
	 * @generated
	 * @ordered
	 */
	protected static final boolean OPTIONISE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isOptionise() <em>Optionise</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isOptionise()
	 * @generated
	 * @ordered
	 */
	protected boolean optionise = OPTIONISE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BaseCaseRowImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.BASE_CASE_ROW;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.BASE_CASE_ROW__BUY_OPTION, oldBuyOption, buyOption));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BASE_CASE_ROW__BUY_OPTION, oldBuyOption, buyOption));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.BASE_CASE_ROW__SELL_OPTION, oldSellOption, sellOption));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BASE_CASE_ROW__SELL_OPTION, oldSellOption, sellOption));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VesselEventOption getVesselEventOption() {
		if (vesselEventOption != null && vesselEventOption.eIsProxy()) {
			InternalEObject oldVesselEventOption = (InternalEObject)vesselEventOption;
			vesselEventOption = (VesselEventOption)eResolveProxy(oldVesselEventOption);
			if (vesselEventOption != oldVesselEventOption) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.BASE_CASE_ROW__VESSEL_EVENT_OPTION, oldVesselEventOption, vesselEventOption));
			}
		}
		return vesselEventOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselEventOption basicGetVesselEventOption() {
		return vesselEventOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVesselEventOption(VesselEventOption newVesselEventOption) {
		VesselEventOption oldVesselEventOption = vesselEventOption;
		vesselEventOption = newVesselEventOption;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BASE_CASE_ROW__VESSEL_EVENT_OPTION, oldVesselEventOption, vesselEventOption));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.BASE_CASE_ROW__SHIPPING, oldShipping, shipping));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BASE_CASE_ROW__SHIPPING, oldShipping, shipping));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isOptionise() {
		return optionise;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOptionise(boolean newOptionise) {
		boolean oldOptionise = optionise;
		optionise = newOptionise;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BASE_CASE_ROW__OPTIONISE, oldOptionise, optionise));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AnalyticsPackage.BASE_CASE_ROW__BUY_OPTION:
				if (resolve) return getBuyOption();
				return basicGetBuyOption();
			case AnalyticsPackage.BASE_CASE_ROW__SELL_OPTION:
				if (resolve) return getSellOption();
				return basicGetSellOption();
			case AnalyticsPackage.BASE_CASE_ROW__VESSEL_EVENT_OPTION:
				if (resolve) return getVesselEventOption();
				return basicGetVesselEventOption();
			case AnalyticsPackage.BASE_CASE_ROW__SHIPPING:
				if (resolve) return getShipping();
				return basicGetShipping();
			case AnalyticsPackage.BASE_CASE_ROW__OPTIONISE:
				return isOptionise();
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
			case AnalyticsPackage.BASE_CASE_ROW__BUY_OPTION:
				setBuyOption((BuyOption)newValue);
				return;
			case AnalyticsPackage.BASE_CASE_ROW__SELL_OPTION:
				setSellOption((SellOption)newValue);
				return;
			case AnalyticsPackage.BASE_CASE_ROW__VESSEL_EVENT_OPTION:
				setVesselEventOption((VesselEventOption)newValue);
				return;
			case AnalyticsPackage.BASE_CASE_ROW__SHIPPING:
				setShipping((ShippingOption)newValue);
				return;
			case AnalyticsPackage.BASE_CASE_ROW__OPTIONISE:
				setOptionise((Boolean)newValue);
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
			case AnalyticsPackage.BASE_CASE_ROW__BUY_OPTION:
				setBuyOption((BuyOption)null);
				return;
			case AnalyticsPackage.BASE_CASE_ROW__SELL_OPTION:
				setSellOption((SellOption)null);
				return;
			case AnalyticsPackage.BASE_CASE_ROW__VESSEL_EVENT_OPTION:
				setVesselEventOption((VesselEventOption)null);
				return;
			case AnalyticsPackage.BASE_CASE_ROW__SHIPPING:
				setShipping((ShippingOption)null);
				return;
			case AnalyticsPackage.BASE_CASE_ROW__OPTIONISE:
				setOptionise(OPTIONISE_EDEFAULT);
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
			case AnalyticsPackage.BASE_CASE_ROW__BUY_OPTION:
				return buyOption != null;
			case AnalyticsPackage.BASE_CASE_ROW__SELL_OPTION:
				return sellOption != null;
			case AnalyticsPackage.BASE_CASE_ROW__VESSEL_EVENT_OPTION:
				return vesselEventOption != null;
			case AnalyticsPackage.BASE_CASE_ROW__SHIPPING:
				return shipping != null;
			case AnalyticsPackage.BASE_CASE_ROW__OPTIONISE:
				return optionise != OPTIONISE_EDEFAULT;
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
		result.append(" (optionise: ");
		result.append(optionise);
		result.append(')');
		return result.toString();
	}

} //BaseCaseRowImpl
