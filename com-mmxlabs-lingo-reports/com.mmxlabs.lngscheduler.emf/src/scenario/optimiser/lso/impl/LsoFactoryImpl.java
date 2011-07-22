/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.optimiser.lso.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import scenario.optimiser.lso.ConstrainedMoveGeneratorSettings;
import scenario.optimiser.lso.LSOSettings;
import scenario.optimiser.lso.LsoFactory;
import scenario.optimiser.lso.LsoPackage;
import scenario.optimiser.lso.MoveGeneratorSettings;
import scenario.optimiser.lso.RandomMoveGeneratorSettings;
import scenario.optimiser.lso.ThresholderSettings;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class LsoFactoryImpl extends EFactoryImpl implements LsoFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static LsoFactory init() {
		try {
			LsoFactory theLsoFactory = (LsoFactory)EPackage.Registry.INSTANCE.getEFactory("http://com.mmxlabs.lng.emf2/optimiser/lso"); 
			if (theLsoFactory != null) {
				return theLsoFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new LsoFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LsoFactoryImpl() {
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
			case LsoPackage.LSO_SETTINGS: return createLSOSettings();
			case LsoPackage.THRESHOLDER_SETTINGS: return createThresholderSettings();
			case LsoPackage.MOVE_GENERATOR_SETTINGS: return createMoveGeneratorSettings();
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS: return createRandomMoveGeneratorSettings();
			case LsoPackage.CONSTRAINED_MOVE_GENERATOR_SETTINGS: return createConstrainedMoveGeneratorSettings();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
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
	public ThresholderSettings createThresholderSettings() {
		ThresholderSettingsImpl thresholderSettings = new ThresholderSettingsImpl();
		return thresholderSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MoveGeneratorSettings createMoveGeneratorSettings() {
		MoveGeneratorSettingsImpl moveGeneratorSettings = new MoveGeneratorSettingsImpl();
		return moveGeneratorSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RandomMoveGeneratorSettings createRandomMoveGeneratorSettings() {
		RandomMoveGeneratorSettingsImpl randomMoveGeneratorSettings = new RandomMoveGeneratorSettingsImpl();
		return randomMoveGeneratorSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConstrainedMoveGeneratorSettings createConstrainedMoveGeneratorSettings() {
		ConstrainedMoveGeneratorSettingsImpl constrainedMoveGeneratorSettings = new ConstrainedMoveGeneratorSettingsImpl();
		return constrainedMoveGeneratorSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LsoPackage getLsoPackage() {
		return (LsoPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static LsoPackage getPackage() {
		return LsoPackage.eINSTANCE;
	}

} //LsoFactoryImpl
