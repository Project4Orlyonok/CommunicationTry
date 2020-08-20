import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ContractBeh extends Behaviour {
    private AID topic;
    private boolean stop = false;

    public ContractBeh(AID topic) {
        this.topic = topic;
    }

    @Override
    public void action() {
        MessageTemplate mt3 = MessageTemplate.and(MessageTemplate.MatchProtocol("Winner"),
                MessageTemplate.MatchTopic(topic));
        ACLMessage receivedMsg3 = myAgent.receive(mt3);
        if (receivedMsg3 != null) {
                System.out.println("ok " + receivedMsg3.getContent());
//                power.powmin(time.getCurrentTime(), Double.parseDouble(receivedMsg2.getContent()));
                stop = true;
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return stop;
    }
}
