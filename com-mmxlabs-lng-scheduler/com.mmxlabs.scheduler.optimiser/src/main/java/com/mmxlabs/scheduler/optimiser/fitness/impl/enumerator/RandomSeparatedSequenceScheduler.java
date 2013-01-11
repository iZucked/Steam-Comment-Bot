/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator;


/**
 * A subclass of {@link SeparatedSequenceScheduler} which randomly searches each sub-sequence independently. Produces better results than an equivalent quantity of sampling, and could be pretty quick
 * if we partially planned/evaluated the sequence.
 * 
 * We could do that by feeding small 'fake' routes to the ScheduleEvaluator I guess.
 * 
 * @author hinton
 * 
 */
public final class RandomSeparatedSequenceScheduler extends SeparatedSequenceScheduler {
	// private long randomSeed = 1;
	// private int maxSamples = 1024;
	// private int minSamples = 256;
	// private double sampleProportion = 0.1;
	//
	// public int getMaxSamples() {
	// return maxSamples;
	// }
	//
	// public void setMaxSamples(int maxSamples) {
	// this.maxSamples = maxSamples;
	// }
	//
	// public int getMinSamples() {
	// return minSamples;
	// }
	//
	// public void setMinSamples(int minSamples) {
	// this.minSamples = minSamples;
	// }
	//
	// public double getSampleProportion() {
	// return sampleProportion;
	// }
	//
	// public void setSampleProportion(double sampleProportion) {
	// this.sampleProportion = sampleProportion;
	// }
	//
	// private int[] bestArrivalTimes;
	//
	// public long getRandomSeed() {
	// return randomSeed;
	// }
	//
	// public void setRandomSeed(long randomSeed) {
	// this.randomSeed = randomSeed;
	// }
	//
	// private final Random random = new Random();
	//
	// @Override
	// public Pair<Integer, List<VoyagePlan>> schedule(IResource resource,
	// ISequence sequence) {
	// // reset random seed
	// random.setSeed(randomSeed);
	//
	// return super.schedule(resource, sequence);
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.
	// * SeparatedSequenceScheduler#optimiseSubSequence(int, int)
	// */
	// @Override
	// protected void optimiseSubSequence(final int chunkStart, final int
	// chunkEnd) {
	// final int approximateCombinations = (int) getApproximateCombinations(
	// chunkStart, chunkEnd, maxSamples);
	//
	// final int sample = Math.max(minSamples,
	// Math.min(maxSamples, (int) sampleProportion * approximateCombinations));
	//
	// if (sample <= minSamples) {
	// enumerateSubSequence(chunkStart, chunkEnd);
	// } else {
	// sampleSubSequence(chunkStart, chunkEnd, sample);
	// }
	// // copy the best arrival times so far back again.
	// System.arraycopy(bestArrivalTimes, 0, arrivalTimes, 0,
	// arrivalTimes.length);
	// }
	//
	// private void enumerateSubSequence(final int chunkStart, final int
	// chunkEnd) {
	// if (chunkStart > chunkEnd) {
	// evaluate();
	// } else {
	// final int minValue = getMinArrivalTime(chunkStart);
	// final int maxValue = getMaxArrivalTime(chunkStart);
	//
	// for (int i = minValue; i<=maxValue; i++) {
	// arrivalTimes[chunkStart] = i;
	// enumerateSubSequence(chunkStart + 1, chunkEnd);
	// }
	// }
	// }
	//
	// @Override
	// protected boolean evaluate() {
	// final boolean b = super.evaluate();
	// if (b) {
	// System.arraycopy(arrivalTimes, 0, bestArrivalTimes, 0,
	// arrivalTimes.length);
	// }
	// return b;
	// }
	//
	// private final void sampleSubSequence(final int chunkStart,
	// final int chunkEnd, final int tests) {
	// for (int counter = 0; counter < tests; counter++) {
	// for (int i = chunkStart; i <= chunkEnd; i++) {
	// final int minTime = getMinArrivalTime(i);
	// final int maxTime = getMaxArrivalTime(i);
	//
	// arrivalTimes[i] = RandomHelper.nextIntBetween(random, minTime,
	// maxTime);
	// }
	// evaluate();
	// }
	// }
	//
	// @Override
	// protected boolean initializeArrivalTimes() {
	// bestArrivalTimes = arrivalTimes.clone();
	// for (int j = 0; j < 500; j++) {
	// for (int i = 0; i < arrivalTimes.length; i++) {
	// final int minTime = getMinArrivalTime(i);
	// final int maxTime = getMaxArrivalTime(i);
	//
	// arrivalTimes[i] = RandomHelper.nextIntBetween(random, minTime,
	// maxTime);
	// }
	// if (evaluate()) {
	// return true;
	// }
	// }
	// System.err.println("Bork! could not find any valid arrival times");
	// return false;
	// }

}
