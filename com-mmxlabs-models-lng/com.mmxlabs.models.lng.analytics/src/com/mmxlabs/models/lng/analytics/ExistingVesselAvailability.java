/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.cargo.VesselAvailability;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Existing Vessel Availability</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ExistingVesselAvailability#getVesselAvailability <em>Vessel Availability</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getExistingVesselAvailability()
 * @model
 * @generated
 */
public interface ExistingVesselAvailability extends ShippingOption {
	/**
	 * Returns the value of the '<em><b>Vessel Availability</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Availability</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Availability</em>' reference.
	 * @see #setVesselAvailability(VesselAvailability)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getExistingVesselAvailability_VesselAvailability()
	 * @model
	 * @generated
	 */
	VesselAvailability getVesselAvailability();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ExistingVesselAvailability#getVesselAvailability <em>Vessel Availability</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Availability</em>' reference.
	 * @see #getVesselAvailability()
	 * @generated
	 */
	void setVesselAvailability(VesselAvailability value);

} // ExistingVesselAvailability
