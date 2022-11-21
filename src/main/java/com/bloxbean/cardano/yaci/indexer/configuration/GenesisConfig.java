package com.bloxbean.cardano.yaci.indexer.configuration;

import com.bloxbean.cardano.yaci.core.model.Era;
import org.springframework.stereotype.Component;

@Component
public class GenesisConfig {

    public long slotDuration(Era era) {
        if (era == Era.Byron)
            return 20; //20 sec
        else
            return 1; //1 sec
    }

    public long slotsPerEpoch(Era era) {
        long totalSecsIn5DaysEpoch = 432000;
        return totalSecsIn5DaysEpoch / slotDuration(era);
    }

}
