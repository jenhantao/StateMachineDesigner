/*
 * Copyright (c) 2003, the JUNG Project and the Regents of the University of
 * California All rights reserved.
 *
 * This software is open-source under the BSD license; see either "license.txt"
 * or http://jung.sourceforge.net/license.txt for a description.
 *
 */
package statemachinedesigner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.functors.MapTransformer;
import org.apache.commons.collections15.map.LazyMap;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.algorithms.layout.util.Relaxer;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.annotations.AnnotationControls;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.util.LinkedList;
import javax.swing.JLabel;

public class EditorApplet extends JApplet {

    /**
     *
     */
    /**
     * the graph
     */
    Graph<Number, Number> _graph;
    AbstractLayout<Number, Number> layout;
    /**
     * the visual component and renderer for the graph
     */
    VisualizationViewer<Number, Number> vv;
    String instructions =
            "<html>"
            + "<h3>All Modes:</h3>"
            + "<ul>"
            + "<li>Right-click an empty area for <b>Create Vertex</b> popup"
            + "<li>Right-click on a Vertex for <b>Delete Vertex</b> popup"
            + "<li>Right-click on a Vertex for <b>Add Edge</b> menus <br>(if there are selected Vertices)"
            + "<li>Right-click on an Edge for <b>Delete Edge</b> popup"
            + "<li>Mousewheel scales with a crossover value of 1.0.<p>"
            + "     - scales the graph layout when the combined scale is greater than 1<p>"
            + "     - scales the graph view when the combined scale is less than 1"
            + "</ul>"
            + "<h3>Editing Mode:</h3>"
            + "<ul>"
            + "<li>Left-click an empty area to create a new Vertex"
            + "<li>Left-click on a Vertex and drag to another Vertex to create an Undirected Edge"
            + "<li>Shift+Left-click on a Vertex and drag to another Vertex to create a Directed Edge"
            + "</ul>"
            + "<h3>Picking Mode:</h3>"
            + "<ul>"
            + "<li>Mouse1 on a Vertex selects the vertex"
            + "<li>Mouse1 elsewhere unselects all Vertices"
            + "<li>Mouse1+Shift on a Vertex adds/removes Vertex selection"
            + "<li>Mouse1+drag on a Vertex moves all selected Vertices"
            + "<li>Mouse1+drag elsewhere selects Vertices in a region"
            + "<li>Mouse1+Shift+drag adds selection of Vertices in a new region"
            + "<li>Mouse1+CTRL on a Vertex selects the vertex and centers the display on it"
            + "<li>Mouse1 double-click on a vertex or edge allows you to edit the label"
            + "</ul>"
            + "<h3>Transforming Mode:</h3>"
            + "<ul>"
            + "<li>Mouse1+drag pans the graph"
            + "<li>Mouse1+Shift+drag rotates the graph"
            + "<li>Mouse1+CTRL(or Command)+drag shears the graph"
            + "<li>Mouse1 double-click on a vertex or edge allows you to edit the label"
            + "</ul>"
            + "<h3>Annotation Mode:</h3>"
            + "<ul>"
            + "<li>Mouse1 begins drawing of a Rectangle"
            + "<li>Mouse1+drag defines the Rectangle shape"
            + "<li>Mouse1 release adds the Rectangle as an annotation"
            + "<li>Mouse1+Shift begins drawing of an Ellipse"
            + "<li>Mouse1+Shift+drag defines the Ellipse shape"
            + "<li>Mouse1+Shift release adds the Ellipse as an annotation"
            + "<li>Mouse3 shows a popup to input text, which will become"
            + "<li>a text annotation on the graph at the mouse location"
            + "</ul>"
            + "</html>";

    /**
     * create an instance of a simple graph with popup controls to
     * create a graph.
     *
     */
    public EditorApplet(SimulatorController controller) {
        // create a simple graph for the demo
        _controller = controller;
        _graph = new DirectedSparseGraph<Number, Number>();

        this.layout = new StaticLayout<Number, Number>(_graph,
                new Dimension(600, 600));

        vv = new VisualizationViewer<Number, Number>(layout);
        vv.setBackground(Color.white);

        vv.getRenderContext().setVertexLabelTransformer(MapTransformer.<Number, String>getInstance(
                LazyMap.<Number, String>decorate(new HashMap<Number, String>(), new ToStringLabeller<Number>())));

        vv.getRenderContext().setEdgeLabelTransformer(MapTransformer.<Number, String>getInstance(
                LazyMap.<Number, String>decorate(new HashMap<Number, String>(), new ToStringLabeller<Number>())));

        vv.setVertexToolTipTransformer(vv.getRenderContext().getVertexLabelTransformer());


        Container content = getContentPane();
        final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
        content.add(panel);
        Factory<Number> vertexFactory = new VertexFactory();
        Factory<Number> edgeFactory = new EdgeFactory();

        final EditingModalGraphMouse<Number, Number> graphMouse =
                new EditingModalGraphMouse<Number, Number>(vv.getRenderContext(), vertexFactory, edgeFactory);

        // the EditingGraphMouse will pass mouse event coordinates to the
        // vertexLocations function to set the locations of the vertices as
        // they are created
        vv.setGraphMouse(graphMouse);

        vv.addKeyListener(graphMouse.getModeKeyListener());
        graphMouse.setMode(ModalGraphMouse.Mode.EDITING);
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                LinkedList<Number> vertices = new LinkedList<Number>();
                vertices.addAll(_graph.getVertices());
                for (Number n : vertices) {
                    if (_graph.getNeighborCount(n) == 0) {
                        _graph.removeVertex(n);
                    }
                }
                LinkedList<Number> edges = new LinkedList<Number>();
                edges.addAll(_graph.getEdges());
                for (Number n : edges) {
                    if (_graph.getDest(n) == null) {
                        _graph.removeEdge(n);
                    } else if (_graph.getSource(n) == _graph.getDest(n)) {
                        _graph.removeEdge(n);
                    }
                }
                vv.repaint();

            }
        });
        JButton clearButton = new JButton("Clear All");
        clearButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //add code to clear all nodes
                vertexCount = 0;
                edgeCount = 0;
                LinkedList<Number> vertices = new LinkedList<Number>();
                vertices.addAll(_graph.getVertices());
                for (Number n : vertices) {
                    _graph.removeVertex(n);
                }
                vv.repaint();
            }
        });

        JButton clearEdgesButton = new JButton("Clear All Edges");
        clearEdgesButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                edgeCount = 0;
                LinkedList<Number> edges = new LinkedList<Number>();
                edges.addAll(_graph.getEdges());
                for (Number n : edges) {
                    _graph.removeEdge(n);
                }
                vv.repaint();
            }
        });

        //to be removed
        JButton helpButton = new JButton("Help");
        helpButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(vv, instructions);
            }
        });
        JComboBox modeBox = graphMouse.getModeComboBox();
        modeBox.removeItemAt(0); //transforming
        modeBox.removeItemAt(2); //annotating
        modeBox.setToolTipText("Editing mode allows for insertion and removal of edges/vertices; Picking mode is for placement");
        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Mouse Mode"));
        controlPanel.add(modeBox);
        controlPanel.add(submitButton);
        controlPanel.add(clearButton);
        controlPanel.add(clearEdgesButton);
        controlPanel.add(helpButton);
        content.add(controlPanel, BorderLayout.SOUTH);
    }

    class VertexFactory implements Factory<Number> {

        VertexFactory() {
            vertexCount = 0;
        }

        public Number create() {
            return vertexCount++;
        }
    }

    class EdgeFactory implements Factory<Number> {

        EdgeFactory() {
            edgeCount = 0;
        }

        public Number create() {
            return edgeCount++;
        }
    }
    private int vertexCount;
    private int edgeCount;
    private SimulatorController _controller;
}
