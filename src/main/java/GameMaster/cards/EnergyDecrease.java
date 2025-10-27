package GameMaster.cards;

import GameMaster.character.MyCharacter;
import GameMaster.util.CardStats;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static GameMaster.BasicMod.makeID;

public class EnergyDecrease extends BaseCard {
    public static final String ID = makeID(EnergyDecrease.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1
    );

    public EnergyDecrease() {
        super(ID, info);
        setMagic(1,1); // <-- ensures !M! shows 1
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SelectCardsInHandAction(
                1,
                "Choose a card to reduce its cost by " + magicNumber + " for this combat.",
                cards -> {
                    for (AbstractCard c : cards) {
                        if (c.cost >= 0) {        // ignore X-cost (-1) and unplayable (-2)
                            c.updateCost(-magicNumber);
                            c.isCostModified = true;
                        }
                        c.flash();
                    }
                }
        ));
    }

    @Override
    public AbstractCard makeCopy() { return new EnergyDecrease(); }
}
