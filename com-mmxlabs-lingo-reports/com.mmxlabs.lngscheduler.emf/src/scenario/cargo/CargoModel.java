/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.cargo;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.cargo.CargoModel#getCargoes <em>Cargoes</em>}</li>
 *   <li>{@link scenario.cargo.CargoModel#getSpareDischargeSlots <em>Spare Discharge Slots</em>}</li>
 *   <li>{@link scenario.cargo.CargoModel#getSpareLoadSlots <em>Spare Load Slots</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.cargo.CargoPackage#getCargoModel()
 * @model
 * @generated
 */
public interface CargoModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Cargoes</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.cargo.Cargo}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cargoes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargoes</em>' containment reference list.
	 * @see scenario.cargo.CargoPackage#getCargoModel_Cargoes()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<Cargo> getCargoes();

	/**
	 * Returns the value of the '<em><b>Spare Discharge Slots</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.cargo.Slot}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Spare Discharge Slots</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Spare Discharge Slots</em>' containment reference list.
	 * @see scenario.cargo.CargoPackage#getCargoModel_SpareDischargeSlots()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<Slot> getSpareDischargeSlots();

	/**
	 * Returns the value of the '<em><b>Spare Load Slots</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.cargo.LoadSlot}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Spare Load Slots</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Spare Load Slots</em>' containment reference list.
	 * @see scenario.cargo.CargoPackage#getCargoModel_SpareLoadSlots()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<LoadSlot> getSpareLoadSlots();

} // CargoModel
