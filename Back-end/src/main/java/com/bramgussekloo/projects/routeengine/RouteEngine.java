package com.bramgussekloo.projects.routeengine;

import com.bramgussekloo.projects.models.ConnectedNode;
import com.bramgussekloo.projects.models.LocationNodeNetwork;
import com.bramgussekloo.projects.models.Node;
import com.carrotsearch.hppc.IntIndexedContainer;
import com.graphhopper.routing.Dijkstra;
import com.graphhopper.routing.Path;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.FootFlagEncoder;
import com.graphhopper.routing.util.TraversalMode;
import com.graphhopper.routing.weighting.ShortestWeighting;
import com.graphhopper.storage.Graph;
import com.graphhopper.storage.GraphBuilder;

import java.util.ArrayList;
import java.util.HashMap;

public class RouteEngine {

    private HashMap<Integer, Node> nodeMap;
    private FlagEncoder encoder;
    private Graph graph;

    public void init(LocationNodeNetwork network) {
        FlagEncoder encoder = new FootFlagEncoder();
        EncodingManager em = EncodingManager.create(encoder);
        GraphBuilder gb = new GraphBuilder(em);
        Graph graph = gb.create();

        this.nodeMap = new HashMap<>();
        for (Node node : network.getNodes()) nodeMap.put(node.getNumber(), node);

        for (ConnectedNode edge : network.getConnections()) {
            Node from = nodeMap.get(edge.getNode1());
            Node to = nodeMap.get(edge.getNode2());
            graph.edge(edge.getNode1(), edge.getNode2(), edge.getDistance(), true);
            graph.getNodeAccess().setNode(from.getNumber(), from.getX(), from.getY(), from.getZ());
            graph.getNodeAccess().setNode(to.getNumber(), to.getX(), to.getY(), to.getZ());
        }
        this.encoder = encoder;
        this.graph = graph;
    }

    public ArrayList<Node> generateRoute(int fromId, int toId) throws IllegalArgumentException {
        Path path = new Dijkstra(this.graph, new ShortestWeighting(this.encoder), TraversalMode.NODE_BASED).calcPath(fromId, toId);
        ArrayList<Node> list = new ArrayList<>();
        IntIndexedContainer container = path.calcNodes();
        for (int i = 0; i < container.size(); i++) {
            int nodeId = container.get(i);
            Node node = this.nodeMap.get(nodeId);
            list.add(node);
        }

        if (!list.isEmpty()) {
            return list;
        } else {
            throw new IllegalArgumentException("No path available between node " + fromId + "to" + toId + ".");
        }
    }
}
