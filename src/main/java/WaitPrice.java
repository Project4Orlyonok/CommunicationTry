import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WaitPrice extends Behaviour {
    private double minprice1, minprice2;
    private AID agent;
    private int count1 = 0, count2 = 0, kolvo = 1;
    private AID topic;
    private AID sender;
    private double pow;
    private boolean stop = false;

    public WaitPrice(AID topic, AID sender, double pow) {
        this.topic = topic;
        this.sender = sender;
        this.pow = pow;
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchProtocol("Price"),
                MessageTemplate.MatchTopic(topic));
        ACLMessage AuctMsg = myAgent.receive(mt);
        if (AuctMsg != null) {
            System.out.println(sender.getLocalName() + "  " + AuctMsg.getContent() + " " + topic.getLocalName());
            System.out.println();
            if (AuctMsg.getContent().equals("Left")) kolvo--;
            else {
                if (count1 == 0) {
                    minprice1 = Double.parseDouble(AuctMsg.getContent());
                    agent = AuctMsg.getSender();
                } else {
                    if (minprice1 > (Double.parseDouble(AuctMsg.getContent()))) {
                        minprice1 = (Double.parseDouble(AuctMsg.getContent()));
                        agent = AuctMsg.getSender();
                    }
                }
                count1++;
                if (kolvo == 0) {
                    ACLMessage EndMsg = new ACLMessage(ACLMessage.INFORM);
                    EndMsg.addReceiver(sender);
                    EndMsg.setProtocol("End");
                    EndMsg.setContent("No");
                    myAgent.send(EndMsg);
                    System.out.println(EndMsg);
                } else {
                    if (count1 == kolvo) {
                        System.out.println(minprice1 + "   dis    "+ agent.getLocalName());
                        ACLMessage WinMsg = new ACLMessage(ACLMessage.INFORM);
                        WinMsg.setContent(String.valueOf(pow));
                        WinMsg.setProtocol("Winner");
                        WinMsg.addReceiver(agent);
                        myAgent.send(WinMsg);
                        count1 = 0;
                        kolvo = 4;

                        ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
                        aclMessage.addReceiver(sender);
                        aclMessage.setProtocol("End");
                        aclMessage.setContent(String.valueOf(minprice1));
                        myAgent.send(aclMessage);
                        stop = true;
                    }
                }
            }
        }else {
            block();
        }
    }

    @Override
    public boolean done() {
        return stop;
    }
}
