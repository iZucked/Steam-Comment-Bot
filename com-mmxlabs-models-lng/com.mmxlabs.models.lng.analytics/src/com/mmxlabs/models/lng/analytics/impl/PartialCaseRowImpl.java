/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.CommodityCurveOption;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.PartialCaseRowGroup;
import com.mmxlabs.models.lng.analytics.PartialCaseRowOptions;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;

import com.mmxlabs.models.lng.analytics.VesselEventOption;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;
import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
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
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.PartialCaseRowImpl#getVesselEventOptions <em>Vessel Event Options</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.PartialCaseRowImpl#getShipping <em>Shipping</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.PartialCaseRowImpl#getOptions <em>Options</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.PartialCaseRowImpl#getCommodityCurveOptions <em>Commodity Curve Options</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.PartialCaseRowImpl#getGroup <em>Group</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PartialCaseRowImpl extends UUIDObjectImpl implements PartialCaseRow {
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
	 * The cached value of the '{@link #getVesselEventOptions() <em>Vessel Event Options</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselEventOptions()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselEventOption> vesselEventOptions;

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
	 * The cached value of the '{@link #getOptions() <em>Options</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOptions()
	 * @generated
	 * @ordered
	 */
	protected PartialCaseRowOptions options;

	/**
	 * The cached value of the '{@link #getCommodityCurveOptions() <em>Commodity Curve Options</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCommodityCurveOptions()
	 * @generated
	 * @ordered
	 */
	protected EList<CommodityCurveOption> commodityCurveOptions;

	/**
	 * The cached value of the '{@link #getGroup() <em>Group</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroup()
	 * @generated
	 * @ordered
	 */
	protected PartialCaseRowGroup group;

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
	@Override
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
	@Override
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
	@Override
	public EList<VesselEventOption> getVesselEventOptions() {
		if (vesselEventOptions == null) {
			vesselEventOptions = new EObjectResolvingEList<VesselEventOption>(VesselEventOption.class, this, AnalyticsPackage.PARTIAL_CASE_ROW__VESSEL_EVENT_OPTIONS);
		}
		return vesselEventOptions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	public PartialCaseRowOptions getOptions() {
		return options;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOptions(PartialCaseRowOptions newOptions, NotificationChain msgs) {
		PartialCaseRowOptions oldOptions = options;
		options = newOptions;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.PARTIAL_CASE_ROW__OPTIONS, oldOptions, newOptions);
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
	public void setOptions(PartialCaseRowOptions newOptions) {
		if (newOptions != options) {
			NotificationChain msgs = null;
			if (options != null)
				msgs = ((InternalEObject)options).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.PARTIAL_CASE_ROW__OPTIONS, null, msgs);
			if (newOptions != null)
				msgs = ((InternalEObject)newOptions).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.PARTIAL_CASE_ROW__OPTIONS, null, msgs);
			msgs = basicSetOptions(newOptions, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.PARTIAL_CASE_ROW__OPTIONS, newOptions, newOptions));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<CommodityCurveOption> getCommodityCurveOptions() {
		if (commodityCurveOptions == null) {
			commodityCurveOptions = new EObjectResolvingEList<CommodityCurveOption>(CommodityCurveOption.class, this, AnalyticsPackage.PARTIAL_CASE_ROW__COMMODITY_CURVE_OPTIONS);
		}
		return commodityCurveOptions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PartialCaseRowGroup getGroup() {
		if (group != null && group.eIsProxy()) {
			InternalEObject oldGroup = (InternalEObject)group;
			group = (PartialCaseRowGroup)eResolveProxy(oldGroup);
			if (group != oldGroup) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.PARTIAL_CASE_ROW__GROUP, oldGroup, group));
			}
		}
		return group;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PartialCaseRowGroup basicGetGroup() {
		return group;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetGroup(PartialCaseRowGroup newGroup, NotificationChain msgs) {
		PartialCaseRowGroup oldGroup = group;
		group = newGroup;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.PARTIAL_CASE_ROW__GROUP, oldGroup, newGroup);
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
	public void setGroup(PartialCaseRowGroup newGroup) {
		if (newGroup != group) {
			NotificationChain msgs = null;
			if (group != null)
				msgs = ((InternalEObject)group).eInverseRemove(this, AnalyticsPackage.PARTIAL_CASE_ROW_GROUP__ROWS, PartialCaseRowGroup.class, msgs);
			if (newGroup != null)
				msgs = ((InternalEObject)newGroup).eInverseAdd(this, AnalyticsPackage.PARTIAL_CASE_ROW_GROUP__ROWS, PartialCaseRowGroup.class, msgs);
			msgs = basicSetGroup(newGroup, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.PARTIAL_CASE_ROW__GROUP, newGroup, newGroup));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.PARTIAL_CASE_ROW__GROUP:
				if (group != null)
					msgs = ((InternalEObject)group).eInverseRemove(this, AnalyticsPackage.PARTIAL_CASE_ROW_GROUP__ROWS, PartialCaseRowGroup.class, msgs);
				return basicSetGroup((PartialCaseRowGroup)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.PARTIAL_CASE_ROW__OPTIONS:
				return basicSetOptions(null, msgs);
			case AnalyticsPackage.PARTIAL_CASE_ROW__GROUP:
				return basicSetGroup(null, msgs);
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
			case AnalyticsPackage.PARTIAL_CASE_ROW__VESSEL_EVENT_OPTIONS:
				return getVesselEventOptions();
			case AnalyticsPackage.PARTIAL_CASE_ROW__SHIPPING:
				return getShipping();
			case AnalyticsPackage.PARTIAL_CASE_ROW__OPTIONS:
				return getOptions();
			case AnalyticsPackage.PARTIAL_CASE_ROW__COMMODITY_CURVE_OPTIONS:
				return getCommodityCurveOptions();
			case AnalyticsPackage.PARTIAL_CASE_ROW__GROUP:
				if (resolve) return getGroup();
				return basicGetGroup();
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
			case AnalyticsPackage.PARTIAL_CASE_ROW__VESSEL_EVENT_OPTIONS:
				getVesselEventOptions().clear();
				getVesselEventOptions().addAll((Collection<? extends VesselEventOption>)newValue);
				return;
			case AnalyticsPackage.PARTIAL_CASE_ROW__SHIPPING:
				getShipping().clear();
				getShipping().addAll((Collection<? extends ShippingOption>)newValue);
				return;
			case AnalyticsPackage.PARTIAL_CASE_ROW__OPTIONS:
				setOptions((PartialCaseRowOptions)newValue);
				return;
			case AnalyticsPackage.PARTIAL_CASE_ROW__COMMODITY_CURVE_OPTIONS:
				getCommodityCurveOptions().clear();
				getCommodityCurveOptions().addAll((Collection<? extends CommodityCurveOption>)newValue);
				return;
			case AnalyticsPackage.PARTIAL_CASE_ROW__GROUP:
				setGroup((PartialCaseRowGroup)newValue);
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
			case AnalyticsPackage.PARTIAL_CASE_ROW__VESSEL_EVENT_OPTIONS:
				getVesselEventOptions().clear();
				return;
			case AnalyticsPackage.PARTIAL_CASE_ROW__SHIPPING:
				getShipping().clear();
				return;
			case AnalyticsPackage.PARTIAL_CASE_ROW__OPTIONS:
				setOptions((PartialCaseRowOptions)null);
				return;
			case AnalyticsPackage.PARTIAL_CASE_ROW__COMMODITY_CURVE_OPTIONS:
				getCommodityCurveOptions().clear();
				return;
			case AnalyticsPackage.PARTIAL_CASE_ROW__GROUP:
				setGroup((PartialCaseRowGroup)null);
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
			case AnalyticsPackage.PARTIAL_CASE_ROW__VESSEL_EVENT_OPTIONS:
				return vesselEventOptions != null && !vesselEventOptions.isEmpty();
			case AnalyticsPackage.PARTIAL_CASE_ROW__SHIPPING:
				return shipping != null && !shipping.isEmpty();
			case AnalyticsPackage.PARTIAL_CASE_ROW__OPTIONS:
				return options != null;
			case AnalyticsPackage.PARTIAL_CASE_ROW__COMMODITY_CURVE_OPTIONS:
				return commodityCurveOptions != null && !commodityCurveOptions.isEmpty();
			case AnalyticsPackage.PARTIAL_CASE_ROW__GROUP:
				return group != null;
		}
		return super.eIsSet(featureID);
	}

} //PartialCaseRowImpl
