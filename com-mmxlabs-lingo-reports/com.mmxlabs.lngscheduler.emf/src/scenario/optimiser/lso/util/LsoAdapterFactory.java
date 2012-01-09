/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.optimiser.lso.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

import scenario.NamedObject;
import scenario.ScenarioObject;
import scenario.optimiser.OptimisationSettings;
import scenario.optimiser.lso.*;
import scenario.optimiser.lso.ConstrainedMoveGeneratorSettings;
import scenario.optimiser.lso.LSOSettings;
import scenario.optimiser.lso.LsoPackage;
import scenario.optimiser.lso.MoveGeneratorSettings;
import scenario.optimiser.lso.RandomMoveGeneratorSettings;
import scenario.optimiser.lso.ThresholderSettings;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see scenario.optimiser.lso.LsoPackage
 * @generated
 */
public class LsoAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static LsoPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LsoAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = LsoPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LsoSwitch<Adapter> modelSwitch =
		new LsoSwitch<Adapter>() {
			@Override
			public Adapter caseLSOSettings(LSOSettings object) {
				return createLSOSettingsAdapter();
			}
			@Override
			public Adapter caseThresholderSettings(ThresholderSettings object) {
				return createThresholderSettingsAdapter();
			}
			@Override
			public Adapter caseMoveGeneratorSettings(MoveGeneratorSettings object) {
				return createMoveGeneratorSettingsAdapter();
			}
			@Override
			public Adapter caseRandomMoveGeneratorSettings(RandomMoveGeneratorSettings object) {
				return createRandomMoveGeneratorSettingsAdapter();
			}
			@Override
			public Adapter caseConstrainedMoveGeneratorSettings(ConstrainedMoveGeneratorSettings object) {
				return createConstrainedMoveGeneratorSettingsAdapter();
			}
			@Override
			public Adapter caseScenarioObject(ScenarioObject object) {
				return createScenarioObjectAdapter();
			}
			@Override
			public Adapter caseNamedObject(NamedObject object) {
				return createNamedObjectAdapter();
			}
			@Override
			public Adapter caseOptimisationSettings(OptimisationSettings object) {
				return createOptimisationSettingsAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link scenario.optimiser.lso.LSOSettings <em>LSO Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.optimiser.lso.LSOSettings
	 * @generated
	 */
	public Adapter createLSOSettingsAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.optimiser.lso.ThresholderSettings <em>Thresholder Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.optimiser.lso.ThresholderSettings
	 * @generated
	 */
	public Adapter createThresholderSettingsAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.optimiser.lso.MoveGeneratorSettings <em>Move Generator Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.optimiser.lso.MoveGeneratorSettings
	 * @generated
	 */
	public Adapter createMoveGeneratorSettingsAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.optimiser.lso.RandomMoveGeneratorSettings <em>Random Move Generator Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.optimiser.lso.RandomMoveGeneratorSettings
	 * @generated
	 */
	public Adapter createRandomMoveGeneratorSettingsAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.optimiser.lso.ConstrainedMoveGeneratorSettings <em>Constrained Move Generator Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.optimiser.lso.ConstrainedMoveGeneratorSettings
	 * @generated
	 */
	public Adapter createConstrainedMoveGeneratorSettingsAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.ScenarioObject <em>Object</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.ScenarioObject
	 * @generated
	 */
	public Adapter createScenarioObjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.NamedObject <em>Named Object</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.NamedObject
	 * @generated
	 */
	public Adapter createNamedObjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.optimiser.OptimisationSettings <em>Optimisation Settings</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.optimiser.OptimisationSettings
	 * @generated
	 */
	public Adapter createOptimisationSettingsAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //LsoAdapterFactory
