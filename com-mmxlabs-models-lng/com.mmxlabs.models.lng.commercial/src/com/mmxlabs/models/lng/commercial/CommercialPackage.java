/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
	 * The feature id for the '<em><b>Chartering Contracts</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMERCIAL_MODEL__CHARTERING_CONTRACTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMERCIAL_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

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
	 * The feature id for the '<em><b>Upstream Book</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_LEGAL_ENTITY__UPSTREAM_BOOK = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Third Party</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_LEGAL_ENTITY__THIRD_PARTY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Base Legal Entity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_LEGAL_ENTITY_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

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
	 * The feature id for the '<em><b>Upstream Book</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEGAL_ENTITY__UPSTREAM_BOOK = BASE_LEGAL_ENTITY__UPSTREAM_BOOK;

	/**
	 * The feature id for the '<em><b>Third Party</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEGAL_ENTITY__THIRD_PARTY = BASE_LEGAL_ENTITY__THIRD_PARTY;

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
	 * The feature id for the '<em><b>Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__CODE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Counterparty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__COUNTERPARTY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Cn</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__CN = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__ENTITY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Start Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__START_DATE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>End Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__END_DATE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Contract Year Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__CONTRACT_YEAR_START = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Allowed Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__ALLOWED_PORTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Preferred Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__PREFERRED_PORT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__MIN_QUANTITY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__MAX_QUANTITY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>Volume Limits Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__VOLUME_LIMITS_UNIT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 12;

	/**
	 * The feature id for the '<em><b>Operational Tolerance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__OPERATIONAL_TOLERANCE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 13;

	/**
	 * The feature id for the '<em><b>Full Cargo Lot</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__FULL_CARGO_LOT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 14;

	/**
	 * The feature id for the '<em><b>Restricted Contracts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__RESTRICTED_CONTRACTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 15;

	/**
	 * The feature id for the '<em><b>Restricted Contracts Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 16;

	/**
	 * The feature id for the '<em><b>Restricted Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__RESTRICTED_PORTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 17;

	/**
	 * The feature id for the '<em><b>Restricted Ports Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__RESTRICTED_PORTS_ARE_PERMISSIVE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 18;

	/**
	 * The feature id for the '<em><b>Restricted Vessels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__RESTRICTED_VESSELS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 19;

	/**
	 * The feature id for the '<em><b>Restricted Vessels Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__RESTRICTED_VESSELS_ARE_PERMISSIVE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 20;

	/**
	 * The feature id for the '<em><b>Price Info</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__PRICE_INFO = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 21;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__NOTES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 22;

	/**
	 * The feature id for the '<em><b>Contract Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__CONTRACT_TYPE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 23;

	/**
	 * The feature id for the '<em><b>Pricing Event</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__PRICING_EVENT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 24;

	/**
	 * The feature id for the '<em><b>Cancellation Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__CANCELLATION_EXPRESSION = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 25;

	/**
	 * The feature id for the '<em><b>Shipping Days Restriction</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__SHIPPING_DAYS_RESTRICTION = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 26;

	/**
	 * The number of structural features of the '<em>Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 27;

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
	 * The feature id for the '<em><b>Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__CODE = CONTRACT__CODE;

	/**
	 * The feature id for the '<em><b>Counterparty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__COUNTERPARTY = CONTRACT__COUNTERPARTY;

	/**
	 * The feature id for the '<em><b>Cn</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__CN = CONTRACT__CN;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__ENTITY = CONTRACT__ENTITY;

	/**
	 * The feature id for the '<em><b>Start Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__START_DATE = CONTRACT__START_DATE;

	/**
	 * The feature id for the '<em><b>End Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__END_DATE = CONTRACT__END_DATE;

	/**
	 * The feature id for the '<em><b>Contract Year Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__CONTRACT_YEAR_START = CONTRACT__CONTRACT_YEAR_START;

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
	 * The feature id for the '<em><b>Operational Tolerance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__OPERATIONAL_TOLERANCE = CONTRACT__OPERATIONAL_TOLERANCE;

	/**
	 * The feature id for the '<em><b>Full Cargo Lot</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__FULL_CARGO_LOT = CONTRACT__FULL_CARGO_LOT;

	/**
	 * The feature id for the '<em><b>Restricted Contracts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__RESTRICTED_CONTRACTS = CONTRACT__RESTRICTED_CONTRACTS;

	/**
	 * The feature id for the '<em><b>Restricted Contracts Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE = CONTRACT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Restricted Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__RESTRICTED_PORTS = CONTRACT__RESTRICTED_PORTS;

	/**
	 * The feature id for the '<em><b>Restricted Ports Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__RESTRICTED_PORTS_ARE_PERMISSIVE = CONTRACT__RESTRICTED_PORTS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Restricted Vessels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__RESTRICTED_VESSELS = CONTRACT__RESTRICTED_VESSELS;

	/**
	 * The feature id for the '<em><b>Restricted Vessels Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__RESTRICTED_VESSELS_ARE_PERMISSIVE = CONTRACT__RESTRICTED_VESSELS_ARE_PERMISSIVE;

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
	 * The feature id for the '<em><b>Cancellation Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__CANCELLATION_EXPRESSION = CONTRACT__CANCELLATION_EXPRESSION;

	/**
	 * The feature id for the '<em><b>Shipping Days Restriction</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__SHIPPING_DAYS_RESTRICTION = CONTRACT__SHIPPING_DAYS_RESTRICTION;

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
	 * The feature id for the '<em><b>Fob Sale Deal Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__FOB_SALE_DEAL_TYPE = CONTRACT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Sales Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT_FEATURE_COUNT = CONTRACT_FEATURE_COUNT + 4;

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
	 * The feature id for the '<em><b>Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__CODE = CONTRACT__CODE;

	/**
	 * The feature id for the '<em><b>Counterparty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__COUNTERPARTY = CONTRACT__COUNTERPARTY;

	/**
	 * The feature id for the '<em><b>Cn</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__CN = CONTRACT__CN;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__ENTITY = CONTRACT__ENTITY;

	/**
	 * The feature id for the '<em><b>Start Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__START_DATE = CONTRACT__START_DATE;

	/**
	 * The feature id for the '<em><b>End Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__END_DATE = CONTRACT__END_DATE;

	/**
	 * The feature id for the '<em><b>Contract Year Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__CONTRACT_YEAR_START = CONTRACT__CONTRACT_YEAR_START;

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
	 * The feature id for the '<em><b>Operational Tolerance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__OPERATIONAL_TOLERANCE = CONTRACT__OPERATIONAL_TOLERANCE;

	/**
	 * The feature id for the '<em><b>Full Cargo Lot</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__FULL_CARGO_LOT = CONTRACT__FULL_CARGO_LOT;

	/**
	 * The feature id for the '<em><b>Restricted Contracts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__RESTRICTED_CONTRACTS = CONTRACT__RESTRICTED_CONTRACTS;

	/**
	 * The feature id for the '<em><b>Restricted Contracts Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE = CONTRACT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Restricted Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__RESTRICTED_PORTS = CONTRACT__RESTRICTED_PORTS;

	/**
	 * The feature id for the '<em><b>Restricted Ports Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__RESTRICTED_PORTS_ARE_PERMISSIVE = CONTRACT__RESTRICTED_PORTS_ARE_PERMISSIVE;

	/**
	 * The feature id for the '<em><b>Restricted Vessels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__RESTRICTED_VESSELS = CONTRACT__RESTRICTED_VESSELS;

	/**
	 * The feature id for the '<em><b>Restricted Vessels Are Permissive</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__RESTRICTED_VESSELS_ARE_PERMISSIVE = CONTRACT__RESTRICTED_VESSELS_ARE_PERMISSIVE;

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
	 * The feature id for the '<em><b>Cancellation Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__CANCELLATION_EXPRESSION = CONTRACT__CANCELLATION_EXPRESSION;

	/**
	 * The feature id for the '<em><b>Shipping Days Restriction</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__SHIPPING_DAYS_RESTRICTION = CONTRACT__SHIPPING_DAYS_RESTRICTION;

	/**
	 * The feature id for the '<em><b>Sales Delivery Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__SALES_DELIVERY_TYPE = CONTRACT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Des Purchase Deal Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__DES_PURCHASE_DEAL_TYPE = CONTRACT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Cargo CV</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT__CARGO_CV = CONTRACT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Purchase Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT_FEATURE_COUNT = CONTRACT_FEATURE_COUNT + 3;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.DateShiftExpressionPriceParametersImpl <em>Date Shift Expression Price Parameters</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.DateShiftExpressionPriceParametersImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getDateShiftExpressionPriceParameters()
	 * @generated
	 */
	int DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS = 14;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__EXTENSIONS = LNG_PRICE_CALCULATOR_PARAMETERS__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__UUID = LNG_PRICE_CALCULATOR_PARAMETERS__UUID;

	/**
	 * The feature id for the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION = LNG_PRICE_CALCULATOR_PARAMETERS_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Specific Day</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__SPECIFIC_DAY = LNG_PRICE_CALCULATOR_PARAMETERS_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__VALUE = LNG_PRICE_CALCULATOR_PARAMETERS_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Date Shift Expression Price Parameters</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS_FEATURE_COUNT = LNG_PRICE_CALCULATOR_PARAMETERS_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.GenericCharterContractImpl <em>Generic Charter Contract</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.GenericCharterContractImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getGenericCharterContract()
	 * @generated
	 */
	int GENERIC_CHARTER_CONTRACT = 15;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_CHARTER_CONTRACT__EXTENSIONS = MMXCorePackage.NAMED_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_CHARTER_CONTRACT__NAME = MMXCorePackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_CHARTER_CONTRACT__UUID = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Min Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_CHARTER_CONTRACT__MIN_DURATION = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Max Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_CHARTER_CONTRACT__MAX_DURATION = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Repositioning Fee Terms</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_CHARTER_CONTRACT__REPOSITIONING_FEE_TERMS = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Ballast Bonus Terms</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_CHARTER_CONTRACT__BALLAST_BONUS_TERMS = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Generic Charter Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_CHARTER_CONTRACT_FEATURE_COUNT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.IRepositioningFee <em>IRepositioning Fee</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.IRepositioningFee
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getIRepositioningFee()
	 * @generated
	 */
	int IREPOSITIONING_FEE = 16;

	/**
	 * The number of structural features of the '<em>IRepositioning Fee</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IREPOSITIONING_FEE_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.SimpleRepositioningFeeContainerImpl <em>Simple Repositioning Fee Container</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.SimpleRepositioningFeeContainerImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getSimpleRepositioningFeeContainer()
	 * @generated
	 */
	int SIMPLE_REPOSITIONING_FEE_CONTAINER = 17;

	/**
	 * The feature id for the '<em><b>Terms</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_REPOSITIONING_FEE_CONTAINER__TERMS = IREPOSITIONING_FEE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Simple Repositioning Fee Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_REPOSITIONING_FEE_CONTAINER_FEATURE_COUNT = IREPOSITIONING_FEE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.IBallastBonus <em>IBallast Bonus</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.IBallastBonus
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getIBallastBonus()
	 * @generated
	 */
	int IBALLAST_BONUS = 18;

	/**
	 * The number of structural features of the '<em>IBallast Bonus</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IBALLAST_BONUS_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.SimpleBallastBonusContainerImpl <em>Simple Ballast Bonus Container</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.SimpleBallastBonusContainerImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getSimpleBallastBonusContainer()
	 * @generated
	 */
	int SIMPLE_BALLAST_BONUS_CONTAINER = 19;

	/**
	 * The feature id for the '<em><b>Terms</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_BALLAST_BONUS_CONTAINER__TERMS = IBALLAST_BONUS_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Simple Ballast Bonus Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_BALLAST_BONUS_CONTAINER_FEATURE_COUNT = IBALLAST_BONUS_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.MonthlyBallastBonusContainerImpl <em>Monthly Ballast Bonus Container</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.MonthlyBallastBonusContainerImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getMonthlyBallastBonusContainer()
	 * @generated
	 */
	int MONTHLY_BALLAST_BONUS_CONTAINER = 20;

	/**
	 * The feature id for the '<em><b>Hubs</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONTHLY_BALLAST_BONUS_CONTAINER__HUBS = IBALLAST_BONUS_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Terms</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONTHLY_BALLAST_BONUS_CONTAINER__TERMS = IBALLAST_BONUS_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Monthly Ballast Bonus Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONTHLY_BALLAST_BONUS_CONTAINER_FEATURE_COUNT = IBALLAST_BONUS_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.LumpSumTermImpl <em>Lump Sum Term</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.LumpSumTermImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getLumpSumTerm()
	 * @generated
	 */
	int LUMP_SUM_TERM = 21;

	/**
	 * The feature id for the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LUMP_SUM_TERM__PRICE_EXPRESSION = 0;

	/**
	 * The number of structural features of the '<em>Lump Sum Term</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LUMP_SUM_TERM_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.NotionalJourneyTermImpl <em>Notional Journey Term</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.NotionalJourneyTermImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getNotionalJourneyTerm()
	 * @generated
	 */
	int NOTIONAL_JOURNEY_TERM = 22;

	/**
	 * The feature id for the '<em><b>Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTIONAL_JOURNEY_TERM__SPEED = 0;

	/**
	 * The feature id for the '<em><b>Fuel Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTIONAL_JOURNEY_TERM__FUEL_PRICE_EXPRESSION = 1;

	/**
	 * The feature id for the '<em><b>Hire Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTIONAL_JOURNEY_TERM__HIRE_PRICE_EXPRESSION = 2;

	/**
	 * The feature id for the '<em><b>Include Canal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTIONAL_JOURNEY_TERM__INCLUDE_CANAL = 3;

	/**
	 * The feature id for the '<em><b>Include Canal Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTIONAL_JOURNEY_TERM__INCLUDE_CANAL_TIME = 4;

	/**
	 * The feature id for the '<em><b>Lump Sum Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTIONAL_JOURNEY_TERM__LUMP_SUM_PRICE_EXPRESSION = 5;

	/**
	 * The number of structural features of the '<em>Notional Journey Term</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTIONAL_JOURNEY_TERM_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.BallastBonusTermImpl <em>Ballast Bonus Term</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.BallastBonusTermImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getBallastBonusTerm()
	 * @generated
	 */
	int BALLAST_BONUS_TERM = 23;

	/**
	 * The feature id for the '<em><b>Redelivery Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BALLAST_BONUS_TERM__REDELIVERY_PORTS = 0;

	/**
	 * The number of structural features of the '<em>Ballast Bonus Term</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BALLAST_BONUS_TERM_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.LumpSumBallastBonusTermImpl <em>Lump Sum Ballast Bonus Term</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.LumpSumBallastBonusTermImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getLumpSumBallastBonusTerm()
	 * @generated
	 */
	int LUMP_SUM_BALLAST_BONUS_TERM = 24;

	/**
	 * The feature id for the '<em><b>Redelivery Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LUMP_SUM_BALLAST_BONUS_TERM__REDELIVERY_PORTS = BALLAST_BONUS_TERM__REDELIVERY_PORTS;

	/**
	 * The feature id for the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LUMP_SUM_BALLAST_BONUS_TERM__PRICE_EXPRESSION = BALLAST_BONUS_TERM_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Lump Sum Ballast Bonus Term</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LUMP_SUM_BALLAST_BONUS_TERM_FEATURE_COUNT = BALLAST_BONUS_TERM_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.NotionalJourneyBallastBonusTermImpl <em>Notional Journey Ballast Bonus Term</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.NotionalJourneyBallastBonusTermImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getNotionalJourneyBallastBonusTerm()
	 * @generated
	 */
	int NOTIONAL_JOURNEY_BALLAST_BONUS_TERM = 25;

	/**
	 * The feature id for the '<em><b>Redelivery Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__REDELIVERY_PORTS = BALLAST_BONUS_TERM__REDELIVERY_PORTS;

	/**
	 * The feature id for the '<em><b>Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__SPEED = BALLAST_BONUS_TERM_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Fuel Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__FUEL_PRICE_EXPRESSION = BALLAST_BONUS_TERM_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Hire Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__HIRE_PRICE_EXPRESSION = BALLAST_BONUS_TERM_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Include Canal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__INCLUDE_CANAL = BALLAST_BONUS_TERM_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Include Canal Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__INCLUDE_CANAL_TIME = BALLAST_BONUS_TERM_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Lump Sum Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__LUMP_SUM_PRICE_EXPRESSION = BALLAST_BONUS_TERM_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Return Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__RETURN_PORTS = BALLAST_BONUS_TERM_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Notional Journey Ballast Bonus Term</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTIONAL_JOURNEY_BALLAST_BONUS_TERM_FEATURE_COUNT = BALLAST_BONUS_TERM_FEATURE_COUNT + 7;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.MonthlyBallastBonusTermImpl <em>Monthly Ballast Bonus Term</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.MonthlyBallastBonusTermImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getMonthlyBallastBonusTerm()
	 * @generated
	 */
	int MONTHLY_BALLAST_BONUS_TERM = 26;

	/**
	 * The feature id for the '<em><b>Redelivery Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONTHLY_BALLAST_BONUS_TERM__REDELIVERY_PORTS = NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__REDELIVERY_PORTS;

	/**
	 * The feature id for the '<em><b>Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONTHLY_BALLAST_BONUS_TERM__SPEED = NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__SPEED;

	/**
	 * The feature id for the '<em><b>Fuel Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONTHLY_BALLAST_BONUS_TERM__FUEL_PRICE_EXPRESSION = NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__FUEL_PRICE_EXPRESSION;

	/**
	 * The feature id for the '<em><b>Hire Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONTHLY_BALLAST_BONUS_TERM__HIRE_PRICE_EXPRESSION = NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__HIRE_PRICE_EXPRESSION;

	/**
	 * The feature id for the '<em><b>Include Canal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONTHLY_BALLAST_BONUS_TERM__INCLUDE_CANAL = NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__INCLUDE_CANAL;

	/**
	 * The feature id for the '<em><b>Include Canal Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONTHLY_BALLAST_BONUS_TERM__INCLUDE_CANAL_TIME = NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__INCLUDE_CANAL_TIME;

	/**
	 * The feature id for the '<em><b>Lump Sum Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONTHLY_BALLAST_BONUS_TERM__LUMP_SUM_PRICE_EXPRESSION = NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__LUMP_SUM_PRICE_EXPRESSION;

	/**
	 * The feature id for the '<em><b>Return Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONTHLY_BALLAST_BONUS_TERM__RETURN_PORTS = NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__RETURN_PORTS;

	/**
	 * The feature id for the '<em><b>Month</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONTHLY_BALLAST_BONUS_TERM__MONTH = NOTIONAL_JOURNEY_BALLAST_BONUS_TERM_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Ballast Bonus To</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONTHLY_BALLAST_BONUS_TERM__BALLAST_BONUS_TO = NOTIONAL_JOURNEY_BALLAST_BONUS_TERM_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Ballast Bonus Pct Fuel</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONTHLY_BALLAST_BONUS_TERM__BALLAST_BONUS_PCT_FUEL = NOTIONAL_JOURNEY_BALLAST_BONUS_TERM_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Ballast Bonus Pct Charter</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONTHLY_BALLAST_BONUS_TERM__BALLAST_BONUS_PCT_CHARTER = NOTIONAL_JOURNEY_BALLAST_BONUS_TERM_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Monthly Ballast Bonus Term</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONTHLY_BALLAST_BONUS_TERM_FEATURE_COUNT = NOTIONAL_JOURNEY_BALLAST_BONUS_TERM_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.RepositioningFeeTermImpl <em>Repositioning Fee Term</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.RepositioningFeeTermImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getRepositioningFeeTerm()
	 * @generated
	 */
	int REPOSITIONING_FEE_TERM = 27;

	/**
	 * The feature id for the '<em><b>Origin Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REPOSITIONING_FEE_TERM__ORIGIN_PORT = 0;

	/**
	 * The number of structural features of the '<em>Repositioning Fee Term</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REPOSITIONING_FEE_TERM_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.LumpSumRepositioningFeeTermImpl <em>Lump Sum Repositioning Fee Term</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.LumpSumRepositioningFeeTermImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getLumpSumRepositioningFeeTerm()
	 * @generated
	 */
	int LUMP_SUM_REPOSITIONING_FEE_TERM = 28;

	/**
	 * The feature id for the '<em><b>Origin Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LUMP_SUM_REPOSITIONING_FEE_TERM__ORIGIN_PORT = REPOSITIONING_FEE_TERM__ORIGIN_PORT;

	/**
	 * The feature id for the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LUMP_SUM_REPOSITIONING_FEE_TERM__PRICE_EXPRESSION = REPOSITIONING_FEE_TERM_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Lump Sum Repositioning Fee Term</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LUMP_SUM_REPOSITIONING_FEE_TERM_FEATURE_COUNT = REPOSITIONING_FEE_TERM_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.OriginPortRepositioningFeeTermImpl <em>Origin Port Repositioning Fee Term</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.OriginPortRepositioningFeeTermImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getOriginPortRepositioningFeeTerm()
	 * @generated
	 */
	int ORIGIN_PORT_REPOSITIONING_FEE_TERM = 29;

	/**
	 * The feature id for the '<em><b>Origin Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORIGIN_PORT_REPOSITIONING_FEE_TERM__ORIGIN_PORT = REPOSITIONING_FEE_TERM__ORIGIN_PORT;

	/**
	 * The feature id for the '<em><b>Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORIGIN_PORT_REPOSITIONING_FEE_TERM__SPEED = REPOSITIONING_FEE_TERM_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Fuel Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORIGIN_PORT_REPOSITIONING_FEE_TERM__FUEL_PRICE_EXPRESSION = REPOSITIONING_FEE_TERM_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Hire Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORIGIN_PORT_REPOSITIONING_FEE_TERM__HIRE_PRICE_EXPRESSION = REPOSITIONING_FEE_TERM_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Include Canal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORIGIN_PORT_REPOSITIONING_FEE_TERM__INCLUDE_CANAL = REPOSITIONING_FEE_TERM_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Include Canal Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORIGIN_PORT_REPOSITIONING_FEE_TERM__INCLUDE_CANAL_TIME = REPOSITIONING_FEE_TERM_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Lump Sum Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORIGIN_PORT_REPOSITIONING_FEE_TERM__LUMP_SUM_PRICE_EXPRESSION = REPOSITIONING_FEE_TERM_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Origin Port Repositioning Fee Term</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORIGIN_PORT_REPOSITIONING_FEE_TERM_FEATURE_COUNT = REPOSITIONING_FEE_TERM_FEATURE_COUNT + 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.ContractType <em>Contract Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.ContractType
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getContractType()
	 * @generated
	 */
	int CONTRACT_TYPE = 30;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.PricingEvent <em>Pricing Event</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.PricingEvent
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getPricingEvent()
	 * @generated
	 */
	int PRICING_EVENT = 31;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.NextPortType <em>Next Port Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.NextPortType
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getNextPortType()
	 * @generated
	 */
	int NEXT_PORT_TYPE = 32;

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
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.commercial.CommercialModel#getCharteringContracts <em>Chartering Contracts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Chartering Contracts</em>'.
	 * @see com.mmxlabs.models.lng.commercial.CommercialModel#getCharteringContracts()
	 * @see #getCommercialModel()
	 * @generated
	 */
	EReference getCommercialModel_CharteringContracts();

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
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.commercial.BaseLegalEntity#getUpstreamBook <em>Upstream Book</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Upstream Book</em>'.
	 * @see com.mmxlabs.models.lng.commercial.BaseLegalEntity#getUpstreamBook()
	 * @see #getBaseLegalEntity()
	 * @generated
	 */
	EReference getBaseLegalEntity_UpstreamBook();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.BaseLegalEntity#isThirdParty <em>Third Party</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Third Party</em>'.
	 * @see com.mmxlabs.models.lng.commercial.BaseLegalEntity#isThirdParty()
	 * @see #getBaseLegalEntity()
	 * @generated
	 */
	EAttribute getBaseLegalEntity_ThirdParty();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.Contract#getOperationalTolerance <em>Operational Tolerance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Operational Tolerance</em>'.
	 * @see com.mmxlabs.models.lng.commercial.Contract#getOperationalTolerance()
	 * @see #getContract()
	 * @generated
	 */
	EAttribute getContract_OperationalTolerance();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.Contract#isFullCargoLot <em>Full Cargo Lot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Full Cargo Lot</em>'.
	 * @see com.mmxlabs.models.lng.commercial.Contract#isFullCargoLot()
	 * @see #getContract()
	 * @generated
	 */
	EAttribute getContract_FullCargoLot();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.Contract#isRestrictedContractsArePermissive <em>Restricted Contracts Are Permissive</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Restricted Contracts Are Permissive</em>'.
	 * @see com.mmxlabs.models.lng.commercial.Contract#isRestrictedContractsArePermissive()
	 * @see #getContract()
	 * @generated
	 */
	EAttribute getContract_RestrictedContractsArePermissive();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.Contract#isRestrictedPortsArePermissive <em>Restricted Ports Are Permissive</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Restricted Ports Are Permissive</em>'.
	 * @see com.mmxlabs.models.lng.commercial.Contract#isRestrictedPortsArePermissive()
	 * @see #getContract()
	 * @generated
	 */
	EAttribute getContract_RestrictedPortsArePermissive();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.commercial.Contract#getRestrictedVessels <em>Restricted Vessels</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Restricted Vessels</em>'.
	 * @see com.mmxlabs.models.lng.commercial.Contract#getRestrictedVessels()
	 * @see #getContract()
	 * @generated
	 */
	EReference getContract_RestrictedVessels();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.Contract#isRestrictedVesselsArePermissive <em>Restricted Vessels Are Permissive</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Restricted Vessels Are Permissive</em>'.
	 * @see com.mmxlabs.models.lng.commercial.Contract#isRestrictedVesselsArePermissive()
	 * @see #getContract()
	 * @generated
	 */
	EAttribute getContract_RestrictedVesselsArePermissive();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.Contract#getCancellationExpression <em>Cancellation Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cancellation Expression</em>'.
	 * @see com.mmxlabs.models.lng.commercial.Contract#getCancellationExpression()
	 * @see #getContract()
	 * @generated
	 */
	EAttribute getContract_CancellationExpression();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.Contract#getShippingDaysRestriction <em>Shipping Days Restriction</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Shipping Days Restriction</em>'.
	 * @see com.mmxlabs.models.lng.commercial.Contract#getShippingDaysRestriction()
	 * @see #getContract()
	 * @generated
	 */
	EAttribute getContract_ShippingDaysRestriction();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.Contract#getContractYearStart <em>Contract Year Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Contract Year Start</em>'.
	 * @see com.mmxlabs.models.lng.commercial.Contract#getContractYearStart()
	 * @see #getContract()
	 * @generated
	 */
	EAttribute getContract_ContractYearStart();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.Contract#getCode <em>Code</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Code</em>'.
	 * @see com.mmxlabs.models.lng.commercial.Contract#getCode()
	 * @see #getContract()
	 * @generated
	 */
	EAttribute getContract_Code();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.Contract#getCounterparty <em>Counterparty</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Counterparty</em>'.
	 * @see com.mmxlabs.models.lng.commercial.Contract#getCounterparty()
	 * @see #getContract()
	 * @generated
	 */
	EAttribute getContract_Counterparty();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.Contract#getCn <em>Cn</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cn</em>'.
	 * @see com.mmxlabs.models.lng.commercial.Contract#getCn()
	 * @see #getContract()
	 * @generated
	 */
	EAttribute getContract_Cn();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.Contract#getStartDate <em>Start Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start Date</em>'.
	 * @see com.mmxlabs.models.lng.commercial.Contract#getStartDate()
	 * @see #getContract()
	 * @generated
	 */
	EAttribute getContract_StartDate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.Contract#getEndDate <em>End Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End Date</em>'.
	 * @see com.mmxlabs.models.lng.commercial.Contract#getEndDate()
	 * @see #getContract()
	 * @generated
	 */
	EAttribute getContract_EndDate();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.SalesContract#getFobSaleDealType <em>Fob Sale Deal Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fob Sale Deal Type</em>'.
	 * @see com.mmxlabs.models.lng.commercial.SalesContract#getFobSaleDealType()
	 * @see #getSalesContract()
	 * @generated
	 */
	EAttribute getSalesContract_FobSaleDealType();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.PurchaseContract#getDesPurchaseDealType <em>Des Purchase Deal Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Des Purchase Deal Type</em>'.
	 * @see com.mmxlabs.models.lng.commercial.PurchaseContract#getDesPurchaseDealType()
	 * @see #getPurchaseContract()
	 * @generated
	 */
	EAttribute getPurchaseContract_DesPurchaseDealType();

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters <em>Date Shift Expression Price Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Date Shift Expression Price Parameters</em>'.
	 * @see com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters
	 * @generated
	 */
	EClass getDateShiftExpressionPriceParameters();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters#getPriceExpression <em>Price Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price Expression</em>'.
	 * @see com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters#getPriceExpression()
	 * @see #getDateShiftExpressionPriceParameters()
	 * @generated
	 */
	EAttribute getDateShiftExpressionPriceParameters_PriceExpression();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters#isSpecificDay <em>Specific Day</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Specific Day</em>'.
	 * @see com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters#isSpecificDay()
	 * @see #getDateShiftExpressionPriceParameters()
	 * @generated
	 */
	EAttribute getDateShiftExpressionPriceParameters_SpecificDay();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters#getValue()
	 * @see #getDateShiftExpressionPriceParameters()
	 * @generated
	 */
	EAttribute getDateShiftExpressionPriceParameters_Value();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.GenericCharterContract <em>Generic Charter Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Generic Charter Contract</em>'.
	 * @see com.mmxlabs.models.lng.commercial.GenericCharterContract
	 * @generated
	 */
	EClass getGenericCharterContract();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.GenericCharterContract#getMinDuration <em>Min Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Duration</em>'.
	 * @see com.mmxlabs.models.lng.commercial.GenericCharterContract#getMinDuration()
	 * @see #getGenericCharterContract()
	 * @generated
	 */
	EAttribute getGenericCharterContract_MinDuration();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.GenericCharterContract#getMaxDuration <em>Max Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Duration</em>'.
	 * @see com.mmxlabs.models.lng.commercial.GenericCharterContract#getMaxDuration()
	 * @see #getGenericCharterContract()
	 * @generated
	 */
	EAttribute getGenericCharterContract_MaxDuration();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.commercial.GenericCharterContract#getRepositioningFeeTerms <em>Repositioning Fee Terms</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Repositioning Fee Terms</em>'.
	 * @see com.mmxlabs.models.lng.commercial.GenericCharterContract#getRepositioningFeeTerms()
	 * @see #getGenericCharterContract()
	 * @generated
	 */
	EReference getGenericCharterContract_RepositioningFeeTerms();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.commercial.GenericCharterContract#getBallastBonusTerms <em>Ballast Bonus Terms</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Ballast Bonus Terms</em>'.
	 * @see com.mmxlabs.models.lng.commercial.GenericCharterContract#getBallastBonusTerms()
	 * @see #getGenericCharterContract()
	 * @generated
	 */
	EReference getGenericCharterContract_BallastBonusTerms();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.IRepositioningFee <em>IRepositioning Fee</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IRepositioning Fee</em>'.
	 * @see com.mmxlabs.models.lng.commercial.IRepositioningFee
	 * @generated
	 */
	EClass getIRepositioningFee();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.SimpleRepositioningFeeContainer <em>Simple Repositioning Fee Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Simple Repositioning Fee Container</em>'.
	 * @see com.mmxlabs.models.lng.commercial.SimpleRepositioningFeeContainer
	 * @generated
	 */
	EClass getSimpleRepositioningFeeContainer();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.commercial.SimpleRepositioningFeeContainer#getTerms <em>Terms</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Terms</em>'.
	 * @see com.mmxlabs.models.lng.commercial.SimpleRepositioningFeeContainer#getTerms()
	 * @see #getSimpleRepositioningFeeContainer()
	 * @generated
	 */
	EReference getSimpleRepositioningFeeContainer_Terms();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.IBallastBonus <em>IBallast Bonus</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IBallast Bonus</em>'.
	 * @see com.mmxlabs.models.lng.commercial.IBallastBonus
	 * @generated
	 */
	EClass getIBallastBonus();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.SimpleBallastBonusContainer <em>Simple Ballast Bonus Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Simple Ballast Bonus Container</em>'.
	 * @see com.mmxlabs.models.lng.commercial.SimpleBallastBonusContainer
	 * @generated
	 */
	EClass getSimpleBallastBonusContainer();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.commercial.SimpleBallastBonusContainer#getTerms <em>Terms</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Terms</em>'.
	 * @see com.mmxlabs.models.lng.commercial.SimpleBallastBonusContainer#getTerms()
	 * @see #getSimpleBallastBonusContainer()
	 * @generated
	 */
	EReference getSimpleBallastBonusContainer_Terms();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContainer <em>Monthly Ballast Bonus Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Monthly Ballast Bonus Container</em>'.
	 * @see com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContainer
	 * @generated
	 */
	EClass getMonthlyBallastBonusContainer();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContainer#getHubs <em>Hubs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Hubs</em>'.
	 * @see com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContainer#getHubs()
	 * @see #getMonthlyBallastBonusContainer()
	 * @generated
	 */
	EReference getMonthlyBallastBonusContainer_Hubs();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContainer#getTerms <em>Terms</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Terms</em>'.
	 * @see com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContainer#getTerms()
	 * @see #getMonthlyBallastBonusContainer()
	 * @generated
	 */
	EReference getMonthlyBallastBonusContainer_Terms();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.LumpSumTerm <em>Lump Sum Term</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Lump Sum Term</em>'.
	 * @see com.mmxlabs.models.lng.commercial.LumpSumTerm
	 * @generated
	 */
	EClass getLumpSumTerm();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.LumpSumTerm#getPriceExpression <em>Price Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price Expression</em>'.
	 * @see com.mmxlabs.models.lng.commercial.LumpSumTerm#getPriceExpression()
	 * @see #getLumpSumTerm()
	 * @generated
	 */
	EAttribute getLumpSumTerm_PriceExpression();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.NotionalJourneyTerm <em>Notional Journey Term</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Notional Journey Term</em>'.
	 * @see com.mmxlabs.models.lng.commercial.NotionalJourneyTerm
	 * @generated
	 */
	EClass getNotionalJourneyTerm();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.NotionalJourneyTerm#getSpeed <em>Speed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Speed</em>'.
	 * @see com.mmxlabs.models.lng.commercial.NotionalJourneyTerm#getSpeed()
	 * @see #getNotionalJourneyTerm()
	 * @generated
	 */
	EAttribute getNotionalJourneyTerm_Speed();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.NotionalJourneyTerm#getFuelPriceExpression <em>Fuel Price Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fuel Price Expression</em>'.
	 * @see com.mmxlabs.models.lng.commercial.NotionalJourneyTerm#getFuelPriceExpression()
	 * @see #getNotionalJourneyTerm()
	 * @generated
	 */
	EAttribute getNotionalJourneyTerm_FuelPriceExpression();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.NotionalJourneyTerm#getHirePriceExpression <em>Hire Price Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Hire Price Expression</em>'.
	 * @see com.mmxlabs.models.lng.commercial.NotionalJourneyTerm#getHirePriceExpression()
	 * @see #getNotionalJourneyTerm()
	 * @generated
	 */
	EAttribute getNotionalJourneyTerm_HirePriceExpression();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.NotionalJourneyTerm#isIncludeCanal <em>Include Canal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Include Canal</em>'.
	 * @see com.mmxlabs.models.lng.commercial.NotionalJourneyTerm#isIncludeCanal()
	 * @see #getNotionalJourneyTerm()
	 * @generated
	 */
	EAttribute getNotionalJourneyTerm_IncludeCanal();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.NotionalJourneyTerm#isIncludeCanalTime <em>Include Canal Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Include Canal Time</em>'.
	 * @see com.mmxlabs.models.lng.commercial.NotionalJourneyTerm#isIncludeCanalTime()
	 * @see #getNotionalJourneyTerm()
	 * @generated
	 */
	EAttribute getNotionalJourneyTerm_IncludeCanalTime();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.NotionalJourneyTerm#getLumpSumPriceExpression <em>Lump Sum Price Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lump Sum Price Expression</em>'.
	 * @see com.mmxlabs.models.lng.commercial.NotionalJourneyTerm#getLumpSumPriceExpression()
	 * @see #getNotionalJourneyTerm()
	 * @generated
	 */
	EAttribute getNotionalJourneyTerm_LumpSumPriceExpression();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.BallastBonusTerm <em>Ballast Bonus Term</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Ballast Bonus Term</em>'.
	 * @see com.mmxlabs.models.lng.commercial.BallastBonusTerm
	 * @generated
	 */
	EClass getBallastBonusTerm();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.commercial.BallastBonusTerm#getRedeliveryPorts <em>Redelivery Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Redelivery Ports</em>'.
	 * @see com.mmxlabs.models.lng.commercial.BallastBonusTerm#getRedeliveryPorts()
	 * @see #getBallastBonusTerm()
	 * @generated
	 */
	EReference getBallastBonusTerm_RedeliveryPorts();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.LumpSumBallastBonusTerm <em>Lump Sum Ballast Bonus Term</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Lump Sum Ballast Bonus Term</em>'.
	 * @see com.mmxlabs.models.lng.commercial.LumpSumBallastBonusTerm
	 * @generated
	 */
	EClass getLumpSumBallastBonusTerm();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusTerm <em>Notional Journey Ballast Bonus Term</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Notional Journey Ballast Bonus Term</em>'.
	 * @see com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusTerm
	 * @generated
	 */
	EClass getNotionalJourneyBallastBonusTerm();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusTerm#getReturnPorts <em>Return Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Return Ports</em>'.
	 * @see com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusTerm#getReturnPorts()
	 * @see #getNotionalJourneyBallastBonusTerm()
	 * @generated
	 */
	EReference getNotionalJourneyBallastBonusTerm_ReturnPorts();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusTerm <em>Monthly Ballast Bonus Term</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Monthly Ballast Bonus Term</em>'.
	 * @see com.mmxlabs.models.lng.commercial.MonthlyBallastBonusTerm
	 * @generated
	 */
	EClass getMonthlyBallastBonusTerm();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusTerm#getMonth <em>Month</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Month</em>'.
	 * @see com.mmxlabs.models.lng.commercial.MonthlyBallastBonusTerm#getMonth()
	 * @see #getMonthlyBallastBonusTerm()
	 * @generated
	 */
	EAttribute getMonthlyBallastBonusTerm_Month();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusTerm#getBallastBonusTo <em>Ballast Bonus To</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ballast Bonus To</em>'.
	 * @see com.mmxlabs.models.lng.commercial.MonthlyBallastBonusTerm#getBallastBonusTo()
	 * @see #getMonthlyBallastBonusTerm()
	 * @generated
	 */
	EAttribute getMonthlyBallastBonusTerm_BallastBonusTo();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusTerm#getBallastBonusPctFuel <em>Ballast Bonus Pct Fuel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ballast Bonus Pct Fuel</em>'.
	 * @see com.mmxlabs.models.lng.commercial.MonthlyBallastBonusTerm#getBallastBonusPctFuel()
	 * @see #getMonthlyBallastBonusTerm()
	 * @generated
	 */
	EAttribute getMonthlyBallastBonusTerm_BallastBonusPctFuel();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusTerm#getBallastBonusPctCharter <em>Ballast Bonus Pct Charter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ballast Bonus Pct Charter</em>'.
	 * @see com.mmxlabs.models.lng.commercial.MonthlyBallastBonusTerm#getBallastBonusPctCharter()
	 * @see #getMonthlyBallastBonusTerm()
	 * @generated
	 */
	EAttribute getMonthlyBallastBonusTerm_BallastBonusPctCharter();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.RepositioningFeeTerm <em>Repositioning Fee Term</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Repositioning Fee Term</em>'.
	 * @see com.mmxlabs.models.lng.commercial.RepositioningFeeTerm
	 * @generated
	 */
	EClass getRepositioningFeeTerm();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.commercial.RepositioningFeeTerm#getOriginPort <em>Origin Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Origin Port</em>'.
	 * @see com.mmxlabs.models.lng.commercial.RepositioningFeeTerm#getOriginPort()
	 * @see #getRepositioningFeeTerm()
	 * @generated
	 */
	EReference getRepositioningFeeTerm_OriginPort();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.LumpSumRepositioningFeeTerm <em>Lump Sum Repositioning Fee Term</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Lump Sum Repositioning Fee Term</em>'.
	 * @see com.mmxlabs.models.lng.commercial.LumpSumRepositioningFeeTerm
	 * @generated
	 */
	EClass getLumpSumRepositioningFeeTerm();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.OriginPortRepositioningFeeTerm <em>Origin Port Repositioning Fee Term</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Origin Port Repositioning Fee Term</em>'.
	 * @see com.mmxlabs.models.lng.commercial.OriginPortRepositioningFeeTerm
	 * @generated
	 */
	EClass getOriginPortRepositioningFeeTerm();

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
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.commercial.NextPortType <em>Next Port Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Next Port Type</em>'.
	 * @see com.mmxlabs.models.lng.commercial.NextPortType
	 * @generated
	 */
	EEnum getNextPortType();

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
		 * The meta object literal for the '<em><b>Chartering Contracts</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMMERCIAL_MODEL__CHARTERING_CONTRACTS = eINSTANCE.getCommercialModel_CharteringContracts();

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
		 * The meta object literal for the '<em><b>Upstream Book</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BASE_LEGAL_ENTITY__UPSTREAM_BOOK = eINSTANCE.getBaseLegalEntity_UpstreamBook();

		/**
		 * The meta object literal for the '<em><b>Third Party</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BASE_LEGAL_ENTITY__THIRD_PARTY = eINSTANCE.getBaseLegalEntity_ThirdParty();

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
		 * The meta object literal for the '<em><b>Operational Tolerance</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTRACT__OPERATIONAL_TOLERANCE = eINSTANCE.getContract_OperationalTolerance();

		/**
		 * The meta object literal for the '<em><b>Full Cargo Lot</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTRACT__FULL_CARGO_LOT = eINSTANCE.getContract_FullCargoLot();

		/**
		 * The meta object literal for the '<em><b>Volume Limits Unit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTRACT__VOLUME_LIMITS_UNIT = eINSTANCE.getContract_VolumeLimitsUnit();

		/**
		 * The meta object literal for the '<em><b>Restricted Contracts</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTRACT__RESTRICTED_CONTRACTS = eINSTANCE.getContract_RestrictedContracts();

		/**
		 * The meta object literal for the '<em><b>Restricted Contracts Are Permissive</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTRACT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE = eINSTANCE.getContract_RestrictedContractsArePermissive();

		/**
		 * The meta object literal for the '<em><b>Restricted Ports</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTRACT__RESTRICTED_PORTS = eINSTANCE.getContract_RestrictedPorts();

		/**
		 * The meta object literal for the '<em><b>Restricted Ports Are Permissive</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTRACT__RESTRICTED_PORTS_ARE_PERMISSIVE = eINSTANCE.getContract_RestrictedPortsArePermissive();

		/**
		 * The meta object literal for the '<em><b>Restricted Vessels</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTRACT__RESTRICTED_VESSELS = eINSTANCE.getContract_RestrictedVessels();

		/**
		 * The meta object literal for the '<em><b>Restricted Vessels Are Permissive</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTRACT__RESTRICTED_VESSELS_ARE_PERMISSIVE = eINSTANCE.getContract_RestrictedVesselsArePermissive();

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
		 * The meta object literal for the '<em><b>Cancellation Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTRACT__CANCELLATION_EXPRESSION = eINSTANCE.getContract_CancellationExpression();

		/**
		 * The meta object literal for the '<em><b>Shipping Days Restriction</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTRACT__SHIPPING_DAYS_RESTRICTION = eINSTANCE.getContract_ShippingDaysRestriction();

		/**
		 * The meta object literal for the '<em><b>Contract Year Start</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTRACT__CONTRACT_YEAR_START = eINSTANCE.getContract_ContractYearStart();

		/**
		 * The meta object literal for the '<em><b>Code</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTRACT__CODE = eINSTANCE.getContract_Code();

		/**
		 * The meta object literal for the '<em><b>Counterparty</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTRACT__COUNTERPARTY = eINSTANCE.getContract_Counterparty();

		/**
		 * The meta object literal for the '<em><b>Cn</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTRACT__CN = eINSTANCE.getContract_Cn();

		/**
		 * The meta object literal for the '<em><b>Start Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTRACT__START_DATE = eINSTANCE.getContract_StartDate();

		/**
		 * The meta object literal for the '<em><b>End Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTRACT__END_DATE = eINSTANCE.getContract_EndDate();

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
		 * The meta object literal for the '<em><b>Fob Sale Deal Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SALES_CONTRACT__FOB_SALE_DEAL_TYPE = eINSTANCE.getSalesContract_FobSaleDealType();

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
		 * The meta object literal for the '<em><b>Des Purchase Deal Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PURCHASE_CONTRACT__DES_PURCHASE_DEAL_TYPE = eINSTANCE.getPurchaseContract_DesPurchaseDealType();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.DateShiftExpressionPriceParametersImpl <em>Date Shift Expression Price Parameters</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.DateShiftExpressionPriceParametersImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getDateShiftExpressionPriceParameters()
		 * @generated
		 */
		EClass DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS = eINSTANCE.getDateShiftExpressionPriceParameters();

		/**
		 * The meta object literal for the '<em><b>Price Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__PRICE_EXPRESSION = eINSTANCE.getDateShiftExpressionPriceParameters_PriceExpression();

		/**
		 * The meta object literal for the '<em><b>Specific Day</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__SPECIFIC_DAY = eINSTANCE.getDateShiftExpressionPriceParameters_SpecificDay();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DATE_SHIFT_EXPRESSION_PRICE_PARAMETERS__VALUE = eINSTANCE.getDateShiftExpressionPriceParameters_Value();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.GenericCharterContractImpl <em>Generic Charter Contract</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.GenericCharterContractImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getGenericCharterContract()
		 * @generated
		 */
		EClass GENERIC_CHARTER_CONTRACT = eINSTANCE.getGenericCharterContract();

		/**
		 * The meta object literal for the '<em><b>Min Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GENERIC_CHARTER_CONTRACT__MIN_DURATION = eINSTANCE.getGenericCharterContract_MinDuration();

		/**
		 * The meta object literal for the '<em><b>Max Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GENERIC_CHARTER_CONTRACT__MAX_DURATION = eINSTANCE.getGenericCharterContract_MaxDuration();

		/**
		 * The meta object literal for the '<em><b>Repositioning Fee Terms</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GENERIC_CHARTER_CONTRACT__REPOSITIONING_FEE_TERMS = eINSTANCE.getGenericCharterContract_RepositioningFeeTerms();

		/**
		 * The meta object literal for the '<em><b>Ballast Bonus Terms</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GENERIC_CHARTER_CONTRACT__BALLAST_BONUS_TERMS = eINSTANCE.getGenericCharterContract_BallastBonusTerms();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.IRepositioningFee <em>IRepositioning Fee</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.IRepositioningFee
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getIRepositioningFee()
		 * @generated
		 */
		EClass IREPOSITIONING_FEE = eINSTANCE.getIRepositioningFee();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.SimpleRepositioningFeeContainerImpl <em>Simple Repositioning Fee Container</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.SimpleRepositioningFeeContainerImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getSimpleRepositioningFeeContainer()
		 * @generated
		 */
		EClass SIMPLE_REPOSITIONING_FEE_CONTAINER = eINSTANCE.getSimpleRepositioningFeeContainer();

		/**
		 * The meta object literal for the '<em><b>Terms</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SIMPLE_REPOSITIONING_FEE_CONTAINER__TERMS = eINSTANCE.getSimpleRepositioningFeeContainer_Terms();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.IBallastBonus <em>IBallast Bonus</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.IBallastBonus
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getIBallastBonus()
		 * @generated
		 */
		EClass IBALLAST_BONUS = eINSTANCE.getIBallastBonus();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.SimpleBallastBonusContainerImpl <em>Simple Ballast Bonus Container</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.SimpleBallastBonusContainerImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getSimpleBallastBonusContainer()
		 * @generated
		 */
		EClass SIMPLE_BALLAST_BONUS_CONTAINER = eINSTANCE.getSimpleBallastBonusContainer();

		/**
		 * The meta object literal for the '<em><b>Terms</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SIMPLE_BALLAST_BONUS_CONTAINER__TERMS = eINSTANCE.getSimpleBallastBonusContainer_Terms();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.MonthlyBallastBonusContainerImpl <em>Monthly Ballast Bonus Container</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.MonthlyBallastBonusContainerImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getMonthlyBallastBonusContainer()
		 * @generated
		 */
		EClass MONTHLY_BALLAST_BONUS_CONTAINER = eINSTANCE.getMonthlyBallastBonusContainer();

		/**
		 * The meta object literal for the '<em><b>Hubs</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MONTHLY_BALLAST_BONUS_CONTAINER__HUBS = eINSTANCE.getMonthlyBallastBonusContainer_Hubs();

		/**
		 * The meta object literal for the '<em><b>Terms</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MONTHLY_BALLAST_BONUS_CONTAINER__TERMS = eINSTANCE.getMonthlyBallastBonusContainer_Terms();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.LumpSumTermImpl <em>Lump Sum Term</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.LumpSumTermImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getLumpSumTerm()
		 * @generated
		 */
		EClass LUMP_SUM_TERM = eINSTANCE.getLumpSumTerm();

		/**
		 * The meta object literal for the '<em><b>Price Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LUMP_SUM_TERM__PRICE_EXPRESSION = eINSTANCE.getLumpSumTerm_PriceExpression();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.NotionalJourneyTermImpl <em>Notional Journey Term</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.NotionalJourneyTermImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getNotionalJourneyTerm()
		 * @generated
		 */
		EClass NOTIONAL_JOURNEY_TERM = eINSTANCE.getNotionalJourneyTerm();

		/**
		 * The meta object literal for the '<em><b>Speed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NOTIONAL_JOURNEY_TERM__SPEED = eINSTANCE.getNotionalJourneyTerm_Speed();

		/**
		 * The meta object literal for the '<em><b>Fuel Price Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NOTIONAL_JOURNEY_TERM__FUEL_PRICE_EXPRESSION = eINSTANCE.getNotionalJourneyTerm_FuelPriceExpression();

		/**
		 * The meta object literal for the '<em><b>Hire Price Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NOTIONAL_JOURNEY_TERM__HIRE_PRICE_EXPRESSION = eINSTANCE.getNotionalJourneyTerm_HirePriceExpression();

		/**
		 * The meta object literal for the '<em><b>Include Canal</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NOTIONAL_JOURNEY_TERM__INCLUDE_CANAL = eINSTANCE.getNotionalJourneyTerm_IncludeCanal();

		/**
		 * The meta object literal for the '<em><b>Include Canal Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NOTIONAL_JOURNEY_TERM__INCLUDE_CANAL_TIME = eINSTANCE.getNotionalJourneyTerm_IncludeCanalTime();

		/**
		 * The meta object literal for the '<em><b>Lump Sum Price Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NOTIONAL_JOURNEY_TERM__LUMP_SUM_PRICE_EXPRESSION = eINSTANCE.getNotionalJourneyTerm_LumpSumPriceExpression();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.BallastBonusTermImpl <em>Ballast Bonus Term</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.BallastBonusTermImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getBallastBonusTerm()
		 * @generated
		 */
		EClass BALLAST_BONUS_TERM = eINSTANCE.getBallastBonusTerm();

		/**
		 * The meta object literal for the '<em><b>Redelivery Ports</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BALLAST_BONUS_TERM__REDELIVERY_PORTS = eINSTANCE.getBallastBonusTerm_RedeliveryPorts();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.LumpSumBallastBonusTermImpl <em>Lump Sum Ballast Bonus Term</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.LumpSumBallastBonusTermImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getLumpSumBallastBonusTerm()
		 * @generated
		 */
		EClass LUMP_SUM_BALLAST_BONUS_TERM = eINSTANCE.getLumpSumBallastBonusTerm();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.NotionalJourneyBallastBonusTermImpl <em>Notional Journey Ballast Bonus Term</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.NotionalJourneyBallastBonusTermImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getNotionalJourneyBallastBonusTerm()
		 * @generated
		 */
		EClass NOTIONAL_JOURNEY_BALLAST_BONUS_TERM = eINSTANCE.getNotionalJourneyBallastBonusTerm();

		/**
		 * The meta object literal for the '<em><b>Return Ports</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__RETURN_PORTS = eINSTANCE.getNotionalJourneyBallastBonusTerm_ReturnPorts();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.MonthlyBallastBonusTermImpl <em>Monthly Ballast Bonus Term</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.MonthlyBallastBonusTermImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getMonthlyBallastBonusTerm()
		 * @generated
		 */
		EClass MONTHLY_BALLAST_BONUS_TERM = eINSTANCE.getMonthlyBallastBonusTerm();

		/**
		 * The meta object literal for the '<em><b>Month</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MONTHLY_BALLAST_BONUS_TERM__MONTH = eINSTANCE.getMonthlyBallastBonusTerm_Month();

		/**
		 * The meta object literal for the '<em><b>Ballast Bonus To</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MONTHLY_BALLAST_BONUS_TERM__BALLAST_BONUS_TO = eINSTANCE.getMonthlyBallastBonusTerm_BallastBonusTo();

		/**
		 * The meta object literal for the '<em><b>Ballast Bonus Pct Fuel</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MONTHLY_BALLAST_BONUS_TERM__BALLAST_BONUS_PCT_FUEL = eINSTANCE.getMonthlyBallastBonusTerm_BallastBonusPctFuel();

		/**
		 * The meta object literal for the '<em><b>Ballast Bonus Pct Charter</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MONTHLY_BALLAST_BONUS_TERM__BALLAST_BONUS_PCT_CHARTER = eINSTANCE.getMonthlyBallastBonusTerm_BallastBonusPctCharter();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.RepositioningFeeTermImpl <em>Repositioning Fee Term</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.RepositioningFeeTermImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getRepositioningFeeTerm()
		 * @generated
		 */
		EClass REPOSITIONING_FEE_TERM = eINSTANCE.getRepositioningFeeTerm();

		/**
		 * The meta object literal for the '<em><b>Origin Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REPOSITIONING_FEE_TERM__ORIGIN_PORT = eINSTANCE.getRepositioningFeeTerm_OriginPort();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.LumpSumRepositioningFeeTermImpl <em>Lump Sum Repositioning Fee Term</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.LumpSumRepositioningFeeTermImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getLumpSumRepositioningFeeTerm()
		 * @generated
		 */
		EClass LUMP_SUM_REPOSITIONING_FEE_TERM = eINSTANCE.getLumpSumRepositioningFeeTerm();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.OriginPortRepositioningFeeTermImpl <em>Origin Port Repositioning Fee Term</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.OriginPortRepositioningFeeTermImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getOriginPortRepositioningFeeTerm()
		 * @generated
		 */
		EClass ORIGIN_PORT_REPOSITIONING_FEE_TERM = eINSTANCE.getOriginPortRepositioningFeeTerm();

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

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.NextPortType <em>Next Port Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.NextPortType
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getNextPortType()
		 * @generated
		 */
		EEnum NEXT_PORT_TYPE = eINSTANCE.getNextPortType();

	}

} //CommercialPackage
