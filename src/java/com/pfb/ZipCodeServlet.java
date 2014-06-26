/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pfb;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ctsuser
 */
public class ZipCodeServlet extends HttpServlet {
 private static final String GEO_CODE_SERVER = "http://maps.googleapis.com/maps/api/geocode/json?";
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String zipCode1 = request.getParameter("zipCode1");
        String zipCode2 = request.getParameter("zipCode2");
         String[] result = parseLocation(getLocation(zipCode1));
        String[] result1 = parseLocation(getLocation(zipCode2));

        System.out.println("Latitude of "+ zipCode1 + " is " +result[0]);
        System.out.println("Longitude of " + zipCode1 + " is "+ result[1]);
        System.out.println("Latitude of "+ zipCode1 + " is " +result1[0]);
        System.out.println("Longitude of " + zipCode1 + " is "+ result1[1]);
       
        request.setAttribute("distance", distanceBetweenCodes(Double.valueOf(result[0]),Double.valueOf( result[1]), Double.valueOf(result1[0]), Double.valueOf(result1[1])));
        
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/response.jsp");
            dispatcher.forward(request, response); 
        
    }
    
    // method to get the json response using geoCoding
 private static String getLocation(String code)
    {
        String address = buildUrl(code);

        String content = null;

        try
        {
            URL url = new URL(address);

            InputStream stream = url.openStream();

            try
            {
                int available = stream.available();

                byte[] bytes = new byte[available];

                stream.read(bytes);

                content = new String(bytes);
            }
            finally
            {
                stream.close();
            }

            return (String) content.toString();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

 //method to get the url
    private static String buildUrl(String code)
    {
        StringBuilder builder = new StringBuilder();

        builder.append(GEO_CODE_SERVER);

        builder.append("address=");
        builder.append(code.replaceAll(" ", "+"));
        builder.append("&sensor=false");

        return builder.toString();
    }

    //get the latitude and longitude from the JSON response
    private static String[] parseLocation(String response)
    {
        String[] lines = response.split("\n");

        String lat = null;
        String lng = null;

        for (int i = 0; i < lines.length; i++)
        {
            if ("\"location\" : {".equals(lines[i].trim()))
            {
                lat = getOrdinate(lines[i+1]);
                lng = getOrdinate(lines[i+2]);
                break;
            }
        }

        return new String[] {lat, lng};
    }

    private static String getOrdinate(String s)
    {
        String[] split = s.trim().split(" ");

        if (split.length < 1)
        {
            return null;
        }

        String ord = split[split.length - 1];

        if (ord.endsWith(","))
        {
            ord = ord.substring(0, ord.length() - 1);
        }

        // Check that the result is a valid double
        Double.parseDouble(ord);

        return ord;
    }
    
    //method to get the distance in Km using lat and long of two positions.
    public static String distanceBetweenCodes(
        double lat1, double lng1, double lat2, double lng2) {
    int r = 6371; // average radius of the earth in km
    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lng2 - lng1);
    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
       Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) 
      * Math.sin(dLon / 2) * Math.sin(dLon / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    double d = r * c;
    return String.valueOf(d);
}
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
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
