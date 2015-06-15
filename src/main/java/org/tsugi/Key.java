
package org.tsugi;

/**
 * This is a class to provide access to the key data.
 *
 * This could be the tenant data if there is one key per tenant.
 * But as LTI 2.0 comes out and if LMS's give instructors "install" 
 * permission, the scope of a key might be pretty small.
 */
public interface Key {

    // TODO: - should keys have a default language??

    /**
     * Get the launch associated with this object
     */
    public Launch getLaunch();

    /**
     * The integer primary key for this key across this Tsugi instance
     */
    public Long getId();

    /**
     * The key title
     */
    public String getTitle();

}
