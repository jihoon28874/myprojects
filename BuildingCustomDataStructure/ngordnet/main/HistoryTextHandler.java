package ngordnet.main;

import ngordnet.hugbrowsermagic.NgordnetQuery;
import ngordnet.hugbrowsermagic.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;

import java.util.List;

public class HistoryTextHandler extends NgordnetQueryHandler {
    private NGramMap gramMap;

    public HistoryTextHandler(NGramMap map) {
        gramMap = map;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        String response = "";
        for (String i : words) {
            TimeSeries timeMap = gramMap.weightHistory(i, q.startYear(), q.endYear());
            response += i + ":" + " ";
            response += timeMap.toString();
            response += "\n";
        }
        return response;
    }
}
