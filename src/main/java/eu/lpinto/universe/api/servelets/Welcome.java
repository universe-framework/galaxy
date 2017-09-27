package eu.lpinto.universe.api.servelets;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import eu.lpinto.universe.util.UniverseFundamentals;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author lpint
 */
@WebServlet(urlPatterns = {"/"})
public class Welcome extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request  servlet request
     * @param response servlet response
     *
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html class='no-js' lang='en'>");
            out.println("<head>");
            out.println("  <meta charset='utf-8'>");
            out.println("  <meta http-equiv='X-UA-Compatible' content='IE=edge'>");
            out.println("  <title>Welcome</title>");
            out.println("  <meta name='viewport' content='width=device-width, initial-scale=1, maximum-scale=1'>");
            out.println("  <link rel='stylesheet' href='http://demo.bonefishcode.com/joanna/demo/flat_constellation_parallax/assets/css/bootstrap.min.css'>");
            out.println("  <link rel='stylesheet' href='http://demo.bonefishcode.com/joanna/demo/flat_constellation_parallax/assets/css/vendor.css'>");
            out.println("  <link rel='stylesheet' href='http://demo.bonefishcode.com/joanna/demo/flat_constellation_parallax/assets/css/theme_dark.css'>");
            out.println("  <link rel='stylesheet' href='http://demo.bonefishcode.com/joanna/demo/flat_constellation_parallax/assets/css/custom.css'>");
            out.println("</head>");
            out.println("<body>");
            out.println("");
            out.println("  <div class='site'>");
            out.println("    <div class='site-loader _multi-circle-line'>");
            out.println("      <div class='site-loader-spinner'></div>");
            out.println("    </div> <!-- .site-loader -->");
            out.println("");
            out.println("    <div class='site-bg'>");
            out.println("      <div class='site-bg-img'></div>");
            out.println("      <div class='site-bg-video'></div>");
            out.println("      <div class='site-bg-overlay'></div>");
            out.println("      <div class='site-bg-effect layer' data-depth='.30'></div> <!-- parallax data, try edit to other number :) -->");
            out.println("      <canvas class='site-bg-canvas layer' data-depth='.30'></canvas> <!-- parallax data, try edit to other number :) -->");
            out.println("      <div class='site-bg-border'></div>");
            out.println("    </div> <!-- .site-bg -->");
            out.println("");
            out.println("    <div class='site-canvas'>");
            out.println("      <header class='site-header'>");
            out.println("        <div class='container'>");
            out.println("          <div class='site-header-header'>");
            out.println("            <div class='site-header-brand'>");
            out.println("              <a href='https://github.com/universe-framework'>");
            out.println("                Universe Framwork");
            out.println("              </a>");
            out.println("            </div>");
            out.println("            <a class='primary-nav-toggle' href='#'>");
            out.println("              <i class='fa fa-navicon'></i>");
            out.println("            </a>");
            out.println("          </div>");
            out.println("        </div>");
            out.println("      </header> <!-- .site-header -->");
            out.println("      <main class='site-main'>");
            out.println("        <div id='nav' class='section'>");
            out.println("          <div class='section-table'>");
            out.println("            <div class='section-table-cell'>");
            out.println("              <div class='container'>");
            out.println("                <div class='col-xs-12'>");
            out.println("                  <div class='col-inner'>");
            out.println("                  </div>");
            out.println("                </div>");
            out.println("              </div>");
            out.println("            </div>");
            out.println("          </div>");
            out.println("        </div> <!-- for `> md` screen, control with `#nav` id -->");
            out.println("        <div id='home' class='section is-active'>");
            out.println("          <div class='section-table'>");
            out.println("            <div class='section-table-cell'>");
            out.println("              <div class='container'>");
            out.println("                <div class='col-xs-12'>");
            out.println("                  <div class='col-inner'>");
            out.println("                    <div>");
            out.println("                      <div class='dash-primary'>");
            out.println("                        <div class='dash days_dash'>");
            out.println("                          <div class='digit'>" + UniverseFundamentals.APP_NAME + "</div>");
            out.println("                        </div>");
            out.println("                      </div>");
            out.println("                    </div> <!-- .countdown -->");
            out.println("                    <p>Project built using the <a href='https://github.com/universe-framework'>Universe Framwork</a>.</p>");
            out.println("                    <p>Running <b>" + UniverseFundamentals.ENV + "</b> enviroment.</p>");
            out.println("                  </div>");
            out.println("                </div>");
            out.println("              </div>");
            out.println("            </div>");
            out.println("          </div>");
            out.println("        </div>");
            out.println("      </main> <!-- .site-main -->");
            out.println("    </div>");
            out.println("  </div>");
            out.println("  <script src='http://demo.bonefishcode.com/joanna/demo/flat_constellation_parallax/assets/js/vendor/jquery-1.11.3.min.js'></script>");
            out.println("  <script src='http://demo.bonefishcode.com/joanna/demo/flat_constellation_parallax/assets/js/vendor/bootstrap.min.js'></script>");
            out.println("  <script src='http://demo.bonefishcode.com/joanna/demo/flat_constellation_parallax/assets/js/vendor/plugin.js'></script>");
            out.println("  <script src='http://demo.bonefishcode.com/joanna/demo/flat_constellation_parallax/assets/js/variable.js'></script>");
            out.println("  <script src='http://demo.bonefishcode.com/joanna/demo/flat_constellation_parallax/assets/js/main.js'></script>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     *
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     *
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
