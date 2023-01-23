/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.schedule.model.impl;

import com.mmxlabs.lingo.reports.views.schedule.model.CompositeRow;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Composite Row</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.CompositeRowImpl#getPreviousRow <em>Previous Row</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.CompositeRowImpl#getPinnedRow <em>Pinned Row</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CompositeRowImpl extends MinimalEObjectImpl.Container implements CompositeRow {
	/**
	 * The cached value of the '{@link #getPreviousRow() <em>Previous Row</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPreviousRow()
	 * @generated
	 * @ordered
	 */
	protected Row previousRow;

	/**
	 * The cached value of the '{@link #getPinnedRow() <em>Pinned Row</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPinnedRow()
	 * @generated
	 * @ordered
	 */
	protected Row pinnedRow;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CompositeRowImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ScheduleReportPackage.Literals.COMPOSITE_ROW;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Row getPreviousRow() {
		if (previousRow != null && previousRow.eIsProxy()) {
			InternalEObject oldPreviousRow = (InternalEObject)previousRow;
			previousRow = (Row)eResolveProxy(oldPreviousRow);
			if (previousRow != oldPreviousRow) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ScheduleReportPackage.COMPOSITE_ROW__PREVIOUS_ROW, oldPreviousRow, previousRow));
			}
		}
		return previousRow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Row basicGetPreviousRow() {
		return previousRow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPreviousRow(Row newPreviousRow) {
		Row oldPreviousRow = previousRow;
		previousRow = newPreviousRow;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.COMPOSITE_ROW__PREVIOUS_ROW, oldPreviousRow, previousRow));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Row getPinnedRow() {
		if (pinnedRow != null && pinnedRow.eIsProxy()) {
			InternalEObject oldPinnedRow = (InternalEObject)pinnedRow;
			pinnedRow = (Row)eResolveProxy(oldPinnedRow);
			if (pinnedRow != oldPinnedRow) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ScheduleReportPackage.COMPOSITE_ROW__PINNED_ROW, oldPinnedRow, pinnedRow));
			}
		}
		return pinnedRow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Row basicGetPinnedRow() {
		return pinnedRow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPinnedRow(Row newPinnedRow) {
		Row oldPinnedRow = pinnedRow;
		pinnedRow = newPinnedRow;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.COMPOSITE_ROW__PINNED_ROW, oldPinnedRow, pinnedRow));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ScheduleReportPackage.COMPOSITE_ROW__PREVIOUS_ROW:
				if (resolve) return getPreviousRow();
				return basicGetPreviousRow();
			case ScheduleReportPackage.COMPOSITE_ROW__PINNED_ROW:
				if (resolve) return getPinnedRow();
				return basicGetPinnedRow();
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
			case ScheduleReportPackage.COMPOSITE_ROW__PREVIOUS_ROW:
				setPreviousRow((Row)newValue);
				return;
			case ScheduleReportPackage.COMPOSITE_ROW__PINNED_ROW:
				setPinnedRow((Row)newValue);
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
			case ScheduleReportPackage.COMPOSITE_ROW__PREVIOUS_ROW:
				setPreviousRow((Row)null);
				return;
			case ScheduleReportPackage.COMPOSITE_ROW__PINNED_ROW:
				setPinnedRow((Row)null);
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
			case ScheduleReportPackage.COMPOSITE_ROW__PREVIOUS_ROW:
				return previousRow != null;
			case ScheduleReportPackage.COMPOSITE_ROW__PINNED_ROW:
				return pinnedRow != null;
		}
		return super.eIsSet(featureID);
	}

} //CompositeRowImpl
