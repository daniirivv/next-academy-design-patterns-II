package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.attacks.Attack;
import com.taller.patrones.domain.Character;
import com.taller.patrones.domain.attacks.AttackMovement;

/**
 * Motor de combate. Calcula daño y crea ataques.
 * <p>
 * Nota: Esta clase crece cada vez que añadimos un ataque nuevo o un tipo de daño distinto.
 */
public class CombatEngine {

    private final AttackFactory attackFactory = new AttackFactory();

    /**
     * Crea un ataque a partir de su nombre.
     * Cada ataque nuevo requiere modificar este método.
     */
    public Attack createAttack(AttackMovement attackMovement) {
        return this.attackFactory.createAttack(attackMovement);
    }

    /**
     * Calcula el daño según el tipo de ataque.
     * Cada fórmula nueva (ej. crítico, veneno con tiempo) requiere modificar este switch.
     */
    public int calculateDamage(Character attacker, Character defender, Attack attack) {
        return attack.type().calculate(attacker.getAttack(), defender.getDefense(), attack.basePower());
    }
}
