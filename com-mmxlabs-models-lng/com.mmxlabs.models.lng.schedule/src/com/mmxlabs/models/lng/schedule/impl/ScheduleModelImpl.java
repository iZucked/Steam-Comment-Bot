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

import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.ScheduleModelImpl#getInitialSchedule <em>Initial Schedule</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.ScheduleModelImpl#getOptimisedSchedule <em>Optimised Schedule</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.ScheduleModelImpl#isDirty <em>Dirty</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ScheduleModelImpl extends UUIDObjectImpl implements ScheduleModel {
	/**
	 * The cached value of the '{@link #getInitialSchedule() <em>Initial Schedule</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInitialSchedule()
	 * @generated
	 * @ordered
	 */
	protected Schedule initialSchedule;

	/**
	 * The cached value of the '{@link #getOptimisedSchedule() <em>Optimised Schedule</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOptimisedSchedule()
	 * @generated
	 * @ordered
	 */
	protected Schedule optimisedSchedule;

	/**
	 * The default value of the '{@link #isDirty() <em>Dirty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDirty()
	 * @generated
	 * @ordered
	 */
	protected static final boolean DIRTY_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isDirty() <em>Dirty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDirty()
	 * @generated
	 * @ordered
	 */
	protected boolean dirty = DIRTY_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ScheduleModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.SCHEDULE_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Schedule getInitialSchedule() {
		return initialSchedule;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetInitialSchedule(Schedule newInitialSchedule, NotificationChain msgs) {
		Schedule oldInitialSchedule = initialSchedule;
		initialSchedule = newInitialSchedule;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchedulePackage.SCHEDULE_MODEL__INITIAL_SCHEDULE, oldInitialSchedule, newInitialSchedule);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInitialSchedule(Schedule newInitialSchedule) {
		if (newInitialSchedule != initialSchedule) {
			NotificationChain msgs = null;
			if (initialSchedule != null)
				msgs = ((InternalEObject)initialSchedule).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.SCHEDULE_MODEL__INITIAL_SCHEDULE, null, msgs);
			if (newInitialSchedule != null)
				msgs = ((InternalEObject)newInitialSchedule).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.SCHEDULE_MODEL__INITIAL_SCHEDULE, null, msgs);
			msgs = basicSetInitialSchedule(newInitialSchedule, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SCHEDULE_MODEL__INITIAL_SCHEDULE, newInitialSchedule, newInitialSchedule));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Schedule getOptimisedSchedule() {
		return optimisedSchedule;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOptimisedSchedule(Schedule newOptimisedSchedule, NotificationChain msgs) {
		Schedule oldOptimisedSchedule = optimisedSchedule;
		optimisedSchedule = newOptimisedSchedule;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchedulePackage.SCHEDULE_MODEL__OPTIMISED_SCHEDULE, oldOptimisedSchedule, newOptimisedSchedule);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOptimisedSchedule(Schedule newOptimisedSchedule) {
		if (newOptimisedSchedule != optimisedSchedule) {
			NotificationChain msgs = null;
			if (optimisedSchedule != null)
				msgs = ((InternalEObject)optimisedSchedule).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.SCHEDULE_MODEL__OPTIMISED_SCHEDULE, null, msgs);
			if (newOptimisedSchedule != null)
				msgs = ((InternalEObject)newOptimisedSchedule).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.SCHEDULE_MODEL__OPTIMISED_SCHEDULE, null, msgs);
			msgs = basicSetOptimisedSchedule(newOptimisedSchedule, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SCHEDULE_MODEL__OPTIMISED_SCHEDULE, newOptimisedSchedule, newOptimisedSchedule));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDirty(boolean newDirty) {
		boolean oldDirty = dirty;
		dirty = newDirty;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SCHEDULE_MODEL__DIRTY, oldDirty, dirty));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.SCHEDULE_MODEL__INITIAL_SCHEDULE:
				return basicSetInitialSchedule(null, msgs);
			case SchedulePackage.SCHEDULE_MODEL__OPTIMISED_SCHEDULE:
				return basicSetOptimisedSchedule(null, msgs);
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
			case SchedulePackage.SCHEDULE_MODEL__INITIAL_SCHEDULE:
				return getInitialSchedule();
			case SchedulePackage.SCHEDULE_MODEL__OPTIMISED_SCHEDULE:
				return getOptimisedSchedule();
			case SchedulePackage.SCHEDULE_MODEL__DIRTY:
				return isDirty();
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
			case SchedulePackage.SCHEDULE_MODEL__INITIAL_SCHEDULE:
				setInitialSchedule((Schedule)newValue);
				return;
			case SchedulePackage.SCHEDULE_MODEL__OPTIMISED_SCHEDULE:
				setOptimisedSchedule((Schedule)newValue);
				return;
			case SchedulePackage.SCHEDULE_MODEL__DIRTY:
				setDirty((Boolean)newValue);
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
			case SchedulePackage.SCHEDULE_MODEL__INITIAL_SCHEDULE:
				setInitialSchedule((Schedule)null);
				return;
			case SchedulePackage.SCHEDULE_MODEL__OPTIMISED_SCHEDULE:
				setOptimisedSchedule((Schedule)null);
				return;
			case SchedulePackage.SCHEDULE_MODEL__DIRTY:
				setDirty(DIRTY_EDEFAULT);
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
			case SchedulePackage.SCHEDULE_MODEL__INITIAL_SCHEDULE:
				return initialSchedule != null;
			case SchedulePackage.SCHEDULE_MODEL__OPTIMISED_SCHEDULE:
				return optimisedSchedule != null;
			case SchedulePackage.SCHEDULE_MODEL__DIRTY:
				return dirty != DIRTY_EDEFAULT;
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
		result.append(" (dirty: ");
		result.append(dirty);
		result.append(')');
		return result.toString();
	}

} // end of ScheduleModelImpl

// finish type fixing
