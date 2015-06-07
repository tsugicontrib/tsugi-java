
package org.tsugi;

/**
 * This an opinionated LTI class that defines how Tsugi tools interact with LTI
 *
 * This class deals with all of the session and database/data model
 * details that Tsugi tools make use of during runtime.  This makes use of the
 * lower level \Tsugi\Util\LTI class which is focused on
 * meeting the protocol requirements.
 * Most tools will not use LTI at all - just LTIX.
 */

public interface LTIX {

   /**
     * Pull out a custom variable from the LTIX session. Do not
     * include the "custom_" prefix - this is automatic.
     */

    public String customGet(String varname, String def);

}
