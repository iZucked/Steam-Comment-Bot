/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import com.mmxlabs.models.lng.schedule.PortVisitLateness;
import com.mmxlabs.models.lng.schedule.PortVisitLatenessType;
import com.mmxlabs.models.lng.schedule.SchedulePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Port Visit Lateness</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.PortVisitLatenessImpl#getType <em>Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.PortVisitLatenessImpl#getLatenessInHours <em>Lateness In Hours</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PortVisitLatenessImpl extends EObjectImpl implements PortVisitLateness {
	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final PortVisitLatenessType TYPE_EDEFAULT = PortVisitLatenessType.PROMPT;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected PortVisitLatenessType type = TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getLatenessInHours() <em>Lateness In Hours</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLatenessInHours()
	 * @generated
	 * @ordered
	 */
	protected static final int LATENESS_IN_HOURS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getLatenessInHours() <em>Lateness In Hours</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLatenessInHours()
	 * @generated
	 * @ordered
	 */
	protected int latenessInHours = LATENESS_IN_HOURS_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PortVisitLatenessImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.PORT_VISIT_LATENESS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PortVisitLatenessType getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(PortVisitLatenessType newType) {
		PortVisitLatenessType oldType = type;
		type = newType == null ? TYPE_EDEFAULT : newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.PORT_VISIT_LATENESS__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getLatenessInHours() {
		return latenessInHours;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLatenessInHours(int newLatenessInHours) {
		int oldLatenessInHours = latenessInHours;
		latenessInHours = newLatenessInHours;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.PORT_VISIT_LATENESS__LATENESS_IN_HOURS, oldLatenessInHours, latenessInHours));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchedulePackage.PORT_VISIT_LATENESS__TYPE:
				return getType();
			case SchedulePackage.PORT_VISIT_LATENESS__LATENESS_IN_HOURS:
				return getLatenessInHours();
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
			case SchedulePackage.PORT_VISIT_LATENESS__TYPE:
				setType((PortVisitLatenessType)newValue);
				return;
			case SchedulePackage.PORT_VISIT_LATENESS__LATENESS_IN_HOURS:
				setLatenessInHours((Integer)newValue);
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
			case SchedulePackage.PORT_VISIT_LATENESS__TYPE:
				setType(TYPE_EDEFAULT);
				return;
			case SchedulePackage.PORT_VISIT_LATENESS__LATENESS_IN_HOURS:
				setLatenessInHours(LATENESS_IN_HOURS_EDEFAULT);
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
			case SchedulePackage.PORT_VISIT_LATENESS__TYPE:
				return type != TYPE_EDEFAULT;
			case SchedulePackage.PORT_VISIT_LATENESS__LATENESS_IN_HOURS:
				return latenessInHours != LATENESS_IN_HOURS_EDEFAULT;
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
		result.append(" (type: ");
		result.append(type);
		result.append(", latenessInHours: ");
		result.append(latenessInHours);
		result.append(')');
		return result.toString();
	}

} //PortVisitLatenessImpl
