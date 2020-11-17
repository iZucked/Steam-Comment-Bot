/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.RelativeEntitlementElement;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Relative Entitlement Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.RelativeEntitlementElementImpl#getEntity <em>Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.RelativeEntitlementElementImpl#getEntitlement <em>Entitlement</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RelativeEntitlementElementImpl extends EObjectImpl implements RelativeEntitlementElement {
	/**
	 * The cached value of the '{@link #getEntity() <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntity()
	 * @generated
	 * @ordered
	 */
	protected BaseLegalEntity entity;

	/**
	 * The default value of the '{@link #getEntitlement() <em>Entitlement</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntitlement()
	 * @generated
	 * @ordered
	 */
	protected static final double ENTITLEMENT_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getEntitlement() <em>Entitlement</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntitlement()
	 * @generated
	 * @ordered
	 */
	protected double entitlement = ENTITLEMENT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RelativeEntitlementElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.RELATIVE_ENTITLEMENT_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BaseLegalEntity getEntity() {
		if (entity != null && entity.eIsProxy()) {
			InternalEObject oldEntity = (InternalEObject)entity;
			entity = (BaseLegalEntity)eResolveProxy(oldEntity);
			if (entity != oldEntity) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.RELATIVE_ENTITLEMENT_ELEMENT__ENTITY, oldEntity, entity));
			}
		}
		return entity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseLegalEntity basicGetEntity() {
		return entity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEntity(BaseLegalEntity newEntity) {
		BaseLegalEntity oldEntity = entity;
		entity = newEntity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.RELATIVE_ENTITLEMENT_ELEMENT__ENTITY, oldEntity, entity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getEntitlement() {
		return entitlement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEntitlement(double newEntitlement) {
		double oldEntitlement = entitlement;
		entitlement = newEntitlement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.RELATIVE_ENTITLEMENT_ELEMENT__ENTITLEMENT, oldEntitlement, entitlement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ADPPackage.RELATIVE_ENTITLEMENT_ELEMENT__ENTITY:
				if (resolve) return getEntity();
				return basicGetEntity();
			case ADPPackage.RELATIVE_ENTITLEMENT_ELEMENT__ENTITLEMENT:
				return getEntitlement();
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
			case ADPPackage.RELATIVE_ENTITLEMENT_ELEMENT__ENTITY:
				setEntity((BaseLegalEntity)newValue);
				return;
			case ADPPackage.RELATIVE_ENTITLEMENT_ELEMENT__ENTITLEMENT:
				setEntitlement((Double)newValue);
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
			case ADPPackage.RELATIVE_ENTITLEMENT_ELEMENT__ENTITY:
				setEntity((BaseLegalEntity)null);
				return;
			case ADPPackage.RELATIVE_ENTITLEMENT_ELEMENT__ENTITLEMENT:
				setEntitlement(ENTITLEMENT_EDEFAULT);
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
			case ADPPackage.RELATIVE_ENTITLEMENT_ELEMENT__ENTITY:
				return entity != null;
			case ADPPackage.RELATIVE_ENTITLEMENT_ELEMENT__ENTITLEMENT:
				return entitlement != ENTITLEMENT_EDEFAULT;
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
		result.append(" (entitlement: ");
		result.append(entitlement);
		result.append(')');
		return result.toString();
	}

} //RelativeEntitlementElementImpl
