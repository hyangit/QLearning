package com.yanhao.qlearning;

import org.junit.Test;

public class StateTest {
    @Test
    public void testSwap() {
        System.out.println(State.swap("123456780", 5, 8));
        System.out.println(State.swap("123456780", 7, 8));
    }

    @Test
    public void testShuffle() {
        for (int i = 0; i < 10; i++) {
            System.out.println(State.shuffle("123456780"));
        }
    }
}
