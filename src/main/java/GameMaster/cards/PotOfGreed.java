package GameMaster.cards;

import GameMaster.character.MyCharacter;
import GameMaster.util.CardStats;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PotOfGreed extends BaseCard {
    public static final String ID = makeID(PotOfGreed.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,     // keep it rare; 0-cost draw 2 is spicy
            CardTarget.SELF,
            0                    // cost
    );

    private static final int DRAW = 2;
    private static final int UPG_DRAW = 1;    // upgrade -> draw 3

    public PotOfGreed() {
        super(ID, info);
        setMagic(DRAW, UPG_DRAW); // !M! shows 2 (then 3 when upgraded)
        setExhaust(true);         // avoid loops
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(p, magicNumber));
    }

    @Override
    public AbstractCard makeCopy() { return new PotOfGreed(); }
}
