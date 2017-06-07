/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.mmxlabs.models.datetime.DateTimePackage;
import com.mmxlabs.models.lng.commercial.BallastBonusCharterContract;
import com.mmxlabs.models.lng.commercial.BallastBonusContract;
import com.mmxlabs.models.lng.commercial.BallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.BaseEntityBook;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CharterContract;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ContractExpressionMapEntry;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.commercial.LumpSumBallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.RuleBasedBallastBonusContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.commercial.SimpleBallastBonusCharterContract;
import com.mmxlabs.models.lng.commercial.SimpleCharterContract;
import com.mmxlabs.models.lng.commercial.SimpleEntityBook;
import com.mmxlabs.models.lng.commercial.SlotContractParams;
import com.mmxlabs.models.lng.commercial.TaxRate;
import com.mmxlabs.models.lng.commercial.VolumeParams;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import org.eclipse.emf.common.util.URI;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class CommercialPackageImpl extends EPackageImpl implements CommercialPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass commercialModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass baseLegalEntityEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass legalEntityEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass contractEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass salesContractEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass purchaseContractEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass taxRateEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass lngPriceCalculatorParametersEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass expressionPriceParametersEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass slotContractParamsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass contractExpressionMapEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass volumeParamsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass baseEntityBookEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass simpleEntityBookEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dateShiftExpressionPriceParametersEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ballastBonusContractEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ruleBasedBallastBonusContractEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ballastBonusContractLineEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass lumpSumBallastBonusContractLineEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass notionalJourneyBallastBonusContractLineEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass charterContractEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass simpleCharterContractEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ballastBonusCharterContractEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass simpleBallastBonusCharterContractEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum contractTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum pricingEventEEnum = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private CommercialPackageImpl() {
		super(eNS_URI, CommercialFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link CommercialPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static CommercialPackage init() {
		if (isInited) return (CommercialPackage)EPackage.Registry.INSTANCE.getEPackage(CommercialPackage.eNS_URI);

		// Obtain or create and register package
		CommercialPackageImpl theCommercialPackage = (CommercialPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof CommercialPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new CommercialPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		DateTimePackage.eINSTANCE.eClass();
		PortPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theCommercialPackage.createPackageContents();

		// Initialize created meta-data
		theCommercialPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theCommercialPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(CommercialPackage.eNS_URI, theCommercialPackage);
		return theCommercialPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCommercialModel() {
		return commercialModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCommercialModel_Entities() {
		return (EReference)commercialModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCommercialModel_SalesContracts() {
		return (EReference)commercialModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCommercialModel_PurchaseContracts() {
		return (EReference)commercialModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCommercialModel_CharteringContracts() {
		return (EReference)commercialModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBaseLegalEntity() {
		return baseLegalEntityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBaseLegalEntity_ShippingBook() {
		return (EReference)baseLegalEntityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBaseLegalEntity_TradingBook() {
		return (EReference)baseLegalEntityEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBaseLegalEntity_UpstreamBook() {
		return (EReference)baseLegalEntityEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLegalEntity() {
		return legalEntityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getContract() {
		return contractEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getContract_Entity() {
		return (EReference)contractEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getContract_AllowedPorts() {
		return (EReference)contractEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getContract_PreferredPort() {
		return (EReference)contractEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContract_MinQuantity() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContract_MaxQuantity() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContract_VolumeLimitsUnit() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContract_RestrictedListsArePermissive() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getContract_RestrictedContracts() {
		return (EReference)contractEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getContract_RestrictedPorts() {
		return (EReference)contractEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getContract_PriceInfo() {
		return (EReference)contractEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContract_Notes() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContract_ContractType() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContract_PricingEvent() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContract_CancellationExpression() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(17);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContract_Code() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContract_Counterparty() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContract_StartDate() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContract_EndDate() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSalesContract() {
		return salesContractEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSalesContract_MinCvValue() {
		return (EAttribute)salesContractEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSalesContract_MaxCvValue() {
		return (EAttribute)salesContractEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSalesContract_PurchaseDeliveryType() {
		return (EAttribute)salesContractEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPurchaseContract() {
		return purchaseContractEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPurchaseContract_CargoCV() {
		return (EAttribute)purchaseContractEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPurchaseContract_SalesDeliveryType() {
		return (EAttribute)purchaseContractEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTaxRate() {
		return taxRateEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTaxRate_Date() {
		return (EAttribute)taxRateEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTaxRate_Value() {
		return (EAttribute)taxRateEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLNGPriceCalculatorParameters() {
		return lngPriceCalculatorParametersEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getExpressionPriceParameters() {
		return expressionPriceParametersEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExpressionPriceParameters_PriceExpression() {
		return (EAttribute)expressionPriceParametersEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSlotContractParams() {
		return slotContractParamsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getContractExpressionMapEntry() {
		return contractExpressionMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getContractExpressionMapEntry_Contract() {
		return (EReference)contractExpressionMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContractExpressionMapEntry_Expression() {
		return (EAttribute)contractExpressionMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getVolumeParams() {
		return volumeParamsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBaseEntityBook() {
		return baseEntityBookEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBaseEntityBook_TaxRates() {
		return (EReference)baseEntityBookEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSimpleEntityBook() {
		return simpleEntityBookEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDateShiftExpressionPriceParameters() {
		return dateShiftExpressionPriceParametersEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDateShiftExpressionPriceParameters_PriceExpression() {
		return (EAttribute)dateShiftExpressionPriceParametersEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDateShiftExpressionPriceParameters_SpecificDay() {
		return (EAttribute)dateShiftExpressionPriceParametersEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDateShiftExpressionPriceParameters_Value() {
		return (EAttribute)dateShiftExpressionPriceParametersEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBallastBonusContract() {
		return ballastBonusContractEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRuleBasedBallastBonusContract() {
		return ruleBasedBallastBonusContractEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRuleBasedBallastBonusContract_Rules() {
		return (EReference)ruleBasedBallastBonusContractEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBallastBonusContractLine() {
		return ballastBonusContractLineEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBallastBonusContractLine_RedeliveryPorts() {
		return (EReference)ballastBonusContractLineEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLumpSumBallastBonusContractLine() {
		return lumpSumBallastBonusContractLineEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLumpSumBallastBonusContractLine_PriceExpression() {
		return (EAttribute)lumpSumBallastBonusContractLineEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNotionalJourneyBallastBonusContractLine() {
		return notionalJourneyBallastBonusContractLineEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNotionalJourneyBallastBonusContractLine_Speed() {
		return (EAttribute)notionalJourneyBallastBonusContractLineEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNotionalJourneyBallastBonusContractLine_FuelPriceExpression() {
		return (EAttribute)notionalJourneyBallastBonusContractLineEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNotionalJourneyBallastBonusContractLine_HirePriceExpression() {
		return (EAttribute)notionalJourneyBallastBonusContractLineEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNotionalJourneyBallastBonusContractLine_ReturnPorts() {
		return (EReference)notionalJourneyBallastBonusContractLineEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNotionalJourneyBallastBonusContractLine_IncludeCanal() {
		return (EAttribute)notionalJourneyBallastBonusContractLineEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCharterContract() {
		return charterContractEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSimpleCharterContract() {
		return simpleCharterContractEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBallastBonusCharterContract() {
		return ballastBonusCharterContractEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBallastBonusCharterContract_BallastBonusContract() {
		return (EReference)ballastBonusCharterContractEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBallastBonusCharterContract_Entity() {
		return (EReference)ballastBonusCharterContractEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSimpleBallastBonusCharterContract() {
		return simpleBallastBonusCharterContractEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getContractType() {
		return contractTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getPricingEvent() {
		return pricingEventEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CommercialFactory getCommercialFactory() {
		return (CommercialFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		commercialModelEClass = createEClass(COMMERCIAL_MODEL);
		createEReference(commercialModelEClass, COMMERCIAL_MODEL__ENTITIES);
		createEReference(commercialModelEClass, COMMERCIAL_MODEL__SALES_CONTRACTS);
		createEReference(commercialModelEClass, COMMERCIAL_MODEL__PURCHASE_CONTRACTS);
		createEReference(commercialModelEClass, COMMERCIAL_MODEL__CHARTERING_CONTRACTS);

		baseLegalEntityEClass = createEClass(BASE_LEGAL_ENTITY);
		createEReference(baseLegalEntityEClass, BASE_LEGAL_ENTITY__SHIPPING_BOOK);
		createEReference(baseLegalEntityEClass, BASE_LEGAL_ENTITY__TRADING_BOOK);
		createEReference(baseLegalEntityEClass, BASE_LEGAL_ENTITY__UPSTREAM_BOOK);

		legalEntityEClass = createEClass(LEGAL_ENTITY);

		contractEClass = createEClass(CONTRACT);
		createEAttribute(contractEClass, CONTRACT__CODE);
		createEAttribute(contractEClass, CONTRACT__COUNTERPARTY);
		createEReference(contractEClass, CONTRACT__ENTITY);
		createEAttribute(contractEClass, CONTRACT__START_DATE);
		createEAttribute(contractEClass, CONTRACT__END_DATE);
		createEReference(contractEClass, CONTRACT__ALLOWED_PORTS);
		createEReference(contractEClass, CONTRACT__PREFERRED_PORT);
		createEAttribute(contractEClass, CONTRACT__MIN_QUANTITY);
		createEAttribute(contractEClass, CONTRACT__MAX_QUANTITY);
		createEAttribute(contractEClass, CONTRACT__VOLUME_LIMITS_UNIT);
		createEAttribute(contractEClass, CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE);
		createEReference(contractEClass, CONTRACT__RESTRICTED_CONTRACTS);
		createEReference(contractEClass, CONTRACT__RESTRICTED_PORTS);
		createEReference(contractEClass, CONTRACT__PRICE_INFO);
		createEAttribute(contractEClass, CONTRACT__NOTES);
		createEAttribute(contractEClass, CONTRACT__CONTRACT_TYPE);
		createEAttribute(contractEClass, CONTRACT__PRICING_EVENT);
		createEAttribute(contractEClass, CONTRACT__CANCELLATION_EXPRESSION);

		salesContractEClass = createEClass(SALES_CONTRACT);
		createEAttribute(salesContractEClass, SALES_CONTRACT__MIN_CV_VALUE);
		createEAttribute(salesContractEClass, SALES_CONTRACT__MAX_CV_VALUE);
		createEAttribute(salesContractEClass, SALES_CONTRACT__PURCHASE_DELIVERY_TYPE);

		purchaseContractEClass = createEClass(PURCHASE_CONTRACT);
		createEAttribute(purchaseContractEClass, PURCHASE_CONTRACT__CARGO_CV);
		createEAttribute(purchaseContractEClass, PURCHASE_CONTRACT__SALES_DELIVERY_TYPE);

		taxRateEClass = createEClass(TAX_RATE);
		createEAttribute(taxRateEClass, TAX_RATE__DATE);
		createEAttribute(taxRateEClass, TAX_RATE__VALUE);

		lngPriceCalculatorParametersEClass = createEClass(LNG_PRICE_CALCULATOR_PARAMETERS);

		expressionPriceParametersEClass = createEClass(EXPRESSION_PRICE_PARAMETERS);
		createEAttribute(expressionPriceParametersEClass, EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION);

		slotContractParamsEClass = createEClass(SLOT_CONTRACT_PARAMS);

		contractExpressionMapEntryEClass = createEClass(CONTRACT_EXPRESSION_MAP_ENTRY);
		createEReference(contractExpressionMapEntryEClass, CONTRACT_EXPRESSION_MAP_ENTRY__CONTRACT);
		createEAttribute(contractExpressionMapEntryEClass, CONTRACT_EXPRESSION_MAP_ENTRY__EXPRESSION);

		volumeParamsEClass = createEClass(VOLUME_PARAMS);

		baseEntityBookEClass = createEClass(BASE_ENTITY_BOOK);
		createEReference(baseEntityBookEClass, BASE_ENTITY_BOOK__TAX_RATES);

		simpleEntityBookEClass = createEClass(SIMPLE_ENTITY_BOOK);

		dateShiftExpressionPriceParametersEClass = createEClass(DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS);
		createEAttribute(dateShiftExpressionPriceParametersEClass, DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION);
		createEAttribute(dateShiftExpressionPriceParametersEClass, DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__SPECIFIC_DAY);
		createEAttribute(dateShiftExpressionPriceParametersEClass, DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__VALUE);

		ballastBonusContractEClass = createEClass(BALLAST_BONUS_CONTRACT);

		ruleBasedBallastBonusContractEClass = createEClass(RULE_BASED_BALLAST_BONUS_CONTRACT);
		createEReference(ruleBasedBallastBonusContractEClass, RULE_BASED_BALLAST_BONUS_CONTRACT__RULES);

		ballastBonusContractLineEClass = createEClass(BALLAST_BONUS_CONTRACT_LINE);
		createEReference(ballastBonusContractLineEClass, BALLAST_BONUS_CONTRACT_LINE__REDELIVERY_PORTS);

		lumpSumBallastBonusContractLineEClass = createEClass(LUMP_SUM_BALLAST_BONUS_CONTRACT_LINE);
		createEAttribute(lumpSumBallastBonusContractLineEClass, LUMP_SUM_BALLAST_BONUS_CONTRACT_LINE__PRICE_EXPRESSION);

		notionalJourneyBallastBonusContractLineEClass = createEClass(NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE);
		createEAttribute(notionalJourneyBallastBonusContractLineEClass, NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__SPEED);
		createEAttribute(notionalJourneyBallastBonusContractLineEClass, NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__FUEL_PRICE_EXPRESSION);
		createEAttribute(notionalJourneyBallastBonusContractLineEClass, NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__HIRE_PRICE_EXPRESSION);
		createEReference(notionalJourneyBallastBonusContractLineEClass, NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__RETURN_PORTS);
		createEAttribute(notionalJourneyBallastBonusContractLineEClass, NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__INCLUDE_CANAL);

		charterContractEClass = createEClass(CHARTER_CONTRACT);

		simpleCharterContractEClass = createEClass(SIMPLE_CHARTER_CONTRACT);

		ballastBonusCharterContractEClass = createEClass(BALLAST_BONUS_CHARTER_CONTRACT);
		createEReference(ballastBonusCharterContractEClass, BALLAST_BONUS_CHARTER_CONTRACT__BALLAST_BONUS_CONTRACT);
		createEReference(ballastBonusCharterContractEClass, BALLAST_BONUS_CHARTER_CONTRACT__ENTITY);

		simpleBallastBonusCharterContractEClass = createEClass(SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT);

		// Create enums
		contractTypeEEnum = createEEnum(CONTRACT_TYPE);
		pricingEventEEnum = createEEnum(PRICING_EVENT);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		MMXCorePackage theMMXCorePackage = (MMXCorePackage)EPackage.Registry.INSTANCE.getEPackage(MMXCorePackage.eNS_URI);
		DateTimePackage theDateTimePackage = (DateTimePackage)EPackage.Registry.INSTANCE.getEPackage(DateTimePackage.eNS_URI);
		TypesPackage theTypesPackage = (TypesPackage)EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);
		PortPackage thePortPackage = (PortPackage)EPackage.Registry.INSTANCE.getEPackage(PortPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		commercialModelEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		baseLegalEntityEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		baseLegalEntityEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		legalEntityEClass.getESuperTypes().add(this.getBaseLegalEntity());
		contractEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		contractEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		salesContractEClass.getESuperTypes().add(this.getContract());
		purchaseContractEClass.getESuperTypes().add(this.getContract());
		lngPriceCalculatorParametersEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		expressionPriceParametersEClass.getESuperTypes().add(this.getLNGPriceCalculatorParameters());
		slotContractParamsEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		baseEntityBookEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		simpleEntityBookEClass.getESuperTypes().add(this.getBaseEntityBook());
		dateShiftExpressionPriceParametersEClass.getESuperTypes().add(this.getLNGPriceCalculatorParameters());
		ballastBonusContractEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		ruleBasedBallastBonusContractEClass.getESuperTypes().add(this.getBallastBonusContract());
		lumpSumBallastBonusContractLineEClass.getESuperTypes().add(this.getBallastBonusContractLine());
		notionalJourneyBallastBonusContractLineEClass.getESuperTypes().add(this.getBallastBonusContractLine());
		charterContractEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		simpleCharterContractEClass.getESuperTypes().add(this.getCharterContract());
		ballastBonusCharterContractEClass.getESuperTypes().add(this.getCharterContract());
		simpleBallastBonusCharterContractEClass.getESuperTypes().add(this.getBallastBonusCharterContract());

		// Initialize classes and features; add operations and parameters
		initEClass(commercialModelEClass, CommercialModel.class, "CommercialModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCommercialModel_Entities(), this.getBaseLegalEntity(), null, "entities", null, 0, -1, CommercialModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCommercialModel_SalesContracts(), this.getSalesContract(), null, "salesContracts", null, 0, -1, CommercialModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCommercialModel_PurchaseContracts(), this.getPurchaseContract(), null, "purchaseContracts", null, 0, -1, CommercialModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCommercialModel_CharteringContracts(), this.getCharterContract(), null, "charteringContracts", null, 0, -1, CommercialModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(baseLegalEntityEClass, BaseLegalEntity.class, "BaseLegalEntity", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBaseLegalEntity_ShippingBook(), this.getBaseEntityBook(), null, "shippingBook", null, 0, 1, BaseLegalEntity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBaseLegalEntity_TradingBook(), this.getBaseEntityBook(), null, "tradingBook", null, 0, 1, BaseLegalEntity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBaseLegalEntity_UpstreamBook(), this.getBaseEntityBook(), null, "upstreamBook", null, 0, 1, BaseLegalEntity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(legalEntityEClass, LegalEntity.class, "LegalEntity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(contractEClass, Contract.class, "Contract", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getContract_Code(), ecorePackage.getEString(), "code", null, 0, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_Counterparty(), ecorePackage.getEString(), "counterparty", null, 0, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getContract_Entity(), this.getBaseLegalEntity(), null, "entity", null, 1, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_StartDate(), theDateTimePackage.getYearMonth(), "startDate", null, 0, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_EndDate(), theDateTimePackage.getYearMonth(), "endDate", null, 0, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		EGenericType g1 = createEGenericType(theTypesPackage.getAPortSet());
		EGenericType g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getContract_AllowedPorts(), g1, null, "allowedPorts", null, 0, -1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getContract_PreferredPort(), thePortPackage.getPort(), null, "preferredPort", null, 0, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_MinQuantity(), ecorePackage.getEInt(), "minQuantity", null, 1, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_MaxQuantity(), ecorePackage.getEInt(), "maxQuantity", "140000", 1, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_VolumeLimitsUnit(), theTypesPackage.getVolumeUnits(), "volumeLimitsUnit", null, 1, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_RestrictedListsArePermissive(), ecorePackage.getEBoolean(), "restrictedListsArePermissive", null, 0, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getContract_RestrictedContracts(), this.getContract(), null, "restrictedContracts", null, 0, -1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getContract_RestrictedPorts(), g1, null, "restrictedPorts", null, 0, -1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getContract_PriceInfo(), this.getLNGPriceCalculatorParameters(), null, "priceInfo", null, 1, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_Notes(), ecorePackage.getEString(), "notes", null, 0, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_ContractType(), this.getContractType(), "contractType", null, 0, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_PricingEvent(), this.getPricingEvent(), "pricingEvent", null, 1, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_CancellationExpression(), ecorePackage.getEString(), "cancellationExpression", "0", 0, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(salesContractEClass, SalesContract.class, "SalesContract", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSalesContract_MinCvValue(), ecorePackage.getEDouble(), "minCvValue", null, 0, 1, SalesContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSalesContract_MaxCvValue(), ecorePackage.getEDouble(), "maxCvValue", null, 0, 1, SalesContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSalesContract_PurchaseDeliveryType(), theTypesPackage.getCargoDeliveryType(), "PurchaseDeliveryType", "Any", 1, 1, SalesContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(purchaseContractEClass, PurchaseContract.class, "PurchaseContract", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPurchaseContract_CargoCV(), ecorePackage.getEDouble(), "cargoCV", null, 0, 1, PurchaseContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPurchaseContract_SalesDeliveryType(), theTypesPackage.getCargoDeliveryType(), "salesDeliveryType", "Any", 1, 1, PurchaseContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(taxRateEClass, TaxRate.class, "TaxRate", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTaxRate_Date(), theDateTimePackage.getLocalDate(), "date", null, 1, 1, TaxRate.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaxRate_Value(), ecorePackage.getEFloat(), "value", null, 1, 1, TaxRate.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(lngPriceCalculatorParametersEClass, LNGPriceCalculatorParameters.class, "LNGPriceCalculatorParameters", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(expressionPriceParametersEClass, ExpressionPriceParameters.class, "ExpressionPriceParameters", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getExpressionPriceParameters_PriceExpression(), ecorePackage.getEString(), "priceExpression", "", 1, 1, ExpressionPriceParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(slotContractParamsEClass, SlotContractParams.class, "SlotContractParams", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(contractExpressionMapEntryEClass, ContractExpressionMapEntry.class, "ContractExpressionMapEntry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getContractExpressionMapEntry_Contract(), this.getContract(), null, "contract", null, 1, 1, ContractExpressionMapEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContractExpressionMapEntry_Expression(), ecorePackage.getEString(), "expression", null, 1, 1, ContractExpressionMapEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(volumeParamsEClass, VolumeParams.class, "VolumeParams", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(baseEntityBookEClass, BaseEntityBook.class, "BaseEntityBook", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBaseEntityBook_TaxRates(), this.getTaxRate(), null, "taxRates", null, 0, -1, BaseEntityBook.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(simpleEntityBookEClass, SimpleEntityBook.class, "SimpleEntityBook", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(dateShiftExpressionPriceParametersEClass, DateShiftExpressionPriceParameters.class, "DateShiftExpressionPriceParameters", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDateShiftExpressionPriceParameters_PriceExpression(), ecorePackage.getEString(), "priceExpression", "", 1, 1, DateShiftExpressionPriceParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDateShiftExpressionPriceParameters_SpecificDay(), ecorePackage.getEBoolean(), "specificDay", null, 0, 1, DateShiftExpressionPriceParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDateShiftExpressionPriceParameters_Value(), ecorePackage.getEInt(), "value", null, 0, 1, DateShiftExpressionPriceParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(ballastBonusContractEClass, BallastBonusContract.class, "BallastBonusContract", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(ruleBasedBallastBonusContractEClass, RuleBasedBallastBonusContract.class, "RuleBasedBallastBonusContract", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRuleBasedBallastBonusContract_Rules(), this.getBallastBonusContractLine(), null, "rules", null, 0, -1, RuleBasedBallastBonusContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(ballastBonusContractLineEClass, BallastBonusContractLine.class, "BallastBonusContractLine", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getBallastBonusContractLine_RedeliveryPorts(), g1, null, "redeliveryPorts", null, 0, -1, BallastBonusContractLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(lumpSumBallastBonusContractLineEClass, LumpSumBallastBonusContractLine.class, "LumpSumBallastBonusContractLine", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getLumpSumBallastBonusContractLine_PriceExpression(), ecorePackage.getEString(), "priceExpression", "", 1, 1, LumpSumBallastBonusContractLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(notionalJourneyBallastBonusContractLineEClass, NotionalJourneyBallastBonusContractLine.class, "NotionalJourneyBallastBonusContractLine", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getNotionalJourneyBallastBonusContractLine_Speed(), ecorePackage.getEDouble(), "speed", "0", 1, 1, NotionalJourneyBallastBonusContractLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNotionalJourneyBallastBonusContractLine_FuelPriceExpression(), ecorePackage.getEString(), "fuelPriceExpression", "", 1, 1, NotionalJourneyBallastBonusContractLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNotionalJourneyBallastBonusContractLine_HirePriceExpression(), ecorePackage.getEString(), "hirePriceExpression", "", 1, 1, NotionalJourneyBallastBonusContractLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getNotionalJourneyBallastBonusContractLine_ReturnPorts(), g1, null, "returnPorts", null, 0, -1, NotionalJourneyBallastBonusContractLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNotionalJourneyBallastBonusContractLine_IncludeCanal(), ecorePackage.getEBoolean(), "includeCanal", null, 0, 1, NotionalJourneyBallastBonusContractLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(charterContractEClass, CharterContract.class, "CharterContract", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(simpleCharterContractEClass, SimpleCharterContract.class, "SimpleCharterContract", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(ballastBonusCharterContractEClass, BallastBonusCharterContract.class, "BallastBonusCharterContract", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBallastBonusCharterContract_BallastBonusContract(), this.getBallastBonusContract(), null, "ballastBonusContract", null, 0, 1, BallastBonusCharterContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBallastBonusCharterContract_Entity(), this.getBaseLegalEntity(), null, "entity", null, 0, 1, BallastBonusCharterContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(simpleBallastBonusCharterContractEClass, SimpleBallastBonusCharterContract.class, "SimpleBallastBonusCharterContract", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		// Initialize enums and add enum literals
		initEEnum(contractTypeEEnum, ContractType.class, "ContractType");
		addEEnumLiteral(contractTypeEEnum, ContractType.BOTH);
		addEEnumLiteral(contractTypeEEnum, ContractType.FOB);
		addEEnumLiteral(contractTypeEEnum, ContractType.DES);

		initEEnum(pricingEventEEnum, PricingEvent.class, "PricingEvent");
		addEEnumLiteral(pricingEventEEnum, PricingEvent.START_LOAD);
		addEEnumLiteral(pricingEventEEnum, PricingEvent.END_LOAD);
		addEEnumLiteral(pricingEventEEnum, PricingEvent.START_DISCHARGE);
		addEEnumLiteral(pricingEventEEnum, PricingEvent.END_DISCHARGE);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.mmxlabs.com/models/ui/numberFormat
		createNumberFormatAnnotations();
		// http://www.mmxlabs.com/models/pricing/expressionType
		createExpressionTypeAnnotations();
		// slotContractParams
		createSlotContractParamsAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http://www.mmxlabs.com/models/ui/numberFormat</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createNumberFormatAnnotations() {
		String source = "http://www.mmxlabs.com/models/ui/numberFormat";	
		addAnnotation
		  (getContract_MinQuantity(), 
		   source, 
		   new String[] {
			 "formatString", "#,###,##0"
		   });	
		addAnnotation
		  (getContract_MaxQuantity(), 
		   source, 
		   new String[] {
			 "formatString", "#,###,##0"
		   });	
		addAnnotation
		  (getSalesContract_MinCvValue(), 
		   source, 
		   new String[] {
			 "unit", "mmBtu/m\u00b3",
			 "formatString", "#0.###"
		   });	
		addAnnotation
		  (getSalesContract_MaxCvValue(), 
		   source, 
		   new String[] {
			 "unit", "mmBtu/m\u00b3",
			 "formatString", "#0.###"
		   });	
		addAnnotation
		  (getPurchaseContract_CargoCV(), 
		   source, 
		   new String[] {
			 "unit", "mmBtu/m\u00b3",
			 "formatString", "#0.###"
		   });	
		addAnnotation
		  (getDateShiftExpressionPriceParameters_Value(), 
		   source, 
		   new String[] {
			 "formatString", "-#0"
		   });
		addAnnotation
		  (getDateShiftExpressionPriceParameters_Value(), 
		   new boolean[] { true },
		   "http://www.mmxlabs.com/models/ui/numberFormat",
		   new String[] {
			 "formatString", "#,###,##0"
		   });	
		addAnnotation
		  (getNotionalJourneyBallastBonusContractLine_HirePriceExpression(), 
		   source, 
		   new String[] {
			 "unit", "$/day"
		   });
	}

	/**
	 * Initializes the annotations for <b>http://www.mmxlabs.com/models/pricing/expressionType</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createExpressionTypeAnnotations() {
		String source = "http://www.mmxlabs.com/models/pricing/expressionType";	
		addAnnotation
		  (getContract_CancellationExpression(), 
		   source, 
		   new String[] {
			 "type", "commodity"
		   });	
		addAnnotation
		  (getExpressionPriceParameters_PriceExpression(), 
		   source, 
		   new String[] {
			 "type", "commodity"
		   });	
		addAnnotation
		  (getDateShiftExpressionPriceParameters_PriceExpression(), 
		   source, 
		   new String[] {
			 "type", "commodity"
		   });	
		addAnnotation
		  (getLumpSumBallastBonusContractLine_PriceExpression(), 
		   source, 
		   new String[] {
			 "type", "commodity"
		   });	
		addAnnotation
		  (getNotionalJourneyBallastBonusContractLine_Speed(), 
		   source, 
		   new String[] {
			 "type", "commodity"
		   });	
		addAnnotation
		  (getNotionalJourneyBallastBonusContractLine_FuelPriceExpression(), 
		   source, 
		   new String[] {
			 "type", "basefuel"
		   });	
		addAnnotation
		  (getNotionalJourneyBallastBonusContractLine_HirePriceExpression(), 
		   source, 
		   new String[] {
			 "type", "charter"
		   });
	}

	/**
	 * Initializes the annotations for <b>slotContractParams</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createSlotContractParamsAnnotations() {
		String source = "slotContractParams";	
		addAnnotation
		  (lngPriceCalculatorParametersEClass, 
		   source, 
		   new String[] {
		   },
		   new URI[] {
			 URI.createURI(eNS_URI).appendFragment("//SlotContractParams")
		   });
	}

} //CommercialPackageImpl
