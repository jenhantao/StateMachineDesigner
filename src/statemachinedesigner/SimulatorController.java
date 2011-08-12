/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package statemachinedesigner;

import edu.uci.ics.jung.graph.Graph;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Jenhan Tao
 */
public class SimulatorController implements Comparator {

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
        for (int i = 0; i < tokens.length - 1; i++) {
            boolean shouldAdd = false;
            if (tokens[i].matches("[pP]{1}[\\d]+") && !tokens[i + 1].matches("[xX]{1}[.]*")) {
                for (int j = i + 1; j < tokens.length; j++) {
                    if (tokens[j].matches("[rR]{1}[\\d]+") || tokens[j].matches("[@]{1}[\\d]+")) {
                        shouldAdd = true;
                        break;
                    } else if (tokens[j].matches("[xX]{1}[^']*")) {
                        shouldAdd = false;
                        break;
                    }
                }
                if (shouldAdd) {
                    try {
                        toReturn.add(Integer.parseInt(tokens[i].substring(1)));
                    } catch (NumberFormatException e) {
                        System.out.println("error parsing for valid promoter sites");
                        e.printStackTrace();
                    }
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
                    Pattern p = Pattern.compile("[rR]{1}[\\d]+");
                    Matcher m = p.matcher(regionOfInterest);
                    while (m.find()) {
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
            while (starts.size() > 1) {
                newState = newState.substring(0, starts.get(0)) + newState.substring(starts.get(1));
                p = Pattern.compile("[iI]{1}" + temp + "[^'\\d]{1}");
                m = p.matcher(newState);
                starts = new ArrayList();
                while (m.find()) {
                    starts.add(m.start());
                }
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
        _graph = g;
        _nodes = new ArrayList<StateMachineNode>();
//        _undesignedStates = new LinkedList<Integer>();
//        _touchedDesignStates = new LinkedList<Integer>();
        _edgeSet = new ArrayList<PromoterEdge>();
        _edgeSet.addAll(_graph.getEdges());
        _recombinaseCount = 0;
        _reporterCount = 0;
        java.util.Collections.sort(_edgeSet, this);
        ArrayList<Integer> vertices = new ArrayList<Integer>();

        vertices.addAll(_graph.getVertices());

        for (Integer i : vertices) {
            if (_graph.getNeighborCount(i) == 0) {
                _graph.removeVertex(i);
            }
        }
        vertices.clear();
        vertices.addAll(_graph.getVertices());

        //at this point only vertices that have a neighbor should remain
        if (!_graph.containsVertex(0)) {
            return "error: design must include vertex 0, the initial state";
        }
        for (PromoterEdge edge : _edgeSet) {
            StateMachineNode newNode = new StateMachineNode(edge, Integer.parseInt(edge.getWeight()));
            _nodes.add(newNode);
            ArrayList<Integer> neighbors = new ArrayList<Integer>();
            neighbors.addAll(_graph.getNeighbors(edge.dest));
            neighbors.remove(edge.source);
            for (Integer i : neighbors) {
                if (i > edge.dest) {
                    PromoterEdge pe = (PromoterEdge) _graph.findEdge(edge.dest, i);
                    newNode._children.add(Integer.parseInt(pe.getWeight()));
                }
            }

        }
        for (StateMachineNode smn : _nodes) {
            generateDesignHelper(smn);
        }
        String toReturn = "";
        for (StateMachineNode smn : _nodes) {
            toReturn = toReturn + smn._module + "\n";
            toReturn=toReturn.replace("X X' X X'", "X X'");
        }
        return toReturn;
    }

    private void generateDesignHelper(StateMachineNode smn) {
        String module = smn._module;
        ArrayList<StateMachineNode> children = smn.getChildren();
        ArrayList<StateMachineNode> siblings = smn.getSiblings();

        //add self deacting invertase sites
        module = module + " @" + _recombinaseCount;
        if (!children.isEmpty()) {
            module = flankElementWith(module, "P" + smn._promoter, "I" + _recombinaseCount);
        }
        for (StateMachineNode sib : siblings) {
            addDeactivatingSites(sib, _recombinaseCount);
        }
        _recombinaseCount++;
        //link with other modules
        for (int i = 0; i < children.size(); i++) {

            module = module + " @" + _recombinaseCount;
            addActivatingSites(children.get(i), _recombinaseCount);
            _recombinaseCount++;
        }

        //add reporters
        if (children.isEmpty()) {
            module = module + " R" + _reporterCount;
            _reporterCount++;
        }
        module = module + " X X'";
        smn._module = module;


    }

    private void addActivatingSites(StateMachineNode smn, int i) {
        String module = smn._module;
        String s = "I" + i;
        module = module.substring(0, module.indexOf("X X'") + 4) + flankElementWith(module.substring(module.indexOf("X X'") + 4), "X X'", s);
        smn._module = module;

    }

    private void addDeactivatingSites(StateMachineNode smn, int i) {
        String module = smn._module;
        String s = "I" + i;
        module = flankElementWith(module, "P" + smn._promoter, s);
        smn._module = module;
    }

    public String flankElementWith(String design, String target, String flanker) {
        int targetIndex = design.indexOf(target);
        String toReturn = design.substring(0, targetIndex) + flanker + " " + design.substring(targetIndex, targetIndex + target.length())
                + " " + flanker + design.substring(targetIndex + target.length());
        return toReturn;

    }

    public int compare(Object t, Object t1) {
        PromoterEdge v1 = (PromoterEdge) t;
        PromoterEdge v2 = (PromoterEdge) t1;
        if (v1.source.equals(v2.source) && v1.dest.equals(v2.dest) && v1.getWeight().equals(v2.getWeight())) {
            return 0;
        } else {
            if (v1.source > v2.source) {
                return 1;
            } else if (v2.source > v1.source) {
                return -1;
            } else {
                if (v1.dest > v2.dest) {
                    return 1;
                } else if (v2.dest > v1.dest) {
                    return -1;
                }
            }

        }
        return 0;
    }

    private class StateMachineNode {

        PromoterEdge _pe;
        Integer _promoter;
        Integer _originVertex;
        String _module;
        ArrayList<Integer> _parents;
        ArrayList<Integer> _children;

        private StateMachineNode(PromoterEdge pe, Integer index) {
            _pe = pe;
            _promoter = index;
            _module = "X X' P" + _promoter;
            if (pe.source != 0) {//exception for starting arcs
                _module = _module + " X X'";
            }
            _children = new ArrayList<Integer>();

        }

        private ArrayList<StateMachineNode> getChildren() {
            ArrayList<StateMachineNode> toReturn = new ArrayList<StateMachineNode>();
            for (StateMachineNode smn : _nodes) {
                if (smn._pe.source == this._pe.dest && this._children.contains(smn._promoter)) {
                    toReturn.add(smn);
                }
            }
            toReturn.remove(this);
            return toReturn;
        }

        private ArrayList<StateMachineNode> getSiblings() {
            ArrayList<StateMachineNode> toReturn = new ArrayList<StateMachineNode>();
            for (StateMachineNode smn : _nodes) {
                if (this._pe.source == smn._pe.source) {
                    toReturn.add(smn);
                }
            }
            toReturn.remove(this);
            return toReturn;
        }
    }
    private Graph _graph;
    private ArrayList<PromoterEdge> _edgeSet;
    private HashSet _designInputs;
    private ArrayList<StateMachineNode> _nodes;
    private int _recombinaseCount;
    private int _reporterCount;
    private GraphDisplayApplet _gda;
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
