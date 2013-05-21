/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;

import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.FleetCostModel;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PortCostEntry;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.pricing.PricingPackage
 * @generated
 */
public class PricingSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static PricingPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PricingSwitch() {
		if (modelPackage == null) {
			modelPackage = PricingPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @parameter ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case PricingPackage.PRICING_MODEL: {
				PricingModel pricingModel = (PricingModel)theEObject;
				T result = casePricingModel(pricingModel);
				if (result == null) result = caseUUIDObject(pricingModel);
				if (result == null) result = caseMMXObject(pricingModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.DATA_INDEX: {
				DataIndex<?> dataIndex = (DataIndex<?>)theEObject;
				T result = caseDataIndex(dataIndex);
				if (result == null) result = caseIndex(dataIndex);
				if (result == null) result = caseUUIDObject(dataIndex);
				if (result == null) result = caseNamedObject(dataIndex);
				if (result == null) result = caseMMXObject(dataIndex);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.DERIVED_INDEX: {
				DerivedIndex<?> derivedIndex = (DerivedIndex<?>)theEObject;
				T result = caseDerivedIndex(derivedIndex);
				if (result == null) result = caseIndex(derivedIndex);
				if (result == null) result = caseUUIDObject(derivedIndex);
				if (result == null) result = caseNamedObject(derivedIndex);
				if (result == null) result = caseMMXObject(derivedIndex);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.INDEX_POINT: {
				IndexPoint<?> indexPoint = (IndexPoint<?>)theEObject;
				T result = caseIndexPoint(indexPoint);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.INDEX: {
				Index<?> index = (Index<?>)theEObject;
				T result = caseIndex(index);
				if (result == null) result = caseUUIDObject(index);
				if (result == null) result = caseNamedObject(index);
				if (result == null) result = caseMMXObject(index);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.FLEET_COST_MODEL: {
				FleetCostModel fleetCostModel = (FleetCostModel)theEObject;
				T result = caseFleetCostModel(fleetCostModel);
				if (result == null) result = caseMMXObject(fleetCostModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.ROUTE_COST: {
				RouteCost routeCost = (RouteCost)theEObject;
				T result = caseRouteCost(routeCost);
				if (result == null) result = caseMMXObject(routeCost);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.BASE_FUEL_COST: {
				BaseFuelCost baseFuelCost = (BaseFuelCost)theEObject;
				T result = caseBaseFuelCost(baseFuelCost);
				if (result == null) result = caseMMXObject(baseFuelCost);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.PORT_COST: {
				PortCost portCost = (PortCost)theEObject;
				T result = casePortCost(portCost);
				if (result == null) result = caseMMXObject(portCost);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.PORT_COST_ENTRY: {
				PortCostEntry portCostEntry = (PortCostEntry)theEObject;
				T result = casePortCostEntry(portCostEntry);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PricingPackage.COOLDOWN_PRICE: {
				CooldownPrice cooldownPrice = (CooldownPrice)theEObject;
				T result = caseCooldownPrice(cooldownPrice);
				if (result == null) result = caseMMXObject(cooldownPrice);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePricingModel(PricingModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Data Index</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Data Index</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <Value> T caseDataIndex(DataIndex<Value> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Derived Index</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Derived Index</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <Value> T caseDerivedIndex(DerivedIndex<Value> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Index Point</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Index Point</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <Value> T caseIndexPoint(IndexPoint<Value> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Index</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Index</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <Value> T caseIndex(Index<Value> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Fleet Cost Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Fleet Cost Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFleetCostModel(FleetCostModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Route Cost</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Route Cost</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRouteCost(RouteCost object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Base Fuel Cost</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Base Fuel Cost</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBaseFuelCost(BaseFuelCost object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Port Cost</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Port Cost</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePortCost(PortCost object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Port Cost Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Port Cost Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePortCostEntry(PortCostEntry object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Cooldown Price</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Cooldown Price</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCooldownPrice(CooldownPrice object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>MMX Object</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>MMX Object</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMMXObject(MMXObject object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UUID Object</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UUID Object</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUUIDObject(UUIDObject object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Named Object</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Named Object</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNamedObject(NamedObject object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} //PricingSwitch
