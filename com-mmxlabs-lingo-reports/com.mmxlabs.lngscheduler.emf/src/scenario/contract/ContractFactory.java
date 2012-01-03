/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.contract;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see scenario.contract.ContractPackage
 * @generated
 */
public interface ContractFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ContractFactory eINSTANCE = scenario.contract.impl.ContractFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	ContractModel createContractModel();

	/**
	 * Returns a new object of class '<em>Sales Contract</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Sales Contract</em>'.
	 * @generated
	 */
	SalesContract createSalesContract();

	/**
	 * Returns a new object of class '<em>Total Volume Limit</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Total Volume Limit</em>'.
	 * @generated
	 */
	TotalVolumeLimit createTotalVolumeLimit();

	/**
	 * Returns a new object of class '<em>Entity</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Entity</em>'.
	 * @generated
	 */
	Entity createEntity();

	/**
	 * Returns a new object of class '<em>Fixed Price Purchase Contract</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Fixed Price Purchase Contract</em>'.
	 * @generated
	 */
	FixedPricePurchaseContract createFixedPricePurchaseContract();

	/**
	 * Returns a new object of class '<em>Index Price Purchase Contract</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Index Price Purchase Contract</em>'.
	 * @generated
	 */
	IndexPricePurchaseContract createIndexPricePurchaseContract();

	/**
	 * Returns a new object of class '<em>Netback Purchase Contract</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Netback Purchase Contract</em>'.
	 * @generated
	 */
	NetbackPurchaseContract createNetbackPurchaseContract();

	/**
	 * Returns a new object of class '<em>Profit Sharing Purchase Contract</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Profit Sharing Purchase Contract</em>'.
	 * @generated
	 */
	ProfitSharingPurchaseContract createProfitSharingPurchaseContract();

	/**
	 * Returns a new object of class '<em>Group Entity</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Group Entity</em>'.
	 * @generated
	 */
	GroupEntity createGroupEntity();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	ContractPackage getContractPackage();

} //ContractFactory
