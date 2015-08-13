/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model.impl;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage;
import com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange;

import com.mmxlabs.models.lng.cargo.LoadSlot;

import com.mmxlabs.models.lng.types.VesselAssignmentType;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Event Vessel Change</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.EventVesselChangeImpl#getLoadSlot_base <em>Load Slot base</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.EventVesselChangeImpl#getLoadSlot_target <em>Load Slot target</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.EventVesselChangeImpl#getOriginalVessel <em>Original Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.EventVesselChangeImpl#getNewVessel <em>New Vessel</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EventVesselChangeImpl extends ChangeImpl implements EventVesselChange {
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EventVesselChangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ChangesetPackage.Literals.EVENT_VESSEL_CHANGE;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.EVENT_VESSEL_CHANGE__LOAD_SLOT_BASE, oldLoadSlot_base, loadSlot_base));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.EVENT_VESSEL_CHANGE__LOAD_SLOT_BASE, oldLoadSlot_base, loadSlot_base));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.EVENT_VESSEL_CHANGE__LOAD_SLOT_TARGET, oldLoadSlot_target, loadSlot_target));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.EVENT_VESSEL_CHANGE__LOAD_SLOT_TARGET, oldLoadSlot_target, loadSlot_target));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.EVENT_VESSEL_CHANGE__ORIGINAL_VESSEL, oldOriginalVessel, originalVessel));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.EVENT_VESSEL_CHANGE__ORIGINAL_VESSEL, oldOriginalVessel, originalVessel));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.EVENT_VESSEL_CHANGE__NEW_VESSEL, oldNewVessel, newVessel));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.EVENT_VESSEL_CHANGE__NEW_VESSEL, oldNewVessel, newVessel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ChangesetPackage.EVENT_VESSEL_CHANGE__LOAD_SLOT_BASE:
				if (resolve) return getLoadSlot_base();
				return basicGetLoadSlot_base();
			case ChangesetPackage.EVENT_VESSEL_CHANGE__LOAD_SLOT_TARGET:
				if (resolve) return getLoadSlot_target();
				return basicGetLoadSlot_target();
			case ChangesetPackage.EVENT_VESSEL_CHANGE__ORIGINAL_VESSEL:
				if (resolve) return getOriginalVessel();
				return basicGetOriginalVessel();
			case ChangesetPackage.EVENT_VESSEL_CHANGE__NEW_VESSEL:
				if (resolve) return getNewVessel();
				return basicGetNewVessel();
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
			case ChangesetPackage.EVENT_VESSEL_CHANGE__LOAD_SLOT_BASE:
				setLoadSlot_base((LoadSlot)newValue);
				return;
			case ChangesetPackage.EVENT_VESSEL_CHANGE__LOAD_SLOT_TARGET:
				setLoadSlot_target((LoadSlot)newValue);
				return;
			case ChangesetPackage.EVENT_VESSEL_CHANGE__ORIGINAL_VESSEL:
				setOriginalVessel((VesselAssignmentType)newValue);
				return;
			case ChangesetPackage.EVENT_VESSEL_CHANGE__NEW_VESSEL:
				setNewVessel((VesselAssignmentType)newValue);
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
			case ChangesetPackage.EVENT_VESSEL_CHANGE__LOAD_SLOT_BASE:
				setLoadSlot_base((LoadSlot)null);
				return;
			case ChangesetPackage.EVENT_VESSEL_CHANGE__LOAD_SLOT_TARGET:
				setLoadSlot_target((LoadSlot)null);
				return;
			case ChangesetPackage.EVENT_VESSEL_CHANGE__ORIGINAL_VESSEL:
				setOriginalVessel((VesselAssignmentType)null);
				return;
			case ChangesetPackage.EVENT_VESSEL_CHANGE__NEW_VESSEL:
				setNewVessel((VesselAssignmentType)null);
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
			case ChangesetPackage.EVENT_VESSEL_CHANGE__LOAD_SLOT_BASE:
				return loadSlot_base != null;
			case ChangesetPackage.EVENT_VESSEL_CHANGE__LOAD_SLOT_TARGET:
				return loadSlot_target != null;
			case ChangesetPackage.EVENT_VESSEL_CHANGE__ORIGINAL_VESSEL:
				return originalVessel != null;
			case ChangesetPackage.EVENT_VESSEL_CHANGE__NEW_VESSEL:
				return newVessel != null;
		}
		return super.eIsSet(featureID);
	}

} //EventVesselChangeImpl
