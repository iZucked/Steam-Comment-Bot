/**
 */
package com.mmxlabs.models.lng.transfers;

import com.mmxlabs.models.mmxcore.MMXCorePackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
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
	 * The feature id for the '<em><b>Transfer Agreements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_MODEL__TRANSFER_AGREEMENTS = 0;

	/**
	 * The feature id for the '<em><b>Transfer Records</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_MODEL__TRANSFER_RECORDS = 1;

	/**
	 * The number of structural features of the '<em>Transfer Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_MODEL_FEATURE_COUNT = 2;

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
	 * The number of structural features of the '<em>Transfer Agreement</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_AGREEMENT_FEATURE_COUNT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 5;

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
	 * The number of structural features of the '<em>Transfer Record</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFER_RECORD_FEATURE_COUNT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 11;

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
