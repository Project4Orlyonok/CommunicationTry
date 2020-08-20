import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class RequestBeh extends Behaviour {
    private AID topic;
    private ACLMessage receivedMsg;
    private AID sender;
    private boolean stop;

    public RequestBeh(AID topic, ACLMessage receivedMsg, AID sender) {
        this.topic = topic;
        this.receivedMsg = receivedMsg;
        this.sender = sender;
    }

    @Override
    public void action() {
        MessageTemplate mt1 = MessageTemplate.MatchProtocol("Access");
        ACLMessage rMsg = myAgent.receive(mt1);
        if (rMsg != null){
            System.out.println(rMsg.getContent() + " ответил " + rMsg.getSender().getLocalName() + " в топик " + topic.getLocalName());
            ACLMessage message1 = new ACLMessage(ACLMessage.INFORM);
            message1.addReceiver(topic);
            message1.setProtocol("Power");
//                System.out.println(receivedMsg.getContent() + " " + receivedMsg.getSender().getLocalName());
            message1.setContent(receivedMsg.getContent());
            double pow = Double.parseDouble(receivedMsg.getContent());
            myAgent.send(message1);
            myAgent.addBehaviour(new WaitPrice(topic, sender, pow));
            stop = true;
//                    System.out.println("ok" + sender.getLocalName());
        }else {
            block();
        }
    }

    @Override
    public boolean done() {
        return stop;
    }
}
