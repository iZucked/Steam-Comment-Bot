/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.market.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import scenario.market.Index;
import scenario.market.MarketModel;
import scenario.market.MarketPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.market.impl.MarketModelImpl#getIndices <em>Indices</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MarketModelImpl extends EObjectImpl implements MarketModel {
	/**
	 * The cached value of the '{@link #getIndices() <em>Indices</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndices()
	 * @generated
	 * @ordered
	 */
	protected EList<Index> indices;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MarketModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MarketPackage.Literals.MARKET_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Index> getIndices() {
		if (indices == null) {
			indices = new EObjectContainmentEList<Index>(Index.class, this, MarketPackage.MARKET_MODEL__INDICES);
		}
		return indices;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MarketPackage.MARKET_MODEL__INDICES:
				return ((InternalEList<?>)getIndices()).basicRemove(otherEnd, msgs);
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
			case MarketPackage.MARKET_MODEL__INDICES:
				return getIndices();
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
			case MarketPackage.MARKET_MODEL__INDICES:
				getIndices().clear();
				getIndices().addAll((Collection<? extends Index>)newValue);
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
			case MarketPackage.MARKET_MODEL__INDICES:
				getIndices().clear();
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
			case MarketPackage.MARKET_MODEL__INDICES:
				return indices != null && !indices.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //MarketModelImpl
