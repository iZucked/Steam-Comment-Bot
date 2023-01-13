/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.impl;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisModel;
import com.mmxlabs.models.lng.analytics.MTMModel;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixModel;
import com.mmxlabs.models.lng.analytics.ViabilityModel;
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
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AnalyticsModelImpl#getViabilityModel <em>Viability Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AnalyticsModelImpl#getMtmModel <em>Mtm Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AnalyticsModelImpl#getBreakevenModels <em>Breakeven Models</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AnalyticsModelImpl#getSwapValueMatrixModels <em>Swap Value Matrix Models</em>}</li>
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
	 * The cached value of the '{@link #getViabilityModel() <em>Viability Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getViabilityModel()
	 * @generated
	 * @ordered
	 */
	protected ViabilityModel viabilityModel;

	/**
	 * The cached value of the '{@link #getMtmModel() <em>Mtm Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMtmModel()
	 * @generated
	 * @ordered
	 */
	protected MTMModel mtmModel;

	/**
	 * The cached value of the '{@link #getBreakevenModels() <em>Breakeven Models</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBreakevenModels()
	 * @generated
	 * @ordered
	 */
	protected EList<BreakEvenAnalysisModel> breakevenModels;

	/**
	 * The cached value of the '{@link #getSwapValueMatrixModels() <em>Swap Value Matrix Models</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSwapValueMatrixModels()
	 * @generated
	 * @ordered
	 */
	protected EList<SwapValueMatrixModel> swapValueMatrixModels;

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
	@Override
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
	@Override
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
	public ViabilityModel getViabilityModel() {
		return viabilityModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetViabilityModel(ViabilityModel newViabilityModel, NotificationChain msgs) {
		ViabilityModel oldViabilityModel = viabilityModel;
		viabilityModel = newViabilityModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.ANALYTICS_MODEL__VIABILITY_MODEL, oldViabilityModel, newViabilityModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setViabilityModel(ViabilityModel newViabilityModel) {
		if (newViabilityModel != viabilityModel) {
			NotificationChain msgs = null;
			if (viabilityModel != null)
				msgs = ((InternalEObject)viabilityModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.ANALYTICS_MODEL__VIABILITY_MODEL, null, msgs);
			if (newViabilityModel != null)
				msgs = ((InternalEObject)newViabilityModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.ANALYTICS_MODEL__VIABILITY_MODEL, null, msgs);
			msgs = basicSetViabilityModel(newViabilityModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.ANALYTICS_MODEL__VIABILITY_MODEL, newViabilityModel, newViabilityModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MTMModel getMtmModel() {
		return mtmModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMtmModel(MTMModel newMtmModel, NotificationChain msgs) {
		MTMModel oldMtmModel = mtmModel;
		mtmModel = newMtmModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.ANALYTICS_MODEL__MTM_MODEL, oldMtmModel, newMtmModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMtmModel(MTMModel newMtmModel) {
		if (newMtmModel != mtmModel) {
			NotificationChain msgs = null;
			if (mtmModel != null)
				msgs = ((InternalEObject)mtmModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.ANALYTICS_MODEL__MTM_MODEL, null, msgs);
			if (newMtmModel != null)
				msgs = ((InternalEObject)newMtmModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.ANALYTICS_MODEL__MTM_MODEL, null, msgs);
			msgs = basicSetMtmModel(newMtmModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.ANALYTICS_MODEL__MTM_MODEL, newMtmModel, newMtmModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<BreakEvenAnalysisModel> getBreakevenModels() {
		if (breakevenModels == null) {
			breakevenModels = new EObjectContainmentEList<BreakEvenAnalysisModel>(BreakEvenAnalysisModel.class, this, AnalyticsPackage.ANALYTICS_MODEL__BREAKEVEN_MODELS);
		}
		return breakevenModels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<SwapValueMatrixModel> getSwapValueMatrixModels() {
		if (swapValueMatrixModels == null) {
			swapValueMatrixModels = new EObjectContainmentEList<SwapValueMatrixModel>(SwapValueMatrixModel.class, this, AnalyticsPackage.ANALYTICS_MODEL__SWAP_VALUE_MATRIX_MODELS);
		}
		return swapValueMatrixModels;
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
			case AnalyticsPackage.ANALYTICS_MODEL__VIABILITY_MODEL:
				return basicSetViabilityModel(null, msgs);
			case AnalyticsPackage.ANALYTICS_MODEL__MTM_MODEL:
				return basicSetMtmModel(null, msgs);
			case AnalyticsPackage.ANALYTICS_MODEL__BREAKEVEN_MODELS:
				return ((InternalEList<?>)getBreakevenModels()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.ANALYTICS_MODEL__SWAP_VALUE_MATRIX_MODELS:
				return ((InternalEList<?>)getSwapValueMatrixModels()).basicRemove(otherEnd, msgs);
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
			case AnalyticsPackage.ANALYTICS_MODEL__VIABILITY_MODEL:
				return getViabilityModel();
			case AnalyticsPackage.ANALYTICS_MODEL__MTM_MODEL:
				return getMtmModel();
			case AnalyticsPackage.ANALYTICS_MODEL__BREAKEVEN_MODELS:
				return getBreakevenModels();
			case AnalyticsPackage.ANALYTICS_MODEL__SWAP_VALUE_MATRIX_MODELS:
				return getSwapValueMatrixModels();
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
			case AnalyticsPackage.ANALYTICS_MODEL__VIABILITY_MODEL:
				setViabilityModel((ViabilityModel)newValue);
				return;
			case AnalyticsPackage.ANALYTICS_MODEL__MTM_MODEL:
				setMtmModel((MTMModel)newValue);
				return;
			case AnalyticsPackage.ANALYTICS_MODEL__BREAKEVEN_MODELS:
				getBreakevenModels().clear();
				getBreakevenModels().addAll((Collection<? extends BreakEvenAnalysisModel>)newValue);
				return;
			case AnalyticsPackage.ANALYTICS_MODEL__SWAP_VALUE_MATRIX_MODELS:
				getSwapValueMatrixModels().clear();
				getSwapValueMatrixModels().addAll((Collection<? extends SwapValueMatrixModel>)newValue);
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
			case AnalyticsPackage.ANALYTICS_MODEL__VIABILITY_MODEL:
				setViabilityModel((ViabilityModel)null);
				return;
			case AnalyticsPackage.ANALYTICS_MODEL__MTM_MODEL:
				setMtmModel((MTMModel)null);
				return;
			case AnalyticsPackage.ANALYTICS_MODEL__BREAKEVEN_MODELS:
				getBreakevenModels().clear();
				return;
			case AnalyticsPackage.ANALYTICS_MODEL__SWAP_VALUE_MATRIX_MODELS:
				getSwapValueMatrixModels().clear();
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
			case AnalyticsPackage.ANALYTICS_MODEL__VIABILITY_MODEL:
				return viabilityModel != null;
			case AnalyticsPackage.ANALYTICS_MODEL__MTM_MODEL:
				return mtmModel != null;
			case AnalyticsPackage.ANALYTICS_MODEL__BREAKEVEN_MODELS:
				return breakevenModels != null && !breakevenModels.isEmpty();
			case AnalyticsPackage.ANALYTICS_MODEL__SWAP_VALUE_MATRIX_MODELS:
				return swapValueMatrixModels != null && !swapValueMatrixModels.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // end of AnalyticsModelImpl

// finish type fixing
