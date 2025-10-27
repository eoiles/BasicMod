package GameMaster.cards;

import GameMaster.character.MyCharacter;
import GameMaster.util.CardStats;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class WildCard extends BaseCard {
    public static final String ID = makeID(WildCard.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            1 // cost; upgrade -> 0
    );

    public WildCard() {
        super(ID, info);
        setCostUpgrade(0);
        // IMPORTANT: do not discard this after use — remove it entirely
        this.purgeOnUse = true;     // prevents going to discard/exhaust
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new WildCardChooseFromDeckAction(this.upgraded));
    }

    @Override
    public AbstractCard makeCopy() { return new WildCard(); }
}
