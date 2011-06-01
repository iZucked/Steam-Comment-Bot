/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.contract.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import scenario.contract.Contract;
import scenario.contract.ContractFactory;
import scenario.contract.ContractModel;
import scenario.contract.ContractPackage;
import scenario.contract.Entity;
import scenario.contract.FixedPricePurchaseContract;
import scenario.contract.IndexPricePurchaseContract;
import scenario.contract.NetbackPurchaseContract;
import scenario.contract.ProfitSharingPurchaseContract;
import scenario.contract.SalesContract;
import scenario.contract.TotalVolumeLimit;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ContractFactoryImpl extends EFactoryImpl implements ContractFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ContractFactory init() {
		try {
			ContractFactory theContractFactory = (ContractFactory)EPackage.Registry.INSTANCE.getEFactory("http://com.mmxlabs.lng.emf/contract"); 
			if (theContractFactory != null) {
				return theContractFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ContractFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ContractFactoryImpl() {
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
			case ContractPackage.CONTRACT_MODEL: return createContractModel();
			case ContractPackage.SALES_CONTRACT: return createSalesContract();
			case ContractPackage.TOTAL_VOLUME_LIMIT: return createTotalVolumeLimit();
			case ContractPackage.ENTITY: return createEntity();
			case ContractPackage.FIXED_PRICE_PURCHASE_CONTRACT: return createFixedPricePurchaseContract();
			case ContractPackage.INDEX_PRICE_PURCHASE_CONTRACT: return createIndexPricePurchaseContract();
			case ContractPackage.NETBACK_PURCHASE_CONTRACT: return createNetbackPurchaseContract();
			case ContractPackage.PROFIT_SHARING_PURCHASE_CONTRACT: return createProfitSharingPurchaseContract();
			case ContractPackage.CONTRACT: return createContract();
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
	public ContractModel createContractModel() {
		ContractModelImpl contractModel = new ContractModelImpl();
		return contractModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SalesContract createSalesContract() {
		SalesContractImpl salesContract = new SalesContractImpl();
		return salesContract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TotalVolumeLimit createTotalVolumeLimit() {
		TotalVolumeLimitImpl totalVolumeLimit = new TotalVolumeLimitImpl();
		return totalVolumeLimit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Entity createEntity() {
		EntityImpl entity = new EntityImpl();
		return entity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public FixedPricePurchaseContract createFixedPricePurchaseContract() {
		FixedPricePurchaseContractImpl fixedPricePurchaseContract = new FixedPricePurchaseContractImpl();
		return fixedPricePurchaseContract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IndexPricePurchaseContract createIndexPricePurchaseContract() {
		IndexPricePurchaseContractImpl indexPricePurchaseContract = new IndexPricePurchaseContractImpl();
		return indexPricePurchaseContract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NetbackPurchaseContract createNetbackPurchaseContract() {
		NetbackPurchaseContractImpl netbackPurchaseContract = new NetbackPurchaseContractImpl();
		return netbackPurchaseContract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ProfitSharingPurchaseContract createProfitSharingPurchaseContract() {
		ProfitSharingPurchaseContractImpl profitSharingPurchaseContract = new ProfitSharingPurchaseContractImpl();
		return profitSharingPurchaseContract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Contract createContract() {
		ContractImpl contract = new ContractImpl();
		return contract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ContractPackage getContractPackage() {
		return (ContractPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ContractPackage getPackage() {
		return ContractPackage.eINSTANCE;
	}

} //ContractFactoryImpl
