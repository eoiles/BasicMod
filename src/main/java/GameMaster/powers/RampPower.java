package GameMaster.powers;

import static GameMaster.BasicMod.makeID;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class RampPower extends BasePower {
    public static final String POWER_ID = makeID("Ramp");
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    public RampPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        if (owner instanceof AbstractPlayer) {
            AbstractPlayer p = (AbstractPlayer) owner;
            p.energy.energyMaster += amount;          // raise max for this combat
            addToTop(new GainEnergyAction(amount));    // feel it immediately this turn
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (owner instanceof AbstractPlayer) {
            AbstractPlayer p = (AbstractPlayer) owner;
            p.energy.energyMaster += stackAmount;      // raise max further
            addToTop(new GainEnergyAction(stackAmount));
        }
        updateDescription();
    }

    @Override
    public void onRemove() {
        // Revert when the power is actually removed (do not also do this in onVictory()).
        if (owner instanceof AbstractPlayer) {
            AbstractPlayer p = (AbstractPlayer) owner;
            p.energy.energyMaster = Math.max(0, p.energy.energyMaster - amount);
        }
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], amount);
    }
}
