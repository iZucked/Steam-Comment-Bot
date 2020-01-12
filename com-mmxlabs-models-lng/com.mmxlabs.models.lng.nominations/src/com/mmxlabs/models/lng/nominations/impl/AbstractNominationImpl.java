/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.nominations.impl;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.mmxcore.MMXObject.DelegateInformation;

import java.time.LocalDate;
import java.util.Collections;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Nomination</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.nominations.impl.AbstractNominationImpl#getNomineeId <em>Nominee Id</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.impl.AbstractNominationImpl#getDueDate <em>Due Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.impl.AbstractNominationImpl#isDone <em>Done</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.impl.AbstractNominationImpl#getAlertDate <em>Alert Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.impl.AbstractNominationImpl#getSpecUuid <em>Spec Uuid</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.impl.AbstractNominationImpl#isDeleted <em>Deleted</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class AbstractNominationImpl extends AbstractNominationSpecImpl implements AbstractNomination {
	/**
	 * The default value of the '{@link #getNomineeId() <em>Nominee Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNomineeId()
	 * @generated
	 * @ordered
	 */
	protected static final String NOMINEE_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNomineeId() <em>Nominee Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNomineeId()
	 * @generated
	 * @ordered
	 */
	protected String nomineeId = NOMINEE_ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getDueDate() <em>Due Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDueDate()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate DUE_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDueDate() <em>Due Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDueDate()
	 * @generated
	 * @ordered
	 */
	protected LocalDate dueDate = DUE_DATE_EDEFAULT;

	/**
	 * This is true if the Due Date attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean dueDateESet;

	/**
	 * The default value of the '{@link #isDone() <em>Done</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDone()
	 * @generated
	 * @ordered
	 */
	protected static final boolean DONE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isDone() <em>Done</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDone()
	 * @generated
	 * @ordered
	 */
	protected boolean done = DONE_EDEFAULT;

	/**
	 * The default value of the '{@link #getAlertDate() <em>Alert Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAlertDate()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate ALERT_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAlertDate() <em>Alert Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAlertDate()
	 * @generated
	 * @ordered
	 */
	protected LocalDate alertDate = ALERT_DATE_EDEFAULT;

	/**
	 * This is true if the Alert Date attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean alertDateESet;

	/**
	 * The default value of the '{@link #getSpecUuid() <em>Spec Uuid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpecUuid()
	 * @generated
	 * @ordered
	 */
	protected static final String SPEC_UUID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSpecUuid() <em>Spec Uuid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpecUuid()
	 * @generated
	 * @ordered
	 */
	protected String specUuid = SPEC_UUID_EDEFAULT;

	/**
	 * This is true if the Spec Uuid attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean specUuidESet;

	/**
	 * The default value of the '{@link #isDeleted() <em>Deleted</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDeleted()
	 * @generated
	 * @ordered
	 */
	protected static final boolean DELETED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isDeleted() <em>Deleted</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDeleted()
	 * @generated
	 * @ordered
	 */
	protected boolean deleted = DELETED_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AbstractNominationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return NominationsPackage.Literals.ABSTRACT_NOMINATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getNomineeId() {
		return nomineeId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setNomineeId(String newNomineeId) {
		String oldNomineeId = nomineeId;
		nomineeId = newNomineeId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NominationsPackage.ABSTRACT_NOMINATION__NOMINEE_ID, oldNomineeId, nomineeId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDate getDueDate() {
		return dueDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDueDate(LocalDate newDueDate) {
		LocalDate oldDueDate = dueDate;
		dueDate = newDueDate;
		boolean oldDueDateESet = dueDateESet;
		dueDateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NominationsPackage.ABSTRACT_NOMINATION__DUE_DATE, oldDueDate, dueDate, !oldDueDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetDueDate() {
		LocalDate oldDueDate = dueDate;
		boolean oldDueDateESet = dueDateESet;
		dueDate = DUE_DATE_EDEFAULT;
		dueDateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, NominationsPackage.ABSTRACT_NOMINATION__DUE_DATE, oldDueDate, DUE_DATE_EDEFAULT, oldDueDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetDueDate() {
		return dueDateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isDone() {
		return done;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDone(boolean newDone) {
		boolean oldDone = done;
		done = newDone;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NominationsPackage.ABSTRACT_NOMINATION__DONE, oldDone, done));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDate getAlertDate() {
		return alertDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAlertDate(LocalDate newAlertDate) {
		LocalDate oldAlertDate = alertDate;
		alertDate = newAlertDate;
		boolean oldAlertDateESet = alertDateESet;
		alertDateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NominationsPackage.ABSTRACT_NOMINATION__ALERT_DATE, oldAlertDate, alertDate, !oldAlertDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetAlertDate() {
		LocalDate oldAlertDate = alertDate;
		boolean oldAlertDateESet = alertDateESet;
		alertDate = ALERT_DATE_EDEFAULT;
		alertDateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, NominationsPackage.ABSTRACT_NOMINATION__ALERT_DATE, oldAlertDate, ALERT_DATE_EDEFAULT, oldAlertDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetAlertDate() {
		return alertDateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getSpecUuid() {
		return specUuid;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSpecUuid(String newSpecUuid) {
		String oldSpecUuid = specUuid;
		specUuid = newSpecUuid;
		boolean oldSpecUuidESet = specUuidESet;
		specUuidESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NominationsPackage.ABSTRACT_NOMINATION__SPEC_UUID, oldSpecUuid, specUuid, !oldSpecUuidESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetSpecUuid() {
		String oldSpecUuid = specUuid;
		boolean oldSpecUuidESet = specUuidESet;
		specUuid = SPEC_UUID_EDEFAULT;
		specUuidESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, NominationsPackage.ABSTRACT_NOMINATION__SPEC_UUID, oldSpecUuid, SPEC_UUID_EDEFAULT, oldSpecUuidESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetSpecUuid() {
		return specUuidESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDeleted(boolean newDeleted) {
		boolean oldDeleted = deleted;
		deleted = newDeleted;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NominationsPackage.ABSTRACT_NOMINATION__DELETED, oldDeleted, deleted));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case NominationsPackage.ABSTRACT_NOMINATION__NOMINEE_ID:
				return getNomineeId();
			case NominationsPackage.ABSTRACT_NOMINATION__DUE_DATE:
				return getDueDate();
			case NominationsPackage.ABSTRACT_NOMINATION__DONE:
				return isDone();
			case NominationsPackage.ABSTRACT_NOMINATION__ALERT_DATE:
				return getAlertDate();
			case NominationsPackage.ABSTRACT_NOMINATION__SPEC_UUID:
				return getSpecUuid();
			case NominationsPackage.ABSTRACT_NOMINATION__DELETED:
				return isDeleted();
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
			case NominationsPackage.ABSTRACT_NOMINATION__NOMINEE_ID:
				setNomineeId((String)newValue);
				return;
			case NominationsPackage.ABSTRACT_NOMINATION__DUE_DATE:
				setDueDate((LocalDate)newValue);
				return;
			case NominationsPackage.ABSTRACT_NOMINATION__DONE:
				setDone((Boolean)newValue);
				return;
			case NominationsPackage.ABSTRACT_NOMINATION__ALERT_DATE:
				setAlertDate((LocalDate)newValue);
				return;
			case NominationsPackage.ABSTRACT_NOMINATION__SPEC_UUID:
				setSpecUuid((String)newValue);
				return;
			case NominationsPackage.ABSTRACT_NOMINATION__DELETED:
				setDeleted((Boolean)newValue);
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
			case NominationsPackage.ABSTRACT_NOMINATION__NOMINEE_ID:
				setNomineeId(NOMINEE_ID_EDEFAULT);
				return;
			case NominationsPackage.ABSTRACT_NOMINATION__DUE_DATE:
				unsetDueDate();
				return;
			case NominationsPackage.ABSTRACT_NOMINATION__DONE:
				setDone(DONE_EDEFAULT);
				return;
			case NominationsPackage.ABSTRACT_NOMINATION__ALERT_DATE:
				unsetAlertDate();
				return;
			case NominationsPackage.ABSTRACT_NOMINATION__SPEC_UUID:
				unsetSpecUuid();
				return;
			case NominationsPackage.ABSTRACT_NOMINATION__DELETED:
				setDeleted(DELETED_EDEFAULT);
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
			case NominationsPackage.ABSTRACT_NOMINATION__NOMINEE_ID:
				return NOMINEE_ID_EDEFAULT == null ? nomineeId != null : !NOMINEE_ID_EDEFAULT.equals(nomineeId);
			case NominationsPackage.ABSTRACT_NOMINATION__DUE_DATE:
				return isSetDueDate();
			case NominationsPackage.ABSTRACT_NOMINATION__DONE:
				return done != DONE_EDEFAULT;
			case NominationsPackage.ABSTRACT_NOMINATION__ALERT_DATE:
				return isSetAlertDate();
			case NominationsPackage.ABSTRACT_NOMINATION__SPEC_UUID:
				return isSetSpecUuid();
			case NominationsPackage.ABSTRACT_NOMINATION__DELETED:
				return deleted != DELETED_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * @generated NOT
	 */
/*	@Override
	public DelegateInformation getUnsetValueOrDelegate(EStructuralFeature feature) {
		
		NominationsPackage nominations = NominationsPackage.eINSTANCE;
		
		if (feature == NominationsPackage.Literals.ABSTRACT_NOMINATION__DUE_DATE) {
			return new DelegateInformation(null, null, null) {
				public boolean delegatesTo(final Object changedFeature) {
					return (changedFeature == NominationsPackage.Literals.ABSTRACT_NOMINATION_SPEC__SIZE);
				}
				
				public Object getValue(final EObject object) {
					Object result = LocalDate.now();
					//TODO change to get calculated alert.
					//TODO: problem is how to get at the slots from CargoPackage without introducing a cyclic dependency?
//					if (contract != null) {
//						if (contract.eIsSet(NominationsPackage.Literals.ABSTRACT_NOMINATION_SPEC__SIZE)) {
//							final int wnSize = ((Contract) contract).getPortNominationSize();
//							final TimePeriod wnUnits = ((Contract) contract).getPortNominationSizeUnits();
//							if (wnUnits == null) return result;
//							result = NominationUtils.computeNewDate(getWindowStart(), wnUnits, -wnSize);
//						}
//					}
					Slot slot = null;
					
					//NominationsModelUtil.
					return result;
					
				}	
			};
		}

		return super.getUnsetValueOrDelegate(feature);
		
	}
	*/

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (nomineeId: ");
		result.append(nomineeId);
		result.append(", dueDate: ");
		if (dueDateESet) result.append(dueDate); else result.append("<unset>");
		result.append(", done: ");
		result.append(done);
		result.append(", alertDate: ");
		if (alertDateESet) result.append(alertDate); else result.append("<unset>");
		result.append(", specUuid: ");
		if (specUuidESet) result.append(specUuid); else result.append("<unset>");
		result.append(", deleted: ");
		result.append(deleted);
		result.append(')');
		return result.toString();
	}

} //AbstractNominationImpl
