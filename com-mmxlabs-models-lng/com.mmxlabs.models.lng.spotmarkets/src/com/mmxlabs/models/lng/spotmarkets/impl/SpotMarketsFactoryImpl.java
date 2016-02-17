/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarket;
import com.mmxlabs.models.lng.spotmarkets.CharterOutStartDate;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotAvailability;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.spotmarkets.SpotType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SpotMarketsFactoryImpl extends EFactoryImpl implements SpotMarketsFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static SpotMarketsFactory init() {
		try {
			SpotMarketsFactory theSpotMarketsFactory = (SpotMarketsFactory)EPackage.Registry.INSTANCE.getEFactory(SpotMarketsPackage.eNS_URI);
			if (theSpotMarketsFactory != null) {
				return theSpotMarketsFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new SpotMarketsFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotMarketsFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case SpotMarketsPackage.SPOT_MARKETS_MODEL: return createSpotMarketsModel();
			case SpotMarketsPackage.SPOT_MARKET_GROUP: return createSpotMarketGroup();
			case SpotMarketsPackage.DES_PURCHASE_MARKET: return createDESPurchaseMarket();
			case SpotMarketsPackage.DES_SALES_MARKET: return createDESSalesMarket();
			case SpotMarketsPackage.FOB_PURCHASES_MARKET: return createFOBPurchasesMarket();
			case SpotMarketsPackage.FOB_SALES_MARKET: return createFOBSalesMarket();
			case SpotMarketsPackage.SPOT_AVAILABILITY: return createSpotAvailability();
			case SpotMarketsPackage.CHARTER_OUT_START_DATE: return createCharterOutStartDate();
			case SpotMarketsPackage.CHARTER_OUT_MARKET: return createCharterOutMarket();
			case SpotMarketsPackage.CHARTER_IN_MARKET: return createCharterInMarket();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case SpotMarketsPackage.SPOT_TYPE:
				return createSpotTypeFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case SpotMarketsPackage.SPOT_TYPE:
				return convertSpotTypeToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotMarketsModel createSpotMarketsModel() {
		SpotMarketsModelImpl spotMarketsModel = new SpotMarketsModelImpl();
		return spotMarketsModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotMarketGroup createSpotMarketGroup() {
		SpotMarketGroupImpl spotMarketGroup = new SpotMarketGroupImpl();
		return spotMarketGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DESPurchaseMarket createDESPurchaseMarket() {
		DESPurchaseMarketImpl desPurchaseMarket = new DESPurchaseMarketImpl();
		return desPurchaseMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DESSalesMarket createDESSalesMarket() {
		DESSalesMarketImpl desSalesMarket = new DESSalesMarketImpl();
		return desSalesMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FOBPurchasesMarket createFOBPurchasesMarket() {
		FOBPurchasesMarketImpl fobPurchasesMarket = new FOBPurchasesMarketImpl();
		return fobPurchasesMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FOBSalesMarket createFOBSalesMarket() {
		FOBSalesMarketImpl fobSalesMarket = new FOBSalesMarketImpl();
		return fobSalesMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotAvailability createSpotAvailability() {
		SpotAvailabilityImpl spotAvailability = new SpotAvailabilityImpl();
		return spotAvailability;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CharterOutStartDate createCharterOutStartDate() {
		CharterOutStartDateImpl charterOutStartDate = new CharterOutStartDateImpl();
		return charterOutStartDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CharterOutMarket createCharterOutMarket() {
		CharterOutMarketImpl charterOutMarket = new CharterOutMarketImpl();
		return charterOutMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CharterInMarket createCharterInMarket() {
		CharterInMarketImpl charterInMarket = new CharterInMarketImpl();
		return charterInMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotType createSpotTypeFromString(EDataType eDataType, String initialValue) {
		SpotType result = SpotType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertSpotTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotMarketsPackage getSpotMarketsPackage() {
		return (SpotMarketsPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static SpotMarketsPackage getPackage() {
		return SpotMarketsPackage.eINSTANCE;
	}

} //SpotMarketsFactoryImpl
