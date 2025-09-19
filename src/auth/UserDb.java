/* 
 * Copyright (C) 2021-2025 by Øyvind Hanssen (ohanssen@acm.org)
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


/**
 * Interface to user database. Can be implemented by using a local file or 
 * some database system. 
 */
 
public interface UserDb extends ServerConfig.UserDb
{

    /**
     * Syncer interface. Can be used by implmentations for synchronizing user
     * data between server instances. Used in dbsync (database plugin)
     */
    public interface Syncer {
        void updateTs(String id, Date ts);
        void add(String userid, Object obj );
        void update(String userid, Object obj);
        void remove(String userid);
    }
    
    
    
    public static class DummySyncer implements Syncer {
        
        public void updateTs(String id, Date ts)
          {}
        public void add(String userid, Object obj)
          {}
        public void update(String userid, Object obj)
          {}
        public void remove(String userid)
          {}
    }
    


    User get(String id); 
    Collection<User> getAll();
    
    User add (String userid);
    User add (String userid, String name, boolean admin, boolean suspend, String passwd, String grp);
    User add (String userid, String name, boolean admin, boolean suspend, String passwd, String grp, String agrp);
    void remove(String username);
    
    /* Should there be a default implementation of this? */
    boolean updatePasswd(String username, String passwd);
    
    Syncer getSyncer();
    void setSyncer(Syncer syncer);
    
    GroupDb getGroupDb();
}

