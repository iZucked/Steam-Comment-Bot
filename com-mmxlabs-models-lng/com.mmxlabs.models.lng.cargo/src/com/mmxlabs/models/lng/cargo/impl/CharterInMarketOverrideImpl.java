/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.commercial.EndHeelOptions;
import com.mmxlabs.models.lng.commercial.StartHeelOptions;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Charter In Market Override</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CharterInMarketOverrideImpl#getCharterInMarket <em>Charter In Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CharterInMarketOverrideImpl#getSpotIndex <em>Spot Index</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CharterInMarketOverrideImpl#getStartHeel <em>Start Heel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CharterInMarketOverrideImpl#getStartDate <em>Start Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CharterInMarketOverrideImpl#getEndPort <em>End Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CharterInMarketOverrideImpl#getEndDate <em>End Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CharterInMarketOverrideImpl#getEndHeel <em>End Heel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CharterInMarketOverrideImpl#isIncludeBallastBonus <em>Include Ballast Bonus</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CharterInMarketOverrideImpl#getMinDuration <em>Min Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CharterInMarketOverrideImpl#getMaxDuration <em>Max Duration</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CharterInMarketOverrideImpl extends MMXObjectImpl implements CharterInMarketOverride {
	/**
	 * The cached value of the '{@link #getCharterInMarket() <em>Charter In Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterInMarket()
	 * @generated
	 * @ordered
	 */
	protected CharterInMarket charterInMarket;

	/**
	 * The default value of the '{@link #getSpotIndex() <em>Spot Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpotIndex()
	 * @generated
	 * @ordered
	 */
	protected static final int SPOT_INDEX_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSpotIndex() <em>Spot Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpotIndex()
	 * @generated
	 * @ordered
	 */
	protected int spotIndex = SPOT_INDEX_EDEFAULT;

	/**
	 * The cached value of the '{@link #getStartHeel() <em>Start Heel</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartHeel()
	 * @generated
	 * @ordered
	 */
	protected StartHeelOptions startHeel;

	/**
	 * This is true if the Start Heel containment reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean startHeelESet;

	/**
	 * The default value of the '{@link #getStartDate() <em>Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartDate()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDateTime START_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStartDate() <em>Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartDate()
	 * @generated
	 * @ordered
	 */
	protected LocalDateTime startDate = START_DATE_EDEFAULT;

	/**
	 * This is true if the Start Date attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean startDateESet;

	/**
	 * The cached value of the '{@link #getEndPort() <em>End Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndPort()
	 * @generated
	 * @ordered
	 */
	protected Port endPort;

	/**
	 * This is true if the End Port reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean endPortESet;

	/**
	 * The default value of the '{@link #getEndDate() <em>End Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndDate()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDateTime END_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEndDate() <em>End Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndDate()
	 * @generated
	 * @ordered
	 */
	protected LocalDateTime endDate = END_DATE_EDEFAULT;

	/**
	 * This is true if the End Date attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean endDateESet;

	/**
	 * The cached value of the '{@link #getEndHeel() <em>End Heel</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndHeel()
	 * @generated
	 * @ordered
	 */
	protected EndHeelOptions endHeel;

	/**
	 * This is true if the End Heel containment reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean endHeelESet;

	/**
	 * The default value of the '{@link #isIncludeBallastBonus() <em>Include Ballast Bonus</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIncludeBallastBonus()
	 * @generated
	 * @ordered
	 */
	protected static final boolean INCLUDE_BALLAST_BONUS_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIncludeBallastBonus() <em>Include Ballast Bonus</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIncludeBallastBonus()
	 * @generated
	 * @ordered
	 */
	protected boolean includeBallastBonus = INCLUDE_BALLAST_BONUS_EDEFAULT;

	/**
	 * The default value of the '{@link #getMinDuration() <em>Min Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinDuration()
	 * @generated
	 * @ordered
	 */
	protected static final int MIN_DURATION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMinDuration() <em>Min Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinDuration()
	 * @generated
	 * @ordered
	 */
	protected int minDuration = MIN_DURATION_EDEFAULT;

	/**
	 * This is true if the Min Duration attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean minDurationESet;

	/**
	 * The default value of the '{@link #getMaxDuration() <em>Max Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxDuration()
	 * @generated
	 * @ordered
	 */
	protected static final int MAX_DURATION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMaxDuration() <em>Max Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxDuration()
	 * @generated
	 * @ordered
	 */
	protected int maxDuration = MAX_DURATION_EDEFAULT;

	/**
	 * This is true if the Max Duration attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean maxDurationESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CharterInMarketOverrideImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.CHARTER_IN_MARKET_OVERRIDE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CharterInMarket getCharterInMarket() {
		if (charterInMarket != null && charterInMarket.eIsProxy()) {
			InternalEObject oldCharterInMarket = (InternalEObject)charterInMarket;
			charterInMarket = (CharterInMarket)eResolveProxy(oldCharterInMarket);
			if (charterInMarket != oldCharterInMarket) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.CHARTER_IN_MARKET_OVERRIDE__CHARTER_IN_MARKET, oldCharterInMarket, charterInMarket));
			}
		}
		return charterInMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CharterInMarket basicGetCharterInMarket() {
		return charterInMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCharterInMarket(CharterInMarket newCharterInMarket) {
		CharterInMarket oldCharterInMarket = charterInMarket;
		charterInMarket = newCharterInMarket;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CHARTER_IN_MARKET_OVERRIDE__CHARTER_IN_MARKET, oldCharterInMarket, charterInMarket));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getSpotIndex() {
		return spotIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSpotIndex(int newSpotIndex) {
		int oldSpotIndex = spotIndex;
		spotIndex = newSpotIndex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CHARTER_IN_MARKET_OVERRIDE__SPOT_INDEX, oldSpotIndex, spotIndex));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public StartHeelOptions getStartHeel() {
		if (startHeel != null && startHeel.eIsProxy()) {
			InternalEObject oldStartHeel = (InternalEObject)startHeel;
			startHeel = (StartHeelOptions)eResolveProxy(oldStartHeel);
			if (startHeel != oldStartHeel) {
				InternalEObject newStartHeel = (InternalEObject)startHeel;
				NotificationChain msgs = oldStartHeel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CargoPackage.CHARTER_IN_MARKET_OVERRIDE__START_HEEL, null, null);
				if (newStartHeel.eInternalContainer() == null) {
					msgs = newStartHeel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CargoPackage.CHARTER_IN_MARKET_OVERRIDE__START_HEEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.CHARTER_IN_MARKET_OVERRIDE__START_HEEL, oldStartHeel, startHeel));
			}
		}
		return startHeel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StartHeelOptions basicGetStartHeel() {
		return startHeel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetStartHeel(StartHeelOptions newStartHeel, NotificationChain msgs) {
		StartHeelOptions oldStartHeel = startHeel;
		startHeel = newStartHeel;
		boolean oldStartHeelESet = startHeelESet;
		startHeelESet = true;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CargoPackage.CHARTER_IN_MARKET_OVERRIDE__START_HEEL, oldStartHeel, newStartHeel, !oldStartHeelESet);
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
	public void setStartHeel(StartHeelOptions newStartHeel) {
		if (newStartHeel != startHeel) {
			NotificationChain msgs = null;
			if (startHeel != null)
				msgs = ((InternalEObject)startHeel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CargoPackage.CHARTER_IN_MARKET_OVERRIDE__START_HEEL, null, msgs);
			if (newStartHeel != null)
				msgs = ((InternalEObject)newStartHeel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CargoPackage.CHARTER_IN_MARKET_OVERRIDE__START_HEEL, null, msgs);
			msgs = basicSetStartHeel(newStartHeel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else {
			boolean oldStartHeelESet = startHeelESet;
			startHeelESet = true;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CHARTER_IN_MARKET_OVERRIDE__START_HEEL, newStartHeel, newStartHeel, !oldStartHeelESet));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicUnsetStartHeel(NotificationChain msgs) {
		StartHeelOptions oldStartHeel = startHeel;
		startHeel = null;
		boolean oldStartHeelESet = startHeelESet;
		startHeelESet = false;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.UNSET, CargoPackage.CHARTER_IN_MARKET_OVERRIDE__START_HEEL, oldStartHeel, null, oldStartHeelESet);
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
	public void unsetStartHeel() {
		if (startHeel != null) {
			NotificationChain msgs = null;
			msgs = ((InternalEObject)startHeel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CargoPackage.CHARTER_IN_MARKET_OVERRIDE__START_HEEL, null, msgs);
			msgs = basicUnsetStartHeel(msgs);
			if (msgs != null) msgs.dispatch();
		}
		else {
			boolean oldStartHeelESet = startHeelESet;
			startHeelESet = false;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.CHARTER_IN_MARKET_OVERRIDE__START_HEEL, null, null, oldStartHeelESet));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetStartHeel() {
		return startHeelESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDateTime getStartDate() {
		return startDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setStartDate(LocalDateTime newStartDate) {
		LocalDateTime oldStartDate = startDate;
		startDate = newStartDate;
		boolean oldStartDateESet = startDateESet;
		startDateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CHARTER_IN_MARKET_OVERRIDE__START_DATE, oldStartDate, startDate, !oldStartDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetStartDate() {
		LocalDateTime oldStartDate = startDate;
		boolean oldStartDateESet = startDateESet;
		startDate = START_DATE_EDEFAULT;
		startDateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.CHARTER_IN_MARKET_OVERRIDE__START_DATE, oldStartDate, START_DATE_EDEFAULT, oldStartDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetStartDate() {
		return startDateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Port getEndPort() {
		if (endPort != null && endPort.eIsProxy()) {
			InternalEObject oldEndPort = (InternalEObject)endPort;
			endPort = (Port)eResolveProxy(oldEndPort);
			if (endPort != oldEndPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.CHARTER_IN_MARKET_OVERRIDE__END_PORT, oldEndPort, endPort));
			}
		}
		return endPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetEndPort() {
		return endPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEndPort(Port newEndPort) {
		Port oldEndPort = endPort;
		endPort = newEndPort;
		boolean oldEndPortESet = endPortESet;
		endPortESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CHARTER_IN_MARKET_OVERRIDE__END_PORT, oldEndPort, endPort, !oldEndPortESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetEndPort() {
		Port oldEndPort = endPort;
		boolean oldEndPortESet = endPortESet;
		endPort = null;
		endPortESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.CHARTER_IN_MARKET_OVERRIDE__END_PORT, oldEndPort, null, oldEndPortESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetEndPort() {
		return endPortESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDateTime getEndDate() {
		return endDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEndDate(LocalDateTime newEndDate) {
		LocalDateTime oldEndDate = endDate;
		endDate = newEndDate;
		boolean oldEndDateESet = endDateESet;
		endDateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CHARTER_IN_MARKET_OVERRIDE__END_DATE, oldEndDate, endDate, !oldEndDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetEndDate() {
		LocalDateTime oldEndDate = endDate;
		boolean oldEndDateESet = endDateESet;
		endDate = END_DATE_EDEFAULT;
		endDateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.CHARTER_IN_MARKET_OVERRIDE__END_DATE, oldEndDate, END_DATE_EDEFAULT, oldEndDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetEndDate() {
		return endDateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EndHeelOptions getEndHeel() {
		if (endHeel != null && endHeel.eIsProxy()) {
			InternalEObject oldEndHeel = (InternalEObject)endHeel;
			endHeel = (EndHeelOptions)eResolveProxy(oldEndHeel);
			if (endHeel != oldEndHeel) {
				InternalEObject newEndHeel = (InternalEObject)endHeel;
				NotificationChain msgs = oldEndHeel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CargoPackage.CHARTER_IN_MARKET_OVERRIDE__END_HEEL, null, null);
				if (newEndHeel.eInternalContainer() == null) {
					msgs = newEndHeel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CargoPackage.CHARTER_IN_MARKET_OVERRIDE__END_HEEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.CHARTER_IN_MARKET_OVERRIDE__END_HEEL, oldEndHeel, endHeel));
			}
		}
		return endHeel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EndHeelOptions basicGetEndHeel() {
		return endHeel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetEndHeel(EndHeelOptions newEndHeel, NotificationChain msgs) {
		EndHeelOptions oldEndHeel = endHeel;
		endHeel = newEndHeel;
		boolean oldEndHeelESet = endHeelESet;
		endHeelESet = true;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CargoPackage.CHARTER_IN_MARKET_OVERRIDE__END_HEEL, oldEndHeel, newEndHeel, !oldEndHeelESet);
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
	public void setEndHeel(EndHeelOptions newEndHeel) {
		if (newEndHeel != endHeel) {
			NotificationChain msgs = null;
			if (endHeel != null)
				msgs = ((InternalEObject)endHeel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CargoPackage.CHARTER_IN_MARKET_OVERRIDE__END_HEEL, null, msgs);
			if (newEndHeel != null)
				msgs = ((InternalEObject)newEndHeel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CargoPackage.CHARTER_IN_MARKET_OVERRIDE__END_HEEL, null, msgs);
			msgs = basicSetEndHeel(newEndHeel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else {
			boolean oldEndHeelESet = endHeelESet;
			endHeelESet = true;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CHARTER_IN_MARKET_OVERRIDE__END_HEEL, newEndHeel, newEndHeel, !oldEndHeelESet));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicUnsetEndHeel(NotificationChain msgs) {
		EndHeelOptions oldEndHeel = endHeel;
		endHeel = null;
		boolean oldEndHeelESet = endHeelESet;
		endHeelESet = false;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.UNSET, CargoPackage.CHARTER_IN_MARKET_OVERRIDE__END_HEEL, oldEndHeel, null, oldEndHeelESet);
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
	public void unsetEndHeel() {
		if (endHeel != null) {
			NotificationChain msgs = null;
			msgs = ((InternalEObject)endHeel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CargoPackage.CHARTER_IN_MARKET_OVERRIDE__END_HEEL, null, msgs);
			msgs = basicUnsetEndHeel(msgs);
			if (msgs != null) msgs.dispatch();
		}
		else {
			boolean oldEndHeelESet = endHeelESet;
			endHeelESet = false;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.CHARTER_IN_MARKET_OVERRIDE__END_HEEL, null, null, oldEndHeelESet));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetEndHeel() {
		return endHeelESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isIncludeBallastBonus() {
		return includeBallastBonus;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setIncludeBallastBonus(boolean newIncludeBallastBonus) {
		boolean oldIncludeBallastBonus = includeBallastBonus;
		includeBallastBonus = newIncludeBallastBonus;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CHARTER_IN_MARKET_OVERRIDE__INCLUDE_BALLAST_BONUS, oldIncludeBallastBonus, includeBallastBonus));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getMinDuration() {
		return minDuration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMinDuration(int newMinDuration) {
		int oldMinDuration = minDuration;
		minDuration = newMinDuration;
		boolean oldMinDurationESet = minDurationESet;
		minDurationESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CHARTER_IN_MARKET_OVERRIDE__MIN_DURATION, oldMinDuration, minDuration, !oldMinDurationESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetMinDuration() {
		int oldMinDuration = minDuration;
		boolean oldMinDurationESet = minDurationESet;
		minDuration = MIN_DURATION_EDEFAULT;
		minDurationESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.CHARTER_IN_MARKET_OVERRIDE__MIN_DURATION, oldMinDuration, MIN_DURATION_EDEFAULT, oldMinDurationESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetMinDuration() {
		return minDurationESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getMaxDuration() {
		return maxDuration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMaxDuration(int newMaxDuration) {
		int oldMaxDuration = maxDuration;
		maxDuration = newMaxDuration;
		boolean oldMaxDurationESet = maxDurationESet;
		maxDurationESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CHARTER_IN_MARKET_OVERRIDE__MAX_DURATION, oldMaxDuration, maxDuration, !oldMaxDurationESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetMaxDuration() {
		int oldMaxDuration = maxDuration;
		boolean oldMaxDurationESet = maxDurationESet;
		maxDuration = MAX_DURATION_EDEFAULT;
		maxDurationESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.CHARTER_IN_MARKET_OVERRIDE__MAX_DURATION, oldMaxDuration, MAX_DURATION_EDEFAULT, oldMaxDurationESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetMaxDuration() {
		return maxDurationESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public ZonedDateTime getStartDateAsDateTime() {
		if (isSetStartDate()) {
			final LocalDateTime ldt = getStartDate();
			if (ldt != null) {
				return ldt.atZone(ZoneId.of("UTC"));
			}
		}
		return null;
	}
 

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public ZonedDateTime getEndDateAsDateTime() {
		if (isSetEndDate()) {
			final LocalDateTime ldt = getEndDate();
			if (ldt != null) {
				return ldt.atZone(ZoneId.of("UTC"));
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public int getLocalOrDelegateMinDuration() {
		return (Integer) eGetWithDefault(CargoPackage.Literals.CHARTER_IN_MARKET_OVERRIDE__MIN_DURATION);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public int getLocalOrDelegateMaxDuration() {
		return (Integer) eGetWithDefault(CargoPackage.Literals.CHARTER_IN_MARKET_OVERRIDE__MIN_DURATION);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__START_HEEL:
				return basicUnsetStartHeel(msgs);
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__END_HEEL:
				return basicUnsetEndHeel(msgs);
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
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__CHARTER_IN_MARKET:
				if (resolve) return getCharterInMarket();
				return basicGetCharterInMarket();
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__SPOT_INDEX:
				return getSpotIndex();
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__START_HEEL:
				if (resolve) return getStartHeel();
				return basicGetStartHeel();
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__START_DATE:
				return getStartDate();
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__END_PORT:
				if (resolve) return getEndPort();
				return basicGetEndPort();
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__END_DATE:
				return getEndDate();
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__END_HEEL:
				if (resolve) return getEndHeel();
				return basicGetEndHeel();
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__INCLUDE_BALLAST_BONUS:
				return isIncludeBallastBonus();
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__MIN_DURATION:
				return getMinDuration();
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__MAX_DURATION:
				return getMaxDuration();
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
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__CHARTER_IN_MARKET:
				setCharterInMarket((CharterInMarket)newValue);
				return;
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__SPOT_INDEX:
				setSpotIndex((Integer)newValue);
				return;
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__START_HEEL:
				setStartHeel((StartHeelOptions)newValue);
				return;
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__START_DATE:
				setStartDate((LocalDateTime)newValue);
				return;
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__END_PORT:
				setEndPort((Port)newValue);
				return;
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__END_DATE:
				setEndDate((LocalDateTime)newValue);
				return;
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__END_HEEL:
				setEndHeel((EndHeelOptions)newValue);
				return;
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__INCLUDE_BALLAST_BONUS:
				setIncludeBallastBonus((Boolean)newValue);
				return;
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__MIN_DURATION:
				setMinDuration((Integer)newValue);
				return;
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__MAX_DURATION:
				setMaxDuration((Integer)newValue);
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
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__CHARTER_IN_MARKET:
				setCharterInMarket((CharterInMarket)null);
				return;
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__SPOT_INDEX:
				setSpotIndex(SPOT_INDEX_EDEFAULT);
				return;
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__START_HEEL:
				unsetStartHeel();
				return;
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__START_DATE:
				unsetStartDate();
				return;
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__END_PORT:
				unsetEndPort();
				return;
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__END_DATE:
				unsetEndDate();
				return;
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__END_HEEL:
				unsetEndHeel();
				return;
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__INCLUDE_BALLAST_BONUS:
				setIncludeBallastBonus(INCLUDE_BALLAST_BONUS_EDEFAULT);
				return;
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__MIN_DURATION:
				unsetMinDuration();
				return;
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__MAX_DURATION:
				unsetMaxDuration();
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
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__CHARTER_IN_MARKET:
				return charterInMarket != null;
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__SPOT_INDEX:
				return spotIndex != SPOT_INDEX_EDEFAULT;
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__START_HEEL:
				return isSetStartHeel();
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__START_DATE:
				return isSetStartDate();
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__END_PORT:
				return isSetEndPort();
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__END_DATE:
				return isSetEndDate();
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__END_HEEL:
				return isSetEndHeel();
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__INCLUDE_BALLAST_BONUS:
				return includeBallastBonus != INCLUDE_BALLAST_BONUS_EDEFAULT;
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__MIN_DURATION:
				return isSetMinDuration();
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__MAX_DURATION:
				return isSetMaxDuration();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE___GET_START_DATE_AS_DATE_TIME:
				return getStartDateAsDateTime();
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE___GET_END_DATE_AS_DATE_TIME:
				return getEndDateAsDateTime();
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE___GET_LOCAL_OR_DELEGATE_MIN_DURATION:
				return getLocalOrDelegateMinDuration();
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE___GET_LOCAL_OR_DELEGATE_MAX_DURATION:
				return getLocalOrDelegateMaxDuration();
		}
		return super.eInvoke(operationID, arguments);
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
		result.append(" (spotIndex: ");
		result.append(spotIndex);
		result.append(", startDate: ");
		if (startDateESet) result.append(startDate); else result.append("<unset>");
		result.append(", endDate: ");
		if (endDateESet) result.append(endDate); else result.append("<unset>");
		result.append(", includeBallastBonus: ");
		result.append(includeBallastBonus);
		result.append(", minDuration: ");
		if (minDurationESet) result.append(minDuration); else result.append("<unset>");
		result.append(", maxDuration: ");
		if (maxDurationESet) result.append(maxDuration); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}
	
	@Override
	public DelegateInformation getUnsetValueOrDelegate(EStructuralFeature feature) {
		CargoPackage cargo = CargoPackage.eINSTANCE;
		SpotMarketsPackage commercial = SpotMarketsPackage.eINSTANCE;
		if (cargo.getCharterInMarketOverride_MinDuration() == feature) {
			return new DelegateInformation(cargo.getCharterInMarketOverride_CharterInMarket(), commercial.getCharterInMarket_MinDuration(), (Integer) 0);
		} else if (cargo.getCharterInMarketOverride_MaxDuration() == feature) {
			return new DelegateInformation(cargo.getCharterInMarketOverride_CharterInMarket(), commercial.getCharterInMarket_MaxDuration(), (Integer) 0);
		}
		
		return new DelegateInformation(null, null, null);
	}	
	

} //CharterInMarketOverrideImpl
