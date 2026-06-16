package com.ludoelite.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.WebApplicationType;

@SpringBootApplication
public class LudoBackendApplication {

    public static void main(String[] args) {
        // Jangan gunakan SpringApplication.run() secara langsung.
        SpringApplication app = new SpringApplication(LudoBackendApplication.class);

        // INI KUNCI UTAMANYA: Paksa aplikasi menjadi Web Server agar terus hidup
        app.setWebApplicationType(WebApplicationType.SERVLET);

        // Jalankan aplikasi
        app.run(args);

        System.out.println("\n" +
                "═══════════════════════════════════════════════════════════════\n" +
                "  🎲 Ludo Elite Backend Started Successfully! 🎲\n" +
                "═══════════════════════════════════════════════════════════════\n" +
                "  REST API:        http://localhost:8080/api\n" +
                "  WebSocket:       ws://localhost:8080/ws-ludo\n" +
                "  H2 Console:      http://localhost:8080/h2-console\n" +
                "  Swagger UI:      http://localhost:8080/swagger-ui.html\n" +
                "═══════════════════════════════════════════════════════════════\n"
        );
    }
}