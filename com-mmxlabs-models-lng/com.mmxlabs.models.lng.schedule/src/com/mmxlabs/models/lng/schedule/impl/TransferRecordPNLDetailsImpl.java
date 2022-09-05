/**
 */
package com.mmxlabs.models.lng.schedule.impl;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.TransferRecordPNLDetails;

import com.mmxlabs.models.mmxcore.NamedObject;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Transfer Record PNL Details</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.TransferRecordPNLDetailsImpl#getTransferRecord <em>Transfer Record</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.TransferRecordPNLDetailsImpl#getTransferPrice <em>Transfer Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.TransferRecordPNLDetailsImpl#getFromEntity <em>From Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.TransferRecordPNLDetailsImpl#getFromEntityRevenue <em>From Entity Revenue</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.TransferRecordPNLDetailsImpl#getFromEntityCost <em>From Entity Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.TransferRecordPNLDetailsImpl#getToEntity <em>To Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.TransferRecordPNLDetailsImpl#getToEntityRevenue <em>To Entity Revenue</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.TransferRecordPNLDetailsImpl#getToEntityCost <em>To Entity Cost</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TransferRecordPNLDetailsImpl extends GeneralPNLDetailsImpl implements TransferRecordPNLDetails {
	/**
	 * The cached value of the '{@link #getTransferRecord() <em>Transfer Record</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransferRecord()
	 * @generated
	 * @ordered
	 */
	protected NamedObject transferRecord;

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
	 * The cached value of the '{@link #getFromEntity() <em>From Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFromEntity()
	 * @generated
	 * @ordered
	 */
	protected BaseLegalEntity fromEntity;

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
	 * The cached value of the '{@link #getToEntity() <em>To Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getToEntity()
	 * @generated
	 * @ordered
	 */
	protected BaseLegalEntity toEntity;

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
	public NamedObject getTransferRecord() {
		if (transferRecord != null && transferRecord.eIsProxy()) {
			InternalEObject oldTransferRecord = (InternalEObject)transferRecord;
			transferRecord = (NamedObject)eResolveProxy(oldTransferRecord);
			if (transferRecord != oldTransferRecord) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TRANSFER_RECORD, oldTransferRecord, transferRecord));
			}
		}
		return transferRecord;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NamedObject basicGetTransferRecord() {
		return transferRecord;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTransferRecord(NamedObject newTransferRecord) {
		NamedObject oldTransferRecord = transferRecord;
		transferRecord = newTransferRecord;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TRANSFER_RECORD, oldTransferRecord, transferRecord));
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
	public BaseLegalEntity getFromEntity() {
		if (fromEntity != null && fromEntity.eIsProxy()) {
			InternalEObject oldFromEntity = (InternalEObject)fromEntity;
			fromEntity = (BaseLegalEntity)eResolveProxy(oldFromEntity);
			if (fromEntity != oldFromEntity) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY, oldFromEntity, fromEntity));
			}
		}
		return fromEntity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseLegalEntity basicGetFromEntity() {
		return fromEntity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFromEntity(BaseLegalEntity newFromEntity) {
		BaseLegalEntity oldFromEntity = fromEntity;
		fromEntity = newFromEntity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY, oldFromEntity, fromEntity));
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
	public BaseLegalEntity getToEntity() {
		if (toEntity != null && toEntity.eIsProxy()) {
			InternalEObject oldToEntity = (InternalEObject)toEntity;
			toEntity = (BaseLegalEntity)eResolveProxy(oldToEntity);
			if (toEntity != oldToEntity) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TO_ENTITY, oldToEntity, toEntity));
			}
		}
		return toEntity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseLegalEntity basicGetToEntity() {
		return toEntity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setToEntity(BaseLegalEntity newToEntity) {
		BaseLegalEntity oldToEntity = toEntity;
		toEntity = newToEntity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TO_ENTITY, oldToEntity, toEntity));
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
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TRANSFER_RECORD:
				if (resolve) return getTransferRecord();
				return basicGetTransferRecord();
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TRANSFER_PRICE:
				return getTransferPrice();
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY:
				if (resolve) return getFromEntity();
				return basicGetFromEntity();
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY_REVENUE:
				return getFromEntityRevenue();
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY_COST:
				return getFromEntityCost();
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TO_ENTITY:
				if (resolve) return getToEntity();
				return basicGetToEntity();
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
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TRANSFER_RECORD:
				setTransferRecord((NamedObject)newValue);
				return;
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TRANSFER_PRICE:
				setTransferPrice((Double)newValue);
				return;
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY:
				setFromEntity((BaseLegalEntity)newValue);
				return;
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY_REVENUE:
				setFromEntityRevenue((Integer)newValue);
				return;
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY_COST:
				setFromEntityCost((Integer)newValue);
				return;
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TO_ENTITY:
				setToEntity((BaseLegalEntity)newValue);
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
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TRANSFER_RECORD:
				setTransferRecord((NamedObject)null);
				return;
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TRANSFER_PRICE:
				setTransferPrice(TRANSFER_PRICE_EDEFAULT);
				return;
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY:
				setFromEntity((BaseLegalEntity)null);
				return;
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY_REVENUE:
				setFromEntityRevenue(FROM_ENTITY_REVENUE_EDEFAULT);
				return;
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY_COST:
				setFromEntityCost(FROM_ENTITY_COST_EDEFAULT);
				return;
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TO_ENTITY:
				setToEntity((BaseLegalEntity)null);
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
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TRANSFER_RECORD:
				return transferRecord != null;
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TRANSFER_PRICE:
				return transferPrice != TRANSFER_PRICE_EDEFAULT;
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY:
				return fromEntity != null;
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY_REVENUE:
				return fromEntityRevenue != FROM_ENTITY_REVENUE_EDEFAULT;
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__FROM_ENTITY_COST:
				return fromEntityCost != FROM_ENTITY_COST_EDEFAULT;
			case SchedulePackage.TRANSFER_RECORD_PNL_DETAILS__TO_ENTITY:
				return toEntity != null;
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
		result.append(", fromEntityRevenue: ");
		result.append(fromEntityRevenue);
		result.append(", fromEntityCost: ");
		result.append(fromEntityCost);
		result.append(", toEntityRevenue: ");
		result.append(toEntityRevenue);
		result.append(", toEntityCost: ");
		result.append(toEntityCost);
		result.append(')');
		return result.toString();
	}

} //TransferRecordPNLDetailsImpl
