
package org.tsugi;

import org.tsugi.*;
import java.util.Properties;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// TODO: Add documentation here

public interface Output {

    public final String SESSION_SUCCESS = "tsugi::session::success";
    public final String SESSION_ERROR = "tsugi::session::error";

    public void flashSuccess(String message);

    public void flashError(String message);

    public void flashMessages(PrintWriter out);

}
