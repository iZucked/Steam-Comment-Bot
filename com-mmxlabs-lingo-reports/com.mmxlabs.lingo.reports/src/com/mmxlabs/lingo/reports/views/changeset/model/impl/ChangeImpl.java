/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model.impl;

import com.mmxlabs.lingo.reports.views.changeset.model.Change;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Change</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeImpl#getOriginalGroupProfitAndLoss <em>Original Group Profit And Loss</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeImpl#getNewGroupProfitAndLoss <em>New Group Profit And Loss</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeImpl#getOriginalEventGrouping <em>Original Event Grouping</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.ChangeImpl#getNewEventGrouping <em>New Event Grouping</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ChangeImpl extends MinimalEObjectImpl.Container implements Change {
	/**
	 * The cached value of the '{@link #getOriginalGroupProfitAndLoss() <em>Original Group Profit And Loss</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginalGroupProfitAndLoss()
	 * @generated
	 * @ordered
	 */
	protected ProfitAndLossContainer originalGroupProfitAndLoss;
	/**
	 * The cached value of the '{@link #getNewGroupProfitAndLoss() <em>New Group Profit And Loss</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewGroupProfitAndLoss()
	 * @generated
	 * @ordered
	 */
	protected ProfitAndLossContainer newGroupProfitAndLoss;

	/**
	 * The cached value of the '{@link #getOriginalEventGrouping() <em>Original Event Grouping</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginalEventGrouping()
	 * @generated
	 * @ordered
	 */
	protected EventGrouping originalEventGrouping;
	/**
	 * The cached value of the '{@link #getNewEventGrouping() <em>New Event Grouping</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewEventGrouping()
	 * @generated
	 * @ordered
	 */
	protected EventGrouping newEventGrouping;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ChangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ChangesetPackage.Literals.CHANGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProfitAndLossContainer getOriginalGroupProfitAndLoss() {
		if (originalGroupProfitAndLoss != null && originalGroupProfitAndLoss.eIsProxy()) {
			InternalEObject oldOriginalGroupProfitAndLoss = (InternalEObject)originalGroupProfitAndLoss;
			originalGroupProfitAndLoss = (ProfitAndLossContainer)eResolveProxy(oldOriginalGroupProfitAndLoss);
			if (originalGroupProfitAndLoss != oldOriginalGroupProfitAndLoss) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE__ORIGINAL_GROUP_PROFIT_AND_LOSS, oldOriginalGroupProfitAndLoss, originalGroupProfitAndLoss));
			}
		}
		return originalGroupProfitAndLoss;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProfitAndLossContainer basicGetOriginalGroupProfitAndLoss() {
		return originalGroupProfitAndLoss;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOriginalGroupProfitAndLoss(ProfitAndLossContainer newOriginalGroupProfitAndLoss) {
		ProfitAndLossContainer oldOriginalGroupProfitAndLoss = originalGroupProfitAndLoss;
		originalGroupProfitAndLoss = newOriginalGroupProfitAndLoss;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE__ORIGINAL_GROUP_PROFIT_AND_LOSS, oldOriginalGroupProfitAndLoss, originalGroupProfitAndLoss));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProfitAndLossContainer getNewGroupProfitAndLoss() {
		if (newGroupProfitAndLoss != null && newGroupProfitAndLoss.eIsProxy()) {
			InternalEObject oldNewGroupProfitAndLoss = (InternalEObject)newGroupProfitAndLoss;
			newGroupProfitAndLoss = (ProfitAndLossContainer)eResolveProxy(oldNewGroupProfitAndLoss);
			if (newGroupProfitAndLoss != oldNewGroupProfitAndLoss) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE__NEW_GROUP_PROFIT_AND_LOSS, oldNewGroupProfitAndLoss, newGroupProfitAndLoss));
			}
		}
		return newGroupProfitAndLoss;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProfitAndLossContainer basicGetNewGroupProfitAndLoss() {
		return newGroupProfitAndLoss;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewGroupProfitAndLoss(ProfitAndLossContainer newNewGroupProfitAndLoss) {
		ProfitAndLossContainer oldNewGroupProfitAndLoss = newGroupProfitAndLoss;
		newGroupProfitAndLoss = newNewGroupProfitAndLoss;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE__NEW_GROUP_PROFIT_AND_LOSS, oldNewGroupProfitAndLoss, newGroupProfitAndLoss));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EventGrouping getOriginalEventGrouping() {
		if (originalEventGrouping != null && originalEventGrouping.eIsProxy()) {
			InternalEObject oldOriginalEventGrouping = (InternalEObject)originalEventGrouping;
			originalEventGrouping = (EventGrouping)eResolveProxy(oldOriginalEventGrouping);
			if (originalEventGrouping != oldOriginalEventGrouping) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE__ORIGINAL_EVENT_GROUPING, oldOriginalEventGrouping, originalEventGrouping));
			}
		}
		return originalEventGrouping;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EventGrouping basicGetOriginalEventGrouping() {
		return originalEventGrouping;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOriginalEventGrouping(EventGrouping newOriginalEventGrouping) {
		EventGrouping oldOriginalEventGrouping = originalEventGrouping;
		originalEventGrouping = newOriginalEventGrouping;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE__ORIGINAL_EVENT_GROUPING, oldOriginalEventGrouping, originalEventGrouping));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EventGrouping getNewEventGrouping() {
		if (newEventGrouping != null && newEventGrouping.eIsProxy()) {
			InternalEObject oldNewEventGrouping = (InternalEObject)newEventGrouping;
			newEventGrouping = (EventGrouping)eResolveProxy(oldNewEventGrouping);
			if (newEventGrouping != oldNewEventGrouping) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangesetPackage.CHANGE__NEW_EVENT_GROUPING, oldNewEventGrouping, newEventGrouping));
			}
		}
		return newEventGrouping;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EventGrouping basicGetNewEventGrouping() {
		return newEventGrouping;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewEventGrouping(EventGrouping newNewEventGrouping) {
		EventGrouping oldNewEventGrouping = newEventGrouping;
		newEventGrouping = newNewEventGrouping;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.CHANGE__NEW_EVENT_GROUPING, oldNewEventGrouping, newEventGrouping));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ChangesetPackage.CHANGE__ORIGINAL_GROUP_PROFIT_AND_LOSS:
				if (resolve) return getOriginalGroupProfitAndLoss();
				return basicGetOriginalGroupProfitAndLoss();
			case ChangesetPackage.CHANGE__NEW_GROUP_PROFIT_AND_LOSS:
				if (resolve) return getNewGroupProfitAndLoss();
				return basicGetNewGroupProfitAndLoss();
			case ChangesetPackage.CHANGE__ORIGINAL_EVENT_GROUPING:
				if (resolve) return getOriginalEventGrouping();
				return basicGetOriginalEventGrouping();
			case ChangesetPackage.CHANGE__NEW_EVENT_GROUPING:
				if (resolve) return getNewEventGrouping();
				return basicGetNewEventGrouping();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ChangesetPackage.CHANGE__ORIGINAL_GROUP_PROFIT_AND_LOSS:
				setOriginalGroupProfitAndLoss((ProfitAndLossContainer)newValue);
				return;
			case ChangesetPackage.CHANGE__NEW_GROUP_PROFIT_AND_LOSS:
				setNewGroupProfitAndLoss((ProfitAndLossContainer)newValue);
				return;
			case ChangesetPackage.CHANGE__ORIGINAL_EVENT_GROUPING:
				setOriginalEventGrouping((EventGrouping)newValue);
				return;
			case ChangesetPackage.CHANGE__NEW_EVENT_GROUPING:
				setNewEventGrouping((EventGrouping)newValue);
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
			case ChangesetPackage.CHANGE__ORIGINAL_GROUP_PROFIT_AND_LOSS:
				setOriginalGroupProfitAndLoss((ProfitAndLossContainer)null);
				return;
			case ChangesetPackage.CHANGE__NEW_GROUP_PROFIT_AND_LOSS:
				setNewGroupProfitAndLoss((ProfitAndLossContainer)null);
				return;
			case ChangesetPackage.CHANGE__ORIGINAL_EVENT_GROUPING:
				setOriginalEventGrouping((EventGrouping)null);
				return;
			case ChangesetPackage.CHANGE__NEW_EVENT_GROUPING:
				setNewEventGrouping((EventGrouping)null);
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
			case ChangesetPackage.CHANGE__ORIGINAL_GROUP_PROFIT_AND_LOSS:
				return originalGroupProfitAndLoss != null;
			case ChangesetPackage.CHANGE__NEW_GROUP_PROFIT_AND_LOSS:
				return newGroupProfitAndLoss != null;
			case ChangesetPackage.CHANGE__ORIGINAL_EVENT_GROUPING:
				return originalEventGrouping != null;
			case ChangesetPackage.CHANGE__NEW_EVENT_GROUPING:
				return newEventGrouping != null;
		}
		return super.eIsSet(featureID);
	}

} //ChangeImpl
