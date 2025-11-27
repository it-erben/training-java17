package tech.erben.gfu.httpclient;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HttpClientAssignmentsTest {

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(options().dynamicPort())
            .build();

    private HttpClientAssignments assignments;

    @BeforeEach
    void setUp() {
        wireMock.resetAll();
        assignments = new HttpClientAssignments(
                HttpClient.newBuilder()
                        .connectTimeout(Duration.ofSeconds(1))
                        .build(),
                URI.create(wireMock.getRuntimeInfo().getHttpBaseUrl()));
    }

    @Test
    void fetchesActiveStatus() throws Exception {
        wireMock.stubFor(get(urlPathEqualTo("/get"))
                .withQueryParam("status", equalTo("active"))
                .willReturn(ok("status=active-ok")));

        String body = assignments.fetchActiveStatus();

        assertThat(body).isEqualTo("status=active-ok");
        wireMock.verify(getRequestedFor(urlPathEqualTo("/get"))
                .withQueryParam("status", equalTo("active")));
    }

    @Test
    void postsOwnerData() throws Exception {
        wireMock.stubFor(post(urlEqualTo("/post"))
                .withHeader("Content-Type", containing("application/json"))
                .withHeader("X-Auth-Token", equalTo("secret123"))
                .withRequestBody(equalToJson("""
                        {
                          "id": "DE-999",
                          "owner": "Mustermann"
                        }
                        """))
                .willReturn(ok("{\"ok\":true,\"received\":true}")));

        String body = assignments.postOwnerData("DE-999", "Mustermann");

        assertThat(body).contains("\"ok\":true");
        wireMock.verify(postRequestedFor(urlEqualTo("/post"))
                .withRequestBody(containing("DE-999"))
                .withRequestBody(containing("Mustermann")));
    }

    @Test
    void timesOutWhenServerIsTooSlow() {
        wireMock.stubFor(get(urlEqualTo("/delay/5"))
                .willReturn(ok("slow response").withFixedDelay(3000)));

        assertThatThrownBy(() -> assignments.callWithTimeout(5, Duration.ofSeconds(1)))
                .isInstanceOf(HttpTimeoutException.class);
    }

    @Test
    void returnsResponseWhenWithinTimeout() throws Exception {
        wireMock.stubFor(get(urlEqualTo("/delay/1"))
                .willReturn(ok("done").withFixedDelay(100)));

        HttpResponse<String> response = assignments.callWithTimeout(1, Duration.ofSeconds(2));

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).isEqualTo("done");
    }

    @Test
    void fetchesDashboardDataInParallel() throws Exception {
        wireMock.stubFor(get(urlPathEqualTo("/get"))
                .withQueryParam("q", equalTo("status"))
                .willReturn(ok("System Operational")));
        wireMock.stubFor(get(urlPathEqualTo("/get"))
                .withQueryParam("q", equalTo("quota"))
                .willReturn(ok("100/5000")));
        wireMock.stubFor(get(urlPathEqualTo("/get"))
                .withQueryParam("q", equalTo("news"))
                .willReturn(ok("No new notifications")));

        HttpClientAssignments.DashboardData data = assignments.fetchDashboardData();

        assertThat(data.status()).isEqualTo("System Operational");
        assertThat(data.quota()).isEqualTo("100/5000");
        assertThat(data.news()).isEqualTo("No new notifications");
    }

    @Test
    void streamsLinesAndTransforms() throws Exception {
        wireMock.stubFor(get(urlEqualTo("/stream/4"))
                .willReturn(ok("""
                        line 1

                        line 2
                        Line 3
                        """)));

        List<String> lines = assignments.streamLines(4);

        assertThat(lines).containsExactly("LINE 1", "LINE 2", "LINE 3");
    }
}
