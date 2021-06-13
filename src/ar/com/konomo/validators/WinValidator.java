package ar.com.konomo.validators;

import ar.com.konomo.entity.Player;
import ar.com.konomo.entity.Shinobi;

import java.util.List;

public class WinValidator {
    public boolean winConditionsMet(Player p1, Player p2){

        return allNinjasKilled(p1) || allNinjasKilled(p2);
    }

    private boolean allNinjasKilled (Player player) {
        boolean allDead = true;
        List<Shinobi> playerTroops = player.getMyNinjas();

        for (Shinobi ninja: playerTroops
             ) {
            if (ninja.isAlive()) {
                allDead = false;
            }
        }
        return allDead;
    }
}
