/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.optimiser.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import scenario.optimiser.*;
import scenario.optimiser.Constraint;
import scenario.optimiser.Discount;
import scenario.optimiser.DiscountCurve;
import scenario.optimiser.Objective;
import scenario.optimiser.Optimisation;
import scenario.optimiser.OptimisationSettings;
import scenario.optimiser.OptimiserFactory;
import scenario.optimiser.OptimiserPackage;

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
			OptimiserFactory theOptimiserFactory = (OptimiserFactory)EPackage.Registry.INSTANCE.getEFactory("http://com.mmxlabs.lng.emf2/optimiser"); 
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
			case OptimiserPackage.OPTIMISATION_SETTINGS: return createOptimisationSettings();
			case OptimiserPackage.OPTIMISATION: return createOptimisation();
			case OptimiserPackage.CONSTRAINT: return createConstraint();
			case OptimiserPackage.OBJECTIVE: return createObjective();
			case OptimiserPackage.DISCOUNT_CURVE: return createDiscountCurve();
			case OptimiserPackage.DISCOUNT: return createDiscount();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OptimisationSettings createOptimisationSettings() {
		OptimisationSettingsImpl optimisationSettings = new OptimisationSettingsImpl();
		return optimisationSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Optimisation createOptimisation() {
		OptimisationImpl optimisation = new OptimisationImpl();
		return optimisation;
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
	public Objective createObjective() {
		ObjectiveImpl objective = new ObjectiveImpl();
		return objective;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiscountCurve createDiscountCurve() {
		DiscountCurveImpl discountCurve = new DiscountCurveImpl();
		return discountCurve;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Discount createDiscount() {
		DiscountImpl discount = new DiscountImpl();
		return discount;
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
