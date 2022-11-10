package com.mmxlabs.scheduler.optimiser.exposures.calculators;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.astnodes.VolumeTierASTNode;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.exposures.Constant;
import com.mmxlabs.scheduler.optimiser.exposures.ExposureRecords;
import com.mmxlabs.scheduler.optimiser.exposures.IExposureNode;
import com.mmxlabs.scheduler.optimiser.exposures.InputRecord;

public class VolumeTierFunctionExposuresCalculator {

	public static Pair<Long, IExposureNode> getExposureNode(@NonNull final VolumeTierASTNode volumeTierNode, final InputRecord inputRecord) {

		final long totalVolumeInMMBTU = inputRecord.volumeInMMBTU();
		long lowVolumeInMMBTU;
		long highVolumeInMMBTU;
		if (volumeTierNode.isM3Volume()) {
			final long thresholdInM3 = OptimiserUnitConvertor.convertToInternalVolume(volumeTierNode.getThreshold());
			final long thresholdInMMBtu = Calculator.convertM3ToMMBTu(thresholdInM3, inputRecord.cargoCV());

			lowVolumeInMMBTU = Math.min(totalVolumeInMMBTU, thresholdInMMBtu);
			highVolumeInMMBTU = Math.max(0, lowVolumeInMMBTU);
		} else {
			lowVolumeInMMBTU = Math.min(totalVolumeInMMBTU, OptimiserUnitConvertor.convertToInternalVolume(volumeTierNode.getThreshold()));
			highVolumeInMMBTU = Math.max(0, lowVolumeInMMBTU);
		}

		final ExposureRecords records = new ExposureRecords();
		long value = 0;
		{
			final Pair<Long, IExposureNode> p = ExposuresASTToCalculator.getExposureNode(volumeTierNode.getLowTier(), inputRecord.withVolume(lowVolumeInMMBTU));

			// Shortcut if there is no high tier volume
			if (highVolumeInMMBTU == 0) {
				return p;
			}

			if (p.getSecond() instanceof final ExposureRecords result) {
				value += Calculator.costFromConsumption(lowVolumeInMMBTU, p.getFirst());
				records.records.addAll(result.records);
			} else if (p.getSecond() instanceof Constant) {
				return new Pair<>(p.getFirst(), p.getSecond());
			}
		}
		{
			final Pair<Long, IExposureNode> p = ExposuresASTToCalculator.getExposureNode(volumeTierNode.getHighTier(), inputRecord.withVolume(highVolumeInMMBTU));
			if (p.getSecond() instanceof final ExposureRecords result) {
				value += Calculator.costFromConsumption(highVolumeInMMBTU, p.getFirst());
				records.records.addAll(result.records);
			} else if (p.getSecond() instanceof Constant) {
				return new Pair<>(p.getFirst(), p.getSecond());
			}
		}
		// Compute weighted price
		return new Pair<>((long) Calculator.getPerMMBTuFromTotalAndVolumeInMMBTu(value, inputRecord.volumeInMMBTU()), records);
	}

}
