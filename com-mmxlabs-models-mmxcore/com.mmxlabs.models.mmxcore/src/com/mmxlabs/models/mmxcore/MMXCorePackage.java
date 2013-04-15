/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
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
 * @see com.mmxlabs.models.mmxcore.MMXCoreFactory
 * @model kind="package"
 * @generated
 */
public interface MMXCorePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "mmxcore";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.mmxlabs.com/models/mmxcore/1/";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "models.mmxcore";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MMXCorePackage eINSTANCE = com.mmxlabs.models.mmxcore.impl.MMXCorePackageImpl.init();

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.mmxcore.impl.MMXObjectImpl <em>MMX Object</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.mmxcore.impl.MMXObjectImpl
	 * @see com.mmxlabs.models.mmxcore.impl.MMXCorePackageImpl#getMMXObject()
	 * @generated
	 */
	int MMX_OBJECT = 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MMX_OBJECT__EXTENSIONS = 0;

	/**
	 * The number of structural features of the '<em>MMX Object</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MMX_OBJECT_FEATURE_COUNT = 1;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MMX_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = 0;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MMX_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = 1;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MMX_OBJECT___ECONTAINER_OP = 2;

	/**
	 * The number of operations of the '<em>MMX Object</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MMX_OBJECT_OPERATION_COUNT = 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.mmxcore.impl.NamedObjectImpl <em>Named Object</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.mmxcore.impl.NamedObjectImpl
	 * @see com.mmxlabs.models.mmxcore.impl.MMXCorePackageImpl#getNamedObject()
	 * @generated
	 */
	int NAMED_OBJECT = 1;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_OBJECT__EXTENSIONS = MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_OBJECT__NAME = MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.2
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_OBJECT__OTHER_NAMES = MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Named Object</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_OBJECT_FEATURE_COUNT = MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMX_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMX_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_OBJECT___ECONTAINER_OP = MMX_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>Named Object</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_OBJECT_OPERATION_COUNT = MMX_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl <em>UUID Object</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl
	 * @see com.mmxlabs.models.mmxcore.impl.MMXCorePackageImpl#getUUIDObject()
	 * @generated
	 */
	int UUID_OBJECT = 2;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UUID_OBJECT__EXTENSIONS = MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UUID_OBJECT__UUID = MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>UUID Object</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UUID_OBJECT_FEATURE_COUNT = MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UUID_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMX_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UUID_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMX_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UUID_OBJECT___ECONTAINER_OP = MMX_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>UUID Object</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UUID_OBJECT_OPERATION_COUNT = MMX_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.mmxcore.impl.MMXRootObjectImpl <em>MMX Root Object</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.mmxcore.impl.MMXRootObjectImpl
	 * @see com.mmxlabs.models.mmxcore.impl.MMXCorePackageImpl#getMMXRootObject()
	 * @generated
	 */
	int MMX_ROOT_OBJECT = 3;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MMX_ROOT_OBJECT__EXTENSIONS = UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MMX_ROOT_OBJECT__UUID = UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MMX_ROOT_OBJECT__NAME = UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.2
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MMX_ROOT_OBJECT__OTHER_NAMES = UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Sub Models</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MMX_ROOT_OBJECT__SUB_MODELS = UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>MMX Root Object</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MMX_ROOT_OBJECT_FEATURE_COUNT = UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MMX_ROOT_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = UUID_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MMX_ROOT_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = UUID_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MMX_ROOT_OBJECT___ECONTAINER_OP = UUID_OBJECT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Add Sub Model</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MMX_ROOT_OBJECT___ADD_SUB_MODEL__UUIDOBJECT = UUID_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Sub Model</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MMX_ROOT_OBJECT___GET_SUB_MODEL__CLASS = UUID_OBJECT_OPERATION_COUNT + 1;

	/**
	 * The number of operations of the '<em>MMX Root Object</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MMX_ROOT_OBJECT_OPERATION_COUNT = UUID_OBJECT_OPERATION_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.mmxcore.impl.MMXSubModelImpl <em>MMX Sub Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.mmxcore.impl.MMXSubModelImpl
	 * @see com.mmxlabs.models.mmxcore.impl.MMXCorePackageImpl#getMMXSubModel()
	 * @generated
	 */
	int MMX_SUB_MODEL = 4;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MMX_SUB_MODEL__EXTENSIONS = MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Sub Model Instance</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MMX_SUB_MODEL__SUB_MODEL_INSTANCE = MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>MMX Sub Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MMX_SUB_MODEL_FEATURE_COUNT = MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MMX_SUB_MODEL___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMX_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MMX_SUB_MODEL___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMX_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MMX_SUB_MODEL___ECONTAINER_OP = MMX_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>MMX Sub Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MMX_SUB_MODEL_OPERATION_COUNT = MMX_OBJECT_OPERATION_COUNT + 0;

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.mmxcore.MMXObject <em>MMX Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>MMX Object</em>'.
	 * @see com.mmxlabs.models.mmxcore.MMXObject
	 * @generated
	 */
	EClass getMMXObject();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.mmxcore.MMXObject#getExtensions <em>Extensions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Extensions</em>'.
	 * @see com.mmxlabs.models.mmxcore.MMXObject#getExtensions()
	 * @see #getMMXObject()
	 * @generated
	 */
	EReference getMMXObject_Extensions();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.mmxcore.MMXObject#getUnsetValue(org.eclipse.emf.ecore.EStructuralFeature) <em>Get Unset Value</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Unset Value</em>' operation.
	 * @see com.mmxlabs.models.mmxcore.MMXObject#getUnsetValue(org.eclipse.emf.ecore.EStructuralFeature)
	 * @generated
	 */
	EOperation getMMXObject__GetUnsetValue__EStructuralFeature();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.mmxcore.MMXObject#eGetWithDefault(org.eclipse.emf.ecore.EStructuralFeature) <em>EGet With Default</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>EGet With Default</em>' operation.
	 * @see com.mmxlabs.models.mmxcore.MMXObject#eGetWithDefault(org.eclipse.emf.ecore.EStructuralFeature)
	 * @generated
	 */
	EOperation getMMXObject__EGetWithDefault__EStructuralFeature();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.mmxcore.MMXObject#eContainerOp() <em>EContainer Op</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>EContainer Op</em>' operation.
	 * @see com.mmxlabs.models.mmxcore.MMXObject#eContainerOp()
	 * @generated
	 */
	EOperation getMMXObject__EContainerOp();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.mmxcore.NamedObject <em>Named Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Named Object</em>'.
	 * @see com.mmxlabs.models.mmxcore.NamedObject
	 * @generated
	 */
	EClass getNamedObject();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.mmxcore.NamedObject#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see com.mmxlabs.models.mmxcore.NamedObject#getName()
	 * @see #getNamedObject()
	 * @generated
	 */
	EAttribute getNamedObject_Name();

	/**
	 * Returns the meta object for the attribute list '{@link com.mmxlabs.models.mmxcore.NamedObject#getOtherNames <em>Other Names</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.2
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Other Names</em>'.
	 * @see com.mmxlabs.models.mmxcore.NamedObject#getOtherNames()
	 * @see #getNamedObject()
	 * @generated
	 */
	EAttribute getNamedObject_OtherNames();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.mmxcore.UUIDObject <em>UUID Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UUID Object</em>'.
	 * @see com.mmxlabs.models.mmxcore.UUIDObject
	 * @generated
	 */
	EClass getUUIDObject();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.mmxcore.UUIDObject#getUuid <em>Uuid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Uuid</em>'.
	 * @see com.mmxlabs.models.mmxcore.UUIDObject#getUuid()
	 * @see #getUUIDObject()
	 * @generated
	 */
	EAttribute getUUIDObject_Uuid();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.mmxcore.MMXRootObject <em>MMX Root Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>MMX Root Object</em>'.
	 * @see com.mmxlabs.models.mmxcore.MMXRootObject
	 * @generated
	 */
	EClass getMMXRootObject();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.mmxcore.MMXRootObject#getSubModels <em>Sub Models</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Sub Models</em>'.
	 * @see com.mmxlabs.models.mmxcore.MMXRootObject#getSubModels()
	 * @see #getMMXRootObject()
	 * @generated
	 */
	EReference getMMXRootObject_SubModels();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.mmxcore.MMXRootObject#addSubModel(com.mmxlabs.models.mmxcore.UUIDObject) <em>Add Sub Model</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Add Sub Model</em>' operation.
	 * @see com.mmxlabs.models.mmxcore.MMXRootObject#addSubModel(com.mmxlabs.models.mmxcore.UUIDObject)
	 * @generated
	 */
	EOperation getMMXRootObject__AddSubModel__UUIDObject();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.mmxcore.MMXRootObject#getSubModel(java.lang.Class) <em>Get Sub Model</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Sub Model</em>' operation.
	 * @see com.mmxlabs.models.mmxcore.MMXRootObject#getSubModel(java.lang.Class)
	 * @generated
	 */
	EOperation getMMXRootObject__GetSubModel__Class();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.mmxcore.MMXSubModel <em>MMX Sub Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>MMX Sub Model</em>'.
	 * @see com.mmxlabs.models.mmxcore.MMXSubModel
	 * @generated
	 */
	EClass getMMXSubModel();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.mmxcore.MMXSubModel#getSubModelInstance <em>Sub Model Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Sub Model Instance</em>'.
	 * @see com.mmxlabs.models.mmxcore.MMXSubModel#getSubModelInstance()
	 * @see #getMMXSubModel()
	 * @generated
	 */
	EReference getMMXSubModel_SubModelInstance();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	MMXCoreFactory getMMXCoreFactory();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.mmxcore.impl.MMXObjectImpl <em>MMX Object</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.mmxcore.impl.MMXObjectImpl
		 * @see com.mmxlabs.models.mmxcore.impl.MMXCorePackageImpl#getMMXObject()
		 * @generated
		 */
		EClass MMX_OBJECT = eINSTANCE.getMMXObject();

		/**
		 * The meta object literal for the '<em><b>Extensions</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MMX_OBJECT__EXTENSIONS = eINSTANCE.getMMXObject_Extensions();

		/**
		 * The meta object literal for the '<em><b>Get Unset Value</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation MMX_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = eINSTANCE.getMMXObject__GetUnsetValue__EStructuralFeature();

		/**
		 * The meta object literal for the '<em><b>EGet With Default</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation MMX_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = eINSTANCE.getMMXObject__EGetWithDefault__EStructuralFeature();

		/**
		 * The meta object literal for the '<em><b>EContainer Op</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation MMX_OBJECT___ECONTAINER_OP = eINSTANCE.getMMXObject__EContainerOp();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.mmxcore.impl.NamedObjectImpl <em>Named Object</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.mmxcore.impl.NamedObjectImpl
		 * @see com.mmxlabs.models.mmxcore.impl.MMXCorePackageImpl#getNamedObject()
		 * @generated
		 */
		EClass NAMED_OBJECT = eINSTANCE.getNamedObject();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NAMED_OBJECT__NAME = eINSTANCE.getNamedObject_Name();

		/**
		 * The meta object literal for the '<em><b>Other Names</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * @since 2.2
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NAMED_OBJECT__OTHER_NAMES = eINSTANCE.getNamedObject_OtherNames();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl <em>UUID Object</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl
		 * @see com.mmxlabs.models.mmxcore.impl.MMXCorePackageImpl#getUUIDObject()
		 * @generated
		 */
		EClass UUID_OBJECT = eINSTANCE.getUUIDObject();

		/**
		 * The meta object literal for the '<em><b>Uuid</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UUID_OBJECT__UUID = eINSTANCE.getUUIDObject_Uuid();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.mmxcore.impl.MMXRootObjectImpl <em>MMX Root Object</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.mmxcore.impl.MMXRootObjectImpl
		 * @see com.mmxlabs.models.mmxcore.impl.MMXCorePackageImpl#getMMXRootObject()
		 * @generated
		 */
		EClass MMX_ROOT_OBJECT = eINSTANCE.getMMXRootObject();

		/**
		 * The meta object literal for the '<em><b>Sub Models</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MMX_ROOT_OBJECT__SUB_MODELS = eINSTANCE.getMMXRootObject_SubModels();

		/**
		 * The meta object literal for the '<em><b>Add Sub Model</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation MMX_ROOT_OBJECT___ADD_SUB_MODEL__UUIDOBJECT = eINSTANCE.getMMXRootObject__AddSubModel__UUIDObject();

		/**
		 * The meta object literal for the '<em><b>Get Sub Model</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation MMX_ROOT_OBJECT___GET_SUB_MODEL__CLASS = eINSTANCE.getMMXRootObject__GetSubModel__Class();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.mmxcore.impl.MMXSubModelImpl <em>MMX Sub Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.mmxcore.impl.MMXSubModelImpl
		 * @see com.mmxlabs.models.mmxcore.impl.MMXCorePackageImpl#getMMXSubModel()
		 * @generated
		 */
		EClass MMX_SUB_MODEL = eINSTANCE.getMMXSubModel();

		/**
		 * The meta object literal for the '<em><b>Sub Model Instance</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MMX_SUB_MODEL__SUB_MODEL_INSTANCE = eINSTANCE.getMMXSubModel_SubModelInstance();

	}

} //MMXCorePackage
