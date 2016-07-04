/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.schedule.model.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage;
import com.mmxlabs.lingo.reports.views.schedule.model.UserGroup;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>User Group</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.UserGroupImpl#getComment <em>Comment</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.UserGroupImpl#getGroups <em>Groups</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.UserGroupImpl#getDelta <em>Delta</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UserGroupImpl extends MinimalEObjectImpl.Container implements UserGroup {
	/**
	 * The default value of the '{@link #getComment() <em>Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComment()
	 * @generated
	 * @ordered
	 */
	protected static final String COMMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getComment() <em>Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComment()
	 * @generated
	 * @ordered
	 */
	protected String comment = COMMENT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getGroups() <em>Groups</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroups()
	 * @generated
	 * @ordered
	 */
	protected EList<CycleGroup> groups;
 

	/**
	 * The default value of the '{@link #getDelta() <em>Delta</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDelta()
	 * @generated
	 * @ordered
	 */
	protected static final int DELTA_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDelta() <em>Delta</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDelta()
	 * @generated
	 * @ordered
	 */
	protected int delta = DELTA_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UserGroupImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ScheduleReportPackage.Literals.USER_GROUP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getComment() {
		return comment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setComment(String newComment) {
		String oldComment = comment;
		comment = newComment;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.USER_GROUP__COMMENT, oldComment, comment));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<CycleGroup> getGroups() {
		if (groups == null) {
			groups = new EObjectContainmentWithInverseEList<CycleGroup>(CycleGroup.class, this, ScheduleReportPackage.USER_GROUP__GROUPS, ScheduleReportPackage.CYCLE_GROUP__USER_GROUP);
		}
		return groups;
	}

	 

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getDelta() {
		return delta;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDelta(int newDelta) {
		int oldDelta = delta;
		delta = newDelta;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.USER_GROUP__DELTA, oldDelta, delta));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ScheduleReportPackage.USER_GROUP__GROUPS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getGroups()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ScheduleReportPackage.USER_GROUP__GROUPS:
				return ((InternalEList<?>)getGroups()).basicRemove(otherEnd, msgs);
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
			case ScheduleReportPackage.USER_GROUP__COMMENT:
				return getComment();
			case ScheduleReportPackage.USER_GROUP__GROUPS:
				return getGroups();
			case ScheduleReportPackage.USER_GROUP__DELTA:
				return getDelta();
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
			case ScheduleReportPackage.USER_GROUP__COMMENT:
				setComment((String)newValue);
				return;
			case ScheduleReportPackage.USER_GROUP__GROUPS:
				getGroups().clear();
				getGroups().addAll((Collection<? extends CycleGroup>)newValue);
				return;
			case ScheduleReportPackage.USER_GROUP__DELTA:
				setDelta((Integer)newValue);
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
			case ScheduleReportPackage.USER_GROUP__COMMENT:
				setComment(COMMENT_EDEFAULT);
				return;
			case ScheduleReportPackage.USER_GROUP__GROUPS:
				getGroups().clear();
				return;
			case ScheduleReportPackage.USER_GROUP__DELTA:
				setDelta(DELTA_EDEFAULT);
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
			case ScheduleReportPackage.USER_GROUP__COMMENT:
				return COMMENT_EDEFAULT == null ? comment != null : !COMMENT_EDEFAULT.equals(comment);
			case ScheduleReportPackage.USER_GROUP__GROUPS:
				return groups != null && !groups.isEmpty();
			case ScheduleReportPackage.USER_GROUP__DELTA:
				return delta != DELTA_EDEFAULT;
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
		result.append(" (comment: ");
		result.append(comment);
		result.append(", delta: ");
		result.append(delta);
		result.append(')');
		return result.toString();
	}

} //UserGroupImpl
