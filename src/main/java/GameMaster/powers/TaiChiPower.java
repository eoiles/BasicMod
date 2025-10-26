package GameMaster.powers;

import static GameMaster.BasicMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class TaiChiPower extends BasePower {
    public static final String POWER_ID = makeID("TaiChi");
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    public TaiChiPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        flash();
        if (card.type == AbstractCard.CardType.ATTACK) {
            addToBot(new GainBlockAction(owner, owner, amount));
        } else {
            addToBot(new DamageRandomEnemyAction(
                    new DamageInfo(owner, amount, DamageInfo.DamageType.THORNS),
                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }
    }

    @Override
    public void updateDescription() {
        // Use a single formatted string with two %d placeholders.
        this.description = String.format(DESCRIPTIONS[0], amount, amount);
    }
}
