package io.rizvan.utils;

import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import py4j.GatewayServer;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Singleton
@Startup
public class PythonGateway {
    private GatewayServer bayesGatewayServer;
    private GatewayServer sentimentGatewayServer;
    private BayesPythonManager bayesManager;
    private SentimentPythonManager sentimentPythonManager;

    @PostConstruct
    public void init() throws UnknownHostException {

        String bayesHost = System.getenv("BAYES_HOST");
        String sentimentHost = System.getenv("SENTIMENT_HOST");

        InetAddress bayesPythonAddress = (bayesHost != null)
                ? InetAddress.getByName(bayesHost)
                : GatewayServer.defaultAddress();

        InetAddress sentimentPythonAddress = (sentimentHost != null)
                ? InetAddress.getByName(sentimentHost)
                : GatewayServer.defaultAddress();

        InetAddress javaServerAddress = (bayesHost != null || sentimentHost != null)
                ? InetAddress.getLocalHost()
                : GatewayServer.defaultAddress();

        bayesGatewayServer = new GatewayServer(
                null,                    // entryPoint
                25333,                   // port
                25334,                   // pythonPort
                javaServerAddress, // GatewayServer.defaultAddress(),  // address
                bayesPythonAddress, // GatewayServer.defaultAddress(),  // pythonAddress
                GatewayServer.DEFAULT_CONNECT_TIMEOUT,  // connectTimeout
                GatewayServer.DEFAULT_READ_TIMEOUT,     // readTimeout
                null                      // customCommands
        );
        bayesGatewayServer.start();

        System.out.println("bayesGatewayServer address: " + bayesGatewayServer.getPythonAddress() + ":" + bayesGatewayServer.getPythonPort());
        System.out.println("gatewayServer: " + InetAddress.getLocalHost());

        // Initialize the Sentiment Gateway Server with its own Python and callback ports
        sentimentGatewayServer = new GatewayServer(
                null,
                25335,
                25336,
                javaServerAddress, // GatewayServer.defaultAddress(),  // address
                sentimentPythonAddress,  // GatewayServer.defaultAddress(),  // pythonAddress
                GatewayServer.DEFAULT_CONNECT_TIMEOUT,  // connectTimeout
                GatewayServer.DEFAULT_READ_TIMEOUT,
                null
        );
        sentimentGatewayServer.start();

        System.out.println("sentimentGatewayServer address: " + sentimentGatewayServer.getPythonAddress() + ":" + sentimentGatewayServer.getPythonPort());


        bayesManager = (BayesPythonManager) bayesGatewayServer.getPythonServerEntryPoint(new Class[]{BayesPythonManager.class});
        sentimentPythonManager = (SentimentPythonManager) sentimentGatewayServer.getPythonServerEntryPoint(new Class[]{SentimentPythonManager.class});
    }

    public BayesPythonManager getBayesNetwork() {
        return bayesManager;
    }

    public SentimentPythonManager getSentimentAnalyser() {
        return sentimentPythonManager;
    }
}
