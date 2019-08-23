/**
 */
package com.mmxlabs.models.lng.nominations.impl;

import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.NominationAuditItem;
import com.mmxlabs.models.lng.nominations.NominationsPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Nomination Audit Item</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.nominations.impl.NominationAuditItemImpl#getNomination <em>Nomination</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NominationAuditItemImpl extends AbstractAuditItemImpl implements NominationAuditItem {
	/**
	 * The cached value of the '{@link #getNomination() <em>Nomination</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNomination()
	 * @generated
	 * @ordered
	 */
	protected AbstractNomination nomination;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NominationAuditItemImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return NominationsPackage.Literals.NOMINATION_AUDIT_ITEM;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AbstractNomination getNomination() {
		if (nomination != null && nomination.eIsProxy()) {
			InternalEObject oldNomination = (InternalEObject)nomination;
			nomination = (AbstractNomination)eResolveProxy(oldNomination);
			if (nomination != oldNomination) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, NominationsPackage.NOMINATION_AUDIT_ITEM__NOMINATION, oldNomination, nomination));
			}
		}
		return nomination;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AbstractNomination basicGetNomination() {
		return nomination;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setNomination(AbstractNomination newNomination) {
		AbstractNomination oldNomination = nomination;
		nomination = newNomination;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NominationsPackage.NOMINATION_AUDIT_ITEM__NOMINATION, oldNomination, nomination));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case NominationsPackage.NOMINATION_AUDIT_ITEM__NOMINATION:
				if (resolve) return getNomination();
				return basicGetNomination();
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
			case NominationsPackage.NOMINATION_AUDIT_ITEM__NOMINATION:
				setNomination((AbstractNomination)newValue);
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
			case NominationsPackage.NOMINATION_AUDIT_ITEM__NOMINATION:
				setNomination((AbstractNomination)null);
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
			case NominationsPackage.NOMINATION_AUDIT_ITEM__NOMINATION:
				return nomination != null;
		}
		return super.eIsSet(featureID);
	}

} //NominationAuditItemImpl
