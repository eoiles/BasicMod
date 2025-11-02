package GameMaster.cards.General;

import GameMaster.cards.BaseCard;
import GameMaster.character.MyCharacter;
import GameMaster.util.CardStats;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DamageIncrease extends BaseCard {
    public static final String ID = makeID(DamageIncrease.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1
    );

    public DamageIncrease() {
        super(ID, info);

        setMagic(3,3); // !M! shows 1 in the description
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
    public AbstractCard makeCopy() { return new DamageIncrease(); }
}
