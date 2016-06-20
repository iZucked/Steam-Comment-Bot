/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import java.time.LocalDate;
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
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
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
	private static final EStructuralFeature Contract = CargoFeatures.getSlot_Contract();
	private static final EStructuralFeature PriceExpression = CargoFeatures.getSlot_PriceExpression();
	private static final EClass SlotContractParams = CommercialPackage.eINSTANCE.getSlotContractParams();

	Composite contentComposite;
	private final Map<EStructuralFeature, IInlineEditor> feature2Editor;
	final ExpandableSet esPricing;
	private final ExpandableSet esWindow;
	private final ExpandableSet esTerms;
	private final ExpandableSet esOther;
	private ArrayList<EStructuralFeature[]> nameFeatures;
	private ArrayList<EStructuralFeature[]> pricingFeatures;
	private HashSet<EStructuralFeature> pricingTitleFeatures;
	private ArrayList<EStructuralFeature[]> mainFeatures;
	private ArrayList<EStructuralFeature[]> windowFeatures;
	private HashSet<EStructuralFeature> windowTitleFeatures;
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
		mainFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_MinQuantity(), CargoFeatures.getSlot_MaxQuantity(), CargoFeatures.getSlot_VolumeLimitsUnit() });
		allFeatures.addAll(getAllFeatures(mainFeatures));

		pricingFeatures = new ArrayList<EStructuralFeature[]>();
		pricingFeatures.add(new EStructuralFeature[] { Contract });
		pricingFeatures.add(new EStructuralFeature[] { PriceExpression });
		pricingFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_PricingEvent(), CargoFeatures.getSlot_PricingDate() });
		pricingFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_Hedges(), CargoFeatures.getSlot_CancellationExpression() });
		pricingTitleFeatures = Sets.newHashSet(Contract, PriceExpression);
		allFeatures.addAll(getAllFeatures(pricingFeatures));

		windowFeatures = new ArrayList<EStructuralFeature[]>();
		windowFeatures.add(new EStructuralFeature[] { WindowStart, WindowStartTime });
		windowFeatures.add(new EStructuralFeature[] { WindowSize, WindowSizeUnits });
		windowFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_Duration(), WindowFlex });
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
					final String text = formatDate(d, time) + " - " + wsize + " " + getUnits(ePeriod);
					textClient.setText(text);
					// ec.setText(baseTitle + ": " + text);
				}
			}

			private String getUnits(TimePeriod ePeriod) {
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
				if (feature == CargoPackage.Literals.SLOT__MAX_QUANTITY || feature == CargoPackage.Literals.SLOT__MIN_QUANTITY) {
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
				missedFeaturesList.add(f);
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
		// contentComposite.setLayout(new GridLayout(2, false));

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
			// System.out.println("Adding pricing feature: " + f);
			missedFeaturesList.remove(f);
		}

		makeExpandable(dialogContext, root, object, dbc, esPricing, pricingFeatures, pricingTitleFeatures, true);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(esPricing.ec, "com.mmxlabs.lingo.doc.DataModel_lng_cargo_Slot_Pricing");

		createSpacer();
		makeExpandable(dialogContext, root, object, dbc, esWindow, windowFeatures, windowTitleFeatures, true);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(esWindow.ec, "com.mmxlabs.lingo.doc.DataModel_lng_cargo_Slot_Window");

		createSpacer();
		makeExpandable(dialogContext, root, object, dbc, esTerms, isLoad ? loadTermsFeatures : dischargeTermsFeatures, null, true);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(esTerms.ec, "com.mmxlabs.lingo.doc.DataModel_lng_cargo_Slot_Terms");

		if (!missedFeaturesList.isEmpty()) {

			createSpacer();
			missedFeaturesList.size();
			for (final EStructuralFeature f : missedFeaturesList) {
				missedFeatures.add(new EStructuralFeature[] { f });
			}
			// System.out.println(object);
			// System.out.println(missedFeaturesList.size());
			// for (EStructuralFeature[] eStructuralFeatures : missedFeatures) {
			// for (EStructuralFeature eStructuralFeature : eStructuralFeatures) {
			// System.out.println(eStructuralFeature);
			// }
			// }
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
}
