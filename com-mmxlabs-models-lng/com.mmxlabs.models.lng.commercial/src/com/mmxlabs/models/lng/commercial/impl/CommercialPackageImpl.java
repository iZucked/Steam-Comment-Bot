/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.FixedPriceContract;
import com.mmxlabs.models.lng.commercial.FixedPriceParameters;
import com.mmxlabs.models.lng.commercial.IndexPriceContract;
import com.mmxlabs.models.lng.commercial.IndexPriceParameters;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.commercial.NetbackPriceParameters;
import com.mmxlabs.models.lng.commercial.PriceExpressionContract;
import com.mmxlabs.models.lng.commercial.ProfitSharePriceParameters;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.RedirectionContractOriginalDate;
import com.mmxlabs.models.lng.commercial.RedirectionPriceParameters;
import com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.commercial.TaxRate;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

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
	private EClass fixedPriceContractEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass indexPriceContractEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass netbackPurchaseContractEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass profitSharePurchaseContractEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass notionalBallastParametersEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass redirectionPurchaseContractEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass priceExpressionContractEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass redirectionContractOriginalDateEClass = null;

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
	private EClass fixedPriceParametersEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass indexPriceParametersEClass = null;

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
	private EClass redirectionPriceParametersEClass = null;

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
		TypesPackage.eINSTANCE.eClass();

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
	public EReference getCommercialModel_ShippingEntity() {
		return (EReference)commercialModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCommercialModel_PurchaseContracts() {
		return (EReference)commercialModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCommercialModel_ContractSlotExtensions() {
		return (EReference)commercialModelEClass.getEStructuralFeatures().get(4);
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
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLegalEntity_TaxRates() {
		return (EReference)legalEntityEClass.getEStructuralFeatures().get(0);
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
		return (EReference)contractEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getContract_AllowedPorts() {
		return (EReference)contractEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getContract_PreferredPort() {
		return (EReference)contractEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContract_MinQuantity() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContract_MaxQuantity() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContract_RestrictedListsArePermissive() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getContract_RestrictedContracts() {
		return (EReference)contractEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getContract_RestrictedPorts() {
		return (EReference)contractEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getContract_PriceInfo() {
		return (EReference)contractEClass.getEStructuralFeatures().get(8);
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
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSalesContract_MinCvValue() {
		return (EAttribute)salesContractEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSalesContract_MaxCvValue() {
		return (EAttribute)salesContractEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
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
	public EClass getFixedPriceContract() {
		return fixedPriceContractEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFixedPriceContract_PricePerMMBTU() {
		return (EAttribute)fixedPriceContractEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIndexPriceContract() {
		return indexPriceContractEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIndexPriceContract_Index() {
		return (EReference)indexPriceContractEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIndexPriceContract_Constant() {
		return (EAttribute)indexPriceContractEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIndexPriceContract_Multiplier() {
		return (EAttribute)indexPriceContractEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNetbackPurchaseContract() {
		return netbackPurchaseContractEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNetbackPurchaseContract_NotionalBallastParameters() {
		return (EReference)netbackPurchaseContractEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNetbackPurchaseContract_Margin() {
		return (EAttribute)netbackPurchaseContractEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNetbackPurchaseContract_FloorPrice() {
		return (EAttribute)netbackPurchaseContractEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getProfitSharePurchaseContract() {
		return profitSharePurchaseContractEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getProfitSharePurchaseContract_BaseMarketPorts() {
		return (EReference)profitSharePurchaseContractEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getProfitSharePurchaseContract_BaseMarketIndex() {
		return (EReference)profitSharePurchaseContractEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProfitSharePurchaseContract_BaseMarketConstant() {
		return (EAttribute)profitSharePurchaseContractEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProfitSharePurchaseContract_BaseMarketMultiplier() {
		return (EAttribute)profitSharePurchaseContractEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getProfitSharePurchaseContract_RefMarketIndex() {
		return (EReference)profitSharePurchaseContractEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProfitSharePurchaseContract_RefMarketConstant() {
		return (EAttribute)profitSharePurchaseContractEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProfitSharePurchaseContract_RefMarketMultiplier() {
		return (EAttribute)profitSharePurchaseContractEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProfitSharePurchaseContract_Share() {
		return (EAttribute)profitSharePurchaseContractEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProfitSharePurchaseContract_Margin() {
		return (EAttribute)profitSharePurchaseContractEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProfitSharePurchaseContract_SalesMultiplier() {
		return (EAttribute)profitSharePurchaseContractEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNotionalBallastParameters() {
		return notionalBallastParametersEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNotionalBallastParameters_Routes() {
		return (EReference)notionalBallastParametersEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNotionalBallastParameters_Speed() {
		return (EAttribute)notionalBallastParametersEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNotionalBallastParameters_HireCost() {
		return (EAttribute)notionalBallastParametersEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNotionalBallastParameters_NboRate() {
		return (EAttribute)notionalBallastParametersEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNotionalBallastParameters_BaseConsumption() {
		return (EAttribute)notionalBallastParametersEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNotionalBallastParameters_VesselClasses() {
		return (EReference)notionalBallastParametersEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRedirectionPurchaseContract() {
		return redirectionPurchaseContractEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRedirectionPurchaseContract_BaseSalesMarketPort() {
		return (EReference)redirectionPurchaseContractEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRedirectionPurchaseContract_BaseSalesPriceExpression() {
		return (EAttribute)redirectionPurchaseContractEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRedirectionPurchaseContract_BasePurchasePriceExpression() {
		return (EAttribute)redirectionPurchaseContractEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRedirectionPurchaseContract_NotionalSpeed() {
		return (EAttribute)redirectionPurchaseContractEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRedirectionPurchaseContract_DesPurchasePort() {
		return (EReference)redirectionPurchaseContractEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRedirectionPurchaseContract_SourcePurchasePort() {
		return (EReference)redirectionPurchaseContractEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRedirectionPurchaseContract_ProfitShare() {
		return (EAttribute)redirectionPurchaseContractEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRedirectionPurchaseContract_VesselClass() {
		return (EReference)redirectionPurchaseContractEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRedirectionPurchaseContract_HireCost() {
		return (EAttribute)redirectionPurchaseContractEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRedirectionPurchaseContract_DaysFromSource() {
		return (EAttribute)redirectionPurchaseContractEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPriceExpressionContract() {
		return priceExpressionContractEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPriceExpressionContract_PriceExpression() {
		return (EAttribute)priceExpressionContractEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRedirectionContractOriginalDate() {
		return redirectionContractOriginalDateEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRedirectionContractOriginalDate_Date() {
		return (EAttribute)redirectionContractOriginalDateEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTaxRate() {
		return taxRateEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTaxRate_Date() {
		return (EAttribute)taxRateEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTaxRate_Value() {
		return (EAttribute)taxRateEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLNGPriceCalculatorParameters() {
		return lngPriceCalculatorParametersEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFixedPriceParameters() {
		return fixedPriceParametersEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFixedPriceParameters_PricePerMMBTU() {
		return (EAttribute)fixedPriceParametersEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIndexPriceParameters() {
		return indexPriceParametersEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIndexPriceParameters_Index() {
		return (EReference)indexPriceParametersEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIndexPriceParameters_Multiplier() {
		return (EAttribute)indexPriceParametersEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIndexPriceParameters_Constant() {
		return (EAttribute)indexPriceParametersEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getExpressionPriceParameters() {
		return expressionPriceParametersEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExpressionPriceParameters_PriceExpression() {
		return (EAttribute)expressionPriceParametersEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRedirectionPriceParameters() {
		return redirectionPriceParametersEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRedirectionPriceParameters_BaseSalesMarketPort() {
		return (EReference)redirectionPriceParametersEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRedirectionPriceParameters_BaseSalesPriceExpression() {
		return (EAttribute)redirectionPriceParametersEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRedirectionPriceParameters_BasePurchasePriceExpression() {
		return (EAttribute)redirectionPriceParametersEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRedirectionPriceParameters_NotionalSpeed() {
		return (EAttribute)redirectionPriceParametersEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRedirectionPriceParameters_DesPurchasePort() {
		return (EReference)redirectionPriceParametersEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRedirectionPriceParameters_SourcePurchasePort() {
		return (EReference)redirectionPriceParametersEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRedirectionPriceParameters_ProfitShare() {
		return (EAttribute)redirectionPriceParametersEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRedirectionPriceParameters_VesselClass() {
		return (EReference)redirectionPriceParametersEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRedirectionPriceParameters_HireCost() {
		return (EAttribute)redirectionPriceParametersEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRedirectionPriceParameters_DaysFromSource() {
		return (EAttribute)redirectionPriceParametersEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
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
		createEReference(commercialModelEClass, COMMERCIAL_MODEL__SHIPPING_ENTITY);
		createEReference(commercialModelEClass, COMMERCIAL_MODEL__PURCHASE_CONTRACTS);
		createEReference(commercialModelEClass, COMMERCIAL_MODEL__CONTRACT_SLOT_EXTENSIONS);

		legalEntityEClass = createEClass(LEGAL_ENTITY);
		createEReference(legalEntityEClass, LEGAL_ENTITY__TAX_RATES);

		contractEClass = createEClass(CONTRACT);
		createEReference(contractEClass, CONTRACT__ENTITY);
		createEReference(contractEClass, CONTRACT__ALLOWED_PORTS);
		createEReference(contractEClass, CONTRACT__PREFERRED_PORT);
		createEAttribute(contractEClass, CONTRACT__MIN_QUANTITY);
		createEAttribute(contractEClass, CONTRACT__MAX_QUANTITY);
		createEAttribute(contractEClass, CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE);
		createEReference(contractEClass, CONTRACT__RESTRICTED_CONTRACTS);
		createEReference(contractEClass, CONTRACT__RESTRICTED_PORTS);
		createEReference(contractEClass, CONTRACT__PRICE_INFO);

		salesContractEClass = createEClass(SALES_CONTRACT);
		createEAttribute(salesContractEClass, SALES_CONTRACT__MIN_CV_VALUE);
		createEAttribute(salesContractEClass, SALES_CONTRACT__MAX_CV_VALUE);
		createEAttribute(salesContractEClass, SALES_CONTRACT__PURCHASE_DELIVERY_TYPE);

		purchaseContractEClass = createEClass(PURCHASE_CONTRACT);

		fixedPriceContractEClass = createEClass(FIXED_PRICE_CONTRACT);
		createEAttribute(fixedPriceContractEClass, FIXED_PRICE_CONTRACT__PRICE_PER_MMBTU);

		indexPriceContractEClass = createEClass(INDEX_PRICE_CONTRACT);
		createEReference(indexPriceContractEClass, INDEX_PRICE_CONTRACT__INDEX);
		createEAttribute(indexPriceContractEClass, INDEX_PRICE_CONTRACT__MULTIPLIER);
		createEAttribute(indexPriceContractEClass, INDEX_PRICE_CONTRACT__CONSTANT);

		netbackPurchaseContractEClass = createEClass(NETBACK_PURCHASE_CONTRACT);
		createEReference(netbackPurchaseContractEClass, NETBACK_PURCHASE_CONTRACT__NOTIONAL_BALLAST_PARAMETERS);
		createEAttribute(netbackPurchaseContractEClass, NETBACK_PURCHASE_CONTRACT__MARGIN);
		createEAttribute(netbackPurchaseContractEClass, NETBACK_PURCHASE_CONTRACT__FLOOR_PRICE);

		profitSharePurchaseContractEClass = createEClass(PROFIT_SHARE_PURCHASE_CONTRACT);
		createEReference(profitSharePurchaseContractEClass, PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_PORTS);
		createEReference(profitSharePurchaseContractEClass, PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_INDEX);
		createEAttribute(profitSharePurchaseContractEClass, PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_MULTIPLIER);
		createEAttribute(profitSharePurchaseContractEClass, PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_CONSTANT);
		createEReference(profitSharePurchaseContractEClass, PROFIT_SHARE_PURCHASE_CONTRACT__REF_MARKET_INDEX);
		createEAttribute(profitSharePurchaseContractEClass, PROFIT_SHARE_PURCHASE_CONTRACT__REF_MARKET_MULTIPLIER);
		createEAttribute(profitSharePurchaseContractEClass, PROFIT_SHARE_PURCHASE_CONTRACT__REF_MARKET_CONSTANT);
		createEAttribute(profitSharePurchaseContractEClass, PROFIT_SHARE_PURCHASE_CONTRACT__SHARE);
		createEAttribute(profitSharePurchaseContractEClass, PROFIT_SHARE_PURCHASE_CONTRACT__MARGIN);
		createEAttribute(profitSharePurchaseContractEClass, PROFIT_SHARE_PURCHASE_CONTRACT__SALES_MULTIPLIER);

		notionalBallastParametersEClass = createEClass(NOTIONAL_BALLAST_PARAMETERS);
		createEReference(notionalBallastParametersEClass, NOTIONAL_BALLAST_PARAMETERS__ROUTES);
		createEAttribute(notionalBallastParametersEClass, NOTIONAL_BALLAST_PARAMETERS__SPEED);
		createEAttribute(notionalBallastParametersEClass, NOTIONAL_BALLAST_PARAMETERS__HIRE_COST);
		createEAttribute(notionalBallastParametersEClass, NOTIONAL_BALLAST_PARAMETERS__NBO_RATE);
		createEAttribute(notionalBallastParametersEClass, NOTIONAL_BALLAST_PARAMETERS__BASE_CONSUMPTION);
		createEReference(notionalBallastParametersEClass, NOTIONAL_BALLAST_PARAMETERS__VESSEL_CLASSES);

		redirectionPurchaseContractEClass = createEClass(REDIRECTION_PURCHASE_CONTRACT);
		createEReference(redirectionPurchaseContractEClass, REDIRECTION_PURCHASE_CONTRACT__BASE_SALES_MARKET_PORT);
		createEAttribute(redirectionPurchaseContractEClass, REDIRECTION_PURCHASE_CONTRACT__BASE_SALES_PRICE_EXPRESSION);
		createEAttribute(redirectionPurchaseContractEClass, REDIRECTION_PURCHASE_CONTRACT__BASE_PURCHASE_PRICE_EXPRESSION);
		createEAttribute(redirectionPurchaseContractEClass, REDIRECTION_PURCHASE_CONTRACT__NOTIONAL_SPEED);
		createEReference(redirectionPurchaseContractEClass, REDIRECTION_PURCHASE_CONTRACT__DES_PURCHASE_PORT);
		createEReference(redirectionPurchaseContractEClass, REDIRECTION_PURCHASE_CONTRACT__SOURCE_PURCHASE_PORT);
		createEAttribute(redirectionPurchaseContractEClass, REDIRECTION_PURCHASE_CONTRACT__PROFIT_SHARE);
		createEReference(redirectionPurchaseContractEClass, REDIRECTION_PURCHASE_CONTRACT__VESSEL_CLASS);
		createEAttribute(redirectionPurchaseContractEClass, REDIRECTION_PURCHASE_CONTRACT__HIRE_COST);
		createEAttribute(redirectionPurchaseContractEClass, REDIRECTION_PURCHASE_CONTRACT__DAYS_FROM_SOURCE);

		priceExpressionContractEClass = createEClass(PRICE_EXPRESSION_CONTRACT);
		createEAttribute(priceExpressionContractEClass, PRICE_EXPRESSION_CONTRACT__PRICE_EXPRESSION);

		redirectionContractOriginalDateEClass = createEClass(REDIRECTION_CONTRACT_ORIGINAL_DATE);
		createEAttribute(redirectionContractOriginalDateEClass, REDIRECTION_CONTRACT_ORIGINAL_DATE__DATE);

		taxRateEClass = createEClass(TAX_RATE);
		createEAttribute(taxRateEClass, TAX_RATE__DATE);
		createEAttribute(taxRateEClass, TAX_RATE__VALUE);

		lngPriceCalculatorParametersEClass = createEClass(LNG_PRICE_CALCULATOR_PARAMETERS);

		fixedPriceParametersEClass = createEClass(FIXED_PRICE_PARAMETERS);
		createEAttribute(fixedPriceParametersEClass, FIXED_PRICE_PARAMETERS__PRICE_PER_MMBTU);

		indexPriceParametersEClass = createEClass(INDEX_PRICE_PARAMETERS);
		createEReference(indexPriceParametersEClass, INDEX_PRICE_PARAMETERS__INDEX);
		createEAttribute(indexPriceParametersEClass, INDEX_PRICE_PARAMETERS__MULTIPLIER);
		createEAttribute(indexPriceParametersEClass, INDEX_PRICE_PARAMETERS__CONSTANT);

		expressionPriceParametersEClass = createEClass(EXPRESSION_PRICE_PARAMETERS);
		createEAttribute(expressionPriceParametersEClass, EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION);

		redirectionPriceParametersEClass = createEClass(REDIRECTION_PRICE_PARAMETERS);
		createEReference(redirectionPriceParametersEClass, REDIRECTION_PRICE_PARAMETERS__BASE_SALES_MARKET_PORT);
		createEAttribute(redirectionPriceParametersEClass, REDIRECTION_PRICE_PARAMETERS__BASE_SALES_PRICE_EXPRESSION);
		createEAttribute(redirectionPriceParametersEClass, REDIRECTION_PRICE_PARAMETERS__BASE_PURCHASE_PRICE_EXPRESSION);
		createEAttribute(redirectionPriceParametersEClass, REDIRECTION_PRICE_PARAMETERS__NOTIONAL_SPEED);
		createEReference(redirectionPriceParametersEClass, REDIRECTION_PRICE_PARAMETERS__DES_PURCHASE_PORT);
		createEReference(redirectionPriceParametersEClass, REDIRECTION_PRICE_PARAMETERS__SOURCE_PURCHASE_PORT);
		createEAttribute(redirectionPriceParametersEClass, REDIRECTION_PRICE_PARAMETERS__PROFIT_SHARE);
		createEReference(redirectionPriceParametersEClass, REDIRECTION_PRICE_PARAMETERS__VESSEL_CLASS);
		createEAttribute(redirectionPriceParametersEClass, REDIRECTION_PRICE_PARAMETERS__HIRE_COST);
		createEAttribute(redirectionPriceParametersEClass, REDIRECTION_PRICE_PARAMETERS__DAYS_FROM_SOURCE);
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
		TypesPackage theTypesPackage = (TypesPackage)EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		commercialModelEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		legalEntityEClass.getESuperTypes().add(theTypesPackage.getALegalEntity());
		contractEClass.getESuperTypes().add(theTypesPackage.getAContract());
		salesContractEClass.getESuperTypes().add(this.getContract());
		salesContractEClass.getESuperTypes().add(theTypesPackage.getASalesContract());
		purchaseContractEClass.getESuperTypes().add(this.getContract());
		purchaseContractEClass.getESuperTypes().add(theTypesPackage.getAPurchaseContract());
		fixedPriceContractEClass.getESuperTypes().add(this.getSalesContract());
		fixedPriceContractEClass.getESuperTypes().add(this.getPurchaseContract());
		indexPriceContractEClass.getESuperTypes().add(this.getSalesContract());
		indexPriceContractEClass.getESuperTypes().add(this.getPurchaseContract());
		netbackPurchaseContractEClass.getESuperTypes().add(this.getPurchaseContract());
		profitSharePurchaseContractEClass.getESuperTypes().add(this.getPurchaseContract());
		notionalBallastParametersEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		redirectionPurchaseContractEClass.getESuperTypes().add(this.getPurchaseContract());
		priceExpressionContractEClass.getESuperTypes().add(this.getSalesContract());
		priceExpressionContractEClass.getESuperTypes().add(this.getPurchaseContract());
		redirectionContractOriginalDateEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		lngPriceCalculatorParametersEClass.getESuperTypes().add(theTypesPackage.getALNGPriceCalculatorParameters());
		fixedPriceParametersEClass.getESuperTypes().add(this.getLNGPriceCalculatorParameters());
		indexPriceParametersEClass.getESuperTypes().add(this.getLNGPriceCalculatorParameters());
		expressionPriceParametersEClass.getESuperTypes().add(this.getLNGPriceCalculatorParameters());
		redirectionPriceParametersEClass.getESuperTypes().add(this.getLNGPriceCalculatorParameters());

		// Initialize classes and features; add operations and parameters
		initEClass(commercialModelEClass, CommercialModel.class, "CommercialModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCommercialModel_Entities(), this.getLegalEntity(), null, "entities", null, 0, -1, CommercialModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCommercialModel_SalesContracts(), this.getSalesContract(), null, "salesContracts", null, 0, -1, CommercialModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCommercialModel_ShippingEntity(), this.getLegalEntity(), null, "shippingEntity", null, 1, 1, CommercialModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCommercialModel_PurchaseContracts(), this.getPurchaseContract(), null, "purchaseContracts", null, 0, -1, CommercialModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCommercialModel_ContractSlotExtensions(), theMMXCorePackage.getUUIDObject(), null, "contractSlotExtensions", null, 0, -1, CommercialModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(legalEntityEClass, LegalEntity.class, "LegalEntity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getLegalEntity_TaxRates(), this.getTaxRate(), null, "taxRates", null, 0, -1, LegalEntity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(contractEClass, Contract.class, "Contract", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getContract_Entity(), this.getLegalEntity(), null, "entity", null, 1, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getContract_AllowedPorts(), theTypesPackage.getAPortSet(), null, "allowedPorts", null, 0, -1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getContract_PreferredPort(), theTypesPackage.getAPort(), null, "preferredPort", null, 1, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_MinQuantity(), ecorePackage.getEInt(), "minQuantity", null, 1, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_MaxQuantity(), ecorePackage.getEInt(), "maxQuantity", null, 1, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_RestrictedListsArePermissive(), ecorePackage.getEBoolean(), "restrictedListsArePermissive", null, 0, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getContract_RestrictedContracts(), this.getContract(), null, "restrictedContracts", null, 0, -1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getContract_RestrictedPorts(), theTypesPackage.getAPortSet(), null, "restrictedPorts", null, 0, -1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getContract_PriceInfo(), theTypesPackage.getALNGPriceCalculatorParameters(), null, "priceInfo", null, 0, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(salesContractEClass, SalesContract.class, "SalesContract", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSalesContract_MinCvValue(), ecorePackage.getEDouble(), "minCvValue", null, 0, 1, SalesContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSalesContract_MaxCvValue(), ecorePackage.getEDouble(), "maxCvValue", null, 0, 1, SalesContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSalesContract_PurchaseDeliveryType(), theTypesPackage.getCargoDeliveryType(), "PurchaseDeliveryType", "false", 0, 1, SalesContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(purchaseContractEClass, PurchaseContract.class, "PurchaseContract", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(fixedPriceContractEClass, FixedPriceContract.class, "FixedPriceContract", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFixedPriceContract_PricePerMMBTU(), ecorePackage.getEDouble(), "pricePerMMBTU", null, 1, 1, FixedPriceContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(indexPriceContractEClass, IndexPriceContract.class, "IndexPriceContract", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getIndexPriceContract_Index(), theTypesPackage.getAIndex(), null, "index", null, 1, 1, IndexPriceContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIndexPriceContract_Multiplier(), ecorePackage.getEDouble(), "multiplier", "1", 1, 1, IndexPriceContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIndexPriceContract_Constant(), ecorePackage.getEDouble(), "constant", "0", 1, 1, IndexPriceContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(netbackPurchaseContractEClass, null, "NetbackPurchaseContract", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		initEReference(getNetbackPurchaseContract_NotionalBallastParameters(), this.getNotionalBallastParameters(), null, "notionalBallastParameters", null, 1, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNetbackPurchaseContract_Margin(), ecorePackage.getEDouble(), "margin", "0", 1, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNetbackPurchaseContract_FloorPrice(), ecorePackage.getEDouble(), "floorPrice", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(profitSharePurchaseContractEClass, null, "ProfitSharePurchaseContract", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		initEReference(getProfitSharePurchaseContract_BaseMarketPorts(), theTypesPackage.getAPortSet(), null, "baseMarketPorts", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProfitSharePurchaseContract_BaseMarketIndex(), theTypesPackage.getAIndex(), null, "baseMarketIndex", null, 1, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProfitSharePurchaseContract_BaseMarketMultiplier(), ecorePackage.getEDouble(), "baseMarketMultiplier", "1", 1, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProfitSharePurchaseContract_BaseMarketConstant(), ecorePackage.getEDouble(), "baseMarketConstant", "0", 1, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProfitSharePurchaseContract_RefMarketIndex(), theTypesPackage.getAIndex(), null, "refMarketIndex", null, 1, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProfitSharePurchaseContract_RefMarketMultiplier(), ecorePackage.getEDouble(), "refMarketMultiplier", "1", 1, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProfitSharePurchaseContract_RefMarketConstant(), ecorePackage.getEDouble(), "refMarketConstant", "0", 1, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProfitSharePurchaseContract_Share(), ecorePackage.getEDouble(), "share", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProfitSharePurchaseContract_Margin(), ecorePackage.getEDouble(), "margin", "0", 1, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProfitSharePurchaseContract_SalesMultiplier(), ecorePackage.getEDouble(), "salesMultiplier", null, 1, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(notionalBallastParametersEClass, null, "NotionalBallastParameters", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		initEReference(getNotionalBallastParameters_Routes(), theTypesPackage.getARoute(), null, "routes", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNotionalBallastParameters_Speed(), ecorePackage.getEDouble(), "speed", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNotionalBallastParameters_HireCost(), ecorePackage.getEInt(), "hireCost", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNotionalBallastParameters_NboRate(), ecorePackage.getEInt(), "nboRate", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNotionalBallastParameters_BaseConsumption(), ecorePackage.getEInt(), "baseConsumption", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getNotionalBallastParameters_VesselClasses(), theTypesPackage.getAVesselClass(), null, "vesselClasses", null, 1, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(redirectionPurchaseContractEClass, RedirectionPurchaseContract.class, "RedirectionPurchaseContract", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRedirectionPurchaseContract_BaseSalesMarketPort(), theTypesPackage.getAPort(), null, "baseSalesMarketPort", null, 1, 1, RedirectionPurchaseContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRedirectionPurchaseContract_BaseSalesPriceExpression(), ecorePackage.getEString(), "baseSalesPriceExpression", null, 0, 1, RedirectionPurchaseContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRedirectionPurchaseContract_BasePurchasePriceExpression(), ecorePackage.getEString(), "basePurchasePriceExpression", null, 0, 1, RedirectionPurchaseContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRedirectionPurchaseContract_NotionalSpeed(), ecorePackage.getEDouble(), "notionalSpeed", null, 0, 1, RedirectionPurchaseContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRedirectionPurchaseContract_DesPurchasePort(), theTypesPackage.getAPort(), null, "desPurchasePort", null, 1, 1, RedirectionPurchaseContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRedirectionPurchaseContract_SourcePurchasePort(), theTypesPackage.getAPort(), null, "sourcePurchasePort", null, 1, 1, RedirectionPurchaseContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRedirectionPurchaseContract_ProfitShare(), ecorePackage.getEDouble(), "profitShare", null, 0, 1, RedirectionPurchaseContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRedirectionPurchaseContract_VesselClass(), theTypesPackage.getAVesselClass(), null, "vesselClass", null, 0, 1, RedirectionPurchaseContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRedirectionPurchaseContract_HireCost(), ecorePackage.getEInt(), "hireCost", null, 0, 1, RedirectionPurchaseContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRedirectionPurchaseContract_DaysFromSource(), ecorePackage.getEInt(), "daysFromSource", null, 0, 1, RedirectionPurchaseContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(priceExpressionContractEClass, PriceExpressionContract.class, "PriceExpressionContract", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPriceExpressionContract_PriceExpression(), ecorePackage.getEString(), "priceExpression", "", 0, 1, PriceExpressionContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(redirectionContractOriginalDateEClass, RedirectionContractOriginalDate.class, "RedirectionContractOriginalDate", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getRedirectionContractOriginalDate_Date(), ecorePackage.getEDate(), "date", null, 0, 1, RedirectionContractOriginalDate.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(taxRateEClass, TaxRate.class, "TaxRate", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTaxRate_Date(), ecorePackage.getEDate(), "date", null, 0, 1, TaxRate.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaxRate_Value(), ecorePackage.getEFloat(), "value", null, 0, 1, TaxRate.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(lngPriceCalculatorParametersEClass, LNGPriceCalculatorParameters.class, "LNGPriceCalculatorParameters", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(fixedPriceParametersEClass, FixedPriceParameters.class, "FixedPriceParameters", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFixedPriceParameters_PricePerMMBTU(), ecorePackage.getEDouble(), "pricePerMMBTU", "0", 1, 1, FixedPriceParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(indexPriceParametersEClass, IndexPriceParameters.class, "IndexPriceParameters", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getIndexPriceParameters_Index(), theTypesPackage.getAIndex(), null, "index", null, 1, 1, IndexPriceParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIndexPriceParameters_Multiplier(), ecorePackage.getEDouble(), "multiplier", "1", 1, 1, IndexPriceParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIndexPriceParameters_Constant(), ecorePackage.getEDouble(), "constant", "0", 1, 1, IndexPriceParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(expressionPriceParametersEClass, ExpressionPriceParameters.class, "ExpressionPriceParameters", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getExpressionPriceParameters_PriceExpression(), ecorePackage.getEString(), "priceExpression", "", 1, 1, ExpressionPriceParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(redirectionPriceParametersEClass, RedirectionPriceParameters.class, "RedirectionPriceParameters", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRedirectionPriceParameters_BaseSalesMarketPort(), theTypesPackage.getAPort(), null, "baseSalesMarketPort", null, 1, 1, RedirectionPriceParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRedirectionPriceParameters_BaseSalesPriceExpression(), ecorePackage.getEString(), "baseSalesPriceExpression", null, 0, 1, RedirectionPriceParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRedirectionPriceParameters_BasePurchasePriceExpression(), ecorePackage.getEString(), "basePurchasePriceExpression", null, 0, 1, RedirectionPriceParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRedirectionPriceParameters_NotionalSpeed(), ecorePackage.getEDouble(), "notionalSpeed", null, 0, 1, RedirectionPriceParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRedirectionPriceParameters_DesPurchasePort(), theTypesPackage.getAPort(), null, "desPurchasePort", null, 1, 1, RedirectionPriceParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRedirectionPriceParameters_SourcePurchasePort(), theTypesPackage.getAPort(), null, "sourcePurchasePort", null, 1, 1, RedirectionPriceParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRedirectionPriceParameters_ProfitShare(), ecorePackage.getEDouble(), "profitShare", null, 0, 1, RedirectionPriceParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRedirectionPriceParameters_VesselClass(), theTypesPackage.getAVesselClass(), null, "vesselClass", null, 0, 1, RedirectionPriceParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRedirectionPriceParameters_HireCost(), ecorePackage.getEInt(), "hireCost", null, 0, 1, RedirectionPriceParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRedirectionPriceParameters_DaysFromSource(), ecorePackage.getEInt(), "daysFromSource", null, 0, 1, RedirectionPriceParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.mmxlabs.com/models/ui/numberFormat
		createNumberFormatAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http://www.mmxlabs.com/models/ui/numberFormat</b>.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createNumberFormatAnnotations() {
		String source = "http://www.mmxlabs.com/models/ui/numberFormat";		
		addAnnotation
		  (getIndexPriceContract_Constant(), 
		   source, 
		   new String[] {
			 "formatString", "-#,##0.##"
		   });
	}

} //CommercialPackageImpl
