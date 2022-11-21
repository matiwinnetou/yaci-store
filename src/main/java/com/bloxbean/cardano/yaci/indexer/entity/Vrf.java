package com.bloxbean.cardano.yaci.indexer.entity;

import com.bloxbean.cardano.yaci.core.model.VrfCert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vrf {
    private String output;
    private String proof;

    public static Vrf from(VrfCert vrfCert) {
        if (vrfCert == null)
            return null;

        return new Vrf(vrfCert.get_1(), vrfCert.get_2());
    }
}
