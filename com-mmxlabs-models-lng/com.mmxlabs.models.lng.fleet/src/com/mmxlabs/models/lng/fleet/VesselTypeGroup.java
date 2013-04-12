/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.AVesselSet;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel Type Group</b></em>'.
 * @since 2.0
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselTypeGroup#getVesselType <em>Vessel Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselTypeGroup()
 * @model
 * @generated
 */
public interface VesselTypeGroup extends AVesselSet {
	/**
	 * Returns the value of the '<em><b>Vessel Type</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.fleet.VesselType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.fleet.VesselType
	 * @see #setVesselType(VesselType)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselTypeGroup_VesselType()
	 * @model required="true"
	 * @generated
	 */
	VesselType getVesselType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselTypeGroup#getVesselType <em>Vessel Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.fleet.VesselType
	 * @see #getVesselType()
	 * @generated
	 */
	void setVesselType(VesselType value);

} // end of  VesselTypeGroup

// finish type fixing
