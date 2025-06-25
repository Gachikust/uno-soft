package org.example;

import java.util.*;

public class Grouper {

    public List<List<String>> groupLines(List<String> lines) {
        int n = lines.size();
        DSU dsu = new DSU(n);
        List<Map<String, Integer>> columnIdentifiers = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String line = lines.get(i);
            if (isInvalidLine(line)) continue;

            String[] parts = line.split(";", -1);
            for (int col = 0; col < parts.length; col++) {
                String part = parts[col];
                if (isInvalidPart(part)) continue;

                while (columnIdentifiers.size() <= col) {
                    columnIdentifiers.add(new HashMap<>());
                }

                Map<String, Integer> columnMap = columnIdentifiers.get(col);
                Integer prevIdx = columnMap.get(part);
                if (prevIdx != null) {
                    dsu.union(i, prevIdx);
                } else {
                    columnMap.put(part, i);
                }
            }
        }

        Map<Integer, List<String>> resultMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            String line = lines.get(i);
            if (isInvalidLine(line)) continue;
            resultMap.computeIfAbsent(root, k -> new ArrayList<>()).add(line);
        }

        List<List<String>> result = new ArrayList<>();
        for (List<String> group : resultMap.values()) {
            Set<String> unique = new LinkedHashSet<>(group);
            if (unique.size() > 1) {
                result.add(new ArrayList<>(unique));
            }
        }

        result.sort((a, b) -> Integer.compare(b.size(), a.size()));
        return result;
    }

    private boolean isInvalidLine(String line) {
        String[] parts = line.split(";", -1);
        for (String part : parts) {
            if (!part.isEmpty() && !part.matches("\"[^\"]*\"")) {
                return true;
            }
        }
        return false;
    }

    private boolean isInvalidPart(String part) {
        return part.length() <= 2 ;
    }

}
