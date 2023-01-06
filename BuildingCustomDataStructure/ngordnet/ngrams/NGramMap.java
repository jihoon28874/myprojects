package ngordnet.ngrams;

import java.util.Collection;
import java.util.HashMap;

import edu.princeton.cs.algs4.In;


/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 * <p>
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {
    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    private HashMap<String, TimeSeries> hashData;
    private TimeSeries timeMap;

    public NGramMap(String wordsFilename, String countsFilename) {
        timeMap = new TimeSeries();
        hashData = new HashMap();
        In readFile = new In(wordsFilename);
        In totalCounts = new In(countsFilename);
        String thisLine = new String();
        String thatLine = new String();
        while (totalCounts.hasNextLine()) {
            thisLine = totalCounts.readLine();
            String[] valueArr = thisLine.split(",");
            timeMap.put(Integer.parseInt(valueArr[0]), Double.parseDouble(valueArr[1]));
        }
        while (readFile.hasNextLine()) {
            thatLine = readFile.readLine();
            String[] strArr = thatLine.split("\t");
            if (hashData.containsKey(strArr[0])) {
                hashData.get(strArr[0]).put(Integer.parseInt(strArr[1]), Double.parseDouble(strArr[2]));
            } else {
                TimeSeries timeSer = new TimeSeries();
                timeSer.put(Integer.parseInt(strArr[1]), Double.parseDouble(strArr[2]));
                hashData.put(strArr[0], timeSer);
            }
        }
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy,
     * not a link to this NGramMap's TimeSeries. In other words, changes made
     * to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    public TimeSeries countHistory(String word) {
        TimeSeries wordTime = new TimeSeries();
        TimeSeries newTime = (TimeSeries) hashData.get(word);
        for (int i : newTime.keySet()) {
            wordTime.put(i, newTime.get(i));
        }
        return wordTime;
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other words,
     * changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        TimeSeries wordTime = new TimeSeries();
        TimeSeries newTime = (TimeSeries) hashData.get(word);
        for (int i : newTime.keySet()) {
            if (i >= startYear && i <= endYear) {
                wordTime.put(i, newTime.get(i));
            }
        }
        return wordTime;
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        return timeMap;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to
     * all words recorded in that year.
     */
    public TimeSeries weightHistory(String word) {
        return countHistory(word).dividedBy(timeMap);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        return countHistory(word, startYear, endYear).dividedBy(timeMap);
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        TimeSeries newTime = new TimeSeries();
        for (String i : words) {
            newTime = newTime.plus(weightHistory(i));
        }
        return newTime;
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS
     * between STARTYEAR and ENDYEAR, inclusive of both ends. If a word does not exist in
     * this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        TimeSeries newTime = new TimeSeries();
        for (String i : words) {
            if (newTime.isEmpty()) {
                newTime = weightHistory(i, startYear, endYear);
            } else {
                newTime = newTime.plus(weightHistory(i, startYear, endYear));
            }
        }
        return newTime;
    }
}
