package ngordnet.main;

import java.util.*;

import edu.princeton.cs.algs4.In;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;

import java.util.HashMap;

import java.util.Set;

public class WordNet {
    // wrapper for a graph
    // one class is a
    private Graph G;
    //Hashmap within wordnet that actually links the string
    //to its corresponding synset ID and can hold mulitple
    // because it is an arraylist
    private HashMap<String, ArrayList<Integer>> wordLink;

    private class Node {

        private int countsOfWords;
        private String word;

        private Node(String word, int counts) {
            this.word = word;
            this.countsOfWords = counts;
        }
    }

    public class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.countsOfWords - o2.countsOfWords;
        }
    }

    //parses out the file and stores into their
    //corresponding data structure
    public WordNet(String synsetsFile, String hyponymsFile) {
        In synFile = new In(synsetsFile);
        In hypoFile = new In(hyponymsFile);
        String syn = new String();
        String hypo = new String();
        G = new Graph();
        wordLink = new HashMap();
        while (synFile.hasNextLine()) {
            syn = synFile.readLine();
            String[] synArr = syn.split(",");
            String[] synWord = synArr[1].split(" ");
            for (String word : synWord) {
                if (wordLink.containsKey(word)) {
                    wordLink.get(word).add(Integer.parseInt(synArr[0]));
                } else {
                    wordLink.put(word, new ArrayList<>());
                    wordLink.get(word).add(Integer.parseInt(synArr[0]));
                }
            }
            G.addNode(Integer.parseInt(synArr[0]), synWord);
        }
        while (hypoFile.hasNextLine()) {
            hypo = hypoFile.readLine();
            String[] hypoArr = hypo.split(",");
            int edge = Integer.parseInt(hypoArr[0]);
            for (int i = 1; i < hypoArr.length; i++) {
                G.addEdge(edge, Integer.parseInt(hypoArr[i]));
            }
        }
    }

    //retainAll()  list call [1,2,3,4,5]
    // [1,2,3]
    // result on retainAll [1,2,3]
    public String wordFormat(List<String> words, int startYear, int endYear, int k, NGramMap ngm) {
        if (words.isEmpty()) {
            return "[]";
        }
        TreeSet<String> travSet = G.traverseGraph(wordLink.get(words.get(0)));
        //retain all function in which it will remove words in which there is
        //no overlap between the other words that is passed in the list in multiple words
        for (int item = 1; item < words.size(); item++) {
            travSet.retainAll(G.traverseGraph(wordLink.get(words.get(item))));
        }
        //Priorityqueue storing node discussed with project lab partner
        //PriorityQueue that holds the size of the words and the comparator
        //Calls timeseries gets the counts of the word and adds on to the queue
        //Queue then removes each lowest value according to the user input of k
        if (travSet.size() > 0 & k > 0) {
            PriorityQueue<Node> queue = new PriorityQueue<>(travSet.size(), new NodeComparator());
            for (String i : travSet) {
                TimeSeries ts = ngm.countHistory(i,startYear,endYear);
                int counts = 0;
                if (!ts.isEmpty()) {
                    for (int item : ts.years()) {
                        counts += ts.get(item);
                    }
                    int count = counts;
                    queue.add(new Node(i, count));
                }
            }
            int countsInc = queue.size();
            if (countsInc - k > 0) {
                for (int i = 0; i < countsInc - k; i++) {
                    if (!queue.isEmpty())
                        queue.remove();
                }
            }
            travSet.clear();
            for (Node item : queue) {
                travSet.add(item.word);
            }
        }
        Arrays.sort(travSet.toArray());
        return travSet.toString();
    }

}

// graph helper functions
