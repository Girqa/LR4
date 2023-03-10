package AdditionalClasses;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.messaging.TopicManagementHelper;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Класс предоставляющий готовые шаблоны из лекций по JADE
 */
public final class JadePatternProvider {
    private JadePatternProvider(){}  // Запретили создание экземпляров класса

    /**
     * Шаблон создания и подключения агента к топику
     * @param agent
     * @param topic
     * @return AID топика
     */
    public static AID connectToTopic (Agent agent, String topic) {
        TopicManagementHelper topicHelper = null;
        AID jadeTopic = null;
        try {
            topicHelper = (TopicManagementHelper) agent.getHelper(TopicManagementHelper.SERVICE_NAME);
            jadeTopic = topicHelper.createTopic(topic);
            topicHelper.register(jadeTopic);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return jadeTopic;
    }

    public static void disconnectFromTopic(Agent agent, AID jadeTopic) {
        TopicManagementHelper topicHelper = null;
        try {
            topicHelper = (TopicManagementHelper) agent.getHelper(TopicManagementHelper.SERVICE_NAME);
            topicHelper.deregister(jadeTopic);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    public static void registerYellowPage(Agent agent, String type) {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(agent.getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType(type);
        sd.setName(agent.getLocalName());
        dfd.addServices(sd);
        try {
            DFService.register(agent, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    public static List<AID> getServiceProviders(Agent agent, String service) {
        List<AID> agents = new ArrayList<>();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(service);
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.addServices(sd);
        try {
            DFAgentDescription[] result = DFService.search(agent, dfd);
            Arrays.stream(result).forEach(a -> agents.add(a.getName()));
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        return agents;
    }

}
