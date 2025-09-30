/* 
 * Copyright (C) 2022-25 by LA7ECA, Øyvind Hanssen (ohanssen@acm.org)
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
 
package no.polaric.core.httpd;
import no.polaric.core.*;
import java.util.*;
import io.javalin.websocket.*; 




public class NodeWsApi<T> {

    private NodeWs _children;
    private String _nodeid;
    private Class<T> _cls;
    private Handler<String> _handler;
    private Handler<T> _chandler;
    private Map<String, NodeWsClient> _servers;
    private ServerConfig _conf;
    
    public interface Handler<T> {
        public void recv(String nodeid, T obj);
    }

    

    @SuppressWarnings("unchecked")
    public NodeWsApi(ServerConfig conf, String nodeid, NodeWs srv, Class<T> cls) {
        _servers = new HashMap<String,NodeWsClient>();
        _children = srv;
        _nodeid = nodeid;
        _cls = cls;
        _conf = conf;
        
        _handler = new Handler<String>() {
            public void recv(String nodeid, String obj) {
                _conf.log().debug("NodeWsApi", "Received message from: "+nodeid);
                if (_chandler != null)
                    _chandler.recv(nodeid, (T) ServerBase.fromJson(obj, _cls));
            }
        };
        
        if (_children != null)
            _children.setHandler(_handler);
    }
    
    
    
    public List<String> getNodes() {
        List<String> list = new ArrayList<String>();
        for (String x : _servers.keySet())
            list.add(x);
        for (String x : _children.getSubscribers())
            list.add(x);
        return list;
    }
    
    
    
    
    public void addServer(String nodeid, NodeWsClient srv) {
        _servers.put(nodeid, srv);
        srv.subscribe(_nodeid);
        srv.setHandler(_handler);
    }
    
    
    
    public void rmNode(String nodeid) {
        var srv = _servers.get(nodeid);
        if (srv != null) {
            srv.unsubscribe();
            srv.close();
        }
        _servers.remove(nodeid);
        _children.removeSubscriber(nodeid);
    }
    
    
    
    public void setHandler(Handler<T> h) {
        _chandler = h;
    }
    
    
    /** Post a object to the connected node (JSON encoded) */
    public boolean put(String nodeid, T obj) {
        NodeWsClient srv = _servers.get(nodeid);
        if (srv != null && srv.isConnected())
            /* Send to parent node if it exists */
            return srv.put(obj);
        else
            /* Send to child node */
            return _children.put(nodeid, obj);
    }
    
    
    
    
    public boolean isConnected(String nodeid) {
        NodeWsClient s = _servers.get(nodeid);
        return ( (s != null && s.isConnected())  
            || _children.getSubscribers().contains(nodeid) );
    }
    
    
}



