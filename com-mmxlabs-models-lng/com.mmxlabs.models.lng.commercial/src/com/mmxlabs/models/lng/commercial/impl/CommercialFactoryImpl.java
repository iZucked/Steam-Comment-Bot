/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.impl;

import com.mmxlabs.models.lng.commercial.*;

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
public class CommercialFactoryImpl extends EFactoryImpl implements CommercialFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static CommercialFactory init() {
		try {
			CommercialFactory theCommercialFactory = (CommercialFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.mmxlabs.com/models/lng/commercial/1/"); 
			if (theCommercialFactory != null) {
				return theCommercialFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new CommercialFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CommercialFactoryImpl() {
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
			case CommercialPackage.COMMERCIAL_MODEL: return createCommercialModel();
			case CommercialPackage.LEGAL_ENTITY: return createLegalEntity();
			case CommercialPackage.CONTRACT: return createContract();
			case CommercialPackage.FIXED_PRICE_CONTRACT: return createFixedPriceContract();
			case CommercialPackage.INDEX_PRICE_CONTRACT: return createIndexPriceContract();
			case CommercialPackage.NETBACK_PURCHASE_CONTRACT: return createNetbackPurchaseContract();
			case CommercialPackage.PROFIT_SHARE_PURCHASE_CONTRACT: return createProfitSharePurchaseContract();
			case CommercialPackage.NOTIONAL_BALLAST_PARAMETERS: return createNotionalBallastParameters();
			case CommercialPackage.REDIRECTION_PURCHASE_CONTRACT: return createRedirectionPurchaseContract();
			case CommercialPackage.PRICE_EXPRESSION_CONTRACT: return createPriceExpressionContract();
			case CommercialPackage.REDIRECTION_CONTRACT_ORIGINAL_DATE: return createRedirectionContractOriginalDate();
			case CommercialPackage.TAX_RATE: return createTaxRate();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CommercialModel createCommercialModel() {
		CommercialModelImpl commercialModel = new CommercialModelImpl();
		return commercialModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LegalEntity createLegalEntity() {
		LegalEntityImpl legalEntity = new LegalEntityImpl();
		return legalEntity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Contract createContract() {
		ContractImpl contract = new ContractImpl();
		return contract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FixedPriceContract createFixedPriceContract() {
		FixedPriceContractImpl fixedPriceContract = new FixedPriceContractImpl();
		return fixedPriceContract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IndexPriceContract createIndexPriceContract() {
		IndexPriceContractImpl indexPriceContract = new IndexPriceContractImpl();
		return indexPriceContract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NetbackPurchaseContract createNetbackPurchaseContract() {
		NetbackPurchaseContractImpl netbackPurchaseContract = new NetbackPurchaseContractImpl();
		return netbackPurchaseContract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProfitSharePurchaseContract createProfitSharePurchaseContract() {
		ProfitSharePurchaseContractImpl profitSharePurchaseContract = new ProfitSharePurchaseContractImpl();
		return profitSharePurchaseContract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotionalBallastParameters createNotionalBallastParameters() {
		NotionalBallastParametersImpl notionalBallastParameters = new NotionalBallastParametersImpl();
		return notionalBallastParameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RedirectionPurchaseContract createRedirectionPurchaseContract() {
		RedirectionPurchaseContractImpl redirectionPurchaseContract = new RedirectionPurchaseContractImpl();
		return redirectionPurchaseContract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PriceExpressionContract createPriceExpressionContract() {
		PriceExpressionContractImpl priceExpressionContract = new PriceExpressionContractImpl();
		return priceExpressionContract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RedirectionContractOriginalDate createRedirectionContractOriginalDate() {
		RedirectionContractOriginalDateImpl redirectionContractOriginalDate = new RedirectionContractOriginalDateImpl();
		return redirectionContractOriginalDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TaxRate createTaxRate() {
		TaxRateImpl taxRate = new TaxRateImpl();
		return taxRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CommercialPackage getCommercialPackage() {
		return (CommercialPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static CommercialPackage getPackage() {
		return CommercialPackage.eINSTANCE;
	}

} //CommercialFactoryImpl
