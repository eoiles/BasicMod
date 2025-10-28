package GameMaster.powers;

import static GameMaster.BasicMod.makeID;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class RampPower extends BasePower {
    public static final String POWER_ID = makeID("Ramp");
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    public RampPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        addToBot(new GainEnergyAction(amount)); // extra energy every turn
        flash();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount); // stacks = more energy each turn
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], amount);
    }
}
