package io.electroma.graph.sample;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collector;

public class GraphLoaderConsumer<T> implements BiConsumer<T, T> {

    private final DirectedGraph<T, DefaultEdge> graph;

    public GraphLoaderConsumer() {
        this(new DefaultDirectedGraph<>(DefaultEdge.class));
    }

    public GraphLoaderConsumer(final DirectedGraph<T, DefaultEdge> graph) {
        this.graph = graph;
    }

    @Override
    public void accept(final T s, final T s2) {

    }

    public static <S, R> Collector<S, ?, ? extends DirectedGraph<R, DefaultEdge>> toDirectedGraph(Function<S, Vertex<R>> transformer) {
        return Collector.of(() -> new DefaultDirectedGraph<>(DefaultEdge.class),
                (DirectedGraph<R, DefaultEdge> g, S e) -> {
                    Vertex<R> apply = transformer.apply(e);
                    g.addVertex(apply.from);
                    g.addVertex(apply.to);
                    g.addEdge(apply.from, apply.to);
                },
                (g1, g2) -> {
                    Graphs.addGraph(g1, g2);
                    return g1;
                });
    }

    public static class Vertex<T> {
        private final T from;

        private final T to;

        public static <S> Vertex<S> of(S from, S to) {
            return new Vertex<>(from, to);
        }

        private Vertex(T from, T to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final Vertex other = (Vertex) obj;
            return Objects.equals(this.from, other.from)
                    && Objects.equals(this.to, other.to);
        }
    }
}
