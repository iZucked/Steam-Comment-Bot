/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.cargo;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import scenario.fleet.Vessel;

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
 *   <li>{@link scenario.cargo.Cargo#getAllowedVessels <em>Allowed Vessels</em>}</li>
 *   <li>{@link scenario.cargo.Cargo#getCargoType <em>Cargo Type</em>}</li>
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

	/**
	 * Returns the value of the '<em><b>Allowed Vessels</b></em>' reference list.
	 * The list contents are of type {@link scenario.fleet.Vessel}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Allowed Vessels</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Allowed Vessels</em>' reference list.
	 * @see scenario.cargo.CargoPackage#getCargo_AllowedVessels()
	 * @model
	 * @generated
	 */
	EList<Vessel> getAllowedVessels();

	/**
	 * Returns the value of the '<em><b>Cargo Type</b></em>' attribute.
	 * The default value is <code>"Fleet"</code>.
	 * The literals are from the enumeration {@link scenario.cargo.CargoType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cargo Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargo Type</em>' attribute.
	 * @see scenario.cargo.CargoType
	 * @see #setCargoType(CargoType)
	 * @see scenario.cargo.CargoPackage#getCargo_CargoType()
	 * @model default="Fleet" required="true"
	 * @generated
	 */
	CargoType getCargoType();

	/**
	 * Sets the value of the '{@link scenario.cargo.Cargo#getCargoType <em>Cargo Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cargo Type</em>' attribute.
	 * @see scenario.cargo.CargoType
	 * @see #getCargoType()
	 * @generated
	 */
	void setCargoType(CargoType value);

} // Cargo
