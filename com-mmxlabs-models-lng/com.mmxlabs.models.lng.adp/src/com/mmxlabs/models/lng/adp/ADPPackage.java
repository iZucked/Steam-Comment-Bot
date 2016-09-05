/**
 */
package com.mmxlabs.models.lng.adp;

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
	 * The feature id for the '<em><b>Year Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADP_MODEL__YEAR_START = 0;

	/**
	 * The feature id for the '<em><b>Purchase Contract Profiles</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADP_MODEL__PURCHASE_CONTRACT_PROFILES = 1;

	/**
	 * The feature id for the '<em><b>Sales Contract Profiles</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADP_MODEL__SALES_CONTRACT_PROFILES = 2;

	/**
	 * The feature id for the '<em><b>Binding Rules</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADP_MODEL__BINDING_RULES = 3;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADP_MODEL_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADP_MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.ContractProfileImpl <em>Contract Profile</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.ContractProfileImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getContractProfile()
	 * @generated
	 */
	int CONTRACT_PROFILE = 1;

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
	 * The feature id for the '<em><b>Distribution Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_PROFILE__DISTRIBUTION_MODEL = 6;

	/**
	 * The feature id for the '<em><b>Sub Profiles</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_PROFILE__SUB_PROFILES = 7;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.DistributionModel <em>Distribution Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.DistributionModel
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getDistributionModel()
	 * @generated
	 */
	int DISTRIBUTION_MODEL = 6;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.CargoSizeDistributionModelImpl <em>Cargo Size Distribution Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.CargoSizeDistributionModelImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getCargoSizeDistributionModel()
	 * @generated
	 */
	int CARGO_SIZE_DISTRIBUTION_MODEL = 7;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.CargoNumberDistributionModelImpl <em>Cargo Number Distribution Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.CargoNumberDistributionModelImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getCargoNumberDistributionModel()
	 * @generated
	 */
	int CARGO_NUMBER_DISTRIBUTION_MODEL = 8;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.PurchaseContractProfileImpl <em>Purchase Contract Profile</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.PurchaseContractProfileImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getPurchaseContractProfile()
	 * @generated
	 */
	int PURCHASE_CONTRACT_PROFILE = 2;

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
	 * The feature id for the '<em><b>Distribution Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT_PROFILE__DISTRIBUTION_MODEL = CONTRACT_PROFILE__DISTRIBUTION_MODEL;

	/**
	 * The feature id for the '<em><b>Sub Profiles</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT_PROFILE__SUB_PROFILES = CONTRACT_PROFILE__SUB_PROFILES;

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
	int SALES_CONTRACT_PROFILE = 3;

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
	 * The feature id for the '<em><b>Distribution Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT_PROFILE__DISTRIBUTION_MODEL = CONTRACT_PROFILE__DISTRIBUTION_MODEL;

	/**
	 * The feature id for the '<em><b>Sub Profiles</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT_PROFILE__SUB_PROFILES = CONTRACT_PROFILE__SUB_PROFILES;

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
	int SUB_CONTRACT_PROFILE = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUB_CONTRACT_PROFILE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Distribution Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL = 1;

	/**
	 * The feature id for the '<em><b>Slot Template Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUB_CONTRACT_PROFILE__SLOT_TEMPLATE_ID = 2;

	/**
	 * The feature id for the '<em><b>Custom Attribs</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUB_CONTRACT_PROFILE__CUSTOM_ATTRIBS = 3;

	/**
	 * The feature id for the '<em><b>Slots</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUB_CONTRACT_PROFILE__SLOTS = 4;

	/**
	 * The number of structural features of the '<em>Sub Contract Profile</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUB_CONTRACT_PROFILE_FEATURE_COUNT = 5;

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
	int CUSTOM_SUB_PROFILE_ATTRIBUTES = 5;

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
	 * The number of structural features of the '<em>Distribution Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISTRIBUTION_MODEL_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Distribution Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISTRIBUTION_MODEL_OPERATION_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Cargo Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_SIZE_DISTRIBUTION_MODEL__CARGO_SIZE = DISTRIBUTION_MODEL_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Exact</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_SIZE_DISTRIBUTION_MODEL__EXACT = DISTRIBUTION_MODEL_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Cargo Size Distribution Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_SIZE_DISTRIBUTION_MODEL_FEATURE_COUNT = DISTRIBUTION_MODEL_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Cargo Size Distribution Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_SIZE_DISTRIBUTION_MODEL_OPERATION_COUNT = DISTRIBUTION_MODEL_OPERATION_COUNT + 0;

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
	 * The number of operations of the '<em>Cargo Number Distribution Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_NUMBER_DISTRIBUTION_MODEL_OPERATION_COUNT = DISTRIBUTION_MODEL_OPERATION_COUNT + 0;


	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.CargoByQuarterDistributionModelImpl <em>Cargo By Quarter Distribution Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.CargoByQuarterDistributionModelImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getCargoByQuarterDistributionModel()
	 * @generated
	 */
	int CARGO_BY_QUARTER_DISTRIBUTION_MODEL = 9;

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
	 * The number of operations of the '<em>Cargo By Quarter Distribution Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_BY_QUARTER_DISTRIBUTION_MODEL_OPERATION_COUNT = DISTRIBUTION_MODEL_OPERATION_COUNT + 0;


	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.CargoIntervalDistributionModelImpl <em>Cargo Interval Distribution Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.CargoIntervalDistributionModelImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getCargoIntervalDistributionModel()
	 * @generated
	 */
	int CARGO_INTERVAL_DISTRIBUTION_MODEL = 10;

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
	 * The number of operations of the '<em>Cargo Interval Distribution Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_INTERVAL_DISTRIBUTION_MODEL_OPERATION_COUNT = DISTRIBUTION_MODEL_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.BindingRuleImpl <em>Binding Rule</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.BindingRuleImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getBindingRule()
	 * @generated
	 */
	int BINDING_RULE = 11;

	/**
	 * The feature id for the '<em><b>Profile</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BINDING_RULE__PROFILE = 0;

	/**
	 * The feature id for the '<em><b>Sub Profile</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BINDING_RULE__SUB_PROFILE = 1;

	/**
	 * The feature id for the '<em><b>Flow Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BINDING_RULE__FLOW_TYPE = 2;

	/**
	 * The feature id for the '<em><b>Shipping Option</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BINDING_RULE__SHIPPING_OPTION = 3;

	/**
	 * The number of structural features of the '<em>Binding Rule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BINDING_RULE_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Binding Rule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BINDING_RULE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.FlowTypeImpl <em>Flow Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.FlowTypeImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getFlowType()
	 * @generated
	 */
	int FLOW_TYPE = 12;

	/**
	 * The number of structural features of the '<em>Flow Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOW_TYPE_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Flow Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOW_TYPE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.SupplyFromFlowImpl <em>Supply From Flow</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.SupplyFromFlowImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getSupplyFromFlow()
	 * @generated
	 */
	int SUPPLY_FROM_FLOW = 13;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.DeliverToFlowImpl <em>Deliver To Flow</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.DeliverToFlowImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getDeliverToFlow()
	 * @generated
	 */
	int DELIVER_TO_FLOW = 14;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.SupplyFromProfileFlowImpl <em>Supply From Profile Flow</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.SupplyFromProfileFlowImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getSupplyFromProfileFlow()
	 * @generated
	 */
	int SUPPLY_FROM_PROFILE_FLOW = 15;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.DeliverToProfileFlowImpl <em>Deliver To Profile Flow</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.DeliverToProfileFlowImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getDeliverToProfileFlow()
	 * @generated
	 */
	int DELIVER_TO_PROFILE_FLOW = 16;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.SupplyFromSpotFlowImpl <em>Supply From Spot Flow</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.SupplyFromSpotFlowImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getSupplyFromSpotFlow()
	 * @generated
	 */
	int SUPPLY_FROM_SPOT_FLOW = 17;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.DeliverToSpotFlowImpl <em>Deliver To Spot Flow</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.DeliverToSpotFlowImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getDeliverToSpotFlow()
	 * @generated
	 */
	int DELIVER_TO_SPOT_FLOW = 18;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.impl.ShippingOptionImpl <em>Shipping Option</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.impl.ShippingOptionImpl
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getShippingOption()
	 * @generated
	 */
	int SHIPPING_OPTION = 19;

	/**
	 * The feature id for the '<em><b>Vessel Assignment Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHIPPING_OPTION__VESSEL_ASSIGNMENT_TYPE = 0;

	/**
	 * The feature id for the '<em><b>Spot Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHIPPING_OPTION__SPOT_INDEX = 1;

	/**
	 * The feature id for the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHIPPING_OPTION__VESSEL = 2;

	/**
	 * The feature id for the '<em><b>Max Laden Idle Days</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHIPPING_OPTION__MAX_LADEN_IDLE_DAYS = 3;

	/**
	 * The number of structural features of the '<em>Shipping Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHIPPING_OPTION_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Shipping Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHIPPING_OPTION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.adp.IntervalType <em>Interval Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.adp.IntervalType
	 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getIntervalType()
	 * @generated
	 */
	int INTERVAL_TYPE = 20;


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
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.adp.ADPModel#getBindingRules <em>Binding Rules</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Binding Rules</em>'.
	 * @see com.mmxlabs.models.lng.adp.ADPModel#getBindingRules()
	 * @see #getADPModel()
	 * @generated
	 */
	EReference getADPModel_BindingRules();

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
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.adp.ContractProfile#getDistributionModel <em>Distribution Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Distribution Model</em>'.
	 * @see com.mmxlabs.models.lng.adp.ContractProfile#getDistributionModel()
	 * @see #getContractProfile()
	 * @generated
	 */
	EReference getContractProfile_DistributionModel();

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.DistributionModel <em>Distribution Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Distribution Model</em>'.
	 * @see com.mmxlabs.models.lng.adp.DistributionModel
	 * @generated
	 */
	EClass getDistributionModel();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.CargoSizeDistributionModel#getCargoSize <em>Cargo Size</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cargo Size</em>'.
	 * @see com.mmxlabs.models.lng.adp.CargoSizeDistributionModel#getCargoSize()
	 * @see #getCargoSizeDistributionModel()
	 * @generated
	 */
	EAttribute getCargoSizeDistributionModel_CargoSize();

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
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.adp.SubContractProfile#getSlots <em>Slots</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Slots</em>'.
	 * @see com.mmxlabs.models.lng.adp.SubContractProfile#getSlots()
	 * @see #getSubContractProfile()
	 * @generated
	 */
	EReference getSubContractProfile_Slots();

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.adp.BindingRule <em>Binding Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Binding Rule</em>'.
	 * @see com.mmxlabs.models.lng.adp.BindingRule
	 * @generated
	 */
	EClass getBindingRule();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.adp.BindingRule#getProfile <em>Profile</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Profile</em>'.
	 * @see com.mmxlabs.models.lng.adp.BindingRule#getProfile()
	 * @see #getBindingRule()
	 * @generated
	 */
	EReference getBindingRule_Profile();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.adp.BindingRule#getSubProfile <em>Sub Profile</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Sub Profile</em>'.
	 * @see com.mmxlabs.models.lng.adp.BindingRule#getSubProfile()
	 * @see #getBindingRule()
	 * @generated
	 */
	EReference getBindingRule_SubProfile();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.adp.BindingRule#getFlowType <em>Flow Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Flow Type</em>'.
	 * @see com.mmxlabs.models.lng.adp.BindingRule#getFlowType()
	 * @see #getBindingRule()
	 * @generated
	 */
	EReference getBindingRule_FlowType();

	/**
	 * Returns the meta object for the containment reference '{@link com.mmxlabs.models.lng.adp.BindingRule#getShippingOption <em>Shipping Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Shipping Option</em>'.
	 * @see com.mmxlabs.models.lng.adp.BindingRule#getShippingOption()
	 * @see #getBindingRule()
	 * @generated
	 */
	EReference getBindingRule_ShippingOption();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.adp.ShippingOption#getMaxLadenIdleDays <em>Max Laden Idle Days</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Laden Idle Days</em>'.
	 * @see com.mmxlabs.models.lng.adp.ShippingOption#getMaxLadenIdleDays()
	 * @see #getShippingOption()
	 * @generated
	 */
	EAttribute getShippingOption_MaxLadenIdleDays();

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
		 * The meta object literal for the '<em><b>Binding Rules</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ADP_MODEL__BINDING_RULES = eINSTANCE.getADPModel_BindingRules();

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
		 * The meta object literal for the '<em><b>Distribution Model</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTRACT_PROFILE__DISTRIBUTION_MODEL = eINSTANCE.getContractProfile_DistributionModel();

		/**
		 * The meta object literal for the '<em><b>Sub Profiles</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTRACT_PROFILE__SUB_PROFILES = eINSTANCE.getContractProfile_SubProfiles();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.DistributionModel <em>Distribution Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.DistributionModel
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getDistributionModel()
		 * @generated
		 */
		EClass DISTRIBUTION_MODEL = eINSTANCE.getDistributionModel();

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
		 * The meta object literal for the '<em><b>Cargo Size</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO_SIZE_DISTRIBUTION_MODEL__CARGO_SIZE = eINSTANCE.getCargoSizeDistributionModel_CargoSize();

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
		 * The meta object literal for the '<em><b>Slots</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SUB_CONTRACT_PROFILE__SLOTS = eINSTANCE.getSubContractProfile_Slots();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.impl.BindingRuleImpl <em>Binding Rule</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.impl.BindingRuleImpl
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getBindingRule()
		 * @generated
		 */
		EClass BINDING_RULE = eINSTANCE.getBindingRule();

		/**
		 * The meta object literal for the '<em><b>Profile</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BINDING_RULE__PROFILE = eINSTANCE.getBindingRule_Profile();

		/**
		 * The meta object literal for the '<em><b>Sub Profile</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BINDING_RULE__SUB_PROFILE = eINSTANCE.getBindingRule_SubProfile();

		/**
		 * The meta object literal for the '<em><b>Flow Type</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BINDING_RULE__FLOW_TYPE = eINSTANCE.getBindingRule_FlowType();

		/**
		 * The meta object literal for the '<em><b>Shipping Option</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BINDING_RULE__SHIPPING_OPTION = eINSTANCE.getBindingRule_ShippingOption();

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
		 * The meta object literal for the '<em><b>Max Laden Idle Days</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SHIPPING_OPTION__MAX_LADEN_IDLE_DAYS = eINSTANCE.getShippingOption_MaxLadenIdleDays();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.adp.IntervalType <em>Interval Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.adp.IntervalType
		 * @see com.mmxlabs.models.lng.adp.impl.ADPPackageImpl#getIntervalType()
		 * @generated
		 */
		EEnum INTERVAL_TYPE = eINSTANCE.getIntervalType();

	}

} //ADPPackage
