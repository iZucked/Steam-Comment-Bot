/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model;

import com.mmxlabs.models.lng.cargo.LoadSlot;

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
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange#getLoadSlot_base <em>Load Slot base</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange#getLoadSlot_target <em>Load Slot target</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange#getOriginalVessel <em>Original Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange#getNewVessel <em>New Vessel</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getEventVesselChange()
 * @model
 * @generated
 */
public interface EventVesselChange extends Change {
	/**
	 * Returns the value of the '<em><b>Load Slot base</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Load Slot base</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Load Slot base</em>' reference.
	 * @see #setLoadSlot_base(LoadSlot)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getEventVesselChange_LoadSlot_base()
	 * @model
	 * @generated
	 */
	LoadSlot getLoadSlot_base();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange#getLoadSlot_base <em>Load Slot base</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Load Slot base</em>' reference.
	 * @see #getLoadSlot_base()
	 * @generated
	 */
	void setLoadSlot_base(LoadSlot value);

	/**
	 * Returns the value of the '<em><b>Load Slot target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Load Slot target</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Load Slot target</em>' reference.
	 * @see #setLoadSlot_target(LoadSlot)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getEventVesselChange_LoadSlot_target()
	 * @model
	 * @generated
	 */
	LoadSlot getLoadSlot_target();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange#getLoadSlot_target <em>Load Slot target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Load Slot target</em>' reference.
	 * @see #getLoadSlot_target()
	 * @generated
	 */
	void setLoadSlot_target(LoadSlot value);

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

} // EventVesselChange
