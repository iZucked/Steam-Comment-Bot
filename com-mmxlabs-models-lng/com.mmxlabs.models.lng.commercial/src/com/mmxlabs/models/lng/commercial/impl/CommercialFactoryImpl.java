/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.impl;

import com.mmxlabs.models.lng.commercial.*;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ContractExpressionMapEntry;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.commercial.SimpleEntityBook;
import com.mmxlabs.models.lng.commercial.TaxRate;

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
			CommercialFactory theCommercialFactory = (CommercialFactory)EPackage.Registry.INSTANCE.getEFactory(CommercialPackage.eNS_URI);
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
			case CommercialPackage.SALES_CONTRACT: return createSalesContract();
			case CommercialPackage.PURCHASE_CONTRACT: return createPurchaseContract();
			case CommercialPackage.TAX_RATE: return createTaxRate();
			case CommercialPackage.EXPRESSION_PRICE_PARAMETERS: return createExpressionPriceParameters();
			case CommercialPackage.CONTRACT_EXPRESSION_MAP_ENTRY: return createContractExpressionMapEntry();
			case CommercialPackage.SIMPLE_ENTITY_BOOK: return createSimpleEntityBook();
			case CommercialPackage.DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS: return createDateShiftExpressionPriceParameters();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case CommercialPackage.CONTRACT_TYPE:
				return createContractTypeFromString(eDataType, initialValue);
			case CommercialPackage.PRICING_EVENT:
				return createPricingEventFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case CommercialPackage.CONTRACT_TYPE:
				return convertContractTypeToString(eDataType, instanceValue);
			case CommercialPackage.PRICING_EVENT:
				return convertPricingEventToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
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
	public SalesContract createSalesContract() {
		SalesContractImpl salesContract = new SalesContractImpl();
		return salesContract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PurchaseContract createPurchaseContract() {
		PurchaseContractImpl purchaseContract = new PurchaseContractImpl();
		return purchaseContract;
	}

	/**
	 * <!-- begin-user-doc -->
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
	public ExpressionPriceParameters createExpressionPriceParameters() {
		ExpressionPriceParametersImpl expressionPriceParameters = new ExpressionPriceParametersImpl();
		return expressionPriceParameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ContractExpressionMapEntry createContractExpressionMapEntry() {
		ContractExpressionMapEntryImpl contractExpressionMapEntry = new ContractExpressionMapEntryImpl();
		return contractExpressionMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SimpleEntityBook createSimpleEntityBook() {
		SimpleEntityBookImpl simpleEntityBook = new SimpleEntityBookImpl();
		return simpleEntityBook;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DateShiftExpressionPriceParameters createDateShiftExpressionPriceParameters() {
		DateShiftExpressionPriceParametersImpl dateShiftExpressionPriceParameters = new DateShiftExpressionPriceParametersImpl();
		return dateShiftExpressionPriceParameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ContractType createContractTypeFromString(EDataType eDataType, String initialValue) {
		ContractType result = ContractType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertContractTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PricingEvent createPricingEventFromString(EDataType eDataType, String initialValue) {
		PricingEvent result = PricingEvent.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertPricingEventToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
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
