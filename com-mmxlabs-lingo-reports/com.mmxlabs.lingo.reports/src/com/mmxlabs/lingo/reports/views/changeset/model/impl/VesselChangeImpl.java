/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model.impl;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage;
import com.mmxlabs.lingo.reports.views.changeset.model.VesselChange;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;

import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.types.VesselAssignmentType;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vessel Change</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.VesselChangeImpl#getOriginalVessel <em>Original Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.VesselChangeImpl#getNewVessel <em>New Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.VesselChangeImpl#getOriginalLoadSlot <em>Original Load Slot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.VesselChangeImpl#getNewLoadSlot <em>New Load Slot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.VesselChangeImpl#getOriginalDischargeSlot <em>Original Discharge Slot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.VesselChangeImpl#getNewDischargeSlot <em>New Discharge Slot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.VesselChangeImpl#getOriginalLoadAllocation <em>Original Load Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.VesselChangeImpl#getNewLoadAllocation <em>New Load Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.VesselChangeImpl#getOriginalDischargeAllocation <em>Original Discharge Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.VesselChangeImpl#getNewDischargeAllocation <em>New Discharge Allocation</em>}</li>
 * </ul>
 *
 * @generated
 */
public class VesselChangeImpl extends ChangeImpl implements VesselChange {
	/**
	 * The cached value of the '{@link #getOriginalVessel() <em>Original Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginalVessel()
	 * @generated
	 * @ordered
	 */
	protected VesselAssignmentType originalVessel;

	/**
	 * The cached value of the '{@link #getNewVessel() <em>New Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewVessel()
	 * @generated
	 * @ordered
	 */
	protected VesselAssignmentType newVessel;

	/**
	 * The cached value of the '{@link #getOriginalLoadSlot() <em>Original Load Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginalLoadSlot()
	 * @generated
	 * @ordered
	 */
	protected LoadSlot originalLoadSlot;

	/**
	 * The cached value of the '{@link #getNewLoadSlot() <em>New Load Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewLoadSlot()
	 * @generated
	 * @ordered
	 */
	protected LoadSlot newLoadSlot;

	/**
	 * The cached value of the '{@link #getOriginalDischargeSlot() <em>Original Discharge Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginalDischargeSlot()
	 * @generated
	 * @ordered
	 */
	protected DischargeSlot originalDischargeSlot;

	/**
	 * The cached value of the '{@link #getNewDischargeSlot() <em>New Discharge Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewDischargeSlot()
	 * @generated
	 * @ordered
	 */
	protected DischargeSlot newDischargeSlot;

	/**
	 * The cached value of the '{@link #getOriginalLoadAllocation() <em>Original Load Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginalLoadAllocation()
	 * @generated
	 * @ordered
	 */
	protected SlotAllocation originalLoadAllocation;

	/**
	 * The cached value of the '{@link #getNewLoadAllocation() <em>New Load Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewLoadAllocation()
	 * @generated
	 * @ordered
	 */
	protected SlotAllocation newLoadAllocation;

	/**
	 * The cached value of the '{@link #getOriginalDischargeAllocation() <em>Original Discharge Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginalDischargeAllocation()
	 * @generated
	 * @ordered
	 */
	protected SlotAllocation originalDischargeAllocation;

	/**
	 * The cached value of the '{@link #getNewDischargeAllocation() <em>New Discharge Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewDischargeAllocation()
	 * @generated
	 * @ordered
	 */
	protected SlotAllocation newDischargeAllocation;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselChangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ChangesetPackage.Literals.VESSEL_CHANGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LoadSlot getNewLoadSlot() {
		if (newLoadSlot != null && newLoadSlot.eIsProxy()) {
			InternalEObject oldNewLoadSlot = (InternalEObject)newLoadSlot;
			newLoadSlot = (LoadSlot)eResolveProxy(oldNewLoadSlot);
			if (newLoadSlot != oldNewLoadSlot) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.VESSEL_CHANGE__NEW_LOAD_SLOT, oldNewLoadSlot, newLoadSlot));
			}
		}
		return newLoadSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LoadSlot basicGetNewLoadSlot() {
		return newLoadSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewLoadSlot(LoadSlot newNewLoadSlot) {
		LoadSlot oldNewLoadSlot = newLoadSlot;
		newLoadSlot = newNewLoadSlot;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.VESSEL_CHANGE__NEW_LOAD_SLOT, oldNewLoadSlot, newLoadSlot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DischargeSlot getOriginalDischargeSlot() {
		if (originalDischargeSlot != null && originalDischargeSlot.eIsProxy()) {
			InternalEObject oldOriginalDischargeSlot = (InternalEObject)originalDischargeSlot;
			originalDischargeSlot = (DischargeSlot)eResolveProxy(oldOriginalDischargeSlot);
			if (originalDischargeSlot != oldOriginalDischargeSlot) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.VESSEL_CHANGE__ORIGINAL_DISCHARGE_SLOT, oldOriginalDischargeSlot, originalDischargeSlot));
			}
		}
		return originalDischargeSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DischargeSlot basicGetOriginalDischargeSlot() {
		return originalDischargeSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOriginalDischargeSlot(DischargeSlot newOriginalDischargeSlot) {
		DischargeSlot oldOriginalDischargeSlot = originalDischargeSlot;
		originalDischargeSlot = newOriginalDischargeSlot;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.VESSEL_CHANGE__ORIGINAL_DISCHARGE_SLOT, oldOriginalDischargeSlot, originalDischargeSlot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DischargeSlot getNewDischargeSlot() {
		if (newDischargeSlot != null && newDischargeSlot.eIsProxy()) {
			InternalEObject oldNewDischargeSlot = (InternalEObject)newDischargeSlot;
			newDischargeSlot = (DischargeSlot)eResolveProxy(oldNewDischargeSlot);
			if (newDischargeSlot != oldNewDischargeSlot) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.VESSEL_CHANGE__NEW_DISCHARGE_SLOT, oldNewDischargeSlot, newDischargeSlot));
			}
		}
		return newDischargeSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DischargeSlot basicGetNewDischargeSlot() {
		return newDischargeSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewDischargeSlot(DischargeSlot newNewDischargeSlot) {
		DischargeSlot oldNewDischargeSlot = newDischargeSlot;
		newDischargeSlot = newNewDischargeSlot;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.VESSEL_CHANGE__NEW_DISCHARGE_SLOT, oldNewDischargeSlot, newDischargeSlot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselAssignmentType getOriginalVessel() {
		if (originalVessel != null && originalVessel.eIsProxy()) {
			InternalEObject oldOriginalVessel = (InternalEObject)originalVessel;
			originalVessel = (VesselAssignmentType)eResolveProxy(oldOriginalVessel);
			if (originalVessel != oldOriginalVessel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.VESSEL_CHANGE__ORIGINAL_VESSEL, oldOriginalVessel, originalVessel));
			}
		}
		return originalVessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselAssignmentType basicGetOriginalVessel() {
		return originalVessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOriginalVessel(VesselAssignmentType newOriginalVessel) {
		VesselAssignmentType oldOriginalVessel = originalVessel;
		originalVessel = newOriginalVessel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.VESSEL_CHANGE__ORIGINAL_VESSEL, oldOriginalVessel, originalVessel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselAssignmentType getNewVessel() {
		if (newVessel != null && newVessel.eIsProxy()) {
			InternalEObject oldNewVessel = (InternalEObject)newVessel;
			newVessel = (VesselAssignmentType)eResolveProxy(oldNewVessel);
			if (newVessel != oldNewVessel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.VESSEL_CHANGE__NEW_VESSEL, oldNewVessel, newVessel));
			}
		}
		return newVessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselAssignmentType basicGetNewVessel() {
		return newVessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewVessel(VesselAssignmentType newNewVessel) {
		VesselAssignmentType oldNewVessel = newVessel;
		newVessel = newNewVessel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.VESSEL_CHANGE__NEW_VESSEL, oldNewVessel, newVessel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LoadSlot getOriginalLoadSlot() {
		if (originalLoadSlot != null && originalLoadSlot.eIsProxy()) {
			InternalEObject oldOriginalLoadSlot = (InternalEObject)originalLoadSlot;
			originalLoadSlot = (LoadSlot)eResolveProxy(oldOriginalLoadSlot);
			if (originalLoadSlot != oldOriginalLoadSlot) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.VESSEL_CHANGE__ORIGINAL_LOAD_SLOT, oldOriginalLoadSlot, originalLoadSlot));
			}
		}
		return originalLoadSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LoadSlot basicGetOriginalLoadSlot() {
		return originalLoadSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOriginalLoadSlot(LoadSlot newOriginalLoadSlot) {
		LoadSlot oldOriginalLoadSlot = originalLoadSlot;
		originalLoadSlot = newOriginalLoadSlot;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.VESSEL_CHANGE__ORIGINAL_LOAD_SLOT, oldOriginalLoadSlot, originalLoadSlot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocation getOriginalLoadAllocation() {
		if (originalLoadAllocation != null && originalLoadAllocation.eIsProxy()) {
			InternalEObject oldOriginalLoadAllocation = (InternalEObject)originalLoadAllocation;
			originalLoadAllocation = (SlotAllocation)eResolveProxy(oldOriginalLoadAllocation);
			if (originalLoadAllocation != oldOriginalLoadAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.VESSEL_CHANGE__ORIGINAL_LOAD_ALLOCATION, oldOriginalLoadAllocation, originalLoadAllocation));
			}
		}
		return originalLoadAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocation basicGetOriginalLoadAllocation() {
		return originalLoadAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOriginalLoadAllocation(SlotAllocation newOriginalLoadAllocation) {
		SlotAllocation oldOriginalLoadAllocation = originalLoadAllocation;
		originalLoadAllocation = newOriginalLoadAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.VESSEL_CHANGE__ORIGINAL_LOAD_ALLOCATION, oldOriginalLoadAllocation, originalLoadAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocation getNewLoadAllocation() {
		if (newLoadAllocation != null && newLoadAllocation.eIsProxy()) {
			InternalEObject oldNewLoadAllocation = (InternalEObject)newLoadAllocation;
			newLoadAllocation = (SlotAllocation)eResolveProxy(oldNewLoadAllocation);
			if (newLoadAllocation != oldNewLoadAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.VESSEL_CHANGE__NEW_LOAD_ALLOCATION, oldNewLoadAllocation, newLoadAllocation));
			}
		}
		return newLoadAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocation basicGetNewLoadAllocation() {
		return newLoadAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewLoadAllocation(SlotAllocation newNewLoadAllocation) {
		SlotAllocation oldNewLoadAllocation = newLoadAllocation;
		newLoadAllocation = newNewLoadAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.VESSEL_CHANGE__NEW_LOAD_ALLOCATION, oldNewLoadAllocation, newLoadAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocation getOriginalDischargeAllocation() {
		if (originalDischargeAllocation != null && originalDischargeAllocation.eIsProxy()) {
			InternalEObject oldOriginalDischargeAllocation = (InternalEObject)originalDischargeAllocation;
			originalDischargeAllocation = (SlotAllocation)eResolveProxy(oldOriginalDischargeAllocation);
			if (originalDischargeAllocation != oldOriginalDischargeAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.VESSEL_CHANGE__ORIGINAL_DISCHARGE_ALLOCATION, oldOriginalDischargeAllocation, originalDischargeAllocation));
			}
		}
		return originalDischargeAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocation basicGetOriginalDischargeAllocation() {
		return originalDischargeAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOriginalDischargeAllocation(SlotAllocation newOriginalDischargeAllocation) {
		SlotAllocation oldOriginalDischargeAllocation = originalDischargeAllocation;
		originalDischargeAllocation = newOriginalDischargeAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.VESSEL_CHANGE__ORIGINAL_DISCHARGE_ALLOCATION, oldOriginalDischargeAllocation, originalDischargeAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocation getNewDischargeAllocation() {
		if (newDischargeAllocation != null && newDischargeAllocation.eIsProxy()) {
			InternalEObject oldNewDischargeAllocation = (InternalEObject)newDischargeAllocation;
			newDischargeAllocation = (SlotAllocation)eResolveProxy(oldNewDischargeAllocation);
			if (newDischargeAllocation != oldNewDischargeAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.VESSEL_CHANGE__NEW_DISCHARGE_ALLOCATION, oldNewDischargeAllocation, newDischargeAllocation));
			}
		}
		return newDischargeAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocation basicGetNewDischargeAllocation() {
		return newDischargeAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewDischargeAllocation(SlotAllocation newNewDischargeAllocation) {
		SlotAllocation oldNewDischargeAllocation = newDischargeAllocation;
		newDischargeAllocation = newNewDischargeAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.VESSEL_CHANGE__NEW_DISCHARGE_ALLOCATION, oldNewDischargeAllocation, newDischargeAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ChangesetPackage.VESSEL_CHANGE__ORIGINAL_VESSEL:
				if (resolve) return getOriginalVessel();
				return basicGetOriginalVessel();
			case ChangesetPackage.VESSEL_CHANGE__NEW_VESSEL:
				if (resolve) return getNewVessel();
				return basicGetNewVessel();
			case ChangesetPackage.VESSEL_CHANGE__ORIGINAL_LOAD_SLOT:
				if (resolve) return getOriginalLoadSlot();
				return basicGetOriginalLoadSlot();
			case ChangesetPackage.VESSEL_CHANGE__NEW_LOAD_SLOT:
				if (resolve) return getNewLoadSlot();
				return basicGetNewLoadSlot();
			case ChangesetPackage.VESSEL_CHANGE__ORIGINAL_DISCHARGE_SLOT:
				if (resolve) return getOriginalDischargeSlot();
				return basicGetOriginalDischargeSlot();
			case ChangesetPackage.VESSEL_CHANGE__NEW_DISCHARGE_SLOT:
				if (resolve) return getNewDischargeSlot();
				return basicGetNewDischargeSlot();
			case ChangesetPackage.VESSEL_CHANGE__ORIGINAL_LOAD_ALLOCATION:
				if (resolve) return getOriginalLoadAllocation();
				return basicGetOriginalLoadAllocation();
			case ChangesetPackage.VESSEL_CHANGE__NEW_LOAD_ALLOCATION:
				if (resolve) return getNewLoadAllocation();
				return basicGetNewLoadAllocation();
			case ChangesetPackage.VESSEL_CHANGE__ORIGINAL_DISCHARGE_ALLOCATION:
				if (resolve) return getOriginalDischargeAllocation();
				return basicGetOriginalDischargeAllocation();
			case ChangesetPackage.VESSEL_CHANGE__NEW_DISCHARGE_ALLOCATION:
				if (resolve) return getNewDischargeAllocation();
				return basicGetNewDischargeAllocation();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ChangesetPackage.VESSEL_CHANGE__ORIGINAL_VESSEL:
				setOriginalVessel((VesselAssignmentType)newValue);
				return;
			case ChangesetPackage.VESSEL_CHANGE__NEW_VESSEL:
				setNewVessel((VesselAssignmentType)newValue);
				return;
			case ChangesetPackage.VESSEL_CHANGE__ORIGINAL_LOAD_SLOT:
				setOriginalLoadSlot((LoadSlot)newValue);
				return;
			case ChangesetPackage.VESSEL_CHANGE__NEW_LOAD_SLOT:
				setNewLoadSlot((LoadSlot)newValue);
				return;
			case ChangesetPackage.VESSEL_CHANGE__ORIGINAL_DISCHARGE_SLOT:
				setOriginalDischargeSlot((DischargeSlot)newValue);
				return;
			case ChangesetPackage.VESSEL_CHANGE__NEW_DISCHARGE_SLOT:
				setNewDischargeSlot((DischargeSlot)newValue);
				return;
			case ChangesetPackage.VESSEL_CHANGE__ORIGINAL_LOAD_ALLOCATION:
				setOriginalLoadAllocation((SlotAllocation)newValue);
				return;
			case ChangesetPackage.VESSEL_CHANGE__NEW_LOAD_ALLOCATION:
				setNewLoadAllocation((SlotAllocation)newValue);
				return;
			case ChangesetPackage.VESSEL_CHANGE__ORIGINAL_DISCHARGE_ALLOCATION:
				setOriginalDischargeAllocation((SlotAllocation)newValue);
				return;
			case ChangesetPackage.VESSEL_CHANGE__NEW_DISCHARGE_ALLOCATION:
				setNewDischargeAllocation((SlotAllocation)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ChangesetPackage.VESSEL_CHANGE__ORIGINAL_VESSEL:
				setOriginalVessel((VesselAssignmentType)null);
				return;
			case ChangesetPackage.VESSEL_CHANGE__NEW_VESSEL:
				setNewVessel((VesselAssignmentType)null);
				return;
			case ChangesetPackage.VESSEL_CHANGE__ORIGINAL_LOAD_SLOT:
				setOriginalLoadSlot((LoadSlot)null);
				return;
			case ChangesetPackage.VESSEL_CHANGE__NEW_LOAD_SLOT:
				setNewLoadSlot((LoadSlot)null);
				return;
			case ChangesetPackage.VESSEL_CHANGE__ORIGINAL_DISCHARGE_SLOT:
				setOriginalDischargeSlot((DischargeSlot)null);
				return;
			case ChangesetPackage.VESSEL_CHANGE__NEW_DISCHARGE_SLOT:
				setNewDischargeSlot((DischargeSlot)null);
				return;
			case ChangesetPackage.VESSEL_CHANGE__ORIGINAL_LOAD_ALLOCATION:
				setOriginalLoadAllocation((SlotAllocation)null);
				return;
			case ChangesetPackage.VESSEL_CHANGE__NEW_LOAD_ALLOCATION:
				setNewLoadAllocation((SlotAllocation)null);
				return;
			case ChangesetPackage.VESSEL_CHANGE__ORIGINAL_DISCHARGE_ALLOCATION:
				setOriginalDischargeAllocation((SlotAllocation)null);
				return;
			case ChangesetPackage.VESSEL_CHANGE__NEW_DISCHARGE_ALLOCATION:
				setNewDischargeAllocation((SlotAllocation)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ChangesetPackage.VESSEL_CHANGE__ORIGINAL_VESSEL:
				return originalVessel != null;
			case ChangesetPackage.VESSEL_CHANGE__NEW_VESSEL:
				return newVessel != null;
			case ChangesetPackage.VESSEL_CHANGE__ORIGINAL_LOAD_SLOT:
				return originalLoadSlot != null;
			case ChangesetPackage.VESSEL_CHANGE__NEW_LOAD_SLOT:
				return newLoadSlot != null;
			case ChangesetPackage.VESSEL_CHANGE__ORIGINAL_DISCHARGE_SLOT:
				return originalDischargeSlot != null;
			case ChangesetPackage.VESSEL_CHANGE__NEW_DISCHARGE_SLOT:
				return newDischargeSlot != null;
			case ChangesetPackage.VESSEL_CHANGE__ORIGINAL_LOAD_ALLOCATION:
				return originalLoadAllocation != null;
			case ChangesetPackage.VESSEL_CHANGE__NEW_LOAD_ALLOCATION:
				return newLoadAllocation != null;
			case ChangesetPackage.VESSEL_CHANGE__ORIGINAL_DISCHARGE_ALLOCATION:
				return originalDischargeAllocation != null;
			case ChangesetPackage.VESSEL_CHANGE__NEW_DISCHARGE_ALLOCATION:
				return newDischargeAllocation != null;
		}
		return super.eIsSet(featureID);
	}

} //VesselChangeImpl
