package com.codecool.dungeoncrawl.logic.util;

import com.codecool.dungeoncrawl.logic.actors.Actor;

import java.util.concurrent.TimeUnit;

public class Util {

    public static void waitOneSec() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Can not wait for some reason, Mr. Developer");
        }
    }

    public int getAttackerHit(Actor attacker, Actor defender) {
        return Util.getRandomNumber(
                attacker.getAttack() + NumberParameters.ATTACK_BONUS.getValue(),
                attacker.getAttack() - NumberParameters.ATTACK_NERF.getValue())
                - (defender.getDefense() / NumberParameters.DEFENSE_DIVISOR.getValue()
        );
    }

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
