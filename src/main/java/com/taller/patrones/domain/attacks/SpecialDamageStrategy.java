package com.taller.patrones.domain.attacks;

public final class SpecialDamageStrategy {

    private SpecialDamageStrategy(){}

    public static int calculateDamage(int attackStat, int defenseStat, int basePower) {
        int rawDamage = AttackType.calculateRawDamage(attackStat, basePower);
        return AttackType.calculateDamageOr1(rawDamage, defenseStat/2);
    }
}
