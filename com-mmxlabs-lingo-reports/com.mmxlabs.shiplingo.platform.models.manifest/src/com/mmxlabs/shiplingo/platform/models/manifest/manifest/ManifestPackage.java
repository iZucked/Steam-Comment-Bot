/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.manifest.manifest;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
 * @see com.mmxlabs.shiplingo.platform.models.manifest.manifest.ManifestFactory
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
	String eNS_URI = "http://www.mmxlabs.com/models/lng/demo-manifest/1/";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "manifest";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ManifestPackage eINSTANCE = com.mmxlabs.shiplingo.platform.models.manifest.manifest.impl.ManifestPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.mmxlabs.shiplingo.platform.models.manifest.manifest.impl.EntryImpl <em>Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.shiplingo.platform.models.manifest.manifest.impl.EntryImpl
	 * @see com.mmxlabs.shiplingo.platform.models.manifest.manifest.impl.ManifestPackageImpl#getEntry()
	 * @generated
	 */
	int ENTRY = 0;

	/**
	 * The feature id for the '<em><b>Sub Model Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTRY__SUB_MODEL_KEY = 0;

	/**
	 * The feature id for the '<em><b>Relative Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTRY__RELATIVE_PATH = 1;

	/**
	 * The number of structural features of the '<em>Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTRY_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.shiplingo.platform.models.manifest.manifest.impl.ManifestImpl <em>Manifest</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.shiplingo.platform.models.manifest.manifest.impl.ManifestImpl
	 * @see com.mmxlabs.shiplingo.platform.models.manifest.manifest.impl.ManifestPackageImpl#getManifest()
	 * @generated
	 */
	int MANIFEST = 1;

	/**
	 * The feature id for the '<em><b>Entries</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MANIFEST__ENTRIES = 0;

	/**
	 * The feature id for the '<em><b>Current Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MANIFEST__CURRENT_VERSION = 1;

	/**
	 * The number of structural features of the '<em>Manifest</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MANIFEST_FEATURE_COUNT = 2;


	/**
	 * Returns the meta object for class '{@link com.mmxlabs.shiplingo.platform.models.manifest.manifest.Entry <em>Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Entry</em>'.
	 * @see com.mmxlabs.shiplingo.platform.models.manifest.manifest.Entry
	 * @generated
	 */
	EClass getEntry();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.shiplingo.platform.models.manifest.manifest.Entry#getSubModelKey <em>Sub Model Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Sub Model Key</em>'.
	 * @see com.mmxlabs.shiplingo.platform.models.manifest.manifest.Entry#getSubModelKey()
	 * @see #getEntry()
	 * @generated
	 */
	EAttribute getEntry_SubModelKey();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.shiplingo.platform.models.manifest.manifest.Entry#getRelativePath <em>Relative Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Relative Path</em>'.
	 * @see com.mmxlabs.shiplingo.platform.models.manifest.manifest.Entry#getRelativePath()
	 * @see #getEntry()
	 * @generated
	 */
	EAttribute getEntry_RelativePath();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.shiplingo.platform.models.manifest.manifest.Manifest <em>Manifest</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Manifest</em>'.
	 * @see com.mmxlabs.shiplingo.platform.models.manifest.manifest.Manifest
	 * @generated
	 */
	EClass getManifest();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.shiplingo.platform.models.manifest.manifest.Manifest#getEntries <em>Entries</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Entries</em>'.
	 * @see com.mmxlabs.shiplingo.platform.models.manifest.manifest.Manifest#getEntries()
	 * @see #getManifest()
	 * @generated
	 */
	EReference getManifest_Entries();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.shiplingo.platform.models.manifest.manifest.Manifest#getCurrentVersion <em>Current Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Current Version</em>'.
	 * @see com.mmxlabs.shiplingo.platform.models.manifest.manifest.Manifest#getCurrentVersion()
	 * @see #getManifest()
	 * @generated
	 */
	EAttribute getManifest_CurrentVersion();

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
		 * The meta object literal for the '{@link com.mmxlabs.shiplingo.platform.models.manifest.manifest.impl.EntryImpl <em>Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.shiplingo.platform.models.manifest.manifest.impl.EntryImpl
		 * @see com.mmxlabs.shiplingo.platform.models.manifest.manifest.impl.ManifestPackageImpl#getEntry()
		 * @generated
		 */
		EClass ENTRY = eINSTANCE.getEntry();

		/**
		 * The meta object literal for the '<em><b>Sub Model Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENTRY__SUB_MODEL_KEY = eINSTANCE.getEntry_SubModelKey();

		/**
		 * The meta object literal for the '<em><b>Relative Path</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENTRY__RELATIVE_PATH = eINSTANCE.getEntry_RelativePath();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.shiplingo.platform.models.manifest.manifest.impl.ManifestImpl <em>Manifest</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.shiplingo.platform.models.manifest.manifest.impl.ManifestImpl
		 * @see com.mmxlabs.shiplingo.platform.models.manifest.manifest.impl.ManifestPackageImpl#getManifest()
		 * @generated
		 */
		EClass MANIFEST = eINSTANCE.getManifest();

		/**
		 * The meta object literal for the '<em><b>Entries</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MANIFEST__ENTRIES = eINSTANCE.getManifest_Entries();

		/**
		 * The meta object literal for the '<em><b>Current Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MANIFEST__CURRENT_VERSION = eINSTANCE.getManifest_CurrentVersion();

	}

} //ManifestPackage
