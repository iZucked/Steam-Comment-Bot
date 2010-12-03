/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package scenario.cargo;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cargo</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.cargo.Cargo#getId <em>Id</em>}</li>
 *   <li>{@link scenario.cargo.Cargo#getLoadSlot <em>Load Slot</em>}</li>
 *   <li>{@link scenario.cargo.Cargo#getDischargeSlot <em>Discharge Slot</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.cargo.CargoPackage#getCargo()
 * @model
 * @generated
 */
public interface Cargo extends EObject {
	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see scenario.cargo.CargoPackage#getCargo_Id()
	 * @model
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link scenario.cargo.Cargo#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Load Slot</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Load Slot</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Load Slot</em>' containment reference.
	 * @see #setLoadSlot(LoadSlot)
	 * @see scenario.cargo.CargoPackage#getCargo_LoadSlot()
	 * @model containment="true"
	 * @generated
	 */
	LoadSlot getLoadSlot();

	/**
	 * Sets the value of the '{@link scenario.cargo.Cargo#getLoadSlot <em>Load Slot</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Load Slot</em>' containment reference.
	 * @see #getLoadSlot()
	 * @generated
	 */
	void setLoadSlot(LoadSlot value);

	/**
	 * Returns the value of the '<em><b>Discharge Slot</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Discharge Slot</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discharge Slot</em>' containment reference.
	 * @see #setDischargeSlot(Slot)
	 * @see scenario.cargo.CargoPackage#getCargo_DischargeSlot()
	 * @model containment="true"
	 * @generated
	 */
	Slot getDischargeSlot();

	/**
	 * Sets the value of the '{@link scenario.cargo.Cargo#getDischargeSlot <em>Discharge Slot</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discharge Slot</em>' containment reference.
	 * @see #getDischargeSlot()
	 * @generated
	 */
	void setDischargeSlot(Slot value);

} // Cargo
