/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;

import com.mmxlabs.models.lng.analytics.BaseCaseRowGroup;
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
 * An implementation of the model object '<em><b>Base Case</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BaseCaseImpl#getBaseCase <em>Base Case</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BaseCaseImpl#getProfitAndLoss <em>Profit And Loss</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BaseCaseImpl#isKeepExistingScenario <em>Keep Existing Scenario</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BaseCaseImpl#getGroups <em>Groups</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BaseCaseImpl extends EObjectImpl implements BaseCase {
	/**
	 * The cached value of the '{@link #getBaseCase() <em>Base Case</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseCase()
	 * @generated
	 * @ordered
	 */
	protected EList<BaseCaseRow> baseCase;

	/**
	 * The default value of the '{@link #getProfitAndLoss() <em>Profit And Loss</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProfitAndLoss()
	 * @generated
	 * @ordered
	 */
	protected static final long PROFIT_AND_LOSS_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getProfitAndLoss() <em>Profit And Loss</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProfitAndLoss()
	 * @generated
	 * @ordered
	 */
	protected long profitAndLoss = PROFIT_AND_LOSS_EDEFAULT;

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
	 * The cached value of the '{@link #getGroups() <em>Groups</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroups()
	 * @generated
	 * @ordered
	 */
	protected EList<BaseCaseRowGroup> groups;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BaseCaseImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.BASE_CASE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<BaseCaseRow> getBaseCase() {
		if (baseCase == null) {
			baseCase = new EObjectContainmentEList<BaseCaseRow>(BaseCaseRow.class, this, AnalyticsPackage.BASE_CASE__BASE_CASE);
		}
		return baseCase;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public long getProfitAndLoss() {
		return profitAndLoss;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setProfitAndLoss(long newProfitAndLoss) {
		long oldProfitAndLoss = profitAndLoss;
		profitAndLoss = newProfitAndLoss;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BASE_CASE__PROFIT_AND_LOSS, oldProfitAndLoss, profitAndLoss));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BASE_CASE__KEEP_EXISTING_SCENARIO, oldKeepExistingScenario, keepExistingScenario));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<BaseCaseRowGroup> getGroups() {
		if (groups == null) {
			groups = new EObjectContainmentEList<BaseCaseRowGroup>(BaseCaseRowGroup.class, this, AnalyticsPackage.BASE_CASE__GROUPS);
		}
		return groups;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.BASE_CASE__BASE_CASE:
				return ((InternalEList<?>)getBaseCase()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.BASE_CASE__GROUPS:
				return ((InternalEList<?>)getGroups()).basicRemove(otherEnd, msgs);
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
			case AnalyticsPackage.BASE_CASE__BASE_CASE:
				return getBaseCase();
			case AnalyticsPackage.BASE_CASE__PROFIT_AND_LOSS:
				return getProfitAndLoss();
			case AnalyticsPackage.BASE_CASE__KEEP_EXISTING_SCENARIO:
				return isKeepExistingScenario();
			case AnalyticsPackage.BASE_CASE__GROUPS:
				return getGroups();
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
			case AnalyticsPackage.BASE_CASE__BASE_CASE:
				getBaseCase().clear();
				getBaseCase().addAll((Collection<? extends BaseCaseRow>)newValue);
				return;
			case AnalyticsPackage.BASE_CASE__PROFIT_AND_LOSS:
				setProfitAndLoss((Long)newValue);
				return;
			case AnalyticsPackage.BASE_CASE__KEEP_EXISTING_SCENARIO:
				setKeepExistingScenario((Boolean)newValue);
				return;
			case AnalyticsPackage.BASE_CASE__GROUPS:
				getGroups().clear();
				getGroups().addAll((Collection<? extends BaseCaseRowGroup>)newValue);
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
			case AnalyticsPackage.BASE_CASE__BASE_CASE:
				getBaseCase().clear();
				return;
			case AnalyticsPackage.BASE_CASE__PROFIT_AND_LOSS:
				setProfitAndLoss(PROFIT_AND_LOSS_EDEFAULT);
				return;
			case AnalyticsPackage.BASE_CASE__KEEP_EXISTING_SCENARIO:
				setKeepExistingScenario(KEEP_EXISTING_SCENARIO_EDEFAULT);
				return;
			case AnalyticsPackage.BASE_CASE__GROUPS:
				getGroups().clear();
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
			case AnalyticsPackage.BASE_CASE__BASE_CASE:
				return baseCase != null && !baseCase.isEmpty();
			case AnalyticsPackage.BASE_CASE__PROFIT_AND_LOSS:
				return profitAndLoss != PROFIT_AND_LOSS_EDEFAULT;
			case AnalyticsPackage.BASE_CASE__KEEP_EXISTING_SCENARIO:
				return keepExistingScenario != KEEP_EXISTING_SCENARIO_EDEFAULT;
			case AnalyticsPackage.BASE_CASE__GROUPS:
				return groups != null && !groups.isEmpty();
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
		result.append(" (profitAndLoss: ");
		result.append(profitAndLoss);
		result.append(", keepExistingScenario: ");
		result.append(keepExistingScenario);
		result.append(')');
		return result.toString();
	}

} //BaseCaseImpl
