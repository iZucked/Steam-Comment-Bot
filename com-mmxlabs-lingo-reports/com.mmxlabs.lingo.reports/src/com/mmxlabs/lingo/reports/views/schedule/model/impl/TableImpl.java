/**
 */
package com.mmxlabs.lingo.reports.views.schedule.model.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.RowGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Table</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.TableImpl#getRows <em>Rows</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.TableImpl#getCycleGroups <em>Cycle Groups</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.TableImpl#getRowGroups <em>Row Groups</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TableImpl extends MinimalEObjectImpl.Container implements Table {
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
	public EList<Row> getRows() {
		if (rows == null) {
			rows = new EObjectContainmentEList<Row>(Row.class, this, ScheduleReportPackage.TABLE__ROWS);
		}
		return rows;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ScheduleReportPackage.TABLE__ROWS:
				return ((InternalEList<?>)getRows()).basicRemove(otherEnd, msgs);
			case ScheduleReportPackage.TABLE__CYCLE_GROUPS:
				return ((InternalEList<?>)getCycleGroups()).basicRemove(otherEnd, msgs);
			case ScheduleReportPackage.TABLE__ROW_GROUPS:
				return ((InternalEList<?>)getRowGroups()).basicRemove(otherEnd, msgs);
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
			case ScheduleReportPackage.TABLE__ROWS:
				return getRows();
			case ScheduleReportPackage.TABLE__CYCLE_GROUPS:
				return getCycleGroups();
			case ScheduleReportPackage.TABLE__ROW_GROUPS:
				return getRowGroups();
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
			case ScheduleReportPackage.TABLE__ROWS:
				getRows().clear();
				return;
			case ScheduleReportPackage.TABLE__CYCLE_GROUPS:
				getCycleGroups().clear();
				return;
			case ScheduleReportPackage.TABLE__ROW_GROUPS:
				getRowGroups().clear();
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
			case ScheduleReportPackage.TABLE__ROWS:
				return rows != null && !rows.isEmpty();
			case ScheduleReportPackage.TABLE__CYCLE_GROUPS:
				return cycleGroups != null && !cycleGroups.isEmpty();
			case ScheduleReportPackage.TABLE__ROW_GROUPS:
				return rowGroups != null && !rowGroups.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //TableImpl
