/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.contract.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

import scenario.NamedObject;
import scenario.ScenarioObject;
import scenario.contract.Contract;
import scenario.contract.ContractModel;
import scenario.contract.ContractPackage;
import scenario.contract.Entity;
import scenario.contract.FixedPricePurchaseContract;
import scenario.contract.GroupEntity;
import scenario.contract.IndexPricePurchaseContract;
import scenario.contract.NetbackPurchaseContract;
import scenario.contract.ProfitSharingPurchaseContract;
import scenario.contract.PurchaseContract;
import scenario.contract.SalesContract;
import scenario.contract.SimplePurchaseContract;
import scenario.contract.TotalVolumeLimit;

/**
 * <!-- begin-user-doc --> The <b>Adapter Factory</b> for the model. It provides an adapter <code>createXXX</code> method for each class of the model. <!-- end-user-doc -->
 * 
 * @see scenario.contract.ContractPackage
 * @generated
 */
public class ContractAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected static ContractPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ContractAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = ContractPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object. <!-- begin-user-doc --> This implementation returns <code>true</code> if the object is either the model's package or is an
	 * instance object of the model. <!-- end-user-doc -->
	 * 
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(final Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject) object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ContractSwitch<Adapter> modelSwitch = new ContractSwitch<Adapter>() {
		@Override
		public Adapter caseContractModel(final ContractModel object) {
			return createContractModelAdapter();
		}

		@Override
		public Adapter caseTotalVolumeLimit(final TotalVolumeLimit object) {
			return createTotalVolumeLimitAdapter();
		}

		@Override
		public Adapter caseEntity(final Entity object) {
			return createEntityAdapter();
		}

		@Override
		public Adapter caseContract(final Contract object) {
			return createContractAdapter();
		}

		@Override
		public Adapter casePurchaseContract(final PurchaseContract object) {
			return createPurchaseContractAdapter();
		}

		@Override
		public Adapter caseSalesContract(final SalesContract object) {
			return createSalesContractAdapter();
		}

		@Override
		public Adapter caseFixedPricePurchaseContract(final FixedPricePurchaseContract object) {
			return createFixedPricePurchaseContractAdapter();
		}

		@Override
		public Adapter caseIndexPricePurchaseContract(final IndexPricePurchaseContract object) {
			return createIndexPricePurchaseContractAdapter();
		}

		@Override
		public Adapter caseNetbackPurchaseContract(final NetbackPurchaseContract object) {
			return createNetbackPurchaseContractAdapter();
		}

		@Override
		public Adapter caseProfitSharingPurchaseContract(final ProfitSharingPurchaseContract object) {
			return createProfitSharingPurchaseContractAdapter();
		}

		@Override
		public Adapter caseSimplePurchaseContract(final SimplePurchaseContract object) {
			return createSimplePurchaseContractAdapter();
		}

		@Override
		public Adapter caseGroupEntity(final GroupEntity object) {
			return createGroupEntityAdapter();
		}

		@Override
		public Adapter caseScenarioObject(final ScenarioObject object) {
			return createScenarioObjectAdapter();
		}

		@Override
		public Adapter caseNamedObject(final NamedObject object) {
			return createNamedObjectAdapter();
		}

		@Override
		public Adapter defaultCase(final EObject object) {
			return createEObjectAdapter();
		}
	};

	/**
	 * Creates an adapter for the <code>target</code>. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param target
	 *            the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(final Notifier target) {
		return modelSwitch.doSwitch((EObject) target);
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.contract.ContractModel <em>Model</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can easily
	 * ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see scenario.contract.ContractModel
	 * @generated
	 */
	public Adapter createContractModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.contract.PurchaseContract <em>Purchase Contract</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we
	 * can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see scenario.contract.PurchaseContract
	 * @generated
	 */
	public Adapter createPurchaseContractAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.contract.SalesContract <em>Sales Contract</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can
	 * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see scenario.contract.SalesContract
	 * @generated
	 */
	public Adapter createSalesContractAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.contract.TotalVolumeLimit <em>Total Volume Limit</em>}'. <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see scenario.contract.TotalVolumeLimit
	 * @generated
	 */
	public Adapter createTotalVolumeLimitAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.contract.Entity <em>Entity</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore
	 * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see scenario.contract.Entity
	 * @generated
	 */
	public Adapter createEntityAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.contract.FixedPricePurchaseContract <em>Fixed Price Purchase Contract</em>}'. <!-- begin-user-doc --> This default implementation
	 * returns null so that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see scenario.contract.FixedPricePurchaseContract
	 * @generated
	 */
	public Adapter createFixedPricePurchaseContractAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.contract.IndexPricePurchaseContract <em>Index Price Purchase Contract</em>}'. <!-- begin-user-doc --> This default implementation
	 * returns null so that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see scenario.contract.IndexPricePurchaseContract
	 * @generated
	 */
	public Adapter createIndexPricePurchaseContractAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.contract.NetbackPurchaseContract <em>Netback Purchase Contract</em>}'. <!-- begin-user-doc --> This default implementation returns
	 * null so that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see scenario.contract.NetbackPurchaseContract
	 * @generated
	 */
	public Adapter createNetbackPurchaseContractAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.contract.ProfitSharingPurchaseContract <em>Profit Sharing Purchase Contract</em>}'. <!-- begin-user-doc --> This default
	 * implementation returns null so that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see scenario.contract.ProfitSharingPurchaseContract
	 * @generated
	 */
	public Adapter createProfitSharingPurchaseContractAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.contract.SimplePurchaseContract <em>Simple Purchase Contract</em>}'. <!-- begin-user-doc --> This default implementation returns
	 * null so that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see scenario.contract.SimplePurchaseContract
	 * @generated
	 */
	public Adapter createSimplePurchaseContractAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.contract.GroupEntity <em>Group Entity</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can
	 * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see scenario.contract.GroupEntity
	 * @generated
	 */
	public Adapter createGroupEntityAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.contract.Contract <em>Contract</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can easily
	 * ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see scenario.contract.Contract
	 * @generated
	 */
	public Adapter createContractAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.ScenarioObject <em>Object</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore
	 * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see scenario.ScenarioObject
	 * @generated
	 */
	public Adapter createScenarioObjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.NamedObject <em>Named Object</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore
	 * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see scenario.NamedObject
	 * @generated
	 */
	public Adapter createNamedObjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case. <!-- begin-user-doc --> This default implementation returns null. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} // ContractAdapterFactory
