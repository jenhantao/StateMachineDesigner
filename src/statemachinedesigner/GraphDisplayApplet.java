/*
 * Copyright (c) 2003, the JUNG Project and the Regents of the University of
 * California All rights reserved.
 *
 * This software is open-source under the BSD license; see either "license.txt"
 * or http://jung.sourceforge.net/license.txt for a description.
 *
 * Created on May 10, 2004
 */
package statemachinedesigner;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.util.Relaxer;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.ObservableGraph;
import edu.uci.ics.jung.graph.event.GraphEvent;
import edu.uci.ics.jung.graph.event.GraphEventListener;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Graphs;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Timer;

/**
 *
 *
 * @author Jenhan Tao
 */
public class GraphDisplayApplet extends javax.swing.JApplet {

    private SimulatorController _controller;
    private Graph<Number, Number> _graph = null;
    private VisualizationViewer<Number, Number> vv = null;
    private AbstractLayout<Number, Number> layout = null;
    Timer timer;
    boolean done;
    private int edgeCount;
    private  DesignTrie _trie;

    GraphDisplayApplet(DesignTrie dt) {
        _trie = dt;
  }

    @Override
    public void init() {
        edgeCount = 0;
        //create a graph
        Graph<Number, Number> ig = Graphs.<Number, Number>synchronizedDirectedGraph(new DirectedSparseMultigraph<Number, Number>());

        ObservableGraph<Number, Number> og = new ObservableGraph<Number, Number>(ig);
        og.addGraphEventListener(new GraphEventListener<Number, Number>() {

            public void handleGraphEvent(GraphEvent<Number, Number> evt) {
                System.err.println("got " + evt);
            }
        });
        this._graph = og;
        //create a graphdraw
        layout = new FRLayout2<Number, Number>(_graph);
//scale this to the size of the design input panel
        vv = new VisualizationViewer<Number, Number>(layout, new Dimension(600, 250));
        getContentPane().setLayout(new BorderLayout());
        vv.setGraphMouse(new DefaultModalGraphMouse<Number, Number>());
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Number>());
        getContentPane().add(vv);
    }

    @Override
    public void start() {
        validate();
        //set timer so applet will change
//        timer.schedule(new RemindTask(), 1000, 1000); //subsequent rate
        vv.repaint();
    }

public void createVertex(Integer v) {
     try {

            layout.lock(true);
            Relaxer relaxer = vv.getModel().getRelaxer();
            relaxer.pause();
            if (!_graph.containsVertex(v)) {
                _graph.addVertex(v);
            }

            layout.initialize();
            relaxer.resume();
            layout.lock(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
}

    public void createEdge(Integer v1, Integer v2) {
        try {

            layout.lock(true);
            Relaxer relaxer = vv.getModel().getRelaxer();
            relaxer.pause();
            if (_graph.containsVertex(v1) && _graph.containsVertex(v2)) {
                _graph.addEdge(edgeCount, v1, v2, EdgeType.DIRECTED);
                edgeCount++;
            } else if (!_graph.containsVertex(v1) && !_graph.containsVertex(v2)) {
                _graph.addVertex(v1);
                _graph.addVertex(v2);
                _graph.addEdge(edgeCount, v1, v2);
                edgeCount++;
            } else if (_graph.containsVertex(v1) && !_graph.containsVertex(v2)) {
                _graph.addVertex(v2);
                _graph.addEdge(edgeCount, v1, v2);
                edgeCount++;
            } else if (!_graph.containsVertex(v1) && _graph.containsVertex(v2)) {
                _graph.addVertex(v1);
                _graph.addEdge(edgeCount, v1, v2);
                edgeCount++;
            }

            layout.initialize();
            relaxer.resume();
            layout.lock(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
