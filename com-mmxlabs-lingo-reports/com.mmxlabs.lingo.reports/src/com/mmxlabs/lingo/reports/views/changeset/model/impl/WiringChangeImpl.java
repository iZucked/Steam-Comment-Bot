/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model.impl;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage;
import com.mmxlabs.lingo.reports.views.changeset.model.WiringChange;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Wiring Change</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.WiringChangeImpl#getLoadSlot_base <em>Load Slot base</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.WiringChangeImpl#getLoadSlot_target <em>Load Slot target</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.WiringChangeImpl#getOriginalDischargeSlot <em>Original Discharge Slot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.WiringChangeImpl#getNewDischargeSlot <em>New Discharge Slot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.WiringChangeImpl#getOriginalLoadAllocation <em>Original Load Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.WiringChangeImpl#getNewLoadAllocation <em>New Load Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.WiringChangeImpl#getOriginalDischargeAllocation <em>Original Discharge Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.WiringChangeImpl#getNewDischargeAllocation <em>New Discharge Allocation</em>}</li>
 * </ul>
 *
 * @generated
 */
public class WiringChangeImpl extends ChangeImpl implements WiringChange {
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
	protected com.mmxlabs.models.lng.schedule.SlotAllocation originalLoadAllocation;

	/**
	 * The cached value of the '{@link #getNewLoadAllocation() <em>New Load Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewLoadAllocation()
	 * @generated
	 * @ordered
	 */
	protected com.mmxlabs.models.lng.schedule.SlotAllocation newLoadAllocation;

	/**
	 * The cached value of the '{@link #getOriginalDischargeAllocation() <em>Original Discharge Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginalDischargeAllocation()
	 * @generated
	 * @ordered
	 */
	protected com.mmxlabs.models.lng.schedule.SlotAllocation originalDischargeAllocation;

	/**
	 * The cached value of the '{@link #getNewDischargeAllocation() <em>New Discharge Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewDischargeAllocation()
	 * @generated
	 * @ordered
	 */
	protected com.mmxlabs.models.lng.schedule.SlotAllocation newDischargeAllocation;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected WiringChangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ChangesetPackage.Literals.WIRING_CHANGE;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.WIRING_CHANGE__LOAD_SLOT_BASE, oldLoadSlot_base, loadSlot_base));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.WIRING_CHANGE__LOAD_SLOT_BASE, oldLoadSlot_base, loadSlot_base));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.WIRING_CHANGE__LOAD_SLOT_TARGET, oldLoadSlot_target, loadSlot_target));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.WIRING_CHANGE__LOAD_SLOT_TARGET, oldLoadSlot_target, loadSlot_target));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.WIRING_CHANGE__ORIGINAL_DISCHARGE_SLOT, oldOriginalDischargeSlot, originalDischargeSlot));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.WIRING_CHANGE__ORIGINAL_DISCHARGE_SLOT, oldOriginalDischargeSlot, originalDischargeSlot));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.WIRING_CHANGE__NEW_DISCHARGE_SLOT, oldNewDischargeSlot, newDischargeSlot));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.WIRING_CHANGE__NEW_DISCHARGE_SLOT, oldNewDischargeSlot, newDischargeSlot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public com.mmxlabs.models.lng.schedule.SlotAllocation getOriginalLoadAllocation() {
		if (originalLoadAllocation != null && originalLoadAllocation.eIsProxy()) {
			InternalEObject oldOriginalLoadAllocation = (InternalEObject)originalLoadAllocation;
			originalLoadAllocation = (com.mmxlabs.models.lng.schedule.SlotAllocation)eResolveProxy(oldOriginalLoadAllocation);
			if (originalLoadAllocation != oldOriginalLoadAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.WIRING_CHANGE__ORIGINAL_LOAD_ALLOCATION, oldOriginalLoadAllocation, originalLoadAllocation));
			}
		}
		return originalLoadAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public com.mmxlabs.models.lng.schedule.SlotAllocation basicGetOriginalLoadAllocation() {
		return originalLoadAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOriginalLoadAllocation(com.mmxlabs.models.lng.schedule.SlotAllocation newOriginalLoadAllocation) {
		com.mmxlabs.models.lng.schedule.SlotAllocation oldOriginalLoadAllocation = originalLoadAllocation;
		originalLoadAllocation = newOriginalLoadAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.WIRING_CHANGE__ORIGINAL_LOAD_ALLOCATION, oldOriginalLoadAllocation, originalLoadAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public com.mmxlabs.models.lng.schedule.SlotAllocation getNewLoadAllocation() {
		if (newLoadAllocation != null && newLoadAllocation.eIsProxy()) {
			InternalEObject oldNewLoadAllocation = (InternalEObject)newLoadAllocation;
			newLoadAllocation = (com.mmxlabs.models.lng.schedule.SlotAllocation)eResolveProxy(oldNewLoadAllocation);
			if (newLoadAllocation != oldNewLoadAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.WIRING_CHANGE__NEW_LOAD_ALLOCATION, oldNewLoadAllocation, newLoadAllocation));
			}
		}
		return newLoadAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public com.mmxlabs.models.lng.schedule.SlotAllocation basicGetNewLoadAllocation() {
		return newLoadAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewLoadAllocation(com.mmxlabs.models.lng.schedule.SlotAllocation newNewLoadAllocation) {
		com.mmxlabs.models.lng.schedule.SlotAllocation oldNewLoadAllocation = newLoadAllocation;
		newLoadAllocation = newNewLoadAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.WIRING_CHANGE__NEW_LOAD_ALLOCATION, oldNewLoadAllocation, newLoadAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public com.mmxlabs.models.lng.schedule.SlotAllocation getOriginalDischargeAllocation() {
		if (originalDischargeAllocation != null && originalDischargeAllocation.eIsProxy()) {
			InternalEObject oldOriginalDischargeAllocation = (InternalEObject)originalDischargeAllocation;
			originalDischargeAllocation = (com.mmxlabs.models.lng.schedule.SlotAllocation)eResolveProxy(oldOriginalDischargeAllocation);
			if (originalDischargeAllocation != oldOriginalDischargeAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.WIRING_CHANGE__ORIGINAL_DISCHARGE_ALLOCATION, oldOriginalDischargeAllocation, originalDischargeAllocation));
			}
		}
		return originalDischargeAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public com.mmxlabs.models.lng.schedule.SlotAllocation basicGetOriginalDischargeAllocation() {
		return originalDischargeAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOriginalDischargeAllocation(com.mmxlabs.models.lng.schedule.SlotAllocation newOriginalDischargeAllocation) {
		com.mmxlabs.models.lng.schedule.SlotAllocation oldOriginalDischargeAllocation = originalDischargeAllocation;
		originalDischargeAllocation = newOriginalDischargeAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.WIRING_CHANGE__ORIGINAL_DISCHARGE_ALLOCATION, oldOriginalDischargeAllocation, originalDischargeAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public com.mmxlabs.models.lng.schedule.SlotAllocation getNewDischargeAllocation() {
		if (newDischargeAllocation != null && newDischargeAllocation.eIsProxy()) {
			InternalEObject oldNewDischargeAllocation = (InternalEObject)newDischargeAllocation;
			newDischargeAllocation = (com.mmxlabs.models.lng.schedule.SlotAllocation)eResolveProxy(oldNewDischargeAllocation);
			if (newDischargeAllocation != oldNewDischargeAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.WIRING_CHANGE__NEW_DISCHARGE_ALLOCATION, oldNewDischargeAllocation, newDischargeAllocation));
			}
		}
		return newDischargeAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public com.mmxlabs.models.lng.schedule.SlotAllocation basicGetNewDischargeAllocation() {
		return newDischargeAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewDischargeAllocation(com.mmxlabs.models.lng.schedule.SlotAllocation newNewDischargeAllocation) {
		com.mmxlabs.models.lng.schedule.SlotAllocation oldNewDischargeAllocation = newDischargeAllocation;
		newDischargeAllocation = newNewDischargeAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.WIRING_CHANGE__NEW_DISCHARGE_ALLOCATION, oldNewDischargeAllocation, newDischargeAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ChangesetPackage.WIRING_CHANGE__LOAD_SLOT_BASE:
				if (resolve) return getLoadSlot_base();
				return basicGetLoadSlot_base();
			case ChangesetPackage.WIRING_CHANGE__LOAD_SLOT_TARGET:
				if (resolve) return getLoadSlot_target();
				return basicGetLoadSlot_target();
			case ChangesetPackage.WIRING_CHANGE__ORIGINAL_DISCHARGE_SLOT:
				if (resolve) return getOriginalDischargeSlot();
				return basicGetOriginalDischargeSlot();
			case ChangesetPackage.WIRING_CHANGE__NEW_DISCHARGE_SLOT:
				if (resolve) return getNewDischargeSlot();
				return basicGetNewDischargeSlot();
			case ChangesetPackage.WIRING_CHANGE__ORIGINAL_LOAD_ALLOCATION:
				if (resolve) return getOriginalLoadAllocation();
				return basicGetOriginalLoadAllocation();
			case ChangesetPackage.WIRING_CHANGE__NEW_LOAD_ALLOCATION:
				if (resolve) return getNewLoadAllocation();
				return basicGetNewLoadAllocation();
			case ChangesetPackage.WIRING_CHANGE__ORIGINAL_DISCHARGE_ALLOCATION:
				if (resolve) return getOriginalDischargeAllocation();
				return basicGetOriginalDischargeAllocation();
			case ChangesetPackage.WIRING_CHANGE__NEW_DISCHARGE_ALLOCATION:
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
			case ChangesetPackage.WIRING_CHANGE__LOAD_SLOT_BASE:
				setLoadSlot_base((LoadSlot)newValue);
				return;
			case ChangesetPackage.WIRING_CHANGE__LOAD_SLOT_TARGET:
				setLoadSlot_target((LoadSlot)newValue);
				return;
			case ChangesetPackage.WIRING_CHANGE__ORIGINAL_DISCHARGE_SLOT:
				setOriginalDischargeSlot((DischargeSlot)newValue);
				return;
			case ChangesetPackage.WIRING_CHANGE__NEW_DISCHARGE_SLOT:
				setNewDischargeSlot((DischargeSlot)newValue);
				return;
			case ChangesetPackage.WIRING_CHANGE__ORIGINAL_LOAD_ALLOCATION:
				setOriginalLoadAllocation((com.mmxlabs.models.lng.schedule.SlotAllocation)newValue);
				return;
			case ChangesetPackage.WIRING_CHANGE__NEW_LOAD_ALLOCATION:
				setNewLoadAllocation((com.mmxlabs.models.lng.schedule.SlotAllocation)newValue);
				return;
			case ChangesetPackage.WIRING_CHANGE__ORIGINAL_DISCHARGE_ALLOCATION:
				setOriginalDischargeAllocation((com.mmxlabs.models.lng.schedule.SlotAllocation)newValue);
				return;
			case ChangesetPackage.WIRING_CHANGE__NEW_DISCHARGE_ALLOCATION:
				setNewDischargeAllocation((com.mmxlabs.models.lng.schedule.SlotAllocation)newValue);
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
			case ChangesetPackage.WIRING_CHANGE__LOAD_SLOT_BASE:
				setLoadSlot_base((LoadSlot)null);
				return;
			case ChangesetPackage.WIRING_CHANGE__LOAD_SLOT_TARGET:
				setLoadSlot_target((LoadSlot)null);
				return;
			case ChangesetPackage.WIRING_CHANGE__ORIGINAL_DISCHARGE_SLOT:
				setOriginalDischargeSlot((DischargeSlot)null);
				return;
			case ChangesetPackage.WIRING_CHANGE__NEW_DISCHARGE_SLOT:
				setNewDischargeSlot((DischargeSlot)null);
				return;
			case ChangesetPackage.WIRING_CHANGE__ORIGINAL_LOAD_ALLOCATION:
				setOriginalLoadAllocation((com.mmxlabs.models.lng.schedule.SlotAllocation)null);
				return;
			case ChangesetPackage.WIRING_CHANGE__NEW_LOAD_ALLOCATION:
				setNewLoadAllocation((com.mmxlabs.models.lng.schedule.SlotAllocation)null);
				return;
			case ChangesetPackage.WIRING_CHANGE__ORIGINAL_DISCHARGE_ALLOCATION:
				setOriginalDischargeAllocation((com.mmxlabs.models.lng.schedule.SlotAllocation)null);
				return;
			case ChangesetPackage.WIRING_CHANGE__NEW_DISCHARGE_ALLOCATION:
				setNewDischargeAllocation((com.mmxlabs.models.lng.schedule.SlotAllocation)null);
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
			case ChangesetPackage.WIRING_CHANGE__LOAD_SLOT_BASE:
				return loadSlot_base != null;
			case ChangesetPackage.WIRING_CHANGE__LOAD_SLOT_TARGET:
				return loadSlot_target != null;
			case ChangesetPackage.WIRING_CHANGE__ORIGINAL_DISCHARGE_SLOT:
				return originalDischargeSlot != null;
			case ChangesetPackage.WIRING_CHANGE__NEW_DISCHARGE_SLOT:
				return newDischargeSlot != null;
			case ChangesetPackage.WIRING_CHANGE__ORIGINAL_LOAD_ALLOCATION:
				return originalLoadAllocation != null;
			case ChangesetPackage.WIRING_CHANGE__NEW_LOAD_ALLOCATION:
				return newLoadAllocation != null;
			case ChangesetPackage.WIRING_CHANGE__ORIGINAL_DISCHARGE_ALLOCATION:
				return originalDischargeAllocation != null;
			case ChangesetPackage.WIRING_CHANGE__NEW_DISCHARGE_ALLOCATION:
				return newDischargeAllocation != null;
		}
		return super.eIsSet(featureID);
	}

} //WiringChangeImpl
