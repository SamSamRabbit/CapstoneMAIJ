/**
 * 
 */
package metricAnalysis;

import java.util.List;

import com.github.javaparser.Range;

/**Class to store the calculated result of a metric on a node
 * @author Jacob Botha
 *
 */
public class MetricResult {
	private String metricIdentifier;
	private Double value;
	private List<Range> pointsOfInterest;
	
	
	/** constructor that take the metric identifier and the calculated value of the metric.
	 * @param identifier2
	 * @param i
	 */
	public MetricResult(String identifier, Double i) {
		metricIdentifier = identifier;
		value = i;
		pointsOfInterest = null;
	}

	/**Constructor that also accepts an array of ranges to store points of interests
	 * that affected the value of the metric.
	 * @param identifier
	 * @param i
	 * @param poi
	 */
	public MetricResult(String identifier, Double i, List<Range> poi) {
		metricIdentifier = identifier;
		value = i;
		pointsOfInterest = poi;
	}
	
	/**
	 * @return the metricIdentifier
	 */
	public String getMetricIdentifier() {
		return metricIdentifier;
	}
	
	/**List of ranges that affected the metric value
	 * @return the pointsOfInterest
	 */
	public List<Range> getPointsOfInterest() {
		return pointsOfInterest;
	}
	
	
	/**
	 * @return the value
	 */
	public Double getValue() {
		return value;
	}
	
	/**Checks whether there are any points of interest recorded for this metric
	 * @return true if the Points of interest array is set and non-empty
	 */
	public boolean hasPointsOfInterest() {
		if(pointsOfInterest == null || pointsOfInterest.isEmpty()) {
			return false;
		}else {
			return true;
		}
	}

}
