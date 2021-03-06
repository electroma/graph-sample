package io.electroma.graph.sample;

import com.google.common.base.Splitter;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static org.jgrapht.alg.DijkstraShortestPath.findPathBetween;

public class App {

    private static final Splitter SPLITTER = Splitter.on(',').trimResults().omitEmptyStrings();

    public static void main(final String[] args) {
        checkArgument(args.length == 3, "Please provide exactly three arguments delimited by spaces: file cityFrom cityTo");
        final Path inputPath = Paths.get(args[0]);
        checkArgument(Files.isReadable(inputPath), "Provided file path is not readable" + inputPath.toAbsolutePath());

        try {
            final DirectedGraph<String, DefaultEdge> graph = Files.lines(inputPath)
                    .collect(GraphLoader.toDirectedGraph(line -> {
                        final List<String> chunks = SPLITTER.splitToList(line);
                        checkArgument(chunks.size() == 2,
                                "There is incorrect input (there must be two city names delimited by comma) in line " + line);
                        return GraphLoader.Vertex.of(chunks.get(0), chunks.get(1));
                    }));
            System.out.println(checkPath(graph, args[1], args[2]) ? "Yes" : "No");

        } catch (IOException e) {
            throw new IllegalArgumentException("Unreadable input file", e);
        }
    }

    private static boolean checkPath(final DirectedGraph<String, DefaultEdge> graph,
                                     final String from,
                                     final String to) {
        try {
            return findPathBetween(graph, from, to) != null;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
