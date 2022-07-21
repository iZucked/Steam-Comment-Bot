/**
 */
package com.mmxlabs.models.lng.transfers.impl;

import com.mmxlabs.models.datetime.DateTimePackage;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.commercial.CommercialPackage;

import com.mmxlabs.models.lng.fleet.FleetPackage;

import com.mmxlabs.models.lng.port.PortPackage;

import com.mmxlabs.models.lng.pricing.PricingPackage;

import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.transfers.CompanyStatus;
import com.mmxlabs.models.lng.transfers.TransferAgreement;
import com.mmxlabs.models.lng.transfers.TransferIncoterm;
import com.mmxlabs.models.lng.transfers.TransferModel;
import com.mmxlabs.models.lng.transfers.TransferRecord;
import com.mmxlabs.models.lng.transfers.TransferStatus;
import com.mmxlabs.models.lng.transfers.TransfersFactory;
import com.mmxlabs.models.lng.transfers.TransfersPackage;

import com.mmxlabs.models.lng.types.TypesPackage;

import com.mmxlabs.models.mmxcore.MMXCorePackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class TransfersPackageImpl extends EPackageImpl implements TransfersPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass transferModelEClass = null;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass transferAgreementEClass = null;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass transferRecordEClass = null;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum transferIncotermEEnum = null;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum transferStatusEEnum = null;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum companyStatusEEnum = null;
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
	 * @see com.mmxlabs.models.lng.transfers.TransfersPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private TransfersPackageImpl() {
		super(eNS_URI, TransfersFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link TransfersPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static TransfersPackage init() {
		if (isInited) return (TransfersPackage)EPackage.Registry.INSTANCE.getEPackage(TransfersPackage.eNS_URI);

		// Obtain or create and register package
		Object registeredTransfersPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		TransfersPackageImpl theTransfersPackage = registeredTransfersPackage instanceof TransfersPackageImpl ? (TransfersPackageImpl)registeredTransfersPackage : new TransfersPackageImpl();

		isInited = true;

		// Initialize simple dependencies
		CargoPackage.eINSTANCE.eClass();
		CommercialPackage.eINSTANCE.eClass();
		DateTimePackage.eINSTANCE.eClass();
		FleetPackage.eINSTANCE.eClass();
		TypesPackage.eINSTANCE.eClass();
		MMXCorePackage.eINSTANCE.eClass();
		PortPackage.eINSTANCE.eClass();
		PricingPackage.eINSTANCE.eClass();
		SpotMarketsPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theTransfersPackage.createPackageContents();

		// Initialize created meta-data
		theTransfersPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theTransfersPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(TransfersPackage.eNS_URI, theTransfersPackage);
		return theTransfersPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getTransferModel() {
		return transferModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTransferModel_TransferAgreements() {
		return (EReference)transferModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTransferModel_TransferRecords() {
		return (EReference)transferModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getTransferAgreement() {
		return transferAgreementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTransferAgreement_FromEntity() {
		return (EReference)transferAgreementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTransferAgreement_ToEntity() {
		return (EReference)transferAgreementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTransferAgreement_PriceExpression() {
		return (EAttribute)transferAgreementEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTransferAgreement_Incoterm() {
		return (EAttribute)transferAgreementEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTransferAgreement_CompanyStatus() {
		return (EAttribute)transferAgreementEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getTransferRecord() {
		return transferRecordEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTransferRecord_TransferAgreement() {
		return (EReference)transferRecordEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTransferRecord_Lhs() {
		return (EReference)transferRecordEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTransferRecord_Rhs() {
		return (EReference)transferRecordEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTransferRecord_CargoReleaseDate() {
		return (EAttribute)transferRecordEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTransferRecord_PriceExpression() {
		return (EAttribute)transferRecordEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTransferRecord_PricingDate() {
		return (EAttribute)transferRecordEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTransferRecord_Incoterm() {
		return (EAttribute)transferRecordEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTransferRecord_Status() {
		return (EAttribute)transferRecordEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTransferRecord_Notes() {
		return (EAttribute)transferRecordEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTransferRecord_Stale() {
		return (EAttribute)transferRecordEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getTransferRecord__GetFromEntity() {
		return transferRecordEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getTransferRecord__GetToEntity() {
		return transferRecordEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getTransferRecord__GetCompanyStatus() {
		return transferRecordEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getTransferRecord__GetRecordOrDelegatePriceExpression() {
		return transferRecordEClass.getEOperations().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getTransferRecord__GetRecordOrDelegateIncoterm() {
		return transferRecordEClass.getEOperations().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getTransferIncoterm() {
		return transferIncotermEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getTransferStatus() {
		return transferStatusEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getCompanyStatus() {
		return companyStatusEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TransfersFactory getTransfersFactory() {
		return (TransfersFactory)getEFactoryInstance();
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
		transferModelEClass = createEClass(TRANSFER_MODEL);
		createEReference(transferModelEClass, TRANSFER_MODEL__TRANSFER_AGREEMENTS);
		createEReference(transferModelEClass, TRANSFER_MODEL__TRANSFER_RECORDS);

		transferAgreementEClass = createEClass(TRANSFER_AGREEMENT);
		createEReference(transferAgreementEClass, TRANSFER_AGREEMENT__FROM_ENTITY);
		createEReference(transferAgreementEClass, TRANSFER_AGREEMENT__TO_ENTITY);
		createEAttribute(transferAgreementEClass, TRANSFER_AGREEMENT__PRICE_EXPRESSION);
		createEAttribute(transferAgreementEClass, TRANSFER_AGREEMENT__INCOTERM);
		createEAttribute(transferAgreementEClass, TRANSFER_AGREEMENT__COMPANY_STATUS);

		transferRecordEClass = createEClass(TRANSFER_RECORD);
		createEReference(transferRecordEClass, TRANSFER_RECORD__TRANSFER_AGREEMENT);
		createEReference(transferRecordEClass, TRANSFER_RECORD__LHS);
		createEReference(transferRecordEClass, TRANSFER_RECORD__RHS);
		createEAttribute(transferRecordEClass, TRANSFER_RECORD__CARGO_RELEASE_DATE);
		createEAttribute(transferRecordEClass, TRANSFER_RECORD__PRICE_EXPRESSION);
		createEAttribute(transferRecordEClass, TRANSFER_RECORD__PRICING_DATE);
		createEAttribute(transferRecordEClass, TRANSFER_RECORD__INCOTERM);
		createEAttribute(transferRecordEClass, TRANSFER_RECORD__STATUS);
		createEAttribute(transferRecordEClass, TRANSFER_RECORD__NOTES);
		createEAttribute(transferRecordEClass, TRANSFER_RECORD__STALE);
		createEOperation(transferRecordEClass, TRANSFER_RECORD___GET_FROM_ENTITY);
		createEOperation(transferRecordEClass, TRANSFER_RECORD___GET_TO_ENTITY);
		createEOperation(transferRecordEClass, TRANSFER_RECORD___GET_COMPANY_STATUS);
		createEOperation(transferRecordEClass, TRANSFER_RECORD___GET_RECORD_OR_DELEGATE_PRICE_EXPRESSION);
		createEOperation(transferRecordEClass, TRANSFER_RECORD___GET_RECORD_OR_DELEGATE_INCOTERM);

		// Create enums
		transferIncotermEEnum = createEEnum(TRANSFER_INCOTERM);
		transferStatusEEnum = createEEnum(TRANSFER_STATUS);
		companyStatusEEnum = createEEnum(COMPANY_STATUS);
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
		CommercialPackage theCommercialPackage = (CommercialPackage)EPackage.Registry.INSTANCE.getEPackage(CommercialPackage.eNS_URI);
		CargoPackage theCargoPackage = (CargoPackage)EPackage.Registry.INSTANCE.getEPackage(CargoPackage.eNS_URI);
		DateTimePackage theDateTimePackage = (DateTimePackage)EPackage.Registry.INSTANCE.getEPackage(DateTimePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		transferAgreementEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		transferRecordEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		transferRecordEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());

		// Initialize classes, features, and operations; add parameters
		initEClass(transferModelEClass, TransferModel.class, "TransferModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getTransferModel_TransferAgreements(), this.getTransferAgreement(), null, "transferAgreements", null, 0, -1, TransferModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTransferModel_TransferRecords(), this.getTransferRecord(), null, "transferRecords", null, 0, -1, TransferModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(transferAgreementEClass, TransferAgreement.class, "TransferAgreement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getTransferAgreement_FromEntity(), theCommercialPackage.getBaseLegalEntity(), null, "fromEntity", null, 0, 1, TransferAgreement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTransferAgreement_ToEntity(), theCommercialPackage.getBaseLegalEntity(), null, "toEntity", null, 0, 1, TransferAgreement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTransferAgreement_PriceExpression(), ecorePackage.getEString(), "priceExpression", null, 0, 1, TransferAgreement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTransferAgreement_Incoterm(), this.getTransferIncoterm(), "incoterm", null, 0, 1, TransferAgreement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTransferAgreement_CompanyStatus(), this.getCompanyStatus(), "companyStatus", null, 0, 1, TransferAgreement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(transferRecordEClass, TransferRecord.class, "TransferRecord", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getTransferRecord_TransferAgreement(), this.getTransferAgreement(), null, "transferAgreement", null, 0, 1, TransferRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		EGenericType g1 = createEGenericType(theCargoPackage.getSlot());
		EGenericType g2 = createEGenericType();
		g1.getETypeArguments().add(g2);
		initEReference(getTransferRecord_Lhs(), g1, null, "lhs", null, 0, 1, TransferRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTransferRecord_Rhs(), this.getTransferRecord(), null, "rhs", null, 0, 1, TransferRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTransferRecord_CargoReleaseDate(), theDateTimePackage.getLocalDate(), "cargoReleaseDate", null, 0, 1, TransferRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTransferRecord_PriceExpression(), ecorePackage.getEString(), "priceExpression", null, 0, 1, TransferRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTransferRecord_PricingDate(), theDateTimePackage.getLocalDate(), "pricingDate", null, 0, 1, TransferRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTransferRecord_Incoterm(), this.getTransferIncoterm(), "incoterm", null, 0, 1, TransferRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTransferRecord_Status(), this.getTransferStatus(), "status", null, 0, 1, TransferRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTransferRecord_Notes(), ecorePackage.getEString(), "notes", null, 0, 1, TransferRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTransferRecord_Stale(), ecorePackage.getEBoolean(), "stale", null, 0, 1, TransferRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getTransferRecord__GetFromEntity(), theCommercialPackage.getBaseLegalEntity(), "getFromEntity", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getTransferRecord__GetToEntity(), theCommercialPackage.getBaseLegalEntity(), "getToEntity", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getTransferRecord__GetCompanyStatus(), this.getCompanyStatus(), "getCompanyStatus", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getTransferRecord__GetRecordOrDelegatePriceExpression(), ecorePackage.getEString(), "getRecordOrDelegatePriceExpression", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getTransferRecord__GetRecordOrDelegateIncoterm(), this.getTransferIncoterm(), "getRecordOrDelegateIncoterm", 0, 1, IS_UNIQUE, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(transferIncotermEEnum, TransferIncoterm.class, "TransferIncoterm");
		addEEnumLiteral(transferIncotermEEnum, TransferIncoterm.BOTH);
		addEEnumLiteral(transferIncotermEEnum, TransferIncoterm.FOB);
		addEEnumLiteral(transferIncotermEEnum, TransferIncoterm.DES);

		initEEnum(transferStatusEEnum, TransferStatus.class, "TransferStatus");
		addEEnumLiteral(transferStatusEEnum, TransferStatus.CONFIRMED);
		addEEnumLiteral(transferStatusEEnum, TransferStatus.DRAFT);
		addEEnumLiteral(transferStatusEEnum, TransferStatus.CANCELLED);

		initEEnum(companyStatusEEnum, CompanyStatus.class, "CompanyStatus");
		addEEnumLiteral(companyStatusEEnum, CompanyStatus.INTRA);
		addEEnumLiteral(companyStatusEEnum, CompanyStatus.INTER);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.mmxlabs.com/models/pricing/expressionType
		createExpressionTypeAnnotations();
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
		  (getTransferAgreement_PriceExpression(),
		   source,
		   new String[] {
			   "type", "commodity"
		   });
		addAnnotation
		  (getTransferRecord_PriceExpression(),
		   source,
		   new String[] {
			   "type", "commodity"
		   });
	}

} //TransfersPackageImpl
