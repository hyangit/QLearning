package com.yanhao.qlearning;

import java.util.Random;

public class State {

    private static final String finishedState = "123456780";
    public static final Double rewardMax = 1d;
    public static final Double rewardMin = -1d;

    private String value;
    private Double reward;

    public State(String value, Double reward) {
        this.value = value;
        this.reward = reward;
    }

    public String getValue() {
        return value;
    }

    public Double getReward() {
        return reward;
    }

    public static String randomState(int times) {
        Random random = new Random();
        String state = finishedState;
        for (int i = 0; i < times; i++) {
            State nextState = next(state, random.nextInt(4));
            if (nextState != null) {
                state = nextState.getValue();
            }
        }
        System.out.println(state + ": gen random state");
        return state;
    }

    public static State next(String state, int action) {
        int idx0 = state.indexOf("0");
        String index0 = String.valueOf(idx0);
        String v = "";
        if (action == Action.TOP.ordinal()) {
            if ("012".contains(index0)) {
                return null;
            }
            v = swap(state, idx0 - 3, idx0);
        } else if (action == Action.BOTTOM.ordinal()) {
            if ("678".contains(index0)) {
                return null;
            }
            v = swap(state, idx0, idx0 + 3);
        } else if (action == Action.LEFT.ordinal()) {
            if ("036".contains(index0)) {
                return null;
            }
            v = swap(state, idx0 - 1, idx0);
        } else if (action == Action.RIGHT.ordinal()) {
            if ("258".contains(index0)) {
                return null;
            }
            v = swap(state, idx0, idx0 + 1);
        } else {
            return null;
        }
        Double r = v.equals(finishedState) ? rewardMax : 0;
        return new State(v, r);
    }

    public static String swap(String state, int i, int j) {
        char[] chars = state.toCharArray();
        char tmp = chars[i];
        chars[i] = chars[j];
        chars[j] = tmp;
        return new String(chars);
    }

    public static String shuffle(String state) {
        char[] chars = state.toCharArray();
        Random random = new Random();
        for (int i = chars.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char tmp = chars[i];
            chars[i] = chars[j];
            chars[j] = tmp;
        }
        return new String(chars);
    }
}
