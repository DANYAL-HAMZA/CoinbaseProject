package com.example.CoinbaseClone.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Web3ConfigTest {

    private static final String PRIVATE_KEY = "09f563e5b755d479f8138074588e19101b8d6269dd0e0e11856260d9f87c9293";
    private static final String DEFAULT_PROVIDER_URL = "https://sepolia.gateway.tenderly.co/6VnYb8FKynzGGiSo1aIjZn";

    @Test
    void normalizePrivateKeyAcceptsPlainHexKey() {
        assertEquals(PRIVATE_KEY, Web3Config.normalizePrivateKey(PRIVATE_KEY));
    }

    @Test
    void normalizePrivateKeyAcceptsKeyWithHexPrefix() {
        assertEquals(PRIVATE_KEY, Web3Config.normalizePrivateKey("0x" + PRIVATE_KEY));
    }

    @Test
    void normalizePrivateKeyRejectsTrailingBrace() {
        assertThrows(IllegalArgumentException.class, () -> Web3Config.normalizePrivateKey(PRIVATE_KEY + "}"));
    }

    @Test
    void normalizeProviderUrlUsesDefaultForBlankValue() {
        assertEquals(DEFAULT_PROVIDER_URL, Web3Config.normalizeProviderUrl(" "));
    }

    @Test
    void normalizeProviderUrlAcceptsHttpUrl() {
        assertEquals("https://example.com/rpc", Web3Config.normalizeProviderUrl(" https://example.com/rpc "));
    }

    @Test
    void normalizeProviderUrlRejectsMissingScheme() {
        assertThrows(IllegalArgumentException.class, () -> Web3Config.normalizeProviderUrl("example.com/rpc"));
    }
}
