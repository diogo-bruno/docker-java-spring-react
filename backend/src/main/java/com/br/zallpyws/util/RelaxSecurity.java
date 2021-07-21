package com.br.zallpyws.util;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import lombok.SneakyThrows;

public class RelaxSecurity {

  @SneakyThrows
  public static void permitirTodosOsCertificadosHTTPS() {

    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
      public boolean verify(String hostname, SSLSession session) {
        return true;
      }
    });

    SSLContext ctx = SSLContext.getInstance("TLS");

    X509TrustManager tm = new X509TrustManager() {
      public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
      }

      public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
      }

      public X509Certificate[] getAcceptedIssuers() {
        return null;
      }
    };

    ctx.init(null, new TrustManager[] { tm }, null);
    SSLContext.setDefault(ctx);
  }

}
