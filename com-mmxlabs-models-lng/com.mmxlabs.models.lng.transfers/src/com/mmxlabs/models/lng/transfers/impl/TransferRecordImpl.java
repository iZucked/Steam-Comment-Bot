/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.transfers.impl;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.BusinessUnit;
import com.mmxlabs.models.lng.transfers.CompanyStatus;
import com.mmxlabs.models.lng.transfers.TransferAgreement;
import com.mmxlabs.models.lng.transfers.TransferIncoterm;
import com.mmxlabs.models.lng.transfers.TransferRecord;
import com.mmxlabs.models.lng.transfers.TransferStatus;
import com.mmxlabs.models.lng.transfers.TransfersPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.mmxcore.MMXObject.DelegateInformation;
import com.mmxlabs.models.mmxcore.impl.NamedObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Transfer Record</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.transfers.impl.TransferRecordImpl#getUuid <em>Uuid</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.impl.TransferRecordImpl#getTransferAgreement <em>Transfer Agreement</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.impl.TransferRecordImpl#getLhs <em>Lhs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.impl.TransferRecordImpl#getRhs <em>Rhs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.impl.TransferRecordImpl#getCargoReleaseDate <em>Cargo Release Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.impl.TransferRecordImpl#getPriceExpression <em>Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.impl.TransferRecordImpl#getPricingDate <em>Pricing Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.impl.TransferRecordImpl#getIncoterm <em>Incoterm</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.impl.TransferRecordImpl#getStatus <em>Status</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.impl.TransferRecordImpl#getNotes <em>Notes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.impl.TransferRecordImpl#isStale <em>Stale</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.impl.TransferRecordImpl#getPricingBasis <em>Pricing Basis</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.impl.TransferRecordImpl#getFromBU <em>From BU</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.transfers.impl.TransferRecordImpl#getToBU <em>To BU</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TransferRecordImpl extends NamedObjectImpl implements TransferRecord {
	/**
	 * The default value of the '{@link #getUuid() <em>Uuid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUuid()
	 * @generated
	 * @ordered
	 */
	protected static final String UUID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUuid() <em>Uuid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUuid()
	 * @generated
	 * @ordered
	 */
	protected String uuid = UUID_EDEFAULT;

	/**
	 * The cached value of the '{@link #getTransferAgreement() <em>Transfer Agreement</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransferAgreement()
	 * @generated
	 * @ordered
	 */
	protected TransferAgreement transferAgreement;

	/**
	 * The cached value of the '{@link #getLhs() <em>Lhs</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLhs()
	 * @generated
	 * @ordered
	 */
	protected Slot<?> lhs;

	/**
	 * This is true if the Lhs reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean lhsESet;

	/**
	 * The cached value of the '{@link #getRhs() <em>Rhs</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRhs()
	 * @generated
	 * @ordered
	 */
	protected TransferRecord rhs;

	/**
	 * This is true if the Rhs reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean rhsESet;

	/**
	 * The default value of the '{@link #getCargoReleaseDate() <em>Cargo Release Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoReleaseDate()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate CARGO_RELEASE_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCargoReleaseDate() <em>Cargo Release Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoReleaseDate()
	 * @generated
	 * @ordered
	 */
	protected LocalDate cargoReleaseDate = CARGO_RELEASE_DATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getPriceExpression() <em>Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriceExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String PRICE_EXPRESSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPriceExpression() <em>Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriceExpression()
	 * @generated
	 * @ordered
	 */
	protected String priceExpression = PRICE_EXPRESSION_EDEFAULT;

	/**
	 * This is true if the Price Expression attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean priceExpressionESet;

	/**
	 * The default value of the '{@link #getPricingDate() <em>Pricing Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPricingDate()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate PRICING_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPricingDate() <em>Pricing Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPricingDate()
	 * @generated
	 * @ordered
	 */
	protected LocalDate pricingDate = PRICING_DATE_EDEFAULT;

	/**
	 * This is true if the Pricing Date attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean pricingDateESet;

	/**
	 * The default value of the '{@link #getIncoterm() <em>Incoterm</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIncoterm()
	 * @generated
	 * @ordered
	 */
	protected static final TransferIncoterm INCOTERM_EDEFAULT = TransferIncoterm.BOTH;

	/**
	 * The cached value of the '{@link #getIncoterm() <em>Incoterm</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIncoterm()
	 * @generated
	 * @ordered
	 */
	protected TransferIncoterm incoterm = INCOTERM_EDEFAULT;

	/**
	 * This is true if the Incoterm attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean incotermESet;

	/**
	 * The default value of the '{@link #getStatus() <em>Status</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStatus()
	 * @generated
	 * @ordered
	 */
	protected static final TransferStatus STATUS_EDEFAULT = TransferStatus.CONFIRMED;

	/**
	 * The cached value of the '{@link #getStatus() <em>Status</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStatus()
	 * @generated
	 * @ordered
	 */
	protected TransferStatus status = STATUS_EDEFAULT;

	/**
	 * The default value of the '{@link #getNotes() <em>Notes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNotes()
	 * @generated
	 * @ordered
	 */
	protected static final String NOTES_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNotes() <em>Notes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNotes()
	 * @generated
	 * @ordered
	 */
	protected String notes = NOTES_EDEFAULT;

	/**
	 * The default value of the '{@link #isStale() <em>Stale</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isStale()
	 * @generated
	 * @ordered
	 */
	protected static final boolean STALE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isStale() <em>Stale</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isStale()
	 * @generated
	 * @ordered
	 */
	protected boolean stale = STALE_EDEFAULT;

	/**
	 * The default value of the '{@link #getPricingBasis() <em>Pricing Basis</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPricingBasis()
	 * @generated
	 * @ordered
	 */
	protected static final String PRICING_BASIS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPricingBasis() <em>Pricing Basis</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPricingBasis()
	 * @generated
	 * @ordered
	 */
	protected String pricingBasis = PRICING_BASIS_EDEFAULT;

	/**
	 * This is true if the Pricing Basis attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean pricingBasisESet;

	/**
	 * The cached value of the '{@link #getFromBU() <em>From BU</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFromBU()
	 * @generated
	 * @ordered
	 */
	protected BusinessUnit fromBU;

	/**
	 * This is true if the From BU reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean fromBUESet;

	/**
	 * The cached value of the '{@link #getToBU() <em>To BU</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getToBU()
	 * @generated
	 * @ordered
	 */
	protected BusinessUnit toBU;

	/**
	 * This is true if the To BU reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean toBUESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TransferRecordImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TransfersPackage.Literals.TRANSFER_RECORD;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getUuid() {
		return uuid;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUuid(String newUuid) {
		String oldUuid = uuid;
		uuid = newUuid;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TransfersPackage.TRANSFER_RECORD__UUID, oldUuid, uuid));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TransferAgreement getTransferAgreement() {
		if (transferAgreement != null && transferAgreement.eIsProxy()) {
			InternalEObject oldTransferAgreement = (InternalEObject)transferAgreement;
			transferAgreement = (TransferAgreement)eResolveProxy(oldTransferAgreement);
			if (transferAgreement != oldTransferAgreement) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, TransfersPackage.TRANSFER_RECORD__TRANSFER_AGREEMENT, oldTransferAgreement, transferAgreement));
			}
		}
		return transferAgreement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TransferAgreement basicGetTransferAgreement() {
		return transferAgreement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTransferAgreement(TransferAgreement newTransferAgreement) {
		TransferAgreement oldTransferAgreement = transferAgreement;
		transferAgreement = newTransferAgreement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TransfersPackage.TRANSFER_RECORD__TRANSFER_AGREEMENT, oldTransferAgreement, transferAgreement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Slot<?> getLhs() {
		if (lhs != null && lhs.eIsProxy()) {
			InternalEObject oldLhs = (InternalEObject)lhs;
			lhs = (Slot<?>)eResolveProxy(oldLhs);
			if (lhs != oldLhs) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, TransfersPackage.TRANSFER_RECORD__LHS, oldLhs, lhs));
			}
		}
		return lhs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Slot<?> basicGetLhs() {
		return lhs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLhs(Slot<?> newLhs) {
		Slot<?> oldLhs = lhs;
		lhs = newLhs;
		boolean oldLhsESet = lhsESet;
		lhsESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TransfersPackage.TRANSFER_RECORD__LHS, oldLhs, lhs, !oldLhsESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetLhs() {
		Slot<?> oldLhs = lhs;
		boolean oldLhsESet = lhsESet;
		lhs = null;
		lhsESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, TransfersPackage.TRANSFER_RECORD__LHS, oldLhs, null, oldLhsESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetLhs() {
		return lhsESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TransferRecord getRhs() {
		if (rhs != null && rhs.eIsProxy()) {
			InternalEObject oldRhs = (InternalEObject)rhs;
			rhs = (TransferRecord)eResolveProxy(oldRhs);
			if (rhs != oldRhs) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, TransfersPackage.TRANSFER_RECORD__RHS, oldRhs, rhs));
			}
		}
		return rhs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TransferRecord basicGetRhs() {
		return rhs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRhs(TransferRecord newRhs) {
		TransferRecord oldRhs = rhs;
		rhs = newRhs;
		boolean oldRhsESet = rhsESet;
		rhsESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TransfersPackage.TRANSFER_RECORD__RHS, oldRhs, rhs, !oldRhsESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetRhs() {
		TransferRecord oldRhs = rhs;
		boolean oldRhsESet = rhsESet;
		rhs = null;
		rhsESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, TransfersPackage.TRANSFER_RECORD__RHS, oldRhs, null, oldRhsESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetRhs() {
		return rhsESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDate getCargoReleaseDate() {
		return cargoReleaseDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCargoReleaseDate(LocalDate newCargoReleaseDate) {
		LocalDate oldCargoReleaseDate = cargoReleaseDate;
		cargoReleaseDate = newCargoReleaseDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TransfersPackage.TRANSFER_RECORD__CARGO_RELEASE_DATE, oldCargoReleaseDate, cargoReleaseDate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getPriceExpression() {
		return priceExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPriceExpression(String newPriceExpression) {
		String oldPriceExpression = priceExpression;
		priceExpression = newPriceExpression;
		boolean oldPriceExpressionESet = priceExpressionESet;
		priceExpressionESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TransfersPackage.TRANSFER_RECORD__PRICE_EXPRESSION, oldPriceExpression, priceExpression, !oldPriceExpressionESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetPriceExpression() {
		String oldPriceExpression = priceExpression;
		boolean oldPriceExpressionESet = priceExpressionESet;
		priceExpression = PRICE_EXPRESSION_EDEFAULT;
		priceExpressionESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, TransfersPackage.TRANSFER_RECORD__PRICE_EXPRESSION, oldPriceExpression, PRICE_EXPRESSION_EDEFAULT, oldPriceExpressionESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetPriceExpression() {
		return priceExpressionESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDate getPricingDate() {
		return pricingDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPricingDate(LocalDate newPricingDate) {
		LocalDate oldPricingDate = pricingDate;
		pricingDate = newPricingDate;
		boolean oldPricingDateESet = pricingDateESet;
		pricingDateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TransfersPackage.TRANSFER_RECORD__PRICING_DATE, oldPricingDate, pricingDate, !oldPricingDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetPricingDate() {
		LocalDate oldPricingDate = pricingDate;
		boolean oldPricingDateESet = pricingDateESet;
		pricingDate = PRICING_DATE_EDEFAULT;
		pricingDateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, TransfersPackage.TRANSFER_RECORD__PRICING_DATE, oldPricingDate, PRICING_DATE_EDEFAULT, oldPricingDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetPricingDate() {
		return pricingDateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TransferIncoterm getIncoterm() {
		return incoterm;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setIncoterm(TransferIncoterm newIncoterm) {
		TransferIncoterm oldIncoterm = incoterm;
		incoterm = newIncoterm == null ? INCOTERM_EDEFAULT : newIncoterm;
		boolean oldIncotermESet = incotermESet;
		incotermESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TransfersPackage.TRANSFER_RECORD__INCOTERM, oldIncoterm, incoterm, !oldIncotermESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetIncoterm() {
		TransferIncoterm oldIncoterm = incoterm;
		boolean oldIncotermESet = incotermESet;
		incoterm = INCOTERM_EDEFAULT;
		incotermESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, TransfersPackage.TRANSFER_RECORD__INCOTERM, oldIncoterm, INCOTERM_EDEFAULT, oldIncotermESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetIncoterm() {
		return incotermESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TransferStatus getStatus() {
		return status;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setStatus(TransferStatus newStatus) {
		TransferStatus oldStatus = status;
		status = newStatus == null ? STATUS_EDEFAULT : newStatus;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TransfersPackage.TRANSFER_RECORD__STATUS, oldStatus, status));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getNotes() {
		return notes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setNotes(String newNotes) {
		String oldNotes = notes;
		notes = newNotes;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TransfersPackage.TRANSFER_RECORD__NOTES, oldNotes, notes));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isStale() {
		return stale;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setStale(boolean newStale) {
		boolean oldStale = stale;
		stale = newStale;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TransfersPackage.TRANSFER_RECORD__STALE, oldStale, stale));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getPricingBasis() {
		return pricingBasis;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPricingBasis(String newPricingBasis) {
		String oldPricingBasis = pricingBasis;
		pricingBasis = newPricingBasis;
		boolean oldPricingBasisESet = pricingBasisESet;
		pricingBasisESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TransfersPackage.TRANSFER_RECORD__PRICING_BASIS, oldPricingBasis, pricingBasis, !oldPricingBasisESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetPricingBasis() {
		String oldPricingBasis = pricingBasis;
		boolean oldPricingBasisESet = pricingBasisESet;
		pricingBasis = PRICING_BASIS_EDEFAULT;
		pricingBasisESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, TransfersPackage.TRANSFER_RECORD__PRICING_BASIS, oldPricingBasis, PRICING_BASIS_EDEFAULT, oldPricingBasisESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetPricingBasis() {
		return pricingBasisESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BusinessUnit getFromBU() {
		if (fromBU != null && fromBU.eIsProxy()) {
			InternalEObject oldFromBU = (InternalEObject)fromBU;
			fromBU = (BusinessUnit)eResolveProxy(oldFromBU);
			if (fromBU != oldFromBU) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, TransfersPackage.TRANSFER_RECORD__FROM_BU, oldFromBU, fromBU));
			}
		}
		return fromBU;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BusinessUnit basicGetFromBU() {
		return fromBU;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFromBU(BusinessUnit newFromBU) {
		BusinessUnit oldFromBU = fromBU;
		fromBU = newFromBU;
		boolean oldFromBUESet = fromBUESet;
		fromBUESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TransfersPackage.TRANSFER_RECORD__FROM_BU, oldFromBU, fromBU, !oldFromBUESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetFromBU() {
		BusinessUnit oldFromBU = fromBU;
		boolean oldFromBUESet = fromBUESet;
		fromBU = null;
		fromBUESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, TransfersPackage.TRANSFER_RECORD__FROM_BU, oldFromBU, null, oldFromBUESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetFromBU() {
		return fromBUESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BusinessUnit getToBU() {
		if (toBU != null && toBU.eIsProxy()) {
			InternalEObject oldToBU = (InternalEObject)toBU;
			toBU = (BusinessUnit)eResolveProxy(oldToBU);
			if (toBU != oldToBU) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, TransfersPackage.TRANSFER_RECORD__TO_BU, oldToBU, toBU));
			}
		}
		return toBU;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BusinessUnit basicGetToBU() {
		return toBU;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setToBU(BusinessUnit newToBU) {
		BusinessUnit oldToBU = toBU;
		toBU = newToBU;
		boolean oldToBUESet = toBUESet;
		toBUESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TransfersPackage.TRANSFER_RECORD__TO_BU, oldToBU, toBU, !oldToBUESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetToBU() {
		BusinessUnit oldToBU = toBU;
		boolean oldToBUESet = toBUESet;
		toBU = null;
		toBUESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, TransfersPackage.TRANSFER_RECORD__TO_BU, oldToBU, null, oldToBUESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetToBU() {
		return toBUESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public BaseLegalEntity getFromEntity() {
		if (getTransferAgreement() != null) {
			return getTransferAgreement().getFromEntity();
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public BaseLegalEntity getToEntity() {
		if (getTransferAgreement() != null) {
			return getTransferAgreement().getToEntity();
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public CompanyStatus getCompanyStatus() {
		if (getTransferAgreement() != null) {
			return getTransferAgreement().getCompanyStatus();
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getRecordOrDelegatePriceExpression() {
		return (String) getUnsetValueOrDelegate(TransfersPackage.Literals.TRANSFER_RECORD__PRICE_EXPRESSION).getValue(this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public TransferIncoterm getRecordOrDelegateIncoterm() {
		return (TransferIncoterm) getUnsetValueOrDelegate(TransfersPackage.Literals.TRANSFER_RECORD__INCOTERM).getValue(this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getRecordOrDelegatePricingBasis() {
		return (String) getUnsetValueOrDelegate(TransfersPackage.Literals.TRANSFER_RECORD__PRICING_BASIS).getValue(this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public BusinessUnit getRecordOrDelegateFromBU() {
		return (BusinessUnit) getUnsetValueOrDelegate(TransfersPackage.Literals.TRANSFER_RECORD__FROM_BU).getValue(this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public BusinessUnit getRecordOrDelegateToBU() {
		return (BusinessUnit) getUnsetValueOrDelegate(TransfersPackage.Literals.TRANSFER_RECORD__TO_BU).getValue(this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case TransfersPackage.TRANSFER_RECORD__UUID:
				return getUuid();
			case TransfersPackage.TRANSFER_RECORD__TRANSFER_AGREEMENT:
				if (resolve) return getTransferAgreement();
				return basicGetTransferAgreement();
			case TransfersPackage.TRANSFER_RECORD__LHS:
				if (resolve) return getLhs();
				return basicGetLhs();
			case TransfersPackage.TRANSFER_RECORD__RHS:
				if (resolve) return getRhs();
				return basicGetRhs();
			case TransfersPackage.TRANSFER_RECORD__CARGO_RELEASE_DATE:
				return getCargoReleaseDate();
			case TransfersPackage.TRANSFER_RECORD__PRICE_EXPRESSION:
				return getPriceExpression();
			case TransfersPackage.TRANSFER_RECORD__PRICING_DATE:
				return getPricingDate();
			case TransfersPackage.TRANSFER_RECORD__INCOTERM:
				return getIncoterm();
			case TransfersPackage.TRANSFER_RECORD__STATUS:
				return getStatus();
			case TransfersPackage.TRANSFER_RECORD__NOTES:
				return getNotes();
			case TransfersPackage.TRANSFER_RECORD__STALE:
				return isStale();
			case TransfersPackage.TRANSFER_RECORD__PRICING_BASIS:
				return getPricingBasis();
			case TransfersPackage.TRANSFER_RECORD__FROM_BU:
				if (resolve) return getFromBU();
				return basicGetFromBU();
			case TransfersPackage.TRANSFER_RECORD__TO_BU:
				if (resolve) return getToBU();
				return basicGetToBU();
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
			case TransfersPackage.TRANSFER_RECORD__UUID:
				setUuid((String)newValue);
				return;
			case TransfersPackage.TRANSFER_RECORD__TRANSFER_AGREEMENT:
				setTransferAgreement((TransferAgreement)newValue);
				return;
			case TransfersPackage.TRANSFER_RECORD__LHS:
				setLhs((Slot<?>)newValue);
				return;
			case TransfersPackage.TRANSFER_RECORD__RHS:
				setRhs((TransferRecord)newValue);
				return;
			case TransfersPackage.TRANSFER_RECORD__CARGO_RELEASE_DATE:
				setCargoReleaseDate((LocalDate)newValue);
				return;
			case TransfersPackage.TRANSFER_RECORD__PRICE_EXPRESSION:
				setPriceExpression((String)newValue);
				return;
			case TransfersPackage.TRANSFER_RECORD__PRICING_DATE:
				setPricingDate((LocalDate)newValue);
				return;
			case TransfersPackage.TRANSFER_RECORD__INCOTERM:
				setIncoterm((TransferIncoterm)newValue);
				return;
			case TransfersPackage.TRANSFER_RECORD__STATUS:
				setStatus((TransferStatus)newValue);
				return;
			case TransfersPackage.TRANSFER_RECORD__NOTES:
				setNotes((String)newValue);
				return;
			case TransfersPackage.TRANSFER_RECORD__STALE:
				setStale((Boolean)newValue);
				return;
			case TransfersPackage.TRANSFER_RECORD__PRICING_BASIS:
				setPricingBasis((String)newValue);
				return;
			case TransfersPackage.TRANSFER_RECORD__FROM_BU:
				setFromBU((BusinessUnit)newValue);
				return;
			case TransfersPackage.TRANSFER_RECORD__TO_BU:
				setToBU((BusinessUnit)newValue);
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
			case TransfersPackage.TRANSFER_RECORD__UUID:
				setUuid(UUID_EDEFAULT);
				return;
			case TransfersPackage.TRANSFER_RECORD__TRANSFER_AGREEMENT:
				setTransferAgreement((TransferAgreement)null);
				return;
			case TransfersPackage.TRANSFER_RECORD__LHS:
				unsetLhs();
				return;
			case TransfersPackage.TRANSFER_RECORD__RHS:
				unsetRhs();
				return;
			case TransfersPackage.TRANSFER_RECORD__CARGO_RELEASE_DATE:
				setCargoReleaseDate(CARGO_RELEASE_DATE_EDEFAULT);
				return;
			case TransfersPackage.TRANSFER_RECORD__PRICE_EXPRESSION:
				unsetPriceExpression();
				return;
			case TransfersPackage.TRANSFER_RECORD__PRICING_DATE:
				unsetPricingDate();
				return;
			case TransfersPackage.TRANSFER_RECORD__INCOTERM:
				unsetIncoterm();
				return;
			case TransfersPackage.TRANSFER_RECORD__STATUS:
				setStatus(STATUS_EDEFAULT);
				return;
			case TransfersPackage.TRANSFER_RECORD__NOTES:
				setNotes(NOTES_EDEFAULT);
				return;
			case TransfersPackage.TRANSFER_RECORD__STALE:
				setStale(STALE_EDEFAULT);
				return;
			case TransfersPackage.TRANSFER_RECORD__PRICING_BASIS:
				unsetPricingBasis();
				return;
			case TransfersPackage.TRANSFER_RECORD__FROM_BU:
				unsetFromBU();
				return;
			case TransfersPackage.TRANSFER_RECORD__TO_BU:
				unsetToBU();
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
			case TransfersPackage.TRANSFER_RECORD__UUID:
				return UUID_EDEFAULT == null ? uuid != null : !UUID_EDEFAULT.equals(uuid);
			case TransfersPackage.TRANSFER_RECORD__TRANSFER_AGREEMENT:
				return transferAgreement != null;
			case TransfersPackage.TRANSFER_RECORD__LHS:
				return isSetLhs();
			case TransfersPackage.TRANSFER_RECORD__RHS:
				return isSetRhs();
			case TransfersPackage.TRANSFER_RECORD__CARGO_RELEASE_DATE:
				return CARGO_RELEASE_DATE_EDEFAULT == null ? cargoReleaseDate != null : !CARGO_RELEASE_DATE_EDEFAULT.equals(cargoReleaseDate);
			case TransfersPackage.TRANSFER_RECORD__PRICE_EXPRESSION:
				return isSetPriceExpression();
			case TransfersPackage.TRANSFER_RECORD__PRICING_DATE:
				return isSetPricingDate();
			case TransfersPackage.TRANSFER_RECORD__INCOTERM:
				return isSetIncoterm();
			case TransfersPackage.TRANSFER_RECORD__STATUS:
				return status != STATUS_EDEFAULT;
			case TransfersPackage.TRANSFER_RECORD__NOTES:
				return NOTES_EDEFAULT == null ? notes != null : !NOTES_EDEFAULT.equals(notes);
			case TransfersPackage.TRANSFER_RECORD__STALE:
				return stale != STALE_EDEFAULT;
			case TransfersPackage.TRANSFER_RECORD__PRICING_BASIS:
				return isSetPricingBasis();
			case TransfersPackage.TRANSFER_RECORD__FROM_BU:
				return isSetFromBU();
			case TransfersPackage.TRANSFER_RECORD__TO_BU:
				return isSetToBU();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == UUIDObject.class) {
			switch (derivedFeatureID) {
				case TransfersPackage.TRANSFER_RECORD__UUID: return MMXCorePackage.UUID_OBJECT__UUID;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == UUIDObject.class) {
			switch (baseFeatureID) {
				case MMXCorePackage.UUID_OBJECT__UUID: return TransfersPackage.TRANSFER_RECORD__UUID;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case TransfersPackage.TRANSFER_RECORD___GET_FROM_ENTITY:
				return getFromEntity();
			case TransfersPackage.TRANSFER_RECORD___GET_TO_ENTITY:
				return getToEntity();
			case TransfersPackage.TRANSFER_RECORD___GET_COMPANY_STATUS:
				return getCompanyStatus();
			case TransfersPackage.TRANSFER_RECORD___GET_RECORD_OR_DELEGATE_PRICE_EXPRESSION:
				return getRecordOrDelegatePriceExpression();
			case TransfersPackage.TRANSFER_RECORD___GET_RECORD_OR_DELEGATE_INCOTERM:
				return getRecordOrDelegateIncoterm();
			case TransfersPackage.TRANSFER_RECORD___GET_RECORD_OR_DELEGATE_PRICING_BASIS:
				return getRecordOrDelegatePricingBasis();
			case TransfersPackage.TRANSFER_RECORD___GET_RECORD_OR_DELEGATE_FROM_BU:
				return getRecordOrDelegateFromBU();
			case TransfersPackage.TRANSFER_RECORD___GET_RECORD_OR_DELEGATE_TO_BU:
				return getRecordOrDelegateToBU();
		}
		return super.eInvoke(operationID, arguments);
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
		result.append(" (uuid: ");
		result.append(uuid);
		result.append(", cargoReleaseDate: ");
		result.append(cargoReleaseDate);
		result.append(", priceExpression: ");
		if (priceExpressionESet) result.append(priceExpression); else result.append("<unset>");
		result.append(", pricingDate: ");
		if (pricingDateESet) result.append(pricingDate); else result.append("<unset>");
		result.append(", incoterm: ");
		if (incotermESet) result.append(incoterm); else result.append("<unset>");
		result.append(", status: ");
		result.append(status);
		result.append(", notes: ");
		result.append(notes);
		result.append(", stale: ");
		result.append(stale);
		result.append(", pricingBasis: ");
		if (pricingBasisESet) result.append(pricingBasis); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}
	
	@Override
	public DelegateInformation getUnsetValueOrDelegate(EStructuralFeature feature) {
		final TransfersPackage transferPackage = TransfersPackage.eINSTANCE;
		if (transferPackage.getTransferRecord_PriceExpression() == feature) {
			return new DelegateInformation(transferPackage.getTransferRecord_TransferAgreement(), transferPackage.getTransferAgreement_PriceExpression(),"");
		} else if (transferPackage.getTransferRecord_Incoterm() == feature) {
			return new DelegateInformation(transferPackage.getTransferRecord_TransferAgreement(), transferPackage.getTransferAgreement_Incoterm(), TransferIncoterm.BOTH);
		} else if (transferPackage.getTransferRecord_PricingBasis() == feature) {
			return new DelegateInformation(transferPackage.getTransferRecord_TransferAgreement(), transferPackage.getTransferAgreement_PricingBasis(),"");
		} else if (transferPackage.getTransferRecord_FromBU() == feature) {
			return new DelegateInformation(null, null, null) {

				@Override
				public boolean delegatesTo(final Object changedFeature) {
					return (changedFeature == TransfersPackage.Literals.TRANSFER_RECORD__FROM_BU);
				}

				@Override
				public Object getValue(final EObject object) {
					if (getTransferAgreement() != null && getTransferAgreement().eIsSet(transferPackage.getTransferAgreement_FromBU())) {
						return getTransferAgreement().getFromBU();
					} else if (getFromEntity() != null) {
						final BaseLegalEntity entity = getFromEntity();
						if (entity.getBusinessUnits() != null && !entity.getBusinessUnits().isEmpty()) {
							for (final var bu : entity.getBusinessUnits()) {
								if (bu.isDefault()) {
									return bu;
								}
							}
							return entity.getBusinessUnits().get(0);
						}
					}
					return ECollections.emptyEList();
				}
			};
		} else if (transferPackage.getTransferRecord_ToBU() == feature) {
			return new DelegateInformation(null, null, null) {

				@Override
				public boolean delegatesTo(final Object changedFeature) {
					return (changedFeature == TransfersPackage.Literals.TRANSFER_RECORD__TO_BU);
				}

				@Override
				public Object getValue(final EObject object) {
					if (getTransferAgreement() != null && getTransferAgreement().eIsSet(transferPackage.getTransferAgreement_ToBU())) {
						return getTransferAgreement().getToBU();
					} if (getToEntity() != null) {
						final BaseLegalEntity entity = getToEntity();
						if (entity.getBusinessUnits() != null && !entity.getBusinessUnits().isEmpty()) {
							for (final var bu : entity.getBusinessUnits()) {
								if (bu.isDefault()) {
									return bu;
								}
							}
							return entity.getBusinessUnits().get(0);
						}
					}
					return ECollections.emptyEList();
				}
			};
		}
		return super.getUnsetValueOrDelegate(feature);
	}

} //TransferRecordImpl
