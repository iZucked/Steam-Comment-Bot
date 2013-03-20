/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore.impl;

import com.mmxlabs.models.mmxcore.MMXCoreFactory;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.MMXProxy;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.MMXSubModel;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MMXCorePackageImpl extends EPackageImpl implements MMXCorePackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mmxObjectEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass namedObjectEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass uuidObjectEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mmxProxyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mmxRootObjectEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mmxSubModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType mmxResourceEDataType = null;

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
	 * @see com.mmxlabs.models.mmxcore.MMXCorePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private MMXCorePackageImpl() {
		super(eNS_URI, MMXCoreFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link MMXCorePackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static MMXCorePackage init() {
		if (isInited) return (MMXCorePackage)EPackage.Registry.INSTANCE.getEPackage(MMXCorePackage.eNS_URI);

		// Obtain or create and register package
		MMXCorePackageImpl theMMXCorePackage = (MMXCorePackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof MMXCorePackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new MMXCorePackageImpl());

		isInited = true;

		// Create package meta-data objects
		theMMXCorePackage.createPackageContents();

		// Initialize created meta-data
		theMMXCorePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theMMXCorePackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(MMXCorePackage.eNS_URI, theMMXCorePackage);
		return theMMXCorePackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMMXObject() {
		return mmxObjectEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMMXObject_Extensions() {
		return (EReference)mmxObjectEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMMXObject_Proxies() {
		return (EReference)mmxObjectEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getMMXObject__MakeProxies() {
		return mmxObjectEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getMMXObject__ResolveProxies__Map() {
		return mmxObjectEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getMMXObject__RestoreProxies() {
		return mmxObjectEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getMMXObject__CollectUUIDObjects__Map() {
		return mmxObjectEClass.getEOperations().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getMMXObject__CollectUUIDObjects() {
		return mmxObjectEClass.getEOperations().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getMMXObject__GetUnsetValue__EStructuralFeature() {
		return mmxObjectEClass.getEOperations().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getMMXObject__EGetWithDefault__EStructuralFeature() {
		return mmxObjectEClass.getEOperations().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getMMXObject__EContainerOp() {
		return mmxObjectEClass.getEOperations().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNamedObject() {
		return namedObjectEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNamedObject_Name() {
		return (EAttribute)namedObjectEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.2
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNamedObject_OtherNames() {
		return (EAttribute)namedObjectEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUUIDObject() {
		return uuidObjectEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUUIDObject_Uuid() {
		return (EAttribute)uuidObjectEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMMXProxy() {
		return mmxProxyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMMXProxy_ReferentID() {
		return (EAttribute)mmxProxyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMMXProxy_ResolvedReferent() {
		return (EReference)mmxProxyEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMMXProxy_Reference() {
		return (EReference)mmxProxyEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMMXProxy_ReferentOwner() {
		return (EAttribute)mmxProxyEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMMXProxy_Index() {
		return (EAttribute)mmxProxyEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMMXProxy_ReferentName() {
		return (EAttribute)mmxProxyEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMMXRootObject() {
		return mmxRootObjectEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMMXRootObject_SubModels() {
		return (EReference)mmxRootObjectEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMMXRootObject_Version() {
		return (EAttribute)mmxRootObjectEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getMMXRootObject__AddSubModel__UUIDObject() {
		return mmxRootObjectEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getMMXRootObject__RestoreSubModels() {
		return mmxRootObjectEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getMMXRootObject__GetSubModel__Class() {
		return mmxRootObjectEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMMXSubModel() {
		return mmxSubModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMMXSubModel_SubModelInstance() {
		return (EReference)mmxSubModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMMXSubModel_OriginalResource() {
		return (EAttribute)mmxSubModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getMMXResource() {
		return mmxResourceEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MMXCoreFactory getMMXCoreFactory() {
		return (MMXCoreFactory)getEFactoryInstance();
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
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		mmxObjectEClass = createEClass(MMX_OBJECT);
		createEReference(mmxObjectEClass, MMX_OBJECT__EXTENSIONS);
		createEReference(mmxObjectEClass, MMX_OBJECT__PROXIES);
		createEOperation(mmxObjectEClass, MMX_OBJECT___MAKE_PROXIES);
		createEOperation(mmxObjectEClass, MMX_OBJECT___RESOLVE_PROXIES__MAP);
		createEOperation(mmxObjectEClass, MMX_OBJECT___RESTORE_PROXIES);
		createEOperation(mmxObjectEClass, MMX_OBJECT___COLLECT_UUID_OBJECTS__MAP);
		createEOperation(mmxObjectEClass, MMX_OBJECT___COLLECT_UUID_OBJECTS);
		createEOperation(mmxObjectEClass, MMX_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE);
		createEOperation(mmxObjectEClass, MMX_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE);
		createEOperation(mmxObjectEClass, MMX_OBJECT___ECONTAINER_OP);

		namedObjectEClass = createEClass(NAMED_OBJECT);
		createEAttribute(namedObjectEClass, NAMED_OBJECT__NAME);
		createEAttribute(namedObjectEClass, NAMED_OBJECT__OTHER_NAMES);

		uuidObjectEClass = createEClass(UUID_OBJECT);
		createEAttribute(uuidObjectEClass, UUID_OBJECT__UUID);

		mmxProxyEClass = createEClass(MMX_PROXY);
		createEAttribute(mmxProxyEClass, MMX_PROXY__REFERENT_ID);
		createEReference(mmxProxyEClass, MMX_PROXY__RESOLVED_REFERENT);
		createEReference(mmxProxyEClass, MMX_PROXY__REFERENCE);
		createEAttribute(mmxProxyEClass, MMX_PROXY__REFERENT_OWNER);
		createEAttribute(mmxProxyEClass, MMX_PROXY__INDEX);
		createEAttribute(mmxProxyEClass, MMX_PROXY__REFERENT_NAME);

		mmxRootObjectEClass = createEClass(MMX_ROOT_OBJECT);
		createEReference(mmxRootObjectEClass, MMX_ROOT_OBJECT__SUB_MODELS);
		createEAttribute(mmxRootObjectEClass, MMX_ROOT_OBJECT__VERSION);
		createEOperation(mmxRootObjectEClass, MMX_ROOT_OBJECT___ADD_SUB_MODEL__UUIDOBJECT);
		createEOperation(mmxRootObjectEClass, MMX_ROOT_OBJECT___RESTORE_SUB_MODELS);
		createEOperation(mmxRootObjectEClass, MMX_ROOT_OBJECT___GET_SUB_MODEL__CLASS);

		mmxSubModelEClass = createEClass(MMX_SUB_MODEL);
		createEReference(mmxSubModelEClass, MMX_SUB_MODEL__SUB_MODEL_INSTANCE);
		createEAttribute(mmxSubModelEClass, MMX_SUB_MODEL__ORIGINAL_RESOURCE);

		// Create data types
		mmxResourceEDataType = createEDataType(MMX_RESOURCE);
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
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		namedObjectEClass.getESuperTypes().add(this.getMMXObject());
		uuidObjectEClass.getESuperTypes().add(this.getMMXObject());
		mmxRootObjectEClass.getESuperTypes().add(this.getUUIDObject());
		mmxRootObjectEClass.getESuperTypes().add(this.getNamedObject());
		mmxSubModelEClass.getESuperTypes().add(this.getMMXObject());

		// Initialize classes, features, and operations; add parameters
		initEClass(mmxObjectEClass, MMXObject.class, "MMXObject", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMMXObject_Extensions(), this.getUUIDObject(), null, "extensions", null, 0, -1, MMXObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		initEReference(getMMXObject_Proxies(), this.getMMXProxy(), null, "proxies", null, 0, -1, MMXObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getMMXObject__MakeProxies(), null, "makeProxies", 1, 1, IS_UNIQUE, IS_ORDERED);

		EOperation op = initEOperation(getMMXObject__ResolveProxies__Map(), null, "resolveProxies", 1, 1, IS_UNIQUE, IS_ORDERED);
		EGenericType g1 = createEGenericType(ecorePackage.getEMap());
		EGenericType g2 = createEGenericType(ecorePackage.getEString());
		g1.getETypeArguments().add(g2);
		g2 = createEGenericType(this.getUUIDObject());
		g1.getETypeArguments().add(g2);
		addEParameter(op, g1, "objectsByUUID", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getMMXObject__RestoreProxies(), null, "restoreProxies", 1, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getMMXObject__CollectUUIDObjects__Map(), null, "collectUUIDObjects", 1, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(ecorePackage.getEMap());
		g2 = createEGenericType(ecorePackage.getEString());
		g1.getETypeArguments().add(g2);
		g2 = createEGenericType(this.getUUIDObject());
		g1.getETypeArguments().add(g2);
		addEParameter(op, g1, "objectsByUUID", 1, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getMMXObject__CollectUUIDObjects(), null, "collectUUIDObjects", 1, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(ecorePackage.getEMap());
		g2 = createEGenericType(ecorePackage.getEString());
		g1.getETypeArguments().add(g2);
		g2 = createEGenericType(this.getUUIDObject());
		g1.getETypeArguments().add(g2);
		initEOperation(op, g1);

		op = initEOperation(getMMXObject__GetUnsetValue__EStructuralFeature(), ecorePackage.getEJavaObject(), "getUnsetValue", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEStructuralFeature(), "feature", 1, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getMMXObject__EGetWithDefault__EStructuralFeature(), ecorePackage.getEJavaObject(), "eGetWithDefault", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEStructuralFeature(), "feature", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getMMXObject__EContainerOp(), ecorePackage.getEObject(), "eContainerOp", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(namedObjectEClass, NamedObject.class, "NamedObject", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getNamedObject_Name(), ecorePackage.getEString(), "name", null, 1, 1, NamedObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNamedObject_OtherNames(), ecorePackage.getEString(), "otherNames", null, 0, -1, NamedObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(uuidObjectEClass, UUIDObject.class, "UUIDObject", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getUUIDObject_Uuid(), ecorePackage.getEString(), "uuid", null, 1, 1, UUIDObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(mmxProxyEClass, MMXProxy.class, "MMXProxy", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMMXProxy_ReferentID(), ecorePackage.getEString(), "referentID", null, 1, 1, MMXProxy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMMXProxy_ResolvedReferent(), this.getUUIDObject(), null, "resolvedReferent", null, 1, 1, MMXProxy.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMMXProxy_Reference(), ecorePackage.getEReference(), null, "reference", null, 1, 1, MMXProxy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMMXProxy_ReferentOwner(), ecorePackage.getEString(), "referentOwner", null, 1, 1, MMXProxy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMMXProxy_Index(), ecorePackage.getEInt(), "index", null, 1, 1, MMXProxy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMMXProxy_ReferentName(), ecorePackage.getEString(), "referentName", null, 1, 1, MMXProxy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(mmxRootObjectEClass, MMXRootObject.class, "MMXRootObject", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMMXRootObject_SubModels(), this.getMMXSubModel(), null, "subModels", null, 0, -1, MMXRootObject.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMMXRootObject_Version(), ecorePackage.getEInt(), "version", "0", 1, 1, MMXRootObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = initEOperation(getMMXRootObject__AddSubModel__UUIDObject(), null, "addSubModel", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getUUIDObject(), "subModel", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getMMXRootObject__RestoreSubModels(), null, "restoreSubModels", 1, 1, IS_UNIQUE, IS_ORDERED);

		op = initEOperation(getMMXRootObject__GetSubModel__Class(), null, "getSubModel", 1, 1, IS_UNIQUE, IS_ORDERED);
		ETypeParameter t1 = addETypeParameter(op, "T");
		g1 = createEGenericType(ecorePackage.getEJavaClass());
		g2 = createEGenericType(t1);
		g1.getETypeArguments().add(g2);
		addEParameter(op, g1, "subModelClass", 0, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(t1);
		initEOperation(op, g1);

		initEClass(mmxSubModelEClass, MMXSubModel.class, "MMXSubModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMMXSubModel_SubModelInstance(), this.getUUIDObject(), null, "subModelInstance", null, 1, 1, MMXSubModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMMXSubModel_OriginalResource(), this.getMMXResource(), "originalResource", null, 1, 1, MMXSubModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize data types
		initEDataType(mmxResourceEDataType, Resource.class, "MMXResource", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //MMXCorePackageImpl
