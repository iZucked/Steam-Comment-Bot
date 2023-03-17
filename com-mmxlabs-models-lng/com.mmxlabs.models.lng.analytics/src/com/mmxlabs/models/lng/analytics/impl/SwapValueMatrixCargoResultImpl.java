/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixCargoResult;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Swap Value Matrix Cargo Result</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixCargoResultImpl#getLoadPrice <em>Load Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixCargoResultImpl#getDischargePrice <em>Discharge Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixCargoResultImpl#getPurchaseCost <em>Purchase Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixCargoResultImpl#getSalesRevenue <em>Sales Revenue</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixCargoResultImpl#getAdditionalPnl <em>Additional Pnl</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SwapValueMatrixCargoResultImpl#getTotalPnl <em>Total Pnl</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class SwapValueMatrixCargoResultImpl extends EObjectImpl implements SwapValueMatrixCargoResult {
	/**
	 * The default value of the '{@link #getLoadPrice() <em>Load Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadPrice()
	 * @generated
	 * @ordered
	 */
	protected static final double LOAD_PRICE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getLoadPrice() <em>Load Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadPrice()
	 * @generated
	 * @ordered
	 */
	protected double loadPrice = LOAD_PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getDischargePrice() <em>Discharge Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargePrice()
	 * @generated
	 * @ordered
	 */
	protected static final double DISCHARGE_PRICE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getDischargePrice() <em>Discharge Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargePrice()
	 * @generated
	 * @ordered
	 */
	protected double dischargePrice = DISCHARGE_PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getPurchaseCost() <em>Purchase Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPurchaseCost()
	 * @generated
	 * @ordered
	 */
	protected static final long PURCHASE_COST_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getPurchaseCost() <em>Purchase Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPurchaseCost()
	 * @generated
	 * @ordered
	 */
	protected long purchaseCost = PURCHASE_COST_EDEFAULT;

	/**
	 * The default value of the '{@link #getSalesRevenue() <em>Sales Revenue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSalesRevenue()
	 * @generated
	 * @ordered
	 */
	protected static final long SALES_REVENUE_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getSalesRevenue() <em>Sales Revenue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSalesRevenue()
	 * @generated
	 * @ordered
	 */
	protected long salesRevenue = SALES_REVENUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getAdditionalPnl() <em>Additional Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAdditionalPnl()
	 * @generated
	 * @ordered
	 */
	protected static final long ADDITIONAL_PNL_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getAdditionalPnl() <em>Additional Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAdditionalPnl()
	 * @generated
	 * @ordered
	 */
	protected long additionalPnl = ADDITIONAL_PNL_EDEFAULT;

	/**
	 * The default value of the '{@link #getTotalPnl() <em>Total Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTotalPnl()
	 * @generated
	 * @ordered
	 */
	protected static final long TOTAL_PNL_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getTotalPnl() <em>Total Pnl</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTotalPnl()
	 * @generated
	 * @ordered
	 */
	protected long totalPnl = TOTAL_PNL_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SwapValueMatrixCargoResultImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_CARGO_RESULT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getLoadPrice() {
		return loadPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLoadPrice(double newLoadPrice) {
		double oldLoadPrice = loadPrice;
		loadPrice = newLoadPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__LOAD_PRICE, oldLoadPrice, loadPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getDischargePrice() {
		return dischargePrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDischargePrice(double newDischargePrice) {
		double oldDischargePrice = dischargePrice;
		dischargePrice = newDischargePrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__DISCHARGE_PRICE, oldDischargePrice, dischargePrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getPurchaseCost() {
		return purchaseCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPurchaseCost(long newPurchaseCost) {
		long oldPurchaseCost = purchaseCost;
		purchaseCost = newPurchaseCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__PURCHASE_COST, oldPurchaseCost, purchaseCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getSalesRevenue() {
		return salesRevenue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSalesRevenue(long newSalesRevenue) {
		long oldSalesRevenue = salesRevenue;
		salesRevenue = newSalesRevenue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__SALES_REVENUE, oldSalesRevenue, salesRevenue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getAdditionalPnl() {
		return additionalPnl;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAdditionalPnl(long newAdditionalPnl) {
		long oldAdditionalPnl = additionalPnl;
		additionalPnl = newAdditionalPnl;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__ADDITIONAL_PNL, oldAdditionalPnl, additionalPnl));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getTotalPnl() {
		return totalPnl;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTotalPnl(long newTotalPnl) {
		long oldTotalPnl = totalPnl;
		totalPnl = newTotalPnl;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__TOTAL_PNL, oldTotalPnl, totalPnl));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__LOAD_PRICE:
				return getLoadPrice();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__DISCHARGE_PRICE:
				return getDischargePrice();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__PURCHASE_COST:
				return getPurchaseCost();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__SALES_REVENUE:
				return getSalesRevenue();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__ADDITIONAL_PNL:
				return getAdditionalPnl();
			case AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__TOTAL_PNL:
				return getTotalPnl();
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
			case AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__LOAD_PRICE:
				setLoadPrice((Double)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__DISCHARGE_PRICE:
				setDischargePrice((Double)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__PURCHASE_COST:
				setPurchaseCost((Long)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__SALES_REVENUE:
				setSalesRevenue((Long)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__ADDITIONAL_PNL:
				setAdditionalPnl((Long)newValue);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__TOTAL_PNL:
				setTotalPnl((Long)newValue);
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
			case AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__LOAD_PRICE:
				setLoadPrice(LOAD_PRICE_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__DISCHARGE_PRICE:
				setDischargePrice(DISCHARGE_PRICE_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__PURCHASE_COST:
				setPurchaseCost(PURCHASE_COST_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__SALES_REVENUE:
				setSalesRevenue(SALES_REVENUE_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__ADDITIONAL_PNL:
				setAdditionalPnl(ADDITIONAL_PNL_EDEFAULT);
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__TOTAL_PNL:
				setTotalPnl(TOTAL_PNL_EDEFAULT);
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
			case AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__LOAD_PRICE:
				return loadPrice != LOAD_PRICE_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__DISCHARGE_PRICE:
				return dischargePrice != DISCHARGE_PRICE_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__PURCHASE_COST:
				return purchaseCost != PURCHASE_COST_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__SALES_REVENUE:
				return salesRevenue != SALES_REVENUE_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__ADDITIONAL_PNL:
				return additionalPnl != ADDITIONAL_PNL_EDEFAULT;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__TOTAL_PNL:
				return totalPnl != TOTAL_PNL_EDEFAULT;
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
		result.append(" (loadPrice: ");
		result.append(loadPrice);
		result.append(", dischargePrice: ");
		result.append(dischargePrice);
		result.append(", purchaseCost: ");
		result.append(purchaseCost);
		result.append(", salesRevenue: ");
		result.append(salesRevenue);
		result.append(", additionalPnl: ");
		result.append(additionalPnl);
		result.append(", totalPnl: ");
		result.append(totalPnl);
		result.append(')');
		return result.toString();
	}

} //SwapValueMatrixCargoResultImpl
