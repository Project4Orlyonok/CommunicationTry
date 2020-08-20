import jade.core.Service;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Register;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

public class Agent extends jade.core.Agent {
    protected void setup() {
        Time time = new Time();
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                System.out.println(myAgent.getLocalName() + " was born");
            }
        });
        switch (getLocalName()) {
            case "Sun":
            case "Wind":
            case "Heat":
            case "System": {
                dfRegister();
                addBehaviour(new GenBehStart(time));
                break;
            }
            case "Distributor": {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                addBehaviour(new DistribBeh());
                break;
            }
            case "Consumer1" : {
                addBehaviour(new ConsumerBeh(this, 5000, time));
                break;
            }
            case  "Consumer2": {
                addBehaviour(new ConsumerBeh(this, 5000, time));
                break;
            }
        }
//
    }

    private void dfRegister() {
        DFAgentDescription dfc = new DFAgentDescription();
        dfc.setName(getAID());
        ServiceDescription dfs = new ServiceDescription();
        dfs.setName("Generation" + getLocalName());
        dfs.setType("Generation");
        dfc.addServices(dfs);
        try {
            DFService.register(this, dfc);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }
}
