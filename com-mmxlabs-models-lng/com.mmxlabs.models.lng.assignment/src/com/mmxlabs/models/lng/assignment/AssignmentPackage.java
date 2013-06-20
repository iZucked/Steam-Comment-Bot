/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.assignment;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.mmxcore.MMXCorePackage;

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
 * @see com.mmxlabs.models.lng.assignment.AssignmentFactory
 * @model kind="package"
 * @generated
 */
public interface AssignmentPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "assignment";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.mmxlabs.com/models/lng/assignment/1/";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "lng.assignment";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	AssignmentPackage eINSTANCE = com.mmxlabs.models.lng.assignment.impl.AssignmentPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.assignment.impl.AssignmentModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.assignment.impl.AssignmentModelImpl
	 * @see com.mmxlabs.models.lng.assignment.impl.AssignmentPackageImpl#getAssignmentModel()
	 * @generated
	 */
	int ASSIGNMENT_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSIGNMENT_MODEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSIGNMENT_MODEL__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Element Assignments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSIGNMENT_MODEL__ELEMENT_ASSIGNMENTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSIGNMENT_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.assignment.impl.ElementAssignmentImpl <em>Element Assignment</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.assignment.impl.ElementAssignmentImpl
	 * @see com.mmxlabs.models.lng.assignment.impl.AssignmentPackageImpl#getElementAssignment()
	 * @generated
	 */
	int ELEMENT_ASSIGNMENT = 1;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_ASSIGNMENT__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.assignment.AssignmentModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see com.mmxlabs.models.lng.assignment.AssignmentModel
	 * @generated
	 */
	EClass getAssignmentModel();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.assignment.AssignmentModel#getElementAssignments <em>Element Assignments</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Element Assignments</em>'.
	 * @see com.mmxlabs.models.lng.assignment.AssignmentModel#getElementAssignments()
	 * @see #getAssignmentModel()
	 * @generated
	 */
	EReference getAssignmentModel_ElementAssignments();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.assignment.ElementAssignment <em>Element Assignment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Element Assignment</em>'.
	 * @see com.mmxlabs.models.lng.assignment.ElementAssignment
	 * @generated
	 */
	EClass getElementAssignment();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.assignment.ElementAssignment#getAssignedObject <em>Assigned Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Assigned Object</em>'.
	 * @see com.mmxlabs.models.lng.assignment.ElementAssignment#getAssignedObject()
	 * @see #getElementAssignment()
	 * @generated
	 */
	EReference getElementAssignment_AssignedObject();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.assignment.ElementAssignment#getAssignment <em>Assignment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Assignment</em>'.
	 * @see com.mmxlabs.models.lng.assignment.ElementAssignment#getAssignment()
	 * @see #getElementAssignment()
	 * @generated
	 */
	EReference getElementAssignment_Assignment();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.assignment.ElementAssignment#isLocked <em>Locked</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Locked</em>'.
	 * @see com.mmxlabs.models.lng.assignment.ElementAssignment#isLocked()
	 * @see #getElementAssignment()
	 * @generated
	 */
	EAttribute getElementAssignment_Locked();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.assignment.ElementAssignment#getSequence <em>Sequence</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Sequence</em>'.
	 * @see com.mmxlabs.models.lng.assignment.ElementAssignment#getSequence()
	 * @see #getElementAssignment()
	 * @generated
	 */
	EAttribute getElementAssignment_Sequence();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.assignment.ElementAssignment#getSpotIndex <em>Spot Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Spot Index</em>'.
	 * @see com.mmxlabs.models.lng.assignment.ElementAssignment#getSpotIndex()
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
	AssignmentFactory getAssignmentFactory();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.assignment.impl.AssignmentModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.assignment.impl.AssignmentModelImpl
		 * @see com.mmxlabs.models.lng.assignment.impl.AssignmentPackageImpl#getAssignmentModel()
		 * @generated
		 */
		EClass ASSIGNMENT_MODEL = eINSTANCE.getAssignmentModel();

		/**
		 * The meta object literal for the '<em><b>Element Assignments</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSIGNMENT_MODEL__ELEMENT_ASSIGNMENTS = eINSTANCE.getAssignmentModel_ElementAssignments();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.assignment.impl.ElementAssignmentImpl <em>Element Assignment</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.assignment.impl.ElementAssignmentImpl
		 * @see com.mmxlabs.models.lng.assignment.impl.AssignmentPackageImpl#getElementAssignment()
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

} //AssignmentPackage
