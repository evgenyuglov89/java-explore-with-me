package ru.practicum.client;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import ru.practicum.dto.EndpointHitDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class StatsClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private static final String APP_NAME = "ewm-main-service";

    public StatsClient(
            RestTemplateBuilder builder,
            @Value("${stats.server.url:http://localhost:9090}") String baseUrl
    ) {
        this.baseUrl = baseUrl;
        this.restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(baseUrl))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build();
    }

    public ResponseEntity<Object> postStats(HttpServletRequest request) {
        EndpointHitDto dto = buildStatisticsDto(request);
        return sendRequest(HttpMethod.POST, "/hit", null, dto);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        Map<String, Object> params = Map.of(
                "start", encode(start.toString()),
                "end", encode(end.toString()),
                "uris", uris,
                "unique", unique
        );
        return sendRequest(HttpMethod.GET, "/stats?start={start}&end={end}&uris={uris}&unique={unique}", params, null);
    }

    private <T> ResponseEntity<Object> sendRequest(HttpMethod method, String path,
                                                   @Nullable Map<String, Object> params, @Nullable T body) {
        HttpEntity<T> entity = new HttpEntity<>(body, buildDefaultHeaders());
        try {
            ResponseEntity<Object> response = (params == null)
                    ? restTemplate.exchange(path, method, entity, Object.class)
                    : restTemplate.exchange(path, method, entity, Object.class, params);

            return wrapResponse(response);

        } catch (HttpStatusCodeException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getResponseBodyAsByteArray());
        }
    }

    private EndpointHitDto buildStatisticsDto(HttpServletRequest request) {
        return EndpointHitDto.builder()
                .app(APP_NAME)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private HttpHeaders buildDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private ResponseEntity<Object> wrapResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(response.getStatusCode());
        return response.hasBody() ? builder.body(response.getBody()) : builder.build();
    }
}
