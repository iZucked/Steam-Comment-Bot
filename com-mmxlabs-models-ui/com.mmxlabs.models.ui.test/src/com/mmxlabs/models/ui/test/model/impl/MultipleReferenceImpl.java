/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.ui.test.model.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.ui.test.model.ModelPackage;
import com.mmxlabs.models.ui.test.model.ModelRoot;
import com.mmxlabs.models.ui.test.model.MultipleReference;
import com.mmxlabs.models.ui.test.model.SingleReference;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Multiple Reference</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.ui.test.model.impl.MultipleReferenceImpl#getSingleReferences <em>Single References</em>}</li>
 *   <li>{@link com.mmxlabs.models.ui.test.model.impl.MultipleReferenceImpl#getModelRoot <em>Model Root</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MultipleReferenceImpl extends EObjectImpl implements MultipleReference {
	/**
	 * The cached value of the '{@link #getSingleReferences() <em>Single References</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSingleReferences()
	 * @generated
	 * @ordered
	 */
	protected EList<SingleReference> singleReferences;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MultipleReferenceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.MULTIPLE_REFERENCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<SingleReference> getSingleReferences() {
		if (singleReferences == null) {
			singleReferences = new EObjectWithInverseResolvingEList<SingleReference>(SingleReference.class, this, ModelPackage.MULTIPLE_REFERENCE__SINGLE_REFERENCES, ModelPackage.SINGLE_REFERENCE__MULTIPLE_REFERENCE);
		}
		return singleReferences;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ModelRoot getModelRoot() {
		if (eContainerFeatureID() != ModelPackage.MULTIPLE_REFERENCE__MODEL_ROOT) return null;
		return (ModelRoot)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetModelRoot(ModelRoot newModelRoot, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newModelRoot, ModelPackage.MULTIPLE_REFERENCE__MODEL_ROOT, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setModelRoot(ModelRoot newModelRoot) {
		if (newModelRoot != eInternalContainer() || (eContainerFeatureID() != ModelPackage.MULTIPLE_REFERENCE__MODEL_ROOT && newModelRoot != null)) {
			if (EcoreUtil.isAncestor(this, newModelRoot))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newModelRoot != null)
				msgs = ((InternalEObject)newModelRoot).eInverseAdd(this, ModelPackage.MODEL_ROOT__MULTIPLE_REFERENCES, ModelRoot.class, msgs);
			msgs = basicSetModelRoot(newModelRoot, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MULTIPLE_REFERENCE__MODEL_ROOT, newModelRoot, newModelRoot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ModelPackage.MULTIPLE_REFERENCE__SINGLE_REFERENCES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getSingleReferences()).basicAdd(otherEnd, msgs);
			case ModelPackage.MULTIPLE_REFERENCE__MODEL_ROOT:
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
			case ModelPackage.MULTIPLE_REFERENCE__SINGLE_REFERENCES:
				return ((InternalEList<?>)getSingleReferences()).basicRemove(otherEnd, msgs);
			case ModelPackage.MULTIPLE_REFERENCE__MODEL_ROOT:
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
			case ModelPackage.MULTIPLE_REFERENCE__MODEL_ROOT:
				return eInternalContainer().eInverseRemove(this, ModelPackage.MODEL_ROOT__MULTIPLE_REFERENCES, ModelRoot.class, msgs);
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
			case ModelPackage.MULTIPLE_REFERENCE__SINGLE_REFERENCES:
				return getSingleReferences();
			case ModelPackage.MULTIPLE_REFERENCE__MODEL_ROOT:
				return getModelRoot();
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
			case ModelPackage.MULTIPLE_REFERENCE__SINGLE_REFERENCES:
				getSingleReferences().clear();
				getSingleReferences().addAll((Collection<? extends SingleReference>)newValue);
				return;
			case ModelPackage.MULTIPLE_REFERENCE__MODEL_ROOT:
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
			case ModelPackage.MULTIPLE_REFERENCE__SINGLE_REFERENCES:
				getSingleReferences().clear();
				return;
			case ModelPackage.MULTIPLE_REFERENCE__MODEL_ROOT:
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
			case ModelPackage.MULTIPLE_REFERENCE__SINGLE_REFERENCES:
				return singleReferences != null && !singleReferences.isEmpty();
			case ModelPackage.MULTIPLE_REFERENCE__MODEL_ROOT:
				return getModelRoot() != null;
		}
		return super.eIsSet(featureID);
	}

} //MultipleReferenceImpl
