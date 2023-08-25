package py.com.daas.microservice.gateway.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "identity-service")
public interface AuthServiceClient {

    @GetMapping("/api/auth/validate")
    boolean validateToken(@RequestParam("token") String token);
}
