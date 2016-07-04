/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.dialogs;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorWrapper;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;

/**
 * A utility class used for a simple "copy to clipboard" action for the {@link DetailCompositeDialog}. It is implemented as an {@link IInlineEditorWrapper} as a simple way to get access to all the
 * {@link IInlineEditor}s. However we loose the "context" we see with child {@link IDisplayComposite}s created from {@link DefaultTopLevelComposite}s.
 * 
 */
public class CopyDialogToClipboard implements IInlineEditorWrapper {

	private final List<IInlineEditor> editors = new LinkedList<IInlineEditor>();

	private final char separator;

	private final AdapterFactory adapterFactory;

	public CopyDialogToClipboard(final AdapterFactory adapterFactory) {
		this(adapterFactory, '\t');
	}

	public CopyDialogToClipboard(final AdapterFactory adapterFactory, final char separator) {
		this.adapterFactory = adapterFactory;
		this.separator = separator;
	}

	@Override
	public IInlineEditor wrap(final IInlineEditor editor) {

		editors.add(editor);

		return editor;
	}

	public void copyToClipboard() {

		final StringBuffer sb = new StringBuffer();

		for (final IInlineEditor editor : editors) {
			if (editor == null) {
				continue;
			}
			final Label label = editor.getLabel();
			final EObject editorTarget = editor.getEditorTarget();
			final EStructuralFeature feature = editor.getFeature();
			if (feature == null) {
				continue;
			}
			sb.append(label.getText());
			sb.append(separator);
			final Object value = editorTarget.eGet(feature);
			if (value != null) {

				if (value instanceof EObject) {
					final IItemLabelProvider itemLabelProvider = (IItemLabelProvider) adapterFactory.adapt(value, IItemLabelProvider.class);
					final String str = itemLabelProvider != null ? itemLabelProvider.getText(value) : value.toString();
					sb.append(str);
				} else if (value instanceof List) {
					final List<?> l = (List<?>) value;
					boolean first = true;
					sb.append("[");
					for (final Object o : l) {
						if (!first) {
							sb.append(", ");
						}
						first = false;
						if (o instanceof EObject) {
							final IItemLabelProvider itemLabelProvider = (IItemLabelProvider) adapterFactory.adapt(o, IItemLabelProvider.class);
							final String str = itemLabelProvider != null ? itemLabelProvider.getText(o) : o.toString();
							sb.append(str);
						} else {
							sb.append(o.toString());
						}
					}
					sb.append("]");
				} else {
					sb.append(value.toString());
				}
			}
			sb.append("\n");
		}

		// Create a new clipboard instance
		final Display display = Display.getDefault();
		final Clipboard cb = new Clipboard(display);
		try {
			// Create the text transfer and set the contents
			final TextTransfer textTransfer = TextTransfer.getInstance();
			cb.setContents(new Object[] { sb.toString() }, new Transfer[] { textTransfer });
		} finally {
			// Clean up our local resources - system clipboard now has the data
			cb.dispose();
		}
	}
}
