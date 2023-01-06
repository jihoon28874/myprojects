package ngordnet.main;

import java.util.*;

import java.util.HashMap;


public class Graph {

    //instantiates the hashmap for the graph
    // which it will hold keys of synset integers
    // and values will be the node for that key
    private HashMap<Integer, Node> hashData;

    public Graph() {
        this.hashData = new HashMap<>();
    }

    // discussed data structure implementation
    //with a classmate about the node and whats its going to store
    // node stores a string[] of words from synset file "middle"
    // and the list of Hyponyms from the hyponym file as an Array
    private class Node {
        private String[] synsetWords;
        private ArrayList<Integer> listHyponyms;

        private Node(String[] words) {
            this.synsetWords = words;
            this.listHyponyms = null;
        }

        //function that will add the hyponym
        //to the arraylist<Integer> that is stored
        //in the node for the specific key
        public void addContent(int hyponymID) {
            if (listHyponyms == null) {
                listHyponyms = new ArrayList<>();
            }
            listHyponyms.add(hyponymID);
        }

        //basic helper functions that will return specific
        //things we need when wrapping the graph such as
        //returning the words in the node as well as the
        //ArrayList of Integers of Hyponyms for that Key
        public String[] returnWords() {
            return synsetWords;
        }

        public ArrayList<Integer> returnHypoList() {
            return listHyponyms;
        }
    }

    // adds the edges of getting the keyset
    // and adding the hypo to a arraylist of
    // integers as a children of keyset
    public void addEdge(int synID, int hypoID) {
        hashData.get(synID).addContent(hypoID);
    }

    // adds the node thats storing the string list
    // of words and putting to its corresponding
    // key values
    public void addNode(int synID, String[] words) {
        hashData.put(synID, new Node(words));
    }

    // worked out recursion with someone
    // with project lab
    //recursive algorithm returns a treeset in which
    //it will recur through the Arrays of SynsetIDs
    //and will put all its corresponding children if it has one
    //into a unique treeset of strings of the words
    // dfs traversal
    public TreeSet<String> traverseGraph(ArrayList<Integer> synIDs) {
        TreeSet<String> newHypo = new TreeSet<>();
        if (synIDs != null) {
            for (int item : synIDs) {
                traverseHelper(item, newHypo);
            }
        }
        return newHypo;
    }

    private void traverseHelper(int synID, TreeSet set) {
        for (String i : hashData.get(synID).returnWords()) {
            set.add(i);
        }
        if (hashData.get(synID).returnHypoList() != null) {
            for (int x : hashData.get(synID).returnHypoList()) {
                traverseHelper(x, set);
            }
        }
    }
}

