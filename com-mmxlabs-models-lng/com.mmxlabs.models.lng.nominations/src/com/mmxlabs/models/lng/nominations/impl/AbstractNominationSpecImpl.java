/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.nominations.impl;

import com.mmxlabs.models.lng.nominations.AbstractNominationSpec;
import com.mmxlabs.models.lng.nominations.DatePeriodPrior;
import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.lng.nominations.Side;

import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Nomination Spec</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.nominations.impl.AbstractNominationSpecImpl#getType <em>Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.impl.AbstractNominationSpecImpl#isCounterparty <em>Counterparty</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.impl.AbstractNominationSpecImpl#getRemark <em>Remark</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.impl.AbstractNominationSpecImpl#getSize <em>Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.impl.AbstractNominationSpecImpl#getSizeUnits <em>Size Units</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.impl.AbstractNominationSpecImpl#getDayOfMonth <em>Day Of Month</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.impl.AbstractNominationSpecImpl#getAlertSize <em>Alert Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.impl.AbstractNominationSpecImpl#getAlertSizeUnits <em>Alert Size Units</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.impl.AbstractNominationSpecImpl#getSide <em>Side</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.impl.AbstractNominationSpecImpl#getRefererId <em>Referer Id</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class AbstractNominationSpecImpl extends UUIDObjectImpl implements AbstractNominationSpec {
	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final String TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected String type = TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #isCounterparty() <em>Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isCounterparty()
	 * @generated
	 * @ordered
	 */
	protected static final boolean COUNTERPARTY_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isCounterparty() <em>Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isCounterparty()
	 * @generated
	 * @ordered
	 */
	protected boolean counterparty = COUNTERPARTY_EDEFAULT;

	/**
	 * The default value of the '{@link #getRemark() <em>Remark</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRemark()
	 * @generated
	 * @ordered
	 */
	protected static final String REMARK_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRemark() <em>Remark</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRemark()
	 * @generated
	 * @ordered
	 */
	protected String remark = REMARK_EDEFAULT;

	/**
	 * The default value of the '{@link #getSize() <em>Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSize()
	 * @generated
	 * @ordered
	 */
	protected static final int SIZE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSize() <em>Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSize()
	 * @generated
	 * @ordered
	 */
	protected int size = SIZE_EDEFAULT;

	/**
	 * This is true if the Size attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean sizeESet;

	/**
	 * The default value of the '{@link #getSizeUnits() <em>Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSizeUnits()
	 * @generated
	 * @ordered
	 */
	protected static final DatePeriodPrior SIZE_UNITS_EDEFAULT = DatePeriodPrior.DAYS_PRIOR;

	/**
	 * The cached value of the '{@link #getSizeUnits() <em>Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSizeUnits()
	 * @generated
	 * @ordered
	 */
	protected DatePeriodPrior sizeUnits = SIZE_UNITS_EDEFAULT;

	/**
	 * The default value of the '{@link #getDayOfMonth() <em>Day Of Month</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDayOfMonth()
	 * @generated
	 * @ordered
	 */
	protected static final int DAY_OF_MONTH_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDayOfMonth() <em>Day Of Month</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDayOfMonth()
	 * @generated
	 * @ordered
	 */
	protected int dayOfMonth = DAY_OF_MONTH_EDEFAULT;

	/**
	 * This is true if the Day Of Month attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean dayOfMonthESet;

	/**
	 * The default value of the '{@link #getAlertSize() <em>Alert Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAlertSize()
	 * @generated
	 * @ordered
	 */
	protected static final int ALERT_SIZE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getAlertSize() <em>Alert Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAlertSize()
	 * @generated
	 * @ordered
	 */
	protected int alertSize = ALERT_SIZE_EDEFAULT;

	/**
	 * This is true if the Alert Size attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean alertSizeESet;

	/**
	 * The default value of the '{@link #getAlertSizeUnits() <em>Alert Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAlertSizeUnits()
	 * @generated
	 * @ordered
	 */
	protected static final DatePeriodPrior ALERT_SIZE_UNITS_EDEFAULT = DatePeriodPrior.DAYS_PRIOR;

	/**
	 * The cached value of the '{@link #getAlertSizeUnits() <em>Alert Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAlertSizeUnits()
	 * @generated
	 * @ordered
	 */
	protected DatePeriodPrior alertSizeUnits = ALERT_SIZE_UNITS_EDEFAULT;

	/**
	 * The default value of the '{@link #getSide() <em>Side</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSide()
	 * @generated
	 * @ordered
	 */
	protected static final Side SIDE_EDEFAULT = Side.BUY;

	/**
	 * The cached value of the '{@link #getSide() <em>Side</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSide()
	 * @generated
	 * @ordered
	 */
	protected Side side = SIDE_EDEFAULT;

	/**
	 * The default value of the '{@link #getRefererId() <em>Referer Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRefererId()
	 * @generated
	 * @ordered
	 */
	protected static final String REFERER_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRefererId() <em>Referer Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRefererId()
	 * @generated
	 * @ordered
	 */
	protected String refererId = REFERER_ID_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AbstractNominationSpecImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return NominationsPackage.Literals.ABSTRACT_NOMINATION_SPEC;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setType(String newType) {
		String oldType = type;
		type = newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NominationsPackage.ABSTRACT_NOMINATION_SPEC__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isCounterparty() {
		return counterparty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCounterparty(boolean newCounterparty) {
		boolean oldCounterparty = counterparty;
		counterparty = newCounterparty;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NominationsPackage.ABSTRACT_NOMINATION_SPEC__COUNTERPARTY, oldCounterparty, counterparty));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getRemark() {
		return remark;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRemark(String newRemark) {
		String oldRemark = remark;
		remark = newRemark;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NominationsPackage.ABSTRACT_NOMINATION_SPEC__REMARK, oldRemark, remark));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getSize() {
		return size;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSize(int newSize) {
		int oldSize = size;
		size = newSize;
		boolean oldSizeESet = sizeESet;
		sizeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NominationsPackage.ABSTRACT_NOMINATION_SPEC__SIZE, oldSize, size, !oldSizeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetSize() {
		int oldSize = size;
		boolean oldSizeESet = sizeESet;
		size = SIZE_EDEFAULT;
		sizeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, NominationsPackage.ABSTRACT_NOMINATION_SPEC__SIZE, oldSize, SIZE_EDEFAULT, oldSizeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetSize() {
		return sizeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DatePeriodPrior getSizeUnits() {
		return sizeUnits;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSizeUnits(DatePeriodPrior newSizeUnits) {
		DatePeriodPrior oldSizeUnits = sizeUnits;
		sizeUnits = newSizeUnits == null ? SIZE_UNITS_EDEFAULT : newSizeUnits;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NominationsPackage.ABSTRACT_NOMINATION_SPEC__SIZE_UNITS, oldSizeUnits, sizeUnits));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getDayOfMonth() {
		return dayOfMonth;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDayOfMonth(int newDayOfMonth) {
		int oldDayOfMonth = dayOfMonth;
		dayOfMonth = newDayOfMonth;
		boolean oldDayOfMonthESet = dayOfMonthESet;
		dayOfMonthESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NominationsPackage.ABSTRACT_NOMINATION_SPEC__DAY_OF_MONTH, oldDayOfMonth, dayOfMonth, !oldDayOfMonthESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetDayOfMonth() {
		int oldDayOfMonth = dayOfMonth;
		boolean oldDayOfMonthESet = dayOfMonthESet;
		dayOfMonth = DAY_OF_MONTH_EDEFAULT;
		dayOfMonthESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, NominationsPackage.ABSTRACT_NOMINATION_SPEC__DAY_OF_MONTH, oldDayOfMonth, DAY_OF_MONTH_EDEFAULT, oldDayOfMonthESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetDayOfMonth() {
		return dayOfMonthESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getAlertSize() {
		return alertSize;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAlertSize(int newAlertSize) {
		int oldAlertSize = alertSize;
		alertSize = newAlertSize;
		boolean oldAlertSizeESet = alertSizeESet;
		alertSizeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NominationsPackage.ABSTRACT_NOMINATION_SPEC__ALERT_SIZE, oldAlertSize, alertSize, !oldAlertSizeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetAlertSize() {
		int oldAlertSize = alertSize;
		boolean oldAlertSizeESet = alertSizeESet;
		alertSize = ALERT_SIZE_EDEFAULT;
		alertSizeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, NominationsPackage.ABSTRACT_NOMINATION_SPEC__ALERT_SIZE, oldAlertSize, ALERT_SIZE_EDEFAULT, oldAlertSizeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetAlertSize() {
		return alertSizeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DatePeriodPrior getAlertSizeUnits() {
		return alertSizeUnits;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAlertSizeUnits(DatePeriodPrior newAlertSizeUnits) {
		DatePeriodPrior oldAlertSizeUnits = alertSizeUnits;
		alertSizeUnits = newAlertSizeUnits == null ? ALERT_SIZE_UNITS_EDEFAULT : newAlertSizeUnits;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NominationsPackage.ABSTRACT_NOMINATION_SPEC__ALERT_SIZE_UNITS, oldAlertSizeUnits, alertSizeUnits));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Side getSide() {
		return side;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSide(Side newSide) {
		Side oldSide = side;
		side = newSide == null ? SIDE_EDEFAULT : newSide;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NominationsPackage.ABSTRACT_NOMINATION_SPEC__SIDE, oldSide, side));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getRefererId() {
		return refererId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRefererId(String newRefererId) {
		String oldRefererId = refererId;
		refererId = newRefererId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NominationsPackage.ABSTRACT_NOMINATION_SPEC__REFERER_ID, oldRefererId, refererId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__TYPE:
				return getType();
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__COUNTERPARTY:
				return isCounterparty();
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__REMARK:
				return getRemark();
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__SIZE:
				return getSize();
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__SIZE_UNITS:
				return getSizeUnits();
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__DAY_OF_MONTH:
				return getDayOfMonth();
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__ALERT_SIZE:
				return getAlertSize();
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__ALERT_SIZE_UNITS:
				return getAlertSizeUnits();
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__SIDE:
				return getSide();
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__REFERER_ID:
				return getRefererId();
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
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__TYPE:
				setType((String)newValue);
				return;
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__COUNTERPARTY:
				setCounterparty((Boolean)newValue);
				return;
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__REMARK:
				setRemark((String)newValue);
				return;
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__SIZE:
				setSize((Integer)newValue);
				return;
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__SIZE_UNITS:
				setSizeUnits((DatePeriodPrior)newValue);
				return;
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__DAY_OF_MONTH:
				setDayOfMonth((Integer)newValue);
				return;
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__ALERT_SIZE:
				setAlertSize((Integer)newValue);
				return;
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__ALERT_SIZE_UNITS:
				setAlertSizeUnits((DatePeriodPrior)newValue);
				return;
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__SIDE:
				setSide((Side)newValue);
				return;
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__REFERER_ID:
				setRefererId((String)newValue);
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
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__TYPE:
				setType(TYPE_EDEFAULT);
				return;
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__COUNTERPARTY:
				setCounterparty(COUNTERPARTY_EDEFAULT);
				return;
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__REMARK:
				setRemark(REMARK_EDEFAULT);
				return;
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__SIZE:
				unsetSize();
				return;
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__SIZE_UNITS:
				setSizeUnits(SIZE_UNITS_EDEFAULT);
				return;
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__DAY_OF_MONTH:
				unsetDayOfMonth();
				return;
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__ALERT_SIZE:
				unsetAlertSize();
				return;
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__ALERT_SIZE_UNITS:
				setAlertSizeUnits(ALERT_SIZE_UNITS_EDEFAULT);
				return;
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__SIDE:
				setSide(SIDE_EDEFAULT);
				return;
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__REFERER_ID:
				setRefererId(REFERER_ID_EDEFAULT);
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
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__TYPE:
				return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__COUNTERPARTY:
				return counterparty != COUNTERPARTY_EDEFAULT;
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__REMARK:
				return REMARK_EDEFAULT == null ? remark != null : !REMARK_EDEFAULT.equals(remark);
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__SIZE:
				return isSetSize();
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__SIZE_UNITS:
				return sizeUnits != SIZE_UNITS_EDEFAULT;
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__DAY_OF_MONTH:
				return isSetDayOfMonth();
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__ALERT_SIZE:
				return isSetAlertSize();
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__ALERT_SIZE_UNITS:
				return alertSizeUnits != ALERT_SIZE_UNITS_EDEFAULT;
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__SIDE:
				return side != SIDE_EDEFAULT;
			case NominationsPackage.ABSTRACT_NOMINATION_SPEC__REFERER_ID:
				return REFERER_ID_EDEFAULT == null ? refererId != null : !REFERER_ID_EDEFAULT.equals(refererId);
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
		result.append(" (type: ");
		result.append(type);
		result.append(", counterparty: ");
		result.append(counterparty);
		result.append(", remark: ");
		result.append(remark);
		result.append(", size: ");
		if (sizeESet) result.append(size); else result.append("<unset>");
		result.append(", sizeUnits: ");
		result.append(sizeUnits);
		result.append(", dayOfMonth: ");
		if (dayOfMonthESet) result.append(dayOfMonth); else result.append("<unset>");
		result.append(", alertSize: ");
		if (alertSizeESet) result.append(alertSize); else result.append("<unset>");
		result.append(", alertSizeUnits: ");
		result.append(alertSizeUnits);
		result.append(", side: ");
		result.append(side);
		result.append(", refererId: ");
		result.append(refererId);
		result.append(')');
		return result.toString();
	}

} //AbstractNominationSpecImpl
