/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.util;

import com.mmxlabs.models.lng.spotmarkets.*;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarket;
import com.mmxlabs.models.lng.spotmarkets.CharterOutStartDate;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotAvailability;
import com.mmxlabs.models.lng.spotmarkets.SpotCharterMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
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
 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage
 * @generated
 */
public class SpotMarketsSwitch<@Nullable T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static SpotMarketsPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotMarketsSwitch() {
		if (modelPackage == null) {
			modelPackage = SpotMarketsPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param ePackage the package in question.
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
			case SpotMarketsPackage.SPOT_MARKETS_MODEL: {
				SpotMarketsModel spotMarketsModel = (SpotMarketsModel)theEObject;
				T result = caseSpotMarketsModel(spotMarketsModel);
				if (result == null) result = caseUUIDObject(spotMarketsModel);
				if (result == null) result = caseMMXObject(spotMarketsModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SpotMarketsPackage.SPOT_MARKET_GROUP: {
				SpotMarketGroup spotMarketGroup = (SpotMarketGroup)theEObject;
				T result = caseSpotMarketGroup(spotMarketGroup);
				if (result == null) result = caseMMXObject(spotMarketGroup);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SpotMarketsPackage.SPOT_MARKET: {
				SpotMarket spotMarket = (SpotMarket)theEObject;
				T result = caseSpotMarket(spotMarket);
				if (result == null) result = caseUUIDObject(spotMarket);
				if (result == null) result = caseNamedObject(spotMarket);
				if (result == null) result = caseMMXObject(spotMarket);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SpotMarketsPackage.DES_PURCHASE_MARKET: {
				DESPurchaseMarket desPurchaseMarket = (DESPurchaseMarket)theEObject;
				T result = caseDESPurchaseMarket(desPurchaseMarket);
				if (result == null) result = caseSpotMarket(desPurchaseMarket);
				if (result == null) result = caseUUIDObject(desPurchaseMarket);
				if (result == null) result = caseNamedObject(desPurchaseMarket);
				if (result == null) result = caseMMXObject(desPurchaseMarket);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SpotMarketsPackage.DES_SALES_MARKET: {
				DESSalesMarket desSalesMarket = (DESSalesMarket)theEObject;
				T result = caseDESSalesMarket(desSalesMarket);
				if (result == null) result = caseSpotMarket(desSalesMarket);
				if (result == null) result = caseUUIDObject(desSalesMarket);
				if (result == null) result = caseNamedObject(desSalesMarket);
				if (result == null) result = caseMMXObject(desSalesMarket);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SpotMarketsPackage.FOB_PURCHASES_MARKET: {
				FOBPurchasesMarket fobPurchasesMarket = (FOBPurchasesMarket)theEObject;
				T result = caseFOBPurchasesMarket(fobPurchasesMarket);
				if (result == null) result = caseSpotMarket(fobPurchasesMarket);
				if (result == null) result = caseUUIDObject(fobPurchasesMarket);
				if (result == null) result = caseNamedObject(fobPurchasesMarket);
				if (result == null) result = caseMMXObject(fobPurchasesMarket);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SpotMarketsPackage.FOB_SALES_MARKET: {
				FOBSalesMarket fobSalesMarket = (FOBSalesMarket)theEObject;
				T result = caseFOBSalesMarket(fobSalesMarket);
				if (result == null) result = caseSpotMarket(fobSalesMarket);
				if (result == null) result = caseUUIDObject(fobSalesMarket);
				if (result == null) result = caseNamedObject(fobSalesMarket);
				if (result == null) result = caseMMXObject(fobSalesMarket);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SpotMarketsPackage.SPOT_AVAILABILITY: {
				SpotAvailability spotAvailability = (SpotAvailability)theEObject;
				T result = caseSpotAvailability(spotAvailability);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SpotMarketsPackage.CHARTER_OUT_START_DATE: {
				CharterOutStartDate charterOutStartDate = (CharterOutStartDate)theEObject;
				T result = caseCharterOutStartDate(charterOutStartDate);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SpotMarketsPackage.CHARTER_OUT_MARKET: {
				CharterOutMarket charterOutMarket = (CharterOutMarket)theEObject;
				T result = caseCharterOutMarket(charterOutMarket);
				if (result == null) result = caseSpotCharterMarket(charterOutMarket);
				if (result == null) result = caseNamedObject(charterOutMarket);
				if (result == null) result = caseMMXObject(charterOutMarket);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SpotMarketsPackage.CHARTER_IN_MARKET: {
				CharterInMarket charterInMarket = (CharterInMarket)theEObject;
				T result = caseCharterInMarket(charterInMarket);
				if (result == null) result = caseSpotCharterMarket(charterInMarket);
				if (result == null) result = caseVesselAssignmentType(charterInMarket);
				if (result == null) result = caseNamedObject(charterInMarket);
				if (result == null) result = caseMMXObject(charterInMarket);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SpotMarketsPackage.SPOT_CHARTER_MARKET: {
				SpotCharterMarket spotCharterMarket = (SpotCharterMarket)theEObject;
				T result = caseSpotCharterMarket(spotCharterMarket);
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
	public T caseSpotMarketsModel(SpotMarketsModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Spot Market Group</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Spot Market Group</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSpotMarketGroup(SpotMarketGroup object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Spot Market</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Spot Market</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSpotMarket(SpotMarket object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>DES Purchase Market</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>DES Purchase Market</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDESPurchaseMarket(DESPurchaseMarket object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>DES Sales Market</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>DES Sales Market</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDESSalesMarket(DESSalesMarket object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>FOB Purchases Market</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>FOB Purchases Market</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFOBPurchasesMarket(FOBPurchasesMarket object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>FOB Sales Market</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>FOB Sales Market</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFOBSalesMarket(FOBSalesMarket object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Spot Availability</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Spot Availability</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSpotAvailability(SpotAvailability object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Charter Out Start Date</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Charter Out Start Date</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCharterOutStartDate(CharterOutStartDate object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Charter Out Market</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Charter Out Market</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCharterOutMarket(CharterOutMarket object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Charter In Market</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Charter In Market</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCharterInMarket(CharterInMarket object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Spot Charter Market</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Spot Charter Market</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSpotCharterMarket(SpotCharterMarket object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>Vessel Assignment Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Vessel Assignment Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVesselAssignmentType(VesselAssignmentType object) {
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

} //SpotMarketsSwitch
