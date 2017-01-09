/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.ui.test.model.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.ui.test.model.ModelPackage;
import com.mmxlabs.models.ui.test.model.ModelRoot;
import com.mmxlabs.models.ui.test.model.MultipleContainmentReference;
import com.mmxlabs.models.ui.test.model.MultipleReference;
import com.mmxlabs.models.ui.test.model.SimpleObject;
import com.mmxlabs.models.ui.test.model.SingleContainmentReference;
import com.mmxlabs.models.ui.test.model.SingleReference;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.ui.test.model.impl.ModelRootImpl#getSimpleObjects <em>Simple Objects</em>}</li>
 *   <li>{@link com.mmxlabs.models.ui.test.model.impl.ModelRootImpl#getSingleReferences <em>Single References</em>}</li>
 *   <li>{@link com.mmxlabs.models.ui.test.model.impl.ModelRootImpl#getMultipleReferences <em>Multiple References</em>}</li>
 *   <li>{@link com.mmxlabs.models.ui.test.model.impl.ModelRootImpl#getSingleContainmentReferences <em>Single Containment References</em>}</li>
 *   <li>{@link com.mmxlabs.models.ui.test.model.impl.ModelRootImpl#getMultipleContainmentReferences <em>Multiple Containment References</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ModelRootImpl extends EObjectImpl implements ModelRoot {
	/**
	 * The cached value of the '{@link #getSimpleObjects() <em>Simple Objects</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSimpleObjects()
	 * @generated
	 * @ordered
	 */
	protected EList<SimpleObject> simpleObjects;

	/**
	 * The cached value of the '{@link #getSingleReferences() <em>Single References</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSingleReferences()
	 * @generated
	 * @ordered
	 */
	protected EList<SingleReference> singleReferences;

	/**
	 * The cached value of the '{@link #getMultipleReferences() <em>Multiple References</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMultipleReferences()
	 * @generated
	 * @ordered
	 */
	protected EList<MultipleReference> multipleReferences;

	/**
	 * The cached value of the '{@link #getSingleContainmentReferences() <em>Single Containment References</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSingleContainmentReferences()
	 * @generated
	 * @ordered
	 */
	protected EList<SingleContainmentReference> singleContainmentReferences;

	/**
	 * The cached value of the '{@link #getMultipleContainmentReferences() <em>Multiple Containment References</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMultipleContainmentReferences()
	 * @generated
	 * @ordered
	 */
	protected EList<MultipleContainmentReference> multipleContainmentReferences;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ModelRootImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.MODEL_ROOT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<SimpleObject> getSimpleObjects() {
		if (simpleObjects == null) {
			simpleObjects = new EObjectContainmentEList<SimpleObject>(SimpleObject.class, this, ModelPackage.MODEL_ROOT__SIMPLE_OBJECTS);
		}
		return simpleObjects;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<SingleReference> getSingleReferences() {
		if (singleReferences == null) {
			singleReferences = new EObjectContainmentWithInverseEList<SingleReference>(SingleReference.class, this, ModelPackage.MODEL_ROOT__SINGLE_REFERENCES, ModelPackage.SINGLE_REFERENCE__MODEL_ROOT);
		}
		return singleReferences;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<MultipleReference> getMultipleReferences() {
		if (multipleReferences == null) {
			multipleReferences = new EObjectContainmentWithInverseEList<MultipleReference>(MultipleReference.class, this, ModelPackage.MODEL_ROOT__MULTIPLE_REFERENCES, ModelPackage.MULTIPLE_REFERENCE__MODEL_ROOT);
		}
		return multipleReferences;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<SingleContainmentReference> getSingleContainmentReferences() {
		if (singleContainmentReferences == null) {
			singleContainmentReferences = new EObjectContainmentEList<SingleContainmentReference>(SingleContainmentReference.class, this, ModelPackage.MODEL_ROOT__SINGLE_CONTAINMENT_REFERENCES);
		}
		return singleContainmentReferences;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<MultipleContainmentReference> getMultipleContainmentReferences() {
		if (multipleContainmentReferences == null) {
			multipleContainmentReferences = new EObjectContainmentEList<MultipleContainmentReference>(MultipleContainmentReference.class, this, ModelPackage.MODEL_ROOT__MULTIPLE_CONTAINMENT_REFERENCES);
		}
		return multipleContainmentReferences;
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
			case ModelPackage.MODEL_ROOT__SINGLE_REFERENCES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getSingleReferences()).basicAdd(otherEnd, msgs);
			case ModelPackage.MODEL_ROOT__MULTIPLE_REFERENCES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getMultipleReferences()).basicAdd(otherEnd, msgs);
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
			case ModelPackage.MODEL_ROOT__SIMPLE_OBJECTS:
				return ((InternalEList<?>)getSimpleObjects()).basicRemove(otherEnd, msgs);
			case ModelPackage.MODEL_ROOT__SINGLE_REFERENCES:
				return ((InternalEList<?>)getSingleReferences()).basicRemove(otherEnd, msgs);
			case ModelPackage.MODEL_ROOT__MULTIPLE_REFERENCES:
				return ((InternalEList<?>)getMultipleReferences()).basicRemove(otherEnd, msgs);
			case ModelPackage.MODEL_ROOT__SINGLE_CONTAINMENT_REFERENCES:
				return ((InternalEList<?>)getSingleContainmentReferences()).basicRemove(otherEnd, msgs);
			case ModelPackage.MODEL_ROOT__MULTIPLE_CONTAINMENT_REFERENCES:
				return ((InternalEList<?>)getMultipleContainmentReferences()).basicRemove(otherEnd, msgs);
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
			case ModelPackage.MODEL_ROOT__SIMPLE_OBJECTS:
				return getSimpleObjects();
			case ModelPackage.MODEL_ROOT__SINGLE_REFERENCES:
				return getSingleReferences();
			case ModelPackage.MODEL_ROOT__MULTIPLE_REFERENCES:
				return getMultipleReferences();
			case ModelPackage.MODEL_ROOT__SINGLE_CONTAINMENT_REFERENCES:
				return getSingleContainmentReferences();
			case ModelPackage.MODEL_ROOT__MULTIPLE_CONTAINMENT_REFERENCES:
				return getMultipleContainmentReferences();
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
			case ModelPackage.MODEL_ROOT__SIMPLE_OBJECTS:
				getSimpleObjects().clear();
				getSimpleObjects().addAll((Collection<? extends SimpleObject>)newValue);
				return;
			case ModelPackage.MODEL_ROOT__SINGLE_REFERENCES:
				getSingleReferences().clear();
				getSingleReferences().addAll((Collection<? extends SingleReference>)newValue);
				return;
			case ModelPackage.MODEL_ROOT__MULTIPLE_REFERENCES:
				getMultipleReferences().clear();
				getMultipleReferences().addAll((Collection<? extends MultipleReference>)newValue);
				return;
			case ModelPackage.MODEL_ROOT__SINGLE_CONTAINMENT_REFERENCES:
				getSingleContainmentReferences().clear();
				getSingleContainmentReferences().addAll((Collection<? extends SingleContainmentReference>)newValue);
				return;
			case ModelPackage.MODEL_ROOT__MULTIPLE_CONTAINMENT_REFERENCES:
				getMultipleContainmentReferences().clear();
				getMultipleContainmentReferences().addAll((Collection<? extends MultipleContainmentReference>)newValue);
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
			case ModelPackage.MODEL_ROOT__SIMPLE_OBJECTS:
				getSimpleObjects().clear();
				return;
			case ModelPackage.MODEL_ROOT__SINGLE_REFERENCES:
				getSingleReferences().clear();
				return;
			case ModelPackage.MODEL_ROOT__MULTIPLE_REFERENCES:
				getMultipleReferences().clear();
				return;
			case ModelPackage.MODEL_ROOT__SINGLE_CONTAINMENT_REFERENCES:
				getSingleContainmentReferences().clear();
				return;
			case ModelPackage.MODEL_ROOT__MULTIPLE_CONTAINMENT_REFERENCES:
				getMultipleContainmentReferences().clear();
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
			case ModelPackage.MODEL_ROOT__SIMPLE_OBJECTS:
				return simpleObjects != null && !simpleObjects.isEmpty();
			case ModelPackage.MODEL_ROOT__SINGLE_REFERENCES:
				return singleReferences != null && !singleReferences.isEmpty();
			case ModelPackage.MODEL_ROOT__MULTIPLE_REFERENCES:
				return multipleReferences != null && !multipleReferences.isEmpty();
			case ModelPackage.MODEL_ROOT__SINGLE_CONTAINMENT_REFERENCES:
				return singleContainmentReferences != null && !singleContainmentReferences.isEmpty();
			case ModelPackage.MODEL_ROOT__MULTIPLE_CONTAINMENT_REFERENCES:
				return multipleContainmentReferences != null && !multipleContainmentReferences.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ModelRootImpl
