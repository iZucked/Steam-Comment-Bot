/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.*;

import com.mmxlabs.models.lng.cargo.Slot;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
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
public class ADPFactoryImpl extends EFactoryImpl implements ADPFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ADPFactory init() {
		try {
			ADPFactory theADPFactory = (ADPFactory)EPackage.Registry.INSTANCE.getEFactory(ADPPackage.eNS_URI);
			if (theADPFactory != null) {
				return theADPFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ADPFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ADPFactoryImpl() {
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
			case ADPPackage.ADP_MODEL: return createADPModel();
			case ADPPackage.CONTRACT_PROFILE: return createContractProfile();
			case ADPPackage.PURCHASE_CONTRACT_PROFILE: return createPurchaseContractProfile();
			case ADPPackage.SALES_CONTRACT_PROFILE: return createSalesContractProfile();
			case ADPPackage.SUB_CONTRACT_PROFILE: return createSubContractProfile();
			case ADPPackage.CARGO_SIZE_DISTRIBUTION_MODEL: return createCargoSizeDistributionModel();
			case ADPPackage.CARGO_NUMBER_DISTRIBUTION_MODEL: return createCargoNumberDistributionModel();
			case ADPPackage.CARGO_BY_QUARTER_DISTRIBUTION_MODEL: return createCargoByQuarterDistributionModel();
			case ADPPackage.CARGO_INTERVAL_DISTRIBUTION_MODEL: return createCargoIntervalDistributionModel();
			case ADPPackage.BINDING_RULE: return createBindingRule();
			case ADPPackage.FLOW_TYPE: return createFlowType();
			case ADPPackage.SUPPLY_FROM_FLOW: return createSupplyFromFlow();
			case ADPPackage.DELIVER_TO_FLOW: return createDeliverToFlow();
			case ADPPackage.SUPPLY_FROM_PROFILE_FLOW: return createSupplyFromProfileFlow();
			case ADPPackage.DELIVER_TO_PROFILE_FLOW: return createDeliverToProfileFlow();
			case ADPPackage.SUPPLY_FROM_SPOT_FLOW: return createSupplyFromSpotFlow();
			case ADPPackage.DELIVER_TO_SPOT_FLOW: return createDeliverToSpotFlow();
			case ADPPackage.SHIPPING_OPTION: return createShippingOption();
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
			case ADPPackage.INTERVAL_TYPE:
				return createIntervalTypeFromString(eDataType, initialValue);
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
			case ADPPackage.INTERVAL_TYPE:
				return convertIntervalTypeToString(eDataType, instanceValue);
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
	public ADPModel createADPModel() {
		ADPModelImpl adpModel = new ADPModelImpl();
		return adpModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public <T extends Slot> ContractProfile<T> createContractProfile() {
		ContractProfileImpl<T> contractProfile = new ContractProfileImpl<T>();
		return contractProfile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CargoSizeDistributionModel createCargoSizeDistributionModel() {
		CargoSizeDistributionModelImpl cargoSizeDistributionModel = new CargoSizeDistributionModelImpl();
		return cargoSizeDistributionModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CargoNumberDistributionModel createCargoNumberDistributionModel() {
		CargoNumberDistributionModelImpl cargoNumberDistributionModel = new CargoNumberDistributionModelImpl();
		return cargoNumberDistributionModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PurchaseContractProfile createPurchaseContractProfile() {
		PurchaseContractProfileImpl purchaseContractProfile = new PurchaseContractProfileImpl();
		return purchaseContractProfile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SalesContractProfile createSalesContractProfile() {
		SalesContractProfileImpl salesContractProfile = new SalesContractProfileImpl();
		return salesContractProfile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public <T extends Slot> SubContractProfile<T> createSubContractProfile() {
		SubContractProfileImpl<T> subContractProfile = new SubContractProfileImpl<T>();
		return subContractProfile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CargoByQuarterDistributionModel createCargoByQuarterDistributionModel() {
		CargoByQuarterDistributionModelImpl cargoByQuarterDistributionModel = new CargoByQuarterDistributionModelImpl();
		return cargoByQuarterDistributionModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CargoIntervalDistributionModel createCargoIntervalDistributionModel() {
		CargoIntervalDistributionModelImpl cargoIntervalDistributionModel = new CargoIntervalDistributionModelImpl();
		return cargoIntervalDistributionModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BindingRule createBindingRule() {
		BindingRuleImpl bindingRule = new BindingRuleImpl();
		return bindingRule;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FlowType createFlowType() {
		FlowTypeImpl flowType = new FlowTypeImpl();
		return flowType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SupplyFromFlow createSupplyFromFlow() {
		SupplyFromFlowImpl supplyFromFlow = new SupplyFromFlowImpl();
		return supplyFromFlow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeliverToFlow createDeliverToFlow() {
		DeliverToFlowImpl deliverToFlow = new DeliverToFlowImpl();
		return deliverToFlow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SupplyFromProfileFlow createSupplyFromProfileFlow() {
		SupplyFromProfileFlowImpl supplyFromProfileFlow = new SupplyFromProfileFlowImpl();
		return supplyFromProfileFlow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeliverToProfileFlow createDeliverToProfileFlow() {
		DeliverToProfileFlowImpl deliverToProfileFlow = new DeliverToProfileFlowImpl();
		return deliverToProfileFlow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SupplyFromSpotFlow createSupplyFromSpotFlow() {
		SupplyFromSpotFlowImpl supplyFromSpotFlow = new SupplyFromSpotFlowImpl();
		return supplyFromSpotFlow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeliverToSpotFlow createDeliverToSpotFlow() {
		DeliverToSpotFlowImpl deliverToSpotFlow = new DeliverToSpotFlowImpl();
		return deliverToSpotFlow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ShippingOption createShippingOption() {
		ShippingOptionImpl shippingOption = new ShippingOptionImpl();
		return shippingOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IntervalType createIntervalTypeFromString(EDataType eDataType, String initialValue) {
		IntervalType result = IntervalType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertIntervalTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ADPPackage getADPPackage() {
		return (ADPPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ADPPackage getPackage() {
		return ADPPackage.eINSTANCE;
	}

} //ADPFactoryImpl
