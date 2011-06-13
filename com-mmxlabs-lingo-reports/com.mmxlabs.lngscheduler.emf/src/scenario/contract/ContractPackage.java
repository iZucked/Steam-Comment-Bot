/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.contract;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import scenario.ScenarioPackage;

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
 * @see scenario.contract.ContractFactory
 * @model kind="package"
 * @generated
 */
public interface ContractPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "contract";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://com.mmxlabs.lng.emf/contract";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "com.mmxlabs.lng.emf.contract";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ContractPackage eINSTANCE = scenario.contract.impl.ContractPackageImpl.init();

	/**
	 * The meta object id for the '{@link scenario.contract.impl.ContractModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.contract.impl.ContractModelImpl
	 * @see scenario.contract.impl.ContractPackageImpl#getContractModel()
	 * @generated
	 */
	int CONTRACT_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Purchase Contracts</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_MODEL__PURCHASE_CONTRACTS = 0;

	/**
	 * The feature id for the '<em><b>Sales Contracts</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_MODEL__SALES_CONTRACTS = 1;

	/**
	 * The feature id for the '<em><b>Volume Constraints</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_MODEL__VOLUME_CONSTRAINTS = 2;

	/**
	 * The feature id for the '<em><b>Entities</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_MODEL__ENTITIES = 3;

	/**
	 * The feature id for the '<em><b>Shipping Entity</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_MODEL__SHIPPING_ENTITY = 4;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_MODEL_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link scenario.contract.impl.ContractImpl <em>Contract</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.contract.impl.ContractImpl
	 * @see scenario.contract.impl.ContractPackageImpl#getContract()
	 * @generated
	 */
	int CONTRACT = 9;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__NAME = ScenarioPackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT__ENTITY = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_FEATURE_COUNT = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT___GET_CONTAINER = ScenarioPackage.NAMED_OBJECT___GET_CONTAINER;

	/**
	 * The number of operations of the '<em>Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_OPERATION_COUNT = ScenarioPackage.NAMED_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link scenario.contract.impl.PurchaseContractImpl <em>Purchase Contract</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.contract.impl.PurchaseContractImpl
	 * @see scenario.contract.impl.ContractPackageImpl#getPurchaseContract()
	 * @generated
	 */
	int PURCHASE_CONTRACT = 1;

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
	 * The number of structural features of the '<em>Purchase Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT_FEATURE_COUNT = CONTRACT_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT___GET_CONTAINER = CONTRACT___GET_CONTAINER;

	/**
	 * The number of operations of the '<em>Purchase Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT_OPERATION_COUNT = CONTRACT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link scenario.contract.impl.SalesContractImpl <em>Sales Contract</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.contract.impl.SalesContractImpl
	 * @see scenario.contract.impl.ContractPackageImpl#getSalesContract()
	 * @generated
	 */
	int SALES_CONTRACT = 2;

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
	 * The feature id for the '<em><b>Index</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__INDEX = CONTRACT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Markup</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT__MARKUP = CONTRACT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Sales Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT_FEATURE_COUNT = CONTRACT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Get Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT___GET_CONTAINER = CONTRACT___GET_CONTAINER;

	/**
	 * The number of operations of the '<em>Sales Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT_OPERATION_COUNT = CONTRACT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link scenario.contract.impl.TotalVolumeLimitImpl <em>Total Volume Limit</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.contract.impl.TotalVolumeLimitImpl
	 * @see scenario.contract.impl.ContractPackageImpl#getTotalVolumeLimit()
	 * @generated
	 */
	int TOTAL_VOLUME_LIMIT = 3;

	/**
	 * The feature id for the '<em><b>Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOTAL_VOLUME_LIMIT__PORTS = 0;

	/**
	 * The feature id for the '<em><b>Maximum Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOTAL_VOLUME_LIMIT__MAXIMUM_VOLUME = 1;

	/**
	 * The feature id for the '<em><b>Start Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOTAL_VOLUME_LIMIT__START_DATE = 2;

	/**
	 * The feature id for the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOTAL_VOLUME_LIMIT__DURATION = 3;

	/**
	 * The feature id for the '<em><b>Repeating</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOTAL_VOLUME_LIMIT__REPEATING = 4;

	/**
	 * The number of structural features of the '<em>Total Volume Limit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOTAL_VOLUME_LIMIT_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>Total Volume Limit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOTAL_VOLUME_LIMIT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link scenario.contract.impl.EntityImpl <em>Entity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.contract.impl.EntityImpl
	 * @see scenario.contract.impl.ContractPackageImpl#getEntity()
	 * @generated
	 */
	int ENTITY = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY__NAME = ScenarioPackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Tax Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY__TAX_RATE = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Ownership</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY__OWNERSHIP = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Entity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_FEATURE_COUNT = ScenarioPackage.NAMED_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Get Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY___GET_CONTAINER = ScenarioPackage.NAMED_OBJECT___GET_CONTAINER;

	/**
	 * The number of operations of the '<em>Entity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_OPERATION_COUNT = ScenarioPackage.NAMED_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link scenario.contract.impl.FixedPricePurchaseContractImpl <em>Fixed Price Purchase Contract</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.contract.impl.FixedPricePurchaseContractImpl
	 * @see scenario.contract.impl.ContractPackageImpl#getFixedPricePurchaseContract()
	 * @generated
	 */
	int FIXED_PRICE_PURCHASE_CONTRACT = 5;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIXED_PRICE_PURCHASE_CONTRACT__NAME = PURCHASE_CONTRACT__NAME;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIXED_PRICE_PURCHASE_CONTRACT__ENTITY = PURCHASE_CONTRACT__ENTITY;

	/**
	 * The feature id for the '<em><b>Unit Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIXED_PRICE_PURCHASE_CONTRACT__UNIT_PRICE = PURCHASE_CONTRACT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Fixed Price Purchase Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIXED_PRICE_PURCHASE_CONTRACT_FEATURE_COUNT = PURCHASE_CONTRACT_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIXED_PRICE_PURCHASE_CONTRACT___GET_CONTAINER = PURCHASE_CONTRACT___GET_CONTAINER;

	/**
	 * The number of operations of the '<em>Fixed Price Purchase Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIXED_PRICE_PURCHASE_CONTRACT_OPERATION_COUNT = PURCHASE_CONTRACT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link scenario.contract.impl.IndexPricePurchaseContractImpl <em>Index Price Purchase Contract</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.contract.impl.IndexPricePurchaseContractImpl
	 * @see scenario.contract.impl.ContractPackageImpl#getIndexPricePurchaseContract()
	 * @generated
	 */
	int INDEX_PRICE_PURCHASE_CONTRACT = 6;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX_PRICE_PURCHASE_CONTRACT__NAME = PURCHASE_CONTRACT__NAME;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX_PRICE_PURCHASE_CONTRACT__ENTITY = PURCHASE_CONTRACT__ENTITY;

	/**
	 * The feature id for the '<em><b>Index</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX_PRICE_PURCHASE_CONTRACT__INDEX = PURCHASE_CONTRACT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Index Price Purchase Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX_PRICE_PURCHASE_CONTRACT_FEATURE_COUNT = PURCHASE_CONTRACT_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX_PRICE_PURCHASE_CONTRACT___GET_CONTAINER = PURCHASE_CONTRACT___GET_CONTAINER;

	/**
	 * The number of operations of the '<em>Index Price Purchase Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEX_PRICE_PURCHASE_CONTRACT_OPERATION_COUNT = PURCHASE_CONTRACT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link scenario.contract.impl.NetbackPurchaseContractImpl <em>Netback Purchase Contract</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.contract.impl.NetbackPurchaseContractImpl
	 * @see scenario.contract.impl.ContractPackageImpl#getNetbackPurchaseContract()
	 * @generated
	 */
	int NETBACK_PURCHASE_CONTRACT = 7;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NETBACK_PURCHASE_CONTRACT__NAME = PURCHASE_CONTRACT__NAME;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NETBACK_PURCHASE_CONTRACT__ENTITY = PURCHASE_CONTRACT__ENTITY;

	/**
	 * The feature id for the '<em><b>Lower Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NETBACK_PURCHASE_CONTRACT__LOWER_BOUND = PURCHASE_CONTRACT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Buyers Margin</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NETBACK_PURCHASE_CONTRACT__BUYERS_MARGIN = PURCHASE_CONTRACT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Netback Purchase Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NETBACK_PURCHASE_CONTRACT_FEATURE_COUNT = PURCHASE_CONTRACT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Get Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NETBACK_PURCHASE_CONTRACT___GET_CONTAINER = PURCHASE_CONTRACT___GET_CONTAINER;

	/**
	 * The number of operations of the '<em>Netback Purchase Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NETBACK_PURCHASE_CONTRACT_OPERATION_COUNT = PURCHASE_CONTRACT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link scenario.contract.impl.ProfitSharingPurchaseContractImpl <em>Profit Sharing Purchase Contract</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.contract.impl.ProfitSharingPurchaseContractImpl
	 * @see scenario.contract.impl.ContractPackageImpl#getProfitSharingPurchaseContract()
	 * @generated
	 */
	int PROFIT_SHARING_PURCHASE_CONTRACT = 8;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARING_PURCHASE_CONTRACT__NAME = PURCHASE_CONTRACT__NAME;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARING_PURCHASE_CONTRACT__ENTITY = PURCHASE_CONTRACT__ENTITY;

	/**
	 * The feature id for the '<em><b>Index</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARING_PURCHASE_CONTRACT__INDEX = PURCHASE_CONTRACT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Reference Index</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARING_PURCHASE_CONTRACT__REFERENCE_INDEX = PURCHASE_CONTRACT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Alpha</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARING_PURCHASE_CONTRACT__ALPHA = PURCHASE_CONTRACT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Beta</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARING_PURCHASE_CONTRACT__BETA = PURCHASE_CONTRACT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Gamma</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARING_PURCHASE_CONTRACT__GAMMA = PURCHASE_CONTRACT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Profit Sharing Purchase Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARING_PURCHASE_CONTRACT_FEATURE_COUNT = PURCHASE_CONTRACT_FEATURE_COUNT + 5;

	/**
	 * The operation id for the '<em>Get Container</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARING_PURCHASE_CONTRACT___GET_CONTAINER = PURCHASE_CONTRACT___GET_CONTAINER;

	/**
	 * The number of operations of the '<em>Profit Sharing Purchase Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFIT_SHARING_PURCHASE_CONTRACT_OPERATION_COUNT = PURCHASE_CONTRACT_OPERATION_COUNT + 0;


	/**
	 * Returns the meta object for class '{@link scenario.contract.ContractModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see scenario.contract.ContractModel
	 * @generated
	 */
	EClass getContractModel();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.contract.ContractModel#getPurchaseContracts <em>Purchase Contracts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Purchase Contracts</em>'.
	 * @see scenario.contract.ContractModel#getPurchaseContracts()
	 * @see #getContractModel()
	 * @generated
	 */
	EReference getContractModel_PurchaseContracts();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.contract.ContractModel#getSalesContracts <em>Sales Contracts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Sales Contracts</em>'.
	 * @see scenario.contract.ContractModel#getSalesContracts()
	 * @see #getContractModel()
	 * @generated
	 */
	EReference getContractModel_SalesContracts();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.contract.ContractModel#getVolumeConstraints <em>Volume Constraints</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Volume Constraints</em>'.
	 * @see scenario.contract.ContractModel#getVolumeConstraints()
	 * @see #getContractModel()
	 * @generated
	 */
	EReference getContractModel_VolumeConstraints();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.contract.ContractModel#getEntities <em>Entities</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Entities</em>'.
	 * @see scenario.contract.ContractModel#getEntities()
	 * @see #getContractModel()
	 * @generated
	 */
	EReference getContractModel_Entities();

	/**
	 * Returns the meta object for the containment reference '{@link scenario.contract.ContractModel#getShippingEntity <em>Shipping Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Shipping Entity</em>'.
	 * @see scenario.contract.ContractModel#getShippingEntity()
	 * @see #getContractModel()
	 * @generated
	 */
	EReference getContractModel_ShippingEntity();

	/**
	 * Returns the meta object for class '{@link scenario.contract.PurchaseContract <em>Purchase Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Purchase Contract</em>'.
	 * @see scenario.contract.PurchaseContract
	 * @generated
	 */
	EClass getPurchaseContract();

	/**
	 * Returns the meta object for class '{@link scenario.contract.SalesContract <em>Sales Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Sales Contract</em>'.
	 * @see scenario.contract.SalesContract
	 * @generated
	 */
	EClass getSalesContract();

	/**
	 * Returns the meta object for the reference '{@link scenario.contract.SalesContract#getIndex <em>Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Index</em>'.
	 * @see scenario.contract.SalesContract#getIndex()
	 * @see #getSalesContract()
	 * @generated
	 */
	EReference getSalesContract_Index();

	/**
	 * Returns the meta object for the attribute '{@link scenario.contract.SalesContract#getMarkup <em>Markup</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Markup</em>'.
	 * @see scenario.contract.SalesContract#getMarkup()
	 * @see #getSalesContract()
	 * @generated
	 */
	EAttribute getSalesContract_Markup();

	/**
	 * Returns the meta object for class '{@link scenario.contract.TotalVolumeLimit <em>Total Volume Limit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Total Volume Limit</em>'.
	 * @see scenario.contract.TotalVolumeLimit
	 * @generated
	 */
	EClass getTotalVolumeLimit();

	/**
	 * Returns the meta object for the reference list '{@link scenario.contract.TotalVolumeLimit#getPorts <em>Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Ports</em>'.
	 * @see scenario.contract.TotalVolumeLimit#getPorts()
	 * @see #getTotalVolumeLimit()
	 * @generated
	 */
	EReference getTotalVolumeLimit_Ports();

	/**
	 * Returns the meta object for the attribute '{@link scenario.contract.TotalVolumeLimit#getMaximumVolume <em>Maximum Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Maximum Volume</em>'.
	 * @see scenario.contract.TotalVolumeLimit#getMaximumVolume()
	 * @see #getTotalVolumeLimit()
	 * @generated
	 */
	EAttribute getTotalVolumeLimit_MaximumVolume();

	/**
	 * Returns the meta object for the attribute '{@link scenario.contract.TotalVolumeLimit#getStartDate <em>Start Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start Date</em>'.
	 * @see scenario.contract.TotalVolumeLimit#getStartDate()
	 * @see #getTotalVolumeLimit()
	 * @generated
	 */
	EAttribute getTotalVolumeLimit_StartDate();

	/**
	 * Returns the meta object for the attribute '{@link scenario.contract.TotalVolumeLimit#getDuration <em>Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Duration</em>'.
	 * @see scenario.contract.TotalVolumeLimit#getDuration()
	 * @see #getTotalVolumeLimit()
	 * @generated
	 */
	EAttribute getTotalVolumeLimit_Duration();

	/**
	 * Returns the meta object for the attribute '{@link scenario.contract.TotalVolumeLimit#isRepeating <em>Repeating</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Repeating</em>'.
	 * @see scenario.contract.TotalVolumeLimit#isRepeating()
	 * @see #getTotalVolumeLimit()
	 * @generated
	 */
	EAttribute getTotalVolumeLimit_Repeating();

	/**
	 * Returns the meta object for class '{@link scenario.contract.Entity <em>Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Entity</em>'.
	 * @see scenario.contract.Entity
	 * @generated
	 */
	EClass getEntity();

	/**
	 * Returns the meta object for the attribute '{@link scenario.contract.Entity#getTaxRate <em>Tax Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Tax Rate</em>'.
	 * @see scenario.contract.Entity#getTaxRate()
	 * @see #getEntity()
	 * @generated
	 */
	EAttribute getEntity_TaxRate();

	/**
	 * Returns the meta object for the attribute '{@link scenario.contract.Entity#getOwnership <em>Ownership</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ownership</em>'.
	 * @see scenario.contract.Entity#getOwnership()
	 * @see #getEntity()
	 * @generated
	 */
	EAttribute getEntity_Ownership();

	/**
	 * Returns the meta object for class '{@link scenario.contract.FixedPricePurchaseContract <em>Fixed Price Purchase Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Fixed Price Purchase Contract</em>'.
	 * @see scenario.contract.FixedPricePurchaseContract
	 * @generated
	 */
	EClass getFixedPricePurchaseContract();

	/**
	 * Returns the meta object for the attribute '{@link scenario.contract.FixedPricePurchaseContract#getUnitPrice <em>Unit Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Unit Price</em>'.
	 * @see scenario.contract.FixedPricePurchaseContract#getUnitPrice()
	 * @see #getFixedPricePurchaseContract()
	 * @generated
	 */
	EAttribute getFixedPricePurchaseContract_UnitPrice();

	/**
	 * Returns the meta object for class '{@link scenario.contract.IndexPricePurchaseContract <em>Index Price Purchase Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Index Price Purchase Contract</em>'.
	 * @see scenario.contract.IndexPricePurchaseContract
	 * @generated
	 */
	EClass getIndexPricePurchaseContract();

	/**
	 * Returns the meta object for the reference '{@link scenario.contract.IndexPricePurchaseContract#getIndex <em>Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Index</em>'.
	 * @see scenario.contract.IndexPricePurchaseContract#getIndex()
	 * @see #getIndexPricePurchaseContract()
	 * @generated
	 */
	EReference getIndexPricePurchaseContract_Index();

	/**
	 * Returns the meta object for class '{@link scenario.contract.NetbackPurchaseContract <em>Netback Purchase Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Netback Purchase Contract</em>'.
	 * @see scenario.contract.NetbackPurchaseContract
	 * @generated
	 */
	EClass getNetbackPurchaseContract();

	/**
	 * Returns the meta object for the attribute '{@link scenario.contract.NetbackPurchaseContract#getLowerBound <em>Lower Bound</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lower Bound</em>'.
	 * @see scenario.contract.NetbackPurchaseContract#getLowerBound()
	 * @see #getNetbackPurchaseContract()
	 * @generated
	 */
	EAttribute getNetbackPurchaseContract_LowerBound();

	/**
	 * Returns the meta object for the attribute '{@link scenario.contract.NetbackPurchaseContract#getBuyersMargin <em>Buyers Margin</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Buyers Margin</em>'.
	 * @see scenario.contract.NetbackPurchaseContract#getBuyersMargin()
	 * @see #getNetbackPurchaseContract()
	 * @generated
	 */
	EAttribute getNetbackPurchaseContract_BuyersMargin();

	/**
	 * Returns the meta object for class '{@link scenario.contract.ProfitSharingPurchaseContract <em>Profit Sharing Purchase Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Profit Sharing Purchase Contract</em>'.
	 * @see scenario.contract.ProfitSharingPurchaseContract
	 * @generated
	 */
	EClass getProfitSharingPurchaseContract();

	/**
	 * Returns the meta object for the reference '{@link scenario.contract.ProfitSharingPurchaseContract#getIndex <em>Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Index</em>'.
	 * @see scenario.contract.ProfitSharingPurchaseContract#getIndex()
	 * @see #getProfitSharingPurchaseContract()
	 * @generated
	 */
	EReference getProfitSharingPurchaseContract_Index();

	/**
	 * Returns the meta object for the reference '{@link scenario.contract.ProfitSharingPurchaseContract#getReferenceIndex <em>Reference Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Reference Index</em>'.
	 * @see scenario.contract.ProfitSharingPurchaseContract#getReferenceIndex()
	 * @see #getProfitSharingPurchaseContract()
	 * @generated
	 */
	EReference getProfitSharingPurchaseContract_ReferenceIndex();

	/**
	 * Returns the meta object for the attribute '{@link scenario.contract.ProfitSharingPurchaseContract#getAlpha <em>Alpha</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Alpha</em>'.
	 * @see scenario.contract.ProfitSharingPurchaseContract#getAlpha()
	 * @see #getProfitSharingPurchaseContract()
	 * @generated
	 */
	EAttribute getProfitSharingPurchaseContract_Alpha();

	/**
	 * Returns the meta object for the attribute '{@link scenario.contract.ProfitSharingPurchaseContract#getBeta <em>Beta</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Beta</em>'.
	 * @see scenario.contract.ProfitSharingPurchaseContract#getBeta()
	 * @see #getProfitSharingPurchaseContract()
	 * @generated
	 */
	EAttribute getProfitSharingPurchaseContract_Beta();

	/**
	 * Returns the meta object for the attribute '{@link scenario.contract.ProfitSharingPurchaseContract#getGamma <em>Gamma</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Gamma</em>'.
	 * @see scenario.contract.ProfitSharingPurchaseContract#getGamma()
	 * @see #getProfitSharingPurchaseContract()
	 * @generated
	 */
	EAttribute getProfitSharingPurchaseContract_Gamma();

	/**
	 * Returns the meta object for class '{@link scenario.contract.Contract <em>Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Contract</em>'.
	 * @see scenario.contract.Contract
	 * @generated
	 */
	EClass getContract();

	/**
	 * Returns the meta object for the reference '{@link scenario.contract.Contract#getEntity <em>Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Entity</em>'.
	 * @see scenario.contract.Contract#getEntity()
	 * @see #getContract()
	 * @generated
	 */
	EReference getContract_Entity();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ContractFactory getContractFactory();

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
		 * The meta object literal for the '{@link scenario.contract.impl.ContractModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.contract.impl.ContractModelImpl
		 * @see scenario.contract.impl.ContractPackageImpl#getContractModel()
		 * @generated
		 */
		EClass CONTRACT_MODEL = eINSTANCE.getContractModel();

		/**
		 * The meta object literal for the '<em><b>Purchase Contracts</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTRACT_MODEL__PURCHASE_CONTRACTS = eINSTANCE.getContractModel_PurchaseContracts();

		/**
		 * The meta object literal for the '<em><b>Sales Contracts</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTRACT_MODEL__SALES_CONTRACTS = eINSTANCE.getContractModel_SalesContracts();

		/**
		 * The meta object literal for the '<em><b>Volume Constraints</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTRACT_MODEL__VOLUME_CONSTRAINTS = eINSTANCE.getContractModel_VolumeConstraints();

		/**
		 * The meta object literal for the '<em><b>Entities</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTRACT_MODEL__ENTITIES = eINSTANCE.getContractModel_Entities();

		/**
		 * The meta object literal for the '<em><b>Shipping Entity</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTRACT_MODEL__SHIPPING_ENTITY = eINSTANCE.getContractModel_ShippingEntity();

		/**
		 * The meta object literal for the '{@link scenario.contract.impl.PurchaseContractImpl <em>Purchase Contract</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.contract.impl.PurchaseContractImpl
		 * @see scenario.contract.impl.ContractPackageImpl#getPurchaseContract()
		 * @generated
		 */
		EClass PURCHASE_CONTRACT = eINSTANCE.getPurchaseContract();

		/**
		 * The meta object literal for the '{@link scenario.contract.impl.SalesContractImpl <em>Sales Contract</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.contract.impl.SalesContractImpl
		 * @see scenario.contract.impl.ContractPackageImpl#getSalesContract()
		 * @generated
		 */
		EClass SALES_CONTRACT = eINSTANCE.getSalesContract();

		/**
		 * The meta object literal for the '<em><b>Index</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SALES_CONTRACT__INDEX = eINSTANCE.getSalesContract_Index();

		/**
		 * The meta object literal for the '<em><b>Markup</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SALES_CONTRACT__MARKUP = eINSTANCE.getSalesContract_Markup();

		/**
		 * The meta object literal for the '{@link scenario.contract.impl.TotalVolumeLimitImpl <em>Total Volume Limit</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.contract.impl.TotalVolumeLimitImpl
		 * @see scenario.contract.impl.ContractPackageImpl#getTotalVolumeLimit()
		 * @generated
		 */
		EClass TOTAL_VOLUME_LIMIT = eINSTANCE.getTotalVolumeLimit();

		/**
		 * The meta object literal for the '<em><b>Ports</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TOTAL_VOLUME_LIMIT__PORTS = eINSTANCE.getTotalVolumeLimit_Ports();

		/**
		 * The meta object literal for the '<em><b>Maximum Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TOTAL_VOLUME_LIMIT__MAXIMUM_VOLUME = eINSTANCE.getTotalVolumeLimit_MaximumVolume();

		/**
		 * The meta object literal for the '<em><b>Start Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TOTAL_VOLUME_LIMIT__START_DATE = eINSTANCE.getTotalVolumeLimit_StartDate();

		/**
		 * The meta object literal for the '<em><b>Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TOTAL_VOLUME_LIMIT__DURATION = eINSTANCE.getTotalVolumeLimit_Duration();

		/**
		 * The meta object literal for the '<em><b>Repeating</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TOTAL_VOLUME_LIMIT__REPEATING = eINSTANCE.getTotalVolumeLimit_Repeating();

		/**
		 * The meta object literal for the '{@link scenario.contract.impl.EntityImpl <em>Entity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.contract.impl.EntityImpl
		 * @see scenario.contract.impl.ContractPackageImpl#getEntity()
		 * @generated
		 */
		EClass ENTITY = eINSTANCE.getEntity();

		/**
		 * The meta object literal for the '<em><b>Tax Rate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENTITY__TAX_RATE = eINSTANCE.getEntity_TaxRate();

		/**
		 * The meta object literal for the '<em><b>Ownership</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENTITY__OWNERSHIP = eINSTANCE.getEntity_Ownership();

		/**
		 * The meta object literal for the '{@link scenario.contract.impl.FixedPricePurchaseContractImpl <em>Fixed Price Purchase Contract</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.contract.impl.FixedPricePurchaseContractImpl
		 * @see scenario.contract.impl.ContractPackageImpl#getFixedPricePurchaseContract()
		 * @generated
		 */
		EClass FIXED_PRICE_PURCHASE_CONTRACT = eINSTANCE.getFixedPricePurchaseContract();

		/**
		 * The meta object literal for the '<em><b>Unit Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FIXED_PRICE_PURCHASE_CONTRACT__UNIT_PRICE = eINSTANCE.getFixedPricePurchaseContract_UnitPrice();

		/**
		 * The meta object literal for the '{@link scenario.contract.impl.IndexPricePurchaseContractImpl <em>Index Price Purchase Contract</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.contract.impl.IndexPricePurchaseContractImpl
		 * @see scenario.contract.impl.ContractPackageImpl#getIndexPricePurchaseContract()
		 * @generated
		 */
		EClass INDEX_PRICE_PURCHASE_CONTRACT = eINSTANCE.getIndexPricePurchaseContract();

		/**
		 * The meta object literal for the '<em><b>Index</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INDEX_PRICE_PURCHASE_CONTRACT__INDEX = eINSTANCE.getIndexPricePurchaseContract_Index();

		/**
		 * The meta object literal for the '{@link scenario.contract.impl.NetbackPurchaseContractImpl <em>Netback Purchase Contract</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.contract.impl.NetbackPurchaseContractImpl
		 * @see scenario.contract.impl.ContractPackageImpl#getNetbackPurchaseContract()
		 * @generated
		 */
		EClass NETBACK_PURCHASE_CONTRACT = eINSTANCE.getNetbackPurchaseContract();

		/**
		 * The meta object literal for the '<em><b>Lower Bound</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NETBACK_PURCHASE_CONTRACT__LOWER_BOUND = eINSTANCE.getNetbackPurchaseContract_LowerBound();

		/**
		 * The meta object literal for the '<em><b>Buyers Margin</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NETBACK_PURCHASE_CONTRACT__BUYERS_MARGIN = eINSTANCE.getNetbackPurchaseContract_BuyersMargin();

		/**
		 * The meta object literal for the '{@link scenario.contract.impl.ProfitSharingPurchaseContractImpl <em>Profit Sharing Purchase Contract</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.contract.impl.ProfitSharingPurchaseContractImpl
		 * @see scenario.contract.impl.ContractPackageImpl#getProfitSharingPurchaseContract()
		 * @generated
		 */
		EClass PROFIT_SHARING_PURCHASE_CONTRACT = eINSTANCE.getProfitSharingPurchaseContract();

		/**
		 * The meta object literal for the '<em><b>Index</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROFIT_SHARING_PURCHASE_CONTRACT__INDEX = eINSTANCE.getProfitSharingPurchaseContract_Index();

		/**
		 * The meta object literal for the '<em><b>Reference Index</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROFIT_SHARING_PURCHASE_CONTRACT__REFERENCE_INDEX = eINSTANCE.getProfitSharingPurchaseContract_ReferenceIndex();

		/**
		 * The meta object literal for the '<em><b>Alpha</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFIT_SHARING_PURCHASE_CONTRACT__ALPHA = eINSTANCE.getProfitSharingPurchaseContract_Alpha();

		/**
		 * The meta object literal for the '<em><b>Beta</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFIT_SHARING_PURCHASE_CONTRACT__BETA = eINSTANCE.getProfitSharingPurchaseContract_Beta();

		/**
		 * The meta object literal for the '<em><b>Gamma</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFIT_SHARING_PURCHASE_CONTRACT__GAMMA = eINSTANCE.getProfitSharingPurchaseContract_Gamma();

		/**
		 * The meta object literal for the '{@link scenario.contract.impl.ContractImpl <em>Contract</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.contract.impl.ContractImpl
		 * @see scenario.contract.impl.ContractPackageImpl#getContract()
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

	}

} //ContractPackage
