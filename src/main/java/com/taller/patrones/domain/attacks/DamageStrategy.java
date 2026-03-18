package com.taller.patrones.domain.attacks;

@FunctionalInterface
public interface DamageStrategy {
    int calculateDamage(int atk, int def, int power);
}
