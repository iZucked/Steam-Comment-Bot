/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.ui.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IInlineEditor;

/**
 * A builder style class to construct custom editor dialog control layouts where multiple features are to be grouped together on one line. Note: features still need to be added in the correct order to
 * the owning composite.
 * 
 * @author Simon Goodall
 *
 */
public class RowGroupDisplayCompositeLayoutProviderBuilder {

	private int maxFeatures = 1;
	private List<RowBuilder> rows = new LinkedList<>();

	public class RowBuilder {

		private String label;
		private List<ETypedElement> orderedFeatures = new LinkedList<>();
		private Map<ETypedElement, Integer> spans = new HashMap<>();
		private Map<ETypedElement, Integer> widthHints = new HashMap<>();
		private Map<ETypedElement, String> labels = new HashMap<>();

		/**
		 * If called, use this String as the label for the start of the row. Hide labels for the other controls
		 * 
		 * @param label
		 * @return
		 */
		public RowBuilder withLabel(String label) {
			this.label = label;
			return this;
		}

		/**
		 * Define a feature for the row
		 */
		public RowBuilder withFeature(ETypedElement feature) {
			orderedFeatures.add(feature);
			return this;
		}
		
		/**
		 * Define a feature for the row with a label override. Calling #withLabel will override this behaviour
		 */
		public RowBuilder withFeature(ETypedElement feature, String label) {
			orderedFeatures.add(feature);
			labels.put(feature,  label);
			return this;
		}

		/**
		 * Define a feature for the row with a min width hint.
		 * 
		 * @See {@link GridData#widthHint}
		 * @param feature
		 * @param widthHint
		 * @return
		 */
		public RowBuilder withFeature(ETypedElement feature, int widthHint) {
			orderedFeatures.add(feature);
			widthHints.put(feature, widthHint);
			return this;
		}

		public RowGroupDisplayCompositeLayoutProviderBuilder makeRow() {
			rows.add(this);
			maxFeatures = Math.max(maxFeatures, orderedFeatures.size());
			return RowGroupDisplayCompositeLayoutProviderBuilder.this;
		}
	}

	/**
	 * Define a new row group.
	 * 
	 * @return
	 */
	public RowBuilder withRow() {
		return new RowBuilder();
	}

	public DefaultDisplayCompositeLayoutProvider make() {

		// As row could contain fewer items that the max, apply a simple scheme to set the expected span.
		// TODO: Not tested! (also see how we apply the value)
		for (RowBuilder row : rows) {
			int count = 0;
			for (ETypedElement f : row.orderedFeatures) {
				row.spans.merge(f, 1, Integer::sum);
				if (count++ == maxFeatures) {
					break;
				}
			}

		}

		return new DefaultDisplayCompositeLayoutProvider() {

			@Override
			public Layout createDetailLayout(final MMXRootObject root, final EObject value) {
				// * 2 as each feature is the label and the editor control
				return new GridLayout(maxFeatures * 2, false);
			}

			@Override
			public Object createEditorLayoutData(final MMXRootObject root, final EObject value, final IInlineEditor editor, final Control control) {

				// Special case for min/max volumes - ensure text box has enough width for around 7 digits.
				// Note: Should really render the font to get width - this is ok on my system, but other systems (default font & size, resolution, dpi etc) could make this wrong
				final var feature = editor.getFeature();

				for (RowBuilder row : rows) {
					if (row.orderedFeatures.contains(feature)) {
						final GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);

						if (row.widthHints.containsKey(feature)) {
							gd.widthHint = row.widthHints.get(feature);
						}
						if (row.label != null) {
							// FIXME: Hack pending proper APi to manipulate labels
							if (feature == row.orderedFeatures.get(0)) {
								final Label label = editor.getLabel();
								if (label != null) {
									label.setText(row.label);
								}
								editor.setLabel(null);
							} else {
								editor.setLabel(null);
							}
						} else if (row.labels.containsKey(feature)) {
							final Label label = editor.getLabel();
							if (label != null) {
								label.setText(row.labels.get(feature));
								// Set as null to avoid later code overwriting the label.
								editor.setLabel(null);
							}
						}
						gd.horizontalSpan = row.spans.get(feature);

						return gd;
					}
				}

				GridData gd = (GridData) super.createEditorLayoutData(root, value, editor, control);
				// This is the GridData for editor control. The label control takes 1 span.
				gd.horizontalSpan = maxFeatures * 2 - 1;
				return gd;
			}
		};
	}
}
