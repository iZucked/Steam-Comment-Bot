/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Wiring Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getLoadSlot_base <em>Load Slot base</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getLoadSlot_target <em>Load Slot target</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getOriginalDischargeSlot <em>Original Discharge Slot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getNewDischargeSlot <em>New Discharge Slot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getOriginalLoadAllocation <em>Original Load Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getNewLoadAllocation <em>New Load Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getOriginalDischargeAllocation <em>Original Discharge Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getNewDischargeAllocation <em>New Discharge Allocation</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getWiringChange()
 * @model
 * @generated
 */
public interface WiringChange extends Change {
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
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getWiringChange_LoadSlot_base()
	 * @model
	 * @generated
	 */
	LoadSlot getLoadSlot_base();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getLoadSlot_base <em>Load Slot base</em>}' reference.
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
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getWiringChange_LoadSlot_target()
	 * @model
	 * @generated
	 */
	LoadSlot getLoadSlot_target();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getLoadSlot_target <em>Load Slot target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Load Slot target</em>' reference.
	 * @see #getLoadSlot_target()
	 * @generated
	 */
	void setLoadSlot_target(LoadSlot value);

	/**
	 * Returns the value of the '<em><b>Original Discharge Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Original Discharge Slot</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Original Discharge Slot</em>' reference.
	 * @see #setOriginalDischargeSlot(DischargeSlot)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getWiringChange_OriginalDischargeSlot()
	 * @model
	 * @generated
	 */
	DischargeSlot getOriginalDischargeSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getOriginalDischargeSlot <em>Original Discharge Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Original Discharge Slot</em>' reference.
	 * @see #getOriginalDischargeSlot()
	 * @generated
	 */
	void setOriginalDischargeSlot(DischargeSlot value);

	/**
	 * Returns the value of the '<em><b>New Discharge Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Discharge Slot</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Discharge Slot</em>' reference.
	 * @see #setNewDischargeSlot(DischargeSlot)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getWiringChange_NewDischargeSlot()
	 * @model
	 * @generated
	 */
	DischargeSlot getNewDischargeSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getNewDischargeSlot <em>New Discharge Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Discharge Slot</em>' reference.
	 * @see #getNewDischargeSlot()
	 * @generated
	 */
	void setNewDischargeSlot(DischargeSlot value);

	/**
	 * Returns the value of the '<em><b>Original Load Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Original Load Allocation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Original Load Allocation</em>' reference.
	 * @see #setOriginalLoadAllocation(com.mmxlabs.models.lng.schedule.SlotAllocation)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getWiringChange_OriginalLoadAllocation()
	 * @model
	 * @generated
	 */
	com.mmxlabs.models.lng.schedule.SlotAllocation getOriginalLoadAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getOriginalLoadAllocation <em>Original Load Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Original Load Allocation</em>' reference.
	 * @see #getOriginalLoadAllocation()
	 * @generated
	 */
	void setOriginalLoadAllocation(com.mmxlabs.models.lng.schedule.SlotAllocation value);

	/**
	 * Returns the value of the '<em><b>New Load Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Load Allocation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Load Allocation</em>' reference.
	 * @see #setNewLoadAllocation(com.mmxlabs.models.lng.schedule.SlotAllocation)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getWiringChange_NewLoadAllocation()
	 * @model
	 * @generated
	 */
	com.mmxlabs.models.lng.schedule.SlotAllocation getNewLoadAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getNewLoadAllocation <em>New Load Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Load Allocation</em>' reference.
	 * @see #getNewLoadAllocation()
	 * @generated
	 */
	void setNewLoadAllocation(com.mmxlabs.models.lng.schedule.SlotAllocation value);

	/**
	 * Returns the value of the '<em><b>Original Discharge Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Original Discharge Allocation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Original Discharge Allocation</em>' reference.
	 * @see #setOriginalDischargeAllocation(com.mmxlabs.models.lng.schedule.SlotAllocation)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getWiringChange_OriginalDischargeAllocation()
	 * @model
	 * @generated
	 */
	com.mmxlabs.models.lng.schedule.SlotAllocation getOriginalDischargeAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getOriginalDischargeAllocation <em>Original Discharge Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Original Discharge Allocation</em>' reference.
	 * @see #getOriginalDischargeAllocation()
	 * @generated
	 */
	void setOriginalDischargeAllocation(com.mmxlabs.models.lng.schedule.SlotAllocation value);

	/**
	 * Returns the value of the '<em><b>New Discharge Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Discharge Allocation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Discharge Allocation</em>' reference.
	 * @see #setNewDischargeAllocation(com.mmxlabs.models.lng.schedule.SlotAllocation)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getWiringChange_NewDischargeAllocation()
	 * @model
	 * @generated
	 */
	com.mmxlabs.models.lng.schedule.SlotAllocation getNewDischargeAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.WiringChange#getNewDischargeAllocation <em>New Discharge Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Discharge Allocation</em>' reference.
	 * @see #getNewDischargeAllocation()
	 * @generated
	 */
	void setNewDischargeAllocation(com.mmxlabs.models.lng.schedule.SlotAllocation value);

} // WiringChange
