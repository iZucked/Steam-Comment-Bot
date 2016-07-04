/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import com.mmxlabs.models.lng.commercial.BaseEntityBook;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.schedule.EntityProfitAndLoss;
import com.mmxlabs.models.lng.schedule.SchedulePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Entity Profit And Loss</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.EntityProfitAndLossImpl#getEntity <em>Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.EntityProfitAndLossImpl#getEntityBook <em>Entity Book</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.EntityProfitAndLossImpl#getProfitAndLoss <em>Profit And Loss</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.EntityProfitAndLossImpl#getProfitAndLossPreTax <em>Profit And Loss Pre Tax</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EntityProfitAndLossImpl extends EObjectImpl implements EntityProfitAndLoss {
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
	 * The cached value of the '{@link #getEntityBook() <em>Entity Book</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntityBook()
	 * @generated
	 * @ordered
	 */
	protected BaseEntityBook entityBook;

	/**
	 * The default value of the '{@link #getProfitAndLoss() <em>Profit And Loss</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProfitAndLoss()
	 * @generated
	 * @ordered
	 */
	protected static final long PROFIT_AND_LOSS_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getProfitAndLoss() <em>Profit And Loss</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProfitAndLoss()
	 * @generated
	 * @ordered
	 */
	protected long profitAndLoss = PROFIT_AND_LOSS_EDEFAULT;

	/**
	 * The default value of the '{@link #getProfitAndLossPreTax() <em>Profit And Loss Pre Tax</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProfitAndLossPreTax()
	 * @generated
	 * @ordered
	 */
	protected static final long PROFIT_AND_LOSS_PRE_TAX_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getProfitAndLossPreTax() <em>Profit And Loss Pre Tax</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProfitAndLossPreTax()
	 * @generated
	 * @ordered
	 */
	protected long profitAndLossPreTax = PROFIT_AND_LOSS_PRE_TAX_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EntityProfitAndLossImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.ENTITY_PROFIT_AND_LOSS;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.ENTITY_PROFIT_AND_LOSS__ENTITY, oldEntity, entity));
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
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.ENTITY_PROFIT_AND_LOSS__ENTITY, oldEntity, entity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseEntityBook getEntityBook() {
		if (entityBook != null && entityBook.eIsProxy()) {
			InternalEObject oldEntityBook = (InternalEObject)entityBook;
			entityBook = (BaseEntityBook)eResolveProxy(oldEntityBook);
			if (entityBook != oldEntityBook) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.ENTITY_PROFIT_AND_LOSS__ENTITY_BOOK, oldEntityBook, entityBook));
			}
		}
		return entityBook;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BaseEntityBook basicGetEntityBook() {
		return entityBook;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEntityBook(BaseEntityBook newEntityBook) {
		BaseEntityBook oldEntityBook = entityBook;
		entityBook = newEntityBook;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.ENTITY_PROFIT_AND_LOSS__ENTITY_BOOK, oldEntityBook, entityBook));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getProfitAndLoss() {
		return profitAndLoss;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProfitAndLoss(long newProfitAndLoss) {
		long oldProfitAndLoss = profitAndLoss;
		profitAndLoss = newProfitAndLoss;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.ENTITY_PROFIT_AND_LOSS__PROFIT_AND_LOSS, oldProfitAndLoss, profitAndLoss));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getProfitAndLossPreTax() {
		return profitAndLossPreTax;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProfitAndLossPreTax(long newProfitAndLossPreTax) {
		long oldProfitAndLossPreTax = profitAndLossPreTax;
		profitAndLossPreTax = newProfitAndLossPreTax;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.ENTITY_PROFIT_AND_LOSS__PROFIT_AND_LOSS_PRE_TAX, oldProfitAndLossPreTax, profitAndLossPreTax));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchedulePackage.ENTITY_PROFIT_AND_LOSS__ENTITY:
				if (resolve) return getEntity();
				return basicGetEntity();
			case SchedulePackage.ENTITY_PROFIT_AND_LOSS__ENTITY_BOOK:
				if (resolve) return getEntityBook();
				return basicGetEntityBook();
			case SchedulePackage.ENTITY_PROFIT_AND_LOSS__PROFIT_AND_LOSS:
				return getProfitAndLoss();
			case SchedulePackage.ENTITY_PROFIT_AND_LOSS__PROFIT_AND_LOSS_PRE_TAX:
				return getProfitAndLossPreTax();
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
			case SchedulePackage.ENTITY_PROFIT_AND_LOSS__ENTITY:
				setEntity((BaseLegalEntity)newValue);
				return;
			case SchedulePackage.ENTITY_PROFIT_AND_LOSS__ENTITY_BOOK:
				setEntityBook((BaseEntityBook)newValue);
				return;
			case SchedulePackage.ENTITY_PROFIT_AND_LOSS__PROFIT_AND_LOSS:
				setProfitAndLoss((Long)newValue);
				return;
			case SchedulePackage.ENTITY_PROFIT_AND_LOSS__PROFIT_AND_LOSS_PRE_TAX:
				setProfitAndLossPreTax((Long)newValue);
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
			case SchedulePackage.ENTITY_PROFIT_AND_LOSS__ENTITY:
				setEntity((BaseLegalEntity)null);
				return;
			case SchedulePackage.ENTITY_PROFIT_AND_LOSS__ENTITY_BOOK:
				setEntityBook((BaseEntityBook)null);
				return;
			case SchedulePackage.ENTITY_PROFIT_AND_LOSS__PROFIT_AND_LOSS:
				setProfitAndLoss(PROFIT_AND_LOSS_EDEFAULT);
				return;
			case SchedulePackage.ENTITY_PROFIT_AND_LOSS__PROFIT_AND_LOSS_PRE_TAX:
				setProfitAndLossPreTax(PROFIT_AND_LOSS_PRE_TAX_EDEFAULT);
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
			case SchedulePackage.ENTITY_PROFIT_AND_LOSS__ENTITY:
				return entity != null;
			case SchedulePackage.ENTITY_PROFIT_AND_LOSS__ENTITY_BOOK:
				return entityBook != null;
			case SchedulePackage.ENTITY_PROFIT_AND_LOSS__PROFIT_AND_LOSS:
				return profitAndLoss != PROFIT_AND_LOSS_EDEFAULT;
			case SchedulePackage.ENTITY_PROFIT_AND_LOSS__PROFIT_AND_LOSS_PRE_TAX:
				return profitAndLossPreTax != PROFIT_AND_LOSS_PRE_TAX_EDEFAULT;
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
		result.append(" (profitAndLoss: ");
		result.append(profitAndLoss);
		result.append(", profitAndLossPreTax: ");
		result.append(profitAndLossPreTax);
		result.append(')');
		return result.toString();
	}

} //EntityProfitAndLossImpl
