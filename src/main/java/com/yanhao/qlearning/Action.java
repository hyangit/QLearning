package com.yanhao.qlearning;

public enum Action {
    TOP,
    BOTTOM,
    LEFT,
    RIGHT;

    public static Action valueOf(int ordinal) {
        for (Action action : Action.values()) {
            if (action.ordinal() == ordinal) {
                return action;
            }
        }
        return null;
    }
}
