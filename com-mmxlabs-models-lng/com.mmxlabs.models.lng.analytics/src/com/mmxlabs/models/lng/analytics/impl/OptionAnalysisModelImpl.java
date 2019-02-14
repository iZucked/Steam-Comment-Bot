/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.PartialCase;
import com.mmxlabs.models.lng.analytics.Result;
import com.mmxlabs.models.lng.analytics.ResultSet;
import com.mmxlabs.models.lng.analytics.SellOption;

import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.mmxcore.impl.NamedObjectImpl;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Option Analysis Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.OptionAnalysisModelImpl#getBaseCase <em>Base Case</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.OptionAnalysisModelImpl#getPartialCase <em>Partial Case</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.OptionAnalysisModelImpl#getBaseCaseResult <em>Base Case Result</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.OptionAnalysisModelImpl#getResults <em>Results</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.OptionAnalysisModelImpl#isUseTargetPNL <em>Use Target PNL</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.OptionAnalysisModelImpl#getChildren <em>Children</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OptionAnalysisModelImpl extends AbstractAnalysisModelImpl implements OptionAnalysisModel {
	/**
	 * The cached value of the '{@link #getBaseCase() <em>Base Case</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseCase()
	 * @generated
	 * @ordered
	 */
	protected BaseCase baseCase;

	/**
	 * The cached value of the '{@link #getPartialCase() <em>Partial Case</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPartialCase()
	 * @generated
	 * @ordered
	 */
	protected PartialCase partialCase;

	/**
	 * The cached value of the '{@link #getBaseCaseResult() <em>Base Case Result</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseCaseResult()
	 * @generated
	 * @ordered
	 */
	protected Result baseCaseResult;

	/**
	 * The cached value of the '{@link #getResults() <em>Results</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResults()
	 * @generated
	 * @ordered
	 */
	protected Result results;

	/**
	 * The default value of the '{@link #isUseTargetPNL() <em>Use Target PNL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUseTargetPNL()
	 * @generated
	 * @ordered
	 */
	protected static final boolean USE_TARGET_PNL_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isUseTargetPNL() <em>Use Target PNL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUseTargetPNL()
	 * @generated
	 * @ordered
	 */
	protected boolean useTargetPNL = USE_TARGET_PNL_EDEFAULT;

	/**
	 * The cached value of the '{@link #getChildren() <em>Children</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildren()
	 * @generated
	 * @ordered
	 */
	protected EList<OptionAnalysisModel> children;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OptionAnalysisModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseCase getBaseCase() {
		return baseCase;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetBaseCase(BaseCase newBaseCase, NotificationChain msgs) {
		BaseCase oldBaseCase = baseCase;
		baseCase = newBaseCase;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.OPTION_ANALYSIS_MODEL__BASE_CASE, oldBaseCase, newBaseCase);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBaseCase(BaseCase newBaseCase) {
		if (newBaseCase != baseCase) {
			NotificationChain msgs = null;
			if (baseCase != null)
				msgs = ((InternalEObject)baseCase).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.OPTION_ANALYSIS_MODEL__BASE_CASE, null, msgs);
			if (newBaseCase != null)
				msgs = ((InternalEObject)newBaseCase).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.OPTION_ANALYSIS_MODEL__BASE_CASE, null, msgs);
			msgs = basicSetBaseCase(newBaseCase, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.OPTION_ANALYSIS_MODEL__BASE_CASE, newBaseCase, newBaseCase));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PartialCase getPartialCase() {
		return partialCase;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPartialCase(PartialCase newPartialCase, NotificationChain msgs) {
		PartialCase oldPartialCase = partialCase;
		partialCase = newPartialCase;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.OPTION_ANALYSIS_MODEL__PARTIAL_CASE, oldPartialCase, newPartialCase);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPartialCase(PartialCase newPartialCase) {
		if (newPartialCase != partialCase) {
			NotificationChain msgs = null;
			if (partialCase != null)
				msgs = ((InternalEObject)partialCase).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.OPTION_ANALYSIS_MODEL__PARTIAL_CASE, null, msgs);
			if (newPartialCase != null)
				msgs = ((InternalEObject)newPartialCase).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.OPTION_ANALYSIS_MODEL__PARTIAL_CASE, null, msgs);
			msgs = basicSetPartialCase(newPartialCase, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.OPTION_ANALYSIS_MODEL__PARTIAL_CASE, newPartialCase, newPartialCase));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Result getBaseCaseResult() {
		return baseCaseResult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetBaseCaseResult(Result newBaseCaseResult, NotificationChain msgs) {
		Result oldBaseCaseResult = baseCaseResult;
		baseCaseResult = newBaseCaseResult;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.OPTION_ANALYSIS_MODEL__BASE_CASE_RESULT, oldBaseCaseResult, newBaseCaseResult);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBaseCaseResult(Result newBaseCaseResult) {
		if (newBaseCaseResult != baseCaseResult) {
			NotificationChain msgs = null;
			if (baseCaseResult != null)
				msgs = ((InternalEObject)baseCaseResult).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.OPTION_ANALYSIS_MODEL__BASE_CASE_RESULT, null, msgs);
			if (newBaseCaseResult != null)
				msgs = ((InternalEObject)newBaseCaseResult).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.OPTION_ANALYSIS_MODEL__BASE_CASE_RESULT, null, msgs);
			msgs = basicSetBaseCaseResult(newBaseCaseResult, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.OPTION_ANALYSIS_MODEL__BASE_CASE_RESULT, newBaseCaseResult, newBaseCaseResult));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Result getResults() {
		return results;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetResults(Result newResults, NotificationChain msgs) {
		Result oldResults = results;
		results = newResults;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.OPTION_ANALYSIS_MODEL__RESULTS, oldResults, newResults);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setResults(Result newResults) {
		if (newResults != results) {
			NotificationChain msgs = null;
			if (results != null)
				msgs = ((InternalEObject)results).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.OPTION_ANALYSIS_MODEL__RESULTS, null, msgs);
			if (newResults != null)
				msgs = ((InternalEObject)newResults).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.OPTION_ANALYSIS_MODEL__RESULTS, null, msgs);
			msgs = basicSetResults(newResults, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.OPTION_ANALYSIS_MODEL__RESULTS, newResults, newResults));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isUseTargetPNL() {
		return useTargetPNL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUseTargetPNL(boolean newUseTargetPNL) {
		boolean oldUseTargetPNL = useTargetPNL;
		useTargetPNL = newUseTargetPNL;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.OPTION_ANALYSIS_MODEL__USE_TARGET_PNL, oldUseTargetPNL, useTargetPNL));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<OptionAnalysisModel> getChildren() {
		if (children == null) {
			children = new EObjectContainmentEList<OptionAnalysisModel>(OptionAnalysisModel.class, this, AnalyticsPackage.OPTION_ANALYSIS_MODEL__CHILDREN);
		}
		return children;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__BASE_CASE:
				return basicSetBaseCase(null, msgs);
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__PARTIAL_CASE:
				return basicSetPartialCase(null, msgs);
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__BASE_CASE_RESULT:
				return basicSetBaseCaseResult(null, msgs);
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__RESULTS:
				return basicSetResults(null, msgs);
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__CHILDREN:
				return ((InternalEList<?>)getChildren()).basicRemove(otherEnd, msgs);
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
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__BASE_CASE:
				return getBaseCase();
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__PARTIAL_CASE:
				return getPartialCase();
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__BASE_CASE_RESULT:
				return getBaseCaseResult();
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__RESULTS:
				return getResults();
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__USE_TARGET_PNL:
				return isUseTargetPNL();
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__CHILDREN:
				return getChildren();
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
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__BASE_CASE:
				setBaseCase((BaseCase)newValue);
				return;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__PARTIAL_CASE:
				setPartialCase((PartialCase)newValue);
				return;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__BASE_CASE_RESULT:
				setBaseCaseResult((Result)newValue);
				return;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__RESULTS:
				setResults((Result)newValue);
				return;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__USE_TARGET_PNL:
				setUseTargetPNL((Boolean)newValue);
				return;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__CHILDREN:
				getChildren().clear();
				getChildren().addAll((Collection<? extends OptionAnalysisModel>)newValue);
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
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__BASE_CASE:
				setBaseCase((BaseCase)null);
				return;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__PARTIAL_CASE:
				setPartialCase((PartialCase)null);
				return;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__BASE_CASE_RESULT:
				setBaseCaseResult((Result)null);
				return;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__RESULTS:
				setResults((Result)null);
				return;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__USE_TARGET_PNL:
				setUseTargetPNL(USE_TARGET_PNL_EDEFAULT);
				return;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__CHILDREN:
				getChildren().clear();
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
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__BASE_CASE:
				return baseCase != null;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__PARTIAL_CASE:
				return partialCase != null;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__BASE_CASE_RESULT:
				return baseCaseResult != null;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__RESULTS:
				return results != null;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__USE_TARGET_PNL:
				return useTargetPNL != USE_TARGET_PNL_EDEFAULT;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__CHILDREN:
				return children != null && !children.isEmpty();
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
		result.append(" (useTargetPNL: ");
		result.append(useTargetPNL);
		result.append(')');
		return result.toString();
	}

} //OptionAnalysisModelImpl
