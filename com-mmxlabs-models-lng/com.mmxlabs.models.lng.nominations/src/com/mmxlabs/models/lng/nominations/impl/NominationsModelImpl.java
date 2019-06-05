/**
 */
package com.mmxlabs.models.lng.nominations.impl;

import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.AbstractNominationSpec;
import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.nominations.ContractNomination;
import com.mmxlabs.models.lng.nominations.ContractNominationSpec;
import com.mmxlabs.models.lng.nominations.NominationsModel;
import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.lng.nominations.SlotNomination;
import com.mmxlabs.models.lng.nominations.SlotNominationSpec;
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
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case NominationsPackage.NOMINATIONS_MODEL__NOMINATION_SPECS:
				return ((InternalEList<?>)getNominationSpecs()).basicRemove(otherEnd, msgs);
			case NominationsPackage.NOMINATIONS_MODEL__NOMINATIONS:
				return ((InternalEList<?>)getNominations()).basicRemove(otherEnd, msgs);
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
		}
		return super.eIsSet(featureID);
	}

} //NominationsModelImpl
