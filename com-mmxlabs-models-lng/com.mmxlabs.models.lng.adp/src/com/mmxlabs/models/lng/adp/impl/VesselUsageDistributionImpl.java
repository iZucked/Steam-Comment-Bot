/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.VesselUsageDistribution;

import com.mmxlabs.models.lng.fleet.Vessel;

import com.mmxlabs.models.lng.fleet.VesselGroup;
import com.mmxlabs.models.lng.types.AVesselSet;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vessel Usage Distribution</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.VesselUsageDistributionImpl#getCargoes <em>Cargoes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.VesselUsageDistributionImpl#getVessels <em>Vessels</em>}</li>
 * </ul>
 *
 * @generated
 */
public class VesselUsageDistributionImpl extends EObjectImpl implements VesselUsageDistribution {
	/**
	 * The default value of the '{@link #getCargoes() <em>Cargoes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoes()
	 * @generated
	 * @ordered
	 */
	protected static final int CARGOES_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getCargoes() <em>Cargoes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoes()
	 * @generated
	 * @ordered
	 */
	protected int cargoes = CARGOES_EDEFAULT;

	/**
	 * The cached value of the '{@link #getVessels() <em>Vessels</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVessels()
	 * @generated
	 * @ordered
	 */
	protected EList<AVesselSet<Vessel>> vessels;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselUsageDistributionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.VESSEL_USAGE_DISTRIBUTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getCargoes() {
		return cargoes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCargoes(int newCargoes) {
		int oldCargoes = cargoes;
		cargoes = newCargoes;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.VESSEL_USAGE_DISTRIBUTION__CARGOES, oldCargoes, cargoes));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<AVesselSet<Vessel>> getVessels() {
		if (vessels == null) {
			vessels = new EObjectResolvingEList<AVesselSet<Vessel>>(AVesselSet.class, this, ADPPackage.VESSEL_USAGE_DISTRIBUTION__VESSELS);
		}
		return vessels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ADPPackage.VESSEL_USAGE_DISTRIBUTION__CARGOES:
				return getCargoes();
			case ADPPackage.VESSEL_USAGE_DISTRIBUTION__VESSELS:
				return getVessels();
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
			case ADPPackage.VESSEL_USAGE_DISTRIBUTION__CARGOES:
				setCargoes((Integer)newValue);
				return;
			case ADPPackage.VESSEL_USAGE_DISTRIBUTION__VESSELS:
				getVessels().clear();
				getVessels().addAll((Collection<? extends AVesselSet<Vessel>>)newValue);
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
			case ADPPackage.VESSEL_USAGE_DISTRIBUTION__CARGOES:
				setCargoes(CARGOES_EDEFAULT);
				return;
			case ADPPackage.VESSEL_USAGE_DISTRIBUTION__VESSELS:
				getVessels().clear();
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
			case ADPPackage.VESSEL_USAGE_DISTRIBUTION__CARGOES:
				return cargoes != CARGOES_EDEFAULT;
			case ADPPackage.VESSEL_USAGE_DISTRIBUTION__VESSELS:
				return vessels != null && !vessels.isEmpty();
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
		result.append(" (cargoes: ");
		result.append(cargoes);
		result.append(')');
		return result.toString();
	}

} //VesselUsageDistributionImpl
