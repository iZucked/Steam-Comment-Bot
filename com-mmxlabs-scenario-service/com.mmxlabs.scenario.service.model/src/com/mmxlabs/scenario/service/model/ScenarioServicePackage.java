/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
	 * The feature id for the '<em><b>Supports Forking</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_SERVICE__SUPPORTS_FORKING = CONTAINER_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Supports Import</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_SERVICE__SUPPORTS_IMPORT = CONTAINER_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Scenario Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_SERVICE__SCENARIO_MODEL = CONTAINER_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Local</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_SERVICE__LOCAL = CONTAINER_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Scenario Service</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_SERVICE_FEATURE_COUNT = CONTAINER_FEATURE_COUNT + 6;

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
	 * The feature id for the '<em><b>Root Object URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__ROOT_OBJECT_URI = CONTAINER_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Metadata</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__METADATA = CONTAINER_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Scenario Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__SCENARIO_VERSION = CONTAINER_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Version Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__VERSION_CONTEXT = CONTAINER_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Client Scenario Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__CLIENT_SCENARIO_VERSION = CONTAINER_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Client Version Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__CLIENT_VERSION_CONTEXT = CONTAINER_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Instance</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__INSTANCE = CONTAINER_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Fragments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__FRAGMENTS = CONTAINER_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Model References</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__MODEL_REFERENCES = CONTAINER_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Adapters</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__ADAPTERS = CONTAINER_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Locked</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__LOCKED = CONTAINER_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>Locks</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__LOCKS = CONTAINER_FEATURE_COUNT + 12;

	/**
	 * The feature id for the '<em><b>Readonly</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__READONLY = CONTAINER_FEATURE_COUNT + 13;

	/**
	 * The feature id for the '<em><b>Dirty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__DIRTY = CONTAINER_FEATURE_COUNT + 14;

	/**
	 * The feature id for the '<em><b>Validation Status Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__VALIDATION_STATUS_CODE = CONTAINER_FEATURE_COUNT + 15;

	/**
	 * The feature id for the '<em><b>Load Failure</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__LOAD_FAILURE = CONTAINER_FEATURE_COUNT + 16;

	/**
	 * The feature id for the '<em><b>Load Exception</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE__LOAD_EXCEPTION = CONTAINER_FEATURE_COUNT + 17;

	/**
	 * The number of structural features of the '<em>Scenario Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * @Note: Compatibility filter created
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_INSTANCE_FEATURE_COUNT = CONTAINER_FEATURE_COUNT + 18;

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
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioFragmentImpl
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getScenarioFragment()
	 * @generated
	 */
	int SCENARIO_FRAGMENT = 7;

	/**
	 * The feature id for the '<em><b>Scenario Instance</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_FRAGMENT__SCENARIO_INSTANCE = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_FRAGMENT__NAME = 1;

	/**
	 * The feature id for the '<em><b>Fragment</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_FRAGMENT__FRAGMENT = 2;

	/**
	 * The feature id for the '<em><b>Content Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_FRAGMENT__CONTENT_TYPE = 3;

	/**
	 * The number of structural features of the '<em>Scenario Fragment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_FRAGMENT_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link java.io.Closeable <em>Closeable</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.io.Closeable
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getCloseable()
	 * @generated
	 */
	int CLOSEABLE = 9;

	/**
	 * The number of structural features of the '<em>Closeable</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLOSEABLE_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.scenario.service.model.impl.ModelReferenceImpl <em>Model Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.scenario.service.model.impl.ModelReferenceImpl
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getModelReference()
	 * @generated
	 */
	int MODEL_REFERENCE = 8;

	/**
	 * The feature id for the '<em><b>Scenario Instance</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_REFERENCE__SCENARIO_INSTANCE = CLOSEABLE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Reference Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_REFERENCE__REFERENCE_ID = CLOSEABLE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Model Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_REFERENCE_FEATURE_COUNT = CLOSEABLE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '<em>IScenario Service</em>' data type.
	 * <!-- begin-user-doc -->
	 * @Note: Compatibility filter created
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.scenario.service.IScenarioService
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getIScenarioService()
	 * @generated
	 */
	int ISCENARIO_SERVICE = 10;

	/**
	 * The meta object id for the '<em>IO Exception</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.io.IOException
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getIOException()
	 * @generated
	 */
	int IO_EXCEPTION = 11;

	/**
	 * The meta object id for the '<em>Exception</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.Exception
	 * @see com.mmxlabs.scenario.service.model.impl.ScenarioServicePackageImpl#getException()
	 * @generated
	 */
	int EXCEPTION = 12;

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioService#isSupportsForking <em>Supports Forking</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Supports Forking</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioService#isSupportsForking()
	 * @see #getScenarioService()
	 * @generated
	 */
	EAttribute getScenarioService_SupportsForking();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioService#isSupportsImport <em>Supports Import</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Supports Import</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioService#isSupportsImport()
	 * @see #getScenarioService()
	 * @generated
	 */
	EAttribute getScenarioService_SupportsImport();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.scenario.service.model.ScenarioService#getScenarioModel <em>Scenario Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Scenario Model</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioService#getScenarioModel()
	 * @see #getScenarioService()
	 * @generated
	 */
	EReference getScenarioService_ScenarioModel();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioService#isLocal <em>Local</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Local</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioService#isLocal()
	 * @see #getScenarioService()
	 * @generated
	 */
	EAttribute getScenarioService_Local();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getRootObjectURI <em>Root Object URI</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Root Object URI</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#getRootObjectURI()
	 * @see #getScenarioInstance()
	 * @generated
	 */
	EAttribute getScenarioInstance_RootObjectURI();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getScenarioVersion <em>Scenario Version</em>}'.
	 * <!-- begin-user-doc -->
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
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Fragments</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#getFragments()
	 * @see #getScenarioInstance()
	 * @generated
	 */
	EReference getScenarioInstance_Fragments();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getModelReferences <em>Model References</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Model References</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#getModelReferences()
	 * @see #getScenarioInstance()
	 * @generated
	 */
	EReference getScenarioInstance_ModelReferences();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#isReadonly <em>Readonly</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Readonly</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#isReadonly()
	 * @see #getScenarioInstance()
	 * @generated
	 */
	EAttribute getScenarioInstance_Readonly();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getClientScenarioVersion <em>Client Scenario Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Client Scenario Version</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#getClientScenarioVersion()
	 * @see #getScenarioInstance()
	 * @generated
	 */
	EAttribute getScenarioInstance_ClientScenarioVersion();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getClientVersionContext <em>Client Version Context</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Client Version Context</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#getClientVersionContext()
	 * @see #getScenarioInstance()
	 * @generated
	 */
	EAttribute getScenarioInstance_ClientVersionContext();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#isLoadFailure <em>Load Failure</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Load Failure</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#isLoadFailure()
	 * @see #getScenarioInstance()
	 * @generated
	 */
	EAttribute getScenarioInstance_LoadFailure();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ScenarioInstance#getLoadException <em>Load Exception</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Load Exception</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioInstance#getLoadException()
	 * @see #getScenarioInstance()
	 * @generated
	 */
	EAttribute getScenarioInstance_LoadException();

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
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Scenario Fragment</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioFragment
	 * @generated
	 */
	EClass getScenarioFragment();

	/**
	 * Returns the meta object for the container reference '{@link com.mmxlabs.scenario.service.model.ScenarioFragment#getScenarioInstance <em>Scenario Instance</em>}'.
	 * <!-- begin-user-doc -->
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
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Content Type</em>'.
	 * @see com.mmxlabs.scenario.service.model.ScenarioFragment#getContentType()
	 * @see #getScenarioFragment()
	 * @generated
	 */
	EAttribute getScenarioFragment_ContentType();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.scenario.service.model.ModelReference <em>Model Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model Reference</em>'.
	 * @see com.mmxlabs.scenario.service.model.ModelReference
	 * @generated
	 */
	EClass getModelReference();

	/**
	 * Returns the meta object for the container reference '{@link com.mmxlabs.scenario.service.model.ModelReference#getScenarioInstance <em>Scenario Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Scenario Instance</em>'.
	 * @see com.mmxlabs.scenario.service.model.ModelReference#getScenarioInstance()
	 * @see #getModelReference()
	 * @generated
	 */
	EReference getModelReference_ScenarioInstance();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.model.ModelReference#getReferenceId <em>Reference Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Reference Id</em>'.
	 * @see com.mmxlabs.scenario.service.model.ModelReference#getReferenceId()
	 * @see #getModelReference()
	 * @generated
	 */
	EAttribute getModelReference_ReferenceId();

	/**
	 * Returns the meta object for class '{@link java.io.Closeable <em>Closeable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Closeable</em>'.
	 * @see java.io.Closeable
	 * @model instanceClass="java.io.Closeable"
	 * @generated
	 */
	EClass getCloseable();

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
	 * Returns the meta object for data type '{@link java.io.IOException <em>IO Exception</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>IO Exception</em>'.
	 * @see java.io.IOException
	 * @model instanceClass="java.io.IOException"
	 * @generated
	 */
	EDataType getIOException();

	/**
	 * Returns the meta object for data type '{@link java.lang.Exception <em>Exception</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Exception</em>'.
	 * @see java.lang.Exception
	 * @model instanceClass="java.lang.Exception"
	 * @generated
	 */
	EDataType getException();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ScenarioServiceFactory getScenarioServiceFactory();

} //ScenarioServicePackage
