package com.taller.patrones.domain.attacks.strategy;

import com.taller.patrones.domain.attacks.AttackType;

public final class NormalDamageStrategy {

    private NormalDamageStrategy() {
    }

    public static int calculateDamage(int attackStat, int defenseStat, int basePower) {
        int rawDamage = AttackType.calculateRawDamage(attackStat, basePower);
        return AttackType.calculateDamageOr1(rawDamage, defenseStat);
    }
}
