/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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
 * @noimplement This interface is not intended to be implemented by clients.
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
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER__NAME = 3;

	/**
	 * The feature id for the '<em><b>Hidden</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER__HIDDEN = 4;

	/**
	 * The number of structural features of the '<em>Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTAINER_FEATURE_COUNT = 5;

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
	int FOLDER__NAME = CONTAINER__NAME;

	/**
	 * The feature id for the '<em><b>Hidden</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOLDER__HIDDEN = CONTAINER__HIDDEN;

	/**
	 * The feature id for the '<em><b>Metadata</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOLDER__METADATA = CONTAINER_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Folder</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOLDER_FEATURE_COUNT = CONTAINER_FEATURE_COUNT + 1;

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
	int SCENARIO_SERVICE__NAME = CONTAINER__NAME;

	/**
	 * The feature id for the '<em><b>Hidden</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_SERVICE__HIDDEN = CONTAINER__HIDDEN;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_SERVICE__DESCRIPTION = CONTAINER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Service Ref</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_SERVICE__SERVICE_REF = CONTAINER_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Scenario Service</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_SERVICE_FEATURE_COUNT = CONTAINER_FEATURE_COUNT + 2;

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
	int SCENARIO_INSTANCE__NAME = CONTAINER__NAME;

	/**
	 * The feature id for the '<em><b>Hidden</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__HIDDEN = CONTAINER__HIDDEN;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__UUID = CONTAINER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Metadata</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__METADATA = CONTAINER_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Locked</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__LOCKED = CONTAINER_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Instance</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__INSTANCE = CONTAINER_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Adapters</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__ADAPTERS = CONTAINER_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Sub Model UR Is</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__SUB_MODEL_UR_IS = CONTAINER_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Dependency UUI Ds</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__DEPENDENCY_UUI_DS = CONTAINER_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Dirty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__DIRTY = CONTAINER_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Locks</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__LOCKS = CONTAINER_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Validation Status Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__VALIDATION_STATUS_CODE = CONTAINER_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Scenario Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 3.1
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__SCENARIO_VERSION = CONTAINER_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Version Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 3.1
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__VERSION_CONTEXT = CONTAINER_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>Fragments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * @since 3.1
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__FRAGMENTS = CONTAINER_FEATURE_COUNT + 12;

	/**
	 * The number of structural features of the '<em>Scenario Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * @Note: Compatibility filter created
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE_FEATURE_COUNT = CONTAINER_FEATURE_COUNT + 13;

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
	 * The meta object id for the '{@link com.mmxlabs.scenario.service.model.impl.ScenarioLockImpl <em>Scenario Lock</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioLockImpl
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getScenarioLock()
	 * @generated
	 */
	int SCENARIO_LOCK = 6;

	/**
	 * The feature id for the '<em><b>Available</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_LOCK__AVAILABLE = 0;

	/**
	 * The feature id for the '<em><b>Claimed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_LOCK__CLAIMED = 1;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_LOCK__KEY = 2;

	/**
	 * The feature id for the '<em><b>Instance</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_LOCK__INSTANCE = 3;

	/**
	 * The number of structural features of the '<em>Scenario Lock</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_LOCK_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link com.mmxlabs.scenario.service.model.impl.ScenarioFragmentImpl <em>Scenario Fragment</em>}' class.
	 * <!-- begin-user-doc -->
	 * @since 3.1
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioFragmentImpl
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getScenarioFragment()
	 * @generated
	 */
	int SCENARIO_FRAGMENT = 7;

	/**
	 * The feature id for the '<em><b>Scenario Instance</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * @since 3.1
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_FRAGMENT__SCENARIO_INSTANCE = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 3.1
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_FRAGMENT__NAME = 1;

	/**
	 * The feature id for the '<em><b>Fragment</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * @since 3.1
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_FRAGMENT__FRAGMENT = 2;

	/**
	 * The feature id for the '<em><b>Content Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 3.1
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_FRAGMENT__CONTENT_TYPE = 3;

	/**
	 * The number of structural features of the '<em>Scenario Fragment</em>' class.
	 * <!-- begin-user-doc -->
	 * @since 3.1
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_FRAGMENT_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '<em>IScenario Service</em>' data type.
	 * <!-- begin-user-doc -->
	 * @Note: Compatibility filter created
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.scenario.service.IScenarioService
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getIScenarioService()
	 * @generated
	 */
	int ISCENARIO_SERVICE = 8;

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.Container#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see com.mmxlabs.scenario.service.model.Container#getName()
	 * @see #getContainer()
	 * @generated
	 */
	EAttribute getContainer_Name();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.Container#isHidden <em>Hidden</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Hidden</em>'.
	 * @see com.mmxlabs.scenario.service.model.Container#isHidden()
	 * @see #getContainer()
	 * @generated
	 */
	EAttribute getContainer_Hidden();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getScenarioVersion <em>Scenario Version</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 3.1
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Scenario Version</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#getScenarioVersion()
	 * @see #getScenarioInstance()
	 * @generated
	 */
	EAttribute getScenarioInstance_ScenarioVersion();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getVersionContext <em>Version Context</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 3.1
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version Context</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#getVersionContext()
	 * @see #getScenarioInstance()
	 * @generated
	 */
	EAttribute getScenarioInstance_VersionContext();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getFragments <em>Fragments</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 3.1
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Fragments</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#getFragments()
	 * @see #getScenarioInstance()
	 * @generated
	 */
	EReference getScenarioInstance_Fragments();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#isDirty <em>Dirty</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Dirty</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#isDirty()
	 * @see #getScenarioInstance()
	 * @generated
	 */
	EAttribute getScenarioInstance_Dirty();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getLocks <em>Locks</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Locks</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#getLocks()
	 * @see #getScenarioInstance()
	 * @generated
	 */
	EReference getScenarioInstance_Locks();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getValidationStatusCode <em>Validation Status Code</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Validation Status Code</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#getValidationStatusCode()
	 * @see #getScenarioInstance()
	 * @generated
	 */
	EAttribute getScenarioInstance_ValidationStatusCode();

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
	 * Returns the meta object for class '{@link com.mmxlabs.scenario.service.model.ScenarioLock <em>Scenario Lock</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Scenario Lock</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioLock
	 * @generated
	 */
	EClass getScenarioLock();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioLock#isAvailable <em>Available</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Available</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioLock#isAvailable()
	 * @see #getScenarioLock()
	 * @generated
	 */
	EAttribute getScenarioLock_Available();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioLock#isClaimed <em>Claimed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Claimed</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioLock#isClaimed()
	 * @see #getScenarioLock()
	 * @generated
	 */
	EAttribute getScenarioLock_Claimed();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioLock#getKey <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioLock#getKey()
	 * @see #getScenarioLock()
	 * @generated
	 */
	EAttribute getScenarioLock_Key();

	/**
	 * Returns the meta object for the container reference '{@link com.mmxlabs.scenario.service.model.ScenarioLock#getInstance <em>Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Instance</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioLock#getInstance()
	 * @see #getScenarioLock()
	 * @generated
	 */
	EReference getScenarioLock_Instance();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.scenario.service.model.ScenarioFragment <em>Scenario Fragment</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 3.1
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Scenario Fragment</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioFragment
	 * @generated
	 */
	EClass getScenarioFragment();

	/**
	 * Returns the meta object for the container reference '{@link com.mmxlabs.scenario.service.model.ScenarioFragment#getScenarioInstance <em>Scenario Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 3.1
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Scenario Instance</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioFragment#getScenarioInstance()
	 * @see #getScenarioFragment()
	 * @generated
	 */
	EReference getScenarioFragment_ScenarioInstance();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioFragment#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 3.1
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioFragment#getName()
	 * @see #getScenarioFragment()
	 * @generated
	 */
	EAttribute getScenarioFragment_Name();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.scenario.service.model.ScenarioFragment#getFragment <em>Fragment</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 3.1
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Fragment</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioFragment#getFragment()
	 * @see #getScenarioFragment()
	 * @generated
	 */
	EReference getScenarioFragment_Fragment();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioFragment#getContentType <em>Content Type</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 3.1
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Content Type</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioFragment#getContentType()
	 * @see #getScenarioFragment()
	 * @generated
	 */
	EAttribute getScenarioFragment_ContentType();

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
	 * @noimplement This interface is not intended to be implemented by clients.
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
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTAINER__NAME = eINSTANCE.getContainer_Name();

		/**
		 * The meta object literal for the '<em><b>Hidden</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTAINER__HIDDEN = eINSTANCE.getContainer_Hidden();

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
		 * The meta object literal for the '<em><b>Scenario Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * @since 3.1
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCENARIO_INSTANCE__SCENARIO_VERSION = eINSTANCE.getScenarioInstance_ScenarioVersion();

		/**
		 * The meta object literal for the '<em><b>Version Context</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * @since 3.1
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCENARIO_INSTANCE__VERSION_CONTEXT = eINSTANCE.getScenarioInstance_VersionContext();

		/**
		 * The meta object literal for the '<em><b>Fragments</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * @since 3.1
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCENARIO_INSTANCE__FRAGMENTS = eINSTANCE.getScenarioInstance_Fragments();

		/**
		 * The meta object literal for the '<em><b>Dirty</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCENARIO_INSTANCE__DIRTY = eINSTANCE.getScenarioInstance_Dirty();

		/**
		 * The meta object literal for the '<em><b>Locks</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCENARIO_INSTANCE__LOCKS = eINSTANCE.getScenarioInstance_Locks();

		/**
		 * The meta object literal for the '<em><b>Validation Status Code</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCENARIO_INSTANCE__VALIDATION_STATUS_CODE = eINSTANCE.getScenarioInstance_ValidationStatusCode();

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
		 * The meta object literal for the '{@link com.mmxlabs.scenario.service.model.impl.ScenarioLockImpl <em>Scenario Lock</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.scenario.service.model.impl.ScenarioLockImpl
		 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getScenarioLock()
		 * @generated
		 */
		EClass SCENARIO_LOCK = eINSTANCE.getScenarioLock();

		/**
		 * The meta object literal for the '<em><b>Available</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCENARIO_LOCK__AVAILABLE = eINSTANCE.getScenarioLock_Available();

		/**
		 * The meta object literal for the '<em><b>Claimed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCENARIO_LOCK__CLAIMED = eINSTANCE.getScenarioLock_Claimed();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCENARIO_LOCK__KEY = eINSTANCE.getScenarioLock_Key();

		/**
		 * The meta object literal for the '<em><b>Instance</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCENARIO_LOCK__INSTANCE = eINSTANCE.getScenarioLock_Instance();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.scenario.service.model.impl.ScenarioFragmentImpl <em>Scenario Fragment</em>}' class.
		 * <!-- begin-user-doc -->
		 * @since 3.1
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.scenario.service.model.impl.ScenarioFragmentImpl
		 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getScenarioFragment()
		 * @generated
		 */
		EClass SCENARIO_FRAGMENT = eINSTANCE.getScenarioFragment();

		/**
		 * The meta object literal for the '<em><b>Scenario Instance</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * @since 3.1
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCENARIO_FRAGMENT__SCENARIO_INSTANCE = eINSTANCE.getScenarioFragment_ScenarioInstance();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * @since 3.1
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCENARIO_FRAGMENT__NAME = eINSTANCE.getScenarioFragment_Name();

		/**
		 * The meta object literal for the '<em><b>Fragment</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * @since 3.1
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCENARIO_FRAGMENT__FRAGMENT = eINSTANCE.getScenarioFragment_Fragment();

		/**
		 * The meta object literal for the '<em><b>Content Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * @since 3.1
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCENARIO_FRAGMENT__CONTENT_TYPE = eINSTANCE.getScenarioFragment_ContentType();

		/**
		 * The meta object literal for the '<em>IScenario Service</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.scenario.service.IScenarioService
		 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getIScenarioService()
		 * @generated
		 */
		EDataType ISCENARIO_SERVICE = eINSTANCE.getIScenarioService();

	}

} //ScenarioServicePackage
