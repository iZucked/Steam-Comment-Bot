/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;

import java.util.Collection;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Partial Case Row</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.PartialCaseRowImpl#getBuyOptions <em>Buy Options</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.PartialCaseRowImpl#getSellOptions <em>Sell Options</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.PartialCaseRowImpl#getShipping <em>Shipping</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PartialCaseRowImpl extends EObjectImpl implements PartialCaseRow {
	/**
	 * The cached value of the '{@link #getBuyOptions() <em>Buy Options</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBuyOptions()
	 * @generated
	 * @ordered
	 */
	protected EList<BuyOption> buyOptions;

	/**
	 * The cached value of the '{@link #getSellOptions() <em>Sell Options</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSellOptions()
	 * @generated
	 * @ordered
	 */
	protected EList<SellOption> sellOptions;

	/**
	 * The cached value of the '{@link #getShipping() <em>Shipping</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShipping()
	 * @generated
	 * @ordered
	 */
	protected EList<ShippingOption> shipping;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PartialCaseRowImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.PARTIAL_CASE_ROW;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<BuyOption> getBuyOptions() {
		if (buyOptions == null) {
			buyOptions = new EObjectResolvingEList<BuyOption>(BuyOption.class, this, AnalyticsPackage.PARTIAL_CASE_ROW__BUY_OPTIONS);
		}
		return buyOptions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<SellOption> getSellOptions() {
		if (sellOptions == null) {
			sellOptions = new EObjectResolvingEList<SellOption>(SellOption.class, this, AnalyticsPackage.PARTIAL_CASE_ROW__SELL_OPTIONS);
		}
		return sellOptions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ShippingOption> getShipping() {
		if (shipping == null) {
			shipping = new EObjectResolvingEList<ShippingOption>(ShippingOption.class, this, AnalyticsPackage.PARTIAL_CASE_ROW__SHIPPING);
		}
		return shipping;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AnalyticsPackage.PARTIAL_CASE_ROW__BUY_OPTIONS:
				return getBuyOptions();
			case AnalyticsPackage.PARTIAL_CASE_ROW__SELL_OPTIONS:
				return getSellOptions();
			case AnalyticsPackage.PARTIAL_CASE_ROW__SHIPPING:
				return getShipping();
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
			case AnalyticsPackage.PARTIAL_CASE_ROW__BUY_OPTIONS:
				getBuyOptions().clear();
				getBuyOptions().addAll((Collection<? extends BuyOption>)newValue);
				return;
			case AnalyticsPackage.PARTIAL_CASE_ROW__SELL_OPTIONS:
				getSellOptions().clear();
				getSellOptions().addAll((Collection<? extends SellOption>)newValue);
				return;
			case AnalyticsPackage.PARTIAL_CASE_ROW__SHIPPING:
				getShipping().clear();
				getShipping().addAll((Collection<? extends ShippingOption>)newValue);
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
			case AnalyticsPackage.PARTIAL_CASE_ROW__BUY_OPTIONS:
				getBuyOptions().clear();
				return;
			case AnalyticsPackage.PARTIAL_CASE_ROW__SELL_OPTIONS:
				getSellOptions().clear();
				return;
			case AnalyticsPackage.PARTIAL_CASE_ROW__SHIPPING:
				getShipping().clear();
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
			case AnalyticsPackage.PARTIAL_CASE_ROW__BUY_OPTIONS:
				return buyOptions != null && !buyOptions.isEmpty();
			case AnalyticsPackage.PARTIAL_CASE_ROW__SELL_OPTIONS:
				return sellOptions != null && !sellOptions.isEmpty();
			case AnalyticsPackage.PARTIAL_CASE_ROW__SHIPPING:
				return shipping != null && !shipping.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //PartialCaseRowImpl
