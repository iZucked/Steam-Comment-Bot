/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model;

import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.types.VesselAssignmentType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Event Vessel Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange#getOriginalEvent <em>Original Event</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange#getNewEvent <em>New Event</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange#getOriginalVessel <em>Original Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange#getNewVessel <em>New Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange#getEventName <em>Event Name</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getEventVesselChange()
 * @model
 * @generated
 */
public interface EventVesselChange extends Change {
	/**
	 * Returns the value of the '<em><b>Original Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Original Event</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Original Event</em>' reference.
	 * @see #setOriginalEvent(Event)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getEventVesselChange_OriginalEvent()
	 * @model
	 * @generated
	 */
	Event getOriginalEvent();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange#getOriginalEvent <em>Original Event</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Original Event</em>' reference.
	 * @see #getOriginalEvent()
	 * @generated
	 */
	void setOriginalEvent(Event value);

	/**
	 * Returns the value of the '<em><b>New Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Event</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Event</em>' reference.
	 * @see #setNewEvent(Event)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getEventVesselChange_NewEvent()
	 * @model
	 * @generated
	 */
	Event getNewEvent();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange#getNewEvent <em>New Event</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Event</em>' reference.
	 * @see #getNewEvent()
	 * @generated
	 */
	void setNewEvent(Event value);

	/**
	 * Returns the value of the '<em><b>Original Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Original Vessel</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Original Vessel</em>' reference.
	 * @see #setOriginalVessel(VesselAssignmentType)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getEventVesselChange_OriginalVessel()
	 * @model
	 * @generated
	 */
	VesselAssignmentType getOriginalVessel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange#getOriginalVessel <em>Original Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Original Vessel</em>' reference.
	 * @see #getOriginalVessel()
	 * @generated
	 */
	void setOriginalVessel(VesselAssignmentType value);

	/**
	 * Returns the value of the '<em><b>New Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Vessel</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Vessel</em>' reference.
	 * @see #setNewVessel(VesselAssignmentType)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getEventVesselChange_NewVessel()
	 * @model
	 * @generated
	 */
	VesselAssignmentType getNewVessel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange#getNewVessel <em>New Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Vessel</em>' reference.
	 * @see #getNewVessel()
	 * @generated
	 */
	void setNewVessel(VesselAssignmentType value);

	/**
	 * Returns the value of the '<em><b>Event Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Event Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Event Name</em>' attribute.
	 * @see #setEventName(String)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getEventVesselChange_EventName()
	 * @model
	 * @generated
	 */
	String getEventName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange#getEventName <em>Event Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Event Name</em>' attribute.
	 * @see #getEventName()
	 * @generated
	 */
	void setEventName(String value);

} // EventVesselChange
