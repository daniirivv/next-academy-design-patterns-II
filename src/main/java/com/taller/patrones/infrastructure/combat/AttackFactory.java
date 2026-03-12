package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.attacks.*;

import java.util.EnumMap;
import java.util.Map;

import static com.taller.patrones.domain.attacks.AttackMovement.*;

public class AttackFactory {

    private final Map<AttackMovement, Attack> attackMap = new EnumMap<>(
            Map.of(
                    TACKLE, new Attack("Tackle", 40, AttackType.NORMAL),
                    SLASH, new Attack("Slash", 55, AttackType.NORMAL),
                    FIREBALL, new Attack("Fireball", 80, AttackType.SPECIAL),
                    ICE_BEAM, new Attack("Ice Beam", 70, AttackType.SPECIAL),
                    POISON_STING, new Attack("Poison Sting", 20, AttackType.STATUS),
                    THUNDER, new Attack("Hit", 30, AttackType.NORMAL)
            )
    );

    public Attack createAttack(AttackMovement attackMovement){
        return attackMap.get(attackMovement);
    }

}
