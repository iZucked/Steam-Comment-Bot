/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Idle</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.IdleImpl#getFuels <em>Fuels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.IdleImpl#isLaden <em>Laden</em>}</li>
 * </ul>
 *
 * @generated
 */
public class IdleImpl extends EventImpl implements Idle {
	/**
	 * The cached value of the '{@link #getFuels() <em>Fuels</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFuels()
	 * @generated
	 * @ordered
	 */
	protected EList<FuelQuantity> fuels;

	/**
	 * The default value of the '{@link #isLaden() <em>Laden</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLaden()
	 * @generated
	 * @ordered
	 */
	protected static final boolean LADEN_EDEFAULT = false;
	/**
	 * The cached value of the '{@link #isLaden() <em>Laden</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLaden()
	 * @generated
	 * @ordered
	 */
	protected boolean laden = LADEN_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IdleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.IDLE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<FuelQuantity> getFuels() {
		if (fuels == null) {
			fuels = new EObjectContainmentEList<FuelQuantity>(FuelQuantity.class, this, SchedulePackage.IDLE__FUELS);
		}
		return fuels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isLaden() {
		return laden;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLaden(boolean newLaden) {
		boolean oldLaden = laden;
		laden = newLaden;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.IDLE__LADEN, oldLaden, laden));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getFuelCost() {
		int sum = 0;
		for (final FuelQuantity fq : getFuels()) {
			sum += fq.getCost();
		}
		return sum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.IDLE__FUELS:
				return ((InternalEList<?>)getFuels()).basicRemove(otherEnd, msgs);
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
			case SchedulePackage.IDLE__FUELS:
				return getFuels();
			case SchedulePackage.IDLE__LADEN:
				return isLaden();
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
			case SchedulePackage.IDLE__FUELS:
				getFuels().clear();
				getFuels().addAll((Collection<? extends FuelQuantity>)newValue);
				return;
			case SchedulePackage.IDLE__LADEN:
				setLaden((Boolean)newValue);
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
			case SchedulePackage.IDLE__FUELS:
				getFuels().clear();
				return;
			case SchedulePackage.IDLE__LADEN:
				setLaden(LADEN_EDEFAULT);
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
			case SchedulePackage.IDLE__FUELS:
				return fuels != null && !fuels.isEmpty();
			case SchedulePackage.IDLE__LADEN:
				return laden != LADEN_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == FuelUsage.class) {
			switch (derivedFeatureID) {
				case SchedulePackage.IDLE__FUELS: return SchedulePackage.FUEL_USAGE__FUELS;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == FuelUsage.class) {
			switch (baseFeatureID) {
				case SchedulePackage.FUEL_USAGE__FUELS: return SchedulePackage.IDLE__FUELS;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedOperationID(int baseOperationID, Class<?> baseClass) {
		if (baseClass == FuelUsage.class) {
			switch (baseOperationID) {
				case SchedulePackage.FUEL_USAGE___GET_FUEL_COST: return SchedulePackage.IDLE___GET_FUEL_COST;
				default: return -1;
			}
		}
		return super.eDerivedOperationID(baseOperationID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case SchedulePackage.IDLE___GET_FUEL_COST:
				return getFuelCost();
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
		result.append(" (laden: ");
		result.append(laden);
		result.append(')');
		return result.toString();
	}

	/**
	 * @generated NOT
	 */
	@Override
	public String type() {
		return "Idle";
	}

	/**
	 * @generated NOT
	 */
	@Override
	public String name() {
		
		// Work backwards through the events list to find a slot or cargo to get the name
		final EObject container = this.eContainer();
		if (container instanceof Sequence) {
			final Sequence sequence = (Sequence) container;
			final EList<Event> events = sequence.getEvents();
			int idx = events.indexOf(this);
			if (idx >= 0) {
				while (--idx >= 0) {
					final EObject namedObject = events.get(idx);
					if (namedObject instanceof SlotVisit) {
						return ((SlotVisit) namedObject).name();
					} else if (namedObject instanceof VesselEventVisit) {
						return ((VesselEventVisit) namedObject).name();
					} else if (namedObject instanceof StartEventImpl) {
						return ((StartEventImpl) namedObject).name();
					} 
				}
			}
		}

		return super.name();
	}
} // end of IdleImpl

// finish type fixing
