package io.electroma.graph.sample;

import com.google.common.collect.ImmutableSet;
import io.electroma.graph.sample.GraphLoaderConsumer.Vertex;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Test;

import java.util.HashMap;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GraphLoaderConsumerTest {
    @Test
    public void accept() throws Exception {

        DirectedGraph<String, DefaultEdge> collect = Stream.of(
                new HashMap.SimpleEntry<>("A", "B"),
                new HashMap.SimpleEntry<>("B", "C"),
                new HashMap.SimpleEntry<>("B", "C"),
                new HashMap.SimpleEntry<>("A", "C")
        ).collect(GraphLoaderConsumer.toDirectedGraph(e -> Vertex.of(e.getKey(), e.getValue())));

        assertEquals(ImmutableSet.of("A", "B", "C"), collect.vertexSet());

        assertEquals(1, DijkstraShortestPath.findPathBetween(collect, "A", "C").size());
        assertNull(DijkstraShortestPath.findPathBetween(collect, "B", "A"));
    }
}