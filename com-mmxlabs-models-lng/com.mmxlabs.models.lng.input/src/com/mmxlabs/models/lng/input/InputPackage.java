/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.input;

import com.mmxlabs.models.mmxcore.MMXCorePackage;

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
 * @see com.mmxlabs.models.lng.input.InputFactory
 * @model kind="package"
 * @generated
 */
public interface InputPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "input";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.mmxlabs.com/models/lng/input/1/";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "lng.input";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	InputPackage eINSTANCE = com.mmxlabs.models.lng.input.impl.InputPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.input.impl.InputModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.input.impl.InputModelImpl
	 * @see com.mmxlabs.models.lng.input.impl.InputPackageImpl#getInputModel()
	 * @generated
	 */
	int INPUT_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_MODEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_MODEL__PROXIES = MMXCorePackage.UUID_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_MODEL__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Assignments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_MODEL__ASSIGNMENTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Locked Assigned Objects</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_MODEL__LOCKED_ASSIGNED_OBJECTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Element Assignments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_MODEL__ELEMENT_ASSIGNMENTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.input.impl.AssignmentImpl <em>Assignment</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.input.impl.AssignmentImpl
	 * @see com.mmxlabs.models.lng.input.impl.InputPackageImpl#getAssignment()
	 * @generated
	 */
	int ASSIGNMENT = 1;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSIGNMENT__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSIGNMENT__PROXIES = MMXCorePackage.MMX_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Vessels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSIGNMENT__VESSELS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Assign To Spot</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSIGNMENT__ASSIGN_TO_SPOT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Assigned Objects</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSIGNMENT__ASSIGNED_OBJECTS = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Assignment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSIGNMENT_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;


	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.input.impl.ElementAssignmentImpl <em>Element Assignment</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.input.impl.ElementAssignmentImpl
	 * @see com.mmxlabs.models.lng.input.impl.InputPackageImpl#getElementAssignment()
	 * @generated
	 */
	int ELEMENT_ASSIGNMENT = 2;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_ASSIGNMENT__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_ASSIGNMENT__PROXIES = MMXCorePackage.MMX_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Assigned Object</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_ASSIGNMENT__ASSIGNED_OBJECT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Assignment</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_ASSIGNMENT__ASSIGNMENT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Locked</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_ASSIGNMENT__LOCKED = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Sequence</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_ASSIGNMENT__SEQUENCE = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Spot Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_ASSIGNMENT__SPOT_INDEX = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Element Assignment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_ASSIGNMENT_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 5;


	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.input.InputModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see com.mmxlabs.models.lng.input.InputModel
	 * @generated
	 */
	EClass getInputModel();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.input.InputModel#getAssignments <em>Assignments</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Assignments</em>'.
	 * @see com.mmxlabs.models.lng.input.InputModel#getAssignments()
	 * @see #getInputModel()
	 * @generated
	 */
	EReference getInputModel_Assignments();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.input.InputModel#getLockedAssignedObjects <em>Locked Assigned Objects</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Locked Assigned Objects</em>'.
	 * @see com.mmxlabs.models.lng.input.InputModel#getLockedAssignedObjects()
	 * @see #getInputModel()
	 * @generated
	 */
	EReference getInputModel_LockedAssignedObjects();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.input.InputModel#getElementAssignments <em>Element Assignments</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Element Assignments</em>'.
	 * @see com.mmxlabs.models.lng.input.InputModel#getElementAssignments()
	 * @see #getInputModel()
	 * @generated
	 */
	EReference getInputModel_ElementAssignments();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.input.Assignment <em>Assignment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Assignment</em>'.
	 * @see com.mmxlabs.models.lng.input.Assignment
	 * @generated
	 */
	EClass getAssignment();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.input.Assignment#getVessels <em>Vessels</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Vessels</em>'.
	 * @see com.mmxlabs.models.lng.input.Assignment#getVessels()
	 * @see #getAssignment()
	 * @generated
	 */
	EReference getAssignment_Vessels();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.input.Assignment#isAssignToSpot <em>Assign To Spot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Assign To Spot</em>'.
	 * @see com.mmxlabs.models.lng.input.Assignment#isAssignToSpot()
	 * @see #getAssignment()
	 * @generated
	 */
	EAttribute getAssignment_AssignToSpot();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.input.Assignment#getAssignedObjects <em>Assigned Objects</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Assigned Objects</em>'.
	 * @see com.mmxlabs.models.lng.input.Assignment#getAssignedObjects()
	 * @see #getAssignment()
	 * @generated
	 */
	EReference getAssignment_AssignedObjects();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.input.ElementAssignment <em>Element Assignment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Element Assignment</em>'.
	 * @see com.mmxlabs.models.lng.input.ElementAssignment
	 * @generated
	 */
	EClass getElementAssignment();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.input.ElementAssignment#getAssignedObject <em>Assigned Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Assigned Object</em>'.
	 * @see com.mmxlabs.models.lng.input.ElementAssignment#getAssignedObject()
	 * @see #getElementAssignment()
	 * @generated
	 */
	EReference getElementAssignment_AssignedObject();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.input.ElementAssignment#getAssignment <em>Assignment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Assignment</em>'.
	 * @see com.mmxlabs.models.lng.input.ElementAssignment#getAssignment()
	 * @see #getElementAssignment()
	 * @generated
	 */
	EReference getElementAssignment_Assignment();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.input.ElementAssignment#isLocked <em>Locked</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Locked</em>'.
	 * @see com.mmxlabs.models.lng.input.ElementAssignment#isLocked()
	 * @see #getElementAssignment()
	 * @generated
	 */
	EAttribute getElementAssignment_Locked();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.input.ElementAssignment#getSequence <em>Sequence</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Sequence</em>'.
	 * @see com.mmxlabs.models.lng.input.ElementAssignment#getSequence()
	 * @see #getElementAssignment()
	 * @generated
	 */
	EAttribute getElementAssignment_Sequence();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.input.ElementAssignment#getSpotIndex <em>Spot Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Spot Index</em>'.
	 * @see com.mmxlabs.models.lng.input.ElementAssignment#getSpotIndex()
	 * @see #getElementAssignment()
	 * @generated
	 */
	EAttribute getElementAssignment_SpotIndex();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	InputFactory getInputFactory();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.input.impl.InputModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.input.impl.InputModelImpl
		 * @see com.mmxlabs.models.lng.input.impl.InputPackageImpl#getInputModel()
		 * @generated
		 */
		EClass INPUT_MODEL = eINSTANCE.getInputModel();

		/**
		 * The meta object literal for the '<em><b>Assignments</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INPUT_MODEL__ASSIGNMENTS = eINSTANCE.getInputModel_Assignments();

		/**
		 * The meta object literal for the '<em><b>Locked Assigned Objects</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INPUT_MODEL__LOCKED_ASSIGNED_OBJECTS = eINSTANCE.getInputModel_LockedAssignedObjects();

		/**
		 * The meta object literal for the '<em><b>Element Assignments</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INPUT_MODEL__ELEMENT_ASSIGNMENTS = eINSTANCE.getInputModel_ElementAssignments();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.input.impl.AssignmentImpl <em>Assignment</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.input.impl.AssignmentImpl
		 * @see com.mmxlabs.models.lng.input.impl.InputPackageImpl#getAssignment()
		 * @generated
		 */
		EClass ASSIGNMENT = eINSTANCE.getAssignment();

		/**
		 * The meta object literal for the '<em><b>Vessels</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSIGNMENT__VESSELS = eINSTANCE.getAssignment_Vessels();

		/**
		 * The meta object literal for the '<em><b>Assign To Spot</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ASSIGNMENT__ASSIGN_TO_SPOT = eINSTANCE.getAssignment_AssignToSpot();

		/**
		 * The meta object literal for the '<em><b>Assigned Objects</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSIGNMENT__ASSIGNED_OBJECTS = eINSTANCE.getAssignment_AssignedObjects();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.input.impl.ElementAssignmentImpl <em>Element Assignment</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.input.impl.ElementAssignmentImpl
		 * @see com.mmxlabs.models.lng.input.impl.InputPackageImpl#getElementAssignment()
		 * @generated
		 */
		EClass ELEMENT_ASSIGNMENT = eINSTANCE.getElementAssignment();

		/**
		 * The meta object literal for the '<em><b>Assigned Object</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ELEMENT_ASSIGNMENT__ASSIGNED_OBJECT = eINSTANCE.getElementAssignment_AssignedObject();

		/**
		 * The meta object literal for the '<em><b>Assignment</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ELEMENT_ASSIGNMENT__ASSIGNMENT = eINSTANCE.getElementAssignment_Assignment();

		/**
		 * The meta object literal for the '<em><b>Locked</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ELEMENT_ASSIGNMENT__LOCKED = eINSTANCE.getElementAssignment_Locked();

		/**
		 * The meta object literal for the '<em><b>Sequence</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ELEMENT_ASSIGNMENT__SEQUENCE = eINSTANCE.getElementAssignment_Sequence();

		/**
		 * The meta object literal for the '<em><b>Spot Index</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ELEMENT_ASSIGNMENT__SPOT_INDEX = eINSTANCE.getElementAssignment_SpotIndex();

	}

} //InputPackage
