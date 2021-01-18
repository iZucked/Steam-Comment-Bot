/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarket;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarketParameters;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsModelImpl#getDesPurchaseSpotMarket <em>Des Purchase Spot Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsModelImpl#getDesSalesSpotMarket <em>Des Sales Spot Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsModelImpl#getFobPurchasesSpotMarket <em>Fob Purchases Spot Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsModelImpl#getFobSalesSpotMarket <em>Fob Sales Spot Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsModelImpl#getCharterInMarkets <em>Charter In Markets</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsModelImpl#getCharterOutMarkets <em>Charter Out Markets</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.SpotMarketsModelImpl#getCharterOutMarketParameters <em>Charter Out Market Parameters</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SpotMarketsModelImpl extends UUIDObjectImpl implements SpotMarketsModel {
	/**
	 * The cached value of the '{@link #getDesPurchaseSpotMarket() <em>Des Purchase Spot Market</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDesPurchaseSpotMarket()
	 * @generated
	 * @ordered
	 */
	protected SpotMarketGroup desPurchaseSpotMarket;

	/**
	 * The cached value of the '{@link #getDesSalesSpotMarket() <em>Des Sales Spot Market</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDesSalesSpotMarket()
	 * @generated
	 * @ordered
	 */
	protected SpotMarketGroup desSalesSpotMarket;

	/**
	 * The cached value of the '{@link #getFobPurchasesSpotMarket() <em>Fob Purchases Spot Market</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFobPurchasesSpotMarket()
	 * @generated
	 * @ordered
	 */
	protected SpotMarketGroup fobPurchasesSpotMarket;

	/**
	 * The cached value of the '{@link #getFobSalesSpotMarket() <em>Fob Sales Spot Market</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFobSalesSpotMarket()
	 * @generated
	 * @ordered
	 */
	protected SpotMarketGroup fobSalesSpotMarket;

	/**
	 * The cached value of the '{@link #getCharterInMarkets() <em>Charter In Markets</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterInMarkets()
	 * @generated
	 * @ordered
	 */
	protected EList<CharterInMarket> charterInMarkets;

	/**
	 * The cached value of the '{@link #getCharterOutMarkets() <em>Charter Out Markets</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterOutMarkets()
	 * @generated
	 * @ordered
	 */
	protected EList<CharterOutMarket> charterOutMarkets;

	/**
	 * The cached value of the '{@link #getCharterOutMarketParameters() <em>Charter Out Market Parameters</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterOutMarketParameters()
	 * @generated
	 * @ordered
	 */
	protected CharterOutMarketParameters charterOutMarketParameters;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SpotMarketsModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SpotMarketGroup getDesPurchaseSpotMarket() {
		return desPurchaseSpotMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDesPurchaseSpotMarket(SpotMarketGroup newDesPurchaseSpotMarket, NotificationChain msgs) {
		SpotMarketGroup oldDesPurchaseSpotMarket = desPurchaseSpotMarket;
		desPurchaseSpotMarket = newDesPurchaseSpotMarket;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.SPOT_MARKETS_MODEL__DES_PURCHASE_SPOT_MARKET, oldDesPurchaseSpotMarket, newDesPurchaseSpotMarket);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDesPurchaseSpotMarket(SpotMarketGroup newDesPurchaseSpotMarket) {
		if (newDesPurchaseSpotMarket != desPurchaseSpotMarket) {
			NotificationChain msgs = null;
			if (desPurchaseSpotMarket != null)
				msgs = ((InternalEObject)desPurchaseSpotMarket).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SpotMarketsPackage.SPOT_MARKETS_MODEL__DES_PURCHASE_SPOT_MARKET, null, msgs);
			if (newDesPurchaseSpotMarket != null)
				msgs = ((InternalEObject)newDesPurchaseSpotMarket).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SpotMarketsPackage.SPOT_MARKETS_MODEL__DES_PURCHASE_SPOT_MARKET, null, msgs);
			msgs = basicSetDesPurchaseSpotMarket(newDesPurchaseSpotMarket, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.SPOT_MARKETS_MODEL__DES_PURCHASE_SPOT_MARKET, newDesPurchaseSpotMarket, newDesPurchaseSpotMarket));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SpotMarketGroup getDesSalesSpotMarket() {
		return desSalesSpotMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDesSalesSpotMarket(SpotMarketGroup newDesSalesSpotMarket, NotificationChain msgs) {
		SpotMarketGroup oldDesSalesSpotMarket = desSalesSpotMarket;
		desSalesSpotMarket = newDesSalesSpotMarket;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.SPOT_MARKETS_MODEL__DES_SALES_SPOT_MARKET, oldDesSalesSpotMarket, newDesSalesSpotMarket);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDesSalesSpotMarket(SpotMarketGroup newDesSalesSpotMarket) {
		if (newDesSalesSpotMarket != desSalesSpotMarket) {
			NotificationChain msgs = null;
			if (desSalesSpotMarket != null)
				msgs = ((InternalEObject)desSalesSpotMarket).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SpotMarketsPackage.SPOT_MARKETS_MODEL__DES_SALES_SPOT_MARKET, null, msgs);
			if (newDesSalesSpotMarket != null)
				msgs = ((InternalEObject)newDesSalesSpotMarket).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SpotMarketsPackage.SPOT_MARKETS_MODEL__DES_SALES_SPOT_MARKET, null, msgs);
			msgs = basicSetDesSalesSpotMarket(newDesSalesSpotMarket, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.SPOT_MARKETS_MODEL__DES_SALES_SPOT_MARKET, newDesSalesSpotMarket, newDesSalesSpotMarket));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SpotMarketGroup getFobPurchasesSpotMarket() {
		return fobPurchasesSpotMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetFobPurchasesSpotMarket(SpotMarketGroup newFobPurchasesSpotMarket, NotificationChain msgs) {
		SpotMarketGroup oldFobPurchasesSpotMarket = fobPurchasesSpotMarket;
		fobPurchasesSpotMarket = newFobPurchasesSpotMarket;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.SPOT_MARKETS_MODEL__FOB_PURCHASES_SPOT_MARKET, oldFobPurchasesSpotMarket, newFobPurchasesSpotMarket);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFobPurchasesSpotMarket(SpotMarketGroup newFobPurchasesSpotMarket) {
		if (newFobPurchasesSpotMarket != fobPurchasesSpotMarket) {
			NotificationChain msgs = null;
			if (fobPurchasesSpotMarket != null)
				msgs = ((InternalEObject)fobPurchasesSpotMarket).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SpotMarketsPackage.SPOT_MARKETS_MODEL__FOB_PURCHASES_SPOT_MARKET, null, msgs);
			if (newFobPurchasesSpotMarket != null)
				msgs = ((InternalEObject)newFobPurchasesSpotMarket).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SpotMarketsPackage.SPOT_MARKETS_MODEL__FOB_PURCHASES_SPOT_MARKET, null, msgs);
			msgs = basicSetFobPurchasesSpotMarket(newFobPurchasesSpotMarket, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.SPOT_MARKETS_MODEL__FOB_PURCHASES_SPOT_MARKET, newFobPurchasesSpotMarket, newFobPurchasesSpotMarket));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SpotMarketGroup getFobSalesSpotMarket() {
		return fobSalesSpotMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetFobSalesSpotMarket(SpotMarketGroup newFobSalesSpotMarket, NotificationChain msgs) {
		SpotMarketGroup oldFobSalesSpotMarket = fobSalesSpotMarket;
		fobSalesSpotMarket = newFobSalesSpotMarket;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.SPOT_MARKETS_MODEL__FOB_SALES_SPOT_MARKET, oldFobSalesSpotMarket, newFobSalesSpotMarket);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFobSalesSpotMarket(SpotMarketGroup newFobSalesSpotMarket) {
		if (newFobSalesSpotMarket != fobSalesSpotMarket) {
			NotificationChain msgs = null;
			if (fobSalesSpotMarket != null)
				msgs = ((InternalEObject)fobSalesSpotMarket).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SpotMarketsPackage.SPOT_MARKETS_MODEL__FOB_SALES_SPOT_MARKET, null, msgs);
			if (newFobSalesSpotMarket != null)
				msgs = ((InternalEObject)newFobSalesSpotMarket).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SpotMarketsPackage.SPOT_MARKETS_MODEL__FOB_SALES_SPOT_MARKET, null, msgs);
			msgs = basicSetFobSalesSpotMarket(newFobSalesSpotMarket, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.SPOT_MARKETS_MODEL__FOB_SALES_SPOT_MARKET, newFobSalesSpotMarket, newFobSalesSpotMarket));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<CharterInMarket> getCharterInMarkets() {
		if (charterInMarkets == null) {
			charterInMarkets = new EObjectContainmentEList<CharterInMarket>(CharterInMarket.class, this, SpotMarketsPackage.SPOT_MARKETS_MODEL__CHARTER_IN_MARKETS);
		}
		return charterInMarkets;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<CharterOutMarket> getCharterOutMarkets() {
		if (charterOutMarkets == null) {
			charterOutMarkets = new EObjectContainmentEList<CharterOutMarket>(CharterOutMarket.class, this, SpotMarketsPackage.SPOT_MARKETS_MODEL__CHARTER_OUT_MARKETS);
		}
		return charterOutMarkets;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CharterOutMarketParameters getCharterOutMarketParameters() {
		return charterOutMarketParameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCharterOutMarketParameters(CharterOutMarketParameters newCharterOutMarketParameters, NotificationChain msgs) {
		CharterOutMarketParameters oldCharterOutMarketParameters = charterOutMarketParameters;
		charterOutMarketParameters = newCharterOutMarketParameters;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.SPOT_MARKETS_MODEL__CHARTER_OUT_MARKET_PARAMETERS, oldCharterOutMarketParameters, newCharterOutMarketParameters);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCharterOutMarketParameters(CharterOutMarketParameters newCharterOutMarketParameters) {
		if (newCharterOutMarketParameters != charterOutMarketParameters) {
			NotificationChain msgs = null;
			if (charterOutMarketParameters != null)
				msgs = ((InternalEObject)charterOutMarketParameters).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SpotMarketsPackage.SPOT_MARKETS_MODEL__CHARTER_OUT_MARKET_PARAMETERS, null, msgs);
			if (newCharterOutMarketParameters != null)
				msgs = ((InternalEObject)newCharterOutMarketParameters).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SpotMarketsPackage.SPOT_MARKETS_MODEL__CHARTER_OUT_MARKET_PARAMETERS, null, msgs);
			msgs = basicSetCharterOutMarketParameters(newCharterOutMarketParameters, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.SPOT_MARKETS_MODEL__CHARTER_OUT_MARKET_PARAMETERS, newCharterOutMarketParameters, newCharterOutMarketParameters));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__DES_PURCHASE_SPOT_MARKET:
				return basicSetDesPurchaseSpotMarket(null, msgs);
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__DES_SALES_SPOT_MARKET:
				return basicSetDesSalesSpotMarket(null, msgs);
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__FOB_PURCHASES_SPOT_MARKET:
				return basicSetFobPurchasesSpotMarket(null, msgs);
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__FOB_SALES_SPOT_MARKET:
				return basicSetFobSalesSpotMarket(null, msgs);
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__CHARTER_IN_MARKETS:
				return ((InternalEList<?>)getCharterInMarkets()).basicRemove(otherEnd, msgs);
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__CHARTER_OUT_MARKETS:
				return ((InternalEList<?>)getCharterOutMarkets()).basicRemove(otherEnd, msgs);
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__CHARTER_OUT_MARKET_PARAMETERS:
				return basicSetCharterOutMarketParameters(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__DES_PURCHASE_SPOT_MARKET:
				return getDesPurchaseSpotMarket();
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__DES_SALES_SPOT_MARKET:
				return getDesSalesSpotMarket();
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__FOB_PURCHASES_SPOT_MARKET:
				return getFobPurchasesSpotMarket();
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__FOB_SALES_SPOT_MARKET:
				return getFobSalesSpotMarket();
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__CHARTER_IN_MARKETS:
				return getCharterInMarkets();
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__CHARTER_OUT_MARKETS:
				return getCharterOutMarkets();
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__CHARTER_OUT_MARKET_PARAMETERS:
				return getCharterOutMarketParameters();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__DES_PURCHASE_SPOT_MARKET:
				setDesPurchaseSpotMarket((SpotMarketGroup)newValue);
				return;
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__DES_SALES_SPOT_MARKET:
				setDesSalesSpotMarket((SpotMarketGroup)newValue);
				return;
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__FOB_PURCHASES_SPOT_MARKET:
				setFobPurchasesSpotMarket((SpotMarketGroup)newValue);
				return;
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__FOB_SALES_SPOT_MARKET:
				setFobSalesSpotMarket((SpotMarketGroup)newValue);
				return;
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__CHARTER_IN_MARKETS:
				getCharterInMarkets().clear();
				getCharterInMarkets().addAll((Collection<? extends CharterInMarket>)newValue);
				return;
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__CHARTER_OUT_MARKETS:
				getCharterOutMarkets().clear();
				getCharterOutMarkets().addAll((Collection<? extends CharterOutMarket>)newValue);
				return;
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__CHARTER_OUT_MARKET_PARAMETERS:
				setCharterOutMarketParameters((CharterOutMarketParameters)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__DES_PURCHASE_SPOT_MARKET:
				setDesPurchaseSpotMarket((SpotMarketGroup)null);
				return;
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__DES_SALES_SPOT_MARKET:
				setDesSalesSpotMarket((SpotMarketGroup)null);
				return;
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__FOB_PURCHASES_SPOT_MARKET:
				setFobPurchasesSpotMarket((SpotMarketGroup)null);
				return;
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__FOB_SALES_SPOT_MARKET:
				setFobSalesSpotMarket((SpotMarketGroup)null);
				return;
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__CHARTER_IN_MARKETS:
				getCharterInMarkets().clear();
				return;
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__CHARTER_OUT_MARKETS:
				getCharterOutMarkets().clear();
				return;
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__CHARTER_OUT_MARKET_PARAMETERS:
				setCharterOutMarketParameters((CharterOutMarketParameters)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__DES_PURCHASE_SPOT_MARKET:
				return desPurchaseSpotMarket != null;
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__DES_SALES_SPOT_MARKET:
				return desSalesSpotMarket != null;
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__FOB_PURCHASES_SPOT_MARKET:
				return fobPurchasesSpotMarket != null;
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__FOB_SALES_SPOT_MARKET:
				return fobSalesSpotMarket != null;
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__CHARTER_IN_MARKETS:
				return charterInMarkets != null && !charterInMarkets.isEmpty();
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__CHARTER_OUT_MARKETS:
				return charterOutMarkets != null && !charterOutMarkets.isEmpty();
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__CHARTER_OUT_MARKET_PARAMETERS:
				return charterOutMarketParameters != null;
		}
		return super.eIsSet(featureID);
	}

} // end of SpotMarketsModelImpl

// finish type fixing
