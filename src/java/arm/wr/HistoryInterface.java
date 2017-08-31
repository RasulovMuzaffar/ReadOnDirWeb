/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arm.wr;

import arm.ent.History;
import arm.ent.Users;

/**
 *
 * @author Muzaffar
 */
public interface HistoryInterface {

    void infoFromSpr(History h);
    
    void sendHist(Users u, int id);
    
    void writeToDB(Users user, String str);

}
