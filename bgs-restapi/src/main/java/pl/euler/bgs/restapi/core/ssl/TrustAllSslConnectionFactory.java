package pl.euler.bgs.restapi.core.ssl;

import javaslang.control.Try;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import static pl.euler.bgs.restapi.core.Exceptions.toIllegalStateException;

public class TrustAllSslConnectionFactory {
        private TrustAllSslConnectionFactory() {}

        public static RestTemplate setupRestTemplate() {
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(Try.of(TrustAllSslConnectionFactory::setupHttpsClient).orElseThrow(
                    toIllegalStateException("Cannot setup SSL trust all request factory!")
            ));
            return new RestTemplate(requestFactory);
        }

        private static HttpClient setupHttpsClient() throws Exception {
            setUntrustedCertificateForUrlHttpsConnection();
            SSLContext sslContext = buildSSlContext();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        }

        private static SSLContext buildSSlContext() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
            return SSLContexts.custom()
                    .setSecureRandom(new SecureRandom())
                    .loadTrustMaterial(null, (TrustStrategy) (chain, authType) -> true)
                    .build();
        }

        private static void setUntrustedCertificateForUrlHttpsConnection() throws Exception {
            HttpsURLConnection.setDefaultSSLSocketFactory(buildSSlContext().getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new NoopHostnameVerifier());
        }

}
