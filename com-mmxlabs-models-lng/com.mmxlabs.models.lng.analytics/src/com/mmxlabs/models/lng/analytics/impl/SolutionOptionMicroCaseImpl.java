/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.SolutionOptionMicroCase;

import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.ScheduleSpecification;
import com.mmxlabs.models.lng.cargo.VesselCharter;

import com.mmxlabs.models.lng.schedule.ScheduleModel;

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
 * An implementation of the model object '<em><b>Solution Option Micro Case</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SolutionOptionMicroCaseImpl#getScheduleSpecification <em>Schedule Specification</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SolutionOptionMicroCaseImpl#getScheduleModel <em>Schedule Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SolutionOptionMicroCaseImpl#getExtraVesselCharters <em>Extra Vessel Charters</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SolutionOptionMicroCaseImpl#getCharterInMarketOverrides <em>Charter In Market Overrides</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SolutionOptionMicroCaseImpl extends EObjectImpl implements SolutionOptionMicroCase {
	/**
	 * The cached value of the '{@link #getScheduleSpecification() <em>Schedule Specification</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScheduleSpecification()
	 * @generated
	 * @ordered
	 */
	protected ScheduleSpecification scheduleSpecification;

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
	 * The cached value of the '{@link #getExtraVesselCharters() <em>Extra Vessel Charters</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtraVesselCharters()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselCharter> extraVesselCharters;

	/**
	 * The cached value of the '{@link #getCharterInMarketOverrides() <em>Charter In Market Overrides</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterInMarketOverrides()
	 * @generated
	 * @ordered
	 */
	protected EList<CharterInMarketOverride> charterInMarketOverrides;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SolutionOptionMicroCaseImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.SOLUTION_OPTION_MICRO_CASE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ScheduleSpecification getScheduleSpecification() {
		return scheduleSpecification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetScheduleSpecification(ScheduleSpecification newScheduleSpecification, NotificationChain msgs) {
		ScheduleSpecification oldScheduleSpecification = scheduleSpecification;
		scheduleSpecification = newScheduleSpecification;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE__SCHEDULE_SPECIFICATION, oldScheduleSpecification, newScheduleSpecification);
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
	public void setScheduleSpecification(ScheduleSpecification newScheduleSpecification) {
		if (newScheduleSpecification != scheduleSpecification) {
			NotificationChain msgs = null;
			if (scheduleSpecification != null)
				msgs = ((InternalEObject)scheduleSpecification).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE__SCHEDULE_SPECIFICATION, null, msgs);
			if (newScheduleSpecification != null)
				msgs = ((InternalEObject)newScheduleSpecification).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE__SCHEDULE_SPECIFICATION, null, msgs);
			msgs = basicSetScheduleSpecification(newScheduleSpecification, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE__SCHEDULE_SPECIFICATION, newScheduleSpecification, newScheduleSpecification));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ScheduleModel getScheduleModel() {
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE__SCHEDULE_MODEL, oldScheduleModel, newScheduleModel);
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
	public void setScheduleModel(ScheduleModel newScheduleModel) {
		if (newScheduleModel != scheduleModel) {
			NotificationChain msgs = null;
			if (scheduleModel != null)
				msgs = ((InternalEObject)scheduleModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE__SCHEDULE_MODEL, null, msgs);
			if (newScheduleModel != null)
				msgs = ((InternalEObject)newScheduleModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE__SCHEDULE_MODEL, null, msgs);
			msgs = basicSetScheduleModel(newScheduleModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE__SCHEDULE_MODEL, newScheduleModel, newScheduleModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<VesselCharter> getExtraVesselCharters() {
		if (extraVesselCharters == null) {
			extraVesselCharters = new EObjectContainmentEList<VesselCharter>(VesselCharter.class, this, AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE__EXTRA_VESSEL_CHARTERS);
		}
		return extraVesselCharters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<CharterInMarketOverride> getCharterInMarketOverrides() {
		if (charterInMarketOverrides == null) {
			charterInMarketOverrides = new EObjectContainmentEList<CharterInMarketOverride>(CharterInMarketOverride.class, this, AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE__CHARTER_IN_MARKET_OVERRIDES);
		}
		return charterInMarketOverrides;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE__SCHEDULE_SPECIFICATION:
				return basicSetScheduleSpecification(null, msgs);
			case AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE__SCHEDULE_MODEL:
				return basicSetScheduleModel(null, msgs);
			case AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE__EXTRA_VESSEL_CHARTERS:
				return ((InternalEList<?>)getExtraVesselCharters()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE__CHARTER_IN_MARKET_OVERRIDES:
				return ((InternalEList<?>)getCharterInMarketOverrides()).basicRemove(otherEnd, msgs);
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
			case AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE__SCHEDULE_SPECIFICATION:
				return getScheduleSpecification();
			case AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE__SCHEDULE_MODEL:
				return getScheduleModel();
			case AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE__EXTRA_VESSEL_CHARTERS:
				return getExtraVesselCharters();
			case AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE__CHARTER_IN_MARKET_OVERRIDES:
				return getCharterInMarketOverrides();
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
			case AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE__SCHEDULE_SPECIFICATION:
				setScheduleSpecification((ScheduleSpecification)newValue);
				return;
			case AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE__SCHEDULE_MODEL:
				setScheduleModel((ScheduleModel)newValue);
				return;
			case AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE__EXTRA_VESSEL_CHARTERS:
				getExtraVesselCharters().clear();
				getExtraVesselCharters().addAll((Collection<? extends VesselCharter>)newValue);
				return;
			case AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE__CHARTER_IN_MARKET_OVERRIDES:
				getCharterInMarketOverrides().clear();
				getCharterInMarketOverrides().addAll((Collection<? extends CharterInMarketOverride>)newValue);
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
			case AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE__SCHEDULE_SPECIFICATION:
				setScheduleSpecification((ScheduleSpecification)null);
				return;
			case AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE__SCHEDULE_MODEL:
				setScheduleModel((ScheduleModel)null);
				return;
			case AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE__EXTRA_VESSEL_CHARTERS:
				getExtraVesselCharters().clear();
				return;
			case AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE__CHARTER_IN_MARKET_OVERRIDES:
				getCharterInMarketOverrides().clear();
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
			case AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE__SCHEDULE_SPECIFICATION:
				return scheduleSpecification != null;
			case AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE__SCHEDULE_MODEL:
				return scheduleModel != null;
			case AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE__EXTRA_VESSEL_CHARTERS:
				return extraVesselCharters != null && !extraVesselCharters.isEmpty();
			case AnalyticsPackage.SOLUTION_OPTION_MICRO_CASE__CHARTER_IN_MARKET_OVERRIDES:
				return charterInMarketOverrides != null && !charterInMarketOverrides.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //SolutionOptionMicroCaseImpl
