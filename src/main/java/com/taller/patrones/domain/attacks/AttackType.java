package com.taller.patrones.domain.attacks;

public enum AttackType {
    NORMAL(NormalDamageStrategy::calculateDamage),
    SPECIAL(SpecialDamageStrategy::calculateDamage),
    STATUS(StatusDamageStrategy::calculateDamage),
    CRITICAL(CriticalDamageStrategy::calculateDamage);

    private final DamageStrategy strategy;

    AttackType(DamageStrategy strategy){
        this.strategy = strategy;
    }

    public int calculate(int attack, int defence, int basePower){
        return this.strategy.calculateDamage(attack, defence, basePower);
    }

    public static int calculateRawDamage(int attackStat, int basePower){
        return attackStat * basePower / 100;
    }

    public static int calculateDamageOr1(int rawDamage, int defenseStat){
        return Math.max(1, rawDamage - defenseStat);
    }
}
