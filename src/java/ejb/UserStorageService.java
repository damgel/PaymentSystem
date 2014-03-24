/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ejb;

import entity.User;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Rhayan
 */
public interface UserStorageService {
    public void registerUser(String firstname, String lastname, String email, String password, 
            Date registrationDate, Date lastVisit);
          
    public List<User> getUserList();
}
