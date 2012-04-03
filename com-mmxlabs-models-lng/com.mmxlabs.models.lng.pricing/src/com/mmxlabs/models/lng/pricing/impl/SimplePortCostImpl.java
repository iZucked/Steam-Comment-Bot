

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.pricing.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.pricing.PortCostEntry;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.SimplePortCost;
import com.mmxlabs.models.lng.types.AVessel;
import com.mmxlabs.models.lng.types.AVesselClass;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.PortCapability;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Simple Port Cost</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SimplePortCostImpl#getReferenceCapacity <em>Reference Capacity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SimplePortCostImpl#getAppliesTo <em>Applies To</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SimplePortCostImpl extends PortCostDefinitionImpl implements SimplePortCost {
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
	 * The cached value of the '{@link #getAppliesTo() <em>Applies To</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAppliesTo()
	 * @generated
	 * @ordered
	 */
	protected EList<AVesselSet> appliesTo;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SimplePortCostImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.SIMPLE_PORT_COST;
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
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SIMPLE_PORT_COST__REFERENCE_CAPACITY, oldReferenceCapacity, referenceCapacity, !oldReferenceCapacityESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, PricingPackage.SIMPLE_PORT_COST__REFERENCE_CAPACITY, oldReferenceCapacity, REFERENCE_CAPACITY_EDEFAULT, oldReferenceCapacityESet));
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
	public EList<AVesselSet> getAppliesTo() {
		if (appliesTo == null) {
			appliesTo = new EObjectResolvingEList<AVesselSet>(AVesselSet.class, this, PricingPackage.SIMPLE_PORT_COST__APPLIES_TO);
		}
		return appliesTo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getPortCost(AVessel vessel, AVesselClass vesselClass, PortCapability activity) {
		for (final PortCostEntry entry : getEntries()) {
			if (entry.getActivity() == activity) {
				return (int)
					(entry.getCost() * ((VesselClass)vesselClass).getCapacity() / (double) getReferenceCapacity());
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
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PricingPackage.SIMPLE_PORT_COST__REFERENCE_CAPACITY:
				return getReferenceCapacity();
			case PricingPackage.SIMPLE_PORT_COST__APPLIES_TO:
				return getAppliesTo();
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
			case PricingPackage.SIMPLE_PORT_COST__REFERENCE_CAPACITY:
				setReferenceCapacity((Integer)newValue);
				return;
			case PricingPackage.SIMPLE_PORT_COST__APPLIES_TO:
				getAppliesTo().clear();
				getAppliesTo().addAll((Collection<? extends AVesselSet>)newValue);
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
			case PricingPackage.SIMPLE_PORT_COST__REFERENCE_CAPACITY:
				unsetReferenceCapacity();
				return;
			case PricingPackage.SIMPLE_PORT_COST__APPLIES_TO:
				getAppliesTo().clear();
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
			case PricingPackage.SIMPLE_PORT_COST__REFERENCE_CAPACITY:
				return isSetReferenceCapacity();
			case PricingPackage.SIMPLE_PORT_COST__APPLIES_TO:
				return appliesTo != null && !appliesTo.isEmpty();
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

} // end of SimplePortCostImpl

// finish type fixing
