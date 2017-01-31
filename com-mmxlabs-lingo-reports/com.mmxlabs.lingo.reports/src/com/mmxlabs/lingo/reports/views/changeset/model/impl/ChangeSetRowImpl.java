/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowDataGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Change Set Row</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#isWiringChange <em>Wiring Change</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#isVesselChange <em>Vessel Change</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getBeforeData <em>Before Data</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getAfterData <em>After Data</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ChangeSetRowImpl extends MinimalEObjectImpl.Container implements ChangeSetRow {
	/**
	 * The default value of the '{@link #isWiringChange() <em>Wiring Change</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isWiringChange()
	 * @generated
	 * @ordered
	 */
	protected static final boolean WIRING_CHANGE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isWiringChange() <em>Wiring Change</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isWiringChange()
	 * @generated
	 * @ordered
	 */
	protected boolean wiringChange = WIRING_CHANGE_EDEFAULT;

	/**
	 * The default value of the '{@link #isVesselChange() <em>Vessel Change</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isVesselChange()
	 * @generated
	 * @ordered
	 */
	protected static final boolean VESSEL_CHANGE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isVesselChange() <em>Vessel Change</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isVesselChange()
	 * @generated
	 * @ordered
	 */
	protected boolean vesselChange = VESSEL_CHANGE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getBeforeData() <em>Before Data</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBeforeData()
	 * @generated
	 * @ordered
	 */
	protected ChangeSetRowDataGroup beforeData;

	/**
	 * The cached value of the '{@link #getAfterData() <em>After Data</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAfterData()
	 * @generated
	 * @ordered
	 */
	protected ChangeSetRowDataGroup afterData;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ChangeSetRowImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ChangesetPackage.Literals.CHANGE_SET_ROW;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isWiringChange() {
		return wiringChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWiringChange(boolean newWiringChange) {
		boolean oldWiringChange = wiringChange;
		wiringChange = newWiringChange;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__WIRING_CHANGE, oldWiringChange, wiringChange));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isVesselChange() {
		return vesselChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVesselChange(boolean newVesselChange) {
		boolean oldVesselChange = vesselChange;
		vesselChange = newVesselChange;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__VESSEL_CHANGE, oldVesselChange, vesselChange));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSetRowDataGroup getBeforeData() {
		return beforeData;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetBeforeData(ChangeSetRowDataGroup newBeforeData, NotificationChain msgs) {
		ChangeSetRowDataGroup oldBeforeData = beforeData;
		beforeData = newBeforeData;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__BEFORE_DATA, oldBeforeData, newBeforeData);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBeforeData(ChangeSetRowDataGroup newBeforeData) {
		if (newBeforeData != beforeData) {
			NotificationChain msgs = null;
			if (beforeData != null)
				msgs = ((InternalEObject)beforeData).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChangesetPackage.CHANGE_SET_ROW__BEFORE_DATA, null, msgs);
			if (newBeforeData != null)
				msgs = ((InternalEObject)newBeforeData).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChangesetPackage.CHANGE_SET_ROW__BEFORE_DATA, null, msgs);
			msgs = basicSetBeforeData(newBeforeData, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__BEFORE_DATA, newBeforeData, newBeforeData));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSetRowDataGroup getAfterData() {
		return afterData;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAfterData(ChangeSetRowDataGroup newAfterData, NotificationChain msgs) {
		ChangeSetRowDataGroup oldAfterData = afterData;
		afterData = newAfterData;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__AFTER_DATA, oldAfterData, newAfterData);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAfterData(ChangeSetRowDataGroup newAfterData) {
		if (newAfterData != afterData) {
			NotificationChain msgs = null;
			if (afterData != null)
				msgs = ((InternalEObject)afterData).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChangesetPackage.CHANGE_SET_ROW__AFTER_DATA, null, msgs);
			if (newAfterData != null)
				msgs = ((InternalEObject)newAfterData).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChangesetPackage.CHANGE_SET_ROW__AFTER_DATA, null, msgs);
			msgs = basicSetAfterData(newAfterData, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__AFTER_DATA, newAfterData, newAfterData));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ChangesetPackage.CHANGE_SET_ROW__BEFORE_DATA:
				return basicSetBeforeData(null, msgs);
			case ChangesetPackage.CHANGE_SET_ROW__AFTER_DATA:
				return basicSetAfterData(null, msgs);
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
			case ChangesetPackage.CHANGE_SET_ROW__WIRING_CHANGE:
				return isWiringChange();
			case ChangesetPackage.CHANGE_SET_ROW__VESSEL_CHANGE:
				return isVesselChange();
			case ChangesetPackage.CHANGE_SET_ROW__BEFORE_DATA:
				return getBeforeData();
			case ChangesetPackage.CHANGE_SET_ROW__AFTER_DATA:
				return getAfterData();
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
			case ChangesetPackage.CHANGE_SET_ROW__WIRING_CHANGE:
				setWiringChange((Boolean)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__VESSEL_CHANGE:
				setVesselChange((Boolean)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__BEFORE_DATA:
				setBeforeData((ChangeSetRowDataGroup)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__AFTER_DATA:
				setAfterData((ChangeSetRowDataGroup)newValue);
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
			case ChangesetPackage.CHANGE_SET_ROW__WIRING_CHANGE:
				setWiringChange(WIRING_CHANGE_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__VESSEL_CHANGE:
				setVesselChange(VESSEL_CHANGE_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__BEFORE_DATA:
				setBeforeData((ChangeSetRowDataGroup)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__AFTER_DATA:
				setAfterData((ChangeSetRowDataGroup)null);
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
			case ChangesetPackage.CHANGE_SET_ROW__WIRING_CHANGE:
				return wiringChange != WIRING_CHANGE_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_ROW__VESSEL_CHANGE:
				return vesselChange != VESSEL_CHANGE_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_ROW__BEFORE_DATA:
				return beforeData != null;
			case ChangesetPackage.CHANGE_SET_ROW__AFTER_DATA:
				return afterData != null;
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
		result.append(" (wiringChange: ");
		result.append(wiringChange);
		result.append(", vesselChange: ");
		result.append(vesselChange);
		result.append(')');
		return result.toString();
	}

} //ChangeSetRowImpl
