package com.example.CoinbaseClone.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class Web3Config {
    private static final String PRIVATE_KEY_PATTERN = "^[0-9a-fA-F]{64}$";
    private static final String DEFAULT_PROVIDER_URL = "https://sepolia.gateway.tenderly.co/6VnYb8FKynzGGiSo1aIjZn";

    @Value("${web3j.provider.url}")
    private String clientAddress;

    @Value("${wallet.private.key}")
    private String privateKey;

    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService(normalizeProviderUrl(clientAddress)));
    }

    @Bean
    public Credentials credentials() {
        return Credentials.create(normalizePrivateKey(privateKey));
    }

    static String normalizePrivateKey(String configuredPrivateKey) {
        if (configuredPrivateKey == null) {
            throw new IllegalArgumentException("WALLET_PRIVATE_KEY must be set to a 64-character hexadecimal private key.");
        }

        String normalizedPrivateKey = configuredPrivateKey.trim();
        if (normalizedPrivateKey.startsWith("0x") || normalizedPrivateKey.startsWith("0X")) {
            normalizedPrivateKey = normalizedPrivateKey.substring(2);
        }

        if (!normalizedPrivateKey.matches(PRIVATE_KEY_PATTERN)) {
            throw new IllegalArgumentException(
                    "WALLET_PRIVATE_KEY must be exactly 64 hexadecimal characters, with optional 0x prefix. "
                            + "Do not include braces, quotes, property placeholders, or trailing punctuation.");
        }

        return normalizedPrivateKey;
    }

    static String normalizeProviderUrl(String configuredProviderUrl) {
        String normalizedProviderUrl = configuredProviderUrl == null ? "" : configuredProviderUrl.trim();
        if (normalizedProviderUrl.isBlank() || "null".equalsIgnoreCase(normalizedProviderUrl)) {
            normalizedProviderUrl = DEFAULT_PROVIDER_URL;
        }

        URI providerUri;
        try {
            providerUri = new URI(normalizedProviderUrl);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("WEB3J_PROVIDER_URL must be a valid HTTP or HTTPS RPC URL.", e);
        }

        String scheme = providerUri.getScheme();
        if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
            throw new IllegalArgumentException("WEB3J_PROVIDER_URL must start with http:// or https://.");
        }

        if (providerUri.getHost() == null || providerUri.getHost().isBlank()) {
            throw new IllegalArgumentException("WEB3J_PROVIDER_URL must include a host name.");
        }

        return normalizedProviderUrl;
    }
}
