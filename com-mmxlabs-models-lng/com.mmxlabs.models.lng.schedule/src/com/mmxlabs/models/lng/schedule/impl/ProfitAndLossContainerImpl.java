/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.SchedulePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Profit And Loss Container</b></em>'.
 * @since 4.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.ProfitAndLossContainerImpl#getGroupProfitAndLoss <em>Group Profit And Loss</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.ProfitAndLossContainerImpl#getGroupProfitAndLossNoTimeCharter <em>Group Profit And Loss No Time Charter</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ProfitAndLossContainerImpl extends EObjectImpl implements ProfitAndLossContainer {
	/**
	 * The cached value of the '{@link #getGroupProfitAndLoss() <em>Group Profit And Loss</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroupProfitAndLoss()
	 * @generated
	 * @ordered
	 */
	protected GroupProfitAndLoss groupProfitAndLoss;

	/**
	 * The cached value of the '{@link #getGroupProfitAndLossNoTimeCharter() <em>Group Profit And Loss No Time Charter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroupProfitAndLossNoTimeCharter()
	 * @generated
	 * @ordered
	 */
	protected GroupProfitAndLoss groupProfitAndLossNoTimeCharter;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ProfitAndLossContainerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.PROFIT_AND_LOSS_CONTAINER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GroupProfitAndLoss getGroupProfitAndLoss() {
		return groupProfitAndLoss;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetGroupProfitAndLoss(GroupProfitAndLoss newGroupProfitAndLoss, NotificationChain msgs) {
		GroupProfitAndLoss oldGroupProfitAndLoss = groupProfitAndLoss;
		groupProfitAndLoss = newGroupProfitAndLoss;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS, oldGroupProfitAndLoss, newGroupProfitAndLoss);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGroupProfitAndLoss(GroupProfitAndLoss newGroupProfitAndLoss) {
		if (newGroupProfitAndLoss != groupProfitAndLoss) {
			NotificationChain msgs = null;
			if (groupProfitAndLoss != null)
				msgs = ((InternalEObject)groupProfitAndLoss).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS, null, msgs);
			if (newGroupProfitAndLoss != null)
				msgs = ((InternalEObject)newGroupProfitAndLoss).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS, null, msgs);
			msgs = basicSetGroupProfitAndLoss(newGroupProfitAndLoss, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS, newGroupProfitAndLoss, newGroupProfitAndLoss));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GroupProfitAndLoss getGroupProfitAndLossNoTimeCharter() {
		return groupProfitAndLossNoTimeCharter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetGroupProfitAndLossNoTimeCharter(GroupProfitAndLoss newGroupProfitAndLossNoTimeCharter, NotificationChain msgs) {
		GroupProfitAndLoss oldGroupProfitAndLossNoTimeCharter = groupProfitAndLossNoTimeCharter;
		groupProfitAndLossNoTimeCharter = newGroupProfitAndLossNoTimeCharter;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER, oldGroupProfitAndLossNoTimeCharter, newGroupProfitAndLossNoTimeCharter);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGroupProfitAndLossNoTimeCharter(GroupProfitAndLoss newGroupProfitAndLossNoTimeCharter) {
		if (newGroupProfitAndLossNoTimeCharter != groupProfitAndLossNoTimeCharter) {
			NotificationChain msgs = null;
			if (groupProfitAndLossNoTimeCharter != null)
				msgs = ((InternalEObject)groupProfitAndLossNoTimeCharter).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER, null, msgs);
			if (newGroupProfitAndLossNoTimeCharter != null)
				msgs = ((InternalEObject)newGroupProfitAndLossNoTimeCharter).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER, null, msgs);
			msgs = basicSetGroupProfitAndLossNoTimeCharter(newGroupProfitAndLossNoTimeCharter, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER, newGroupProfitAndLossNoTimeCharter, newGroupProfitAndLossNoTimeCharter));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS:
				return basicSetGroupProfitAndLoss(null, msgs);
			case SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER:
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
			case SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS:
				return getGroupProfitAndLoss();
			case SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER:
				return getGroupProfitAndLossNoTimeCharter();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS:
				setGroupProfitAndLoss((GroupProfitAndLoss)newValue);
				return;
			case SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER:
				setGroupProfitAndLossNoTimeCharter((GroupProfitAndLoss)newValue);
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
			case SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS:
				setGroupProfitAndLoss((GroupProfitAndLoss)null);
				return;
			case SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER:
				setGroupProfitAndLossNoTimeCharter((GroupProfitAndLoss)null);
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
			case SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS:
				return groupProfitAndLoss != null;
			case SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER:
				return groupProfitAndLossNoTimeCharter != null;
		}
		return super.eIsSet(featureID);
	}

} //ProfitAndLossContainerImpl
