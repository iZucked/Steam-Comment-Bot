/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.impl;
import java.time.LocalDate;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.VolumeMode;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.mmxcore.MMXObject.DelegateInformation;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Buy Opportunity</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BuyOpportunityImpl#getName <em>Name</em>}</li>
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
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BuyOpportunityImpl#getMinVolume <em>Min Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BuyOpportunityImpl#getMaxVolume <em>Max Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BuyOpportunityImpl#getVolumeUnits <em>Volume Units</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BuyOpportunityImpl#isSpecifyWindow <em>Specify Window</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BuyOpportunityImpl#getWindowSize <em>Window Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.BuyOpportunityImpl#getWindowSizeUnits <em>Window Size Units</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BuyOpportunityImpl extends UUIDObjectImpl implements BuyOpportunity {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * This is true if the Name attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean nameESet;

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
	 * This is true if the Cv attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean cvESet;

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
	 * The default value of the '{@link #isSpecifyWindow() <em>Specify Window</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSpecifyWindow()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SPECIFY_WINDOW_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isSpecifyWindow() <em>Specify Window</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSpecifyWindow()
	 * @generated
	 * @ordered
	 */
	protected boolean specifyWindow = SPECIFY_WINDOW_EDEFAULT;

	/**
	 * The default value of the '{@link #getWindowSize() <em>Window Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowSize()
	 * @generated
	 * @ordered
	 */
	protected static final int WINDOW_SIZE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getWindowSize() <em>Window Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowSize()
	 * @generated
	 * @ordered
	 */
	protected int windowSize = WINDOW_SIZE_EDEFAULT;

	/**
	 * The default value of the '{@link #getWindowSizeUnits() <em>Window Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowSizeUnits()
	 * @generated
	 * @ordered
	 */
	protected static final TimePeriod WINDOW_SIZE_UNITS_EDEFAULT = TimePeriod.HOURS;

	/**
	 * The cached value of the '{@link #getWindowSizeUnits() <em>Window Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowSizeUnits()
	 * @generated
	 * @ordered
	 */
	protected TimePeriod windowSizeUnits = WINDOW_SIZE_UNITS_EDEFAULT;

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
	@Override
	public boolean isDesPurchase() {
		return desPurchase;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
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
	@Override
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
	@Override
	public double getCv() {
		return cv;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCv(double newCv) {
		double oldCv = cv;
		cv = newCv;
		boolean oldCvESet = cvESet;
		cvESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BUY_OPPORTUNITY__CV, oldCv, cv, !oldCvESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetCv() {
		double oldCv = cv;
		boolean oldCvESet = cvESet;
		cv = CV_EDEFAULT;
		cvESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, AnalyticsPackage.BUY_OPPORTUNITY__CV, oldCv, CV_EDEFAULT, oldCvESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetCv() {
		return cvESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getCancellationExpression() {
		return cancellationExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
	public int getMiscCosts() {
		return miscCosts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
	public VolumeMode getVolumeMode() {
		return volumeMode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
	public VolumeUnits getVolumeUnits() {
		return volumeUnits;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
	public int getMinVolume() {
		return minVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
	public int getMaxVolume() {
		return maxVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	public boolean isSpecifyWindow() {
		return specifyWindow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSpecifyWindow(boolean newSpecifyWindow) {
		boolean oldSpecifyWindow = specifyWindow;
		specifyWindow = newSpecifyWindow;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BUY_OPPORTUNITY__SPECIFY_WINDOW, oldSpecifyWindow, specifyWindow));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getWindowSize() {
		return windowSize;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setWindowSize(int newWindowSize) {
		int oldWindowSize = windowSize;
		windowSize = newWindowSize;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BUY_OPPORTUNITY__WINDOW_SIZE, oldWindowSize, windowSize));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TimePeriod getWindowSizeUnits() {
		return windowSizeUnits;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setWindowSizeUnits(TimePeriod newWindowSizeUnits) {
		TimePeriod oldWindowSizeUnits = windowSizeUnits;
		windowSizeUnits = newWindowSizeUnits == null ? WINDOW_SIZE_UNITS_EDEFAULT : newWindowSizeUnits;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BUY_OPPORTUNITY__WINDOW_SIZE_UNITS, oldWindowSizeUnits, windowSizeUnits));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		boolean oldNameESet = nameESet;
		nameESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.BUY_OPPORTUNITY__NAME, oldName, name, !oldNameESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetName() {
		String oldName = name;
		boolean oldNameESet = nameESet;
		name = NAME_EDEFAULT;
		nameESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, AnalyticsPackage.BUY_OPPORTUNITY__NAME, oldName, NAME_EDEFAULT, oldNameESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetName() {
		return nameESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AnalyticsPackage.BUY_OPPORTUNITY__NAME:
				return getName();
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
			case AnalyticsPackage.BUY_OPPORTUNITY__MIN_VOLUME:
				return getMinVolume();
			case AnalyticsPackage.BUY_OPPORTUNITY__MAX_VOLUME:
				return getMaxVolume();
			case AnalyticsPackage.BUY_OPPORTUNITY__VOLUME_UNITS:
				return getVolumeUnits();
			case AnalyticsPackage.BUY_OPPORTUNITY__SPECIFY_WINDOW:
				return isSpecifyWindow();
			case AnalyticsPackage.BUY_OPPORTUNITY__WINDOW_SIZE:
				return getWindowSize();
			case AnalyticsPackage.BUY_OPPORTUNITY__WINDOW_SIZE_UNITS:
				return getWindowSizeUnits();
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
			case AnalyticsPackage.BUY_OPPORTUNITY__NAME:
				setName((String)newValue);
				return;
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
			case AnalyticsPackage.BUY_OPPORTUNITY__MIN_VOLUME:
				setMinVolume((Integer)newValue);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__MAX_VOLUME:
				setMaxVolume((Integer)newValue);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__VOLUME_UNITS:
				setVolumeUnits((VolumeUnits)newValue);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__SPECIFY_WINDOW:
				setSpecifyWindow((Boolean)newValue);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__WINDOW_SIZE:
				setWindowSize((Integer)newValue);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__WINDOW_SIZE_UNITS:
				setWindowSizeUnits((TimePeriod)newValue);
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
			case AnalyticsPackage.BUY_OPPORTUNITY__NAME:
				unsetName();
				return;
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
				unsetCv();
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
			case AnalyticsPackage.BUY_OPPORTUNITY__MIN_VOLUME:
				setMinVolume(MIN_VOLUME_EDEFAULT);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__MAX_VOLUME:
				setMaxVolume(MAX_VOLUME_EDEFAULT);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__VOLUME_UNITS:
				setVolumeUnits(VOLUME_UNITS_EDEFAULT);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__SPECIFY_WINDOW:
				setSpecifyWindow(SPECIFY_WINDOW_EDEFAULT);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__WINDOW_SIZE:
				setWindowSize(WINDOW_SIZE_EDEFAULT);
				return;
			case AnalyticsPackage.BUY_OPPORTUNITY__WINDOW_SIZE_UNITS:
				setWindowSizeUnits(WINDOW_SIZE_UNITS_EDEFAULT);
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
			case AnalyticsPackage.BUY_OPPORTUNITY__NAME:
				return isSetName();
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
				return isSetCv();
			case AnalyticsPackage.BUY_OPPORTUNITY__CANCELLATION_EXPRESSION:
				return CANCELLATION_EXPRESSION_EDEFAULT == null ? cancellationExpression != null : !CANCELLATION_EXPRESSION_EDEFAULT.equals(cancellationExpression);
			case AnalyticsPackage.BUY_OPPORTUNITY__MISC_COSTS:
				return miscCosts != MISC_COSTS_EDEFAULT;
			case AnalyticsPackage.BUY_OPPORTUNITY__VOLUME_MODE:
				return volumeMode != VOLUME_MODE_EDEFAULT;
			case AnalyticsPackage.BUY_OPPORTUNITY__MIN_VOLUME:
				return minVolume != MIN_VOLUME_EDEFAULT;
			case AnalyticsPackage.BUY_OPPORTUNITY__MAX_VOLUME:
				return maxVolume != MAX_VOLUME_EDEFAULT;
			case AnalyticsPackage.BUY_OPPORTUNITY__VOLUME_UNITS:
				return volumeUnits != VOLUME_UNITS_EDEFAULT;
			case AnalyticsPackage.BUY_OPPORTUNITY__SPECIFY_WINDOW:
				return specifyWindow != SPECIFY_WINDOW_EDEFAULT;
			case AnalyticsPackage.BUY_OPPORTUNITY__WINDOW_SIZE:
				return windowSize != WINDOW_SIZE_EDEFAULT;
			case AnalyticsPackage.BUY_OPPORTUNITY__WINDOW_SIZE_UNITS:
				return windowSizeUnits != WINDOW_SIZE_UNITS_EDEFAULT;
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

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (name: ");
		if (nameESet) result.append(name); else result.append("<unset>");
		result.append(", desPurchase: ");
		result.append(desPurchase);
		result.append(", date: ");
		result.append(date);
		result.append(", priceExpression: ");
		result.append(priceExpression);
		result.append(", cv: ");
		if (cvESet) result.append(cv); else result.append("<unset>");
		result.append(", cancellationExpression: ");
		result.append(cancellationExpression);
		result.append(", miscCosts: ");
		result.append(miscCosts);
		result.append(", volumeMode: ");
		result.append(volumeMode);
		result.append(", minVolume: ");
		result.append(minVolume);
		result.append(", maxVolume: ");
		result.append(maxVolume);
		result.append(", volumeUnits: ");
		result.append(volumeUnits);
		result.append(", specifyWindow: ");
		result.append(specifyWindow);
		result.append(", windowSize: ");
		result.append(windowSize);
		result.append(", windowSizeUnits: ");
		result.append(windowSizeUnits);
		result.append(')');
		return result.toString();
	}

	@Override
	public DelegateInformation getUnsetValueOrDelegate(EStructuralFeature feature) {
		if (feature == AnalyticsPackage.Literals.BUY_OPPORTUNITY__CV) {
			return new DelegateInformation(null, null, null) {
				@Override
				public boolean delegatesTo(final Object changedFeature) {
					return (changedFeature == AnalyticsPackage.Literals.BUY_OPPORTUNITY__CONTRACT || changedFeature == AnalyticsPackage.Literals.BUY_OPPORTUNITY__PORT);
				}
				
				@Override
				public Object getValue(final EObject object) {
					Object result = null;
					final PurchaseContract purchaseContract =  getContract();
					if (purchaseContract != null) {
						// Get value if set, otherwise we want to fall back to current load port, not contract fallback.
						if (purchaseContract.eIsSet(CommercialPackage.Literals.PURCHASE_CONTRACT__CARGO_CV)) {
							result = purchaseContract.eGet(CommercialPackage.Literals.PURCHASE_CONTRACT__CARGO_CV);
						}
					}
					if (result == null && port != null) {
						result =  port.eGetWithDefault(PortPackage.Literals.PORT__CV_VALUE);
					}
					if (result == null) {
						result = 0.0;
					}
					return result;
					
				}				
			};
		}
		return super.getUnsetValueOrDelegate(feature);
	}
	
} // end of BuyOpportunityImpl

// finish type fixing
