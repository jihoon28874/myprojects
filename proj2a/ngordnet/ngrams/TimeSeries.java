package ngordnet.ngrams;

import java.util.*;

/**
 * An object for mapping a year number (e.g. 1996) to numerical data. Provides
 * utility methods useful for data analysis.
 *
 * @author Josh Hug
 */
public class TimeSeries extends TreeMap<Integer, Double> {
    /**
     * Constructs a new empty TimeSeries.
     */
    public TimeSeries() {
        super();
    }

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     * inclusive of both end points.
     */
    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        super();
        if (ts == null) {
            return;
        } else {
            for (int i : ts.keySet()) {
                if (i >= startYear && i <= endYear) {
                    ts.put(i, ts.get(i));
                }
            }
        }
    }


    /**
     * Returns all years for this TimeSeries (in any order).
     */
    public List<Integer> years() {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i : this.keySet()) {
            result.add(i);
        }
        return result;
    }

    /**
     * Returns all data for this TimeSeries (in any order).
     * Must be in the same order as years().
     */
    public List<Double> data() {
        ArrayList<Double> result = new ArrayList<>();
        for (int i : this.years()) {
            result.add(this.get(i));
        }
        return result;
    }

    /**
     * Returns the yearwise sum of this TimeSeries with the given TS. In other words, for
     * each year, sum the data from this TimeSeries with the data from TS. Should return a
     * new TimeSeries (does not modify this TimeSeries).
     */
    public TimeSeries plus(TimeSeries ts) {
        TimeSeries newTime = new TimeSeries();
        TreeSet<Integer> newTree = new TreeSet();
        newTree.addAll(this.keySet());
        newTree.addAll(ts.keySet());
        for (int i : newTree) {
            if (this.containsKey(i) && ts.containsKey(i)) {
                double sumVal = this.get(i) + ts.get(i);
                newTime.put(i, sumVal);
            } else if (this.containsKey(i)) {
                newTime.put(i, this.get(i));
            } else if (ts.containsKey(i)) {
                newTime.put(i, ts.get(i));
            }
        }
        return newTime;
    }


    /**
     * Returns the quotient of the value for each year this TimeSeries divided by the
     * value for the same year in TS. If TS is missing a year that exists in this TimeSeries,
     * throw an IllegalArgumentException. If TS has a year that is not in this TimeSeries, ignore it.
     * Should return a new TimeSeries (does not modify this TimeSeries).
     */
    public TimeSeries dividedBy(TimeSeries ts) {
        TimeSeries division = new TimeSeries();
        for (int i : this.keySet()) {
            if (!ts.containsKey(i)) {
                throw new IllegalArgumentException();
            }
            double divided = this.get(i) / ts.get(i);
            division.put(i, divided);
        }
        return division;
    }
}
