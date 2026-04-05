package com.taller.patrones.domain.attacks.strategy;

@FunctionalInterface
public interface DamageStrategy {
    int calculateDamage(int atk, int def, int power);
}
