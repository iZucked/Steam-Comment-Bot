/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.mmxcore.MMXCorePackage;

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
 * @see com.mmxlabs.models.lng.commercial.CommercialFactory
 * @model kind="package"
 * @generated
 */
public interface CommercialPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "commercial";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.mmxlabs.com/models/lng/commercial/1/";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "lng.commercial";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	CommercialPackage eINSTANCE = com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.CommercialModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialModelImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getCommercialModel()
	 * @generated
	 */
	int COMMERCIAL_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMERCIAL_MODEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMERCIAL_MODEL__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Entities</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMERCIAL_MODEL__ENTITIES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Sales Contracts</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMERCIAL_MODEL__SALES_CONTRACTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Purchase Contracts</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMERCIAL_MODEL__PURCHASE_CONTRACTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMERCIAL_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.BaseLegalEntityImpl <em>Base Legal Entity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.BaseLegalEntityImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getBaseLegalEntity()
	 * @generated
	 */
	int BASE_LEGAL_ENTITY = 1;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_LEGAL_ENTITY__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_LEGAL_ENTITY__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_LEGAL_ENTITY__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Shipping Book</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_LEGAL_ENTITY__SHIPPING_BOOK = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Trading Book</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_LEGAL_ENTITY__TRADING_BOOK = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Base Legal Entity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_LEGAL_ENTITY_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.LegalEntityImpl <em>Legal Entity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.LegalEntityImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getLegalEntity()
	 * @generated
	 */
	int LEGAL_ENTITY = 2;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEGAL_ENTITY__EXTENSIONS = BASE_LEGAL_ENTITY__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEGAL_ENTITY__UUID = BASE_LEGAL_ENTITY__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEGAL_ENTITY__NAME = BASE_LEGAL_ENTITY__NAME;

	/**
	 * The feature id for the '<em><b>Shipping Book</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEGAL_ENTITY__SHIPPING_BOOK = BASE_LEGAL_ENTITY__SHIPPING_BOOK;

	/**
	 * The feature id for the '<em><b>Trading Book</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEGAL_ENTITY__TRADING_BOOK = BASE_LEGAL_ENTITY__TRADING_BOOK;

	/**
	 * The number of structural features of the '<em>Legal Entity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEGAL_ENTITY_FEATURE_COUNT = BASE_LEGAL_ENTITY_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl <em>Contract</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.ContractImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getContract()
	 * @generated
	 */
	int CONTRACT = 3;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__ENTITY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Allowed Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__ALLOWED_PORTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Preferred Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__PREFERRED_PORT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__MIN_QUANTITY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__MAX_QUANTITY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Volume Limits Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__VOLUME_LIMITS_UNIT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Restricted Lists Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Restricted Contracts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__RESTRICTED_CONTRACTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Restricted Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__RESTRICTED_PORTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Price Info</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__PRICE_INFO = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__NOTES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>Contract Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__CONTRACT_TYPE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 12;

	/**
	 * The feature id for the '<em><b>Pricing Event</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__PRICING_EVENT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 13;

	/**
	 * The feature id for the '<em><b>Cancellation Fee</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__CANCELLATION_FEE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 14;

	/**
	 * The number of structural features of the '<em>Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 15;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.SalesContractImpl <em>Sales Contract</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.SalesContractImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getSalesContract()
	 * @generated
	 */
	int SALES_CONTRACT = 4;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__EXTENSIONS = CONTRACT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__UUID = CONTRACT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__NAME = CONTRACT__NAME;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__ENTITY = CONTRACT__ENTITY;

	/**
	 * The feature id for the '<em><b>Allowed Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__ALLOWED_PORTS = CONTRACT__ALLOWED_PORTS;

	/**
	 * The feature id for the '<em><b>Preferred Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__PREFERRED_PORT = CONTRACT__PREFERRED_PORT;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__MIN_QUANTITY = CONTRACT__MIN_QUANTITY;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__MAX_QUANTITY = CONTRACT__MAX_QUANTITY;

	/**
	 * The feature id for the '<em><b>Volume Limits Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__VOLUME_LIMITS_UNIT = CONTRACT__VOLUME_LIMITS_UNIT;

	/**
	 * The feature id for the '<em><b>Restricted Lists Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE = CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Restricted Contracts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__RESTRICTED_CONTRACTS = CONTRACT__RESTRICTED_CONTRACTS;

	/**
	 * The feature id for the '<em><b>Restricted Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__RESTRICTED_PORTS = CONTRACT__RESTRICTED_PORTS;

	/**
	 * The feature id for the '<em><b>Price Info</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__PRICE_INFO = CONTRACT__PRICE_INFO;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__NOTES = CONTRACT__NOTES;

	/**
	 * The feature id for the '<em><b>Contract Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__CONTRACT_TYPE = CONTRACT__CONTRACT_TYPE;

	/**
	 * The feature id for the '<em><b>Pricing Event</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__PRICING_EVENT = CONTRACT__PRICING_EVENT;

	/**
	 * The feature id for the '<em><b>Cancellation Fee</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__CANCELLATION_FEE = CONTRACT__CANCELLATION_FEE;

	/**
	 * The feature id for the '<em><b>Min Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__MIN_CV_VALUE = CONTRACT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Max Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__MAX_CV_VALUE = CONTRACT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Purchase Delivery Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__PURCHASE_DELIVERY_TYPE = CONTRACT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Sales Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT_FEATURE_COUNT = CONTRACT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.PurchaseContractImpl <em>Purchase Contract</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.PurchaseContractImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getPurchaseContract()
	 * @generated
	 */
	int PURCHASE_CONTRACT = 5;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__EXTENSIONS = CONTRACT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__UUID = CONTRACT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__NAME = CONTRACT__NAME;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__ENTITY = CONTRACT__ENTITY;

	/**
	 * The feature id for the '<em><b>Allowed Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__ALLOWED_PORTS = CONTRACT__ALLOWED_PORTS;

	/**
	 * The feature id for the '<em><b>Preferred Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__PREFERRED_PORT = CONTRACT__PREFERRED_PORT;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__MIN_QUANTITY = CONTRACT__MIN_QUANTITY;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__MAX_QUANTITY = CONTRACT__MAX_QUANTITY;

	/**
	 * The feature id for the '<em><b>Volume Limits Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__VOLUME_LIMITS_UNIT = CONTRACT__VOLUME_LIMITS_UNIT;

	/**
	 * The feature id for the '<em><b>Restricted Lists Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE = CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Restricted Contracts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__RESTRICTED_CONTRACTS = CONTRACT__RESTRICTED_CONTRACTS;

	/**
	 * The feature id for the '<em><b>Restricted Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__RESTRICTED_PORTS = CONTRACT__RESTRICTED_PORTS;

	/**
	 * The feature id for the '<em><b>Price Info</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__PRICE_INFO = CONTRACT__PRICE_INFO;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__NOTES = CONTRACT__NOTES;

	/**
	 * The feature id for the '<em><b>Contract Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__CONTRACT_TYPE = CONTRACT__CONTRACT_TYPE;

	/**
	 * The feature id for the '<em><b>Pricing Event</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__PRICING_EVENT = CONTRACT__PRICING_EVENT;

	/**
	 * The feature id for the '<em><b>Cancellation Fee</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__CANCELLATION_FEE = CONTRACT__CANCELLATION_FEE;

	/**
	 * The feature id for the '<em><b>Cargo CV</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__CARGO_CV = CONTRACT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Sales Delivery Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__SALES_DELIVERY_TYPE = CONTRACT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Purchase Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT_FEATURE_COUNT = CONTRACT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.TaxRateImpl <em>Tax Rate</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.TaxRateImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getTaxRate()
	 * @generated
	 */
	int TAX_RATE = 6;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TAX_RATE__DATE = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TAX_RATE__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Tax Rate</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TAX_RATE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.LNGPriceCalculatorParametersImpl <em>LNG Price Calculator Parameters</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.LNGPriceCalculatorParametersImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getLNGPriceCalculatorParameters()
	 * @generated
	 */
	int LNG_PRICE_CALCULATOR_PARAMETERS = 7;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_PRICE_CALCULATOR_PARAMETERS__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_PRICE_CALCULATOR_PARAMETERS__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The number of structural features of the '<em>LNG Price Calculator Parameters</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNG_PRICE_CALCULATOR_PARAMETERS_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.ExpressionPriceParametersImpl <em>Expression Price Parameters</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.ExpressionPriceParametersImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getExpressionPriceParameters()
	 * @generated
	 */
	int EXPRESSION_PRICE_PARAMETERS = 8;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPRESSION_PRICE_PARAMETERS__EXTENSIONS = LNG_PRICE_CALCULATOR_PARAMETERS__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPRESSION_PRICE_PARAMETERS__UUID = LNG_PRICE_CALCULATOR_PARAMETERS__UUID;

	/**
	 * The feature id for the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION = LNG_PRICE_CALCULATOR_PARAMETERS_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Expression Price Parameters</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPRESSION_PRICE_PARAMETERS_FEATURE_COUNT = LNG_PRICE_CALCULATOR_PARAMETERS_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.SlotContractParamsImpl <em>Slot Contract Params</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.SlotContractParamsImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getSlotContractParams()
	 * @generated
	 */
	int SLOT_CONTRACT_PARAMS = 9;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_CONTRACT_PARAMS__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_CONTRACT_PARAMS__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The number of structural features of the '<em>Slot Contract Params</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_CONTRACT_PARAMS_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.ContractExpressionMapEntryImpl <em>Contract Expression Map Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.ContractExpressionMapEntryImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getContractExpressionMapEntry()
	 * @generated
	 */
	int CONTRACT_EXPRESSION_MAP_ENTRY = 10;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_EXPRESSION_MAP_ENTRY__CONTRACT = 0;

	/**
	 * The feature id for the '<em><b>Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_EXPRESSION_MAP_ENTRY__EXPRESSION = 1;

	/**
	 * The number of structural features of the '<em>Contract Expression Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_EXPRESSION_MAP_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.VolumeParamsImpl <em>Volume Params</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.VolumeParamsImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getVolumeParams()
	 * @generated
	 */
	int VOLUME_PARAMS = 11;

	/**
	 * The number of structural features of the '<em>Volume Params</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VOLUME_PARAMS_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.BaseEntityBookImpl <em>Base Entity Book</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.BaseEntityBookImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getBaseEntityBook()
	 * @generated
	 */
	int BASE_ENTITY_BOOK = 12;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_ENTITY_BOOK__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_ENTITY_BOOK__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Tax Rates</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_ENTITY_BOOK__TAX_RATES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Base Entity Book</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_ENTITY_BOOK_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.SimpleEntityBookImpl <em>Simple Entity Book</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.SimpleEntityBookImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getSimpleEntityBook()
	 * @generated
	 */
	int SIMPLE_ENTITY_BOOK = 13;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_ENTITY_BOOK__EXTENSIONS = BASE_ENTITY_BOOK__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_ENTITY_BOOK__UUID = BASE_ENTITY_BOOK__UUID;

	/**
	 * The feature id for the '<em><b>Tax Rates</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_ENTITY_BOOK__TAX_RATES = BASE_ENTITY_BOOK__TAX_RATES;

	/**
	 * The number of structural features of the '<em>Simple Entity Book</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_ENTITY_BOOK_FEATURE_COUNT = BASE_ENTITY_BOOK_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.ContractType <em>Contract Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.ContractType
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getContractType()
	 * @generated
	 */
	int CONTRACT_TYPE = 14;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.PricingEvent <em>Pricing Event</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.PricingEvent
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getPricingEvent()
	 * @generated
	 */
	int PRICING_EVENT = 15;

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.CommercialModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see com.mmxlabs.models.lng.commercial.CommercialModel
	 * @generated
	 */
	EClass getCommercialModel();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.commercial.CommercialModel#getEntities <em>Entities</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Entities</em>'.
	 * @see com.mmxlabs.models.lng.commercial.CommercialModel#getEntities()
	 * @see #getCommercialModel()
	 * @generated
	 */
	EReference getCommercialModel_Entities();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.commercial.CommercialModel#getSalesContracts <em>Sales Contracts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Sales Contracts</em>'.
	 * @see com.mmxlabs.models.lng.commercial.CommercialModel#getSalesContracts()
	 * @see #getCommercialModel()
	 * @generated
	 */
	EReference getCommercialModel_SalesContracts();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.commercial.CommercialModel#getPurchaseContracts <em>Purchase Contracts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Purchase Contracts</em>'.
	 * @see com.mmxlabs.models.lng.commercial.CommercialModel#getPurchaseContracts()
	 * @see #getCommercialModel()
	 * @generated
	 */
	EReference getCommercialModel_PurchaseContracts();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.BaseLegalEntity <em>Base Legal Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Base Legal Entity</em>'.
	 * @see com.mmxlabs.models.lng.commercial.BaseLegalEntity
	 * @generated
	 */
	EClass getBaseLegalEntity();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.commercial.BaseLegalEntity#getShippingBook <em>Shipping Book</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Shipping Book</em>'.
	 * @see com.mmxlabs.models.lng.commercial.BaseLegalEntity#getShippingBook()
	 * @see #getBaseLegalEntity()
	 * @generated
	 */
	EReference getBaseLegalEntity_ShippingBook();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.commercial.BaseLegalEntity#getTradingBook <em>Trading Book</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Trading Book</em>'.
	 * @see com.mmxlabs.models.lng.commercial.BaseLegalEntity#getTradingBook()
	 * @see #getBaseLegalEntity()
	 * @generated
	 */
	EReference getBaseLegalEntity_TradingBook();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.LegalEntity <em>Legal Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Legal Entity</em>'.
	 * @see com.mmxlabs.models.lng.commercial.LegalEntity
	 * @generated
	 */
	EClass getLegalEntity();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.Contract <em>Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Contract</em>'.
	 * @see com.mmxlabs.models.lng.commercial.Contract
	 * @generated
	 */
	EClass getContract();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.commercial.Contract#getEntity <em>Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Entity</em>'.
	 * @see com.mmxlabs.models.lng.commercial.Contract#getEntity()
	 * @see #getContract()
	 * @generated
	 */
	EReference getContract_Entity();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.commercial.Contract#getAllowedPorts <em>Allowed Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Allowed Ports</em>'.
	 * @see com.mmxlabs.models.lng.commercial.Contract#getAllowedPorts()
	 * @see #getContract()
	 * @generated
	 */
	EReference getContract_AllowedPorts();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.commercial.Contract#getPreferredPort <em>Preferred Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Preferred Port</em>'.
	 * @see com.mmxlabs.models.lng.commercial.Contract#getPreferredPort()
	 * @see #getContract()
	 * @generated
	 */
	EReference getContract_PreferredPort();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.Contract#getMinQuantity <em>Min Quantity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Quantity</em>'.
	 * @see com.mmxlabs.models.lng.commercial.Contract#getMinQuantity()
	 * @see #getContract()
	 * @generated
	 */
	EAttribute getContract_MinQuantity();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.Contract#getMaxQuantity <em>Max Quantity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Quantity</em>'.
	 * @see com.mmxlabs.models.lng.commercial.Contract#getMaxQuantity()
	 * @see #getContract()
	 * @generated
	 */
	EAttribute getContract_MaxQuantity();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.Contract#getVolumeLimitsUnit <em>Volume Limits Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volume Limits Unit</em>'.
	 * @see com.mmxlabs.models.lng.commercial.Contract#getVolumeLimitsUnit()
	 * @see #getContract()
	 * @generated
	 */
	EAttribute getContract_VolumeLimitsUnit();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.Contract#isRestrictedListsArePermissive <em>Restricted Lists Are Permissive</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Restricted Lists Are Permissive</em>'.
	 * @see com.mmxlabs.models.lng.commercial.Contract#isRestrictedListsArePermissive()
	 * @see #getContract()
	 * @generated
	 */
	EAttribute getContract_RestrictedListsArePermissive();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.commercial.Contract#getRestrictedContracts <em>Restricted Contracts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Restricted Contracts</em>'.
	 * @see com.mmxlabs.models.lng.commercial.Contract#getRestrictedContracts()
	 * @see #getContract()
	 * @generated
	 */
	EReference getContract_RestrictedContracts();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.commercial.Contract#getRestrictedPorts <em>Restricted Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Restricted Ports</em>'.
	 * @see com.mmxlabs.models.lng.commercial.Contract#getRestrictedPorts()
	 * @see #getContract()
	 * @generated
	 */
	EReference getContract_RestrictedPorts();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.commercial.Contract#getPriceInfo <em>Price Info</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Price Info</em>'.
	 * @see com.mmxlabs.models.lng.commercial.Contract#getPriceInfo()
	 * @see #getContract()
	 * @generated
	 */
	EReference getContract_PriceInfo();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.Contract#getNotes <em>Notes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Notes</em>'.
	 * @see com.mmxlabs.models.lng.commercial.Contract#getNotes()
	 * @see #getContract()
	 * @generated
	 */
	EAttribute getContract_Notes();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.Contract#getContractType <em>Contract Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Contract Type</em>'.
	 * @see com.mmxlabs.models.lng.commercial.Contract#getContractType()
	 * @see #getContract()
	 * @generated
	 */
	EAttribute getContract_ContractType();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.Contract#getPricingEvent <em>Pricing Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Pricing Event</em>'.
	 * @see com.mmxlabs.models.lng.commercial.Contract#getPricingEvent()
	 * @see #getContract()
	 * @generated
	 */
	EAttribute getContract_PricingEvent();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.Contract#getCancellationFee <em>Cancellation Fee</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cancellation Fee</em>'.
	 * @see com.mmxlabs.models.lng.commercial.Contract#getCancellationFee()
	 * @see #getContract()
	 * @generated
	 */
	EAttribute getContract_CancellationFee();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.SalesContract <em>Sales Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Sales Contract</em>'.
	 * @see com.mmxlabs.models.lng.commercial.SalesContract
	 * @generated
	 */
	EClass getSalesContract();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.SalesContract#getMinCvValue <em>Min Cv Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Cv Value</em>'.
	 * @see com.mmxlabs.models.lng.commercial.SalesContract#getMinCvValue()
	 * @see #getSalesContract()
	 * @generated
	 */
	EAttribute getSalesContract_MinCvValue();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.SalesContract#getMaxCvValue <em>Max Cv Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Cv Value</em>'.
	 * @see com.mmxlabs.models.lng.commercial.SalesContract#getMaxCvValue()
	 * @see #getSalesContract()
	 * @generated
	 */
	EAttribute getSalesContract_MaxCvValue();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.SalesContract#getPurchaseDeliveryType <em>Purchase Delivery Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Purchase Delivery Type</em>'.
	 * @see com.mmxlabs.models.lng.commercial.SalesContract#getPurchaseDeliveryType()
	 * @see #getSalesContract()
	 * @generated
	 */
	EAttribute getSalesContract_PurchaseDeliveryType();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.PurchaseContract <em>Purchase Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Purchase Contract</em>'.
	 * @see com.mmxlabs.models.lng.commercial.PurchaseContract
	 * @generated
	 */
	EClass getPurchaseContract();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.PurchaseContract#getCargoCV <em>Cargo CV</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cargo CV</em>'.
	 * @see com.mmxlabs.models.lng.commercial.PurchaseContract#getCargoCV()
	 * @see #getPurchaseContract()
	 * @generated
	 */
	EAttribute getPurchaseContract_CargoCV();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.PurchaseContract#getSalesDeliveryType <em>Sales Delivery Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Sales Delivery Type</em>'.
	 * @see com.mmxlabs.models.lng.commercial.PurchaseContract#getSalesDeliveryType()
	 * @see #getPurchaseContract()
	 * @generated
	 */
	EAttribute getPurchaseContract_SalesDeliveryType();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.TaxRate <em>Tax Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Tax Rate</em>'.
	 * @see com.mmxlabs.models.lng.commercial.TaxRate
	 * @generated
	 */
	EClass getTaxRate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.TaxRate#getDate <em>Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Date</em>'.
	 * @see com.mmxlabs.models.lng.commercial.TaxRate#getDate()
	 * @see #getTaxRate()
	 * @generated
	 */
	EAttribute getTaxRate_Date();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.TaxRate#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see com.mmxlabs.models.lng.commercial.TaxRate#getValue()
	 * @see #getTaxRate()
	 * @generated
	 */
	EAttribute getTaxRate_Value();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters <em>LNG Price Calculator Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>LNG Price Calculator Parameters</em>'.
	 * @see com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters
	 * @generated
	 */
	EClass getLNGPriceCalculatorParameters();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.ExpressionPriceParameters <em>Expression Price Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Expression Price Parameters</em>'.
	 * @see com.mmxlabs.models.lng.commercial.ExpressionPriceParameters
	 * @generated
	 */
	EClass getExpressionPriceParameters();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.ExpressionPriceParameters#getPriceExpression <em>Price Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price Expression</em>'.
	 * @see com.mmxlabs.models.lng.commercial.ExpressionPriceParameters#getPriceExpression()
	 * @see #getExpressionPriceParameters()
	 * @generated
	 */
	EAttribute getExpressionPriceParameters_PriceExpression();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.SlotContractParams <em>Slot Contract Params</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Slot Contract Params</em>'.
	 * @see com.mmxlabs.models.lng.commercial.SlotContractParams
	 * @generated
	 */
	EClass getSlotContractParams();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.ContractExpressionMapEntry <em>Contract Expression Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Contract Expression Map Entry</em>'.
	 * @see com.mmxlabs.models.lng.commercial.ContractExpressionMapEntry
	 * @generated
	 */
	EClass getContractExpressionMapEntry();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.commercial.ContractExpressionMapEntry#getContract <em>Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Contract</em>'.
	 * @see com.mmxlabs.models.lng.commercial.ContractExpressionMapEntry#getContract()
	 * @see #getContractExpressionMapEntry()
	 * @generated
	 */
	EReference getContractExpressionMapEntry_Contract();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.ContractExpressionMapEntry#getExpression <em>Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Expression</em>'.
	 * @see com.mmxlabs.models.lng.commercial.ContractExpressionMapEntry#getExpression()
	 * @see #getContractExpressionMapEntry()
	 * @generated
	 */
	EAttribute getContractExpressionMapEntry_Expression();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.VolumeParams <em>Volume Params</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Volume Params</em>'.
	 * @see com.mmxlabs.models.lng.commercial.VolumeParams
	 * @generated
	 */
	EClass getVolumeParams();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.BaseEntityBook <em>Base Entity Book</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Base Entity Book</em>'.
	 * @see com.mmxlabs.models.lng.commercial.BaseEntityBook
	 * @generated
	 */
	EClass getBaseEntityBook();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.commercial.BaseEntityBook#getTaxRates <em>Tax Rates</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Tax Rates</em>'.
	 * @see com.mmxlabs.models.lng.commercial.BaseEntityBook#getTaxRates()
	 * @see #getBaseEntityBook()
	 * @generated
	 */
	EReference getBaseEntityBook_TaxRates();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.SimpleEntityBook <em>Simple Entity Book</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Simple Entity Book</em>'.
	 * @see com.mmxlabs.models.lng.commercial.SimpleEntityBook
	 * @generated
	 */
	EClass getSimpleEntityBook();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.commercial.ContractType <em>Contract Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Contract Type</em>'.
	 * @see com.mmxlabs.models.lng.commercial.ContractType
	 * @generated
	 */
	EEnum getContractType();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.commercial.PricingEvent <em>Pricing Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Pricing Event</em>'.
	 * @see com.mmxlabs.models.lng.commercial.PricingEvent
	 * @generated
	 */
	EEnum getPricingEvent();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	CommercialFactory getCommercialFactory();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.CommercialModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialModelImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getCommercialModel()
		 * @generated
		 */
		EClass COMMERCIAL_MODEL = eINSTANCE.getCommercialModel();

		/**
		 * The meta object literal for the '<em><b>Entities</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMMERCIAL_MODEL__ENTITIES = eINSTANCE.getCommercialModel_Entities();

		/**
		 * The meta object literal for the '<em><b>Sales Contracts</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMMERCIAL_MODEL__SALES_CONTRACTS = eINSTANCE.getCommercialModel_SalesContracts();

		/**
		 * The meta object literal for the '<em><b>Purchase Contracts</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMMERCIAL_MODEL__PURCHASE_CONTRACTS = eINSTANCE.getCommercialModel_PurchaseContracts();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.BaseLegalEntityImpl <em>Base Legal Entity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.BaseLegalEntityImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getBaseLegalEntity()
		 * @generated
		 */
		EClass BASE_LEGAL_ENTITY = eINSTANCE.getBaseLegalEntity();

		/**
		 * The meta object literal for the '<em><b>Shipping Book</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BASE_LEGAL_ENTITY__SHIPPING_BOOK = eINSTANCE.getBaseLegalEntity_ShippingBook();

		/**
		 * The meta object literal for the '<em><b>Trading Book</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BASE_LEGAL_ENTITY__TRADING_BOOK = eINSTANCE.getBaseLegalEntity_TradingBook();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.LegalEntityImpl <em>Legal Entity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.LegalEntityImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getLegalEntity()
		 * @generated
		 */
		EClass LEGAL_ENTITY = eINSTANCE.getLegalEntity();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl <em>Contract</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.ContractImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getContract()
		 * @generated
		 */
		EClass CONTRACT = eINSTANCE.getContract();

		/**
		 * The meta object literal for the '<em><b>Entity</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTRACT__ENTITY = eINSTANCE.getContract_Entity();

		/**
		 * The meta object literal for the '<em><b>Allowed Ports</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTRACT__ALLOWED_PORTS = eINSTANCE.getContract_AllowedPorts();

		/**
		 * The meta object literal for the '<em><b>Preferred Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTRACT__PREFERRED_PORT = eINSTANCE.getContract_PreferredPort();

		/**
		 * The meta object literal for the '<em><b>Min Quantity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTRACT__MIN_QUANTITY = eINSTANCE.getContract_MinQuantity();

		/**
		 * The meta object literal for the '<em><b>Max Quantity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTRACT__MAX_QUANTITY = eINSTANCE.getContract_MaxQuantity();

		/**
		 * The meta object literal for the '<em><b>Volume Limits Unit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTRACT__VOLUME_LIMITS_UNIT = eINSTANCE.getContract_VolumeLimitsUnit();

		/**
		 * The meta object literal for the '<em><b>Restricted Lists Are Permissive</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE = eINSTANCE.getContract_RestrictedListsArePermissive();

		/**
		 * The meta object literal for the '<em><b>Restricted Contracts</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTRACT__RESTRICTED_CONTRACTS = eINSTANCE.getContract_RestrictedContracts();

		/**
		 * The meta object literal for the '<em><b>Restricted Ports</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTRACT__RESTRICTED_PORTS = eINSTANCE.getContract_RestrictedPorts();

		/**
		 * The meta object literal for the '<em><b>Price Info</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTRACT__PRICE_INFO = eINSTANCE.getContract_PriceInfo();

		/**
		 * The meta object literal for the '<em><b>Notes</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTRACT__NOTES = eINSTANCE.getContract_Notes();

		/**
		 * The meta object literal for the '<em><b>Contract Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTRACT__CONTRACT_TYPE = eINSTANCE.getContract_ContractType();

		/**
		 * The meta object literal for the '<em><b>Pricing Event</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTRACT__PRICING_EVENT = eINSTANCE.getContract_PricingEvent();

		/**
		 * The meta object literal for the '<em><b>Cancellation Fee</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTRACT__CANCELLATION_FEE = eINSTANCE.getContract_CancellationFee();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.SalesContractImpl <em>Sales Contract</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.SalesContractImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getSalesContract()
		 * @generated
		 */
		EClass SALES_CONTRACT = eINSTANCE.getSalesContract();

		/**
		 * The meta object literal for the '<em><b>Min Cv Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SALES_CONTRACT__MIN_CV_VALUE = eINSTANCE.getSalesContract_MinCvValue();

		/**
		 * The meta object literal for the '<em><b>Max Cv Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SALES_CONTRACT__MAX_CV_VALUE = eINSTANCE.getSalesContract_MaxCvValue();

		/**
		 * The meta object literal for the '<em><b>Purchase Delivery Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SALES_CONTRACT__PURCHASE_DELIVERY_TYPE = eINSTANCE.getSalesContract_PurchaseDeliveryType();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.PurchaseContractImpl <em>Purchase Contract</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.PurchaseContractImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getPurchaseContract()
		 * @generated
		 */
		EClass PURCHASE_CONTRACT = eINSTANCE.getPurchaseContract();

		/**
		 * The meta object literal for the '<em><b>Cargo CV</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PURCHASE_CONTRACT__CARGO_CV = eINSTANCE.getPurchaseContract_CargoCV();

		/**
		 * The meta object literal for the '<em><b>Sales Delivery Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PURCHASE_CONTRACT__SALES_DELIVERY_TYPE = eINSTANCE.getPurchaseContract_SalesDeliveryType();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.TaxRateImpl <em>Tax Rate</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.TaxRateImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getTaxRate()
		 * @generated
		 */
		EClass TAX_RATE = eINSTANCE.getTaxRate();

		/**
		 * The meta object literal for the '<em><b>Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TAX_RATE__DATE = eINSTANCE.getTaxRate_Date();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TAX_RATE__VALUE = eINSTANCE.getTaxRate_Value();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.LNGPriceCalculatorParametersImpl <em>LNG Price Calculator Parameters</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.LNGPriceCalculatorParametersImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getLNGPriceCalculatorParameters()
		 * @generated
		 */
		EClass LNG_PRICE_CALCULATOR_PARAMETERS = eINSTANCE.getLNGPriceCalculatorParameters();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.ExpressionPriceParametersImpl <em>Expression Price Parameters</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.ExpressionPriceParametersImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getExpressionPriceParameters()
		 * @generated
		 */
		EClass EXPRESSION_PRICE_PARAMETERS = eINSTANCE.getExpressionPriceParameters();

		/**
		 * The meta object literal for the '<em><b>Price Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION = eINSTANCE.getExpressionPriceParameters_PriceExpression();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.SlotContractParamsImpl <em>Slot Contract Params</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.SlotContractParamsImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getSlotContractParams()
		 * @generated
		 */
		EClass SLOT_CONTRACT_PARAMS = eINSTANCE.getSlotContractParams();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.ContractExpressionMapEntryImpl <em>Contract Expression Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.ContractExpressionMapEntryImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getContractExpressionMapEntry()
		 * @generated
		 */
		EClass CONTRACT_EXPRESSION_MAP_ENTRY = eINSTANCE.getContractExpressionMapEntry();

		/**
		 * The meta object literal for the '<em><b>Contract</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTRACT_EXPRESSION_MAP_ENTRY__CONTRACT = eINSTANCE.getContractExpressionMapEntry_Contract();

		/**
		 * The meta object literal for the '<em><b>Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTRACT_EXPRESSION_MAP_ENTRY__EXPRESSION = eINSTANCE.getContractExpressionMapEntry_Expression();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.VolumeParamsImpl <em>Volume Params</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.VolumeParamsImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getVolumeParams()
		 * @generated
		 */
		EClass VOLUME_PARAMS = eINSTANCE.getVolumeParams();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.BaseEntityBookImpl <em>Base Entity Book</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.BaseEntityBookImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getBaseEntityBook()
		 * @generated
		 */
		EClass BASE_ENTITY_BOOK = eINSTANCE.getBaseEntityBook();

		/**
		 * The meta object literal for the '<em><b>Tax Rates</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BASE_ENTITY_BOOK__TAX_RATES = eINSTANCE.getBaseEntityBook_TaxRates();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.SimpleEntityBookImpl <em>Simple Entity Book</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.SimpleEntityBookImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getSimpleEntityBook()
		 * @generated
		 */
		EClass SIMPLE_ENTITY_BOOK = eINSTANCE.getSimpleEntityBook();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.ContractType <em>Contract Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.ContractType
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getContractType()
		 * @generated
		 */
		EEnum CONTRACT_TYPE = eINSTANCE.getContractType();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.PricingEvent <em>Pricing Event</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.PricingEvent
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getPricingEvent()
		 * @generated
		 */
		EEnum PRICING_EVENT = eINSTANCE.getPricingEvent();

	}

} //CommercialPackage
