/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model.impl;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetVesselType;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetWiringGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage;
import java.lang.reflect.InvocationTargetException;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
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
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#getLhsBefore <em>Lhs Before</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#getLhsAfter <em>Lhs After</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#isLhsValid <em>Lhs Valid</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#getCurrentRhsBefore <em>Current Rhs Before</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#getCurrentRhsAfter <em>Current Rhs After</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#getPreviousRhsBefore <em>Previous Rhs Before</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#getPreviousRhsAfter <em>Previous Rhs After</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#isCurrentRhsValid <em>Current Rhs Valid</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#isWiringChange <em>Wiring Change</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#isVesselChange <em>Vessel Change</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#isDateChange <em>Date Change</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#getWiringGroup <em>Wiring Group</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetTableRowImpl#getTableGroup <em>Table Group</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ChangeSetTableRowImpl extends MinimalEObjectImpl.Container implements ChangeSetTableRow {
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
	 * The cached value of the '{@link #getCurrentRhsBefore() <em>Current Rhs Before</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCurrentRhsBefore()
	 * @generated
	 * @ordered
	 */
	protected ChangeSetRowData currentRhsBefore;

	/**
	 * The cached value of the '{@link #getCurrentRhsAfter() <em>Current Rhs After</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCurrentRhsAfter()
	 * @generated
	 * @ordered
	 */
	protected ChangeSetRowData currentRhsAfter;

	/**
	 * The cached value of the '{@link #getPreviousRhsBefore() <em>Previous Rhs Before</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPreviousRhsBefore()
	 * @generated
	 * @ordered
	 */
	protected ChangeSetRowData previousRhsBefore;

	/**
	 * The cached value of the '{@link #getPreviousRhsAfter() <em>Previous Rhs After</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPreviousRhsAfter()
	 * @generated
	 * @ordered
	 */
	protected ChangeSetRowData previousRhsAfter;

	/**
	 * The default value of the '{@link #isCurrentRhsValid() <em>Current Rhs Valid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isCurrentRhsValid()
	 * @generated
	 * @ordered
	 */
	protected static final boolean CURRENT_RHS_VALID_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isCurrentRhsValid() <em>Current Rhs Valid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isCurrentRhsValid()
	 * @generated
	 * @ordered
	 */
	protected boolean currentRhsValid = CURRENT_RHS_VALID_EDEFAULT;

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
	 * The default value of the '{@link #isDateChange() <em>Date Change</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDateChange()
	 * @generated
	 * @ordered
	 */
	protected static final boolean DATE_CHANGE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isDateChange() <em>Date Change</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDateChange()
	 * @generated
	 * @ordered
	 */
	protected boolean dateChange = DATE_CHANGE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getWiringGroup() <em>Wiring Group</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWiringGroup()
	 * @generated
	 * @ordered
	 */
	protected ChangeSetWiringGroup wiringGroup;


	/**
	 * The cached value of the '{@link #getTableGroup() <em>Table Group</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTableGroup()
	 * @generated
	 * @ordered
	 */
	protected ChangeSetTableGroup tableGroup;


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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
	public boolean isWiringChange() {
		return wiringChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
	public boolean isVesselChange() {
		return vesselChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
	public boolean isDateChange() {
		return dateChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDateChange(boolean newDateChange) {
		boolean oldDateChange = dateChange;
		dateChange = newDateChange;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__DATE_CHANGE, oldDateChange, dateChange));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isLhsValid() {
		return lhsValid;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
	public ChangeSetRowData getCurrentRhsBefore() {
		if (currentRhsBefore != null && currentRhsBefore.eIsProxy()) {
			InternalEObject oldCurrentRhsBefore = (InternalEObject)currentRhsBefore;
			currentRhsBefore = (ChangeSetRowData)eResolveProxy(oldCurrentRhsBefore);
			if (currentRhsBefore != oldCurrentRhsBefore) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_TABLE_ROW__CURRENT_RHS_BEFORE, oldCurrentRhsBefore, currentRhsBefore));
			}
		}
		return currentRhsBefore;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSetRowData basicGetCurrentRhsBefore() {
		return currentRhsBefore;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCurrentRhsBefore(ChangeSetRowData newCurrentRhsBefore) {
		ChangeSetRowData oldCurrentRhsBefore = currentRhsBefore;
		currentRhsBefore = newCurrentRhsBefore;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__CURRENT_RHS_BEFORE, oldCurrentRhsBefore, currentRhsBefore));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ChangeSetRowData getCurrentRhsAfter() {
		if (currentRhsAfter != null && currentRhsAfter.eIsProxy()) {
			InternalEObject oldCurrentRhsAfter = (InternalEObject)currentRhsAfter;
			currentRhsAfter = (ChangeSetRowData)eResolveProxy(oldCurrentRhsAfter);
			if (currentRhsAfter != oldCurrentRhsAfter) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_TABLE_ROW__CURRENT_RHS_AFTER, oldCurrentRhsAfter, currentRhsAfter));
			}
		}
		return currentRhsAfter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSetRowData basicGetCurrentRhsAfter() {
		return currentRhsAfter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCurrentRhsAfter(ChangeSetRowData newCurrentRhsAfter) {
		ChangeSetRowData oldCurrentRhsAfter = currentRhsAfter;
		currentRhsAfter = newCurrentRhsAfter;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__CURRENT_RHS_AFTER, oldCurrentRhsAfter, currentRhsAfter));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ChangeSetRowData getPreviousRhsBefore() {
		if (previousRhsBefore != null && previousRhsBefore.eIsProxy()) {
			InternalEObject oldPreviousRhsBefore = (InternalEObject)previousRhsBefore;
			previousRhsBefore = (ChangeSetRowData)eResolveProxy(oldPreviousRhsBefore);
			if (previousRhsBefore != oldPreviousRhsBefore) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_TABLE_ROW__PREVIOUS_RHS_BEFORE, oldPreviousRhsBefore, previousRhsBefore));
			}
		}
		return previousRhsBefore;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSetRowData basicGetPreviousRhsBefore() {
		return previousRhsBefore;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPreviousRhsBefore(ChangeSetRowData newPreviousRhsBefore) {
		ChangeSetRowData oldPreviousRhsBefore = previousRhsBefore;
		previousRhsBefore = newPreviousRhsBefore;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__PREVIOUS_RHS_BEFORE, oldPreviousRhsBefore, previousRhsBefore));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ChangeSetRowData getPreviousRhsAfter() {
		if (previousRhsAfter != null && previousRhsAfter.eIsProxy()) {
			InternalEObject oldPreviousRhsAfter = (InternalEObject)previousRhsAfter;
			previousRhsAfter = (ChangeSetRowData)eResolveProxy(oldPreviousRhsAfter);
			if (previousRhsAfter != oldPreviousRhsAfter) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_TABLE_ROW__PREVIOUS_RHS_AFTER, oldPreviousRhsAfter, previousRhsAfter));
			}
		}
		return previousRhsAfter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSetRowData basicGetPreviousRhsAfter() {
		return previousRhsAfter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPreviousRhsAfter(ChangeSetRowData newPreviousRhsAfter) {
		ChangeSetRowData oldPreviousRhsAfter = previousRhsAfter;
		previousRhsAfter = newPreviousRhsAfter;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__PREVIOUS_RHS_AFTER, oldPreviousRhsAfter, previousRhsAfter));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isCurrentRhsValid() {
		return currentRhsValid;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCurrentRhsValid(boolean newCurrentRhsValid) {
		boolean oldCurrentRhsValid = currentRhsValid;
		currentRhsValid = newCurrentRhsValid;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__CURRENT_RHS_VALID, oldCurrentRhsValid, currentRhsValid));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ChangeSetWiringGroup getWiringGroup() {
		if (wiringGroup != null && wiringGroup.eIsProxy()) {
			InternalEObject oldWiringGroup = (InternalEObject)wiringGroup;
			wiringGroup = (ChangeSetWiringGroup)eResolveProxy(oldWiringGroup);
			if (wiringGroup != oldWiringGroup) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_TABLE_ROW__WIRING_GROUP, oldWiringGroup, wiringGroup));
			}
		}
		return wiringGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSetWiringGroup basicGetWiringGroup() {
		return wiringGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetWiringGroup(ChangeSetWiringGroup newWiringGroup, NotificationChain msgs) {
		ChangeSetWiringGroup oldWiringGroup = wiringGroup;
		wiringGroup = newWiringGroup;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__WIRING_GROUP, oldWiringGroup, newWiringGroup);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setWiringGroup(ChangeSetWiringGroup newWiringGroup) {
		if (newWiringGroup != wiringGroup) {
			NotificationChain msgs = null;
			if (wiringGroup != null)
				msgs = ((InternalEObject)wiringGroup).eInverseRemove(this, ChangesetPackage.CHANGE_SET_WIRING_GROUP__ROWS, ChangeSetWiringGroup.class, msgs);
			if (newWiringGroup != null)
				msgs = ((InternalEObject)newWiringGroup).eInverseAdd(this, ChangesetPackage.CHANGE_SET_WIRING_GROUP__ROWS, ChangeSetWiringGroup.class, msgs);
			msgs = basicSetWiringGroup(newWiringGroup, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__WIRING_GROUP, newWiringGroup, newWiringGroup));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ChangeSetTableGroup getTableGroup() {
		if (tableGroup != null && tableGroup.eIsProxy()) {
			InternalEObject oldTableGroup = (InternalEObject)tableGroup;
			tableGroup = (ChangeSetTableGroup)eResolveProxy(oldTableGroup);
			if (tableGroup != oldTableGroup) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_TABLE_ROW__TABLE_GROUP, oldTableGroup, tableGroup));
			}
		}
		return tableGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSetTableGroup basicGetTableGroup() {
		return tableGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetTableGroup(ChangeSetTableGroup newTableGroup, NotificationChain msgs) {
		ChangeSetTableGroup oldTableGroup = tableGroup;
		tableGroup = newTableGroup;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__TABLE_GROUP, oldTableGroup, newTableGroup);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTableGroup(ChangeSetTableGroup newTableGroup) {
		if (newTableGroup != tableGroup) {
			NotificationChain msgs = null;
			if (tableGroup != null)
				msgs = ((InternalEObject)tableGroup).eInverseRemove(this, ChangesetPackage.CHANGE_SET_TABLE_GROUP__ROWS, ChangeSetTableGroup.class, msgs);
			if (newTableGroup != null)
				msgs = ((InternalEObject)newTableGroup).eInverseAdd(this, ChangesetPackage.CHANGE_SET_TABLE_GROUP__ROWS, ChangeSetTableGroup.class, msgs);
			msgs = basicSetTableGroup(newTableGroup, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_TABLE_ROW__TABLE_GROUP, newTableGroup, newTableGroup));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public boolean isMajorChange() {
		return isWiringChange() || isVesselChange() || isDateChange();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public ChangeSetRowData getLHSAfterOrBefore() {
		ChangeSetRowData row = getLhsAfter();
		if (row != null) {
			return row;
		}
		return getLhsBefore();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public ChangeSetRowData getCurrentRHSAfterOrBefore() {
		ChangeSetRowData row = getCurrentRhsAfter();
		if (row != null) {
			return row;
		}
		return getCurrentRhsBefore();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getLhsName() {
		ChangeSetRowData row = getLHSAfterOrBefore();
		if (row != null) {
			return row.getLhsName();
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getCurrentRhsName() {
		ChangeSetRowData row = getCurrentRHSAfterOrBefore();
		if (row != null) {
			return row.getRhsName();
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public int getBeforeVesselCharterNumber() {
		ChangeSetRowData row = getLhsBefore();
		if (row != null) {
			return row.getVesselCharterNumber();
		}
		return 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getBeforeVesselName() {
		ChangeSetRowData row = getLhsBefore();
		if (row != null) {
			return row.getVesselName();
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getBeforeVesselShortName() {
		ChangeSetRowData row = getLhsBefore();
		if (row != null) {
			return row.getVesselShortName();
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public ChangeSetVesselType getBeforeVesselType() {
		ChangeSetRowData row = getLhsBefore();
		if (row != null) {
			return row.getVesselType();
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public int getAfterVesselCharterNumber() {
		ChangeSetRowData row = getLhsAfter();
		if (row != null) {
			return row.getVesselCharterNumber();
		}
		return 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getAfterVesselName() {
		ChangeSetRowData row = getLhsAfter();
		if (row != null) {
			return row.getVesselName();
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getAfterVesselShortName() {
		ChangeSetRowData row = getLhsAfter();
		if (row != null) {
			return row.getVesselShortName();
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public ChangeSetVesselType getAfterVesselType() {
		ChangeSetRowData row = getLhsAfter();
		if (row != null) {
			return row.getVesselType();
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public boolean isLhsSlot() {
		ChangeSetRowData row = getLHSAfterOrBefore();
		if (row != null) {
			return row.isLhsSlot();
		}
		return false;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public boolean isCurrentRhsSlot() {
		ChangeSetRowData row = getCurrentRHSAfterOrBefore();
		if (row != null) {
			return row.isRhsSlot();
		}
		return false;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public boolean isLhsSpot() {
		ChangeSetRowData row = getLHSAfterOrBefore();
		if (row != null) {
			return row.isLhsSpot();
		}
		return false;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public boolean isCurrentRhsOptional() {
		ChangeSetRowData row = getCurrentRHSAfterOrBefore();
		if (row != null) {
			return row.isRhsOptional();
		}
		return false;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public boolean isLhsOptional() {
		ChangeSetRowData row = getLHSAfterOrBefore();
		if (row != null) {
			return row.isLhsOptional();
		}
		return false;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public boolean isCurrentRhsSpot() {
		ChangeSetRowData row = getCurrentRHSAfterOrBefore();
		if (row != null) {
			return row.isRhsSpot();
		}
		return false;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public boolean isLhsNonShipped() {
		ChangeSetRowData row = getLHSAfterOrBefore();
		if (row != null) {
			return row.isLhsNonShipped();
		}
		return false;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public boolean isCurrentRhsNonShipped() {
		ChangeSetRowData row = getCurrentRHSAfterOrBefore();
		if (row != null) {
			return row.isRhsNonShipped();
		}
		return false;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__WIRING_GROUP:
				if (wiringGroup != null)
					msgs = ((InternalEObject)wiringGroup).eInverseRemove(this, ChangesetPackage.CHANGE_SET_WIRING_GROUP__ROWS, ChangeSetWiringGroup.class, msgs);
				return basicSetWiringGroup((ChangeSetWiringGroup)otherEnd, msgs);
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__TABLE_GROUP:
				if (tableGroup != null)
					msgs = ((InternalEObject)tableGroup).eInverseRemove(this, ChangesetPackage.CHANGE_SET_TABLE_GROUP__ROWS, ChangeSetTableGroup.class, msgs);
				return basicSetTableGroup((ChangeSetTableGroup)otherEnd, msgs);
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
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__WIRING_GROUP:
				return basicSetWiringGroup(null, msgs);
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__TABLE_GROUP:
				return basicSetTableGroup(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	@Override
	public boolean isWiringOrVesselChange() {
		return isWiringChange() || isVesselChange();
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_BEFORE:
				if (resolve) return getLhsBefore();
				return basicGetLhsBefore();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_AFTER:
				if (resolve) return getLhsAfter();
				return basicGetLhsAfter();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_VALID:
				return isLhsValid();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__CURRENT_RHS_BEFORE:
				if (resolve) return getCurrentRhsBefore();
				return basicGetCurrentRhsBefore();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__CURRENT_RHS_AFTER:
				if (resolve) return getCurrentRhsAfter();
				return basicGetCurrentRhsAfter();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__PREVIOUS_RHS_BEFORE:
				if (resolve) return getPreviousRhsBefore();
				return basicGetPreviousRhsBefore();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__PREVIOUS_RHS_AFTER:
				if (resolve) return getPreviousRhsAfter();
				return basicGetPreviousRhsAfter();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__CURRENT_RHS_VALID:
				return isCurrentRhsValid();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__WIRING_CHANGE:
				return isWiringChange();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__VESSEL_CHANGE:
				return isVesselChange();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__DATE_CHANGE:
				return isDateChange();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__WIRING_GROUP:
				if (resolve) return getWiringGroup();
				return basicGetWiringGroup();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__TABLE_GROUP:
				if (resolve) return getTableGroup();
				return basicGetTableGroup();
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
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_BEFORE:
				setLhsBefore((ChangeSetRowData)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_AFTER:
				setLhsAfter((ChangeSetRowData)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_VALID:
				setLhsValid((Boolean)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__CURRENT_RHS_BEFORE:
				setCurrentRhsBefore((ChangeSetRowData)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__CURRENT_RHS_AFTER:
				setCurrentRhsAfter((ChangeSetRowData)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__PREVIOUS_RHS_BEFORE:
				setPreviousRhsBefore((ChangeSetRowData)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__PREVIOUS_RHS_AFTER:
				setPreviousRhsAfter((ChangeSetRowData)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__CURRENT_RHS_VALID:
				setCurrentRhsValid((Boolean)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__WIRING_CHANGE:
				setWiringChange((Boolean)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__VESSEL_CHANGE:
				setVesselChange((Boolean)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__DATE_CHANGE:
				setDateChange((Boolean)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__WIRING_GROUP:
				setWiringGroup((ChangeSetWiringGroup)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__TABLE_GROUP:
				setTableGroup((ChangeSetTableGroup)newValue);
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
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_BEFORE:
				setLhsBefore((ChangeSetRowData)null);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_AFTER:
				setLhsAfter((ChangeSetRowData)null);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_VALID:
				setLhsValid(LHS_VALID_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__CURRENT_RHS_BEFORE:
				setCurrentRhsBefore((ChangeSetRowData)null);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__CURRENT_RHS_AFTER:
				setCurrentRhsAfter((ChangeSetRowData)null);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__PREVIOUS_RHS_BEFORE:
				setPreviousRhsBefore((ChangeSetRowData)null);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__PREVIOUS_RHS_AFTER:
				setPreviousRhsAfter((ChangeSetRowData)null);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__CURRENT_RHS_VALID:
				setCurrentRhsValid(CURRENT_RHS_VALID_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__WIRING_CHANGE:
				setWiringChange(WIRING_CHANGE_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__VESSEL_CHANGE:
				setVesselChange(VESSEL_CHANGE_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__DATE_CHANGE:
				setDateChange(DATE_CHANGE_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__WIRING_GROUP:
				setWiringGroup((ChangeSetWiringGroup)null);
				return;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__TABLE_GROUP:
				setTableGroup((ChangeSetTableGroup)null);
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
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_BEFORE:
				return lhsBefore != null;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_AFTER:
				return lhsAfter != null;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__LHS_VALID:
				return lhsValid != LHS_VALID_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__CURRENT_RHS_BEFORE:
				return currentRhsBefore != null;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__CURRENT_RHS_AFTER:
				return currentRhsAfter != null;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__PREVIOUS_RHS_BEFORE:
				return previousRhsBefore != null;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__PREVIOUS_RHS_AFTER:
				return previousRhsAfter != null;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__CURRENT_RHS_VALID:
				return currentRhsValid != CURRENT_RHS_VALID_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__WIRING_CHANGE:
				return wiringChange != WIRING_CHANGE_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__VESSEL_CHANGE:
				return vesselChange != VESSEL_CHANGE_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__DATE_CHANGE:
				return dateChange != DATE_CHANGE_EDEFAULT;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__WIRING_GROUP:
				return wiringGroup != null;
			case ChangesetPackage.CHANGE_SET_TABLE_ROW__TABLE_GROUP:
				return tableGroup != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case ChangesetPackage.CHANGE_SET_TABLE_ROW___IS_MAJOR_CHANGE:
				return isMajorChange();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW___GET_LHS_AFTER_OR_BEFORE:
				return getLHSAfterOrBefore();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW___GET_CURRENT_RHS_AFTER_OR_BEFORE:
				return getCurrentRHSAfterOrBefore();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW___GET_LHS_NAME:
				return getLhsName();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW___GET_CURRENT_RHS_NAME:
				return getCurrentRhsName();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW___GET_BEFORE_VESSEL_CHARTER_NUMBER:
				return getBeforeVesselCharterNumber();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW___GET_BEFORE_VESSEL_NAME:
				return getBeforeVesselName();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW___GET_BEFORE_VESSEL_SHORT_NAME:
				return getBeforeVesselShortName();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW___GET_BEFORE_VESSEL_TYPE:
				return getBeforeVesselType();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW___GET_AFTER_VESSEL_CHARTER_NUMBER:
				return getAfterVesselCharterNumber();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW___GET_AFTER_VESSEL_NAME:
				return getAfterVesselName();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW___GET_AFTER_VESSEL_SHORT_NAME:
				return getAfterVesselShortName();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW___GET_AFTER_VESSEL_TYPE:
				return getAfterVesselType();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW___IS_LHS_SLOT:
				return isLhsSlot();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW___IS_CURRENT_RHS_SLOT:
				return isCurrentRhsSlot();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW___IS_LHS_SPOT:
				return isLhsSpot();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW___IS_CURRENT_RHS_OPTIONAL:
				return isCurrentRhsOptional();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW___IS_LHS_OPTIONAL:
				return isLhsOptional();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW___IS_CURRENT_RHS_SPOT:
				return isCurrentRhsSpot();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW___IS_LHS_NON_SHIPPED:
				return isLhsNonShipped();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW___IS_CURRENT_RHS_NON_SHIPPED:
				return isCurrentRhsNonShipped();
		}
		return super.eInvoke(operationID, arguments);
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
		result.append(" (lhsValid: ");
		result.append(lhsValid);
		result.append(", currentRhsValid: ");
		result.append(currentRhsValid);
		result.append(", wiringChange: ");
		result.append(wiringChange);
		result.append(", vesselChange: ");
		result.append(vesselChange);
		result.append(", dateChange: ");
		result.append(dateChange);
		result.append(')');
		return result.toString();
	}

} //ChangeSetTableRowImpl
