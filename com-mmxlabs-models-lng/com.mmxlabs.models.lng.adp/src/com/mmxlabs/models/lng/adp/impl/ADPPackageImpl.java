/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.datetime.DateTimePackage;

import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.BindingRule;
import com.mmxlabs.models.lng.adp.CargoByQuarterDistributionModel;
import com.mmxlabs.models.lng.adp.CargoIntervalDistributionModel;
import com.mmxlabs.models.lng.adp.CargoNumberDistributionModel;
import com.mmxlabs.models.lng.adp.CargoSizeDistributionModel;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.CustomSubProfileAttributes;
import com.mmxlabs.models.lng.adp.DeliverToFlow;
import com.mmxlabs.models.lng.adp.DeliverToProfileFlow;
import com.mmxlabs.models.lng.adp.DeliverToSpotFlow;
import com.mmxlabs.models.lng.adp.DistributionModel;
import com.mmxlabs.models.lng.adp.FlowType;
import com.mmxlabs.models.lng.adp.IntervalType;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.adp.ShippingOption;

import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.SupplyFromFlow;
import com.mmxlabs.models.lng.adp.SupplyFromProfileFlow;
import com.mmxlabs.models.lng.adp.SupplyFromSpotFlow;
import com.mmxlabs.models.lng.cargo.CargoPackage;

import com.mmxlabs.models.lng.commercial.CommercialPackage;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.port.PortPackage;

import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.types.TypesPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ADPPackageImpl extends EPackageImpl implements ADPPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass adpModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass contractProfileEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass distributionModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass cargoSizeDistributionModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass cargoNumberDistributionModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass purchaseContractProfileEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass salesContractProfileEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass subContractProfileEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass customSubProfileAttributesEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass cargoByQuarterDistributionModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass cargoIntervalDistributionModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass bindingRuleEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass flowTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass supplyFromFlowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass deliverToFlowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass supplyFromProfileFlowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass deliverToProfileFlowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass supplyFromSpotFlowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass deliverToSpotFlowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass shippingOptionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum intervalTypeEEnum = null;

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
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ADPPackageImpl() {
		super(eNS_URI, ADPFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link ADPPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static ADPPackage init() {
		if (isInited) return (ADPPackage)EPackage.Registry.INSTANCE.getEPackage(ADPPackage.eNS_URI);

		// Obtain or create and register package
		ADPPackageImpl theADPPackage = (ADPPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ADPPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ADPPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		CargoPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theADPPackage.createPackageContents();

		// Initialize created meta-data
		theADPPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theADPPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(ADPPackage.eNS_URI, theADPPackage);
		return theADPPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getADPModel() {
		return adpModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getADPModel_YearStart() {
		return (EAttribute)adpModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getADPModel_PurchaseContractProfiles() {
		return (EReference)adpModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getADPModel_SalesContractProfiles() {
		return (EReference)adpModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getADPModel_BindingRules() {
		return (EReference)adpModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getContractProfile() {
		return contractProfileEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getContractProfile_Contract() {
		return (EReference)contractProfileEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContractProfile_ContractCode() {
		return (EAttribute)contractProfileEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContractProfile_Custom() {
		return (EAttribute)contractProfileEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContractProfile_Enabled() {
		return (EAttribute)contractProfileEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContractProfile_TotalVolume() {
		return (EAttribute)contractProfileEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContractProfile_VolumeUnit() {
		return (EAttribute)contractProfileEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getContractProfile_DistributionModel() {
		return (EReference)contractProfileEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getContractProfile_SubProfiles() {
		return (EReference)contractProfileEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDistributionModel() {
		return distributionModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCargoSizeDistributionModel() {
		return cargoSizeDistributionModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCargoSizeDistributionModel_CargoSize() {
		return (EAttribute)cargoSizeDistributionModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCargoSizeDistributionModel_Exact() {
		return (EAttribute)cargoSizeDistributionModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCargoNumberDistributionModel() {
		return cargoNumberDistributionModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCargoNumberDistributionModel_NumberOfCargoes() {
		return (EAttribute)cargoNumberDistributionModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPurchaseContractProfile() {
		return purchaseContractProfileEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSalesContractProfile() {
		return salesContractProfileEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSubContractProfile() {
		return subContractProfileEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSubContractProfile_Name() {
		return (EAttribute)subContractProfileEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSubContractProfile_DistributionModel() {
		return (EReference)subContractProfileEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSubContractProfile_SlotTemplateId() {
		return (EAttribute)subContractProfileEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSubContractProfile_CustomAttribs() {
		return (EReference)subContractProfileEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSubContractProfile_Slots() {
		return (EReference)subContractProfileEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCustomSubProfileAttributes() {
		return customSubProfileAttributesEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCargoByQuarterDistributionModel() {
		return cargoByQuarterDistributionModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCargoByQuarterDistributionModel_Q1() {
		return (EAttribute)cargoByQuarterDistributionModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCargoByQuarterDistributionModel_Q2() {
		return (EAttribute)cargoByQuarterDistributionModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCargoByQuarterDistributionModel_Q3() {
		return (EAttribute)cargoByQuarterDistributionModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCargoByQuarterDistributionModel_Q4() {
		return (EAttribute)cargoByQuarterDistributionModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCargoIntervalDistributionModel() {
		return cargoIntervalDistributionModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCargoIntervalDistributionModel_Quantity() {
		return (EAttribute)cargoIntervalDistributionModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCargoIntervalDistributionModel_IntervalType() {
		return (EAttribute)cargoIntervalDistributionModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCargoIntervalDistributionModel_Spacing() {
		return (EAttribute)cargoIntervalDistributionModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBindingRule() {
		return bindingRuleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBindingRule_Profile() {
		return (EReference)bindingRuleEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBindingRule_SubProfile() {
		return (EReference)bindingRuleEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBindingRule_FlowType() {
		return (EReference)bindingRuleEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBindingRule_ShippingOption() {
		return (EReference)bindingRuleEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFlowType() {
		return flowTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSupplyFromFlow() {
		return supplyFromFlowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDeliverToFlow() {
		return deliverToFlowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSupplyFromProfileFlow() {
		return supplyFromProfileFlowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSupplyFromProfileFlow_Profile() {
		return (EReference)supplyFromProfileFlowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSupplyFromProfileFlow_SubProfile() {
		return (EReference)supplyFromProfileFlowEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDeliverToProfileFlow() {
		return deliverToProfileFlowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDeliverToProfileFlow_Profile() {
		return (EReference)deliverToProfileFlowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDeliverToProfileFlow_SubProfile() {
		return (EReference)deliverToProfileFlowEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSupplyFromSpotFlow() {
		return supplyFromSpotFlowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSupplyFromSpotFlow_Market() {
		return (EReference)supplyFromSpotFlowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDeliverToSpotFlow() {
		return deliverToSpotFlowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDeliverToSpotFlow_Market() {
		return (EReference)deliverToSpotFlowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getShippingOption() {
		return shippingOptionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getShippingOption_VesselAssignmentType() {
		return (EReference)shippingOptionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getShippingOption_SpotIndex() {
		return (EAttribute)shippingOptionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getShippingOption_Vessel() {
		return (EReference)shippingOptionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getShippingOption_MaxLadenIdleDays() {
		return (EAttribute)shippingOptionEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getIntervalType() {
		return intervalTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ADPFactory getADPFactory() {
		return (ADPFactory)getEFactoryInstance();
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
		adpModelEClass = createEClass(ADP_MODEL);
		createEAttribute(adpModelEClass, ADP_MODEL__YEAR_START);
		createEReference(adpModelEClass, ADP_MODEL__PURCHASE_CONTRACT_PROFILES);
		createEReference(adpModelEClass, ADP_MODEL__SALES_CONTRACT_PROFILES);
		createEReference(adpModelEClass, ADP_MODEL__BINDING_RULES);

		contractProfileEClass = createEClass(CONTRACT_PROFILE);
		createEReference(contractProfileEClass, CONTRACT_PROFILE__CONTRACT);
		createEAttribute(contractProfileEClass, CONTRACT_PROFILE__CONTRACT_CODE);
		createEAttribute(contractProfileEClass, CONTRACT_PROFILE__CUSTOM);
		createEAttribute(contractProfileEClass, CONTRACT_PROFILE__ENABLED);
		createEAttribute(contractProfileEClass, CONTRACT_PROFILE__TOTAL_VOLUME);
		createEAttribute(contractProfileEClass, CONTRACT_PROFILE__VOLUME_UNIT);
		createEReference(contractProfileEClass, CONTRACT_PROFILE__DISTRIBUTION_MODEL);
		createEReference(contractProfileEClass, CONTRACT_PROFILE__SUB_PROFILES);

		purchaseContractProfileEClass = createEClass(PURCHASE_CONTRACT_PROFILE);

		salesContractProfileEClass = createEClass(SALES_CONTRACT_PROFILE);

		subContractProfileEClass = createEClass(SUB_CONTRACT_PROFILE);
		createEAttribute(subContractProfileEClass, SUB_CONTRACT_PROFILE__NAME);
		createEReference(subContractProfileEClass, SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL);
		createEAttribute(subContractProfileEClass, SUB_CONTRACT_PROFILE__SLOT_TEMPLATE_ID);
		createEReference(subContractProfileEClass, SUB_CONTRACT_PROFILE__CUSTOM_ATTRIBS);
		createEReference(subContractProfileEClass, SUB_CONTRACT_PROFILE__SLOTS);

		customSubProfileAttributesEClass = createEClass(CUSTOM_SUB_PROFILE_ATTRIBUTES);

		distributionModelEClass = createEClass(DISTRIBUTION_MODEL);

		cargoSizeDistributionModelEClass = createEClass(CARGO_SIZE_DISTRIBUTION_MODEL);
		createEAttribute(cargoSizeDistributionModelEClass, CARGO_SIZE_DISTRIBUTION_MODEL__CARGO_SIZE);
		createEAttribute(cargoSizeDistributionModelEClass, CARGO_SIZE_DISTRIBUTION_MODEL__EXACT);

		cargoNumberDistributionModelEClass = createEClass(CARGO_NUMBER_DISTRIBUTION_MODEL);
		createEAttribute(cargoNumberDistributionModelEClass, CARGO_NUMBER_DISTRIBUTION_MODEL__NUMBER_OF_CARGOES);

		cargoByQuarterDistributionModelEClass = createEClass(CARGO_BY_QUARTER_DISTRIBUTION_MODEL);
		createEAttribute(cargoByQuarterDistributionModelEClass, CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q1);
		createEAttribute(cargoByQuarterDistributionModelEClass, CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q2);
		createEAttribute(cargoByQuarterDistributionModelEClass, CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q3);
		createEAttribute(cargoByQuarterDistributionModelEClass, CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q4);

		cargoIntervalDistributionModelEClass = createEClass(CARGO_INTERVAL_DISTRIBUTION_MODEL);
		createEAttribute(cargoIntervalDistributionModelEClass, CARGO_INTERVAL_DISTRIBUTION_MODEL__QUANTITY);
		createEAttribute(cargoIntervalDistributionModelEClass, CARGO_INTERVAL_DISTRIBUTION_MODEL__INTERVAL_TYPE);
		createEAttribute(cargoIntervalDistributionModelEClass, CARGO_INTERVAL_DISTRIBUTION_MODEL__SPACING);

		bindingRuleEClass = createEClass(BINDING_RULE);
		createEReference(bindingRuleEClass, BINDING_RULE__PROFILE);
		createEReference(bindingRuleEClass, BINDING_RULE__SUB_PROFILE);
		createEReference(bindingRuleEClass, BINDING_RULE__FLOW_TYPE);
		createEReference(bindingRuleEClass, BINDING_RULE__SHIPPING_OPTION);

		flowTypeEClass = createEClass(FLOW_TYPE);

		supplyFromFlowEClass = createEClass(SUPPLY_FROM_FLOW);

		deliverToFlowEClass = createEClass(DELIVER_TO_FLOW);

		supplyFromProfileFlowEClass = createEClass(SUPPLY_FROM_PROFILE_FLOW);
		createEReference(supplyFromProfileFlowEClass, SUPPLY_FROM_PROFILE_FLOW__PROFILE);
		createEReference(supplyFromProfileFlowEClass, SUPPLY_FROM_PROFILE_FLOW__SUB_PROFILE);

		deliverToProfileFlowEClass = createEClass(DELIVER_TO_PROFILE_FLOW);
		createEReference(deliverToProfileFlowEClass, DELIVER_TO_PROFILE_FLOW__PROFILE);
		createEReference(deliverToProfileFlowEClass, DELIVER_TO_PROFILE_FLOW__SUB_PROFILE);

		supplyFromSpotFlowEClass = createEClass(SUPPLY_FROM_SPOT_FLOW);
		createEReference(supplyFromSpotFlowEClass, SUPPLY_FROM_SPOT_FLOW__MARKET);

		deliverToSpotFlowEClass = createEClass(DELIVER_TO_SPOT_FLOW);
		createEReference(deliverToSpotFlowEClass, DELIVER_TO_SPOT_FLOW__MARKET);

		shippingOptionEClass = createEClass(SHIPPING_OPTION);
		createEReference(shippingOptionEClass, SHIPPING_OPTION__VESSEL_ASSIGNMENT_TYPE);
		createEAttribute(shippingOptionEClass, SHIPPING_OPTION__SPOT_INDEX);
		createEReference(shippingOptionEClass, SHIPPING_OPTION__VESSEL);
		createEAttribute(shippingOptionEClass, SHIPPING_OPTION__MAX_LADEN_IDLE_DAYS);

		// Create enums
		intervalTypeEEnum = createEEnum(INTERVAL_TYPE);
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
		DateTimePackage theDateTimePackage = (DateTimePackage)EPackage.Registry.INSTANCE.getEPackage(DateTimePackage.eNS_URI);
		CargoPackage theCargoPackage = (CargoPackage)EPackage.Registry.INSTANCE.getEPackage(CargoPackage.eNS_URI);
		CommercialPackage theCommercialPackage = (CommercialPackage)EPackage.Registry.INSTANCE.getEPackage(CommercialPackage.eNS_URI);
		TypesPackage theTypesPackage = (TypesPackage)EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);
		SpotMarketsPackage theSpotMarketsPackage = (SpotMarketsPackage)EPackage.Registry.INSTANCE.getEPackage(SpotMarketsPackage.eNS_URI);
		FleetPackage theFleetPackage = (FleetPackage)EPackage.Registry.INSTANCE.getEPackage(FleetPackage.eNS_URI);

		// Create type parameters
		ETypeParameter contractProfileEClass_T = addETypeParameter(contractProfileEClass, "T");
		ETypeParameter subContractProfileEClass_T = addETypeParameter(subContractProfileEClass, "T");

		// Set bounds for type parameters
		EGenericType g1 = createEGenericType(theCargoPackage.getSlot());
		contractProfileEClass_T.getEBounds().add(g1);
		g1 = createEGenericType(theCargoPackage.getSlot());
		subContractProfileEClass_T.getEBounds().add(g1);

		// Add supertypes to classes
		g1 = createEGenericType(this.getContractProfile());
		EGenericType g2 = createEGenericType(theCargoPackage.getLoadSlot());
		g1.getETypeArguments().add(g2);
		purchaseContractProfileEClass.getEGenericSuperTypes().add(g1);
		g1 = createEGenericType(this.getContractProfile());
		g2 = createEGenericType(theCargoPackage.getDischargeSlot());
		g1.getETypeArguments().add(g2);
		salesContractProfileEClass.getEGenericSuperTypes().add(g1);
		cargoSizeDistributionModelEClass.getESuperTypes().add(this.getDistributionModel());
		cargoNumberDistributionModelEClass.getESuperTypes().add(this.getDistributionModel());
		cargoByQuarterDistributionModelEClass.getESuperTypes().add(this.getDistributionModel());
		cargoIntervalDistributionModelEClass.getESuperTypes().add(this.getDistributionModel());
		supplyFromFlowEClass.getESuperTypes().add(this.getFlowType());
		deliverToFlowEClass.getESuperTypes().add(this.getFlowType());
		supplyFromProfileFlowEClass.getESuperTypes().add(this.getSupplyFromFlow());
		deliverToProfileFlowEClass.getESuperTypes().add(this.getDeliverToFlow());
		supplyFromSpotFlowEClass.getESuperTypes().add(this.getSupplyFromFlow());
		deliverToSpotFlowEClass.getESuperTypes().add(this.getDeliverToFlow());

		// Initialize classes, features, and operations; add parameters
		initEClass(adpModelEClass, ADPModel.class, "ADPModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getADPModel_YearStart(), theDateTimePackage.getYearMonth(), "yearStart", null, 0, 1, ADPModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getADPModel_PurchaseContractProfiles(), this.getPurchaseContractProfile(), null, "purchaseContractProfiles", null, 0, -1, ADPModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getADPModel_SalesContractProfiles(), this.getSalesContractProfile(), null, "salesContractProfiles", null, 0, -1, ADPModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getADPModel_BindingRules(), this.getBindingRule(), null, "bindingRules", null, 0, -1, ADPModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(contractProfileEClass, ContractProfile.class, "ContractProfile", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getContractProfile_Contract(), theCommercialPackage.getContract(), null, "contract", null, 0, 1, ContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContractProfile_ContractCode(), ecorePackage.getEString(), "contractCode", null, 0, 1, ContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContractProfile_Custom(), ecorePackage.getEBoolean(), "custom", null, 0, 1, ContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContractProfile_Enabled(), ecorePackage.getEBoolean(), "enabled", null, 0, 1, ContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContractProfile_TotalVolume(), ecorePackage.getEInt(), "totalVolume", null, 0, 1, ContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContractProfile_VolumeUnit(), theTypesPackage.getVolumeUnits(), "volumeUnit", null, 0, 1, ContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getContractProfile_DistributionModel(), this.getDistributionModel(), null, "distributionModel", null, 0, 1, ContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(this.getSubContractProfile());
		g2 = createEGenericType(contractProfileEClass_T);
		g1.getETypeArguments().add(g2);
		initEReference(getContractProfile_SubProfiles(), g1, null, "subProfiles", null, 0, -1, ContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(purchaseContractProfileEClass, PurchaseContractProfile.class, "PurchaseContractProfile", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(salesContractProfileEClass, SalesContractProfile.class, "SalesContractProfile", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(subContractProfileEClass, SubContractProfile.class, "SubContractProfile", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSubContractProfile_Name(), ecorePackage.getEString(), "name", null, 0, 1, SubContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSubContractProfile_DistributionModel(), this.getDistributionModel(), null, "distributionModel", null, 0, 1, SubContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSubContractProfile_SlotTemplateId(), ecorePackage.getEString(), "slotTemplateId", null, 0, 1, SubContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSubContractProfile_CustomAttribs(), this.getCustomSubProfileAttributes(), null, "customAttribs", null, 0, 1, SubContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(subContractProfileEClass_T);
		initEReference(getSubContractProfile_Slots(), g1, null, "slots", null, 0, -1, SubContractProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(customSubProfileAttributesEClass, CustomSubProfileAttributes.class, "CustomSubProfileAttributes", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(distributionModelEClass, DistributionModel.class, "DistributionModel", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(cargoSizeDistributionModelEClass, CargoSizeDistributionModel.class, "CargoSizeDistributionModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCargoSizeDistributionModel_CargoSize(), ecorePackage.getEInt(), "cargoSize", null, 0, 1, CargoSizeDistributionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCargoSizeDistributionModel_Exact(), ecorePackage.getEBoolean(), "exact", null, 0, 1, CargoSizeDistributionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(cargoNumberDistributionModelEClass, CargoNumberDistributionModel.class, "CargoNumberDistributionModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCargoNumberDistributionModel_NumberOfCargoes(), ecorePackage.getEInt(), "numberOfCargoes", null, 0, 1, CargoNumberDistributionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(cargoByQuarterDistributionModelEClass, CargoByQuarterDistributionModel.class, "CargoByQuarterDistributionModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCargoByQuarterDistributionModel_Q1(), ecorePackage.getEInt(), "q1", null, 0, 1, CargoByQuarterDistributionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCargoByQuarterDistributionModel_Q2(), ecorePackage.getEInt(), "q2", null, 0, 1, CargoByQuarterDistributionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCargoByQuarterDistributionModel_Q3(), ecorePackage.getEInt(), "q3", null, 0, 1, CargoByQuarterDistributionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCargoByQuarterDistributionModel_Q4(), ecorePackage.getEInt(), "q4", null, 0, 1, CargoByQuarterDistributionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(cargoIntervalDistributionModelEClass, CargoIntervalDistributionModel.class, "CargoIntervalDistributionModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCargoIntervalDistributionModel_Quantity(), ecorePackage.getEInt(), "quantity", null, 0, 1, CargoIntervalDistributionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCargoIntervalDistributionModel_IntervalType(), this.getIntervalType(), "intervalType", null, 0, 1, CargoIntervalDistributionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCargoIntervalDistributionModel_Spacing(), ecorePackage.getEInt(), "spacing", null, 0, 1, CargoIntervalDistributionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(bindingRuleEClass, BindingRule.class, "BindingRule", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(this.getContractProfile());
		g2 = createEGenericType();
		g1.getETypeArguments().add(g2);
		initEReference(getBindingRule_Profile(), g1, null, "profile", null, 0, 1, BindingRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(this.getSubContractProfile());
		g2 = createEGenericType();
		g1.getETypeArguments().add(g2);
		initEReference(getBindingRule_SubProfile(), g1, null, "subProfile", null, 0, 1, BindingRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBindingRule_FlowType(), this.getFlowType(), null, "flowType", null, 0, 1, BindingRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBindingRule_ShippingOption(), this.getShippingOption(), null, "shippingOption", null, 0, 1, BindingRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(flowTypeEClass, FlowType.class, "FlowType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(supplyFromFlowEClass, SupplyFromFlow.class, "SupplyFromFlow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(deliverToFlowEClass, DeliverToFlow.class, "DeliverToFlow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(supplyFromProfileFlowEClass, SupplyFromProfileFlow.class, "SupplyFromProfileFlow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(this.getContractProfile());
		g2 = createEGenericType();
		g1.getETypeArguments().add(g2);
		initEReference(getSupplyFromProfileFlow_Profile(), g1, null, "profile", null, 0, 1, SupplyFromProfileFlow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(this.getSubContractProfile());
		g2 = createEGenericType(theCargoPackage.getLoadSlot());
		g1.getETypeArguments().add(g2);
		initEReference(getSupplyFromProfileFlow_SubProfile(), g1, null, "subProfile", null, 0, 1, SupplyFromProfileFlow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(deliverToProfileFlowEClass, DeliverToProfileFlow.class, "DeliverToProfileFlow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(this.getContractProfile());
		g2 = createEGenericType();
		g1.getETypeArguments().add(g2);
		initEReference(getDeliverToProfileFlow_Profile(), g1, null, "profile", null, 0, 1, DeliverToProfileFlow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(this.getSubContractProfile());
		g2 = createEGenericType(theCargoPackage.getDischargeSlot());
		g1.getETypeArguments().add(g2);
		initEReference(getDeliverToProfileFlow_SubProfile(), g1, null, "subProfile", null, 0, 1, DeliverToProfileFlow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(supplyFromSpotFlowEClass, SupplyFromSpotFlow.class, "SupplyFromSpotFlow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSupplyFromSpotFlow_Market(), theSpotMarketsPackage.getSpotMarket(), null, "market", null, 0, 1, SupplyFromSpotFlow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(deliverToSpotFlowEClass, DeliverToSpotFlow.class, "DeliverToSpotFlow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDeliverToSpotFlow_Market(), theSpotMarketsPackage.getSpotMarket(), null, "market", null, 0, 1, DeliverToSpotFlow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(shippingOptionEClass, ShippingOption.class, "ShippingOption", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getShippingOption_VesselAssignmentType(), theTypesPackage.getVesselAssignmentType(), null, "vesselAssignmentType", null, 0, 1, ShippingOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getShippingOption_SpotIndex(), ecorePackage.getEInt(), "spotIndex", null, 0, 1, ShippingOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getShippingOption_Vessel(), theFleetPackage.getVessel(), null, "vessel", null, 0, 1, ShippingOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getShippingOption_MaxLadenIdleDays(), ecorePackage.getEInt(), "maxLadenIdleDays", null, 0, 1, ShippingOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(intervalTypeEEnum, IntervalType.class, "IntervalType");
		addEEnumLiteral(intervalTypeEEnum, IntervalType.QUARTERLY);
		addEEnumLiteral(intervalTypeEEnum, IntervalType.MONTHLY);
		addEEnumLiteral(intervalTypeEEnum, IntervalType.WEEKLY);

		// Create resource
		createResource(eNS_URI);
	}

} //ADPPackageImpl
