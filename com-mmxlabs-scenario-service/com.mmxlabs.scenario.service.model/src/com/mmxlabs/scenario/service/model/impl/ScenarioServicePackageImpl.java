/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.impl;

import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ParamSet;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioModel;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;
import com.mmxlabs.scenario.service.model.Solution;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ScenarioServicePackageImpl extends EPackageImpl implements ScenarioServicePackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass scenarioInstanceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass scenarioModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass paramSetEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass solutionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass scenarioServiceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass metadataEClass = null;

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
	 * @see com.mmxlabs.scenario.service.model.ScenarioServicePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ScenarioServicePackageImpl() {
		super(eNS_URI, ScenarioServiceFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link ScenarioServicePackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static ScenarioServicePackage init() {
		if (isInited)
			return (ScenarioServicePackage) EPackage.Registry.INSTANCE.getEPackage(ScenarioServicePackage.eNS_URI);

		// Obtain or create and register package
		ScenarioServicePackageImpl theScenarioServicePackage = (ScenarioServicePackageImpl) (EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ScenarioServicePackageImpl ? EPackage.Registry.INSTANCE
				.get(eNS_URI) : new ScenarioServicePackageImpl());

		isInited = true;

		// Create package meta-data objects
		theScenarioServicePackage.createPackageContents();

		// Initialize created meta-data
		theScenarioServicePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theScenarioServicePackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(ScenarioServicePackage.eNS_URI, theScenarioServicePackage);
		return theScenarioServicePackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getScenarioInstance() {
		return scenarioInstanceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getScenarioInstance_Variations() {
		return (EReference) scenarioInstanceEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getScenarioInstance_ParameterSets() {
		return (EReference) scenarioInstanceEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getScenarioInstance_Instance() {
		return (EReference) scenarioInstanceEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getScenarioInstance_Controller() {
		return (EReference) scenarioInstanceEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getScenarioInstance_Adapters() {
		return (EAttribute) scenarioInstanceEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getScenarioInstance_Uri() {
		return (EAttribute) scenarioInstanceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getScenarioInstance_Name() {
		return (EAttribute) scenarioInstanceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getScenarioInstance_InitialSolution() {
		return (EReference) scenarioInstanceEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getScenarioInstance_Uuid() {
		return (EAttribute) scenarioInstanceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getScenarioInstance_Locked() {
		return (EAttribute) scenarioInstanceEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getScenarioInstance_Archived() {
		return (EAttribute) scenarioInstanceEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getScenarioInstance_Metadata() {
		return (EReference) scenarioInstanceEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getScenarioModel() {
		return scenarioModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getScenarioModel_ScenarioServices() {
		return (EReference) scenarioModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getParamSet() {
		return paramSetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getParamSet_Solutions() {
		return (EReference) paramSetEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParamSet_Uri() {
		return (EAttribute) paramSetEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParamSet_Name() {
		return (EAttribute) paramSetEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParamSet_Uuid() {
		return (EAttribute) paramSetEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParamSet_Archived() {
		return (EAttribute) paramSetEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getParamSet_Metadata() {
		return (EReference) paramSetEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSolution() {
		return solutionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSolution_Uri() {
		return (EAttribute) solutionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSolution_Name() {
		return (EAttribute) solutionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSolution_Uuid() {
		return (EAttribute) solutionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSolution_Archived() {
		return (EAttribute) solutionEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSolution_ParamSet() {
		return (EReference) solutionEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSolution_Metadata() {
		return (EReference) solutionEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getScenarioService() {
		return scenarioServiceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getScenarioService_Name() {
		return (EAttribute) scenarioServiceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getScenarioService_Scenarios() {
		return (EReference) scenarioServiceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getScenarioService_Description() {
		return (EAttribute) scenarioServiceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMetadata() {
		return metadataEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMetadata_Creator() {
		return (EAttribute) metadataEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMetadata_Created() {
		return (EAttribute) metadataEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMetadata_LastModified() {
		return (EAttribute) metadataEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMetadata_Comment() {
		return (EAttribute) metadataEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMetadata_LastModifiedBy() {
		return (EAttribute) metadataEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMetadata_ContentType() {
		return (EAttribute) metadataEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenarioServiceFactory getScenarioServiceFactory() {
		return (ScenarioServiceFactory) getEFactoryInstance();
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
		if (isCreated)
			return;
		isCreated = true;

		// Create classes and their features
		scenarioModelEClass = createEClass(SCENARIO_MODEL);
		createEReference(scenarioModelEClass, SCENARIO_MODEL__SCENARIO_SERVICES);

		scenarioServiceEClass = createEClass(SCENARIO_SERVICE);
		createEAttribute(scenarioServiceEClass, SCENARIO_SERVICE__NAME);
		createEAttribute(scenarioServiceEClass, SCENARIO_SERVICE__DESCRIPTION);
		createEReference(scenarioServiceEClass, SCENARIO_SERVICE__SCENARIOS);

		scenarioInstanceEClass = createEClass(SCENARIO_INSTANCE);
		createEAttribute(scenarioInstanceEClass, SCENARIO_INSTANCE__NAME);
		createEAttribute(scenarioInstanceEClass, SCENARIO_INSTANCE__UUID);
		createEAttribute(scenarioInstanceEClass, SCENARIO_INSTANCE__URI);
		createEAttribute(scenarioInstanceEClass, SCENARIO_INSTANCE__LOCKED);
		createEReference(scenarioInstanceEClass, SCENARIO_INSTANCE__METADATA);
		createEAttribute(scenarioInstanceEClass, SCENARIO_INSTANCE__ARCHIVED);
		createEReference(scenarioInstanceEClass, SCENARIO_INSTANCE__INITIAL_SOLUTION);
		createEReference(scenarioInstanceEClass, SCENARIO_INSTANCE__VARIATIONS);
		createEReference(scenarioInstanceEClass, SCENARIO_INSTANCE__PARAMETER_SETS);
		createEReference(scenarioInstanceEClass, SCENARIO_INSTANCE__INSTANCE);
		createEReference(scenarioInstanceEClass, SCENARIO_INSTANCE__CONTROLLER);
		createEAttribute(scenarioInstanceEClass, SCENARIO_INSTANCE__ADAPTERS);

		paramSetEClass = createEClass(PARAM_SET);
		createEAttribute(paramSetEClass, PARAM_SET__NAME);
		createEAttribute(paramSetEClass, PARAM_SET__UUID);
		createEAttribute(paramSetEClass, PARAM_SET__URI);
		createEReference(paramSetEClass, PARAM_SET__METADATA);
		createEAttribute(paramSetEClass, PARAM_SET__ARCHIVED);
		createEReference(paramSetEClass, PARAM_SET__SOLUTIONS);

		solutionEClass = createEClass(SOLUTION);
		createEAttribute(solutionEClass, SOLUTION__NAME);
		createEAttribute(solutionEClass, SOLUTION__UUID);
		createEAttribute(solutionEClass, SOLUTION__URI);
		createEReference(solutionEClass, SOLUTION__METADATA);
		createEAttribute(solutionEClass, SOLUTION__ARCHIVED);
		createEReference(solutionEClass, SOLUTION__PARAM_SET);

		metadataEClass = createEClass(METADATA);
		createEAttribute(metadataEClass, METADATA__CREATOR);
		createEAttribute(metadataEClass, METADATA__CREATED);
		createEAttribute(metadataEClass, METADATA__LAST_MODIFIED);
		createEAttribute(metadataEClass, METADATA__COMMENT);
		createEAttribute(metadataEClass, METADATA__LAST_MODIFIED_BY);
		createEAttribute(metadataEClass, METADATA__CONTENT_TYPE);
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
		if (isInitialized)
			return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes and features; add operations and parameters
		initEClass(scenarioModelEClass, ScenarioModel.class, "ScenarioModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getScenarioModel_ScenarioServices(), this.getScenarioService(), null, "scenarioServices", null, 0, -1, ScenarioModel.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(scenarioServiceEClass, ScenarioService.class, "ScenarioService", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getScenarioService_Name(), ecorePackage.getEString(), "name", null, 0, 1, ScenarioService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getScenarioService_Description(), ecorePackage.getEString(), "description", null, 0, 1, ScenarioService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
				!IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getScenarioService_Scenarios(), this.getScenarioInstance(), null, "scenarios", null, 0, -1, ScenarioService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(scenarioInstanceEClass, ScenarioInstance.class, "ScenarioInstance", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getScenarioInstance_Name(), ecorePackage.getEString(), "name", null, 0, 1, ScenarioInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScenarioInstance_Uuid(), ecorePackage.getEString(), "uuid", null, 1, 1, ScenarioInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getScenarioInstance_Uri(), ecorePackage.getEString(), "uri", null, 1, 1, ScenarioInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getScenarioInstance_Locked(), ecorePackage.getEBoolean(), "locked", null, 0, 1, ScenarioInstance.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getScenarioInstance_Metadata(), this.getMetadata(), null, "metadata", null, 0, 1, ScenarioInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScenarioInstance_Archived(), ecorePackage.getEBoolean(), "archived", null, 0, 1, ScenarioInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getScenarioInstance_InitialSolution(), this.getSolution(), null, "initialSolution", null, 0, 1, ScenarioInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getScenarioInstance_Variations(), this.getScenarioInstance(), null, "variations", null, 0, -1, ScenarioInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getScenarioInstance_ParameterSets(), this.getParamSet(), null, "parameterSets", null, 0, -1, ScenarioInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getScenarioInstance_Instance(), ecorePackage.getEObject(), null, "instance", null, 0, 1, ScenarioInstance.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
				IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getScenarioInstance_Controller(), ecorePackage.getEObject(), null, "controller", null, 0, 1, ScenarioInstance.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
				IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		EGenericType g1 = createEGenericType(ecorePackage.getEMap());
		EGenericType g2 = createEGenericType();
		g1.getETypeArguments().add(g2);
		g2 = createEGenericType();
		g1.getETypeArguments().add(g2);
		initEAttribute(getScenarioInstance_Adapters(), g1, "adapters", null, 0, 1, ScenarioInstance.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);

		initEClass(paramSetEClass, ParamSet.class, "ParamSet", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getParamSet_Name(), ecorePackage.getEString(), "name", null, 0, 1, ParamSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEAttribute(getParamSet_Uuid(), ecorePackage.getEString(), "uuid", null, 1, 1, ParamSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEAttribute(getParamSet_Uri(), ecorePackage.getEString(), "uri", null, 1, 1, ParamSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEReference(getParamSet_Metadata(), this.getMetadata(), null, "metadata", null, 0, 1, ParamSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParamSet_Archived(), ecorePackage.getEBoolean(), "archived", null, 0, 1, ParamSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEReference(getParamSet_Solutions(), this.getSolution(), this.getSolution_ParamSet(), "solutions", null, 0, -1, ParamSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(solutionEClass, Solution.class, "Solution", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSolution_Name(), ecorePackage.getEString(), "name", null, 0, 1, Solution.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEAttribute(getSolution_Uuid(), ecorePackage.getEString(), "uuid", null, 1, 1, Solution.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEAttribute(getSolution_Uri(), ecorePackage.getEString(), "uri", null, 1, 1, Solution.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEReference(getSolution_Metadata(), this.getMetadata(), null, "metadata", null, 0, 1, Solution.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSolution_Archived(), ecorePackage.getEBoolean(), "archived", null, 0, 1, Solution.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEReference(getSolution_ParamSet(), this.getParamSet(), this.getParamSet_Solutions(), "paramSet", null, 0, 1, Solution.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(metadataEClass, Metadata.class, "Metadata", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMetadata_Creator(), ecorePackage.getEString(), "creator", null, 1, 1, Metadata.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getMetadata_Created(), ecorePackage.getEDate(), "created", null, 1, 1, Metadata.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getMetadata_LastModified(), ecorePackage.getEDate(), "lastModified", null, 0, 1, Metadata.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getMetadata_Comment(), ecorePackage.getEString(), "comment", null, 0, 1, Metadata.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getMetadata_LastModifiedBy(), ecorePackage.getEString(), "lastModifiedBy", null, 0, 1, Metadata.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMetadata_ContentType(), ecorePackage.getEString(), "contentType", null, 0, 1, Metadata.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //ScenarioServicePackageImpl
