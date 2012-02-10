/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.optimiser.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

import scenario.NamedObject;
import scenario.ScenarioObject;
import scenario.optimiser.*;
import scenario.optimiser.Constraint;
import scenario.optimiser.Discount;
import scenario.optimiser.DiscountCurve;
import scenario.optimiser.Objective;
import scenario.optimiser.Optimisation;
import scenario.optimiser.OptimisationSettings;
import scenario.optimiser.OptimiserPackage;

/**
 * <!-- begin-user-doc --> The <b>Adapter Factory</b> for the model. It provides an adapter <code>createXXX</code> method for each class of the model. <!-- end-user-doc -->
 * @see scenario.optimiser.OptimiserPackage
 * @generated
 */
public class OptimiserAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected static OptimiserPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public OptimiserAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = OptimiserPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc --> This implementation returns <code>true</code> if the object is either the model's package or is an
	 * instance object of the model. <!-- end-user-doc -->
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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected OptimiserSwitch<Adapter> modelSwitch = new OptimiserSwitch<Adapter>() {
			@Override
			public Adapter caseOptimisationSettings(OptimisationSettings object) {
				return createOptimisationSettingsAdapter();
			}
			@Override
			public Adapter caseOptimisation(Optimisation object) {
				return createOptimisationAdapter();
			}
			@Override
			public Adapter caseConstraint(Constraint object) {
				return createConstraintAdapter();
			}
			@Override
			public Adapter caseObjective(Objective object) {
				return createObjectiveAdapter();
			}
			@Override
			public Adapter caseDiscountCurve(DiscountCurve object) {
				return createDiscountCurveAdapter();
			}
			@Override
			public Adapter caseDiscount(Discount object) {
				return createDiscountAdapter();
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
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.optimiser.OptimisationSettings <em>Optimisation Settings</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null
	 * so that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.optimiser.OptimisationSettings
	 * @generated
	 */
	public Adapter createOptimisationSettingsAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.optimiser.Optimisation <em>Optimisation</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can
	 * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.optimiser.Optimisation
	 * @generated
	 */
	public Adapter createOptimisationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.optimiser.Constraint <em>Constraint</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily
	 * ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.optimiser.Constraint
	 * @generated
	 */
	public Adapter createConstraintAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.optimiser.Objective <em>Objective</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily
	 * ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.optimiser.Objective
	 * @generated
	 */
	public Adapter createObjectiveAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.optimiser.DiscountCurve <em>Discount Curve</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can
	 * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.optimiser.DiscountCurve
	 * @generated
	 */
	public Adapter createDiscountCurveAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.optimiser.Discount <em>Discount</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily
	 * ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.optimiser.Discount
	 * @generated
	 */
	public Adapter createDiscountAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.ScenarioObject <em>Object</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore
	 * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.ScenarioObject
	 * @generated
	 */
	public Adapter createScenarioObjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.NamedObject <em>Named Object</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore
	 * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.NamedObject
	 * @generated
	 */
	public Adapter createNamedObjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc --> This default implementation returns null. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} // OptimiserAdapterFactory
