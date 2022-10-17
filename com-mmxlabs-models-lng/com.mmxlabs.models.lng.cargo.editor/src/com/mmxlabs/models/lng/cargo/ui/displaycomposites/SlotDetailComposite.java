/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.SharedScrolledComposite;

import com.google.common.collect.Sets;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.ui.displaycomposites.ExpandableSet.ExpansionListener;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.util.EditorControlFactory;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeLayoutProvider;

public class SlotDetailComposite extends DefaultDetailComposite implements IDisplayComposite, ExpansionListener {

	private static final CargoPackage CargoFeatures = CargoPackage.eINSTANCE;
	private static final EStructuralFeature WindowStart = CargoFeatures.getSlot_WindowStart();
	private static final EStructuralFeature WindowStartTime = CargoFeatures.getSlot_WindowStartTime();
	private static final EStructuralFeature WindowSize = CargoFeatures.getSlot_WindowSize();
	private static final EStructuralFeature WindowSizeUnits = CargoFeatures.getSlot_WindowSizeUnits();
	private static final EStructuralFeature WindowFlex = CargoFeatures.getSlot_WindowFlex();
	private static final EStructuralFeature WindowFlexUnits = CargoFeatures.getSlot_WindowFlexUnits();
	private static final EStructuralFeature WindowCounterParty = CargoFeatures.getSlot_WindowCounterParty();
	private static final EStructuralFeature Contract = CargoFeatures.getSlot_Contract();
	private static final EStructuralFeature PriceExpression = CargoFeatures.getSlot_PriceExpression();
	private static final EClass SlotContractParams = CommercialPackage.eINSTANCE.getSlotContractParams();

	Composite contentComposite;
	private final Map<ETypedElement, IInlineEditor> feature2Editor;
	final ExpandableSet esPricing;
	private final ExpandableSet esWindow;
	private final ExpandableSet esTerms;
	private final ExpandableSet esOther;
	private ArrayList<ETypedElement[]> nameFeatures;
	private ArrayList<ETypedElement[]> pricingFeatures;
	private HashSet<ETypedElement> pricingTitleFeatures;
	private ArrayList<ETypedElement[]> mainFeatures;
	private ArrayList<ETypedElement[]> shippedWindowFeatures;
	private HashSet<ETypedElement> shippedWindowTitleFeatures;
	private ArrayList<ETypedElement[]> unshippedWindowFeatures;
	private HashSet<ETypedElement> unshippedWindowTitleFeatures;
	
	private ArrayList<ETypedElement[]> loadTermsFeatures;
	private ArrayList<ETypedElement[]> dischargeTermsFeatures;
	private ArrayList<ETypedElement[]> noteFeatures;
	private HashSet<ETypedElement> allFeatures;

	private ArrayList<ETypedElement[]> missedFeatures;
	private ArrayList<ETypedElement> missedFeaturesList;

	{
		allFeatures = new HashSet<>();

		nameFeatures = new ArrayList<>();
		nameFeatures
				.add(new EStructuralFeature[] { MMXCorePackage.eINSTANCE.getNamedObject_Name(), CargoFeatures.getSlot_Optional(), CargoFeatures.getSlot_Locked(), CargoFeatures.getSlot_Cancelled(), CargoFeatures.getDischargeSlot_HeelCarry() });
		nameFeatures.add(new EStructuralFeature[] { CargoFeatures.getLoadSlot_DesPurchaseDealType(), CargoFeatures.getDischargeSlot_FobSaleDealType() });
		allFeatures.addAll(getAllFeatures(nameFeatures));

		mainFeatures = new ArrayList<>();
		mainFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_Port(), CargoFeatures.getSlot_Entity() });
		mainFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_MinQuantity(), CargoFeatures.getSlot_MaxQuantity(), CargoFeatures.getSlot_VolumeLimitsUnit(),
				CargoFeatures.getSlot_OperationalTolerance(), CargoFeatures.getSlot_FullCargoLot(), CargoFeatures.getLoadSlot_VolumeCounterParty() });
		mainFeatures.add(new EStructuralFeature[] { CargoPackage.Literals.SLOT__COUNTERPARTY, CargoPackage.Literals.SLOT__CN });
		allFeatures.addAll(getAllFeatures(mainFeatures));

		pricingFeatures = new ArrayList<>();
		pricingFeatures.add(new EStructuralFeature[] { Contract, CargoPackage.Literals.SPOT_SLOT__MARKET });
		pricingFeatures.add(new EStructuralFeature[] { PriceExpression });
		pricingFeatures.add(new EStructuralFeature[] { CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS__OVERRIDE_CONTRACT });
		pricingFeatures.add(new EStructuralFeature[] { CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS__LOW_EXPRESSION,  CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS__HIGH_EXPRESSION });
		pricingFeatures.add(new EStructuralFeature[] { CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS__THRESHOLD,  CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS__VOLUME_LIMITS_UNIT });
		pricingFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_PricingEvent(), CargoFeatures.getSlot_PricingDate() });
		pricingFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_MiscCosts(), CargoFeatures.getSlot_CancellationExpression() });
		pricingTitleFeatures = Sets.newHashSet(Contract, PriceExpression);
		allFeatures.addAll(getAllFeatures(pricingFeatures));

		shippedWindowFeatures = new ArrayList<>();
		shippedWindowFeatures.add(new EStructuralFeature[] { WindowStart, WindowStartTime });
		shippedWindowFeatures.add(new EStructuralFeature[] { WindowSize, WindowSizeUnits,  WindowCounterParty });
		shippedWindowFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_Duration() });
		shippedWindowFeatures.add(new EStructuralFeature[] { WindowFlex, WindowFlexUnits });
		shippedWindowFeatures.add(new EStructuralFeature[] {});
		shippedWindowTitleFeatures = Sets.newHashSet(WindowStart, WindowStartTime, WindowSize, WindowSizeUnits, WindowCounterParty);
		allFeatures.addAll(getAllFeatures(shippedWindowFeatures));

		unshippedWindowFeatures = new ArrayList<>();
		unshippedWindowFeatures.add(new EStructuralFeature[] { WindowStart, WindowStartTime });
		unshippedWindowFeatures.add(new EStructuralFeature[] { WindowSize, WindowSizeUnits });
		unshippedWindowFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_Duration() });
		unshippedWindowFeatures.add(new EStructuralFeature[] { WindowFlex, WindowFlexUnits });
		unshippedWindowFeatures.add(new EStructuralFeature[] {});
		unshippedWindowTitleFeatures = Sets.newHashSet(WindowStart, WindowStartTime, WindowSize, WindowSizeUnits);
		allFeatures.addAll(getAllFeatures(unshippedWindowFeatures));
		
		loadTermsFeatures = new ArrayList<ETypedElement[]>();
		loadTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getLoadSlot_SchedulePurge() });
		loadTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getLoadSlot_ArriveCold(), CargoFeatures.getLoadSlot_CargoCV() });
		loadTermsFeatures
				.add(new EStructuralFeature[] { CargoFeatures.getSlot_RestrictedPortsOverride(), CargoFeatures.getSlot_RestrictedPorts(), CargoFeatures.getSlot_RestrictedPortsArePermissive() });
		loadTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_RestrictedContractsOverride(), CargoFeatures.getSlot_RestrictedContracts(),
				CargoFeatures.getSlot_RestrictedContractsArePermissive() });
		loadTermsFeatures
				.add(new EStructuralFeature[] { CargoFeatures.getSlot_RestrictedVesselsOverride(), CargoFeatures.getSlot_RestrictedVessels(), CargoFeatures.getSlot_RestrictedVesselsArePermissive() });
		loadTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_RestrictedSlots(), CargoFeatures.getSlot_RestrictedSlotsArePermissive() });

		loadTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_NominatedVessel() });
		loadTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_ShippingDaysRestriction() });
		loadTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getLoadSlot_SalesDeliveryType() });
		allFeatures.addAll(getAllFeatures(loadTermsFeatures));

		dischargeTermsFeatures = new ArrayList<ETypedElement[]>();
		dischargeTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getDischargeSlot_PurchaseDeliveryType() });
		dischargeTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getDischargeSlot_MinCvValue(), CargoFeatures.getDischargeSlot_MaxCvValue() });
		dischargeTermsFeatures
				.add(new EStructuralFeature[] { CargoFeatures.getSlot_RestrictedPorts(), CargoFeatures.getSlot_RestrictedPortsArePermissive(), CargoFeatures.getSlot_RestrictedPortsOverride() });
		dischargeTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_RestrictedContracts(), CargoFeatures.getSlot_RestrictedContractsArePermissive(),
				CargoFeatures.getSlot_RestrictedContractsOverride() });
		dischargeTermsFeatures
				.add(new EStructuralFeature[] { CargoFeatures.getSlot_RestrictedVessels(), CargoFeatures.getSlot_RestrictedVesselsArePermissive(), CargoFeatures.getSlot_RestrictedVesselsOverride() });
		dischargeTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_RestrictedSlots(), CargoFeatures.getSlot_RestrictedSlotsArePermissive() });
		dischargeTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_NominatedVessel() });
		dischargeTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_ShippingDaysRestriction() });
		allFeatures.addAll(getAllFeatures(dischargeTermsFeatures));

		noteFeatures = new ArrayList<>();
		noteFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_Notes() });
		allFeatures.addAll(getAllFeatures(noteFeatures));

		missedFeaturesList = new ArrayList<>();
		missedFeatures = new ArrayList<>();
	}

	public SlotDetailComposite(final Composite parent, final int style, final FormToolkit toolkit) {
		super(parent, style, toolkit);
		feature2Editor = new HashMap<>();

		esPricing = new ExpandableSet("Pricing", this) {

			@Override
			protected void updateTextClient(final EObject eo) {

				final MMXObject mmxEo = (MMXObject) eo;
				final Contract c = (Contract) mmxEo.eGet(Contract);
				final String pe = (String) mmxEo.eGet(PriceExpression);
				String text = "";
				if (c != null) {
					text += c.getName() != null ? c.getName() : "";
					text += pe != null && pe.length() > 0 ? ", " : "";
				}
				text += pe != null ? pe : "";
				textClient.setText(text);
				textClient.update();
			}
		};

		esWindow = new ExpandableSet("Window", this) {

			@Override
			protected void updateTextClient(final EObject eo) {

				final MMXObject mmxEo = (MMXObject) eo;
				final LocalDate d = (LocalDate) mmxEo.eGet(WindowStart);
				if (d != null) {
					final int time = (Integer) mmxEo.eGetWithDefault(WindowStartTime);
					final int wsize = (Integer) mmxEo.eGetWithDefault(WindowSize);
					final TimePeriod ePeriod = (TimePeriod) mmxEo.eGetWithDefault(WindowSizeUnits);
					if (mmxEo instanceof Slot) {
						final Slot<?> slot = (Slot<?>) mmxEo;
						final ZonedDateTime ed = getDisplayedWindowEndTime(slot);
						final String text = formatDate(d, time) + " - " + formatDate(ed.toLocalDate(), ed.toLocalDateTime().getHour());
						textClient.setText(text);
					} else {
						final String text = formatDate(d, time) + " - " + wsize + " " + getUnits(ePeriod);
						textClient.setText(text);
					}
				}
			}

			private ZonedDateTime getDisplayedWindowEndTime(Slot<?> slot) {
				final ZonedDateTime start = slot.getSchedulingTimeWindow().getStart();
				final TimePeriod p = slot.getWindowSizeUnits();
				int windowSize = slot.getWindowSize();
				ZonedDateTime end = start;

				if (windowSize > 0) {
					switch (p) {
					case DAYS:
						end = end.plusDays(windowSize).minusHours(1);
						break;
					case HOURS:
						end = end.plusHours(windowSize) ;
						break;
					case MONTHS:
						end = end.plusMonths(windowSize).minusHours(1);
						break;
					default:
						break;
					}
				}
				
				return end;
			}
			
			private String getUnits(final TimePeriod ePeriod) {
				switch (ePeriod) {
				case HOURS:
					return "Hours";
				case DAYS:
					return "Days";
				case MONTHS:
					return "Months";
				default:
					return ePeriod.getName();
				}
			}
		};
		esWindow.setToolTipText("Permitted arrival date range (inclusive start and end dates)");

		esTerms = new ExpandableSet("Terms", this);

		esOther = new ExpandableSet("Other", this);
	}

	@Override
	protected IDisplayCompositeLayoutProvider createLayoutProvider() {
		return new DefaultDisplayCompositeLayoutProvider() {

			@Override
			public Layout createDetailLayout(final MMXRootObject root, final EObject value) {
				return new FillLayout();
			}

			@Override
			public Object createEditorLayoutData(final MMXRootObject root, final EObject value, final IInlineEditor editor, final Control control) {

				// Special case for min/max volumes - ensure text box has enough width for around 7 digits.
				// Note: Should really render the font to get width - this is ok on my system, but other systems (default font & size, resolution, dpi etc) could make this wrong
				final var feature = editor.getFeature();
				if (feature == CargoPackage.Literals.SLOT__MAX_QUANTITY || feature == CargoPackage.Literals.SLOT__MIN_QUANTITY || feature == CargoPackage.Literals.SLOT__OPERATIONAL_TOLERANCE) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					// 64 - magic constant from MultiDetailDialog
					// gd.widthHint = 80;

					// FIXME: Hack pending proper APi to manipulate labels
					if (feature == CargoPackage.Literals.SLOT__MIN_QUANTITY) {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("Volume");
						}
						editor.setLabel(null);
					} else {
						editor.setLabel(null);
					}
					return gd;
				}
				if (feature == CargoPackage.Literals.SLOT__VOLUME_LIMITS_UNIT) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					// 64 - magic constant from MultiDetailDialog
					gd.widthHint = 100;
					editor.setLabel(null);
					return gd;
				}
				if (feature == CargoPackage.Literals.SLOT__WINDOW_SIZE || feature == CargoPackage.Literals.SLOT__WINDOW_SIZE_UNITS) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					// 64 - magic constant from MultiDetailDialog
					gd.widthHint = 100;

					// FIXME: Hack pending proper APi to manipulate labels
					if (feature == CargoPackage.Literals.SLOT__WINDOW_SIZE) {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("Window");
						}
						editor.setLabel(null);
					} else {
						editor.setLabel(null);
					}
					return gd;
				}
				if (feature == CargoPackage.Literals.SLOT__WINDOW_FLEX || feature == CargoPackage.Literals.SLOT__WINDOW_FLEX_UNITS) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					// 64 - magic constant from MultiDetailDialog
					gd.widthHint = 100;

					// FIXME: Hack pending proper APi to manipulate labels
					if (feature == CargoPackage.Literals.SLOT__WINDOW_FLEX) {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("Flex");
						}
						editor.setLabel(null);
					} else {
						editor.setLabel(null);
					}
					return gd;
				}
				if (feature == CargoPackage.Literals.SLOT__NOTES) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					gd.widthHint = 100;
					return gd;
				}

				if (feature == CargoPackage.Literals.SLOT__RESTRICTED_CONTRACTS || feature == CargoPackage.Literals.SLOT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE
						|| feature == CargoPackage.Literals.SLOT__RESTRICTED_CONTRACTS_OVERRIDE) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					// 64 - magic constant from MultiDetailDialog
					// gd.widthHint = 80;

					// FIXME: Hack pending proper APi to manipulate labels
					if (feature == CargoPackage.Literals.SLOT__RESTRICTED_CONTRACTS) {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("Contracts");
						}
						editor.setLabel(null);
					} else if (feature == CargoPackage.Literals.SLOT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE) {
						editor.setLabel(null);
					} else if (feature == CargoPackage.Literals.SLOT__RESTRICTED_CONTRACTS_OVERRIDE) {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("Override");
						}
						editor.setLabel(null);
					}
					return gd;
				}
				if (feature == CargoPackage.Literals.SLOT__RESTRICTED_PORTS || feature == CargoPackage.Literals.SLOT__RESTRICTED_PORTS_ARE_PERMISSIVE
						|| feature == CargoPackage.Literals.SLOT__RESTRICTED_PORTS_OVERRIDE) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					// 64 - magic constant from MultiDetailDialog
					// gd.widthHint = 80;

					// FIXME: Hack pending proper APi to manipulate labels
					if (feature == CargoPackage.Literals.SLOT__RESTRICTED_PORTS) {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("Ports");
						}
						editor.setLabel(null);
					} else if (feature == CargoPackage.Literals.SLOT__RESTRICTED_PORTS_ARE_PERMISSIVE) {
						editor.setLabel(null);
					} else if (feature == CargoPackage.Literals.SLOT__RESTRICTED_PORTS_OVERRIDE) {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("Override");
						}
						editor.setLabel(null);
					}
					return gd;
				}
				if (feature == CargoPackage.Literals.SLOT__RESTRICTED_VESSELS || feature == CargoPackage.Literals.SLOT__RESTRICTED_VESSELS_ARE_PERMISSIVE
						|| feature == CargoPackage.Literals.SLOT__RESTRICTED_VESSELS_OVERRIDE) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					// 64 - magic constant from MultiDetailDialog
					// gd.widthHint = 80;

					// FIXME: Hack pending proper APi to manipulate labels
					if (feature == CargoPackage.Literals.SLOT__RESTRICTED_VESSELS) {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("Vessels");
						}
						editor.setLabel(null);
					} else if (feature == CargoPackage.Literals.SLOT__RESTRICTED_VESSELS_ARE_PERMISSIVE) {
						editor.setLabel(null);
					} else if (feature == CargoPackage.Literals.SLOT__RESTRICTED_VESSELS_OVERRIDE) {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("Override");
						}
						editor.setLabel(null);
					}
					return gd;
				}
				if (feature == CargoPackage.Literals.SLOT__RESTRICTED_SLOTS || feature == CargoPackage.Literals.SLOT__RESTRICTED_SLOTS_ARE_PERMISSIVE) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					// 64 - magic constant from MultiDetailDialog
					// gd.widthHint = 80;

					// FIXME: Hack pending proper APi to manipulate labels
					if (feature == CargoPackage.Literals.SLOT__RESTRICTED_SLOTS) {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("Slots");
						}
						editor.setLabel(null);
					} else if (feature == CargoPackage.Literals.SLOT__RESTRICTED_SLOTS_ARE_PERMISSIVE) {
						editor.setLabel(null);
					}
					return gd;
				}
				
				if (feature == CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS__LOW_EXPRESSION || feature == CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS__HIGH_EXPRESSION) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					// 64 - magic constant from MultiDetailDialog
					// gd.widthHint = 80;

					// FIXME: Hack pending proper APi to manipulate labels
					if (feature == CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS__LOW_EXPRESSION) {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("Price tiers");
						}
						editor.setLabel(null);
					} else  {
						editor.setLabel(null);
					}
					return gd;
				}
				if (feature == CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS__THRESHOLD || feature == CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS__VOLUME_LIMITS_UNIT) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					// 64 - magic constant from MultiDetailDialog
					// gd.widthHint = 80;
					
					// FIXME: Hack pending proper APi to manipulate labels
					if (feature == CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS__THRESHOLD) {
						final Label label = editor.getLabel();
						if (label != null) {
							label.setText("Threshold");
						}
						editor.setLabel(null);
					} else  {
						editor.setLabel(null);
					}
					return gd;
				}
				
				
				return super.createEditorLayoutData(root, value, editor, control);
			}
		};
	}

	@Override
	public IInlineEditor addInlineEditor(IInlineEditor editor) {

		editor = super.addInlineEditor(editor);
		if (editor != null) {
			final var f = editor.getFeature();
			feature2Editor.put(f, editor);
			if (!allFeatures.contains(f)) {
				final Section section = getSectionFor(f);
				switch (section) {
				case MAIN:
					mainFeatures.add(new ETypedElement[] { f });
					break;
				case PRICING:
					pricingFeatures.add(new ETypedElement[] { f });
					break;
				case TERMS:
					loadTermsFeatures.add(new ETypedElement[] { f });
					dischargeTermsFeatures.add(new ETypedElement[] { f });
					break;
				case WINDOW:
					unshippedWindowFeatures.add(new ETypedElement[] { f });
					shippedWindowFeatures.add(new ETypedElement[] { f });
					break;
				case OTHER:
				default:
					missedFeaturesList.add(f);
					break;
				}

				allFeatures.add(f);
			}
		}

		return editor;
	}
	
	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final Collection<EObject> range, final EMFDataBindingContext dbc) {
		super.display(dialogContext, root, object, range, dbc);
		final MMXObject eo = (MMXObject) object;
		esPricing.init(eo);
		esWindow.init(eo);
		esTerms.init(eo);
		//esOther.init(eo);
	}

	@Override
	public void createControls(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final EMFDataBindingContext dbc) {
		
		toolkit.adapt(this);

		boolean isLoad;
		if (object instanceof LoadSlot) {
			isLoad = true;
		} else if (object instanceof DischargeSlot) {
			isLoad = false;
		} else {
			// Say what?...
			isLoad = false;
		}

		boolean isUnshipped = false;
		if (object instanceof Slot<?> slot) {
			if (slot.getCargo() != null && slot.getCargo().getCargoType() != CargoType.FLEET) {
				isUnshipped = true;
			}
			if (slot.isWindowCounterParty()) {
				//Validation will flag it as bad. Let them undo it.
				isUnshipped = false;
			}
		}
		
		contentComposite = toolkit.createComposite(this);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(contentComposite, "com.mmxlabs.lingo.doc.DataModel_lng_cargo_Slot");

		final Layout l = layoutProvider.createDetailLayout(root, object);

		final GridLayout layout = new GridLayout(2, false);
		layout.verticalSpacing = 8;
		contentComposite.setLayout(layout);

		for (final ETypedElement[] fs : nameFeatures) {
			EditorControlFactory.makeControls(dialogContext, root, object, contentComposite, fs, feature2Editor, dbc, layoutProvider, toolkit);
		}

		for (final ETypedElement[] fs : mainFeatures) {
			EditorControlFactory.makeControls(dialogContext, root, object, contentComposite, fs, feature2Editor, dbc, layoutProvider, toolkit);
		}

		createSpacer();

		final HashSet<ETypedElement> contractFeatures = new LinkedHashSet<>();
		for (final ETypedElement f : missedFeaturesList) {

			if (f instanceof EStructuralFeature feature && feature.getEContainingClass().getEAllSuperTypes().contains(SlotContractParams)) {
				contractFeatures.add(f);
			}
		}
		for (final ETypedElement f : contractFeatures) {
			pricingFeatures.add(new ETypedElement[] { f });
			missedFeaturesList.remove(f);
		}

		makeExpandable(dialogContext, root, object, dbc, esPricing, pricingFeatures, pricingTitleFeatures, true);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(esPricing.ec, "com.mmxlabs.lingo.doc.DataModel_lng_cargo_Slot_Pricing");

		createSpacer();
		if (isUnshipped) {
			makeExpandable(dialogContext, root, object, dbc, esWindow, unshippedWindowFeatures, unshippedWindowTitleFeatures, true);
		}
		else {
			makeExpandable(dialogContext, root, object, dbc, esWindow, shippedWindowFeatures, shippedWindowTitleFeatures, true);
		}
		PlatformUI.getWorkbench().getHelpSystem().setHelp(esWindow.ec, "com.mmxlabs.lingo.doc.DataModel_lng_cargo_Slot_Window");

		createSpacer();
		makeExpandable(dialogContext, root, object, dbc, esTerms, isLoad ? loadTermsFeatures : dischargeTermsFeatures, null, true);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(esTerms.ec, "com.mmxlabs.lingo.doc.DataModel_lng_cargo_Slot_Terms");

		if (!missedFeaturesList.isEmpty()) {
			createSpacer();
			missedFeaturesList.size();
			for (final ETypedElement f : missedFeaturesList) {
				missedFeatures.add(new ETypedElement[] { f });
			}
			makeExpandable(dialogContext, root, object, dbc, esOther, missedFeatures, null, true);
		}

		for (final ETypedElement[] fs : noteFeatures) {
			EditorControlFactory.makeControls(dialogContext, root, object, contentComposite, fs, feature2Editor, dbc, layoutProvider, toolkit);
		}
	}

	private void makeExpandable(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final EMFDataBindingContext dbc, final ExpandableSet expandable,
			final List<ETypedElement[]> features, final Set<ETypedElement> titleFeatures, final boolean expanded) {
		expandable.setFeatures(features, titleFeatures);
		expandable.create(dialogContext, contentComposite, root, object, feature2Editor, dbc, layoutProvider, toolkit);
		expandable.setExpanded(expanded);
	}

	private void createSpacer() {
		// final Composite spacer = toolkit.createComposite(contentComposite);
		// final GridData gd = new GridData();
		// gd.heightHint = 3;
		// spacer.setLayoutData(gd);
	}

	@Override
	public void expansionStateChanged(final ExpansionEvent e, final ExpandableComposite ec) {
		final Point p = ec.getSize();
		final Point p2 = ec.computeSize(p.x, SWT.DEFAULT);
		ec.setSize(p.x, p2.y);
		contentComposite.layout();
	}

	protected final void expandedStateChanged(final ExpandableComposite expandable) {
		final SharedScrolledComposite parentScrolledComposite = getParentScrolledComposite(expandable);
		if (parentScrolledComposite != null) {
			parentScrolledComposite.reflow(true);
		}
	}

	protected SharedScrolledComposite getParentScrolledComposite(final Control control) {
		Control parent = control.getParent();
		while (!(parent instanceof SharedScrolledComposite) && parent != null) {
			parent = parent.getParent();
		}
		if (parent instanceof SharedScrolledComposite) {
			return (SharedScrolledComposite) parent;
		}
		return null;
	}

	private HashSet<ETypedElement> getAllFeatures(final ArrayList<ETypedElement[]> list) {
		final HashSet<ETypedElement> fs = new HashSet<>();
		for (final ETypedElement[] eStructuralFeatures : list) {
			for (final ETypedElement eStructuralFeature : eStructuralFeatures) {
				fs.add(eStructuralFeature);
			}
		}
		return fs;
	}

	private String formatDate(final LocalDate localDate, final int hourOfDay) {

		return String.format("%02d %s %04d %02d:00", localDate.getDayOfMonth(), localDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()), localDate.getYear(), hourOfDay);
	}

	private enum Section {
		OTHER, MAIN, PRICING, WINDOW, TERMS
	}

	private Section getSectionFor(ETypedElement feature) {
		final EAnnotation annotation = feature.getEAnnotation("http://www.mmxlabs.com/models/ui/layout/slot");

		if (annotation != null) {
			if (annotation.getDetails().containsKey("section")) {
				final String value = annotation.getDetails().get("section");
				switch (value) {
				case "main":
					return Section.MAIN;
				case "pricing":
					return Section.PRICING;
				case "window":
					return Section.WINDOW;
				case "terms":
					return Section.TERMS;
				}
			}
		}
		return Section.OTHER;
	}
}
