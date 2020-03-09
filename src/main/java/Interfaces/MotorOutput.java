package Interfaces;

import nn.Action;

import java.util.List;

public interface MotorOutput {
    void takeAction(List<Action> recommended);
}

