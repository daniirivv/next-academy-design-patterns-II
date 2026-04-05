package com.taller.patrones.domain.attacks.strategy;

public final class StatusDamageStrategy {

    private StatusDamageStrategy() {
    }

    public static int calculateDamage(int attackStat, int defenseStat, int basePower) {
        // Lo suyo sería añadir estados a los personajes; ahora mismo este ataque tiene 0 sentido

        // A veeer... que yo te entiendo ¿eh? Pero este ataque ya existía en la aplicación con un daño base, si en la
        // vida real te piden refactorizar y si algo no tiene sentido, lo cambias... Yo no te diré nada, pero pediré
        // ir a la reunión en la que se lo expliques al cliente. E iré con un bol de palomitas.
        return 0;
    }
}
