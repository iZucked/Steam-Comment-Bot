/**
 */
package com.mmxlabs.models.lng.scenario.model.impl;

import com.mmxlabs.models.lng.scenario.model.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class LNGScenarioFactoryImpl extends EFactoryImpl implements LNGScenarioFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static LNGScenarioFactory init() {
		try {
			LNGScenarioFactory theLNGScenarioFactory = (LNGScenarioFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.mmxlabs.com/models/lng/scenario/1/"); 
			if (theLNGScenarioFactory != null) {
				return theLNGScenarioFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new LNGScenarioFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LNGScenarioFactoryImpl() {
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
			case LNGScenarioPackage.LNG_SCENARIO_MODEL: return createLNGScenarioModel();
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL: return createLNGPortfolioModel();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LNGScenarioModel createLNGScenarioModel() {
		LNGScenarioModelImpl lngScenarioModel = new LNGScenarioModelImpl();
		return lngScenarioModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LNGPortfolioModel createLNGPortfolioModel() {
		LNGPortfolioModelImpl lngPortfolioModel = new LNGPortfolioModelImpl();
		return lngPortfolioModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LNGScenarioPackage getLNGScenarioPackage() {
		return (LNGScenarioPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static LNGScenarioPackage getPackage() {
		return LNGScenarioPackage.eINSTANCE;
	}

} //LNGScenarioFactoryImpl
