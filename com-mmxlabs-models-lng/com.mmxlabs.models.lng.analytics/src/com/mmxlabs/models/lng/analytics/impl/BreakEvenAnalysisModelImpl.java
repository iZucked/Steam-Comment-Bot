/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisModel;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisRow;

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
 * An implementation of the model object '<em><b>Break Even Analysis Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisModelImpl#getRows <em>Rows</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BreakEvenAnalysisModelImpl#getMarkets <em>Markets</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BreakEvenAnalysisModelImpl extends AbstractAnalysisModelImpl implements BreakEvenAnalysisModel {
	/**
	 * The cached value of the '{@link #getRows() <em>Rows</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRows()
	 * @generated
	 * @ordered
	 */
	protected EList<BreakEvenAnalysisRow> rows;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BreakEvenAnalysisModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.BREAK_EVEN_ANALYSIS_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<BreakEvenAnalysisRow> getRows() {
		if (rows == null) {
			rows = new EObjectContainmentEList<BreakEvenAnalysisRow>(BreakEvenAnalysisRow.class, this, AnalyticsPackage.BREAK_EVEN_ANALYSIS_MODEL__ROWS);
		}
		return rows;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<SpotMarket> getMarkets() {
		if (markets == null) {
			markets = new EObjectResolvingEList<SpotMarket>(SpotMarket.class, this, AnalyticsPackage.BREAK_EVEN_ANALYSIS_MODEL__MARKETS);
		}
		return markets;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_MODEL__ROWS:
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
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_MODEL__ROWS:
				return getRows();
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_MODEL__MARKETS:
				return getMarkets();
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
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_MODEL__ROWS:
				getRows().clear();
				getRows().addAll((Collection<? extends BreakEvenAnalysisRow>)newValue);
				return;
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_MODEL__MARKETS:
				getMarkets().clear();
				getMarkets().addAll((Collection<? extends SpotMarket>)newValue);
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
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_MODEL__ROWS:
				getRows().clear();
				return;
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_MODEL__MARKETS:
				getMarkets().clear();
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
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_MODEL__ROWS:
				return rows != null && !rows.isEmpty();
			case AnalyticsPackage.BREAK_EVEN_ANALYSIS_MODEL__MARKETS:
				return markets != null && !markets.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //BreakEvenAnalysisModelImpl
