

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.fleet.impl;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.VesselAvailablility;

import com.mmxlabs.models.lng.types.APortSet;

import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

import java.util.Collection;
import java.util.Date;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vessel Availablility</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselAvailablilityImpl#getStartAt <em>Start At</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselAvailablilityImpl#getStartAfter <em>Start After</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselAvailablilityImpl#getStartBefore <em>Start Before</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselAvailablilityImpl#getEndAt <em>End At</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselAvailablilityImpl#getEndAfter <em>End After</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.VesselAvailablilityImpl#getEndBefore <em>End Before</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VesselAvailablilityImpl extends MMXObjectImpl implements VesselAvailablility {
	/**
	 * The cached value of the '{@link #getStartAt() <em>Start At</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartAt()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet> startAt;

	/**
	 * The default value of the '{@link #getStartAfter() <em>Start After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartAfter()
	 * @generated
	 * @ordered
	 */
	protected static final Date START_AFTER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStartAfter() <em>Start After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartAfter()
	 * @generated
	 * @ordered
	 */
	protected Date startAfter = START_AFTER_EDEFAULT;

	/**
	 * This is true if the Start After attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean startAfterESet;

	/**
	 * The default value of the '{@link #getStartBefore() <em>Start Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartBefore()
	 * @generated
	 * @ordered
	 */
	protected static final Date START_BEFORE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStartBefore() <em>Start Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartBefore()
	 * @generated
	 * @ordered
	 */
	protected Date startBefore = START_BEFORE_EDEFAULT;

	/**
	 * This is true if the Start Before attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean startBeforeESet;

	/**
	 * The cached value of the '{@link #getEndAt() <em>End At</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndAt()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet> endAt;

	/**
	 * The default value of the '{@link #getEndAfter() <em>End After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndAfter()
	 * @generated
	 * @ordered
	 */
	protected static final Date END_AFTER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEndAfter() <em>End After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndAfter()
	 * @generated
	 * @ordered
	 */
	protected Date endAfter = END_AFTER_EDEFAULT;

	/**
	 * This is true if the End After attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean endAfterESet;

	/**
	 * The default value of the '{@link #getEndBefore() <em>End Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndBefore()
	 * @generated
	 * @ordered
	 */
	protected static final Date END_BEFORE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEndBefore() <em>End Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndBefore()
	 * @generated
	 * @ordered
	 */
	protected Date endBefore = END_BEFORE_EDEFAULT;

	/**
	 * This is true if the End Before attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean endBeforeESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselAvailablilityImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.VESSEL_AVAILABLILITY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<APortSet> getStartAt() {
		if (startAt == null) {
			startAt = new EObjectResolvingEList<APortSet>(APortSet.class, this, FleetPackage.VESSEL_AVAILABLILITY__START_AT);
		}
		return startAt;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getStartAfter() {
		return startAfter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStartAfter(Date newStartAfter) {
		Date oldStartAfter = startAfter;
		startAfter = newStartAfter;
		boolean oldStartAfterESet = startAfterESet;
		startAfterESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_AVAILABLILITY__START_AFTER, oldStartAfter, startAfter, !oldStartAfterESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetStartAfter() {
		Date oldStartAfter = startAfter;
		boolean oldStartAfterESet = startAfterESet;
		startAfter = START_AFTER_EDEFAULT;
		startAfterESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL_AVAILABLILITY__START_AFTER, oldStartAfter, START_AFTER_EDEFAULT, oldStartAfterESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetStartAfter() {
		return startAfterESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getStartBefore() {
		return startBefore;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStartBefore(Date newStartBefore) {
		Date oldStartBefore = startBefore;
		startBefore = newStartBefore;
		boolean oldStartBeforeESet = startBeforeESet;
		startBeforeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_AVAILABLILITY__START_BEFORE, oldStartBefore, startBefore, !oldStartBeforeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetStartBefore() {
		Date oldStartBefore = startBefore;
		boolean oldStartBeforeESet = startBeforeESet;
		startBefore = START_BEFORE_EDEFAULT;
		startBeforeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL_AVAILABLILITY__START_BEFORE, oldStartBefore, START_BEFORE_EDEFAULT, oldStartBeforeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetStartBefore() {
		return startBeforeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<APortSet> getEndAt() {
		if (endAt == null) {
			endAt = new EObjectResolvingEList<APortSet>(APortSet.class, this, FleetPackage.VESSEL_AVAILABLILITY__END_AT);
		}
		return endAt;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getEndAfter() {
		return endAfter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEndAfter(Date newEndAfter) {
		Date oldEndAfter = endAfter;
		endAfter = newEndAfter;
		boolean oldEndAfterESet = endAfterESet;
		endAfterESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_AVAILABLILITY__END_AFTER, oldEndAfter, endAfter, !oldEndAfterESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetEndAfter() {
		Date oldEndAfter = endAfter;
		boolean oldEndAfterESet = endAfterESet;
		endAfter = END_AFTER_EDEFAULT;
		endAfterESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL_AVAILABLILITY__END_AFTER, oldEndAfter, END_AFTER_EDEFAULT, oldEndAfterESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetEndAfter() {
		return endAfterESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getEndBefore() {
		return endBefore;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEndBefore(Date newEndBefore) {
		Date oldEndBefore = endBefore;
		endBefore = newEndBefore;
		boolean oldEndBeforeESet = endBeforeESet;
		endBeforeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_AVAILABLILITY__END_BEFORE, oldEndBefore, endBefore, !oldEndBeforeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetEndBefore() {
		Date oldEndBefore = endBefore;
		boolean oldEndBeforeESet = endBeforeESet;
		endBefore = END_BEFORE_EDEFAULT;
		endBeforeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL_AVAILABLILITY__END_BEFORE, oldEndBefore, END_BEFORE_EDEFAULT, oldEndBeforeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetEndBefore() {
		return endBeforeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case FleetPackage.VESSEL_AVAILABLILITY__START_AT:
				return getStartAt();
			case FleetPackage.VESSEL_AVAILABLILITY__START_AFTER:
				return getStartAfter();
			case FleetPackage.VESSEL_AVAILABLILITY__START_BEFORE:
				return getStartBefore();
			case FleetPackage.VESSEL_AVAILABLILITY__END_AT:
				return getEndAt();
			case FleetPackage.VESSEL_AVAILABLILITY__END_AFTER:
				return getEndAfter();
			case FleetPackage.VESSEL_AVAILABLILITY__END_BEFORE:
				return getEndBefore();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case FleetPackage.VESSEL_AVAILABLILITY__START_AT:
				getStartAt().clear();
				getStartAt().addAll((Collection<? extends APortSet>)newValue);
				return;
			case FleetPackage.VESSEL_AVAILABLILITY__START_AFTER:
				setStartAfter((Date)newValue);
				return;
			case FleetPackage.VESSEL_AVAILABLILITY__START_BEFORE:
				setStartBefore((Date)newValue);
				return;
			case FleetPackage.VESSEL_AVAILABLILITY__END_AT:
				getEndAt().clear();
				getEndAt().addAll((Collection<? extends APortSet>)newValue);
				return;
			case FleetPackage.VESSEL_AVAILABLILITY__END_AFTER:
				setEndAfter((Date)newValue);
				return;
			case FleetPackage.VESSEL_AVAILABLILITY__END_BEFORE:
				setEndBefore((Date)newValue);
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
			case FleetPackage.VESSEL_AVAILABLILITY__START_AT:
				getStartAt().clear();
				return;
			case FleetPackage.VESSEL_AVAILABLILITY__START_AFTER:
				unsetStartAfter();
				return;
			case FleetPackage.VESSEL_AVAILABLILITY__START_BEFORE:
				unsetStartBefore();
				return;
			case FleetPackage.VESSEL_AVAILABLILITY__END_AT:
				getEndAt().clear();
				return;
			case FleetPackage.VESSEL_AVAILABLILITY__END_AFTER:
				unsetEndAfter();
				return;
			case FleetPackage.VESSEL_AVAILABLILITY__END_BEFORE:
				unsetEndBefore();
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
			case FleetPackage.VESSEL_AVAILABLILITY__START_AT:
				return startAt != null && !startAt.isEmpty();
			case FleetPackage.VESSEL_AVAILABLILITY__START_AFTER:
				return isSetStartAfter();
			case FleetPackage.VESSEL_AVAILABLILITY__START_BEFORE:
				return isSetStartBefore();
			case FleetPackage.VESSEL_AVAILABLILITY__END_AT:
				return endAt != null && !endAt.isEmpty();
			case FleetPackage.VESSEL_AVAILABLILITY__END_AFTER:
				return isSetEndAfter();
			case FleetPackage.VESSEL_AVAILABLILITY__END_BEFORE:
				return isSetEndBefore();
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
		result.append(" (startAfter: ");
		if (startAfterESet) result.append(startAfter); else result.append("<unset>");
		result.append(", startBefore: ");
		if (startBeforeESet) result.append(startBefore); else result.append("<unset>");
		result.append(", endAfter: ");
		if (endAfterESet) result.append(endAfter); else result.append("<unset>");
		result.append(", endBefore: ");
		if (endBeforeESet) result.append(endBefore); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} // end of VesselAvailablilityImpl

// finish type fixing
