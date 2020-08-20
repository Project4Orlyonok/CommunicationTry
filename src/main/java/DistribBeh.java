import jade.core.AID;
import jade.core.ServiceException;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.Arrays;

public class DistribBeh extends Behaviour {
    //    int length=;
//    Double[][] price=new Double[4][2];
    double minprice1, minprice2;
    AID agent;
    public int count1 = 0, count2 = 0, kolvo = 4;
    private ArrayList<AID>topicList = new ArrayList<>();
    double pow;


    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchProtocol("NeedAuction");
        ACLMessage receivedMsg = myAgent.receive(mt);
        if (receivedMsg != null) {
//            if (receivedMsg.getProtocol().equals("NeedAuction")) {
                AID sender = receivedMsg.getSender();
                AID topic = createTopic(receivedMsg.getSender().getLocalName() + "t");
                topic = subsTopic(topic.getLocalName());
                topicList.add(topic);
                AID[] resultsAID;
                resultsAID = DfSearch("Generation", receivedMsg);

                for (AID aid : resultsAID) {
                    ACLMessage message = new ACLMessage(ACLMessage.INFORM);//!!!!
                    message.addReceiver(aid);
                    message.setProtocol("Start");
                    message.setContent(topic.getLocalName());
                    myAgent.send(message);
                }

                myAgent.addBehaviour(new RequestBeh(topic, receivedMsg, sender));
//                System.out.println(topicList);
//                if (rMsg != null){
//                    System.out.println(rMsg.getContent() + " ответил " + rMsg.getSender().getLocalName() + " в топик " + topic.getLocalName());
//                    ACLMessage message1 = new ACLMessage(ACLMessage.INFORM);
//                    message1.addReceiver(topic);
//                    message1.setProtocol("Power");
////                System.out.println(receivedMsg.getContent() + " " + receivedMsg.getSender().getLocalName());
//                    message1.setContent(receivedMsg.getContent());
//                    pow = Double.parseDouble(receivedMsg.getContent());
//                    myAgent.send(message1);
//
////                    System.out.println("ok" + sender.getLocalName());
//
////                    myAgent.addBehaviour(new WaitPrice(topic, sender, pow));
//                }
//                else {
//                    block();
//                }
//            }
        } else {
            block();
        }
        }


        @Override
        public boolean done () {
            return false;
        }


        public AID createTopic (String topicName){
            TopicManagementHelper topicHelper;
            AID jadeTopic = null;
            try {
                topicHelper = (TopicManagementHelper)
                        myAgent.getHelper(TopicManagementHelper.SERVICE_NAME);
                jadeTopic = topicHelper.createTopic(topicName);
            } catch (ServiceException e) {
                e.printStackTrace();
            }
            return jadeTopic;
        }

        public AID[] DfSearch (String type, ACLMessage receivedMsg){
            DFAgentDescription dfc = new DFAgentDescription();
            ServiceDescription dfs = new ServiceDescription();
            dfs.setType(type);
            dfc.addServices(dfs);

            DFAgentDescription[] results = new DFAgentDescription[0];
            try {
                results = DFService.search(myAgent, dfc);
            } catch (FIPAException e) {
                e.printStackTrace();
            }
            AID[] resultsAID = new AID[results.length + 1];
            for (int i = 0; i < results.length; i++) {
                resultsAID[i] = results[i].getName();
            }
            resultsAID[resultsAID.length - 1] = receivedMsg.getSender();
            return resultsAID;
        }

        private AID subsTopic (String topicName){
            TopicManagementHelper topicHelper = null;
            AID jadeTopic = null;
            try {
                topicHelper = (TopicManagementHelper)
                        myAgent.getHelper(TopicManagementHelper.SERVICE_NAME);
                jadeTopic = topicHelper.createTopic(topicName);
                topicHelper.register(jadeTopic);
            } catch (ServiceException e) {
                e.printStackTrace();
            }
            return jadeTopic;
        }
    }
