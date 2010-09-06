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

import scenario.optimiser.lso.*;

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
			LsoFactory theLsoFactory = (LsoFactory)EPackage.Registry.INSTANCE.getEFactory("http://com.mmxlabs.lng.emf/optimiser/lso"); 
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
