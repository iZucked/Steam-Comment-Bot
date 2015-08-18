/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model;

import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.Change#getOriginalGroupProfitAndLoss <em>Original Group Profit And Loss</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.Change#getNewGroupProfitAndLoss <em>New Group Profit And Loss</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.Change#getOriginalEventGrouping <em>Original Event Grouping</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.Change#getNewEventGrouping <em>New Event Grouping</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChange()
 * @model
 * @generated
 */
public interface Change extends EObject {

	/**
	 * Returns the value of the '<em><b>Original Group Profit And Loss</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Original Group Profit And Loss</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Original Group Profit And Loss</em>' reference.
	 * @see #setOriginalGroupProfitAndLoss(ProfitAndLossContainer)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChange_OriginalGroupProfitAndLoss()
	 * @model
	 * @generated
	 */
	ProfitAndLossContainer getOriginalGroupProfitAndLoss();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.Change#getOriginalGroupProfitAndLoss <em>Original Group Profit And Loss</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Original Group Profit And Loss</em>' reference.
	 * @see #getOriginalGroupProfitAndLoss()
	 * @generated
	 */
	void setOriginalGroupProfitAndLoss(ProfitAndLossContainer value);

	/**
	 * Returns the value of the '<em><b>New Group Profit And Loss</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Group Profit And Loss</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Group Profit And Loss</em>' reference.
	 * @see #setNewGroupProfitAndLoss(ProfitAndLossContainer)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChange_NewGroupProfitAndLoss()
	 * @model
	 * @generated
	 */
	ProfitAndLossContainer getNewGroupProfitAndLoss();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.Change#getNewGroupProfitAndLoss <em>New Group Profit And Loss</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Group Profit And Loss</em>' reference.
	 * @see #getNewGroupProfitAndLoss()
	 * @generated
	 */
	void setNewGroupProfitAndLoss(ProfitAndLossContainer value);

	/**
	 * Returns the value of the '<em><b>Original Event Grouping</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Original Event Grouping</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Original Event Grouping</em>' reference.
	 * @see #setOriginalEventGrouping(EventGrouping)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChange_OriginalEventGrouping()
	 * @model
	 * @generated
	 */
	EventGrouping getOriginalEventGrouping();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.Change#getOriginalEventGrouping <em>Original Event Grouping</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Original Event Grouping</em>' reference.
	 * @see #getOriginalEventGrouping()
	 * @generated
	 */
	void setOriginalEventGrouping(EventGrouping value);

	/**
	 * Returns the value of the '<em><b>New Event Grouping</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Event Grouping</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Event Grouping</em>' reference.
	 * @see #setNewEventGrouping(EventGrouping)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChange_NewEventGrouping()
	 * @model
	 * @generated
	 */
	EventGrouping getNewEventGrouping();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.Change#getNewEventGrouping <em>New Event Grouping</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Event Grouping</em>' reference.
	 * @see #getNewEventGrouping()
	 * @generated
	 */
	void setNewEventGrouping(EventGrouping value);
} // Change
