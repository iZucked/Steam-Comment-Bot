/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowDataGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetVesselType;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.PaperDealAllocation;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.SlotAllocation;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Change Set Row Data</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl#isPrimaryRecord <em>Primary Record</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl#getRowDataGroup <em>Row Data Group</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl#getEventGrouping <em>Event Grouping</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl#getVesselName <em>Vessel Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl#getVesselShortName <em>Vessel Short Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl#getVesselType <em>Vessel Type</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl#getVesselCharterNumber <em>Vessel Charter Number</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl#getLhsName <em>Lhs Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl#getRhsName <em>Rhs Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl#getLhsLink <em>Lhs Link</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl#getRhsLink <em>Rhs Link</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl#isLhsSlot <em>Lhs Slot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl#isLhsSpot <em>Lhs Spot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl#isLhsOptional <em>Lhs Optional</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl#isRhsSlot <em>Rhs Slot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl#isRhsSpot <em>Rhs Spot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl#isRhsOptional <em>Rhs Optional</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl#isLhsNonShipped <em>Lhs Non Shipped</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl#isRhsNonShipped <em>Rhs Non Shipped</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl#getLoadSlot <em>Load Slot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl#getDischargeSlot <em>Discharge Slot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl#getLoadAllocation <em>Load Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl#getDischargeAllocation <em>Discharge Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl#getOpenLoadAllocation <em>Open Load Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl#getOpenDischargeAllocation <em>Open Discharge Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl#getLhsEvent <em>Lhs Event</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl#getRhsEvent <em>Rhs Event</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl#getLhsGroupProfitAndLoss <em>Lhs Group Profit And Loss</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl#getRhsGroupProfitAndLoss <em>Rhs Group Profit And Loss</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowDataImpl#getPaperDealAllocation <em>Paper Deal Allocation</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ChangeSetRowDataImpl extends MinimalEObjectImpl.Container implements ChangeSetRowData {
	/**
	 * The default value of the '{@link #isPrimaryRecord() <em>Primary Record</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isPrimaryRecord()
	 * @generated
	 * @ordered
	 */
	protected static final boolean PRIMARY_RECORD_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isPrimaryRecord() <em>Primary Record</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isPrimaryRecord()
	 * @generated
	 * @ordered
	 */
	protected boolean primaryRecord = PRIMARY_RECORD_EDEFAULT;

	/**
	 * The cached value of the '{@link #getEventGrouping() <em>Event Grouping</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEventGrouping()
	 * @generated
	 * @ordered
	 */
	protected EventGrouping eventGrouping;

	/**
	 * The default value of the '{@link #getVesselName() <em>Vessel Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselName()
	 * @generated
	 * @ordered
	 */
	protected static final String VESSEL_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVesselName() <em>Vessel Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselName()
	 * @generated
	 * @ordered
	 */
	protected String vesselName = VESSEL_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getVesselShortName() <em>Vessel Short Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselShortName()
	 * @generated
	 * @ordered
	 */
	protected static final String VESSEL_SHORT_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVesselShortName() <em>Vessel Short Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselShortName()
	 * @generated
	 * @ordered
	 */
	protected String vesselShortName = VESSEL_SHORT_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getVesselType() <em>Vessel Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselType()
	 * @generated
	 * @ordered
	 */
	protected static final ChangeSetVesselType VESSEL_TYPE_EDEFAULT = ChangeSetVesselType.FLEET;

	/**
	 * The cached value of the '{@link #getVesselType() <em>Vessel Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselType()
	 * @generated
	 * @ordered
	 */
	protected ChangeSetVesselType vesselType = VESSEL_TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getVesselCharterNumber() <em>Vessel Charter Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselCharterNumber()
	 * @generated
	 * @ordered
	 */
	protected static final int VESSEL_CHARTER_NUMBER_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getVesselCharterNumber() <em>Vessel Charter Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselCharterNumber()
	 * @generated
	 * @ordered
	 */
	protected int vesselCharterNumber = VESSEL_CHARTER_NUMBER_EDEFAULT;

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
	 * The cached value of the '{@link #getLhsLink() <em>Lhs Link</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLhsLink()
	 * @generated
	 * @ordered
	 */
	protected ChangeSetRowData lhsLink;

	/**
	 * The cached value of the '{@link #getRhsLink() <em>Rhs Link</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRhsLink()
	 * @generated
	 * @ordered
	 */
	protected ChangeSetRowData rhsLink;

	/**
	 * The default value of the '{@link #isLhsSlot() <em>Lhs Slot</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLhsSlot()
	 * @generated
	 * @ordered
	 */
	protected static final boolean LHS_SLOT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isLhsSlot() <em>Lhs Slot</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLhsSlot()
	 * @generated
	 * @ordered
	 */
	protected boolean lhsSlot = LHS_SLOT_EDEFAULT;

	/**
	 * The default value of the '{@link #isLhsSpot() <em>Lhs Spot</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLhsSpot()
	 * @generated
	 * @ordered
	 */
	protected static final boolean LHS_SPOT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isLhsSpot() <em>Lhs Spot</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLhsSpot()
	 * @generated
	 * @ordered
	 */
	protected boolean lhsSpot = LHS_SPOT_EDEFAULT;

	/**
	 * The default value of the '{@link #isLhsOptional() <em>Lhs Optional</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLhsOptional()
	 * @generated
	 * @ordered
	 */
	protected static final boolean LHS_OPTIONAL_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isLhsOptional() <em>Lhs Optional</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLhsOptional()
	 * @generated
	 * @ordered
	 */
	protected boolean lhsOptional = LHS_OPTIONAL_EDEFAULT;

	/**
	 * The default value of the '{@link #isRhsSlot() <em>Rhs Slot</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRhsSlot()
	 * @generated
	 * @ordered
	 */
	protected static final boolean RHS_SLOT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isRhsSlot() <em>Rhs Slot</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRhsSlot()
	 * @generated
	 * @ordered
	 */
	protected boolean rhsSlot = RHS_SLOT_EDEFAULT;

	/**
	 * The default value of the '{@link #isRhsSpot() <em>Rhs Spot</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRhsSpot()
	 * @generated
	 * @ordered
	 */
	protected static final boolean RHS_SPOT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isRhsSpot() <em>Rhs Spot</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRhsSpot()
	 * @generated
	 * @ordered
	 */
	protected boolean rhsSpot = RHS_SPOT_EDEFAULT;

	/**
	 * The default value of the '{@link #isRhsOptional() <em>Rhs Optional</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRhsOptional()
	 * @generated
	 * @ordered
	 */
	protected static final boolean RHS_OPTIONAL_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isRhsOptional() <em>Rhs Optional</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRhsOptional()
	 * @generated
	 * @ordered
	 */
	protected boolean rhsOptional = RHS_OPTIONAL_EDEFAULT;

	/**
	 * The default value of the '{@link #isLhsNonShipped() <em>Lhs Non Shipped</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLhsNonShipped()
	 * @generated
	 * @ordered
	 */
	protected static final boolean LHS_NON_SHIPPED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isLhsNonShipped() <em>Lhs Non Shipped</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLhsNonShipped()
	 * @generated
	 * @ordered
	 */
	protected boolean lhsNonShipped = LHS_NON_SHIPPED_EDEFAULT;

	/**
	 * The default value of the '{@link #isRhsNonShipped() <em>Rhs Non Shipped</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRhsNonShipped()
	 * @generated
	 * @ordered
	 */
	protected static final boolean RHS_NON_SHIPPED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isRhsNonShipped() <em>Rhs Non Shipped</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRhsNonShipped()
	 * @generated
	 * @ordered
	 */
	protected boolean rhsNonShipped = RHS_NON_SHIPPED_EDEFAULT;

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
	 * The cached value of the '{@link #getLoadAllocation() <em>Load Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadAllocation()
	 * @generated
	 * @ordered
	 */
	protected SlotAllocation loadAllocation;

	/**
	 * The cached value of the '{@link #getDischargeAllocation() <em>Discharge Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeAllocation()
	 * @generated
	 * @ordered
	 */
	protected SlotAllocation dischargeAllocation;

	/**
	 * The cached value of the '{@link #getOpenLoadAllocation() <em>Open Load Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOpenLoadAllocation()
	 * @generated
	 * @ordered
	 */
	protected OpenSlotAllocation openLoadAllocation;

	/**
	 * The cached value of the '{@link #getOpenDischargeAllocation() <em>Open Discharge Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOpenDischargeAllocation()
	 * @generated
	 * @ordered
	 */
	protected OpenSlotAllocation openDischargeAllocation;

	/**
	 * The cached value of the '{@link #getLhsEvent() <em>Lhs Event</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLhsEvent()
	 * @generated
	 * @ordered
	 */
	protected Event lhsEvent;

	/**
	 * The cached value of the '{@link #getRhsEvent() <em>Rhs Event</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRhsEvent()
	 * @generated
	 * @ordered
	 */
	protected Event rhsEvent;

	/**
	 * The cached value of the '{@link #getLhsGroupProfitAndLoss() <em>Lhs Group Profit And Loss</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLhsGroupProfitAndLoss()
	 * @generated
	 * @ordered
	 */
	protected ProfitAndLossContainer lhsGroupProfitAndLoss;

	/**
	 * The cached value of the '{@link #getRhsGroupProfitAndLoss() <em>Rhs Group Profit And Loss</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRhsGroupProfitAndLoss()
	 * @generated
	 * @ordered
	 */
	protected ProfitAndLossContainer rhsGroupProfitAndLoss;

	/**
	 * The cached value of the '{@link #getPaperDealAllocation() <em>Paper Deal Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPaperDealAllocation()
	 * @generated
	 * @ordered
	 */
	protected PaperDealAllocation paperDealAllocation;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ChangeSetRowDataImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ChangesetPackage.Literals.CHANGE_SET_ROW_DATA;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isPrimaryRecord() {
		return primaryRecord;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPrimaryRecord(boolean newPrimaryRecord) {
		boolean oldPrimaryRecord = primaryRecord;
		primaryRecord = newPrimaryRecord;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW_DATA__PRIMARY_RECORD, oldPrimaryRecord, primaryRecord));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getLhsName() {
		return lhsName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLhsName(String newLhsName) {
		String oldLhsName = lhsName;
		lhsName = newLhsName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_NAME, oldLhsName, lhsName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getRhsName() {
		return rhsName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRhsName(String newRhsName) {
		String oldRhsName = rhsName;
		rhsName = newRhsName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_NAME, oldRhsName, rhsName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ChangeSetRowData getLhsLink() {
		if (lhsLink != null && lhsLink.eIsProxy()) {
			InternalEObject oldLhsLink = (InternalEObject)lhsLink;
			lhsLink = (ChangeSetRowData)eResolveProxy(oldLhsLink);
			if (lhsLink != oldLhsLink) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_LINK, oldLhsLink, lhsLink));
			}
		}
		return lhsLink;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSetRowData basicGetLhsLink() {
		return lhsLink;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLhsLink(ChangeSetRowData newLhsLink) {
		ChangeSetRowData oldLhsLink = lhsLink;
		lhsLink = newLhsLink;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_LINK, oldLhsLink, lhsLink));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ChangeSetRowData getRhsLink() {
		if (rhsLink != null && rhsLink.eIsProxy()) {
			InternalEObject oldRhsLink = (InternalEObject)rhsLink;
			rhsLink = (ChangeSetRowData)eResolveProxy(oldRhsLink);
			if (rhsLink != oldRhsLink) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_LINK, oldRhsLink, rhsLink));
			}
		}
		return rhsLink;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSetRowData basicGetRhsLink() {
		return rhsLink;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRhsLink(ChangeSetRowData newRhsLink) {
		ChangeSetRowData oldRhsLink = rhsLink;
		rhsLink = newRhsLink;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_LINK, oldRhsLink, rhsLink));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isLhsSlot() {
		return lhsSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLhsSlot(boolean newLhsSlot) {
		boolean oldLhsSlot = lhsSlot;
		lhsSlot = newLhsSlot;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_SLOT, oldLhsSlot, lhsSlot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isLhsSpot() {
		return lhsSpot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLhsSpot(boolean newLhsSpot) {
		boolean oldLhsSpot = lhsSpot;
		lhsSpot = newLhsSpot;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_SPOT, oldLhsSpot, lhsSpot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isLhsOptional() {
		return lhsOptional;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLhsOptional(boolean newLhsOptional) {
		boolean oldLhsOptional = lhsOptional;
		lhsOptional = newLhsOptional;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_OPTIONAL, oldLhsOptional, lhsOptional));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isRhsSlot() {
		return rhsSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRhsSlot(boolean newRhsSlot) {
		boolean oldRhsSlot = rhsSlot;
		rhsSlot = newRhsSlot;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_SLOT, oldRhsSlot, rhsSlot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isRhsSpot() {
		return rhsSpot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRhsSpot(boolean newRhsSpot) {
		boolean oldRhsSpot = rhsSpot;
		rhsSpot = newRhsSpot;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_SPOT, oldRhsSpot, rhsSpot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isRhsOptional() {
		return rhsOptional;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRhsOptional(boolean newRhsOptional) {
		boolean oldRhsOptional = rhsOptional;
		rhsOptional = newRhsOptional;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_OPTIONAL, oldRhsOptional, rhsOptional));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isLhsNonShipped() {
		return lhsNonShipped;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLhsNonShipped(boolean newLhsNonShipped) {
		boolean oldLhsNonShipped = lhsNonShipped;
		lhsNonShipped = newLhsNonShipped;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_NON_SHIPPED, oldLhsNonShipped, lhsNonShipped));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isRhsNonShipped() {
		return rhsNonShipped;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRhsNonShipped(boolean newRhsNonShipped) {
		boolean oldRhsNonShipped = rhsNonShipped;
		rhsNonShipped = newRhsNonShipped;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_NON_SHIPPED, oldRhsNonShipped, rhsNonShipped));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LoadSlot getLoadSlot() {
		if (loadSlot != null && loadSlot.eIsProxy()) {
			InternalEObject oldLoadSlot = (InternalEObject)loadSlot;
			loadSlot = (LoadSlot)eResolveProxy(oldLoadSlot);
			if (loadSlot != oldLoadSlot) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_ROW_DATA__LOAD_SLOT, oldLoadSlot, loadSlot));
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
	@Override
	public void setLoadSlot(LoadSlot newLoadSlot) {
		LoadSlot oldLoadSlot = loadSlot;
		loadSlot = newLoadSlot;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW_DATA__LOAD_SLOT, oldLoadSlot, loadSlot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DischargeSlot getDischargeSlot() {
		if (dischargeSlot != null && dischargeSlot.eIsProxy()) {
			InternalEObject oldDischargeSlot = (InternalEObject)dischargeSlot;
			dischargeSlot = (DischargeSlot)eResolveProxy(oldDischargeSlot);
			if (dischargeSlot != oldDischargeSlot) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_ROW_DATA__DISCHARGE_SLOT, oldDischargeSlot, dischargeSlot));
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
	@Override
	public void setDischargeSlot(DischargeSlot newDischargeSlot) {
		DischargeSlot oldDischargeSlot = dischargeSlot;
		dischargeSlot = newDischargeSlot;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW_DATA__DISCHARGE_SLOT, oldDischargeSlot, dischargeSlot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SlotAllocation getLoadAllocation() {
		if (loadAllocation != null && loadAllocation.eIsProxy()) {
			InternalEObject oldLoadAllocation = (InternalEObject)loadAllocation;
			loadAllocation = (SlotAllocation)eResolveProxy(oldLoadAllocation);
			if (loadAllocation != oldLoadAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_ROW_DATA__LOAD_ALLOCATION, oldLoadAllocation, loadAllocation));
			}
		}
		return loadAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocation basicGetLoadAllocation() {
		return loadAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLoadAllocation(SlotAllocation newLoadAllocation) {
		SlotAllocation oldLoadAllocation = loadAllocation;
		loadAllocation = newLoadAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW_DATA__LOAD_ALLOCATION, oldLoadAllocation, loadAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SlotAllocation getDischargeAllocation() {
		if (dischargeAllocation != null && dischargeAllocation.eIsProxy()) {
			InternalEObject oldDischargeAllocation = (InternalEObject)dischargeAllocation;
			dischargeAllocation = (SlotAllocation)eResolveProxy(oldDischargeAllocation);
			if (dischargeAllocation != oldDischargeAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_ROW_DATA__DISCHARGE_ALLOCATION, oldDischargeAllocation, dischargeAllocation));
			}
		}
		return dischargeAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocation basicGetDischargeAllocation() {
		return dischargeAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDischargeAllocation(SlotAllocation newDischargeAllocation) {
		SlotAllocation oldDischargeAllocation = dischargeAllocation;
		dischargeAllocation = newDischargeAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW_DATA__DISCHARGE_ALLOCATION, oldDischargeAllocation, dischargeAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OpenSlotAllocation getOpenLoadAllocation() {
		if (openLoadAllocation != null && openLoadAllocation.eIsProxy()) {
			InternalEObject oldOpenLoadAllocation = (InternalEObject)openLoadAllocation;
			openLoadAllocation = (OpenSlotAllocation)eResolveProxy(oldOpenLoadAllocation);
			if (openLoadAllocation != oldOpenLoadAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_ROW_DATA__OPEN_LOAD_ALLOCATION, oldOpenLoadAllocation, openLoadAllocation));
			}
		}
		return openLoadAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OpenSlotAllocation basicGetOpenLoadAllocation() {
		return openLoadAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOpenLoadAllocation(OpenSlotAllocation newOpenLoadAllocation) {
		OpenSlotAllocation oldOpenLoadAllocation = openLoadAllocation;
		openLoadAllocation = newOpenLoadAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW_DATA__OPEN_LOAD_ALLOCATION, oldOpenLoadAllocation, openLoadAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OpenSlotAllocation getOpenDischargeAllocation() {
		if (openDischargeAllocation != null && openDischargeAllocation.eIsProxy()) {
			InternalEObject oldOpenDischargeAllocation = (InternalEObject)openDischargeAllocation;
			openDischargeAllocation = (OpenSlotAllocation)eResolveProxy(oldOpenDischargeAllocation);
			if (openDischargeAllocation != oldOpenDischargeAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_ROW_DATA__OPEN_DISCHARGE_ALLOCATION, oldOpenDischargeAllocation, openDischargeAllocation));
			}
		}
		return openDischargeAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OpenSlotAllocation basicGetOpenDischargeAllocation() {
		return openDischargeAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOpenDischargeAllocation(OpenSlotAllocation newOpenDischargeAllocation) {
		OpenSlotAllocation oldOpenDischargeAllocation = openDischargeAllocation;
		openDischargeAllocation = newOpenDischargeAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW_DATA__OPEN_DISCHARGE_ALLOCATION, oldOpenDischargeAllocation, openDischargeAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Event getLhsEvent() {
		if (lhsEvent != null && lhsEvent.eIsProxy()) {
			InternalEObject oldLhsEvent = (InternalEObject)lhsEvent;
			lhsEvent = (Event)eResolveProxy(oldLhsEvent);
			if (lhsEvent != oldLhsEvent) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_EVENT, oldLhsEvent, lhsEvent));
			}
		}
		return lhsEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Event basicGetLhsEvent() {
		return lhsEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLhsEvent(Event newLhsEvent) {
		Event oldLhsEvent = lhsEvent;
		lhsEvent = newLhsEvent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_EVENT, oldLhsEvent, lhsEvent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Event getRhsEvent() {
		if (rhsEvent != null && rhsEvent.eIsProxy()) {
			InternalEObject oldRhsEvent = (InternalEObject)rhsEvent;
			rhsEvent = (Event)eResolveProxy(oldRhsEvent);
			if (rhsEvent != oldRhsEvent) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_EVENT, oldRhsEvent, rhsEvent));
			}
		}
		return rhsEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Event basicGetRhsEvent() {
		return rhsEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRhsEvent(Event newRhsEvent) {
		Event oldRhsEvent = rhsEvent;
		rhsEvent = newRhsEvent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_EVENT, oldRhsEvent, rhsEvent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ProfitAndLossContainer getLhsGroupProfitAndLoss() {
		if (lhsGroupProfitAndLoss != null && lhsGroupProfitAndLoss.eIsProxy()) {
			InternalEObject oldLhsGroupProfitAndLoss = (InternalEObject)lhsGroupProfitAndLoss;
			lhsGroupProfitAndLoss = (ProfitAndLossContainer)eResolveProxy(oldLhsGroupProfitAndLoss);
			if (lhsGroupProfitAndLoss != oldLhsGroupProfitAndLoss) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_GROUP_PROFIT_AND_LOSS, oldLhsGroupProfitAndLoss, lhsGroupProfitAndLoss));
			}
		}
		return lhsGroupProfitAndLoss;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProfitAndLossContainer basicGetLhsGroupProfitAndLoss() {
		return lhsGroupProfitAndLoss;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLhsGroupProfitAndLoss(ProfitAndLossContainer newLhsGroupProfitAndLoss) {
		ProfitAndLossContainer oldLhsGroupProfitAndLoss = lhsGroupProfitAndLoss;
		lhsGroupProfitAndLoss = newLhsGroupProfitAndLoss;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_GROUP_PROFIT_AND_LOSS, oldLhsGroupProfitAndLoss, lhsGroupProfitAndLoss));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ProfitAndLossContainer getRhsGroupProfitAndLoss() {
		if (rhsGroupProfitAndLoss != null && rhsGroupProfitAndLoss.eIsProxy()) {
			InternalEObject oldRhsGroupProfitAndLoss = (InternalEObject)rhsGroupProfitAndLoss;
			rhsGroupProfitAndLoss = (ProfitAndLossContainer)eResolveProxy(oldRhsGroupProfitAndLoss);
			if (rhsGroupProfitAndLoss != oldRhsGroupProfitAndLoss) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_GROUP_PROFIT_AND_LOSS, oldRhsGroupProfitAndLoss, rhsGroupProfitAndLoss));
			}
		}
		return rhsGroupProfitAndLoss;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProfitAndLossContainer basicGetRhsGroupProfitAndLoss() {
		return rhsGroupProfitAndLoss;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRhsGroupProfitAndLoss(ProfitAndLossContainer newRhsGroupProfitAndLoss) {
		ProfitAndLossContainer oldRhsGroupProfitAndLoss = rhsGroupProfitAndLoss;
		rhsGroupProfitAndLoss = newRhsGroupProfitAndLoss;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_GROUP_PROFIT_AND_LOSS, oldRhsGroupProfitAndLoss, rhsGroupProfitAndLoss));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PaperDealAllocation getPaperDealAllocation() {
		if (paperDealAllocation != null && paperDealAllocation.eIsProxy()) {
			InternalEObject oldPaperDealAllocation = (InternalEObject)paperDealAllocation;
			paperDealAllocation = (PaperDealAllocation)eResolveProxy(oldPaperDealAllocation);
			if (paperDealAllocation != oldPaperDealAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_ROW_DATA__PAPER_DEAL_ALLOCATION, oldPaperDealAllocation, paperDealAllocation));
			}
		}
		return paperDealAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PaperDealAllocation basicGetPaperDealAllocation() {
		return paperDealAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPaperDealAllocation(PaperDealAllocation newPaperDealAllocation) {
		PaperDealAllocation oldPaperDealAllocation = paperDealAllocation;
		paperDealAllocation = newPaperDealAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW_DATA__PAPER_DEAL_ALLOCATION, oldPaperDealAllocation, paperDealAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getVesselCharterNumber() {
		return vesselCharterNumber;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVesselCharterNumber(int newVesselCharterNumber) {
		int oldVesselCharterNumber = vesselCharterNumber;
		vesselCharterNumber = newVesselCharterNumber;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW_DATA__VESSEL_CHARTER_NUMBER, oldVesselCharterNumber, vesselCharterNumber));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ChangeSetRowDataGroup getRowDataGroup() {
		if (eContainerFeatureID() != ChangesetPackage.CHANGE_SET_ROW_DATA__ROW_DATA_GROUP) return null;
		return (ChangeSetRowDataGroup)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetRowDataGroup(ChangeSetRowDataGroup newRowDataGroup, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newRowDataGroup, ChangesetPackage.CHANGE_SET_ROW_DATA__ROW_DATA_GROUP, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRowDataGroup(ChangeSetRowDataGroup newRowDataGroup) {
		if (newRowDataGroup != eInternalContainer() || (eContainerFeatureID() != ChangesetPackage.CHANGE_SET_ROW_DATA__ROW_DATA_GROUP && newRowDataGroup != null)) {
			if (EcoreUtil.isAncestor(this, newRowDataGroup))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newRowDataGroup != null)
				msgs = ((InternalEObject)newRowDataGroup).eInverseAdd(this, ChangesetPackage.CHANGE_SET_ROW_DATA_GROUP__MEMBERS, ChangeSetRowDataGroup.class, msgs);
			msgs = basicSetRowDataGroup(newRowDataGroup, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW_DATA__ROW_DATA_GROUP, newRowDataGroup, newRowDataGroup));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EventGrouping getEventGrouping() {
		if (eventGrouping != null && eventGrouping.eIsProxy()) {
			InternalEObject oldEventGrouping = (InternalEObject)eventGrouping;
			eventGrouping = (EventGrouping)eResolveProxy(oldEventGrouping);
			if (eventGrouping != oldEventGrouping) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_ROW_DATA__EVENT_GROUPING, oldEventGrouping, eventGrouping));
			}
		}
		return eventGrouping;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EventGrouping basicGetEventGrouping() {
		return eventGrouping;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEventGrouping(EventGrouping newEventGrouping) {
		EventGrouping oldEventGrouping = eventGrouping;
		eventGrouping = newEventGrouping;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW_DATA__EVENT_GROUPING, oldEventGrouping, eventGrouping));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getVesselName() {
		return vesselName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVesselName(String newVesselName) {
		String oldVesselName = vesselName;
		vesselName = newVesselName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW_DATA__VESSEL_NAME, oldVesselName, vesselName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getVesselShortName() {
		return vesselShortName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVesselShortName(String newVesselShortName) {
		String oldVesselShortName = vesselShortName;
		vesselShortName = newVesselShortName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW_DATA__VESSEL_SHORT_NAME, oldVesselShortName, vesselShortName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ChangeSetVesselType getVesselType() {
		return vesselType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVesselType(ChangeSetVesselType newVesselType) {
		ChangeSetVesselType oldVesselType = vesselType;
		vesselType = newVesselType == null ? VESSEL_TYPE_EDEFAULT : newVesselType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW_DATA__VESSEL_TYPE, oldVesselType, vesselType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ChangesetPackage.CHANGE_SET_ROW_DATA__ROW_DATA_GROUP:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetRowDataGroup((ChangeSetRowDataGroup)otherEnd, msgs);
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
			case ChangesetPackage.CHANGE_SET_ROW_DATA__ROW_DATA_GROUP:
				return basicSetRowDataGroup(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case ChangesetPackage.CHANGE_SET_ROW_DATA__ROW_DATA_GROUP:
				return eInternalContainer().eInverseRemove(this, ChangesetPackage.CHANGE_SET_ROW_DATA_GROUP__MEMBERS, ChangeSetRowDataGroup.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ChangesetPackage.CHANGE_SET_ROW_DATA__PRIMARY_RECORD:
				return isPrimaryRecord();
			case ChangesetPackage.CHANGE_SET_ROW_DATA__ROW_DATA_GROUP:
				return getRowDataGroup();
			case ChangesetPackage.CHANGE_SET_ROW_DATA__EVENT_GROUPING:
				if (resolve) return getEventGrouping();
				return basicGetEventGrouping();
			case ChangesetPackage.CHANGE_SET_ROW_DATA__VESSEL_NAME:
				return getVesselName();
			case ChangesetPackage.CHANGE_SET_ROW_DATA__VESSEL_SHORT_NAME:
				return getVesselShortName();
			case ChangesetPackage.CHANGE_SET_ROW_DATA__VESSEL_TYPE:
				return getVesselType();
			case ChangesetPackage.CHANGE_SET_ROW_DATA__VESSEL_CHARTER_NUMBER:
				return getVesselCharterNumber();
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_NAME:
				return getLhsName();
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_NAME:
				return getRhsName();
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_LINK:
				if (resolve) return getLhsLink();
				return basicGetLhsLink();
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_LINK:
				if (resolve) return getRhsLink();
				return basicGetRhsLink();
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_SLOT:
				return isLhsSlot();
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_SPOT:
				return isLhsSpot();
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_OPTIONAL:
				return isLhsOptional();
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_SLOT:
				return isRhsSlot();
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_SPOT:
				return isRhsSpot();
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_OPTIONAL:
				return isRhsOptional();
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_NON_SHIPPED:
				return isLhsNonShipped();
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_NON_SHIPPED:
				return isRhsNonShipped();
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LOAD_SLOT:
				if (resolve) return getLoadSlot();
				return basicGetLoadSlot();
			case ChangesetPackage.CHANGE_SET_ROW_DATA__DISCHARGE_SLOT:
				if (resolve) return getDischargeSlot();
				return basicGetDischargeSlot();
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LOAD_ALLOCATION:
				if (resolve) return getLoadAllocation();
				return basicGetLoadAllocation();
			case ChangesetPackage.CHANGE_SET_ROW_DATA__DISCHARGE_ALLOCATION:
				if (resolve) return getDischargeAllocation();
				return basicGetDischargeAllocation();
			case ChangesetPackage.CHANGE_SET_ROW_DATA__OPEN_LOAD_ALLOCATION:
				if (resolve) return getOpenLoadAllocation();
				return basicGetOpenLoadAllocation();
			case ChangesetPackage.CHANGE_SET_ROW_DATA__OPEN_DISCHARGE_ALLOCATION:
				if (resolve) return getOpenDischargeAllocation();
				return basicGetOpenDischargeAllocation();
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_EVENT:
				if (resolve) return getLhsEvent();
				return basicGetLhsEvent();
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_EVENT:
				if (resolve) return getRhsEvent();
				return basicGetRhsEvent();
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_GROUP_PROFIT_AND_LOSS:
				if (resolve) return getLhsGroupProfitAndLoss();
				return basicGetLhsGroupProfitAndLoss();
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_GROUP_PROFIT_AND_LOSS:
				if (resolve) return getRhsGroupProfitAndLoss();
				return basicGetRhsGroupProfitAndLoss();
			case ChangesetPackage.CHANGE_SET_ROW_DATA__PAPER_DEAL_ALLOCATION:
				if (resolve) return getPaperDealAllocation();
				return basicGetPaperDealAllocation();
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
			case ChangesetPackage.CHANGE_SET_ROW_DATA__PRIMARY_RECORD:
				setPrimaryRecord((Boolean)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__ROW_DATA_GROUP:
				setRowDataGroup((ChangeSetRowDataGroup)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__EVENT_GROUPING:
				setEventGrouping((EventGrouping)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__VESSEL_NAME:
				setVesselName((String)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__VESSEL_SHORT_NAME:
				setVesselShortName((String)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__VESSEL_TYPE:
				setVesselType((ChangeSetVesselType)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__VESSEL_CHARTER_NUMBER:
				setVesselCharterNumber((Integer)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_NAME:
				setLhsName((String)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_NAME:
				setRhsName((String)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_LINK:
				setLhsLink((ChangeSetRowData)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_LINK:
				setRhsLink((ChangeSetRowData)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_SLOT:
				setLhsSlot((Boolean)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_SPOT:
				setLhsSpot((Boolean)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_OPTIONAL:
				setLhsOptional((Boolean)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_SLOT:
				setRhsSlot((Boolean)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_SPOT:
				setRhsSpot((Boolean)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_OPTIONAL:
				setRhsOptional((Boolean)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_NON_SHIPPED:
				setLhsNonShipped((Boolean)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_NON_SHIPPED:
				setRhsNonShipped((Boolean)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LOAD_SLOT:
				setLoadSlot((LoadSlot)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__DISCHARGE_SLOT:
				setDischargeSlot((DischargeSlot)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LOAD_ALLOCATION:
				setLoadAllocation((SlotAllocation)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__DISCHARGE_ALLOCATION:
				setDischargeAllocation((SlotAllocation)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__OPEN_LOAD_ALLOCATION:
				setOpenLoadAllocation((OpenSlotAllocation)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__OPEN_DISCHARGE_ALLOCATION:
				setOpenDischargeAllocation((OpenSlotAllocation)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_EVENT:
				setLhsEvent((Event)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_EVENT:
				setRhsEvent((Event)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_GROUP_PROFIT_AND_LOSS:
				setLhsGroupProfitAndLoss((ProfitAndLossContainer)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_GROUP_PROFIT_AND_LOSS:
				setRhsGroupProfitAndLoss((ProfitAndLossContainer)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__PAPER_DEAL_ALLOCATION:
				setPaperDealAllocation((PaperDealAllocation)newValue);
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
			case ChangesetPackage.CHANGE_SET_ROW_DATA__PRIMARY_RECORD:
				setPrimaryRecord(PRIMARY_RECORD_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__ROW_DATA_GROUP:
				setRowDataGroup((ChangeSetRowDataGroup)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__EVENT_GROUPING:
				setEventGrouping((EventGrouping)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__VESSEL_NAME:
				setVesselName(VESSEL_NAME_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__VESSEL_SHORT_NAME:
				setVesselShortName(VESSEL_SHORT_NAME_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__VESSEL_TYPE:
				setVesselType(VESSEL_TYPE_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__VESSEL_CHARTER_NUMBER:
				setVesselCharterNumber(VESSEL_CHARTER_NUMBER_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_NAME:
				setLhsName(LHS_NAME_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_NAME:
				setRhsName(RHS_NAME_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_LINK:
				setLhsLink((ChangeSetRowData)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_LINK:
				setRhsLink((ChangeSetRowData)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_SLOT:
				setLhsSlot(LHS_SLOT_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_SPOT:
				setLhsSpot(LHS_SPOT_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_OPTIONAL:
				setLhsOptional(LHS_OPTIONAL_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_SLOT:
				setRhsSlot(RHS_SLOT_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_SPOT:
				setRhsSpot(RHS_SPOT_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_OPTIONAL:
				setRhsOptional(RHS_OPTIONAL_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_NON_SHIPPED:
				setLhsNonShipped(LHS_NON_SHIPPED_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_NON_SHIPPED:
				setRhsNonShipped(RHS_NON_SHIPPED_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LOAD_SLOT:
				setLoadSlot((LoadSlot)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__DISCHARGE_SLOT:
				setDischargeSlot((DischargeSlot)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LOAD_ALLOCATION:
				setLoadAllocation((SlotAllocation)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__DISCHARGE_ALLOCATION:
				setDischargeAllocation((SlotAllocation)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__OPEN_LOAD_ALLOCATION:
				setOpenLoadAllocation((OpenSlotAllocation)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__OPEN_DISCHARGE_ALLOCATION:
				setOpenDischargeAllocation((OpenSlotAllocation)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_EVENT:
				setLhsEvent((Event)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_EVENT:
				setRhsEvent((Event)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_GROUP_PROFIT_AND_LOSS:
				setLhsGroupProfitAndLoss((ProfitAndLossContainer)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_GROUP_PROFIT_AND_LOSS:
				setRhsGroupProfitAndLoss((ProfitAndLossContainer)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__PAPER_DEAL_ALLOCATION:
				setPaperDealAllocation((PaperDealAllocation)null);
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
			case ChangesetPackage.CHANGE_SET_ROW_DATA__PRIMARY_RECORD:
				return primaryRecord != PRIMARY_RECORD_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__ROW_DATA_GROUP:
				return getRowDataGroup() != null;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__EVENT_GROUPING:
				return eventGrouping != null;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__VESSEL_NAME:
				return VESSEL_NAME_EDEFAULT == null ? vesselName != null : !VESSEL_NAME_EDEFAULT.equals(vesselName);
			case ChangesetPackage.CHANGE_SET_ROW_DATA__VESSEL_SHORT_NAME:
				return VESSEL_SHORT_NAME_EDEFAULT == null ? vesselShortName != null : !VESSEL_SHORT_NAME_EDEFAULT.equals(vesselShortName);
			case ChangesetPackage.CHANGE_SET_ROW_DATA__VESSEL_TYPE:
				return vesselType != VESSEL_TYPE_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__VESSEL_CHARTER_NUMBER:
				return vesselCharterNumber != VESSEL_CHARTER_NUMBER_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_NAME:
				return LHS_NAME_EDEFAULT == null ? lhsName != null : !LHS_NAME_EDEFAULT.equals(lhsName);
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_NAME:
				return RHS_NAME_EDEFAULT == null ? rhsName != null : !RHS_NAME_EDEFAULT.equals(rhsName);
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_LINK:
				return lhsLink != null;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_LINK:
				return rhsLink != null;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_SLOT:
				return lhsSlot != LHS_SLOT_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_SPOT:
				return lhsSpot != LHS_SPOT_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_OPTIONAL:
				return lhsOptional != LHS_OPTIONAL_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_SLOT:
				return rhsSlot != RHS_SLOT_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_SPOT:
				return rhsSpot != RHS_SPOT_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_OPTIONAL:
				return rhsOptional != RHS_OPTIONAL_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_NON_SHIPPED:
				return lhsNonShipped != LHS_NON_SHIPPED_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_NON_SHIPPED:
				return rhsNonShipped != RHS_NON_SHIPPED_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LOAD_SLOT:
				return loadSlot != null;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__DISCHARGE_SLOT:
				return dischargeSlot != null;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LOAD_ALLOCATION:
				return loadAllocation != null;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__DISCHARGE_ALLOCATION:
				return dischargeAllocation != null;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__OPEN_LOAD_ALLOCATION:
				return openLoadAllocation != null;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__OPEN_DISCHARGE_ALLOCATION:
				return openDischargeAllocation != null;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_EVENT:
				return lhsEvent != null;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_EVENT:
				return rhsEvent != null;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__LHS_GROUP_PROFIT_AND_LOSS:
				return lhsGroupProfitAndLoss != null;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__RHS_GROUP_PROFIT_AND_LOSS:
				return rhsGroupProfitAndLoss != null;
			case ChangesetPackage.CHANGE_SET_ROW_DATA__PAPER_DEAL_ALLOCATION:
				return paperDealAllocation != null;
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

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (primaryRecord: ");
		result.append(primaryRecord);
		result.append(", vesselName: ");
		result.append(vesselName);
		result.append(", vesselShortName: ");
		result.append(vesselShortName);
		result.append(", vesselType: ");
		result.append(vesselType);
		result.append(", vesselCharterNumber: ");
		result.append(vesselCharterNumber);
		result.append(", lhsName: ");
		result.append(lhsName);
		result.append(", rhsName: ");
		result.append(rhsName);
		result.append(", lhsSlot: ");
		result.append(lhsSlot);
		result.append(", lhsSpot: ");
		result.append(lhsSpot);
		result.append(", lhsOptional: ");
		result.append(lhsOptional);
		result.append(", rhsSlot: ");
		result.append(rhsSlot);
		result.append(", rhsSpot: ");
		result.append(rhsSpot);
		result.append(", rhsOptional: ");
		result.append(rhsOptional);
		result.append(", lhsNonShipped: ");
		result.append(lhsNonShipped);
		result.append(", rhsNonShipped: ");
		result.append(rhsNonShipped);
		result.append(')');
		return result.toString();
	}

} //ChangeSetRowDataImpl
