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
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.DAGLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Timer;
import org.apache.commons.collections15.Transformer;

/**
 *
 *
 * @author Jenhan Tao
 */
public class GraphDisplayApplet extends javax.swing.JApplet {

    private Graph<Number, PromoterEdge> _graph = null;
    private VisualizationViewer<Number, PromoterEdge> vv = null;
    private AbstractLayout<Number, PromoterEdge> layout = null;
    boolean done;
    private int edgeCount;
    private int vertexCount;

    @Override
    public void init() {
        //create a graph
        Graph<Number, PromoterEdge> ig = Graphs.<Number, PromoterEdge>synchronizedDirectedGraph(new DirectedSparseMultigraph<Number, PromoterEdge>());

        ObservableGraph<Number, PromoterEdge> og = new ObservableGraph<Number, PromoterEdge>(ig);

        this._graph = og;
        //create a graphdraw
        layout = new SpringLayout<Number, PromoterEdge>(_graph);
//scale this to the size of the design input panel
        vv = new VisualizationViewer<Number, PromoterEdge>(layout, new Dimension(600, 250));
        getContentPane().setLayout(new BorderLayout());
        vv.setGraphMouse(new DefaultModalGraphMouse<Number, PromoterEdge>());
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Number>());

        //code below adds edge labels
        vv.getRenderContext().setEdgeLabelTransformer(new Transformer<PromoterEdge, String>() {

            public String transform(PromoterEdge e) {
                return (e.getWeight());
            }
        });



        getContentPane().add(vv);
        _graph.addVertex(0);
    }

    @Override
    public void start() {
        validate();
        //set timer so applet will change
//        timer.schedule(new RemindTask(), 1000, 1000); //subsequent rate
        vv.repaint();
    }

    public Graph getModel() {
        return _graph;
    }

    public void createEdge(Integer v1, Integer v2, String label) {
        try {

            layout.lock(true);
            Relaxer relaxer = vv.getModel().getRelaxer();
            relaxer.pause();
            if (_graph.containsVertex(v1) && _graph.containsVertex(v2)) {
                _graph.addEdge(new PromoterEdge(label, v1, v2), v1, v2, EdgeType.DIRECTED);
            } else if (!_graph.containsVertex(v1) && !_graph.containsVertex(v2)) {
                _graph.addVertex(v1);
                _graph.addVertex(v2);
                _graph.addEdge(new PromoterEdge(label, v1, v2), v1, v2, EdgeType.DIRECTED);
            } else if (_graph.containsVertex(v1) && !_graph.containsVertex(v2)) {
                _graph.addVertex(v2);
                _graph.addEdge(new PromoterEdge(label, v1, v2), v1, v2, EdgeType.DIRECTED);
            } else if (!_graph.containsVertex(v1) && _graph.containsVertex(v2)) {
                _graph.addVertex(v1);
                _graph.addEdge(new PromoterEdge(label, v1, v2), v1, v2, EdgeType.DIRECTED);
            }

            layout.initialize();
            relaxer.resume();
            layout.lock(false);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void removeEdge(Integer source, Integer dest, String weight) {
        try {

            layout.lock(true);
            Relaxer relaxer = vv.getModel().getRelaxer();
            relaxer.pause();
            ArrayList<PromoterEdge> deleteCandidates = new ArrayList<PromoterEdge>();
            deleteCandidates.addAll(_graph.findEdgeSet(source, dest));
            for (int i = 0; i < deleteCandidates.size(); i++) {
                if (!deleteCandidates.get(i).getWeight().equals(weight)) {
                    deleteCandidates.remove(i);
                    i--;
                }
            }
            for (PromoterEdge pe : deleteCandidates) {
                    _graph.removeEdge(pe);              
            }
            if (deleteCandidates.size() > 0) {
                if (_graph.getNeighborCount(dest) < 1) {
                    _graph.removeVertex(dest);
                }
                if (_graph.getNeighborCount(source) < 1) {
                    _graph.removeVertex(source);
                }
            }
            layout.initialize();
            relaxer.resume();
            layout.lock(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
