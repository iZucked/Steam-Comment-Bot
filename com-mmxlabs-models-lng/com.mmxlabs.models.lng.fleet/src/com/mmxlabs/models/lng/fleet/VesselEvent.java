

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.fleet;
import com.mmxlabs.models.lng.types.AVesselEvent;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.ITimezoneProvider;
import com.mmxlabs.models.lng.port.Port;

import java.util.Date;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel Event</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselEvent#getDurationInDays <em>Duration In Days</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselEvent#getAllowedVessels <em>Allowed Vessels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselEvent#getPort <em>Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselEvent#getEarliestStartDate <em>Earliest Start Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselEvent#getLatestStartDate <em>Latest Start Date</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselEvent()
 * @model abstract="true"
 * @generated
 */
public interface VesselEvent extends AVesselEvent, ITimezoneProvider {
	/**
	 * Returns the value of the '<em><b>Duration In Days</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Duration In Days</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Duration In Days</em>' attribute.
	 * @see #setDurationInDays(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselEvent_DurationInDays()
	 * @model required="true"
	 * @generated
	 */
	int getDurationInDays();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselEvent#getDurationInDays <em>Duration In Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Duration In Days</em>' attribute.
	 * @see #getDurationInDays()
	 * @generated
	 */
	void setDurationInDays(int value);

	/**
	 * Returns the value of the '<em><b>Allowed Vessels</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.AVesselSet}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Allowed Vessels</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Allowed Vessels</em>' reference list.
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselEvent_AllowedVessels()
	 * @model
	 * @generated
	 */
	EList<AVesselSet> getAllowedVessels();

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
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselEvent_Port()
	 * @model required="true"
	 * @generated
	 */
	Port getPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselEvent#getPort <em>Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port</em>' reference.
	 * @see #getPort()
	 * @generated
	 */
	void setPort(Port value);

	/**
	 * Returns the value of the '<em><b>Earliest Start Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Earliest Start Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Earliest Start Date</em>' attribute.
	 * @see #setEarliestStartDate(Date)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselEvent_EarliestStartDate()
	 * @model required="true"
	 * @generated
	 */
	Date getEarliestStartDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselEvent#getEarliestStartDate <em>Earliest Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Earliest Start Date</em>' attribute.
	 * @see #getEarliestStartDate()
	 * @generated
	 */
	void setEarliestStartDate(Date value);

	/**
	 * Returns the value of the '<em><b>Latest Start Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Latest Start Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Latest Start Date</em>' attribute.
	 * @see #setLatestStartDate(Date)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselEvent_LatestStartDate()
	 * @model required="true"
	 * @generated
	 */
	Date getLatestStartDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselEvent#getLatestStartDate <em>Latest Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Latest Start Date</em>' attribute.
	 * @see #getLatestStartDate()
	 * @generated
	 */
	void setLatestStartDate(Date value);

} // end of  VesselEvent

// finish type fixing
