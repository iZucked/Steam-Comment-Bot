/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo;

import com.mmxlabs.models.mmxcore.NamedObject;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Deal Set</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.DealSet#getSlots <em>Slots</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.DealSet#getPaperDeals <em>Paper Deals</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getDealSet()
 * @model
 * @generated
 */
public interface DealSet extends NamedObject {
	/**
	 * Returns the value of the '<em><b>Slots</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.Slot}<code>&lt;?&gt;</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slots</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slots</em>' reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getDealSet_Slots()
	 * @model
	 * @generated
	 */
	EList<Slot<?>> getSlots();

	/**
	 * Returns the value of the '<em><b>Paper Deals</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.PaperDeal}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Paper Deals</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Paper Deals</em>' reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getDealSet_PaperDeals()
	 * @model
	 * @generated
	 */
	EList<PaperDeal> getPaperDeals();

} // DealSet
