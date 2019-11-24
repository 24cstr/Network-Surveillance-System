/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server8;

import java.util.Random;

public class passGen{
    //Verison 1.0
    private static final String dCase = "abcdefghijklmnopqrstuvwxyz";
    private static final String uCase = "ABCDEFGHIJKLOMNOPQRSTUVWXYZ";
    private static final String sChar = "!@#$%^&*<>";
    private static final String intChar = "0123456789";
    private static Random r = new Random();
    private static String pass = "";

    public static String gens () {
        System.out.println ("Generating pass...");
        while (pass.length () != 5){
            int rPick = r.nextInt(4);
            if (rPick == 0){
                int spot = r.nextInt(dCase.length());
                pass += dCase.charAt(spot);
            } else if (rPick == 1) {
                int spot = r.nextInt (uCase.length());
                pass += uCase.charAt(spot);
            } else if (rPick == 2) {
                int spot = r.nextInt (7);
                pass += sChar.charAt(spot);
            } else if (rPick == 3){
                int spot = r.nextInt (10);
                pass += intChar.charAt (spot);
            }
        }
        System.out.println ("Generated Pass: " + pass);
        return(pass);
    }
}