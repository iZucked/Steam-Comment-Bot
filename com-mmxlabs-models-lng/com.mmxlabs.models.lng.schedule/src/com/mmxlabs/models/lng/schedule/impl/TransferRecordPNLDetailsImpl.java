/**
 */
package com.mmxlabs.models.lng.schedule.impl;

import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.TransferRecordPNLDetails;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Transfer Record PNL Details</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.TransferRecordPNLDetailsImpl#getTransferPrice <em>Transfer Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.TransferRecordPNLDetailsImpl#getFromEntityName <em>From Entity Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.TransferRecordPNLDetailsImpl#getFromEntityRevenue <em>From Entity Revenue</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.TransferRecordPNLDetailsImpl#getFromEntityCost <em>From Entity Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.TransferRecordPNLDetailsImpl#getToEntityName <em>To Entity Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.TransferRecordPNLDetailsImpl#getToEntityRevenue <em>To Entity Revenue</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.TransferRecordPNLDetailsImpl#getToEntityCost <em>To Entity Cost</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TransferRecordPNLDetailsImpl extends GeneralPNLDetailsImpl implements TransferRecordPNLDetails {
	/**
	 * The default value of the '{@link #getTransferPrice() <em>Transfer Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransferPrice()
	 * @generated
	 * @ordered
	 */
	protected static final double TRANSFER_PRICE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getTransferPrice() <em>Transfer Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransferPrice()
	 * @generated
	 * @ordered
	 */
	protected double transferPrice = TRANSFER_PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getFromEntityName() <em>From Entity Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFromEntityName()
	 * @generated
	 * @ordered
	 */
	protected static final String FROM_ENTITY_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFromEntityName() <em>From Entity Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFromEntityName()
	 * @generated
	 * @ordered
	 */
	protected String fromEntityName = FROM_ENTITY_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getFromEntityRevenue() <em>From Entity Revenue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFromEntityRevenue()
	 * @generated
	 * @ordered
	 */
	protected static final int FROM_ENTITY_REVENUE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getFromEntityRevenue() <em>From Entity Revenue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFromEntityRevenue()
	 * @generated
	 * @ordered
	 */
	protected int fromEntityRevenue = FROM_ENTITY_REVENUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getFromEntityCost() <em>From Entity Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFromEntityCost()
	 * @generated
	 * @ordered
	 */
	protected static final int FROM_ENTITY_COST_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getFromEntityCost() <em>From Entity Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFromEntityCost()
	 * @generated
	 * @ordered
	 */
	protected int fromEntityCost = FROM_ENTITY_COST_EDEFAULT;

	/**
	 * The default value of the '{@link #getToEntityName() <em>To Entity Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getToEntityName()
	 * @generated
	 * @ordered
	 */
	protected static final String TO_ENTITY_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getToEntityName() <em>To Entity Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getToEntityName()
	 * @generated
	 * @ordered
	 */
	protected String toEntityName = TO_ENTITY_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getToEntityRevenue() <em>To Entity Revenue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getToEntityRevenue()
	 * @generated
	 * @ordered
	 */
	protected static final int TO_ENTITY_REVENUE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getToEntityRevenue() <em>To Entity Revenue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getToEntityRevenue()
	 * @generated
	 * @ordered
	 */
	protected int toEntityRevenue = TO_ENTITY_REVENUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getToEntityCost() <em>To Entity Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getToEntityCost()
	 * @generated
	 * @ordered
	 */
	protected static final int TO_ENTITY_COST_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getToEntityCost() <em>To Entity Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getToEntityCost()
	 * @generated
	 * @ordered
	 */
	protected int toEntityCost = TO_ENTITY_COST_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TransferRecordPNLDetailsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.TRANSFER_RECORD_PNL_DETAILS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getTransferPrice() {
		return transferPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTransferPrice(double newTransferPrice) {
		double oldTransferPrice = transferPrice;
		transferPrice = newTransferPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TRANSFER_PRICE, oldTransferPrice, transferPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getFromEntityName() {
		return fromEntityName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFromEntityName(String newFromEntityName) {
		String oldFromEntityName = fromEntityName;
		fromEntityName = newFromEntityName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY_NAME, oldFromEntityName, fromEntityName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getFromEntityRevenue() {
		return fromEntityRevenue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFromEntityRevenue(int newFromEntityRevenue) {
		int oldFromEntityRevenue = fromEntityRevenue;
		fromEntityRevenue = newFromEntityRevenue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY_REVENUE, oldFromEntityRevenue, fromEntityRevenue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getFromEntityCost() {
		return fromEntityCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFromEntityCost(int newFromEntityCost) {
		int oldFromEntityCost = fromEntityCost;
		fromEntityCost = newFromEntityCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY_COST, oldFromEntityCost, fromEntityCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getToEntityName() {
		return toEntityName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setToEntityName(String newToEntityName) {
		String oldToEntityName = toEntityName;
		toEntityName = newToEntityName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TO_ENTITY_NAME, oldToEntityName, toEntityName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getToEntityRevenue() {
		return toEntityRevenue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setToEntityRevenue(int newToEntityRevenue) {
		int oldToEntityRevenue = toEntityRevenue;
		toEntityRevenue = newToEntityRevenue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TO_ENTITY_REVENUE, oldToEntityRevenue, toEntityRevenue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getToEntityCost() {
		return toEntityCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setToEntityCost(int newToEntityCost) {
		int oldToEntityCost = toEntityCost;
		toEntityCost = newToEntityCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TO_ENTITY_COST, oldToEntityCost, toEntityCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TRANSFER_PRICE:
				return getTransferPrice();
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY_NAME:
				return getFromEntityName();
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY_REVENUE:
				return getFromEntityRevenue();
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY_COST:
				return getFromEntityCost();
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TO_ENTITY_NAME:
				return getToEntityName();
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TO_ENTITY_REVENUE:
				return getToEntityRevenue();
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TO_ENTITY_COST:
				return getToEntityCost();
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
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TRANSFER_PRICE:
				setTransferPrice((Double)newValue);
				return;
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY_NAME:
				setFromEntityName((String)newValue);
				return;
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY_REVENUE:
				setFromEntityRevenue((Integer)newValue);
				return;
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY_COST:
				setFromEntityCost((Integer)newValue);
				return;
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TO_ENTITY_NAME:
				setToEntityName((String)newValue);
				return;
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TO_ENTITY_REVENUE:
				setToEntityRevenue((Integer)newValue);
				return;
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TO_ENTITY_COST:
				setToEntityCost((Integer)newValue);
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
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TRANSFER_PRICE:
				setTransferPrice(TRANSFER_PRICE_EDEFAULT);
				return;
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY_NAME:
				setFromEntityName(FROM_ENTITY_NAME_EDEFAULT);
				return;
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY_REVENUE:
				setFromEntityRevenue(FROM_ENTITY_REVENUE_EDEFAULT);
				return;
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY_COST:
				setFromEntityCost(FROM_ENTITY_COST_EDEFAULT);
				return;
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TO_ENTITY_NAME:
				setToEntityName(TO_ENTITY_NAME_EDEFAULT);
				return;
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TO_ENTITY_REVENUE:
				setToEntityRevenue(TO_ENTITY_REVENUE_EDEFAULT);
				return;
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TO_ENTITY_COST:
				setToEntityCost(TO_ENTITY_COST_EDEFAULT);
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
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TRANSFER_PRICE:
				return transferPrice != TRANSFER_PRICE_EDEFAULT;
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY_NAME:
				return FROM_ENTITY_NAME_EDEFAULT == null ? fromEntityName != null : !FROM_ENTITY_NAME_EDEFAULT.equals(fromEntityName);
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY_REVENUE:
				return fromEntityRevenue != FROM_ENTITY_REVENUE_EDEFAULT;
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY_COST:
				return fromEntityCost != FROM_ENTITY_COST_EDEFAULT;
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TO_ENTITY_NAME:
				return TO_ENTITY_NAME_EDEFAULT == null ? toEntityName != null : !TO_ENTITY_NAME_EDEFAULT.equals(toEntityName);
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TO_ENTITY_REVENUE:
				return toEntityRevenue != TO_ENTITY_REVENUE_EDEFAULT;
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TO_ENTITY_COST:
				return toEntityCost != TO_ENTITY_COST_EDEFAULT;
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
		result.append(" (transferPrice: ");
		result.append(transferPrice);
		result.append(", fromEntityName: ");
		result.append(fromEntityName);
		result.append(", fromEntityRevenue: ");
		result.append(fromEntityRevenue);
		result.append(", fromEntityCost: ");
		result.append(fromEntityCost);
		result.append(", toEntityName: ");
		result.append(toEntityName);
		result.append(", toEntityRevenue: ");
		result.append(toEntityRevenue);
		result.append(", toEntityCost: ");
		result.append(toEntityCost);
		result.append(')');
		return result.toString();
	}

} //TransferRecordPNLDetailsImpl
