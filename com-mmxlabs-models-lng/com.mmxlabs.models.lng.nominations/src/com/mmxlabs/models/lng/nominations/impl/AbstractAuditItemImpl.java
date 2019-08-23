/**
 */
package com.mmxlabs.models.lng.nominations.impl;

import com.mmxlabs.models.lng.nominations.AbstractAuditItem;
import com.mmxlabs.models.lng.nominations.AuditItemType;
import com.mmxlabs.models.lng.nominations.NominationsPackage;

import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Audit Item</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.nominations.impl.AbstractAuditItemImpl#getAuditItemType <em>Audit Item Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AbstractAuditItemImpl extends UUIDObjectImpl implements AbstractAuditItem {
	/**
	 * The default value of the '{@link #getAuditItemType() <em>Audit Item Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAuditItemType()
	 * @generated
	 * @ordered
	 */
	protected static final AuditItemType AUDIT_ITEM_TYPE_EDEFAULT = AuditItemType.DELETE;

	/**
	 * The cached value of the '{@link #getAuditItemType() <em>Audit Item Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAuditItemType()
	 * @generated
	 * @ordered
	 */
	protected AuditItemType auditItemType = AUDIT_ITEM_TYPE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AbstractAuditItemImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return NominationsPackage.Literals.ABSTRACT_AUDIT_ITEM;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AuditItemType getAuditItemType() {
		return auditItemType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAuditItemType(AuditItemType newAuditItemType) {
		AuditItemType oldAuditItemType = auditItemType;
		auditItemType = newAuditItemType == null ? AUDIT_ITEM_TYPE_EDEFAULT : newAuditItemType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NominationsPackage.ABSTRACT_AUDIT_ITEM__AUDIT_ITEM_TYPE, oldAuditItemType, auditItemType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case NominationsPackage.ABSTRACT_AUDIT_ITEM__AUDIT_ITEM_TYPE:
				return getAuditItemType();
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
			case NominationsPackage.ABSTRACT_AUDIT_ITEM__AUDIT_ITEM_TYPE:
				setAuditItemType((AuditItemType)newValue);
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
			case NominationsPackage.ABSTRACT_AUDIT_ITEM__AUDIT_ITEM_TYPE:
				setAuditItemType(AUDIT_ITEM_TYPE_EDEFAULT);
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
			case NominationsPackage.ABSTRACT_AUDIT_ITEM__AUDIT_ITEM_TYPE:
				return auditItemType != AUDIT_ITEM_TYPE_EDEFAULT;
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
		result.append(" (auditItemType: ");
		result.append(auditItemType);
		result.append(')');
		return result.toString();
	}

} //AbstractAuditItemImpl
