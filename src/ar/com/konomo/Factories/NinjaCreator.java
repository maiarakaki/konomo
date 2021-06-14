package ar.com.konomo.Factories;

import ar.com.konomo.entity.Shinobi;
import ar.com.konomo.enums.NinjaType;

import java.util.ArrayList;
import java.util.List;

public class NinjaCreator {
    static int CHUNIN_STAMINA = NinjaType.CHUUNIN.getBaseStamina();
    static int JOUNIN_STAMINA = NinjaType.JOUNIN.getBaseStamina();

    public List<Shinobi> create (int numberOfNinjas){
        List <Shinobi> ninjaList = new ArrayList<>();

        for (int i = 0; i < numberOfNinjas -1; i++) {
            ninjaList.add(create(NinjaType.CHUUNIN));
        }
        ninjaList.add(create(NinjaType.JOUNIN));

        return ninjaList;
    }

    private Shinobi create(NinjaType ninjaType){
        Shinobi shinobi = new Shinobi();
        shinobi.setAlive(true);
        shinobi.setNinjaType(ninjaType);

        switch (ninjaType) {
            case JOUNIN:
                shinobi.setStamina(JOUNIN_STAMINA);
                break;
            case CHUUNIN:
                shinobi.setStamina(CHUNIN_STAMINA);
                break;
        }
        return shinobi;
    }

}
