package com.mmxlabs.demo.reports.views;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.optimiser.IOptimisationContext;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.fitness.impl.FitnessComponentInstantiator;
import com.mmxlabs.optimiser.fitness.impl.FitnessHelper;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.fitness.IAnnotatedSolution;

/**
 * Content provider for the {@link CargoReportView}.
 * 
 * @author Simon Goodall
 * 
 */
public class FitnessContentProvider implements IStructuredContentProvider {

	public class RowData {
		RowData(String c, long f) {
			this.component = c;
			this.fitness = f;
		}

		public String component;
		public long fitness;
	}

	private RowData[] rowData = new RowData[0];

	@Override
	public Object[] getElements(final Object inputElement) {

		return rowData;
	}

	@Override
	public synchronized void inputChanged(final Viewer viewer, final Object oldInput,
			final Object newInput) {

		IAnnotatedSolution<ISequenceElement> solution = null;
		if (newInput instanceof IAnnotatedSolution) {
			solution = (IAnnotatedSolution) newInput;
		}

		// Process input dats.
		if (solution == null) {
			rowData = new RowData[0];
			return;
		}

		IOptimisationContext<ISequenceElement> context = solution.getContext();

		Map<String, Long> result = createFitnessEvaluator(context,
				solution.getSequences());
		rowData = new RowData[result.size() + 1];

		int idx = 0;
		long total = 0;
		for (final Map.Entry<String, Long> entry : result.entrySet()) {

			rowData[idx++] = new RowData(entry.getKey(), entry.getValue());
			total += entry.getValue();
		}
		rowData[idx] = new RowData("Total", total);
	}

	@Override
	public void dispose() {

	}

	<T> Map<String, Long> createFitnessEvaluator(
			IOptimisationContext<T> context, ISequences<T> state) {

		final FitnessComponentInstantiator fitnessComponentInstantiator = new FitnessComponentInstantiator();
		final List<IFitnessComponent<T>> fitnessComponents = fitnessComponentInstantiator
				.instantiateFitnesses(context.getFitnessFunctionRegistry(),
						context.getFitnessComponents());

		final FitnessHelper<T> fitnessHelper = new FitnessHelper<T>();

		fitnessHelper.initFitnessComponents(fitnessComponents, context.getOptimisationData());
		
		fitnessHelper.evaluateSequencesFromComponents(state, fitnessComponents);

		Map<String, Long> result = new HashMap<String, Long>();
		for (IFitnessComponent<T> c : fitnessComponents) {
			result.put(c.getName(), c.getFitness());
		}
		return result;

	}
}
