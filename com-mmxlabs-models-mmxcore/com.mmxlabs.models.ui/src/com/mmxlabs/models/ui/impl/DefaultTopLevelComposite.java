package com.mmxlabs.models.ui.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * The default composite used to display an EObject
 * 
 * @author hinton
 * 
 */
public class DefaultTopLevelComposite extends Composite {

	private DefaultDetailComposite topLevel = null;
	private List<EReference> childReferences = new LinkedList<EReference>();
	private List<DefaultDetailComposite> childComposites = new LinkedList<DefaultDetailComposite>();

	public DefaultTopLevelComposite(final Composite parent, final int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
	}

	public void display(final MMXRootObject root, final EObject object) {
		final EClass eClass = object.eClass();
		final Group g = new Group(this, SWT.NONE);
		g.setText(eClass.getName());
		g.setLayout(new FillLayout());
		topLevel = new DefaultDetailComposite(g, SWT.NONE);
		
		for (final EReference ref : eClass.getEAllReferences()) {
			if (ref.isContainment() && !ref.isMany()) {
				final EObject value = (EObject) object.eGet(ref);
				if (value != null) {
					final Group g2 = new Group(this, SWT.NONE);
					g2.setText(value.eClass().getName());
					g2.setLayout(new FillLayout());
					final DefaultDetailComposite sub = new DefaultDetailComposite(
							g2, SWT.NONE);
					childReferences.add(ref);
					childComposites.add(sub);
				}
			}
		}

		topLevel.display(root, object);
		final Iterator<EReference> refs = childReferences.iterator();
		final Iterator<DefaultDetailComposite> children = childComposites
				.iterator();

		while (refs.hasNext()) {
			children.next().display(root, (EObject) object.eGet(refs.next()));
		}
		
		((GridLayout)getLayout()).numColumns = childComposites.size() + 1;
	}
}
