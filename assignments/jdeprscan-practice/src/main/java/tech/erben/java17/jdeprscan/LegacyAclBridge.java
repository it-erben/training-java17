package tech.erben.java17.jdeprscan;

import java.security.acl.Acl;

public class LegacyAclBridge {

    private final Acl acl;

    public LegacyAclBridge(Acl acl) {
        this.acl = acl;
    }

    public boolean hasEntries() {
        return acl != null && acl.entries().hasMoreElements();
    }

    public Acl acl() {
        return acl;
    }
}
