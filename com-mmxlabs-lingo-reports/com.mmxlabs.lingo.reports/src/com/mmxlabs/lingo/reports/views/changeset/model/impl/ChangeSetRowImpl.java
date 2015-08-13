/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model.impl;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage;
import com.mmxlabs.lingo.reports.views.changeset.model.WiringChange;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

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
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getLhsVesselName <em>Lhs Vessel Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getRhsVesselName <em>Rhs Vessel Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getLhsWiringLink <em>Lhs Wiring Link</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getRhsWiringLink <em>Rhs Wiring Link</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getLhsWiringChange <em>Lhs Wiring Change</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getRhsWiringChange <em>Rhs Wiring Change</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getLhsVesselChange <em>Lhs Vessel Change</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getLoadSlot <em>Load Slot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getDischargeSlot <em>Discharge Slot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getOriginalLoadAllocation <em>Original Load Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getNewLoadAllocation <em>New Load Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getOriginalDischargeAllocation <em>Original Discharge Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeSetRowImpl#getNewDischargeAllocation <em>New Discharge Allocation</em>}</li>
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
	 * The default value of the '{@link #getLhsVesselName() <em>Lhs Vessel Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLhsVesselName()
	 * @generated
	 * @ordered
	 */
	protected static final String LHS_VESSEL_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLhsVesselName() <em>Lhs Vessel Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLhsVesselName()
	 * @generated
	 * @ordered
	 */
	protected String lhsVesselName = LHS_VESSEL_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getRhsVesselName() <em>Rhs Vessel Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRhsVesselName()
	 * @generated
	 * @ordered
	 */
	protected static final String RHS_VESSEL_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRhsVesselName() <em>Rhs Vessel Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRhsVesselName()
	 * @generated
	 * @ordered
	 */
	protected String rhsVesselName = RHS_VESSEL_NAME_EDEFAULT;

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
	 * The cached value of the '{@link #getLhsWiringChange() <em>Lhs Wiring Change</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLhsWiringChange()
	 * @generated
	 * @ordered
	 */
	protected WiringChange lhsWiringChange;

	/**
	 * The cached value of the '{@link #getRhsWiringChange() <em>Rhs Wiring Change</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRhsWiringChange()
	 * @generated
	 * @ordered
	 */
	protected WiringChange rhsWiringChange;

	/**
	 * The cached value of the '{@link #getLhsVesselChange() <em>Lhs Vessel Change</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLhsVesselChange()
	 * @generated
	 * @ordered
	 */
	protected WiringChange lhsVesselChange;

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
	public String getLhsVesselName() {
		return lhsVesselName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLhsVesselName(String newLhsVesselName) {
		String oldLhsVesselName = lhsVesselName;
		lhsVesselName = newLhsVesselName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__LHS_VESSEL_NAME, oldLhsVesselName, lhsVesselName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getRhsVesselName() {
		return rhsVesselName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRhsVesselName(String newRhsVesselName) {
		String oldRhsVesselName = rhsVesselName;
		rhsVesselName = newRhsVesselName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__RHS_VESSEL_NAME, oldRhsVesselName, rhsVesselName));
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
	public WiringChange getLhsWiringChange() {
		if (lhsWiringChange != null && lhsWiringChange.eIsProxy()) {
			InternalEObject oldLhsWiringChange = (InternalEObject)lhsWiringChange;
			lhsWiringChange = (WiringChange)eResolveProxy(oldLhsWiringChange);
			if (lhsWiringChange != oldLhsWiringChange) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_ROW__LHS_WIRING_CHANGE, oldLhsWiringChange, lhsWiringChange));
			}
		}
		return lhsWiringChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WiringChange basicGetLhsWiringChange() {
		return lhsWiringChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLhsWiringChange(WiringChange newLhsWiringChange) {
		WiringChange oldLhsWiringChange = lhsWiringChange;
		lhsWiringChange = newLhsWiringChange;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__LHS_WIRING_CHANGE, oldLhsWiringChange, lhsWiringChange));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WiringChange getRhsWiringChange() {
		if (rhsWiringChange != null && rhsWiringChange.eIsProxy()) {
			InternalEObject oldRhsWiringChange = (InternalEObject)rhsWiringChange;
			rhsWiringChange = (WiringChange)eResolveProxy(oldRhsWiringChange);
			if (rhsWiringChange != oldRhsWiringChange) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_ROW__RHS_WIRING_CHANGE, oldRhsWiringChange, rhsWiringChange));
			}
		}
		return rhsWiringChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WiringChange basicGetRhsWiringChange() {
		return rhsWiringChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRhsWiringChange(WiringChange newRhsWiringChange) {
		WiringChange oldRhsWiringChange = rhsWiringChange;
		rhsWiringChange = newRhsWiringChange;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__RHS_WIRING_CHANGE, oldRhsWiringChange, rhsWiringChange));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WiringChange getLhsVesselChange() {
		if (lhsVesselChange != null && lhsVesselChange.eIsProxy()) {
			InternalEObject oldLhsVesselChange = (InternalEObject)lhsVesselChange;
			lhsVesselChange = (WiringChange)eResolveProxy(oldLhsVesselChange);
			if (lhsVesselChange != oldLhsVesselChange) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE_SET_ROW__LHS_VESSEL_CHANGE, oldLhsVesselChange, lhsVesselChange));
			}
		}
		return lhsVesselChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WiringChange basicGetLhsVesselChange() {
		return lhsVesselChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLhsVesselChange(WiringChange newLhsVesselChange) {
		WiringChange oldLhsVesselChange = lhsVesselChange;
		lhsVesselChange = newLhsVesselChange;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__LHS_VESSEL_CHANGE, oldLhsVesselChange, lhsVesselChange));
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
	public com.mmxlabs.models.lng.schedule.SlotAllocation getOriginalLoadAllocation() {
		if (originalLoadAllocation != null && originalLoadAllocation.eIsProxy()) {
			InternalEObject oldOriginalLoadAllocation = (InternalEObject)originalLoadAllocation;
			originalLoadAllocation = (com.mmxlabs.models.lng.schedule.SlotAllocation)eResolveProxy(oldOriginalLoadAllocation);
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
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_LOAD_ALLOCATION, oldOriginalLoadAllocation, originalLoadAllocation));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__NEW_LOAD_ALLOCATION, oldNewLoadAllocation, newLoadAllocation));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_DISCHARGE_ALLOCATION, oldOriginalDischargeAllocation, originalDischargeAllocation));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE_SET_ROW__NEW_DISCHARGE_ALLOCATION, oldNewDischargeAllocation, newDischargeAllocation));
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
			case ChangesetPackage.CHANGE_SET_ROW__LHS_VESSEL_NAME:
				return getLhsVesselName();
			case ChangesetPackage.CHANGE_SET_ROW__RHS_VESSEL_NAME:
				return getRhsVesselName();
			case ChangesetPackage.CHANGE_SET_ROW__LHS_WIRING_LINK:
				if (resolve) return getLhsWiringLink();
				return basicGetLhsWiringLink();
			case ChangesetPackage.CHANGE_SET_ROW__RHS_WIRING_LINK:
				if (resolve) return getRhsWiringLink();
				return basicGetRhsWiringLink();
			case ChangesetPackage.CHANGE_SET_ROW__LHS_WIRING_CHANGE:
				if (resolve) return getLhsWiringChange();
				return basicGetLhsWiringChange();
			case ChangesetPackage.CHANGE_SET_ROW__RHS_WIRING_CHANGE:
				if (resolve) return getRhsWiringChange();
				return basicGetRhsWiringChange();
			case ChangesetPackage.CHANGE_SET_ROW__LHS_VESSEL_CHANGE:
				if (resolve) return getLhsVesselChange();
				return basicGetLhsVesselChange();
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
			case ChangesetPackage.CHANGE_SET_ROW__LHS_VESSEL_NAME:
				setLhsVesselName((String)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__RHS_VESSEL_NAME:
				setRhsVesselName((String)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__LHS_WIRING_LINK:
				setLhsWiringLink((ChangeSetRow)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__RHS_WIRING_LINK:
				setRhsWiringLink((ChangeSetRow)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__LHS_WIRING_CHANGE:
				setLhsWiringChange((WiringChange)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__RHS_WIRING_CHANGE:
				setRhsWiringChange((WiringChange)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__LHS_VESSEL_CHANGE:
				setLhsVesselChange((WiringChange)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__LOAD_SLOT:
				setLoadSlot((LoadSlot)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__DISCHARGE_SLOT:
				setDischargeSlot((DischargeSlot)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_LOAD_ALLOCATION:
				setOriginalLoadAllocation((com.mmxlabs.models.lng.schedule.SlotAllocation)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__NEW_LOAD_ALLOCATION:
				setNewLoadAllocation((com.mmxlabs.models.lng.schedule.SlotAllocation)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_DISCHARGE_ALLOCATION:
				setOriginalDischargeAllocation((com.mmxlabs.models.lng.schedule.SlotAllocation)newValue);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__NEW_DISCHARGE_ALLOCATION:
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
			case ChangesetPackage.CHANGE_SET_ROW__LHS_NAME:
				setLhsName(LHS_NAME_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__RHS_NAME:
				setRhsName(RHS_NAME_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__LHS_VESSEL_NAME:
				setLhsVesselName(LHS_VESSEL_NAME_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__RHS_VESSEL_NAME:
				setRhsVesselName(RHS_VESSEL_NAME_EDEFAULT);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__LHS_WIRING_LINK:
				setLhsWiringLink((ChangeSetRow)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__RHS_WIRING_LINK:
				setRhsWiringLink((ChangeSetRow)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__LHS_WIRING_CHANGE:
				setLhsWiringChange((WiringChange)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__RHS_WIRING_CHANGE:
				setRhsWiringChange((WiringChange)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__LHS_VESSEL_CHANGE:
				setLhsVesselChange((WiringChange)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__LOAD_SLOT:
				setLoadSlot((LoadSlot)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__DISCHARGE_SLOT:
				setDischargeSlot((DischargeSlot)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_LOAD_ALLOCATION:
				setOriginalLoadAllocation((com.mmxlabs.models.lng.schedule.SlotAllocation)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__NEW_LOAD_ALLOCATION:
				setNewLoadAllocation((com.mmxlabs.models.lng.schedule.SlotAllocation)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__ORIGINAL_DISCHARGE_ALLOCATION:
				setOriginalDischargeAllocation((com.mmxlabs.models.lng.schedule.SlotAllocation)null);
				return;
			case ChangesetPackage.CHANGE_SET_ROW__NEW_DISCHARGE_ALLOCATION:
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
			case ChangesetPackage.CHANGE_SET_ROW__LHS_NAME:
				return LHS_NAME_EDEFAULT == null ? lhsName != null : !LHS_NAME_EDEFAULT.equals(lhsName);
			case ChangesetPackage.CHANGE_SET_ROW__RHS_NAME:
				return RHS_NAME_EDEFAULT == null ? rhsName != null : !RHS_NAME_EDEFAULT.equals(rhsName);
			case ChangesetPackage.CHANGE_SET_ROW__LHS_VESSEL_NAME:
				return LHS_VESSEL_NAME_EDEFAULT == null ? lhsVesselName != null : !LHS_VESSEL_NAME_EDEFAULT.equals(lhsVesselName);
			case ChangesetPackage.CHANGE_SET_ROW__RHS_VESSEL_NAME:
				return RHS_VESSEL_NAME_EDEFAULT == null ? rhsVesselName != null : !RHS_VESSEL_NAME_EDEFAULT.equals(rhsVesselName);
			case ChangesetPackage.CHANGE_SET_ROW__LHS_WIRING_LINK:
				return lhsWiringLink != null;
			case ChangesetPackage.CHANGE_SET_ROW__RHS_WIRING_LINK:
				return rhsWiringLink != null;
			case ChangesetPackage.CHANGE_SET_ROW__LHS_WIRING_CHANGE:
				return lhsWiringChange != null;
			case ChangesetPackage.CHANGE_SET_ROW__RHS_WIRING_CHANGE:
				return rhsWiringChange != null;
			case ChangesetPackage.CHANGE_SET_ROW__LHS_VESSEL_CHANGE:
				return lhsVesselChange != null;
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
		result.append(", lhsVesselName: ");
		result.append(lhsVesselName);
		result.append(", rhsVesselName: ");
		result.append(rhsVesselName);
		result.append(')');
		return result.toString();
	}

} //ChangeSetRowImpl
