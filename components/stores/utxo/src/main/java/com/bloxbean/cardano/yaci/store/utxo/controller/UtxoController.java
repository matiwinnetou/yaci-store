package com.bloxbean.cardano.yaci.store.utxo.controller;

import com.bloxbean.cardano.yaci.store.common.domain.AddressUtxo;
import com.bloxbean.cardano.yaci.store.common.domain.UtxoKey;
import com.bloxbean.cardano.yaci.store.utxo.service.UtxoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${apiPrefix}/utxos")
@RequiredArgsConstructor
@Slf4j
public class UtxoController {
    private final UtxoService utxoService;

    @GetMapping(value = "/{txHash}/{index}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<AddressUtxo> getUtxo(@PathVariable String txHash, @PathVariable Integer index) {
        return utxoService.getUtxo(txHash, index);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AddressUtxo> getUtxos(@RequestBody List<UtxoKey> utxoIds) {
        return utxoService.getUtxos(utxoIds);
    }

}
