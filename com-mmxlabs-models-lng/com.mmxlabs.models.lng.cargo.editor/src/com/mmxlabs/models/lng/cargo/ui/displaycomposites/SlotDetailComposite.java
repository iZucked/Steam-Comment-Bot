/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
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
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.CargoPackage;
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
	private static final EStructuralFeature WindowNomination = CargoFeatures.getSlot_WindowNominationDate();
	private static final EStructuralFeature WindowNominationIsDone = CargoFeatures.getSlot_WindowNominationIsDone();
	private static final EStructuralFeature WindowNominationCounterparty = CargoFeatures.getSlot_WindowNominationCounterparty();
	private static final EStructuralFeature WindowNominationComment = CargoFeatures.getSlot_WindowNominationComment();
	private static final EStructuralFeature VesselNomination = CargoFeatures.getSlot_VesselNominationDate();
	private static final EStructuralFeature VesselNominationDone = CargoFeatures.getSlot_VesselNominationDone();
	private static final EStructuralFeature VesselNominationCounterparty = CargoFeatures.getSlot_VesselNominationCounterparty();
	private static final EStructuralFeature VesselNominationComment = CargoFeatures.getSlot_VesselNominationComment();
	private static final EStructuralFeature PortNomination = CargoFeatures.getSlot_PortNominationDate();
	private static final EStructuralFeature PortNominationDone = CargoFeatures.getSlot_PortNominationDone();
	private static final EStructuralFeature PortNominationCounterparty = CargoFeatures.getSlot_PortNominationCounterparty();
	private static final EStructuralFeature PortNominationComment = CargoFeatures.getSlot_PortNominationComment();
	private static final EStructuralFeature PortLoadNomination = CargoFeatures.getSlot_PortLoadNominationDate();
	private static final EStructuralFeature PortLoadNominationDone = CargoFeatures.getSlot_PortLoadNominationDone();
	private static final EStructuralFeature PortLoadNominationCounterparty = CargoFeatures.getSlot_PortLoadNominationCounterparty();
	private static final EStructuralFeature PortLoadNominationComment = CargoFeatures.getSlot_PortLoadNominationComment();
	private static final EStructuralFeature VolumeNomination = CargoFeatures.getSlot_VolumeNominationDate();
	private static final EStructuralFeature VolumeNominationDone = CargoFeatures.getSlot_VolumeNominationDone();
	private static final EStructuralFeature VolumeNominationCounterparty = CargoFeatures.getSlot_VolumeNominationCounterparty();
	private static final EStructuralFeature VolumeNominationComment = CargoFeatures.getSlot_VolumeNominationComment();
	private static final EStructuralFeature Contract = CargoFeatures.getSlot_Contract();
	private static final EStructuralFeature PriceExpression = CargoFeatures.getSlot_PriceExpression();
	private static final EClass SlotContractParams = CommercialPackage.eINSTANCE.getSlotContractParams();

	Composite contentComposite;
	private final Map<EStructuralFeature, IInlineEditor> feature2Editor;
	final ExpandableSet esPricing;
	private final ExpandableSet esWindow;
	private ExpandableSet esNomination;
	private final ExpandableSet esTerms;
	private final ExpandableSet esOther;
	private ArrayList<EStructuralFeature[]> nameFeatures;
	private ArrayList<EStructuralFeature[]> pricingFeatures;
	private HashSet<EStructuralFeature> pricingTitleFeatures;
	private ArrayList<EStructuralFeature[]> mainFeatures;
	private ArrayList<EStructuralFeature[]> windowFeatures;
	private HashSet<EStructuralFeature> windowTitleFeatures;
	
	private ArrayList<EStructuralFeature[]> nominationFeatures;
	private HashSet<EStructuralFeature> nominationTitleFeatures;
	
	private ArrayList<EStructuralFeature[]> loadTermsFeatures;
	private ArrayList<EStructuralFeature[]> dischargeTermsFeatures;
	private ArrayList<EStructuralFeature[]> noteFeatures;
	private HashSet<EStructuralFeature> allFeatures;

	private ArrayList<EStructuralFeature[]> missedFeatures;
	private ArrayList<EStructuralFeature> missedFeaturesList;

	{
		allFeatures = new HashSet<EStructuralFeature>();

		nameFeatures = new ArrayList<EStructuralFeature[]>();
		nameFeatures.add(new EStructuralFeature[] { MMXCorePackage.eINSTANCE.getNamedObject_Name(), CargoFeatures.getSlot_Optional(), CargoFeatures.getSlot_Locked() });
		allFeatures.addAll(getAllFeatures(nameFeatures));

		mainFeatures = new ArrayList<EStructuralFeature[]>();
		mainFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_Port(), CargoFeatures.getSlot_Entity() });
		mainFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_MinQuantity(), CargoFeatures.getSlot_MaxQuantity(), CargoFeatures.getSlot_VolumeLimitsUnit(),
				CargoFeatures.getSlot_OperationalTolerance() });
		mainFeatures.add(new EStructuralFeature[] { CargoPackage.Literals.SLOT__COUNTERPARTY, CargoPackage.Literals.SLOT__CN });
		allFeatures.addAll(getAllFeatures(mainFeatures));

		pricingFeatures = new ArrayList<EStructuralFeature[]>();
		pricingFeatures.add(new EStructuralFeature[] { Contract, CargoPackage.Literals.SPOT_SLOT__MARKET });
		pricingFeatures.add(new EStructuralFeature[] { PriceExpression });
		pricingFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_PricingEvent(), CargoFeatures.getSlot_PricingDate() });
		pricingFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_Hedges(), CargoFeatures.getSlot_MiscCosts(), CargoFeatures.getSlot_CancellationExpression() });
		pricingTitleFeatures = Sets.newHashSet(Contract, PriceExpression);
		allFeatures.addAll(getAllFeatures(pricingFeatures));

		windowFeatures = new ArrayList<EStructuralFeature[]>();
		windowFeatures.add(new EStructuralFeature[] { WindowStart, WindowStartTime });
		windowFeatures.add(new EStructuralFeature[] { WindowSize, WindowSizeUnits });
		windowFeatures.add(new EStructuralFeature[] { WindowFlex, WindowFlexUnits });
		windowFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_Duration() });
		windowFeatures.add(new EStructuralFeature[] {});
		windowTitleFeatures = Sets.newHashSet(WindowStart, WindowStartTime, WindowSize, WindowSizeUnits);
		allFeatures.addAll(getAllFeatures(windowFeatures));

		loadTermsFeatures = new ArrayList<EStructuralFeature[]>();
		loadTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getLoadSlot_ArriveCold(), CargoFeatures.getLoadSlot_CargoCV() });
		loadTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_OverrideRestrictions() });
		loadTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_RestrictedListsArePermissive() });
		loadTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_RestrictedPorts() });
		loadTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_RestrictedContracts() });

		loadTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_AllowedVessels() });
		loadTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_NominatedVessel() });
		loadTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_Divertible(), CargoFeatures.getSlot_ShippingDaysRestriction() });
		loadTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getLoadSlot_SalesDeliveryType() });
		allFeatures.addAll(getAllFeatures(loadTermsFeatures));

		dischargeTermsFeatures = new ArrayList<EStructuralFeature[]>();
		dischargeTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getDischargeSlot_PurchaseDeliveryType() });
		dischargeTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getDischargeSlot_MinCvValue(), CargoFeatures.getDischargeSlot_MaxCvValue() });
		dischargeTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_OverrideRestrictions() });
		dischargeTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_RestrictedListsArePermissive() });
		dischargeTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_RestrictedPorts() });
		dischargeTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_RestrictedContracts() });
		dischargeTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_AllowedVessels() });
		dischargeTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_NominatedVessel() });
		dischargeTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_Divertible(), CargoFeatures.getSlot_ShippingDaysRestriction() });
		allFeatures.addAll(getAllFeatures(dischargeTermsFeatures));

		noteFeatures = new ArrayList<EStructuralFeature[]>();
		noteFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_Notes() });
		allFeatures.addAll(getAllFeatures(noteFeatures));

		missedFeaturesList = new ArrayList<EStructuralFeature>();
		missedFeatures = new ArrayList<EStructuralFeature[]>();
	}

	public SlotDetailComposite(final Composite parent, final int style, final FormToolkit toolkit) {
		super(parent, style, toolkit);
		feature2Editor = new HashMap<EStructuralFeature, IInlineEditor>();

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
				// ec.setText(baseTitle + ": " + text);
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
						final Slot slot = (Slot) mmxEo;
						final ZonedDateTime ed = slot.getWindowEndWithSlotOrPortTime();
						final String text = formatDate(d, time) + " - " + formatDate(ed.toLocalDate(), ed.toLocalDateTime().getHour());
						textClient.setText(text);
					} else {
						final String text = formatDate(d, time) + " - " + wsize + " " + getUnits(ePeriod);
						textClient.setText(text);
					}
					// ec.setText(baseTitle + ": " + text);
				}
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
		
		// FM - put separately to allow feature enablement
		if (LicenseFeatures.isPermitted("features:nominations")) {
			esNomination = new ExpandableSet("Nominations", this);
			{
				nominationFeatures = new ArrayList<EStructuralFeature[]>();
				nominationFeatures.add(new EStructuralFeature[] {WindowNomination, WindowNominationCounterparty, WindowNominationIsDone});
				nominationFeatures.add(new EStructuralFeature[] {WindowNominationComment});
				nominationFeatures.add(new EStructuralFeature[] {VolumeNomination, VolumeNominationCounterparty, VolumeNominationDone});
				nominationFeatures.add(new EStructuralFeature[] {VolumeNominationComment});
				nominationFeatures.add(new EStructuralFeature[] {VesselNomination, VesselNominationCounterparty, VesselNominationDone});
				nominationFeatures.add(new EStructuralFeature[] {VesselNominationComment});
				nominationFeatures.add(new EStructuralFeature[] {PortNomination, PortNominationCounterparty, PortNominationDone});
				nominationFeatures.add(new EStructuralFeature[] {PortNominationComment});
				nominationFeatures.add(new EStructuralFeature[] {PortLoadNomination, PortLoadNominationCounterparty, PortLoadNominationDone});
				nominationFeatures.add(new EStructuralFeature[] {PortLoadNominationComment});
				nominationTitleFeatures = Sets.newHashSet(WindowNomination, WindowNominationCounterparty, WindowNominationIsDone, WindowNominationComment,
						VolumeNomination, VolumeNominationCounterparty, VolumeNominationDone, VolumeNominationComment,
						VesselNomination, VesselNominationCounterparty, VesselNominationDone, VesselNominationComment,
						PortNomination, PortNominationCounterparty, PortNominationDone, PortNominationComment,
						PortLoadNomination, PortLoadNominationCounterparty, PortLoadNominationDone, PortLoadNominationComment);
				allFeatures.addAll(getAllFeatures(nominationFeatures));
			}
		}

		esTerms = new ExpandableSet("Terms", this);

		esOther = new ExpandableSet("Other", this);
	}

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
				final EStructuralFeature feature = editor.getFeature();
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
				if (LicenseFeatures.isPermitted("features:nominations")) {
					if (feature == CargoPackage.Literals.SLOT__WINDOW_NOMINATION_DATE 
							|| feature == CargoPackage.Literals.SLOT__WINDOW_NOMINATION_IS_DONE
							|| feature == CargoPackage.Literals.SLOT__WINDOW_NOMINATION_COUNTERPARTY
							|| feature == CargoPackage.Literals.SLOT__WINDOW_NOMINATION_COMMENT) {
						final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
						// 64 - magic constant from MultiDetailDialog
						gd.widthHint = 100;

						if (feature == CargoPackage.Literals.SLOT__WINDOW_NOMINATION_DATE) {
							final Label label = editor.getLabel();
							if (label != null) {
								label.setText("Window");
							}
							editor.setLabel(null);
						} else if(feature == CargoPackage.Literals.SLOT__WINDOW_NOMINATION_COUNTERPARTY) {
							final Label label = editor.getLabel();
							if (label != null) {
								label.setText("CP");
							}
							editor.setLabel(null);
						} else if(feature == CargoPackage.Literals.SLOT__WINDOW_NOMINATION_COMMENT) {
							final Label label = editor.getLabel();
							if (label != null) {
								label.setText("Remarks");
							}
							editor.setLabel(null);
						} else {
							final Label label = editor.getLabel();
							gd.widthHint = 10;
							if (label != null) {
								label.setText("Done");
							}
							editor.setLabel(null);
						}
						return gd;
					}
					if (feature == CargoPackage.Literals.SLOT__VESSEL_NOMINATION_DATE 
							|| feature == CargoPackage.Literals.SLOT__VESSEL_NOMINATION_DONE
							|| feature == CargoPackage.Literals.SLOT__VESSEL_NOMINATION_COUNTERPARTY
							|| feature == CargoPackage.Literals.SLOT__VESSEL_NOMINATION_COMMENT) {
						final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
						// 64 - magic constant from MultiDetailDialog
						gd.widthHint = 100;

						if (feature == CargoPackage.Literals.SLOT__VESSEL_NOMINATION_DATE) {
							final Label label = editor.getLabel();
							if (label != null) {
								label.setText("Vessel");
							}
							editor.setLabel(null);
						} else if(feature == CargoPackage.Literals.SLOT__VESSEL_NOMINATION_COUNTERPARTY) {
							final Label label = editor.getLabel();
							if (label != null) {
								label.setText("CP");
							}
							editor.setLabel(null);
						} else if(feature == CargoPackage.Literals.SLOT__VESSEL_NOMINATION_COMMENT) {
							final Label label = editor.getLabel();
							if (label != null) {
								label.setText("Remarks");
							}
							editor.setLabel(null);
						} else {
							final Label label = editor.getLabel();
							gd.widthHint = 10;
							if (label != null) {
								label.setText("Done");
							}
							editor.setLabel(null);
						}
						return gd;
					}
					if (feature == CargoPackage.Literals.SLOT__VOLUME_NOMINATION_DATE 
							|| feature == CargoPackage.Literals.SLOT__VOLUME_NOMINATION_DONE
							|| feature == CargoPackage.Literals.SLOT__VOLUME_NOMINATION_COUNTERPARTY
							|| feature == CargoPackage.Literals.SLOT__VOLUME_NOMINATION_COMMENT) {
						final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
						// 64 - magic constant from MultiDetailDialog
						gd.widthHint = 100;

						if (feature == CargoPackage.Literals.SLOT__VOLUME_NOMINATION_DATE) {
							final Label label = editor.getLabel();
							if (label != null) {
								label.setText("Volume");
							}
							editor.setLabel(null);
						} else if(feature == CargoPackage.Literals.SLOT__VOLUME_NOMINATION_COUNTERPARTY) {
							final Label label = editor.getLabel();
							if (label != null) {
								label.setText("CP");
							}
							editor.setLabel(null);
						} else if(feature == CargoPackage.Literals.SLOT__VOLUME_NOMINATION_COMMENT) {
							final Label label = editor.getLabel();
							if (label != null) {
								label.setText("Remarks");
							}
							editor.setLabel(null);
						} else {
							final Label label = editor.getLabel();
							gd.widthHint = 10;
							if (label != null) {
								label.setText("Done");
							}
							editor.setLabel(null);
						}
						return gd;
					}
					if (feature == CargoPackage.Literals.SLOT__PORT_NOMINATION_DATE 
							|| feature == CargoPackage.Literals.SLOT__PORT_NOMINATION_DONE
							|| feature == CargoPackage.Literals.SLOT__PORT_NOMINATION_COUNTERPARTY
							|| feature == CargoPackage.Literals.SLOT__PORT_NOMINATION_COMMENT) {
						final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
						// 64 - magic constant from MultiDetailDialog
						gd.widthHint = 100;

						if (feature == CargoPackage.Literals.SLOT__PORT_NOMINATION_DATE) {
							final Label label = editor.getLabel();
							if (label != null) {
								label.setText("Port");
							}
							editor.setLabel(null);
						} else if(feature == CargoPackage.Literals.SLOT__PORT_NOMINATION_COUNTERPARTY) {
							final Label label = editor.getLabel();
							if (label != null) {
								label.setText("CP");
							}
							editor.setLabel(null);
						} else if(feature == CargoPackage.Literals.SLOT__PORT_NOMINATION_COMMENT) {
							final Label label = editor.getLabel();
							if (label != null) {
								label.setText("Remarks");
							}
							editor.setLabel(null);
						} else {
							final Label label = editor.getLabel();
							gd.widthHint = 10;
							if (label != null) {
								label.setText("Done");
							}
							editor.setLabel(null);
						}
						return gd;
					}
					if (feature == CargoPackage.Literals.SLOT__PORT_LOAD_NOMINATION_DATE 
							|| feature == CargoPackage.Literals.SLOT__PORT_LOAD_NOMINATION_DONE
							|| feature == CargoPackage.Literals.SLOT__PORT_LOAD_NOMINATION_COUNTERPARTY
							|| feature == CargoPackage.Literals.SLOT__PORT_LOAD_NOMINATION_COMMENT) {
						final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
						// 64 - magic constant from MultiDetailDialog
						gd.widthHint = 100;

						if (feature == CargoPackage.Literals.SLOT__PORT_LOAD_NOMINATION_DATE) {
							final Label label = editor.getLabel();
							if (label != null) {
								label.setText("Load Port");
							}
							editor.setLabel(null);
						} else if(feature == CargoPackage.Literals.SLOT__PORT_LOAD_NOMINATION_COUNTERPARTY) {
							final Label label = editor.getLabel();
							if (label != null) {
								label.setText("CP");
							}
							editor.setLabel(null);
						} else if(feature == CargoPackage.Literals.SLOT__PORT_LOAD_NOMINATION_COMMENT) {
							final Label label = editor.getLabel();
							if (label != null) {
								label.setText("Remarks");
							}
							editor.setLabel(null);
						} else {
							final Label label = editor.getLabel();
							gd.widthHint = 10;
							if (label != null) {
								label.setText("Done");
							}
							editor.setLabel(null);
						}
						return gd;
					}
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
				if (feature == CargoPackage.Literals.SLOT__VOLUME_LIMITS_UNIT) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					// 64 - magic constant from MultiDetailDialog
					gd.widthHint = 80;
					editor.setLabel(null);
					return gd;
				}
				if (feature == CargoPackage.Literals.SLOT__NOTES) {
					final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
					gd.widthHint = 100;
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
			final EStructuralFeature f = editor.getFeature();
			feature2Editor.put(f, editor);
			if (!allFeatures.contains(f)) {
				Section section = getSectionFor(f);
				switch (section) {
				case MAIN:
					mainFeatures.add(new EStructuralFeature[]{f});
					break;
				case PRICING:
					pricingFeatures.add(new EStructuralFeature[]{f});
					break;
				case TERMS:
					loadTermsFeatures.add(new EStructuralFeature[]{f});
					dischargeTermsFeatures.add(new EStructuralFeature[]{f});
					break;
				case WINDOW:
					windowFeatures.add(new EStructuralFeature[]{f});
					break;
				case NOMINATION:
					if (LicenseFeatures.isPermitted("features:nominations")) {
						nominationFeatures.add(new EStructuralFeature[]{f});
					}
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
		if (LicenseFeatures.isPermitted("features:nominations")) {
			esNomination.init(eo);
		}
		esTerms.init(eo);
		esTerms.init(eo);
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

		contentComposite = toolkit.createComposite(this);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(contentComposite, "com.mmxlabs.lingo.doc.DataModel_lng_cargo_Slot");

		final Layout l = layoutProvider.createDetailLayout(root, object);

		final GridLayout layout = new GridLayout(2, false);
		layout.verticalSpacing = 8;
		contentComposite.setLayout(layout);

		for (final EStructuralFeature[] fs : nameFeatures) {
			EditorControlFactory.makeControls(dialogContext, root, object, contentComposite, fs, feature2Editor, dbc, layoutProvider, toolkit);
		}

		for (final EStructuralFeature[] fs : mainFeatures) {
			EditorControlFactory.makeControls(dialogContext, root, object, contentComposite, fs, feature2Editor, dbc, layoutProvider, toolkit);
		}

		createSpacer();

		final HashSet<EStructuralFeature> contractFeatures = new LinkedHashSet<EStructuralFeature>();
		for (final EStructuralFeature f : missedFeaturesList) {

			if (f.getEContainingClass().getEAllSuperTypes().contains(SlotContractParams)) {
				contractFeatures.add(f);
			}
		}
		for (final EStructuralFeature f : contractFeatures) {
			pricingFeatures.add(new EStructuralFeature[] { f });
			missedFeaturesList.remove(f);
		}

		makeExpandable(dialogContext, root, object, dbc, esPricing, pricingFeatures, pricingTitleFeatures, true);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(esPricing.ec, "com.mmxlabs.lingo.doc.DataModel_lng_cargo_Slot_Pricing");

		createSpacer();
		makeExpandable(dialogContext, root, object, dbc, esWindow, windowFeatures, windowTitleFeatures, true);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(esWindow.ec, "com.mmxlabs.lingo.doc.DataModel_lng_cargo_Slot_Window");
		
		if (LicenseFeatures.isPermitted("features:nominations")) {
			createSpacer();
			makeExpandable(dialogContext, root, object, dbc, esNomination, nominationFeatures, nominationTitleFeatures, isNominationPresent(object));
		}
		
		createSpacer();
		makeExpandable(dialogContext, root, object, dbc, esTerms, isLoad ? loadTermsFeatures : dischargeTermsFeatures, null, true);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(esTerms.ec, "com.mmxlabs.lingo.doc.DataModel_lng_cargo_Slot_Terms");

		if (!missedFeaturesList.isEmpty()) {

			createSpacer();
			missedFeaturesList.size();
			for (final EStructuralFeature f : missedFeaturesList) {
				missedFeatures.add(new EStructuralFeature[] { f });
			}
			makeExpandable(dialogContext, root, object, dbc, esOther, missedFeatures, null, true);
		}

		for (final EStructuralFeature[] fs : noteFeatures) {
			EditorControlFactory.makeControls(dialogContext, root, object, contentComposite, fs, feature2Editor, dbc, layoutProvider, toolkit);
		}
	}

	private void makeExpandable(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final EMFDataBindingContext dbc, final ExpandableSet expandable,
			final List<EStructuralFeature[]> features, final Set<EStructuralFeature> titleFeatures, final boolean expanded) {
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

	private HashSet<EStructuralFeature> getAllFeatures(final ArrayList<EStructuralFeature[]> list) {
		final HashSet<EStructuralFeature> fs = new HashSet<EStructuralFeature>();
		for (final EStructuralFeature[] eStructuralFeatures : list) {
			for (final EStructuralFeature eStructuralFeature : eStructuralFeatures) {
				fs.add(eStructuralFeature);
			}
		}
		return fs;
	}

	private String formatDate(final LocalDate localDate, final int hourOfDay) {

		return String.format("%02d %s %04d %02d:00", localDate.getDayOfMonth(), localDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()), localDate.getYear(), hourOfDay);
	}
	
	
	private enum Section {
		OTHER, MAIN,
		PRICING, WINDOW, TERMS, NOMINATION
	}
	
	private Section getSectionFor(EStructuralFeature feature) {
		final EAnnotation annotation = feature.getEAnnotation("http://www.mmxlabs.com/models/ui/layout/slot");

		if (annotation != null) {
			if (annotation.getDetails().containsKey("section")) {
				String value = annotation.getDetails().get("section");
				switch (value) {
				case "main": return Section.MAIN;
				case "pricing": return Section.PRICING;
				case "window": return Section.WINDOW;
				case "terms": return Section.TERMS;
				case "nominations": return Section.NOMINATION;
				}
			}
		}
		return Section.OTHER;
	}
	
	private boolean isNominationPresent(EObject object) {
		if (object instanceof Slot) {
			Slot slot = (Slot) object;
			if (slot.getSlotOrDelegateWindowNominationDate() != null 
					|| slot.getSlotOrDelegateVolumeNominationDate() != null
					|| slot.getSlotOrDelegateVesselNominationDate() != null
					|| slot.getSlotOrDelegatePortNominationDate() != null) {
				return true;
			}
		}
		return false;
	}
}
