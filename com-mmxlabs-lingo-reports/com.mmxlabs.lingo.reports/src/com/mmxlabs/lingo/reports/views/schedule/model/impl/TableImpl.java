/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.DiffOptions;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.RowGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.lingo.reports.views.schedule.model.UserGroup;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Table</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.TableImpl#getOptions <em>Options</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.TableImpl#getRows <em>Rows</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.TableImpl#getCycleGroups <em>Cycle Groups</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.TableImpl#getRowGroups <em>Row Groups</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.TableImpl#getScenarios <em>Scenarios</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.TableImpl#getPinnedScenario <em>Pinned Scenario</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.TableImpl#getUserGroups <em>User Groups</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.TableImpl#getSelectedElements <em>Selected Elements</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TableImpl extends MinimalEObjectImpl.Container implements Table {
	/**
	 * The cached value of the '{@link #getOptions() <em>Options</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOptions()
	 * @generated
	 * @ordered
	 */
	protected DiffOptions options;

	/**
	 * The cached value of the '{@link #getRows() <em>Rows</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRows()
	 * @generated
	 * @ordered
	 */
	protected EList<Row> rows;

	/**
	 * The cached value of the '{@link #getCycleGroups() <em>Cycle Groups</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCycleGroups()
	 * @generated
	 * @ordered
	 */
	protected EList<CycleGroup> cycleGroups;

	/**
	 * The cached value of the '{@link #getRowGroups() <em>Row Groups</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRowGroups()
	 * @generated
	 * @ordered
	 */
	protected EList<RowGroup> rowGroups;

	/**
	 * The cached value of the '{@link #getScenarios() <em>Scenarios</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScenarios()
	 * @generated
	 * @ordered
	 */
	protected EList<EObject> scenarios;

	/**
	 * The cached value of the '{@link #getPinnedScenario() <em>Pinned Scenario</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPinnedScenario()
	 * @generated
	 * @ordered
	 */
	protected EObject pinnedScenario;

	/**
	 * The cached value of the '{@link #getUserGroups() <em>User Groups</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUserGroups()
	 * @generated
	 * @ordered
	 */
	protected EList<UserGroup> userGroups;

	/**
	 * The cached value of the '{@link #getSelectedElements() <em>Selected Elements</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSelectedElements()
	 * @generated
	 * @ordered
	 */
	protected EList<EObject> selectedElements;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TableImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ScheduleReportPackage.Literals.TABLE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiffOptions getOptions() {
		return options;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOptions(DiffOptions newOptions, NotificationChain msgs) {
		DiffOptions oldOptions = options;
		options = newOptions;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.TABLE__OPTIONS, oldOptions, newOptions);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOptions(DiffOptions newOptions) {
		if (newOptions != options) {
			NotificationChain msgs = null;
			if (options != null)
				msgs = ((InternalEObject)options).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ScheduleReportPackage.TABLE__OPTIONS, null, msgs);
			if (newOptions != null)
				msgs = ((InternalEObject)newOptions).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ScheduleReportPackage.TABLE__OPTIONS, null, msgs);
			msgs = basicSetOptions(newOptions, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.TABLE__OPTIONS, newOptions, newOptions));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Row> getRows() {
		if (rows == null) {
			rows = new EObjectContainmentWithInverseEList<Row>(Row.class, this, ScheduleReportPackage.TABLE__ROWS, ScheduleReportPackage.ROW__TABLE);
		}
		return rows;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<CycleGroup> getCycleGroups() {
		if (cycleGroups == null) {
			cycleGroups = new EObjectContainmentEList<CycleGroup>(CycleGroup.class, this, ScheduleReportPackage.TABLE__CYCLE_GROUPS);
		}
		return cycleGroups;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<RowGroup> getRowGroups() {
		if (rowGroups == null) {
			rowGroups = new EObjectContainmentEList<RowGroup>(RowGroup.class, this, ScheduleReportPackage.TABLE__ROW_GROUPS);
		}
		return rowGroups;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<EObject> getScenarios() {
		if (scenarios == null) {
			scenarios = new EObjectResolvingEList<EObject>(EObject.class, this, ScheduleReportPackage.TABLE__SCENARIOS);
		}
		return scenarios;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject getPinnedScenario() {
		if (pinnedScenario != null && pinnedScenario.eIsProxy()) {
			InternalEObject oldPinnedScenario = (InternalEObject)pinnedScenario;
			pinnedScenario = eResolveProxy(oldPinnedScenario);
			if (pinnedScenario != oldPinnedScenario) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ScheduleReportPackage.TABLE__PINNED_SCENARIO, oldPinnedScenario, pinnedScenario));
			}
		}
		return pinnedScenario;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetPinnedScenario() {
		return pinnedScenario;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPinnedScenario(EObject newPinnedScenario) {
		EObject oldPinnedScenario = pinnedScenario;
		pinnedScenario = newPinnedScenario;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.TABLE__PINNED_SCENARIO, oldPinnedScenario, pinnedScenario));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<UserGroup> getUserGroups() {
		if (userGroups == null) {
			userGroups = new EObjectContainmentEList<UserGroup>(UserGroup.class, this, ScheduleReportPackage.TABLE__USER_GROUPS);
		}
		return userGroups;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EObject> getSelectedElements() {
		if (selectedElements == null) {
			selectedElements = new EObjectResolvingEList<EObject>(EObject.class, this, ScheduleReportPackage.TABLE__SELECTED_ELEMENTS);
		}
		return selectedElements;
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
			case ScheduleReportPackage.TABLE__ROWS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getRows()).basicAdd(otherEnd, msgs);
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
			case ScheduleReportPackage.TABLE__OPTIONS:
				return basicSetOptions(null, msgs);
			case ScheduleReportPackage.TABLE__ROWS:
				return ((InternalEList<?>)getRows()).basicRemove(otherEnd, msgs);
			case ScheduleReportPackage.TABLE__CYCLE_GROUPS:
				return ((InternalEList<?>)getCycleGroups()).basicRemove(otherEnd, msgs);
			case ScheduleReportPackage.TABLE__ROW_GROUPS:
				return ((InternalEList<?>)getRowGroups()).basicRemove(otherEnd, msgs);
			case ScheduleReportPackage.TABLE__USER_GROUPS:
				return ((InternalEList<?>)getUserGroups()).basicRemove(otherEnd, msgs);
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
			case ScheduleReportPackage.TABLE__OPTIONS:
				return getOptions();
			case ScheduleReportPackage.TABLE__ROWS:
				return getRows();
			case ScheduleReportPackage.TABLE__CYCLE_GROUPS:
				return getCycleGroups();
			case ScheduleReportPackage.TABLE__ROW_GROUPS:
				return getRowGroups();
			case ScheduleReportPackage.TABLE__SCENARIOS:
				return getScenarios();
			case ScheduleReportPackage.TABLE__PINNED_SCENARIO:
				if (resolve) return getPinnedScenario();
				return basicGetPinnedScenario();
			case ScheduleReportPackage.TABLE__USER_GROUPS:
				return getUserGroups();
			case ScheduleReportPackage.TABLE__SELECTED_ELEMENTS:
				return getSelectedElements();
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
			case ScheduleReportPackage.TABLE__OPTIONS:
				setOptions((DiffOptions)newValue);
				return;
			case ScheduleReportPackage.TABLE__ROWS:
				getRows().clear();
				getRows().addAll((Collection<? extends Row>)newValue);
				return;
			case ScheduleReportPackage.TABLE__CYCLE_GROUPS:
				getCycleGroups().clear();
				getCycleGroups().addAll((Collection<? extends CycleGroup>)newValue);
				return;
			case ScheduleReportPackage.TABLE__ROW_GROUPS:
				getRowGroups().clear();
				getRowGroups().addAll((Collection<? extends RowGroup>)newValue);
				return;
			case ScheduleReportPackage.TABLE__SCENARIOS:
				getScenarios().clear();
				getScenarios().addAll((Collection<? extends EObject>)newValue);
				return;
			case ScheduleReportPackage.TABLE__PINNED_SCENARIO:
				setPinnedScenario((EObject)newValue);
				return;
			case ScheduleReportPackage.TABLE__USER_GROUPS:
				getUserGroups().clear();
				getUserGroups().addAll((Collection<? extends UserGroup>)newValue);
				return;
			case ScheduleReportPackage.TABLE__SELECTED_ELEMENTS:
				getSelectedElements().clear();
				getSelectedElements().addAll((Collection<? extends EObject>)newValue);
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
			case ScheduleReportPackage.TABLE__OPTIONS:
				setOptions((DiffOptions)null);
				return;
			case ScheduleReportPackage.TABLE__ROWS:
				getRows().clear();
				return;
			case ScheduleReportPackage.TABLE__CYCLE_GROUPS:
				getCycleGroups().clear();
				return;
			case ScheduleReportPackage.TABLE__ROW_GROUPS:
				getRowGroups().clear();
				return;
			case ScheduleReportPackage.TABLE__SCENARIOS:
				getScenarios().clear();
				return;
			case ScheduleReportPackage.TABLE__PINNED_SCENARIO:
				setPinnedScenario((EObject)null);
				return;
			case ScheduleReportPackage.TABLE__USER_GROUPS:
				getUserGroups().clear();
				return;
			case ScheduleReportPackage.TABLE__SELECTED_ELEMENTS:
				getSelectedElements().clear();
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
			case ScheduleReportPackage.TABLE__OPTIONS:
				return options != null;
			case ScheduleReportPackage.TABLE__ROWS:
				return rows != null && !rows.isEmpty();
			case ScheduleReportPackage.TABLE__CYCLE_GROUPS:
				return cycleGroups != null && !cycleGroups.isEmpty();
			case ScheduleReportPackage.TABLE__ROW_GROUPS:
				return rowGroups != null && !rowGroups.isEmpty();
			case ScheduleReportPackage.TABLE__SCENARIOS:
				return scenarios != null && !scenarios.isEmpty();
			case ScheduleReportPackage.TABLE__PINNED_SCENARIO:
				return pinnedScenario != null;
			case ScheduleReportPackage.TABLE__USER_GROUPS:
				return userGroups != null && !userGroups.isEmpty();
			case ScheduleReportPackage.TABLE__SELECTED_ELEMENTS:
				return selectedElements != null && !selectedElements.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //TableImpl
