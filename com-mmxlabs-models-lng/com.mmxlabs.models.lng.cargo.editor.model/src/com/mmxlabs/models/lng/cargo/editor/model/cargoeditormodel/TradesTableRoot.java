/**
 */
package com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Trades Table Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesTableRoot#getTradesRows <em>Trades Rows</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.CargoEditorModelPackage#getTradesTableRoot()
 * @model
 * @generated
 */
public interface TradesTableRoot extends EObject {
	/**
	 * Returns the value of the '<em><b>Trades Rows</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Trades Rows</em>' reference list.
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.CargoEditorModelPackage#getTradesTableRoot_TradesRows()
	 * @model
	 * @generated
	 */
	EList<TradesRow> getTradesRows();

} // TradesTableRoot
