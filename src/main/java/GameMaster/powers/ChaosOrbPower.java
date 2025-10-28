package GameMaster.powers;

import static GameMaster.BasicMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class ChaosOrbPower extends BasePower {
    public static final String POWER_ID = makeID("ChaosOrb");
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    public ChaosOrbPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount); // amount = debuffs per Attack
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type != AbstractCard.CardType.ATTACK) return;

        // Primary target if present; otherwise pick a random living enemy
        AbstractMonster target = null;
        if (action != null && action.target instanceof AbstractMonster) {
            target = (AbstractMonster) action.target;
        }
        if (target == null || target.isDeadOrEscaped()) {
            target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        }
        if (target == null || target.isDeadOrEscaped()) return;

        flash();
        for (int i = 0; i < amount; i++) {
            applyRandomDebuff(target);
        }
    }

    private void applyRandomDebuff(AbstractMonster m) {
        int roll = AbstractDungeon.cardRandomRng.random(0, 3); // 4 options
        switch (roll) {
            case 0: // Weak 1
                addToBot(new ApplyPowerAction(m, owner, new WeakPower(m, 1, false), 1));
                break;
            case 1: // Vulnerable 1
                addToBot(new ApplyPowerAction(m, owner, new VulnerablePower(m, 1, false), 1));
                break;
            case 2: // Poison 1
                addToBot(new ApplyPowerAction(m, owner, new PoisonPower(m, owner, 1), 1));
                break;
            case 3: // Constricted 1
            default:
                addToBot(new ApplyPowerAction(m, owner, new ConstrictedPower(m, owner, 1), 1));
                break;
        }
    }

    @Override
    public void updateDescription() {
        // "Apply a random debuff" x amount; use %d for amount
        if (amount == 1) {
            this.description = DESCRIPTIONS[0];
        } else {
            this.description = String.format(DESCRIPTIONS[1], amount);
        }
    }
}
