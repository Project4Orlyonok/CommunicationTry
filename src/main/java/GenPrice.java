import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class GenPrice extends Behaviour {
    private GenInf power = new GenInf();
    private Time time;
    private AID topic;
    private boolean stop = false;

    public GenPrice(Time time, AID topic) {
        this.time = time;
        this.topic = topic;
        power.setPri();
        power.setPow();
    }

    @Override
    public void action() {
        MessageTemplate mt2 = MessageTemplate.and(MessageTemplate.MatchProtocol("Power"),
                MessageTemplate.MatchTopic(topic));
        ACLMessage receivedMsg2 = myAgent.receive(mt2);
        if ((receivedMsg2 != null)) {
            System.out.println(receivedMsg2.getContent());
//                if (Double.parseDouble(receivedMsg2.getContent()) <= power.pow(time.getCurrentTime())) {
            ACLMessage message = new ACLMessage(ACLMessage.INFORM);
            message.addReceiver(topic);
            message.setProtocol("Price");
            message.setContent(String.valueOf(power.pri(time.getCurrentTime())));
            myAgent.send(message);
            myAgent.addBehaviour(new ContractBeh(topic));
            stop = true;
//                }
        }else {
            block();
        }
    }

    @Override
    public boolean done() {
        return stop;
    }
}
