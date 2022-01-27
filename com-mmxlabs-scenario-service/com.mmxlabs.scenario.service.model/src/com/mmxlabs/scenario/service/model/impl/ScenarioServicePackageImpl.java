/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.manifest.ManifestPackage;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioFragment;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioModel;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

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
	private EClass scenarioModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass containerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass folderEClass = null;

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
	private EClass scenarioInstanceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass metadataEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass scenarioFragmentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType iScenarioServiceEDataType = null;

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
		Object registeredScenarioServicePackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		ScenarioServicePackageImpl theScenarioServicePackage = registeredScenarioServicePackage instanceof ScenarioServicePackageImpl ? (ScenarioServicePackageImpl) registeredScenarioServicePackage
				: new ScenarioServicePackageImpl();

		isInited = true;

		// Initialize simple dependencies
		ManifestPackage.eINSTANCE.eClass();

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
	@Override
	public EClass getScenarioModel() {
		return scenarioModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getScenarioModel_ScenarioServices() {
		return (EReference) scenarioModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getContainer() {
		return containerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getContainer_Parent() {
		return (EReference) containerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getContainer_Elements() {
		return (EReference) containerEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContainer_Archived() {
		return (EAttribute) containerEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContainer_Name() {
		return (EAttribute) containerEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContainer_Hidden() {
		return (EAttribute) containerEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getFolder() {
		return folderEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getFolder_Metadata() {
		return (EReference) folderEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getFolder_Managed() {
		return (EAttribute) folderEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getScenarioService() {
		return scenarioServiceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getScenarioService_Description() {
		return (EAttribute) scenarioServiceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getScenarioService_ServiceRef() {
		return (EAttribute) scenarioServiceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getScenarioService_SupportsForking() {
		return (EAttribute) scenarioServiceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getScenarioService_SupportsImport() {
		return (EAttribute) scenarioServiceEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getScenarioService_ScenarioModel() {
		return (EReference) scenarioServiceEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getScenarioService_Local() {
		return (EAttribute) scenarioServiceEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getScenarioService_ServiceID() {
		return (EAttribute) scenarioServiceEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getScenarioService_Offline() {
		return (EAttribute) scenarioServiceEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getScenarioService_LockedBy() {
		return (EAttribute) scenarioServiceEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getScenarioService_Locked() {
		return (EAttribute) scenarioServiceEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getScenarioService_Image() {
		return (EAttribute) scenarioServiceEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getScenarioInstance() {
		return scenarioInstanceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getScenarioInstance_Uuid() {
		return (EAttribute) scenarioInstanceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getScenarioInstance_Metadata() {
		return (EReference) scenarioInstanceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getScenarioInstance_RootObjectURI() {
		return (EAttribute) scenarioInstanceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getScenarioInstance_ScenarioVersion() {
		return (EAttribute) scenarioInstanceEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getScenarioInstance_VersionContext() {
		return (EAttribute) scenarioInstanceEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getScenarioInstance_Fragments() {
		return (EReference) scenarioInstanceEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getScenarioInstance_Readonly() {
		return (EAttribute) scenarioInstanceEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getScenarioInstance_CloudLocked() {
		return (EAttribute) scenarioInstanceEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getScenarioInstance_ValidationStatusCode() {
		return (EAttribute) scenarioInstanceEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getScenarioInstance_Manifest() {
		return (EReference) scenarioInstanceEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getScenarioInstance_ExternalID() {
		return (EAttribute) scenarioInstanceEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getScenarioInstance_ClientScenarioVersion() {
		return (EAttribute) scenarioInstanceEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getScenarioInstance_ClientVersionContext() {
		return (EAttribute) scenarioInstanceEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMetadata() {
		return metadataEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMetadata_Creator() {
		return (EAttribute) metadataEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMetadata_Created() {
		return (EAttribute) metadataEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMetadata_LastModified() {
		return (EAttribute) metadataEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMetadata_Comment() {
		return (EAttribute) metadataEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMetadata_LastModifiedBy() {
		return (EAttribute) metadataEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMetadata_ContentType() {
		return (EAttribute) metadataEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getScenarioFragment() {
		return scenarioFragmentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getScenarioFragment_ScenarioInstance() {
		return (EReference) scenarioFragmentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getScenarioFragment_Name() {
		return (EAttribute) scenarioFragmentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getScenarioFragment_Fragment() {
		return (EReference) scenarioFragmentEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getScenarioFragment_ContentType() {
		return (EAttribute) scenarioFragmentEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getScenarioFragment_UseCommandStack() {
		return (EAttribute) scenarioFragmentEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getScenarioFragment_TypeHint() {
		return (EAttribute) scenarioFragmentEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getIScenarioService() {
		return iScenarioServiceEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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

		containerEClass = createEClass(CONTAINER);
		createEReference(containerEClass, CONTAINER__PARENT);
		createEReference(containerEClass, CONTAINER__ELEMENTS);
		createEAttribute(containerEClass, CONTAINER__ARCHIVED);
		createEAttribute(containerEClass, CONTAINER__NAME);
		createEAttribute(containerEClass, CONTAINER__HIDDEN);

		folderEClass = createEClass(FOLDER);
		createEReference(folderEClass, FOLDER__METADATA);
		createEAttribute(folderEClass, FOLDER__MANAGED);

		scenarioServiceEClass = createEClass(SCENARIO_SERVICE);
		createEAttribute(scenarioServiceEClass, SCENARIO_SERVICE__DESCRIPTION);
		createEAttribute(scenarioServiceEClass, SCENARIO_SERVICE__SERVICE_REF);
		createEAttribute(scenarioServiceEClass, SCENARIO_SERVICE__SUPPORTS_FORKING);
		createEAttribute(scenarioServiceEClass, SCENARIO_SERVICE__SUPPORTS_IMPORT);
		createEReference(scenarioServiceEClass, SCENARIO_SERVICE__SCENARIO_MODEL);
		createEAttribute(scenarioServiceEClass, SCENARIO_SERVICE__LOCAL);
		createEAttribute(scenarioServiceEClass, SCENARIO_SERVICE__SERVICE_ID);
		createEAttribute(scenarioServiceEClass, SCENARIO_SERVICE__OFFLINE);
		createEAttribute(scenarioServiceEClass, SCENARIO_SERVICE__LOCKED_BY);
		createEAttribute(scenarioServiceEClass, SCENARIO_SERVICE__LOCKED);
		createEAttribute(scenarioServiceEClass, SCENARIO_SERVICE__IMAGE);

		scenarioInstanceEClass = createEClass(SCENARIO_INSTANCE);
		createEAttribute(scenarioInstanceEClass, SCENARIO_INSTANCE__UUID);
		createEAttribute(scenarioInstanceEClass, SCENARIO_INSTANCE__ROOT_OBJECT_URI);
		createEReference(scenarioInstanceEClass, SCENARIO_INSTANCE__METADATA);
		createEAttribute(scenarioInstanceEClass, SCENARIO_INSTANCE__SCENARIO_VERSION);
		createEAttribute(scenarioInstanceEClass, SCENARIO_INSTANCE__VERSION_CONTEXT);
		createEAttribute(scenarioInstanceEClass, SCENARIO_INSTANCE__CLIENT_SCENARIO_VERSION);
		createEAttribute(scenarioInstanceEClass, SCENARIO_INSTANCE__CLIENT_VERSION_CONTEXT);
		createEReference(scenarioInstanceEClass, SCENARIO_INSTANCE__FRAGMENTS);
		createEAttribute(scenarioInstanceEClass, SCENARIO_INSTANCE__READONLY);
		createEAttribute(scenarioInstanceEClass, SCENARIO_INSTANCE__CLOUD_LOCKED);
		createEAttribute(scenarioInstanceEClass, SCENARIO_INSTANCE__VALIDATION_STATUS_CODE);
		createEReference(scenarioInstanceEClass, SCENARIO_INSTANCE__MANIFEST);
		createEAttribute(scenarioInstanceEClass, SCENARIO_INSTANCE__EXTERNAL_ID);

		metadataEClass = createEClass(METADATA);
		createEAttribute(metadataEClass, METADATA__CREATOR);
		createEAttribute(metadataEClass, METADATA__CREATED);
		createEAttribute(metadataEClass, METADATA__LAST_MODIFIED);
		createEAttribute(metadataEClass, METADATA__COMMENT);
		createEAttribute(metadataEClass, METADATA__LAST_MODIFIED_BY);
		createEAttribute(metadataEClass, METADATA__CONTENT_TYPE);

		scenarioFragmentEClass = createEClass(SCENARIO_FRAGMENT);
		createEReference(scenarioFragmentEClass, SCENARIO_FRAGMENT__SCENARIO_INSTANCE);
		createEAttribute(scenarioFragmentEClass, SCENARIO_FRAGMENT__NAME);
		createEReference(scenarioFragmentEClass, SCENARIO_FRAGMENT__FRAGMENT);
		createEAttribute(scenarioFragmentEClass, SCENARIO_FRAGMENT__CONTENT_TYPE);
		createEAttribute(scenarioFragmentEClass, SCENARIO_FRAGMENT__USE_COMMAND_STACK);
		createEAttribute(scenarioFragmentEClass, SCENARIO_FRAGMENT__TYPE_HINT);

		// Create data types
		iScenarioServiceEDataType = createEDataType(ISCENARIO_SERVICE);
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

		// Obtain other dependent packages
		ManifestPackage theManifestPackage = (ManifestPackage) EPackage.Registry.INSTANCE.getEPackage(ManifestPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		folderEClass.getESuperTypes().add(this.getContainer());
		scenarioServiceEClass.getESuperTypes().add(this.getContainer());
		scenarioInstanceEClass.getESuperTypes().add(this.getContainer());

		// Initialize classes and features; add operations and parameters
		initEClass(scenarioModelEClass, ScenarioModel.class, "ScenarioModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getScenarioModel_ScenarioServices(), this.getScenarioService(), this.getScenarioService_ScenarioModel(), "scenarioServices", null, 0, -1, ScenarioModel.class, IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(containerEClass, com.mmxlabs.scenario.service.model.Container.class, "Container", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getContainer_Parent(), this.getContainer(), this.getContainer_Elements(), "parent", null, 0, 1, com.mmxlabs.scenario.service.model.Container.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getContainer_Elements(), this.getContainer(), this.getContainer_Parent(), "elements", null, 0, -1, com.mmxlabs.scenario.service.model.Container.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContainer_Archived(), ecorePackage.getEBoolean(), "archived", null, 0, 1, com.mmxlabs.scenario.service.model.Container.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContainer_Name(), ecorePackage.getEString(), "name", null, 0, 1, com.mmxlabs.scenario.service.model.Container.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContainer_Hidden(), ecorePackage.getEBoolean(), "hidden", null, 0, 1, com.mmxlabs.scenario.service.model.Container.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(containerEClass, ecorePackage.getEInt(), "getContainedInstanceCount", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(folderEClass, Folder.class, "Folder", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFolder_Metadata(), this.getMetadata(), null, "metadata", null, 0, 1, Folder.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFolder_Managed(), ecorePackage.getEBoolean(), "managed", null, 0, 1, Folder.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);

		initEClass(scenarioServiceEClass, ScenarioService.class, "ScenarioService", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getScenarioService_Description(), ecorePackage.getEString(), "description", null, 0, 1, ScenarioService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
				!IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScenarioService_ServiceRef(), this.getIScenarioService(), "serviceRef", null, 0, 1, ScenarioService.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScenarioService_SupportsForking(), ecorePackage.getEBoolean(), "supportsForking", null, 0, 1, ScenarioService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScenarioService_SupportsImport(), ecorePackage.getEBoolean(), "supportsImport", null, 0, 1, ScenarioService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
				!IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getScenarioService_ScenarioModel(), this.getScenarioModel(), this.getScenarioModel_ScenarioServices(), "scenarioModel", null, 0, 1, ScenarioService.class, IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScenarioService_Local(), ecorePackage.getEBoolean(), "local", null, 0, 1, ScenarioService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScenarioService_ServiceID(), ecorePackage.getEString(), "serviceID", null, 0, 1, ScenarioService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScenarioService_Offline(), ecorePackage.getEBoolean(), "offline", null, 0, 1, ScenarioService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScenarioService_LockedBy(), ecorePackage.getEString(), "lockedBy", null, 0, 1, ScenarioService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScenarioService_Locked(), ecorePackage.getEBoolean(), "locked", null, 0, 1, ScenarioService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScenarioService_Image(), ecorePackage.getEJavaObject(), "image", null, 0, 1, ScenarioService.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(scenarioInstanceEClass, ScenarioInstance.class, "ScenarioInstance", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getScenarioInstance_Uuid(), ecorePackage.getEString(), "uuid", null, 1, 1, ScenarioInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getScenarioInstance_RootObjectURI(), ecorePackage.getEString(), "rootObjectURI", null, 0, 1, ScenarioInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
				!IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getScenarioInstance_Metadata(), this.getMetadata(), null, "metadata", null, 0, 1, ScenarioInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScenarioInstance_ScenarioVersion(), ecorePackage.getEInt(), "scenarioVersion", null, 0, 1, ScenarioInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
				!IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScenarioInstance_VersionContext(), ecorePackage.getEString(), "versionContext", null, 0, 1, ScenarioInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScenarioInstance_ClientScenarioVersion(), ecorePackage.getEInt(), "clientScenarioVersion", null, 0, 1, ScenarioInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScenarioInstance_ClientVersionContext(), ecorePackage.getEString(), "clientVersionContext", null, 0, 1, ScenarioInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getScenarioInstance_Fragments(), this.getScenarioFragment(), this.getScenarioFragment_ScenarioInstance(), "fragments", null, 0, -1, ScenarioInstance.class, IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScenarioInstance_Readonly(), ecorePackage.getEBoolean(), "readonly", null, 0, 1, ScenarioInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScenarioInstance_CloudLocked(), ecorePackage.getEBoolean(), "cloudLocked", null, 0, 1, ScenarioInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
				!IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScenarioInstance_ValidationStatusCode(), ecorePackage.getEInt(), "validationStatusCode", null, 0, 1, ScenarioInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getScenarioInstance_Manifest(), theManifestPackage.getManifest(), null, "manifest", null, 0, 1, ScenarioInstance.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
				IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScenarioInstance_ExternalID(), ecorePackage.getEString(), "externalID", null, 0, 1, ScenarioInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
				!IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(scenarioInstanceEClass, ecorePackage.getEInt(), "getContainedInstanceCount", 1, 1, IS_UNIQUE, IS_ORDERED);

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

		initEClass(scenarioFragmentEClass, ScenarioFragment.class, "ScenarioFragment", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getScenarioFragment_ScenarioInstance(), this.getScenarioInstance(), this.getScenarioInstance_Fragments(), "scenarioInstance", null, 0, 1, ScenarioFragment.class, IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScenarioFragment_Name(), ecorePackage.getEString(), "name", null, 0, 1, ScenarioFragment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEReference(getScenarioFragment_Fragment(), ecorePackage.getEObject(), null, "fragment", null, 0, 1, ScenarioFragment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
				IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScenarioFragment_ContentType(), ecorePackage.getEString(), "contentType", null, 0, 1, ScenarioFragment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
				!IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScenarioFragment_UseCommandStack(), ecorePackage.getEBoolean(), "useCommandStack", null, 0, 1, ScenarioFragment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScenarioFragment_TypeHint(), ecorePackage.getEString(), "typeHint", null, 0, 1, ScenarioFragment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize data types
		initEDataType(iScenarioServiceEDataType, IScenarioService.class, "IScenarioService", !IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //ScenarioServicePackageImpl
