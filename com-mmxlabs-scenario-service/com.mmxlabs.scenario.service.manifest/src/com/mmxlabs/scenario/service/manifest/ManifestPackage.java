/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.manifest;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
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
 * @see com.mmxlabs.scenario.service.manifest.ManifestFactory
 * @model kind="package"
 * @generated
 */
public interface ManifestPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "manifest";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.mmxlabs.com/models/scenario/manifest/1/";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ManifestPackage eINSTANCE = com.mmxlabs.scenario.service.manifest.impl.ManifestPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.mmxlabs.scenario.service.manifest.impl.ManifestImpl <em>Manifest</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.scenario.service.manifest.impl.ManifestImpl
	 * @see com.mmxlabs.scenario.service.manifest.impl.ManifestPackageImpl#getManifest()
	 * @generated
	 */
	int MANIFEST = 0;

	/**
	 * The feature id for the '<em><b>Model UR Is</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MANIFEST__MODEL_UR_IS = 0;

	/**
	 * The feature id for the '<em><b>UUID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MANIFEST__UUID = 1;

	/**
	 * The feature id for the '<em><b>Scenario Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MANIFEST__SCENARIO_TYPE = 2;

	/**
	 * The feature id for the '<em><b>Scenario Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MANIFEST__SCENARIO_VERSION = 3;

	/**
	 * The feature id for the '<em><b>Version Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MANIFEST__VERSION_CONTEXT = 4;

	/**
	 * The feature id for the '<em><b>Client Scenario Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MANIFEST__CLIENT_SCENARIO_VERSION = 5;

	/**
	 * The feature id for the '<em><b>Client Version Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MANIFEST__CLIENT_VERSION_CONTEXT = 6;

	/**
	 * The feature id for the '<em><b>Model Dependencies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MANIFEST__MODEL_DEPENDENCIES = 7;

	/**
	 * The feature id for the '<em><b>Model Fragments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MANIFEST__MODEL_FRAGMENTS = 8;

	/**
	 * The number of structural features of the '<em>Manifest</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MANIFEST_FEATURE_COUNT = 9;


	/**
	 * The meta object id for the '{@link com.mmxlabs.scenario.service.manifest.impl.ModelArtifactImpl <em>Model Artifact</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.scenario.service.manifest.impl.ModelArtifactImpl
	 * @see com.mmxlabs.scenario.service.manifest.impl.ManifestPackageImpl#getModelArtifact()
	 * @generated
	 */
	int MODEL_ARTIFACT = 1;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ARTIFACT__KEY = 0;

	/**
	 * The feature id for the '<em><b>Storage Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ARTIFACT__STORAGE_TYPE = 1;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ARTIFACT__TYPE = 2;

	/**
	 * The feature id for the '<em><b>Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ARTIFACT__PATH = 3;

	/**
	 * The feature id for the '<em><b>Data Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ARTIFACT__DATA_VERSION = 4;

	/**
	 * The feature id for the '<em><b>Display Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ARTIFACT__DISPLAY_NAME = 5;

	/**
	 * The number of structural features of the '<em>Model Artifact</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ARTIFACT_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.scenario.service.manifest.StorageType <em>Storage Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.scenario.service.manifest.StorageType
	 * @see com.mmxlabs.scenario.service.manifest.impl.ManifestPackageImpl#getStorageType()
	 * @generated
	 */
	int STORAGE_TYPE = 2;


	/**
	 * Returns the meta object for class '{@link com.mmxlabs.scenario.service.manifest.Manifest <em>Manifest</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Manifest</em>'.
	 * @see com.mmxlabs.scenario.service.manifest.Manifest
	 * @generated
	 */
	EClass getManifest();

	/**
	 * Returns the meta object for the attribute list '{@link com.mmxlabs.scenario.service.manifest.Manifest#getModelURIs <em>Model UR Is</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Model UR Is</em>'.
	 * @see com.mmxlabs.scenario.service.manifest.Manifest#getModelURIs()
	 * @see #getManifest()
	 * @generated
	 */
	EAttribute getManifest_ModelURIs();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.manifest.Manifest#getUUID <em>UUID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>UUID</em>'.
	 * @see com.mmxlabs.scenario.service.manifest.Manifest#getUUID()
	 * @see #getManifest()
	 * @generated
	 */
	EAttribute getManifest_UUID();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.manifest.Manifest#getScenarioType <em>Scenario Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Scenario Type</em>'.
	 * @see com.mmxlabs.scenario.service.manifest.Manifest#getScenarioType()
	 * @see #getManifest()
	 * @generated
	 */
	EAttribute getManifest_ScenarioType();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.manifest.Manifest#getScenarioVersion <em>Scenario Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Scenario Version</em>'.
	 * @see com.mmxlabs.scenario.service.manifest.Manifest#getScenarioVersion()
	 * @see #getManifest()
	 * @generated
	 */
	EAttribute getManifest_ScenarioVersion();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.manifest.Manifest#getVersionContext <em>Version Context</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version Context</em>'.
	 * @see com.mmxlabs.scenario.service.manifest.Manifest#getVersionContext()
	 * @see #getManifest()
	 * @generated
	 */
	EAttribute getManifest_VersionContext();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.manifest.Manifest#getClientScenarioVersion <em>Client Scenario Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Client Scenario Version</em>'.
	 * @see com.mmxlabs.scenario.service.manifest.Manifest#getClientScenarioVersion()
	 * @see #getManifest()
	 * @generated
	 */
	EAttribute getManifest_ClientScenarioVersion();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.manifest.Manifest#getClientVersionContext <em>Client Version Context</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Client Version Context</em>'.
	 * @see com.mmxlabs.scenario.service.manifest.Manifest#getClientVersionContext()
	 * @see #getManifest()
	 * @generated
	 */
	EAttribute getManifest_ClientVersionContext();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.scenario.service.manifest.Manifest#getModelDependencies <em>Model Dependencies</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Model Dependencies</em>'.
	 * @see com.mmxlabs.scenario.service.manifest.Manifest#getModelDependencies()
	 * @see #getManifest()
	 * @generated
	 */
	EReference getManifest_ModelDependencies();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.scenario.service.manifest.Manifest#getModelFragments <em>Model Fragments</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Model Fragments</em>'.
	 * @see com.mmxlabs.scenario.service.manifest.Manifest#getModelFragments()
	 * @see #getManifest()
	 * @generated
	 */
	EReference getManifest_ModelFragments();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.scenario.service.manifest.ModelArtifact <em>Model Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model Artifact</em>'.
	 * @see com.mmxlabs.scenario.service.manifest.ModelArtifact
	 * @generated
	 */
	EClass getModelArtifact();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.manifest.ModelArtifact#getKey <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see com.mmxlabs.scenario.service.manifest.ModelArtifact#getKey()
	 * @see #getModelArtifact()
	 * @generated
	 */
	EAttribute getModelArtifact_Key();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.manifest.ModelArtifact#getStorageType <em>Storage Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Storage Type</em>'.
	 * @see com.mmxlabs.scenario.service.manifest.ModelArtifact#getStorageType()
	 * @see #getModelArtifact()
	 * @generated
	 */
	EAttribute getModelArtifact_StorageType();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.manifest.ModelArtifact#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see com.mmxlabs.scenario.service.manifest.ModelArtifact#getType()
	 * @see #getModelArtifact()
	 * @generated
	 */
	EAttribute getModelArtifact_Type();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.manifest.ModelArtifact#getPath <em>Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Path</em>'.
	 * @see com.mmxlabs.scenario.service.manifest.ModelArtifact#getPath()
	 * @see #getModelArtifact()
	 * @generated
	 */
	EAttribute getModelArtifact_Path();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.manifest.ModelArtifact#getDataVersion <em>Data Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Data Version</em>'.
	 * @see com.mmxlabs.scenario.service.manifest.ModelArtifact#getDataVersion()
	 * @see #getModelArtifact()
	 * @generated
	 */
	EAttribute getModelArtifact_DataVersion();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.scenario.service.manifest.ModelArtifact#getDisplayName <em>Display Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Display Name</em>'.
	 * @see com.mmxlabs.scenario.service.manifest.ModelArtifact#getDisplayName()
	 * @see #getModelArtifact()
	 * @generated
	 */
	EAttribute getModelArtifact_DisplayName();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.scenario.service.manifest.StorageType <em>Storage Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Storage Type</em>'.
	 * @see com.mmxlabs.scenario.service.manifest.StorageType
	 * @generated
	 */
	EEnum getStorageType();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ManifestFactory getManifestFactory();

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
		 * The meta object literal for the '{@link com.mmxlabs.scenario.service.manifest.impl.ManifestImpl <em>Manifest</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.scenario.service.manifest.impl.ManifestImpl
		 * @see com.mmxlabs.scenario.service.manifest.impl.ManifestPackageImpl#getManifest()
		 * @generated
		 */
		EClass MANIFEST = eINSTANCE.getManifest();

		/**
		 * The meta object literal for the '<em><b>Model UR Is</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MANIFEST__MODEL_UR_IS = eINSTANCE.getManifest_ModelURIs();

		/**
		 * The meta object literal for the '<em><b>UUID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MANIFEST__UUID = eINSTANCE.getManifest_UUID();

		/**
		 * The meta object literal for the '<em><b>Scenario Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MANIFEST__SCENARIO_TYPE = eINSTANCE.getManifest_ScenarioType();

		/**
		 * The meta object literal for the '<em><b>Scenario Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MANIFEST__SCENARIO_VERSION = eINSTANCE.getManifest_ScenarioVersion();

		/**
		 * The meta object literal for the '<em><b>Version Context</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MANIFEST__VERSION_CONTEXT = eINSTANCE.getManifest_VersionContext();

		/**
		 * The meta object literal for the '<em><b>Client Scenario Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MANIFEST__CLIENT_SCENARIO_VERSION = eINSTANCE.getManifest_ClientScenarioVersion();

		/**
		 * The meta object literal for the '<em><b>Client Version Context</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MANIFEST__CLIENT_VERSION_CONTEXT = eINSTANCE.getManifest_ClientVersionContext();

		/**
		 * The meta object literal for the '<em><b>Model Dependencies</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MANIFEST__MODEL_DEPENDENCIES = eINSTANCE.getManifest_ModelDependencies();

		/**
		 * The meta object literal for the '<em><b>Model Fragments</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MANIFEST__MODEL_FRAGMENTS = eINSTANCE.getManifest_ModelFragments();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.scenario.service.manifest.impl.ModelArtifactImpl <em>Model Artifact</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.scenario.service.manifest.impl.ModelArtifactImpl
		 * @see com.mmxlabs.scenario.service.manifest.impl.ManifestPackageImpl#getModelArtifact()
		 * @generated
		 */
		EClass MODEL_ARTIFACT = eINSTANCE.getModelArtifact();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MODEL_ARTIFACT__KEY = eINSTANCE.getModelArtifact_Key();

		/**
		 * The meta object literal for the '<em><b>Storage Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MODEL_ARTIFACT__STORAGE_TYPE = eINSTANCE.getModelArtifact_StorageType();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MODEL_ARTIFACT__TYPE = eINSTANCE.getModelArtifact_Type();

		/**
		 * The meta object literal for the '<em><b>Path</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MODEL_ARTIFACT__PATH = eINSTANCE.getModelArtifact_Path();

		/**
		 * The meta object literal for the '<em><b>Data Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MODEL_ARTIFACT__DATA_VERSION = eINSTANCE.getModelArtifact_DataVersion();

		/**
		 * The meta object literal for the '<em><b>Display Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MODEL_ARTIFACT__DISPLAY_NAME = eINSTANCE.getModelArtifact_DisplayName();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.scenario.service.manifest.StorageType <em>Storage Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.scenario.service.manifest.StorageType
		 * @see com.mmxlabs.scenario.service.manifest.impl.ManifestPackageImpl#getStorageType()
		 * @generated
		 */
		EEnum STORAGE_TYPE = eINSTANCE.getStorageType();

	}

} //ManifestPackage
