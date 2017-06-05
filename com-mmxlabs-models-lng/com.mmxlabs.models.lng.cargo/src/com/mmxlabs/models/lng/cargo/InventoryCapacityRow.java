/**
 */
package com.mmxlabs.models.lng.cargo;

import java.time.LocalDate;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Inventory Capacity Row</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.InventoryCapacityRow#getDate <em>Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.InventoryCapacityRow#getMinVolume <em>Min Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.InventoryCapacityRow#getMaxVolume <em>Max Volume</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventoryCapacityRow()
 * @model
 * @generated
 */
public interface InventoryCapacityRow extends EObject {
	/**
	 * Returns the value of the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Date</em>' attribute.
	 * @see #setDate(LocalDate)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventoryCapacityRow_Date()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.InventoryCapacityRow#getDate <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Date</em>' attribute.
	 * @see #getDate()
	 * @generated
	 */
	void setDate(LocalDate value);

	/**
	 * Returns the value of the '<em><b>Min Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Min Volume</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Volume</em>' attribute.
	 * @see #setMinVolume(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventoryCapacityRow_MinVolume()
	 * @model
	 * @generated
	 */
	int getMinVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.InventoryCapacityRow#getMinVolume <em>Min Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Volume</em>' attribute.
	 * @see #getMinVolume()
	 * @generated
	 */
	void setMinVolume(int value);

	/**
	 * Returns the value of the '<em><b>Max Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Volume</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Volume</em>' attribute.
	 * @see #setMaxVolume(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventoryCapacityRow_MaxVolume()
	 * @model
	 * @generated
	 */
	int getMaxVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.InventoryCapacityRow#getMaxVolume <em>Max Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Volume</em>' attribute.
	 * @see #getMaxVolume()
	 * @generated
	 */
	void setMaxVolume(int value);

} // InventoryCapacityRow
