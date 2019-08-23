/**
 */
package com.mmxlabs.models.lng.nominations.impl;

import com.mmxlabs.models.lng.nominations.AbstractNominationSpec;
import com.mmxlabs.models.lng.nominations.NominationSpecAuditItem;
import com.mmxlabs.models.lng.nominations.NominationsPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Nomination Spec Audit Item</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.nominations.impl.NominationSpecAuditItemImpl#getNominationSpec <em>Nomination Spec</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NominationSpecAuditItemImpl extends AbstractAuditItemImpl implements NominationSpecAuditItem {
	/**
	 * The cached value of the '{@link #getNominationSpec() <em>Nomination Spec</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNominationSpec()
	 * @generated
	 * @ordered
	 */
	protected AbstractNominationSpec nominationSpec;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NominationSpecAuditItemImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return NominationsPackage.Literals.NOMINATION_SPEC_AUDIT_ITEM;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AbstractNominationSpec getNominationSpec() {
		if (nominationSpec != null && nominationSpec.eIsProxy()) {
			InternalEObject oldNominationSpec = (InternalEObject)nominationSpec;
			nominationSpec = (AbstractNominationSpec)eResolveProxy(oldNominationSpec);
			if (nominationSpec != oldNominationSpec) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, NominationsPackage.NOMINATION_SPEC_AUDIT_ITEM__NOMINATION_SPEC, oldNominationSpec, nominationSpec));
			}
		}
		return nominationSpec;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AbstractNominationSpec basicGetNominationSpec() {
		return nominationSpec;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setNominationSpec(AbstractNominationSpec newNominationSpec) {
		AbstractNominationSpec oldNominationSpec = nominationSpec;
		nominationSpec = newNominationSpec;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NominationsPackage.NOMINATION_SPEC_AUDIT_ITEM__NOMINATION_SPEC, oldNominationSpec, nominationSpec));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case NominationsPackage.NOMINATION_SPEC_AUDIT_ITEM__NOMINATION_SPEC:
				if (resolve) return getNominationSpec();
				return basicGetNominationSpec();
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
			case NominationsPackage.NOMINATION_SPEC_AUDIT_ITEM__NOMINATION_SPEC:
				setNominationSpec((AbstractNominationSpec)newValue);
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
			case NominationsPackage.NOMINATION_SPEC_AUDIT_ITEM__NOMINATION_SPEC:
				setNominationSpec((AbstractNominationSpec)null);
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
			case NominationsPackage.NOMINATION_SPEC_AUDIT_ITEM__NOMINATION_SPEC:
				return nominationSpec != null;
		}
		return super.eIsSet(featureID);
	}

} //NominationSpecAuditItemImpl
