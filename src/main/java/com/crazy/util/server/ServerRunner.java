package com.crazy.util.server;

import com.crazy.core.BMDataContext;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.crazy.util.server.handler.GameEventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component  
public class ServerRunner implements CommandLineRunner {  
    private final SocketIOServer server;
    private final SocketIONamespace gameSocketNameSpace ;
    
    @Autowired  
    public ServerRunner(SocketIOServer server) {  
        this.server = server;  
        gameSocketNameSpace = server.addNamespace(BMDataContext.NameSpaceEnum.GAME.getNamespace())  ;
    }
    
    @Bean(name="gameNamespace")
    public SocketIONamespace getGameSocketIONameSpace(SocketIOServer server ){
    	gameSocketNameSpace.addListeners(new GameEventHandler(server));
    	return gameSocketNameSpace  ;
    }
    
    public void run(String... args) throws Exception { 
        server.start();  
        BMDataContext.setIMServerStatus(true);	//IMServer 启动成功
    }  
}  