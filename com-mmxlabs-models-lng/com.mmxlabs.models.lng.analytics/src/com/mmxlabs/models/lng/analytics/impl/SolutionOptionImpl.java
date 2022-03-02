/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.ChangeDescription;
import com.mmxlabs.models.lng.analytics.SolutionOption;

import com.mmxlabs.models.lng.cargo.ScheduleSpecification;
import com.mmxlabs.models.lng.schedule.ScheduleModel;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Solution Option</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SolutionOptionImpl#getChangeDescription <em>Change Description</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SolutionOptionImpl#getScheduleSpecification <em>Schedule Specification</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SolutionOptionImpl#getScheduleModel <em>Schedule Model</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SolutionOptionImpl extends EObjectImpl implements SolutionOption {
	/**
	 * The cached value of the '{@link #getChangeDescription() <em>Change Description</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChangeDescription()
	 * @generated
	 * @ordered
	 */
	protected ChangeDescription changeDescription;
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SolutionOptionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.SOLUTION_OPTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ChangeDescription getChangeDescription() {
		return changeDescription;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetChangeDescription(ChangeDescription newChangeDescription, NotificationChain msgs) {
		ChangeDescription oldChangeDescription = changeDescription;
		changeDescription = newChangeDescription;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SOLUTION_OPTION__CHANGE_DESCRIPTION, oldChangeDescription, newChangeDescription);
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
	public void setChangeDescription(ChangeDescription newChangeDescription) {
		if (newChangeDescription != changeDescription) {
			NotificationChain msgs = null;
			if (changeDescription != null)
				msgs = ((InternalEObject)changeDescription).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SOLUTION_OPTION__CHANGE_DESCRIPTION, null, msgs);
			if (newChangeDescription != null)
				msgs = ((InternalEObject)newChangeDescription).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SOLUTION_OPTION__CHANGE_DESCRIPTION, null, msgs);
			msgs = basicSetChangeDescription(newChangeDescription, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SOLUTION_OPTION__CHANGE_DESCRIPTION, newChangeDescription, newChangeDescription));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SOLUTION_OPTION__SCHEDULE_SPECIFICATION, oldScheduleSpecification, newScheduleSpecification);
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
				msgs = ((InternalEObject)scheduleSpecification).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SOLUTION_OPTION__SCHEDULE_SPECIFICATION, null, msgs);
			if (newScheduleSpecification != null)
				msgs = ((InternalEObject)newScheduleSpecification).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SOLUTION_OPTION__SCHEDULE_SPECIFICATION, null, msgs);
			msgs = basicSetScheduleSpecification(newScheduleSpecification, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SOLUTION_OPTION__SCHEDULE_SPECIFICATION, newScheduleSpecification, newScheduleSpecification));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SOLUTION_OPTION__SCHEDULE_MODEL, oldScheduleModel, newScheduleModel);
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
				msgs = ((InternalEObject)scheduleModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SOLUTION_OPTION__SCHEDULE_MODEL, null, msgs);
			if (newScheduleModel != null)
				msgs = ((InternalEObject)newScheduleModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.SOLUTION_OPTION__SCHEDULE_MODEL, null, msgs);
			msgs = basicSetScheduleModel(newScheduleModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.SOLUTION_OPTION__SCHEDULE_MODEL, newScheduleModel, newScheduleModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.SOLUTION_OPTION__CHANGE_DESCRIPTION:
				return basicSetChangeDescription(null, msgs);
			case AnalyticsPackage.SOLUTION_OPTION__SCHEDULE_SPECIFICATION:
				return basicSetScheduleSpecification(null, msgs);
			case AnalyticsPackage.SOLUTION_OPTION__SCHEDULE_MODEL:
				return basicSetScheduleModel(null, msgs);
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
			case AnalyticsPackage.SOLUTION_OPTION__CHANGE_DESCRIPTION:
				return getChangeDescription();
			case AnalyticsPackage.SOLUTION_OPTION__SCHEDULE_SPECIFICATION:
				return getScheduleSpecification();
			case AnalyticsPackage.SOLUTION_OPTION__SCHEDULE_MODEL:
				return getScheduleModel();
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
			case AnalyticsPackage.SOLUTION_OPTION__CHANGE_DESCRIPTION:
				setChangeDescription((ChangeDescription)newValue);
				return;
			case AnalyticsPackage.SOLUTION_OPTION__SCHEDULE_SPECIFICATION:
				setScheduleSpecification((ScheduleSpecification)newValue);
				return;
			case AnalyticsPackage.SOLUTION_OPTION__SCHEDULE_MODEL:
				setScheduleModel((ScheduleModel)newValue);
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
			case AnalyticsPackage.SOLUTION_OPTION__CHANGE_DESCRIPTION:
				setChangeDescription((ChangeDescription)null);
				return;
			case AnalyticsPackage.SOLUTION_OPTION__SCHEDULE_SPECIFICATION:
				setScheduleSpecification((ScheduleSpecification)null);
				return;
			case AnalyticsPackage.SOLUTION_OPTION__SCHEDULE_MODEL:
				setScheduleModel((ScheduleModel)null);
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
			case AnalyticsPackage.SOLUTION_OPTION__CHANGE_DESCRIPTION:
				return changeDescription != null;
			case AnalyticsPackage.SOLUTION_OPTION__SCHEDULE_SPECIFICATION:
				return scheduleSpecification != null;
			case AnalyticsPackage.SOLUTION_OPTION__SCHEDULE_MODEL:
				return scheduleModel != null;
		}
		return super.eIsSet(featureID);
	}

} //SolutionOptionImpl
