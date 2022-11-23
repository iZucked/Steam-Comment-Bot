/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixModel;

import com.mmxlabs.models.lng.analytics.SwapValueMatrixResultSet;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Swap Value Matrix Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixModelImpl#getBaseLoad <em>Base Load</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixModelImpl#getBaseDischarge <em>Base Discharge</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixModelImpl#getBaseVesselCharter <em>Base Vessel Charter</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixModelImpl#getSwapLoadMarket <em>Swap Load Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixModelImpl#getSwapDischargeMarket <em>Swap Discharge Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixModelImpl#getBaseDischargeMinPrice <em>Base Discharge Min Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixModelImpl#getBaseDischargeMaxPrice <em>Base Discharge Max Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixModelImpl#getBaseDischargeStepSize <em>Base Discharge Step Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixModelImpl#getMarketMinPrice <em>Market Min Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixModelImpl#getMarketMaxPrice <em>Market Max Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixModelImpl#getMarketStepSize <em>Market Step Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixModelImpl#getSwapFee <em>Swap Fee</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixModelImpl#getSwapValueMatrixResult <em>Swap Value Matrix Result</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SwapValueMatrixModelImpl extends AbstractAnalysisModelImpl implements SwapValueMatrixModel {
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
	 * The default value of the '{@link #getBaseDischargeMinPrice() <em>Base Discharge Min Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseDischargeMinPrice()
	 * @generated
	 * @ordered
	 */
	protected static final int BASE_DISCHARGE_MIN_PRICE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getBaseDischargeMinPrice() <em>Base Discharge Min Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseDischargeMinPrice()
	 * @generated
	 * @ordered
	 */
	protected int baseDischargeMinPrice = BASE_DISCHARGE_MIN_PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getBaseDischargeMaxPrice() <em>Base Discharge Max Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseDischargeMaxPrice()
	 * @generated
	 * @ordered
	 */
	protected static final int BASE_DISCHARGE_MAX_PRICE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getBaseDischargeMaxPrice() <em>Base Discharge Max Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseDischargeMaxPrice()
	 * @generated
	 * @ordered
	 */
	protected int baseDischargeMaxPrice = BASE_DISCHARGE_MAX_PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getBaseDischargeStepSize() <em>Base Discharge Step Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseDischargeStepSize()
	 * @generated
	 * @ordered
	 */
	protected static final int BASE_DISCHARGE_STEP_SIZE_EDEFAULT = 1;

	/**
	 * The cached value of the '{@link #getBaseDischargeStepSize() <em>Base Discharge Step Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseDischargeStepSize()
	 * @generated
	 * @ordered
	 */
	protected int baseDischargeStepSize = BASE_DISCHARGE_STEP_SIZE_EDEFAULT;

	/**
	 * The default value of the '{@link #getMarketMinPrice() <em>Market Min Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarketMinPrice()
	 * @generated
	 * @ordered
	 */
	protected static final int MARKET_MIN_PRICE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMarketMinPrice() <em>Market Min Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarketMinPrice()
	 * @generated
	 * @ordered
	 */
	protected int marketMinPrice = MARKET_MIN_PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getMarketMaxPrice() <em>Market Max Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarketMaxPrice()
	 * @generated
	 * @ordered
	 */
	protected static final int MARKET_MAX_PRICE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMarketMaxPrice() <em>Market Max Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarketMaxPrice()
	 * @generated
	 * @ordered
	 */
	protected int marketMaxPrice = MARKET_MAX_PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getMarketStepSize() <em>Market Step Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarketStepSize()
	 * @generated
	 * @ordered
	 */
	protected static final int MARKET_STEP_SIZE_EDEFAULT = 1;

	/**
	 * The cached value of the '{@link #getMarketStepSize() <em>Market Step Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarketStepSize()
	 * @generated
	 * @ordered
	 */
	protected int marketStepSize = MARKET_STEP_SIZE_EDEFAULT;

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
	 * The cached value of the '{@link #getSwapValueMatrixResult() <em>Swap Value Matrix Result</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapValueMatrixResult()
	 * @generated
	 * @ordered
	 */
	protected SwapValueMatrixResultSet swapValueMatrixResult;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SwapValueMatrixModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_MODEL;
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_LOAD, oldBaseLoad, newBaseLoad);
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
				msgs = ((InternalEObject)baseLoad).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_LOAD, null, msgs);
			if (newBaseLoad != null)
				msgs = ((InternalEObject)newBaseLoad).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_LOAD, null, msgs);
			msgs = basicSetBaseLoad(newBaseLoad, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_LOAD, newBaseLoad, newBaseLoad));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE, oldBaseDischarge, newBaseDischarge);
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
				msgs = ((InternalEObject)baseDischarge).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE, null, msgs);
			if (newBaseDischarge != null)
				msgs = ((InternalEObject)newBaseDischarge).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE, null, msgs);
			msgs = basicSetBaseDischarge(newBaseDischarge, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE, newBaseDischarge, newBaseDischarge));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_VESSEL_CHARTER, oldBaseVesselCharter, newBaseVesselCharter);
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
				msgs = ((InternalEObject)baseVesselCharter).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_VESSEL_CHARTER, null, msgs);
			if (newBaseVesselCharter != null)
				msgs = ((InternalEObject)newBaseVesselCharter).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_VESSEL_CHARTER, null, msgs);
			msgs = basicSetBaseVesselCharter(newBaseVesselCharter, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_VESSEL_CHARTER, newBaseVesselCharter, newBaseVesselCharter));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_LOAD_MARKET, oldSwapLoadMarket, newSwapLoadMarket);
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
				msgs = ((InternalEObject)swapLoadMarket).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_LOAD_MARKET, null, msgs);
			if (newSwapLoadMarket != null)
				msgs = ((InternalEObject)newSwapLoadMarket).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_LOAD_MARKET, null, msgs);
			msgs = basicSetSwapLoadMarket(newSwapLoadMarket, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_LOAD_MARKET, newSwapLoadMarket, newSwapLoadMarket));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_DISCHARGE_MARKET, oldSwapDischargeMarket, newSwapDischargeMarket);
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
				msgs = ((InternalEObject)swapDischargeMarket).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_DISCHARGE_MARKET, null, msgs);
			if (newSwapDischargeMarket != null)
				msgs = ((InternalEObject)newSwapDischargeMarket).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_DISCHARGE_MARKET, null, msgs);
			msgs = basicSetSwapDischargeMarket(newSwapDischargeMarket, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_DISCHARGE_MARKET, newSwapDischargeMarket, newSwapDischargeMarket));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getBaseDischargeMinPrice() {
		return baseDischargeMinPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBaseDischargeMinPrice(int newBaseDischargeMinPrice) {
		int oldBaseDischargeMinPrice = baseDischargeMinPrice;
		baseDischargeMinPrice = newBaseDischargeMinPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE_MIN_PRICE, oldBaseDischargeMinPrice, baseDischargeMinPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getBaseDischargeMaxPrice() {
		return baseDischargeMaxPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBaseDischargeMaxPrice(int newBaseDischargeMaxPrice) {
		int oldBaseDischargeMaxPrice = baseDischargeMaxPrice;
		baseDischargeMaxPrice = newBaseDischargeMaxPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE_MAX_PRICE, oldBaseDischargeMaxPrice, baseDischargeMaxPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getBaseDischargeStepSize() {
		return baseDischargeStepSize;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBaseDischargeStepSize(int newBaseDischargeStepSize) {
		int oldBaseDischargeStepSize = baseDischargeStepSize;
		baseDischargeStepSize = newBaseDischargeStepSize;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE_STEP_SIZE, oldBaseDischargeStepSize, baseDischargeStepSize));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getMarketMinPrice() {
		return marketMinPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMarketMinPrice(int newMarketMinPrice) {
		int oldMarketMinPrice = marketMinPrice;
		marketMinPrice = newMarketMinPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__MARKET_MIN_PRICE, oldMarketMinPrice, marketMinPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getMarketMaxPrice() {
		return marketMaxPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMarketMaxPrice(int newMarketMaxPrice) {
		int oldMarketMaxPrice = marketMaxPrice;
		marketMaxPrice = newMarketMaxPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__MARKET_MAX_PRICE, oldMarketMaxPrice, marketMaxPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getMarketStepSize() {
		return marketStepSize;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMarketStepSize(int newMarketStepSize) {
		int oldMarketStepSize = marketStepSize;
		marketStepSize = newMarketStepSize;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__MARKET_STEP_SIZE, oldMarketStepSize, marketStepSize));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_FEE, oldSwapFee, swapFee));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SwapValueMatrixResultSet getSwapValueMatrixResult() {
		return swapValueMatrixResult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSwapValueMatrixResult(SwapValueMatrixResultSet newSwapValueMatrixResult, NotificationChain msgs) {
		SwapValueMatrixResultSet oldSwapValueMatrixResult = swapValueMatrixResult;
		swapValueMatrixResult = newSwapValueMatrixResult;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_VALUE_MATRIX_RESULT, oldSwapValueMatrixResult, newSwapValueMatrixResult);
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
	public void setSwapValueMatrixResult(SwapValueMatrixResultSet newSwapValueMatrixResult) {
		if (newSwapValueMatrixResult != swapValueMatrixResult) {
			NotificationChain msgs = null;
			if (swapValueMatrixResult != null)
				msgs = ((InternalEObject)swapValueMatrixResult).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_VALUE_MATRIX_RESULT, null, msgs);
			if (newSwapValueMatrixResult != null)
				msgs = ((InternalEObject)newSwapValueMatrixResult).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_VALUE_MATRIX_RESULT, null, msgs);
			msgs = basicSetSwapValueMatrixResult(newSwapValueMatrixResult, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_VALUE_MATRIX_RESULT, newSwapValueMatrixResult, newSwapValueMatrixResult));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_LOAD:
				return basicSetBaseLoad(null, msgs);
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE:
				return basicSetBaseDischarge(null, msgs);
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_VESSEL_CHARTER:
				return basicSetBaseVesselCharter(null, msgs);
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_LOAD_MARKET:
				return basicSetSwapLoadMarket(null, msgs);
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_DISCHARGE_MARKET:
				return basicSetSwapDischargeMarket(null, msgs);
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_VALUE_MATRIX_RESULT:
				return basicSetSwapValueMatrixResult(null, msgs);
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
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_LOAD:
				return getBaseLoad();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE:
				return getBaseDischarge();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_VESSEL_CHARTER:
				return getBaseVesselCharter();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_LOAD_MARKET:
				return getSwapLoadMarket();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_DISCHARGE_MARKET:
				return getSwapDischargeMarket();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE_MIN_PRICE:
				return getBaseDischargeMinPrice();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE_MAX_PRICE:
				return getBaseDischargeMaxPrice();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE_STEP_SIZE:
				return getBaseDischargeStepSize();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__MARKET_MIN_PRICE:
				return getMarketMinPrice();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__MARKET_MAX_PRICE:
				return getMarketMaxPrice();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__MARKET_STEP_SIZE:
				return getMarketStepSize();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_FEE:
				return getSwapFee();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_VALUE_MATRIX_RESULT:
				return getSwapValueMatrixResult();
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
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_LOAD:
				setBaseLoad((BuyReference)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE:
				setBaseDischarge((SellReference)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_VESSEL_CHARTER:
				setBaseVesselCharter((ExistingVesselCharterOption)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_LOAD_MARKET:
				setSwapLoadMarket((BuyMarket)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_DISCHARGE_MARKET:
				setSwapDischargeMarket((SellMarket)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE_MIN_PRICE:
				setBaseDischargeMinPrice((Integer)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE_MAX_PRICE:
				setBaseDischargeMaxPrice((Integer)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE_STEP_SIZE:
				setBaseDischargeStepSize((Integer)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__MARKET_MIN_PRICE:
				setMarketMinPrice((Integer)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__MARKET_MAX_PRICE:
				setMarketMaxPrice((Integer)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__MARKET_STEP_SIZE:
				setMarketStepSize((Integer)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_FEE:
				setSwapFee((Double)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_VALUE_MATRIX_RESULT:
				setSwapValueMatrixResult((SwapValueMatrixResultSet)newValue);
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
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_LOAD:
				setBaseLoad((BuyReference)null);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE:
				setBaseDischarge((SellReference)null);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_VESSEL_CHARTER:
				setBaseVesselCharter((ExistingVesselCharterOption)null);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_LOAD_MARKET:
				setSwapLoadMarket((BuyMarket)null);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_DISCHARGE_MARKET:
				setSwapDischargeMarket((SellMarket)null);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE_MIN_PRICE:
				setBaseDischargeMinPrice(BASE_DISCHARGE_MIN_PRICE_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE_MAX_PRICE:
				setBaseDischargeMaxPrice(BASE_DISCHARGE_MAX_PRICE_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE_STEP_SIZE:
				setBaseDischargeStepSize(BASE_DISCHARGE_STEP_SIZE_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__MARKET_MIN_PRICE:
				setMarketMinPrice(MARKET_MIN_PRICE_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__MARKET_MAX_PRICE:
				setMarketMaxPrice(MARKET_MAX_PRICE_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__MARKET_STEP_SIZE:
				setMarketStepSize(MARKET_STEP_SIZE_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_FEE:
				setSwapFee(SWAP_FEE_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_VALUE_MATRIX_RESULT:
				setSwapValueMatrixResult((SwapValueMatrixResultSet)null);
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
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_LOAD:
				return baseLoad != null;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE:
				return baseDischarge != null;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_VESSEL_CHARTER:
				return baseVesselCharter != null;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_LOAD_MARKET:
				return swapLoadMarket != null;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_DISCHARGE_MARKET:
				return swapDischargeMarket != null;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE_MIN_PRICE:
				return baseDischargeMinPrice != BASE_DISCHARGE_MIN_PRICE_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE_MAX_PRICE:
				return baseDischargeMaxPrice != BASE_DISCHARGE_MAX_PRICE_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE_STEP_SIZE:
				return baseDischargeStepSize != BASE_DISCHARGE_STEP_SIZE_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__MARKET_MIN_PRICE:
				return marketMinPrice != MARKET_MIN_PRICE_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__MARKET_MAX_PRICE:
				return marketMaxPrice != MARKET_MAX_PRICE_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__MARKET_STEP_SIZE:
				return marketStepSize != MARKET_STEP_SIZE_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_FEE:
				return swapFee != SWAP_FEE_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_VALUE_MATRIX_RESULT:
				return swapValueMatrixResult != null;
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
		result.append(" (baseDischargeMinPrice: ");
		result.append(baseDischargeMinPrice);
		result.append(", baseDischargeMaxPrice: ");
		result.append(baseDischargeMaxPrice);
		result.append(", baseDischargeStepSize: ");
		result.append(baseDischargeStepSize);
		result.append(", marketMinPrice: ");
		result.append(marketMinPrice);
		result.append(", marketMaxPrice: ");
		result.append(marketMaxPrice);
		result.append(", marketStepSize: ");
		result.append(marketStepSize);
		result.append(", swapFee: ");
		result.append(swapFee);
		result.append(')');
		return result.toString();
	}

} //SwapValueMatrixModelImpl
