/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.impl;
import java.time.LocalDate;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.VolumeMode;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Buy Opportunity</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BuyOpportunityImpl#isDesPurchase <em>Des Purchase</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BuyOpportunityImpl#getPort <em>Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BuyOpportunityImpl#getContract <em>Contract</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BuyOpportunityImpl#getDate <em>Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BuyOpportunityImpl#getPriceExpression <em>Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BuyOpportunityImpl#getEntity <em>Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BuyOpportunityImpl#getCv <em>Cv</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BuyOpportunityImpl#getCancellationExpression <em>Cancellation Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BuyOpportunityImpl#getMiscCosts <em>Misc Costs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BuyOpportunityImpl#getVolumeMode <em>Volume Mode</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BuyOpportunityImpl#getVolumeUnits <em>Volume Units</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BuyOpportunityImpl#getMinVolume <em>Min Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BuyOpportunityImpl#getMaxVolume <em>Max Volume</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BuyOpportunityImpl extends MMXObjectImpl implements BuyOpportunity {
	/**
	 * The default value of the '{@link #isDesPurchase() <em>Des Purchase</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDesPurchase()
	 * @generated
	 * @ordered
	 */
	protected static final boolean DES_PURCHASE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isDesPurchase() <em>Des Purchase</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDesPurchase()
	 * @generated
	 * @ordered
	 */
	protected boolean desPurchase = DES_PURCHASE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getPort() <em>Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPort()
	 * @generated
	 * @ordered
	 */
	protected Port port;

	/**
	 * The cached value of the '{@link #getContract() <em>Contract</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContract()
	 * @generated
	 * @ordered
	 */
	protected PurchaseContract contract;

	/**
	 * The default value of the '{@link #getDate() <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDate()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDate() <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDate()
	 * @generated
	 * @ordered
	 */
	protected LocalDate date = DATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getPriceExpression() <em>Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriceExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String PRICE_EXPRESSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPriceExpression() <em>Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriceExpression()
	 * @generated
	 * @ordered
	 */
	protected String priceExpression = PRICE_EXPRESSION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getEntity() <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntity()
	 * @generated
	 * @ordered
	 */
	protected BaseLegalEntity entity;

	/**
	 * The default value of the '{@link #getCv() <em>Cv</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCv()
	 * @generated
	 * @ordered
	 */
	protected static final double CV_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getCv() <em>Cv</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCv()
	 * @generated
	 * @ordered
	 */
	protected double cv = CV_EDEFAULT;

	/**
	 * The default value of the '{@link #getCancellationExpression() <em>Cancellation Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCancellationExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String CANCELLATION_EXPRESSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCancellationExpression() <em>Cancellation Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCancellationExpression()
	 * @generated
	 * @ordered
	 */
	protected String cancellationExpression = CANCELLATION_EXPRESSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getMiscCosts() <em>Misc Costs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMiscCosts()
	 * @generated
	 * @ordered
	 */
	protected static final int MISC_COSTS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMiscCosts() <em>Misc Costs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMiscCosts()
	 * @generated
	 * @ordered
	 */
	protected int miscCosts = MISC_COSTS_EDEFAULT;

	/**
	 * The default value of the '{@link #getVolumeMode() <em>Volume Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeMode()
	 * @generated
	 * @ordered
	 */
	protected static final VolumeMode VOLUME_MODE_EDEFAULT = VolumeMode.NOT_SPECIFIED;

	/**
	 * The cached value of the '{@link #getVolumeMode() <em>Volume Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeMode()
	 * @generated
	 * @ordered
	 */
	protected VolumeMode volumeMode = VOLUME_MODE_EDEFAULT;

	/**
	 * The default value of the '{@link #getVolumeUnits() <em>Volume Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeUnits()
	 * @generated
	 * @ordered
	 */
	protected static final VolumeUnits VOLUME_UNITS_EDEFAULT = VolumeUnits.M3;

	/**
	 * The cached value of the '{@link #getVolumeUnits() <em>Volume Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeUnits()
	 * @generated
	 * @ordered
	 */
	protected VolumeUnits volumeUnits = VOLUME_UNITS_EDEFAULT;

	/**
	 * The default value of the '{@link #getMinVolume() <em>Min Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinVolume()
	 * @generated
	 * @ordered
	 */
	protected static final int MIN_VOLUME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMinVolume() <em>Min Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinVolume()
	 * @generated
	 * @ordered
	 */
	protected int minVolume = MIN_VOLUME_EDEFAULT;

	/**
	 * The default value of the '{@link #getMaxVolume() <em>Max Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxVolume()
	 * @generated
	 * @ordered
	 */
	protected static final int MAX_VOLUME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMaxVolume() <em>Max Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxVolume()
	 * @generated
	 * @ordered
	 */
	protected int maxVolume = MAX_VOLUME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BuyOpportunityImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.BUY_OPPORTUNITY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isDesPurchase() {
		return desPurchase;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDesPurchase(boolean newDesPurchase) {
		boolean oldDesPurchase = desPurchase;
		desPurchase = newDesPurchase;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BUY_OPPORTUNITY__DES_PURCHASE, oldDesPurchase, desPurchase));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Port getPort() {
		if (port != null && port.eIsProxy()) {
			InternalEObject oldPort = (InternalEObject)port;
			port = (Port)eResolveProxy(oldPort);
			if (port != oldPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.BUY_OPPORTUNITY__PORT, oldPort, port));
			}
		}
		return port;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetPort() {
		return port;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPort(Port newPort) {
		Port oldPort = port;
		port = newPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BUY_OPPORTUNITY__PORT, oldPort, port));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PurchaseContract getContract() {
		if (contract != null && contract.eIsProxy()) {
			InternalEObject oldContract = (InternalEObject)contract;
			contract = (PurchaseContract)eResolveProxy(oldContract);
			if (contract != oldContract) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.BUY_OPPORTUNITY__CONTRACT, oldContract, contract));
			}
		}
		return contract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PurchaseContract basicGetContract() {
		return contract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setContract(PurchaseContract newContract) {
		PurchaseContract oldContract = contract;
		contract = newContract;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BUY_OPPORTUNITY__CONTRACT, oldContract, contract));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDate getDate() {
		return date;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDate(LocalDate newDate) {
		LocalDate oldDate = date;
		date = newDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BUY_OPPORTUNITY__DATE, oldDate, date));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getPriceExpression() {
		return priceExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPriceExpression(String newPriceExpression) {
		String oldPriceExpression = priceExpression;
		priceExpression = newPriceExpression;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BUY_OPPORTUNITY__PRICE_EXPRESSION, oldPriceExpression, priceExpression));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseLegalEntity getEntity() {
		if (entity != null && entity.eIsProxy()) {
			InternalEObject oldEntity = (InternalEObject)entity;
			entity = (BaseLegalEntity)eResolveProxy(oldEntity);
			if (entity != oldEntity) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.BUY_OPPORTUNITY__ENTITY, oldEntity, entity));
			}
		}
		return entity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseLegalEntity basicGetEntity() {
		return entity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEntity(BaseLegalEntity newEntity) {
		BaseLegalEntity oldEntity = entity;
		entity = newEntity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BUY_OPPORTUNITY__ENTITY, oldEntity, entity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getCv() {
		return cv;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCv(double newCv) {
		double oldCv = cv;
		cv = newCv;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BUY_OPPORTUNITY__CV, oldCv, cv));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getCancellationExpression() {
		return cancellationExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCancellationExpression(String newCancellationExpression) {
		String oldCancellationExpression = cancellationExpression;
		cancellationExpression = newCancellationExpression;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BUY_OPPORTUNITY__CANCELLATION_EXPRESSION, oldCancellationExpression, cancellationExpression));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMiscCosts() {
		return miscCosts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMiscCosts(int newMiscCosts) {
		int oldMiscCosts = miscCosts;
		miscCosts = newMiscCosts;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BUY_OPPORTUNITY__MISC_COSTS, oldMiscCosts, miscCosts));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VolumeMode getVolumeMode() {
		return volumeMode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVolumeMode(VolumeMode newVolumeMode) {
		VolumeMode oldVolumeMode = volumeMode;
		volumeMode = newVolumeMode == null ? VOLUME_MODE_EDEFAULT : newVolumeMode;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BUY_OPPORTUNITY__VOLUME_MODE, oldVolumeMode, volumeMode));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VolumeUnits getVolumeUnits() {
		return volumeUnits;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVolumeUnits(VolumeUnits newVolumeUnits) {
		VolumeUnits oldVolumeUnits = volumeUnits;
		volumeUnits = newVolumeUnits == null ? VOLUME_UNITS_EDEFAULT : newVolumeUnits;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BUY_OPPORTUNITY__VOLUME_UNITS, oldVolumeUnits, volumeUnits));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMinVolume() {
		return minVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinVolume(int newMinVolume) {
		int oldMinVolume = minVolume;
		minVolume = newMinVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BUY_OPPORTUNITY__MIN_VOLUME, oldMinVolume, minVolume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMaxVolume() {
		return maxVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxVolume(int newMaxVolume) {
		int oldMaxVolume = maxVolume;
		maxVolume = newMaxVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BUY_OPPORTUNITY__MAX_VOLUME, oldMaxVolume, maxVolume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AnalyticsPackage.BUY_OPPORTUNITY__DES_PURCHASE:
				return isDesPurchase();
			case AnalyticsPackage.BUY_OPPORTUNITY__PORT:
				if (resolve) return getPort();
				return basicGetPort();
			case AnalyticsPackage.BUY_OPPORTUNITY__CONTRACT:
				if (resolve) return getContract();
				return basicGetContract();
			case AnalyticsPackage.BUY_OPPORTUNITY__DATE:
				return getDate();
			case AnalyticsPackage.BUY_OPPORTUNITY__PRICE_EXPRESSION:
				return getPriceExpression();
			case AnalyticsPackage.BUY_OPPORTUNITY__ENTITY:
				if (resolve) return getEntity();
				return basicGetEntity();
			case AnalyticsPackage.BUY_OPPORTUNITY__CV:
				return getCv();
			case AnalyticsPackage.BUY_OPPORTUNITY__CANCELLATION_EXPRESSION:
				return getCancellationExpression();
			case AnalyticsPackage.BUY_OPPORTUNITY__MISC_COSTS:
				return getMiscCosts();
			case AnalyticsPackage.BUY_OPPORTUNITY__VOLUME_MODE:
				return getVolumeMode();
			case AnalyticsPackage.BUY_OPPORTUNITY__VOLUME_UNITS:
				return getVolumeUnits();
			case AnalyticsPackage.BUY_OPPORTUNITY__MIN_VOLUME:
				return getMinVolume();
			case AnalyticsPackage.BUY_OPPORTUNITY__MAX_VOLUME:
				return getMaxVolume();
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
			case AnalyticsPackage.BUY_OPPORTUNITY__DES_PURCHASE:
				setDesPurchase((Boolean)newValue);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__PORT:
				setPort((Port)newValue);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__CONTRACT:
				setContract((PurchaseContract)newValue);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__DATE:
				setDate((LocalDate)newValue);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__PRICE_EXPRESSION:
				setPriceExpression((String)newValue);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__ENTITY:
				setEntity((BaseLegalEntity)newValue);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__CV:
				setCv((Double)newValue);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__CANCELLATION_EXPRESSION:
				setCancellationExpression((String)newValue);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__MISC_COSTS:
				setMiscCosts((Integer)newValue);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__VOLUME_MODE:
				setVolumeMode((VolumeMode)newValue);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__VOLUME_UNITS:
				setVolumeUnits((VolumeUnits)newValue);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__MIN_VOLUME:
				setMinVolume((Integer)newValue);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__MAX_VOLUME:
				setMaxVolume((Integer)newValue);
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
			case AnalyticsPackage.BUY_OPPORTUNITY__DES_PURCHASE:
				setDesPurchase(DES_PURCHASE_EDEFAULT);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__PORT:
				setPort((Port)null);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__CONTRACT:
				setContract((PurchaseContract)null);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__DATE:
				setDate(DATE_EDEFAULT);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__PRICE_EXPRESSION:
				setPriceExpression(PRICE_EXPRESSION_EDEFAULT);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__ENTITY:
				setEntity((BaseLegalEntity)null);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__CV:
				setCv(CV_EDEFAULT);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__CANCELLATION_EXPRESSION:
				setCancellationExpression(CANCELLATION_EXPRESSION_EDEFAULT);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__MISC_COSTS:
				setMiscCosts(MISC_COSTS_EDEFAULT);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__VOLUME_MODE:
				setVolumeMode(VOLUME_MODE_EDEFAULT);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__VOLUME_UNITS:
				setVolumeUnits(VOLUME_UNITS_EDEFAULT);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__MIN_VOLUME:
				setMinVolume(MIN_VOLUME_EDEFAULT);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__MAX_VOLUME:
				setMaxVolume(MAX_VOLUME_EDEFAULT);
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
			case AnalyticsPackage.BUY_OPPORTUNITY__DES_PURCHASE:
				return desPurchase != DES_PURCHASE_EDEFAULT;
			case AnalyticsPackage.BUY_OPPORTUNITY__PORT:
				return port != null;
			case AnalyticsPackage.BUY_OPPORTUNITY__CONTRACT:
				return contract != null;
			case AnalyticsPackage.BUY_OPPORTUNITY__DATE:
				return DATE_EDEFAULT == null ? date != null : !DATE_EDEFAULT.equals(date);
			case AnalyticsPackage.BUY_OPPORTUNITY__PRICE_EXPRESSION:
				return PRICE_EXPRESSION_EDEFAULT == null ? priceExpression != null : !PRICE_EXPRESSION_EDEFAULT.equals(priceExpression);
			case AnalyticsPackage.BUY_OPPORTUNITY__ENTITY:
				return entity != null;
			case AnalyticsPackage.BUY_OPPORTUNITY__CV:
				return cv != CV_EDEFAULT;
			case AnalyticsPackage.BUY_OPPORTUNITY__CANCELLATION_EXPRESSION:
				return CANCELLATION_EXPRESSION_EDEFAULT == null ? cancellationExpression != null : !CANCELLATION_EXPRESSION_EDEFAULT.equals(cancellationExpression);
			case AnalyticsPackage.BUY_OPPORTUNITY__MISC_COSTS:
				return miscCosts != MISC_COSTS_EDEFAULT;
			case AnalyticsPackage.BUY_OPPORTUNITY__VOLUME_MODE:
				return volumeMode != VOLUME_MODE_EDEFAULT;
			case AnalyticsPackage.BUY_OPPORTUNITY__VOLUME_UNITS:
				return volumeUnits != VOLUME_UNITS_EDEFAULT;
			case AnalyticsPackage.BUY_OPPORTUNITY__MIN_VOLUME:
				return minVolume != MIN_VOLUME_EDEFAULT;
			case AnalyticsPackage.BUY_OPPORTUNITY__MAX_VOLUME:
				return maxVolume != MAX_VOLUME_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (desPurchase: ");
		result.append(desPurchase);
		result.append(", date: ");
		result.append(date);
		result.append(", priceExpression: ");
		result.append(priceExpression);
		result.append(", cv: ");
		result.append(cv);
		result.append(", cancellationExpression: ");
		result.append(cancellationExpression);
		result.append(", miscCosts: ");
		result.append(miscCosts);
		result.append(", volumeMode: ");
		result.append(volumeMode);
		result.append(", volumeUnits: ");
		result.append(volumeUnits);
		result.append(", minVolume: ");
		result.append(minVolume);
		result.append(", maxVolume: ");
		result.append(maxVolume);
		result.append(')');
		return result.toString();
	}

} // end of BuyOpportunityImpl

// finish type fixing
