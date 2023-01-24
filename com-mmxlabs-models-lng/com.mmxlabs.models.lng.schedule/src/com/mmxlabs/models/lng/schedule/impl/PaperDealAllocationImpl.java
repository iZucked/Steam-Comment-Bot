/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule.impl;

import com.mmxlabs.models.lng.cargo.PaperDeal;

import com.mmxlabs.models.lng.schedule.PaperDealAllocation;
import com.mmxlabs.models.lng.schedule.PaperDealAllocationEntry;
import com.mmxlabs.models.lng.schedule.SchedulePackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Paper Deal Allocation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.PaperDealAllocationImpl#getPaperDeal <em>Paper Deal</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.PaperDealAllocationImpl#getEntries <em>Entries</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PaperDealAllocationImpl extends ProfitAndLossContainerImpl implements PaperDealAllocation {
	/**
	 * The cached value of the '{@link #getPaperDeal() <em>Paper Deal</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPaperDeal()
	 * @generated
	 * @ordered
	 */
	protected PaperDeal paperDeal;

	/**
	 * The cached value of the '{@link #getEntries() <em>Entries</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntries()
	 * @generated
	 * @ordered
	 */
	protected EList<PaperDealAllocationEntry> entries;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PaperDealAllocationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.PAPER_DEAL_ALLOCATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PaperDeal getPaperDeal() {
		if (paperDeal != null && paperDeal.eIsProxy()) {
			InternalEObject oldPaperDeal = (InternalEObject)paperDeal;
			paperDeal = (PaperDeal)eResolveProxy(oldPaperDeal);
			if (paperDeal != oldPaperDeal) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.PAPER_DEAL_ALLOCATION__PAPER_DEAL, oldPaperDeal, paperDeal));
			}
		}
		return paperDeal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PaperDeal basicGetPaperDeal() {
		return paperDeal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPaperDeal(PaperDeal newPaperDeal) {
		PaperDeal oldPaperDeal = paperDeal;
		paperDeal = newPaperDeal;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.PAPER_DEAL_ALLOCATION__PAPER_DEAL, oldPaperDeal, paperDeal));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<PaperDealAllocationEntry> getEntries() {
		if (entries == null) {
			entries = new EObjectContainmentEList<PaperDealAllocationEntry>(PaperDealAllocationEntry.class, this, SchedulePackage.PAPER_DEAL_ALLOCATION__ENTRIES);
		}
		return entries;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.PAPER_DEAL_ALLOCATION__ENTRIES:
				return ((InternalEList<?>)getEntries()).basicRemove(otherEnd, msgs);
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
			case SchedulePackage.PAPER_DEAL_ALLOCATION__PAPER_DEAL:
				if (resolve) return getPaperDeal();
				return basicGetPaperDeal();
			case SchedulePackage.PAPER_DEAL_ALLOCATION__ENTRIES:
				return getEntries();
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
			case SchedulePackage.PAPER_DEAL_ALLOCATION__PAPER_DEAL:
				setPaperDeal((PaperDeal)newValue);
				return;
			case SchedulePackage.PAPER_DEAL_ALLOCATION__ENTRIES:
				getEntries().clear();
				getEntries().addAll((Collection<? extends PaperDealAllocationEntry>)newValue);
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
			case SchedulePackage.PAPER_DEAL_ALLOCATION__PAPER_DEAL:
				setPaperDeal((PaperDeal)null);
				return;
			case SchedulePackage.PAPER_DEAL_ALLOCATION__ENTRIES:
				getEntries().clear();
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
			case SchedulePackage.PAPER_DEAL_ALLOCATION__PAPER_DEAL:
				return paperDeal != null;
			case SchedulePackage.PAPER_DEAL_ALLOCATION__ENTRIES:
				return entries != null && !entries.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //PaperDealAllocationImpl
