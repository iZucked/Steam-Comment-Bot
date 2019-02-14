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
 * A representation of the model object '<em><b>New Vessel Availability</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.NewVesselAvailability#getVesselAvailability <em>Vessel Availability</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getNewVesselAvailability()
 * @model
 * @generated
 */
public interface NewVesselAvailability extends ShippingOption {
	/**
	 * Returns the value of the '<em><b>Vessel Availability</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Availability</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Availability</em>' containment reference.
	 * @see #setVesselAvailability(VesselAvailability)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getNewVesselAvailability_VesselAvailability()
	 * @model containment="true"
	 * @generated
	 */
	VesselAvailability getVesselAvailability();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.NewVesselAvailability#getVesselAvailability <em>Vessel Availability</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Availability</em>' containment reference.
	 * @see #getVesselAvailability()
	 * @generated
	 */
	void setVesselAvailability(VesselAvailability value);

} // NewVesselAvailability
