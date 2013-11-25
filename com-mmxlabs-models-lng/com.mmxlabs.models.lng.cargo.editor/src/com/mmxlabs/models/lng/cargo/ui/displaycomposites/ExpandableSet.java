/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

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
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.Section;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.util.EditorControlFactory;

class ExpandableSet implements DisposeListener {

	public interface ExpansionListener {
		void expansionStateChanged(final ExpansionEvent e, ExpandableComposite ec);
	}
	
	
	ExpandableComposite ec;
	Composite client;
	List<EStructuralFeature[]> featureLines;
//	Hyperlink textClient;
	Label textClient;
	String baseTitle;
	Set<EStructuralFeature> headerFeatures;
	EContentAdapter titleListener;
	HashSet<EObject> titleEObjects;
	private ExpansionListener expansionListener;
	
	public ExpandableSet(String title, ExpansionListener el) {
		baseTitle = title;
		titleEObjects = new HashSet<EObject>();	
		expansionListener = el;
	}

	void setFeatures(List<EStructuralFeature[]> f, Set<EStructuralFeature> titleF){
		headerFeatures = titleF;
		featureLines = f;						
		titleListener = headerFeatures == null ? null: new EContentAdapter(){
			@Override
			public void notifyChanged(final Notification notification) {
				super.notifyChanged(notification);
				if((notification.getNotifier() instanceof EObject) && headerFeatures.contains(notification.getFeature())) ExpandableSet.this.updateTextClient((EObject)notification.getNotifier());
			}
		};	
	}	
	
	void create(Composite contentComposite, MMXRootObject root, EObject object,  Map<EStructuralFeature, IInlineEditor> feature2Editor, EMFDataBindingContext dbc, IDisplayCompositeLayoutProvider lp, FormToolkit toolkit){
//		ec = toolkit.createSection(contentComposite, Section.TITLE_BAR | ExpandableComposite.TWISTIE);
		ec = toolkit.createSection(contentComposite, ExpandableComposite.TWISTIE);
		Composite c = createExpandable(ec, toolkit);
		ec.addDisposeListener(this);	
		// feature editors
		boolean visible = false;
		for (EStructuralFeature[] fs : featureLines) {		
			EditorControlFactory.makeControls(root, object, c, fs, feature2Editor, dbc, lp, toolkit);
			visible = true;
		}
		ec.setExpanded(true);
		ec.setText(baseTitle);
		// title label
		{
//			textClient = toolkit.createHyperlink(ec, "", SWT.NONE);
			textClient = toolkit.createLabel(ec, "", SWT.NONE);
		}
		textClient.setText("");
		ec.setTextClient(textClient);
		updateTextClient(object);
		// hide if no features
		ec.setVisible(visible);
		ec.addExpansionListener(new ExpansionAdapter() 
		{
			@Override
			public void expansionStateChanged(final ExpansionEvent e) {
				expansionListener.expansionStateChanged(e, ec);
			}
		});
	}
	
	private Composite createExpandable(final ExpandableComposite ec, FormToolkit toolkit) {
		ec.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,2, 1));
		final Composite inner = toolkit.createComposite(ec);
		inner.setLayout(new GridLayout(2, false));
		ec.setClient(inner);
		return inner;
	}

	void init(EObject eo){
		ec.setSize(ec.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		ec.layout();
		updateTextClient(eo);
		titleEObjects.add(eo);
		if (titleListener!=null) eo.eAdapters().add(titleListener);
	}
	
	public void setExpanded(boolean b){
		ec.setExpanded(b);
	}
	
	protected void updateTextClient(final EObject eo) {}
			
	@Override
	public void widgetDisposed(DisposeEvent e) {
		if (titleListener!=null) for (EObject eo : titleEObjects) { eo.eAdapters().remove(titleListener); }
	}
}