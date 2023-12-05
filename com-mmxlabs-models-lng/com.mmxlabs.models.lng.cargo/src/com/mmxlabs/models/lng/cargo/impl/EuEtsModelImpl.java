/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.EmissionsCoveredTable;
import com.mmxlabs.models.lng.cargo.EuEtsModel;

import com.mmxlabs.models.lng.port.PortGroup;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Eu Ets Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.EuEtsModelImpl#getEuaPriceExpression <em>Eua Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.EuEtsModelImpl#getEuPortGroup <em>Eu Port Group</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.EuEtsModelImpl#getEmissionsCovered <em>Emissions Covered</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EuEtsModelImpl extends EObjectImpl implements EuEtsModel {
	/**
	 * The default value of the '{@link #getEuaPriceExpression() <em>Eua Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEuaPriceExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String EUA_PRICE_EXPRESSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEuaPriceExpression() <em>Eua Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEuaPriceExpression()
	 * @generated
	 * @ordered
	 */
	protected String euaPriceExpression = EUA_PRICE_EXPRESSION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getEuPortGroup() <em>Eu Port Group</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEuPortGroup()
	 * @generated
	 * @ordered
	 */
	protected PortGroup euPortGroup;

	/**
	 * The cached value of the '{@link #getEmissionsCovered() <em>Emissions Covered</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEmissionsCovered()
	 * @generated
	 * @ordered
	 */
	protected EList<EmissionsCoveredTable> emissionsCovered;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EuEtsModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.EU_ETS_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getEuaPriceExpression() {
		return euaPriceExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEuaPriceExpression(String newEuaPriceExpression) {
		String oldEuaPriceExpression = euaPriceExpression;
		euaPriceExpression = newEuaPriceExpression;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.EU_ETS_MODEL__EUA_PRICE_EXPRESSION, oldEuaPriceExpression, euaPriceExpression));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<EmissionsCoveredTable> getEmissionsCovered() {
		if (emissionsCovered == null) {
			emissionsCovered = new EObjectContainmentEList.Resolving<EmissionsCoveredTable>(EmissionsCoveredTable.class, this, CargoPackage.EU_ETS_MODEL__EMISSIONS_COVERED);
		}
		return emissionsCovered;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PortGroup getEuPortGroup() {
		if (euPortGroup != null && euPortGroup.eIsProxy()) {
			InternalEObject oldEuPortGroup = (InternalEObject)euPortGroup;
			euPortGroup = (PortGroup)eResolveProxy(oldEuPortGroup);
			if (euPortGroup != oldEuPortGroup) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.EU_ETS_MODEL__EU_PORT_GROUP, oldEuPortGroup, euPortGroup));
			}
		}
		return euPortGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PortGroup basicGetEuPortGroup() {
		return euPortGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEuPortGroup(PortGroup newEuPortGroup) {
		PortGroup oldEuPortGroup = euPortGroup;
		euPortGroup = newEuPortGroup;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.EU_ETS_MODEL__EU_PORT_GROUP, oldEuPortGroup, euPortGroup));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CargoPackage.EU_ETS_MODEL__EMISSIONS_COVERED:
				return ((InternalEList<?>)getEmissionsCovered()).basicRemove(otherEnd, msgs);
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
			case CargoPackage.EU_ETS_MODEL__EUA_PRICE_EXPRESSION:
				return getEuaPriceExpression();
			case CargoPackage.EU_ETS_MODEL__EU_PORT_GROUP:
				if (resolve) return getEuPortGroup();
				return basicGetEuPortGroup();
			case CargoPackage.EU_ETS_MODEL__EMISSIONS_COVERED:
				return getEmissionsCovered();
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
			case CargoPackage.EU_ETS_MODEL__EUA_PRICE_EXPRESSION:
				setEuaPriceExpression((String)newValue);
				return;
			case CargoPackage.EU_ETS_MODEL__EU_PORT_GROUP:
				setEuPortGroup((PortGroup)newValue);
				return;
			case CargoPackage.EU_ETS_MODEL__EMISSIONS_COVERED:
				getEmissionsCovered().clear();
				getEmissionsCovered().addAll((Collection<? extends EmissionsCoveredTable>)newValue);
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
			case CargoPackage.EU_ETS_MODEL__EUA_PRICE_EXPRESSION:
				setEuaPriceExpression(EUA_PRICE_EXPRESSION_EDEFAULT);
				return;
			case CargoPackage.EU_ETS_MODEL__EU_PORT_GROUP:
				setEuPortGroup((PortGroup)null);
				return;
			case CargoPackage.EU_ETS_MODEL__EMISSIONS_COVERED:
				getEmissionsCovered().clear();
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
			case CargoPackage.EU_ETS_MODEL__EUA_PRICE_EXPRESSION:
				return EUA_PRICE_EXPRESSION_EDEFAULT == null ? euaPriceExpression != null : !EUA_PRICE_EXPRESSION_EDEFAULT.equals(euaPriceExpression);
			case CargoPackage.EU_ETS_MODEL__EU_PORT_GROUP:
				return euPortGroup != null;
			case CargoPackage.EU_ETS_MODEL__EMISSIONS_COVERED:
				return emissionsCovered != null && !emissionsCovered.isEmpty();
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
		result.append(" (euaPriceExpression: ");
		result.append(euaPriceExpression);
		result.append(')');
		return result.toString();
	}

} //EuEtsModelImpl
