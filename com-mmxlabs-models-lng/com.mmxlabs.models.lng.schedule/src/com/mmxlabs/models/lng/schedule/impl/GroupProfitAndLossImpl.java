/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule.impl;

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

import com.mmxlabs.models.lng.schedule.EntityProfitAndLoss;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.SchedulePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Group Profit And Loss</b></em>'.
 * @since 4.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.GroupProfitAndLossImpl#getProfitAndLoss <em>Profit And Loss</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.GroupProfitAndLossImpl#getEntityProfitAndLosses <em>Entity Profit And Losses</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GroupProfitAndLossImpl extends EObjectImpl implements GroupProfitAndLoss {
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
	 * The cached value of the '{@link #getEntityProfitAndLosses() <em>Entity Profit And Losses</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntityProfitAndLosses()
	 * @generated
	 * @ordered
	 */
	protected EList<EntityProfitAndLoss> entityProfitAndLosses;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GroupProfitAndLossImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.GROUP_PROFIT_AND_LOSS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getProfitAndLoss() {
		return profitAndLoss;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProfitAndLoss(long newProfitAndLoss) {
		long oldProfitAndLoss = profitAndLoss;
		profitAndLoss = newProfitAndLoss;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.GROUP_PROFIT_AND_LOSS__PROFIT_AND_LOSS, oldProfitAndLoss, profitAndLoss));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EntityProfitAndLoss> getEntityProfitAndLosses() {
		if (entityProfitAndLosses == null) {
			entityProfitAndLosses = new EObjectContainmentEList<EntityProfitAndLoss>(EntityProfitAndLoss.class, this, SchedulePackage.GROUP_PROFIT_AND_LOSS__ENTITY_PROFIT_AND_LOSSES);
		}
		return entityProfitAndLosses;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.GROUP_PROFIT_AND_LOSS__ENTITY_PROFIT_AND_LOSSES:
				return ((InternalEList<?>)getEntityProfitAndLosses()).basicRemove(otherEnd, msgs);
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
			case SchedulePackage.GROUP_PROFIT_AND_LOSS__PROFIT_AND_LOSS:
				return getProfitAndLoss();
			case SchedulePackage.GROUP_PROFIT_AND_LOSS__ENTITY_PROFIT_AND_LOSSES:
				return getEntityProfitAndLosses();
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
			case SchedulePackage.GROUP_PROFIT_AND_LOSS__PROFIT_AND_LOSS:
				setProfitAndLoss((Long)newValue);
				return;
			case SchedulePackage.GROUP_PROFIT_AND_LOSS__ENTITY_PROFIT_AND_LOSSES:
				getEntityProfitAndLosses().clear();
				getEntityProfitAndLosses().addAll((Collection<? extends EntityProfitAndLoss>)newValue);
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
			case SchedulePackage.GROUP_PROFIT_AND_LOSS__PROFIT_AND_LOSS:
				setProfitAndLoss(PROFIT_AND_LOSS_EDEFAULT);
				return;
			case SchedulePackage.GROUP_PROFIT_AND_LOSS__ENTITY_PROFIT_AND_LOSSES:
				getEntityProfitAndLosses().clear();
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
			case SchedulePackage.GROUP_PROFIT_AND_LOSS__PROFIT_AND_LOSS:
				return profitAndLoss != PROFIT_AND_LOSS_EDEFAULT;
			case SchedulePackage.GROUP_PROFIT_AND_LOSS__ENTITY_PROFIT_AND_LOSSES:
				return entityProfitAndLosses != null && !entityProfitAndLosses.isEmpty();
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

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (profitAndLoss: ");
		result.append(profitAndLoss);
		result.append(')');
		return result.toString();
	}

} //GroupProfitAndLossImpl
