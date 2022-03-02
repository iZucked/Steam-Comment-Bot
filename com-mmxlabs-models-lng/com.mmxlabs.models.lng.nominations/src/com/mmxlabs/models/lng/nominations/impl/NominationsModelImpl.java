/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.nominations.impl;

import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.AbstractNominationSpec;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.nominations.NominationsModel;
import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.lng.nominations.NominationsParameters;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.nominations.impl.NominationsModelImpl#getNominationSpecs <em>Nomination Specs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.impl.NominationsModelImpl#getNominations <em>Nominations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.nominations.impl.NominationsModelImpl#getNominationParameters <em>Nomination Parameters</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NominationsModelImpl extends UUIDObjectImpl implements NominationsModel {
	/**
	 * The cached value of the '{@link #getNominationSpecs() <em>Nomination Specs</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNominationSpecs()
	 * @generated
	 * @ordered
	 */
	protected EList<AbstractNominationSpec> nominationSpecs;

	/**
	 * The cached value of the '{@link #getNominations() <em>Nominations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNominations()
	 * @generated
	 * @ordered
	 */
	protected EList<AbstractNomination> nominations;

	/**
	 * The cached value of the '{@link #getNominationParameters() <em>Nomination Parameters</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNominationParameters()
	 * @generated
	 * @ordered
	 */
	protected NominationsParameters nominationParameters;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NominationsModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return NominationsPackage.Literals.NOMINATIONS_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<AbstractNominationSpec> getNominationSpecs() {
		if (nominationSpecs == null) {
			nominationSpecs = new EObjectContainmentEList<AbstractNominationSpec>(AbstractNominationSpec.class, this, NominationsPackage.NOMINATIONS_MODEL__NOMINATION_SPECS);
		}
		return nominationSpecs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<AbstractNomination> getNominations() {
		if (nominations == null) {
			nominations = new EObjectContainmentEList<AbstractNomination>(AbstractNomination.class, this, NominationsPackage.NOMINATIONS_MODEL__NOMINATIONS);
		}
		return nominations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NominationsParameters getNominationParameters() {
		return nominationParameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetNominationParameters(NominationsParameters newNominationParameters, NotificationChain msgs) {
		NominationsParameters oldNominationParameters = nominationParameters;
		nominationParameters = newNominationParameters;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, NominationsPackage.NOMINATIONS_MODEL__NOMINATION_PARAMETERS, oldNominationParameters, newNominationParameters);
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
	public void setNominationParameters(NominationsParameters newNominationParameters) {
		if (newNominationParameters != nominationParameters) {
			NotificationChain msgs = null;
			if (nominationParameters != null)
				msgs = ((InternalEObject)nominationParameters).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - NominationsPackage.NOMINATIONS_MODEL__NOMINATION_PARAMETERS, null, msgs);
			if (newNominationParameters != null)
				msgs = ((InternalEObject)newNominationParameters).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - NominationsPackage.NOMINATIONS_MODEL__NOMINATION_PARAMETERS, null, msgs);
			msgs = basicSetNominationParameters(newNominationParameters, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NominationsPackage.NOMINATIONS_MODEL__NOMINATION_PARAMETERS, newNominationParameters, newNominationParameters));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case NominationsPackage.NOMINATIONS_MODEL__NOMINATION_SPECS:
				return ((InternalEList<?>)getNominationSpecs()).basicRemove(otherEnd, msgs);
			case NominationsPackage.NOMINATIONS_MODEL__NOMINATIONS:
				return ((InternalEList<?>)getNominations()).basicRemove(otherEnd, msgs);
			case NominationsPackage.NOMINATIONS_MODEL__NOMINATION_PARAMETERS:
				return basicSetNominationParameters(null, msgs);
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
			case NominationsPackage.NOMINATIONS_MODEL__NOMINATION_SPECS:
				return getNominationSpecs();
			case NominationsPackage.NOMINATIONS_MODEL__NOMINATIONS:
				return getNominations();
			case NominationsPackage.NOMINATIONS_MODEL__NOMINATION_PARAMETERS:
				return getNominationParameters();
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
			case NominationsPackage.NOMINATIONS_MODEL__NOMINATION_SPECS:
				getNominationSpecs().clear();
				getNominationSpecs().addAll((Collection<? extends AbstractNominationSpec>)newValue);
				return;
			case NominationsPackage.NOMINATIONS_MODEL__NOMINATIONS:
				getNominations().clear();
				getNominations().addAll((Collection<? extends AbstractNomination>)newValue);
				return;
			case NominationsPackage.NOMINATIONS_MODEL__NOMINATION_PARAMETERS:
				setNominationParameters((NominationsParameters)newValue);
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
			case NominationsPackage.NOMINATIONS_MODEL__NOMINATION_SPECS:
				getNominationSpecs().clear();
				return;
			case NominationsPackage.NOMINATIONS_MODEL__NOMINATIONS:
				getNominations().clear();
				return;
			case NominationsPackage.NOMINATIONS_MODEL__NOMINATION_PARAMETERS:
				setNominationParameters((NominationsParameters)null);
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
			case NominationsPackage.NOMINATIONS_MODEL__NOMINATION_SPECS:
				return nominationSpecs != null && !nominationSpecs.isEmpty();
			case NominationsPackage.NOMINATIONS_MODEL__NOMINATIONS:
				return nominations != null && !nominations.isEmpty();
			case NominationsPackage.NOMINATIONS_MODEL__NOMINATION_PARAMETERS:
				return nominationParameters != null;
		}
		return super.eIsSet(featureID);
	}

} //NominationsModelImpl
