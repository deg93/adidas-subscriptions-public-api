package es.davidenjuan.subscriptions.publicapi;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import es.davidenjuan.subscriptions.publicapi.configuration.ApplicationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class SubscriptionsPublicApiApplication {

    private static final Logger log = LoggerFactory.getLogger(SubscriptionsPublicApiApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext appContext = SpringApplication.run(SubscriptionsPublicApiApplication.class, args);
        logApplicationReady(appContext.getEnvironment());
    }

    private static void logApplicationReady(Environment environment) {
        String serverPort = environment.getProperty("server.port");
        String contextPath = Optional
            .ofNullable(environment.getProperty("server.servlet.context-path"))
            .filter(StringUtils::hasText)
            .orElse("/");
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info(
            "\n-------------------------------------------------------------------------\n\t" +
            "Application '{}' is running! Access URLs:\n\t" +
            "Local: \t\thttp://localhost:{}{}\n\t" +
            "API docs: \thttp://localhost:{}{}swagger-ui/index.html\n\t" +
            "External: \thttp://{}:{}{}\n\t" +
            "Profile(s): \t{}\n-------------------------------------------------------------------------",
            environment.getProperty("spring.application.name"),
            serverPort,
            contextPath,
            serverPort,
            contextPath,
            hostAddress,
            serverPort,
            contextPath,
            environment.getActiveProfiles().length == 0 ? environment.getDefaultProfiles() : environment.getActiveProfiles()
        );
    }
}
