/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.ui.test.model.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import com.mmxlabs.models.ui.test.model.ModelFactory;
import com.mmxlabs.models.ui.test.model.ModelPackage;
import com.mmxlabs.models.ui.test.model.ModelRoot;
import com.mmxlabs.models.ui.test.model.MultipleContainmentReference;
import com.mmxlabs.models.ui.test.model.MultipleReference;
import com.mmxlabs.models.ui.test.model.SimpleObject;
import com.mmxlabs.models.ui.test.model.SingleContainmentReference;
import com.mmxlabs.models.ui.test.model.SingleReference;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ModelFactoryImpl extends EFactoryImpl implements ModelFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ModelFactory init() {
		try {
			ModelFactory theModelFactory = (ModelFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.mmxlabs.com/models/ui/test/dialogecorecopier/model"); 
			if (theModelFactory != null) {
				return theModelFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ModelFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case ModelPackage.MODEL_ROOT: return createModelRoot();
			case ModelPackage.SIMPLE_OBJECT: return createSimpleObject();
			case ModelPackage.SINGLE_REFERENCE: return createSingleReference();
			case ModelPackage.MULTIPLE_REFERENCE: return createMultipleReference();
			case ModelPackage.SINGLE_CONTAINMENT_REFERENCE: return createSingleContainmentReference();
			case ModelPackage.MULTIPLE_CONTAINMENT_REFERENCE: return createMultipleContainmentReference();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ModelRoot createModelRoot() {
		ModelRootImpl modelRoot = new ModelRootImpl();
		return modelRoot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SimpleObject createSimpleObject() {
		SimpleObjectImpl simpleObject = new SimpleObjectImpl();
		return simpleObject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SingleReference createSingleReference() {
		SingleReferenceImpl singleReference = new SingleReferenceImpl();
		return singleReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MultipleReference createMultipleReference() {
		MultipleReferenceImpl multipleReference = new MultipleReferenceImpl();
		return multipleReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SingleContainmentReference createSingleContainmentReference() {
		SingleContainmentReferenceImpl singleContainmentReference = new SingleContainmentReferenceImpl();
		return singleContainmentReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MultipleContainmentReference createMultipleContainmentReference() {
		MultipleContainmentReferenceImpl multipleContainmentReference = new MultipleContainmentReferenceImpl();
		return multipleContainmentReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ModelPackage getModelPackage() {
		return (ModelPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ModelPackage getPackage() {
		return ModelPackage.eINSTANCE;
	}

} //ModelFactoryImpl
