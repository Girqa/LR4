package Behaviours.Producer.FSMSubBehaviours;

import jade.core.behaviours.OneShotBehaviour;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RichesFinalPrice extends OneShotBehaviour {
    @Override
    public void action() {
        log.info( "{} riches final price", getAgent().getLocalName());
    }
}
