/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.optimiser.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import com.mmxlabs.models.lng.optimiser.AnnealingSettings;
import com.mmxlabs.models.lng.optimiser.Argument;
import com.mmxlabs.models.lng.optimiser.Constraint;
import com.mmxlabs.models.lng.optimiser.Objective;
import com.mmxlabs.models.lng.optimiser.OptimisationRange;
import com.mmxlabs.models.lng.optimiser.OptimiserFactory;
import com.mmxlabs.models.lng.optimiser.OptimiserModel;
import com.mmxlabs.models.lng.optimiser.OptimiserPackage;
import com.mmxlabs.models.lng.optimiser.OptimiserSettings;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class OptimiserFactoryImpl extends EFactoryImpl implements OptimiserFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static OptimiserFactory init() {
		try {
			OptimiserFactory theOptimiserFactory = (OptimiserFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.mmxlabs.com/models/lng/optimiser/1/"); 
			if (theOptimiserFactory != null) {
				return theOptimiserFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new OptimiserFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OptimiserFactoryImpl() {
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
			case OptimiserPackage.OPTIMISER_MODEL: return createOptimiserModel();
			case OptimiserPackage.OPTIMISER_SETTINGS: return createOptimiserSettings();
			case OptimiserPackage.OBJECTIVE: return createObjective();
			case OptimiserPackage.CONSTRAINT: return createConstraint();
			case OptimiserPackage.OPTIMISATION_RANGE: return createOptimisationRange();
			case OptimiserPackage.ANNEALING_SETTINGS: return createAnnealingSettings();
			case OptimiserPackage.ARGUMENT: return createArgument();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OptimiserModel createOptimiserModel() {
		OptimiserModelImpl optimiserModel = new OptimiserModelImpl();
		return optimiserModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OptimiserSettings createOptimiserSettings() {
		OptimiserSettingsImpl optimiserSettings = new OptimiserSettingsImpl();
		return optimiserSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Objective createObjective() {
		ObjectiveImpl objective = new ObjectiveImpl();
		return objective;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Constraint createConstraint() {
		ConstraintImpl constraint = new ConstraintImpl();
		return constraint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OptimisationRange createOptimisationRange() {
		OptimisationRangeImpl optimisationRange = new OptimisationRangeImpl();
		return optimisationRange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnnealingSettings createAnnealingSettings() {
		AnnealingSettingsImpl annealingSettings = new AnnealingSettingsImpl();
		return annealingSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Argument createArgument() {
		ArgumentImpl argument = new ArgumentImpl();
		return argument;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OptimiserPackage getOptimiserPackage() {
		return (OptimiserPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static OptimiserPackage getPackage() {
		return OptimiserPackage.eINSTANCE;
	}

} //OptimiserFactoryImpl
