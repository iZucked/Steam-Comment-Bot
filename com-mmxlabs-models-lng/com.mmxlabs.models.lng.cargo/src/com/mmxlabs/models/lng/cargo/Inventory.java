/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.mmxcore.NamedObject;
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
 *   <li>{@link com.mmxlabs.models.lng.cargo.Inventory#getPort <em>Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Inventory#getFeeds <em>Feeds</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Inventory#getOfftakes <em>Offtakes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Inventory#getCapacities <em>Capacities</em>}</li>
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
