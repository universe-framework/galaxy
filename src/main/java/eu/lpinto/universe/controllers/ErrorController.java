//package eu.lpinto.universe.controllers;
//
//import eu.lpinto.universe.util.UniverseFundamentals;
//import javax.ejb.EJB;
//import javax.ejb.Singleton;
//
///**
// *
// * @author Luis Pinto <code>- mail@lpinto.eu</code>
// */
//@Singleton
//public class ErrorController {
//
//    @EJB
//    private EmailController emailController;
//
//    public void report(final Throwable message) {
//
//        String body = ""
//                      + "<HTML>"
//                      + "<HEAD>"
//                      + "</HEAD>"
//                      + "<BODY>"
//                      + "<H1>" + UniverseFundamentals.APP_NAME + "</H1>"
//                      + "<H2>Exception report</H2>"
//                      + "<H3> Error: <small>" + message.getMessage() + "</small></H3>"
//                      message.
//                      + "</BODY>"
//                      + "</HTML>";
//
//        emailController.sendEmail(UniverseFundamentals.ERRORS_EMAIL_ADDR, UniverseFundamentals.APP_NAME + " : Error rport", message);
//    }
//}
