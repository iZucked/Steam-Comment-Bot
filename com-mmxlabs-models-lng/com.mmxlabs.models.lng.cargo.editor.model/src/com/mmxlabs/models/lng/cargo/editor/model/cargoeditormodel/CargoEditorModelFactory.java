/**
 */
package com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.CargoEditorModelPackage
 * @generated
 */
public interface CargoEditorModelFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	CargoEditorModelFactory eINSTANCE = com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl.CargoEditorModelFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Trades Row</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Trades Row</em>'.
	 * @generated
	 */
	TradesRow createTradesRow();

	/**
	 * Returns a new object of class '<em>Trades Table Root</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Trades Table Root</em>'.
	 * @generated
	 */
	TradesTableRoot createTradesTableRoot();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	CargoEditorModelPackage getCargoEditorModelPackage();

} //CargoEditorModelFactory
