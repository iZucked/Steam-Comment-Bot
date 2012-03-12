

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.pricing.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.pricing.PortCostEntry;
import com.mmxlabs.models.lng.pricing.PortCostVessels;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Port Cost Vessels</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PortCostVesselsImpl#getVessels <em>Vessels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PortCostVesselsImpl#getPortCostEntries <em>Port Cost Entries</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PortCostVesselsImpl extends MMXObjectImpl implements PortCostVessels {
	/**
	 * The cached value of the '{@link #getVessels() <em>Vessels</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVessels()
	 * @generated
	 * @ordered
	 */
	protected EList<AVesselSet> vessels;

	/**
	 * The cached value of the '{@link #getPortCostEntries() <em>Port Cost Entries</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortCostEntries()
	 * @generated
	 * @ordered
	 */
	protected EList<PortCostEntry> portCostEntries;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PortCostVesselsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.PORT_COST_VESSELS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AVesselSet> getVessels() {
		if (vessels == null) {
			vessels = new EObjectResolvingEList<AVesselSet>(AVesselSet.class, this, PricingPackage.PORT_COST_VESSELS__VESSELS);
		}
		return vessels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PortCostEntry> getPortCostEntries() {
		if (portCostEntries == null) {
			portCostEntries = new EObjectContainmentEList<PortCostEntry>(PortCostEntry.class, this, PricingPackage.PORT_COST_VESSELS__PORT_COST_ENTRIES);
		}
		return portCostEntries;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PricingPackage.PORT_COST_VESSELS__PORT_COST_ENTRIES:
				return ((InternalEList<?>)getPortCostEntries()).basicRemove(otherEnd, msgs);
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
			case PricingPackage.PORT_COST_VESSELS__VESSELS:
				return getVessels();
			case PricingPackage.PORT_COST_VESSELS__PORT_COST_ENTRIES:
				return getPortCostEntries();
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
			case PricingPackage.PORT_COST_VESSELS__VESSELS:
				getVessels().clear();
				getVessels().addAll((Collection<? extends AVesselSet>)newValue);
				return;
			case PricingPackage.PORT_COST_VESSELS__PORT_COST_ENTRIES:
				getPortCostEntries().clear();
				getPortCostEntries().addAll((Collection<? extends PortCostEntry>)newValue);
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
			case PricingPackage.PORT_COST_VESSELS__VESSELS:
				getVessels().clear();
				return;
			case PricingPackage.PORT_COST_VESSELS__PORT_COST_ENTRIES:
				getPortCostEntries().clear();
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
			case PricingPackage.PORT_COST_VESSELS__VESSELS:
				return vessels != null && !vessels.isEmpty();
			case PricingPackage.PORT_COST_VESSELS__PORT_COST_ENTRIES:
				return portCostEntries != null && !portCostEntries.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // end of PortCostVesselsImpl

// finish type fixing
