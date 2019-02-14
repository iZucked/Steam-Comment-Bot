/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AbstractAnalysisModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;

import com.mmxlabs.models.mmxcore.impl.NamedObjectImpl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Analysis Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AbstractAnalysisModelImpl#getBuys <em>Buys</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AbstractAnalysisModelImpl#getSells <em>Sells</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AbstractAnalysisModelImpl#getShippingTemplates <em>Shipping Templates</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class AbstractAnalysisModelImpl extends NamedObjectImpl implements AbstractAnalysisModel {
	/**
	 * The cached value of the '{@link #getBuys() <em>Buys</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBuys()
	 * @generated
	 * @ordered
	 */
	protected EList<BuyOption> buys;

	/**
	 * The cached value of the '{@link #getSells() <em>Sells</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSells()
	 * @generated
	 * @ordered
	 */
	protected EList<SellOption> sells;

	/**
	 * The cached value of the '{@link #getShippingTemplates() <em>Shipping Templates</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShippingTemplates()
	 * @generated
	 * @ordered
	 */
	protected EList<ShippingOption> shippingTemplates;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AbstractAnalysisModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<BuyOption> getBuys() {
		if (buys == null) {
			buys = new EObjectContainmentEList<BuyOption>(BuyOption.class, this, AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__BUYS);
		}
		return buys;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<SellOption> getSells() {
		if (sells == null) {
			sells = new EObjectContainmentEList<SellOption>(SellOption.class, this, AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SELLS);
		}
		return sells;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ShippingOption> getShippingTemplates() {
		if (shippingTemplates == null) {
			shippingTemplates = new EObjectContainmentEList<ShippingOption>(ShippingOption.class, this, AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES);
		}
		return shippingTemplates;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__BUYS:
				return ((InternalEList<?>)getBuys()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SELLS:
				return ((InternalEList<?>)getSells()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES:
				return ((InternalEList<?>)getShippingTemplates()).basicRemove(otherEnd, msgs);
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
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__BUYS:
				return getBuys();
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SELLS:
				return getSells();
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES:
				return getShippingTemplates();
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
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__BUYS:
				getBuys().clear();
				getBuys().addAll((Collection<? extends BuyOption>)newValue);
				return;
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SELLS:
				getSells().clear();
				getSells().addAll((Collection<? extends SellOption>)newValue);
				return;
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES:
				getShippingTemplates().clear();
				getShippingTemplates().addAll((Collection<? extends ShippingOption>)newValue);
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
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__BUYS:
				getBuys().clear();
				return;
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SELLS:
				getSells().clear();
				return;
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES:
				getShippingTemplates().clear();
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
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__BUYS:
				return buys != null && !buys.isEmpty();
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SELLS:
				return sells != null && !sells.isEmpty();
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES:
				return shippingTemplates != null && !shippingTemplates.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //AbstractAnalysisModelImpl
