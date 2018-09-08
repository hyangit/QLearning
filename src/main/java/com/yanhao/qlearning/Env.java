package com.yanhao.qlearning;

public class Env {

    private String state;
    private Integer episode;
    private Long beginTime;
    private Integer step;

    public void show() {
        System.out.println(String.format("state[%s], episode[%s], step[%s], cost ms[%s]", state, episode, step, (System.nanoTime() - beginTime) / 1000000));
    }

    public void begin(String state) {
        this.state = state;
        episode = 1;
        step = 1;
        beginTime = System.nanoTime();
    }

    public void nextEpisode() {
        episode++;
    }

    public void nextStep() {
        step++;
    }
}
