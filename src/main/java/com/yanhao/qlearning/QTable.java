package com.yanhao.qlearning;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class QTable {

    private Map<String, List<Double>> qMap;
    private Random random = new Random();
    private String modelFile;

    public QTable(String modelFile) {
        this.modelFile = modelFile;
        loadFromFile();
    }

    public synchronized void loadFromFile() {
        qMap = new ConcurrentHashMap<>();
        if (new File(modelFile).exists()) {
            try (InputStream inputStream = new FileInputStream(modelFile)) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                reader.lines().parallel().forEach(line -> {
                    String[] vs = line.split(",");
                    qMap.put(vs[0], Arrays.asList(vs[1], vs[2], vs[3], vs[4]).parallelStream().map(Double::valueOf).collect(Collectors.toList()));
                });
                System.out.println("model load finished: " + qMap.size());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void createIfNotExists(String state) {
        if (state != null) {
            qMap.putIfAbsent(state, Arrays.asList(0d, 0d, 0d, 0d));
        }
    }

    public synchronized int getIndexOfMaxValue(String state) {
        List<Double> values = qMap.get(state);
        List<Integer> indexOfMaxValue = new ArrayList<>(values.size());
        Double max = -1d;
        for (int i = 0; i < values.size(); i++) {
            int c = values.get(i).compareTo(max);
            if (c > 0) {
                indexOfMaxValue.clear();
                indexOfMaxValue.add(i);
            } else if (c == 0) {
                indexOfMaxValue.add(i);
            }
        }
        if (indexOfMaxValue.size() == 1) {
            return indexOfMaxValue.get(0);
        }
        return indexOfMaxValue.get(random.nextInt(indexOfMaxValue.size()));
    }

    public synchronized void update(String state, int action, Double reward, String nextState, double learningRate, double rewardDecay) {
        List<Double> values = qMap.get(state);
        double value = values.get(action);
        double nextStateMax = 0;
        if (nextState != null) {
            nextStateMax = Collections.max(qMap.get(nextState));
        }
        value += learningRate * (reward - value + rewardDecay * nextStateMax);
        values.set(action, value);
    }

    public synchronized void save() {
        String tmp = modelFile + ".tmp";
        try (OutputStream outputStream = new FileOutputStream(tmp)) {
            for (Map.Entry<String, List<Double>> entry : qMap.entrySet()) {
                StringBuilder line = new StringBuilder(entry.getKey());
                for (Double d : entry.getValue()) {
                    line.append(",").append(d);
                }
                line.append("\n");
                outputStream.write(line.toString().getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        new File(tmp).renameTo(new File(modelFile));
        System.out.println("model save finished: " + qMap.size());
    }
}
