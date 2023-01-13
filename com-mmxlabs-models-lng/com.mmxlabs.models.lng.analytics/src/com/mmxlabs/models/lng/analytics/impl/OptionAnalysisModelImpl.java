/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.PartialCase;

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
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.OptionAnalysisModelImpl#getResults <em>Results</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.OptionAnalysisModelImpl#isUseTargetPNL <em>Use Target PNL</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.OptionAnalysisModelImpl#getMode <em>Mode</em>}</li>
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
	 * The cached value of the '{@link #getResults() <em>Results</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResults()
	 * @generated
	 * @ordered
	 */
	protected AbstractSolutionSet results;

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
	 * The default value of the '{@link #getMode() <em>Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMode()
	 * @generated
	 * @ordered
	 */
	protected static final int MODE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMode() <em>Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMode()
	 * @generated
	 * @ordered
	 */
	protected int mode = MODE_EDEFAULT;

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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
	public AbstractSolutionSet getResults() {
		return results;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetResults(AbstractSolutionSet newResults, NotificationChain msgs) {
		AbstractSolutionSet oldResults = results;
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
	@Override
	public void setResults(AbstractSolutionSet newResults) {
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
	@Override
	public boolean isUseTargetPNL() {
		return useTargetPNL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
	public int getMode() {
		return mode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMode(int newMode) {
		int oldMode = mode;
		mode = newMode;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.OPTION_ANALYSIS_MODEL__MODE, oldMode, mode));
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
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__RESULTS:
				return basicSetResults(null, msgs);
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
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__RESULTS:
				return getResults();
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__USE_TARGET_PNL:
				return isUseTargetPNL();
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__MODE:
				return getMode();
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
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__RESULTS:
				setResults((AbstractSolutionSet)newValue);
				return;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__USE_TARGET_PNL:
				setUseTargetPNL((Boolean)newValue);
				return;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__MODE:
				setMode((Integer)newValue);
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
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__RESULTS:
				setResults((AbstractSolutionSet)null);
				return;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__USE_TARGET_PNL:
				setUseTargetPNL(USE_TARGET_PNL_EDEFAULT);
				return;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__MODE:
				setMode(MODE_EDEFAULT);
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
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__RESULTS:
				return results != null;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__USE_TARGET_PNL:
				return useTargetPNL != USE_TARGET_PNL_EDEFAULT;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__MODE:
				return mode != MODE_EDEFAULT;
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
		result.append(", mode: ");
		result.append(mode);
		result.append(')');
		return result.toString();
	}

} //OptionAnalysisModelImpl
