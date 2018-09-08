package com.yanhao.qlearning;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QLearning {

    private double greedy;
    private double learningRate;
    private double rewardDecay;
    private Random random = new Random();
    private QTable qTable;

    public QLearning(QTable qTable, double learningRate, double greedy, double rewardDecay) {
        this.learningRate = learningRate;
        this.greedy = greedy;
        this.rewardDecay = rewardDecay;
        this.qTable = qTable;
    }

    public int chooseAction(String state) {
        qTable.createIfNotExists(state);
        if (random.nextDouble() > greedy) {
            return qTable.getIndexOfMaxValue(state);
        }
        return random.nextInt(4);
    }

    public void learn(String state, int action, Double reward, String nextState) {
        qTable.createIfNotExists(nextState);
        qTable.update(state, action, reward, nextState, learningRate, rewardDecay);
    }

    public static void main(String[] args) {
        String modelFile = "model.csv";
        double learningRate = 0.01;
        double greedy = 0.9;
        double rewardDecay = 0.9;
        int playTimes = 1000000;
        int difficulty = 30;
        int saveInterval = 5;

        QTable qTable = new QTable(modelFile);
        QLearning qLearning = new QLearning(qTable, learningRate, greedy, rewardDecay);

        List<Integer> aa = new ArrayList<>(playTimes);
        for (int a = 0; a < playTimes; a++) {
            aa.add(a);
        }
        aa.parallelStream().forEach(a -> {
            String initState = State.randomState(difficulty);
            boolean success = false;

            while (!success) {
                String state = initState;
                while (true) {
                    int action = qLearning.chooseAction(state);
                    State s = State.next(state, action);
                    if (s == null) {
                        qLearning.learn(state, action, State.rewardMin, null);
                        break;
                    }
                    qLearning.learn(state, action, s.getReward(), s.getValue());
                    if (s.getReward().equals(State.rewardMax)) {
                        success = true;
                        break;
                    }
                    state = s.getValue();
                }
            }
            System.out.println(initState + ": state finished");

            if (a % saveInterval == 0) {
                qTable.save();
            }
        });
    }
}
