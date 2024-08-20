package io.rizvan.utils;

import java.util.List;
import java.util.Map;

public interface BayesPythonManager {
    void add_nodes(List<String> nodes);

    void add_edges(List<String[]> edges);

    void add_cpd(String variable, int variable_card, double[][] values, String[] evidence, int[] evidence_card);

    void finalize_model();

    Map<String, Integer> map_query(List<String> query, List<String[]> evidence);
}
