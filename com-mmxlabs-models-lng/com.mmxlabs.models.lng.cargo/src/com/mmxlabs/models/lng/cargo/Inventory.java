/**
 */
package com.mmxlabs.models.lng.cargo;

import java.time.LocalDate;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Inventory</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Inventory#getStartDate <em>Start Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Inventory#getStartVolume <em>Start Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Inventory#getFeeds <em>Feeds</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Inventory#getOfftakes <em>Offtakes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Inventory#getCapacities <em>Capacities</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Inventory#getName <em>Name</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventory()
 * @model
 * @generated
 */
public interface Inventory extends EObject {
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
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventory_StartDate()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getStartDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Inventory#getStartDate <em>Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Date</em>' attribute.
	 * @see #getStartDate()
	 * @generated
	 */
	void setStartDate(LocalDate value);

	/**
	 * Returns the value of the '<em><b>Start Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Volume</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Volume</em>' attribute.
	 * @see #setStartVolume(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventory_StartVolume()
	 * @model
	 * @generated
	 */
	int getStartVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Inventory#getStartVolume <em>Start Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Volume</em>' attribute.
	 * @see #getStartVolume()
	 * @generated
	 */
	void setStartVolume(int value);

	/**
	 * Returns the value of the '<em><b>Feeds</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.InventoryEventRow}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Feeds</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Feeds</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventory_Feeds()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<InventoryEventRow> getFeeds();

	/**
	 * Returns the value of the '<em><b>Offtakes</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.InventoryEventRow}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Offtakes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Offtakes</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventory_Offtakes()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<InventoryEventRow> getOfftakes();

	/**
	 * Returns the value of the '<em><b>Capacities</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.InventoryCapacityRow}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Capacities</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Capacities</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventory_Capacities()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<InventoryCapacityRow> getCapacities();

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventory_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Inventory#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

} // Inventory
