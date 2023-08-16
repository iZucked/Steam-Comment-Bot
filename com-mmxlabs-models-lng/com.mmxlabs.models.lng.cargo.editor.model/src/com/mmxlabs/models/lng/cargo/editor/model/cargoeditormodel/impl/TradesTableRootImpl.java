/**
 */
package com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl;

import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.CargoEditorModelPackage;
import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow;
import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesTableRoot;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Trades Table Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl.TradesTableRootImpl#getTradesRows <em>Trades Rows</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TradesTableRootImpl extends EObjectImpl implements TradesTableRoot {
	/**
	 * The cached value of the '{@link #getTradesRows() <em>Trades Rows</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTradesRows()
	 * @generated
	 * @ordered
	 */
	protected EList<TradesRow> tradesRows;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TradesTableRootImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoEditorModelPackage.Literals.TRADES_TABLE_ROOT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<TradesRow> getTradesRows() {
		if (tradesRows == null) {
			tradesRows = new EObjectResolvingEList<TradesRow>(TradesRow.class, this, CargoEditorModelPackage.TRADES_TABLE_ROOT__TRADES_ROWS);
		}
		return tradesRows;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoEditorModelPackage.TRADES_TABLE_ROOT__TRADES_ROWS:
				return getTradesRows();
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
			case CargoEditorModelPackage.TRADES_TABLE_ROOT__TRADES_ROWS:
				getTradesRows().clear();
				getTradesRows().addAll((Collection<? extends TradesRow>)newValue);
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
			case CargoEditorModelPackage.TRADES_TABLE_ROOT__TRADES_ROWS:
				getTradesRows().clear();
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
			case CargoEditorModelPackage.TRADES_TABLE_ROOT__TRADES_ROWS:
				return tradesRows != null && !tradesRows.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //TradesTableRootImpl
