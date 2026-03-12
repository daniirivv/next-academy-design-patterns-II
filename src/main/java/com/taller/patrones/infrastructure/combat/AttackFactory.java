package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.attacks.*;

import java.util.EnumMap;
import java.util.Map;

import static com.taller.patrones.domain.attacks.AttackMovement.*;

public class AttackFactory {

    private final Map<AttackMovement, Attack> attackMap = new EnumMap<>(
            Map.of(
                    TACKLE, new TackleAttack(),
                    SLASH, new SlashAttack(),
                    FIREBALL, new FireballAttack(),
                    ICE_BEAM, new IceBeamAttack(),
                    POISON_STING, new PoisonStingAttack(),
                    THUNDER, new ThunderAttack()
            )
    );

    public Attack createAttack(AttackMovement attackMovement){
        return attackMap.get(attackMovement);
    }

}
