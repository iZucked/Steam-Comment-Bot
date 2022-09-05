/**
 */
package com.mmxlabs.models.lng.transfers;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.transfers.TransfersPackage
 * @generated
 */
public interface TransfersFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	TransfersFactory eINSTANCE = com.mmxlabs.models.lng.transfers.impl.TransfersFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Transfer Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Transfer Model</em>'.
	 * @generated
	 */
	TransferModel createTransferModel();

	/**
	 * Returns a new object of class '<em>Transfer Agreement</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Transfer Agreement</em>'.
	 * @generated
	 */
	TransferAgreement createTransferAgreement();

	/**
	 * Returns a new object of class '<em>Transfer Record</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Transfer Record</em>'.
	 * @generated
	 */
	TransferRecord createTransferRecord();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	TransfersPackage getTransfersPackage();

} //TransfersFactory
