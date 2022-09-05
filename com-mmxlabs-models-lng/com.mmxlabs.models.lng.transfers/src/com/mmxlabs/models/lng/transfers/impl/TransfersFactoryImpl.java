/**
 */
package com.mmxlabs.models.lng.transfers.impl;

import com.mmxlabs.models.lng.transfers.*;

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
public class TransfersFactoryImpl extends EFactoryImpl implements TransfersFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static TransfersFactory init() {
		try {
			TransfersFactory theTransfersFactory = (TransfersFactory)EPackage.Registry.INSTANCE.getEFactory(TransfersPackage.eNS_URI);
			if (theTransfersFactory != null) {
				return theTransfersFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new TransfersFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TransfersFactoryImpl() {
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
			case TransfersPackage.TRANSFER_MODEL: return createTransferModel();
			case TransfersPackage.TRANSFER_AGREEMENT: return createTransferAgreement();
			case TransfersPackage.TRANSFER_RECORD: return createTransferRecord();
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
			case TransfersPackage.TRANSFER_INCOTERM:
				return createTransferIncotermFromString(eDataType, initialValue);
			case TransfersPackage.TRANSFER_STATUS:
				return createTransferStatusFromString(eDataType, initialValue);
			case TransfersPackage.COMPANY_STATUS:
				return createCompanyStatusFromString(eDataType, initialValue);
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
			case TransfersPackage.TRANSFER_INCOTERM:
				return convertTransferIncotermToString(eDataType, instanceValue);
			case TransfersPackage.TRANSFER_STATUS:
				return convertTransferStatusToString(eDataType, instanceValue);
			case TransfersPackage.COMPANY_STATUS:
				return convertCompanyStatusToString(eDataType, instanceValue);
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
	public TransferModel createTransferModel() {
		TransferModelImpl transferModel = new TransferModelImpl();
		return transferModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TransferAgreement createTransferAgreement() {
		TransferAgreementImpl transferAgreement = new TransferAgreementImpl();
		return transferAgreement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TransferRecord createTransferRecord() {
		TransferRecordImpl transferRecord = new TransferRecordImpl();
		return transferRecord;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TransferIncoterm createTransferIncotermFromString(EDataType eDataType, String initialValue) {
		TransferIncoterm result = TransferIncoterm.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertTransferIncotermToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TransferStatus createTransferStatusFromString(EDataType eDataType, String initialValue) {
		TransferStatus result = TransferStatus.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertTransferStatusToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CompanyStatus createCompanyStatusFromString(EDataType eDataType, String initialValue) {
		CompanyStatus result = CompanyStatus.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertCompanyStatusToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TransfersPackage getTransfersPackage() {
		return (TransfersPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static TransfersPackage getPackage() {
		return TransfersPackage.eINSTANCE;
	}

} //TransfersFactoryImpl
