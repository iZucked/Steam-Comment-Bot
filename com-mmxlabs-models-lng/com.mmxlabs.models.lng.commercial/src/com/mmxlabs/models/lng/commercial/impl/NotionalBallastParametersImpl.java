/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.NotionalBallastParameters;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.types.AVesselClass;
import com.mmxlabs.models.mmxcore.impl.NamedObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Notional Ballast Parameters</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.NotionalBallastParametersImpl#getRoutes <em>Routes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.NotionalBallastParametersImpl#getSpeed <em>Speed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.NotionalBallastParametersImpl#getHireCost <em>Hire Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.NotionalBallastParametersImpl#getNboRate <em>Nbo Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.NotionalBallastParametersImpl#getBaseConsumption <em>Base Consumption</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.NotionalBallastParametersImpl#getVesselClasses <em>Vessel Classes</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class NotionalBallastParametersImpl extends NamedObjectImpl implements NotionalBallastParameters {
	/**
	 * The cached value of the '{@link #getRoutes() <em>Routes</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRoutes()
	 * @generated
	 * @ordered
	 */
	protected EList<Route> routes;

	/**
	 * The default value of the '{@link #getSpeed() <em>Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpeed()
	 * @generated
	 * @ordered
	 */
	protected static final double SPEED_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getSpeed() <em>Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpeed()
	 * @generated
	 * @ordered
	 */
	protected double speed = SPEED_EDEFAULT;

	/**
	 * This is true if the Speed attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean speedESet;

	/**
	 * The default value of the '{@link #getHireCost() <em>Hire Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHireCost()
	 * @generated
	 * @ordered
	 */
	protected static final int HIRE_COST_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getHireCost() <em>Hire Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHireCost()
	 * @generated
	 * @ordered
	 */
	protected int hireCost = HIRE_COST_EDEFAULT;

	/**
	 * This is true if the Hire Cost attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean hireCostESet;

	/**
	 * The default value of the '{@link #getNboRate() <em>Nbo Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNboRate()
	 * @generated
	 * @ordered
	 */
	protected static final int NBO_RATE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getNboRate() <em>Nbo Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNboRate()
	 * @generated
	 * @ordered
	 */
	protected int nboRate = NBO_RATE_EDEFAULT;

	/**
	 * This is true if the Nbo Rate attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean nboRateESet;

	/**
	 * The default value of the '{@link #getBaseConsumption() <em>Base Consumption</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseConsumption()
	 * @generated
	 * @ordered
	 */
	protected static final int BASE_CONSUMPTION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getBaseConsumption() <em>Base Consumption</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseConsumption()
	 * @generated
	 * @ordered
	 */
	protected int baseConsumption = BASE_CONSUMPTION_EDEFAULT;

	/**
	 * This is true if the Base Consumption attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean baseConsumptionESet;

	/**
	 * The cached value of the '{@link #getVesselClasses() <em>Vessel Classes</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselClasses()
	 * @generated
	 * @ordered
	 */
	protected EList<AVesselClass> vesselClasses;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NotionalBallastParametersImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CommercialPackage.Literals.NOTIONAL_BALLAST_PARAMETERS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Route> getRoutes() {
		if (routes == null) {
			routes = new EObjectResolvingEList<Route>(Route.class, this, CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__ROUTES);
		}
		return routes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSpeed(double newSpeed) {
		double oldSpeed = speed;
		speed = newSpeed;
		boolean oldSpeedESet = speedESet;
		speedESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__SPEED, oldSpeed, speed, !oldSpeedESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetSpeed() {
		double oldSpeed = speed;
		boolean oldSpeedESet = speedESet;
		speed = SPEED_EDEFAULT;
		speedESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__SPEED, oldSpeed, SPEED_EDEFAULT, oldSpeedESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetSpeed() {
		return speedESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getHireCost() {
		return hireCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHireCost(int newHireCost) {
		int oldHireCost = hireCost;
		hireCost = newHireCost;
		boolean oldHireCostESet = hireCostESet;
		hireCostESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__HIRE_COST, oldHireCost, hireCost, !oldHireCostESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetHireCost() {
		int oldHireCost = hireCost;
		boolean oldHireCostESet = hireCostESet;
		hireCost = HIRE_COST_EDEFAULT;
		hireCostESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__HIRE_COST, oldHireCost, HIRE_COST_EDEFAULT, oldHireCostESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetHireCost() {
		return hireCostESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getNboRate() {
		return nboRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNboRate(int newNboRate) {
		int oldNboRate = nboRate;
		nboRate = newNboRate;
		boolean oldNboRateESet = nboRateESet;
		nboRateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__NBO_RATE, oldNboRate, nboRate, !oldNboRateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetNboRate() {
		int oldNboRate = nboRate;
		boolean oldNboRateESet = nboRateESet;
		nboRate = NBO_RATE_EDEFAULT;
		nboRateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__NBO_RATE, oldNboRate, NBO_RATE_EDEFAULT, oldNboRateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetNboRate() {
		return nboRateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getBaseConsumption() {
		return baseConsumption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBaseConsumption(int newBaseConsumption) {
		int oldBaseConsumption = baseConsumption;
		baseConsumption = newBaseConsumption;
		boolean oldBaseConsumptionESet = baseConsumptionESet;
		baseConsumptionESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__BASE_CONSUMPTION, oldBaseConsumption, baseConsumption, !oldBaseConsumptionESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetBaseConsumption() {
		int oldBaseConsumption = baseConsumption;
		boolean oldBaseConsumptionESet = baseConsumptionESet;
		baseConsumption = BASE_CONSUMPTION_EDEFAULT;
		baseConsumptionESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__BASE_CONSUMPTION, oldBaseConsumption, BASE_CONSUMPTION_EDEFAULT, oldBaseConsumptionESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetBaseConsumption() {
		return baseConsumptionESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AVesselClass> getVesselClasses() {
		if (vesselClasses == null) {
			vesselClasses = new EObjectResolvingEList<AVesselClass>(AVesselClass.class, this, CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__VESSEL_CLASSES);
		}
		return vesselClasses;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__ROUTES:
				return getRoutes();
			case CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__SPEED:
				return getSpeed();
			case CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__HIRE_COST:
				return getHireCost();
			case CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__NBO_RATE:
				return getNboRate();
			case CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__BASE_CONSUMPTION:
				return getBaseConsumption();
			case CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__VESSEL_CLASSES:
				return getVesselClasses();
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
			case CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__ROUTES:
				getRoutes().clear();
				getRoutes().addAll((Collection<? extends Route>)newValue);
				return;
			case CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__SPEED:
				setSpeed((Double)newValue);
				return;
			case CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__HIRE_COST:
				setHireCost((Integer)newValue);
				return;
			case CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__NBO_RATE:
				setNboRate((Integer)newValue);
				return;
			case CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__BASE_CONSUMPTION:
				setBaseConsumption((Integer)newValue);
				return;
			case CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__VESSEL_CLASSES:
				getVesselClasses().clear();
				getVesselClasses().addAll((Collection<? extends AVesselClass>)newValue);
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
			case CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__ROUTES:
				getRoutes().clear();
				return;
			case CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__SPEED:
				unsetSpeed();
				return;
			case CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__HIRE_COST:
				unsetHireCost();
				return;
			case CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__NBO_RATE:
				unsetNboRate();
				return;
			case CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__BASE_CONSUMPTION:
				unsetBaseConsumption();
				return;
			case CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__VESSEL_CLASSES:
				getVesselClasses().clear();
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
			case CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__ROUTES:
				return routes != null && !routes.isEmpty();
			case CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__SPEED:
				return isSetSpeed();
			case CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__HIRE_COST:
				return isSetHireCost();
			case CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__NBO_RATE:
				return isSetNboRate();
			case CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__BASE_CONSUMPTION:
				return isSetBaseConsumption();
			case CommercialPackage.NOTIONAL_BALLAST_PARAMETERS__VESSEL_CLASSES:
				return vesselClasses != null && !vesselClasses.isEmpty();
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
		result.append(" (speed: ");
		if (speedESet) result.append(speed); else result.append("<unset>");
		result.append(", hireCost: ");
		if (hireCostESet) result.append(hireCost); else result.append("<unset>");
		result.append(", nboRate: ");
		if (nboRateESet) result.append(nboRate); else result.append("<unset>");
		result.append(", baseConsumption: ");
		if (baseConsumptionESet) result.append(baseConsumption); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} // end of NotionalBallastParametersImpl

// finish type fixing
