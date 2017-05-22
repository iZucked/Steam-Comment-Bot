/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model.impl;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage;

import com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics;
import com.mmxlabs.lingo.reports.views.changeset.model.Metrics;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Change Set Table Group</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableGroupImpl#getRows <em>Rows</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableGroupImpl#getDeltaMetrics <em>Delta Metrics</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableGroupImpl#getCurrentMetrics <em>Current Metrics</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableGroupImpl#getChangeSet <em>Change Set</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableGroupImpl#getDescription <em>Description</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ChangeSetTableGroupImpl extends MinimalEObjectImpl.Container implements ChangeSetTableGroup {
	/**
	 * The cached value of the '{@link #getRows() <em>Rows</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRows()
	 * @generated
	 * @ordered
	 */
	protected EList<ChangeSetTableRow> rows;

	/**
	 * The cached value of the '{@link #getDeltaMetrics() <em>Delta Metrics</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDeltaMetrics()
	 * @generated
	 * @ordered
	 */
	protected DeltaMetrics deltaMetrics;

	/**
	 * The cached value of the '{@link #getCurrentMetrics() <em>Current Metrics</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCurrentMetrics()
	 * @generated
	 * @ordered
	 */
	protected Metrics currentMetrics;

	/**
	 * The cached value of the '{@link #getChangeSet() <em>Change Set</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChangeSet()
	 * @generated
	 * @ordered
	 */
	protected ChangeSet changeSet;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ChangeSetTableGroupImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ChangesetPackage.Literals.CHANGE_SET_TABLE_GROUP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ChangeSetTableRow> getRows() {
		if (rows == null) {
			rows = new EObjectContainmentEList<ChangeSetTableRow>(ChangeSetTableRow.class, this, ChangesetPackage.CHANGE_SET_TABLE_GROUP__ROWS);
		}
		return rows;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeltaMetrics getDeltaMetrics() {
		return deltaMetrics;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDeltaMetrics(DeltaMetrics newDeltaMetrics, NotificationChain msgs) {
		DeltaMetrics oldDeltaMetrics = deltaMetrics;
		deltaMetrics = newDeltaMetrics;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_GROUP__DELTA_METRICS, oldDeltaMetrics, newDeltaMetrics);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDeltaMetrics(DeltaMetrics newDeltaMetrics) {
		if (newDeltaMetrics != deltaMetrics) {
			NotificationChain msgs = null;
			if (deltaMetrics != null)
				msgs = ((InternalEObject)deltaMetrics).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChangesetPackage.CHANGE_SET_TABLE_GROUP__DELTA_METRICS, null, msgs);
			if (newDeltaMetrics != null)
				msgs = ((InternalEObject)newDeltaMetrics).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChangesetPackage.CHANGE_SET_TABLE_GROUP__DELTA_METRICS, null, msgs);
			msgs = basicSetDeltaMetrics(newDeltaMetrics, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_GROUP__DELTA_METRICS, newDeltaMetrics, newDeltaMetrics));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Metrics getCurrentMetrics() {
		return currentMetrics;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCurrentMetrics(Metrics newCurrentMetrics, NotificationChain msgs) {
		Metrics oldCurrentMetrics = currentMetrics;
		currentMetrics = newCurrentMetrics;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_GROUP__CURRENT_METRICS, oldCurrentMetrics, newCurrentMetrics);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCurrentMetrics(Metrics newCurrentMetrics) {
		if (newCurrentMetrics != currentMetrics) {
			NotificationChain msgs = null;
			if (currentMetrics != null)
				msgs = ((InternalEObject)currentMetrics).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChangesetPackage.CHANGE_SET_TABLE_GROUP__CURRENT_METRICS, null, msgs);
			if (newCurrentMetrics != null)
				msgs = ((InternalEObject)newCurrentMetrics).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChangesetPackage.CHANGE_SET_TABLE_GROUP__CURRENT_METRICS, null, msgs);
			msgs = basicSetCurrentMetrics(newCurrentMetrics, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_GROUP__CURRENT_METRICS, newCurrentMetrics, newCurrentMetrics));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSet getChangeSet() {
		if (changeSet != null && changeSet.eIsProxy()) {
			InternalEObject oldChangeSet = (InternalEObject)changeSet;
			changeSet = (ChangeSet)eResolveProxy(oldChangeSet);
			if (changeSet != oldChangeSet) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_TABLE_GROUP__CHANGE_SET, oldChangeSet, changeSet));
			}
		}
		return changeSet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSet basicGetChangeSet() {
		return changeSet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setChangeSet(ChangeSet newChangeSet) {
		ChangeSet oldChangeSet = changeSet;
		changeSet = newChangeSet;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_GROUP__CHANGE_SET, oldChangeSet, changeSet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_GROUP__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__ROWS:
				return ((InternalEList<?>)getRows()).basicRemove(otherEnd, msgs);
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__DELTA_METRICS:
				return basicSetDeltaMetrics(null, msgs);
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__CURRENT_METRICS:
				return basicSetCurrentMetrics(null, msgs);
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
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__ROWS:
				return getRows();
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__DELTA_METRICS:
				return getDeltaMetrics();
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__CURRENT_METRICS:
				return getCurrentMetrics();
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__CHANGE_SET:
				if (resolve) return getChangeSet();
				return basicGetChangeSet();
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__DESCRIPTION:
				return getDescription();
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
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__ROWS:
				getRows().clear();
				getRows().addAll((Collection<? extends ChangeSetTableRow>)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__DELTA_METRICS:
				setDeltaMetrics((DeltaMetrics)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__CURRENT_METRICS:
				setCurrentMetrics((Metrics)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__CHANGE_SET:
				setChangeSet((ChangeSet)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__DESCRIPTION:
				setDescription((String)newValue);
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
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__ROWS:
				getRows().clear();
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__DELTA_METRICS:
				setDeltaMetrics((DeltaMetrics)null);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__CURRENT_METRICS:
				setCurrentMetrics((Metrics)null);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__CHANGE_SET:
				setChangeSet((ChangeSet)null);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
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
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__ROWS:
				return rows != null && !rows.isEmpty();
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__DELTA_METRICS:
				return deltaMetrics != null;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__CURRENT_METRICS:
				return currentMetrics != null;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__CHANGE_SET:
				return changeSet != null;
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
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
		result.append(')');
		return result.toString();
	}

} //ChangeSetTableGroupImpl
