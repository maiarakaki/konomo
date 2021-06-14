package ar.com.konomo.validators;

import ar.com.konomo.entity.Player;
import ar.com.konomo.entity.Shinobi;

import java.util.List;

public class WinValidator {
    Player winner;

    public WinValidator() {
        winner = new Player();
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public boolean winConditionsMet(Player p1, Player p2){
            boolean p1Lost = allNinjasKilled(p1);
            boolean p2Lost = allNinjasKilled(p2);

            if (p1Lost) {setWinner(p2);} else if (p2Lost){setWinner(p1);}

        return  p1Lost || p2Lost;
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

    public String getWinner(){
        return winner.getName();
    }
}
