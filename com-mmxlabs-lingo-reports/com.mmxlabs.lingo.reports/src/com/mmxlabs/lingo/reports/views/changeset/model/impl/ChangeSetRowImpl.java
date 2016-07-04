/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.SlotAllocation;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Change Set Row</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getLhsName <em>Lhs Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getRhsName <em>Rhs Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getOriginalVesselName <em>Original Vessel Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getNewVesselName <em>New Vessel Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getLhsWiringLink <em>Lhs Wiring Link</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getRhsWiringLink <em>Rhs Wiring Link</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getLoadSlot <em>Load Slot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getDischargeSlot <em>Discharge Slot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getOriginalLoadAllocation <em>Original Load Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getNewLoadAllocation <em>New Load Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getOriginalDischargeAllocation <em>Original Discharge Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getNewDischargeAllocation <em>New Discharge Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#isWiringChange <em>Wiring Change</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#isVesselChange <em>Vessel Change</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getOriginalGroupProfitAndLoss <em>Original Group Profit And Loss</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getNewGroupProfitAndLoss <em>New Group Profit And Loss</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getOriginalEventGrouping <em>Original Event Grouping</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getNewEventGrouping <em>New Event Grouping</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getOriginalVesselShortName <em>Original Vessel Short Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getNewVesselShortName <em>New Vessel Short Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ChangeSetRowImpl extends MinimalEObjectImpl.Container implements ChangeSetRow {
	/**
	 * The default value of the '{@link #getLhsName() <em>Lhs Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLhsName()
	 * @generated
	 * @ordered
	 */
	protected static final String LHS_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLhsName() <em>Lhs Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLhsName()
	 * @generated
	 * @ordered
	 */
	protected String lhsName = LHS_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getRhsName() <em>Rhs Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRhsName()
	 * @generated
	 * @ordered
	 */
	protected static final String RHS_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRhsName() <em>Rhs Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRhsName()
	 * @generated
	 * @ordered
	 */
	protected String rhsName = RHS_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getOriginalVesselName() <em>Original Vessel Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginalVesselName()
	 * @generated
	 * @ordered
	 */
	protected static final String ORIGINAL_VESSEL_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOriginalVesselName() <em>Original Vessel Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginalVesselName()
	 * @generated
	 * @ordered
	 */
	protected String originalVesselName = ORIGINAL_VESSEL_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getNewVesselName() <em>New Vessel Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewVesselName()
	 * @generated
	 * @ordered
	 */
	protected static final String NEW_VESSEL_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNewVesselName() <em>New Vessel Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewVesselName()
	 * @generated
	 * @ordered
	 */
	protected String newVesselName = NEW_VESSEL_NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getLhsWiringLink() <em>Lhs Wiring Link</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLhsWiringLink()
	 * @generated
	 * @ordered
	 */
	protected ChangeSetRow lhsWiringLink;

	/**
	 * The cached value of the '{@link #getRhsWiringLink() <em>Rhs Wiring Link</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRhsWiringLink()
	 * @generated
	 * @ordered
	 */
	protected ChangeSetRow rhsWiringLink;

	/**
	 * The cached value of the '{@link #getLoadSlot() <em>Load Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadSlot()
	 * @generated
	 * @ordered
	 */
	protected LoadSlot loadSlot;

	/**
	 * The cached value of the '{@link #getDischargeSlot() <em>Discharge Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeSlot()
	 * @generated
	 * @ordered
	 */
	protected DischargeSlot dischargeSlot;

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
	 * The default value of the '{@link #isWiringChange() <em>Wiring Change</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isWiringChange()
	 * @generated
	 * @ordered
	 */
	protected static final boolean WIRING_CHANGE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isWiringChange() <em>Wiring Change</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isWiringChange()
	 * @generated
	 * @ordered
	 */
	protected boolean wiringChange = WIRING_CHANGE_EDEFAULT;

	/**
	 * The default value of the '{@link #isVesselChange() <em>Vessel Change</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isVesselChange()
	 * @generated
	 * @ordered
	 */
	protected static final boolean VESSEL_CHANGE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isVesselChange() <em>Vessel Change</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isVesselChange()
	 * @generated
	 * @ordered
	 */
	protected boolean vesselChange = VESSEL_CHANGE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getOriginalGroupProfitAndLoss() <em>Original Group Profit And Loss</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginalGroupProfitAndLoss()
	 * @generated
	 * @ordered
	 */
	protected ProfitAndLossContainer originalGroupProfitAndLoss;

	/**
	 * The cached value of the '{@link #getNewGroupProfitAndLoss() <em>New Group Profit And Loss</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewGroupProfitAndLoss()
	 * @generated
	 * @ordered
	 */
	protected ProfitAndLossContainer newGroupProfitAndLoss;

	/**
	 * The cached value of the '{@link #getOriginalEventGrouping() <em>Original Event Grouping</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginalEventGrouping()
	 * @generated
	 * @ordered
	 */
	protected EventGrouping originalEventGrouping;

	/**
	 * The cached value of the '{@link #getNewEventGrouping() <em>New Event Grouping</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewEventGrouping()
	 * @generated
	 * @ordered
	 */
	protected EventGrouping newEventGrouping;

	/**
	 * The default value of the '{@link #getOriginalVesselShortName() <em>Original Vessel Short Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginalVesselShortName()
	 * @generated
	 * @ordered
	 */
	protected static final String ORIGINAL_VESSEL_SHORT_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOriginalVesselShortName() <em>Original Vessel Short Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginalVesselShortName()
	 * @generated
	 * @ordered
	 */
	protected String originalVesselShortName = ORIGINAL_VESSEL_SHORT_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getNewVesselShortName() <em>New Vessel Short Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewVesselShortName()
	 * @generated
	 * @ordered
	 */
	protected static final String NEW_VESSEL_SHORT_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNewVesselShortName() <em>New Vessel Short Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewVesselShortName()
	 * @generated
	 * @ordered
	 */
	protected String newVesselShortName = NEW_VESSEL_SHORT_NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ChangeSetRowImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ChangesetPackage.Literals.CHANGE_SET_ROW;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLhsName() {
		return lhsName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLhsName(String newLhsName) {
		String oldLhsName = lhsName;
		lhsName = newLhsName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__LHS_NAME, oldLhsName, lhsName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getRhsName() {
		return rhsName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRhsName(String newRhsName) {
		String oldRhsName = rhsName;
		rhsName = newRhsName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__RHS_NAME, oldRhsName, rhsName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getOriginalVesselName() {
		return originalVesselName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOriginalVesselName(String newOriginalVesselName) {
		String oldOriginalVesselName = originalVesselName;
		originalVesselName = newOriginalVesselName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_VESSEL_NAME, oldOriginalVesselName, originalVesselName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getNewVesselName() {
		return newVesselName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewVesselName(String newNewVesselName) {
		String oldNewVesselName = newVesselName;
		newVesselName = newNewVesselName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__NEW_VESSEL_NAME, oldNewVesselName, newVesselName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSetRow getLhsWiringLink() {
		if (lhsWiringLink != null && lhsWiringLink.eIsProxy()) {
			InternalEObject oldLhsWiringLink = (InternalEObject)lhsWiringLink;
			lhsWiringLink = (ChangeSetRow)eResolveProxy(oldLhsWiringLink);
			if (lhsWiringLink != oldLhsWiringLink) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_ROW__LHS_WIRING_LINK, oldLhsWiringLink, lhsWiringLink));
			}
		}
		return lhsWiringLink;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSetRow basicGetLhsWiringLink() {
		return lhsWiringLink;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLhsWiringLink(ChangeSetRow newLhsWiringLink, NotificationChain msgs) {
		ChangeSetRow oldLhsWiringLink = lhsWiringLink;
		lhsWiringLink = newLhsWiringLink;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__LHS_WIRING_LINK, oldLhsWiringLink, newLhsWiringLink);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLhsWiringLink(ChangeSetRow newLhsWiringLink) {
		if (newLhsWiringLink != lhsWiringLink) {
			NotificationChain msgs = null;
			if (lhsWiringLink != null)
				msgs = ((InternalEObject)lhsWiringLink).eInverseRemove(this, ChangesetPackage.CHANGE_SET_ROW__RHS_WIRING_LINK, ChangeSetRow.class, msgs);
			if (newLhsWiringLink != null)
				msgs = ((InternalEObject)newLhsWiringLink).eInverseAdd(this, ChangesetPackage.CHANGE_SET_ROW__RHS_WIRING_LINK, ChangeSetRow.class, msgs);
			msgs = basicSetLhsWiringLink(newLhsWiringLink, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__LHS_WIRING_LINK, newLhsWiringLink, newLhsWiringLink));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSetRow getRhsWiringLink() {
		if (rhsWiringLink != null && rhsWiringLink.eIsProxy()) {
			InternalEObject oldRhsWiringLink = (InternalEObject)rhsWiringLink;
			rhsWiringLink = (ChangeSetRow)eResolveProxy(oldRhsWiringLink);
			if (rhsWiringLink != oldRhsWiringLink) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_ROW__RHS_WIRING_LINK, oldRhsWiringLink, rhsWiringLink));
			}
		}
		return rhsWiringLink;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSetRow basicGetRhsWiringLink() {
		return rhsWiringLink;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetRhsWiringLink(ChangeSetRow newRhsWiringLink, NotificationChain msgs) {
		ChangeSetRow oldRhsWiringLink = rhsWiringLink;
		rhsWiringLink = newRhsWiringLink;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__RHS_WIRING_LINK, oldRhsWiringLink, newRhsWiringLink);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRhsWiringLink(ChangeSetRow newRhsWiringLink) {
		if (newRhsWiringLink != rhsWiringLink) {
			NotificationChain msgs = null;
			if (rhsWiringLink != null)
				msgs = ((InternalEObject)rhsWiringLink).eInverseRemove(this, ChangesetPackage.CHANGE_SET_ROW__LHS_WIRING_LINK, ChangeSetRow.class, msgs);
			if (newRhsWiringLink != null)
				msgs = ((InternalEObject)newRhsWiringLink).eInverseAdd(this, ChangesetPackage.CHANGE_SET_ROW__LHS_WIRING_LINK, ChangeSetRow.class, msgs);
			msgs = basicSetRhsWiringLink(newRhsWiringLink, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__RHS_WIRING_LINK, newRhsWiringLink, newRhsWiringLink));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LoadSlot getLoadSlot() {
		if (loadSlot != null && loadSlot.eIsProxy()) {
			InternalEObject oldLoadSlot = (InternalEObject)loadSlot;
			loadSlot = (LoadSlot)eResolveProxy(oldLoadSlot);
			if (loadSlot != oldLoadSlot) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_ROW__LOAD_SLOT, oldLoadSlot, loadSlot));
			}
		}
		return loadSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LoadSlot basicGetLoadSlot() {
		return loadSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLoadSlot(LoadSlot newLoadSlot) {
		LoadSlot oldLoadSlot = loadSlot;
		loadSlot = newLoadSlot;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__LOAD_SLOT, oldLoadSlot, loadSlot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DischargeSlot getDischargeSlot() {
		if (dischargeSlot != null && dischargeSlot.eIsProxy()) {
			InternalEObject oldDischargeSlot = (InternalEObject)dischargeSlot;
			dischargeSlot = (DischargeSlot)eResolveProxy(oldDischargeSlot);
			if (dischargeSlot != oldDischargeSlot) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_ROW__DISCHARGE_SLOT, oldDischargeSlot, dischargeSlot));
			}
		}
		return dischargeSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DischargeSlot basicGetDischargeSlot() {
		return dischargeSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDischargeSlot(DischargeSlot newDischargeSlot) {
		DischargeSlot oldDischargeSlot = dischargeSlot;
		dischargeSlot = newDischargeSlot;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__DISCHARGE_SLOT, oldDischargeSlot, dischargeSlot));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_LOAD_ALLOCATION, oldOriginalLoadAllocation, originalLoadAllocation));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_LOAD_ALLOCATION, oldOriginalLoadAllocation, originalLoadAllocation));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_ROW__NEW_LOAD_ALLOCATION, oldNewLoadAllocation, newLoadAllocation));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__NEW_LOAD_ALLOCATION, oldNewLoadAllocation, newLoadAllocation));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_DISCHARGE_ALLOCATION, oldOriginalDischargeAllocation, originalDischargeAllocation));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_DISCHARGE_ALLOCATION, oldOriginalDischargeAllocation, originalDischargeAllocation));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_ROW__NEW_DISCHARGE_ALLOCATION, oldNewDischargeAllocation, newDischargeAllocation));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__NEW_DISCHARGE_ALLOCATION, oldNewDischargeAllocation, newDischargeAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isWiringChange() {
		return wiringChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWiringChange(boolean newWiringChange) {
		boolean oldWiringChange = wiringChange;
		wiringChange = newWiringChange;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__WIRING_CHANGE, oldWiringChange, wiringChange));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isVesselChange() {
		return vesselChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVesselChange(boolean newVesselChange) {
		boolean oldVesselChange = vesselChange;
		vesselChange = newVesselChange;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__VESSEL_CHANGE, oldVesselChange, vesselChange));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProfitAndLossContainer getOriginalGroupProfitAndLoss() {
		if (originalGroupProfitAndLoss != null && originalGroupProfitAndLoss.eIsProxy()) {
			InternalEObject oldOriginalGroupProfitAndLoss = (InternalEObject)originalGroupProfitAndLoss;
			originalGroupProfitAndLoss = (ProfitAndLossContainer)eResolveProxy(oldOriginalGroupProfitAndLoss);
			if (originalGroupProfitAndLoss != oldOriginalGroupProfitAndLoss) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_GROUP_PROFIT_AND_LOSS, oldOriginalGroupProfitAndLoss, originalGroupProfitAndLoss));
			}
		}
		return originalGroupProfitAndLoss;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProfitAndLossContainer basicGetOriginalGroupProfitAndLoss() {
		return originalGroupProfitAndLoss;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOriginalGroupProfitAndLoss(ProfitAndLossContainer newOriginalGroupProfitAndLoss) {
		ProfitAndLossContainer oldOriginalGroupProfitAndLoss = originalGroupProfitAndLoss;
		originalGroupProfitAndLoss = newOriginalGroupProfitAndLoss;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_GROUP_PROFIT_AND_LOSS, oldOriginalGroupProfitAndLoss, originalGroupProfitAndLoss));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProfitAndLossContainer getNewGroupProfitAndLoss() {
		if (newGroupProfitAndLoss != null && newGroupProfitAndLoss.eIsProxy()) {
			InternalEObject oldNewGroupProfitAndLoss = (InternalEObject)newGroupProfitAndLoss;
			newGroupProfitAndLoss = (ProfitAndLossContainer)eResolveProxy(oldNewGroupProfitAndLoss);
			if (newGroupProfitAndLoss != oldNewGroupProfitAndLoss) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_ROW__NEW_GROUP_PROFIT_AND_LOSS, oldNewGroupProfitAndLoss, newGroupProfitAndLoss));
			}
		}
		return newGroupProfitAndLoss;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProfitAndLossContainer basicGetNewGroupProfitAndLoss() {
		return newGroupProfitAndLoss;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewGroupProfitAndLoss(ProfitAndLossContainer newNewGroupProfitAndLoss) {
		ProfitAndLossContainer oldNewGroupProfitAndLoss = newGroupProfitAndLoss;
		newGroupProfitAndLoss = newNewGroupProfitAndLoss;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__NEW_GROUP_PROFIT_AND_LOSS, oldNewGroupProfitAndLoss, newGroupProfitAndLoss));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EventGrouping getOriginalEventGrouping() {
		if (originalEventGrouping != null && originalEventGrouping.eIsProxy()) {
			InternalEObject oldOriginalEventGrouping = (InternalEObject)originalEventGrouping;
			originalEventGrouping = (EventGrouping)eResolveProxy(oldOriginalEventGrouping);
			if (originalEventGrouping != oldOriginalEventGrouping) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_EVENT_GROUPING, oldOriginalEventGrouping, originalEventGrouping));
			}
		}
		return originalEventGrouping;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EventGrouping basicGetOriginalEventGrouping() {
		return originalEventGrouping;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOriginalEventGrouping(EventGrouping newOriginalEventGrouping) {
		EventGrouping oldOriginalEventGrouping = originalEventGrouping;
		originalEventGrouping = newOriginalEventGrouping;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_EVENT_GROUPING, oldOriginalEventGrouping, originalEventGrouping));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EventGrouping getNewEventGrouping() {
		if (newEventGrouping != null && newEventGrouping.eIsProxy()) {
			InternalEObject oldNewEventGrouping = (InternalEObject)newEventGrouping;
			newEventGrouping = (EventGrouping)eResolveProxy(oldNewEventGrouping);
			if (newEventGrouping != oldNewEventGrouping) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_ROW__NEW_EVENT_GROUPING, oldNewEventGrouping, newEventGrouping));
			}
		}
		return newEventGrouping;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EventGrouping basicGetNewEventGrouping() {
		return newEventGrouping;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewEventGrouping(EventGrouping newNewEventGrouping) {
		EventGrouping oldNewEventGrouping = newEventGrouping;
		newEventGrouping = newNewEventGrouping;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__NEW_EVENT_GROUPING, oldNewEventGrouping, newEventGrouping));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getOriginalVesselShortName() {
		return originalVesselShortName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOriginalVesselShortName(String newOriginalVesselShortName) {
		String oldOriginalVesselShortName = originalVesselShortName;
		originalVesselShortName = newOriginalVesselShortName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_VESSEL_SHORT_NAME, oldOriginalVesselShortName, originalVesselShortName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getNewVesselShortName() {
		return newVesselShortName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewVesselShortName(String newNewVesselShortName) {
		String oldNewVesselShortName = newVesselShortName;
		newVesselShortName = newNewVesselShortName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__NEW_VESSEL_SHORT_NAME, oldNewVesselShortName, newVesselShortName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ChangesetPackage.CHANGE_SET_ROW__LHS_WIRING_LINK:
				if (lhsWiringLink != null)
					msgs = ((InternalEObject)lhsWiringLink).eInverseRemove(this, ChangesetPackage.CHANGE_SET_ROW__RHS_WIRING_LINK, ChangeSetRow.class, msgs);
				return basicSetLhsWiringLink((ChangeSetRow)otherEnd, msgs);
			case ChangesetPackage.CHANGE_SET_ROW__RHS_WIRING_LINK:
				if (rhsWiringLink != null)
					msgs = ((InternalEObject)rhsWiringLink).eInverseRemove(this, ChangesetPackage.CHANGE_SET_ROW__LHS_WIRING_LINK, ChangeSetRow.class, msgs);
				return basicSetRhsWiringLink((ChangeSetRow)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ChangesetPackage.CHANGE_SET_ROW__LHS_WIRING_LINK:
				return basicSetLhsWiringLink(null, msgs);
			case ChangesetPackage.CHANGE_SET_ROW__RHS_WIRING_LINK:
				return basicSetRhsWiringLink(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ChangesetPackage.CHANGE_SET_ROW__LHS_NAME:
				return getLhsName();
			case ChangesetPackage.CHANGE_SET_ROW__RHS_NAME:
				return getRhsName();
			case ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_VESSEL_NAME:
				return getOriginalVesselName();
			case ChangesetPackage.CHANGE_SET_ROW__NEW_VESSEL_NAME:
				return getNewVesselName();
			case ChangesetPackage.CHANGE_SET_ROW__LHS_WIRING_LINK:
				if (resolve) return getLhsWiringLink();
				return basicGetLhsWiringLink();
			case ChangesetPackage.CHANGE_SET_ROW__RHS_WIRING_LINK:
				if (resolve) return getRhsWiringLink();
				return basicGetRhsWiringLink();
			case ChangesetPackage.CHANGE_SET_ROW__LOAD_SLOT:
				if (resolve) return getLoadSlot();
				return basicGetLoadSlot();
			case ChangesetPackage.CHANGE_SET_ROW__DISCHARGE_SLOT:
				if (resolve) return getDischargeSlot();
				return basicGetDischargeSlot();
			case ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_LOAD_ALLOCATION:
				if (resolve) return getOriginalLoadAllocation();
				return basicGetOriginalLoadAllocation();
			case ChangesetPackage.CHANGE_SET_ROW__NEW_LOAD_ALLOCATION:
				if (resolve) return getNewLoadAllocation();
				return basicGetNewLoadAllocation();
			case ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_DISCHARGE_ALLOCATION:
				if (resolve) return getOriginalDischargeAllocation();
				return basicGetOriginalDischargeAllocation();
			case ChangesetPackage.CHANGE_SET_ROW__NEW_DISCHARGE_ALLOCATION:
				if (resolve) return getNewDischargeAllocation();
				return basicGetNewDischargeAllocation();
			case ChangesetPackage.CHANGE_SET_ROW__WIRING_CHANGE:
				return isWiringChange();
			case ChangesetPackage.CHANGE_SET_ROW__VESSEL_CHANGE:
				return isVesselChange();
			case ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_GROUP_PROFIT_AND_LOSS:
				if (resolve) return getOriginalGroupProfitAndLoss();
				return basicGetOriginalGroupProfitAndLoss();
			case ChangesetPackage.CHANGE_SET_ROW__NEW_GROUP_PROFIT_AND_LOSS:
				if (resolve) return getNewGroupProfitAndLoss();
				return basicGetNewGroupProfitAndLoss();
			case ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_EVENT_GROUPING:
				if (resolve) return getOriginalEventGrouping();
				return basicGetOriginalEventGrouping();
			case ChangesetPackage.CHANGE_SET_ROW__NEW_EVENT_GROUPING:
				if (resolve) return getNewEventGrouping();
				return basicGetNewEventGrouping();
			case ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_VESSEL_SHORT_NAME:
				return getOriginalVesselShortName();
			case ChangesetPackage.CHANGE_SET_ROW__NEW_VESSEL_SHORT_NAME:
				return getNewVesselShortName();
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
			case ChangesetPackage.CHANGE_SET_ROW__LHS_NAME:
				setLhsName((String)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__RHS_NAME:
				setRhsName((String)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_VESSEL_NAME:
				setOriginalVesselName((String)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__NEW_VESSEL_NAME:
				setNewVesselName((String)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__LHS_WIRING_LINK:
				setLhsWiringLink((ChangeSetRow)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__RHS_WIRING_LINK:
				setRhsWiringLink((ChangeSetRow)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__LOAD_SLOT:
				setLoadSlot((LoadSlot)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__DISCHARGE_SLOT:
				setDischargeSlot((DischargeSlot)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_LOAD_ALLOCATION:
				setOriginalLoadAllocation((SlotAllocation)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__NEW_LOAD_ALLOCATION:
				setNewLoadAllocation((SlotAllocation)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_DISCHARGE_ALLOCATION:
				setOriginalDischargeAllocation((SlotAllocation)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__NEW_DISCHARGE_ALLOCATION:
				setNewDischargeAllocation((SlotAllocation)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__WIRING_CHANGE:
				setWiringChange((Boolean)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__VESSEL_CHANGE:
				setVesselChange((Boolean)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_GROUP_PROFIT_AND_LOSS:
				setOriginalGroupProfitAndLoss((ProfitAndLossContainer)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__NEW_GROUP_PROFIT_AND_LOSS:
				setNewGroupProfitAndLoss((ProfitAndLossContainer)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_EVENT_GROUPING:
				setOriginalEventGrouping((EventGrouping)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__NEW_EVENT_GROUPING:
				setNewEventGrouping((EventGrouping)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_VESSEL_SHORT_NAME:
				setOriginalVesselShortName((String)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__NEW_VESSEL_SHORT_NAME:
				setNewVesselShortName((String)newValue);
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
			case ChangesetPackage.CHANGE_SET_ROW__LHS_NAME:
				setLhsName(LHS_NAME_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__RHS_NAME:
				setRhsName(RHS_NAME_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_VESSEL_NAME:
				setOriginalVesselName(ORIGINAL_VESSEL_NAME_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__NEW_VESSEL_NAME:
				setNewVesselName(NEW_VESSEL_NAME_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__LHS_WIRING_LINK:
				setLhsWiringLink((ChangeSetRow)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__RHS_WIRING_LINK:
				setRhsWiringLink((ChangeSetRow)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__LOAD_SLOT:
				setLoadSlot((LoadSlot)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__DISCHARGE_SLOT:
				setDischargeSlot((DischargeSlot)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_LOAD_ALLOCATION:
				setOriginalLoadAllocation((SlotAllocation)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__NEW_LOAD_ALLOCATION:
				setNewLoadAllocation((SlotAllocation)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_DISCHARGE_ALLOCATION:
				setOriginalDischargeAllocation((SlotAllocation)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__NEW_DISCHARGE_ALLOCATION:
				setNewDischargeAllocation((SlotAllocation)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__WIRING_CHANGE:
				setWiringChange(WIRING_CHANGE_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__VESSEL_CHANGE:
				setVesselChange(VESSEL_CHANGE_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_GROUP_PROFIT_AND_LOSS:
				setOriginalGroupProfitAndLoss((ProfitAndLossContainer)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__NEW_GROUP_PROFIT_AND_LOSS:
				setNewGroupProfitAndLoss((ProfitAndLossContainer)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_EVENT_GROUPING:
				setOriginalEventGrouping((EventGrouping)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__NEW_EVENT_GROUPING:
				setNewEventGrouping((EventGrouping)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_VESSEL_SHORT_NAME:
				setOriginalVesselShortName(ORIGINAL_VESSEL_SHORT_NAME_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__NEW_VESSEL_SHORT_NAME:
				setNewVesselShortName(NEW_VESSEL_SHORT_NAME_EDEFAULT);
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
			case ChangesetPackage.CHANGE_SET_ROW__LHS_NAME:
				return LHS_NAME_EDEFAULT == null ? lhsName != null : !LHS_NAME_EDEFAULT.equals(lhsName);
			case ChangesetPackage.CHANGE_SET_ROW__RHS_NAME:
				return RHS_NAME_EDEFAULT == null ? rhsName != null : !RHS_NAME_EDEFAULT.equals(rhsName);
			case ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_VESSEL_NAME:
				return ORIGINAL_VESSEL_NAME_EDEFAULT == null ? originalVesselName != null : !ORIGINAL_VESSEL_NAME_EDEFAULT.equals(originalVesselName);
			case ChangesetPackage.CHANGE_SET_ROW__NEW_VESSEL_NAME:
				return NEW_VESSEL_NAME_EDEFAULT == null ? newVesselName != null : !NEW_VESSEL_NAME_EDEFAULT.equals(newVesselName);
			case ChangesetPackage.CHANGE_SET_ROW__LHS_WIRING_LINK:
				return lhsWiringLink != null;
			case ChangesetPackage.CHANGE_SET_ROW__RHS_WIRING_LINK:
				return rhsWiringLink != null;
			case ChangesetPackage.CHANGE_SET_ROW__LOAD_SLOT:
				return loadSlot != null;
			case ChangesetPackage.CHANGE_SET_ROW__DISCHARGE_SLOT:
				return dischargeSlot != null;
			case ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_LOAD_ALLOCATION:
				return originalLoadAllocation != null;
			case ChangesetPackage.CHANGE_SET_ROW__NEW_LOAD_ALLOCATION:
				return newLoadAllocation != null;
			case ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_DISCHARGE_ALLOCATION:
				return originalDischargeAllocation != null;
			case ChangesetPackage.CHANGE_SET_ROW__NEW_DISCHARGE_ALLOCATION:
				return newDischargeAllocation != null;
			case ChangesetPackage.CHANGE_SET_ROW__WIRING_CHANGE:
				return wiringChange != WIRING_CHANGE_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_ROW__VESSEL_CHANGE:
				return vesselChange != VESSEL_CHANGE_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_GROUP_PROFIT_AND_LOSS:
				return originalGroupProfitAndLoss != null;
			case ChangesetPackage.CHANGE_SET_ROW__NEW_GROUP_PROFIT_AND_LOSS:
				return newGroupProfitAndLoss != null;
			case ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_EVENT_GROUPING:
				return originalEventGrouping != null;
			case ChangesetPackage.CHANGE_SET_ROW__NEW_EVENT_GROUPING:
				return newEventGrouping != null;
			case ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_VESSEL_SHORT_NAME:
				return ORIGINAL_VESSEL_SHORT_NAME_EDEFAULT == null ? originalVesselShortName != null : !ORIGINAL_VESSEL_SHORT_NAME_EDEFAULT.equals(originalVesselShortName);
			case ChangesetPackage.CHANGE_SET_ROW__NEW_VESSEL_SHORT_NAME:
				return NEW_VESSEL_SHORT_NAME_EDEFAULT == null ? newVesselShortName != null : !NEW_VESSEL_SHORT_NAME_EDEFAULT.equals(newVesselShortName);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (lhsName: ");
		result.append(lhsName);
		result.append(", rhsName: ");
		result.append(rhsName);
		result.append(", originalVesselName: ");
		result.append(originalVesselName);
		result.append(", newVesselName: ");
		result.append(newVesselName);
		result.append(", wiringChange: ");
		result.append(wiringChange);
		result.append(", vesselChange: ");
		result.append(vesselChange);
		result.append(", originalVesselShortName: ");
		result.append(originalVesselShortName);
		result.append(", newVesselShortName: ");
		result.append(newVesselShortName);
		result.append(')');
		return result.toString();
	}

} //ChangeSetRowImpl
