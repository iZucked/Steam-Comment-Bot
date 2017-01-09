/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore.impl;

import com.mmxlabs.models.mmxcore.*;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import com.mmxlabs.models.mmxcore.MMXCoreFactory;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.OtherNamesObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MMXCoreFactoryImpl extends EFactoryImpl implements MMXCoreFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static MMXCoreFactory init() {
		try {
			MMXCoreFactory theMMXCoreFactory = (MMXCoreFactory)EPackage.Registry.INSTANCE.getEFactory(MMXCorePackage.eNS_URI);
			if (theMMXCoreFactory != null) {
				return theMMXCoreFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new MMXCoreFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MMXCoreFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case MMXCorePackage.NAMED_OBJECT: return createNamedObject();
			case MMXCorePackage.OTHER_NAMES_OBJECT: return createOtherNamesObject();
			case MMXCorePackage.UUID_OBJECT: return createUUIDObject();
			case MMXCorePackage.MMX_ROOT_OBJECT: return createMMXRootObject();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NamedObject createNamedObject() {
		NamedObjectImpl namedObject = new NamedObjectImpl();
		return namedObject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OtherNamesObject createOtherNamesObject() {
		OtherNamesObjectImpl otherNamesObject = new OtherNamesObjectImpl();
		return otherNamesObject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UUIDObject createUUIDObject() {
		UUIDObjectImpl uuidObject = new UUIDObjectImpl();
		return uuidObject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MMXRootObject createMMXRootObject() {
		MMXRootObjectImpl mmxRootObject = new MMXRootObjectImpl();
		return mmxRootObject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MMXCorePackage getMMXCorePackage() {
		return (MMXCorePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static MMXCorePackage getPackage() {
		return MMXCorePackage.eINSTANCE;
	}

} //MMXCoreFactoryImpl
