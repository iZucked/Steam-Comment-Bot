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
	 * The feature id for the '<em><b>Volume Constraints</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_MODEL__VOLUME_CONSTRAINTS = 2;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_MODEL_FEATURE_COUNT = 3;

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
	 * The number of structural features of the '<em>Purchase Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PURCHASE_CONTRACT_FEATURE_COUNT = 0;

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
	 * The number of structural features of the '<em>Sales Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT_FEATURE_COUNT = 0;


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
	 * Returns the meta object for the reference list '{@link scenario.contract.ContractModel#getVolumeConstraints <em>Volume Constraints</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Volume Constraints</em>'.
	 * @see scenario.contract.ContractModel#getVolumeConstraints()
	 * @see #getContractModel()
	 * @generated
	 */
	EReference getContractModel_VolumeConstraints();

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
		 * The meta object literal for the '<em><b>Volume Constraints</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTRACT_MODEL__VOLUME_CONSTRAINTS = eINSTANCE.getContractModel_VolumeConstraints();

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

	}

} //ContractPackage
