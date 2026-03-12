package com.taller.patrones.domain.attacks;

/**
 * Representa un ataque que puede ejecutar un personaje.
 */
public record Attack(String name, int basePower, AttackType type) {

}
