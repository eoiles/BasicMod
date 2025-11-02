package GameMaster.cards.General;

import GameMaster.cards.BaseCard;
import GameMaster.character.MyCharacter;
import GameMaster.util.CardStats;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Chance extends BaseCard {
    public static final String ID = makeID(Chance.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1 // cost
    );

    private static final int DRAW = 2;
    private static final int UPG_DRAW = 1; // 2 -> 3

    public Chance() {
        super(ID, info);
        setMagic(DRAW, UPG_DRAW); // !M! shows 2 (then 3 when upgraded)
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DiscardAction(p, p, 1, false));   // player chooses which card to discard
        addToBot(new DrawCardAction(p, magicNumber));  // then draw
    }

    @Override
    public AbstractCard makeCopy() { return new Chance(); }
}
