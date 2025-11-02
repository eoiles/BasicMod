package GameMaster.cards.General;

import GameMaster.cards.BaseCard;
import GameMaster.character.MyCharacter;
import GameMaster.util.CardStats; // as requested
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class EnergyDiminish extends BaseCard {
    public static final String ID = makeID(EnergyDiminish.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1 // base cost (upgrades to 0)
    );

    public EnergyDiminish() {
        super(ID, info);
        setExhaust(true); // Exhaust; effect lasts the rest of combat
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SelectCardsInHandAction(
                1,
                "Choose a card to halve its cost for this combat (banker's rounding).",
                cards -> {
                    for (AbstractCard c : cards) {
                        // Skip X-cost (-1) and unplayable (-2)
                        if (c.cost < 0) { c.flash(); continue; }

                        // Banker's rounding (round-to-even) for cost/2
                        int current = c.cost;
                        int target = (int) Math.rint(current / 2.0);
                        if (target < 0) target = 0;

                        // Persist for the rest of the combat (delta-based)
                        int delta = target - current;
                        if (delta != 0) {
                            c.modifyCostForCombat(delta);
                            c.isCostModified = true;
                            c.flash();
                        }
                    }
                }
        ));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(0);
        }
    }

    @Override
    public AbstractCard makeCopy() { return new EnergyDiminish(); }
}
