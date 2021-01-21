/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>New Vessel Availability</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.FullVesselCharterOption#getVesselCharter <em>Vessel Charter</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getFullVesselCharterOption()
 * @model
 * @generated
 */
public interface FullVesselCharterOption extends UUIDObject, ShippingOption {
	/**
	 * Returns the value of the '<em><b>Vessel Charter</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Availability</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Charter</em>' containment reference.
	 * @see #setVesselCharter(VesselAvailability)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getFullVesselCharterOption_VesselCharter()
	 * @model containment="true"
	 * @generated
	 */
	VesselAvailability getVesselCharter();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.FullVesselCharterOption#getVesselCharter <em>Vessel Charter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Charter</em>' containment reference.
	 * @see #getVesselCharter()
	 * @generated
	 */
	void setVesselCharter(VesselAvailability value);

} // NewVesselAvailability
