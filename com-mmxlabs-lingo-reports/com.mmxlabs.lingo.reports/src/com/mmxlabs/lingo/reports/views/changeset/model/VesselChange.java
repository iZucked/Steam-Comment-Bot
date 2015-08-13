/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;

import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.types.VesselAssignmentType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getLoadSlot_base <em>Load Slot base</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getLoadSlot_target <em>Load Slot target</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getOriginalVessel <em>Original Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getNewVessel <em>New Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getDischargeSlot_base <em>Discharge Slot base</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getDischargeSlot_target <em>Discharge Slot target</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getOriginalLoadAllocation <em>Original Load Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getNewLoadAllocation <em>New Load Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getOriginalDischargeAllocation <em>Original Discharge Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getNewDischargeAllocation <em>New Discharge Allocation</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getVesselChange()
 * @model
 * @generated
 */
public interface VesselChange extends Change {
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
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getVesselChange_LoadSlot_base()
	 * @model
	 * @generated
	 */
	LoadSlot getLoadSlot_base();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getLoadSlot_base <em>Load Slot base</em>}' reference.
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
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getVesselChange_LoadSlot_target()
	 * @model
	 * @generated
	 */
	LoadSlot getLoadSlot_target();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getLoadSlot_target <em>Load Slot target</em>}' reference.
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
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getVesselChange_OriginalVessel()
	 * @model
	 * @generated
	 */
	VesselAssignmentType getOriginalVessel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getOriginalVessel <em>Original Vessel</em>}' reference.
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
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getVesselChange_NewVessel()
	 * @model
	 * @generated
	 */
	VesselAssignmentType getNewVessel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getNewVessel <em>New Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Vessel</em>' reference.
	 * @see #getNewVessel()
	 * @generated
	 */
	void setNewVessel(VesselAssignmentType value);

	/**
	 * Returns the value of the '<em><b>Discharge Slot base</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Discharge Slot base</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discharge Slot base</em>' reference.
	 * @see #setDischargeSlot_base(DischargeSlot)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getVesselChange_DischargeSlot_base()
	 * @model
	 * @generated
	 */
	DischargeSlot getDischargeSlot_base();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getDischargeSlot_base <em>Discharge Slot base</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discharge Slot base</em>' reference.
	 * @see #getDischargeSlot_base()
	 * @generated
	 */
	void setDischargeSlot_base(DischargeSlot value);

	/**
	 * Returns the value of the '<em><b>Discharge Slot target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Discharge Slot target</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discharge Slot target</em>' reference.
	 * @see #setDischargeSlot_target(DischargeSlot)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getVesselChange_DischargeSlot_target()
	 * @model
	 * @generated
	 */
	DischargeSlot getDischargeSlot_target();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getDischargeSlot_target <em>Discharge Slot target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discharge Slot target</em>' reference.
	 * @see #getDischargeSlot_target()
	 * @generated
	 */
	void setDischargeSlot_target(DischargeSlot value);

	/**
	 * Returns the value of the '<em><b>Original Load Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Original Load Allocation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Original Load Allocation</em>' reference.
	 * @see #setOriginalLoadAllocation(SlotAllocation)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getVesselChange_OriginalLoadAllocation()
	 * @model
	 * @generated
	 */
	SlotAllocation getOriginalLoadAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getOriginalLoadAllocation <em>Original Load Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Original Load Allocation</em>' reference.
	 * @see #getOriginalLoadAllocation()
	 * @generated
	 */
	void setOriginalLoadAllocation(SlotAllocation value);

	/**
	 * Returns the value of the '<em><b>New Load Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Load Allocation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Load Allocation</em>' reference.
	 * @see #setNewLoadAllocation(SlotAllocation)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getVesselChange_NewLoadAllocation()
	 * @model
	 * @generated
	 */
	SlotAllocation getNewLoadAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getNewLoadAllocation <em>New Load Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Load Allocation</em>' reference.
	 * @see #getNewLoadAllocation()
	 * @generated
	 */
	void setNewLoadAllocation(SlotAllocation value);

	/**
	 * Returns the value of the '<em><b>Original Discharge Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Original Discharge Allocation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Original Discharge Allocation</em>' reference.
	 * @see #setOriginalDischargeAllocation(SlotAllocation)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getVesselChange_OriginalDischargeAllocation()
	 * @model
	 * @generated
	 */
	SlotAllocation getOriginalDischargeAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getOriginalDischargeAllocation <em>Original Discharge Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Original Discharge Allocation</em>' reference.
	 * @see #getOriginalDischargeAllocation()
	 * @generated
	 */
	void setOriginalDischargeAllocation(SlotAllocation value);

	/**
	 * Returns the value of the '<em><b>New Discharge Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Discharge Allocation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Discharge Allocation</em>' reference.
	 * @see #setNewDischargeAllocation(SlotAllocation)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getVesselChange_NewDischargeAllocation()
	 * @model
	 * @generated
	 */
	SlotAllocation getNewDischargeAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.VesselChange#getNewDischargeAllocation <em>New Discharge Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Discharge Allocation</em>' reference.
	 * @see #getNewDischargeAllocation()
	 * @generated
	 */
	void setNewDischargeAllocation(SlotAllocation value);

} // VesselChange
