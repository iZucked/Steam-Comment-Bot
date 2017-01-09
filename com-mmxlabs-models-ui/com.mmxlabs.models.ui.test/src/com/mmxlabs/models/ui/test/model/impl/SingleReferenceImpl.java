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
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.mmxlabs.models.ui.test.model.ModelPackage;
import com.mmxlabs.models.ui.test.model.ModelRoot;
import com.mmxlabs.models.ui.test.model.MultipleReference;
import com.mmxlabs.models.ui.test.model.SingleReference;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Single Reference</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.ui.test.model.impl.SingleReferenceImpl#getMultipleReference <em>Multiple Reference</em>}</li>
 *   <li>{@link com.mmxlabs.models.ui.test.model.impl.SingleReferenceImpl#getModelRoot <em>Model Root</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SingleReferenceImpl extends EObjectImpl implements SingleReference {
	/**
	 * The cached value of the '{@link #getMultipleReference() <em>Multiple Reference</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMultipleReference()
	 * @generated
	 * @ordered
	 */
	protected MultipleReference multipleReference;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SingleReferenceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.SINGLE_REFERENCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MultipleReference getMultipleReference() {
		if (multipleReference != null && multipleReference.eIsProxy()) {
			InternalEObject oldMultipleReference = (InternalEObject)multipleReference;
			multipleReference = (MultipleReference)eResolveProxy(oldMultipleReference);
			if (multipleReference != oldMultipleReference) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ModelPackage.SINGLE_REFERENCE__MULTIPLE_REFERENCE, oldMultipleReference, multipleReference));
			}
		}
		return multipleReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MultipleReference basicGetMultipleReference() {
		return multipleReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMultipleReference(MultipleReference newMultipleReference, NotificationChain msgs) {
		MultipleReference oldMultipleReference = multipleReference;
		multipleReference = newMultipleReference;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ModelPackage.SINGLE_REFERENCE__MULTIPLE_REFERENCE, oldMultipleReference, newMultipleReference);
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
	public void setMultipleReference(MultipleReference newMultipleReference) {
		if (newMultipleReference != multipleReference) {
			NotificationChain msgs = null;
			if (multipleReference != null)
				msgs = ((InternalEObject)multipleReference).eInverseRemove(this, ModelPackage.MULTIPLE_REFERENCE__SINGLE_REFERENCES, MultipleReference.class, msgs);
			if (newMultipleReference != null)
				msgs = ((InternalEObject)newMultipleReference).eInverseAdd(this, ModelPackage.MULTIPLE_REFERENCE__SINGLE_REFERENCES, MultipleReference.class, msgs);
			msgs = basicSetMultipleReference(newMultipleReference, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.SINGLE_REFERENCE__MULTIPLE_REFERENCE, newMultipleReference, newMultipleReference));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ModelRoot getModelRoot() {
		if (eContainerFeatureID() != ModelPackage.SINGLE_REFERENCE__MODEL_ROOT) return null;
		return (ModelRoot)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetModelRoot(ModelRoot newModelRoot, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newModelRoot, ModelPackage.SINGLE_REFERENCE__MODEL_ROOT, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setModelRoot(ModelRoot newModelRoot) {
		if (newModelRoot != eInternalContainer() || (eContainerFeatureID() != ModelPackage.SINGLE_REFERENCE__MODEL_ROOT && newModelRoot != null)) {
			if (EcoreUtil.isAncestor(this, newModelRoot))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newModelRoot != null)
				msgs = ((InternalEObject)newModelRoot).eInverseAdd(this, ModelPackage.MODEL_ROOT__SINGLE_REFERENCES, ModelRoot.class, msgs);
			msgs = basicSetModelRoot(newModelRoot, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.SINGLE_REFERENCE__MODEL_ROOT, newModelRoot, newModelRoot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ModelPackage.SINGLE_REFERENCE__MULTIPLE_REFERENCE:
				if (multipleReference != null)
					msgs = ((InternalEObject)multipleReference).eInverseRemove(this, ModelPackage.MULTIPLE_REFERENCE__SINGLE_REFERENCES, MultipleReference.class, msgs);
				return basicSetMultipleReference((MultipleReference)otherEnd, msgs);
			case ModelPackage.SINGLE_REFERENCE__MODEL_ROOT:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetModelRoot((ModelRoot)otherEnd, msgs);
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
			case ModelPackage.SINGLE_REFERENCE__MULTIPLE_REFERENCE:
				return basicSetMultipleReference(null, msgs);
			case ModelPackage.SINGLE_REFERENCE__MODEL_ROOT:
				return basicSetModelRoot(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case ModelPackage.SINGLE_REFERENCE__MODEL_ROOT:
				return eInternalContainer().eInverseRemove(this, ModelPackage.MODEL_ROOT__SINGLE_REFERENCES, ModelRoot.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ModelPackage.SINGLE_REFERENCE__MULTIPLE_REFERENCE:
				if (resolve) return getMultipleReference();
				return basicGetMultipleReference();
			case ModelPackage.SINGLE_REFERENCE__MODEL_ROOT:
				return getModelRoot();
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
			case ModelPackage.SINGLE_REFERENCE__MULTIPLE_REFERENCE:
				setMultipleReference((MultipleReference)newValue);
				return;
			case ModelPackage.SINGLE_REFERENCE__MODEL_ROOT:
				setModelRoot((ModelRoot)newValue);
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
			case ModelPackage.SINGLE_REFERENCE__MULTIPLE_REFERENCE:
				setMultipleReference((MultipleReference)null);
				return;
			case ModelPackage.SINGLE_REFERENCE__MODEL_ROOT:
				setModelRoot((ModelRoot)null);
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
			case ModelPackage.SINGLE_REFERENCE__MULTIPLE_REFERENCE:
				return multipleReference != null;
			case ModelPackage.SINGLE_REFERENCE__MODEL_ROOT:
				return getModelRoot() != null;
		}
		return super.eIsSet(featureID);
	}

} //SingleReferenceImpl
