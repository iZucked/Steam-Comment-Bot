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
 *   <li>{@link com.mmxlabs.models.lng.adp.InventoryProfile#getRelativeEntitlements <em>Relative Entitlements</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.InventoryProfile#getInventory <em>Inventory</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.InventoryProfile#getGeneratedSlots <em>Generated Slots</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.InventoryProfile#getVolume <em>Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.InventoryProfile#getInitialAllocations <em>Initial Allocations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.InventoryProfile#getEntityTable <em>Entity Table</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getInventoryProfile()
 * @model
 * @generated
 */
public interface InventoryProfile extends EObject {
	/**
	 * Returns the value of the '<em><b>Relative Entitlements</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.adp.RelativeEntitlementElement}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Relative Entitlements</em>' reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getInventoryProfile_RelativeEntitlements()
	 * @model
	 * @generated
	 */
	EList<RelativeEntitlementElement> getRelativeEntitlements();

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
	 * Returns the value of the '<em><b>Generated Slots</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.LoadSlot}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Generated Slots</em>' reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getInventoryProfile_GeneratedSlots()
	 * @model
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
	 * Returns the value of the '<em><b>Initial Allocations</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.adp.AllocationElement}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Initial Allocations</em>' reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getInventoryProfile_InitialAllocations()
	 * @model
	 * @generated
	 */
	EList<AllocationElement> getInitialAllocations();

	/**
	 * Returns the value of the '<em><b>Entity Table</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.adp.InventoryADPEntityRow}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entity Table</em>' reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getInventoryProfile_EntityTable()
	 * @model
	 * @generated
	 */
	EList<InventoryADPEntityRow> getEntityTable();

} // InventoryProfile
