package GameMaster.cards;

import GameMaster.character.MyCharacter;
import GameMaster.util.CardStats;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DamageMagnify extends BaseCard {
    public static final String ID = makeID(DamageMagnify.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1 // cost
    );

    private static final int MULTIPLIER = 10;

    public DamageMagnify() {
        super(ID, info);
        setMagic(MULTIPLIER, 0);   // how much to multiply damage by
        setExhaust(true);          // one-and-done
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SelectCardsInHandAction(
                1,
                "Choose a card with damage.",
                // Only allow cards that actually have baseDamage defined (>= 0)
                c -> c.baseDamage >= 0,
                cards -> {
                    for (AbstractCard c : cards) {
                        c.baseDamage += magicNumber;
                        c.applyPowers();
                        c.flash();
                    }
                }
        ));
    }

    @Override
    public AbstractCard makeCopy() { return new DamageMagnify(); }
}
