package com.bloxbean.cardano.yaci.store.script.storage.impl.jpa.model;

import com.bloxbean.cardano.yaci.store.common.model.BaseEntity;
import com.bloxbean.cardano.yaci.store.script.domain.ScriptType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "transaction_scripts")
public class TxScriptEntity extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "tx_hash")
    private String txHash;

    @Column(name = "script_hash")
    private String scriptHash;

    @Column(name = "slot")
    private Long slot;

    @Column(name = "block")
    private Long block;

    @Column(name = "block_hash")
    private String blockHash;

    @Column(name = "script_type")
    private ScriptType type;

    @Column(name = "redeemer")
    private String redeemer;

    @Column(name = "datum")
    private String datum;

    @Column(name = "datum_hash")
    private String datumHash;

}
