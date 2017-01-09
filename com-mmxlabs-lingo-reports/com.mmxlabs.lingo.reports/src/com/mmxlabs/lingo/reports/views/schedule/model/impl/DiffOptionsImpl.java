/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.schedule.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import com.mmxlabs.lingo.reports.views.schedule.model.DiffOptions;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Diff Options</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.DiffOptionsImpl#isFilterSelectedElements <em>Filter Selected Elements</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.DiffOptionsImpl#isFilterSelectedSequences <em>Filter Selected Sequences</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DiffOptionsImpl extends MinimalEObjectImpl.Container implements DiffOptions {
	/**
	 * The default value of the '{@link #isFilterSelectedElements() <em>Filter Selected Elements</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFilterSelectedElements()
	 * @generated
	 * @ordered
	 */
	protected static final boolean FILTER_SELECTED_ELEMENTS_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isFilterSelectedElements() <em>Filter Selected Elements</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFilterSelectedElements()
	 * @generated
	 * @ordered
	 */
	protected boolean filterSelectedElements = FILTER_SELECTED_ELEMENTS_EDEFAULT;

	/**
	 * The default value of the '{@link #isFilterSelectedSequences() <em>Filter Selected Sequences</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFilterSelectedSequences()
	 * @generated
	 * @ordered
	 */
	protected static final boolean FILTER_SELECTED_SEQUENCES_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isFilterSelectedSequences() <em>Filter Selected Sequences</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFilterSelectedSequences()
	 * @generated
	 * @ordered
	 */
	protected boolean filterSelectedSequences = FILTER_SELECTED_SEQUENCES_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DiffOptionsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ScheduleReportPackage.Literals.DIFF_OPTIONS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isFilterSelectedElements() {
		return filterSelectedElements;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFilterSelectedElements(boolean newFilterSelectedElements) {
		boolean oldFilterSelectedElements = filterSelectedElements;
		filterSelectedElements = newFilterSelectedElements;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.DIFF_OPTIONS__FILTER_SELECTED_ELEMENTS, oldFilterSelectedElements, filterSelectedElements));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isFilterSelectedSequences() {
		return filterSelectedSequences;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFilterSelectedSequences(boolean newFilterSelectedSequences) {
		boolean oldFilterSelectedSequences = filterSelectedSequences;
		filterSelectedSequences = newFilterSelectedSequences;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.DIFF_OPTIONS__FILTER_SELECTED_SEQUENCES, oldFilterSelectedSequences, filterSelectedSequences));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ScheduleReportPackage.DIFF_OPTIONS__FILTER_SELECTED_ELEMENTS:
				return isFilterSelectedElements();
			case ScheduleReportPackage.DIFF_OPTIONS__FILTER_SELECTED_SEQUENCES:
				return isFilterSelectedSequences();
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
			case ScheduleReportPackage.DIFF_OPTIONS__FILTER_SELECTED_ELEMENTS:
				setFilterSelectedElements((Boolean)newValue);
				return;
			case ScheduleReportPackage.DIFF_OPTIONS__FILTER_SELECTED_SEQUENCES:
				setFilterSelectedSequences((Boolean)newValue);
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
			case ScheduleReportPackage.DIFF_OPTIONS__FILTER_SELECTED_ELEMENTS:
				setFilterSelectedElements(FILTER_SELECTED_ELEMENTS_EDEFAULT);
				return;
			case ScheduleReportPackage.DIFF_OPTIONS__FILTER_SELECTED_SEQUENCES:
				setFilterSelectedSequences(FILTER_SELECTED_SEQUENCES_EDEFAULT);
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
			case ScheduleReportPackage.DIFF_OPTIONS__FILTER_SELECTED_ELEMENTS:
				return filterSelectedElements != FILTER_SELECTED_ELEMENTS_EDEFAULT;
			case ScheduleReportPackage.DIFF_OPTIONS__FILTER_SELECTED_SEQUENCES:
				return filterSelectedSequences != FILTER_SELECTED_SEQUENCES_EDEFAULT;
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
		result.append(" (filterSelectedElements: ");
		result.append(filterSelectedElements);
		result.append(", filterSelectedSequences: ");
		result.append(filterSelectedSequences);
		result.append(')');
		return result.toString();
	}

} //DiffOptionsImpl
