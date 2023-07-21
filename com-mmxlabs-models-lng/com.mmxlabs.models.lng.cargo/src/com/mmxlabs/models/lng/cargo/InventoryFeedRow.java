/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Inventory Feed Row</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.InventoryFeedRow#getCv <em>Cv</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventoryFeedRow()
 * @model
 * @generated
 */
public interface InventoryFeedRow extends InventoryEventRow {
	/**
	 * Returns the value of the '<em><b>Cv</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cv</em>' attribute.
	 * @see #setCv(double)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventoryFeedRow_Cv()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='#0.###'"
	 * @generated
	 */
	double getCv();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.InventoryFeedRow#getCv <em>Cv</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cv</em>' attribute.
	 * @see #getCv()
	 * @generated
	 */
	void setCv(double value);

} // InventoryFeedRow
