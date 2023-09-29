package io.camunda.zeebe.example.job;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProvider;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProviderBuilder;

public class MyZeebeClientBuilder {

    private static final String zeebeAddress = "localhost:26500";
    private static final String audience = "zeebe-api";
    private static final String clientId = "zeebe";
    private static final String clientSecret = "XALaRPl5qwTEItdwCMiPS62nVpKs7dL7";
    private static final String oAuthAPI = "http://localhost:18080/auth/realms/camunda-platform/protocol/openid-connect/token/";

    public static ZeebeClient createZbclient () {

        ZeebeClient client = null;

        try {
            OAuthCredentialsProvider credentialsProvider =
                    new OAuthCredentialsProviderBuilder()
                            .authorizationServerUrl(oAuthAPI)
                            .audience(audience)
                            .clientId(clientId)
                            .clientSecret(clientSecret)
                            .build();

           client = ZeebeClient.newClientBuilder()
                    .gatewayAddress(zeebeAddress)
                    .usePlaintext()
                    .credentialsProvider(credentialsProvider)
                    .build();
           System.out.println(client.getConfiguration().getGatewayAddress());
           client.newTopologyRequest().send().join();

        } catch (Exception e) {
            System.out.println("!!! error in creating client: "+ e.getCause().getMessage());
            System.exit(999);
        }
        return client;
    }

}
