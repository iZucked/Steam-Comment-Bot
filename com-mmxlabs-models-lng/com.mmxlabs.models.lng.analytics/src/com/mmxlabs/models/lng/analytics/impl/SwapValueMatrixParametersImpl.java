/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.Range;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters;

import com.mmxlabs.models.lng.cargo.VesselCharter;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Swap Value Matrix Parameters</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixParametersImpl#getBaseLoad <em>Base Load</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixParametersImpl#getBaseDischarge <em>Base Discharge</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixParametersImpl#getBaseVesselCharter <em>Base Vessel Charter</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixParametersImpl#getBasePriceRange <em>Base Price Range</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixParametersImpl#getSwapLoadMarket <em>Swap Load Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixParametersImpl#getSwapDischargeMarket <em>Swap Discharge Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixParametersImpl#getSwapFee <em>Swap Fee</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixParametersImpl#getSwapPriceRange <em>Swap Price Range</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SwapValueMatrixParametersImpl extends EObjectImpl implements SwapValueMatrixParameters {
	/**
	 * The cached value of the '{@link #getBaseLoad() <em>Base Load</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseLoad()
	 * @generated
	 * @ordered
	 */
	protected BuyReference baseLoad;

	/**
	 * The cached value of the '{@link #getBaseDischarge() <em>Base Discharge</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseDischarge()
	 * @generated
	 * @ordered
	 */
	protected SellReference baseDischarge;

	/**
	 * The cached value of the '{@link #getBaseVesselCharter() <em>Base Vessel Charter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseVesselCharter()
	 * @generated
	 * @ordered
	 */
	protected ExistingVesselCharterOption baseVesselCharter;

	/**
	 * The cached value of the '{@link #getBasePriceRange() <em>Base Price Range</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBasePriceRange()
	 * @generated
	 * @ordered
	 */
	protected Range basePriceRange;

	/**
	 * The cached value of the '{@link #getSwapLoadMarket() <em>Swap Load Market</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapLoadMarket()
	 * @generated
	 * @ordered
	 */
	protected BuyMarket swapLoadMarket;

	/**
	 * The cached value of the '{@link #getSwapDischargeMarket() <em>Swap Discharge Market</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapDischargeMarket()
	 * @generated
	 * @ordered
	 */
	protected SellMarket swapDischargeMarket;

	/**
	 * The default value of the '{@link #getSwapFee() <em>Swap Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapFee()
	 * @generated
	 * @ordered
	 */
	protected static final double SWAP_FEE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getSwapFee() <em>Swap Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapFee()
	 * @generated
	 * @ordered
	 */
	protected double swapFee = SWAP_FEE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getSwapPriceRange() <em>Swap Price Range</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapPriceRange()
	 * @generated
	 * @ordered
	 */
	protected Range swapPriceRange;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SwapValueMatrixParametersImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_PARAMETERS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BuyReference getBaseLoad() {
		return baseLoad;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetBaseLoad(BuyReference newBaseLoad, NotificationChain msgs) {
		BuyReference oldBaseLoad = baseLoad;
		baseLoad = newBaseLoad;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_LOAD, oldBaseLoad, newBaseLoad);
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
	public void setBaseLoad(BuyReference newBaseLoad) {
		if (newBaseLoad != baseLoad) {
			NotificationChain msgs = null;
			if (baseLoad != null)
				msgs = ((InternalEObject)baseLoad).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_LOAD, null, msgs);
			if (newBaseLoad != null)
				msgs = ((InternalEObject)newBaseLoad).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_LOAD, null, msgs);
			msgs = basicSetBaseLoad(newBaseLoad, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_LOAD, newBaseLoad, newBaseLoad));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SellReference getBaseDischarge() {
		return baseDischarge;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetBaseDischarge(SellReference newBaseDischarge, NotificationChain msgs) {
		SellReference oldBaseDischarge = baseDischarge;
		baseDischarge = newBaseDischarge;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_DISCHARGE, oldBaseDischarge, newBaseDischarge);
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
	public void setBaseDischarge(SellReference newBaseDischarge) {
		if (newBaseDischarge != baseDischarge) {
			NotificationChain msgs = null;
			if (baseDischarge != null)
				msgs = ((InternalEObject)baseDischarge).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_DISCHARGE, null, msgs);
			if (newBaseDischarge != null)
				msgs = ((InternalEObject)newBaseDischarge).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_DISCHARGE, null, msgs);
			msgs = basicSetBaseDischarge(newBaseDischarge, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_DISCHARGE, newBaseDischarge, newBaseDischarge));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ExistingVesselCharterOption getBaseVesselCharter() {
		return baseVesselCharter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetBaseVesselCharter(ExistingVesselCharterOption newBaseVesselCharter, NotificationChain msgs) {
		ExistingVesselCharterOption oldBaseVesselCharter = baseVesselCharter;
		baseVesselCharter = newBaseVesselCharter;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_VESSEL_CHARTER, oldBaseVesselCharter, newBaseVesselCharter);
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
	public void setBaseVesselCharter(ExistingVesselCharterOption newBaseVesselCharter) {
		if (newBaseVesselCharter != baseVesselCharter) {
			NotificationChain msgs = null;
			if (baseVesselCharter != null)
				msgs = ((InternalEObject)baseVesselCharter).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_VESSEL_CHARTER, null, msgs);
			if (newBaseVesselCharter != null)
				msgs = ((InternalEObject)newBaseVesselCharter).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_VESSEL_CHARTER, null, msgs);
			msgs = basicSetBaseVesselCharter(newBaseVesselCharter, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_VESSEL_CHARTER, newBaseVesselCharter, newBaseVesselCharter));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Range getBasePriceRange() {
		return basePriceRange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetBasePriceRange(Range newBasePriceRange, NotificationChain msgs) {
		Range oldBasePriceRange = basePriceRange;
		basePriceRange = newBasePriceRange;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_PRICE_RANGE, oldBasePriceRange, newBasePriceRange);
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
	public void setBasePriceRange(Range newBasePriceRange) {
		if (newBasePriceRange != basePriceRange) {
			NotificationChain msgs = null;
			if (basePriceRange != null)
				msgs = ((InternalEObject)basePriceRange).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_PRICE_RANGE, null, msgs);
			if (newBasePriceRange != null)
				msgs = ((InternalEObject)newBasePriceRange).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_PRICE_RANGE, null, msgs);
			msgs = basicSetBasePriceRange(newBasePriceRange, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_PRICE_RANGE, newBasePriceRange, newBasePriceRange));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BuyMarket getSwapLoadMarket() {
		return swapLoadMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSwapLoadMarket(BuyMarket newSwapLoadMarket, NotificationChain msgs) {
		BuyMarket oldSwapLoadMarket = swapLoadMarket;
		swapLoadMarket = newSwapLoadMarket;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_LOAD_MARKET, oldSwapLoadMarket, newSwapLoadMarket);
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
	public void setSwapLoadMarket(BuyMarket newSwapLoadMarket) {
		if (newSwapLoadMarket != swapLoadMarket) {
			NotificationChain msgs = null;
			if (swapLoadMarket != null)
				msgs = ((InternalEObject)swapLoadMarket).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_LOAD_MARKET, null, msgs);
			if (newSwapLoadMarket != null)
				msgs = ((InternalEObject)newSwapLoadMarket).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_LOAD_MARKET, null, msgs);
			msgs = basicSetSwapLoadMarket(newSwapLoadMarket, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_LOAD_MARKET, newSwapLoadMarket, newSwapLoadMarket));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SellMarket getSwapDischargeMarket() {
		return swapDischargeMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSwapDischargeMarket(SellMarket newSwapDischargeMarket, NotificationChain msgs) {
		SellMarket oldSwapDischargeMarket = swapDischargeMarket;
		swapDischargeMarket = newSwapDischargeMarket;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_DISCHARGE_MARKET, oldSwapDischargeMarket, newSwapDischargeMarket);
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
	public void setSwapDischargeMarket(SellMarket newSwapDischargeMarket) {
		if (newSwapDischargeMarket != swapDischargeMarket) {
			NotificationChain msgs = null;
			if (swapDischargeMarket != null)
				msgs = ((InternalEObject)swapDischargeMarket).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_DISCHARGE_MARKET, null, msgs);
			if (newSwapDischargeMarket != null)
				msgs = ((InternalEObject)newSwapDischargeMarket).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_DISCHARGE_MARKET, null, msgs);
			msgs = basicSetSwapDischargeMarket(newSwapDischargeMarket, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_DISCHARGE_MARKET, newSwapDischargeMarket, newSwapDischargeMarket));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getSwapFee() {
		return swapFee;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSwapFee(double newSwapFee) {
		double oldSwapFee = swapFee;
		swapFee = newSwapFee;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_FEE, oldSwapFee, swapFee));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Range getSwapPriceRange() {
		return swapPriceRange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSwapPriceRange(Range newSwapPriceRange, NotificationChain msgs) {
		Range oldSwapPriceRange = swapPriceRange;
		swapPriceRange = newSwapPriceRange;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_PRICE_RANGE, oldSwapPriceRange, newSwapPriceRange);
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
	public void setSwapPriceRange(Range newSwapPriceRange) {
		if (newSwapPriceRange != swapPriceRange) {
			NotificationChain msgs = null;
			if (swapPriceRange != null)
				msgs = ((InternalEObject)swapPriceRange).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_PRICE_RANGE, null, msgs);
			if (newSwapPriceRange != null)
				msgs = ((InternalEObject)newSwapPriceRange).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_PRICE_RANGE, null, msgs);
			msgs = basicSetSwapPriceRange(newSwapPriceRange, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_PRICE_RANGE, newSwapPriceRange, newSwapPriceRange));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_LOAD:
				return basicSetBaseLoad(null, msgs);
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_DISCHARGE:
				return basicSetBaseDischarge(null, msgs);
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_VESSEL_CHARTER:
				return basicSetBaseVesselCharter(null, msgs);
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_PRICE_RANGE:
				return basicSetBasePriceRange(null, msgs);
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_LOAD_MARKET:
				return basicSetSwapLoadMarket(null, msgs);
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_DISCHARGE_MARKET:
				return basicSetSwapDischargeMarket(null, msgs);
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_PRICE_RANGE:
				return basicSetSwapPriceRange(null, msgs);
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
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_LOAD:
				return getBaseLoad();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_DISCHARGE:
				return getBaseDischarge();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_VESSEL_CHARTER:
				return getBaseVesselCharter();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_PRICE_RANGE:
				return getBasePriceRange();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_LOAD_MARKET:
				return getSwapLoadMarket();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_DISCHARGE_MARKET:
				return getSwapDischargeMarket();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_FEE:
				return getSwapFee();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_PRICE_RANGE:
				return getSwapPriceRange();
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
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_LOAD:
				setBaseLoad((BuyReference)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_DISCHARGE:
				setBaseDischarge((SellReference)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_VESSEL_CHARTER:
				setBaseVesselCharter((ExistingVesselCharterOption)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_PRICE_RANGE:
				setBasePriceRange((Range)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_LOAD_MARKET:
				setSwapLoadMarket((BuyMarket)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_DISCHARGE_MARKET:
				setSwapDischargeMarket((SellMarket)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_FEE:
				setSwapFee((Double)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_PRICE_RANGE:
				setSwapPriceRange((Range)newValue);
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
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_LOAD:
				setBaseLoad((BuyReference)null);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_DISCHARGE:
				setBaseDischarge((SellReference)null);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_VESSEL_CHARTER:
				setBaseVesselCharter((ExistingVesselCharterOption)null);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_PRICE_RANGE:
				setBasePriceRange((Range)null);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_LOAD_MARKET:
				setSwapLoadMarket((BuyMarket)null);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_DISCHARGE_MARKET:
				setSwapDischargeMarket((SellMarket)null);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_FEE:
				setSwapFee(SWAP_FEE_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_PRICE_RANGE:
				setSwapPriceRange((Range)null);
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
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_LOAD:
				return baseLoad != null;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_DISCHARGE:
				return baseDischarge != null;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_VESSEL_CHARTER:
				return baseVesselCharter != null;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__BASE_PRICE_RANGE:
				return basePriceRange != null;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_LOAD_MARKET:
				return swapLoadMarket != null;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_DISCHARGE_MARKET:
				return swapDischargeMarket != null;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_FEE:
				return swapFee != SWAP_FEE_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_PARAMETERS__SWAP_PRICE_RANGE:
				return swapPriceRange != null;
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
		result.append(" (swapFee: ");
		result.append(swapFee);
		result.append(')');
		return result.toString();
	}

} //SwapValueMatrixParametersImpl
