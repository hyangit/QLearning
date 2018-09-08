package com.yanhao.qlearning;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class QLearningTest {
    @Test
    public void testChooseAction() {
        QTable qTable = new QTable("model.csv");
        QLearning qLearning = new QLearning(qTable, 0.01, 0.9, 0.9);
        List<Integer> actions = new ArrayList<Integer>(4) {{
            add(0);
            add(1);
            add(2);
            add(3);
        }};
        for (int i = 0; i < 100; i++) {
            int action = qLearning.chooseAction("a");
            System.out.println(action);
            Assert.assertTrue(actions.contains(action));
        }
    }
}
