/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PortCostEntry;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Port Cost</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PortCostImpl#getPorts <em>Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PortCostImpl#getEntries <em>Entries</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PortCostImpl#getReferenceCapacity <em>Reference Capacity</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PortCostImpl extends MMXObjectImpl implements PortCost {
	/**
	 * The cached value of the '{@link #getPorts() <em>Ports</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet<Port>> ports;

	/**
	 * The cached value of the '{@link #getEntries() <em>Entries</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntries()
	 * @generated
	 * @ordered
	 */
	protected EList<PortCostEntry> entries;

	/**
	 * The default value of the '{@link #getReferenceCapacity() <em>Reference Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferenceCapacity()
	 * @generated
	 * @ordered
	 */
	protected static final int REFERENCE_CAPACITY_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getReferenceCapacity() <em>Reference Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferenceCapacity()
	 * @generated
	 * @ordered
	 */
	protected int referenceCapacity = REFERENCE_CAPACITY_EDEFAULT;

	/**
	 * This is true if the Reference Capacity attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean referenceCapacityESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PortCostImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.PORT_COST;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<APortSet<Port>> getPorts() {
		if (ports == null) {
			ports = new EObjectResolvingEList<APortSet<Port>>(APortSet.class, this, PricingPackage.PORT_COST__PORTS);
		}
		return ports;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PortCostEntry> getEntries() {
		if (entries == null) {
			entries = new EObjectContainmentEList<PortCostEntry>(PortCostEntry.class, this, PricingPackage.PORT_COST__ENTRIES);
		}
		return entries;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getReferenceCapacity() {
		return referenceCapacity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReferenceCapacity(int newReferenceCapacity) {
		int oldReferenceCapacity = referenceCapacity;
		referenceCapacity = newReferenceCapacity;
		boolean oldReferenceCapacityESet = referenceCapacityESet;
		referenceCapacityESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.PORT_COST__REFERENCE_CAPACITY, oldReferenceCapacity, referenceCapacity, !oldReferenceCapacityESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetReferenceCapacity() {
		int oldReferenceCapacity = referenceCapacity;
		boolean oldReferenceCapacityESet = referenceCapacityESet;
		referenceCapacity = REFERENCE_CAPACITY_EDEFAULT;
		referenceCapacityESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, PricingPackage.PORT_COST__REFERENCE_CAPACITY, oldReferenceCapacity, REFERENCE_CAPACITY_EDEFAULT, oldReferenceCapacityESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetReferenceCapacity() {
		return referenceCapacityESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getPortCost(final VesselClass vesselClass, final PortCapability activity) {
		for (final PortCostEntry entry : getEntries()) {
			if (entry.getActivity() == activity) {
				if (isSetReferenceCapacity()) {
					return (int)
						(entry.getCost() * (((VesselClass)vesselClass).getCapacity() / (double) getReferenceCapacity()));
				} else {
					return entry.getCost();
				}
			}
		}
		return 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PricingPackage.PORT_COST__ENTRIES:
				return ((InternalEList<?>)getEntries()).basicRemove(otherEnd, msgs);
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
			case PricingPackage.PORT_COST__PORTS:
				return getPorts();
			case PricingPackage.PORT_COST__ENTRIES:
				return getEntries();
			case PricingPackage.PORT_COST__REFERENCE_CAPACITY:
				return getReferenceCapacity();
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
			case PricingPackage.PORT_COST__PORTS:
				getPorts().clear();
				getPorts().addAll((Collection<? extends APortSet<Port>>)newValue);
				return;
			case PricingPackage.PORT_COST__ENTRIES:
				getEntries().clear();
				getEntries().addAll((Collection<? extends PortCostEntry>)newValue);
				return;
			case PricingPackage.PORT_COST__REFERENCE_CAPACITY:
				setReferenceCapacity((Integer)newValue);
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
			case PricingPackage.PORT_COST__PORTS:
				getPorts().clear();
				return;
			case PricingPackage.PORT_COST__ENTRIES:
				getEntries().clear();
				return;
			case PricingPackage.PORT_COST__REFERENCE_CAPACITY:
				unsetReferenceCapacity();
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
			case PricingPackage.PORT_COST__PORTS:
				return ports != null && !ports.isEmpty();
			case PricingPackage.PORT_COST__ENTRIES:
				return entries != null && !entries.isEmpty();
			case PricingPackage.PORT_COST__REFERENCE_CAPACITY:
				return isSetReferenceCapacity();
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
		result.append(" (referenceCapacity: ");
		if (referenceCapacityESet) result.append(referenceCapacity); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} // end of PortCostImpl

// finish type fixing
