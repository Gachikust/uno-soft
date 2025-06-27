package org.example;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        String path;
        if(args.length==0){
            System.out.println("Введите имя файла");
        }
        path = args[0];

        List<String> lines = null;
        try(BufferedReader br = new BufferedReader(new FileReader(path))){
            lines = br.lines().toList();
        }catch (IOException e){
            System.out.println("Ошибка чтения файла"+e);
        }

        if(lines == null){
            System.out.println("Пустой файл");
            throw new IOException("empty file");
        }
        var groupsList = (new Grouper()).groupLines(lines);

        String outPath = "out.txt";
        try(FileWriter bw = new FileWriter(outPath);) {
            String countMessage = "Число групп с более чем одним элементом: "+groupsList.size()+'\n';
            bw.write(countMessage);
            System.out.println(countMessage);
            for (int i = 0; i < groupsList.size(); i++) {
                bw.write("Группа " + (i+1)+'\n');
                List<String> linesIds = groupsList.get(i);
                for (String linesId : linesIds) {
                    bw.write(linesId + '\n');
                }
            }
        }
        long endTime = System.currentTimeMillis();
        long timeElapsed = endTime - startTime;
        System.out.println("Время выполнения программмы "+timeElapsed/1000+"с");
    }
}

