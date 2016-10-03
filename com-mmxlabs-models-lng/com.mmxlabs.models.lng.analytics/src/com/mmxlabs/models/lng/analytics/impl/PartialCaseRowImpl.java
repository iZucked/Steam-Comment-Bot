/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
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
	 * The cached value of the '{@link #getShipping() <em>Shipping</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShipping()
	 * @generated
	 * @ordered
	 */
	protected ShippingOption shipping;

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
	public ShippingOption getShipping() {
		return shipping;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetShipping(ShippingOption newShipping, NotificationChain msgs) {
		ShippingOption oldShipping = shipping;
		shipping = newShipping;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.PARTIAL_CASE_ROW__SHIPPING, oldShipping, newShipping);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setShipping(ShippingOption newShipping) {
		if (newShipping != shipping) {
			NotificationChain msgs = null;
			if (shipping != null)
				msgs = ((InternalEObject)shipping).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.PARTIAL_CASE_ROW__SHIPPING, null, msgs);
			if (newShipping != null)
				msgs = ((InternalEObject)newShipping).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.PARTIAL_CASE_ROW__SHIPPING, null, msgs);
			msgs = basicSetShipping(newShipping, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.PARTIAL_CASE_ROW__SHIPPING, newShipping, newShipping));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.PARTIAL_CASE_ROW__SHIPPING:
				return basicSetShipping(null, msgs);
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
				setShipping((ShippingOption)newValue);
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
				setShipping((ShippingOption)null);
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
				return shipping != null;
		}
		return super.eIsSet(featureID);
	}

} //PartialCaseRowImpl
