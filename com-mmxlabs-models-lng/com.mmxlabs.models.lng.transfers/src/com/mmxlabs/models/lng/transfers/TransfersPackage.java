/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.transfers;

import com.mmxlabs.models.mmxcore.MMXCorePackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.transfers.TransfersFactory
 * @model kind="package"
 * @generated
 */
public interface TransfersPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "transfers";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.mmxlabs.com/models/lng/transfers/1/";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "lng.transfers";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	TransfersPackage eINSTANCE = com.mmxlabs.models.lng.transfers.impl.TransfersPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.transfers.impl.TransferModelImpl <em>Transfer Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.transfers.impl.TransferModelImpl
	 * @see com.mmxlabs.models.lng.transfers.impl.TransfersPackageImpl#getTransferModel()
	 * @generated
	 */
	int TRANSFER_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_MODEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_MODEL__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Transfer Agreements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_MODEL__TRANSFER_AGREEMENTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Transfer Records</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_MODEL__TRANSFER_RECORDS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Transfer Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_MODEL___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_MODEL___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_MODEL___ECONTAINER_OP = MMXCorePackage.UUID_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>Transfer Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_MODEL_OPERATION_COUNT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.transfers.impl.TransferAgreementImpl <em>Transfer Agreement</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.transfers.impl.TransferAgreementImpl
	 * @see com.mmxlabs.models.lng.transfers.impl.TransfersPackageImpl#getTransferAgreement()
	 * @generated
	 */
	int TRANSFER_AGREEMENT = 1;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_AGREEMENT__EXTENSIONS = MMXCorePackage.NAMED_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_AGREEMENT__NAME = MMXCorePackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>From Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_AGREEMENT__FROM_ENTITY = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>To Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_AGREEMENT__TO_ENTITY = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_AGREEMENT__PRICE_EXPRESSION = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Incoterm</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_AGREEMENT__INCOTERM = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Company Status</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_AGREEMENT__COMPANY_STATUS = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Pricing Basis</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_AGREEMENT__PRICING_BASIS = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Buffer Days</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_AGREEMENT__BUFFER_DAYS = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>From BU</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_AGREEMENT__FROM_BU = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>To BU</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_AGREEMENT__TO_BU = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Preferred PBs</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_AGREEMENT__PREFERRED_PBS = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_AGREEMENT__CODE = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 10;

	/**
	 * The number of structural features of the '<em>Transfer Agreement</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_AGREEMENT_FEATURE_COUNT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 11;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_AGREEMENT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.NAMED_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_AGREEMENT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.NAMED_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_AGREEMENT___ECONTAINER_OP = MMXCorePackage.NAMED_OBJECT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Agreement Or Delegate From BU</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_AGREEMENT___GET_AGREEMENT_OR_DELEGATE_FROM_BU = MMXCorePackage.NAMED_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Agreement Or Delegate To BU</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_AGREEMENT___GET_AGREEMENT_OR_DELEGATE_TO_BU = MMXCorePackage.NAMED_OBJECT_OPERATION_COUNT + 1;

	/**
	 * The number of operations of the '<em>Transfer Agreement</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_AGREEMENT_OPERATION_COUNT = MMXCorePackage.NAMED_OBJECT_OPERATION_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.transfers.impl.TransferRecordImpl <em>Transfer Record</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.transfers.impl.TransferRecordImpl
	 * @see com.mmxlabs.models.lng.transfers.impl.TransfersPackageImpl#getTransferRecord()
	 * @generated
	 */
	int TRANSFER_RECORD = 2;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_RECORD__EXTENSIONS = MMXCorePackage.NAMED_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_RECORD__NAME = MMXCorePackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_RECORD__UUID = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Transfer Agreement</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_RECORD__TRANSFER_AGREEMENT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Lhs</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_RECORD__LHS = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Rhs</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_RECORD__RHS = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Cargo Release Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_RECORD__CARGO_RELEASE_DATE = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_RECORD__PRICE_EXPRESSION = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Pricing Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_RECORD__PRICING_DATE = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Incoterm</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_RECORD__INCOTERM = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Status</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_RECORD__STATUS = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_RECORD__NOTES = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Stale</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_RECORD__STALE = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Pricing Basis</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_RECORD__PRICING_BASIS = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>From BU</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_RECORD__FROM_BU = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 12;

	/**
	 * The feature id for the '<em><b>To BU</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_RECORD__TO_BU = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 13;

	/**
	 * The number of structural features of the '<em>Transfer Record</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_RECORD_FEATURE_COUNT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 14;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_RECORD___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.NAMED_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_RECORD___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.NAMED_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_RECORD___ECONTAINER_OP = MMXCorePackage.NAMED_OBJECT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get From Entity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_RECORD___GET_FROM_ENTITY = MMXCorePackage.NAMED_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get To Entity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_RECORD___GET_TO_ENTITY = MMXCorePackage.NAMED_OBJECT_OPERATION_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Company Status</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_RECORD___GET_COMPANY_STATUS = MMXCorePackage.NAMED_OBJECT_OPERATION_COUNT + 2;

	/**
	 * The operation id for the '<em>Get Record Or Delegate Price Expression</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_RECORD___GET_RECORD_OR_DELEGATE_PRICE_EXPRESSION = MMXCorePackage.NAMED_OBJECT_OPERATION_COUNT + 3;

	/**
	 * The operation id for the '<em>Get Record Or Delegate Incoterm</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_RECORD___GET_RECORD_OR_DELEGATE_INCOTERM = MMXCorePackage.NAMED_OBJECT_OPERATION_COUNT + 4;

	/**
	 * The operation id for the '<em>Get Record Or Delegate Pricing Basis</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_RECORD___GET_RECORD_OR_DELEGATE_PRICING_BASIS = MMXCorePackage.NAMED_OBJECT_OPERATION_COUNT + 5;

	/**
	 * The operation id for the '<em>Get Record Or Delegate From BU</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_RECORD___GET_RECORD_OR_DELEGATE_FROM_BU = MMXCorePackage.NAMED_OBJECT_OPERATION_COUNT + 6;

	/**
	 * The operation id for the '<em>Get Record Or Delegate To BU</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_RECORD___GET_RECORD_OR_DELEGATE_TO_BU = MMXCorePackage.NAMED_OBJECT_OPERATION_COUNT + 7;

	/**
	 * The number of operations of the '<em>Transfer Record</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_RECORD_OPERATION_COUNT = MMXCorePackage.NAMED_OBJECT_OPERATION_COUNT + 8;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.transfers.TransferIncoterm <em>Transfer Incoterm</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.transfers.TransferIncoterm
	 * @see com.mmxlabs.models.lng.transfers.impl.TransfersPackageImpl#getTransferIncoterm()
	 * @generated
	 */
	int TRANSFER_INCOTERM = 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.transfers.TransferStatus <em>Transfer Status</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.transfers.TransferStatus
	 * @see com.mmxlabs.models.lng.transfers.impl.TransfersPackageImpl#getTransferStatus()
	 * @generated
	 */
	int TRANSFER_STATUS = 4;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.transfers.CompanyStatus <em>Company Status</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.transfers.CompanyStatus
	 * @see com.mmxlabs.models.lng.transfers.impl.TransfersPackageImpl#getCompanyStatus()
	 * @generated
	 */
	int COMPANY_STATUS = 5;


	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.transfers.TransferModel <em>Transfer Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Transfer Model</em>'.
	 * @see com.mmxlabs.models.lng.transfers.TransferModel
	 * @generated
	 */
	EClass getTransferModel();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.transfers.TransferModel#getTransferAgreements <em>Transfer Agreements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Transfer Agreements</em>'.
	 * @see com.mmxlabs.models.lng.transfers.TransferModel#getTransferAgreements()
	 * @see #getTransferModel()
	 * @generated
	 */
	EReference getTransferModel_TransferAgreements();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.transfers.TransferModel#getTransferRecords <em>Transfer Records</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Transfer Records</em>'.
	 * @see com.mmxlabs.models.lng.transfers.TransferModel#getTransferRecords()
	 * @see #getTransferModel()
	 * @generated
	 */
	EReference getTransferModel_TransferRecords();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.transfers.TransferAgreement <em>Transfer Agreement</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Transfer Agreement</em>'.
	 * @see com.mmxlabs.models.lng.transfers.TransferAgreement
	 * @generated
	 */
	EClass getTransferAgreement();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.transfers.TransferAgreement#getFromEntity <em>From Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>From Entity</em>'.
	 * @see com.mmxlabs.models.lng.transfers.TransferAgreement#getFromEntity()
	 * @see #getTransferAgreement()
	 * @generated
	 */
	EReference getTransferAgreement_FromEntity();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.transfers.TransferAgreement#getToEntity <em>To Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>To Entity</em>'.
	 * @see com.mmxlabs.models.lng.transfers.TransferAgreement#getToEntity()
	 * @see #getTransferAgreement()
	 * @generated
	 */
	EReference getTransferAgreement_ToEntity();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.transfers.TransferAgreement#getPriceExpression <em>Price Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price Expression</em>'.
	 * @see com.mmxlabs.models.lng.transfers.TransferAgreement#getPriceExpression()
	 * @see #getTransferAgreement()
	 * @generated
	 */
	EAttribute getTransferAgreement_PriceExpression();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.transfers.TransferAgreement#getIncoterm <em>Incoterm</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Incoterm</em>'.
	 * @see com.mmxlabs.models.lng.transfers.TransferAgreement#getIncoterm()
	 * @see #getTransferAgreement()
	 * @generated
	 */
	EAttribute getTransferAgreement_Incoterm();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.transfers.TransferAgreement#getCompanyStatus <em>Company Status</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Company Status</em>'.
	 * @see com.mmxlabs.models.lng.transfers.TransferAgreement#getCompanyStatus()
	 * @see #getTransferAgreement()
	 * @generated
	 */
	EAttribute getTransferAgreement_CompanyStatus();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.transfers.TransferAgreement#getPricingBasis <em>Pricing Basis</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Pricing Basis</em>'.
	 * @see com.mmxlabs.models.lng.transfers.TransferAgreement#getPricingBasis()
	 * @see #getTransferAgreement()
	 * @generated
	 */
	EAttribute getTransferAgreement_PricingBasis();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.transfers.TransferAgreement#getBufferDays <em>Buffer Days</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Buffer Days</em>'.
	 * @see com.mmxlabs.models.lng.transfers.TransferAgreement#getBufferDays()
	 * @see #getTransferAgreement()
	 * @generated
	 */
	EAttribute getTransferAgreement_BufferDays();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.transfers.TransferAgreement#getFromBU <em>From BU</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>From BU</em>'.
	 * @see com.mmxlabs.models.lng.transfers.TransferAgreement#getFromBU()
	 * @see #getTransferAgreement()
	 * @generated
	 */
	EReference getTransferAgreement_FromBU();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.transfers.TransferAgreement#getToBU <em>To BU</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>To BU</em>'.
	 * @see com.mmxlabs.models.lng.transfers.TransferAgreement#getToBU()
	 * @see #getTransferAgreement()
	 * @generated
	 */
	EReference getTransferAgreement_ToBU();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.transfers.TransferAgreement#getPreferredPBs <em>Preferred PBs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Preferred PBs</em>'.
	 * @see com.mmxlabs.models.lng.transfers.TransferAgreement#getPreferredPBs()
	 * @see #getTransferAgreement()
	 * @generated
	 */
	EReference getTransferAgreement_PreferredPBs();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.transfers.TransferAgreement#getCode <em>Code</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Code</em>'.
	 * @see com.mmxlabs.models.lng.transfers.TransferAgreement#getCode()
	 * @see #getTransferAgreement()
	 * @generated
	 */
	EAttribute getTransferAgreement_Code();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.transfers.TransferAgreement#getAgreementOrDelegateFromBU() <em>Get Agreement Or Delegate From BU</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Agreement Or Delegate From BU</em>' operation.
	 * @see com.mmxlabs.models.lng.transfers.TransferAgreement#getAgreementOrDelegateFromBU()
	 * @generated
	 */
	EOperation getTransferAgreement__GetAgreementOrDelegateFromBU();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.transfers.TransferAgreement#getAgreementOrDelegateToBU() <em>Get Agreement Or Delegate To BU</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Agreement Or Delegate To BU</em>' operation.
	 * @see com.mmxlabs.models.lng.transfers.TransferAgreement#getAgreementOrDelegateToBU()
	 * @generated
	 */
	EOperation getTransferAgreement__GetAgreementOrDelegateToBU();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.transfers.TransferRecord <em>Transfer Record</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Transfer Record</em>'.
	 * @see com.mmxlabs.models.lng.transfers.TransferRecord
	 * @generated
	 */
	EClass getTransferRecord();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getTransferAgreement <em>Transfer Agreement</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Transfer Agreement</em>'.
	 * @see com.mmxlabs.models.lng.transfers.TransferRecord#getTransferAgreement()
	 * @see #getTransferRecord()
	 * @generated
	 */
	EReference getTransferRecord_TransferAgreement();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getLhs <em>Lhs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Lhs</em>'.
	 * @see com.mmxlabs.models.lng.transfers.TransferRecord#getLhs()
	 * @see #getTransferRecord()
	 * @generated
	 */
	EReference getTransferRecord_Lhs();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getRhs <em>Rhs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Rhs</em>'.
	 * @see com.mmxlabs.models.lng.transfers.TransferRecord#getRhs()
	 * @see #getTransferRecord()
	 * @generated
	 */
	EReference getTransferRecord_Rhs();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getCargoReleaseDate <em>Cargo Release Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cargo Release Date</em>'.
	 * @see com.mmxlabs.models.lng.transfers.TransferRecord#getCargoReleaseDate()
	 * @see #getTransferRecord()
	 * @generated
	 */
	EAttribute getTransferRecord_CargoReleaseDate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getPriceExpression <em>Price Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price Expression</em>'.
	 * @see com.mmxlabs.models.lng.transfers.TransferRecord#getPriceExpression()
	 * @see #getTransferRecord()
	 * @generated
	 */
	EAttribute getTransferRecord_PriceExpression();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getPricingDate <em>Pricing Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Pricing Date</em>'.
	 * @see com.mmxlabs.models.lng.transfers.TransferRecord#getPricingDate()
	 * @see #getTransferRecord()
	 * @generated
	 */
	EAttribute getTransferRecord_PricingDate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getIncoterm <em>Incoterm</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Incoterm</em>'.
	 * @see com.mmxlabs.models.lng.transfers.TransferRecord#getIncoterm()
	 * @see #getTransferRecord()
	 * @generated
	 */
	EAttribute getTransferRecord_Incoterm();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getStatus <em>Status</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Status</em>'.
	 * @see com.mmxlabs.models.lng.transfers.TransferRecord#getStatus()
	 * @see #getTransferRecord()
	 * @generated
	 */
	EAttribute getTransferRecord_Status();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getNotes <em>Notes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Notes</em>'.
	 * @see com.mmxlabs.models.lng.transfers.TransferRecord#getNotes()
	 * @see #getTransferRecord()
	 * @generated
	 */
	EAttribute getTransferRecord_Notes();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.transfers.TransferRecord#isStale <em>Stale</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Stale</em>'.
	 * @see com.mmxlabs.models.lng.transfers.TransferRecord#isStale()
	 * @see #getTransferRecord()
	 * @generated
	 */
	EAttribute getTransferRecord_Stale();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getPricingBasis <em>Pricing Basis</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Pricing Basis</em>'.
	 * @see com.mmxlabs.models.lng.transfers.TransferRecord#getPricingBasis()
	 * @see #getTransferRecord()
	 * @generated
	 */
	EAttribute getTransferRecord_PricingBasis();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getFromBU <em>From BU</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>From BU</em>'.
	 * @see com.mmxlabs.models.lng.transfers.TransferRecord#getFromBU()
	 * @see #getTransferRecord()
	 * @generated
	 */
	EReference getTransferRecord_FromBU();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getToBU <em>To BU</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>To BU</em>'.
	 * @see com.mmxlabs.models.lng.transfers.TransferRecord#getToBU()
	 * @see #getTransferRecord()
	 * @generated
	 */
	EReference getTransferRecord_ToBU();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getFromEntity() <em>Get From Entity</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get From Entity</em>' operation.
	 * @see com.mmxlabs.models.lng.transfers.TransferRecord#getFromEntity()
	 * @generated
	 */
	EOperation getTransferRecord__GetFromEntity();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getToEntity() <em>Get To Entity</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get To Entity</em>' operation.
	 * @see com.mmxlabs.models.lng.transfers.TransferRecord#getToEntity()
	 * @generated
	 */
	EOperation getTransferRecord__GetToEntity();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getCompanyStatus() <em>Get Company Status</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Company Status</em>' operation.
	 * @see com.mmxlabs.models.lng.transfers.TransferRecord#getCompanyStatus()
	 * @generated
	 */
	EOperation getTransferRecord__GetCompanyStatus();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getRecordOrDelegatePriceExpression() <em>Get Record Or Delegate Price Expression</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Record Or Delegate Price Expression</em>' operation.
	 * @see com.mmxlabs.models.lng.transfers.TransferRecord#getRecordOrDelegatePriceExpression()
	 * @generated
	 */
	EOperation getTransferRecord__GetRecordOrDelegatePriceExpression();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getRecordOrDelegateIncoterm() <em>Get Record Or Delegate Incoterm</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Record Or Delegate Incoterm</em>' operation.
	 * @see com.mmxlabs.models.lng.transfers.TransferRecord#getRecordOrDelegateIncoterm()
	 * @generated
	 */
	EOperation getTransferRecord__GetRecordOrDelegateIncoterm();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getRecordOrDelegatePricingBasis() <em>Get Record Or Delegate Pricing Basis</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Record Or Delegate Pricing Basis</em>' operation.
	 * @see com.mmxlabs.models.lng.transfers.TransferRecord#getRecordOrDelegatePricingBasis()
	 * @generated
	 */
	EOperation getTransferRecord__GetRecordOrDelegatePricingBasis();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getRecordOrDelegateFromBU() <em>Get Record Or Delegate From BU</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Record Or Delegate From BU</em>' operation.
	 * @see com.mmxlabs.models.lng.transfers.TransferRecord#getRecordOrDelegateFromBU()
	 * @generated
	 */
	EOperation getTransferRecord__GetRecordOrDelegateFromBU();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.transfers.TransferRecord#getRecordOrDelegateToBU() <em>Get Record Or Delegate To BU</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Record Or Delegate To BU</em>' operation.
	 * @see com.mmxlabs.models.lng.transfers.TransferRecord#getRecordOrDelegateToBU()
	 * @generated
	 */
	EOperation getTransferRecord__GetRecordOrDelegateToBU();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.transfers.TransferIncoterm <em>Transfer Incoterm</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Transfer Incoterm</em>'.
	 * @see com.mmxlabs.models.lng.transfers.TransferIncoterm
	 * @generated
	 */
	EEnum getTransferIncoterm();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.transfers.TransferStatus <em>Transfer Status</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Transfer Status</em>'.
	 * @see com.mmxlabs.models.lng.transfers.TransferStatus
	 * @generated
	 */
	EEnum getTransferStatus();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.transfers.CompanyStatus <em>Company Status</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Company Status</em>'.
	 * @see com.mmxlabs.models.lng.transfers.CompanyStatus
	 * @generated
	 */
	EEnum getCompanyStatus();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	TransfersFactory getTransfersFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.transfers.impl.TransferModelImpl <em>Transfer Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.transfers.impl.TransferModelImpl
		 * @see com.mmxlabs.models.lng.transfers.impl.TransfersPackageImpl#getTransferModel()
		 * @generated
		 */
		EClass TRANSFER_MODEL = eINSTANCE.getTransferModel();
		/**
		 * The meta object literal for the '<em><b>Transfer Agreements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRANSFER_MODEL__TRANSFER_AGREEMENTS = eINSTANCE.getTransferModel_TransferAgreements();
		/**
		 * The meta object literal for the '<em><b>Transfer Records</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRANSFER_MODEL__TRANSFER_RECORDS = eINSTANCE.getTransferModel_TransferRecords();
		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.transfers.impl.TransferAgreementImpl <em>Transfer Agreement</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.transfers.impl.TransferAgreementImpl
		 * @see com.mmxlabs.models.lng.transfers.impl.TransfersPackageImpl#getTransferAgreement()
		 * @generated
		 */
		EClass TRANSFER_AGREEMENT = eINSTANCE.getTransferAgreement();
		/**
		 * The meta object literal for the '<em><b>From Entity</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRANSFER_AGREEMENT__FROM_ENTITY = eINSTANCE.getTransferAgreement_FromEntity();
		/**
		 * The meta object literal for the '<em><b>To Entity</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRANSFER_AGREEMENT__TO_ENTITY = eINSTANCE.getTransferAgreement_ToEntity();
		/**
		 * The meta object literal for the '<em><b>Price Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRANSFER_AGREEMENT__PRICE_EXPRESSION = eINSTANCE.getTransferAgreement_PriceExpression();
		/**
		 * The meta object literal for the '<em><b>Incoterm</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRANSFER_AGREEMENT__INCOTERM = eINSTANCE.getTransferAgreement_Incoterm();
		/**
		 * The meta object literal for the '<em><b>Company Status</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRANSFER_AGREEMENT__COMPANY_STATUS = eINSTANCE.getTransferAgreement_CompanyStatus();
		/**
		 * The meta object literal for the '<em><b>Pricing Basis</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRANSFER_AGREEMENT__PRICING_BASIS = eINSTANCE.getTransferAgreement_PricingBasis();
		/**
		 * The meta object literal for the '<em><b>Buffer Days</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRANSFER_AGREEMENT__BUFFER_DAYS = eINSTANCE.getTransferAgreement_BufferDays();
		/**
		 * The meta object literal for the '<em><b>From BU</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRANSFER_AGREEMENT__FROM_BU = eINSTANCE.getTransferAgreement_FromBU();
		/**
		 * The meta object literal for the '<em><b>To BU</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRANSFER_AGREEMENT__TO_BU = eINSTANCE.getTransferAgreement_ToBU();
		/**
		 * The meta object literal for the '<em><b>Preferred PBs</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRANSFER_AGREEMENT__PREFERRED_PBS = eINSTANCE.getTransferAgreement_PreferredPBs();
		/**
		 * The meta object literal for the '<em><b>Code</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRANSFER_AGREEMENT__CODE = eINSTANCE.getTransferAgreement_Code();
		/**
		 * The meta object literal for the '<em><b>Get Agreement Or Delegate From BU</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation TRANSFER_AGREEMENT___GET_AGREEMENT_OR_DELEGATE_FROM_BU = eINSTANCE.getTransferAgreement__GetAgreementOrDelegateFromBU();
		/**
		 * The meta object literal for the '<em><b>Get Agreement Or Delegate To BU</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation TRANSFER_AGREEMENT___GET_AGREEMENT_OR_DELEGATE_TO_BU = eINSTANCE.getTransferAgreement__GetAgreementOrDelegateToBU();
		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.transfers.impl.TransferRecordImpl <em>Transfer Record</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.transfers.impl.TransferRecordImpl
		 * @see com.mmxlabs.models.lng.transfers.impl.TransfersPackageImpl#getTransferRecord()
		 * @generated
		 */
		EClass TRANSFER_RECORD = eINSTANCE.getTransferRecord();
		/**
		 * The meta object literal for the '<em><b>Transfer Agreement</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRANSFER_RECORD__TRANSFER_AGREEMENT = eINSTANCE.getTransferRecord_TransferAgreement();
		/**
		 * The meta object literal for the '<em><b>Lhs</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRANSFER_RECORD__LHS = eINSTANCE.getTransferRecord_Lhs();
		/**
		 * The meta object literal for the '<em><b>Rhs</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRANSFER_RECORD__RHS = eINSTANCE.getTransferRecord_Rhs();
		/**
		 * The meta object literal for the '<em><b>Cargo Release Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRANSFER_RECORD__CARGO_RELEASE_DATE = eINSTANCE.getTransferRecord_CargoReleaseDate();
		/**
		 * The meta object literal for the '<em><b>Price Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRANSFER_RECORD__PRICE_EXPRESSION = eINSTANCE.getTransferRecord_PriceExpression();
		/**
		 * The meta object literal for the '<em><b>Pricing Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRANSFER_RECORD__PRICING_DATE = eINSTANCE.getTransferRecord_PricingDate();
		/**
		 * The meta object literal for the '<em><b>Incoterm</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRANSFER_RECORD__INCOTERM = eINSTANCE.getTransferRecord_Incoterm();
		/**
		 * The meta object literal for the '<em><b>Status</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRANSFER_RECORD__STATUS = eINSTANCE.getTransferRecord_Status();
		/**
		 * The meta object literal for the '<em><b>Notes</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRANSFER_RECORD__NOTES = eINSTANCE.getTransferRecord_Notes();
		/**
		 * The meta object literal for the '<em><b>Stale</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRANSFER_RECORD__STALE = eINSTANCE.getTransferRecord_Stale();
		/**
		 * The meta object literal for the '<em><b>Pricing Basis</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRANSFER_RECORD__PRICING_BASIS = eINSTANCE.getTransferRecord_PricingBasis();
		/**
		 * The meta object literal for the '<em><b>From BU</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRANSFER_RECORD__FROM_BU = eINSTANCE.getTransferRecord_FromBU();
		/**
		 * The meta object literal for the '<em><b>To BU</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRANSFER_RECORD__TO_BU = eINSTANCE.getTransferRecord_ToBU();
		/**
		 * The meta object literal for the '<em><b>Get From Entity</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation TRANSFER_RECORD___GET_FROM_ENTITY = eINSTANCE.getTransferRecord__GetFromEntity();
		/**
		 * The meta object literal for the '<em><b>Get To Entity</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation TRANSFER_RECORD___GET_TO_ENTITY = eINSTANCE.getTransferRecord__GetToEntity();
		/**
		 * The meta object literal for the '<em><b>Get Company Status</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation TRANSFER_RECORD___GET_COMPANY_STATUS = eINSTANCE.getTransferRecord__GetCompanyStatus();
		/**
		 * The meta object literal for the '<em><b>Get Record Or Delegate Price Expression</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation TRANSFER_RECORD___GET_RECORD_OR_DELEGATE_PRICE_EXPRESSION = eINSTANCE.getTransferRecord__GetRecordOrDelegatePriceExpression();
		/**
		 * The meta object literal for the '<em><b>Get Record Or Delegate Incoterm</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation TRANSFER_RECORD___GET_RECORD_OR_DELEGATE_INCOTERM = eINSTANCE.getTransferRecord__GetRecordOrDelegateIncoterm();
		/**
		 * The meta object literal for the '<em><b>Get Record Or Delegate Pricing Basis</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation TRANSFER_RECORD___GET_RECORD_OR_DELEGATE_PRICING_BASIS = eINSTANCE.getTransferRecord__GetRecordOrDelegatePricingBasis();
		/**
		 * The meta object literal for the '<em><b>Get Record Or Delegate From BU</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation TRANSFER_RECORD___GET_RECORD_OR_DELEGATE_FROM_BU = eINSTANCE.getTransferRecord__GetRecordOrDelegateFromBU();
		/**
		 * The meta object literal for the '<em><b>Get Record Or Delegate To BU</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation TRANSFER_RECORD___GET_RECORD_OR_DELEGATE_TO_BU = eINSTANCE.getTransferRecord__GetRecordOrDelegateToBU();
		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.transfers.TransferIncoterm <em>Transfer Incoterm</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.transfers.TransferIncoterm
		 * @see com.mmxlabs.models.lng.transfers.impl.TransfersPackageImpl#getTransferIncoterm()
		 * @generated
		 */
		EEnum TRANSFER_INCOTERM = eINSTANCE.getTransferIncoterm();
		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.transfers.TransferStatus <em>Transfer Status</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.transfers.TransferStatus
		 * @see com.mmxlabs.models.lng.transfers.impl.TransfersPackageImpl#getTransferStatus()
		 * @generated
		 */
		EEnum TRANSFER_STATUS = eINSTANCE.getTransferStatus();
		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.transfers.CompanyStatus <em>Company Status</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.transfers.CompanyStatus
		 * @see com.mmxlabs.models.lng.transfers.impl.TransfersPackageImpl#getCompanyStatus()
		 * @generated
		 */
		EEnum COMPANY_STATUS = eINSTANCE.getCompanyStatus();

	}

} //TransfersPackage
