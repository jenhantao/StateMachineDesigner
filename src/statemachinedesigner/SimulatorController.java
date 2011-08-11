/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package statemachinedesigner;

import edu.uci.ics.jung.graph.Graph;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Jenhan Tao
 */
public class SimulatorController {

    SimulatorController(SimulatorFrame aThis) {
        _view = aThis;
        _seenStates = new HashMap<String, String>();
        _finalStates = new HashMap<String, String>();
        _unexploredStates = new LinkedList<String>();
        _inputSet = new LinkedList<String>();
        _input = "";
        _designInputs = new HashSet();
        _useRecombinase = true;
        _useInvertase = false;


    }

    /**
     * Starts the simulation and returns the result text to the view, requires input text, which is the design
     * @param
     * @return
     */
    public String startSimulation(String text) {
        String designInput = text.replaceAll("\\n", " ").toUpperCase().trim() + " ";//new lines essentially denote spaces, add space at end by convention
        String[] tokens = designInput.split("\\s");
        boolean containsReporter = false;
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].matches("[pP]{1}[\\d]+[']?")) {
//                System.out.println(text + " contains a promoter site: " + tokens[i]);
            } else if (tokens[i].matches("[iI]{1}[\\d]+[']?")) {
//                System.out.println(text + " contains a invertase recognition site: " + tokens[i]);
            } else if (tokens[i].matches("[@]{1}[\\d]+[']?")) {
//                System.out.println(text + " contains a invertase coding region: " + tokens[i]);
            } else if (tokens[i].matches("[xX]{1}[']?")) {
//                System.out.println(text + " contains a terminator region: " + tokens[i]);
            } else if (tokens[i].matches("[rR]{1}[\\d]+[']?")) {
//                System.out.println(text + " contains a reporter coding region: " + tokens[i]);
                containsReporter = true;
            } else {
//                System.out.println("construct contains invalid component: " + tokens[i]);
                return "construct contains invalid component: " + tokens[i];
            }
        }
        if (!containsReporter) {
            return "no reporter found in construct";
        }
        _inputSet = new LinkedList<String>();
        _seenStates = new HashMap<String, String>();
        _finalStates = new HashMap<String, String>();
        _deadStates = new HashMap<String, String>();
        _unexploredStates = new LinkedList<String>();
        _currentState = designInput;
        _unexploredStates.offer(designInput);
        _seenStates.put("", designInput);
        _input = "";
        _inputSet.offer("");
        runSimulation();

        Collection coll = _finalStates.keySet();
        LinkedList listy = new LinkedList<String>();
        listy.addAll(coll);
        java.util.Collections.sort(listy);
        String toReturn = "Results\n";
        for (Object s : listy) {
            toReturn = toReturn + s + "\n";
        }
        toReturn = toReturn + "\n\nDead States\n";
        coll = _deadStates.keySet();
        listy = new LinkedList<String>();
        listy.addAll(coll);
        java.util.Collections.sort(listy);
        for (Object s : listy) {
            toReturn = toReturn + s + "\n";
        }
        return toReturn;
    }

    private void runSimulation() {

        while (!_unexploredStates.isEmpty()) {
            _currentState = _unexploredStates.poll();
            _input = _inputSet.poll();
            ArrayList<Integer> activePromoters = findActivePromoters(_currentState);
            for (Integer i : activePromoters) {
                activatePromoter(i);
            }
        }

    }

    private ArrayList<Integer> findActivePromoters(String s) {
        String designString = s.replaceAll("\\n", " ").trim();//new lines essentially denote spaces
        String[] tokens = designString.split("\\s");
        ArrayList<Integer> toReturn = new ArrayList<Integer>();
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].matches("[pP]{1}[\\d]+")) {
                try {
                    toReturn.add(Integer.parseInt(tokens[i].substring(1)));
                } catch (NumberFormatException e) {
                    System.out.println("error parsing for valid promoter sites");
                    e.printStackTrace();
                }
            }
        }
        return toReturn;
    }

    private void activatePromoter(int promoterNumber) {
        _promoterNumber = promoterNumber;
        if (_input.indexOf(Integer.toString(promoterNumber)) < 0) { //don't allow repeat use of promoters
            ArrayList<Integer> activatedInvertase = new ArrayList<Integer>();
            ArrayList<Integer> starts = new ArrayList<Integer>();
            ArrayList<Integer> ends = new ArrayList<Integer>();
            int start = _currentState.indexOf("P" + promoterNumber + " ") + ("p" + promoterNumber + 1).length();
            int end = _currentState.indexOf("X ", start);
            while (start > -1) {
                starts.add(start);
                if (end < 0) {
                    end = _currentState.length();
                }
                ends.add(end);
                start = _currentState.indexOf("P" + promoterNumber + " ", start + 1);
                end = _currentState.indexOf("X ", start);
            }
            if (starts.size() > 0 && ends.size() > 0) {
                for (int i = 0; i < Math.min(starts.size(), ends.size()); i++) {
                    String regionOfInterest = _currentState.substring(starts.get(i), ends.get(i));
//                    System.out.println("active region: " + regionOfInterest);
                    Pattern p = Pattern.compile("[rR]{1}[\\d]+");
                    Matcher m = p.matcher(regionOfInterest);
                    while (m.find()) {
//                        System.out.println("expressed reporter: " + m.group());
                        _finalStates.put(_input + " " + _promoterNumber + " " + m.group(), _currentState);

                    }

                    p = Pattern.compile("[@]{1}[\\d]+[^']{1}");
                    m = p.matcher(regionOfInterest);

                    while (m.find()) {
                        activatedInvertase.add(Integer.parseInt(m.group().substring(1).trim()));
                    }
                }
            }
            String newState = null;
            if (_useInvertase) {
                newState = activateInvertase(activatedInvertase);
            } else if (_useRecombinase) {
                newState = activateRecombinase(activatedInvertase);
            } else {
                _view.setTestStatus("Status: Can't run test. Must use either excision or inversion");
                return;
            }
            if (_view.isShowIntermediate()) {
                System.out.println("current state:" + _currentState);
                System.out.println("inputs used:" + _input + " " + _promoterNumber);
                System.out.println("new state:" + newState);
            }
            if (!_seenStates.containsKey(_input + " " + _promoterNumber)) {
                //if the state did not change and did not express a reporter, then it is a state that did nothing, a dead state
                if (newState.equals(_currentState) && !_finalStates.containsValue(_currentState)) {
                    _deadStates.put(_input + " " + _promoterNumber, newState);
                    //dead states only lead to other dead states
                } else if (_deadStates.containsKey(_input)) {
                    _deadStates.put(_input + " " + _promoterNumber, newState);
                }
                _seenStates.put(_input + " " + _promoterNumber, newState);
                if (_view.isFinalReporter()) {
                    if (!_finalStates.containsValue(newState)) {
                        _unexploredStates.add(newState);
                        _inputSet.add(_input + " " + _promoterNumber);
                    }
                } else {
                    _unexploredStates.add(newState);
                    _inputSet.add(_input + " " + _promoterNumber);

                }


            }
        }

    }

    private String activateRecombinase(ArrayList<Integer> activeSites) {
        String newState = _currentState;
        for (int recombinaseNumber : activeSites) {
            String numberString = Integer.toString(recombinaseNumber);
            String temp = "";
            for (int i = 0; i < numberString.length(); i++) {
                temp = temp + "[" + numberString.substring(i, i + 1) + "]{1}";
            }
            Pattern p = Pattern.compile("[iI]{1}" + temp + "[^']{1}");
            Matcher m = p.matcher(newState);
            ArrayList<Integer> starts = new ArrayList();
            while (m.find()) {
                starts.add(m.start());
            }
            for (int i = 0; i < starts.size() - 1; i++) {
                //this loop performs the excisions
                newState = newState.substring(0, starts.get(i)) + newState.substring(starts.get(i + 1));
            }
        }
        return newState;

    }

    private String activateInvertase(ArrayList<Integer> activeSites) {
        String newState = _currentState;
        for (int invertaseNumber : activeSites) {
            String numberString = Integer.toString(invertaseNumber);
            String temp = "";
            for (int i = 0; i < numberString.length(); i++) {
                temp = temp + "[" + numberString.substring(i, i + 1) + "]{1}";
            }
            Pattern p = Pattern.compile("[iI]{1}" + temp + "[\\s]{1}");
            Matcher m = p.matcher(newState);
            ArrayList<Integer> starts = new ArrayList<Integer>();
            ArrayList<Integer> ends = new ArrayList<Integer>();
            while (m.find()) {
                starts.add(m.end());
            }
            p = Pattern.compile("[iI]{1}" + temp + "[']{1}");
            m = p.matcher(newState);
            while (m.find()) {
                ends.add(m.start());
            }
            if (Math.min(starts.size(), ends.size()) > 0) {
                for (int i = 0; i < Math.min(starts.size(), ends.size()); i++) {
                    String first = newState.substring(0, starts.get(i));
                    String second = newState.substring(starts.get(i) - 1, ends.get(i));
                    String last = newState.substring(ends.get(i));
                    temp = "";
                    String[] tokens = second.split("\\s");
                    for (int j = 0; j < tokens.length; j++) {
                        if (tokens[j].matches("[^']+[']{1}")) {
                            temp = tokens[j].substring(0, tokens[j].length() - 1) + " " + temp;
                        } else if (tokens[j].matches("[^']+")) {
                            temp = tokens[j] + "'" + " " + temp;
                        }
                    }
                    if (first.endsWith(" ")) {
                        if (temp.startsWith(" ")) {
                            temp = temp.substring(1);
                        }
                    } else {
                        if (!temp.startsWith(" ")) {
                            temp = " " + temp;
                        }
                    }
                    if (last.startsWith(" ")) {
                        if (temp.endsWith(" ")) {
                            last = last.substring(1);
                        }
                    } else {
                        if (!temp.endsWith(" ")) {
                            temp = temp + " ";
                        }
                    }
                    int oldLength = newState.length();
                    newState = (first + temp + last);
                    int lengthDifference = newState.length() - oldLength;
                    for (int k = 0; k < starts.size(); k++) {
                        starts.set(k, starts.get(k) + lengthDifference);

                    }
                    for (int k = 0; k < ends.size(); k++) {
                        ends.set(k, ends.get(k) + lengthDifference);
                    }
                }
            }
        }
        return newState;

    }

    public void changeInvertaseUse() {
        if (_useInvertase) {
            _useInvertase = false;
        } else {
            _useInvertase = true;
        }
    }

    public void changeRecombinaseUse() {
        if (_useRecombinase) {
            _useRecombinase = false;
        } else {
            _useRecombinase = true;
        }
    }

    public String generateDesign(Graph g) {
        _design="";
        _undesignedStates = new LinkedList<Integer>();
        _touchedDesignStates = new LinkedList<Integer>();
        ArrayList<Integer> vertices = new ArrayList<Integer>();

        vertices.addAll(g.getVertices());

        for (Integer i : vertices) {
            if (g.getNeighborCount(i) == 0) {
                g.removeVertex(i);
            }
        }
        vertices.clear();
        vertices.addAll(g.getVertices());

        //at this point only vertices that have a neighbor should remain
        if (!g.containsVertex(0)) {
            return "error: design must include vertex 0, the initial state";
        }
//        for (Integer i : vertices) {
//            _touchedDesignStates.add(i);
//        }
//        for (Integer i : vertices) {
//            findParents(g, i);
//        }
        _touchedDesignStates.add(0);
        _undesignedStates.add(0);
        while (!_undesignedStates.isEmpty()) {
            _currentVertex = _undesignedStates.poll();
            _design=_design+generateDesignHelper(g);
        }



        return null;
    }

    private String generateDesignHelper(Graph g) {
System.out.println(_currentVertex);
        String toAdd="X X' P"+_currentVertex;
        
        
//        System.out.println("touched states");
//        for (Integer i : _touchedDesignStates) {
//            System.out.print(i + ", ");
//        }
//        System.out.println();
        ArrayList<Integer> neighbors = new ArrayList<Integer>();
        neighbors.addAll(g.getNeighbors(_currentVertex));
//        System.out.println(_currentVertex + " has neighbors: ");
//        for (Integer i : neighbors) {
//            System.out.print(i + ", ");
//        }
//        System.out.println();
        ArrayList<Integer> parents = findParents(g, _currentVertex);
//        System.out.println(_currentVertex + " has parents: ");
        for (Integer i : parents) {
//            System.out.print(i + ", ");
            PromoterEdge edge =(PromoterEdge) g.findEdge(i, _currentVertex);
            if (edge!=null) {
            toAdd=flankElementWith(toAdd, "P"+edge.getWeight(),"I"+edge.getWeight());
            }
            if (i!=0) {
                
            toAdd=toAdd+"X X'";
            }
            
            neighbors.remove(i);
        }
        
        for (Integer i:neighbors) {
            if(!_touchedDesignStates.contains(i)) {
                _touchedDesignStates.add(i);
                _undesignedStates.add(i);
            }
        }


        return null;
    }

    private ArrayList<Integer> findParents(Graph g, Integer vertex) {

        ArrayList<Integer> parents = new ArrayList<Integer>();
        int vertexIndex = _touchedDesignStates.indexOf(vertex);
        if (vertexIndex > 0) {
            for (int i = 0; i < vertexIndex; i++) {
                if (g.getNeighbors(_touchedDesignStates.get(i)).contains(vertex)) {
                    parents.add(_touchedDesignStates.get(i));
                }
            }
        }

        return parents;
    }

    public String flankElementWith(String design, String target, String flanker) {
        int targetIndex = design.indexOf(target);
        String toReturn = design.substring(0, targetIndex) + " " + flanker + " " + design.substring(targetIndex, targetIndex + target.length())
                + " " + flanker + " " + design.substring(targetIndex + target.length());




        return toReturn;

    }
    private class StateMachineNode {
        String _module;
        private StateMachineNode(String s) {
            _module=s;
        }
    }
    private ArrayList<StateMachineNode> _nodes;
    private int _promoterCount;
    private String _design;
    private int _currentVertex;
    private LinkedList<Integer> _touchedDesignStates;
    private LinkedList<Integer> _undesignedStates;
    private GraphDisplayApplet _gda;
    private HashSet<String> _designInputs;
    private HashMap<String, String> _deadStates;
    private SimulatorFrame _view;
    private HashMap<String, String> _seenStates;
    private HashMap<String, String> _finalStates;
    private LinkedList<String> _unexploredStates;
    private String _currentState;
    private String _input;
    private LinkedList<String> _inputSet;
    private int _promoterNumber;
    private boolean _useInvertase;
    private boolean _useRecombinase;
}
