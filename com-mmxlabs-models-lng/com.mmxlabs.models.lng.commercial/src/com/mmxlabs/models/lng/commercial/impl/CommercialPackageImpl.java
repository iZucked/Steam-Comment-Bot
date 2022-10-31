/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.impl;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.mmxlabs.models.datetime.DateTimePackage;
import com.mmxlabs.models.lng.commercial.BallastBonusTerm;
import com.mmxlabs.models.lng.commercial.BaseEntityBook;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ContractExpressionMapEntry;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.EVesselTankState;
import com.mmxlabs.models.lng.commercial.EndHeelOptions;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.commercial.IBallastBonus;
import com.mmxlabs.models.lng.commercial.IRepositioningFee;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.commercial.LumpSumBallastBonusTerm;
import com.mmxlabs.models.lng.commercial.LumpSumRepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.LumpSumTerm;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContainer;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusTerm;
import com.mmxlabs.models.lng.commercial.NextPortType;
import com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusTerm;
import com.mmxlabs.models.lng.commercial.NotionalJourneyTerm;
import com.mmxlabs.models.lng.commercial.OriginPortRepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.RegasPricingParams;
import com.mmxlabs.models.lng.commercial.RepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.commercial.SimpleBallastBonusContainer;
import com.mmxlabs.models.lng.commercial.SimpleEntityBook;
import com.mmxlabs.models.lng.commercial.SimpleRepositioningFeeContainer;
import com.mmxlabs.models.lng.commercial.SlotContractParams;
import com.mmxlabs.models.lng.commercial.StartHeelOptions;
import com.mmxlabs.models.lng.commercial.TaxRate;
import com.mmxlabs.models.lng.commercial.VolumeParams;
import com.mmxlabs.models.lng.commercial.VolumeTierPriceParameters;
import com.mmxlabs.models.lng.commercial.VolumeTierSlotParams;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.port.PortPackage;
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
	private EClass volumeTierPriceParametersEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass volumeTierSlotParamsEClass = null;

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
	private EClass genericCharterContractEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iRepositioningFeeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass simpleRepositioningFeeContainerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iBallastBonusEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass simpleBallastBonusContainerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass monthlyBallastBonusContainerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass lumpSumTermEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass notionalJourneyTermEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ballastBonusTermEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass lumpSumBallastBonusTermEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass notionalJourneyBallastBonusTermEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass monthlyBallastBonusTermEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass repositioningFeeTermEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass lumpSumRepositioningFeeTermEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass originPortRepositioningFeeTermEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass endHeelOptionsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass startHeelOptionsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass regasPricingParamsEClass = null;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum nextPortTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum eVesselTankStateEEnum = null;

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
		Object registeredCommercialPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		CommercialPackageImpl theCommercialPackage = registeredCommercialPackage instanceof CommercialPackageImpl ? (CommercialPackageImpl)registeredCommercialPackage : new CommercialPackageImpl();

		isInited = true;

		// Initialize simple dependencies
		DateTimePackage.eINSTANCE.eClass();
		FleetPackage.eINSTANCE.eClass();
		TypesPackage.eINSTANCE.eClass();
		MMXCorePackage.eINSTANCE.eClass();
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
	@Override
	public EClass getCommercialModel() {
		return commercialModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCommercialModel_Entities() {
		return (EReference)commercialModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCommercialModel_SalesContracts() {
		return (EReference)commercialModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCommercialModel_PurchaseContracts() {
		return (EReference)commercialModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCommercialModel_CharterContracts() {
		return (EReference)commercialModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getBaseLegalEntity() {
		return baseLegalEntityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBaseLegalEntity_ShippingBook() {
		return (EReference)baseLegalEntityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBaseLegalEntity_TradingBook() {
		return (EReference)baseLegalEntityEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBaseLegalEntity_UpstreamBook() {
		return (EReference)baseLegalEntityEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBaseLegalEntity_ThirdParty() {
		return (EAttribute)baseLegalEntityEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getLegalEntity() {
		return legalEntityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getContract() {
		return contractEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getContract_Entity() {
		return (EReference)contractEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getContract_AllowedPorts() {
		return (EReference)contractEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getContract_PreferredPort() {
		return (EReference)contractEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContract_MinQuantity() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContract_MaxQuantity() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContract_OperationalTolerance() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContract_FullCargoLot() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContract_VolumeLimitsUnit() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getContract_RestrictedContracts() {
		return (EReference)contractEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContract_RestrictedContractsArePermissive() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getContract_RestrictedPorts() {
		return (EReference)contractEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContract_RestrictedPortsArePermissive() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(17);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getContract_RestrictedVessels() {
		return (EReference)contractEClass.getEStructuralFeatures().get(18);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContract_RestrictedVesselsArePermissive() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(19);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getContract_PriceInfo() {
		return (EReference)contractEClass.getEStructuralFeatures().get(20);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContract_Notes() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(21);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContract_ContractType() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(22);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContract_PricingEvent() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(23);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContract_CancellationExpression() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(24);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContract_ShippingDaysRestriction() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(25);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContract_ContractYearStart() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContract_Code() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContract_Counterparty() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContract_Cn() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContract_StartDate() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContract_EndDate() {
		return (EAttribute)contractEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSalesContract() {
		return salesContractEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSalesContract_MinCvValue() {
		return (EAttribute)salesContractEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSalesContract_MaxCvValue() {
		return (EAttribute)salesContractEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSalesContract_PurchaseDeliveryType() {
		return (EAttribute)salesContractEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSalesContract_FobSaleDealType() {
		return (EAttribute)salesContractEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPurchaseContract() {
		return purchaseContractEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPurchaseContract_CargoCV() {
		return (EAttribute)purchaseContractEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPurchaseContract_SalesDeliveryType() {
		return (EAttribute)purchaseContractEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPurchaseContract_DesPurchaseDealType() {
		return (EAttribute)purchaseContractEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getTaxRate() {
		return taxRateEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTaxRate_Date() {
		return (EAttribute)taxRateEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTaxRate_Value() {
		return (EAttribute)taxRateEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getLNGPriceCalculatorParameters() {
		return lngPriceCalculatorParametersEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getExpressionPriceParameters() {
		return expressionPriceParametersEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getExpressionPriceParameters_PriceExpression() {
		return (EAttribute)expressionPriceParametersEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getVolumeTierPriceParameters() {
		return volumeTierPriceParametersEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVolumeTierPriceParameters_LowExpression() {
		return (EAttribute)volumeTierPriceParametersEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVolumeTierPriceParameters_HighExpression() {
		return (EAttribute)volumeTierPriceParametersEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVolumeTierPriceParameters_Threshold() {
		return (EAttribute)volumeTierPriceParametersEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVolumeTierPriceParameters_VolumeLimitsUnit() {
		return (EAttribute)volumeTierPriceParametersEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getVolumeTierSlotParams() {
		return volumeTierSlotParamsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVolumeTierSlotParams_LowExpression() {
		return (EAttribute)volumeTierSlotParamsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVolumeTierSlotParams_HighExpression() {
		return (EAttribute)volumeTierSlotParamsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVolumeTierSlotParams_Threshold() {
		return (EAttribute)volumeTierSlotParamsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVolumeTierSlotParams_VolumeLimitsUnit() {
		return (EAttribute)volumeTierSlotParamsEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVolumeTierSlotParams_OverrideContract() {
		return (EAttribute)volumeTierSlotParamsEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSlotContractParams() {
		return slotContractParamsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getContractExpressionMapEntry() {
		return contractExpressionMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getContractExpressionMapEntry_Contract() {
		return (EReference)contractExpressionMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContractExpressionMapEntry_Expression() {
		return (EAttribute)contractExpressionMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getVolumeParams() {
		return volumeParamsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getBaseEntityBook() {
		return baseEntityBookEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBaseEntityBook_TaxRates() {
		return (EReference)baseEntityBookEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSimpleEntityBook() {
		return simpleEntityBookEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDateShiftExpressionPriceParameters() {
		return dateShiftExpressionPriceParametersEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDateShiftExpressionPriceParameters_PriceExpression() {
		return (EAttribute)dateShiftExpressionPriceParametersEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDateShiftExpressionPriceParameters_SpecificDay() {
		return (EAttribute)dateShiftExpressionPriceParametersEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDateShiftExpressionPriceParameters_Value() {
		return (EAttribute)dateShiftExpressionPriceParametersEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getGenericCharterContract() {
		return genericCharterContractEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getGenericCharterContract_MinDuration() {
		return (EAttribute)genericCharterContractEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getGenericCharterContract_MaxDuration() {
		return (EAttribute)genericCharterContractEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getGenericCharterContract_RepositioningFeeTerms() {
		return (EReference)genericCharterContractEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getGenericCharterContract_BallastBonusTerms() {
		return (EReference)genericCharterContractEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getGenericCharterContract_StartHeel() {
		return (EReference)genericCharterContractEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getGenericCharterContract_EndHeel() {
		return (EReference)genericCharterContractEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getIRepositioningFee() {
		return iRepositioningFeeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSimpleRepositioningFeeContainer() {
		return simpleRepositioningFeeContainerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSimpleRepositioningFeeContainer_Terms() {
		return (EReference)simpleRepositioningFeeContainerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getIBallastBonus() {
		return iBallastBonusEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSimpleBallastBonusContainer() {
		return simpleBallastBonusContainerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSimpleBallastBonusContainer_Terms() {
		return (EReference)simpleBallastBonusContainerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMonthlyBallastBonusContainer() {
		return monthlyBallastBonusContainerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMonthlyBallastBonusContainer_Hubs() {
		return (EReference)monthlyBallastBonusContainerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMonthlyBallastBonusContainer_Terms() {
		return (EReference)monthlyBallastBonusContainerEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getLumpSumTerm() {
		return lumpSumTermEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getLumpSumTerm_PriceExpression() {
		return (EAttribute)lumpSumTermEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getNotionalJourneyTerm() {
		return notionalJourneyTermEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNotionalJourneyTerm_Speed() {
		return (EAttribute)notionalJourneyTermEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNotionalJourneyTerm_FuelPriceExpression() {
		return (EAttribute)notionalJourneyTermEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNotionalJourneyTerm_HirePriceExpression() {
		return (EAttribute)notionalJourneyTermEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNotionalJourneyTerm_IncludeCanal() {
		return (EAttribute)notionalJourneyTermEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNotionalJourneyTerm_IncludeCanalTime() {
		return (EAttribute)notionalJourneyTermEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNotionalJourneyTerm_LumpSumPriceExpression() {
		return (EAttribute)notionalJourneyTermEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getBallastBonusTerm() {
		return ballastBonusTermEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBallastBonusTerm_RedeliveryPorts() {
		return (EReference)ballastBonusTermEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getLumpSumBallastBonusTerm() {
		return lumpSumBallastBonusTermEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getNotionalJourneyBallastBonusTerm() {
		return notionalJourneyBallastBonusTermEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getNotionalJourneyBallastBonusTerm_ReturnPorts() {
		return (EReference)notionalJourneyBallastBonusTermEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMonthlyBallastBonusTerm() {
		return monthlyBallastBonusTermEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMonthlyBallastBonusTerm_Month() {
		return (EAttribute)monthlyBallastBonusTermEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMonthlyBallastBonusTerm_BallastBonusTo() {
		return (EAttribute)monthlyBallastBonusTermEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMonthlyBallastBonusTerm_BallastBonusPctFuel() {
		return (EAttribute)monthlyBallastBonusTermEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMonthlyBallastBonusTerm_BallastBonusPctCharter() {
		return (EAttribute)monthlyBallastBonusTermEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getRepositioningFeeTerm() {
		return repositioningFeeTermEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getRepositioningFeeTerm_StartPorts() {
		return (EReference)repositioningFeeTermEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getLumpSumRepositioningFeeTerm() {
		return lumpSumRepositioningFeeTermEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getOriginPortRepositioningFeeTerm() {
		return originPortRepositioningFeeTermEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOriginPortRepositioningFeeTerm_OriginPort() {
		return (EReference)originPortRepositioningFeeTermEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEndHeelOptions() {
		return endHeelOptionsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEndHeelOptions_TankState() {
		return (EAttribute)endHeelOptionsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEndHeelOptions_MinimumEndHeel() {
		return (EAttribute)endHeelOptionsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEndHeelOptions_MaximumEndHeel() {
		return (EAttribute)endHeelOptionsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEndHeelOptions_UseLastHeelPrice() {
		return (EAttribute)endHeelOptionsEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEndHeelOptions_PriceExpression() {
		return (EAttribute)endHeelOptionsEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getStartHeelOptions() {
		return startHeelOptionsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getStartHeelOptions_CvValue() {
		return (EAttribute)startHeelOptionsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getStartHeelOptions_MinVolumeAvailable() {
		return (EAttribute)startHeelOptionsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getStartHeelOptions_MaxVolumeAvailable() {
		return (EAttribute)startHeelOptionsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getStartHeelOptions_PriceExpression() {
		return (EAttribute)startHeelOptionsEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getRegasPricingParams() {
		return regasPricingParamsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getRegasPricingParams_PriceExpression() {
		return (EAttribute)regasPricingParamsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getRegasPricingParams_NumPricingDays() {
		return (EAttribute)regasPricingParamsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getContractType() {
		return contractTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getPricingEvent() {
		return pricingEventEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getNextPortType() {
		return nextPortTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getEVesselTankState() {
		return eVesselTankStateEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
		createEReference(commercialModelEClass, COMMERCIAL_MODEL__CHARTER_CONTRACTS);

		baseLegalEntityEClass = createEClass(BASE_LEGAL_ENTITY);
		createEReference(baseLegalEntityEClass, BASE_LEGAL_ENTITY__SHIPPING_BOOK);
		createEReference(baseLegalEntityEClass, BASE_LEGAL_ENTITY__TRADING_BOOK);
		createEReference(baseLegalEntityEClass, BASE_LEGAL_ENTITY__UPSTREAM_BOOK);
		createEAttribute(baseLegalEntityEClass, BASE_LEGAL_ENTITY__THIRD_PARTY);

		legalEntityEClass = createEClass(LEGAL_ENTITY);

		contractEClass = createEClass(CONTRACT);
		createEAttribute(contractEClass, CONTRACT__CODE);
		createEAttribute(contractEClass, CONTRACT__COUNTERPARTY);
		createEAttribute(contractEClass, CONTRACT__CN);
		createEReference(contractEClass, CONTRACT__ENTITY);
		createEAttribute(contractEClass, CONTRACT__START_DATE);
		createEAttribute(contractEClass, CONTRACT__END_DATE);
		createEAttribute(contractEClass, CONTRACT__CONTRACT_YEAR_START);
		createEReference(contractEClass, CONTRACT__ALLOWED_PORTS);
		createEReference(contractEClass, CONTRACT__PREFERRED_PORT);
		createEAttribute(contractEClass, CONTRACT__MIN_QUANTITY);
		createEAttribute(contractEClass, CONTRACT__MAX_QUANTITY);
		createEAttribute(contractEClass, CONTRACT__VOLUME_LIMITS_UNIT);
		createEAttribute(contractEClass, CONTRACT__OPERATIONAL_TOLERANCE);
		createEAttribute(contractEClass, CONTRACT__FULL_CARGO_LOT);
		createEReference(contractEClass, CONTRACT__RESTRICTED_CONTRACTS);
		createEAttribute(contractEClass, CONTRACT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE);
		createEReference(contractEClass, CONTRACT__RESTRICTED_PORTS);
		createEAttribute(contractEClass, CONTRACT__RESTRICTED_PORTS_ARE_PERMISSIVE);
		createEReference(contractEClass, CONTRACT__RESTRICTED_VESSELS);
		createEAttribute(contractEClass, CONTRACT__RESTRICTED_VESSELS_ARE_PERMISSIVE);
		createEReference(contractEClass, CONTRACT__PRICE_INFO);
		createEAttribute(contractEClass, CONTRACT__NOTES);
		createEAttribute(contractEClass, CONTRACT__CONTRACT_TYPE);
		createEAttribute(contractEClass, CONTRACT__PRICING_EVENT);
		createEAttribute(contractEClass, CONTRACT__CANCELLATION_EXPRESSION);
		createEAttribute(contractEClass, CONTRACT__SHIPPING_DAYS_RESTRICTION);

		salesContractEClass = createEClass(SALES_CONTRACT);
		createEAttribute(salesContractEClass, SALES_CONTRACT__MIN_CV_VALUE);
		createEAttribute(salesContractEClass, SALES_CONTRACT__MAX_CV_VALUE);
		createEAttribute(salesContractEClass, SALES_CONTRACT__PURCHASE_DELIVERY_TYPE);
		createEAttribute(salesContractEClass, SALES_CONTRACT__FOB_SALE_DEAL_TYPE);

		purchaseContractEClass = createEClass(PURCHASE_CONTRACT);
		createEAttribute(purchaseContractEClass, PURCHASE_CONTRACT__SALES_DELIVERY_TYPE);
		createEAttribute(purchaseContractEClass, PURCHASE_CONTRACT__DES_PURCHASE_DEAL_TYPE);
		createEAttribute(purchaseContractEClass, PURCHASE_CONTRACT__CARGO_CV);

		taxRateEClass = createEClass(TAX_RATE);
		createEAttribute(taxRateEClass, TAX_RATE__DATE);
		createEAttribute(taxRateEClass, TAX_RATE__VALUE);

		lngPriceCalculatorParametersEClass = createEClass(LNG_PRICE_CALCULATOR_PARAMETERS);

		expressionPriceParametersEClass = createEClass(EXPRESSION_PRICE_PARAMETERS);
		createEAttribute(expressionPriceParametersEClass, EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION);

		volumeTierPriceParametersEClass = createEClass(VOLUME_TIER_PRICE_PARAMETERS);
		createEAttribute(volumeTierPriceParametersEClass, VOLUME_TIER_PRICE_PARAMETERS__LOW_EXPRESSION);
		createEAttribute(volumeTierPriceParametersEClass, VOLUME_TIER_PRICE_PARAMETERS__HIGH_EXPRESSION);
		createEAttribute(volumeTierPriceParametersEClass, VOLUME_TIER_PRICE_PARAMETERS__THRESHOLD);
		createEAttribute(volumeTierPriceParametersEClass, VOLUME_TIER_PRICE_PARAMETERS__VOLUME_LIMITS_UNIT);

		volumeTierSlotParamsEClass = createEClass(VOLUME_TIER_SLOT_PARAMS);
		createEAttribute(volumeTierSlotParamsEClass, VOLUME_TIER_SLOT_PARAMS__LOW_EXPRESSION);
		createEAttribute(volumeTierSlotParamsEClass, VOLUME_TIER_SLOT_PARAMS__HIGH_EXPRESSION);
		createEAttribute(volumeTierSlotParamsEClass, VOLUME_TIER_SLOT_PARAMS__THRESHOLD);
		createEAttribute(volumeTierSlotParamsEClass, VOLUME_TIER_SLOT_PARAMS__VOLUME_LIMITS_UNIT);
		createEAttribute(volumeTierSlotParamsEClass, VOLUME_TIER_SLOT_PARAMS__OVERRIDE_CONTRACT);

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

		genericCharterContractEClass = createEClass(GENERIC_CHARTER_CONTRACT);
		createEAttribute(genericCharterContractEClass, GENERIC_CHARTER_CONTRACT__MIN_DURATION);
		createEAttribute(genericCharterContractEClass, GENERIC_CHARTER_CONTRACT__MAX_DURATION);
		createEReference(genericCharterContractEClass, GENERIC_CHARTER_CONTRACT__REPOSITIONING_FEE_TERMS);
		createEReference(genericCharterContractEClass, GENERIC_CHARTER_CONTRACT__BALLAST_BONUS_TERMS);
		createEReference(genericCharterContractEClass, GENERIC_CHARTER_CONTRACT__START_HEEL);
		createEReference(genericCharterContractEClass, GENERIC_CHARTER_CONTRACT__END_HEEL);

		iRepositioningFeeEClass = createEClass(IREPOSITIONING_FEE);

		simpleRepositioningFeeContainerEClass = createEClass(SIMPLE_REPOSITIONING_FEE_CONTAINER);
		createEReference(simpleRepositioningFeeContainerEClass, SIMPLE_REPOSITIONING_FEE_CONTAINER__TERMS);

		iBallastBonusEClass = createEClass(IBALLAST_BONUS);

		simpleBallastBonusContainerEClass = createEClass(SIMPLE_BALLAST_BONUS_CONTAINER);
		createEReference(simpleBallastBonusContainerEClass, SIMPLE_BALLAST_BONUS_CONTAINER__TERMS);

		monthlyBallastBonusContainerEClass = createEClass(MONTHLY_BALLAST_BONUS_CONTAINER);
		createEReference(monthlyBallastBonusContainerEClass, MONTHLY_BALLAST_BONUS_CONTAINER__HUBS);
		createEReference(monthlyBallastBonusContainerEClass, MONTHLY_BALLAST_BONUS_CONTAINER__TERMS);

		lumpSumTermEClass = createEClass(LUMP_SUM_TERM);
		createEAttribute(lumpSumTermEClass, LUMP_SUM_TERM__PRICE_EXPRESSION);

		notionalJourneyTermEClass = createEClass(NOTIONAL_JOURNEY_TERM);
		createEAttribute(notionalJourneyTermEClass, NOTIONAL_JOURNEY_TERM__SPEED);
		createEAttribute(notionalJourneyTermEClass, NOTIONAL_JOURNEY_TERM__FUEL_PRICE_EXPRESSION);
		createEAttribute(notionalJourneyTermEClass, NOTIONAL_JOURNEY_TERM__HIRE_PRICE_EXPRESSION);
		createEAttribute(notionalJourneyTermEClass, NOTIONAL_JOURNEY_TERM__INCLUDE_CANAL);
		createEAttribute(notionalJourneyTermEClass, NOTIONAL_JOURNEY_TERM__INCLUDE_CANAL_TIME);
		createEAttribute(notionalJourneyTermEClass, NOTIONAL_JOURNEY_TERM__LUMP_SUM_PRICE_EXPRESSION);

		ballastBonusTermEClass = createEClass(BALLAST_BONUS_TERM);
		createEReference(ballastBonusTermEClass, BALLAST_BONUS_TERM__REDELIVERY_PORTS);

		lumpSumBallastBonusTermEClass = createEClass(LUMP_SUM_BALLAST_BONUS_TERM);

		notionalJourneyBallastBonusTermEClass = createEClass(NOTIONAL_JOURNEY_BALLAST_BONUS_TERM);
		createEReference(notionalJourneyBallastBonusTermEClass, NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__RETURN_PORTS);

		monthlyBallastBonusTermEClass = createEClass(MONTHLY_BALLAST_BONUS_TERM);
		createEAttribute(monthlyBallastBonusTermEClass, MONTHLY_BALLAST_BONUS_TERM__MONTH);
		createEAttribute(monthlyBallastBonusTermEClass, MONTHLY_BALLAST_BONUS_TERM__BALLAST_BONUS_TO);
		createEAttribute(monthlyBallastBonusTermEClass, MONTHLY_BALLAST_BONUS_TERM__BALLAST_BONUS_PCT_FUEL);
		createEAttribute(monthlyBallastBonusTermEClass, MONTHLY_BALLAST_BONUS_TERM__BALLAST_BONUS_PCT_CHARTER);

		repositioningFeeTermEClass = createEClass(REPOSITIONING_FEE_TERM);
		createEReference(repositioningFeeTermEClass, REPOSITIONING_FEE_TERM__START_PORTS);

		lumpSumRepositioningFeeTermEClass = createEClass(LUMP_SUM_REPOSITIONING_FEE_TERM);

		originPortRepositioningFeeTermEClass = createEClass(ORIGIN_PORT_REPOSITIONING_FEE_TERM);
		createEReference(originPortRepositioningFeeTermEClass, ORIGIN_PORT_REPOSITIONING_FEE_TERM__ORIGIN_PORT);

		endHeelOptionsEClass = createEClass(END_HEEL_OPTIONS);
		createEAttribute(endHeelOptionsEClass, END_HEEL_OPTIONS__TANK_STATE);
		createEAttribute(endHeelOptionsEClass, END_HEEL_OPTIONS__MINIMUM_END_HEEL);
		createEAttribute(endHeelOptionsEClass, END_HEEL_OPTIONS__MAXIMUM_END_HEEL);
		createEAttribute(endHeelOptionsEClass, END_HEEL_OPTIONS__USE_LAST_HEEL_PRICE);
		createEAttribute(endHeelOptionsEClass, END_HEEL_OPTIONS__PRICE_EXPRESSION);

		startHeelOptionsEClass = createEClass(START_HEEL_OPTIONS);
		createEAttribute(startHeelOptionsEClass, START_HEEL_OPTIONS__CV_VALUE);
		createEAttribute(startHeelOptionsEClass, START_HEEL_OPTIONS__MIN_VOLUME_AVAILABLE);
		createEAttribute(startHeelOptionsEClass, START_HEEL_OPTIONS__MAX_VOLUME_AVAILABLE);
		createEAttribute(startHeelOptionsEClass, START_HEEL_OPTIONS__PRICE_EXPRESSION);

		regasPricingParamsEClass = createEClass(REGAS_PRICING_PARAMS);
		createEAttribute(regasPricingParamsEClass, REGAS_PRICING_PARAMS__PRICE_EXPRESSION);
		createEAttribute(regasPricingParamsEClass, REGAS_PRICING_PARAMS__NUM_PRICING_DAYS);

		// Create enums
		contractTypeEEnum = createEEnum(CONTRACT_TYPE);
		pricingEventEEnum = createEEnum(PRICING_EVENT);
		nextPortTypeEEnum = createEEnum(NEXT_PORT_TYPE);
		eVesselTankStateEEnum = createEEnum(EVESSEL_TANK_STATE);
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
		FleetPackage theFleetPackage = (FleetPackage)EPackage.Registry.INSTANCE.getEPackage(FleetPackage.eNS_URI);

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
		volumeTierPriceParametersEClass.getESuperTypes().add(this.getLNGPriceCalculatorParameters());
		volumeTierSlotParamsEClass.getESuperTypes().add(this.getSlotContractParams());
		slotContractParamsEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		baseEntityBookEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		simpleEntityBookEClass.getESuperTypes().add(this.getBaseEntityBook());
		dateShiftExpressionPriceParametersEClass.getESuperTypes().add(this.getLNGPriceCalculatorParameters());
		genericCharterContractEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		genericCharterContractEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		simpleRepositioningFeeContainerEClass.getESuperTypes().add(this.getIRepositioningFee());
		simpleBallastBonusContainerEClass.getESuperTypes().add(this.getIBallastBonus());
		monthlyBallastBonusContainerEClass.getESuperTypes().add(this.getIBallastBonus());
		lumpSumBallastBonusTermEClass.getESuperTypes().add(this.getBallastBonusTerm());
		lumpSumBallastBonusTermEClass.getESuperTypes().add(this.getLumpSumTerm());
		notionalJourneyBallastBonusTermEClass.getESuperTypes().add(this.getBallastBonusTerm());
		notionalJourneyBallastBonusTermEClass.getESuperTypes().add(this.getNotionalJourneyTerm());
		monthlyBallastBonusTermEClass.getESuperTypes().add(this.getNotionalJourneyBallastBonusTerm());
		lumpSumRepositioningFeeTermEClass.getESuperTypes().add(this.getRepositioningFeeTerm());
		lumpSumRepositioningFeeTermEClass.getESuperTypes().add(this.getLumpSumTerm());
		originPortRepositioningFeeTermEClass.getESuperTypes().add(this.getRepositioningFeeTerm());
		originPortRepositioningFeeTermEClass.getESuperTypes().add(this.getNotionalJourneyTerm());
		regasPricingParamsEClass.getESuperTypes().add(this.getLNGPriceCalculatorParameters());

		// Initialize classes and features; add operations and parameters
		initEClass(commercialModelEClass, CommercialModel.class, "CommercialModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCommercialModel_Entities(), this.getBaseLegalEntity(), null, "entities", null, 0, -1, CommercialModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCommercialModel_SalesContracts(), this.getSalesContract(), null, "salesContracts", null, 0, -1, CommercialModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCommercialModel_PurchaseContracts(), this.getPurchaseContract(), null, "purchaseContracts", null, 0, -1, CommercialModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCommercialModel_CharterContracts(), this.getGenericCharterContract(), null, "charterContracts", null, 0, -1, CommercialModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(baseLegalEntityEClass, BaseLegalEntity.class, "BaseLegalEntity", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBaseLegalEntity_ShippingBook(), this.getBaseEntityBook(), null, "shippingBook", null, 0, 1, BaseLegalEntity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBaseLegalEntity_TradingBook(), this.getBaseEntityBook(), null, "tradingBook", null, 0, 1, BaseLegalEntity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBaseLegalEntity_UpstreamBook(), this.getBaseEntityBook(), null, "upstreamBook", null, 0, 1, BaseLegalEntity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBaseLegalEntity_ThirdParty(), ecorePackage.getEBoolean(), "thirdParty", null, 0, 1, BaseLegalEntity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(legalEntityEClass, LegalEntity.class, "LegalEntity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(contractEClass, Contract.class, "Contract", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getContract_Code(), ecorePackage.getEString(), "code", null, 0, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_Counterparty(), ecorePackage.getEString(), "counterparty", null, 0, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_Cn(), ecorePackage.getEString(), "cn", null, 0, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getContract_Entity(), this.getBaseLegalEntity(), null, "entity", null, 1, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_StartDate(), theDateTimePackage.getYearMonth(), "startDate", null, 0, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_EndDate(), theDateTimePackage.getYearMonth(), "endDate", null, 0, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_ContractYearStart(), ecorePackage.getEInt(), "contractYearStart", null, 0, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		EGenericType g1 = createEGenericType(theTypesPackage.getAPortSet());
		EGenericType g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getContract_AllowedPorts(), g1, null, "allowedPorts", null, 0, -1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getContract_PreferredPort(), thePortPackage.getPort(), null, "preferredPort", null, 0, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_MinQuantity(), ecorePackage.getEInt(), "minQuantity", null, 1, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_MaxQuantity(), ecorePackage.getEInt(), "maxQuantity", "140000", 1, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_VolumeLimitsUnit(), theTypesPackage.getVolumeUnits(), "volumeLimitsUnit", null, 1, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_OperationalTolerance(), ecorePackage.getEDouble(), "operationalTolerance", null, 1, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_FullCargoLot(), ecorePackage.getEBoolean(), "fullCargoLot", null, 0, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getContract_RestrictedContracts(), this.getContract(), null, "restrictedContracts", null, 0, -1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_RestrictedContractsArePermissive(), ecorePackage.getEBoolean(), "restrictedContractsArePermissive", null, 0, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getContract_RestrictedPorts(), g1, null, "restrictedPorts", null, 0, -1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_RestrictedPortsArePermissive(), ecorePackage.getEBoolean(), "restrictedPortsArePermissive", null, 0, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(theTypesPackage.getAVesselSet());
		g2 = createEGenericType(theFleetPackage.getVessel());
		g1.getETypeArguments().add(g2);
		initEReference(getContract_RestrictedVessels(), g1, null, "restrictedVessels", null, 0, -1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_RestrictedVesselsArePermissive(), ecorePackage.getEBoolean(), "restrictedVesselsArePermissive", null, 0, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getContract_PriceInfo(), this.getLNGPriceCalculatorParameters(), null, "priceInfo", null, 1, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_Notes(), ecorePackage.getEString(), "notes", null, 0, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_ContractType(), this.getContractType(), "contractType", null, 0, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_PricingEvent(), this.getPricingEvent(), "pricingEvent", null, 1, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_CancellationExpression(), ecorePackage.getEString(), "cancellationExpression", "0", 0, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContract_ShippingDaysRestriction(), ecorePackage.getEInt(), "shippingDaysRestriction", null, 0, 1, Contract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(salesContractEClass, SalesContract.class, "SalesContract", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSalesContract_MinCvValue(), ecorePackage.getEDouble(), "minCvValue", null, 0, 1, SalesContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSalesContract_MaxCvValue(), ecorePackage.getEDouble(), "maxCvValue", null, 0, 1, SalesContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSalesContract_PurchaseDeliveryType(), theTypesPackage.getCargoDeliveryType(), "PurchaseDeliveryType", "Any", 1, 1, SalesContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSalesContract_FobSaleDealType(), theTypesPackage.getFOBSaleDealType(), "fobSaleDealType", null, 0, 1, SalesContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(purchaseContractEClass, PurchaseContract.class, "PurchaseContract", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPurchaseContract_SalesDeliveryType(), theTypesPackage.getCargoDeliveryType(), "salesDeliveryType", "Any", 1, 1, PurchaseContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPurchaseContract_DesPurchaseDealType(), theTypesPackage.getDESPurchaseDealType(), "desPurchaseDealType", null, 0, 1, PurchaseContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPurchaseContract_CargoCV(), ecorePackage.getEDouble(), "cargoCV", null, 0, 1, PurchaseContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(taxRateEClass, TaxRate.class, "TaxRate", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTaxRate_Date(), theDateTimePackage.getLocalDate(), "date", null, 1, 1, TaxRate.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaxRate_Value(), ecorePackage.getEFloat(), "value", null, 1, 1, TaxRate.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(lngPriceCalculatorParametersEClass, LNGPriceCalculatorParameters.class, "LNGPriceCalculatorParameters", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(expressionPriceParametersEClass, ExpressionPriceParameters.class, "ExpressionPriceParameters", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getExpressionPriceParameters_PriceExpression(), ecorePackage.getEString(), "priceExpression", "", 1, 1, ExpressionPriceParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(volumeTierPriceParametersEClass, VolumeTierPriceParameters.class, "VolumeTierPriceParameters", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getVolumeTierPriceParameters_LowExpression(), ecorePackage.getEString(), "lowExpression", "", 1, 1, VolumeTierPriceParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVolumeTierPriceParameters_HighExpression(), ecorePackage.getEString(), "highExpression", "", 1, 1, VolumeTierPriceParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVolumeTierPriceParameters_Threshold(), ecorePackage.getEDouble(), "threshold", null, 0, 1, VolumeTierPriceParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVolumeTierPriceParameters_VolumeLimitsUnit(), theTypesPackage.getVolumeUnits(), "volumeLimitsUnit", null, 1, 1, VolumeTierPriceParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(volumeTierSlotParamsEClass, VolumeTierSlotParams.class, "VolumeTierSlotParams", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getVolumeTierSlotParams_LowExpression(), ecorePackage.getEString(), "lowExpression", "", 1, 1, VolumeTierSlotParams.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVolumeTierSlotParams_HighExpression(), ecorePackage.getEString(), "highExpression", "", 1, 1, VolumeTierSlotParams.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVolumeTierSlotParams_Threshold(), ecorePackage.getEDouble(), "threshold", null, 0, 1, VolumeTierSlotParams.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVolumeTierSlotParams_VolumeLimitsUnit(), theTypesPackage.getVolumeUnits(), "volumeLimitsUnit", null, 1, 1, VolumeTierSlotParams.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVolumeTierSlotParams_OverrideContract(), ecorePackage.getEBoolean(), "overrideContract", null, 1, 1, VolumeTierSlotParams.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

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

		initEClass(genericCharterContractEClass, GenericCharterContract.class, "GenericCharterContract", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getGenericCharterContract_MinDuration(), ecorePackage.getEInt(), "minDuration", null, 1, 1, GenericCharterContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGenericCharterContract_MaxDuration(), ecorePackage.getEInt(), "maxDuration", null, 1, 1, GenericCharterContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGenericCharterContract_RepositioningFeeTerms(), this.getIRepositioningFee(), null, "repositioningFeeTerms", null, 0, 1, GenericCharterContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGenericCharterContract_BallastBonusTerms(), this.getIBallastBonus(), null, "ballastBonusTerms", null, 0, 1, GenericCharterContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGenericCharterContract_StartHeel(), this.getStartHeelOptions(), null, "startHeel", null, 1, 1, GenericCharterContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGenericCharterContract_EndHeel(), this.getEndHeelOptions(), null, "endHeel", null, 0, 1, GenericCharterContract.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(iRepositioningFeeEClass, IRepositioningFee.class, "IRepositioningFee", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(simpleRepositioningFeeContainerEClass, SimpleRepositioningFeeContainer.class, "SimpleRepositioningFeeContainer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSimpleRepositioningFeeContainer_Terms(), this.getRepositioningFeeTerm(), null, "terms", null, 0, -1, SimpleRepositioningFeeContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(iBallastBonusEClass, IBallastBonus.class, "IBallastBonus", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(simpleBallastBonusContainerEClass, SimpleBallastBonusContainer.class, "SimpleBallastBonusContainer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSimpleBallastBonusContainer_Terms(), this.getBallastBonusTerm(), null, "terms", null, 0, -1, SimpleBallastBonusContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(monthlyBallastBonusContainerEClass, MonthlyBallastBonusContainer.class, "MonthlyBallastBonusContainer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getMonthlyBallastBonusContainer_Hubs(), g1, null, "hubs", null, 0, -1, MonthlyBallastBonusContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMonthlyBallastBonusContainer_Terms(), this.getMonthlyBallastBonusTerm(), null, "terms", null, 0, -1, MonthlyBallastBonusContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(lumpSumTermEClass, LumpSumTerm.class, "LumpSumTerm", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getLumpSumTerm_PriceExpression(), ecorePackage.getEString(), "priceExpression", "", 1, 1, LumpSumTerm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(notionalJourneyTermEClass, NotionalJourneyTerm.class, "NotionalJourneyTerm", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getNotionalJourneyTerm_Speed(), ecorePackage.getEDouble(), "speed", "0", 1, 1, NotionalJourneyTerm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNotionalJourneyTerm_FuelPriceExpression(), ecorePackage.getEString(), "fuelPriceExpression", "", 1, 1, NotionalJourneyTerm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNotionalJourneyTerm_HirePriceExpression(), ecorePackage.getEString(), "hirePriceExpression", "", 1, 1, NotionalJourneyTerm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNotionalJourneyTerm_IncludeCanal(), ecorePackage.getEBoolean(), "includeCanal", null, 0, 1, NotionalJourneyTerm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNotionalJourneyTerm_IncludeCanalTime(), ecorePackage.getEBoolean(), "includeCanalTime", "true", 0, 1, NotionalJourneyTerm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNotionalJourneyTerm_LumpSumPriceExpression(), ecorePackage.getEString(), "lumpSumPriceExpression", null, 1, 1, NotionalJourneyTerm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(ballastBonusTermEClass, BallastBonusTerm.class, "BallastBonusTerm", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getBallastBonusTerm_RedeliveryPorts(), g1, null, "redeliveryPorts", null, 0, -1, BallastBonusTerm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(lumpSumBallastBonusTermEClass, LumpSumBallastBonusTerm.class, "LumpSumBallastBonusTerm", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(notionalJourneyBallastBonusTermEClass, NotionalJourneyBallastBonusTerm.class, "NotionalJourneyBallastBonusTerm", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getNotionalJourneyBallastBonusTerm_ReturnPorts(), g1, null, "returnPorts", null, 0, -1, NotionalJourneyBallastBonusTerm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(monthlyBallastBonusTermEClass, MonthlyBallastBonusTerm.class, "MonthlyBallastBonusTerm", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMonthlyBallastBonusTerm_Month(), theDateTimePackage.getYearMonth(), "month", null, 0, 1, MonthlyBallastBonusTerm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMonthlyBallastBonusTerm_BallastBonusTo(), this.getNextPortType(), "ballastBonusTo", null, 0, 1, MonthlyBallastBonusTerm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMonthlyBallastBonusTerm_BallastBonusPctFuel(), ecorePackage.getEString(), "ballastBonusPctFuel", null, 0, 1, MonthlyBallastBonusTerm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMonthlyBallastBonusTerm_BallastBonusPctCharter(), ecorePackage.getEString(), "ballastBonusPctCharter", null, 0, 1, MonthlyBallastBonusTerm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(repositioningFeeTermEClass, RepositioningFeeTerm.class, "RepositioningFeeTerm", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(thePortPackage.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getRepositioningFeeTerm_StartPorts(), g1, null, "startPorts", null, 0, -1, RepositioningFeeTerm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(lumpSumRepositioningFeeTermEClass, LumpSumRepositioningFeeTerm.class, "LumpSumRepositioningFeeTerm", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(originPortRepositioningFeeTermEClass, OriginPortRepositioningFeeTerm.class, "OriginPortRepositioningFeeTerm", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getOriginPortRepositioningFeeTerm_OriginPort(), thePortPackage.getPort(), null, "originPort", null, 0, 1, OriginPortRepositioningFeeTerm.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(endHeelOptionsEClass, EndHeelOptions.class, "EndHeelOptions", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEndHeelOptions_TankState(), this.getEVesselTankState(), "tankState", null, 0, 1, EndHeelOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEndHeelOptions_MinimumEndHeel(), ecorePackage.getEInt(), "minimumEndHeel", null, 0, 1, EndHeelOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEndHeelOptions_MaximumEndHeel(), ecorePackage.getEInt(), "maximumEndHeel", null, 0, 1, EndHeelOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEndHeelOptions_UseLastHeelPrice(), ecorePackage.getEBoolean(), "useLastHeelPrice", null, 0, 1, EndHeelOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEndHeelOptions_PriceExpression(), ecorePackage.getEString(), "priceExpression", null, 0, 1, EndHeelOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(startHeelOptionsEClass, StartHeelOptions.class, "StartHeelOptions", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getStartHeelOptions_CvValue(), ecorePackage.getEDouble(), "cvValue", null, 1, 1, StartHeelOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStartHeelOptions_MinVolumeAvailable(), ecorePackage.getEDouble(), "minVolumeAvailable", null, 1, 1, StartHeelOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStartHeelOptions_MaxVolumeAvailable(), ecorePackage.getEDouble(), "maxVolumeAvailable", null, 1, 1, StartHeelOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStartHeelOptions_PriceExpression(), ecorePackage.getEString(), "priceExpression", null, 0, 1, StartHeelOptions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(regasPricingParamsEClass, RegasPricingParams.class, "RegasPricingParams", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getRegasPricingParams_PriceExpression(), ecorePackage.getEString(), "priceExpression", null, 1, 1, RegasPricingParams.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRegasPricingParams_NumPricingDays(), ecorePackage.getEInt(), "numPricingDays", null, 0, 1, RegasPricingParams.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

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

		initEEnum(nextPortTypeEEnum, NextPortType.class, "NextPortType");
		addEEnumLiteral(nextPortTypeEEnum, NextPortType.LOAD_PORT);
		addEEnumLiteral(nextPortTypeEEnum, NextPortType.NEAREST_HUB);

		initEEnum(eVesselTankStateEEnum, EVesselTankState.class, "EVesselTankState");
		addEEnumLiteral(eVesselTankStateEEnum, EVesselTankState.EITHER);
		addEEnumLiteral(eVesselTankStateEEnum, EVesselTankState.MUST_BE_COLD);
		addEEnumLiteral(eVesselTankStateEEnum, EVesselTankState.MUST_BE_WARM);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.mmxlabs.com/models/ui/numberFormat
		createNumberFormatAnnotations();
		// http://www.mmxlabs.com/models/validation
		createValidationAnnotations();
		// http://www.mmxlabs.com/models/pricing/expressionType
		createExpressionTypeAnnotations();
		// slotContractParams
		createSlotContractParamsAnnotations();
		// http://minimaxlabs.com/models/commercial/slot/parameters
		createParametersAnnotations();
		// http://minimaxlabs.com/models/commercial/slot/expression
		createExpressionAnnotations();
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
		  (getContract_OperationalTolerance(),
		   source,
		   new String[] {
			   "scale", "100",
			   "formatString", "##0.#",
			   "unit", "%",
			   "exportFormatString", "#.###",
			   "unitPrefix", "\u00b1"
		   });
		addAnnotation
		  (getContract_ShippingDaysRestriction(),
		   source,
		   new String[] {
			   "unit", "days",
			   "formatString", "###"
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
		  (getGenericCharterContract_MinDuration(),
		   source,
		   new String[] {
			   "unit", "days",
			   "formatString", "##0"
		   });
		addAnnotation
		  (getGenericCharterContract_MaxDuration(),
		   source,
		   new String[] {
			   "unit", "days",
			   "formatString", "##0"
		   });
		addAnnotation
		  (getNotionalJourneyTerm_HirePriceExpression(),
		   source,
		   new String[] {
			   "unit", "$/day"
		   });
		addAnnotation
		  (getEndHeelOptions_MinimumEndHeel(),
		   source,
		   new String[] {
			   "unit", "m\u00b3",
			   "formatString", "###,##0.###"
		   });
		addAnnotation
		  (getEndHeelOptions_MaximumEndHeel(),
		   source,
		   new String[] {
			   "unit", "m\u00b3",
			   "formatString", "###,##0.###"
		   });
		addAnnotation
		  (getStartHeelOptions_CvValue(),
		   source,
		   new String[] {
			   "unit", "mmBtu/m\u00b3",
			   "formatString", "#0.######"
		   });
		addAnnotation
		  (getStartHeelOptions_MinVolumeAvailable(),
		   source,
		   new String[] {
			   "unit", "m\u00b3",
			   "formatString", "###,##0.###"
		   });
		addAnnotation
		  (getStartHeelOptions_MaxVolumeAvailable(),
		   source,
		   new String[] {
			   "unit", "m\u00b3",
			   "formatString", "###,##0.###"
		   });
	}

	/**
	 * Initializes the annotations for <b>http://www.mmxlabs.com/models/validation</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createValidationAnnotations() {
		String source = "http://www.mmxlabs.com/models/validation";
		addAnnotation
		  (getContract_Notes(),
		   source,
		   new String[] {
			   "ignore", "true"
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
		  (getVolumeTierPriceParameters_LowExpression(),
		   source,
		   new String[] {
			   "type", "commodity"
		   });
		addAnnotation
		  (getVolumeTierPriceParameters_HighExpression(),
		   source,
		   new String[] {
			   "type", "commodity"
		   });
		addAnnotation
		  (getVolumeTierSlotParams_LowExpression(),
		   source,
		   new String[] {
			   "type", "commodity"
		   });
		addAnnotation
		  (getVolumeTierSlotParams_HighExpression(),
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
		  (getLumpSumTerm_PriceExpression(),
		   source,
		   new String[] {
			   "type", "charter"
		   });
		addAnnotation
		  (getNotionalJourneyTerm_Speed(),
		   source,
		   new String[] {
			   "type", "commodity"
		   });
		addAnnotation
		  (getNotionalJourneyTerm_FuelPriceExpression(),
		   source,
		   new String[] {
			   "type", "basefuel"
		   });
		addAnnotation
		  (getNotionalJourneyTerm_HirePriceExpression(),
		   source,
		   new String[] {
			   "type", "charter"
		   });
		addAnnotation
		  (getNotionalJourneyTerm_LumpSumPriceExpression(),
		   source,
		   new String[] {
			   "type", "charter"
		   });
		addAnnotation
		  (getEndHeelOptions_PriceExpression(),
		   source,
		   new String[] {
			   "type", "commodity"
		   });
		addAnnotation
		  (getStartHeelOptions_PriceExpression(),
		   source,
		   new String[] {
			   "type", "commodity"
		   });
		addAnnotation
		  (getRegasPricingParams_PriceExpression(),
		   source,
		   new String[] {
			   "type", "commodity"
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

	/**
	 * Initializes the annotations for <b>http://minimaxlabs.com/models/commercial/slot/parameters</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createParametersAnnotations() {
		String source = "http://minimaxlabs.com/models/commercial/slot/parameters";
		addAnnotation
		  (volumeTierPriceParametersEClass,
		   source,
		   new String[] {
		   },
		   new URI[] {
			 URI.createURI(eNS_URI).appendFragment("//VolumeTierSlotParams")
		   });
	}

	/**
	 * Initializes the annotations for <b>http://minimaxlabs.com/models/commercial/slot/expression</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createExpressionAnnotations() {
		String source = "http://minimaxlabs.com/models/commercial/slot/expression";
		addAnnotation
		  (volumeTierPriceParametersEClass,
		   source,
		   new String[] {
			   "allowExpressionOverride", "false"
		   });
	}

} //CommercialPackageImpl
