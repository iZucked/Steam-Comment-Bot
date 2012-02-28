/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.mmxcore.impl;

import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXSubModel;
import com.mmxlabs.models.mmxcore.UUIDObject;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>MMX Sub Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.mmxcore.impl.MMXSubModelImpl#getSubModelInstance <em>Sub Model Instance</em>}</li>
 *   <li>{@link com.mmxlabs.models.mmxcore.impl.MMXSubModelImpl#getOriginalResource <em>Original Resource</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MMXSubModelImpl extends MMXObjectImpl implements MMXSubModel {
	/**
	 * The cached value of the '{@link #getSubModelInstance() <em>Sub Model Instance</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubModelInstance()
	 * @generated
	 * @ordered
	 */
	protected UUIDObject subModelInstance;

	/**
	 * The default value of the '{@link #getOriginalResource() <em>Original Resource</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginalResource()
	 * @generated
	 * @ordered
	 */
	protected static final Resource ORIGINAL_RESOURCE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOriginalResource() <em>Original Resource</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginalResource()
	 * @generated
	 * @ordered
	 */
	protected Resource originalResource = ORIGINAL_RESOURCE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MMXSubModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MMXCorePackage.Literals.MMX_SUB_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UUIDObject getSubModelInstance() {
		return subModelInstance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSubModelInstance(UUIDObject newSubModelInstance, NotificationChain msgs) {
		UUIDObject oldSubModelInstance = subModelInstance;
		subModelInstance = newSubModelInstance;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MMXCorePackage.MMX_SUB_MODEL__SUB_MODEL_INSTANCE, oldSubModelInstance, newSubModelInstance);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSubModelInstance(UUIDObject newSubModelInstance) {
		if (newSubModelInstance != subModelInstance) {
			NotificationChain msgs = null;
			if (subModelInstance != null)
				msgs = ((InternalEObject)subModelInstance).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MMXCorePackage.MMX_SUB_MODEL__SUB_MODEL_INSTANCE, null, msgs);
			if (newSubModelInstance != null)
				msgs = ((InternalEObject)newSubModelInstance).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MMXCorePackage.MMX_SUB_MODEL__SUB_MODEL_INSTANCE, null, msgs);
			msgs = basicSetSubModelInstance(newSubModelInstance, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MMXCorePackage.MMX_SUB_MODEL__SUB_MODEL_INSTANCE, newSubModelInstance, newSubModelInstance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Resource getOriginalResource() {
		return originalResource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOriginalResource(Resource newOriginalResource) {
		Resource oldOriginalResource = originalResource;
		originalResource = newOriginalResource;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MMXCorePackage.MMX_SUB_MODEL__ORIGINAL_RESOURCE, oldOriginalResource, originalResource));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MMXCorePackage.MMX_SUB_MODEL__SUB_MODEL_INSTANCE:
				return basicSetSubModelInstance(null, msgs);
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
			case MMXCorePackage.MMX_SUB_MODEL__SUB_MODEL_INSTANCE:
				return getSubModelInstance();
			case MMXCorePackage.MMX_SUB_MODEL__ORIGINAL_RESOURCE:
				return getOriginalResource();
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
			case MMXCorePackage.MMX_SUB_MODEL__SUB_MODEL_INSTANCE:
				setSubModelInstance((UUIDObject)newValue);
				return;
			case MMXCorePackage.MMX_SUB_MODEL__ORIGINAL_RESOURCE:
				setOriginalResource((Resource)newValue);
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
			case MMXCorePackage.MMX_SUB_MODEL__SUB_MODEL_INSTANCE:
				setSubModelInstance((UUIDObject)null);
				return;
			case MMXCorePackage.MMX_SUB_MODEL__ORIGINAL_RESOURCE:
				setOriginalResource(ORIGINAL_RESOURCE_EDEFAULT);
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
			case MMXCorePackage.MMX_SUB_MODEL__SUB_MODEL_INSTANCE:
				return subModelInstance != null;
			case MMXCorePackage.MMX_SUB_MODEL__ORIGINAL_RESOURCE:
				return ORIGINAL_RESOURCE_EDEFAULT == null ? originalResource != null : !ORIGINAL_RESOURCE_EDEFAULT.equals(originalResource);
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
		result.append(" (originalResource: ");
		result.append(originalResource);
		result.append(')');
		return result.toString();
	}

} //MMXSubModelImpl
