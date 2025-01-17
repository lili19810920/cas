package org.jasig.cas.adaptors.jdbc;

import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;

import javax.security.auth.login.FailedLoginException;
import javax.validation.constraints.NotNull;
import java.security.GeneralSecurityException;

/**
 * Class that given a table, username field and password field will query a
 * database table with the provided encryption technique to see if the user
 * exists. This class defaults to a PasswordTranslator of
 * PlainTextPasswordTranslator.
 *
 * @author Scott Battaglia
 * @author Dmitriy Kopylenko
 * @author Marvin S. Addison
 *
 * @since 3.0.0
 */

public class SearchModeSearchDatabaseAuthenticationHandler extends AbstractJdbcUsernamePasswordAuthenticationHandler
        implements InitializingBean {

    private static final String SQL_PREFIX = "Select count('x') from ";

    @NotNull
    private String fieldUser;

    @NotNull
    private String fieldPassword;

    @NotNull
    private String tableUsers;

    private String sql;

    /**
     * {@inheritDoc}
     */
    @Override
    protected final HandlerResult authenticateUsernamePasswordInternal(final UsernamePasswordCredential credential)
            throws GeneralSecurityException, PreventedException {

        final String username = credential.getUsername();
        final String encyptedPassword = getPasswordEncoder().encode(credential.getPassword());
        final int count;
        try {
            count = getJdbcTemplate().queryForObject(this.sql, Integer.class, username, encyptedPassword);
        } catch (final DataAccessException e) {
            throw new PreventedException("SQL exception while executing query for " + username, e);
        }
        if (count == 0) {
            throw new FailedLoginException(username + " not found with SQL query.");
        }
        return createHandlerResult(credential, this.principalFactory.createPrincipal(username), null);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.sql = SQL_PREFIX + this.tableUsers + " WHERE " + this.fieldUser + " = ? AND " + this.fieldPassword
                + " = ?";
    }

    /**
     * @param fieldPassword The fieldPassword to set.
     */
    public final void setFieldPassword(final String fieldPassword) {
        this.fieldPassword = fieldPassword;
    }

    /**
     * @param fieldUser The fieldUser to set.
     */
    public final void setFieldUser(final String fieldUser) {
        this.fieldUser = fieldUser;
    }

    /**
     * @param tableUsers The tableUsers to set.
     */
    public final void setTableUsers(final String tableUsers) {
        this.tableUsers = tableUsers;
    }

}
