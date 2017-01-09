/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.ui.test.model;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.ui.test.model.ModelFactory
 * @model kind="package"
 * @generated
 */
public interface ModelPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "model";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.mmxlabs.com/models/ui/test/dialogecorecopier/model";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "lng.cargomodel";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ModelPackage eINSTANCE = com.mmxlabs.models.ui.test.model.impl.ModelPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.ui.test.model.impl.ModelRootImpl <em>Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.ui.test.model.impl.ModelRootImpl
	 * @see com.mmxlabs.models.ui.test.model.impl.ModelPackageImpl#getModelRoot()
	 * @generated
	 */
	int MODEL_ROOT = 0;

	/**
	 * The feature id for the '<em><b>Simple Objects</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ROOT__SIMPLE_OBJECTS = 0;

	/**
	 * The feature id for the '<em><b>Single References</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ROOT__SINGLE_REFERENCES = 1;

	/**
	 * The feature id for the '<em><b>Multiple References</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ROOT__MULTIPLE_REFERENCES = 2;

	/**
	 * The feature id for the '<em><b>Single Containment References</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ROOT__SINGLE_CONTAINMENT_REFERENCES = 3;

	/**
	 * The feature id for the '<em><b>Multiple Containment References</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ROOT__MULTIPLE_CONTAINMENT_REFERENCES = 4;

	/**
	 * The number of structural features of the '<em>Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ROOT_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.ui.test.model.impl.SimpleObjectImpl <em>Simple Object</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.ui.test.model.impl.SimpleObjectImpl
	 * @see com.mmxlabs.models.ui.test.model.impl.ModelPackageImpl#getSimpleObject()
	 * @generated
	 */
	int SIMPLE_OBJECT = 1;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_OBJECT__ATTRIBUTE = 0;

	/**
	 * The number of structural features of the '<em>Simple Object</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_OBJECT_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.ui.test.model.impl.SingleReferenceImpl <em>Single Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.ui.test.model.impl.SingleReferenceImpl
	 * @see com.mmxlabs.models.ui.test.model.impl.ModelPackageImpl#getSingleReference()
	 * @generated
	 */
	int SINGLE_REFERENCE = 2;

	/**
	 * The feature id for the '<em><b>Multiple Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SINGLE_REFERENCE__MULTIPLE_REFERENCE = 0;

	/**
	 * The feature id for the '<em><b>Model Root</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SINGLE_REFERENCE__MODEL_ROOT = 1;

	/**
	 * The number of structural features of the '<em>Single Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SINGLE_REFERENCE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.ui.test.model.impl.MultipleReferenceImpl <em>Multiple Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.ui.test.model.impl.MultipleReferenceImpl
	 * @see com.mmxlabs.models.ui.test.model.impl.ModelPackageImpl#getMultipleReference()
	 * @generated
	 */
	int MULTIPLE_REFERENCE = 3;

	/**
	 * The feature id for the '<em><b>Single References</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULTIPLE_REFERENCE__SINGLE_REFERENCES = 0;

	/**
	 * The feature id for the '<em><b>Model Root</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULTIPLE_REFERENCE__MODEL_ROOT = 1;

	/**
	 * The number of structural features of the '<em>Multiple Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULTIPLE_REFERENCE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.ui.test.model.impl.SingleContainmentReferenceImpl <em>Single Containment Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.ui.test.model.impl.SingleContainmentReferenceImpl
	 * @see com.mmxlabs.models.ui.test.model.impl.ModelPackageImpl#getSingleContainmentReference()
	 * @generated
	 */
	int SINGLE_CONTAINMENT_REFERENCE = 4;

	/**
	 * The feature id for the '<em><b>Simple Object</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SINGLE_CONTAINMENT_REFERENCE__SIMPLE_OBJECT = 0;

	/**
	 * The number of structural features of the '<em>Single Containment Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SINGLE_CONTAINMENT_REFERENCE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.ui.test.model.impl.MultipleContainmentReferenceImpl <em>Multiple Containment Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.ui.test.model.impl.MultipleContainmentReferenceImpl
	 * @see com.mmxlabs.models.ui.test.model.impl.ModelPackageImpl#getMultipleContainmentReference()
	 * @generated
	 */
	int MULTIPLE_CONTAINMENT_REFERENCE = 5;

	/**
	 * The feature id for the '<em><b>Simple Objects</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULTIPLE_CONTAINMENT_REFERENCE__SIMPLE_OBJECTS = 0;

	/**
	 * The number of structural features of the '<em>Multiple Containment Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULTIPLE_CONTAINMENT_REFERENCE_FEATURE_COUNT = 1;


	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.ui.test.model.ModelRoot <em>Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Root</em>'.
	 * @see com.mmxlabs.models.ui.test.model.ModelRoot
	 * @generated
	 */
	EClass getModelRoot();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.ui.test.model.ModelRoot#getSimpleObjects <em>Simple Objects</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Simple Objects</em>'.
	 * @see com.mmxlabs.models.ui.test.model.ModelRoot#getSimpleObjects()
	 * @see #getModelRoot()
	 * @generated
	 */
	EReference getModelRoot_SimpleObjects();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.ui.test.model.ModelRoot#getSingleReferences <em>Single References</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Single References</em>'.
	 * @see com.mmxlabs.models.ui.test.model.ModelRoot#getSingleReferences()
	 * @see #getModelRoot()
	 * @generated
	 */
	EReference getModelRoot_SingleReferences();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.ui.test.model.ModelRoot#getMultipleReferences <em>Multiple References</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Multiple References</em>'.
	 * @see com.mmxlabs.models.ui.test.model.ModelRoot#getMultipleReferences()
	 * @see #getModelRoot()
	 * @generated
	 */
	EReference getModelRoot_MultipleReferences();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.ui.test.model.ModelRoot#getSingleContainmentReferences <em>Single Containment References</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Single Containment References</em>'.
	 * @see com.mmxlabs.models.ui.test.model.ModelRoot#getSingleContainmentReferences()
	 * @see #getModelRoot()
	 * @generated
	 */
	EReference getModelRoot_SingleContainmentReferences();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.ui.test.model.ModelRoot#getMultipleContainmentReferences <em>Multiple Containment References</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Multiple Containment References</em>'.
	 * @see com.mmxlabs.models.ui.test.model.ModelRoot#getMultipleContainmentReferences()
	 * @see #getModelRoot()
	 * @generated
	 */
	EReference getModelRoot_MultipleContainmentReferences();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.ui.test.model.SimpleObject <em>Simple Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Simple Object</em>'.
	 * @see com.mmxlabs.models.ui.test.model.SimpleObject
	 * @generated
	 */
	EClass getSimpleObject();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.ui.test.model.SimpleObject#getAttribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Attribute</em>'.
	 * @see com.mmxlabs.models.ui.test.model.SimpleObject#getAttribute()
	 * @see #getSimpleObject()
	 * @generated
	 */
	EAttribute getSimpleObject_Attribute();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.ui.test.model.SingleReference <em>Single Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Single Reference</em>'.
	 * @see com.mmxlabs.models.ui.test.model.SingleReference
	 * @generated
	 */
	EClass getSingleReference();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.ui.test.model.SingleReference#getMultipleReference <em>Multiple Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Multiple Reference</em>'.
	 * @see com.mmxlabs.models.ui.test.model.SingleReference#getMultipleReference()
	 * @see #getSingleReference()
	 * @generated
	 */
	EReference getSingleReference_MultipleReference();

	/**
	 * Returns the meta object for the container reference '{@link com.mmxlabs.models.ui.test.model.SingleReference#getModelRoot <em>Model Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Model Root</em>'.
	 * @see com.mmxlabs.models.ui.test.model.SingleReference#getModelRoot()
	 * @see #getSingleReference()
	 * @generated
	 */
	EReference getSingleReference_ModelRoot();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.ui.test.model.MultipleReference <em>Multiple Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Multiple Reference</em>'.
	 * @see com.mmxlabs.models.ui.test.model.MultipleReference
	 * @generated
	 */
	EClass getMultipleReference();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.ui.test.model.MultipleReference#getSingleReferences <em>Single References</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Single References</em>'.
	 * @see com.mmxlabs.models.ui.test.model.MultipleReference#getSingleReferences()
	 * @see #getMultipleReference()
	 * @generated
	 */
	EReference getMultipleReference_SingleReferences();

	/**
	 * Returns the meta object for the container reference '{@link com.mmxlabs.models.ui.test.model.MultipleReference#getModelRoot <em>Model Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Model Root</em>'.
	 * @see com.mmxlabs.models.ui.test.model.MultipleReference#getModelRoot()
	 * @see #getMultipleReference()
	 * @generated
	 */
	EReference getMultipleReference_ModelRoot();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.ui.test.model.SingleContainmentReference <em>Single Containment Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Single Containment Reference</em>'.
	 * @see com.mmxlabs.models.ui.test.model.SingleContainmentReference
	 * @generated
	 */
	EClass getSingleContainmentReference();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.ui.test.model.SingleContainmentReference#getSimpleObject <em>Simple Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Simple Object</em>'.
	 * @see com.mmxlabs.models.ui.test.model.SingleContainmentReference#getSimpleObject()
	 * @see #getSingleContainmentReference()
	 * @generated
	 */
	EReference getSingleContainmentReference_SimpleObject();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.ui.test.model.MultipleContainmentReference <em>Multiple Containment Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Multiple Containment Reference</em>'.
	 * @see com.mmxlabs.models.ui.test.model.MultipleContainmentReference
	 * @generated
	 */
	EClass getMultipleContainmentReference();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.ui.test.model.MultipleContainmentReference#getSimpleObjects <em>Simple Objects</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Simple Objects</em>'.
	 * @see com.mmxlabs.models.ui.test.model.MultipleContainmentReference#getSimpleObjects()
	 * @see #getMultipleContainmentReference()
	 * @generated
	 */
	EReference getMultipleContainmentReference_SimpleObjects();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ModelFactory getModelFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.ui.test.model.impl.ModelRootImpl <em>Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.ui.test.model.impl.ModelRootImpl
		 * @see com.mmxlabs.models.ui.test.model.impl.ModelPackageImpl#getModelRoot()
		 * @generated
		 */
		EClass MODEL_ROOT = eINSTANCE.getModelRoot();

		/**
		 * The meta object literal for the '<em><b>Simple Objects</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODEL_ROOT__SIMPLE_OBJECTS = eINSTANCE.getModelRoot_SimpleObjects();

		/**
		 * The meta object literal for the '<em><b>Single References</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODEL_ROOT__SINGLE_REFERENCES = eINSTANCE.getModelRoot_SingleReferences();

		/**
		 * The meta object literal for the '<em><b>Multiple References</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODEL_ROOT__MULTIPLE_REFERENCES = eINSTANCE.getModelRoot_MultipleReferences();

		/**
		 * The meta object literal for the '<em><b>Single Containment References</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODEL_ROOT__SINGLE_CONTAINMENT_REFERENCES = eINSTANCE.getModelRoot_SingleContainmentReferences();

		/**
		 * The meta object literal for the '<em><b>Multiple Containment References</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODEL_ROOT__MULTIPLE_CONTAINMENT_REFERENCES = eINSTANCE.getModelRoot_MultipleContainmentReferences();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.ui.test.model.impl.SimpleObjectImpl <em>Simple Object</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.ui.test.model.impl.SimpleObjectImpl
		 * @see com.mmxlabs.models.ui.test.model.impl.ModelPackageImpl#getSimpleObject()
		 * @generated
		 */
		EClass SIMPLE_OBJECT = eINSTANCE.getSimpleObject();

		/**
		 * The meta object literal for the '<em><b>Attribute</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SIMPLE_OBJECT__ATTRIBUTE = eINSTANCE.getSimpleObject_Attribute();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.ui.test.model.impl.SingleReferenceImpl <em>Single Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.ui.test.model.impl.SingleReferenceImpl
		 * @see com.mmxlabs.models.ui.test.model.impl.ModelPackageImpl#getSingleReference()
		 * @generated
		 */
		EClass SINGLE_REFERENCE = eINSTANCE.getSingleReference();

		/**
		 * The meta object literal for the '<em><b>Multiple Reference</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SINGLE_REFERENCE__MULTIPLE_REFERENCE = eINSTANCE.getSingleReference_MultipleReference();

		/**
		 * The meta object literal for the '<em><b>Model Root</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SINGLE_REFERENCE__MODEL_ROOT = eINSTANCE.getSingleReference_ModelRoot();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.ui.test.model.impl.MultipleReferenceImpl <em>Multiple Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.ui.test.model.impl.MultipleReferenceImpl
		 * @see com.mmxlabs.models.ui.test.model.impl.ModelPackageImpl#getMultipleReference()
		 * @generated
		 */
		EClass MULTIPLE_REFERENCE = eINSTANCE.getMultipleReference();

		/**
		 * The meta object literal for the '<em><b>Single References</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MULTIPLE_REFERENCE__SINGLE_REFERENCES = eINSTANCE.getMultipleReference_SingleReferences();

		/**
		 * The meta object literal for the '<em><b>Model Root</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MULTIPLE_REFERENCE__MODEL_ROOT = eINSTANCE.getMultipleReference_ModelRoot();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.ui.test.model.impl.SingleContainmentReferenceImpl <em>Single Containment Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.ui.test.model.impl.SingleContainmentReferenceImpl
		 * @see com.mmxlabs.models.ui.test.model.impl.ModelPackageImpl#getSingleContainmentReference()
		 * @generated
		 */
		EClass SINGLE_CONTAINMENT_REFERENCE = eINSTANCE.getSingleContainmentReference();

		/**
		 * The meta object literal for the '<em><b>Simple Object</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SINGLE_CONTAINMENT_REFERENCE__SIMPLE_OBJECT = eINSTANCE.getSingleContainmentReference_SimpleObject();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.ui.test.model.impl.MultipleContainmentReferenceImpl <em>Multiple Containment Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.ui.test.model.impl.MultipleContainmentReferenceImpl
		 * @see com.mmxlabs.models.ui.test.model.impl.ModelPackageImpl#getMultipleContainmentReference()
		 * @generated
		 */
		EClass MULTIPLE_CONTAINMENT_REFERENCE = eINSTANCE.getMultipleContainmentReference();

		/**
		 * The meta object literal for the '<em><b>Simple Objects</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MULTIPLE_CONTAINMENT_REFERENCE__SIMPLE_OBJECTS = eINSTANCE.getMultipleContainmentReference_SimpleObjects();

	}

} //ModelPackage
