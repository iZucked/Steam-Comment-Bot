/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.optimiser.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import scenario.optimiser.*;

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
			OptimiserFactory theOptimiserFactory = (OptimiserFactory)EPackage.Registry.INSTANCE.getEFactory("http://com.mmxlabs.lng.emf/optimiser"); 
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
			case OptimiserPackage.LSO_SETTINGS: return createLSOSettings();
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
	public LSOSettings createLSOSettings() {
		LSOSettingsImpl lsoSettings = new LSOSettingsImpl();
		return lsoSettings;
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
