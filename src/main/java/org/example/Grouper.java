package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class Grouper {
    public List<List<String>> groupLines(List<String> lines){
        var columnIdentifier = getColumnIdentifiers(lines);
        var groups = getGroups(lines.size(), columnIdentifier);
        List<List<String>> groupedLines = groups.values().stream()
                .filter(group->group.size()>1)
                .sorted((o1, o2) -> o2.size()-o1.size())
                .map(integers ->
                    integers.stream()
                            .map(integer -> lines.get(integer - 1))
                            .collect(Collectors.toList()))
                .map(strings -> strings.stream().distinct().collect(Collectors.toList()))
                .toList();
        Set<List<String>> uniqueSet = new HashSet<>(groupedLines);
        return new ArrayList<>(uniqueSet);

    }

    private Map<Integer, List<Integer>> getGroups(Integer linesCount,List<Map<String, List<Integer>>> columnIdentifier){
        int totalLines =linesCount+1;
        DSU dsu = new DSU(totalLines);

        for (Map<String, List<Integer>> colMap : columnIdentifier) {
            for (List<Integer> idxList : colMap.values()) {
                if (idxList.size() <= 1) continue;
                int base = idxList.getFirst();
                for (int i = 1; i < idxList.size(); i++) {
                    dsu.union(base, idxList.get(i));
                }
            }
        }

        Map<Integer, List<Integer>> groups = new HashMap<>();
        for (int i = 1; i < totalLines; i++) {
            int root = dsu.find(i);
            //System.out.println(root);
            groups.computeIfAbsent(root, k -> new ArrayList<>()).add(i);
        }
        return groups;
    }
    private List<Map<String, List<Integer>>> getColumnIdentifiers(List<String> lines){
        List<Map<String, List<Integer>>> columnIdentifier = new ArrayList<>();
        int n=0;
        for (String line:lines) {
            n++;
            //System.out.println(columnIdentifier);
            String[] lineParts = line.split(";");

            for(int i=0;i<lineParts.length;i++) {
                if(checkParts(lineParts[i])){
                    continue;
                }
                Map<String,List<Integer>> columnIdentifiers = null;
                if(columnIdentifier.size()>i){
                    columnIdentifiers = columnIdentifier.get(i);

                }else{
                    columnIdentifiers = new HashMap<>();
                    columnIdentifier.add(columnIdentifiers);
                }
                columnIdentifiers.computeIfAbsent(lineParts[i],s -> new ArrayList<>()).add(n);
            }
        }
        return columnIdentifier;
    }

    private Boolean checkParts(String part){
        return (part.matches("\"\"[^\"]*\"\"") || part.length()<=2);
    }
}
