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
 * A representation of the model object '<em><b>Existing Vessel Availability</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption#getVesselCharter <em>Vessel Charter</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getExistingVesselCharterOption()
 * @model
 * @generated
 */
public interface ExistingVesselCharterOption extends UUIDObject, ShippingOption {
	/**
	 * Returns the value of the '<em><b>Vessel Charter</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Availability</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Charter</em>' reference.
	 * @see #setVesselCharter(VesselAvailability)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getExistingVesselCharterOption_VesselCharter()
	 * @model
	 * @generated
	 */
	VesselAvailability getVesselCharter();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption#getVesselCharter <em>Vessel Charter</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Charter</em>' reference.
	 * @see #getVesselCharter()
	 * @generated
	 */
	void setVesselCharter(VesselAvailability value);

} // ExistingVesselAvailability
