/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.manifest;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

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
	 * The feature id for the '<em><b>Dependency UUI Ds</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MANIFEST__DEPENDENCY_UUI_DS = 1;

	/**
	 * The feature id for the '<em><b>UUID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MANIFEST__UUID = 2;

	/**
	 * The feature id for the '<em><b>Scenario Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MANIFEST__SCENARIO_TYPE = 3;

	/**
	 * The feature id for the '<em><b>Scenario Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MANIFEST__SCENARIO_VERSION = 4;

	/**
	 * The feature id for the '<em><b>Version Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MANIFEST__VERSION_CONTEXT = 5;

	/**
	 * The feature id for the '<em><b>Client Scenario Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MANIFEST__CLIENT_SCENARIO_VERSION = 6;

	/**
	 * The feature id for the '<em><b>Client Version Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MANIFEST__CLIENT_VERSION_CONTEXT = 7;

	/**
	 * The number of structural features of the '<em>Manifest</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MANIFEST_FEATURE_COUNT = 8;


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
	 * Returns the meta object for the attribute list '{@link com.mmxlabs.scenario.service.manifest.Manifest#getDependencyUUIDs <em>Dependency UUI Ds</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Dependency UUI Ds</em>'.
	 * @see com.mmxlabs.scenario.service.manifest.Manifest#getDependencyUUIDs()
	 * @see #getManifest()
	 * @generated
	 */
	EAttribute getManifest_DependencyUUIDs();

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
		 * The meta object literal for the '<em><b>Dependency UUI Ds</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MANIFEST__DEPENDENCY_UUI_DS = eINSTANCE.getManifest_DependencyUUIDs();

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

	}

} //ManifestPackage
