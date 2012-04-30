/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
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
 * @see com.mmxlabs.scenario.service.model.ScenarioServiceFactory
 * @model kind="package"
 * @generated
 */
public interface ScenarioServicePackage extends EPackage {
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
	String eNS_URI = "http://com.mmxlabs.scenario.service/model/1";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "com.mmxlabs.scenario.service";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ScenarioServicePackage eINSTANCE = com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl.init();

	/**
	 * The meta object id for the '{@link com.mmxlabs.scenario.service.model.impl.ScenarioModelImpl <em>Scenario Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioModelImpl
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getScenarioModel()
	 * @generated
	 */
	int SCENARIO_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Scenario Services</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_MODEL__SCENARIO_SERVICES = 0;

	/**
	 * The number of structural features of the '<em>Scenario Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_MODEL_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.scenario.service.model.impl.ContainerImpl <em>Container</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.scenario.service.model.impl.ContainerImpl
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getContainer()
	 * @generated
	 */
	int CONTAINER = 1;

	/**
	 * The feature id for the '<em><b>Parent</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER__PARENT = 0;

	/**
	 * The feature id for the '<em><b>Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER__ELEMENTS = 1;

	/**
	 * The feature id for the '<em><b>Archived</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER__ARCHIVED = 2;

	/**
	 * The number of structural features of the '<em>Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.scenario.service.model.impl.FolderImpl <em>Folder</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.scenario.service.model.impl.FolderImpl
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getFolder()
	 * @generated
	 */
	int FOLDER = 2;

	/**
	 * The feature id for the '<em><b>Parent</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOLDER__PARENT = CONTAINER__PARENT;

	/**
	 * The feature id for the '<em><b>Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOLDER__ELEMENTS = CONTAINER__ELEMENTS;

	/**
	 * The feature id for the '<em><b>Archived</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOLDER__ARCHIVED = CONTAINER__ARCHIVED;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOLDER__NAME = CONTAINER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Metadata</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOLDER__METADATA = CONTAINER_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Folder</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOLDER_FEATURE_COUNT = CONTAINER_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.scenario.service.model.impl.ScenarioServiceImpl <em>Scenario Service</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServiceImpl
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getScenarioService()
	 * @generated
	 */
	int SCENARIO_SERVICE = 3;

	/**
	 * The feature id for the '<em><b>Parent</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_SERVICE__PARENT = CONTAINER__PARENT;

	/**
	 * The feature id for the '<em><b>Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_SERVICE__ELEMENTS = CONTAINER__ELEMENTS;

	/**
	 * The feature id for the '<em><b>Archived</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_SERVICE__ARCHIVED = CONTAINER__ARCHIVED;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_SERVICE__NAME = CONTAINER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_SERVICE__DESCRIPTION = CONTAINER_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Service Ref</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_SERVICE__SERVICE_REF = CONTAINER_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Scenario Service</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_SERVICE_FEATURE_COUNT = CONTAINER_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl <em>Scenario Instance</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getScenarioInstance()
	 * @generated
	 */
	int SCENARIO_INSTANCE = 4;

	/**
	 * The feature id for the '<em><b>Parent</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__PARENT = CONTAINER__PARENT;

	/**
	 * The feature id for the '<em><b>Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__ELEMENTS = CONTAINER__ELEMENTS;

	/**
	 * The feature id for the '<em><b>Archived</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__ARCHIVED = CONTAINER__ARCHIVED;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__NAME = CONTAINER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__UUID = CONTAINER_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Metadata</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__METADATA = CONTAINER_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Locked</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__LOCKED = CONTAINER_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Instance</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__INSTANCE = CONTAINER_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Adapters</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__ADAPTERS = CONTAINER_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Sub Model UR Is</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__SUB_MODEL_UR_IS = CONTAINER_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Dependency UUI Ds</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__DEPENDENCY_UUI_DS = CONTAINER_FEATURE_COUNT + 7;

	/**
	 * The number of structural features of the '<em>Scenario Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE_FEATURE_COUNT = CONTAINER_FEATURE_COUNT + 8;

	/**
	 * The meta object id for the '{@link com.mmxlabs.scenario.service.model.impl.MetadataImpl <em>Metadata</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.scenario.service.model.impl.MetadataImpl
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getMetadata()
	 * @generated
	 */
	int METADATA = 5;

	/**
	 * The feature id for the '<em><b>Creator</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METADATA__CREATOR = 0;

	/**
	 * The feature id for the '<em><b>Created</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METADATA__CREATED = 1;

	/**
	 * The feature id for the '<em><b>Last Modified</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METADATA__LAST_MODIFIED = 2;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METADATA__COMMENT = 3;

	/**
	 * The feature id for the '<em><b>Last Modified By</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METADATA__LAST_MODIFIED_BY = 4;

	/**
	 * The feature id for the '<em><b>Content Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METADATA__CONTENT_TYPE = 5;

	/**
	 * The number of structural features of the '<em>Metadata</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METADATA_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '<em>Class</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.Class
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getClass_()
	 * @generated
	 */
	int CLASS = 6;

	/**
	 * The meta object id for the '<em>IScenario Service</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.scenario.service.IScenarioService
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getIScenarioService()
	 * @generated
	 */
	int ISCENARIO_SERVICE = 7;

	/**
	 * The meta object id for the '<em>Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.Object
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getObject()
	 * @generated
	 */
	int OBJECT = 8;

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.scenario.service.model.ScenarioModel <em>Scenario Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Scenario Model</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioModel
	 * @generated
	 */
	EClass getScenarioModel();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.scenario.service.model.ScenarioModel#getScenarioServices <em>Scenario Services</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Scenario Services</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioModel#getScenarioServices()
	 * @see #getScenarioModel()
	 * @generated
	 */
	EReference getScenarioModel_ScenarioServices();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.scenario.service.model.Container <em>Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Container</em>'.
	 * @see com.mmxlabs.scenario.service.model.Container
	 * @generated
	 */
	EClass getContainer();

	/**
	 * Returns the meta object for the container reference '{@link com.mmxlabs.scenario.service.model.Container#getParent <em>Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Parent</em>'.
	 * @see com.mmxlabs.scenario.service.model.Container#getParent()
	 * @see #getContainer()
	 * @generated
	 */
	EReference getContainer_Parent();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.scenario.service.model.Container#getElements <em>Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Elements</em>'.
	 * @see com.mmxlabs.scenario.service.model.Container#getElements()
	 * @see #getContainer()
	 * @generated
	 */
	EReference getContainer_Elements();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.Container#isArchived <em>Archived</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Archived</em>'.
	 * @see com.mmxlabs.scenario.service.model.Container#isArchived()
	 * @see #getContainer()
	 * @generated
	 */
	EAttribute getContainer_Archived();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.scenario.service.model.Folder <em>Folder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Folder</em>'.
	 * @see com.mmxlabs.scenario.service.model.Folder
	 * @generated
	 */
	EClass getFolder();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.Folder#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see com.mmxlabs.scenario.service.model.Folder#getName()
	 * @see #getFolder()
	 * @generated
	 */
	EAttribute getFolder_Name();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.scenario.service.model.Folder#getMetadata <em>Metadata</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Metadata</em>'.
	 * @see com.mmxlabs.scenario.service.model.Folder#getMetadata()
	 * @see #getFolder()
	 * @generated
	 */
	EReference getFolder_Metadata();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.scenario.service.model.ScenarioService <em>Scenario Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Scenario Service</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioService
	 * @generated
	 */
	EClass getScenarioService();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioService#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioService#getName()
	 * @see #getScenarioService()
	 * @generated
	 */
	EAttribute getScenarioService_Name();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioService#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioService#getDescription()
	 * @see #getScenarioService()
	 * @generated
	 */
	EAttribute getScenarioService_Description();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioService#getServiceRef <em>Service Ref</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Service Ref</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioService#getServiceRef()
	 * @see #getScenarioService()
	 * @generated
	 */
	EAttribute getScenarioService_ServiceRef();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.scenario.service.model.ScenarioInstance <em>Scenario Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Scenario Instance</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance
	 * @generated
	 */
	EClass getScenarioInstance();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#getName()
	 * @see #getScenarioInstance()
	 * @generated
	 */
	EAttribute getScenarioInstance_Name();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getUuid <em>Uuid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Uuid</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#getUuid()
	 * @see #getScenarioInstance()
	 * @generated
	 */
	EAttribute getScenarioInstance_Uuid();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getMetadata <em>Metadata</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Metadata</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#getMetadata()
	 * @see #getScenarioInstance()
	 * @generated
	 */
	EReference getScenarioInstance_Metadata();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#isLocked <em>Locked</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Locked</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#isLocked()
	 * @see #getScenarioInstance()
	 * @generated
	 */
	EAttribute getScenarioInstance_Locked();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getInstance <em>Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Instance</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#getInstance()
	 * @see #getScenarioInstance()
	 * @generated
	 */
	EReference getScenarioInstance_Instance();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getAdapters <em>Adapters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Adapters</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#getAdapters()
	 * @see #getScenarioInstance()
	 * @generated
	 */
	EAttribute getScenarioInstance_Adapters();

	/**
	 * Returns the meta object for the attribute list '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getSubModelURIs <em>Sub Model UR Is</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Sub Model UR Is</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#getSubModelURIs()
	 * @see #getScenarioInstance()
	 * @generated
	 */
	EAttribute getScenarioInstance_SubModelURIs();

	/**
	 * Returns the meta object for the attribute list '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getDependencyUUIDs <em>Dependency UUI Ds</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Dependency UUI Ds</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#getDependencyUUIDs()
	 * @see #getScenarioInstance()
	 * @generated
	 */
	EAttribute getScenarioInstance_DependencyUUIDs();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.scenario.service.model.Metadata <em>Metadata</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Metadata</em>'.
	 * @see com.mmxlabs.scenario.service.model.Metadata
	 * @generated
	 */
	EClass getMetadata();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.Metadata#getCreator <em>Creator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Creator</em>'.
	 * @see com.mmxlabs.scenario.service.model.Metadata#getCreator()
	 * @see #getMetadata()
	 * @generated
	 */
	EAttribute getMetadata_Creator();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.Metadata#getCreated <em>Created</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Created</em>'.
	 * @see com.mmxlabs.scenario.service.model.Metadata#getCreated()
	 * @see #getMetadata()
	 * @generated
	 */
	EAttribute getMetadata_Created();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.Metadata#getLastModified <em>Last Modified</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Last Modified</em>'.
	 * @see com.mmxlabs.scenario.service.model.Metadata#getLastModified()
	 * @see #getMetadata()
	 * @generated
	 */
	EAttribute getMetadata_LastModified();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.Metadata#getComment <em>Comment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Comment</em>'.
	 * @see com.mmxlabs.scenario.service.model.Metadata#getComment()
	 * @see #getMetadata()
	 * @generated
	 */
	EAttribute getMetadata_Comment();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.Metadata#getLastModifiedBy <em>Last Modified By</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Last Modified By</em>'.
	 * @see com.mmxlabs.scenario.service.model.Metadata#getLastModifiedBy()
	 * @see #getMetadata()
	 * @generated
	 */
	EAttribute getMetadata_LastModifiedBy();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.Metadata#getContentType <em>Content Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Content Type</em>'.
	 * @see com.mmxlabs.scenario.service.model.Metadata#getContentType()
	 * @see #getMetadata()
	 * @generated
	 */
	EAttribute getMetadata_ContentType();

	/**
	 * Returns the meta object for data type '{@link java.lang.Class <em>Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Class</em>'.
	 * @see java.lang.Class
	 * @model instanceClass="java.lang.Class" typeParameters="T"
	 * @generated
	 */
	EDataType getClass_();

	/**
	 * Returns the meta object for data type '{@link com.mmxlabs.scenario.service.IScenarioService <em>IScenario Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>IScenario Service</em>'.
	 * @see com.mmxlabs.scenario.service.IScenarioService
	 * @model instanceClass="com.mmxlabs.scenario.service.IScenarioService" serializeable="false"
	 * @generated
	 */
	EDataType getIScenarioService();

	/**
	 * Returns the meta object for data type '{@link java.lang.Object <em>Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Object</em>'.
	 * @see java.lang.Object
	 * @model instanceClass="java.lang.Object"
	 * @generated
	 */
	EDataType getObject();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ScenarioServiceFactory getScenarioServiceFactory();

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
		 * The meta object literal for the '{@link com.mmxlabs.scenario.service.model.impl.ScenarioModelImpl <em>Scenario Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.scenario.service.model.impl.ScenarioModelImpl
		 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getScenarioModel()
		 * @generated
		 */
		EClass SCENARIO_MODEL = eINSTANCE.getScenarioModel();

		/**
		 * The meta object literal for the '<em><b>Scenario Services</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCENARIO_MODEL__SCENARIO_SERVICES = eINSTANCE.getScenarioModel_ScenarioServices();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.scenario.service.model.impl.ContainerImpl <em>Container</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.scenario.service.model.impl.ContainerImpl
		 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getContainer()
		 * @generated
		 */
		EClass CONTAINER = eINSTANCE.getContainer();

		/**
		 * The meta object literal for the '<em><b>Parent</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTAINER__PARENT = eINSTANCE.getContainer_Parent();

		/**
		 * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTAINER__ELEMENTS = eINSTANCE.getContainer_Elements();

		/**
		 * The meta object literal for the '<em><b>Archived</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTAINER__ARCHIVED = eINSTANCE.getContainer_Archived();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.scenario.service.model.impl.FolderImpl <em>Folder</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.scenario.service.model.impl.FolderImpl
		 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getFolder()
		 * @generated
		 */
		EClass FOLDER = eINSTANCE.getFolder();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FOLDER__NAME = eINSTANCE.getFolder_Name();

		/**
		 * The meta object literal for the '<em><b>Metadata</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FOLDER__METADATA = eINSTANCE.getFolder_Metadata();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.scenario.service.model.impl.ScenarioServiceImpl <em>Scenario Service</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServiceImpl
		 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getScenarioService()
		 * @generated
		 */
		EClass SCENARIO_SERVICE = eINSTANCE.getScenarioService();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCENARIO_SERVICE__NAME = eINSTANCE.getScenarioService_Name();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCENARIO_SERVICE__DESCRIPTION = eINSTANCE.getScenarioService_Description();

		/**
		 * The meta object literal for the '<em><b>Service Ref</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCENARIO_SERVICE__SERVICE_REF = eINSTANCE.getScenarioService_ServiceRef();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl <em>Scenario Instance</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.scenario.service.model.impl.ScenarioInstanceImpl
		 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getScenarioInstance()
		 * @generated
		 */
		EClass SCENARIO_INSTANCE = eINSTANCE.getScenarioInstance();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCENARIO_INSTANCE__NAME = eINSTANCE.getScenarioInstance_Name();

		/**
		 * The meta object literal for the '<em><b>Uuid</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCENARIO_INSTANCE__UUID = eINSTANCE.getScenarioInstance_Uuid();

		/**
		 * The meta object literal for the '<em><b>Metadata</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCENARIO_INSTANCE__METADATA = eINSTANCE.getScenarioInstance_Metadata();

		/**
		 * The meta object literal for the '<em><b>Locked</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCENARIO_INSTANCE__LOCKED = eINSTANCE.getScenarioInstance_Locked();

		/**
		 * The meta object literal for the '<em><b>Instance</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCENARIO_INSTANCE__INSTANCE = eINSTANCE.getScenarioInstance_Instance();

		/**
		 * The meta object literal for the '<em><b>Adapters</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCENARIO_INSTANCE__ADAPTERS = eINSTANCE.getScenarioInstance_Adapters();

		/**
		 * The meta object literal for the '<em><b>Sub Model UR Is</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCENARIO_INSTANCE__SUB_MODEL_UR_IS = eINSTANCE.getScenarioInstance_SubModelURIs();

		/**
		 * The meta object literal for the '<em><b>Dependency UUI Ds</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCENARIO_INSTANCE__DEPENDENCY_UUI_DS = eINSTANCE.getScenarioInstance_DependencyUUIDs();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.scenario.service.model.impl.MetadataImpl <em>Metadata</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.scenario.service.model.impl.MetadataImpl
		 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getMetadata()
		 * @generated
		 */
		EClass METADATA = eINSTANCE.getMetadata();

		/**
		 * The meta object literal for the '<em><b>Creator</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute METADATA__CREATOR = eINSTANCE.getMetadata_Creator();

		/**
		 * The meta object literal for the '<em><b>Created</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute METADATA__CREATED = eINSTANCE.getMetadata_Created();

		/**
		 * The meta object literal for the '<em><b>Last Modified</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute METADATA__LAST_MODIFIED = eINSTANCE.getMetadata_LastModified();

		/**
		 * The meta object literal for the '<em><b>Comment</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute METADATA__COMMENT = eINSTANCE.getMetadata_Comment();

		/**
		 * The meta object literal for the '<em><b>Last Modified By</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute METADATA__LAST_MODIFIED_BY = eINSTANCE.getMetadata_LastModifiedBy();

		/**
		 * The meta object literal for the '<em><b>Content Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute METADATA__CONTENT_TYPE = eINSTANCE.getMetadata_ContentType();

		/**
		 * The meta object literal for the '<em>Class</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.Class
		 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getClass_()
		 * @generated
		 */
		EDataType CLASS = eINSTANCE.getClass_();

		/**
		 * The meta object literal for the '<em>IScenario Service</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.scenario.service.IScenarioService
		 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getIScenarioService()
		 * @generated
		 */
		EDataType ISCENARIO_SERVICE = eINSTANCE.getIScenarioService();

		/**
		 * The meta object literal for the '<em>Object</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.Object
		 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getObject()
		 * @generated
		 */
		EDataType OBJECT = eINSTANCE.getObject();

	}

} //ScenarioServicePackage
