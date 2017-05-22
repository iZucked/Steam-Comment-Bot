/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model.impl;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Change Set Table Row</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#getLhsName <em>Lhs Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#getRhsName <em>Rhs Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#getLhsBefore <em>Lhs Before</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#getLhsAfter <em>Lhs After</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#getRhsBefore <em>Rhs Before</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#getRhsAfter <em>Rhs After</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#getBeforeVesselName <em>Before Vessel Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#getBeforeVesselShortName <em>Before Vessel Short Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#getAfterVesselName <em>After Vessel Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#getAfterVesselShortName <em>After Vessel Short Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#isWiringChange <em>Wiring Change</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#isVesselChange <em>Vessel Change</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#getPreviousRHS <em>Previous RHS</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#getNextLHS <em>Next LHS</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#isLhsSlot <em>Lhs Slot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#isLhsSpot <em>Lhs Spot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#isLhsOptional <em>Lhs Optional</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#isLhsValid <em>Lhs Valid</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#isLhsNonShipped <em>Lhs Non Shipped</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#isRhsSlot <em>Rhs Slot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#isRhsSpot <em>Rhs Spot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#isRhsOptional <em>Rhs Optional</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#isRhsValid <em>Rhs Valid</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#isRhsNonShipped <em>Rhs Non Shipped</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ChangeSetTableRowImpl extends MinimalEObjectImpl.Container implements ChangeSetTableRow {
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
	 * The cached value of the '{@link #getLhsBefore() <em>Lhs Before</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLhsBefore()
	 * @generated
	 * @ordered
	 */
	protected ChangeSetRowData lhsBefore;

	/**
	 * The cached value of the '{@link #getLhsAfter() <em>Lhs After</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLhsAfter()
	 * @generated
	 * @ordered
	 */
	protected ChangeSetRowData lhsAfter;

	/**
	 * The cached value of the '{@link #getRhsBefore() <em>Rhs Before</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRhsBefore()
	 * @generated
	 * @ordered
	 */
	protected ChangeSetRowData rhsBefore;

	/**
	 * The cached value of the '{@link #getRhsAfter() <em>Rhs After</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRhsAfter()
	 * @generated
	 * @ordered
	 */
	protected ChangeSetRowData rhsAfter;

	/**
	 * The default value of the '{@link #getBeforeVesselName() <em>Before Vessel Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBeforeVesselName()
	 * @generated
	 * @ordered
	 */
	protected static final String BEFORE_VESSEL_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBeforeVesselName() <em>Before Vessel Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBeforeVesselName()
	 * @generated
	 * @ordered
	 */
	protected String beforeVesselName = BEFORE_VESSEL_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getBeforeVesselShortName() <em>Before Vessel Short Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBeforeVesselShortName()
	 * @generated
	 * @ordered
	 */
	protected static final String BEFORE_VESSEL_SHORT_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBeforeVesselShortName() <em>Before Vessel Short Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBeforeVesselShortName()
	 * @generated
	 * @ordered
	 */
	protected String beforeVesselShortName = BEFORE_VESSEL_SHORT_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getAfterVesselName() <em>After Vessel Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAfterVesselName()
	 * @generated
	 * @ordered
	 */
	protected static final String AFTER_VESSEL_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAfterVesselName() <em>After Vessel Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAfterVesselName()
	 * @generated
	 * @ordered
	 */
	protected String afterVesselName = AFTER_VESSEL_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getAfterVesselShortName() <em>After Vessel Short Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAfterVesselShortName()
	 * @generated
	 * @ordered
	 */
	protected static final String AFTER_VESSEL_SHORT_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAfterVesselShortName() <em>After Vessel Short Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAfterVesselShortName()
	 * @generated
	 * @ordered
	 */
	protected String afterVesselShortName = AFTER_VESSEL_SHORT_NAME_EDEFAULT;

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
	 * The cached value of the '{@link #getPreviousRHS() <em>Previous RHS</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPreviousRHS()
	 * @generated
	 * @ordered
	 */
	protected ChangeSetTableRow previousRHS;

	/**
	 * The cached value of the '{@link #getNextLHS() <em>Next LHS</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNextLHS()
	 * @generated
	 * @ordered
	 */
	protected ChangeSetTableRow nextLHS;

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
	 * The default value of the '{@link #isLhsValid() <em>Lhs Valid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLhsValid()
	 * @generated
	 * @ordered
	 */
	protected static final boolean LHS_VALID_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isLhsValid() <em>Lhs Valid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLhsValid()
	 * @generated
	 * @ordered
	 */
	protected boolean lhsValid = LHS_VALID_EDEFAULT;

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
	 * The default value of the '{@link #isRhsValid() <em>Rhs Valid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRhsValid()
	 * @generated
	 * @ordered
	 */
	protected static final boolean RHS_VALID_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isRhsValid() <em>Rhs Valid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRhsValid()
	 * @generated
	 * @ordered
	 */
	protected boolean rhsValid = RHS_VALID_EDEFAULT;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ChangeSetTableRowImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ChangesetPackage.Literals.CHANGE_SET_TABLE_ROW;
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
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_NAME, oldLhsName, lhsName));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_NAME, oldRhsName, rhsName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSetRowData getLhsBefore() {
		if (lhsBefore != null && lhsBefore.eIsProxy()) {
			InternalEObject oldLhsBefore = (InternalEObject)lhsBefore;
			lhsBefore = (ChangeSetRowData)eResolveProxy(oldLhsBefore);
			if (lhsBefore != oldLhsBefore) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_BEFORE, oldLhsBefore, lhsBefore));
			}
		}
		return lhsBefore;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSetRowData basicGetLhsBefore() {
		return lhsBefore;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLhsBefore(ChangeSetRowData newLhsBefore) {
		ChangeSetRowData oldLhsBefore = lhsBefore;
		lhsBefore = newLhsBefore;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_BEFORE, oldLhsBefore, lhsBefore));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSetRowData getLhsAfter() {
		if (lhsAfter != null && lhsAfter.eIsProxy()) {
			InternalEObject oldLhsAfter = (InternalEObject)lhsAfter;
			lhsAfter = (ChangeSetRowData)eResolveProxy(oldLhsAfter);
			if (lhsAfter != oldLhsAfter) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_AFTER, oldLhsAfter, lhsAfter));
			}
		}
		return lhsAfter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSetRowData basicGetLhsAfter() {
		return lhsAfter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLhsAfter(ChangeSetRowData newLhsAfter) {
		ChangeSetRowData oldLhsAfter = lhsAfter;
		lhsAfter = newLhsAfter;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_AFTER, oldLhsAfter, lhsAfter));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSetRowData getRhsBefore() {
		if (rhsBefore != null && rhsBefore.eIsProxy()) {
			InternalEObject oldRhsBefore = (InternalEObject)rhsBefore;
			rhsBefore = (ChangeSetRowData)eResolveProxy(oldRhsBefore);
			if (rhsBefore != oldRhsBefore) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_BEFORE, oldRhsBefore, rhsBefore));
			}
		}
		return rhsBefore;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSetRowData basicGetRhsBefore() {
		return rhsBefore;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRhsBefore(ChangeSetRowData newRhsBefore) {
		ChangeSetRowData oldRhsBefore = rhsBefore;
		rhsBefore = newRhsBefore;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_BEFORE, oldRhsBefore, rhsBefore));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSetRowData getRhsAfter() {
		if (rhsAfter != null && rhsAfter.eIsProxy()) {
			InternalEObject oldRhsAfter = (InternalEObject)rhsAfter;
			rhsAfter = (ChangeSetRowData)eResolveProxy(oldRhsAfter);
			if (rhsAfter != oldRhsAfter) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_AFTER, oldRhsAfter, rhsAfter));
			}
		}
		return rhsAfter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSetRowData basicGetRhsAfter() {
		return rhsAfter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRhsAfter(ChangeSetRowData newRhsAfter) {
		ChangeSetRowData oldRhsAfter = rhsAfter;
		rhsAfter = newRhsAfter;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_AFTER, oldRhsAfter, rhsAfter));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getBeforeVesselName() {
		return beforeVesselName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBeforeVesselName(String newBeforeVesselName) {
		String oldBeforeVesselName = beforeVesselName;
		beforeVesselName = newBeforeVesselName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__BEFORE_VESSEL_NAME, oldBeforeVesselName, beforeVesselName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getBeforeVesselShortName() {
		return beforeVesselShortName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBeforeVesselShortName(String newBeforeVesselShortName) {
		String oldBeforeVesselShortName = beforeVesselShortName;
		beforeVesselShortName = newBeforeVesselShortName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__BEFORE_VESSEL_SHORT_NAME, oldBeforeVesselShortName, beforeVesselShortName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getAfterVesselName() {
		return afterVesselName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAfterVesselName(String newAfterVesselName) {
		String oldAfterVesselName = afterVesselName;
		afterVesselName = newAfterVesselName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__AFTER_VESSEL_NAME, oldAfterVesselName, afterVesselName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getAfterVesselShortName() {
		return afterVesselShortName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAfterVesselShortName(String newAfterVesselShortName) {
		String oldAfterVesselShortName = afterVesselShortName;
		afterVesselShortName = newAfterVesselShortName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__AFTER_VESSEL_SHORT_NAME, oldAfterVesselShortName, afterVesselShortName));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__WIRING_CHANGE, oldWiringChange, wiringChange));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__VESSEL_CHANGE, oldVesselChange, vesselChange));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSetTableRow getPreviousRHS() {
		if (previousRHS != null && previousRHS.eIsProxy()) {
			InternalEObject oldPreviousRHS = (InternalEObject)previousRHS;
			previousRHS = (ChangeSetTableRow)eResolveProxy(oldPreviousRHS);
			if (previousRHS != oldPreviousRHS) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_TABLE_ROW__PREVIOUS_RHS, oldPreviousRHS, previousRHS));
			}
		}
		return previousRHS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSetTableRow basicGetPreviousRHS() {
		return previousRHS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPreviousRHS(ChangeSetTableRow newPreviousRHS, NotificationChain msgs) {
		ChangeSetTableRow oldPreviousRHS = previousRHS;
		previousRHS = newPreviousRHS;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__PREVIOUS_RHS, oldPreviousRHS, newPreviousRHS);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPreviousRHS(ChangeSetTableRow newPreviousRHS) {
		if (newPreviousRHS != previousRHS) {
			NotificationChain msgs = null;
			if (previousRHS != null)
				msgs = ((InternalEObject)previousRHS).eInverseRemove(this, ChangesetPackage.CHANGE_SET_TABLE_ROW__NEXT_LHS, ChangeSetTableRow.class, msgs);
			if (newPreviousRHS != null)
				msgs = ((InternalEObject)newPreviousRHS).eInverseAdd(this, ChangesetPackage.CHANGE_SET_TABLE_ROW__NEXT_LHS, ChangeSetTableRow.class, msgs);
			msgs = basicSetPreviousRHS(newPreviousRHS, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__PREVIOUS_RHS, newPreviousRHS, newPreviousRHS));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSetTableRow getNextLHS() {
		if (nextLHS != null && nextLHS.eIsProxy()) {
			InternalEObject oldNextLHS = (InternalEObject)nextLHS;
			nextLHS = (ChangeSetTableRow)eResolveProxy(oldNextLHS);
			if (nextLHS != oldNextLHS) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_TABLE_ROW__NEXT_LHS, oldNextLHS, nextLHS));
			}
		}
		return nextLHS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSetTableRow basicGetNextLHS() {
		return nextLHS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetNextLHS(ChangeSetTableRow newNextLHS, NotificationChain msgs) {
		ChangeSetTableRow oldNextLHS = nextLHS;
		nextLHS = newNextLHS;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__NEXT_LHS, oldNextLHS, newNextLHS);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNextLHS(ChangeSetTableRow newNextLHS) {
		if (newNextLHS != nextLHS) {
			NotificationChain msgs = null;
			if (nextLHS != null)
				msgs = ((InternalEObject)nextLHS).eInverseRemove(this, ChangesetPackage.CHANGE_SET_TABLE_ROW__PREVIOUS_RHS, ChangeSetTableRow.class, msgs);
			if (newNextLHS != null)
				msgs = ((InternalEObject)newNextLHS).eInverseAdd(this, ChangesetPackage.CHANGE_SET_TABLE_ROW__PREVIOUS_RHS, ChangeSetTableRow.class, msgs);
			msgs = basicSetNextLHS(newNextLHS, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__NEXT_LHS, newNextLHS, newNextLHS));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isLhsSlot() {
		return lhsSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLhsSlot(boolean newLhsSlot) {
		boolean oldLhsSlot = lhsSlot;
		lhsSlot = newLhsSlot;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_SLOT, oldLhsSlot, lhsSlot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isLhsSpot() {
		return lhsSpot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLhsSpot(boolean newLhsSpot) {
		boolean oldLhsSpot = lhsSpot;
		lhsSpot = newLhsSpot;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_SPOT, oldLhsSpot, lhsSpot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isLhsOptional() {
		return lhsOptional;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLhsOptional(boolean newLhsOptional) {
		boolean oldLhsOptional = lhsOptional;
		lhsOptional = newLhsOptional;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_OPTIONAL, oldLhsOptional, lhsOptional));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isLhsValid() {
		return lhsValid;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLhsValid(boolean newLhsValid) {
		boolean oldLhsValid = lhsValid;
		lhsValid = newLhsValid;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_VALID, oldLhsValid, lhsValid));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isLhsNonShipped() {
		return lhsNonShipped;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLhsNonShipped(boolean newLhsNonShipped) {
		boolean oldLhsNonShipped = lhsNonShipped;
		lhsNonShipped = newLhsNonShipped;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_NON_SHIPPED, oldLhsNonShipped, lhsNonShipped));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isRhsSlot() {
		return rhsSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRhsSlot(boolean newRhsSlot) {
		boolean oldRhsSlot = rhsSlot;
		rhsSlot = newRhsSlot;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_SLOT, oldRhsSlot, rhsSlot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isRhsSpot() {
		return rhsSpot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRhsSpot(boolean newRhsSpot) {
		boolean oldRhsSpot = rhsSpot;
		rhsSpot = newRhsSpot;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_SPOT, oldRhsSpot, rhsSpot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isRhsOptional() {
		return rhsOptional;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRhsOptional(boolean newRhsOptional) {
		boolean oldRhsOptional = rhsOptional;
		rhsOptional = newRhsOptional;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_OPTIONAL, oldRhsOptional, rhsOptional));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isRhsValid() {
		return rhsValid;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRhsValid(boolean newRhsValid) {
		boolean oldRhsValid = rhsValid;
		rhsValid = newRhsValid;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_VALID, oldRhsValid, rhsValid));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isRhsNonShipped() {
		return rhsNonShipped;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRhsNonShipped(boolean newRhsNonShipped) {
		boolean oldRhsNonShipped = rhsNonShipped;
		rhsNonShipped = newRhsNonShipped;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_NON_SHIPPED, oldRhsNonShipped, rhsNonShipped));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__PREVIOUS_RHS:
				if (previousRHS != null)
					msgs = ((InternalEObject)previousRHS).eInverseRemove(this, ChangesetPackage.CHANGE_SET_TABLE_ROW__NEXT_LHS, ChangeSetTableRow.class, msgs);
				return basicSetPreviousRHS((ChangeSetTableRow)otherEnd, msgs);
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__NEXT_LHS:
				if (nextLHS != null)
					msgs = ((InternalEObject)nextLHS).eInverseRemove(this, ChangesetPackage.CHANGE_SET_TABLE_ROW__PREVIOUS_RHS, ChangeSetTableRow.class, msgs);
				return basicSetNextLHS((ChangeSetTableRow)otherEnd, msgs);
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
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__PREVIOUS_RHS:
				return basicSetPreviousRHS(null, msgs);
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__NEXT_LHS:
				return basicSetNextLHS(null, msgs);
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
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_NAME:
				return getLhsName();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_NAME:
				return getRhsName();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_BEFORE:
				if (resolve) return getLhsBefore();
				return basicGetLhsBefore();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_AFTER:
				if (resolve) return getLhsAfter();
				return basicGetLhsAfter();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_BEFORE:
				if (resolve) return getRhsBefore();
				return basicGetRhsBefore();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_AFTER:
				if (resolve) return getRhsAfter();
				return basicGetRhsAfter();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__BEFORE_VESSEL_NAME:
				return getBeforeVesselName();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__BEFORE_VESSEL_SHORT_NAME:
				return getBeforeVesselShortName();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__AFTER_VESSEL_NAME:
				return getAfterVesselName();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__AFTER_VESSEL_SHORT_NAME:
				return getAfterVesselShortName();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__WIRING_CHANGE:
				return isWiringChange();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__VESSEL_CHANGE:
				return isVesselChange();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__PREVIOUS_RHS:
				if (resolve) return getPreviousRHS();
				return basicGetPreviousRHS();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__NEXT_LHS:
				if (resolve) return getNextLHS();
				return basicGetNextLHS();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_SLOT:
				return isLhsSlot();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_SPOT:
				return isLhsSpot();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_OPTIONAL:
				return isLhsOptional();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_VALID:
				return isLhsValid();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_NON_SHIPPED:
				return isLhsNonShipped();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_SLOT:
				return isRhsSlot();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_SPOT:
				return isRhsSpot();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_OPTIONAL:
				return isRhsOptional();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_VALID:
				return isRhsValid();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_NON_SHIPPED:
				return isRhsNonShipped();
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
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_NAME:
				setLhsName((String)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_NAME:
				setRhsName((String)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_BEFORE:
				setLhsBefore((ChangeSetRowData)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_AFTER:
				setLhsAfter((ChangeSetRowData)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_BEFORE:
				setRhsBefore((ChangeSetRowData)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_AFTER:
				setRhsAfter((ChangeSetRowData)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__BEFORE_VESSEL_NAME:
				setBeforeVesselName((String)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__BEFORE_VESSEL_SHORT_NAME:
				setBeforeVesselShortName((String)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__AFTER_VESSEL_NAME:
				setAfterVesselName((String)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__AFTER_VESSEL_SHORT_NAME:
				setAfterVesselShortName((String)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__WIRING_CHANGE:
				setWiringChange((Boolean)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__VESSEL_CHANGE:
				setVesselChange((Boolean)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__PREVIOUS_RHS:
				setPreviousRHS((ChangeSetTableRow)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__NEXT_LHS:
				setNextLHS((ChangeSetTableRow)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_SLOT:
				setLhsSlot((Boolean)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_SPOT:
				setLhsSpot((Boolean)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_OPTIONAL:
				setLhsOptional((Boolean)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_VALID:
				setLhsValid((Boolean)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_NON_SHIPPED:
				setLhsNonShipped((Boolean)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_SLOT:
				setRhsSlot((Boolean)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_SPOT:
				setRhsSpot((Boolean)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_OPTIONAL:
				setRhsOptional((Boolean)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_VALID:
				setRhsValid((Boolean)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_NON_SHIPPED:
				setRhsNonShipped((Boolean)newValue);
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
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_NAME:
				setLhsName(LHS_NAME_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_NAME:
				setRhsName(RHS_NAME_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_BEFORE:
				setLhsBefore((ChangeSetRowData)null);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_AFTER:
				setLhsAfter((ChangeSetRowData)null);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_BEFORE:
				setRhsBefore((ChangeSetRowData)null);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_AFTER:
				setRhsAfter((ChangeSetRowData)null);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__BEFORE_VESSEL_NAME:
				setBeforeVesselName(BEFORE_VESSEL_NAME_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__BEFORE_VESSEL_SHORT_NAME:
				setBeforeVesselShortName(BEFORE_VESSEL_SHORT_NAME_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__AFTER_VESSEL_NAME:
				setAfterVesselName(AFTER_VESSEL_NAME_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__AFTER_VESSEL_SHORT_NAME:
				setAfterVesselShortName(AFTER_VESSEL_SHORT_NAME_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__WIRING_CHANGE:
				setWiringChange(WIRING_CHANGE_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__VESSEL_CHANGE:
				setVesselChange(VESSEL_CHANGE_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__PREVIOUS_RHS:
				setPreviousRHS((ChangeSetTableRow)null);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__NEXT_LHS:
				setNextLHS((ChangeSetTableRow)null);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_SLOT:
				setLhsSlot(LHS_SLOT_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_SPOT:
				setLhsSpot(LHS_SPOT_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_OPTIONAL:
				setLhsOptional(LHS_OPTIONAL_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_VALID:
				setLhsValid(LHS_VALID_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_NON_SHIPPED:
				setLhsNonShipped(LHS_NON_SHIPPED_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_SLOT:
				setRhsSlot(RHS_SLOT_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_SPOT:
				setRhsSpot(RHS_SPOT_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_OPTIONAL:
				setRhsOptional(RHS_OPTIONAL_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_VALID:
				setRhsValid(RHS_VALID_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_NON_SHIPPED:
				setRhsNonShipped(RHS_NON_SHIPPED_EDEFAULT);
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
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_NAME:
				return LHS_NAME_EDEFAULT == null ? lhsName != null : !LHS_NAME_EDEFAULT.equals(lhsName);
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_NAME:
				return RHS_NAME_EDEFAULT == null ? rhsName != null : !RHS_NAME_EDEFAULT.equals(rhsName);
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_BEFORE:
				return lhsBefore != null;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_AFTER:
				return lhsAfter != null;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_BEFORE:
				return rhsBefore != null;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_AFTER:
				return rhsAfter != null;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__BEFORE_VESSEL_NAME:
				return BEFORE_VESSEL_NAME_EDEFAULT == null ? beforeVesselName != null : !BEFORE_VESSEL_NAME_EDEFAULT.equals(beforeVesselName);
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__BEFORE_VESSEL_SHORT_NAME:
				return BEFORE_VESSEL_SHORT_NAME_EDEFAULT == null ? beforeVesselShortName != null : !BEFORE_VESSEL_SHORT_NAME_EDEFAULT.equals(beforeVesselShortName);
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__AFTER_VESSEL_NAME:
				return AFTER_VESSEL_NAME_EDEFAULT == null ? afterVesselName != null : !AFTER_VESSEL_NAME_EDEFAULT.equals(afterVesselName);
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__AFTER_VESSEL_SHORT_NAME:
				return AFTER_VESSEL_SHORT_NAME_EDEFAULT == null ? afterVesselShortName != null : !AFTER_VESSEL_SHORT_NAME_EDEFAULT.equals(afterVesselShortName);
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__WIRING_CHANGE:
				return wiringChange != WIRING_CHANGE_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__VESSEL_CHANGE:
				return vesselChange != VESSEL_CHANGE_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__PREVIOUS_RHS:
				return previousRHS != null;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__NEXT_LHS:
				return nextLHS != null;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_SLOT:
				return lhsSlot != LHS_SLOT_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_SPOT:
				return lhsSpot != LHS_SPOT_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_OPTIONAL:
				return lhsOptional != LHS_OPTIONAL_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_VALID:
				return lhsValid != LHS_VALID_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_NON_SHIPPED:
				return lhsNonShipped != LHS_NON_SHIPPED_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_SLOT:
				return rhsSlot != RHS_SLOT_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_SPOT:
				return rhsSpot != RHS_SPOT_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_OPTIONAL:
				return rhsOptional != RHS_OPTIONAL_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_VALID:
				return rhsValid != RHS_VALID_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__RHS_NON_SHIPPED:
				return rhsNonShipped != RHS_NON_SHIPPED_EDEFAULT;
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
		result.append(", beforeVesselName: ");
		result.append(beforeVesselName);
		result.append(", beforeVesselShortName: ");
		result.append(beforeVesselShortName);
		result.append(", afterVesselName: ");
		result.append(afterVesselName);
		result.append(", afterVesselShortName: ");
		result.append(afterVesselShortName);
		result.append(", wiringChange: ");
		result.append(wiringChange);
		result.append(", vesselChange: ");
		result.append(vesselChange);
		result.append(", lhsSlot: ");
		result.append(lhsSlot);
		result.append(", lhsSpot: ");
		result.append(lhsSpot);
		result.append(", lhsOptional: ");
		result.append(lhsOptional);
		result.append(", lhsValid: ");
		result.append(lhsValid);
		result.append(", lhsNonShipped: ");
		result.append(lhsNonShipped);
		result.append(", rhsSlot: ");
		result.append(rhsSlot);
		result.append(", rhsSpot: ");
		result.append(rhsSpot);
		result.append(", rhsOptional: ");
		result.append(rhsOptional);
		result.append(", rhsValid: ");
		result.append(rhsValid);
		result.append(", rhsNonShipped: ");
		result.append(rhsNonShipped);
		result.append(')');
		return result.toString();
	}

} //ChangeSetTableRowImpl
