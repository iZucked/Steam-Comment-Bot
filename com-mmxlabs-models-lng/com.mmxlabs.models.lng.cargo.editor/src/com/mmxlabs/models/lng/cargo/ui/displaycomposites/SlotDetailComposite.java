/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.SharedScrolledComposite;

import com.google.common.collect.Sets;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.ui.displaycomposites.ExpandableSet.ExpansionListener;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeLayoutProvider;

public class SlotDetailComposite extends DefaultDetailComposite implements IDisplayComposite, ExpansionListener {

	private static final CargoPackage CargoFeatures = CargoPackage.eINSTANCE;
	private static final EStructuralFeature WindowStart = CargoFeatures.getSlot_WindowStart();
	private static final EStructuralFeature WindowStartTime = CargoFeatures.getSlot_WindowStartTime();
	private static final EStructuralFeature WindowSize = CargoFeatures.getSlot_WindowSize();
	private static final EStructuralFeature Contract = CargoFeatures.getSlot_Contract();
	private static final EStructuralFeature PriceExpression = CargoFeatures.getSlot_PriceExpression();

	private static final SimpleDateFormat WindowDateFormat = new SimpleDateFormat("dd MMM YYYY");

	ScrolledComposite scrollComposite;
	Composite contentComposite;
	private final Map<EStructuralFeature, IInlineEditor> feature2Editor;
	final ExpandableSet esPricing;
	private final ExpandableSet esWindow;
	private final ExpandableSet esTerms;
	private ArrayList<EStructuralFeature[]> nameFeatures;
	private ArrayList<EStructuralFeature[]> pricingFeatures;
	private ArrayList<EStructuralFeature[]> mainFeatures;
	private ArrayList<EStructuralFeature[]> windowFeatures;
	private ArrayList<EStructuralFeature[]> loadTermsFeatures;
	private ArrayList<EStructuralFeature[]> dischargeTermsFeatures;
	private ArrayList<EStructuralFeature> missedFeatures;
	private HashSet<EStructuralFeature> windowTitleFeatures;
	private HashSet<EStructuralFeature> pricingTitleFeatures;

	{
		nameFeatures = new ArrayList<EStructuralFeature[]>();
		nameFeatures.add(new EStructuralFeature[] { MMXCorePackage.eINSTANCE.getNamedObject_Name(), CargoFeatures.getSlot_Optional() });

		mainFeatures = new ArrayList<EStructuralFeature[]>();
		mainFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_Port() });
		mainFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_MinQuantity(), CargoFeatures.getSlot_MaxQuantity() });

		pricingFeatures = new ArrayList<EStructuralFeature[]>();
		pricingFeatures.add(new EStructuralFeature[] { Contract });
		pricingFeatures.add(new EStructuralFeature[] { PriceExpression });
		pricingFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_PricingDate() });
		pricingTitleFeatures = Sets.newHashSet(Contract, PriceExpression);

		windowFeatures = new ArrayList<EStructuralFeature[]>();
		windowFeatures.add(new EStructuralFeature[] { WindowStart, WindowStartTime });
		windowFeatures.add(new EStructuralFeature[] { WindowSize, CargoFeatures.getSlot_Duration() });
		windowFeatures.add(new EStructuralFeature[] {});
		windowTitleFeatures = Sets.newHashSet(WindowStart, WindowStartTime, WindowSize);

		loadTermsFeatures = new ArrayList<EStructuralFeature[]>();
		loadTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getLoadSlot_ArriveCold() });
		loadTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getLoadSlot_CargoCV() });
		loadTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_Notes() });

		dischargeTermsFeatures = new ArrayList<EStructuralFeature[]>();
		dischargeTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getDischargeSlot_PurchaseDeliveryType() });
		dischargeTermsFeatures.add(new EStructuralFeature[] { CargoFeatures.getSlot_Notes() });

		missedFeatures = new ArrayList<EStructuralFeature>();
	}

	public SlotDetailComposite(final Composite parent, final int style, FormToolkit toolkit) {
		super(parent, style, toolkit);
		feature2Editor = new HashMap<EStructuralFeature, IInlineEditor>();

		esPricing = new ExpandableSet("Pricing", this){

			@Override
			protected void updateTextClient(EObject eo) {

				MMXObject mmxEo = (MMXObject) eo;
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

		esWindow = new ExpandableSet("Window", this){

			@Override
			protected void updateTextClient(EObject eo) {

				MMXObject mmxEo = (MMXObject) eo;
				final Date d = (Date) mmxEo.eGet(CargoFeatures.getSlot_WindowStart());
				int time = (Integer) mmxEo.eGetWithDefault(CargoFeatures.getSlot_WindowStartTime());
				int wsize = (Integer) mmxEo.eGetWithDefault(CargoFeatures.getSlot_WindowSize());
				textClient.setText(WindowDateFormat.format(d) + ", " + String.format("%02d:00", time) + " - " + wsize + " hours");
			}
		};

		esTerms = new ExpandableSet("Terms", this);
	}

	protected IDisplayCompositeLayoutProvider createLayoutProvider() {
		return new DefaultDisplayCompositeLayoutProvider() {

			@Override
			public Layout createDetailLayout(final MMXRootObject root, final EObject value) {
				return new FillLayout();
			}
		};
	}

	@Override
	public IInlineEditor addInlineEditor(IInlineEditor editor) {

		editor = super.addInlineEditor(editor);
		if (editor != null) {
			final EStructuralFeature f = editor.getFeature();
			feature2Editor.put(f, editor);
			if (!mainFeatures.contains(f) && !windowFeatures.contains(f) && !loadTermsFeatures.contains(f)) {
				missedFeatures.add(f);
			}
		}

		return editor;
	}

	@Override
	public void display(final IScenarioEditingLocation location, final MMXRootObject root, final EObject object, final Collection<EObject> range, final EMFDataBindingContext dbc) {

		super.display(location, root, object, range, dbc);
		MMXObject eo = (MMXObject) object;
		esPricing.init(eo);
		esWindow.init(eo);
		esTerms.init(eo);
		scrollComposite.setMinSize(contentComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	@Override
	public void createControls(final MMXRootObject root, final EObject object, final EMFDataBindingContext dbc) {

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

		scrollComposite = new ScrolledComposite(this, SWT.NONE | SWT.V_SCROLL);
		toolkit.adapt(scrollComposite, true, true);
		scrollComposite.setLayout(new FillLayout());
		scrollComposite.setExpandHorizontal(true);
		scrollComposite.setExpandVertical(true);

		contentComposite = toolkit.createComposite(scrollComposite);
		contentComposite.setLayout(new GridLayout(2, false));
		scrollComposite.setContent(contentComposite);

		for (EStructuralFeature[] fs : nameFeatures) {
			makeControls(root, object, contentComposite, fs, feature2Editor, dbc, layoutProvider, toolkit);
		}

		for (EStructuralFeature[] fs : mainFeatures) {
			makeControls(root, object, contentComposite, fs, feature2Editor, dbc, layoutProvider, toolkit);
		}

		createSpacer();
		esPricing.setFeatures(pricingFeatures, pricingTitleFeatures);
		esPricing.create(contentComposite, root, object, feature2Editor, dbc, layoutProvider, toolkit);
		addExpansionListener(esPricing.ec);

		createSpacer();
		esWindow.setFeatures(windowFeatures, windowTitleFeatures);
		esWindow.create(contentComposite, root, object, feature2Editor, dbc, layoutProvider, toolkit);
		addExpansionListener(esWindow.ec);

		createSpacer();
		esTerms.setFeatures(isLoad ? loadTermsFeatures : dischargeTermsFeatures, null);
		esTerms.create(contentComposite, root, object, feature2Editor, dbc, layoutProvider, toolkit);
		addExpansionListener(esTerms.ec);

//		for (EStructuralFeature f : missedFeatures) {
//		}
		
		this.addControlListener(new ControlAdapter() {
			public void controlResized(final ControlEvent e) {
				scrollComposite.setMinSize(contentComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}
		});
	}

	private void createSpacer() {
		Composite spacer = new Composite(contentComposite, SWT.NONE);
		spacer.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		GridData gd = new GridData();
		gd.heightHint = 3;
		spacer.setLayoutData(gd);
	}

	Composite createExpandable(final ExpandableComposite ec) {
		ec.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		addExpansionListener(ec);
		final Composite inner = toolkit.createComposite(ec);
		inner.setLayout(new GridLayout(2, false));
		ec.setClient(inner);
		return inner;
	}

	private void addExpansionListener(final ExpandableComposite ec) {
		ec.addExpansionListener(createExpansionListener(ec));
	}
	
	private ExpansionAdapter createExpansionListener(
			final ExpandableComposite ec) {
		return new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(final ExpansionEvent e) {
				final Point p = ec.getSize();
				final Point p2 = ec.computeSize(p.x, SWT.DEFAULT);
				ec.setSize(p.x, p2.y);
				scrollComposite.setMinSize(contentComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				contentComposite.layout();
			}
		};
	}

	@Override
	public void expansionStateChanged(final ExpansionEvent e, ExpandableComposite ec) {
		final Point p = ec.getSize();
		final Point p2 = ec.computeSize(p.x, SWT.DEFAULT);
		ec.setSize(p.x, p2.y);
		scrollComposite.setMinSize(contentComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
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

	/**
	 * Make a set of controls for the given []
	 * 
	 * @param root
	 * @param object
	 * @param c
	 * @param fs
	 * @return
	 */
	public static Control makeControls(MMXRootObject root, EObject object, Composite c, EStructuralFeature[] fs, Map<EStructuralFeature, IInlineEditor> feature2Editor, EMFDataBindingContext dbc, IDisplayCompositeLayoutProvider layoutProvider, FormToolkit toolkit) {

		Composite holder = c;
		if (fs.length > 1) {
			holder = new Composite(c, SWT.NONE);
			holder.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			GridData gd = new GridData();
			gd.horizontalSpan = 5;
			holder.setLayoutData(gd);
			GridLayout gl = new GridLayout(2 * fs.length, false);
			gl.marginWidth = 0;
			gl.marginHeight = 0;
			holder.setLayout(gl);
		}
		for (EStructuralFeature f : fs) {
			IInlineEditor editor = feature2Editor.get(f);
			if (editor != null) {
			createLabelledEditorControl(root, object, holder, editor, dbc, layoutProvider, toolkit);
			}
		}
		return holder;
	}

	// private Control makeControl(MMXRootObject root, EObject object, Composite c, EStructuralFeature f, EMFDataBindingContext dbc) {
	//
	// IInlineEditor editor = feature2Editor.get(f);
	// return createLabelledEditorControl(root, object, c, editor, dbc);
	// }
}
