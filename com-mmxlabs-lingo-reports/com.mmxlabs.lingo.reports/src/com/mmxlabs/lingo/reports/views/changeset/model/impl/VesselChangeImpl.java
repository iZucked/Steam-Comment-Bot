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
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.VesselChangeImpl#getLoadSlot_base <em>Load Slot base</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.VesselChangeImpl#getLoadSlot_target <em>Load Slot target</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.VesselChangeImpl#getOriginalVessel <em>Original Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.VesselChangeImpl#getNewVessel <em>New Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.VesselChangeImpl#getDischargeSlot_base <em>Discharge Slot base</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.VesselChangeImpl#getDischargeSlot_target <em>Discharge Slot target</em>}</li>
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
	 * The cached value of the '{@link #getLoadSlot_base() <em>Load Slot base</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadSlot_base()
	 * @generated
	 * @ordered
	 */
	protected LoadSlot loadSlot_base;

	/**
	 * The cached value of the '{@link #getLoadSlot_target() <em>Load Slot target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadSlot_target()
	 * @generated
	 * @ordered
	 */
	protected LoadSlot loadSlot_target;

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
	 * The cached value of the '{@link #getDischargeSlot_base() <em>Discharge Slot base</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeSlot_base()
	 * @generated
	 * @ordered
	 */
	protected DischargeSlot dischargeSlot_base;

	/**
	 * The cached value of the '{@link #getDischargeSlot_target() <em>Discharge Slot target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeSlot_target()
	 * @generated
	 * @ordered
	 */
	protected DischargeSlot dischargeSlot_target;

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
	public LoadSlot getLoadSlot_base() {
		if (loadSlot_base != null && loadSlot_base.eIsProxy()) {
			InternalEObject oldLoadSlot_base = (InternalEObject)loadSlot_base;
			loadSlot_base = (LoadSlot)eResolveProxy(oldLoadSlot_base);
			if (loadSlot_base != oldLoadSlot_base) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.VESSEL_CHANGE__LOAD_SLOT_BASE, oldLoadSlot_base, loadSlot_base));
			}
		}
		return loadSlot_base;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LoadSlot basicGetLoadSlot_base() {
		return loadSlot_base;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLoadSlot_base(LoadSlot newLoadSlot_base) {
		LoadSlot oldLoadSlot_base = loadSlot_base;
		loadSlot_base = newLoadSlot_base;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.VESSEL_CHANGE__LOAD_SLOT_BASE, oldLoadSlot_base, loadSlot_base));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LoadSlot getLoadSlot_target() {
		if (loadSlot_target != null && loadSlot_target.eIsProxy()) {
			InternalEObject oldLoadSlot_target = (InternalEObject)loadSlot_target;
			loadSlot_target = (LoadSlot)eResolveProxy(oldLoadSlot_target);
			if (loadSlot_target != oldLoadSlot_target) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.VESSEL_CHANGE__LOAD_SLOT_TARGET, oldLoadSlot_target, loadSlot_target));
			}
		}
		return loadSlot_target;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LoadSlot basicGetLoadSlot_target() {
		return loadSlot_target;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLoadSlot_target(LoadSlot newLoadSlot_target) {
		LoadSlot oldLoadSlot_target = loadSlot_target;
		loadSlot_target = newLoadSlot_target;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.VESSEL_CHANGE__LOAD_SLOT_TARGET, oldLoadSlot_target, loadSlot_target));
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
	public DischargeSlot getDischargeSlot_base() {
		if (dischargeSlot_base != null && dischargeSlot_base.eIsProxy()) {
			InternalEObject oldDischargeSlot_base = (InternalEObject)dischargeSlot_base;
			dischargeSlot_base = (DischargeSlot)eResolveProxy(oldDischargeSlot_base);
			if (dischargeSlot_base != oldDischargeSlot_base) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.VESSEL_CHANGE__DISCHARGE_SLOT_BASE, oldDischargeSlot_base, dischargeSlot_base));
			}
		}
		return dischargeSlot_base;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DischargeSlot basicGetDischargeSlot_base() {
		return dischargeSlot_base;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDischargeSlot_base(DischargeSlot newDischargeSlot_base) {
		DischargeSlot oldDischargeSlot_base = dischargeSlot_base;
		dischargeSlot_base = newDischargeSlot_base;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.VESSEL_CHANGE__DISCHARGE_SLOT_BASE, oldDischargeSlot_base, dischargeSlot_base));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DischargeSlot getDischargeSlot_target() {
		if (dischargeSlot_target != null && dischargeSlot_target.eIsProxy()) {
			InternalEObject oldDischargeSlot_target = (InternalEObject)dischargeSlot_target;
			dischargeSlot_target = (DischargeSlot)eResolveProxy(oldDischargeSlot_target);
			if (dischargeSlot_target != oldDischargeSlot_target) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.VESSEL_CHANGE__DISCHARGE_SLOT_TARGET, oldDischargeSlot_target, dischargeSlot_target));
			}
		}
		return dischargeSlot_target;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DischargeSlot basicGetDischargeSlot_target() {
		return dischargeSlot_target;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDischargeSlot_target(DischargeSlot newDischargeSlot_target) {
		DischargeSlot oldDischargeSlot_target = dischargeSlot_target;
		dischargeSlot_target = newDischargeSlot_target;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.VESSEL_CHANGE__DISCHARGE_SLOT_TARGET, oldDischargeSlot_target, dischargeSlot_target));
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
			case ChangesetPackage.VESSEL_CHANGE__LOAD_SLOT_BASE:
				if (resolve) return getLoadSlot_base();
				return basicGetLoadSlot_base();
			case ChangesetPackage.VESSEL_CHANGE__LOAD_SLOT_TARGET:
				if (resolve) return getLoadSlot_target();
				return basicGetLoadSlot_target();
			case ChangesetPackage.VESSEL_CHANGE__ORIGINAL_VESSEL:
				if (resolve) return getOriginalVessel();
				return basicGetOriginalVessel();
			case ChangesetPackage.VESSEL_CHANGE__NEW_VESSEL:
				if (resolve) return getNewVessel();
				return basicGetNewVessel();
			case ChangesetPackage.VESSEL_CHANGE__DISCHARGE_SLOT_BASE:
				if (resolve) return getDischargeSlot_base();
				return basicGetDischargeSlot_base();
			case ChangesetPackage.VESSEL_CHANGE__DISCHARGE_SLOT_TARGET:
				if (resolve) return getDischargeSlot_target();
				return basicGetDischargeSlot_target();
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
			case ChangesetPackage.VESSEL_CHANGE__LOAD_SLOT_BASE:
				setLoadSlot_base((LoadSlot)newValue);
				return;
			case ChangesetPackage.VESSEL_CHANGE__LOAD_SLOT_TARGET:
				setLoadSlot_target((LoadSlot)newValue);
				return;
			case ChangesetPackage.VESSEL_CHANGE__ORIGINAL_VESSEL:
				setOriginalVessel((VesselAssignmentType)newValue);
				return;
			case ChangesetPackage.VESSEL_CHANGE__NEW_VESSEL:
				setNewVessel((VesselAssignmentType)newValue);
				return;
			case ChangesetPackage.VESSEL_CHANGE__DISCHARGE_SLOT_BASE:
				setDischargeSlot_base((DischargeSlot)newValue);
				return;
			case ChangesetPackage.VESSEL_CHANGE__DISCHARGE_SLOT_TARGET:
				setDischargeSlot_target((DischargeSlot)newValue);
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
			case ChangesetPackage.VESSEL_CHANGE__LOAD_SLOT_BASE:
				setLoadSlot_base((LoadSlot)null);
				return;
			case ChangesetPackage.VESSEL_CHANGE__LOAD_SLOT_TARGET:
				setLoadSlot_target((LoadSlot)null);
				return;
			case ChangesetPackage.VESSEL_CHANGE__ORIGINAL_VESSEL:
				setOriginalVessel((VesselAssignmentType)null);
				return;
			case ChangesetPackage.VESSEL_CHANGE__NEW_VESSEL:
				setNewVessel((VesselAssignmentType)null);
				return;
			case ChangesetPackage.VESSEL_CHANGE__DISCHARGE_SLOT_BASE:
				setDischargeSlot_base((DischargeSlot)null);
				return;
			case ChangesetPackage.VESSEL_CHANGE__DISCHARGE_SLOT_TARGET:
				setDischargeSlot_target((DischargeSlot)null);
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
			case ChangesetPackage.VESSEL_CHANGE__LOAD_SLOT_BASE:
				return loadSlot_base != null;
			case ChangesetPackage.VESSEL_CHANGE__LOAD_SLOT_TARGET:
				return loadSlot_target != null;
			case ChangesetPackage.VESSEL_CHANGE__ORIGINAL_VESSEL:
				return originalVessel != null;
			case ChangesetPackage.VESSEL_CHANGE__NEW_VESSEL:
				return newVessel != null;
			case ChangesetPackage.VESSEL_CHANGE__DISCHARGE_SLOT_BASE:
				return dischargeSlot_base != null;
			case ChangesetPackage.VESSEL_CHANGE__DISCHARGE_SLOT_TARGET:
				return dischargeSlot_target != null;
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
