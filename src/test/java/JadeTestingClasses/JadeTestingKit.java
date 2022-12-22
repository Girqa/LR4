package JadeTestingClasses;


import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.core.behaviours.Behaviour;
import jade.util.leap.Properties;
import jade.wrapper.*;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;


public class JadeTestingKit {
    private AgentContainer mainContainer;
    private List<AgentController> controllers = new ArrayList<>();

    public void startJade(){
        Properties props = new Properties();
        ProfileImpl p = new ProfileImpl(props);
        p.setParameter("services", "jade.core.messaging.TopicManagementService");
        Runtime.instance().setCloseVM(true);
        mainContainer = Runtime.instance().createMainContainer(p);

    }

    @SneakyThrows
    public void createAgent(String agentName, Behaviour ... behs){
        AgentController newAgent = mainContainer.createNewAgent(agentName, MockAgent.class.getName(), behs);
        controllers.add(newAgent);
        newAgent.start();
    }
    @SneakyThrows
    public void killAgents() {
        if (controllers.isEmpty()) return;
        for(AgentController controller: controllers) {
            controller.kill();
        }
        controllers.clear();
    }
}
