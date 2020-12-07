/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Inventory Profile</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.InventoryProfile#getInventory <em>Inventory</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.InventoryProfile#getGeneratedSlots <em>Generated Slots</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.InventoryProfile#getVolume <em>Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.InventoryProfile#getEntityTable <em>Entity Table</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.InventoryProfile#getWindowSize <em>Window Size</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getInventoryProfile()
 * @model
 * @generated
 */
public interface InventoryProfile extends EObject {
	/**
	 * Returns the value of the '<em><b>Inventory</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Inventory</em>' reference.
	 * @see #setInventory(Inventory)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getInventoryProfile_Inventory()
	 * @model
	 * @generated
	 */
	Inventory getInventory();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.InventoryProfile#getInventory <em>Inventory</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Inventory</em>' reference.
	 * @see #getInventory()
	 * @generated
	 */
	void setInventory(Inventory value);

	/**
	 * Returns the value of the '<em><b>Generated Slots</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.LoadSlot}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Generated Slots</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getInventoryProfile_GeneratedSlots()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<LoadSlot> getGeneratedSlots();

	/**
	 * Returns the value of the '<em><b>Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume</em>' attribute.
	 * @see #setVolume(int)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getInventoryProfile_Volume()
	 * @model
	 * @generated
	 */
	int getVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.InventoryProfile#getVolume <em>Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume</em>' attribute.
	 * @see #getVolume()
	 * @generated
	 */
	void setVolume(int value);

	/**
	 * Returns the value of the '<em><b>Entity Table</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.adp.InventoryADPEntityRow}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entity Table</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getInventoryProfile_EntityTable()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<InventoryADPEntityRow> getEntityTable();

	/**
	 * Returns the value of the '<em><b>Window Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Window Size</em>' attribute.
	 * @see #setWindowSize(int)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getInventoryProfile_WindowSize()
	 * @model
	 * @generated
	 */
	int getWindowSize();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.InventoryProfile#getWindowSize <em>Window Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Window Size</em>' attribute.
	 * @see #getWindowSize()
	 * @generated
	 */
	void setWindowSize(int value);

} // InventoryProfile
