/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lngdataserver.browser;

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
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see com.mmxlabs.lngdataserver.browser.BrowserFactory
 * @model kind="package"
 * @generated
 */
public interface BrowserPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "browser";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.mmxlabs.com/lngdataserver/browser/1";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "browser";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	BrowserPackage eINSTANCE = com.mmxlabs.lngdataserver.browser.impl.BrowserPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.mmxlabs.lngdataserver.browser.impl.RootNodeImpl <em>Root Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lngdataserver.browser.impl.RootNodeImpl
	 * @see com.mmxlabs.lngdataserver.browser.impl.BrowserPackageImpl#getRootNode()
	 * @generated
	 */
	int ROOT_NODE = 0;

	/**
	 * The number of structural features of the '<em>Root Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROOT_NODE_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Root Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROOT_NODE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.lngdataserver.browser.impl.NodeImpl <em>Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lngdataserver.browser.impl.NodeImpl
	 * @see com.mmxlabs.lngdataserver.browser.impl.BrowserPackageImpl#getNode()
	 * @generated
	 */
	int NODE = 3;

	/**
	 * The feature id for the '<em><b>Display Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__DISPLAY_NAME = 0;

	/**
	 * The feature id for the '<em><b>Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__PARENT = 1;

	/**
	 * The feature id for the '<em><b>Published</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__PUBLISHED = 2;

	/**
	 * The feature id for the '<em><b>Version Identifier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__VERSION_IDENTIFIER = 3;

	/**
	 * The number of structural features of the '<em>Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.lngdataserver.browser.impl.CompositeNodeImpl <em>Composite Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lngdataserver.browser.impl.CompositeNodeImpl
	 * @see com.mmxlabs.lngdataserver.browser.impl.BrowserPackageImpl#getCompositeNode()
	 * @generated
	 */
	int COMPOSITE_NODE = 1;

	/**
	 * The feature id for the '<em><b>Display Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_NODE__DISPLAY_NAME = NODE__DISPLAY_NAME;

	/**
	 * The feature id for the '<em><b>Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_NODE__PARENT = NODE__PARENT;

	/**
	 * The feature id for the '<em><b>Published</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_NODE__PUBLISHED = NODE__PUBLISHED;

	/**
	 * The feature id for the '<em><b>Version Identifier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_NODE__VERSION_IDENTIFIER = NODE__VERSION_IDENTIFIER;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_NODE__CHILDREN = NODE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_NODE__TYPE = NODE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Current</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_NODE__CURRENT = NODE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Action Handler</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_NODE__ACTION_HANDLER = NODE_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Composite Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_NODE_FEATURE_COUNT = NODE_FEATURE_COUNT + 4;

	/**
	 * The number of operations of the '<em>Composite Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_NODE_OPERATION_COUNT = NODE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.lngdataserver.browser.impl.LeafImpl <em>Leaf</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lngdataserver.browser.impl.LeafImpl
	 * @see com.mmxlabs.lngdataserver.browser.impl.BrowserPackageImpl#getLeaf()
	 * @generated
	 */
	int LEAF = 2;

	/**
	 * The feature id for the '<em><b>Display Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEAF__DISPLAY_NAME = NODE__DISPLAY_NAME;

	/**
	 * The feature id for the '<em><b>Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEAF__PARENT = NODE__PARENT;

	/**
	 * The feature id for the '<em><b>Published</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEAF__PUBLISHED = NODE__PUBLISHED;

	/**
	 * The feature id for the '<em><b>Version Identifier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEAF__VERSION_IDENTIFIER = NODE__VERSION_IDENTIFIER;

	/**
	 * The number of structural features of the '<em>Leaf</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEAF_FEATURE_COUNT = NODE_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Leaf</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEAF_OPERATION_COUNT = NODE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '<em>IData Browser Actions Handler</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.lngdataserver.commons.IDataBrowserActionsHandler
	 * @see com.mmxlabs.lngdataserver.browser.impl.BrowserPackageImpl#getIDataBrowserActionsHandler()
	 * @generated
	 */
	int IDATA_BROWSER_ACTIONS_HANDLER = 4;

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.lngdataserver.browser.RootNode <em>Root Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Root Node</em>'.
	 * @see com.mmxlabs.lngdataserver.browser.RootNode
	 * @generated
	 */
	EClass getRootNode();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.lngdataserver.browser.CompositeNode <em>Composite Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Composite Node</em>'.
	 * @see com.mmxlabs.lngdataserver.browser.CompositeNode
	 * @generated
	 */
	EClass getCompositeNode();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.lngdataserver.browser.CompositeNode#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Children</em>'.
	 * @see com.mmxlabs.lngdataserver.browser.CompositeNode#getChildren()
	 * @see #getCompositeNode()
	 * @generated
	 */
	EReference getCompositeNode_Children();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lngdataserver.browser.CompositeNode#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see com.mmxlabs.lngdataserver.browser.CompositeNode#getType()
	 * @see #getCompositeNode()
	 * @generated
	 */
	EAttribute getCompositeNode_Type();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lngdataserver.browser.CompositeNode#getCurrent <em>Current</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Current</em>'.
	 * @see com.mmxlabs.lngdataserver.browser.CompositeNode#getCurrent()
	 * @see #getCompositeNode()
	 * @generated
	 */
	EReference getCompositeNode_Current();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lngdataserver.browser.CompositeNode#getActionHandler <em>Action Handler</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Action Handler</em>'.
	 * @see com.mmxlabs.lngdataserver.browser.CompositeNode#getActionHandler()
	 * @see #getCompositeNode()
	 * @generated
	 */
	EAttribute getCompositeNode_ActionHandler();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.lngdataserver.browser.Leaf <em>Leaf</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Leaf</em>'.
	 * @see com.mmxlabs.lngdataserver.browser.Leaf
	 * @generated
	 */
	EClass getLeaf();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.lngdataserver.browser.Node <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Node</em>'.
	 * @see com.mmxlabs.lngdataserver.browser.Node
	 * @generated
	 */
	EClass getNode();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lngdataserver.browser.Node#getDisplayName <em>Display Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Display Name</em>'.
	 * @see com.mmxlabs.lngdataserver.browser.Node#getDisplayName()
	 * @see #getNode()
	 * @generated
	 */
	EAttribute getNode_DisplayName();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.lngdataserver.browser.Node#getParent <em>Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Parent</em>'.
	 * @see com.mmxlabs.lngdataserver.browser.Node#getParent()
	 * @see #getNode()
	 * @generated
	 */
	EReference getNode_Parent();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lngdataserver.browser.Node#isPublished <em>Published</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Published</em>'.
	 * @see com.mmxlabs.lngdataserver.browser.Node#isPublished()
	 * @see #getNode()
	 * @generated
	 */
	EAttribute getNode_Published();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.lngdataserver.browser.Node#getVersionIdentifier <em>Version Identifier</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version Identifier</em>'.
	 * @see com.mmxlabs.lngdataserver.browser.Node#getVersionIdentifier()
	 * @see #getNode()
	 * @generated
	 */
	EAttribute getNode_VersionIdentifier();

	/**
	 * Returns the meta object for data type '{@link com.mmxlabs.lngdataserver.commons.IDataBrowserActionsHandler <em>IData Browser Actions Handler</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>IData Browser Actions Handler</em>'.
	 * @see com.mmxlabs.lngdataserver.commons.IDataBrowserActionsHandler
	 * @model instanceClass="com.mmxlabs.lngdataserver.commons.IDataBrowserActionsHandler"
	 * @generated
	 */
	EDataType getIDataBrowserActionsHandler();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	BrowserFactory getBrowserFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link com.mmxlabs.lngdataserver.browser.impl.RootNodeImpl <em>Root Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lngdataserver.browser.impl.RootNodeImpl
		 * @see com.mmxlabs.lngdataserver.browser.impl.BrowserPackageImpl#getRootNode()
		 * @generated
		 */
		EClass ROOT_NODE = eINSTANCE.getRootNode();
		/**
		 * The meta object literal for the '{@link com.mmxlabs.lngdataserver.browser.impl.CompositeNodeImpl <em>Composite Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lngdataserver.browser.impl.CompositeNodeImpl
		 * @see com.mmxlabs.lngdataserver.browser.impl.BrowserPackageImpl#getCompositeNode()
		 * @generated
		 */
		EClass COMPOSITE_NODE = eINSTANCE.getCompositeNode();
		/**
		 * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPOSITE_NODE__CHILDREN = eINSTANCE.getCompositeNode_Children();
		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPOSITE_NODE__TYPE = eINSTANCE.getCompositeNode_Type();
		/**
		 * The meta object literal for the '<em><b>Current</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPOSITE_NODE__CURRENT = eINSTANCE.getCompositeNode_Current();
		/**
		 * The meta object literal for the '<em><b>Action Handler</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPOSITE_NODE__ACTION_HANDLER = eINSTANCE.getCompositeNode_ActionHandler();
		/**
		 * The meta object literal for the '{@link com.mmxlabs.lngdataserver.browser.impl.LeafImpl <em>Leaf</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lngdataserver.browser.impl.LeafImpl
		 * @see com.mmxlabs.lngdataserver.browser.impl.BrowserPackageImpl#getLeaf()
		 * @generated
		 */
		EClass LEAF = eINSTANCE.getLeaf();
		/**
		 * The meta object literal for the '{@link com.mmxlabs.lngdataserver.browser.impl.NodeImpl <em>Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lngdataserver.browser.impl.NodeImpl
		 * @see com.mmxlabs.lngdataserver.browser.impl.BrowserPackageImpl#getNode()
		 * @generated
		 */
		EClass NODE = eINSTANCE.getNode();
		/**
		 * The meta object literal for the '<em><b>Display Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NODE__DISPLAY_NAME = eINSTANCE.getNode_DisplayName();
		/**
		 * The meta object literal for the '<em><b>Parent</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NODE__PARENT = eINSTANCE.getNode_Parent();
		/**
		 * The meta object literal for the '<em><b>Published</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NODE__PUBLISHED = eINSTANCE.getNode_Published();
		/**
		 * The meta object literal for the '<em><b>Version Identifier</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NODE__VERSION_IDENTIFIER = eINSTANCE.getNode_VersionIdentifier();
		/**
		 * The meta object literal for the '<em>IData Browser Actions Handler</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.lngdataserver.commons.IDataBrowserActionsHandler
		 * @see com.mmxlabs.lngdataserver.browser.impl.BrowserPackageImpl#getIDataBrowserActionsHandler()
		 * @generated
		 */
		EDataType IDATA_BROWSER_ACTIONS_HANDLER = eINSTANCE.getIDataBrowserActionsHandler();

	}

} //BrowserPackage
