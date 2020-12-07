/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.cargo.Inventory;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Inventory Subprofile</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.InventorySubprofile#getInventory <em>Inventory</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.InventorySubprofile#getEntityTable <em>Entity Table</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getInventorySubprofile()
 * @model
 * @generated
 */
public interface InventorySubprofile extends EObject {
	/**
	 * Returns the value of the '<em><b>Inventory</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Inventory</em>' reference.
	 * @see #setInventory(Inventory)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getInventorySubprofile_Inventory()
	 * @model
	 * @generated
	 */
	Inventory getInventory();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.InventorySubprofile#getInventory <em>Inventory</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Inventory</em>' reference.
	 * @see #getInventory()
	 * @generated
	 */
	void setInventory(Inventory value);

	/**
	 * Returns the value of the '<em><b>Entity Table</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.adp.InventoryADPEntityRow}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entity Table</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getInventorySubprofile_EntityTable()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<InventoryADPEntityRow> getEntityTable();

} // InventorySubprofile
