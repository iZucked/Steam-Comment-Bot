/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.ActionableSet;
import com.mmxlabs.models.lng.analytics.ActionableSetPlan;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;

import com.mmxlabs.models.lng.cargo.Slot;

import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;
import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Actionable Set Plan</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ActionableSetPlanImpl#getActionSets <em>Action Sets</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ActionableSetPlanImpl#getExtraSlots <em>Extra Slots</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ActionableSetPlanImpl extends UUIDObjectImpl implements ActionableSetPlan {
	/**
	 * The cached value of the '{@link #getActionSets() <em>Action Sets</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getActionSets()
	 * @generated
	 * @ordered
	 */
	protected EList<ActionableSet> actionSets;

	/**
	 * The cached value of the '{@link #getExtraSlots() <em>Extra Slots</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtraSlots()
	 * @generated
	 * @ordered
	 */
	protected EList<Slot> extraSlots;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ActionableSetPlanImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.ACTIONABLE_SET_PLAN;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ActionableSet> getActionSets() {
		if (actionSets == null) {
			actionSets = new EObjectContainmentEList<ActionableSet>(ActionableSet.class, this, AnalyticsPackage.ACTIONABLE_SET_PLAN__ACTION_SETS);
		}
		return actionSets;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Slot> getExtraSlots() {
		if (extraSlots == null) {
			extraSlots = new EObjectContainmentEList<Slot>(Slot.class, this, AnalyticsPackage.ACTIONABLE_SET_PLAN__EXTRA_SLOTS);
		}
		return extraSlots;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.ACTIONABLE_SET_PLAN__ACTION_SETS:
				return ((InternalEList<?>)getActionSets()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.ACTIONABLE_SET_PLAN__EXTRA_SLOTS:
				return ((InternalEList<?>)getExtraSlots()).basicRemove(otherEnd, msgs);
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
			case AnalyticsPackage.ACTIONABLE_SET_PLAN__ACTION_SETS:
				return getActionSets();
			case AnalyticsPackage.ACTIONABLE_SET_PLAN__EXTRA_SLOTS:
				return getExtraSlots();
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
			case AnalyticsPackage.ACTIONABLE_SET_PLAN__ACTION_SETS:
				getActionSets().clear();
				getActionSets().addAll((Collection<? extends ActionableSet>)newValue);
				return;
			case AnalyticsPackage.ACTIONABLE_SET_PLAN__EXTRA_SLOTS:
				getExtraSlots().clear();
				getExtraSlots().addAll((Collection<? extends Slot>)newValue);
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
			case AnalyticsPackage.ACTIONABLE_SET_PLAN__ACTION_SETS:
				getActionSets().clear();
				return;
			case AnalyticsPackage.ACTIONABLE_SET_PLAN__EXTRA_SLOTS:
				getExtraSlots().clear();
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
			case AnalyticsPackage.ACTIONABLE_SET_PLAN__ACTION_SETS:
				return actionSets != null && !actionSets.isEmpty();
			case AnalyticsPackage.ACTIONABLE_SET_PLAN__EXTRA_SLOTS:
				return extraSlots != null && !extraSlots.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ActionableSetPlanImpl
