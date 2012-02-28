/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SchedulePackage;

import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;

import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.ScheduleModelImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.ScheduleModelImpl#getInitialSchedule <em>Initial Schedule</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.ScheduleModelImpl#getOptimisedSchedule <em>Optimised Schedule</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ScheduleModelImpl extends UUIDObjectImpl implements ScheduleModel {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

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
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SCHEDULE_MODEL__NAME, oldName, name));
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
			case SchedulePackage.SCHEDULE_MODEL__NAME:
				return getName();
			case SchedulePackage.SCHEDULE_MODEL__INITIAL_SCHEDULE:
				return getInitialSchedule();
			case SchedulePackage.SCHEDULE_MODEL__OPTIMISED_SCHEDULE:
				return getOptimisedSchedule();
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
			case SchedulePackage.SCHEDULE_MODEL__NAME:
				setName((String)newValue);
				return;
			case SchedulePackage.SCHEDULE_MODEL__INITIAL_SCHEDULE:
				setInitialSchedule((Schedule)newValue);
				return;
			case SchedulePackage.SCHEDULE_MODEL__OPTIMISED_SCHEDULE:
				setOptimisedSchedule((Schedule)newValue);
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
			case SchedulePackage.SCHEDULE_MODEL__NAME:
				setName(NAME_EDEFAULT);
				return;
			case SchedulePackage.SCHEDULE_MODEL__INITIAL_SCHEDULE:
				setInitialSchedule((Schedule)null);
				return;
			case SchedulePackage.SCHEDULE_MODEL__OPTIMISED_SCHEDULE:
				setOptimisedSchedule((Schedule)null);
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
			case SchedulePackage.SCHEDULE_MODEL__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case SchedulePackage.SCHEDULE_MODEL__INITIAL_SCHEDULE:
				return initialSchedule != null;
			case SchedulePackage.SCHEDULE_MODEL__OPTIMISED_SCHEDULE:
				return optimisedSchedule != null;
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
		if (baseClass == NamedObject.class) {
			switch (derivedFeatureID) {
				case SchedulePackage.SCHEDULE_MODEL__NAME: return MMXCorePackage.NAMED_OBJECT__NAME;
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
		if (baseClass == NamedObject.class) {
			switch (baseFeatureID) {
				case MMXCorePackage.NAMED_OBJECT__NAME: return SchedulePackage.SCHEDULE_MODEL__NAME;
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
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} // end of ScheduleModelImpl

// finish type fixing
