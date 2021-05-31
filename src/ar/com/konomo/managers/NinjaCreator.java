package ar.com.konomo.managers;

import ar.com.konomo.entity.Shinobi;

import java.util.ArrayList;
import java.util.List;

public class NinjaCreator {
    static int CHUNIN_STAMINA = 10;
    static int JOUNIN_STAMINA = 10;

    public List<Shinobi> create (int numberOfNinjas){
        List <Shinobi> ninjaList = new ArrayList<>();

        for (int i = 0; i < numberOfNinjas -1; i++) {
            ninjaList.add(create(false));
        }
        ninjaList.add(create(true));

        return ninjaList;
    }

    private Shinobi create(boolean isJounin){
        Shinobi shinobi = new Shinobi();
        shinobi.setAlive(true);
        shinobi.setJounin(isJounin);
        if (isJounin) {
            shinobi.setStamina(JOUNIN_STAMINA);
        } else {
            shinobi.setStamina(CHUNIN_STAMINA);
        }
        return shinobi;
    }

}
