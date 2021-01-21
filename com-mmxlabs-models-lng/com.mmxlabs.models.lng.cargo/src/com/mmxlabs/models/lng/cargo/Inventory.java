/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.mmxcore.NamedObject;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Inventory</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Inventory#getPort <em>Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Inventory#getFeeds <em>Feeds</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Inventory#getOfftakes <em>Offtakes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Inventory#getCapacities <em>Capacities</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Inventory#getFacilityType <em>Facility Type</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventory()
 * @model
 * @generated
 */
public interface Inventory extends NamedObject {
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
	 * Returns the value of the '<em><b>Facility Type</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.cargo.InventoryFacilityType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Facility Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Facility Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.cargo.InventoryFacilityType
	 * @see #setFacilityType(InventoryFacilityType)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventory_FacilityType()
	 * @model
	 * @generated
	 */
	InventoryFacilityType getFacilityType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Inventory#getFacilityType <em>Facility Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Facility Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.cargo.InventoryFacilityType
	 * @see #getFacilityType()
	 * @generated
	 */
	void setFacilityType(InventoryFacilityType value);

	/**
	 * Returns the value of the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port</em>' reference.
	 * @see #setPort(Port)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventory_Port()
	 * @model
	 * @generated
	 */
	Port getPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Inventory#getPort <em>Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port</em>' reference.
	 * @see #getPort()
	 * @generated
	 */
	void setPort(Port value);

} // Inventory
