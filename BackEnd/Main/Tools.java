package BackEnd.Main;

import java.util.Set;

class Tools {

    protected static int genRandIndex(Set set) {
        int randIndex;
        do {
            randIndex = (int) Math.round(Math.random() * Integer.MAX_VALUE);
        } while (set.contains(randIndex));
        return randIndex;
    }
}
