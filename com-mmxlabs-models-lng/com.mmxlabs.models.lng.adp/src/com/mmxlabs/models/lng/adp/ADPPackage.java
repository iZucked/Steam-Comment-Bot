/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp;

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
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.adp.ADPFactory
 * @model kind="package"
 * @generated
 */
public interface ADPPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "adp";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.mmxlabs.com/models/lng/adp/1/";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "lng.adp";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ADPPackage eINSTANCE = com.mmxlabs.models.lng.adp.impl.ADPPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.ADPModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.ADPModelImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getADPModel()
	 * @generated
	 */
	int ADP_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADP_MODEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADP_MODEL__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Year Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADP_MODEL__YEAR_START = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Year End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADP_MODEL__YEAR_END = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Purchase Contract Profiles</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADP_MODEL__PURCHASE_CONTRACT_PROFILES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Sales Contract Profiles</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADP_MODEL__SALES_CONTRACT_PROFILES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Fleet Profile</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADP_MODEL__FLEET_PROFILE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Mull Profile</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADP_MODEL__MULL_PROFILE = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADP_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADP_MODEL___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADP_MODEL___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADP_MODEL___ECONTAINER_OP = MMXCorePackage.UUID_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADP_MODEL_OPERATION_COUNT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.FleetProfileImpl <em>Fleet Profile</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.FleetProfileImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getFleetProfile()
	 * @generated
	 */
	int FLEET_PROFILE = 1;

	/**
	 * The feature id for the '<em><b>Constraints</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_PROFILE__CONSTRAINTS = 0;

	/**
	 * The feature id for the '<em><b>Default Nominal Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_PROFILE__DEFAULT_NOMINAL_MARKET = 1;

	/**
	 * The number of structural features of the '<em>Fleet Profile</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_PROFILE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Fleet Profile</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_PROFILE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.ContractProfileImpl <em>Contract Profile</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.ContractProfileImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getContractProfile()
	 * @generated
	 */
	int CONTRACT_PROFILE = 2;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_PROFILE__CONTRACT = 0;

	/**
	 * The feature id for the '<em><b>Contract Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_PROFILE__CONTRACT_CODE = 1;

	/**
	 * The feature id for the '<em><b>Custom</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_PROFILE__CUSTOM = 2;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_PROFILE__ENABLED = 3;

	/**
	 * The feature id for the '<em><b>Total Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_PROFILE__TOTAL_VOLUME = 4;

	/**
	 * The feature id for the '<em><b>Volume Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_PROFILE__VOLUME_UNIT = 5;

	/**
	 * The feature id for the '<em><b>Sub Profiles</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_PROFILE__SUB_PROFILES = 6;

	/**
	 * The feature id for the '<em><b>Constraints</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_PROFILE__CONSTRAINTS = 7;

	/**
	 * The number of structural features of the '<em>Contract Profile</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_PROFILE_FEATURE_COUNT = 8;

	/**
	 * The number of operations of the '<em>Contract Profile</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_PROFILE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.DistributionModelImpl <em>Distribution Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.DistributionModelImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getDistributionModel()
	 * @generated
	 */
	int DISTRIBUTION_MODEL = 7;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.CargoSizeDistributionModelImpl <em>Cargo Size Distribution Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.CargoSizeDistributionModelImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getCargoSizeDistributionModel()
	 * @generated
	 */
	int CARGO_SIZE_DISTRIBUTION_MODEL = 8;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.CargoNumberDistributionModelImpl <em>Cargo Number Distribution Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.CargoNumberDistributionModelImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getCargoNumberDistributionModel()
	 * @generated
	 */
	int CARGO_NUMBER_DISTRIBUTION_MODEL = 9;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.PurchaseContractProfileImpl <em>Purchase Contract Profile</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.PurchaseContractProfileImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getPurchaseContractProfile()
	 * @generated
	 */
	int PURCHASE_CONTRACT_PROFILE = 3;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT_PROFILE__CONTRACT = CONTRACT_PROFILE__CONTRACT;

	/**
	 * The feature id for the '<em><b>Contract Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT_PROFILE__CONTRACT_CODE = CONTRACT_PROFILE__CONTRACT_CODE;

	/**
	 * The feature id for the '<em><b>Custom</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT_PROFILE__CUSTOM = CONTRACT_PROFILE__CUSTOM;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT_PROFILE__ENABLED = CONTRACT_PROFILE__ENABLED;

	/**
	 * The feature id for the '<em><b>Total Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT_PROFILE__TOTAL_VOLUME = CONTRACT_PROFILE__TOTAL_VOLUME;

	/**
	 * The feature id for the '<em><b>Volume Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT_PROFILE__VOLUME_UNIT = CONTRACT_PROFILE__VOLUME_UNIT;

	/**
	 * The feature id for the '<em><b>Sub Profiles</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT_PROFILE__SUB_PROFILES = CONTRACT_PROFILE__SUB_PROFILES;

	/**
	 * The feature id for the '<em><b>Constraints</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT_PROFILE__CONSTRAINTS = CONTRACT_PROFILE__CONSTRAINTS;

	/**
	 * The number of structural features of the '<em>Purchase Contract Profile</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT_PROFILE_FEATURE_COUNT = CONTRACT_PROFILE_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Purchase Contract Profile</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT_PROFILE_OPERATION_COUNT = CONTRACT_PROFILE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.SalesContractProfileImpl <em>Sales Contract Profile</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.SalesContractProfileImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getSalesContractProfile()
	 * @generated
	 */
	int SALES_CONTRACT_PROFILE = 4;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT_PROFILE__CONTRACT = CONTRACT_PROFILE__CONTRACT;

	/**
	 * The feature id for the '<em><b>Contract Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT_PROFILE__CONTRACT_CODE = CONTRACT_PROFILE__CONTRACT_CODE;

	/**
	 * The feature id for the '<em><b>Custom</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT_PROFILE__CUSTOM = CONTRACT_PROFILE__CUSTOM;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT_PROFILE__ENABLED = CONTRACT_PROFILE__ENABLED;

	/**
	 * The feature id for the '<em><b>Total Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT_PROFILE__TOTAL_VOLUME = CONTRACT_PROFILE__TOTAL_VOLUME;

	/**
	 * The feature id for the '<em><b>Volume Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT_PROFILE__VOLUME_UNIT = CONTRACT_PROFILE__VOLUME_UNIT;

	/**
	 * The feature id for the '<em><b>Sub Profiles</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT_PROFILE__SUB_PROFILES = CONTRACT_PROFILE__SUB_PROFILES;

	/**
	 * The feature id for the '<em><b>Constraints</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT_PROFILE__CONSTRAINTS = CONTRACT_PROFILE__CONSTRAINTS;

	/**
	 * The number of structural features of the '<em>Sales Contract Profile</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT_PROFILE_FEATURE_COUNT = CONTRACT_PROFILE_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Sales Contract Profile</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT_PROFILE_OPERATION_COUNT = CONTRACT_PROFILE_OPERATION_COUNT + 0;


	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.SubContractProfileImpl <em>Sub Contract Profile</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.SubContractProfileImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getSubContractProfile()
	 * @generated
	 */
	int SUB_CONTRACT_PROFILE = 5;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUB_CONTRACT_PROFILE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Contract Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUB_CONTRACT_PROFILE__CONTRACT_TYPE = 1;

	/**
	 * The feature id for the '<em><b>Distribution Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL = 2;

	/**
	 * The feature id for the '<em><b>Slot Template Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUB_CONTRACT_PROFILE__SLOT_TEMPLATE_ID = 3;

	/**
	 * The feature id for the '<em><b>Nominated Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUB_CONTRACT_PROFILE__NOMINATED_VESSEL = 4;

	/**
	 * The feature id for the '<em><b>Shipping Days</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUB_CONTRACT_PROFILE__SHIPPING_DAYS = 5;

	/**
	 * The feature id for the '<em><b>Custom Attribs</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUB_CONTRACT_PROFILE__CUSTOM_ATTRIBS = 6;

	/**
	 * The feature id for the '<em><b>Constraints</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUB_CONTRACT_PROFILE__CONSTRAINTS = 7;

	/**
	 * The feature id for the '<em><b>Window Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUB_CONTRACT_PROFILE__WINDOW_SIZE = 8;

	/**
	 * The feature id for the '<em><b>Window Size Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUB_CONTRACT_PROFILE__WINDOW_SIZE_UNITS = 9;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUB_CONTRACT_PROFILE__PORT = 10;

	/**
	 * The number of structural features of the '<em>Sub Contract Profile</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUB_CONTRACT_PROFILE_FEATURE_COUNT = 11;

	/**
	 * The number of operations of the '<em>Sub Contract Profile</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUB_CONTRACT_PROFILE_OPERATION_COUNT = 0;


	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.CustomSubProfileAttributes <em>Custom Sub Profile Attributes</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.CustomSubProfileAttributes
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getCustomSubProfileAttributes()
	 * @generated
	 */
	int CUSTOM_SUB_PROFILE_ATTRIBUTES = 6;

	/**
	 * The number of structural features of the '<em>Custom Sub Profile Attributes</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CUSTOM_SUB_PROFILE_ATTRIBUTES_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Custom Sub Profile Attributes</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CUSTOM_SUB_PROFILE_ATTRIBUTES_OPERATION_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISTRIBUTION_MODEL__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Volume Per Cargo</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISTRIBUTION_MODEL__VOLUME_PER_CARGO = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Volume Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISTRIBUTION_MODEL__VOLUME_UNIT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Distribution Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISTRIBUTION_MODEL_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISTRIBUTION_MODEL___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.MMX_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISTRIBUTION_MODEL___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.MMX_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISTRIBUTION_MODEL___ECONTAINER_OP = MMXCorePackage.MMX_OBJECT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Model Or Contract Volume Per Cargo</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISTRIBUTION_MODEL___GET_MODEL_OR_CONTRACT_VOLUME_PER_CARGO = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Model Or Contract Volume Unit</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISTRIBUTION_MODEL___GET_MODEL_OR_CONTRACT_VOLUME_UNIT = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 1;

	/**
	 * The number of operations of the '<em>Distribution Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISTRIBUTION_MODEL_OPERATION_COUNT = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_SIZE_DISTRIBUTION_MODEL__EXTENSIONS = DISTRIBUTION_MODEL__EXTENSIONS;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.PeriodDistributionImpl <em>Period Distribution</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.PeriodDistributionImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getPeriodDistribution()
	 * @generated
	 */
	int PERIOD_DISTRIBUTION = 28;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.CargoByQuarterDistributionModelImpl <em>Cargo By Quarter Distribution Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.CargoByQuarterDistributionModelImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getCargoByQuarterDistributionModel()
	 * @generated
	 */
	int CARGO_BY_QUARTER_DISTRIBUTION_MODEL = 10;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.CargoIntervalDistributionModelImpl <em>Cargo Interval Distribution Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.CargoIntervalDistributionModelImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getCargoIntervalDistributionModel()
	 * @generated
	 */
	int CARGO_INTERVAL_DISTRIBUTION_MODEL = 11;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.PreDefinedDistributionModelImpl <em>Pre Defined Distribution Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.PreDefinedDistributionModelImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getPreDefinedDistributionModel()
	 * @generated
	 */
	int PRE_DEFINED_DISTRIBUTION_MODEL = 12;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.PreDefinedDateImpl <em>Pre Defined Date</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.PreDefinedDateImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getPreDefinedDate()
	 * @generated
	 */
	int PRE_DEFINED_DATE = 13;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.SubProfileConstraintImpl <em>Sub Profile Constraint</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.SubProfileConstraintImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getSubProfileConstraint()
	 * @generated
	 */
	int SUB_PROFILE_CONSTRAINT = 24;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.FlowTypeImpl <em>Flow Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.FlowTypeImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getFlowType()
	 * @generated
	 */
	int FLOW_TYPE = 14;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.SupplyFromFlowImpl <em>Supply From Flow</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.SupplyFromFlowImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getSupplyFromFlow()
	 * @generated
	 */
	int SUPPLY_FROM_FLOW = 15;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.DeliverToFlowImpl <em>Deliver To Flow</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.DeliverToFlowImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getDeliverToFlow()
	 * @generated
	 */
	int DELIVER_TO_FLOW = 16;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.SupplyFromProfileFlowImpl <em>Supply From Profile Flow</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.SupplyFromProfileFlowImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getSupplyFromProfileFlow()
	 * @generated
	 */
	int SUPPLY_FROM_PROFILE_FLOW = 17;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.DeliverToProfileFlowImpl <em>Deliver To Profile Flow</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.DeliverToProfileFlowImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getDeliverToProfileFlow()
	 * @generated
	 */
	int DELIVER_TO_PROFILE_FLOW = 18;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.SupplyFromSpotFlowImpl <em>Supply From Spot Flow</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.SupplyFromSpotFlowImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getSupplyFromSpotFlow()
	 * @generated
	 */
	int SUPPLY_FROM_SPOT_FLOW = 19;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.DeliverToSpotFlowImpl <em>Deliver To Spot Flow</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.DeliverToSpotFlowImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getDeliverToSpotFlow()
	 * @generated
	 */
	int DELIVER_TO_SPOT_FLOW = 20;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.ProfileVesselRestrictionImpl <em>Profile Vessel Restriction</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.ProfileVesselRestrictionImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getProfileVesselRestriction()
	 * @generated
	 */
	int PROFILE_VESSEL_RESTRICTION = 21;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.ShippingOptionImpl <em>Shipping Option</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.ShippingOptionImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getShippingOption()
	 * @generated
	 */
	int SHIPPING_OPTION = 22;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.ProfileConstraintImpl <em>Profile Constraint</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.ProfileConstraintImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getProfileConstraint()
	 * @generated
	 */
	int PROFILE_CONSTRAINT = 23;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.PeriodDistributionProfileConstraintImpl <em>Period Distribution Profile Constraint</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.PeriodDistributionProfileConstraintImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getPeriodDistributionProfileConstraint()
	 * @generated
	 */
	int PERIOD_DISTRIBUTION_PROFILE_CONSTRAINT = 27;

	/**
	 * The feature id for the '<em><b>Volume Per Cargo</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_SIZE_DISTRIBUTION_MODEL__VOLUME_PER_CARGO = DISTRIBUTION_MODEL__VOLUME_PER_CARGO;

	/**
	 * The feature id for the '<em><b>Volume Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_SIZE_DISTRIBUTION_MODEL__VOLUME_UNIT = DISTRIBUTION_MODEL__VOLUME_UNIT;

	/**
	 * The feature id for the '<em><b>Exact</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_SIZE_DISTRIBUTION_MODEL__EXACT = DISTRIBUTION_MODEL_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Cargo Size Distribution Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_SIZE_DISTRIBUTION_MODEL_FEATURE_COUNT = DISTRIBUTION_MODEL_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_SIZE_DISTRIBUTION_MODEL___GET_UNSET_VALUE__ESTRUCTURALFEATURE = DISTRIBUTION_MODEL___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_SIZE_DISTRIBUTION_MODEL___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = DISTRIBUTION_MODEL___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_SIZE_DISTRIBUTION_MODEL___ECONTAINER_OP = DISTRIBUTION_MODEL___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Model Or Contract Volume Per Cargo</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_SIZE_DISTRIBUTION_MODEL___GET_MODEL_OR_CONTRACT_VOLUME_PER_CARGO = DISTRIBUTION_MODEL___GET_MODEL_OR_CONTRACT_VOLUME_PER_CARGO;

	/**
	 * The operation id for the '<em>Get Model Or Contract Volume Unit</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_SIZE_DISTRIBUTION_MODEL___GET_MODEL_OR_CONTRACT_VOLUME_UNIT = DISTRIBUTION_MODEL___GET_MODEL_OR_CONTRACT_VOLUME_UNIT;

	/**
	 * The number of operations of the '<em>Cargo Size Distribution Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_SIZE_DISTRIBUTION_MODEL_OPERATION_COUNT = DISTRIBUTION_MODEL_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_NUMBER_DISTRIBUTION_MODEL__EXTENSIONS = DISTRIBUTION_MODEL__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Volume Per Cargo</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_NUMBER_DISTRIBUTION_MODEL__VOLUME_PER_CARGO = DISTRIBUTION_MODEL__VOLUME_PER_CARGO;

	/**
	 * The feature id for the '<em><b>Volume Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_NUMBER_DISTRIBUTION_MODEL__VOLUME_UNIT = DISTRIBUTION_MODEL__VOLUME_UNIT;

	/**
	 * The feature id for the '<em><b>Number Of Cargoes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_NUMBER_DISTRIBUTION_MODEL__NUMBER_OF_CARGOES = DISTRIBUTION_MODEL_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Cargo Number Distribution Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_NUMBER_DISTRIBUTION_MODEL_FEATURE_COUNT = DISTRIBUTION_MODEL_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_NUMBER_DISTRIBUTION_MODEL___GET_UNSET_VALUE__ESTRUCTURALFEATURE = DISTRIBUTION_MODEL___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_NUMBER_DISTRIBUTION_MODEL___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = DISTRIBUTION_MODEL___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_NUMBER_DISTRIBUTION_MODEL___ECONTAINER_OP = DISTRIBUTION_MODEL___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Model Or Contract Volume Per Cargo</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_NUMBER_DISTRIBUTION_MODEL___GET_MODEL_OR_CONTRACT_VOLUME_PER_CARGO = DISTRIBUTION_MODEL___GET_MODEL_OR_CONTRACT_VOLUME_PER_CARGO;

	/**
	 * The operation id for the '<em>Get Model Or Contract Volume Unit</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_NUMBER_DISTRIBUTION_MODEL___GET_MODEL_OR_CONTRACT_VOLUME_UNIT = DISTRIBUTION_MODEL___GET_MODEL_OR_CONTRACT_VOLUME_UNIT;

	/**
	 * The number of operations of the '<em>Cargo Number Distribution Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_NUMBER_DISTRIBUTION_MODEL_OPERATION_COUNT = DISTRIBUTION_MODEL_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_BY_QUARTER_DISTRIBUTION_MODEL__EXTENSIONS = DISTRIBUTION_MODEL__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Volume Per Cargo</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_BY_QUARTER_DISTRIBUTION_MODEL__VOLUME_PER_CARGO = DISTRIBUTION_MODEL__VOLUME_PER_CARGO;

	/**
	 * The feature id for the '<em><b>Volume Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_BY_QUARTER_DISTRIBUTION_MODEL__VOLUME_UNIT = DISTRIBUTION_MODEL__VOLUME_UNIT;

	/**
	 * The feature id for the '<em><b>Q1</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q1 = DISTRIBUTION_MODEL_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Q2</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q2 = DISTRIBUTION_MODEL_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Q3</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q3 = DISTRIBUTION_MODEL_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Q4</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q4 = DISTRIBUTION_MODEL_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Cargo By Quarter Distribution Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_BY_QUARTER_DISTRIBUTION_MODEL_FEATURE_COUNT = DISTRIBUTION_MODEL_FEATURE_COUNT + 4;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_BY_QUARTER_DISTRIBUTION_MODEL___GET_UNSET_VALUE__ESTRUCTURALFEATURE = DISTRIBUTION_MODEL___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_BY_QUARTER_DISTRIBUTION_MODEL___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = DISTRIBUTION_MODEL___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_BY_QUARTER_DISTRIBUTION_MODEL___ECONTAINER_OP = DISTRIBUTION_MODEL___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Model Or Contract Volume Per Cargo</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_BY_QUARTER_DISTRIBUTION_MODEL___GET_MODEL_OR_CONTRACT_VOLUME_PER_CARGO = DISTRIBUTION_MODEL___GET_MODEL_OR_CONTRACT_VOLUME_PER_CARGO;

	/**
	 * The operation id for the '<em>Get Model Or Contract Volume Unit</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_BY_QUARTER_DISTRIBUTION_MODEL___GET_MODEL_OR_CONTRACT_VOLUME_UNIT = DISTRIBUTION_MODEL___GET_MODEL_OR_CONTRACT_VOLUME_UNIT;

	/**
	 * The number of operations of the '<em>Cargo By Quarter Distribution Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_BY_QUARTER_DISTRIBUTION_MODEL_OPERATION_COUNT = DISTRIBUTION_MODEL_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_INTERVAL_DISTRIBUTION_MODEL__EXTENSIONS = DISTRIBUTION_MODEL__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Volume Per Cargo</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_INTERVAL_DISTRIBUTION_MODEL__VOLUME_PER_CARGO = DISTRIBUTION_MODEL__VOLUME_PER_CARGO;

	/**
	 * The feature id for the '<em><b>Volume Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_INTERVAL_DISTRIBUTION_MODEL__VOLUME_UNIT = DISTRIBUTION_MODEL__VOLUME_UNIT;

	/**
	 * The feature id for the '<em><b>Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_INTERVAL_DISTRIBUTION_MODEL__QUANTITY = DISTRIBUTION_MODEL_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Interval Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_INTERVAL_DISTRIBUTION_MODEL__INTERVAL_TYPE = DISTRIBUTION_MODEL_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Spacing</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_INTERVAL_DISTRIBUTION_MODEL__SPACING = DISTRIBUTION_MODEL_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Cargo Interval Distribution Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_INTERVAL_DISTRIBUTION_MODEL_FEATURE_COUNT = DISTRIBUTION_MODEL_FEATURE_COUNT + 3;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_INTERVAL_DISTRIBUTION_MODEL___GET_UNSET_VALUE__ESTRUCTURALFEATURE = DISTRIBUTION_MODEL___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_INTERVAL_DISTRIBUTION_MODEL___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = DISTRIBUTION_MODEL___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_INTERVAL_DISTRIBUTION_MODEL___ECONTAINER_OP = DISTRIBUTION_MODEL___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Model Or Contract Volume Per Cargo</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_INTERVAL_DISTRIBUTION_MODEL___GET_MODEL_OR_CONTRACT_VOLUME_PER_CARGO = DISTRIBUTION_MODEL___GET_MODEL_OR_CONTRACT_VOLUME_PER_CARGO;

	/**
	 * The operation id for the '<em>Get Model Or Contract Volume Unit</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_INTERVAL_DISTRIBUTION_MODEL___GET_MODEL_OR_CONTRACT_VOLUME_UNIT = DISTRIBUTION_MODEL___GET_MODEL_OR_CONTRACT_VOLUME_UNIT;

	/**
	 * The number of operations of the '<em>Cargo Interval Distribution Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_INTERVAL_DISTRIBUTION_MODEL_OPERATION_COUNT = DISTRIBUTION_MODEL_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRE_DEFINED_DISTRIBUTION_MODEL__EXTENSIONS = DISTRIBUTION_MODEL__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Volume Per Cargo</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRE_DEFINED_DISTRIBUTION_MODEL__VOLUME_PER_CARGO = DISTRIBUTION_MODEL__VOLUME_PER_CARGO;

	/**
	 * The feature id for the '<em><b>Volume Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRE_DEFINED_DISTRIBUTION_MODEL__VOLUME_UNIT = DISTRIBUTION_MODEL__VOLUME_UNIT;

	/**
	 * The feature id for the '<em><b>Dates</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRE_DEFINED_DISTRIBUTION_MODEL__DATES = DISTRIBUTION_MODEL_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Pre Defined Distribution Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRE_DEFINED_DISTRIBUTION_MODEL_FEATURE_COUNT = DISTRIBUTION_MODEL_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRE_DEFINED_DISTRIBUTION_MODEL___GET_UNSET_VALUE__ESTRUCTURALFEATURE = DISTRIBUTION_MODEL___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRE_DEFINED_DISTRIBUTION_MODEL___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = DISTRIBUTION_MODEL___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRE_DEFINED_DISTRIBUTION_MODEL___ECONTAINER_OP = DISTRIBUTION_MODEL___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Model Or Contract Volume Per Cargo</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRE_DEFINED_DISTRIBUTION_MODEL___GET_MODEL_OR_CONTRACT_VOLUME_PER_CARGO = DISTRIBUTION_MODEL___GET_MODEL_OR_CONTRACT_VOLUME_PER_CARGO;

	/**
	 * The operation id for the '<em>Get Model Or Contract Volume Unit</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRE_DEFINED_DISTRIBUTION_MODEL___GET_MODEL_OR_CONTRACT_VOLUME_UNIT = DISTRIBUTION_MODEL___GET_MODEL_OR_CONTRACT_VOLUME_UNIT;

	/**
	 * The number of operations of the '<em>Pre Defined Distribution Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRE_DEFINED_DISTRIBUTION_MODEL_OPERATION_COUNT = DISTRIBUTION_MODEL_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRE_DEFINED_DATE__DATE = 0;

	/**
	 * The number of structural features of the '<em>Pre Defined Date</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRE_DEFINED_DATE_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Pre Defined Date</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRE_DEFINED_DATE_OPERATION_COUNT = 0;

	/**
	 * The number of structural features of the '<em>Sub Profile Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUB_PROFILE_CONSTRAINT_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Sub Profile Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUB_PROFILE_CONSTRAINT_OPERATION_COUNT = 0;

	/**
	 * The number of structural features of the '<em>Flow Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOW_TYPE_FEATURE_COUNT = SUB_PROFILE_CONSTRAINT_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Flow Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOW_TYPE_OPERATION_COUNT = SUB_PROFILE_CONSTRAINT_OPERATION_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Supply From Flow</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUPPLY_FROM_FLOW_FEATURE_COUNT = FLOW_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Supply From Flow</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUPPLY_FROM_FLOW_OPERATION_COUNT = FLOW_TYPE_OPERATION_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Deliver To Flow</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELIVER_TO_FLOW_FEATURE_COUNT = FLOW_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Deliver To Flow</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELIVER_TO_FLOW_OPERATION_COUNT = FLOW_TYPE_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Profile</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUPPLY_FROM_PROFILE_FLOW__PROFILE = SUPPLY_FROM_FLOW_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Sub Profile</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUPPLY_FROM_PROFILE_FLOW__SUB_PROFILE = SUPPLY_FROM_FLOW_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Supply From Profile Flow</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUPPLY_FROM_PROFILE_FLOW_FEATURE_COUNT = SUPPLY_FROM_FLOW_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Supply From Profile Flow</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUPPLY_FROM_PROFILE_FLOW_OPERATION_COUNT = SUPPLY_FROM_FLOW_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Profile</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELIVER_TO_PROFILE_FLOW__PROFILE = DELIVER_TO_FLOW_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Sub Profile</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELIVER_TO_PROFILE_FLOW__SUB_PROFILE = DELIVER_TO_FLOW_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Deliver To Profile Flow</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELIVER_TO_PROFILE_FLOW_FEATURE_COUNT = DELIVER_TO_FLOW_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Deliver To Profile Flow</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELIVER_TO_PROFILE_FLOW_OPERATION_COUNT = DELIVER_TO_FLOW_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUPPLY_FROM_SPOT_FLOW__MARKET = SUPPLY_FROM_FLOW_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Supply From Spot Flow</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUPPLY_FROM_SPOT_FLOW_FEATURE_COUNT = SUPPLY_FROM_FLOW_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Supply From Spot Flow</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUPPLY_FROM_SPOT_FLOW_OPERATION_COUNT = SUPPLY_FROM_FLOW_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELIVER_TO_SPOT_FLOW__MARKET = DELIVER_TO_FLOW_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Deliver To Spot Flow</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELIVER_TO_SPOT_FLOW_FEATURE_COUNT = DELIVER_TO_FLOW_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Deliver To Spot Flow</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELIVER_TO_SPOT_FLOW_OPERATION_COUNT = DELIVER_TO_FLOW_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Vessels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_VESSEL_RESTRICTION__VESSELS = SUB_PROFILE_CONSTRAINT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Profile Vessel Restriction</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_VESSEL_RESTRICTION_FEATURE_COUNT = SUB_PROFILE_CONSTRAINT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Profile Vessel Restriction</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_VESSEL_RESTRICTION_OPERATION_COUNT = SUB_PROFILE_CONSTRAINT_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Vessel Assignment Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHIPPING_OPTION__VESSEL_ASSIGNMENT_TYPE = SUB_PROFILE_CONSTRAINT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Spot Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHIPPING_OPTION__SPOT_INDEX = SUB_PROFILE_CONSTRAINT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHIPPING_OPTION__VESSEL = SUB_PROFILE_CONSTRAINT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Shipping Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHIPPING_OPTION_FEATURE_COUNT = SUB_PROFILE_CONSTRAINT_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>Shipping Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHIPPING_OPTION_OPERATION_COUNT = SUB_PROFILE_CONSTRAINT_OPERATION_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Profile Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_CONSTRAINT_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Profile Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_CONSTRAINT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.MinCargoConstraintImpl <em>Min Cargo Constraint</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.MinCargoConstraintImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getMinCargoConstraint()
	 * @generated
	 */
	int MIN_CARGO_CONSTRAINT = 25;

	/**
	 * The feature id for the '<em><b>Min Cargoes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MIN_CARGO_CONSTRAINT__MIN_CARGOES = PROFILE_CONSTRAINT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Interval Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MIN_CARGO_CONSTRAINT__INTERVAL_TYPE = PROFILE_CONSTRAINT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Min Cargo Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MIN_CARGO_CONSTRAINT_FEATURE_COUNT = PROFILE_CONSTRAINT_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Min Cargo Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MIN_CARGO_CONSTRAINT_OPERATION_COUNT = PROFILE_CONSTRAINT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.MaxCargoConstraintImpl <em>Max Cargo Constraint</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.MaxCargoConstraintImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getMaxCargoConstraint()
	 * @generated
	 */
	int MAX_CARGO_CONSTRAINT = 26;

	/**
	 * The feature id for the '<em><b>Max Cargoes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAX_CARGO_CONSTRAINT__MAX_CARGOES = PROFILE_CONSTRAINT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Interval Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAX_CARGO_CONSTRAINT__INTERVAL_TYPE = PROFILE_CONSTRAINT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Max Cargo Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAX_CARGO_CONSTRAINT_FEATURE_COUNT = PROFILE_CONSTRAINT_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Max Cargo Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAX_CARGO_CONSTRAINT_OPERATION_COUNT = PROFILE_CONSTRAINT_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Distributions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERIOD_DISTRIBUTION_PROFILE_CONSTRAINT__DISTRIBUTIONS = PROFILE_CONSTRAINT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Period Distribution Profile Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERIOD_DISTRIBUTION_PROFILE_CONSTRAINT_FEATURE_COUNT = PROFILE_CONSTRAINT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Period Distribution Profile Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERIOD_DISTRIBUTION_PROFILE_CONSTRAINT_OPERATION_COUNT = PROFILE_CONSTRAINT_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Range</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERIOD_DISTRIBUTION__RANGE = 0;

	/**
	 * The feature id for the '<em><b>Min Cargoes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERIOD_DISTRIBUTION__MIN_CARGOES = 1;

	/**
	 * The feature id for the '<em><b>Max Cargoes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERIOD_DISTRIBUTION__MAX_CARGOES = 2;

	/**
	 * The number of structural features of the '<em>Period Distribution</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERIOD_DISTRIBUTION_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Period Distribution</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERIOD_DISTRIBUTION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.FleetConstraint <em>Fleet Constraint</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.FleetConstraint
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getFleetConstraint()
	 * @generated
	 */
	int FLEET_CONSTRAINT = 29;

	/**
	 * The number of structural features of the '<em>Fleet Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_CONSTRAINT_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Fleet Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_CONSTRAINT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.TargetCargoesOnVesselConstraintImpl <em>Target Cargoes On Vessel Constraint</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.TargetCargoesOnVesselConstraintImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getTargetCargoesOnVesselConstraint()
	 * @generated
	 */
	int TARGET_CARGOES_ON_VESSEL_CONSTRAINT = 30;

	/**
	 * The feature id for the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TARGET_CARGOES_ON_VESSEL_CONSTRAINT__VESSEL = FLEET_CONSTRAINT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Target Number Of Cargoes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TARGET_CARGOES_ON_VESSEL_CONSTRAINT__TARGET_NUMBER_OF_CARGOES = FLEET_CONSTRAINT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Interval Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TARGET_CARGOES_ON_VESSEL_CONSTRAINT__INTERVAL_TYPE = FLEET_CONSTRAINT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Weight</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TARGET_CARGOES_ON_VESSEL_CONSTRAINT__WEIGHT = FLEET_CONSTRAINT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Target Cargoes On Vessel Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TARGET_CARGOES_ON_VESSEL_CONSTRAINT_FEATURE_COUNT = FLEET_CONSTRAINT_FEATURE_COUNT + 4;

	/**
	 * The number of operations of the '<em>Target Cargoes On Vessel Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TARGET_CARGOES_ON_VESSEL_CONSTRAINT_OPERATION_COUNT = FLEET_CONSTRAINT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.MullEntityRowImpl <em>Mull Entity Row</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.MullEntityRowImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getMullEntityRow()
	 * @generated
	 */
	int MULL_ENTITY_ROW = 31;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULL_ENTITY_ROW__ENTITY = 0;

	/**
	 * The feature id for the '<em><b>Initial Allocation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULL_ENTITY_ROW__INITIAL_ALLOCATION = 1;

	/**
	 * The feature id for the '<em><b>Relative Entitlement</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULL_ENTITY_ROW__RELATIVE_ENTITLEMENT = 2;

	/**
	 * The feature id for the '<em><b>Des Sales Market Allocation Rows</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULL_ENTITY_ROW__DES_SALES_MARKET_ALLOCATION_ROWS = 3;

	/**
	 * The feature id for the '<em><b>Sales Contract Allocation Rows</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULL_ENTITY_ROW__SALES_CONTRACT_ALLOCATION_ROWS = 4;

	/**
	 * The number of structural features of the '<em>Mull Entity Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULL_ENTITY_ROW_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>Mull Entity Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULL_ENTITY_ROW_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.MullAllocationRowImpl <em>Mull Allocation Row</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.MullAllocationRowImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getMullAllocationRow()
	 * @generated
	 */
	int MULL_ALLOCATION_ROW = 36;

	/**
	 * The feature id for the '<em><b>Weight</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULL_ALLOCATION_ROW__WEIGHT = 0;

	/**
	 * The feature id for the '<em><b>Vessels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULL_ALLOCATION_ROW__VESSELS = 1;

	/**
	 * The number of structural features of the '<em>Mull Allocation Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULL_ALLOCATION_ROW_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Mull Allocation Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULL_ALLOCATION_ROW_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.DESSalesMarketAllocationRowImpl <em>DES Sales Market Allocation Row</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.DESSalesMarketAllocationRowImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getDESSalesMarketAllocationRow()
	 * @generated
	 */
	int DES_SALES_MARKET_ALLOCATION_ROW = 32;

	/**
	 * The feature id for the '<em><b>Weight</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_SALES_MARKET_ALLOCATION_ROW__WEIGHT = MULL_ALLOCATION_ROW__WEIGHT;

	/**
	 * The feature id for the '<em><b>Vessels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_SALES_MARKET_ALLOCATION_ROW__VESSELS = MULL_ALLOCATION_ROW__VESSELS;

	/**
	 * The feature id for the '<em><b>Des Sales Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_SALES_MARKET_ALLOCATION_ROW__DES_SALES_MARKET = MULL_ALLOCATION_ROW_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>DES Sales Market Allocation Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_SALES_MARKET_ALLOCATION_ROW_FEATURE_COUNT = MULL_ALLOCATION_ROW_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>DES Sales Market Allocation Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DES_SALES_MARKET_ALLOCATION_ROW_OPERATION_COUNT = MULL_ALLOCATION_ROW_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.SalesContractAllocationRowImpl <em>Sales Contract Allocation Row</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.SalesContractAllocationRowImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getSalesContractAllocationRow()
	 * @generated
	 */
	int SALES_CONTRACT_ALLOCATION_ROW = 33;

	/**
	 * The feature id for the '<em><b>Weight</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT_ALLOCATION_ROW__WEIGHT = MULL_ALLOCATION_ROW__WEIGHT;

	/**
	 * The feature id for the '<em><b>Vessels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT_ALLOCATION_ROW__VESSELS = MULL_ALLOCATION_ROW__VESSELS;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT_ALLOCATION_ROW__CONTRACT = MULL_ALLOCATION_ROW_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Sales Contract Allocation Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT_ALLOCATION_ROW_FEATURE_COUNT = MULL_ALLOCATION_ROW_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Sales Contract Allocation Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT_ALLOCATION_ROW_OPERATION_COUNT = MULL_ALLOCATION_ROW_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.MullProfileImpl <em>Mull Profile</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.MullProfileImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getMullProfile()
	 * @generated
	 */
	int MULL_PROFILE = 34;

	/**
	 * The feature id for the '<em><b>Window Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULL_PROFILE__WINDOW_SIZE = 0;

	/**
	 * The feature id for the '<em><b>Volume Flex</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULL_PROFILE__VOLUME_FLEX = 1;

	/**
	 * The feature id for the '<em><b>Inventories</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULL_PROFILE__INVENTORIES = 2;

	/**
	 * The feature id for the '<em><b>Full Cargo Lot Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULL_PROFILE__FULL_CARGO_LOT_VALUE = 3;

	/**
	 * The feature id for the '<em><b>Cargoes To Keep</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULL_PROFILE__CARGOES_TO_KEEP = 4;

	/**
	 * The number of structural features of the '<em>Mull Profile</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULL_PROFILE_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>Mull Profile</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULL_PROFILE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.MullSubprofileImpl <em>Mull Subprofile</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.MullSubprofileImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getMullSubprofile()
	 * @generated
	 */
	int MULL_SUBPROFILE = 35;

	/**
	 * The feature id for the '<em><b>Inventory</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULL_SUBPROFILE__INVENTORY = 0;

	/**
	 * The feature id for the '<em><b>Entity Table</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULL_SUBPROFILE__ENTITY_TABLE = 1;

	/**
	 * The number of structural features of the '<em>Mull Subprofile</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULL_SUBPROFILE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Mull Subprofile</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULL_SUBPROFILE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.MullCargoWrapperImpl <em>Mull Cargo Wrapper</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.MullCargoWrapperImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getMullCargoWrapper()
	 * @generated
	 */
	int MULL_CARGO_WRAPPER = 37;

	/**
	 * The feature id for the '<em><b>Load Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULL_CARGO_WRAPPER__LOAD_SLOT = 0;

	/**
	 * The feature id for the '<em><b>Discharge Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULL_CARGO_WRAPPER__DISCHARGE_SLOT = 1;

	/**
	 * The number of structural features of the '<em>Mull Cargo Wrapper</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULL_CARGO_WRAPPER_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Mull Cargo Wrapper</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MULL_CARGO_WRAPPER_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.IntervalType <em>Interval Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.IntervalType
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getIntervalType()
	 * @generated
	 */
	int INTERVAL_TYPE = 38;


	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.LNGVolumeUnit <em>LNG Volume Unit</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.LNGVolumeUnit
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getLNGVolumeUnit()
	 * @generated
	 */
	int LNG_VOLUME_UNIT = 39;

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.ADPModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see com.mmxlabs.models.lng.adp.ADPModel
	 * @generated
	 */
	EClass getADPModel();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.ADPModel#getYearStart <em>Year Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Year Start</em>'.
	 * @see com.mmxlabs.models.lng.adp.ADPModel#getYearStart()
	 * @see #getADPModel()
	 * @generated
	 */
	EAttribute getADPModel_YearStart();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.ADPModel#getYearEnd <em>Year End</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Year End</em>'.
	 * @see com.mmxlabs.models.lng.adp.ADPModel#getYearEnd()
	 * @see #getADPModel()
	 * @generated
	 */
	EAttribute getADPModel_YearEnd();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.adp.ADPModel#getPurchaseContractProfiles <em>Purchase Contract Profiles</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Purchase Contract Profiles</em>'.
	 * @see com.mmxlabs.models.lng.adp.ADPModel#getPurchaseContractProfiles()
	 * @see #getADPModel()
	 * @generated
	 */
	EReference getADPModel_PurchaseContractProfiles();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.adp.ADPModel#getSalesContractProfiles <em>Sales Contract Profiles</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Sales Contract Profiles</em>'.
	 * @see com.mmxlabs.models.lng.adp.ADPModel#getSalesContractProfiles()
	 * @see #getADPModel()
	 * @generated
	 */
	EReference getADPModel_SalesContractProfiles();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.adp.ADPModel#getFleetProfile <em>Fleet Profile</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Fleet Profile</em>'.
	 * @see com.mmxlabs.models.lng.adp.ADPModel#getFleetProfile()
	 * @see #getADPModel()
	 * @generated
	 */
	EReference getADPModel_FleetProfile();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.adp.ADPModel#getMullProfile <em>Mull Profile</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Mull Profile</em>'.
	 * @see com.mmxlabs.models.lng.adp.ADPModel#getMullProfile()
	 * @see #getADPModel()
	 * @generated
	 */
	EReference getADPModel_MullProfile();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.FleetProfile <em>Fleet Profile</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Fleet Profile</em>'.
	 * @see com.mmxlabs.models.lng.adp.FleetProfile
	 * @generated
	 */
	EClass getFleetProfile();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.adp.FleetProfile#getConstraints <em>Constraints</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Constraints</em>'.
	 * @see com.mmxlabs.models.lng.adp.FleetProfile#getConstraints()
	 * @see #getFleetProfile()
	 * @generated
	 */
	EReference getFleetProfile_Constraints();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.adp.FleetProfile#getDefaultNominalMarket <em>Default Nominal Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Default Nominal Market</em>'.
	 * @see com.mmxlabs.models.lng.adp.FleetProfile#getDefaultNominalMarket()
	 * @see #getFleetProfile()
	 * @generated
	 */
	EReference getFleetProfile_DefaultNominalMarket();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.ContractProfile <em>Contract Profile</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Contract Profile</em>'.
	 * @see com.mmxlabs.models.lng.adp.ContractProfile
	 * @generated
	 */
	EClass getContractProfile();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.adp.ContractProfile#getContract <em>Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Contract</em>'.
	 * @see com.mmxlabs.models.lng.adp.ContractProfile#getContract()
	 * @see #getContractProfile()
	 * @generated
	 */
	EReference getContractProfile_Contract();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.ContractProfile#getContractCode <em>Contract Code</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Contract Code</em>'.
	 * @see com.mmxlabs.models.lng.adp.ContractProfile#getContractCode()
	 * @see #getContractProfile()
	 * @generated
	 */
	EAttribute getContractProfile_ContractCode();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.ContractProfile#isCustom <em>Custom</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Custom</em>'.
	 * @see com.mmxlabs.models.lng.adp.ContractProfile#isCustom()
	 * @see #getContractProfile()
	 * @generated
	 */
	EAttribute getContractProfile_Custom();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.ContractProfile#isEnabled <em>Enabled</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Enabled</em>'.
	 * @see com.mmxlabs.models.lng.adp.ContractProfile#isEnabled()
	 * @see #getContractProfile()
	 * @generated
	 */
	EAttribute getContractProfile_Enabled();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.ContractProfile#getTotalVolume <em>Total Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Total Volume</em>'.
	 * @see com.mmxlabs.models.lng.adp.ContractProfile#getTotalVolume()
	 * @see #getContractProfile()
	 * @generated
	 */
	EAttribute getContractProfile_TotalVolume();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.ContractProfile#getVolumeUnit <em>Volume Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volume Unit</em>'.
	 * @see com.mmxlabs.models.lng.adp.ContractProfile#getVolumeUnit()
	 * @see #getContractProfile()
	 * @generated
	 */
	EAttribute getContractProfile_VolumeUnit();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.adp.ContractProfile#getSubProfiles <em>Sub Profiles</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Sub Profiles</em>'.
	 * @see com.mmxlabs.models.lng.adp.ContractProfile#getSubProfiles()
	 * @see #getContractProfile()
	 * @generated
	 */
	EReference getContractProfile_SubProfiles();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.adp.ContractProfile#getConstraints <em>Constraints</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Constraints</em>'.
	 * @see com.mmxlabs.models.lng.adp.ContractProfile#getConstraints()
	 * @see #getContractProfile()
	 * @generated
	 */
	EReference getContractProfile_Constraints();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.DistributionModel <em>Distribution Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Distribution Model</em>'.
	 * @see com.mmxlabs.models.lng.adp.DistributionModel
	 * @generated
	 */
	EClass getDistributionModel();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.DistributionModel#getVolumePerCargo <em>Volume Per Cargo</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volume Per Cargo</em>'.
	 * @see com.mmxlabs.models.lng.adp.DistributionModel#getVolumePerCargo()
	 * @see #getDistributionModel()
	 * @generated
	 */
	EAttribute getDistributionModel_VolumePerCargo();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.DistributionModel#getVolumeUnit <em>Volume Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volume Unit</em>'.
	 * @see com.mmxlabs.models.lng.adp.DistributionModel#getVolumeUnit()
	 * @see #getDistributionModel()
	 * @generated
	 */
	EAttribute getDistributionModel_VolumeUnit();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.adp.DistributionModel#getModelOrContractVolumePerCargo() <em>Get Model Or Contract Volume Per Cargo</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Model Or Contract Volume Per Cargo</em>' operation.
	 * @see com.mmxlabs.models.lng.adp.DistributionModel#getModelOrContractVolumePerCargo()
	 * @generated
	 */
	EOperation getDistributionModel__GetModelOrContractVolumePerCargo();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.adp.DistributionModel#getModelOrContractVolumeUnit() <em>Get Model Or Contract Volume Unit</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Model Or Contract Volume Unit</em>' operation.
	 * @see com.mmxlabs.models.lng.adp.DistributionModel#getModelOrContractVolumeUnit()
	 * @generated
	 */
	EOperation getDistributionModel__GetModelOrContractVolumeUnit();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.PeriodDistributionProfileConstraint <em>Period Distribution Profile Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Period Distribution Profile Constraint</em>'.
	 * @see com.mmxlabs.models.lng.adp.PeriodDistributionProfileConstraint
	 * @generated
	 */
	EClass getPeriodDistributionProfileConstraint();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.adp.PeriodDistributionProfileConstraint#getDistributions <em>Distributions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Distributions</em>'.
	 * @see com.mmxlabs.models.lng.adp.PeriodDistributionProfileConstraint#getDistributions()
	 * @see #getPeriodDistributionProfileConstraint()
	 * @generated
	 */
	EReference getPeriodDistributionProfileConstraint_Distributions();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.PeriodDistribution <em>Period Distribution</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Period Distribution</em>'.
	 * @see com.mmxlabs.models.lng.adp.PeriodDistribution
	 * @generated
	 */
	EClass getPeriodDistribution();

	/**
	 * Returns the meta object for the attribute list '{@link com.mmxlabs.models.lng.adp.PeriodDistribution#getRange <em>Range</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Range</em>'.
	 * @see com.mmxlabs.models.lng.adp.PeriodDistribution#getRange()
	 * @see #getPeriodDistribution()
	 * @generated
	 */
	EAttribute getPeriodDistribution_Range();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.PeriodDistribution#getMinCargoes <em>Min Cargoes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Cargoes</em>'.
	 * @see com.mmxlabs.models.lng.adp.PeriodDistribution#getMinCargoes()
	 * @see #getPeriodDistribution()
	 * @generated
	 */
	EAttribute getPeriodDistribution_MinCargoes();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.PeriodDistribution#getMaxCargoes <em>Max Cargoes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Cargoes</em>'.
	 * @see com.mmxlabs.models.lng.adp.PeriodDistribution#getMaxCargoes()
	 * @see #getPeriodDistribution()
	 * @generated
	 */
	EAttribute getPeriodDistribution_MaxCargoes();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.CargoSizeDistributionModel <em>Cargo Size Distribution Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cargo Size Distribution Model</em>'.
	 * @see com.mmxlabs.models.lng.adp.CargoSizeDistributionModel
	 * @generated
	 */
	EClass getCargoSizeDistributionModel();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.CargoSizeDistributionModel#isExact <em>Exact</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Exact</em>'.
	 * @see com.mmxlabs.models.lng.adp.CargoSizeDistributionModel#isExact()
	 * @see #getCargoSizeDistributionModel()
	 * @generated
	 */
	EAttribute getCargoSizeDistributionModel_Exact();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.CargoNumberDistributionModel <em>Cargo Number Distribution Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cargo Number Distribution Model</em>'.
	 * @see com.mmxlabs.models.lng.adp.CargoNumberDistributionModel
	 * @generated
	 */
	EClass getCargoNumberDistributionModel();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.CargoNumberDistributionModel#getNumberOfCargoes <em>Number Of Cargoes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Number Of Cargoes</em>'.
	 * @see com.mmxlabs.models.lng.adp.CargoNumberDistributionModel#getNumberOfCargoes()
	 * @see #getCargoNumberDistributionModel()
	 * @generated
	 */
	EAttribute getCargoNumberDistributionModel_NumberOfCargoes();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.PurchaseContractProfile <em>Purchase Contract Profile</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Purchase Contract Profile</em>'.
	 * @see com.mmxlabs.models.lng.adp.PurchaseContractProfile
	 * @generated
	 */
	EClass getPurchaseContractProfile();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.SalesContractProfile <em>Sales Contract Profile</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Sales Contract Profile</em>'.
	 * @see com.mmxlabs.models.lng.adp.SalesContractProfile
	 * @generated
	 */
	EClass getSalesContractProfile();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.SubContractProfile <em>Sub Contract Profile</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Sub Contract Profile</em>'.
	 * @see com.mmxlabs.models.lng.adp.SubContractProfile
	 * @generated
	 */
	EClass getSubContractProfile();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.SubContractProfile#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see com.mmxlabs.models.lng.adp.SubContractProfile#getName()
	 * @see #getSubContractProfile()
	 * @generated
	 */
	EAttribute getSubContractProfile_Name();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.SubContractProfile#getContractType <em>Contract Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Contract Type</em>'.
	 * @see com.mmxlabs.models.lng.adp.SubContractProfile#getContractType()
	 * @see #getSubContractProfile()
	 * @generated
	 */
	EAttribute getSubContractProfile_ContractType();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.adp.SubContractProfile#getDistributionModel <em>Distribution Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Distribution Model</em>'.
	 * @see com.mmxlabs.models.lng.adp.SubContractProfile#getDistributionModel()
	 * @see #getSubContractProfile()
	 * @generated
	 */
	EReference getSubContractProfile_DistributionModel();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.SubContractProfile#getSlotTemplateId <em>Slot Template Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Slot Template Id</em>'.
	 * @see com.mmxlabs.models.lng.adp.SubContractProfile#getSlotTemplateId()
	 * @see #getSubContractProfile()
	 * @generated
	 */
	EAttribute getSubContractProfile_SlotTemplateId();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.adp.SubContractProfile#getCustomAttribs <em>Custom Attribs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Custom Attribs</em>'.
	 * @see com.mmxlabs.models.lng.adp.SubContractProfile#getCustomAttribs()
	 * @see #getSubContractProfile()
	 * @generated
	 */
	EReference getSubContractProfile_CustomAttribs();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.adp.SubContractProfile#getNominatedVessel <em>Nominated Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Nominated Vessel</em>'.
	 * @see com.mmxlabs.models.lng.adp.SubContractProfile#getNominatedVessel()
	 * @see #getSubContractProfile()
	 * @generated
	 */
	EReference getSubContractProfile_NominatedVessel();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.SubContractProfile#getShippingDays <em>Shipping Days</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Shipping Days</em>'.
	 * @see com.mmxlabs.models.lng.adp.SubContractProfile#getShippingDays()
	 * @see #getSubContractProfile()
	 * @generated
	 */
	EAttribute getSubContractProfile_ShippingDays();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.adp.SubContractProfile#getConstraints <em>Constraints</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Constraints</em>'.
	 * @see com.mmxlabs.models.lng.adp.SubContractProfile#getConstraints()
	 * @see #getSubContractProfile()
	 * @generated
	 */
	EReference getSubContractProfile_Constraints();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.SubContractProfile#getWindowSize <em>Window Size</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Window Size</em>'.
	 * @see com.mmxlabs.models.lng.adp.SubContractProfile#getWindowSize()
	 * @see #getSubContractProfile()
	 * @generated
	 */
	EAttribute getSubContractProfile_WindowSize();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.SubContractProfile#getWindowSizeUnits <em>Window Size Units</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Window Size Units</em>'.
	 * @see com.mmxlabs.models.lng.adp.SubContractProfile#getWindowSizeUnits()
	 * @see #getSubContractProfile()
	 * @generated
	 */
	EAttribute getSubContractProfile_WindowSizeUnits();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.adp.SubContractProfile#getPort <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Port</em>'.
	 * @see com.mmxlabs.models.lng.adp.SubContractProfile#getPort()
	 * @see #getSubContractProfile()
	 * @generated
	 */
	EReference getSubContractProfile_Port();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.CustomSubProfileAttributes <em>Custom Sub Profile Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Custom Sub Profile Attributes</em>'.
	 * @see com.mmxlabs.models.lng.adp.CustomSubProfileAttributes
	 * @generated
	 */
	EClass getCustomSubProfileAttributes();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.CargoByQuarterDistributionModel <em>Cargo By Quarter Distribution Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cargo By Quarter Distribution Model</em>'.
	 * @see com.mmxlabs.models.lng.adp.CargoByQuarterDistributionModel
	 * @generated
	 */
	EClass getCargoByQuarterDistributionModel();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.CargoByQuarterDistributionModel#getQ1 <em>Q1</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Q1</em>'.
	 * @see com.mmxlabs.models.lng.adp.CargoByQuarterDistributionModel#getQ1()
	 * @see #getCargoByQuarterDistributionModel()
	 * @generated
	 */
	EAttribute getCargoByQuarterDistributionModel_Q1();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.CargoByQuarterDistributionModel#getQ2 <em>Q2</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Q2</em>'.
	 * @see com.mmxlabs.models.lng.adp.CargoByQuarterDistributionModel#getQ2()
	 * @see #getCargoByQuarterDistributionModel()
	 * @generated
	 */
	EAttribute getCargoByQuarterDistributionModel_Q2();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.CargoByQuarterDistributionModel#getQ3 <em>Q3</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Q3</em>'.
	 * @see com.mmxlabs.models.lng.adp.CargoByQuarterDistributionModel#getQ3()
	 * @see #getCargoByQuarterDistributionModel()
	 * @generated
	 */
	EAttribute getCargoByQuarterDistributionModel_Q3();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.CargoByQuarterDistributionModel#getQ4 <em>Q4</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Q4</em>'.
	 * @see com.mmxlabs.models.lng.adp.CargoByQuarterDistributionModel#getQ4()
	 * @see #getCargoByQuarterDistributionModel()
	 * @generated
	 */
	EAttribute getCargoByQuarterDistributionModel_Q4();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.CargoIntervalDistributionModel <em>Cargo Interval Distribution Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cargo Interval Distribution Model</em>'.
	 * @see com.mmxlabs.models.lng.adp.CargoIntervalDistributionModel
	 * @generated
	 */
	EClass getCargoIntervalDistributionModel();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.CargoIntervalDistributionModel#getQuantity <em>Quantity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Quantity</em>'.
	 * @see com.mmxlabs.models.lng.adp.CargoIntervalDistributionModel#getQuantity()
	 * @see #getCargoIntervalDistributionModel()
	 * @generated
	 */
	EAttribute getCargoIntervalDistributionModel_Quantity();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.CargoIntervalDistributionModel#getIntervalType <em>Interval Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Interval Type</em>'.
	 * @see com.mmxlabs.models.lng.adp.CargoIntervalDistributionModel#getIntervalType()
	 * @see #getCargoIntervalDistributionModel()
	 * @generated
	 */
	EAttribute getCargoIntervalDistributionModel_IntervalType();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.CargoIntervalDistributionModel#getSpacing <em>Spacing</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Spacing</em>'.
	 * @see com.mmxlabs.models.lng.adp.CargoIntervalDistributionModel#getSpacing()
	 * @see #getCargoIntervalDistributionModel()
	 * @generated
	 */
	EAttribute getCargoIntervalDistributionModel_Spacing();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.PreDefinedDistributionModel <em>Pre Defined Distribution Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Pre Defined Distribution Model</em>'.
	 * @see com.mmxlabs.models.lng.adp.PreDefinedDistributionModel
	 * @generated
	 */
	EClass getPreDefinedDistributionModel();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.adp.PreDefinedDistributionModel#getDates <em>Dates</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Dates</em>'.
	 * @see com.mmxlabs.models.lng.adp.PreDefinedDistributionModel#getDates()
	 * @see #getPreDefinedDistributionModel()
	 * @generated
	 */
	EReference getPreDefinedDistributionModel_Dates();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.PreDefinedDate <em>Pre Defined Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Pre Defined Date</em>'.
	 * @see com.mmxlabs.models.lng.adp.PreDefinedDate
	 * @generated
	 */
	EClass getPreDefinedDate();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.PreDefinedDate#getDate <em>Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Date</em>'.
	 * @see com.mmxlabs.models.lng.adp.PreDefinedDate#getDate()
	 * @see #getPreDefinedDate()
	 * @generated
	 */
	EAttribute getPreDefinedDate_Date();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.FlowType <em>Flow Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Flow Type</em>'.
	 * @see com.mmxlabs.models.lng.adp.FlowType
	 * @generated
	 */
	EClass getFlowType();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.SupplyFromFlow <em>Supply From Flow</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Supply From Flow</em>'.
	 * @see com.mmxlabs.models.lng.adp.SupplyFromFlow
	 * @generated
	 */
	EClass getSupplyFromFlow();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.DeliverToFlow <em>Deliver To Flow</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Deliver To Flow</em>'.
	 * @see com.mmxlabs.models.lng.adp.DeliverToFlow
	 * @generated
	 */
	EClass getDeliverToFlow();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.SupplyFromProfileFlow <em>Supply From Profile Flow</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Supply From Profile Flow</em>'.
	 * @see com.mmxlabs.models.lng.adp.SupplyFromProfileFlow
	 * @generated
	 */
	EClass getSupplyFromProfileFlow();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.adp.SupplyFromProfileFlow#getProfile <em>Profile</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Profile</em>'.
	 * @see com.mmxlabs.models.lng.adp.SupplyFromProfileFlow#getProfile()
	 * @see #getSupplyFromProfileFlow()
	 * @generated
	 */
	EReference getSupplyFromProfileFlow_Profile();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.adp.SupplyFromProfileFlow#getSubProfile <em>Sub Profile</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Sub Profile</em>'.
	 * @see com.mmxlabs.models.lng.adp.SupplyFromProfileFlow#getSubProfile()
	 * @see #getSupplyFromProfileFlow()
	 * @generated
	 */
	EReference getSupplyFromProfileFlow_SubProfile();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.DeliverToProfileFlow <em>Deliver To Profile Flow</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Deliver To Profile Flow</em>'.
	 * @see com.mmxlabs.models.lng.adp.DeliverToProfileFlow
	 * @generated
	 */
	EClass getDeliverToProfileFlow();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.adp.DeliverToProfileFlow#getProfile <em>Profile</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Profile</em>'.
	 * @see com.mmxlabs.models.lng.adp.DeliverToProfileFlow#getProfile()
	 * @see #getDeliverToProfileFlow()
	 * @generated
	 */
	EReference getDeliverToProfileFlow_Profile();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.adp.DeliverToProfileFlow#getSubProfile <em>Sub Profile</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Sub Profile</em>'.
	 * @see com.mmxlabs.models.lng.adp.DeliverToProfileFlow#getSubProfile()
	 * @see #getDeliverToProfileFlow()
	 * @generated
	 */
	EReference getDeliverToProfileFlow_SubProfile();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.SupplyFromSpotFlow <em>Supply From Spot Flow</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Supply From Spot Flow</em>'.
	 * @see com.mmxlabs.models.lng.adp.SupplyFromSpotFlow
	 * @generated
	 */
	EClass getSupplyFromSpotFlow();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.adp.SupplyFromSpotFlow#getMarket <em>Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Market</em>'.
	 * @see com.mmxlabs.models.lng.adp.SupplyFromSpotFlow#getMarket()
	 * @see #getSupplyFromSpotFlow()
	 * @generated
	 */
	EReference getSupplyFromSpotFlow_Market();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.DeliverToSpotFlow <em>Deliver To Spot Flow</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Deliver To Spot Flow</em>'.
	 * @see com.mmxlabs.models.lng.adp.DeliverToSpotFlow
	 * @generated
	 */
	EClass getDeliverToSpotFlow();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.adp.DeliverToSpotFlow#getMarket <em>Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Market</em>'.
	 * @see com.mmxlabs.models.lng.adp.DeliverToSpotFlow#getMarket()
	 * @see #getDeliverToSpotFlow()
	 * @generated
	 */
	EReference getDeliverToSpotFlow_Market();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.ProfileVesselRestriction <em>Profile Vessel Restriction</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Profile Vessel Restriction</em>'.
	 * @see com.mmxlabs.models.lng.adp.ProfileVesselRestriction
	 * @generated
	 */
	EClass getProfileVesselRestriction();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.adp.ProfileVesselRestriction#getVessels <em>Vessels</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Vessels</em>'.
	 * @see com.mmxlabs.models.lng.adp.ProfileVesselRestriction#getVessels()
	 * @see #getProfileVesselRestriction()
	 * @generated
	 */
	EReference getProfileVesselRestriction_Vessels();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.ShippingOption <em>Shipping Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Shipping Option</em>'.
	 * @see com.mmxlabs.models.lng.adp.ShippingOption
	 * @generated
	 */
	EClass getShippingOption();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.adp.ShippingOption#getVesselAssignmentType <em>Vessel Assignment Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel Assignment Type</em>'.
	 * @see com.mmxlabs.models.lng.adp.ShippingOption#getVesselAssignmentType()
	 * @see #getShippingOption()
	 * @generated
	 */
	EReference getShippingOption_VesselAssignmentType();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.ShippingOption#getSpotIndex <em>Spot Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Spot Index</em>'.
	 * @see com.mmxlabs.models.lng.adp.ShippingOption#getSpotIndex()
	 * @see #getShippingOption()
	 * @generated
	 */
	EAttribute getShippingOption_SpotIndex();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.adp.ShippingOption#getVessel <em>Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel</em>'.
	 * @see com.mmxlabs.models.lng.adp.ShippingOption#getVessel()
	 * @see #getShippingOption()
	 * @generated
	 */
	EReference getShippingOption_Vessel();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.ProfileConstraint <em>Profile Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Profile Constraint</em>'.
	 * @see com.mmxlabs.models.lng.adp.ProfileConstraint
	 * @generated
	 */
	EClass getProfileConstraint();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.SubProfileConstraint <em>Sub Profile Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Sub Profile Constraint</em>'.
	 * @see com.mmxlabs.models.lng.adp.SubProfileConstraint
	 * @generated
	 */
	EClass getSubProfileConstraint();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.MinCargoConstraint <em>Min Cargo Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Min Cargo Constraint</em>'.
	 * @see com.mmxlabs.models.lng.adp.MinCargoConstraint
	 * @generated
	 */
	EClass getMinCargoConstraint();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.MinCargoConstraint#getMinCargoes <em>Min Cargoes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Cargoes</em>'.
	 * @see com.mmxlabs.models.lng.adp.MinCargoConstraint#getMinCargoes()
	 * @see #getMinCargoConstraint()
	 * @generated
	 */
	EAttribute getMinCargoConstraint_MinCargoes();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.MinCargoConstraint#getIntervalType <em>Interval Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Interval Type</em>'.
	 * @see com.mmxlabs.models.lng.adp.MinCargoConstraint#getIntervalType()
	 * @see #getMinCargoConstraint()
	 * @generated
	 */
	EAttribute getMinCargoConstraint_IntervalType();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.MaxCargoConstraint <em>Max Cargo Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Max Cargo Constraint</em>'.
	 * @see com.mmxlabs.models.lng.adp.MaxCargoConstraint
	 * @generated
	 */
	EClass getMaxCargoConstraint();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.MaxCargoConstraint#getMaxCargoes <em>Max Cargoes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Cargoes</em>'.
	 * @see com.mmxlabs.models.lng.adp.MaxCargoConstraint#getMaxCargoes()
	 * @see #getMaxCargoConstraint()
	 * @generated
	 */
	EAttribute getMaxCargoConstraint_MaxCargoes();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.MaxCargoConstraint#getIntervalType <em>Interval Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Interval Type</em>'.
	 * @see com.mmxlabs.models.lng.adp.MaxCargoConstraint#getIntervalType()
	 * @see #getMaxCargoConstraint()
	 * @generated
	 */
	EAttribute getMaxCargoConstraint_IntervalType();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.FleetConstraint <em>Fleet Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Fleet Constraint</em>'.
	 * @see com.mmxlabs.models.lng.adp.FleetConstraint
	 * @generated
	 */
	EClass getFleetConstraint();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.TargetCargoesOnVesselConstraint <em>Target Cargoes On Vessel Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Target Cargoes On Vessel Constraint</em>'.
	 * @see com.mmxlabs.models.lng.adp.TargetCargoesOnVesselConstraint
	 * @generated
	 */
	EClass getTargetCargoesOnVesselConstraint();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.adp.TargetCargoesOnVesselConstraint#getVessel <em>Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel</em>'.
	 * @see com.mmxlabs.models.lng.adp.TargetCargoesOnVesselConstraint#getVessel()
	 * @see #getTargetCargoesOnVesselConstraint()
	 * @generated
	 */
	EReference getTargetCargoesOnVesselConstraint_Vessel();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.TargetCargoesOnVesselConstraint#getTargetNumberOfCargoes <em>Target Number Of Cargoes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Target Number Of Cargoes</em>'.
	 * @see com.mmxlabs.models.lng.adp.TargetCargoesOnVesselConstraint#getTargetNumberOfCargoes()
	 * @see #getTargetCargoesOnVesselConstraint()
	 * @generated
	 */
	EAttribute getTargetCargoesOnVesselConstraint_TargetNumberOfCargoes();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.TargetCargoesOnVesselConstraint#getIntervalType <em>Interval Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Interval Type</em>'.
	 * @see com.mmxlabs.models.lng.adp.TargetCargoesOnVesselConstraint#getIntervalType()
	 * @see #getTargetCargoesOnVesselConstraint()
	 * @generated
	 */
	EAttribute getTargetCargoesOnVesselConstraint_IntervalType();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.TargetCargoesOnVesselConstraint#getWeight <em>Weight</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Weight</em>'.
	 * @see com.mmxlabs.models.lng.adp.TargetCargoesOnVesselConstraint#getWeight()
	 * @see #getTargetCargoesOnVesselConstraint()
	 * @generated
	 */
	EAttribute getTargetCargoesOnVesselConstraint_Weight();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.MullEntityRow <em>Mull Entity Row</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Mull Entity Row</em>'.
	 * @see com.mmxlabs.models.lng.adp.MullEntityRow
	 * @generated
	 */
	EClass getMullEntityRow();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.adp.MullEntityRow#getEntity <em>Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Entity</em>'.
	 * @see com.mmxlabs.models.lng.adp.MullEntityRow#getEntity()
	 * @see #getMullEntityRow()
	 * @generated
	 */
	EReference getMullEntityRow_Entity();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.MullEntityRow#getInitialAllocation <em>Initial Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Initial Allocation</em>'.
	 * @see com.mmxlabs.models.lng.adp.MullEntityRow#getInitialAllocation()
	 * @see #getMullEntityRow()
	 * @generated
	 */
	EAttribute getMullEntityRow_InitialAllocation();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.MullEntityRow#getRelativeEntitlement <em>Relative Entitlement</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Relative Entitlement</em>'.
	 * @see com.mmxlabs.models.lng.adp.MullEntityRow#getRelativeEntitlement()
	 * @see #getMullEntityRow()
	 * @generated
	 */
	EAttribute getMullEntityRow_RelativeEntitlement();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.adp.MullEntityRow#getDesSalesMarketAllocationRows <em>Des Sales Market Allocation Rows</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Des Sales Market Allocation Rows</em>'.
	 * @see com.mmxlabs.models.lng.adp.MullEntityRow#getDesSalesMarketAllocationRows()
	 * @see #getMullEntityRow()
	 * @generated
	 */
	EReference getMullEntityRow_DesSalesMarketAllocationRows();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.adp.MullEntityRow#getSalesContractAllocationRows <em>Sales Contract Allocation Rows</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Sales Contract Allocation Rows</em>'.
	 * @see com.mmxlabs.models.lng.adp.MullEntityRow#getSalesContractAllocationRows()
	 * @see #getMullEntityRow()
	 * @generated
	 */
	EReference getMullEntityRow_SalesContractAllocationRows();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.DESSalesMarketAllocationRow <em>DES Sales Market Allocation Row</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>DES Sales Market Allocation Row</em>'.
	 * @see com.mmxlabs.models.lng.adp.DESSalesMarketAllocationRow
	 * @generated
	 */
	EClass getDESSalesMarketAllocationRow();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.adp.DESSalesMarketAllocationRow#getDesSalesMarket <em>Des Sales Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Des Sales Market</em>'.
	 * @see com.mmxlabs.models.lng.adp.DESSalesMarketAllocationRow#getDesSalesMarket()
	 * @see #getDESSalesMarketAllocationRow()
	 * @generated
	 */
	EReference getDESSalesMarketAllocationRow_DesSalesMarket();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.SalesContractAllocationRow <em>Sales Contract Allocation Row</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Sales Contract Allocation Row</em>'.
	 * @see com.mmxlabs.models.lng.adp.SalesContractAllocationRow
	 * @generated
	 */
	EClass getSalesContractAllocationRow();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.adp.SalesContractAllocationRow#getContract <em>Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Contract</em>'.
	 * @see com.mmxlabs.models.lng.adp.SalesContractAllocationRow#getContract()
	 * @see #getSalesContractAllocationRow()
	 * @generated
	 */
	EReference getSalesContractAllocationRow_Contract();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.MullProfile <em>Mull Profile</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Mull Profile</em>'.
	 * @see com.mmxlabs.models.lng.adp.MullProfile
	 * @generated
	 */
	EClass getMullProfile();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.MullProfile#getWindowSize <em>Window Size</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Window Size</em>'.
	 * @see com.mmxlabs.models.lng.adp.MullProfile#getWindowSize()
	 * @see #getMullProfile()
	 * @generated
	 */
	EAttribute getMullProfile_WindowSize();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.MullProfile#getVolumeFlex <em>Volume Flex</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volume Flex</em>'.
	 * @see com.mmxlabs.models.lng.adp.MullProfile#getVolumeFlex()
	 * @see #getMullProfile()
	 * @generated
	 */
	EAttribute getMullProfile_VolumeFlex();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.adp.MullProfile#getInventories <em>Inventories</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Inventories</em>'.
	 * @see com.mmxlabs.models.lng.adp.MullProfile#getInventories()
	 * @see #getMullProfile()
	 * @generated
	 */
	EReference getMullProfile_Inventories();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.MullProfile#getFullCargoLotValue <em>Full Cargo Lot Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Full Cargo Lot Value</em>'.
	 * @see com.mmxlabs.models.lng.adp.MullProfile#getFullCargoLotValue()
	 * @see #getMullProfile()
	 * @generated
	 */
	EAttribute getMullProfile_FullCargoLotValue();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.adp.MullProfile#getCargoesToKeep <em>Cargoes To Keep</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Cargoes To Keep</em>'.
	 * @see com.mmxlabs.models.lng.adp.MullProfile#getCargoesToKeep()
	 * @see #getMullProfile()
	 * @generated
	 */
	EReference getMullProfile_CargoesToKeep();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.MullSubprofile <em>Mull Subprofile</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Mull Subprofile</em>'.
	 * @see com.mmxlabs.models.lng.adp.MullSubprofile
	 * @generated
	 */
	EClass getMullSubprofile();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.adp.MullSubprofile#getInventory <em>Inventory</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Inventory</em>'.
	 * @see com.mmxlabs.models.lng.adp.MullSubprofile#getInventory()
	 * @see #getMullSubprofile()
	 * @generated
	 */
	EReference getMullSubprofile_Inventory();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.adp.MullSubprofile#getEntityTable <em>Entity Table</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Entity Table</em>'.
	 * @see com.mmxlabs.models.lng.adp.MullSubprofile#getEntityTable()
	 * @see #getMullSubprofile()
	 * @generated
	 */
	EReference getMullSubprofile_EntityTable();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.MullAllocationRow <em>Mull Allocation Row</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Mull Allocation Row</em>'.
	 * @see com.mmxlabs.models.lng.adp.MullAllocationRow
	 * @generated
	 */
	EClass getMullAllocationRow();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.MullAllocationRow#getWeight <em>Weight</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Weight</em>'.
	 * @see com.mmxlabs.models.lng.adp.MullAllocationRow#getWeight()
	 * @see #getMullAllocationRow()
	 * @generated
	 */
	EAttribute getMullAllocationRow_Weight();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.adp.MullAllocationRow#getVessels <em>Vessels</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Vessels</em>'.
	 * @see com.mmxlabs.models.lng.adp.MullAllocationRow#getVessels()
	 * @see #getMullAllocationRow()
	 * @generated
	 */
	EReference getMullAllocationRow_Vessels();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.MullCargoWrapper <em>Mull Cargo Wrapper</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Mull Cargo Wrapper</em>'.
	 * @see com.mmxlabs.models.lng.adp.MullCargoWrapper
	 * @generated
	 */
	EClass getMullCargoWrapper();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.adp.MullCargoWrapper#getLoadSlot <em>Load Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Load Slot</em>'.
	 * @see com.mmxlabs.models.lng.adp.MullCargoWrapper#getLoadSlot()
	 * @see #getMullCargoWrapper()
	 * @generated
	 */
	EReference getMullCargoWrapper_LoadSlot();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.adp.MullCargoWrapper#getDischargeSlot <em>Discharge Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Discharge Slot</em>'.
	 * @see com.mmxlabs.models.lng.adp.MullCargoWrapper#getDischargeSlot()
	 * @see #getMullCargoWrapper()
	 * @generated
	 */
	EReference getMullCargoWrapper_DischargeSlot();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.adp.IntervalType <em>Interval Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Interval Type</em>'.
	 * @see com.mmxlabs.models.lng.adp.IntervalType
	 * @generated
	 */
	EEnum getIntervalType();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.adp.LNGVolumeUnit <em>LNG Volume Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>LNG Volume Unit</em>'.
	 * @see com.mmxlabs.models.lng.adp.LNGVolumeUnit
	 * @generated
	 */
	EEnum getLNGVolumeUnit();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ADPFactory getADPFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.ADPModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.ADPModelImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getADPModel()
		 * @generated
		 */
		EClass ADP_MODEL = eINSTANCE.getADPModel();

		/**
		 * The meta object literal for the '<em><b>Year Start</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ADP_MODEL__YEAR_START = eINSTANCE.getADPModel_YearStart();

		/**
		 * The meta object literal for the '<em><b>Year End</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ADP_MODEL__YEAR_END = eINSTANCE.getADPModel_YearEnd();

		/**
		 * The meta object literal for the '<em><b>Purchase Contract Profiles</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ADP_MODEL__PURCHASE_CONTRACT_PROFILES = eINSTANCE.getADPModel_PurchaseContractProfiles();

		/**
		 * The meta object literal for the '<em><b>Sales Contract Profiles</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ADP_MODEL__SALES_CONTRACT_PROFILES = eINSTANCE.getADPModel_SalesContractProfiles();

		/**
		 * The meta object literal for the '<em><b>Fleet Profile</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ADP_MODEL__FLEET_PROFILE = eINSTANCE.getADPModel_FleetProfile();

		/**
		 * The meta object literal for the '<em><b>Mull Profile</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ADP_MODEL__MULL_PROFILE = eINSTANCE.getADPModel_MullProfile();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.FleetProfileImpl <em>Fleet Profile</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.FleetProfileImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getFleetProfile()
		 * @generated
		 */
		EClass FLEET_PROFILE = eINSTANCE.getFleetProfile();

		/**
		 * The meta object literal for the '<em><b>Constraints</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FLEET_PROFILE__CONSTRAINTS = eINSTANCE.getFleetProfile_Constraints();

		/**
		 * The meta object literal for the '<em><b>Default Nominal Market</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FLEET_PROFILE__DEFAULT_NOMINAL_MARKET = eINSTANCE.getFleetProfile_DefaultNominalMarket();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.ContractProfileImpl <em>Contract Profile</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.ContractProfileImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getContractProfile()
		 * @generated
		 */
		EClass CONTRACT_PROFILE = eINSTANCE.getContractProfile();

		/**
		 * The meta object literal for the '<em><b>Contract</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTRACT_PROFILE__CONTRACT = eINSTANCE.getContractProfile_Contract();

		/**
		 * The meta object literal for the '<em><b>Contract Code</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTRACT_PROFILE__CONTRACT_CODE = eINSTANCE.getContractProfile_ContractCode();

		/**
		 * The meta object literal for the '<em><b>Custom</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTRACT_PROFILE__CUSTOM = eINSTANCE.getContractProfile_Custom();

		/**
		 * The meta object literal for the '<em><b>Enabled</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTRACT_PROFILE__ENABLED = eINSTANCE.getContractProfile_Enabled();

		/**
		 * The meta object literal for the '<em><b>Total Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTRACT_PROFILE__TOTAL_VOLUME = eINSTANCE.getContractProfile_TotalVolume();

		/**
		 * The meta object literal for the '<em><b>Volume Unit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTRACT_PROFILE__VOLUME_UNIT = eINSTANCE.getContractProfile_VolumeUnit();

		/**
		 * The meta object literal for the '<em><b>Sub Profiles</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTRACT_PROFILE__SUB_PROFILES = eINSTANCE.getContractProfile_SubProfiles();

		/**
		 * The meta object literal for the '<em><b>Constraints</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTRACT_PROFILE__CONSTRAINTS = eINSTANCE.getContractProfile_Constraints();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.DistributionModelImpl <em>Distribution Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.DistributionModelImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getDistributionModel()
		 * @generated
		 */
		EClass DISTRIBUTION_MODEL = eINSTANCE.getDistributionModel();

		/**
		 * The meta object literal for the '<em><b>Volume Per Cargo</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DISTRIBUTION_MODEL__VOLUME_PER_CARGO = eINSTANCE.getDistributionModel_VolumePerCargo();

		/**
		 * The meta object literal for the '<em><b>Volume Unit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DISTRIBUTION_MODEL__VOLUME_UNIT = eINSTANCE.getDistributionModel_VolumeUnit();

		/**
		 * The meta object literal for the '<em><b>Get Model Or Contract Volume Per Cargo</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DISTRIBUTION_MODEL___GET_MODEL_OR_CONTRACT_VOLUME_PER_CARGO = eINSTANCE.getDistributionModel__GetModelOrContractVolumePerCargo();

		/**
		 * The meta object literal for the '<em><b>Get Model Or Contract Volume Unit</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DISTRIBUTION_MODEL___GET_MODEL_OR_CONTRACT_VOLUME_UNIT = eINSTANCE.getDistributionModel__GetModelOrContractVolumeUnit();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.PeriodDistributionProfileConstraintImpl <em>Period Distribution Profile Constraint</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.PeriodDistributionProfileConstraintImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getPeriodDistributionProfileConstraint()
		 * @generated
		 */
		EClass PERIOD_DISTRIBUTION_PROFILE_CONSTRAINT = eINSTANCE.getPeriodDistributionProfileConstraint();

		/**
		 * The meta object literal for the '<em><b>Distributions</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PERIOD_DISTRIBUTION_PROFILE_CONSTRAINT__DISTRIBUTIONS = eINSTANCE.getPeriodDistributionProfileConstraint_Distributions();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.PeriodDistributionImpl <em>Period Distribution</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.PeriodDistributionImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getPeriodDistribution()
		 * @generated
		 */
		EClass PERIOD_DISTRIBUTION = eINSTANCE.getPeriodDistribution();

		/**
		 * The meta object literal for the '<em><b>Range</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PERIOD_DISTRIBUTION__RANGE = eINSTANCE.getPeriodDistribution_Range();

		/**
		 * The meta object literal for the '<em><b>Min Cargoes</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PERIOD_DISTRIBUTION__MIN_CARGOES = eINSTANCE.getPeriodDistribution_MinCargoes();

		/**
		 * The meta object literal for the '<em><b>Max Cargoes</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PERIOD_DISTRIBUTION__MAX_CARGOES = eINSTANCE.getPeriodDistribution_MaxCargoes();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.CargoSizeDistributionModelImpl <em>Cargo Size Distribution Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.CargoSizeDistributionModelImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getCargoSizeDistributionModel()
		 * @generated
		 */
		EClass CARGO_SIZE_DISTRIBUTION_MODEL = eINSTANCE.getCargoSizeDistributionModel();

		/**
		 * The meta object literal for the '<em><b>Exact</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO_SIZE_DISTRIBUTION_MODEL__EXACT = eINSTANCE.getCargoSizeDistributionModel_Exact();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.CargoNumberDistributionModelImpl <em>Cargo Number Distribution Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.CargoNumberDistributionModelImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getCargoNumberDistributionModel()
		 * @generated
		 */
		EClass CARGO_NUMBER_DISTRIBUTION_MODEL = eINSTANCE.getCargoNumberDistributionModel();

		/**
		 * The meta object literal for the '<em><b>Number Of Cargoes</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO_NUMBER_DISTRIBUTION_MODEL__NUMBER_OF_CARGOES = eINSTANCE.getCargoNumberDistributionModel_NumberOfCargoes();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.PurchaseContractProfileImpl <em>Purchase Contract Profile</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.PurchaseContractProfileImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getPurchaseContractProfile()
		 * @generated
		 */
		EClass PURCHASE_CONTRACT_PROFILE = eINSTANCE.getPurchaseContractProfile();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.SalesContractProfileImpl <em>Sales Contract Profile</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.SalesContractProfileImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getSalesContractProfile()
		 * @generated
		 */
		EClass SALES_CONTRACT_PROFILE = eINSTANCE.getSalesContractProfile();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.SubContractProfileImpl <em>Sub Contract Profile</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.SubContractProfileImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getSubContractProfile()
		 * @generated
		 */
		EClass SUB_CONTRACT_PROFILE = eINSTANCE.getSubContractProfile();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SUB_CONTRACT_PROFILE__NAME = eINSTANCE.getSubContractProfile_Name();

		/**
		 * The meta object literal for the '<em><b>Contract Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SUB_CONTRACT_PROFILE__CONTRACT_TYPE = eINSTANCE.getSubContractProfile_ContractType();

		/**
		 * The meta object literal for the '<em><b>Distribution Model</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL = eINSTANCE.getSubContractProfile_DistributionModel();

		/**
		 * The meta object literal for the '<em><b>Slot Template Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SUB_CONTRACT_PROFILE__SLOT_TEMPLATE_ID = eINSTANCE.getSubContractProfile_SlotTemplateId();

		/**
		 * The meta object literal for the '<em><b>Custom Attribs</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SUB_CONTRACT_PROFILE__CUSTOM_ATTRIBS = eINSTANCE.getSubContractProfile_CustomAttribs();

		/**
		 * The meta object literal for the '<em><b>Nominated Vessel</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SUB_CONTRACT_PROFILE__NOMINATED_VESSEL = eINSTANCE.getSubContractProfile_NominatedVessel();

		/**
		 * The meta object literal for the '<em><b>Shipping Days</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SUB_CONTRACT_PROFILE__SHIPPING_DAYS = eINSTANCE.getSubContractProfile_ShippingDays();

		/**
		 * The meta object literal for the '<em><b>Constraints</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SUB_CONTRACT_PROFILE__CONSTRAINTS = eINSTANCE.getSubContractProfile_Constraints();

		/**
		 * The meta object literal for the '<em><b>Window Size</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SUB_CONTRACT_PROFILE__WINDOW_SIZE = eINSTANCE.getSubContractProfile_WindowSize();

		/**
		 * The meta object literal for the '<em><b>Window Size Units</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SUB_CONTRACT_PROFILE__WINDOW_SIZE_UNITS = eINSTANCE.getSubContractProfile_WindowSizeUnits();

		/**
		 * The meta object literal for the '<em><b>Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SUB_CONTRACT_PROFILE__PORT = eINSTANCE.getSubContractProfile_Port();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.CustomSubProfileAttributes <em>Custom Sub Profile Attributes</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.CustomSubProfileAttributes
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getCustomSubProfileAttributes()
		 * @generated
		 */
		EClass CUSTOM_SUB_PROFILE_ATTRIBUTES = eINSTANCE.getCustomSubProfileAttributes();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.CargoByQuarterDistributionModelImpl <em>Cargo By Quarter Distribution Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.CargoByQuarterDistributionModelImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getCargoByQuarterDistributionModel()
		 * @generated
		 */
		EClass CARGO_BY_QUARTER_DISTRIBUTION_MODEL = eINSTANCE.getCargoByQuarterDistributionModel();

		/**
		 * The meta object literal for the '<em><b>Q1</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q1 = eINSTANCE.getCargoByQuarterDistributionModel_Q1();

		/**
		 * The meta object literal for the '<em><b>Q2</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q2 = eINSTANCE.getCargoByQuarterDistributionModel_Q2();

		/**
		 * The meta object literal for the '<em><b>Q3</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q3 = eINSTANCE.getCargoByQuarterDistributionModel_Q3();

		/**
		 * The meta object literal for the '<em><b>Q4</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q4 = eINSTANCE.getCargoByQuarterDistributionModel_Q4();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.CargoIntervalDistributionModelImpl <em>Cargo Interval Distribution Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.CargoIntervalDistributionModelImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getCargoIntervalDistributionModel()
		 * @generated
		 */
		EClass CARGO_INTERVAL_DISTRIBUTION_MODEL = eINSTANCE.getCargoIntervalDistributionModel();

		/**
		 * The meta object literal for the '<em><b>Quantity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO_INTERVAL_DISTRIBUTION_MODEL__QUANTITY = eINSTANCE.getCargoIntervalDistributionModel_Quantity();

		/**
		 * The meta object literal for the '<em><b>Interval Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO_INTERVAL_DISTRIBUTION_MODEL__INTERVAL_TYPE = eINSTANCE.getCargoIntervalDistributionModel_IntervalType();

		/**
		 * The meta object literal for the '<em><b>Spacing</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO_INTERVAL_DISTRIBUTION_MODEL__SPACING = eINSTANCE.getCargoIntervalDistributionModel_Spacing();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.PreDefinedDistributionModelImpl <em>Pre Defined Distribution Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.PreDefinedDistributionModelImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getPreDefinedDistributionModel()
		 * @generated
		 */
		EClass PRE_DEFINED_DISTRIBUTION_MODEL = eINSTANCE.getPreDefinedDistributionModel();

		/**
		 * The meta object literal for the '<em><b>Dates</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PRE_DEFINED_DISTRIBUTION_MODEL__DATES = eINSTANCE.getPreDefinedDistributionModel_Dates();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.PreDefinedDateImpl <em>Pre Defined Date</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.PreDefinedDateImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getPreDefinedDate()
		 * @generated
		 */
		EClass PRE_DEFINED_DATE = eINSTANCE.getPreDefinedDate();

		/**
		 * The meta object literal for the '<em><b>Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PRE_DEFINED_DATE__DATE = eINSTANCE.getPreDefinedDate_Date();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.FlowTypeImpl <em>Flow Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.FlowTypeImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getFlowType()
		 * @generated
		 */
		EClass FLOW_TYPE = eINSTANCE.getFlowType();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.SupplyFromFlowImpl <em>Supply From Flow</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.SupplyFromFlowImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getSupplyFromFlow()
		 * @generated
		 */
		EClass SUPPLY_FROM_FLOW = eINSTANCE.getSupplyFromFlow();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.DeliverToFlowImpl <em>Deliver To Flow</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.DeliverToFlowImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getDeliverToFlow()
		 * @generated
		 */
		EClass DELIVER_TO_FLOW = eINSTANCE.getDeliverToFlow();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.SupplyFromProfileFlowImpl <em>Supply From Profile Flow</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.SupplyFromProfileFlowImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getSupplyFromProfileFlow()
		 * @generated
		 */
		EClass SUPPLY_FROM_PROFILE_FLOW = eINSTANCE.getSupplyFromProfileFlow();

		/**
		 * The meta object literal for the '<em><b>Profile</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SUPPLY_FROM_PROFILE_FLOW__PROFILE = eINSTANCE.getSupplyFromProfileFlow_Profile();

		/**
		 * The meta object literal for the '<em><b>Sub Profile</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SUPPLY_FROM_PROFILE_FLOW__SUB_PROFILE = eINSTANCE.getSupplyFromProfileFlow_SubProfile();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.DeliverToProfileFlowImpl <em>Deliver To Profile Flow</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.DeliverToProfileFlowImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getDeliverToProfileFlow()
		 * @generated
		 */
		EClass DELIVER_TO_PROFILE_FLOW = eINSTANCE.getDeliverToProfileFlow();

		/**
		 * The meta object literal for the '<em><b>Profile</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DELIVER_TO_PROFILE_FLOW__PROFILE = eINSTANCE.getDeliverToProfileFlow_Profile();

		/**
		 * The meta object literal for the '<em><b>Sub Profile</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DELIVER_TO_PROFILE_FLOW__SUB_PROFILE = eINSTANCE.getDeliverToProfileFlow_SubProfile();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.SupplyFromSpotFlowImpl <em>Supply From Spot Flow</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.SupplyFromSpotFlowImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getSupplyFromSpotFlow()
		 * @generated
		 */
		EClass SUPPLY_FROM_SPOT_FLOW = eINSTANCE.getSupplyFromSpotFlow();

		/**
		 * The meta object literal for the '<em><b>Market</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SUPPLY_FROM_SPOT_FLOW__MARKET = eINSTANCE.getSupplyFromSpotFlow_Market();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.DeliverToSpotFlowImpl <em>Deliver To Spot Flow</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.DeliverToSpotFlowImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getDeliverToSpotFlow()
		 * @generated
		 */
		EClass DELIVER_TO_SPOT_FLOW = eINSTANCE.getDeliverToSpotFlow();

		/**
		 * The meta object literal for the '<em><b>Market</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DELIVER_TO_SPOT_FLOW__MARKET = eINSTANCE.getDeliverToSpotFlow_Market();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.ProfileVesselRestrictionImpl <em>Profile Vessel Restriction</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.ProfileVesselRestrictionImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getProfileVesselRestriction()
		 * @generated
		 */
		EClass PROFILE_VESSEL_RESTRICTION = eINSTANCE.getProfileVesselRestriction();

		/**
		 * The meta object literal for the '<em><b>Vessels</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROFILE_VESSEL_RESTRICTION__VESSELS = eINSTANCE.getProfileVesselRestriction_Vessels();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.ShippingOptionImpl <em>Shipping Option</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.ShippingOptionImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getShippingOption()
		 * @generated
		 */
		EClass SHIPPING_OPTION = eINSTANCE.getShippingOption();

		/**
		 * The meta object literal for the '<em><b>Vessel Assignment Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SHIPPING_OPTION__VESSEL_ASSIGNMENT_TYPE = eINSTANCE.getShippingOption_VesselAssignmentType();

		/**
		 * The meta object literal for the '<em><b>Spot Index</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SHIPPING_OPTION__SPOT_INDEX = eINSTANCE.getShippingOption_SpotIndex();

		/**
		 * The meta object literal for the '<em><b>Vessel</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SHIPPING_OPTION__VESSEL = eINSTANCE.getShippingOption_Vessel();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.ProfileConstraintImpl <em>Profile Constraint</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.ProfileConstraintImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getProfileConstraint()
		 * @generated
		 */
		EClass PROFILE_CONSTRAINT = eINSTANCE.getProfileConstraint();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.SubProfileConstraintImpl <em>Sub Profile Constraint</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.SubProfileConstraintImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getSubProfileConstraint()
		 * @generated
		 */
		EClass SUB_PROFILE_CONSTRAINT = eINSTANCE.getSubProfileConstraint();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.MinCargoConstraintImpl <em>Min Cargo Constraint</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.MinCargoConstraintImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getMinCargoConstraint()
		 * @generated
		 */
		EClass MIN_CARGO_CONSTRAINT = eINSTANCE.getMinCargoConstraint();

		/**
		 * The meta object literal for the '<em><b>Min Cargoes</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MIN_CARGO_CONSTRAINT__MIN_CARGOES = eINSTANCE.getMinCargoConstraint_MinCargoes();

		/**
		 * The meta object literal for the '<em><b>Interval Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MIN_CARGO_CONSTRAINT__INTERVAL_TYPE = eINSTANCE.getMinCargoConstraint_IntervalType();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.MaxCargoConstraintImpl <em>Max Cargo Constraint</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.MaxCargoConstraintImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getMaxCargoConstraint()
		 * @generated
		 */
		EClass MAX_CARGO_CONSTRAINT = eINSTANCE.getMaxCargoConstraint();

		/**
		 * The meta object literal for the '<em><b>Max Cargoes</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MAX_CARGO_CONSTRAINT__MAX_CARGOES = eINSTANCE.getMaxCargoConstraint_MaxCargoes();

		/**
		 * The meta object literal for the '<em><b>Interval Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MAX_CARGO_CONSTRAINT__INTERVAL_TYPE = eINSTANCE.getMaxCargoConstraint_IntervalType();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.FleetConstraint <em>Fleet Constraint</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.FleetConstraint
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getFleetConstraint()
		 * @generated
		 */
		EClass FLEET_CONSTRAINT = eINSTANCE.getFleetConstraint();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.TargetCargoesOnVesselConstraintImpl <em>Target Cargoes On Vessel Constraint</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.TargetCargoesOnVesselConstraintImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getTargetCargoesOnVesselConstraint()
		 * @generated
		 */
		EClass TARGET_CARGOES_ON_VESSEL_CONSTRAINT = eINSTANCE.getTargetCargoesOnVesselConstraint();

		/**
		 * The meta object literal for the '<em><b>Vessel</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TARGET_CARGOES_ON_VESSEL_CONSTRAINT__VESSEL = eINSTANCE.getTargetCargoesOnVesselConstraint_Vessel();

		/**
		 * The meta object literal for the '<em><b>Target Number Of Cargoes</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TARGET_CARGOES_ON_VESSEL_CONSTRAINT__TARGET_NUMBER_OF_CARGOES = eINSTANCE.getTargetCargoesOnVesselConstraint_TargetNumberOfCargoes();

		/**
		 * The meta object literal for the '<em><b>Interval Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TARGET_CARGOES_ON_VESSEL_CONSTRAINT__INTERVAL_TYPE = eINSTANCE.getTargetCargoesOnVesselConstraint_IntervalType();

		/**
		 * The meta object literal for the '<em><b>Weight</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TARGET_CARGOES_ON_VESSEL_CONSTRAINT__WEIGHT = eINSTANCE.getTargetCargoesOnVesselConstraint_Weight();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.MullEntityRowImpl <em>Mull Entity Row</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.MullEntityRowImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getMullEntityRow()
		 * @generated
		 */
		EClass MULL_ENTITY_ROW = eINSTANCE.getMullEntityRow();

		/**
		 * The meta object literal for the '<em><b>Entity</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MULL_ENTITY_ROW__ENTITY = eINSTANCE.getMullEntityRow_Entity();

		/**
		 * The meta object literal for the '<em><b>Initial Allocation</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MULL_ENTITY_ROW__INITIAL_ALLOCATION = eINSTANCE.getMullEntityRow_InitialAllocation();

		/**
		 * The meta object literal for the '<em><b>Relative Entitlement</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MULL_ENTITY_ROW__RELATIVE_ENTITLEMENT = eINSTANCE.getMullEntityRow_RelativeEntitlement();

		/**
		 * The meta object literal for the '<em><b>Des Sales Market Allocation Rows</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MULL_ENTITY_ROW__DES_SALES_MARKET_ALLOCATION_ROWS = eINSTANCE.getMullEntityRow_DesSalesMarketAllocationRows();

		/**
		 * The meta object literal for the '<em><b>Sales Contract Allocation Rows</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MULL_ENTITY_ROW__SALES_CONTRACT_ALLOCATION_ROWS = eINSTANCE.getMullEntityRow_SalesContractAllocationRows();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.DESSalesMarketAllocationRowImpl <em>DES Sales Market Allocation Row</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.DESSalesMarketAllocationRowImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getDESSalesMarketAllocationRow()
		 * @generated
		 */
		EClass DES_SALES_MARKET_ALLOCATION_ROW = eINSTANCE.getDESSalesMarketAllocationRow();

		/**
		 * The meta object literal for the '<em><b>Des Sales Market</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DES_SALES_MARKET_ALLOCATION_ROW__DES_SALES_MARKET = eINSTANCE.getDESSalesMarketAllocationRow_DesSalesMarket();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.SalesContractAllocationRowImpl <em>Sales Contract Allocation Row</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.SalesContractAllocationRowImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getSalesContractAllocationRow()
		 * @generated
		 */
		EClass SALES_CONTRACT_ALLOCATION_ROW = eINSTANCE.getSalesContractAllocationRow();

		/**
		 * The meta object literal for the '<em><b>Contract</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SALES_CONTRACT_ALLOCATION_ROW__CONTRACT = eINSTANCE.getSalesContractAllocationRow_Contract();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.MullProfileImpl <em>Mull Profile</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.MullProfileImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getMullProfile()
		 * @generated
		 */
		EClass MULL_PROFILE = eINSTANCE.getMullProfile();

		/**
		 * The meta object literal for the '<em><b>Window Size</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MULL_PROFILE__WINDOW_SIZE = eINSTANCE.getMullProfile_WindowSize();

		/**
		 * The meta object literal for the '<em><b>Volume Flex</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MULL_PROFILE__VOLUME_FLEX = eINSTANCE.getMullProfile_VolumeFlex();

		/**
		 * The meta object literal for the '<em><b>Inventories</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MULL_PROFILE__INVENTORIES = eINSTANCE.getMullProfile_Inventories();

		/**
		 * The meta object literal for the '<em><b>Full Cargo Lot Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MULL_PROFILE__FULL_CARGO_LOT_VALUE = eINSTANCE.getMullProfile_FullCargoLotValue();

		/**
		 * The meta object literal for the '<em><b>Cargoes To Keep</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MULL_PROFILE__CARGOES_TO_KEEP = eINSTANCE.getMullProfile_CargoesToKeep();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.MullSubprofileImpl <em>Mull Subprofile</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.MullSubprofileImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getMullSubprofile()
		 * @generated
		 */
		EClass MULL_SUBPROFILE = eINSTANCE.getMullSubprofile();

		/**
		 * The meta object literal for the '<em><b>Inventory</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MULL_SUBPROFILE__INVENTORY = eINSTANCE.getMullSubprofile_Inventory();

		/**
		 * The meta object literal for the '<em><b>Entity Table</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MULL_SUBPROFILE__ENTITY_TABLE = eINSTANCE.getMullSubprofile_EntityTable();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.MullAllocationRowImpl <em>Mull Allocation Row</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.MullAllocationRowImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getMullAllocationRow()
		 * @generated
		 */
		EClass MULL_ALLOCATION_ROW = eINSTANCE.getMullAllocationRow();

		/**
		 * The meta object literal for the '<em><b>Weight</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MULL_ALLOCATION_ROW__WEIGHT = eINSTANCE.getMullAllocationRow_Weight();

		/**
		 * The meta object literal for the '<em><b>Vessels</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MULL_ALLOCATION_ROW__VESSELS = eINSTANCE.getMullAllocationRow_Vessels();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.MullCargoWrapperImpl <em>Mull Cargo Wrapper</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.MullCargoWrapperImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getMullCargoWrapper()
		 * @generated
		 */
		EClass MULL_CARGO_WRAPPER = eINSTANCE.getMullCargoWrapper();

		/**
		 * The meta object literal for the '<em><b>Load Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MULL_CARGO_WRAPPER__LOAD_SLOT = eINSTANCE.getMullCargoWrapper_LoadSlot();

		/**
		 * The meta object literal for the '<em><b>Discharge Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MULL_CARGO_WRAPPER__DISCHARGE_SLOT = eINSTANCE.getMullCargoWrapper_DischargeSlot();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.IntervalType <em>Interval Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.IntervalType
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getIntervalType()
		 * @generated
		 */
		EEnum INTERVAL_TYPE = eINSTANCE.getIntervalType();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.LNGVolumeUnit <em>LNG Volume Unit</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.LNGVolumeUnit
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getLNGVolumeUnit()
		 * @generated
		 */
		EEnum LNG_VOLUME_UNIT = eINSTANCE.getLNGVolumeUnit();

	}

} //ADPPackage
