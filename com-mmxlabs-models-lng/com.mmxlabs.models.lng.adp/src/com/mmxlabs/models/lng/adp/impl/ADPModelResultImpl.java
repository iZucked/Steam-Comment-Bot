/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPModelResult;
import com.mmxlabs.models.lng.adp.ADPPackage;

import com.mmxlabs.models.lng.cargo.Slot;

import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
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
 * An implementation of the model object '<em><b>Model Result</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ADPModelResultImpl#getExtraSlots <em>Extra Slots</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ADPModelResultImpl#getScheduleModel <em>Schedule Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ADPModelResultImpl#getExtraSpotCharterMarkets <em>Extra Spot Charter Markets</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ADPModelResultImpl extends EObjectImpl implements ADPModelResult {
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
	 * The cached value of the '{@link #getScheduleModel() <em>Schedule Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScheduleModel()
	 * @generated
	 * @ordered
	 */
	protected ScheduleModel scheduleModel;

	/**
	 * The cached value of the '{@link #getExtraSpotCharterMarkets() <em>Extra Spot Charter Markets</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtraSpotCharterMarkets()
	 * @generated
	 * @ordered
	 */
	protected EList<CharterInMarket> extraSpotCharterMarkets;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ADPModelResultImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.ADP_MODEL_RESULT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Slot> getExtraSlots() {
		if (extraSlots == null) {
			extraSlots = new EObjectContainmentEList.Resolving<Slot>(Slot.class, this, ADPPackage.ADP_MODEL_RESULT__EXTRA_SLOTS);
		}
		return extraSlots;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScheduleModel getScheduleModel() {
		if (scheduleModel != null && scheduleModel.eIsProxy()) {
			InternalEObject oldScheduleModel = (InternalEObject)scheduleModel;
			scheduleModel = (ScheduleModel)eResolveProxy(oldScheduleModel);
			if (scheduleModel != oldScheduleModel) {
				InternalEObject newScheduleModel = (InternalEObject)scheduleModel;
				NotificationChain msgs = oldScheduleModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ADPPackage.ADP_MODEL_RESULT__SCHEDULE_MODEL, null, null);
				if (newScheduleModel.eInternalContainer() == null) {
					msgs = newScheduleModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ADPPackage.ADP_MODEL_RESULT__SCHEDULE_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.ADP_MODEL_RESULT__SCHEDULE_MODEL, oldScheduleModel, scheduleModel));
			}
		}
		return scheduleModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScheduleModel basicGetScheduleModel() {
		return scheduleModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetScheduleModel(ScheduleModel newScheduleModel, NotificationChain msgs) {
		ScheduleModel oldScheduleModel = scheduleModel;
		scheduleModel = newScheduleModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ADPPackage.ADP_MODEL_RESULT__SCHEDULE_MODEL, oldScheduleModel, newScheduleModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setScheduleModel(ScheduleModel newScheduleModel) {
		if (newScheduleModel != scheduleModel) {
			NotificationChain msgs = null;
			if (scheduleModel != null)
				msgs = ((InternalEObject)scheduleModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ADPPackage.ADP_MODEL_RESULT__SCHEDULE_MODEL, null, msgs);
			if (newScheduleModel != null)
				msgs = ((InternalEObject)newScheduleModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ADPPackage.ADP_MODEL_RESULT__SCHEDULE_MODEL, null, msgs);
			msgs = basicSetScheduleModel(newScheduleModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.ADP_MODEL_RESULT__SCHEDULE_MODEL, newScheduleModel, newScheduleModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<CharterInMarket> getExtraSpotCharterMarkets() {
		if (extraSpotCharterMarkets == null) {
			extraSpotCharterMarkets = new EObjectContainmentEList.Resolving<CharterInMarket>(CharterInMarket.class, this, ADPPackage.ADP_MODEL_RESULT__EXTRA_SPOT_CHARTER_MARKETS);
		}
		return extraSpotCharterMarkets;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ADPPackage.ADP_MODEL_RESULT__EXTRA_SLOTS:
				return ((InternalEList<?>)getExtraSlots()).basicRemove(otherEnd, msgs);
			case ADPPackage.ADP_MODEL_RESULT__SCHEDULE_MODEL:
				return basicSetScheduleModel(null, msgs);
			case ADPPackage.ADP_MODEL_RESULT__EXTRA_SPOT_CHARTER_MARKETS:
				return ((InternalEList<?>)getExtraSpotCharterMarkets()).basicRemove(otherEnd, msgs);
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
			case ADPPackage.ADP_MODEL_RESULT__EXTRA_SLOTS:
				return getExtraSlots();
			case ADPPackage.ADP_MODEL_RESULT__SCHEDULE_MODEL:
				if (resolve) return getScheduleModel();
				return basicGetScheduleModel();
			case ADPPackage.ADP_MODEL_RESULT__EXTRA_SPOT_CHARTER_MARKETS:
				return getExtraSpotCharterMarkets();
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
			case ADPPackage.ADP_MODEL_RESULT__EXTRA_SLOTS:
				getExtraSlots().clear();
				getExtraSlots().addAll((Collection<? extends Slot>)newValue);
				return;
			case ADPPackage.ADP_MODEL_RESULT__SCHEDULE_MODEL:
				setScheduleModel((ScheduleModel)newValue);
				return;
			case ADPPackage.ADP_MODEL_RESULT__EXTRA_SPOT_CHARTER_MARKETS:
				getExtraSpotCharterMarkets().clear();
				getExtraSpotCharterMarkets().addAll((Collection<? extends CharterInMarket>)newValue);
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
			case ADPPackage.ADP_MODEL_RESULT__EXTRA_SLOTS:
				getExtraSlots().clear();
				return;
			case ADPPackage.ADP_MODEL_RESULT__SCHEDULE_MODEL:
				setScheduleModel((ScheduleModel)null);
				return;
			case ADPPackage.ADP_MODEL_RESULT__EXTRA_SPOT_CHARTER_MARKETS:
				getExtraSpotCharterMarkets().clear();
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
			case ADPPackage.ADP_MODEL_RESULT__EXTRA_SLOTS:
				return extraSlots != null && !extraSlots.isEmpty();
			case ADPPackage.ADP_MODEL_RESULT__SCHEDULE_MODEL:
				return scheduleModel != null;
			case ADPPackage.ADP_MODEL_RESULT__EXTRA_SPOT_CHARTER_MARKETS:
				return extraSpotCharterMarkets != null && !extraSpotCharterMarkets.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ADPModelResultImpl
