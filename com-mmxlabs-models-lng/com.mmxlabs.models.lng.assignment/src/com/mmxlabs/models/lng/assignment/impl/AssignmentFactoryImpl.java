/**
 */
package com.mmxlabs.models.lng.assignment.impl;

import com.mmxlabs.models.lng.assignment.*;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import com.mmxlabs.models.lng.assignment.AssignmentFactory;
import com.mmxlabs.models.lng.assignment.AssignmentModel;
import com.mmxlabs.models.lng.assignment.AssignmentPackage;
import com.mmxlabs.models.lng.assignment.ElementAssignment;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class AssignmentFactoryImpl extends EFactoryImpl implements AssignmentFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static AssignmentFactory init() {
		try {
			AssignmentFactory theAssignmentFactory = (AssignmentFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.mmxlabs.com/models/lng/assignment/1/"); 
			if (theAssignmentFactory != null) {
				return theAssignmentFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new AssignmentFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AssignmentFactoryImpl() {
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
			case AssignmentPackage.ASSIGNMENT_MODEL: return createAssignmentModel();
			case AssignmentPackage.ELEMENT_ASSIGNMENT: return createElementAssignment();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AssignmentModel createAssignmentModel() {
		AssignmentModelImpl assignmentModel = new AssignmentModelImpl();
		return assignmentModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ElementAssignment createElementAssignment() {
		ElementAssignmentImpl elementAssignment = new ElementAssignmentImpl();
		return elementAssignment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AssignmentPackage getAssignmentPackage() {
		return (AssignmentPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static AssignmentPackage getPackage() {
		return AssignmentPackage.eINSTANCE;
	}

} //AssignmentFactoryImpl
