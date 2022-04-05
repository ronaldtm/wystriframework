package org.wystriframework.examples;

import javax.naming.NamingException;

import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory.Client;
import org.eclipse.jetty.webapp.WebAppContext;

public class Start {
    /**
     * Main function, starts the jetty server.
     *
     * @param args
     * @throws NamingException 
     */
    public static void main(String[] args) throws NamingException {
        System.out.println(System.getProperty("java.home"));
        System.setProperty("wicket.configuration", "development");
        System.setProperty("spring.profiles.active", "development");

        Server server = new Server();
        org.eclipse.jetty.webapp.Configuration.ClassList classlist = org.eclipse.jetty.webapp.Configuration.ClassList.setServerDefault(server);
        classlist.addAfter(
            "org.eclipse.jetty.webapp.FragmentConfiguration",
            "org.eclipse.jetty.plus.webapp.EnvConfiguration",
            "org.eclipse.jetty.plus.webapp.PlusConfiguration");

        HttpConfiguration http_config = new HttpConfiguration();
        http_config.setSecureScheme("https");
        http_config.setSecurePort(8443);
        http_config.setOutputBufferSize(32768);

        ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(http_config));
        http.setPort(8080);
        http.setIdleTimeout(1000 * 60 * 60);

        server.addConnector(http);

        Resource keystore = Resource.newClassPathResource("/keystore");
        if (keystore != null && keystore.exists()) {
            // if a keystore for a SSL certificate is available, start a SSL
            // connector on port 8443.
            // By default, the quickstart comes with a Apache Wicket Quickstart
            // Certificate that expires about half way september 2021. Do not
            // use this certificate anywhere important as the passwords are
            // available in the source.

            SslContextFactory sslContextFactory = new Client();
            sslContextFactory.setKeyStoreResource(keystore);
            sslContextFactory.setKeyStorePassword("wicket");
            sslContextFactory.setKeyManagerPassword("wicket");

            HttpConfiguration https_config = new HttpConfiguration(http_config);
            https_config.addCustomizer(new SecureRequestCustomizer());

            ServerConnector https = new ServerConnector(server, new SslConnectionFactory(
                sslContextFactory, "http/1.1"), new HttpConnectionFactory(https_config));
            https.setPort(8443);
            https.setIdleTimeout(500000);

            server.addConnector(https);
            System.out.println("SSL access to the examples has been enabled on port 8443");
            System.out.println("You can access the application using SSL on https://localhost:8443");
            System.out.println();
        }

        WebAppContext bb = new WebAppContext();
        bb.setServer(server);
        bb.setContextPath("/");
        bb.setWar("src/main/webapp");

        // uncomment the next two lines if you want to start Jetty with WebSocket (JSR-356) support
        // you need org.apache.wicket:wicket-native-websocket-javax in the classpath!
        // ServerContainer serverContainer = WebSocketServerContainerInitializer.configureContext(bb);
        // serverContainer.addEndpoint(new WicketServerEndpointConfig());

        // uncomment next line if you want to test with JSESSIONID encoded in the urls
        // ((AbstractSessionManager)
        // bb.getSessionHandler().getSessionManager()).setUsingCookies(false);

        server.setHandler(bb);

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(100);
        }
    }
}
