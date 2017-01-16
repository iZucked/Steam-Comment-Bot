/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo;

import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.types.AVesselSet;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel Type Group</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselTypeGroup#getVesselType <em>Vessel Type</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselTypeGroup()
 * @model
 * @generated
 */
public interface VesselTypeGroup extends AVesselSet<Vessel> {
	/**
	 * Returns the value of the '<em><b>Vessel Type</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.cargo.VesselType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.cargo.VesselType
	 * @see #setVesselType(VesselType)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselTypeGroup_VesselType()
	 * @model required="true"
	 * @generated
	 */
	VesselType getVesselType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselTypeGroup#getVesselType <em>Vessel Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.cargo.VesselType
	 * @see #getVesselType()
	 * @generated
	 */
	void setVesselType(VesselType value);

} // VesselTypeGroup
