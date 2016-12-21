/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.mmxcore.MMXCorePackage
 * @generated
 */
public interface MMXCoreFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MMXCoreFactory eINSTANCE = com.mmxlabs.models.mmxcore.impl.MMXCoreFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Named Object</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Named Object</em>'.
	 * @generated
	 */
	NamedObject createNamedObject();

	/**
	 * Returns a new object of class '<em>Other Names Object</em>'.
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Other Names Object</em>'.
	 * @generated
	 */
	OtherNamesObject createOtherNamesObject();

	/**
	 * Returns a new object of class '<em>UUID Object</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>UUID Object</em>'.
	 * @generated
	 */
	UUIDObject createUUIDObject();

	/**
	 * Returns a new object of class '<em>MMX Root Object</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>MMX Root Object</em>'.
	 * @generated
	 */
	MMXRootObject createMMXRootObject();

	/**
	 * Returns a new object of class '<em>MMX Result Root</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>MMX Result Root</em>'.
	 * @generated
	 */
	MMXResultRoot createMMXResultRoot();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	MMXCorePackage getMMXCorePackage();

} //MMXCoreFactory
