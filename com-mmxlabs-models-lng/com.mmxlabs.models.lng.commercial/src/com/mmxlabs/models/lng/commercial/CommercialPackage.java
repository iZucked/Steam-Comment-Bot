/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.commercial;

import com.mmxlabs.models.lng.types.TypesPackage;

import com.mmxlabs.models.mmxcore.MMXCorePackage;

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
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMERCIAL_MODEL__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Entities</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMERCIAL_MODEL__ENTITIES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Contracts</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMERCIAL_MODEL__CONTRACTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Shipping Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMERCIAL_MODEL__SHIPPING_ENTITY = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMERCIAL_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

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
	 * The number of structural features of the '<em>Legal Entity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEGAL_ENTITY_FEATURE_COUNT = TypesPackage.ALEGAL_ENTITY_FEATURE_COUNT + 0;

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
	 * The number of structural features of the '<em>Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTRACT_FEATURE_COUNT = TypesPackage.ACONTRACT_FEATURE_COUNT + 3;

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
	 * The number of structural features of the '<em>Sales Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SALES_CONTRACT_FEATURE_COUNT = CONTRACT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.commercial.impl.DischargeContractImpl <em>Discharge Contract</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.commercial.impl.DischargeContractImpl
	 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getDischargeContract()
	 * @generated
	 */
	int DISCHARGE_CONTRACT = 4;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_CONTRACT__EXTENSIONS = CONTRACT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_CONTRACT__PROXIES = CONTRACT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_CONTRACT__UUID = CONTRACT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_CONTRACT__NAME = CONTRACT__NAME;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_CONTRACT__ENTITY = CONTRACT__ENTITY;

	/**
	 * The feature id for the '<em><b>Allowed Ports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_CONTRACT__ALLOWED_PORTS = CONTRACT__ALLOWED_PORTS;

	/**
	 * The feature id for the '<em><b>Preferred Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_CONTRACT__PREFERRED_PORT = CONTRACT__PREFERRED_PORT;

	/**
	 * The number of structural features of the '<em>Discharge Contract</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_CONTRACT_FEATURE_COUNT = CONTRACT_FEATURE_COUNT + 0;


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
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.commercial.CommercialModel#getContracts <em>Contracts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Contracts</em>'.
	 * @see com.mmxlabs.models.lng.commercial.CommercialModel#getContracts()
	 * @see #getCommercialModel()
	 * @generated
	 */
	EReference getCommercialModel_Contracts();

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.SalesContract <em>Sales Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Sales Contract</em>'.
	 * @see com.mmxlabs.models.lng.commercial.SalesContract
	 * @generated
	 */
	EClass getSalesContract();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.commercial.DischargeContract <em>Discharge Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Discharge Contract</em>'.
	 * @see com.mmxlabs.models.lng.commercial.DischargeContract
	 * @generated
	 */
	EClass getDischargeContract();

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
		 * The meta object literal for the '<em><b>Contracts</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMMERCIAL_MODEL__CONTRACTS = eINSTANCE.getCommercialModel_Contracts();

		/**
		 * The meta object literal for the '<em><b>Shipping Entity</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMMERCIAL_MODEL__SHIPPING_ENTITY = eINSTANCE.getCommercialModel_ShippingEntity();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.SalesContractImpl <em>Sales Contract</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.SalesContractImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getSalesContract()
		 * @generated
		 */
		EClass SALES_CONTRACT = eINSTANCE.getSalesContract();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.commercial.impl.DischargeContractImpl <em>Discharge Contract</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.commercial.impl.DischargeContractImpl
		 * @see com.mmxlabs.models.lng.commercial.impl.CommercialPackageImpl#getDischargeContract()
		 * @generated
		 */
		EClass DISCHARGE_CONTRACT = eINSTANCE.getDischargeContract();

	}

} //CommercialPackage
