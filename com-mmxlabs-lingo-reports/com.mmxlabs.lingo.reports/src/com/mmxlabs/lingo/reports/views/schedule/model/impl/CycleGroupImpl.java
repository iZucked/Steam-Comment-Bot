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
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.lingo.reports.views.schedule.model.ChangeType;
import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage;
import com.mmxlabs.lingo.reports.views.schedule.model.UserGroup;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cycle Group</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.CycleGroupImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.CycleGroupImpl#getRows <em>Rows</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.CycleGroupImpl#getIndex <em>Index</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.CycleGroupImpl#getUserGroup <em>User Group</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.CycleGroupImpl#getDelta <em>Delta</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.CycleGroupImpl#getChangeType <em>Change Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CycleGroupImpl extends MinimalEObjectImpl.Container implements CycleGroup {
	/**
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected String description = DESCRIPTION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getRows() <em>Rows</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRows()
	 * @generated
	 * @ordered
	 */
	protected EList<Row> rows;

	/**
	 * The default value of the '{@link #getIndex() <em>Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndex()
	 * @generated
	 * @ordered
	 */
	protected static final int INDEX_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getIndex() <em>Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndex()
	 * @generated
	 * @ordered
	 */
	protected int index = INDEX_EDEFAULT;

	/**
	 * This is true if the Index attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean indexESet;

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
	 * The default value of the '{@link #getChangeType() <em>Change Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChangeType()
	 * @generated
	 * @ordered
	 */
	protected static final ChangeType CHANGE_TYPE_EDEFAULT = ChangeType.PNL;

	/**
	 * The cached value of the '{@link #getChangeType() <em>Change Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChangeType()
	 * @generated
	 * @ordered
	 */
	protected ChangeType changeType = CHANGE_TYPE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CycleGroupImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ScheduleReportPackage.Literals.CYCLE_GROUP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.CYCLE_GROUP__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Row> getRows() {
		if (rows == null) {
			rows = new EObjectWithInverseResolvingEList<Row>(Row.class, this, ScheduleReportPackage.CYCLE_GROUP__ROWS, ScheduleReportPackage.ROW__CYCLE_GROUP);
		}
		return rows;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getIndex() {
		return index;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setIndex(int newIndex) {
		int oldIndex = index;
		index = newIndex;
		boolean oldIndexESet = indexESet;
		indexESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.CYCLE_GROUP__INDEX, oldIndex, index, !oldIndexESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetIndex() {
		int oldIndex = index;
		boolean oldIndexESet = indexESet;
		index = INDEX_EDEFAULT;
		indexESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ScheduleReportPackage.CYCLE_GROUP__INDEX, oldIndex, INDEX_EDEFAULT, oldIndexESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetIndex() {
		return indexESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public UserGroup getUserGroup() {
		if (eContainerFeatureID() != ScheduleReportPackage.CYCLE_GROUP__USER_GROUP) return null;
		return (UserGroup)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetUserGroup(UserGroup newUserGroup, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newUserGroup, ScheduleReportPackage.CYCLE_GROUP__USER_GROUP, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setUserGroup(UserGroup newUserGroup) {
		if (newUserGroup != eInternalContainer() || (eContainerFeatureID() != ScheduleReportPackage.CYCLE_GROUP__USER_GROUP && newUserGroup != null)) {
			if (EcoreUtil.isAncestor(this, newUserGroup))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newUserGroup != null)
				msgs = ((InternalEObject)newUserGroup).eInverseAdd(this, ScheduleReportPackage.USER_GROUP__GROUPS, UserGroup.class, msgs);
			msgs = basicSetUserGroup(newUserGroup, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.CYCLE_GROUP__USER_GROUP, newUserGroup, newUserGroup));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.CYCLE_GROUP__DELTA, oldDelta, delta));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeType getChangeType() {
		return changeType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setChangeType(ChangeType newChangeType) {
		ChangeType oldChangeType = changeType;
		changeType = newChangeType == null ? CHANGE_TYPE_EDEFAULT : newChangeType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.CYCLE_GROUP__CHANGE_TYPE, oldChangeType, changeType));
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
			case ScheduleReportPackage.CYCLE_GROUP__ROWS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getRows()).basicAdd(otherEnd, msgs);
			case ScheduleReportPackage.CYCLE_GROUP__USER_GROUP:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetUserGroup((UserGroup)otherEnd, msgs);
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
			case ScheduleReportPackage.CYCLE_GROUP__ROWS:
				return ((InternalEList<?>)getRows()).basicRemove(otherEnd, msgs);
			case ScheduleReportPackage.CYCLE_GROUP__USER_GROUP:
				return basicSetUserGroup(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case ScheduleReportPackage.CYCLE_GROUP__USER_GROUP:
				return eInternalContainer().eInverseRemove(this, ScheduleReportPackage.USER_GROUP__GROUPS, UserGroup.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ScheduleReportPackage.CYCLE_GROUP__DESCRIPTION:
				return getDescription();
			case ScheduleReportPackage.CYCLE_GROUP__ROWS:
				return getRows();
			case ScheduleReportPackage.CYCLE_GROUP__INDEX:
				return getIndex();
			case ScheduleReportPackage.CYCLE_GROUP__USER_GROUP:
				return getUserGroup();
			case ScheduleReportPackage.CYCLE_GROUP__DELTA:
				return getDelta();
			case ScheduleReportPackage.CYCLE_GROUP__CHANGE_TYPE:
				return getChangeType();
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
			case ScheduleReportPackage.CYCLE_GROUP__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case ScheduleReportPackage.CYCLE_GROUP__ROWS:
				getRows().clear();
				getRows().addAll((Collection<? extends Row>)newValue);
				return;
			case ScheduleReportPackage.CYCLE_GROUP__INDEX:
				setIndex((Integer)newValue);
				return;
			case ScheduleReportPackage.CYCLE_GROUP__USER_GROUP:
				setUserGroup((UserGroup)newValue);
				return;
			case ScheduleReportPackage.CYCLE_GROUP__DELTA:
				setDelta((Integer)newValue);
				return;
			case ScheduleReportPackage.CYCLE_GROUP__CHANGE_TYPE:
				setChangeType((ChangeType)newValue);
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
			case ScheduleReportPackage.CYCLE_GROUP__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case ScheduleReportPackage.CYCLE_GROUP__ROWS:
				getRows().clear();
				return;
			case ScheduleReportPackage.CYCLE_GROUP__INDEX:
				unsetIndex();
				return;
			case ScheduleReportPackage.CYCLE_GROUP__USER_GROUP:
				setUserGroup((UserGroup)null);
				return;
			case ScheduleReportPackage.CYCLE_GROUP__DELTA:
				setDelta(DELTA_EDEFAULT);
				return;
			case ScheduleReportPackage.CYCLE_GROUP__CHANGE_TYPE:
				setChangeType(CHANGE_TYPE_EDEFAULT);
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
			case ScheduleReportPackage.CYCLE_GROUP__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case ScheduleReportPackage.CYCLE_GROUP__ROWS:
				return rows != null && !rows.isEmpty();
			case ScheduleReportPackage.CYCLE_GROUP__INDEX:
				return isSetIndex();
			case ScheduleReportPackage.CYCLE_GROUP__USER_GROUP:
				return getUserGroup() != null;
			case ScheduleReportPackage.CYCLE_GROUP__DELTA:
				return delta != DELTA_EDEFAULT;
			case ScheduleReportPackage.CYCLE_GROUP__CHANGE_TYPE:
				return changeType != CHANGE_TYPE_EDEFAULT;
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
		result.append(" (description: ");
		result.append(description);
		result.append(", index: ");
		if (indexESet) result.append(index); else result.append("<unset>");
		result.append(", delta: ");
		result.append(delta);
		result.append(", changeType: ");
		result.append(changeType);
		result.append(')');
		return result.toString();
	}

} //CycleGroupImpl
