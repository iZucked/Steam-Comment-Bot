/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.SchedulePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Generated Charter Out</b></em>'.
 * @since 2.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.GeneratedCharterOutImpl#getGroupProfitAndLoss <em>Group Profit And Loss</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.GeneratedCharterOutImpl#getGroupProfitAndLossNoTimeCharter <em>Group Profit And Loss No Time Charter</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.GeneratedCharterOutImpl#getRevenue <em>Revenue</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GeneratedCharterOutImpl extends EventImpl implements GeneratedCharterOut {
	/**
	 * The cached value of the '{@link #getGroupProfitAndLoss() <em>Group Profit And Loss</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @see #getGroupProfitAndLoss()
	 * @generated
	 * @ordered
	 */
	protected GroupProfitAndLoss groupProfitAndLoss;

	/**
	 * The cached value of the '{@link #getGroupProfitAndLossNoTimeCharter() <em>Group Profit And Loss No Time Charter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * @since 6.0
	 * <!-- end-user-doc -->
	 * @see #getGroupProfitAndLossNoTimeCharter()
	 * @generated
	 * @ordered
	 */
	protected GroupProfitAndLoss groupProfitAndLossNoTimeCharter;

	/**
	 * The default value of the '{@link #getRevenue() <em>Revenue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRevenue()
	 * @generated
	 * @ordered
	 */
	protected static final int REVENUE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getRevenue() <em>Revenue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRevenue()
	 * @generated
	 * @ordered
	 */
	protected int revenue = REVENUE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GeneratedCharterOutImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.GENERATED_CHARTER_OUT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GroupProfitAndLoss getGroupProfitAndLoss() {
		return groupProfitAndLoss;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetGroupProfitAndLoss(GroupProfitAndLoss newGroupProfitAndLoss, NotificationChain msgs) {
		GroupProfitAndLoss oldGroupProfitAndLoss = groupProfitAndLoss;
		groupProfitAndLoss = newGroupProfitAndLoss;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchedulePackage.GENERATED_CHARTER_OUT__GROUP_PROFIT_AND_LOSS, oldGroupProfitAndLoss, newGroupProfitAndLoss);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGroupProfitAndLoss(GroupProfitAndLoss newGroupProfitAndLoss) {
		if (newGroupProfitAndLoss != groupProfitAndLoss) {
			NotificationChain msgs = null;
			if (groupProfitAndLoss != null)
				msgs = ((InternalEObject)groupProfitAndLoss).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.GENERATED_CHARTER_OUT__GROUP_PROFIT_AND_LOSS, null, msgs);
			if (newGroupProfitAndLoss != null)
				msgs = ((InternalEObject)newGroupProfitAndLoss).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.GENERATED_CHARTER_OUT__GROUP_PROFIT_AND_LOSS, null, msgs);
			msgs = basicSetGroupProfitAndLoss(newGroupProfitAndLoss, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.GENERATED_CHARTER_OUT__GROUP_PROFIT_AND_LOSS, newGroupProfitAndLoss, newGroupProfitAndLoss));
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 6.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GroupProfitAndLoss getGroupProfitAndLossNoTimeCharter() {
		return groupProfitAndLossNoTimeCharter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 6.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetGroupProfitAndLossNoTimeCharter(GroupProfitAndLoss newGroupProfitAndLossNoTimeCharter, NotificationChain msgs) {
		GroupProfitAndLoss oldGroupProfitAndLossNoTimeCharter = groupProfitAndLossNoTimeCharter;
		groupProfitAndLossNoTimeCharter = newGroupProfitAndLossNoTimeCharter;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchedulePackage.GENERATED_CHARTER_OUT__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER, oldGroupProfitAndLossNoTimeCharter, newGroupProfitAndLossNoTimeCharter);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 6.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGroupProfitAndLossNoTimeCharter(GroupProfitAndLoss newGroupProfitAndLossNoTimeCharter) {
		if (newGroupProfitAndLossNoTimeCharter != groupProfitAndLossNoTimeCharter) {
			NotificationChain msgs = null;
			if (groupProfitAndLossNoTimeCharter != null)
				msgs = ((InternalEObject)groupProfitAndLossNoTimeCharter).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.GENERATED_CHARTER_OUT__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER, null, msgs);
			if (newGroupProfitAndLossNoTimeCharter != null)
				msgs = ((InternalEObject)newGroupProfitAndLossNoTimeCharter).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.GENERATED_CHARTER_OUT__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER, null, msgs);
			msgs = basicSetGroupProfitAndLossNoTimeCharter(newGroupProfitAndLossNoTimeCharter, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.GENERATED_CHARTER_OUT__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER, newGroupProfitAndLossNoTimeCharter, newGroupProfitAndLossNoTimeCharter));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getRevenue() {
		return revenue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRevenue(int newRevenue) {
		int oldRevenue = revenue;
		revenue = newRevenue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.GENERATED_CHARTER_OUT__REVENUE, oldRevenue, revenue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.GENERATED_CHARTER_OUT__GROUP_PROFIT_AND_LOSS:
				return basicSetGroupProfitAndLoss(null, msgs);
			case SchedulePackage.GENERATED_CHARTER_OUT__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER:
				return basicSetGroupProfitAndLossNoTimeCharter(null, msgs);
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
			case SchedulePackage.GENERATED_CHARTER_OUT__GROUP_PROFIT_AND_LOSS:
				return getGroupProfitAndLoss();
			case SchedulePackage.GENERATED_CHARTER_OUT__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER:
				return getGroupProfitAndLossNoTimeCharter();
			case SchedulePackage.GENERATED_CHARTER_OUT__REVENUE:
				return getRevenue();
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
			case SchedulePackage.GENERATED_CHARTER_OUT__GROUP_PROFIT_AND_LOSS:
				setGroupProfitAndLoss((GroupProfitAndLoss)newValue);
				return;
			case SchedulePackage.GENERATED_CHARTER_OUT__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER:
				setGroupProfitAndLossNoTimeCharter((GroupProfitAndLoss)newValue);
				return;
			case SchedulePackage.GENERATED_CHARTER_OUT__REVENUE:
				setRevenue((Integer)newValue);
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
			case SchedulePackage.GENERATED_CHARTER_OUT__GROUP_PROFIT_AND_LOSS:
				setGroupProfitAndLoss((GroupProfitAndLoss)null);
				return;
			case SchedulePackage.GENERATED_CHARTER_OUT__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER:
				setGroupProfitAndLossNoTimeCharter((GroupProfitAndLoss)null);
				return;
			case SchedulePackage.GENERATED_CHARTER_OUT__REVENUE:
				setRevenue(REVENUE_EDEFAULT);
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
			case SchedulePackage.GENERATED_CHARTER_OUT__GROUP_PROFIT_AND_LOSS:
				return groupProfitAndLoss != null;
			case SchedulePackage.GENERATED_CHARTER_OUT__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER:
				return groupProfitAndLossNoTimeCharter != null;
			case SchedulePackage.GENERATED_CHARTER_OUT__REVENUE:
				return revenue != REVENUE_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == ProfitAndLossContainer.class) {
			switch (derivedFeatureID) {
				case SchedulePackage.GENERATED_CHARTER_OUT__GROUP_PROFIT_AND_LOSS: return SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS;
				case SchedulePackage.GENERATED_CHARTER_OUT__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER: return SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == ProfitAndLossContainer.class) {
			switch (baseFeatureID) {
				case SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS: return SchedulePackage.GENERATED_CHARTER_OUT__GROUP_PROFIT_AND_LOSS;
				case SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER: return SchedulePackage.GENERATED_CHARTER_OUT__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (revenue: ");
		result.append(revenue);
		result.append(')');
		return result.toString();
	}

	/**
	 * @generated NOT
	 */
	@Override
	public String name() {
		return "Charter Out";
	}

	/**
	 * @generated NOT
	 */
	@Override
	public String type() {
		return "Charter Out";
	}
	
} // end of GeneratedCharterOutImpl

// finish type fixing
