import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ConsumerBeh extends TickerBehaviour {
    public Time time;
    private Const poww = new Const();

    //    List<Double> pow=Arrays.asList(1.0,2.4,3.7,4.0,5.8);
    public ConsumerBeh(Agent a, long period, Time time) {
        super(a, period);
        this.time = time;
        poww.setPow();
    }

    @Override
    protected void onTick() {

        double pow = poww.pow(time.getCurrentTime());
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setContent(String.valueOf(pow));
        System.out.println(myAgent.getLocalName() + " запросил " + pow);
        message.setProtocol("NeedAuction");
        message.addReceiver(myAgent.getAID("Distributor"));
        myAgent.send(message);
        myAgent.addBehaviour(new Behaviour() {
            boolean stop = false;

            @Override
            public void action() {
                MessageTemplate mt = MessageTemplate.MatchProtocol("End");
                ACLMessage receivedMsg = myAgent.receive(mt);
                if (receivedMsg != null) {
                    System.out.println("Deal " + myAgent.getLocalName() + ": " + receivedMsg.getContent());
                    stop = true;
                }
                block();
            }

            @Override
            public boolean done() {
                return stop;
            }
        });
//        ожидание сообщ от дистр о вышло/не вышло
    }
}
