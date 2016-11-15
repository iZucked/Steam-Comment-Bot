/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalysisResultDetail;
import com.mmxlabs.models.lng.analytics.AnalysisResultRow;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.ResultContainer;
import com.mmxlabs.models.lng.analytics.SellOption;

import com.mmxlabs.models.lng.analytics.ShippingOption;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Analysis Result Row</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AnalysisResultRowImpl#getBuyOption <em>Buy Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AnalysisResultRowImpl#getSellOption <em>Sell Option</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AnalysisResultRowImpl#getResultDetail <em>Result Detail</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AnalysisResultRowImpl#getShipping <em>Shipping</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AnalysisResultRowImpl#getResultDetails <em>Result Details</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AnalysisResultRowImpl extends EObjectImpl implements AnalysisResultRow {
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
	 * The cached value of the '{@link #getResultDetail() <em>Result Detail</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResultDetail()
	 * @generated
	 * @ordered
	 */
	protected AnalysisResultDetail resultDetail;

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
	 * The cached value of the '{@link #getResultDetails() <em>Result Details</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResultDetails()
	 * @generated
	 * @ordered
	 */
	protected ResultContainer resultDetails;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AnalysisResultRowImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BuyOption getBuyOption() {
		if (buyOption != null && buyOption.eIsProxy()) {
			InternalEObject oldBuyOption = (InternalEObject)buyOption;
			buyOption = (BuyOption)eResolveProxy(oldBuyOption);
			if (buyOption != oldBuyOption) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.ANALYSIS_RESULT_ROW__BUY_OPTION, oldBuyOption, buyOption));
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
	public void setBuyOption(BuyOption newBuyOption) {
		BuyOption oldBuyOption = buyOption;
		buyOption = newBuyOption;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.ANALYSIS_RESULT_ROW__BUY_OPTION, oldBuyOption, buyOption));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SellOption getSellOption() {
		if (sellOption != null && sellOption.eIsProxy()) {
			InternalEObject oldSellOption = (InternalEObject)sellOption;
			sellOption = (SellOption)eResolveProxy(oldSellOption);
			if (sellOption != oldSellOption) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.ANALYSIS_RESULT_ROW__SELL_OPTION, oldSellOption, sellOption));
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
	public void setSellOption(SellOption newSellOption) {
		SellOption oldSellOption = sellOption;
		sellOption = newSellOption;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.ANALYSIS_RESULT_ROW__SELL_OPTION, oldSellOption, sellOption));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnalysisResultDetail getResultDetail() {
		return resultDetail;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetResultDetail(AnalysisResultDetail newResultDetail, NotificationChain msgs) {
		AnalysisResultDetail oldResultDetail = resultDetail;
		resultDetail = newResultDetail;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.ANALYSIS_RESULT_ROW__RESULT_DETAIL, oldResultDetail, newResultDetail);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setResultDetail(AnalysisResultDetail newResultDetail) {
		if (newResultDetail != resultDetail) {
			NotificationChain msgs = null;
			if (resultDetail != null)
				msgs = ((InternalEObject)resultDetail).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.ANALYSIS_RESULT_ROW__RESULT_DETAIL, null, msgs);
			if (newResultDetail != null)
				msgs = ((InternalEObject)newResultDetail).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.ANALYSIS_RESULT_ROW__RESULT_DETAIL, null, msgs);
			msgs = basicSetResultDetail(newResultDetail, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.ANALYSIS_RESULT_ROW__RESULT_DETAIL, newResultDetail, newResultDetail));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ShippingOption getShipping() {
		if (shipping != null && shipping.eIsProxy()) {
			InternalEObject oldShipping = (InternalEObject)shipping;
			shipping = (ShippingOption)eResolveProxy(oldShipping);
			if (shipping != oldShipping) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.ANALYSIS_RESULT_ROW__SHIPPING, oldShipping, shipping));
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
	public void setShipping(ShippingOption newShipping) {
		ShippingOption oldShipping = shipping;
		shipping = newShipping;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.ANALYSIS_RESULT_ROW__SHIPPING, oldShipping, shipping));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResultContainer getResultDetails() {
		return resultDetails;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetResultDetails(ResultContainer newResultDetails, NotificationChain msgs) {
		ResultContainer oldResultDetails = resultDetails;
		resultDetails = newResultDetails;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.ANALYSIS_RESULT_ROW__RESULT_DETAILS, oldResultDetails, newResultDetails);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setResultDetails(ResultContainer newResultDetails) {
		if (newResultDetails != resultDetails) {
			NotificationChain msgs = null;
			if (resultDetails != null)
				msgs = ((InternalEObject)resultDetails).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.ANALYSIS_RESULT_ROW__RESULT_DETAILS, null, msgs);
			if (newResultDetails != null)
				msgs = ((InternalEObject)newResultDetails).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.ANALYSIS_RESULT_ROW__RESULT_DETAILS, null, msgs);
			msgs = basicSetResultDetails(newResultDetails, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.ANALYSIS_RESULT_ROW__RESULT_DETAILS, newResultDetails, newResultDetails));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.ANALYSIS_RESULT_ROW__RESULT_DETAIL:
				return basicSetResultDetail(null, msgs);
			case AnalyticsPackage.ANALYSIS_RESULT_ROW__RESULT_DETAILS:
				return basicSetResultDetails(null, msgs);
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
			case AnalyticsPackage.ANALYSIS_RESULT_ROW__BUY_OPTION:
				if (resolve) return getBuyOption();
				return basicGetBuyOption();
			case AnalyticsPackage.ANALYSIS_RESULT_ROW__SELL_OPTION:
				if (resolve) return getSellOption();
				return basicGetSellOption();
			case AnalyticsPackage.ANALYSIS_RESULT_ROW__RESULT_DETAIL:
				return getResultDetail();
			case AnalyticsPackage.ANALYSIS_RESULT_ROW__SHIPPING:
				if (resolve) return getShipping();
				return basicGetShipping();
			case AnalyticsPackage.ANALYSIS_RESULT_ROW__RESULT_DETAILS:
				return getResultDetails();
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
			case AnalyticsPackage.ANALYSIS_RESULT_ROW__BUY_OPTION:
				setBuyOption((BuyOption)newValue);
				return;
			case AnalyticsPackage.ANALYSIS_RESULT_ROW__SELL_OPTION:
				setSellOption((SellOption)newValue);
				return;
			case AnalyticsPackage.ANALYSIS_RESULT_ROW__RESULT_DETAIL:
				setResultDetail((AnalysisResultDetail)newValue);
				return;
			case AnalyticsPackage.ANALYSIS_RESULT_ROW__SHIPPING:
				setShipping((ShippingOption)newValue);
				return;
			case AnalyticsPackage.ANALYSIS_RESULT_ROW__RESULT_DETAILS:
				setResultDetails((ResultContainer)newValue);
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
			case AnalyticsPackage.ANALYSIS_RESULT_ROW__BUY_OPTION:
				setBuyOption((BuyOption)null);
				return;
			case AnalyticsPackage.ANALYSIS_RESULT_ROW__SELL_OPTION:
				setSellOption((SellOption)null);
				return;
			case AnalyticsPackage.ANALYSIS_RESULT_ROW__RESULT_DETAIL:
				setResultDetail((AnalysisResultDetail)null);
				return;
			case AnalyticsPackage.ANALYSIS_RESULT_ROW__SHIPPING:
				setShipping((ShippingOption)null);
				return;
			case AnalyticsPackage.ANALYSIS_RESULT_ROW__RESULT_DETAILS:
				setResultDetails((ResultContainer)null);
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
			case AnalyticsPackage.ANALYSIS_RESULT_ROW__BUY_OPTION:
				return buyOption != null;
			case AnalyticsPackage.ANALYSIS_RESULT_ROW__SELL_OPTION:
				return sellOption != null;
			case AnalyticsPackage.ANALYSIS_RESULT_ROW__RESULT_DETAIL:
				return resultDetail != null;
			case AnalyticsPackage.ANALYSIS_RESULT_ROW__SHIPPING:
				return shipping != null;
			case AnalyticsPackage.ANALYSIS_RESULT_ROW__RESULT_DETAILS:
				return resultDetails != null;
		}
		return super.eIsSet(featureID);
	}

} //AnalysisResultRowImpl
