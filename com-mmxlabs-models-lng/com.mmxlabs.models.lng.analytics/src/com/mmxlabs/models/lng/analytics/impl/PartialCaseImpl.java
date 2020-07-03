/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.PartialCase;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Partial Case</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.PartialCaseImpl#getPartialCase <em>Partial Case</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.PartialCaseImpl#isKeepExistingScenario <em>Keep Existing Scenario</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PartialCaseImpl extends EObjectImpl implements PartialCase {
	/**
	 * The cached value of the '{@link #getPartialCase() <em>Partial Case</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPartialCase()
	 * @generated
	 * @ordered
	 */
	protected EList<PartialCaseRow> partialCase;

	/**
	 * The default value of the '{@link #isKeepExistingScenario() <em>Keep Existing Scenario</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isKeepExistingScenario()
	 * @generated
	 * @ordered
	 */
	protected static final boolean KEEP_EXISTING_SCENARIO_EDEFAULT = false;
	/**
	 * The cached value of the '{@link #isKeepExistingScenario() <em>Keep Existing Scenario</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isKeepExistingScenario()
	 * @generated
	 * @ordered
	 */
	protected boolean keepExistingScenario = KEEP_EXISTING_SCENARIO_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PartialCaseImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.PARTIAL_CASE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<PartialCaseRow> getPartialCase() {
		if (partialCase == null) {
			partialCase = new EObjectContainmentEList<PartialCaseRow>(PartialCaseRow.class, this, AnalyticsPackage.PARTIAL_CASE__PARTIAL_CASE);
		}
		return partialCase;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isKeepExistingScenario() {
		return keepExistingScenario;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setKeepExistingScenario(boolean newKeepExistingScenario) {
		boolean oldKeepExistingScenario = keepExistingScenario;
		keepExistingScenario = newKeepExistingScenario;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.PARTIAL_CASE__KEEP_EXISTING_SCENARIO, oldKeepExistingScenario, keepExistingScenario));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.PARTIAL_CASE__PARTIAL_CASE:
				return ((InternalEList<?>)getPartialCase()).basicRemove(otherEnd, msgs);
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
			case AnalyticsPackage.PARTIAL_CASE__PARTIAL_CASE:
				return getPartialCase();
			case AnalyticsPackage.PARTIAL_CASE__KEEP_EXISTING_SCENARIO:
				return isKeepExistingScenario();
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
			case AnalyticsPackage.PARTIAL_CASE__PARTIAL_CASE:
				getPartialCase().clear();
				getPartialCase().addAll((Collection<? extends PartialCaseRow>)newValue);
				return;
			case AnalyticsPackage.PARTIAL_CASE__KEEP_EXISTING_SCENARIO:
				setKeepExistingScenario((Boolean)newValue);
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
			case AnalyticsPackage.PARTIAL_CASE__PARTIAL_CASE:
				getPartialCase().clear();
				return;
			case AnalyticsPackage.PARTIAL_CASE__KEEP_EXISTING_SCENARIO:
				setKeepExistingScenario(KEEP_EXISTING_SCENARIO_EDEFAULT);
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
			case AnalyticsPackage.PARTIAL_CASE__PARTIAL_CASE:
				return partialCase != null && !partialCase.isEmpty();
			case AnalyticsPackage.PARTIAL_CASE__KEEP_EXISTING_SCENARIO:
				return keepExistingScenario != KEEP_EXISTING_SCENARIO_EDEFAULT;
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
		result.append(" (keepExistingScenario: ");
		result.append(keepExistingScenario);
		result.append(')');
		return result.toString();
	}

} //PartialCaseImpl
