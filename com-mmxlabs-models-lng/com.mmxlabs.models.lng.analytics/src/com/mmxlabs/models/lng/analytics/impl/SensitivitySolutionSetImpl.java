/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.CargoPnLResult;
import com.mmxlabs.models.lng.analytics.PortfolioSensitivityResult;
import com.mmxlabs.models.lng.analytics.SensitivitySolutionSet;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Sensitivity Solution Set</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SensitivitySolutionSetImpl#getPorfolioPnLResult <em>Porfolio Pn LResult</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SensitivitySolutionSetImpl#getCargoPnLResults <em>Cargo Pn LResults</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SensitivitySolutionSetImpl extends AbstractSolutionSetImpl implements SensitivitySolutionSet {
	/**
	 * The cached value of the '{@link #getPorfolioPnLResult() <em>Porfolio Pn LResult</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPorfolioPnLResult()
	 * @generated
	 * @ordered
	 */
	protected PortfolioSensitivityResult porfolioPnLResult;

	/**
	 * The cached value of the '{@link #getCargoPnLResults() <em>Cargo Pn LResults</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoPnLResults()
	 * @generated
	 * @ordered
	 */
	protected EList<CargoPnLResult> cargoPnLResults;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SensitivitySolutionSetImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.SENSITIVITY_SOLUTION_SET;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PortfolioSensitivityResult getPorfolioPnLResult() {
		return porfolioPnLResult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPorfolioPnLResult(PortfolioSensitivityResult newPorfolioPnLResult, NotificationChain msgs) {
		PortfolioSensitivityResult oldPorfolioPnLResult = porfolioPnLResult;
		porfolioPnLResult = newPorfolioPnLResult;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SENSITIVITY_SOLUTION_SET__PORFOLIO_PN_LRESULT, oldPorfolioPnLResult, newPorfolioPnLResult);
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
	public void setPorfolioPnLResult(PortfolioSensitivityResult newPorfolioPnLResult) {
		if (newPorfolioPnLResult != porfolioPnLResult) {
			NotificationChain msgs = null;
			if (porfolioPnLResult != null)
				msgs = ((InternalEObject)porfolioPnLResult).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SENSITIVITY_SOLUTION_SET__PORFOLIO_PN_LRESULT, null, msgs);
			if (newPorfolioPnLResult != null)
				msgs = ((InternalEObject)newPorfolioPnLResult).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SENSITIVITY_SOLUTION_SET__PORFOLIO_PN_LRESULT, null, msgs);
			msgs = basicSetPorfolioPnLResult(newPorfolioPnLResult, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SENSITIVITY_SOLUTION_SET__PORFOLIO_PN_LRESULT, newPorfolioPnLResult, newPorfolioPnLResult));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<CargoPnLResult> getCargoPnLResults() {
		if (cargoPnLResults == null) {
			cargoPnLResults = new EObjectContainmentEList<CargoPnLResult>(CargoPnLResult.class, this, AnalyticsPackage.SENSITIVITY_SOLUTION_SET__CARGO_PN_LRESULTS);
		}
		return cargoPnLResults;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.SENSITIVITY_SOLUTION_SET__PORFOLIO_PN_LRESULT:
				return basicSetPorfolioPnLResult(null, msgs);
			case AnalyticsPackage.SENSITIVITY_SOLUTION_SET__CARGO_PN_LRESULTS:
				return ((InternalEList<?>)getCargoPnLResults()).basicRemove(otherEnd, msgs);
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
			case AnalyticsPackage.SENSITIVITY_SOLUTION_SET__PORFOLIO_PN_LRESULT:
				return getPorfolioPnLResult();
			case AnalyticsPackage.SENSITIVITY_SOLUTION_SET__CARGO_PN_LRESULTS:
				return getCargoPnLResults();
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
			case AnalyticsPackage.SENSITIVITY_SOLUTION_SET__PORFOLIO_PN_LRESULT:
				setPorfolioPnLResult((PortfolioSensitivityResult)newValue);
				return;
			case AnalyticsPackage.SENSITIVITY_SOLUTION_SET__CARGO_PN_LRESULTS:
				getCargoPnLResults().clear();
				getCargoPnLResults().addAll((Collection<? extends CargoPnLResult>)newValue);
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
			case AnalyticsPackage.SENSITIVITY_SOLUTION_SET__PORFOLIO_PN_LRESULT:
				setPorfolioPnLResult((PortfolioSensitivityResult)null);
				return;
			case AnalyticsPackage.SENSITIVITY_SOLUTION_SET__CARGO_PN_LRESULTS:
				getCargoPnLResults().clear();
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
			case AnalyticsPackage.SENSITIVITY_SOLUTION_SET__PORFOLIO_PN_LRESULT:
				return porfolioPnLResult != null;
			case AnalyticsPackage.SENSITIVITY_SOLUTION_SET__CARGO_PN_LRESULTS:
				return cargoPnLResults != null && !cargoPnLResults.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //SensitivitySolutionSetImpl
