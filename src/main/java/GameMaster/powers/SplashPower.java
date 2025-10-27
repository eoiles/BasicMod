package GameMaster.powers;

import static GameMaster.BasicMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SplashPower extends BasePower {
    public static final String POWER_ID = makeID("Splash");
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    // We only splash during damage caused by a played Attack this turn.
    private boolean trackingAttack = false;

    public SplashPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount); // amount = percent (e.g., 50)
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, com.megacrit.cardcrawl.actions.utility.UseCardAction action) {
        trackingAttack = (card.type == AbstractCard.CardType.ATTACK);
    }

    @Override
    public void onAfterUseCard(AbstractCard card, com.megacrit.cardcrawl.actions.utility.UseCardAction action) {
        trackingAttack = false;
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (!trackingAttack) return;
        if (info == null || info.type != DamageInfo.DamageType.NORMAL) return;
        if (damageAmount <= 0 || target == null) return;
        if (info.owner != this.owner) return;

        int splash = (int)Math.floor(damageAmount * (amount / 100.0));
        if (splash <= 0) return;

        // Use THORNS so our mirrored hits don't recursively trigger this power.
        for (AbstractCreature mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mo == target || mo.isDeadOrEscaped()) continue;
            addToBot(new DamageAction(
                    mo,
                    new DamageInfo(owner, splash, DamageInfo.DamageType.THORNS),
                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
            ));
        }
        flash();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], amount);
    }
}
