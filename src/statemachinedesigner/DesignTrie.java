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
    private TrieNode _current;
    private int vertexCount;

    public DesignTrie(SimulatorController sc) {
        _controller = sc;
        head = new TrieNode();
        _gda = new GraphDisplayApplet(this);
    }

    /**
     * adds a word to the trie
     * @param s string representation of the word being added
     */
    public void addPath(String s) {
        Pattern p = Pattern.compile("[^\\s]+[\\s]*");
        Matcher m = p.matcher(s);
        if (m.find()) {
            //should grab the first group
            String group = m.group().trim();
            if (head.indexOf(group) > -1) {
                addPathHelper(s.substring(group.length()).trim(), head.children.get(head.indexOf(group)));

            } else {
                head.children.add(new TrieNode(group));
//                _gda.createVertex(Integer.parseInt(group));
                addPathHelper(s.substring(group.length()).trim(), head.children.get(head.indexOf(group)));

            }
        }
    }

    /**
     * resets the head of the trie, effectively erasing the tree contents
     */
    public void reset() {
        head = new TrieNode();
    }

    /**
     *
     * @param s is the value of the node that should be deleted
     */
    public void removeNode(String s) {
        System.out.println("removeNode is operating on node: " + s);
        if (head.children.size() > 0) {
            ArrayList<TrieNode> children = new ArrayList<TrieNode>();
            children.addAll(head.children);
            for (TrieNode tn : children) {
                if (tn.value.equals(s)) {

                    for (TrieNode t : tn.children) {
                        head.children.add(t);
                    }
                    head.children.remove(tn);
                    System.out.println(s + " was successfully removed" + head.children.remove(tn));

                } else {
                    System.out.println("calling helper for :" + s);
                    removeNodeHelper(head, tn, s);
                }
            }
        }

    }
    /*
     * TrieNode passed
     */

    private void removeNodeHelper(TrieNode parent, TrieNode current, String s) {
        System.out.println("parent node is:" + parent.value);
        System.out.println("current node is:" + current.value + ": comparing with:" + s + ": so they are equal: " + current.value.equals(s));
        System.out.println("helper is operating on: " + s);
        if (current.value.equals(s)) {

            ArrayList<TrieNode> children = new ArrayList<TrieNode>();
            children.addAll(current.children);
            if (current.children.size() > 0) {
                for (TrieNode tn : children) {
                    parent.children.add(tn);
                }
            }
            System.out.println(s + " was successfully removed" + parent.children.remove(current));
        } else {
            if (current.children.size() > 0) {
                ArrayList<TrieNode> children = new ArrayList<TrieNode>();
                children.addAll(current.children);
                for (TrieNode tn : children) {
                    removeNodeHelper(current, tn, s);
                }
            }
        }

    }

    /**
     * returns a boolean indicating if input s is a valid path in this trie, node values should be separated by a space
     * @param s
     * @return
     */
    public boolean containsPath(String s) {
        TrieNode current = head;
        Pattern p = Pattern.compile("[^\\s]+");
        Matcher m = p.matcher(s);
        ArrayList<String> pathNodes = new ArrayList<String>();
        while (m.find()) {
            pathNodes.add(m.group());
        }
        while (!pathNodes.isEmpty()) {
            if (current.indexOf(pathNodes.get(0)) > -1) {
                current = current.children.get(current.indexOf(pathNodes.get(0)));
                pathNodes.remove(0);
            } else {
                return false;
            }
        }
        return true;
    }

    public void drawGraph() {
        _gda.initGraph();
        vertexCount = 0;
//        int edgeCount=0;
        _gda.createVertex();
        if (head.children.size() > 0) {

            for (TrieNode tn : head.children) {
                vertexCount++;
                _gda.createVertex();
                _gda.createEdge(0, vertexCount, tn.value);
                drawGraphHelper(vertexCount, tn);
            }

        }
    }

    private void drawGraphHelper(int vertexIndex, TrieNode current) {
        if (current.children.size() > 0) {
            for (TrieNode tn : current.children) {
                vertexCount++;
                _gda.createVertex();
                _gda.createEdge(vertexIndex, vertexCount, tn.value);
                drawGraphHelper(vertexCount, tn);
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
//                _gda.createEdge(Integer.parseInt(tn.value),Integer.parseInt(group));
                addPathHelper(s.substring(group.length()).trim(), tn.children.get(tn.indexOf(group)));
            }
        }
    }

    public GraphDisplayApplet getView() {
        return _gda;
    }

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
