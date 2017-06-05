/**
 */
package com.mmxlabs.models.lng.cargo;

import java.time.LocalDate;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Inventory Event Row</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getStartDate <em>Start Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getEndDate <em>End Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getPeriod <em>Period</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getCounterParty <em>Counter Party</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getVolume <em>Volume</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventoryEventRow()
 * @model
 * @generated
 */
public interface InventoryEventRow extends EObject {
	/**
	 * Returns the value of the '<em><b>Start Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Date</em>' attribute.
	 * @see #setStartDate(LocalDate)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventoryEventRow_StartDate()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getStartDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getStartDate <em>Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Date</em>' attribute.
	 * @see #getStartDate()
	 * @generated
	 */
	void setStartDate(LocalDate value);

	/**
	 * Returns the value of the '<em><b>End Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Date</em>' attribute.
	 * @see #setEndDate(LocalDate)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventoryEventRow_EndDate()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getEndDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getEndDate <em>End Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Date</em>' attribute.
	 * @see #getEndDate()
	 * @generated
	 */
	void setEndDate(LocalDate value);

	/**
	 * Returns the value of the '<em><b>Period</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.cargo.InventoryFrequency}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Period</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Period</em>' attribute.
	 * @see com.mmxlabs.models.lng.cargo.InventoryFrequency
	 * @see #setPeriod(InventoryFrequency)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventoryEventRow_Period()
	 * @model
	 * @generated
	 */
	InventoryFrequency getPeriod();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getPeriod <em>Period</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Period</em>' attribute.
	 * @see com.mmxlabs.models.lng.cargo.InventoryFrequency
	 * @see #getPeriod()
	 * @generated
	 */
	void setPeriod(InventoryFrequency value);

	/**
	 * Returns the value of the '<em><b>Counter Party</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Counter Party</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Counter Party</em>' attribute.
	 * @see #setCounterParty(String)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventoryEventRow_CounterParty()
	 * @model
	 * @generated
	 */
	String getCounterParty();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getCounterParty <em>Counter Party</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Counter Party</em>' attribute.
	 * @see #getCounterParty()
	 * @generated
	 */
	void setCounterParty(String value);

	/**
	 * Returns the value of the '<em><b>Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume</em>' attribute.
	 * @see #setVolume(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventoryEventRow_Volume()
	 * @model
	 * @generated
	 */
	int getVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getVolume <em>Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume</em>' attribute.
	 * @see #getVolume()
	 * @generated
	 */
	void setVolume(int value);

} // InventoryEventRow
