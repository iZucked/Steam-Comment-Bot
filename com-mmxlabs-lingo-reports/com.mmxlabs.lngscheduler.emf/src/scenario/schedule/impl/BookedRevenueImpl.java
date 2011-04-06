/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule.impl;

import java.lang.reflect.InvocationTargetException;

import java.util.Collection;
import java.util.Date;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import scenario.contract.Entity;

import scenario.impl.ScenarioObjectImpl;

import scenario.schedule.BookedRevenue;
import scenario.schedule.CargoAllocation;
import scenario.schedule.LineItem;
import scenario.schedule.SchedulePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Booked Revenue</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.schedule.impl.BookedRevenueImpl#getEntity <em>Entity</em>}</li>
 *   <li>{@link scenario.schedule.impl.BookedRevenueImpl#getDate <em>Date</em>}</li>
 *   <li>{@link scenario.schedule.impl.BookedRevenueImpl#getLineItems <em>Line Items</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BookedRevenueImpl extends ScenarioObjectImpl implements BookedRevenue {
	/**
	 * The cached value of the '{@link #getEntity() <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntity()
	 * @generated
	 * @ordered
	 */
	protected Entity entity;

	/**
	 * The default value of the '{@link #getDate() <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDate()
	 * @generated
	 * @ordered
	 */
	protected static final Date DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDate() <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDate()
	 * @generated
	 * @ordered
	 */
	protected Date date = DATE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getLineItems() <em>Line Items</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLineItems()
	 * @generated
	 * @ordered
	 */
	protected EList<LineItem> lineItems;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BookedRevenueImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.BOOKED_REVENUE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Entity getEntity() {
		if (entity != null && entity.eIsProxy()) {
			InternalEObject oldEntity = (InternalEObject)entity;
			entity = (Entity)eResolveProxy(oldEntity);
			if (entity != oldEntity) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.BOOKED_REVENUE__ENTITY, oldEntity, entity));
			}
		}
		return entity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Entity basicGetEntity() {
		return entity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEntity(Entity newEntity) {
		Entity oldEntity = entity;
		entity = newEntity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.BOOKED_REVENUE__ENTITY, oldEntity, entity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Date getDate() {
		return date;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDate(Date newDate) {
		Date oldDate = date;
		date = newDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.BOOKED_REVENUE__DATE, oldDate, date));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<LineItem> getLineItems() {
		if (lineItems == null) {
			lineItems = new EObjectContainmentEList<LineItem>(LineItem.class, this, SchedulePackage.BOOKED_REVENUE__LINE_ITEMS);
		}
		return lineItems;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getUntaxedValue() {
		int untaxedRevenue = 0;
		
		for (final LineItem item : getLineItems()) {
			untaxedRevenue += item.getValue();
		}
		
		return untaxedRevenue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getTaxedValue() {
		final int untaxedRevenue = getUntaxedValue();
		
		if (untaxedRevenue <= 0) {
			return untaxedRevenue;
		} else {
			return (int) (untaxedRevenue * (1.0 - getEntity().getTaxRate()));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getUntaxedRevenues() {
		int untaxedRevenue = 0;
		
		for (final LineItem item : getLineItems()) {
			if (item.getValue() > 0)
				untaxedRevenue += item.getValue();
		}
		
		return untaxedRevenue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getUntaxedCosts() {
		int untaxedRevenue = 0;
		
		for (final LineItem item : getLineItems()) {
			if (item.getValue() < 0)
			untaxedRevenue += item.getValue();
		}
		
		return untaxedRevenue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getTaxCost() {
		return getUntaxedValue() - getTaxedValue();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.BOOKED_REVENUE__LINE_ITEMS:
				return ((InternalEList<?>)getLineItems()).basicRemove(otherEnd, msgs);
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
			case SchedulePackage.BOOKED_REVENUE__ENTITY:
				if (resolve) return getEntity();
				return basicGetEntity();
			case SchedulePackage.BOOKED_REVENUE__DATE:
				return getDate();
			case SchedulePackage.BOOKED_REVENUE__LINE_ITEMS:
				return getLineItems();
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
			case SchedulePackage.BOOKED_REVENUE__ENTITY:
				setEntity((Entity)newValue);
				return;
			case SchedulePackage.BOOKED_REVENUE__DATE:
				setDate((Date)newValue);
				return;
			case SchedulePackage.BOOKED_REVENUE__LINE_ITEMS:
				getLineItems().clear();
				getLineItems().addAll((Collection<? extends LineItem>)newValue);
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
			case SchedulePackage.BOOKED_REVENUE__ENTITY:
				setEntity((Entity)null);
				return;
			case SchedulePackage.BOOKED_REVENUE__DATE:
				setDate(DATE_EDEFAULT);
				return;
			case SchedulePackage.BOOKED_REVENUE__LINE_ITEMS:
				getLineItems().clear();
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
			case SchedulePackage.BOOKED_REVENUE__ENTITY:
				return entity != null;
			case SchedulePackage.BOOKED_REVENUE__DATE:
				return DATE_EDEFAULT == null ? date != null : !DATE_EDEFAULT.equals(date);
			case SchedulePackage.BOOKED_REVENUE__LINE_ITEMS:
				return lineItems != null && !lineItems.isEmpty();
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
			case SchedulePackage.BOOKED_REVENUE___GET_UNTAXED_VALUE:
				return getUntaxedValue();
			case SchedulePackage.BOOKED_REVENUE___GET_TAXED_VALUE:
				return getTaxedValue();
			case SchedulePackage.BOOKED_REVENUE___GET_UNTAXED_REVENUES:
				return getUntaxedRevenues();
			case SchedulePackage.BOOKED_REVENUE___GET_UNTAXED_COSTS:
				return getUntaxedCosts();
			case SchedulePackage.BOOKED_REVENUE___GET_TAX_COST:
				return getTaxCost();
			case SchedulePackage.BOOKED_REVENUE___GET_NAME:
				return getName();
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

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (date: ");
		result.append(date);
		result.append(')');
		return result.toString();
	}

} //BookedRevenueImpl
