/* 
 * Copyright (C) 2018-25 by Øyvind Hanssen (ohanssen@acm.org)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
 
package no.arctic.core.auth;
import no.arctic.core.*; 
import no.arctic.core.httpd.*;
import java.util.*; 
import java.io.Serializable;


/**
 * User info that can be stored on file. 
 */
public abstract class User {

    private String userid; 
    private String name = "";
    private String callsign = "";
    
    public String  getIdent()             { return userid; }
    public void    setName(String n)      { name = n; }
    public String  getName()              { return name; }
    public void    setCallsign(String c)  { callsign = c.toUpperCase(); }
    public String  getCallsign()          { return callsign; }
    
    public abstract Date    getLastUsed();
    public abstract void    setLastUsed(Date d);
    public abstract void    updateTime();
    public abstract void    setPasswd(String pw);
    
    /* 
     * Group membership and authorisations
     * These attrs are now stored in this class. 
     */
    private Group group = Group.DEFAULT;
    private Group altgroup = Group.DEFAULT;
    
    private boolean admin=false;
    private boolean suspended = false; 
    private String trackerAllowed = "";
    
    public boolean isOperator()                 { return group.isOperator(); }
    public final Group getGroup()               { return group; }
    public final void setGroup(Group g)         { group = g; } 
    public final Group getAltGroup()            { return altgroup; }
    public final void setAltGroup(Group g)      { altgroup = g; } 
    
    public boolean isAdmin()                    { return admin; }
    public final void setAdmin(boolean a)       { admin=a; }
    public final boolean isSuspended()          { return suspended; }
    public final void setSuspended(boolean s)   { suspended = s; }
    public String getAllowedTrackers()          { return trackerAllowed; }
    public void setAllowedTrackers(String expr) { trackerAllowed = expr; }
    
    public boolean roleAllowed(Group role) {
        return (admin || role==group || role == altgroup || role == Group.DEFAULT);
    }
    
    protected User(String id)
        { userid=id; }
        
    public User() 
        { }
    
        
}
