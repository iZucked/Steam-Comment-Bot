/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.optimiser.lso;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see scenario.optimiser.lso.LsoPackage
 * @generated
 */
public interface LsoFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	LsoFactory eINSTANCE = scenario.optimiser.lso.impl.LsoFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>LSO Settings</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>LSO Settings</em>'.
	 * @generated
	 */
	LSOSettings createLSOSettings();

	/**
	 * Returns a new object of class '<em>Thresholder Settings</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Thresholder Settings</em>'.
	 * @generated
	 */
	ThresholderSettings createThresholderSettings();

	/**
	 * Returns a new object of class '<em>Move Generator Settings</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Move Generator Settings</em>'.
	 * @generated
	 */
	MoveGeneratorSettings createMoveGeneratorSettings();

	/**
	 * Returns a new object of class '<em>Random Move Generator Settings</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Random Move Generator Settings</em>'.
	 * @generated
	 */
	RandomMoveGeneratorSettings createRandomMoveGeneratorSettings();

	/**
	 * Returns a new object of class '<em>Constrained Move Generator Settings</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Constrained Move Generator Settings</em>'.
	 * @generated
	 */
	ConstrainedMoveGeneratorSettings createConstrainedMoveGeneratorSettings();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	LsoPackage getLsoPackage();

} //LsoFactory
