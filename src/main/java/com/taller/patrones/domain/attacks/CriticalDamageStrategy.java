package com.taller.patrones.domain.attacks;

import java.util.Random;

public final class CriticalDamageStrategy {

    // Hay que hacer que algún ataque sea de este tipo para que tenga algo de sentido
    private CriticalDamageStrategy(){}

    public static int calculateDamage(int attackStat, int defenseStat, int basePower) {
        int rawDamage = (int) (AttackType.calculateRawDamage(attackStat, basePower) * 1.5);
        Random random = new Random();
        if(random.nextDouble(1) < 0.2) return AttackType.calculateDamageOr1(rawDamage, defenseStat);
        else return 0;
    }
}
