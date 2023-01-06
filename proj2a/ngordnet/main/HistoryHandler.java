package ngordnet.main;

import ngordnet.hugbrowsermagic.NgordnetQuery;
import ngordnet.hugbrowsermagic.NgordnetQueryHandler;
import ngordnet.ngrams.TimeSeries;
import ngordnet.plotting.Plotter;
import org.knowm.xchart.XYChart;
import ngordnet.ngrams.NGramMap;

import java.util.ArrayList;


public class HistoryHandler extends NgordnetQueryHandler {
    private NGramMap gramMap;

    public HistoryHandler(NGramMap map) {
        gramMap = map;
    }

    @Override
    public String handle(NgordnetQuery q) {
        ArrayList<TimeSeries> lts = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        HistoryTextHandler temp = new HistoryTextHandler(gramMap);
        System.out.println(temp.handle(q));
        for (String i : q.words()) {
            TimeSeries thisTime = gramMap.weightHistory(i, q.startYear(), q.endYear());
            labels.add(i);
            lts.add(thisTime);
        }

        XYChart chart = Plotter.generateTimeSeriesChart(labels, lts);
        String encodedImage = Plotter.encodeChartAsString(chart);

        return encodedImage;
    }
}
