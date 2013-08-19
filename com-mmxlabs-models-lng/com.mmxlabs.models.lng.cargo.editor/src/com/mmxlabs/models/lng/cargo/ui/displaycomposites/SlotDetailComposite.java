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
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
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

public class SlotDetailComposite extends DefaultDetailComposite implements IDisplayComposite {

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
	private final ExpandableSet esPricing;
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
		nameFeatures.add(new EStructuralFeature[]{MMXCorePackage.eINSTANCE.getNamedObject_Name(), CargoFeatures.getSlot_Optional()}); 
			
		mainFeatures = new ArrayList<EStructuralFeature[]>();
		mainFeatures.add(new EStructuralFeature[]{CargoFeatures.getSlot_Port()}); 
		mainFeatures.add(new EStructuralFeature[]{CargoFeatures.getSlot_MinQuantity(), CargoFeatures.getSlot_MaxQuantity()}); 

		pricingFeatures = new ArrayList<EStructuralFeature[]>();
		pricingFeatures.add(new EStructuralFeature[]{Contract}); 
		pricingFeatures.add(new EStructuralFeature[]{PriceExpression}); 
		pricingFeatures.add(new EStructuralFeature[]{CargoFeatures.getSlot_PricingDate()}); 
		pricingTitleFeatures = Sets.newHashSet(Contract, PriceExpression);
		
		windowFeatures = new ArrayList<EStructuralFeature[]>(); 
		windowFeatures.add(new EStructuralFeature[]{WindowStart,WindowStartTime});
		windowFeatures.add(new EStructuralFeature[]{WindowSize, CargoFeatures.getSlot_Duration()});
		windowFeatures.add(new EStructuralFeature[]{});
		windowTitleFeatures = Sets.newHashSet(WindowStart, WindowStartTime, WindowSize);
						
		loadTermsFeatures = new ArrayList<EStructuralFeature[]>(); 
		loadTermsFeatures.add(new EStructuralFeature[]{CargoFeatures.getLoadSlot_ArriveCold()}); 
		loadTermsFeatures.add(new EStructuralFeature[]{CargoFeatures.getLoadSlot_CargoCV()});
		loadTermsFeatures.add(new EStructuralFeature[]{CargoFeatures.getSlot_Notes()});

		dischargeTermsFeatures = new ArrayList<EStructuralFeature[]>(); 
		dischargeTermsFeatures.add(new EStructuralFeature[]{CargoFeatures.getDischargeSlot_PurchaseDeliveryType()});
		dischargeTermsFeatures.add(new EStructuralFeature[]{CargoFeatures.getSlot_Notes()});

		missedFeatures = new ArrayList<EStructuralFeature>();
	}

	private class ExpandableSet implements DisposeListener {

		ExpandableComposite ec;
		Composite client;
		List<EStructuralFeature[]> featureLines;
		Label textClient;
		String baseTitle;
		Set<EStructuralFeature> headerFeatures;
		EContentAdapter titleListener;
		HashSet<EObject> titleEObjects;
		
		public ExpandableSet(String title) {
			baseTitle = title;
			titleEObjects = new HashSet<EObject>();			
		}

		void setFeatures(List<EStructuralFeature[]> f, Set<EStructuralFeature> titleF){
			headerFeatures = titleF;
			featureLines = f;						
			titleListener = headerFeatures == null ? null: new EContentAdapter(){
				@Override
				public void notifyChanged(final Notification notification) {
					super.notifyChanged(notification);
					if((notification.getNotifier() instanceof EObject) && headerFeatures.contains(notification.getFeature()))ExpandableSet.this.updateTextClient((EObject)notification.getNotifier());
				}
			};	
		}	
		
		void create(MMXRootObject root, EObject object, EMFDataBindingContext dbc){
			ec = toolkit.createSection(contentComposite, ExpandableComposite.TWISTIE);
			Composite c = createExpandable(ec);
			ec.addDisposeListener(this);
			// feature editors
			boolean visible = false;
			for (EStructuralFeature[] fs : featureLines) {		
				makeControls(root, object, c, fs, dbc);
				visible = true;
			}
			ec.setExpanded(true);
			ec.setText(baseTitle);
			// title label
			textClient = new Label(ec, SWT.NONE);
			textClient.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			ec.setTextClient(textClient);
			makeLabel();
			updateTextClient(object);
			// hide if no features
			ec.setVisible(visible);
		}

		void init(EObject eo){
			ec.setSize(esPricing.ec.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			ec.layout();
			updateTextClient(eo);
			titleEObjects.add(eo);
			if (titleListener!=null) eo.eAdapters().add(titleListener);
		}
		
		protected void updateTextClient(final EObject eo) {}
		
		void makeLabel(){
		}
				
		@Override
		public void widgetDisposed(DisposeEvent e) {
			if (titleListener!=null) for (EObject eo : titleEObjects) { eo.eAdapters().remove(titleListener); }
		}
	}

	public SlotDetailComposite(final Composite parent, final int style, FormToolkit toolkit) {
		super(parent, style, toolkit);
		feature2Editor = new HashMap<EStructuralFeature, IInlineEditor>();

		esPricing = new ExpandableSet("Pricing"){
			
			@Override
			protected void updateTextClient(EObject eo) {

				MMXObject mmxEo = (MMXObject) eo;
				final Contract c = (Contract) mmxEo.eGet(Contract);
				final String pe = (String) mmxEo.eGet(PriceExpression);
				String text = "";
				if(c != null){					
					text += c.getName() != null ? c.getName() : "";
					text += pe !=null && pe.length() > 0? ", " : "";
				}
				text += pe != null? pe : "";
				textClient.setText(text);		
				textClient.update();
			}
		};

		esWindow = new ExpandableSet("Window"){
			
			@Override
			protected void updateTextClient(EObject eo) {

				MMXObject mmxEo = (MMXObject) eo;
				final Date d = (Date) mmxEo.eGet(CargoFeatures.getSlot_WindowStart());
				int time = (Integer) mmxEo.eGetWithDefault(CargoFeatures.getSlot_WindowStartTime());
				int wsize = (Integer) mmxEo.eGetWithDefault(CargoFeatures.getSlot_WindowSize()) ;
				textClient.setText(WindowDateFormat.format(d) + ", " + String.format("%02d:00", time) + " - " + wsize + " hours");
			}
		};
 
		esTerms = new ExpandableSet("Terms");
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
	public void addInlineEditor(final IInlineEditor editor) {
		final EStructuralFeature f = editor.getFeature();
		feature2Editor.put(f, editor);
		if (!mainFeatures.contains(f) && !windowFeatures.contains(f) && !loadTermsFeatures.contains(f)) {
			missedFeatures.add(f);
		}		

		super.addInlineEditor(editor);
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

//		LoadSlot loadSlot;
//		DischargeSlot dischargeSlot;
		boolean isLoad;
		if (object instanceof LoadSlot) {
//			loadSlot = (LoadSlot) object;
			isLoad = true;
		} else if (object instanceof DischargeSlot) {
//			dischargeSlot = (DischargeSlot) object;
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
			makeControls(root, object, contentComposite, fs, dbc);
		}

		for (EStructuralFeature[] fs : mainFeatures) {
			makeControls(root, object, contentComposite, fs, dbc);
		}
		
		createSpacer();
		esPricing.setFeatures(pricingFeatures, pricingTitleFeatures);
		esPricing.create(root, object, dbc);
		
		createSpacer();
		esWindow.setFeatures(windowFeatures, windowTitleFeatures);
		esWindow.create(root, object, dbc);

		createSpacer();
		esTerms.setFeatures(isLoad? loadTermsFeatures : dischargeTermsFeatures, null);
		esTerms.create(root, object, dbc);

		for (EStructuralFeature f : missedFeatures) {
		}

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

	private Composite createExpandable(final ExpandableComposite ec) {
		ec.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		ec.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(final ExpansionEvent e) {
				final Point p = ec.getSize();
				final Point p2 = ec.computeSize(p.x, SWT.DEFAULT);
				ec.setSize(p.x, p2.y);
				scrollComposite.setMinSize(contentComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				contentComposite.layout();
			}
		});
		final Composite inner = toolkit.createComposite(ec);
		inner.setLayout(new GridLayout(2, false));
		ec.setClient(inner);
		return inner;
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
	 * @param root
	 * @param object
	 * @param c
	 * @param fs
	 * @return
	 */
	private Control makeControls(MMXRootObject root, EObject object, Composite c, EStructuralFeature[] fs, EMFDataBindingContext dbc) {

		Composite holder = c;		
		if(fs.length > 1){
			holder = new Composite(c, SWT.NONE);
			holder.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			GridData gd = new GridData();
			gd.horizontalSpan = 5;
			holder.setLayoutData(gd);
			GridLayout gl = new GridLayout(2*fs.length, false);
			gl.marginWidth = 0;
			gl.marginHeight = 0;
			holder.setLayout(gl);
		}
		for (EStructuralFeature f : fs) {			
			IInlineEditor editor = feature2Editor.get(f);		
			createLabelledEditorControl(root, object, holder, editor, dbc);
		}
		return holder;
	}	
	
//	private Control makeControl(MMXRootObject root, EObject object, Composite c, EStructuralFeature f, EMFDataBindingContext dbc) {
//		
//		IInlineEditor editor = feature2Editor.get(f);		
//		return createLabelledEditorControl(root, object, c, editor, dbc);
//	}
}
