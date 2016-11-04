package com.mmxlabs.models.lng.analytics.ui.views.providers;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.analytics.AnalysisResultRow;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.MultipleResultGrouper;
import com.mmxlabs.models.lng.analytics.MultipleResultGrouperRow;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.ResultSet;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.BuyOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.SellOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.ShippingOptionDescriptionFormatter;

public class ResultsViewerContentProvider implements ITreeContentProvider {

	// WIP
	public class GroupNode {
		String name;
		MultipleResultGrouper keyA;
		Object keyB;
		GroupNode parentGroup;
		GroupNode[] childNodes;
		List<ResultSet> childResults;
	}

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

	}

	@Override
	public Object[] getElements(final Object inputElement) {

		if (inputElement instanceof OptionAnalysisModel) {
			final OptionAnalysisModel model = (OptionAnalysisModel) inputElement;

			if (model.getResultGroups().isEmpty()) {
				// Convert to flat list - we only expect a single result set
				final List<AnalysisResultRow> l = new LinkedList<>();
				for (final ResultSet rs : model.getResultSets()) {
					l.addAll(rs.getRows());
				}

				return l.toArray();
			}

			// Build result tree
			final GroupNode[] nodes = buildGroups(model, null, 0);
			// Collapse to a list of leaf nodes
			final List<GroupNode> collapsedNodes = new LinkedList<>();
			for (final GroupNode n : nodes) {
				collapse(n, collapsedNodes);
			}
			return collapsedNodes.toArray();
		}
		return new Object[0];
	}

	private void collapse(final GroupNode n, final List<GroupNode> l) {
		if (n.childNodes != null) {
			for (final GroupNode c : n.childNodes) {
				collapse(c, l);
			}
		} else {
			l.add(n);
		}
	}

	public GroupNode[] buildGroups(final OptionAnalysisModel model, final GroupNode parent, final int depth) {
		final EList<MultipleResultGrouper> resultGroups = model.getResultGroups();
		if (depth == resultGroups.size()) {
			return null;
		}

		final MultipleResultGrouper g = resultGroups.get(depth);
		List<ResultSet> l;
		if (parent != null) {
			l = Lists.newArrayList(parent.childResults);
		} else {
			l = new LinkedList<>(model.getResultSets());
		}
		final List<GroupNode> children = new LinkedList<>();
		for (final MultipleResultGrouperRow f : g.getGroupResults()) {
			final GroupNode n = new GroupNode();
			n.name = format(f.getObject());
			n.keyA = g;
			n.keyB = f;
			n.parentGroup = parent;

			final Set<ResultSet> childResultsSet = new LinkedHashSet<>(f.getGroupResults());
			childResultsSet.retainAll(l);

			n.childResults = new LinkedList<>(childResultsSet);
			n.childNodes = buildGroups(model, n, depth + 1);
			children.add(n);
		}

		return children.toArray(new GroupNode[children.size()]);
	}

	private String format(final Object f) {
		if (f instanceof BuyOption) {
			final BuyOptionDescriptionFormatter b = new BuyOptionDescriptionFormatter();
			return b.render(f);
		}
		if (f instanceof SellOption) {
			final SellOptionDescriptionFormatter b = new SellOptionDescriptionFormatter();
			return b.render(f);
		}
		if (f instanceof ShippingOption) {
			final ShippingOptionDescriptionFormatter b = new ShippingOptionDescriptionFormatter();
			return b.render(f);
		}

		return f.toString();
	}

	private boolean matches(final ResultSet rs, final Object f) {
		for (final AnalysisResultRow r : rs.getRows()) {
			if (r.getBuyOption() == f) {
				return true;
			}
			if (r.getSellOption() == f) {
				return true;
			}
			if (r.getShipping() == f) {
				return true;
			}
		}

		return false;
	}

	@Override
	public Object[] getChildren(final Object parentElement) {
		if (parentElement instanceof ResultSet) {
			final ResultSet model = (ResultSet) parentElement;
			return model.getRows().toArray();
		}
		if (parentElement instanceof GroupNode) {
			final GroupNode groupNode = (GroupNode) parentElement;
			if (groupNode.childNodes != null) {
				return groupNode.childNodes;
			} else if (groupNode.childResults != null) {
				final List<AnalysisResultRow> l = new LinkedList<>();
				for (final ResultSet rs : groupNode.childResults) {
					l.addAll(rs.getRows());
				}

				return l.toArray();
			}
		}
		return new Object[0];
	}

	@Override
	public Object getParent(final Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(final Object element) {
		if (element instanceof OptionAnalysisModel) {
			return true;
		}
		if (element instanceof ResultSet) {
			return true;
		}
		if (element instanceof GroupNode) {
			final GroupNode groupNode = (GroupNode) element;
			if (groupNode.childNodes != null) {
				return true;
			} else if (groupNode.childResults != null) {
				return true;
			}
		}
		return false;
	}

}
