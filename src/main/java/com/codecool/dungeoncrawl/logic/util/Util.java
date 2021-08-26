package com.codecool.dungeoncrawl.logic.util;

import com.codecool.dungeoncrawl.logic.actors.Actor;

public class Util {

    public static int getAttackerHit(Actor attacker, Actor defender) {
        return getRandomNumber(
                attacker.getAttack() + NumberParameters.ATTACK_BONUS.getValue(),
                attacker.getAttack() - NumberParameters.ATTACK_NERF.getValue()
        ) - (defender.getDefense() / NumberParameters.DEFENSE_DIVISOR.getValue());
    }

    public static void exitGame() {
        System.exit(0);
    }

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
