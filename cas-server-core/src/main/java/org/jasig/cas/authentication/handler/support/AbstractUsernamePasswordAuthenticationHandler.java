/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package org.jasig.cas.authentication.handler.support;

import org.inspektr.common.ioc.annotation.NotNull;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.PasswordEncoder;
import org.jasig.cas.authentication.handler.PlainTextPasswordEncoder;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;

/**
 * Abstract class to override supports so that we don't need to duplicate the
 * check for UsernamePasswordCredentials.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 * <p>
 * This is a published and supported CAS Server 3 API.
 * </p>
 */
public abstract class AbstractUsernamePasswordAuthenticationHandler extends
    AbstractPreAndPostProcessingAuthenticationHandler {

    /** Default class to support if one is not supplied. */
    private static final Class<UsernamePasswordCredentials> DEFAULT_CLASS = UsernamePasswordCredentials.class;

    /** Class that this instance will support. */
    @NotNull
    private Class< ? > classToSupport = DEFAULT_CLASS;

    /**
     * Boolean to determine whether to support subclasses of the class to
     * support.
     */
    private boolean supportSubClasses = true;

    /**
     * PasswordEncoder to be used by subclasses to encode passwords for
     * comparing against a resource.
     */
    @NotNull
    private PasswordEncoder passwordEncoder = new PlainTextPasswordEncoder();

    /**
     * Method automatically handles conversion to UsernamePasswordCredentials
     * and delegates to abstract authenticateUsernamePasswordInternal so
     * subclasses do not need to cast.
     */
    protected final boolean doAuthentication(final Credentials credentials)
        throws AuthenticationException {
        return authenticateUsernamePasswordInternal((UsernamePasswordCredentials) credentials);
    }

    /**
     * Abstract convenience method that assumes the credentials passed in are a
     * subclass of UsernamePasswordCredentials.
     * 
     * @param credentials the credentials representing the Username and Password
     * presented to CAS
     * @return true if the credentials are authentic, false otherwise.
     * @throws AuthenticationException if authenticity cannot be determined.
     */
    protected abstract boolean authenticateUsernamePasswordInternal(
        final UsernamePasswordCredentials credentials)
        throws AuthenticationException;

    /**
     * Method to return the PasswordEncoder to be used to encode passwords.
     * 
     * @return the PasswordEncoder associated with this class.
     */
    protected final PasswordEncoder getPasswordEncoder() {
        return this.passwordEncoder;
    }

    /**
     * Method to set the class to support.
     * 
     * @param classToSupport the class we want this handler to support
     * explicitly.
     */
    public final void setClassToSupport(final Class< ? > classToSupport) {
        this.classToSupport = classToSupport;
    }

    /**
     * Method to set whether this handler will support subclasses of the
     * supported class.
     * 
     * @param supportSubClasses boolean of whether to support subclasses or not.
     */
    public final void setSupportSubClasses(final boolean supportSubClasses) {
        this.supportSubClasses = supportSubClasses;
    }

    /**
     * Sets the PasswordEncoder to be used with this class.
     * 
     * @param passwordEncoder the PasswordEncoder to use when encoding
     * passwords.
     */
    public final void setPasswordEncoder(final PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * @return true if the credentials are not null and the credentials class is
     * equal to the class defined in classToSupport.
     */
    public final boolean supports(final Credentials credentials) {
        return credentials != null
            && (this.classToSupport.equals(credentials.getClass()) || (this.classToSupport
                .isAssignableFrom(credentials.getClass()))
                && this.supportSubClasses);
    }
}