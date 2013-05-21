/**
 */
package com.mmxlabs.models.lng.assignment.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.mmxlabs.models.lng.assignment.AssignmentFactory;
import com.mmxlabs.models.lng.assignment.AssignmentModel;
import com.mmxlabs.models.lng.assignment.AssignmentPackage;
import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class AssignmentPackageImpl extends EPackageImpl implements AssignmentPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass assignmentModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass elementAssignmentEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see com.mmxlabs.models.lng.assignment.AssignmentPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private AssignmentPackageImpl() {
		super(eNS_URI, AssignmentFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link AssignmentPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static AssignmentPackage init() {
		if (isInited) return (AssignmentPackage)EPackage.Registry.INSTANCE.getEPackage(AssignmentPackage.eNS_URI);

		// Obtain or create and register package
		AssignmentPackageImpl theAssignmentPackage = (AssignmentPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof AssignmentPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new AssignmentPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		FleetPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theAssignmentPackage.createPackageContents();

		// Initialize created meta-data
		theAssignmentPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theAssignmentPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(AssignmentPackage.eNS_URI, theAssignmentPackage);
		return theAssignmentPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAssignmentModel() {
		return assignmentModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAssignmentModel_ElementAssignments() {
		return (EReference)assignmentModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getElementAssignment() {
		return elementAssignmentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getElementAssignment_AssignedObject() {
		return (EReference)elementAssignmentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getElementAssignment_Assignment() {
		return (EReference)elementAssignmentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getElementAssignment_Locked() {
		return (EAttribute)elementAssignmentEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getElementAssignment_Sequence() {
		return (EAttribute)elementAssignmentEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getElementAssignment_SpotIndex() {
		return (EAttribute)elementAssignmentEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AssignmentFactory getAssignmentFactory() {
		return (AssignmentFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		assignmentModelEClass = createEClass(ASSIGNMENT_MODEL);
		createEReference(assignmentModelEClass, ASSIGNMENT_MODEL__ELEMENT_ASSIGNMENTS);

		elementAssignmentEClass = createEClass(ELEMENT_ASSIGNMENT);
		createEReference(elementAssignmentEClass, ELEMENT_ASSIGNMENT__ASSIGNED_OBJECT);
		createEReference(elementAssignmentEClass, ELEMENT_ASSIGNMENT__ASSIGNMENT);
		createEAttribute(elementAssignmentEClass, ELEMENT_ASSIGNMENT__LOCKED);
		createEAttribute(elementAssignmentEClass, ELEMENT_ASSIGNMENT__SEQUENCE);
		createEAttribute(elementAssignmentEClass, ELEMENT_ASSIGNMENT__SPOT_INDEX);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		MMXCorePackage theMMXCorePackage = (MMXCorePackage)EPackage.Registry.INSTANCE.getEPackage(MMXCorePackage.eNS_URI);
		TypesPackage theTypesPackage = (TypesPackage)EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);
		FleetPackage theFleetPackage = (FleetPackage)EPackage.Registry.INSTANCE.getEPackage(FleetPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		assignmentModelEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		elementAssignmentEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());

		// Initialize classes and features; add operations and parameters
		initEClass(assignmentModelEClass, AssignmentModel.class, "AssignmentModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAssignmentModel_ElementAssignments(), this.getElementAssignment(), null, "elementAssignments", null, 0, -1, AssignmentModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(elementAssignmentEClass, ElementAssignment.class, "ElementAssignment", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getElementAssignment_AssignedObject(), theMMXCorePackage.getUUIDObject(), null, "assignedObject", null, 1, 1, ElementAssignment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		EGenericType g1 = createEGenericType(theTypesPackage.getAVesselSet());
		EGenericType g2 = createEGenericType(theFleetPackage.getVessel());
		g1.getETypeArguments().add(g2);
		initEReference(getElementAssignment_Assignment(), g1, null, "assignment", null, 0, 1, ElementAssignment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getElementAssignment_Locked(), ecorePackage.getEBoolean(), "locked", null, 1, 1, ElementAssignment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getElementAssignment_Sequence(), ecorePackage.getEInt(), "sequence", null, 1, 1, ElementAssignment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getElementAssignment_SpotIndex(), ecorePackage.getEInt(), "spotIndex", null, 1, 1, ElementAssignment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //AssignmentPackageImpl
