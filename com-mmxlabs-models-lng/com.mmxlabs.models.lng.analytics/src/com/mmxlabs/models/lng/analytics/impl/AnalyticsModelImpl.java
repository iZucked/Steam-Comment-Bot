/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.impl;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AnalyticsModelImpl#getOptionModels <em>Option Models</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AnalyticsModelImpl#getOptimisations <em>Optimisations</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AnalyticsModelImpl extends UUIDObjectImpl implements AnalyticsModel {
	/**
	 * The cached value of the '{@link #getOptionModels() <em>Option Models</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOptionModels()
	 * @generated
	 * @ordered
	 */
	protected EList<OptionAnalysisModel> optionModels;

	/**
	 * The cached value of the '{@link #getOptimisations() <em>Optimisations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOptimisations()
	 * @generated
	 * @ordered
	 */
	protected EList<AbstractSolutionSet> optimisations;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AnalyticsModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.ANALYTICS_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<OptionAnalysisModel> getOptionModels() {
		if (optionModels == null) {
			optionModels = new EObjectContainmentEList<OptionAnalysisModel>(OptionAnalysisModel.class, this, AnalyticsPackage.ANALYTICS_MODEL__OPTION_MODELS);
		}
		return optionModels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AbstractSolutionSet> getOptimisations() {
		if (optimisations == null) {
			optimisations = new EObjectContainmentEList<AbstractSolutionSet>(AbstractSolutionSet.class, this, AnalyticsPackage.ANALYTICS_MODEL__OPTIMISATIONS);
		}
		return optimisations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.ANALYTICS_MODEL__OPTION_MODELS:
				return ((InternalEList<?>)getOptionModels()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.ANALYTICS_MODEL__OPTIMISATIONS:
				return ((InternalEList<?>)getOptimisations()).basicRemove(otherEnd, msgs);
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
			case AnalyticsPackage.ANALYTICS_MODEL__OPTION_MODELS:
				return getOptionModels();
			case AnalyticsPackage.ANALYTICS_MODEL__OPTIMISATIONS:
				return getOptimisations();
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
			case AnalyticsPackage.ANALYTICS_MODEL__OPTION_MODELS:
				getOptionModels().clear();
				getOptionModels().addAll((Collection<? extends OptionAnalysisModel>)newValue);
				return;
			case AnalyticsPackage.ANALYTICS_MODEL__OPTIMISATIONS:
				getOptimisations().clear();
				getOptimisations().addAll((Collection<? extends AbstractSolutionSet>)newValue);
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
			case AnalyticsPackage.ANALYTICS_MODEL__OPTION_MODELS:
				getOptionModels().clear();
				return;
			case AnalyticsPackage.ANALYTICS_MODEL__OPTIMISATIONS:
				getOptimisations().clear();
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
			case AnalyticsPackage.ANALYTICS_MODEL__OPTION_MODELS:
				return optionModels != null && !optionModels.isEmpty();
			case AnalyticsPackage.ANALYTICS_MODEL__OPTIMISATIONS:
				return optimisations != null && !optimisations.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // end of AnalyticsModelImpl

// finish type fixing
