package com.company.application.authentication;

import com.company.application.security.LdapProperties;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import org.springframework.stereotype.Service;

/**
 * Searches LDAP users with the enterprise employeeId filter.
 */
@Service
public class LdapSearchService {

    private final LdapProperties properties;

    public LdapSearchService(LdapProperties properties) {
        this.properties = properties;
    }

    public String findDistinguishedName(DirContext context, String username) throws NamingException {
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        controls.setCountLimit(1);
        String filter = "(&(|(objectclass=userproxy)(objectclass=user))(employeeId={0}))";
        Object[] arguments = new Object[] { username };
        NamingEnumeration<SearchResult> results = context.search(properties.getPeopleDirectory(), filter, arguments, controls);
        if (!results.hasMore()) {
            return null;
        }
        Attributes attributes = results.next().getAttributes();
        if (attributes == null || attributes.get("distinguishedName") == null) {
            return null;
        }
        return String.valueOf(attributes.get("distinguishedName").get());
    }
}
