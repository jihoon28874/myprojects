package ngordnet.main;

import ngordnet.hugbrowsermagic.NgordnetQuery;
import ngordnet.hugbrowsermagic.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;

public class HyponymsHandler extends NgordnetQueryHandler {

    private WordNet wn;
    private NGramMap ngm;

    public HyponymsHandler(WordNet graph, NGramMap map) {
        this.wn = graph;
        this.ngm = map;
    }

    @Override
    public String handle(NgordnetQuery q) {
        return wn.wordFormat(q.words(), q.startYear(), q.endYear(), q.k(), ngm);
    }
}
