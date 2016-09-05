/**
 */
package com.mmxlabs.models.lng.adp.util;

import com.mmxlabs.models.lng.adp.*;

import com.mmxlabs.models.lng.cargo.Slot;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.jdt.annotation.Nullable;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.adp.ADPPackage
 * @generated
 */
public class ADPAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static ADPPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ADPAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = ADPPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ADPSwitch<@Nullable Adapter> modelSwitch =
		new ADPSwitch<@Nullable Adapter>() {
			@Override
			public Adapter caseADPModel(ADPModel object) {
				return createADPModelAdapter();
			}
			@Override
			public <T extends Slot> Adapter caseContractProfile(ContractProfile<T> object) {
				return createContractProfileAdapter();
			}
			@Override
			public Adapter casePurchaseContractProfile(PurchaseContractProfile object) {
				return createPurchaseContractProfileAdapter();
			}
			@Override
			public Adapter caseSalesContractProfile(SalesContractProfile object) {
				return createSalesContractProfileAdapter();
			}
			@Override
			public <T extends Slot> Adapter caseSubContractProfile(SubContractProfile<T> object) {
				return createSubContractProfileAdapter();
			}
			@Override
			public Adapter caseCustomSubProfileAttributes(CustomSubProfileAttributes object) {
				return createCustomSubProfileAttributesAdapter();
			}
			@Override
			public Adapter caseDistributionModel(DistributionModel object) {
				return createDistributionModelAdapter();
			}
			@Override
			public Adapter caseCargoSizeDistributionModel(CargoSizeDistributionModel object) {
				return createCargoSizeDistributionModelAdapter();
			}
			@Override
			public Adapter caseCargoNumberDistributionModel(CargoNumberDistributionModel object) {
				return createCargoNumberDistributionModelAdapter();
			}
			@Override
			public Adapter caseCargoByQuarterDistributionModel(CargoByQuarterDistributionModel object) {
				return createCargoByQuarterDistributionModelAdapter();
			}
			@Override
			public Adapter caseCargoIntervalDistributionModel(CargoIntervalDistributionModel object) {
				return createCargoIntervalDistributionModelAdapter();
			}
			@Override
			public Adapter caseBindingRule(BindingRule object) {
				return createBindingRuleAdapter();
			}
			@Override
			public Adapter caseFlowType(FlowType object) {
				return createFlowTypeAdapter();
			}
			@Override
			public Adapter caseSupplyFromFlow(SupplyFromFlow object) {
				return createSupplyFromFlowAdapter();
			}
			@Override
			public Adapter caseDeliverToFlow(DeliverToFlow object) {
				return createDeliverToFlowAdapter();
			}
			@Override
			public Adapter caseSupplyFromProfileFlow(SupplyFromProfileFlow object) {
				return createSupplyFromProfileFlowAdapter();
			}
			@Override
			public Adapter caseDeliverToProfileFlow(DeliverToProfileFlow object) {
				return createDeliverToProfileFlowAdapter();
			}
			@Override
			public Adapter caseSupplyFromSpotFlow(SupplyFromSpotFlow object) {
				return createSupplyFromSpotFlowAdapter();
			}
			@Override
			public Adapter caseDeliverToSpotFlow(DeliverToSpotFlow object) {
				return createDeliverToSpotFlowAdapter();
			}
			@Override
			public Adapter caseShippingOption(ShippingOption object) {
				return createShippingOptionAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.adp.ADPModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.adp.ADPModel
	 * @generated
	 */
	public Adapter createADPModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.adp.ContractProfile <em>Contract Profile</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.adp.ContractProfile
	 * @generated
	 */
	public Adapter createContractProfileAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.adp.DistributionModel <em>Distribution Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.adp.DistributionModel
	 * @generated
	 */
	public Adapter createDistributionModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.adp.CargoSizeDistributionModel <em>Cargo Size Distribution Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.adp.CargoSizeDistributionModel
	 * @generated
	 */
	public Adapter createCargoSizeDistributionModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.adp.CargoNumberDistributionModel <em>Cargo Number Distribution Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.adp.CargoNumberDistributionModel
	 * @generated
	 */
	public Adapter createCargoNumberDistributionModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.adp.PurchaseContractProfile <em>Purchase Contract Profile</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.adp.PurchaseContractProfile
	 * @generated
	 */
	public Adapter createPurchaseContractProfileAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.adp.SalesContractProfile <em>Sales Contract Profile</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.adp.SalesContractProfile
	 * @generated
	 */
	public Adapter createSalesContractProfileAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.adp.SubContractProfile <em>Sub Contract Profile</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.adp.SubContractProfile
	 * @generated
	 */
	public Adapter createSubContractProfileAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.adp.CustomSubProfileAttributes <em>Custom Sub Profile Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.adp.CustomSubProfileAttributes
	 * @generated
	 */
	public Adapter createCustomSubProfileAttributesAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.adp.CargoByQuarterDistributionModel <em>Cargo By Quarter Distribution Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.adp.CargoByQuarterDistributionModel
	 * @generated
	 */
	public Adapter createCargoByQuarterDistributionModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.adp.CargoIntervalDistributionModel <em>Cargo Interval Distribution Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.adp.CargoIntervalDistributionModel
	 * @generated
	 */
	public Adapter createCargoIntervalDistributionModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.adp.BindingRule <em>Binding Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.adp.BindingRule
	 * @generated
	 */
	public Adapter createBindingRuleAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.adp.FlowType <em>Flow Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.adp.FlowType
	 * @generated
	 */
	public Adapter createFlowTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.adp.SupplyFromFlow <em>Supply From Flow</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.adp.SupplyFromFlow
	 * @generated
	 */
	public Adapter createSupplyFromFlowAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.adp.DeliverToFlow <em>Deliver To Flow</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.adp.DeliverToFlow
	 * @generated
	 */
	public Adapter createDeliverToFlowAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.adp.SupplyFromProfileFlow <em>Supply From Profile Flow</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.adp.SupplyFromProfileFlow
	 * @generated
	 */
	public Adapter createSupplyFromProfileFlowAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.adp.DeliverToProfileFlow <em>Deliver To Profile Flow</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.adp.DeliverToProfileFlow
	 * @generated
	 */
	public Adapter createDeliverToProfileFlowAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.adp.SupplyFromSpotFlow <em>Supply From Spot Flow</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.adp.SupplyFromSpotFlow
	 * @generated
	 */
	public Adapter createSupplyFromSpotFlowAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.adp.DeliverToSpotFlow <em>Deliver To Spot Flow</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.adp.DeliverToSpotFlow
	 * @generated
	 */
	public Adapter createDeliverToSpotFlowAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.adp.ShippingOption <em>Shipping Option</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.adp.ShippingOption
	 * @generated
	 */
	public Adapter createShippingOptionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //ADPAdapterFactory