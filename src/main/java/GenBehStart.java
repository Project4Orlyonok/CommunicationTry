import jade.core.AID;
import jade.core.ServiceException;
import jade.core.behaviours.Behaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class GenBehStart extends Behaviour {
    boolean flag = false;
    private AID topic;
    private Time time;
    private GenInf power = new GenInf();

    public GenBehStart(Time time) {
        this.time = time;
        power.setPri();
        power.setPow();
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchProtocol("Start");
        ACLMessage receivedMsg = myAgent.receive(mt);
        if (receivedMsg != null) {
            topic = subsTopic(receivedMsg.getContent());
            System.out.println(myAgent.getLocalName() + " подписался на " + topic.getLocalName());

            ACLMessage reply = receivedMsg.createReply();
//            reply.addReceiver(topic);
            reply.setPerformative(ACLMessage.INFORM);
            reply.setProtocol("Access");
            reply.setContent(topic.getLocalName() + " ok");
            myAgent.send(reply);
//            System.out.println(reply);

            myAgent.addBehaviour(new GenPrice(time, topic));

//            MessageTemplate mt2 = MessageTemplate.and(MessageTemplate.MatchProtocol("Power"),
//                    MessageTemplate.MatchTopic(topic));
//            ACLMessage receivedMsg2 = myAgent.receive(mt2);
//            if ((receivedMsg2 != null)) {
//                System.out.println(receivedMsg2.getContent());
////                if (Double.parseDouble(receivedMsg2.getContent()) <= power.pow(time.getCurrentTime())) {
//                    ACLMessage message = new ACLMessage(ACLMessage.INFORM);
//                    message.addReceiver(topic);
//                    message.setProtocol("Price");
//                    message.setContent(String.valueOf(power.pri(time.getCurrentTime())));
//                    myAgent.send(message);
////                }
//            }else {
//                block();
//            }
//            MessageTemplate mt3 = MessageTemplate.and(MessageTemplate.MatchProtocol("Winner"),
//                    MessageTemplate.MatchTopic(topic));
//            ACLMessage receivedMsg3 = myAgent.receive(mt3);
//            if (receivedMsg3 != null) {
////                System.out.println("ok");
////                power.powmin(time.getCurrentTime(), Double.parseDouble(receivedMsg2.getContent()));
//
//            }else {
//                block();
//            }
        }else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }


    private AID subsTopic(String topicName) {
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
