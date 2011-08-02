/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package statemachinedesigner;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *A trie data structure that is used for generating a graph out of the words list
 * @author Henry
 */
public class DesignTrie {

    private TrieNode head;
    private SimulatorController _controller;
    private GraphDisplayApplet _gda;

    public DesignTrie(SimulatorController sc) {
        _controller =sc;
        head = new TrieNode();
        _gda = new GraphDisplayApplet(this);
    }

    /**
     * adds a word to the trie
     * @param s string representation of the word being added
     */
    public void addPath(String s) {
        Pattern p = Pattern.compile("[\\d]+[\\s]*");
        Matcher m = p.matcher(s);
        if (m.find()) {
            //should grab the first group
            String group = m.group().trim();
            if (head.indexOf(group) > -1) {
                addPathHelper(s.substring(group.length()).trim(), head.children.get(head.indexOf(group)));
                
            } else {
                head.children.add(new TrieNode(group));
                _gda.createVertex(Integer.parseInt(group));
                addPathHelper(s.substring(group.length()).trim(), head.children.get(head.indexOf(group)));

            }
        }
    }

    private void addPathHelper(String s, TrieNode tn) {
        Pattern p = Pattern.compile("[\\d]+[\\s]*");
        Matcher m = p.matcher(s);
        if (m.find()) {
            String group = m.group().trim();
            if (tn.indexOf(group) > -1) {
                addPathHelper(s.substring(group.length()).trim(), tn.children.get(tn.indexOf(group)));
            } else {
                tn.children.add(new TrieNode(group));
                _gda.createEdge(Integer.parseInt(tn.value),Integer.parseInt(group));
                addPathHelper(s.substring(group.length()).trim(), tn.children.get(tn.indexOf(group)));
            }
        }
    }
    public GraphDisplayApplet getView() {
        return _gda;
    }

//    class BreadthFirstIterator implements Iterator {
//
//        private ArrayList<Object> queue;
//
//        BreadthFirstIterator(DesignTrie dt) {
//            queue = new ArrayList<Object>();
//            for (TrieNode tn: dt.head.getChildren()) {
//                queue.add(tn.value);
//            }
//        }
//
//        public boolean hasNext() {
//        if (queue.size()>0) {
//            return true;
//        }
//            return false;
//        }
//
//        public Object next() {
//
//            return null;
//        }
//
//        public void remove() {
//        }
//    }

    class TrieNode {

        private ArrayList<TrieNode> children;
        private String value;
        //constructor for the header

        TrieNode() {
            children = new ArrayList<TrieNode>();
        }

        TrieNode(String s) {
            children = new ArrayList<TrieNode>();
            value = s.trim();
        }

        ArrayList<TrieNode> getChildren() {
            return children;
        }

        int indexOf(String s) {
            if (children.size() > 0) {
                for (int i = 0; i < children.size(); i++) {
                    if (children.get(i).value.equals(s)) {
                        return i;
                    }
                }
            }
            return -1;
        }
    }
}
