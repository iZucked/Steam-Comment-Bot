/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial;

import com.mmxlabs.models.lng.types.TypesPackage;

import com.mmxlabs.models.mmxcore.MMXCorePackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMERCIAL_MODEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMERCIAL_MODEL__PROXIES = MMXCorePackage.UUID_OBJECT__PROXIES;

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
	 * The feature id for the '<em><b>Shipping Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMERCIAL_MODEL__SHIPPING_ENTITY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Purchase Contracts</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMERCIAL_MODEL__PURCHASE_CONTRACTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Contract Slot Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMERCIAL_MODEL__CONTRACT_SLOT_EXTENSIONS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMERCIAL_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.LegalEntityImpl <em>Legal Entity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.LegalEntityImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getLegalEntity()
	 * @generated
	 */
	int LEGAL_ENTITY = 1;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEGAL_ENTITY__EXTENSIONS = TypesPackage.ALEGAL_ENTITY__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEGAL_ENTITY__PROXIES = TypesPackage.ALEGAL_ENTITY__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEGAL_ENTITY__UUID = TypesPackage.ALEGAL_ENTITY__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEGAL_ENTITY__NAME = TypesPackage.ALEGAL_ENTITY__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEGAL_ENTITY__OTHER_NAMES = TypesPackage.ALEGAL_ENTITY__OTHER_NAMES;

	/**
	 * The feature id for the '<em><b>Tax Rates</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEGAL_ENTITY__TAX_RATES = TypesPackage.ALEGAL_ENTITY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Transfer Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEGAL_ENTITY__TRANSFER_PRICE = TypesPackage.ALEGAL_ENTITY_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Legal Entity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEGAL_ENTITY_FEATURE_COUNT = TypesPackage.ALEGAL_ENTITY_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.ContractImpl <em>Contract</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.ContractImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getContract()
	 * @generated
	 */
	int CONTRACT = 2;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__EXTENSIONS = TypesPackage.ACONTRACT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__PROXIES = TypesPackage.ACONTRACT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__UUID = TypesPackage.ACONTRACT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__NAME = TypesPackage.ACONTRACT__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__OTHER_NAMES = TypesPackage.ACONTRACT__OTHER_NAMES;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__ENTITY = TypesPackage.ACONTRACT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Allowed Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__ALLOWED_PORTS = TypesPackage.ACONTRACT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Preferred Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__PREFERRED_PORT = TypesPackage.ACONTRACT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__MIN_QUANTITY = TypesPackage.ACONTRACT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__MAX_QUANTITY = TypesPackage.ACONTRACT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Restricted Lists Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE = TypesPackage.ACONTRACT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Restricted Contracts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__RESTRICTED_CONTRACTS = TypesPackage.ACONTRACT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Restricted Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__RESTRICTED_PORTS = TypesPackage.ACONTRACT_FEATURE_COUNT + 7;

	/**
	 * The number of structural features of the '<em>Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_FEATURE_COUNT = TypesPackage.ACONTRACT_FEATURE_COUNT + 8;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.SalesContractImpl <em>Sales Contract</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.SalesContractImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getSalesContract()
	 * @generated
	 */
	int SALES_CONTRACT = 3;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__EXTENSIONS = CONTRACT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__PROXIES = CONTRACT__PROXIES;

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
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__OTHER_NAMES = CONTRACT__OTHER_NAMES;

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
	 * The feature id for the '<em><b>Restricted Lists Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE = CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Restricted Contracts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__RESTRICTED_CONTRACTS = CONTRACT__RESTRICTED_CONTRACTS;

	/**
	 * The feature id for the '<em><b>Restricted Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__RESTRICTED_PORTS = CONTRACT__RESTRICTED_PORTS;

	/**
	 * The feature id for the '<em><b>Min Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__MIN_CV_VALUE = CONTRACT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Max Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__MAX_CV_VALUE = CONTRACT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Purchase Delivery Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
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
	int PURCHASE_CONTRACT = 4;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__EXTENSIONS = CONTRACT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__PROXIES = CONTRACT__PROXIES;

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
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__OTHER_NAMES = CONTRACT__OTHER_NAMES;

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
	 * The feature id for the '<em><b>Restricted Lists Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE = CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Restricted Contracts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__RESTRICTED_CONTRACTS = CONTRACT__RESTRICTED_CONTRACTS;

	/**
	 * The feature id for the '<em><b>Restricted Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__RESTRICTED_PORTS = CONTRACT__RESTRICTED_PORTS;

	/**
	 * The number of structural features of the '<em>Purchase Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT_FEATURE_COUNT = CONTRACT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.FixedPriceContractImpl <em>Fixed Price Contract</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.FixedPriceContractImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getFixedPriceContract()
	 * @generated
	 */
	int FIXED_PRICE_CONTRACT = 5;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIXED_PRICE_CONTRACT__EXTENSIONS = SALES_CONTRACT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIXED_PRICE_CONTRACT__PROXIES = SALES_CONTRACT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIXED_PRICE_CONTRACT__UUID = SALES_CONTRACT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIXED_PRICE_CONTRACT__NAME = SALES_CONTRACT__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIXED_PRICE_CONTRACT__OTHER_NAMES = SALES_CONTRACT__OTHER_NAMES;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIXED_PRICE_CONTRACT__ENTITY = SALES_CONTRACT__ENTITY;

	/**
	 * The feature id for the '<em><b>Allowed Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIXED_PRICE_CONTRACT__ALLOWED_PORTS = SALES_CONTRACT__ALLOWED_PORTS;

	/**
	 * The feature id for the '<em><b>Preferred Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIXED_PRICE_CONTRACT__PREFERRED_PORT = SALES_CONTRACT__PREFERRED_PORT;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIXED_PRICE_CONTRACT__MIN_QUANTITY = SALES_CONTRACT__MIN_QUANTITY;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIXED_PRICE_CONTRACT__MAX_QUANTITY = SALES_CONTRACT__MAX_QUANTITY;

	/**
	 * The feature id for the '<em><b>Restricted Lists Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIXED_PRICE_CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE = SALES_CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Restricted Contracts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIXED_PRICE_CONTRACT__RESTRICTED_CONTRACTS = SALES_CONTRACT__RESTRICTED_CONTRACTS;

	/**
	 * The feature id for the '<em><b>Restricted Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIXED_PRICE_CONTRACT__RESTRICTED_PORTS = SALES_CONTRACT__RESTRICTED_PORTS;

	/**
	 * The feature id for the '<em><b>Min Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIXED_PRICE_CONTRACT__MIN_CV_VALUE = SALES_CONTRACT__MIN_CV_VALUE;

	/**
	 * The feature id for the '<em><b>Max Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIXED_PRICE_CONTRACT__MAX_CV_VALUE = SALES_CONTRACT__MAX_CV_VALUE;

	/**
	 * The feature id for the '<em><b>Purchase Delivery Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIXED_PRICE_CONTRACT__PURCHASE_DELIVERY_TYPE = SALES_CONTRACT__PURCHASE_DELIVERY_TYPE;

	/**
	 * The feature id for the '<em><b>Price Per MMBTU</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIXED_PRICE_CONTRACT__PRICE_PER_MMBTU = SALES_CONTRACT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Fixed Price Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIXED_PRICE_CONTRACT_FEATURE_COUNT = SALES_CONTRACT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.IndexPriceContractImpl <em>Index Price Contract</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.IndexPriceContractImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getIndexPriceContract()
	 * @generated
	 */
	int INDEX_PRICE_CONTRACT = 6;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX_PRICE_CONTRACT__EXTENSIONS = SALES_CONTRACT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX_PRICE_CONTRACT__PROXIES = SALES_CONTRACT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX_PRICE_CONTRACT__UUID = SALES_CONTRACT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX_PRICE_CONTRACT__NAME = SALES_CONTRACT__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX_PRICE_CONTRACT__OTHER_NAMES = SALES_CONTRACT__OTHER_NAMES;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX_PRICE_CONTRACT__ENTITY = SALES_CONTRACT__ENTITY;

	/**
	 * The feature id for the '<em><b>Allowed Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX_PRICE_CONTRACT__ALLOWED_PORTS = SALES_CONTRACT__ALLOWED_PORTS;

	/**
	 * The feature id for the '<em><b>Preferred Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX_PRICE_CONTRACT__PREFERRED_PORT = SALES_CONTRACT__PREFERRED_PORT;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX_PRICE_CONTRACT__MIN_QUANTITY = SALES_CONTRACT__MIN_QUANTITY;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX_PRICE_CONTRACT__MAX_QUANTITY = SALES_CONTRACT__MAX_QUANTITY;

	/**
	 * The feature id for the '<em><b>Restricted Lists Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX_PRICE_CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE = SALES_CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Restricted Contracts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX_PRICE_CONTRACT__RESTRICTED_CONTRACTS = SALES_CONTRACT__RESTRICTED_CONTRACTS;

	/**
	 * The feature id for the '<em><b>Restricted Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX_PRICE_CONTRACT__RESTRICTED_PORTS = SALES_CONTRACT__RESTRICTED_PORTS;

	/**
	 * The feature id for the '<em><b>Min Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX_PRICE_CONTRACT__MIN_CV_VALUE = SALES_CONTRACT__MIN_CV_VALUE;

	/**
	 * The feature id for the '<em><b>Max Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX_PRICE_CONTRACT__MAX_CV_VALUE = SALES_CONTRACT__MAX_CV_VALUE;

	/**
	 * The feature id for the '<em><b>Purchase Delivery Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX_PRICE_CONTRACT__PURCHASE_DELIVERY_TYPE = SALES_CONTRACT__PURCHASE_DELIVERY_TYPE;

	/**
	 * The feature id for the '<em><b>Index</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX_PRICE_CONTRACT__INDEX = SALES_CONTRACT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Multiplier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX_PRICE_CONTRACT__MULTIPLIER = SALES_CONTRACT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Constant</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX_PRICE_CONTRACT__CONSTANT = SALES_CONTRACT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Index Price Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX_PRICE_CONTRACT_FEATURE_COUNT = SALES_CONTRACT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.NetbackPurchaseContractImpl <em>Netback Purchase Contract</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.NetbackPurchaseContractImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getNetbackPurchaseContract()
	 * @generated
	 */
	int NETBACK_PURCHASE_CONTRACT = 7;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NETBACK_PURCHASE_CONTRACT__EXTENSIONS = PURCHASE_CONTRACT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NETBACK_PURCHASE_CONTRACT__PROXIES = PURCHASE_CONTRACT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NETBACK_PURCHASE_CONTRACT__UUID = PURCHASE_CONTRACT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NETBACK_PURCHASE_CONTRACT__NAME = PURCHASE_CONTRACT__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NETBACK_PURCHASE_CONTRACT__OTHER_NAMES = PURCHASE_CONTRACT__OTHER_NAMES;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NETBACK_PURCHASE_CONTRACT__ENTITY = PURCHASE_CONTRACT__ENTITY;

	/**
	 * The feature id for the '<em><b>Allowed Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NETBACK_PURCHASE_CONTRACT__ALLOWED_PORTS = PURCHASE_CONTRACT__ALLOWED_PORTS;

	/**
	 * The feature id for the '<em><b>Preferred Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NETBACK_PURCHASE_CONTRACT__PREFERRED_PORT = PURCHASE_CONTRACT__PREFERRED_PORT;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NETBACK_PURCHASE_CONTRACT__MIN_QUANTITY = PURCHASE_CONTRACT__MIN_QUANTITY;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NETBACK_PURCHASE_CONTRACT__MAX_QUANTITY = PURCHASE_CONTRACT__MAX_QUANTITY;

	/**
	 * The feature id for the '<em><b>Restricted Lists Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NETBACK_PURCHASE_CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE = PURCHASE_CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Restricted Contracts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NETBACK_PURCHASE_CONTRACT__RESTRICTED_CONTRACTS = PURCHASE_CONTRACT__RESTRICTED_CONTRACTS;

	/**
	 * The feature id for the '<em><b>Restricted Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NETBACK_PURCHASE_CONTRACT__RESTRICTED_PORTS = PURCHASE_CONTRACT__RESTRICTED_PORTS;

	/**
	 * The feature id for the '<em><b>Notional Ballast Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NETBACK_PURCHASE_CONTRACT__NOTIONAL_BALLAST_PARAMETERS = PURCHASE_CONTRACT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Margin</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NETBACK_PURCHASE_CONTRACT__MARGIN = PURCHASE_CONTRACT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Floor Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NETBACK_PURCHASE_CONTRACT__FLOOR_PRICE = PURCHASE_CONTRACT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Netback Purchase Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NETBACK_PURCHASE_CONTRACT_FEATURE_COUNT = PURCHASE_CONTRACT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.ProfitSharePurchaseContractImpl <em>Profit Share Purchase Contract</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.ProfitSharePurchaseContractImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getProfitSharePurchaseContract()
	 * @generated
	 */
	int PROFIT_SHARE_PURCHASE_CONTRACT = 8;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARE_PURCHASE_CONTRACT__EXTENSIONS = PURCHASE_CONTRACT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARE_PURCHASE_CONTRACT__PROXIES = PURCHASE_CONTRACT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARE_PURCHASE_CONTRACT__UUID = PURCHASE_CONTRACT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARE_PURCHASE_CONTRACT__NAME = PURCHASE_CONTRACT__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARE_PURCHASE_CONTRACT__OTHER_NAMES = PURCHASE_CONTRACT__OTHER_NAMES;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARE_PURCHASE_CONTRACT__ENTITY = PURCHASE_CONTRACT__ENTITY;

	/**
	 * The feature id for the '<em><b>Allowed Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARE_PURCHASE_CONTRACT__ALLOWED_PORTS = PURCHASE_CONTRACT__ALLOWED_PORTS;

	/**
	 * The feature id for the '<em><b>Preferred Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARE_PURCHASE_CONTRACT__PREFERRED_PORT = PURCHASE_CONTRACT__PREFERRED_PORT;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARE_PURCHASE_CONTRACT__MIN_QUANTITY = PURCHASE_CONTRACT__MIN_QUANTITY;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARE_PURCHASE_CONTRACT__MAX_QUANTITY = PURCHASE_CONTRACT__MAX_QUANTITY;

	/**
	 * The feature id for the '<em><b>Restricted Lists Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARE_PURCHASE_CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE = PURCHASE_CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Restricted Contracts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARE_PURCHASE_CONTRACT__RESTRICTED_CONTRACTS = PURCHASE_CONTRACT__RESTRICTED_CONTRACTS;

	/**
	 * The feature id for the '<em><b>Restricted Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARE_PURCHASE_CONTRACT__RESTRICTED_PORTS = PURCHASE_CONTRACT__RESTRICTED_PORTS;

	/**
	 * The feature id for the '<em><b>Base Market Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_PORTS = PURCHASE_CONTRACT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Base Market Index</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_INDEX = PURCHASE_CONTRACT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Base Market Multiplier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_MULTIPLIER = PURCHASE_CONTRACT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Base Market Constant</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_CONSTANT = PURCHASE_CONTRACT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Ref Market Index</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARE_PURCHASE_CONTRACT__REF_MARKET_INDEX = PURCHASE_CONTRACT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Ref Market Multiplier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARE_PURCHASE_CONTRACT__REF_MARKET_MULTIPLIER = PURCHASE_CONTRACT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Ref Market Constant</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARE_PURCHASE_CONTRACT__REF_MARKET_CONSTANT = PURCHASE_CONTRACT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Share</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARE_PURCHASE_CONTRACT__SHARE = PURCHASE_CONTRACT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Margin</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARE_PURCHASE_CONTRACT__MARGIN = PURCHASE_CONTRACT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Sales Multiplier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARE_PURCHASE_CONTRACT__SALES_MULTIPLIER = PURCHASE_CONTRACT_FEATURE_COUNT + 9;

	/**
	 * The number of structural features of the '<em>Profit Share Purchase Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARE_PURCHASE_CONTRACT_FEATURE_COUNT = PURCHASE_CONTRACT_FEATURE_COUNT + 10;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.NotionalBallastParametersImpl <em>Notional Ballast Parameters</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.NotionalBallastParametersImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getNotionalBallastParameters()
	 * @generated
	 */
	int NOTIONAL_BALLAST_PARAMETERS = 9;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTIONAL_BALLAST_PARAMETERS__EXTENSIONS = MMXCorePackage.NAMED_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTIONAL_BALLAST_PARAMETERS__PROXIES = MMXCorePackage.NAMED_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTIONAL_BALLAST_PARAMETERS__NAME = MMXCorePackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTIONAL_BALLAST_PARAMETERS__OTHER_NAMES = MMXCorePackage.NAMED_OBJECT__OTHER_NAMES;

	/**
	 * The feature id for the '<em><b>Routes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTIONAL_BALLAST_PARAMETERS__ROUTES = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTIONAL_BALLAST_PARAMETERS__SPEED = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Hire Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTIONAL_BALLAST_PARAMETERS__HIRE_COST = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Nbo Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTIONAL_BALLAST_PARAMETERS__NBO_RATE = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Base Consumption</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTIONAL_BALLAST_PARAMETERS__BASE_CONSUMPTION = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Vessel Classes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTIONAL_BALLAST_PARAMETERS__VESSEL_CLASSES = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Notional Ballast Parameters</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTIONAL_BALLAST_PARAMETERS_FEATURE_COUNT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.RedirectionPurchaseContractImpl <em>Redirection Purchase Contract</em>}' class.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.RedirectionPurchaseContractImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getRedirectionPurchaseContract()
	 * @generated
	 */
	int REDIRECTION_PURCHASE_CONTRACT = 10;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REDIRECTION_PURCHASE_CONTRACT__EXTENSIONS = PURCHASE_CONTRACT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REDIRECTION_PURCHASE_CONTRACT__PROXIES = PURCHASE_CONTRACT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REDIRECTION_PURCHASE_CONTRACT__UUID = PURCHASE_CONTRACT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REDIRECTION_PURCHASE_CONTRACT__NAME = PURCHASE_CONTRACT__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REDIRECTION_PURCHASE_CONTRACT__OTHER_NAMES = PURCHASE_CONTRACT__OTHER_NAMES;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REDIRECTION_PURCHASE_CONTRACT__ENTITY = PURCHASE_CONTRACT__ENTITY;

	/**
	 * The feature id for the '<em><b>Allowed Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REDIRECTION_PURCHASE_CONTRACT__ALLOWED_PORTS = PURCHASE_CONTRACT__ALLOWED_PORTS;

	/**
	 * The feature id for the '<em><b>Preferred Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REDIRECTION_PURCHASE_CONTRACT__PREFERRED_PORT = PURCHASE_CONTRACT__PREFERRED_PORT;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REDIRECTION_PURCHASE_CONTRACT__MIN_QUANTITY = PURCHASE_CONTRACT__MIN_QUANTITY;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REDIRECTION_PURCHASE_CONTRACT__MAX_QUANTITY = PURCHASE_CONTRACT__MAX_QUANTITY;

	/**
	 * The feature id for the '<em><b>Restricted Lists Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REDIRECTION_PURCHASE_CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE = PURCHASE_CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Restricted Contracts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REDIRECTION_PURCHASE_CONTRACT__RESTRICTED_CONTRACTS = PURCHASE_CONTRACT__RESTRICTED_CONTRACTS;

	/**
	 * The feature id for the '<em><b>Restricted Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REDIRECTION_PURCHASE_CONTRACT__RESTRICTED_PORTS = PURCHASE_CONTRACT__RESTRICTED_PORTS;

	/**
	 * The feature id for the '<em><b>Base Sales Market Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REDIRECTION_PURCHASE_CONTRACT__BASE_SALES_MARKET_PORT = PURCHASE_CONTRACT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Base Sales Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REDIRECTION_PURCHASE_CONTRACT__BASE_SALES_PRICE_EXPRESSION = PURCHASE_CONTRACT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Base Purchase Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REDIRECTION_PURCHASE_CONTRACT__BASE_PURCHASE_PRICE_EXPRESSION = PURCHASE_CONTRACT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Notional Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REDIRECTION_PURCHASE_CONTRACT__NOTIONAL_SPEED = PURCHASE_CONTRACT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Des Purchase Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REDIRECTION_PURCHASE_CONTRACT__DES_PURCHASE_PORT = PURCHASE_CONTRACT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Source Purchase Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REDIRECTION_PURCHASE_CONTRACT__SOURCE_PURCHASE_PORT = PURCHASE_CONTRACT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Profit Share</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REDIRECTION_PURCHASE_CONTRACT__PROFIT_SHARE = PURCHASE_CONTRACT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Vessel Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REDIRECTION_PURCHASE_CONTRACT__VESSEL_CLASS = PURCHASE_CONTRACT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Hire Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REDIRECTION_PURCHASE_CONTRACT__HIRE_COST = PURCHASE_CONTRACT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Days From Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REDIRECTION_PURCHASE_CONTRACT__DAYS_FROM_SOURCE = PURCHASE_CONTRACT_FEATURE_COUNT + 9;

	/**
	 * The number of structural features of the '<em>Redirection Purchase Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REDIRECTION_PURCHASE_CONTRACT_FEATURE_COUNT = PURCHASE_CONTRACT_FEATURE_COUNT + 10;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.PriceExpressionContractImpl <em>Price Expression Contract</em>}' class.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.PriceExpressionContractImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getPriceExpressionContract()
	 * @generated
	 */
	int PRICE_EXPRESSION_CONTRACT = 11;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICE_EXPRESSION_CONTRACT__EXTENSIONS = SALES_CONTRACT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICE_EXPRESSION_CONTRACT__PROXIES = SALES_CONTRACT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICE_EXPRESSION_CONTRACT__UUID = SALES_CONTRACT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICE_EXPRESSION_CONTRACT__NAME = SALES_CONTRACT__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICE_EXPRESSION_CONTRACT__OTHER_NAMES = SALES_CONTRACT__OTHER_NAMES;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICE_EXPRESSION_CONTRACT__ENTITY = SALES_CONTRACT__ENTITY;

	/**
	 * The feature id for the '<em><b>Allowed Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICE_EXPRESSION_CONTRACT__ALLOWED_PORTS = SALES_CONTRACT__ALLOWED_PORTS;

	/**
	 * The feature id for the '<em><b>Preferred Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICE_EXPRESSION_CONTRACT__PREFERRED_PORT = SALES_CONTRACT__PREFERRED_PORT;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICE_EXPRESSION_CONTRACT__MIN_QUANTITY = SALES_CONTRACT__MIN_QUANTITY;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICE_EXPRESSION_CONTRACT__MAX_QUANTITY = SALES_CONTRACT__MAX_QUANTITY;

	/**
	 * The feature id for the '<em><b>Restricted Lists Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICE_EXPRESSION_CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE = SALES_CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Restricted Contracts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICE_EXPRESSION_CONTRACT__RESTRICTED_CONTRACTS = SALES_CONTRACT__RESTRICTED_CONTRACTS;

	/**
	 * The feature id for the '<em><b>Restricted Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICE_EXPRESSION_CONTRACT__RESTRICTED_PORTS = SALES_CONTRACT__RESTRICTED_PORTS;

	/**
	 * The feature id for the '<em><b>Min Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICE_EXPRESSION_CONTRACT__MIN_CV_VALUE = SALES_CONTRACT__MIN_CV_VALUE;

	/**
	 * The feature id for the '<em><b>Max Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICE_EXPRESSION_CONTRACT__MAX_CV_VALUE = SALES_CONTRACT__MAX_CV_VALUE;

	/**
	 * The feature id for the '<em><b>Purchase Delivery Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICE_EXPRESSION_CONTRACT__PURCHASE_DELIVERY_TYPE = SALES_CONTRACT__PURCHASE_DELIVERY_TYPE;

	/**
	 * The feature id for the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICE_EXPRESSION_CONTRACT__PRICE_EXPRESSION = SALES_CONTRACT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Price Expression Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRICE_EXPRESSION_CONTRACT_FEATURE_COUNT = SALES_CONTRACT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.RedirectionContractOriginalDateImpl <em>Redirection Contract Original Date</em>}' class.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.RedirectionContractOriginalDateImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getRedirectionContractOriginalDate()
	 * @generated
	 */
	int REDIRECTION_CONTRACT_ORIGINAL_DATE = 12;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REDIRECTION_CONTRACT_ORIGINAL_DATE__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REDIRECTION_CONTRACT_ORIGINAL_DATE__PROXIES = MMXCorePackage.UUID_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REDIRECTION_CONTRACT_ORIGINAL_DATE__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REDIRECTION_CONTRACT_ORIGINAL_DATE__DATE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Redirection Contract Original Date</em>' class.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REDIRECTION_CONTRACT_ORIGINAL_DATE_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.TaxRateImpl <em>Tax Rate</em>}' class.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.TaxRateImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getTaxRate()
	 * @generated
	 */
	int TAX_RATE = 13;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TAX_RATE__DATE = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TAX_RATE__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Tax Rate</em>' class.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TAX_RATE_FEATURE_COUNT = 2;

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
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.commercial.CommercialModel#getShippingEntity <em>Shipping Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Shipping Entity</em>'.
	 * @see com.mmxlabs.models.lng.commercial.CommercialModel#getShippingEntity()
	 * @see #getCommercialModel()
	 * @generated
	 */
	EReference getCommercialModel_ShippingEntity();

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
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.commercial.CommercialModel#getContractSlotExtensions <em>Contract Slot Extensions</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Contract Slot Extensions</em>'.
	 * @see com.mmxlabs.models.lng.commercial.CommercialModel#getContractSlotExtensions()
	 * @see #getCommercialModel()
	 * @generated
	 */
	EReference getCommercialModel_ContractSlotExtensions();

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
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.commercial.LegalEntity#getTaxRates <em>Tax Rates</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Tax Rates</em>'.
	 * @see com.mmxlabs.models.lng.commercial.LegalEntity#getTaxRates()
	 * @see #getLegalEntity()
	 * @generated
	 */
	EReference getLegalEntity_TaxRates();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.LegalEntity#getTransferPrice <em>Transfer Price</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Transfer Price</em>'.
	 * @see com.mmxlabs.models.lng.commercial.LegalEntity#getTransferPrice()
	 * @see #getLegalEntity()
	 * @generated
	 */
	EAttribute getLegalEntity_TransferPrice();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.Contract#isRestrictedListsArePermissive <em>Restricted Lists Are Permissive</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
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
	 * @since 2.0
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
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Restricted Ports</em>'.
	 * @see com.mmxlabs.models.lng.commercial.Contract#getRestrictedPorts()
	 * @see #getContract()
	 * @generated
	 */
	EReference getContract_RestrictedPorts();

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
	 * @since 2.0
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
	 * @since 2.0
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
	 * @since 2.0
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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.FixedPriceContract <em>Fixed Price Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Fixed Price Contract</em>'.
	 * @see com.mmxlabs.models.lng.commercial.FixedPriceContract
	 * @generated
	 */
	EClass getFixedPriceContract();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.FixedPriceContract#getPricePerMMBTU <em>Price Per MMBTU</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price Per MMBTU</em>'.
	 * @see com.mmxlabs.models.lng.commercial.FixedPriceContract#getPricePerMMBTU()
	 * @see #getFixedPriceContract()
	 * @generated
	 */
	EAttribute getFixedPriceContract_PricePerMMBTU();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.IndexPriceContract <em>Index Price Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Index Price Contract</em>'.
	 * @see com.mmxlabs.models.lng.commercial.IndexPriceContract
	 * @generated
	 */
	EClass getIndexPriceContract();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.commercial.IndexPriceContract#getIndex <em>Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Index</em>'.
	 * @see com.mmxlabs.models.lng.commercial.IndexPriceContract#getIndex()
	 * @see #getIndexPriceContract()
	 * @generated
	 */
	EReference getIndexPriceContract_Index();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.IndexPriceContract#getConstant <em>Constant</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Constant</em>'.
	 * @see com.mmxlabs.models.lng.commercial.IndexPriceContract#getConstant()
	 * @see #getIndexPriceContract()
	 * @generated
	 */
	EAttribute getIndexPriceContract_Constant();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.IndexPriceContract#getMultiplier <em>Multiplier</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Multiplier</em>'.
	 * @see com.mmxlabs.models.lng.commercial.IndexPriceContract#getMultiplier()
	 * @see #getIndexPriceContract()
	 * @generated
	 */
	EAttribute getIndexPriceContract_Multiplier();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.NetbackPurchaseContract <em>Netback Purchase Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Netback Purchase Contract</em>'.
	 * @see com.mmxlabs.models.lng.commercial.NetbackPurchaseContract
	 * @generated
	 */
	EClass getNetbackPurchaseContract();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.commercial.NetbackPurchaseContract#getNotionalBallastParameters <em>Notional Ballast Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Notional Ballast Parameters</em>'.
	 * @see com.mmxlabs.models.lng.commercial.NetbackPurchaseContract#getNotionalBallastParameters()
	 * @see #getNetbackPurchaseContract()
	 * @generated
	 */
	EReference getNetbackPurchaseContract_NotionalBallastParameters();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.NetbackPurchaseContract#getMargin <em>Margin</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Margin</em>'.
	 * @see com.mmxlabs.models.lng.commercial.NetbackPurchaseContract#getMargin()
	 * @see #getNetbackPurchaseContract()
	 * @generated
	 */
	EAttribute getNetbackPurchaseContract_Margin();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.NetbackPurchaseContract#getFloorPrice <em>Floor Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Floor Price</em>'.
	 * @see com.mmxlabs.models.lng.commercial.NetbackPurchaseContract#getFloorPrice()
	 * @see #getNetbackPurchaseContract()
	 * @generated
	 */
	EAttribute getNetbackPurchaseContract_FloorPrice();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract <em>Profit Share Purchase Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Profit Share Purchase Contract</em>'.
	 * @see com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract
	 * @generated
	 */
	EClass getProfitSharePurchaseContract();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract#getBaseMarketPorts <em>Base Market Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Base Market Ports</em>'.
	 * @see com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract#getBaseMarketPorts()
	 * @see #getProfitSharePurchaseContract()
	 * @generated
	 */
	EReference getProfitSharePurchaseContract_BaseMarketPorts();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract#getBaseMarketIndex <em>Base Market Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Base Market Index</em>'.
	 * @see com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract#getBaseMarketIndex()
	 * @see #getProfitSharePurchaseContract()
	 * @generated
	 */
	EReference getProfitSharePurchaseContract_BaseMarketIndex();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract#getBaseMarketConstant <em>Base Market Constant</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Base Market Constant</em>'.
	 * @see com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract#getBaseMarketConstant()
	 * @see #getProfitSharePurchaseContract()
	 * @generated
	 */
	EAttribute getProfitSharePurchaseContract_BaseMarketConstant();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract#getBaseMarketMultiplier <em>Base Market Multiplier</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Base Market Multiplier</em>'.
	 * @see com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract#getBaseMarketMultiplier()
	 * @see #getProfitSharePurchaseContract()
	 * @generated
	 */
	EAttribute getProfitSharePurchaseContract_BaseMarketMultiplier();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract#getRefMarketIndex <em>Ref Market Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Ref Market Index</em>'.
	 * @see com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract#getRefMarketIndex()
	 * @see #getProfitSharePurchaseContract()
	 * @generated
	 */
	EReference getProfitSharePurchaseContract_RefMarketIndex();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract#getRefMarketConstant <em>Ref Market Constant</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ref Market Constant</em>'.
	 * @see com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract#getRefMarketConstant()
	 * @see #getProfitSharePurchaseContract()
	 * @generated
	 */
	EAttribute getProfitSharePurchaseContract_RefMarketConstant();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract#getRefMarketMultiplier <em>Ref Market Multiplier</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ref Market Multiplier</em>'.
	 * @see com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract#getRefMarketMultiplier()
	 * @see #getProfitSharePurchaseContract()
	 * @generated
	 */
	EAttribute getProfitSharePurchaseContract_RefMarketMultiplier();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract#getShare <em>Share</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Share</em>'.
	 * @see com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract#getShare()
	 * @see #getProfitSharePurchaseContract()
	 * @generated
	 */
	EAttribute getProfitSharePurchaseContract_Share();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract#getMargin <em>Margin</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Margin</em>'.
	 * @see com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract#getMargin()
	 * @see #getProfitSharePurchaseContract()
	 * @generated
	 */
	EAttribute getProfitSharePurchaseContract_Margin();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract#getSalesMultiplier <em>Sales Multiplier</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Sales Multiplier</em>'.
	 * @see com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract#getSalesMultiplier()
	 * @see #getProfitSharePurchaseContract()
	 * @generated
	 */
	EAttribute getProfitSharePurchaseContract_SalesMultiplier();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.NotionalBallastParameters <em>Notional Ballast Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Notional Ballast Parameters</em>'.
	 * @see com.mmxlabs.models.lng.commercial.NotionalBallastParameters
	 * @generated
	 */
	EClass getNotionalBallastParameters();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.commercial.NotionalBallastParameters#getRoutes <em>Routes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Routes</em>'.
	 * @see com.mmxlabs.models.lng.commercial.NotionalBallastParameters#getRoutes()
	 * @see #getNotionalBallastParameters()
	 * @generated
	 */
	EReference getNotionalBallastParameters_Routes();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.NotionalBallastParameters#getSpeed <em>Speed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Speed</em>'.
	 * @see com.mmxlabs.models.lng.commercial.NotionalBallastParameters#getSpeed()
	 * @see #getNotionalBallastParameters()
	 * @generated
	 */
	EAttribute getNotionalBallastParameters_Speed();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.NotionalBallastParameters#getHireCost <em>Hire Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Hire Cost</em>'.
	 * @see com.mmxlabs.models.lng.commercial.NotionalBallastParameters#getHireCost()
	 * @see #getNotionalBallastParameters()
	 * @generated
	 */
	EAttribute getNotionalBallastParameters_HireCost();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.NotionalBallastParameters#getNboRate <em>Nbo Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Nbo Rate</em>'.
	 * @see com.mmxlabs.models.lng.commercial.NotionalBallastParameters#getNboRate()
	 * @see #getNotionalBallastParameters()
	 * @generated
	 */
	EAttribute getNotionalBallastParameters_NboRate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.NotionalBallastParameters#getBaseConsumption <em>Base Consumption</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Base Consumption</em>'.
	 * @see com.mmxlabs.models.lng.commercial.NotionalBallastParameters#getBaseConsumption()
	 * @see #getNotionalBallastParameters()
	 * @generated
	 */
	EAttribute getNotionalBallastParameters_BaseConsumption();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.commercial.NotionalBallastParameters#getVesselClasses <em>Vessel Classes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Vessel Classes</em>'.
	 * @see com.mmxlabs.models.lng.commercial.NotionalBallastParameters#getVesselClasses()
	 * @see #getNotionalBallastParameters()
	 * @generated
	 */
	EReference getNotionalBallastParameters_VesselClasses();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract <em>Redirection Purchase Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Redirection Purchase Contract</em>'.
	 * @see com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract
	 * @generated
	 */
	EClass getRedirectionPurchaseContract();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getBaseSalesMarketPort <em>Base Sales Market Port</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Base Sales Market Port</em>'.
	 * @see com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getBaseSalesMarketPort()
	 * @see #getRedirectionPurchaseContract()
	 * @generated
	 */
	EReference getRedirectionPurchaseContract_BaseSalesMarketPort();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getBaseSalesPriceExpression <em>Base Sales Price Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Base Sales Price Expression</em>'.
	 * @see com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getBaseSalesPriceExpression()
	 * @see #getRedirectionPurchaseContract()
	 * @generated
	 */
	EAttribute getRedirectionPurchaseContract_BaseSalesPriceExpression();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getBasePurchasePriceExpression <em>Base Purchase Price Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Base Purchase Price Expression</em>'.
	 * @see com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getBasePurchasePriceExpression()
	 * @see #getRedirectionPurchaseContract()
	 * @generated
	 */
	EAttribute getRedirectionPurchaseContract_BasePurchasePriceExpression();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getNotionalSpeed <em>Notional Speed</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Notional Speed</em>'.
	 * @see com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getNotionalSpeed()
	 * @see #getRedirectionPurchaseContract()
	 * @generated
	 */
	EAttribute getRedirectionPurchaseContract_NotionalSpeed();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getDesPurchasePort <em>Des Purchase Port</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Des Purchase Port</em>'.
	 * @see com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getDesPurchasePort()
	 * @see #getRedirectionPurchaseContract()
	 * @generated
	 */
	EReference getRedirectionPurchaseContract_DesPurchasePort();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getSourcePurchasePort <em>Source Purchase Port</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Source Purchase Port</em>'.
	 * @see com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getSourcePurchasePort()
	 * @see #getRedirectionPurchaseContract()
	 * @generated
	 */
	EReference getRedirectionPurchaseContract_SourcePurchasePort();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getProfitShare <em>Profit Share</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Profit Share</em>'.
	 * @see com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getProfitShare()
	 * @see #getRedirectionPurchaseContract()
	 * @generated
	 */
	EAttribute getRedirectionPurchaseContract_ProfitShare();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getVesselClass <em>Vessel Class</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel Class</em>'.
	 * @see com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getVesselClass()
	 * @see #getRedirectionPurchaseContract()
	 * @generated
	 */
	EReference getRedirectionPurchaseContract_VesselClass();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getHireCost <em>Hire Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Hire Cost</em>'.
	 * @see com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getHireCost()
	 * @see #getRedirectionPurchaseContract()
	 * @generated
	 */
	EAttribute getRedirectionPurchaseContract_HireCost();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getDaysFromSource <em>Days From Source</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Days From Source</em>'.
	 * @see com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getDaysFromSource()
	 * @see #getRedirectionPurchaseContract()
	 * @generated
	 */
	EAttribute getRedirectionPurchaseContract_DaysFromSource();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.PriceExpressionContract <em>Price Expression Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Price Expression Contract</em>'.
	 * @see com.mmxlabs.models.lng.commercial.PriceExpressionContract
	 * @generated
	 */
	EClass getPriceExpressionContract();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.PriceExpressionContract#getPriceExpression <em>Price Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price Expression</em>'.
	 * @see com.mmxlabs.models.lng.commercial.PriceExpressionContract#getPriceExpression()
	 * @see #getPriceExpressionContract()
	 * @generated
	 */
	EAttribute getPriceExpressionContract_PriceExpression();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.RedirectionContractOriginalDate <em>Redirection Contract Original Date</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Redirection Contract Original Date</em>'.
	 * @see com.mmxlabs.models.lng.commercial.RedirectionContractOriginalDate
	 * @generated
	 */
	EClass getRedirectionContractOriginalDate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.RedirectionContractOriginalDate#getDate <em>Date</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Date</em>'.
	 * @see com.mmxlabs.models.lng.commercial.RedirectionContractOriginalDate#getDate()
	 * @see #getRedirectionContractOriginalDate()
	 * @generated
	 */
	EAttribute getRedirectionContractOriginalDate_Date();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.TaxRate <em>Tax Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Tax Rate</em>'.
	 * @see com.mmxlabs.models.lng.commercial.TaxRate
	 * @generated
	 */
	EClass getTaxRate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.TaxRate#getDate <em>Date</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
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
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see com.mmxlabs.models.lng.commercial.TaxRate#getValue()
	 * @see #getTaxRate()
	 * @generated
	 */
	EAttribute getTaxRate_Value();

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
		 * The meta object literal for the '<em><b>Shipping Entity</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMMERCIAL_MODEL__SHIPPING_ENTITY = eINSTANCE.getCommercialModel_ShippingEntity();

		/**
		 * The meta object literal for the '<em><b>Purchase Contracts</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMMERCIAL_MODEL__PURCHASE_CONTRACTS = eINSTANCE.getCommercialModel_PurchaseContracts();

		/**
		 * The meta object literal for the '<em><b>Contract Slot Extensions</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMMERCIAL_MODEL__CONTRACT_SLOT_EXTENSIONS = eINSTANCE.getCommercialModel_ContractSlotExtensions();

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
		 * The meta object literal for the '<em><b>Tax Rates</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LEGAL_ENTITY__TAX_RATES = eINSTANCE.getLegalEntity_TaxRates();

		/**
		 * The meta object literal for the '<em><b>Transfer Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LEGAL_ENTITY__TRANSFER_PRICE = eINSTANCE.getLegalEntity_TransferPrice();

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
		 * The meta object literal for the '<em><b>Restricted Lists Are Permissive</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE = eINSTANCE.getContract_RestrictedListsArePermissive();

		/**
		 * The meta object literal for the '<em><b>Restricted Contracts</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTRACT__RESTRICTED_CONTRACTS = eINSTANCE.getContract_RestrictedContracts();

		/**
		 * The meta object literal for the '<em><b>Restricted Ports</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTRACT__RESTRICTED_PORTS = eINSTANCE.getContract_RestrictedPorts();

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
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SALES_CONTRACT__MIN_CV_VALUE = eINSTANCE.getSalesContract_MinCvValue();

		/**
		 * The meta object literal for the '<em><b>Max Cv Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SALES_CONTRACT__MAX_CV_VALUE = eINSTANCE.getSalesContract_MaxCvValue();

		/**
		 * The meta object literal for the '<em><b>Purchase Delivery Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.FixedPriceContractImpl <em>Fixed Price Contract</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.FixedPriceContractImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getFixedPriceContract()
		 * @generated
		 */
		EClass FIXED_PRICE_CONTRACT = eINSTANCE.getFixedPriceContract();

		/**
		 * The meta object literal for the '<em><b>Price Per MMBTU</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FIXED_PRICE_CONTRACT__PRICE_PER_MMBTU = eINSTANCE.getFixedPriceContract_PricePerMMBTU();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.IndexPriceContractImpl <em>Index Price Contract</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.IndexPriceContractImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getIndexPriceContract()
		 * @generated
		 */
		EClass INDEX_PRICE_CONTRACT = eINSTANCE.getIndexPriceContract();

		/**
		 * The meta object literal for the '<em><b>Index</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INDEX_PRICE_CONTRACT__INDEX = eINSTANCE.getIndexPriceContract_Index();

		/**
		 * The meta object literal for the '<em><b>Constant</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INDEX_PRICE_CONTRACT__CONSTANT = eINSTANCE.getIndexPriceContract_Constant();

		/**
		 * The meta object literal for the '<em><b>Multiplier</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INDEX_PRICE_CONTRACT__MULTIPLIER = eINSTANCE.getIndexPriceContract_Multiplier();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.NetbackPurchaseContractImpl <em>Netback Purchase Contract</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.NetbackPurchaseContractImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getNetbackPurchaseContract()
		 * @generated
		 */
		EClass NETBACK_PURCHASE_CONTRACT = eINSTANCE.getNetbackPurchaseContract();

		/**
		 * The meta object literal for the '<em><b>Notional Ballast Parameters</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NETBACK_PURCHASE_CONTRACT__NOTIONAL_BALLAST_PARAMETERS = eINSTANCE.getNetbackPurchaseContract_NotionalBallastParameters();

		/**
		 * The meta object literal for the '<em><b>Margin</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NETBACK_PURCHASE_CONTRACT__MARGIN = eINSTANCE.getNetbackPurchaseContract_Margin();

		/**
		 * The meta object literal for the '<em><b>Floor Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NETBACK_PURCHASE_CONTRACT__FLOOR_PRICE = eINSTANCE.getNetbackPurchaseContract_FloorPrice();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.ProfitSharePurchaseContractImpl <em>Profit Share Purchase Contract</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.ProfitSharePurchaseContractImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getProfitSharePurchaseContract()
		 * @generated
		 */
		EClass PROFIT_SHARE_PURCHASE_CONTRACT = eINSTANCE.getProfitSharePurchaseContract();

		/**
		 * The meta object literal for the '<em><b>Base Market Ports</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_PORTS = eINSTANCE.getProfitSharePurchaseContract_BaseMarketPorts();

		/**
		 * The meta object literal for the '<em><b>Base Market Index</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_INDEX = eINSTANCE.getProfitSharePurchaseContract_BaseMarketIndex();

		/**
		 * The meta object literal for the '<em><b>Base Market Constant</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_CONSTANT = eINSTANCE.getProfitSharePurchaseContract_BaseMarketConstant();

		/**
		 * The meta object literal for the '<em><b>Base Market Multiplier</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET_MULTIPLIER = eINSTANCE.getProfitSharePurchaseContract_BaseMarketMultiplier();

		/**
		 * The meta object literal for the '<em><b>Ref Market Index</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROFIT_SHARE_PURCHASE_CONTRACT__REF_MARKET_INDEX = eINSTANCE.getProfitSharePurchaseContract_RefMarketIndex();

		/**
		 * The meta object literal for the '<em><b>Ref Market Constant</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFIT_SHARE_PURCHASE_CONTRACT__REF_MARKET_CONSTANT = eINSTANCE.getProfitSharePurchaseContract_RefMarketConstant();

		/**
		 * The meta object literal for the '<em><b>Ref Market Multiplier</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFIT_SHARE_PURCHASE_CONTRACT__REF_MARKET_MULTIPLIER = eINSTANCE.getProfitSharePurchaseContract_RefMarketMultiplier();

		/**
		 * The meta object literal for the '<em><b>Share</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFIT_SHARE_PURCHASE_CONTRACT__SHARE = eINSTANCE.getProfitSharePurchaseContract_Share();

		/**
		 * The meta object literal for the '<em><b>Margin</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFIT_SHARE_PURCHASE_CONTRACT__MARGIN = eINSTANCE.getProfitSharePurchaseContract_Margin();

		/**
		 * The meta object literal for the '<em><b>Sales Multiplier</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFIT_SHARE_PURCHASE_CONTRACT__SALES_MULTIPLIER = eINSTANCE.getProfitSharePurchaseContract_SalesMultiplier();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.NotionalBallastParametersImpl <em>Notional Ballast Parameters</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.NotionalBallastParametersImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getNotionalBallastParameters()
		 * @generated
		 */
		EClass NOTIONAL_BALLAST_PARAMETERS = eINSTANCE.getNotionalBallastParameters();

		/**
		 * The meta object literal for the '<em><b>Routes</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NOTIONAL_BALLAST_PARAMETERS__ROUTES = eINSTANCE.getNotionalBallastParameters_Routes();

		/**
		 * The meta object literal for the '<em><b>Speed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NOTIONAL_BALLAST_PARAMETERS__SPEED = eINSTANCE.getNotionalBallastParameters_Speed();

		/**
		 * The meta object literal for the '<em><b>Hire Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NOTIONAL_BALLAST_PARAMETERS__HIRE_COST = eINSTANCE.getNotionalBallastParameters_HireCost();

		/**
		 * The meta object literal for the '<em><b>Nbo Rate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NOTIONAL_BALLAST_PARAMETERS__NBO_RATE = eINSTANCE.getNotionalBallastParameters_NboRate();

		/**
		 * The meta object literal for the '<em><b>Base Consumption</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NOTIONAL_BALLAST_PARAMETERS__BASE_CONSUMPTION = eINSTANCE.getNotionalBallastParameters_BaseConsumption();

		/**
		 * The meta object literal for the '<em><b>Vessel Classes</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NOTIONAL_BALLAST_PARAMETERS__VESSEL_CLASSES = eINSTANCE.getNotionalBallastParameters_VesselClasses();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.RedirectionPurchaseContractImpl <em>Redirection Purchase Contract</em>}' class.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.RedirectionPurchaseContractImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getRedirectionPurchaseContract()
		 * @generated
		 */
		EClass REDIRECTION_PURCHASE_CONTRACT = eINSTANCE.getRedirectionPurchaseContract();

		/**
		 * The meta object literal for the '<em><b>Base Sales Market Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REDIRECTION_PURCHASE_CONTRACT__BASE_SALES_MARKET_PORT = eINSTANCE.getRedirectionPurchaseContract_BaseSalesMarketPort();

		/**
		 * The meta object literal for the '<em><b>Base Sales Price Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REDIRECTION_PURCHASE_CONTRACT__BASE_SALES_PRICE_EXPRESSION = eINSTANCE.getRedirectionPurchaseContract_BaseSalesPriceExpression();

		/**
		 * The meta object literal for the '<em><b>Base Purchase Price Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REDIRECTION_PURCHASE_CONTRACT__BASE_PURCHASE_PRICE_EXPRESSION = eINSTANCE.getRedirectionPurchaseContract_BasePurchasePriceExpression();

		/**
		 * The meta object literal for the '<em><b>Notional Speed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REDIRECTION_PURCHASE_CONTRACT__NOTIONAL_SPEED = eINSTANCE.getRedirectionPurchaseContract_NotionalSpeed();

		/**
		 * The meta object literal for the '<em><b>Des Purchase Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REDIRECTION_PURCHASE_CONTRACT__DES_PURCHASE_PORT = eINSTANCE.getRedirectionPurchaseContract_DesPurchasePort();

		/**
		 * The meta object literal for the '<em><b>Source Purchase Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REDIRECTION_PURCHASE_CONTRACT__SOURCE_PURCHASE_PORT = eINSTANCE.getRedirectionPurchaseContract_SourcePurchasePort();

		/**
		 * The meta object literal for the '<em><b>Profit Share</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REDIRECTION_PURCHASE_CONTRACT__PROFIT_SHARE = eINSTANCE.getRedirectionPurchaseContract_ProfitShare();

		/**
		 * The meta object literal for the '<em><b>Vessel Class</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REDIRECTION_PURCHASE_CONTRACT__VESSEL_CLASS = eINSTANCE.getRedirectionPurchaseContract_VesselClass();

		/**
		 * The meta object literal for the '<em><b>Hire Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REDIRECTION_PURCHASE_CONTRACT__HIRE_COST = eINSTANCE.getRedirectionPurchaseContract_HireCost();

		/**
		 * The meta object literal for the '<em><b>Days From Source</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REDIRECTION_PURCHASE_CONTRACT__DAYS_FROM_SOURCE = eINSTANCE.getRedirectionPurchaseContract_DaysFromSource();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.PriceExpressionContractImpl <em>Price Expression Contract</em>}' class.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.PriceExpressionContractImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getPriceExpressionContract()
		 * @generated
		 */
		EClass PRICE_EXPRESSION_CONTRACT = eINSTANCE.getPriceExpressionContract();

		/**
		 * The meta object literal for the '<em><b>Price Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PRICE_EXPRESSION_CONTRACT__PRICE_EXPRESSION = eINSTANCE.getPriceExpressionContract_PriceExpression();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.RedirectionContractOriginalDateImpl <em>Redirection Contract Original Date</em>}' class.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.RedirectionContractOriginalDateImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getRedirectionContractOriginalDate()
		 * @generated
		 */
		EClass REDIRECTION_CONTRACT_ORIGINAL_DATE = eINSTANCE.getRedirectionContractOriginalDate();

		/**
		 * The meta object literal for the '<em><b>Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REDIRECTION_CONTRACT_ORIGINAL_DATE__DATE = eINSTANCE.getRedirectionContractOriginalDate_Date();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.TaxRateImpl <em>Tax Rate</em>}' class.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.TaxRateImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getTaxRate()
		 * @generated
		 */
		EClass TAX_RATE = eINSTANCE.getTaxRate();

		/**
		 * The meta object literal for the '<em><b>Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TAX_RATE__DATE = eINSTANCE.getTaxRate_Date();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TAX_RATE__VALUE = eINSTANCE.getTaxRate_Value();

	}

} //CommercialPackage
