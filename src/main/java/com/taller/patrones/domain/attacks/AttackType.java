package com.taller.patrones.domain.attacks;

import com.taller.patrones.domain.attacks.strategy.*;

public enum AttackType {
    NORMAL(NormalDamageStrategy::calculateDamage),
    SPECIAL(SpecialDamageStrategy::calculateDamage),
    STATUS(StatusDamageStrategy::calculateDamage),
    CRITICAL(CriticalDamageStrategy::calculateDamage);

    private final DamageStrategy strategy;

    AttackType(DamageStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * No sé si me gusta esto, al final estás creando un enum y haciendo que dependa de un strategy cuando si hubieras usado un strategy
     * te habrías ahorrado clases. Imagínate el dagrama UML.
     * <p>
     * Prioriza que el código se entienda con leerlo sólo una vez. SI no, tus compañeros perderán tiempo entendiéndolo, y tú mismo en unas semanas, te
     * perderás. ¿Es más interesante esto? Claro, ¿Más avanzado? Si. En la práctica funcionaría igual que una solución más simple y sería más difícil de
     * trabajar con ello? También.
     * <p>
     * Si en el futuro necesitas optimizar un código para que ejcute más rápido o lo que sea, ya lo harás después de tener la funcionalidad y hacer
     * pruebas de carga, reunir los requisitos del cliente y saber tu insfrastructura. No te adelantes a hacer trabajo que igual luego no necesitas.
     */
    public static int calculateRawDamage(int attackStat, int basePower) {
        return attackStat * basePower / 100;
    }

    public static int calculateDamageOr1(int rawDamage, int defenseStat) {
        return Math.max(1, rawDamage - defenseStat);
    }

    public int calculate(int attack, int defence, int basePower) {
        return this.strategy.calculateDamage(attack, defence, basePower);
    }
}
