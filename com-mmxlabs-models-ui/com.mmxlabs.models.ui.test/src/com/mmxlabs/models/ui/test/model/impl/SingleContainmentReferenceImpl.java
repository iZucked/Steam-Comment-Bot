/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.ui.test.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import com.mmxlabs.models.ui.test.model.ModelPackage;
import com.mmxlabs.models.ui.test.model.SimpleObject;
import com.mmxlabs.models.ui.test.model.SingleContainmentReference;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Single Containment Reference</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.ui.test.model.impl.SingleContainmentReferenceImpl#getSimpleObject <em>Simple Object</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SingleContainmentReferenceImpl extends EObjectImpl implements SingleContainmentReference {
	/**
	 * The cached value of the '{@link #getSimpleObject() <em>Simple Object</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSimpleObject()
	 * @generated
	 * @ordered
	 */
	protected SimpleObject simpleObject;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SingleContainmentReferenceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.SINGLE_CONTAINMENT_REFERENCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SimpleObject getSimpleObject() {
		return simpleObject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSimpleObject(SimpleObject newSimpleObject, NotificationChain msgs) {
		SimpleObject oldSimpleObject = simpleObject;
		simpleObject = newSimpleObject;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ModelPackage.SINGLE_CONTAINMENT_REFERENCE__SIMPLE_OBJECT, oldSimpleObject, newSimpleObject);
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
	public void setSimpleObject(SimpleObject newSimpleObject) {
		if (newSimpleObject != simpleObject) {
			NotificationChain msgs = null;
			if (simpleObject != null)
				msgs = ((InternalEObject)simpleObject).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ModelPackage.SINGLE_CONTAINMENT_REFERENCE__SIMPLE_OBJECT, null, msgs);
			if (newSimpleObject != null)
				msgs = ((InternalEObject)newSimpleObject).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ModelPackage.SINGLE_CONTAINMENT_REFERENCE__SIMPLE_OBJECT, null, msgs);
			msgs = basicSetSimpleObject(newSimpleObject, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.SINGLE_CONTAINMENT_REFERENCE__SIMPLE_OBJECT, newSimpleObject, newSimpleObject));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ModelPackage.SINGLE_CONTAINMENT_REFERENCE__SIMPLE_OBJECT:
				return basicSetSimpleObject(null, msgs);
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
			case ModelPackage.SINGLE_CONTAINMENT_REFERENCE__SIMPLE_OBJECT:
				return getSimpleObject();
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
			case ModelPackage.SINGLE_CONTAINMENT_REFERENCE__SIMPLE_OBJECT:
				setSimpleObject((SimpleObject)newValue);
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
			case ModelPackage.SINGLE_CONTAINMENT_REFERENCE__SIMPLE_OBJECT:
				setSimpleObject((SimpleObject)null);
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
			case ModelPackage.SINGLE_CONTAINMENT_REFERENCE__SIMPLE_OBJECT:
				return simpleObject != null;
		}
		return super.eIsSet(featureID);
	}

} //SingleContainmentReferenceImpl
