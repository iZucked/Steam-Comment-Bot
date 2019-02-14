/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.MTMModel;
import com.mmxlabs.models.lng.analytics.MTMRow;
import com.mmxlabs.models.lng.analytics.ViabilityRow;

import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>MTM Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MTMModelImpl#getRows <em>Rows</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MTMModelImpl#getMarkets <em>Markets</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MTMModelImpl#getNominalMarkets <em>Nominal Markets</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MTMModelImpl extends AbstractAnalysisModelImpl implements MTMModel {
	/**
	 * The cached value of the '{@link #getRows() <em>Rows</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRows()
	 * @generated
	 * @ordered
	 */
	protected EList<MTMRow> rows;

	/**
	 * The cached value of the '{@link #getMarkets() <em>Markets</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarkets()
	 * @generated
	 * @ordered
	 */
	protected EList<SpotMarket> markets;

	/**
	 * The cached value of the '{@link #getNominalMarkets() <em>Nominal Markets</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNominalMarkets()
	 * @generated
	 * @ordered
	 */
	protected EList<CharterInMarket> nominalMarkets;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MTMModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.MTM_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MTMRow> getRows() {
		if (rows == null) {
			rows = new EObjectContainmentEList<MTMRow>(MTMRow.class, this, AnalyticsPackage.MTM_MODEL__ROWS);
		}
		return rows;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<SpotMarket> getMarkets() {
		if (markets == null) {
			markets = new EObjectResolvingEList<SpotMarket>(SpotMarket.class, this, AnalyticsPackage.MTM_MODEL__MARKETS);
		}
		return markets;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<CharterInMarket> getNominalMarkets() {
		if (nominalMarkets == null) {
			nominalMarkets = new EObjectResolvingEList<CharterInMarket>(CharterInMarket.class, this, AnalyticsPackage.MTM_MODEL__NOMINAL_MARKETS);
		}
		return nominalMarkets;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.MTM_MODEL__ROWS:
				return ((InternalEList<?>)getRows()).basicRemove(otherEnd, msgs);
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
			case AnalyticsPackage.MTM_MODEL__ROWS:
				return getRows();
			case AnalyticsPackage.MTM_MODEL__MARKETS:
				return getMarkets();
			case AnalyticsPackage.MTM_MODEL__NOMINAL_MARKETS:
				return getNominalMarkets();
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
			case AnalyticsPackage.MTM_MODEL__ROWS:
				getRows().clear();
				getRows().addAll((Collection<? extends MTMRow>)newValue);
				return;
			case AnalyticsPackage.MTM_MODEL__MARKETS:
				getMarkets().clear();
				getMarkets().addAll((Collection<? extends SpotMarket>)newValue);
				return;
			case AnalyticsPackage.MTM_MODEL__NOMINAL_MARKETS:
				getNominalMarkets().clear();
				getNominalMarkets().addAll((Collection<? extends CharterInMarket>)newValue);
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
			case AnalyticsPackage.MTM_MODEL__ROWS:
				getRows().clear();
				return;
			case AnalyticsPackage.MTM_MODEL__MARKETS:
				getMarkets().clear();
				return;
			case AnalyticsPackage.MTM_MODEL__NOMINAL_MARKETS:
				getNominalMarkets().clear();
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
			case AnalyticsPackage.MTM_MODEL__ROWS:
				return rows != null && !rows.isEmpty();
			case AnalyticsPackage.MTM_MODEL__MARKETS:
				return markets != null && !markets.isEmpty();
			case AnalyticsPackage.MTM_MODEL__NOMINAL_MARKETS:
				return nominalMarkets != null && !nominalMarkets.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //MTMModelImpl
