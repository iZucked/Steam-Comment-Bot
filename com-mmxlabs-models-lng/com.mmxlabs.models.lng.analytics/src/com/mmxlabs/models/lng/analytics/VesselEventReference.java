/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.cargo.VesselEvent;

import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel Event Reference</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.VesselEventReference#getEvent <em>Event</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getVesselEventReference()
 * @model
 * @generated
 */
public interface VesselEventReference extends UUIDObject, VesselEventOption {
	/**
	 * Returns the value of the '<em><b>Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Event</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Event</em>' reference.
	 * @see #setEvent(VesselEvent)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getVesselEventReference_Event()
	 * @model
	 * @generated
	 */
	VesselEvent getEvent();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.VesselEventReference#getEvent <em>Event</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Event</em>' reference.
	 * @see #getEvent()
	 * @generated
	 */
	void setEvent(VesselEvent value);

} // VesselEventReference
