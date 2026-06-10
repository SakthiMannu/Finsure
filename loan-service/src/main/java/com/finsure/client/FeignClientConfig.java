package com.finsure.client;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig {

    @Bean
    RequestInterceptor requestInterceptor() {
        return requestTemplate -> {

            requestTemplate.header("X-Internal-Call", "true");

            try {
                ServletRequestAttributes attributes =
                        (ServletRequestAttributes) RequestContextHolder
                                .getRequestAttributes();

                if (attributes != null) {
                    String role = attributes.getRequest()
                            .getHeader("X-User-Role");
                    String email = attributes.getRequest()
                            .getHeader("X-User-Email");

                    if (role != null) {
                        requestTemplate.header("X-User-Role", role);
                    }
                    if (email != null) {
                        requestTemplate.header("X-User-Email", email);
                    }
                }

            } catch (Exception e) {


                System.out.println("FeignClientConfig: "
                        + "Running in non-request context "
                        + "(scheduled task) — skipping role header");
            }
        };
    }
}
