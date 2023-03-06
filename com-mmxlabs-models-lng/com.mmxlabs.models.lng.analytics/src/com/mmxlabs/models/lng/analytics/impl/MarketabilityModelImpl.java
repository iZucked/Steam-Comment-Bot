/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.MarketabilityModel;
import com.mmxlabs.models.lng.analytics.MarketabilityRow;
import com.mmxlabs.models.lng.analytics.ViabilityRow;

import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;

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

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Marketability Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityModelImpl#getRows <em>Rows</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityModelImpl#getMarkets <em>Markets</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.MarketabilityModelImpl#getVesselSpeed <em>Vessel Speed</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MarketabilityModelImpl extends AbstractAnalysisModelImpl implements MarketabilityModel {
	/**
	 * The cached value of the '{@link #getRows() <em>Rows</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRows()
	 * @generated
	 * @ordered
	 */
	protected EList<MarketabilityRow> rows;

	/**
	 * The cached value of the '{@link #getMarkets() <em>Markets</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarkets()
	 * @generated
	 * @ordered
	 */
	protected EList<SpotMarket> markets;

	/**
	 * The default value of the '{@link #getVesselSpeed() <em>Vessel Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselSpeed()
	 * @generated
	 * @ordered
	 */
	protected static final int VESSEL_SPEED_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getVesselSpeed() <em>Vessel Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselSpeed()
	 * @generated
	 * @ordered
	 */
	protected int vesselSpeed = VESSEL_SPEED_EDEFAULT;

	/**
	 * This is true if the Vessel Speed attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean vesselSpeedESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MarketabilityModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.MARKETABILITY_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<MarketabilityRow> getRows() {
		if (rows == null) {
			rows = new EObjectContainmentEList<MarketabilityRow>(MarketabilityRow.class, this, AnalyticsPackage.MARKETABILITY_MODEL__ROWS);
		}
		return rows;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<SpotMarket> getMarkets() {
		if (markets == null) {
			markets = new EObjectResolvingEList<SpotMarket>(SpotMarket.class, this, AnalyticsPackage.MARKETABILITY_MODEL__MARKETS);
		}
		return markets;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getVesselSpeed() {
		return vesselSpeed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVesselSpeed(int newVesselSpeed) {
		int oldVesselSpeed = vesselSpeed;
		vesselSpeed = newVesselSpeed;
		boolean oldVesselSpeedESet = vesselSpeedESet;
		vesselSpeedESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.MARKETABILITY_MODEL__VESSEL_SPEED, oldVesselSpeed, vesselSpeed, !oldVesselSpeedESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetVesselSpeed() {
		int oldVesselSpeed = vesselSpeed;
		boolean oldVesselSpeedESet = vesselSpeedESet;
		vesselSpeed = VESSEL_SPEED_EDEFAULT;
		vesselSpeedESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, AnalyticsPackage.MARKETABILITY_MODEL__VESSEL_SPEED, oldVesselSpeed, VESSEL_SPEED_EDEFAULT, oldVesselSpeedESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetVesselSpeed() {
		return vesselSpeedESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.MARKETABILITY_MODEL__ROWS:
				return ((InternalEList<?>)getRows()).basicRemove(otherEnd, msgs);
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
			case AnalyticsPackage.MARKETABILITY_MODEL__ROWS:
				return getRows();
			case AnalyticsPackage.MARKETABILITY_MODEL__MARKETS:
				return getMarkets();
			case AnalyticsPackage.MARKETABILITY_MODEL__VESSEL_SPEED:
				return getVesselSpeed();
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
			case AnalyticsPackage.MARKETABILITY_MODEL__ROWS:
				getRows().clear();
				getRows().addAll((Collection<? extends MarketabilityRow>)newValue);
				return;
			case AnalyticsPackage.MARKETABILITY_MODEL__MARKETS:
				getMarkets().clear();
				getMarkets().addAll((Collection<? extends SpotMarket>)newValue);
				return;
			case AnalyticsPackage.MARKETABILITY_MODEL__VESSEL_SPEED:
				setVesselSpeed((Integer)newValue);
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
			case AnalyticsPackage.MARKETABILITY_MODEL__ROWS:
				getRows().clear();
				return;
			case AnalyticsPackage.MARKETABILITY_MODEL__MARKETS:
				getMarkets().clear();
				return;
			case AnalyticsPackage.MARKETABILITY_MODEL__VESSEL_SPEED:
				unsetVesselSpeed();
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
			case AnalyticsPackage.MARKETABILITY_MODEL__ROWS:
				return rows != null && !rows.isEmpty();
			case AnalyticsPackage.MARKETABILITY_MODEL__MARKETS:
				return markets != null && !markets.isEmpty();
			case AnalyticsPackage.MARKETABILITY_MODEL__VESSEL_SPEED:
				return isSetVesselSpeed();
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
		result.append(" (vesselSpeed: ");
		if (vesselSpeedESet) result.append(vesselSpeed); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //MarketabilityModelImpl
