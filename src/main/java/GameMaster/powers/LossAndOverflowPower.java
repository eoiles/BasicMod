package GameMaster.powers;

import static GameMaster.BasicMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class LossAndOverflowPower extends BasePower {
    public static final String POWER_ID = makeID("LossAndOverflow");
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    private int cap; // independent of this.amount; used for logic

    public LossAndOverflowPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
        this.cap = Math.max(0, amount);
        this.amount = this.cap; // display matches cap
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        this.cap += stackAmount;
        this.amount = this.cap;
        updateDescription();
    }

    @Override
    public void reducePower(int reduceAmount) {
        super.reducePower(reduceAmount);
        this.cap = Math.max(0, this.cap - reduceAmount);
        this.amount = this.cap;
        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw() {
        addToBot(new AbstractGameAction() {
            @Override public void update() {
                int sum = 0;
                if (owner instanceof AbstractPlayer) {
                    for (AbstractCard c : ((AbstractPlayer) owner).hand.group) {
                        int cost = c.costForTurn;
                        if (cost < 0) cost = 0; // X/Unplayable -> 0
                        sum += cost;
                    }
                }
                int x = Math.min(cap, sum);
                int gain = cap - x;
                if (gain > 0) {
                    flash();
                    addToTop(new ApplyPowerAction(owner, owner, new StrengthPower(owner, gain), gain));
                }
                isDone = true;
            }
        });
    }

    @Override
    public void updateDescription() {
        // Use a single arg twice in JSON with %1$d, or pass two args here:
        this.description = String.format(DESCRIPTIONS[0], cap, cap);
    }
}
