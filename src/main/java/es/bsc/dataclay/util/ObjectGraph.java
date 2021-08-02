package es.bsc.dataclay.util;

import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.structs.Tuple;

import java.io.Serializable;
import java.util.*;

public class ObjectGraph implements Serializable, Cloneable {

    private static final long serialVersionUID = -4244299627338569938L;

    /** Adjacency matrix. Per each vertex, a list of adjacent vertices and their weight. */
    private Map<Vertex, Map<Vertex, Integer>> adjMatrix = new HashMap<>();
    // Design note: We use a hashmap instead of tuple to allow fast modification of the weight

    /**
     * Vertex class. Each vertex contains ID of an object
     */
    public static class Vertex implements Serializable {
        ObjectID objectID;
        public Vertex(ObjectID value) {
            this.objectID = value;
        }
        public  int hashCode() {
            return objectID.hashCode();
        }
        public boolean equals(Object obj) {
            if (obj instanceof Vertex) {
                return this.objectID.equals(((Vertex) obj).objectID);
            }
            return false;
        }
        public String toString() {
            // get small representation of nodes, clashing is not important for print
            return this.objectID.getId().toString().substring(0, 5);
        }
    }

    /**
     * Constructor
     */
    public ObjectGraph() {
        // Add graph root with object ID = 0L
        // Root objects are external references (federation, aliases) and EEs
        addVertex(getRootID());
    }

    /**
     * Get adjacency matrix
     * @return Adjacency matrix
     */
    private Map<Vertex, Map<Vertex, Integer>> getAdjMatrix() {
        return this.adjMatrix;
    }

    /**
     * Get root of graph i.e. EE/sessions
     * @return ID of root of the graph
     */
    public ObjectID getRootID() {
        return new ObjectID(new UUID(0L, 0L));
    }

    /**
     * Add vertex in graph with ID provided
     * @param objectID ID of the vertex
     */
    public void addVertex(ObjectID objectID) {
        adjMatrix.putIfAbsent(new Vertex(objectID), new HashMap<>());
    }


    /**
     * Remove vertex in graph with ID provided
     * @param objectID ID of the vertex
     */
    public void removeVertex(ObjectID objectID) {
        Vertex v = new Vertex(objectID);
        adjMatrix.values().stream().forEach(e -> e.remove(v));
        adjMatrix.remove(new Vertex(objectID));
    }


    /**
     * Return number of vertices in graph
     * @return Number of vertices in graph
     */
    public int getNumberOfVertices() {
        return adjMatrix.keySet().size();
    }

    /**
     * If there is no edge from root to dst, add it with weight provided, otherwise
     * set the weight with weight
     * @param dst ID of the dst edge
     * @param weight Weight of the edge i.e. number of references
     */
    public void addOrSetEdgeFromRoot(ObjectID dst, final int weight) {
        addOrSetEdge(this.getRootID(), dst, weight);
    }

    /**
     * If there is no edge from src to dst, add it with weight provided, otherwise
     * set the weight with weight
     * @param src ID of the src vertex
     * @param dst ID of the dst vertex
     * @param weight Weight of the edge i.e. number of references
     */
    public void addOrSetEdge(ObjectID src, ObjectID dst, final int weight) {
        Vertex v1 = new Vertex(src);
        Vertex v2 = new Vertex(dst);
        Map<Vertex, Integer> neighbors = adjMatrix.get(v1);
        neighbors.put(v2, weight);
    }

    /**
     * If there is no edge from src to dst, add it with weight provided, otherwise
     * increment the weight with weight
     * @param src ID of the src vertex
     * @param dst ID of the dst vertex
     * @param weight Weight of the edge i.e. number of references
     */
    public void addOrIncrementEdge(ObjectID src, ObjectID dst, final int weight) {
        Vertex v1 = new Vertex(src);
        Vertex v2 = new Vertex(dst);
        Map<Vertex, Integer> neighbors = adjMatrix.get(v1);
        Integer currentValue = neighbors.putIfAbsent(v2, weight);
        if (currentValue != null) {
            // it exists, so increment weight
            neighbors.put(v2, currentValue + weight);
        }
    }

    /**
     * Remove edge from src to dst
     * @param src ID of the src vertex
     * @param dst ID of the dst vertex
     */
    public void removeEdge(ObjectID src, ObjectID dst) {
        Vertex v1 = new Vertex(src);
        Vertex v2 = new Vertex(dst);
        Map<Vertex, Integer> neighbors = adjMatrix.get(v1);
        neighbors.remove(v2);

    }

    /**
     * Remove all edges from src
     * @param src ID of the src vertex
     */
    public void removeAllEdges(ObjectID src) {
        Vertex v1 = new Vertex(src);
        Map<Vertex, Integer> neighbors = adjMatrix.get(v1);
        neighbors.clear();
    }

    /**
     * Remove edge from root to dst
     * @param dst ID of the dst vertex
     */
    public void removeEdgeFromRoot(ObjectID dst) {
        removeEdge(getRootID(), dst);
    }

    /**
     * Get all adjacent vertices of src
     * @param src ID of the src vertex
     * @return all adjacent vertices of src
     */
    public Set<Vertex> getAdjVertices(ObjectID src) {
        return getAdjVerticesAndWeight(src).keySet();
    }

    /**
     * Get all adjacent vertices of src and weight
     * @param src ID of the src vertex
     * @return all adjacent vertices of src and weight
     */
    public Map<Vertex, Integer> getAdjVerticesAndWeight(ObjectID src) {
        return adjMatrix.get(new Vertex(src));
    }

    /**
     * Return all object IDs in graph
     * @return IDs of all vertices in graph
     */
    public Set<ObjectID> getAllObjectsIDsInGraph() {
        Set<ObjectID> objectIDS = new HashSet<>();
        for (Vertex vertex : this.adjMatrix.keySet()) {
            objectIDS.add(vertex.objectID);
        }
        return objectIDS;
    }

    /**
     * Get all vertices that are not accessible from root i.e. there is no path from root to vertex
     * @return IDs of all vertices that are not accessible
     */
    public Set<ObjectID> getUnaccesibleObjects() {
        Set<ObjectID> allAccessibleObjects = depthFirstTraversal(this, this.getRootID());
        Set<ObjectID> allObjectsInGraph = getAllObjectsIDsInGraph();
        allObjectsInGraph.removeAll(allAccessibleObjects);
        return allObjectsInGraph;
    }

    /**
     * Merge current graph with provided graph i.e. add all vertices and edges from graph to current
     * @param graph Graph to merge with this
     */
    public void merge(final ObjectGraph graph) {
        // add all vertexs if they do not exist
        Set<ObjectID> allObjectsInGraph = graph.getAllObjectsIDsInGraph();
        Set<ObjectID> visitedInGraph = depthFirstMerge(this, graph, getRootID());
        allObjectsInGraph.removeAll(visitedInGraph);
        for (ObjectID unaccessibleVertex : allObjectsInGraph) {
            // merge unaccesible vertex
            if (!visitedInGraph.contains(unaccessibleVertex)) {
                visitedInGraph.addAll(depthFirstMerge(this, graph, unaccessibleVertex));
            }
        }

    }

    /**
     * Traverse graph using depth-first algorithm and return all visited nodes
     * @param graph Graph to traverse
     * @param src Source node
     * @return All visited nodes
     */
    public static Set<ObjectID> depthFirstTraversal(final ObjectGraph graph, final ObjectID src) {
        Set<ObjectID> visited = new LinkedHashSet<ObjectID>();
        Stack<ObjectID> stack = new Stack<ObjectID>();
        stack.push(src);
        while (!stack.isEmpty()) {
            ObjectID vertex = stack.pop();
            if (!visited.contains(vertex)) {
                visited.add(vertex);
                for (Vertex v : graph.getAdjVertices(vertex)) {
                    stack.push(v.objectID);
                }
            }
        }
        return visited;
    }

    /**
     * Traverse graph using depth-first algorithm and for each visited vertex and edge, add it into
     * current graph
     * @param srcGraph Source graph in which to store merge result
     * @param dstGraph Graph to traverse and merge
     * @param src Source node
     * @return set of visited nodes in src dstGraph.
     */
    public static Set<ObjectID> depthFirstMerge(final ObjectGraph srcGraph, final ObjectGraph dstGraph, final ObjectID src) {
        Set<ObjectID> visited = new LinkedHashSet<ObjectID>();
        Stack<ObjectID> stack = new Stack<ObjectID>();
        stack.push(src);
        while (!stack.isEmpty()) {
            ObjectID vertex = stack.pop();
            if (!visited.contains(vertex)) {
                visited.add(vertex);
                srcGraph.addVertex(vertex); //add to result graph
                for (Map.Entry<Vertex, Integer> vEntry : dstGraph.getAdjVerticesAndWeight(vertex).entrySet()) {
                    Vertex v = vEntry.getKey();
                    Integer w = vEntry.getValue();
                    // if the edge already exists, increment weight
                    srcGraph.addOrIncrementEdge(vertex, v.objectID, w);
                    stack.push(v.objectID);
                }
            }
        }
        return visited;
    }


    @Override
    public ObjectGraph clone() {
        ObjectGraph cloneObjectGraph = new ObjectGraph();
        for (Map.Entry<Vertex, Map<Vertex, Integer>> vertexMapEntry : this.getAdjMatrix().entrySet()) {
            Vertex key = vertexMapEntry.getKey();
            ObjectID srcID = new ObjectID(key.objectID.getId());
            cloneObjectGraph.addVertex(srcID);
            for (Map.Entry<Vertex, Integer> adjVerticesEntry : vertexMapEntry.getValue().entrySet()) {
                Vertex adjVertex = adjVerticesEntry.getKey();
                ObjectID dstID = new ObjectID(adjVertex.objectID.getId());
                Integer weight = adjVerticesEntry.getValue();
                cloneObjectGraph.addOrSetEdge(srcID, dstID, weight);
            }
        }
        return cloneObjectGraph;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Set<ObjectID> visited = new LinkedHashSet<ObjectID>();
            Stack<Tuple<ObjectID, Integer>> stack = new Stack<Tuple<ObjectID, Integer>>();
            stack.push(new Tuple<ObjectID, Integer>(this.getRootID(), 0));
            // print graph
            stringBuilder.append("\n");
            for (Map.Entry<Vertex, Map<Vertex, Integer>> vertexMapEntry : this.adjMatrix.entrySet()) {
                stringBuilder.append(vertexMapEntry.getKey());
                Iterator<Vertex> it = vertexMapEntry.getValue().keySet().iterator();
                stringBuilder.append("->[");
                while (it.hasNext()) {
                    stringBuilder.append(it.next());
                    if (it.hasNext()) {
                        stringBuilder.append(",");
                    }
                }
                stringBuilder.append("]");
                stringBuilder.append("\n");
            }

            // print adjacency matrix, print 20x20 matrixes to fit in the screen
            Iterator<Map.Entry<Vertex, Map<Vertex, Integer>>> adjMatrixIt = this.adjMatrix.entrySet().iterator();
            int matrixSize = 20;
            int curAdjMatrixSize = this.adjMatrix.size();
            if (curAdjMatrixSize < 20) {
                matrixSize = curAdjMatrixSize;
            }
            while (adjMatrixIt.hasNext()) {

                stringBuilder.append("\n");
                int numLines = 6;
                for (int j = 0; j < matrixSize; ++j) {
                    numLines += 6;
                }
                for (int j = 0; j < numLines; ++j) {
                    stringBuilder.append("-");
                }
                stringBuilder.append("\n");
                stringBuilder.append("     |");
                // print headers
                List<Map.Entry<Vertex, Map<Vertex, Integer>>> vertices = new ArrayList<>();
                for (int k = 0; k < matrixSize && adjMatrixIt.hasNext(); ++k) {
                    Map.Entry<Vertex, Map<Vertex, Integer>> entry = adjMatrixIt.next();
                    stringBuilder.append(entry.getKey());
                    stringBuilder.append("|");
                    vertices.add(entry);

                }
                stringBuilder.append("\n");
                for (int j = 0; j < numLines; ++j) {
                    stringBuilder.append("-");
                }
                stringBuilder.append("\n");

                // print rows
                for (Map.Entry<Vertex, Map<Vertex, Integer>> vertexMapEntry : this.adjMatrix.entrySet()) {
                    Vertex v = vertexMapEntry.getKey();
                    stringBuilder.append(v);
                    stringBuilder.append("|");
                    Map<Vertex, Integer> neighbours = vertexMapEntry.getValue();
                    for (Map.Entry<Vertex, Map<Vertex, Integer>> curHeaderVertex : vertices) {
                        Integer weight = neighbours.get(curHeaderVertex.getKey());
                        stringBuilder.append("  ");
                        if (weight != null) {
                            stringBuilder.append(weight);
                        } else {
                            stringBuilder.append("0");
                        }
                        stringBuilder.append("  ");
                        stringBuilder.append("|");
                    }
                    stringBuilder.append("\n");
                    for (int j = 0; j < numLines; ++j) {
                        stringBuilder.append("-");
                    }
                    stringBuilder.append("\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            stringBuilder.append("ERROR");
        }
        return stringBuilder.toString();
    }
}
