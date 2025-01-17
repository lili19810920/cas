package org.jasig.cas.adaptors.x509.authentication.principal;

import java.security.cert.X509Certificate;

/**
 * Returns a new principal based on the Sereial Number of the certificate.
 *
 * @author Scott Battaglia
 * @since 3.0.0.4
 */
public final class X509SerialNumberPrincipalResolver extends AbstractX509PrincipalResolver {

    @Override
    protected String resolvePrincipalInternal(
            final X509Certificate certificate) {
        return certificate.getSerialNumber().toString();
    }
}
