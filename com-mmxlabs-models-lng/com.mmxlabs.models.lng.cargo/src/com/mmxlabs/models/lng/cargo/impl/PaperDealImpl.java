/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.PaperDeal;

import com.mmxlabs.models.lng.cargo.PaperPricingType;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.pricing.PricingCalendar;
import com.mmxlabs.models.lng.pricing.SettleStrategy;
import com.mmxlabs.models.mmxcore.impl.NamedObjectImpl;
import java.time.LocalDate;

import java.time.YearMonth;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Paper Deal</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.PaperDealImpl#getPrice <em>Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.PaperDealImpl#getPricingType <em>Pricing Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.PaperDealImpl#getIndex <em>Index</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.PaperDealImpl#getInstrument <em>Instrument</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.PaperDealImpl#getQuantity <em>Quantity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.PaperDealImpl#getPricingMonth <em>Pricing Month</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.PaperDealImpl#getEntity <em>Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.PaperDealImpl#getYear <em>Year</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.PaperDealImpl#getComment <em>Comment</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.PaperDealImpl#getPricingPeriodStart <em>Pricing Period Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.PaperDealImpl#getPricingPeriodEnd <em>Pricing Period End</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.PaperDealImpl#getHedgingPeriodStart <em>Hedging Period Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.PaperDealImpl#getHedgingPeriodEnd <em>Hedging Period End</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.PaperDealImpl#getPricingCalendar <em>Pricing Calendar</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class PaperDealImpl extends NamedObjectImpl implements PaperDeal {
	/**
	 * The default value of the '{@link #getPrice() <em>Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrice()
	 * @generated
	 * @ordered
	 */
	protected static final double PRICE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getPrice() <em>Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrice()
	 * @generated
	 * @ordered
	 */
	protected double price = PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getPricingType() <em>Pricing Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPricingType()
	 * @generated
	 * @ordered
	 */
	protected static final PaperPricingType PRICING_TYPE_EDEFAULT = PaperPricingType.PERIOD_AVG;

	/**
	 * The cached value of the '{@link #getPricingType() <em>Pricing Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPricingType()
	 * @generated
	 * @ordered
	 */
	protected PaperPricingType pricingType = PRICING_TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getIndex() <em>Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndex()
	 * @generated
	 * @ordered
	 */
	protected static final String INDEX_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getIndex() <em>Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndex()
	 * @generated
	 * @ordered
	 */
	protected String index = INDEX_EDEFAULT;

	/**
	 * The cached value of the '{@link #getInstrument() <em>Instrument</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInstrument()
	 * @generated
	 * @ordered
	 */
	protected SettleStrategy instrument;

	/**
	 * This is true if the Instrument reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean instrumentESet;

	/**
	 * The default value of the '{@link #getQuantity() <em>Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQuantity()
	 * @generated
	 * @ordered
	 */
	protected static final double QUANTITY_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getQuantity() <em>Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQuantity()
	 * @generated
	 * @ordered
	 */
	protected double quantity = QUANTITY_EDEFAULT;

	/**
	 * The default value of the '{@link #getPricingMonth() <em>Pricing Month</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPricingMonth()
	 * @generated
	 * @ordered
	 */
	protected static final YearMonth PRICING_MONTH_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPricingMonth() <em>Pricing Month</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPricingMonth()
	 * @generated
	 * @ordered
	 */
	protected YearMonth pricingMonth = PRICING_MONTH_EDEFAULT;

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
	 * The default value of the '{@link #getYear() <em>Year</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getYear()
	 * @generated
	 * @ordered
	 */
	protected static final int YEAR_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getYear() <em>Year</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getYear()
	 * @generated
	 * @ordered
	 */
	protected int year = YEAR_EDEFAULT;

	/**
	 * The default value of the '{@link #getComment() <em>Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComment()
	 * @generated
	 * @ordered
	 */
	protected static final String COMMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getComment() <em>Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComment()
	 * @generated
	 * @ordered
	 */
	protected String comment = COMMENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getPricingPeriodStart() <em>Pricing Period Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPricingPeriodStart()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate PRICING_PERIOD_START_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPricingPeriodStart() <em>Pricing Period Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPricingPeriodStart()
	 * @generated
	 * @ordered
	 */
	protected LocalDate pricingPeriodStart = PRICING_PERIOD_START_EDEFAULT;

	/**
	 * The default value of the '{@link #getPricingPeriodEnd() <em>Pricing Period End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPricingPeriodEnd()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate PRICING_PERIOD_END_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPricingPeriodEnd() <em>Pricing Period End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPricingPeriodEnd()
	 * @generated
	 * @ordered
	 */
	protected LocalDate pricingPeriodEnd = PRICING_PERIOD_END_EDEFAULT;

	/**
	 * The default value of the '{@link #getHedgingPeriodStart() <em>Hedging Period Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHedgingPeriodStart()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate HEDGING_PERIOD_START_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getHedgingPeriodStart() <em>Hedging Period Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHedgingPeriodStart()
	 * @generated
	 * @ordered
	 */
	protected LocalDate hedgingPeriodStart = HEDGING_PERIOD_START_EDEFAULT;

	/**
	 * The default value of the '{@link #getHedgingPeriodEnd() <em>Hedging Period End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHedgingPeriodEnd()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate HEDGING_PERIOD_END_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getHedgingPeriodEnd() <em>Hedging Period End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHedgingPeriodEnd()
	 * @generated
	 * @ordered
	 */
	protected LocalDate hedgingPeriodEnd = HEDGING_PERIOD_END_EDEFAULT;

	/**
	 * The cached value of the '{@link #getPricingCalendar() <em>Pricing Calendar</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPricingCalendar()
	 * @generated
	 * @ordered
	 */
	protected PricingCalendar pricingCalendar;

	/**
	 * This is true if the Pricing Calendar reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean pricingCalendarESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PaperDealImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.PAPER_DEAL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getQuantity() {
		return quantity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setQuantity(double newQuantity) {
		double oldQuantity = quantity;
		quantity = newQuantity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.PAPER_DEAL__QUANTITY, oldQuantity, quantity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public YearMonth getPricingMonth() {
		return pricingMonth;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPricingMonth(YearMonth newPricingMonth) {
		YearMonth oldPricingMonth = pricingMonth;
		pricingMonth = newPricingMonth;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.PAPER_DEAL__PRICING_MONTH, oldPricingMonth, pricingMonth));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.PAPER_DEAL__ENTITY, oldEntity, entity));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.PAPER_DEAL__ENTITY, oldEntity, entity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getYear() {
		return year;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setYear(int newYear) {
		int oldYear = year;
		year = newYear;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.PAPER_DEAL__YEAR, oldYear, year));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getComment() {
		return comment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setComment(String newComment) {
		String oldComment = comment;
		comment = newComment;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.PAPER_DEAL__COMMENT, oldComment, comment));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDate getPricingPeriodStart() {
		return pricingPeriodStart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPricingPeriodStart(LocalDate newPricingPeriodStart) {
		LocalDate oldPricingPeriodStart = pricingPeriodStart;
		pricingPeriodStart = newPricingPeriodStart;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.PAPER_DEAL__PRICING_PERIOD_START, oldPricingPeriodStart, pricingPeriodStart));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDate getPricingPeriodEnd() {
		return pricingPeriodEnd;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPricingPeriodEnd(LocalDate newPricingPeriodEnd) {
		LocalDate oldPricingPeriodEnd = pricingPeriodEnd;
		pricingPeriodEnd = newPricingPeriodEnd;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.PAPER_DEAL__PRICING_PERIOD_END, oldPricingPeriodEnd, pricingPeriodEnd));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDate getHedgingPeriodStart() {
		return hedgingPeriodStart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setHedgingPeriodStart(LocalDate newHedgingPeriodStart) {
		LocalDate oldHedgingPeriodStart = hedgingPeriodStart;
		hedgingPeriodStart = newHedgingPeriodStart;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.PAPER_DEAL__HEDGING_PERIOD_START, oldHedgingPeriodStart, hedgingPeriodStart));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDate getHedgingPeriodEnd() {
		return hedgingPeriodEnd;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setHedgingPeriodEnd(LocalDate newHedgingPeriodEnd) {
		LocalDate oldHedgingPeriodEnd = hedgingPeriodEnd;
		hedgingPeriodEnd = newHedgingPeriodEnd;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.PAPER_DEAL__HEDGING_PERIOD_END, oldHedgingPeriodEnd, hedgingPeriodEnd));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PricingCalendar getPricingCalendar() {
		if (pricingCalendar != null && pricingCalendar.eIsProxy()) {
			InternalEObject oldPricingCalendar = (InternalEObject)pricingCalendar;
			pricingCalendar = (PricingCalendar)eResolveProxy(oldPricingCalendar);
			if (pricingCalendar != oldPricingCalendar) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.PAPER_DEAL__PRICING_CALENDAR, oldPricingCalendar, pricingCalendar));
			}
		}
		return pricingCalendar;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PricingCalendar basicGetPricingCalendar() {
		return pricingCalendar;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPricingCalendar(PricingCalendar newPricingCalendar) {
		PricingCalendar oldPricingCalendar = pricingCalendar;
		pricingCalendar = newPricingCalendar;
		boolean oldPricingCalendarESet = pricingCalendarESet;
		pricingCalendarESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.PAPER_DEAL__PRICING_CALENDAR, oldPricingCalendar, pricingCalendar, !oldPricingCalendarESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetPricingCalendar() {
		PricingCalendar oldPricingCalendar = pricingCalendar;
		boolean oldPricingCalendarESet = pricingCalendarESet;
		pricingCalendar = null;
		pricingCalendarESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.PAPER_DEAL__PRICING_CALENDAR, oldPricingCalendar, null, oldPricingCalendarESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetPricingCalendar() {
		return pricingCalendarESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SettleStrategy getInstrument() {
		if (instrument != null && instrument.eIsProxy()) {
			InternalEObject oldInstrument = (InternalEObject)instrument;
			instrument = (SettleStrategy)eResolveProxy(oldInstrument);
			if (instrument != oldInstrument) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.PAPER_DEAL__INSTRUMENT, oldInstrument, instrument));
			}
		}
		return instrument;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SettleStrategy basicGetInstrument() {
		return instrument;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setInstrument(SettleStrategy newInstrument) {
		SettleStrategy oldInstrument = instrument;
		instrument = newInstrument;
		boolean oldInstrumentESet = instrumentESet;
		instrumentESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.PAPER_DEAL__INSTRUMENT, oldInstrument, instrument, !oldInstrumentESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetInstrument() {
		SettleStrategy oldInstrument = instrument;
		boolean oldInstrumentESet = instrumentESet;
		instrument = null;
		instrumentESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.PAPER_DEAL__INSTRUMENT, oldInstrument, null, oldInstrumentESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetInstrument() {
		return instrumentESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getPrice() {
		return price;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPrice(double newPrice) {
		double oldPrice = price;
		price = newPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.PAPER_DEAL__PRICE, oldPrice, price));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PaperPricingType getPricingType() {
		return pricingType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPricingType(PaperPricingType newPricingType) {
		PaperPricingType oldPricingType = pricingType;
		pricingType = newPricingType == null ? PRICING_TYPE_EDEFAULT : newPricingType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.PAPER_DEAL__PRICING_TYPE, oldPricingType, pricingType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getIndex() {
		return index;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setIndex(String newIndex) {
		String oldIndex = index;
		index = newIndex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.PAPER_DEAL__INDEX, oldIndex, index));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoPackage.PAPER_DEAL__PRICE:
				return getPrice();
			case CargoPackage.PAPER_DEAL__PRICING_TYPE:
				return getPricingType();
			case CargoPackage.PAPER_DEAL__INDEX:
				return getIndex();
			case CargoPackage.PAPER_DEAL__INSTRUMENT:
				if (resolve) return getInstrument();
				return basicGetInstrument();
			case CargoPackage.PAPER_DEAL__QUANTITY:
				return getQuantity();
			case CargoPackage.PAPER_DEAL__PRICING_MONTH:
				return getPricingMonth();
			case CargoPackage.PAPER_DEAL__ENTITY:
				if (resolve) return getEntity();
				return basicGetEntity();
			case CargoPackage.PAPER_DEAL__YEAR:
				return getYear();
			case CargoPackage.PAPER_DEAL__COMMENT:
				return getComment();
			case CargoPackage.PAPER_DEAL__PRICING_PERIOD_START:
				return getPricingPeriodStart();
			case CargoPackage.PAPER_DEAL__PRICING_PERIOD_END:
				return getPricingPeriodEnd();
			case CargoPackage.PAPER_DEAL__HEDGING_PERIOD_START:
				return getHedgingPeriodStart();
			case CargoPackage.PAPER_DEAL__HEDGING_PERIOD_END:
				return getHedgingPeriodEnd();
			case CargoPackage.PAPER_DEAL__PRICING_CALENDAR:
				if (resolve) return getPricingCalendar();
				return basicGetPricingCalendar();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case CargoPackage.PAPER_DEAL__PRICE:
				setPrice((Double)newValue);
				return;
			case CargoPackage.PAPER_DEAL__PRICING_TYPE:
				setPricingType((PaperPricingType)newValue);
				return;
			case CargoPackage.PAPER_DEAL__INDEX:
				setIndex((String)newValue);
				return;
			case CargoPackage.PAPER_DEAL__INSTRUMENT:
				setInstrument((SettleStrategy)newValue);
				return;
			case CargoPackage.PAPER_DEAL__QUANTITY:
				setQuantity((Double)newValue);
				return;
			case CargoPackage.PAPER_DEAL__PRICING_MONTH:
				setPricingMonth((YearMonth)newValue);
				return;
			case CargoPackage.PAPER_DEAL__ENTITY:
				setEntity((BaseLegalEntity)newValue);
				return;
			case CargoPackage.PAPER_DEAL__YEAR:
				setYear((Integer)newValue);
				return;
			case CargoPackage.PAPER_DEAL__COMMENT:
				setComment((String)newValue);
				return;
			case CargoPackage.PAPER_DEAL__PRICING_PERIOD_START:
				setPricingPeriodStart((LocalDate)newValue);
				return;
			case CargoPackage.PAPER_DEAL__PRICING_PERIOD_END:
				setPricingPeriodEnd((LocalDate)newValue);
				return;
			case CargoPackage.PAPER_DEAL__HEDGING_PERIOD_START:
				setHedgingPeriodStart((LocalDate)newValue);
				return;
			case CargoPackage.PAPER_DEAL__HEDGING_PERIOD_END:
				setHedgingPeriodEnd((LocalDate)newValue);
				return;
			case CargoPackage.PAPER_DEAL__PRICING_CALENDAR:
				setPricingCalendar((PricingCalendar)newValue);
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
			case CargoPackage.PAPER_DEAL__PRICE:
				setPrice(PRICE_EDEFAULT);
				return;
			case CargoPackage.PAPER_DEAL__PRICING_TYPE:
				setPricingType(PRICING_TYPE_EDEFAULT);
				return;
			case CargoPackage.PAPER_DEAL__INDEX:
				setIndex(INDEX_EDEFAULT);
				return;
			case CargoPackage.PAPER_DEAL__INSTRUMENT:
				unsetInstrument();
				return;
			case CargoPackage.PAPER_DEAL__QUANTITY:
				setQuantity(QUANTITY_EDEFAULT);
				return;
			case CargoPackage.PAPER_DEAL__PRICING_MONTH:
				setPricingMonth(PRICING_MONTH_EDEFAULT);
				return;
			case CargoPackage.PAPER_DEAL__ENTITY:
				setEntity((BaseLegalEntity)null);
				return;
			case CargoPackage.PAPER_DEAL__YEAR:
				setYear(YEAR_EDEFAULT);
				return;
			case CargoPackage.PAPER_DEAL__COMMENT:
				setComment(COMMENT_EDEFAULT);
				return;
			case CargoPackage.PAPER_DEAL__PRICING_PERIOD_START:
				setPricingPeriodStart(PRICING_PERIOD_START_EDEFAULT);
				return;
			case CargoPackage.PAPER_DEAL__PRICING_PERIOD_END:
				setPricingPeriodEnd(PRICING_PERIOD_END_EDEFAULT);
				return;
			case CargoPackage.PAPER_DEAL__HEDGING_PERIOD_START:
				setHedgingPeriodStart(HEDGING_PERIOD_START_EDEFAULT);
				return;
			case CargoPackage.PAPER_DEAL__HEDGING_PERIOD_END:
				setHedgingPeriodEnd(HEDGING_PERIOD_END_EDEFAULT);
				return;
			case CargoPackage.PAPER_DEAL__PRICING_CALENDAR:
				unsetPricingCalendar();
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
			case CargoPackage.PAPER_DEAL__PRICE:
				return price != PRICE_EDEFAULT;
			case CargoPackage.PAPER_DEAL__PRICING_TYPE:
				return pricingType != PRICING_TYPE_EDEFAULT;
			case CargoPackage.PAPER_DEAL__INDEX:
				return INDEX_EDEFAULT == null ? index != null : !INDEX_EDEFAULT.equals(index);
			case CargoPackage.PAPER_DEAL__INSTRUMENT:
				return isSetInstrument();
			case CargoPackage.PAPER_DEAL__QUANTITY:
				return quantity != QUANTITY_EDEFAULT;
			case CargoPackage.PAPER_DEAL__PRICING_MONTH:
				return PRICING_MONTH_EDEFAULT == null ? pricingMonth != null : !PRICING_MONTH_EDEFAULT.equals(pricingMonth);
			case CargoPackage.PAPER_DEAL__ENTITY:
				return entity != null;
			case CargoPackage.PAPER_DEAL__YEAR:
				return year != YEAR_EDEFAULT;
			case CargoPackage.PAPER_DEAL__COMMENT:
				return COMMENT_EDEFAULT == null ? comment != null : !COMMENT_EDEFAULT.equals(comment);
			case CargoPackage.PAPER_DEAL__PRICING_PERIOD_START:
				return PRICING_PERIOD_START_EDEFAULT == null ? pricingPeriodStart != null : !PRICING_PERIOD_START_EDEFAULT.equals(pricingPeriodStart);
			case CargoPackage.PAPER_DEAL__PRICING_PERIOD_END:
				return PRICING_PERIOD_END_EDEFAULT == null ? pricingPeriodEnd != null : !PRICING_PERIOD_END_EDEFAULT.equals(pricingPeriodEnd);
			case CargoPackage.PAPER_DEAL__HEDGING_PERIOD_START:
				return HEDGING_PERIOD_START_EDEFAULT == null ? hedgingPeriodStart != null : !HEDGING_PERIOD_START_EDEFAULT.equals(hedgingPeriodStart);
			case CargoPackage.PAPER_DEAL__HEDGING_PERIOD_END:
				return HEDGING_PERIOD_END_EDEFAULT == null ? hedgingPeriodEnd != null : !HEDGING_PERIOD_END_EDEFAULT.equals(hedgingPeriodEnd);
			case CargoPackage.PAPER_DEAL__PRICING_CALENDAR:
				return isSetPricingCalendar();
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
		result.append(" (price: ");
		result.append(price);
		result.append(", pricingType: ");
		result.append(pricingType);
		result.append(", index: ");
		result.append(index);
		result.append(", quantity: ");
		result.append(quantity);
		result.append(", pricingMonth: ");
		result.append(pricingMonth);
		result.append(", year: ");
		result.append(year);
		result.append(", comment: ");
		result.append(comment);
		result.append(", pricingPeriodStart: ");
		result.append(pricingPeriodStart);
		result.append(", pricingPeriodEnd: ");
		result.append(pricingPeriodEnd);
		result.append(", hedgingPeriodStart: ");
		result.append(hedgingPeriodStart);
		result.append(", hedgingPeriodEnd: ");
		result.append(hedgingPeriodEnd);
		result.append(')');
		return result.toString();
	}

} //PaperDealImpl
