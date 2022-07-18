/**
 */
package com.mmxlabs.models.lng.transfers.impl;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;

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

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

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
	protected EObject lhs;

	/**
	 * The cached value of the '{@link #getRhs() <em>Rhs</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRhs()
	 * @generated
	 * @ordered
	 */
	protected EObject rhs;

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
	public EObject getLhs() {
		if (lhs != null && lhs.eIsProxy()) {
			InternalEObject oldLhs = (InternalEObject)lhs;
			lhs = eResolveProxy(oldLhs);
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
	public EObject basicGetLhs() {
		return lhs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLhs(EObject newLhs) {
		EObject oldLhs = lhs;
		lhs = newLhs;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TransfersPackage.TRANSFER_RECORD__LHS, oldLhs, lhs));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject getRhs() {
		if (rhs != null && rhs.eIsProxy()) {
			InternalEObject oldRhs = (InternalEObject)rhs;
			rhs = eResolveProxy(oldRhs);
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
	public EObject basicGetRhs() {
		return rhs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRhs(EObject newRhs) {
		EObject oldRhs = rhs;
		rhs = newRhs;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TransfersPackage.TRANSFER_RECORD__RHS, oldRhs, rhs));
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
			getTransferAgreement().getCompanyStatus();
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
				setLhs((EObject)newValue);
				return;
			case TransfersPackage.TRANSFER_RECORD__RHS:
				setRhs((EObject)newValue);
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
				setLhs((EObject)null);
				return;
			case TransfersPackage.TRANSFER_RECORD__RHS:
				setRhs((EObject)null);
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
				return lhs != null;
			case TransfersPackage.TRANSFER_RECORD__RHS:
				return rhs != null;
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
		}
		return super.getUnsetValueOrDelegate(feature);
	}

} //TransferRecordImpl
