/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Change Set Row</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getLhsName <em>Lhs Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getRhsName <em>Rhs Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getLhsVesselName <em>Lhs Vessel Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getRhsVesselName <em>Rhs Vessel Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getLhsWiringLink <em>Lhs Wiring Link</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getRhsWiringLink <em>Rhs Wiring Link</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getLhsWiringChange <em>Lhs Wiring Change</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getRhsWiringChange <em>Rhs Wiring Change</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getLhsVesselChange <em>Lhs Vessel Change</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getLoadSlot <em>Load Slot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getDischargeSlot <em>Discharge Slot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalLoadAllocation <em>Original Load Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewLoadAllocation <em>New Load Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalDischargeAllocation <em>Original Discharge Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewDischargeAllocation <em>New Discharge Allocation</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow()
 * @model
 * @generated
 */
public interface ChangeSetRow extends EObject {
	/**
	 * Returns the value of the '<em><b>Lhs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lhs Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lhs Name</em>' attribute.
	 * @see #setLhsName(String)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_LhsName()
	 * @model
	 * @generated
	 */
	String getLhsName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getLhsName <em>Lhs Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lhs Name</em>' attribute.
	 * @see #getLhsName()
	 * @generated
	 */
	void setLhsName(String value);

	/**
	 * Returns the value of the '<em><b>Rhs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rhs Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rhs Name</em>' attribute.
	 * @see #setRhsName(String)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_RhsName()
	 * @model
	 * @generated
	 */
	String getRhsName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getRhsName <em>Rhs Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rhs Name</em>' attribute.
	 * @see #getRhsName()
	 * @generated
	 */
	void setRhsName(String value);

	/**
	 * Returns the value of the '<em><b>Lhs Vessel Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lhs Vessel Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lhs Vessel Name</em>' attribute.
	 * @see #setLhsVesselName(String)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_LhsVesselName()
	 * @model
	 * @generated
	 */
	String getLhsVesselName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getLhsVesselName <em>Lhs Vessel Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lhs Vessel Name</em>' attribute.
	 * @see #getLhsVesselName()
	 * @generated
	 */
	void setLhsVesselName(String value);

	/**
	 * Returns the value of the '<em><b>Rhs Vessel Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rhs Vessel Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rhs Vessel Name</em>' attribute.
	 * @see #setRhsVesselName(String)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_RhsVesselName()
	 * @model
	 * @generated
	 */
	String getRhsVesselName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getRhsVesselName <em>Rhs Vessel Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rhs Vessel Name</em>' attribute.
	 * @see #getRhsVesselName()
	 * @generated
	 */
	void setRhsVesselName(String value);

	/**
	 * Returns the value of the '<em><b>Lhs Wiring Link</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getRhsWiringLink <em>Rhs Wiring Link</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lhs Wiring Link</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lhs Wiring Link</em>' reference.
	 * @see #setLhsWiringLink(ChangeSetRow)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_LhsWiringLink()
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getRhsWiringLink
	 * @model opposite="rhsWiringLink"
	 * @generated
	 */
	ChangeSetRow getLhsWiringLink();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getLhsWiringLink <em>Lhs Wiring Link</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lhs Wiring Link</em>' reference.
	 * @see #getLhsWiringLink()
	 * @generated
	 */
	void setLhsWiringLink(ChangeSetRow value);

	/**
	 * Returns the value of the '<em><b>Rhs Wiring Link</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getLhsWiringLink <em>Lhs Wiring Link</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rhs Wiring Link</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rhs Wiring Link</em>' reference.
	 * @see #setRhsWiringLink(ChangeSetRow)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_RhsWiringLink()
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getLhsWiringLink
	 * @model opposite="lhsWiringLink"
	 * @generated
	 */
	ChangeSetRow getRhsWiringLink();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getRhsWiringLink <em>Rhs Wiring Link</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rhs Wiring Link</em>' reference.
	 * @see #getRhsWiringLink()
	 * @generated
	 */
	void setRhsWiringLink(ChangeSetRow value);

	/**
	 * Returns the value of the '<em><b>Lhs Wiring Change</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lhs Wiring Change</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lhs Wiring Change</em>' reference.
	 * @see #setLhsWiringChange(WiringChange)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_LhsWiringChange()
	 * @model
	 * @generated
	 */
	WiringChange getLhsWiringChange();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getLhsWiringChange <em>Lhs Wiring Change</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lhs Wiring Change</em>' reference.
	 * @see #getLhsWiringChange()
	 * @generated
	 */
	void setLhsWiringChange(WiringChange value);

	/**
	 * Returns the value of the '<em><b>Rhs Wiring Change</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rhs Wiring Change</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rhs Wiring Change</em>' reference.
	 * @see #setRhsWiringChange(WiringChange)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_RhsWiringChange()
	 * @model
	 * @generated
	 */
	WiringChange getRhsWiringChange();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getRhsWiringChange <em>Rhs Wiring Change</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rhs Wiring Change</em>' reference.
	 * @see #getRhsWiringChange()
	 * @generated
	 */
	void setRhsWiringChange(WiringChange value);

	/**
	 * Returns the value of the '<em><b>Lhs Vessel Change</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lhs Vessel Change</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lhs Vessel Change</em>' reference.
	 * @see #setLhsVesselChange(WiringChange)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_LhsVesselChange()
	 * @model
	 * @generated
	 */
	WiringChange getLhsVesselChange();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getLhsVesselChange <em>Lhs Vessel Change</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lhs Vessel Change</em>' reference.
	 * @see #getLhsVesselChange()
	 * @generated
	 */
	void setLhsVesselChange(WiringChange value);

	/**
	 * Returns the value of the '<em><b>Load Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Load Slot</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Load Slot</em>' reference.
	 * @see #setLoadSlot(LoadSlot)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_LoadSlot()
	 * @model
	 * @generated
	 */
	LoadSlot getLoadSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getLoadSlot <em>Load Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Load Slot</em>' reference.
	 * @see #getLoadSlot()
	 * @generated
	 */
	void setLoadSlot(LoadSlot value);

	/**
	 * Returns the value of the '<em><b>Discharge Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Discharge Slot</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discharge Slot</em>' reference.
	 * @see #setDischargeSlot(DischargeSlot)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_DischargeSlot()
	 * @model
	 * @generated
	 */
	DischargeSlot getDischargeSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getDischargeSlot <em>Discharge Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discharge Slot</em>' reference.
	 * @see #getDischargeSlot()
	 * @generated
	 */
	void setDischargeSlot(DischargeSlot value);

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
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_OriginalLoadAllocation()
	 * @model
	 * @generated
	 */
	com.mmxlabs.models.lng.schedule.SlotAllocation getOriginalLoadAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalLoadAllocation <em>Original Load Allocation</em>}' reference.
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
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_NewLoadAllocation()
	 * @model
	 * @generated
	 */
	com.mmxlabs.models.lng.schedule.SlotAllocation getNewLoadAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewLoadAllocation <em>New Load Allocation</em>}' reference.
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
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_OriginalDischargeAllocation()
	 * @model
	 * @generated
	 */
	com.mmxlabs.models.lng.schedule.SlotAllocation getOriginalDischargeAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalDischargeAllocation <em>Original Discharge Allocation</em>}' reference.
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
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_NewDischargeAllocation()
	 * @model
	 * @generated
	 */
	com.mmxlabs.models.lng.schedule.SlotAllocation getNewDischargeAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewDischargeAllocation <em>New Discharge Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Discharge Allocation</em>' reference.
	 * @see #getNewDischargeAllocation()
	 * @generated
	 */
	void setNewDischargeAllocation(com.mmxlabs.models.lng.schedule.SlotAllocation value);

} // ChangeSetRow
